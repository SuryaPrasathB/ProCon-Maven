package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S14_idle_condition_Bay1 implements STA_NoLoadTestBay1State {
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S14_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		boolean printLogAlreadyDone = false;
		boolean idleComplete = false;

		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				if(!printLogAlreadyDone){
					StaNld_Bay1.logger.info("S14_idle_condition : Waiting in Idle Condition");
					printLogAlreadyDone = true;
				}
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		if (StaNld_Bay1.isResetProcessRequestedStaNldBay1()) {
			StaNld_Bay1.setResetProcessCompletedStaNldBay1(true);
			StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);
		}

		if (StaNld_Bay1.isStopProcessRequestedStaNldBay1()) {
			StaNld_Bay1.setStopProcessCompletedStaNldBay1(true);
		}

		return bayResponse;
	}
}