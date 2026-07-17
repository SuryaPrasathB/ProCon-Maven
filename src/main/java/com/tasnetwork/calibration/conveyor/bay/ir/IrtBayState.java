package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface IrtBayState {
	String myBayKey = ConstantConveyor.IR_BAY_KEY;
	BayResponse handleRequest();
	
	//String getMyBayKey();
}
