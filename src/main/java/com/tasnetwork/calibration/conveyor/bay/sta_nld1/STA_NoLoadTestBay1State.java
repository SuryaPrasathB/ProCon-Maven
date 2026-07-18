package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

public interface STA_NoLoadTestBay1State {
	String myBayKey = ConstantConveyor.STA_NLD1_BAY_KEY;
	BayResponse handleRequest();

	default String getMyBayKey() {
		return myBayKey;
	}

	public static STA_NoLoadTestBay1State createSctNltBay1StateInstance(String stateName) {
		try {
			// Get the fully qualified class name dynamically
			String packageName = STA_NoLoadTestBay1State.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of STA_NoLoadTestBay1State
			if (!STA_NoLoadTestBay1State.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (STA_NoLoadTestBay1State) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}

	public static STA_NoLoadTestBay1State createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S13_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S13_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		case "S22_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S22_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}
}
