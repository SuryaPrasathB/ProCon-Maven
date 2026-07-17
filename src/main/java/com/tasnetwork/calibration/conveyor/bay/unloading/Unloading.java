package com.tasnetwork.calibration.conveyor.bay.unloading;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Unloading implements BayStateContext {
	public static Logger logger = Logger.getLogger(Unloading.class.getPackage().getName()); 
	private UnloadingBayContext unloadingBayStateManager = new UnloadingBayContext();  
	
	
	public static boolean startProcessRequestedUnloadingBay = false ;
	public static boolean stopProcessRequestedUnloadingBay = false ;
	public static boolean resetProcessRequestedUnloadingBay = false ;

	public static boolean startProcessCompletedUnloadingBay = false ;
	public static boolean stopProcessCompletedUnloadingBay = false ;
	public static boolean resetProcessCompletedUnloadingBay = false ;
	
	public static boolean abort_Unloading_Bay = false ;
	@Override
	public void onStartComplete() {
		setStartProcessCompletedUnloadingBay(true);
	}

	@Override
	public void onStopComplete() {
		setStopProcessCompletedUnloadingBay(false);
		setStopProcessRequestedUnloadingBay(false);
		Unloading.logger.debug("Unloading : onStopComplete -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		UnloadingBayState newState;
		if (stateName.equals("S10_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createUnloadingBayStateInstance(stateName);
		}
		unloadingBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = unloadingBayStateManager.processPresentState();

		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public UnloadingBayState getPreviousState(){
		return unloadingBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_UnloadingBay = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_UnloadingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static UnloadingBayState createUnloadingBayStateInstance(String stateName) {
        // Create and return an instance of the state class based on the state name
        switch (stateName) {
            case "S01_check_for_pallet_at_Unloading_Bay":
                return new S01_check_for_pallet_at_Unloading_Bay();
            case "S02_qR_Code_Scanning_of_Pallet":
                return new S02_qR_Code_Scanning_of_Pallet();
            case "S03_check_for_empty_pallet":
                return new S03_check_for_empty_pallet();
            case "S04_check_for_failed_meters":
                return new S04_check_for_failed_meters();
            case "S05_indicate_the_status_of_meters":
                return new S05_indicate_the_status_of_meters();
            case "S06_check_for_alarm_pushButton_status":
                return new S06_check_for_alarm_pushButton_status();
            case "S07_check_for_loadingBay_pushButton_status":
                return new S07_check_for_loadingBay_pushButton_status();
            case "S08_check_for_pallet_at_Loading_Bay":
                return new S08_check_for_pallet_at_Loading_Bay();
            case "S09_let_the_pallet_to_Loading_Bay":
                return new S09_let_the_pallet_to_Loading_Bay();
            case "S10_error_Handling":
                return new S10_error_Handling(); 
            case "S11_idle_condition":
                return new S11_idle_condition(); 
            case "S12_close_stop_latch_Unloading_Bay":
                return new S12_close_stop_latch_Unloading_Bay();
            case "S13_open_stop_latch_Unloading_Bay":
                return new S13_open_stop_latch_Unloading_Bay();
            case "S29_turn_on_tower_lamp_Unloading_Bay" :
                return new S29_turn_on_tower_lamp_Unloading_Bay();
            case "S30_turn_off_tower_lamp_Unloading_Bay" :
                return new S30_turn_off_tower_lamp_Unloading_Bay();
            default:
            	Unloading.logger.debug("createRejectionBayStateInstance: Unknown state: " + stateName); 
                throw new IllegalArgumentException("Unknown state: " + stateName);
        }
    }


	private UnloadingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
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

	public ArrayList<StateFlow> getTableStatePlanner_UnloadingBay() {
		return tableStatePlanner_UnloadingBay;
	}

	public void setTableStatePlanner_UnloadingBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_UnloadingBay = tableStatePlanner_FtBay;
	}

	public static boolean isStopProcessRequestedUnloadingBay() {
		return stopProcessRequestedUnloadingBay;
	}

	public static boolean isStopProcessCompletedUnloadingBay() {
		return stopProcessCompletedUnloadingBay;
	}

	public static boolean isResetProcessCompletedUnloadingBay() {
		return resetProcessCompletedUnloadingBay;
	}

	public static void setStopProcessRequestedUnloadingBay(boolean stopProcessRequestedUnloadingBay) {
		Unloading.stopProcessRequestedUnloadingBay = stopProcessRequestedUnloadingBay;
	}

	public static void setStopProcessCompletedUnloadingBay(boolean stopProcessCompletedUnloadingBay) {
		Unloading.stopProcessCompletedUnloadingBay = stopProcessCompletedUnloadingBay;
	}

	public static void setResetProcessCompletedUnloadingBay(boolean resetProcessCompletedUnloadingBay) {
		Unloading.resetProcessCompletedUnloadingBay = resetProcessCompletedUnloadingBay;
	}

	public static boolean isResetProcessRequestedUnloadingBay() {
		return resetProcessRequestedUnloadingBay;
	}

	public static void setResetProcessRequestedUnloadingBay(boolean resetProcessRequestedUnloadingBay) {
		Unloading.resetProcessRequestedUnloadingBay = resetProcessRequestedUnloadingBay;
	}

	public static boolean isStartProcessRequestedUnloadingBay() {
		return startProcessRequestedUnloadingBay;
	}

	public static void setStartProcessRequestedUnloadingBay(boolean startProcessRequestedUnloadingBay) {
		Unloading.startProcessRequestedUnloadingBay = startProcessRequestedUnloadingBay;
	}

	public static boolean isStartProcessCompletedUnloadingBay() {
		return startProcessCompletedUnloadingBay;
	}

	public static void setStartProcessCompletedUnloadingBay(boolean startProcessCompletedUnloadingBay) {
		Unloading.startProcessCompletedUnloadingBay = startProcessCompletedUnloadingBay;
	}



}

