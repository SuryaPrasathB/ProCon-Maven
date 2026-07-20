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

public class S03_close_the_fingerTip_Latch implements CommTestBayState {

    // ===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Comm.logger.info("S03_close_the_fingerTip_Latch : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        Map<String, Object> responseReturn = close_FingerTipLatch_CommBay();
        boolean close_FingerTipLatch_CommBay = (boolean) responseReturn.get("status");

        if (close_FingerTipLatch_CommBay) {
            Comm.logger.info("S03_close_the_fingerTip_Latch : Finger Tip Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Comm.logger.info("S03_close_the_fingerTip_Latch : Failed to Close Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_COMM_003);
        }

        Comm.logger.info("S03_close_the_fingerTip_Latch : Exit");
        return bayResponse;
    }
    // ============================================================================================================================================

    private Map<String, Object> close_FingerTipLatch_CommBay() {
        Comm.logger.debug("S03_close_the_fingerTip_Latch : commBay_FingerTipLatch_Status : Entry");

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
            Comm.logger.debug("S03_close_the_fingerTip_Latch : commBay_FingerTipLatch_Status : Output port not found");
            return responseReturn;
        }

        BayUtils bayUtils = new BayUtils();

        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId(),
                Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

        if (StateExecutorController.simulateCommBayHappyPath) {
            status = true;
        }

        Comm.logger.debug("S03_close_the_fingerTip_Latch : commBay_FingerTipLatch_Status : status : " + status);
        // ============================================================================================

        responseReturn.put("status", status);

        Comm.logger.debug("S03_close_the_fingerTip_Latch : commBay_FingerTipLatch_Status : Exit");
        return responseReturn;
    }
}
