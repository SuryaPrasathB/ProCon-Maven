package com.tasnetwork.calibration.conveyor.bay.rejection;


import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;


public class S04_idle_condition implements RejectionBayState {
	
	public String getMyBayKey() {
        return myBayKey;
    }
	
	@Override
	public BayResponse handleRequest() {
		Rejection.logger.info("S04_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;

/*		if (Rejection.isResetProcessRequestedRejectionBay()) {
			Rejection.setResetProcessCompletedRejectionBay(true);
			Rejection.setResetProcessRequestedRejectionBay(false);
		}

		if (Rejection.isStopProcessRequestedRejectionBay()) {
			Rejection.setStopProcessCompletedRejectionBay(true);
			Rejection.setStopProcessRequestedRejectionBay(false);
		}*/


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				Rejection.logger.info("S04_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
/*		if (Rejection.isResetProcessRequestedRejectionBay()) {
			Rejection.setResetProcessCompletedRejectionBay(true);
			Rejection.setResetProcessRequestedRejectionBay(false);
		}

		if (Rejection.isStopProcessRequestedRejectionBay()) {
			Rejection.setStopProcessCompletedRejectionBay(true);
			//Rejection.setStopProcessRequestedRejectionBay(false);
		}*/
		
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
