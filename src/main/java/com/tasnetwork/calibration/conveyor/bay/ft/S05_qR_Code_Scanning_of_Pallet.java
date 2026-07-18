package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet; // Keep this import
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

/**
 * State class responsible for triggering the QR code scanning sequence for the
 * pallet at the FT Bay.
 * It delegates the actual scanning logic to the QrCodeScanningPallet service.
 */
public class S05_qR_Code_Scanning_of_Pallet implements FtBayState {

	private String flowPathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		// Changed to SEQUENCE_ENTRY as requested
		Ft.logger.info(String.format("[%s] : [QR_SCANNING] : [SEQUENCE_ENTRY] - Starting QR code scanning sequence.",
				getMyBayKey()));

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// The actual QR code scanning process is delegated to QrCodeScanningPallet.
		// Ensure that QrCodeScanningPallet also implements robust logging and threading
		// internally.
		// ConveyorDeviceDataManagerController.getDashboardObject().removePalletFromBay(getMyBayKey());
		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				Ft.logger, // Passing the Ft.logger for consistent logging source
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);

		// Execute the QR code scanning process
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		// Log the outcome of the delegated process
		if (bayResponse.isStatus()) {

			Ft.logger.info(String.format(
					"[%s] : [QR_SCANNING] : [SUCCESS] - QR code scanning completed successfully. Response: %s",
					getMyBayKey(), bayResponse.getErrorCode()));
		} else {
			Ft.logger.error(String.format("[%s] : [QR_SCANNING] : [FAILED] - QR code scanning failed. Error: %s",
					getMyBayKey(), bayResponse.getErrorCode()));
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format("[%s] : [QR_SCANNING] : [SEQUENCE_EXIT] - QR code scanning sequence completed.",
				getMyBayKey()));
		return bayResponse;
	}

	// ============================================================================================================================================

	public String getFlowPathId() {
		return flowPathId;
	}

	public void setFlowPathId(String sequencePathId) {
		this.flowPathId = sequencePathId;
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
