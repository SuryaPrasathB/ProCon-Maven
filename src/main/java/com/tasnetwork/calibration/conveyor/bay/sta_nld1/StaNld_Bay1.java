package com.tasnetwork.calibration.conveyor.bay.sta_nld1;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;


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
			newState = STA_NoLoadTestBay1State.createErrorStateInstance(stateName, errorCode);
		} else {
			newState = STA_NoLoadTestBay1State.createSctNltBay1StateInstance(stateName);
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


	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay1 = new ArrayList<StateFlow>();

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

