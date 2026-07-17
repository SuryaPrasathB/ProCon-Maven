package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class HvtBayContext {

	private HvtBayState hvtBayState;
	private HvtBayState lastProcessedBayState;
	
	public HvtBayContext() {
	//	hvtBayState = new  ();
	}
	
	public void setState(HvtBayState state) {
		this.hvtBayState = state;
	}
	
	public HvtBayState getState() {
		return this.hvtBayState;
	}
	
	public BayResponse processPresentState() {
		Hv.logger.debug("HvtBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = hvtBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public HvtBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(HvtBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

