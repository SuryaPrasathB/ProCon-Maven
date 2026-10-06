package com.tasnetwork.calibration.conveyor.dashboard;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.tasnetwork.calibration.conveyor.bay.BayStateEngine;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class BayThreadManager {
    
    // Map of BayKey -> Currently active Timer
    private static final Map<String, Timer> activeTimers = new ConcurrentHashMap<>();
    
    // Map of BayKey -> Currently active Engine (for stopping)
    private static final Map<String, BayStateEngine> activeEngines = new ConcurrentHashMap<>();
    
    // Map of BayKey -> Currently active running Thread
    private static final Map<String, Thread> activeThreads = new ConcurrentHashMap<>();
    
    /**
     * Safely schedules a new task for a bay, cancelling any existing tasks.
     * @param bayKey The unique identifier for the bay
     * @param task The task to run (BayStateEngine or other TimerTask)
     * @param actionName A name for logging (e.g. "START", "STOP")
     * @return the newly created Timer
     */
    public static synchronized Timer scheduleTask(String bayKey, TimerTask task, String actionName) {
        ApplicationLauncher.logger.info(String.format("BayThreadManager: Requesting %s for %s", actionName, bayKey));
        
        // 1. Cancel previous timer
        Timer oldTimer = activeTimers.remove(bayKey);
        if (oldTimer != null) {
            ApplicationLauncher.logger.warn(String.format("BayThreadManager: Cancelling existing timer for %s", bayKey));
            oldTimer.cancel();
        }
        
        // 2. If the previous task was an engine, request it to stop gracefully
        BayStateEngine oldEngine = activeEngines.remove(bayKey);
        if (oldEngine != null) {
            ApplicationLauncher.logger.warn(String.format("BayThreadManager: Requesting graceful stop of existing engine for %s", bayKey));
            oldEngine.requestStop();
        }

        // Interrupt any active thread running for this bay
        Thread oldThread = activeThreads.remove(bayKey);
        if (oldThread != null && oldThread.isAlive()) {
            try {
                oldThread.interrupt();
            } catch (Exception e) {
                ApplicationLauncher.logger.warn("BayThreadManager: Exception interrupting previous thread for " + bayKey, e);
            }
        }
        
        // 3. Create new timer and schedule wrapped task
        Timer newTimer = new Timer();
        activeTimers.put(bayKey, newTimer);
        
        if (task instanceof BayStateEngine) {
            activeEngines.put(bayKey, (BayStateEngine) task);
        }
        
        TimerTask wrappedTask = new TimerTask() {
            @Override
            public void run() {
                activeThreads.put(bayKey, Thread.currentThread());
                try {
                    task.run();
                } finally {
                    activeThreads.remove(bayKey);
                }
            }
        };

        newTimer.schedule(wrappedTask, 100);
        return newTimer;
    }

    /**
     * Cancels any active task/timer for the specified bay.
     * @param bayKey The unique identifier for the bay
     */
    public static synchronized void cancelTask(String bayKey) {
        Timer oldTimer = activeTimers.remove(bayKey);
        if (oldTimer != null) {
            ApplicationLauncher.logger.warn(String.format("BayThreadManager: Cancelling existing timer for %s", bayKey));
            oldTimer.cancel();
        }
        
        BayStateEngine oldEngine = activeEngines.remove(bayKey);
        if (oldEngine != null) {
            ApplicationLauncher.logger.warn(String.format("BayThreadManager: Requesting graceful stop of existing engine for %s", bayKey));
            oldEngine.requestStop();
        }

        Thread runningThread = activeThreads.remove(bayKey);
        if (runningThread != null && runningThread.isAlive()) {
            try {
                runningThread.interrupt();
            } catch (Exception e) {
                ApplicationLauncher.logger.warn("BayThreadManager: Exception interrupting thread for " + bayKey, e);
            }
        }
    }

    /**
     * Gets the currently active BayStateEngine for the specified bay.
     * @param bayKey The unique identifier for the bay
     * @return the active engine, or null if none is running
     */
    public static synchronized BayStateEngine getEngine(String bayKey) {
        return activeEngines.get(bayKey);
    }
}
