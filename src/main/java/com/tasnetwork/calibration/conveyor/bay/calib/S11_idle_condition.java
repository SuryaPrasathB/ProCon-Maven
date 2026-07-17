package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public class S11_idle_condition implements CalibrationBayState {
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S11_idle_condition : Entry");
		
	    StateExecutorController.BTN_CALIB_START.setDisable(true);
	    StateExecutorController.BTN_CALIB_STOP.setDisable(true);
	    StateExecutorController.BTN_CALIB_RESET.setDisable(true);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		boolean idleComplete = false;
		
		
		
		Calib.logger.info("S11_idle_condition : Waiting in Idle Condition : 1min 30secs " + myBayKey);
		while (!idleComplete &&
				(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
			for(int i = 0; i < 90; i ++) {
				
				BayUtils.delay(1000);
			}
			idleComplete = true;
		}
		
		if (Calib.isStartProcessRequestedCalibBay()) {
			Calib.setStopProcessCompletedCalibBay(true);
			//CalibrationBay2.setStopProcessRequestedCalibBay(false);
		}
		
		
		if (Calib.isStopProcessRequestedCalibBay()) {
			Calib.setStopProcessCompletedCalibBay(true);
			//CalibrationBay2.setStopProcessRequestedCalibBay(false);
		}
		
		if (Calib.isResetProcessRequestedCalibBay()) {
			Calib.setResetProcessCompletedCalibBay(true);
			//CalibrationBay2.setResetProcessRequestedCalibBay(false);
		}

		StateExecutorController.BTN_CALIB_START.setDisable(false);
	    StateExecutorController.BTN_CALIB_RESET.setDisable(false);		

		return bayResponse;
	}
}