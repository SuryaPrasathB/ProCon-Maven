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
        
        // 3. Create new timer and schedule
        Timer newTimer = new Timer();
        activeTimers.put(bayKey, newTimer);
        
        if (task instanceof BayStateEngine) {
            activeEngines.put(bayKey, (BayStateEngine) task);
        }
        
        newTimer.schedule(task, 100);
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
    }
}
