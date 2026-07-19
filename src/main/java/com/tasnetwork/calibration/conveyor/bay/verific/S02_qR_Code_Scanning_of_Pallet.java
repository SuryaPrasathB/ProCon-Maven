package com.tasnetwork.calibration.conveyor.bay.verific;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S02_qR_Code_Scanning_of_Pallet implements VerificTestBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_VERIFIC_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_VERIFIC_BAY_PALLET_POS_ID;

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

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
