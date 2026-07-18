package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

public class Ir implements BayStateContext {
	public static Logger logger = Logger.getLogger(Ir.class.getPackage().getName()); 
	private IrtBayContext IrtBayStateManager = new IrtBayContext();
	
	public static boolean StartProcessRequestedIrtBay = false ;
	public static boolean stopProcessRequestedIrtBay = false ;
	public static boolean resetProcessRequestedIrtBay = false ;
	
	public static boolean startProcessCompletedIrtBay = false ;
	public static boolean stopProcessCompletedIrtBay = false ;
	public static boolean resetProcessCompletedIrtBay = false ;
	
	public static boolean abort_IRT_Bay = false ;
	@Override
	public void onStartComplete() {
		setStartProcessCompletedIrtBay(true);
	}

	@Override
	public void onStopComplete() {
		Ir.logger.debug("InsulationResistanceTestBay : isStopProcessRequestedIrtBay -Pass");
	}
	
	@Override
	public void setNextState(String stateName, String errorCode) {
		IrtBayState newState;
		if (stateName.equals("S10_error_Handling") || stateName.equals("S22_error_Handling") || stateName.startsWith("ERROR")) {
			newState = IrtBayState.createErrorState(stateName, errorCode);
		} else {
			newState = IrtBayState.createState(stateName);
		}
		IrtBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = IrtBayStateManager.processPresentState();

		Ir.logger.debug("processCurrentState : " + IrtBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ir.logger.debug("processCurrentState : " + IrtBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public IrtBayState getPreviousState(){
		return IrtBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_IrtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_IrtBay = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
		case  ConvErrorCodeMapping.ERROR_CODE_IRT_010 :
			return "S06_open_the_fingerTip_Latch";
			
		default:
			return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_IrtBay() {
		return tableStatePlanner_IrtBay;
	}

	public void setTableStatePlanner_IrtBay(ArrayList<StateFlow> tableStatePlanner_IrtBay) {
		this.tableStatePlanner_IrtBay = tableStatePlanner_IrtBay;
	}

	public static boolean isStopProcessRequestedIrtBay() {
		return stopProcessRequestedIrtBay;
	}

	public static boolean isStopProcessCompletedIrtBay() {
		return stopProcessCompletedIrtBay;
	}

	public static boolean isResetProcessCompletedIrtBay() {
		return resetProcessCompletedIrtBay;
	}

	public static void setStopProcessRequestedIrtBay(boolean stopProcessRequestedIrtBay) {
		Ir.stopProcessRequestedIrtBay = stopProcessRequestedIrtBay;
	}

	public static void setStopProcessCompletedIrtBay(boolean stopProcessCompletedIrtBay) {
		Ir.stopProcessCompletedIrtBay = stopProcessCompletedIrtBay;
	}

	public static void setResetProcessCompletedIrtBay(boolean resetProcessCompletedIrtBay) {
		Ir.resetProcessCompletedIrtBay = resetProcessCompletedIrtBay;
	}

	public static boolean isResetProcessRequestedIrtBay() {
		return resetProcessRequestedIrtBay;
	}

	public static void setResetProcessRequestedIrtBay(boolean resetProcessRequestedIrtBay) {
		Ir.resetProcessRequestedIrtBay = resetProcessRequestedIrtBay;
	}

	public static boolean isStartProcessRequestedIrtBay() {
		return StartProcessRequestedIrtBay;
	}

	public static void setStartProcessRequestedIrtBay(boolean startProcessRequestedIrtBay) {
		StartProcessRequestedIrtBay = startProcessRequestedIrtBay;
	}

	public static boolean isStartProcessCompletedIrtBay() {
		return startProcessCompletedIrtBay;
	}

	public static void setStartProcessCompletedIrtBay(boolean startProcessCompletedIrtBay) {
		Ir.startProcessCompletedIrtBay = startProcessCompletedIrtBay;
	}



}

