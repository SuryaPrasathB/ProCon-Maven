package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface STA_NoLoadTestBay1State {
	String myBayKey = ConstantConveyor.STA_NLD1_BAY_KEY;
			BayResponse handleRequest();
}
