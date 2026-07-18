package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_pallet_at_IRT_Bay implements IrtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_PALLET ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_IRT_001 ;
	

	private boolean logEnabled = true;

	/**
     * Checks if a pallet has arrived at the IRT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S01_check_for_pallet_at_IRT_Bay : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        
		boolean isPalletAvailableAt_IRTBay;
		long startTime;
		boolean stableDetection = false;

		/*// Initial delay before checking
		BayUtils.delay(ConstantConveyor.STABLE_PALLET_TIME);*/
		ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayMonitoringAllPalletsExistInBay(getMyBayKey());
		while (!stableDetection && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Ir.isStopProcessRequestedIrtBay()) {
		    Map<String, Object> responseReturn = isPalletAvailableAt_IRTBay();
		    isPalletAvailableAt_IRTBay = (boolean) responseReturn.get("status");

		    if (isPalletAvailableAt_IRTBay) {
		        startTime = System.currentTimeMillis();
		        
		        while (System.currentTimeMillis() - startTime < ConstantConveyor.STABLE_PALLET_TIME_IN_MSEC) {
		            BayUtils.delay(1000); // Small delay to prevent CPU overuse
		            responseReturn = isPalletAvailableAt_IRTBay();
		            isPalletAvailableAt_IRTBay = (boolean) responseReturn.get("status");
		            
		            if (!isPalletAvailableAt_IRTBay) {
		                break; // Reset if detection is lost
		            }
		        }
		        
		        // If detection lasted for stable pallet time, confirm stability
		        if (isPalletAvailableAt_IRTBay) {
		            stableDetection = true;
		        }
		    } else {
		    	if(logEnabled) {
		    		Ir.logger.info("S01_check_for_pallet_at_IRT_Bay : No pallet Available at IRT Bay");
		    	}
		        BayUtils.delay(1000);
		    }
		    logEnabled = false;
		}

		if (stableDetection) {
		    Ir.logger.info("S01_check_for_pallet_at_IRT_Bay : Pallet Available");
		    ConveyorDataManager.getDashboardObject().getBayIndicatorManager().updateBayAllPalletsExistInBay(getMyBayKey(),true);
		    bayResponse.setStatus(true);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
		    Ir.logger.info("S01_check_for_pallet_at_IRT_Bay : Pallet Not Available");
		    bayResponse.setStatus(false);
		    bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_001);
		}

		if(bayResponse.getStatus()) {
			BayUtils bayUtils = new BayUtils();
			bayUtils.markAsCompleteForPreviousBayPallet(getMyBayKey(),Ir.logger);
			Ir.logger.info("S01_check_for_pallet_at_IRT_Bay: Waiting for Halt Pallet at Ir-Entry");
			
			while((!Ir.isStopProcessRequestedIrtBay()) &&
				(ConveyorDataManager.isHaltPalletActiveInIr()) ) 	{
				BayUtils.delay(1000);
			}
		        
			Ir.logger.info("S01_check_for_pallet_at_IRT_Bay: Waiting for Halt Pallet at Ir-Exit");
		}
        Ir.logger.info("S01_check_for_pallet_at_IRT_Bay : Exit");
        return bayResponse;
    }
//===================================================================================================================================
    private Map<String,Object> isPalletAvailableAt_IRTBay() {
    	if(logEnabled) {
    		Ir.logger.debug("S01_check_for_pallet_at_IRT_Bay : isPalletAvailableAt_IRTBay : Entry");
    	}
        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.IR_PORT_NAME_SNSR_PALLET);

        if (portInfo != null) {
        	if(logEnabled) {
	            //Ir.logger.debug("PortId    : " + portInfo.getPortId());
	            //Ir.logger.debug("ClusterId : " + portInfo.getClusterId());
	            //Ir.logger.debug("BayId     : " + portInfo.getBayId());
	            Ir.logger.debug("isPalletAvailableAt_IRTBay : getClusterId: " +portInfo.getClusterId() + " -> getBayId: " + portInfo.getBayId() + " -> getPortId: " + portInfo.getPortId() );

        	}
        } else {
        	if(logEnabled) {
        		Ir.logger.debug("S01_check_for_pallet_at_IRT_Bay : isPalletAvailableAt_IRTBay : Output port not found");
        	}
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        /*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
                portInfo.getBayId(),
                portInfo.getPortId());*/
        
        String state = bayUtils.getInputDataFromBayV2(portInfo) ;
        if(logEnabled) {
        	Ir.logger.debug("S01_check_for_pallet_at_IRT_Bay : isPalletAvailableAt_IRTBay : state : " + state);
        }
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
        if(StateExecutorController.simulateIrBayHappyPath){
        	status = true; 
        }
        if(logEnabled) {
        	Ir.logger.debug("S01_check_for_pallet_at_IRT_Bay : isPalletAvailableAt_IRTBay : status : " + status); 
        }
		responseReturn.put("status", status);
		if(logEnabled) {
			Ir.logger.debug("S01_check_for_pallet_at_IRT_Bay : isPalletAvailableAt_IRTBay : Exit");
		}
        return responseReturn;
    }
  //===================================================================================================================================


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
	
	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
}
