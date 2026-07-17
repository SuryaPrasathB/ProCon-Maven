package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface VerificTestBayState {
	String myBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
	BayResponse handleRequest();
}
