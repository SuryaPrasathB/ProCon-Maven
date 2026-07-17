package com.tasnetwork.calibration.conveyor.bay.sta_nld1;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class STA_NoLoadTestBay1Reset extends TimerTask{
	//public static Logger logger = Logger.getLogger(STA_NoLoadTestBay1.class.getPackage().getName()); 
	private STA_NoLoadTestBay1Context sctNltBay1ResetStateManager = new STA_NoLoadTestBay1Context();  
	public static boolean abort_SCT_NLT_Bay1 = false ;


	public void run() {
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay12 : Entry"); 

		manageShortCircuit_NoLoadTestBay1ResetStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageShortCircuit_NoLoadTestBay1ResetStates() {

		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Reset : manageShortCircuit_NoLoadTestBay1ResetStates : Entry");

		//setTableStatePlanner_SctNltBay1(StatePlannerController.getTableStatePlannerSctNltBay1_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD1_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.STA_NLD1_BAY_KEY);
		setTableStatePlanner_StaNldTestBay1(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process

		if(getTableStatePlanner_StaNldTestBay1().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaNldTestBay1().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay1State currentState = createSctNltBay1StateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Reset : manageShortCircuit_NoLoadTestBay1ResetStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_StaNldTestBay1().size());
	
	
			while (!StaNld_Bay1.isResetProcessCompletedStaNldBay1() &&
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
						STA_NoLoadTestBay1State nextState = createSctNltBay1StateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_StaNldTestBay1()) {
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
					String errorCode = bayStatus.getErrorCode() ;
					String nextStateName = getErrorStateInstance(errorCode);//createStateInstance("S22_error_Handling");
	
					presentRow.setIfFailed(nextStateName);
	
					//=====
	
					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();
	
					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S13_error_Handling")) {
							STA_NoLoadTestBay1State nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else if (nextStateName.equals("S13_error_Handling_Bay1")) {
							STA_NoLoadTestBay1State nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							STA_NoLoadTestBay1State nextState2 = createSctNltBay1StateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
					}
	
	
	
					for (StateFlow row : getTableStatePlanner_StaNldTestBay1()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
		}else {
			StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Reset : manageShortCircuit_NoLoadTestBay1ResetStates : No states found in the planner");
		}
		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Reset : manageShortCircuit_NoLoadTestBay1ResetStates : Exit");

	}

	//=====================================================================================================================

	/*	public static void singleStateTestRun(SctNltBay1State currentState){
		ShortCircuit_NoLoadTestBay1.logger.debug("singleStateTestRun : Entry");

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		ShortCircuit_NoLoadTestBay1.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		ShortCircuit_NoLoadTestBay1.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		ShortCircuit_NoLoadTestBay1.logger.debug("singleStateTestRun : Exit");
	}*/

	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay1State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay1ResetStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay1ResetStateManager.processPresentState();

		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1ResetStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1ResetStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay1State getPreviousState(){
		return sctNltBay1ResetStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_SctNltBay1 = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay1 = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_StaNldTestBay1()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static STA_NoLoadTestBay1State createSctNltBay1StateInstance(String stateName) {
		try {
			// Get the fully qualified class name dynamically
			String packageName = STA_NoLoadTestBay1State.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of STA_NoLoadTestBay1State
			if (!STA_NoLoadTestBay1State.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (STA_NoLoadTestBay1State) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}


	private STA_NoLoadTestBay1State createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S22_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020:
			return "S14_idle_condition_Bay1";
		default:
			return "S13_error_Handling_Bay1";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_StaNldTestBay1() {
		return tableStatePlanner_StaNldTestBay1;
	}

	public void setTableStatePlanner_StaNldTestBay1(ArrayList<StateFlow> tableStatePlanner_SctNltBay1) {
		this.tableStatePlanner_StaNldTestBay1 = tableStatePlanner_SctNltBay1;
	}



}

