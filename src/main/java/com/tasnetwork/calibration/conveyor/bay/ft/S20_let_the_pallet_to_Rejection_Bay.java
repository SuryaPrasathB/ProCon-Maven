package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

/**
 * State class responsible for releasing the pallet to the Rejection Bay.
 */
public class S20_let_the_pallet_to_Rejection_Bay implements FtBayState {

/*    String LOW    = "OPEN";
    String HIGH   = "CLOSE";
    String CLOSE  = "Off"; //"On";
    String OPEN   = "On"; //"Off";
    String ON  = "On";
 	String OFF  = "Off";*/
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Ft.logger.info("S20_let_the_pallet_to_Rejection_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Assuming 701 for Rejection Bay

        //=============================================================
		Map<String,Object> responseReturn =  open_StopLatch_FtBay();	 
		boolean open_StopLatch_FtBay = (boolean)responseReturn.get("status");

        boolean openSuccess = open_StopLatch_FtBay;
        if (openSuccess) {
            BayUtils.delay(500);
    		responseReturn =  close_StopLatch_FtBay();	 
    		boolean close_StopLatch_FtBay = (boolean)responseReturn.get("status");

            boolean closeSuccess = close_StopLatch_FtBay;
            if (closeSuccess) {
                Ft.logger.info("S20_let_the_pallet_to_Rejection_Bay : Opening and Closing Stopper Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Ft.logger.info("S20_let_the_pallet_to_Rejection_Bay : Closing Stopper Failed");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_023); // Assuming 702 for failure
            }
        } else {
            Ft.logger.info("S20_let_the_pallet_to_Rejection_Bay : Opening Stopper Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_007);
        }
        //=============================================================

        Ft.logger.info("S20_let_the_pallet_to_Rejection_Bay : Exit");
        return bayResponse;
    }

    //============================================================================================================================================

    private Map<String,Object> open_StopLatch_FtBay() {
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : open_StopLatch_HvtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT);

        if (portInfo != null) {
            Ft.logger.debug("PortId    : " + portInfo.getPortId());
            Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ft.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                portInfo.getBayId(),
                                                portInfo.getPortId(),
                                                Constant_IO_ActionMapping.CLOSE);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : open_StopLatch_HvtBay : status : " + status);
        //============================================================================================  
        
        responseReturn.put("status", status);
        
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : open_StopLatch_HvtBay : Exit");
        return responseReturn;
    }
 
    //============================================================================================================================================

    private Map<String,Object> close_StopLatch_FtBay() {
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : close_StopLatch_HvtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================		 
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT);

        if (portInfo != null) {
            Ft.logger.debug("PortId    : " + portInfo.getPortId());
            Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
            Ft.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : Stopper Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                              portInfo.getBayId(),
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : close_StopLatch_HvtBay : status : " + status);
        //============================================================================================  
        

		responseReturn.put("status", status);
		
        Ft.logger.debug("S12_let_the_pallet_to_HVT_Bay : close_StopLatch_HvtBay : Exit");
        return responseReturn;
    }
    

    //============================================================================================================================================
}
