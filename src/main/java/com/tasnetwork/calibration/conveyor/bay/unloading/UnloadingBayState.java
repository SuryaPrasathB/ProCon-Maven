package com.tasnetwork.calibration.conveyor.bay.unloading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface UnloadingBayState {
	String myBayKey = ConstantConveyor.UNLOADING_BAY_KEY;
	BayResponse handleRequest();
}
