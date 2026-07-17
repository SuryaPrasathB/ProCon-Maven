package com.tasnetwork.calibration.conveyor.bay.ft;

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

public class FunctionalTestBayBypass extends TimerTask{

	//public static Logger logger = Logger.getLogger(FunctionalTestBay.class.getPackage().getName());
	private FtBayContext ftBayBypassStateManager = new FtBayContext();

	public void run() {

		Ft.logger.debug("FunctionalTestBayBypass : Entry");
		tableBypassStatePlanner_FtBay.clear();
		manageFunctionalTestBayBypassStates2();
		
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


	public void setNextState(FtBayState newState) {
		//Set previous state here 

		// Set the new state
		ftBayBypassStateManager.setState(newState);


	}
	//====================================================================================================================

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = ftBayBypassStateManager.processPresentState();

		Ft.logger.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ft.logger.debug("processCurrentState : " + ftBayBypassStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}
	//====================================================================================================================

	public FtBayState getPreviousState(){
		return ftBayBypassStateManager.getLastProcessedBayState();
	}

	//===========================================================
	//==============================================================

	//=================================================================================================================
	public void manageFunctionalTestBayBypassStates2() {

		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : Entry");

		//setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerFtBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.FT_BAY_KEY, ConstantStateModes.BAY_BYPASS);

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
			FtBayState currentState = createFtBayStateInstance(currentStateName, errorCode); // Create the state instance
			setNextState(currentState); // Set the first state
	
			Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : getTableStatePlanner2 : Size : " + getTableStatePlanner_FtBay().size());
			//setStopProcessCompletedFtBay(false);
			//setStopProcessRequestedFtBay(false);
			while (!Ft.isStopProcessCompletedFtBay() &&
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
						FtBayState nextState = createFtBayStateInstance(nextStateName, errorCode);
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
						if (nextStateName.equals("S22_error_Handling")) {
							FtBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							FtBayState nextState2 = createFtBayStateInstance(nextStateName, errorCode);
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
			Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : No states found in the planner");
		}

		//=================================================================================================	
		Ft.logger.debug("FunctionalTestBay : manageFunctionalTestBayBypassStates2 : Exit");

	}
	//===============================================================================================
	private FtBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S22_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public FtBayState createFtBayStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		Ft.logger.debug("createFtBayStateInstance : stateName: " + stateName);

		Class<?> c=null;
		try {
			c = Class.forName(Ft.class.getPackage().getName() +"."+stateName);
			//FtBayState ftBayStateObj=null;
			Object ftBayStateObj = null;
			try {
				//ftBayStateObj = (FtBayState)c.newInstance();
				ftBayStateObj = c.newInstance();
				return (FtBayState)ftBayStateObj;
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
		case  ConvErrorCodeMapping.ERROR_CODE_FT_002 :
			return "S28_check_for_stop_latch_B4_status_FT_Bay";
		case  ConvErrorCodeMapping.ERROR_CODE_FT_034 :
			return "S27_open_stop_latch_b4_FT_Bay";
		case  ConvErrorCodeMapping.ERROR_CODE_FT_010  :
			return "S17_turn_off_divertor_relay_FT_Bay"; // REJECTION BAY PATH
		case  ConvErrorCodeMapping.ERROR_CODE_FT_006  :
			return "S17_turn_off_divertor_relay_FT_Bay";
		case  ConvErrorCodeMapping.ERROR_CODE_FT_001  :
			return "S17_turn_off_divertor_relay_FT_Bay";
		default: 
			return "S23_idle_condition" ; //S22_error_Handling";
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
