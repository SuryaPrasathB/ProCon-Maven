package com.tasnetwork.calibration.conveyor.bay.rejection;

import java.net.HttpURLConnection; // Added import for HttpURLConnection
import java.net.URL; // Added import for URL
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S021_check_for_pallet_removed_at_Reject_Bay implements RejectionBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname = ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_REJECTION_001 ;
	
	private static final String FLASK_API_HOST = "127.0.0.1";
	private static final int FLASK_API_PORT = 5001;
	private static final String IDLE_API_ENDPOINT = "/api/rejection_idle";
	private boolean logEnabled = true;
	/**
	 * Checks if the pallet has been removed at the Rejection Bay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Entry");
		BayResponse bayResponse = new BayResponse();
		// Default status to false (pallet not removed), and set the default error code.
		bayResponse.setStatus(false);
		bayResponse.setErrorCode(failStateErrorCode);

		boolean isPalletPresent;
		long startTime;
		boolean stableAbsenceDetected = false; // Flag to indicate if stable absence of pallet is confirmed

		// Loop until stable absence is detected or a global break flag is set
		while (!stableAbsenceDetected 
				&& !Rejection.isStopProcessRequestedRejectionBay()
				&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			// Check the current status of the pallet sensor
			Map<String, Object> responseReturn = isPalletAvailableAt_RejectBay();
			isPalletPresent = (boolean) responseReturn.get("status"); // True if pallet is detected

			if (!isPalletPresent) { // If pallet is NOT present (i.e., it appears to be removed)
				if(logEnabled) {
					Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Pallet not detected, checking for stable absence.");
				}
				startTime = System.currentTimeMillis();

				// Start a sub-loop to confirm the pallet remains absent for a stable duration
				while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
					BayUtils.delay(1000); // Small delay to prevent high CPU usage
					responseReturn = isPalletAvailableAt_RejectBay();
					isPalletPresent = (boolean) responseReturn.get("status");

					if (isPalletPresent) { // If the pallet reappears during the stable check
						if(logEnabled) {
							Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Pallet re-appeared, resetting stable absence check.");
						}
						break; // Break inner loop, outer loop continues to re-check
					}
				}

				// If the inner loop completed without the pallet reappearing, stable absence is confirmed
				if (!isPalletPresent) {
					stableAbsenceDetected = true;
					Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Stable absence of pallet confirmed.");
				}
			} else { // If pallet IS present (it has NOT been removed yet)
				if(logEnabled) {
					Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Pallet still available at Reject Bay, waiting for removal.");
				}
				BayUtils.delay(1000); // Wait for a short period and re-check
			}
			logEnabled = false;
		}

		// After the main loop, determine the final response based on whether stable absence was detected
		if (stableAbsenceDetected) {
			Rejection.logger.info("S021_check_for_pallet_removed_at_Reject_Bay : Pallet successfully removed. Proceeding to next state.");
			bayResponse.setStatus(true); // Success: pallet was removed
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_602); // Use a success error code if applicable, or null/empty string
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().resetAllPalletsExistInBayIndicator(getMyBayKey());
			// Add a 3-second delay as requested
			BayUtils.delay(3000); // Delay for 3 seconds

			// Call the method to set the Rejection Screen to Idle
			setRejectionScreenIdle();

		} else {
			// This path is reached if ALL_LOOP_BREAK_FLAG was set, or if stable absence was never achieved.
			Rejection.logger.warn("S021_check_for_pallet_removed_at_Reject_Bay : Pallet removal check failed or process interrupted.");
			bayResponse.setStatus(false); // Failure: pallet not removed or process interrupted
			bayResponse.setErrorCode(failStateErrorCode); // Keep the default failure error code
		}

		return bayResponse;
	}

	private Map<String,Object> isPalletAvailableAt_RejectBay() {
		if(logEnabled) {
			Rejection.logger.debug("S021_check_for_pallet_removed_at_Reject_Bay : Checking pallet sensor state.");
		}
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default to false (pallet not detected)

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET);

		if (portInfo != null) {

			if(logEnabled) {
				/*Rejection.logger.debug("PortId    : " + portInfo.getPortId());
				Rejection.logger.debug("ClusterId : " + portInfo.getClusterId());
				Rejection.logger.debug("BayId     : " + portInfo.getBayId());*/
				Rejection.logger.debug("isPalletAvailableAt_RejectBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

			}
		} else {
			if(logEnabled) {
				Rejection.logger.error("S021_check_for_pallet_removed_at_Reject_Bay : Pallet sensor port not found: " + ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET);
			}
			return responseReturn; // Return false as the sensor info is missing
		}

		BayUtils bayUtils = new BayUtils();

		// Get the state from the sensor
		String state = bayUtils.getInputDataFromBayV2(portInfo);
		if(logEnabled) {
			Rejection.logger.debug("S021_check_for_pallet_removed_at_Reject_Bay : Sensor state : " + state);
		}
		// Pallet is considered available if the sensor state matches OLD_OFF_NEW_ON
		status = state.equals(Constant_IO_ActionMapping.ON); // True if pallet is present

		// Simulation override: if simulateRejectionBayHappyPath is true, it forces the pallet to be present
		// Note: For simulating "pallet removed" happy path for this state,
		// 'StateExecutorController.simulateRejectionBayHappyPath' should be false
		// or overridden by specific state simulation.
		if(StateExecutorController.simulateRejectionBayHappyPath){
			status = true; // This will simulate the pallet as ALWAYS present
		}
		if(logEnabled) {
			Rejection.logger.debug("S021_check_for_pallet_removed_at_Reject_Bay : Pallet available status : " + status);
		}
		responseReturn.put("status", status);
		if(logEnabled) {
			Rejection.logger.debug("S021_check_for_pallet_removed_at_Reject_Bay : Exit isPalletAvailableAt_RejectBay");
		}
		return responseReturn;
	}

	/**
	 * Sends an HTTP PUT request to the Flask API to set the Rejection Screen to an idle state.
	 * This method assumes the Flask server is running at FLASK_API_HOST:FLASK_API_PORT.
	 */
	private void setRejectionScreenIdle() {
		Rejection.logger.info("Attempting to set Rejection Screen to Idle state via API.");
		String apiUrl = "http://" + FLASK_API_HOST + ":" + FLASK_API_PORT + IDLE_API_ENDPOINT;

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true); // Required for PUT requests, even if body is empty
			conn.setConnectTimeout(5000); // Set connection timeout to 5 seconds
			conn.setReadTimeout(5000);    // Set read timeout to 5 seconds

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				Rejection.logger.info("Successfully set Rejection Screen to Idle. Response Code: " + responseCode);
			} else {
				Rejection.logger.error("Failed to set Rejection Screen to Idle. Response Code: " + responseCode);
				// Optionally, read error stream for more details
				// try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
				//     String line;
				//     StringBuilder response = new StringBuilder();
				//     while ((line = br.readLine()) != null) {
				//         response.append(line);
				//     }
				//     RejectionBay.logger.error("Error Response Body: " + response.toString());
				// }
			}
			conn.disconnect();
		} catch (Exception e) {
			Rejection.logger.error("Exception while trying to set Rejection Screen to Idle: " + e.getMessage(), e);
		}
	}

	public String getPalletSensorPortCname() {
		return palletSensorPortCname;
	}

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public void setPalletSensorPortCname(String palletSensorPortCname) {
		this.palletSensorPortCname = palletSensorPortCname;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
}
