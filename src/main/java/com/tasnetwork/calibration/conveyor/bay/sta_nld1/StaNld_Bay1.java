package com.tasnetwork.calibration.conveyor.bay.sta_nld1;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S041_Start_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S051_Stop_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S042_Start_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S043_Get_Test_Point_Status_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S044_Close_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S045_Select_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S046_Stop_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class StaNld_Bay1 extends TimerTask{
	public static Logger logger = Logger.getLogger(StaNld_Bay1.class.getPackage().getName()); 
	private STA_NoLoadTestBay1Context sctNltBay1StateManager = new STA_NoLoadTestBay1Context();  
	public static boolean abort_SCT_NLT_Bay1 = false ;

	public static boolean startProcessRequestedStaNldBay1 = false ;
	public static boolean stopProcessRequestedStaNldBay1 = false ;
	public static boolean resetProcessRequestedStaNldBay1 = false ;

	public static boolean startProcessCompletedStaNldBay1 = false ;
	public static boolean stopProcessCompletedStaNldBay1 = false ;
	public static boolean resetProcessCompletedStaNldBay1 = false ;

	public void run() {
		StaNld_Bay1.logger.debug("StaNld_Bay1 : Entry"); 

		manageSta_NoLoadTestBay1States();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageSta_NoLoadTestBay1States() {

		StaNld_Bay1.logger.debug("StaNld_Bay1 : manageSta_NoLoadTestBay1States : Entry");

		//setTableStatePlanner_SctNltBay1(StatePlannerController.getTableStatePlannerSctNltBay1_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD1_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.STA_NLD1_BAY_KEY);
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
	
	
			StaNld_Bay1.logger.debug("StaNld_Bay1 : manageSta_NoLoadTestBay1States : getTableStatePlanner2 : Size : " + getTableStatePlanner_StaNldTestBay1().size());
	
			setStartProcessCompletedStaNldBay1(true);
	
			while (!isStopProcessRequestedStaNldBay1() &&
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
						if (nextStateName.equals("S13_error_Handling_Bay1")) {
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
			StaNld_Bay1.logger.debug("StaNld_Bay1 : manageSta_NoLoadTestBay1States : getTableStatePlanner2  : No states found in the planner");
		}
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
		case "S13_error_Handling":
			return new S13_error_Handling_Bay1(errorCode);
		case "S13_error_Handling_Bay1":
			return new S13_error_Handling_Bay1(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020 :
			return "S12_release_the_pallet_release_semaphore_Bay1";
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

	public static boolean isStopProcessRequestedStaNldBay1() {
		return stopProcessRequestedStaNldBay1;
	}

	public static boolean isStopProcessCompletedStaNldBay1() {
		return stopProcessCompletedStaNldBay1;
	}

	public static boolean isResetProcessCompletedStaNldBay1() {
		return resetProcessCompletedStaNldBay1;
	}

	public static void setStopProcessRequestedStaNldBay1(boolean stopProcessRequestedSctNltBay1) {
		StaNld_Bay1.stopProcessRequestedStaNldBay1 = stopProcessRequestedSctNltBay1;
	}

	public static void setStopProcessCompletedStaNldBay1(boolean stopProcessCompletedSctNltBay1) {
		StaNld_Bay1.stopProcessCompletedStaNldBay1 = stopProcessCompletedSctNltBay1;
	}

	public static void setResetProcessCompletedStaNldBay1(boolean resetProcessCompletedSctNltBay1) {
		StaNld_Bay1.resetProcessCompletedStaNldBay1 = resetProcessCompletedSctNltBay1;
	}

	public static boolean isStartProcessRequestedStaNldBay1() {
		return startProcessRequestedStaNldBay1;
	}

	public static void setStartProcessRequestedStaNldBay1(boolean startProcessRequestedSctNltBay1) {
		StaNld_Bay1.startProcessRequestedStaNldBay1 = startProcessRequestedSctNltBay1;
	}

	public static boolean isResetProcessRequestedStaNldBay1() {
		return resetProcessRequestedStaNldBay1;
	}

	public static void setResetProcessRequestedStaNldBay1(boolean resetProcessRequestedSctNltBay1) {
		StaNld_Bay1.resetProcessRequestedStaNldBay1 = resetProcessRequestedSctNltBay1;
	}

	public static boolean isStartProcessCompletedStaNldBay1() {
		return startProcessCompletedStaNldBay1;
	}

	public static void setStartProcessCompletedStaNldBay1(boolean startProcessCompletedSctNltBay1) {
		StaNld_Bay1.startProcessCompletedStaNldBay1 = startProcessCompletedSctNltBay1;
	}



}

