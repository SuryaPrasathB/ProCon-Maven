
package com.tasnetwork.calibration.conveyor.bay.ir;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class InsulationResistanceTestBaySingleStateTestRun extends TimerTask{
	public static Logger logger = Logger.getLogger(Ir.class.getPackage().getName());
	private IrtBayContext irtBayStateManager = new IrtBayContext(); 

	public void run() {
		Ir.logger.debug("InsulationResistanceTestBaySingleStateTestRun : Entry");
		
		singleStateTestRun();
	}

	private void singleStateTestRun() {

		Ir.logger.debug("InsulationResistanceTestBaySingleStateTestRun : singleStateTestRun : Entry");

		String stateName = StatePlannerController.ref_cmbBxSelectSingleState.getSelectionModel().getSelectedItem();
		IrtBayState currentState = IrtBayState.createState(stateName); 
		
		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Ir.logger.debug("InsulationResistanceTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Ir.logger.debug("InsulationResistanceTestBaySingleStateTestRun : singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Ir.logger.debug("InsulationResistanceTestBaySingleStateTestRun : singleStateTestRun : Exit");

	}

	//=====================================================================================================================

	public void setNextState(IrtBayState newState) {
		//Set previous state here 

		// Set the new state
		irtBayStateManager.setState(newState);


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = irtBayStateManager.processPresentState();

		Ir.logger.debug("processCurrentState : " + irtBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ir.logger.debug("processCurrentState : " + irtBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public IrtBayState getPreviousState(){
		return irtBayStateManager.getLastProcessedBayState();
	}

	public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();

	public TableView<StateFlow> getTableStatePlanner_FtBay() {
		return tableStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(TableView<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_FtBay = tableStatePlanner_FtBay;
	}



}
