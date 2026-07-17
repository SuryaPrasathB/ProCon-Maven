package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class STA_NoLoadTestBay2Context {

	private STA_NoLoadTestBay2State sclNltBay2State;
	private STA_NoLoadTestBay2State lastProcessedBayState;
	
	public STA_NoLoadTestBay2Context() {
	//	sclNltBay2State = new  ();
	}
	
	public void setState(STA_NoLoadTestBay2State state) {
		this.sclNltBay2State = state;
	}
	
	public STA_NoLoadTestBay2State getState() {
		return this.sclNltBay2State;
	}
	
	public BayResponse processPresentState() {
		StaNld_Bay2.logger.debug("SclNltBay2Context: processPresentState: Entry");
		
		BayResponse bayResponse = sclNltBay2State.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public STA_NoLoadTestBay2State getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(STA_NoLoadTestBay2State lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}


