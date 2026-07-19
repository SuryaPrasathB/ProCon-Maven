package com.tasnetwork.calibration.conveyor.bay.unloading;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.spring.orm.model.StateFlow;

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
			newState = UnloadingBayState.createErrorState(stateName, errorCode);
		} else {
			newState = UnloadingBayState.createState(stateName);
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

