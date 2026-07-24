package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S04_idle_condition implements LoadingBayState {
	/**
	 * Idle condition state for the Loading Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
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

		 
		
		return bayResponse;
	}
}