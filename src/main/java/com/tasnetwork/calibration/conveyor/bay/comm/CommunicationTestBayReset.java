package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class CommunicationTestBayReset extends TimerTask {
	// public static Logger logger =
	// Logger.getLogger(CommunicationTestBay.class.getPackage().getName());
	private CommTestBayContext commTestBayResetManager = new CommTestBayContext();

	public static boolean abort_CommTest_Bay = false;

	public void run() {
		Comm.logger.debug("CommunicationTestBayReset : Entry");

		manageCommunicationTestBayResetStates();
		// DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageCommunicationTestBayResetStates() {

		Comm.logger.debug("CommunicationTestBayReset : manageCommunicationTestBayResetStates : Entry");

		// setTableStatePlanner_CommBay(StatePlannerController.getTableStatePlannerCommBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.COMMUNICATION_BAY_KEY, "RESET");// findByBayKey(ConstantConveyor.COMMUNICATION_BAY_KEY);
		setTableStatePlanner_CommBay(statePlanner);
		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if (getTableStatePlanner_CommBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_CommBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			CommTestBayState currentState = CommTestBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state

			Comm.logger.debug(
					"CommunicationTestBay : manageCommunicationTestBayResetStates : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_CommBay().size());

			while (!Comm.isResetProcessCompletedCommBay() &&
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
						CommTestBayState nextState = CommTestBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_CommBay()) {
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
							CommTestBayState nextState2 = CommTestBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CommTestBayState nextState2 = CommTestBayState.createState(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}

					}

					for (StateFlow row : getTableStatePlanner_CommBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}

			}

		} else {
			Comm.logger.debug(
					"CommunicationTestBayReset : manageCommunicationTestBayResetStates : No states found in the planner");
		}

		Comm.logger.debug("CommunicationTestBayReset : manageCommunicationTestBayResetStates : Exit");

	}

	// =====================================================================================================================

	/*
	 * public static void singleStateTestRun(CommTestBayState currentState){
	 * CommunicationTestBay.logger.debug("singleStateTestRun : Entry");
	 * 
	 * setNextState(currentState);
	 * 
	 * BayResponse bayStatus = processCurrentState();
	 * 
	 * CommunicationTestBay.logger.
	 * debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
	 * CommunicationTestBay.logger.
	 * debug("singleStateTestRun : bayStatus : Error Code : " +
	 * bayStatus.getErrorCode());
	 * CommunicationTestBay.logger.debug("singleStateTestRun : Exit");
	 * }
	 */

	// =====================================================================================================================

	public void setNextState(CommTestBayState newState) {
		// Set previous state here

		// Set the new state
		commTestBayResetManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = commTestBayResetManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + commTestBayResetManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + commTestBayResetManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public CommTestBayState getPreviousState() {
		return commTestBayResetManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_CommBay = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CommBay = new ArrayList<StateFlow>();

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			default:
				return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_CommBay() {
		return tableStatePlanner_CommBay;
	}

	public void setTableStatePlanner_CommBay(ArrayList<StateFlow> tableStatePlanner_CommBay) {
		this.tableStatePlanner_CommBay = tableStatePlanner_CommBay;
	}

}
