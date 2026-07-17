package com.tasnetwork.calibration.conveyor.bay.loading;

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
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S01_check_for_LoadingBay_pushButton_status implements LoadingBayState {

	private BayUtils bayUtils = new BayUtils();
	private String sequencePathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01;
	private String palletSensorPortCname =  ConstantBayPortNameMapping.CALIB_PORT_NAME_SNSR_PALLET;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_001;
	
	public String getMyBayKey() {
		return myBayKey;
	}

	@Override
	public BayResponse handleRequest() {
		Loading.logger.info("S01_check_for_LoadingBay_pushButton_status : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


/*		Map<String,Object> responseReturn =  isPushButtonPressedAt_LoadingBay();	 
		boolean isPushButtonPressedAt_LoadingBay = (boolean)responseReturn.get("status");


		while (!isPushButtonPressedAt_LoadingBay) {
			LoadingBay.logger.info("S01_check_for_LoadingBay_pushButton_status : Push button not pressed at FT Bay");
			BayUtils.delay(1000);
			responseReturn =  isPushButtonPressedAt_LoadingBay();	 
			isPushButtonPressedAt_LoadingBay = (boolean)responseReturn.get("status");


		}

		if (isPushButtonPressedAt_LoadingBay) {
			LoadingBay.logger.info("S01_check_for_LoadingBay_pushButton_status : Push button pressed");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			LoadingBay.logger.info("S01_check_for_LoadingBay_pushButton_status : Push button not pressed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_LOADING_001);
		}
*/
		
		CheckForPalletAtBay bayPalletService = new CheckForPalletAtBay(Loading.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletSensorPortCname(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.checkForPalletAtBayProcess();
		
		Loading.logger.info("S01_check_for_LoadingBay_pushButton_status : Exit");
		return bayResponse;
	}

	private Map<String,Object> isPushButtonPressedAt_LoadingBay() {
		Loading.logger.debug("S01_check_for_LoadingBay_pushButton_status : Entry");

		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);

		IoPortInfo portInfo = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.LOADING_PORT_NAME_PUSH_BTN);

		if (portInfo != null) {
			Loading.logger.debug("PortId    : " + portInfo.getPortId());
			Loading.logger.debug("ClusterId : " + portInfo.getClusterId());
			Loading.logger.debug("BayId     : " + portInfo.getBayId());
		} else {
			Loading.logger.debug("S01_check_for_LoadingBay_pushButton_status : Output port not found");
			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		/*String state = bayUtils.getInputDataFromBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId());*/
		
		String state = bayUtils.getInputDataFromBayV2(portInfo) ;
		

		Loading.logger.debug("S01_check_for_LoadingBay_pushButton_status : state : " + state);

		status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;

		if(StateExecutorController.simulateLoadingBayHappyPath){
			status = true; 
		}
 
		Loading.logger.debug("S01_check_for_LoadingBay_pushButton_status : status : " + status); 

		responseReturn.put("status", status);


		Loading.logger.debug("S01_check_for_LoadingBay_pushButton_status : Exit");
		return responseReturn;
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
	
	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
}
