package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface IrtBayState {
	String myBayKey = ConstantConveyor.IR_BAY_KEY;
	BayResponse handleRequest();
	
	default String getMyBayKey() {
		return myBayKey;
	}

	static IrtBayState createState(String stateName) {
		if (stateName.startsWith("ERROR")) {
			return new S11_idle_condition();
		}
		try {
			Class<?> c = Class.forName(IrtBayState.class.getPackage().getName() + "." + stateName);
			return (IrtBayState) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException("IR: Exception loading state: " + stateName, e);
		}
	}

	static IrtBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S10_error_Handling":
			case "S22_error_Handling":
				return new S10_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}
}
