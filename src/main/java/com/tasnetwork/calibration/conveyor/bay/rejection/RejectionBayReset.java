package com.tasnetwork.calibration.conveyor.bay.rejection;


import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class RejectionBayReset extends TimerTask{
//	public static Logger logger = Logger.getLogger(RejectionBay.class.getPackage().getName()); 
	private RejectionBayContext rejectionBayResetStateManager = new RejectionBayContext();  
	public static boolean abort_Rejection_Bay = false ;
	public void run() {
		Rejection.logger.debug("RejectionBay2 : Entry"); 

		manageRejectionBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageRejectionBayStates() {

		Rejection.logger.debug("RejectionBayReset : manageRejectionBayStates : Entry");

		//setTableStatePlanner_RejectionBay(StatePlannerController.getTableStatePlannerRejectionBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.REJECTION_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.REJECTION_BAY_KEY);
		setTableStatePlanner_RejectionBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_RejectionBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_RejectionBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			RejectionBayState currentState = createRejectionBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
			Rejection.logger.debug("RejectionBayReset : manageRejectionBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_RejectionBay().size());
	
			while (!Rejection.isResetProcessCompletedRejectionBay() &&
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
						RejectionBayState nextState = createRejectionBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_RejectionBay()) {
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
						if (nextStateName.equals("S03_error_Handling")) {
							RejectionBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							RejectionBayState nextState2 = createRejectionBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_RejectionBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			Rejection.logger.debug("RejectionBayReset : manageRejectionBayStates : getTableStatePlanner2  : No states found in the planner");
		}
	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(RejectionBayState currentState){
		RejectionBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		RejectionBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		RejectionBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		RejectionBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(RejectionBayState newState) {
		//Set previous state here 

		// Set the new state
		rejectionBayResetStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = rejectionBayResetStateManager.processPresentState();

		Rejection.logger.debug("processCurrentState : " + rejectionBayResetStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Rejection.logger.debug("processCurrentState : " + rejectionBayResetStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public RejectionBayState getPreviousState(){
		return rejectionBayResetStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_RejectionBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_RejectionBay = new ArrayList<StateFlow>();
	
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_RejectionBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static RejectionBayState createRejectionBayStateInstance(String stateName) {
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_pallets_at_Reject_Bay": 
	                return new S01_check_for_pallets_at_Reject_Bay();
	            case "S03_error_Handling":
	                return new S03_error_Handling(); 
	            case "S04_idle_condition":
	                return new S04_idle_condition(); 
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private RejectionBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S03_error_Handling":
			return new S03_error_Handling(errorCode);
		default:
			throw new IllegalArgumentException("Unknown state: " + stateName);
		}
	}


	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
		default:
			return "S03_error_Handling";
		}
	}

	public ArrayList<StateFlow> getTableStatePlanner_RejectionBay() {
		return tableStatePlanner_RejectionBay;
	}

	public void setTableStatePlanner_RejectionBay(ArrayList<StateFlow> tableStatePlanner_RejectionBay) {
		this.tableStatePlanner_RejectionBay = tableStatePlanner_RejectionBay;
	}



}

