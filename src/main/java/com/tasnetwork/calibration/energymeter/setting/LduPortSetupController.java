package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.conveyor.bay.Elmeasure_MultiMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Ldu;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantLdu;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.LduDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmLdu;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import gnu.io.CommPortIdentifier;
import javafx.application.Application;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LduPortSetupController implements Initializable {

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private Map<String, String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

	Timer ldu1ClusterSelectionOnChangeTimer;
	// Timer qr2ClusterSelectionOnChangeTimer;

	Timer ldu1BaySelectionOnChangeTimer;
	// Timer qr2BaySelectionOnChangeTimer;

	Timer ldu1PositionIdOnChangeTimer;

	@FXML
	private Button btn_Save;
	private static Button ref_btn_Save;

	@FXML
	private ComboBox<String> cmbBxLdu1ClusterId;
	// @FXML private ComboBox<String> cmbBxLdu2ClusterId;

	@FXML
	private ComboBox<String> cmbBxLdu1BayId;

	@FXML
	private ComboBox<String> cmbBxLdu1PositionId;
	@FXML
	private ComboBox<String> cmbBxLdu1DeviceAddress;

	private static ComboBox<String> ref_cmbBxLdu1ClusterId;
	// private static ComboBox<String> ref_cmbBxLdu2ClusterId;

	private static ComboBox<String> ref_cmbBxLdu1BayId;
	// private static ComboBox<String> ref_cmbBxLdu2BayId;

	private static ComboBox<String> ref_cmbBxLdu1PositionId;

	private static ComboBox<String> ref_cmbBxLdu1DeviceAddress;
	// private static ComboBox<String> ref_cmbBxLdu2PositionId;

	@FXML
	private TextField txtLdu1Cname;
	// @FXML private TextField txtLdu2Cname;

	private static TextField ref_txtLdu1Cname;
	// private static TextField ref_txtLdu2Cname;

	@FXML
	private ComboBox<Integer> cmbBxLdu1_BaudRate;

	static private ComboBox<Integer> ref_cmbBxLdu1_BaudRate;
	// @FXML private ComboBox<Integer> cmbBxLdu2_BaudRate;

	@FXML
	private ComboBox<String> cmbBxLdu1_PortSelection;

	static private ComboBox<String> ref_cmbBxLdu1_PortSelection;
	// @FXML private ComboBox<String> cmbBxLdu2_PortSelection;
	/*
	 * @FXML
	 * private ComboBox<String> cmbBxPowerSource_ModelName;
	 * 
	 * @FXML
	 * private ComboBox<String> cmbBxReferanceStd_ModelName;
	 */

	@FXML
	private ComboBox<String> cmbBxLdu1_ModelName;

	static private ComboBox<String> ref_cmbBxLdu1_ModelName;
	// @FXML private ComboBox<String> cmbBxLdu2_ModelName;

	@FXML
	private TextField txtLdu1ReadData;
	// @FXML private TextField txtLduReadData2;

	static private TextField ref_txtLdu1ReadData;

	@FXML
	private TextField txtValidateLdu1_CmdStatus;
	// @FXML private TextField txtValidateLdu2_CmdStatus;

	static private TextField ref_txtValidateLdu1_CmdStatus;

	@FXML
	private Button btnValidateLdu1_Cmd;
	// @FXML private Button btnValidateLdu_Cmd2;
	private static Button ref_btnValidateLdu1_Cmd;
	// private static Button ref_btnValidateLDU_Cmd2;

	Timer ldu1_ValidateTimer;

	private static HashMap FXML_PortMap = new HashMap();

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

		initializeComPorts();

		disableGuiObjects();

		applyUacSettings();
		// }
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("LduPortSetupController : applyUacSettings :  Entry");

	}

	public void disableGuiObjects() {

	}

	public void ref_assignment() {
		ref_btnValidateLdu1_Cmd = btnValidateLdu1_Cmd;
		// ref_btnValidateLDU_Cmd2 = btnValidateLdu_Cmd2;
		ref_btn_Save = btn_Save;

		ref_cmbBxLdu1ClusterId = cmbBxLdu1ClusterId;
		// ref_cmbBxLdu2ClusterId = cmbBxLdu2ClusterId;

		ref_cmbBxLdu1BayId = cmbBxLdu1BayId;
		// ref_cmbBxLdu2BayId = cmbBxLdu2BayId;

		ref_cmbBxLdu1PositionId = cmbBxLdu1PositionId;

		ref_cmbBxLdu1DeviceAddress = cmbBxLdu1DeviceAddress;
		// ref_cmbBxLdu2PositionId = cmbBxLdu2PositionId;

		ref_txtLdu1Cname = txtLdu1Cname;

		ref_cmbBxLdu1_ModelName = cmbBxLdu1_ModelName;
		ref_cmbBxLdu1_PortSelection = cmbBxLdu1_PortSelection;
		ref_cmbBxLdu1_BaudRate = cmbBxLdu1_BaudRate;
		ref_txtLdu1ReadData = txtLdu1ReadData;
		ref_txtValidateLdu1_CmdStatus = txtValidateLdu1_CmdStatus;
		// ref_txtLdu2Cname = txtLdu2Cname;

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
		ref_cmbBxLdu1ClusterId.getItems().clear();
		// ref_cmbBxLdu2ClusterId.getItems().clear();
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxLdu1ClusterId.getItems().add(eachClusterDetail.getName());
					// ref_cmbBxLdu2ClusterId.getItems().add(eachClusterDetail.getName());
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
						for (Ldu eachLduDevice : eachTerminal.getLdu()) {
							if ((eachLduDevice.getClusterId().equals(clusterId))
									&& (eachLduDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								positionNoList.add(eachLduDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachLduDevice.getPositionId(),
										eachLduDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachLduDevice.getPositionId(),
										eachLduDevice.getDeviceId());
								ApplicationLauncher.logger
										.debug("loadDataFromConfig : getDeviceId :" + eachLduDevice.getDeviceId());
								if (eachLduDevice.isRs485Enabled()) {
									addressList = eachLduDevice.getRs485DeviceIdList();
								}
								// addressList =
								// GUIUtils.extractDeviceIdAddressList(eachLduDevice.getDeviceId(),ConstantConveyor.DEVICE_TYPE_VOLT_METER);
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
											+ eachBay.getBayName() + "_" + eachLduDevice.getPositionId(), addressList);
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

		if (ref_cmbBxLdu1ClusterId.getItems().size() > 0) {
			ref_cmbBxLdu1ClusterId.getSelectionModel().select(0);
			// ref_cmbBxLdu2ClusterId.getSelectionModel().select(0);

		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxLdu1BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem().toString()));
				// ref_cmbBxLdu2BayId.getItems().addAll(getClusterBayNameListMap().get(ref_cmbBxLdu2ClusterId.getSelectionModel().getSelectedItem().toString()));
			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}
		if (ref_cmbBxLdu1BayId.getItems().size() > 0) {

			ref_cmbBxLdu1BayId.getSelectionModel().select(0);
			// ref_cmbBxLdu2BayId.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxLdu1BayId.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			String bayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

			ArrayList<Ldu> lduList = (ArrayList<Ldu>) getBayConfigModel().getTerminal().stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getLdu().stream())
					.filter(e2 -> e2.getBayId().equals(bayId))
					.collect(Collectors.toList());
			ArrayList<String> cNameList = new ArrayList<String>();
			for (Ldu eachQrDevice : lduList) {

				ref_cmbBxLdu1PositionId.getItems().add(eachQrDevice.getPositionId());
				// ref_cmbBxLdu2PositionId.getItems().add(eachQrDevice.getPositionId());
				cNameList.add(eachQrDevice.getPortName());
			}
			// ref_cmbBxQrCname1.getItems().addAll(cNameList);

			ref_cmbBxLdu1DeviceAddress.getItems().clear();
			if (ref_cmbBxLdu1PositionId.getItems().size() > 0) {
				ref_cmbBxLdu1PositionId.getSelectionModel().select(0);
				String positionNo = ref_cmbBxLdu1PositionId.getSelectionModel().getSelectedItem();
				if (getClusterBayPositionNoAddressListMap()
						.containsKey(clusterName + "_" + bayName + "_" + positionNo)) {
					ref_cmbBxLdu1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(clusterName + "_" + bayName + "_" + positionNo));
					ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
				}
				// ref_cmbBxLdu2PositionId.getSelectionModel().select(0);
				// ref_cmbBxLduCname1.getSelectionModel().select(0);
				// ref_txtLduCname1.setText(arg0);
			}
			if (cNameList.size() > 0) {
				ref_txtLdu1Cname.setText(cNameList.get(0));
				// ref_txtLdu2Cname.setText(cNameList.get(0));
			}

		}
	}

	public void loadVoltMeterCname() {

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		// ConstantApp MyPropertyObj= new ConstantApp();
		// ModelName = ConstantQrScanner.QR_SCANNER_MODEL;

		List<Ldu> lduList = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getLdu().stream())
				.collect(Collectors.toList());
		// .filter(p -> searchPortName.equals(p.getPortName()))
		// .findFirst();

		for (Ldu eachDevice : lduList) {
			ModelList.add(eachDevice.getPortName());
		}

		ref_txtLdu1Cname.setText("");// .getItems().clear();
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

	public void guiLduRefresh(
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
				ApplicationLauncher.logger
						.debug("guiLduRefresh: qr" + qrNo + " cluster_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiLduRefresh: qr" + qrNo + " cluster_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiLduRefresh: qr" + qrNo + " cluster_name:  Data not retrieved from database");

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
						.debug("guiLduRefresh: qr" + qrNo + " bay_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiLduRefresh: qr" + qrNo + " bay_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			bayId.getSelectionModel().select("Bay-Ex1");
			ApplicationLauncher.logger
					.info("guiLduRefresh: qr" + qrNo + " bay_name:  Data not retrieved from database");

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
					ref_cmbBxLdu1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
				}

			} else {
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("No-PositionId" + qrNo);
				ApplicationLauncher.logger
						.debug("guiLduRefresh: qr" + qrNo + " position_no  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiLduRefresh: qr" + qrNo + " position_no: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiLduRefresh: qr" + qrNo + " position_no:  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("c_name")) {
				c_name.setText(saved_ldu_setting.getString("c_name"));
			} else {
				c_name.setText("");
				ApplicationLauncher.logger
						.debug("guiLduRefresh: qr Port name" + qrNo + " Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiLduRefresh: qr_port_name" + qrNo + ": JSONException-1:" + e.getMessage());
			c_name.setText("");
			ApplicationLauncher.logger
					.info("guiLduRefresh: qr_port_name" + qrNo + ":  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("port_name")) {
				portName.setValue(saved_ldu_setting.getString("port_name"));
			} else {
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiLduRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiLduRefresh: JSONException5-1:" + e.getMessage());
			portName.setValue("");
			ApplicationLauncher.logger
					.info("guiLduRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("baud_rate")) {
				baudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate")));
			} else {
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiLduRefresh: LDU_BaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiLduRefresh: JSONException6-1:" + e.getMessage());
			baudRate.setValue(9600);
			ApplicationLauncher.logger.info("guiLduRefresh: LDU_BaudRate: Data not retrieved from database");

		}

	}

	public void guiRefreshLduV2(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		DeviceSetting savedLduDeviceSetting = MySqlServiceManager.getDeviceSettingService()
				.findFirstByDeviceTypeKey(modelKey);

		// JSONObject saved_ldu_setting =
		// MySQL_Controller.sp_getdevice_setting(modelKey);
		if (savedLduDeviceSetting != null) {
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
				selectedClusterName = savedLduDeviceSetting.getClusterName();// saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * clusterId.getSelectionModel().select("No-ClusterId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshLduV2: qr"+ qrNo +
				 * " cluster_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshLduV2: qr" + qrNo + " cluster_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshLduV2: qr" + qrNo + " cluster_name:  Data not retrieved from database");

			}

			String selectedBayName = "";

			try {
				// if(saved_ldu_setting.has("bay_name")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = savedLduDeviceSetting.getBayName();// saved_ldu_setting.getString("bay_name");
				ApplicationLauncher.logger.debug("guiRefreshLduV2: selectedBayName: " + selectedBayName);
				ApplicationLauncher.logger.debug("guiRefreshLduV2: selectedClusterName: " + selectedClusterName);
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * bayId.getSelectionModel().select("No-BayId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshLduV2: qr"+ qrNo +
				 * " bay_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshLduV2: qr" + qrNo + " bay_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger
						.info("guiRefreshLduV2: qr" + qrNo + " bay_name:  Data not retrieved from database");

			}

			String selectedPositionNo = "";

			try {
				selectedPositionNo = savedLduDeviceSetting.getPositionNo();// saved_ldu_setting.getString("position_no");
				positionId.getSelectionModel().select(selectedPositionNo);

				if (getClusterBayPositionNoAddressListMap()
						.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
					ref_cmbBxLdu1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
				}

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshLduV2: qr" + qrNo + " position_no: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
				ApplicationLauncher.logger
						.info("guiRefreshLduV2: qr" + qrNo + " position_no:  Data not retrieved from database");

			}

			try {
				c_name.setText(savedLduDeviceSetting.getCanName());// saved_ldu_setting.

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshLduV2: qr_port_name" + qrNo + ": Exception-1:" + e.getMessage());
				c_name.setText("");
				ApplicationLauncher.logger
						.info("guiRefreshLduV2: qr_port_name" + qrNo + ":  Data not retrieved from database");

			}

			try {
				portName.setValue(savedLduDeviceSetting.getPortName());// saved_ldu_setting

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshLduV2: JSONException5-1:" + e.getMessage());
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiRefreshLduV2: LDU" + qrNo + "_PortSelection: Data not retrieved from database");

			}

			try {
				baudRate.setValue(Integer.parseInt(savedLduDeviceSetting.getBaudRate()));//
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshLduV2: JSONException6-1:" + e.getMessage());
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiRefreshLduV2: LDU_BaudRate: Data not retrieved from database");

			}
		}

	}

	public void load_saved_device_settings() {

		ArrayList<String> modelKeyList = new ArrayList<String>();
		modelKeyList.add(ConstantLdu.LDU_TYPE_LSCS_1);
		// modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);

		ArrayList<Object> clusterIdList = new ArrayList<Object>();
		clusterIdList.add(ref_cmbBxLdu1ClusterId);
		// clusterIdList.add(ref_cmbBxQr2ClusterId);

		ArrayList<Object> bayIdList = new ArrayList<Object>();
		bayIdList.add(ref_cmbBxLdu1BayId);
		// bayIdList.add(ref_cmbBxLdu2BayId);

		ArrayList<Object> positionIdList = new ArrayList<Object>();
		positionIdList.add(ref_cmbBxLdu1PositionId);
		// positionIdList.add(ref_cmbBxLdu2PositionId);

		ArrayList<TextField> CnameList = new ArrayList<TextField>();
		CnameList.add(ref_txtLdu1Cname);
		// CnameList.add(ref_txtLdu2Cname);

		ArrayList<Object> portNameList = new ArrayList<Object>();
		portNameList.add(ref_cmbBxLdu1_PortSelection);
		// portNameList.add(cmbBxLdu2_PortSelection);

		ArrayList<Object> baudRateList = new ArrayList<Object>();
		baudRateList.add(ref_cmbBxLdu1_BaudRate);
		// baudRateList.add(cmbBxLdu2_BaudRate);

		for (int i = 0; i < 1; i++) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				guiRefreshLduV2(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			} else {
				guiLduRefresh(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i), positionIdList.get(i),
						CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			}
		}
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
		ModelName = ConstantLdu.LDU_LSCS_MODEL;
		ModelList.add(ModelName);

		// ref_cmbBxQrScannerName.get

		ref_cmbBxLdu1_ModelName.getItems().clear();
		ref_cmbBxLdu1_ModelName.getItems().addAll(ModelList);
		ref_cmbBxLdu1_ModelName.getSelectionModel().select(0);

		try {
			ref_cmbBxLdu1_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
		}

	}

	public void ldu1_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		ref_txtValidateLdu1_CmdStatus.clear();
		ref_txtLdu1ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID1();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
			// boolean status =
			// DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
			String portCname = ref_txtLdu1Cname.getText();
			SpmLdu serialPortManager = new SpmLdu(portCname);
			boolean status = serialPortManager.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				ref_txtValidateLdu1_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				// status = serialDM_Obj.lscsLDU1_CheckCom();
				// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
				serialPortManager.startSerialRxPhysical_Ldu();
				serialPortManager.enableSerialRxPhysical_LduMonitor();
				LduDirector pwrSrcDirector = new LduDirector(serialPortManager);
				String slaveId = ref_cmbBxLdu1DeviceAddress.getSelectionModel().getSelectedItem();// ConstantBayPortNameMapping.ERC_MEGA_OHM_METER_01_SLAVE_ID;
				ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: slaveId: " + slaveId);
				Map<String, Object> responseMap = pwrSrcDirector.lduCheckCom(slaveId);

				status = (boolean) responseMap.get("status");
				String responseData = "";
				try {
					if (status) {
						responseData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: responseData1: " + responseData);
						Elmeasure_MultiMeter elmeasure_MultiMeter = new Elmeasure_MultiMeter();
						responseData = elmeasure_MultiMeter
								.extractVoltageValueFromResponse((String) responseMap.get("responseData"));
						ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: responseData2: " + responseData);
					} else {
						ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("ldu1_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					ref_txtValidateLdu1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					ref_txtValidateLdu1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					ref_txtLdu1ReadData.setText(responseData);
				}
				setPortValidationTurnedON(false);
				// DisplayDataObj.pwrSrcDisconnectPort_V2();
				serialPortManager.disconnectLdu();
				// DisplayDataObj.setLDU1_ReadDataFlag(false);
			}
			// serialDM_Obj.DisconnectLDU1();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ldu1_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("ldu1_ValidateSerialCmd : Exit");

	}

	private String getCurrentLDU_ComBaudRate1() {

		return ref_cmbBxLdu1_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID1() {

		return ref_cmbBxLdu1_PortSelection.getSelectionModel().getSelectedItem();
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
	public void cmbBxLdu1BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		ldu1BaySelectionOnChangeTimer = new Timer();
		ldu1BaySelectionOnChangeTimer.schedule(new Ldu1BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxLdu1ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		ldu1ClusterSelectionOnChangeTimer = new Timer();
		ldu1ClusterSelectionOnChangeTimer.schedule(new Ldu1ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxLduClusterId1;
	// ref_cmbBxLduBayId1;

	class Ldu1ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					ref_cmbBxLdu1BayId.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxLdu1BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxLdu1BayId.getSelectionModel().select(0);

				}
			});
			ldu1ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class Ldu1BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem();
				String selectedBayName = (String) ref_cmbBxLdu1BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {

					ref_cmbBxLdu1PositionId.getItems().clear();

					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxLdu1PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxLdu1PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxLdu1PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtLdu1Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}

					} else {
						ref_txtLdu1Cname.setText("");
					}

				}
			});
			ldu1BaySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxLdu1PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxLdu1PositionIdOnChange: Entry");
		ldu1PositionIdOnChangeTimer = new Timer();
		ldu1PositionIdOnChangeTimer.schedule(new Ldu1PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxLduClusterId1;
	// ref_cmbBxLduBayId1;

	class Ldu1PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxLdu1ClusterId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_cmbBxLdu1DeviceAddress.getItems().clear();
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxLduBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxLdu1BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxLdu1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtLdu1Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}
					// String positionNo =
					// ref_cmbBxLdu1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoAddressListMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_cmbBxLdu1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
						ref_cmbBxLdu1DeviceAddress.getSelectionModel().select(0);
					}

				}
			});
			ldu1PositionIdOnChangeTimer.cancel();

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
		LduPortSetupController.bayConfigModel = bayConfigModel;
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

	public void setupComPortsBaudRate() {

		/*
		 * cmbBxPowerSrcBaudRate.getItems().clear();
		 * cmbBxPowerSrcBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		 * if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
		 * cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantMtePowerSource.
		 * PowerSrcDefaultBaudRate);
		 * }else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		 * cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantLscsPowerSource.
		 * PowerSrcDefaultBaudRate);
		 * }
		 * cmbBxRefStdBaudRate.getItems().clear();
		 * cmbBxRefStdBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxRefStdBaudRate.getSelectionModel().select(ConstantRadiantRefStd.
		 * RefStdDefaultBaudRate);
		 */
		ref_cmbBxLdu1_BaudRate.getItems().clear();
		ref_cmbBxLdu1_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		ref_cmbBxLdu1_BaudRate.getSelectionModel().select(ConstantLdu.LDU_DEFAULT_BAUD_RATE);
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


		ref_cmbBxLdu1_PortSelection.getItems().clear();

		// Enumeration ports is now provided above

		while (ports.hasMoreElements()) {
			String curPortName = (String) ports.nextElement();

			if (true) {
				ref_cmbBxLdu1_PortSelection.getItems().add(curPortName);
			}
		}

		try {
			ref_cmbBxLdu1_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
		}
	
            });
        }).start();
}
}
