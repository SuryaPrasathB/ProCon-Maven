package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class S14_idle_condition_Bay2 implements STA_NoLoadTestBay2State {
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S16_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		
		if (StaNld_Bay2.isResetProcessRequestedStaNldBay2()) {
			StaNld_Bay2.setResetProcessCompletedStaNldBay2(true);
			StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);
		}

		if (StaNld_Bay2.isStopProcessRequestedStaNldBay2()) {
			StaNld_Bay2.setStopProcessCompletedStaNldBay2(true);
			//StaNld_2.setStopProcessRequestedStaNldBay2(false);
		}

		return bayResponse;
	}
}