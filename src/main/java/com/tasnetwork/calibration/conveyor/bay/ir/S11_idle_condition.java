package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;



public class S11_idle_condition implements IrtBayState {
	/**
	 * Idle condition state for the IRT Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S11_idle_condition : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;

		


		while (!idleComplete &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 2; i ++) {
				Ir.logger.info("S11_idle_condition : Waiting in Idle Condition" + getMyBayKey());
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		if (Ir.isStartProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
		}

		if (Ir.isStopProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
		}

		if (Ir.isResetProcessRequestedIrtBay()) {
			Ir.setResetProcessCompletedIrtBay(true);
		}

		return bayResponse;
	}
}