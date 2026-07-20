package com.tasnetwork.calibration.conveyor.bay.comm;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet implements CommTestBayState {

	private String sequencePathId = "p1";

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Comm.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		setSequencePathId("p1");
		setPalletAvailableTest_I_F_Status(null);

		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				Comm.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		Comm.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

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
