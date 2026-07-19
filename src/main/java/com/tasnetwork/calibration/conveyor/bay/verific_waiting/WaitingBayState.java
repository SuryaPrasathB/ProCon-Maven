package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface WaitingBayState {
	String myBayKey = ConstantConveyor.WAITING_BAY_KEY;
	BayResponse handleRequest();

	public default String getMyBayKey() {
		return myBayKey;
	}

	static WaitingBayState createState(String stateName) {
		VerificWaiting.logger.debug("WaitingBayState.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(WaitingBayState.class.getPackage().getName() + "." + stateName);
			return (WaitingBayState) c.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			VerificWaiting.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("Waiting: Exception loading state: " + stateName, e);
		}
	}

	static WaitingBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S05_error_Handling":
				return new S05_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}
}
