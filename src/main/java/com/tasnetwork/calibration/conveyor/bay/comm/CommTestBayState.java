package com.tasnetwork.calibration.conveyor.bay.comm;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface CommTestBayState {
	String myBayKey = ConstantConveyor.COMMUNICATION_BAY_KEY ;
	BayResponse handleRequest();
}
