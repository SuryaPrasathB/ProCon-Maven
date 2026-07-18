package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S10_error_Handling implements HvtBayState {

	Timer hvtBayStopTaskTimer;

	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S10_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		/*
		 * while(bayResponse.getStatus()){
		 * HighVoltageTestBay.logger.info("S10_error_Handling : Error");
		 * Sleep(2000);
		 * }
		 */

		return bayResponse;
	}

	public S10_error_Handling() {

	}

	public S10_error_Handling(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_HVT_001:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_002:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_003:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_004:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_005:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_006:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_007:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_008:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_009:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_010:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_011:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_012:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_013:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_014:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_015:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_016:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_HVT_017:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
			default:
				hvtBayStopTaskTimer = new Timer();
				hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);

				break;
		}

		bayResponse.setErrorCode(errorCode);
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