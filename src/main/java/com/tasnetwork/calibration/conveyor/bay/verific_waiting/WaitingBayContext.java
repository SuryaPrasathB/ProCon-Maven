package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class WaitingBayContext {

	private WaitingBayState waitingBayState;
	private WaitingBayState lastProcessedBayState;
	
	public WaitingBayContext() {
		//waitingBayState = new  ();
	}
	
	public void setState(WaitingBayState state) {
		this.waitingBayState = state;
	}
	
	public WaitingBayState getState() {
		return this.waitingBayState;
	}
	
	public BayResponse processPresentState() {
		VerificWaiting.logger.debug("WaitingBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = waitingBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public WaitingBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(WaitingBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}
