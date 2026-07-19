package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.spring.orm.model.StateFlow;

public class StaNld_Bay2 implements BayStateContext {
	public static Logger logger = Logger.getLogger(StaNld_Bay2.class.getPackage().getName());
	private STA_NoLoadTestBay2Context sctNltBay2StateManager = new STA_NoLoadTestBay2Context();
	public static boolean abort_SCT_NLT_Bay2 = false;

	public static boolean startProcessRequestedStaNldBay2 = false;
	public static boolean stopProcessRequestedStaNldBay2 = false;
	public static boolean resetProcessRequestedStaNldBay2 = false;

	public static boolean startProcessCompletedStaNldBay2 = false;
	public static boolean stopProcessCompletedStaNldBay2 = false;
	public static boolean resetProcessCompletedStaNldBay2 = false;

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
		if (stateName.equals("S13_error_Handling_Bay2") || stateName.equals("S13_error_Handling")
 				|| stateName.startsWith("ERROR")) {
			newState = STA_NoLoadTestBay2State.createErrorState(stateName, errorCode);
		} else {
			newState = STA_NoLoadTestBay2State.createState(stateName);
		}
		sctNltBay2StateManager.setState(newState);
	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = sctNltBay2StateManager.processPresentState();

		StaNld_Bay2.logger.debug(
				"StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger.debug(
				"StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public STA_NoLoadTestBay2State getPreviousState() {
		return sctNltBay2StateManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_SctNltBay2 = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay2 = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
			// case :
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
