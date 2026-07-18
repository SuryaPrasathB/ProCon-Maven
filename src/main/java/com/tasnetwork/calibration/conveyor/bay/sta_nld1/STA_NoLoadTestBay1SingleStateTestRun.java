package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.TimerTask;


import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class STA_NoLoadTestBay1SingleStateTestRun extends TimerTask{

	private STA_NoLoadTestBay1Context sctNltBay1StateManager = new STA_NoLoadTestBay1Context(); 

	public void run() {
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay1SingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		STA_NoLoadTestBay1State currentState = STA_NoLoadTestBay1State.createSctNltBay1StateInstance(stateName); 
		
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

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
