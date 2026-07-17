package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S10_error_Handling  implements CommTestBayState {
	Timer commStopTaskTimer;

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

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_COMM_001:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_002:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_003:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_004:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_005:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_006:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_007:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_008:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_009:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_010:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_011:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_012:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_COMM_013:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		default:
			commStopTaskTimer = new Timer();
			commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);
			Sleep(500);
			commStopTaskTimer.cancel();
			break;
		}

		bayResponse.setErrorCode(errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Comm.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}
}