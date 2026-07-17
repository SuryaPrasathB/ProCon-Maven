package com.tasnetwork.calibration.conveyor.bay.verific;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.unloading.Unloading;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Verification extends TimerTask{
	public static Logger logger = Logger.getLogger(Verification.class.getPackage().getName()); 
	private VerificationBayContext VerificTestBayStateManager = new VerificationBayContext(); 
	
	public static boolean startProcessRequestedVerificBay = false ;
	public static boolean stopProcessRequestedVerificBay = false ;
	public static boolean resetProcessRequestedVerificBay = false ;

	public static boolean startProcessCompletedVerificBay = false ;
	public static boolean stopProcessCompletedVerificBay = false ;
	public static boolean resetProcessCompletedVerificBay = false ;
	
	public static boolean abort_VerificTest_Bay = false ;
	public void run() {
		Verification.logger.debug("VerificationTestBay2 : Entry"); 

		manageVerificationTestBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageVerificationTestBayStates() {

		Verification.logger.debug("VerificationTestBay2 : manageVerificationTestBayStates : Entry");

		//setTableStatePlanner_VerificBay(StatePlannerController.getTableStatePlannerVerificBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.VERIFICATION_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.VERIFICATION_BAY_KEY);
		setTableStatePlanner_VerificBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_VerificBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_VerificBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			VerificTestBayState currentState = createVerificTestBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Verification.logger.debug("VerificationTestBay2 : manageVerificationTestBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_VerificBay().size());
	
			setStartProcessCompletedVerificBay(true);
			
			while (!isStopProcessRequestedVerificBay() &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				
				Verification.logger.debug("VERIFIC BAY : isStopProcessRequested : " + isStopProcessRequestedVerificBay());
				
				// Process the current state
				BayResponse bayStatus = processCurrentState();
	
				presentRow = nextRow ;
	
				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)
	
					String nextStateName = presentRow.getIfSuccess();
	
					if (nextStateName != null && !nextStateName.isEmpty()) {
						// Set the next state based on the success column
						VerificTestBayState nextState = createVerificTestBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_VerificBay()) {
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
						if (nextStateName.equals("S17_error_Handling")) {
							VerificTestBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							VerificTestBayState nextState2 = createVerificTestBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
					
					}
	
	
					for (StateFlow row : getTableStatePlanner_VerificBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			Verification.logger.debug("VerificationTestBay2 : manageVerificationTestBayStates : No states found in the planner");
		}
	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(VerificTestBayState currentState){
		VerificationTestBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		VerificationTestBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		VerificationTestBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		VerificationTestBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(VerificTestBayState newState) {
		//Set previous state here 

		// Set the new state
		VerificTestBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = VerificTestBayStateManager.processPresentState();

		Verification.logger.debug("processCurrentState : " + VerificTestBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Verification.logger.debug("processCurrentState : " + VerificTestBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public VerificTestBayState getPreviousState(){
		return VerificTestBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_VerificBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_VerificBay = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_VerificBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static VerificTestBayState createVerificTestBayStateInstance(String stateName) {
	    try {
	        // Get the fully qualified class name dynamically
	        String packageName = VerificTestBayState.class.getPackage().getName(); // Adjust if necessary
	        Class<?> c = Class.forName(packageName + "." + stateName);

	        // Ensure the class is a subclass of VerificTestBayState
	        if (!VerificTestBayState.class.isAssignableFrom(c)) {
	            throw new IllegalArgumentException("Invalid state class: " + stateName);
	        }

	        // Create an instance using the default constructor
	        return (VerificTestBayState) c.getDeclaredConstructor().newInstance();
	    } catch (ClassNotFoundException e) {
	        throw new IllegalArgumentException("Unknown state: " + stateName, e);
	    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
	        throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
	    }
	}
	
	private VerificTestBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S17_error_Handling":
			return new S17_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022 :
			return "S01_check_for_pallets_at_Verific_Bay";
		case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_013 :
			return "S12_check_for_pallets_at_SCT_NLT_Bay2";
		case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_014 :
			return "S08_check_for_pallets_at_SCT_NLT_Bay1";
		case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_030 :
			return S18_idle_condition.class.getSimpleName();
		case ConvErrorCodeMapping.ERROR_CODE_VERIFIC_031 :
			return S18_idle_condition.class.getSimpleName();
		default:
			return "S17_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_VerificBay() {
		return tableStatePlanner_VerificBay;
	}

	public void setTableStatePlanner_VerificBay(ArrayList<StateFlow> tableStatePlanner_VerificBay) {
		this.tableStatePlanner_VerificBay = tableStatePlanner_VerificBay;
	}

	public static boolean isStopProcessRequestedVerificBay() {
		return stopProcessRequestedVerificBay;
	}

	public static boolean isStopProcessCompletedVerificBay() {
		return stopProcessCompletedVerificBay;
	}

	public static boolean isResetProcessCompletedVerificBay() {
		return resetProcessCompletedVerificBay;
	}

	public static void setStopProcessRequestedVerificBay(boolean stopProcessRequestedVerificBay) {
		Verification.stopProcessRequestedVerificBay = stopProcessRequestedVerificBay;
	}

	public static void setStopProcessCompletedVerificBay(boolean stopProcessCompletedVerificBay) {
		Verification.stopProcessCompletedVerificBay = stopProcessCompletedVerificBay;
	}

	public static void setResetProcessCompletedVerificBay(boolean resetProcessCompletedVerificBay) {
		Verification.resetProcessCompletedVerificBay = resetProcessCompletedVerificBay;
	}

	public static boolean isResetProcessRequestedVerificBay() {
		return resetProcessRequestedVerificBay;
	}

	public static void setResetProcessRequestedVerificBay(boolean resetProcessRequestedVerificBay) {
		Verification.resetProcessRequestedVerificBay = resetProcessRequestedVerificBay;
	}

	public static boolean isStartProcessRequestedVerificBay() {
		return startProcessRequestedVerificBay;
	}

	public static void setStartProcessRequestedVerificBay(boolean startProcessRequestedVerificBay) {
		Verification.startProcessRequestedVerificBay = startProcessRequestedVerificBay;
	}

	public static boolean isStartProcessCompletedVerificBay() {
		return startProcessCompletedVerificBay;
	}

	public static void setStartProcessCompletedVerificBay(boolean startProcessCompletedVerificBay) {
		Verification.startProcessCompletedVerificBay = startProcessCompletedVerificBay;
	}



}

