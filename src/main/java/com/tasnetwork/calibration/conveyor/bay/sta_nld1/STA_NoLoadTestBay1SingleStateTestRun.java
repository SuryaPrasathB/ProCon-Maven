package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class STA_NoLoadTestBay1SingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(StaNld_Bay1.class.getPackage().getName());
	private STA_NoLoadTestBay1Context sctNltBay1StateManager = new STA_NoLoadTestBay1Context(); 

	public void run() {
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		STA_NoLoadTestBay1State currentState = StaNld_Bay1.createSctNltBay1StateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay1State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay1StateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay1StateManager.processPresentState();

		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay1State getPreviousState(){
		return sctNltBay1StateManager.getLastProcessedBayState();
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

	private STA_NoLoadTestBay1State createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S13_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S13_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S13_error_Handling_Bay1";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
