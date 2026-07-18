package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S02_qR_Code_Scanning_of_Pallet_Bay2 implements STA_NoLoadTestBay2State {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);



		int verificToSta2TransitTimeInSec = 15;
		StaNld_Bay2.logger.info(
				"S02_qR_Code_Scanning_of_Pallet : awaiting for all pallets movement and exit confirmation from WaitingVerific bay : "
						+ verificToSta2TransitTimeInSec + " Sec");
		while ((verificToSta2TransitTimeInSec > 0) &&
				(!ConveyorDataManager.isVerific1PalletsAllCleared()) &&
				(!StaNld_Bay2.isStopProcessRequestedStaNldBay2())) {
			verificToSta2TransitTimeInSec--;
			BayUtils.delay(1000);
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : verificToSta2TransitTimeInSec: "
					+ verificToSta2TransitTimeInSec);
			StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet : awaiting : isVerific1PalletsAllCleared(): "
					+ ConveyorDataManager.isVerific1PalletsAllCleared());
			StaNld_Bay2.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : StaNld_1.isStopProcessRequestedSctNltBay1(): "
							+ StaNld_Bay1.isStopProcessRequestedStaNldBay1());
			StaNld_Bay2.logger
					.info("S02_qR_Code_Scanning_of_Pallet : awaiting : *********************************************");

		}

		StaNld_Bay2.logger.info(
				"S02_qR_Code_Scanning_of_Pallet :  verificToSta2TransitTimeInSec: " + verificToSta2TransitTimeInSec);
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet :  isVerific1WaitingBayPalletsAllCleared(): "
				+ ConveyorDataManager.isWaitingVerific1BayPalletsAllCleared());
		StaNld_Bay2.logger.info("S02_qR_Code_Scanning_of_Pallet :  StaNld_Bay2.isStopProcessRequestedSctNltBay2(): "
				+ StaNld_Bay2.isStopProcessRequestedStaNldBay2());

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
