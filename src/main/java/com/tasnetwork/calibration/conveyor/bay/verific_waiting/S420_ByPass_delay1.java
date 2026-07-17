package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S420_ByPass_delay1 implements WaitingBayState{

	@Override
	public BayResponse handleRequest() {
		VerificWaiting.logger.info("S420_ByPass_delay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);




		int delayTimeInSec = 20;
		VerificWaiting.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec >0 
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG 
				&& !VerificWaiting.isStopProcessRequestedWaitingBay()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			VerificWaiting.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		VerificWaiting.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);


		VerificWaiting.logger.info("S420_ByPass_delay1 : Exit");
		return bayResponse;
	}
}