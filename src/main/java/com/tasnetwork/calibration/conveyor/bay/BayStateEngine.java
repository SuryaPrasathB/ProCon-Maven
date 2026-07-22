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
        this.stopRequested = true;
    }

    public void requestRunOnce() {
        this.runOnceRequested = true;
    }

    @Override
    public void run() {
        logger.debug("BayStateEngine : Entry for BayKey: " + bayKey);
        manageBayStates();
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

        while (!stopRequested && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
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

                // Re-fetch the current row for the next iteration
                for (StateFlow row : statePlanner) {
                    if (row.getState().equals(nextStateName)) {
                        nextRow = row;
                        break;
                    }
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

                for (StateFlow row : statePlanner) {
                    if (row.getState().equals(nextStateName)) {
                        nextRow = row;
                        break;
                    }
                }
            }
        }
        
        if (stopRequested) {
            context.onStopComplete();
        }
        
        logger.debug("BayStateEngine : Loop exited for BayKey: " + bayKey);
    }

    public void requestStop() {
        this.stopRequested = true;
    }
}
