package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateContext;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * Context manager for the Functional Test (FT) Bay.
 * Implements {@link BayStateContext} to provide state management, transition logic,
 * and error handling specific to the FT Bay. 
 * Handles dynamic instantiation of state classes via reflection.
 */
public class Ft implements BayStateContext {
	public static Logger logger = Logger.getLogger(Ft.class.getPackage().getName());
	private FtBayContext ftBayStateManager = new FtBayContext();

	public static volatile String stopper_B4_FT_Bay_Status = "STOPPER_B4_FT_BAY_CLOSED";
	public static volatile boolean startProcessRequestedFtBay = false;
	public static volatile boolean stopProcessRequestedFtBay = false;
	public static volatile boolean resetProcessRequestedFtBay = false;

	public static volatile boolean startProcessCompletedFtBay = false;
	public static volatile boolean stopProcessCompletedFtBay = false;
	public static volatile boolean resetProcessCompletedFtBay = false;

	public static boolean abort_FT_Bay = false;

	/**
	 * Callback invoked when the start process for the FT Bay is completed.
	 */
	@Override
	public void onStartComplete() {
		setStartProcessCompletedFtBay(true);
	}

	/**
	 * Callback invoked when the stop process for the FT Bay is completed.
	 * Updates the GUI to reflect the stopped status.
	 */
	@Override
	public void onStopComplete() {
		Ft.logger.debug("FT : isStopProcessRequestedFtBay");

		String pathId = "Ex1";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				"", // ConstantBayStateManage.FT_BAY_HP_SEQ_01,
				"", // ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
				pathId,
				"-",
				"", // portInfo.getPortId(),
				"", // ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"", // "Waiting",
				"StopRequested"// ConstantConveyor.COMM_EXECUTION_STATUS_INP
		);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	/**
	 * Sets the next state for the FT Bay based on the state name and error code.
	 * Instantiates the state dynamically and assigns it to the state manager.
	 *
	 * @param stateName The name of the next state class to load.
	 * @param errorCode The error code associated with a failure, if applicable.
	 */
	@Override
	public void setNextState(String stateName, String errorCode) {
		FtBayState newState;
		if (stateName.equals("S22_error_Handling") || stateName.startsWith("ERROR")) {
			newState = FtBayState.createErrorState(stateName, errorCode);
		} else {
			newState = FtBayState.createState(stateName);
		}
		ftBayStateManager.setState(newState);
	}

	/**
	 * Processes the currently active state in the FT Bay.
	 *
	 * @return A {@link BayResponse} indicating the success/failure status and any error codes.
	 */
	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = ftBayStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	/**
	 * Retrieves the previously processed state.
	 *
	 * @return The last {@link FtBayState} that was processed.
	 */
	public FtBayState getPreviousState() {
		return ftBayStateManager.getLastProcessedBayState();
	}

	public ArrayList<StateFlow> tableStatePlanner_FtBay = new ArrayList<StateFlow>();



	// ==========================================================================================================================================

	/**
	 * Maps a conveyor error code to the corresponding error handling state name.
	 * Used when a state fails and needs to determine the fallback path.
	 *
	 * @param errorCode The error code encountered.
	 * @return The string name of the next state class to handle the error.
	 */
	@Override
	public String getErrorStateInstanceString(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_FT_002:
				return "S28_check_for_stop_latch_B4_status_FT_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_FT_034:
				return "S27_open_stop_latch_b4_FT_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_FT_010:
				return "S17_turn_off_divertor_relay_FT_Bay"; // REJECTION BAY PATH
			case ConvErrorCodeMapping.ERROR_CODE_FT_006:
				return "S17_turn_off_divertor_relay_FT_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_FT_001:
				return "S17_turn_off_divertor_relay_FT_Bay";
			default:
				return "S23_idle_condition"; // S22_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

	public static boolean isStopProcessRequestedFtBay() {
		return stopProcessRequestedFtBay;
	}

	public static boolean isStopProcessCompletedFtBay() {
		return stopProcessCompletedFtBay;
	}

	public static void setStopProcessRequestedFtBay(boolean stopProcessRequestedFtBay) {
		Ft.stopProcessRequestedFtBay = stopProcessRequestedFtBay;
	}

	public static void setStopProcessCompletedFtBay(boolean stopProcessCompletedFtBay) {
		Ft.stopProcessCompletedFtBay = stopProcessCompletedFtBay;
	}

	public static boolean isResetProcessCompletedFtBay() {
		return resetProcessCompletedFtBay;
	}

	public static void setResetProcessCompletedFtBay(boolean resetProcessCompletedFtBay) {
		Ft.resetProcessCompletedFtBay = resetProcessCompletedFtBay;
	}

	public static boolean isResetProcessRequestedFtBay() {
		return resetProcessRequestedFtBay;
	}

	public static void setResetProcessRequestedFtBay(boolean resetProcessRequestedFtBay) {
		Ft.resetProcessRequestedFtBay = resetProcessRequestedFtBay;
	}

	public static boolean isStartProcessRequestedFtBay() {
		return startProcessRequestedFtBay;
	}

	public static void setStartProcessRequestedFtBay(boolean startProcessRequestedFtBay) {
		Ft.startProcessRequestedFtBay = startProcessRequestedFtBay;
	}

	public static boolean isStartProcessCompletedFtBay() {
		return startProcessCompletedFtBay;
	}

	public static void setStartProcessCompletedFtBay(boolean startProcessCompletedFtBay) {
		Ft.startProcessCompletedFtBay = startProcessCompletedFtBay;
	}

}
