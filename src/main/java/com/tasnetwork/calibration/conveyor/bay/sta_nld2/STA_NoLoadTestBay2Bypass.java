package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class STA_NoLoadTestBay2Bypass extends TimerTask {

	// public static Logger logger =
	// Logger.getLogger(STA_NoLoadTestBay2.class.getPackage().getName());
	private STA_NoLoadTestBay2Context staBay2BypassStateManager = new STA_NoLoadTestBay2Context();

	public void run() {

		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2Bypass : Entry");
		tableBypassStatePlanner_StaBay2.clear();
		manageSTA_NoLoadTestBay2BypassStates2();

		String pathId = "ExR";
		new TestInterfaceStatus(
				ConstantConveyor.STA_NLD2_BAY_KEY,
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
	}

	// ====================================================================================================================

	public void setNextState(STA_NoLoadTestBay2State newState) {
		// Set previous state here

		// Set the new state
		staBay2BypassStateManager.setState(newState);

	}
	// ====================================================================================================================

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = staBay2BypassStateManager.processPresentState();

		StaNld_Bay2.logger
				.debug("processCurrentState : " + staBay2BypassStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger
				.debug("processCurrentState : " + staBay2BypassStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}
	// ====================================================================================================================

	public STA_NoLoadTestBay2State getPreviousState() {
		return staBay2BypassStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageSTA_NoLoadTestBay2BypassStates2() {

		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2 : manageSTA_NoLoadTestBay2BypassStates2 : Entry");

		// setTableStatePlanner_HvBay(StatePlannerController.getTableStatePlannerHvBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD2_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_StaBay2(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		String errorCode = "";

		if (getTableStatePlanner_StaBay2().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaBay2().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay2State currentState = createStateInstance(currentStateName, errorCode); // Create the state
																										// instance
			setNextState(currentState); // Set the first state

			StaNld_Bay2.logger.debug(
					"STA_NoLoadTestBay2 : manageSTA_NoLoadTestBay2BypassStates2 : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_StaBay2().size());
			// setStopProcessCompletedHvBay(false);
			// setStopProcessRequestedHvBay(false);
			while (!StaNld_Bay2.isStopProcessCompletedStaNldBay2() &&
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
						STA_NoLoadTestBay2State nextState = createStateInstance(nextStateName, errorCode);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_StaBay2()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
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
							STA_NoLoadTestBay2State nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							STA_NoLoadTestBay2State nextState2 = createStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}

					}

					for (StateFlow row : getTableStatePlanner_StaBay2()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}
			}
		} else {
			StaNld_Bay2.logger.debug(
					"STA_NoLoadTestBay2 : manageSTA_NoLoadTestBay2BypassStates2 :  : No states found in the planner");
		}
		// =================================================================================================
		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2 : manageSTA_NoLoadTestBay2BypassStates2 : Exit");

	}

	// ===============================================================================================
	private STA_NoLoadTestBay2State createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
			case "S13_error_Handling":
				return new S13_error_Handling_Bay2(errorCode);
			case "S13_error_Handling_Bay2":
				return new S13_error_Handling_Bay2(errorCode);
			default:
				throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public STA_NoLoadTestBay2State createStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		StaNld_Bay2.logger.debug("createStateInstance : stateName: " + stateName);

		Class<?> c = null;
		try {
			c = Class.forName(StaNld_Bay2.class.getPackage().getName() + "." + stateName);
			// HvBayState HvBayStateObj=null;
			Object StaBay2StateObj = null;
			try {
				// HvBayStateObj = (HvBayState)c.newInstance();
				StaBay2StateObj = c.newInstance();
				return (STA_NoLoadTestBay2State) StaBay2StateObj;
			} catch (InstantiationException e) {

				e.printStackTrace();
				throw new IllegalArgumentException("STA_NoLoadTestBay2Bypass: Exception: Unknown state1: " + stateName);
			} catch (IllegalAccessException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"STA_NoLoadTestBay2Bypass : Exception: Unknown state2: " + stateName);
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("STA_NoLoadTestBay2Bypass : Exception: Unknown state3: " + stateName);
		}

	}
	// ==========================================================================================================================================

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020:
				return "S12_release_the_pallet_release_semaphore_Bay2";
			default:
				return "S13_error_Handling_Bay2";
		}
	}

	// ======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_StaBay2 = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_StaBay2() {
		return tableBypassStatePlanner_StaBay2;
	}

	public void setTableStatePlanner_StaBay2(ArrayList<StateFlow> tableStatePlanner_StaBay2) {
		this.tableBypassStatePlanner_StaBay2 = tableStatePlanner_StaBay2;
	}

	// ===========================================================
	// ==============================================================

}
