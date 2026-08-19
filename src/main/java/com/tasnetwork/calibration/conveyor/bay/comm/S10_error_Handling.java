package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class S10_error_Handling implements CommTestBayState {
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
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.COMMUNICATION_BAY_KEY, errorCode);

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_COMM_001:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_002:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_003:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_004:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_005:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_006:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_007:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_008:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_009:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_010:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_011:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_012:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			case ConvErrorCodeMapping.ERROR_CODE_COMM_013:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
			default:
				BayControlsManager.getInstance().handleStop(ConstantConveyor.COMMUNICATION_BAY_KEY);

				break;
		}

		bayResponse.setErrorCode(errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			Comm.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}
}
