package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface LoadingBayState {
	String myBayKey = ConstantConveyor.LOADING_BAY_KEY ;
			BayResponse handleRequest();
}
