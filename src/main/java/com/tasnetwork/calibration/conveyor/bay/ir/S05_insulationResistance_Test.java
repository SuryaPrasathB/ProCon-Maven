package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
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

public class S05_insulationResistance_Test implements IrtBayState {
    private String myBayKey = ConstantConveyor.IR_BAY_KEY;
    private HashMap<Integer, Float> irValues = new HashMap<>();
    private String testType = ConstantConveyor.IR_RESULT_KEY;
    private String testCaseName = ConstantConveyor.IR_RESULT_TEST_NAME;

    /**
     * Handles the insulation resistance test request for the conveyor bay.
     * @return BayResponse indicating the test status and error code
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S05_insulationResistance_Test: Entry");

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ErrorCode.ERR_601);

        if (StateExecutorController.simulateIrBayResult) {
        	int irExecutionTime_InSec = DeviceDataManagerController.getConveyorConfigParsedKey().getIrExecutionTime_InSec();
            Ir.logger.debug("S05_insulationResistance_Test: Sim: turn off meter read enable pin: Success");
            //for (int i = 0; i < 60; i++) {
            Ir.logger.debug("S05_insulationResistance_Test: Sim: IrExecutionTime_InSec: " + irExecutionTime_InSec);
            for (int i = 0; i < irExecutionTime_InSec; i++) {	
            	
                Ir.logger.debug("S05_insulationResistance_Test: Sim: Waiting 1 min for IR: " + myBayKey);
                BayUtils.delay(1000);
                if(Ir.isStopProcessRequestedIrtBay()){
                	Hv.logger.debug("S05_insulationResistance_Test: Sim: in Ir wait time stop requested: " + Ir.isStopProcessRequestedIrtBay());
                	break;
                }
            }
            Ir.logger.debug("S05_insulationResistance_Test: Sim: IrExecutionTime_InSec: Exit");
            populatePassResults();
            bayResponse.setStatus(true);
        } else {
            Map<String, Object> responseReturn = insulationResistanceTest();
            boolean insulationResistanceTestStatus = (boolean) responseReturn.get("status");

            if (insulationResistanceTestStatus) {
                Ir.logger.info("S05_insulationResistance_Test: Insulation Resistance Test Successful");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ErrorCode.ERR_601); // Success
            } else {
                Ir.logger.info("S05_insulationResistance_Test: Insulation Resistance Test Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ErrorCode.ERR_000); // Default failure code
            }
        }

        Ir.logger.info("S05_insulationResistance_Test: Exit");
        return bayResponse;
    }

    /**
     * Populates pass results for simulation mode and updates dashboard.
     */
    public void populatePassResults() {
        Ir.logger.debug("S05_insulationResistance_Test: populatePassResults: Entry");

        AtomicInteger meterPassedCount = new AtomicInteger(0);
        AtomicInteger meterFailedCount = new AtomicInteger(0);
        PalletTrackerController palletTracker = new PalletTrackerController();
        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(myBayKey);
        PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
        if(myPalletManage!=null) {
	        // Update dashboard and results for each meter
	        for (int positionNum = 1; positionNum <= ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET; positionNum++) {
	            String resultStatus = ConstantReport.REPORT_POPULATE_PASS;
	            String resultValue = ConstantReport.REPORT_POPULATE_PASS;
	
	            // Update GUI with pass result
	            TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
	                    myBayKey,
	                    "IR_SEQ",
	                    ConstantConveyor.DEVICE_TYPE_OHM_METER,
	                    "IR_SIM",
	                    String.valueOf(positionNum),
	                    "-",
	                    "-",
	                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	                    testCaseName,
	                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
	            );
	            int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
	            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
	            testInterfaceStatus.setDeviceResponseStatus("Success");
	            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
	            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
	
	            // Update dashboard with pass status
	            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
	                    myBayKey, positionNum, MeterStatus.PASSED, ErrorCode.ERR_601);
	
	            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, myBayKey, testType, testCaseName);
	            palletTracker.addMeterResultSummary(positionNum, resultStatus, resultStatus, myBayKey, testType, testCaseName);
	            meterPassedCount.incrementAndGet();
	        }
	
	        // Update pallet management
	        myPalletManage.setNoOfMeterPresent(meterPassedCount.get() + meterFailedCount.get());
	        myPalletManage.setNoOfMeterPassed(meterPassedCount.get());
	        myPalletManage.setNoOfMeterFailed(meterFailedCount.get());
	        MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
	        
	    }

        //palletTracker.refreshPalletManageDataFromDb();
        //palletTracker.refreshPalletManageDataFromDbv2("ir-populatePassResults");
        Ir.logger.debug("S05_insulationResistance_Test: populatePassResults: Exit");
    }

    /**
     * Executes the insulation resistance test for all meters, updating the dashboard with results.
     * @return Map containing the test status
     */
    public Map<String, Object> insulationResistanceTest() {
        Ir.logger.debug("S05_insulationResistance_Test: insulationResistanceTest: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        // Update dashboard to indicate testing in progress
        for (int position = 1; position <= ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET; position++) {
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    myBayKey, position, MeterStatus.TESTING, ErrorCode.ERR_601);
        }

        // 1. Enable meter read
        responseReturn = turn_on_ir_meter_read_enable();
        status = (boolean) responseReturn.get("status");

        // Update GUI with meter read enable status
        TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                myBayKey,
                "IR_SEQ",
                ConstantConveyor.DEVICE_TYPE_OHM_METER,
                "IR1",
                "-",
                "-",
                "-",
                ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                "IR Meter Read Enable",
                ConstantConveyor.COMM_EXECUTION_STATUS_INP
        );
        int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
        testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
        testInterfaceStatus.setDeviceResponseStatus(status ? "Success" : "Failed");
        testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
        StateExecutorController.updateTestStatusGui(testInterfaceStatus);
        
        

        // 2. Read and validate IR values for all meters
        if (status) {
            Ir.logger.debug("S05_insulationResistance_Test: turn on meter read enable pin: Success");
            readIrMeterForAllMeters();
            status = validateIrValuesForAllMeters();
        } else {
            Ir.logger.debug("S05_insulationResistance_Test: Failed to turn on meter read enable pin");
        }

        // 3. Disable meter read
        if (status) {
            Ir.logger.debug("S05_insulationResistance_Test: IR Values: Validation Success");
            responseReturn = turn_off_ir_meter_read_disable();
            status = (boolean) responseReturn.get("status");

            // Update GUI with meter read disable status
            testInterfaceStatus = new TestInterfaceStatus(
                    myBayKey,
                    "IR_SEQ",
                    ConstantConveyor.DEVICE_TYPE_OHM_METER,
                    "IR2",
                    "-",
                    "-",
                    "-",
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "IR Meter Read Disable",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));
            testInterfaceStatus.setDeviceResponseStatus(status ? "Success" : "Failed");
            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);
        } else {
            Ir.logger.debug("S05_insulationResistance_Test: IR Values: Validation failed");
        }

        // 4. Wait 1 minute and update results
        int irExecutionTime_InSec = DeviceDataManagerController.getConveyorConfigParsedKey().getIrExecutionTime_InSec();
        
        if (status) {
            Ir.logger.debug("S05_insulationResistance_Test: turn off meter read enable pin: Success");
            //for (int i = 0; i < 60; i++) {
            Ir.logger.debug("S05_insulationResistance_Test: IrExecutionTime_InSec: " + irExecutionTime_InSec);
            for (int i = 0; i < irExecutionTime_InSec; i++) {	
            	
                Ir.logger.debug("S05_insulationResistance_Test: Waiting 1 min for IR: " + myBayKey);
                BayUtils.delay(1000);
                if(Ir.isStopProcessRequestedIrtBay()){
                	Hv.logger.debug("S05_insulationResistance_Test: in Ir wait time stop requested: " + Ir.isStopProcessRequestedIrtBay());
                	break;
                }
            }
            Ir.logger.debug("S05_insulationResistance_Test: IrExecutionTime_InSec: Exit");
            status = updateIrTestResults();
        } else {
            Ir.logger.debug("S05_insulationResistance_Test: Failed to turn off meter read enable pin");
        }

        if (!status) {
            Ir.logger.debug("S05_insulationResistance_Test: Failed to update results");
        }

        if (StateExecutorController.simulateIrBayHappyPath) {
            status = true;
        }

        responseReturn.put("status", status);
        Ir.logger.debug("S05_insulationResistance_Test: insulationResistanceTest: Exit");
        return responseReturn;
    }

    /**
     * Updates the insulation resistance test results.
     * @return true if results are updated successfully
     */
    private boolean updateIrTestResults() {
        Ir.logger.debug("S05_insulationResistance_Test: updateIrTestResults: Entry");
        Ir.logger.debug("S05_insulationResistance_Test: updateIrTestResults: Exit");
        return true;
    }

    /**
     * Validates insulation resistance values for all meters and updates dashboard.
     * @return true if all meters pass validation
     */
    private boolean validateIrValuesForAllMeters() {
        Ir.logger.debug("S05_insulationResistance_Test: validateIrValuesForAllMeters: Entry");

        boolean status = true;
        float minimumIR = ConstantBayPortNameMapping.MINIMUM_IR_REQD_IR_BAY;
        AtomicInteger meterPassedCount = new AtomicInteger(0);
        AtomicInteger meterFailedCount = new AtomicInteger(0);
        PalletTrackerController palletTracker = new PalletTrackerController();
        String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(myBayKey);
        PalletManage myPalletManage = MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);

        for (Map.Entry<Integer, Float> entry : irValues.entrySet()) {
            int positionNum = entry.getKey();
            float irValue = entry.getValue();
            String resultStatus;
            String resultValue = String.valueOf(irValue);

            // Update GUI with IR value result
            TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus(
                    myBayKey,
                    "IR_SEQ",
                    ConstantConveyor.DEVICE_TYPE_OHM_METER,
                    "IR3",
                    String.valueOf(positionNum),
                    "-",
                    "-",
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    testCaseName,
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP
            );
            int serialNo = StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            testInterfaceStatus.setSerialNo(String.valueOf(serialNo));

            if (irValue > minimumIR) {
                Ir.logger.debug("validateIrValuesForAllMeters: Insulation Resistance of Meter " + positionNum + ": " + irValue + " meets requirement");
                resultStatus = ConstantReport.REPORT_POPULATE_PASS;
                meterPassedCount.incrementAndGet();
                testInterfaceStatus.setDeviceResponseStatus("Success");
                testInterfaceStatus.setDeviceResponseData("IR=" + irValue + " MOhm");
            } else {
                Ir.logger.debug("validateIrValuesForAllMeters: Insulation Resistance of Meter " + positionNum + ": " + irValue + " does not meet requirement");
                resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
                meterFailedCount.incrementAndGet();
                status = false; // Fail overall test if any meter fails
                testInterfaceStatus.setDeviceResponseStatus("Failed");
                testInterfaceStatus.setDeviceResponseData("IR=" + irValue + " MOhm");
            }

            testInterfaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
            StateExecutorController.updateTestStatusGui(testInterfaceStatus);

            // Update dashboard with meter status
            MeterStatus meterStatus = resultStatus.equals(ConstantReport.REPORT_POPULATE_PASS) ? MeterStatus.PASSED : MeterStatus.FAILED;
            String errorCode = resultStatus.equals(ConstantReport.REPORT_POPULATE_PASS) ? ErrorCode.ERR_601 : ErrorCode.ERR_000;
            ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
                    myBayKey, positionNum, meterStatus, errorCode);

            palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, myBayKey, testType, testCaseName);
            palletTracker.addMeterResultSummary(positionNum, resultStatus, resultStatus, myBayKey, testType, testCaseName);
        }

        // Update pallet management
        myPalletManage.setNoOfMeterPresent(meterPassedCount.get() + meterFailedCount.get());
        myPalletManage.setNoOfMeterPassed(meterPassedCount.get());
        myPalletManage.setNoOfMeterFailed(meterFailedCount.get());
        MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

        //palletTracker.refreshPalletManageDataFromDb();
        //palletTracker.refreshPalletManageDataFromDbv2("ir-validateIrValuesForAllMeters");
        Ir.logger.debug("S05_insulationResistance_Test: validateIrValuesForAllMeters: Exit");
        return status;
    }

    /**
     * Reads insulation resistance values for all meters.
     */
    private void readIrMeterForAllMeters() {
        Ir.logger.debug("S05_insulationResistance_Test: readIrMeterForAllMeters: Entry");

        TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.IR_BAY_KEY);
        String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
                terminalBayProfile.getBayId() + ConstantConveyor.DEVICE_TYPE_OHM_METER +
                ConstantConveyor.COMMON_ID_FOR_ALL_POSITION;

        Ir.logger.debug("S05_insulationResistance_Test: readIrMeterForAllMeters: deviceId: " + deviceId);

        String addressKey = terminalBayProfile.getClusterName() + "_" + terminalBayProfile.getBayName() + "_" + ConstantConveyor.COMMON_ID_FOR_ALL_POSITION;
        ArrayList<String> activeSlaveIdList = BayUtils.getFilteredClusterBayPositionNoAddressListMap()
                .get(ConstantConveyor.DEVICE_TYPE_OHM_METER).get(addressKey);

        Ir.logger.debug("S05_insulationResistance_Test: readIrMeterForAllMeters: slaveAddressList: " + activeSlaveIdList);

        // Initialize IR values
        int numMeters = ConstantBayPortNameMapping.NUM_OF_METERS_IN_ONE_PALLET;
        for (int meterNum = 1; meterNum <= numMeters; meterNum++) {
            irValues.put(meterNum, 0.0f);
        }

        ConveyorDataManager deviceDataManager = new ConveyorDataManager();
        DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);

        EIC_MegaOhmMeter eicMegaOhmMeter = new EIC_MegaOhmMeter();

        for (int i = 0; i < numMeters; i++) {
            String slaveId = activeSlaveIdList.get(i);
            IR_ReadResult result = eicMegaOhmMeter.readMegaOhmMeter(deviceSetting, slaveId);

            if (result.isStatus()) {
                float value = result.getValue();
                irValues.put(Integer.parseInt(slaveId), value);
                Ir.logger.debug("S05_insulationResistance_Test: METER " + (i + 1) + ": Read Value: " + value);
            } else {
                Ir.logger.debug("S05_insulationResistance_Test: METER " + (i + 1) + ": Failed to read value");
            }
            BayUtils.delay(5);
        }

        Ir.logger.debug("S05_insulationResistance_Test: readIrMeterForAllMeters: Exit");
    }

    /**
     * Enables the IR meter read by setting the output port.
     * @return Map containing the enable status
     */
    private Map<String, Object> turn_on_ir_meter_read_enable() {
        Ir.logger.debug("S05_insulationResistance_Test: turn_on_ir_meter_read_enable: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_IR_MTR_RAED_EN);

        if (portInfo != null) {
            Ir.logger.debug("PortId: " + portInfo.getPortId());
            Ir.logger.debug("ClusterId: " + portInfo.getClusterId());
            Ir.logger.debug("BayId: " + portInfo.getBayId());

            BayUtils bayUtils = new BayUtils();
            String state = bayUtils.setOutputDataToBay(
                    portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.ON);
            status = state.equals(Constant_IO_ActionMapping.ON);
            Ir.logger.debug("S05_insulationResistance_Test: turn_on_ir_meter_read_enable: status: " + status);
        } else {
            Ir.logger.debug("S05_insulationResistance_Test: turn_on_ir_meter_read_enable: Output port not found");
        }

        responseReturn.put("status", status);
        Ir.logger.debug("S05_insulationResistance_Test: turn_on_ir_meter_read_enable: Exit");
        return responseReturn;
    }

    /**
     * Disables the IR meter read by setting the output port.
     * @return Map containing the disable status
     */
    private Map<String, Object> turn_off_ir_meter_read_disable() {
        Ir.logger.debug("S05_insulationResistance_Test: turn_off_ir_meter_read_disable: Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_IR_MTR_RAED_EN);

        if (portInfo != null) {
            Ir.logger.debug("PortId: " + portInfo.getPortId());
            Ir.logger.debug("ClusterId: " + portInfo.getClusterId());
            Ir.logger.debug("BayId: " + portInfo.getBayId());

            BayUtils bayUtils = new BayUtils();
            String state = bayUtils.setOutputDataToBay(
                    portInfo.getClusterId(),
                    portInfo.getBayId(),
                    portInfo.getPortId(),
                    Constant_IO_ActionMapping.OFF);
            status = state.equals(Constant_IO_ActionMapping.OFF);
            Ir.logger.debug("S05_insulationResistance_Test: turn_off_ir_meter_read_disable: status: " + status);
        } else {
            Ir.logger.debug("S05_insulationResistance_Test: turn_off_ir_meter_read_disable: Output port not found");
        }

        responseReturn.put("status", status);
        Ir.logger.debug("S05_insulationResistance_Test: turn_off_ir_meter_read_disable: Exit");
        return responseReturn;
    }

    public String getMyBayKey() {
        return myBayKey;
    }

    public void setMyBayKey(String myBayKey) {
        this.myBayKey = myBayKey;
    }
}