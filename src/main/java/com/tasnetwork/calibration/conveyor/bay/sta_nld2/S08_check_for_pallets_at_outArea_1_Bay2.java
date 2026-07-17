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

public class S08_check_for_pallets_at_outArea_1_Bay2 implements STA_NoLoadTestBay2State {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET2 ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_014 ;
	

	public String getMyBayKey() {
		return myBayKey;
	}
	
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		/*Map<String,Object> responseReturn =  isPalletAvailableAt_outArea1();	 
		boolean isPalletAvailableAt_outArea1 = (boolean)responseReturn.get("status");
	     
        while (isPalletAvailableAt_outArea1 &&
        		(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
        	   STA_NoLoadTestBay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Available at Out Area 1");
               BayUtils.delay(1000);
               
    		  responseReturn =  isPalletAvailableAt_outArea1();	 
    		  isPalletAvailableAt_outArea1 = (boolean)responseReturn.get("status");
    	    
         
        }

        if (!isPalletAvailableAt_outArea1) {
            STA_NoLoadTestBay2.logger.info("S08_check_for_pallets_at_outArea_1 : No Pallet Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            STA_NoLoadTestBay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_014);
        }*/
        
        boolean isPalletAvailableAt_outArea1;
        long startTime;
        boolean stableDetectionForNoPallet = false;

        while (!stableDetectionForNoPallet && !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
            Map<String, Object> responseReturn = isPalletAvailableAt_outArea1();
            isPalletAvailableAt_outArea1 = (boolean) responseReturn.get("status");

            if (!isPalletAvailableAt_outArea1) {
                startTime = System.currentTimeMillis();
                
                while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
                    BayUtils.delay(1000); // Small delay to prevent CPU overuse
                    responseReturn = isPalletAvailableAt_outArea1();
                    isPalletAvailableAt_outArea1 = (boolean) responseReturn.get("status");
                    
                    if (isPalletAvailableAt_outArea1) {
                        break; // Reset if detection is lost
                    }
                }
                
                // If detection lasted for stable pallet time, confirm stability
                if (!isPalletAvailableAt_outArea1) {
                    stableDetectionForNoPallet = true;
                    StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Not Available at Out Area 1");
                }
            } else {
                StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Available at Out Area 1");
                BayUtils.delay(1000);
            }
        }

        if (stableDetectionForNoPallet) {
            StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Not Available");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Pallet Available");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_014);
        }

        
		/*CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(FunctionalTestBay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();*/
        
        StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 :  BayResponse status : " + bayResponse.getStatus()); 

        StaNld_Bay2.logger.info("S08_check_for_pallets_at_outArea_1 : Exit");
        return bayResponse;
    }

    private Map<String,Object> isPalletAvailableAt_outArea1() {
        StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.UNLOADING_PORT_NAME_SNSR_PALLET); // Minor Fix Applied, please verify on the placement of the scanner and adjust accordingly - SCT_NLT_BAY2_SNSR_PALLET2

        if (portInfo != null) {
            StaNld_Bay2.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay2.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay2.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 :  Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;

        

        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        if(StateExecutorController.simulateSCTNLTBay2HappyPath){
        	status = true; 
        }
         
        StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 :  state : " + state);
        StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 :  status : " + status); 

		responseReturn.put("status", status);
		

        StaNld_Bay2.logger.debug("S08_check_for_pallets_at_outArea_1 : isPalletAvailableAt_outArea1 :  Exit");
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
