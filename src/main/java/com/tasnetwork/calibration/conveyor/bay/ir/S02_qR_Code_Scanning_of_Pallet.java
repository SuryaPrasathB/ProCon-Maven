package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet implements IrtBayState {

	

	//boolean simulateHvBayHappyPath = true;
	private String sequencePathId = "p1";
	
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
        Ir.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        /*setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status (null);

		Map<String,Object> responseReturn =  do_QR_Code_Scanning_Pallet_IrtBay();	 
		String do_QR_Code_Scanning_Pallet_IrtBay = (String)responseReturn.get("responseData");
	    
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		
        String status = do_QR_Code_Scanning_Pallet_IrtBay;  // Call the function to scan pallet QR codes

        if (status.equals("GOOD")) {
            InsulationResistanceTestBay.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Successful");
            // Logic for success case (status is true)
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
        } else if(status.equals("NO_QR_CODE_AVAILABLE")){
            InsulationResistanceTestBay.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
            InsulationResistanceTestBay.logger.info("S02_qR_Code_Scanning_of_Pallet : Issue with Pallet Side");
// Logic for failure case (status is false)
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_006);  // Failure error code
        }
        else if(status.equals("SCNR_NW")){
            InsulationResistanceTestBay.logger.info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
            InsulationResistanceTestBay.logger.info("S02_qR_Code_Scanning_of_Pallet : Issue with Scanner Side");
// Logic for failure case (status is false)
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_IRT_006);  // Failure error code
        }
*/
        
		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(Ir.logger, 
				getMyBayKey(),
				getBayStateSequenceId(), 
				getPalletQrScannerPositionId(), 
				getFailStateErrorCode(),
				StateExecutorController.simulateHvBayHappyPath);
			bayResponse = bayPalletService.qrCodePalletScanningProcess();
        Ir.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
        return bayResponse;
    }
    
    //============================================================================================================================================  

    private Map<String, Object> do_QR_Code_Scanning_Pallet_IrtBay() {
        Ir.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_IrtBay : Entry");

        String status = "";
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.IR_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_05,
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getSequencePathId(),
				"" + ConstantBayPortNameMapping.QR_SCNR_IRT_BAY_PALLET_POS_ID,
				"",
				"",
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"Waiting",
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);

/*		TerminalBayProfileModel terminalBayProfile = new TerminalBayProfileModel();
		terminalBayProfile.setMyClusterId("01");
		terminalBayProfile.setMyBayId("03");*/
		
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.IR_BAY_KEY);
		
    	NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
    	String scannedData = qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_IRT_BAY_PALLET_POS_ID);

    	/*if(scannedData == null){  // == null is enough since we do all validation in extractScannedData() function
    		//status = "NULL";
    	}
    	else*/
    	if(scannedData.equals("NO_QR_CODE_AVAILABLE")){  
			status = "NO_QR_CODE_AVAILABLE";
			testIntefaceStatus.setDeviceResponseData(status);
		}
		else if(scannedData.equals("SCNR_NW")){  
			status = "SCNR_NW";
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData(status);
		}else if(scannedData.equals(ConstantConveyor.COMM_ACCESS_FAILED)){  
			status = ConstantConveyor.COMM_ACCESS_FAILED;
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus(status);
		}else if(scannedData.equals(NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII)){  
			status = NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII;
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
		}
		else {
			status = "GOOD";
			testIntefaceStatus.setDeviceResponseStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
			// do the needful";
		} 
		
    	StateExecutorController.updateTestStatusGui(testIntefaceStatus);
    	
    	if(StateExecutorController.simulateIrBayHappyPath){
    		status = "GOOD";
    	}
 	

        Ir.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_IrtBay : status : " + status);
        
        if (status.equals("GOOD")) {
        	responseReturn.put("status", true);
		} 
		responseReturn.put("responseData", status);

        Ir.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_IrtBay : Exit");
        return responseReturn;
    }

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}

	/*public boolean isSimulateHvBayHappyPath() {
		return simulateHvBayHappyPath;
	}*/

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public int getPalletQrScannerPositionId() {
		return palletQrScannerPositionId;
	}

	/*public void setSimulateHvBayHappyPath(boolean simulateHvBayHappyPath) {
		this.simulateHvBayHappyPath = simulateHvBayHappyPath;
	}*/

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}

	public void setPalletQrScannerPositionId(int palletQrScannerPositionId) {
		this.palletQrScannerPositionId = palletQrScannerPositionId;
	}

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}
}
