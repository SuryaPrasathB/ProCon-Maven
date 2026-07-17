package com.tasnetwork.calibration.conveyor.bay.comm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
//import com.tasnetwork.calibration.conveyor.bay_verificationtest.VerificTestBayState;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Comm extends TimerTask{
	public static Logger logger = Logger.getLogger(Comm.class.getPackage().getName()); 
	private CommTestBayContext commTestBayManager = new CommTestBayContext();  

	public static boolean startProcessRequestedCommBay = false ;
	public static boolean stopProcessRequestedCommBay = false ;
	public static boolean resetProcessRequestedCommBay = false ;

	public static boolean startProcessCompletedCommBay = false ;
	public static boolean stopProcessCompletedCommBay = false ;
	public static boolean resetProcessCompletedCommBay = false ;

	public static boolean abort_CommTest_Bay = false ;

	public void run() {
		Comm.logger.debug("CommunicationTestBay2 : Entry"); 

		manageCommunicationTestBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageCommunicationTestBayStates() {

		Comm.logger.debug("CommunicationTestBay2 : manageCommunicationTestBayStates : Entry");

		//setTableStatePlanner_CommBay(StatePlannerController.getTableStatePlannerCommBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.COMMUNICATION_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.COMMUNICATION_BAY_KEY);

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

			Comm.logger.debug("CommunicationTestBay2 : manageCommunicationTestBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_CommBay().size());

			setStartProcessCompletedCommBay(true);

			while (!isStopProcessRequestedCommBay()&&
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
							CommTestBayState nextState2 =  createErrorStateInstance(nextStateName,errorCode);
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
			Comm.logger.debug("CommunicationTestBay2 : manageCommunicationTestBayStates : No states found in the planner");
		}
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
		commTestBayManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = commTestBayManager.processPresentState();

		Comm.logger.debug("processCurrentState : " + commTestBayManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Comm.logger.debug("processCurrentState : " + commTestBayManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CommTestBayState getPreviousState(){
		return commTestBayManager.getLastProcessedBayState();
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
		try {
			// Get the fully qualified class name dynamically
			String packageName = CommTestBayState.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of VerificTestBayState
			if (!CommTestBayState.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (CommTestBayState) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
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

	public static boolean isStopProcessRequestedCommBay() {
		return stopProcessRequestedCommBay;
	}

	public static boolean isStopProcessCompletedCommBay() {
		return stopProcessCompletedCommBay;
	}

	public static boolean isResetProcessCompletedCommBay() {
		return resetProcessCompletedCommBay;
	}

	public static void setStopProcessRequestedCommBay(boolean stopProcessRequestedCommBay) {
		Comm.stopProcessRequestedCommBay = stopProcessRequestedCommBay;
	}

	public static void setStopProcessCompletedCommBay(boolean stopProcessCompletedCommBay) {
		Comm.stopProcessCompletedCommBay = stopProcessCompletedCommBay;
	}

	public static void setResetProcessCompletedCommBay(boolean resetProcessCompletedCommBay) {
		Comm.resetProcessCompletedCommBay = resetProcessCompletedCommBay;
	}

	public static boolean isResetProcessRequestedCommBay() {
		return resetProcessRequestedCommBay;
	}

	public static void setResetProcessRequestedCommBay(boolean resetProcessRequestedCommBay) {
		Comm.resetProcessRequestedCommBay = resetProcessRequestedCommBay;
	}

	public static boolean isStartProcessRequestedCommBay() {
		return startProcessRequestedCommBay;
	}

	public static void setStartProcessRequestedCommBay(boolean startProcessRequestedCommBay) {
		Comm.startProcessRequestedCommBay = startProcessRequestedCommBay;
	}

	public static boolean isStartProcessCompletedCommBay() {
		return startProcessCompletedCommBay;
	}

	public static void setStartProcessCompletedCommBay(boolean startProcessCompletedCommBay) {
		Comm.startProcessCompletedCommBay = startProcessCompletedCommBay;
	}



}

