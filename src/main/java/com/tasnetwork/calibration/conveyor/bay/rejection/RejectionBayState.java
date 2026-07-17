package com.tasnetwork.calibration.conveyor.bay.rejection;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface RejectionBayState {
	String myBayKey = ConstantConveyor.REJECTION_BAY_KEY;
	BayResponse handleRequest();
}
