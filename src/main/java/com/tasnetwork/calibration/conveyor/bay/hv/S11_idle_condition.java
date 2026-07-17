package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;



public class S11_idle_condition implements HvtBayState {
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S11_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		
		boolean idleComplete = false;

		


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 2; i ++) {
				Hv.logger.info("S11_idle_condition : Waiting in Idle Condition" + myBayKey);
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}

		if (Hv.isStartProcessRequestedHvtBay()) {
			//FunctionalTestBay2.setStopProcessRequestedFtBay(false);
			Hv.setStopProcessCompletedHvtBay(true);
		}

		if (Hv.isStopProcessRequestedHvtBay()) {
			Hv.setStopProcessCompletedHvtBay(true);
			//HighVoltageTestBay2.setStopProcessRequestedHvtBay(false);
		}
		
		if (Hv.isResetProcessRequestedHvtBay()) {
			Hv.setResetProcessCompletedHvtBay(true);
			//HighVoltageTestBay2.setResetProcessRequestedHvtBay(false);
		}
		
		return bayResponse;
	}
}