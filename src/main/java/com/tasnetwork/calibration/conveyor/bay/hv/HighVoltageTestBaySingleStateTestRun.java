
package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class HighVoltageTestBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Hv.class.getPackage().getName());
	private HvtBayContext HvtBayStateManager = new HvtBayContext(); 

	public void run() {
		Hv.logger.debug("HighVoltageTestBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Hv.logger.debug("HighVoltageTestBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		HvtBayState currentState = HvtBayState.createState(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Hv.logger.debug("HighVoltageTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Hv.logger.debug("HighVoltageTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Hv.logger.debug("HighVoltageTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(HvtBayState newState) {
		//Set previous state here 

		// Set the new state
		HvtBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = HvtBayStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + HvtBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + HvtBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public HvtBayState getPreviousState(){
		return HvtBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_HvtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_HvtBay() {
		return tableStatePlanner_HvtBay;
	}

	public void setTableStatePlanner_HvtBay(TableView<StateFlow> tableStatePlanner_HvtBay) {
		this.tableStatePlanner_HvtBay = tableStatePlanner_HvtBay;
	}



}
