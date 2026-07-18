package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet implements VerificTestBayState {

	private String flowPathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_VERIFIC_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_VERIFIC_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		/*
		 * Map<String,Object> responseReturn = do_QR_Code_Scanning_Pallet_VerificBay();
		 * String do_QR_Code_Scanning_Pallet_VerificBay =
		 * (String)responseReturn.get("responseData");
		 * 
		 * String status = do_QR_Code_Scanning_Pallet_VerificBay; // Call the function
		 * to scan pallet QR codes
		 * 
		 * if (status.equals("GOOD")) {
		 * VerificationTestBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Successful");
		 * // Logic for success case (status is true)
		 * bayResponse.setStatus(true);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success
		 * error code
		 * } else if(status.equals("NO_QR_CODE_AVAILABLE")){
		 * VerificationTestBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * VerificationTestBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : Issue with Pallet Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_006); //
		 * Failure error code
		 * }
		 * else if(status.equals("SCNR_NW")){
		 * VerificationTestBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * VerificationTestBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : Issue with Scanner Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_006); //
		 * Failure error code
		 * }
		 */
		int waitingVerificToVerificTransitTimeInSec = 15;
		Verification.logger.info(
				"S02_qR_Code_Scanning_of_Pallet : awaiting for all pallets movement and exit confirmation from WaitingVerific bay : "
						+ waitingVerificToVerificTransitTimeInSec + " Sec");
		while ((waitingVerificToVerificTransitTimeInSec > 0) &&
				(!ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared()) &&
				(!Verification.isStopProcessRequestedVerificBay())) {
			waitingVerificToVerificTransitTimeInSec--;
			BayUtils.delay(1000);
			Verification.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : waitingVerificToVerificTransitTimeInSec: "
							+ waitingVerificToVerificTransitTimeInSec);
			Verification.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : isVerific1WaitingBayPalletsAllCleared(): "
							+ ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
			Verification.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : isStopProcessRequestedVerificBay(): "
					+ Verification.isStopProcessRequestedVerificBay());
			Verification.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : *********************************************");

		}

		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet :  waitingVerificToVerificTransitTimeInSec: "
				+ waitingVerificToVerificTransitTimeInSec);
		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet :  isVerific1WaitingBayPalletsAllCleared(): "
				+ ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet :  isStopProcessRequestedVerificBay(): "
				+ Verification.isStopProcessRequestedVerificBay());

		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				Verification.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateVerificBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	private Map<String, Object> do_QR_Code_Scanning_Pallet_VerificBay() {

		Verification.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_VerificBay : Entry");

		String status = "";
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		/*
		 * TerminalBayProfileModel terminalBayProfile = new TerminalBayProfileModel();
		 * terminalBayProfile.setMyClusterId("01");
		 * terminalBayProfile.setMyBayId("02");
		 */
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.VERIFICATION_BAY_KEY);

		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		String scannedData = qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_VERIFIC_BAY_PALLET_POS_ID);

		/*
		 * if(scannedData == null){ // == null is enough since we do all validation in
		 * extractScannedData() function
		 * //status = "NULL";
		 * }
		 * else
		 */
		if (scannedData.equals("NO_QR_CODE_AVAILABLE")) {
			status = "NO_QR_CODE_AVAILABLE";
		} else if (scannedData.equals("SCNR_NW")) {
			status = "SCNR_NW";
		} else {
			status = "GOOD";
			// do the needful";
		}

		if (StateExecutorController.simulateVerificBayHappyPath) {
			status = "GOOD";
		}

		Verification.logger
				.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_VerificBay : status : " + status);
		if (status.equals("GOOD")) {
			responseReturn.put("status", true);
		}
		responseReturn.put("responseData", status);
		Verification.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_VerificBay : Exit");
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
