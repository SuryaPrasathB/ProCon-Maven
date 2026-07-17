package com.tasnetwork.calibration.conveyor.bay.sta_nld2;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S27_turn_on_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S28_turn_off_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S29_turn_on_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S30_turn_off_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import groovyjarjarantlr4.v4.parse.ANTLRParser.labeledAlt_return;
import javafx.scene.control.TableView;

public class StaNld_Bay2 implements BayStateContext {
	public static Logger logger = Logger.getLogger(StaNld_Bay2.class.getPackage().getName()); 
	private STA_NoLoadTestBay2Context sctNltBay2StateManager = new STA_NoLoadTestBay2Context();  
	public static boolean abort_SCT_NLT_Bay2 = false ;

	public static boolean startProcessRequestedStaNldBay2 = false ;
	public static boolean stopProcessRequestedStaNldBay2 = false ;
	public static boolean resetProcessRequestedStaNldBay2 = false ;

	public static boolean startProcessCompletedStaNldBay2 = false ;
	public static boolean stopProcessCompletedStaNldBay2 = false ;
	public static boolean resetProcessCompletedStaNldBay2 = false ;

	@Override
	public void onStartComplete() {
		setStartProcessCompletedStaNldBay2(true);
	}

	@Override
	public void onStopComplete() {
		StaNld_Bay2.logger.debug("StaNld_Bay2 : isStopProcessRequestedStaNldBay2 -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		STA_NoLoadTestBay2State newState;
		if (stateName.equals("S13_error_Handling_Bay2") || stateName.equals("S13_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createSctNltBay2StateInstance(stateName);
		}
		sctNltBay2StateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay2StateManager.processPresentState();

		StaNld_Bay2.logger.debug("StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger.debug("StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay2State getPreviousState(){
		return sctNltBay2StateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_SctNltBay2 = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay2 = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_StaNldTestBay2()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static STA_NoLoadTestBay2State createSctNltBay2StateInstance(String stateName) {
	    try {
	        // Get the fully qualified class name dynamically
	        String packageName = STA_NoLoadTestBay2State.class.getPackage().getName(); // Adjust if necessary
	        Class<?> c = Class.forName(packageName + "." + stateName);

	        // Ensure the class is a subclass of STA_NoLoadTestBay2State
	        if (!STA_NoLoadTestBay2State.class.isAssignableFrom(c)) {
	            throw new IllegalArgumentException("Invalid state class: " + stateName);
	        }

	        // Create an instance using the default constructor
	        return (STA_NoLoadTestBay2State) c.getDeclaredConstructor().newInstance();
	    } catch (ClassNotFoundException e) {
	        throw new IllegalArgumentException("Unknown state: " + stateName, e);
	    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
	        throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
	    }
	}


	private STA_NoLoadTestBay2State createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S13_error_Handling":
			return new S13_error_Handling_Bay2(errorCode);
		case "S13_error_Handling_Bay2":
			return new S13_error_Handling_Bay2(errorCode);	
			
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		//case :
		default:
			return "S13_error_Handling_Bay2";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_StaNldTestBay2() {
		return tableStatePlanner_StaNldTestBay2;
	}

	public void setTableStatePlanner_StaNldTestBay2(ArrayList<StateFlow> tableStatePlanner_SctNltBay2) {
		this.tableStatePlanner_StaNldTestBay2 = tableStatePlanner_SctNltBay2;
	}

	public static boolean isStopProcessRequestedStaNldBay2() {
		return stopProcessRequestedStaNldBay2;
	}

	public static boolean isStopProcessCompletedStaNldBay2() {
		return stopProcessCompletedStaNldBay2;
	}

	public static boolean isResetProcessCompletedStaNldBay2() {
		return resetProcessCompletedStaNldBay2;
	}

	public static void setStopProcessRequestedStaNldBay2(boolean stopProcessRequestedSctNltBay2) {
		StaNld_Bay2.stopProcessRequestedStaNldBay2 = stopProcessRequestedSctNltBay2;
	}

	public static void setStopProcessCompletedStaNldBay2(boolean stopProcessCompletedSctNltBay2) {
		StaNld_Bay2.stopProcessCompletedStaNldBay2 = stopProcessCompletedSctNltBay2;
	}

	public static void setResetProcessCompletedStaNldBay2(boolean resetProcessCompletedSctNltBay2) {
		StaNld_Bay2.resetProcessCompletedStaNldBay2 = resetProcessCompletedSctNltBay2;
	}

	public static boolean isResetProcessRequestedStaNldBay2() {
		return resetProcessRequestedStaNldBay2;
	}

	public static void setResetProcessRequestedStaNldBay2(boolean resetProcessRequestedSctNltBay2) {
		StaNld_Bay2.resetProcessRequestedStaNldBay2 = resetProcessRequestedSctNltBay2;
	}

	public static boolean isStartProcessRequestedStaNldBay2() {
		return startProcessRequestedStaNldBay2;
	}

	public static void setStartProcessRequestedStaNldBay2(boolean startProcessRequestedSctNltBay2) {
		StaNld_Bay2.startProcessRequestedStaNldBay2 = startProcessRequestedSctNltBay2;
	}

	public static boolean isStartProcessCompletedStaNldBay2() {
		return startProcessCompletedStaNldBay2;
	}

	public static void setStartProcessCompletedStaNldBay2(boolean startProcessCompletedSctNltBay2) {
		StaNld_Bay2.startProcessCompletedStaNldBay2 = startProcessCompletedSctNltBay2;
	}



}

