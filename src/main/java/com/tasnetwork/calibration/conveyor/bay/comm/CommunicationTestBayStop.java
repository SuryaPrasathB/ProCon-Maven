package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class CommunicationTestBayStop extends TimerTask{
	//public static Logger logger = Logger.getLogger(CommunicationTestBay.class.getPackage().getName()); 
	private CommTestBayContext commTestBayStopManager = new CommTestBayContext();  
	
	public static boolean abort_CommTest_Bay = false ;
	public void run() {
		Comm.logger.debug("CommunicationTestBayStop : Entry"); 

		manageCommunicationTestBayStopStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageCommunicationTestBayStopStates() {

		Comm.logger.debug("CommunicationTestBayStop : manageCommunicationTestBayStopStates : Entry");

		//setTableStatePlanner_CommBay(StatePlannerController.getTableStatePlannerCommBay_UI());
		
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.COMMUNICATION_BAY_KEY, "STOP");//findByBayKey(ConstantConveyor.COMMUNICATION_BAY_KEY);
		setTableStatePlanner_CommBay(statePlanner);
		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_CommBay().size()>0) {
			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_CommBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			CommTestBayState currentState = createCommBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
			Comm.logger.debug("CommunicationTestBayStop : manageCommunicationTestBayStopStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_CommBay().size());
	
			while (!Comm.isStopProcessCompletedCommBay()&&
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
						CommTestBayState nextState = createCommBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_CommBay()) {
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
							CommTestBayState nextState2 =  createErrorStateInstance(nextStateName ,errorCode );
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CommTestBayState nextState2 = createCommBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_CommBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
		}else {
			Comm.logger.debug("CommunicationTestBayStop : manageCommunicationTestBayStopStates : No states found in the planner");
		}
		
		Comm.logger.debug("CommunicationTestBayStop : manageCommunicationTestBayStopStates : Exit");

	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(CommTestBayState currentState){
		CommunicationTestBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		CommunicationTestBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		CommunicationTestBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		CommunicationTestBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(CommTestBayState newState) {
		//Set previous state here 

		// Set the new state
		commTestBayStopManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = commTestBayStopManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + commTestBayStopManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + commTestBayStopManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CommTestBayState getPreviousState(){
		return commTestBayStopManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_CommBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CommBay = new ArrayList<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_CommBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static CommTestBayState createCommBayStateInstance(String stateName) {           
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_pallet_at_Comm_Bay":
	                return new S01_check_for_pallet_at_Comm_Bay();
	            case "S02_qR_Code_Scanning_of_Pallet":
	                return new S02_qR_Code_Scanning_of_Pallet();
	            case "S03_close_the_fingerTip_Latch":
	                return new S03_close_the_fingerTip_Latch();
	            case "S04_ensure_the_fingerTip_Latch_Closed":
	                return new S04_ensure_the_fingerTip_Latch_Closed();
	            case "S05_communication_Test":
	                return new S05_communication_Test();
	            case "S06_open_the_fingerTip_Latch":
	                return new S06_open_the_fingerTip_Latch();
	            case "S07_ensure_the_fingerTip_Latch_Opened":
	                return new S07_ensure_the_fingerTip_Latch_Opened();
	            case "S08_check_for_pallet_at_Unloading_Bay":
	                return new S08_check_for_pallet_at_Unloading_Bay();
	            case "S09_let_the_pallet_to_Unloading_Bay":
	                return new S09_let_the_pallet_to_Unloading_Bay();
	            case "S10_error_Handling":
	                return new S10_error_Handling();
	            case "S11_idle_condition":
	                return new S11_idle_condition(); 
	            case "S12_open_stop_latch_COMM_Bay":
	                return new S12_open_stop_latch_COMM_Bay();
	            case "S13_close_stop_latch_COMM_Bay":
	                return new S13_close_stop_latch_COMM_Bay();
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private CommTestBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
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

	public ArrayList<StateFlow> getTableStatePlanner_CommBay() {
		return tableStatePlanner_CommBay;
	}

	public void setTableStatePlanner_CommBay(ArrayList<StateFlow> tableStatePlanner_CommBay) {
		this.tableStatePlanner_CommBay = tableStatePlanner_CommBay;
	}

 



}

