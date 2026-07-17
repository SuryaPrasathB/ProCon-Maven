package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class STA_NoLoadTestBay2SingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(StaNld_Bay2.class.getPackage().getName());
	private STA_NoLoadTestBay2Context sctNltBay2StateManager = new STA_NoLoadTestBay2Context(); 

	public void run() {
		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2SingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2SingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		STA_NoLoadTestBay2State currentState = StaNld_Bay2.createSctNltBay2StateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2SingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2SingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2SingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay2State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay2StateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay2StateManager.processPresentState();

		StaNld_Bay2.logger.debug("processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger.debug("processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay2State getPreviousState(){
		return sctNltBay2StateManager.getLastProcessedBayState();
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

	private STA_NoLoadTestBay2State createErrorStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S13_error_Handling":
			return new S13_error_Handling_Bay2(errorCode);
			
		case "S13_error_Handling_Bay2":
			return new S13_error_Handling_Bay2(errorCode);	
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String stateName) {


		return "S13_error_Handling_Bay2";
	}

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
