package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class HighVoltageTestBayBypass extends TimerTask{

	//public static Logger logger = Logger.getLogger(HighVoltageTestBay.class.getPackage().getName());
	private HvtBayContext HvBayBypassStateManager = new HvtBayContext();

	public void run() {

		Hv.logger.debug("HighVoltageTestBayBypass : Entry");
		tableBypassStatePlanner_HvBay.clear();
		manageHighVoltageTestBayBypassStates2();
		
		String pathId = "ExR";
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.HV_BAY_KEY,
				"",//ConstantBayStateManage.Hv_BAY_HP_SEQ_01,
				"",//ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
				pathId,
				"-",
				"",//portInfo.getPortId(),
				"",//ConstantBayPortNameMapping.Hv_PORT_NAME_SNSR_PALLET,						
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"",//"Waiting",
				"Bypass Completed"//ConstantConveyor.COMM_EXECUTION_STATUS_INP
				);
		
		int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
	}

	//====================================================================================================================


	public void setNextState(HvtBayState newState) {
		//Set previous state here 

		// Set the new state
		HvBayBypassStateManager.setState(newState);


	}
	//====================================================================================================================

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = HvBayBypassStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + HvBayBypassStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}
	//====================================================================================================================

	public HvtBayState getPreviousState(){
		return HvBayBypassStateManager.getLastProcessedBayState();
	}

	//===========================================================
	//==============================================================

	//=================================================================================================================
	public void manageHighVoltageTestBayBypassStates2() {

		Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : Entry");

		//setTableStatePlanner_HvBay(StatePlannerController.getTableStatePlannerHvBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.HV_BAY_KEY, ConstantStateModes.BAY_BYPASS);

		setTableStatePlanner_HvBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		String errorCode = "";
		
		if(getTableStatePlanner_HvBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_HvBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			HvtBayState currentState = createHvBayStateInstance(currentStateName, errorCode); // Create the state instance
			setNextState(currentState); // Set the first state
	
			Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : getTableStatePlanner2 : Size : " + getTableStatePlanner_HvBay().size());
			//setStopProcessCompletedHvBay(false);
			//setStopProcessRequestedHvBay(false);
			while (!Hv.isStopProcessCompletedHvtBay() &&
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
						HvtBayState nextState = createHvBayStateInstance(nextStateName, errorCode);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_HvBay()) {
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
							HvtBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							HvtBayState nextState2 = createHvBayStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_HvBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
			}
			
		}else {
			Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : No states found in the planner");
		}

		//=================================================================================================	
		Hv.logger.debug("HighVoltageTestBay : manageHighVoltageTestBayBypassStates2 : Exit");

	}
	//===============================================================================================
	private HvtBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}

	// Helper method to create a state instance dynamically based on the state name
	public HvtBayState createHvBayStateInstance(String stateName, String errorCode) {
		// Create and return an instance of the state class based on the state name
		Hv.logger.debug("createHvBayStateInstance : stateName: " + stateName);

		Class<?> c=null;
		try {
			c = Class.forName(Hv.class.getPackage().getName() +"."+stateName);
			//HvBayState HvBayStateObj=null;
			Object HvBayStateObj = null;
			try {
				//HvBayStateObj = (HvBayState)c.newInstance();
				HvBayStateObj = c.newInstance();
				return (HvtBayState)HvBayStateObj;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new IllegalArgumentException("Hv: Exception: Unknown state1: " + stateName);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
				throw new IllegalArgumentException("Hv: Exception: Unknown state2: " + stateName);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalArgumentException("Hv: Exception: Unknown state3: " + stateName);
		}


	}
	//==========================================================================================================================================


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		case  ConvErrorCodeMapping.ERROR_CODE_HVT_010 :
			return "S051_stop_HV_source";

		default:
			return "S10_error_Handling";
		}
	}
	
	//======================================================================================================
	public ArrayList<StateFlow> tableBypassStatePlanner_HvBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_HvBay() {
		return tableBypassStatePlanner_HvBay;
	}

	public void setTableStatePlanner_HvBay(ArrayList<StateFlow> tableStatePlanner_HvBay) {
		this.tableBypassStatePlanner_HvBay = tableStatePlanner_HvBay;
	}

	//===========================================================
	//==============================================================

}
