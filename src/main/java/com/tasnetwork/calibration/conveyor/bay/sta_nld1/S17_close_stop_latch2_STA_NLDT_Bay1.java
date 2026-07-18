package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;

import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

public class S17_close_stop_latch2_STA_NLDT_Bay1 implements STA_NoLoadTestBay1State {

    @Override
    public BayResponse handleRequest() {
    	PalletTrackerController palletTracker = new PalletTrackerController();
    	
        StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(4000);

        Map<String, Object> responseReturn = close_StopLatch_SCT_NLT_Bay1();

        boolean closeStopLatch_SCT_NLT_Bay1 = (boolean) responseReturn.get("status");

        if (closeStopLatch_SCT_NLT_Bay1) {
            StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : Stop Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            
            BayUtils.delay(3000);
        } else {
            StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : Failed to Close Stop Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_018);
        }
        
        int passedPallets = S16_open_stop_latch_STA_NLDT_Bay1.getPalletsPassedSTA_NLDT_Bay1();
        
        StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : getPalletsPassedSTA_NLDT_Bay1()" + passedPallets);
        
        
        
        if (S16_open_stop_latch_STA_NLDT_Bay1.getPalletsPassedSTA_NLDT_Bay1() == ConstantConveyor.NUM_PALLETS_STA_NLD1_BAY1) {
        	StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : All Pallets Passed STA_NLDT_Bay1");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_020);
            S16_open_stop_latch_STA_NLDT_Bay1.setPalletsPassedSTA_NLDT_Bay1(0);
            
            palletTracker.switchBatchToNextBay(myBayKey, ConstantConveyor.UNLOADING_BAY_KEY);//ConstantConveyor.COMMUNICATION_BAY_KEY);
            StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : last pallet exit wait time: "+DeviceDataManagerController.getConveyorConfigParsedKey().getSta1LastPalletExitWaitTime_InSec()+" Sec");
            BayUtils.delay(DeviceDataManagerController.getConveyorConfigParsedKey().getSta1LastPalletExitWaitTime_InSec()*1000);
            StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : last pallet exit wait done");
            ConveyorDataManager.setSta1PalletsExitInProgress(false);
            ConveyorDataManager.setSta1PalletsAllCleared(true);
            ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta1Bays();
            if (ProconFeatureEnable.MOTOR_CONTROL_ENABLE) {
            	BayUtils bayUtils = new BayUtils();
            	BayUtils.delay(5000);
            	responseReturn = bayUtils.set_motor_not_required(getMyBayKey());
            	boolean set_motor_not_required = (boolean) responseReturn.get("status");
            	if (set_motor_not_required) {
            		StaNld_Bay1.logger
            		.info("S17_close_stop_latch2_STA_NLDT_Bay1 : set_motor_not_required : Success");
            		//bayResponse.setStatus(false);
            		//bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_022);
            	} else {
            		StaNld_Bay1.logger
            		.info("S17_close_stop_latch2_STA_NLDT_Bay1 : Failed to set_motor_not_required ");
            		bayResponse.setStatus(false);
            		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_027);
            	} 
            }
		}

        StaNld_Bay1.logger.info("S17_close_stop_latch2_STA_NLDT_Bay1 : Exit");
        return bayResponse;
    }

    private Map<String, Object> close_StopLatch_SCT_NLT_Bay1() {
        StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : sctNltBay1_StopLatch_Status : Entry");

        boolean status = false;
        Map<String, Object> responseReturn = new HashMap<>();
        responseReturn.put("status", false);

        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR2);

        String state = "";
        if (portInfo != null) {
            StaNld_Bay1.logger.debug("PortId    : " + portInfo.getPortId());
            StaNld_Bay1.logger.debug("ClusterId : " + portInfo.getClusterId());
            StaNld_Bay1.logger.debug("BayId     : " + portInfo.getBayId());

            String outputInactive = Constant_IO_ActionMapping.ON;
            
            BayUtils bayUtils = new BayUtils();
            
            state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
                                                     portInfo.getBayId(),
                                                     portInfo.getPortId(),
                                                     outputInactive);

            StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : state : " + state);

            status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

            if(StateExecutorController.simulateSCTNLTBay1HappyPath){
            	status = true; 
            }
             
            StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : close_StopLatch_SCT_NLT_Bay1 : status : " + status);

        } else {
            StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : Output port not found");
        }

        responseReturn.put("status", status);
        responseReturn.put("responseData", state);

        StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : sctNltBay1_StopLatch_Status : status : " + status);
        StaNld_Bay1.logger.debug("S17_close_stop_latch2_STA_NLDT_Bay1 : sctNltBay1_StopLatch_Status : Exit");
        return responseReturn;
    }
}

