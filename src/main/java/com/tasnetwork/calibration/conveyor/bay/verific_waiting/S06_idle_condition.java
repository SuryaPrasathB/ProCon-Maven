package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class S06_idle_condition implements WaitingBayState {
	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S06_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		if (VerificWaiting.isResetProcessRequestedWaitingBay()) {
			VerificWaiting.setResetProcessCompletedWaitingBay(true);
			VerificWaiting.setResetProcessRequestedWaitingBay(false);
		}

		if (VerificWaiting.isStopProcessRequestedWaitingBay()) {
			VerificWaiting.setStopProcessCompletedWaitingBay(true);
			VerificWaiting.setResetProcessRequestedWaitingBay(false);
		}



		return bayResponse;
	}
}