package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class STA_NoLoadTestBay1Bypass extends TimerTask {

	// public static Logger logger =
	// Logger.getLogger(STA_NoLoadTestBay1.class.getPackage().getName());
	private STA_NoLoadTestBay1Context staBay1BypassStateManager = new STA_NoLoadTestBay1Context();

	public void run() {

		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Bypass : Entry");
		tableBypassStatePlanner_StaBay1.clear();
		manageSTA_NoLoadTestBay1BypassStates2();

		String pathId = "ExR";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.STA_NLD1_BAY_KEY,
				"", // ConstantBayStateManage.Hv_BAY_HP_SEQ_01,
				"", // ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
				pathId,
				"-",
				"", // portInfo.getPortId(),
				"", // ConstantBayPortNameMapping.Hv_PORT_NAME_SNSR_PALLET,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"", // "Waiting",
				"Bypass Completed"// ConstantConveyor.COMM_EXECUTION_STATUS_INP
		);

		int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	// ====================================================================================================================

	public void setNextState(STA_NoLoadTestBay1State newState) {
		// Set previous state here

		// Set the new state
		staBay1BypassStateManager.setState(newState);

	}
	// ====================================================================================================================

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = staBay1BypassStateManager.processPresentState();

		StaNld_Bay1.logger
				.debug("processCurrentState : " + staBay1BypassStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		StaNld_Bay1.logger
				.debug("processCurrentState : " + staBay1BypassStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}
	// ====================================================================================================================

	public STA_NoLoadTestBay1State getPreviousState() {
		return staBay1BypassStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageSTA_NoLoadTestBay1BypassStates2() {

		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1 : manageSTA_NoLoadTestBay1BypassStates2 : Entry");

		// setTableStatePlanner_HvBay(StatePlannerController.getTableStatePlannerHvBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD1_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_StaBay1(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		String errorCode = "";

		if (getTableStatePlanner_StaBay1().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaBay1().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay1State currentState = createStateInstance(currentStateName, errorCode); // Create the state
																										// instance
			setNextState(currentState); // Set the first state

			StaNld_Bay1.logger.debug(
					"STA_NoLoadTestBay1 : manageSTA_NoLoadTestBay1BypassStates2 : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_StaBay1().size());
			// setStopProcessCompletedHvBay(false);
			// setStopProcessRequestedHvBay(false);
			while (!StaNld_Bay1.isStopProcessCompletedStaNldBay1() &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty()) {
						// Set the next state based on the success column
						STA_NoLoadTestBay1State nextState = createStateInstance(nextStateName, errorCode);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration

					for (StateFlow row : getTableStatePlanner_StaBay1()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							// currentIndex = presentRow.;
							stateFound = true;
							break; // Exit the loop once the next state is found
						}
					}

				} else {
					// ======
					// update in the table.
					errorCode = bayStatus.getErrorCode();
					String nextStateName = getErrorStateInstance(errorCode);// createStateInstance("S22_error_Handling");

					presentRow.setIfFailed(nextStateName);

					// =====

					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();

					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S13_error_Handling")) {
							STA_NoLoadTestBay1State nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else if (nextStateName.equals("S13_error_Handling_Bay1")) {
							STA_NoLoadTestBay1State nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							STA_NoLoadTestBay1State nextState2 = createStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}

					}

					for (StateFlow row : getTableStatePlanner_StaBay1()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}
			}

		} else {
			StaNld_Bay1.logger.debug(
					"STA_NoLoadTestBay1 : manageSTA_NoLoadTestBay1BypassStates2  : No states found in the planner");
		}
		// =================================================================================================
		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1 : manageSTA_NoLoadTestBay1BypassStates2 : Exit");

	}

	// ===============================================================================================
	private STA_NoLoadTestBay1State createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
			case "S13_error_Handling":
				return new S13_error_Handling_Bay1(errorCode);
			case "S13_error_Handling_Bay1":
				return new S13_error_Handling_Bay1(errorCode);
			default:
				throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public STA_NoLoadTestBay1State createStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		StaNld_Bay1.logger.debug("createStateInstance : stateName: " + stateName);

		Class<?> c = null;
		try {
			c = Class.forName(StaNld_Bay1.class.getPackage().getName() + "." + stateName);
			// HvBayState HvBayStateObj=null;
			Object staBay1StateObj = null;
			try {
				// HvBayStateObj = (HvBayState)c.newInstance();
				staBay1StateObj = c.newInstance();
				return (STA_NoLoadTestBay1State) staBay1StateObj;
			} catch (InstantiationException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"STA_NoLoadTestBay1Bypass : Exception: Unknown state1: " + stateName);
			} catch (IllegalAccessException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"STA_NoLoadTestBay1Bypass : Exception: Unknown state2: " + stateName);
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("STA_NoLoadTestBay1Bypass : Exception: Unknown state3: " + stateName);
		}

	}
	// ==========================================================================================================================================

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020:
				return "S12_release_the_pallet_release_semaphore_Bay1";
			default:
				return "S13_error_Handling_Bay1";
		}
	}

	// ======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_StaBay1 = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_StaBay1() {
		return tableBypassStatePlanner_StaBay1;
	}

	public void setTableStatePlanner_StaBay1(ArrayList<StateFlow> tableStatePlanner_StaBay1) {
		this.tableBypassStatePlanner_StaBay1 = tableStatePlanner_StaBay1;
	}

	// ===========================================================
	// ==============================================================

}
