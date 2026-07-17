package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.CalibrationSummaryProcessor;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S059_09_Stop_Execution implements CalibrationBayState {

    private final CalibrationSummaryProcessor calibrationSummaryProcessor = CalibrationSummaryProcessor.getInstance();

	static boolean stopExecutionRequested = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	String resultStatus = "";
	String resultValue = "";
	String testType = "";
	String testCaseName = "";

	private String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY;

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {

		Calib.logger.info("S059_09_Stop_Execution : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = voltage_current_stop();

		boolean voltage_current_stop_initiated = (boolean) responseReturn.get("status");

		if (voltage_current_stop_initiated) {
			Calib.logger.info("S059_09_Stop_Execution : Voltage Current Stop Command Initiated");

			//Calib.logger.info("S059_04_Current_Stop : 10secs Wait Time");
			//BayUtils.delay(10000); // Delay after sending stop command
			if(ConstantConveyor.CALIB_SANGYONG_SOURCE_CONNECTED){
				Calib.logger.info("S059_09_Stop_Execution : 15 secs Wait Time");
				BayUtils.delay(5000);//10000); // Delay for 10 seconds as per PLC TIMER
				BayUtils.delay(5000);
				BayUtils.delay(5000);
			}else{
				Calib.logger.info("S059_09_Stop_Execution : 10secs Wait Time");
				BayUtils.delay(10000); // Delay for 10 seconds as per PLC TIMER
			}

			// --- Validate Voltage Stop Status ---
			int retry_count = 5;
			boolean voltageStopped = false;
			for (int i = 0; i < retry_count; i++) {
				responseReturn = checkVoltageStopStatus();
				voltageStopped = (boolean) responseReturn.get("status");

				if (voltageStopped) {
					break;
				}
				BayUtils.delay(2000);
				Calib.logger.info("S059_09_Stop_Execution : Exit (Voltage Stop Check) Retrying : " + i);
			}

			if (!voltageStopped) {
				Calib.logger.info("S059_09_Stop_Execution : Voltage Stop Verification Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Or a more specific error code
				Calib.logger.info("S059_09_Stop_Execution : Exit (Voltage Stop Verification Failed)");
				return bayResponse; // Exit early if voltage stop verification fails
			}
			Calib.logger.info("S059_09_Stop_Execution : Voltage Stopped Successfully Verified");

			// --- Validate Current Stop Status ---
			boolean currentStopped = false;
			for (int i = 0; i < retry_count; i++) {
				responseReturn = checkCurrentStopStatus();
				currentStopped = (boolean) responseReturn.get("status");

				if (currentStopped) {
					break;
				}
				BayUtils.delay(2000);
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Exit (Current Stop Check) Retrying : " + i);
			}

			if (!currentStopped) {
				Calib.logger.info("S059_09_Stop_Execution : Current Stop Verification Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Or a more specific error code
				Calib.logger.info("S059_09_Stop_Execution : Exit (Current Stop Verification Failed)");
				return bayResponse; // Exit early if current stop verification fails
			}
			Calib.logger.info("S059_09_Stop_Execution : Current Stopped Successfully Verified");

			// If both voltage and current stop are verified
			calibrationSummaryProcessor.processOverallCalibration();
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger.info("S059_09_Stop_Execution : Voltage Current Stop Command Failed to Initiate");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
		}

		// WITH IR BAY LAMP TOGGLE
		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {

			boolean toggle = false; // This flag will be used to alternate
			long startTime = System.currentTimeMillis();

			StateExecutorController.getRef_btn_CalibRemove().setDisable(false);

			Calib.logger.debug("S081_stop_FT_source : CALIB_OPTICAL_REMOVED :" + ConstantConveyor.isCALIB_OPTICAL_REMOVED());

			while (!ConstantConveyor.isCALIB_OPTICAL_REMOVED() && !Calib.isStopProcessRequestedCalibBay()) {
				long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

				Platform.runLater(() -> {
					StateExecutorController.ref_tf_CALIB_prompt.setText("Remove Optical Readers - " + elapsedTime + " secs");
				});

				// Alternate the tower lamp state
				if (toggle) {
					turn_on_tower_lamp1();  // Turn ON Lamp 1
					BayUtils.delay(100);
					//turn_off_tower_lamp2(); // Turn OFF Lamp 2
				} else {
					turn_off_tower_lamp1(); // Turn OFF Lamp 1
					BayUtils.delay(100);
					//turn_on_tower_lamp2();// Turn ON Lamp 2
				}

				toggle = !toggle; // Flip the flag for next iteration

				Calib.logger.debug("S081_stop_FT_source : Waiting to remove Optical Readers ");
			}
			BayUtils.delay(2000);
			ConstantConveyor.setCALIB_OPTICAL_REMOVED(false);
			StateExecutorController.getRef_btn_CalibRemove().setDisable(true);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_CALIB_prompt.clear();
			});
		}

		updateCalibrationResult();
		
		
		Calib.logger.info("S059_09_Stop_Execution : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> voltage_current_stop() {
		Calib.logger.debug("S059_09_Stop_Execution : voltage_current_stop : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_CURRENT_STOP);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_09_Stop_Execution : voltage_current_stop : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.ON); // Assuming OLD_OFF_NEW_ON signifies 'stop' command

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateCalibBayHappyPath) {
			status = true;
		}

		Calib.logger.debug("S059_09_Stop_Execution : voltage_current_stop : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		Calib.logger.debug("S059_09_Stop_Execution : voltage_current_stop : Exit");
		return responseReturn;

	}

	// ============================================================================================================================================
	/**
	 * Checks the voltage stop status.
	 * Assumes an input port that indicates the voltage has successfully stopped.
	 * You will need to define `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_STOP_STATUS`
	 * with the correct port name in your `ConstantBayPortNameMapping` class.
	 *
	 * @return A Map containing a "status" boolean, true if voltage stop is confirmed, false otherwise.
	 */
	private Map<String, Object> checkVoltageStopStatus() {
		Calib.logger.debug("S059_09_Stop_Execution : checkVoltageStopStatus : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_STOP_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_09_Stop_Execution : checkVoltageStopStatus : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_09_Stop_Execution : checkVoltageStopStatus : state : " + state);

		// Assuming OLD_OFF_NEW_ON indicates that the voltage is successfully stopped
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_09_Stop_Execution : checkVoltageStopStatus : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_09_Stop_Execution : checkVoltageStopStatus : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================
	/**
	 * Checks the current stop status.
	 * Assumes an input port that indicates the current has successfully stopped.
	 * You will need to define `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_STOP_STATUS`
	 * with the correct port name in your `ConstantBayPortNameMapping` class.
	 *
	 * @return A Map containing a "status" boolean, true if current stop is confirmed, false otherwise.
	 */
	private Map<String, Object> checkCurrentStopStatus() {
		Calib.logger.debug("S059_09_Stop_Execution : checkCurrentStopStatus : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_STOP_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_09_Stop_Execution : checkCurrentStopStatus : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_09_Stop_Execution : checkCurrentStopStatus : state : " + state);

		// Assuming OLD_OFF_NEW_ON indicates that the current is successfully stopped
		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_09_Stop_Execution : checkCurrentStopStatus : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_09_Stop_Execution : checkCurrentStopStatus : Exit");
		return responseReturn;
	}


	// ============================================================================================================================================

	private void updateCalibrationResult() {
		Calib.logger.info("S059_09_Stop_Execution : updateCalibrationResult: Entry");
		PalletTrackerController palletTracker = new PalletTrackerController();
		String selectedBayTypeKey = getMyBayKey();
		String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);

		PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

		for (int positionNo = 1; positionNo <= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {

			resultStatus = calibrationSummaryProcessor.getOverallCalibrationResult(positionNo);
			resultValue = resultStatus;

			testCaseName = ConstantConveyor.SUMMARY_CALIB_RESULT_TEST_NAME;//CALIB_RESULT_TEST_NAME; //"Calibration";
			testType = ConstantConveyor.CALIBRATION_RESULT_KEY; //"CALIB";
			Calib.logger.info("S059_09_Stop_Execution : updateCalibrationResult: testType: " + testType + " ,testCaseName:" + testCaseName + " , resultStatus: " + resultStatus + " ,resultValue: " +resultValue);
			palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
		}
	}

	// ============================================================================================================================================

	private void turn_on_tower_lamp1() {

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
	}

	private void turn_off_tower_lamp1() {

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"
	}

	private void turn_on_tower_lamp2() {

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
	}

	private void turn_off_tower_lamp2() {

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.CLOSE); // Use CLOSE to represent turning the relay "On"
	}

	// ============================================================================================================================================

	// ============================================================================================================================================

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}

	public static boolean isStopExecutionRequested() {
		return stopExecutionRequested;
	}

	public static void setStopExecutionRequested(boolean stopExecutionRequested) {
		S059_09_Stop_Execution.stopExecutionRequested = stopExecutionRequested;
	}

	public String getMyBayKey() {
		return myBayKey;
	}

	public void setMyBayKey(String myBayKey) {
		this.myBayKey = myBayKey;
	}
}
