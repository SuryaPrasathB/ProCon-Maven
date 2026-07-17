package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Loading extends TimerTask{
	public static Logger logger = Logger.getLogger(Loading.class.getPackage().getName()); 
	private LoadingBayContext loadingBayStateManager = new LoadingBayContext();  
	
	public static boolean stopProcessRequestedLoadingBay = false ;
	public static boolean resetProcessRequestedLoadingBay = false ;
	
	public static boolean stopProcessCompletedLoadingBay = false ;
	public static boolean resetProcessCompletedLoadingBay = false ;
	
	public static boolean abort_Loading_Bay = false ;
	public void run() {
		Loading.logger.debug("LoadingBay2 : Entry"); 

		manageLoadingBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageLoadingBayStates() {

		Loading.logger.debug("LoadingBay2 : manageLoadingBayStates : Entry");

		//setTableStatePlanner_LoadingBay(StatePlannerController.getTableStatePlannerLoadingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.LOADING_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.LOADING_BAY_KEY);
		setTableStatePlanner_LoadingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_LoadingBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_LoadingBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			LoadingBayState currentState = createLoadingBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Loading.logger.debug("LoadingBay2 : manageLoadingBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_LoadingBay().size());
	
	
			while (!isStopProcessRequestedLoadingBay() &&
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
						LoadingBayState nextState = createLoadingBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_LoadingBay()) {
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
						if (nextStateName.equals("S04_error_Handling")) {
							LoadingBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							LoadingBayState nextState2 = createLoadingBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_LoadingBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			Loading.logger.debug("LoadingBay2 : manageLoadingBayStates : getTableStatePlanner2 : No states found in the planner");
		}
	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(LoadingBayState currentState){
		LoadingBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		LoadingBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		LoadingBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		LoadingBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(LoadingBayState newState) {
		//Set previous state here 

		// Set the new state
		loadingBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = loadingBayStateManager.processPresentState();

		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Loading.logger.debug("processCurrentState : " + loadingBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public LoadingBayState getPreviousState(){
		return loadingBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_LoadingBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_LoadingBay = new ArrayList<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_LoadingBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static LoadingBayState createLoadingBayStateInstance(String stateName) {
	        // Create and return an instance of the state class based on the state name
	        switch (stateName) {
	            case "S01_check_for_LoadingBay_pushButton_status":
	                return new S01_check_for_LoadingBay_pushButton_status();
	            case "S02_let_pallet_outside_loading_bay":
	                return new S02_let_pallet_outside_loading_bay();
	            case "S03_error_Handling":
	                return new S03_error_Handling(); 
	            case "S04_idle_condition":
	                return new S04_idle_condition(); 
	            case "S05_open_stop_latch_Loading_Bay":
	                return new S05_open_stop_latch_Loading_Bay();
	            case "S06_close_stop_latch_Loading_Bay":
	                return new S06_close_stop_latch_Loading_Bay();
	            default:
	                throw new IllegalArgumentException("Unknown state: " + stateName);
	        }
	    }

	private LoadingBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S22_error_Handling":
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

	public ArrayList<StateFlow> getTableStatePlanner_LoadingBay() {
		return tableStatePlanner_LoadingBay;
	}

	public void setTableStatePlanner_LoadingBay(ArrayList<StateFlow> tableStatePlanner_LoadingBay) {
		this.tableStatePlanner_LoadingBay = tableStatePlanner_LoadingBay;
	}

	public static boolean isStopProcessRequestedLoadingBay() {
		return stopProcessRequestedLoadingBay;
	}

	public static boolean isStopProcescccsCompletedLoadingBay() {
		return stopProcessCompletedLoadingBay;
	}

	public static boolean isResetProcessCompletedLoadingBay() {
		return resetProcessCompletedLoadingBay;
	}

	public static void setStopProcessRequestedLoadingBay(boolean stopProcessRequestedLoadingBay) {
		Loading.stopProcessRequestedLoadingBay = stopProcessRequestedLoadingBay;
	}

	public static void setStopProcessCompletedLoadingBay(boolean stopProcessCompletedLoadingBay) {
		Loading.stopProcessCompletedLoadingBay = stopProcessCompletedLoadingBay;
	}

	public static void setResetProcessCompletedLoadingBay(boolean resetProcessCompletedLoadingBay) {
		Loading.resetProcessCompletedLoadingBay = resetProcessCompletedLoadingBay;
	}

	public static boolean isResetProcessRequestedLoadingBay() {
		return resetProcessRequestedLoadingBay;
	}

	public static void setResetProcessRequestedLoadingBay(boolean resetProcessRequestedLoadingBay) {
		Loading.resetProcessRequestedLoadingBay = resetProcessRequestedLoadingBay;
	}



}
