package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class LoadingBayContext {

	private LoadingBayState loadingBayState;
	private LoadingBayState lastProcessedBayState;
	
	public LoadingBayContext() {
	//	loadingBayState = new  ();
	}
	
	public void setState(LoadingBayState state) {
		this.loadingBayState = state;
	}
	
	public LoadingBayState getState() {
		return this.loadingBayState;
	}
	
	public BayResponse processPresentState() {
		Loading.logger.debug("LoadingBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = loadingBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public LoadingBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(LoadingBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

