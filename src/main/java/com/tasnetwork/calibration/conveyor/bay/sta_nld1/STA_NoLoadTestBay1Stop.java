package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;


public class STA_NoLoadTestBay1Stop extends TimerTask{

	private STA_NoLoadTestBay1Context sctNltBay1StopStateManager = new STA_NoLoadTestBay1Context();  
	public static boolean abort_SCT_NLT_Bay1 = false ;


	public void run() {
		StaNld_Bay1.logger.debug("ShortCircuit_NoLoadTestBay12 : Entry"); 

		manageShortCircuit_NoLoadTestBay1StopStates();

	}

	private void manageShortCircuit_NoLoadTestBay1StopStates() {

		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Stop : manageShortCircuit_NoLoadTestBay1StopStates : Entry");


		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.STA_NLD1_BAY_KEY, "STOP");//findByBayKey(ConstantConveyor.STA_NLD1_BAY_KEY);
		setTableStatePlanner_StaNldTestBay1(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row

		if(getTableStatePlanner_StaNldTestBay1().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_StaNldTestBay1().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			STA_NoLoadTestBay1State currentState = STA_NoLoadTestBay1State.createSctNltBay1StateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Stop : manageShortCircuit_NoLoadTestBay1StopStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_StaNldTestBay1().size());
	
	
			while (!StaNld_Bay1.isStopProcessCompletedStaNldBay1() &&
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
						STA_NoLoadTestBay1State nextState = STA_NoLoadTestBay1State.createSctNltBay1StateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}

					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_StaNldTestBay1()) {
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
							STA_NoLoadTestBay1State nextState2 =  STA_NoLoadTestBay1State.createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else if (nextStateName.equals("S13_error_Handling_Bay1")) {
							STA_NoLoadTestBay1State nextState2 =  STA_NoLoadTestBay1State.createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							STA_NoLoadTestBay1State nextState2 = STA_NoLoadTestBay1State.createSctNltBay1StateInstance(nextStateName);
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
			StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Stop : manageShortCircuit_NoLoadTestBay1StopStates : : No states found in the planner");
		}
		StaNld_Bay1.logger.debug("STA_NoLoadTestBay1Stop : Exit"); 

	}

	//=====================================================================================================================



	//=====================================================================================================================

	public void setNextState(STA_NoLoadTestBay1State newState) {
		//Set previous state here 

		// Set the new state
		sctNltBay1StopStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = sctNltBay1StopStateManager.processPresentState();

		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StopStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		StaNld_Bay1.logger.debug("processCurrentState : " + sctNltBay1StopStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public STA_NoLoadTestBay1State getPreviousState(){
		return sctNltBay1StopStateManager.getLastProcessedBayState();
	}


	public ArrayList<StateFlow> tableStatePlanner_StaNldTestBay1 = new ArrayList<StateFlow>();

	private String getErrorStateInstance(String errorCode) {

		switch (errorCode) {
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

