package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S13_error_Handling_Bay1  implements STA_NoLoadTestBay1State {
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

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_001:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
		    break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_002:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_003:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_004:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_005:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_006:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_007:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_008:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_009:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_010:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_011:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_012:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_013:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_014:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_015:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_016:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_017:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
			break;
		default:
			sctNlt1StopTaskTimer = new Timer();
		    sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);
		    Sleep(500);
		    sctNlt1StopTaskTimer.cancel();
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
			StaNld_Bay1.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

}