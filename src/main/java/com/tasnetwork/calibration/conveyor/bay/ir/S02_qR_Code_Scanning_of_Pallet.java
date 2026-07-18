package com.tasnetwork.calibration.conveyor.bay.ir;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet implements IrtBayState {

	

	//boolean simulateHvBayHappyPath = true;
	private String sequencePathId = "p1";
	
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_01 ;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

 
	public String getMyBayKey() {
		return myBayKey;
	}
    //==========================================================================================	private boolean logEnabled = true;

	/**
     * Scans the QR code of the pallet at the IRT Bay.
     *
     * @return BayResponse indicating success or failure.
     */
    @Override
    public BayResponse handleRequest() {
        Ir.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                
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
