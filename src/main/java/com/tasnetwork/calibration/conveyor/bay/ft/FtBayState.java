package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

/**
 * Interface representing a distinct state within the Functional Test (FT) Bay.
 * All concrete states for the FT Bay must implement this interface to be
 * executed
 * by the {@link FtBayContext}.
 */
public interface FtBayState {

	/**
	 * The identifier key for the FT Bay.
	 * Implicitly public, static, and final.
	 */
	String myBayKey = ConstantConveyor.FT_BAY_KEY;

	/**
	 * Executes the core logic for this specific state.
	 * 
	 * @return A {@link BayResponse} containing the success/failure status and any
	 *         error codes.
	 */
	BayResponse handleRequest();

	/**
	 * Factory method to dynamically instantiate an FT Bay state class using
	 * reflection.
	 *
	 * @param stateName The name of the state class to instantiate.
	 * @return A new instance of {@link FtBayState}.
	 * @throws RuntimeException If the class cannot be found or instantiated.
	 */
	static FtBayState createState(String stateName) {
		Ft.logger.debug("FtBayState.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(FtBayState.class.getPackage().getName() + "." + stateName);
			return (FtBayState) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Ft.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("FT: Exception loading state: " + stateName, e);
		}
	}

	/**
	 * Factory method to instantiate specific error handling states.
	 *
	 * @param stateName The name of the error state.
	 * @param errorCode The error code to pass to the error state.
	 * @return A new instance of {@link FtBayState} representing the error handling
	 *         state.
	 * @throws IllegalArgumentException If the stateName is unknown.
	 */
	static FtBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S22_error_Handling":
				return new S22_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}

	public default String getMyBayKey() {
		return myBayKey;
	}
}
