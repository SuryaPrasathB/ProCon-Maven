package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface HvtBayState {
	String myBayKey = ConstantConveyor.HV_BAY_KEY;

	BayResponse handleRequest();
	
	default String getMyBayKey() {
		return myBayKey;
	}

	static HvtBayState createState(String stateName) {
		if (stateName.startsWith("ERROR")) {
			return new S11_idle_condition();
		}
		try {
			Class<?> c = Class.forName(HvtBayState.class.getPackage().getName() + "." + stateName);
			return (HvtBayState) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException("HV: Exception loading state: " + stateName, e);
		}
	}

	static HvtBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S10_error_Handling":
				return new S10_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}
}
