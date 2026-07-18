package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;



public class S11_idle_condition implements HvtBayState {
	/**
	 * Idle condition state for the HVT Bay.
	 *
	 * @return BayResponse.
	 */
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
				Hv.logger.info("S11_idle_condition : Waiting in Idle Condition" + getMyBayKey());
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}

		if (Hv.isStartProcessRequestedHvtBay()) {
			Hv.setStopProcessCompletedHvtBay(true);
		}

		if (Hv.isStopProcessRequestedHvtBay()) {
			Hv.setStopProcessCompletedHvtBay(true);
		}
		
		if (Hv.isResetProcessRequestedHvtBay()) {
			Hv.setResetProcessCompletedHvtBay(true);
		}
		
		return bayResponse;
	}
}