package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S16_check_for_pallets_at_both_bays_in_loop  implements VerificTestBayState {

	/*    String LOW = "OPEN";
	    String HIGH = "CLOSE";*/

	    @Override
	    public BayResponse handleRequest() {
	        Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Entry");
	        BayResponse bayResponse = new BayResponse();
	        bayResponse.setStatus(true);
	        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
	        
	        Map<String, Object> responseReturn = isPalletAvailableAt_SCT_NLT_Bay1();
	        boolean isPalletAvailableAt_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

	        responseReturn = isPalletAvailableAt_SCT_NLT_Bay2();
	        boolean isPalletAvailableAt_SCT_NLT_Bay2 = (boolean) responseReturn.get("status");

	        boolean stableDetection = false;
	        long startTimeBay1 = 0, startTimeBay2 = 0;

	        while (!stableDetection && 
	        		 (!Verification.isStopProcessRequestedVerificBay()) &&
	        		!ConstantConveyor.ALL_LOOP_BREAK_FLAG) {

	            // Check for pallet availability at Bay 1
	            if (isPalletAvailableAt_SCT_NLT_Bay1) {
	                if (startTimeBay1 == 0) {
	                    startTimeBay1 = System.currentTimeMillis(); // Start timer when the first pallet is detected
	                }

	                // Wait for stable detection time
	                if (System.currentTimeMillis() - startTimeBay1 >= ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
	                    stableDetection = true; // Mark as stable if available for stable time
	                    Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Available at SCT NLT Bay 1");
	                    
	                    
	                    
	                    bayResponse.setStatus(true);
	                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
	                }
	            }

	            // Check for pallet availability at Bay 2
	            if (isPalletAvailableAt_SCT_NLT_Bay2) {
	                if (startTimeBay2 == 0) {
	                    startTimeBay2 = System.currentTimeMillis(); // Start timer when the first pallet is detected
	                }

	                // Wait for stable detection time
	                if (System.currentTimeMillis() - startTimeBay2 >= ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
	                    stableDetection = true; // Mark as stable if available for stable time
	                    Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Available at SCT NLT Bay 2");
	                    bayResponse.setStatus(true);
	                    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
	                }
	            }

	            if (!stableDetection) {
	                // If neither pallet is stable, log and check again
	                Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Checking for pallet availability...");
	                BayUtils.delay(1000);

	                // Recheck pallet availability at both bays
	                responseReturn = isPalletAvailableAt_SCT_NLT_Bay1();
	                isPalletAvailableAt_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

	                responseReturn = isPalletAvailableAt_SCT_NLT_Bay2();
	                isPalletAvailableAt_SCT_NLT_Bay2 = (boolean) responseReturn.get("status");
	            }
	        }

	        if (!stableDetection) {
	            Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Not Available");
	            bayResponse.setStatus(false);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
	        }


			/*Map<String,Object> responseReturn =  isPalletAvailableAt_SCT_NLT_Bay1();	 
			boolean isPalletAvailableAt_SCT_NLT_Bay1 = (boolean)responseReturn.get("status");
		    

			  responseReturn =  isPalletAvailableAt_SCT_NLT_Bay2();	 
			boolean isPalletAvailableAt_SCT_NLT_Bay2 = (boolean)responseReturn.get("status");
		    
	        
	        while ((!isPalletAvailableAt_SCT_NLT_Bay1) || (!isPalletAvailableAt_SCT_NLT_Bay2) ) {
	        	

	        	if (isPalletAvailableAt_SCT_NLT_Bay1) {
		            VerificationTestBay.logger.info("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : Pallet Available at SCT NLT Bay 1");
		            bayResponse.setStatus(true);
		            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
		        }
	        	else if (isPalletAvailableAt_SCT_NLT_Bay2) {
		            VerificationTestBay.logger.info("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay2 : Pallet Available at SCT NLT Bay 2");
		            bayResponse.setStatus(true);
		            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
		        }	          	
	            BayUtils.delay(1000);

				  responseReturn =  isPalletAvailableAt_SCT_NLT_Bay1();	 
				  isPalletAvailableAt_SCT_NLT_Bay1 = (boolean)responseReturn.get("status");
			    

				  responseReturn =  isPalletAvailableAt_SCT_NLT_Bay2();	 
				  isPalletAvailableAt_SCT_NLT_Bay2 = (boolean)responseReturn.get("status");
	        	
	            
	            
	        }

	        if (isPalletAvailableAt_SCT_NLT_Bay1) {
	            VerificationTestBay.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Available at SCT NLT Bay 1");
	            bayResponse.setStatus(true);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
	        }
	        else if (isPalletAvailableAt_SCT_NLT_Bay2) {
	            VerificationTestBay.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Available at SCT NLT Bay 2");
	            bayResponse.setStatus(true);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
	        } else {
	            VerificationTestBay.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Pallet Not Available");
	            bayResponse.setStatus(false);
	            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_019);
	        }*/

	        Verification.logger.info("S16_check_for_pallets_at_both_bays_in_loop : Exit");
	        return bayResponse;
	    }

//====================================================================================================================================	    
	    private Map<String,Object> isPalletAvailableAt_SCT_NLT_Bay1() {
	        Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : isPalletAvailableAt_SCT_NLT_Bay1 : Entry");

	        boolean status = false;
	        Map<String,Object> responseReturn = new HashMap<String,Object>();
			responseReturn.put("status", false);
	        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY1_SNSR_PALLET1);

	        if (portInfo != null) {
	            Verification.logger.debug("PortId    : " + portInfo.getPortId());
	            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
	            Verification.logger.debug("BayId     : " + portInfo.getBayId());
	        } else {
	            Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : isPalletAvailableAt_SCT_NLT_Bay1 : Output port not found");
	            return responseReturn ;
	        }

	        BayUtils bayUtils = new BayUtils();
	        
	        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
	                portInfo.getBayId(),
	                portInfo.getPortId());*/
			
			String state = bayUtils.getInputDataFromBayV2(portInfo) ;

	        Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : isPalletAvailableAt_SCT_NLT_Bay1 : state : " + state);

	        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
	        
			if(StateExecutorController.simulateVerificBayHappyPath){
				state = Constant_IO_ActionMapping.OPEN;
			}

	        Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : isPalletAvailableAt_SCT_NLT_Bay1 : status : " + status); 

			responseReturn.put("status", status);
			

	        Verification.logger.debug("S08_check_for_pallets_at_SCT_NLT_Bay1 : isPalletAvailableAt_SCT_NLT_Bay1 : Exit");
	        return responseReturn;
	    }
	  //====================================================================================================================================	    
	    
	    private Map<String,Object> isPalletAvailableAt_SCT_NLT_Bay2() {
	        Verification.logger.debug("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : Entry");

	        boolean status = false;
	        Map<String,Object> responseReturn = new HashMap<String,Object>();
			responseReturn.put("status", false);
	        
	        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.SCT_NLT_BAY2_SNSR_PALLET1);

	        if (portInfo != null) {
	            Verification.logger.debug("PortId    : " + portInfo.getPortId());
	            Verification.logger.debug("ClusterId : " + portInfo.getClusterId());
	            Verification.logger.debug("BayId     : " + portInfo.getBayId());
	        } else {
	            Verification.logger.debug("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : Output port not found");
	            return responseReturn ;
	        }

	        BayUtils bayUtils = new BayUtils();
	        
	       /* String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
	                portInfo.getBayId(),
	                portInfo.getPortId());*/
			
			String state = bayUtils.getInputDataFromBayV2(portInfo) ;

	        Verification.logger.debug("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : state : " + state);

	        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
	        
			if(StateExecutorController.simulateVerificBayHappyPath){
				state = Constant_IO_ActionMapping.OPEN;
			}

	        Verification.logger.debug("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : status : " + status); 

			responseReturn.put("status", status);
			

	        Verification.logger.debug("S16_check_for_pallets_at_both_bays_in_loop : isPalletAvailableAt_SCT_NLT_Bay1 : Exit");
	        return responseReturn;
	    }
	    
	  //====================================================================================================================================	    
	    
	    
	}
