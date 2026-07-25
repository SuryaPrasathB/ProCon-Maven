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
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for checking for a pallet at the HVT Bay.
 */
public class S11_check_for_pallet_at_HVT_Bay implements FtBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname = ConstantBayPortNameMapping.HV_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_HVT_001;

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();


	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [SEQUENCE_ENTRY] - Checking for pallet at HVT Bay.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); 
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		setSequencePathId("p1"); // Set sequence path ID for this operation
		setPalletAvailableTest_I_F_Status(null); // Resetting for current operation

		Map<String,Object> responseReturn = null; // Initialize to null for the first check
		boolean isPalletPresent = true; 
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [WAITING_FOR_CLEARANCE] - Waiting for noEntry at HV...", getMyBayKey()));
		
		while((!Ft.isStopProcessRequestedFtBay()) &&
			(ConveyorDataManager.isNoEntryActiveInHv()) ) 	{
			BayUtils.delay(1000);
		}
		// Loop to wait until no pallet is available at HVT Bay
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [WAITING_FOR_CLEARANCE] - Waiting for pallet to clear HVT Bay...", getMyBayKey()));
		while (isPalletPresent && !Ft.isStopProcessRequestedFtBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			responseReturn = isPalletAvailableAt_HvtBay();	 
			isPalletPresent = (boolean)responseReturn.getOrDefault("status", true); // Default to true if status is missing
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInNextTargetBay(getMyBayKey(), isPalletPresent);
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));

			if (isPalletPresent) {
				Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [PALLET_DETECTED] - Pallet still detected at HVT Bay. Waiting...", getMyBayKey()));
				BayUtils.delay(1000); // Wait for 1 second before re-checking
			}
		}   
		
		if (Ft.isStopProcessRequestedFtBay() || ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
			ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInNextTargetBay(getMyBayKey(), false);
		}

		// After the loop, check the final state and trigger motor if needed
		if (!isPalletPresent) { // Loop exited because no pallet is available
			Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [CLEARED] - No pallet available at HVT Bay. Proceeding with motor control if enabled.", getMyBayKey()));
			
			if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [ENABLED] - Activating motor control to move pallet into HVT Bay.", getMyBayKey()));
				
				List<String> motor_requirement = Constant_Motor_Requirement.FT_MOTOR_1_2_REQUIRED; // Assuming this is correct for HVT entry
				
				// Ensure responseReturn is not null before using it, or create a new one for motor control result
				Map<String,Object> motorResponse = bayUtils.set_motor_required(getMyBayKey(), motor_requirement);
				boolean set_motor_required_status = (boolean) motorResponse.getOrDefault("status", false);

				if (set_motor_required_status) {
					Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [SUCCESS] - Motor set to required state successfully.", getMyBayKey()));
					bayResponse.setStatus(true);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				} else {
					Ft.logger.error(String.format("[%s] : [MOTOR_CONTROL] : [FAILED] - Failed to set motor to required state. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_CALIB_026));
					bayResponse.setStatus(false);
					bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
				} 
			} else {
				Ft.logger.info(String.format("[%s] : [MOTOR_CONTROL] : [DISABLED] - Motor control is disabled. Proceeding without motor activation.", getMyBayKey()));
				bayResponse.setStatus(true); // If motor control is disabled, and pallet is cleared, this state is successful
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}
		} else { // Loop exited because of stop process flags, but pallet is still present
			Ft.logger.warn(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [STOP_REQUESTED] - Stop process requested, but pallet is still detected at HVT Bay. Status set to failed.", getMyBayKey()));
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Indicate failure due to pallet still being present on stop
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [PALLET_CHECK_HVT_BAY] : [SEQUENCE_EXIT] - Pallet check at HVT Bay sequence completed. Final Status: %s", getMyBayKey(), bayResponse.getStatus()));
		return bayResponse;
	}
	//============================================================================================================================================

	private Map<String,Object> isPalletAvailableAt_HvtBay() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_ENTRY] - Reading pallet sensor status at HVT Bay.", getMyBayKey()));

		boolean status = false; // Local boolean status for the operation
		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status

		// Prepare TestInterfaceStatus for GUI update
		TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();
		testInterfaceStatus.setBayName(getMyBayKey()); // Use myBayKey
		testInterfaceStatus.setStateName(getBayStateSequenceId()); // Use the sequence ID for this state
		testInterfaceStatus.setDeviceType(ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT); // This is an input sensor
		testInterfaceStatus.setSerialStatus(ConstantConveyor.COMM_STATUS_NOT_APPLICABLE);
		testInterfaceStatus.setPositionNo(getSequencePathId());
		testInterfaceStatus.setcName(palletSensorPortCname); // Use the correct CName for the sensor
		testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		testInterfaceStatus.setDeviceResponseStatus("Waiting"); // Initial status for GUI

		// Get port information for the pallet sensor
		IoPortInfo portInfo = BayUtils.getInputPortDetails(palletSensorPortCname);

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			testInterfaceStatus.setPortName(portInfo.getPortId()); // Set port name for GUI

			// Add to GUI and get serial number
			if (getPalletAvailableTest_I_F_Status() == null) {
				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
				testInterfaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			} else {
				// If status object already exists, update it
				testInterfaceStatus = getPalletAvailableTest_I_F_Status();
			}

			String rawStateFromSensor = "";
			try {
				rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
			} catch (Exception e) {
				Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [PALLET_SENSOR_READ] - Failed to read pallet sensor. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
				testInterfaceStatus.setDeviceResponseStatus("Error");
				testInterfaceStatus.setDeviceResponseData("Communication Error");
				StateExecutorController.updateTestStatusGui(testInterfaceStatus);
				responseReturn.put("status", false); // Indicate failure
				responseReturn.put("testInterfaceStatus", testInterfaceStatus);
				return responseReturn;
			}

			Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

			// Determine status based on raw sensor state
			status = rawStateFromSensor.equals(Constant_IO_ActionMapping.ON);
			
			// Simulate happy path if enabled
			if(StateExecutorController.simulateFtBayHappyPath){
				status = false; 
				Ft.logger.debug(String.format("[%s] : [SIMULATION] : Pallet sensor status overridden to NO_PALLET (false).", getMyBayKey()));
			}

			// Update GUI status based on interpreted status
			if(status){
				testInterfaceStatus.setDeviceResponseStatus("Success");
				testInterfaceStatus.setDeviceResponseData("Pallet Present");
			} else {
				testInterfaceStatus.setDeviceResponseStatus("Failed");
				testInterfaceStatus.setDeviceResponseData("No Pallet");
			}
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED); // Mark status check as completed
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
			
			responseReturn.put("status", status); // Return boolean status: true if pallet present, false if not
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [PALLET_SENSOR_READ] - Input port not found for pallet sensor: %s", getMyBayKey(), palletSensorPortCname));
			testInterfaceStatus.setDeviceResponseStatus("Failed");
			testInterfaceStatus.setDeviceResponseData("I/P port not found");
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.addToTestStatusGui(testInterfaceStatus);
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
			responseReturn.put("status", false); // Indicate failure due to missing config
			responseReturn.put("testInterfaceStatus", testInterfaceStatus);
			return responseReturn;
		}

		Ft.logger.debug(String.format("[%s] : [PALLET_SENSOR_READ] : [REQUEST_EXIT] - Pallet sensor read completed. Status: %s", getMyBayKey(), status));
		return responseReturn;
	}

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

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}

	public String getPalletSensorPortCname() {
		return palletSensorPortCname;
	}

	public void setPalletSensorPortCname(String palletSensorPortCname) {
		this.palletSensorPortCname = palletSensorPortCname;
	}

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}
}
