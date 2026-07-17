package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class VerificationBayContext {

	private VerificTestBayState verificationBayState;
	private VerificTestBayState lastProcessedBayState;
	
	public VerificationBayContext() {
	//	verificationBayState = new  ();
	}
	
	public void setState(VerificTestBayState state) {
		this.verificationBayState = state;
	}
	
	public VerificTestBayState getState() {
		return this.verificationBayState;
	}
	
	public BayResponse processPresentState() {
		Verification.logger.debug("VerificationBayContext: processPresentState: Entry");
		
		BayResponse bayResponse = verificationBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public VerificTestBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(VerificTestBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

