package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface CalibrationBayState {
	String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY ;
	BayResponse handleRequest();

	public default String getMyBayKey() {
		return myBayKey;
	}

	static CalibrationBayState createState(String stateName) {
		Calib.logger.debug("CalibrationBayState.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(CalibrationBayState.class.getPackage().getName() + "." + stateName);
			return (CalibrationBayState) c.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			Calib.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("Calibration: Exception loading state: " + stateName, e);
		}
	}

	static CalibrationBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S10_error_Handling":
				return new S10_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}
}
