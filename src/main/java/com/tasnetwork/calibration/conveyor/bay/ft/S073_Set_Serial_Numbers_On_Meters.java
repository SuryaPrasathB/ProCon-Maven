package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for writing serial numbers to the meters via optical probe.
 */
public class S073_Set_Serial_Numbers_On_Meters implements FtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	String sequencePathId = "p1";

	// These are class members in the original code, retaining them.
	// For better thread safety and clarity, consider making them local to methods
	// where they are truly transient results, especially if methods can be called concurrently.
	String presentBayKey = "";
	// String meterSerialNumber; // Moved to local scope in dutOpticalSerialNoWriteProcess for clarity
	String resultStatus = "";
	String resultValue = "";
	String testType = "";
	String testCaseName = "";
	

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence entry
        Ft.logger.info(String.format("[%s] : [METER_SERIAL_WRITE] : [SEQUENCE_ENTRY] - Starting meter serial number write sequence to DUTs.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true); // Assume success initially
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // This method orchestrates writing serial numbers to meters, potentially in parallel.
        bayResponse = processWriteMeterSerialNoToDutFtBay();
        
        // Log the overall outcome of the serial number writing process
        if(bayResponse.getStatus()){
            Ft.logger.info(String.format("[%s] : [METER_SERIAL_WRITE] : [SUCCESS] - Meter serial number writing completed successfully.", getMyBayKey()));
	    } else {
	    	Ft.logger.error(String.format("[%s] : [METER_SERIAL_WRITE] : [OVERALL_FAILED] - Meter serial number writing overall status failed. Error: %s", getMyBayKey(), bayResponse.getErrorCode()));
	    }

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [METER_SERIAL_WRITE] : [SEQUENCE_EXIT] - Meter serial number write sequence completed.", getMyBayKey()));
        return bayResponse;
    }
    //============================================================================================================================================  
    
    public BayResponse processWriteMeterSerialNoToDutFtBay() {
    	BayResponse overAllBayResponse = new BayResponse();
    	overAllBayResponse.setStatus(true);
    	Ft.logger.debug(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [ENTRY] - Starting process to write meter serial numbers to DUTs.", getMyBayKey()));

    	int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
    	ParallelTaskManager dutManager = new ParallelTaskManager();
    	boolean monitorAlreadyinitiated = false;
    	PalletTrackerController palletTracker = new PalletTrackerController();
    	testCaseName = ConstantConveyor.DUT_OPTICAL_SERIAL_NO_SET_CMD_RESULT_TEST_NAME;
        testType = ConstantConveyor.FT_RESULT_KEY;
		if(ProconFeatureEnable.FT_OPTICAL_SERIAL_WRITE_EXECUTION_PROCESS_IN_PARALLEL){
			Ft.logger.info(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [PARALLEL] - Executing serial number write in parallel mode.", getMyBayKey()));
			for(int positionNo = 1; positionNo <= maxDutSupported; positionNo++){
				Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE] : [INITIATING] - Starting serial write process for position: %d", getMyBayKey(), positionNo));
				dutManager.startWriteMeterSerialNoToDutProcess(positionNo);
				if(!monitorAlreadyinitiated){
					dutManager.monitorDutControlProcessTrigger(); 
					monitorAlreadyinitiated = true;
                    Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE] : [MONITOR_INITIATED] - DUT control process monitor initiated.", getMyBayKey()));
				}
			}
			int dutWaitTimeDurationMaxInSec = 300; // Define timeout constant
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;

			Ft.logger.info(String.format("[%s] : [PARALLEL_WRITE] : [WAITING] - Waiting for parallel serial write tasks to complete. Timeout: %d secs.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
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
				Ft.logger.info(String.format("[%s] : [PARALLEL_WRITE] : [ALL_COMPLETED] - All parallel DUT serial write tasks completed.", getMyBayKey()));
				
				
				 for(int positionNo =1; positionNo<= maxDutSupported; positionNo++){
					 Ft.logger.debug(String.format("[%s] : [PARALLEL_WRITE_RESULT] : Position: %d, Summary: %s, Response: %s",
                                       getMyBayKey(), positionNo, dutManager.getDutResultSummary(positionNo), dutManager.getDutResultResponse(positionNo)));
					 
					 if(dutManager.getDutResultSummary(positionNo).equals(ConstantReport.RESULT_STATUS_FAIL)) { // Check for explicit FAIL
						 overAllBayResponse.setStatus(false);
						 overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for parallel write failure
						 resultValue= ConstantReport.REPORT_POPULATE_FAIL;
					 }else if(dutManager.getDutResultSummary(positionNo).equals(ConstantReport.RESULT_STATUS_PASS)){
						 resultValue= ConstantReport.REPORT_POPULATE_PASS;
					 }else{
						 resultValue= ConstantReport.REPORT_POPULATE_UNDEFINED;
					 }
					 resultStatus = resultValue;
					palletTracker.addResultToMeter(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
					if(resultStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
						palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus, ErrorCode.ERR_OPTICAL_WRITE_SERIAL_NO);
					}
				 }
			}else{
				Ft.logger.error(String.format("[%s] : [PARALLEL_WRITE] : [TIMEOUT] - Parallel serial number writing timed out after %d seconds.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
				overAllBayResponse.setStatus(false);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for timeout
			}
		} else {
            Ft.logger.info(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [SEQUENTIAL] - Executing serial number write in sequential mode.", getMyBayKey()));
	    	for(int positionNo =1; positionNo<= maxDutSupported ; positionNo++) {
	    		BayResponse bayResponse  =  dutOpticalSerialNoWriteProcess(positionNo);	 
			   	Ft.logger.info(String.format("[%s] : [SEQUENTIAL_WRITE_RESULT] : Position: %d, Status: %s, Response Data: %s", getMyBayKey(), positionNo, bayResponse.getStatus(), bayResponse.getResponseData()));
			   	
			   	if(!bayResponse.getStatus()) {
			   		Ft.logger.error(String.format("[%s] : [SEQUENTIAL_WRITE] : [FAILED] - Serial number write failed for position: %d. Status: %s", getMyBayKey(), positionNo, bayResponse.getStatus()));
			   		overAllBayResponse.setStatus(false);
					overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error code for sequential write failure
					resultValue= ConstantReport.REPORT_POPULATE_FAIL;
			   	}else{
			   		resultValue= ConstantReport.REPORT_POPULATE_PASS;
			   	}
			   	resultStatus = resultValue;
			   	palletTracker.addResultToMeter(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
			   	if(resultStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
			   		palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus, ErrorCode.ERR_OPTICAL_WRITE_SERIAL_NO);
			   	}
	    	}
    	}
    	
    	Ft.logger.debug(String.format("[%s] : [SERIAL_WRITE_PROCESS] : [EXIT] - Process to write meter serial numbers to DUTs completed. Overall Status: %s", getMyBayKey(), overAllBayResponse.getStatus()));
    	return overAllBayResponse;
    }
    
    public void Sleep(int timeInMsec) {
		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// Structured error log with stack trace and proper interrupt handling
			Ft.logger.error(String.format("[%s] : [THREAD_SLEEP] : [INTERRUPTED] - Sleep interrupted. Error: %s", getMyBayKey(), e.getMessage()), e);
			Thread.currentThread().interrupt(); // Restore the interrupted status
		}
	}

	private boolean checkAckForCommands() {
		// This method needs actual implementation to check acknowledgment from devices.
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [ENTRY] - Checking acknowledgment for commands.", getMyBayKey()));
		boolean status = true; // Placeholder for actual implementation.
		// Add actual logic to check acknowledgment
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [EXIT] - Acknowledgment check completed. Status: %s", getMyBayKey(), status));
		return status;
	}
	
	public BayResponse dutOpticalSerialNoWriteProcess(int positionNum){
		Ft.logger.info(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [ENTRY] - Starting optical serial number write process for position: %d", getMyBayKey(), positionNum));
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Default to false

		DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
		String opticalReaderPortCname = "";
		String meterSerialNumber = ""; // Local variable for meter serial number

		TestInterfaceStatus opticalReaderTestIntefaceStatus = new TestInterfaceStatus();
		TerminalProfileSetting terminalBayProfile = null;
		
		try {
			terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
			if (terminalBayProfile == null) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [CONFIG_ERROR] - Terminal profile setting not found for bay key: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY));
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
                return bayResponse;
            }
		} catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DB_ERROR] - Failed to retrieve terminal profile for bay: %s. Error: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY, e.getMessage()), e);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
            return bayResponse;
		}

		setSequencePathId("p1"); // Set sequence path for this operation
		
		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		String opticalReaderDeviceId = terminalBayProfile.getTerminalId()+ terminalBayProfile.getClusterId()+
				terminalBayProfile.getBayId()+
				ConstantConveyor.DEVICE_TYPE_DUT+
				String.format("%02d", positionNum);

		Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DEVICE_ID] - Optical device ID: %s for Position: %d", getMyBayKey(), opticalReaderDeviceId, positionNum));
		
		DeviceSetting opticalReaderDeviceSetting = null;
        try {
            opticalReaderDeviceSetting = deviceDataManager.getDeviceSettingByDeviceId(opticalReaderDeviceId);
            if (opticalReaderDeviceSetting == null) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [CONFIG_ERROR] - Device setting not found for optical reader ID: %s at position %d", getMyBayKey(), opticalReaderDeviceId, positionNum));
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
                return bayResponse;
            }
            opticalReaderPortCname = opticalReaderDeviceSetting.getCanName();
            Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [PORT_INFO] - Optical port CName: %s, PortName: %s for Position: %d", getMyBayKey(), opticalReaderPortCname, opticalReaderDeviceSetting.getPortName(), positionNum));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DEVICE_MANAGER_ERROR] - Failed to get device setting for optical reader ID: %s. Error: %s", getMyBayKey(), opticalReaderDeviceId, e.getMessage()), e);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
            return bayResponse;
        }
		
		// Initialize TestInterfaceStatus for the GUI
		opticalReaderTestIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_09, // Assuming BAY_HP_SEQ_09 is for serial write
				ConstantConveyor.DEVICE_TYPE_DUT,
				getSequencePathId(),
				"" + positionNum,
				opticalReaderDeviceSetting.getPortName(),
				opticalReaderPortCname,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				ConstantConveyor.DUT_OPTICAL_SERIAL_NO_SET_CMD_RESULT_TEST_NAME,
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		int serialNoForGui = StateExecutorController.addToTestStatusGui(opticalReaderTestIntefaceStatus);
		opticalReaderTestIntefaceStatus.setSerialNo(String.valueOf(serialNoForGui));
		Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus to GUI for position %d. SerialNo: %s", getMyBayKey(), positionNum, serialNoForGui));

		SpmDut spManager = null;
		try {
			spManager = devSysEnergyMeter.serialPortInitV2(opticalReaderDeviceSetting);
		} catch (Exception e) {
			Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [SERIAL_PORT_EXCEPTION] - Exception during serial port initialization for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
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
            Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [INIT_SUCCESS] - Serial port initialized successfully for position %d.", getMyBayKey(), positionNum));
		}

		// 1. Communication Check (Placeholder - needs real implementation)
		boolean status = checkAckForCommands();
        if (!status) {
            Ft.logger.warn(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [ACK_CHECK_FAILED] - Acknowledgment check failed for position %d. Proceeding with caution or returning.", getMyBayKey(), positionNum));
        }

        // Send Device Unlock Command
        boolean unlockCommandStatus = false;
        try {
		    unlockCommandStatus = devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
            Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [UNLOCK_CMD_SENT] - Device unlock command sent for position %d. Status: %s", getMyBayKey(), positionNum, unlockCommandStatus));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [UNLOCK_CMD_ERROR] - Failed to send device unlock command for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
            unlockCommandStatus = false;
        }
        if (!unlockCommandStatus) {
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Generic communication error
            Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [UNLOCK_FAILED] - Device unlock command failed for position %d.", getMyBayKey(), positionNum));
            if (spManager != null) {
                try { spManager.disconnectDut(); } catch (Exception ex) { Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DISCONNECT_ERROR] - Error disconnecting DUT after unlock fail for position %d: %s", getMyBayKey(), positionNum, ex.getMessage()), ex); }
            }
            return bayResponse;
        }
		
		// 2. Write the Serial Number to Dut- meters
		// This 'status' variable needs to be updated based on the unlock command status
		if (unlockCommandStatus) { // Proceed only if unlock command was successful
			Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [PRE_CHECK_SUCCESS] - Communication and unlock successful for position: %d", positionNum, serialNoForGui));
			Map<String, Object> result = new HashMap<>();

			testType = "FT"; // Class member
			testCaseName = ConstantConveyor.DUT_OPTICAL_SERIAL_NO_SET_CMD_RESULT_TEST_NAME; // Class member
			
			// Retrieve meter serial number from the tracking map
			if(ConveyorPalletTracking.hasMeterPositionToMeterSerialNoMap(positionNum)){
				meterSerialNumber = ConveyorPalletTracking.getPositionToMeterSerialNoMap(positionNum);
			} else {
			    Ft.logger.warn(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [NO_SERIAL] - No serial number found in pallet tracking map for position: %d. Using empty string.", getMyBayKey(), positionNum));
			    meterSerialNumber = ""; // Default to empty if not found
			}

			Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [TARGET_SERIAL] - Target write meter serialNo: '<%s>' for Position: %d", getMyBayKey(), meterSerialNumber, positionNum));

            boolean writeStatus = false;
            try {
			    result = devSysEnergyMeter.writeSerialNumOfMeter(positionNum, meterSerialNumber, spManager);
			    writeStatus = (boolean) result.getOrDefault("status", false);
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [WRITE_ERROR] - Failed to write serial number for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
                writeStatus = false;
            }

			if (writeStatus) {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
				bayResponse.setStatus(true); // Mark as successful for this DUT
				resultStatus = ConstantReport.REPORT_POPULATE_PASS; // Class member
                Ft.logger.info(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [WRITE_SUCCESS] - Serial number '%s' written successfully for position %d.", getMyBayKey(), meterSerialNumber, positionNum));
			}else{
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Failed");
				bayResponse.setStatus(false); // Mark as failed for this DUT
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL; // Class member
                Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [WRITE_FAILED] - Failed to write serial number for position %d. Status: %s", getMyBayKey(), positionNum, resultStatus));
			}

			resultValue = resultStatus; // Class member - Confirm if resultValue is always the same as resultStatus here.
			opticalReaderTestIntefaceStatus.setDeviceResponseData("Wr-Dut S.N= " + meterSerialNumber);

			// Add result to meter tracker (assuming these methods handle database operations)
            try {
                // Ensure palletTracker is instantiated if needed for these methods
                PalletTrackerController palletTracker = new PalletTrackerController(); // Instantiate if not a class member
                palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
                Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [ADD_RESULT_DB] - Result added to meter for position %d. Status: %s", getMyBayKey(), positionNum, resultStatus));
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [ADD_RESULT_DB_ERROR] - Failed to add result to meter DB for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
            }
			
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus); // Update GUI
		} else {
			// This path is already handled by the `if (!unlockCommandStatus)` block above.
			// No additional action needed here unless there's a different failure path.
		}

        try {
		    spManager.disconnectDut();
		    Ft.logger.debug(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DUT_DISCONNECT] - DUT disconnected for position %d.", getMyBayKey(), positionNum));
		} catch (Exception e) {
		    Ft.logger.error(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [DUT_DISCONNECT_ERROR] - Exception during DUT disconnect for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			bayResponse.setStatus(true); // Overriding status for happy path simulation
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Optical serial write status overridden to TRUE for position %d.", getMyBayKey(), positionNum));
		}

		Ft.logger.info(String.format("[%s] : [OPTICAL_SERIAL_WRITE] : [EXIT] - Optical serial number write process completed for position %d. Final status: %s", getMyBayKey(), positionNum, bayResponse.getStatus()));
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
