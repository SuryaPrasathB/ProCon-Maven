package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class STA_NoLoadTestBay1Context {

	private STA_NoLoadTestBay1State sclNltBay1State;
	private STA_NoLoadTestBay1State lastProcessedBayState;
	
	public STA_NoLoadTestBay1Context() {
	}
	
	public void setState(STA_NoLoadTestBay1State state) {
		this.sclNltBay1State = state;
	}
	
	public STA_NoLoadTestBay1State getState() {
		return this.sclNltBay1State;
	}
	
	public BayResponse processPresentState() {
		StaNld_Bay1.logger.debug("SclNltBay1Context: processPresentState: Entry");
		
		BayResponse bayResponse = sclNltBay1State.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public STA_NoLoadTestBay1State getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(STA_NoLoadTestBay1State lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}


