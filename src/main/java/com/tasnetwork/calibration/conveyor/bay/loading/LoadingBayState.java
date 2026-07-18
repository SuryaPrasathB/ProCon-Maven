package com.tasnetwork.calibration.conveyor.bay.loading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface LoadingBayState {
	String myBayKey = ConstantConveyor.LOADING_BAY_KEY ;
	
	default String getMyBayKey() {
		return myBayKey;
	}

	BayResponse handleRequest();

	static LoadingBayState createState(String stateName) {
		try {
			Class<?> clazz = Class.forName("com.tasnetwork.calibration.conveyor.bay.loading." + stateName);
			return (LoadingBayState) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			Loading.logger.error("Failed to create LoadingBayState instance for state: " + stateName, e);
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		}
	}

	static LoadingBayState createErrorState(String stateName, String errorCode) {
		try {
			Class<?> clazz = Class.forName("com.tasnetwork.calibration.conveyor.bay.loading." + stateName);
			return (LoadingBayState) clazz.getDeclaredConstructor(String.class).newInstance(errorCode);
		} catch (Exception e) {
			Loading.logger.error("Failed to create Loading error state instance for state: " + stateName, e);
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		}
	}
}
