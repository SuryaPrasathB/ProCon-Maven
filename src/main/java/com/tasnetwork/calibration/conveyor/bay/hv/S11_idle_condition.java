package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;


public class S11_idle_condition implements HvtBayState {
	/**
	 * Idle condition state for the HVT Bay.
	 *
	 * @return BayResponse.
	 */
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S11_idle_condition : Entry");
		
		BayUtils.delay(1000); // Prevent CPU looping while idle
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		





		if (Hv.isStartProcessRequestedHvtBay()) {
			Hv.setStopProcessCompletedHvtBay(true);
		}

		if (Hv.isStopProcessRequestedHvtBay()) {
			Hv.setStopProcessCompletedHvtBay(true);
		}
		
		if (Hv.isResetProcessRequestedHvtBay()) {
			Hv.setResetProcessCompletedHvtBay(true);
		}
		
		return bayResponse;
	}
}