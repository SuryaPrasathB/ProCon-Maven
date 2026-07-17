package com.tasnetwork.calibration.conveyor.bay.verific_waiting;


import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class WaitingBayStop extends TimerTask{
	//public static Logger logger = Logger.getLogger(WaitingBay.class.getPackage().getName()); 
	private WaitingBayContext WaitingBayStopStateManager = new WaitingBayContext();  
	public static boolean abort_Waiting_Bay = false ;
	public void run() {
		VerificWaiting.logger.debug("WaitingBayStop : Entry"); 

		manageWaitingBayStopStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageWaitingBayStopStates() {

		VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : Entry");

		//setTableStatePlanner_WaitingBay(StatePlannerController.getTableStatePlannerWaitingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.WAITING_BAY_KEY, "STOP");//findByBayKey(ConstantConveyor.WAITING_BAY_KEY);
		setTableStatePlanner_WaitingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_WaitingBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_WaitingBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			WaitingBayState currentState = createWaitingBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_WaitingBay().size());
	
	
			while (!VerificWaiting.isStopProcessCompletedWaitingBay() &&
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
						WaitingBayState nextState = createWaitingBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_WaitingBay()) {
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
						if (nextStateName.equals("S05_error_Handling")) {
							WaitingBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							WaitingBayState nextState2 = createWaitingBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_WaitingBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : getTableStatePlanner2  : No states found in the planner");
		}
		
		VerificWaiting.logger.debug("WaitingBayStop : manageWaitingBayStopStates : Exit");

	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(WaitingBayState currentState){
		WaitingBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		WaitingBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		WaitingBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		WaitingBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(WaitingBayState newState) {
		//Set previous state here 

		// Set the new state
		WaitingBayStopStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = WaitingBayStopStateManager.processPresentState();

		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStopStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		VerificWaiting.logger.debug("processCurrentState : " + WaitingBayStopStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public WaitingBayState getPreviousState(){
		return WaitingBayStopStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_WaitingBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_WaitingBay = new ArrayList<StateFlow>();
	
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_WaitingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static WaitingBayState createWaitingBayStateInstance(String stateName) {           
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_all_pallets_at_Waiting_Bay":
	                return new S01_check_for_all_pallets_at_Waiting_Bay();
	            case "S02_check_for_pallets_at_Verific_Bay":
	                return new S02_check_for_pallets_at_Verific_Bay();
	            case "S03_let_the_pallet_to_Verific_Bay":
	                return new S03_let_the_pallet_to_Verific_Bay();
	            case "S04_ensure_all_pallets_reached_Verific_Bay":
	                return new S04_ensure_all_pallets_reached_Verific_Bay();
	            case "S05_error_Handling":
	                return new S05_error_Handling(); 
	            case "S06_idle_condition":
	                return new S06_idle_condition(); 
	            case "S07_open_stop_latch_Waiting_Bay":
	                return new S07_open_stop_latch_Waiting_Bay();
	            case "S08_close_stop_latch_Waiting_Bay":
	                return new S08_close_stop_latch_Waiting_Bay();
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private WaitingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S05_error_Handling":
			return new S05_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		default:
			return "S05_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_WaitingBay() {
		return tableStatePlanner_WaitingBay;
	}

	public void setTableStatePlanner_WaitingBay(ArrayList<StateFlow> tableStatePlanner_WaitingBay) {
		this.tableStatePlanner_WaitingBay = tableStatePlanner_WaitingBay;
	}



}

