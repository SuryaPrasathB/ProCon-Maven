package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tasnetwork.calibration.conveyor.ConveyorPalletTracking;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable; // Keeping original import
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.dutprocess.ParallelTaskManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.util.ChannelQueueRequestProcessor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

/**
 * State class responsible for scanning QR codes of the meters.
 */
public class S07_qR_Code_Scanning_of_Meters implements FtBayState {

	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	private String sequencePathId = "p1";
	private String meterSerialNumber = "";
	private String resultStatus = "";
	private String resultValue = "";
	private String testType = "";
	private String testCaseName = "";
	private String myBaySeqId = ConstantBayStateManage.BAY_HP_SEQ_10;

	// ===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Ft.logger.info(String.format(
				"[%s] : [METER_QR_SCAN] : [SEQUENCE_ENTRY] - Starting QR code scanning for meters.", getMyBayKey()));
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true); // Assume success initially
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		bayResponse = processQrCodeScanningMeterSerialNoFtBay();

		if (bayResponse.getStatus()) {
			if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {
				Ft.logger.info(String.format(
						"[%s] : [OPTICAL_READER_PROMPT] : [ACTIVE] - Waiting for user to place optical readers.",
						getMyBayKey()));

				boolean toggle = false;
				long startTime = System.currentTimeMillis();

				// UI update: enable button
				Platform.runLater(() -> {
					if (StateExecutorController.getRef_btn_FtPlace() != null) {
						StateExecutorController.getRef_btn_FtPlace().setDisable(false);
					} else {
						Ft.logger.warn(String.format(
								"[%s] : [UI_UPDATE_WARNING] : ref_btn_FtPlace is null, cannot enable button.",
								getMyBayKey()));
					}
				});

				while (!ConstantConveyor.isFT_OPTICAL_PLACED() && !Ft.isStopProcessRequestedFtBay()
						&& !ConstantConveyor.ALL_LOOP_BREAK_FLAG) {
					long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // in seconds

					// UI update: update prompt text
					Platform.runLater(() -> {
						if (StateExecutorController.ref_tf_FT_prompt != null) {
							StateExecutorController.ref_tf_FT_prompt
									.setText("Place Optical Readers - " + elapsedTime + " secs");
						} else {
							Ft.logger.warn(String.format(
									"[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot update prompt text.",
									getMyBayKey()));
						}
					});

					// Alternate the tower lamp state (I/O operation, should be on background
					// thread)
					if (toggle) {
						turn_on_tower_lamp1(); // Turn ON Lamp 2
						BayUtils.delay(100);
					} else {
						turn_off_tower_lamp1(); // Turn OFF Lamp 1
						BayUtils.delay(100);
					}
					toggle = !toggle; // Flip the flag for next iteration

					Ft.logger.debug(String.format(
							"[%s] : [OPTICAL_READER_PROMPT] : [WAITING] - Waiting to place Optical Readers. Elapsed: %d secs.",
							getMyBayKey(), elapsedTime));
				}

				BayUtils.delay(2000); // Blocking delay
				ConstantConveyor.setFT_OPTICAL_PLACED(false); // Reset flag

				// UI update: disable button
				Platform.runLater(() -> {
					if (StateExecutorController.getRef_btn_FtPlace() != null) {
						StateExecutorController.getRef_btn_FtPlace().setDisable(true);
					} else {
						Ft.logger.warn(String.format(
								"[%s] : [UI_UPDATE_WARNING] : ref_btn_FtPlace is null, cannot disable button.",
								getMyBayKey()));
					}
				});

				// UI update: clear prompt text
				Platform.runLater(() -> {
					if (StateExecutorController.ref_tf_FT_prompt != null) {
						StateExecutorController.ref_tf_FT_prompt.clear();
					} else {
						Ft.logger.warn(String.format(
								"[%s] : [UI_UPDATE_WARNING] : ref_tf_FT_prompt is null, cannot clear prompt text.",
								getMyBayKey()));
					}
				});
				Ft.logger.info(String.format(
						"[%s] : [OPTICAL_READER_PROMPT] : [COMPLETED] - User input for optical readers completed.",
						getMyBayKey()));

			} else {
				Ft.logger.info(String.format(
						"[%s] : [OPTICAL_READER_PROMPT] : [DISABLED] - User input for optical readers is disabled.",
						getMyBayKey()));
			}
		} else {
			// Structured error log for overall QR scanning failure
			Ft.logger.error(String.format(
					"[%s] : [METER_QR_SCAN] : [OVERALL_FAILED] - Meter QR scanning overall status failed. Error: %s",
					getMyBayKey(), bayResponse.getErrorCode()));
		}

		// Structured log for sequence exit
		Ft.logger.info(String.format(
				"[%s] : [METER_QR_SCAN] : [SEQUENCE_EXIT] - QR code scanning for meters sequence completed.",
				getMyBayKey()));
		return bayResponse;
	}
	// ============================================================================================================================================

	private void turn_on_tower_lamp1() {
		Ft.logger.debug(String.format(
				"[%s] : [TOWER_LAMP_1] : [TURN_ON] - Sending command to turn on Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).",
				getMyBayKey()));
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

		if (portInfo == null) {
			Ft.logger.error(String.format(
					"[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).",
					getMyBayKey()));
			return;
		}

		BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.OPEN); // Use CLOSE to represent turning the relay "On"
			Ft.logger
					.debug(String.format("[%s] : [TOWER_LAMP_1] : [ON_COMMAND_SENT] - Command sent. Response state: %s",
							getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send ON command to Tower Lamp 1. Port: %s. Error: %s",
					getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
	}

	private void turn_off_tower_lamp1() {
		Ft.logger.debug(String.format(
				"[%s] : [TOWER_LAMP_1] : [TURN_OFF] - Sending command to turn off Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).",
				getMyBayKey()));
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_TWR_LAMP2);

		if (portInfo == null) {
			Ft.logger.error(String.format(
					"[%s] : [CONFIG_ERROR] : [TOWER_LAMP_1] - Port info not found for Tower Lamp 1 (HV_PORT_NAME_TWR_LAMP2).",
					getMyBayKey()));
			return;
		}

		BayUtils bayUtils = new BayUtils();
		String state = "";
		try {
			state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
					portInfo.getBayId(),
					portInfo.getPortId(),
					Constant_IO_ActionMapping.CLOSE); // Use OPEN to represent turning the relay "Off"
			Ft.logger.debug(
					String.format("[%s] : [TOWER_LAMP_1] : [OFF_COMMAND_SENT] - Command sent. Response state: %s",
							getMyBayKey(), state));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [COMM_ERROR] : [TOWER_LAMP_1] - Failed to send OFF command to Tower Lamp 1. Port: %s. Error: %s",
					getMyBayKey(), portInfo.getPortId(), e.getMessage()), e);
		}
	}

	// ============================================================================================================================================
	public BayResponse processQrCodeScanningMeterSerialNoFtBay() {
		Ft.logger.info(String.format(
				"[%s] : [METER_QR_SCAN_PROCESS] : [ENTRY] - Starting parallel/sequential QR scanning for meters.",
				getMyBayKey()));
		BayResponse overAllBayResponse = new BayResponse();
		overAllBayResponse.setStatus(true);
		overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
		ParallelTaskManager dutManager = new ParallelTaskManager();
		boolean monitorAlreadyinitiated = false;

		ConveyorPalletTracking.resetPositionToMeterSerialNoMapToDefaultMappings();
		PalletTrackerController palletTracker = new PalletTrackerController();
		testCaseName = ConstantConveyor.DUT_QR_SERIAL_NO_READ_CMD_RESULT_TEST_NAME;
		testType = ConstantConveyor.FT_RESULT_KEY;
		if (ProconFeatureEnable.FT_QR_SCANNER_EXECUTION_PROCESS_IN_PARALLEL) {
			Ft.logger.info(
					String.format("[%s] : [METER_QR_SCAN_PROCESS] : [PARALLEL] - Executing QR scan in parallel mode.",
							getMyBayKey()));
			for (int positionNo = 1; positionNo <= maxDutSupported; positionNo++) {
				Ft.logger.debug(String.format(
						"[%s] : [PARALLEL_SCAN] : [INITIATING] - Starting QR scan process for position: %d",
						getMyBayKey(), positionNo));
				dutManager.startFtQrScanProcess(positionNo);
				if (!monitorAlreadyinitiated) {
					dutManager.monitorDutControlProcessTrigger();
					monitorAlreadyinitiated = true;
					Ft.logger.debug(String.format(
							"[%s] : [PARALLEL_SCAN] : [MONITOR_INITIATED] - DUT control process monitor initiated.",
							getMyBayKey()));
				}
			}

			int dutWaitTimeDurationMaxInSec = 300;
			int dutWaitTimeCounter = 0;
			boolean dutAllProcessExecutionCompleted = false;

			Ft.logger.info(String.format(
					"[%s] : [PARALLEL_SCAN] : [WAITING] - Waiting for parallel QR scan tasks to complete. Timeout: %d secs.",
					getMyBayKey(), dutWaitTimeDurationMaxInSec));
			while ((!BayUtils.isUserAborted()) &&
					(dutWaitTimeCounter < dutWaitTimeDurationMaxInSec) &&
					(!dutAllProcessExecutionCompleted) &&
					(!ConstantConveyor.ALL_LOOP_BREAK_FLAG) &&
					(!Ft.isStopProcessRequestedFtBay())) {
				Sleep(1000); // Blocking call, ensure this method is in a background thread
				dutWaitTimeCounter++;
				dutAllProcessExecutionCompleted = dutManager.isDutAllControlProcessCompleted();
				Ft.logger.debug(String.format(
						"[%s] : [PARALLEL_SCAN] : [PROGRESS] - Counter: %d/%d. All tasks completed: %s", getMyBayKey(),
						dutWaitTimeCounter, dutWaitTimeDurationMaxInSec, dutAllProcessExecutionCompleted));
			}

			if (dutManager.isDutAllControlProcessCompleted()) {
				Ft.logger.info(String.format(
						"[%s] : [PARALLEL_SCAN] : [ALL_COMPLETED] - All parallel DUT QR scan tasks completed.",
						getMyBayKey()));
				String readQrMeterSerialNo = "";
				for (int positionNo = 1; positionNo <= maxDutSupported; positionNo++) {
					String resultSummary = dutManager.getDutResultSummary(positionNo);
					readQrMeterSerialNo = dutManager.getDutResultResponse(positionNo);

					Ft.logger.debug(String.format(
							"[%s] : [PARALLEL_SCAN_RESULT] : Position: %d, Summary: <%s>, SerialNo: %s, HexSerialNo: %s",
							getMyBayKey(), positionNo, resultSummary, readQrMeterSerialNo,
							GuiUtils.StringToHex(readQrMeterSerialNo)));

					ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNo, readQrMeterSerialNo);

					// if(resultSummary.equals(ConstantReport.REPORT_POPULATE_FAIL.trim())) {
					if (resultSummary.trim().equals(ConstantReport.RESULT_STATUS_FAIL.trim())) {
						overAllBayResponse.setStatus(false);
						overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // 
						Ft.logger.debug(String.format("[%s] : [PARALLEL_SCAN_RESULT] : overAllBayResponse set to false",
								getMyBayKey()));
						resultValue = ConstantReport.REPORT_POPULATE_FAIL;
					} else {
						resultValue = ConstantReport.REPORT_POPULATE_PASS;
					}
					resultStatus = resultValue;
					palletTracker.addResultToMeter(positionNo, resultStatus, resultValue, getMyBayKey(), testType,
							testCaseName);
					if (resultStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
						palletTracker.updateMetersToPallet(getMyBayKey(), positionNo, resultStatus,
								ErrorCode.ERR_DUT_QR_READ_SERIAL_NO);
					}
				}
			} else {
				Ft.logger.error(String.format(
						"[%s] : [PARALLEL_SCAN] : [TIMEOUT] - Parallel QR scanning timed out after %d seconds.",
						getMyBayKey(), dutWaitTimeDurationMaxInSec));
				overAllBayResponse.setStatus(false);
				overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001); // Specific error for timeout
			}
		} else {
			Ft.logger.info(String.format(
					"[%s] : [METER_QR_SCAN_PROCESS] : [SEQUENTIAL] - Executing QR scan in non-parallel (sequential) mode.",
					getMyBayKey()));
			for (int positionNo = 1; positionNo <= maxDutSupported; positionNo++) {
				Ft.logger.debug(
						String.format("[%s] : [SEQUENTIAL_SCAN] : [INITIATING] - Starting QR scan for position: %d",
								getMyBayKey(), positionNo));
				BayResponse individualBayResponse = dutQrScanSerialNoProcess(positionNo);

				Ft.logger.info(
						String.format("[%s] : [SEQUENTIAL_SCAN_RESULT] : Position: %d, Status: %s, Response Data: %s",
								getMyBayKey(), positionNo, individualBayResponse.getStatus(),
								individualBayResponse.getResponseData()));

				String readQrMeterSerialNo = individualBayResponse.getResponseData();
				ConveyorPalletTracking.setPositionToMeterSerialNoMap(positionNo, readQrMeterSerialNo);

				if (!individualBayResponse.getStatus()) {
					Ft.logger.error(String.format(
							"[%s] : [SEQUENTIAL_SCAN] : [FAILED] - QR scan failed for position: %d. Status: %s",
							getMyBayKey(), positionNo, individualBayResponse.getStatus()));
					overAllBayResponse.setStatus(false);
					overAllBayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_001);
				}
			}
		}

		Ft.logger.info(String.format(
				"[%s] : [METER_QR_SCAN_PROCESS] : [EXIT] - QR scanning process for meters completed. Overall Status: %s",
				getMyBayKey(), overAllBayResponse.getStatus()));
		return overAllBayResponse;
	}

	public void Sleep(int timeInMsec) {
		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			Ft.logger.error(String.format("[%s] : [THREAD_SLEEP] : [INTERRUPTED] - Sleep interrupted. Error: %s",
					getMyBayKey(), e.getMessage()), e);
			Thread.currentThread().interrupt(); // Restore the interrupted status
		}
	}

	private boolean checkAckForCommands() {
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [ENTRY] - Checking acknowledgment for commands.",
				getMyBayKey()));
		boolean status = true; // Placeholder for actual implementation.
		// Add actual logic to check acknowledgment
		Ft.logger.debug(String.format("[%s] : [COMM_ACK_CHECK] : [EXIT] - Acknowledgment check completed. Status: %s",
				getMyBayKey(), status));
		return status;
	}

	public BayResponse dutQrScanSerialNoProcess(int positionNum) {
		Ft.logger.info(String.format("[%s] : [DUT_QR_SCAN] : [ENTRY] - Processing QR scan for DUT at position: %d",
				getMyBayKey(), positionNum));
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Default to false
		String qrScannerPortCname = "";
		String qrResponseStatus = "";

		setSequencePathId("p1"); // Re-setting sequence path id

		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		TestInterfaceStatus qrScannerTestIntefaceStatus = new TestInterfaceStatus();
		TerminalProfileSetting terminalBayProfile = null;

		try {
			terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
					.findByBayKey(ConstantConveyor.FT_BAY_KEY);
			if (terminalBayProfile == null) {
				Ft.logger.error(String.format(
						"[%s] : [DUT_QR_SCAN] : [CONFIG_ERROR] - Terminal profile setting not found for bay key: %s",
						getMyBayKey(), ConstantConveyor.FT_BAY_KEY));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Use specific error code
				return bayResponse;
			}
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [DUT_QR_SCAN] : [DB_ERROR] - Failed to retrieve terminal profile for bay: %s. Error: %s",
					getMyBayKey(), ConstantConveyor.FT_BAY_KEY, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Use specific error code
			return bayResponse;
		}

		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		String qrScannerDeviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER +
				String.format("%02d", positionNum);

		DeviceSetting qrScannerDeviceSetting = null;
		try {
			qrScannerDeviceSetting = deviceDataManager.getDeviceSettingByDeviceId(qrScannerDeviceId);
			if (qrScannerDeviceSetting == null) {
				Ft.logger.error(String.format(
						"[%s] : [DUT_QR_SCAN] : [CONFIG_ERROR] - Device setting not found for QR scanner ID: %s at position %d",
						getMyBayKey(), qrScannerDeviceId, positionNum));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
				return bayResponse;
			}
			qrScannerPortCname = qrScannerDeviceSetting.getCanName();
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [DUT_QR_SCAN] : [DEVICE_MANAGER_ERROR] - Failed to get device setting for QR scanner ID: %s. Error: %s",
					getMyBayKey(), qrScannerDeviceId, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			return bayResponse;
		}

		qrScannerTestIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_07,
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getSequencePathId(),
				"" + positionNum,
				qrScannerDeviceSetting.getPortName(),
				qrScannerPortCname,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				ConstantConveyor.DUT_QR_SERIAL_NO_READ_CMD_RESULT_TEST_NAME,
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		int serialNo = StateExecutorController.addToTestStatusGui(qrScannerTestIntefaceStatus);
		qrScannerTestIntefaceStatus.setSerialNo(String.valueOf(serialNo));
		Ft.logger.debug(String.format(
				"[%s] : [DUT_QR_SCAN] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus to GUI for position %d. SerialNo: %s",
				getMyBayKey(), positionNum, serialNo));

		String scannedData = "";
		try {
			scannedData = qrScannerObj.scan_QR_code(positionNum);
			Ft.logger.debug(
					String.format("[%s] : [DUT_QR_SCAN] : [RAW_SCANNED_DATA] - Raw scanned data for position %d: '%s'",
							getMyBayKey(), positionNum, scannedData));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [DUT_QR_SCAN] : [SCAN_EXCEPTION] - Exception during QR scan for position %d. Error: %s",
					getMyBayKey(), positionNum, e.getMessage()), e);
			qrResponseStatus = "SCAN_EXCEPTION";
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Failed");
			qrScannerTestIntefaceStatus.setDeviceResponseData("Scan Exception");
			bayResponse.setStatus(false);
			bayResponse.setResponseData("SCAN_EXCEPTION");
		}

		// Evaluate scanned data
		if (scannedData.equals("NO_QR_CODE_AVAILABLE")) {
			qrResponseStatus = "NO_QR_CODE_AVAILABLE";
			qrScannerTestIntefaceStatus.setDeviceResponseData(qrResponseStatus);
			bayResponse.setStatus(false);
			bayResponse.setResponseData(scannedData);
			Ft.logger.warn(String.format("[%s] : [DUT_QR_SCAN] : [NO_DATA] - No QR code available for position %d.",
					getMyBayKey(), positionNum));
		} else if (scannedData.equals("SCNR_NW")) {
			qrResponseStatus = "SCNR_NW";
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Failed");
			qrScannerTestIntefaceStatus.setDeviceResponseData(qrResponseStatus);
			bayResponse.setStatus(false);
			bayResponse.setResponseData(scannedData);
			Ft.logger.error(
					String.format("[%s] : [DUT_QR_SCAN] : [NETWORK_ERROR] - Scanner network error for position %d.",
							getMyBayKey(), positionNum));
		} else if (scannedData.equals(ConstantConveyor.COMM_ACCESS_FAILED)) {
			qrResponseStatus = ConstantConveyor.COMM_ACCESS_FAILED;
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Failed");
			qrScannerTestIntefaceStatus.setSerialStatus(qrResponseStatus);
			bayResponse.setStatus(false);
			bayResponse.setResponseData(scannedData);
			Ft.logger.error(
					String.format("[%s] : [DUT_QR_SCAN] : [COMM_FAILED] - Communication access failed for position %d.",
							getMyBayKey(), positionNum));
		} else if (scannedData.equals(NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII)) {
			qrResponseStatus = NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII;
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Failed");
			qrScannerTestIntefaceStatus.setSerialStatus("Success"); // Communication was fine, but read was bad
			qrScannerTestIntefaceStatus.setDeviceResponseData(scannedData);
			bayResponse.setStatus(false);
			bayResponse.setResponseData(scannedData);
			Ft.logger.warn(String.format(
					"[%s] : [DUT_QR_SCAN] : [BAD_READ] - Incomplete/malformed QR read for position %d: '%s'",
					getMyBayKey(), positionNum, scannedData));
		} else if (scannedData.equals(NewlandQRCodeScanner.QR_METER_SERIAL_NUMBER_SCAN_FAILED)) {
			qrResponseStatus = NewlandQRCodeScanner.QR_METER_SERIAL_NUMBER_SCAN_FAILED;
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Failed");
			qrScannerTestIntefaceStatus.setSerialStatus("Success"); // Communication was fine, but scan failed for
																	// specific reason
			qrScannerTestIntefaceStatus.setDeviceResponseData(scannedData);
			bayResponse.setStatus(false);
			bayResponse.setResponseData(scannedData);
			Ft.logger.error(String.format(
					"[%s] : [DUT_QR_SCAN] : [SCAN_FAILED_SPECIFIC] - QR meter serial number scan failed for position %d: '%s'",
					getMyBayKey(), positionNum, scannedData));
		} else {
			qrResponseStatus = "GOOD";
			qrScannerTestIntefaceStatus.setDeviceResponseStatus("Success");
			qrScannerTestIntefaceStatus.setSerialStatus("Success");
			bayResponse.setStatus(true);

			String newLineFilteredScannedData = scannedData.replace("\r\n", "");
			qrScannerTestIntefaceStatus.setDeviceResponseData("DutQrRead: " + newLineFilteredScannedData);
			bayResponse.setResponseData(newLineFilteredScannedData);
			String dutSerialNo = newLineFilteredScannedData;
			Ft.logger.info(
					String.format("[%s] : [DUT_QR_SCAN] : [GOOD_READ] - Successfully scanned QR for position %d: '%s'",
							getMyBayKey(), positionNum, dutSerialNo));

			PalletTrackerController palletTracker = new PalletTrackerController();
			Callable<Boolean> taskAddMetersToPalletDb = () -> {
				boolean queueStatus = palletTracker.addMetersToPallet(getMyBayKey(), dutSerialNo, positionNum);
				Ft.logger.debug(String.format(
						"[%s] : [DUT_QR_SCAN] : [DB_ADD_STATUS] - Add meters to pallet DB for position %d: %s",
						getMyBayKey(), positionNum, queueStatus));
				return queueStatus;
			};

			long timeoutInSec = 10;
			Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_FT_DB,
					taskAddMetersToPalletDb, 5, timeoutInSec, TimeUnit.SECONDS);

			try {
				boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS);
				Ft.logger.debug(String.format(
						"[%s] : [DUT_QR_SCAN] : [DB_TASK_COMPLETE] - DB add task for position %d completed with status: %s",
						getMyBayKey(), positionNum, responseStatus));
			} catch (TimeoutException e) {
				Ft.logger.error(String.format(
						"[%s] : [DUT_QR_SCAN] : [DB_TASK_TIMEOUT] - DB add task for position %d timed out after %d seconds. Error: %s",
						getMyBayKey(), positionNum, timeoutInSec, e.getMessage()), e);
				bayResponse.setStatus(false); // Mark as failed due to DB timeout
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Specific error code
			} catch (ExecutionException e) {
				Ft.logger.error(String.format(
						"[%s] : [DUT_QR_SCAN] : [DB_TASK_FAILED] - DB add task for position %d failed. Cause: %s",
						getMyBayKey(), positionNum, e.getCause() != null ? e.getCause().getMessage() : "Unknown"),
						e.getCause());
				bayResponse.setStatus(false); // Mark as failed due to DB execution error
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			} catch (InterruptedException e) {
				Ft.logger.error(String.format(
						"[%s] : [DUT_QR_SCAN] : [DB_TASK_INTERRUPTED] - DB add task for position %d interrupted. Error: %s",
						getMyBayKey(), positionNum, e.getMessage()), e);
				Thread.currentThread().interrupt(); // Restore the interrupted status
				bayResponse.setStatus(false); // Mark as failed due to interruption
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			}

			// UI update: update dashboard with serial number
			final String finalDutSerialNo = dutSerialNo; // Need final variable for lambda
			Platform.runLater(() -> {
				if (ConveyorDataManager.getDashboardObject() != null) {
					ConveyorDataManager.getDashboardObject().updateSerialByBayAndPosition(getMyBayKey(), positionNum,
							finalDutSerialNo);
					Ft.logger.debug(
							String.format("[%s] : [UI_UPDATE] : Dashboard updated for position %d with serial: %s",
									getMyBayKey(), positionNum, finalDutSerialNo));
				} else {
					Ft.logger.warn(String.format(
							"[%s] : [UI_UPDATE_WARNING] : Dashboard object is null, cannot update serial for position %d.",
							getMyBayKey(), positionNum));
				}
			});
		}

		if (!qrResponseStatus.equals("GOOD")) {
			// UI update: update dashboard with "Scan Failed"
			Platform.runLater(() -> {
				if (ConveyorDataManager.getDashboardObject() != null) {
					ConveyorDataManager.getDashboardObject().updateSerialByBayAndPosition(getMyBayKey(), positionNum,
							"Scan Failed");
					Ft.logger.warn(
							String.format("[%s] : [UI_UPDATE] : Dashboard updated for position %d with 'Scan Failed'.",
									getMyBayKey(), positionNum));
				} else {
					Ft.logger.warn(String.format(
							"[%s] : [UI_UPDATE_WARNING] : Dashboard object is null, cannot update 'Scan Failed' for position %d.",
							getMyBayKey(), positionNum));
				}
			});
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			qrResponseStatus = "GOOD"; // This might overwrite actual failure status for simulation check
			bayResponse.setStatus(true); // Ensure bayResponse status is also set for happy path
			Ft.logger
					.debug(String.format("[%s] : [SIMULATION] : DUT QR scan status overridden to GOOD for position %d.",
							getMyBayKey(), positionNum));
		}

		qrScannerTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		StateExecutorController.updateTestStatusGui(qrScannerTestIntefaceStatus); // Update GUI

		Ft.logger.info(String.format(
				"[%s] : [DUT_QR_SCAN] : [EXIT] - Processing QR scan for DUT at position %d completed. Status: %s",
				getMyBayKey(), positionNum, bayResponse.getStatus()));
		return bayResponse;
	}

	public BayResponse dutOpticalSerialNoWriteProcess(int positionNum) {
		Ft.logger.info(String.format(
				"[%s] : [OPTICAL_WRITE_PROCESS] : [ENTRY] - Starting optical serial number write process for position: %d",
				getMyBayKey(), positionNum));
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false); // Default to false

		DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Ft.logger);
		String opticalReaderPortCname = "";

		TestInterfaceStatus opticalReaderTestIntefaceStatus = new TestInterfaceStatus();
		TerminalProfileSetting terminalBayProfile = null;

		try {
			terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
					.findByBayKey(ConstantConveyor.FT_BAY_KEY);
			if (terminalBayProfile == null) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [CONFIG_ERROR] - Terminal profile setting not found for bay key: %s",
						getMyBayKey(), ConstantConveyor.FT_BAY_KEY));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Generic config error
				return bayResponse;
			}
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [DB_ERROR] - Failed to retrieve terminal profile for bay: %s. Error: %s",
					getMyBayKey(), ConstantConveyor.FT_BAY_KEY, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			return bayResponse;
		}

		setSequencePathId("p2"); // Setting sequence path id for this process

		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		String opticalReaderDeviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_DUT +
				String.format("%02d", positionNum);

		Ft.logger.debug(
				String.format("[%s] : [OPTICAL_WRITE_PROCESS] : [DEVICE_ID] - Optical device ID: %s for Position: %d",
						getMyBayKey(), opticalReaderDeviceId, positionNum));

		DeviceSetting opticalReaderDeviceSetting = null;
		try {
			opticalReaderDeviceSetting = deviceDataManager.getDeviceSettingByDeviceId(opticalReaderDeviceId);
			if (opticalReaderDeviceSetting == null) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [CONFIG_ERROR] - Device setting not found for optical reader ID: %s at position %d",
						getMyBayKey(), opticalReaderDeviceId, positionNum));
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
				return bayResponse;
			}
			opticalReaderPortCname = opticalReaderDeviceSetting.getCanName();

			Ft.logger.debug(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [PORT_INFO] - Optical port CName: %s, PortName: %s for Position: %d",
					getMyBayKey(), opticalReaderPortCname, opticalReaderDeviceSetting.getPortName(), positionNum));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [DEVICE_MANAGER_ERROR] - Failed to get device setting for optical reader ID: %s. Error: %s",
					getMyBayKey(), opticalReaderDeviceId, e.getMessage()), e);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			return bayResponse;
		}

		opticalReaderTestIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.FT_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_07,
				ConstantConveyor.DEVICE_TYPE_DUT,
				getSequencePathId(),
				"" + positionNum,
				opticalReaderDeviceSetting.getPortName(),
				opticalReaderPortCname,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME,
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		int serialNo = StateExecutorController.addToTestStatusGui(opticalReaderTestIntefaceStatus);
		opticalReaderTestIntefaceStatus.setSerialNo(String.valueOf(serialNo));
		Ft.logger.debug(String.format(
				"[%s] : [OPTICAL_WRITE_PROCESS] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus to GUI for position %d. SerialNo: %s",
				getMyBayKey(), positionNum, serialNo));

		SpmDut spManager = null;
		try {
			spManager = devSysEnergyMeter.serialPortInitV2(opticalReaderDeviceSetting);
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [SERIAL_PORT_EXCEPTION] - Exception during serial port initialization for position %d. Error: %s",
					getMyBayKey(), positionNum, e.getMessage()), e);
			spManager = null; // Ensure spManager is null on exception
		}

		if (spManager == null) {
			opticalReaderTestIntefaceStatus.setSerialStatus(ConstantConveyor.COMM_ACCESS_FAILED);
			bayResponse.setStatus(false);
			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [INIT_FAILED] - Serial port initialization failed for position %d.",
					getMyBayKey(), positionNum));
			return bayResponse;
		} else {
			opticalReaderTestIntefaceStatus.setSerialStatus("Success");
			bayResponse.setStatus(true);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus);
			Ft.logger.debug(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [INIT_SUCCESS] - Serial port initialized successfully for position %d.",
					getMyBayKey(), positionNum));
		}

		// 1. Communication Check (Placeholder)
		boolean status = checkAckForCommands(); // This method needs actual implementation
		if (!status) {
			Ft.logger.warn(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [ACK_CHECK_FAILED] - Acknowledgment check failed for position %d. Proceeding with caution or returning.",
					getMyBayKey(), positionNum));
		}

		// Send Device Unlock Command
		try {
			devSysEnergyMeter.sendDeviceUnlockCommand(positionNum, spManager);
			Ft.logger.debug(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [UNLOCK_CMD] - Device unlock command sent for position %d.",
					getMyBayKey(), positionNum));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [UNLOCK_CMD_ERROR] - Failed to send device unlock command for position %d. Error: %s",
					getMyBayKey(), positionNum, e.getMessage()), e);
			bayResponse.setStatus(false);
		}

		PalletTrackerController palletTracker = new PalletTrackerController();
		String selectedBayTypeKey = myBayKey;
		String myPalletDistinctId = PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);
		PalletManage myPalletManage = null;

		try {
			myPalletManage = MySqlServiceManager.getPalletManageService()
					.findFirstByPalletDistinctId(myPalletDistinctId);
			if (myPalletManage == null) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [DB_ERROR] - Pallet distinct ID: %s not found in DB.",
						getMyBayKey(), myPalletDistinctId));
				bayResponse.setStatus(false);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006); // Use a relevant error code
				spManager.disconnectDut();
				return bayResponse;
			}
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [DB_ERROR] - Exception while fetching pallet details for ID: %s. Error: %s",
					getMyBayKey(), myPalletDistinctId, e.getMessage()), e);
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_006);
			spManager.disconnectDut();
			return bayResponse;
		}

		// 2. Read and Map Serial Number of all meters
		if (status) {
			Ft.logger.info(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [COMM_CHECK_SUCCESS] - Communication check success for position: %d",
					getMyBayKey(), positionNum));

			Map<String, Object> result = new HashMap<>();
			testType = "FT";
			testCaseName = ConstantConveyor.DUT_OPTICAL_SERIAL_NO_READ_CMD_RESULT_TEST_NAME;

			try {
				result = devSysEnergyMeter.readSerialNumOfMeter(positionNum, spManager);
				status = (boolean) result.getOrDefault("status", false);
				meterSerialNumber = (String) result.getOrDefault("meterSerialNumber", "");
			} catch (Exception e) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [SERIAL_READ_ERROR] - Failed to read serial number for position %d. Error: %s",
						getMyBayKey(), positionNum, e.getMessage()), e);
				status = false;
				meterSerialNumber = "ERROR_READ";
			}

			if (status) {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
				bayResponse.setStatus(true);
				resultStatus = ConstantReport.REPORT_POPULATE_PASS;
				Ft.logger.info(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [SERIAL_READ_SUCCESS] - Serial number read for position %d: %s",
						getMyBayKey(), positionNum, meterSerialNumber));
			} else {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Failed");
				bayResponse.setStatus(false);
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [SERIAL_READ_FAILED] - Failed to read serial number for position %d. Status: %s",
						getMyBayKey(), positionNum, resultStatus));
			}

			resultValue = resultStatus;

			opticalReaderTestIntefaceStatus.setDeviceResponseData("Dut S.N= " + meterSerialNumber);

			try {
				palletTracker.addMetersToPallet(getMyBayKey(), meterSerialNumber, positionNum);
				Ft.logger.debug(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [ADD_METER_DB] - Meter %s added to pallet for position %d.",
						getMyBayKey(), meterSerialNumber, positionNum));
			} catch (Exception e) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [ADD_METER_DB_ERROR] - Failed to add meter %s to pallet DB for position %d. Error: %s",
						getMyBayKey(), meterSerialNumber, positionNum, e.getMessage()), e);
				// Decide if this failure should make bayResponse.setStatus(false)
			}

			try {
				palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType,
						testCaseName);
				Ft.logger.debug(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [ADD_RESULT_DB] - Result added to meter for position %d. Status: %s",
						getMyBayKey(), positionNum, resultStatus));
			} catch (Exception e) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [ADD_RESULT_DB_ERROR] - Failed to add result to meter DB for position %d. Error: %s",
						getMyBayKey(), positionNum, e.getMessage()), e);
				// Decide if this failure should make bayResponse.setStatus(false)
			}

			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus); // Update GUI
		} else {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [SERIAL_READ_PRE_CHECK_FAILED] - Communication check failed or prior step failed for position: %d",
					getMyBayKey(), positionNum));
			bayResponse.setStatus(false);
			spManager.disconnectDut();
			return bayResponse;
		}
		// =================================================================================================================

		// 4.6 Relay ON
		if (status) { // This `status` is from the serial number read.
			Ft.logger.info(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [RELAY_ON_CMD] - Sending Relay ON command for position: %d",
					getMyBayKey(), positionNum));

			setSequencePathId("p2"); // Re-setting sequence path id

			// Resetting these local variables for this step's context, if they are reused.
			resultValue = "";
			testType = "FT";
			testCaseName = ConstantConveyor.RELAY_ON_CMD_RESULT_TEST_NAME;

			opticalReaderTestIntefaceStatus = new TestInterfaceStatus(
					getMyBayKey(),
					getMyBaySeqId(),
					ConstantConveyor.DEVICE_TYPE_DUT,
					getSequencePathId(),
					"" + positionNum,
					opticalReaderDeviceSetting.getPortName(), // Ensure port name is correct
					opticalReaderPortCname,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					testCaseName,
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			serialNo = StateExecutorController.addToTestStatusGui(opticalReaderTestIntefaceStatus);
			opticalReaderTestIntefaceStatus.setSerialNo(String.valueOf(serialNo));
			Ft.logger.debug(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [TEST_STATUS_GUI_ADD] - Added TestInterfaceStatus for Relay ON. Position %d. SerialNo: %s",
					getMyBayKey(), positionNum, serialNo));

			boolean relayCommandStatus = false;
			try {
				relayCommandStatus = devSysEnergyMeter.sendRelayOnCommand(spManager);
			} catch (Exception e) {
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [RELAY_ON_ERROR] - Failed to send Relay ON command for position %d. Error: %s",
						getMyBayKey(), positionNum, e.getMessage()), e);
				relayCommandStatus = false;
			}

			if (relayCommandStatus) {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Success");
				resultStatus = ConstantReport.REPORT_POPULATE_PASS;
				bayResponse.setStatus(true);
				Ft.logger.info(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [RELAY_ON_SUCCESS] - Relay ON command successful for position %d.",
						getMyBayKey(), positionNum));
			} else {
				opticalReaderTestIntefaceStatus.setDeviceResponseStatus("Failed");
				resultStatus = ConstantReport.REPORT_POPULATE_FAIL;
				bayResponse.setStatus(false);
				Ft.logger.error(String.format(
						"[%s] : [OPTICAL_WRITE_PROCESS] : [RELAY_ON_FAILED] - Relay ON command failed for position %d.",
						getMyBayKey(), positionNum));
			}

			palletTracker.addResultToMeter(positionNum, resultStatus, resultValue, getMyBayKey(), testType,
					testCaseName); // resultValue is empty here.

			opticalReaderTestIntefaceStatus.setTestStatus(ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
			StateExecutorController.updateTestStatusGui(opticalReaderTestIntefaceStatus); // Update GUI
			BayUtils.delay(1000); // Blocking delay, ensures this runs in background thread
		} else {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [RELAY_ON_SKIPPED] - Relay ON command skipped for position %d due to prior failure (Serial Number Read Failed).",
					getMyBayKey(), positionNum));
			bayResponse.setStatus(false);
			spManager.disconnectDut();
		}

		// Always disconnect DUT
		try {
			spManager.disconnectDut();
			Ft.logger.debug(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [DUT_DISCONNECT] - DUT disconnected for position %d.",
					getMyBayKey(), positionNum));
		} catch (Exception e) {
			Ft.logger.error(String.format(
					"[%s] : [OPTICAL_WRITE_PROCESS] : [DUT_DISCONNECT_ERROR] - Exception during DUT disconnect for position %d. Error: %s",
					getMyBayKey(), positionNum, e.getMessage()), e);
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			bayResponse.setStatus(true); // Overriding status for happy path simulation
			Ft.logger.debug(String.format(
					"[%s] : [SIMULATION] : Optical serial write process status overridden to TRUE for position %d.",
					getMyBayKey(), positionNum));
		}

		Ft.logger.info(String.format(
				"[%s] : [OPTICAL_WRITE_PROCESS] : [EXIT] - Optical serial number write process completed for position %d. Final status: %s",
				getMyBayKey(), positionNum, bayResponse.getStatus()));
		return bayResponse;
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

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public String getMyBaySeqId() {
		return myBaySeqId;
	}

	public void setMyBaySeqId(String myBaySeqId) {
		this.myBaySeqId = myBaySeqId;
	}
}
