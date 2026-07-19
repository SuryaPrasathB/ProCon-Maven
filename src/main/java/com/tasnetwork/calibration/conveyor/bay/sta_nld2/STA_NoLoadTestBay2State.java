package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

/**
 * Interface representing a distinct state within the STA No Load Test Bay 2.
 * All concrete states for this Bay must implement this interface.
 */
public interface STA_NoLoadTestBay2State {

	/**
	 * The identifier key for the STA No Load Test Bay 2.
	 */
	String myBayKey = ConstantConveyor.STA_NLD2_BAY_KEY;

	/**
	 * Executes the core logic for this specific state.
	 * 
	 * @return A {@link BayResponse} containing the success/failure status and any error codes.
	 */
	BayResponse handleRequest();

	/**
	 * Factory method to dynamically instantiate a state class using reflection.
	 *
	 * @param stateName The name of the state class to instantiate.
	 * @return A new instance of {@link STA_NoLoadTestBay2State}.
	 * @throws RuntimeException If the class cannot be found or instantiated.
	 */
	static STA_NoLoadTestBay2State createState(String stateName) {
		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2State.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(STA_NoLoadTestBay2State.class.getPackage().getName() + "." + stateName);
			return (STA_NoLoadTestBay2State) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			StaNld_Bay2.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("STA2: Exception loading state: " + stateName, e);
		}
	}

	/**
	 * Factory method to instantiate specific error handling states.
	 *
	 * @param stateName The name of the error state.
	 * @param errorCode The error code to pass to the error state.
	 * @return A new instance of {@link STA_NoLoadTestBay2State} representing the error handling state.
	 */
	static STA_NoLoadTestBay2State createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S13_error_Handling":
			case "S13_error_Handling_Bay2":
				return new S13_error_Handling_Bay2(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}

	public default String getMyBayKey() {
		return myBayKey;
	}
}
