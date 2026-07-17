package com.tasnetwork.calibration.conveyor.bay.verific_waiting;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class VerificWaiting implements BayStateContext {
	public static Logger logger = Logger.getLogger(VerificWaiting.class.getPackage().getName()); 
	private WaitingBayContext WaitingBayStateManager = new WaitingBayContext();  
	
	
	public static boolean startProcessRequestedWaitingBay = false ;
	public static boolean stopProcessRequestedWaitingBay = false ;
	public static boolean resetProcessRequestedWaitingBay = false ;

	public static boolean startProcessCompletedWaitingBay = false ;
 	public static boolean stopProcessCompletedWaitingBay = false ;
	public static boolean resetProcessCompletedWaitingBay = false ;
	
	public static boolean abort_Waiting_Bay = false ;
	@Override
	public void onStartComplete() {
		setStartProcessCompletedWaitingBay(true);
	}

	@Override
	public void onStopComplete() {
		VerificWaiting.logger.debug("Waiting : onStopComplete -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		WaitingBayState newState;
		if (stateName.equals("S05_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createWaitingBayStateInstance(stateName);
		}
		WaitingBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = WaitingBayStateManager.processPresentState();

		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public WaitingBayState getPreviousState(){
		return WaitingBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_WaitingBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_WaitingBay = new ArrayList<StateFlow>();
	
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_WaitingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static WaitingBayState createWaitingBayStateInstance(String stateName) {           
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_all_pallets_at_Waiting_Bay":
	                return new S01_check_for_all_pallets_at_Waiting_Bay();
	            case "S02_check_for_pallets_at_Verific_Bay":
	                return new S02_check_for_pallets_at_Verific_Bay();
	            case "S03_let_the_pallet_to_Verific_Bay":
	                return new S03_let_the_pallet_to_Verific_Bay();
	            case "S04_ensure_all_pallets_reached_Verific_Bay":
	                return new S04_ensure_all_pallets_reached_Verific_Bay();
	            case "S05_error_Handling":
	                return new S05_error_Handling(); 
	            case "S06_idle_condition":
	                return new S06_idle_condition(); 
	            case "S07_open_stop_latch_Waiting_Bay":
	                return new S07_open_stop_latch_Waiting_Bay();
	            case "S08_close_stop_latch_Waiting_Bay":
	                return new S08_close_stop_latch_Waiting_Bay();
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private WaitingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S05_error_Handling":
			return new S05_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		default:
			return "S05_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_WaitingBay() {
		return tableStatePlanner_WaitingBay;
	}

	public void setTableStatePlanner_WaitingBay(ArrayList<StateFlow> tableStatePlanner_WaitingBay) {
		this.tableStatePlanner_WaitingBay = tableStatePlanner_WaitingBay;
	}

	public static boolean isStopProcessRequestedWaitingBay() {
		return stopProcessRequestedWaitingBay;
	}

	public static boolean isStopProcessCompletedWaitingBay() {
		return stopProcessCompletedWaitingBay;
	}

	public static boolean isResetProcessCompletedWaitingBay() {
		return resetProcessCompletedWaitingBay;
	}

	public static void setStopProcessRequestedWaitingBay(boolean stopProcessRequestedWaitingBay) {
		VerificWaiting.stopProcessRequestedWaitingBay = stopProcessRequestedWaitingBay;
	}

	public static void setStopProcessCompletedWaitingBay(boolean stopProcessCompletedWaitingBay) {
		VerificWaiting.stopProcessCompletedWaitingBay = stopProcessCompletedWaitingBay;
	}

	public static void setResetProcessCompletedWaitingBay(boolean resetProcessCompletedWaitingBay) {
		VerificWaiting.resetProcessCompletedWaitingBay = resetProcessCompletedWaitingBay;
	}

	public static boolean isResetProcessRequestedWaitingBay() {
		return resetProcessRequestedWaitingBay;
	}

	public static void setResetProcessRequestedWaitingBay(boolean resetProcessRequestedWaitingBay) {
		VerificWaiting.resetProcessRequestedWaitingBay = resetProcessRequestedWaitingBay;
	}

	public static boolean isStartProcessRequestedWaitingBay() {
		return startProcessRequestedWaitingBay;
	}

	public static void setStartProcessRequestedWaitingBay(boolean startProcessRequestedWaitingBay) {
		VerificWaiting.startProcessRequestedWaitingBay = startProcessRequestedWaitingBay;
	}

	public static boolean isStartProcessCompletedWaitingBay() {
		return startProcessCompletedWaitingBay;
	}

	public static void setStartProcessCompletedWaitingBay(boolean startProcessCompletedWaitingBay) {
		VerificWaiting.startProcessCompletedWaitingBay = startProcessCompletedWaitingBay;
	}

 


}

