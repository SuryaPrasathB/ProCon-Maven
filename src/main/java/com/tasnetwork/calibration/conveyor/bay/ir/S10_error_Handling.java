package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;

public class S10_error_Handling implements IrtBayState {

	Timer insResStopTaskTimer;

	BayResponse bayResponse = new BayResponse();

	/**
	 * Error handling state for the IRT Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S10_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S10_error_Handling() {

	}

	public S10_error_Handling(String errorCode) {
		insResStopTaskTimer = new Timer();
		insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(), 100);
		ConveyorDataManager.getDashboardObject().showInlineBayError(ConstantConveyor.IR_BAY_KEY, errorCode);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			Ir.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}
}
