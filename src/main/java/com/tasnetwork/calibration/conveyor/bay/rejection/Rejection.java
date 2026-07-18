package com.tasnetwork.calibration.conveyor.bay.rejection;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.spring.orm.model.StateFlow;

public class Rejection implements BayStateContext {
	public static Logger logger = Logger.getLogger(Rejection.class.getPackage().getName()); 
	private RejectionBayContext rejectionBayStateManager = new RejectionBayContext();  
	
	
	public static volatile boolean startProcessRequestedRejectionBay = false ;
	public static boolean stopProcessRequestedRejectionBay = false ;
	public static boolean resetProcessRequestedRejectionBay = false ;
	
	public static boolean startProcessCompletedRejectionBay = false ;
	public static boolean stopProcessCompletedRejectionBay = false ;
	public static boolean resetProcessCompletedRejectionBay = false ;
	
	public static boolean abort_Rejection_Bay = false ;
	@Override
	public void onStartComplete() {
		setStartProcessCompletedRejectionBay(true);
	}

	@Override
	public void onStopComplete() {
		setStopProcessCompletedRejectionBay(false);
		setStopProcessRequestedRejectionBay(false);
		Rejection.logger.debug("Rejection : onStopComplete -Pass");
	}
	
	@Override
	public void setNextState(String stateName, String errorCode) {
		RejectionBayState newState;
		if (stateName.equals("S03_error_Handling") || stateName.startsWith("ERROR")) {
			newState = RejectionBayState.createErrorState(stateName, errorCode);
		} else {
			newState = RejectionBayState.createState(stateName);
		}
		rejectionBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = rejectionBayStateManager.processPresentState();

		Rejection.logger.debug("processCurrentState : " + rejectionBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Rejection.logger.debug("processCurrentState : " + rejectionBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public RejectionBayState getPreviousState(){
		return rejectionBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_RejectionBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_RejectionBay = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		default:
			return "S03_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_RejectionBay() {
		return tableStatePlanner_RejectionBay;
	}

	public void setTableStatePlanner_RejectionBay(ArrayList<StateFlow> tableStatePlanner_RejectionBay) {
		this.tableStatePlanner_RejectionBay = tableStatePlanner_RejectionBay;
	}

	public static boolean isStopProcessRequestedRejectionBay() {
		return stopProcessRequestedRejectionBay;
	}

	public static boolean isStopProcessCompletedRejectionBay() {
		return stopProcessCompletedRejectionBay;
	}

	public static boolean isResetProcessCompletedRejectionBay() {
		return resetProcessCompletedRejectionBay;
	}

	public static void setStopProcessRequestedRejectionBay(boolean stopProcessRequestedRejectionBay) {
		Rejection.stopProcessRequestedRejectionBay = stopProcessRequestedRejectionBay;
	}

	public static void setStopProcessCompletedRejectionBay(boolean stopProcessCompletedRejectionBay) {
		Rejection.stopProcessCompletedRejectionBay = stopProcessCompletedRejectionBay;
	}

	public static void setResetProcessCompletedRejectionBay(boolean resetProcessCompletedRejectionBay) {
		Rejection.resetProcessCompletedRejectionBay = resetProcessCompletedRejectionBay;
	}

	public static boolean isResetProcessRequestedRejectionBay() {
		return resetProcessRequestedRejectionBay;
	}

	public static void setResetProcessRequestedRejectionBay(boolean resetProcessRequestedRejectionBay) {
		Rejection.resetProcessRequestedRejectionBay = resetProcessRequestedRejectionBay;
	}

	public static boolean isStartProcessRequestedRejectionBay() {
		return startProcessRequestedRejectionBay;
	}

	public static void setStartProcessRequestedRejectionBay(boolean startProcessRequestedRejectionBay) {
		Rejection.startProcessRequestedRejectionBay = startProcessRequestedRejectionBay;
	}

	public static boolean isStartProcessCompletedRejectionBay() {
		return startProcessCompletedRejectionBay;
	}

	public static void setStartProcessCompletedRejectionBay(boolean startProcessCompletedRejectionBay) {
		Rejection.startProcessCompletedRejectionBay = startProcessCompletedRejectionBay;
	}



}

