package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S18_idle_condition implements VerificTestBayState {
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S18_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");



		if (Verification.isResetProcessRequestedVerificBay()) {
			Verification.setResetProcessCompletedVerificBay(true);
			Verification.setResetProcessRequestedVerificBay(false);
		}

		if (Verification.isStopProcessRequestedVerificBay()) {
			Verification.setStopProcessCompletedVerificBay(true);
			Verification.setStopProcessRequestedVerificBay(false);
		}


		

		return bayResponse;
	}
}