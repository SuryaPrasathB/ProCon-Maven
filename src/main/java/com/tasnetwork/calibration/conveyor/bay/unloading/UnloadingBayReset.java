package com.tasnetwork.calibration.conveyor.bay.unloading;


import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class UnloadingBayReset extends TimerTask{
	//public static Logger logger = Logger.getLogger(UnloadingBay.class.getPackage().getName()); 
	private UnloadingBayContext unloadingBayResetStateManager = new UnloadingBayContext();  
	public static boolean abort_Unloading_Bay = false ;
	public void run() {
		Unloading.logger.debug("UnloadingBay2 : Entry"); 

		manageUnloadingBayResetStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageUnloadingBayResetStates() {

		Unloading.logger.debug("UnloadingBayReset : manageUnloadingBayResetStates : Entry");

		//setTableStatePlanner_FtBay(StatePlannerController.getTableStatePlannerUnloadingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.UNLOADING_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.UNLOADING_BAY_KEY);
		setTableStatePlanner_UnloadingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if(getTableStatePlanner_UnloadingBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_UnloadingBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			UnloadingBayState currentState = UnloadingBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Unloading.logger.debug("UnloadingBayReset : manageUnloadingBayResetStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_UnloadingBay().size());
	
	
			while (!Unloading.isResetProcessCompletedUnloadingBay() &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();
	
				presentRow = nextRow ;
	
				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)
	
					String nextStateName = presentRow.getIfSuccess();
	
					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						// Set the next state based on the success column
						UnloadingBayState nextState = UnloadingBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_UnloadingBay()) {
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
	
					if (nextStateName != null && !nextStateName.isEmpty() && !nextStateName.equals("Select State")) {
						if (nextStateName.equals("S10_error_Handling")) {
							UnloadingBayState nextState2 = UnloadingBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							UnloadingBayState nextState2 = UnloadingBayState.createState(nextStateName);
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
			Unloading.logger.debug("UnloadingBayReset : manageUnloadingBayResetStates : getTableStatePlanner2  : No states found in the planner");
		}
		
		Unloading.logger.debug("UnloadingBayReset : manageUnloadingBayResetStates : Exit");

	}

	//=====================================================================================================================

	public void setNextState(UnloadingBayState newState) {
		//Set previous state here 

		// Set the new state
		unloadingBayResetStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = unloadingBayResetStateManager.processPresentState();

		Unloading.logger.debug("processCurrentState : " + unloadingBayResetStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Unloading.logger.debug("processCurrentState : " + unloadingBayResetStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public UnloadingBayState getPreviousState(){
		return unloadingBayResetStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_FtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_UnloadingBay = new ArrayList<StateFlow>();
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



}

