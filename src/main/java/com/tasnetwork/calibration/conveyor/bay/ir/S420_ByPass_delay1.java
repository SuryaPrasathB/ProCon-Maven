package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S420_ByPass_delay1 implements IrtBayState {

	/**
	 * Bypass delay state for the IRT Bay.
	 * Provides a workaround delay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S420_ByPass_delay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int delayTimeInSec = 5;
		Ir.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec > 0
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG
				&& !Ir.isStopProcessRequestedIrtBay()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			Ir.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		Ir.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);

		Ir.logger.info("S420_ByPass_delay1 : Exit");
		return bayResponse;
	}
}