package com.tasnetwork.calibration.conveyor.bay.ir;

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

public class InsulationResistanceTestBayBypass extends TimerTask {

	// public static Logger logger =
	// Logger.getLogger(InsulationResistanceTestBay.class.getPackage().getName());
	private IrtBayContext HvBayBypassStateManager = new IrtBayContext();

	public void run() {

		Ir.logger.debug("InsulationResistanceTestBayBypass : Entry");
		tableBypassStatePlanner_HvBay.clear();
		manageInsulationResistanceTestBayBypassStates2();

		String pathId = "ExR";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.HV_BAY_KEY,
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

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	// ====================================================================================================================

	public void setNextState(IrtBayState newState) {
		// Set previous state here

		// Set the new state
		HvBayBypassStateManager.setState(newState);

	}
	// ====================================================================================================================

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = HvBayBypassStateManager.processPresentState();

		Ir.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Ir.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}
	// ====================================================================================================================

	public IrtBayState getPreviousState() {
		return HvBayBypassStateManager.getLastProcessedBayState();
	}

	// ===========================================================
	// ==============================================================

	// =================================================================================================================
	public void manageInsulationResistanceTestBayBypassStates2() {

		Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayBypassStates2 : Entry");

		// setTableStatePlanner_HvBay(StatePlannerController.getTableStatePlannerHvBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService()
				.findByBayKeyAndExecutionMode(ConstantConveyor.IR_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_HvBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		String errorCode = "";

		if (getTableStatePlanner_HvBay().size() > 0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_HvBay().get(currentIndex);
			StateFlow nextRow = presentRow;
			String currentStateName = presentRow.getState(); // Get the current state from the row
			IrtBayState currentState = IrtBayState.createState(currentStateName); // Create the state
																								// instance
			setNextState(currentState); // Set the first state

			Ir.logger.debug(
					"InsulationResistanceTestBay : manageInsulationResistanceTestBayBypassStates2 : getTableStatePlanner2 : Size : "
							+ getTableStatePlanner_HvBay().size());
			// setStopProcessCompletedHvBay(false);
			// setStopProcessRequestedHvBay(false);
			while (!Ir.isStopProcessCompletedIrtBay() &&
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
						IrtBayState nextState = IrtBayState.createState(nextStateName);
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
						if (nextStateName.equals("S10_error_Handling") || nextStateName.equals("S22_error_Handling")) {
							IrtBayState nextState = IrtBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState); // Set the next state dynamically
						} else {
							IrtBayState nextState = IrtBayState.createState(nextStateName);
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
			Ir.logger.debug(
					"InsulationResistanceTestBay : manageInsulationResistanceTestBayBypassStates2 : getTableStatePlanner2 : No states found in the planner");
		}

		// =================================================================================================
		Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayBypassStates2 : Exit");

	}

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
			case ConvErrorCodeMapping.ERROR_CODE_IRT_010:
				return "S06_open_the_fingerTip_Latch";

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
