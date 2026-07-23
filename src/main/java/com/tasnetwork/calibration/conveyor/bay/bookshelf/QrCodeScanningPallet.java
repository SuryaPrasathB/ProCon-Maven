package com.tasnetwork.calibration.conveyor.bay.bookshelf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_Pallet_Bay_Map;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.PalletBayState;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class QrCodeScanningPallet {

	private Logger eachBaylogger = null;
	boolean simulateBayHappyPath = false;
	private String bayKey = "";
	private String stateManageSeqNo = "";
	private String failPathErrorCode = ConvErrorCodeMapping.ERROR_CODE_602;
	private int palletQrScannerPositionId = -1;// ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID

	private String flowPathId = "p1";
	private TestInterfaceStatus deviceTestInterfaceStatus = new TestInterfaceStatus();

	public QrCodeScanningPallet(Logger logger, String bayKey, String stateManageSeqNo,
			int palletQrScannerPositionId, String failPathErrorCode, boolean simulateHpPath) {
		this.eachBaylogger = logger;
		this.bayKey = bayKey;
		this.stateManageSeqNo = stateManageSeqNo;
		this.palletQrScannerPositionId = palletQrScannerPositionId;
		this.failPathErrorCode = failPathErrorCode;
		this.simulateBayHappyPath = simulateHpPath;
	}

	public BayResponse qrCodePalletScanningProcess() {
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		setFlowPathId("p1");
		setDeviceTestInterfaceStatus(null);
		// String status = qR_Code_Scanning_of_Pallet(); // Call the function to scan QR
		// code
		bayResponse = qrCodePalletScanning();
		String status = bayResponse.getResponseData();// (String)responseReturn.get("responseData");

		StateExecutorController.updateTestInterfaceStatusOnGuiV2(bayResponse,
				ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		if (status.equals("GOOD")) {
			eachBaylogger.info("qrCodePalletScanningProcess : QR Code Scan Successful" + " : " + getBayKey());
			// Logic for success case (status is true)
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601); // Success error code
		} else if (status.equals("NO_QR_CODE_AVAILABLE")) {
			eachBaylogger.info("qrCodePalletScanningProcess : QR Code Scan Failed" + " : " + getBayKey());
			eachBaylogger.info("qrCodePalletScanningProcess : Issue with Pallet Side" + " : " + getBayKey());
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(getFailPathErrorCode()); // Failure error code
		} else if (status.equals("SCNR_NW")) {
			eachBaylogger.info("qrCodePalletScanningProcess : QR Code Scan Failed" + " : " + getBayKey());
			eachBaylogger.info("qrCodePalletScanningProcess : Issue with Scanner Side" + " : " + getBayKey());
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(getFailPathErrorCode()); // Failure error code
		} else if (status.equals(ConstantConveyor.COMM_ACCESS_FAILED)) {
			eachBaylogger
					.info("qrCodePalletScanningProcess : QR Code Scan serial port access Failed" + " : " + getBayKey());
			// eachBaylogger.info("qrCodePalletScanningProcess : Issue with Scanner Side");
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(getFailPathErrorCode()); // Failure error code
		} else if (status.equals("ALREADY_COMPLETED")) {
			eachBaylogger.info("qrCodePalletScanningProcess : Test Already Completed" + " : " + getBayKey());
			bayResponse.setStatus(false);
			bayResponse.setErrorCode("TEST_ALREADY_COMPLETED");
		} else if (status.equals(NewlandQRCodeScanner.NOT_GOOD_READ_EXPECTED_DATA_IN_ASCII)) {
			eachBaylogger.info("qrCodePalletScanningProcess : QR Code Scan - not Good Read" + " : " + getBayKey());
			// eachBaylogger.info("qrCodePalletScanningProcess : Issue with Scanner Side");
			// Logic for failure case (status is false)
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(getFailPathErrorCode()); // Failure error code
		}

		eachBaylogger.info("qrCodePalletScanningProcess : Exit : " + getBayKey());

		return bayResponse;
	}

	private BayResponse qrCodePalletScanning() {

		eachBaylogger.debug("qrCodePalletScanning : Entry" + " : " + getBayKey());

		String status = "";
		/*
		 * Map<String,Object> responseReturn = new HashMap<String,Object>();
		 * responseReturn.put("status", false);
		 */

		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(false);
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(getBayKey());
		String deviceId = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
				terminalBayProfile.getBayId() +
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER +
				String.format("%02d", getPalletQrScannerPositionId());

		ApplicationLauncher.logger.debug("qrCodePalletScanning : deviceId : " + deviceId + " : " + getBayKey());

		ConveyorDataManager deviceDataManager = new ConveyorDataManager();
		DeviceSetting deviceSetting = deviceDataManager.getDeviceSettingByDeviceId(deviceId);
		ApplicationLauncher.logger
				.debug("qrCodePalletScanning : getPortName : " + deviceSetting.getPortName() + " : " + getBayKey());
		ApplicationLauncher.logger
				.debug("qrCodePalletScanning : getcName : " + deviceSetting.getCanName() + " : " + getBayKey());

		testIntefaceStatus = new TestInterfaceStatus(
				getBayKey(),
				getStateManageSeqNo(),
				ConstantConveyor.DEVICE_TYPE_QR_SCANNER,
				getFlowPathId(),
				"" + getPalletQrScannerPositionId(),
				deviceSetting.getPortName(), // portName
				deviceSetting.getCanName(),
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"Waiting",
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);

		// TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		// terminalBayProfile =
		// MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(getBayKey());

		NewlandQRCodeScanner qrScannerObj = new NewlandQRCodeScanner(terminalBayProfile);
		String scannedData = qrScannerObj.scan_QR_code(getPalletQrScannerPositionId());

		eachBaylogger.debug("qrCodePalletScanning : scannedData: " + scannedData);
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
			eachBaylogger.debug("qrCodePalletScanning : Else Hit1: ");
			PalletTrackerController palletTrackerController = new PalletTrackerController();
			String selectedBayTypeKey = getBayKey();
			String palletQrId = scannedData.replace("\r", "").replace("\n", "");

			String palletDistinctId = PalletTrackerController.getActivePalletMap().get(palletQrId);

			// Check if test is already completed
			boolean isAlreadyCompleted = false;
			try {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(palletDistinctId);
				if (myPalletManage != null) {
					PalletBayState presentBayState = myPalletManage.getPalleteBayStateList().stream()
							.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
							.findFirst()
							.orElse(null);
					if (presentBayState != null && Boolean.TRUE.equals(presentBayState.isTestCompleted())) {
						isAlreadyCompleted = true;
					}
				}
			} catch (Exception e) {
				eachBaylogger.error("Error checking testCompleted flag: " + e.getMessage());
			}

			if (isAlreadyCompleted) {
				status = "ALREADY_COMPLETED";
				testIntefaceStatus.setDeviceResponseStatus("Success");
				testIntefaceStatus.setDeviceResponseData("ALREADY_COMPLETED");
			} else {
				status = "GOOD";
				testIntefaceStatus.setDeviceResponseStatus("Success");
				testIntefaceStatus.setDeviceResponseData(scannedData);
			}

			eachBaylogger.debug("qrCodePalletScanning: getActivePalletMap palletDistinctId : " + palletDistinctId
					+ "  selectedBayTypeKey: " + selectedBayTypeKey);

			refreshDashBoard(palletTrackerController, palletQrId, selectedBayTypeKey, palletDistinctId);

			Constant_Pallet_Bay_Map.setFT_PALLET_QR(palletQrId);

			eachBaylogger.debug("qrCodePalletScanning : palletDistinctId: " + " : " + palletDistinctId +
					" : bay: " + selectedBayTypeKey + " : qrCode: " + palletQrId);
			bayResponse.setMyPalletDistinctId(palletDistinctId);
			bayResponse.setMyPalletQrCode(palletQrId);
		}

		StateExecutorController.updateTestStatusGui(testIntefaceStatus);

		if (status.equals("GOOD")) {
			bayResponse.setStatus(true);
		}

		if (StateExecutorController.simulateFtBayHappyPath) {
			bayResponse.setStatus(true);
		}

		bayResponse.setResponseData(status);
		bayResponse.setTestInterfaceStatus(testIntefaceStatus);

		eachBaylogger.debug("qrCodePalletScanning : status : " + status + " : " + getBayKey());

		eachBaylogger.debug("qrCodePalletScanning : Exit" + " : " + getBayKey());

		return bayResponse;
	}

	public void refreshDashBoard(PalletTrackerController palletTrackerController, String palletQrId,
			String selectedBayTypeKey, String palletDistinctId) {

		Map<Integer, String> meterListWithSerialNoMap = new HashMap<Integer, String>();

		PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
				.findFirstByPalletDistinctId(palletDistinctId);

		Set<PalletMeter> palletMeterSetList = new HashSet<PalletMeter>();
		if (myPalletManage != null) {
			palletMeterSetList = myPalletManage.getPalletMeterList();
			palletMeterSetList.stream()
					.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
			for (PalletMeter eachPalletMeter : palletMeterSetList) {
				meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(), eachPalletMeter.getMeterSerialNo());
			}
		}

		boolean batchUpdate = false;
		List<PalletManage> selectedPalletManageList = new ArrayList<PalletManage>();
		if (selectedBayTypeKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: refreshDashBoard: verification bay");
			batchUpdate = true;
			BayUtils bayUtils = new BayUtils();
			String bayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
			List<PalletManage> palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
			eachBaylogger
					.debug("qrCodePalletScanning: refreshDashBoard: palletManageList size: " + palletManageList.size());
			boolean scannedPalletQrIdExist = false;
			for (PalletManage eachPalletManage : palletManageList) {
				ApplicationLauncher.logger.debug("refreshDashBoard: fetchPalletsByBayState: getPalletDistinctId:    "
						+ eachPalletManage.getPalletDistinctId());
				ApplicationLauncher.logger
						.debug("refreshDashBoard : fetchPalletsByBayState: palletQrId:    " + palletQrId);

				if (eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
					scannedPalletQrIdExist = true;
					ApplicationLauncher.logger
							.debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list");
				}
			}
			if (!scannedPalletQrIdExist) {
				ApplicationLauncher.logger.debug(
						"refreshDashBoard: fetchPalletsByBayState: in existing list scanned pallet qr id does not exist : "
								+ palletQrId);
				Platform.runLater(() -> {
					String header = "Scanned qr id not found in active list : " + palletQrId;
					String title = "Verification Bay";
					String userInputData = GuiUtils.textFieldInputDialogDisplay(header, title);

					if (!userInputData.isEmpty()) {
						ApplicationLauncher.logger
								.debug("refreshDashBoard: VERIFICATION_BAY: userInputData: " + userInputData);
					}
				});
				bayKey = ConstantConveyor.WAITING_BAY_KEY;
				palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
				eachBaylogger.debug("qrCodePalletScanning: refreshDashBoard: waiting bay palletManageList size: "
						+ palletManageList.size());
				for (PalletManage eachPalletManage : palletManageList) {
					eachBaylogger.debug("qrCodePalletScanning: refreshDashBoard: waiting bay getPalletDistinctId: "
							+ eachPalletManage.getPalletDistinctId());
				}

			} else {
				selectedPalletManageList = palletManageList;
			}

		} else if (selectedBayTypeKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: refreshDashBoard: STA1 bay");
			batchUpdate = true;
			BayUtils bayUtils = new BayUtils();
			String bayKey = ConstantConveyor.STA_NLD1_BAY_KEY;
			List<PalletManage> palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
			eachBaylogger
					.debug("qrCodePalletScanning: refreshDashBoard: palletManageList size: " + palletManageList.size());
			boolean scannedPalletQrIdExist = false;
			for (PalletManage eachPalletManage : palletManageList) {
				ApplicationLauncher.logger.debug("refreshDashBoard: fetchPalletsByBayState: getPalletDistinctId:    "
						+ eachPalletManage.getPalletDistinctId());
				ApplicationLauncher.logger
						.debug("refreshDashBoard : fetchPalletsByBayState: palletQrId:    " + palletQrId);

				if (eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
					scannedPalletQrIdExist = true;
					ApplicationLauncher.logger
							.debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list");
				}
			}
			if (!scannedPalletQrIdExist) {
				ApplicationLauncher.logger.debug(
						"refreshDashBoard: fetchPalletsByBayState: in existing list scanned pallet qr id does not exist : "
								+ palletQrId);
				Platform.runLater(() -> {
					String header = "Scanned qr id not found in active list : " + palletQrId;
					String title = "Sta1 Bay";
					String userInputData = GuiUtils.textFieldInputDialogDisplay(header, title);

					if (!userInputData.isEmpty()) {
						ApplicationLauncher.logger
								.debug("refreshDashBoard: STA_NLD1_BAY_KEY: userInputData: " + userInputData);
					}
				});
				bayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
				palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
				eachBaylogger.debug(
						"qrCodePalletScanning: refreshDashBoard: VERIFICATION_BAY_KEY bay palletManageList size: "
								+ palletManageList);

			} else {
				selectedPalletManageList = palletManageList;
			}

		} else if (selectedBayTypeKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: refreshDashBoard: STA2 bay");
			batchUpdate = true;
			BayUtils bayUtils = new BayUtils();
			String bayKey = ConstantConveyor.STA_NLD2_BAY_KEY;
			List<PalletManage> palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
			eachBaylogger
					.debug("qrCodePalletScanning: refreshDashBoard: palletManageList size: " + palletManageList.size());
			boolean scannedPalletQrIdExist = false;
			for (PalletManage eachPalletManage : palletManageList) {
				ApplicationLauncher.logger.debug("refreshDashBoard: fetchPalletsByBayState: getPalletDistinctId:    "
						+ eachPalletManage.getPalletDistinctId());
				ApplicationLauncher.logger
						.debug("refreshDashBoard : fetchPalletsByBayState: palletQrId:    " + palletQrId);

				if (eachPalletManage.getPalletDistinctId().contains(palletQrId)) {
					scannedPalletQrIdExist = true;
					ApplicationLauncher.logger
							.debug("refreshDashBoard : fetchPalletsByBayState: scannedPalletQrIdExist in fetch list");
				}
			}
			if (!scannedPalletQrIdExist) {
				ApplicationLauncher.logger.debug(
						"refreshDashBoard: fetchPalletsByBayState: in existing list scanned pallet qr id does not exist : "
								+ palletQrId);
				Platform.runLater(() -> {
					String header = "Scanned qr id not found in active list : " + palletQrId;
					String title = "Sta2 Bay";
					String userInputData = GuiUtils.textFieldInputDialogDisplay(header, title);

					if (!userInputData.isEmpty()) {
						ApplicationLauncher.logger
								.debug("refreshDashBoard: STA_NLD2_BAY_KEY: userInputData: " + userInputData);
					}
				});
				bayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
				palletManageList = bayUtils.fetchPalletsByBayState(bayKey);
				eachBaylogger.debug(
						"qrCodePalletScanning: refreshDashBoard: VERIFICATION_BAY_KEY bay palletManageList size: "
								+ palletManageList);

			} else {
				selectedPalletManageList = palletManageList;
			}

		} else if (selectedBayTypeKey.equals(ConstantConveyor.WAITING_BAY_KEY)) {

			eachBaylogger.debug("qrCodePalletScanning: Waiting Bay: refreshDashBoard:  AUTOMATE REPORTS");

		} else if (selectedBayTypeKey.equals(ConstantConveyor.UNLOADING_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: Unloading Bay: refreshDashBoard : AUTOMATE REPORTS");
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPalletQrIdAndExitNotAppeared(palletQrId);
			if (myPalletManageList.size() > 0) {
				ApplicationLauncher.logger.error("readPalletMetersData: getPalletDistinctId: "
						+ myPalletManageList.get(0).getPalletDistinctId());

				myPalletManage = myPalletManageList.get(0);
				palletMeterSetList = myPalletManage.getPalletMeterList();
				palletMeterSetList.stream()
						.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
				for (PalletMeter eachPalletMeter : palletMeterSetList) {
					meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
							eachPalletMeter.getMeterSerialNo());
				}
			}

		} else if (selectedBayTypeKey.equals(ConstantConveyor.REJECTION_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: rejected bay: ");
			palletTrackerController.addPalletBayState(selectedBayTypeKey);
		} else if (ConstantConveyor.ENTRY_BAY_LIST.contains(selectedBayTypeKey)) {
			eachBaylogger.debug("qrCodePalletScanning: EntryBAy List : No ActivePalletMap :  selectedBayTypeKey : "
					+ selectedBayTypeKey);

			palletDistinctId = palletTrackerController.addNewPalletManage(selectedBayTypeKey, palletQrId,
					meterListWithSerialNoMap);
			updatePalletViewer(selectedBayTypeKey, palletQrId);

		} else if (selectedBayTypeKey.equals(ConstantConveyor.UNLOADING_BAY_KEY)) {
			eachBaylogger.debug("qrCodePalletScanning: unloading bay: ");

		} else {
			eachBaylogger.debug("qrCodePalletScanning: other bays: ");

			if (PalletTrackerController.getActivePalletMap().containsKey(palletQrId)) {
				eachBaylogger.debug(
						"qrCodePalletScanning: getActivePalletMap exist : selectedBayTypeKey: " + selectedBayTypeKey);
				eachBaylogger.debug("getActivePalletMap() : " + PalletTrackerController.getActivePalletMap());

				palletDistinctId = PalletTrackerController.getActivePalletMap().get(palletQrId);

				updatePalletViewer(selectedBayTypeKey, palletQrId);
				palletTrackerController.addPalletBayState(selectedBayTypeKey);
			} else {
				eachBaylogger.debug(
						"qrCodePalletScanning: No ActivePalletMap-2 :  selectedBayTypeKey : " + selectedBayTypeKey);
			}
		}
		if (batchUpdate) {
			eachBaylogger.debug("qrCodePalletScanning: batch update ");
			// ConveyorDeviceDataManagerController.getDashboardObject().removeAllPalletsFromVerificationBays();
			if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
				ConveyorDataManager.getDashboardObject().removeAllPalletsFromVerificationBays();
				eachBaylogger.info("qrCodePalletScanning: batch update: all verific removed");
			} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
				ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta1Bays();
				eachBaylogger.info("qrCodePalletScanning: batch update: all STA1 removed");
			} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
				ConveyorDataManager.getDashboardObject().removeAllPalletsFromSta2Bays();
				eachBaylogger.info("qrCodePalletScanning: batch update: all STA2 removed");
			}
			BayUtils.delay(100);
			eachBaylogger.debug("qrCodePalletScanning: batch update : delay done :for removal: ");
			String palletName = "";
			for (PalletManage eachPalletManage : selectedPalletManageList) {
				meterListWithSerialNoMap.clear();
				palletMeterSetList = eachPalletManage.getPalletMeterList();
				palletMeterSetList.stream()
						.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
				for (PalletMeter eachPalletMeter : palletMeterSetList) {
					meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
							eachPalletMeter.getMeterSerialNo());
				}
				palletName = eachPalletManage.getPalletQrId();
				eachBaylogger.debug("qrCodePalletScanning: batch update : palletName: " + palletName);

				if (bayKey.equals(ConstantConveyor.VERIFICATION_BAY_KEY)) {
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableVerificationBay(palletName,
							meterListWithSerialNoMap);
					eachBaylogger.info("qrCodePalletScanning: batch update: verific added");
				} else if (bayKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) {
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableSta1Bay(palletName,
							meterListWithSerialNoMap);
					eachBaylogger.info("qrCodePalletScanning: batch update: STA1 added");
				} else if (bayKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY)) {
					ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableSta2Bay(palletName,
							meterListWithSerialNoMap);
					eachBaylogger.info("qrCodePalletScanning: batch update: STA2 added");
				}

				BayUtils.delay(50);
				eachBaylogger.debug("qrCodePalletScanning: batch update : delay done :palletName: " + palletName);

			}
		} else {

			eachBaylogger.debug("qrCodePalletScanning: individual pallet update ");
			ConveyorDataManager.getDashboardObject().removePalletFromBay(selectedBayTypeKey);
			ConveyorDataManager.getDashboardObject().addNewPalletViewDashboard(selectedBayTypeKey, palletQrId,
					meterListWithSerialNoMap);

			Map<Integer, MeterStatus> statusMap = new HashMap<>();
			statusMap.put(1, MeterStatus.IDLE);
			statusMap.put(2, MeterStatus.IDLE);
			statusMap.put(3, MeterStatus.IDLE);
			statusMap.put(4, MeterStatus.IDLE);
			statusMap.put(5, MeterStatus.IDLE);
			statusMap.put(6, MeterStatus.IDLE);

			Map<Integer, String> errorCodeMap = new HashMap<>();

			errorCodeMap.put(1, "");
			errorCodeMap.put(2, "");
			errorCodeMap.put(3, "");
			errorCodeMap.put(4, "");
			errorCodeMap.put(5, "");
			errorCodeMap.put(6, "");
			Platform.runLater(() -> {
				ConveyorDataManager.getDashboardObject().updateDashBoardPalletStatus(palletQrId, statusMap,
						errorCodeMap);
			});
		}
	}

	public void updatePalletViewer(String selectedBayTypeKey, String palletQrId) {
		switch (selectedBayTypeKey) {
			case "FTB":
				ConveyorDebugController.getRef_txtFTBayPallet().setText(palletQrId);
				break;
			case "HVB":
				ConveyorDebugController.getRef_txtHVBayPallet().setText(palletQrId);
				break;
			case "IRB":
				ConveyorDebugController.getRef_txtIRBayPallet().setText(palletQrId);
				break;
			case "CALB":
				ConveyorDebugController.getRef_txtCalibBayPallet().setText(palletQrId);
				break;
			default:
				break;
		}
	}

	public String getFlowPathId() {
		return flowPathId;
	}

	public void setFlowPathId(String sequencePathId) {
		this.flowPathId = sequencePathId;
	}

	public TestInterfaceStatus getDeviceTestInterfaceStatus() {
		return deviceTestInterfaceStatus;
	}

	public void setDeviceTestInterfaceStatus(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.deviceTestInterfaceStatus = palletAvailableTest_I_F_Status;
	}

	public String getBayKey() {
		return bayKey;
	}

	public void setBayKey(String bayKey) {
		this.bayKey = bayKey;
	}

	public String getFailPathErrorCode() {
		return failPathErrorCode;
	}

	public void setFailPathErrorCode(String failPathErrorCode) {
		this.failPathErrorCode = failPathErrorCode;
	}

	public int getPalletQrScannerPositionId() {
		return palletQrScannerPositionId;
	}

	public void setPalletQrScannerPositionId(int palletQrScannerPositionId) {
		this.palletQrScannerPositionId = palletQrScannerPositionId;
	}

	public String getStateManageSeqNo() {
		return stateManageSeqNo;
	}

	public void setStateManageSeqNo(String stateManageSeqNo) {
		this.stateManageSeqNo = stateManageSeqNo;
	}
}
