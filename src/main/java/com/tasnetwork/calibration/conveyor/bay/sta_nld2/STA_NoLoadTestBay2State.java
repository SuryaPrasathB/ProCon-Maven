package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface STA_NoLoadTestBay2State {
	String myBayKey = ConstantConveyor.STA_NLD2_BAY_KEY;
	BayResponse handleRequest();
}
