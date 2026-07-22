package com.tasnetwork.calibration.conveyor.bay.verific;

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

public class VerificationTestBayBypass extends TimerTask {

	// public static Logger logger =
	// Logger.getLogger(VerificationTestBay.class.getPackage().getName());
	private VerificationBayContext ftBayBypassStateManager = new VerificationBayContext();

	public void run() {

		Verification.logger.debug("VerificationTestBayBypass : Entry");
		tableBypassStatePlanner_FtBay.clear();
		manageVerificationTestBayBypassStates2();

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

	public void setNextState(VerificTestBayState newState) {
		// Set previous state here

		// Set the new state
		ftBayBypassStateManager.setState(newState);

	}
	// ====================================================================================================================

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = ftBayBypassStateManager.processPresentState();

		Verification.logger
				.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		Verification.logger
				.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}
	// ====================================================================================================================

	public VerificTestBayState getPreviousState() {
		return ftBayBypassStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageVerificationTestBayBypassStates2() {

		Verification.logger.debug("VerificationTestBay : manageVerificationTestBayBypassStates2 : Entry");

		// setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerFtBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.VERIFICATION_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_FtBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		String errorCode = "";

		if (getTableStatePlanner_FtBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_FtBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			VerificTestBayState currentState = VerificTestBayState.createState(currentStateName); // Create the state
																									// instance
			setNextState(currentState); // Set the first state

			Verification.logger.debug(
					"VerificationTestBay : manageVerificationTestBayBypassStates2 : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_FtBay().size());
			// setStopProcessCompletedFtBay(false);
			// setStopProcessRequestedFtBay(false);
			while (!Verification.isStopProcessCompletedVerificBay() && !Verification.isStopProcessRequestedVerificBay() && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty()) {
						// Set the next state based on the success column
						VerificTestBayState nextState = VerificTestBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					// Re-fetch the current row for the next iteration

					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							// currentIndex = presentRow.;
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
						if (nextStateName.equals("S17_error_Handling")) {
							VerificTestBayState nextState2 = VerificTestBayState.createErrorState(nextStateName,
									errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							VerificTestBayState nextState2 = VerificTestBayState.createState(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
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
			Verification.logger.debug(
					"VerificationTestBay : manageVerificationTestBayBypassStates2 : No states found in the planner");
		}

		if (Verification.isStopProcessRequestedVerificBay()) {
			Verification.setStopProcessCompletedVerificBay(true);
		}

		// =================================================================================================
		Verification.logger.debug("VerificationTestBay : manageVerificationTestBayBypassStates2 : Exit");

	}

	// ===============================================================================================
	// ==========================================================================================================================================

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022:
				return "S01_check_for_pallets_at_Verific_Bay";
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013:
				return "S12_check_for_pallets_at_SCT_NLT_Bay2";
			case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014:
				return "S08_check_for_pallets_at_SCT_NLT_Bay1";

			default:
				return "S17_error_Handling";
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
