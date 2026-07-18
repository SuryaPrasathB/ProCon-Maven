package com.tasnetwork.calibration.conveyor.bay.ft;

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
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for ensuring the fingertip latch is opened.
 */
public class S10_ensure_the_fingerTip_Latch_Opened implements FtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();


	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence start
		Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [SEQUENCE_ENTRY] - Ensuring the fingertip latch is opened.", getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Assume failure initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_100);

		int try_count = 0;
		Map<String,Object> responseReturn = new HashMap<>(); // Initialize once outside the loop
		String fingerLatchPresentState = "";
		boolean isLatchOpenedSuccessfully = false; // To track the boolean status from ftBay_FingerTipLatch_Status()

		setSequencePathId("p1"); // Set sequence path ID for this operation
		setPalletAvailableTest_I_F_Status (null); // Resetting for current operation

		while(try_count <= 3 && !Ft.isStopProcessRequestedFtBay() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG){ // Loop up to 4 attempts
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [RETRY] - Attempt %d of 4 to verify fingertip latch status.", getMyBayKey(), try_count + 1));
			
			responseReturn = ftBay_FingerTipLatch_Status();
			fingerLatchPresentState = (String)responseReturn.get("responseData");
			isLatchOpenedSuccessfully = (boolean)responseReturn.get("status"); // Retrieve the boolean status
			setPalletAvailableTest_I_F_Status((TestInterfaceStatus)responseReturn.get("testInterfaceStatus"));
			
			if (isLatchOpenedSuccessfully && Constant_IO_ActionMapping.CLOSE.equals(fingerLatchPresentState)){  // Verify success
				Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [SUCCESS] - Fingertip latch confirmed OPENED. State: %s", getMyBayKey(), fingerLatchPresentState));
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().setPalletsLockedImageDisplayOn(getMyBayKey(),false);
				ConveyorDataManager.getDashboardObject().getBayIndicatorManager().stopTimeUpDisplay(getMyBayKey());
				
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				break; // Exit loop on success
			}
			else{
				Ft.logger.warn(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [MISMATCH] - Fingertip latch status is not OPENED. Expected: %s, Actual: %s. Retrying...", getMyBayKey(), Constant_IO_ActionMapping.CLOSE, fingerLatchPresentState));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_013);	
				BayUtils.delay(1000); // Blocking delay; ensure handleRequest() is called from a background thread.
				try_count++ ;
			}
		}

		if (!isLatchOpenedSuccessfully || !Constant_IO_ActionMapping.CLOSE.equals(fingerLatchPresentState)) {
		    // Log final failure if loop completes without success
		    Ft.logger.error(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [FINAL_FAILURE] - Failed to ensure fingertip latch is opened after %d attempts. Final state: %s. Error: %s", getMyBayKey(), try_count, fingerLatchPresentState, ConvErrorCodeMapping.ERROR_CODE_FT_013));
		}

		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [FINGERTIP_LATCH_STATUS_CHECK] : [SEQUENCE_EXIT] - Fingertip latch status check sequence completed.", getMyBayKey()));
		return bayResponse; 
	}	 
	//============================================================================================================================================	 

	private Map<String, Object> ftBay_FingerTipLatch_Status() {
		// Structured debug log for method entry
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_SENSOR_READ] : [REQUEST_ENTRY] - Reading fingertip latch sensor status.", getMyBayKey()));

		Map<String,Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false); // Default boolean status
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		String rawStateFromSensor = ""; // Raw state read from sensor
		boolean isSuccess = false; // Local boolean status for the operation
		
		//============================================================================================
		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP);

		if (portInfo != null) {
			Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_SENSOR_READ] : [PORT_INFO] - PortId: %s, ClusterId: %s, BayId: %s", getMyBayKey(), portInfo.getPortId(), portInfo.getClusterId(), portInfo.getBayId()));
			
			if(getPalletAvailableTest_I_F_Status()==null){
				// Initialize TestInterfaceStatus for GUI
				testIntefaceStatus = new TestInterfaceStatus(
						getMyBayKey(), // Use getMyBayKey() for consistency
						ConstantBayStateManage.FT_BAY_HP_SEQ_13, // Assuming this sequence ID is correct
						ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT, // Corrected to INPUT for sensor
						getSequencePathId(),
						"-",
						portInfo.getPortId(),
						ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP,
						ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
						"Waiting",
						ConstantConveyor.COMM_EXECUTION_STATUS_INP
						);
				
				int newRecordSerialNo = StateExecutorController.addToTestStatusGui(testIntefaceStatus);
				testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
			}else{
				testIntefaceStatus = getPalletAvailableTest_I_F_Status();
			}
		} else {
			// Log a clear error if the port information is missing
			Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [FINGERTIP_LATCH_SENSOR_READ] - Input port not found for Finger Tip Latch Sensor: %s", getMyBayKey(), ConstantBayPortNameMapping.FT_PORT_NAME_SNSR_FINGER_TIP));
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("I/P port not found");
			// Ensure GUI status is updated even for config errors
			StateExecutorController.addToTestStatusGui(testIntefaceStatus); 
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); 
			responseReturn.put("status", false);
			responseReturn.put("responseData", "CONFIG_ERROR"); // Indicate configuration error
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit if critical config is missing
        }

		BayUtils bayUtils = new BayUtils();

		try {
			rawStateFromSensor = bayUtils.getInputDataFromBayV2(portInfo);
		} catch (Exception e) {
			// Log any exceptions during input data retrieval
			Ft.logger.error(String.format("[%s] : [COMMUNICATION_ERROR] : [FINGERTIP_LATCH_SENSOR_READ] - Failed to get input data from fingertip latch sensor. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
			testIntefaceStatus.setDeviceResponseStatus("Error");
			testIntefaceStatus.setDeviceResponseData("Communication Error");
			StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update GUI with error status
			responseReturn.put("status", false);
			responseReturn.put("responseData", rawStateFromSensor); // Still include raw state for debugging
			responseReturn.put("testInterfaceStatus", testIntefaceStatus);
			return responseReturn; // Early exit on communication failure
		}

		Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_SENSOR_READ] : [RAW_STATE] : %s", getMyBayKey(), rawStateFromSensor));

		// Interpret the raw state from the sensor
		String interpretedState = rawStateFromSensor.equals(Constant_IO_ActionMapping.OFF) ? Constant_IO_ActionMapping.CLOSE : Constant_IO_ActionMapping.OPEN;

		if(StateExecutorController.simulateFtBayHappyPath){
			interpretedState = Constant_IO_ActionMapping.CLOSE; // Simulate successful state (latch is open)
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Fingertip latch status overridden to OPENED.", getMyBayKey()));
		}

		// Update TestInterfaceStatus based on the interpreted state
		if(Constant_IO_ActionMapping.CLOSE.equals(interpretedState)){ // Check if the interpreted state is OPEN
			testIntefaceStatus.setDeviceResponseStatus("Success");
			isSuccess = true; // Set local status to true if latch is OPEN
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			isSuccess = false; // Set local status to false if latch is NOT OPEN
		}
		
		// Check for timeout or invalid response
		if(portInfo.getPortId().equals(rawStateFromSensor)){ 
			testIntefaceStatus.setDeviceResponseData("TimeOut");
			Ft.logger.warn(String.format("[%s] : [FINGERTIP_LATCH_SENSOR_READ] : [TIMEOUT] - Fingertip latch sensor read timed out or invalid response. Raw state: %s", getMyBayKey(), rawStateFromSensor));
		}else{
			testIntefaceStatus.setDeviceResponseData(interpretedState); // Store the actual interpreted state
		}
		
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		responseReturn.put("responseData", interpretedState); // Use the interpreted state in responseData
		responseReturn.put("status", isSuccess); // Return the boolean status
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);
		
		// Structured debug log for method exit
		Ft.logger.debug(String.format("[%s] : [FINGERTIP_LATCH_SENSOR_READ] : [REQUEST_EXIT] - Fingertip latch sensor read completed. Status: %s, Interpreted State: %s", getMyBayKey(), isSuccess, interpretedState));
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

	//===============================================================================================================================

}
