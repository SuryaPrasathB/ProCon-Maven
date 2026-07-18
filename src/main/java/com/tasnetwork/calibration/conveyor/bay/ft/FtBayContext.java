package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

/**
 * Manages the current and previous states of the Functional Test (FT) Bay.
 * Acts as the context object in the State Design Pattern, holding the reference to the active state 
 * and delegating execution to it.
 */
public class FtBayContext {
	 
	private FtBayState ftBayState;
	private FtBayState lastProcessedBayState;
	
	/**
	 * Initializes a new context for the FT Bay.
	 * Sets the default initial state to check for a pallet. This is usually overridden
	 * immediately by the database-driven state planner during engine startup.
	 */
	public FtBayContext() {
		ftBayState = new S01_check_for_pallet_at_FT_Bay();
	}
	
	/**
	 * Sets the current state of the FT Bay.
	 *
	 * @param state The new {@link FtBayState} to transition to.
	 */
	public void setState(FtBayState state) {
        this.ftBayState = state;
    }
	
	/**
	 * Retrieves the currently active state.
	 *
	 * @return The current {@link FtBayState}.
	 */
	public FtBayState getState() {
        return this.ftBayState;
    }
	
	/**
	 * Executes the logic of the currently active state and updates the state history.
	 *
	 * @return A {@link BayResponse} indicating the success or failure of the state execution.
	 */
	public BayResponse processPresentState() {
		 Ft.logger.debug("FtBayContext: processPresentState: Entry ");
			
		 BayResponse bayResponse = ftBayState.handleRequest();
		 setLastProcessedBayState(getState());
		 return bayResponse;
	}

	/**
	 * Retrieves the most recently executed state.
	 *
	 * @return The previous {@link FtBayState} that was processed.
	 */
	public FtBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}

	/**
	 * Records the state that was just executed.
	 *
	 * @param lastProcessedBayState The state to record as the previous state.
	 */
	public void setLastProcessedBayState(FtBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}