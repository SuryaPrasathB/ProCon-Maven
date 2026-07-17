package com.tasnetwork.calibration.conveyor.bay.sta_nld2;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S27_turn_on_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S28_turn_off_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S29_turn_on_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S30_turn_off_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import groovyjarjarantlr4.v4.parse.ANTLRParser.labeledAlt_return;
import javafx.scene.control.TableView;

public class StaNld_Bay2 extends TimerTask{
	public static Logger logger = Logger.getLogger(StaNld_Bay2.class.getPackage().getName()); 
	private STA_NoLoadTestBay2Context sctNltBay2StateManager = new STA_NoLoadTestBay2Context();  
	public static boolean abort_SCT_NLT_Bay2 = false ;

	public static boolean startProcessRequestedStaNldBay2 = false ;
	public static boolean stopProcessRequestedStaNldBay2 = false ;
	public static boolean resetProcessRequestedStaNldBay2 = false ;

	public static boolean startProcessCompletedStaNldBay2 = false ;
	public static boolean stopProcessCompletedStaNldBay2 = false ;
	public static boolean resetProcessCompletedStaNldBay2 = false ;

	public void run() {
		StaNld_Bay2.logger.debug("StaNld_Bay2 : Entry"); 

		manageSta_NoLoadTestBay2States();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageSta_NoLoadTestBay2States() {

		StaNld_Bay2.logger.debug("StaNld_Bay2 : manageSta_NoLoadTestBay2States : Entry");

		//setTableStatePlanner_SctNltBay2(StatePlannerController.getTableStatePlannerSctNltBay2_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD2_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.STA_NLD2_BAY_KEY);
		setTableStatePlanner_StaNldTestBay2(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_StaNldTestBay2().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaNldTestBay2().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay2State currentState = createSctNltBay2StateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			StaNld_Bay2.logger.debug("StaNld_Bay2 : manageSta_NoLoadTestBay2States : getTableStatePlanner2 : Size : " + getTableStatePlanner_StaNldTestBay2().size());
			
			setStartProcessCompletedStaNldBay2(true);
	
			while (!isStopProcessRequestedStaNldBay2() &&
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
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_StaNldTestBay2()) {
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
							STA_NoLoadTestBay2State nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else if (nextStateName.equals("S13_error_Handling_Bay2")) {
							STA_NoLoadTestBay2State nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							STA_NoLoadTestBay2State nextState2 = createSctNltBay2StateInstance(nextStateName);
							StaNld_Bay2.logger.debug("StaNld_Bay2 : manageSta_NoLoadTestBay2States : else  : nextStateName" + nextStateName);
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
			StaNld_Bay2.logger.debug("StaNld_Bay2 : manageSta_NoLoadTestBay2States : getTableStatePlanner2  : No states found in the planner");
		}
		StaNld_Bay2.logger.debug("StaNld_Bay2 : manageSta_NoLoadTestBay2States : exit");
	}

	//=====================================================================================================================

	/*	public static void singleStateTestRun(SctNltBay2State currentState){
		Sta_NoLoadTestBay2.logger.debug("singleStateTestRun : Entry");

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		Sta_NoLoadTestBay2.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		Sta_NoLoadTestBay2.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		Sta_NoLoadTestBay2.logger.debug("singleStateTestRun : Exit");
	}*/

	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay2State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay2StateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay2StateManager.processPresentState();

		StaNld_Bay2.logger.debug("StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay2.logger.debug("StaNld_Bay2 : processCurrentState : " + sctNltBay2StateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay2State getPreviousState(){
		return sctNltBay2StateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_SctNltBay2 = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay2 = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_StaNldTestBay2()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

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
		case "S13_error_Handling":
			return new S13_error_Handling_Bay2(errorCode);
		case "S13_error_Handling_Bay2":
			return new S13_error_Handling_Bay2(errorCode);	
			
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		//case :
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

	public static boolean isStopProcessRequestedStaNldBay2() {
		return stopProcessRequestedStaNldBay2;
	}

	public static boolean isStopProcessCompletedStaNldBay2() {
		return stopProcessCompletedStaNldBay2;
	}

	public static boolean isResetProcessCompletedStaNldBay2() {
		return resetProcessCompletedStaNldBay2;
	}

	public static void setStopProcessRequestedStaNldBay2(boolean stopProcessRequestedSctNltBay2) {
		StaNld_Bay2.stopProcessRequestedStaNldBay2 = stopProcessRequestedSctNltBay2;
	}

	public static void setStopProcessCompletedStaNldBay2(boolean stopProcessCompletedSctNltBay2) {
		StaNld_Bay2.stopProcessCompletedStaNldBay2 = stopProcessCompletedSctNltBay2;
	}

	public static void setResetProcessCompletedStaNldBay2(boolean resetProcessCompletedSctNltBay2) {
		StaNld_Bay2.resetProcessCompletedStaNldBay2 = resetProcessCompletedSctNltBay2;
	}

	public static boolean isResetProcessRequestedStaNldBay2() {
		return resetProcessRequestedStaNldBay2;
	}

	public static void setResetProcessRequestedStaNldBay2(boolean resetProcessRequestedSctNltBay2) {
		StaNld_Bay2.resetProcessRequestedStaNldBay2 = resetProcessRequestedSctNltBay2;
	}

	public static boolean isStartProcessRequestedStaNldBay2() {
		return startProcessRequestedStaNldBay2;
	}

	public static void setStartProcessRequestedStaNldBay2(boolean startProcessRequestedSctNltBay2) {
		StaNld_Bay2.startProcessRequestedStaNldBay2 = startProcessRequestedSctNltBay2;
	}

	public static boolean isStartProcessCompletedStaNldBay2() {
		return startProcessCompletedStaNldBay2;
	}

	public static void setStartProcessCompletedStaNldBay2(boolean startProcessCompletedSctNltBay2) {
		StaNld_Bay2.startProcessCompletedStaNldBay2 = startProcessCompletedSctNltBay2;
	}



}

