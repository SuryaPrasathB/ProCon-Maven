package com.tasnetwork.calibration.conveyor.bay.loading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_open_stop_latch_Loading_Bay implements LoadingBayState {

    /**
     * Opens the stop latch at the Loading Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Loading.logger.info("S05_open_stop_latch_Loading_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
     //   BayUtils.delay(2000);

        Map<String, Object> responseReturn = open_StopLatch_Loading_Bay();

        boolean openStopLatch_Loading_Bay = (boolean) responseReturn.get("status");

        if (openStopLatch_Loading_Bay) {
        	Loading.logger.info("S05_open_stop_latch_Loading_Bay : Stop Latch Opened");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            BayUtils.delay(1000);
        } else {
            Loading.logger.info("S05_open_stop_latch_Loading_Bay : Failed to Open Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_LOADING_004);
        }

        Loading.logger.info("S05_open_stop_latch_Loading_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> open_StopLatch_Loading_Bay() {
        Loading.logger.debug("S05_open_stop_latch_Loading_Bay : loadingBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.LOADING_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Loading.logger.debug("PortId    : " + portInfo.getPortId());
            Loading.logger.debug("ClusterId : " + portInfo.getClusterId());
            Loading.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            Loading.logger.debug("S05_open_stop_latch_Loading_Bay : open_StopLatch_Loading_Bay : state : " + state);
            /*if (simulateLoadingBayHappyPath) {
                state = Constant_IO_ActionMapping.ON;
            }*/

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

    		if(StateExecutorController.simulateLoadingBayHappyPath){
    			status = true; 
    		}
     
            Loading.logger.debug("S05_open_stop_latch_Loading_Bay : open_StopLatch_Loading_Bay : status : " + status);

        } else {
            Loading.logger.debug("S05_open_stop_latch_Loading_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Loading.logger.debug("S05_open_stop_latch_Loading_Bay : loadingBay_StopLatch_Status : status : " + status); 

		responseReturn.put("status", status);
		
        Loading.logger.debug("S05_open_stop_latch_Loading_Bay : loadingBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
