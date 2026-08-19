package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
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
		BayControlsManager.getInstance().handleStop(ConstantConveyor.VERIFICATION_BAY_KEY);
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.VERIFICATION_BAY_KEY, errorCode);
	}

}
