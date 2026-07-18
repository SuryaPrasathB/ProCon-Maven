package com.tasnetwork.calibration.conveyor.bay.calib;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
//import com.tasnetwork.calibration.conveyor.bay_functionaltest.FunctionalTestBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S02_qR_Code_Scanning_of_Pallet implements CalibrationBayState {

	// boolean simulateHvBayHappyPath = true;
	private String sequencePathId = "p1";

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_CALIB_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_CALIB_BAY_PALLET_POS_ID;

	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	public String getMyBayKey() {
		return myBayKey;
	}

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Calib.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		/*
		 * setSequencePathId("p1");
		 * setPalletAvailableTest_I_F_Status (null);
		 * 
		 * Map<String,Object> responseReturn = do_QR_Code_Scanning_Pallet_CalibBay();
		 * String do_QR_Code_Scanning_Pallet_CalibBay = (String)
		 * responseReturn.get("responseData");
		 * 
		 * StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,
		 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * 
		 * String status = do_QR_Code_Scanning_Pallet_CalibBay ; // Call the function to
		 * scan pallet QR codes
		 * 
		 * if (status.equals("GOOD")) {
		 * CalibrationBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Successful");
		 * // Logic for success case (status is true)
		 * bayResponse.setStatus(true);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success
		 * error code
		 * } else if(status.equals("NO_QR_CODE_AVAILABLE")){
		 * CalibrationBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * CalibrationBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : Issue with Pallet Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_006); //
		 * Failure error code
		 * }
		 * else if(status.equals("SCNR_NW")){
		 * CalibrationBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : QR Code Scanning Failed");
		 * CalibrationBay.logger.
		 * info("S02_qR_Code_Scanning_of_Pallet : Issue with Scanner Side");
		 * // Logic for failure case (status is false)
		 * bayResponse.setStatus(false);
		 * bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_006); //
		 * Failure error code
		 * }
		 */
		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(Calib.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateHvBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		Calib.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	// ============================================================================================================================================

	public Map<String, Object> do_QR_Code_Scanning_Pallet_CalibBay() {
		Calib.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_CalibBay : Entry");

		String status = "";

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.CALIBRATION_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_05,
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getSequencePathId(),
				"" + ConstantBayPortNameMapping.QR_SCNR_CALIB_BAY_PALLET_POS_ID,
				"",
				"",
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"Waiting",
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);

		/*
		 * TerminalBayProfileModel myTerminalProfile = new TerminalBayProfileModel();
		 * myTerminalProfile.setMyClusterId("02");
		 * myTerminalProfile.setMyBayId("04");
		 */

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.CALIBRATION_BAY_KEY);

		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		// String scannedData =
		// qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_CALIB_BAY_PALLET);

		String scannedData = qrScannerObj.scan_QR_code(ConstantBayPortNameMapping.QR_SCNR_CALIB_BAY_PALLET_POS_ID);

		// =================================
		ParallelTaskManager dutManager = new ParallelTaskManager();
		boolean monitorAlreadyinitiated = false;
		int maxDeviceConnected = 0;// Zero to scan the pallete
		if (ProconFeatureEnable.CALIB_QR_SCANNER_EXECUTION_PROCESS_IN_PARALLEL) {
			for (int positionNo = 0; positionNo <= maxDeviceConnected; positionNo++) {

				// if(positionNo==1 || positionNo == 6){
				dutManager.startCalibrationQrScanProcess(positionNo);
				if (!monitorAlreadyinitiated) {
					dutManager.monitorDutControlProcessTrigger();
					monitorAlreadyinitiated = true;
				}
				// }
			}
			int dutWaitTimeDurationMaxInSec = 300;
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;
			while ((!BayUtils.isUserAborted()) &&
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) &&
					(!dutAllProcessExecutionCompleted) &&
					(!Calib.isStopProcessRequestedCalibBay()) &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG)) {
				Sleep(1000);
				// dutWaitTimeDurationInSec--;
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Calib.logger.debug(
						"calibrationTask: dutWaitTimeCounter: " + dutWaitTimeCounter + "/" + dutWaitTimeDurationMaxInSec
								+ " : dutAllProcessExecutionCompleted: " + dutAllProcessExecutionCompleted);

				// ApplicationHomeController.update_left_status("Creep: " + lduTimeDuration + "
				// Sec",ConstantApp.LEFT_STATUS_DEBUG);

			}
			if (dutManager.isDutAllControlProcessCompleted()) {
				// if(dutManager.isProjectExitProcess()){
				Calib.logger.debug("calibrationTask: All dut task completed");
				for (int positionNo = 0; positionNo <= maxDeviceConnected; positionNo++) {
					Calib.logger.debug("calibrationTask: result position Id : " + positionNo
							+ " : getDutResultSummary : " + dutManager.getDutResultSummary(positionNo));
					Calib.logger.debug("calibrationTask: result position Id : " + positionNo
							+ " : getDutResultResponse : " + dutManager.getDutResultResponse(positionNo));

				}

				// ProjectExitProcess();
				// return status;
				// }
			} else {
				Calib.logger.debug("calibrationTask QR : Timed out");
			}

		} else {
			if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
				scannedData = qrScannerObj.scan_QR_code(0);
			}
		}

		// =================================

		Calib.logger.debug(
				"S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_CalibBay : scannedData : " + scannedData);

		if (scannedData.equals("NO_QR_CODE_AVAILABLE")) {
			status = "NO_QR_CODE_AVAILABLE";
			testIntefaceStatus.setDeviceResponseData(status);
		} else if (scannedData.equals("SCNR_NW")) {
			status = "SCNR_NW";
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData(status);
		} else if (scannedData.equals(ConstantConveyor.COMM_ACCESS_FAILED)) {
			status = ConstantConveyor.COMM_ACCESS_FAILED;
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus(status);
		} else if (scannedData.equals(NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII)) {
			status = NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII;
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setSerialStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
		} else {
			status = "GOOD";
			testIntefaceStatus.setDeviceResponseStatus("Success");
			testIntefaceStatus.setDeviceResponseData(scannedData);
			// do the needful";
		}

		if (StateExecutorController.simulateCalibBayHappyPath) {
			status = "GOOD";
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		if (status.equals("GOOD")) {
			responseReturn.put("status", true);
		}

		responseReturn.put("responseData", status);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		Calib.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_CalibBay : status : " + status);
		Calib.logger.debug("S02_qR_Code_Scanning_of_Pallet : do_QR_Code_Scanning_Pallet_CalibBay : Exit");
		return responseReturn;
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			Calib.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

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
