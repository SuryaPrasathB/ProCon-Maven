package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface FtBayState {
	//void handleRequestAndAssignNextState(FtBayState ftBayState);
	String myBayKey = ConstantConveyor.FT_BAY_KEY;
	BayResponse  handleRequest();
	
	//String getMyBayKey() ;
}
