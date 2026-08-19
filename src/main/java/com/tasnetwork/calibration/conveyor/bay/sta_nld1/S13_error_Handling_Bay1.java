package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S13_error_Handling_Bay1 implements STA_NoLoadTestBay1State {
	Timer sctNlt1StopTaskTimer;
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S13_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S13_error_Handling_Bay1() {

	}

	public S13_error_Handling_Bay1(String errorCode) {
		BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD1_BAY_KEY);
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.STA_NLD1_BAY_KEY, errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			StaNld_Bay1.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

}
