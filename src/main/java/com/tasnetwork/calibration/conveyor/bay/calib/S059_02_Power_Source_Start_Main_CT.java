// S059_02_Power_Source_Start_Main_CT.java (Modified)
package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch; // For blocking until UI interaction

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

// JavaFX imports
import javafx.application.Platform;
import javafx.scene.control.Alert; // Added for Alert
import javafx.scene.control.Alert.AlertType; // Added for AlertType

public class S059_02_Power_Source_Start_Main_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;
	static volatile boolean powerStartAcknowledged = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Entry");
		BayResponse bayResponse = new BayResponse();
		// Initialize bayResponse to success, will be updated if any step fails or
		// aborts
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		boolean allCalibrationStepsCompleted = false;

		// Outer loop to re-attempt the entire power source start and validation process
		// if PF is not set, or if an initial step fails (unless aborted by user).
		do {
			// Check if the user has aborted the operation before starting a new iteration
			if (BayUtils.isUserAborted()) {
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : User aborted, exiting calibration process.");
				bayResponse.setStatus(false); // Set status to false

				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_999); // Use a specific abort error code if
																				// available
				break; // Exit the do-while loop immediately
			}

			Map<String, Object> responseReturn;

			// --- 1. Set 40V, Stop, Set 240V ---

			Calib.logger.info("Start 40V: voltage_current_start(40)");

			// Step 1: Start 40V
			Map<String, Object> start40Response = voltage_current_start(40);
			boolean start40Success = (boolean) start40Response.get("status");

			if (!start40Success) {
				Calib.logger.error("Step Failed: voltage_current_start(40)");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
				continue;
			}

			Calib.logger.info("Start 40V: Delay Started");

			int waitTimeInSec = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getCalibSuperCapacitorChargeWaitTimeInSec();// 90;//45
			Calib.logger.info("Start 40V: CalibSuperCapacitorChargeWaitTimeInSec: " + waitTimeInSec);
			while ((waitTimeInSec > 0) && (!BayUtils.isUserAborted())) {
				if (!BayUtils.isUserAborted()) {
					BayUtils.delay(1000);
				}
				waitTimeInSec--;
			}
			Calib.logger.info("Start 40V: Delay Complete");

			// Step 2: stop

			Calib.logger.info("Stop: voltage_current_stop()");

			Map<String, Object> stopResponse = voltage_current_stop();
			boolean stopSuccess = (boolean) stopResponse.get("status");

			if (!stopSuccess) {
				Calib.logger.error("Step Failed: voltage_current_stop()");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
				continue;
			}

			Calib.logger.info("Stop: Delay Started");

			BayUtils.delay(5000);

			Calib.logger.info("Stop: Delay Complete");

			// Step 3: Start 240V

			Calib.logger.info("Start 240V: voltage_current_start(240)");

			Map<String, Object> start240Response = voltage_current_start(240);
			boolean start240Success = (boolean) start240Response.get("status");

			if (!start240Success) {
				Calib.logger.error("Step Failed: voltage_current_start(240)");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
				continue;
			}

			Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Voltage Current Started");
			// Calib.logger.info("S059_04_Current_Stop : 10secs Wait Time");
			if (ConstantConveyor.CALIB_SANGYONG_SOURCE_CONNECTED) {
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : 15 secs Wait Time");
				BayUtils.delay(5000);// 10000); // Delay for 10 seconds as per PLC TIMER
				BayUtils.delay(5000);
				BayUtils.delay(5000);
			} else {
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : 10secs Wait Time");
				BayUtils.delay(10000); // Delay for 10 seconds as per PLC TIMER
			}
			// --- 2. Validate Voltage Start Status ---
			int retry_count = 5;
			boolean voltageSet = false;
			for (int i = 0; i < retry_count; i++) {
				if (BayUtils.isUserAborted())
					break; // Allow abort during retries
				responseReturn = checkVoltageStart();
				voltageSet = (boolean) responseReturn.get("status");
				if (voltageSet) {
					break; // Voltage set, exit retry loop
				}
				BayUtils.delay(2000);
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Voltage Check Retrying : " + (i + 1));
			}

			if (!voltageSet) {
				if (BayUtils.isUserAborted()) {
					Calib.logger
							.info("S059_02_Power_Source_Start_Main_CT : User aborted during VOLTAGE check retries.");
					allCalibrationStepsCompleted = false; // Mark as not completed
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_999);
					break; // Exit the main do-while loop
				}
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Voltage Start Failed after retries.");
				displayVoltageNotSetAlert();
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_015); // Error for voltage check failure
				continue; // Return to Do While to retry Voltage Setting
				// break; // Exit state after failure
			}
			Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Voltage Start Checked Successfully");

			// --- 3. Validate Current Start Status ---
			boolean currentSet = false;
			for (int i = 0; i < retry_count; i++) {
				if (BayUtils.isUserAborted())
					break; // Allow abort during retries
				responseReturn = checkCurrentStart();
				currentSet = (boolean) responseReturn.get("status");

				if (currentSet) {
					break; // Current set, exit retry loop
				}
				BayUtils.delay(2000);
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Current Check Retrying : " + (i + 1));
			}

			if (!currentSet) {
				Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Current Start Failed after retries.");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Error for current check failure
				BayUtils.delay(2000); // Keep this delay as in original code
				continue; // Restart the outer do-while loop to re-attempt from voltage_current_start
			}
			Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Current Start Checked Successfully");

			// --- 4. Validate PF Parameters with internal retries ---
			boolean pfSet = false;
			int pf_internal_retry_count = 3;
			for (int i = 0; i < pf_internal_retry_count; i++) {
				if (BayUtils.isUserAborted()) {
					pfSet = false; // Ensure flag is false if aborted
					break; // Allow abort during retries
				}
				responseReturn = checkPFParameters();
				pfSet = (boolean) responseReturn.get("status");
				if (pfSet) {
					break; // PF set, exit internal retry loop
				}
				Calib.logger.warn("S059_02_Power_Source_Start_Main_CT : PF not set to 0.5. Internal retry " + (i + 1)
						+ " of " + pf_internal_retry_count + ".");
				BayUtils.delay(3000); // Small delay between internal PF checks
			}

			if (!pfSet) {
				if (BayUtils.isUserAborted()) {
					Calib.logger.info("S059_02_Power_Source_Start_Main_CT : User aborted during PF check retries.");
					allCalibrationStepsCompleted = false; // Mark as not completed
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_999);
					break; // Exit the main do-while loop
				}
				Calib.logger.warn(
						"S059_02_Power_Source_Start_Main_CT : PF Not set to 0.5 after internal retries. Prompting user to adjust.");
				displayPfNotSetAlert(); // This will block until user clicks OK
				Calib.logger.info(
						"S059_02_Power_Source_Start_Main_CT : User acknowledged PF alert. Re-checking all parameters from start.");
				allCalibrationStepsCompleted = false; // Set to false to trigger outer loop re-run
			} else {
				Calib.logger.info(
						"S059_02_Power_Source_Start_Main_CT : PF is successfully set to 0.5. Proceeding with calibration.");
				allCalibrationStepsCompleted = true; // All steps passed successfully for this iteration
			}

		} while (!allCalibrationStepsCompleted && !BayUtils.isUserAborted() && !Calib.isStopProcessRequestedCalibBay());

		// Final update to bayResponse status based on whether all steps were truly
		// completed
		// or if the loop was exited due to an abort.
		if (allCalibrationStepsCompleted) {
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			// If allCalibrationStepsCompleted is false here, it means either:
			// 1. User aborted (status and error code set inside the loop)
			// 2. An intermediate step failed and the loop continued, then user might have
			// aborted
			// or it simply ended up with allCalibrationStepsCompleted as false.
			// The status and error code from the last failure or abort will persist.
		}

		Calib.logger.info("S059_02_Power_Source_Start_Main_CT : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	/**
	 * This method displays a standard JavaFX Alert dialog to the user when the PF
	 * is not set.
	 * It uses JavaFX's Platform.runLater to ensure UI operations are on the FX
	 * Application Thread
	 * and a CountDownLatch to block the calling thread until the user clicks 'OK'.
	 */
	private void displayPfNotSetAlert() {
		// CountDownLatch to block the current thread until the dialog is closed
		final CountDownLatch latch = new CountDownLatch(1);

		Platform.runLater(() -> {
			try {
				Alert alert = new Alert(AlertType.WARNING); // Use WARNING type for the alert
				alert.setTitle("PF Not Set - Action Required");
				alert.setHeaderText(null); // No header text
				alert.setContentText("PF Not set to 0.5. Please set it and press OK to continue.");

				alert.getDialogPane().getScene().getWindow();

				alert.showAndWait();

				// If the user clicks OK, or simply closes the dialog, the latch counts down.
				// For this specific use case (PF not set, user must fix), we assume
				// any dismissal means the user has acknowledged and is ready for re-check.
				latch.countDown();

			} catch (Exception e) {
				Calib.logger.error("Failed to display PF alert dialog: " + e.getMessage(), e);
				latch.countDown(); // Release the latch even on error to prevent indefinite blocking
			}
		});

		// Block the current thread until the JavaFX dialog is closed and the latch is
		// counted down
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Calib.logger.error("Interrupted while waiting for PF alert acknowledgment.", e);
		}
	}

	private void displayVoltageNotSetAlert() {
		// CountDownLatch to block the current thread until the dialog is closed
		final CountDownLatch latch = new CountDownLatch(1);

		Platform.runLater(() -> {
			try {
				Alert alert = new Alert(AlertType.WARNING); // Use WARNING type for the alert
				alert.setTitle("Voltage Failed - Action Required");
				alert.setHeaderText(null); // No header text
				alert.setContentText("Voltage Start Failed after retries. Please check CALIBRATION SOURCE");

				alert.getDialogPane().getScene().getWindow();

				alert.showAndWait();

				// If the user clicks OK, or simply closes the dialog, the latch counts down.
				// For this specific use case (PF not set, user must fix), we assume
				// any dismissal means the user has acknowledged and is ready for re-check.
				latch.countDown();

			} catch (Exception e) {
				Calib.logger.error("Failed to display Voltage alert dialog: " + e.getMessage(), e);
				latch.countDown(); // Release the latch even on error to prevent indefinite blocking
			}
		});

		// Block the current thread until the JavaFX dialog is closed and the latch is
		// counted down
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			Calib.logger.error("Interrupted while waiting for Voltage alert acknowledgment.", e);
		}
	}

	// ============================================================================================================================================
	private Map<String, Object> voltage_current_start(int voltage) {
		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo;

		if (voltage == 40) {
			portInfo = BayUtils
					.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_40V_VOLTAGE_CURRENT_START);
		} else {
			portInfo = BayUtils
					.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_240V_VOLTAGE_CURRENT_START);
		}

		if (portInfo != null) {
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.ON);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if (StateExecutorController.simulateCalibBayHappyPath) {
			status = true;
		}

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : voltage_current_start : Exit");
		return responseReturn;

	}

	// ============================================================================================================================================

	private Map<String, Object> voltage_current_stop() {
		Calib.logger.debug("S059_09_Stop_Execution : voltage_current_stop : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo = BayUtils
				.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_CURRENT_STOP);

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

	private Map<String, Object> checkVoltageStart() {
		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkVoltageStart : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_START_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkVoltageStart : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkVoltageStart : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkVoltageStart : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkVoltageStart : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================

	private Map<String, Object> checkCurrentStart() {
		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkCurrentStart : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_START_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkCurrentStart : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkCurrentStart : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkCurrentStart : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkCurrentStart : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================

	private Map<String, Object> checkPFParameters() {
		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkPFParameters : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_05L_PF_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkPFParameters : Output port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkPFParameters : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkPFParameters : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_02_Power_Source_Start_Main_CT : checkPFParameters : Exit");
		return responseReturn;
	}

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

	public static boolean isPowerStartRequest() {
		return powerStartRequest;
	}

	public static boolean isPowerStartAcknowledged() {
		return powerStartAcknowledged;
	}

	public static void setPowerStartRequest(boolean powerStartRequest) {
		S059_02_Power_Source_Start_Main_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S059_02_Power_Source_Start_Main_CT.powerStartAcknowledged = powerStartAcknowledged;
	}

}
