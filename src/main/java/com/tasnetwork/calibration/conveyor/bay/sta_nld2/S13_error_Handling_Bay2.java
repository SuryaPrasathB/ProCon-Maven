package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S13_error_Handling_Bay2 implements STA_NoLoadTestBay2State {
	Timer sctNlt2StartTaskTimer;

	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		ApplicationLauncher.logger.info("S15_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S13_error_Handling_Bay2() {

	}

	public S13_error_Handling_Bay2(String errorCode) {
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.STA_NLD2_BAY_KEY, errorCode);

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_001:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_002:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_003:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_004:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_005:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_006:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_007:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_008:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_009:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_010:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_011:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_012:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_013:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_014:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_015:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_016:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_017:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_026:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;

			default:
				sctNlt2StartTaskTimer = new Timer();
				sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

				break;
		}

		bayResponse.setErrorCode(errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			StaNld_Bay2.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

}
