package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

public class Verification implements BayStateContext {
	public static Logger logger = Logger.getLogger(Verification.class.getPackage().getName());
	private VerificationBayContext VerificTestBayStateManager = new VerificationBayContext();

	public static boolean startProcessRequestedVerificBay = false;
	public static boolean stopProcessRequestedVerificBay = false;
	public static boolean resetProcessRequestedVerificBay = false;

	public static boolean startProcessCompletedVerificBay = false;
	public static boolean stopProcessCompletedVerificBay = false;
	public static boolean resetProcessCompletedVerificBay = false;

	public static boolean abort_VerificTest_Bay = false;

	@Override
	public void onStartComplete() {
		setStartProcessCompletedVerificBay(true);
	}

	@Override
	public void onStopComplete() {
		Verification.logger.debug("VerificationTestBay : isStopProcessRequestedVerificBay -Pass");
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		VerificTestBayState newState;
		if (stateName.equals("S17_error_Handling") || stateName.startsWith("ERROR")) {
			newState = VerificTestBayState.createErrorState(stateName, errorCode);
		} else {
			newState = VerificTestBayState.createState(stateName);
		}
		VerificTestBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = VerificTestBayStateManager.processPresentState();

		Verification.logger
				.debug("processCurrentState : " + VerificTestBayStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		Verification.logger
				.debug("processCurrentState : " + VerificTestBayStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public VerificTestBayState getPreviousState() {
		return VerificTestBayStateManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_VerificBay = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_VerificBay = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022:
				return "S01_check_for_pallets_at_Verific_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013:
				return "S12_check_for_pallets_at_SCT_NLT_Bay2";
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014:
				return "S08_check_for_pallets_at_SCT_NLT_Bay1";
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_030:
				return S18_idle_condition.class.getSimpleName();
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_031:
				return S18_idle_condition.class.getSimpleName();
			default:
				return "S17_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_VerificBay() {
		return tableStatePlanner_VerificBay;
	}

	public void setTableStatePlanner_VerificBay(ArrayList<StateFlow> tableStatePlanner_VerificBay) {
		this.tableStatePlanner_VerificBay = tableStatePlanner_VerificBay;
	}

	public static boolean isStopProcessRequestedVerificBay() {
		return stopProcessRequestedVerificBay;
	}

	public static boolean isStopProcessCompletedVerificBay() {
		return stopProcessCompletedVerificBay;
	}

	public static boolean isResetProcessCompletedVerificBay() {
		return resetProcessCompletedVerificBay;
	}

	public static void setStopProcessRequestedVerificBay(boolean stopProcessRequestedVerificBay) {
		Verification.stopProcessRequestedVerificBay = stopProcessRequestedVerificBay;
	}

	public static void setStopProcessCompletedVerificBay(boolean stopProcessCompletedVerificBay) {
		Verification.stopProcessCompletedVerificBay = stopProcessCompletedVerificBay;
	}

	public static void setResetProcessCompletedVerificBay(boolean resetProcessCompletedVerificBay) {
		Verification.resetProcessCompletedVerificBay = resetProcessCompletedVerificBay;
	}

	public static boolean isResetProcessRequestedVerificBay() {
		return resetProcessRequestedVerificBay;
	}

	public static void setResetProcessRequestedVerificBay(boolean resetProcessRequestedVerificBay) {
		Verification.resetProcessRequestedVerificBay = resetProcessRequestedVerificBay;
	}

	public static boolean isStartProcessRequestedVerificBay() {
		return startProcessRequestedVerificBay;
	}

	public static void setStartProcessRequestedVerificBay(boolean startProcessRequestedVerificBay) {
		Verification.startProcessRequestedVerificBay = startProcessRequestedVerificBay;
	}

	public static boolean isStartProcessCompletedVerificBay() {
		return startProcessCompletedVerificBay;
	}

	public static void setStartProcessCompletedVerificBay(boolean startProcessCompletedVerificBay) {
		Verification.startProcessCompletedVerificBay = startProcessCompletedVerificBay;
	}

}
