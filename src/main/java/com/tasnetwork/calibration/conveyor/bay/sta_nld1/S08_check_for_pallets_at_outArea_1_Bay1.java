package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

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

public class S08_check_for_pallets_at_outArea_1_Bay1 implements STA_NoLoadTestBay1State {

    private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
    private String palletSensorPortCname = ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET2;
    private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_014;

    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        boolean isPalletAvailableAt_outArea1;
        long startTime;
        boolean stableDetectionForNoPallet = false;

        while (!stableDetectionForNoPallet
                && (!StaNld_Bay1.isStopProcessRequestedStaNldBay1())
                && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            Map<String, Object> responseReturn = isPalletAvailableAt_outArea1();
            isPalletAvailableAt_outArea1 = (boolean) responseReturn.get("status");

            if (!isPalletAvailableAt_outArea1) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_outArea1();
                    isPalletAvailableAt_outArea1 = (boolean) responseReturn.get("status");

                    if (isPalletAvailableAt_outArea1) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for stable pallet time, confirm stability
                if (!isPalletAvailableAt_outArea1) {
                    stableDetectionForNoPallet = true;
                    StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Not Available at Out Area 1");
                }
            } else {
                StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : No pallet Available at Out Area 1");
                BayUtils.delay(1000);
            }
        }

        if (stableDetectionForNoPallet) {
            StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Not Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_014);
        }

        StaNld_Bay1.logger.info("S08_check_for_pallets_at_outArea_1 : Exit");
        return bayResponse;
    }

    // ==================================================================================================
    private Map<String, Object> isPalletAvailableAt_outArea1() {
        StaNld_Bay1.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET); // Minor
                                                                                                                        // Fix
                                                                                                                        // Applied,
                                                                                                                        // please
                                                                                                                        // verify
                                                                                                                        // on
                                                                                                                        // the
                                                                                                                        // placement
                                                                                                                        // of
                                                                                                                        // the
                                                                                                                        // scanner
                                                                                                                        // and
                                                                                                                        // adjust
                                                                                                                        // accordingly
                                                                                                                        // -
                                                                                                                        // SCT_NLT_BAY1_SNSR_PALLET2

        TestInterfaceStatus testInterfaceStatus = null;
        if (portInfo != null) {
            testInterfaceStatus = new TestInterfaceStatus(
                    ConstantConveyor.STA_NLD1_BAY_KEY,
                    "-",
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
                    "-",
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Executing",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
            StateExecutorController.addToTestStatusGui(testInterfaceStatus);

            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger
                    .debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        StaNld_Bay1.logger
                .debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : state : " + state);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
            status = true;
        }

        StaNld_Bay1.logger
                .debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : status : " + status);

        responseReturn.put("status", status);

        StaNld_Bay1.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : Exit");
        return responseReturn;
    }

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
