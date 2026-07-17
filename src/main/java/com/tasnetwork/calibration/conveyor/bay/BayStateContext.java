package com.tasnetwork.calibration.conveyor.bay;

public interface BayStateContext {
    /**
     * Instantiates and sets the next state for the specific bay context.
     * @param stateName The name of the next state.
     * @param errorCode An optional error code if the state is an error handler.
     */
    void setNextState(String stateName, String errorCode);

    /**
     * Process the currently set state.
     * @return BayResponse indicating success or failure and any error code.
     */
    BayResponse processCurrentState();

    /**
     * Retrieve the appropriate error state name for a given error code.
     * @param errorCode The error code returned by a failed state.
     * @return The name of the error state to fallback to.
     */
    String getErrorStateInstanceString(String errorCode);

    /**
     * Called when the state engine exits its loop due to a stop request.
     */
    default void onStopComplete() {}

    /**
     * Called right before the state engine enters its main loop.
     */
    default void onStartComplete() {}
}
