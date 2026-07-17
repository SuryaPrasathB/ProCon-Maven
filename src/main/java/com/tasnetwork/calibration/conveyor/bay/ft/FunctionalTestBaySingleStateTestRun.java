package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class FunctionalTestBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Ft.class.getPackage().getName());
	private FtBayContext ftBayStateManager = new FtBayContext(); 

	public void run() {
		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Ft.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : Entry");
		String errorCode = "";
		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		Ft functionalTestBay = new Ft();
		FtBayState currentState = functionalTestBay.createFtBayStateInstance(stateName,errorCode ); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		functionalTestBay.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		functionalTestBay.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		functionalTestBay.logger.debug("FunctionalTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(FtBayState newState) {
		//Set previous state here 

		// Set the new state
		ftBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = ftBayStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public FtBayState getPreviousState(){
		return ftBayStateManager.getLastProcessedBayState();
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

	private FtBayState createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S22_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S22_error_Handling";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
