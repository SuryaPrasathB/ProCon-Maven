package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class CalibrationBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Calib.class.getPackage().getName());
	private CalibrationBayContext calibBayStateManager = new CalibrationBayContext(); 

	public void run() {
		Calib.logger.debug("CalibrationBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Calib.logger.debug("CalibrationBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		CalibrationBayState currentState = Calib.createCalibBayStateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Calib.logger.debug("CalibrationBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Calib.logger.debug("CalibrationBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Calib.logger.debug("CalibrationBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(CalibrationBayState newState) {
		//Set previous state here 

		// Set the new state
		calibBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = calibBayStateManager.processPresentState();

		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CalibrationBayState getPreviousState(){
		return calibBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_CalibBay = new TableView<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : gettableStatePlanner_CalibBay().getItems()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	private CalibrationBayState createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S22_error_Handling";
	}

	public TableView<StateFlow> gettableStatePlanner_CalibBay() {
		return tableStatePlanner_CalibBay;
	}

	public void settableStatePlanner_CalibBay(TableView<StateFlow> tableStatePlanner_CalibBay) {
		this.tableStatePlanner_CalibBay = tableStatePlanner_CalibBay;
	}



}
