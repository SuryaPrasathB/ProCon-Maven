package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;

public class S10_error_Handling implements HvtBayState {

	Timer hvtBayStopTaskTimer;

	BayResponse bayResponse = new BayResponse();

	/**
	 * Error handling state for the HVT Bay.
	 * Schedules the stop task timer upon error.
	 *
	 * @return BayResponse.
	 */
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S10_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S10_error_Handling() {

	}

	public S10_error_Handling(String errorCode) {
		BayControlsManager.getInstance().handleStop(ConstantConveyor.HV_BAY_KEY);
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.HV_BAY_KEY, errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			Hv.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

}
