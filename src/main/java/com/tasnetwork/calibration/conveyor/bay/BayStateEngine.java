package com.tasnetwork.calibration.conveyor.bay;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class BayStateEngine extends TimerTask {
    public static Logger logger = Logger.getLogger(BayStateEngine.class.getPackage().getName());

    private final String bayKey;
    private final BayStateContext context;
    private volatile boolean stopRequested = false;
    private volatile boolean runOnceRequested = false;
    private volatile boolean isFinished = false;
    private volatile Thread runningThread = null;
    private String executionMode = ConstantStateModes.RUN;

    public BayStateEngine(String bayKey, BayStateContext context) {
        this.bayKey = bayKey;
        this.context = context;
        this.executionMode = ConstantStateModes.RUN;
    }

    public BayStateEngine(String bayKey, BayStateContext context, String executionMode) {
        this.bayKey = bayKey;
        this.context = context;
        this.executionMode = executionMode;
    }

    public void requestStopProcess() {
        requestStop();
    }

    public void requestRunOnce() {
        this.runOnceRequested = true;
    }

    @Override
    public void run() {
        this.runningThread = Thread.currentThread();
        this.isFinished = false;
        logger.debug("BayStateEngine : Entry for BayKey: " + bayKey);
        try {
            manageBayStates();
        } finally {
            this.isFinished = true;
            this.runningThread = null;
        }
    }

    private void manageBayStates() {
        logger.debug("BayStateEngine : manageBayStates : Entry for BayKey: " + bayKey);

        ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
                .findByBayKeyAndExecutionMode(bayKey, executionMode);

        if (statePlanner == null || statePlanner.isEmpty()) {
            logger.debug("BayStateEngine : No states found in the planner for BayKey: " + bayKey);
            return;
        }

        int currentIndex = 0; // Start from the first row
        
        StateFlow presentRow = statePlanner.get(currentIndex);
        StateFlow nextRow = presentRow;
        String currentStateName = presentRow.getState();
        
        // Initialize the first state
        context.setNextState(currentStateName, "");

        logger.debug("BayStateEngine : Size : " + statePlanner.size());

        context.onStartComplete();

        while (!stopRequested && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Thread.currentThread().isInterrupted()) {
            // Process the current state
            BayResponse bayStatus = context.processCurrentState();

            presentRow = nextRow;

            if (bayStatus.isStatus()) {
                // SUCCESS
                String nextStateName = presentRow.getIfSuccess();

                // If Run Once was requested and we have looped back to the start state, stop the engine gracefully.
                if (runOnceRequested && nextStateName != null && nextStateName.startsWith("S01_")) {
                    logger.info("BayStateEngine : Run Once requested and loop reached start state. Stopping engine for BayKey: " + bayKey);
                    stopRequested = true;
                    runOnceRequested = false;
                }

                if (nextStateName != null && !nextStateName.isEmpty()) {
                    context.setNextState(nextStateName, "");
                }

                boolean foundNextState = false;
                for (StateFlow row : statePlanner) {
                    if (row.getState().equals(nextStateName)) {
                        nextRow = row;
                        foundNextState = true;
                        break;
                    }
                }
                
                if (!foundNextState) {
                    logger.warn("BayStateEngine : Next state '" + nextStateName + "' not found in planner for BayKey: " + bayKey + ". Stopping engine.");
                    break; // Break out of the execution loop if the next state isn't in the database
                }
            } else {
                // FAILURE
                String errorCode = bayStatus.getErrorCode();
                String nextStateName = context.getErrorStateInstanceString(errorCode);

                presentRow.setIfFailed(nextStateName);

                nextStateName = presentRow.getIfFailed();

                if (nextStateName != null && !nextStateName.isEmpty()) {
                    context.setNextState(nextStateName, errorCode);
                }

                boolean foundNextState = false;
                for (StateFlow row : statePlanner) {
                    if (row.getState().equals(nextStateName)) {
                        nextRow = row;
                        foundNextState = true;
                        break;
                    }
                }
                
                if (!foundNextState) {
                    logger.warn("BayStateEngine : Next state '" + nextStateName + "' not found in planner for BayKey: " + bayKey + ". Stopping engine.");
                    break; // Break out of the execution loop if the next state isn't in the database
                }
            }
        }
        
        if (stopRequested || ConstantStateModes.STOP.equals(executionMode)) {
            context.onStopComplete();
        }
        
        logger.debug("BayStateEngine : Loop exited for BayKey: " + bayKey);
    }

    public void requestStop() {
        this.stopRequested = true;
        Thread t = this.runningThread;
        if (t != null && t.isAlive()) {
            try {
                t.interrupt();
            } catch (Exception e) {
                logger.warn("Exception while interrupting BayStateEngine thread for " + bayKey, e);
            }
        }
    }

    public boolean isStopRequested() {
        return this.stopRequested;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void join(long timeoutMillis) {
        Thread t = this.runningThread;
        if (t != null && t.isAlive()) {
            try {
                t.join(timeoutMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
