package com.tasnetwork.calibration.conveyor.bay.comm;

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
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S01_check_for_pallet_at_Comm_Bay implements CommTestBayState {

    private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
    private String palletSensorPortCname = ConstantBayPortNameMapping.COMM_PORT_NAME_SNSR_PALLET;
    private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_COMM_001;

    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S01_check_for_pallet_at_Comm_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        boolean isPalletAvailableAt_CommBay;
        long startTime;
        boolean stableDetection = false;

        while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            Map<String, Object> responseReturn = isPalletAvailableAt_CommBay();
            isPalletAvailableAt_CommBay = (boolean) responseReturn.get("status");

            if (isPalletAvailableAt_CommBay) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_CommBay();
                    isPalletAvailableAt_CommBay = (boolean) responseReturn.get("status");

                    if (!isPalletAvailableAt_CommBay) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for stable pallet time, confirm stability
                if (isPalletAvailableAt_CommBay) {
                    stableDetection = true;
                }
            } else {
                Comm.logger.info("S01_check_for_pallet_at_Comm_Bay : No Pallet Available");
                BayUtils.delay(1000);
            }
        }

        if (stableDetection) {
            Comm.logger.info("S01_check_for_pallet_at_Comm_Bay : Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Comm.logger.info("S01_check_for_pallet_at_Comm_Bay : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_001);
        }

        Comm.logger.info("S01_check_for_pallet_at_Comm_Bay : Exit");
        return bayResponse;
    }

    // ===========================================================================================================================
    private Map<String, Object> isPalletAvailableAt_CommBay() {
        Comm.logger.debug("S01_check_for_pallet_at_Comm_Bay : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_SNSR_PALLET);

        TestInterfaceStatus testInterfaceStatus = null;
        if (portInfo != null) {
            testInterfaceStatus = new TestInterfaceStatus(
                    ConstantConveyor.COMMUNICATION_BAY_KEY,
                    "-",
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
                    "-",
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.COMM_PORT_NAME_SNSR_PALLET,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Executing",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
            StateExecutorController.addToTestStatusGui(testInterfaceStatus);

            Comm.logger.debug("PortId    : " + portInfo.getPortId());
            Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
            Comm.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Comm.logger.debug("S01_check_for_pallet_at_Comm_Bay : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        Comm.logger.debug("S01_check_for_pallet_at_Comm_Bay : state : " + state);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        if (StateExecutorController.simulateCommBayHappyPath) {
            status = true;
        }

        Comm.logger.debug("S01_check_for_pallet_at_Comm_Bay : status : " + status);

        responseReturn.put("status", status);

        Comm.logger.debug("S01_check_for_pallet_at_Comm_Bay : Exit");
        return responseReturn;
    }

    // ==========================================================================================================================
    public String getBayStateSequenceId() {
        return bayStateSequenceId;
    }

    public void setBayStateSequenceId(String bayStateSequenceId) {
        this.bayStateSequenceId = bayStateSequenceId;
    }

    public String getPalletSensorPortCname() {
        return palletSensorPortCname;
    }

    public String getFailStateErrorCode() {
        return failStateErrorCode;
    }

    public void setPalletSensorPortCname(String palletSensorPortCname) {
        this.palletSensorPortCname = palletSensorPortCname;
    }

    public void setFailStateErrorCode(String failStateErrorCode) {
        this.failStateErrorCode = failStateErrorCode;
    }
}
