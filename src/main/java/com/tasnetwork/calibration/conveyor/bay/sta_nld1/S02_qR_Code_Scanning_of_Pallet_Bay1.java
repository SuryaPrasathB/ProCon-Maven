package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S02_qR_Code_Scanning_of_Pallet_Bay1 implements STA_NoLoadTestBay1State {
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;



	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);



		int verificToSta1TransitTimeInSec = 15;
		StaNld_Bay1.logger.info(
				"S02_qR_Code_Scanning_of_Pallet : awaiting for all pallets movement and exit confirmation from WaitingVerific bay : "
						+ verificToSta1TransitTimeInSec + " Sec");
		while ((verificToSta1TransitTimeInSec > 0) &&
		// (!ConstantConveyor.isVerificationBayPalletsCleared() ) &&
				(!ConveyorDataManager.isVerific1PalletsAllCleared()) &&
				(!StaNld_Bay1.isStopProcessRequestedStaNldBay1())) {
			verificToSta1TransitTimeInSec--;
			BayUtils.delay(1000);
			StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : verificToSta1TransitTimeInSec: "
					+ verificToSta1TransitTimeInSec);
			StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : isVerific1PalletsAllCleared(): "
					+ ConveyorDataManager.isVerific1PalletsAllCleared());
			StaNld_Bay1.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : StaNld_Bay1.isStopProcessRequestedSctNltBay1(): "
							+ StaNld_Bay1.isStopProcessRequestedStaNldBay1());
			StaNld_Bay1.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : *********************************************");

		}

		StaNld_Bay1.logger.info(
				"S02_qR_Code_Scanning_of_Pallet :  verificToSta1TransitTimeInSec: " + verificToSta1TransitTimeInSec);
		StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet :  isVerific1WaitingBayPalletsAllCleared(): "
				+ ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
		StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet :  StaNld_Bay1.isStopProcessRequestedSctNltBay1(): "
				+ StaNld_Bay1.isStopProcessRequestedStaNldBay1());

		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				StaNld_Bay1.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		StaNld_Bay1.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
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
