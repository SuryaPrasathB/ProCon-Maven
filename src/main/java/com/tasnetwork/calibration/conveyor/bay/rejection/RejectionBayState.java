package com.tasnetwork.calibration.conveyor.bay.rejection;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface RejectionBayState {
	String myBayKey = ConstantConveyor.REJECTION_BAY_KEY;
	
	default String getMyBayKey() {
		return myBayKey;
	}

	BayResponse handleRequest();

	static RejectionBayState createState(String stateName) {
		try {
			Class<?> clazz = Class.forName("com.tasnetwork.calibration.conveyor.bay.rejection." + stateName);
			return (RejectionBayState) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			Rejection.logger.error("Failed to create RejectionBayState instance for state: " + stateName, e);
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		}
	}

	static RejectionBayState createErrorState(String stateName, String errorCode) {
		try {
			Class<?> clazz = Class.forName("com.tasnetwork.calibration.conveyor.bay.rejection." + stateName);
			return (RejectionBayState) clazz.getDeclaredConstructor(String.class).newInstance(errorCode);
		} catch (Exception e) {
			Rejection.logger.error("Failed to create Rejection error state instance for state: " + stateName, e);
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		}
	}
}
