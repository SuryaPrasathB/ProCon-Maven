package com.tasnetwork.calibration.conveyor.bay.ir;

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

public class Ir extends TimerTask{
	public static Logger logger = Logger.getLogger(Ir.class.getPackage().getName()); 
	private IrtBayContext IrtBayStateManager = new IrtBayContext();
	
	public static boolean StartProcessRequestedIrtBay = false ;
	public static boolean stopProcessRequestedIrtBay = false ;
	public static boolean resetProcessRequestedIrtBay = false ;
	
	public static boolean startProcessCompletedIrtBay = false ;
	public static boolean stopProcessCompletedIrtBay = false ;
	public static boolean resetProcessCompletedIrtBay = false ;
	
	public static boolean abort_IRT_Bay = false ;
	public void run() {
		Ir.logger.debug("InsulationResistanceTestBay : Entry"); 

		manageInsulationResistanceTestBayStates();
		//DevSysEnergyMeter.sendReadNeutralCurrentCommand();
	}

	private void manageInsulationResistanceTestBayStates() {

		Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Entry");

		//setTableStatePlanner_IrtBay(StatePlannerController.getTableStatePlannerIrtBay_UI());
		ArrayList<StateFlow> statePlanner = (ArrayList<StateFlow>) MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(ConstantConveyor.IR_BAY_KEY, "RUN");//findByBayKey(ConstantConveyor.IR_BAY_KEY);
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
	
	
			Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : getTableStatePlanner2 : Size : " + getTableStatePlanner_IrtBay().size());
	
			setStartProcessCompletedIrtBay(true);
	
			while (!isStopProcessRequestedIrtBay() &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				// Process the current state
				BayResponse bayStatus = processCurrentState();
	
				presentRow = nextRow ;
	
				// Check if the status is success
				if (bayStatus.isStatus()) {
					// If successful, fetch the next state from the table (columnSuccess)
	
					String nextStateName = presentRow.getIfSuccess();
	
					Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Next State : " +  nextStateName );
				
					if (nextStateName != null && !nextStateName.isEmpty()) {
						Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Next State2 : " +  nextStateName );
	
						// Set the next state based on the success column
						IrtBayState nextState = createIrtBayStateInstance(nextStateName);
						setNextState(nextState); // Set the next state dynamically
					}else{
						Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Next State : null or empty "  );
	
					}
					boolean stateFound = false;
					// Re-fetch the current row for the next iteration
	
					for (StateFlow row : getTableStatePlanner_IrtBay()) {
						if (row.getState().equals(nextStateName)) { // Assuming 'getState()' fetches the columnState
							nextRow = row; // Set the next row based on the matched state
							Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Next State found  "  );
	
							// currentIndex = presentRow.;
							stateFound = true;
							break; // Exit the loop once the next state is found
						}else {
							Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : Next State NOT found  "  );
	
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
							IrtBayState nextState2 =  createErrorStateInstance(nextStateName, errorCode);
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
			Ir.logger.debug("InsulationResistanceTestBay : manageInsulationResistanceTestBayStates : getTableStatePlanner2 : No states found in the planner");
		}
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
		IrtBayStateManager.setState(newState);  


	}

	public BayResponse processCurrentState(){
		// Process the current state
		BayResponse bayStatus = IrtBayStateManager.processPresentState();

		Ir.logger.debug("processCurrentState : " + IrtBayStateManager.getState().getClass().getSimpleName() + " : Status     : " + bayStatus.isStatus());
		Ir.logger.debug("processCurrentState : " + IrtBayStateManager.getState().getClass().getSimpleName() + " : Error Code : " + bayStatus.getErrorCode());
		return bayStatus ;
	}

	public IrtBayState getPreviousState(){
		return IrtBayStateManager.getLastProcessedBayState();
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
		case  ConvErrorCodeMapping.ERROR_CODE_IRT_010 :
			return "S06_open_the_fingerTip_Latch";
			
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

	public static boolean isStopProcessRequestedIrtBay() {
		return stopProcessRequestedIrtBay;
	}

	public static boolean isStopProcessCompletedIrtBay() {
		return stopProcessCompletedIrtBay;
	}

	public static boolean isResetProcessCompletedIrtBay() {
		return resetProcessCompletedIrtBay;
	}

	public static void setStopProcessRequestedIrtBay(boolean stopProcessRequestedIrtBay) {
		Ir.stopProcessRequestedIrtBay = stopProcessRequestedIrtBay;
	}

	public static void setStopProcessCompletedIrtBay(boolean stopProcessCompletedIrtBay) {
		Ir.stopProcessCompletedIrtBay = stopProcessCompletedIrtBay;
	}

	public static void setResetProcessCompletedIrtBay(boolean resetProcessCompletedIrtBay) {
		Ir.resetProcessCompletedIrtBay = resetProcessCompletedIrtBay;
	}

	public static boolean isResetProcessRequestedIrtBay() {
		return resetProcessRequestedIrtBay;
	}

	public static void setResetProcessRequestedIrtBay(boolean resetProcessRequestedIrtBay) {
		Ir.resetProcessRequestedIrtBay = resetProcessRequestedIrtBay;
	}

	public static boolean isStartProcessRequestedIrtBay() {
		return StartProcessRequestedIrtBay;
	}

	public static void setStartProcessRequestedIrtBay(boolean startProcessRequestedIrtBay) {
		StartProcessRequestedIrtBay = startProcessRequestedIrtBay;
	}

	public static boolean isStartProcessCompletedIrtBay() {
		return startProcessCompletedIrtBay;
	}

	public static void setStartProcessCompletedIrtBay(boolean startProcessCompletedIrtBay) {
		Ir.startProcessCompletedIrtBay = startProcessCompletedIrtBay;
	}



}

