package com.tasnetwork.calibration.conveyor.bay.ir;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;


public class InsulationResistanceTestBayReset extends TimerTask{

	//public static Logger logger = Logger.getLogger(InsulationResistanceTestBayReset.class.getPackage().getName());
	private IrtBayContext irtBayResetStateManager = new IrtBayContext();
	
	public void run() {
		Ir.logger.debug("InsulationResistanceTestBayReset : Entry"); 

		manageInsulationResistanceTestBayResetStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageInsulationResistanceTestBayResetStates() {

		Ir.logger.debug("InsulationResistanceTestBayReset : manageInsulationResistanceTestBayResetStates : Entry");

		//setTableStatePlanner_IrtBay(StatePlannerController.getTableStatePlannerIrtBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.IR_BAY_KEY, "RESET");//findByBayKey(ConstantConveyor.IR_BAY_KEY);
		setTableStatePlanner_IrtBay(statePlanner);

		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process
		if(getTableStatePlanner_IrtBay().size()>0) {

			// Fetch the first state from the table to start the process
			StateFlow presentRow = getTableStatePlanner_IrtBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			IrtBayState currentState = createIrtBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state
	
	
			Ir.logger.debug("InsulationResistanceTestBayReset : manageInsulationResistanceTestBayResetStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_IrtBay().size());
	
	
			while (!Ir.isResetProcessCompletedIrtBay() &&
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
						IrtBayState nextState = createIrtBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_IrtBay()) {
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
							IrtBayState nextState2 = createErrorStateInstance(nextStateName, errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							IrtBayState nextState2 = createIrtBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}
						
					}
	
	
					for (StateFlow row : getTableStatePlanner_IrtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}
	
					}
				}
	
			}
		}else {
			Ir.logger.debug("InsulationResistanceTestBayReset : manageInsulationResistanceTestBayResetStates : No states found in the planner");
		}
		Ir.logger.debug("InsulationResistanceTestBayReset : Exit"); 

	}

	//=====================================================================================================================
	
/*	public static void singleStateTestRun(IrtBayState currentState){
		InsulationResistanceTestBay.logger.debug("singleStateTestRun : Entry");
		
		setNextState(currentState);
		
		BayResponse bayStatus = processCurrentState();
		
		InsulationResistanceTestBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		InsulationResistanceTestBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		InsulationResistanceTestBay.logger.debug("singleStateTestRun : Exit");
	}*/
	
	//=====================================================================================================================
	
	public void setNextState(IrtBayState newState) {
		//Set previous state here 

		// Set the new state
		irtBayResetStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = irtBayResetStateManager.processPresentState();

		Ir.logger.debug("processCurrentState : " + irtBayResetStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ir.logger.debug("processCurrentState : " + irtBayResetStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public IrtBayState getPreviousState(){
		return irtBayResetStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_IrtBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_IrtBay = new ArrayList<StateFlow>();
	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_IrtBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
		public static IrtBayState createIrtBayStateInstance(String stateName) {
		    try {
		        // Get the fully qualified class name dynamically
		        String packageName = IrtBayState.class.getPackage().getName(); // Adjust if necessary
		        Class<?> c = Class.forName(packageName + "." + stateName);

		        // Ensure the class is a subclass of IrtBayState
		        if (!IrtBayState.class.isAssignableFrom(c)) {
		            throw new IllegalArgumentException("Invalid state class: " + stateName);
		        }

		        // Create an instance using the default constructor
		        return (IrtBayState) c.getDeclaredConstructor().newInstance();
		    } catch (ClassNotFoundException e) {
		        throw new IllegalArgumentException("Unknown state: " + stateName, e);
		    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
		        throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		    }
		}


	private IrtBayState createErrorStateInstance(String stateName, String errorCode) {  
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

	public ArrayList<StateFlow> getTableStatePlanner_IrtBay() {
		return tableStatePlanner_IrtBay;
	}

	public void setTableStatePlanner_IrtBay(ArrayList<StateFlow> tableStatePlanner_IrtBay) {
		this.tableStatePlanner_IrtBay = tableStatePlanner_IrtBay;
	}
}
