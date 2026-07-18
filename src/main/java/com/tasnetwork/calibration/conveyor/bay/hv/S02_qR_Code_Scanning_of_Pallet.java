package com.tasnetwork.calibration.conveyor.bay.hv;

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

public class S02_qR_Code_Scanning_of_Pallet implements HvtBayState {

	// boolean simulateHvBayHappyPath = true;
	private String sequencePathId = "p1";

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_HVT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_HVT_BAY_PALLET_POS_ID;

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Hv.logger.debug("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		/*
		 * setSequencePathId("p1");
		 * setPalletAvailableTest_I_F_Status (null);
		 * 
		 * Map<String,Object> responseReturn = do_QR_Code_Scanning_Pallet_HvtBay();
		 * String status = (String)responseReturn.get("responseData");
		 * 
		 * StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,
		 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * 
		 * if (status.equals("GOOD")) {
		 * HighVoltageTestBay.logger.
		 * debug("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Successful");
		 * // Logic for success case (status is true)
		 * bayResponse.setStatus(true);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success
		 * error code
		 * } else if(status.equals("NO_QR_CODE_AVAILABLE")){
		 * HighVoltageTestBay.logger.
		 * debug("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * HighVoltageTestBay.logger.
		 * debug("S02_qR_Code_Scanning_of_Pallet : Issue with Pallet Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_006); // Failure
		 * error code
		 * }
		 * else if(status.equals("SCNR_NW")){
		 * HighVoltageTestBay.logger.
		 * debug("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * HighVoltageTestBay.logger.
		 * debug("S02_qR_Code_Scanning_of_Pallet : Issue with Scanner Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_006); // Failure
		 * error code
		 * }
		 */
		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(Hv.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateHvBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		Hv.logger.debug("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> do_QR_Code_Scanning_Pallet_HvtBay() {

		Hv.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_HvtBay : Entry");

		String status = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.HV_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_05,
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getSequencePathId(),
				"" + ConstantBayPortNameMapping.QR_SCNR_HVT_BAY_PALLET_POS_ID,
				"",
				"",
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"Waiting",
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);

		/*
		 * TerminalBayProfileModel terminalBayProfile = new TerminalBayProfileModel();
		 * 
		 * 
		 * terminalBayProfile.setMyClusterId("01");
		 * terminalBayProfile.setMyBayId("02");
		 */
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.HV_BAY_KEY);

		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		String scannedData = qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_HVT_BAY_PALLET_POS_ID);

		if (scannedData == null) { // == null is enough since we do all validation in extractScannedData() function
			status = "NULL";
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData(status);
		} else if (scannedData.equals("NO_QR_CODE_AVAILABLE")) {
			status = "NO_QR_CODE_AVAILABLE";
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus(status);
		} else if (scannedData.equals("SCNR_NW")) {
			status = "SCNR_NW";
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
		} else {
			status = "GOOD";
			testIntefaceStatus.setDeviceResponseStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
			// do the needful";
		}

		if (StateExecutorController.simulateHvBayHappyPath) {
			status = "GOOD";
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		if (status.equals("GOOD")) {
			responseReturn.put("status", true);
		}
		responseReturn.put("responseData", status);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		Hv.logger.debug("S02_qR_Code_Scanning_of_Pallet : status : do_QR_Code_Scanning_Pallet_HvtBay : " + status);

		Hv.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_HvtBay : Exit");
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

	public String getBayStateSequenceId() {
		return bayStateSequenceId;
	}

	public String getFailStateErrorCode() {
		return failStateErrorCode;
	}

	public int getPalletQrScannerPositionId() {
		return palletQrScannerPositionId;
	}

	public void setBayStateSequenceId(String bayStateSequenceId) {
		this.bayStateSequenceId = bayStateSequenceId;
	}

	public void setFailStateErrorCode(String failStateErrorCode) {
		this.failStateErrorCode = failStateErrorCode;
	}

	public void setPalletQrScannerPositionId(int palletQrScannerPositionId) {
		this.palletQrScannerPositionId = palletQrScannerPositionId;
	}
}
