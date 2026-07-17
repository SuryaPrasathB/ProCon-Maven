package com.tasnetwork.calibration.conveyor.bay.unloading;


import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Unloading extends TimerTask{
	public static Logger logger = Logger.getLogger(Unloading.class.getPackage().getName()); 
	private UnloadingBayContext unloadingBayStateManager = new UnloadingBayContext();  
	
	
	public static boolean startProcessRequestedUnloadingBay = false ;
	public static boolean stopProcessRequestedUnloadingBay = false ;
	public static boolean resetProcessRequestedUnloadingBay = false ;

	public static boolean startProcessCompletedUnloadingBay = false ;
	public static boolean stopProcessCompletedUnloadingBay = false ;
	public static boolean resetProcessCompletedUnloadingBay = false ;
	
	public static boolean abort_Unloading_Bay = false ;
	public void run() {
		Unloading.logger.debug("UnloadingBay2 : Entry"); 

		manageUnloadingBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageUnloadingBayStates() {

		Unloading.logger.debug("UnloadingBay2 : manageUnloadingBayStates : Entry");

		//setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerUnloadingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.UNLOADING_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.UNLOADING_BAY_KEY);
		
		
		setTableStatePlanner_UnloadingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_UnloadingBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_UnloadingBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			UnloadingBayState currentState = createUnloadingBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Unloading.logger.debug("UnloadingBay2 : manageUnloadingBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_UnloadingBay().size());
	
			setStopProcessCompletedUnloadingBay(false);
			setStopProcessRequestedUnloadingBay(false);
			
			setStartProcessCompletedUnloadingBay(true);
			
			while (!isStopProcessRequestedUnloadingBay() &&
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
						UnloadingBayState nextState = createUnloadingBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_UnloadingBay()) {
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
						if (nextStateName.equals("S10_error_Handling")) {
			UnloadingBayState nextState2 = createErrorStateInstance(nextStateName, errorCode);
			setNextState(nextState2); // Set the next state dynamically
						} else {
							UnloadingBayState nextState2 = createUnloadingBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_UnloadingBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			Unloading.logger.debug("UnloadingBay2 : manageUnloadingBayStates : getTableStatePlanner2  : No states found in the planner");
		}
	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(UnloadingBayState currentState){
		UnloadingBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		UnloadingBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		UnloadingBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		UnloadingBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(UnloadingBayState newState) {
		//Set previous state here 

		// Set the new state
		unloadingBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = unloadingBayStateManager.processPresentState();

		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Unloading.logger.debug("processCurrentState : " + unloadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public UnloadingBayState getPreviousState(){
		return unloadingBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_UnloadingBay = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_UnloadingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static UnloadingBayState createUnloadingBayStateInstance(String stateName) {
        // Create and return an instance of the state class based on the state name
        switch (stateName) {
            case "S01_check_for_pallet_at_Unloading_Bay":
                return new S01_check_for_pallet_at_Unloading_Bay();
            case "S02_qR_Code_Scanning_of_Pallet":
                return new S02_qR_Code_Scanning_of_Pallet();
            case "S03_check_for_empty_pallet":
                return new S03_check_for_empty_pallet();
            case "S04_check_for_failed_meters":
                return new S04_check_for_failed_meters();
            case "S05_indicate_the_status_of_meters":
                return new S05_indicate_the_status_of_meters();
            case "S06_check_for_alarm_pushButton_status":
                return new S06_check_for_alarm_pushButton_status();
            case "S07_check_for_loadingBay_pushButton_status":
                return new S07_check_for_loadingBay_pushButton_status();
            case "S08_check_for_pallet_at_Loading_Bay":
                return new S08_check_for_pallet_at_Loading_Bay();
            case "S09_let_the_pallet_to_Loading_Bay":
                return new S09_let_the_pallet_to_Loading_Bay();
            case "S10_error_Handling":
                return new S10_error_Handling(); 
            case "S11_idle_condition":
                return new S11_idle_condition(); 
            case "S12_close_stop_latch_Unloading_Bay":
                return new S12_close_stop_latch_Unloading_Bay();
            case "S13_open_stop_latch_Unloading_Bay":
                return new S13_open_stop_latch_Unloading_Bay();
            case "S29_turn_on_tower_lamp_Unloading_Bay" :
                return new S29_turn_on_tower_lamp_Unloading_Bay();
            case "S30_turn_off_tower_lamp_Unloading_Bay" :
                return new S30_turn_off_tower_lamp_Unloading_Bay();
            default:
            	Unloading.logger.debug("createRejectionBayStateInstance: Unknown state: " + stateName); 
                throw new IllegalArgumentException("Unknown state: " + stateName);
        }
    }


	private UnloadingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new S10_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		default:
			return "S10_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_UnloadingBay() {
		return tableStatePlanner_UnloadingBay;
	}

	public void setTableStatePlanner_UnloadingBay(ArrayList<StateFlow> tableStatePlanner_FtBay) {
		this.tableStatePlanner_UnloadingBay = tableStatePlanner_FtBay;
	}

	public static boolean isStopProcessRequestedUnloadingBay() {
		return stopProcessRequestedUnloadingBay;
	}

	public static boolean isStopProcessCompletedUnloadingBay() {
		return stopProcessCompletedUnloadingBay;
	}

	public static boolean isResetProcessCompletedUnloadingBay() {
		return resetProcessCompletedUnloadingBay;
	}

	public static void setStopProcessRequestedUnloadingBay(boolean stopProcessRequestedUnloadingBay) {
		Unloading.stopProcessRequestedUnloadingBay = stopProcessRequestedUnloadingBay;
	}

	public static void setStopProcessCompletedUnloadingBay(boolean stopProcessCompletedUnloadingBay) {
		Unloading.stopProcessCompletedUnloadingBay = stopProcessCompletedUnloadingBay;
	}

	public static void setResetProcessCompletedUnloadingBay(boolean resetProcessCompletedUnloadingBay) {
		Unloading.resetProcessCompletedUnloadingBay = resetProcessCompletedUnloadingBay;
	}

	public static boolean isResetProcessRequestedUnloadingBay() {
		return resetProcessRequestedUnloadingBay;
	}

	public static void setResetProcessRequestedUnloadingBay(boolean resetProcessRequestedUnloadingBay) {
		Unloading.resetProcessRequestedUnloadingBay = resetProcessRequestedUnloadingBay;
	}

	public static boolean isStartProcessRequestedUnloadingBay() {
		return startProcessRequestedUnloadingBay;
	}

	public static void setStartProcessRequestedUnloadingBay(boolean startProcessRequestedUnloadingBay) {
		Unloading.startProcessRequestedUnloadingBay = startProcessRequestedUnloadingBay;
	}

	public static boolean isStartProcessCompletedUnloadingBay() {
		return startProcessCompletedUnloadingBay;
	}

	public static void setStartProcessCompletedUnloadingBay(boolean startProcessCompletedUnloadingBay) {
		Unloading.startProcessCompletedUnloadingBay = startProcessCompletedUnloadingBay;
	}



}

