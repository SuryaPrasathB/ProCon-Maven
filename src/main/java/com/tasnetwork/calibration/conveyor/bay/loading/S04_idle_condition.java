package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S04_idle_condition implements LoadingBayState {
	@Override
	public BayResponse handleRequest() {
		Loading.logger.info("S04_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");
		
		boolean idleComplete = false;

		if (Loading.isResetProcessRequestedLoadingBay()) {
			Loading.setResetProcessCompletedLoadingBay(true);
			Loading.setResetProcessRequestedLoadingBay(false);
		}

		if (Loading.isStopProcessRequestedLoadingBay()) {
			Loading.setStopProcessCompletedLoadingBay(true);
			Loading.setStopProcessRequestedLoadingBay(false);
		}


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 5; i ++) {
				Loading.logger.info("S23_idle_condition : Waiting in Idle Condition");
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}		 
		
		return bayResponse;
	}
}