package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S04_ensure_the_fingerTip_Latch_Closed_Bay1 implements STA_NoLoadTestBay1State {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        int try_count = 0;

        while (try_count <= 3 && !StaNld_Bay1.isStopProcessRequestedStaNldBay1() && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            Map<String, Object> responseReturn = sctNltBay1_FingerTipLatch_Status();
            String sctNltBay1_FingerTipLatch_Status = (String) responseReturn.get("status");

            if (sctNltBay1_FingerTipLatch_Status.equals(Constant_IO_ActionMapping.OPEN)) {
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager()
                        .setGroupedPalletsLockedImageDisplayOn(getMyBayKey(), true);
                ConveyorDataManager.getDashboardObject().getBayIndicatorManager().startTimeUpDisplay(getMyBayKey());

                if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
                    BayUtils bayUtils = new BayUtils();
                    responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
                    boolean set_motor_not_required = (boolean) responseReturn.get("status");
                    if (set_motor_not_required) {
                        StaNld_Bay1.logger.info("set_motor_not_required : Success");
                        bayResponse.setStatus(true);
                        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                    } else {
                        StaNld_Bay1.logger.info("Failed to set_motor_not_required ");
                        bayResponse.setStatus(false);
                        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
                    }
                }
                break;
            } else {
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_004);
                BayUtils.delay(1000);
                try_count++;
            }
        }

        StaNld_Bay1.logger.info("S04_ensure_the_fingerTip_Latch_Closed : Exit");
        return bayResponse;
    }
    // ============================================================================================================================================

    private Map<String, Object> sctNltBay1_FingerTipLatch_Status() {
        StaNld_Bay1.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : sctNltBay1_FingerTipLatch_Status : Entry");

        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_FINGER_TIP);

        TestInterfaceStatus testInterfaceStatus = null;
        if (portInfo != null) {
            testInterfaceStatus = new TestInterfaceStatus(
                    ConstantConveyor.STA_NLD1_BAY_KEY,
                    "-",
                    ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT,
                    "-",
                    "-",
                    portInfo.getPortId(),
                    ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_FINGER_TIP,
                    ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
                    "Executing",
                    ConstantConveyor.COMM_EXECUTION_STATUS_INP);
            StateExecutorController.addToTestStatusGui(testInterfaceStatus);

            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay1.logger.debug(
                    "S04_ensure_the_fingerTip_Latch_Closed : sctNltBay1_FingerTipLatch_Status : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.getInputDataFromBayV2(portInfo);

        state = state.equals(Constant_IO_ActionMapping.ON) ? Constant_IO_ActionMapping.OPEN
                : Constant_IO_ActionMapping.CLOSE;

        if (StateExecutorController.simulateSCTNLTBay1HappyPath) {
            state = Constant_IO_ActionMapping.OPEN;
        }

        StaNld_Bay1.logger
                .debug("S04_ensure_the_fingerTip_Latch_Closed : sctNltBay1_FingerTipLatch_Status : state : " + state);

        responseReturn.put("status", state);

        StaNld_Bay1.logger.debug("S04_ensure_the_fingerTip_Latch_Closed : sctNltBay1_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
