package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;



public class S06_idle_condition implements WaitingBayState {
	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S06_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;

		if (VerificWaiting.isResetProcessRequestedWaitingBay()) {
			VerificWaiting.setResetProcessCompletedWaitingBay(true);
			VerificWaiting.setResetProcessRequestedWaitingBay(false);
		}

		if (VerificWaiting.isStopProcessRequestedWaitingBay()) {
			VerificWaiting.setStopProcessCompletedWaitingBay(true);
			VerificWaiting.setResetProcessRequestedWaitingBay(false);
		}


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				VerificWaiting.logger.info("S23_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;

		}

		return bayResponse;
	}
}