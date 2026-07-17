package com.tasnetwork.calibration.conveyor.bay.ft;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.codehaus.groovy.runtime.StringGroovyMethods;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantDutDevSys;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ChannelQueueRequestProcessor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S08_functional_Test implements FtBayState {
    String sequencePathId = "p1";
    private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
    private String myBayKey = ConstantConveyor.FT_BAY_KEY;
    private String myBaySeqId = ConstantBayStateManage.BAY_HP_SEQ_10;
    boolean allPass = false;
    String presentBayKey = "";
    String meterSerialNumber = "";
    String resultStatus = "";
    String resultValue = "";
    String testType = "";
    String testCaseName = "";
    
    static final boolean rejectionQrNotAvailable = false;
    static Map<Integer, String> meterSerialNumberMap = new HashMap<>();
    static Map<Integer, String> errorCodeMap = new HashMap<>();

    /**
     * Handles the functional test request for the conveyor bay.
     * @return BayResponse indicating the test status and error code
     */
    @Override
    public BayResponse handleRequest() {
        Ft.logger.info("S08_functional_Test: Entry");
        
        errorCodeMap.clear();
        meterSerialNumberMap.clear();
        
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        setSequencePathId("p1");
        setPalletAvailableTest_I_F_Status(null);

        Map<String, Object> responseReturn = functionalTest();
        boolean functionalTestStatus = (boolean) responseReturn.get("status");
        Ft.logger.debug("S08_functional_Test: functionalTestStatus: " + functionalTestStatus);

        if (functionalTestStatus) {
            Ft.logger.info("S08_functional_Test: Functional Test Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success
        } else {
            Ft.logger.info("S08_functional_Test: Functional Test Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_010); // Failure
        }

        Ft.logger.info("S08_functional_Test: Exit");
        return bayResponse;
    }

    /**
     * Executes functional tests for all devices in parallel or sequentially based on configuration.
     * Updates dashboard with test results and pallet status.
     * @return Map containing the test status
     */
    public Map<String, Object> functionalTest() {
        Ft.logger.debug("S08_functional_Test: functionalTest: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", status);

        ParallelTaskManager ftManager = new ParallelTaskManager();
        int maxDeviceConnected = 6;
        boolean monitorAlreadyInitiated = false;

        // Parallel execution of functional tests
        if (ProconFeatureEnable.FT_EXECUTION_PROCESS_IN_PARALLEL) {
            for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
                ftManager.startFtProcess(positionNo);
                if (!monitorAlreadyInitiated) {
                    ftManager.monitorDutControlProcessTrigger();
                    monitorAlreadyInitiated = true;
                }
            }

            int dutWaitTimeDurationMaxInSec = 200;
            int dutWaitTimeCounter = 0;
            boolean dutAllProcessExecutionCompleted = false;

            PalletTrackerController palletTracker = new PalletTrackerController();
            String selectedBayTypeKey = getMyBayKey();
            ApplicationLauncher.logger.debug("S08_functional_Test: getPresentPalletAtBayMap: " + PalletTrackerController.getPresentPalletAtBayMap());
            String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
            ApplicationLauncher.logger.debug("S08_functional_Test: selectedBayTypeKey: " + selectedBayTypeKey);
            ApplicationLauncher.logger.debug("S08_functional_Test: myPalletDistinctId: " + myPalletDistinctId);
            

            PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
            if(myPalletManage!=null){
	            // Wait for all device tests to complete or timeout
	            while (!ProjectExecutionController.getUserAbortedFlag() &&
	                   dutWaitTimeCounter < dutWaitTimeDurationMaxInSec &&
	                   !dutAllProcessExecutionCompleted &&
	                   !ConstantConveyor.ALL_LOOP_BREAK_FLAG &&
	                   !Ft.isStopProcessRequestedFtBay()) {
	                Sleep(1000);
	                dutWaitTimeCounter++;
	                dutAllProcessExecutionCompleted = ftManager.isDutAllControlProcessCompleted();
	                Ft.logger.debug("S08_functional_Test: dutWaitTimeCounter: " + dutWaitTimeCounter + "/" + dutWaitTimeDurationMaxInSec + " : dutAllProcessExecutionCompleted: " + dutAllProcessExecutionCompleted);
	            }
	
	            if (ftManager.isDutAllControlProcessCompleted()) {
	                Ft.logger.debug("S08_functional_Test: All DUT tasks completed");
	                responseReturn.put("status", true);
	
	                for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
	                    Ft.logger.debug("S08_functional_Test: result position Id: " + positionNo + " : " + ftManager.getDutResultSummary(positionNo));
	                }
	
	                // Check if all devices passed
	                for (int positionNo = 1; positionNo <= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {
	                    String resultSummary = ftManager.getDutResultSummary(positionNo);
	                    Ft.logger.debug("S08_functional_Test: positionNo: " + positionNo + ", Result Summary: <" + resultSummary+">");
	                    //FunctionalTestBay.logger.debug("S08_functional_Test: positionNo: " + positionNo + ", Result Summary: " + resultSummary);
	                	
	                    if (!resultSummary.equals("P ")) {
	                        allPass = false;
	                        Ft.logger.debug("S08_functional_Test: positionNo: " + positionNo + ", All pass -false hit");
		                	
	                        break;
	                    } else {
	                        allPass = true;
	                        Ft.logger.debug("S08_functional_Test: positionNo: " + positionNo + ", All pass -true hit");
	                    }
	                }
	
	                int meterPassedCount = 0;
	                int meterFailedCount = 0;
	                MeterStatus meterStatus;
	
	                // Update dashboard with individual meter results
	                for (int positionNo = 1; positionNo <= ConstantConveyor.MAX_DEVICES_CONNECTED; positionNo++) {
	                    String resultSummary = ftManager.getDutResultSummary(positionNo);
	                    DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
	                    
	                    TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
	                    String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
	                            terminalBayProfile.getBayId() + ConstantConveyor.DEVICE_TYPE_DUT + String.format("%02d", positionNo);
	                    
	                    ConveyorDataManager deviceDataManager = new ConveyorDataManager();
	                    DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
	                    String portCname = deviceSetting.getCanName();
	                    
	                    /*SpmDut spManager = null;
	                    if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
	                        spManager = devSysEnergyMeter.serialPortInitV2(deviceSetting);
	                    } else {
	                        spManager = devSysEnergyMeter.serialPortInit(portCname);
	                    }
	                    
	                    Map<String, Object> result = devSysEnergyMeter.readSerialNumOfMeter(positionNo, spManager);*/
	                    
	                    Ft.logger.debug("Position Error Code2:  Position Number: " + positionNo);
	                    Ft.logger.debug("S08_functional_Test: positionNo: " + positionNo + ", resultSummary: <" + resultSummary +">");
	                    if (resultSummary.equals("P ")) {
	                        resultStatus = "Pass";
	                        resultValue = "Pass";
	                        meterStatus = MeterStatus.PASSED;
	                        Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " , Pass Hit");
	                        meterPassedCount++;
	                    } else {
	                        resultStatus = "Fail";
	                        resultValue = "Fail";
	                        meterStatus = MeterStatus.FAILED;
	                        Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " , Fail Hit");
	                        meterFailedCount++;
	                    }
	                    Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " , meterPassedCount: " + meterPassedCount);
	                    Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " , meterFailedCount: " + meterFailedCount);
	                    
	                    testCaseName = ConstantConveyor.FT_RESULT_TEST_NAME;
	                    testType = ConstantConveyor.FT_RESULT_KEY;
	                    palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	
	                    Ft.logger.debug("Final Error Code Map: " + errorCodeMap);
	
	                    for (int i = 1; i <= 6; i++) {
	                        Ft.logger.debug("S08_functional_Test: Position Error Code: errorCodeMap: <" + errorCodeMap.getOrDefault(i, ErrorCode.ERR_000) + "> Position Number: " + i);
	                    }
	
	                    // Update dashboard with meter status and error code
	                    ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
	                            myBayKey, positionNo, meterStatus, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
	                    
	                    if (rejectionQrNotAvailable) {
	                    	 Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " Hit1");
		                    sendRejectionMeterUpdate(positionNo, meterSerialNumberMap.getOrDefault(positionNo, " "), resultStatus, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
						}
	                    Ft.logger.debug("S08_functional_Test:  Position: " + positionNo + " Hit2"); 
	                    PalletMeter responsePalletMeter = palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus, errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000));
	                    
	                    /*if(responsePalletMeter!=null){
	                    	myPalletManage.getPalletMeterList().removeIf(e->e.getId()==responsePalletMeter.getId());
	                    	myPalletManage.getPalletMeterList().add(responsePalletMeter);
	                    	FunctionalTestBay.logger.debug("Position Error Code2:  Position Number: " + positionNo);
	                        
	                    }*/
	                    
	                    
	                    //FunctionalTestBay.logger.debug("Position Error Code: " + errorCodeMap.getOrDefault(positionNo, ErrorCode.ERR_000) + " Position Number: " + positionNo);
	                }
	
	                // Update pallet management with pass/fail counts and save to database
	                PalletManage myPalletManage2 = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
	                myPalletManage2.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
	                myPalletManage2.setNoOfMeterPassed(meterPassedCount);
	                myPalletManage2.setNoOfMeterFailed(meterFailedCount);
	                MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage2);
	
	                meterPassedCount = 0;
	                meterFailedCount = 0;
	
	                Ft.logger.debug("S08_functional_Test: allPass: " + allPass);
	                responseReturn.put("status", allPass);
	
	                //palletTracker.refreshPalletManageDataFromDb();
	                //palletTracker.refreshPalletManageDataFromDbv2("functionalTest");
	                
	                
	            }else{
	            	ApplicationLauncher.logger.debug("S08_functional_Test: myPalletDistinctId: " + myPalletDistinctId + " not found");
	            }
            } else {
                Ft.logger.debug("S08_functional_Test: Timed out");
            }
        } else {
            // Sequential execution for each device
            
            for (int position = 1; position <= 6; position++) {
                responseReturn = functionalTestProcess(position);
                @SuppressWarnings("unused")
				boolean functionalTestProcessStatus = (boolean) responseReturn.get("status");
            }
        }

        Ft.logger.debug("S08_functional_Test: functionalTest: Exit");
        return responseReturn;
    }

    /**
     * Performs functional tests for a specific device at the given position.
     * Updates dashboard with test progress and results.
     * @param positionNum The position number of the device (1 to 6)
     * @return Map containing the test status
     */
    public Map<String, Object> functionalTestProcess(int positionNum) {
        Ft.logger.debug("S08_functional_Test: functionalTestProcess: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
        String portCname = "";

        TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
        String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
                terminalBayProfile.getBayId() + ConstantConveyor.DEVICE_TYPE_DUT + String.format("%02d", positionNum);

        Ft.logger.debug("functionalTestProcess: deviceId: " + deviceId + " : Position No: " + positionNum);

        ConveyorDataManager deviceDataManager = new ConveyorDataManager();
        DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
        portCname = deviceSetting.getCanName();

        Ft.logger.debug("functionalTestProcess: portCname: " + portCname + " : Position No: " + positionNum);
        Ft.logger.debug("functionalTestProcess: getPortName: " + deviceSetting.getPortName() + " : Position No: " + positionNum);

        setSequencePathId("p1");
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
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

        // Add test status to GUI
        int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
        testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

        SpmDut spManager = null;
        try{
	        if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
	            spManager = devSysEnergyMeter.serialPortInitV2(deviceSetting);
	        } else {
	            spManager = devSysEnergyMeter.serialPortInit(portCname);
	        }
	
	        if (spManager == null) {
	            testInterfaceStatus.setSerialStatus(ConstantConveyor.COMM_ACCESS_FAILED);
	            // Update GUI with communication failure
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_COMM_ACCESS);
	            Ft.logger.debug("functionalTestProcess: ERR_COMM_ACCESS:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            Ft.logger.debug("functionalTestProcess: exit : COMM_ACCESS_FAILED:   : Position No: " + positionNum);
	            return responseReturn;
	        } else {
	            testInterfaceStatus.setSerialStatus("Success");
	            // Update GUI with successful serial connection
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	        }
	
	        status = checkAckForCommands();
	        status =devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
	        Ft.logger.debug("S08_functional_Test: sendDeviceUnlockCommand : status : " + status);
	        PalletTrackerController palletTracker = new PalletTrackerController();
	        String selectedBayTypeKey = myBayKey;
	        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
	        MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
	
	        // Read and map serial number
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: Communication Check: Success: Position: " + positionNum);
	            testType = "FT";
	            testCaseName = ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME;
	
	            Map<String, Object> result = devSysEnergyMeter.readSerialNumOfMeter(positionNum, spManager);
	            status = (boolean) result.get("status");
	
	            if (status) {
	                meterSerialNumber = (String) result.get("meterSerialNumber");
	                Ft.logger.debug("functionalTestProcess: meterSerialNumber:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                
	                meterSerialNumberMap.put(positionNum, meterSerialNumber);
	                Ft.logger.debug("functionalTestProcess: meterSerialNumberMap:   : Position No: " + positionNum + ", meterSerialNumberMap: " + meterSerialNumberMap);
	                
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	            } else {
	            	meterSerialNumberMap.put(positionNum, "Serial No - Read Failed");
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	            }
	
	            resultValue = resultStatus;
	            testInterfaceStatus.setDeviceResponseData("Dut S.N= " + meterSerialNumber);
	
	            // Update dashboard to indicate testing in progress
	            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
	                    myBayKey, positionNum, MeterStatus.TESTING, ErrorCode.ERR_601);         
	
	            //palletTracker.addResultToMeter(positionNum, resultStatus, resultStatus, getMyBayKey(), testType, testCaseName);
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with serial number test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_SERIAL_NO);//ErrorCode.ERR_COMM);
	                Ft.logger.debug("functionalTestProcess: ERR_SERIAL_NO: Serial Number Read:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                //FunctionalTestBay.logger.debug("S08_functional_Test: Serial Number Read: Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        } else {
	            //status = false;
	            Ft.logger.debug("S08_functional_Test: Communication Check: Failed: Position: " + positionNum);
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_COMM);
	            Ft.logger.debug("functionalTestProcess: ERR_COMM:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            spManager.disconnectDut();
	            Ft.logger.debug("functionalTestProcess: exit : Communication Check : ERR_COMM:   : Position No: " + positionNum);
	            //return responseReturn;
	        }
	        
	        
	        // command to set default calibration
	        
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: Serial Number Read: Success: Position: " + positionNum);
	            
	            boolean executeSetDutToDefaultCalibrationTest = DeviceDataManagerController.getConveyorConfigParsedKey().isFtSetDutToDefaultCalibrationTest();
	            if(executeSetDutToDefaultCalibrationTest){
		            setSequencePathId("p2");
		            resultValue = "";
		            testType = "FT";
		            testCaseName = ConstantConveyor.DUT_OPTICAL_DEFAULT_CALIBRATION_CMD_RESULT_TEST_NAME;
		
		            testInterfaceStatus = new TestInterfaceStatus(
		                    getMyBayKey(),
		                    getMyBaySeqId(),
		                    ConstantConveyor.DEVICE_TYPE_DUT,
		                    getSequencePathId(),
		                    "" + positionNum,
		                    "-",
		                    portCname,
		                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
		                    testCaseName,
		                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		
		            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
		            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
		
		            status = devSysEnergyMeter.sendDefaultCalibrationCommand(spManager);
		            if (status) {
		                testInterfaceStatus.setDeviceResponseStatus("Success");
		                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
		                resultValue  = ConstantReport.REPORT_POPULATE_PASS;
		            } else {
		                testInterfaceStatus.setDeviceResponseStatus("Failed");
		                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
		            }
		
		            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
		            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		            // Update GUI with relay ON test result
		            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
		            if(status){
		            	BayUtils.delay(1000);
		            }else{
		            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RON);//ErrorCode.ERR_COMM);
		                Ft.logger.debug("functionalTestProcess: ERR_RON-1:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
		                //FunctionalTestBay.logger.debug("S08_functional_Test: Serial Number Read: Failed: Position: " + positionNum);
		                spManager.disconnectDut();
		                
		                
		                
		            }
		        }else{
		        	Ft.logger.debug("S08_functional_Test: skipping DUT_OPTICAL_DEFAULT_CALIBRATION_CMD, since config set: " + executeSetDutToDefaultCalibrationTest);
		        	
		        	status = true;
		        }
	            
	        } 
	
	        
	        
	
	        // Relay ON test
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: Serial Number Read: Success: Position: " + positionNum);
	            setSequencePathId("p2");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            status = devSysEnergyMeter.sendRelayOnCommand(spManager);
	            if (status) {
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with relay ON test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(status){
	            	BayUtils.delay(1000);
	            }else{
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RON);//ErrorCode.ERR_COMM);
	                Ft.logger.debug("functionalTestProcess: ERR_RON-1:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                //FunctionalTestBay.logger.debug("S08_functional_Test: Serial Number Read: Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	                
	                
	                
	            }
	            
	        }
	        
	        /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RON);//ErrorCode.ERR_COMM);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_RON:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: Serial Number Read: Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	/*        // Check pulses in meter
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: Relay ON: Success: Position: " + positionNum);
	            if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
	                responseReturn = checkForPulsesInMeterWithPlc(positionNum);
	            } else {
	                responseReturn = checkForPulsesInMeter(positionNum);
	            }
	            setSequencePathId("p2");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            boolean checkForPulsesInMeter = (boolean) responseReturn.get("status");
	            status = checkForPulsesInMeter;
	
	            if (status) {
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	            } else {
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with pulse check result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_PULSE);//ErrorCode.ERR_RON);gjhg
	                Ft.logger.debug("functionalTestProcess: ERR_PULSE:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                //FunctionalTestBay.logger.debug("S08_functional_Test: Relay ON: Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        }*/ 
	        
	        /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_PULSE);//ErrorCode.ERR_RON);gjhg
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_PULSE:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: Relay ON: Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	        // Relay OFF test
	        if(status) {
	           // Ft.logger.debug("S08_functional_Test: Meter Pulse Check: Success: Position: " + positionNum);
	        	Ft.logger.debug("S08_functional_Test: Relay ON: Success: Position: " + positionNum);
	        	
	            setSequencePathId("p4");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_OFF_CMD_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            if (StateExecutorController.simulateFtBayHappyPath) {
	                status = true;
	            } else {
	                status = devSysEnergyMeter.sendRelayOffCommand(spManager);
	            }
	
	            if (status) {
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with relay OFF test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            
	            if(!status){
	            	
	                	
	                    //status = false;
	                    errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_ROFF);
	                    Ft.logger.debug("functionalTestProcess: ERR_ROFF:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                    //FunctionalTestBay.logger.debug("S08_functional_Test: Meter Pulse Check: Failed: Position: " + positionNum);
	                    spManager.disconnectDut();
	                               
	                
	            	
	            
	            }
	        } 
	
	        // Read phase current (Relay OFF)
	        if(status){
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay OFF: Success: Position: " + positionNum);
	            BayUtils.delay(1000);
	            setSequencePathId("p5");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_OFF_READ_PHASE_CURRENT_RESULT_TEST_NAME;//READ_PHASE_CURRENT_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            BayResponse bayResponse = devSysEnergyMeter.sendReadPhaseCurrentCommandV2(spManager);
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay OFF: Position: " + positionNum + " Phase Current Value " + bayResponse.getCurrentValue());
	
	            if (bayResponse.getStatus()) {
	                if (StateExecutorController.simulateFtBayHappyPath) {
	                    status = true;
	                    bayResponse.setCurrentValue(0.0f);
	                }
	                if (Math.floor(bayResponse.getCurrentValue()) < 1.0f) {
	                    testInterfaceStatus.setDeviceResponseStatus("Success");
	                    resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                    status = true;
	                    testInterfaceStatus.setDeviceResponseData("ReadPhaseCurrent-" + bayResponse.getCurrentValue() + " A");
	                } else {
	                    testInterfaceStatus.setDeviceResponseStatus("Failed");
	                    resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                    status = false;
	                }
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                status = false;
	            }
	            resultValue = "ReadPhaseCurrent-" + bayResponse.getCurrentValue() + " A";
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with phase current test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            
	            if (!status) {
	            	status = false;
	                errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RelayOffPhaseCurrent);
	                Ft.logger.debug("functionalTestProcess: ERR_RelayOffPhaseCurrent:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                Ft.logger.debug("S08_functional_Test: Read phase current (Relay OFF): Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        } /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_ROFF);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_ROFF:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: functionalTestProcess: Relay OFF: Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	        // Read neutral current (Relay OFF)
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Reading Phase Current: Success: Position: " + positionNum);
	            BayUtils.delay(1000);
	            setSequencePathId("p6");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_OFF_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME;//READ_NEUTRAL_CURRENT_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            BayResponse bayResponse = devSysEnergyMeter.sendReadNeutralCurrentCommandV2(spManager);
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay OFF: Position: " + positionNum + " Neutral Current Value: " + bayResponse.getCurrentValue());
	
	            if (bayResponse.getStatus()) {
	                if (StateExecutorController.simulateFtBayHappyPath) {
	                    status = true;
	                    bayResponse.setCurrentValue(0.0f);
	                }
	                //if (Math.floor(bayResponse.getCurrentValue()) == 0.0f || bayResponse.getCurrentValue() > 0) {
	                if (Math.floor(bayResponse.getCurrentValue()) == 0.0f ){//|| bayResponse.getCurrentValue() > 0) { // commented on version d.8.6.7  by Gopi 13-jul-2025
	                    testInterfaceStatus.setDeviceResponseStatus("Success");
	                    resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                    status = true;
	                    testInterfaceStatus.setDeviceResponseData("ReadNeutralCurrent-" + bayResponse.getCurrentValue() + " A");
	                } else {
	                    testInterfaceStatus.setDeviceResponseStatus("Failed");
	                    resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                    status = false;
	                }
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                status = false;
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with neutral current test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RelayOffNeutralCurrent);
	                Ft.logger.debug("functionalTestProcess: ERR_RelayOffNeutralCurrent:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: relay Off-Reading Neutral Current: Validation Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        } /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_OffP);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_OffP:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: functionalTestProcess: Reading Phase Current: Validation Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	        // Relay ON test (second iteration)
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Reading Neutral Current: Success: Position: " + positionNum);
	            setSequencePathId("p7");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            status = devSysEnergyMeter.sendRelayOnCommand(spManager);
	
	            if (status) {
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with second relay ON test result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	                errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RON);
	                Ft.logger.debug("functionalTestProcess: ERR_RON-2:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay On-2: Validation Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        } /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_OffN);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_OffN:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: functionalTestProcess: Reading Neutral Current: Validation Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	        // Read phase current (Relay ON)
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay On: Success: Position: " + positionNum);
	            BayUtils.delay(1000);
	            setSequencePathId("p8");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_ON_READ_PHASE_CURRENT_RESULT_TEST_NAME;//READ_PHASE_CURRENT_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            BayResponse bayResponse = devSysEnergyMeter.sendReadPhaseCurrentCommandV2(spManager);
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay ON: Position: " + positionNum + " Phase Current Value " + bayResponse.getCurrentValue());
	
	            if (bayResponse.getStatus()) {
	                if (StateExecutorController.simulateFtBayHappyPath) {
	                    status = true;
	                }
	                //if (bayResponse.getCurrentValue() > 0.0f) {
	                float minAcceptedValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFtRelayOnPhaseCurrentMinAccepted();
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay ON: Phase Current: Position: " + positionNum + " ,minAcceptedValue " + minAcceptedValue);
	
	                if (bayResponse.getCurrentValue() > minAcceptedValue) {
	                    testInterfaceStatus.setDeviceResponseStatus("Success");
	                    resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                    status = true;
	                    testInterfaceStatus.setDeviceResponseData("ReadPhaseCurrent-" + bayResponse.getCurrentValue() + " A");
	                } else {
	                    testInterfaceStatus.setDeviceResponseStatus("Failed");
	                    resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                    status = false;
	                }
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                status = false;
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with phase current test result (Relay ON)
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RelayOnPhaseCurrent);
	                Ft.logger.debug("functionalTestProcess: ERR_RelayOnPhaseCurrent:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: Read phase current (Relay ON): Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        } /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RON);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_RON:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: functionalTestProcess: Relay On: Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	        
	        
	
	        // Read neutral current (Relay ON)
	        if (status) {
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Reading Phase Current: Success: Position: " + positionNum);
	            BayUtils.delay(1000);
	            setSequencePathId("p9");
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.RELAY_ON_READ_NEUTRAL_CURRENT_RESULT_TEST_NAME;//READ_NEUTRAL_CURRENT_RESULT_TEST_NAME;
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            BayResponse bayResponse = devSysEnergyMeter.sendReadNeutralCurrentCommandV2(spManager);
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay ON: Position: " + positionNum + " Neutral Current Value " + bayResponse.getCurrentValue());
	
	            if (bayResponse.getStatus()) {
	                if (StateExecutorController.simulateFtBayHappyPath) {
	                    status = true;
	                }
	                //if (bayResponse.getCurrentValue() > 0.0f) {
	                float minAcceptedValue = DeviceDataManagerController.getConveyorConfigParsedKey().getFtRelayOnNeutralCurrentMinAccepted();
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay ON: Neutral Current: Position: " + positionNum + " ,minAcceptedValue " + minAcceptedValue);
	                //if (bayResponse.getCurrentValue() > 0.0f) {
	                if (bayResponse.getCurrentValue() > minAcceptedValue) { 
	                    testInterfaceStatus.setDeviceResponseStatus("Success");
	                    resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                    status = true;
	                    testInterfaceStatus.setDeviceResponseData("ReadNeutralCurrent-" + bayResponse.getCurrentValue() + " A");
	                } else {
	                    testInterfaceStatus.setDeviceResponseStatus("Failed");
	                    resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                    status = false;
	                }
	            } else {
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	                status = false;
	            }
	
	            String ftResultStatus = "";
	            palletTracker.closePresentBay(getMyBayKey(), ftResultStatus);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with neutral current test result (Relay ON)
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_RelayOnNeutralCurrent);
	                Ft.logger.debug("functionalTestProcess: ERR_RelayOnNeutralCurrent:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                Ft.logger.debug("S08_functional_Test: functionalTestProcess: Relay On-Reading Neutral Current: Validation Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	            
	        } /*else {
	            status = false;
	            errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_OnP);
	            FunctionalTestBay.logger.debug("functionalTestProcess: ERR_OnP:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            FunctionalTestBay.logger.debug("S08_functional_Test: functionalTestProcess: Reading Phase Current: Validation Failed: Position: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	        
	        
	        // Check pulses in meter
	        if (status) {
	            //Ft.logger.debug("S08_functional_Test: Relay ON: Success: Position: " + positionNum);
	            Ft.logger.debug("S08_functional_Test: functionalTestProcess: Reading Neutral Current: Success: Position: " + positionNum);
	            resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME;
	            if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
	                responseReturn = checkForPulsesInMeterWithPlc(positionNum);
	            } else {
	                responseReturn = checkForPulsesInMeter(positionNum);
	            }
	            setSequencePathId("p2");
	           /* resultValue = "";
	            testType = "FT";
	            testCaseName = ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME;*/
	
	            testInterfaceStatus = new TestInterfaceStatus(
	                    getMyBayKey(),
	                    getMyBaySeqId(),
	                    ConstantConveyor.DEVICE_TYPE_DUT,
	                    getSequencePathId(),
	                    "" + positionNum,
	                    "-",
	                    portCname,
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
	
	            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	
	            boolean checkForPulsesInMeter = (boolean) responseReturn.get("status");
	            status = checkForPulsesInMeter;
	
	            if (status) {
	                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	                testInterfaceStatus.setDeviceResponseStatus("Success");
	            } else {
	                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
	                testInterfaceStatus.setDeviceResponseStatus("Failed");
	            }
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            // Update GUI with pulse check result
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	            if(!status){
	            	errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_PULSE);//ErrorCode.ERR_RON);gjhg
	                Ft.logger.debug("functionalTestProcess: ERR_PULSE:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	                //FunctionalTestBay.logger.debug("S08_functional_Test: Relay ON: Failed: Position: " + positionNum);
	                spManager.disconnectDut();
	            }
	        }
	
	        // Update final results
	        if (status) {
	        	Ft.logger.debug("S08_functional_Test: Meter Pulse Check: Success: Position: " + positionNum);
	            Ft.logger.debug("S08_functional_Test: Results update: Success: positionNum: " + positionNum);
	            status = updateFtResults();
	        } else {
	           // status = false;
	            //errorCodeMap.putIfAbsent(positionNum, ErrorCode.ERR_OnN);
	            //FunctionalTestBay.logger.debug("functionalTestProcess: ERR_OnN:   : Position No: " + positionNum + ", errorCodeMap: " + errorCodeMap);
	            Ft.logger.debug("S08_functional_Test: Failed to update results: positionNum: " + positionNum);
	            spManager.disconnectDut();
	        }
	
	/*        if (status) {
	            Ft.logger.debug("S08_functional_Test: Results update: Success-2: positionNum: " + positionNum);
	        } else {
	            //status = false;
	            //Ft.logger.debug("S08_functional_Test: Failed to update results-2: positionNum: " + positionNum);
	            spManager.disconnectDut();
	        }*/
	
	        spManager.disconnectDut();
        }catch (Exception e) {
            Ft.logger.error("S08_functional_Test: Exception1: " + e.getMessage());
            
            try{
            	if(spManager!=null){
            		spManager.disconnectDut();
            	}
            }catch (Exception e1) {
                Ft.logger.error("S08_functional_Test: Exception2: " + e1.getMessage());
            }
        }

        if (StateExecutorController.simulateFtBayHappyPath) {
            status = true;
        }

        responseReturn.put("status", status);
        Ft.logger.debug("S08_functional_Test: Error Code Map: " + errorCodeMap);
        Ft.logger.debug("S08_functional_Test: functionalTestProcess:%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Ft.logger.debug("S08_functional_Test: functionalTestProcess: Exit : positionNum: " + positionNum + " status : " + status);
        Ft.logger.debug("S08_functional_Test: functionalTestProcess:%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Ft.logger.debug("S08_functional_Test: functionalTestProcess:=============================================================");
        
        ApplicationLauncher.logger.debug("S08_functional_Test: functionalTestProcess:%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        ApplicationLauncher.logger.debug("S08_functional_Test: functionalTestProcess: Exit : positionNum: " + positionNum + " status : " + status);
        ApplicationLauncher.logger.debug("S08_functional_Test: functionalTestProcess:%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        ApplicationLauncher.logger.debug("S08_functional_Test: functionalTestProcess:=============================================================");
        return responseReturn;
    }

    /**
     * Updates functional test results.
     * @return true if results are updated successfully
     */
    private boolean updateFtResults() {
        Ft.logger.debug("S08_functional_Test: updateFtResults: Entry");
        Ft.logger.debug("S08_functional_Test: updateFtResults: Exit");
        return true;
    }

    /**
     * Checks for pulses in the meter at the specified position.
     * @param positionNum The position number of the device
     * @return Map containing the pulse check status
     */
    private Map<String, Object> checkForPulsesInMeter(int positionNum) {
        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
        IoPortInfo portInfo = BayUtils.getInputPortDetailsWithPositionNo(
                terminalBayProfile.getTerminalId(),
                terminalBayProfile.getClusterId(),
                terminalBayProfile.getBayId(), positionNum);

        if (portInfo != null) {
            Ft.logger.debug("PortId: " + portInfo.getPortId());
            Ft.logger.debug("ClusterId: " + portInfo.getClusterId());
            Ft.logger.debug("BayId: " + portInfo.getBayId());

            setSequencePathId("p3");

            TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    getMyBaySeqId(),
                    ConstantConveyor.DEVICE_TYPE_DUT,
                    getSequencePathId(),
                    "" + positionNum,
                    "-",
                    "-",
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME,//READ_NEUTRAL_CURRENT_RESULT_TEST_NAME,
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);

            int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

            BayUtils bayUtils = new BayUtils();
            String pulseCounter = "";
            ApplicationLauncher.logger.info("checkForPulsesInMeter-2: Entry-failed-debug");
            Boolean pulseCounterStarted = bayUtils.setInitiatePulseCounterOnCluster(
                    portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(), Constant_IO_ActionMapping.OFF);

            if (pulseCounterStarted) {
                testInterfaceStatus.setDeviceResponseStatus("Success");
            } else {
                testInterfaceStatus.setDeviceResponseStatus("Failed");
            }
            testInterfaceStatus.setDeviceResponseData("InitiatePulseCounterCmd");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Update GUI with pulse counter initiation status
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : pulseCounterStarted: " + pulseCounterStarted);

            if (pulseCounterStarted) {
			    int waitTimeInSec = 20;
			    testInterfaceStatus = new TestInterfaceStatus(
			            getMyBayKey(),
			            getMyBaySeqId(),
			            ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
			            getSequencePathId(),
			            "" + positionNum,
			            "-",
			            "-",
			            ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
			            "PulseCounterStatusCmd",
			            ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			    serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
			    testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

			    while (!ProjectExecutionController.getUserAbortedFlag() &&
			           waitTimeInSec != 0 &&
			           pulseCounter.isEmpty() &&
			           !Ft.isStopProcessRequestedFtBay()) {
			        Sleep(1000);
			        ApplicationLauncher.logger.info("checkForPulsesInMeter: Entry-failed-debug");
			        pulseCounter = bayUtils.getPulseCounterStatusFromBay(
			                portInfo.getClusterId(),
			                portInfo.getBayId(),
			                portInfo.getPortId());
			        waitTimeInSec--;
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : waitTimeInSec: " + waitTimeInSec);
			    }

			    if (ProjectExecutionController.getUserAbortedFlag()) {
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " user aborted");
			    }

			    if (waitTimeInSec == 0) {
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " Time out");
			    }
			} else {
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: setInitiatePulseCounterOnCluster: Position: " + positionNum + " failed");
			}

			if (StateExecutorController.simulateFtBayHappyPath) {
			    pulseCounter = "20";
			}

			if (!pulseCounter.isEmpty()) {
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : FT_EXPECTED_MIN_PULSE_COUNT: " + ConstantDutDevSys.FT_EXPECTED_MIN_PULSE_COUNT);
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : pulseCounter: " + pulseCounter);
			    if (Integer.parseInt(pulseCounter) >= ConstantDutDevSys.FT_EXPECTED_MIN_PULSE_COUNT) {
			        status = true;
			        testInterfaceStatus.setDeviceResponseData("PulseCounterStatusCmd-Cntr=" + pulseCounter);
			        testInterfaceStatus.setDeviceResponseStatus("Success");
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : pulseCounter expected success: ");
			    } else {
			        testInterfaceStatus.setDeviceResponseStatus("Failed");
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Position: " + positionNum + " : pulseCounter failed: ");
			    }
			}
			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			// Update GUI with pulse counter status
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
        } else {
            Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: Input port not found");
            return responseReturn;
        }

        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeter: " + positionNum + " status: " + status);
        responseReturn.put("status", status);
        Ft.logger.debug("S08_functional_Test: checkForPulsesInAllMeters: Exit");
        return responseReturn;
    }

    /**
     * Checks for pulses in the meter using PLC mode at the specified position.
     * @param positionNum The position number of the device
     * @return Map containing the pulse check status
     */
    private Map<String, Object> checkForPulsesInMeterWithPlc(int positionNum) {
        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
        IoPortInfo enablePortInfo = BayUtils.getOutputPortDetailsWithPortNamePrefixAndPositionNo(
                terminalBayProfile.getTerminalId(),
                terminalBayProfile.getClusterId(),
                terminalBayProfile.getBayId(), positionNum, ConstantBayPortNameMapping.FT_PORT_NAME_LDU_OUTPUT_ENABLE_PREFIX);

        if (enablePortInfo != null) {
            Ft.logger.debug("PortId: " + enablePortInfo.getPortId());
            Ft.logger.debug("ClusterId: " + enablePortInfo.getClusterId());
            Ft.logger.debug("BayId: " + enablePortInfo.getBayId());

            setSequencePathId("p3");

            TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    getMyBaySeqId(),
                    ConstantConveyor.DEVICE_TYPE_DUT,
                    getSequencePathId(),
                    "" + positionNum,
                    "-",
                    "-",
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    ConstantConveyor.LED_PULSE_CHECK_RESULT_TEST_NAME,//READ_NEUTRAL_CURRENT_RESULT_TEST_NAME,
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);

            int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

            BayUtils bayUtils = new BayUtils();
            String pulseStatus = Constant_IO_ActionMapping.OFF;
            String state = bayUtils.setOutputDataToPlcBay(
                    enablePortInfo.getClusterId(),
                    enablePortInfo.getBayId(),
                    enablePortInfo.getPortId(), Constant_IO_ActionMapping.ON);

            if (state.equals(Constant_IO_ActionMapping.ON)) {
                Ft.logger.debug("S08_functional_Test: setOutputDataToPlcBay: Position: " + positionNum + " : Success");
                testInterfaceStatus.setDeviceResponseStatus("Success");
            } else {
                Ft.logger.debug("S08_functional_Test: setOutputDataToPlcBay: Position: " + positionNum + " : Failed");
                testInterfaceStatus.setDeviceResponseStatus("Failed");
            }
            testInterfaceStatus.setDeviceResponseData("InitiatePulseCounterCmd");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            // Update GUI with pulse counter initiation status
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
            Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " : pulseCounterStarted: " + state);

            if (state.equals(Constant_IO_ActionMapping.ON)) {
			    int waitTimeInSec = 20;
			    testInterfaceStatus = new TestInterfaceStatus(
			            getMyBayKey(),
			            getMyBaySeqId(),
			            ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
			            getSequencePathId(),
			            "" + positionNum,
			            "-",
			            "-",
			            ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
			            "PulseCounterStatusCmd",
			            ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			    serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
			    testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

			    IoPortInfo statusCheckPortInfo = BayUtils.getInputPortDetailsWithPortNamePrefixAndPositionNo(
			            terminalBayProfile.getTerminalId(),
			            terminalBayProfile.getClusterId(),
			            terminalBayProfile.getBayId(), positionNum,
			            ConstantBayPortNameMapping.FT_PORT_NAME_LDU_STATUS_CHECK_PREFIX);

			    while (!ProjectExecutionController.getUserAbortedFlag() &&
			           waitTimeInSec != 0 &&
			           pulseStatus.equals(Constant_IO_ActionMapping.OFF) &&
			           !Ft.isStopProcessRequestedFtBay()) {
			        Sleep(1000);
			        pulseStatus = bayUtils.getInputDataFromPlcBay(statusCheckPortInfo);
			        waitTimeInSec--;
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " : waitTimeInSec: " + waitTimeInSec);
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " : pulseStatus: " + pulseStatus);
			    }

			    if (ProjectExecutionController.getUserAbortedFlag()) {
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " user aborted");
			    }

			    if (waitTimeInSec == 0) {
			        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " Time out");
			    }
			} else {
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: setInitiatePulseCounterOnCluster: Position: " + positionNum + " failed");
			}

			if (StateExecutorController.simulateFtBayHappyPath) {
			    String pulseCounter = "20";
			}

			if (pulseStatus.equals(Constant_IO_ActionMapping.ON)) {
			    status = true;
			    testInterfaceStatus.setDeviceResponseStatus("Success");
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " : pulseCounter expected success: ");
			} else if (pulseStatus.equals(Constant_IO_ActionMapping.OFF)) {
			    testInterfaceStatus.setDeviceResponseStatus("Failed");
			    Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Position: " + positionNum + " : pulseCounter failed: ");
			}

			testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			// Update GUI with pulse status
			StateExecutorController.updateTestStatusGui(testInterfaceStatus);
        } else {
            Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: Input port not found");
            return responseReturn;
        }

        Ft.logger.debug("S08_functional_Test: checkForPulsesInMeterWithPlc: " + positionNum + " status: " + status);
        responseReturn.put("status", status);
        Ft.logger.debug("S08_functional_Test: checkForPulsesInAllMeters: Exit");
        return responseReturn;
    }

    /**
     * Pauses execution for the specified duration.
     * @param timeInMsec Time to sleep in milliseconds
     */
    public void Sleep(int timeInMsec) {
        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {
            Ft.logger.error("Sleep: InterruptedException: " + e.getMessage());
        }
    }

    /**
     * Checks acknowledgment for commands sent to the device.
     * @return true if acknowledgment is received
     */
    private boolean checkAckForCommands() {
        Ft.logger.debug("S08_functional_Test: checkAckForCommands: Entry");
        boolean status = true;
        Ft.logger.debug("S08_functional_Test: checkAckForCommands: Exit");
        return status;
    }

    /**
     * Checks the status of the source pin for the functional test bay.
     * @return Map containing the source status
     */
    @SuppressWarnings("unused")
	private Map<String, Object> checkSourceStatus() {
        Ft.logger.debug("S08_functional_Test: checkAckForCommands: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = ftBay_SourceStatusPin_Status();
        boolean ftBay_SourceStatusPin_Status = (boolean) responseReturn.get("status");

        if (ftBay_SourceStatusPin_Status) {
            Ft.logger.debug("S08_functional_Test: checkSourceStatus: Hv Source Start: Ensured");
            status = true;
        } else {
            Ft.logger.debug("S08_functional_Test: checkSourceStatus: Hv Source Start: Not Started");
        }

        Ft.logger.debug("S08_functional_Test: checkAckForCommands: Exit");
        return responseReturn;
    }

    /**
     * Retrieves the status of the source pin for the functional test bay.
     * @return Map containing the source pin status
     */
    private Map<String, Object> ftBay_SourceStatusPin_Status() {
        Ft.logger.debug("S08_functional_Test: ftBay_SourceStatusPin_Status: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_SPARE_OP2);

        if (portInfo != null) {
            Ft.logger.debug("PortId: " + portInfo.getPortId());
            Ft.logger.debug("ClusterId: " + portInfo.getClusterId());
            Ft.logger.debug("BayId: " + portInfo.getBayId());
        } else {
            Ft.logger.debug("S08_functional_Test: ftBay_SourceStatusPin_Status: Output port not found");
        }

        BayUtils bayUtils = new BayUtils();
        String state = bayUtils.getInputDataFromBayV2(portInfo);
        Ft.logger.debug("S08_functional_Test: ftBay_SourceStatusPin_Status: state: " + state);

        status = state.equals(Constant_IO_ActionMapping.LOW) ? true : false;

        if (StateExecutorController.simulateFtBayHappyPath) {
            status = true;
        }

        Ft.logger.debug("S08_functional_Test: ftBay_SourceStatusPin_Status: status: " + status);
        responseReturn.put("status", status);
        Ft.logger.debug("S08_functional_Test: ftBay_SourceStatusPin_Status: Exit");
        return responseReturn;
    }
    
    /**
     * Sends an update to the rejection Python application's API.
     * This method will construct a curl command to update a specific meter's status
     * and reason on the rejection dashboard.
     *
     * @param positionNo The position number (meter ID) to update.
     * @param serialNo The serial number of the meter.
     * @param status The status of the meter (e.g., "PASS", "FAIL").
     * @param reason The reason for the status (e.g., error code).
     */
    public static void sendRejectionMeterUpdate(int positionNo, String serialNo, String status, String reason) {
    	
    	 Ft.logger.debug("sendRejectionMeterUpdate: Entry : positionNo: " + positionNo);
    	 Ft.logger.debug("sendRejectionMeterUpdate: Entry : positionNo: " + positionNo + " , serialNo :" + serialNo + " , status: " + status + ", status:" + status);
        new Thread(() -> {
        	Ft.logger.debug("sendRejectionMeterUpdate: Entry1 : positionNo: " + positionNo + " , serialNo :" + serialNo + " , status: " + status + ", status:" + status);
            
            try {
                // Construct the raw JSON payload
                String rawJsonPayload = String.format(
                    "{\"id\": %d, \"serialNo\": \"%s\", \"status\": \"%s\", \"reason\": \"%s\", \"timestamp\": \"%s\"}",
                    positionNo,
                    serialNo,
                    status,
                    reason,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                );

                // Escape internal double quotes and wrap in outer double quotes for curl's -d argument
                String jsonPayloadForCurl = "\"" + rawJsonPayload.replace("\"", "\\\"") + "\"";

                // Construct the curl command
                // Assuming curl.exe is in system PATH. If not, provide full path: "C:\\Windows\\System32\\curl.exe"
                String[] command = {
                    "curl",
                    "-X", "PUT",
                    "-H", "Content-Type: application/json",
                    "-d", jsonPayloadForCurl, // Use the properly quoted and escaped string
                    "http://127.0.0.1:5001/api/rejection_meters/" + positionNo // Target specific meter ID
                };

                ProcessBuilder pb = new ProcessBuilder(command);
                pb.inheritIO(); // Inherit I/O to see curl output in Java console

                Ft.logger.debug("Sending Rejection API Update: " + Arrays.toString(command));
                Process process = pb.start();
                int exitCode = process.waitFor(); // Wait for curl command to complete
                Ft.logger.debug("Curl command for rejection app exited with code: " + exitCode);

            } catch (IOException | InterruptedException e) {
                System.err.println("Error sending rejection meter update: " + e.getMessage());
                e.printStackTrace();
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
            }
            Ft.logger.debug("sendRejectionMeterUpdate: Exit : positionNo: " + positionNo);
        }).start();
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

    public String getMyBayKey() {
        return myBayKey;
    }

    public void setMyBayKey(String myBayKey) {
        this.myBayKey = myBayKey;
    }

    public String getMyBaySeqId() {
        return myBaySeqId;
    }

    public void setMyBaySeqId(String myBaySeqId) {
        this.myBaySeqId = myBaySeqId;
    }
}