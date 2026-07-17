package com.tasnetwork.calibration.conveyor.bay.sta_nld1;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S041_Start_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S051_Stop_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S042_Start_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S043_Get_Test_Point_Status_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S044_Close_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S045_Select_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S046_Stop_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class StaNld_Bay1 implements BayStateContext {
	public static Logger logger = Logger.getLogger(StaNld_Bay1.class.getPackage().getName()); 
	private STA_NoLoadTestBay1Context sctNltBay1StateManager = new STA_NoLoadTestBay1Context();  
	public static boolean abort_SCT_NLT_Bay1 = false ;

	public static boolean startProcessRequestedStaNldBay1 = false ;
	public static boolean stopProcessRequestedStaNldBay1 = false ;
	public static boolean resetProcessRequestedStaNldBay1 = false ;

	public static boolean startProcessCompletedStaNldBay1 = false ;
	public static boolean stopProcessCompletedStaNldBay1 = false ;
	public static boolean resetProcessCompletedStaNldBay1 = false ;

	@Override
	public void onStartComplete() {
		setStartProcessCompletedStaNldBay1(true);
	}

	@Override
	public void onStopComplete() {
		StaNld_Bay1.logger.debug("StaNld_Bay1 : isStopProcessRequestedStaNldBay1 -Pass");
	}


	@Override
	public void setNextState(String stateName, String errorCode) {
		STA_NoLoadTestBay1State newState;
		if (stateName.equals("S13_error_Handling_Bay1") || stateName.equals("S13_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createSctNltBay1StateInstance(stateName);
		}
		sctNltBay1StateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay1StateManager.processPresentState();

		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay1State getPreviousState(){
		return sctNltBay1StateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_SctNltBay1 = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay1 = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_StaNldTestBay1()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
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
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}


	private STA_NoLoadTestBay1State createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S13_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S13_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020 :
			return "S12_release_the_pallet_release_semaphore_Bay1";
		default:
			return "S13_error_Handling_Bay1";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_StaNldTestBay1() {
		return tableStatePlanner_StaNldTestBay1;
	}

	public void setTableStatePlanner_StaNldTestBay1(ArrayList<StateFlow> tableStatePlanner_SctNltBay1) {
		this.tableStatePlanner_StaNldTestBay1 = tableStatePlanner_SctNltBay1;
	}

	public static boolean isStopProcessRequestedStaNldBay1() {
		return stopProcessRequestedStaNldBay1;
	}

	public static boolean isStopProcessCompletedStaNldBay1() {
		return stopProcessCompletedStaNldBay1;
	}

	public static boolean isResetProcessCompletedStaNldBay1() {
		return resetProcessCompletedStaNldBay1;
	}

	public static void setStopProcessRequestedStaNldBay1(boolean stopProcessRequestedSctNltBay1) {
		StaNld_Bay1.stopProcessRequestedStaNldBay1 = stopProcessRequestedSctNltBay1;
	}

	public static void setStopProcessCompletedStaNldBay1(boolean stopProcessCompletedSctNltBay1) {
		StaNld_Bay1.stopProcessCompletedStaNldBay1 = stopProcessCompletedSctNltBay1;
	}

	public static void setResetProcessCompletedStaNldBay1(boolean resetProcessCompletedSctNltBay1) {
		StaNld_Bay1.resetProcessCompletedStaNldBay1 = resetProcessCompletedSctNltBay1;
	}

	public static boolean isStartProcessRequestedStaNldBay1() {
		return startProcessRequestedStaNldBay1;
	}

	public static void setStartProcessRequestedStaNldBay1(boolean startProcessRequestedSctNltBay1) {
		StaNld_Bay1.startProcessRequestedStaNldBay1 = startProcessRequestedSctNltBay1;
	}

	public static boolean isResetProcessRequestedStaNldBay1() {
		return resetProcessRequestedStaNldBay1;
	}

	public static void setResetProcessRequestedStaNldBay1(boolean resetProcessRequestedSctNltBay1) {
		StaNld_Bay1.resetProcessRequestedStaNldBay1 = resetProcessRequestedSctNltBay1;
	}

	public static boolean isStartProcessCompletedStaNldBay1() {
		return startProcessCompletedStaNldBay1;
	}

	public static void setStartProcessCompletedStaNldBay1(boolean startProcessCompletedSctNltBay1) {
		StaNld_Bay1.startProcessCompletedStaNldBay1 = startProcessCompletedSctNltBay1;
	}



}

