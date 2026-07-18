package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ChannelQueueRequestProcessor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

/**
 * State class responsible for setting the Hardware ID on the meters.
 */
public class S074_Set_HardwareId_On_Meters implements FtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	String sequencePathId = "p1";

	String presentBayKey = "";

	String resultStatus = "";
	String resultValue = "";
	String testType = "";
	String testCaseName = "";

	static Map<Integer, String> errorCodeMap = new HashMap<>();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Structured log entry for sequence entry
		Ft.logger.info(String.format("[%s] : [METER_HW_ID_WRITE] : [SEQUENCE_ENTRY] - Starting meter hardwareId number write sequence to DUTs.", getMyBayKey()));
		errorCodeMap.clear();
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		//errorCodeMap.clear(); just added not tested

		boolean status = generateNextNewHardwareIds();    

		// This method orchestrates writing hardwareId numbers to meters, potentially in parallel.
		if(status){
			bayResponse = processWriteMeterHardwareIdNoToDutFtBay();

			// Log the overall outcome of the hardwareId number writing process
			if(bayResponse.getStatus()){
				Ft.logger.info(String.format("[%s] : [METER_HW_ID_WRITE] : [SUCCESS] - Meter hardwareId number writing completed successfully.", getMyBayKey()));
			} else {
				Ft.logger.error(String.format("[%s] : [METER_HW_ID_WRITE] : [OVERALL_FAILED] - Meter hardwareId number writing overall status failed. Error: %s", getMyBayKey(), bayResponse.getErrorCode()));
			}

			// Structured log for sequence exit
			Ft.logger.info(String.format("[%s] : [METER_HW_ID_WRITE] : [SEQUENCE_EXIT] - Meter hardwareId number write sequence completed.", getMyBayKey()));
		}else{
			Ft.logger.info(String.format("[%s] : [METER_HW_ID_WRITE] : [SEQUENCE_EXIT] - generateNextNewHardwareIds: Failed", getMyBayKey()));
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			bayResponse.setStatus(false);
		}
		return bayResponse;
	}
	//============================================================================================================================================  


	public  boolean generateNextNewHardwareIds() {
		Ft.logger.debug("generateNextNewHardwareIds : Entry");
		List<String> nextIds = new ArrayList<>();
		boolean status = false;
		try {
			Ft.logger.debug("generateNextNewHardwareIds : hit1");
			ConveyorPalletTracking.clearPositionToMeterHardwareIdNoMap();
			String maxHardwareId = MySqlServiceManager.getPalletMeterResultsService().findMaxOfMeterHardwareId();
			Ft.logger.debug("generateNextNewHardwareIds : hit2");
			Ft.logger.debug("generateNextNewHardwareIds : maxHardwareId-1:" +maxHardwareId);
			if( maxHardwareId==null || maxHardwareId.isEmpty()  || maxHardwareId.equals(ConstantConveyor.DEFAULT_DUT_HARDWARE_ID)){
				Ft.logger.debug("generateNextNewHardwareIds : hit3");
				if(maxHardwareId.equals(ConstantConveyor.DEFAULT_DUT_HARDWARE_ID)){
					Ft.logger.debug("generateNextNewHardwareIds : default hardware id observed in the previous result: maxHardwareId-2: " +maxHardwareId);
					return status;
				}
				maxHardwareId = DeviceDataManagerController.getConveyorConfigParsedKey().getDutHardwareIdInitialValue();
				Ft.logger.debug("generateNextNewHardwareIds : maxHardwareId-using default initial value: " +maxHardwareId);
				Ft.logger.debug("generateNextNewHardwareIds : hit4");
			}else{
				Ft.logger.debug("generateNextNewHardwareIds : Max value from MeterResult : maxHardwareId: " +maxHardwareId);
			}
			
			int currentNumber = Integer.parseInt(maxHardwareId);
			Ft.logger.debug("generateNextNewHardwareIds : hit5");
			int paddingLength = maxHardwareId.length();
			Ft.logger.debug("generateNextNewHardwareIds : hit6");
			int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
			Ft.logger.debug("generateNextNewHardwareIds : maxDutSupported :" + maxDutSupported);
			Ft.logger.debug("generateNextNewHardwareIds : hit7");
			//ConveyorPalletTracking.clearPositionToMeterHardwareIdNoMap();
			Ft.logger.debug("generateNextNewHardwareIds : hit8");
			for (int positionNo = 1; positionNo <= maxDutSupported; positionNo++) {
				Ft.logger.debug("generateNextNewHardwareIds : hit9");
				String newHardwareIdNoForDutMeter = String.format("%0" + paddingLength + "d", currentNumber + positionNo);
				nextIds.add(newHardwareIdNoForDutMeter);
				ConveyorPalletTracking.setPositionToMeterHardwareIdNoMap(positionNo, newHardwareIdNoForDutMeter);
				Ft.logger.debug(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [HW_ID-GEN] - new HardwareIdNoForDutMeter position [%s] is : [%s]", getMyBayKey(),positionNo, newHardwareIdNoForDutMeter ));

			}
			Ft.logger.debug("generateNextNewHardwareIds : hit10");
			status = true;


		} catch (Exception e) {

			Ft.logger.error("generateNextNewHardwareIds : Exception :" + e.getMessage());
			//throw new IllegalArgumentException("Hardware ID must be numeric: " + maxHardwareId);
		}
		Ft.logger.debug("generateNextNewHardwareIds : getAllPositionToMeterHardwareIdNoMap: " + ConveyorPalletTracking.getAllPositionToMeterHardwareIdNoMap());
		return status;
	}

	//==========================================================================================================================================================================  
	public BayResponse processWriteMeterHardwareIdNoToDutFtBay() {
		BayResponse overAllBayResponse = new BayResponse();
		overAllBayResponse.setStatus(true);
		Ft.logger.debug(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [ENTRY] - Starting process to write meter hardwareId numbers to DUTs.", getMyBayKey()));

		int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
		ParallelTaskManager dutManager = new ParallelTaskManager();
		boolean monitorAlreadyinitiated = false;
		PalletTrackerController palletTracker = new PalletTrackerController();
		testCaseName = ConstantConveyor.DUT_OPTICAL_HARDWARE_ID_NO_SET_CMD_RESULT_TEST_NAME;
		testType = ConstantConveyor.FT_RESULT_KEY;
		if(ProconFeatureEnable.FT_OPTICAL_SERIAL_WRITE_EXECUTION_PROCESS_IN_PARALLEL){
			Ft.logger.info(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [PARALLEL] - Executing hardwareId number write in parallel mode.", getMyBayKey()));
			for(int positionNo = 1; positionNo <= maxDutSupported; positionNo++){
				Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE] : [INITIATING] - Starting hardwareId write process for position: %d", getMyBayKey(), positionNo));


				dutManager.startWriteMeterHardwareIdNoToDutProcess(positionNo);  


				if(!monitorAlreadyinitiated){
					dutManager.monitorDutControlProcessTrigger(); 
					monitorAlreadyinitiated = true;
					Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE] : [MONITOR_INITIATED] - DUT control process monitor initiated.", getMyBayKey()));
				}
			}
			int dutWaitTimeDurationMaxInSec = 300; // Define timeout constant
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;

			Ft.logger.info(String.format("[%s] : [PARALLEL_WRITE] : [WAITING] - Waiting for parallel hardwareId write tasks to complete. Timeout: %d secs.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
			while ( (!BayUtils.isUserAborted()) && 
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) && 
					(!dutAllProcessExecutionCompleted) &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG) &&
					(!Ft.isStopProcessRequestedFtBay()) ) { // Added Ft.isStopProcessRequestedFtBay() check
				Sleep(1000); // Blocking delay, ensure this is called from a background thread
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE] : [PROGRESS] - Counter: %d/%d. All tasks completed: %s", getMyBayKey(), dutWaitTimeCounter, dutWaitTimeDurationMaxInSec, dutAllProcessExecutionCompleted));
			}

			if(dutManager.isDutAllControlProcessCompleted()){
				Ft.logger.info(String.format("[%s] : [PARALLEL_WRITE] : [ALL_COMPLETED] - All parallel DUT hardwareId write tasks completed.", getMyBayKey()));
				overAllBayResponse.setStatus(true);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
				for(int positionNo =1; positionNo<= maxDutSupported; positionNo++){
					Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE_RESULT] : Position: %d, Summary: <%s>, Response: <%s>",
							getMyBayKey(), positionNo, dutManager.getDutResultSummary(positionNo), dutManager.getDutResultResponse(positionNo)));

					if(dutManager.getDutResultSummary(positionNo).equals(ConstantReport.RESULT_STATUS_FAIL)) { // Check for explicit FAIL
						Ft.logger.debug("Result Hit1: positionNo: " + positionNo);
						overAllBayResponse.setStatus(false);
						overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for parallel write failure
						resultValue= ConstantReport.REPORT_POPULATE_FAIL;
					}else if(dutManager.getDutResultSummary(positionNo).equals(ConstantReport.RESULT_STATUS_PASS)){
						Ft.logger.debug("Result Hit2: positionNo: " + positionNo);
						//overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
						resultValue= ConstantReport.REPORT_POPULATE_PASS;
						
					}else{
						Ft.logger.debug("Result Hit3: positionNo: " + positionNo);
						resultValue= ConstantReport.REPORT_POPULATE_UNDEFINED;
					}
					resultStatus = resultValue;
					palletTracker.addResultToMeter(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
					Ft.logger.info(String.format("[%s] : [PARALLEL_WRITE] : [RESULT] - Position: %s: resultStatus :<%s> resultValue<%s>", getMyBayKey(),positionNo,resultStatus,resultValue));
					if(resultStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
						Ft.logger.debug("Result Hit4: positionNo: " + positionNo);
						palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus,errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000) );
						ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
	                            getMyBayKey(), positionNo, MeterStatus.FAILED, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
					}else{
						Ft.logger.debug("Result Hit5: positionNo: " + positionNo);
						ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
	                            getMyBayKey(), positionNo, MeterStatus.PASSED, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
					}
				}
			}else{
				Ft.logger.error(String.format("[%s] : [PARALLEL_WRITE] : [TIMEOUT] - Parallel hardwareId number writing timed out after %d seconds.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
				overAllBayResponse.setStatus(false);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for timeout
			}
		} else {
			Ft.logger.info(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [SEQUENTIAL] - Executing hardwareId number write in sequential mode.", getMyBayKey()));
			for(int positionNo =1; positionNo<= maxDutSupported ; positionNo++) {
				BayResponse bayResponse  =  dutOpticalHardwareIdNoWriteProcess(positionNo);	 
				Ft.logger.info(String.format("[%s] : [SEQUENTIAL_WRITE_RESULT] : Position: %d, Status: %s, Response Data: %s", getMyBayKey(), positionNo, bayResponse.getStatus(), bayResponse.getResponseData()));

				if(!bayResponse.getStatus()) {
					Ft.logger.error(String.format("[%s] : [SEQUENTIAL_WRITE] : [FAILED] - HardwareId number write failed for position: %d. Status: %s", getMyBayKey(), positionNo, bayResponse.getStatus()));
					overAllBayResponse.setStatus(false);
					overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for sequential write failure
					resultValue= ConstantReport.REPORT_POPULATE_FAIL;
					ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                            getMyBayKey(), positionNo, MeterStatus.FAILED, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
				}else{
					resultValue= ConstantReport.REPORT_POPULATE_PASS;
					ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                            getMyBayKey(), positionNo, MeterStatus.PASSED, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
				}
				resultStatus = resultValue;
				palletTracker.addResultToMeter(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
				if(resultStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
					palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
				}
			}
		}

		Ft.logger.debug(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [EXIT] - Process to write meter hardwareId numbers to DUTs completed. Overall Status: %s", getMyBayKey(), overAllBayResponse.getStatus()));
		return overAllBayResponse;
	}

	//==========================================================================================================================================================================  

	public void Sleep(int timeInMsec) {
		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// Structured error log with stack trace and proper interrupt handling
			Ft.logger.error(String.format("[%s] : [THREAD_SLEEP] : [INTERRUPTED] - Sleep interrupted. Error: %s", getMyBayKey(), e.getMessage()), e);
			Thread.currentThread().interrupt(); // Restore the interrupted status
		}
	}
	//==========================================================================================================================================================================  

	// This method appears to be an internal/legacy method and not directly called from handleRequest().
	// Its logging has been updated for consistency if it's used elsewhere.
	private boolean checkAckForCommands() {
		// This method needs actual implementation to check acknowledgment from devices.
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [ENTRY] - Checking acknowledgment for commands.", getMyBayKey()));
		boolean status = true; // Placeholder for actual implementation.
		// Add actual logic to check acknowledgment
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [EXIT] - Acknowledgment check completed. Status: %s", getMyBayKey(), status));
		return status;
	}
	//==========================================================================================================================================================================  

	public BayResponse dutOpticalHardwareIdNoWriteProcess(int positionNum){
		Ft.logger.info(String.format("[%s] : [OPTICAL_HWID_WRITE] : [ENTRY] - Starting optical hardwareId number write process for position: %d", getMyBayKey(), positionNum));
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Default to false

		DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
		String opticalReaderPortCname = "";
		String meterHardwareIdNumber = ""; // Local variable for meter hardwareId number

		TestInterfaceStatus opticalReaderTestIntefaceStatus = new TestInterfaceStatus();
		TerminalProfileSetting terminalBayProfile = null;

		try {
			terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
			if (terminalBayProfile == null) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [CONFIG_ERROR] - Terminal profile setting not found for bay key: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
				return bayResponse;
			}
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_ERROR] - Failed to retrieve terminal profile for bay: %s. Error: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			return bayResponse;
		}

		setSequencePathId("p1"); // Set sequence path for this operation

		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		String opticalReaderDeviceId = terminalBayProfile.getTerminalId()+ terminalBayProfile.getClusterId()+
				terminalBayProfile.getBayId()+
				ConstantConveyor.DEVICE_TYPE_DUT+
				String.format("%02d", positionNum);

		Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DEVICE_ID] - Optical device ID: %s for Position: %d", getMyBayKey(), opticalReaderDeviceId, positionNum));

		DeviceSetting opticalReaderDeviceSetting = null;
		try {
			opticalReaderDeviceSetting = deviceDataManager.getDeviceSettingByDeviceId(opticalReaderDeviceId);
			if (opticalReaderDeviceSetting == null) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [CONFIG_ERROR] - Device setting not found for optical reader ID: %s at position %d", getMyBayKey(), opticalReaderDeviceId, positionNum));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
				return bayResponse;
			}
			opticalReaderPortCname = opticalReaderDeviceSetting.getCanName();
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [PORT_INFO] - Optical port CName: %s, PortName: %s for Position: %d", getMyBayKey(), opticalReaderPortCname, opticalReaderDeviceSetting.getPortName(), positionNum));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DEVICE_MANAGER_ERROR] - Failed to get device setting for optical reader ID: %s. Error: %s", getMyBayKey(), opticalReaderDeviceId, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			return bayResponse;
		}

		// Initialize TestInterfaceStatus for the GUI
		opticalReaderTestIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_09A, // Assuming BAY_HP_SEQ_09 is for hardwareId write
				ConstantConveyor.DEVICE_TYPE_DUT,
				getSequencePathId(),
				"" + positionNum,
				opticalReaderDeviceSetting.getPortName(),
				opticalReaderPortCname,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				ConstantConveyor.DUT_OPTICAL_HARDWARE_ID_NO_SET_CMD_RESULT_TEST_NAME,
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		int serialNo = StateExecutorController.addToTestStatusGui(opticalReaderTestIntefaceStatus);
		opticalReaderTestIntefaceStatus.setSerialNo(String.valueOf(serialNo));
		//opticalReaderTestIntefaceStatus.setHardwareIdNo(String.valueOf(hardwareIdNoForGui));
		Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus to GUI for position %d. serialNo: %s", getMyBayKey(), positionNum, serialNo));

		SpmDut spManager = null;
		try {
			spManager = devSysEnergyMeter.serialPortInitV2(opticalReaderDeviceSetting);
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [SERIAL_PORT_EXCEPTION] - Exception during serial port initialization for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
			spManager = null; // Ensure spManager is null on exception
		}

		if(spManager == null){
			opticalReaderTestIntefaceStatus.setSerialStatus(ConstantConveyor.COMM_ACCESS_FAILED);
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			bayResponse.setStatus(false); // Mark as failed due to port init failure
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Use a specific error code
			return bayResponse;
		}else{
			opticalReaderTestIntefaceStatus.setSerialStatus("Success");
			// Don't set bayResponse.setStatus(true) here, it should reflect the overall outcome.
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [INIT_SUCCESS] - Serial port initialized successfully for position %d.", getMyBayKey(), positionNum));
		}

		// 1. Communication Check (Placeholder - needs real implementation)
		boolean status = checkAckForCommands();
		if (!status) {
			Ft.logger.warn(String.format("[%s] : [OPTICAL_HWID_WRITE] : [ACK_CHECK_FAILED] - Acknowledgment check failed for position %d. Proceeding with caution or returning.", getMyBayKey(), positionNum));
		}

		// Send Device Unlock Command
		boolean unlockCommandStatus = false;
		try {
			unlockCommandStatus = devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [UNLOCK_CMD_SENT] - Device unlock command sent for position %d. Status: %s", getMyBayKey(), positionNum, unlockCommandStatus));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [UNLOCK_CMD_ERROR] - Failed to send device unlock command for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
			unlockCommandStatus = false;
		}
		if (!unlockCommandStatus) {
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Generic communication error
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [UNLOCK_FAILED] - Device unlock command failed for position %d.", getMyBayKey(), positionNum));
			if (spManager != null) {
				try { spManager.disconnectDut(); } catch (Exception ex) { Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DISCONNECT_ERROR] - Error disconnecting DUT after unlock fail for position %d: %s", getMyBayKey(), positionNum, ex.getMessage()), ex); }
			}
			
			opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Unlock Failed");
			errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_COMM);  
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);

			PalletTrackerController palletTracker = new PalletTrackerController();
			Callable<Boolean> taskAddMetersToPalletDb = () -> {
				boolean queueStatus = palletTracker.addMetersToPallet(getMyBayKey(), String.valueOf(positionNum), positionNum);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_ADD_STATUS-1A] - Add meters to pallet DB for position %d: %s", getMyBayKey(), positionNum, queueStatus));
				return queueStatus;
			};

			long timeoutInSec = 10;
			Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_FT_DB, taskAddMetersToPalletDb, 5, timeoutInSec, TimeUnit.SECONDS);

			try {
				boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_COMPLETE-1A] - DB add task for position %d completed with status: %s", getMyBayKey(), positionNum, responseStatus));
			} catch (TimeoutException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_TIMEOUT-1A] - DB add task for position %d timed out after %d seconds. Error: %s", getMyBayKey(), positionNum, timeoutInSec, e.getMessage()), e);
				bayResponse.setStatus(false); // Mark as failed due to DB timeout
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Specific error code
			} catch (ExecutionException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_FAILED-1A] - DB add task for position %d failed. Cause: %s", getMyBayKey(), positionNum, e.getCause() != null ? e.getCause().getMessage() : "Unknown"), e.getCause());
				bayResponse.setStatus(false); // Mark as failed due to DB execution error
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			} catch (InterruptedException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_INTERRUPTED-1A] - DB add task for position %d interrupted. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				Thread.currentThread().interrupt(); // Restore the interrupted status
				bayResponse.setStatus(false); // Mark as failed due to interruption
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			}
			return bayResponse;
		}

		String presentHardwareIdInDut = "";
		boolean existingPresentHardwareIdInDutIsValid = false;
		if (unlockCommandStatus) {

			Map<String, Object> result = new HashMap<>();

			try {
				result = devSysEnergyMeter.readSerialNumOfMeter(positionNum, spManager); 
				status = (boolean) result.getOrDefault("status", false);
				presentHardwareIdInDut = (String) result.getOrDefault("meterSerialNumber", "");

				existingPresentHardwareIdInDutIsValid = GuiUtils.isNumber(presentHardwareIdInDut);


			} catch (Exception e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [SERIAL_READ_ERROR] - Failed to read serial number for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				status = false;
				presentHardwareIdInDut = "ERROR_READ";
			}

		}



		if(existingPresentHardwareIdInDutIsValid){
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [VALID HWID EXIST IN METER] - Skipping HW ID overwriting : %d", getMyBayKey(), positionNum));
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [VALID HWID EXIST IN METER] - HW ID position: %d : unused HWID : %s", getMyBayKey(), positionNum,ConveyorPalletTracking.getPositionToMeterHardwareIdNoMap(positionNum)));

			ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNum, presentHardwareIdInDut);
			opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
			bayResponse.setStatus(true); // Mark as successful for this DUT
			resultStatus = ConstantReport.REPORT_POPULATE_PASS; // Class member
			resultValue = resultStatus; 
			opticalReaderTestIntefaceStatus.setDeviceResponseData("Wr-Dut Skip HWID= " + presentHardwareIdInDut);




			bayResponse.setResponseData(presentHardwareIdInDut);
			String localPresentHardwareIdInDut = presentHardwareIdInDut;
			PalletTrackerController palletTracker = new PalletTrackerController();
			Callable<Boolean> taskAddMetersToPalletDb = () -> {
				boolean queueStatus = palletTracker.addMetersToPallet(getMyBayKey(), localPresentHardwareIdInDut, positionNum);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_ADD_STATUS-1] - Add meters to pallet DB for position %d: %s", getMyBayKey(), positionNum, queueStatus));
				return queueStatus;
			};

			long timeoutInSec = 10;
			Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_FT_DB, taskAddMetersToPalletDb, 5, timeoutInSec, TimeUnit.SECONDS);

			try {
				boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_COMPLETE-1] - DB add task for position %d completed with status: %s", getMyBayKey(), positionNum, responseStatus));
			} catch (TimeoutException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_TIMEOUT-1] - DB add task for position %d timed out after %d seconds. Error: %s", getMyBayKey(), positionNum, timeoutInSec, e.getMessage()), e);
				bayResponse.setStatus(false); // Mark as failed due to DB timeout
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Specific error code
			} catch (ExecutionException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_FAILED-1] - DB add task for position %d failed. Cause: %s", getMyBayKey(), positionNum, e.getCause() != null ? e.getCause().getMessage() : "Unknown"), e.getCause());
				bayResponse.setStatus(false); // Mark as failed due to DB execution error
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			} catch (InterruptedException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_INTERRUPTED-1] - DB add task for position %d interrupted. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				Thread.currentThread().interrupt(); // Restore the interrupted status
				bayResponse.setStatus(false); // Mark as failed due to interruption
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			}

			// UI update: update dashboard with serial number
			final String finalpresentHardwareIdInDut = presentHardwareIdInDut; // Need final variable for lambda
			Platform.runLater(() -> {
				if (ConveyorDataManager.getDashboardObject() != null) {
					ConveyorDataManager.getDashboardObject().updateSerialByBayAndPosition(getMyBayKey(), positionNum, finalpresentHardwareIdInDut);
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE-1] : Dashboard updated for position %d with serial: %s", getMyBayKey(), positionNum, finalpresentHardwareIdInDut));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING-1] : Dashboard object is null, cannot update HardwardId for position %d.", getMyBayKey(), positionNum));
				}
			});
		//} 
			
		opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);

	}else{

		try {
			unlockCommandStatus = devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE -1] : [UNLOCK_CMD_SENT] - Device unlock command sent for position %d. Status: %s", getMyBayKey(), positionNum, unlockCommandStatus));
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE -1] : [UNLOCK_CMD_ERROR] - Failed to send device unlock command for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
			unlockCommandStatus = false;
		}
		if (!unlockCommandStatus) {
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Generic communication error
			Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE -1] : [UNLOCK_FAILED] - Device unlock command failed for position %d.", getMyBayKey(), positionNum));
			if (spManager != null) {
				try { spManager.disconnectDut(); } catch (Exception ex) { Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DISCONNECT_ERROR] - Error disconnecting DUT after unlock fail for position %d: %s", getMyBayKey(), positionNum, ex.getMessage()), ex); }
			}
			errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_COMM);
			opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Unlock2 Failed");
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			
			
			PalletTrackerController palletTracker = new PalletTrackerController();
			Callable<Boolean> taskAddMetersToPalletDb = () -> {
				boolean queueStatus = palletTracker.addMetersToPallet(getMyBayKey(), String.valueOf(positionNum), positionNum);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_ADD_STATUS-1B] - Add meters to pallet DB for position %d: %s", getMyBayKey(), positionNum, queueStatus));
				return queueStatus;
			};

			long timeoutInSec = 10;
			Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_FT_DB, taskAddMetersToPalletDb, 5, timeoutInSec, TimeUnit.SECONDS);

			try {
				boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_COMPLETE-1B] - DB add task for position %d completed with status: %s", getMyBayKey(), positionNum, responseStatus));
			} catch (TimeoutException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_TIMEOUT-1B] - DB add task for position %d timed out after %d seconds. Error: %s", getMyBayKey(), positionNum, timeoutInSec, e.getMessage()), e);
				bayResponse.setStatus(false); // Mark as failed due to DB timeout
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Specific error code
			} catch (ExecutionException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_FAILED-1B] - DB add task for position %d failed. Cause: %s", getMyBayKey(), positionNum, e.getCause() != null ? e.getCause().getMessage() : "Unknown"), e.getCause());
				bayResponse.setStatus(false); // Mark as failed due to DB execution error
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			} catch (InterruptedException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_INTERRUPTED-1B] - DB add task for position %d interrupted. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				Thread.currentThread().interrupt(); // Restore the interrupted status
				bayResponse.setStatus(false); // Mark as failed due to interruption
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			}
			
			return bayResponse;
		}

		// 2. Write the HardwareId Number to Dut- meters
		// This 'status' variable needs to be updated based on the unlock command status
		if (unlockCommandStatus) { // Proceed only if unlock command was successful
			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [PRE_CHECK_SUCCESS] - Communication and unlock successfull", positionNum));
			Map<String, Object> result = new HashMap<>();

			testType = "FT"; // Class member
			testCaseName = ConstantConveyor.DUT_OPTICAL_HARDWARE_ID_NO_SET_CMD_RESULT_TEST_NAME; // Class member

			// Retrieve meter hardwareId number from the tracking map
			if(ConveyorPalletTracking.hasMeterPositionToMeterHardwareIdNoMap(positionNum)){
				meterHardwareIdNumber = ConveyorPalletTracking.getPositionToMeterHardwareIdNoMap(positionNum);
			} else {
				Ft.logger.warn(String.format("[%s] : [OPTICAL_HWID_WRITE] : [NO_SERIAL] - No hardwareId number found in pallet tracking map for position: %d. Using empty string.", getMyBayKey(), positionNum));
				meterHardwareIdNumber = ""; // Default to empty if not found
			}

			Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [TARGET_SERIAL] - Target write meter hardwareIdNo: '<%s>' for Position: %d", getMyBayKey(), meterHardwareIdNumber, positionNum));

			boolean writeStatus = false;
			try {
				result = devSysEnergyMeter.writeSerialNumOfMeter(positionNum, meterHardwareIdNumber, spManager);

				writeStatus = (boolean) result.getOrDefault("status", false);
			} catch (Exception e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [WRITE_ERROR] - Failed to write hardwareId number for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				writeStatus = false;
			}

			if (writeStatus) {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
				bayResponse.setStatus(true); // Mark as successful for this DUT
				resultStatus = ConstantReport.REPORT_POPULATE_PASS; // Class member
				Ft.logger.info(String.format("[%s] : [OPTICAL_HWID_WRITE] : [WRITE_SUCCESS] - HardwareId number '%s' written successfully for position %d.", getMyBayKey(), meterHardwareIdNumber, positionNum));
			}else{
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Failed");
				errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_OPTICAL_WRITE_SERIAL_NO);
				bayResponse.setStatus(false); // Mark as failed for this DUT
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL; // Class member
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [WRITE_FAILED] - Failed to write hardwareId number for position %d. Status: %s", getMyBayKey(), positionNum, resultStatus));
			}

			resultValue = resultStatus; // Class member - Confirm if resultValue is always the same as resultStatus here.
			opticalReaderTestIntefaceStatus.setDeviceResponseData("Wr-Dut HWID= " + meterHardwareIdNumber);

			bayResponse.setResponseData(meterHardwareIdNumber);
			String localMeterHardwareIdNumber = meterHardwareIdNumber;
			PalletTrackerController palletTracker = new PalletTrackerController();
			Callable<Boolean> taskAddMetersToPalletDb = () -> {
				boolean queueStatus = palletTracker.addMetersToPallet(getMyBayKey(), localMeterHardwareIdNumber, positionNum);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_ADD_STATUS-2] - Add meters to pallet DB for position %d: %s", getMyBayKey(), positionNum, queueStatus));
				return queueStatus;
			};

			long timeoutInSec = 10;
			Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_FT_DB, taskAddMetersToPalletDb, 5, timeoutInSec, TimeUnit.SECONDS);

			try {
				boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS);
				Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_COMPLETE-2] - DB add task for position %d completed with status: %s", getMyBayKey(), positionNum, responseStatus));
			} catch (TimeoutException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_TIMEOUT-2] - DB add task for position %d timed out after %d seconds. Error: %s", getMyBayKey(), positionNum, timeoutInSec, e.getMessage()), e);
				bayResponse.setStatus(false); // Mark as failed due to DB timeout
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Specific error code
			} catch (ExecutionException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_FAILED-2] - DB add task for position %d failed. Cause: %s", getMyBayKey(), positionNum, e.getCause() != null ? e.getCause().getMessage() : "Unknown"), e.getCause());
				bayResponse.setStatus(false); // Mark as failed due to DB execution error
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			} catch (InterruptedException e) {
				Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DB_TASK_INTERRUPTED-2] - DB add task for position %d interrupted. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
				Thread.currentThread().interrupt(); // Restore the interrupted status
				bayResponse.setStatus(false); // Mark as failed due to interruption
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			}

			// UI update: update dashboard with serial number
			//final String localMeterHardwareIdNumber = meterHardwareIdNumber; // Need final variable for lambda
			Platform.runLater(() -> {
				if (ConveyorDataManager.getDashboardObject() != null) {
					ConveyorDataManager.getDashboardObject().updateSerialByBayAndPosition(getMyBayKey(), positionNum, localMeterHardwareIdNumber);
					
					Ft.logger.debug(String.format("[%s] : [UI_UPDATE-2] : Dashboard updated for position %d with serial: %s", getMyBayKey(), positionNum, localMeterHardwareIdNumber));
				} else {
					Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING-2] : Dashboard object is null, cannot update HardwardId for position %d.", getMyBayKey(), positionNum));
				}
			});
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus); // Update GUI
		} else {

		}
	}



	try {
		spManager.disconnectDut();
		Ft.logger.debug(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DUT_DISCONNECT] - DUT disconnected for position %d.", getMyBayKey(), positionNum));
	} catch (Exception e) {
		Ft.logger.error(String.format("[%s] : [OPTICAL_HWID_WRITE] : [DUT_DISCONNECT_ERROR] - Exception during DUT disconnect for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
	}

	if (StateExecutorController.simulateFtBayHappyPath) {
		bayResponse.setStatus(true); // Overriding status for happy path simulation
		Ft.logger.debug(String.format("[%s] : [SIMULATION] : Optical hardwareId write status overridden to TRUE for position %d.", getMyBayKey(), positionNum));
	}

	Ft.logger.info(String.format("[%s] : [OPTICAL_HWID_WRITE] : [EXIT] - Optical hardwareId number write process completed for position %d. Final status: %s", getMyBayKey(), positionNum, bayResponse.getStatus()));
	return bayResponse;
}



//============================================================================================================================================  

public String getSequencePathId() {
	return sequencePathId;
}

public void setSequencePathId(String sequencePathId) {
	this.sequencePathId = sequencePathId;
}

public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
	return getPalletAvailableTest_I_F_Status();
}

public String getBayStateSequenceId() {
	return bayStateSequenceId;
}

public void setBayStateSequenceId(String bayStateSequenceId) {
	this.bayStateSequenceId = bayStateSequenceId;
}

public String getFailStateErrorCode() {
	return failStateErrorCode;
}

public void setFailStateErrorCode(String failStateErrorCode) {
	this.failStateErrorCode = failStateErrorCode;
}
}
