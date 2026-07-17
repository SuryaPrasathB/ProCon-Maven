package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;


/*
 * S047  -> Start Execution
 * S046  -> Turn On Meter Relays
 * S042  -> Turn On Main CT 
 * S052  -> Power Source Start Main CT  
 * S055  -> Phase Calibration  
 * S041  -> Voltage Start  
 * S047  -> Turn Off Main CT Neutral CT  
 * S043  -> Turn On Neutral CT  
 * S053  -> Power Source Start Neutral CT 
 * S056  -> Neutral Calibration  
 * S050  -> Stop Execution  
 * S045  -> Turn Off Main CT Neutral CT  

 */

public class CalibrationBayContext {

	private CalibrationBayState calibTestBayState;
	private CalibrationBayState lastProcessedBayState;
	
	public CalibrationBayContext() {
	//	commTestBayState = new  ();
	}
	
	public void setState(CalibrationBayState state) {
		this.calibTestBayState = state;
	}
	
	public CalibrationBayState getState() {
		return this.calibTestBayState;
	}
	
	public BayResponse processPresentState() {
		Calib.logger.debug("CalibBayTestContext: processPresentState: Entry");
		
		BayResponse bayResponse = calibTestBayState.handleRequest();
		setLastProcessedBayState(getState());
		return bayResponse;
	}
	
	public CalibrationBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}
	
	public void setLastProcessedBayState(CalibrationBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
}

