package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class CalibrationBayReset extends TimerTask {
	// public static Logger logger =
	// Logger.getLogger(CalibrationBay.class.getPackage().getName());
	private CalibrationBayContext calibBayResetStateManager = new CalibrationBayContext();

	public static boolean abort_Calib_Bay = false;

	public void run() {
		Calib.logger.debug("CalibrationBayReset : Entry");

		manageCalibrationBayResetStates();
		// DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageCalibrationBayResetStates() {

		Calib.logger.debug("CalibrationBayReset : manageCalibrationBayResetStates : Entry");

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.CALIBRATION_BAY_KEY, "RESET");// findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		setTableStatePlanner_CalibBay(statePlanner);// StatePlannerController.getTableStatePlannerCalibBay_UI());

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if (getTableStatePlanner_CalibBay().size() > 0) {
			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_CalibBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			CalibrationBayState currentState = CalibrationBayState.createState(currentStateName); // Create the state
																									// instance
			setNextState(currentState); // Set the first state

			Calib.logger.debug("CalibrationBayReset : manageCalibrationBayResetStates : getTableStatePlanner2 : Size : "
					+ getTableStatePlanner_CalibBay().size());

			while (!Calib.isResetProcessCompletedCalibBay() &&
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
						CalibrationBayState nextState = CalibrationBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_CalibBay()) {
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
						if (nextStateName.equals("S10_error_Handling")) {
							CalibrationBayState nextState2 = CalibrationBayState.createErrorState(nextStateName,
									errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CalibrationBayState nextState2 = CalibrationBayState.createState(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}

					}

					for (StateFlow row : getTableStatePlanner_CalibBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}

			}
		} else {
			Calib.logger
					.debug("CalibrationBayReset : manageCalibrationBayResetStates : No states found in the planner");
		}

		Calib.logger.debug("CalibrationBayReset : manageCalibrationBayResetStates : Exit");

	}

	public void setNextState(CalibrationBayState newState) {
		// Set previous state here

		// Set the new state
		calibBayResetStateManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = calibBayResetStateManager.processPresentState();

		Calib.logger.debug("processCurrentState : " + calibBayResetStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Calib.logger.debug("processCurrentState : " + calibBayResetStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public CalibrationBayState getPreviousState() {
		return calibBayResetStateManager.getLastProcessedBayState();
	}

	// public TableView<StateFlow> tableStatePlanner_CalibBay = new
	// TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CalibBay = new ArrayList<StateFlow>();

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			default:
				return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_CalibBay() {
		return tableStatePlanner_CalibBay;
	}

	public void setTableStatePlanner_CalibBay(ArrayList<StateFlow> tableStatePlanner_CalibBay) {
		this.tableStatePlanner_CalibBay = tableStatePlanner_CalibBay;
	}

}
