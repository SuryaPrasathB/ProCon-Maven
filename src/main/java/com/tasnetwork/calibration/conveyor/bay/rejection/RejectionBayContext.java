package com.tasnetwork.calibration.conveyor.bay.rejection;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class RejectionBayContext {

	private RejectionBayState rejectionBayState;
	private RejectionBayState lastProcessedBayState;
	
	public RejectionBayContext() {
	//	rejectionBayState = new  ();
	}
	
	public void setState(RejectionBayState state) {
		this.rejectionBayState = state;
	}
	
	public RejectionBayState getState() {
		return this.rejectionBayState;
	}
	
	public BayResponse processPresentState() {
		Rejection.logger.debug("RejectionBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = rejectionBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public RejectionBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(RejectionBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

