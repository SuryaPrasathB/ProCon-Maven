package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Loading implements BayStateContext {
	public static Logger logger = Logger.getLogger(Loading.class.getPackage().getName()); 
	private LoadingBayContext loadingBayStateManager = new LoadingBayContext();  
	
	public static boolean stopProcessRequestedLoadingBay = false ;
	public static boolean resetProcessRequestedLoadingBay = false ;
	
	public static boolean stopProcessCompletedLoadingBay = false ;
	public static boolean resetProcessCompletedLoadingBay = false ;
	
	public static boolean abort_Loading_Bay = false ;
	@Override
	public void onStartComplete() {
		// Nothing for Loading
	}

	@Override
	public void onStopComplete() {
		Loading.logger.debug("Loading : onStopComplete -Pass");
	}
	
	@Override
	public void setNextState(String stateName, String errorCode) {
		LoadingBayState newState;
		if (stateName.equals("S04_error_Handling") || stateName.startsWith("ERROR")) {
			newState = createErrorStateInstance(stateName, errorCode);
		} else {
			newState = createLoadingBayStateInstance(stateName);
		}
		loadingBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = loadingBayStateManager.processPresentState();

		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public LoadingBayState getPreviousState(){
		return loadingBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_LoadingBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_LoadingBay = new ArrayList<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_LoadingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static LoadingBayState createLoadingBayStateInstance(String stateName) {
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_LoadingBay_pushButton_status":
	                return new S01_check_for_LoadingBay_pushButton_status();
	            case "S02_let_pallet_outside_loading_bay":
	                return new S02_let_pallet_outside_loading_bay();
	            case "S03_error_Handling":
	                return new S03_error_Handling(); 
	            case "S04_idle_condition":
	                return new S04_idle_condition(); 
	            case "S05_open_stop_latch_Loading_Bay":
	                return new S05_open_stop_latch_Loading_Bay();
	            case "S06_close_stop_latch_Loading_Bay":
	                return new S06_close_stop_latch_Loading_Bay();
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private LoadingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S03_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		default:
			return "S03_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_LoadingBay() {
		return tableStatePlanner_LoadingBay;
	}

	public void setTableStatePlanner_LoadingBay(ArrayList<StateFlow> tableStatePlanner_LoadingBay) {
		this.tableStatePlanner_LoadingBay = tableStatePlanner_LoadingBay;
	}

	public static boolean isStopProcessRequestedLoadingBay() {
		return stopProcessRequestedLoadingBay;
	}

	public static boolean isStopProcescccsCompletedLoadingBay() {
		return stopProcessCompletedLoadingBay;
	}

	public static boolean isResetProcessCompletedLoadingBay() {
		return resetProcessCompletedLoadingBay;
	}

	public static void setStopProcessRequestedLoadingBay(boolean stopProcessRequestedLoadingBay) {
		Loading.stopProcessRequestedLoadingBay = stopProcessRequestedLoadingBay;
	}

	public static void setStopProcessCompletedLoadingBay(boolean stopProcessCompletedLoadingBay) {
		Loading.stopProcessCompletedLoadingBay = stopProcessCompletedLoadingBay;
	}

	public static void setResetProcessCompletedLoadingBay(boolean resetProcessCompletedLoadingBay) {
		Loading.resetProcessCompletedLoadingBay = resetProcessCompletedLoadingBay;
	}

	public static boolean isResetProcessRequestedLoadingBay() {
		return resetProcessRequestedLoadingBay;
	}

	public static void setResetProcessRequestedLoadingBay(boolean resetProcessRequestedLoadingBay) {
		Loading.resetProcessRequestedLoadingBay = resetProcessRequestedLoadingBay;
	}



}
