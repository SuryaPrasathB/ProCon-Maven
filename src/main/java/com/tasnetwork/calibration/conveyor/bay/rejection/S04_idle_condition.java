package com.tasnetwork.calibration.conveyor.bay.rejection;


import com.tasnetwork.calibration.conveyor.bay.BayResponse;


public class S04_idle_condition implements RejectionBayState {
	
	/**
	 * Idle condition state for the Rejection Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Rejection.logger.info("S04_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");


		
		if (Rejection.isStartProcessRequestedRejectionBay()) {
			Rejection.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [START_REQUESTED] - Start process requested. Setting stop process completed.", getMyBayKey()));
			Rejection.setStopProcessCompletedRejectionBay(true);
		}

        // Check and handle stop process request
		if (Rejection.isStopProcessRequestedRejectionBay()) {
			Rejection.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [STOP_REQUESTED] - Stop process requested. Setting stop process completed.", getMyBayKey()));
			Rejection.setStopProcessCompletedRejectionBay(true);
		}

        // Check and handle reset process request
		if (Rejection.isResetProcessRequestedRejectionBay()) {
			Rejection.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [RESET_REQUESTED] - Reset process requested. Setting reset process completed.", getMyBayKey()));
			Rejection.setResetProcessCompletedRejectionBay(true);
		}

		return bayResponse;
	}
}
