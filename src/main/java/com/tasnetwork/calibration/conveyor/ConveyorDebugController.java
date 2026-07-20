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
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ConveyorClientManager;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.DutDevice;
import com.tasnetwork.calibration.conveyor.bay.configloader.MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;
import com.tasnetwork.calibration.conveyor.bay.configloader.QrScanner;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

		ref_cmbBxPosLoadedTerminalSelection.getItems().add("1");
		ref_cmbBxPosLoadedTerminalSelection.getSelectionModel().select(0);

		ref_cmbBxPosLoadedDeviceTypeSelection.getItems().addAll(ConstantConveyor.DEVICE_TYPE_LIST);
		ref_cmbBxPosLoadedDeviceTypeSelection.getSelectionModel().select(0);
	}

	public void guiInit() {

		// P O S I T I O N L O A D E D - Column Values
		ref_colLpPositonNo.setCellValueFactory(cellData -> cellData.getValue().getPositionNoProperty());
		ref_colLpSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		ref_colLpCname.setCellValueFactory(cellData -> cellData.getValue().getcNameProperty());
		ref_colLpSelect.setEditable(true);
		ref_colLpSelect.setCellValueFactory(new PositionLoadedSelectCheckBoxValueFactory());
		ref_colLpSelect.setStyle("-fx-alignment: CENTER;");

		ref_tabBayTest.setDisable(true);

		SingleSelectionModel<Tab> selectionModel = ref_tabPaneConveyor.getSelectionModel();

		selectionModel.select(ref_tabPlcClientBayTest);
	}

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

	@FXML
	void btnSendCommDataOnClick() {
		sendCommDataTaskTimer = new Timer();
	}

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

			if (ref_txtAreaResponseDisplay.getText().isEmpty()) {
				ref_txtAreaResponseDisplay.setText(clusterResponseData.getMessage());
			} else {
				ref_txtAreaResponseDisplay
						.setText(ref_txtAreaResponseDisplay.getText() + "\n" + clusterResponseData.getMessage());
			}
			status = true;

			// ApplicationHomeController.EnableScanDeviceButton();
		} else {

			Platform.runLater(() -> {
				ref_txtAreaResponseDisplay
						.setText(ref_txtAreaResponseDisplay.getText() + "\n" + outputPortId + "-no response");
			});
			ApplicationHomeController.update_left_status("Device Connection Failed " + outputPortId + " :",
					ConstantApp.LEFT_STATUS_DEBUG);
		}
		return status;
	}

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

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	@FXML
	public void btnRefreshOnClick() {

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
}
