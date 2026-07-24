package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

/**
 * State class representing the idle condition in the FT Bay.
 */
public class S23_idle_condition implements FtBayState {


	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [SEQUENCE_ENTRY] - Entering idle condition state.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");


		// Check and handle start process request
		if (Ft.isStartProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [START_REQUESTED] - Start process requested. Setting stop process completed.", getMyBayKey()));
			Ft.setStopProcessCompletedFtBay(true);
		}

        // Check and handle stop process request
		if (Ft.isStopProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [STOP_REQUESTED] - Stop process requested. Setting stop process completed.", getMyBayKey()));
			Ft.setStopProcessCompletedFtBay(true);
		}

        // Check and handle reset process request
		if (Ft.isResetProcessRequestedFtBay()) {
			Ft.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [RESET_REQUESTED] - Reset process requested. Setting reset process completed.", getMyBayKey()));
			Ft.setResetProcessCompletedFtBay(true);
		}
				
		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [IDLE_CONDITION] : [SEQUENCE_EXIT] - Idle condition state completed. Final Status: %s, Error Code: %s", getMyBayKey(), bayResponse.getStatus(), bayResponse.getErrorCode()));
		return bayResponse;
	}
}
