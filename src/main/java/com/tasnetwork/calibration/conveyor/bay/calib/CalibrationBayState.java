package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface CalibrationBayState {
	String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY ;
	BayResponse handleRequest();
	//String getMyBayKey();
	
}
