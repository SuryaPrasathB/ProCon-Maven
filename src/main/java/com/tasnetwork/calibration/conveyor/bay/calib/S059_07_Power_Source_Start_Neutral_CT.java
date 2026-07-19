package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch; // Added for blocking until UI interaction

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

// JavaFX imports (ensure these are available in your project's classpath)
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class S059_07_Power_Source_Start_Neutral_CT implements CalibrationBayState {

	static volatile boolean powerStartRequest = false;
	static volatile boolean powerStartAcknowledged = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		BayUtils.delay(2000);
		Map<String, Object> responseReturn = voltage_current_start();

		boolean voltage_current_start_initiated = (boolean) responseReturn.get("status");

		if (voltage_current_start_initiated) {
			Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Voltage Current Start Command Initiated");

			// Calib.logger.info("S059_04_Current_Stop : 10secs Wait Time");
			// BayUtils.delay(10000); // Delay after sending start command
			if (ConstantConveyor.CALIB_SANGYONG_SOURCE_CONNECTED) {
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : 15 secs Wait Time");
				BayUtils.delay(5000);// 10000); // Delay for 10 seconds as per PLC TIMER
				BayUtils.delay(5000);
				BayUtils.delay(5000);
			} else {
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : 10secs Wait Time");
				BayUtils.delay(10000); // Delay for 10 seconds as per PLC TIMER
			}

			// --- NEW: Validate Voltage Start Status for Neutral CT ---
			int retry_count = 5;
			boolean neutralVoltageSet = false;
			for (int i = 0; i < retry_count; i++) {
				responseReturn = checkNeutralVoltageStart();
				neutralVoltageSet = (boolean) responseReturn.get("status");
				if (neutralVoltageSet) {
					break;
				}
				BayUtils.delay(2000);
				Calib.logger.info(
						"S059_07_Power_Source_Start_Neutral_CT : Exit (Neutral Voltage Start Check) Retrying : " + i);
			}

			if (!neutralVoltageSet) {
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Neutral Voltage Start Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Or a more specific error code
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Exit (Neutral Voltage Start Failed)");
				return bayResponse; // Exit early if neutral voltage check fails
			}
			Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Neutral Voltage Start Checked Successfully");

			// --- NEW: Validate Current Start Status for Neutral CT ---
			boolean neutralCurrentSet = false;
			for (int i = 0; i < retry_count; i++) {
				responseReturn = checkNeutralCurrentStart();
				neutralCurrentSet = (boolean) responseReturn.get("status");

				if (neutralCurrentSet) {
					break;
				}
				BayUtils.delay(2000);
				Calib.logger.info(
						"S059_02_Power_Source_Start_Main_CT : Exit (Neutral Current Start Check) Retrying : " + i);
			}

			if (!neutralCurrentSet) {
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Neutral Current Start Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Or a more specific error code
				Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Exit (Neutral Current Start Failed)");
				return bayResponse; // Exit early if neutral current check fails
			}
			Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Neutral Current Start Checked Successfully");

			boolean pfSet = false;
			// Loop to repeatedly check PF parameters until they are set correctly
			do {
				responseReturn = checkNeutralPFParameters(); // Use neutral PF check
				pfSet = (boolean) responseReturn.get("status");

				if (!pfSet) {
					Calib.logger.warn(
							"S059_07_Power_Source_Start_Neutral_CT : PF Not set to 0.5. Prompting user to adjust.");
					// Call the method to display the alert and wait for user acknowledgment
					displayPfNotSetAlert(); // Reuses the same alert method
					Calib.logger.info(
							"S059_07_Power_Source_Start_Neutral_CT : User acknowledged PF alert. Re-checking PF parameters.");
				}
			} while (!pfSet && !Calib.isStopProcessRequestedCalibBay()); // Continue looping as long as PF is not set

			Calib.logger.info(
					"S059_07_Power_Source_Start_Neutral_CT : PF is successfully set to 0.5. Proceeding with calibration.");
			// If all checks pass, the overall status is true
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Calib.logger
					.info("S059_07_Power_Source_Start_Neutral_CT : Voltage Current Start Command Failed to Initiate");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003);
		}

		/*
		 * The commented out ProcalRemoteSender logic is complex and involves external
		 * communication and loops. It remains commented as per your original snippet.
		 */

		Calib.logger.info("S059_07_Power_Source_Start_Neutral_CT : Exit");
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

	// ============================================================================================================================================
	private Map<String, Object> voltage_current_start() {
		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : voltage_current_start : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		// This port is for the general voltage/current start command, which might be
		// common
		IoPortInfo portInfo = BayUtils
				.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_240V_VOLTAGE_CURRENT_START);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : voltage_current_start : Output port not found");
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

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : voltage_current_start : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : voltage_current_start : Exit");
		return responseReturn;

	}

	// ============================================================================================================================================
	/**
	 * Checks the voltage start status specifically for the Neutral CT.
	 * Assumes a distinct port for Neutral CT voltage status.
	 * You may need to define
	 * `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_NEUTRAL_VOLTAGE_START_STATUS`.
	 */
	private Map<String, Object> checkNeutralVoltageStart() {
		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralVoltageStart : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Using a new port name for Neutral CT voltage start status
		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_VOLTAGE_START_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger
					.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralVoltageStart : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralVoltageStart : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralVoltageStart : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralVoltageStart : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================
	/**
	 * Checks the current start status specifically for the Neutral CT.
	 * Assumes a distinct port for Neutral CT current status.
	 * You may need to define
	 * `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_NEUTRAL_CURRENT_START_STATUS`.
	 */
	private Map<String, Object> checkNeutralCurrentStart() {
		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralCurrentStart : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Using a new port name for Neutral CT current start status
		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_START_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger
					.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralCurrentStart : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralCurrentStart : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralCurrentStart : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralCurrentStart : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================
	/**
	 * Checks the Power Factor (PF) parameters specifically for the Neutral CT.
	 * Assumes a distinct port for Neutral CT PF status.
	 * You may need to define
	 * `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_NEUTRAL_05L_PF_STATUS`.
	 */
	private Map<String, Object> checkNeutralPFParameters() {
		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralPFParameters : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// Using a new port name for Neutral CT 0.5L PF status
		IoPortInfo portInfo = BayUtils
				.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_05L_PF_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger
					.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralPFParameters : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralPFParameters : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralPFParameters : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_07_Power_Source_Start_Neutral_CT : checkNeutralPFParameters : Exit");
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
		S059_07_Power_Source_Start_Neutral_CT.powerStartRequest = powerStartRequest;
	}

	public static void setPowerStartAcknowledged(boolean powerStartAcknowledged) {
		S059_07_Power_Source_Start_Neutral_CT.powerStartAcknowledged = powerStartAcknowledged;
	}

}
