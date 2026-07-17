package com.tasnetwork.calibration.conveyor.bay.sta_nld2;


import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S420_ByPass_delay1_Bay2 implements STA_NoLoadTestBay2State{

	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S420_ByPass_delay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);




		int delayTimeInSec = 20;
		StaNld_Bay2.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec >0 
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG 
				&& !StaNld_Bay2.isStopProcessRequestedStaNldBay2()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			StaNld_Bay2.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		StaNld_Bay2.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);


		StaNld_Bay2.logger.info("S420_ByPass_delay1 : Exit");
		return bayResponse;
	}
}