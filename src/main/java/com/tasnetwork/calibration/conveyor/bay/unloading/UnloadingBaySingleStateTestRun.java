package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class UnloadingBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Unloading.class.getPackage().getName());
	private UnloadingBayContext unloadingBayStateManager = new UnloadingBayContext(); 

	public void run() {
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		UnloadingBayState currentState = Unloading.createUnloadingBayStateInstance(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Unloading.logger.debug("UnloadingBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(UnloadingBayState newState) {
		//Set previous state here 

		// Set the new state
		unloadingBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = unloadingBayStateManager.processPresentState();

		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public UnloadingBayState getPreviousState(){
		return unloadingBayStateManager.getLastProcessedBayState();
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

	private UnloadingBayState createErrorStateInstance(String stateName, String errorCode) {
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
