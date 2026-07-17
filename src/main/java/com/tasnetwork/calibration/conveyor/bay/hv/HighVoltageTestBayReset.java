package com.tasnetwork.calibration.conveyor.bay.hv;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

public class HighVoltageTestBayReset extends TimerTask{

	//public static Logger logger = Logger.getLogger(HighVoltageTestBay.class.getPackage().getName()); 
	private HvtBayContext hvtBayStateResetManager = new HvtBayContext();  
	public static boolean abort_HVT_Bay = false ;
	
	public void run() {
		Hv.logger.debug("HighVoltageTestBayReset : Entry"); 

		manageHighVoltageTestBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageHighVoltageTestBayStates() {

		Hv.logger.debug("HighVoltageTestBayReset : manageHighVoltageTestBayStates : Entry");

		//setTableStatePlanner_HvtBay(StatePlannerController.getTableStatePlannerHvtBay_UI());
		
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.HV_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.HV_BAY_KEY);
		setTableStatePlanner_HvtBay(statePlanner);//StatePlannerController.getTableStatePlannerCalibBay_UI());

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_HvtBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_HvtBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			HvtBayState currentState = createHvtBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Hv.logger.debug("HighVoltageTestBayReset : manageHighVoltageTestBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_HvtBay().size());
	
	
			while (!Hv.isResetProcessCompletedHvtBay() &&
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
						HvtBayState nextState = createHvtBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_HvtBay()) {
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
							HvtBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							HvtBayState nextState2 = createHvtBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_HvtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
			
		}else {
			Hv.logger.debug("HighVoltageTestBayReset : manageHighVoltageTestBayStates : getTableStatePlanner2 : No states found in the planner");
		}
	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(HvtBayState currentState){
		HighVoltageTestBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		HighVoltageTestBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		HighVoltageTestBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		HighVoltageTestBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(HvtBayState newState) {
		//Set previous state here 

		// Set the new state
		hvtBayStateResetManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = hvtBayStateResetManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + hvtBayStateResetManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + hvtBayStateResetManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public HvtBayState getPreviousState(){
		return hvtBayStateResetManager.getLastProcessedBayState();
	}

//================================================================
	//public TableView<StateFlow> tableStatePlanner_HvtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_HvtBay = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_HvtBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
		public static HvtBayState createHvtBayStateInstance(String stateName) {
		    try {
		        // Get the fully qualified class name dynamically
		        String packageName = HvtBayState.class.getPackage().getName(); // Adjust if necessary
		        Class<?> c = Class.forName(packageName + "." + stateName);

		        // Ensure the class is a subclass of HvtBayState
		        if (!HvtBayState.class.isAssignableFrom(c)) {
		            throw new IllegalArgumentException("Invalid state class: " + stateName);
		        }

		        // Create an instance using the default constructor
		        return (HvtBayState) c.getDeclaredConstructor().newInstance();
		    } catch (ClassNotFoundException e) {
		        throw new IllegalArgumentException("Unknown state: " + stateName, e);
		    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
		        throw new IllegalArgumentException("Error instantiating state: " + stateName);
		    }
		}


	private HvtBayState createErrorStateInstance(String stateName, String errorCode) {  
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

	public ArrayList<StateFlow> getTableStatePlanner_HvtBay() {
		return tableStatePlanner_HvtBay;
	}

	public void setTableStatePlanner_HvtBay(ArrayList<StateFlow> tableStatePlanner_HvtBay) {
		this.tableStatePlanner_HvtBay = tableStatePlanner_HvtBay;
	}

}
