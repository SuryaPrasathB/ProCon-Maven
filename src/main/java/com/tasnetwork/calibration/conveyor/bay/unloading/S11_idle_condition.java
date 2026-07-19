package com.tasnetwork.calibration.conveyor.bay.unloading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S11_idle_condition implements UnloadingBayState {
	
	

	@Override
	public BayResponse handleRequest() {
		Unloading.logger.info("S11_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;

		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				Unloading.logger.info("S23_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		
		if (Unloading.isStartProcessRequestedUnloadingBay()) {
			Unloading.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [START_REQUESTED] - Start process requested. Setting stop process completed.", getMyBayKey()));
			Unloading.setStopProcessCompletedUnloadingBay(true);
		}

        // Check and handle stop process request
		if (Unloading.isStopProcessRequestedUnloadingBay()) {
			Unloading.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [STOP_REQUESTED] - Stop process requested. Setting stop process completed.", getMyBayKey()));
			Unloading.setStopProcessCompletedUnloadingBay(true);
		}

        // Check and handle reset process request
		if (Unloading.isResetProcessRequestedUnloadingBay()) {
			Unloading.logger.debug(String.format("[%s] : [IDLE_CONDITION] : [RESET_REQUESTED] - Reset process requested. Setting reset process completed.", getMyBayKey()));
			Unloading.setResetProcessCompletedUnloadingBay(true);
		}


		return bayResponse;
	}
}