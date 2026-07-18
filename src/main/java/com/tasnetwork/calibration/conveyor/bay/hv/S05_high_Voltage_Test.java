package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S05_high_Voltage_Test implements HvtBayState {

    private String sequencePathId = "p1";
    private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
    private String resultStatus = "";
    private String resultValue = "";
    private String testType = ConstantConveyor.HV_RESULT_KEY;
    private String testCaseName = ConstantConveyor.HV_RESULT_TEST_NAME;

    /**
     * Handles the high voltage test request for the conveyor bay.
     * @return BayResponse indicating the test status and error code
     */
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S05_high_Voltage_Test: Entry");

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ErrorCode.ERR_601);

        setSequencePathId("p1");
        setPalletAvailableTest_I_F_Status(null);

        if (StateExecutorController.simulateHvBayResult) {
        	waitForOneMinute();
            checkForInputPinsVoltageSensing();
            bayResponse.setStatus(true);
        } else {
            Map<String, Object> responseReturn = highVoltageTest();
            boolean highVoltageTestStatus = (boolean) responseReturn.get("status");

            if (highVoltageTestStatus) {
                Hv.logger.info("S05_high_Voltage_Test: High Voltage Test Successful");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ErrorCode.ERR_601); // Success
            } else {
                Hv.logger.info("S05_high_Voltage_Test: High Voltage Test Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ErrorCode.ERR_000); // Default failure code
            }
        }

        Hv.logger.info("S05_high_Voltage_Test: Exit");
        return bayResponse;
    }

    /**
     * Executes the high voltage test, including reading the HV meter, checking input pins, and updating the dashboard.
     * @return Map containing the test status
     */
    public Map<String, Object> highVoltageTest() {
        Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        float readVoltage = 0;

        setSequencePathId("HV1");
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                ConstantConveyor.HV_BAY_KEY,
                ConstantBayStateManage.BAY_HP_SEQ_10,
                ConstantConveyor.DEVICE_TYPE_VOLT_METER,
                getSequencePathId(),
                "-",
                "-",
                "-",
                ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                "HV Test",
                ConstantConveyor.COMM_EXECUTION_STATUS_INP
        );

        int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
        testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

        // Update dashboard to indicate testing in progress
        for (int position = 1; position <= ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET; position++) {
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    getMyBayKey(), position, MeterStatus.TESTING, ErrorCode.ERR_601);
        }

        // 1. Read HV meter - check voltage (e.g., 4KV)
        BayResponse bayResponse = readHvMeter();
        status = bayResponse.getStatus();

        if (status) {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: reading HV Meter: Success");
            readVoltage = bayResponse.getVoltValue();
            status = checkforEnoughVoltageApplied(readVoltage);
            if (status) {
                Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: HV Meter Voltage applied in range: Success");
                testInterfaceStatus.setDeviceResponseData("Success: Voltage=" + readVoltage + "V");
            } else {
                Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: HV Meter Voltage applied in range: Failed");
                testInterfaceStatus.setDeviceResponseData("Failed: Voltage=" + readVoltage + "V");
            }
        } else {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Failed to read HV Meter");
            testInterfaceStatus.setDeviceResponseData("Failed: Unable to read HV Meter");
        }

        testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
        // Update GUI with HV meter reading result
        StateExecutorController.updateTestStatusGui(testInterfaceStatus);

        // 2. Check meter inputs
        if (status) {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Voltage validation: Success");
            status = checkForInputPinsVoltageSensing();
        } else {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Voltage validation: Failed");
        }

        // 3. Wait 1 minute
        if (status) {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: checkForInputPinsVoltageSensing: Success");
            status = waitForOneMinute();
        } else {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: checkForInputPinsVoltageSensing: Failed");
        }

        // 4. Update Results
        if (status) {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Results update: Success");
            status = updateHvTestResults();
        } else {
            Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Failed to update results");
        }

        // Update dashboard with final results
        PalletTrackerController palletTracker = new PalletTrackerController();
        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(getMyBayKey());
        PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
        int meterPassedCount = 0;
        int meterFailedCount = 0;

        for (int position = 1; position <= ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET; position++) {
            MeterStatus meterStatus = status ? MeterStatus.PASSED : MeterStatus.FAILED;
            String errorCode = status ? ErrorCode.ERR_601 : ErrorCode.ERR_000;
            if (status) {
                meterPassedCount++;
            } else {
                meterFailedCount++;
            }
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    getMyBayKey(), position, meterStatus, errorCode);
            palletTracker.addMeterResultSummary(position, resultStatus, resultStatus, getMyBayKey(), testType, testCaseName);
        }

        // Update pallet management with pass/fail counts
        myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
        myPalletManage.setNoOfMeterPassed(meterPassedCount);
        myPalletManage.setNoOfMeterFailed(meterFailedCount);
        MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

        //palletTracker.refreshPalletManageDataFromDb();
        //palletTracker.refreshPalletManageDataFromDbv2("hv-highVoltageTest");
        if (StateExecutorController.simulateHvBayHappyPath) {
            status = true;
        }

        responseReturn.put("status", status);
        Hv.logger.debug("S05_high_Voltage_Test: highVoltageTest: Exit");
        return responseReturn;
    }

    /**
     * Updates the high voltage test results.
     * @return true if results are updated successfully
     */
    private boolean updateHvTestResults() {
        Hv.logger.debug("S05_high_Voltage_Test: updateHvTestResults: Entry");
        Hv.logger.debug("S05_high_Voltage_Test: updateHvTestResults: Exit");
        return true;
    }

    /**
     * Waits for one minute to allow voltage application.
     * @return true if wait is successful
     */
    private boolean waitForOneMinute() {
        Hv.logger.debug("S05_high_Voltage_Test: waitForOneMinute: Waiting for one minute");
        //BayUtils.delay(5000); // 5 seconds for testing; replace with 60000 for 1 minute
        
       
        
        int hvExecutionTime_InSec = DeviceDataManagerController.getConveyorConfigParsedKey().getHvExecutionTime_InSec();
        Hv.logger.debug("S05_high_Voltage_Test: hvExecutionTime_InSec: " + hvExecutionTime_InSec);
        
        for (int i = 0; i < hvExecutionTime_InSec; i++) {	
        	
        	Hv.logger.debug("S05_high_Voltage_Test: Waiting 1 min for IR: " + getMyBayKey());
            BayUtils.delay(1000);
            if(Hv.isStopProcessRequestedHvtBay()){
            	Hv.logger.debug("S05_high_Voltage_Test: in hv wait time stop requested: " + Hv.isStopProcessRequestedHvtBay());
            	break;
            }
        }
        
        Hv.logger.debug("S05_high_Voltage_Test: hvExecutionTime_InSec: Exit ");
        
        return true;
    }

    /**
     * Checks if the applied voltage meets the minimum requirement.
     * @param readVoltage The voltage read from the HV meter
     * @return true if voltage is sufficient
     */
    private boolean checkforEnoughVoltageApplied(float readVoltage) {
        Hv.logger.debug("S05_high_Voltage_Test: checkforEnoughVoltageApplied: Entry");
        boolean status = readVoltage > ConstantBayPortNameMapping.MINIMUM_REQD_VOLTAGE_HV_BAY;
        Hv.logger.debug("S05_high_Voltage_Test: checkforEnoughVoltageApplied: readVoltage: " + readVoltage + ", status: " + status);
        Hv.logger.debug("S05_high_Voltage_Test: checkforEnoughVoltageApplied: Exit");
        return status;
    }

    /**
     * Reads the high voltage meter to check applied voltage.
     * @return BayResponse containing the voltage value and status
     */
    public BayResponse readHvMeter() {
        Hv.logger.debug("S05_high_Voltage_Test: readHvMeter: Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(false);

        TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.HV_BAY_KEY);
        String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
                terminalBayProfile.getBayId() + ConstantConveyor.DEVICE_TYPE_VOLT_METER +
                ConstantConveyor.COMMON_ID_FOR_ALL_POSITION;

        Hv.logger.debug("S05_high_Voltage_Test: readHvMeter: deviceId: " + deviceId);

        ConveyorDataManager deviceDataManager = new ConveyorDataManager();
        DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
        deviceSetting.getCanName();

        Elmeasure_MultiMeter elmeasureVoltMeter = new Elmeasure_MultiMeter();
		HV_ReadResult result = elmeasureVoltMeter.readElmeasureVoltMeter(deviceSetting, ConstantBayPortNameMapping.ERC_MEGA_OHM_METER_01_SLAVE_ID);
		if (result.isStatus()) {
		    float value = result.getValue();
		    Hv.logger.debug("S05_high_Voltage_Test: readHvMeter: Read Value: " + value);
		    bayResponse.setStatus(true);
		    bayResponse.setVoltValue(value);
		} else {
		    Hv.logger.debug("S05_high_Voltage_Test: readHvMeter: Failed to read value");
		}

        Hv.logger.debug("S05_high_Voltage_Test: readHvMeter: Exit");
        return bayResponse;
    }

    /**
     * Checks voltage sensing on input pins for all meters in the pallet.
     * Updates dashboard with individual meter results.
     * @return true if all input pins sense voltage
     */
    private boolean checkForInputPinsVoltageSensing() {
        Hv.logger.debug("S05_high_Voltage_Test: checkForInputPinsVoltageSensing: Entry");

        boolean status = true;
        PalletTrackerController palletTracker = new PalletTrackerController();
        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(getMyBayKey());
        PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

        String[] inputPins = new String[] {
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP1,
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP2,
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP3,
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP4,
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP5,
                ConstantBayPortNameMapping.HV_PORT_NAME_SRC_IP6
        };

        int meterPassedCount = 0;
        int meterFailedCount = 0;

        for (int i = 0; i < ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET; i++) {
            Map<String, Object> responseReturn = isVoltageSensedinHvSrcInput(inputPins[i]);
            boolean isVoltageSensed = (boolean) responseReturn.get("status");

            resultStatus = isVoltageSensed ? ConstantReport.REPORT_POPULATE_PASS : ConstantReport.REPORT_POPULATE_FAIL;
            resultValue = resultStatus;

            Hv.logger.debug("S05_high_Voltage_Test: checkForInputPinsVoltageSensing: Voltage sensed at pin: " + inputPins[i] + ", status: " + isVoltageSensed);

            // Update GUI with voltage sensing result
            TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                    getMyBayKey(),
                    ConstantBayStateManage.BAY_HP_SEQ_10,
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
                    "HV2",
                    String.valueOf(i + 1),
                    "-",
                    "-",
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    testCaseName,
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
            testInterfaceStatus.setDeviceResponseStatus(isVoltageSensed ? "Success" : "Failed");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);

            // Update dashboard with meter status
            MeterStatus meterStatus = isVoltageSensed ? MeterStatus.PASSED : MeterStatus.FAILED;
            String errorCode = isVoltageSensed ? ErrorCode.ERR_601 : ErrorCode.ERR_000;
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    getMyBayKey(), i + 1, meterStatus, errorCode);

            palletTracker.addResultToMeter(i + 1, resultStatus, resultValue, getMyBayKey(), testType, testCaseName);
            palletTracker.addMeterResultSummary(i + 1, resultStatus, resultStatus, getMyBayKey(), testType, testCaseName);

            if (isVoltageSensed) {
                meterPassedCount++;
            } else {
                meterFailedCount++;
                status = false; // Fail overall test if any pin fails
            }

            BayUtils.delay(50);
        }

        // Update pallet management with pass/fail counts
        myPalletManage.setNoOfMeterPresent(meterPassedCount + meterFailedCount);
        myPalletManage.setNoOfMeterPassed(meterPassedCount);
        myPalletManage.setNoOfMeterFailed(meterFailedCount);
        MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

        //palletTracker.refreshPalletManageDataFromDb();
        //palletTracker.refreshPalletManageDataFromDbv2("hv-checkForInputPinsVoltageSensing");
        Hv.logger.debug("S05_high_Voltage_Test: checkForInputPinsVoltageSensing: Exit");
        return status;
    }

    /**
     * Checks if voltage is sensed on a specific input pin.
     * @param inputPin The input pin to check
     * @return Map containing the voltage sensing status
     */
    private Map<String, Object> isVoltageSensedinHvSrcInput(String inputPin) {
        Hv.logger.debug("S05_high_Voltage_Test: isVoltageSensedinHvSrcInput: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(inputPin);

        if (portInfo != null) {
            Hv.logger.debug("PortId: " + portInfo.getPortId());
            Hv.logger.debug("ClusterId: " + portInfo.getClusterId());
            Hv.logger.debug("BayId: " + portInfo.getBayId());

            BayUtils bayUtils = new BayUtils();
            String state = bayUtils.getInputDataFromBayV2(portInfo);
            Hv.logger.debug("S05_high_Voltage_Test: isVoltageSensedinHvSrcInput: state: " + state);

            status = state.equals(Constant_IO_ActionMapping.ON);
            Hv.logger.debug("S05_high_Voltage_Test: isVoltageSensedinHvSrcInput: status: " + status);
        } else {
            Hv.logger.debug("S05_high_Voltage_Test: isVoltageSensedinHvSrcInput: Input port not found");
        }

        responseReturn.put("status", status);
        Hv.logger.debug("S05_high_Voltage_Test: isVoltageSensedinHvSrcInput: Exit");
        return responseReturn;
    }

    public String getSequencePathId() {
        return sequencePathId;
    }

    public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
        return palletAvailableTest_I_F_Status;
    }

    public void setSequencePathId(String sequencePathId) {
        this.sequencePathId = sequencePathId;
    }

    public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
        this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
    }
}