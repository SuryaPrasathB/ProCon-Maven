package com.tasnetwork.calibration.conveyor.dashboard;

public interface IBayUIController {
    /**
     * Updates the UI buttons for the specified bay.
     * @param bayKey The unique identifier for the bay.
     * @param isRunning True if the engine is running.
     */
    void updateBayUI(String bayKey, boolean isRunning);
}
