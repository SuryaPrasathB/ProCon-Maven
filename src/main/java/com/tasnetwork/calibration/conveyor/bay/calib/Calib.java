package com.tasnetwork.calibration.conveyor.bay.calib;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Calib implements BayStateContext {
	public static Logger logger = Logger.getLogger(Calib.class.getPackage().getName()); 
	private CalibrationBayContext calibBayStateManager = new CalibrationBayContext();  

	public static boolean startProcessRequestedCalibBay = false ;
	public static boolean stopProcessRequestedCalibBay = false ;
	public static boolean resetProcessRequestedCalibBay = false ;

	public static boolean startProcessCompletedCalibBay = false ;
	public static boolean stopProcessCompletedCalibBay = false ;
	public static boolean resetProcessCompletedCalibBay = false ;

	public static boolean abort_Calib_Bay = false ;
	@Override
	public void onStartComplete() {
		setStartProcessCompletedCalibBay(true);
	}

	@Override
	public void onStopComplete() {
		Calib.logger.debug("CalibrationBay : isStopProcessRequestedCalibBay -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		CalibrationBayState newState;
		if (stateName.equals("S10_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createCalibBayStateInstance(stateName);
		}
		calibBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = calibBayStateManager.processPresentState();

		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CalibrationBayState getPreviousState(){
		return calibBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_CalibBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CalibBay = new ArrayList<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_CalibBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static CalibrationBayState createCalibBayStateInstance(String stateName) {
		try {
			// Get the fully qualified class name dynamically
			String packageName = CalibrationBayState.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of CalibrationBayState
			if (!CalibrationBayState.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (CalibrationBayState) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}

	private CalibrationBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new  S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {

		default:
			return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_CalibBay() {
		return tableStatePlanner_CalibBay;
	}

	public void setTableStatePlanner_CalibBay(ArrayList<StateFlow> tableStatePlanner_CalibBay) {
		this.tableStatePlanner_CalibBay = tableStatePlanner_CalibBay;
	}

	public static boolean isStopProcessRequestedCalibBay() {
		return stopProcessRequestedCalibBay;
	}

	public static boolean isStopProcessCompletedCalibBay() {
		return stopProcessCompletedCalibBay;
	}

	public static boolean isResetProcessCompletedCalibBay() {
		return resetProcessCompletedCalibBay;
	}

	public static void setStopProcessRequestedCalibBay(boolean stopProcessRequestedCalibBay) {
		Calib.stopProcessRequestedCalibBay = stopProcessRequestedCalibBay;
	}

	public static void setStopProcessCompletedCalibBay(boolean stopProcessCompletedCalibBay) {
		Calib.stopProcessCompletedCalibBay = stopProcessCompletedCalibBay;
	}

	public static void setResetProcessCompletedCalibBay(boolean resetProcessCompletedCalibBay) {
		Calib.resetProcessCompletedCalibBay = resetProcessCompletedCalibBay;
	}

	public static boolean isResetProcessRequestedCalibBay() {
		return resetProcessRequestedCalibBay;
	}

	public static void setResetProcessRequestedCalibBay(boolean resetProcessRequestedCalibBay) {
		Calib.resetProcessRequestedCalibBay = resetProcessRequestedCalibBay;
	}

	public static boolean isStartProcessRequestedCalibBay() {
		return startProcessRequestedCalibBay;
	}

	public static void setStartProcessRequestedCalibBay(boolean startProcessRequestedCalibBay) {
		Calib.startProcessRequestedCalibBay = startProcessRequestedCalibBay;
	}

	public static boolean isStartProcessCompletedCalibBay() {
		return startProcessCompletedCalibBay;
	}

	public static void setStartProcessCompletedCalibBay(boolean startProcessCompletedCalibBay) {
		Calib.startProcessCompletedCalibBay = startProcessCompletedCalibBay;
	}



}

