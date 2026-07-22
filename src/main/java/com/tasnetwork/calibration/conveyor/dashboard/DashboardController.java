package com.tasnetwork.calibration.conveyor.dashboard;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.stream.Collectors;

import org.json.JSONException;

import com.tasnetwork.calibration.conveyor.bay.BayStateEngine;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayStop;
import com.tasnetwork.calibration.conveyor.bay.comm.Comm;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.StaNld_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitingBayStop;
import com.tasnetwork.calibration.conveyor.constant.ConstantBypassFlags;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayBypass;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Bypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Bypass;
import com.tasnetwork.calibration.conveyor.bay.comm.CommBayBypass;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsService;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsSummaryService;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup; // Import Popup
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * Controller for the conveyor dashboard, managing pallet views, bay updates,
 * and error code history.
 * Updated for Java 8 compatibility by using Runnable instead of lambdas,
 * explicit casting instead of
 * instanceof pattern matching, and ensuring proper variable scoping for
 * subBayKey.
 * Error code history functionality merged to retain table management and daily
 * reset, using Java 8-compatible Runnable.
 */
public class DashboardController implements Initializable {

	private String palletViewFxmlFileName = "DashboardPalletView_v1_7";
	private String bayViewFxmlFileName = "DashboardBayView_v1_3";

	private BayIndicatorManager bayIndicatorManager;

	// private final Set<String> reservedVerificBayKeys =
	// ConcurrentHashMap.newKeySet();
	public static volatile boolean verific1BayLocked = false;
	public static volatile boolean sta1BayLocked = false;
	public static volatile boolean sta2BayLocked = false;

	// private final Object sta1BayLock = new Object();

	@FXML
	private TextArea eventLog;
	@FXML
	private TextField palletNameField;
	@FXML
	private AnchorPane pallet1Container; // Functional Test Bay
	@FXML
	private AnchorPane pallet2Container; // High Voltage Bay
	@FXML
	private AnchorPane pallet3Container; // Insulation Resistance Bay
	@FXML
	private AnchorPane pallet4Container; // Calibration Bay
	@FXML
	private AnchorPane pallet5Container; // Waiting PP4
	@FXML
	private AnchorPane pallet6Container; // Waiting PP3
	@FXML
	private AnchorPane pallet7Container; // Waiting PP2
	@FXML
	private AnchorPane pallet8Container; // Waiting PP1
	@FXML
	private AnchorPane pallet9Container; // Verification PP4
	@FXML
	private AnchorPane pallet10Container; // Verification PP3
	@FXML
	private AnchorPane pallet11Container; // Verification PP2
	@FXML
	private AnchorPane pallet12Container; // Verification PP1
	@FXML
	private AnchorPane pallet13Container; // STA_NLD1
	@FXML
	private AnchorPane pallet14Container; // STA_NLD1
	@FXML
	private AnchorPane pallet15Container; // STA_NLD1
	@FXML
	private AnchorPane pallet16Container; // STA_NLD1
	@FXML
	private AnchorPane pallet17Container; // STA_NLD2
	@FXML
	private AnchorPane pallet18Container; // STA_NLD2
	@FXML
	private AnchorPane pallet19Container; // STA_NLD2
	@FXML
	private AnchorPane pallet20Container; // STA_NLD2
	@FXML
	private AnchorPane rejectionPalletContainer;
	@FXML
	private AnchorPane unloadingPalletContainer;

	// Static AnchorPane variables
	public static AnchorPane ref_pallet1Container;
	public static AnchorPane ref_pallet2Container;
	public static AnchorPane ref_pallet3Container;
	public static AnchorPane ref_pallet4Container;
	public static AnchorPane ref_pallet5Container;
	public static AnchorPane ref_pallet6Container;
	public static AnchorPane ref_pallet7Container;
	public static AnchorPane ref_pallet8Container;
	public static AnchorPane ref_pallet9Container;
	public static AnchorPane ref_pallet10Container;
	public static AnchorPane ref_pallet11Container;
	public static AnchorPane ref_pallet12Container;
	public static AnchorPane ref_pallet13Container;
	public static AnchorPane ref_pallet14Container;
	public static AnchorPane ref_pallet15Container;
	public static AnchorPane ref_pallet16Container;
	public static AnchorPane ref_pallet17Container;
	public static AnchorPane ref_pallet18Container;
	public static AnchorPane ref_pallet19Container;
	public static AnchorPane ref_pallet20Container;
	public static AnchorPane ref_rejectionPalletContainer;
	public static AnchorPane ref_unloadingPalletContainer;

	@FXML
	private AnchorPane bay1Container; // Functional Test Bay
	@FXML
	private AnchorPane bay2Container; // High Voltage Bay
	@FXML
	private AnchorPane bay3Container; // Insulation Resistance Bay
	@FXML
	private AnchorPane bay4Container; // Calibration Bay
	@FXML
	private AnchorPane bay5Container; // Waiting PP4
	@FXML
	private AnchorPane bay6Container; // Waiting PP3
	@FXML
	private AnchorPane bay7Container; // Waiting PP2
	@FXML
	private AnchorPane bay8Container; // Waiting PP1
	@FXML
	private AnchorPane bay9Container; // Verification PP4
	@FXML
	private AnchorPane bay10Container; // Verification PP3
	@FXML
	private AnchorPane bay11Container; // Verification PP2
	@FXML
	private AnchorPane bay12Container; // Verification PP1
	@FXML
	private AnchorPane bay13Container; // STA_NLD1
	@FXML
	private AnchorPane bay14Container; // STA_NLD1
	@FXML
	private AnchorPane bay15Container; // STA_NLD1
	@FXML
	private AnchorPane bay16Container; // STA_NLD1
	@FXML
	private AnchorPane bay17Container; // STA_NLD2
	@FXML
	private AnchorPane bay18Container; // STA_NLD2
	@FXML
	private AnchorPane bay19Container; // STA_NLD2
	@FXML
	private AnchorPane bay20Container; // STA_NLD2
	@FXML
	private AnchorPane rejectionBayContainer;
	@FXML
	private AnchorPane unloadingBayContainer;

	// Static AnchorPane variables
	public static AnchorPane ref_bay1Container;
	public static AnchorPane ref_bay2Container;
	public static AnchorPane ref_bay3Container;
	public static AnchorPane ref_bay4Container;
	public static AnchorPane ref_bay5Container;
	public static AnchorPane ref_bay6Container;
	public static AnchorPane ref_bay7Container;
	public static AnchorPane ref_bay8Container;
	public static AnchorPane ref_bay9Container;
	public static AnchorPane ref_bay10Container;
	public static AnchorPane ref_bay11Container;
	public static AnchorPane ref_bay12Container;
	public static AnchorPane ref_bay13Container;
	public static AnchorPane ref_bay14Container;
	public static AnchorPane ref_bay15Container;
	public static AnchorPane ref_bay16Container;
	public static AnchorPane ref_bay17Container;
	public static AnchorPane ref_bay18Container;
	public static AnchorPane ref_bay19Container;
	public static AnchorPane ref_bay20Container;
	public static AnchorPane ref_rejectionBayContainer;
	public static AnchorPane ref_unloadingBayContainer;

	// private final Map<String, BayStatusManager> bayStatusManagers = new
	// HashMap<>();

	@FXML
	private TableView<ErrorCodeEntry> tv_FTErrorCodeHistory;
	@FXML
	private TableColumn<ErrorCodeEntry, String> tc_FtHistoryCodeColumn;
	@FXML
	private TableColumn<ErrorCodeEntry, Number> tc_FtHistoryCountColumn;

	@FXML
	private StackPane motorControlTabContainer; // Inject the new StackPane
	@FXML
	private StackPane mainControlTabContainer; // Inject the new StackPane
	@FXML
	private StackPane launcherTabContainer; // Inject the new StackPane

	private static TextArea ref_eventLog;
	private static boolean conveyorDebugGUILoaded = false;
	private final List<AnchorPane> allContainers = new ArrayList<>();
	private final Map<String, AnchorPane> bayKeyToPalletContainer = new HashMap<>();
	private final Map<String, AnchorPane> bayKeyToBayContainer = new HashMap<>();
	// For Motor Control Popup
	private Popup motorControlPopup;
	private Node motorControlContent;

	// For Main Control Popup
	private Popup mainControlPopup;
	private Node mainControlContent;

	// For Main Control Popup
	private Popup launcherPopup;
	private Node launcherContent;

	@FXML
	private Button btnAllStart;
	@FXML
	private Button btnAllStop;
	@FXML
	private Button btnDummy;
	Timer funtionalBayStartTaskTimer;
	Timer calibrationStartTaskTimer;
	Timer insResStartTaskTimer;
	Timer hvtBayStartTaskTimer;
	Timer verificStartTaskTimer;
	Timer sctNlt1StartTaskTimer;
	Timer sctNlt2StartTaskTimer;
	Timer waitingBayStartTaskTimer;
	Timer commStartTaskTimer;

	private BayStateEngine activeStaNld1Engine;
	private BayStateEngine activeStaNld2Engine;
	private BayStateEngine activeCommEngine;
	private BayStateEngine activeWaitingEngine;
	private BayStateEngine activeFtEngine;
	private BayStateEngine activeHvEngine;
	private BayStateEngine activeIrEngine;
	private BayStateEngine activeCalibEngine;
	private BayStateEngine activeVerificEngine;

	Timer funtionalBayStopTaskTimer;
	Timer calibrationStopTaskTimer;
	Timer insResStopTaskTimer;
	Timer hvtBayStopTaskTimer;
	Timer verificStopTaskTimer;
	Timer commStopTaskTimer;
	Timer sctNlt2StopTaskTimer;
	Timer sctNlt1StopTaskTimer;
	Timer waitingBayStopTaskTimer;
	Timer rejectionBayStopTaskTimer;

	// METRICS TABLE
	@FXML
	private TextField failedMetersRJ;
	@FXML
	private TextField failedMetersUL;
	@FXML
	private TextField passedMetersRJ;
	@FXML
	private TextField passedMetersUL;
	@FXML
	private TextField throughputRJ;
	@FXML
	private TextField throughputUL;
	@FXML
	private TextField totalNoOfMetersRJ;
	@FXML
	private TextField totalNoOfMetersUL;

	@FXML
	private TextField passedMetersUlPercent;
	@FXML
	private TextField failedMetersUlPercent;

	@FXML
	private TextField passedMetersRjPercent;
	@FXML
	private TextField failedMetersRjPercent;

	@FXML
	private ComboBox<String> periodComboBox;

	private ConveyorOutputMetricsSummaryService conveyorOutputMetricsSummaryService;

	public void setConveyorOutputMetricsService(ConveyorOutputMetricsService service) {
	}

	public void setConveyorOutputMetricsSummaryService(ConveyorOutputMetricsSummaryService service) {
		this.conveyorOutputMetricsSummaryService = service;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ref_eventLog = eventLog;
		initializeBayKeyMap();
		bayIndicatorManager = new BayIndicatorManager(bayKeyToBayContainer, bayViewFxmlFileName);
		initializeBayContainer();
		refInit();
		guiInit();

		initializeMotorControlTab(); // Initialize the motor control tab logic
		initializeMainControlTab(); // Initialize the main control tab logic
		initializeLaunchersTab();
		initAllBayView();

		try {
			if (ConstantConveyorConfig.CONVEYOR_DEBUG_SCREEN_DISPLAY_ENABLED && !conveyorDebugGUILoaded) {
				/*
				 * loadConveyorDebugGUI();
				 * loadPalletTrackerGUI();
				 * loadDutExecutor();
				 */
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("initialize: Exception: Failed to load debug GUIs: " + e.getMessage());
		}

		ObservableList<String> options = FXCollections.observableArrayList(
				"Today", "Yesterday", "This Week", "Last Week", "Last 7 Days",
				"This Month", "Last Month", "This Year", "Last Year", "All Time");
		periodComboBox.setItems(options);
		periodComboBox.setValue("Today"); // default

		ConveyorDataManager.setDashboardObject(this);

		refreshInitialConveyorStatus();
	}

	private void refreshInitialConveyorStatus() {
		new Thread(() -> {
			try {
				// Wait slightly for UI to be ready
				Thread.sleep(1000);

				com.tasnetwork.calibration.conveyor.bay.BayUtils bayUtils = new com.tasnetwork.calibration.conveyor.bay.BayUtils();
				BayActionHandler bayActionHandler = new BayActionHandler("");
				BayIndicatorManager indicatorManager = ConveyorDataManager.getDashboardObject()
						.getBayIndicatorManager();

				// Mapping for Fingertip latches
				java.util.Map<String, String> fingertipPorts = new java.util.HashMap<>();
				fingertipPorts.put(ConstantConveyor.FT_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.FT_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.HV_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.HV_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.IR_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.IR_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.CALIBRATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.CALIB_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.VERIFICATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.VERIFIC_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.STA_NLD1_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.STA_NLD2_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.COMMUNICATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.COMM_PORT_NAME_FINGER_TIP);
				fingertipPorts.put(ConstantConveyor.UNLOADING_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.UNLOADING_PORT_NAME_FINGER_TIP);

				// Mapping for Stoppers
				java.util.Map<String, String> stopperPorts = new java.util.HashMap<>();
				stopperPorts.put(ConstantConveyor.FT_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.FT_PORT_NAME_STPR_AT);
				stopperPorts.put(ConstantConveyor.HV_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.HV_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.IR_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.IR_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.CALIBRATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.CALIB_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.VERIFICATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.VERIFIC_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.WAITING_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.WAITING_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.STA_NLD1_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.SCT_NLT1_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.STA_NLD2_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.SCT_NLT2_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.COMMUNICATION_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.COMM_PORT_NAME_STPR);
				stopperPorts.put(ConstantConveyor.UNLOADING_BAY_KEY,
						com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.UNLOADING_PORT_NAME_STPR);

				for (String bayKey : stopperPorts.keySet()) {
					ApplicationLauncher.logger.info("refreshInitialConveyorStatus: Processing bayKey: " + bayKey);

					// 1. Fetch Latch (FINGER_TIP) State from OUTPUT
					String fingertipPort = fingertipPorts.get(bayKey);
					if (fingertipPort != null) {
						com.tasnetwork.calibration.conveyor.bay.IoPortInfo fingertipPortInfo = com.tasnetwork.calibration.conveyor.bay.BayUtils
								.getOutputPortDetails(fingertipPort);
						if (fingertipPortInfo != null) {
							String state = bayUtils.getInputDataFromPlcBayV2(fingertipPortInfo);
							boolean isOpen = com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping.OFF
									.equalsIgnoreCase(state);
							ApplicationLauncher.logger.info("refreshInitialConveyorStatus [FINGERTIP] - bayKey: "
									+ bayKey
									+ " | port: " + fingertipPort + " | state: " + state + " | isOpen: " + isOpen);
							javafx.application.Platform.runLater(() -> {
								indicatorManager.setPalletsLockedImageDisplayOn(bayKey, isOpen);
							});
						} else {
							ApplicationLauncher.logger
									.warn("refreshInitialConveyorStatus [FINGERTIP] - Port info null for port: "
											+ fingertipPort);
						}
					}

					// 2. Fetch Stopper State from OUTPUT
					String stopperPort = stopperPorts.get(bayKey);
					if (stopperPort != null) {
						com.tasnetwork.calibration.conveyor.bay.IoPortInfo stopperPortInfo = com.tasnetwork.calibration.conveyor.bay.BayUtils
								.getOutputPortDetails(stopperPort);
						if (stopperPortInfo != null) {
							String state = bayUtils.getInputDataFromPlcBayV2(stopperPortInfo);
							boolean isOpen = com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping.OFF
									.equalsIgnoreCase(state);
							ApplicationLauncher.logger.info("refreshInitialConveyorStatus [STOPPER] - bayKey: " + bayKey
									+ " | port: " + stopperPort + " | state: " + state + " | isOpen: " + isOpen);
							javafx.application.Platform.runLater(() -> {
								indicatorManager.updateBayExitStopper(bayKey, !isOpen);
							});
						} else {
							ApplicationLauncher.logger.warn(
									"refreshInitialConveyorStatus [STOPPER] - Port info null for port: " + stopperPort);
						}
					}

					// Fetch Entry Stopper for FT Bay explicitly
					if (ConstantConveyor.FT_BAY_KEY.equals(bayKey)) {
						com.tasnetwork.calibration.conveyor.bay.IoPortInfo entryStopperPortInfo = com.tasnetwork.calibration.conveyor.bay.BayUtils
								.getOutputPortDetails(com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4);
						if (entryStopperPortInfo != null) {
							String state = bayUtils.getInputDataFromPlcBayV2(entryStopperPortInfo);
							boolean isOpen = com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping.OFF
									.equalsIgnoreCase(state);
							ApplicationLauncher.logger.info("refreshInitialConveyorStatus [ENTRY STOPPER] - bayKey: " + bayKey
									+ " | port: " + com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping.FT_PORT_NAME_STPR_B4 + " | state: " + state + " | isOpen: " + isOpen);
							javafx.application.Platform.runLater(() -> {
								indicatorManager.updateBayEntryStopper(bayKey, !isOpen);
							});
						}
					}
				}

				// 3. Trigger Pallet Refreshes
				if (bayActionHandler != null) {
					bayActionHandler.refreshFtBay();
					bayActionHandler.refreshHvBay();
					bayActionHandler.refreshIrBay();
					bayActionHandler.refreshCalibBay();
					bayActionHandler.refreshVerific1Bay();
					bayActionHandler.refreshWaitingVerific1Bay();
					bayActionHandler.refreshSta1Bay();
					bayActionHandler.refreshSta2Bay();
					bayActionHandler.refreshRejectionBay();
					bayActionHandler.refreshUnloadingBay();
				}

			} catch (Exception e) {
				ApplicationLauncher.logger.error("Error fetching initial conveyor status: " + e.getMessage(), e);
			}
		}).start();
	}

	/**
	 * Assign static reference variables
	 */
	private void refInit() {
		ref_pallet1Container = pallet1Container;
		ref_pallet2Container = pallet2Container;
		ref_pallet3Container = pallet3Container;
		ref_pallet4Container = pallet4Container;
		ref_pallet5Container = pallet5Container;
		ref_pallet6Container = pallet6Container;
		ref_pallet7Container = pallet7Container;
		ref_pallet8Container = pallet8Container;
		ref_pallet9Container = pallet9Container;
		ref_pallet10Container = pallet10Container;
		ref_pallet11Container = pallet11Container;
		ref_pallet12Container = pallet12Container;
		ref_pallet13Container = pallet13Container;
		ref_pallet14Container = pallet14Container;
		ref_pallet15Container = pallet15Container;
		ref_pallet16Container = pallet16Container;
		ref_pallet17Container = pallet17Container;
		ref_pallet18Container = pallet18Container;
		ref_pallet19Container = pallet19Container;
		ref_pallet20Container = pallet20Container;
		ref_rejectionPalletContainer = rejectionPalletContainer;
		ref_unloadingPalletContainer = unloadingPalletContainer;

		ref_bay1Container = bay1Container;
		ref_bay2Container = bay2Container;
		ref_bay3Container = bay3Container;
		ref_bay4Container = bay4Container;
		ref_bay5Container = bay5Container;
		ref_bay6Container = bay6Container;
		ref_bay7Container = bay7Container;
		ref_bay8Container = bay8Container;
		ref_bay9Container = bay9Container;
		ref_bay10Container = bay10Container;
		ref_bay11Container = bay11Container;
		ref_bay12Container = bay12Container;
		ref_bay13Container = bay13Container;
		ref_bay14Container = bay14Container;
		ref_bay15Container = bay15Container;
		ref_bay16Container = bay16Container;
		ref_bay17Container = bay17Container;
		ref_bay18Container = bay18Container;
		ref_bay19Container = bay19Container;
		ref_bay20Container = bay20Container;
		ref_rejectionBayContainer = rejectionBayContainer;
		ref_unloadingBayContainer = unloadingBayContainer;
	}

	public void loadDutExecutor() throws JSONException {
		ApplicationLauncher.logger.info("loadDutExecutor: entry");
		// conveyorDebugGUILoaded = true;
		ApplicationHomeController.SetInstantMetricsGUI_Displayed(true);

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/conveyor/DutExecutor" + ConstantApp.THEME_FXML));

		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("loadDutExecutor: IOException:" + ex.getMessage());
			return;
		}

		Stage palletRackerStage = new Stage();
		palletRackerStage.initModality(Modality.NONE);// Modality.WINDOW_MODAL
		// InstantMetricsStage.initModality(Modality.WINDOW_MODAL);
		// https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		palletRackerStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		palletRackerStage.setScene(newScene);
		palletRackerStage.setTitle(ConstantVersion.APPLICATION_NAME + " - Dut Executor");
		palletRackerStage.setResizable(false);
		palletRackerStage.show();

		palletRackerStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}

		});

	}

	/**
	 * Initializes the Motor Control tab's hover functionality.
	 * Loads the MotorControlPane.fxml content once and manages its visibility via a
	 * Popup.
	 */
	private void initializeMotorControlTab() {
		motorControlPopup = new Popup();
		motorControlPopup.setAutoHide(true);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conveyor/MotorControlPane_W.fxml"));
			motorControlContent = loader.load();
			motorControlPopup.getContent().add(motorControlContent);
		} catch (IOException e) {
			ApplicationLauncher.logger.error("Failed to load MotorControlPane.fxml: " + e.getMessage());
			logEvent("Failed to load Motor Control Panel.");
			return;
		}

		PauseTransition hideDelay = new PauseTransition(Duration.millis(300));

		motorControlTabContainer.setOnMouseEntered(event -> {
			hideDelay.stop();
			if (!motorControlPopup.isShowing()) {
				motorControlPopup.show(
						motorControlTabContainer.getScene().getWindow(),
						motorControlTabContainer.localToScreen(0, 0).getX() - motorControlContent.prefWidth(-1),
						motorControlTabContainer.localToScreen(0, 0).getY());
			}
		});

		motorControlTabContainer.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!motorControlContent.isHover() && !motorControlTabContainer.isHover()) {
					motorControlPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		motorControlContent.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!motorControlContent.isHover() && !motorControlTabContainer.isHover()) {
					motorControlPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		// Optional: Cancel hide if re-entered
		motorControlContent.setOnMouseEntered(event -> hideDelay.stop());
	}

	/**
	 * Initializes the Motor Control tab's hover functionality.
	 * Loads the MotorControlPane.fxml content once and manages its visibility via a
	 * Popup.
	 */
	private void initializeMainControlTab() {
		mainControlPopup = new Popup();
		mainControlPopup.setAutoHide(true);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conveyor/MainControlPane_W.fxml"));
			mainControlContent = loader.load();
			mainControlPopup.getContent().add(mainControlContent);
		} catch (IOException e) {
			ApplicationLauncher.logger.error("Failed to load MainControlPane.fxml: " + e.getMessage());
			logEvent("Failed to load Main Control Panel.");
			return;
		}

		PauseTransition hideDelay = new PauseTransition(Duration.millis(300));

		mainControlTabContainer.setOnMouseEntered(event -> {
			hideDelay.stop();
			if (!mainControlPopup.isShowing()) {
				mainControlPopup.show(
						mainControlTabContainer.getScene().getWindow(),
						mainControlTabContainer.localToScreen(0, 0).getX() - mainControlContent.prefWidth(-1),
						mainControlTabContainer.localToScreen(0, 0).getY());
			}
		});

		mainControlTabContainer.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!mainControlContent.isHover() && !mainControlTabContainer.isHover()) {
					mainControlPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		mainControlContent.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!mainControlContent.isHover() && !mainControlTabContainer.isHover()) {
					mainControlPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		mainControlContent.setOnMouseEntered(event -> hideDelay.stop());
	}

	/**
	 * Initializes the Motor Control tab's hover functionality.
	 * Loads the MotorControlPane.fxml content once and manages its visibility via a
	 * Popup.
	 */
	private void initializeLaunchersTab() {
		launcherPopup = new Popup();
		launcherPopup.setAutoHide(true);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conveyor/LauncherPane_W.fxml"));
			launcherContent = loader.load();
			launcherPopup.getContent().add(launcherContent);
		} catch (IOException e) {
			ApplicationLauncher.logger.error("Failed to load launcherPane.fxml: " + e.getMessage());
			logEvent("Failed to load Main Control Panel.");
			return;
		}

		PauseTransition hideDelay = new PauseTransition(Duration.millis(300));

		launcherTabContainer.setOnMouseEntered(event -> {
			hideDelay.stop();
			if (!launcherPopup.isShowing()) {
				launcherPopup.show(
						launcherTabContainer.getScene().getWindow(),
						launcherTabContainer.localToScreen(0, 0).getX() - launcherContent.prefWidth(-1),
						launcherTabContainer.localToScreen(0, 0).getY());
			}
		});

		launcherTabContainer.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!launcherContent.isHover() && !launcherTabContainer.isHover()) {
					launcherPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		launcherContent.setOnMouseExited(event -> {
			hideDelay.setOnFinished(e -> {
				if (!launcherContent.isHover() && !launcherTabContainer.isHover()) {
					launcherPopup.hide();
				}
			});
			hideDelay.playFromStart();
		});

		launcherContent.setOnMouseEntered(event -> hideDelay.stop());
	}

	/**
	 * Initializes the list of bay containers for pallet operations.
	 */
	private void initializeBayContainer() {
		allContainers.addAll(Arrays.asList(
				pallet1Container, pallet2Container, pallet3Container, pallet4Container,
				pallet5Container, pallet6Container, pallet7Container, pallet8Container,
				pallet9Container, pallet10Container, pallet11Container, pallet12Container,
				pallet13Container, pallet14Container, pallet15Container, pallet16Container, // STA_NLD1

				pallet17Container, pallet18Container, pallet19Container, pallet20Container,
				rejectionPalletContainer, unloadingPalletContainer));

		// Add context menu to all containers
		for (AnchorPane container : allContainers) {
			String bayKey = getBayKeyForPalletContainer(container);
			if (bayKey != null) {
				CommonContextMenu menu = new CommonContextMenu(bayKey) {
					@Override
					protected void handleAction(PalletController.BayActionType actionType) {
						handleContainerAction(container, actionType);
					}

					@Override
					protected boolean shouldShowMenu() {
						// Always show menu for containers, even when empty
						return true;
					}
				};
				menu.attachToNode(container);
			}
		}
	}

	private String getBayKeyForPalletContainer(AnchorPane container) {

		ApplicationLauncher.logger.debug("getBayKeyForPalletContainer: Entry ");
		for (Map.Entry<String, AnchorPane> entry : bayKeyToPalletContainer.entrySet()) {
			ApplicationLauncher.logger.debug("getBayKeyForPalletContainer: entry.getValue(): " + entry.getValue());
			if (entry.getValue() == container) {
				ApplicationLauncher.logger.debug("getBayKeyForPalletContainer: Hit1 ");
				return entry.getKey();
			}
		}
		return null;
	}

	private void handleContainerAction(AnchorPane container, PalletController.BayActionType actionType) {
		// Find the pallet in this container if any
		/*
		 * Node pallet = container.getChildren().stream()
		 * .filter(n -> n.getUserData() instanceof PalletController)
		 * .findFirst()
		 * .orElse(null);
		 * 
		 * if (pallet != null && pallet.getUserData() instanceof PalletController) {
		 * PalletController palletController = (PalletController) pallet.getUserData();
		 * palletController.handleActionByBayType(actionType);
		 * } else {
		 * ApplicationLauncher.logger.
		 * warn("No pallet found in container to perform action: " + actionType);
		 * }
		 */

		String bayKey = getBayKeyForPalletContainer(container);
		if (bayKey == null)
			return;

		BayActionHandler handler = new BayActionHandler(bayKey);

		// First try to delegate to pallet if exists
		Node pallet = container.getChildren().stream()
				.filter(n -> n.getUserData() instanceof PalletController)
				.findFirst()
				.orElse(null);

		if (pallet != null && pallet.getUserData() instanceof PalletController) {
			PalletController palletController = (PalletController) pallet.getUserData();
			// palletController.handleActionByBayType(actionType);
			// palletController.handleBayAction(actionType);
			handler.handleActionByBayType(actionType);
			logEvent("Action " + actionType + " performed on pallet " +
					palletController.getPalletName().getText());
		} else {
			// Handle action directly on container
			handler.handleActionByBayType(actionType);
			logEvent("Action " + actionType + " performed on container " + bayKey);
		}
	}

	/**
	 * Initializes the mapping of bay key to their respective containers.
	 */
	private void initializeBayKeyMap() {
		bayKeyToPalletContainer.put(ConstantConveyor.FT_BAY_KEY, pallet1Container);
		bayKeyToPalletContainer.put(ConstantConveyor.HV_BAY_KEY, pallet2Container);
		bayKeyToPalletContainer.put(ConstantConveyor.IR_BAY_KEY, pallet3Container);
		bayKeyToPalletContainer.put(ConstantConveyor.CALIBRATION_BAY_KEY, pallet4Container);
		bayKeyToPalletContainer.put(ConstantConveyor.WAITING_PP4_BAY_KEY, pallet5Container);
		bayKeyToPalletContainer.put(ConstantConveyor.WAITING_PP3_BAY_KEY, pallet6Container);
		bayKeyToPalletContainer.put(ConstantConveyor.WAITING_PP2_BAY_KEY, pallet7Container);
		bayKeyToPalletContainer.put(ConstantConveyor.WAITING_PP1_BAY_KEY, pallet8Container);
		bayKeyToPalletContainer.put(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, pallet9Container);
		bayKeyToPalletContainer.put(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, pallet10Container);
		bayKeyToPalletContainer.put(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, pallet11Container);
		bayKeyToPalletContainer.put(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, pallet12Container);
		// bayKeyToContainer.put(ConstantConveyor.STA_NLD1_BAY_KEY, bay13Container);

		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, pallet13Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, pallet14Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, pallet15Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, pallet16Container);

		// bayKeyToContainer.put(ConstantConveyor.STA_NLD2_BAY_KEY, bay17Container);

		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, pallet17Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, pallet18Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, pallet19Container);
		bayKeyToPalletContainer.put(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, pallet20Container);

		bayKeyToPalletContainer.put(ConstantConveyor.REJECTION_BAY_KEY, rejectionPalletContainer);
		bayKeyToPalletContainer.put(ConstantConveyor.UNLOADING_BAY_KEY, unloadingPalletContainer);

		bayKeyToBayContainer.put(ConstantConveyor.FT_BAY_KEY, bay1Container);
		bayKeyToBayContainer.put(ConstantConveyor.HV_BAY_KEY, bay2Container);
		bayKeyToBayContainer.put(ConstantConveyor.IR_BAY_KEY, bay3Container);
		bayKeyToBayContainer.put(ConstantConveyor.CALIBRATION_BAY_KEY, bay4Container);
		bayKeyToBayContainer.put(ConstantConveyor.WAITING_PP4_BAY_KEY, bay5Container);
		bayKeyToBayContainer.put(ConstantConveyor.WAITING_PP3_BAY_KEY, bay6Container);
		bayKeyToBayContainer.put(ConstantConveyor.WAITING_PP2_BAY_KEY, bay7Container);
		bayKeyToBayContainer.put(ConstantConveyor.WAITING_PP1_BAY_KEY, bay8Container);
		bayKeyToBayContainer.put(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, bay9Container);
		bayKeyToBayContainer.put(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, bay10Container);
		bayKeyToBayContainer.put(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, bay11Container);
		bayKeyToBayContainer.put(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, bay12Container);
		// bayKeyToContainer.put(ConstantConveyor.STA_NLD1_BAY_KEY, bay13Container);

		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, bay13Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, bay14Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, bay15Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, bay16Container);

		// bayKeyToContainer.put(ConstantConveyor.STA_NLD2_BAY_KEY, bay17Container);

		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, bay17Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, bay18Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, bay19Container);
		bayKeyToBayContainer.put(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, bay20Container);

		bayKeyToBayContainer.put(ConstantConveyor.REJECTION_BAY_KEY, rejectionBayContainer);
		bayKeyToBayContainer.put(ConstantConveyor.UNLOADING_BAY_KEY, unloadingBayContainer);
	}

	/**
	 * Loads the conveyor debug GUI.
	 */
	public void loadConveyorDebugGUI() {
		ApplicationLauncher.logger.info("loadConveyorDebugGUI: Entry");
		conveyorDebugGUILoaded = true;
		ApplicationHomeController.SetInstantMetricsGUI_Displayed(true);

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/fxml/conveyor/ConveyorDebugV2" + ConstantApp.THEME_FXML));
			Scene scene = new Scene(loader.load());
			Stage stage = createStage("Conveyor Debug", scene);
			stage.show();
		} catch (IOException e) {
			ApplicationLauncher.logger.error("loadConveyorDebugGUI: IOException: " + e.getMessage());
		}
	}

	/**
	 * Loads the pallet tracker GUI.
	 */
	public void loadPalletTrackerGUI() {
		ApplicationLauncher.logger.info("loadPalletTrackerGUI: Entry");
		ApplicationHomeController.SetInstantMetricsGUI_Displayed(true);

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/fxml/conveyor/PalletTracker" + ConstantApp.THEME_FXML));
			Scene scene = new Scene(loader.load());
			Stage stage = createStage("Pallet Tracker", scene);
			stage.show();
		} catch (IOException e) {
			ApplicationLauncher.logger.error("loadPalletTrackerGUI: IOException: " + e.getMessage());
		}
	}

	/**
	 * Creates a new stage with the specified title and scene, configured for debug
	 * GUIs.
	 * 
	 * @param title Stage title
	 * @param scene Scene to display
	 * @return Configured stage
	 */
	private Stage createStage(String title, Scene scene) {
		Stage stage = new Stage();
		stage.initModality(Modality.NONE);
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		stage.setScene(scene);
		stage.setTitle(ConstantVersion.APPLICATION_NAME + " - " + title);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> event.consume());
		return stage;
	}

	/**
	 * Handles adding a new pallet via the pallet name field.
	 */
	@FXML
	private void handleAddPallet() {
		String palletName = palletNameField.getText().trim();
		if (palletName.isEmpty()) {
			logEvent("Failed to add pallet: Empty pallet name");
			ApplicationLauncher.logger.warn("handleAddPallet: Empty pallet name");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/fxml/conveyor/" + palletViewFxmlFileName + ConstantApp.THEME_FXML));
			Node pallet = loader.load();
			// Changed to explicit cast for Java 8 compatibility
			PalletController palletController = (PalletController) loader.getController();
			palletController.setPalletName(palletName);
			palletController.setBayTypeKey(ConstantConveyor.FT_BAY_KEY);
			pallet.setUserData(palletController);

			pallet1Container.getChildren().add(pallet);
			logEvent("Added pallet: " + palletName + " to FT Bay");
			ApplicationLauncher.logger.info("handleAddPallet: Added pallet " + palletName);
		} catch (IOException e) {
			logEvent("Failed to add pallet: " + e.getMessage());
			ApplicationLauncher.logger.error("handleAddPallet: IOException: " + e.getMessage());
		}
	}

	@FXML
	public void btnAllStartOnClick() {
		ApplicationLauncher.logger.info("btnFtStartOnClick : Invoked:");

		btnAllStart.setDisable(true);
		btnAllStop.setDisable(false);

		funtionalBayStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.FT_BAY_KEY)) {
			funtionalBayStartTaskTimer.schedule(new FunctionalTestBayBypass(), 100);
		} else {
			activeFtEngine = new BayStateEngine(ConstantConveyor.FT_BAY_KEY, new Ft());
			funtionalBayStartTaskTimer.schedule(activeFtEngine, 100);
		}

		hvtBayStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.HV_BAY_KEY)) {
			hvtBayStartTaskTimer.schedule(new HighVoltageTestBayBypass(), 100);
		} else {
			activeHvEngine = new BayStateEngine(ConstantConveyor.HV_BAY_KEY, new Hv());
			hvtBayStartTaskTimer.schedule(activeHvEngine, 100);
		}

		insResStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.IR_BAY_KEY)) {
			insResStartTaskTimer.schedule(new InsulationResistanceTestBayBypass(), 100);
		} else {
			activeIrEngine = new BayStateEngine(ConstantConveyor.IR_BAY_KEY, new Ir());
			insResStartTaskTimer.schedule(activeIrEngine, 100);
		}

		calibrationStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.CALIBRATION_BAY_KEY)) {
			calibrationStartTaskTimer.schedule(new CalibrationBayBypass(), 100);
		} else {
			activeCalibEngine = new BayStateEngine(ConstantConveyor.CALIBRATION_BAY_KEY, new Calib());
			calibrationStartTaskTimer.schedule(activeCalibEngine, 100);
		}

		waitingBayStartTaskTimer = new Timer();
		activeWaitingEngine = new BayStateEngine(ConstantConveyor.WAITING_BAY_KEY, new VerificWaiting());
		waitingBayStartTaskTimer.schedule(activeWaitingEngine, 100);

		verificStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.VERIFICATION_BAY_KEY)) {
			verificStartTaskTimer.schedule(new VerificationTestBayBypass(), 100);
		} else {
			activeVerificEngine = new BayStateEngine(ConstantConveyor.VERIFICATION_BAY_KEY, new Verification());
			verificStartTaskTimer.schedule(activeVerificEngine, 100);
		}

		sctNlt1StartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.STA_NLD1_BAY_KEY)) {
			sctNlt1StartTaskTimer.schedule(new STA_NoLoadTestBay1Bypass(), 100);
		} else {
			activeStaNld1Engine = new BayStateEngine(ConstantConveyor.STA_NLD1_BAY_KEY, new StaNld_Bay1());
			sctNlt1StartTaskTimer.schedule(activeStaNld1Engine, 100);
		}

		sctNlt2StartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.STA_NLD2_BAY_KEY)) {
			sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2Bypass(), 100);
		} else {
			activeStaNld2Engine = new BayStateEngine(ConstantConveyor.STA_NLD2_BAY_KEY, new StaNld_Bay2());
			sctNlt2StartTaskTimer.schedule(activeStaNld2Engine, 100);
		}

		commStartTaskTimer = new Timer();
		if (ConstantBypassFlags.isBayFullyBypassed(ConstantConveyor.COMMUNICATION_BAY_KEY)) {
			commStartTaskTimer.schedule(new CommBayBypass(), 100);
		} else {
			activeCommEngine = new BayStateEngine(ConstantConveyor.COMMUNICATION_BAY_KEY, new Comm());
			commStartTaskTimer.schedule(activeCommEngine, 100);
		}

		ApplicationLauncher.logger.info("btnFtStartOnClick : EXIT:");
	}

	@FXML
	public void btnAllStopOnClick() {
		ApplicationLauncher.logger.info("btnAllStopOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = true;
		BayUtils.setUserAborted(true);

		Ft.setStopProcessRequestedFtBay(true);
		Hv.abort_HVT_Bay = true;
		Ir.abort_IRT_Bay = true;
		Calib.abort_Calib_Bay = true;
		Verification.abort_VerificTest_Bay = true;
		StaNld_Bay1.abort_SCT_NLT_Bay1 = true;
		StaNld_Bay2.abort_SCT_NLT_Bay2 = true;
		Comm.abort_CommTest_Bay = true;

		Ft.setStopProcessRequestedFtBay(true);
		Hv.setStopProcessRequestedHvtBay(true);
		Ir.setStopProcessRequestedIrtBay(true);
		Calib.setStopProcessRequestedCalibBay(true);
		Verification.setStopProcessRequestedVerificBay(true);
		StaNld_Bay1.setStopProcessRequestedStaNldBay1(true);
		StaNld_Bay2.setStopProcessRequestedStaNldBay2(true);
		StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test2  : true");
		Comm.setStopProcessRequestedCommBay(true);
		VerificWaiting.setStopProcessRequestedWaitingBay(true);

		btnAllStart.setDisable(false);
		btnAllStop.setDisable(true);

		funtionalBayStopTaskTimer = new Timer();
		funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(), 100);
		if (activeFtEngine != null)
			activeFtEngine.requestStop();

		hvtBayStopTaskTimer = new Timer();
		hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(), 100);
		if (activeHvEngine != null)
			activeHvEngine.requestStop();

		insResStopTaskTimer = new Timer();
		insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(), 100);
		if (activeIrEngine != null)
			activeIrEngine.requestStop();

		calibrationStopTaskTimer = new Timer();
		calibrationStopTaskTimer.schedule(new CalibrationBayStop(), 100);
		if (activeCalibEngine != null)
			activeCalibEngine.requestStop();

		if (activeWaitingEngine != null)
			activeWaitingEngine.requestStop();
		waitingBayStopTaskTimer = new Timer();
		waitingBayStopTaskTimer.schedule(new WaitingBayStop(), 100);

		verificStopTaskTimer = new Timer();
		verificStopTaskTimer.schedule(new VerificationTestBayStop(), 100);
		if (activeVerificEngine != null)
			activeVerificEngine.requestStop();

		if (activeStaNld1Engine != null)
			activeStaNld1Engine.requestStop();
		sctNlt1StopTaskTimer = new Timer();
		sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(), 100);

		if (activeStaNld2Engine != null)
			activeStaNld2Engine.requestStop();
		sctNlt2StopTaskTimer = new Timer();
		sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(), 100);

		if (activeCommEngine != null)
			activeCommEngine.requestStop();
		commStopTaskTimer = new Timer();
		commStopTaskTimer.schedule(new CommunicationTestBayStop(), 100);

		ApplicationLauncher.logger.info("btnAllStopOnClick : Exit:");
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	/**
	 * Adds a new pallet view to the specified bay.
	 * Uses Runnable for Java 8 compatibility and explicit casting.
	 * 
	 * @param bayKey                   Bay key (e.g., FT_BAY_KEY)
	 * @param palletQrId               Pallet QR ID
	 * @param meterListWithSerialNoMap Map of meter positions to serial numbers
	 */
	public void addNewPalletViewDashboard(String bayKey, String palletQrId,
			Map<Integer, String> meterListWithSerialNoMap) {
		ApplicationLauncher.logger
				.debug("addNewPalletViewDashboard: Entry: bayKey: " + bayKey + " : palletQrId: " + palletQrId);
		if (palletQrId.isEmpty()) {
			logEvent("Failed to add pallet: Empty QR ID");
			ApplicationLauncher.logger.warn("addNewPalletViewDashboard: Empty pallet QR ID");
			return;
		}

		AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
		if (targetBay == null) {
			logEvent("Failed to add pallet: Invalid bay key " + bayKey);
			ApplicationLauncher.logger.warn("addNewPalletViewDashboard: Invalid bay key " + bayKey);
			return;
		}

		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (targetBay.getChildren().stream().anyMatch(new java.util.function.Predicate<Node>() {
					@Override
					public boolean test(Node node) {
						return node.getUserData() instanceof PalletController;
					}
				})) {
					logEvent("Failed to add pallet: Bay " + bayKey + " already contains a pallet");
					ApplicationLauncher.logger
							.warn("addNewPalletViewDashboard: Bay " + bayKey + " already contains a pallet");
					return;
				}

				try {
					FXMLLoader loader = new FXMLLoader(getClass()
							.getResource("/fxml/conveyor/" + palletViewFxmlFileName + ConstantApp.THEME_FXML));
					Node pallet = loader.load();
					// Changed to explicit cast for Java 8 compatibility
					PalletController palletController = (PalletController) loader.getController();
					palletController.setPalletName(palletQrId);
					palletController.setBayTypeKey(bayKey);

					int maxDutSupported = DeviceDataManagerController.getConveyorConfigParsedKey().getMaxDutSupported();
					for (int position = 1; position <= maxDutSupported; position++) {
						palletController.setSerialByPosition(position, meterListWithSerialNoMap.getOrDefault(position,
								NewlandQRCodeScanner.QR_METER_SERIAL_NUMBER_SCAN_FAILED));// "SN:N/A"));nbjh
					}
					/*
					 * palletController.startBlinkingPalletsExistInQueueIndicator();
					 * palletController.setPalletsExistInQueueIndicator(false);
					 * Sleep(50);
					 * palletController.startBlinkingTargetBayAllPalletsFreeIndicator();
					 * palletController.setTargetBayAllPalletsFreeIndicator(false);
					 * Sleep(50);
					 * palletController.startBlinkingEntryStopperOpenIndicator();
					 * palletController.setEntryStopperOpenIndicator(true);
					 * Sleep(50);
					 * palletController.startBlinkingExitStopperOpenIndicator();
					 * palletController.setExitStopperOpenIndicator(false);
					 * Sleep(50);
					 * palletController.startBlinkingAllPalletsExistInBayIndicator();
					 * palletController.setAllPalletsExistInBayIndicator(true);
					 * Sleep(50);
					 */
					// palletController.setTargetBayAllPalletsFreeIndicatorVisible(false);

					pallet.setUserData(palletController);

					targetBay.getChildren().add(pallet);
					logEvent("Added pallet " + palletQrId + " to " + bayKey);
					ApplicationLauncher.logger
							.info("addNewPalletViewDashboard: Added pallet " + palletQrId + " to " + bayKey);
					// Sleep(20000);
					// palletController.stopBlinkingQueueIndicator();
				} catch (IOException e) {
					logEvent("Failed to add pallet: " + e.getMessage());
					ApplicationLauncher.logger.error("addNewPalletViewDashboard: IOException: " + e.getMessage());
				}

				ApplicationLauncher.logger.info("addNewPalletViewDashboard: bayKey2: " + bayKey);
				if (bayKey.startsWith(ConstantConveyor.VERIFICATION_BAY_KEY)) {
					verific1BayLocked = false;
					ApplicationLauncher.logger.info("addNewPalletViewDashboard: Added pallet " + palletQrId + " : "
							+ bayKey + " verific1BayLocked released");
				} else if (bayKey.startsWith(ConstantConveyor.STA_NLD1_BAY_KEY)) {
					sta1BayLocked = false;
					ApplicationLauncher.logger.info("addNewPalletViewDashboard: Added pallet " + palletQrId + " : "
							+ bayKey + " sta1BayLocked released");
				} else if (bayKey.startsWith(ConstantConveyor.STA_NLD2_BAY_KEY)) {
					sta2BayLocked = false;
					ApplicationLauncher.logger.info("addNewPalletViewDashboard: Added pallet " + palletQrId + " : "
							+ bayKey + " sta2BayLocked released");
				}

			}
		});
	}

	/**
	 * Adds a pallet to the first available waiting bay (PP1 to PP4 in order).
	 * 
	 * @param palletName               The pallet QR ID
	 * @param meterListWithSerialNoMap Map of meter positions to serial numbers
	 * @return true if pallet was added successfully, false if all waiting bays are
	 *         occupied
	 */
	public boolean addPalletToFirstAvailableWaitingBay(String palletName,
			Map<Integer, String> meterListWithSerialNoMap) {
		// Define the order of waiting bays to check (PP1 to PP4)
		String[] waitingBaysInOrder = {
				ConstantConveyor.WAITING_PP1_BAY_KEY,
				ConstantConveyor.WAITING_PP2_BAY_KEY,
				ConstantConveyor.WAITING_PP3_BAY_KEY,
				ConstantConveyor.WAITING_PP4_BAY_KEY
		};

		for (String bayKey : waitingBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger.error("Invalid waiting bay key: " + bayKey);
				continue;
			}

			// Check if bay is empty
			boolean isBayEmpty = targetBay.getChildren().stream()
					.noneMatch(node -> node.getUserData() instanceof PalletController);

			if (isBayEmpty) {
				// Found an empty bay - add the pallet here
				addNewPalletViewDashboard(bayKey, palletName, meterListWithSerialNoMap);
				logEvent("Added pallet " + palletName + " to " + bayKey);
				return true;
			}
		}

		// All waiting bays are occupied
		logEvent("Failed to add pallet " + palletName + ": All waiting bays are occupied");
		ApplicationLauncher.logger.warn("All waiting bays are occupied - cannot add pallet " + palletName);
		return false;
	}

	public boolean addPalletToFirstAvailableVerificationBay(String palletName,
			Map<Integer, String> meterListWithSerialNoMap) {
		ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName);
		// Define the order of verification bays to check (PP1 to PP4)
		String[] verificBaysInOrder = {
				ConstantConveyor.VERIFICATION_PP1_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP2_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP3_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			ApplicationLauncher.logger
					.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit1");
			if (targetBay == null) {
				ApplicationLauncher.logger.error("addPalletToFirstAvailableVerificationBay:  palletName : " + palletName
						+ " : Invalid Verification bay key: " + bayKey);
				continue;
			}

			// Skip if bay is already scheduled for update
			/*
			 * if (reservedVerificBayKeys.contains(bayKey)) {
			 * ApplicationLauncher.logger.
			 * debug("addPalletToFirstAvailableVerificationBay: Bay " + bayKey +
			 * " is already reserved for update.");
			 * continue;
			 * }
			 */

			boolean isBayEmpty = targetBay.getChildren().stream()
					.noneMatch(node -> node.getUserData() instanceof PalletController);

			if (isBayEmpty) {
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit2");
				// reservedVerificBayKeys.add(bayKey); // ✅ Reserve immediately to prevent race
				// condition

				// Platform.runLater(() -> {
				try {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit3");
					// Double-check again inside Platform.runLater
					boolean stillEmpty = targetBay.getChildren().stream()
							.noneMatch(node -> node.getUserData() instanceof PalletController);

					if (!stillEmpty) {
						ApplicationLauncher.logger.debug(
								"addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit4");
						ApplicationLauncher.logger.warn("addPalletToFirstAvailableVerificationBay : Bay " + bayKey
								+ " already contains a pallet (during Platform.runLater)");
						logEvent("Skipped adding pallet " + palletName + " to " + bayKey
								+ ": already occupied at UI time");
						// return;
					}
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit5");
					int waitTimeInMSec = 3000;
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : "
							+ palletName + " : awaiting for verific semlock acquiring entry: " + waitTimeInMSec);
					while ((waitTimeInMSec > 0) && (!BayUtils.isUserAborted())
							&& (verific1BayLocked)) {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : "
								+ palletName + " : still awaiting for verific semlock: " + waitTimeInMSec);
						waitTimeInMSec = waitTimeInMSec - 200;
					}
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : "
							+ palletName + " : awaiting for verific semlock exit");
					if (!verific1BayLocked) {
						verific1BayLocked = true;
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : "
								+ palletName + " : verific semlock: acquired");
						addNewPalletViewDashboard(bayKey, palletName, meterListWithSerialNoMap);
					} else {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableVerificationBay: palletName : "
								+ palletName + " : verific semlock stil found locked");
					}
					logEvent("Added pallet " + palletName + " to " + bayKey);// "(confirmed in Platform.runLater)");
				} finally {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit6");
					// reservedVerificBayKeys.remove(bayKey); // ✅ Always clean up reservation
				}
				// });
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableVerificationBay: palletName : " + palletName + " : Hit7");
				return true; // We scheduled the addition
			}
		}

		// All bays are occupied or reserved
		logEvent("Failed to add pallet " + palletName + ": All Verification bays are occupied or reserved");
		ApplicationLauncher.logger.warn("addPalletToFirstAvailableVerificationBay: palletName : " + palletName
				+ " : All Verification bays are occupied or reserved - cannot add pallet " + palletName);
		return false;
	}

	public boolean addPalletToFirstAvailableSta1Bay(String palletName, Map<Integer, String> meterListWithSerialNoMap) {
		ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName);
		// Define the order of verification bays to check (PP1 to PP4)
		String[] verificBaysInOrder = {
				ConstantConveyor.STA_NLD1_PP1_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP2_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP3_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			ApplicationLauncher.logger
					.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit1");
			if (targetBay == null) {
				ApplicationLauncher.logger.error("addPalletToFirstAvailableSta1Bay:  palletName : " + palletName
						+ " : Invalid STA1 bay key: " + bayKey);
				continue;
			}

			// Skip if bay is already scheduled for update
			/*
			 * if (reservedVerificBayKeys.contains(bayKey)) {
			 * ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: Bay " +
			 * bayKey + " is already reserved for update.");
			 * continue;
			 * }
			 */

			boolean isBayEmpty = targetBay.getChildren().stream()
					.noneMatch(node -> node.getUserData() instanceof PalletController);

			if (isBayEmpty) {
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit2");
				// reservedVerificBayKeys.add(bayKey); // ✅ Reserve immediately to prevent race
				// condition

				// Platform.runLater(() -> {
				try {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit3");
					// Double-check again inside Platform.runLater
					boolean stillEmpty = targetBay.getChildren().stream()
							.noneMatch(node -> node.getUserData() instanceof PalletController);
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
							+ " : stillEmpty : " + stillEmpty);
					if (!stillEmpty) {
						ApplicationLauncher.logger
								.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit4");
						ApplicationLauncher.logger.warn("addPalletToFirstAvailableSta1Bay : Bay " + bayKey
								+ " already contains a pallet (during Platform.runLater)");
						logEvent("Skipped adding pallet " + palletName + " to " + bayKey
								+ ": already occupied at UI time");
						// return;
					}
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit5");
					int waitTimeInMSec = 3000;
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
							+ " : awaiting for STA1 semlock acquiring entry: " + waitTimeInMSec);
					while ((waitTimeInMSec > 0) && (!BayUtils.isUserAborted())
							&& (sta1BayLocked)) {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
								+ " : still awaiting for STA1 semlock: " + waitTimeInMSec);
						waitTimeInMSec = waitTimeInMSec - 200;
					}
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
							+ " : awaiting for STA1 semlock exit");
					if (!sta1BayLocked) {
						sta1BayLocked = true;
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
								+ " : STA1 semlock: acquired");
						addNewPalletViewDashboard(bayKey, palletName, meterListWithSerialNoMap);
					} else {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
								+ " : STA1 semlock stil found locked");
					}
					logEvent("Added pallet " + palletName + " to " + bayKey);// + " (confirmed in Platform.runLater)");
				} finally {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit6");
					// reservedVerificBayKeys.remove(bayKey); // ✅ Always clean up reservation
				}
				// });
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableSta1Bay: palletName : " + palletName + " : Hit7");
				return true; // We scheduled the addition
			}
		}

		// All bays are occupied or reserved
		logEvent("Failed to add pallet " + palletName + ": All STA1 bays are occupied or reserved");
		ApplicationLauncher.logger.warn("addPalletToFirstAvailableSta1Bay: palletName : " + palletName
				+ " : All STA1 bays are occupied or reserved - cannot add pallet " + palletName);
		return false;
	}

	public boolean addPalletToFirstAvailableSta2Bay(String palletName, Map<Integer, String> meterListWithSerialNoMap) {
		ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName);
		// Define the order of verification bays to check (PP1 to PP4)
		String[] verificBaysInOrder = {
				ConstantConveyor.STA_NLD2_PP1_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP2_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP3_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			ApplicationLauncher.logger
					.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit1");
			if (targetBay == null) {
				ApplicationLauncher.logger.error("addPalletToFirstAvailableSta2Bay:  palletName : " + palletName
						+ " : Invalid STA2 bay key: " + bayKey);
				continue;
			}

			// Skip if bay is already scheduled for update
			/*
			 * if (reservedVerificBayKeys.contains(bayKey)) {
			 * ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: Bay " +
			 * bayKey + " is already reserved for update.");
			 * continue;
			 * }
			 */

			boolean isBayEmpty = targetBay.getChildren().stream()
					.noneMatch(node -> node.getUserData() instanceof PalletController);

			if (isBayEmpty) {
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit2");
				// reservedVerificBayKeys.add(bayKey); // ✅ Reserve immediately to prevent race
				// condition

				// Platform.runLater(() -> {
				try {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit3");
					// Double-check again inside Platform.runLater
					boolean stillEmpty = targetBay.getChildren().stream()
							.noneMatch(node -> node.getUserData() instanceof PalletController);

					if (!stillEmpty) {
						ApplicationLauncher.logger
								.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit4");
						ApplicationLauncher.logger.warn("addPalletToFirstAvailableSta2Bay : Bay " + bayKey
								+ " already contains a pallet (during Platform.runLater)");
						logEvent("Skipped adding pallet " + palletName + " to " + bayKey
								+ ": already occupied at UI time");
						// return;
					}
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit5");
					int waitTimeInMSec = 3000;
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
							+ " : awaiting for STA2 semlock acquiring entry: " + waitTimeInMSec);
					while ((waitTimeInMSec > 0) && (!BayUtils.isUserAborted())
							&& (sta2BayLocked)) {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
								+ " : still awaiting for STA2 semlock: " + waitTimeInMSec);
						waitTimeInMSec = waitTimeInMSec - 200;
					}
					ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
							+ " : awaiting for STA2 semlock exit");
					if (!sta2BayLocked) {
						sta2BayLocked = true;
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
								+ " : STA2 semlock: acquired");
						addNewPalletViewDashboard(bayKey, palletName, meterListWithSerialNoMap);
					} else {
						ApplicationLauncher.logger.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
								+ " : STA2 semlock stil found locked");
					}
					logEvent("Added pallet " + palletName + " to " + bayKey);// + " (confirmed in Platform.runLater)");
				} finally {
					ApplicationLauncher.logger
							.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit6");
					// reservedVerificBayKeys.remove(bayKey); // ✅ Always clean up reservation
				}
				// });
				ApplicationLauncher.logger
						.debug("addPalletToFirstAvailableSta2Bay: palletName : " + palletName + " : Hit7");
				return true; // We scheduled the addition
			}
		}

		// All bays are occupied or reserved
		logEvent("Failed to add pallet " + palletName + ": All STA2 bays are occupied or reserved");
		ApplicationLauncher.logger.warn("addPalletToFirstAvailableSta2Bay: palletName : " + palletName
				+ " : All STA2 bays are occupied or reserved - cannot add pallet " + palletName);
		return false;
	}

	public void removeAllPalletsFromVerificationBays() {
		ApplicationLauncher.logger.debug("removeAllPalletsFromVerificationBays: Entry");
		String[] verificBaysInOrder = {
				ConstantConveyor.VERIFICATION_PP1_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP2_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP3_BAY_KEY,
				ConstantConveyor.VERIFICATION_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger
						.error("removeAllPalletsFromVerificationBays: Invalid Verification bay key: " + bayKey);
				continue;
			}

			// Collect pallets to remove
			List<Node> nodesToRemove = targetBay.getChildren().stream()
					.filter(node -> node.getUserData() instanceof PalletController)
					.collect(Collectors.toList());

			// Remove them from the UI
			Platform.runLater(() -> targetBay.getChildren().removeAll(nodesToRemove));

			ApplicationLauncher.logger.info("removeAllPalletsFromVerificationBays: Removed " + nodesToRemove.size()
					+ " pallet(s) from " + bayKey);
		}
	}

	public void removeAllPalletsFromSta1Bays() {
		ApplicationLauncher.logger.debug("removeAllPalletsFromSta1Bays: Entry");
		String[] verificBaysInOrder = {
				ConstantConveyor.STA_NLD1_PP1_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP2_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP3_BAY_KEY,
				ConstantConveyor.STA_NLD1_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger.error("removeAllPalletsFromSta1Bays: Invalid STA1 bay key: " + bayKey);
				continue;
			}

			// Collect pallets to remove
			List<Node> nodesToRemove = targetBay.getChildren().stream()
					.filter(node -> node.getUserData() instanceof PalletController)
					.collect(Collectors.toList());

			// Remove them from the UI
			Platform.runLater(() -> targetBay.getChildren().removeAll(nodesToRemove));

			ApplicationLauncher.logger.info(
					"removeAllPalletsFromSta1Bays: Removed " + nodesToRemove.size() + " pallet(s) from " + bayKey);
		}
	}

	public void removeAllPalletsFromSta2Bays() {
		ApplicationLauncher.logger.debug("removeAllPalletsFromSta2Bays: Entry");
		String[] verificBaysInOrder = {
				ConstantConveyor.STA_NLD2_PP1_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP2_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP3_BAY_KEY,
				ConstantConveyor.STA_NLD2_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger.error("removeAllPalletsFromSta2Bays: Invalid STA2 bay key: " + bayKey);
				continue;
			}

			// Collect pallets to remove
			List<Node> nodesToRemove = targetBay.getChildren().stream()
					.filter(node -> node.getUserData() instanceof PalletController)
					.collect(Collectors.toList());

			// Remove them from the UI
			Platform.runLater(() -> targetBay.getChildren().removeAll(nodesToRemove));

			ApplicationLauncher.logger.info(
					"removeAllPalletsFromSta2Bays: Removed " + nodesToRemove.size() + " pallet(s) from " + bayKey);
		}
	}

	public void removeAllPalletsFromWaitingVerific1Bays() {
		ApplicationLauncher.logger.debug("removeAllPalletsFromWaitingVerific1Bays: Entry");
		String[] verificBaysInOrder = {
				ConstantConveyor.WAITING_PP1_BAY_KEY,
				ConstantConveyor.WAITING_PP2_BAY_KEY,
				ConstantConveyor.WAITING_PP3_BAY_KEY,
				ConstantConveyor.WAITING_PP4_BAY_KEY
		};

		for (String bayKey : verificBaysInOrder) {
			AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
			if (targetBay == null) {
				ApplicationLauncher.logger
						.error("removeAllPalletsFromWaitingVerific1Bays: Invalid WaitingVerific1 bay key: " + bayKey);
				continue;
			}

			// Collect pallets to remove
			List<Node> nodesToRemove = targetBay.getChildren().stream()
					.filter(node -> node.getUserData() instanceof PalletController)
					.collect(Collectors.toList());

			// Remove them from the UI
			Platform.runLater(() -> targetBay.getChildren().removeAll(nodesToRemove));

			ApplicationLauncher.logger.info("removeAllPalletsFromVerificationBays: Removed " + nodesToRemove.size()
					+ " pallet(s) from " + bayKey);
		}
	}

	/**
	 * Updates serial number for a meter by bay and position.
	 * Uses Runnable and explicit casting for Java 8 compatibility.
	 * 
	 * @param bayKey       Bay key
	 * @param position     Meter position
	 * @param serialNumber Serial number
	 */
	public void updateSerialByBayAndPosition(String bayKey, int position, String serialNumber) {
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
				if (targetBay == null) {
					ApplicationLauncher.logger.warn("Invalid bay key: " + bayKey);
					return;
				}

				for (Node node : targetBay.getChildren()) {
					if (node.getUserData() instanceof PalletController) {
						// Changed to explicit cast for Java 8 compatibility
						PalletController palletController = (PalletController) node.getUserData();
						palletController.setSerialByPosition(position, serialNumber);
						ApplicationLauncher.logger.info(
								"Updated serial in " + bayKey + " at position " + position + " = " + serialNumber);
						return;
					}
				}
				ApplicationLauncher.logger.warn("Failed to update serial: No bay found in " + bayKey);
			}
		});
	}

	/**
	 * Moves a pallet from one bay to another.
	 * Uses Runnable and explicit casting for Java 8 compatibility.
	 * 
	 * @param palletName        Pallet name
	 * @param fromBayName       Source bay key
	 * @param toBayName         Destination bay key
	 * @param destinationBayKey Destination bay key for controller
	 */
	public void movePalletByName(String palletName, String fromBayName, String toBayName, String destinationBayKey) {
		ApplicationLauncher.logger
				.info("movePalletByName: Moving " + palletName + " from " + fromBayName + " to " + toBayName);
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane fromBay = findSourceBay(fromBayName, palletName);
				if (fromBay == null) {
					logEvent("Failed to move pallet: Pallet " + palletName + " not found in " + fromBayName);
					ApplicationLauncher.logger
							.warn("movePalletByName: PalletName " + palletName + " not found in " + fromBayName);
					return;
				}

				AnchorPane toBay = findTargetBay(toBayName);
				if (toBay == null) {
					logEvent("Failed to move attempted: Invalid destination bay " + toBayName);
					ApplicationLauncher.logger.warn("movePalletByName: Invalid destination bay " + toBayName);
					return;
				}

				if (!isBayAvailable(toBay, toBayName)) {
					logEvent("Move failed: " + toBayName + " already has a pallet");
					ApplicationLauncher.logger
							.warn("movePalletByName: Destination bay " + toBayName + " already has a pallet");
					return;
				}

				Node palletToMove = findPalletByName(palletName, fromBay);
				if (palletToMove != null) {
					fromBay.getChildren().remove(palletToMove);
					toBay.getChildren().add(palletToMove);
					AnchorPane.setLeftAnchor(palletToMove, 10.0);
					AnchorPane.setTopAnchor(palletToMove, 10.0);

					if (palletToMove.getUserData() instanceof PalletController) {
						// Changed to explicit cast for Java 8 compatibility
						PalletController palletController = (PalletController) palletToMove.getUserData();
						palletController.setBayTypeKey(destinationBayKey);
					}

					logEvent("Moved " + palletName + " to " + toBayName);
					ApplicationLauncher.logger.info("movePalletByName: Moved " + palletName + " to " + toBayName);
				} else {
					logEvent("Move failed: Pallet " + palletName + " not found in " + fromBayName);
					ApplicationLauncher.logger
							.warn("movePalletByName: Pallet " + palletName + " not found in " + fromBayName);
				}
			}
		});
	}

	/**
	 * Finds the source bay for a pallet, handling group bays (e.g., WAITING,
	 * VERIFICATION).
	 * Ensures subBayKey is scoped to loop.
	 * 
	 * @param bayName    Source bay key
	 * @param palletName Pallet name
	 * @return Source bay container or null if not found
	 */
	private AnchorPane findSourceBay(String bayName, String palletName) {
		List<String> groupKeys = getBayGroupKeys(bayName);
		if (groupKeys != null && !groupKeys.isEmpty()) {
			// subBayKey is loop variable, used only within this scope
			for (String subBayKey : groupKeys) {
				AnchorPane subBay = bayKeyToPalletContainer.get(subBayKey);
				if (subBay != null && subBay.getChildren().stream().anyMatch(new java.util.function.Predicate<Node>() {
					@Override
					public boolean test(Node node) {
						if (node.getUserData() instanceof PalletController) {
							// Changed to explicit cast for Java 8 compatibility
							PalletController palletController = (PalletController) node.getUserData();
							return palletController.getPalletName().getText().equals(palletName);
						}
						return false;
					}
				})) {
					return subBay;
				}
			}
			return null;
		}
		return bayKeyToPalletContainer.get(bayName);
	}

	/**
	 * Finds an available target bay, prioritizing empty slots in group bays.
	 * Ensures subBayKey is scoped to loop.
	 * 
	 * @param bayName Target bay key
	 * @return Target bay container or null if none available
	 */
	private AnchorPane findTargetBay(String bayName) {
		List<String> groupKeys = getBayGroupKeys(bayName);
		if (groupKeys != null && !groupKeys.isEmpty()) {
			// subBayKey is loop variable, used only within this scope
			for (String subBayKey : groupKeys) {
				AnchorPane subBay = bayKeyToPalletContainer.get(subBayKey);
				if (subBay != null && !subBay.getChildren().stream()
						.anyMatch(new java.util.function.Predicate<Node>() {
							@Override
							public boolean test(Node node) {
								return node.getUserData() instanceof PalletController;
							}
						})) {
					return subBay;
				}
			}
			return null;
		}
		return bayKeyToPalletContainer.get(bayName);
	}

	/**
	 * Checks if a bay is available for a new pallet.
	 * 
	 * @param bay     Bay container
	 * @param bayName Bay key
	 * @return True if bay is available
	 */
	public boolean isBayAvailable(AnchorPane bay, String bayName) {
		if (!ConstantConveyor.WAITING_BAY_GROUP_LIST.contains(bayName) &&
				!ConstantConveyor.VERIFIC_BAY_GROUP_LIST.contains(bayName) &&
				!ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST.contains(bayName) &&
				!ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST.contains(bayName)) {
			return !bay.getChildren().stream().anyMatch(new java.util.function.Predicate<Node>() {
				@Override
				public boolean test(Node node) {
					return node.getUserData() instanceof PalletController;
				}
			});
		}
		return true;
	}

	/**
	 * Gets the group keys for a bay group (e.g., WAITING_BAY_KEY).
	 * 
	 * @param bayName Bay key
	 * @return List of sub-bay keys, never null
	 */
	private List<String> getBayGroupKeys(String bayName) {
		if (ConstantConveyor.WAITING_BAY_KEY.equals(bayName)) {
			return ConstantConveyor.WAITING_BAY_GROUP_LIST;
		} else if (ConstantConveyor.VERIFICATION_BAY_KEY.equals(bayName)) {
			return ConstantConveyor.VERIFIC_BAY_GROUP_LIST;
		} else if (ConstantConveyor.STA_NLD1_BAY_KEY.equals(bayName)) {
			return ConstantConveyor.STA_NLD1_BAY_KEY_GROUP_LIST;
		} else if (ConstantConveyor.STA_NLD2_BAY_KEY.equals(bayName)) {
			return ConstantConveyor.STA_NLD2_BAY_KEY_GROUP_LIST;
		}
		return Arrays.asList(); // Java 8 safe: return empty list
	}

	/**
	 * Finds a pallet by name in a bay container.
	 * Uses explicit casting for Java 8 compatibility.
	 * 
	 * @param palletName   Pallet name
	 * @param bayContainer Bay container
	 * @return Pallet node or null if not found
	 */
	private Node findPalletByName(String palletName, AnchorPane bayContainer) {
		return bayContainer.getChildren().stream()
				.filter(new java.util.function.Predicate<Node>() {
					@Override
					public boolean test(Node node) {
						if (node.getUserData() instanceof PalletController) {
							// Changed to explicit cast for Java 8 compatibility
							PalletController palletController = (PalletController) node.getUserData();
							return palletName.equals(palletController.getPalletName().getText());
						}
						return false;
					}
				})
				.findFirst()
				.orElse(null);
	}

	/**
	 * Updates pallet meter statuses and error codes.
	 * Uses Runnable and explicit casting for Java 8 compatibility.
	 * 
	 * @param palletQrId   Pallet QR ID
	 * @param statusMap    Map of positions to meter statuses
	 * @param errorCodeMap Map of positions to error codes
	 */
	public void updateDashBoardPalletStatus(String palletQrId, Map<Integer, MeterStatus> statusMap,
			Map<Integer, String> errorCodeMap) {
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Node foundPallet = null;
				for (AnchorPane container : allContainers) {
					foundPallet = findPalletByName(palletQrId, container);
					if (foundPallet != null)
						break; // Stop once pallet is found
				}

				if (foundPallet != null && foundPallet.getUserData() instanceof PalletController) {
					// Changed to explicit cast for Java 8 compatibility
					PalletController palletController = (PalletController) foundPallet.getUserData();
					statusMap.forEach(new java.util.function.BiConsumer<Integer, MeterStatus>() {
						@Override
						public void accept(Integer pos, MeterStatus status) {
							palletController.setMeterStatus(pos, status);
						}
					});
					errorCodeMap.forEach(new java.util.function.BiConsumer<Integer, String>() {
						@Override
						public void accept(Integer pos, String errorCode) {
							palletController.setErrorCodeByPosition(pos, errorCode);
						}
					});
					ApplicationLauncher.logger.info("Updated pallet " + palletQrId + " status and error codes");
				} else {
					ApplicationLauncher.logger.warn("Failed to update pallet: Not found: " + palletQrId);
				}
			}
		});
	}

	/**
	 * Updates meter status and error code for a specific pallet position.
	 * Uses Runnable and explicit casting for Java 8 compatibility.
	 * 
	 * @param palletQrId Pallet QR ID
	 * @param position   Meter position
	 * @param status     Meter status
	 * @param errorCode  Error code
	 */
	public void updatePalletMeterStatusByPosition(String palletQrId, int position, MeterStatus status,
			String errorCode) {
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Node foundPallet = null;
				for (AnchorPane container : allContainers) {
					foundPallet = findPalletByName(palletQrId, container);
					if (foundPallet != null)
						break; // Stop once found
				}
				if (foundPallet != null && foundPallet.getUserData() instanceof PalletController) {
					// Changed to explicit cast for Java 8 compatibility
					PalletController palletController = (PalletController) foundPallet.getUserData();
					if (status != null)
						palletController.setMeterStatus(position, status);
					if (errorCode != null)
						palletController.setErrorCodeByPosition(position, errorCode);
					ApplicationLauncher.logger.info("Updated pallet " + palletQrId + " at position " + position
							+ " with status=" + status + ", errorCode=" + errorCode);
				} else {
					ApplicationLauncher.logger.warn("Failed to update pallet: Not found: " + palletQrId);
				}
			}
		});
	}

	/**
	 * Updates meter status and error code by bay and position.
	 * Uses Runnable and explicit casting for Java 8 compatibility.
	 * 
	 * @param bayKey    Bay key
	 * @param position  Meter position
	 * @param status    Meter status
	 * @param errorCode Error code
	 */
	public void updatePalletMeterStatusByBayAndPosition(String bayKey, int position, MeterStatus status,
			String errorCode) {
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
				if (targetBay == null) {
					ApplicationLauncher.logger.warn("Invalid bay key: " + bayKey);
					return;
				}

				for (Node node : targetBay.getChildren()) {
					if (node.getUserData() instanceof PalletController) {
						// Changed to explicit cast for Java 8 compatibility
						PalletController palletController = (PalletController) node.getUserData();
						if (status != null)
							palletController.setMeterStatus(position, status);
						if (errorCode != null)
							palletController.setErrorCodeByPosition(position, errorCode);
						ApplicationLauncher.logger.info("Updated pallet in " + bayKey + " at position " + position
								+ " with status=" + status + ", errorCode=" + errorCode);
						return;
					}
				}
				ApplicationLauncher.logger.warn("Failed to update: No pallet found in " + bayKey);
			}
		});
	}

	public void updatePalletMeterStatusByBayAndPositionWithSerialNo(String bayKey, int position, String dutSerialNo,
			MeterStatus status, String errorCode) {

		ApplicationLauncher.logger.debug("updatePalletMeterStatusByBayAndPositionWithSerialNo: Entry : " + bayKey);
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane targetBay = bayKeyToPalletContainer.get(bayKey);
				if (targetBay == null) {
					ApplicationLauncher.logger.warn("Invalid bay key: " + bayKey);
					return;
				}

				for (Node node : targetBay.getChildren()) {
					if (node.getUserData() instanceof PalletController) {
						// Changed to explicit cast for Java 8 compatibility
						PalletController palletController = (PalletController) node.getUserData();
						if (status != null)
							palletController.setMeterStatus(position, status);
						if (errorCode != null)
							palletController.setErrorCodeByPosition(position, errorCode);
						if (status != null)
							palletController.setMeterStatus(position, status);
						if (dutSerialNo != null)
							palletController.setSerialByPosition(position, dutSerialNo);
						ApplicationLauncher.logger.info("Updated pallet in " + bayKey + " at position " + position
								+ " with status=" + status + ", errorCode=" + errorCode);
						return;
					}
				}
				ApplicationLauncher.logger.warn("Failed to update: No pallet found in " + bayKey);
			}
		});
	}

	/**
	 * Removes a pallet from a bay.
	 * Uses Runnable for Java 8 compatibility.
	 * 
	 * @param palletName  Pallet name
	 * @param fromBayName Source bay key
	 */
	public void removePalletByName(String palletName, String fromBayName) {
		// Changed from lambda to Runnable for Java 8 compatibility
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane fromBay = bayKeyToPalletContainer.get(fromBayName);
				if (fromBay == null) {
					logEvent("Remove failed: Invalid bay " + fromBayName);
					ApplicationLauncher.logger.warn("removePalletByName: Invalid bay " + fromBayName);
					return;
				}

				Node palletToRemove = findPalletByName(palletName, fromBay);
				if (palletToRemove != null) {
					fromBay.getChildren().remove(palletToRemove);
					logEvent("Removed " + palletName + " from " + fromBayName);
					ApplicationLauncher.logger
							.info("removePalletByName: Removed " + palletName + " from " + fromBayName);
				} else {
					logEvent("Remove failed: Pallet " + palletName + " not found in " + fromBayName);
					ApplicationLauncher.logger
							.warn("removePalletByName: Pallet " + palletName + " not found in " + fromBayName);
				}
			}
		});
	}

	/**
	 * Removes any pallet from the specified bay.
	 * 
	 * @param fromBayName The key of the bay to remove the pallet from
	 */
	public void removePalletFromBay(String fromBayName) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				AnchorPane fromBay = bayKeyToPalletContainer.get(fromBayName);
				if (fromBay == null) {
					logEvent("Remove failed: Invalid bay " + fromBayName);
					ApplicationLauncher.logger.warn("removePalletFromBay: Invalid bay " + fromBayName);
					return;
				}

				// Find the pallet node in the bay (if any)
				Node palletToRemove = fromBay.getChildren().stream()
						.filter(node -> node.getUserData() instanceof PalletController)
						.findFirst()
						.orElse(null);

				if (palletToRemove != null) {
					// Get pallet name for logging before removing
					String palletName = "Unknown";
					if (palletToRemove.getUserData() instanceof PalletController) {
						PalletController pc = (PalletController) palletToRemove.getUserData();
						palletName = pc.getPalletName().getText();
					}

					fromBay.getChildren().remove(palletToRemove);
					logEvent("Removed pallet " + palletName + " from " + fromBayName);
					ApplicationLauncher.logger.info("removePalletFromBay: Removed pallet from " + fromBayName);
				} else {
					logEvent("No pallet found to remove in " + fromBayName);
					ApplicationLauncher.logger.info("removePalletFromBay: No pallet found in " + fromBayName);
				}
			}
		});
	}

	/**
	 * Logs an event to the event log text area.
	 * 
	 * @param eventMessage Message to log
	 */
	public static void logEvent(String eventMessage) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (ref_eventLog != null) {
					String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
					ref_eventLog.appendText(timestamp + " - " + eventMessage + "\n");
				}
			}
		});
	}

	/**
	 * Inner class for error code history table entries.
	 */
	public static class ErrorCodeEntry {
		private final SimpleStringProperty errorCode;
		private final SimpleIntegerProperty count;

		public ErrorCodeEntry(String errorCode, int count) {
			this.errorCode = new SimpleStringProperty(errorCode);
			this.count = new SimpleIntegerProperty(count);
		}

		public String getErrorCode() {
			return errorCode.get();
		}

		public SimpleStringProperty errorCodeProperty() {
			return errorCode;
		}

		public int getCount() {
			return count.get();
		}

		public SimpleIntegerProperty countProperty() {
			return count;
		}
	}

	private static final Background LIGHT_GREEN_BG = new Background(
			new BackgroundFill(Color.rgb(230, 255, 230), CornerRadii.EMPTY, Insets.EMPTY) // rgb(230, 255, 230)
	);

	private static final Background LIGHT_RED_BG = new Background(
			new BackgroundFill(Color.rgb(255, 230, 230), CornerRadii.EMPTY, Insets.EMPTY) // rgb(255, 230, 230)
	);

	private static final Background DEFAULT_BG = new Background(
			new BackgroundFill(Color.rgb(244, 244, 244), CornerRadii.EMPTY, Insets.EMPTY) // rgb(244, 244, 244)
	);

	/**
	 * Change the background color of the given container to light green.
	 */
	public static void highlightGreenBay(AnchorPane bayContainer) {
		if (bayContainer != null) {
			Platform.runLater(() -> bayContainer.setBackground(LIGHT_GREEN_BG));
		}
	}

	public static void highlightRedBay(AnchorPane bayContainer) {
		if (bayContainer != null) {
			Platform.runLater(() -> bayContainer.setBackground(LIGHT_RED_BG));
		}
	}

	/**
	 * Reset the background color of the given container to default.
	 */
	public static void resetBayHighlight(AnchorPane bayContainer) {
		if (bayContainer != null) {
			Platform.runLater(() -> bayContainer.setBackground(DEFAULT_BG));
		}
	}

	/**
	 * Retrieves a map of global meter positions to their serial numbers.
	 * This method assumes access to MySqlServiceManager, PalletManage, PalletMeter,
	 * ConstantConveyor, and VerificationTestBay classes.
	 *
	 * @return A map where the key is the global position (1-based) and the value is
	 *         the meter serial number.
	 */
	public Map<Integer, String> getMeterSerialNumberMap(String presentBayKey) {
		ApplicationLauncher.logger.info("DashBoard : getMeterSerialNumberMap : Entry");

		Map<Integer, String> meterSerialNumberMap = new HashMap<>();
		// String presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;
		List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
				.findByPresentBayKeyAndPalletActive(presentBayKey);

		if (myPalletManageList.size() < 4) {
			ApplicationLauncher.logger.error("DashBoard :Not enough pallets to proceed");
			return meterSerialNumberMap;
		}

		// Order of mapping: [palletIndex, rackPosition]
		// New custom order as requested:
		// Pallet 0 positions 1-6
		// Pallet 1 positions 1-6
		// Pallet 2 positions 1-6
		// Pallet 3 positions 1-6
		int[][] customOrder = ConstantConveyor.LDU_PLACEMENT_ORDER;

		/*
		 * {
		 * {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, // Pallet 3
		 * {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, // Pallet 2
		 * {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, // Pallet 1
		 * {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6} // Pallet 0
		 * };
		 */

		for (int globalPos = 0; globalPos < customOrder.length; globalPos++) {
			int palletIndex = customOrder[globalPos][0];
			int localRackPos = customOrder[globalPos][1];

			// Ensure palletIndex is within bounds before accessing
			if (palletIndex >= myPalletManageList.size()) {
				ApplicationLauncher.logger.error("DashBoard : " + String.format(
						"Error: palletIndex %d is out of bounds for myPalletManageList (size: %d). Skipping this global position.",
						palletIndex, myPalletManageList.size()));
				meterSerialNumberMap.put(globalPos + 1, "ERROR: Invalid Pallet Index");
				continue; // Skip to the next iteration
			}

			PalletManage palletManage = myPalletManageList.get(palletIndex);
			String palletDistinctId = palletManage.getPalletDistinctId();

			Optional<PalletMeter> meterOpt = MySqlServiceManager.getPalletMeterService()
					.findByRackPositionNoAndPalletDistinctId(localRackPos, palletDistinctId);
			if (meterOpt.isPresent()) {
				PalletMeter meter = meterOpt.get();
				String serialNumber = (meter != null) ? meter.getMeterSerialNo() : "NA";
				meterSerialNumberMap.put(globalPos + 1, serialNumber);

				ApplicationLauncher.logger.info("DashBoard : " + String.format(
						"Mapped globalPos=%d => palletIndex=%d, rackPos=%d => Serial=%s",
						globalPos + 1, palletIndex, localRackPos, serialNumber));
			} else {
				ApplicationLauncher.logger.info("DashBoard : getMeterSerialNumberMap: palletDistinctId: "
						+ palletDistinctId + " ,localRackPos not found: " + localRackPos);
			}
		}

		ApplicationLauncher.logger.info("DashBoard : : Final meterSerialNumberMap = " + meterSerialNumberMap);
		return meterSerialNumberMap;
	}

	public static AnchorPane getRef_bay1Container() {
		return ref_pallet1Container;
	}

	public static void setRef_bay1Container(AnchorPane ref_bay1Container) {
		DashboardController.ref_pallet1Container = ref_bay1Container;
	}

	public static AnchorPane getRef_bay2Container() {
		return ref_pallet2Container;
	}

	public static void setRef_bay2Container(AnchorPane ref_bay2Container) {
		DashboardController.ref_pallet2Container = ref_bay2Container;
	}

	public static AnchorPane getRef_bay3Container() {
		return ref_pallet3Container;
	}

	public static void setRef_bay3Container(AnchorPane ref_bay3Container) {
		DashboardController.ref_pallet3Container = ref_bay3Container;
	}

	public static AnchorPane getRef_bay4Container() {
		return ref_pallet4Container;
	}

	public static void setRef_bay4Container(AnchorPane ref_bay4Container) {
		DashboardController.ref_pallet4Container = ref_bay4Container;
	}

	public static AnchorPane getRef_bay5Container() {
		return ref_pallet5Container;
	}

	public static void setRef_bay5Container(AnchorPane ref_bay5Container) {
		DashboardController.ref_pallet5Container = ref_bay5Container;
	}

	public static AnchorPane getRef_bay6Container() {
		return ref_pallet6Container;
	}

	public static void setRef_bay6Container(AnchorPane ref_bay6Container) {
		DashboardController.ref_pallet6Container = ref_bay6Container;
	}

	public static AnchorPane getRef_bay7Container() {
		return ref_pallet7Container;
	}

	public static void setRef_bay7Container(AnchorPane ref_bay7Container) {
		DashboardController.ref_pallet7Container = ref_bay7Container;
	}

	public static AnchorPane getRef_bay8Container() {
		return ref_pallet8Container;
	}

	public static void setRef_bay8Container(AnchorPane ref_bay8Container) {
		DashboardController.ref_pallet8Container = ref_bay8Container;
	}

	public static AnchorPane getRef_bay9Container() {
		return ref_pallet9Container;
	}

	public static void setRef_bay9Container(AnchorPane ref_bay9Container) {
		DashboardController.ref_pallet9Container = ref_bay9Container;
	}

	public static AnchorPane getRef_bay10Container() {
		return ref_pallet10Container;
	}

	public static void setRef_bay10Container(AnchorPane ref_bay10Container) {
		DashboardController.ref_pallet10Container = ref_bay10Container;
	}

	public static AnchorPane getRef_bay11Container() {
		return ref_pallet11Container;
	}

	public static void setRef_bay11Container(AnchorPane ref_bay11Container) {
		DashboardController.ref_pallet11Container = ref_bay11Container;
	}

	public static AnchorPane getRef_bay12Container() {
		return ref_pallet12Container;
	}

	public static void setRef_bay12Container(AnchorPane ref_bay12Container) {
		DashboardController.ref_pallet12Container = ref_bay12Container;
	}

	public static AnchorPane getRef_bay13Container() {
		return ref_pallet13Container;
	}

	public static void setRef_bay13Container(AnchorPane ref_bay13Container) {
		DashboardController.ref_pallet13Container = ref_bay13Container;
	}

	public static AnchorPane getRef_bay14Container() {
		return ref_pallet14Container;
	}

	public static void setRef_bay14Container(AnchorPane ref_bay14Container) {
		DashboardController.ref_pallet14Container = ref_bay14Container;
	}

	public static AnchorPane getRef_bay15Container() {
		return ref_pallet15Container;
	}

	public static void setRef_bay15Container(AnchorPane ref_bay15Container) {
		DashboardController.ref_pallet15Container = ref_bay15Container;
	}

	public static AnchorPane getRef_bay16Container() {
		return ref_pallet16Container;
	}

	public static void setRef_bay16Container(AnchorPane ref_bay16Container) {
		DashboardController.ref_pallet16Container = ref_bay16Container;
	}

	public static AnchorPane getRef_bay17Container() {
		return ref_pallet17Container;
	}

	public static void setRef_bay17Container(AnchorPane ref_bay17Container) {
		DashboardController.ref_pallet17Container = ref_bay17Container;
	}

	public static AnchorPane getRef_bay18Container() {
		return ref_pallet18Container;
	}

	public static void setRef_bay18Container(AnchorPane ref_bay18Container) {
		DashboardController.ref_pallet18Container = ref_bay18Container;
	}

	public static AnchorPane getRef_bay19Container() {
		return ref_pallet19Container;
	}

	public static void setRef_bay19Container(AnchorPane ref_bay19Container) {
		DashboardController.ref_pallet19Container = ref_bay19Container;
	}

	public static AnchorPane getRef_bay20Container() {
		return ref_pallet20Container;
	}

	public static void setRef_bay20Container(AnchorPane ref_bay20Container) {
		DashboardController.ref_pallet20Container = ref_bay20Container;
	}

	public static AnchorPane getRef_rejectionBayContainer() {
		return ref_rejectionPalletContainer;
	}

	public static void setRef_rejectionBayContainer(AnchorPane ref_rejectionBayContainer) {
		DashboardController.ref_rejectionPalletContainer = ref_rejectionBayContainer;
	}

	public static AnchorPane getRef_unloadingBayContainer() {
		return ref_unloadingPalletContainer;
	}

	public static void setRef_unloadingBayContainer(AnchorPane ref_unloadingBayContainer) {
		DashboardController.ref_unloadingPalletContainer = ref_unloadingBayContainer;
	}

	private void initAllBayView() {

		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.FT_BAY_KEY);

		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.HV_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.IR_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.CALIBRATION_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.WAITING_PP4_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.WAITING_PP3_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.WAITING_PP2_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.WAITING_PP1_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.VERIFICATION_PP4_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.VERIFICATION_PP3_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.VERIFICATION_PP2_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);
		// bayKeyToContainer.put(ConstantConveyor.STA_NLD1_BAY_KEY, bay13Container);

		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD1_PP1_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD1_PP2_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD1_PP3_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD1_PP4_BAY_KEY);

		// bayKeyToContainer.put(ConstantConveyor.STA_NLD2_BAY_KEY, bay17Container);

		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD2_PP1_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD2_PP2_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD2_PP3_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.STA_NLD2_PP4_BAY_KEY);

		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.REJECTION_BAY_KEY);
		getBayIndicatorManager().addDefaultBayView(ConstantConveyor.UNLOADING_BAY_KEY);

		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.CALIBRATION_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setEntryStopperOpenIndicatorVisible(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setExitStopperOpenIndicatorVisible(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.CALIBRATION_BAY_KEY, false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.WAITING_PP1_BAY_KEY, false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY,
				false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY,
				false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY,
				false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.VERIFICATION_PP1_BAY_KEY,
				false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);

		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setPalletsExistInQueueIndicatorVisible(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.WAITING_PP4_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.WAITING_PP3_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.WAITING_PP2_BAY_KEY,
				false);

		getBayIndicatorManager()
				.setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager()
				.setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager()
				.setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);

		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY,
				false);

		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY,
				false);

		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.REJECTION_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInTargetBayIndicatorVisible(ConstantConveyor.UNLOADING_BAY_KEY,
				false);

		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);

		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY,
				false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY,
				false);

		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);

		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setAllPalletsExistInBayIndicatorVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);

		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.CALIBRATION_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setHaltImageDisplayOn(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.CALIBRATION_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setNoEntryImageDisplayOn(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.CALIBRATION_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setByPassImageDisplayOn(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.CALIBRATION_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setPalletsLockedImageDisplayOn(ConstantConveyor.UNLOADING_BAY_KEY, false);

		for (String eachBay : ConstantConveyor.ALL_BAY_LIST) {
			getBayIndicatorManager().setTimeUpDisplayVisible(eachBay, true);
			getBayIndicatorManager().setTpCountStatusVisible(eachBay, true);
			getBayIndicatorManager().setProgressBarIndicatorVisible(eachBay, false);
		}

		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);

		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);

		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setTimeUpDisplayVisible(ConstantConveyor.UNLOADING_BAY_KEY, false);

		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.FT_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.HV_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.IR_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.CALIBRATION_BAY_KEY, false);

		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.VERIFICATION_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.VERIFICATION_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.VERIFICATION_PP4_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD1_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD1_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD1_PP4_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD2_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD2_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.STA_NLD2_PP4_BAY_KEY, false);

		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.WAITING_PP1_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.WAITING_PP2_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.WAITING_PP3_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.WAITING_PP4_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.REJECTION_BAY_KEY, false);
		getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.UNLOADING_BAY_KEY, false);

		// getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.FT_BAY_KEY,true);
		// getBayIndicatorManager().setProgressBarIndicatorVisible(ConstantConveyor.FT_BAY_KEY,true);

		// getBayIndicatorManager().setTpCountStatusVisible(ConstantConveyor.VERIFICATION_PP1_BAY_KEY,true);
		// getBayIndicatorManager().updateTpCountStatus(ConstantConveyor.VERIFICATION_PP1_BAY_KEY,2,16);
		// getBayIndicatorManager().updateTpCountStatus(ConstantConveyor.STA_NLD1_PP1_BAY_KEY,0,2);
		// getBayIndicatorManager().updateTpCountStatus(ConstantConveyor.STA_NLD2_PP1_BAY_KEY,0,2);

		getBayIndicatorManager().setProgressBarIndicatorVisible(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, true);
		getBayIndicatorManager().setProgressBarIndicatorVisible(ConstantConveyor.STA_NLD1_PP1_BAY_KEY, true);
		getBayIndicatorManager().setProgressBarIndicatorVisible(ConstantConveyor.STA_NLD2_PP1_BAY_KEY, true);

		// getBayIndicatorManager().startProgressBarWithTime(ConstantConveyor.STA_NLD1_PP1_BAY_KEY,60);
		// getBayIndicatorManager().startProgressBarWithTime(ConstantConveyor.STA_NLD2_PP1_BAY_KEY,120);
		// getBayIndicatorManager().startProgressBarWithTpCount(ConstantConveyor.VERIFICATION_PP1_BAY_KEY);

		// getBayIndicatorManager().startTimeUpDisplay(ConstantConveyor.FT_BAY_KEY);
		// resetEntryStopperOpenIndicator(ConstantConveyor.FT_BAY_KEY);
		// resetExitStopperOpenIndicator(ConstantConveyor.FT_BAY_KEY);
		// updateBayEntryStopper(ConstantConveyor.FT_BAY_KEY, false);
		// updateBayExitStopper(ConstantConveyor.FT_BAY_KEY, false);
		// updateBayMonitoringAllPalletsExistInBay(ConstantConveyor.FT_BAY_KEY);
		// updateBayMonitoringAllPalletsExistInTargetBayIndicator(ConstantConveyor.FT_BAY_KEY);
		// updateBayMonitoringPalletsExistInQueueIndicator(ConstantConveyor.FT_BAY_KEY);

	}

	public void guiInit() {

		// tv_FTErrorCodeHistory.setVisible(false);
		Platform.runLater(() -> {
			// ref_btnDummy.setVisible(false);
		});
	}

	// METRICS TABLE UPDATE
	@FXML
	private void addDummySampleData() {

		getBayIndicatorManager().updateTpCountStatus(ConstantConveyor.VERIFICATION_PP1_BAY_KEY, 8);

	}

	@FXML
	public void refreshMetricsTable() {
		if (conveyorOutputMetricsSummaryService == null) {
			throw new IllegalStateException("conveyorOutputMetricsSummaryService not initialized");
		}

		String selectedPeriod = periodComboBox.getValue();
		refreshMetricsByPeriod(ConstantConveyor.REJECTION_BAY_KEY, failedMetersRJ, passedMetersRJ,
				failedMetersRjPercent, passedMetersRjPercent, throughputRJ, totalNoOfMetersRJ, selectedPeriod);
		refreshMetricsByPeriod(ConstantConveyor.UNLOADING_BAY_KEY, failedMetersUL, passedMetersUL,
				failedMetersUlPercent, passedMetersUlPercent, throughputUL, totalNoOfMetersUL, selectedPeriod);
	}

	private void updateMetricsTable() {
		updateMetricsByPeriod(ConstantConveyor.REJECTION_BAY_KEY, failedMetersRJ, passedMetersRJ,
				failedMetersRjPercent, passedMetersRjPercent, throughputRJ, totalNoOfMetersRJ);
		updateMetricsByPeriod(ConstantConveyor.UNLOADING_BAY_KEY, failedMetersUL, passedMetersUL,
				failedMetersUlPercent, passedMetersUlPercent,
				throughputUL, totalNoOfMetersUL);
	}

	private void updateMetricsByPeriod(String bayType, TextField failedField, TextField passedField,
			TextField failedPercentField, TextField passedPercentField,
			TextField throughputField, TextField totalField) {
		LocalDateTime now = LocalDateTime.now();

		List<ConveyorOutputMetricsSummary> summaries = conveyorOutputMetricsSummaryService.findByBayType(bayType);
		List<ConveyorOutputMetricsSummary> today = filterByDays(summaries, now.minusDays(1));
		List<ConveyorOutputMetricsSummary> week = filterByDays(summaries, now.minusWeeks(1));
		List<ConveyorOutputMetricsSummary> month = filterByDays(summaries, now.minusMonths(1));
		List<ConveyorOutputMetricsSummary> year = filterByDays(summaries, now.minusYears(1));
		List<ConveyorOutputMetricsSummary> fiveYears = filterByDays(summaries, now.minusYears(5));
		List<ConveyorOutputMetricsSummary> lifetime = summaries;

		List<ConveyorOutputMetricsSummary> currentPeriod = today; // Replace with actual UI selection if needed

		int failed = currentPeriod.stream().mapToInt(m -> m.getFailedMeters() != null ? m.getFailedMeters() : 0).sum();
		int passed = currentPeriod.stream().mapToInt(m -> m.getPassedMeters() != null ? m.getPassedMeters() : 0).sum();
		int total = currentPeriod.stream().mapToInt(m -> m.getTotalNoOfMeters() != null ? m.getTotalNoOfMeters() : 0)
				.sum();
		double throughput = currentPeriod.stream()
				.mapToDouble(m -> m.getAverageHourlyOutput() != null ? m.getAverageHourlyOutput() : 0).average()
				.orElse(0.0);

		OptionalDouble passedPercentOpt = currentPeriod.stream()
				.mapToDouble(m -> m.getPassedPercentage() != null ? m.getPassedPercentage() : 0.0)
				// .filter(value -> value != 0.0)
				.average();
		if (passedPercentOpt.isPresent()) {
			double passedPercent = passedPercentOpt.getAsDouble();
			// ApplicationLauncher.logger.debug("Dummy pallet added: " + passCount + " PASS,
			// " + failCount + " FAIL (Total: " + totalMeters + ")");

			passedPercentField.setText(String.format("%.2f", passedPercent) + "%");
		} else {
			passedPercentField.setText("");
		}

		OptionalDouble failedPercentOpt = currentPeriod.stream()
				.mapToDouble(m -> m.getFailedPercentage() != null ? m.getFailedPercentage() : 0.0)
				// .filter(value -> value != 0.0)
				.average();
		if (failedPercentOpt.isPresent()) {
			double failedPercent = failedPercentOpt.getAsDouble();
			failedPercentField.setText(String.format("%.2f", failedPercent) + "%");
		} else {
			failedPercentField.setText("");
		}

		failedField.setText(String.valueOf(failed));
		passedField.setText(String.valueOf(passed));
		totalField.setText(String.valueOf(total));

		throughputField.setText(String.format("%.2f", throughput));
	}

	private void refreshMetricsByPeriod(String bayType, TextField failedField, TextField passedField,
			TextField failedPercentField, TextField passedPercentField,
			TextField throughputField, TextField totalField, String period) {
		LocalDateTime now = LocalDateTime.now();

		List<ConveyorOutputMetricsSummary> summaries = conveyorOutputMetricsSummaryService.findByBayType(bayType);
		List<ConveyorOutputMetricsSummary> currentPeriod;

		switch (period) {
			case "Today":
				currentPeriod = filterByDays(summaries, now.toLocalDate().atStartOfDay());
				break;
			case "Yesterday":
				LocalDateTime startYesterday = now.minusDays(1).toLocalDate().atStartOfDay();
				// LocalDateTime endYesterday = now.toLocalDate().atStartOfDay();
				LocalDateTime endYesterday = now.minusDays(1).toLocalDate().atTime(LocalTime.MAX);
				currentPeriod = filterByPeriod(summaries, startYesterday, endYesterday);
				break;
			case "This Week":
				// currentPeriod = filterByDays(summaries, now.minusWeeks(1));
				LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate();
				currentPeriod = filterByPeriod(
						summaries,
						startOfWeek.atStartOfDay(),
						now // include up to the current date/time
				);
				break;
			case "Last Week":
				/*
				 * LocalDateTime startLastWeek = now.minusWeeks(2);
				 * LocalDateTime endLastWeek = now.minusWeeks(1);
				 * currentPeriod = filterByPeriod(summaries, startLastWeek, endLastWeek);
				 */

				LocalDate startLastWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate()
						.minusWeeks(1);
				LocalDate endLastWeek = startLastWeek.plusDays(6); // Saturday
				currentPeriod = filterByPeriod(
						summaries,
						startLastWeek.atStartOfDay(),
						endLastWeek.atTime(LocalTime.MAX));
				break;

			case "Last 7 Days":
				LocalDate startLast7Days = now.toLocalDate().minusDays(7);
				LocalDate endLast7Days = now.toLocalDate().minusDays(1); // yesterday
				currentPeriod = filterByPeriod(
						summaries,
						startLast7Days.atStartOfDay(),
						endLast7Days.atTime(LocalTime.MAX));
				break;
			case "This Month":
				currentPeriod = filterByDays(summaries, now.withDayOfMonth(1));
				/*
				 * currentPeriod.forEach(m -> {
				 * ApplicationLauncher.logger.debug(m.getDateH() + " | " +
				 * m.getTotalNoOfMeters() + " | " +
				 * m.getPassedMeters() + " | " + m.getFailedMeters() +
				 * " | " + m.getAverageHourlyOutput());
				 * });
				 */
				break;
			case "Last Month":
				LocalDateTime startLastMonth = now.minusMonths(1).withDayOfMonth(1);
				LocalDateTime endLastMonth = now.withDayOfMonth(1);
				currentPeriod = filterByPeriod(summaries, startLastMonth, endLastMonth);
				break;
			case "This Year":
				currentPeriod = filterByDays(summaries, now.withDayOfYear(1));
				break;
			case "Last Year":
				LocalDateTime startLastYear = now.minusYears(1).withDayOfYear(1);
				LocalDateTime endLastYear = now.withDayOfYear(1);
				currentPeriod = filterByPeriod(summaries, startLastYear, endLastYear);
				break;
			case "All Time":
			default:
				currentPeriod = summaries;
				break;
		}

		ApplicationLauncher.logger.debug("refreshMetricsByPeriod : currentPeriod (" + period + ") : " + currentPeriod);

		int failed = currentPeriod.stream().mapToInt(m -> m.getFailedMeters() != null ? m.getFailedMeters() : 0).sum();
		int passed = currentPeriod.stream().mapToInt(m -> m.getPassedMeters() != null ? m.getPassedMeters() : 0).sum();
		int total = currentPeriod.stream().mapToInt(m -> m.getTotalNoOfMeters() != null ? m.getTotalNoOfMeters() : 0)
				.sum();
		double throughput = currentPeriod.stream()
				.mapToDouble(m -> m.getAverageHourlyOutput() != null ? m.getAverageHourlyOutput() : 0).average()
				.orElse(0.0);

		failedField.setText(String.valueOf(failed));
		passedField.setText(String.valueOf(passed));
		totalField.setText(String.valueOf(total));
		throughputField.setText(String.format("%.2f", throughput));

		OptionalDouble passedPercentOpt = currentPeriod.stream()
				.mapToDouble(m -> m.getPassedPercentage() != null ? m.getPassedPercentage() : 0.0)
				// .filter(value -> value != 0.0)
				.average();
		if (passedPercentOpt.isPresent()) {
			double passedPercent = passedPercentOpt.getAsDouble();
			// ApplicationLauncher.logger.debug("Dummy pallet added: " + passCount + " PASS,
			// " + failCount + " FAIL (Total: " + totalMeters + ")");

			passedPercentField.setText(String.format("%.2f", passedPercent) + "%");
		} else {
			passedPercentField.setText("");
		}

		OptionalDouble failedPercentOpt = currentPeriod.stream()
				.mapToDouble(m -> m.getFailedPercentage() != null ? m.getFailedPercentage() : 0.0)
				// .filter(value -> value != 0.0)
				.average();
		if (failedPercentOpt.isPresent()) {
			double failedPercent = failedPercentOpt.getAsDouble();
			failedPercentField.setText(String.format("%.2f", failedPercent) + "%");
		} else {
			failedPercentField.setText("");
		}

	}

	private List<ConveyorOutputMetricsSummary> filterByDays(
			List<ConveyorOutputMetricsSummary> list,
			LocalDateTime from) {
		// Convert input (LocalDateTime) to LocalDate (since dateH has no time)
		LocalDate filterDate = from.toLocalDate();

		return list.stream()
				.filter(m -> {
					if (m.getDateH() == null)
						return false;

					/*
					 * // Convert Date (dateH) to LocalDate
					 * LocalDate recordDate = m.getDateH().toInstant()
					 * .atZone(ZoneId.systemDefault())
					 * .toLocalDate();
					 */

					LocalDate recordDate = new java.util.Date(m.getDateH().getTime())
							.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();

					// Check if record date is on or after the filter date
					return !recordDate.isBefore(filterDate);
				})
				.collect(Collectors.toList());
	}

	private List<ConveyorOutputMetricsSummary> filterByPeriod(
			List<ConveyorOutputMetricsSummary> list,
			LocalDateTime from,
			LocalDateTime to) {
		// Extract just the date part (ignore time)
		LocalDate fromDate = from.toLocalDate();
		LocalDate toDate = to.toLocalDate();

		return list.stream()
				.filter(m -> {
					if (m.getDateH() == null)
						return false;

					LocalDate recordDate = new java.util.Date(m.getDateH().getTime())
							.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();

					// Check if the record date is within the range (inclusive of from and to)
					return !recordDate.isBefore(fromDate) && !recordDate.isAfter(toDate);
				})
				.collect(Collectors.toList());
	}

	@FXML
	private void onPeriodSelectionChanged() {
		refreshMetricsTable();
	}

	//

	public BayIndicatorManager getBayIndicatorManager() {
		return bayIndicatorManager;
	}
}