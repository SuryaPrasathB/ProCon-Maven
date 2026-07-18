package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.spring.orm.model.StateFlow;

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
		if (stateName.equals("S03_error_Handling") || stateName.startsWith("ERROR")) {
			newState = LoadingBayState.createErrorState(stateName, errorCode);
		} else {
			newState = LoadingBayState.createState(stateName);
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
