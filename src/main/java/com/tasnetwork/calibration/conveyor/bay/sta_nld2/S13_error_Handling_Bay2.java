package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
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
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_002:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_003:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_004:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_005:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_006:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_007:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_008:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_009:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_010:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_011:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_012:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_013:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_014:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_015:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_016:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_017:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_026:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

				break;

			default:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.STA_NLD2_BAY_KEY);

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
