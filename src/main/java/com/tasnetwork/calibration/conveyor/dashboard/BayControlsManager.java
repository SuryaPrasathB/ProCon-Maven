package com.tasnetwork.calibration.conveyor.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class BayControlsManager {
    
    private static BayControlsManager instance;
    private final List<IBayUIController> registeredControllers = new ArrayList<>();

    private BayControlsManager() {
        // Private constructor for singleton
    }

    public static synchronized BayControlsManager getInstance() {
        if (instance == null) {
            instance = new BayControlsManager();
        }
        return instance;
    }

    public void registerController(IBayUIController controller) {
        if (!registeredControllers.contains(controller)) {
            registeredControllers.add(controller);
        }
    }

    public void unregisterController(IBayUIController controller) {
        registeredControllers.remove(controller);
    }

    /**
     * Broadcasts the UI state to all registered controllers.
     * @param bayKey The key of the bay.
     * @param isRunning True if the engine is running.
     */
    public void broadcastUIState(String bayKey, boolean isRunning) {
        for (IBayUIController controller : registeredControllers) {
            controller.updateBayUI(bayKey, isRunning);
        }
    }

    public void handleStart(String bayKey) {
        ApplicationLauncher.logger.info("BayControlsManager: handleStart requested for " + bayKey);
        broadcastUIState(bayKey, true);
        new BayActionHandler(bayKey).handleActionByBayType(PalletController.BayActionType.BAY_START);
    }

    public void handleStop(String bayKey) {
        ApplicationLauncher.logger.info("BayControlsManager: handleStop requested for " + bayKey);
        broadcastUIState(bayKey, false);
        new BayActionHandler(bayKey).handleActionByBayType(PalletController.BayActionType.BAY_STOP);
    }

    public void handleReset(String bayKey) {
        ApplicationLauncher.logger.info("BayControlsManager: handleReset requested for " + bayKey);
        broadcastUIState(bayKey, false);
        new BayActionHandler(bayKey).handleActionByBayType(PalletController.BayActionType.BAY_RESET);
    }
}
