package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.CheckForPalletAtBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S091_check_for_no_pallets_at_unloading_Bay2 implements STA_NoLoadTestBay2State {


	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET;//ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET2 ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_015 ;
	
	
	public String getMyBayKey() {
		return myBayKey;
	}
	
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		/*Map<String,Object> responseReturn =  isPalletAvailableAt_outArea2();	 
		boolean isPalletAvailableAt_outArea2 = (boolean)responseReturn.get("status");
	    
        while (isPalletAvailableAt_outArea2 &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
        	
        	STA_NoLoadTestBay1.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Pallet Available at UnloadingBay");
            BayUtils.delay(1000);
            
            responseReturn =  isPalletAvailableAt_outArea2();	 
    		 isPalletAvailableAt_outArea2 = (boolean)responseReturn.get("status");
    	      
        }

        if (!isPalletAvailableAt_outArea2) {
            STA_NoLoadTestBay1.logger.info("S091_check_for_no_pallets_at_unloading_Bay : No Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            STA_NoLoadTestBay1.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Pallet Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_015);
        }*/
        
        boolean isPalletAvailableAt_UnloadingBay = false;
        long startTime;
        boolean stableDetectionForNoPallet = false;

        while (!stableDetectionForNoPallet 
        		&& (!StaNld_Bay2.isStopProcessRequestedStaNldBay2())
        		&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            Map<String, Object> responseReturn = isPalletAvailableAt_UnloadingBay();
            isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");

            if (!isPalletAvailableAt_UnloadingBay) {
                startTime = System.currentTimeMillis();
                
                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_UnloadingBay();
                    isPalletAvailableAt_UnloadingBay = (boolean) responseReturn.get("status");
                    
                    if (isPalletAvailableAt_UnloadingBay) {
                        break; // Reset if detection is lost
                    }
                }
                
                // If detection lasted for stable pallet time, confirm stability
                if (!isPalletAvailableAt_UnloadingBay) {
                    stableDetectionForNoPallet = true;
                }
            } else {
                StaNld_Bay2.logger.info("S091_check_for_no_pallets_at_unloading_Bay : No pallet Available at UnloadingBay");
                BayUtils.delay(1000);
            }
        }

        if (stableDetectionForNoPallet) {
            StaNld_Bay2.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Pallet Not Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay2.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Pallet Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_015);
        }


     /*   
		CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();*/
			
        StaNld_Bay2.logger.info("S091_check_for_no_pallets_at_unloading_Bay : Exit");
        return bayResponse;
    }
//==========================================================================================================
    private Map<String,Object> isPalletAvailableAt_UnloadingBay() {
        StaNld_Bay2.logger.debug("S091_check_for_no_pallets_at_unloading_Bay : isPalletAvailableAt_UnloadingBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(getPalletSensorPortCname());//ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET2);

        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S091_check_for_no_pallets_at_unloading_Bay : isPalletAvailableAt_UnloadingBay : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;

        StaNld_Bay2.logger.debug("S091_check_for_no_pallets_at_unloading_Bay : isPalletAvailableAt_UnloadingBay : state : " + state);

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        if(StateExecutorController.simulateSCTNLTBay1HappyPath){
        	status = true; 
        }
         
        StaNld_Bay2.logger.debug("S091_check_for_no_pallets_at_unloading_Bay : isPalletAvailableAt_UnloadingBay : status : " + status); 

		responseReturn.put("status", status);
		

        StaNld_Bay2.logger.debug("S091_check_for_no_pallets_at_unloading_Bay : isPalletAvailableAt_UnloadingBay : Exit");
        return responseReturn;
    }
    
	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}


/*	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
	*/
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



