package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class WaitingBayStop extends TimerTask {
	// public static Logger logger =
	// Logger.getLogger(WaitingBay.class.getPackage().getName());
	private WaitingBayContext WaitingBayStopStateManager = new WaitingBayContext();
	public static boolean abort_Waiting_Bay = false;

	public void run() {
		VerificWaiting.logger.debug("WaitingBayStop : Entry");

		manageWaitingBayStopStates();
		// DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageWaitingBayStopStates() {

		VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : Entry");

		// setTableStatePlanner_WaitingBay(StatePlannerController.getTableStatePlannerWaitingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.WAITING_BAY_KEY, "STOP");// findByBayKey(ConstantConveyor.WAITING_BAY_KEY);
		setTableStatePlanner_WaitingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if (getTableStatePlanner_WaitingBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_WaitingBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			WaitingBayState currentState = WaitingBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state

			VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : getTableStatePlanner2 : Size : "
					+ getTableStatePlanner_WaitingBay().size());

			while (!VerificWaiting.isStopProcessCompletedWaitingBay() &&
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
						WaitingBayState nextState = WaitingBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					} else {
						VerificWaiting.setStopProcessCompletedWaitingBay(true);
						break;
					}
					for (StateFlow row : getTableStatePlanner_WaitingBay()) {
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

					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S05_error_Handling")) {
							WaitingBayState nextState2 = WaitingBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							WaitingBayState nextState2 = WaitingBayState.createState(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}

					} else {
						VerificWaiting.setStopProcessCompletedWaitingBay(true);
						break;
					}

					for (StateFlow row : getTableStatePlanner_WaitingBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}

			}

		} else {
			VerificWaiting.logger.debug(
					"WaitingBayStop : manageWaitingBayStopStates : getTableStatePlanner2  : No states found in the planner");
		}

		VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : Exit");

	}

	// =====================================================================================================================

	public void setNextState(WaitingBayState newState) {
		// Set previous state here

		// Set the new state
		WaitingBayStopStateManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = WaitingBayStopStateManager.processPresentState();

		VerificWaiting.logger
				.debug("processCurrentState : " + WaitingBayStopStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		VerificWaiting.logger
				.debug("processCurrentState : " + WaitingBayStopStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public WaitingBayState getPreviousState() {
		return WaitingBayStopStateManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_WaitingBay = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_WaitingBay = new ArrayList<StateFlow>();

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			default:
				return "S05_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_WaitingBay() {
		return tableStatePlanner_WaitingBay;
	}

	public void setTableStatePlanner_WaitingBay(ArrayList<StateFlow> tableStatePlanner_WaitingBay) {
		this.tableStatePlanner_WaitingBay = tableStatePlanner_WaitingBay;
	}

}
