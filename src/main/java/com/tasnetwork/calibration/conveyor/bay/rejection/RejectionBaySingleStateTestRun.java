package com.tasnetwork.calibration.conveyor.bay.rejection;


import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

import org.apache.log4j.Logger;

public class RejectionBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger("com.tasnetwork.calibration.conveyor.bay.rejection");
	private RejectionBayContext rejectionBayStateManager = new RejectionBayContext(); 

	public void run() {
		Logger log = (Rejection.logger != null) ? Rejection.logger : logger;
		log.debug("RejectionBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {
		Logger log = (Rejection.logger != null) ? Rejection.logger : logger;
		log.debug("RejectionBaySingleStateTestRun : singleStateTestRun : Entry");

		if (StatePlannerController.ref_cmbBxSelectSingleState == null ||
				StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel() == null) {
			log.error("RejectionBaySingleStateTestRun : ref_cmbBxSelectSingleState is null");
			return;
		}

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		if (stateName == null || stateName.trim().isEmpty() || "Select State".equals(stateName)) {
			log.error("RejectionBaySingleStateTestRun : No state selected to run");
			return;
		}
		
		RejectionBayState currentState = RejectionBayState.createState(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		if (bayStatus != null) {
			log.debug("RejectionBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
			log.debug("RejectionBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		}
		log.debug("RejectionBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(RejectionBayState newState) {
		//Set previous state here 

		// Set the new state
		rejectionBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = rejectionBayStateManager.processPresentState();

		Rejection.logger.debug("processCurrentState : " + rejectionBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Rejection.logger.debug("processCurrentState : " + rejectionBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public RejectionBayState getPreviousState(){
		return rejectionBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
