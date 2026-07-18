package com.tasnetwork.calibration.conveyor.bay.rejection;


import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class RejectionBaySingleStateTestRun extends TimerTask{
	//public static Logger logger = Logger.getLogger(RejectionBay2.class.getPackage().getName());
	private RejectionBayContext rejectionBayStateManager = new RejectionBayContext(); 

	public void run() {
		Rejection.logger.debug("RejectionBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Rejection.logger.debug("RejectionBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		RejectionBayState currentState = RejectionBayState.createState(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Rejection.logger.debug("RejectionBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Rejection.logger.debug("RejectionBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Rejection.logger.debug("RejectionBaySingleStateTestRun : singleStateTestRun : Exit");

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
