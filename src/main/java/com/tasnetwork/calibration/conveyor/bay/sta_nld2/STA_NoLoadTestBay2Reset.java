package com.tasnetwork.calibration.conveyor.bay.sta_nld2;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class STA_NoLoadTestBay2Reset extends TimerTask{
	//public static Logger logger = Logger.getLogger(STA_NoLoadTestBay2.class.getPackage().getName()); 
	private STA_NoLoadTestBay2Context sctNltBay2ResetStateManager = new STA_NoLoadTestBay2Context();  
	public static boolean abort_SCT_NLT_Bay2 = false ;
	public void run() {
		StaNld_Bay2.logger.debug("ShortCircuit_NoLoadTestBay2_2 : Entry"); 

		manageShortCircuit_NoLoadTestBay2ResetStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageShortCircuit_NoLoadTestBay2ResetStates() {

		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2Reset : manageShortCircuit_NoLoadTestBay2ResetStates : Entry");

		//setTableStatePlanner_SctNltBay2(StatePlannerController.getTableStatePlannerSctNltBay2_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD2_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.STA_NLD2_BAY_KEY);
		setTableStatePlanner_StaNldTestBay2(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if(getTableStatePlanner_StaNldTestBay2().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaNldTestBay2().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay2State currentState = createSctNltBay2StateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			StaNld_Bay2.logger.debug("STA_NoLoadTestBay2Reset : manageShortCircuit_NoLoadTestBay2ResetStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_StaNldTestBay2().size());
	
	
			while (!StaNld_Bay2.isResetProcessCompletedStaNldBay2() &&
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
						STA_NoLoadTestBay2State nextState = createSctNltBay2StateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_StaNldTestBay2()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
					}
	
				}
				else{
					//======
					// update in the table.
					String errorCode = bayStatus.getErrorCode() ;
					String nextStateName = getErrorStateInstance(errorCode);//createStateInstance("S22_error_Handling");
	
					presentRow.setIfFailed(nextStateName);
	
					//=====
	
					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();
	
					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S13_error_Handling")) {
							STA_NoLoadTestBay2State nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}else if (nextStateName.equals("S13_error_Handling_Bay2")) {
							STA_NoLoadTestBay2State nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						}  else {
							STA_NoLoadTestBay2State nextState2 = createSctNltBay2StateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
	
					}
	
	
					for (StateFlow row : getTableStatePlanner_StaNldTestBay2()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			StaNld_Bay2.logger.debug("STA_NoLoadTestBay2Reset : manageShortCircuit_NoLoadTestBay2ResetStates : No states found in the planner");
		}
		StaNld_Bay2.logger.debug("STA_NoLoadTestBay2Reset : manageShortCircuit_NoLoadTestBay2ResetStates : Exit");

	}

	//=====================================================================================================================

	/*	public static void singleStateTestRun(SctNltBay2State currentState){
		ShortCircuit_NoLoadTestBay2.logger.debug("singleStateTestRun : Entry");

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		ShortCircuit_NoLoadTestBay2.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		ShortCircuit_NoLoadTestBay2.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		ShortCircuit_NoLoadTestBay2.logger.debug("singleStateTestRun : Exit");
	}*/

	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay2State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay2ResetStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay2ResetStateManager.processPresentState();

		StaNld_Bay2.logger.debug("processCurrentState : " + sctNltBay2ResetStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger.debug("processCurrentState : " + sctNltBay2ResetStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay2State getPreviousState(){
		return sctNltBay2ResetStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_SctNltBay2 = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay2 = new ArrayList<StateFlow>();
	// Helper method to create a state instance dynamically based on the state name
	public static STA_NoLoadTestBay2State createSctNltBay2StateInstance(String stateName) {
		try {
			// Get the fully qualified class name dynamically
			String packageName = STA_NoLoadTestBay2State.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of STA_NoLoadTestBay2State
			if (!STA_NoLoadTestBay2State.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (STA_NoLoadTestBay2State) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}


	private STA_NoLoadTestBay2State createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S13_error_Handling_Bay2(errorCode);
		case "S22_error_Handling_Bay2":
			return new S13_error_Handling_Bay2(errorCode);	
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		default:
			return "S13_error_Handling_Bay2";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_StaNldTestBay2() {
		return tableStatePlanner_StaNldTestBay2;
	}

	public void setTableStatePlanner_StaNldTestBay2(ArrayList<StateFlow> tableStatePlanner_SctNltBay2) {
		this.tableStatePlanner_StaNldTestBay2 = tableStatePlanner_SctNltBay2;
	}



}

