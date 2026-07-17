package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S14_idle_condition_Bay2 implements STA_NoLoadTestBay2State {
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S16_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		boolean printLogAlreadyDone = false;
		boolean idleComplete = false;

		


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				if(!printLogAlreadyDone){
					StaNld_Bay2.logger.info("S16_idle_condition : Waiting in Idle Condition");
					printLogAlreadyDone = true;
				}
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		if (StaNld_Bay2.isResetProcessRequestedStaNldBay2()) {
			StaNld_Bay2.setResetProcessCompletedStaNldBay2(true);
			StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);
		}

		if (StaNld_Bay2.isStopProcessRequestedStaNldBay2()) {
			StaNld_Bay2.setStopProcessCompletedStaNldBay2(true);
			//StaNld_2.setStopProcessRequestedStaNldBay2(false);
		}

		return bayResponse;
	}
}