package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.ArrayList;
import java.util.TimerTask;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class LoadingBayStop extends TimerTask{
	//public static Logger logger = Logger.getLogger(LoadingBay.class.getPackage().getName()); 
	private LoadingBayContext loadingBayStopStateManager = new LoadingBayContext();  
	public static boolean abort_Loading_Bay = false ;
	public void run() {
		Loading.logger.debug("LoadingBay2 : Entry"); 

		manageLoadingBayStopStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageLoadingBayStopStates() {

		Loading.logger.debug("LoadingBayStop : manageLoadingBayStopStates : Entry");

		//setTableStatePlanner_LoadingBay(StatePlannerController.getTableStatePlannerLoadingBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.LOADING_BAY_KEY, "STOP");//findByBayKey(ConstantConveyor.LOADING_BAY_KEY);
		setTableStatePlanner_LoadingBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		if(getTableStatePlanner_LoadingBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_LoadingBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			LoadingBayState currentState = LoadingBayState.createState(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Loading.logger.debug("LoadingBayStop : manageLoadingBayStopStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_LoadingBay().size());
	
	
			while (!Loading.isStopProcescccsCompletedLoadingBay() &&
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
						LoadingBayState nextState = LoadingBayState.createState(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					for (StateFlow row : getTableStatePlanner_LoadingBay()) {
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
					Loading loading = new Loading();
					String nextStateName = loading.getErrorStateInstanceString(errorCode);//createStateInstance("S22_error_Handling");
	
					presentRow.setIfFailed(nextStateName);
	
					//=====
	
					// If failed, fetch the next state from the table (columnFailure)
					nextStateName = presentRow.getIfFailed();
	
					if (nextStateName != null && !nextStateName.isEmpty()) {
						if (nextStateName.equals("S03_error_Handling")) {
							LoadingBayState nextState2 = LoadingBayState.createErrorState(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							LoadingBayState nextState2 = LoadingBayState.createState(nextStateName);
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
			Loading.logger.debug("LoadingBayStop : manageLoadingBayStopStates : getTableStatePlanner2  : No states found in the planner");
		}
		
		Loading.logger.debug("LoadingBayStop : manageLoadingBayStopStates : Exit");

	}
	
	public void setNextState(LoadingBayState newState) {
		//Set previous state here 

		// Set the new state
		loadingBayStopStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = loadingBayStopStateManager.processPresentState();

		Loading.logger.debug("processCurrentState : " + loadingBayStopStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Loading.logger.debug("processCurrentState : " + loadingBayStopStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public LoadingBayState getPreviousState(){
		return loadingBayStopStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_LoadingBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_LoadingBay = new ArrayList<StateFlow>();

	public ArrayList<StateFlow> getTableStatePlanner_LoadingBay() {
		return tableStatePlanner_LoadingBay;
	}

	public void setTableStatePlanner_LoadingBay(ArrayList<StateFlow> tableStatePlanner_LoadingBay) {
		this.tableStatePlanner_LoadingBay = tableStatePlanner_LoadingBay;
	}



}
