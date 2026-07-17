package com.tasnetwork.calibration.conveyor.bay.hv;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.scene.control.TableView;

public class Hv extends TimerTask{
	public static Logger logger = Logger.getLogger(Hv.class.getPackage().getName()); 
	private HvtBayContext hvtBayStateManager = new HvtBayContext();

	public static boolean startProcessRequestedHvtBay = false ;
	public static boolean stopProcessRequestedHvtBay = false ;
	public static boolean resetProcessRequestedHvtBay = false ;

	public static boolean startProcessCompletedHvtBay = false ;
	public static boolean stopProcessCompletedHvtBay = false ;
	public static boolean resetProcessCompletedHvtBay = false ;

	public static boolean abort_HVT_Bay = false ;

	public void run() {
		Hv.logger.debug("HighVoltageTestBay2 : Entry"); 

		manageHighVoltageTestBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageHighVoltageTestBayStates() {

		Hv.logger.debug("HighVoltageTestBay2 : manageHighVoltageTestBayStates : Entry");




		//setTableStatePlanner_HvtBay(StatePlannerController.getTableStatePlannerHvtBay_UI());

		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.HV_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.HV_BAY_KEY);
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
	
	
			Hv.logger.debug("HighVoltageTestBay2 : manageHighVoltageTestBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_HvtBay().size());
	
			setStartProcessCompletedHvtBay(true);
			
			while (!isStopProcessRequestedHvtBay() &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
	
				Hv.logger.info("StopProcessRequestedHvtBay :" + isStopProcessRequestedHvtBay());
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
			Hv.logger.debug("HighVoltageTestBay2 : manageHighVoltageTestBayStates : getTableStatePlanner2 : No states found in the planner");
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
		hvtBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = hvtBayStateManager.processPresentState();

		Hv.logger.debug("processCurrentState : " + hvtBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Hv.logger.debug("processCurrentState : " + hvtBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public HvtBayState getPreviousState(){
		return hvtBayStateManager.getLastProcessedBayState();
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
	public HvtBayState createHvtBayStateInstance(String stateName) {
		if (stateName.startsWith("ERROR")) {
			getErrorStateInstance(stateName);
			return new S11_idle_condition();
		}
		else{
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
	}



	private HvtBayState createErrorStateInstance(String stateName, String errorCode) {  
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
		case  ConvErrorCodeMapping.ERROR_CODE_HVT_010 :
			return "S051_stop_HV_source";

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

	public static boolean isStopProcessRequestedHvtBay() {
		return stopProcessRequestedHvtBay;
	}

	public static boolean isStopProcessCompletedHvtBay() {
		return stopProcessCompletedHvtBay;
	}

	public static boolean isResetProcessCompletedHvtBay() {
		return resetProcessCompletedHvtBay;
	}

	public static void setStopProcessRequestedHvtBay(boolean stopProcessRequestedHvtBay) {
		Hv.stopProcessRequestedHvtBay = stopProcessRequestedHvtBay;
	}

	public static void setStopProcessCompletedHvtBay(boolean stopProcessCompletedHvtBay) {
		Hv.stopProcessCompletedHvtBay = stopProcessCompletedHvtBay;
	}

	public static void setResetProcessCompletedHvtBay(boolean resetProcessCompletedHvtBay) {
		Hv.resetProcessCompletedHvtBay = resetProcessCompletedHvtBay;
	}

	public static boolean isResetProcessRequestedHvtBay() {
		return resetProcessRequestedHvtBay;
	}

	public static void setResetProcessRequestedHvtBay(boolean resetProcessRequestedHvtBay) {
		Hv.resetProcessRequestedHvtBay = resetProcessRequestedHvtBay;
	}

	public static boolean isStartProcessRequestedHvtBay() {
		return startProcessRequestedHvtBay;
	}

	public static void setStartProcessRequestedHvtBay(boolean startProcessRequestedHvtBay) {
		Hv.startProcessRequestedHvtBay = startProcessRequestedHvtBay;
	}

	public static boolean isStartProcessCompletedHvtBay() {
		return startProcessCompletedHvtBay;
	}

	public static void setStartProcessCompletedHvtBay(boolean startProcessCompletedHvtBay) {
		Hv.startProcessCompletedHvtBay = startProcessCompletedHvtBay;
	}





}
