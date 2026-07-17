package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class CommunicationTestBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Comm.class.getPackage().getName());
	private CommTestBayContext CommTestBayStateManager = new CommTestBayContext(); 

	public void run() {
		Comm.logger.debug("CommunicationTestBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Comm.logger.debug("CommunicationTestBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		CommTestBayState currentState = Comm.createCommBayStateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Comm.logger.debug("CommunicationTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Comm.logger.debug("CommunicationTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Comm.logger.debug("CommunicationTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(CommTestBayState newState) {
		//Set previous state here 

		// Set the new state
		CommTestBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = CommTestBayStateManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + CommTestBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + CommTestBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CommTestBayState getPreviousState(){
		return CommTestBayStateManager.getLastProcessedBayState();
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

	private CommTestBayState createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S10_error_Handling";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
