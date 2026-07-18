package com.tasnetwork.calibration.conveyor.bay.ft;

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

/**
 * TimerTask responsible for executing the Functional Test (FT) Bay in Bypass mode.
 * In Bypass mode, the bay executes a distinct set of states from the database planner
 * (execution mode: 'BAY_BYPASS') to safely pass a pallet through without processing.
 */
public class FunctionalTestBayBypass extends TimerTask {

	private FtBayContext ftBayBypassStateManager = new FtBayContext();

	/**
	 * Entry point for the TimerTask. Fetches the bypass state planner and begins execution.
	 */
	@Override
	public void run() {

		Ft.logger.debug("FunctionalTestBayBypass : Entry");
		tableBypassStatePlanner_FtBay.clear();
		manageFunctionalTestBayBypassStates2();

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
				"Bypass Completed"// ConstantConveyor.COMM_EXECUTION_STATUS_INP
		);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	// ====================================================================================================================

	/**
	 * Sets the next state to be executed in the bypass sequence.
	 *
	 * @param newState The new {@link FtBayState} instance to transition to.
	 */
	public void setNextState(FtBayState newState) {
		ftBayBypassStateManager.setState(newState);
	}

	/**
	 * Executes the logic of the currently active bypass state.
	 *
	 * @return A {@link BayResponse} indicating the success/failure status.
	 */
	public BayResponse processCurrentState() {
		BayResponse bayStatus = ftBayBypassStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	/**
	 * Retrieves the previously executed state.
	 *
	 * @return The last {@link FtBayState} processed.
	 */
	public FtBayState getPreviousState() {
		return ftBayBypassStateManager.getLastProcessedBayState();
	}

	// =================================================================================================================
	/**
	 * The core state engine loop for Bypass mode.
	 * Fetches the state flow from the database and continuously executes states
	 * until the process completes, a stop is requested, or a fatal error occurs.
	 */
	public void manageFunctionalTestBayBypassStates2() {

		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : Entry");

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.FT_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_FtBay(statePlanner);

		int currentIndex = 0; // Start from the first row
		String errorCode = "";
		if (getTableStatePlanner_FtBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_FtBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); 
			FtBayState currentState = FtBayState.createState(currentStateName); 
			setNextState(currentState); 

			Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : getTableStatePlanner2 : Size : "
					+ getTableStatePlanner_FtBay().size());
			
			// BUG FIX: Ensure loop exits naturally on completion OR aborts on stop request
			while (!Ft.isStopProcessCompletedFtBay() && !Ft.isStopProcessRequestedFtBay() && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty()) {
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

					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S22_error_Handling")) {
							FtBayState nextState = FtBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState); // Set the next state dynamically
						} else {
							FtBayState nextState = FtBayState.createState(nextStateName);
							setNextState(nextState); // Set the next state dynamically
						}
					}

					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}
			}

		} else {
			Ft.logger
					.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : No states found in the planner");
		}

		// =================================================================================================
		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : Exit");

	}


	// ==========================================================================================================================================

	private String getErrorStateInstance(String errorCode) {

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

	// ======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_FtBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_FtBay() {
		return tableBypassStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableBypassStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

	// ===========================================================
	// ==============================================================

}
