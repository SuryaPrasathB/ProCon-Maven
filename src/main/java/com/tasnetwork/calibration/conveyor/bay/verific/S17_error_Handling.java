package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S17_error_Handling implements VerificTestBayState {
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S17_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S17_error_Handling() {

	}

	public S17_error_Handling(String errorCode) {
		MainControlPaneController mainControlPaneController = new MainControlPaneController();
		mainControlPaneController.verificTestStop();
	}

}