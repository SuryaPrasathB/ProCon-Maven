package com.tasnetwork.calibration.conveyor.bay.calib;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.StatePlannerController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Calib extends TimerTask{
	public static Logger logger = Logger.getLogger(Calib.class.getPackage().getName()); 
	private CalibrationBayContext calibBayStateManager = new CalibrationBayContext();  

	public static boolean startProcessRequestedCalibBay = false ;
	public static boolean stopProcessRequestedCalibBay = false ;
	public static boolean resetProcessRequestedCalibBay = false ;

	public static boolean startProcessCompletedCalibBay = false ;
	public static boolean stopProcessCompletedCalibBay = false ;
	public static boolean resetProcessCompletedCalibBay = false ;

	public static boolean abort_Calib_Bay = false ;
	public void run() {
		Calib.logger.debug("Calib : Entry"); 

		manageCalibrationBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageCalibrationBayStates() {

		Calib.logger.debug("Calib : manageCalibrationBayStates : Entry");

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.CALIBRATION_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		setTableStatePlanner_CalibBay(statePlanner);//StatePlannerController.getTableStatePlannerCalibBay_UI());


		// Set the first state from the table outside the while loops
		int currentIndex = 0; // Start from the first row
		boolean abortFlag = false; // Abort flag to stop the process

		// Fetch the first state from the table to start the process
		if(getTableStatePlanner_CalibBay().size()>0) {
			StateFlow presentRow = getTableStatePlanner_CalibBay().get(currentIndex);
			StateFlow nextRow = presentRow ; 
			String currentStateName = presentRow.getState(); // Get the current state from the row
			CalibrationBayState currentState = createCalibBayStateInstance(currentStateName); // Create the state instance
			setNextState(currentState); // Set the first state


			Calib.logger.debug("Calib : manageCalibrationBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_CalibBay().size());

			setStartProcessCompletedCalibBay(true);

			while (!isStopProcessRequestedCalibBay() &&
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
						CalibrationBayState nextState = createCalibBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration

					for (StateFlow row : getTableStatePlanner_CalibBay()) {
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
							CalibrationBayState nextState2 =  createErrorStateInstance(nextStateName,errorCode);
							setNextState(nextState2); // Set the next state dynamically
						} else {
							CalibrationBayState nextState2 = createCalibBayStateInstance(nextStateName);
							setNextState(nextState2); // Set the next state dynamically
						}

					}


					for (StateFlow row : getTableStatePlanner_CalibBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row;                         // Set the next row based on the matched state
							break; // Exit the loop once the next state is found
						}

					}
				}

			}
		}else {
			Calib.logger.debug("Calib : manageCalibrationBayStates : getTableStatePlanner : No states found in the planner");
		}
	}

	//=====================================================================================================================

	/*	public static void singleStateTestRun(CalibrationBayState currentState){
		CalibrationBay.logger.debug("singleStateTestRun : Entry");

		setNextState(currentState);

		BayResponse bayStatus = processCurrentState();

		CalibrationBay.logger.debug("singleStateTestRun : bayStatus : Status : " + bayStatus.getStatus());
		CalibrationBay.logger.debug("singleStateTestRun : bayStatus : Error Code : " + bayStatus.getErrorCode());
		CalibrationBay.logger.debug("singleStateTestRun : Exit");
	}*/

	//=====================================================================================================================

	public void setNextState(CalibrationBayState newState) {
		//Set previous state here 

		// Set the new state
		calibBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = calibBayStateManager.processPresentState();

		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Calib.logger.debug("processCurrentState : " + calibBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public CalibrationBayState getPreviousState(){
		return calibBayStateManager.getLastProcessedBayState();
	}

	//public TableView<StateFlow> tableStatePlanner_CalibBay = new TableView<StateFlow>();
	public ArrayList<StateFlow> tableStatePlanner_CalibBay = new ArrayList<StateFlow>();

	// Helper method to find the row by state name
	private StateFlow findRowByStateName(String stateName) {

		for (StateFlow row : getTableStatePlanner_CalibBay()) {
			if (row.getState().equals(stateName)) {
				return row;
			}
		}
		return null;
	}

	// Helper method to create a state instance dynamically based on the state name
	public static CalibrationBayState createCalibBayStateInstance(String stateName) {
		try {
			// Get the fully qualified class name dynamically
			String packageName = CalibrationBayState.class.getPackage().getName(); // Adjust if necessary
			Class<?> c = Class.forName(packageName + "." + stateName);

			// Ensure the class is a subclass of CalibrationBayState
			if (!CalibrationBayState.class.isAssignableFrom(c)) {
				throw new IllegalArgumentException("Invalid state class: " + stateName);
			}

			// Create an instance using the default constructor
			return (CalibrationBayState) c.getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown state: " + stateName, e);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalArgumentException("Error instantiating state: " + stateName, e);
		}
	}

	private CalibrationBayState createErrorStateInstance(String stateName, String errorCode) {  
		// Create and return an instance of the state class based on the state name
		switch (stateName) {
		case "S10_error_Handling":
			return new  S10_error_Handling(errorCode);
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

	public ArrayList<StateFlow> getTableStatePlanner_CalibBay() {
		return tableStatePlanner_CalibBay;
	}

	public void setTableStatePlanner_CalibBay(ArrayList<StateFlow> tableStatePlanner_CalibBay) {
		this.tableStatePlanner_CalibBay = tableStatePlanner_CalibBay;
	}

	public static boolean isStopProcessRequestedCalibBay() {
		return stopProcessRequestedCalibBay;
	}

	public static boolean isStopProcessCompletedCalibBay() {
		return stopProcessCompletedCalibBay;
	}

	public static boolean isResetProcessCompletedCalibBay() {
		return resetProcessCompletedCalibBay;
	}

	public static void setStopProcessRequestedCalibBay(boolean stopProcessRequestedCalibBay) {
		Calib.stopProcessRequestedCalibBay = stopProcessRequestedCalibBay;
	}

	public static void setStopProcessCompletedCalibBay(boolean stopProcessCompletedCalibBay) {
		Calib.stopProcessCompletedCalibBay = stopProcessCompletedCalibBay;
	}

	public static void setResetProcessCompletedCalibBay(boolean resetProcessCompletedCalibBay) {
		Calib.resetProcessCompletedCalibBay = resetProcessCompletedCalibBay;
	}

	public static boolean isResetProcessRequestedCalibBay() {
		return resetProcessRequestedCalibBay;
	}

	public static void setResetProcessRequestedCalibBay(boolean resetProcessRequestedCalibBay) {
		Calib.resetProcessRequestedCalibBay = resetProcessRequestedCalibBay;
	}

	public static boolean isStartProcessRequestedCalibBay() {
		return startProcessRequestedCalibBay;
	}

	public static void setStartProcessRequestedCalibBay(boolean startProcessRequestedCalibBay) {
		Calib.startProcessRequestedCalibBay = startProcessRequestedCalibBay;
	}

	public static boolean isStartProcessCompletedCalibBay() {
		return startProcessCompletedCalibBay;
	}

	public static void setStartProcessCompletedCalibBay(boolean startProcessCompletedCalibBay) {
		Calib.startProcessCompletedCalibBay = startProcessCompletedCalibBay;
	}



}

