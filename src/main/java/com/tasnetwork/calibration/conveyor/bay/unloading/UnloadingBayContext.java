package com.tasnetwork.calibration.conveyor.bay.unloading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class UnloadingBayContext {

	private UnloadingBayState unloadingBayState;
	private UnloadingBayState lastProcessedBayState;
	
	public UnloadingBayContext() {
	//	unloadingBayState = new  ();
	}
	
	public void setState(UnloadingBayState state) {
		this.unloadingBayState = state;
	}
	
	public UnloadingBayState getState() {
		return this.unloadingBayState;
	}
	
	public BayResponse processPresentState() {
		Unloading.logger.debug("UnloadingBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = unloadingBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public UnloadingBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(UnloadingBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

