package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface VerificTestBayState {
	String myBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
	BayResponse handleRequest();

	public default String getMyBayKey() {
		return myBayKey;
	}

	static VerificTestBayState createState(String stateName) {
		Verification.logger.debug("VerificTestBayState.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(VerificTestBayState.class.getPackage().getName() + "." + stateName);
			return (VerificTestBayState) c.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			Verification.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("Verific: Exception loading state: " + stateName, e);
		}
	}

	static VerificTestBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S17_error_Handling":
				return new S17_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}
}
