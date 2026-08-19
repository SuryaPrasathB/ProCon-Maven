package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import javafx.application.Platform;

public class S11_idle_condition implements CalibrationBayState {
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S11_idle_condition : Entry");

		Platform.runLater(() -> {
			if (StateExecutorController.BTN_CALIB_START != null) {
				StateExecutorController.BTN_CALIB_START.setDisable(true);
				StateExecutorController.BTN_CALIB_STOP.setDisable(true);
				StateExecutorController.BTN_CALIB_RESET.setDisable(true);
			}
		});

		BayUtils.delay(1000); // Prevent CPU looping while idle

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		Calib.logger.info("S11_idle_condition : Idle Condition check for flags " + myBayKey);

		if (Calib.isStartProcessRequestedCalibBay()) {
			Calib.setStopProcessCompletedCalibBay(true);
		}

		if (Calib.isStopProcessRequestedCalibBay()) {
			Calib.setStopProcessCompletedCalibBay(true);
		}

		if (Calib.isResetProcessRequestedCalibBay()) {
			Calib.setResetProcessCompletedCalibBay(true);
		}

		Platform.runLater(() -> {
			if (StateExecutorController.BTN_CALIB_START != null) {
				StateExecutorController.BTN_CALIB_START.setDisable(false);
				StateExecutorController.BTN_CALIB_RESET.setDisable(false);
			}
		});

		return bayResponse;
	}
}