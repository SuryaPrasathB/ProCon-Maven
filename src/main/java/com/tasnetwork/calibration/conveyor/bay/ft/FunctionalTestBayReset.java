package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * TimerTask responsible for executing the Functional Test (FT) Bay in Reset mode.
 * In Reset mode, the bay executes a distinct set of states from the database planner
 * (execution mode: 'RESET') to return hardware mechanisms to their default/home positions.
 */
public class FunctionalTestBayReset extends TimerTask {

	private FtBayContext ftBayResetStateManager = new FtBayContext();

	/**
	 * Entry point for the TimerTask. Fetches the reset state planner and begins execution.
	 */
	@Override
	public void run() {

		Ft.logger.debug("FunctionalTestBayReset : Entry");
		tableResetStatePlanner_FtBay.clear();
		manageFunctionalTestBayResetStates2();

		String pathId = "ExR";
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
				"Reset Completed"// ConstantConveyor.COMM_EXECUTION_STATUS_INP
		);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	// ====================================================================================================================

	/**
	 * Sets the next state to be executed in the reset sequence.
	 *
	 * @param newState The new {@link FtBayState} instance to transition to.
	 */
	public void setNextState(FtBayState newState) {
		ftBayResetStateManager.setState(newState);
	}

	/**
	 * Executes the logic of the currently active reset state.
	 *
	 * @return A {@link BayResponse} indicating the success/failure status.
	 */
	public BayResponse processCurrentState() {
		BayResponse bayStatus = ftBayResetStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayResetStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayResetStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	/**
	 * Retrieves the previously executed state.
	 *
	 * @return The last {@link FtBayState} processed.
	 */
	public FtBayState getPreviousState() {
		return ftBayResetStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageFunctionalTestBayResetStates2() {

		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayResetStates2 : Entry");

		// setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerFtBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.FT_BAY_KEY, "RESET");

		setTableStatePlanner_FtBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		String errorCode = "";

		if (getTableStatePlanner_FtBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_FtBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); 
			FtBayState currentState = FtBayState.createState(currentStateName); 
			setNextState(currentState); 

			Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayResetStates2 : getTableStatePlanner2 : Size : "
					+ getTableStatePlanner_FtBay().size());
			
			// BUG FIX: Ensure loop exits naturally on completion OR aborts on stop request
			while (!Ft.isResetProcessCompletedFtBay() && !Ft.isStopProcessRequestedFtBay() && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						// Set the next state based on the success column
						FtBayState nextState = FtBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					
					// Re-fetch the current row for the next iteration
					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { 
							nextRow = row; 
							break; 
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

					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						if (nextStateName.equals("S22_error_Handling")) {
							FtBayState nextState = FtBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState); // Set the next state dynamically
						} else {
							FtBayState nextState = FtBayState.createState(nextStateName);
							setNextState(nextState); // Set the next state dynamically
						}
					}

					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { 
							nextRow = row; 
							break; 
						}
					}
				}
			}

		} else {
			Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayResetStates2 : No states found in the planner");
		}

		// =================================================================================================
		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayResetStates2 : Exit");

	}



	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_FT_034:
				return "S28_check_for_stop_latch_B4_status_FT_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_FT_010:
				return "S17_turn_off_divertor_relay_FT_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_FT_006:
				return "S17_turn_off_divertor_relay_FT_Bay";
			default:
				return "S22_error_Handling";
		}
	}

	// ======================================================================================================
	public ArrayList<StateFlow> tableResetStatePlanner_FtBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_FtBay() {
		return tableResetStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableResetStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

	// ===========================================================
	// ==============================================================

}
