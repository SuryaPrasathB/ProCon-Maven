package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class LoadingBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Loading.class.getPackage().getName());
	private LoadingBayContext loadingBayStateManager = new LoadingBayContext(); 

	public void run() {
		Loading.logger.debug("LoadingBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Loading.logger.debug("LoadingBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		LoadingBayState currentState = LoadingBayState.createState(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Loading.logger.debug("LoadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Loading.logger.debug("LoadingBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Loading.logger.debug("LoadingBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(LoadingBayState newState) {
		//Set previous state here 

		// Set the new state
		loadingBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = loadingBayStateManager.processPresentState();

		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public LoadingBayState getPreviousState(){
		return loadingBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
