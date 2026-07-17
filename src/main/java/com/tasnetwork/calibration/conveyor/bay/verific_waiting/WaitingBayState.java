package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface WaitingBayState {
	String myBayKey = ConstantConveyor.WAITING_BAY_KEY;
	BayResponse handleRequest();
}
