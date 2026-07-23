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
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S01_check_for_pallets_at_SCT_NLT_Bay1 implements STA_NoLoadTestBay1State {

    private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
    private String palletSensorPortCname = ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1;
    private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_001;
    private boolean logEnabled = true;

    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);

        ConveyorDataManager.getDashboardObject().removePalletFromBay(getMyBayKey());
        ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
                .updateBayMonitoringAllPalletsExistInBay(getMyBayKey());

        boolean isPalletAvailableAt_SCT_NLTBay1;
        long startTime;
        boolean stableDetection = false;

        while ((!stableDetection) && (!ConstantConveyor.ALL_LOOP_BREAK_FLAG)
                && (!StaNld_Bay1.isStopProcessRequestedStaNldBay1())) {
            Map<String, Object> responseReturn = isPalletAvailableAt_SCT_NLTBay1();
            isPalletAvailableAt_SCT_NLTBay1 = (boolean) responseReturn.get("status");

            if (isPalletAvailableAt_SCT_NLTBay1) {
                startTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_SCT_NLTBay1();
                    isPalletAvailableAt_SCT_NLTBay1 = (boolean) responseReturn.get("status");

                    if (!isPalletAvailableAt_SCT_NLTBay1) {
                        break; // Reset if detection is lost
                    }
                }

                // If detection lasted for stable pallet time, confirm stability
                if (isPalletAvailableAt_SCT_NLTBay1) {
                    stableDetection = true;
                }
            } else {
                if (logEnabled) {
                    StaNld_Bay1.logger
                            .info("S01_check_for_pallets_at_SCT_NLT_Bay1 : No pallet Available at SCT NLT Bay1");
                }
                BayUtils.delay(1000);
            }
            logEnabled = false;
        }

        if (stableDetection) {
            StaNld_Bay1.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay1 : Pallet Available");
            ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
                    .updateBayAllPalletsExistInBay(getMyBayKey(), true);
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            ConveyorDataManager.setSta1PalletsAllCleared(false);
        } else {
            StaNld_Bay1.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay1 : Pallet Not Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_001);
        }

        StaNld_Bay1.logger.info("S01_check_for_pallets_at_SCT_NLT_Bay1 : Exit");
        return bayResponse;
    }

    // ==================================================================================

    private Map<String, Object> isPalletAvailableAt_SCT_NLTBay1() {
        if (logEnabled) {
            StaNld_Bay1.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay1 : Entry");
        }
        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1);

        TestInterfaceStatus testInterfaceStatus = null;
        if (portInfo != null) {
            testInterfaceStatus = new TestInterfaceStatus(
                    ConstantConveyor.STA_NLD1_BAY_KEY,
                    "-",
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
                    "-",
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Executing",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
            StateExecutorController.addToTestStatusGui(testInterfaceStatus);
            if (logEnabled) {

                StaNld_Bay1.logger.debug("isPalletAvailableAt_SCT_NLTBay1 : getClusterId: " + portInfo.getClusterId()
                        + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId());

            }
        } else {
            if (logEnabled) {
                StaNld_Bay1.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay1 : Output port not found");
            }
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        if (logEnabled) {
            StaNld_Bay1.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay1 : state : " + state);
        }
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
            status = true;
        }
        if (logEnabled) {
            StaNld_Bay1.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay1 : status : " + status);
        }
        responseReturn.put("status", status);

        if (logEnabled) {
            StaNld_Bay1.logger.debug("S01_check_for_pallets_at_SCT_NLT_Bay1 : Exit");
        }
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
