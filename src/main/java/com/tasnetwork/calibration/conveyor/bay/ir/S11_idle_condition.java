package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;

public class S11_idle_condition implements IrtBayState {
	/**
	 * Idle condition state for the IRT Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S11_idle_condition : Entry");
		
		BayUtils.delay(1000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");


		
		if (Ir.isStartProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
		}

		if (Ir.isStopProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
		}

		if (Ir.isResetProcessRequestedIrtBay()) {
			Ir.setResetProcessCompletedIrtBay(true);
		}

		return bayResponse;
	}
}