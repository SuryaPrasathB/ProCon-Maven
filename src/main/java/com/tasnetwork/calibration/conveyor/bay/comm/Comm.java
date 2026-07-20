package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.spring.orm.model.StateFlow;

public class Comm implements BayStateContext {
	public static Logger logger = Logger.getLogger(Comm.class.getPackage().getName());
	private CommTestBayContext commTestBayManager = new CommTestBayContext();

	public static boolean startProcessRequestedCommBay = false;
	public static boolean stopProcessRequestedCommBay = false;
	public static boolean resetProcessRequestedCommBay = false;

	public static boolean startProcessCompletedCommBay = false;
	public static boolean stopProcessCompletedCommBay = false;
	public static boolean resetProcessCompletedCommBay = false;

	public static boolean abort_CommTest_Bay = false;

	@Override
	public void onStartComplete() {
		setStartProcessCompletedCommBay(true);
	}

	@Override
	public void onStopComplete() {
		Comm.logger.debug("Comm : isStopProcessRequestedCommBay -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		CommTestBayState newState;
		if (stateName.equals("S10_error_Handling") || stateName.startsWith("ERROR")) {
			newState = CommTestBayState.createErrorState(stateName, errorCode);
		} else {
			newState = CommTestBayState.createState(stateName);
		}
		commTestBayManager.setState(newState);
	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = commTestBayManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + commTestBayManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + commTestBayManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public CommTestBayState getPreviousState() {
		return commTestBayManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_CommBay = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CommBay = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
			default:
				return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_CommBay() {
		return tableStatePlanner_CommBay;
	}

	public void setTableStatePlanner_CommBay(ArrayList<StateFlow> tableStatePlanner_CommBay) {
		this.tableStatePlanner_CommBay = tableStatePlanner_CommBay;
	}

	public static boolean isStopProcessRequestedCommBay() {
		return stopProcessRequestedCommBay;
	}

	public static boolean isStopProcessCompletedCommBay() {
		return stopProcessCompletedCommBay;
	}

	public static boolean isResetProcessCompletedCommBay() {
		return resetProcessCompletedCommBay;
	}

	public static void setStopProcessRequestedCommBay(boolean stopProcessRequestedCommBay) {
		Comm.stopProcessRequestedCommBay = stopProcessRequestedCommBay;
	}

	public static void setStopProcessCompletedCommBay(boolean stopProcessCompletedCommBay) {
		Comm.stopProcessCompletedCommBay = stopProcessCompletedCommBay;
	}

	public static void setResetProcessCompletedCommBay(boolean resetProcessCompletedCommBay) {
		Comm.resetProcessCompletedCommBay = resetProcessCompletedCommBay;
	}

	public static boolean isResetProcessRequestedCommBay() {
		return resetProcessRequestedCommBay;
	}

	public static void setResetProcessRequestedCommBay(boolean resetProcessRequestedCommBay) {
		Comm.resetProcessRequestedCommBay = resetProcessRequestedCommBay;
	}

	public static boolean isStartProcessRequestedCommBay() {
		return startProcessRequestedCommBay;
	}

	public static void setStartProcessRequestedCommBay(boolean startProcessRequestedCommBay) {
		Comm.startProcessRequestedCommBay = startProcessRequestedCommBay;
	}

	public static boolean isStartProcessCompletedCommBay() {
		return startProcessCompletedCommBay;
	}

	public static void setStartProcessCompletedCommBay(boolean startProcessCompletedCommBay) {
		Comm.startProcessCompletedCommBay = startProcessCompletedCommBay;
	}

}
