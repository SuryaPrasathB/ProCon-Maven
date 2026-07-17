package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface HvtBayState {

	String myBayKey = ConstantConveyor.HV_BAY_KEY;
	BayResponse handleRequest();
	/*String getMyBayKey();*/
}
