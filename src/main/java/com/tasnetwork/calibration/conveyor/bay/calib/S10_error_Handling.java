package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
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

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_001:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_002:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_003:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_004:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_005:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_006:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_007:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_008:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_009:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_010:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_011:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_012:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_013:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_014:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_CALIB_015:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

				break;
			default:
				calibrationStopTaskTimer = new Timer();
				calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);

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
