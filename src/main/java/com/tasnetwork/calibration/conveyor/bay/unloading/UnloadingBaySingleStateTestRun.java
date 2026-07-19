package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class UnloadingBaySingleStateTestRun extends TimerTask {
	public static Logger logger = Logger.getLogger(Unloading.class.getPackage().getName());
	private UnloadingBayContext unloadingBayStateManager = new UnloadingBayContext();

	public void run() {
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : Entry");

		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		UnloadingBayState currentState = UnloadingBayState.createState(stateName);

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Unloading.logger.debug(
				"UnloadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : "
				+ bayStatus.getErrorCode());
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	// =====================================================================================================================

	public void setNextState(UnloadingBayState newState) {
		// Set previous state here

		// Set the new state
		unloadingBayStateManager.setState(newState);

	}

	public BayResponse processCurrentState() {
		// Process the current state
		BayResponse bayStatus = unloadingBayStateManager.processPresentState();

		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName()
				+ " : Status     : " + bayStatus.isStatus());
		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName()
				+ " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus;
	}

	public UnloadingBayState getPreviousState() {
		return unloadingBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

}
