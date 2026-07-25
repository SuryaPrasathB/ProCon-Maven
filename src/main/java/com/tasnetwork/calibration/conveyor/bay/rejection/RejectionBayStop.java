package com.tasnetwork.calibration.conveyor.bay.rejection;


import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class RejectionBayStop extends TimerTask{
	//public static Logger logger = Logger.getLogger(RejectionBay.class.getPackage().getName()); 
	private RejectionBayContext rejectionBayStopStateManager = new RejectionBayContext();  
	public static boolean abort_Rejection_Bay = false ;
	public void run() {
		Rejection.logger.debug("RejectionBayStop : Entry"); 

		manageRejectionBayStopStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageRejectionBayStopStates() {

		Rejection.logger.debug("RejectionBayStop : manageRejectionBayStopStates : Entry");

		//setTableStatePlanner_RejectionBay(StatePlannerController.getTableStatePlannerRejectionBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.REJECTION_BAY_KEY, "STOP");//findByBayKey(ConstantConveyor.REJECTION_BAY_KEY);
		setTableStatePlanner_RejectionBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if(getTableStatePlanner_RejectionBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_RejectionBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			RejectionBayState currentState = RejectionBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Rejection.logger.debug("RejectionBayStop : manageRejectionBayStopStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_RejectionBay().size());
	
	
			while (!Rejection.isStopProcessCompletedRejectionBay() &&
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
						RejectionBayState nextState = RejectionBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					} else {
						Rejection.setStopProcessCompletedRejectionBay(true);
						break;
					}
					for (StateFlow row : getTableStatePlanner_RejectionBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
					}
	
				}
				else{
					String errorCode = bayStatus.getErrorCode() ;
					//======
					// update in the table.
					Rejection rejection = new Rejection();
					String nextStateName = rejection.getErrorStateInstanceString(errorCode);//createStateInstance("S22_error_Handling");
	
					presentRow.setIfFailed(nextStateName);
	
					//=====
	
					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();
	
					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S03_error_Handling")) {
							RejectionBayState nextState2 =  RejectionBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							RejectionBayState nextState2 = RejectionBayState.createState(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					} else {
						Rejection.setStopProcessCompletedRejectionBay(true);
						break;
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
			Rejection.logger.debug("RejectionBayStop : manageRejectionBayStopStates : getTableStatePlanner2  : No states found in the planner");
		}
		
		Rejection.logger.debug("RejectionBayStop : manageRejectionBayStopStates : Exit");

	}
	public void setNextState(RejectionBayState newState) {
		//Set previous state here 

		// Set the new state
		rejectionBayStopStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = rejectionBayStopStateManager.processPresentState();

		Rejection.logger.debug("processCurrentState : " + rejectionBayStopStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Rejection.logger.debug("processCurrentState : " + rejectionBayStopStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public RejectionBayState getPreviousState(){
		return rejectionBayStopStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_RejectionBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_RejectionBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_RejectionBay() {
		return tableStatePlanner_RejectionBay;
	}

	public void setTableStatePlanner_RejectionBay(ArrayList<StateFlow> tableStatePlanner_RejectionBay) {
		this.tableStatePlanner_RejectionBay = tableStatePlanner_RejectionBay;
	}



}

