package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S420_ByPass_delay1 implements FtBayState{

	@Override
	public BayResponse handleRequest() {
		Ft.logger.info("S420_ByPass_delay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int delayTimeInSec = 20;
		Ft.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec >0 
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG 
				&& !Ft.isStopProcessRequestedFtBay()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			Ft.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		Ft.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);


		Ft.logger.info("S420_ByPass_delay1 : Exit");
		return bayResponse;
	}
}