package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.bookshelf.QrCodeScanningPallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.dashboard.ErrorCode;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletTrackerController;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testreport.ReportUtils;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.calibration.energymeter.testreport.TestReportConveyorController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsService;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsSummaryService;

import javafx.application.Platform;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;

public class S02_qR_Code_Scanning_of_Pallet implements UnloadingBayState {

	private boolean restApiSendIndividualMeterStatus = false;
	// Define the URL for your Python unloading application's single meter update
	// API
	private static final String UNLOADING_SINGLE_METER_API_URL = "http://127.0.0.1:5002/api/unloading_meters/"; // Base
																												// URL
																												// for
																												// PUT

	private String flowPathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_UNLOADING_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	private ConveyorOutputMetricsService conveyorOutputMetricsService;
	private ConveyorOutputMetricsSummaryService conveyorOutputMetricsSummaryService;
	// private String myBayKey; // This should be initialized when the state machine
	// for a specific bay starts.

	public S02_qR_Code_Scanning_of_Pallet() {
		this.conveyorOutputMetricsService = MySqlServiceManager.getConveyorOutputMetricsService();
		this.conveyorOutputMetricsSummaryService = MySqlServiceManager.getConveyorOutputMetricsSummaryService();
		// myBayKey needs to be set dynamically per bay instance.
		// For demonstration, let's assume it's set via a setter or passed during
		// instantiation.
	}

	public String getMyBayKey() {
		return myBayKey;
	}
	/*
	 * public void setMyBayKey(String myBayKey) {
	 * this.myBayKey = myBayKey;
	 * }
	 */

	// ==============================================================================================================================================================
	@Override
	public BayResponse handleRequest() {
		Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// if (this.myBayKey == null || this.myBayKey.isEmpty()) {
		if (getMyBayKey() == null || getMyBayKey().isEmpty()) {
			Unloading.logger.error("S02_qR_Code_Scanning_of_Pallet: myBayKey is not set. Cannot process request.");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			return bayResponse;
		}

		long qrScanningWaitTimeInSec = DeviceDataManagerController.getConveyorConfigParsedKey()
				.getUnloadingQrScanningWaitTime_InSec();

		Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : isSta2PalletsExitInProgress()-X :"
				+ ConveyorDataManager.isSta2PalletsExitInProgress());
		Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : isSta1PalletsExitInProgress() :"
				+ ConveyorDataManager.isSta1PalletsExitInProgress());
		if ((ConveyorDataManager.isSta1PalletsExitInProgress())) {
			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA1 pallet realease observed ");
			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA1: qrScanningWaitTimeInSec: Entry :"
					+ qrScanningWaitTimeInSec);
			while ((qrScanningWaitTimeInSec > 0) &&
					(!Unloading.isStopProcessRequestedUnloadingBay())) {
				Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA1: qrScanningWaitTimeInSec: waiting :"
						+ qrScanningWaitTimeInSec);
				BayUtils.delay(1000);
				qrScanningWaitTimeInSec--;
				if (!ConveyorDataManager.isSta1PalletsExitInProgress()) {
					Unloading.logger.info(
							"S02_qR_Code_Scanning_of_Pallet : STA1: isSta1PalletsExitInProgress cleared: Exiting wait loop");
					break;
				}
			}
			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA1: qrScanningWaitTimeInSec: Exit :");
		}
		Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : isSta2PalletsExitInProgress()-Y :"
				+ ConveyorDataManager.isSta2PalletsExitInProgress());

		if ((ConveyorDataManager.isSta2PalletsExitInProgress())) {
			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA2: pallet realease observed ");
			qrScanningWaitTimeInSec = DeviceDataManagerController.getConveyorConfigParsedKey()
					.getUnloadingQrScanningWaitTime_InSec();

			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA2: qrScanningWaitTimeInSec: Entry :"
					+ qrScanningWaitTimeInSec);
			while ((qrScanningWaitTimeInSec > 0) &&
					(!Unloading.isStopProcessRequestedUnloadingBay())) {
				Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA2: qrScanningWaitTimeInSec: waiting :"
						+ qrScanningWaitTimeInSec);
				BayUtils.delay(1000);
				qrScanningWaitTimeInSec--;
				if (!ConveyorDataManager.isSta2PalletsExitInProgress()) {
					Unloading.logger.info(
							"S02_qR_Code_Scanning_of_Pallet : STA2: isSta2PalletsExitInProgress cleared: Exiting wait loop");
					break;
				}
			}
			Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : STA2: qrScanningWaitTimeInSec: Exit :");
		}

		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				Unloading.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		if ((bayResponse.getStatus()) ||
				(ConveyorDataManager.isUnloadingBayScreenPalletResultDisplayUserRequested())) {
			String palletQrId = "";
			String palletDistinctId = "";
			if (bayResponse.getStatus()) {
				palletQrId = bayResponse.getMyPalletQrCode();
				Unloading.logger.debug("S02_qR_Code_Scanning_of_Pallet: palletQrId: " + palletQrId);
				try {
					if (BayUtils.getUnloadingBayPalletDistinctIdList().contains(palletQrId)) {

						String palletQrIdFinal = palletQrId;
						long palletCount = BayUtils.getUnloadingBayPalletDistinctIdList().stream()
								.filter(e -> e.endsWith(palletQrIdFinal))
								.count();
						Unloading.logger.debug("S02_qR_Code_Scanning_of_Pallet: palletCount : " + palletCount
								+ " for palletQrId :" + palletQrId);
						if (palletCount > 1) {
							Unloading.logger.debug("S02_qR_Code_Scanning_of_Pallet: duplicate pallets found for "
									+ palletQrId + " . Kindly check BayUtils.getUnloadingBayPalletDistinctIdList():"
									+ BayUtils.getUnloadingBayPalletDistinctIdList());
						} else {
							palletDistinctId = BayUtils.getUnloadingBayPalletDistinctIdList().stream()
									.filter(e -> e.endsWith(palletQrIdFinal))
									.findFirst().get();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					Unloading.logger.debug("S02_qR_Code_Scanning_of_Pallet: Exception: " + e.getMessage());
				}

			}

			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// getMyBayKey(), qrData);

			if (palletDistinctId.isEmpty()) {
				Unloading.logger.info(
						"S02_qR_Code_Scanning_of_Pallet : not success in fetching from getUnloadingBayPalletDistinctIdList");
				boolean exitAppeared = false;
				Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
						.findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(
								getMyBayKey(), palletQrId, exitAppeared);
				if (myPalletManageOptional.isPresent()) {
					PalletManage myPalletManage = myPalletManageOptional.get();
					Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : found result  in database myPalletManage : "
							+ myPalletManage.getPalletDistinctId());

					palletDistinctId = myPalletManage.getPalletDistinctId();

				} else {
					Optional<PalletManage> myPalletManageOptional2 = MySqlServiceManager.getPalletManageService()
							.findLastByPalletQrIdAndPresentBayKey(
									palletQrId, getMyBayKey());
					if (myPalletManageOptional2.isPresent()) {
						PalletManage myPalletManage = myPalletManageOptional2.get();
						Unloading.logger
								.info("S02_qR_Code_Scanning_of_Pallet : found result  in database myPalletManage-2 : "
										+ myPalletManage.getPalletDistinctId());

						palletDistinctId = myPalletManage.getPalletDistinctId();
					} else {
						// fg
						Unloading.logger.info(
								"S02_qR_Code_Scanning_of_Pallet : result not found in database with today date for Pallet : "
										+ palletQrId);
					}
				}
			} else {

				BayUtils.getUnloadingBayPalletDistinctIdList().remove(palletDistinctId);
				Unloading.logger.info(
						"S02_qR_Code_Scanning_of_Pallet : removed palletDistinctId from getUnloadingBayPalletDistinctIdList :"
								+ BayUtils.getUnloadingBayPalletDistinctIdList());
			}
			if (palletDistinctId.isEmpty()) {

				if (ConveyorDataManager.isUnloadingBayScreenPalletResultDisplayUserRequested()) {
					palletDistinctId = new String(ConveyorDataManager
							.getUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId());
					/*
					 * ConveyorDataManager.setUnloadingBayScreenPalletResultDisplayUserRequested(
					 * false);
					 * ConveyorDataManager.
					 * setUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId("");
					 */

					Unloading.logger
							.info("S02_qR_Code_Scanning_of_Pallet :processing user request for palletDistinctId: "
									+ palletDistinctId);
				}

			}

			if (!palletDistinctId.isEmpty()) {
				BayUtils bayUtils = new BayUtils();
				bayUtils.computeMeterOverAllStatus(palletDistinctId);
				/*
				 * List<Map<String, Object>> metersData =
				 * readPalletMetersData(getMyBayKey(),palletDistinctId);
				 * 
				 * if (metersData != null && !metersData.isEmpty()) {
				 * batchUpdateUnloadingMeters(palletDistinctId,palletQrId,metersData); // This
				 * will now update ConveyorOutputMetrics daily with average hourly output
				 * 
				 * ReportUtilsV2 reportUtils = new ReportUtilsV2();
				 * reportUtils.processPalletMeterIndividualResult(palletDistinctId);
				 * } else {
				 * Unloading.logger.
				 * debug("S02_qR_Code_Scanning_of_Pallet: No meter data found for batch update for bay: "
				 * + getMyBayKey());
				 * }
				 */

				ArrayList<PalletMeter> palletMeterList = getPalletMeterList(getMyBayKey(), palletDistinctId);
				if (palletMeterList != null && !palletMeterList.isEmpty()) {

					updatePalletStatusOnDisplayMonitor(palletQrId, palletMeterList);
					metricsUpdateUnloadingMeters(palletDistinctId, palletQrId, palletMeterList); // This will now update
																									// ConveyorOutputMetrics
																									// daily with
																									// average hourly
																									// output
					updateUnloadingDashboard(palletMeterList);
					ReportUtils reportUtils = new ReportUtils();
					reportUtils.generateIndividualReportsForPalletMeters(palletDistinctId);
					Platform.runLater(() -> {
						ConveyorDataManager.getDashboardObject().refreshMetricsTable();
					});
				} else {
					Unloading.logger
							.debug("S02_qR_Code_Scanning_of_Pallet: No meter data found for batch update for bay: "
									+ getMyBayKey());
				}
			} else {
				// Add logic to clear existing results at bay display

				Unloading.logger
						.debug("S02_qR_Code_Scanning_of_Pallet: No Pallet details found for bay: " + getMyBayKey());

				sendIdleStatusUpdate();
			}

			if (ConveyorDataManager.isUnloadingBayScreenPalletResultDisplayUserRequested()) {
				ConveyorDataManager.setUnloadingBayScreenPalletResultDisplayUserRequested(false);
				ConveyorDataManager.setUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId("");

			}
		} else {
			Unloading.logger.debug("S02_qR_Code_Scanning_of_Pallet: Qr scanning failed : getResponseData: "
					+ bayResponse.getResponseData());
			sendIdleStatusUpdate();
		}

		/*
		 * List<Map<String, Object>> metersData =
		 * readPalletMetersData(getMyBayKey(),qrData);
		 * 
		 * if (metersData != null && !metersData.isEmpty()) {
		 * batchUpdateUnloadingMeters(qrData,metersData); // This will now update
		 * ConveyorOutputMetrics daily with average hourly output
		 * } else {
		 * Unloading.logger.
		 * debug("handleRequest: No meter data found for batch update for bay: " +
		 * getMyBayKey());
		 * }
		 */

		Unloading.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	// =============================================================================================================================================================

	private void updateUnloadingDashboard(ArrayList<PalletMeter> palletMeterList) {

		Integer positionNo = 0;
		String serialNo = "";
		String status = "";
		String reason = "";
		for (PalletMeter eachpalletMeter : palletMeterList) {
			try {
				positionNo = eachpalletMeter.getRackPositionNo();// (Integer)meterData.get("rackPositionNo");
				serialNo = eachpalletMeter.getMeterSerialNo();// (String) meterData.get("meterSerialNo");
				status = eachpalletMeter.getOverAllTestResultStatus();// (String)
																		// meterData.get("overallTestResultStatus");
				reason = eachpalletMeter.getErrorCode();// (String) meterData.get("errorCode");
				ApplicationLauncher.logger.debug(
						"updateConveyorMetrics: Unloading: positionNo: " + positionNo + " status :<" + status + ">");

				if (positionNo != null) {
					// sendunloadingMeterUpdate(positionNo, serialNo, status, reason);
					// BayUtils.delay(20000);
					// sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
					// MeterStatus meterStatus = status.equals("Pass") ? MeterStatus.PASSED :
					// MeterStatus.FAILED;
					MeterStatus meterStatus = status.equals(ConstantReport.REPORT_POPULATE_PASS) ? MeterStatus.PASSED
							: MeterStatus.FAILED;
					if (status.equals(ConstantReport.REPORT_POPULATE_PASS)) {
						meterStatus = MeterStatus.PASSED;
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo + " Pass hit1");
					} else if (status.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
						meterStatus = MeterStatus.FAILED;
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo + " Fail hit2");
					} else if (status.equals(ConstantReport.REPORT_POPULATE_WFR)) {
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo + " WFR hit3");
						meterStatus = MeterStatus.FAILED;
					} else {
						ApplicationLauncher.logger
								.debug("updateConveyorMetrics: Unloading: positionNo: " + positionNo + " others hit4");
						meterStatus = MeterStatus.FAILED;
					}
					// ConveyorDeviceDataManagerController.getDashboardObject().updatePalletMeterStatusByBayAndPosition(
					// getMyBayKey(), positionNo, meterStatus, reason);
					ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPositionWithSerialNo(
							getMyBayKey(), positionNo, serialNo, meterStatus,
							reason.replace(ConstantConveyor.REASON_RESULT_FAILED_DISPLAY, "").replace("\n", ""));// .replace(ConstantConveyor.REASON_RESULT_WFR_DISPLAY,
																													// ""));
				} else {
					ApplicationLauncher.logger
							.debug("batchUpdateMeters: Unloading: Meter ID is null, skipping update for: " + serialNo);
					// allUpdatesInitiatedSuccessfully = false;
				}
			} catch (Exception e) {
				ApplicationLauncher.logger
						.error("batchUpdateMeters: Unloading: Error initiating update for meter data: "
								+ eachpalletMeter.getMeterSerialNo() + ". Exception: " + e.getMessage(), e);
				// allUpdatesInitiatedSuccessfully = false;
			}
		}
	}

	public List<Map<String, Object>> readPalletMetersData(String selectedBayTypeKey, String palletDistinctId) {
		Unloading.logger.debug("readPalletMetersData: Entry for bay: " + selectedBayTypeKey);
		try {
			/*
			 * Unloading.logger.error("readPalletMetersData: getPresentPalletAtBayMap : " +
			 * PalletTrackerController.getPresentPalletAtBayMap());
			 * List<PalletManage> myPalletManage =
			 * MySqlServiceManager.getPalletManageService().
			 * findByPalletQrIdAndExitNotAppeared(palletQrCode);
			 * 
			 * if (myPalletManage.size()==0) {
			 * Unloading.logger.
			 * error("readPalletMetersData: PalletManage not found for pallet Qr code " +
			 * palletQrCode);
			 * return null;
			 * }
			 */

			PalletManage myPalletManage = null;// MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// selectedBayTypeKey, palletDistinctId);
			Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(palletDistinctId);
			if (myPalletManageOptional.isPresent()) {
				myPalletManage = myPalletManageOptional.get();
				Unloading.logger.debug("Unloading : readPalletMetersData: result found for " + selectedBayTypeKey
						+ " : " + palletDistinctId);
				Unloading.logger.debug("Unloading : readPalletMetersData: result found for " + palletDistinctId
						+ " : getPalletDistinctId: " + myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " + myPalletManage.g);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				/*
				 * TestReportConveyorController testReportConveyorController = new
				 * TestReportConveyorController();
				 * testReportConveyorController.exportPalletMeterResult(myPalletManage);
				 */
			} else {
				Unloading.logger.debug(
						"readPalletMetersData: result not found for " + selectedBayTypeKey + " : " + palletDistinctId);
				return null;
			}
			Unloading.logger
					.debug("readPalletMetersData: getPalletDistinctId: " + myPalletManage.getPalletDistinctId());

			Set<PalletMeter> palletMeters = myPalletManage.getPalletMeterList();
			List<Map<String, Object>> metersData = new ArrayList<>();

			for (PalletMeter meter : palletMeters) {
				Map<String, Object> meterMap = new HashMap<>();
				meterMap.put("id", meter.getId());
				meterMap.put("meterSerialNo", meter.getMeterSerialNo());
				meterMap.put("rackPositionNo", meter.getRackPositionNo());
				meterMap.put("meterProfileName", meter.getMeterProfileName());
				meterMap.put("overallTestResultStatus", meter.getOverAllTestResultStatus());
				meterMap.put("palletDistinctId", meter.getPalletDistinctId());
				meterMap.put("errorCode", meter.getErrorCode());
				metersData.add(meterMap);
			}
			Unloading.logger.debug("readPalletMetersData: Successfully read " + metersData.size() + " meters.");
			myPalletManage.setExitAppeared(true);
			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			Unloading.logger.debug("readPalletMetersData: updated setExitAppeared to true for getPalletDistinctId: "
					+ myPalletManage.getPalletDistinctId());
			Unloading.logger.debug("readPalletMetersData: metersData: " + metersData);

			return metersData;
		} catch (Exception e) {
			Unloading.logger.error("readPalletMetersData: Exception: " + e.getMessage(), e);
			return null;
		}
	}

	public ArrayList<PalletMeter> getPalletMeterList(String selectedBayTypeKey, String palletDistinctId) {
		Unloading.logger.debug("readPalletMetersData: Entry for bay: " + selectedBayTypeKey);
		try {
			/*
			 * Unloading.logger.error("readPalletMetersData: getPresentPalletAtBayMap : " +
			 * PalletTrackerController.getPresentPalletAtBayMap());
			 * List<PalletManage> myPalletManage =
			 * MySqlServiceManager.getPalletManageService().
			 * findByPalletQrIdAndExitNotAppeared(palletQrCode);
			 * 
			 * if (myPalletManage.size()==0) {
			 * Unloading.logger.
			 * error("readPalletMetersData: PalletManage not found for pallet Qr code " +
			 * palletQrCode);
			 * return null;
			 * }
			 */

			PalletManage myPalletManage = null;// MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// selectedBayTypeKey, palletDistinctId);
			Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(palletDistinctId);
			if (myPalletManageOptional.isPresent()) {
				myPalletManage = myPalletManageOptional.get();
				Unloading.logger.debug("Unloading : readPalletMetersData: result found for " + selectedBayTypeKey
						+ " : " + palletDistinctId);
				Unloading.logger.debug("Unloading : readPalletMetersData: result found for " + palletDistinctId
						+ " : getPalletDistinctId: " + myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " + myPalletManage.g);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				/*
				 * TestReportConveyorController testReportConveyorController = new
				 * TestReportConveyorController();
				 * testReportConveyorController.exportPalletMeterResult(myPalletManage);
				 */
			} else {
				Unloading.logger.debug(
						"readPalletMetersData: result not found for " + selectedBayTypeKey + " : " + palletDistinctId);
				return null;
			}
			Unloading.logger
					.debug("readPalletMetersData: getPalletDistinctId: " + myPalletManage.getPalletDistinctId());

			Set<PalletMeter> palletMeterSet = myPalletManage.getPalletMeterList();
			ArrayList<PalletMeter> palletMeterList = new ArrayList<PalletMeter>(palletMeterSet);
			/*
			 * List<Map<String, Object>> metersData = new ArrayList<>();
			 * 
			 * for (PalletMeter meter : palletMeters) {
			 * Map<String, Object> meterMap = new HashMap<>();
			 * meterMap.put("id", meter.getId());
			 * meterMap.put("meterSerialNo", meter.getMeterSerialNo());
			 * meterMap.put("rackPositionNo", meter.getRackPositionNo());
			 * meterMap.put("meterProfileName", meter.getMeterProfileName());
			 * meterMap.put("overallTestResultStatus", meter.getOverAllTestResultStatus());
			 * meterMap.put("palletDistinctId", meter.getPalletDistinctId());
			 * meterMap.put("errorCode", meter.getErrorCode());
			 * metersData.add(meterMap);
			 * }
			 */
			Unloading.logger.debug("readPalletMetersData: Successfully read " + palletMeterList.size() + " meters.");
			myPalletManage.setExitAppeared(true);
			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			Unloading.logger.debug("readPalletMetersData: updated setExitAppeared to true for getPalletDistinctId: "
					+ myPalletManage.getPalletDistinctId());
			// Unloading.logger.debug("readPalletMetersData: metersData: " + metersData);

			return palletMeterList;
		} catch (Exception e) {
			Unloading.logger.error("readPalletMetersData: Exception: " + e.getMessage(), e);
			return null;
		}
	}

	/*
	 * public static void sendunloadingMeterUpdate(int positionNo, String serialNo,
	 * String status, String reason) {
	 * new Thread(() -> {
	 * try {
	 * String rawJsonPayload = String.format(
	 * "{\"id\": %d, \"serialNo\": \"%s\", \"status\": \"%s\", \"reason\": \"%s\", \"timestamp\": \"%s\"}"
	 * ,
	 * positionNo,
	 * serialNo,
	 * status,
	 * reason,
	 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"
	 * ))
	 * );
	 * 
	 * String jsonPayloadForCurl = "\"" + rawJsonPayload.replace("\"", "\\\"") +
	 * "\"";
	 * 
	 * String[] command = {
	 * "curl",
	 * "-X", "PUT",
	 * "-H", "Content-Type: application/json",
	 * "-d", jsonPayloadForCurl,
	 * UNLOADING_SINGLE_METER_API_URL + positionNo
	 * };
	 * 
	 * ProcessBuilder pb = new ProcessBuilder(command);
	 * pb.inheritIO();
	 * 
	 * System.out.println("Sending unloading API Update: " +
	 * Arrays.toString(command));
	 * Process process = pb.start();
	 * int exitCode = process.waitFor();
	 * System.out.println("Curl command for unloading app exited with code: " +
	 * exitCode);
	 * 
	 * } catch (IOException | InterruptedException e) {
	 * System.err.println("Error sending unloading meter update: " +
	 * e.getMessage());
	 * e.printStackTrace();
	 * if (e instanceof InterruptedException) {
	 * Thread.currentThread().interrupt();
	 * }
	 * }
	 * }).start();
	 * }
	 */

	public void sendRestApiMeterStatusUpdate(int positionNo, String serialNo, String status, String reason) {
		Unloading.logger.debug("sendRestApiMeterStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Unloading.logger
						.debug("sendRestApiMeterStatusUpdate: status-1: <" + status + "> : positionNo: " + positionNo);
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_TAIL_END_UNLOADING_DISPLAY;// "rejection_meters";
				String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR, ConstantReport.REPORT_POPULATE_PASS)
						.toUpperCase();
				Unloading.logger
						.debug("sendRestApiMeterStatusUpdate: status-2: <" + status2 + "> : positionNo: " + positionNo);
				cluster4ClientManager.getRestConvClient().sendMeterStatusUpdate(clusterServer, targetDisplay,
						positionNo, serialNo, status2, reason);
				Unloading.logger.debug("sendRestApiMeterStatusUpdate: Exit");
			} catch (Exception e) {
				Unloading.logger.error("sendRestApiMeterStatusUpdate : Error sending Unloading meter update: "
						+ e.getMessage() + " : Position :" + positionNo);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	// public void sendPalletWithMetersStatusUpdate(ClusterServer
	// clusterServer,String targetDisplay,String palletQrCode, List<Map<String,
	// Object>> metersData) {
	/*
	 * public void sendRestApiMeterStatusUpdate(int positionNo, String serialNo,
	 * String status, String reason) {
	 * Unloading.logger.debug("sendRestApiMeterStatusUpdate: Entry");
	 * new Thread(() -> {
	 * try {
	 * Unloading.logger.debug("sendRestApiMeterStatusUpdate: status-1: <" + status +
	 * "> : positionNo: " + positionNo);
	 * //ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
	 * String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
	 * String clusterId =
	 * ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;//"4";
	 * BayUtils bayUtils = new BayUtils();
	 * ClusterServer clusterServer = bayUtils.getServerDetails(terminalId,
	 * clusterId);
	 * ConveyorClientManager cluster4ClientManager =
	 * ConveyorClientManager.getInstance(clusterId);
	 * String targetDisplay=
	 * ConstantConveyorConfig.REST_API_TAIL_END_UNLOADING_DISPLAY;//
	 * "rejection_meters";
	 * String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
	 * ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
	 * Unloading.logger.debug("sendRestApiMeterStatusUpdate: status-2: <" + status2
	 * + "> : positionNo: " + positionNo);
	 * cluster4ClientManager.getRestConvClient().sendMeterStatusUpdate(clusterServer
	 * ,targetDisplay,positionNo, serialNo, status2, reason);
	 * Unloading.logger.debug("sendRestApiMeterStatusUpdate: Exit");
	 * } catch (Exception e) {
	 * Unloading.logger.
	 * error("sendRestApiMeterStatusUpdate : Error sending Unloading meter update: "
	 * + e.getMessage() + " : Position :" +positionNo);
	 * e.printStackTrace();
	 * if (e instanceof InterruptedException) {
	 * Thread.currentThread().interrupt(); // Restore interrupt status
	 * }
	 * }
	 * }).start();
	 * }
	 */

	public void sendPalletWithMetersStatusUpdate(String palletQrCode, List<Map<String, Object>> metersData) {
		Unloading.logger.debug("sendPalletWithMetersStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Unloading.logger.debug("sendPalletWithMetersStatusUpdate: palletQrCode: <" + palletQrCode + ">");
				Unloading.logger.debug("sendPalletWithMetersStatusUpdate: metersData: <" + metersData + ">");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_UNLOADING_DISPLAY;// "rejection_meters";
				List<Map<String, Object>> resultManipulatedMetersData = new ArrayList<Map<String, Object>>();

				for (Map<String, Object> meter : metersData) {
					try {
						JSONObject meterJson = new JSONObject();
						meterJson.put("id", meter.get("rackPositionNo"));// meter.get("id"));
						meterJson.put("rackPositionNo", meter.get("rackPositionNo"));
						meterJson.put("meterSerialNo", meter.get("meterSerialNo"));
						String manipulatedStatus = (String) meter.get("overallTestResultStatus");

						manipulatedStatus = manipulatedStatus
								.replace(ConstantReport.REPORT_POPULATE_WFR, ConstantReport.REPORT_POPULATE_PASS)
								.toUpperCase();

						meterJson.put("overallTestResultStatus", manipulatedStatus);
						meterJson.put("errorCode", meter.getOrDefault("errorCode", ""));
						resultManipulatedMetersData.add(meterJson);
						Unloading.logger.debug(
								"sendPalletWithMetersStatusUpdate: serial number : " + meterJson.get("meterSerialNo")
										+ " , status: <" + meterJson.get("overallTestResultStatus") + "> : positionNo: "
										+ meterJson.get("rackPositionNo"));

					} catch (Exception e) {
						Unloading.logger.error("sendPalletWithMetersStatusUpdate : Exception in result manipulation: "
								+ e.getMessage() + " : palletQrCode :" + palletQrCode);
						e.printStackTrace();

					}
				}

				Unloading.logger.debug("sendPalletWithMetersStatusUpdate: resultManipulatedMetersData: "
						+ resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Unloading.logger.debug("sendPalletWithMetersStatusUpdate: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendPalletWithMetersStatusUpdate(clusterServer, targetDisplay,
						palletQrCode, resultManipulatedMetersData);
				Unloading.logger.debug("sendPalletWithMetersStatusUpdate: Exit");
			} catch (Exception e) {
				Unloading.logger.error("sendPalletWithMetersStatusUpdate : Error sending Unloading meter update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void updatePalletStatusOnDisplayMonitor(String palletQrCode, ArrayList<PalletMeter> palletMeterList) {
		Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: Entry");
		new Thread(() -> {
			try {
				Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: palletQrCode: <" + palletQrCode + ">");
				// Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: metersData: <" +
				// metersData + ">");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_UNLOADING_DISPLAY;// "rejection_meters";
				List<Map<String, Object>> resultManipulatedMetersData = new ArrayList<Map<String, Object>>();

				// for (Map<String, Object> meter : metersData) {
				for (PalletMeter eachPalletMeter : palletMeterList) {
					try {
						JSONObject meterJson = new JSONObject();
						meterJson.put("id", eachPalletMeter.getRackPositionNo());// meter.get("rackPositionNo"));//meter.get("id"));
						meterJson.put("rackPositionNo", eachPalletMeter.getRackPositionNo());// meter.get("rackPositionNo"));
						meterJson.put("meterSerialNo", eachPalletMeter.getMeterSerialNo());// meter.get("meterSerialNo"));
						String manipulatedStatus = eachPalletMeter.getOverAllTestResultStatus();// (String)meter.get("overallTestResultStatus");

						manipulatedStatus = manipulatedStatus
								.replace(ConstantReport.REPORT_POPULATE_WFR, ConstantReport.REPORT_POPULATE_PASS)
								.toUpperCase();

						meterJson.put("overallTestResultStatus", manipulatedStatus);
						meterJson.put("errorCode", eachPalletMeter.getErrorCode());// meter.getOrDefault("errorCode",
																					// ""));
						resultManipulatedMetersData.add(meterJson);
						Unloading.logger.debug(
								"updatePalletStatusOnDisplayMonitor: serial number : " + meterJson.get("meterSerialNo")
										+ " , status: <" + meterJson.get("overallTestResultStatus") + "> : positionNo: "
										+ meterJson.get("rackPositionNo"));

					} catch (Exception e) {
						Unloading.logger.error("updatePalletStatusOnDisplayMonitor : Exception in result manipulation: "
								+ e.getMessage() + " : palletQrCode :" + palletQrCode);
						e.printStackTrace();

					}
				}

				Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: resultManipulatedMetersData: "
						+ resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendPalletWithMetersStatusUpdate(clusterServer, targetDisplay,
						palletQrCode, resultManipulatedMetersData);
				Unloading.logger.debug("updatePalletStatusOnDisplayMonitor: Exit");
			} catch (Exception e) {
				Unloading.logger.error("updatePalletStatusOnDisplayMonitor : Error sending Unloading meter update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void sendRestApiPalletUpdate(String palletQrCode) {
		Unloading.logger.debug("sendRestApiPalletUpdate: Entry");
		new Thread(() -> {
			try {
				// Unloading.logger.debug("sendRestApiMeterStatusUpdate: status-1: <" + status +
				// "> : positionNo: " + positionNo);
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;// "4";
				Unloading.logger.debug(
						"sendRestApiPalletUpdate: clusterId: <" + clusterId + "> : palletQrCode: " + palletQrCode);
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_TAIL_END_UNLOADING_DISPLAY;// "rejection_meters";
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Unloading.logger.debug("sendRestApiMeterStatusUpdate: status-2: <" + status2
				// + "> : positionNo: " + positionNo);
				cluster4ClientManager.getRestConvClient().sendPalletNumberUpdate(clusterServer, targetDisplay,
						palletQrCode);

				Unloading.logger.debug("sendRestApiPalletUpdate: Exit");
			} catch (Exception e) {
				Unloading.logger.error("sendRestApiPalletUpdate : Error sending Unloading pallet update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void sendIdleStatusUpdate() {
		Unloading.logger.debug("Unloading: sendIdleStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Unloading.logger.debug("sendIdleStatusUpdate: Entry2");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_UNLOADING_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_UNLOADING_DISPLAY;// "rejection_meters";

				// Unloading.logger.debug("sendPalletWithMetersStatusUpdate:
				// resultManipulatedMetersData: " + resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Unloading.logger.debug("sendPalletWithMetersStatusUpdate: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendIdleStatusUpdate(clusterServer, targetDisplay);
				Unloading.logger.debug("sendIdleStatusUpdate: Exit");
			} catch (Exception e) {
				Unloading.logger
						.error("sendIdleStatusUpdate : Error sending Unloading meter update: " + e.getMessage());
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	/**
	 * Sends a batch update of meter statuses to the Python unloading app and
	 * updates daily ConveyorOutputMetrics.
	 * This method now maintains a single daily record for customer/bay,
	 * accumulating totals and calculating
	 * an average hourly output for the active duration of the day.
	 *
	 * @param metersToUpdate A list of maps, where each map contains "id",
	 *                       "meterSerialNo",
	 *                       "overallTestResultStatus", and "errorCode" for a meter.
	 * @return true if all updates were successfully initiated, false if any failed
	 *         to initiate.
	 */

	public boolean metricsUpdateUnloadingMeters(String palletDistinctId, String palletQrCode,
			ArrayList<PalletMeter> palletMeterList) {
		Unloading.logger.debug("metricsUpdateUnloadingMeters: Entry.");
		if (palletMeterList == null || palletMeterList.isEmpty()) {
			Unloading.logger.debug("metricsUpdateUnloadingMeters: No meters to update in batch.");
			return false;
		}

		if (palletQrCode.isEmpty()) {
			Unloading.logger.debug("metricsUpdateUnloadingMeters: palletQrCode found empty.");
			GuiUtils guiUtils = new GuiUtils();
			palletQrCode = guiUtils.getPalletNoFromPalletDistinctId(palletDistinctId);
			Unloading.logger.debug("metricsUpdateUnloadingMeters: updated palletQrCode: " + palletQrCode);
		}

		boolean allUpdatesInitiatedSuccessfully = true;
		// String customerName =
		// DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//."DevSys";
		// // Fixed customerName
		String bayType = getMyBayKey(); // Use the bayType from the current instance

		if (bayType == null || bayType.isEmpty()) {
			Unloading.logger.error(
					"metricsUpdateUnloadingMeters: bayType (myBayKey) is null or empty. Cannot update daily metrics.");
			return false;
		}

		/*
		 * for (Map<String, Object> meterData : metersToUpdate) {
		 * try {
		 * Integer positionNo = (Integer) meterData.get("rackPositionNo");
		 * String serialNo = (String) meterData.get("meterSerialNo");
		 * String status = (String) meterData.get("overallTestResultStatus");
		 * String reason = (String) meterData.get("errorCode");
		 * 
		 * if (positionNo != null) {
		 * //sendunloadingMeterUpdate(positionNo, serialNo, status, reason);
		 * if(restApiSendIndividualMeterStatus) {
		 * sendRestApiPalletUpdate(palletQrCode);
		 * BayUtils.delay(100);
		 * sendRestApiMeterStatusUpdate(positionNo, serialNo, status, reason);
		 * }
		 * } else {
		 * Unloading.logger.
		 * error("metricsUpdateUnloadingMeters: Meter ID is null, skipping update for: "
		 * + serialNo);
		 * allUpdatesInitiatedSuccessfully = false;
		 * }
		 * } catch (Exception e) {
		 * Unloading.logger.
		 * error("metricsUpdateUnloadingMeters: Error initiating update for meter data: "
		 * + meterData + ". Exception: " + e.getMessage(), e);
		 * allUpdatesInitiatedSuccessfully = false;
		 * }
		 * }
		 */

		/*
		 * if(!restApiSendIndividualMeterStatus) {
		 * //String palletQrCodeWithBayName = "Unloading Bay : " + palletQrCode;
		 * //sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
		 * sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
		 * }
		 */

		BayUtils bayUtils = new BayUtils();
		allUpdatesInitiatedSuccessfully = bayUtils.updateConveyorMetrics(
				bayType, palletDistinctId, palletQrCode, palletMeterList);

		Unloading.logger.debug("metricsUpdateUnloadingMeters: Exit. All updates initiated successfully: "
				+ allUpdatesInitiatedSuccessfully);
		return allUpdatesInitiatedSuccessfully;
	}

	// =============================================================================================================================================================

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