package com.tasnetwork.calibration.conveyor.bay.comm;

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

public class CommBayBypass extends TimerTask{

	//public static Logger logger = Logger.getLogger(CommBay.class.getPackage().getName());
	private CommTestBayContext commBayBypassStateManager = new CommTestBayContext();

	public void run() {

		Comm.logger.debug("CommBayBypass : Entry");
		tableBypassStatePlanner_FtBay.clear();
		manageCommBayBypassStates2();
		
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


	public void setNextState(CommTestBayState newState) {
		//Set previous state here 

		// Set the new state
		commBayBypassStateManager.setState(newState);


	}
	//====================================================================================================================

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = commBayBypassStateManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + commBayBypassStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + commBayBypassStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}
	//====================================================================================================================

	public CommTestBayState getPreviousState(){
		return commBayBypassStateManager.getLastProcessedBayState();
	}

	//===========================================================
	//==============================================================

	//=================================================================================================================
	public void manageCommBayBypassStates2() {

		Comm.logger.debug("CommBay : manageCommBayBypassStates2 : Entry");

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
			CommTestBayState currentState = createCommTestBayStateInstance(currentStateName, errorCode); // Create the state instance
			setNextState(currentState); // Set the first state
	
			Comm.logger.debug("CommBay : manageCommBayBypassStates2 : getTableStatePlanner2 : Size : " + getTableStatePlanner_FtBay().size());
			//setStopProcessCompletedFtBay(false);
			//setStopProcessRequestedFtBay(false);
			while (!Comm.isStopProcessCompletedCommBay() &&
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
						CommTestBayState nextState = createCommTestBayStateInstance(nextStateName, errorCode);
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
							CommTestBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CommTestBayState nextState2 = createCommTestBayStateInstance(nextStateName, errorCode);
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
			Comm.logger.debug("CommBay : manageCommBayBypassStates2 : No states found in the planner");
		}
		//=================================================================================================	
		Comm.logger.debug("CommBay : manageCommBayBypassStates2 : Exit");

	}
	//===============================================================================================
	private CommTestBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public CommTestBayState createCommTestBayStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		Comm.logger.debug("createCommTestBayStateInstance : stateName: " + stateName);

		Class<?> c=null;
		try {
			c = Class.forName(Comm.class.getPackage().getName() +"."+stateName);
			//CommTestBayState CommTestBayStateObj=null;
			Object CommTestBayStateObj = null;
			try {
				//CommTestBayStateObj = (CommTestBayState)c.newInstance();
				CommTestBayStateObj = c.newInstance();
				return (CommTestBayState)CommTestBayStateObj;
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
