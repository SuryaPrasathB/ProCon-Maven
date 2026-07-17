package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class IrtBayContext {

	private IrtBayState irtBayState;
	private IrtBayState lastProcessedBayState;
	
	public IrtBayContext() {
		// irtBayState = new ();
	}
	
	public void setState(IrtBayState state) {
		this.irtBayState = state;
	}
	
	public IrtBayState getState() {
		return this.irtBayState;
	}
	
	public BayResponse processPresentState() {
		Ir.logger.debug("IrtBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = irtBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public IrtBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(IrtBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

