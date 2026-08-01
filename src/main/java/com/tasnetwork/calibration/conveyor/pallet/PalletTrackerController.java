package com.tasnetwork.calibration.conveyor.pallet;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.rejection.S02_qR_Code_Scanning_of_Rejected_Pallet;
import com.tasnetwork.calibration.conveyor.bay.unloading.S02_qR_Code_Scanning_of_Pallet;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantLdu;
import com.tasnetwork.calibration.conveyor.dashboard.MeterStatus;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.util.ChannelQueueRequestProcessor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.MeterResultSummary;
import com.tasnetwork.spring.orm.model.PalletBayState;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterResults;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PalletTrackerController implements Initializable {

	// Timer validateTimer;

	String resultStyleWfr = "-fx-text-fill: blue;-fx-alignment: CENTER;";
	String resultStylePass = "-fx-text-fill: green;-fx-alignment: CENTER";
	String resultStyleFail = "-fx-text-fill: red;-fx-alignment: CENTER;";
	String resultStyleUndefined = "-fx-text-fill: black;-fx-alignment: CENTER;";
	String resultStyleDefault = "-fx-text-fill: black;-fx-alignment: CENTER;";

	ContextMenu contextMenu = new ContextMenu();
	MenuItem reComputeResults = new MenuItem("Reprocess result");
	MenuItem displayResultAtUnloadingBay = new MenuItem("Display Results-Unloading");
	MenuItem displayResultAtRejectionBay = new MenuItem("Display Results-Rejection");
	// MenuItem deleteItem = new MenuItem("Delete");

	// private static Map<String, String> activePalletMap = new
	// LinkedHashMap<String,String>();// key = palletQrCode, value =
	// palletDistinctId
	// private static Map<String, String> presentPalletAtBayMap = new
	// LinkedHashMap<String,String>();//key = bayKey, value = palletDistinctId

	private static Map<String, String> activePalletMap = new ConcurrentHashMap<String, String>();// key = palletQrCode,
																									// value =
																									// palletDistinctId
	private static Map<String, String> presentPalletAtBayMap = new ConcurrentHashMap<String, String>();// key = bayKey,
																										// value =
																										// palletDistinctId

	@FXML
	private ComboBox<String> cmbBxSelectBayType;
	private static ComboBox<String> ref_cmbBxSelectBayType;

	@FXML
	private ListView<String> lvMeterList;
	// @FXML private ListView<String> lvDbMeterList;
	@FXML
	private TextField txtPalletQrId;
	@FXML
	private TextField txtMeterSerialNo;
	@FXML
	private TextField txtAddResultPositionNo;
	@FXML
	private TextField txtAddResultValue;
	@FXML
	private TextField txtAddResultStatus;
	@FXML
	private TextField txtAddResultTestCaseName;

	private static ListView<String> ref_lvMeterList;
	private static TextField ref_txtMeterSerialNo;
	private static TextField ref_txtAddResultPositionNo;
	private static TextField ref_txtAddResultValue;
	private static TextField ref_txtAddResultStatus;
	private static TextField ref_txtAddResultTestCaseName;

	// private static ListView<String> ref_lvDbMeterList;

	String deviceTypeKeyPrefix = ConstantLdu.LDU_TYPE_PREFIX;
	String deviceModelName = ConstantLdu.LDU_LSCS_MODEL;
	String deviceType = ConstantConveyor.DEVICE_TYPE_LDU;
	String deviceDefaultBaudRate = String.valueOf(ConstantLdu.LDU_DEFAULT_BAUD_RATE);

	@FXML
	private TableColumn<PalletManage, String> colPmBayType;

	@FXML
	private TableColumn<PalletManage, Integer> colPmSerialNo;

	@FXML
	private TableColumn<PalletManage, Integer> colPmPalletBatchNo;

	@FXML
	private TableColumn colPmPalletActive;

	@FXML
	private TableColumn colPmExitAppeared;

	@FXML
	private TableColumn<PalletManage, String> colPmPalletQrId;

	@FXML
	private TableColumn<PalletManage, String> colPmPalletBatchMapId;

	@FXML
	private TableColumn<PalletManage, String> colPmConvEntryTime;

	@FXML
	private TableColumn<PalletManage, String> colPmConvExitTime;

	@FXML
	private TableColumn<PalletManage, String> colPmTestExecutionStatus;

	@FXML
	private TableColumn<PalletManage, String> colPmTestResultStatus;

	@FXML
	private TableColumn<PalletManage, String> colPmTransitionError;

	@FXML
	private TableColumn<PalletManage, String> colPmConvPalletRunTime;

	@FXML
	private TableColumn<PalletManage, Integer> colPmNoOfMetersPresent;

	@FXML
	private TableColumn<PalletManage, Integer> colPmNoOfMetersPassed;

	@FXML
	private TableColumn<PalletManage, Integer> colPmNoOfMetersFailed;

	@FXML
	private TableColumn<PalletManage, Integer> colPmNoOfMetersRejected;

	@FXML
	private TableView<PalletManage> tvPalletManage;

	private static TableView<PalletManage> ref_tvPalletManage;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayEntryTime;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayExecutionStatus;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayExitTime;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayResultStatus;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayRunTime;

	@FXML
	private TableColumn<PalletBayState, String> colPbsBayType;

	@FXML
	private TableColumn<PalletBayState, String> colPbsPalletBatchMapId;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsPalletBatchNo;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsSerialNo;

	@FXML
	private TableColumn<PalletBayState, String> colPbsPalletQrId;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsNoOfMetersPresent;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsNoOfMetersPassed;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsNoOfMetersFailed;

	@FXML
	private TableColumn<PalletBayState, Integer> colPbsNoOfMetersRejected;

	@FXML
	private TableView<PalletBayState> tvPalletBayState;

	private static TableView<PalletBayState> ref_tvPalletBayState;

	@FXML
	private ComboBox<String> cmbBxAddResultSelectBayTestType;
	private static ComboBox<String> ref_cmbBxAddResultSelectBayTestType;

	@FXML
	private TableColumn<PalletMeterResults, Integer> colPmrSerialNo;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrBayType;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrPalletQrId;

	@FXML
	private TableColumn<PalletMeterResults, Integer> colPmrPalletBatchNo;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrMeterSerialNo;

	@FXML
	private TableColumn<PalletMeterResults, Integer> colPmrPositionNo;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrTestType;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrTestCaseName;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrResultValue;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrResultStatus;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrExpectedRangeValue;

	@FXML
	private TableColumn<PalletMeterResults, String> colPmrPalletBatchMapId;
	@FXML
	private TableColumn<PalletMeterResults, String> colPmrTestCaseEntryTime;
	@FXML
	private TableColumn<PalletMeterResults, String> colPmrTestCaseExitTime;
	@FXML
	private TableColumn<PalletMeterResults, String> colPmrTestCaseRunTime;

	@FXML
	private TableView<PalletMeterResults> tvPalletMeterResult;

	private static TableView<PalletMeterResults> ref_tvPalletMeterResult;

	@FXML
	private TableColumn<PalletMeter, String> colPmtrMeterSerialNo;

	@FXML
	private TableColumn<PalletMeter, Integer> colPmtrPositionNo;

	@FXML
	private TableColumn<PalletMeter, String> colPmtrResultStatus;

	@FXML
	private TableColumn<PalletMeter, String> colPmtrErrorCode;

	@FXML
	private TableColumn<PalletMeter, Integer> colPmtrSerialNo;

	@FXML
	private TableColumn<PalletMeter, String> colPmtrPalletDistinctId;

	@FXML
	private TableView<PalletMeter> tvPalletMeter;

	private static TableView<PalletMeter> ref_tvPalletMeter;

	@FXML
	private ComboBox<String> cmbBxMsumDistinctPalletId;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumPalletDistinctId;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumMeterSerialNo;

	@FXML
	private TableColumn<MeterResultSummary, Integer> colMsumPalletBatchNo;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumPalletQrId;

	@FXML
	private TableColumn<MeterResultSummary, Integer> colMsumSerialNo;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestVerific1;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeCalib;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeComm;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeFt;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeHv;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeIr;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeNoLoad;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumTestTypeSta;

	@FXML
	private TableColumn<MeterResultSummary, String> colMsumOverAllStatus;

	@FXML
	private TableView<MeterResultSummary> tvMeterResultSummary;
	private static TableView<MeterResultSummary> ref_tvMeterResultSummary;

	private static int currentPage = 0;
	private static int pageSize = 100;

	@FXML
	private Button btnPrevPage;
	@FXML
	private Button btnNextPage;
	@FXML
	private javafx.scene.control.Label lblPageInfo;

	private static Button ref_btnPrevPage;
	private static Button ref_btnNextPage;
	private static javafx.scene.control.Label ref_lblPageInfo;

	private static AtomicInteger palletManageSerialNoAtomic = new AtomicInteger(1);
	private AtomicInteger palletBayStateSerialNoAtomic = new AtomicInteger(1);

	private AtomicInteger palletMeterResultsSerialNoAtomic = new AtomicInteger(1);

	private static AtomicInteger palletMeterSerialNoAtomic = new AtomicInteger(1);

	private static AtomicInteger meterResultSummarySerialNoAtomic = new AtomicInteger(1);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		refAssignment();
		loadDataConfig();
		guiInit();
		// refreshPalletManageDataFromDb();
		refreshPalletManageDataFromDbv2("PalletTracker-initialize");
	}

	public void refAssignment() {

		ref_tvPalletManage = tvPalletManage;

		ref_cmbBxSelectBayType = cmbBxSelectBayType;

		ref_lvMeterList = lvMeterList;
		ref_txtMeterSerialNo = txtMeterSerialNo;
		// ref_lvDbMeterList = lvDbMeterList;

		ref_tvPalletBayState = tvPalletBayState;

		ref_tvPalletMeterResult = tvPalletMeterResult;

		ref_txtAddResultPositionNo = txtAddResultPositionNo;

		ref_txtAddResultValue = txtAddResultValue;
		ref_txtAddResultStatus = txtAddResultStatus;
		ref_txtAddResultTestCaseName = txtAddResultTestCaseName;
		ref_cmbBxAddResultSelectBayTestType = cmbBxAddResultSelectBayTestType;

		ref_tvPalletMeter = tvPalletMeter;

		ref_tvMeterResultSummary = tvMeterResultSummary;
		ref_btnPrevPage = btnPrevPage;
		ref_btnNextPage = btnNextPage;
		ref_lblPageInfo = lblPageInfo;

	}

	public void loadDataConfig() {

		ApplicationLauncher.logger.debug("loadDataConfig : Entry");
		// setClusterBayPositionNoCnameMap(BayUtils.getDutClusterBayPositionNoCnameMap());
		// setClusterBayNamePositionListMap(BayUtils.getDutClusterBayNamePositionListMap());
		List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService().findByPalletActive();
		PalletManage palletBayTracker = null;

		for (int i = 0; i < myPalletManageList.size(); i++) {
			palletBayTracker = myPalletManageList.get(i);
			getPresentPalletAtBayMap().put(palletBayTracker.getPresentBayKey(), palletBayTracker.getPalletDistinctId());
		}
		ApplicationLauncher.logger.debug("loadDataConfig : getPresentPalletAtBayMap() : " + getPresentPalletAtBayMap());

		List<PalletManage> rejectedPalletManageList = getYesterdayAndTodayPallets();// MySqlServiceManager.getPalletManageService().findByPalletActive();
		for (int i = 0; i < rejectedPalletManageList.size(); i++) {
			palletBayTracker = rejectedPalletManageList.get(i);
			ApplicationLauncher.logger
					.debug("loadDataConfig : getPalletBatchNo() : " + palletBayTracker.getPalletBatchNo());
			getPresentPalletAtBayMap().put(ConstantConveyor.REJECTION_BAY_KEY, palletBayTracker.getPalletDistinctId());
		}

		ApplicationLauncher.logger
				.debug("loadDataConfig : with rejectedPalletManageList() : " + getPresentPalletAtBayMap());

	}

	public List<PalletManage> getYesterdayAndTodayPallets() {
		ZoneId zone = ZoneId.systemDefault();

		// Start of yesterday (00:00)
		long startOfYesterday = LocalDate.now().minusDays(1)
				.atStartOfDay(zone).toEpochSecond();

		// End of today (23:59:59)
		long endOfToday = LocalDate.now()
				.atTime(LocalTime.MAX).atZone(zone).toEpochSecond();

		return MySqlServiceManager.getPalletManageService().findByBayKeyAndEpochRange(
				ConstantConveyor.REJECTION_BAY_KEY, (int) startOfYesterday, (int) endOfToday);
	}

	@FXML
	public void refreshDataOnClick() {

		ApplicationLauncher.logger.debug("refreshDataOnClick : Entry");

		// refreshPalletManageDataFromDb();

		refreshPalletManageDataFromDbv2("PalletTracker-refreshDataOnClick");
		ApplicationLauncher.logger.debug("loadDataConfig : getPresentPalletAtBayMap() : " + getPresentPalletAtBayMap());
		ApplicationLauncher.logger.debug("refreshDataOnClick : Exit");
	}

	@FXML
	public void onPrevPageClick(ActionEvent event) {
		if (currentPage > 0) {
			currentPage--;
			refreshPalletManageDataFromDbv2("Pagination-Prev");
		}
	}

	@FXML
	public void onNextPageClick(ActionEvent event) {
		currentPage++;
		refreshPalletManageDataFromDbv2("Pagination-Next");
	}

	public void refreshPalletManageDataFromDbv2(String invokedBy) {

		Callable<Boolean> taskRefreshPalletManageDataFromDb = () -> {
			refreshPalletManageDataFromDb();
			return true;
		};
		long timeoutInSec = 10;// for 5 - concurrent issue occured
		Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(ConstantConveyor.CHANNEL_KEY_DB_TO_GUI_REFRESH,
				taskRefreshPalletManageDataFromDb, 5, timeoutInSec, TimeUnit.SECONDS);

		// Option 1: Wait for it to complete, but with your own timeout
		try {
			boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
			ApplicationLauncher.logger
					.debug("functionalTest : refreshPalletManageDataFromDb: Task completed: responseStatus : "
							+ responseStatus + " : invoked by -> " + invokedBy);
		} catch (TimeoutException e) {
			ApplicationLauncher.logger
					.debug("functionalTest : refreshPalletManageDataFromDb : Task timed out (from caller side)"
							+ " : invoked by -> " + invokedBy);

		} catch (ExecutionException e) {
			ApplicationLauncher.logger.debug("functionalTest : refreshPalletManageDataFromDb : Task failed: "
					+ e.getCause() + " : invoked by -> " + invokedBy);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ApplicationLauncher.logger.debug("functionalTest : refreshPalletManageDataFromDb : Task interrupted: "
					+ " : invoked by -> " + invokedBy);

		}

	}

	public void refreshPalletManageDataFromDb() {

		Platform.runLater(() -> {
			if (ref_tvPalletManage != null && ref_tvPalletManage.getItems() != null) {
				ref_tvPalletManage.getItems().clear();
			}
			if (ref_tvPalletBayState != null && ref_tvPalletBayState.getItems() != null) {
				ref_tvPalletBayState.getItems().clear();
			}
			if (ref_tvPalletMeterResult != null && ref_tvPalletMeterResult.getItems() != null) {
				ref_tvPalletMeterResult.getItems().clear();
			}
		});

		int days = ConstantConveyorConfig.PALLET_MANAGE_RECENT_NO_OF_DAYS_DISPLAY;

		List<PalletManage> palletManageList = new ArrayList<PalletManage>();
		org.springframework.data.domain.Page<PalletManage> pageResult = null;
		if (days == 0) {
			pageResult = MySqlServiceManager.getPalletManageService().findAllPaginated(currentPage, pageSize);
		} else {
			// LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -days);
			Date cutoffDate = calendar.getTime();
			pageResult = MySqlServiceManager.getPalletManageService().findByCreatedAtAfterPaginated(cutoffDate,
					currentPage, pageSize);
		}
		palletManageList = pageResult.getContent();
		int totalPages = pageResult.getTotalPages();

		// ref_tvPalletManage.getItems().addAll(palletManageList);
		// reOrderedPalletManageSerialNo();
		List<PalletManage> palletManageListFinal = palletManageList;
		palletManageListFinal.forEach(BayTransitionValidator::validate);
		Platform.runLater(() -> {
			if (ref_tvPalletManage != null && ref_tvPalletManage.getItems() != null) {
				ref_tvPalletManage.getItems().setAll(palletManageListFinal); // optional: setAll replaces existing items
				reOrderedPalletManageSerialNo();
			}
			if (ref_lblPageInfo != null) {
				ref_lblPageInfo.setText("Page " + (currentPage + 1) + " of " + (totalPages == 0 ? 1 : totalPages));
			}
			if (ref_btnPrevPage != null) {
				ref_btnPrevPage.setDisable(currentPage == 0);
			}
			if (ref_btnNextPage != null) {
				ref_btnNextPage.setDisable(currentPage >= totalPages - 1 || totalPages == 0);
			}
		});

		// LOGIC UPDATED - 28/2/2025 -- findByPalletActive

		List<PalletManage> palletActiveList = MySqlServiceManager.getPalletManageService().findByPalletActive();
		for (PalletManage eachPallet : palletActiveList) {
			getActivePalletMap().put(eachPallet.getPalletQrId(), eachPallet.getPalletDistinctId());
		}

		ApplicationLauncher.logger.debug("refreshFromDb: getActivePalletMap: " + getActivePalletMap());

		// presentPalletAtBayMap;///

		if (ref_tvPalletManage.getItems().size() > 0) {
			// ref_lvDbMeterList.getItems().clear();
			ref_tvPalletMeter.getItems().clear();
			PalletManage palletBayTest = ref_tvPalletManage.getItems().get(0);
			// List<PalletMeter> palletMeterList = palletBayTest.getPalletMeterList();
			Set<PalletMeter> palletMeterSetList = palletBayTest.getPalletMeterList();// GopiConveyorReport
			/*
			 * for(int i =0 ; i< palletMeterList.size();i++) {
			 * ref_lvDbMeterList.getItems().add(palletMeterList.get(i).getMeterSerialNo());
			 * }
			 */

			/*
			 * palletMeterSetList.forEach(e->{
			 * ref_lvDbMeterList.getItems().add(e.getMeterSerialNo());
			 * });
			 */

			// List<PalletMeter> sortedPalletMeterList = new ArrayList<PalletMeter>();

			// Collections.sort(Comparator.comparing(PalletMeter::getStartDate));
			List<PalletMeter> sortedPalletMeterList = palletMeterSetList.stream()
					.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo)).collect(Collectors.toList());
			// ;////(e1,e2)->(e1.getRackPositionNo()>e2.getRackPositionNo()))
			// .collect(Collectors.toList());
			// sortedPalletMeterList.addAll(palletMeterList);
			ref_tvPalletMeter.getItems().addAll(sortedPalletMeterList);
			reOrderedPalletMeterSerialNo();
			// Set<PalletMeterResults> palletMeterResults = new
			// HashSet<PalletMeterResults>();
			Set<PalletMeter> palletMeterSetList2 = new HashSet<PalletMeter>();
			List<PalletMeterResults> palletMeterResultsList = new ArrayList<PalletMeterResults>();
			for (int i = 0; i < ref_tvPalletManage.getItems().size(); i++) {
				palletMeterSetList2 = ref_tvPalletManage.getItems().get(i).getPalletMeterList();
				for (PalletMeter eachPalletMeter : palletMeterSetList2) {
					palletMeterResultsList.addAll(eachPalletMeter.getPalletMeterResultsList());
					// ref_tvPalletMeterResult.getItems().addAll(eachPalletMeter.getPalletMeterResultsList());
				}
			}

			ref_tvPalletMeterResult.getItems().addAll(palletMeterResultsList);
			reOrderedPalletMeterResultsSerialNo();

			Set<PalletBayState> palletBayStateList = new HashSet<PalletBayState>();
			for (int i = 0; i < ref_tvPalletManage.getItems().size(); i++) {
				palletBayStateList = ref_tvPalletManage.getItems().get(i).getPalleteBayStateList();
				ref_tvPalletBayState.getItems().addAll(palletBayStateList);
			}
			reOrderedPalletBayStateSerialNo();

			// if(palletMeterSetList.size()>1) {

			populateResultSummary(palletMeterResultsList);
			reOrderedMeterResultSummarySerialNo();
			// }

			// ref_tvPalletBayState.getItems().addAll(palletBayStateList);

		}

		getActivePalletMap().entrySet().stream().forEach(e -> {
			ApplicationLauncher.logger.debug(
					"refreshFromDb: getActivePalletMap: QrCode: " + e.getKey() + " -> distinctId: " + e.getValue());
			// private static Map<String, String> activePalletMap = new
			// LinkedHashMap<String,String>();// key = palletQrCode, value =
			// palletDistinctId
			// private static Map<String, String> presentPalletAtBayMap = new
			// LinkedHashMap<String,String>();//key = bayKey, value = palletDistinctId
			// getPresentPalletAtBayMap().get(e.g)
			// gnbgh

		});

		getPresentPalletAtBayMap().entrySet().stream().forEach(e -> {
			ApplicationLauncher.logger.debug("refreshFromDb: getPresentPalletAtBayMap: bayKey: " + e.getKey()
					+ " -> distinctId: " + e.getValue());

		});

		// });
		// FXCollections.observableArrayList(products)
	}

	public void populateResultSummary(List<PalletMeterResults> palletMeterResultsList) {

		ref_tvMeterResultSummary.getItems().clear();
		// List<PalletMeter> palletMeterList = new ArrayList<PalletMeter>();
		// palletMeterList.addAll(palletMeterSetList);
		List<MeterResultSummary> meterResultSummaryList = new ArrayList<MeterResultSummary>();

		Map<String, ArrayList<String>> palletDistinctIdMeterSerialNumberMap = new LinkedHashMap<String, ArrayList<String>>();
		Set<String> meterSerialNoSet = new HashSet<String>();
		ArrayList<String> newMeterSerialNoList = new ArrayList<String>();
		ArrayList<String> existingMeterSerialNoList = new ArrayList<String>();
		for (PalletMeterResults eachPalletMeterResults : palletMeterResultsList) {
			// ApplicationLauncher.logger.debug("populateResultSummary: eachPalletMeter: " +
			// eachPalletMeterResults.getMeterSerialNo());
			meterSerialNoSet.add(eachPalletMeterResults.getMeterSerialNo());
			if (palletDistinctIdMeterSerialNumberMap.containsKey(eachPalletMeterResults.getPalletDistinctId())) {
				existingMeterSerialNoList = palletDistinctIdMeterSerialNumberMap
						.get(eachPalletMeterResults.getPalletDistinctId());
				existingMeterSerialNoList.add(eachPalletMeterResults.getMeterSerialNo());
				palletDistinctIdMeterSerialNumberMap.put(eachPalletMeterResults.getPalletDistinctId(),
						existingMeterSerialNoList);
			} else {
				newMeterSerialNoList = new ArrayList<String>();
				newMeterSerialNoList.add(eachPalletMeterResults.getMeterSerialNo());
				palletDistinctIdMeterSerialNumberMap.put(eachPalletMeterResults.getPalletDistinctId(),
						newMeterSerialNoList);
			}
			// palletDistinctIdMeterSerialNumberMap.put(eachPalletMeterResults.getPalletDistinctId(),
			// eachPalletMeterResults.getMeterSerialNo());
			/*
			 * MeterResultSummary MeterResultSummary = new MeterResultSummary();
			 * MeterResultSummary.setMeterSerialNo(eachPalletMeter.getMeterSerialNo());
			 * MeterResultSummary.setPalletDistinctId(palletDistinctId);
			 * meterResultSummarySet.add(MeterResultSummary);
			 */
		}

		ArrayList<String> eachPalletMeterList = new ArrayList<String>();
		boolean entryAlreadyExist = false;
		for (Entry<String, ArrayList<String>> eachPalletDistinct : palletDistinctIdMeterSerialNumberMap.entrySet()) {

			eachPalletMeterList = eachPalletDistinct.getValue();
			for (String eachMeterSerialNo : eachPalletMeterList) {
				entryAlreadyExist = meterResultSummaryList.stream()
						.filter(e -> e.getPalletDistinctId().equals(eachPalletDistinct.getKey()))
						.anyMatch(e -> e.getMeterSerialNo().equals(eachMeterSerialNo));
				if (!entryAlreadyExist) {
					MeterResultSummary MeterResultSummary = new MeterResultSummary();
					MeterResultSummary.setPalletDistinctId(eachPalletDistinct.getKey());
					MeterResultSummary.setMeterSerialNo(eachMeterSerialNo);
					meterResultSummaryList.add(MeterResultSummary);
				}

			}
		}

		/*
		 * for(String eachMeterSerialNo: meterSerialNoSet) {
		 * MeterResultSummary MeterResultSummary = new MeterResultSummary();
		 * MeterResultSummary.setMeterSerialNo(eachMeterSerialNo);
		 * meterResultSummaryList.add(MeterResultSummary);
		 * }
		 */

		for (PalletMeterResults eachPalletMeterResults : palletMeterResultsList) {
			// ApplicationLauncher.logger.debug("populateResultSummary: eachPalletMeter:
			// getTestType: " + eachPalletMeterResults.getTestType());

			meterResultSummaryList.stream()
					.filter(e1 -> e1.getPalletDistinctId().equals(eachPalletMeterResults.getPalletDistinctId()))
					.filter(e1 -> e1.getMeterSerialNo().equals(eachPalletMeterResults.getMeterSerialNo()))
					.forEach(e -> {
						e.setPalletBatchNo(eachPalletMeterResults.getPalletBatchNo());
						e.setPalletDistinctId(eachPalletMeterResults.getPalletDistinctId());
						e.setPalletQrId(eachPalletMeterResults.getPalletQrId());
						String testResult = eachPalletMeterResults.getResultStatus() + " "
								+ eachPalletMeterResults.getResultValue();
						if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.CREEP_ALIAS_NAME)) {
							e.setTestTypeNoLoad(testResult);

							// ApplicationLauncher.logger.debug("populateResultSummary: setTestTypeNoLoad: "
							// + e.getTestTypeNoLoad());

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.CALIB_ALIAS_NAME)) {
							e.setTestTypeCalib(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.STA_ALIAS_NAME)) {
							e.setTestTypeSta(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.FT_ALIAS_NAME)) {
							e.setTestTypeFt(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.HV_ALIAS_NAME)) {
							e.setTestTypeHv(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.IR_ALIAS_NAME)) {
							e.setTestTypeIr(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.ACCURACY_ALIAS_NAME)) {
							e.setTestVerific1(testResult);

						} else if (eachPalletMeterResults.getTestType().equals(ConstantConveyor.COMM_ALIAS_NAME)) {
							e.setTestTypeComm(testResult);

						}

					});

		}

		// ref_tvMeterResultSummary.getItems().addAll(meterResultSummaryList);
		ObservableList<MeterResultSummary> observableMeterResultSummaryList = FXCollections
				.observableArrayList(meterResultSummaryList);

		// ApplicationLauncher.logger.debug("populateResultSummary:
		// observableMeterResultSummaryList : " + observableMeterResultSummaryList);

		ref_tvMeterResultSummary.setItems(observableMeterResultSummaryList);
		ref_tvMeterResultSummary.refresh();

	}

	public void reOrderedPalletMeterSerialNo() {

		getPalletMeterSerialNoAtomic().set(1);
		ref_tvPalletMeter.getItems().stream().forEachOrdered(e -> {
			e.setSerialNo(getPalletMeterSerialNoAtomic().getAndIncrement());
			// ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " +
			// e.getPositionNo() + " -> getcName: " + e.getcName());

		});
		ref_tvPalletMeterResult.refresh();

	}

	public void reOrderedMeterResultSummarySerialNo() {

		getMeterResultSummarySerialNoAtomic().set(1);
		ref_tvMeterResultSummary.getItems().stream().forEachOrdered(e -> {
			e.setSerialNo(getMeterResultSummarySerialNoAtomic().getAndIncrement());
			// ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " +
			// e.getPositionNo() + " -> getcName: " + e.getcName());

		});
		ref_tvMeterResultSummary.refresh();

	}

	public void reOrderedPalletMeterResultsSerialNo() {

		getPalletMeterResultsSerialNoAtomic().set(1);
		ref_tvPalletMeterResult.getItems().stream().forEachOrdered(e -> {
			e.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
			// ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " +
			// e.getPositionNo() + " -> getcName: " + e.getcName());

		});
		ref_tvPalletMeterResult.refresh();

	}

	public void reOrderedPalletBayStateSerialNo() {
		// List<DeviceSetting> deviceSettingList = ref_tvDeviceSetting.getItems();
		getPalletBayStateSerialNoAtomic().set(1);
		ref_tvPalletBayState.getItems().stream().forEachOrdered(e -> {
			e.setSerialNo(getPalletBayStateSerialNoAtomic().getAndIncrement());
			// ApplicationLauncher.logger.debug("reOrderedSerialNo: getPositionNo : " +
			// e.getPositionNo() + " -> getcName: " + e.getcName());

		});
		ref_tvPalletBayState.refresh();
		// ref_tvDeviceSetting.getItems().clear();
		// ref_tvDeviceSetting.getItems().addAll(deviceSettingList);
	}

	public static void reOrderedPalletManageSerialNo() {
		// List<DeviceSetting> deviceSettingList = ref_tvDeviceSetting.getItems();
		if (ref_tvPalletManage != null && ref_tvPalletManage.getItems() != null) {
			getPalletManageSerialNoAtomic().set(1);
			ref_tvPalletManage.getItems().stream().forEachOrdered(e -> {
				e.setSerialNo(getPalletManageSerialNoAtomic().getAndIncrement());
			});
			ref_tvPalletManage.refresh();
		}
	}

	public void guiInit() {

		ArrayList<String> bayList = (ArrayList<String>) ConstantConveyor.getBayLookup().keySet().stream()
				.collect(Collectors.toList());
		ref_cmbBxSelectBayType.getItems().add("Select Bay");
		ref_cmbBxSelectBayType.getItems().addAll(bayList);
		if (bayList.size() > 0) {
			ref_cmbBxSelectBayType.getSelectionModel().select(1);
		} else {
			ref_cmbBxSelectBayType.getSelectionModel().select(0);
		}

		palletManageGuiInit();

		palletBayStateGuiInit();

		palletMeterResultGuiInit();

		palletMeterGuiInit();

		meterResultSummaryGuiInit();

		ref_lvMeterList.getItems().add("21");
		ref_lvMeterList.getItems().add("22");
		ref_lvMeterList.getItems().add("23");
		ref_lvMeterList.getItems().add("24");
		ref_lvMeterList.getItems().add("25");
		ref_lvMeterList.getItems().add("26");
	}

	public void meterResultSummaryGuiInit() {

		/*
		 * String resultStyleWfr = "-fx-text-fill: blue;-fx-alignment: CENTER;";
		 * String resultStylePass = "-fx-text-fill: green;-fx-alignment: CENTER";
		 * String resultStyleFail = "-fx-text-fill: red;-fx-alignment: CENTER;";
		 * String resultStyleUndefined = "-fx-text-fill: black;-fx-alignment: CENTER;";
		 * String resultStyleDefault = "-fx-text-fill: black;-fx-alignment: CENTER;";
		 */

		ref_tvMeterResultSummary.setEditable(true);
		colMsumPalletDistinctId.setCellValueFactory(data -> data.getValue().getPalletDistinctIdProperty());
		colMsumPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colMsumPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());
		colMsumMeterSerialNo.setCellValueFactory(data -> data.getValue().getMeterSerialNoProperty());
		colMsumSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colMsumTestVerific1.setCellValueFactory(data -> data.getValue().getTestVerific1Property());
		colMsumTestVerific1.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {
					presentValue = displayResultStyle(presentValue, this);

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestVerific1: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});
		colMsumTestTypeCalib.setCellValueFactory(data -> data.getValue().getTestTypeCalibProperty());
		colMsumTestTypeCalib.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_PASS ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeCalib: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

		colMsumTestTypeComm.setCellValueFactory(data -> data.getValue().getTestTypeCommProperty());
		colMsumTestTypeComm.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeComm: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

		colMsumTestTypeHv.setCellValueFactory(data -> data.getValue().getTestTypeHvProperty());
		colMsumTestTypeHv.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_PASS ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeHv: updateItem: Exception: " + e.getMessage(), e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

		colMsumTestTypeIr.setCellValueFactory(data -> data.getValue().getTestTypeIrProperty());
		colMsumTestTypeIr.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_PASS ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeIr: updateItem: Exception: " + e.getMessage(), e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

		colMsumTestTypeSta.setCellValueFactory(data -> data.getValue().getTestTypeStaProperty());
		colMsumTestTypeSta.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_PASS ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeSta: updateItem: Exception: " + e.getMessage(), e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});
		colMsumOverAllStatus.setCellValueFactory(data -> data.getValue().getOverAllStatusProperty());
		colMsumOverAllStatus.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					// presentValue = displayResultStyle (presentValue, this);
					if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
						setStyle("-fx-text-fill: blue;-fx-alignment: CENTER");
					} else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
						setStyle("-fx-background-color: lightgreen;-fx-alignment: CENTER");// greenyellow, limegreen
						// presentValue = presentValue.replaceFirst(ConstantReport.REPORT_POPULATE_PASS
						// ,"" ).trim();
					} else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
						setStyle("-fx-background-color: tomato;-fx-alignment: CENTER");
						// presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
						// ).trim();
					} else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
						setStyle("-fx-text-fill: black;-fx-alignment: CENTER");
						// presentValue = presentValue.substring(2);
					} else if (presentValue.isEmpty()) {
						setStyle("-fx-text-fill: black;-fx-alignment: CENTER");
					}

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumOverAllStatus: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});
		// colMsumTestTypeNoLoad.setStyle( "-fx-alignment: CENTER;");

		colMsumTestTypeFt.setCellValueFactory(data -> data.getValue().getTestTypeFtProperty());
		colMsumTestTypeFt.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replaceFirst(ConstantReport.REPORT_POPULATE_PASS
					 * ,"" ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * //presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeFt: updateItem: Exception: " + e.getMessage(), e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

		colMsumTestTypeNoLoad.setCellValueFactory(data -> data.getValue().getTestTypeNoLoadProperty());
		colMsumTestTypeNoLoad.setCellFactory(param -> new TableCell<MeterResultSummary, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeNoLoad: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					presentValue = displayResultStyle(presentValue, this);
					/*
					 * if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
					 * setStyle(resultStyleWfr);
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
					 * setStyle(resultStylePass);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_PASS ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
					 * setStyle(resultStyleFail);
					 * presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
					 * ).trim();
					 * } else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
					 * setStyle(resultStyleUndefined);
					 * presentValue = presentValue.substring(2);
					 * } else if (presentValue.isEmpty()) {
					 * setStyle(resultStyleDefault);
					 * }
					 */

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colMsumTestTypeNoLoad: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});

	}

	public String displayResultStyle(String displayPresentValue, TableCell tableCell) {

		if (displayPresentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
			tableCell.setStyle(getResultStyleWfr());
		} else if (displayPresentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
			tableCell.setStyle(getResultStylePass());
			displayPresentValue = displayPresentValue.replaceFirst(ConstantReport.REPORT_POPULATE_PASS, "").trim();
		} else if (displayPresentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
			tableCell.setStyle(getResultStyleFail());
			displayPresentValue = displayPresentValue.replaceFirst(ConstantReport.REPORT_POPULATE_FAIL, "").trim();
		} else if (displayPresentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
			tableCell.setStyle(getResultStyleUndefined());
			// presentValue = presentValue.substring(2);
		} else if (displayPresentValue.isEmpty()) {
			tableCell.setStyle(getResultStyleDefault());
		}
		return displayPresentValue;
	}

	@FXML
	public void computeMeterOverAllStatus() {
		//// ********************************************************************
		// this logic need to be modified additiona validation all test case completed
		//// .s since verification has lot of test
		// **********************************************************************
		String meterOverAllStatus = ConstantReport.REPORT_POPULATE_WFR;

		// int selectedIndex =
		// ref_tvMeterResultSummary.getSelectionModel().getSelectedIndex();
		// MeterResultSummary meterResultSummary =
		// ref_tvMeterResultSummary.getSelectionModel().getSelectedItem();
		MeterResultSummary meterResultSummary;
		Map<String, Boolean> meterTestTypeResultTemplate = new HashMap<String, Boolean>();
		for (String eachTestType : ConstantConveyor.CONV_TEST_TYPE_ALIAS_LIST) {
			meterTestTypeResultTemplate.put(eachTestType, false);
		}
		for (int i = 0; i < ref_tvMeterResultSummary.getItems().size(); i++) {
			meterResultSummary = ref_tvMeterResultSummary.getItems().get(i);
			meterOverAllStatus = ConstantReport.REPORT_POPULATE_WFR;

			String presentMeterSerialNo = meterResultSummary.getMeterSerialNo();
			String palletDistinctId = meterResultSummary.getPalletDistinctId();

			PalletMeter palletMeter = MySqlServiceManager.getPalletMeterService()
					.findByMeterSerialNoAndPalletDistinctId(presentMeterSerialNo, palletDistinctId);

			if (palletMeter != null) {

				Map<String, Boolean> meterTestTypeResultPresent = new HashMap<String, Boolean>(
						meterTestTypeResultTemplate);

				/*
				 * palletMeter.getPalletMeterResultsList().stream().forEach(e->{
				 * if(palletMeter.getPalletMeterResultsList().contains(o)) {
				 * 
				 * }
				 * meterTestTypePresent.put(e.getTestType(), true);
				 * });
				 */

				palletMeter.getPalletMeterResultsList().stream().forEach(e -> {
					ApplicationLauncher.logger.debug("computeMeterOverAllStatus: getPalletMeterResultsList : "
							+ e.getMeterSerialNo() + " : " + e.getTestType());
					meterTestTypeResultPresent.put(e.getTestType(), true);
				});
				ApplicationLauncher.logger
						.debug("computeMeterOverAllStatus: presentMeterSerialNo : " + presentMeterSerialNo);

				meterTestTypeResultPresent.entrySet().stream().forEach(e -> {
					ApplicationLauncher.logger.debug("computeMeterOverAllStatus: meterTestTypeResultPresent : key: "
							+ e.getKey() + " -> " + e.getValue());
					// meterTestTypePresent.put(e.getTestType(), true);
				});

				Boolean allTestTypeCompleted = meterTestTypeResultPresent.entrySet().stream()
						.allMatch(e -> e.getValue().equals(true)); // this logic need to be modified for all testing
																	// completed
				ApplicationLauncher.logger
						.debug("computeMeterOverAllStatus: allTestTypeCompleted : " + allTestTypeCompleted);
				if (allTestTypeCompleted) {
					boolean failedResultFound = palletMeter.getPalletMeterResultsList().stream()
							.filter(e -> e.getMeterSerialNo().equals(presentMeterSerialNo))
							.anyMatch(e -> e.getResultStatus().equals(ConstantReport.REPORT_POPULATE_FAIL));

					if (failedResultFound) {
						meterOverAllStatus = ConstantReport.REPORT_POPULATE_FAIL;
					} else {
						meterOverAllStatus = ConstantReport.REPORT_POPULATE_PASS;
					}
				}

			}

			meterResultSummary.setOverAllStatus(meterOverAllStatus);
			ref_tvMeterResultSummary.getItems().set(i, meterResultSummary);

			palletMeter.setOverAllTestResultStatus(meterOverAllStatus);
			MySqlServiceManager.getPalletMeterService().save(palletMeter);
		}

	}

	public void palletMeterGuiInit() {

		ref_tvPalletMeter.setEditable(true);
		colPmtrMeterSerialNo.setCellValueFactory(data -> data.getValue().getMeterSerialNoProperty());
		colPmtrPositionNo.setCellValueFactory(data -> data.getValue().getRackPositionNoProperty().asObject());
		colPmtrResultStatus.setCellValueFactory(data -> data.getValue().getOverAllTestResultStatusProperty());
		colPmtrErrorCode.setCellValueFactory(data -> data.getValue().getErrorCodeProperty());
		colPmtrResultStatus.setCellFactory(param -> new TableCell<PalletMeter, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit:
				// colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String presentValue = item; // Use the current item value
				try {

					// presentValue = displayResultStyle (presentValue, this);
					if (presentValue.contains(ConstantReport.REPORT_POPULATE_WFR)) {
						setStyle("-fx-text-fill: blue;-fx-alignment: CENTER");
					} else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
						setStyle("-fx-background-color: lightgreen;-fx-alignment: CENTER");// greenyellow, limegreen
						// presentValue = presentValue.replaceFirst(ConstantReport.REPORT_POPULATE_PASS
						// ,"" ).trim();
					} else if (presentValue.startsWith(ConstantReport.REPORT_POPULATE_FAIL)) {
						setStyle("-fx-background-color: tomato;-fx-alignment: CENTER");
						// presentValue = presentValue.replace(ConstantReport.REPORT_POPULATE_FAIL ,""
						// ).trim();
					} else if (presentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {
						setStyle("-fx-text-fill: black;-fx-alignment: CENTER");
						// presentValue = presentValue.substring(2);
					} else if (presentValue.isEmpty()) {
						setStyle("-fx-text-fill: black;-fx-alignment: CENTER");
					}

				} catch (Exception e) {
					ApplicationLauncher.logger.error("colPmtrResultStatus: updateItem: Exception: " + e.getMessage(),
							e);
				}

				setText(presentValue); // Set the text for the cell
			}
		});
		colPmtrSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colPmtrPalletDistinctId.setCellValueFactory(data -> data.getValue().getPalletDistinctIdProperty());
	}

	public void palletMeterResultSummaryInit() {

		ref_tvMeterResultSummary.setEditable(true);
		colPmtrMeterSerialNo.setCellValueFactory(data -> data.getValue().getMeterSerialNoProperty());
		/*
		 * colPmtrPositionNo.setCellValueFactory(data ->
		 * data.getValue().getRackPositionNoProperty().asObject());
		 * colPmtrResultStatus.setCellValueFactory(data ->
		 * data.getValue().getOverAllTestResultStatusProperty());
		 * colPmtrSerialNo.setCellValueFactory(data ->
		 * data.getValue().getSerialNoProperty().asObject());
		 */
	}

	public void palletMeterResultGuiInit() {

		ref_cmbBxAddResultSelectBayTestType.getItems().addAll(ConstantConveyor.CONV_TEST_TYPE_ALIAS_LIST);
		ref_cmbBxAddResultSelectBayTestType.getSelectionModel().select(0);
		ref_tvPalletMeterResult.setEditable(true);

		colPmrSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colPmrBayType.setCellValueFactory(data -> data.getValue().getBayStateKeyProperty());
		colPmrPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());
		colPmrPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colPmrPalletBatchMapId.setCellValueFactory(data -> data.getValue().getPalletDistinctIdProperty());

		colPmrPositionNo.setCellValueFactory(data -> data.getValue().getRackPositionNoProperty().asObject());
		colPmrTestType.setCellValueFactory(data -> data.getValue().getTestTypeProperty());
		colPmrTestCaseName.setCellValueFactory(data -> data.getValue().getTestCaseNameProperty());
		colPmrResultValue.setCellValueFactory(data -> data.getValue().getResultValueProperty());
		colPmrResultStatus.setCellValueFactory(data -> data.getValue().getResultStatusProperty());
		colPmrMeterSerialNo.setCellValueFactory(data -> data.getValue().getMeterSerialNoProperty());
		/*
		 * 
		 * colPmrExpectedRangeValue
		 * 
		 * colPmrTestCaseEntryTime
		 * colPmrTestCaseExitTime
		 * colPmrTestCaseRunTime
		 */
	}

	public void palletBayStateGuiInit() {

		ref_tvPalletBayState.setEditable(true);
		colPbsBayEntryTime.setCellValueFactory(data -> data.getValue().getPalletBayEntryTimeStampH_Property());

		colPbsNoOfMetersPresent.setCellValueFactory(data -> data.getValue().getNoOfMeterPresentProperty().asObject());
		colPbsNoOfMetersPassed.setCellValueFactory(data -> data.getValue().getNoOfMeterPassedProperty().asObject());
		colPbsNoOfMetersFailed.setCellValueFactory(data -> data.getValue().getNoOfMeterFailedProperty().asObject());
		colPbsNoOfMetersRejected.setCellValueFactory(data -> data.getValue().getNoOfMeterRejectedProperty().asObject());
		colPbsBayExecutionStatus.setCellValueFactory(data -> data.getValue().getBayExecutionStatusProperty());
		colPbsBayResultStatus.setCellValueFactory(data -> data.getValue().getBayResultStatusProperty());
		colPbsBayExitTime.setCellValueFactory(data -> data.getValue().getPalletBayExitTimeStampH_Property());

		colPbsBayRunTime.setCellValueFactory(data -> data.getValue().getPalleteBayRunTimeInMinProperty());
		colPbsBayType.setCellValueFactory(data -> data.getValue().getBayStateKeyProperty());
		colPbsPalletBatchMapId.setCellValueFactory(data -> data.getValue().getPalletDistinctIdProperty());
		colPbsPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colPbsSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colPbsPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());

	}

	public void palletManageGuiInit() {

		ref_tvPalletManage.setEditable(true);
		ref_tvPalletManage.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

		contextMenu.getItems().addAll(reComputeResults, displayResultAtUnloadingBay, displayResultAtRejectionBay);
		ref_tvPalletManage.setContextMenu(contextMenu);

		ref_tvPalletManage.setRowFactory(tv -> {
			TableRow<PalletManage> row = new TableRow<>();

			row.setOnContextMenuRequested(event -> {
				if (!row.isEmpty()) {
					ref_tvPalletManage.getSelectionModel().select(row.getIndex()); // select the row
					contextMenu.show(row, event.getScreenX(), event.getScreenY());
				} else {
					contextMenu.hide(); // no context menu on empty row
				}
				event.consume();
			});

			return row;
		});

		reComputeResults.setOnAction(event -> {
			PalletManage selectedPallet = ref_tvPalletManage.getSelectionModel().getSelectedItem();
			if (selectedPallet != null) {

				ApplicationLauncher.logger
						.debug("palletManageGuiInit: reComputeResults: selectedPallet.getPalletDistinctId: "
								+ selectedPallet.getPalletDistinctId());
				if (ConstantConveyor.EXIT_BAY_LIST.contains(selectedPallet.getPresentBayKey())) {
					//
					ApplicationLauncher.logger.debug("palletManageGuiInit: reComputeResults: recompute initiated for "
							+ selectedPallet.getPalletDistinctId());
					new Thread(() -> {
						BayUtils bayUtils = new BayUtils();
						bayUtils.computeMeterOverAllStatus(selectedPallet.getPalletDistinctId());
					}).start();
				} else {
					WindowManager.InformUser(
							"ComputeResults", "Invalid Bay state : " + selectedPallet.getPresentBayKey()
									+ "\n\nOnly Bay state with " + ConstantConveyor.EXIT_BAY_LIST + " can be processed",
							AlertType.INFORMATION);
				}

			}
		});

		displayResultAtUnloadingBay.setOnAction(event -> {
			PalletManage selectedPallet = ref_tvPalletManage.getSelectionModel().getSelectedItem();
			if (selectedPallet != null) {

				ApplicationLauncher.logger
						.debug("palletManageGuiInit: displayResultAtUnloadingBay: selectedPallet.getPalletDistinctId: "
								+ selectedPallet.getPalletDistinctId());
				// if(ConstantConveyor.EXIT_BAY_LIST.contains(selectedPallet.getPresentBayKey()))
				// {
				if (ConstantConveyor.UNLOADING_BAY_KEY.equals(selectedPallet.getPresentBayKey())) {
					// BayUtils bayUtils = new BayUtils();
					new Thread(() -> {
						ApplicationLauncher.logger
								.debug("palletManageGuiInit: displayResultAtUnloadingBay: recompute initiated for "
										+ selectedPallet.getPalletDistinctId());

						ConveyorDataManager.setUnloadingBayScreenPalletResultDisplayUserRequested(true);
						ConveyorDataManager.setUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId(
								selectedPallet.getPalletDistinctId());
						S02_qR_Code_Scanning_of_Pallet s02_qR_Code_Scanning_of_Pallet = new S02_qR_Code_Scanning_of_Pallet();
						s02_qR_Code_Scanning_of_Pallet.handleRequest();
					}).start();

				} else {
					WindowManager.InformUser(
							"DisplayResult-Unloading", "Invalid Bay state : " + selectedPallet.getPresentBayKey()
									+ "\n\nOnly Bay state with " + ConstantConveyor.EXIT_BAY_LIST + " can be processed",
							AlertType.INFORMATION);
				}

			}
		});

		displayResultAtRejectionBay.setOnAction(event -> {
			PalletManage selectedPallet = ref_tvPalletManage.getSelectionModel().getSelectedItem();
			if (selectedPallet != null) {

				ApplicationLauncher.logger
						.debug("palletManageGuiInit: displayResultAtRejectionBay: selectedPallet.getPalletDistinctId: "
								+ selectedPallet.getPalletDistinctId());
				// if(ConstantConveyor.EXIT_BAY_LIST.contains(selectedPallet.getPresentBayKey()))
				// {
				if (ConstantConveyor.REJECTION_BAY_KEY.equals(selectedPallet.getPresentBayKey())) {
					// BayUtils bayUtils = new BayUtils();
					new Thread(() -> {
						ApplicationLauncher.logger
								.debug("palletManageGuiInit: displayResultAtRejectionBay: recompute initiated for "
										+ selectedPallet.getPalletDistinctId());
						ConveyorDataManager.setRejectionBayScreenPalletResultDisplayUserRequested(true);
						ConveyorDataManager.setRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId(
								selectedPallet.getPalletDistinctId());
						S02_qR_Code_Scanning_of_Rejected_Pallet s02_qR_Code_Scanning_of_Rejected_Pallet = new S02_qR_Code_Scanning_of_Rejected_Pallet();
						s02_qR_Code_Scanning_of_Rejected_Pallet.handleRequest();
					}).start();

				} else {
					WindowManager.InformUser(
							"DisplayResult-Rejection", "Invalid Bay state : " + selectedPallet.getPresentBayKey()
									+ "\n\nOnly Bay state with " + ConstantConveyor.EXIT_BAY_LIST + " can be processed",
							AlertType.INFORMATION);
				}

			}
		});

		colPmSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colPmBayType.setStyle("-fx-alignment: CENTER;");
		colPmBayType.setCellValueFactory(data -> data.getValue().getPresentBayKeyProperty());

		colPmPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colPmPalletActive.setStyle("-fx-alignment: CENTER;");
		colPmPalletActive.setCellValueFactory(new PalletBayTestPalletActive_CheckBoxValueFactory());

		colPmExitAppeared.setStyle("-fx-alignment: CENTER;");
		colPmExitAppeared.setCellValueFactory(new PalletBayTestPalletExitAppeared_CheckBoxValueFactory());

		colPmPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());

		colPmPalletBatchMapId.setCellValueFactory(data -> data.getValue().getPalletDistinctIdProperty());

		colPmConvEntryTime.setCellValueFactory(data -> data.getValue().getPalletConvEntryTimeStampH_Property());

		colPmConvExitTime.setCellValueFactory(data -> data.getValue().getPalletConvExitTimeStampH_Property());

		colPmTestExecutionStatus.setCellValueFactory(data -> data.getValue().getPalletExecutionStatusProperty());

		colPmTestResultStatus.setCellValueFactory(data -> data.getValue().getPalletResultStatusProperty());
		if (colPmTransitionError != null) {
			colPmTransitionError.setCellValueFactory(data -> data.getValue().getTransitionErrorDetailsProperty());
			colPmTransitionError.setStyle("-fx-text-fill: red;");
		}
		colPmConvPalletRunTime.setCellValueFactory(data -> data.getValue().getPalleteConveyorRunTimeInMinProperty());
		colPmNoOfMetersPresent.setCellValueFactory(data -> data.getValue().getNoOfMeterPresentProperty().asObject());

		colPmNoOfMetersPassed.setCellValueFactory(data -> data.getValue().getNoOfMeterPassedProperty().asObject());
		colPmNoOfMetersFailed.setCellValueFactory(data -> data.getValue().getNoOfMeterFailedProperty().asObject());
		colPmNoOfMetersRejected.setCellValueFactory(data -> data.getValue().getNoOfMeterRejectedProperty().asObject());

		ref_tvPalletManage.setRowFactory(tv -> {
			TableRow<PalletManage> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if ((!row.isEmpty())) {
					if (event.getClickCount() == 2 || (event.getClickCount() == 1)) {
						PalletManage palletBayTest = row.getItem();// GopiConveyorReport

						Set<PalletMeter> palletMeterList = palletBayTest.getPalletMeterList();
						// ref_lvDbMeterList.getItems().clear();
						ref_tvPalletMeter.getItems().clear();

						List<PalletMeter> sortedPalletMeterList = palletMeterList.stream()
								.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo))
								.collect(Collectors.toList());
						ref_tvPalletMeter.getItems().addAll(sortedPalletMeterList);
						reOrderedPalletMeterSerialNo();
						if (event.getClickCount() == 2) {
							ref_tvPalletBayState.getItems().clear();
							ref_tvPalletMeterResult.getItems().clear();
							Set<PalletBayState> palletBayStateList = palletBayTest.getPalleteBayStateList();

							ref_tvPalletBayState.getItems().addAll(palletBayStateList);
							reOrderedPalletBayStateSerialNo();

							new HashSet<PalletMeterResults>();

							palletMeterList = palletBayTest.getPalletMeterList();
							for (PalletMeter eachPalletMeter : palletMeterList) {
								ref_tvPalletMeterResult.getItems().addAll(eachPalletMeter.getPalletMeterResultsList());
							}
							// }
							reOrderedPalletMeterResultsSerialNo();
						}
					}
				}
			});
			return row;
		});

	}

	@FXML
	void meterSummaryApplyFilterOnClick() {
		ApplicationLauncher.logger.debug("meterSummaryApplyFilterOnClick: Entry ");
	}

	public static class PreviewRow {
		private final javafx.beans.property.StringProperty qrId = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty currentBay = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty expectedBay = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty currentStatus = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty expectedStatus = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty currentBatch = new javafx.beans.property.SimpleStringProperty();
		private final javafx.beans.property.StringProperty expectedBatch = new javafx.beans.property.SimpleStringProperty();

		public PreviewRow(String qrId, String currentBay, String currentStatus, String currentBatch) {
			this.qrId.set(qrId);
			this.currentBay.set(currentBay);
			this.expectedBay.set(currentBay);
			this.currentStatus.set(currentStatus);
			this.expectedStatus.set(currentStatus);
			this.currentBatch.set(currentBatch);
			this.expectedBatch.set(currentBatch);
		}

		public javafx.beans.property.StringProperty qrIdProperty() {
			return qrId;
		}

		public javafx.beans.property.StringProperty currentBayProperty() {
			return currentBay;
		}

		public javafx.beans.property.StringProperty expectedBayProperty() {
			return expectedBay;
		}

		public javafx.beans.property.StringProperty currentStatusProperty() {
			return currentStatus;
		}

		public javafx.beans.property.StringProperty expectedStatusProperty() {
			return expectedStatus;
		}

		public javafx.beans.property.StringProperty currentBatchProperty() {
			return currentBatch;
		}

		public javafx.beans.property.StringProperty expectedBatchProperty() {
			return expectedBatch;
		}
	}

	@FXML
	void onBulkEditClick(ActionEvent event) {
		ObservableList<PalletManage> selectedPallets = ref_tvPalletManage.getSelectionModel().getSelectedItems();
		if (selectedPallets == null || selectedPallets.isEmpty()) {
			WindowManager.InformUser("Bulk Edit", "No pallets selected for bulk edit.", AlertType.INFORMATION);
			return;
		}
		ApplicationLauncher.logger.debug("onBulkEditClick: Selected pallets count = " + selectedPallets.size());

		javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
		dialog.setTitle("Bulk Edit Pallets");
		dialog.setHeaderText("Edit fields for " + selectedPallets.size()
				+ " selected pallets.\nLeave blank to keep existing values.");
		dialog.getDialogPane().setPrefWidth(800);

		javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save",
				javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

		javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new javafx.geometry.Insets(20, 10, 10, 10));

		ComboBox<String> activeBox = new ComboBox<>();
		activeBox.getItems().addAll("No Change", "True", "False");
		activeBox.getSelectionModel().selectFirst();

		TextField batchNoField = new TextField();
		batchNoField.setPromptText("Leave empty for no change");

		TextField execStatusField = new TextField();
		execStatusField.setPromptText("Leave empty for no change");

		TextField resultStatusField = new TextField();
		resultStatusField.setPromptText("Leave empty for no change");

		ComboBox<String> actionBox = new ComboBox<>();
		actionBox.getItems().addAll("No Action", "Switch to Previous Bay", "Switch to Next Bay", "Add Pallet Bay State",
				"Mark as Completed", "Revert Completed", "Mark as Exit Appeared", "Revert Exit Appeared");
		actionBox.getSelectionModel().selectFirst();

		grid.add(new Label("Pallet Active:"), 0, 0);
		grid.add(activeBox, 1, 0);
		grid.add(new Label("Batch No:"), 0, 1);
		grid.add(batchNoField, 1, 1);
		grid.add(new Label("Execution Status:"), 0, 2);
		grid.add(execStatusField, 1, 2);
		grid.add(new Label("Result Status:"), 0, 3);
		grid.add(resultStatusField, 1, 3);
		grid.add(new Label("Bulk Action:"), 0, 4);
		grid.add(actionBox, 1, 4);

		TableView<PreviewRow> previewTable = new TableView<>();
		previewTable.setPrefHeight(200);

		javafx.scene.control.TableColumn<PreviewRow, String> colQrId = new javafx.scene.control.TableColumn<>(
				"Pallet QR");
		colQrId.setCellValueFactory(data -> data.getValue().qrIdProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colCurBay = new javafx.scene.control.TableColumn<>(
				"Current Bay");
		colCurBay.setCellValueFactory(data -> data.getValue().currentBayProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colExpBay = new javafx.scene.control.TableColumn<>(
				"Expected Bay");
		colExpBay.setCellValueFactory(data -> data.getValue().expectedBayProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colCurStatus = new javafx.scene.control.TableColumn<>(
				"Current Status");
		colCurStatus.setCellValueFactory(data -> data.getValue().currentStatusProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colExpStatus = new javafx.scene.control.TableColumn<>(
				"Expected Status");
		colExpStatus.setCellValueFactory(data -> data.getValue().expectedStatusProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colCurBatch = new javafx.scene.control.TableColumn<>(
				"Current Batch");
		colCurBatch.setCellValueFactory(data -> data.getValue().currentBatchProperty());
		javafx.scene.control.TableColumn<PreviewRow, String> colExpBatch = new javafx.scene.control.TableColumn<>(
				"Expected Batch");
		colExpBatch.setCellValueFactory(data -> data.getValue().expectedBatchProperty());

		previewTable.getColumns().addAll(colQrId, colCurBay, colExpBay, colCurStatus, colExpStatus, colCurBatch,
				colExpBatch);

		ObservableList<PreviewRow> previewData = javafx.collections.FXCollections.observableArrayList();
		for (PalletManage p : selectedPallets) {
			previewData.add(new PreviewRow(p.getPalletQrId(), p.getPresentBayKey(), p.getPalletExecutionStatus(),
					String.valueOf(p.getPalletBatchNo())));
		}
		previewTable.setItems(previewData);
		grid.add(new Label("Live Preview:"), 0, 5, 2, 1);
		grid.add(previewTable, 0, 6, 2, 1);

		Runnable updatePreview = () -> {
			String action = actionBox.getValue();
			String batchStr = batchNoField.getText();
			String execStr = execStatusField.getText();

			for (int i = 0; i < selectedPallets.size(); i++) {
				PreviewRow row = previewData.get(i);

				if (batchStr != null && !batchStr.trim().isEmpty()) {
					row.expectedBatchProperty().set(batchStr.trim());
				} else {
					row.expectedBatchProperty().set(row.currentBatchProperty().get());
				}

				String expStatus = row.currentStatusProperty().get();
				if (execStr != null && !execStr.trim().isEmpty()) {
					expStatus = execStr.trim();
				}
				String expBay = row.currentBayProperty().get();

				if ("Switch to Previous Bay".equals(action)) {
					int idx = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(expBay);
					if (idx > 0)
						expBay = ConstantConveyor.STATE_SEQUENCE_LIST.get(idx - 1);
					expStatus = ConstantConveyor.EXECUTION_STATUS_INPROGRESS;
				} else if ("Switch to Next Bay".equals(action)) {
					int idx = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(expBay);
					if (idx >= 0 && idx < ConstantConveyor.STATE_SEQUENCE_LIST.size() - 1) {
						expBay = ConstantConveyor.STATE_SEQUENCE_LIST.get(idx + 1);
					}
					expStatus = ConstantConveyor.EXECUTION_STATUS_COMPLETED;
				} else if ("Mark as Completed".equals(action)) {
					expStatus = ConstantConveyor.EXECUTION_STATUS_COMPLETED;
				} else if ("Revert Completed".equals(action)) {
					expStatus = ConstantConveyor.EXECUTION_STATUS_INPROGRESS;
				}

				row.expectedBayProperty().set(expBay);
				row.expectedStatusProperty().set(expStatus);
			}
		};

		actionBox.valueProperty().addListener((obs, oldV, newV) -> updatePreview.run());
		batchNoField.textProperty().addListener((obs, oldV, newV) -> updatePreview.run());
		execStatusField.textProperty().addListener((obs, oldV, newV) -> updatePreview.run());

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == saveButtonType) {
				String batchNoStr = batchNoField.getText();
				String execStr = execStatusField.getText();
				String resStr = resultStatusField.getText();
				String activeStr = activeBox.getValue();
				String actionStr = actionBox.getValue();

				for (PalletManage p : selectedPallets) {
					if (!activeStr.equals("No Change")) {
						p.setPalletActive("True".equals(activeStr));
					}
					if (batchNoStr != null && !batchNoStr.trim().isEmpty()) {
						try {
							p.setPalletBatchNo(Integer.parseInt(batchNoStr.trim()));
						} catch (Exception ex) {
						}
					}
					if (execStr != null && !execStr.trim().isEmpty()) {
						p.setPalletExecutionStatus(execStr.trim());
					}
					if (resStr != null && !resStr.trim().isEmpty()) {
						p.setPalletResultStatus(resStr.trim());
					}

					if ("Switch to Previous Bay".equals(actionStr))
						switchToPreviousBayForPallet(p);
					else if ("Switch to Next Bay".equals(actionStr))
						switchToNextBayForPallet(p);
					else if ("Add Pallet Bay State".equals(actionStr))
						addPalletBayStateForPallet(p);
					else if ("Mark as Completed".equals(actionStr))
						markAsCompletedForPallet(p);
					else if ("Revert Completed".equals(actionStr))
						revertCompletedForPallet(p);
					else if ("Mark as Exit Appeared".equals(actionStr))
						markAsExitAppearedForPallet(p);
					else if ("Revert Exit Appeared".equals(actionStr))
						revertExitAppearedForPallet(p);
				}
				MySqlServiceManager.getPalletManageService().saveAll(selectedPallets);
				return true;
			}
			return null;
		});

		Optional<Boolean> result = dialog.showAndWait();
		result.ifPresent(updated -> {
			if (updated) {
				ref_tvPalletManage.refresh();
				refreshPalletManageDataFromDbv2("BulkEdit");
				WindowManager.InformUser("Bulk Edit", "Bulk edit saved successfully.", AlertType.INFORMATION);
			}
		});
	}

	private void addPalletBayStateForPallet(PalletManage palletBayTracker) {
		ApplicationLauncher.logger.debug("addPalletBayStateForPallet: Entry ");

		PalletBayState palletBayState = new PalletBayState();
		palletBayState.setSerialNo(getPalletBayStateSerialNoAtomic().getAndIncrement());
		palletBayState.setPalletBatchNo(palletBayTracker.getPalletBatchNo());
		palletBayState.setPalletDistinctId(palletBayTracker.getPalletDistinctId());
		palletBayState.setBayStateKey(palletBayTracker.getPresentBayKey());
		palletBayState.setPalletQrId(palletBayTracker.getPalletQrId());
		palletBayState.setNoOfMeterPresent(palletBayTracker.getNoOfMeterPresent());
		palletBayState.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
		palletBayState.setBayResultStatus(ConstantReport.REPORT_POPULATE_WFR);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTime = LocalDateTime.now();
		ApplicationLauncher.logger.debug("addPalletBayState: Start Time: " + dtf.format(startTime));
		String palletBayEntryTime = dtf.format(startTime);
		ZoneId zoneId = ZoneId.systemDefault();
		long startTimeEpoch = startTime.atZone(zoneId).toInstant().toEpochMilli();
		palletBayState.setPalletBayEntryTimeStampH(palletBayEntryTime);
		palletBayState.setPalletBayEntryTimeEpoch(String.valueOf(startTimeEpoch));

		getPresentPalletAtBayMap().put(palletBayTracker.getPresentBayKey(), palletBayTracker.getPalletDistinctId());

		if (ref_tvPalletManage != null && ref_tvPalletManage.getSelectionModel() != null) {
			if (palletBayTracker.equals(ref_tvPalletManage.getSelectionModel().getSelectedItem())) {
				if (ref_tvPalletBayState != null && ref_tvPalletBayState.getItems() != null) {
					ref_tvPalletBayState.getItems().add(palletBayState);
					reOrderedPalletBayStateSerialNo();
				}
			}
		}
		// palletBayTracker.getPalleteBayStateList().add(palletBayState);

		palletBayTracker.addPalleteBayState(palletBayState);
	}

	@FXML
	void addPalletBayState() {
		if (ref_tvPalletManage != null && ref_tvPalletManage.getSelectionModel() != null) {
			PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
			if (palletBayTracker != null) {
				addPalletBayStateForPallet(palletBayTracker);
				ref_tvPalletManage.refresh();
			}
		}
	}

	public boolean addPalletBayState(String selectedBayTypeKey) {
		ApplicationLauncher.logger.debug("addPalletBayState: Entry : " + selectedBayTypeKey);
		boolean status = false;

		ApplicationLauncher.logger.debug("addPalletBayState: getPresentPalletAtBayMap : " + getPresentPalletAtBayMap());

		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
			if (getActivePalletMap().values().contains(myPalletDistinctId)) {

				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findLastByPalletDistinctId(myPalletDistinctId);

				PalletBayState palletBayState = new PalletBayState();
				palletBayState.setSerialNo(getPalletBayStateSerialNoAtomic().getAndIncrement());
				palletBayState.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletBayState.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletBayState.setBayStateKey(selectedBayTypeKey);

				ApplicationLauncher.logger.debug("addPalletBayState: selectedBayTypeKey : " + selectedBayTypeKey);

				palletBayState.setPalletQrId(myPalletManage.getPalletQrId());
				palletBayState.setNoOfMeterPresent(myPalletManage.getNoOfMeterPresent());
				palletBayState.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
				palletBayState.setBayResultStatus(ConstantReport.REPORT_POPULATE_WFR);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime startTime = LocalDateTime.now();
				ApplicationLauncher.logger.debug("addPalletBayState: Start Time: " + dtf.format(startTime));
				String palletBayEntryTime = dtf.format(startTime);
				ZoneId zoneId = ZoneId.systemDefault();
				long startTimeEpoch = startTime.atZone(zoneId).toInstant().toEpochMilli();
				palletBayState.setPalletBayEntryTimeStampH(palletBayEntryTime);
				palletBayState.setPalletBayEntryTimeEpoch(String.valueOf(startTimeEpoch));

				getPresentPalletAtBayMap().put(selectedBayTypeKey, myPalletManage.getPalletDistinctId());
				myPalletManage.addPalleteBayState(palletBayState);
				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

				ApplicationLauncher.logger.debug("addPalletBayState: new bay added " + " : selectedBayTypeKey: "
						+ selectedBayTypeKey + " : myPalletDistinctId : " + myPalletDistinctId);

				status = true;

			} else {
				ApplicationLauncher.logger
						.debug("addPalletBayState: pallet not in active mode " + " : selectedBayTypeKey: "
								+ selectedBayTypeKey + " : myPalletDistinctId : " + myPalletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addPalletBayState: Exception: " + e.getMessage());
		}

		return status;
	}

	public boolean addPalletNextBayState(String nextBayState) {
		ApplicationLauncher.logger.debug("addPalletNextBayState: Entry  : " + nextBayState);
		boolean status = false;

		ApplicationLauncher.logger
				.debug("addPalletNextBayState: getPresentPalletAtBayMap : " + getPresentPalletAtBayMap());

		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(nextBayState);
			if (getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);

				PalletBayState palletBayState = new PalletBayState();
				palletBayState.setSerialNo(getPalletBayStateSerialNoAtomic().getAndIncrement());
				palletBayState.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletBayState.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletBayState.setBayStateKey(nextBayState);

				ApplicationLauncher.logger.debug("addPalletNextBayState: myPalletManage.getPresentBayKey() : "
						+ myPalletManage.getPresentBayKey());

				palletBayState.setPalletQrId(myPalletManage.getPalletQrId());
				palletBayState.setNoOfMeterPresent(myPalletManage.getNoOfMeterPresent());
				palletBayState.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
				palletBayState.setBayResultStatus(ConstantReport.REPORT_POPULATE_WFR);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime startTime = LocalDateTime.now();
				ApplicationLauncher.logger.debug("addPalletNextBayState: Start Time: " + dtf.format(startTime));
				String palletBayEntryTime = dtf.format(startTime);
				ZoneId zoneId = ZoneId.systemDefault();
				long startTimeEpoch = startTime.atZone(zoneId).toInstant().toEpochMilli();
				palletBayState.setPalletBayEntryTimeStampH(palletBayEntryTime);
				palletBayState.setPalletBayEntryTimeEpoch(String.valueOf(startTimeEpoch));

				getPresentPalletAtBayMap().put(nextBayState, myPalletManage.getPalletDistinctId());
				myPalletManage.addPalleteBayState(palletBayState);
				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

				ApplicationLauncher.logger.debug("addPalletNextBayState: new bay added " + " : nextBayState: "
						+ nextBayState + " : myPalletDistinctId : " + myPalletDistinctId);

				status = true;

			} else {
				ApplicationLauncher.logger.debug("addPalletNextBayState: pallet not in active mode "
						+ " : nextBayState: " + nextBayState + " : myPalletDistinctId : " + myPalletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addPalletNextBayState: Exception: " + e.getMessage());
		}

		return status;
	}

	private void switchToNextBayForPallet(PalletManage palletBayTracker) {
		if (palletBayTracker.isPalletActive()) {
			String presentBayState = palletBayTracker.getPresentBayKey();
			String palletBatchMapId = palletBayTracker.getPalletDistinctId();
			int indexOfPresentState = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(presentBayState);
			if (ConstantConveyor.STATE_SEQUENCE_LIST.size() > (indexOfPresentState + 1)) {

				Set<PalletBayState> palleteBayStateList = palletBayTracker.getPalleteBayStateList();
				if (palleteBayStateList.size() > 0) {
					palleteBayStateList.stream().filter(e -> e.getBayStateKey().equals(presentBayState))
							.filter(e -> e.getPalletDistinctId().equals(palletBatchMapId))
							.forEach(e1 -> {
								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								LocalDateTime endTime = LocalDateTime.now();
								ApplicationLauncher.logger
										.debug("switchToNextBayOnClick: End Time: " + dtf.format(endTime));
								String palletBayExitTime = dtf.format(endTime);
								ZoneId zoneId = ZoneId.systemDefault();
								long exitTimeEpoch = endTime.atZone(zoneId).toInstant().toEpochMilli();
								e1.setPalletBayExitTimeStampH(palletBayExitTime);
								e1.setPalletBayExitTimeEpoch(String.valueOf(exitTimeEpoch));
								long palleteBayRuntimeInMin = (exitTimeEpoch
										- e1.getNormalizedEntryTimeMilli()) / 60000;
								ApplicationLauncher.logger.debug(
										"switchToNextBayOnClick: palleteBayRuntimeInMin: " + palleteBayRuntimeInMin);
								e1.setPalleteBayRunTimeInMin(String.valueOf(palleteBayRuntimeInMin));
								e1.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
								e1.setBayResultStatus(ConstantReport.REPORT_POPULATE_PASS);
								e1.setTestCompleted("Y");
							});
				}

				palletBayTracker.setPresentBayKey(ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState + 1));

				String destinationBayKey = ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState + 1);
				String matchedQrCode = null;

				for (Map.Entry<String, String> entry : getActivePalletMap().entrySet()) {
					if (entry.getValue().equals(palletBatchMapId)) {
						matchedQrCode = entry.getKey();
						break;
					}
				}
				String fromBayName = presentBayState;
				String toBayName = ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState + 1);
				String palletName = matchedQrCode;
				ConveyorDataManager.getDashboardObject().movePalletByName(palletName, fromBayName, toBayName,
						destinationBayKey);
				// ConveyorDeviceDataManagerController.getDashboardObject().removePalletByName(palletName,
				// fromBayName);

				ref_tvPalletBayState.refresh();
				addPalletBayStateForPallet(palletBayTracker);
			} else {
				ApplicationLauncher.logger.debug("switchToNextBayOnClick: reached End of Conveyor ");
				palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
				palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime endTime = LocalDateTime.now();
				ApplicationLauncher.logger.debug("switchToNextBayOnClick: End Time: " + dtf.format(endTime));
				String conveyorPalletExitTime = dtf.format(endTime);
				ZoneId zoneId = ZoneId.systemDefault();
				long exitTimeEpoch = endTime.atZone(zoneId).toInstant().toEpochMilli();
				palletBayTracker.setPalletConvExitTimeStampH(conveyorPalletExitTime);
				palletBayTracker.setPalletConvExitTimeEpoch(String.valueOf(exitTimeEpoch));
				palletBayTracker.setPalletActive(false);
				long conveyorPalleteRuntimeInMin = (exitTimeEpoch
						- Long.parseLong(palletBayTracker.getPalletConvEntryTimeEpoch())) / 60;
				ApplicationLauncher.logger
						.debug("switchToNextBayOnClick: conveyorPalleteRuntimeInMin: " + conveyorPalleteRuntimeInMin);
				palletBayTracker.setPalleteConveyorRunTimeInMin(String.valueOf(conveyorPalleteRuntimeInMin));
			}
		} else {
			ApplicationLauncher.logger.debug("switchToNextBayOnClick: Pallet is inactive");
		}
	}

	@FXML
	void switchToNextBayOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			switchToNextBayForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	private void switchToPreviousBayForPallet(PalletManage palletBayTracker) {
		if (palletBayTracker.isPalletActive()) {
			String presentBayState = palletBayTracker.getPresentBayKey();
			String palletBatchMapId = palletBayTracker.getPalletDistinctId();
			int indexOfPresentState = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(presentBayState);

			// Check if we can go back to a previous bay
			if (indexOfPresentState > 0) {
				String previousBayState = ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState - 1);

				Set<PalletBayState> palleteBayStateList = palletBayTracker.getPalleteBayStateList();
				if (palleteBayStateList.size() > 0) {
					palleteBayStateList.stream()
							.filter(e -> e.getBayStateKey().equals(presentBayState))
							.filter(e -> e.getPalletDistinctId().equals(palletBatchMapId))
							.forEach(e1 -> {
								ApplicationLauncher.logger
										.debug("switchToPreviousBayForPallet: Rolling back from bay: "
												+ presentBayState);
								e1.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
								e1.setBayResultStatus(ConstantReport.REPORT_POPULATE_WFR);
							});
				}

				palletBayTracker.setPresentBayKey(previousBayState);
				ref_tvPalletBayState.refresh();
				addPalletBayStateForPallet(palletBayTracker);
			} else {
				ApplicationLauncher.logger.debug("switchToPreviousBayForPallet: Already at the first bay.");
			}
		} else {
			ApplicationLauncher.logger
					.debug("switchToPreviswitchToPreviousBayForPalletousBayOnClick: Pallet is inactive");
		}
	}

	@FXML
	void switchToPreviousBayOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			switchToPreviousBayForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	public boolean closePresentBay(String selectedBayTypeKey, String resultStatus) {
		ApplicationLauncher.logger.debug("closePresentBay: Entry : selectedBayTypeKey: " + selectedBayTypeKey);
		boolean status = false;
		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
			ApplicationLauncher.logger
					.debug("closePresentBay:  getPresentPalletAtBayMap(): " + getPresentPalletAtBayMap());
			if (getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);
				if (myPalletManage.isPalletActive()) {
					String presentBayState = myPalletManage.getPresentBayKey();
					String palletBatchMapId = myPalletManage.getPalletDistinctId();
					ApplicationLauncher.logger.debug("closePresentBay: : presentBayState: " + presentBayState);
					ApplicationLauncher.logger.debug("closePresentBay: : palletBatchMapId: " + palletBatchMapId);
					ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(presentBayState);

					Set<PalletBayState> palleteBayStateList = myPalletManage.getPalleteBayStateList();

					if (palleteBayStateList.size() > 0) {

						Optional<PalletBayState> palletBayStateOpt = palleteBayStateList.stream()
								.filter(e -> e.getBayStateKey().equals(presentBayState))
								.filter(e1 -> e1.getPalletDistinctId().equals(palletBatchMapId))
								.findFirst();
						// .findFirst(e->e.getPalletDistinctId().equals(palletBatchMapId))
						if (palletBayStateOpt.isPresent()) {

							PalletBayState palletBayState = palletBayStateOpt.get();
							// .forEach(e1->{
							DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							LocalDateTime endTime = LocalDateTime.now();

							ApplicationLauncher.logger.debug("closePresentBay: End Time: " + dtf.format(endTime));

							String palletBayExitTime = dtf.format(endTime);
							ZoneId zoneId = ZoneId.systemDefault();
							long exitTimeEpoch = endTime.atZone(zoneId).toInstant().toEpochMilli();
							palletBayState.setPalletBayExitTimeStampH(palletBayExitTime);
							palletBayState.setPalletBayExitTimeEpoch(String.valueOf(exitTimeEpoch));
							long palleteBayRuntimeInMin = (exitTimeEpoch
									- palletBayState.getNormalizedEntryTimeMilli()) / 60000;

							ApplicationLauncher.logger
									.debug("closePresentBay: palleteBayRuntimeInMin: " + palleteBayRuntimeInMin);

							palletBayState.setPalleteBayRunTimeInMin(String.valueOf(palleteBayRuntimeInMin));
							palletBayState.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
							palletBayState.setBayResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
							if (resultStatus != null && resultStatus.startsWith(ConstantReport.REPORT_POPULATE_PASS)) {
								palletBayState.setTestCompleted("Y");
							}
							// });

							MySqlServiceManager.getPalletBayStateService().saveToDb(palletBayState);

						}
					}
				} else {
					ApplicationLauncher.logger.debug("closePresentBay: no pallet active ");
				}

			} else {
				ApplicationLauncher.logger.debug("closePresentBay: pallet not in active mode : selectedBayTypeKey : "
						+ selectedBayTypeKey + " : myPalletDistinctId : " + myPalletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("closePresentBay: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("closePresentBay: Exit: selectedBayTypeKey " + selectedBayTypeKey);
		return status;
	}

	public void switchPalletToNextBay(String selectedBayTypeKey, String nextBayState) {

		ApplicationLauncher.logger.debug("switchPalletToNextBay: Entry ");

		ConveyorDebugController.getTextFieldByKey(selectedBayTypeKey);

		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);

			ApplicationLauncher.logger
					.debug("switchPalletToNextBay : getPresentPalletAtBayMap() : " + getPresentPalletAtBayMap());

			if (getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);

				if (myPalletManage.isPalletActive()) {
					String presentBayState = myPalletManage.getPresentBayKey();
					String palletBatchMapId = myPalletManage.getPalletDistinctId();
					int indexOfPresentState = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(presentBayState);

					boolean nextBayIsExit = false;
					if (ConstantConveyor.EXIT_BAY_LIST.contains(nextBayState)) {
						ApplicationLauncher.logger.debug("switchPalletToNextBay: EXIT_BAY_LIST-X : present");
						nextBayIsExit = true;
					}
					if (ConstantConveyor.STATE_SEQUENCE_LIST.size() > (indexOfPresentState + 1) &&
							!nextBayState.equals(ConstantConveyor.REJECTION_BAY_KEY) &&
							!nextBayState.equals(ConstantConveyor.UNLOADING_BAY_KEY) &&
							!nextBayState.equals(ConstantConveyor.COMMUNICATION_BAY_KEY)
					// && !nextBayState.equals(ConstantConveyor.CALIBRATION_BAY_KEY)
					) {

						Set<PalletBayState> palleteBayStateList = myPalletManage.getPalleteBayStateList();

						if (palleteBayStateList.size() > 0) {
							palleteBayStateList.stream()
									.filter(e -> e.getBayStateKey().equals(presentBayState))
									.filter(e -> e.getPalletDistinctId().equals(palletBatchMapId))
									.forEach(e1 -> {
										DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
										LocalDateTime endTime = LocalDateTime.now();

										ApplicationLauncher.logger
												.debug("switchPalletToNextBay: End Time: " + dtf.format(endTime));

										String palletBayExitTime = dtf.format(endTime);
										ZoneId zoneId = ZoneId.systemDefault();
										long exitTimeEpoch = endTime.atZone(zoneId).toInstant().toEpochMilli();
										e1.setPalletBayExitTimeStampH(palletBayExitTime);
										e1.setPalletBayExitTimeEpoch(String.valueOf(exitTimeEpoch));

										e1.setNoOfMeterPresent(myPalletManage.getNoOfMeterPresent());
										e1.setNoOfMeterPassed(myPalletManage.getNoOfMeterPassed());
										e1.setNoOfMeterFailed(myPalletManage.getNoOfMeterFailed());

										long palleteBayRuntimeInMin = (exitTimeEpoch
												- e1.getNormalizedEntryTimeMilli()) / 60000;

										ApplicationLauncher.logger
												.debug("switchPalletToNextBay: palleteBayRuntimeInMin: "
														+ palleteBayRuntimeInMin);

										e1.setPalleteBayRunTimeInMin(String.valueOf(palleteBayRuntimeInMin));
										e1.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
										e1.setBayResultStatus(ConstantReport.REPORT_POPULATE_PASS);
										e1.setTestCompleted("Y");
									});
						}
						String palletDistinctId = myPalletManage.getPalletDistinctId();
						myPalletManage.setPresentBayKey(nextBayState);
						getPresentPalletAtBayMap().put(nextBayState, palletDistinctId);
						ApplicationLauncher.logger.debug("switchPalletToNextBay: getPresentPalletAtBayMap() : put : "
								+ getPresentPalletAtBayMap());
						ApplicationLauncher.logger.debug("switchPalletToNextBay: Removing Key : " + selectedBayTypeKey);
						ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState + 1);
						getPresentPalletAtBayMap().remove(selectedBayTypeKey);

						String matchedQrCode = null;

						for (Map.Entry<String, String> entry : getActivePalletMap().entrySet()) {
							if (entry.getValue().equals(palletBatchMapId)) {
								matchedQrCode = entry.getKey();
								break;
							}
						}

						String fromBayName = presentBayState;
						// String toBayName =
						// ConstantConveyor.STATE_SEQUENCE_LIST.get(indexOfPresentState+1);
						String palletName = matchedQrCode;
						// ConveyorDeviceDataManagerController.getDashboardObject().movePalletByName(palletName,
						// fromBayName, toBayName,destinationBayKey);
						ConveyorDataManager.getDashboardObject().removePalletByName(palletName, fromBayName);
						if (ConstantConveyor.GROUPED_BAY_LIST.contains(nextBayState)) { // nextBayState.equals(ConstantConveyor.WAITING_BAY_KEY)){

							Map<Integer, String> meterListWithSerialNoMap = new HashMap<Integer, String>();

							Set<PalletMeter> palletMeterSetList = new HashSet<PalletMeter>();
							palletMeterSetList = myPalletManage.getPalletMeterList();
							palletMeterSetList.stream()
									.sorted(Comparator.comparingInt(PalletMeter::getRackPositionNo))
									.collect(Collectors.toList());
							for (PalletMeter eachPalletMeter : palletMeterSetList) {
								meterListWithSerialNoMap.put(eachPalletMeter.getRackPositionNo(),
										eachPalletMeter.getMeterSerialNo());
							}
							// Platform.runLater(() -> {
							if (nextBayState.equals(ConstantConveyor.WAITING_BAY_KEY)) {
								ConveyorDataManager.getDashboardObject().addPalletToFirstAvailableWaitingBay(palletName,
										meterListWithSerialNoMap);
								BayUtils.delay(100);
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
								// Platform.runLater(()->{
								ConveyorDataManager.getDashboardObject().updateDashBoardPalletStatus(palletName,
										statusMap, errorCodeMap);
								// });
							}

							// });
						}
						// ref_tvPalletManage.getItems().set(selectedIndex, myPalletManage);
						if (ref_tvPalletBayState != null) {
							ref_tvPalletBayState.refresh();
						}

						ApplicationLauncher.logger.debug("switchPalletToNextBay: getPresentPalletAtBayMap() : remove :"
								+ getPresentPalletAtBayMap());

						if (nextBayState.equals(ConstantConveyor.WAITING_BAY_KEY)
								|| nextBayState.equals(ConstantConveyor.REJECTION_BAY_KEY)) {
							addPalletBayState(nextBayState);
						}
						// addPalletBayState(nextBayState);

						ref_tvPalletBayState.refresh();
					} else {
						ApplicationLauncher.logger.debug("switchPalletToNextBay: reached End of Conveyor ");
						myPalletManage.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
						myPalletManage.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);

						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime endTime = LocalDateTime.now();
						ApplicationLauncher.logger.debug("switchPalletToNextBay: End Time: " + dtf.format(endTime));
						String conveyorPalletExitTime = dtf.format(endTime);
						ZoneId zoneId = ZoneId.systemDefault();
						long exitTimeEpoch = endTime.atZone(zoneId).toInstant().toEpochMilli();
						String palletDistinctId = myPalletManage.getPalletDistinctId();
						myPalletManage.setPresentBayKey(nextBayState);

						getPresentPalletAtBayMap().remove(selectedBayTypeKey);
						String palletKey = extractPalletKey(palletDistinctId); // Extract the correct key
						getActivePalletMap().remove(palletKey);
						ref_tvPalletBayState.refresh();

						getPresentPalletAtBayMap().put(nextBayState, palletDistinctId);
						myPalletManage.setPalletConvExitTimeStampH(conveyorPalletExitTime);
						myPalletManage.setPalletConvExitTimeEpoch(String.valueOf(exitTimeEpoch));
						myPalletManage.setPalletActive(false);
						long conveyorPalleteRuntimeInMin = (exitTimeEpoch
								- Long.parseLong(myPalletManage.getPalletConvEntryTimeEpoch())) / 60;
						ApplicationLauncher.logger.debug(
								"switchPalletToNextBay: conveyorPalleteRuntimeInMin: " + conveyorPalleteRuntimeInMin);
						myPalletManage.setPalleteConveyorRunTimeInMin(String.valueOf(conveyorPalleteRuntimeInMin));
						if (nextBayIsExit) {

							ApplicationLauncher.logger
									.debug("switchPalletToNextBay: Adding to getRejectionBayPalletDistinctIdList : "
											+ myPalletManage.getPalletDistinctId());
							BayUtils.getRejectionBayPalletDistinctIdList().add(myPalletManage.getPalletDistinctId());
						}
						// ref_tvPalletManage.getItems().set(selectedIndex, palletBayTracker);
					}
					// String palletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
					// getPresentPalletAtBayMap().put(nextBayState, palletDistinctId);

					myPalletManage.setNoOfMeterPassed(0);
					myPalletManage.setNoOfMeterFailed(0);

					MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
					// refreshPalletManageDataFromDb();
					// refreshPalletManageDataFromDbv2("PalletTracker-switchPalletsToNextBay-2");

				} else {
					ApplicationLauncher.logger
							.debug("switchPalletToNextBay: pallet not in active mode " + " : selectedBayTypeKey: "
									+ selectedBayTypeKey + " : myPalletDistinctId : " + myPalletDistinctId);
				}

			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("switchPalletToNextBay: Exception: " + e.getMessage());
		}
	}

	public void switchBatchToNextBay(String selectedBayTypeKey, String nextBayState) {
		ApplicationLauncher.logger.debug("switchBatchToNextBay: Entry");

		try {
			// Fetch the list of active pallets in the selected bay
			List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(selectedBayTypeKey);

			// ApplicationLauncher.logger.debug("switchBatchToNextBay: myPalletManageList :
			// " + myPalletManageList);

			if (myPalletManageList.isEmpty()) {
				ApplicationLauncher.logger.debug("No active pallets found in bay: " + selectedBayTypeKey);
				return;
			}

			/*
			 * Set<Integer> excludedBayKeys = Set.of(
			 * ConstantConveyor.STA_NLD1_BAY_KEY,
			 * ConstantConveyor.STA_NLD2_BAY_KEY
			 * );
			 */

			boolean nextBayIsExit = false;
			if (ConstantConveyor.EXIT_BAY_LIST.contains(nextBayState)) {
				ApplicationLauncher.logger.debug("switchBatchToNextBay: EXIT_BAY_LIST-X : present");
				nextBayIsExit = true;
			}
			for (PalletManage myPalletManage : myPalletManageList) {
				String myPalletDistinctId = myPalletManage.getPalletDistinctId();
				String presentBayState = myPalletManage.getPresentBayKey();
				int indexOfPresentState = ConstantConveyor.STATE_SEQUENCE_LIST.indexOf(presentBayState);
				ApplicationLauncher.logger.debug("switchBatchToNextBay: myPalletDistinctId-X : " + myPalletDistinctId);
				ApplicationLauncher.logger.debug("switchBatchToNextBay: presentBayState-X : " + presentBayState);
				ApplicationLauncher.logger.debug("switchBatchToNextBay: nextBayState-X : " + nextBayState);
				ApplicationLauncher.logger
						.debug("switchBatchToNextBay: (indexOfPresentState + 1): " + (indexOfPresentState + 1));

				ApplicationLauncher.logger.debug("switchBatchToNextBay: STATE_SEQUENCE_LIST.size()  : "
						+ ConstantConveyor.STATE_SEQUENCE_LIST.size());

				if (ConstantConveyor.STATE_SEQUENCE_LIST.size() > (indexOfPresentState + 1)
						&& !nextBayState.equals(ConstantConveyor.COMMUNICATION_BAY_KEY) // ){
						&& !nextBayState.equals(ConstantConveyor.UNLOADING_BAY_KEY)) {
					ApplicationLauncher.logger.debug("switchBatchToNextBay: Hit1");
					updatePalletBayState(myPalletManage, presentBayState);
					myPalletManage.setPresentBayKey(nextBayState);
					getPresentPalletAtBayMap().put(nextBayState, myPalletDistinctId);
				} else {
					ApplicationLauncher.logger.debug("switchBatchToNextBay: Hit2");
					updatePalletBayState(myPalletManage, presentBayState);
					myPalletManage.setPresentBayKey(nextBayState);
					getPresentPalletAtBayMap().put(nextBayState, myPalletDistinctId);
					markPalletAsCompleted(myPalletManage);

					if (nextBayIsExit) {

						ApplicationLauncher.logger
								.debug("switchBatchToNextBay: Adding to getUnloadingBayPalletDistinctIdList : "
										+ myPalletManage.getPalletDistinctId());
						BayUtils.getUnloadingBayPalletDistinctIdList().add(myPalletManage.getPalletDistinctId());
					}

				}

				myPalletManage.setNoOfMeterPassed(0);
				myPalletManage.setNoOfMeterFailed(0);

				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

				/// below if case need to be revoked when STA1 and STA2 is made active and up
				/// and running
				if (!(selectedBayTypeKey.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) &&
						!(selectedBayTypeKey.equals(ConstantConveyor.STA_NLD2_BAY_KEY))) {
					addPalletNextBayState(nextBayState);
				}

				// MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
			}

			// refreshPalletManageDataFromDb();
			// refreshPalletManageDataFromDbv2("PalletTracker-switchPalletsToNextBay");
			// ref_tvPalletBayState.refresh();
			ApplicationLauncher.logger
					.debug("switchBatchToNextBay: getPresentPalletAtBayMap() : " + getPresentPalletAtBayMap());
			ApplicationLauncher.logger.debug("switchBatchToNextBay: Updated pallets in bay: " + selectedBayTypeKey);
			ApplicationLauncher.logger.debug("switchBatchToNextBay: myPalletManageList : " + myPalletManageList);
		} catch (Exception e) {
			ApplicationLauncher.logger.error("switchBatchToNextBay: Exception: " + e.getMessage());
		}
	}

	private void updatePalletBayState(PalletManage myPalletManage, String presentBayState) {
		Set<PalletBayState> palletBayStateList = myPalletManage.getPalleteBayStateList();
		if (!palletBayStateList.isEmpty()) {
			palletBayStateList.stream()
					.filter(e -> e.getBayStateKey().equals(presentBayState))
					.forEach(e1 -> {
						LocalDateTime endTime = LocalDateTime.now();
						String palletBayExitTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endTime);
						long exitTimeEpoch = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

						e1.setPalletBayExitTimeStampH(palletBayExitTime);
						e1.setPalletBayExitTimeEpoch(String.valueOf(exitTimeEpoch));
						e1.setNoOfMeterPresent(myPalletManage.getNoOfMeterPresent());
						e1.setNoOfMeterPassed(myPalletManage.getNoOfMeterPassed());
						e1.setNoOfMeterFailed(myPalletManage.getNoOfMeterFailed());
						e1.setPalleteBayRunTimeInMin(
								String.valueOf((exitTimeEpoch - e1.getNormalizedEntryTimeMilli()) / 60000));
						e1.setBayExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
						e1.setBayResultStatus(ConstantReport.REPORT_POPULATE_PASS);
						e1.setTestCompleted("Y");
					});
		}
	}

	private void markPalletAsCompleted(PalletManage myPalletManage) {
		LocalDateTime endTime = LocalDateTime.now();
		String conveyorPalletExitTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(endTime);
		long exitTimeEpoch = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

		myPalletManage.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
		myPalletManage.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
		myPalletManage.setPalletConvExitTimeStampH(conveyorPalletExitTime);
		myPalletManage.setPalletConvExitTimeEpoch(String.valueOf(exitTimeEpoch));
		myPalletManage.setPalletActive(false);
		myPalletManage.setPalleteConveyorRunTimeInMin(
				String.valueOf((exitTimeEpoch - Long.parseLong(myPalletManage.getPalletConvEntryTimeEpoch())) / 60));
	}

	@FXML
	void addResultOnClick(ActionEvent event) {
		// String selectedMeterSerialNo =
		// lvDbMeterList.getSelectionModel().getSelectedItem();
		int positionNo = Integer.parseInt(ref_txtAddResultPositionNo.getText());// 2;
		String resultStatus = ref_txtAddResultStatus.getText();
		String resultValue = ref_txtAddResultValue.getText();
		String testCaseName = ref_txtAddResultTestCaseName.getText();
		String testType = ref_cmbBxAddResultSelectBayTestType.getSelectionModel().getSelectedItem();
		ref_tvPalletManage.getSelectionModel().getSelectedIndex();
		PalletManage myPalletManage = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		// MySqlServiceManager.getPalletManageService().saveToDb(palletTracker);
		if (myPalletManage != null) {
			String selectedMeterSerialNo = "";// =
			Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
					.filter(e -> e.getRackPositionNo() == positionNo)
					.findFirst();
			if (palletMeterOpt.isPresent()) {
				PalletMeter selectedPalletMeter = palletMeterOpt.get();
				selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();

				PalletMeterResults palletMeterResult = new PalletMeterResults();
				palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
				palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
				palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
				palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);
				palletMeterResult.setTestType(testType);
				palletMeterResult.setTestCaseName(testCaseName);
				palletMeterResult.setRackPositionNo(positionNo);
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);

				Optional<PalletMeterResults> palletMeterResultsOpt = palletMeterOpt.get().getPalletMeterResultsList()
						.stream()
						.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
						.filter(e -> e.getRackPositionNo() == positionNo)
						.filter(e -> e.getTestType().equals(testType))
						.filter(e -> e.getTestCaseName().equals(testCaseName))
						.findFirst();
				if (palletMeterResultsOpt.isPresent()) {
					ApplicationLauncher.logger
							.debug("addResultOnClick: db already exist " + " : Position No: " + positionNo);
					palletMeterResult = palletMeterResultsOpt.get();
					palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
					palletMeterResult.setResultValue(resultValue);

					PalletMeterResults palletMeterResultL = palletMeterResult;
					ref_tvPalletMeterResult.getItems().stream()
							.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
							.filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.forEach(e -> {
								e = palletMeterResultL;
							});
					/*
					 * if(palletMeterResultsViewOpt.isPresent()) {
					 * ref_tvPalletMeterResult.getItems().set(tableViewResultSerialNo-1,
					 * palletMeterResult);
					 * }
					 */
				} else {
					ApplicationLauncher.logger
							.debug("addResultOnClick: creating new record " + " : Position No: " + positionNo);

					selectedPalletMeter.addPalletMeterResults(palletMeterResult);
					ref_tvPalletMeterResult.getItems().add(palletMeterResult);
				}

				MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
				reOrderedPalletMeterResultsSerialNo();

			}
		}
	}

	public void addMeterResultSummaryWithPalletDetails(int positionNo, String resultStatus, String resultValue,
			String testCaseName, String testType, PalletManage myPalletManage) { // , int palletBayStateSelectedIndex,
																					// PalletManage myPalletManage) {
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: Entry");
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: positionNo: " + positionNo);// + " ->
																												// serialNo:
																												// " +
																												// myPalletManage.get);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: resultStatus: " + resultStatus);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: resultValue: " + resultValue);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: testCaseName: " + testCaseName);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: testType: " + testType);

		ref_tvPalletManage.getSelectionModel().getSelectedIndex();
		if (myPalletManage != null) {
			String selectedMeterSerialNo = "";// =

			ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: getPalletMeterList "
					+ myPalletManage.getPalletMeterList());

			Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
					.filter(e -> e.getRackPositionNo() == positionNo)
					.findFirst();

			if (palletMeterOpt.isPresent()) {
				ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: palletMeterOpt: present");
				PalletMeter selectedPalletMeter = palletMeterOpt.get();
				selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();

				PalletMeterResults palletMeterResult = new PalletMeterResults();
				palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
				palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
				palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
				palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);
				palletMeterResult.setTestType(testType);
				palletMeterResult.setTestCaseName(testCaseName);
				palletMeterResult.setRackPositionNo(positionNo);
				if (ConstantConveyor.TEST_NAME_SUMMARY_LIST.contains(testCaseName)) {
					palletMeterResult.setResultSummary(true);
					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetails: setting summary to true: testCaseName: "
									+ testCaseName);
				}

				Optional<PalletMeterResults> palletMeterResultsOpt = palletMeterOpt.get().getPalletMeterResultsList()
						.stream()
						.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
						.filter(e -> e.getRackPositionNo() == positionNo)
						.filter(e -> e.getTestType().equals(testType))
						.filter(e -> e.getTestCaseName().equals(testCaseName))
						.findFirst();

				if (palletMeterResultsOpt.isPresent()) {
					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetails: palletMeterResultsOpt: present");
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: db already exist "
							+ " : Position No: " + positionNo);
					palletMeterResult = palletMeterResultsOpt.get();
					palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
					palletMeterResult.setResultValue(resultValue);

					PalletMeterResults palletMeterResultL = palletMeterResult;
					ref_tvPalletMeterResult.getItems().stream()
							.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
							.filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.forEach(e -> {
								ApplicationLauncher.logger.debug(
										"addMeterResultSummaryWithPalletDetails: ref_tvPalletMeterResult: palletMeterResultL : Hit1 + SerialNo: "
												+ palletMeterResultL.getMeterSerialNo());
								e = palletMeterResultL;
							});

				} else {
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: creating new record "
							+ " : Position No: " + positionNo + " : serialNo:" + palletMeterResult.getMeterSerialNo());

					int getId = selectedPalletMeter.addPalletMeterResults(palletMeterResult);
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: new record id: " + getId
							+ " : Position No: " + positionNo + " : serialNo:" + palletMeterResult.getMeterSerialNo());

					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetails: palletMeterResult new record id: "
									+ palletMeterResult.getId() + " : Position No: " + positionNo + " : serialNo:"
									+ palletMeterResult.getMeterSerialNo());
					ref_tvPalletMeterResult.getItems().add(palletMeterResult);
				}

				int dbRecordId = MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
				ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetails: dbRecordId: " + dbRecordId
						+ " : Position No: " + positionNo);
				reOrderedPalletMeterResultsSerialNo();
			}
		}
	}

	public void addMeterResultSummaryWithPalletDetailsV2(int positionNo, String dutSerialNo, String resultStatus,
			String resultValue, String testCaseName, String testType, String palletDistinctId) { // , int
																									// palletBayStateSelectedIndex,
																									// PalletManage
																									// myPalletManage) {
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: Entry");
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: positionNo: " + positionNo);// + "
																												// ->
																												// serialNo:
																												// " +
																												// myPalletManage.get);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: resultStatus: " + resultStatus);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: resultValue: " + resultValue);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: testCaseName: " + testCaseName);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: testType: " + testType);
		ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: dutSerialNo: " + dutSerialNo);
		ApplicationLauncher.logger
				.debug("addMeterResultSummaryWithPalletDetailsV2: palletDistinctId: " + palletDistinctId);
		ref_tvPalletManage.getSelectionModel().getSelectedIndex();

		Optional<PalletManage> myPalletManageOpt = MySqlServiceManager.getPalletManageService()
				.findByPalletDistinctId(palletDistinctId);
		if (myPalletManageOpt.isPresent()) {
			// if(myPalletManage!=null) {
			PalletManage myPalletManage = myPalletManageOpt.get();
			// String selectedMeterSerialNo="";// =
			myPalletManage.getPalletMeterList().stream().forEach(e -> {
				ApplicationLauncher.logger
						.debug("addMeterResultSummaryWithPalletDetailsV2: getPalletMeterList: getMeterSerialNo: "
								+ e.getMeterSerialNo());

			});

			Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
					.filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
					.findFirst();

			if (palletMeterOpt.isPresent()) {
				ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: palletMeterOpt: present");
				PalletMeter selectedPalletMeter = palletMeterOpt.get();

				PalletMeterResults palletMeterResult = new PalletMeterResults();
				palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
				palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
				palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
				palletMeterResult.setMeterSerialNo(dutSerialNo);
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);
				palletMeterResult.setTestType(testType);
				palletMeterResult.setTestCaseName(testCaseName);
				palletMeterResult.setRackPositionNo(positionNo);
				if (ConstantConveyor.TEST_NAME_SUMMARY_LIST.contains(testCaseName)) {
					palletMeterResult.setResultSummary(true);
					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetailsV2: setting summary to true: testCaseName: "
									+ testCaseName);
				}

				Optional<PalletMeterResults> palletMeterResultsOpt = palletMeterOpt.get().getPalletMeterResultsList()
						.stream()
						.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
						// .filter(e->e.getRackPositionNo()==positionNo) bgfhc
						.filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
						.filter(e -> e.getTestType().equals(testType))
						.filter(e -> e.getTestCaseName().equals(testCaseName))
						.findFirst();

				if (palletMeterResultsOpt.isPresent()) {
					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetailsV2: palletMeterResultsOpt: present");
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: db already exist "
							+ " : Position No: " + positionNo);
					palletMeterResult = palletMeterResultsOpt.get();
					palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
					palletMeterResult.setResultValue(resultValue);

					BayUtils bayUtils = new BayUtils();
					bayUtils.archiveExistingResultInDb(palletMeterResultsOpt.get().getBayStateKey(), palletDistinctId);
					int getId = selectedPalletMeter.addPalletMeterResults(palletMeterResult);
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2 : Exist: new record id: "
							+ getId + " : Position No: " + positionNo + " : serialNo:"
							+ palletMeterResult.getMeterSerialNo());

					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetailsV2: Exist: palletMeterResult new record id: "
									+ palletMeterResult.getId() + " : Position No: " + positionNo + " : serialNo:"
									+ palletMeterResult.getMeterSerialNo());
					// ref_tvPalletMeterResult.getItems().add(palletMeterResult);

					int dbRecordId = MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: Exist: dbRecordId: "
							+ dbRecordId + " : Position No: " + positionNo);
				} else {
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: creating new record "
							+ " : Position No: " + positionNo + " : serialNo:" + palletMeterResult.getMeterSerialNo());

					int getId = selectedPalletMeter.addPalletMeterResults(palletMeterResult);
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: new record id: " + getId
							+ " : Position No: " + positionNo + " : serialNo:" + palletMeterResult.getMeterSerialNo());

					ApplicationLauncher.logger
							.debug("addMeterResultSummaryWithPalletDetailsV2: palletMeterResult new record id: "
									+ palletMeterResult.getId() + " : Position No: " + positionNo + " : serialNo:"
									+ palletMeterResult.getMeterSerialNo());

					int dbRecordId = MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
					ApplicationLauncher.logger.debug("addMeterResultSummaryWithPalletDetailsV2: dbRecordId: "
							+ dbRecordId + " : Position No: " + positionNo);

				}
			}
		} else {

			ApplicationLauncher.logger.debug(
					"addMeterResultSummaryWithPalletDetailsV2: Not found : palletDisctinctId: " + palletDistinctId);

		}
	}

	public void addMeterResultSummary(int positionNo, String resultStatus, String resultValue,
			String selectedBayTypeKey, String testType, String testCaseName) { // , int palletBayStateSelectedIndex,
																				// PalletManage myPalletManage) {

		ApplicationLauncher.logger
				.debug("addMeterResultSummary: getPresentPalletAtBayMap " + getPresentPalletAtBayMap());

		String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
		PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
				.findFirstByPalletDistinctId(myPalletDistinctId);
		if (myPalletManage != null) {
			String selectedMeterSerialNo = "";// =
			String selectedHardwareIdNo = "";
			ApplicationLauncher.logger
					.debug("addMeterResultSummary: getPalletMeterList " + myPalletManage.getPalletMeterList());

			Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
					.filter(e -> e.getRackPositionNo() == positionNo)
					.findFirst();

			ApplicationLauncher.logger.debug("addMeterResultSummary: palletMeterOpt " + palletMeterOpt);

			if (palletMeterOpt.isPresent()) {
				PalletMeter selectedPalletMeter = palletMeterOpt.get();
				selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();

				PalletMeterResults palletMeterResult = new PalletMeterResults();
				palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
				palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
				palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
				palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
				palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
				palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
				if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
					selectedHardwareIdNo = selectedPalletMeter.getHardwareId();
					palletMeterResult.setMeterHardwareId(selectedHardwareIdNo);
					ApplicationLauncher.logger
							.debug("addMeterResultSummary: HARDWARE_ID_FEATURE_ENABLED Hit1: selectedHardwareIdNo:<"
									+ selectedHardwareIdNo + "> : positionNo: " + positionNo);
				}
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);
				palletMeterResult.setTestType(testType);
				palletMeterResult.setTestCaseName(testCaseName);
				palletMeterResult.setRackPositionNo(positionNo);
				palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
				palletMeterResult.setResultValue(resultValue);
				if (ConstantConveyor.TEST_NAME_SUMMARY_LIST.contains(testCaseName)) {
					palletMeterResult.setResultSummary(true);
					ApplicationLauncher.logger
							.debug("addMeterResultSummary: setting summary to true: testCaseName: " + testCaseName);
				}

				Optional<PalletMeterResults> palletMeterResultsOpt = palletMeterOpt.get().getPalletMeterResultsList()
						.stream()
						.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
						.filter(e -> e.getRackPositionNo() == positionNo)
						.filter(e -> e.getTestType().equals(testType))
						.filter(e -> e.getTestCaseName().equals(testCaseName))
						.findFirst();

				if (palletMeterResultsOpt.isPresent()) {
					ApplicationLauncher.logger
							.debug("addMeterResultSummary: db already exist " + " : Position No: " + positionNo);
					palletMeterResult = palletMeterResultsOpt.get();
					palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
					palletMeterResult.setResultValue(resultValue);

					PalletMeterResults palletMeterResultL = palletMeterResult;
					ref_tvPalletMeterResult.getItems().stream()
							.filter(e -> e.getBayStateKey().equals(myPalletManage.getPresentBayKey()))
							.filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.forEach(e -> {
								e = palletMeterResultL;
							});
				} else {
					ApplicationLauncher.logger
							.debug("addMeterResultSummary: creating new record " + " : Position No: " + positionNo);

					selectedPalletMeter.addPalletMeterResults(palletMeterResult);
					ref_tvPalletMeterResult.getItems().add(palletMeterResult);
				}

				MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);

				reOrderedPalletMeterResultsSerialNo();
			}
		}
	}

	public boolean addResultToPalletMeter(int positionNo, String resultStatus, String resultValue,
			String selectedBayTypeKey, PalletManage myPalletManage, String testType, String testCaseName) {

		ApplicationLauncher.logger
				.debug("addResultToPalletMeter: Entry : Position No: " + positionNo + " : testCaseName: " + testCaseName
						+ " , resultStatus: " + resultStatus + " , resultValue: " + resultValue);
		// ApplicationLauncher.logger.debug("addResultToPalletMeter: Entry : Position
		// No: " + positionNo);
		boolean status = false;
		try {
			ApplicationLauncher.logger
					.debug("addResultToPalletMeter: getPresentPalletAtBayMap " + getPresentPalletAtBayMap());

			ApplicationLauncher.logger.debug("addResultToPalletMeter: getActivePalletMap " + getActivePalletMap());

			if (myPalletManage != null) {
				String selectedMeterSerialNo = "";// =
				Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
						.filter(e -> e.getRackPositionNo() == positionNo)
						.findFirst();

				ApplicationLauncher.logger.debug("addResultToPalletMeter: palletMeterOpt " + palletMeterOpt);

				if (palletMeterOpt.isPresent()) {
					PalletMeter selectedPalletMeter = palletMeterOpt.get();
					selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();
					ApplicationLauncher.logger.debug("addResultToPalletMeter: Entry : Position No: " + positionNo
							+ " : selectedMeterSerialNo: " + selectedMeterSerialNo);
					// Create new result
					PalletMeterResults palletMeterResult = new PalletMeterResults();
					palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
					palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
					palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
					palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
					palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
					palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
					palletMeterResult.setResultStatus(resultStatus);
					palletMeterResult.setResultValue(resultValue);
					palletMeterResult.setTestType(testType);
					palletMeterResult.setTestCaseName(testCaseName);
					palletMeterResult.setRackPositionNo(positionNo);

					// Check for existing match
					Optional<PalletMeterResults> palletMeterResultsOpt = selectedPalletMeter.getPalletMeterResultsList()
							.stream()
							.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
							.filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.findFirst();

					if (palletMeterResultsOpt.isPresent()) {
						ApplicationLauncher.logger
								.debug("addResultToPalletMeter: db already exist : Position No: " + positionNo);
						ApplicationLauncher.logger
								.debug("addResultToPalletMeter: db already exist : testCaseName: " + testCaseName);
						palletMeterResult = palletMeterResultsOpt.get();
					} else {
						ApplicationLauncher.logger
								.debug("addResultToPalletMeter: creating new record : Position No: " + positionNo);
						selectedPalletMeter.addPalletMeterResults(palletMeterResult);
						ApplicationLauncher.logger.debug("addResultToPalletMeter: creating new record : Position No: "
								+ positionNo + " : palletMeterResult.id: " + palletMeterResult.getId());
						// palletMeterResult.
						// MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);

					}

					status = true;
				}

			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addResultToPalletMeter: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("addResultToPalletMeter: Exit " + " : Position No: " + positionNo);
		return status;
	}

	public boolean addResultToPalletMeterV1_1(int positionNo, String dutSerialNo, String resultStatus,
			String resultValue, String selectedBayTypeKey, String palletDistinctId, String testType,
			String testCaseName, String error_min, String error_max) {

		ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: Entry : palletDisctinctId: " + palletDistinctId
				+ ", dutSerialNo: " + dutSerialNo + ", Position No: " + positionNo + " : testCaseName: " + testCaseName
				+ " , resultStatus: " + resultStatus + " , resultValue: " + resultValue);
		// ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: Entry :
		// Position No: " + positionNo);
		boolean status = false;
		try {
			ApplicationLauncher.logger
					.debug("addResultToPalletMeterV1_1: getPresentPalletAtBayMap " + getPresentPalletAtBayMap());

			ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: getActivePalletMap " + getActivePalletMap());
			Optional<PalletManage> myPalletManageOpt = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(palletDistinctId);
			if (myPalletManageOpt.isPresent()) {

				PalletManage myPalletManage = myPalletManageOpt.get();
				// String selectedMeterSerialNo="";// =
				Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
						.filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
						.findFirst();

				ApplicationLauncher.logger
						.debug("addResultToPalletMeterV1_1: palletMeterOpt " + palletMeterOpt.isPresent());

				if (palletMeterOpt.isPresent()) {
					PalletMeter selectedPalletMeter = palletMeterOpt.get();
					// selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();
					ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: Entry : Position No: " + positionNo
							+ " : dutSerialNo: " + dutSerialNo);
					// Create new result
					PalletMeterResults palletMeterResult = new PalletMeterResults();
					palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
					palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
					palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
					palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
					palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
					palletMeterResult.setMeterSerialNo(dutSerialNo);
					palletMeterResult.setResultStatus(resultStatus);
					palletMeterResult.setResultValue(resultValue);
					palletMeterResult.setTestType(testType);
					palletMeterResult.setTestCaseName(testCaseName);
					palletMeterResult.setRackPositionNo(positionNo);
					palletMeterResult.setPermissibleLowerLimit(error_min);
					palletMeterResult.setPermissibleUpperLimit(error_max);

					// Check for existing match
					Optional<PalletMeterResults> palletMeterResultsOpt = selectedPalletMeter.getPalletMeterResultsList()
							.stream()
							.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
							// .filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getMeterSerialNo().equals(dutSerialNo))
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.findFirst();

					if (palletMeterResultsOpt.isPresent()) {
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV1_1: db already exist : Position No: " + positionNo);
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV1_1: db already exist : testCaseName: " + testCaseName);
						BayUtils bayUtils = new BayUtils();
						bayUtils.archiveExistingResultInDb(selectedBayTypeKey, palletDistinctId);

						selectedPalletMeter.addPalletMeterResults(palletMeterResult);
						// ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: creating new
						// record : Position No: " + positionNo + " : palletMeterResult.id: "+
						// palletMeterResult.getId());
						MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV1_1: Exist: creating new record : Position No: "
										+ positionNo + " : palletMeterResult.id: " + palletMeterResult.getId());

						// palletMeterResult.setId(palletMeterResultsOpt.get().getId());
						// MySqlServiceManager.getPalletMeterResultsService().save(palletMeterResult);
						// palletMeterResult.setId(palletMeterResultsOpt.get().getId());
						// MySqlServiceManager.getPalletMeterResultsService().save(palletMeterResult);
					} else {
						// ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: creating new
						// record : Position No: " + positionNo );
						int newPalletMeterResult = selectedPalletMeter.addPalletMeterResults(palletMeterResult);
						// ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: creating new
						// record : Position No: " + positionNo + " : palletMeterResult.id: "+
						// palletMeterResult.getId());
						MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV1_1: creating new record : Position No: " + positionNo
										+ " : palletMeterResult.id: " + newPalletMeterResult);

						// palletMeterResult.
						// MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);

					}

					status = true;
				}

			} else {
				ApplicationLauncher.logger
						.debug("addResultToPalletMeterV1_1: Not found : palletDisctinctId: " + palletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addResultToPalletMeterV1_1: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("addResultToPalletMeterV1_1: Exit " + " : Position No: " + positionNo);
		return status;
	}

	public boolean addResultToPalletMeterV2(int positionNo, String resultStatus, String resultValue, String error_min,
			String error_max, String selectedBayTypeKey, PalletManage myPalletManage, String testType,
			String testCaseName) {

		ApplicationLauncher.logger.debug("addResultToPalletMeterV2: Entry " + " : Position No: " + positionNo);
		ApplicationLauncher.logger.debug("addResultToPalletMeterV2: Entry " + " : Position No: " + positionNo
				+ " , resultStatus: " + resultStatus);
		ApplicationLauncher.logger.debug("addResultToPalletMeterV2: Entry " + " : Position No: " + positionNo
				+ " , resultValue: " + resultValue);
		boolean status = false;
		try {
			ApplicationLauncher.logger
					.debug("addResultToPalletMeterV2: getPresentPalletAtBayMap " + getPresentPalletAtBayMap());

			ApplicationLauncher.logger.debug("addResultToPalletMeterV2: getActivePalletMap " + getActivePalletMap());

			if (myPalletManage != null) {
				String selectedMeterSerialNo = "";// =
				Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
						.filter(e -> e.getRackPositionNo() == positionNo)
						.findFirst();

				ApplicationLauncher.logger.debug("addResultToPalletMeterV2: palletMeterOpt " + palletMeterOpt);

				if (palletMeterOpt.isPresent()) {
					PalletMeter selectedPalletMeter = palletMeterOpt.get();
					selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();

					// Create new result
					PalletMeterResults palletMeterResult = new PalletMeterResults();
					palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
					palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
					palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
					palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
					palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
					palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
					palletMeterResult.setResultStatus(resultStatus);
					palletMeterResult.setResultValue(resultValue);
					palletMeterResult.setPermissibleLowerLimit(error_min);
					palletMeterResult.setPermissibleUpperLimit(error_max);
					palletMeterResult.setTestType(testType);
					palletMeterResult.setTestCaseName(testCaseName);
					palletMeterResult.setRackPositionNo(positionNo);

					// Check for existing match
					Optional<PalletMeterResults> palletMeterResultsOpt = selectedPalletMeter.getPalletMeterResultsList()
							.stream()
							.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
							.filter(e -> e.getRackPositionNo() == positionNo)
							.filter(e -> e.getTestType().equals(testType))
							.filter(e -> e.getTestCaseName().equals(testCaseName))
							.findFirst();

					if (palletMeterResultsOpt.isPresent()) {
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV2: db already exist : Position No: " + positionNo);
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV2: db already exist : testCaseName: " + testCaseName);
						palletMeterResult = palletMeterResultsOpt.get();
					} else {
						ApplicationLauncher.logger
								.debug("addResultToPalletMeterV2: creating new record : Position No: " + positionNo);
						selectedPalletMeter.addPalletMeterResults(palletMeterResult);

						// MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);

					}

					status = true;
				}

			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addResultToPalletMeter: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("addResultToPalletMeter: Exit " + " : Position No: " + positionNo);
		return status;
	}

	public boolean addResultToMeter(int positionNo, String resultStatus, String resultValue, String selectedBayTypeKey,
			String testType, String testCaseName) {

		boolean status = false;
		// status = addResultToMeterQueueTask( positionNo, resultStatus, resultValue,
		// selectedBayTypeKey, testType, testCaseName );
		ApplicationLauncher.logger.debug("PalletTracker : addResultToMeter: Entry: positionNo : " + positionNo);

		Callable<Boolean> taskAddResultToMeterDb = () -> {
			boolean queueStatus = addResultToMeterQueueTask(positionNo, resultStatus, resultValue, selectedBayTypeKey,
					testType, testCaseName);

			return queueStatus;
		};
		long timeoutInSec = 10;// for 5 - concurrent issue occured
		Future<Boolean> future = ChannelQueueRequestProcessor.addRequest(
				ConstantConveyor.CHANNEL_KEY_ADD_METER_RESULT_DB, taskAddResultToMeterDb, 5, timeoutInSec,
				TimeUnit.SECONDS);

		// Option 1: Wait for it to complete, but with your own timeout
		try {
			boolean responseStatus = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
			ApplicationLauncher.logger
					.debug("PalletTracker : addResultToMeter: Task completed: responseStatus : " + responseStatus);
		} catch (TimeoutException e) {
			ApplicationLauncher.logger.debug("PalletTracker : addResultToMeter : Task timed out (from caller side)");

		} catch (ExecutionException e) {
			ApplicationLauncher.logger.debug("PalletTracker : addResultToMeter : Task failed: " + e.getCause());

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			ApplicationLauncher.logger.debug("PalletTracker : addResultToMeter : Task interrupted: ");

		}

		ApplicationLauncher.logger.debug("PalletTracker : addResultToMeter: Exit : positionNo : " + positionNo);
		return status;
	}

	public boolean addResultToMeterQueueTask(int positionNo, String resultStatus, String resultValue,
			String selectedBayTypeKey, String testType, String testCaseName) {

		ApplicationLauncher.logger.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo);
		ApplicationLauncher.logger.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo
				+ " , selectedBayTypeKey: " + selectedBayTypeKey);
		boolean status = false;
		try {
			ApplicationLauncher.logger
					.debug("addResultToMeterQueueTask: getPresentPalletAtBayMap " + getPresentPalletAtBayMap());

			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
			ApplicationLauncher.logger.debug("addResultToMeterQueueTask: getActivePalletMap: " + getActivePalletMap());
			ApplicationLauncher.logger
					.debug("addResultToMeterQueueTask: getActivePalletMap: " + getActivePalletMap().values());
			ApplicationLauncher.logger.debug("addResultToMeterQueueTask: myPalletDistinctId: " + myPalletDistinctId);
			boolean isPalletAvailableAtActiveMap = getActivePalletMap().values().contains(myPalletDistinctId);
			boolean palletAvailableInDatabase = false;
			ApplicationLauncher.logger
					.debug("addResultToMeterQueueTask: isPalletAvailableAtActiveMap: " + isPalletAvailableAtActiveMap);
			Optional<PalletManage> myPalletManageOpt = MySqlServiceManager.getPalletManageService()
					.findByPalletDistinctId(myPalletDistinctId);
			if (myPalletManageOpt.isPresent()) {
				palletAvailableInDatabase = true;
			}
			ApplicationLauncher.logger
					.debug("addResultToMeterQueueTask: palletAvailableInDatabase: " + palletAvailableInDatabase);
			// if(getActivePalletMap().values().contains(myPalletDistinctId)){
			if ((isPalletAvailableAtActiveMap) || (palletAvailableInDatabase)) { // some time by mistake user updated
																					// the Pallet as markAsCompleted by
																					// mistake, on that case results
																					// were missing
				ApplicationLauncher.logger
						.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo + "-Hit1");
				// int selectedIndex =
				// ref_tvPalletManage.getSelectionModel().getSelectedIndex();
				// PalletManage palletBayTracker =
				// ref_tvPalletManage.getSelectionModel().getSelectedItem();
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);

				if (myPalletManage != null) {
					ApplicationLauncher.logger
							.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo + "-Hit2");
					String selectedMeterSerialNo = "";// =
					String selectedMeterHardwareIdNo = "";// =
					Optional<PalletMeter> palletMeterOpt = myPalletManage.getPalletMeterList().stream()
							.filter(e -> e.getRackPositionNo() == positionNo)
							.findFirst();

					ApplicationLauncher.logger.debug("addResultToMeterQueueTask: palletMeterOpt " + palletMeterOpt);

					if (palletMeterOpt.isPresent()) {
						ApplicationLauncher.logger
								.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo + "-Hit3");
						PalletMeter selectedPalletMeter = palletMeterOpt.get();
						selectedMeterSerialNo = selectedPalletMeter.getMeterSerialNo();
						ApplicationLauncher.logger.debug("addResultToMeterQueueTask: selectedMeterSerialNo: "
								+ selectedMeterSerialNo + " : Position No: " + positionNo + "-Hit3");
						if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
							selectedMeterHardwareIdNo = selectedPalletMeter.getHardwareId();
						}
						PalletMeterResults palletMeterResult = new PalletMeterResults();
						palletMeterResult.setBayStateKey(myPalletManage.getPresentBayKey());
						palletMeterResult.setSerialNo(getPalletMeterResultsSerialNoAtomic().getAndIncrement());
						palletMeterResult.setPalletBatchNo(myPalletManage.getPalletBatchNo());
						palletMeterResult.setPalletDistinctId(myPalletManage.getPalletDistinctId());
						palletMeterResult.setPalletQrId(myPalletManage.getPalletQrId());
						palletMeterResult.setMeterSerialNo(selectedMeterSerialNo);
						if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
							palletMeterResult.setMeterHardwareId(selectedMeterHardwareIdNo);
						}
						palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
						palletMeterResult.setResultValue(resultValue);
						palletMeterResult.setTestType(testType);
						palletMeterResult.setTestCaseName(testCaseName);
						palletMeterResult.setRackPositionNo(positionNo);
						if (ConstantConveyor.TEST_NAME_SUMMARY_LIST.contains(testCaseName)) {
							palletMeterResult.setResultSummary(true);
							ApplicationLauncher.logger
									.debug("addResultToMeterQueueTask: setting summary to true: testCaseName: "
											+ testCaseName);
						}

						Optional<PalletMeterResults> palletMeterResultsOpt = palletMeterOpt.get()
								.getPalletMeterResultsList()
								.stream()
								.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
								.filter(e -> e.getRackPositionNo() == positionNo)
								.filter(e -> e.getTestType().equals(testType))
								.filter(e -> e.getTestCaseName().equals(testCaseName))
								.findFirst();
						if (palletMeterResultsOpt.isPresent()) {
							ApplicationLauncher.logger
									.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo + "-Hit4");
							ApplicationLauncher.logger.debug(
									"addResultToMeterQueueTask: db already exist " + " : Position No: " + positionNo);
							palletMeterResult = palletMeterResultsOpt.get();
							palletMeterResult.setResultStatus(resultStatus);// ConstantReport.REPORT_POPULATE_PASS);
							palletMeterResult.setResultValue(resultValue);
							// MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);//
							// added approximately on 13th July-2025 - version ProCon-d0.8.6.7
							MySqlServiceManager.getPalletMeterResultsService().save(palletMeterResult);// added on 15th
																										// July-2025 -
																										// version
																										// ProCon-d0.8.7.0
						} else {
							ApplicationLauncher.logger
									.debug("addResultToMeterQueueTask: Entry : Position No: " + positionNo + "-Hit5");
							ApplicationLauncher.logger.debug("addResultToMeterQueueTask: creating new record "
									+ " : Position No: " + positionNo);

							selectedPalletMeter.addPalletMeterResults(palletMeterResult);

							MySqlServiceManager.getPalletMeterService().save(selectedPalletMeter);

						}

						status = true;
					}

				}
			} else {
				ApplicationLauncher.logger
						.debug("addResultToMeterQueueTask: pallet not in active mode " + " : selectedBayTypeKey: "
								+ selectedBayTypeKey + " : myPalletDistinctId : " + myPalletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("addResultToMeterQueueTask: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("addResultToMeterQueueTask: Exit " + " : Position No: " + positionNo);
		return status;
	}

	private void markAsCompletedForPallet(PalletManage palletBayTracker) {
		palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
		palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
		palletBayTracker.setPalletActive(false);

		String palletDistinctId = palletBayTracker.getPalletDistinctId();
		ApplicationLauncher.logger
				.debug("markAsCompletedOnClick: getPalletDistinctId::" + palletBayTracker.getPalletDistinctId());

		ApplicationLauncher.logger.debug("markAsCompletedOnClick: getActivePalletMap: " + getActivePalletMap());
		// getActivePalletMap().remove(palletDistinctId); // (palletQrId,
		// palletDistinctId);
		String palletKey = extractPalletKey(palletDistinctId); // Extract the correct key
		ApplicationLauncher.logger.debug("markAsCompletedOnClick: palletKey: " + palletKey);

		if (getActivePalletMap().containsKey(palletKey)) {
			if (getActivePalletMap().get(palletKey).equals(palletDistinctId)) {
				getActivePalletMap().remove(palletKey);
				ApplicationLauncher.logger
						.debug("markAsCompletedOnClick: removed from getActivePalletMap: palletDistinctId: "
								+ palletDistinctId);

			} else {
				ApplicationLauncher.logger
						.debug("markAsCompletedOnClick: removed failed in getActivePalletMap: pallet-id:" + palletKey
								+ " ,map not equal palletDistinctId: " + palletDistinctId);

			}
		} else {
			ApplicationLauncher.logger.debug(
					"markAsCompletedOnClick: removed failed in getActivePalletMap: pallet-id not exist in active map : "
							+ palletKey);

		}
		// getActivePalletMap().remove(palletKey);
		ApplicationLauncher.logger
				.debug("markAsCompletedOnClick: getActivePalletMap after removal: " + getActivePalletMap());

		palletBayTracker.getPresentBayKey();
	}

	@FXML
	void markAsCompletedOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			markAsCompletedForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	private String extractPalletKey(String palletDistinctId) {

		String returnValue = "";
		if (palletDistinctId != null) {// && palletDistinctId.contains("_Pallet-No-")) {
			returnValue = palletDistinctId.substring(palletDistinctId.lastIndexOf("_") + 1);

			ApplicationLauncher.logger.debug("extractPalletKey: returnValue-1 : " + returnValue);
			return returnValue;
		}
		ApplicationLauncher.logger.debug("extractPalletKey: palletDistinctId-2 : " + palletDistinctId);
		return palletDistinctId;
	}

	private void revertCompletedForPallet(PalletManage palletBayTracker) {
		// palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
		palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
		palletBayTracker.setPalletActive(true);

		String palletDistinctId = palletBayTracker.getPalletDistinctId();
		ApplicationLauncher.logger
				.debug("revertCompletedOnClick: getPalletDistinctId " + palletBayTracker.getPalletDistinctId());

		ApplicationLauncher.logger.debug("revertCompletedOnClick: getActivePalletMap " + getActivePalletMap());
		// getActivePalletMap().remove(palletDistinctId); // (palletQrId,
		// palletDistinctId);
		String palletKey = extractPalletKey(palletDistinctId); // Extract the correct key
		getActivePalletMap().put(palletBayTracker.getPalletQrId(), palletKey);
		ApplicationLauncher.logger.debug("revertCompletedOnClick: getActivePalletMap " + getActivePalletMap());

		palletBayTracker.getPresentBayKey();
	}

	@FXML
	void revertCompletedOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			revertCompletedForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	private void markAsExitAppearedForPallet(PalletManage palletBayTracker) {
		// palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_PASS);
		// palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_COMPLETED);
		// palletBayTracker.setPalletActive(false);
		palletBayTracker.setExitAppeared(true);

		palletBayTracker.getPalletDistinctId();
		ApplicationLauncher.logger
				.debug("markAsExitAppearedOnClick: getPalletDistinctId " + palletBayTracker.getPalletDistinctId());

		palletBayTracker.getPresentBayKey();
	}

	@FXML
	void markAsExitAppearedOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			markAsExitAppearedForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	private void revertExitAppearedForPallet(PalletManage palletBayTracker) {
		palletBayTracker.setExitAppeared(false);

		palletBayTracker.getPalletDistinctId();
		ApplicationLauncher.logger
				.debug("revertExitAppearedOnClick: getPalletDistinctId " + palletBayTracker.getPalletDistinctId());

		palletBayTracker.getPresentBayKey();
	}

	@FXML
	void revertExitAppearedOnClick(ActionEvent event) {
		PalletManage palletBayTracker = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (palletBayTracker != null) {
			revertExitAppearedForPallet(palletBayTracker);
			ref_tvPalletManage.refresh();
		}
	}

	public boolean addMetersToPallet(String selectedBayTypeKey, String inpMeterSerialNo, int positionNo) {

		ApplicationLauncher.logger.debug("addMetersToPallet : ======================: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet: Entry  : Position No: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet: inpMeterSerialNo " + inpMeterSerialNo);
		boolean status = false;
		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
			if (getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);
				ApplicationLauncher.logger.debug("addMetersToPallet: myPalletManage : getPalletDistinctId "
						+ myPalletManage.getPalletDistinctId() + " : Position No: " + positionNo);

				ApplicationLauncher.logger.debug("addMetersToPallet: myPalletManage : Position No: " + positionNo
						+ " inpMeterSerialNo : " + inpMeterSerialNo);

				Optional<PalletMeter> existingMeterOpt = myPalletManage.getPalletMeterList().stream()
						.filter(m -> m.getRackPositionNo() == positionNo)
						.findFirst();

				if (existingMeterOpt.isPresent()) {
					// Overwrite the existing meter at this position
					ApplicationLauncher.logger
							.debug("addMetersToPallet: meter serial No:  exist  : Position No: " + positionNo);
					PalletMeter existingMeter = existingMeterOpt.get();
					myPalletManage.getPalletMeterList().remove(existingMeter);

					if ((inpMeterSerialNo.equals("��������")) || (inpMeterSerialNo.equals(""))) {
						ApplicationLauncher.logger.debug("addMetersToPallet: default1  : Position No: " + positionNo);
						int randomNum = new Random().nextInt(900000) + 100000;
						String generatedSerialNo = "ID" + randomNum;

						ApplicationLauncher.logger.error("Invalid meter serial number: " + inpMeterSerialNo
								+ ", setting to " + generatedSerialNo);
						existingMeter.setMeterSerialNo(generatedSerialNo);
						if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
							ApplicationLauncher.logger.debug(
									"addMetersToPallet: default2  : HARDWARE_ID_FEATURE_ENABLED Hit1: " + positionNo);
						}
					} else {
						existingMeter.setMeterSerialNo(inpMeterSerialNo);
						if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
							String hardwareIdNo = inpMeterSerialNo;
							existingMeter.setHardwareId(hardwareIdNo);
							ApplicationLauncher.logger
									.debug("addMetersToPallet: default2  : HARDWARE_ID_FEATURE_ENABLED else Hit2: "
											+ positionNo);
						}
						// existingMeter.setRackPositionNo(positionNo);
					}
					existingMeter.setRackPositionNo(positionNo);
					existingMeter.setPalletDistinctId(myPalletDistinctId);
					// existingMeter.setErrorCode(ErrorCode.ERR_000);
					existingMeter.setOverAllTestResultStatus(ConstantReport.REPORT_POPULATE_WFR);
					myPalletManage.addPalletMeter(existingMeter);

				} else {
					// Add a new meter
					ApplicationLauncher.logger.debug("addMetersToPallet: new serial no  : Position No: " + positionNo);
					PalletMeter newPalletMeter = new PalletMeter();

					if ((inpMeterSerialNo.equals("��������")) || (inpMeterSerialNo.equals(""))) {
						ApplicationLauncher.logger.debug("addMetersToPallet: default2  : Position No: " + positionNo);
						int randomNum = new Random().nextInt(900000) + 100000;
						String generatedSerialNo = "ID" + randomNum;

						ApplicationLauncher.logger.error("Invalid meter serial number: " + inpMeterSerialNo
								+ ", setting to " + generatedSerialNo);
						newPalletMeter.setMeterSerialNo(generatedSerialNo);
					} else {
						ApplicationLauncher.logger
								.debug("addMetersToPallet: non default - new serial no  : Position No: " + positionNo);
						newPalletMeter.setMeterSerialNo(inpMeterSerialNo);
						if (ProcalFeatureEnable.HARDWARE_ID_FEATURE_ENABLED) {
							String hardwareIdNo = inpMeterSerialNo;
							newPalletMeter.setHardwareId(hardwareIdNo);
							ApplicationLauncher.logger
									.debug("addMetersToPallet: HARDWARE_ID_FEATURE_ENABLED Hit3: " + positionNo);
						}
					}

					newPalletMeter.setPalletDistinctId(myPalletDistinctId);
					newPalletMeter.setRackPositionNo(positionNo);
					// newPalletMeter.setErrorCode(ErrorCode.ERR_000);
					// newPalletMeter.setOverAllTestResultStatus(ConstantReport.REPORT_POPULATE_WFR);
					myPalletManage.addPalletMeter(newPalletMeter);
				}

				int totalPresentMeterCount = myPalletManage.getPalletMeterList().size();

				ApplicationLauncher.logger
						.debug("addMetersToPallet: myPalletManage : getPalletMeterList() : totalPresentMeterCount: "
								+ totalPresentMeterCount);

				myPalletManage.setNoOfMeterPresent(totalPresentMeterCount);

				myPalletManage.getPalleteBayStateList().stream()
						.filter(e -> e.getBayStateKey().equals(selectedBayTypeKey))
						.forEach(e1 -> e1.setNoOfMeterPresent(totalPresentMeterCount));

				MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

				ApplicationLauncher.logger.debug("addMetersToPallet: added/updated Meter serial No: "
						+ inpMeterSerialNo + " : Position No: " + positionNo);

				status = true;
			} else {
				ApplicationLauncher.logger.debug("addMetersToPallet: pallet not in active mode " + " : Position No: "
						+ positionNo + " : myPalletDistinctId : " + myPalletDistinctId);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger
					.error("addMetersToPallet: Exception: Position No: " + positionNo + " : " + e.getMessage());
		}
		// ApplicationLauncher.logger.debug("addMetersToPallet: Exit " + " : Position
		// No: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet : **********************: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet : **********************: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet : **********************: " + positionNo);
		ApplicationLauncher.logger.debug("addMetersToPallet : Exit : position : " + positionNo);
		return status;
	}

	/**
	 * Updates an existing meter's details on a pallet, including its serial number,
	 * overall test result status, and error code.
	 *
	 * @param selectedBayTypeKey         The key identifying the bay type (e.g.,
	 *                                   "BAY_A").
	 * @param inpMeterSerialNo           The new serial number for the meter. If
	 *                                   invalid or empty, a generated ID will be
	 *                                   used.
	 * @param positionNo                 The rack position of the meter to be
	 *                                   updated.
	 * @param newOverallTestResultStatus The new overall test result status for the
	 *                                   meter.
	 * @param newErrorCode               The new error code for the meter.
	 * @return true if the meter was successfully updated, false otherwise.
	 */
	public PalletMeter updateMetersToPallet(String selectedBayTypeKey, int positionNo,
			String newOverallTestResultStatus, String newErrorCode) {
		ApplicationLauncher.logger.debug("updateMetersToPallet: Entry : Position No: " + positionNo);
		ApplicationLauncher.logger
				.debug("updateMetersToPallet: newOverallTestResultStatus " + newOverallTestResultStatus);
		ApplicationLauncher.logger.debug("updateMetersToPallet: newErrorCode " + newErrorCode);
		ApplicationLauncher.logger.debug("updateMetersToPallet: selectedBayTypeKey : " + selectedBayTypeKey);
		ApplicationLauncher.logger
				.debug("updateMetersToPallet: getPresentPalletAtBayMap(): " + getPresentPalletAtBayMap());
		ApplicationLauncher.logger.debug("updateMetersToPallet: getActivePalletMap(): " + getActivePalletMap());
		PalletMeter responsePalletMeter = null;
		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(selectedBayTypeKey);
			ApplicationLauncher.logger.debug("updateMetersToPallet: myPalletDistinctId: " + myPalletDistinctId);
			if (myPalletDistinctId != null) {
				if (getActivePalletMap().values().contains(myPalletDistinctId)) {
					PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
							.findFirstByPalletDistinctId(myPalletDistinctId);

					if (myPalletManage == null) {
						ApplicationLauncher.logger.error(
								"updateMetersToPallet: PalletManage not found for distinct ID: " + myPalletDistinctId);
						return responsePalletMeter;
					}

					ApplicationLauncher.logger.debug("updateMetersToPallet: myPalletManage : getPalletDistinctId "
							+ myPalletManage.getPalletDistinctId() + " : Position No: " + positionNo);

					Optional<PalletMeter> existingMeterOpt = myPalletManage.getPalletMeterList().stream()
							.filter(m -> m.getRackPositionNo() == positionNo)
							.findFirst();

					if (existingMeterOpt.isPresent()) {
						responsePalletMeter = existingMeterOpt.get();

						responsePalletMeter.setOverAllTestResultStatus(newOverallTestResultStatus);
						responsePalletMeter.setErrorCode(newErrorCode); // Set the new error code
						ApplicationLauncher.logger.debug("updateMetersToPallet: position: " + positionNo
								+ " ,getErrorCode: " + responsePalletMeter.getErrorCode());
						ApplicationLauncher.logger.debug("updateMetersToPallet: position: " + positionNo
								+ " ,getMeterSerialNo " + responsePalletMeter.getMeterSerialNo());
						ApplicationLauncher.logger.debug("updateMetersToPallet: position: " + positionNo
								+ " ,getHardwareId " + responsePalletMeter.getHardwareId());

						MySqlServiceManager.getPalletMeterService().save(responsePalletMeter);

						ApplicationLauncher.logger.debug("updateMetersToPallet: updated Meter serial No: "
								+ responsePalletMeter.getMeterSerialNo() + " : Position No: " + positionNo
								+ " with Error Code: " + newErrorCode);

					} else {
						ApplicationLauncher.logger.debug("updateMetersToPallet: No meter found at position: "
								+ positionNo + " for pallet: " + myPalletDistinctId + ". Cannot update.");
					}
				} else {
					ApplicationLauncher.logger.debug("updateMetersToPallet: pallet not in active mode "
							+ " : Position No: " + positionNo + " : myPalletDistinctId : " + myPalletDistinctId);
				}
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("updateMetersToPallet: Exception: " + e.getMessage(), e); // Log the full
																										// stack trace
		}
		ApplicationLauncher.logger.debug("updateMetersToPallet: Exit " + " : Position No: " + positionNo);
		return responsePalletMeter;
	}

	public String addNewPalletManage(String selectedBayTypeKey, String palletQrId,
			Map<Integer, String> meterListWithSerialNoMap) {
		ApplicationLauncher.logger.debug("addNewPalletManage: Entry ");
		String palletDistinctId = "Unknown";
		PalletManage palletBayTracker = new PalletManage();

		palletBayTracker.setPresentBayKey(selectedBayTypeKey);
		palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
		palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_WFR);
		// ApplicationLauncher.logger.debug("addNewPalletManage: Hit1 " );

		int palletBatchNo = 1;
		try {
			if (ref_tvPalletManage != null && ref_tvPalletManage.getItems() != null) {
				OptionalInt maxExistingPalletBatchNo = ref_tvPalletManage.getItems().stream()
						.mapToInt(e -> e.getPalletBatchNo())
						.max();
				if (maxExistingPalletBatchNo.isPresent()) {
					palletBatchNo = maxExistingPalletBatchNo.getAsInt() + 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("addNewPalletManage: palletBatchNo : Exception: " + e.getMessage());
		}

		// UPDATED LOGIC TO RESET BATCH NUMBER EVERYDAY
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String presentDate = LocalDateTime.now().format(formatter);
		ApplicationLauncher.logger.debug("addNewPalletManage : presentDate : " + presentDate);

		List<PalletManage> todayPalletList = MySqlServiceManager.getPalletManageService()
				.findByPalletConvEntryDateH(presentDate);
		if (todayPalletList.isEmpty()) {
			palletBatchNo = 1;
			ApplicationLauncher.logger.debug("addNewPalletManage : presentDate : No Pallet in Present Date");
			ApplicationLauncher.logger.debug("addNewPalletManage : presentDate : Reseting Batch Number");
		} else {
			int maxRetries = 3;
			int attempts = 0;
			boolean success = false;

			while (attempts < maxRetries && !success) {
				try {
					OptionalInt maxExistingPalletBatchNo = todayPalletList.stream()
							.mapToInt(e -> e.getPalletBatchNo())
							.max();
					if (maxExistingPalletBatchNo.isPresent()) {
						palletBatchNo = maxExistingPalletBatchNo.getAsInt() + 1;
					}
					success = true; // If no exception, mark as success
				} catch (Exception e) {
					attempts++;
					e.printStackTrace();
					ApplicationLauncher.logger
							.error("addNewPalletManage: palletBatchNo : Exception: " + e.getMessage());

					if (attempts >= maxRetries) {
						ApplicationLauncher.logger.error("Failed after " + attempts + " attempts.");
					}
				}
			}
		}

		// ApplicationLauncher.logger.debug("addNewPalletManage: Hit2 " );
		palletBayTracker.setPalletBatchNo(palletBatchNo);
		// String palletQrId = txtPalletQrId.getText()
		// ;//"Pallet-No-0"+String.valueOf(palletBatchNo)+"5";
		palletBayTracker.setPalletQrId(palletQrId);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTime = LocalDateTime.now();
		ApplicationLauncher.logger.debug("addNewPalletManage : Start Time: " + dtf.format(startTime));
		String conveyorPalletEntryTime = dtf.format(startTime);
		ZoneId zoneId = ZoneId.systemDefault();
		long startTimeEpoch = startTime.atZone(zoneId).toInstant().toEpochMilli();
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");// + "T"
																					// +DateTimeFormatter.ofPattern("yyyyMMdd
																					// HHmmss");
		DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMdd");// + "T" +DateTimeFormatter.ofPattern("yyyyMMdd
																		// HHmmss");

		// ApplicationLauncher.logger.debug("addNewPalletManage: Hit3 " );
		palletDistinctId = dtf2.format(startTime) + "_" + String.format("%04d", palletBatchNo) + "_" + palletQrId;//// String.valueOf(startTimeEpoch);
		palletBayTracker.setPalletDistinctId(palletDistinctId);
		palletBayTracker.setPalletConvEntryTimeStampH(conveyorPalletEntryTime);
		palletBayTracker.setPalletConvEntryDateH(df2.format(startTime));
		palletBayTracker.setPalletConvEntryTimeEpoch(String.valueOf(startTimeEpoch));
		// ApplicationLauncher.logger.debug("addNewPalletManage: Hit4 " );
		// ArrayList<PalletMeter> meterSerialNoList = new ArrayList<PalletMeter>();
		Set<PalletMeter> meterSerialNoList = new HashSet<PalletMeter>();
		// for(int i=0; i< ref_lvMeterList.getItems().size();i++) {
		String palletDistinctIdFinal = palletDistinctId;
		meterListWithSerialNoMap.entrySet().stream().forEach(e -> {
			PalletMeter palletMeter = new PalletMeter();
			palletMeter.setMeterSerialNo(e.getValue());
			palletMeter.setRackPositionNo(e.getKey());
			palletMeter.setPalletDistinctId(palletDistinctIdFinal);
			meterSerialNoList.add(palletMeter);
		});

		// }

		palletBayTracker.setPalletMeterList(meterSerialNoList); // GopiConveyorReport
		palletBayTracker.setNoOfMeterPresent(meterSerialNoList.size());
		MySqlServiceManager.getPalletManageService().saveToDb(palletBayTracker);

		if (ref_tvPalletManage != null && ref_tvPalletManage.getItems() != null) {
			javafx.application.Platform.runLater(() -> {
				ref_tvPalletManage.getItems().add(palletBayTracker);
				reOrderedPalletManageSerialNo();
			});
		}
		getActivePalletMap().put(palletQrId, palletDistinctId);
		getPresentPalletAtBayMap().put(selectedBayTypeKey, palletDistinctId);
		addPalletBayState(selectedBayTypeKey);
		ApplicationLauncher.logger.debug("addNewPalletManage: Exit ");
		return palletDistinctId;

	}

	@FXML
	void addPalletManageOnClick() {

		String selectedBayTypeDisplayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		String selectedBayTypeKey = ConstantConveyor.getBayLookup().get(selectedBayTypeDisplayName);
		PalletManage palletBayTracker = new PalletManage();

		palletBayTracker.setPresentBayKey(selectedBayTypeKey);

		palletBayTracker.setPalletExecutionStatus(ConstantConveyor.EXECUTION_STATUS_INPROGRESS);
		palletBayTracker.setPalletResultStatus(ConstantReport.REPORT_POPULATE_WFR);

		int palletBatchNo = 1;
		try {
			OptionalInt maxExistingPalletBatchNo = ref_tvPalletManage.getItems().stream()
					.mapToInt(e -> e.getPalletBatchNo())
					.max();
			if (maxExistingPalletBatchNo.isPresent()) {
				palletBatchNo = maxExistingPalletBatchNo.getAsInt() + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("addPalletManageOnClick: palletBatchNo : Exception: " + e.getMessage());
		}
		palletBayTracker.setPalletBatchNo(palletBatchNo);
		String palletQrId = txtPalletQrId.getText();// "Pallet-No-0"+String.valueOf(palletBatchNo)+"5";
		palletBayTracker.setPalletQrId(palletQrId);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startTime = LocalDateTime.now();
		ApplicationLauncher.logger.debug("addPalletManageOnClick : Start Time: " + dtf.format(startTime));
		String conveyorPalletEntryTime = dtf.format(startTime);
		ZoneId zoneId = ZoneId.systemDefault();
		long startTimeEpoch = startTime.atZone(zoneId).toInstant().toEpochMilli();
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");// + "T"

		String palletDistinctId = dtf2.format(startTime) + "_" + String.format("%04d", palletBatchNo) + "_"
				+ palletQrId;//// String.valueOf(startTimeEpoch);

		palletBayTracker.setPalletDistinctId(palletDistinctId);
		palletBayTracker.setPalletConvEntryTimeStampH(conveyorPalletEntryTime);
		palletBayTracker.setPalletConvEntryTimeEpoch(String.valueOf(startTimeEpoch));
		// ArrayList<PalletMeter> meterSerialNoList = new ArrayList<PalletMeter>();
		Map<Integer, String> meterListWithSerialNoMap = new HashMap<Integer, String>();
		Set<PalletMeter> meterSerialNoList = new HashSet<PalletMeter>();
		for (int i = 0; i < ref_lvMeterList.getItems().size(); i++) {
			PalletMeter palletMeter = new PalletMeter();
			palletMeter.setMeterSerialNo(ref_lvMeterList.getItems().get(i));
			palletMeter.setRackPositionNo(i + 1);
			palletMeter.setPalletDistinctId(palletDistinctId);
			meterSerialNoList.add(palletMeter);
			meterListWithSerialNoMap.put(i + 1, ref_lvMeterList.getItems().get(i));
		}

		palletBayTracker.setPalletMeterList(meterSerialNoList); // GopiConveyorReport
		palletBayTracker.setNoOfMeterPresent(meterSerialNoList.size());
		ref_tvPalletManage.getItems().add(palletBayTracker);
		reOrderedPalletManageSerialNo();
		getActivePalletMap().put(palletQrId, palletDistinctId);

		ConveyorDataManager.getDashboardObject().addNewPalletViewDashboard(selectedBayTypeKey, palletQrId,
				meterListWithSerialNoMap);

		Map<Integer, MeterStatus> statusMap = new HashMap<>();
		statusMap.put(1, MeterStatus.PASSED);
		statusMap.put(2, MeterStatus.FAILED);
		statusMap.put(3, MeterStatus.PASSED);
		statusMap.put(4, MeterStatus.PASSED);
		statusMap.put(5, MeterStatus.FAILED);
		statusMap.put(6, MeterStatus.PASSED);

		Map<Integer, String> errorCodeMap = new HashMap<>();
		errorCodeMap.put(1, "ERR-001"); // Err-Qr //Err-Com //Err-Kwh //Err-ROn //Err-
		errorCodeMap.put(2, "ERR-002");
		errorCodeMap.put(3, "ERR-003");
		errorCodeMap.put(4, "ERR-004");
		errorCodeMap.put(5, "ERR-005");
		errorCodeMap.put(6, "ERR-006");

		ConveyorDataManager.getDashboardObject().updateDashBoardPalletStatus("Pallet-No-008", statusMap, errorCodeMap);
	}

	@FXML
	public void addMeterOnClick() {
		if (!ref_txtMeterSerialNo.getText().isEmpty()) {
			ref_lvMeterList.getItems().add(ref_txtMeterSerialNo.getText());
			// ref_txtPalletQrId;
		}
		;
	}

	@FXML
	public void removeDeviceOnClick(ActionEvent event) {
	}

	@FXML
	public void clearMeterListOnClick(ActionEvent event) {

		ref_lvMeterList.getItems().clear();
	}

	@FXML
	public void saveOnClick(ActionEvent event) {

		PalletManage selectedItem = ref_tvPalletManage.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			WindowManager.InformUser("Items not selected", "Kindly select an item to save", AlertType.INFORMATION);

		} else {
			MySqlServiceManager.getPalletManageService().saveToDb(selectedItem);
			if (ref_tvPalletManage.getItems().size() > 0) {
				WindowManager.InformUser("Saved", "Pallet bay test saved successfully", AlertType.INFORMATION);

			}
		}

		// refreshPalletManageDataFromDb();
		refreshPalletManageDataFromDbv2("PalletTracker-saveOnClick");
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public String getDeviceTypeKeyPrefix() {
		return deviceTypeKeyPrefix;
	}

	public void setDeviceTypeKeyPrefix(String deviceTypePrefix) {
		this.deviceTypeKeyPrefix = deviceTypePrefix;
	}

	public String getDeviceModelName() {
		return deviceModelName;
	}

	public String getDeviceDefaultBaudRate() {
		return deviceDefaultBaudRate;
	}

	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}

	public void setDeviceDefaultBaudRate(String deviceDefaultBaudRate) {
		this.deviceDefaultBaudRate = deviceDefaultBaudRate;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public static AtomicInteger getPalletManageSerialNoAtomic() {
		return palletManageSerialNoAtomic;
	}

	public AtomicInteger getPalletBayStateSerialNoAtomic() {
		return palletBayStateSerialNoAtomic;
	}

	public void setPalletManageSerialNoAtomic(AtomicInteger palletManageSerialNoAtomic) {
		PalletTrackerController.palletManageSerialNoAtomic = palletManageSerialNoAtomic;
	}

	public void setPalletBayStateSerialNoAtomic(AtomicInteger palletBayStateSerialNoAtomic) {
		this.palletBayStateSerialNoAtomic = palletBayStateSerialNoAtomic;
	}

	public AtomicInteger getPalletMeterResultsSerialNoAtomic() {
		return palletMeterResultsSerialNoAtomic;
	}

	public void setPalletMeterResultsSerialNoAtomic(AtomicInteger palletMeterResultsSerialNoAtomic) {
		this.palletMeterResultsSerialNoAtomic = palletMeterResultsSerialNoAtomic;
	}

	public static Map<String, String> getActivePalletMap() {
		return activePalletMap;
	}

	public static void setActivePalletMap(Map<String, String> activePalletMap) {
		PalletTrackerController.activePalletMap = activePalletMap;
	}

	public static AtomicInteger getPalletMeterSerialNoAtomic() {
		return palletMeterSerialNoAtomic;
	}

	public static void setPalletMeterSerialNoAtomic(AtomicInteger palletMeterSerialNoAtomic) {
		PalletTrackerController.palletMeterSerialNoAtomic = palletMeterSerialNoAtomic;
	}

	public static Map<String, String> getPresentPalletAtBayMap() {
		return presentPalletAtBayMap;
	}

	public static void setPresentPalletAtBayMap(Map<String, String> presentPalletAtBayMap) {
		PalletTrackerController.presentPalletAtBayMap = presentPalletAtBayMap;
	}

	public static AtomicInteger getMeterResultSummarySerialNoAtomic() {
		return meterResultSummarySerialNoAtomic;
	}

	public static void setMeterResultSummarySerialNoAtomic(AtomicInteger meterResultSummarySerialNoAtomic) {
		PalletTrackerController.meterResultSummarySerialNoAtomic = meterResultSummarySerialNoAtomic;
	}

	public String getResultStyleWfr() {
		return resultStyleWfr;
	}

	public String getResultStylePass() {
		return resultStylePass;
	}

	public String getResultStyleFail() {
		return resultStyleFail;
	}

	public String getResultStyleUndefined() {
		return resultStyleUndefined;
	}

	public String getResultStyleDefault() {
		return resultStyleDefault;
	}

	public void setResultStyleWfr(String resultStyleWfr) {
		this.resultStyleWfr = resultStyleWfr;
	}

	public void setResultStylePass(String resultStylePass) {
		this.resultStylePass = resultStylePass;
	}

	public void setResultStyleFail(String resultStyleFail) {
		this.resultStyleFail = resultStyleFail;
	}

	public void setResultStyleUndefined(String resultStyleUndefined) {
		this.resultStyleUndefined = resultStyleUndefined;
	}

	public void setResultStyleDefault(String resultStyleDefault) {
		this.resultStyleDefault = resultStyleDefault;
	}

	public void exitBatchFromPresentBay(String currentBay) {
		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(currentBay);
			if (myPalletDistinctId != null && getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);
				if (myPalletManage.isPalletActive()) {
					updatePalletBayState(myPalletManage, currentBay);
					MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);
				}
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("exitBatchFromPresentBay: Exception: " + e.getMessage());
		}
	}

	public void enterBatchToNextBay(String currentBay, String nextBayState) {
		try {
			String myPalletDistinctId = getPresentPalletAtBayMap().get(currentBay);
			if (myPalletDistinctId != null && getActivePalletMap().values().contains(myPalletDistinctId)) {
				PalletManage myPalletManage = MySqlServiceManager.getPalletManageService()
						.findFirstByPalletDistinctId(myPalletDistinctId);
				if (myPalletManage.isPalletActive()) {
					myPalletManage.setPresentBayKey(nextBayState);
					getPresentPalletAtBayMap().put(nextBayState, myPalletDistinctId);
					myPalletManage.setNoOfMeterPassed(0);
					myPalletManage.setNoOfMeterFailed(0);
					MySqlServiceManager.getPalletManageService().saveToDb(myPalletManage);

					if (!(currentBay.equals(ConstantConveyor.STA_NLD1_BAY_KEY)) &&
							!(currentBay.equals(ConstantConveyor.STA_NLD2_BAY_KEY))) {
						addPalletNextBayState(nextBayState);
					}
				}
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("enterBatchToNextBay: Exception: " + e.getMessage());
		}
	}

}
