package com.tasnetwork.calibration.conveyor.bay.comm;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class CommTestBayContext {

	private CommTestBayState commTestBayState;
	private CommTestBayState lastProcessedBayState;
	
	public CommTestBayContext() {
	//	commTestBayState = new  ();
	}
	
	public void setState(CommTestBayState state) {
		this.commTestBayState = state;
	}
	
	public CommTestBayState getState() {
		return this.commTestBayState;
	}
	
	public BayResponse processPresentState() {
		Comm.logger.debug("CommTestBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = commTestBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public CommTestBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(CommTestBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

