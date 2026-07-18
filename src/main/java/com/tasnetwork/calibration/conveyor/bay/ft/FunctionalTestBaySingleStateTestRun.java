package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

/**
 * TimerTask responsible for executing a single FT Bay state in isolation.
 * This is primarily used for debugging and manual testing via the UI, where a
 * user
 * can select a specific state from a dropdown and execute it once.
 */
public class FunctionalTestBaySingleStateTestRun extends TimerTask {
	public static Logger logger = Logger.getLogger(Ft.class.getPackage().getName());
	private FtBayContext ftBayStateManager = new FtBayContext();

	/**
	 * Entry point for the TimerTask. Triggers the execution of the selected state.
	 */
	@Override
	public void run() {
		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : Entry");

		singleStateTestRun();
	}

	/**
	 * Reads the selected state from the UI dropdown, instantiates it, and processes
	 * it once.
	 */
	private void singleStateTestRun() {

		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : Entry");
		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();

		FtBayState currentState = FtBayState.createState(stateName);

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : "
				+ bayStatus.getStatus());
		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : "
				+ bayStatus.getErrorCode());
		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	// =====================================================================================================================

	/**
	 * Sets the state to be executed in the test run.
	 *
	 * @param newState The new {@link FtBayState} instance.
	 */
	public void setNextState(FtBayState newState) {
		ftBayStateManager.setState(newState);
	}

	/**
	 * Executes the logic of the selected state.
	 *
	 * @return A {@link BayResponse} indicating the success/failure status.
	 */
	public BayResponse processCurrentState() {
		BayResponse bayStatus = ftBayStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	/**
	 * Retrieves the previously executed state.
	 *
	 * @return The last {@link FtBayState} processed.
	 */
	public FtBayState getPreviousState() {
		return ftBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

}
