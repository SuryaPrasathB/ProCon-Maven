package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S18_idle_condition implements VerificTestBayState {
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S18_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;
		
		while (!idleComplete &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG) ) {
			for(int i = 0; i < 15; i ++) {
				Verification.logger.info("S18_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;

		}

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