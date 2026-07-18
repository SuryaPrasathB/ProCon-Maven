package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S13_open_stop_latch_IRT_Bay implements IrtBayState {
	
	 PalletTrackerController palletTracker = new PalletTrackerController();

    /**
     * Opens the stop latch at the IRT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S13_open_stop_latch_IRT_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        //BayUtils.delay(2000);
        Ir.logger.info("S13_open_stop_latch_IRT_Bay: Waiting for Halt Pallet at Ir-Entry");
		
		while((!Ir.isStopProcessRequestedIrtBay()) &&
			(ConveyorDataManager.isHaltPalletActiveInIr()) ) 	{
			BayUtils.delay(1000);
		}
	        
		Ir.logger.info("S13_open_stop_latch_IRT_Bay: Waiting for Halt Pallet at Ir-Exit");
		
		Ir.logger.info("S13_open_stop_latch_IRT_Bay: Waiting for NoEntry at Ir-Entry for Calib");
		while((!Ir.isStopProcessRequestedIrtBay()) &&
				(ConveyorDataManager.isNoEntryActiveInCalib()) ) 	{
			BayUtils.delay(1000);
		}
			        
		Ir.logger.info("S13_open_stop_latch_IRT_Bay: Waiting for NoEntry at Ir-Exit for Calib");
			
		if(!Ir.isStopProcessRequestedIrtBay()) {
	        Map<String, Object> responseReturn = open_StopLatch_IRT_Bay();
	
	        boolean openStopLatch_IRT_Bay = (boolean) responseReturn.get("status");
	
	        if (openStopLatch_IRT_Bay) {
	            Ir.logger.info("S13_open_stop_latch_IRT_Bay : Stop Latch Opened");
	            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),true);
	            bayResponse.setStatus(true);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
	            
	            palletTracker.switchPalletToNextBay(myBayKey, ConstantConveyor.CALIBRATION_BAY_KEY);
	            
	            BayUtils.delay(1000);
	        } else {
	            Ir.logger.info("S13_open_stop_latch_IRT_Bay : Failed to Open Stop Latch");
	            bayResponse.setStatus(false);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_008);
	        }
    	}
        Ir.logger.info("S13_open_stop_latch_IRT_Bay : Exit");
        return bayResponse;
    }

    private Map<String, Object> open_StopLatch_IRT_Bay() {
        Ir.logger.debug("S13_open_stop_latch_IRT_Bay : irtBay_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_STPR);

        String state = "";
        if (portInfo != null) {
            Ir.logger.debug("PortId    : " + portInfo.getPortId());
            Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ir.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);
            
            
             
            Ir.logger.debug("S13_open_stop_latch_IRT_Bay : open_StopLatch_IRT_Bay : state : " + state);
            
          /*  if (simulateIrtBayHappyPath) {
                state = Constant_IO_ActionMapping.ON;
            }*/

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
            
            if(StateExecutorController.simulateIrBayHappyPath){
    			status = true; 
    		}
            
            
            Ir.logger.debug("S13_open_stop_latch_IRT_Bay : open_StopLatch_IRT_Bay : status : " + status);

        } else {
            Ir.logger.debug("S13_open_stop_latch_IRT_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        Ir.logger.debug("S13_open_stop_latch_IRT_Bay : irtBay_StopLatch_Status : status : " + status);
        Ir.logger.debug("S13_open_stop_latch_IRT_Bay : irtBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}

