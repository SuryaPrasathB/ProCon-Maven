package com.tasnetwork.calibration.conveyor.bay.comm;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S11_idle_condition implements CommTestBayState {
	@Override
	public BayResponse handleRequest() {
		Comm.logger.info("S11_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		if (Comm.isResetProcessRequestedCommBay()) {
			Comm.setResetProcessCompletedCommBay(true);
			Comm.setResetProcessRequestedCommBay(false);
		}

		if (Comm.isStopProcessRequestedCommBay()) {
			Comm.setStopProcessCompletedCommBay(true);
			Comm.setStopProcessRequestedCommBay(false);
		}

		return bayResponse;
	}
}