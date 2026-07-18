package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;


public class Hv implements BayStateContext {
	public static Logger logger = Logger.getLogger(Hv.class.getPackage().getName());
	private HvtBayContext hvtBayStateManager = new HvtBayContext();

	public static boolean startProcessRequestedHvtBay = false;
	public static boolean stopProcessRequestedHvtBay = false;
	public static boolean resetProcessRequestedHvtBay = false;

	public static boolean startProcessCompletedHvtBay = false;
	public static boolean stopProcessCompletedHvtBay = false;
	public static boolean resetProcessCompletedHvtBay = false;

	public static boolean abort_HVT_Bay = false;

	@Override
	public void onStartComplete() {
		setStartProcessCompletedHvtBay(true);
	}

	@Override
	public void onStopComplete() {
		// Stop completion logic
	}

	@Override
	public void setNextState(String stateName, String errorCode) {
		HvtBayState newState;
		if (stateName.equals("S10_error_Handling")) {
			newState = HvtBayState.createErrorState(stateName, errorCode);
		} else {
			newState = HvtBayState.createState(stateName);
		}
		hvtBayStateManager.setState(newState);
	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = hvtBayStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + hvtBayStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + hvtBayStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public HvtBayState getPreviousState() {
		return hvtBayStateManager.getLastProcessedBayState();
	}

	// ================================================================

	public ArrayList<StateFlow> tableStatePlanner_HvtBay = new ArrayList<StateFlow>();

	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_HVT_010:
				return "S051_stop_HV_source";

			default:
				return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_HvtBay() {
		return tableStatePlanner_HvtBay;
	}

	public void setTableStatePlanner_HvtBay(ArrayList<StateFlow> tableStatePlanner_HvtBay) {
		this.tableStatePlanner_HvtBay = tableStatePlanner_HvtBay;
	}

	public static boolean isStopProcessRequestedHvtBay() {
		return stopProcessRequestedHvtBay;
	}

	public static boolean isStopProcessCompletedHvtBay() {
		return stopProcessCompletedHvtBay;
	}

	public static boolean isResetProcessCompletedHvtBay() {
		return resetProcessCompletedHvtBay;
	}

	public static void setStopProcessRequestedHvtBay(boolean stopProcessRequestedHvtBay) {
		Hv.stopProcessRequestedHvtBay = stopProcessRequestedHvtBay;
	}

	public static void setStopProcessCompletedHvtBay(boolean stopProcessCompletedHvtBay) {
		Hv.stopProcessCompletedHvtBay = stopProcessCompletedHvtBay;
	}

	public static void setResetProcessCompletedHvtBay(boolean resetProcessCompletedHvtBay) {
		Hv.resetProcessCompletedHvtBay = resetProcessCompletedHvtBay;
	}

	public static boolean isResetProcessRequestedHvtBay() {
		return resetProcessRequestedHvtBay;
	}

	public static void setResetProcessRequestedHvtBay(boolean resetProcessRequestedHvtBay) {
		Hv.resetProcessRequestedHvtBay = resetProcessRequestedHvtBay;
	}

	public static boolean isStartProcessRequestedHvtBay() {
		return startProcessRequestedHvtBay;
	}

	public static void setStartProcessRequestedHvtBay(boolean startProcessRequestedHvtBay) {
		Hv.startProcessRequestedHvtBay = startProcessRequestedHvtBay;
	}

	public static boolean isStartProcessCompletedHvtBay() {
		return startProcessCompletedHvtBay;
	}

	public static void setStartProcessCompletedHvtBay(boolean startProcessCompletedHvtBay) {
		Hv.startProcessCompletedHvtBay = startProcessCompletedHvtBay;
	}

}
