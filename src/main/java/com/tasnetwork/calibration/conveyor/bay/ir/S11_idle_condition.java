package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;



public class S11_idle_condition implements IrtBayState {
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
				Ir.logger.info("S23_idle_condition : Waiting in Idle Condition" + myBayKey);
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		if (Ir.isStartProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
			//InsulationResistanceTestBay.setResetProcessRequestedIrtBay(false);
		}

		if (Ir.isStopProcessRequestedIrtBay()) {
			Ir.setStopProcessCompletedIrtBay(true);
			//InsulationResistanceTestBay.setStopProcessRequestedIrtBay(false);
		}

		if (Ir.isResetProcessRequestedIrtBay()) {
			Ir.setResetProcessCompletedIrtBay(true);
			//InsulationResistanceTestBay.setResetProcessRequestedIrtBay(false);
		}

		return bayResponse;
	}
}