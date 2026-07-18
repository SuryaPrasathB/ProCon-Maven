package com.tasnetwork.calibration.conveyor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xpath.internal.functions.FuncId;
import com.sun.org.apache.xpath.internal.functions.Function;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ServerProperties;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.DutDevice;
import com.tasnetwork.calibration.conveyor.bay.configloader.MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.QrScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
//import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBay2;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayReset;
import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBayStop;
import com.tasnetwork.calibration.conveyor.bay.comm.Comm;
//import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBay2;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.hv.Hv;
//import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBay2;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.ir.Ir;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBayStop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1Stop;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Reset;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2Stop;
//import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2_2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
//import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBay2;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBayStop;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayReset;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBayStop;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.ResultSummary;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.control.cell.ComboBoxTableCell;

public class ConveyorDebugController implements Initializable {

	// public static final boolean simulateFtBayHappyPath = true;
	public static final boolean simulateHvBayHappyPath = true;
	public static final boolean simulateIrBayHappyPath = true;
	public static final boolean simulateCalibBayHappyPath = true;
	public static final boolean simulateCommBayHappyPath = true;
	public static final boolean simulateLoadingBayHappyPath = true;
	public static final boolean simulateRejectionBayHappyPath = true;
	public static final boolean simulateSCTNLTBay1HappyPath = true;
	public static final boolean simulateSCTNLTBay2HappyPath = true;
	public static final boolean simulateUnloadingBayHappyPath = true;
	public static final boolean simulateVerificBayHappyPath = true;
	public static final boolean simulateWaitingBayHappyPath = true;

	public static Logger logger = Logger.getLogger(ConveyorDebugController.class.getPackage().getName());// ConveyorDebugController.class
																											// );
	// ==================== STATE PLANNER
	// ===============================================================//
	/*
	 * @FXML
	 * public TableView<StateFlowRow> tableStatePlanner;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnPath;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnState;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnStateErrorCode;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnSuccess;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnSuccessErrorCode;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnFailed;
	 * 
	 * @FXML
	 * public TableColumn<StateFlowRow, String> columnFailedErrorCode;
	 * 
	 * 
	 * public static TableView<StateFlowRow> tableStatePlanner_FtBay_UI = new
	 * TableView<StateFlowRow>();
	 * 
	 * private final ObservableList<String> stateNames =
	 * FXCollections.observableArrayList(
	 */

	/*
	 * "Check for Pallet",
	 * "Check Stopper Before Bay Status",
	 * "Open Stopper Before Bay",
	 * "Close Stopper Before Bay",
	 * "Close FingerTip Latch",
	 * "Ensure FingerTip Latch Closed",
	 * "Soucre Start",
	 * "Ensure Source Started",
	 * "Functional Test",
	 * "Source Stop",
	 * "Ensure Source Stopped",
	 * "Close Divertor Relay",
	 * "Open Divertor Relay",
	 * "Open FingerTip Latch",
	 * "Ensure FingerTip Latch Opened",
	 * "Open Stopper At Bay",
	 * "Close Stopper At Bay",
	 * "Error Handling",
	 * "Idle Condition"
	 */

	/*
	 * "S01_check_for_pallet_at_FT_Bay",
	 * "S02_let_the_pallet_to_FT_Bay",
	 * "S03_close_the_fingerTip_Latch",
	 * "S04_ensure_the_fingerTip_Latch_Closed",
	 * "S05_qR_Code_Scanning_of_Pallet",
	 * "S06_scanning_of_Meters_Presence",
	 * "S07_qR_Code_Scanning_of_Meters",
	 * "S071_start_FT_source",
	 * "S072_ensure_FT_source_started",
	 * "S08_functional_Test",
	 * "S081_stop_FT_source",
	 * "S082_ensure_FT_source_stopped",
	 * "S09_open_the_fingerTip_Latch",
	 * "S10_ensure_the_fingerTip_Latch_Opened",
	 * "S11_check_for_pallet_at_HVT_Bay",
	 * "S12_turn_on_divertor_relay_FT_Bay",
	 * "S13_ensure_divertor_relay_turned_on_FT_Bay",
	 * "S14_let_the_pallet_to_HVT_Bay",
	 * "S15_ensure_pallet_reached_HVT_Bay",
	 * "S16_check_for_pallet_at_Rejection_Bay",
	 * "S17_turn_off_divertor_relay_FT_Bay",
	 * "S18_ensure_divertor_relay_turned_off_FT_Bay",
	 * "S19_wait_for_reset_button_ip_Rejection_Bay",
	 * "S20_let_the_pallet_to_Rejection_Bay",
	 * "S21_ensure_pallet_reached_Reject_Bay",
	 * "S22_error_Handling"
	 * 
	 * 
	 * );
	 */

	private final Map<String, String> stateCodeMap = new HashMap<>();

	// Populate the map with state names and their corresponding state codes
	/*
	 * private void initializeStateCodeMap() {
	 * stateCodeMap.put("Select State", "No State Selected");
	 * stateCodeMap.put("Check for Pallet", "STATE_CODE_001");
	 * stateCodeMap.put("Check Stopper Before Bay Status", "STATE_CODE_002");
	 * stateCodeMap.put("Open Stopper Before Bay", "STATE_CODE_003");
	 * stateCodeMap.put("Close Stopper Before Bay", "STATE_CODE_004");
	 * stateCodeMap.put("Close FingerTip Latch", "STATE_CODE_005");
	 * stateCodeMap.put("Ensure FingerTip Latch Closed", "STATE_CODE_006");
	 * stateCodeMap.put("Soucre Start", "STATE_CODE_007");
	 * stateCodeMap.put("Ensure Source Started", "STATE_CODE_008");
	 * stateCodeMap.put("Functional Test", "STATE_CODE_009");
	 * stateCodeMap.put("Source Stop", "STATE_CODE_010");
	 * stateCodeMap.put("Ensure Source Stopped", "STATE_CODE_011");
	 * stateCodeMap.put("Close Divertor Relay", "STATE_CODE_012");
	 * stateCodeMap.put("Open Divertor Relay", "STATE_CODE_013");
	 * stateCodeMap.put("Open FingerTip Latch", "STATE_CODE_014");
	 * stateCodeMap.put("Ensure FingerTip Latch Opened", "STATE_CODE_015");
	 * stateCodeMap.put("Open Stopper At Bay", "STATE_CODE_016");
	 * stateCodeMap.put("Close Stopper At Bay", "STATE_CODE_017");
	 * stateCodeMap.put("Error Handling", "STATE_CODE_018");
	 * stateCodeMap.put("Idle Condition", "STATE_CODE_019");
	 * 
	 * // Add other states as needed...
	 * 
	 * stateCodeMap.put("S01_check_for_pallet_at_FT_Bay", "STATE_CODE_001");
	 * stateCodeMap.put("S02_let_the_pallet_to_FT_Bay", "STATE_CODE_002");
	 * stateCodeMap.put("S03_close_the_fingerTip_Latch", "STATE_CODE_003");
	 * stateCodeMap.put("S04_ensure_the_fingerTip_Latch_Closed","STATE_CODE_004");
	 * stateCodeMap.put("S05_qR_Code_Scanning_of_Pallet", "STATE_CODE_005");
	 * stateCodeMap.put("S06_scanning_of_Meters_Presence", "STATE_CODE_006");
	 * stateCodeMap.put("S07_qR_Code_Scanning_of_Meters", "STATE_CODE_007");
	 * stateCodeMap.put("S071_start_FT_source", "STATE_CODE_008");
	 * stateCodeMap.put("S072_ensure_FT_source_started", "STATE_CODE_009");
	 * stateCodeMap.put("S08_functional_Test", "STATE_CODE_010");
	 * stateCodeMap.put("S081_stop_FT_source", "STATE_CODE_011");
	 * stateCodeMap.put("S082_ensure_FT_source_stopped", "STATE_CODE_012");
	 * stateCodeMap.put("S09_open_the_fingerTip_Latch", "STATE_CODE_013");
	 * stateCodeMap.put("S10_ensure_the_fingerTip_Latch_Opened","STATE_CODE_014");
	 * stateCodeMap.put("S11_check_for_pallet_at_HVT_Bay", "STATE_CODE_015");
	 * stateCodeMap.put("S12_turn_on_divertor_relay_FT_Bay", "STATE_CODE_016");
	 * stateCodeMap.put("S13_ensure_divertor_relay_turned_on_FT_Bay",
	 * "STATE_CODE_017");
	 * stateCodeMap.put("S14_let_the_pallet_to_HVT_Bay", "STATE_CODE_018");
	 * stateCodeMap.put("S15_ensure_pallet_reached_HVT_Bay", "STATE_CODE_019");
	 * stateCodeMap.put("S16_check_for_pallet_at_Rejection_Bay", "STATE_CODE_020");
	 * stateCodeMap.put("S17_turn_off_divertor_relay_FT_Bay", "STATE_CODE_021");
	 * stateCodeMap.put("S18_ensure_divertor_relay_turned_off_FT_Bay",
	 * "STATE_CODE_022");
	 * stateCodeMap.put("S19_wait_for_reset_button_ip_Rejection_Bay",
	 * "STATE_CODE_023");
	 * stateCodeMap.put("S20_let_the_pallet_to_Rejection_Bay", "STATE_CODE_024");
	 * stateCodeMap.put("S21_ensure_pallet_reached_Reject_Bay", "STATE_CODE_025");
	 * stateCodeMap.put("S22_error_Handling", "STATE_CODE_026");
	 * 
	 * }
	 */
	/*
	 * private final ObservableList<StateFlowRow> stateFlowRows =
	 * FXCollections.observableArrayList();
	 */

	// =============================================================================================================================================
	@FXML
	private Tab tabBayTest;
	private static Tab ref_tabBayTest;

	@FXML
	private Tab tabPlcClientBayTest;
	private static Tab ref_tabPlcClientBayTest;

	@FXML
	private TabPane tabPaneConveyor;
	private static TabPane ref_tabPaneConveyor;

	// P A L L E T V I E W E R

	@FXML
	private TextField txtCalibBayPallet;

	@FXML
	private TextField txtCommBayPallet;

	@FXML
	private TextField txtFTBayPallet;

	@FXML
	private TextField txtHVBayPallet;

	@FXML
	private TextField txtIRBayPallet;

	@FXML
	private TextField txtLoadingBayPallet;

	@FXML
	private TextField txtRejectionBayPallet;

	@FXML
	private TextField txtSCTNLTBay1Pallet1;

	@FXML
	private TextField txtSCTNLTBay1Pallet2;

	@FXML
	private TextField txtSCTNLTBay1Pallet3;

	@FXML
	private TextField txtSCTNLTBay1Pallet4;

	@FXML
	private TextField txtSCTNLTBay2Pallet1;

	@FXML
	private TextField txtSCTNLTBay2Pallet2;

	@FXML
	private TextField txtSCTNLTBay2Pallet3;

	@FXML
	private TextField txtSCTNLTBay2Pallet4;

	@FXML
	private TextField txtUnloadingBayPallet;

	@FXML
	private TextField txtVerificBayPallet1;

	@FXML
	private TextField txtVerificBayPallet2;

	@FXML
	private TextField txtVerificBayPallet3;

	@FXML
	private TextField txtVerificBayPallet4;

	@FXML
	private TextField txtWaitingBayPallet1;

	@FXML
	private TextField txtWaitingBayPallet2;

	@FXML
	private TextField txtWaitingBayPallet3;

	@FXML
	private TextField txtWaitingBayPallet4;

	// Static references
	private static TextField ref_txtCalibBayPallet;
	private static TextField ref_txtCommBayPallet;
	private static TextField ref_txtFTBayPallet;
	private static TextField ref_txtHVBayPallet;
	private static TextField ref_txtIRBayPallet;
	private static TextField ref_txtLoadingBayPallet;
	private static TextField ref_txtRejectionBayPallet;
	private static TextField ref_txtSCTNLTBay1Pallet1;
	private static TextField ref_txtSCTNLTBay1Pallet2;
	private static TextField ref_txtSCTNLTBay1Pallet3;
	private static TextField ref_txtSCTNLTBay1Pallet4;
	private static TextField ref_txtSCTNLTBay2Pallet1;
	private static TextField ref_txtSCTNLTBay2Pallet2;
	private static TextField ref_txtSCTNLTBay2Pallet3;
	private static TextField ref_txtSCTNLTBay2Pallet4;
	private static TextField ref_txtUnloadingBayPallet;
	private static TextField ref_txtVerificBayPallet1;
	private static TextField ref_txtVerificBayPallet2;
	private static TextField ref_txtVerificBayPallet3;
	private static TextField ref_txtVerificBayPallet4;
	private static TextField ref_txtWaitingBayPallet1;
	private static TextField ref_txtWaitingBayPallet2;
	private static TextField ref_txtWaitingBayPallet3;
	private static TextField ref_txtWaitingBayPallet4;

	// =============================================================================================================================================

	@FXML
	private AnchorPane statePlannerChild;

	@FXML
	private AnchorPane stateExecutorChild;

	@FXML
	private AnchorPane bayTestChild;

	@FXML
	private AnchorPane plcClientBayTestChild;

	@FXML
	private AnchorPane plcServerBayTestChild;

	// static AtomicInteger serialNoTestStatusAtomic = new AtomicInteger(1);
	// Position Loaded @FXML Attributes
	@FXML
	private TableView<PositionLoadedModel> tvLoadedPosition;

	@FXML
	private TableColumn<PositionLoadedModel, String> colLpPositonNo;

	@FXML
	private TableColumn<PositionLoadedModel, String> colLpSelect;

	@FXML
	private TableColumn<PositionLoadedModel, String> colLpSerialNo;

	@FXML
	private TableColumn<PositionLoadedModel, String> colLpCname;

	@FXML
	private Button btnRefresh;

	/*
	 * @FXML
	 * private Button btnFilter;
	 */

	@FXML
	private Button btnSampleData;

	// Test Status @FXML Attributes
	/*
	 * @FXML
	 * private TableView<TestInterfaceStatus> tvTestStatus;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsSerialNo;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsBayName;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsStateName;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsDeviceType;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsPathNo;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsPositionNo;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsCname;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsPortName;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsSerialStatus;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsQrResponse;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsQrData;
	 * 
	 * @FXML
	 * private TableColumn<TestInterfaceStatus, String> colTsStatus;
	 */

	// Position Loaded STATIC Attributes

	private static TableView<PositionLoadedModel> ref_tvLoadedPosition;

	private static TableColumn<PositionLoadedModel, String> ref_colLpPositonNo;

	private static TableColumn ref_colLpSelect;

	private static TableColumn<PositionLoadedModel, String> ref_colLpSerialNo;

	private static TableColumn<PositionLoadedModel, String> ref_colLpCname;

	private static Button ref_btnRefresh;

	// private static Button ref_btnFilter;

	private static Button ref_btnSampleData;

	// Test Status STATIC Attributes

	/* private static TableView<TestInterfaceStatus> ref_tvTestStatus; */

	/*
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsSerialNo;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsBayName;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsStateName;
	 * 
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsDeviceType;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsPathNo;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsPositionNo;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsCname;
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsPortName;
	 * 
	 * private static TableColumn<TestInterfaceStatus, String>
	 * ref_colTsSerialStatus;
	 * 
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsQrResponse;
	 * 
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsQrData;
	 * 
	 * private static TableColumn<TestInterfaceStatus, String> ref_colTsStatus;
	 * 
	 * public static ArrayList<TestInterfaceStatus> allData = new
	 * ArrayList<TestInterfaceStatus>();
	 */

	Timer sendCommDataTaskTimer;
	// Timer clusterSelectionOnChangeTimer;
	Timer posLoadedClusterSelectionOnChangeTimer;
	// Timer baySelectionOnChangeTimer;
	Timer posLoadedBaySelectionOnChangeTimer;
	Timer PosLoadedDeviceTypeSelectionOnChangeTimer;
	Timer btnRefreshTaskTimer;
	// Timer loadOnClickTimer;

	/*
	 * Timer funtionalBayStartTaskTimer;
	 * Timer calibrationStartTaskTimer;
	 * Timer insResStartTaskTimer;
	 * Timer hvtBayStartTaskTimer;
	 * Timer verificStartTaskTimer;
	 * Timer commStartTaskTimer;
	 * Timer sctNlt1StartTaskTimer;
	 * Timer sctNlt2StartTaskTimer;
	 * 
	 * Timer funtionalBayStopTaskTimer;
	 * Timer calibrationStopTaskTimer;
	 * Timer insResStopTaskTimer;
	 * Timer hvtBayStopTaskTimer;
	 * Timer verificStopTaskTimer ;
	 * Timer commStopTaskTimer ;
	 * Timer sctNlt2StopTaskTimer ;
	 * Timer sctNlt1StopTaskTimer ;
	 * 
	 * Timer funtionalBayResetTaskTimer;
	 * Timer calibrationResetTaskTimer;
	 * Timer insResResetTaskTimer;
	 * Timer hvtBayResetTaskTimer;
	 * Timer verificResetTaskTimer ;
	 * Timer commResetTaskTimer ;
	 * Timer sctNlt1ResetTaskTimer ;
	 * Timer sctNlt2ResetTaskTimer;
	 * 
	 * Timer btnRefreshTaskTimer;
	 * Timer btnFilterTaskTimer;
	 * Timer btnSampleDataTaskTimer;
	 * 
	 * @FXML private Button btnAllStart;
	 * 
	 * @FXML private Button btnAllStop;
	 * 
	 * @FXML private Button btnAllReset;
	 */

	/*
	 * @FXML private Button btnFtStart;
	 * static private Button ref_btnFtStart;
	 * 
	 * @FXML private Button btnFtStop;
	 * 
	 * @FXML private Button btnHvtStart;
	 * 
	 * @FXML private Button btnHvtStop;
	 * 
	 * @FXML private Button btnIrtStart;
	 * 
	 * @FXML private Button btnIrtStop;
	 * 
	 * @FXML private Button btnCalibStart;
	 * 
	 * @FXML private Button btnCalibStop;
	 * 
	 * @FXML private Button btnVerificTestStart;
	 * 
	 * @FXML private Button btnVerificTestStop;
	 * 
	 * @FXML private Button btnSctNlt1Start;
	 * 
	 * @FXML private Button btnSctNlt1Stop;
	 * 
	 * @FXML private Button btnSctNlt2Start;
	 * 
	 * @FXML private Button btnSctNlt2Stop;
	 * 
	 * @FXML private Button btnCommTestStart;
	 * 
	 * @FXML private Button btnCommTestStop;
	 */

	/*
	 * @FXML
	 * private Button btnSendDataToBay;
	 * public static Button ref_btnSendDataToBay;
	 * 
	 * @FXML
	 * private TableView<OutputPort> tbViewOutputPortData;
	 * public static TableView<OutputPort> ref_tbViewOutputPortData;
	 * 
	 * 
	 * @FXML
	 * private TableColumn<OutputPort,String> tblColOutputSerialNo;
	 * public static TableColumn<OutputPort,String> ref_tblColOutputSerialNo;
	 * 
	 * @FXML
	 * private TableColumn<OutputPort,String> tblColOutputPortStateDescription;
	 * public static TableColumn<OutputPort,String>
	 * ref_tblColOutputPortStateDescription;
	 * 
	 * 
	 * 
	 * 
	 * @FXML
	 * private TableColumn<OutputPort,String> tblColOutputPortName;
	 * public static TableColumn<OutputPort,String> ref_tblColOutputPortName;
	 * 
	 * @FXML
	 * private TableColumn tblColOutputActive;
	 * public static TableColumn ref_tblColOutputActive;
	 * 
	 * @FXML
	 * private TableColumn tblColOutputUpdateBay;
	 * public static TableColumn ref_tblColOutputUpdateBay;
	 * 
	 * 
	 * 
	 * @FXML
	 * private TableView<InputPort> tbViewInputPortData;
	 * public static TableView<InputPort> ref_tbViewInputPortData;
	 * 
	 * @FXML
	 * private TableColumn<InputPort,String> tblColInputSerialNo;
	 * public static TableColumn<InputPort,String> ref_tblColInputSerialNo;
	 * 
	 * @FXML
	 * private TableColumn<InputPort,String> tblColInputPortStateDescription;
	 * public static TableColumn<InputPort,String>
	 * ref_tblColInputPortStateDescription;
	 * 
	 * 
	 * 
	 * @FXML
	 * private TableColumn<InputPort,String> tblColInputPortName;
	 * public static TableColumn<InputPort,String> ref_tblColInputPortName;
	 * 
	 * @FXML
	 * private TableColumn tblColInputActive;
	 * public static TableColumn ref_tblColInputActive;
	 * 
	 * @FXML
	 * private TableColumn tblColInputReadBay;
	 * public static TableColumn ref_tblColInputReadBay;
	 * 
	 * 
	 * 
	 * @FXML
	 * private ComboBox cmbBxBaySelection;
	 * public static ComboBox ref_cmbBxBaySelection;
	 * 
	 * @FXML
	 * private ComboBox cmbBxClusterSelection;
	 * public static ComboBox ref_cmbBxClusterSelection;
	 */

	@FXML
	private ComboBox cmbBoxCommMode;
	public static ComboBox ref_cmbBoxCommMode;

	/*
	 * @FXML
	 * private TextField txtClusterIpAddress;
	 * public static TextField ref_txtClusterIpAddress;
	 * 
	 * @FXML
	 * private TextField txtClusterPortNo;
	 * public static TextField ref_txtClusterPortNo;
	 */

	// Position Loaded Attributes
	@FXML
	private ComboBox cmbBxPosLoadedTerminalSelection;
	private static ComboBox ref_cmbBxPosLoadedTerminalSelection;

	/*
	 * @FXML
	 * private ComboBox cmbBxPosLoadedDeviceSelection;
	 * private static ComboBox ref_cmbBxPosLoadedDeviceSelection;
	 */

	@FXML
	private ComboBox cmbBxPosLoadedDeviceTypeSelection;
	public static ComboBox ref_cmbBxPosLoadedDeviceTypeSelection;

	@FXML
	private ComboBox cmbBxPosLoadedClusterSelection;
	private static ComboBox ref_cmbBxPosLoadedClusterSelection;

	@FXML
	private ComboBox cmbBxPosLoadedBaySelection;
	private static ComboBox ref_cmbBxPosLoadedBaySelection;

	/*
	 * @FXML
	 * private ComboBox cmbBxFilterPosition;
	 * private static ComboBox ref_cmbBxFilterPosition;
	 */

	@FXML
	private CheckBox chkBxGreenLedData;
	public static CheckBox ref_chkBxGreenLedData;

	@FXML
	private CheckBox chkBxRedLedData;
	public static CheckBox ref_chkBxRedLedData;

	@FXML
	private CheckBox chkBxYellowLedData;
	public static CheckBox ref_chkBxYellowLedData;

	@FXML
	private CheckBox chkBxWriteGreenLed;
	public static CheckBox ref_chkBxWriteGreenLed;

	@FXML
	private CheckBox chkBxWriteRedLed;
	public static CheckBox ref_chkBxWriteRedLed;

	@FXML
	private CheckBox chkBxWriteYellowLed;
	public static CheckBox ref_chkBxWriteYellowLed;

	@FXML
	private TextArea txtAreaResponseDisplay;
	public static TextArea ref_txtAreaResponseDisplay;

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();

	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	// private Map<String,String> bayNameIdListMap = new HashMap<String,String>();
	// private Map<String,ArrayList<String>> bayNameListMap = new
	// HashMap<String,ArrayList<String>>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		logger.info("This is message from ConveyorDebug");
		;
		refInit();
		guiInit();
		dataSetupInit();
		loadAllChild_FXML();
	}

	// private void logInit() {

	// }

	public void loadAllChild_FXML() {
		ApplicationLauncher.logger.info("loadAllChild_FXML :Entry");

		Platform.runLater(() -> {
			try {

				statePlannerChild.getChildren()
						.add(getNodeFromFXML("/fxml/conveyor/StatePlanner" + ConstantApp.THEME_FXML));
				// InitCounter--;

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger
						.error("ConveyorDebugController : loadAllChild_FXML:  Exception:" + e.getMessage());
			}
		});

		Platform.runLater(() -> {
			try {

				stateExecutorChild.getChildren()
						.add(getNodeFromFXML("/fxml/conveyor/StateExecutor" + ConstantApp.THEME_FXML));
				// InitCounter--;

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger
						.error("ConveyorDebugController : loadAllChild_FXML:  Exception:" + e.getMessage());
			}
		});

		Platform.runLater(() -> {
			try {

				bayTestChild.getChildren().add(getNodeFromFXML("/fxml/conveyor/BayTest" + ConstantApp.THEME_FXML));
				// InitCounter--;

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger
						.error("ConveyorDebugController : loadAllChild_FXML:  Exception:" + e.getMessage());
			}
		});

		Platform.runLater(() -> {
			try {

				plcClientBayTestChild.getChildren()
						.add(getNodeFromFXML("/fxml/conveyor/PlcClientBayTest" + ConstantApp.THEME_FXML));
				// InitCounter--;

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error(
						"ConveyorDebugController : loadAllChild_FXML: PlcClientBayTest:  Exception:" + e.getMessage());
			}
		});

		Platform.runLater(() -> {
			try {

				plcServerBayTestChild.getChildren()
						.add(getNodeFromFXML("/fxml/conveyor/PlcServerBayTest" + ConstantApp.THEME_FXML));
				// InitCounter--;

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error(
						"ConveyorDebugController : loadAllChild_FXML: PlcServerBayTest:  Exception:" + e.getMessage());
			}
		});
	}

	private Parent getNodeFromFXML(String url) throws IOException {
		return FXMLLoader.load(getClass().getResource(url));
	}

	private void dataSetupInit() {

		// TerminalBayConfigModel bayConfigModel =
		// ConveyorDeviceDataManagerController.getBayConfigParsedKey();
		ref_cmbBoxCommMode.getItems().add("Ethernet");
		ref_cmbBoxCommMode.getSelectionModel().select(0);
		loadDataFromConfig();

		/*
		 * ref_tbViewOutputPortData.getItems().clear();
		 * ref_tbViewInputPortData.getItems().clear();
		 * ref_txtClusterIpAddress.setText("");
		 * ref_txtClusterPortNo.setText("");
		 */

		/*
		 * PositionLoadedModel dataSet1 = new PositionLoadedModel("1", "20");
		 * dataSet1.setPositionSelected(true);
		 * PositionLoadedModel dataSet2 = new PositionLoadedModel("2", "21");
		 * ref_tvLoadedPosition.getItems().add(dataSet1);
		 * ref_tvLoadedPosition.getItems().add(dataSet2);
		 */

		/*
		 * TestIntefaceStatus dataSet1 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()), ConstantConveyor.FT_BAY_KEY,
		 * ConstantBayStateManage.FT_BAY_HP_SEQ_01,ConstantConveyor.
		 * DEVICE_TYPE_CLUSTER_OUTPUT, "0","STPR_B4_FT_BAY",
		 * ConstantConveyor.COMM_STATUS_NOT_APPLICABLE, "Success", "On",
		 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * TestIntefaceStatus dataSet2 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_02,
		 * ConstantConveyor.DEVICE_TYPE_QR_SCANNER, "0","Com9-FT_BAY_QR_SCNR_PALLET",
		 * "Closed", "Success", "PLT-123456",
		 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * TestIntefaceStatus dataSet3 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_03,
		 * ConstantConveyor.DEVICE_TYPE_DUT, "1","Com10-FT_BAY_DUT1", "Closed",
		 * "Success", "SN-987654", ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * TestIntefaceStatus dataSet4 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_04,
		 * ConstantConveyor.DEVICE_TYPE_DUT, "1","Com10-FT_BAY_DUT1", "Closed",
		 * "Success", "Dut-Relay-On", ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * TestIntefaceStatus dataSet5 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_05,
		 * ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT, "0","SRC_START_STOP_FT_BAY",
		 * ConstantConveyor.COMM_STATUS_NOT_APPLICABLE, "Success", "FtSrcOn",
		 * ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);
		 * TestIntefaceStatus dataSet6 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_06,
		 * ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT, "1","LDU_SCANNER1_IP_FT_BAY",
		 * ConstantConveyor.COMM_STATUS_NOT_APPLICABLE, "", "",
		 * ConstantConveyor.COMM_EXECUTION_STATUS_INP);
		 * TestIntefaceStatus dataSet7 = new
		 * TestIntefaceStatus(String.valueOf(getSerialNoTestStatusAtomic().
		 * getAndIncrement()),
		 * ConstantConveyor.FT_BAY_KEY,ConstantBayStateManage.FT_BAY_HP_SEQ_07,
		 * ConstantConveyor.DEVICE_TYPE_CLUSTER_INPUT, "1","STPR_B4_FT_BAY", "", "", "",
		 * ConstantConveyor.COMM_EXECUTION_STATUS_PENDING);
		 * 
		 * 
		 * ref_tvTestStatus.getItems().add(dataSet1);
		 * ref_tvTestStatus.getItems().add(dataSet2);
		 * ref_tvTestStatus.getItems().add(dataSet3);
		 * ref_tvTestStatus.getItems().add(dataSet4);
		 * ref_tvTestStatus.getItems().add(dataSet5);
		 * ref_tvTestStatus.getItems().add(dataSet6);
		 * ref_tvTestStatus.getItems().add(dataSet7);
		 */
		/*
		 * ref_tvTestStatus.getItems().add(dataSet8);
		 * ref_tvTestStatus.getItems().add(dataSet9);
		 * ref_tvTestStatus.getItems().add(dataSet10);
		 * ref_tvTestStatus.getItems().add(dataSet11);
		 * ref_tvTestStatus.getItems().add(dataSet12);
		 */

		ref_cmbBxPosLoadedTerminalSelection.getItems().add("1");
		ref_cmbBxPosLoadedTerminalSelection.getSelectionModel().select(0);

		ref_cmbBxPosLoadedDeviceTypeSelection.getItems().addAll(ConstantConveyor.DEVICE_TYPE_LIST);
		ref_cmbBxPosLoadedDeviceTypeSelection.getSelectionModel().select(0);
	}

	/*
	 * public static int addToTestStatusGui(TestInterfaceStatus testIntefaceStatus){
	 * testIntefaceStatus.setSerialNo(String.valueOf(getSerialNoTestStatusAtomic().
	 * get()));
	 * ref_tvTestStatus.getItems().add(testIntefaceStatus);
	 * ref_tvTestStatus.refresh();
	 * allData.add(testIntefaceStatus);
	 * return getSerialNoTestStatusAtomic().getAndIncrement();
	 * }
	 * 
	 * public static void updateTestInterfaceStatusOnGui(Map<String,Object>
	 * responseReturn,String testStatus){
	 * TestInterfaceStatus test_I_F_Status =
	 * (TestInterfaceStatus)responseReturn.get("testInterfaceStatus");
	 * test_I_F_Status.setTestStatus(testStatus);
	 * updateTestStatusGui(test_I_F_Status);
	 * }
	 * 
	 * 
	 * public static void updateTestInterfaceStatusOnGuiV2(BayResponse
	 * bayResponse,String testStatus){
	 * TestInterfaceStatus test_I_F_Status = bayResponse.getTestInterfaceStatus();
	 * test_I_F_Status.setTestStatus(testStatus);
	 * updateTestStatusGui(test_I_F_Status);
	 * }
	 */
	/*
	 * public static void updateTestStatusGui(TestInterfaceStatus
	 * testIntefaceStatus){
	 * 
	 * ref_tvTestStatus.getItems().stream()
	 * .filter(e->e.getSerialNo().equals(testIntefaceStatus.getSerialNo()))
	 * .filter(e->e.getBayName().equals(testIntefaceStatus.getBayName()))
	 * .filter(e->e.getStateName().equals(testIntefaceStatus.getStateName()))
	 * .filter(e->e.getcName().equals(testIntefaceStatus.getcName()))
	 * //.filter(e->e.getPortId().equals(testIntefaceStatus.getPortId()))
	 * .forEach(e->{
	 * 
	 * //ApplicationLauncher.logger.debug("updateTestStatusGui: getTestStatus: " +
	 * testIntefaceStatus.getTestStatus());
	 * e.setTestStatus(testIntefaceStatus.getTestStatus());
	 * e.setDeviceResponseStatus(testIntefaceStatus.getDeviceResponseStatus());
	 * e.setDeviceResponseData(testIntefaceStatus.getDeviceResponseData());
	 * e.setPortName(testIntefaceStatus.getPortName());
	 * //e.setPositionNo(testIntefaceStatus.getPositionNo());
	 * });
	 * 
	 * 
	 * 
	 * ref_tvTestStatus.refresh();
	 * }
	 */
	public void guiInit() {

		/*
		 * ref_tblColOutputSerialNo.setCellValueFactory(new
		 * PropertyValueFactory<OutputPort, String>("serialNo"));
		 * ref_tblColOutputPortName.setCellValueFactory(new
		 * PropertyValueFactory<OutputPort, String>("portName"));
		 * ref_tblColOutputActive.setCellValueFactory(new
		 * OutputPortActiveCheckBoxValueFactory());
		 * ref_tblColOutputActive.setStyle( "-fx-alignment: CENTER;");
		 * ref_tblColOutputUpdateBay.setCellValueFactory(new
		 * OutputPortUpdateBayCheckBoxValueFactory());
		 * ref_tblColOutputUpdateBay.setStyle( "-fx-alignment: CENTER;");
		 * ref_tblColOutputPortStateDescription.setCellValueFactory(new
		 * PropertyValueFactory<OutputPort, String>("stateDescription"));
		 * 
		 * 
		 * ref_tblColInputSerialNo.setCellValueFactory(new
		 * PropertyValueFactory<InputPort, String>("serialNo"));
		 * ref_tblColInputPortName.setCellValueFactory(new
		 * PropertyValueFactory<InputPort, String>("portName"));
		 * ref_tblColInputActive.setEditable(false);
		 * ref_tblColInputActive.setCellValueFactory(new
		 * InputPortActiveCheckBoxValueFactory());
		 * ref_tblColInputActive.setStyle( "-fx-alignment: CENTER;");
		 * ref_tblColInputReadBay.setCellValueFactory(new
		 * InputPortReadBayCheckBoxValueFactory());
		 * ref_tblColInputReadBay.setStyle( "-fx-alignment: CENTER;");
		 * ref_tblColInputPortStateDescription.setCellValueFactory(new
		 * PropertyValueFactory<InputPort, String>("stateDescription"));
		 * 
		 */

		// ref_colLpPositonNo.setCellValueFactory(new
		// PropertyValueFactory<PositionLoadedModel, String>("positionNo"));
		// ref_colLpSerialNo.setCellValueFactory(new
		// PropertyValueFactory<PositionLoadedModel, String>("serialNo"));

		// P O S I T I O N L O A D E D - Column Values
		ref_colLpPositonNo.setCellValueFactory(cellData -> cellData.getValue().getPositionNoProperty());
		ref_colLpSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		ref_colLpCname.setCellValueFactory(cellData -> cellData.getValue().getcNameProperty());
		ref_colLpSelect.setEditable(true);
		ref_colLpSelect.setCellValueFactory(new PositionLoadedSelectCheckBoxValueFactory());
		ref_colLpSelect.setStyle("-fx-alignment: CENTER;");

		/*
		 * //T E S T S T A T U S - Column Values
		 * ref_colTsSerialNo.setCellValueFactory(cellData ->
		 * cellData.getValue().getSerialNoProperty());
		 * ref_colTsBayName.setCellValueFactory(cellData ->
		 * cellData.getValue().getBayNameProperty());
		 * ref_colTsStateName.setCellValueFactory(cellData ->
		 * cellData.getValue().getStateNameProperty());
		 * ref_colTsDeviceType.setCellValueFactory(cellData ->
		 * cellData.getValue().getDeviceTypeProperty());
		 * ref_colTsPathNo.setCellValueFactory(cellData ->
		 * cellData.getValue().getPathNoProperty());
		 * ref_colTsPositionNo.setCellValueFactory(cellData ->
		 * cellData.getValue().getPositionNoProperty());
		 * ref_colTsCname.setCellValueFactory(cellData ->
		 * cellData.getValue().getcNameProperty());
		 * ref_colTsPortName.setCellValueFactory(cellData ->
		 * cellData.getValue().getPortNameProperty());
		 * ref_colTsSerialStatus.setCellValueFactory(cellData ->
		 * cellData.getValue().getSerialStatusProperty());
		 * ref_colTsQrResponse.setCellValueFactory(cellData ->
		 * cellData.getValue().getDeviceResponseStatusProperty());
		 * ref_colTsQrData.setCellValueFactory(cellData ->
		 * cellData.getValue().getDeviceResponseDataProperty());
		 * ref_colTsStatus.setCellValueFactory(cellData ->
		 * cellData.getValue().getTestStatusProperty());
		 */
		// statePlannerGuiInit();

		ref_tabBayTest.setDisable(true);

		SingleSelectionModel<Tab> selectionModel = ref_tabPaneConveyor.getSelectionModel();

		selectionModel.select(ref_tabPlcClientBayTest);
	}
	// ===========================================================================================================================================================================
	/*
	 * public void statePlannerGuiInit() {
	 * 
	 * 
	 * //==================== STATE PLANNER
	 * ==================================================================//
	 * ApplicationLauncher.logger.info("StatePlanner : initialize : Entry");
	 * 
	 * columnPath.setCellValueFactory(data -> data.getValue().pathProperty());
	 * columnState.setCellValueFactory(data -> data.getValue().stateProperty());
	 * columnStateErrorCode.setCellValueFactory(data ->
	 * data.getValue().stateErrorCodeProperty());
	 * columnSuccess.setCellValueFactory(data -> data.getValue().successProperty());
	 * columnSuccessErrorCode.setCellValueFactory(data ->
	 * data.getValue().successErrorCodeProperty());
	 * columnFailed.setCellValueFactory(data -> data.getValue().failedProperty());
	 * columnFailedErrorCode.setCellValueFactory(data ->
	 * data.getValue().failedErrorCodeProperty());
	 * 
	 * // Set the columns as editable
	 * tableStatePlanner.setEditable(true);
	 * 
	 * columnState.setEditable(true);
	 * columnSuccess.setEditable(true);
	 * 
	 * 
	 * // Set ComboBox for State and Success columns (dropdown)
	 * columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * 
	 * // No drop-down for the Failed column, it will auto-update based on other
	 * columns
	 * // columnFailed.setCellFactory(TextFieldTableCell.forTableColumn());
	 * 
	 * // Populate table with initial dummy data
	 * stateFlowRows.addAll(
	 * new StateFlowRow("P1", "Select State", "No State Selected", "Select State",
	 * "No State Selected", "No State Selected", "No State Selected")
	 * new StateFlowRow("P2", "Check Stopper Before Bay Status", "ERROR_CODE_002",
	 * "Open Stopper Before Bay", "ERROR_CODE_102", "Error Handling",
	 * "ERROR_CODE_202"),
	 * new StateFlowRow("P3", "Open Stopper Before Bay", "ERROR_CODE_003",
	 * "Close Stopper Before Bay", "ERROR_CODE_103", "Error Handling",
	 * "ERROR_CODE_203"),
	 * new StateFlowRow("P4", "Close Stopper Before Bay", "ERROR_CODE_004",
	 * "Close FingerTip Latch", "ERROR_CODE_104", "Error Handling",
	 * "ERROR_CODE_204"),
	 * new StateFlowRow("P5", "Close FingerTip Latch", "ERROR_CODE_005",
	 * "Ensure FingerTip Latch Closed", "ERROR_CODE_105", "Error Handling",
	 * "ERROR_CODE_205"),
	 * new StateFlowRow("P6", "Ensure FingerTip Latch Closed", "ERROR_CODE_006",
	 * "Soucre Start", "ERROR_CODE_106", "Error Handling", "ERROR_CODE_206"),
	 * new StateFlowRow("P7", "Soucre Start", "ERROR_CODE_007",
	 * "Ensure Source Started", "ERROR_CODE_107", "Error Handling",
	 * "ERROR_CODE_207"),
	 * new StateFlowRow("P8", "Ensure Source Started", "ERROR_CODE_008",
	 * "Functional Test", "ERROR_CODE_108", "Error Handling", "ERROR_CODE_208"),
	 * new StateFlowRow("P9", "Functional Test", "ERROR_CODE_009", "Source Stop",
	 * "ERROR_CODE_109", "Error Handling", "ERROR_CODE_209"),
	 * new StateFlowRow("P10", "Source Stop", "ERROR_CODE_010",
	 * "Ensure Source Stopped", "ERROR_CODE_110", "Error Handling",
	 * "ERROR_CODE_210"),
	 * new StateFlowRow("P11", "Ensure Source Stopped", "ERROR_CODE_011",
	 * "Close Divertor Relay", "ERROR_CODE_111", "Error Handling",
	 * "ERROR_CODE_211"),
	 * new StateFlowRow("P12", "Close Divertor Relay", "ERROR_CODE_012",
	 * "Open Divertor Relay", "ERROR_CODE_112", "Error Handling", "ERROR_CODE_212"),
	 * new StateFlowRow("P13", "Open Divertor Relay", "ERROR_CODE_013",
	 * "Open FingerTip Latch", "ERROR_CODE_113", "Error Handling",
	 * "ERROR_CODE_213"),
	 * new StateFlowRow("P14", "Open FingerTip Latch", "ERROR_CODE_014",
	 * "Ensure FingerTip Latch Opened", "ERROR_CODE_114", "Error Handling",
	 * "ERROR_CODE_214"),
	 * new StateFlowRow("P15", "Ensure FingerTip Latch Opened", "ERROR_CODE_015",
	 * "Open Stopper At Bay", "ERROR_CODE_115", "Error Handling", "ERROR_CODE_215"),
	 * new StateFlowRow("P16", "Open Stopper At Bay", "ERROR_CODE_016",
	 * "Close Stopper At Bay", "ERROR_CODE_116", "Error Handling",
	 * "ERROR_CODE_216"),
	 * new StateFlowRow("P17", "Close Stopper At Bay", "ERROR_CODE_017",
	 * "Check for Pallet", "ERROR_CODE_117", "Error Handling", "ERROR_CODE_217"),
	 * new StateFlowRow("P18", "Error Handling", "ERROR_CODE_018",
	 * "Check for Pallet", "ERROR_CODE_118", "Error Handling", "ERROR_CODE_218")
	 * );
	 * 
	 * tableStatePlanner.setItems(stateFlowRows);
	 * tableStatePlanner.refresh();
	 * 
	 * // Initialize stateCodeMap
	 * initializeStateCodeMap();
	 * // Set up the table (this includes setting up ComboBox for the State column)
	 * setupTable();
	 * 
	 * 
	 * 
	 * }
	 */
	// ===========================================================================================================================================================================

	public void loadDataFromConfig() {
		// B A Y T E S T ====================================================
		ApplicationLauncher.logger.debug("loadDataFromConfig: Entry");
		/* ref_cmbBxClusterSelection.getItems().clear(); */
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					// ref_cmbBxClusterSelection.getItems().add(eachClusterDetail.getName());
					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);

					for (Bay eachBay : eachClusterDetail.getBay()) {
						// ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						// getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						getClusterBayNameIdMap().put(eachClusterDetail.getName() + "_" + eachBay.getBayName(),
								eachBay.getBayId());
						// ApplicationLauncher.logger.debug("loadDataFromConfig :
						// getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}
			}
		}
		/*
		 * if(ref_cmbBxClusterSelection.getItems().size()>0){
		 * ref_cmbBxClusterSelection.getSelectionModel().select(0);
		 * 
		 * }
		 * 
		 * if(getClusterBayNameListMap().size()>0){
		 * if(getClusterBayNameListMap().containsKey(ref_cmbBxClusterSelection.
		 * getSelectionModel().getSelectedItem().toString())){
		 * ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(
		 * ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString()));
		 * }
		 * //ref_cmbBxBaySelection.getSelectionModel().select(0);
		 * 
		 * }
		 * 
		 * if(ref_cmbBxBaySelection.getItems().size()>0){
		 * ref_cmbBxBaySelection.getSelectionModel().select(0);
		 * String clusterName =
		 * (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
		 * String bayName =
		 * (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());
		 * 
		 * String clusterId = getClusterNameIdListMap().get(clusterName);
		 * //ApplicationLauncher.logger.
		 * debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-2 :"+
		 * getClusterBayNameIdMap().get(clusterName));
		 * //String bayId =
		 * "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);
		 * 
		 * ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :"+
		 * clusterId);
		 * String clusterIpAddress = "";
		 * String clusterPortNo = "";
		 * 
		 * Optional<ClusterDetail> clusterOpt =
		 * getBayConfigModel().getTerminal().stream()
		 * .filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
		 * .flatMap(terminal -> terminal.getClusterDetails().stream())
		 * .filter(e2->e2.getClusterId().equals(clusterId))
		 * .findFirst();
		 * 
		 * if (clusterOpt.isPresent()) {
		 * ClusterDetail clusterDetail = clusterOpt.get();
		 * ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
		 * ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());
		 * 
		 * }
		 * //Optional<OutputPort> outputPortOpt =
		 * 
		 * Optional<Bay> bayOpt = getBayConfigModel().getTerminal().stream()
		 * .filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
		 * .flatMap(terminal -> terminal.getClusterDetails().stream())
		 * .filter(e2->e2.getClusterId().equals(clusterId))
		 * .flatMap(e3 -> e3.getBay().stream())
		 * .filter(e4->e4.getBayName().equals(bayName))
		 * .findFirst();
		 * 
		 * if (bayOpt.isPresent()) {
		 * Bay bayDetails = bayOpt.get();
		 * String bayId = bayDetails.getBayId();
		 * ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :"+ bayId);
		 * 
		 * ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>)
		 * getBayConfigModel().getTerminal().stream()
		 * .flatMap(terminal -> terminal.getOutputPort().stream())
		 * .filter(p -> clusterId.equals(p.getClusterId()))
		 * .filter(p -> bayId.equals(p.getBayId()))
		 * .collect(Collectors.toList());
		 * 
		 * if (outputPortList.size()>0) {
		 * int serialNo = 1;
		 * for(OutputPort eachOutputPort : outputPortList) {
		 * eachOutputPort.setSerialNo(String.valueOf(serialNo));
		 * serialNo++;
		 * }
		 * ref_tbViewOutputPortData.getItems().addAll(outputPortList);
		 * 
		 * }
		 * 
		 * ArrayList<InputPort> inputPortList = (ArrayList<InputPort>)
		 * getBayConfigModel().getTerminal().stream()
		 * .flatMap(terminal -> terminal.getInputPort().stream())
		 * .filter(p -> clusterId.equals(p.getClusterId()))
		 * .filter(p -> bayId.equals(p.getBayId()))
		 * .collect(Collectors.toList());
		 * 
		 * if (inputPortList.size()>0) {
		 * int serialNo = 1;
		 * for(InputPort eachInputPort : inputPortList) {
		 * eachInputPort.setSerialNo(String.valueOf(serialNo));
		 * serialNo++;
		 * }
		 * ref_tbViewInputPortData.getItems().addAll(inputPortList);
		 * }
		 * }
		 * }
		 */

		// P O S I T I O N L O A D E D
		// ========================================================
		ApplicationLauncher.logger.debug("loadDataFromConfig: Entry - Position Loaded");
		ref_cmbBxPosLoadedClusterSelection.getItems().clear();
		// ref_cmbBxPosLoadedTerminalSelection.setId("1");
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) { // Get cluster details
					ref_cmbBxPosLoadedClusterSelection.getItems().add(eachClusterDetail.getName()); // Add details to
																									// Combo Box
					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>(); // Create a Bay Array
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList); // Map Bay and Cluster

					for (Bay eachBay : eachClusterDetail.getBay()) {
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
					}
				}
			}
		}

		if (ref_cmbBxPosLoadedClusterSelection.getItems().size() > 0) {
			ref_cmbBxPosLoadedClusterSelection.getSelectionModel().select(0);
		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxPosLoadedClusterSelection.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxPosLoadedBaySelection.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxPosLoadedClusterSelection.getSelectionModel().getSelectedItem().toString()));
			}
		}

		if (ref_cmbBxPosLoadedBaySelection.getItems().size() > 0) {
			ref_cmbBxPosLoadedBaySelection.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxPosLoadedClusterSelection.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxPosLoadedBaySelection.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			String clusterIpAddress = "";
			String clusterPortNo = "";

			Optional<ClusterDetail> clusterOpt = getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2 -> e2.getClusterId().equals(clusterId))
					.findFirst();

			Optional<Bay> bayOpt = getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getClusterDetails().stream())
					.filter(e2 -> e2.getClusterId().equals(clusterId))
					.flatMap(e3 -> e3.getBay().stream())
					.filter(e4 -> e4.getBayName().equals(bayName))
					.findFirst();

			if (bayOpt.isPresent()) {
				Bay bayDetails = bayOpt.get();
				String bayId = bayDetails.getBayId();
				ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

				ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>) getBayConfigModel().getTerminal()
						.stream()
						.flatMap(terminal -> terminal.getOutputPort().stream())
						.filter(p -> clusterId.equals(p.getClusterId()))
						.filter(p -> bayId.equals(p.getBayId()))
						.collect(Collectors.toList());
			}
		}
		/*
		 * // F I L T E R C O M B O B O X
		 * ====================================================================
		 * ref_cmbBxFilterPosition.getItems().clear();
		 * ref_cmbBxFilterPosition.getItems().add("All");
		 * for (int i = 0; i <= 6; i ++) {
		 * ref_cmbBxFilterPosition.getItems().add(i);
		 * }
		 */

	}

	private void refInit() {

		ref_chkBxGreenLedData = chkBxGreenLedData;
		ref_chkBxRedLedData = chkBxRedLedData;
		ref_chkBxYellowLedData = chkBxYellowLedData;

		ref_chkBxWriteGreenLed = chkBxWriteGreenLed;
		ref_chkBxWriteRedLed = chkBxWriteRedLed;
		ref_chkBxWriteYellowLed = chkBxWriteYellowLed;

		ref_cmbBoxCommMode = cmbBoxCommMode;
		ref_cmbBxPosLoadedDeviceTypeSelection = cmbBxPosLoadedDeviceTypeSelection;
		ref_txtAreaResponseDisplay = txtAreaResponseDisplay;

		/*
		 * ref_cmbBxBaySelection= cmbBxBaySelection;
		 * ref_cmbBxClusterSelection = cmbBxClusterSelection;
		 */
		ref_cmbBxPosLoadedBaySelection = cmbBxPosLoadedBaySelection;
		ref_cmbBxPosLoadedClusterSelection = cmbBxPosLoadedClusterSelection;
		ref_cmbBxPosLoadedTerminalSelection = cmbBxPosLoadedTerminalSelection;

		// ref_cmbBxFilterPosition = cmbBxFilterPosition;
		/*
		 * 
		 * ref_txtClusterIpAddress = txtClusterIpAddress;
		 * 
		 * ref_txtClusterPortNo = txtClusterPortNo;
		 * 
		 * 
		 * 
		 * ref_tbViewOutputPortData = tbViewOutputPortData;
		 * ref_tblColOutputSerialNo = tblColOutputSerialNo;
		 * ref_tblColOutputPortName = tblColOutputPortName;
		 * ref_tblColOutputActive =tblColOutputActive;
		 * ref_tblColOutputUpdateBay = tblColOutputUpdateBay;
		 * ref_tblColOutputPortStateDescription = tblColOutputPortStateDescription;
		 * 
		 * 
		 * ref_tbViewInputPortData = tbViewInputPortData;
		 * ref_tblColInputSerialNo = tblColInputSerialNo;
		 * ref_tblColInputPortName = tblColInputPortName;
		 * ref_tblColInputActive =tblColInputActive;
		 * ref_tblColInputReadBay = tblColInputReadBay;
		 * ref_tblColInputPortStateDescription = tblColInputPortStateDescription;
		 * 
		 * 
		 * ref_btnSendDataToBay = btnSendDataToBay;
		 */
		ref_btnRefresh = btnRefresh;

		// ref_btnFilter = btnFilter;

		ref_btnSampleData = btnSampleData;

		// P O S I T I O N L O A D E D
		ref_tvLoadedPosition = tvLoadedPosition;
		ref_colLpPositonNo = colLpPositonNo;
		ref_colLpSelect = colLpSelect;
		ref_colLpSerialNo = colLpSerialNo;
		ref_colLpCname = colLpCname;

		// T E S T S T A T U S
		/*
		 * ref_tvTestStatus = tvTestStatus;
		 * ref_colTsSerialNo = colTsSerialNo;
		 * ref_colTsBayName = colTsBayName;
		 * ref_colTsStateName = colTsStateName;
		 * ref_colTsDeviceType = colTsDeviceType;
		 * ref_colTsPathNo = colTsPathNo;
		 * ref_colTsPositionNo = colTsPositionNo;
		 * ref_colTsCname = colTsCname;
		 * ref_colTsPortName = colTsPortName;
		 * 
		 * ref_colTsSerialStatus = colTsSerialStatus;
		 * ref_colTsQrResponse = colTsQrResponse;
		 * ref_colTsQrData = colTsQrData;
		 * ref_colTsStatus = colTsStatus;
		 */

		// ref_btnFtStart = btnFtStart;

		ref_tabBayTest = tabBayTest;
		ref_tabPlcClientBayTest = tabPlcClientBayTest;
		ref_tabPaneConveyor = tabPaneConveyor;
		// P A L L E T V I E W E R
		ref_txtCalibBayPallet = txtCalibBayPallet;
		ref_txtCommBayPallet = txtCommBayPallet;
		ref_txtFTBayPallet = txtFTBayPallet;
		ref_txtHVBayPallet = txtHVBayPallet;
		ref_txtIRBayPallet = txtIRBayPallet;
		ref_txtLoadingBayPallet = txtLoadingBayPallet;
		ref_txtRejectionBayPallet = txtRejectionBayPallet;
		ref_txtSCTNLTBay1Pallet1 = txtSCTNLTBay1Pallet1;
		ref_txtSCTNLTBay1Pallet2 = txtSCTNLTBay1Pallet2;
		ref_txtSCTNLTBay1Pallet3 = txtSCTNLTBay1Pallet3;
		ref_txtSCTNLTBay1Pallet4 = txtSCTNLTBay1Pallet4;
		ref_txtSCTNLTBay2Pallet1 = txtSCTNLTBay2Pallet1;
		ref_txtSCTNLTBay2Pallet2 = txtSCTNLTBay2Pallet2;
		ref_txtSCTNLTBay2Pallet3 = txtSCTNLTBay2Pallet3;
		ref_txtSCTNLTBay2Pallet4 = txtSCTNLTBay2Pallet4;
		ref_txtUnloadingBayPallet = txtUnloadingBayPallet;
		ref_txtVerificBayPallet1 = txtVerificBayPallet1;
		ref_txtVerificBayPallet2 = txtVerificBayPallet2;
		ref_txtVerificBayPallet3 = txtVerificBayPallet3;
		ref_txtVerificBayPallet4 = txtVerificBayPallet4;
		ref_txtWaitingBayPallet1 = txtWaitingBayPallet1;
		ref_txtWaitingBayPallet2 = txtWaitingBayPallet2;
		ref_txtWaitingBayPallet3 = txtWaitingBayPallet3;
		ref_txtWaitingBayPallet4 = txtWaitingBayPallet4;

	}

	/*
	 * public void WaitForServerResponse(int WaitTimeInSec){
	 * int SleepCounter = WaitTimeInSec;
	 * 
	 * while ((!ServerProperties.getServerStatus()) && (SleepCounter > 0)){
	 * Sleep(1000);
	 * SleepCounter --;
	 * 
	 * }
	 * }
	 * 
	 * public void Sleep(int timeInMsec) {
	 * 
	 * try {
	 * Thread.sleep(timeInMsec);
	 * } catch (InterruptedException e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("Sleep :InterruptedException:"+
	 * e.getMessage());
	 * }
	 * 
	 * }
	 */

	@FXML
	void btnSendCommDataOnClick() {
		sendCommDataTaskTimer = new Timer();
		// sendCommDataTaskTimer.schedule(new SendCommDataTask(),10);
		// sendCommDataTaskTimer.schedule(new SendCommStartToProcalTask(),10);
		// sendCommDataTaskTimer.schedule(new SendCommStopToProcalTask(),10);
		// sendCommDataTaskTimer.schedule(new SendCommResultRefreshToProcalTask(),10);
	}

	/*
	 * class SendCommResultRefreshToProcalTask extends TimerTask {
	 * public void run() {
	 * ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
	 * ProcalRemoteResponse procalServerResponse =
	 * procalRemoteSender.sendCommResultRefreshToProcal();
	 * ApplicationLauncher.logger.
	 * debug("SendCommResultRefreshToProcalTask: procalServerResponse: " +
	 * procalServerResponse);
	 * //procalRemoteSender.//setProcalVerifyRemoteResponse();
	 * 
	 * }
	 * }
	 * 
	 * class SendCommStartToProcalTask extends TimerTask {
	 * public void run() {
	 * ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
	 * String procalServerResponse = procalRemoteSender.sendStartCommandToProcal();
	 * ApplicationLauncher.logger.
	 * debug("SendCommStartToProcalTask: procalServerResponse: " +
	 * procalServerResponse);
	 * //procalRemoteSender.//setProcalVerifyRemoteResponse();
	 * 
	 * }
	 * }
	 * 
	 * class SendCommStopToProcalTask extends TimerTask {
	 * public void run() {
	 * ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
	 * String procalServerResponse = procalRemoteSender.sendStartCommandToProcal();
	 * ApplicationLauncher.logger.
	 * debug("SendCommStopToProcalTask: procalServerResponse: " +
	 * procalServerResponse);
	 * //procalRemoteSender.//setProcalVerifyRemoteResponse();
	 * 
	 * }
	 * }
	 */

	class SendCommDataTask extends TimerTask {
		public void run() {
			ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
			Platform.runLater(() -> {
				ref_txtAreaResponseDisplay.clear();
			});

			if (ref_chkBxWriteGreenLed.isSelected()) {
				String greenLedStatus = "Off";
				if (ref_chkBxGreenLedData.isSelected()) {
					greenLedStatus = "On";
				}
				String outputPortId = "GLed";
				String deviceId = "Device";
				String bayId = "BayId";
				setOutputDataOnCluster(deviceId, bayId, outputPortId, greenLedStatus);

			}

			if (ref_chkBxWriteRedLed.isSelected()) {
				String redLedStatus = "Off";
				if (ref_chkBxRedLedData.isSelected()) {
					redLedStatus = "On";
				}
				String outputPortId = "RLed";
				String deviceId = "Device";
				String bayId = "BayId";
				setOutputDataOnCluster(deviceId, bayId, outputPortId, redLedStatus);

			}

			if (ref_chkBxWriteYellowLed.isSelected()) {
				String yellowLedStatus = "Off";
				if (ref_chkBxYellowLedData.isSelected()) {
					yellowLedStatus = "On";
				}

				String outputPortId = "YLed";
				String deviceId = "Device";
				String bayId = "BayId";
				setOutputDataOnCluster(deviceId, bayId, outputPortId, yellowLedStatus);

			}

		}
	}

	public boolean setOutputDataOnCluster(String deviceId, String bayId, String outputPortId, String outputStatus) {

		boolean status = false;
		ConveyorClientManager cluster1ClientManager = ConveyorClientManager.getInstance();
		cluster1ClientManager.setData(deviceId, bayId, outputPortId, outputStatus);
		ApplicationHomeController.update_left_status("Awaiting Device Response", ConstantApp.LEFT_STATUS_DEBUG);
		// Sleep(8000);
		cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
		if (cluster1ClientManager.getAsyncConvClient().isResponseReceived()) { // validate for server access
			ApplicationHomeController.update_left_status("Device Connected", ConstantApp.LEFT_STATUS_DEBUG);

			RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
			clusterResponseData = cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
			ApplicationLauncher.logger.info(
					"AsyncConveyorClient: " + outputPortId + " : getDevice Data: " + clusterResponseData.getDevice());
			ApplicationLauncher.logger.info(
					"AsyncConveyorClient: " + outputPortId + " : getStatus Data: " + clusterResponseData.getStatus());

			// String responseData =
			// cluster1ClientManager.getAsyncConvClient().getResponseData();
			/*
			 * Platform.runLater(()->{
			 * ref_txtAreaResponseDisplay.setText(responseData);
			 * });
			 */
			if (ref_txtAreaResponseDisplay.getText().isEmpty()) {
				ref_txtAreaResponseDisplay.setText(clusterResponseData.getMessage());
			} else {
				ref_txtAreaResponseDisplay
						.setText(ref_txtAreaResponseDisplay.getText() + "\n" + clusterResponseData.getMessage());
			}
			status = true;

			// ApplicationHomeController.EnableScanDeviceButton();
		} else {

			// ScanDeviceController.ScanDeviceCompletedPostProcess();
			// ApplicationHomeController.EnableScanDeviceButton();
			// ApplicationHomeController.DisableTestRunButton();
			// ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
			Platform.runLater(() -> {
				ref_txtAreaResponseDisplay
						.setText(ref_txtAreaResponseDisplay.getText() + "\n" + outputPortId + "-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed " + outputPortId + " :",
					ConstantApp.LEFT_STATUS_DEBUG);
		}
		return status;
	}

	/*
	 * public boolean setOutputDataToBay(ClusterServer clusterServer,String
	 * deviceId, String bayId,String outputPortId, String outputStatus) {
	 * 
	 * boolean status = false;
	 * AsyncConveyorClientManager cluster1ClientManager = new
	 * AsyncConveyorClientManager();
	 * cluster1ClientManager.setBayData(clusterServer,deviceId, bayId, outputPortId,
	 * outputStatus);
	 * ApplicationHomeController.update_left_status("Awaiting Device Response"
	 * ,ConstantApp.LEFT_STATUS_DEBUG);
	 * //Sleep(8000);
	 * cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
	 * if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){
	 * //validate for server access
	 * ApplicationHomeController.update_left_status("Device Connected",ConstantApp.
	 * LEFT_STATUS_DEBUG);
	 * 
	 * RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
	 * clusterResponseData =
	 * cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseData();
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: "
	 * +outputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
	 * ApplicationLauncher.logger.info("AsyncConveyorClient: "
	 * +outputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());
	 * 
	 * 
	 * String responseData =
	 * cluster1ClientManager.getAsyncConvClient().getResponseData();
	 * Platform.runLater(()->{
	 * ref_txtAreaResponseDisplay.setText(responseData);
	 * });
	 * if(ref_txtAreaResponseDisplay.getText().isEmpty()) {
	 * ref_txtAreaResponseDisplay.setText(responseData);
	 * }else {
	 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
	 * responseData);
	 * }
	 * status = true;
	 * 
	 * //ApplicationHomeController.EnableScanDeviceButton();
	 * }else{
	 * 
	 * //ScanDeviceController.ScanDeviceCompletedPostProcess();
	 * //ApplicationHomeController.EnableScanDeviceButton();
	 * //ApplicationHomeController.DisableTestRunButton();
	 * //ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
	 * Platform.runLater(()->{
	 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
	 * outputPortId+"-no response");
	 * });
	 * ApplicationHomeController.update_left_status("Device Connection Failed "
	 * +outputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
	 * //status = null;
	 * }
	 * return status;
	 * }
	 * 
	 */

	/*
	 * public RestApiJsonBodyResponse getInputDataFromBay(ClusterServer
	 * clusterServer,String deviceId, String bayId,String inputPortId) {
	 * 
	 * boolean status = false;
	 * AsyncConveyorClientManager cluster1ClientManager = new
	 * AsyncConveyorClientManager();
	 * cluster1ClientManager.getBayData(clusterServer,deviceId, bayId, inputPortId);
	 * ApplicationHomeController.update_left_status("Awaiting Device Response"
	 * ,ConstantApp.LEFT_STATUS_DEBUG);
	 * //Sleep(8000);
	 * RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse();
	 * //clusterResponseData.setStatuscode(statuscode);
	 * //HashMap<Boolean,RestApiClusterResponse> returnData = new
	 * HashMap<Boolean,RestApiClusterResponse>();
	 * cluster1ClientManager.getAsyncConvClient().WaitForServerResponse(8);
	 * if(cluster1ClientManager.getAsyncConvClient().isResponseReceived()){
	 * //validate for server access
	 * ApplicationHomeController.update_left_status("Device Connected",ConstantApp.
	 * LEFT_STATUS_DEBUG);
	 * 
	 * //RestApiClusterResponse clusterResponseData = new RestApiClusterResponse();
	 * clusterResponseData =
	 * cluster1ClientManager.getAsyncConvClient().getRestApiClusterResponseBodyData(
	 * );
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getDevice Data: "+
	 * clusterResponseData.getJsonBodyResponse().get(inputPortId));
	 * 
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getDevice Data: "+ clusterResponseData.getDevice());
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getStatus Data: "+ clusterResponseData.getStatus());
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getStatus getOpGreen: "+ clusterResponseData.getOpGreen());
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getStatus getOpYellow: "+
	 * clusterResponseData.getOpYellow());
	 * ApplicationLauncher.logger.info("getInputDataFromBay: "
	 * +inputPortId+" : getStatus getOpRed: "+ clusterResponseData.getOpRed());
	 * 
	 * 
	 * String responseData =
	 * cluster1ClientManager.getAsyncConvClient().getResponseData();
	 * 
	 * Platform.runLater(()->{
	 * ref_txtAreaResponseDisplay.setText(responseData);
	 * });
	 * if(ref_txtAreaResponseDisplay.getText().isEmpty()) {
	 * ref_txtAreaResponseDisplay.setText(responseData);
	 * }else {
	 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
	 * responseData);
	 * }
	 * status = true;
	 * 
	 * //ApplicationHomeController.EnableScanDeviceButton();
	 * }else{
	 * 
	 * //ScanDeviceController.ScanDeviceCompletedPostProcess();
	 * //ApplicationHomeController.EnableScanDeviceButton();
	 * //ApplicationHomeController.DisableTestRunButton();
	 * //ApplicationHomeController.EnableLeftMenuButtonsForTestRun();
	 * Platform.runLater(()->{
	 * ref_txtAreaResponseDisplay.setText(ref_txtAreaResponseDisplay.getText()+"\n"+
	 * inputPortId+"-no response");
	 * });
	 * ApplicationHomeController.update_left_status("Device Connection Failed "
	 * +inputPortId+" :",ConstantApp.LEFT_STATUS_DEBUG);
	 * }
	 * return clusterResponseData;
	 * }
	 */
	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		ConveyorDebugController.bayConfigModel = bayConfigModel;
	}

	public Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}

	public void setClusterBayNameListMap(Map<String, ArrayList<String>> bayNameListMap) {
		this.clusterBayNameListMap = bayNameListMap;
	}

	public Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}

	public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap) {
		this.clusterNameIdListMap = clusterIdNameListMap;
	}

	/*
	 * public Map<String, Map<String, String>> getClusterBayNameIdMap() {
	 * return clusterBayNameIdMap;
	 * }
	 * 
	 * public void setClusterBayNameIdMap(Map<String, Map<String, String>>
	 * clusterBayNameIdtMap) {
	 * this.clusterBayNameIdMap = clusterBayNameIdtMap;
	 * }
	 */

	/*
	 * @FXML
	 * public void btnSendDataToBayOnClick() {
	 * 
	 * ApplicationLauncher.logger.debug("btnSendDataToBayOnClick: Entry");
	 * sendCommDataTaskTimer = new Timer();
	 * sendCommDataTaskTimer.schedule(new SendDataToBayTask(),10);
	 * 
	 * }
	 * 
	 * class SendDataToBayTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * ref_btnSendDataToBay.setDisable(true);
	 * });
	 * 
	 * AsyncConveyorClientManager cluster1ClientManager = new
	 * AsyncConveyorClientManager();
	 * Platform.runLater(()->{
	 * ref_txtAreaResponseDisplay.clear();
	 * });
	 * 
	 * if(ref_chkBxWriteGreenLed.isSelected()) {
	 * String greenLedStatus = "Off";
	 * if(ref_chkBxGreenLedData.isSelected()) {
	 * greenLedStatus = "On";
	 * }
	 * 
	 * String deviceId = "1";
	 * String bayId = "1";
	 * boolean deviceResponded = false;
	 * if(ref_tbViewOutputPortData.getItems().size()>0) {
	 * String ipAddress = ref_txtClusterIpAddress.getText();
	 * String ipPort = ref_txtClusterPortNo.getText();
	 * ClusterServer clusterServer = new ClusterServer(ipAddress,ipPort );
	 * for(int i =0; i< ref_tbViewOutputPortData.getItems().size(); i++) {
	 * OutputPort outputPortDetails = ref_tbViewOutputPortData.getItems().get(i);
	 * if(outputPortDetails.isUpdateBay()) {
	 * String outputPortId = outputPortDetails.getPortId();
	 * String outputActive = "Off";
	 * if(outputPortDetails.isOutputActive()) {
	 * outputActive = "On";
	 * }
	 * 
	 * deviceResponded = setOutputDataToBay(clusterServer,
	 * deviceId,bayId,outputPortId, outputActive) ;
	 * }
	 * }
	 * }
	 * 
	 * if(ref_tbViewInputPortData.getItems().size()>0) {
	 * String ipAddress = ref_txtClusterIpAddress.getText();
	 * String ipPort = ref_txtClusterPortNo.getText();
	 * String stateDesc = "";
	 * ClusterServer clusterServer = new ClusterServer(ipAddress,ipPort );
	 * for(int i =0; i< ref_tbViewInputPortData.getItems().size(); i++) {
	 * stateDesc = "";
	 * InputPort inputPortDetails = ref_tbViewInputPortData.getItems().get(i);
	 * if(inputPortDetails.isReadBay()) {
	 * String inputPortId = inputPortDetails.getPortId();
	 * 
	 * 
	 * RestApiJsonBodyResponse clusterResponseData =
	 * getInputDataFromBay(clusterServer, deviceId,bayId,inputPortId) ;
	 * ApplicationLauncher.logger.debug("SendDataToBayTask: getStatuscode : "
	 * +clusterResponseData.getStatusCode());
	 * 
	 * if(clusterResponseData.getStatusCode().equals("200")){
	 * if(clusterResponseData.getJsonBodyResponse().get(inputPortId).equals("On")){
	 * ref_tbViewInputPortData.getItems().get(i).setInputActive(true);
	 * stateDesc = ref_tbViewInputPortData.getItems().get(i).getOnStateDesc();
	 * 
	 * }else
	 * if(clusterResponseData.getJsonBodyResponse().get(inputPortId).equals("Off")){
	 * ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
	 * stateDesc = ref_tbViewInputPortData.getItems().get(i).getOffStateDesc();
	 * }
	 * 
	 * 
	 * //JSONObject bodyRespinse = getJsonBodyResponse();
	 * }else {
	 * ref_tbViewInputPortData.getItems().get(i).setInputActive(false);
	 * }
	 * ref_tbViewInputPortData.getItems().get(i).setStateDescription(stateDesc);
	 * ref_tbViewInputPortData.refresh();
	 * }
	 * }
	 * }
	 * 
	 * 
	 * // }
	 * 
	 * Platform.runLater(()->{
	 * ref_btnSendDataToBay.setDisable(false);
	 * });
	 * }
	 * }
	 * 
	 * @FXML
	 * public void btnLoadOnClick(){
	 * 
	 * ApplicationLauncher.logger.debug("btnLoadOnClick: Entry");
	 * loadOnClickTimer = new Timer();
	 * loadOnClickTimer.schedule(new LoadOnClickTask(),10);
	 * 
	 * }
	 * 
	 * 
	 * class LoadOnClickTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * ref_tbViewOutputPortData.getItems().clear();
	 * ref_tbViewInputPortData.getItems().clear();
	 * String clusterName =
	 * (ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem().toString());
	 * String bayName =
	 * (ref_cmbBxBaySelection.getSelectionModel().getSelectedItem().toString());
	 * 
	 * String clusterId = getClusterNameIdListMap().get(clusterName);
	 * //ApplicationLauncher.logger.
	 * debug("loadDataFromConfig : getClusterBayNameIdMap().get(clusterName)-2 :"+
	 * getClusterBayNameIdMap().get(clusterName));
	 * //String bayId =
	 * "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);
	 * 
	 * ApplicationLauncher.logger.debug("LoadOnClickTask : clusterId :"+ clusterId);
	 * 
	 * 
	 * String clusterIpAddress = "";
	 * String clusterPortNo = "";
	 * 
	 * Optional<ClusterDetail> clusterOpt =
	 * getBayConfigModel().getTerminal().stream()
	 * .filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
	 * .flatMap(terminal -> terminal.getClusterDetails().stream())
	 * .filter(e2->e2.getClusterId().equals(clusterId))
	 * .findFirst();
	 * 
	 * if (clusterOpt.isPresent()) {
	 * ClusterDetail clusterDetail = clusterOpt.get();
	 * ref_txtClusterIpAddress.setText(clusterDetail.getClusterIpAddress());
	 * ref_txtClusterPortNo.setText(clusterDetail.getClusterPortId());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * //Optional<OutputPort> outputPortOpt =
	 * 
	 * Optional<Bay> bayOpt = getBayConfigModel().getTerminal().stream()
	 * .filter(e1->e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
	 * .flatMap(terminal -> terminal.getClusterDetails().stream())
	 * .filter(e2->e2.getClusterId().equals(clusterId))
	 * .flatMap(e3 -> e3.getBay().stream())
	 * .filter(e4->e4.getBayName().equals(bayName))
	 * .findFirst();
	 * 
	 * if (bayOpt.isPresent()) {
	 * Bay bayDetails = bayOpt.get();
	 * String bayId = bayDetails.getBayId();
	 * ApplicationLauncher.logger.debug("LoadOnClickTask : bayId :"+ bayId);
	 * 
	 * ArrayList<OutputPort> outputPortList = (ArrayList<OutputPort>)
	 * getBayConfigModel().getTerminal().stream()
	 * .flatMap(terminal -> terminal.getOutputPort().stream())
	 * .filter(p -> clusterId.equals(p.getClusterId()))
	 * .filter(p -> bayId.equals(p.getBayId()))
	 * .collect(Collectors.toList());
	 * 
	 * if (outputPortList.size()>0) {
	 * int serialNo = 1;
	 * for(OutputPort eachOutputPort : outputPortList) {
	 * eachOutputPort.setSerialNo(String.valueOf(serialNo));
	 * if(eachOutputPort.isOutputActive()) {
	 * eachOutputPort.setStateDescription(eachOutputPort.getOnStateDesc());
	 * //ApplicationLauncher.logger.
	 * debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-1 : " +
	 * rowData.getStateDescription());
	 * }else {
	 * eachOutputPort.setStateDescription(eachOutputPort.getOffStateDesc());
	 * //ApplicationLauncher.logger.
	 * debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-12: " +
	 * rowData.getStateDescription());
	 * 
	 * }
	 * //eachOutputPort.setStateDescription("DummyData");
	 * serialNo++;
	 * }
	 * ref_tbViewOutputPortData.getItems().addAll(outputPortList);
	 * 
	 * }
	 * 
	 * ArrayList<InputPort> inputPortList = (ArrayList<InputPort>)
	 * getBayConfigModel().getTerminal().stream()
	 * .flatMap(terminal -> terminal.getInputPort().stream())
	 * .filter(p -> clusterId.equals(p.getClusterId()))
	 * .filter(p -> bayId.equals(p.getBayId()))
	 * .collect(Collectors.toList());
	 * 
	 * if (inputPortList.size()>0) {
	 * int serialNo = 1;
	 * for(InputPort eachInputPort : inputPortList) {
	 * eachInputPort.setSerialNo(String.valueOf(serialNo));
	 * if(eachInputPort.isInputActive()) {
	 * eachInputPort.setStateDescription(eachInputPort.getOnStateDesc());
	 * //ApplicationLauncher.logger.
	 * debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-1 : " +
	 * rowData.getStateDescription());
	 * }else {
	 * eachInputPort.setStateDescription(eachInputPort.getOffStateDesc());
	 * //ApplicationLauncher.logger.
	 * debug("OutputPortActiveCheckBoxValueFactory : getStateDescription-12: " +
	 * rowData.getStateDescription());
	 * 
	 * }
	 * serialNo++;
	 * }
	 * ref_tbViewInputPortData.getItems().addAll(inputPortList);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 * });
	 * loadOnClickTimer.cancel();
	 * 
	 * 
	 * 
	 * 
	 * }
	 * }
	 */

	// B A Y T E S T - COMBO BOX ON CHANGE HANDLERS
	// ====================================================================================================

	// CLUSTER COMBO BOX CHANGE
	/*
	 * @FXML
	 * public void cmbBxClusterSelectionOnChange(){
	 * ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
	 * clusterSelectionOnChangeTimer = new Timer();
	 * clusterSelectionOnChangeTimer.schedule(new
	 * ClusterSelectionOnChangeTask(),10);
	 * }
	 * 
	 * class ClusterSelectionOnChangeTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(() -> {
	 * String selectedClusterName =
	 * (String)ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
	 * if(getClusterBayNameListMap().size()>0){
	 * ref_tbViewOutputPortData.getItems().clear();
	 * ref_tbViewInputPortData.getItems().clear();
	 * ref_cmbBxBaySelection.getItems().clear();
	 * ref_txtClusterIpAddress.setText("");
	 * ref_txtClusterPortNo.setText("");
	 * if(getClusterBayNameListMap().containsKey(selectedClusterName)){
	 * ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(
	 * selectedClusterName));
	 * }
	 * ref_cmbBxBaySelection.getSelectionModel().select(0);
	 * 
	 * }
	 * });
	 * clusterSelectionOnChangeTimer.cancel();
	 * }
	 * }
	 * 
	 * //BAY COMBO BOX CHANGE
	 * 
	 * @FXML
	 * public void cmbBxBaySelectionOnChange(){
	 * ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
	 * baySelectionOnChangeTimer = new Timer();
	 * baySelectionOnChangeTimer.schedule(new BaySelectionOnChangeTask(),10);
	 * }
	 * 
	 * 
	 * class BaySelectionOnChangeTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * 
	 * String selectedClusterName =
	 * (String)ref_cmbBxClusterSelection.getSelectionModel().getSelectedItem();
	 * if(getClusterBayNameListMap().size()>0){
	 * ref_tbViewOutputPortData.getItems().clear();
	 * ref_tbViewInputPortData.getItems().clear();
	 * //ref_txtClusterIpAddress.setText("");
	 * //ref_txtClusterPortNo.setText("");
	 * //if(getClusterBayNameListMap().containsKey(selectedClusterName)){
	 * // ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(
	 * selectedClusterName));
	 * //}
	 * //ref_cmbBxBaySelection.getSelectionModel().select(0);
	 * 
	 * }
	 * });
	 * baySelectionOnChangeTimer.cancel();
	 * 
	 * 
	 * 
	 * 
	 * }
	 * }
	 */
	// ===============================================================================================================================================

	// P O S I T I O N L O A D E D - COMBO BOX ON-CHANGE HANDLERS
	// ===================================================================================

	// CLUSTER COMBO BOX CHANGE
	@FXML
	public void cmbBxPosLoadedClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxPosLoadedClusterSelectionOnChange: Entry");
		posLoadedClusterSelectionOnChangeTimer = new Timer();
		posLoadedClusterSelectionOnChangeTimer.schedule(new posLoadedlusterSelectionOnChangeTask(), 10);
		ref_tvLoadedPosition.getItems().clear();
	}

	class posLoadedlusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxPosLoadedClusterSelection.getSelectionModel()
						.getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_cmbBxPosLoadedBaySelection.getItems().clear();
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxPosLoadedBaySelection.getItems()
								.addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxPosLoadedBaySelection.getSelectionModel().select(0);

				}
			});
			posLoadedClusterSelectionOnChangeTimer.cancel();
		}
	}

	// BAY COMBO BOX CHANGE
	@FXML
	public void cmbBxPosLoadedBaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxPosLoadedBaySelectionOnChange: Entry");
		posLoadedBaySelectionOnChangeTimer = new Timer();
		posLoadedBaySelectionOnChangeTimer.schedule(new PosLoadedBaySelectionOnChangeTask(), 10);
		ref_tvLoadedPosition.getItems().clear();
	}

	class PosLoadedBaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxPosLoadedClusterSelection.getSelectionModel()
						.getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_txtClusterIpAddress.setText("");
					// ref_txtClusterPortNo.setText("");
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
			});
			posLoadedBaySelectionOnChangeTimer.cancel();
		}
	}

	// DEVICE TYPE COMBO BOX CHANGE
	@FXML
	public void cmbBxPosLoadedDeviceTypeSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxPosLoadedDeviceTypeSelectionOnChange: Entry");
		PosLoadedDeviceTypeSelectionOnChangeTimer = new Timer();
		PosLoadedDeviceTypeSelectionOnChangeTimer.schedule(new PosLoadedDeviceTypeSelectionOnChangeTask(), 10);
		ref_tvLoadedPosition.getItems().clear();
	}

	class PosLoadedDeviceTypeSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxPosLoadedDeviceTypeSelection.getSelectionModel()
						.getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_txtClusterIpAddress.setText("");
					// ref_txtClusterPortNo.setText("");
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxBaySelection.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxBaySelection.getSelectionModel().select(0);

				}
			});
			PosLoadedDeviceTypeSelectionOnChangeTimer.cancel();
		}
	}

	// ===============================================================================================================================================

	// ============================================================================================================================================

	/*
	 * @FXML
	 * public void btnAllStartOnClick() {
	 * ApplicationLauncher.logger.info("btnFtStartOnClick : Invoked:");
	 * 
	 * btnAllStart.setStyle("-fx-background-color: #FF5733;");
	 * btnAllStart.setDisable(true);
	 * btnAllStop.setDisable(false);
	 * 
	 * ref_tvTestStatus.getItems().clear();
	 * 
	 * allData.clear();
	 * funtionalBayStartTaskTimer = new Timer();
	 * funtionalBayStartTaskTimer.schedule(new FunctionalTestBay2(),100);
	 * 
	 * 
	 * hvtBayStartTaskTimer = new Timer();
	 * hvtBayStartTaskTimer.schedule(new HighVoltageTestBay2(),100);
	 * 
	 * 
	 * insResStartTaskTimer = new Timer();
	 * insResStartTaskTimer.schedule(new InsulationResistanceTestBay2(),100);
	 * 
	 * 
	 * calibrationStartTaskTimer = new Timer();
	 * calibrationStartTaskTimer.schedule(new CalibrationBay2(),100);
	 * 
	 * 
	 * verificStartTaskTimer = new Timer();
	 * verificStartTaskTimer.schedule(new VerificationTestBay2(),100);
	 * 
	 * 
	 * sctNlt1StartTaskTimer = new Timer();
	 * sctNlt1StartTaskTimer.schedule(new STA_NoLoadTestBay1_2(),100);
	 * 
	 * 
	 * sctNlt2StartTaskTimer = new Timer();
	 * sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2_2(),100);
	 * 
	 * 
	 * commStartTaskTimer = new Timer();
	 * commStartTaskTimer.schedule(new CommunicationTestBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @FXML
	 * public void btnAllStopOnClick() {
	 * ApplicationLauncher.logger.info("btnAllStopOnClick : Invoked:");
	 * 
	 * FunctionalTestBay2.setStopProcessRequestedFtBay(true);
	 * HighVoltageTestBay2.abort_HVT_Bay = true;
	 * InsulationResistanceTestBay2.abort_IRT_Bay = true;
	 * CalibrationBay2.abort_Calib_Bay = true;
	 * VerificationTestBay2.abort_VerificTest_Bay = true;
	 * STA_NoLoadTestBay1_2.abort_SCT_NLT_Bay1 = true;
	 * STA_NoLoadTestBay2_2.abort_SCT_NLT_Bay2 = true;
	 * CommunicationTestBay2.abort_CommTest_Bay = true;
	 * 
	 * 
	 * btnAllStart.setStyle("-fx-background-color: #FF5733;");
	 * btnAllStart.setDisable(false);
	 * btnAllStop.setDisable(true);
	 * 
	 * funtionalBayStopTaskTimer = new Timer();
	 * funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(),100);
	 * 
	 * 
	 * hvtBayStopTaskTimer = new Timer();
	 * hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(),100);
	 * 
	 * 
	 * insResStopTaskTimer = new Timer();
	 * insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
	 * 
	 * 
	 * calibrationStopTaskTimer = new Timer();
	 * calibrationStopTaskTimer.schedule(new CalibrationBayStop(),100);
	 * 
	 * 
	 * verificStopTaskTimer = new Timer();
	 * verificStopTaskTimer.schedule(new VerificationTestBayStop(),100);
	 * 
	 * 
	 * sctNlt1StopTaskTimer = new Timer();
	 * sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(),100);
	 * 
	 * 
	 * sctNlt2StopTaskTimer = new Timer();
	 * sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(),100);
	 * 
	 * 
	 * commStopTaskTimer = new Timer();
	 * commStopTaskTimer.schedule(new CommunicationTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnAllResetOnClick() {
	 * ApplicationLauncher.logger.info("btnAllResetOnClick : Invoked:");
	 * 
	 * funtionalBayResetTaskTimer = new Timer();
	 * funtionalBayResetTaskTimer.schedule(new FunctionalTestBayReset(),100);
	 * 
	 * 
	 * hvtBayResetTaskTimer = new Timer();
	 * hvtBayResetTaskTimer.schedule(new HighVoltageTestBayReset(),100);
	 * 
	 * 
	 * insResResetTaskTimer = new Timer();
	 * insResResetTaskTimer.schedule(new InsulationResistanceTestBayReset(),100);
	 * 
	 * 
	 * calibrationResetTaskTimer = new Timer();
	 * calibrationResetTaskTimer.schedule(new CalibrationBayReset(),100);
	 * 
	 * 
	 * verificResetTaskTimer = new Timer();
	 * verificResetTaskTimer.schedule(new VerificationTestBayReset(),100);
	 * 
	 * 
	 * sctNlt1ResetTaskTimer = new Timer();
	 * sctNlt1ResetTaskTimer.schedule(new STA_NoLoadTestBay1Reset(),100);
	 * 
	 * 
	 * sctNlt2ResetTaskTimer = new Timer();
	 * sctNlt2ResetTaskTimer.schedule(new STA_NoLoadTestBay2Reset(),100);
	 * 
	 * 
	 * commResetTaskTimer = new Timer();
	 * commResetTaskTimer.schedule(new CommunicationTestBayReset(),100);
	 * 
	 * 
	 * }
	 */
	// ============================================================================================================================================

	/*
	 * @FXML
	 * public void btnFtStartOnClick(){
	 * 
	 * ApplicationLauncher.logger.info("btnFtStartOnClick : Invoked:");
	 * 
	 * btnFtStart.setStyle("-fx-background-color: #FF5733;");
	 * btnFtStart.setDisable(true);
	 * btnFtStop.setDisable(false);
	 * 
	 * ref_tvTestStatus.getItems().clear();
	 * 
	 * allData.clear();
	 * //String CurrentProjectName =
	 * ref_cmbBox_ChooseProject.getSelectionModel().getSelectedItem();
	 * //if(!CurrentProjectName.equals("Select")){
	 * funtionalBayStartTaskTimer = new Timer();
	 * //LoadProjectTaskTimer.schedule(new LoadProjectTask(),100);
	 * funtionalBayStartTaskTimer.schedule(new FunctionalTestBay2(),100);
	 * 
	 * 
	 * //QrScannerTaskTimer= new Timer();
	 * 
	 * //QrScannerTaskTimer.schedule(new QrScannerTaskTimer(),100);
	 * //}
	 * 
	 * hvtBayTaskTimer = new Timer();
	 * hvtBayTaskTimer.schedule(new HighVoltageTestBay(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnFtStopOnClick(){
	 * ApplicationLauncher.logger.info("btnFtStartOnClick : Invoked:");
	 * 
	 * FunctionalTestBay2.setStopProcessRequestedFtBay(true);
	 * 
	 * btnFtStart.setStyle("-fx-background-color: #FF5733;");
	 * //btnFtStart.setDisable(false);
	 * btnFtStop.setDisable(true);
	 * 
	 * funtionalBayStopTaskTimer = new Timer();
	 * funtionalBayStopTaskTimer.schedule(new FunctionalTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * public static void enableFtStartButton() {
	 * try {
	 * ref_btnFtStart.setDisable(false);
	 * }catch(Exception e) {
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("enableFtStartButton : Exception: " +
	 * e.getMessage());
	 * }
	 * }
	 * 
	 * @FXML
	 * public void btnFtResetOnClick(){
	 * funtionalBayResetTaskTimer = new Timer();
	 * funtionalBayResetTaskTimer.schedule(new FunctionalTestBayReset(), 100);
	 * 
	 * 
	 * }
	 * 
	 * //===========================================================================
	 * =================================================================
	 * 
	 * 
	 * @FXML
	 * public void btnHvtStartOnClick(){
	 * btnHvtStart.setStyle("-fx-background-color: #FF5733;");
	 * btnHvtStart.setDisable(true);
	 * btnHvtStop.setDisable(false);
	 * 
	 * hvtBayStartTaskTimer = new Timer();
	 * hvtBayStartTaskTimer.schedule(new HighVoltageTestBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnHvtStopOnClick(){
	 * HighVoltageTestBay2.abort_HVT_Bay = true;
	 * 
	 * btnHvtStart.setStyle("-fx-background-color: #FF5733;");
	 * btnHvtStart.setDisable(false);
	 * btnHvtStop.setDisable(true);
	 * 
	 * hvtBayStopTaskTimer = new Timer();
	 * hvtBayStopTaskTimer.schedule(new HighVoltageTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnHvtResetOnClick(){
	 * hvtBayResetTaskTimer = new Timer();
	 * hvtBayResetTaskTimer.schedule(new HighVoltageTestBayReset(), 100);
	 * 
	 * 
	 * }
	 * 
	 * //===========================================================================
	 * =================================================================
	 * 
	 * @FXML
	 * public void btnIrtStartOnClick(){
	 * btnIrtStart.setStyle("-fx-background-color: #FF5733;");
	 * btnIrtStart.setDisable(true);
	 * btnIrtStop.setDisable(false);
	 * 
	 * insResStartTaskTimer = new Timer();
	 * insResStartTaskTimer.schedule(new InsulationResistanceTestBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnIrtStopOnClick(){
	 * InsulationResistanceTestBay2.abort_IRT_Bay = true;
	 * 
	 * btnIrtStart.setStyle("-fx-background-color: #FF5733;");
	 * btnIrtStart.setDisable(false);
	 * btnIrtStop.setDisable(true);
	 * 
	 * insResStopTaskTimer = new Timer();
	 * insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnIrtResetOnClick(){
	 * insResResetTaskTimer = new Timer();
	 * insResResetTaskTimer.schedule(new InsulationResistanceTestBayReset(), 100);
	 * 
	 * 
	 * }
	 * //===========================================================================
	 * =================================================================
	 * 
	 * 
	 * 
	 * @FXML
	 * public void btnCalibStartOnClick(){
	 * btnCalibStart.setStyle("-fx-background-color: #FF5733;");
	 * btnCalibStart.setDisable(true);
	 * btnCalibStop.setDisable(false);
	 * 
	 * calibrationStartTaskTimer = new Timer();
	 * calibrationStartTaskTimer.schedule(new CalibrationBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnCalibStopOnClick(){
	 * CalibrationBay2.abort_Calib_Bay = true;
	 * 
	 * btnCalibStart.setStyle("-fx-background-color: #FF5733;");
	 * btnCalibStart.setDisable(false);
	 * btnCalibStop.setDisable(true);
	 * 
	 * calibrationStopTaskTimer = new Timer();
	 * calibrationStopTaskTimer.schedule(new CalibrationBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnCalibResetOnClick(){
	 * calibrationResetTaskTimer = new Timer();
	 * calibrationResetTaskTimer.schedule(new CalibrationBayReset(), 100);
	 * 
	 * 
	 * }
	 * //===========================================================================
	 * =================================================================
	 * 
	 * @FXML
	 * public void btnVerificTestStartOnClick(){
	 * verificStartTaskTimer = new Timer();
	 * verificStartTaskTimer.schedule(new VerificationTestBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnVerificTestStopOnClick(){
	 * VerificationTestBay2.abort_VerificTest_Bay = true;
	 * 
	 * verificStopTaskTimer = new Timer();
	 * verificStopTaskTimer.schedule(new VerificationTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnVerificTestResetOnClick(){
	 * verificResetTaskTimer = new Timer();
	 * verificResetTaskTimer.schedule(new VerificationTestBayReset(), 100);
	 * 
	 * 
	 * }
	 * //===========================================================================
	 * =================================================================
	 * 
	 * @FXML
	 * public void btnSctNlt1StartOnClick(){
	 * sctNlt1StartTaskTimer = new Timer();
	 * sctNlt1StartTaskTimer.schedule(new STA_NoLoadTestBay1_2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnSctNlt1StopOnClick(){
	 * STA_NoLoadTestBay1_2.abort_SCT_NLT_Bay1 = true;
	 * 
	 * sctNlt1StopTaskTimer = new Timer();
	 * sctNlt1StopTaskTimer.schedule(new STA_NoLoadTestBay1Stop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnSctNlt1ResetOnClick(){
	 * sctNlt1ResetTaskTimer = new Timer();
	 * sctNlt1ResetTaskTimer.schedule(new STA_NoLoadTestBay1Reset(), 100);
	 * 
	 * 
	 * }
	 * //===========================================================================
	 * =================================================================
	 * 
	 * @FXML
	 * public void btnSctNlt2StartOnClick(){
	 * sctNlt2StartTaskTimer = new Timer();
	 * sctNlt2StartTaskTimer.schedule(new STA_NoLoadTestBay2_2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnSctNlt2StopOnClick(){
	 * STA_NoLoadTestBay2_2.abort_SCT_NLT_Bay2 = true;
	 * 
	 * sctNlt2StopTaskTimer = new Timer();
	 * sctNlt2StopTaskTimer.schedule(new STA_NoLoadTestBay2Stop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnSctNlt2ResetOnClick(){
	 * sctNlt2ResetTaskTimer = new Timer();
	 * sctNlt2ResetTaskTimer.schedule(new STA_NoLoadTestBay2Reset(), 100);
	 * 
	 * 
	 * }
	 * //===========================================================================
	 * =================================================================
	 * 
	 * @FXML
	 * public void btnCommTestStartOnClick(){
	 * commStartTaskTimer = new Timer();
	 * commStartTaskTimer.schedule(new CommunicationTestBay2(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnCommTestStopOnClick(){
	 * CommunicationTestBay2.abort_CommTest_Bay = true;
	 * 
	 * commStopTaskTimer = new Timer();
	 * commStopTaskTimer.schedule(new CommunicationTestBayStop(),100);
	 * 
	 * 
	 * }
	 * 
	 * @FXML
	 * public void btnCommTestResetOnClick(){
	 * commResetTaskTimer = new Timer();
	 * commResetTaskTimer.schedule(new CommunicationTestBayReset(), 100);
	 * 
	 * 
	 * }
	 */
	// ============================================================================================================================================

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	/*
	 * //=== L O A D S A M P L E D A T A
	 * ======================================================
	 * 
	 * @FXML
	 * public void btnSampleDataOnClick(){
	 * ApplicationLauncher.logger.debug("btnSampleDataOnClick Invoked:");
	 * 
	 * TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();
	 * 
	 * Random random = new Random();
	 * 
	 * for (int j = 0; j < 15; j++) {
	 * int i = random.nextInt(7);
	 * testIntefaceStatus = new TestInterfaceStatus(
	 * "" + i + "BAY",
	 * "" + i + "Seq",
	 * "" + i + "DEVICE",
	 * "P" + i,
	 * "" + i,
	 * "-",
	 * "(" + i + ")",
	 * ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
	 * "Waiting",
	 * ConstantConveyor.COMM_EXECUTION_STATUS_INP
	 * );
	 * allData.add(testIntefaceStatus) ;
	 * int newRecordSerialNo =
	 * ConveyorDebugController.addToTestStatusGui(testIntefaceStatus);
	 * testIntefaceStatus.setSerialNo(String.valueOf(newRecordSerialNo));
	 * }
	 * }
	 */

	/*
	 * //=== F I L T E R B U T T O N - O N C L I C K
	 * ======================================================
	 * 
	 * @FXML
	 * public void btnFilterOnClick() {
	 * ApplicationLauncher.logger.debug("btnFilterOnClick Invoked:");
	 * 
	 * Platform.runLater(()->{
	 * 
	 * ref_btnFilter.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * });
	 * btnFilterTaskTimer = new Timer();
	 * btnFilterTaskTimer.schedule(new FilterOnClickTimerTask(),100);
	 * 
	 * 
	 * }
	 * 
	 * class FilterOnClickTimerTask extends TimerTask {
	 * 
	 * @Override
	 * public void run() {
	 * Platform.runLater(()->{
	 * Object selectedValue = ref_cmbBxFilterPosition.getValue();
	 * ApplicationLauncher.logger.debug("FilterOnClickTimerTask Position Number : "
	 * + selectedValue);
	 * 
	 * List<TestInterfaceStatus> filteredDataList;
	 * if ("All".equals(selectedValue)) {
	 * filteredDataList = new ArrayList<>(allData);
	 * } else if (selectedValue instanceof Integer) {
	 * int filterPosition = (Integer) selectedValue;
	 * filteredDataList = allData.stream()
	 * .filter(item -> {
	 * try {
	 * String positionNo = item.getPositionNo();
	 * return positionNo == null ||positionNo == "-" || positionNo == "0"
	 * ||positionNo.isEmpty() ||
	 * Integer.parseInt(positionNo) == filterPosition;
	 * } catch (NumberFormatException | NullPointerException e) {
	 * return false;
	 * }
	 * })
	 * .collect(Collectors.toList());
	 * } else {
	 * // Handle unexpected cases
	 * filteredDataList = new ArrayList<>();
	 * }
	 * 
	 * ref_tvTestStatus.getItems().clear();
	 * 
	 * // Print the filtered list
	 * filteredDataList.forEach(item ->
	 * //ApplicationLauncher.logger.error("Filtered List Item: " + item)
	 * ref_tvTestStatus.getItems().add(item)
	 * );
	 * 
	 * filteredDataList.clear();
	 * 
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ref_btnFilter.setDisable(false);
	 * });
	 * }
	 * }
	 */
	// ============================================================================================================

	@FXML
	public void btnRefreshOnClick() {

		/*
		 * ApplicationLauncher.logger.debug("btnRefreshOnClick Invoked:");
		 * 
		 * ResultSummary newResultRecord = new ResultSummary();
		 * 
		 * newResultRecord.setCustomerId("2");
		 * newResultRecord.setCustomerName("L&T");
		 * newResultRecord.setSiteId("1");
		 * newResultRecord.setSiteName("Mysore");
		 * 
		 * //MySqlServiceManager.getResultSummaryService().saveToDb(newResultRecord);
		 * 
		 * List<ResultSummary> resultSummaryList=
		 * MySqlServiceManager.getResultSummaryService().findAll();//
		 * 
		 * resultSummaryList.stream().forEach(e->{
		 * ApplicationLauncher.logger.debug("Each Customer: " + e.getCustomerId() +
		 * " : name : " +e.getCustomerName());
		 * });
		 * 
		 * 
		 * List<ResultSummary> resultSummaryWithCustomerId=
		 * MySqlServiceManager.getResultSummaryService().findByCustomerId("2");//
		 * 
		 * resultSummaryWithCustomerId.stream().forEach(e->{
		 * ApplicationLauncher.logger.debug("Each Customer with id: " +
		 * e.getCustomerId() + " : name : " +e.getCustomerName());
		 * });
		 */

		Platform.runLater(() -> {

			ref_btnRefresh.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
		});
		btnRefreshTaskTimer = new Timer();
		btnRefreshTaskTimer.schedule(new RefreshOnClickTimerTask(), 100);

	}

	class RefreshOnClickTimerTask extends TimerTask {

		@Override
		public void run() {
			Platform.runLater(() -> {

				// ref_btnRefresh.setDisable(true);

				String clusterName = (String) ref_cmbBxPosLoadedClusterSelection.getSelectionModel().getSelectedItem();
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask Cluster Name : " + clusterName);

				String selectedClusterId = getClusterNameIdListMap().get(clusterName);
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask Cluster Id : " + selectedClusterId);

				String bayName = (String) ref_cmbBxPosLoadedBaySelection.getSelectionModel().getSelectedItem();
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask Bay Name : " + bayName);

				String selectedBayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask Bay Id : " + selectedBayId);

				ApplicationLauncher.logger
						.debug("RefreshOnClickTimerTask getClusterBayNameIdMap(): " + getClusterBayNameIdMap());
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask selectedClusterId: " + selectedClusterId);
				ApplicationLauncher.logger.debug("RefreshOnClickTimerTask selectedBayId: " + selectedBayId);

				String selectedTestType = (String) cmbBxPosLoadedDeviceTypeSelection.getSelectionModel()
						.getSelectedItem();
				ref_tvLoadedPosition.getItems().clear();

				// Q R S C A N N E R P O S I T I O N S
				// ==========================================

				PositionLoadedModel positionLoadedModel = new PositionLoadedModel();
				int serialNumber = 1;
				if (selectedTestType.equals(ConstantConveyor.DEVICE_TYPE_QR_SCANNER)) {

					ArrayList<QrScanner> qrScannerList = (ArrayList<QrScanner>) getBayConfigModel().getTerminal()
							.stream()
							.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
							.flatMap(terminal -> terminal.getQrScanner().stream())
							.filter(e -> e.getClusterId().equals(selectedClusterId))
							.filter(e1 -> e1.getBayId().equals(selectedBayId))
							.collect(Collectors.toList());

					if (qrScannerList.size() > 0) {
						// ApplicationLauncher.logger.debug("RefreshOnClickTimerTask qrScannerList: QR:
						// " + qrScannerList);
						for (int i = 0; i < qrScannerList.size(); i++) {
							positionLoadedModel = new PositionLoadedModel();
							ApplicationLauncher.logger.debug("RefreshOnClickTimerTask qrScannerList: QR: getPositionId:"
									+ qrScannerList.get(i).getPositionId());
							positionLoadedModel.setSerialNo(String.valueOf(serialNumber++));
							positionLoadedModel.setPositionNo(qrScannerList.get(i).getPositionId());
							positionLoadedModel.setcName(qrScannerList.get(i).getPortName());
							ref_tvLoadedPosition.getItems().add(positionLoadedModel);
						}
					} else {
						ApplicationLauncher.logger.debug("RefreshOnClickTimerTask No record found for QR Scanner");
						PositionLoadedModel dataSet1 = new PositionLoadedModel("", "", "NO RECORD FOUND");
						ref_tvLoadedPosition.getItems().add(dataSet1);
					}
				}

				// D U T _ D E V I C E P O S I T I O N S
				// ==========================================

				if (selectedTestType.equals(ConstantConveyor.DEVICE_TYPE_DUT)) {

					ArrayList<DutDevice> dutDeviceList = (ArrayList<DutDevice>) getBayConfigModel().getTerminal()
							.stream()
							.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
							.flatMap(terminal -> terminal.getDutDevice().stream())
							.filter(e -> e.getClusterId().equals(selectedClusterId))
							.filter(e1 -> e1.getBayId().equals(selectedBayId))
							.collect(Collectors.toList());

					if (dutDeviceList.size() > 0) {
						ApplicationLauncher.logger.debug("RefreshOnClickTimerTask dutDeviceList: EM: " + dutDeviceList);
						for (int i = 0; i < dutDeviceList.size(); i++) {
							positionLoadedModel = new PositionLoadedModel();
							ApplicationLauncher.logger.debug("RefreshOnClickTimerTask dutDeviceList: EM: getPositionId:"
									+ dutDeviceList.get(i).getPositionId());
							positionLoadedModel.setSerialNo(String.valueOf(serialNumber++));
							positionLoadedModel.setPositionNo(dutDeviceList.get(i).getPositionId());
							positionLoadedModel.setcName(dutDeviceList.get(i).getPortName());
							ref_tvLoadedPosition.getItems().add(positionLoadedModel);
						}
					} else {
						ApplicationLauncher.logger.debug("RefreshOnClickTimerTask No record found for Dut Device");
						PositionLoadedModel dataSet1 = new PositionLoadedModel("", "", "NO RECORD FOUND");
						ref_tvLoadedPosition.getItems().add(dataSet1);
					}
				}

				// O H M - M E T E R P O S I T I O N S
				// ==========================================

				if (selectedTestType.equals(ConstantConveyor.DEVICE_TYPE_OHM_METER)) {

					ArrayList<MegaOhmMeter> megaOhmMeterList = (ArrayList<MegaOhmMeter>) getBayConfigModel()
							.getTerminal().stream()
							.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
							.flatMap(terminal -> terminal.getMegaOhmMeter().stream())
							.filter(e -> e.getClusterId().equals(selectedClusterId))
							.filter(e1 -> e1.getBayId().equals(selectedBayId))
							.collect(Collectors.toList());

					if (megaOhmMeterList.size() > 0) {
						ApplicationLauncher.logger
								.debug("RefreshOnClickTimerTask megaOhmMeterList: OM: " + megaOhmMeterList);
						for (int i = 0; i < megaOhmMeterList.size(); i++) {
							positionLoadedModel = new PositionLoadedModel();
							ApplicationLauncher.logger
									.debug("RefreshOnClickTimerTask megaOhmMeterList: OM: getPositionId:"
											+ megaOhmMeterList.get(i).getPositionId());
							positionLoadedModel.setSerialNo(String.valueOf(serialNumber++));
							positionLoadedModel.setPositionNo(megaOhmMeterList.get(i).getPositionId());
							positionLoadedModel.setcName(megaOhmMeterList.get(i).getPortName());
							ref_tvLoadedPosition.getItems().add(positionLoadedModel);
						}
					} else {
						ApplicationLauncher.logger.debug("RefreshOnClickTimerTask No record found for Ohm Meter");
						PositionLoadedModel dataSet1 = new PositionLoadedModel("", "", "NO RECORD FOUND");
						ref_tvLoadedPosition.getItems().add(dataSet1);
					}
				}

				// V O L T - M E T E R P O S I T I O N S
				// ==========================================

				/*
				 * if(selectedTestType.equals(ConstantConveyor.DEVICE_TYPE_OHM_METER)){
				 * 
				 * ArrayList<//MegaOhmMeter> voltMeterList =
				 * (ArrayList<MegaOhmMeter>) getBayConfigModel().getTerminal().stream()
				 * .filter(e1->e1.getTerminalId().equals(ConstantConfig.MY_TERMINAL_ID))
				 * .flatMap(terminal -> terminal.//getMegaOhmMeter().stream())
				 * .filter(e->e.getClusterId().equals(selectedClusterId))
				 * .filter(e1->e1.getBayId().equals(selectedBayId))
				 * .collect(Collectors.toList());
				 * 
				 * if(voltMeterList.size()>0){
				 * ApplicationLauncher.logger.
				 * debug("RefreshOnClickTimerTask voltMeterList: VM: " + voltMeterList);
				 * for(int i=0; i< voltMeterList.size();i++){
				 * ApplicationLauncher.logger.
				 * debug("RefreshOnClickTimerTask voltMeterList: VM: getPositionId:" +
				 * voltMeterList.get(i).getPositionId());
				 * 
				 * }
				 * }else{
				 * ApplicationLauncher.logger.
				 * debug("RefreshOnClickTimerTask No record found for Volt Meter");
				 * }
				 * }
				 */

				ref_tvLoadedPosition.refresh();

				/*
				 * Sleep(5000);
				 * for ( int i = 0; i < ref_tvLoadedPosition.getItems().size(); i ++){
				 * ApplicationLauncher.logger.debug("RefreshOnClickTimerTask: " +
				 * ref_tvLoadedPosition.getItems().get(i).getcName() + " -> status :" +
				 * ref_tvLoadedPosition.getItems().get(i).isPositionSelected());
				 * 
				 * }
				 */
				WindowManager.setCursor(Cursor.DEFAULT);
				ref_btnRefresh.setDisable(false);
			});

		}
	}

	public static TextField getTextFieldByKey(String bayKey) {
		switch (bayKey) {
			case "CALB":
				return ref_txtCalibBayPallet;
			case "COMB":
				return ref_txtCommBayPallet;
			case "FTB":
				return ref_txtFTBayPallet;
			case "HVB":
				return ref_txtHVBayPallet;
			case "IRB":
				return ref_txtIRBayPallet;
			case "LOADB":
				return ref_txtLoadingBayPallet;
			case "RJTB":
				return ref_txtRejectionBayPallet;
			case "SCTNLT1P1":
				return ref_txtSCTNLTBay1Pallet1;
			case "SCTNLT1P2":
				return ref_txtSCTNLTBay1Pallet2;
			case "SCTNLT1P3":
				return ref_txtSCTNLTBay1Pallet3;
			case "SCTNLT1P4":
				return ref_txtSCTNLTBay1Pallet4;
			case "SCTNLT2P1":
				return ref_txtSCTNLTBay2Pallet1;
			case "SCTNLT2P2":
				return ref_txtSCTNLTBay2Pallet2;
			case "SCTNLT2P3":
				return ref_txtSCTNLTBay2Pallet3;
			case "SCTNLT2P4":
				return ref_txtSCTNLTBay2Pallet4;
			case "UNLDB":
				return ref_txtUnloadingBayPallet;
			case "Verific1":
				return ref_txtVerificBayPallet1;
			case "Verific2":
				return ref_txtVerificBayPallet2;
			case "Verific3":
				return ref_txtVerificBayPallet3;
			case "Verific4":
				return ref_txtVerificBayPallet4;
			case "Waiting1":
				return ref_txtWaitingBayPallet1;
			case "Waiting2":
				return ref_txtWaitingBayPallet2;
			case "Waiting3":
				return ref_txtWaitingBayPallet3;
			case "Waiting4":
				return ref_txtWaitingBayPallet4;
			default:
				return null;
		}
	}

	public static List<TextField> getMultiPalletTextFields(String bayKey) {
		switch (bayKey) {
			case "VERIFICB":
				return Arrays.asList(ref_txtVerificBayPallet1, ref_txtVerificBayPallet2, ref_txtVerificBayPallet3,
						ref_txtVerificBayPallet4);
			case "STNLD1B":
				return Arrays.asList(ref_txtSCTNLTBay1Pallet1, ref_txtSCTNLTBay1Pallet2, ref_txtSCTNLTBay1Pallet3,
						ref_txtSCTNLTBay1Pallet4);
			case "STNLD2B":
				return Arrays.asList(ref_txtSCTNLTBay2Pallet1, ref_txtSCTNLTBay2Pallet2, ref_txtSCTNLTBay2Pallet3,
						ref_txtSCTNLTBay2Pallet4);
			default:
				return Collections.emptyList();
		}
	}

	public Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}

	public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		this.clusterBayNameIdMap = clusterBayNameIdMap;
	}

	public static TextField getRef_txtCalibBayPallet() {
		return ref_txtCalibBayPallet;
	}

	public static void setRef_txtCalibBayPallet(TextField ref_txtCalibBayPallet) {
		ConveyorDebugController.ref_txtCalibBayPallet = ref_txtCalibBayPallet;
	}

	public static TextField getRef_txtCommBayPallet() {
		return ref_txtCommBayPallet;
	}

	public static void setRef_txtCommBayPallet(TextField ref_txtCommBayPallet) {
		ConveyorDebugController.ref_txtCommBayPallet = ref_txtCommBayPallet;
	}

	public static TextField getRef_txtFTBayPallet() {
		return ref_txtFTBayPallet;
	}

	public static void setRef_txtFTBayPallet(TextField ref_txtFTBayPallet) {
		ConveyorDebugController.ref_txtFTBayPallet = ref_txtFTBayPallet;
	}

	public static TextField getRef_txtHVBayPallet() {
		return ref_txtHVBayPallet;
	}

	public static void setRef_txtHVBayPallet(TextField ref_txtHVBayPallet) {
		ConveyorDebugController.ref_txtHVBayPallet = ref_txtHVBayPallet;
	}

	public static TextField getRef_txtIRBayPallet() {
		return ref_txtIRBayPallet;
	}

	public static void setRef_txtIRBayPallet(TextField ref_txtIRBayPallet) {
		ConveyorDebugController.ref_txtIRBayPallet = ref_txtIRBayPallet;
	}

	public static TextField getRef_txtLoadingBayPallet() {
		return ref_txtLoadingBayPallet;
	}

	public static void setRef_txtLoadingBayPallet(TextField ref_txtLoadingBayPallet) {
		ConveyorDebugController.ref_txtLoadingBayPallet = ref_txtLoadingBayPallet;
	}

	public static TextField getRef_txtRejectionBayPallet() {
		return ref_txtRejectionBayPallet;
	}

	public static void setRef_txtRejectionBayPallet(TextField ref_txtRejectionBayPallet) {
		ConveyorDebugController.ref_txtRejectionBayPallet = ref_txtRejectionBayPallet;
	}

	public static TextField getRef_txtSCTNLTBay1Pallet1() {
		return ref_txtSCTNLTBay1Pallet1;
	}

	public static void setRef_txtSCTNLTBay1Pallet1(TextField ref_txtSCTNLTBay1Pallet1) {
		ConveyorDebugController.ref_txtSCTNLTBay1Pallet1 = ref_txtSCTNLTBay1Pallet1;
	}

	public static TextField getRef_txtSCTNLTBay1Pallet2() {
		return ref_txtSCTNLTBay1Pallet2;
	}

	public static void setRef_txtSCTNLTBay1Pallet2(TextField ref_txtSCTNLTBay1Pallet2) {
		ConveyorDebugController.ref_txtSCTNLTBay1Pallet2 = ref_txtSCTNLTBay1Pallet2;
	}

	public static TextField getRef_txtSCTNLTBay1Pallet3() {
		return ref_txtSCTNLTBay1Pallet3;
	}

	public static void setRef_txtSCTNLTBay1Pallet3(TextField ref_txtSCTNLTBay1Pallet3) {
		ConveyorDebugController.ref_txtSCTNLTBay1Pallet3 = ref_txtSCTNLTBay1Pallet3;
	}

	public static TextField getRef_txtSCTNLTBay1Pallet4() {
		return ref_txtSCTNLTBay1Pallet4;
	}

	public static void setRef_txtSCTNLTBay1Pallet4(TextField ref_txtSCTNLTBay1Pallet4) {
		ConveyorDebugController.ref_txtSCTNLTBay1Pallet4 = ref_txtSCTNLTBay1Pallet4;
	}

	public static TextField getRef_txtSCTNLTBay2Pallet1() {
		return ref_txtSCTNLTBay2Pallet1;
	}

	public static void setRef_txtSCTNLTBay2Pallet1(TextField ref_txtSCTNLTBay2Pallet1) {
		ConveyorDebugController.ref_txtSCTNLTBay2Pallet1 = ref_txtSCTNLTBay2Pallet1;
	}

	public static TextField getRef_txtSCTNLTBay2Pallet2() {
		return ref_txtSCTNLTBay2Pallet2;
	}

	public static void setRef_txtSCTNLTBay2Pallet2(TextField ref_txtSCTNLTBay2Pallet2) {
		ConveyorDebugController.ref_txtSCTNLTBay2Pallet2 = ref_txtSCTNLTBay2Pallet2;
	}

	public static TextField getRef_txtSCTNLTBay2Pallet3() {
		return ref_txtSCTNLTBay2Pallet3;
	}

	public static void setRef_txtSCTNLTBay2Pallet3(TextField ref_txtSCTNLTBay2Pallet3) {
		ConveyorDebugController.ref_txtSCTNLTBay2Pallet3 = ref_txtSCTNLTBay2Pallet3;
	}

	public static TextField getRef_txtSCTNLTBay2Pallet4() {
		return ref_txtSCTNLTBay2Pallet4;
	}

	public static void setRef_txtSCTNLTBay2Pallet4(TextField ref_txtSCTNLTBay2Pallet4) {
		ConveyorDebugController.ref_txtSCTNLTBay2Pallet4 = ref_txtSCTNLTBay2Pallet4;
	}

	public static TextField getRef_txtUnloadingBayPallet() {
		return ref_txtUnloadingBayPallet;
	}

	public static void setRef_txtUnloadingBayPallet(TextField ref_txtUnloadingBayPallet) {
		ConveyorDebugController.ref_txtUnloadingBayPallet = ref_txtUnloadingBayPallet;
	}

	public static TextField getRef_txtVerificBayPallet1() {
		return ref_txtVerificBayPallet1;
	}

	public static void setRef_txtVerificBayPallet1(TextField ref_txtVerificBayPallet1) {
		ConveyorDebugController.ref_txtVerificBayPallet1 = ref_txtVerificBayPallet1;
	}

	public static TextField getRef_txtVerificBayPallet2() {
		return ref_txtVerificBayPallet2;
	}

	public static void setRef_txtVerificBayPallet2(TextField ref_txtVerificBayPallet2) {
		ConveyorDebugController.ref_txtVerificBayPallet2 = ref_txtVerificBayPallet2;
	}

	public static TextField getRef_txtVerificBayPallet3() {
		return ref_txtVerificBayPallet3;
	}

	public static void setRef_txtVerificBayPallet3(TextField ref_txtVerificBayPallet3) {
		ConveyorDebugController.ref_txtVerificBayPallet3 = ref_txtVerificBayPallet3;
	}

	public static TextField getRef_txtVerificBayPallet4() {
		return ref_txtVerificBayPallet4;
	}

	public static void setRef_txtVerificBayPallet4(TextField ref_txtVerificBayPallet4) {
		ConveyorDebugController.ref_txtVerificBayPallet4 = ref_txtVerificBayPallet4;
	}

	public static TextField getRef_txtWaitingBayPallet1() {
		return ref_txtWaitingBayPallet1;
	}

	public static void setRef_txtWaitingBayPallet1(TextField ref_txtWaitingBayPallet1) {
		ConveyorDebugController.ref_txtWaitingBayPallet1 = ref_txtWaitingBayPallet1;
	}

	public static TextField getRef_txtWaitingBayPallet2() {
		return ref_txtWaitingBayPallet2;
	}

	public static void setRef_txtWaitingBayPallet2(TextField ref_txtWaitingBayPallet2) {
		ConveyorDebugController.ref_txtWaitingBayPallet2 = ref_txtWaitingBayPallet2;
	}

	public static TextField getRef_txtWaitingBayPallet3() {
		return ref_txtWaitingBayPallet3;
	}

	public static void setRef_txtWaitingBayPallet3(TextField ref_txtWaitingBayPallet3) {
		ConveyorDebugController.ref_txtWaitingBayPallet3 = ref_txtWaitingBayPallet3;
	}

	public static TextField getRef_txtWaitingBayPallet4() {
		return ref_txtWaitingBayPallet4;
	}

	public static void setRef_txtWaitingBayPallet4(TextField ref_txtWaitingBayPallet4) {
		ConveyorDebugController.ref_txtWaitingBayPallet4 = ref_txtWaitingBayPallet4;
	}

	/*
	 * public static AtomicInteger getSerialNoTestStatusAtomic() {
	 * return serialNoTestStatusAtomic;
	 * }
	 * 
	 * public void setSerialNoTestStatusAtomic(AtomicInteger
	 * serialNoTestStatusAtomic) {
	 * this.serialNoTestStatusAtomic = serialNoTestStatusAtomic;
	 * }
	 */

	// ================ STATE PLAANER
	// =================================================================================================//

	/*
	 * @FXML public Button AddRow ;
	 * 
	 * @FXML public Button DeleteRow ;
	 * 
	 * @FXML public Button ButtonSaveStateFlow ;
	 */

	/*
	 * @FXML
	 * public void ButtonAddRowOnClick() {
	 * // Add a new row to the stateFlowRows list
	 * stateFlowRows.add(new StateFlowRow("P" + (stateFlowRows.size() + 1),
	 * "Select State",
	 * "No State Selected",
	 * "Select State",
	 * "No State Selected",
	 * " ",
	 * " "));
	 * 
	 * // Refresh the table view to display the new row
	 * tableStatePlanner.refresh();
	 * }
	 */

	/*
	 * @FXML
	 * public void ButtonDeleteRowOnClick() {
	 * // Get the selected row
	 * StateFlowRow selectedRow =
	 * tableStatePlanner.getSelectionModel().getSelectedItem();
	 * 
	 * // Check if a row is selected
	 * if (selectedRow != null) {
	 * // Remove the selected row from the list
	 * stateFlowRows.remove(selectedRow);
	 * 
	 * // Refresh the table view to reflect the deletion
	 * tableStatePlanner.refresh();
	 * }
	 * }
	 */

	/*
	 * @FXML
	 * public void ButtonSaveStateFlowOnClick() {
	 * // Call the validation function
	 * if (!validateStateFlowRows()) {
	 * // If validation fails, stop further execution
	 * return;
	 * }
	 * 
	 * setTableStatePlannerFtBay_UI(tableStatePlanner);
	 * 
	 * // Add save logic here
	 * WindowManager.InformUser("State Plan ", "Successfully Saved",
	 * AlertType.INFORMATION);
	 * 
	 * }
	 */

	// Validation function
	/*
	 * private boolean validateStateFlowRows() {
	 * // Get all rows from the TableView
	 * ObservableList<StateFlowRow> rows = tableStatePlanner.getItems();
	 * 
	 * // Iterate through the rows to validate
	 * for (int i = 0; i < rows.size(); i++) {
	 * StateFlowRow row = rows.get(i);
	 * 
	 * // Check if the state is "Select State"
	 * if ("Select State".equals(row.getState())) {
	 * // Prompt the user with the row number
	 * WindowManager.InformUser("State not selected in row " + (i + 1),
	 * "Kindly select a state", AlertType.INFORMATION);
	 * 
	 * return false; // Validation failed
	 * }
	 * }
	 * return true; // Validation successful
	 * }
	 */

	/*
	 * @FXML
	 * public void ButtonDeleteRowOnClick() {
	 * // Check if the table has rows to delete
	 * if (!stateFlowRows.isEmpty()) {
	 * // Remove the last row (or you can modify this to remove a specific row based
	 * on some condition)
	 * stateFlowRows.remove(stateFlowRows.size() - 1);
	 * 
	 * // Refresh the table view to reflect the deletion
	 * tableStatePlanner.refresh();
	 * }
	 * }
	 */

	// ======================================================================================================
	/*
	 * @FXML
	 * private void setupTable() {
	 * // Set the cell factory for the "State" column to use a ComboBox
	 * columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * 
	 * // Listen for changes in the "State" column and update the "StateErrorCode"
	 * column
	 * columnState.setOnEditCommit(event -> {
	 * // Get the selected row
	 * StateFlowRow row = event.getRowValue();
	 * 
	 * // Get the new state name
	 * String newState = event.getNewValue();
	 * 
	 * // Update the state in the row
	 * row.setState(newState);
	 * 
	 * // Map the state name to its corresponding state code and update it in the
	 * row
	 * String stateCode = stateCodeMap.getOrDefault(newState, "UNKNOWN_CODE");
	 * row.setStateErrorCode(stateCode);
	 * 
	 * // Refresh the table view to reflect the updated data
	 * tableStatePlanner.refresh();
	 * });
	 * 
	 * // Set up other columns (e.g., "Success") as needed
	 * columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * }
	 */

	/*
	 * @FXML
	 * private void setupTable() {
	 * //=========================================================
	 * // Set the cell factory for the "State" column to use a ComboBox
	 * columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * 
	 * // Listen for changes in the "State" column and update the "StateErrorCode"
	 * column
	 * columnState.setOnEditCommit(event -> {
	 * // Get the selected row
	 * StateFlowRow row = event.getRowValue();
	 * 
	 * // Get the new state name
	 * String newState = event.getNewValue();
	 * 
	 * // Update the state in the row
	 * row.setState(newState);
	 * 
	 * // Map the state name to its corresponding state code and update it in the
	 * row
	 * String stateCode = stateCodeMap.getOrDefault(newState, "UNKNOWN_CODE");
	 * row.setStateErrorCode(stateCode);
	 * 
	 * // Refresh the table view to reflect the updated data
	 * tableStatePlanner.refresh();
	 * });
	 * //=========================================================
	 * 
	 * // Set the cell factory for the "Success" column to use a ComboBox
	 * columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * 
	 * // Listen for changes in the "Success" column and update the
	 * "SuccessErrorCode" column
	 * columnSuccess.setOnEditCommit(event -> {
	 * // Get the selected row
	 * StateFlowRow row = event.getRowValue();
	 * 
	 * // Get the new success state name
	 * String newSuccessState = event.getNewValue();
	 * 
	 * // Update the success state in the row
	 * row.setSuccess(newSuccessState);
	 * 
	 * // Map the success state name to its corresponding success state code and
	 * update it in the row
	 * String successStateCode = stateCodeMap.getOrDefault(newSuccessState,
	 * "UNKNOWN_CODE");
	 * row.setSuccessErrorCode(successStateCode);
	 * 
	 * // Refresh the table view to reflect the updated data
	 * tableStatePlanner.refresh();
	 * });
	 * //=========================================================
	 * 
	 * 
	 * // Set the cell factory for the "Success" column to use a ComboBox
	 * columnFailed.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
	 * 
	 * // Listen for changes in the "Success" column and update the
	 * "SuccessErrorCode" column
	 * columnFailed.setOnEditCommit(event -> {
	 * // Get the selected row
	 * StateFlowRow row = event.getRowValue();
	 * 
	 * // Get the new success state name
	 * String newSuccessState = event.getNewValue();
	 * 
	 * // Update the success state in the row
	 * row.setSuccess(newSuccessState);
	 * 
	 * // Map the success state name to its corresponding success state code and
	 * update it in the row
	 * String successStateCode = stateCodeMap.getOrDefault(newSuccessState,
	 * "UNKNOWN_CODE");
	 * row.setSuccessErrorCode(successStateCode);
	 * 
	 * // Refresh the table view to reflect the updated data
	 * tableStatePlanner.refresh();
	 * });
	 * 
	 * 
	 * // You can set up other columns as needed, following a similar pattern
	 * }
	 */

	// ==============================================================================================

	/*
	 * @FXML
	 * public void saveStateFlow() {
	 * for (StateFlowRow row : stateFlowRows) {
	 * System.out.println("Path: " + row.getPath());
	 * System.out.println("State Error Code: " + row.getStateErrorCode());
	 * System.out.println("Success: " + row.getSuccess());
	 * System.out.println("Success Error Code: " + row.getSuccessErrorCode());
	 * System.out.println("Failed: " + row.getFailed());
	 * System.out.println("Failed Error Code: " + row.getFailedErrorCode());
	 * }
	 * }
	 */
	// ======================================================
	/*
	 * public static class StateFlowRow {
	 * private final SimpleStringProperty path;
	 * private final SimpleStringProperty state;
	 * private final SimpleStringProperty stateCode;
	 * private final SimpleStringProperty success;
	 * private final SimpleStringProperty successStateCode;
	 * private final SimpleStringProperty fail;
	 * private final SimpleStringProperty failStateCode;
	 * 
	 * // State code map (static, shared across instances)
	 * private static final Map<String, String> stateCodeMap = new HashMap<>();
	 * 
	 * static {
	 * stateCodeMap.put("Check for Pallet", "STATE_CODE_001");
	 * stateCodeMap.put("Check Stopper Before Bay Status", "STATE_CODE_002");
	 * stateCodeMap.put("Open Stopper Before Bay", "STATE_CODE_003");
	 * stateCodeMap.put("Close Stopper Before Bay", "STATE_CODE_004");
	 * stateCodeMap.put("Close FingerTip Latch", "STATE_CODE_005");
	 * stateCodeMap.put("Ensure FingerTip Latch Closed", "STATE_CODE_006");
	 * stateCodeMap.put("Soucre Start", "STATE_CODE_007");
	 * stateCodeMap.put("Ensure Source Started", "STATE_CODE_008");
	 * stateCodeMap.put("Functional Test", "STATE_CODE_009");
	 * stateCodeMap.put("Source Stop", "STATE_CODE_010");
	 * stateCodeMap.put("Ensure Source Stopped", "STATE_CODE_011");
	 * stateCodeMap.put("Close Divertor Relay", "STATE_CODE_012");
	 * stateCodeMap.put("Open Divertor Relay", "STATE_CODE_013");
	 * stateCodeMap.put("Open FingerTip Latch", "STATE_CODE_014");
	 * stateCodeMap.put("Ensure FingerTip Latch Opened", "STATE_CODE_015");
	 * stateCodeMap.put("Open Stopper At Bay", "STATE_CODE_016");
	 * stateCodeMap.put("Close Stopper At Bay", "STATE_CODE_017");
	 * stateCodeMap.put("Error Handling", "STATE_CODE_018");
	 * stateCodeMap.put("Idle Condition", "STATE_CODE_019");
	 * }
	 * 
	 * public StateFlowRow(String path, String state, String stateErrorCode, String
	 * success, String successErrorCode, String failed, String failedErrorCode) {
	 * this.path = new SimpleStringProperty(path);
	 * this.state = new SimpleStringProperty(state);
	 * this.stateCode = new SimpleStringProperty(stateErrorCode);
	 * this.success = new SimpleStringProperty(success);
	 * this.successStateCode = new SimpleStringProperty(successErrorCode);
	 * this.fail = new SimpleStringProperty(failed);
	 * this.failStateCode = new SimpleStringProperty(failedErrorCode);
	 * 
	 * // Add a listener to update stateCode whenever state changes
	 * this.state.addListener((observable, oldValue, newValue) -> {
	 * String mappedCode = stateCodeMap.getOrDefault(newValue, "UNKNOWN_CODE");
	 * this.stateCode.set(mappedCode);
	 * });
	 * }
	 * 
	 * public String getPath() {
	 * return path.get();
	 * }
	 * 
	 * public SimpleStringProperty pathProperty() {
	 * return path;
	 * }
	 * 
	 * public String getState() {
	 * return state.get();
	 * }
	 * 
	 * public SimpleStringProperty stateProperty() {
	 * return state;
	 * }
	 * 
	 * public void setState(String state) {
	 * this.state.set(state);
	 * }
	 * 
	 * public String getStateErrorCode() {
	 * return stateCode.get();
	 * }
	 * 
	 * public SimpleStringProperty stateErrorCodeProperty() {
	 * return stateCode;
	 * }
	 * 
	 * public void setStateErrorCode(String stateCode) {
	 * this.stateCode.set(stateCode);
	 * }
	 * 
	 * public String getSuccess() {
	 * return success.get();
	 * }
	 * 
	 * public SimpleStringProperty successProperty() {
	 * return success;
	 * }
	 * 
	 * public void setSuccess(String success) {
	 * this.success.set(success);
	 * }
	 * 
	 * public String getSuccessErrorCode() {
	 * return successStateCode.get();
	 * }
	 * 
	 * public SimpleStringProperty successErrorCodeProperty() {
	 * return successStateCode;
	 * }
	 * 
	 * public void setSuccessErrorCode(String successStateCode) {
	 * this.successStateCode.set(successStateCode);
	 * }
	 * 
	 * public String getFailed() {
	 * return fail.get();
	 * }
	 * 
	 * public SimpleStringProperty failedProperty() {
	 * return fail;
	 * }
	 * 
	 * public void setFailed(String failed) {
	 * this.fail.set(failed);
	 * }
	 * 
	 * public String getFailedErrorCode() {
	 * return failStateCode.get();
	 * }
	 * 
	 * public SimpleStringProperty failedErrorCodeProperty() {
	 * return failStateCode;
	 * }
	 * 
	 * public void setFailedErrorCode(String failStateCode) {
	 * this.failStateCode.set(failStateCode);
	 * }
	 * 
	 * 
	 * 
	 * }
	 */
	/*
	 * public static TableView<StateFlowRow> getTableStatePlannerFtBay_UI() {
	 * return tableStatePlanner_FtBay_UI;
	 * }
	 * 
	 * public void setTableStatePlannerFtBay_UI(TableView<StateFlowRow>
	 * tableStatePlannerFtBay_UI) {
	 * this.tableStatePlanner_FtBay_UI = tableStatePlannerFtBay_UI;
	 * }
	 */

	// ================================================================

}
