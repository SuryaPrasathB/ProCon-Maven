package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S10_error_Handling implements CalibrationBayState {

	Timer calibrationStopTaskTimer;

	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S10_error_Handling : Entry");

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S10_error_Handling() {

	}

	public S10_error_Handling(String errorCode) {

		// F L A G S
		Calib.abort_Calib_Bay = true;

		Calib.setStartProcessRequestedCalibBay(false);

		Calib.setStopProcessCompletedCalibBay(false);
		Calib.setStopProcessRequestedCalibBay(true);

		Calib.setResetProcessCompletedCalibBay(false);
		Calib.setResetProcessRequestedCalibBay(false);

		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.CALIBRATION_BAY_KEY, errorCode);

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_001:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_002:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_003:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_004:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_005:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_006:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_007:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_008:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_009:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_010:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_011:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_012:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_013:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_014:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_015:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
			default:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.CALIBRATION_BAY_KEY);

				break;
		}

		bayResponse.setErrorCode(errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			Calib.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}
}
