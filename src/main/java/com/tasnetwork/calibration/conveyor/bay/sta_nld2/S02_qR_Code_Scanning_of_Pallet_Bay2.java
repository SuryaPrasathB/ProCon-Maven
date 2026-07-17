package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet_Bay2 implements STA_NoLoadTestBay2State {

	private String flowPathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}
	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);



	/*	Map<String,Object> responseReturn =  do_QR_Code_Scanning_Pallet_SctNltBay2();	 
		String do_QR_Code_Scanning_Pallet_SctNltBay2 = (String)responseReturn.get("responseData");

		String status = do_QR_Code_Scanning_Pallet_SctNltBay2;  // Call the function to scan pallet QR codes

		if (status.equals("GOOD")) {
			STA_NoLoadTestBay2.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Successful");
			// Logic for success case (status is true)
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
		} else if(status.equals("NO_QR_CODE_AVAILABLE")){
			STA_NoLoadTestBay2.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
			STA_NoLoadTestBay2.logger.info("S02_qR_Code_Scanning_of_Pallet : Issue with Pallet Side");
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_006);  // Failure error code
		}
		else if(status.equals("SCNR_NW")){
			STA_NoLoadTestBay2.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
			STA_NoLoadTestBay2.logger.info("S02_qR_Code_Scanning_of_Pallet : Issue with Scanner Side");
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_006);  // Failure error code
		}*/
		
		
		int verificToSta2TransitTimeInSec = 15;
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting for all pallets movement and exit confirmation from WaitingVerific bay : " +verificToSta2TransitTimeInSec + " Sec");
		while( (verificToSta2TransitTimeInSec>0) && 
				//(!ConstantConveyor.isVerificationBayPalletsCleared() ) && 
				(!ConveyorDataManager.isVerific1PalletsAllCleared() ) && 
				(!StaNld_Bay2.isStopProcessRequestedStaNldBay2())){
			verificToSta2TransitTimeInSec--;
			BayUtils.delay(1000);
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : verificToSta2TransitTimeInSec: "+ verificToSta2TransitTimeInSec);
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : isVerific1PalletsAllCleared(): "+ ConveyorDataManager.isVerific1PalletsAllCleared());
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : StaNld_1.isStopProcessRequestedSctNltBay1(): "+ StaNld_Bay1.isStopProcessRequestedStaNldBay1());
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : *********************************************"); 
			
		}
		
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet :  verificToSta2TransitTimeInSec: "+ verificToSta2TransitTimeInSec);
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet :  isVerific1WaitingBayPalletsAllCleared(): "+ ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet :  StaNld_Bay2.isStopProcessRequestedSctNltBay2(): "+ StaNld_Bay2.isStopProcessRequestedStaNldBay2());
		
		
		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				StaNld_Bay2.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletQrScannerPositionId(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
			bayResponse = bayPalletService.qrCodePalletScanningProcess();

		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	//============================================================================================================================================  

	private Map<String, Object> do_QR_Code_Scanning_Pallet_SctNltBay2() {
		// TODO Auto-generated method stub

		StaNld_Bay2.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_SctNltBay2 : Entry");

		String status = "";
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
/*		TerminalBayProfileModel terminalBayProfile = new TerminalBayProfileModel();
		terminalBayProfile.setMyClusterId("01");
		terminalBayProfile.setMyBayId("02");*/
		
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.STA_NLD2_BAY_KEY);
		
		
		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		String scannedData = qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_SCT_NLT_BAY2_PALLET_POS_ID);

		/*if(scannedData == null){  // == null is enough since we do all validation in extractScannedData() function
		//status = "NULL";
	}
	else*/
		if(scannedData.equals("NO_QR_CODE_AVAILABLE")){  
			status = "NO_QR_CODE_AVAILABLE";
		}
		else if(scannedData.equals("SCNR_NW")){  
			status = "SCNR_NW";
		}
		else {
			status = "GOOD";
			// do the needful";
		} 

		if(StateExecutorController.simulateSCTNLTBay2HappyPath){
			status = "GOOD"; 
		}


		StaNld_Bay2.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_SctNltBay2 : status : " + status);

		if (status.equals("GOOD")) {
			responseReturn.put("status", true);
		} 
		responseReturn.put("responseData", status);

		StaNld_Bay2.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_SctNltBay2 : Exit");
		return responseReturn;
	}
	
	
	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}
	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}
	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}
	public int getPalletQrScannerPositionId() {
		return palletQrScannerPositionId;
	}
	public void setPalletQrScannerPositionId(int palletQrScannerPositionId) {
		this.palletQrScannerPositionId = palletQrScannerPositionId;
	}
}
