package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S070_opticalProbe_Scanning_of_Meters implements FtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
	
	private String sequencePathId = "p1"; // Class member, retain for consistency with other methods

	private String meterSerialNumber = "";
	private String resultStatus = "";
	private String resultValue = "";
	private String testType = "";
	private String testCaseName = "";
	
	private String myBaySeqId = ConstantBayStateManage.BAY_HP_SEQ_10;

	public String getMyBayKey() {
		return myBayKey;
	}

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Ft.logger.info(String.format("[%s] : [METER_SCAN_FLOW] : [SEQUENCE_ENTRY] - Starting optical probe scanning for meters.", getMyBayKey()));
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        
        bayResponse = processOpticalProbeScanningMeterSerialNoFtBay();
       
        if(bayResponse.getStatus()){
	        if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {
                Ft.logger.info(String.format("[%s] : [OPTICAL_READER_PROMPT] : [ACTIVE] - Waiting for user to place optical readers.", getMyBayKey()));
	        	
	            boolean toggle = false;
	            long startTime = System.currentTimeMillis();
	            
	            Platform.runLater(() -> {
                    if (StateExecutorController.getRef_btn_FtPlace() != null) {
                        StateExecutorController.getRef_btn_FtPlace().setDisable(false);
                        Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Enabled FtPlace button.", getMyBayKey()));
                    } else {
                        Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_FtPlace is null, cannot enable button.", getMyBayKey()));
                    }
                });
	            
	            while (!ConstantConveyor.isFT_OPTICAL_PLACED()) {
	            	long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds
	
	                Platform.runLater(() -> {
                        if (StateExecutorController.ref_tf_FT_prompt != null) {
                            StateExecutorController.ref_tf_FT_prompt.setText("Place Optical Readers - " + elapsedTime + " secs");
                        } else {
                            Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot update prompt text.", getMyBayKey()));
                        }
	                });
	
	                // Alternate the tower lamp state
	                if (toggle) {
	                    turn_on_tower_lamp1();  // Turn ON Lamp 2
	                    BayUtils.delay(100); // Blocking delay, ensure calling context is a background thread
	                } else {
	                    turn_off_tower_lamp1(); // Turn OFF Lamp 1
	                    BayUtils.delay(100); // Blocking delay
	                }
	                toggle = !toggle; // Flip the flag for next iteration
	
	                Ft.logger.debug(String.format("[%s] : [OPTICAL_READER_PROMPT] : [WAITING] - Waiting to place Optical Readers. Elapsed: %d secs.", getMyBayKey(), elapsedTime));
	            }
	            
	            BayUtils.delay(2000); // Blocking delay
	            ConstantConveyor.setFT_OPTICAL_PLACED(false); // Reset flag
	            
	            Platform.runLater(() -> {
                    if (StateExecutorController.getRef_btn_FtPlace() != null) {
                        StateExecutorController.getRef_btn_FtPlace().setDisable(true);
                        Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Disabled FtPlace button.", getMyBayKey()));
                    } else {
                        Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_btn_FtPlace is null, cannot disable button.", getMyBayKey()));
                    }
	            });
	            
	            Platform.runLater(() -> {
                    if (StateExecutorController.ref_tf_FT_prompt != null) {
                        StateExecutorController.ref_tf_FT_prompt.clear();
                        Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Cleared FT prompt text.", getMyBayKey()));
                    } else {
                        Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot clear prompt text.", getMyBayKey()));
                    }
	            });
                Ft.logger.info(String.format("[%s] : [OPTICAL_READER_PROMPT] : [COMPLETED] - User input for optical readers completed.", getMyBayKey()));

	        } else {
                Ft.logger.info(String.format("[%s] : [OPTICAL_READER_PROMPT] : [DISABLED] - User input for optical readers is disabled.", getMyBayKey()));
            }
	    } else {
	    	// Structured error log for overall QR scanning failure
	    	Ft.logger.error(String.format("[%s] : [METER_SCAN_FLOW] : [OVERALL_FAILED] - Meter optical scanning overall status failed. Error: %s", getMyBayKey(), bayResponse.getErrorCode()));
	    }

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [METER_SCAN_FLOW] : [SEQUENCE_EXIT] - Optical probe scanning for meters sequence completed.", getMyBayKey()));
        return bayResponse;
    }
   //============================================================================================================================================  

    private void turn_on_tower_lamp1() {
    	Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [TURN_ON_REQUEST] - Sending command to turn on Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
    	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

        if (portInfo == null) {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
            return;
        }

    	BayUtils bayUtils = new BayUtils();
        String state = "";
        try {
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [ON_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send ON command to Tower Lamp 1. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
        }
    }

    private void turn_off_tower_lamp1() {
    	Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [TURN_OFF_REQUEST] - Sending command to turn off Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
    	IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

        if (portInfo == null) {
            Ft.logger.error(String.format("[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).", getMyBayKey()));
            return;
        }

    	BayUtils bayUtils = new BayUtils();
        String state = "";
        try {
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"
            Ft.logger.debug(String.format("[%s] : [TOWER_LAMP_1] : [OFF_COMMAND_SENT] - Command sent. Response state: %s", getMyBayKey(), state));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send OFF command to Tower Lamp 1. Port: %s. Error: %s", getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
        }
    }

    
    
  //============================================================================================================================================
  
    public BayResponse processOpticalProbeScanningMeterSerialNoFtBay() {
    	
    	BayResponse overAllBayResponse = new BayResponse();
    	overAllBayResponse.setStatus(true);
    	Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_SCAN_PROCESS] : [ENTRY] - Starting optical probe scanning process for meters.", getMyBayKey()));
    	int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
    	
    	// The ParallelTaskManager and its associated logic are related to QR scanning, not optical probe.
    	// If optical probe scanning is always sequential, this section can be simplified.
    	// Assuming for now that 'dutManager' is only for QR, and optical is sequential.

    	ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();
    	
    	Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_SCAN_PROCESS] : [SEQUENTIAL] - Executing optical probe scan in sequential mode.", getMyBayKey()));
		for(int positionNo = 1; positionNo <= maxDutSupported ; positionNo++) {
			BayResponse bayResponse  =  readMeterSerialOpticalProbe(positionNo);	 
			Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_SCAN_RESULT] : Position: %d, Status: %s, Response Data: %s", getMyBayKey(), positionNo, bayResponse.getStatus(), bayResponse.getResponseData()));
		   	
		   	String readMeterSerialNo = bayResponse.getResponseData(); // Renamed from readQrMeterSerialNo
		   	ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNo, readMeterSerialNo);
		   	
		   	if(!bayResponse.getStatus()) {
		   		Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_SCAN_PROCESS] : [FAILED] - Optical probe scan failed for position: %d. Status: %s", getMyBayKey(), positionNo, bayResponse.getStatus()));
		   		overAllBayResponse.setStatus(false);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Use a more specific error code for optical probe failure
		   	}
		}
    
		Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_SCAN_PROCESS] : [EXIT] - Optical probe scanning process completed. Overall Status: %s", getMyBayKey(), overAllBayResponse.getStatus()));
		return overAllBayResponse;
    }
    
    public BayResponse processQrCodeScanningMeterSerialNoFtBay() {
    	Ft.logger.debug(String.format("[%s] : [QR_CODE_SCAN_PROCESS] : [ENTRY] - Starting QR code scanning process for meters.", getMyBayKey()));
    	BayResponse overAllBayResponse = new BayResponse();
    	overAllBayResponse.setStatus(true);
    	
    	int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
    	ParallelTaskManager dutManager = new ParallelTaskManager();
    	boolean monitorAlreadyinitiated = false;

    	ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();

		if(ProconFeatureEnable.FT_QR_SCANNER_EXECUTION_PROCESS_IN_PARALLEL){
            Ft.logger.info(String.format("[%s] : [QR_CODE_SCAN_PROCESS] : [PARALLEL] - Executing QR scan in parallel mode.", getMyBayKey()));
			for(int positionNo = 1; positionNo <= maxDutSupported; positionNo++){
                Ft.logger.debug(String.format("[%s] : [PARALLEL_SCAN] : [INITIATING] - Starting QR scan process for position: %d", getMyBayKey(), positionNo));
				dutManager.startFtQrScanProcess(positionNo);
				if(!monitorAlreadyinitiated){
					dutManager.monitorDutControlProcessTrigger(); 
					monitorAlreadyinitiated = true;
                    Ft.logger.debug(String.format("[%s] : [PARALLEL_SCAN] : [MONITOR_INITIATED] - DUT control process monitor initiated.", getMyBayKey()));
				}
			}
			int dutWaitTimeDurationMaxInSec = 300;
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;

            Ft.logger.info(String.format("[%s] : [PARALLEL_SCAN] : [WAITING] - Waiting for parallel QR scan tasks to complete. Timeout: %d secs.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
			while ( (!ProjectExecutionController.getUserAbortedFlag()) && 
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) && 
					(!dutAllProcessExecutionCompleted) &&
	        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG) &&
	        		(!Ft.isStopProcessRequestedFtBay() )){
				Sleep(1000); // Blocking call, ensure this method is in a background thread
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Ft.logger.debug(String.format("[%s] : [PARALLEL_SCAN] : [PROGRESS] - Counter: %d/%d. All tasks completed: %s", getMyBayKey(), dutWaitTimeCounter, dutWaitTimeDurationMaxInSec, dutAllProcessExecutionCompleted));
			}
			if(dutManager.isDutAllControlProcessCompleted()){
				Ft.logger.info(String.format("[%s] : [PARALLEL_SCAN] : [ALL_COMPLETED] - All parallel DUT QR scan tasks completed.", getMyBayKey()));
				String readQrMeterSerialNo = "";
				for(int positionNo =0; positionNo<= maxDutSupported; positionNo++){ // Iterating from 0, ensure consistency with how positions are used (1-based vs 0-based)
					Ft.logger.debug(String.format("[%s] : [PARALLEL_SCAN_RESULT] : Position: %d, Summary: %s, SerialNo: %s, HexSerialNo: %s",
                                       getMyBayKey(), positionNo, dutManager.getDutResultSummary(positionNo), dutManager.getDutResultResponse(positionNo), GuiUtils.StringToHex(dutManager.getDutResultResponse(positionNo))));
					readQrMeterSerialNo = dutManager.getDutResultResponse(positionNo);
					ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNo, readQrMeterSerialNo);
					if(!dutManager.getDutResultSummary(positionNo).equals(ConstantReport.RESULT_STATUS_PASS.trim())) {
						overAllBayResponse.setStatus(false);
						overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error for parallel scan failure
					}
				}
			}else{
				Ft.logger.error(String.format("[%s] : [PARALLEL_SCAN] : [TIMEOUT] - Parallel QR scanning timed out after %d seconds.", getMyBayKey(), dutWaitTimeDurationMaxInSec));
				overAllBayResponse.setStatus(false);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error for timeout
			}
		 }else {
            Ft.logger.info(String.format("[%s] : [QR_CODE_SCAN_PROCESS] : [SEQUENTIAL] - Executing QR scan in sequential mode.", getMyBayKey()));
			for(int positionNo =1; positionNo<= maxDutSupported ; positionNo++) {
				BayResponse bayResponse  =  readMeterSerialOpticalProbe(positionNo);	 
				Ft.logger.info(String.format("[%s] : [SEQUENTIAL_SCAN_RESULT] : Position: %d, Status: %s, Response Data: %s", getMyBayKey(), positionNo, bayResponse.getStatus(), bayResponse.getResponseData()));
			   	String readQrMeterSerialNo = bayResponse.getResponseData();
			   	ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNo, readQrMeterSerialNo);
			   	
			   	if(!bayResponse.getStatus()) {
			   		Ft.logger.error(String.format("[%s] : [SEQUENTIAL_SCAN] : [FAILED] - QR scan failed for position: %d. Status: %s", getMyBayKey(), positionNo, bayResponse.getStatus()));
			   		overAllBayResponse.setStatus(false);
					overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error for sequential scan failure
			   	}
	    	}
    	}
    	Ft.logger.debug(String.format("[%s] : [QR_CODE_SCAN_PROCESS] : [EXIT] - QR code scanning process completed. Overall Status: %s", getMyBayKey(), overAllBayResponse.getStatus()));
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


	public BayResponse readMeterSerialOpticalProbe(int positionNum){
		Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_READ] : [ENTRY] - Reading meter serial number via optical probe for position: %d", getMyBayKey(), positionNum));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Default to false
		DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
		String portCname = "";
		
		TerminalProfileSetting terminalBayProfile = null;
        try {
            terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
            if (terminalBayProfile == null) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [CONFIG_ERROR] - Terminal profile setting not found for bay key: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY));
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
                return bayResponse;
            }
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [DB_ERROR] - Failed to retrieve terminal profile for bay: %s. Error: %s", getMyBayKey(), ConstantConveyor.FT_BAY_KEY, e.getMessage()), e);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
            return bayResponse;
        }

		String deviceId = terminalBayProfile.getTerminalId()+ terminalBayProfile.getClusterId()+
				terminalBayProfile.getBayId()+
				ConstantConveyor.DEVICE_TYPE_DUT+
				String.format("%02d", positionNum);

		Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [DEVICE_ID] - Device ID: %s for Position: %d", getMyBayKey(), deviceId, positionNum));
		
		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		DeviceSetting deviceSetting = null;
        try {
            deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
            if (deviceSetting == null) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [CONFIG_ERROR] - Device setting not found for device ID: %s at position %d", getMyBayKey(), deviceId, positionNum));
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
                return bayResponse;
            }
            portCname = deviceSetting.getCanName();
            Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [PORT_INFO] - Port CName: %s, PortName: %s for Position: %d", getMyBayKey(), portCname, deviceSetting.getPortName(), positionNum));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [DEVICE_MANAGER_ERROR] - Failed to get device setting for ID: %s. Error: %s", getMyBayKey(), deviceId, e.getMessage()), e);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
            return bayResponse;
        }
		
		setSequencePathId("p1"); // Set for this step
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_10,
				ConstantConveyor.DEVICE_TYPE_DUT,
				getSequencePathId(),
				"" + positionNum,
				deviceSetting.getPortName(),
				portCname,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME,
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		int serialNo=StateExecutorController.addToTestStatusGui(testIntefaceStatus);
		testIntefaceStatus.setSerialNo(String.valueOf(serialNo));
		Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus to GUI for position %d. SerialNo: %s", getMyBayKey(), positionNum, serialNo));

		SpmDut spManager = null;
        try {
            if(ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) { // Original code used ProconFeatureEnable, retaining.
                spManager = devSysEnergyMeter.serialPortInitV2(deviceSetting);
            } else {
                spManager = devSysEnergyMeter.serialPortInit(portCname); // Old method
            }
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [SERIAL_PORT_EXCEPTION] - Exception during serial port initialization for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
            spManager = null; // Ensure spManager is null on exception
        }
		
		if(spManager == null){
			testIntefaceStatus.setSerialStatus(ConstantConveyor.COMM_ACCESS_FAILED);
			testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [INIT_FAILED] - Serial port initialization failed for position %d.", getMyBayKey(), positionNum));
			return bayResponse; // Return failed response
		}else{
			testIntefaceStatus.setSerialStatus("Success");
			bayResponse.setStatus(true); // Initial success for communication
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);
            Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [INIT_SUCCESS] - Serial port initialized successfully for position %d.", getMyBayKey(), positionNum));
		}

		boolean status = checkAckForCommands(); // This method needs actual implementation
        if (!status) {
            Ft.logger.warn(String.format("[%s] : [OPTICAL_PROBE_READ] : [ACK_CHECK_FAILED] - Acknowledgment check failed for position %d. This might indicate communication issues.", getMyBayKey(), positionNum));
            // Decide if this should immediately fail the process or just be a warning
        }

        try {
		    devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
            Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [UNLOCK_CMD_SENT] - Device unlock command sent for position %d.", getMyBayKey(), positionNum));
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [UNLOCK_CMD_ERROR] - Failed to send device unlock command for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
            bayResponse.setStatus(false);
            if (spManager != null) spManager.disconnectDut(); // Disconnect on failure
            return bayResponse;
        }
		
		PalletTrackerController palletTracker = new PalletTrackerController();
		String selectedBayTypeKey = myBayKey;
		String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
		PalletManage myPalletManage = null;
        try {
            myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
            if (myPalletManage == null) {
                Ft.logger.warn(String.format("[%s] : [OPTICAL_PROBE_READ] : [PALLET_NOT_FOUND] - Pallet distinct ID: %s not found in DB or map. Meter will still be processed but not linked to a pallet in DB.", getMyBayKey(), myPalletDistinctId));
                // Do not return false immediately here if meter read can still proceed without pallet association
            }
        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [DB_ERROR] - Exception while fetching pallet details for ID: %s. Error: %s", getMyBayKey(), myPalletDistinctId, e.getMessage()), e);
            // Decide if this DB read error should fail the overall process
        }

		// 2. Read and Map Serial Number of all meters
		if (status) { // This `status` comes from `checkAckForCommands()`. Ensure it reflects actual comm status.
			Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [COMM_CHECK_SUCCESS] - Communication check success for position: %d", getMyBayKey(), positionNum));
			
			Map<String, Object> result = new HashMap<>();
			testType = "FT"; // Class member
			testCaseName = ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME; // Class member

			try {
				result = devSysEnergyMeter.readSerialNumOfMeter(positionNum, spManager); 
				status = (boolean) result.getOrDefault("status", false); // Default to false if "status" key is missing
                meterSerialNumber = (String) result.getOrDefault("meterSerialNumber", "N/A"); // Class member
			} catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [SERIAL_READ_ERROR] - Failed to read serial number for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
                status = false; // Mark failure
                meterSerialNumber = "ERROR_READ";
            }

			if (status) {
				testIntefaceStatus.setDeviceResponseStatus("Success");
				bayResponse.setStatus(true);
				resultStatus = ConstantReport.REPORT_POPULATE_PASS; // Class member
                Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_READ] : [SERIAL_READ_SUCCESS] - Serial number read for position %d: %s", getMyBayKey(), positionNum, meterSerialNumber));
			}else{
				testIntefaceStatus.setDeviceResponseStatus("Failed");
				bayResponse.setStatus(false);
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL; // Class member
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [SERIAL_READ_FAILED] - Failed to read serial number for position %d. Status: %s", getMyBayKey(), positionNum, resultStatus));
			}

			resultValue = resultStatus; // Class member: Confirm if resultValue is intended to always be same as resultStatus here.

			testIntefaceStatus.setDeviceResponseData("Dut S.N= " + meterSerialNumber);

            try {
                palletTracker.addMetersToPallet(getMyBayKey(),meterSerialNumber,positionNum);
                Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [ADD_METER_DB] - Meter %s added to pallet for position %d.", getMyBayKey(), meterSerialNumber, positionNum));
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [ADD_METER_DB_ERROR] - Failed to add meter %s to pallet DB for position %d. Error: %s", getMyBayKey(), meterSerialNumber, positionNum, e.getMessage()), e);
                // Decide if this DB write error should make bayResponse.setStatus(false)
            }
            
            try {
                palletTracker.addResultToMeter(positionNum, resultStatus,  resultValue,  getMyBayKey(),  testType,  testCaseName );			
                Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [ADD_RESULT_DB] - Result added to meter for position %d. Status: %s", getMyBayKey(), positionNum, resultStatus));
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [ADD_RESULT_DB_ERROR] - Failed to add result to meter DB for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
                // Decide if this DB write error should make bayResponse.setStatus(false)
            }
            
            // UI Update: Update dashboard serial number (should be on JavaFX Application Thread)
            final String finalMeterSerialNumber = meterSerialNumber;
            Platform.runLater(() -> {
                if (ConveyorDataManager.getDashboardObject() != null) {
                    ConveyorDataManager.getDashboardObject().updateSerialByBayAndPosition(getMyBayKey(),positionNum, finalMeterSerialNumber);
                    Ft.logger.debug(String.format("[%s] : [UI_UPDATE] : Dashboard serial updated for position %d: %s", getMyBayKey(), positionNum, finalMeterSerialNumber));
                } else {
                    Ft.logger.warn(String.format("[%s] : [UI_UPDATE_WARNING] : Dashboard object is null, cannot update serial for position %d.", getMyBayKey(), positionNum));
                }
            });

			testIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(testIntefaceStatus);
		} else {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [COMM_CHECK_FAILED] - Communication check failed or prior step failed for position: %d. Skipping serial read.", getMyBayKey(), positionNum));
			bayResponse.setStatus(false); // Propagate failure
			if (spManager != null) spManager.disconnectDut();
			return bayResponse; 
		}

		// 4.6 Relay ON
		if (status) { // This `status` is from the serial number read
			Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_READ] : [RELAY_ON_CMD] - Sending Relay ON command for position: %d", getMyBayKey(), positionNum));

			setSequencePathId("p2"); // Set for this step

			resultValue = ""; // Class member - re-setting for this step
			testType = "FT"; // Class member
			testCaseName = ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME; // Class member

			TestInterfaceStatus opticalReaderTestIntefaceStatus = new TestInterfaceStatus( // Use `opticalReaderTestIntefaceStatus` not `testIntefaceStatus`
					getMyBayKey(),
					getMyBaySeqId(),
					ConstantConveyor.DEVICE_TYPE_DUT,
					getSequencePathId(),
					"" + positionNum,
					deviceSetting.getPortName(), // Use actual port name
					portCname,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					testCaseName,
					ConstantConveyor.COMM_EXECUTION_STATUS_INP
					);

			serialNo=StateExecutorController.addToTestStatusGui(opticalReaderTestIntefaceStatus);
			opticalReaderTestIntefaceStatus.setSerialNo(String.valueOf(serialNo));
            Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus for Relay ON. Position %d. SerialNo: %s", getMyBayKey(), positionNum, serialNo));

            boolean relayCommandStatus = false;
            try {
                relayCommandStatus = devSysEnergyMeter.sendRelayOnCommand(spManager);
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [RELAY_ON_ERROR] - Failed to send Relay ON command for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
                relayCommandStatus = false;
            }

			if(relayCommandStatus){
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
				resultStatus = ConstantReport.REPORT_POPULATE_PASS; // Class member
				bayResponse.setStatus(true);
                Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_READ] : [RELAY_ON_SUCCESS] - Relay ON command successful for position %d.", getMyBayKey(), positionNum));
			}else{
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Failed");
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL; // Class member
				bayResponse.setStatus(false);
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [RELAY_ON_FAILED] - Relay ON command failed for position %d.", getMyBayKey(), positionNum));
			}

            try {
                palletTracker.addResultToMeter(positionNum, resultStatus,  resultValue,  getMyBayKey(),  testType,  testCaseName ); // resultValue is empty here.
            } catch (Exception e) {
                Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [ADD_RELAY_RESULT_DB_ERROR] - Failed to add relay result to meter DB for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
            }

			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			BayUtils.delay(1000); // Blocking delay, ensure this runs in background thread
		} else {
            Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [RELAY_ON_SKIPPED] - Relay ON command skipped for position %d due to prior failure (Serial Number Read Failed).", getMyBayKey(), positionNum));
			bayResponse.setStatus(false);
		}
		//=================================================================================================================

        try {
		    spManager.disconnectDut();
		    Ft.logger.debug(String.format("[%s] : [OPTICAL_PROBE_READ] : [DUT_DISCONNECT] - DUT disconnected for position %d.", getMyBayKey(), positionNum));
		} catch (Exception e) {
		    Ft.logger.error(String.format("[%s] : [OPTICAL_PROBE_READ] : [DUT_DISCONNECT_ERROR] - Exception during DUT disconnect for position %d. Error: %s", getMyBayKey(), positionNum, e.getMessage()), e);
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			bayResponse.setStatus(true); // Overriding status for happy path simulation
			Ft.logger.debug(String.format("[%s] : [SIMULATION] : Optical probe serial read status overridden to TRUE for position %d.", getMyBayKey(), positionNum));
		}
		
		Ft.logger.info(String.format("[%s] : [OPTICAL_PROBE_READ] : [EXIT] - Optical probe serial number read process completed for position %d. Final status: %s", getMyBayKey(), positionNum, bayResponse.getStatus()));
		return bayResponse;
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

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}
	
	public String getMyBaySeqId() {
		return myBaySeqId;
	}
	public void setMyBaySeqId(String myBaySeqId) {
		this.myBaySeqId = myBaySeqId;
	}
}
