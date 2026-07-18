package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Constant_Motor_Requirement;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus; // Import for TestInterfaceStatus

/**
 * State class responsible for checking for a pallet at the Rejection Bay.
 */
public class S16_check_for_pallet_at_Rejection_Bay implements FtBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.FT_BAY_HP_SEQ_18; // Adjusted sequence ID
	private String palletSensorPortCname = ConstantBayPortNameMapping.REJECT_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_021;

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [SEQUENCE_ENTRY] - Checking for pallet at Rejection Bay.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially, status will be updated based on logic
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn = null; // Initialize to null for the first check
		boolean isPalletPresent = true; // Assume pallet is present to enter the waiting loop
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [WAITING_FOR_CLEARANCE] - Waiting for noEntry at Rejection...", getMyBayKey()));
		
		while((!Ft.isStopProcessRequestedFtBay()) &&
			(ConveyorDataManager.isNoEntryActiveInRejection()) ) 	{
			BayUtils.delay(1000);
		}
		// Loop to wait until no pallet is available at Rejection Bay
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [WAITING_FOR_CLEARANCE] - Waiting for pallet to clear Rejection Bay...", getMyBayKey()));
		while (isPalletPresent && !Ft.isStopProcessRequestedFtBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			responseReturn = isPalletAvailableAt_RejectionBay();	 
			isPalletPresent = (boolean)responseReturn.getOrDefault("status", true); // 
            StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_INP);


			if (isPalletPresent) {
				Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [PALLET_DETECTED] - Pallet still detected at Rejection Bay. Waiting...", getMyBayKey()));
				BayUtils.delay(1000); // Wait for 1 second before re-checking
			}
		}

		// After the loop, check the final state and trigger motor if needed
		if (!isPalletPresent) { // Loop exited because no pallet is available (bay is cleared)
			Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [CLEARED] - No pallet available at Rejection Bay. Proceeding with motor control if enabled.", getMyBayKey()));
			
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [ENABLED] - Activating motor control.", getMyBayKey()));
				
				List<String> motor_requirement = Constant_Motor_Requirement.FT_MOTOR_1_REQUIRED; // Assuming this motor is to move pallet further from rejection bay
				
				Map<String,Object> motorResponse = new HashMap<>();
				try {
					motorResponse = bayUtils.set_motor_required(getMyBayKey(), motor_requirement);
				} catch (Exception e) {
					Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [ERROR] - Exception setting motor requirement: %s", getMyBayKey(), e.getMessage()), e);
					motorResponse.put("status", false); // Indicate failure
				}

				boolean set_motor_required = (boolean) motorResponse.getOrDefault("status", false);
				if (set_motor_required) {
					Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [SUCCESS] - Motor set to required state successfully.", getMyBayKey()));
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [FAILED] - Failed to set motor to required state. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_CALIB_026));
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			} else {
				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [DISABLED] - Motor control is disabled. Assuming pallet cleared correctly.", getMyBayKey()));
				bayResponse.setStatus(true); // If motor control is disabled, and pallet is cleared, this state is successful
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}
		} else { // Loop exited because of stop process flags, but pallet is still present
			Ft.logger.warn(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [STOP_REQUESTED] - Stop process requested, but pallet is still detected at Rejection Bay. Status set to failed.", getMyBayKey()));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_021);  // Indicate failure due to pallet still being present on stop
		}
		
		// Mark GUI status check as completed after final action
        StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn, ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_REJECTION_BAY] : [SEQUENCE_EXIT] - Pallet check at Rejection Bay sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
		return bayResponse;
	}
	//============================================================================================================================================     

	private Map<String,Object> isPalletAvailableAt_RejectionBay() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_ENTRY] - Reading pallet sensor status at Rejection Bay.", getMyBayKey()));

		boolean status = false; // Local boolean status for the operation
		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status

		// Prepare TestInterfaceStatus for GUI update
		TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
		testInterfaceStatus.setBayName(getMyBayKey()); // Use myBayKey
		testInterfaceStatus.setStateName(getBayStateSequenceId()); // Use the sequence ID for this state
		testInterfaceStatus.setDeviceType(ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT); // This is an input sensor
		testInterfaceStatus.setSerialStatus(ConstantConveyor.COMM_STATUS_NOT_APPLICABLE);
		testInterfaceStatus.setPositionNo(sequencePathId); // Use the current sequencePathId
		testInterfaceStatus.setcName(palletSensorPortCname); // Use the correct CName for the sensor
		testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		testInterfaceStatus.setDeviceResponseStatus("Waiting"); // Initial status for GUI

		//============================================================================================		 
		IoPortInfo portInfo = BayUtils.getInputPortDetails(palletSensorPortCname);

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			testInterfaceStatus.setPortName(portInfo.getPortId()); // Set port name for GUI

			String rawStateFromSensor = "";
			try {
				rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
			} catch (Exception e) {
				Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [PALLET_SENSOR_READ] - Failed to read pallet sensor. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
				testInterfaceStatus.setDeviceResponseStatus("Error");
				testInterfaceStatus.setDeviceResponseData("Communication Error");
				testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
				StateExecutorController.updateTestStatusGui(testInterfaceStatus);
				responseReturn.put("status", false); // Indicate failure
				responseReturn.put("responseData", rawStateFromSensor);
				responseReturn.put("testInterfaceStatus", testInterfaceStatus);
				return responseReturn;
			}

			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

			// Determine status based on raw sensor state
			// Assuming OLD_ON_NEW_OFF means pallet is present (sensor is "ON"). This implies sensor is active LOW.
			// The original code `status = state.equals(Constant_IO_ActionMapping.OLD_ON_NEW_OFF) ? true : false;` suggests this.
			// If OLD_ON_NEW_OFF means detected, then `status` should be true.
			// If it means "sensor not active", then `status` is false (no pallet).
			// Let's assume OLD_ON_NEW_OFF means the pallet IS present (sensor triggered).
			
			//status = rawStateFromSensor.equals(Constant_IO_ActionMapping.OLD_ON_NEW_OFF); // true if pallet detected (sensor active)
			status = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON); 
			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [status] : %s", getMyBayKey(), status));
			String interpretedState = status ? Constant_IO_ActionMapping.DETECTED : Constant_IO_ActionMapping.NOT_DETECTED;
			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [interpretedState] : %s", getMyBayKey(), interpretedState));
			// Simulate happy path if enabled
			if(StateExecutorController.simulateFtBayHappyPath){ // Corrected from simulateHappyPath
				// In simulation, for checking if pallet has cleared the bay, we want to simulate 'NOT_DETECTED'
				// to allow the `while` loop in `handleRequest` to exit successfully.
				status = false; // Simulate no pallet for a "happy path" (meaning it can proceed to next step - pallet cleared)
				interpretedState = Constant_IO_ActionMapping.NOT_DETECTED;
				Ft.logger.debug(String.format("[%s] : [SIMULATION] : Pallet sensor status overridden to NOT_DETECTED (false) for clear bay check.", getMyBayKey()));
			}

			// Update GUI status based on interpreted status
			if(status){ // Pallet is detected (present)
				testInterfaceStatus.setDeviceResponseStatus("Success");
				testInterfaceStatus.setDeviceResponseData("Pallet Detected");
			} else { // Pallet is not detected (cleared)
				testInterfaceStatus.setDeviceResponseStatus("Success"); // Still a successful read, just indicates absence
				testInterfaceStatus.setDeviceResponseData("No Pallet");
			}
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED); // Mark status check as completed
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
			
			responseReturn.put("status", status); // Return boolean status: true if pallet present, false if not
			responseReturn.put("responseData", interpretedState); // Return the interpreted state
			responseReturn.put("testInterfaceStatus", testInterfaceStatus); // Include the TestInterfaceStatus object

		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [PALLET_SENSOR_READ] - Input port not found for pallet sensor: %s", getMyBayKey(), palletSensorPortCname));
			testInterfaceStatus.setBayName(getMyBayKey()); // Initialize for error reporting
			testInterfaceStatus.setDeviceResponseStatus("Failed");
			testInterfaceStatus.setDeviceResponseData("I/P port not found");
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			// Add or update GUI with config error status
			// This might be the first time it's added if portInfo is null from the start
			StateExecutorController.addToTestStatusGui(testInterfaceStatus); 
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
			responseReturn.put("status", false); // Indicate failure due to missing config
			responseReturn.put("responseData", "CONFIG_ERROR");
			responseReturn.put("testInterfaceStatus", testInterfaceStatus);
		}

		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_EXIT] - Pallet sensor read completed. Status: %s", getMyBayKey(), status));
		return responseReturn;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
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
}
