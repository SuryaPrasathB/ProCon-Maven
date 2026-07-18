package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S13_close_stop_latch_HVT_Bay implements HvtBayState {

	/**
     * Closes the stop latch at the HVT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
    	Hv.logger.info("S13_close_stop_latch_HV_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(2000);

        Map<String, Object> responseReturn = close_StopLatch_HV_Bay();

        boolean closeStopLatch_HV_Bay = (boolean) responseReturn.get("status");

        if (closeStopLatch_HV_Bay) {
        	Hv.logger.info("S13_close_stop_latch_HV_Bay : Stop Latch Closed");
        	 ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),false);
        	BayUtils.delay(1000);
        	
        	BayUtils bayUtils = new BayUtils();			
        	responseReturn = bayUtils.set_motor_not_required(getMyBayKey());

        	boolean set_motor_not_required = (boolean)responseReturn.get("status");  

        	if (set_motor_not_required) {
        		Hv.logger.info("set_motor_not_required : Success");
        		bayResponse.setStatus(true);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        	} 
        	else {
        		Hv.logger.info("Failed to set_motor_not_required ");
        		bayResponse.setStatus(false);
        		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026 );
        	}

        	//bayResponse.setStatus(true);
        	//bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
        	Hv.logger.info("S13_close_stop_latch_HV_Bay : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_009);
        }

        Hv.logger.info("S13_close_stop_latch_HV_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_HV_Bay() {
    	Hv.logger.debug("S13_close_stop_latch_HV_Bay : hvBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
        	Hv.logger.debug("PortId    : " + portInfo.getPortId());
        	Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
        	Hv.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.ON;
          
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            Hv.logger.debug("S13_close_stop_latch_HV_Bay : close_StopLatch_HV_Bay : state : " + state);
            
            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            Hv.logger.debug("S13_close_stop_latch_HV_Bay : close_StopLatch_HV_Bay : status : " + status);

        } else {
            Hv.logger.debug("S13_close_stop_latch_HV_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);
        
        if(StateExecutorController.simulateHvBayHappyPath){
        	status = true; 
        }

        Hv.logger.debug("S13_close_stop_latch_HV_Bay : hvBay_StopLatch_Status : status : " + status); 

		responseReturn.put("status", status);
		
        Hv.logger.debug("S13_close_stop_latch_HV_Bay : hvBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}

