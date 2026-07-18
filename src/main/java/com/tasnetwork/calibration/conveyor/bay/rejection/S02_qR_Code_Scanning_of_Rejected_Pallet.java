package com.tasnetwork.calibration.conveyor.bay.rejection;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date; // Import Date for timestamp fields
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsService;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsSummaryService;

import javafx.application.Platform;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics; // Import the ConveyorOutputMetrics model
import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;

public class S02_qR_Code_Scanning_of_Rejected_Pallet implements RejectionBayState {

	private boolean restApiSendIndividualMeterStatus = false;
	// Define the URL for your Python rejection application's single meter update
	// API
	private static final String REJECTION_SINGLE_METER_API_URL = "http://127.0.0.1:5001/api/rejection_meters/"; // Base
																												// URL
																												// for
																												// PUT

	private String flowPathId = "p1";
	private String bayStateSequenceId = ConstantBayStateManage.BAY_HP_SEQ_05;
	private String failStateErrorCode = ConvErrorCodeMapping.ERROR_CODE_FT_006;
	private int palletQrScannerPositionId = ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID;
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	// Inject the ConveyorOutputMetricsService
	/*
	 * private ConveyorOutputMetricsService conveyorOutputMetricsService;
	 * private ConveyorOutputMetricsSummaryService
	 * conveyorOutputMetricsSummaryService;
	 */

	// This field represents the current bay's identifier. It must be set for each
	// bay instance.
	// private String myBayKey;

	// Constructor - assuming MySqlServiceManager provides access to your services
	public S02_qR_Code_Scanning_of_Rejected_Pallet() {
		// this.conveyorOutputMetricsService =
		// MySqlServiceManager.getConveyorOutputMetricsService();
		// this.conveyorOutputMetricsSummaryService =
		// MySqlServiceManager.getConveyorOutputMetricsSummaryService();
	}

	public String getMyBayKey() {
		return myBayKey;
	}

	// Setter for myBayKey, often used if this class is instantiated and then
	// configured
	/*
	 * public void setMyBayKey(String myBayKey) {
	 * this.myBayKey = myBayKey;
	 * }
	 */

	// ==============================================================================================================================================================
	@Override
	public BayResponse handleRequest() {
		Rejection.logger.info("S02_qR_Code_Scanning_of_Pallet : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		// Ensure myBayKey is set before proceeding. This is critical.
		// if (this.myBayKey == null || this.myBayKey.isEmpty()) {
		if (getMyBayKey() == null || getMyBayKey().isEmpty()) {
			Rejection.logger.error("S02_qR_Code_Scanning_of_Pallet: myBayKey is not set. Cannot process request.");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			return bayResponse;
		}

		QrCodeScanningPallet bayPalletService = new QrCodeScanningPallet(
				Rejection.logger,
				getMyBayKey(),
				getBayStateSequenceId(),
				getPalletQrScannerPositionId(),
				getFailStateErrorCode(),
				StateExecutorController.simulateFtBayHappyPath);
		bayResponse = bayPalletService.qrCodePalletScanningProcess();

		// if(bayResponse.getStatus()) {
		if ((bayResponse.getStatus()) ||
				(ConveyorDataManager.isRejectionBayScreenPalletResultDisplayUserRequested())) {
			String palletQrId = "";
			String palletDistinctId = "";
			if (bayResponse.getStatus()) {
				palletQrId = bayResponse.getMyPalletQrCode();
				Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: palletQrId :" + palletQrId);
				try {
					if (BayUtils.getRejectionBayPalletDistinctIdList().contains(palletQrId)) {

						String palletQrIdFinal = palletQrId;
						long palletCount = BayUtils.getRejectionBayPalletDistinctIdList().stream()
								.filter(e -> e.endsWith(palletQrIdFinal))
								.count();
						Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: palletCount : " + palletCount
								+ " for palletQrId :" + palletQrId);
						if (palletCount > 1) {
							Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: duplicate pallets found for "
									+ palletQrId + " . Kindly check BayUtils.getRejectionBayPalletDistinctIdList():"
									+ BayUtils.getRejectionBayPalletDistinctIdList());
						} else {
							palletDistinctId = BayUtils.getRejectionBayPalletDistinctIdList().stream()
									.filter(e -> e.endsWith(palletQrIdFinal))
									.findFirst().get();
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: Exception: " + e.getMessage());
				}

			}

			// Read meter data for the current pallet

			if (palletDistinctId.isEmpty()) {
				Rejection.logger.info(
						"S02_qR_Code_Scanning_of_Pallet : not success in fetching from getRejectionBayPalletDistinctIdList");
				boolean exitAppeared = false;
				Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
						.findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(
								getMyBayKey(), palletQrId, exitAppeared);
				if (myPalletManageOptional.isPresent()) {
					PalletManage myPalletManage = myPalletManageOptional.get();
					Rejection.logger.info("S02_qR_Code_Scanning_of_Pallet : found result  in database myPalletManage : "
							+ myPalletManage.getPalletDistinctId());

					palletDistinctId = myPalletManage.getPalletDistinctId();

				} else {
					Rejection.logger.info(
							"S02_qR_Code_Scanning_of_Pallet : result not found with today date in database for Pallet : "
									+ palletQrId);
				}

			} else {

				BayUtils.getRejectionBayPalletDistinctIdList().remove(palletDistinctId);
				Rejection.logger.info(
						"S02_qR_Code_Scanning_of_Pallet : removed palletDistinctId from getRejectionBayPalletDistinctIdList :"
								+ BayUtils.getRejectionBayPalletDistinctIdList());
			}

			if (palletDistinctId.isEmpty()) {

				if (ConveyorDataManager.isRejectionBayScreenPalletResultDisplayUserRequested()) {
					palletDistinctId = new String(ConveyorDataManager
							.getRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId());
					/*
					 * ConveyorDataManager.setRejectionBayScreenPalletResultDisplayUserRequested(
					 * false);
					 * ConveyorDataManager.
					 * setRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId("");
					 */

					Rejection.logger
							.info("S02_qR_Code_Scanning_of_Pallet :processing user request for palletDistinctId: "
									+ palletDistinctId);
				}

			}

			if (!palletDistinctId.isEmpty()) {
				BayUtils bayUtils = new BayUtils();
				// bayUtils.computeMeterOverAllStatus(palletDistinctId);
				/*
				 * List<Map<String, Object>> metersData =
				 * readPalletMetersData(getMyBayKey(),palletDistinctId);
				 * 
				 * if (metersData != null && !metersData.isEmpty()) {
				 * batchUpdateRejectionMeters(palletDistinctId,palletQrId,metersData); // This
				 * will now update ConveyorOutputMetrics daily with average hourly output
				 * } else {
				 * Rejection.logger.
				 * debug("S02_qR_Code_Scanning_of_Pallet: No meter data found for batch update for bay: "
				 * + getMyBayKey());
				 * }
				 */

				ArrayList<PalletMeter> palletMeterList = getPalletMeterList(getMyBayKey(), palletDistinctId);
				if (palletMeterList != null && !palletMeterList.isEmpty()) {
					updatePalletStatusOnDisplayMonitor(palletQrId, palletMeterList);
					metricsUpdateRejectionMeters(palletDistinctId, palletQrId, palletMeterList); // This will now update
																									// ConveyorOutputMetrics
																									// daily with
																									// average hourly
																									// output
					updateRejectionDashboard(palletMeterList);
					Platform.runLater(() -> {
						ConveyorDataManager.getDashboardObject().refreshMetricsTable();
					});
				} else {
					Rejection.logger
							.debug("S02_qR_Code_Scanning_of_Pallet: No meter data found for batch update for bay: "
									+ getMyBayKey());
				}

			} else {
				// Add logic to clear existing results at bay display
				Rejection.logger
						.debug("S02_qR_Code_Scanning_of_Pallet: No Pallet details found for bay: " + getMyBayKey());

				sendIdleStatusUpdate();
			}
			if (ConveyorDataManager.isRejectionBayScreenPalletResultDisplayUserRequested()) {
				ConveyorDataManager.setRejectionBayScreenPalletResultDisplayUserRequested(false);
				ConveyorDataManager.setRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId("");

			}
		} else {
			Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: Qr scanning failed : getResponseData: "
					+ bayResponse.getResponseData());
			sendIdleStatusUpdate();
		}

		/*
		 * List<Map<String, Object>> metersData =
		 * readPalletMetersData(getMyBayKey(),qrData);
		 * Rejection.logger.debug("S02_qR_Code_Scanning_of_Pallet: metersData: " +
		 * metersData);
		 * // If data is available, send it for batch update to the rejection app
		 * if (metersData != null && !metersData.isEmpty()) {
		 * batchUpdateRejectionMeters(qrData,metersData); // This will now update
		 * ConveyorOutputMetrics daily with average hourly output
		 * } else {
		 * Rejection.logger.
		 * debug("handleRequest: No meter data found for batch update for bay: " +
		 * getMyBayKey());
		 * }
		 */

		Rejection.logger.info("S02_qR_Code_Scanning_of_Pallet : Exit");
		return bayResponse;
	}

	private void updateRejectionDashboard(ArrayList<PalletMeter> palletMeterList) {

		Integer positionNo = 0;
		String serialNo = "";
		String status = "";
		String reason = "";
		for (PalletMeter eachpalletMeter : palletMeterList) {
			try {
				positionNo = eachpalletMeter.getRackPositionNo();// (Integer) meterData.get("rackPositionNo");
				serialNo = eachpalletMeter.getMeterSerialNo();// (String) meterData.get("meterSerialNo");
				status = eachpalletMeter.getOverAllTestResultStatus();// (String)
																		// meterData.get("overallTestResultStatus");
				reason = eachpalletMeter.getErrorCode();// (String) meterData.get("errorCode");
				ApplicationLauncher.logger.debug("updateRejectionDashBoard: Rejection: positionNo: " + positionNo
						+ " ,serialNo: " + serialNo + " , status: <" + status + "> , reason: " + reason);

				if (positionNo != null) {

					MeterStatus meterStatus = status.equals(ConstantReport.REPORT_POPULATE_PASS) ? MeterStatus.PASSED
							: MeterStatus.FAILED;
					if (status.equals(ConstantReport.REPORT_POPULATE_PASS)) {
						meterStatus = MeterStatus.PASSED;
						ApplicationLauncher.logger
								.debug("updateRejectionDashBoard: Rejection: positionNo: " + positionNo + " Pass hit1");
					} else if (status.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
						meterStatus = MeterStatus.FAILED;
						ApplicationLauncher.logger
								.debug("updateRejectionDashBoard: Rejection: positionNo: " + positionNo + " Fail hit2");
					} else if (status.equals(ConstantReport.REPORT_POPULATE_WFR)) {
						ApplicationLauncher.logger
								.debug("updateRejectionDashBoard: Rejection: positionNo: " + positionNo + " WFR hit3");
						meterStatus = MeterStatus.IDLE;
					} else {
						ApplicationLauncher.logger.debug(
								"updateRejectionDashBoard: Rejection: positionNo: " + positionNo + " others hit4");
						meterStatus = MeterStatus.IDLE;
					}
					ConveyorDataManager.getDashboardObject().updatePalletMeterStatusByBayAndPositionWithSerialNo(
							getMyBayKey(), positionNo, serialNo, meterStatus, reason);
				} else {
					ApplicationLauncher.logger.error(
							"updateRejectionDashBoard: Rejection: Meter ID is null, skipping update for: " + serialNo);
					// allUpdatesInitiatedSuccessfully = false;
				}
			} catch (Exception e) {
				ApplicationLauncher.logger
						.error("updateRejectionDashBoard: Rejection: Error initiating update for meter data: "
								+ eachpalletMeter.getMeterSerialNo() + ". Exception: " + e.getMessage(), e);
				// allUpdatesInitiatedSuccessfully = false;
			}
		}
	}

	// =============================================================================================================================================================

	/**
	 * Reads meter data from a specified pallet and prepares it for sending.
	 *
	 * @param selectedBayTypeKey The key identifying the bay type (e.g., "BAY_A") to
	 *                           retrieve the pallet.
	 * @return A list of maps, where each map represents a meter's data, or null if
	 *         the pallet is not found or not active.
	 */
	public List<Map<String, Object>> readPalletMetersData(String selectedBayTypeKey, String palletDistinctId) {
		Rejection.logger.debug("readPalletMetersData: Entry for bay: " + selectedBayTypeKey);
		try {
			// Rejection.logger.error("readPalletMetersData: getPresentPalletAtBayMap : " +
			// PalletTrackerController.getPresentPalletAtBayMap());
			// String myPalletDistinctId =
			// PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);

			PalletManage myPalletManage = null;// MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// selectedBayTypeKey, palletQrId);

			boolean exitAppeared = false;
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// getMyBayKey(), qrData);
			/*
			 * Optional<PalletManage> myPalletManageOptional =
			 * MySqlServiceManager.getPalletManageService().
			 * findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(
			 * getMyBayKey(), palletQrId,exitAppeared);
			 */
			/*
			 * if (myPalletManage == null) {
			 * Rejection.logger.
			 * error("readPalletMetersData: PalletManage not found for distinct ID: " +
			 * myPalletDistinctId);
			 * return null;
			 * }
			 */

			Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(palletDistinctId);

			if (myPalletManageOptional.isPresent()) {
				myPalletManage = myPalletManageOptional.get();
				Rejection.logger.debug("Rejection : readPalletMetersData: result found for " + selectedBayTypeKey
						+ " : palletDistinctId: " + palletDistinctId);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " + myPalletManage.g);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());

			} else {
				Rejection.logger.debug(
						"readPalletMetersData: result not found for " + selectedBayTypeKey + " : " + palletDistinctId);
				return null;
			}
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
			Rejection.logger.debug("readPalletMetersData: Successfully read " + metersData.size() + " meters.");
			myPalletManage.setExitAppeared(true);
			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			Rejection.logger
					.debug("Rejection: readPalletMetersData: updated setExitAppeared to true for getPalletDistinctId: "
							+ myPalletManage.getPalletDistinctId());

			return metersData;
		} catch (Exception e) {
			Rejection.logger.error("readPalletMetersData: Exception: " + e.getMessage(), e);
			return null;
		}
	}

	public ArrayList<PalletMeter> getPalletMeterList(String selectedBayTypeKey, String palletDistinctId) {
		Rejection.logger.debug("readPalletMetersData: Entry for bay: " + selectedBayTypeKey);
		try {
			// Rejection.logger.error("readPalletMetersData: getPresentPalletAtBayMap : " +
			// PalletTrackerController.getPresentPalletAtBayMap());
			// String myPalletDistinctId =
			// PalletTrackerController.getPresentPalletAtBayMap().get(selectedBayTypeKey);

			PalletManage myPalletManage = null;// MySqlServiceManager.getPalletManageService().findFirstByPalletDistinctId(myPalletDistinctId);
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// selectedBayTypeKey, palletQrId);

			boolean exitAppeared = false;
			// Optional<PalletManage> myPalletManageOptional =
			// MySqlServiceManager.getPalletManageService().findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(
			// getMyBayKey(), qrData);
			/*
			 * Optional<PalletManage> myPalletManageOptional =
			 * MySqlServiceManager.getPalletManageService().
			 * findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(
			 * getMyBayKey(), palletQrId,exitAppeared);
			 */
			/*
			 * if (myPalletManage == null) {
			 * Rejection.logger.
			 * error("readPalletMetersData: PalletManage not found for distinct ID: " +
			 * myPalletDistinctId);
			 * return null;
			 * }
			 */

			Optional<PalletManage> myPalletManageOptional = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(palletDistinctId);

			if (myPalletManageOptional.isPresent()) {
				myPalletManage = myPalletManageOptional.get();
				Rejection.logger.debug("Rejection : readPalletMetersData: result found for " + selectedBayTypeKey
						+ " : palletDistinctId: " + palletDistinctId);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " + myPalletManage.g);
				// Rejection.logger.debug("Rejection : readPalletMetersData: result found for "
				// + palletQrId + " : getPalletDistinctId: " +
				// myPalletManage.getPalletDistinctId());

			} else {
				Rejection.logger.debug(
						"readPalletMetersData: result not found for " + selectedBayTypeKey + " : " + palletDistinctId);
				return null;
			}
			Set<PalletMeter> palletMeterSet = myPalletManage.getPalletMeterList();
			ArrayList<PalletMeter> palletMeterList = new ArrayList<PalletMeter>(palletMeterSet);
			// List<Map<String, Object>> metersData = new ArrayList<>();

			/*
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
			Rejection.logger.debug("readPalletMetersData: Successfully read " + palletMeterList.size() + " meters.");
			myPalletManage.setExitAppeared(true);
			MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			Rejection.logger
					.debug("Rejection: readPalletMetersData: updated setExitAppeared to true for getPalletDistinctId: "
							+ myPalletManage.getPalletDistinctId());

			return palletMeterList;
		} catch (Exception e) {
			Rejection.logger.error("readPalletMetersData: Exception: " + e.getMessage(), e);
			return null;
		}
	}

	public void sendIdleStatusUpdate() {
		Rejection.logger.debug("Rejected: sendIdleStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Rejection.logger.debug("sendIdleStatusUpdate: Entry2");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_IDLE_DISPLAY_TAIL_END_REJECTION_DISPLAY;// "rejection_meters";

				// Rejection.logger.debug("sendPalletWithMetersStatusUpdate:
				// resultManipulatedMetersData: " + resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Rejection.logger.debug("sendPalletWithMetersStatusUpdate: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendIdleStatusUpdate(clusterServer, targetDisplay);
				Rejection.logger.debug("sendIdleStatusUpdate: Exit");
			} catch (Exception e) {
				Rejection.logger
						.error("sendIdleStatusUpdate : Error sending Rejection meter update: " + e.getMessage());
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	/**
	 * Sends an update to the rejection Python application's API using a curl
	 * command.
	 * This method will construct a curl command to update a specific meter's status
	 * and reason on the rejection dashboard. This is typically for single meter
	 * updates.
	 *
	 * @param positionNo The position number (meter ID) to update.
	 * @param serialNo   The serial number of the meter.
	 * @param status     The status of the meter (e.g., "PASS", "FAIL").
	 * @param reason     The reason for the status (e.g., error code).
	 */
	/*
	 * public static void sendRejectionMeterUpdate(int positionNo, String serialNo,
	 * String status, String reason) {
	 * new Thread(() -> {
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : Entry");
	 * try {
	 * // Construct the raw JSON payload
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
	 * // Escape internal double quotes and wrap in outer double quotes for curl's
	 * -d argument
	 * String jsonPayloadForCurl = "\"" + rawJsonPayload.replace("\"", "\\\"") +
	 * "\"";
	 * Rejection.logger.
	 * debug("Rejection: sendRejectionMeterUpdate : jsonPayloadForCurl: " +
	 * jsonPayloadForCurl);
	 * // Construct the curl command
	 * // Assuming curl.exe is in system PATH. If not, provide full path:
	 * "C:\\Windows\\System32\\curl.exe"
	 * String[] command = {
	 * "curl",
	 * "-X", "PUT",
	 * "-H", "Content-Type: application/json",
	 * "-d", jsonPayloadForCurl, // Use the properly quoted and escaped string
	 * REJECTION_SINGLE_METER_API_URL + positionNo // Target specific meter ID
	 * };
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : command: " +
	 * command.toString());
	 * ProcessBuilder pb = new ProcessBuilder(command);
	 * pb.inheritIO(); // Inherit I/O to see curl output in Java console
	 * 
	 * Rejection.logger.debug("Sending Rejection API Update: " +
	 * Arrays.toString(command));
	 * Process process = pb.start();
	 * int exitCode = process.waitFor(); // Wait for curl command to complete
	 * Rejection.logger.debug("Curl command for rejection app exited with code: " +
	 * exitCode);
	 * 
	 * } catch (IOException | InterruptedException e) {
	 * Rejection.logger.error("Error sending rejection meter update: " +
	 * e.getMessage());
	 * e.printStackTrace();
	 * if (e instanceof InterruptedException) {
	 * Thread.currentThread().interrupt(); // Restore interrupt status
	 * }
	 * }
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : Exit");
	 * }).start();
	 * }
	 */

	/*
	 * public static void sendRejectionMeterUpdate(int positionNo, String serialNo,
	 * String status, String reason) {
	 * new Thread(() -> {
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : Entry");
	 * try {
	 * // Construct the raw JSON payload
	 * String escapedSerialNo = serialNo.replace("\"", "\\\"");
	 * String escapedReason = reason.replace("\"", "\\\"");
	 * 
	 * String rawJsonPayload = String.format(
	 * "{\"id\": %d, \"serialNo\": \"%s\", \"status\": \"%s\", \"reason\": \"%s\", \"timestamp\": \"%s\"}"
	 * ,
	 * positionNo,
	 * escapedSerialNo,
	 * status.replace("WFR", "PASS").toUpperCase(),
	 * escapedReason,
	 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"
	 * ))
	 * );
	 * 
	 * // --- CRITICAL CHANGE HERE ---
	 * // For ProcessBuilder, the -d argument should be the raw JSON string itself
	 * String jsonPayloadForProcessBuilder = rawJsonPayload;
	 * Rejection.logger.
	 * debug("Rejection: sendRejectionMeterUpdate : jsonPayloadForProcessBuilder: "
	 * + jsonPayloadForProcessBuilder);
	 * 
	 * // Construct the curl command
	 * String[] command = {
	 * "curl",
	 * "-X", "PUT",
	 * "-H", "Content-Type: application/json",
	 * "-d", jsonPayloadForProcessBuilder, // Use the raw JSON string
	 * REJECTION_SINGLE_METER_API_URL + positionNo // Target specific meter ID
	 * };
	 * 
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : command: " +
	 * Arrays.toString(command));
	 * ProcessBuilder pb = new ProcessBuilder(command);
	 * pb.inheritIO(); // Inherit I/O to see curl output in Java console
	 * 
	 * Rejection.logger.debug("Sending Rejection API Update: " +
	 * Arrays.toString(command));
	 * Process process = pb.start();
	 * int exitCode = process.waitFor(); // Wait for curl command to complete
	 * Rejection.logger.debug("Curl command for rejection app exited with code: " +
	 * exitCode);
	 * 
	 * } catch (IOException | InterruptedException e) {
	 * Rejection.logger.error("Error sending rejection meter update: " +
	 * e.getMessage());
	 * e.printStackTrace();
	 * if (e instanceof InterruptedException) {
	 * Thread.currentThread().interrupt(); // Restore interrupt status
	 * }
	 * }
	 * Rejection.logger.debug("Rejection: sendRejectionMeterUpdate : Exit");
	 * }).start();
	 * }
	 */

	public void sendRestApiPalletUpdate(String palletQrCode) {
		Rejection.logger.debug("sendRestApiPalletUpdate: Entry");
		new Thread(() -> {
			try {
				// Rejection.logger.debug("sendRestApiMeterStatusUpdate: status-1: <" + status +
				// "> : positionNo: " + positionNo);
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID;// "4";
				Rejection.logger.debug(
						"sendRestApiPalletUpdate: clusterId: <" + clusterId + "> : palletQrCode: " + palletQrCode);
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_TAIL_END_REJECTION_DISPLAY;// "rejection_meters";
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Rejection.logger.debug("sendRestApiMeterStatusUpdate: status-2: <" + status2
				// + "> : positionNo: " + positionNo);
				cluster4ClientManager.getRestConvClient().sendPalletNumberUpdate(clusterServer, targetDisplay,
						palletQrCode);

				Rejection.logger.debug("sendRestApiPalletUpdate: Exit");
			} catch (Exception e) {
				Rejection.logger.error("sendRestApiPalletUpdate : Error sending rejection pallet update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void sendPalletWithMetersStatusUpdate(String palletQrCode, List<Map<String, Object>> metersData) {
		Rejection.logger.debug("sendPalletWithMetersStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Rejection.logger.debug("sendPalletWithMetersStatusUpdate: palletQrCode: <" + palletQrCode + ">");
				Rejection.logger.debug("sendPalletWithMetersStatusUpdate: metersData: <" + metersData + ">");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_REJECTION_DISPLAY;// "rejection_meters";
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
						Rejection.logger.debug(
								"sendPalletWithMetersStatusUpdate: serial number : " + meterJson.get("meterSerialNo")
										+ " , status: <" + meterJson.get("overallTestResultStatus") + "> : positionNo: "
										+ meterJson.get("rackPositionNo"));

					} catch (Exception e) {
						Rejection.logger.error("sendPalletWithMetersStatusUpdate : Exception in result manipulation: "
								+ e.getMessage() + " : palletQrCode :" + palletQrCode);
						e.printStackTrace();

					}
				}

				Rejection.logger.debug("sendPalletWithMetersStatusUpdate: resultManipulatedMetersData: "
						+ resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Rejection.logger.debug("sendPalletWithMetersStatusUpdate: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendPalletWithMetersStatusUpdate(clusterServer, targetDisplay,
						palletQrCode, resultManipulatedMetersData);
				Rejection.logger.debug("sendPalletWithMetersStatusUpdate: Exit");
			} catch (Exception e) {
				Rejection.logger.error("sendPalletWithMetersStatusUpdate : Error sending Rejection meter update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void updatePalletStatusOnDisplayMonitor(String palletQrCode, ArrayList<PalletMeter> palletMeterList) {
		Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: Entry");
		new Thread(() -> {
			try {
				Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: palletQrCode: <" + palletQrCode + ">");
				// Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: metersData: <" +
				// metersData + ">");
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID;// "4";
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_BATCH_UPDATE_TAIL_END_REJECTION_DISPLAY;// "rejection_meters";
				List<Map<String, Object>> resultManipulatedMetersData = new ArrayList<Map<String, Object>>();

				/* for (Map<String, Object> meter : metersData) { */
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
						Rejection.logger.debug(
								"updatePalletStatusOnDisplayMonitor: serial number : " + meterJson.get("meterSerialNo")
										+ " , status: <" + meterJson.get("overallTestResultStatus") + "> : positionNo: "
										+ meterJson.get("rackPositionNo"));

					} catch (Exception e) {
						Rejection.logger.error("updatePalletStatusOnDisplayMonitor : Exception in result manipulation: "
								+ e.getMessage() + " : palletQrCode :" + palletQrCode);
						e.printStackTrace();

					}
				}

				Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: resultManipulatedMetersData: "
						+ resultManipulatedMetersData);
				// String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR,
				// ConstantReport.REPORT_POPULATE_PASS).toUpperCase();
				// Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: status-2: <" +
				// status2 + "> : positionNo: " + positionNo);

				cluster4ClientManager.getRestConvClient().sendPalletWithMetersStatusUpdate(clusterServer, targetDisplay,
						palletQrCode, resultManipulatedMetersData);
				Rejection.logger.debug("updatePalletStatusOnDisplayMonitor: Exit");
			} catch (Exception e) {
				Rejection.logger.error("updatePalletStatusOnDisplayMonitor : Error sending Rejection meter update: "
						+ e.getMessage() + " : palletQrCode :" + palletQrCode);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	public void sendRestApiMeterStatusUpdate(int positionNo, String serialNo, String status, String reason) {
		Rejection.logger.debug("sendRestApiMeterStatusUpdate: Entry");
		new Thread(() -> {
			try {
				Rejection.logger
						.debug("sendRestApiMeterStatusUpdate: status-1: <" + status + "> : positionNo: " + positionNo);
				// ConveyorClientManager cluster1ClientManager = new ConveyorClientManager();
				String terminalId = ConstantConveyorConfig.MY_TERMINAL_ID;
				String clusterId = ConstantConveyorConfig.CONVEYOR_REJECTION_CLUSTER_ID;// "4";
				Rejection.logger.debug(
						"sendRestApiMeterStatusUpdate: clusterId: <" + clusterId + "> : positionNo: " + positionNo);
				BayUtils bayUtils = new BayUtils();
				ClusterServer clusterServer = bayUtils.getServerDetails(terminalId, clusterId);
				ConveyorClientManager cluster4ClientManager = ConveyorClientManager.getInstance(clusterId);
				String targetDisplay = ConstantConveyorConfig.REST_API_TAIL_END_REJECTION_DISPLAY;// "rejection_meters";
				String status2 = status.replace(ConstantReport.REPORT_POPULATE_WFR, ConstantReport.REPORT_POPULATE_PASS)
						.toUpperCase();
				Rejection.logger
						.debug("sendRestApiMeterStatusUpdate: status-2: <" + status2 + "> : positionNo: " + positionNo);
				cluster4ClientManager.getRestConvClient().sendMeterStatusUpdate(clusterServer, targetDisplay,
						positionNo, serialNo, status2, reason);
				Rejection.logger.debug("sendRestApiMeterStatusUpdate: Exit");
			} catch (Exception e) {
				Rejection.logger.error("sendRestApiMeterStatusUpdate : Error sending rejection meter update: "
						+ e.getMessage() + " : Position :" + positionNo);
				e.printStackTrace();
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt(); // Restore interrupt status
				}
			}
		}).start();
	}

	/**
	 * Sends a batch update of meter statuses and reasons to the Python rejection
	 * application's API.
	 * This method iterates through a list of meter data and sends an individual
	 * curl command
	 * for each meter update.
	 * It also updates the ConveyorOutputMetrics table with daily aggregated metrics
	 * and a running
	 * average hourly output for the specified customer and bay type.
	 *
	 * @param metersToUpdate A list of maps, where each map contains "id",
	 *                       "meterSerialNo",
	 *                       "overallTestResultStatus", and "errorCode" for a meter.
	 * @return true if all updates were successfully initiated, false if any failed
	 *         to initiate.
	 */

	public boolean batchUpdateRejectionMeters(String palletDistinctId, String palletQrCode,
			List<Map<String, Object>> metersToUpdate) {
		Rejection.logger.debug("batchUpdateRejectionMeters: Entry.");
		if (metersToUpdate == null || metersToUpdate.isEmpty()) {
			Rejection.logger.debug("batchUpdateRejectionMeters: No meters to update in batch.");
			return false;
		}

		if (palletQrCode.isEmpty()) {
			Rejection.logger.debug("batchUpdateRejectionMeters: palletQrCode found empty.");
			GuiUtils guiUtils = new GuiUtils();
			palletQrCode = guiUtils.getPalletNoFromPalletDistinctId(palletDistinctId);
			Rejection.logger.debug("batchUpdateRejectionMeters: updated palletQrCode: " + palletQrCode);
		}

		boolean allUpdatesInitiatedSuccessfully = true;
		// String customerName =
		// DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//."DevSys";
		// // Fixed customerName
		String bayType = getMyBayKey(); // Use the bayType from the current instance

		if (bayType == null || bayType.isEmpty()) {
			Rejection.logger.error(
					"batchUpdateRejectionMeters: bayType (myBayKey) is null or empty. Cannot update daily metrics.");
			return false;
		}

		for (Map<String, Object> meterData : metersToUpdate) {
			try {
				Integer positionNo = (Integer) meterData.get("rackPositionNo");
				String serialNo = (String) meterData.get("meterSerialNo");
				String status = (String) meterData.get("overallTestResultStatus");
				String reason = (String) meterData.get("errorCode");

				if (positionNo != null) {
					// sendunloadingMeterUpdate(positionNo, serialNo, status, reason);
					if (restApiSendIndividualMeterStatus) {
						sendRestApiPalletUpdate(palletQrCode);
						BayUtils.delay(100);
						sendRestApiMeterStatusUpdate(positionNo, serialNo, status, reason);
					}
				} else {
					Rejection.logger
							.error("batchUpdateRejectionMeters: Meter ID is null, skipping update for: " + serialNo);
					allUpdatesInitiatedSuccessfully = false;
				}
			} catch (Exception e) {
				Rejection.logger.error("batchUpdateRejectionMeters: Error initiating update for meter data: "
						+ meterData + ". Exception: " + e.getMessage(), e);
				allUpdatesInitiatedSuccessfully = false;
			}
		}

		if (!restApiSendIndividualMeterStatus) {
			// String palletQrCodeWithBayName = "Unloading Bay : " + palletQrCode;
			// sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
			sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
		}

		/*
		 * BayUtils bayUtils = new BayUtils();
		 * allUpdatesInitiatedSuccessfully = bayUtils.updateConveyorMetrics(
		 * bayType, palletDistinctId, palletQrCode, metersToUpdate);
		 */
		Rejection.logger.debug("batchUpdateRejectionMeters: Exit. All updates initiated successfully: "
				+ allUpdatesInitiatedSuccessfully);
		return allUpdatesInitiatedSuccessfully;
	}

	public boolean metricsUpdateRejectionMeters(String palletDistinctId, String palletQrCode,
			ArrayList<PalletMeter> palletMeterList) {
		Rejection.logger.debug("metricsUpdateRejectionMeters: Entry.");
		if (palletMeterList == null || palletMeterList.isEmpty()) {
			Rejection.logger.debug("metricsUpdateRejectionMeters: No meters to update in batch.");
			return false;
		}

		if (palletQrCode.isEmpty()) {
			Rejection.logger.debug("metricsUpdateRejectionMeters: palletQrCode found empty.");
			GuiUtils guiUtils = new GuiUtils();
			palletQrCode = guiUtils.getPalletNoFromPalletDistinctId(palletDistinctId);
			Rejection.logger.debug("metricsUpdateRejectionMeters: updated palletQrCode: " + palletQrCode);
		}

		boolean allUpdatesInitiatedSuccessfully = true;
		// String customerName =
		// DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//."DevSys";
		// // Fixed customerName
		String bayType = getMyBayKey(); // Use the bayType from the current instance

		if (bayType == null || bayType.isEmpty()) {
			Rejection.logger.error(
					"metricsUpdateRejectionMeters: bayType (myBayKey) is null or empty. Cannot update daily metrics.");
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
		 * Rejection.logger.
		 * error("metricsUpdateRejectionMeters: Meter ID is null, skipping update for: "
		 * + serialNo);
		 * allUpdatesInitiatedSuccessfully = false;
		 * }
		 * } catch (Exception e) {
		 * Rejection.logger.
		 * error("metricsUpdateRejectionMeters: Error initiating update for meter data: "
		 * + meterData + ". Exception: " + e.getMessage(), e);
		 * allUpdatesInitiatedSuccessfully = false;
		 * }
		 * }
		 */
		/*
		 * if(!restApiSendIndividualMeterStatus) {
		 * //String palletQrCodeWithBayName = "Unloading Bay : " + palletQrCode;
		 * //sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
		 * sendPalletWithMetersStatusUpdateV2(palletQrCode, palletMeterList);
		 * }
		 */

		BayUtils bayUtils = new BayUtils();
		allUpdatesInitiatedSuccessfully = bayUtils.updateConveyorMetrics(
				bayType, palletDistinctId, palletQrCode, palletMeterList);

		Rejection.logger.debug("metricsUpdateRejectionMeters: Exit. All updates initiated successfully: "
				+ allUpdatesInitiatedSuccessfully);
		return allUpdatesInitiatedSuccessfully;
	}

	/*
	 * public boolean batchUpdateRejectionMeters(String
	 * palletQrCode,List<Map<String, Object>> metersToUpdate) {
	 * Rejection.logger.debug("batchUpdateRejectionMeters: Entry.");
	 * if (metersToUpdate == null || metersToUpdate.isEmpty()) {
	 * Rejection.logger.
	 * debug("batchUpdateRejectionMeters: No meters to update in batch.");
	 * return false;
	 * }
	 * 
	 * boolean allUpdatesInitiatedSuccessfully = true;
	 * String customerName =
	 * DeviceDataManagerController.getConveyorConfigParsedKey().getCustomerName();//
	 * "DevSys"; // Fixed customerName
	 * String bayType = getMyBayKey(); // Use the bayType from the current instance
	 * 
	 * // Critical: Ensure bayType is available before proceeding with metrics
	 * update
	 * if (bayType == null || bayType.isEmpty()) {
	 * Rejection.logger.
	 * error("batchUpdateRejectionMeters: bayType (myBayKey) is null or empty. Cannot update daily metrics."
	 * );
	 * return false;
	 * }
	 * ConveyorOutputMetrics metrics = new ConveyorOutputMetrics();
	 * metrics.setCustomerName(customerName);
	 * metrics.setBayType(bayType); // Set the bayType for the new entry
	 * // Initialize counts to 0 for the new daily entry
	 * metrics.setPalletOutput(0);
	 * //metrics.setPalletOutput(0);
	 * metrics.setTotalNoOfMeters(0);
	 * metrics.setPassedMeters(0);
	 * metrics.setFailedMeters(0);
	 * metrics.setAverageHourlyOutput(0.0);
	 * long diffInMillis = 0;
	 * // Find or create the daily metrics entry for the specific customer and
	 * bayType
	 * Optional<ConveyorOutputMetrics> metricsOptional =
	 * conveyorOutputMetricsService.findByCustomerNameBayTypeAndCurrentDate(
	 * customerName, bayType);
	 * .orElseGet(() -> {
	 * ConveyorOutputMetrics newMetrics = new ConveyorOutputMetrics();
	 * newMetrics.setCustomerName(customerName);
	 * newMetrics.setBayType(bayType); // Set the bayType for the new entry
	 * // Initialize counts to 0 for the new daily entry
	 * newMetrics.setPalletOutput(0);
	 * newMetrics.setTotalNoOfMeters(0);
	 * newMetrics.setPassedMeters(0);
	 * newMetrics.setFailedMeters(0);
	 * newMetrics.setAverageHourlyOutput(0.0); // Initialize new field
	 * // createdAt for the daily record will be set to the start of the day by the
	 * service
	 * return newMetrics;
	 * });
	 * 
	 * if(metricsOptional.isPresent()){
	 * Rejection.logger.debug("batchUpdateRejectionMeters: bayType " + bayType +
	 * " metrics exist");
	 * metrics= metricsOptional.get();
	 * }else{
	 * Rejection.logger.debug("batchUpdateRejectionMeters: bayType " + bayType +
	 * " metrics NOT exist");
	 * }
	 * //ConveyorOutputMetrics metrics= metricsOptional.get();
	 * int currentPalletTotalMeters = metersToUpdate.size();
	 * int currentPalletPassedMeters = 0;
	 * int currentPalletFailedMeters = 0;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: metersToUpdate.size(): "
	 * +metersToUpdate.size());
	 * for (Map<String, Object> meterData : metersToUpdate) {
	 * try {
	 * Integer positionNo = (Integer) meterData.get("rackPositionNo");
	 * String serialNo = (String) meterData.get("meterSerialNo");
	 * String status = (String) meterData.get("overallTestResultStatus");
	 * String reason = (String) meterData.get("errorCode");
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo: " +
	 * positionNo + " ,serialNo: " + serialNo + " , status: <" + status +
	 * "> , reason: " +reason);
	 * if (ConstantReport.REPORT_POPULATE_PASS.equalsIgnoreCase(status)) {
	 * currentPalletPassedMeters++;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo : " +
	 * positionNo + ": Pass hit1");
	 * } else if (ConstantReport.REPORT_POPULATE_FAIL.equalsIgnoreCase(status)) {
	 * currentPalletFailedMeters++;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo : " +
	 * positionNo + ": Fail hit2");
	 * }else if (ConstantReport.REPORT_POPULATE_WFR.equalsIgnoreCase(status)) {
	 * currentPalletPassedMeters++;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo : " +
	 * positionNo + ": WFR hit3");
	 * }else{
	 * currentPalletPassedMeters++;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo : " +
	 * positionNo + ": Others hit4");
	 * }
	 * 
	 * if (positionNo != null) {
	 * //sendRejectionMeterUpdate(positionNo, serialNo, status, reason);
	 * if(restApiSendIndividualMeterStatus) {
	 * sendRestApiPalletUpdate(palletQrCode);
	 * BayUtils.delay(100);
	 * sendRestApiMeterStatusUpdate(positionNo, serialNo, status, reason);
	 * }
	 * MeterStatus meterStatus = status.equals(ConstantReport.REPORT_POPULATE_PASS)
	 * ? MeterStatus.PASSED : MeterStatus.FAILED;
	 * if(status.equals(ConstantReport.REPORT_POPULATE_PASS)) {
	 * meterStatus = MeterStatus.PASSED;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo: "+ positionNo
	 * +" Pass hit1");
	 * }else if(status.equals(ConstantReport.REPORT_POPULATE_FAIL)) {
	 * meterStatus = MeterStatus.FAILED;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo: "+ positionNo
	 * +" Fail hit2");
	 * }else if(status.equals(ConstantReport.REPORT_POPULATE_WFR)) {
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo: "+ positionNo
	 * +" WFR hit3");
	 * meterStatus = MeterStatus.IDLE;
	 * }else {
	 * Rejection.logger.debug("batchUpdateRejectionMeters: positionNo: "+ positionNo
	 * +" others hit4");
	 * meterStatus = MeterStatus.IDLE;
	 * }
	 * ConveyorDeviceDataManagerController.getDashboardObject().
	 * updatePalletMeterStatusByBayAndPositionWithSerialNo(
	 * getMyBayKey(), positionNo, serialNo, meterStatus, reason);
	 * } else {
	 * Rejection.logger.
	 * error("batchUpdateRejectionMeters: Meter ID is null, skipping update for: " +
	 * serialNo);
	 * allUpdatesInitiatedSuccessfully = false;
	 * }
	 * } catch (Exception e) {
	 * Rejection.logger.
	 * error("batchUpdateRejectionMeters: Error initiating update for meter data: "
	 * + meterData + ". Exception: " + e.getMessage(), e);
	 * allUpdatesInitiatedSuccessfully = false;
	 * }
	 * }
	 * if(!restApiSendIndividualMeterStatus) {
	 * //String palletQrCodeWithBayName = "Rejection Bay : " + palletQrCode;
	 * //sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
	 * sendPalletWithMetersStatusUpdate(palletQrCode, metersToUpdate);
	 * }
	 * // After processing all meters for the current pallet, update the daily
	 * ConveyorOutputMetrics
	 * metrics.setPalletOutput(metrics.getPalletOutput() + 1); // Increment pallet
	 * count for the day
	 * metrics.setTotalNoOfMeters(metrics.getTotalNoOfMeters() +
	 * currentPalletTotalMeters);
	 * metrics.setPassedMeters(metrics.getPassedMeters() +
	 * currentPalletPassedMeters);
	 * metrics.setFailedMeters(metrics.getFailedMeters() +
	 * currentPalletFailedMeters);
	 * // updatedAt will be set by the service automatically before saving
	 * 
	 * // Calculate Average Hourly Output
	 * // The createdAt field in 'metrics' will be the start of the day (00:00:00)
	 * // The current time 'now' represents the end of the active period for average
	 * calculation
	 * Date now = new Date();
	 * if(metricsOptional.isPresent()){
	 * diffInMillis = now.getTime() - metrics.getCreatedAt().getTime(); // Time
	 * elapsed since start of day
	 * Rejection.logger.debug("batchUpdateRejectionMeters: diffInMillis: " +
	 * diffInMillis);
	 * }
	 * double hoursElapsed = (double) diffInMillis / (1000 * 60 * 60); // Convert
	 * milliseconds to hours
	 * Rejection.logger.debug("batchUpdateRejectionMeters: hoursElapsed: " +
	 * hoursElapsed);
	 * // To avoid division by zero or inflated "per hour" numbers for very short
	 * durations
	 * // If the elapsed time is less than an hour, we'll consider it 1 hour for
	 * averaging purposes.
	 * // This gives a more realistic "rate" from the beginning of operation.
	 * if (hoursElapsed < 1.0) {
	 * hoursElapsed = 1.0;
	 * }
	 * 
	 * double calculatedAverage = metrics.getTotalNoOfMeters() / hoursElapsed;
	 * Rejection.logger.debug("batchUpdateRejectionMeters: calculatedAverage: " +
	 * calculatedAverage);
	 * metrics.setAverageHourlyOutput(calculatedAverage);
	 * 
	 * 
	 * try {
	 * conveyorOutputMetricsService.saveToDb(metrics);
	 * Rejection.logger.
	 * info("ConveyorOutputMetrics (Daily) updated successfully for customer: " +
	 * customerName + ", bay: " + bayType + " for today. Avg Hourly Output: " +
	 * String.format("%.2f", calculatedAverage));
	 * Platform.runLater(()->{
	 * ConveyorDeviceDataManagerController.getDashboardObject().refreshMetricsTable(
	 * );
	 * });
	 * 
	 * } catch (Exception e) {
	 * Rejection.logger.error("Error saving ConveyorOutputMetrics (Daily): " +
	 * e.getMessage(), e);
	 * allUpdatesInitiatedSuccessfully = false;
	 * }
	 * 
	 * // SUMMARY - UPDATE
	 * try {
	 * // Try to find existing summary record for today
	 * Optional<ConveyorOutputMetricsSummary> summaryOptional =
	 * conveyorOutputMetricsSummaryService.findByCustomerNameBayTypeAndCurrentDate(
	 * customerName, bayType);
	 * 
	 * Date metricsCreatedAt = metrics.getCreatedAt();
	 * 
	 * ConveyorOutputMetricsSummary summary = summaryOptional.orElseGet(() -> {
	 * ConveyorOutputMetricsSummary s = new ConveyorOutputMetricsSummary();
	 * s.setCustomerName(customerName);
	 * s.setBayType(bayType);
	 * s.setCreatedAt(metricsCreatedAt);
	 * s.setPalletOutput(0);
	 * s.setTotalNoOfMeters(0);
	 * s.setPassedMeters(0);
	 * s.setFailedMeters(0);
	 * s.setAverageHourlyOutput(0.0);
	 * return s;
	 * });
	 * 
	 * hoursElapsed = (double) diffInMillis / (1000 * 60 * 60); // Convert
	 * milliseconds to hours
	 * Rejection.logger.
	 * debug("batchUpdateUnloadingMeters : summary : hoursElapsed: " +
	 * hoursElapsed);
	 * // To avoid division by zero or inflated "per hour" numbers for very short
	 * durations
	 * // If the elapsed time is less than an hour, we'll consider it 1 hour for
	 * averaging purposes.
	 * // This gives a more realistic "rate" from the beginning of operation.
	 * if (hoursElapsed < 1.0) {
	 * hoursElapsed = 1.0;
	 * }
	 * 
	 * double calculatedSummaryAverage = ( summary.getTotalNoOfMeters() +
	 * currentPalletTotalMeters ) / hoursElapsed;
	 * Rejection.logger.
	 * debug("batchUpdateUnloadingMeters : summary : hoursElapsed: " +
	 * hoursElapsed);
	 * Rejection.logger.
	 * debug("batchUpdateUnloadingMeters : summary : TotalNoOfMeters: " +
	 * summary.getTotalNoOfMeters());
	 * Rejection.logger.
	 * debug("batchUpdateUnloadingMeters : summary : calculatedSummaryAverage: " +
	 * calculatedSummaryAverage);
	 * 
	 * summaryOptional.ifPresent(existing -> summary.setId(existing.getId())); //
	 * Keep ID if exists
	 * summary.setUpdatedAt(now);
	 * summary.setPalletOutput(summary.getPalletOutput() + 1);
	 * summary.setTotalNoOfMeters(summary.getTotalNoOfMeters() +
	 * currentPalletTotalMeters);
	 * summary.setPassedMeters(summary.getPassedMeters() +
	 * currentPalletPassedMeters);
	 * summary.setFailedMeters(summary.getFailedMeters() +
	 * currentPalletFailedMeters);
	 * summary.setAverageHourlyOutput(calculatedSummaryAverage);
	 * 
	 * conveyorOutputMetricsSummaryService.saveToDb(summary);
	 * 
	 * Rejection.logger.info("ConveyorOutputMetricsSummary updated for customer: " +
	 * customerName + ", bay: " + bayType);
	 * } catch (Exception e) {
	 * Rejection.logger.error("Error saving ConveyorOutputMetricsSummary: " +
	 * e.getMessage(), e);
	 * }
	 * // --------------------- END SUMMARY LOGIC ---------------------
	 * 
	 * 
	 * BayUtils bayUtils = new BayUtils();
	 * bayUtils.updateSummaryMetrics(conveyorOutputMetricsSummaryService, metrics,
	 * customerName, bayType, currentPalletTotalMeters, currentPalletPassedMeters,
	 * currentPalletFailedMeters);
	 * 
	 * Rejection.logger.
	 * debug("batchUpdateRejectionMeters: Exit. All updates initiated successfully: "
	 * + allUpdatesInitiatedSuccessfully);
	 * return allUpdatesInitiatedSuccessfully;
	 * }
	 */

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