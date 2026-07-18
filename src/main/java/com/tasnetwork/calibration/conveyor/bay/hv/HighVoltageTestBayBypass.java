package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class HighVoltageTestBayBypass extends TimerTask {

	private HvtBayContext HvBayBypassStateManager = new HvtBayContext();

	public void run() {

		Hv.logger.debug("HighVoltageTestBayBypass : Entry");
		tableBypassStatePlanner_HvBay.clear();
		manageHighVoltageTestBayBypassStates2();

		String pathId = "ExR";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.HV_BAY_KEY,
				"",
				"",
				pathId,
				"-",
				"",
				"",
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"",
				"Bypass Completed");

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	// ====================================================================================================================

	public void setNextState(HvtBayState newState) {
		// Set previous state here

		// Set the new state
		HvBayBypassStateManager.setState(newState);

	}
	// ====================================================================================================================

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = HvBayBypassStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}
	// ====================================================================================================================

	public HvtBayState getPreviousState() {
		return HvBayBypassStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageHighVoltageTestBayBypassStates2() {

		Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : Entry");

		// setTableStatePlanner_HvBay(StatePlannerController.getTableStatePlannerHvBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.HV_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_HvBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		String errorCode = "";

		if (getTableStatePlanner_HvBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_HvBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			HvtBayState currentState = HvtBayState.createState(currentStateName);
			setNextState(currentState); // Set the first state

			Hv.logger.debug(
					"HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_HvBay().size());

			while (!Hv.isStopProcessCompletedHvtBay() &&
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
						HvtBayState nextState = HvtBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_HvBay()) {
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
						if (nextStateName.equals("S10_error_Handling")) {
							HvtBayState nextState = HvtBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState); // Set the next state dynamically
						} else {
							HvtBayState nextState = HvtBayState.createState(nextStateName);
							setNextState(nextState); // Set the next state dynamically
						}

					}

					for (StateFlow row : getTableStatePlanner_HvBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}
			}

		} else {
			Hv.logger.debug(
					"HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : No states found in the planner");
		}

		// =================================================================================================
		Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : Exit");

	}

	// ===============================================================================================

	// ==========================================================================================================================================

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_HVT_010:
				return "S051_stop_HV_source";

			default:
				return "S10_error_Handling";
		}
	}

	// ======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_HvBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_HvBay() {
		return tableBypassStatePlanner_HvBay;
	}

	public void setTableStatePlanner_HvBay(ArrayList<StateFlow> tableStatePlanner_HvBay) {
		this.tableBypassStatePlanner_HvBay = tableStatePlanner_HvBay;
	}

	// ===========================================================
	// ==============================================================

}
