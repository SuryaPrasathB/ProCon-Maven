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
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S059_04_Current_Stop implements CalibrationBayState {

	static volatile boolean voltageStartRequest = false;
	static volatile boolean voltageStartAcknowledged = false;

	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S059_04_Current_Stop : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String, Object> responseReturn = current_stop();

		boolean current_stop_initiated = (boolean) responseReturn.get("status");

		if (current_stop_initiated) {
			Calib.logger.info("S059_04_Current_Stop : Current Stop Command Initiated");

			//Calib.logger.info("S059_04_Current_Stop : 10secs Wait Time");
			//BayUtils.delay(10000); // Delay after sending stop command
			if(ConstantConveyor.CALIB_SANGYONG_SOURCE_CONNECTED){
				Calib.logger.info("S059_04_Current_Stop : 15 secs Wait Time");
				BayUtils.delay(5000);//10000); // Delay for 10 seconds as per PLC TIMER
				BayUtils.delay(5000);
				BayUtils.delay(5000);
			}else{
				Calib.logger.info("S059_04_Current_Stop : 10secs Wait Time");
				BayUtils.delay(10000); // Delay for 10 seconds as per PLC TIMER
			}

			// --- Validate Current Stop Status ---
			int retry_count = 5;
			boolean currentStopConfirmed = false;
			for (int i = 0; i < retry_count; i++) {
				responseReturn = checkCurrentStopStatus();
				currentStopConfirmed = (boolean) responseReturn.get("status");
				if (currentStopConfirmed) {
					break;
				}
				BayUtils.delay(2000);
				Calib.logger.info("S059_04_Current_Stop : Exit (Current Stop Verification ) Retrying : " + i);
			}

			if (currentStopConfirmed) {
				Calib.logger.info("S059_04_Current_Stop : Current Stopped Successfully Verified");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				Calib.logger.info("S059_04_Current_Stop : Current Stop Verification Failed");
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Use an appropriate error code
				Calib.logger.info("S059_04_Current_Stop : Exit (Current Stop Verification Failed)");
				
				return bayResponse; // Exit early if current stop verification fails
			}
		} else {
			Calib.logger.info("S059_04_Current_Stop : Current Stop Command Failed to Initiate");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_003); // Use an appropriate error code
		}

		Calib.logger.info("S059_04_Current_Stop : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> current_stop() {
		Calib.logger.debug("S059_04_Current_Stop : current_stop : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		// ============================================================================================
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_ONLY_STOP);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_04_Current_Stop : current_stop : Output port not found");
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

		Calib.logger.debug("S059_04_Current_Stop : current_stop : status : " + status);
		// ============================================================================================

		responseReturn.put("status", status);

		Calib.logger.debug("S059_04_Current_Stop : current_stop : Exit");
		return responseReturn;
	}

	// ============================================================================================================================================

	/**
	 * Checks the status of the Current Stop.
	 * This method assumes there's an input port that indicates the current has successfully stopped.
	 * You will need to define `ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_STOP_STATUS`
	 * with the correct port name in your `ConstantBayPortNameMapping` class.
	 *
	 * @return A Map containing a "status" boolean, true if current stop is confirmed, false otherwise.
	 */
	private Map<String, Object> checkCurrentStopStatus() {
		Calib.logger.debug("S059_04_Current_Stop : checkCurrentStopStatus : Entry");

		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_BOFA_CURRENT_STOP_STATUS);

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Calib.logger.debug("S059_04_Current_Stop : checkCurrentStopStatus : Input port not found");
			return responseReturn;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.getInputDataFromBayV2(portInfo);

		Calib.logger.debug("S059_04_Current_Stop : checkCurrentStopStatus : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		
		  
		/*
		 * if(StateExecutorController.simulateCalibBayHappyPath){ status = true; }
		 */
		 

		Calib.logger.debug("S059_04_Current_Stop : checkCurrentStopStatus : status : " + status);

		responseReturn.put("status", status);

		Calib.logger.debug("S059_04_Current_Stop : checkCurrentStopStatus : Exit");
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

	public static boolean isVoltageStartRequest() {
		return voltageStartRequest;
	}

	public static boolean isVoltageStartAcknowledged() {
		return voltageStartAcknowledged;
	}

	public static void setVoltageStartRequest(boolean voltageStartRequest) {
		S059_04_Current_Stop.voltageStartRequest = voltageStartRequest;
	}

	public static void setVoltageStartAcknowledged(boolean voltageStartAcknowledged) {
		S059_04_Current_Stop.voltageStartAcknowledged = voltageStartAcknowledged;
	}

}