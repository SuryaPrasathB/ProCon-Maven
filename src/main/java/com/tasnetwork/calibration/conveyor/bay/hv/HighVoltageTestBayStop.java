package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class HighVoltageTestBayStop extends TimerTask {

	private HvtBayContext hvtBayStopStateManager = new HvtBayContext();

	public void run() {
		Hv.logger.debug("HighVoltageTestBayStop : Entry");

		manageHighVoltageTestBayStates();
	}

	private void manageHighVoltageTestBayStates() {

		Hv.logger.debug("HighVoltageTestBayStop : manageHighVoltageTestBayStates : Entry");

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.HV_BAY_KEY, "STOP");
		setTableStatePlanner_HvtBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if (getTableStatePlanner_HvtBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_HvtBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			HvtBayState currentState = HvtBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state

			Hv.logger.debug("HighVoltageTestBayStop : manageHighVoltageTestBayStates : getTableStatePlanner2 : Size : "	+ getTableStatePlanner_HvtBay().size());

			while (!Hv.isStopProcessCompletedHvtBay() &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						// Set the next state based on the success column
						HvtBayState nextState = HvtBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					} else {
						Hv.setStopProcessCompletedHvtBay(true);
						break;
					}
					for (StateFlow row : getTableStatePlanner_HvtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
					}

				} else {
					// ======
					// update in the table.
					String errorCode = bayStatus.getErrorCode();
					String nextStateName = getErrorStateInstance(errorCode);// createStateInstance("S22_error_Handling");

					presentRow.setIfFailed(nextStateName);

					// =====

					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();

					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						if (nextStateName.equals("S10_error_Handling")) {
							HvtBayState nextState = HvtBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState); // Set the next state dynamically
						} else {
							HvtBayState nextState = HvtBayState.createState(nextStateName);
							setNextState(nextState); // Set the next state dynamically
						}

					} else {
						Hv.setStopProcessCompletedHvtBay(true);
						break;
					}

					for (StateFlow row : getTableStatePlanner_HvtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}

			}

		} else {
			Hv.logger
					.debug("HighVoltageTestBayReset : manageHighVoltageTestBayStates : No states found in the planner");
		}

		Hv.logger.debug("HighVoltageTestBayStop : manageHighVoltageTestBayStates : Entry");

	}

	// =====================================================================================================================

	// =====================================================================================================================

	public void setNextState(HvtBayState newState) {
		// Set previous state here

		// Set the new state
		hvtBayStopStateManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = hvtBayStopStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + hvtBayStopStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + hvtBayStopStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public HvtBayState getPreviousState() {
		return hvtBayStopStateManager.getLastProcessedBayState();
	}

	// ================================================================
	public ArrayList<StateFlow> tableStatePlanner_HvtBay = new ArrayList<StateFlow>();

	// ==========================================================
	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
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

}
