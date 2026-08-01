package com.tasnetwork.calibration.conveyor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayStateEngine;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayBypass;
import com.tasnetwork.calibration.conveyor.bay.comm.Comm;
import com.tasnetwork.calibration.conveyor.bay.comm.CommBayBypass;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.rejection.Rejection;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Bypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.StaNld_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Bypass;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.StaNld_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayBypass;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.VerificWaiting;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

//import groovyjarjarantlr4.v4.parse.ANTLRParser.v3tokenSpec_return;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class StateExecutorController
		implements com.tasnetwork.calibration.conveyor.dashboard.IBayUIController, Initializable {

	private static volatile StateExecutorController instance;

	public static StateExecutorController getInstance() {
		return instance;
	}

	BayUtils bayUtils = new BayUtils();
	public static final Boolean ON = true;
	public static final Boolean OFF = false;

	public static final boolean simulateHappyPath = /* false; */ true;
	public static final boolean simulateFtBayHappyPath = false; // true;
	public static final boolean simulateHvBayHappyPath = false; // true;
	public static final boolean simulateIrBayHappyPath = false; // true;
	public static final boolean simulateCalibBayHappyPath = false; // true;
	public static final boolean simulateCommBayHappyPath = false; // true;
	public static final boolean simulateLoadingBayHappyPath = false; // true;
	public static final boolean simulateRejectionBayHappyPath = false; // true;
	public static final boolean simulateSCTNLTBay1HappyPath = false; // true;
	public static final boolean simulateSCTNLTBay2HappyPath = false; // true;
	public static final boolean simulateUnloadingBayHappyPath = false; // true;
	public static final boolean simulateVerificBayHappyPath = false; // true;
	public static final boolean simulateWaitingBayHappyPath = false; // true;

	public static final boolean simulateHvBayResult = true;
	public static final boolean simulateIrBayResult = true;

	@FXML
	private Button btnFtStart;
	@FXML
	private Button btnFtStop;
	@FXML
	private Button btnFtReset;
	@FXML
	private Button btnFtBayBypass;
	@FXML
	private Button btnHvtStart;
	@FXML
	private Button btnHvtStop;
	@FXML
	private Button btnHvtReset;
	@FXML
	private Button btnHvtBayBypass;
	@FXML
	private Button btnIrtStart;
	@FXML
	private Button btnIrtStop;
	@FXML
	private Button btnIrtReset;
	@FXML
	private Button btnIrtBayBypass;
	@FXML
	private Button btnCalibStart;
	@FXML
	private Button btnCalibStop;
	@FXML
	private Button btnCalibReset;
	@FXML
	private Button btnCalibBayBypass;
	@FXML
	private Button btnWaitingBayStart;
	@FXML
	private Button btnWaitingBayStop;
	@FXML
	private Button btnWaitingBayReset;
	@FXML
	private Button btnWaitingBayBypass;
	@FXML
	private Button btnVerificTestStart;
	@FXML
	private Button btnVerificTestStop;
	@FXML
	private Button btnVerificTestReset;
	@FXML
	private Button btnVerificTestBayBypass;
	@FXML
	private Button btnSctNlt1Start;
	@FXML
	private Button btnSctNlt1Stop;
	@FXML
	private Button btnSctNlt1Reset;
	@FXML
	private Button btnSctNlt1BayBypass;
	@FXML
	private Button btnSctNlt2Start;
	@FXML
	private Button btnSctNlt2Stop;
	@FXML
	private Button btnSctNlt2Reset;
	@FXML
	private Button btnSctNlt2BayBypass;
	@FXML
	private Button btnCommTestStart;
	@FXML
	private Button btnCommTestStop;
	@FXML
	private Button btnCommTestReset;
	@FXML
	private Button btnCommTestBayBypass;
	@FXML
	private Button btnRejectStart;
	@FXML
	private Button btnRejectStop;
	@FXML
	private Button btnRejectReset;
	@FXML
	private Button btnRejectBayBypass;

	// Static references
	public static Button BTN_FT_START;
	public static Button BTN_FT_STOP;
	public static Button BTN_FT_RESET;
	public static Button BTN_FT_BAY_BYPASS;
	public static Button BTN_HVT_START;
	public static Button BTN_HVT_STOP;
	public static Button BTN_HVT_RESET;
	public static Button BTN_HVT_BAY_BYPASS;
	public static Button BTN_IRT_START;
	public static Button BTN_IRT_STOP;
	public static Button BTN_IRT_RESET;
	public static Button BTN_IRT_BAY_BYPASS;
	public static Button BTN_CALIB_START;
	public static Button BTN_CALIB_STOP;
	public static Button BTN_CALIB_RESET;
	public static Button BTN_CALIB_BAY_BYPASS;
	public static Button BTN_VERIFIC_TEST_START;
	public static Button BTN_VERIFIC_TEST_STOP;
	public static Button BTN_VERIFIC_TEST_RESET;
	public static Button BTN_VERIFIC_TEST_BAY_BYPASS;
	public static Button BTN_SCT_NLT1_START;
	public static Button BTN_SCT_NLT1_STOP;
	public static Button BTN_SCT_NLT1_RESET;
	public static Button BTN_SCT_NLT1_BAY_BYPASS;
	public static Button BTN_SCT_NLT2_START;
	public static Button BTN_SCT_NLT2_STOP;
	public static Button BTN_SCT_NLT2_RESET;
	public static Button BTN_SCT_NLT2_BAY_BYPASS;
	public static Button BTN_COMM_TEST_START;
	public static Button BTN_COMM_TEST_STOP;
	public static Button BTN_COMM_TEST_RESET;
	public static Button BTN_COMM_TEST_BAY_BYPASS;
	public static Button BTN_REJECT_START;
	public static Button BTN_REJECT_STOP;
	public static Button BTN_REJECT_RESET;
	public static Button BTN_REJECT_BAY_BYPASS;

	// Motor 'Off' buttons
	@FXML
	private Button btn_offMotor1;
	@FXML
	private Button btn_offMotor2;
	@FXML
	private Button btn_offMotor3;
	@FXML
	private Button btn_offMotor4;
	@FXML
	private Button btn_offMotor5;
	@FXML
	private Button btn_offMotor6;
	@FXML
	private Button btn_offMotor7;
	@FXML
	private Button btn_offMotor8;
	@FXML
	private Button btn_offMotor9;

	// Motor 'On' buttons
	@FXML
	private Button btn_onMotor1;
	@FXML
	private Button btn_onMotor2;
	@FXML
	private Button btn_onMotor3;
	@FXML
	private Button btn_onMotor4;
	@FXML
	private Button btn_onMotor5;
	@FXML
	private Button btn_onMotor6;
	@FXML
	private Button btn_onMotor7;
	@FXML
	private Button btn_onMotor8;
	@FXML
	private Button btn_onMotor9;

	// Static references for 'Off' buttons
	@FXML
	private static Button ref_btn_offMotor1;
	@FXML
	private static Button ref_btn_offMotor2;
	@FXML
	private static Button ref_btn_offMotor3;
	@FXML
	private static Button ref_btn_offMotor4;
	@FXML
	private static Button ref_btn_offMotor5;
	@FXML
	private static Button ref_btn_offMotor6;
	@FXML
	private static Button ref_btn_offMotor7;
	@FXML
	private static Button ref_btn_offMotor8;
	@FXML
	private static Button ref_btn_offMotor9;

	// Static references for 'On' buttons
	@FXML
	private static Button ref_btn_onMotor1;
	@FXML
	private static Button ref_btn_onMotor2;
	@FXML
	private static Button ref_btn_onMotor3;
	@FXML
	private static Button ref_btn_onMotor4;
	@FXML
	private static Button ref_btn_onMotor5;
	@FXML
	private static Button ref_btn_onMotor6;
	@FXML
	private static Button ref_btn_onMotor7;
	@FXML
	private static Button ref_btn_onMotor8;
	@FXML
	private static Button ref_btn_onMotor9;

	@FXML
	private Button btn_CalibPlace;
	private static Button ref_btn_CalibPlace;

	@FXML
	private Button btn_CalibRemove;
	private static Button ref_btn_CalibRemove;

	@FXML
	private Button btn_CalibCurrentStable;
	private static Button ref_btn_CalibCurrentStable;

	@FXML
	private Button btn_FtPlace;
	private static Button ref_btn_FtPlace;

	@FXML
	private Button btn_FtRemove;
	private static Button ref_btn_FtRemove;

	@FXML
	private Button btn_Ft_LDUOK;
	private static Button ref_btn_Ft_LDUOK;

	@FXML
	private Button btn_VerificDone;
	private static Button ref_btn_VerificDone;

	@FXML
	private TabPane tpTestStatusBays;

	@FXML
	private Button btnFilter;

	@FXML
	private TextField tf_CALIB_prompt;
	public static TextField ref_tf_CALIB_prompt;

	@FXML
	private TextField tf_FT_prompt;
	public static TextField ref_tf_FT_prompt;

	@FXML
	private TextField tf_VERIFIC_prompt;
	public static TextField ref_tf_VERIFIC_prompt;

	private static Button ref_btnFilter;

	@FXML
	private ComboBox cmbBxFilterPosition;
	private static ComboBox ref_cmbBxFilterPosition;

	public static ArrayList<TestInterfaceStatus> allData = new ArrayList<TestInterfaceStatus>();

	static AtomicInteger serialNoTestStatusAtomic = new AtomicInteger(1);

	Timer funtionalBayStartTaskTimer;
	Timer calibrationStartTaskTimer;
	Timer insResStartTaskTimer;
	Timer hvtBayStartTaskTimer;
	Timer verificStartTaskTimer;
	Timer commStartTaskTimer;
	Timer sctNlt1StartTaskTimer;
	Timer sctNlt2StartTaskTimer;
	Timer waitingBayStartTaskTimer;
	Timer rejectionBayStartTaskTimer;

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

	Timer funtionalBayResetTaskTimer;
	Timer calibrationResetTaskTimer;
	Timer insResResetTaskTimer;
	Timer hvtBayResetTaskTimer;
	Timer verificResetTaskTimer;
	Timer commResetTaskTimer;
	Timer sctNlt1ResetTaskTimer;
	Timer sctNlt2ResetTaskTimer;
	Timer waitingBayResetTaskTimer;
	Timer rejectionBayResetTaskTimer;

	Timer funtionalBayBypassTaskTimer;
	Timer hvtBayBypassTaskTimer; // Added for HV Bay Bypass
	Timer irtBayBypassTaskTimer; // Added for IR Bay Bypass
	Timer calibBayBypassTaskTimer; // Added for Calib Bay Bypass
	Timer verificTestBayBypassTaskTimer; // Added for Verification Bay Bypass
	Timer sctNlt1BayBypassTaskTimer; // Added for SCT NLT1 Bay Bypass
	Timer sctNlt2BayBypassTaskTimer; // Added for SCT NLT2 Bay Bypass
	Timer commTestBayBypassTaskTimer; // Added for Comm Test Bay Bypass
	Timer rejectBayBypassTaskTimer; // Added for Rejection Bay Bypass
	Timer waitingBayBypassTaskTimer; // Added for Waiting Bay Bypass

	// Timer btnRefreshTaskTimer;
	Timer btnFilterTaskTimer;
	Timer btnSampleDataTaskTimer;

	@FXML
	private Button btnAllStart;
	@FXML
	private Button btnAllStop;
	@FXML
	private Button btnAllReset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance().registerController(this);
		instance = this;
		setupTestStatusTabs();

		refInit();
		guiInit();
		dataSetupInit();
	}

	public void dataSetupInit() {

	}

	public void guiInit() {

		// F I L T E R C O M B O B O X
		// ====================================================================
		ref_cmbBxFilterPosition.getItems().clear();
		ref_cmbBxFilterPosition.getItems().add("All");
		for (int i = 0; i <= 6; i++) {
			ref_cmbBxFilterPosition.getItems().add(i);
		}

		btn_CalibPlace.setDisable(true);
		btn_CalibRemove.setDisable(true);
		btn_CalibCurrentStable.setDisable(true);
		btn_FtPlace.setDisable(true);
		btn_FtRemove.setDisable(true);
		btn_Ft_LDUOK.setDisable(true);
		btn_VerificDone.setDisable(true);

	}

	public void refInit() {

		ref_btnFilter = btnFilter;

		ref_tf_CALIB_prompt = tf_CALIB_prompt;
		ref_tf_FT_prompt = tf_FT_prompt;
		ref_tf_VERIFIC_prompt = tf_VERIFIC_prompt;

		ref_cmbBxFilterPosition = cmbBxFilterPosition;

		ref_btn_CalibPlace = btn_CalibPlace;
		ref_btn_CalibRemove = btn_CalibRemove;
		ref_btn_CalibCurrentStable = btn_CalibCurrentStable;
		ref_btn_FtPlace = btn_FtPlace;
		ref_btn_FtRemove = btn_FtRemove;
		ref_btn_Ft_LDUOK = btn_Ft_LDUOK;
		ref_btn_VerificDone = btn_VerificDone;

		BTN_FT_START = btnFtStart;
		BTN_FT_STOP = btnFtStop;
		BTN_FT_RESET = btnFtReset;
		BTN_FT_BAY_BYPASS = btnFtBayBypass;

		BTN_HVT_START = btnHvtStart;
		BTN_HVT_STOP = btnHvtStop;
		BTN_HVT_RESET = btnHvtReset;
		BTN_HVT_BAY_BYPASS = btnHvtBayBypass; // Added static reference

		BTN_IRT_START = btnIrtStart;
		BTN_IRT_STOP = btnIrtStop;
		BTN_IRT_RESET = btnIrtReset;
		BTN_IRT_BAY_BYPASS = btnIrtBayBypass; // Added static reference

		BTN_CALIB_START = btnCalibStart;
		BTN_CALIB_STOP = btnCalibStop;
		BTN_CALIB_RESET = btnCalibReset;
		BTN_CALIB_BAY_BYPASS = btnCalibBayBypass; // Added static reference

		BTN_VERIFIC_TEST_START = btnVerificTestStart;
		BTN_VERIFIC_TEST_STOP = btnVerificTestStop;
		BTN_VERIFIC_TEST_RESET = btnVerificTestReset;
		BTN_VERIFIC_TEST_BAY_BYPASS = btnVerificTestBayBypass; // Added static reference

		BTN_SCT_NLT1_START = btnSctNlt1Start;
		BTN_SCT_NLT1_STOP = btnSctNlt1Stop;
		BTN_SCT_NLT1_RESET = btnSctNlt1Reset;
		BTN_SCT_NLT1_BAY_BYPASS = btnSctNlt1BayBypass; // Added static reference

		BTN_SCT_NLT2_START = btnSctNlt2Start;
		BTN_SCT_NLT2_STOP = btnSctNlt2Stop;
		BTN_SCT_NLT2_RESET = btnSctNlt2Reset;
		BTN_SCT_NLT2_BAY_BYPASS = btnSctNlt2BayBypass; // Added static reference

		BTN_COMM_TEST_START = btnCommTestStart;
		BTN_COMM_TEST_STOP = btnCommTestStop;
		BTN_COMM_TEST_RESET = btnCommTestReset;
		BTN_COMM_TEST_BAY_BYPASS = btnCommTestBayBypass; // Added static reference

		BTN_REJECT_START = btnRejectStart;
		BTN_REJECT_STOP = btnRejectStop;
		BTN_REJECT_RESET = btnRejectReset;
		BTN_REJECT_BAY_BYPASS = btnRejectBayBypass; // Added static reference

		ref_btn_offMotor1 = btn_offMotor1;
		ref_btn_offMotor2 = btn_offMotor2;
		ref_btn_offMotor3 = btn_offMotor3;
		ref_btn_offMotor4 = btn_offMotor4;
		ref_btn_offMotor5 = btn_offMotor5;
		ref_btn_offMotor6 = btn_offMotor6;
		ref_btn_offMotor7 = btn_offMotor7;
		ref_btn_offMotor8 = btn_offMotor8;
		ref_btn_offMotor9 = btn_offMotor9;

		ref_btn_onMotor1 = btn_onMotor1;
		ref_btn_onMotor2 = btn_onMotor2;
		ref_btn_onMotor3 = btn_onMotor3;
		ref_btn_onMotor4 = btn_onMotor4;
		ref_btn_onMotor5 = btn_onMotor5;
		ref_btn_onMotor6 = btn_onMotor6;
		ref_btn_onMotor7 = btn_onMotor7;
		ref_btn_onMotor8 = btn_onMotor8;
		ref_btn_onMotor9 = btn_onMotor9;
	}

	// ============================================================================================================================================

	@FXML
	public void btnRjStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.REJECTION_BAY_KEY);
	}

	@FXML
	public void btnRjStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.REJECTION_BAY_KEY);
	}

	@FXML
	public void btnRjResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.REJECTION_BAY_KEY);
	}

	@FXML
	public void btnRejectBayBypassOnClick() {
		Rejection.logger.info("btnRejectBayBypassOnClick : Invoked:");

		// F L A G S
		Rejection.setStartProcessRequestedRejectionBay(true);

		Rejection.setStopProcessCompletedRejectionBay(false);
		Rejection.setStopProcessRequestedRejectionBay(false);

		Rejection.setResetProcessCompletedRejectionBay(false);
		Rejection.setResetProcessRequestedRejectionBay(false);

		// B U T T O N I N T E R L O C K
		btnRejectBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnRejectBayBypass.setDisable(true);

		btnRejectReset.setStyle(""); // Enabled - Default
		btnRejectReset.setDisable(false);

		btnRejectStart.setStyle(""); // Enabled - Default
		btnRejectStart.setDisable(false);

		btnRejectStop.setStyle(""); // Enabled - Default
		btnRejectStop.setDisable(false);

		// L O G I C
		rejectBayBypassTaskTimer = new Timer();
		// Assuming a RejectionBayBypass class exists or will be created
		rejectBayBypassTaskTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Rejection.logger.info("RejectionBayBypass task executed.");
				/* Add actual bypass logic here */ }
		}, 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Rejection.isStartProcessCompletedRejectionBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnRejectReset.setDisable(false);
					btnRejectReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Rejection.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Rejection.logger.info("btnRejectBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnFtStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.FT_BAY_KEY);
	}

	@FXML
	public void btnFtStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.FT_BAY_KEY);
	}

	@FXML
	public void btnFtResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.FT_BAY_KEY);
	}

	@FXML
	public void btnFtBayBypassOnClick() {
		Ft.logger.info("btnFtBayBypassOnClick : Invoked:");

		// F L A G S
		Ft.setStartProcessRequestedFtBay(true);

		Ft.setStopProcessCompletedFtBay(false);
		Ft.setStopProcessRequestedFtBay(false);

		Ft.setResetProcessCompletedFtBay(false);
		Ft.setResetProcessRequestedFtBay(false);

		// B U T T O N I N T E R L O C K
		btnFtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnFtBayBypass.setDisable(true);

		btnFtReset.setStyle(""); // Enabled - Default
		btnFtReset.setDisable(false);

		btnFtStart.setStyle(""); // Enabled - Default
		btnFtStart.setDisable(false);

		btnFtStop.setStyle(""); // Enabled - Default
		btnFtStop.setDisable(false);

		// L O G I C
		funtionalBayBypassTaskTimer = new Timer();
		funtionalBayBypassTaskTimer.schedule(new FunctionalTestBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Ft.isStartProcessCompletedFtBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnFtReset.setDisable(false);
					btnFtReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Ft.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Ft.logger.info("btnFtBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnHvtStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.HV_BAY_KEY);
	}

	@FXML
	public void btnHvtStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.HV_BAY_KEY);
	}

	@FXML
	public void btnHvtResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.HV_BAY_KEY);
	}

	@FXML
	public void btnHvtBayBypassOnClick() {
		Hv.logger.info("btnHvtBayBypassOnClick : Invoked:");

		// F L A G S
		Hv.setStartProcessRequestedHvtBay(true);

		Hv.setStopProcessCompletedHvtBay(false);
		Hv.setStopProcessRequestedHvtBay(false);

		Hv.setResetProcessCompletedHvtBay(false);
		Hv.setResetProcessRequestedHvtBay(false);

		// B U T T O N I N T E R L O C K
		btnHvtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnHvtBayBypass.setDisable(true);

		btnHvtReset.setStyle(""); // Enabled - Default
		btnHvtReset.setDisable(false);

		btnHvtStart.setStyle(""); // Enabled - Default
		btnHvtStart.setDisable(false);

		btnHvtStop.setStyle(""); // Enabled - Default
		btnHvtStop.setDisable(false);

		// L O G I C
		hvtBayBypassTaskTimer = new Timer();
		// Assuming a HighVoltageTestBayBypass class exists or will be created
		hvtBayBypassTaskTimer.schedule(new HighVoltageTestBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Hv.isStartProcessCompletedHvtBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnHvtReset.setDisable(false);
					btnHvtReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Hv.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Hv.logger.info("btnHvtBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnIrtStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.IR_BAY_KEY);
	}

	@FXML
	public void btnIrtStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.IR_BAY_KEY);
	}

	@FXML
	public void btnIrtResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.IR_BAY_KEY);
	}

	@FXML
	public void btnIrtBayBypassOnClick() {
		Ir.logger.info("btnIrtBayBypassOnClick : Invoked:");

		// F L A G S
		Ir.setStartProcessRequestedIrtBay(true);

		Ir.setStopProcessCompletedIrtBay(false);
		Ir.setStopProcessRequestedIrtBay(false);

		Ir.setResetProcessCompletedIrtBay(false);
		Ir.setResetProcessRequestedIrtBay(false);

		// B U T T O N I N T E R L O C K
		btnIrtBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnIrtBayBypass.setDisable(true);

		btnIrtReset.setStyle(""); // Enabled - Default
		btnIrtReset.setDisable(false);

		btnIrtStart.setStyle(""); // Enabled - Default
		btnIrtStart.setDisable(false);

		btnIrtStop.setStyle(""); // Enabled - Default
		btnIrtStop.setDisable(false);

		// L O G I C
		irtBayBypassTaskTimer = new Timer();
		// Assuming an InsulationResistanceTestBayBypass class exists or will be created
		irtBayBypassTaskTimer.schedule(new InsulationResistanceTestBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Ir.isStartProcessCompletedIrtBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnIrtReset.setDisable(false);
					btnIrtReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Ir.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Ir.logger.info("btnIrtBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnCalibStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.CALIBRATION_BAY_KEY);
	}

	@FXML
	public void btnCalibStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.CALIBRATION_BAY_KEY);
	}

	@FXML
	public void btnCalibResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.CALIBRATION_BAY_KEY);
	}

	@FXML
	public void btnCalibBayBypassOnClick() {
		Calib.logger.info("btnCalibBayBypassOnClick : Invoked:");

		// F L A G S
		Calib.setStartProcessRequestedCalibBay(true);

		Calib.setStopProcessCompletedCalibBay(false);
		Calib.setStopProcessRequestedCalibBay(false);

		Calib.setResetProcessCompletedCalibBay(false);
		Calib.setResetProcessRequestedCalibBay(false);

		// B U T T O N I N T E R L O C K
		btnCalibBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnCalibBayBypass.setDisable(true);

		btnCalibReset.setStyle(""); // Enabled - Default
		btnCalibReset.setDisable(false);

		btnCalibStart.setStyle(""); // Enabled - Default
		btnCalibStart.setDisable(false);

		btnCalibStop.setStyle(""); // Enabled - Default
		btnCalibStop.setDisable(false);

		// L O G I C
		calibBayBypassTaskTimer = new Timer();
		// Assuming a CalibrationBayBypass class exists or will be created
		calibBayBypassTaskTimer.schedule(new CalibrationBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Calib.isStartProcessCompletedCalibBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnCalibReset.setDisable(false);
					btnCalibReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Calib.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Calib.logger.info("btnCalibBayBypassOnClick : Exit:");
	}

	// ====================================================================================
	@FXML
	public void btnWaitingBayStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.WAITING_BAY_KEY);
	}

	@FXML
	public void btnWaitingBayStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.WAITING_BAY_KEY);
	}

	@FXML
	public void btnWaitingBayResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.WAITING_BAY_KEY);
	}

	@FXML
	public void btnWaitingBayBypassOnClick() {
		VerificWaiting.logger.info("btnWaitingBayBypassOnClick : Invoked:");

		// F L A G S
		VerificWaiting.setStartProcessRequestedWaitingBay(true);

		VerificWaiting.setStopProcessCompletedWaitingBay(false);
		VerificWaiting.setStopProcessRequestedWaitingBay(false);

		VerificWaiting.setResetProcessCompletedWaitingBay(false);
		VerificWaiting.setResetProcessRequestedWaitingBay(false);

		// B U T T O N I N T E R L O C K
		btnWaitingBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnWaitingBayBypass.setDisable(true);

		btnWaitingBayReset.setStyle(""); // Enabled - Default
		btnWaitingBayReset.setDisable(false);

		btnWaitingBayStart.setStyle(""); // Enabled - Default
		btnWaitingBayStart.setDisable(false);

		btnWaitingBayStop.setStyle(""); // Enabled - Default
		btnWaitingBayStop.setDisable(false);

		// L O G I C
		waitingBayBypassTaskTimer = new Timer();
		// Assuming a WaitingBayBypass class exists or will be created
		BayStateEngine engine = new BayStateEngine(ConstantConveyor.WAITING_BAY_KEY, new VerificWaiting());
		waitingBayBypassTaskTimer.schedule(engine, 100);

		VerificWaiting.logger.info("btnWaitingBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnVerificTestStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.VERIFICATION_BAY_KEY);
	}

	@FXML
	public void btnVerificTestStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.VERIFICATION_BAY_KEY);
	}

	@FXML
	public void btnVerificTestResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.VERIFICATION_BAY_KEY);
	}

	@FXML
	public void btnVerificTestBayBypassOnClick() {
		Verification.logger.info("btnVerificTestBayBypassOnClick : Invoked:");

		// F L A G S
		Verification.setStartProcessRequestedVerificBay(true);

		Verification.setStopProcessCompletedVerificBay(false);
		Verification.setStopProcessRequestedVerificBay(false);

		Verification.setResetProcessCompletedVerificBay(false);
		Verification.setResetProcessRequestedVerificBay(false);

		// B U T T O N I N T E R L O C K
		btnVerificTestBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnVerificTestBayBypass.setDisable(true);

		btnVerificTestReset.setStyle(""); // Enabled - Default
		btnVerificTestReset.setDisable(false);

		btnVerificTestStart.setStyle(""); // Enabled - Default
		btnVerificTestStart.setDisable(false);

		btnVerificTestStop.setStyle(""); // Enabled - Default
		btnVerificTestStop.setDisable(false);

		// L O G I C
		verificTestBayBypassTaskTimer = new Timer();
		// Assuming a VerificationTestBayBypass class exists or will be created
		verificTestBayBypassTaskTimer.schedule(new VerificationTestBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Verification.isStartProcessCompletedVerificBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnVerificTestReset.setDisable(false);
					btnVerificTestReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Verification.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Verification.logger.info("btnVerificTestBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnSctNlt1StartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD1_BAY_KEY);
	}

	@FXML
	public void btnSctNlt1StopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD1_BAY_KEY);
	}

	@FXML
	public void btnSctNlt1ResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD1_BAY_KEY);
	}

	@FXML
	public void btnSctNlt1BayBypassOnClick() {
		StaNld_Bay1.logger.info("btnSctNlt1BayBypassOnClick : Invoked:");

		// F L A G S
		StaNld_Bay1.setStartProcessRequestedStaNldBay1(true);

		StaNld_Bay1.setStopProcessCompletedStaNldBay1(false);
		StaNld_Bay1.setStopProcessRequestedStaNldBay1(false);

		StaNld_Bay1.setResetProcessCompletedStaNldBay1(false);
		StaNld_Bay1.setResetProcessRequestedStaNldBay1(false);

		// B U T T O N I N T E R L O C K
		btnSctNlt1BayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnSctNlt1BayBypass.setDisable(true);

		btnSctNlt1Reset.setStyle(""); // Enabled - Default
		btnSctNlt1Reset.setDisable(false);

		btnSctNlt1Start.setStyle(""); // Enabled - Default
		btnSctNlt1Start.setDisable(false);

		btnSctNlt1Stop.setStyle(""); // Enabled - Default
		btnSctNlt1Stop.setDisable(false);

		// L O G I C
		sctNlt1BayBypassTaskTimer = new Timer();
		// Assuming a STA_NoLoadTestBay1Bypass class exists or will be created
		sctNlt1BayBypassTaskTimer.schedule(new STA_NoLoadTestBay1Bypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!StaNld_Bay1.isStartProcessCompletedStaNldBay1()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnSctNlt1Reset.setDisable(false);
					btnSctNlt1Reset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				StaNld_Bay1.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		StaNld_Bay1.logger.info("btnSctNlt1BayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnSctNlt2StartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD2_BAY_KEY);
	}

	@FXML
	public void btnSctNlt2StopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD2_BAY_KEY);
	}

	@FXML
	public void btnSctNlt2ResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD2_BAY_KEY);
	}

	@FXML
	public void btnSctNlt2BayBypassOnClick() {
		StaNld_Bay2.logger.info("btnSctNlt2BayBypassOnClick : Invoked:");

		// F L A G S
		StaNld_Bay2.setStartProcessRequestedStaNldBay2(true);

		StaNld_Bay2.setStopProcessCompletedStaNldBay2(false);
		StaNld_Bay2.setStopProcessRequestedStaNldBay2(false);
		StaNld_Bay2.logger.info("setStopProcessRequestedStaNldBay2 -Test9  : false");

		StaNld_Bay2.setResetProcessCompletedStaNldBay2(false);
		StaNld_Bay2.setResetProcessRequestedStaNldBay2(false);

		// B U T T O N I N T E R L O C K
		btnSctNlt2BayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnSctNlt2BayBypass.setDisable(true);

		btnSctNlt2Reset.setStyle(""); // Enabled - Default
		btnSctNlt2Reset.setDisable(false);

		btnSctNlt2Start.setStyle(""); // Enabled - Default
		btnSctNlt2Start.setDisable(false);

		btnSctNlt2Stop.setStyle(""); // Enabled - Default
		btnSctNlt2Stop.setDisable(false);

		// L O G I C
		sctNlt2BayBypassTaskTimer = new Timer();
		// Assuming a STA_NoLoadTestBay2Bypass class exists or will be created
		sctNlt2BayBypassTaskTimer.schedule(new STA_NoLoadTestBay2Bypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!StaNld_Bay2.isStartProcessCompletedStaNldBay2()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnSctNlt2Reset.setDisable(false);
					btnSctNlt2Reset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				StaNld_Bay2.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		StaNld_Bay2.logger.info("btnSctNlt2BayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	@FXML
	public void btnCommTestStartOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStart(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.COMMUNICATION_BAY_KEY);
	}

	@FXML
	public void btnCommTestStopOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleStop(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.COMMUNICATION_BAY_KEY);
	}

	@FXML
	public void btnCommTestResetOnClick() {
		com.tasnetwork.calibration.conveyor.dashboard.BayControlsManager.getInstance()
				.handleReset(com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.COMMUNICATION_BAY_KEY);
	}

	@FXML
	public void btnCommTestBayBypassOnClick() {
		Comm.logger.info("btnCommTestBayBypassOnClick : Invoked:");

		// F L A G S
		Comm.setStartProcessRequestedCommBay(true);

		Comm.setStopProcessCompletedCommBay(false);
		Comm.setStopProcessRequestedCommBay(false);

		Comm.setResetProcessCompletedCommBay(false);
		Comm.setResetProcessRequestedCommBay(false);

		// B U T T O N I N T E R L O C K
		btnCommTestBayBypass.setStyle("-fx-background-color: #FF5733;"); // Disabled - Red
		btnCommTestBayBypass.setDisable(true);

		btnCommTestReset.setStyle(""); // Enabled - Default
		btnCommTestReset.setDisable(false);

		btnCommTestStart.setStyle(""); // Enabled - Default
		btnCommTestStart.setDisable(false);

		btnCommTestStop.setStyle(""); // Enabled - Default
		btnCommTestStop.setDisable(false);

		// L O G I C
		commTestBayBypassTaskTimer = new Timer();
		// Assuming a CommunicationTestBayBypass class exists or will be created
		commTestBayBypassTaskTimer.schedule(new CommBayBypass(), 100);
		Thread waitForBypassCompletion = new Thread(() -> {
			try {
				while (!Comm.isStartProcessCompletedCommBay()) {
					Thread.sleep(200);
				}
				Platform.runLater(() -> {
					btnCommTestReset.setDisable(false);
					btnCommTestReset.setStyle("");
				});
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				Comm.logger.warn("Bypass-process wait thread interrupted", e);
			}
		});
		waitForBypassCompletion.setDaemon(true);
		waitForBypassCompletion.start();

		Comm.logger.info("btnCommTestBayBypassOnClick : Exit:");
	}

	// ============================================================================================================================================

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public static void updateTestStatusGui(TestInterfaceStatus testInterfaceStatus) {
		com.tasnetwork.calibration.conveyor.logger.BayStatusLogger.getInstance().updateStatus(testInterfaceStatus);
	}

	public static int addToTestStatusGui(TestInterfaceStatus testIntefaceStatus) {
		try {
			testIntefaceStatus.setSerialNo(String.valueOf(getSerialNoTestStatusAtomic().get()));
			com.tasnetwork.calibration.conveyor.logger.BayStatusLogger.getInstance().logNewStatus(testIntefaceStatus);
		} catch (Exception ex) {
			ApplicationLauncher.logger.error("updateTestStatusGui: Exception-X: " + ex.getMessage());
		}
		return getSerialNoTestStatusAtomic().getAndIncrement();
	}

	public static void updateTestInterfaceStatusOnGui(Map<String, Object> responseReturn, String testStatus) {
		if (responseReturn != null) {
			TestInterfaceStatus test_I_F_Status = (TestInterfaceStatus) responseReturn.get("testInterfaceStatus");
			if (test_I_F_Status != null) {
				test_I_F_Status.setTestStatus(testStatus);
				updateTestStatusGui(test_I_F_Status);
			} else {
				ApplicationLauncher.logger.warn("updateTestInterfaceStatusOnGui: testInterfaceStatus is null");
			}
		}
	}

	public static void updateTestInterfaceStatusOnGuiV2(BayResponse bayResponse, String testStatus) {
		if (bayResponse != null) {
			TestInterfaceStatus test_I_F_Status = bayResponse.getTestInterfaceStatus();
			if (test_I_F_Status != null) {
				test_I_F_Status.setTestStatus(testStatus);
				updateTestStatusGui(test_I_F_Status);
			} else {
				ApplicationLauncher.logger.warn("updateTestInterfaceStatusOnGuiV2: testInterfaceStatus is null");
			}
		}
	}

	// ============================================================================================================================================

	// ============================================================================================================================================

	private void setupTestStatusTabs() {
		// Create All Bays Tab
		javafx.scene.control.Tab allBaysTab = new javafx.scene.control.Tab("All Bays");
		javafx.scene.control.TableView<TestInterfaceStatus> allBaysTable = createTestStatusTableView();
		allBaysTable.setItems(com.tasnetwork.calibration.conveyor.logger.BayStatusLogger.getInstance().getAllBaysLog());
		allBaysTab.setContent(allBaysTable);
		tpTestStatusBays.getTabs().add(allBaysTab);

		// Create individual bay tabs
		for (String bayKey : com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STATE_SEQUENCE_LIST) {
			javafx.scene.control.Tab bayTab = new javafx.scene.control.Tab(bayKey);
			javafx.scene.control.TableView<TestInterfaceStatus> bayTable = createTestStatusTableView();
			bayTable.setItems(
					com.tasnetwork.calibration.conveyor.logger.BayStatusLogger.getInstance().getLogForBay(bayKey));
			bayTab.setContent(bayTable);
			tpTestStatusBays.getTabs().add(bayTab);
		}
	}

	private javafx.scene.control.TableView<TestInterfaceStatus> createTestStatusTableView() {
		javafx.scene.control.TableView<TestInterfaceStatus> table = new javafx.scene.control.TableView<>();

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colSNo = new javafx.scene.control.TableColumn<>(
				"S.No");
		colSNo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("serialNo"));
		colSNo.setPrefWidth(60);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colBay = new javafx.scene.control.TableColumn<>(
				"Bay Name");
		colBay.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("bayName"));
		colBay.setPrefWidth(120);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colState = new javafx.scene.control.TableColumn<>(
				"State Name");
		colState.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stateName"));
		colState.setPrefWidth(250);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colType = new javafx.scene.control.TableColumn<>(
				"Type");
		colType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deviceType"));
		colType.setPrefWidth(100);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colPath = new javafx.scene.control.TableColumn<>(
				"Path No.");
		colPath.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("pathNo"));
		colPath.setPrefWidth(80);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colPos = new javafx.scene.control.TableColumn<>(
				"Pos. No");
		colPos.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("positionNo"));
		colPos.setPrefWidth(80);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colPort = new javafx.scene.control.TableColumn<>(
				"Port Name");
		colPort.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("portName"));
		colPort.setPrefWidth(120);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colCName = new javafx.scene.control.TableColumn<>(
				"C-Name");
		colCName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cName"));
		colCName.setPrefWidth(250);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colSerialStat = new javafx.scene.control.TableColumn<>(
				"Serial Status");
		colSerialStat.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("serialStatus"));
		colSerialStat.setPrefWidth(120);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colRespStat = new javafx.scene.control.TableColumn<>(
				"Response Status");
		colRespStat.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deviceResponseStatus"));
		colRespStat.setPrefWidth(120);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colRespData = new javafx.scene.control.TableColumn<>(
				"Response Data");
		colRespData.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deviceResponseData"));
		colRespData.setPrefWidth(240);

		javafx.scene.control.TableColumn<TestInterfaceStatus, String> colTestStat = new javafx.scene.control.TableColumn<>(
				"Test Status");
		colTestStat.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("testStatus"));
		colTestStat.setPrefWidth(197);

		table.getColumns().addAll(colSNo, colBay, colState, colType, colPath, colPos, colPort, colCName, colSerialStat,
				colRespStat, colRespData, colTestStat);
		return table;
	}

	// A L L S T A R T O N C L I C K
	// ===========================================================================================================

	@FXML
	public void btnAllStartOnClick() {
		ApplicationLauncher.logger.info("btnAllStartOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

		btnAllStart.setDisable(true);
		btnAllStop.setDisable(false);

		btnFtStartOnClick();
		btnHvtStartOnClick();
		btnIrtStartOnClick();
		btnCalibStartOnClick();
		btnWaitingBayStartOnClick();
		btnVerificTestStartOnClick();
		btnSctNlt1StartOnClick();
		btnSctNlt2StartOnClick();
		btnCommTestStartOnClick();
		btnRjStartOnClick();

		ApplicationLauncher.logger.info("btnAllStartOnClick : EXIT:");
	}

	@FXML
	public void btnAllStopOnClick() {
		ApplicationLauncher.logger.info("btnAllStopOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = true;
		BayUtils.setUserAborted(true);

		btnAllStart.setDisable(false);
		btnAllStop.setDisable(true);

		btnFtStopOnClick();
		btnHvtStopOnClick();
		btnIrtStopOnClick();
		btnCalibStopOnClick();
		btnWaitingBayStopOnClick();
		btnVerificTestStopOnClick();
		btnSctNlt1StopOnClick();
		btnSctNlt2StopOnClick();
		btnCommTestStopOnClick();
		btnRjStopOnClick();

		ApplicationLauncher.logger.info("btnAllStopOnClick : Exit:");
	}

	@FXML
	public void btnAllResetOnClick() {
		ApplicationLauncher.logger.info("btnAllResetOnClick : Invoked:");

		ConstantConveyor.ALL_LOOP_BREAK_FLAG = false;

		commResetTaskTimer = new Timer();
		commResetTaskTimer.schedule(new CommunicationTestBayReset(), 100);

		ApplicationLauncher.logger.info("btnAllResetOnClick : Invoked:");
	}

	// === F I L T E R B U T T O N - O N C L I C K
	// ======================================================

	@FXML
	public void btnFilterOnClick() {
		ApplicationLauncher.logger.debug("btnFilterOnClick Invoked:");

		Platform.runLater(() -> {

			ref_btnFilter.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
		});
		btnFilterTaskTimer = new Timer();
		btnFilterTaskTimer.schedule(new FilterOnClickTimerTask(), 100);

	}

	class FilterOnClickTimerTask extends TimerTask {
		@Override
		public void run() {
			Platform.runLater(() -> {
				Object selectedValue = ref_cmbBxFilterPosition.getValue();
				ApplicationLauncher.logger.debug("FilterOnClickTimerTask Position Number : " + selectedValue);

				List<TestInterfaceStatus> filteredDataList;
				if ("All".equals(selectedValue)) {
					filteredDataList = new ArrayList<>(allData);
				} else if (selectedValue instanceof Integer) {
					int filterPosition = (Integer) selectedValue;
					filteredDataList = allData.stream()
							.filter(item -> {
								try {
									String positionNo = item.getPositionNo();
									return positionNo == null || positionNo == "-" || positionNo == "0"
											|| positionNo.isEmpty() ||
											Integer.parseInt(positionNo) == filterPosition;
								} catch (NumberFormatException | NullPointerException e) {
									return false;
								}
							})
							.collect(Collectors.toList());
				} else {
					// Handle unexpected cases
					filteredDataList = new ArrayList<>();
				}

				// Print the filtered list
				// filteredDataList.forEach(item ->
				// ApplicationLauncher.logger.error("Filtered List Item: " + item)
				// ref_tvTestStatus.getItems().add(item));

				filteredDataList.clear();

				WindowManager.setCursor(Cursor.DEFAULT);
				ref_btnFilter.setDisable(false);
			});
		}
	}

	// === L O A D S A M P L E D A T A
	// ======================================================

	@FXML
	public void btnSampleDataOnClick() {
		ApplicationLauncher.logger.debug("btnSampleDataOnClick Invoked:");

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		Random random = new Random();

		for (int j = 0; j < 15; j++) {
			int i = random.nextInt(7);
			testIntefaceStatus = new TestInterfaceStatus(
					"" + i + "BAY",
					"" + i + "Seq",
					"" + i + "DEVICE",
					"P" + i,
					"" + i,
					"-",
					"(" + i + ")",
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);
			allData.add(testIntefaceStatus);
			int newRecordSerialNo = addToTestStatusGui(testIntefaceStatus);
			testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
		}
	}

	// === U S E R F R I E N D L Y P R O M P T S
	// ======================================================

	@FXML
	void CalibPlaceOnClick() {
		ConstantConveyor.setCALIB_OPTICAL_PLACED(true);
	}

	@FXML
	void CalibRemoveOnClick() {
		ConstantConveyor.setCALIB_OPTICAL_REMOVED(true);
	}

	@FXML
	void CalibCurrentStableOnClick() {
		ConstantConveyor.setCALIB_CURRENT_STABLE(true);
	}

	@FXML
	void FtPlaceOnClick() {
		ConstantConveyor.setFT_OPTICAL_PLACED(true);
	}

	@FXML
	void FtRemoveOnClick() {
		ConstantConveyor.setFT_OPTICAL_REMOVED(true);
	}

	@FXML
	void FtLDUOKOnClick() {
		ConstantConveyor.setFT_LDU_PLACEMENT(true);
	}

	@FXML
	void VerificDoneOnClick() {
		ConstantConveyor.setVERIFIC_TESTING_DONE(true);
	}

	// M O T O R C O N T R O L F U N C T I O N S

	@FXML
	void OffMotor1OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_1_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor2OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_2_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor3OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_3_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor4OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_4_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor5OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_5_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor6OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_6_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor7OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_7_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor8OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_8_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OffMotor9OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_9_CONTROL, OFF);
		bayUtils.set_motor_not_required(ConstantConveyor.FT_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.HV_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.IR_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.CALIBRATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.COMMUNICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.LOADING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.REJECTION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD1_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.STA_NLD2_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.WAITING_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.VERIFICATION_BAY_KEY);
		bayUtils.set_motor_not_required(ConstantConveyor.UNLOADING_BAY_KEY);
	}

	@FXML
	void OnMotor1OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_1_CONTROL, ON);
	}

	@FXML
	void OnMotor2OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_2_CONTROL, ON);
	}

	@FXML
	void OnMotor3OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_3_CONTROL, ON);
	}

	@FXML
	void OnMotor4OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_4_CONTROL, ON);
	}

	@FXML
	void OnMotor5OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_5_CONTROL, ON);
	}

	@FXML
	void OnMotor6OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_6_CONTROL, ON);
	}

	@FXML
	void OnMotor7OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_7_CONTROL, ON);
	}

	@FXML
	void OnMotor8OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_8_CONTROL, ON);
	}

	@FXML
	void OnMotor9OnClick() {
		controlOutput(ConstantBayPortNameMapping.MOTOR_9_CONTROL, ON);
	}

	private void controlOutput(String portNameKey, boolean shouldClose) {
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(portNameKey);

		if (portInfo != null) {
			Ft.logger.debug("PortId    : " + portInfo.getPortId());
			Ft.logger.debug("ClusterId : " + portInfo.getClusterId());
			Ft.logger.debug("BayId     : " + portInfo.getBayId());

			String outputAction = shouldClose
					? Constant_IO_ActionMapping.ON // Close = OFF->ON
					: Constant_IO_ActionMapping.OFF; // Open = ON->OFF

			if (ProcalFeatureEnable.MODBUS_PLC_SLAVE_MODE) {
				getBayUtils().setOutputDataToPlcBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			} else {
				getBayUtils().setOutputDataToBay(
						portInfo.getClusterId(),
						portInfo.getBayId(),
						portInfo.getPortId(),
						outputAction);
			}
		}
	}
	// ==========================================================================================

	public static AtomicInteger getSerialNoTestStatusAtomic() {
		return serialNoTestStatusAtomic;
	}

	public void setSerialNoTestStatusAtomic(AtomicInteger serialNoTestStatusAtomic) {
		StateExecutorController.serialNoTestStatusAtomic = serialNoTestStatusAtomic;
	}

	public static TextField getRef_tf_CALIB_prompt() {
		return ref_tf_CALIB_prompt;
	}

	public static void setRef_tf_CALIB_prompt(TextField ref_tf_CALIB_prompt) {
		StateExecutorController.ref_tf_CALIB_prompt = ref_tf_CALIB_prompt;
	}

	public static TextField getRef_tf_FT_prompt() {
		return ref_tf_FT_prompt;
	}

	public static void setRef_tf_FT_prompt(TextField ref_tf_FT_prompt) {
		StateExecutorController.ref_tf_FT_prompt = ref_tf_FT_prompt;
	}

	public static Button getRef_btn_CalibPlace() {
		return ref_btn_CalibPlace;
	}

	public static void setRef_btn_CalibPlace(Button ref_btn_CalibPlace) {
		StateExecutorController.ref_btn_CalibPlace = ref_btn_CalibPlace;
	}

	public static Button getRef_btn_CalibRemove() {
		return ref_btn_CalibRemove;
	}

	public static void setRef_btn_CalibRemove(Button ref_btn_CalibRemove) {
		StateExecutorController.ref_btn_CalibRemove = ref_btn_CalibRemove;
	}

	public static Button getRef_btn_FtPlace() {
		return ref_btn_FtPlace;
	}

	public static void setRef_btn_FtPlace(Button ref_btn_FtPlace) {
		StateExecutorController.ref_btn_FtPlace = ref_btn_FtPlace;
	}

	public static Button getRef_btn_FtRemove() {
		return ref_btn_FtRemove;
	}

	public static void setRef_btn_FtRemove(Button ref_btn_FtRemove) {
		StateExecutorController.ref_btn_FtRemove = ref_btn_FtRemove;
	}

	public static Button getRef_btn_CalibCurrentStable() {
		return ref_btn_CalibCurrentStable;
	}

	public static void setRef_btn_CalibCurrentStable(Button ref_btn_CalibCurrentStable) {
		StateExecutorController.ref_btn_CalibCurrentStable = ref_btn_CalibCurrentStable;
	}

	public static Button getRef_btn_Ft_LDUOK() {
		return ref_btn_Ft_LDUOK;
	}

	public static void setRef_btn_Ft_LDUOK(Button ref_btn_Ft_LDUOK) {
		StateExecutorController.ref_btn_Ft_LDUOK = ref_btn_Ft_LDUOK;
	}

	public static Button getRef_btn_VerificDone() {
		return ref_btn_VerificDone;
	}

	public static void setRef_btn_VerificDone(Button ref_btn_VerificDone) {
		StateExecutorController.ref_btn_VerificDone = ref_btn_VerificDone;
	}

	public BayUtils getBayUtils() {
		return bayUtils;
	}

	public void setBayUtils(BayUtils bayUtils) {
		this.bayUtils = bayUtils;
	}

	public void triggerStartByBayKey(String bayKey) {
		Platform.runLater(() -> {
			com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController
					.getInstance();
			if (mcpc != null) {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						mcpc.btnFtStartOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						mcpc.btnHvtStartOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						mcpc.btnIrtStartOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						mcpc.btnCalibStartOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						mcpc.btnWaitingBayStartOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						mcpc.btnVerificTestStartOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						mcpc.btnSctNlt1StartOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						mcpc.btnSctNlt2StartOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						mcpc.btnCommTestStartOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						mcpc.btnRjStartOnClick();
						break;
				}
			} else {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						btnFtStartOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						btnHvtStartOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						btnIrtStartOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						btnCalibStartOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						btnWaitingBayStartOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						btnVerificTestStartOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						btnSctNlt1StartOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						btnSctNlt2StartOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						btnCommTestStartOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						btnRjStartOnClick();
						break;
				}
			}
		});
	}

	public void triggerStopByBayKey(String bayKey) {
		Platform.runLater(() -> {
			com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController
					.getInstance();
			if (mcpc != null) {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						mcpc.btnFtStopOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						mcpc.btnHvtStopOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						mcpc.btnIrtStopOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						mcpc.btnCalibStopOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						mcpc.btnWaitingBayStopOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						mcpc.btnVerificTestStopOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						mcpc.btnSctNlt1StopOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						mcpc.btnSctNlt2StopOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						mcpc.btnCommTestStopOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						mcpc.btnRjStopOnClick();
						break;
				}
			} else {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						btnFtStopOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						btnHvtStopOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						btnIrtStopOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						btnCalibStopOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						btnWaitingBayStopOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						btnVerificTestStopOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						btnSctNlt1StopOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						btnSctNlt2StopOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						btnCommTestStopOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						btnRjStopOnClick();
						break;
				}
			}
		});
	}

	public void triggerResetByBayKey(String bayKey) {
		Platform.runLater(() -> {
			com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController
					.getInstance();
			if (mcpc != null) {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						mcpc.btnFtResetOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						mcpc.btnHvtResetOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						mcpc.btnIrtResetOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						mcpc.btnCalibResetOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						mcpc.btnWaitingBayResetOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						mcpc.btnVerificTestResetOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						mcpc.btnSctNlt1ResetOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						mcpc.btnSctNlt2ResetOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						mcpc.btnCommTestResetOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						mcpc.btnRjResetOnClick();
						break;
				}
			} else {
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						btnFtResetOnClick();
						break;
					case ConstantConveyor.HV_BAY_KEY:
						btnHvtResetOnClick();
						break;
					case ConstantConveyor.IR_BAY_KEY:
						btnIrtResetOnClick();
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						btnCalibResetOnClick();
						break;
					case ConstantConveyor.WAITING_BAY_KEY:
					case ConstantConveyor.WAITING_PP1_BAY_KEY:
					case ConstantConveyor.WAITING_PP2_BAY_KEY:
					case ConstantConveyor.WAITING_PP3_BAY_KEY:
					case ConstantConveyor.WAITING_PP4_BAY_KEY:
						btnWaitingBayResetOnClick();
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
					case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
						btnVerificTestResetOnClick();
						break;
					case ConstantConveyor.STA_NLD1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
						btnSctNlt1ResetOnClick();
						break;
					case ConstantConveyor.STA_NLD2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
					case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
						btnSctNlt2ResetOnClick();
						break;
					case ConstantConveyor.COMMUNICATION_BAY_KEY:
						btnCommTestResetOnClick();
						break;
					case ConstantConveyor.REJECTION_BAY_KEY:
						btnRjResetOnClick();
						break;
				}
			}
		});
	}

	public void triggerBypassByBayKey(String bayKey) {
		Platform.runLater(() -> {
			switch (bayKey) {
				case ConstantConveyor.FT_BAY_KEY:
					btnFtBayBypassOnClick();
					break;
				case ConstantConveyor.HV_BAY_KEY:
					btnHvtBayBypassOnClick();
					break;
				case ConstantConveyor.IR_BAY_KEY:
					btnIrtBayBypassOnClick();
					break;
				case ConstantConveyor.CALIBRATION_BAY_KEY:
					btnCalibBayBypassOnClick();
					break;
				case ConstantConveyor.WAITING_BAY_KEY:
				case ConstantConveyor.WAITING_PP1_BAY_KEY:
				case ConstantConveyor.WAITING_PP2_BAY_KEY:
				case ConstantConveyor.WAITING_PP3_BAY_KEY:
				case ConstantConveyor.WAITING_PP4_BAY_KEY:
					btnWaitingBayBypassOnClick();
					break;
				case ConstantConveyor.VERIFICATION_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP1_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP2_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP3_BAY_KEY:
				case ConstantConveyor.VERIFICATION_PP4_BAY_KEY:
					btnVerificTestBayBypassOnClick();
					break;
				case ConstantConveyor.STA_NLD1_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP1_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP2_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP3_BAY_KEY:
				case ConstantConveyor.STA_NLD1_PP4_BAY_KEY:
					btnSctNlt1BayBypassOnClick();
					break;
				case ConstantConveyor.STA_NLD2_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP1_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP2_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP3_BAY_KEY:
				case ConstantConveyor.STA_NLD2_PP4_BAY_KEY:
					btnSctNlt2BayBypassOnClick();
					break;
				case ConstantConveyor.COMMUNICATION_BAY_KEY:
					btnCommTestBayBypassOnClick();
					break;
				case ConstantConveyor.REJECTION_BAY_KEY:
					btnRejectBayBypassOnClick();
					break;
			}
		});
	}

	public static void updateBayPrompt(String bayKey, String promptMessage) {
		Platform.runLater(() -> {
			try {
				if (bayKey == null)
					return;
				switch (bayKey) {
					case ConstantConveyor.FT_BAY_KEY:
						if (ref_tf_FT_prompt != null)
							ref_tf_FT_prompt.setText(promptMessage);
						break;
					case ConstantConveyor.CALIBRATION_BAY_KEY:
						if (ref_tf_CALIB_prompt != null)
							ref_tf_CALIB_prompt.setText(promptMessage);
						break;
					case ConstantConveyor.VERIFICATION_BAY_KEY:
						if (ref_tf_VERIFIC_prompt != null)
							ref_tf_VERIFIC_prompt.setText(promptMessage);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				ApplicationLauncher.logger.warn("updateBayPrompt exception: " + e.getMessage());
			}
		});
	}

	@Override
	public void updateBayUI(String bayKey, boolean isRunning) {
		javafx.application.Platform.runLater(() -> {
			switch (bayKey) {
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.FT_BAY_KEY:
					if (btnFtStart != null) {
						if (isRunning) {
							btnFtStart.setStyle("-fx-background-color: #FF5733;");
							btnFtStart.setDisable(true);
							if (btnFtStop != null) {
								btnFtStop.setStyle("");
								btnFtStop.setDisable(false);
							}
							if (btnFtReset != null) {
								btnFtReset.setStyle("-fx-background-color: #FF5733;");
								btnFtReset.setDisable(true);
							}
							if (btnFtBayBypass != null) {
								btnFtBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnFtBayBypass.setDisable(true);
							}
						} else {
							btnFtStart.setStyle("");
							btnFtStart.setDisable(false);
							if (btnFtStop != null) {
								btnFtStop.setStyle("-fx-background-color: #FF5733;");
								btnFtStop.setDisable(true);
							}
							if (btnFtReset != null) {
								btnFtReset.setStyle("");
								btnFtReset.setDisable(false);
							}
							if (btnFtBayBypass != null) {
								btnFtBayBypass.setStyle("");
								btnFtBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.HV_BAY_KEY:
					if (btnHvtStart != null) {
						if (isRunning) {
							btnHvtStart.setStyle("-fx-background-color: #FF5733;");
							btnHvtStart.setDisable(true);
							if (btnHvtStop != null) {
								btnHvtStop.setStyle("");
								btnHvtStop.setDisable(false);
							}
							if (btnHvtReset != null) {
								btnHvtReset.setStyle("-fx-background-color: #FF5733;");
								btnHvtReset.setDisable(true);
							}
							if (btnHvtBayBypass != null) {
								btnHvtBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnHvtBayBypass.setDisable(true);
							}
						} else {
							btnHvtStart.setStyle("");
							btnHvtStart.setDisable(false);
							if (btnHvtStop != null) {
								btnHvtStop.setStyle("-fx-background-color: #FF5733;");
								btnHvtStop.setDisable(true);
							}
							if (btnHvtReset != null) {
								btnHvtReset.setStyle("");
								btnHvtReset.setDisable(false);
							}
							if (btnHvtBayBypass != null) {
								btnHvtBayBypass.setStyle("");
								btnHvtBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.IR_BAY_KEY:
					if (btnIrtStart != null) {
						if (isRunning) {
							btnIrtStart.setStyle("-fx-background-color: #FF5733;");
							btnIrtStart.setDisable(true);
							if (btnIrtStop != null) {
								btnIrtStop.setStyle("");
								btnIrtStop.setDisable(false);
							}
							if (btnIrtReset != null) {
								btnIrtReset.setStyle("-fx-background-color: #FF5733;");
								btnIrtReset.setDisable(true);
							}
							if (btnIrtBayBypass != null) {
								btnIrtBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnIrtBayBypass.setDisable(true);
							}
						} else {
							btnIrtStart.setStyle("");
							btnIrtStart.setDisable(false);
							if (btnIrtStop != null) {
								btnIrtStop.setStyle("-fx-background-color: #FF5733;");
								btnIrtStop.setDisable(true);
							}
							if (btnIrtReset != null) {
								btnIrtReset.setStyle("");
								btnIrtReset.setDisable(false);
							}
							if (btnIrtBayBypass != null) {
								btnIrtBayBypass.setStyle("");
								btnIrtBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.CALIBRATION_BAY_KEY:
					if (btnCalibStart != null) {
						if (isRunning) {
							btnCalibStart.setStyle("-fx-background-color: #FF5733;");
							btnCalibStart.setDisable(true);
							if (btnCalibStop != null) {
								btnCalibStop.setStyle("");
								btnCalibStop.setDisable(false);
							}
							if (btnCalibReset != null) {
								btnCalibReset.setStyle("-fx-background-color: #FF5733;");
								btnCalibReset.setDisable(true);
							}
							if (btnCalibBayBypass != null) {
								btnCalibBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnCalibBayBypass.setDisable(true);
							}
						} else {
							btnCalibStart.setStyle("");
							btnCalibStart.setDisable(false);
							if (btnCalibStop != null) {
								btnCalibStop.setStyle("-fx-background-color: #FF5733;");
								btnCalibStop.setDisable(true);
							}
							if (btnCalibReset != null) {
								btnCalibReset.setStyle("");
								btnCalibReset.setDisable(false);
							}
							if (btnCalibBayBypass != null) {
								btnCalibBayBypass.setStyle("");
								btnCalibBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.WAITING_BAY_KEY:
					if (btnWaitingBayStart != null) {
						if (isRunning) {
							btnWaitingBayStart.setStyle("-fx-background-color: #FF5733;");
							btnWaitingBayStart.setDisable(true);
							if (btnWaitingBayStop != null) {
								btnWaitingBayStop.setStyle("");
								btnWaitingBayStop.setDisable(false);
							}
							if (btnWaitingBayReset != null) {
								btnWaitingBayReset.setStyle("-fx-background-color: #FF5733;");
								btnWaitingBayReset.setDisable(true);
							}
						} else {
							btnWaitingBayStart.setStyle("");
							btnWaitingBayStart.setDisable(false);
							if (btnWaitingBayStop != null) {
								btnWaitingBayStop.setStyle("-fx-background-color: #FF5733;");
								btnWaitingBayStop.setDisable(true);
							}
							if (btnWaitingBayReset != null) {
								btnWaitingBayReset.setStyle("");
								btnWaitingBayReset.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.VERIFICATION_BAY_KEY:
					if (btnVerificTestStart != null) {
						if (isRunning) {
							btnVerificTestStart.setStyle("-fx-background-color: #FF5733;");
							btnVerificTestStart.setDisable(true);
							if (btnVerificTestStop != null) {
								btnVerificTestStop.setStyle("");
								btnVerificTestStop.setDisable(false);
							}
							if (btnVerificTestReset != null) {
								btnVerificTestReset.setStyle("-fx-background-color: #FF5733;");
								btnVerificTestReset.setDisable(true);
							}
							if (btnVerificTestBayBypass != null) {
								btnVerificTestBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnVerificTestBayBypass.setDisable(true);
							}
						} else {
							btnVerificTestStart.setStyle("");
							btnVerificTestStart.setDisable(false);
							if (btnVerificTestStop != null) {
								btnVerificTestStop.setStyle("-fx-background-color: #FF5733;");
								btnVerificTestStop.setDisable(true);
							}
							if (btnVerificTestReset != null) {
								btnVerificTestReset.setStyle("");
								btnVerificTestReset.setDisable(false);
							}
							if (btnVerificTestBayBypass != null) {
								btnVerificTestBayBypass.setStyle("");
								btnVerificTestBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD1_BAY_KEY:
					if (btnSctNlt1Start != null) {
						if (isRunning) {
							btnSctNlt1Start.setStyle("-fx-background-color: #FF5733;");
							btnSctNlt1Start.setDisable(true);
							if (btnSctNlt1Stop != null) {
								btnSctNlt1Stop.setStyle("");
								btnSctNlt1Stop.setDisable(false);
							}
							if (btnSctNlt1Reset != null) {
								btnSctNlt1Reset.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt1Reset.setDisable(true);
							}
							if (btnSctNlt1BayBypass != null) {
								btnSctNlt1BayBypass.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt1BayBypass.setDisable(true);
							}
						} else {
							btnSctNlt1Start.setStyle("");
							btnSctNlt1Start.setDisable(false);
							if (btnSctNlt1Stop != null) {
								btnSctNlt1Stop.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt1Stop.setDisable(true);
							}
							if (btnSctNlt1Reset != null) {
								btnSctNlt1Reset.setStyle("");
								btnSctNlt1Reset.setDisable(false);
							}
							if (btnSctNlt1BayBypass != null) {
								btnSctNlt1BayBypass.setStyle("");
								btnSctNlt1BayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.STA_NLD2_BAY_KEY:
					if (btnSctNlt2Start != null) {
						if (isRunning) {
							btnSctNlt2Start.setStyle("-fx-background-color: #FF5733;");
							btnSctNlt2Start.setDisable(true);
							if (btnSctNlt2Stop != null) {
								btnSctNlt2Stop.setStyle("");
								btnSctNlt2Stop.setDisable(false);
							}
							if (btnSctNlt2Reset != null) {
								btnSctNlt2Reset.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt2Reset.setDisable(true);
							}
							if (btnSctNlt2BayBypass != null) {
								btnSctNlt2BayBypass.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt2BayBypass.setDisable(true);
							}
						} else {
							btnSctNlt2Start.setStyle("");
							btnSctNlt2Start.setDisable(false);
							if (btnSctNlt2Stop != null) {
								btnSctNlt2Stop.setStyle("-fx-background-color: #FF5733;");
								btnSctNlt2Stop.setDisable(true);
							}
							if (btnSctNlt2Reset != null) {
								btnSctNlt2Reset.setStyle("");
								btnSctNlt2Reset.setDisable(false);
							}
							if (btnSctNlt2BayBypass != null) {
								btnSctNlt2BayBypass.setStyle("");
								btnSctNlt2BayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.COMMUNICATION_BAY_KEY:
					if (btnCommTestStart != null) {
						if (isRunning) {
							btnCommTestStart.setStyle("-fx-background-color: #FF5733;");
							btnCommTestStart.setDisable(true);
							if (btnCommTestStop != null) {
								btnCommTestStop.setStyle("");
								btnCommTestStop.setDisable(false);
							}
							if (btnCommTestReset != null) {
								btnCommTestReset.setStyle("-fx-background-color: #FF5733;");
								btnCommTestReset.setDisable(true);
							}
							if (btnCommTestBayBypass != null) {
								btnCommTestBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnCommTestBayBypass.setDisable(true);
							}
						} else {
							btnCommTestStart.setStyle("");
							btnCommTestStart.setDisable(false);
							if (btnCommTestStop != null) {
								btnCommTestStop.setStyle("-fx-background-color: #FF5733;");
								btnCommTestStop.setDisable(true);
							}
							if (btnCommTestReset != null) {
								btnCommTestReset.setStyle("");
								btnCommTestReset.setDisable(false);
							}
							if (btnCommTestBayBypass != null) {
								btnCommTestBayBypass.setStyle("");
								btnCommTestBayBypass.setDisable(false);
							}
						}
					}
					break;
				case com.tasnetwork.calibration.conveyor.constant.ConstantConveyor.REJECTION_BAY_KEY:
					if (btnRejectStart != null) {
						if (isRunning) {
							btnRejectStart.setStyle("-fx-background-color: #FF5733;");
							btnRejectStart.setDisable(true);
							if (btnRejectStop != null) {
								btnRejectStop.setStyle("");
								btnRejectStop.setDisable(false);
							}
							if (btnRejectReset != null) {
								btnRejectReset.setStyle("-fx-background-color: #FF5733;");
								btnRejectReset.setDisable(true);
							}
							if (btnRejectBayBypass != null) {
								btnRejectBayBypass.setStyle("-fx-background-color: #FF5733;");
								btnRejectBayBypass.setDisable(true);
							}
						} else {
							btnRejectStart.setStyle("");
							btnRejectStart.setDisable(false);
							if (btnRejectStop != null) {
								btnRejectStop.setStyle("-fx-background-color: #FF5733;");
								btnRejectStop.setDisable(true);
							}
							if (btnRejectReset != null) {
								btnRejectReset.setStyle("");
								btnRejectReset.setDisable(false);
							}
							if (btnRejectBayBypass != null) {
								btnRejectBayBypass.setStyle("");
								btnRejectBayBypass.setDisable(false);
							}
						}
					}
					break;
			}
		});
	}
}