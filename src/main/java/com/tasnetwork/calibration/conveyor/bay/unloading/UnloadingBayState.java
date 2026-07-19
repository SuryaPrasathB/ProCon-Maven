package com.tasnetwork.calibration.conveyor.bay.unloading;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

/**
 * Interface representing a distinct state within the Unloading Bay.
 * All concrete states for this Bay must implement this interface.
 */
public interface UnloadingBayState {

	/**
	 * The identifier key for the Unloading Bay.
	 */
	String myBayKey = ConstantConveyor.UNLOADING_BAY_KEY;

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
	 * @return A new instance of {@link UnloadingBayState}.
	 * @throws RuntimeException If the class cannot be found or instantiated.
	 */
	static UnloadingBayState createState(String stateName) {
		Unloading.logger.debug("UnloadingBayState.createState : stateName: " + stateName);
		try {
			Class<?> c = Class.forName(UnloadingBayState.class.getPackage().getName() + "." + stateName);
			return (UnloadingBayState) c.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Unloading.logger.error("Failed to dynamically instantiate state: " + stateName, e);
			throw new RuntimeException("Unloading: Exception loading state: " + stateName, e);
		}
	}

	/**
	 * Factory method to instantiate specific error handling states.
	 *
	 * @param stateName The name of the error state.
	 * @param errorCode The error code to pass to the error state.
	 * @return A new instance of {@link UnloadingBayState} representing the error handling state.
	 */
	static UnloadingBayState createErrorState(String stateName, String errorCode) {
		switch (stateName) {
			case "S10_error_Handling":
				return new S10_error_Handling(errorCode);
			default:
				throw new IllegalArgumentException("Unknown error state: " + stateName);
		}
	}

	public default String getMyBayKey() {
		return myBayKey;
	}
}
