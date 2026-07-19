package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class WaitngBaySingleStateTestRun extends TimerTask {
	public static Logger logger = Logger.getLogger(VerificWaiting.class.getPackage().getName());
	private WaitingBayContext WaitingBayStateManager = new WaitingBayContext();

	public void run() {
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : Entry");

		singleStateTestRun();
	}

	private void singleStateTestRun() {

		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		WaitingBayState currentState = WaitingBayState.createState(stateName);

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		VerificWaiting.logger.debug(
				"WaitingBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : "
				+ bayStatus.getErrorCode());
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	// =====================================================================================================================

	public void setNextState(WaitingBayState newState) {
		// Set previous state here

		// Set the new state
		WaitingBayStateManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = WaitingBayStateManager.processPresentState();

		VerificWaiting.logger
				.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName()
						+ " : Status     : " + bayStatus.isStatus());
		VerificWaiting.logger
				.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName()
						+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public WaitingBayState getPreviousState() {
		return WaitingBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

}
