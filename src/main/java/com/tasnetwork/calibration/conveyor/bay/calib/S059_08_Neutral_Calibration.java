package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.CalibrationSummaryProcessor;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S059_08_Neutral_Calibration implements CalibrationBayState {
    private final CalibrationSummaryProcessor calibrationSummaryProcessor = CalibrationSummaryProcessor.getInstance();
    private String myBayKey = ConstantConveyor.CALIBRATION_BAY_KEY;
    private String resultStatus = "";
    private String resultValue = "";
    private String testType = ConstantConveyor.CALIBRATION_RESULT_KEY;
    private String testCaseName = ConstantConveyor.NEUTRAL_CALIB_RESULT_TEST_NAME;
    private boolean allPass = false;

    /**
     * Handles the neutral calibration test request for the conveyor bay.
     * @return BayResponse indicating the test status and error code
     */
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S059_08_Neutral_Calibration: Entry");

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ErrorCode.ERR_601);

        // Update dashboard to indicate testing in progress
        for (int position = 1; position <= ConstantConveyor.MAX_DEVICES_CONNECTED; position++) {
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    myBayKey, position, MeterStatus.TESTING, ErrorCode.ERR_601);
        }

        Map<String, Object> responseReturn = neutralCalibrationTask();
        boolean neutralCalibrationStatus = (boolean) responseReturn.get("status");

        if (neutralCalibrationStatus) {
            Calib.logger.info("S059_08_Neutral_Calibration: Calibration Test Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ErrorCode.ERR_601);
            if(!Calib.isStopProcessRequestedCalibBay()){
	            for (int i = 1; i <= ConstantConveyor.NEUTRAL_CALIB_WAIT_TIME; i++) {
	                Calib.logger.info("S059_08_Neutral_Calibration: Waiting for Neutral Calib: T-" + (ConstantConveyor.NEUTRAL_CALIB_WAIT_TIME - i) + "secs");
	                BayUtils.delay(1000);
	            }
            }else{
            	Calib.logger.info("S059_08_Neutral_Calibration: Skipping wait time due to stop request");
            }

            /*if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {
                boolean toggle = false;
                long startTime = System.currentTimeMillis();
                StateExecutorController.getRef_btn_CalibRemove().setDisable(false);

                while (!ConstantConveyor.isCALIB_OPTICAL_REMOVED()) {
                    long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                    Platform.runLater(() -> {
                        StateExecutorController.ref_tf_CALIB_prompt.setText("Remove Optical Readers - " + elapsedTime + " secs");
                    });

                    if (toggle) {
                        turn_on_tower_lamp1();
                    } else {
                        turn_off_tower_lamp1();
                    }
                    toggle = !toggle;
                    CalibrationBay.logger.debug("S059_08_Neutral_Calibration: Waiting to remove Optical Readers");
                    BayUtils.delay(1000);
                }

                BayUtils.delay(2000);
                ConstantConveyor.setCALIB_OPTICAL_REMOVED(false);
                StateExecutorController.getRef_btn_CalibRemove().setDisable(true);
                Platform.runLater(() -> {
                    StateExecutorController.ref_tf_CALIB_prompt.clear();
                });
            }*/
        } else {
            Calib.logger.info("S059_08_Neutral_Calibration: Calibration Test Failed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ErrorCode.ERR_601);  // Do not reject at calibration bay
        }

        Calib.logger.info("S059_08_Neutral_Calibration: Exit");
        return bayResponse;
    }

    /**
     * Executes the neutral calibration task for all meters, updating the dashboard with results.
     * @return Map containing the test status
     */
    public Map<String, Object> neutralCalibrationTask() {
        Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        ParallelTaskManager dutManager = new ParallelTaskManager();
        PalletTrackerController palletTracker = new PalletTrackerController();
        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(myBayKey);
        PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

        int maxDeviceConnected = ConstantConveyor.MAX_DEVICES_CONNECTED;
        int meterPassedCount = 0;
        int meterFailedCount = 0;

        if (ProconFeatureEnable.CALIB_NEUTRAL_DUT_EXECUTION_PROCESS_IN_PARALLEL) {
            boolean monitorAlreadyInitiated = false;
            for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
                //dutManager.startDutCalibrationProcess(positionNo);
            		if(!Calib.isStopProcessRequestedCalibBay()){
		            	dutManager.startDutNeutralCalibrationProcess(positionNo);
		                if (!monitorAlreadyInitiated) {
		                    dutManager.monitorDutControlProcessTrigger();
		                    monitorAlreadyInitiated = true;
		                }
		            }else{
		       		 Calib.logger.debug("S059_08_Neutral_Calibration: isStopProcessRequestedCalibBay: hit");
		       		break;
		       	}
            }

            int dutWaitTimeDurationMaxInSec = 300;
            int dutWaitTimeCounter = 0;
            boolean dutAllProcessExecutionCompleted = false;

            while (!ProjectExecutionController.getUserAbortedFlag() &&
                   dutWaitTimeCounter < dutWaitTimeDurationMaxInSec &&
                   !dutAllProcessExecutionCompleted &&
                   !ConstantConveyor.ALL_LOOP_BREAK_FLAG &&
                   !Calib.isStopProcessRequestedCalibBay()) {
                Sleep(1000);
                dutWaitTimeCounter++;
                dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
                Calib.logger.debug("neutralCalibrationTask: dutWaitTimeCounter: " + dutWaitTimeCounter + "/" + dutWaitTimeDurationMaxInSec + " : dutAllProcessExecutionCompleted: " + dutAllProcessExecutionCompleted);
            }
            
            Calib.logger.debug("neutralCalibrationTask: dutWaitTimeDurationMaxInSec: " + dutWaitTimeDurationMaxInSec);
            Calib.logger.debug("neutralCalibrationTask: getUserAbortedFlag(): " + ProjectExecutionController.getUserAbortedFlag());
            Calib.logger.debug("neutralCalibrationTask: dutWaitTimeCounter: " + dutWaitTimeCounter);
            Calib.logger.debug("neutralCalibrationTask: ALL_LOOP_BREAK_FLAG: " + ConstantConveyor.ALL_LOOP_BREAK_FLAG);
            Calib.logger.debug("neutralCalibrationTask: isStopProcessRequestedCalibBay: " + Calib.isStopProcessRequestedCalibBay());

            if (dutManager.isDutAllControlProcessCompleted()) {
                Calib.logger.debug("neutralCalibrationTask: All DUT tasks completed");
                allPass = true;

                for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
                    String resultSummary = dutManager.getDutResultSummary(positionNo);
                    Calib.logger.debug("neutralCalibrationTask: result position Id: " + positionNo + " : " + resultSummary);

                    // Update GUI with calibration result
                    TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                            myBayKey,
                            "CALIB_SEQ",
                            ConstantConveyor.DEVICE_TYPE_DUT,
                            "NEUTRAL_CALIB",
                            String.valueOf(positionNo),
                            "-",
                            "-",
                            ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                            testCaseName,
                            ConstantConveyor.COMM_EXECUTION_STATUS_INP
                    );
                    int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
                    testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

                    if (resultSummary.equals(ConstantReport.RESULT_STATUS_PASS)) {
                        resultStatus = "Pass";
                        resultValue = "Pass";
                        meterPassedCount++;
                        testInterfaceStatus.setDeviceResponseStatus("Success");
                    } else {
                        resultStatus = "Fail";
                        resultValue = "Fail";
                        meterFailedCount++;
                        allPass = false;
                        testInterfaceStatus.setDeviceResponseStatus("Failed");
                    }

                    testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                    StateExecutorController.updateTestStatusGui(testInterfaceStatus);

                    // Update dashboard with meter status
                    MeterStatus meterStatus = resultStatus.equals("Pass") ? MeterStatus.PASSED : MeterStatus.FAILED;
                    String errorCode = resultStatus.equals("Pass") ? ErrorCode.ERR_601 : ErrorCode.ERR_000;
                    ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                            myBayKey, positionNo, meterStatus, errorCode);

                    palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, myBayKey, testType, testCaseName);
                    calibrationSummaryProcessor.addNeutralCalibrationResult(positionNo, resultStatus);
                }
            } else {
                Calib.logger.debug("neutralCalibrationTask: Timed out");
                allPass = false;
            }
        } else {
            if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
                DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
                boolean[] statuses = new boolean[maxDeviceConnected];
                for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
                    statuses[positionNo - 1] = devSysEnergyMeter.neutralCalibrationProcess(positionNo);
                    Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: Meter " + positionNo + " : status: " + statuses[positionNo - 1]);
                }

                allPass = true;
                for (int positionNo = 1; positionNo <= maxDeviceConnected; positionNo++) {
                    // Update GUI with calibration result
                    TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                            myBayKey,
                            "CALIB_SEQ",
                            ConstantConveyor.DEVICE_TYPE_DUT,
                            "NEUTRAL_CALIB",
                            String.valueOf(positionNo),
                            "-",
                            "-",
                            ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                            testCaseName,
                            ConstantConveyor.COMM_EXECUTION_STATUS_INP
                    );
                    int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
                    testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

                    if (statuses[positionNo - 1]) {
                        resultStatus = "Pass";
                        resultValue = "Pass";
                        meterPassedCount++;
                        testInterfaceStatus.setDeviceResponseStatus("Success");
                    } else {
                        resultStatus = "Fail";
                        resultValue = "Fail";
                        meterFailedCount++;
                        allPass = false;
                        testInterfaceStatus.setDeviceResponseStatus("Failed");
                    }

                    testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
                    StateExecutorController.updateTestStatusGui(testInterfaceStatus);

                    // Update dashboard with meter status
                    MeterStatus meterStatus = resultStatus.equals("Pass") ? MeterStatus.PASSED : MeterStatus.FAILED;
                    String errorCode = resultStatus.equals("Pass") ? ErrorCode.ERR_601 : ErrorCode.ERR_000;
                    ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                            myBayKey, positionNo, meterStatus, errorCode);

                    palletTracker.addMeterResultSummary(positionNo, resultStatus, resultValue, myBayKey, testType, testCaseName);
                    calibrationSummaryProcessor.addNeutralCalibrationResult(positionNo, resultStatus);
                }
            }
        }

        // Update pallet management
        myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
        myPalletManage.setNoOfMeterPassed(meterPassedCount);
        myPalletManage.setNoOfMeterFailed(meterFailedCount);
        MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

        if (StateExecutorController.simulateCalibBayHappyPath) {
            status = true;
        } else {
            //status = allPass;
        	int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
        	//status = allPass; 
        	if(meterFailedCount==maxDutSupported){ // updated by Gopi on version d0.8.5.7  -   07-July-2025
        		Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: all status: failed");
        		status = allPass;
        	}else{
        		Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: atleast one failed: still continuing testing");
        		status = true;
        	}
        }

        responseReturn.put("status", status);
        Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: status: " + status);
        Calib.logger.debug("S059_08_Neutral_Calibration: neutralCalibrationTask: Exit");
        return responseReturn;
    }

    /**
     * Turns on tower lamp 1.
     */
    private void turn_on_tower_lamp1() {
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);
        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            bayUtils.setOutputDataToBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(), Constant_IO_ActionMapping.OPEN);
        }
    }

    /**
     * Turns off tower lamp 1.
     */
    private void turn_off_tower_lamp1() {
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP2);
        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            bayUtils.setOutputDataToBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(), Constant_IO_ActionMapping.CLOSE);
        }
    }

    /**
     * Turns on tower lamp 2.
     */
    private void turn_on_tower_lamp2() {
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);
        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            bayUtils.setOutputDataToBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(), Constant_IO_ActionMapping.OPEN);
        }
    }

    /**
     * Turns off tower lamp 2.
     */
    private void turn_off_tower_lamp2() {
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_TWR_LAMP1);
        if (portInfo != null) {
            BayUtils bayUtils = new BayUtils();
            bayUtils.setOutputDataToBay(portInfo.getClusterId(), portInfo.getBayId(), portInfo.getPortId(), Constant_IO_ActionMapping.CLOSE);
        }
    }

    /**
     * Pauses execution for the specified time.
     * @param timeInMsec Time to sleep in milliseconds
     */
    public void Sleep(int timeInMsec) {
        try {
            Thread.sleep(timeInMsec);
        } catch (InterruptedException e) {
            Calib.logger.error("Sleep: InterruptedException: " + e.getMessage());
        }
    }

    public String getMyBayKey() {
        return myBayKey;
    }
}