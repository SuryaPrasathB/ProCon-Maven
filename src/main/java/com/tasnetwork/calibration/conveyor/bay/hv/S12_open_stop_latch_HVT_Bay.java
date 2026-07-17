package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S12_open_stop_latch_HVT_Bay implements HvtBayState {
	PalletTrackerController palletTracker = new PalletTrackerController();
	public String getMyBayKey() {
		return myBayKey;
	}
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S12_open_stop_latch_HV_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
       // BayUtils.delay(2000);
        Hv.logger.info("S12_open_stop_latch_HVT_Bay: Waiting for Halt Pallet at Hv-Entry");
		
		while((!Hv.isStopProcessRequestedHvtBay()) &&
			(ConveyorDataManager.isHaltPalletActiveInHv()) ) 	{
			BayUtils.delay(1000);
		}
	        
		Hv.logger.info("S12_open_stop_latch_HVT_Bay: Waiting for Halt Pallet at Hv-Exit");
		

		Hv.logger.info("S12_open_stop_latch_HVT_Bay: Waiting for NoEntry at Hv-Entry for Ir");
		while((!Hv.isStopProcessRequestedHvtBay()) &&
				(ConveyorDataManager.isNoEntryActiveInIr()) ) 	{
			BayUtils.delay(1000);
		}
			        
		Hv.logger.info("S12_open_stop_latch_HVT_Bay: Waiting for NoEntry at Hv-Exit for Ir");
				
		
		if(!Hv.isStopProcessRequestedHvtBay()) {
	        Map<String, Object> responseReturn = open_StopLatch_HvBay();
	
	        boolean openStopLatch_HvBay = (boolean) responseReturn.get("status");
	
	        if (openStopLatch_HvBay) {
	            Hv.logger.info("S12_open_stop_latch_HV_Bay : Stop Latch Opened");
	            ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayExitStopper(getMyBayKey(),true);
	            bayResponse.setStatus(true);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
	            
	            palletTracker.switchPalletToNextBay(myBayKey, ConstantConveyor.IR_BAY_KEY);
	            
	            BayUtils.delay(1000);
	        } else {
	            Hv.logger.info("S12_open_stop_latch_HV_Bay : Failed to Open Stop Latch");
	            bayResponse.setStatus(false);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_008);
	        }
		}
        Hv.logger.info("S12_open_stop_latch_HV_Bay : Exit");
        return bayResponse;
    }
    
  //============================================================================================================

    private Map<String, Object> open_StopLatch_HvBay() {
    	Hv.logger.debug("S12_open_stop_latch_HV_Bay : hvBay_StopLatch_Status : Entry");
    	
    	

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_STPR);
        String state = "";
        
        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());

            String outputActive = Constant_IO_ActionMapping.OFF;
            
            
            BayUtils bayUtils = new BayUtils();
            
             state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputActive);

            Hv.logger.debug("S12_open_stop_latch_HV_Bay : open_StopLatch_HvBay : state : " + state);
           
        /*    if (simulateHvBayHappyPath) {
                state = Constant_IO_ActionMapping.ON;
            }*/

            status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false ;
            
           /* PalletTrackerController palletTracker = new PalletTrackerController();
            palletTracker.switchPalletToNextBay(myBayKey, ConstantConveyor.IR_BAY_KEY);*/

            Hv.logger.debug("S12_open_stop_latch_HV_Bay : open_StopLatch_HvBay : status : " + status);

        } else {
            Hv.logger.debug("S12_open_stop_latch_HV_Bay : Output port not found");
            return responseReturn ;
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);
        
        if(StateExecutorController.simulateHvBayHappyPath){
        	status = true; 
        }

        Hv.logger.debug("S12_open_stop_latch_HV_Bay : hvBay_StopLatch_Status : status : " + status); 

		responseReturn.put("status", status);
		
        Hv.logger.debug("S12_open_stop_latch_HV_Bay : hvBay_StopLatch_Status : Exit");
        return responseReturn;
    }
}
