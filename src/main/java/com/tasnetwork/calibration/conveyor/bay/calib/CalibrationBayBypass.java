package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class CalibrationBayBypass extends TimerTask{

	//public static Logger logger = Logger.getLogger(CalibrationBay.class.getPackage().getName());
	private CalibrationBayContext calibBayBypassStateManager = new CalibrationBayContext();

	public void run() {

		Calib.logger.debug("CalibrationBayBypass : Entry");
		tableBypassStatePlanner_FtBay.clear();
		manageCalibrationBayBypassStates2();

		String pathId = "ExR";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				"",//ConstantBayStateManage.FT_BAY_HP_SEQ_01,
				"",//ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
				pathId,
				"-",
				"",//portInfo.getPortId(),
				"",//ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_PALLET,						
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"",//"Waiting",
				"Bypass Completed"//ConstantConveyor.COMM_EXECUTION_STATUS_INP
				);

		int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	//====================================================================================================================


	public void setNextState(CalibrationBayState newState) {
		//Set previous state here 

		// Set the new state
		calibBayBypassStateManager.setState(newState);


	}
	//====================================================================================================================

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = calibBayBypassStateManager.processPresentState();

		Calib.logger.debug("processCurrentState : " + calibBayBypassStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Calib.logger.debug("processCurrentState : " + calibBayBypassStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}
	//====================================================================================================================

	public CalibrationBayState getPreviousState(){
		return calibBayBypassStateManager.getLastProcessedBayState();
	}

	//===========================================================
	//==============================================================

	//=================================================================================================================
	public void manageCalibrationBayBypassStates2() {

		Calib.logger.debug("CalibrationBay : manageCalibrationBayBypassStates2 : Entry");

		//setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerFtBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.CALIBRATION_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_FtBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		String errorCode = "";
		if(getTableStatePlanner_FtBay().size()>0) {
			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_FtBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			CalibrationBayState currentState = createCalibrationBayStateInstance(currentStateName, errorCode); // Create the state instance
			setNextState(currentState); // Set the first state

			Calib.logger.debug("CalibrationBay : manageCalibrationBayBypassStates2 : getTableStatePlanner2 : Size : " + getTableStatePlanner_FtBay().size());
			//setStopProcessCompletedFtBay(false);
			//setStopProcessRequestedFtBay(false);
			while (!Calib.isStopProcessCompletedCalibBay() &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();

				presentRow = nextRow ;

				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)

					String nextStateName = presentRow.getIfSuccess();

					if (nextStateName != null && !nextStateName.isEmpty()) {
						// Set the next state based on the success column
						CalibrationBayState nextState = createCalibrationBayStateInstance(nextStateName, errorCode);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration

					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							// currentIndex = presentRow.;
							stateFound = true;
							break; // Exit the loop once the next state is found
						}
					}

				}
				else{
					//======
					// update in the table.
					errorCode = bayStatus.getErrorCode() ;
					String nextStateName = getErrorStateInstance(errorCode);//createStateInstance("S22_error_Handling");

					presentRow.setIfFailed(nextStateName);

					//=====

					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();

					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S10_error_Handling")) {
							CalibrationBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CalibrationBayState nextState2 = createCalibrationBayStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}

					}


					for (StateFlow row : getTableStatePlanner_FtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}
			}
		}else {
			Calib.logger.debug("CalibrationBay : manageCalibrationBayBypassStates2  : No states found in the planner");
		}

		//=================================================================================================	
		Calib.logger.debug("CalibrationBay : manageCalibrationBayBypassStates2 : Exit");

	}
	//===============================================================================================
	private CalibrationBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public CalibrationBayState createCalibrationBayStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		Calib.logger.debug("createCalibrationBayStateInstance : stateName: " + stateName);

		Class<?> c=null;
		try {
			c = Class.forName(Calib.class.getPackage().getName() +"."+stateName);
			//CalibrationBayState CalibrationBayStateObj=null;
			Object CalibrationBayStateObj = null;
			try {
				//CalibrationBayStateObj = (CalibrationBayState)c.newInstance();
				CalibrationBayStateObj = c.newInstance();
				return (CalibrationBayState)CalibrationBayStateObj;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new IllegalArgumentException("FT: Exception: Unknown state1: " + stateName);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new IllegalArgumentException("FT: Exception: Unknown state2: " + stateName);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("FT: Exception: Unknown state3: " + stateName);
		}


	}
	//==========================================================================================================================================


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {

		default:
			return "S10_error_Handling";
		}
	}
	//======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_FtBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_FtBay() {
		return tableBypassStatePlanner_FtBay;
	}

	public void setTableStatePlanner_FtBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableBypassStatePlanner_FtBay = tableStatePlanner_FtBay;
	}

	//===========================================================
	//==============================================================

}
