package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.bay.configloader.VoltMeter;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantVoltPm;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.VoltPmDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmVoltPm;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import gnu.io.CommPortIdentifier;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class VoltPmPortSetupController implements Initializable {

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private Map<String, String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

	Timer voltPm1ClusterSelectionOnChangeTimer;
	// Timer qr2ClusterSelectionOnChangeTimer;

	Timer voltPm1BaySelectionOnChangeTimer;
	// Timer qr2BaySelectionOnChangeTimer;

	Timer voltPm1PositionIdOnChangeTimer;
	// Timer qr2PositionIdOnChangeTimer;
	/*
	 * @FXML
	 * private ComboBox<Integer> cmbBxPowerSrcBaudRate;
	 * 
	 * @FXML
	 * private ComboBox<Integer> cmbBxRefStdBaudRate;
	 */

	@FXML
	private Button btn_Save;
	

	// @FXML
	// private Button btnValidatePwrSrcCmd;
	// private static Button ref_btnValidatePwrSrcCmd;

	// @FXML
	// private Button btnValidateRefStdCmd;
	// private static Button ref_btnValidateRefStdCmd;

	@FXML
	private ComboBox<String> cmbBxVoltPm1ClusterId;
	// @FXML private ComboBox<String> cmbBxVoltPm2ClusterId;

	@FXML
	private ComboBox<String> cmbBxVoltPm1BayId;
	// @FXML private ComboBox<String> cmbBxVoltPm2BayId;

	@FXML
	private ComboBox<String> cmbBxVoltPm1PositionId;
	@FXML
	private ComboBox<String> cmbBxVoltPm1DeviceAddress;

	private static ComboBox<String> ref_cmbBxVoltPm1ClusterId;
	// private static ComboBox<String> ref_cmbBxVoltPm2ClusterId;

	private static ComboBox<String> ref_cmbBxVoltPm1BayId;
	// private static ComboBox<String> ref_cmbBxVoltPm2BayId;

	private static ComboBox<String> ref_cmbBxVoltPm1PositionId;

	private static ComboBox<String> ref_cmbBxVoltPm1DeviceAddress;
	// private static ComboBox<String> ref_cmbBxVoltPm2PositionId;

	@FXML
	private TextField txtVoltPm1Cname;
	// @FXML private TextField txtVoltPm2Cname;

	private static TextField ref_txtVoltPm1Cname;
	// private static TextField ref_txtVoltPm2Cname;

	@FXML
	private ComboBox<Integer> cmbBxVoltPm1_BaudRate;
	// @FXML private ComboBox<Integer> cmbBxVoltPm2_BaudRate;

	@FXML
	private ComboBox<String> cmbBxVoltPm1_PortSelection;
	// @FXML private ComboBox<String> cmbBxVoltPm2_PortSelection;
	/*
	 * @FXML
	 * private ComboBox<String> cmbBxPowerSource_ModelName;
	 * 
	 * @FXML
	 * private ComboBox<String> cmbBxReferanceStd_ModelName;
	 */

	@FXML
	private ComboBox<String> cmbBxVoltPm1_ModelName;
	// @FXML private ComboBox<String> cmbBxVoltPm2_ModelName;

	@FXML
	private TextField txtVoltPm1ReadData;
	// @FXML private TextField txtVoltPmReadData2;

	@FXML
	private TextField txtValidateVoltPm1_CmdStatus;
	// @FXML private TextField txtValidateVoltPm2_CmdStatus;

	@FXML
	private Button btnValidateVoltPm1_Cmd;
	// @FXML private Button btnValidateVoltPm_Cmd2;
	private static Button ref_btnValidateVoltPm1_Cmd;
	// private static Button ref_btnValidateLDU_Cmd2;

	Timer voltPm1_ValidateTimer;

	private static boolean PortValidationTurnedON = false;

	/*
	 * Timer UI_DisplayTimer = new Timer();
	 * UI_DisplayTimerTask UI_DisplayTimerTaskObj;
	 * 
	 * MyRunnable myRunnable;
	 * Thread myRunnableThread;
	 */

	// public SerialDataManager serialDM_Obj = new SerialDataManager();
	ConveyorDataManager DisplayDataObj = new ConveyorDataManager();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ref_assignment();
		/*
		 * Platform.runLater(() -> {
		 * enableBusyLoadingScreen();
		 * });
		 */
		initializeComPorts();

		// UI_DisplayTimerTaskObj = new UI_DisplayTimerTask(txtValidateRefStdCmdStatus);
		// myRunnable = new MyRunnable(txtValidateRefStdCmdStatus);
		disableGuiObjects();
		// SerialDataManager.setSkipCurrentTP_Execution(false);
		// disableBusyLoadingScreen();
		// if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
		applyUacSettings();
		// }
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("VoltPmPortSetupController : applyUacSettings :  Entry");
	}

	public void disableGuiObjects() {

	}

	public void ref_assignment() {
		ref_btnValidateVoltPm1_Cmd = btnValidateVoltPm1_Cmd;
		ref_cmbBxVoltPm1ClusterId = cmbBxVoltPm1ClusterId;
		// ref_cmbBxVoltPm2ClusterId = cmbBxVoltPm2ClusterId;

		ref_cmbBxVoltPm1BayId = cmbBxVoltPm1BayId;
		// ref_cmbBxVoltPm2BayId = cmbBxVoltPm2BayId;

		ref_cmbBxVoltPm1PositionId = cmbBxVoltPm1PositionId;

		ref_cmbBxVoltPm1DeviceAddress = cmbBxVoltPm1DeviceAddress;
		// ref_cmbBxVoltPm2PositionId = cmbBxVoltPm2PositionId;

		ref_txtVoltPm1Cname = txtVoltPm1Cname;
		// ref_txtVoltPm2Cname = txtVoltPm2Cname;

		// ref_btnValidatePwrSrcCmd = btnValidatePwrSrcCmd;
		// ref_btnValidateRefStdCmd = btnValidateRefStdCmd;
	}

	public void initializeComPorts() {

		setupComPortsBaudRate();

		loadAvailableComPorts();

		// updatePowerSourceModel();
		// updateReferenceMeterModel();
		updateLDUModel();
		loadDataFromConfig();
		// loadQrScannerName();
		load_saved_device_settings();
	}

	public void loadDataFromConfig() {

		ApplicationLauncher.logger.debug("loadDataFromConfig-M: Entry");
		// ref_cmbBxDut1ClusterId.getItems().clear();

		// ref_cmbBxDutClusterId1;
		// ref_cmbBxDutBayId1;

		// =======================================
		ref_cmbBxVoltPm1ClusterId.getItems().clear();
		// ref_cmbBxVoltPm2ClusterId.getItems().clear();
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxVoltPm1ClusterId.getItems().add(eachClusterDetail.getName());
					// ref_cmbBxVoltPm2ClusterId.getItems().add(eachClusterDetail.getName());
					getClusterNameIdListMap().put(eachClusterDetail.getName(), eachClusterDetail.getClusterId());
					ArrayList<String> bayList = new ArrayList<String>();
					getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);

					for (Bay eachBay : eachClusterDetail.getBay()) {

						// ref_cmbBxBaySelection.getItems().add(eachBay.getBayName());
						bayList.add(eachBay.getBayName());
						getClusterBayNameListMap().put(eachClusterDetail.getName(), bayList);
						Map<String, String> bayNameIdMap = new HashMap<String, String>();
						bayNameIdMap.put(eachBay.getBayName(), eachBay.getBayId());
						getClusterBayNameIdMap().put(eachClusterDetail.getName() + "_" + eachBay.getBayName(),
								eachBay.getBayId());
						// getClusterBayNameIdMap().put(eachClusterDetail.getName(), bayNameIdMap);
						// ApplicationLauncher.logger.debug("loadDataFromConfig :
						// getClusterBayNameIdMap().get(clusterName)-1 :"+ getClusterBayNameIdMap());

					}
				}

			}

		}

		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			String clusterId = "";
			String bayId = "";
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					clusterId = getClusterNameIdListMap().get(eachClusterDetail.getName());
					for (Bay eachBay : eachClusterDetail.getBay()) {
						bayId = getClusterBayNameIdMap().get(eachClusterDetail.getName() + "_" + eachBay.getBayName());
						ArrayList<String> positionNoList = new ArrayList<String>();
						ArrayList<String> addressList = new ArrayList<String>();
						for (VoltMeter eachVoltMeterDevice : eachTerminal.getVoltMeter()) {
							if ((eachVoltMeterDevice.getClusterId().equals(clusterId))
									&& (eachVoltMeterDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								positionNoList.add(eachVoltMeterDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachVoltMeterDevice.getPositionId(),
										eachVoltMeterDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachVoltMeterDevice.getPositionId(),
										eachVoltMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug(
										"loadDataFromConfig : getDeviceId :" + eachVoltMeterDevice.getDeviceId());
								if (eachVoltMeterDevice.isRs485Enabled()) {
									addressList = eachVoltMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger
											.debug("loadDataFromConfig-V : addressList :" + addressList.toString());
								}
								// addressList =
								// GUIUtils.extractDeviceIdAddressList(eachVoltMeterDevice.getDeviceId(),ConstantConveyor.DEVICE_TYPE_VOLT_METER);
								/*
								 * for(String eachAddress : addressList) {
								 * getClusterBayPositionNoAddressListMap().put(
								 * eachClusterDetail.getName()+"_"+eachBay.getBayName()+"_"+
								 * eachMegaOhmMeterDevice.getPositionId(),
								 * eachAddress);
								 * }
								 */
								if (addressList.size() > 0) {
									getClusterBayPositionNoAddressListMap().put(eachClusterDetail.getName() + "_"
											+ eachBay.getBayName() + "_" + eachVoltMeterDevice.getPositionId(),
											addressList);
								}

							}

						}
						if (positionNoList.size() > 0) {
							getClusterBayNamePositionListMap()
									.put(eachClusterDetail.getName() + "_" + eachBay.getBayName(), positionNoList);
						}

					}

				}

			}
		}

		getClusterBayPositionNoCnameMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger.debug("loadDataFromConfig-V: getClusterBayPositionNoCnameMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		getClusterBayNameIdMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger
					.debug("loadDataFromConfig-V: getClusterBayNameIdMap: key : " + e.getKey() + " -> " + e.getValue());
		});

		getClusterBayNamePositionListMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger.debug("loadDataFromConfig-V: getClusterBayNamePositionListMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		if (ref_cmbBxVoltPm1ClusterId.getItems().size() > 0) {
			ref_cmbBxVoltPm1ClusterId.getSelectionModel().select(0);
			// ref_cmbBxVoltPm2ClusterId.getSelectionModel().select(0);

		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxVoltPm1BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem().toString()));
				// ref_cmbBxVoltPm2BayId.getItems().addAll(getClusterBayNameListMap().get(ref_cmbBxVoltPm2ClusterId.getSelectionModel().getSelectedItem().toString()));
			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}
		if (ref_cmbBxVoltPm1BayId.getItems().size() > 0) {

			ref_cmbBxVoltPm1BayId.getSelectionModel().select(0);
			// ref_cmbBxVoltPm2BayId.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxVoltPm1BayId.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			String bayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

			ArrayList<VoltMeter> voltMeterList = (ArrayList<VoltMeter>) getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getVoltMeter().stream())
					.filter(e2 -> e2.getBayId().equals(bayId))
					.collect(Collectors.toList());
			ArrayList<String> cNameList = new ArrayList<String>();
			for (VoltMeter eachQrDevice : voltMeterList) {

				ref_cmbBxVoltPm1PositionId.getItems().add(eachQrDevice.getPositionId());
				// ref_cmbBxVoltPm2PositionId.getItems().add(eachQrDevice.getPositionId());
				cNameList.add(eachQrDevice.getPortName());
			}
			// ref_cmbBxQrCname1.getItems().addAll(cNameList);

			ref_cmbBxVoltPm1DeviceAddress.getItems().clear();
			if (ref_cmbBxVoltPm1PositionId.getItems().size() > 0) {
				ref_cmbBxVoltPm1PositionId.getSelectionModel().select(0);
				String positionNo = ref_cmbBxVoltPm1PositionId.getSelectionModel().getSelectedItem();
				if (getClusterBayPositionNoAddressListMap()
						.containsKey(clusterName + "_" + bayName + "_" + positionNo)) {
					ref_cmbBxVoltPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(clusterName + "_" + bayName + "_" + positionNo));
					ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().select(0);
				}
				// ref_cmbBxVoltPm2PositionId.getSelectionModel().select(0);
				// ref_cmbBxVoltPmCname1.getSelectionModel().select(0);
				// ref_txtVoltPmCname1.setText(arg0);
			}
			if (cNameList.size() > 0) {
				ref_txtVoltPm1Cname.setText(cNameList.get(0));
				// ref_txtVoltPm2Cname.setText(cNameList.get(0));
			}

		}
	}

	public void loadVoltMeterCname() {

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		// ConstantApp MyPropertyObj= new ConstantApp();
		// ModelName = ConstantQrScanner.QR_SCANNER_MODEL;

		List<VoltMeter> voltMeterList = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getVoltMeter().stream())
				.collect(Collectors.toList());
		// .filter(p -> searchPortName.equals(p.getPortName()))
		// .findFirst();

		for (VoltMeter eachScanner : voltMeterList) {
			ModelList.add(eachScanner.getPortName());
		}

		ref_txtVoltPm1Cname.setText("");// .getItems().clear();
		// ref_txtQr2Cname.setText("");//.getItems().clear();

		if (ModelList.size() > 0) {
			/*
			 * ref_txtQr1Cname.getItems().addAll(ModelList);
			 * ref_txtQr1Cname.getSelectionModel().select(0);
			 * 
			 * ref_txtQr2Cname.getItems().addAll(ModelList);
			 * ref_txtQr2Cname.getSelectionModel().select(0);
			 */
		}

	}

	public void guiVoltPmRefresh(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		JSONObject saved_ldu_setting = MySQL_Controller.sp_getdevice_setting(modelKey);

		String selectedClusterName = "";

		ComboBox<String> clusterId = (ComboBox<String>) clusterIdObj;
		ComboBox<String> bayId = (ComboBox<String>) bayIdObj;
		ComboBox<String> positionId = (ComboBox<String>) positionIdObj;
		TextField c_name = (TextField) c_nameObj;
		ComboBox<String> portName = (ComboBox<String>) portNameObj;
		ComboBox<Integer> baudRate = (ComboBox<Integer>) baudRateObj;

		try {
			if (saved_ldu_setting.has("cluster_name")) {
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedClusterName = saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

			} else {
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("No-ClusterId" + qrNo);
				ApplicationLauncher.logger.debug(
						"guiVoltPmRefresh: VoltPm" + qrNo + " cluster_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiVoltPmRefresh: VoltPm" + qrNo + " cluster_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiVoltPmRefresh: VoltPm" + qrNo + " cluster_name:  Data not retrieved from database");

		}

		String selectedBayName = "";

		try {
			if (saved_ldu_setting.has("bay_name")) {
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = saved_ldu_setting.getString("bay_name");
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

			} else {
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("No-BayId" + qrNo);
				ApplicationLauncher.logger
						.debug("guiVoltPmRefresh: VoltPm" + qrNo + " bay_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiVoltPmRefresh: VoltPm" + qrNo + " bay_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			bayId.getSelectionModel().select("Bay-Ex1");
			ApplicationLauncher.logger
					.info("guiVoltPmRefresh: VoltPm" + qrNo + " bay_name:  Data not retrieved from database");

		}

		String selectedPositionNo = "";

		try {
			if (saved_ldu_setting.has("position_no")) {
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);
				/*
				 * selectedClusterName =
				 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				 * ref_cmbBxQr1PositionId.getItems().clear();
				 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
				 * get(selectedClusterName + "_"+selectedBayName));
				 */
				if (getClusterBayPositionNoAddressListMap()
						.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
					ref_cmbBxVoltPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().select(0);
				}

			} else {
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("No-PositionId" + qrNo);
				ApplicationLauncher.logger.debug(
						"guiVoltPmRefresh: VoltPm" + qrNo + " position_no  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiVoltPmRefresh: VoltPm" + qrNo + " position_no: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiVoltPmRefresh: VoltPm" + qrNo + " position_no:  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("c_name")) {
				c_name.setText(saved_ldu_setting.getString("c_name"));
			} else {
				c_name.setText("");
				ApplicationLauncher.logger
						.debug("guiVoltPmRefresh: qr Port name" + qrNo + " Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiVoltPmRefresh: qr_port_name" + qrNo + ": JSONException-1:" + e.getMessage());
			c_name.setText("");
			ApplicationLauncher.logger
					.info("guiVoltPmRefresh: qr_port_name" + qrNo + ":  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("port_name")) {
				portName.setValue(saved_ldu_setting.getString("port_name"));
			} else {
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiVoltPmRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiVoltPmRefresh: JSONException5-1:" + e.getMessage());
			portName.setValue("");
			ApplicationLauncher.logger
					.info("guiVoltPmRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("baud_rate")) {
				baudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate")));
			} else {
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiVoltPmRefresh: LDU_BaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiVoltPmRefresh: JSONException6-1:" + e.getMessage());
			baudRate.setValue(9600);
			ApplicationLauncher.logger.info("guiVoltPmRefresh: LDU_BaudRate: Data not retrieved from database");

		}

	}

	public void guiRefreshVoltPmV2(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		DeviceSetting savedVoltPmDeviceSetting = MySqlServiceManager.getDeviceSettingService()
				.findFirstByDeviceTypeKey(modelKey);

		// JSONObject saved_ldu_setting =
		// MySQL_Controller.sp_getdevice_setting(modelKey);
		if (savedVoltPmDeviceSetting != null) {
			String selectedClusterName = "";

			ComboBox<String> clusterId = (ComboBox<String>) clusterIdObj;
			ComboBox<String> bayId = (ComboBox<String>) bayIdObj;
			ComboBox<String> positionId = (ComboBox<String>) positionIdObj;
			TextField c_name = (TextField) c_nameObj;
			ComboBox<String> portName = (ComboBox<String>) portNameObj;
			ComboBox<Integer> baudRate = (ComboBox<Integer>) baudRateObj;

			try {
				// if(saved_ldu_setting.has("cluster_name")){
				// ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedClusterName = savedVoltPmDeviceSetting.getClusterName();// saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * clusterId.getSelectionModel().select("No-ClusterId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshLduV2: VoltPm"+ qrNo +
				 * " cluster_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshVoltPmV2: VoltPm" + qrNo + " cluster_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshVoltPmV2: VoltPm" + qrNo + " cluster_name:  Data not retrieved from database");

			}

			String selectedBayName = "";

			try {
				// if(saved_ldu_setting.has("bay_name")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = savedVoltPmDeviceSetting.getBayName();// saved_ldu_setting.getString("bay_name");
				ApplicationLauncher.logger.debug("guiRefreshVoltPmV2: selectedBayName: " + selectedBayName);
				ApplicationLauncher.logger.debug("guiRefreshVoltPmV2: selectedClusterName: " + selectedClusterName);
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * bayId.getSelectionModel().select("No-BayId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshVoltPmV2: VoltPm"+ qrNo +
				 * " bay_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshVoltPmV2: VoltPm" + qrNo + " bay_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger
						.info("guiRefreshVoltPmV2: VoltPm" + qrNo + " bay_name:  Data not retrieved from database");

			}

			String selectedPositionNo = "";

			try {
				// if(saved_ldu_setting.has("position_no")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = savedVoltPmDeviceSetting.getPositionNo();// saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);
				/*
				 * selectedClusterName =
				 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
				 * ref_cmbBxQr1PositionId.getItems().clear();
				 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
				 * get(selectedClusterName + "_"+selectedBayName));
				 */
				if (getClusterBayPositionNoAddressListMap()
						.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
					ref_cmbBxVoltPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().select(0);
				}

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * positionId.getSelectionModel().select("No-PositionId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshVoltPmV2: VoltPm"+ qrNo +
				 * " position_no  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshVoltPmV2: VoltPm" + qrNo + " position_no: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshVoltPmV2: VoltPm" + qrNo + " position_no:  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("c_name")){
				c_name.setText(savedVoltPmDeviceSetting.getCanName());// saved_ldu_setting.getString("c_name"));
				/*
				 * } else {
				 * c_name.setText("");
				 * ApplicationLauncher.logger.debug("guiRefreshVoltPmV2: qr Port name"+ qrNo +
				 * " Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshVoltPmV2: VoltPm_port_name" + qrNo + ": Exception-1:" + e.getMessage());
				c_name.setText("");
				ApplicationLauncher.logger
						.info("guiRefreshVoltPmV2: VoltPm_port_name" + qrNo + ":  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("port_name")){
				portName.setValue(savedVoltPmDeviceSetting.getPortName());// saved_ldu_setting.getString("port_name"));
				/*
				 * } else {
				 * portName.setValue("");
				 * ApplicationLauncher.logger.info("guiRefreshVoltPmV2: LDU"+ qrNo +
				 * "_PortSelection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshVoltPmV2: JSONException5-1:" + e.getMessage());
				portName.setValue("");
				ApplicationLauncher.logger.info(
						"guiRefreshVoltPmV2: VoltPm: " + qrNo + "_PortSelection: Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("baud_rate")){
				baudRate.setValue(Integer.parseInt(savedVoltPmDeviceSetting.getBaudRate()));// saved_ldu_setting.getString("baud_rate")));
				/*
				 * } else {
				 * baudRate.setValue(9600);
				 * ApplicationLauncher.logger.
				 * info("guiRefreshVoltPmV2: LDU_BaudRate: Data not retrieved from DB");
				 * 
				 * }
				 */
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshVoltPmV2: JSONException6-1:" + e.getMessage());
				baudRate.setValue(9600);
				ApplicationLauncher.logger
						.info("guiRefreshVoltPmV2: VoltPm_BaudRate: Data not retrieved from database");

			}
		}
	}

	public void load_saved_device_settings() {

		// JSONObject saved_pwr_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		// JSONObject saved_ref_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_REF_STD);
		// JSONObject saved_ldu1_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_1);
		// JSONObject saved_ldu2_setting =
		// MySQL_Controller.sp_getdevice_setting(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);

		ArrayList<String> modelKeyList = new ArrayList<String>();
		modelKeyList.add(ConstantVoltPm.VOLT_PM_TYPE_ELM_1);
		// modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);

		ArrayList<Object> clusterIdList = new ArrayList<Object>();
		clusterIdList.add(ref_cmbBxVoltPm1ClusterId);
		// clusterIdList.add(ref_cmbBxQr2ClusterId);

		ArrayList<Object> bayIdList = new ArrayList<Object>();
		bayIdList.add(ref_cmbBxVoltPm1BayId);
		// bayIdList.add(ref_cmbBxVoltPm2BayId);

		ArrayList<Object> positionIdList = new ArrayList<Object>();
		positionIdList.add(ref_cmbBxVoltPm1PositionId);
		// positionIdList.add(ref_cmbBxVoltPm2PositionId);

		ArrayList<TextField> CnameList = new ArrayList<TextField>();
		CnameList.add(ref_txtVoltPm1Cname);
		// CnameList.add(ref_txtVoltPm2Cname);

		ArrayList<Object> portNameList = new ArrayList<Object>();
		portNameList.add(cmbBxVoltPm1_PortSelection);
		// portNameList.add(cmbBxVoltPm2_PortSelection);

		ArrayList<Object> baudRateList = new ArrayList<Object>();
		baudRateList.add(cmbBxVoltPm1_BaudRate);
		// baudRateList.add(cmbBxVoltPm2_BaudRate);

		/*
		 * for(int i = 0; i < 1; i++) {
		 * guiVoltPmRefresh(i + 1, modelKeyList.get(i),clusterIdList.get(i),
		 * bayIdList.get(i), positionIdList.get(i), CnameList.get(i),
		 * portNameList.get(i), baudRateList.get(i));
		 * }
		 */
		for (int i = 0; i < 1; i++) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				guiRefreshVoltPmV2(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			} else {
				// guiLduRefresh(i + 1, modelKeyList.get(i),clusterIdList.get(i),
				// bayIdList.get(i), positionIdList.get(i), CnameList.get(i),
				// portNameList.get(i), baudRateList.get(i));
				guiVoltPmRefresh(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			}
		}

		String selectedClusterName = "";

		String selectedBayName = "";

		String selectedPositionNo = "";

	}

	public static JSONObject get_device_settings(String InputSourceType) {

		JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
		return saved_pwr_setting;
	}

	public void setupComPortsBaudRate() {
		cmbBxVoltPm1_BaudRate.getItems().clear();
		cmbBxVoltPm1_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		cmbBxVoltPm1_BaudRate.getSelectionModel().select(ConstantVoltPm.VOLT_PM_DEFAULT_BAUD_RATE);
	}

	public static boolean getPortValidationTurnedON() {
		return PortValidationTurnedON;
	}

	public static void setPortValidationTurnedON(boolean status) {
		PortValidationTurnedON = status;

	}

	public void loadAvailableComPorts() {

		// Stub for testing

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantConveyorConfig.REFSTD;
		ModelList.add("Com1");
		ModelList.add("Com2");
		ModelList.add("Com3");

		scanSerialPortAndUpdateDisplay();// dramesh
	}

	public void updateLDUModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantVoltPm.VOLT_PM_ELM_MODEL;
		ModelList.add(ModelName);

		// ref_cmbBxQrScannerName.get

		cmbBxVoltPm1_ModelName.getItems().clear();
		cmbBxVoltPm1_ModelName.getItems().addAll(ModelList);
		cmbBxVoltPm1_ModelName.getSelectionModel().select(0);
	}

	public void scanSerialPortAndUpdateDisplay() {
        new Thread(() -> {
            java.util.List<String> availablePorts = new java.util.ArrayList<>();
            java.util.Enumeration sysPorts = CommPortIdentifier.getPortIdentifiers();
            while (sysPorts.hasMoreElements()) {
                CommPortIdentifier curPort = (CommPortIdentifier) sysPorts.nextElement();
                if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    availablePorts.add(curPort.getName());
                }
            }
            
            javafx.application.Platform.runLater(() -> {
                // Mock enumeration to inject ports into original UI code
                java.util.Enumeration ports = java.util.Collections.enumeration(availablePorts);

		cmbBxVoltPm1_PortSelection.getItems().clear();

		while (ports.hasMoreElements()) {
			String curPortName = (String) ports.nextElement();

			if (true) {

				cmbBxVoltPm1_PortSelection.getItems().add(curPortName);
			}
		}

		try {
			cmbBxVoltPm1_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
		}
            });
        }).start();
}

	public void SaveOnClick() {
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;

		String device1_type_key = ConstantVoltPm.VOLT_PM_TYPE_ELM_1;

		String device1_model_name = cmbBxVoltPm1_ModelName.getSelectionModel().getSelectedItem();

		String device1_port_name = cmbBxVoltPm1_PortSelection.getSelectionModel().getSelectedItem();

		String device1_baud_rate = cmbBxVoltPm1_BaudRate.getSelectionModel().getSelectedItem().toString();

		String device1ClusterName = (String) ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem();

		String device1BayName = (String) ref_cmbBxVoltPm1BayId.getSelectionModel().getSelectedItem();

		String device1PositionNo = (String) ref_cmbBxVoltPm1PositionId.getSelectionModel().getSelectedItem();
		// String device2PositionNo =
		// (String)ref_cmbBxVoltPm2PositionId.getSelectionModel().getSelectedItem();

		String device1Cname = ref_txtVoltPm1Cname.getText();// getSelectionModel().getSelectedItem();
		// String device2Cname =
		// ref_txtVoltPm2Cname.getText();//getSelectionModel().getSelectedItem();

		String deviceId1 = getClusterBayPositionNoDeviceIdMap().get(
				device1ClusterName + "_" + device1BayName + "_" + device1PositionNo);// ref_txtDut1Cname.getText();//ref_cmbBxDutCname1.getSelectionModel().getSelectedItem();
		// String deviceId2 = getClusterBayPositionNoDeviceIdMap().get(
		// device2ClusterName+"_"+device2BayName+"_"+device2PositionNo);
		// MySQL_Controller.sp_add_device_settings(1, pwr_type, pwr_model_name,
		// pwr_port_name, pwr_baud_rate);
		// MySQL_Controller.sp_add_device_settings(2, ref_type, ref_model_name,
		// ref_port_name, ref_baud_rate);
		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
			String clusterId = getClusterNameIdListMap().get(device1ClusterName);
			String bayId = getClusterBayNameIdMap().get(device1ClusterName + "_" + device1BayName);
			String deviceType = ConstantConveyor.DEVICE_TYPE_VOLT_METER;
			DeviceSetting deviceSetting = new DeviceSetting(device1_type_key, device1_model_name, device1_port_name,
					device1_baud_rate,
					clusterId, device1ClusterName, bayId, device1BayName, device1PositionNo, device1Cname, deviceId1,
					deviceType);

			MySqlServiceManager.getDeviceSettingService().saveToDb(deviceSetting);
		} else {
			MySQL_Controller.sp_add_device_settings_v2(device1_type_key, device1_model_name, device1_port_name,
					device1_baud_rate,
					device1ClusterName, device1BayName, device1PositionNo, device1Cname, deviceId1);
		}

		if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
			ConveyorDataManager.loadDeviceSettingFromDb();
		}
		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	public void voltPm1_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		txtValidateVoltPm1_CmdStatus.clear();
		txtVoltPm1ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID1();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
			// boolean status =
			// DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
			String portCname = ref_txtVoltPm1Cname.getText();
			SpmVoltPm serialPortManagerQrScanner = new SpmVoltPm(portCname, Elmeasure_MultiMeter.ER_LENGTH_ASCII);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				txtValidateVoltPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				// status = serialDM_Obj.lscsLDU1_CheckCom();
				// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
				serialPortManagerQrScanner.startSerialRxPhysical_VoltPm();
				serialPortManagerQrScanner.enableSerialRxPhysical_VoltPmMonitor();
				VoltPmDirector pwrSrcDirector = new VoltPmDirector(serialPortManagerQrScanner);
				String slaveId = ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().getSelectedItem();// ConstantBayPortNameMapping.ERC_MEGA_OHM_METER_01_SLAVE_ID;
				ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd: slaveId: " + slaveId);
				Map<String, Object> responseMap = pwrSrcDirector.fetchVoltMeterMetrics(slaveId);

				status = (boolean) responseMap.get("status");
				String responseData = "";
				try {
					if (status) {
						responseData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd: responseData1: " + responseData);
						Elmeasure_MultiMeter elmeasure_MultiMeter = new Elmeasure_MultiMeter();
						responseData = elmeasure_MultiMeter
								.extractVoltageValueFromResponse((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd: responseData2: " + responseData);
					} else {
						ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("voltPm1_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					txtValidateVoltPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					txtValidateVoltPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					txtVoltPm1ReadData.setText(responseData);
				}
				setPortValidationTurnedON(false);
				// DisplayDataObj.pwrSrcDisconnectPort_V2();
				serialPortManagerQrScanner.disconnectVoltPm();
				// DisplayDataObj.setLDU1_ReadDataFlag(false);
			}
			// serialDM_Obj.DisconnectLDU1();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("voltPm1_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("voltPm1_ValidateSerialCmd : Exit");

	}

	private String getCurrentLDU_ComBaudRate1() {

		return cmbBxVoltPm1_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID1() {

		return cmbBxVoltPm1_PortSelection.getSelectionModel().getSelectedItem();
	}

	@FXML
	public void voltPm1_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr1_ValidateSerialCmdTrigger: Invoked:");
		voltPm1_ValidateTimer = new Timer();
		voltPm1_ValidateTimer.schedule(new VoltPm1_ValidateTimerTask(), 100);// 1000);

	}

	class VoltPm1_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateVoltPm1_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("VoltPm1_ValidateTimerTask: WAIT");
			try {
				/*
				 * if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED){
				 * LDU_ValidateSerialCmd();
				 * } else if (ProcalFeatureEnable.LSCS_LDU_CONNECTED){
				 * lscsLDU1_ValidateSerialCmd();
				 * }
				 */
				voltPm1_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("VoltPm1_ValidateTimerTask: Exception:" + e.getMessage());
			}
			voltPm1_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("VoltPm1_ValidateTimerTask: DEFAULT");
			ref_btnValidateVoltPm1_Cmd.setDisable(false);
		}
	}

	public void enableBusyLoadingScreen() {// long time_in_seconds) {
		ApplicationLauncher.logger.info("SystemSettingController: enableBusyLoadingScreen: entry");

		// FXMLLoader loader = new FXMLLoader(
		// getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
		Parent nodeFromFXML = null;
		try {
			nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
			ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger
					.error("SystemSettingController: enableBusyLoadingScreen: Exception: " + e.getMessage());
		}
	}

	public void disableBusyLoadingScreen() {
		BusyLoadingController.removeBusyLoadingScreenOverlay();
	}

	private Parent getNodeFromFXML(String url) throws IOException {
		return FXMLLoader.load(getClass().getResource(url));
	}

	@FXML
	public void cmbBxVoltPm1BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		voltPm1BaySelectionOnChangeTimer = new Timer();
		voltPm1BaySelectionOnChangeTimer.schedule(new VoltPm1BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxVoltPm1ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		voltPm1ClusterSelectionOnChangeTimer = new Timer();
		voltPm1ClusterSelectionOnChangeTimer.schedule(new VoltPm1ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxVoltPmClusterId1;
	// ref_cmbBxVoltPmBayId1;

	class VoltPm1ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					ref_cmbBxVoltPm1BayId.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxVoltPm1BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxVoltPm1BayId.getSelectionModel().select(0);

				}
			});
			voltPm1ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class VoltPm1BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxVoltPm1BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_tbViewOutputPortData.getItems().clear();
					// ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxVoltPm1PositionId.getItems().clear();
					// ref_cmbBxVoltPm1DeviceAddress.getItems().clear();
					// ApplicationLauncher.logger.debug("cmbBxVoltPm1PositionIdOnChange:
					// selectedClusterName : " +selectedClusterName);
					// ApplicationLauncher.logger.debug("cmbBxVoltPm1PositionIdOnChange:
					// selectedBayName : " +selectedBayName);
					// ApplicationLauncher.logger.debug("cmbBxVoltPm1PositionIdOnChange:
					// getClusterBayNamePositionListMap : " +
					// getClusterBayNamePositionListMap().get(selectedClusterName +
					// "_"+selectedBayName));
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxVoltPm1PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxVoltPm1PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxVoltPm1PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtVoltPm1Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}

						/*
						 * String positionNo =
						 * ref_cmbBxVoltPm1PositionId.getSelectionModel().getSelectedItem();
						 * if(getClusterBayPositionNoAddressListMap().containsKey(selectedClusterName+
						 * "_"+selectedBayName + "_"+ positionNo)) {
						 * ref_cmbBxVoltPm1DeviceAddress.getItems().addAll(
						 * getClusterBayPositionNoAddressListMap().get(selectedClusterName+"_"+
						 * selectedBayName + "_"+ positionNo));
						 * ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().select(0);
						 * }
						 */
					} else {
						ref_txtVoltPm1Cname.setText("");
					}
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxVoltPmPositionId1.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxVoltPmPositionId1.getSelectionModel().select(0);

				}
			});
			voltPm1BaySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxVoltPm1PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxVoltPm1PositionIdOnChange: Entry");
		voltPm1PositionIdOnChangeTimer = new Timer();
		voltPm1PositionIdOnChangeTimer.schedule(new VoltPm1PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxVoltPmClusterId1;
	// ref_cmbBxVoltPmBayId1;

	class VoltPm1PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxVoltPm1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_cmbBxVoltPm1DeviceAddress.getItems().clear();
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxVoltPmBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxVoltPm1BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxVoltPm1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtVoltPm1Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}
					// String positionNo =
					// ref_cmbBxVoltPm1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoAddressListMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_cmbBxVoltPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
						ref_cmbBxVoltPm1DeviceAddress.getSelectionModel().select(0);
					}

				}
			});
			voltPm1PositionIdOnChangeTimer.cancel();

		}
	}

	public Map<String, String> getClusterBayNameIdMap() {
		return clusterBayNameIdMap;
	}

	public void setClusterBayNameIdMap(Map<String, String> clusterBayNameIdMap) {
		this.clusterBayNameIdMap = clusterBayNameIdMap;
	}

	public Map<String, String> getClusterBayPositionNoCnameMap() {
		return clusterBayPositionNoCnameMap;
	}

	public void setClusterBayPositionNoCnameMap(Map<String, String> clusterBayPositionNoCnameMap) {
		this.clusterBayPositionNoCnameMap = clusterBayPositionNoCnameMap;
	}

	public Map<String, ArrayList<String>> getClusterBayNamePositionListMap() {
		return clusterBayNamePositionListMap;
	}

	public void setClusterBayNamePositionListMap(Map<String, ArrayList<String>> clusterBayNamePositionListMap) {
		this.clusterBayNamePositionListMap = clusterBayNamePositionListMap;
	}

	public Map<String, String> getClusterNameIdListMap() {
		return clusterNameIdListMap;
	}

	public void setClusterNameIdListMap(Map<String, String> clusterIdNameListMap) {
		this.clusterNameIdListMap = clusterIdNameListMap;
	}

	public Map<String, ArrayList<String>> getClusterBayNameListMap() {
		return clusterBayNameListMap;
	}

	public void setClusterBayNameListMap(Map<String, ArrayList<String>> bayNameListMap) {
		this.clusterBayNameListMap = bayNameListMap;
	}

	public static TerminalBayConfigModel getBayConfigModel() {
		return bayConfigModel;
	}

	public static void setBayConfigModel(TerminalBayConfigModel bayConfigModel) {
		VoltPmPortSetupController.bayConfigModel = bayConfigModel;
	}

	public Map<String, String> getClusterBayPositionNoDeviceIdMap() {
		return clusterBayPositionNoDeviceIdMap;
	}

	public void setClusterBayPositionNoDeviceIdMap(Map<String, String> clusterBayPositionNoDeviceIdMap) {
		this.clusterBayPositionNoDeviceIdMap = clusterBayPositionNoDeviceIdMap;
	}

	public Map<String, ArrayList<String>> getClusterBayPositionNoAddressListMap() {
		return clusterBayPositionNoAddressListMap;
	}

	public void setClusterBayPositionNoAddressListMap(
			Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap) {
		this.clusterBayPositionNoAddressListMap = clusterBayPositionNoAddressListMap;
	}
}
