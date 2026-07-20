package com.tasnetwork.calibration.conveyor.bay.comm;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S06_open_the_fingerTip_Latch implements CommTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S06_open_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = open_FingerTipLatch_CommBay();
        boolean open_FingerTipLatch_CommBay = (boolean) responseReturn.get("status");

        if (open_FingerTipLatch_CommBay) {
            Comm.logger.info("S06_open_the_fingerTip_Latch : Finger Tip Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Comm.logger.info("S06_open_the_fingerTip_Latch : Failed to Open Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_011);
        }

        Comm.logger.info("S06_open_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    // ============================================================================================================================================

    private Map<String, Object> open_FingerTipLatch_CommBay() {
        Comm.logger.debug("S06_open_the_fingerTip_Latch : commBay_FingerTipLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<String, Object>();
        responseReturn.put("status", false);
        // ============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.COMM_PORT_NAME_FINGER_TIP);

        if (portInfo != null) {
            Comm.logger.debug("PortId    : " + portInfo.getPortId());
            Comm.logger.debug("ClusterId : " + portInfo.getClusterId());
            Comm.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Comm.logger.debug("S06_open_the_fingerTip_Latch : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

        if (StateExecutorController.simulateCommBayHappyPath) {
            status = true;
        }
        Comm.logger.debug("S06_open_the_fingerTip_Latch : commBay_FingerTipLatch_Status : status : " + status);
        // ============================================================================================

        responseReturn.put("status", status);

        Comm.logger.debug("S06_open_the_fingerTip_Latch : commBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
