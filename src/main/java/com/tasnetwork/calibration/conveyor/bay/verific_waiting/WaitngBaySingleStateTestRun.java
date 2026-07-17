package com.tasnetwork.calibration.conveyor.bay.verific_waiting;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class WaitngBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(VerificWaiting.class.getPackage().getName());
	private WaitingBayContext WaitingBayStateManager = new WaitingBayContext(); 

	public void run() {
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		WaitingBayState currentState = VerificWaiting.createWaitingBayStateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		VerificWaiting.logger.debug("WaitingBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(WaitingBayState newState) {
		//Set previous state here 

		// Set the new state
		WaitingBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = WaitingBayStateManager.processPresentState();

		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public WaitingBayState getPreviousState(){
		return WaitingBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_FtBay().getItems()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	private WaitingBayState createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S05_error_Handling":
			return new S05_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S05_error_Handling";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
