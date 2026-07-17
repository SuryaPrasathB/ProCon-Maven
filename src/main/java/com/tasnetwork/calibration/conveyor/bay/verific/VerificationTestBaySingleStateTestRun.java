package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class VerificationTestBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Verification.class.getPackage().getName());
	private VerificationBayContext verificBayStateManager = new VerificationBayContext(); 

	public void run() {
		Verification.logger.debug("VerificationTestBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Verification.logger.debug("VerificationTestBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		VerificTestBayState currentState = Verification.createVerificTestBayStateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Verification.logger.debug("VerificationTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Verification.logger.debug("VerificationTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Verification.logger.debug("VerificationTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(VerificTestBayState newState) {
		//Set previous state here 

		// Set the new state
		verificBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = verificBayStateManager.processPresentState();

		Verification.logger.debug("processCurrentState : " + verificBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Verification.logger.debug("processCurrentState : " + verificBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public VerificTestBayState getPreviousState(){
		return verificBayStateManager.getLastProcessedBayState();
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

	private VerificTestBayState createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S17_error_Handling":
			return new S17_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S17_error_Handling";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
