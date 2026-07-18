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

import com.tasnetwork.calibration.conveyor.bay.EIC_MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.bay.configloader.Bay;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.MegaOhmMeter;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantDutDevSys;
import com.tasnetwork.calibration.conveyor.constant.ConstantMegaOhmPm;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.conveyor.serial.director.MegaOhmPmDirector;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmMegaOhmPm;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
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

public class MegaOhmPmPortSetupController implements Initializable {

	private static TerminalBayConfigModel bayConfigModel = ConveyorDataManager.getTerminalBayConfig();
	private Map<String, ArrayList<String>> clusterBayNameListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterNameIdListMap = new HashMap<String, String>();
	private Map<String, String> clusterBayNameIdMap = new HashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayNamePositionListMap = new HashMap<String, ArrayList<String>>();
	private Map<String, String> clusterBayPositionNoCnameMap = new LinkedHashMap<String, String>();
	private Map<String, String> clusterBayPositionNoDeviceIdMap = new LinkedHashMap<String, String>();
	private Map<String, ArrayList<String>> clusterBayPositionNoAddressListMap = new LinkedHashMap<String, ArrayList<String>>();

	Timer megaOhmPm1ClusterSelectionOnChangeTimer;
	// Timer qr2ClusterSelectionOnChangeTimer;

	Timer megaOhmPm1BaySelectionOnChangeTimer;
	// Timer qr2BaySelectionOnChangeTimer;

	Timer megaOhmPm1PositionIdOnChangeTimer;
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
	private static Button ref_btn_Save;

	// @FXML
	// private Button btnValidatePwrSrcCmd;
	// private static Button ref_btnValidatePwrSrcCmd;

	// @FXML
	// private Button btnValidateRefStdCmd;
	// private static Button ref_btnValidateRefStdCmd;

	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1ClusterId;
	// @FXML private ComboBox<String> cmbBxMegaOhmPm2ClusterId;

	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1BayId;
	// @FXML private ComboBox<String> cmbBxMegaOhmPm2BayId;

	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1PositionId;
	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1DeviceAddress;

	private static ComboBox<String> ref_cmbBxMegaOhmPm1ClusterId;
	// private static ComboBox<String> ref_cmbBxMegaOhmPm2ClusterId;

	private static ComboBox<String> ref_cmbBxMegaOhmPm1BayId;
	// private static ComboBox<String> ref_cmbBxMegaOhmPm2BayId;

	private static ComboBox<String> ref_cmbBxMegaOhmPm1PositionId;

	private static ComboBox<String> ref_cmbBxMegaOhmPm1DeviceAddress;
	// private static ComboBox<String> ref_cmbBxMegaOhmPm2PositionId;

	@FXML
	private TextField txtMegaOhmPm1Cname;
	// @FXML private TextField txtMegaOhmPm2Cname;

	private static TextField ref_txtMegaOhmPm1Cname;
	// private static TextField ref_txtMegaOhmPm2Cname;

	@FXML
	private ComboBox<Integer> cmbBxMegaOhmPm1_BaudRate;
	// @FXML private ComboBox<Integer> cmbBxMegaOhmPm2_BaudRate;

	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1_PortSelection;
	// @FXML private ComboBox<String> cmbBxMegaOhmPm2_PortSelection;
	/*
	 * @FXML
	 * private ComboBox<String> cmbBxPowerSource_ModelName;
	 * 
	 * @FXML
	 * private ComboBox<String> cmbBxReferanceStd_ModelName;
	 */

	@FXML
	private ComboBox<String> cmbBxMegaOhmPm1_ModelName;
	// @FXML private ComboBox<String> cmbBxMegaOhmPm2_ModelName;

	@FXML
	private TextField txtMegaOhmPm1ReadData;
	// @FXML private TextField txtMegaOhmPmReadData2;

	@FXML
	private TextField txtValidateMegaOhmPm1_CmdStatus;
	// @FXML private TextField txtValidateMegaOhmPm2_CmdStatus;

	@FXML
	private Button btnValidateMegaOhmPm1_Cmd;
	// @FXML private Button btnValidateMegaOhmPm_Cmd2;
	private static Button ref_btnValidateMegaOhmPm1_Cmd;
	// private static Button ref_btnValidateLDU_Cmd2;

	Timer megaOhmPm1_ValidateTimer;

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

		ApplicationLauncher.logger.info("MegaOhmPmPortSetupController : applyUacSettings :  Entry");
		/*
		 * ArrayList<UacDataModel> uacSelectProfileScreenList =
		 * DeviceDataManagerController.getUacSelectProfileScreenList();
		 * String screenName = "";
		 * for (int i = 0; i < uacSelectProfileScreenList.size(); i++){
		 * 
		 * screenName = uacSelectProfileScreenList.get(i).getScreenName();
		 * switch (screenName) {
		 * case ConstantApp.UAC_DEVICE_SETTINGS_SCREEN:
		 * 
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
		 * //ref_btn_deploy.setDisable(true);
		 * //ref_btnValidatePwrSrcCmd.setDisable(true);
		 * //ref_btnValidateRefStdCmd.setDisable(true);
		 * ref_btnValidateLDU_Cmd1.setDisable(true);
		 * ref_btnValidateLDU_Cmd2.setDisable(true);
		 * ref_btnValidateLDU_Cmd3.setDisable(true);
		 * ref_btnValidateLDU_Cmd4.setDisable(true);
		 * ref_btnValidateLDU_Cmd5.setDisable(true);
		 * ref_btnValidateLDU_Cmd6.setDisable(true);
		 * ref_btnValidateLDU_Cmd7.setDisable(true);
		 * ref_btnValidateLDU_Cmd8.setDisable(true);
		 * ref_btnValidateLDU_Cmd9.setDisable(true);
		 * ref_btnValidateLDU_Cmd10.setDisable(true);
		 * ref_btnValidateLDU_Cmd11.setDisable(true);
		 * ref_btnValidateLDU_Cmd12.setDisable(true);
		 * ref_btnValidateLDU_Cmd13.setDisable(true);
		 * ref_btnValidateLDU_Cmd14.setDisable(true);
		 * ref_btnValidateLDU_Cmd15.setDisable(true);
		 * ref_btnValidateLDU_Cmd16.setDisable(true);
		 * ref_btnValidateLDU_Cmd17.setDisable(true);
		 * ref_btnValidateLDU_Cmd18.setDisable(true);
		 * ref_btnValidateLDU_Cmd19.setDisable(true);
		 * ref_btnValidateLDU_Cmd20.setDisable(true);
		 * ref_btnValidateLDU_Cmd21.setDisable(true);
		 * ref_btnValidateLDU_Cmd22.setDisable(true);
		 * ref_btnValidateLDU_Cmd23.setDisable(true);
		 * ref_btnValidateLDU_Cmd24.setDisable(true);
		 * ref_btnValidateLDU_Cmd25.setDisable(true);
		 * ref_btnValidateLDU_Cmd26.setDisable(true);
		 * ref_btnValidateLDU_Cmd27.setDisable(true);
		 * ref_btnValidateLDU_Cmd28.setDisable(true);
		 * ref_btnValidateLDU_Cmd29.setDisable(true);
		 * ref_btnValidateLDU_Cmd30.setDisable(true);
		 * ref_btnValidateLDU_Cmd31.setDisable(true);
		 * ref_btnValidateLDU_Cmd32.setDisable(true);
		 * ref_btnValidateLDU_Cmd33.setDisable(true);
		 * ref_btnValidateLDU_Cmd34.setDisable(true);
		 * ref_btnValidateLDU_Cmd35.setDisable(true);
		 * ref_btnValidateLDU_Cmd36.setDisable(true);
		 * ref_btnValidateLDU_Cmd37.setDisable(true);
		 * ref_btnValidateLDU_Cmd38.setDisable(true);
		 * ref_btnValidateLDU_Cmd39.setDisable(true);
		 * ref_btnValidateLDU_Cmd40.setDisable(true);
		 * ref_btnValidateLDU_Cmd41.setDisable(true);
		 * ref_btnValidateLDU_Cmd42.setDisable(true);
		 * ref_btnValidateLDU_Cmd43.setDisable(true);
		 * ref_btnValidateLDU_Cmd44.setDisable(true);
		 * ref_btnValidateLDU_Cmd45.setDisable(true);
		 * ref_btnValidateLDU_Cmd46.setDisable(true);
		 * ref_btnValidateLDU_Cmd47.setDisable(true);
		 * ref_btnValidateLDU_Cmd48.setDisable(true);
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getAddPossible()){
		 * //ref_btn_Create.setDisable(true);
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
		 * //ref_vbox_testscript.setDisable(true);sdvsc
		 * //setChildPropertySaveEnabled(false);
		 * ref_btn_Save.setDisable(true);
		 * 
		 * 
		 * }
		 * 
		 * if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
		 * //ref_btn_Delete.setDisable(true);
		 * 
		 * }
		 * break;
		 * 
		 * 
		 * 
		 * default:
		 * break;
		 * }
		 * 
		 * 
		 * 
		 * }
		 */
	}

	public void disableGuiObjects() {

		/*
		 * for(int i = (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK +1); i <=
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION; i++){
		 * switch (i){
		 * 
		 * case 1:
		 * ref_btnValidateLDU_Cmd1.setDisable(true);
		 * break;
		 * case 2:
		 * ref_btnValidateLDU_Cmd2.setDisable(true);
		 * break;
		 * case 3:
		 * ref_btnValidateLDU_Cmd3.setDisable(true);
		 * break;
		 * case 4:
		 * ref_btnValidateLDU_Cmd4.setDisable(true);
		 * break;
		 * case 5:
		 * ref_btnValidateLDU_Cmd5.setDisable(true);
		 * break;
		 * case 6:
		 * ref_btnValidateLDU_Cmd6.setDisable(true);
		 * break;
		 * case 7:
		 * ref_btnValidateLDU_Cmd7.setDisable(true);
		 * break;
		 * case 8:
		 * ref_btnValidateLDU_Cmd8.setDisable(true);
		 * break;
		 * case 9:
		 * ref_btnValidateLDU_Cmd9.setDisable(true);
		 * break;
		 * case 10:
		 * ref_btnValidateLDU_Cmd10.setDisable(true);
		 * break;
		 * 
		 * case 11:
		 * ref_btnValidateLDU_Cmd11.setDisable(true);
		 * break;
		 * case 12:
		 * ref_btnValidateLDU_Cmd12.setDisable(true);
		 * break;
		 * case 13:
		 * ref_btnValidateLDU_Cmd13.setDisable(true);
		 * break;
		 * case 14:
		 * ref_btnValidateLDU_Cmd14.setDisable(true);
		 * break;
		 * case 15:
		 * ref_btnValidateLDU_Cmd15.setDisable(true);
		 * break;
		 * case 16:
		 * ref_btnValidateLDU_Cmd16.setDisable(true);
		 * break;
		 * case 17:
		 * ref_btnValidateLDU_Cmd17.setDisable(true);
		 * break;
		 * case 18:
		 * ref_btnValidateLDU_Cmd18.setDisable(true);
		 * break;
		 * case 19:
		 * ref_btnValidateLDU_Cmd19.setDisable(true);
		 * break;
		 * case 20:
		 * ref_btnValidateLDU_Cmd20.setDisable(true);
		 * break;
		 * 
		 * case 21:
		 * ref_btnValidateLDU_Cmd21.setDisable(true);
		 * break;
		 * case 22:
		 * ref_btnValidateLDU_Cmd22.setDisable(true);
		 * break;
		 * case 23:
		 * ref_btnValidateLDU_Cmd23.setDisable(true);
		 * break;
		 * case 24:
		 * ref_btnValidateLDU_Cmd24.setDisable(true);
		 * break;
		 * case 25:
		 * ref_btnValidateLDU_Cmd25.setDisable(true);
		 * break;
		 * case 26:
		 * ref_btnValidateLDU_Cmd26.setDisable(true);
		 * break;
		 * case 27:
		 * ref_btnValidateLDU_Cmd27.setDisable(true);
		 * break;
		 * case 28:
		 * ref_btnValidateLDU_Cmd28.setDisable(true);
		 * break;
		 * case 29:
		 * ref_btnValidateLDU_Cmd29.setDisable(true);
		 * break;
		 * case 30:
		 * ref_btnValidateLDU_Cmd30.setDisable(true);
		 * break;
		 * 
		 * case 31:
		 * ref_btnValidateLDU_Cmd31.setDisable(true);
		 * break;
		 * case 32:
		 * ref_btnValidateLDU_Cmd32.setDisable(true);
		 * break;
		 * case 33:
		 * ref_btnValidateLDU_Cmd33.setDisable(true);
		 * break;
		 * case 34:
		 * ref_btnValidateLDU_Cmd34.setDisable(true);
		 * break;
		 * case 35:
		 * ref_btnValidateLDU_Cmd35.setDisable(true);
		 * break;
		 * case 36:
		 * ref_btnValidateLDU_Cmd36.setDisable(true);
		 * break;
		 * case 37:
		 * ref_btnValidateLDU_Cmd37.setDisable(true);
		 * break;
		 * case 38:
		 * ref_btnValidateLDU_Cmd38.setDisable(true);
		 * break;
		 * case 39:
		 * ref_btnValidateLDU_Cmd39.setDisable(true);
		 * break;
		 * case 40:
		 * ref_btnValidateLDU_Cmd40.setDisable(true);
		 * break;
		 * 
		 * default:
		 * break;
		 * }
		 * 
		 * }
		 */

	}

	public void ref_assignment() {
		ref_btnValidateMegaOhmPm1_Cmd = btnValidateMegaOhmPm1_Cmd;
		// ref_btnValidateLDU_Cmd2 = btnValidateMegaOhmPm_Cmd2;
		ref_btn_Save = btn_Save;

		ref_cmbBxMegaOhmPm1ClusterId = cmbBxMegaOhmPm1ClusterId;
		// ref_cmbBxMegaOhmPm2ClusterId = cmbBxMegaOhmPm2ClusterId;

		ref_cmbBxMegaOhmPm1BayId = cmbBxMegaOhmPm1BayId;
		// ref_cmbBxMegaOhmPm2BayId = cmbBxMegaOhmPm2BayId;

		ref_cmbBxMegaOhmPm1PositionId = cmbBxMegaOhmPm1PositionId;

		ref_cmbBxMegaOhmPm1DeviceAddress = cmbBxMegaOhmPm1DeviceAddress;
		// ref_cmbBxMegaOhmPm2PositionId = cmbBxMegaOhmPm2PositionId;

		ref_txtMegaOhmPm1Cname = txtMegaOhmPm1Cname;
		// ref_txtMegaOhmPm2Cname = txtMegaOhmPm2Cname;

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
		ref_cmbBxMegaOhmPm1ClusterId.getItems().clear();
		// ref_cmbBxMegaOhmPm2ClusterId.getItems().clear();
		for (Terminal eachTerminal : getBayConfigModel().getTerminal()) {
			if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
				for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
					ref_cmbBxMegaOhmPm1ClusterId.getItems().add(eachClusterDetail.getName());
					// ref_cmbBxMegaOhmPm2ClusterId.getItems().add(eachClusterDetail.getName());
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
						for (MegaOhmMeter eachMegaOhmMeterDevice : eachTerminal.getMegaOhmMeter()) {
							if ((eachMegaOhmMeterDevice.getClusterId().equals(clusterId))
									&& (eachMegaOhmMeterDevice.getBayId().equals(bayId))) {
								// getClusterBayNamePositionListMap().put
								positionNoList.add(eachMegaOhmMeterDevice.getPositionId());
								getClusterBayPositionNoCnameMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachMegaOhmMeterDevice.getPositionId(),
										eachMegaOhmMeterDevice.getPortName());
								getClusterBayPositionNoDeviceIdMap().put(
										eachClusterDetail.getName() + "_" + eachBay.getBayName() + "_"
												+ eachMegaOhmMeterDevice.getPositionId(),
										eachMegaOhmMeterDevice.getDeviceId());
								ApplicationLauncher.logger.debug(
										"loadDataFromConfig : getDeviceId :" + eachMegaOhmMeterDevice.getDeviceId());
								if (eachMegaOhmMeterDevice.isRs485Enabled()) {
									addressList = eachMegaOhmMeterDevice.getRs485DeviceIdList();
									ApplicationLauncher.logger
											.debug("loadDataFromConfig-M : addressList :" + addressList.toString());
								}
								// addressList =
								// GUIUtils.extractDeviceIdAddressList(eachMegaOhmMeterDevice.getDeviceId(),ConstantConveyor.DEVICE_TYPE_OHM_METER);
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
											+ eachBay.getBayName() + "_" + eachMegaOhmMeterDevice.getPositionId(),
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
			ApplicationLauncher.logger.debug("loadDataFromConfig-M: getClusterBayPositionNoCnameMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		getClusterBayNameIdMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger
					.debug("loadDataFromConfig-M: getClusterBayNameIdMap: key : " + e.getKey() + " -> " + e.getValue());
		});

		getClusterBayNamePositionListMap().entrySet().forEach((e) -> {
			ApplicationLauncher.logger.debug("loadDataFromConfig-M: getClusterBayNamePositionListMap: key : "
					+ e.getKey() + " -> " + e.getValue());
		});

		if (ref_cmbBxMegaOhmPm1ClusterId.getItems().size() > 0) {
			ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel().select(0);
			// ref_cmbBxMegaOhmPm2ClusterId.getSelectionModel().select(0);

		}

		if (getClusterBayNameListMap().size() > 0) {
			if (getClusterBayNameListMap()
					.containsKey(ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel().getSelectedItem().toString())) {
				ref_cmbBxMegaOhmPm1BayId.getItems().addAll(getClusterBayNameListMap()
						.get(ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel().getSelectedItem().toString()));
				// ref_cmbBxMegaOhmPm2BayId.getItems().addAll(getClusterBayNameListMap().get(ref_cmbBxMegaOhmPm2ClusterId.getSelectionModel().getSelectedItem().toString()));
			}
			// ref_cmbBxBaySelection.getSelectionModel().select(0);

		}
		if (ref_cmbBxMegaOhmPm1BayId.getItems().size() > 0) {

			ref_cmbBxMegaOhmPm1BayId.getSelectionModel().select(0);
			// ref_cmbBxMegaOhmPm2BayId.getSelectionModel().select(0);
			String clusterName = (ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel().getSelectedItem().toString());
			String bayName = (ref_cmbBxMegaOhmPm1BayId.getSelectionModel().getSelectedItem().toString());

			String clusterId = getClusterNameIdListMap().get(clusterName);
			String bayId = getClusterBayNameIdMap().get(clusterName + "_" + bayName);
			// ApplicationLauncher.logger.debug("loadDataFromConfig :
			// getClusterBayNameIdMap().get(clusterName)-2 :"+
			// getClusterBayNameIdMap().get(clusterName));
			// String bayId = "11";//getClusterBayNameIdMap().get(clusterName).get(bayName);

			ApplicationLauncher.logger.debug("loadDataFromConfig : clusterId :" + clusterId);
			ApplicationLauncher.logger.debug("loadDataFromConfig : bayId :" + bayId);

			ArrayList<MegaOhmMeter> megaOhmMeterList = (ArrayList<MegaOhmMeter>) getBayConfigModel().getTerminal()
					.stream()
					.filter(e1 -> e1.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
					.flatMap(terminal -> terminal.getMegaOhmMeter().stream())
					.filter(e2 -> e2.getBayId().equals(bayId))
					.collect(Collectors.toList());
			ArrayList<String> cNameList = new ArrayList<String>();
			for (MegaOhmMeter eachQrDevice : megaOhmMeterList) {

				ref_cmbBxMegaOhmPm1PositionId.getItems().add(eachQrDevice.getPositionId());
				// ref_cmbBxMegaOhmPm2PositionId.getItems().add(eachQrDevice.getPositionId());
				cNameList.add(eachQrDevice.getPortName());
			}
			// ref_cmbBxQrCname1.getItems().addAll(cNameList);

			ref_cmbBxMegaOhmPm1DeviceAddress.getItems().clear();
			if (ref_cmbBxMegaOhmPm1PositionId.getItems().size() > 0) {
				ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().select(0);
				String positionNo = ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().getSelectedItem();
				if (getClusterBayPositionNoAddressListMap()
						.containsKey(clusterName + "_" + bayName + "_" + positionNo)) {
					ref_cmbBxMegaOhmPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(clusterName + "_" + bayName + "_" + positionNo));
					ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().select(0);
				}
				// ref_cmbBxMegaOhmPm2PositionId.getSelectionModel().select(0);
				// ref_cmbBxMegaOhmPmCname1.getSelectionModel().select(0);
				// ref_txtMegaOhmPmCname1.setText(arg0);
			}
			if (cNameList.size() > 0) {
				ref_txtMegaOhmPm1Cname.setText(cNameList.get(0));
				// ref_txtMegaOhmPm2Cname.setText(cNameList.get(0));
			}

		}
	}

	public void loadQrScannerName() {

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		// ConstantApp MyPropertyObj= new ConstantApp();
		// ModelName = ConstantQrScanner.QR_SCANNER_MODEL;

		List<MegaOhmMeter> megaOhmMeterList = ConveyorDataManager.getTerminalBayConfig().getTerminal().stream()
				.filter(e -> e.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID))
				.flatMap(terminal -> terminal.getMegaOhmMeter().stream())
				.collect(Collectors.toList());
		// .filter(p -> searchPortName.equals(p.getPortName()))
		// .findFirst();

		for (MegaOhmMeter eachScanner : megaOhmMeterList) {
			ModelList.add(eachScanner.getPortName());
		}

		ref_txtMegaOhmPm1Cname.setText("");// .getItems().clear();
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

	public void guiMegaOhmPmRefresh(
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
				ApplicationLauncher.logger.debug("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo
						+ " cluster_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error(
					"guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " cluster_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " cluster_name:  Data not retrieved from database");

		}

		String selectedBayName = "";

		try {
			if (saved_ldu_setting.has("bay_name")) {
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = saved_ldu_setting.getString("bay_name");
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				ApplicationLauncher.logger.info("guiMegaOhmPmRefresh: selectedClusterName : " + selectedClusterName);
				ApplicationLauncher.logger.info("guiMegaOhmPmRefresh: selectedBayName : " + selectedBayName);
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

			} else {
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("No-BayId" + qrNo);
				ApplicationLauncher.logger.debug(
						"guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " bay_name  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " bay_name: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			bayId.getSelectionModel().select("Bay-Ex1");
			ApplicationLauncher.logger
					.info("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " bay_name:  Data not retrieved from database");

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

			} else {
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("No-PositionId" + qrNo);
				ApplicationLauncher.logger.debug("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo
						+ " position_no  Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " position_no: JSONException-1:" + e.getMessage());
			// ref_cmbBxQrCname1.setValue("");
			positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
			ApplicationLauncher.logger
					.info("guiMegaOhmPmRefresh: MegaOhmPm" + qrNo + " position_no:  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("c_name")) {
				c_name.setText(saved_ldu_setting.getString("c_name"));
			} else {
				c_name.setText("");
				ApplicationLauncher.logger
						.debug("guiMegaOhmPmRefresh: qr Port name" + qrNo + " Selection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("guiMegaOhmPmRefresh: qr_port_name" + qrNo + ": JSONException-1:" + e.getMessage());
			c_name.setText("");
			ApplicationLauncher.logger
					.info("guiMegaOhmPmRefresh: qr_port_name" + qrNo + ":  Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("port_name")) {
				portName.setValue(saved_ldu_setting.getString("port_name"));
			} else {
				portName.setValue("");
				ApplicationLauncher.logger
						.info("guiMegaOhmPmRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiMegaOhmPmRefresh: JSONException5-1:" + e.getMessage());
			portName.setValue("");
			ApplicationLauncher.logger
					.info("guiMegaOhmPmRefresh: LDU" + qrNo + "_PortSelection: Data not retrieved from database");

		}

		try {
			if (saved_ldu_setting.has("baud_rate")) {
				baudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate")));
			} else {
				baudRate.setValue(9600);
				ApplicationLauncher.logger.info("guiMegaOhmPmRefresh: LDU_BaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("guiMegaOhmPmRefresh: JSONException6-1:" + e.getMessage());
			baudRate.setValue(9600);
			ApplicationLauncher.logger.info("guiMegaOhmPmRefresh: LDU_BaudRate: Data not retrieved from database");

		}

	}

	public void guiRefreshMegaOhmPmV2(
			int qrNo,
			String modelKey,
			Object clusterIdObj,
			Object bayIdObj,
			Object positionIdObj,
			TextField c_nameObj,
			Object portNameObj,
			Object baudRateObj) {

		DeviceSetting savedMegOhmDeviceSetting = MySqlServiceManager.getDeviceSettingService()
				.findFirstByDeviceTypeKey(modelKey);

		// JSONObject saved_ldu_setting =
		// MySQL_Controller.sp_getdevice_setting(modelKey);
		if (savedMegOhmDeviceSetting != null) {
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
				selectedClusterName = savedMegOhmDeviceSetting.getClusterName();// saved_ldu_setting.getString("cluster_name");
				clusterId.getSelectionModel().select(selectedClusterName);
				bayId.getItems().clear();
				bayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * clusterId.getSelectionModel().select("No-ClusterId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: MegaOhmPm"+ qrNo +
				 * " cluster_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " cluster_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				clusterId.getSelectionModel().select("Cluster-Ex" + qrNo);
				ApplicationLauncher.logger.info(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " cluster_name:  Data not retrieved from database");

			}

			String selectedBayName = "";

			try {
				// if(saved_ldu_setting.has("bay_name")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedBayName = savedMegOhmDeviceSetting.getBayName();// saved_ldu_setting.getString("bay_name");
				ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: selectedBayName: " + selectedBayName);
				ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: selectedClusterName: " + selectedClusterName);
				bayId.getSelectionModel().select(selectedBayName);
				selectedClusterName = (String) clusterId.getSelectionModel().getSelectedItem();
				positionId.getItems().clear();
				positionId.getItems()
						.addAll(getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * bayId.getSelectionModel().select("No-BayId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: MegaOhmPm"+ qrNo +
				 * " bay_name  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " bay_name: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				bayId.getSelectionModel().select("Bay-Ex1");
				ApplicationLauncher.logger.info(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " bay_name:  Data not retrieved from database");

			}

			String selectedPositionNo = "";

			try {
				// if(saved_ldu_setting.has("position_no")){
				// ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
				selectedPositionNo = savedMegOhmDeviceSetting.getPositionNo();// saved_ldu_setting.getString("position_no");
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
					ref_cmbBxMegaOhmPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
							.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().select(0);
				}

				/*
				 * } else {
				 * //ref_cmbBxQrCname1.setValue("");
				 * positionId.getSelectionModel().select("No-PositionId"+ qrNo);
				 * ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: MegaOhmPm"+ qrNo +
				 * " position_no  Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " position_no: Exception-1:" + e.getMessage());
				// ref_cmbBxQrCname1.setValue("");
				positionId.getSelectionModel().select("PositionId-Ex" + qrNo);
				ApplicationLauncher.logger.info(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + " position_no:  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("c_name")){
				c_name.setText(savedMegOhmDeviceSetting.getCanName());// saved_ldu_setting.getString("c_name"));
				/*
				 * } else {
				 * c_name.setText("");
				 * ApplicationLauncher.logger.debug("guiRefreshMegaOhmPmV2: qr Port name"+ qrNo
				 * + " Selection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger
						.error("guiRefreshMegaOhmPmV2: MegaOhmPm_port_name" + qrNo + ": Exception-1:" + e.getMessage());
				c_name.setText("");
				ApplicationLauncher.logger.info(
						"guiRefreshMegaOhmPmV2: MegaOhmPm_port_name" + qrNo + ":  Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("port_name")){
				portName.setValue(savedMegOhmDeviceSetting.getPortName());// saved_ldu_setting.getString("port_name"));
				/*
				 * } else {
				 * portName.setValue("");
				 * ApplicationLauncher.logger.info("guiRefreshMegaOhmPmV2: LDU"+ qrNo +
				 * "_PortSelection: Data not retrieved from DB");
				 * 
				 * }
				 */

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshMegaOhmPmV2: JSONException5-1:" + e.getMessage());
				portName.setValue("");
				ApplicationLauncher.logger.info(
						"guiRefreshMegaOhmPmV2: MegaOhmPm" + qrNo + "_PortSelection: Data not retrieved from database");

			}

			try {
				// if(saved_ldu_setting.has("baud_rate")){
				baudRate.setValue(Integer.parseInt(savedMegOhmDeviceSetting.getBaudRate()));// saved_ldu_setting.getString("baud_rate")));
				/*
				 * } else {
				 * baudRate.setValue(9600);
				 * ApplicationLauncher.logger.
				 * info("guiRefreshMegaOhmPmV2: LDU_BaudRate: Data not retrieved from DB");
				 * 
				 * }
				 */
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("guiRefreshMegaOhmPmV2: JSONException6-1:" + e.getMessage());
				baudRate.setValue(9600);
				ApplicationLauncher.logger
						.info("guiRefreshMegaOhmPmV2: MegaOhmPm_BaudRate: Data not retrieved from database");

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
		modelKeyList.add(ConstantMegaOhmPm.MEGA_OHM_PM_TYPE_EIC_1);
		// modelKeyList.add(ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2);

		ArrayList<Object> clusterIdList = new ArrayList<Object>();
		clusterIdList.add(ref_cmbBxMegaOhmPm1ClusterId);
		// clusterIdList.add(ref_cmbBxQr2ClusterId);

		ArrayList<Object> bayIdList = new ArrayList<Object>();
		bayIdList.add(ref_cmbBxMegaOhmPm1BayId);
		// bayIdList.add(ref_cmbBxMegaOhmPm2BayId);

		ArrayList<Object> positionIdList = new ArrayList<Object>();
		positionIdList.add(ref_cmbBxMegaOhmPm1PositionId);
		// positionIdList.add(ref_cmbBxMegaOhmPm2PositionId);

		ArrayList<TextField> CnameList = new ArrayList<TextField>();
		CnameList.add(ref_txtMegaOhmPm1Cname);
		// CnameList.add(ref_txtMegaOhmPm2Cname);

		ArrayList<Object> portNameList = new ArrayList<Object>();
		portNameList.add(cmbBxMegaOhmPm1_PortSelection);
		// portNameList.add(cmbBxMegaOhmPm2_PortSelection);

		ArrayList<Object> baudRateList = new ArrayList<Object>();
		baudRateList.add(cmbBxMegaOhmPm1_BaudRate);
		// baudRateList.add(cmbBxMegaOhmPm2_BaudRate);

		/*
		 * for(int i = 0; i < 1; i++) {
		 * guiMegaOhmPmRefresh(i + 1, modelKeyList.get(i),clusterIdList.get(i),
		 * bayIdList.get(i), positionIdList.get(i), CnameList.get(i),
		 * portNameList.get(i), baudRateList.get(i));
		 * 
		 * 
		 * }
		 */

		for (int i = 0; i < 1; i++) {
			if (ProconFeatureEnable.CONVEYOR_DEVICE_SETTING_SPRING_ENABLED) {
				guiRefreshMegaOhmPmV2(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			} else {
				guiMegaOhmPmRefresh(i + 1, modelKeyList.get(i), clusterIdList.get(i), bayIdList.get(i),
						positionIdList.get(i), CnameList.get(i), portNameList.get(i), baudRateList.get(i));

			}
		}

		/*
		 * guiQrRefresh(1, modelKeyList.get(0),clusterIdList.get(0), bayIdList.get(0),
		 * positionIdList.get(0), CnameList.get(0), portNameList.get(0),
		 * baudRateList.get(0));
		 * guiQrRefresh(2, modelKeyList.get(1),clusterIdList.get(1), bayIdList.get(1),
		 * positionIdList.get(1), CnameList.get(1), portNameList.get(1),
		 * baudRateList.get(1));
		 */

		/*
		 * JSONObject saved_ldu3_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU3);
		 * JSONObject saved_ldu4_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU4);
		 * JSONObject saved_ldu5_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU5);
		 * JSONObject saved_ldu6_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU6);
		 * JSONObject saved_ldu7_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU7);
		 * 
		 * 
		 * JSONObject saved_ldu8_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU8);
		 * JSONObject saved_ldu9_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU9);
		 * JSONObject saved_ldu10_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU10);
		 * JSONObject saved_ldu11_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU11);
		 * JSONObject saved_ldu12_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU12);
		 * JSONObject saved_ldu13_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU13);
		 * JSONObject saved_ldu14_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU14);
		 * JSONObject saved_ldu15_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU15);
		 * JSONObject saved_ldu16_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU16);
		 * JSONObject saved_ldu17_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU17);
		 * JSONObject saved_ldu18_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU18);
		 * JSONObject saved_ldu19_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU19);
		 * JSONObject saved_ldu20_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU20);
		 * JSONObject saved_ldu21_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU21);
		 * JSONObject saved_ldu22_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU22);
		 * JSONObject saved_ldu23_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU23);
		 * JSONObject saved_ldu24_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU24);
		 * JSONObject saved_ldu25_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU25);
		 * JSONObject saved_ldu26_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU26);
		 * JSONObject saved_ldu27_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU27);
		 * JSONObject saved_ldu28_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU28);
		 * JSONObject saved_ldu29_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU29);
		 * JSONObject saved_ldu30_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU30);
		 * JSONObject saved_ldu31_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU31);
		 * JSONObject saved_ldu32_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU32);
		 * JSONObject saved_ldu33_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU33);
		 * JSONObject saved_ldu34_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU34);
		 * JSONObject saved_ldu35_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU35);
		 * JSONObject saved_ldu36_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU36);
		 * JSONObject saved_ldu37_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU37);
		 * JSONObject saved_ldu38_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU38);
		 * JSONObject saved_ldu39_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU39);
		 * JSONObject saved_ldu40_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU40);
		 * JSONObject saved_ldu41_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU41);
		 * JSONObject saved_ldu42_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU42);
		 * JSONObject saved_ldu43_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU43);
		 * JSONObject saved_ldu44_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU44);
		 * JSONObject saved_ldu45_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU45);
		 * JSONObject saved_ldu46_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU46);
		 * JSONObject saved_ldu47_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU47);
		 * JSONObject saved_ldu48_setting =
		 * MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU48);
		 */
		// if(true){//Condition TO BE DEFINED
		/*
		 * try {
		 * if(saved_pwr_setting.has("port_name")){
		 * cmbBxPowerSrcPortSelection.setValue(saved_pwr_setting.getString("port_name"))
		 * ;
		 * } else{
		 * 
		 * cmbBxPowerSrcPortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException1:"+e.getMessage());
		 * cmbBxPowerSrcPortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from database"
		 * );
		 * }
		 * try {
		 * if(saved_pwr_setting.has("baud_rate")){
		 * cmbBxPowerSrcBaudRate.setValue(Integer.parseInt(saved_pwr_setting.getString(
		 * "baud_rate")));
		 * } else{
		 * cmbBxPowerSrcBaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException2:"+e.getMessage());
		 * cmbBxPowerSrcBaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * try {
		 * if(saved_ref_setting.has("port_name")){
		 * cmbBxRefStdPortSelection.setValue(saved_ref_setting.getString("port_name"));
		 * } else{
		 * cmbBxRefStdPortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException3:"+e.getMessage());
		 * cmbBxRefStdPortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * try {
		 * if(saved_ref_setting.has("baud_rate")){
		 * cmbBxRefStdBaudRate.setValue(Integer.parseInt(saved_ref_setting.getString(
		 * "baud_rate")));
		 * } else{
		 * cmbBxRefStdBaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException4:"+e.getMessage());
		 * cmbBxRefStdBaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		String selectedClusterName = "";
		/*
		 * try {
		 * if(saved_ldu1_setting.has("cluster_name")){
		 * //ref_cmbBxDutCname1.setValue(saved_ldu1_setting.getString("c_name"));
		 * selectedClusterName = saved_ldu1_setting.getString("cluster_name");
		 * ref_cmbBxQr1ClusterId.getSelectionModel().select(selectedClusterName);
		 * ref_cmbBxQr1BayId.getItems().clear();
		 * ref_cmbBxQr1BayId.getItems().addAll(getClusterBayNameListMap().get(
		 * selectedClusterName));
		 * 
		 * 
		 * } else {
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1ClusterId.getSelectionModel().select("No-ClusterId1");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr1 cluster_name  Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr1 cluster_name: JSONException-1:"+e.
		 * getMessage());
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1ClusterId.getSelectionModel().select("Cluster-Ex1");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr1 cluster_name:  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu2_setting.has("cluster_name")){
		 * selectedClusterName = saved_ldu2_setting.getString("cluster_name");
		 * ref_cmbBxQr2ClusterId.getSelectionModel().select(selectedClusterName);
		 * //ref_cmbBxQr2ClusterId.getSelectionModel().select(selectedClusterName);
		 * ref_cmbBxQr2BayId.getItems().clear();
		 * ref_cmbBxQr2BayId.getItems().addAll(getClusterBayNameListMap().get(
		 * selectedClusterName));
		 * } else {
		 * ref_cmbBxQr2ClusterId.getSelectionModel().select("No-ClusterId2");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr2 cluster_name Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr2 cluster_name: JSONException-1:"+e.
		 * getMessage());
		 * ref_cmbBxQr2ClusterId.getSelectionModel().select("Cluster-Ex2");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr2 cluster_name :  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		String selectedBayName = "";

		/*
		 * try {
		 * if(saved_ldu1_setting.has("bay_name")){
		 * //ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
		 * selectedBayName = saved_ldu1_setting.getString("bay_name");
		 * ref_cmbBxQr1BayId.getSelectionModel().select(selectedBayName);
		 * selectedClusterName =
		 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
		 * ref_cmbBxQr1PositionId.getItems().clear();
		 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
		 * get(selectedClusterName + "_"+selectedBayName));
		 * 
		 * 
		 * } else {
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1BayId.getSelectionModel().select("No-BayId1");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr1 bay_name  Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr1 bay_name: JSONException-1:"+e.
		 * getMessage());
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1BayId.getSelectionModel().select("Bay-Ex1");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr1 bay_name:  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu2_setting.has("bay_name")){
		 * selectedBayName = saved_ldu2_setting.getString("bay_name");
		 * ref_cmbBxQr2BayId.getSelectionModel().select(selectedBayName);
		 * selectedClusterName =
		 * (String)ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
		 * ref_cmbBxQr2PositionId.getItems().clear();
		 * ref_cmbBxQr2PositionId.getItems().addAll(getClusterBayNamePositionListMap().
		 * get(selectedClusterName + "_"+selectedBayName));
		 * } else {
		 * ref_cmbBxQr2BayId.getSelectionModel().select("No-BayId2");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr2 bay_name Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr2 bay_name: JSONException-1:"+e.
		 * getMessage());
		 * ref_cmbBxQr2BayId.getSelectionModel().select("Bay-Ex2");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr2 bay_name :  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		String selectedPositionNo = "";

		/*
		 * try {
		 * if(saved_ldu1_setting.has("position_no")){
		 * //ref_cmbBxQrCname1.setValue(saved_ldu1_setting.getString("c_name"));
		 * selectedPositionNo = saved_ldu1_setting.getString("position_no");
		 * ref_cmbBxQr1PositionId.getSelectionModel().select(selectedPositionNo);
		 * selectedClusterName =
		 * (String)ref_cmbBxQr1ClusterId.getSelectionModel().getSelectedItem();
		 * ref_cmbBxQr1PositionId.getItems().clear();
		 * ref_cmbBxQr1PositionId.getItems().addAll(getClusterBayNamePositionListMap().
		 * get(selectedClusterName + "_"+selectedBayName));
		 * 
		 * 
		 * } else {
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1PositionId.getSelectionModel().select("No-PositionId1");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr1 position_no  Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr1 position_no: JSONException-1:"+e.
		 * getMessage());
		 * //ref_cmbBxQrCname1.setValue("");
		 * ref_cmbBxQr1PositionId.getSelectionModel().select("PositionId-Ex1");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr1 position_no:  Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * try {
		 * if(saved_ldu2_setting.has("position_no")){
		 * selectedPositionNo = saved_ldu2_setting.getString("position_no");
		 * ref_cmbBxQr2PositionId.getSelectionModel().select(selectedPositionNo);
		 * 
		 * } else {
		 * ref_cmbBxQr2PositionId.getSelectionModel().select("No-PositionId2");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: position_no bay_name Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr2 position_no: JSONException-1:"+e.
		 * getMessage());
		 * ref_cmbBxQr2PositionId.getSelectionModel().select("PositionId-Ex2");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr2 position_no :  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu1_setting.has("c_name")){
		 * ref_txtQr1Cname.setText(saved_ldu1_setting.getString("c_name"));
		 * } else {
		 * ref_txtQr1Cname.setText("");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr Port name1 Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr_port_name1: JSONException-1:"+e.
		 * getMessage());
		 * ref_txtQr1Cname.setText("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr_port_name1:  Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * try {
		 * if(saved_ldu2_setting.has("c_name")){
		 * ref_txtQr2Cname.setText(saved_ldu2_setting.getString("c_name"));
		 * } else {
		 * ref_txtQr2Cname.setText("");
		 * ApplicationLauncher.logger.
		 * debug("load_saved_device_settings: qr Port name2 Selection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: qr_port_name2: JSONException-1:"+e.
		 * getMessage());
		 * ref_txtQr2Cname.setText("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: qr_port_name2:  Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu1_setting.has("port_name")){
		 * cmbBxQr1_PortSelection.setValue(saved_ldu1_setting.getString("port_name"));
		 * } else {
		 * cmbBxQr1_PortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU1_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-1:"+e.getMessage());
		 * cmbBxQr1_PortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU1_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu2_setting.has("port_name")){
		 * cmbBxQr2_PortSelection.setValue(saved_ldu2_setting.getString("port_name"));
		 * } else {
		 * cmbBxQr2_PortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU2_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-2:"+e.getMessage());
		 * cmbBxQr2_PortSelection.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU2_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu3_setting.has("port_name")){
		 * cmbBxLDU_PortSelection3.setValue(saved_ldu3_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection3.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU3_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-3:"+e.getMessage());
		 * cmbBxLDU_PortSelection3.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU3_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu4_setting.has("port_name")){
		 * cmbBxLDU_PortSelection4.setValue(saved_ldu4_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection4.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU4_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-4:"+e.getMessage());
		 * cmbBxLDU_PortSelection4.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU4_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu5_setting.has("port_name")){
		 * cmbBxLDU_PortSelection5.setValue(saved_ldu5_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection5.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU5_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-5:"+e.getMessage());
		 * cmbBxLDU_PortSelection5.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU5_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu6_setting.has("port_name")){
		 * cmbBxLDU_PortSelection6.setValue(saved_ldu6_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection6.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU6_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-6:"+e.getMessage());
		 * cmbBxLDU_PortSelection6.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU6_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu7_setting.has("port_name")){
		 * cmbBxLDU_PortSelection7.setValue(saved_ldu7_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection7.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU7_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-7:"+e.getMessage());
		 * cmbBxLDU_PortSelection7.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU7_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu8_setting.has("port_name")){
		 * cmbBxLDU_PortSelection8.setValue(saved_ldu8_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection8.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU8_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-8:"+e.getMessage());
		 * cmbBxLDU_PortSelection8.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU8_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu9_setting.has("port_name")){
		 * cmbBxLDU_PortSelection9.setValue(saved_ldu9_setting.getString("port_name"));
		 * } else {
		 * cmbBxLDU_PortSelection9.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU9_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-9:"+e.getMessage());
		 * cmbBxLDU_PortSelection9.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU9_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu10_setting.has("port_name")){
		 * cmbBxLDU_PortSelection10.setValue(saved_ldu10_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection10.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU10_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-10:"+e.getMessage());
		 * cmbBxLDU_PortSelection10.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU10_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu11_setting.has("port_name")){
		 * cmbBxLDU_PortSelection11.setValue(saved_ldu11_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection11.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU11_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-11:"+e.getMessage());
		 * cmbBxLDU_PortSelection11.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU11_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu12_setting.has("port_name")){
		 * cmbBxLDU_PortSelection12.setValue(saved_ldu12_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection12.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU12_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-12:"+e.getMessage());
		 * cmbBxLDU_PortSelection12.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU12_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu13_setting.has("port_name")){
		 * cmbBxLDU_PortSelection13.setValue(saved_ldu13_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection13.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU13_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-13:"+e.getMessage());
		 * cmbBxLDU_PortSelection13.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU13_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu14_setting.has("port_name")){
		 * cmbBxLDU_PortSelection14.setValue(saved_ldu14_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection14.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU14_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-14:"+e.getMessage());
		 * cmbBxLDU_PortSelection14.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU14_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu15_setting.has("port_name")){
		 * cmbBxLDU_PortSelection15.setValue(saved_ldu15_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection15.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU15_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-15:"+e.getMessage());
		 * cmbBxLDU_PortSelection15.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU15_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu16_setting.has("port_name")){
		 * cmbBxLDU_PortSelection16.setValue(saved_ldu16_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection16.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU16_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-16:"+e.getMessage());
		 * cmbBxLDU_PortSelection16.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU16_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu17_setting.has("port_name")){
		 * cmbBxLDU_PortSelection17.setValue(saved_ldu17_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection17.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU17_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-17:"+e.getMessage());
		 * cmbBxLDU_PortSelection17.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU17_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu18_setting.has("port_name")){
		 * cmbBxLDU_PortSelection18.setValue(saved_ldu18_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection18.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU18_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-18:"+e.getMessage());
		 * cmbBxLDU_PortSelection18.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU18_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu19_setting.has("port_name")){
		 * cmbBxLDU_PortSelection19.setValue(saved_ldu19_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection19.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU19_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-19:"+e.getMessage());
		 * cmbBxLDU_PortSelection19.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU19_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu20_setting.has("port_name")){
		 * cmbBxLDU_PortSelection20.setValue(saved_ldu20_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection20.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU20_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-20:"+e.getMessage());
		 * cmbBxLDU_PortSelection20.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU20_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * try {
		 * if(saved_ldu21_setting.has("port_name")){
		 * cmbBxLDU_PortSelection21.setValue(saved_ldu21_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection21.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU21_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-21:"+e.getMessage());
		 * cmbBxLDU_PortSelection21.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU21_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu22_setting.has("port_name")){
		 * cmbBxLDU_PortSelection22.setValue(saved_ldu22_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection22.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU22_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-22:"+e.getMessage());
		 * cmbBxLDU_PortSelection22.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU22_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu23_setting.has("port_name")){
		 * cmbBxLDU_PortSelection23.setValue(saved_ldu23_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection23.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU23_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-23:"+e.getMessage());
		 * cmbBxLDU_PortSelection23.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU23_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu24_setting.has("port_name")){
		 * cmbBxLDU_PortSelection24.setValue(saved_ldu24_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection24.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU24_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-24:"+e.getMessage());
		 * cmbBxLDU_PortSelection24.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU24_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu25_setting.has("port_name")){
		 * cmbBxLDU_PortSelection25.setValue(saved_ldu25_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection25.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU25_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-25:"+e.getMessage());
		 * cmbBxLDU_PortSelection25.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU25_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu26_setting.has("port_name")){
		 * cmbBxLDU_PortSelection26.setValue(saved_ldu26_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection26.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU26_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-26:"+e.getMessage());
		 * cmbBxLDU_PortSelection26.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU26_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu27_setting.has("port_name")){
		 * cmbBxLDU_PortSelection27.setValue(saved_ldu27_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection27.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU27_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-27:"+e.getMessage());
		 * cmbBxLDU_PortSelection27.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU27_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu28_setting.has("port_name")){
		 * cmbBxLDU_PortSelection28.setValue(saved_ldu28_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection28.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU28_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-28:"+e.getMessage());
		 * cmbBxLDU_PortSelection28.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU28_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu29_setting.has("port_name")){
		 * cmbBxLDU_PortSelection29.setValue(saved_ldu29_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection29.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU29_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-29:"+e.getMessage());
		 * cmbBxLDU_PortSelection29.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU29_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu30_setting.has("port_name")){
		 * cmbBxLDU_PortSelection30.setValue(saved_ldu30_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection30.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU30_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-30:"+e.getMessage());
		 * cmbBxLDU_PortSelection30.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU30_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * try {
		 * if(saved_ldu31_setting.has("port_name")){
		 * cmbBxLDU_PortSelection31.setValue(saved_ldu31_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection31.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU31_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-31:"+e.getMessage());
		 * cmbBxLDU_PortSelection31.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU31_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu32_setting.has("port_name")){
		 * cmbBxLDU_PortSelection32.setValue(saved_ldu32_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection32.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU32_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-32:"+e.getMessage());
		 * cmbBxLDU_PortSelection32.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU32_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu33_setting.has("port_name")){
		 * cmbBxLDU_PortSelection33.setValue(saved_ldu33_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection33.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU33_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-33:"+e.getMessage());
		 * cmbBxLDU_PortSelection33.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU33_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu34_setting.has("port_name")){
		 * cmbBxLDU_PortSelection34.setValue(saved_ldu34_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection34.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU34_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-34:"+e.getMessage());
		 * cmbBxLDU_PortSelection34.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU34_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu35_setting.has("port_name")){
		 * cmbBxLDU_PortSelection35.setValue(saved_ldu35_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection35.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU35_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-35:"+e.getMessage());
		 * cmbBxLDU_PortSelection35.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU35_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu36_setting.has("port_name")){
		 * cmbBxLDU_PortSelection36.setValue(saved_ldu36_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection36.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU36_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-36:"+e.getMessage());
		 * cmbBxLDU_PortSelection36.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU36_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu37_setting.has("port_name")){
		 * cmbBxLDU_PortSelection37.setValue(saved_ldu37_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection37.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU37_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-37:"+e.getMessage());
		 * cmbBxLDU_PortSelection37.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU37_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu38_setting.has("port_name")){
		 * cmbBxLDU_PortSelection38.setValue(saved_ldu38_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection38.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU38_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-38:"+e.getMessage());
		 * cmbBxLDU_PortSelection38.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU38_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu39_setting.has("port_name")){
		 * cmbBxLDU_PortSelection39.setValue(saved_ldu39_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection39.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU39_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-39:"+e.getMessage());
		 * cmbBxLDU_PortSelection39.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU39_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu40_setting.has("port_name")){
		 * cmbBxLDU_PortSelection40.setValue(saved_ldu40_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection40.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU40_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-40:"+e.getMessage());
		 * cmbBxLDU_PortSelection40.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU40_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * 
		 * try {
		 * if(saved_ldu41_setting.has("port_name")){
		 * cmbBxLDU_PortSelection41.setValue(saved_ldu41_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection41.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU41_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-41:"+e.getMessage());
		 * cmbBxLDU_PortSelection41.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU41_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu42_setting.has("port_name")){
		 * cmbBxLDU_PortSelection42.setValue(saved_ldu42_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection42.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU42_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-42:"+e.getMessage());
		 * cmbBxLDU_PortSelection42.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU42_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu43_setting.has("port_name")){
		 * cmbBxLDU_PortSelection43.setValue(saved_ldu43_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection43.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU43_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-43:"+e.getMessage());
		 * cmbBxLDU_PortSelection43.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU43_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu44_setting.has("port_name")){
		 * cmbBxLDU_PortSelection44.setValue(saved_ldu44_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection44.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU44_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-44:"+e.getMessage());
		 * cmbBxLDU_PortSelection44.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU44_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu45_setting.has("port_name")){
		 * cmbBxLDU_PortSelection45.setValue(saved_ldu45_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection45.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU45_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-45:"+e.getMessage());
		 * cmbBxLDU_PortSelection45.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU45_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu46_setting.has("port_name")){
		 * cmbBxLDU_PortSelection46.setValue(saved_ldu46_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection46.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU46_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-46:"+e.getMessage());
		 * cmbBxLDU_PortSelection46.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU46_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu47_setting.has("port_name")){
		 * cmbBxLDU_PortSelection47.setValue(saved_ldu47_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection47.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU47_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-47:"+e.getMessage());
		 * cmbBxLDU_PortSelection47.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU47_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu48_setting.has("port_name")){
		 * cmbBxLDU_PortSelection48.setValue(saved_ldu48_setting.getString("port_name"))
		 * ;
		 * } else {
		 * cmbBxLDU_PortSelection48.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU48_PortSelection: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * 
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException5-48:"+e.getMessage());
		 * cmbBxLDU_PortSelection48.setValue("");
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU48_PortSelection: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * }
		 */

		/*
		 * try {
		 * if(saved_ldu1_setting.has("baud_rate")){
		 * cmbBxQr1_BaudRate.setValue(Integer.parseInt(saved_ldu1_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxQr1_BaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from DB");
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-1:"+e.getMessage());
		 * cmbBxQr1_BaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * 
		 * 
		 * try {
		 * if(saved_ldu2_setting.has("baud_rate")){
		 * cmbBxQr2_BaudRate.setValue(Integer.parseInt(saved_ldu2_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxQr2_BaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU2_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-2:"+e.getMessage());
		 * cmbBxQr2_BaudRate.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU2_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 */
		/*
		 * try {
		 * if(saved_ldu3_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate3.setValue(Integer.parseInt(saved_ldu3_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate3.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU3_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-3:"+e.getMessage());
		 * cmbBxLDU_BaudRate3.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU3_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu4_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate4.setValue(Integer.parseInt(saved_ldu4_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate4.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU4_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-4:"+e.getMessage());
		 * cmbBxLDU_BaudRate4.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU4_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu5_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate5.setValue(Integer.parseInt(saved_ldu5_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate5.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU5_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-5:"+e.getMessage());
		 * cmbBxLDU_BaudRate5.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU5_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu6_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate6.setValue(Integer.parseInt(saved_ldu6_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate6.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU6_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-6:"+e.getMessage());
		 * cmbBxLDU_BaudRate6.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU6_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu7_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate7.setValue(Integer.parseInt(saved_ldu7_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate7.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU7_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-7:"+e.getMessage());
		 * cmbBxLDU_BaudRate7.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU7_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu8_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate8.setValue(Integer.parseInt(saved_ldu8_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate8.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU8_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-8:"+e.getMessage());
		 * cmbBxLDU_BaudRate8.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU8_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu9_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate9.setValue(Integer.parseInt(saved_ldu9_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate9.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU9_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-9:"+e.getMessage());
		 * cmbBxLDU_BaudRate9.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU9_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu10_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate10.setValue(Integer.parseInt(saved_ldu10_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate10.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU10_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-10:"+e.getMessage());
		 * cmbBxLDU_BaudRate10.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU10_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu11_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate11.setValue(Integer.parseInt(saved_ldu11_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate11.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU11_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-11:"+e.getMessage());
		 * cmbBxLDU_BaudRate11.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU11_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu12_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate12.setValue(Integer.parseInt(saved_ldu12_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate12.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU12_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-12:"+e.getMessage());
		 * cmbBxLDU_BaudRate12.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU12_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu13_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate13.setValue(Integer.parseInt(saved_ldu13_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate13.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU13_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-13:"+e.getMessage());
		 * cmbBxLDU_BaudRate13.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU13_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu14_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate14.setValue(Integer.parseInt(saved_ldu14_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate14.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU14_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-14:"+e.getMessage());
		 * cmbBxLDU_BaudRate14.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU14_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu15_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate15.setValue(Integer.parseInt(saved_ldu15_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate15.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU15_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-15:"+e.getMessage());
		 * cmbBxLDU_BaudRate15.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU15_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu16_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate16.setValue(Integer.parseInt(saved_ldu16_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate16.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU16_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-16:"+e.getMessage());
		 * cmbBxLDU_BaudRate16.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU16_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu17_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate17.setValue(Integer.parseInt(saved_ldu17_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate17.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU17_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-17:"+e.getMessage());
		 * cmbBxLDU_BaudRate17.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU17_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu18_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate18.setValue(Integer.parseInt(saved_ldu18_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate18.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU18_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-18:"+e.getMessage());
		 * cmbBxLDU_BaudRate18.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU18_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu19_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate19.setValue(Integer.parseInt(saved_ldu19_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate19.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU19_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-19:"+e.getMessage());
		 * cmbBxLDU_BaudRate19.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU19_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu20_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate20.setValue(Integer.parseInt(saved_ldu20_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate20.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU20_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-20:"+e.getMessage());
		 * cmbBxLDU_BaudRate20.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU20_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu21_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate21.setValue(Integer.parseInt(saved_ldu21_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate21.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU21_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-21:"+e.getMessage());
		 * cmbBxLDU_BaudRate21.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU21_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu22_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate22.setValue(Integer.parseInt(saved_ldu22_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate22.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU22_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-22:"+e.getMessage());
		 * cmbBxLDU_BaudRate22.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU22_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu23_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate23.setValue(Integer.parseInt(saved_ldu23_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate23.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU23_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-23:"+e.getMessage());
		 * cmbBxLDU_BaudRate23.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU23_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu24_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate24.setValue(Integer.parseInt(saved_ldu24_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate24.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU24_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-24:"+e.getMessage());
		 * cmbBxLDU_BaudRate24.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU24_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu25_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate25.setValue(Integer.parseInt(saved_ldu25_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate25.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU25_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-25:"+e.getMessage());
		 * cmbBxLDU_BaudRate25.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU25_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu26_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate26.setValue(Integer.parseInt(saved_ldu26_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate26.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU26_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-26:"+e.getMessage());
		 * cmbBxLDU_BaudRate26.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU26_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu27_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate27.setValue(Integer.parseInt(saved_ldu27_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate27.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU27_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-27:"+e.getMessage());
		 * cmbBxLDU_BaudRate27.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU27_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu28_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate28.setValue(Integer.parseInt(saved_ldu28_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate28.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU28_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-28:"+e.getMessage());
		 * cmbBxLDU_BaudRate28.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU28_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu29_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate29.setValue(Integer.parseInt(saved_ldu29_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate29.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU29_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-29:"+e.getMessage());
		 * cmbBxLDU_BaudRate29.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU29_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu30_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate30.setValue(Integer.parseInt(saved_ldu30_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate30.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU30_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-30:"+e.getMessage());
		 * cmbBxLDU_BaudRate30.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU30_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu31_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate31.setValue(Integer.parseInt(saved_ldu31_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate31.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU31_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-31:"+e.getMessage());
		 * cmbBxLDU_BaudRate31.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU31_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu32_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate32.setValue(Integer.parseInt(saved_ldu32_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate32.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU32_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-32:"+e.getMessage());
		 * cmbBxLDU_BaudRate32.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU32_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu33_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate33.setValue(Integer.parseInt(saved_ldu33_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate33.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU33_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-33:"+e.getMessage());
		 * cmbBxLDU_BaudRate33.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU33_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu34_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate34.setValue(Integer.parseInt(saved_ldu34_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate34.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU34_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-34:"+e.getMessage());
		 * cmbBxLDU_BaudRate34.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU34_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu35_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate35.setValue(Integer.parseInt(saved_ldu35_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate35.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU35_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-35:"+e.getMessage());
		 * cmbBxLDU_BaudRate35.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU35_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu36_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate36.setValue(Integer.parseInt(saved_ldu36_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate36.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU36_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-36:"+e.getMessage());
		 * cmbBxLDU_BaudRate36.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU36_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu37_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate37.setValue(Integer.parseInt(saved_ldu37_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate37.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU37_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-37:"+e.getMessage());
		 * cmbBxLDU_BaudRate37.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU37_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu38_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate38.setValue(Integer.parseInt(saved_ldu38_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate38.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU38_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-38:"+e.getMessage());
		 * cmbBxLDU_BaudRate38.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU38_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu39_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate39.setValue(Integer.parseInt(saved_ldu39_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate39.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU39_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-39:"+e.getMessage());
		 * cmbBxLDU_BaudRate39.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU39_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu40_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate40.setValue(Integer.parseInt(saved_ldu40_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate40.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU40_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-40:"+e.getMessage());
		 * cmbBxLDU_BaudRate40.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU40_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 */
		/*
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * try {
		 * if(saved_ldu41_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate41.setValue(Integer.parseInt(saved_ldu41_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate41.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU41_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-41:"+e.getMessage());
		 * cmbBxLDU_BaudRate41.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU41_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu42_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate42.setValue(Integer.parseInt(saved_ldu42_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate42.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU42_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-42:"+e.getMessage());
		 * cmbBxLDU_BaudRate42.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU42_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu43_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate43.setValue(Integer.parseInt(saved_ldu43_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate43.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU43_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-43:"+e.getMessage());
		 * cmbBxLDU_BaudRate43.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU43_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu44_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate44.setValue(Integer.parseInt(saved_ldu44_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate44.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU44_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-44:"+e.getMessage());
		 * cmbBxLDU_BaudRate44.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU44_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu45_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate45.setValue(Integer.parseInt(saved_ldu45_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate45.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU45_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-45:"+e.getMessage());
		 * cmbBxLDU_BaudRate45.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU45_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu46_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate46.setValue(Integer.parseInt(saved_ldu46_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate46.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU46_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-46:"+e.getMessage());
		 * cmbBxLDU_BaudRate46.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU46_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu47_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate47.setValue(Integer.parseInt(saved_ldu47_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate47.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU47_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-47:"+e.getMessage());
		 * cmbBxLDU_BaudRate47.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU47_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * 
		 * try {
		 * if(saved_ldu48_setting.has("baud_rate")){
		 * cmbBxLDU_BaudRate48.setValue(Integer.parseInt(saved_ldu48_setting.getString(
		 * "baud_rate")));
		 * } else {
		 * cmbBxLDU_BaudRate48.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU48_BaudRate: Data not retrieved from DB"
		 * );
		 * 
		 * }
		 * } catch (JSONException e) {
		 * 
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("load_saved_device_settings: JSONException6-48:"+e.getMessage());
		 * cmbBxLDU_BaudRate48.setValue(9600);
		 * ApplicationLauncher.logger.
		 * info("load_saved_device_settings: LDU48_BaudRate: Data not retrieved from database"
		 * );
		 * 
		 * }
		 * }
		 */
		// }

	}

	public static JSONObject get_device_settings(String InputSourceType) {

		JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
		return saved_pwr_setting;
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
		cmbBxMegaOhmPm1_BaudRate.getItems().clear();
		cmbBxMegaOhmPm1_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		cmbBxMegaOhmPm1_BaudRate.getSelectionModel().select(ConstantMegaOhmPm.MEGA_OHM_PM_DEFAULT_BAUD_RATE);

		/*
		 * cmbBxQr2_BaudRate.getItems().clear();
		 * cmbBxQr2_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxQr2_BaudRate.getSelectionModel().select(ConstantQrScanner.
		 * QR_SCANNER_DEFAULT_BAUD_RATE);
		 */
		/*
		 * cmbBxLDU_BaudRate3.getItems().clear();
		 * cmbBxLDU_BaudRate3.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate3.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * 
		 * cmbBxLDU_BaudRate4.getItems().clear();
		 * cmbBxLDU_BaudRate4.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate4.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * 
		 * cmbBxLDU_BaudRate5.getItems().clear();
		 * cmbBxLDU_BaudRate5.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate5.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * 
		 * cmbBxLDU_BaudRate6.getItems().clear();
		 * cmbBxLDU_BaudRate6.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate6.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * 
		 * cmbBxLDU_BaudRate7.getItems().clear();
		 * cmbBxLDU_BaudRate7.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate7.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 */

		/*
		 * cmbBxLDU_BaudRate8.getItems().clear();
		 * cmbBxLDU_BaudRate9.getItems().clear();
		 * cmbBxLDU_BaudRate10.getItems().clear();
		 * cmbBxLDU_BaudRate11.getItems().clear();
		 * cmbBxLDU_BaudRate12.getItems().clear();
		 * cmbBxLDU_BaudRate13.getItems().clear();
		 * cmbBxLDU_BaudRate14.getItems().clear();
		 * cmbBxLDU_BaudRate15.getItems().clear();
		 * cmbBxLDU_BaudRate16.getItems().clear();
		 * cmbBxLDU_BaudRate17.getItems().clear();
		 * cmbBxLDU_BaudRate18.getItems().clear();
		 * cmbBxLDU_BaudRate19.getItems().clear();
		 * cmbBxLDU_BaudRate20.getItems().clear();
		 * cmbBxLDU_BaudRate21.getItems().clear();
		 * cmbBxLDU_BaudRate22.getItems().clear();
		 * cmbBxLDU_BaudRate23.getItems().clear();
		 * cmbBxLDU_BaudRate24.getItems().clear();
		 * cmbBxLDU_BaudRate25.getItems().clear();
		 * cmbBxLDU_BaudRate26.getItems().clear();
		 * cmbBxLDU_BaudRate27.getItems().clear();
		 * cmbBxLDU_BaudRate28.getItems().clear();
		 * cmbBxLDU_BaudRate29.getItems().clear();
		 * cmbBxLDU_BaudRate30.getItems().clear();
		 * cmbBxLDU_BaudRate31.getItems().clear();
		 * cmbBxLDU_BaudRate32.getItems().clear();
		 * cmbBxLDU_BaudRate33.getItems().clear();
		 * cmbBxLDU_BaudRate34.getItems().clear();
		 * cmbBxLDU_BaudRate35.getItems().clear();
		 * cmbBxLDU_BaudRate36.getItems().clear();
		 * cmbBxLDU_BaudRate37.getItems().clear();
		 * cmbBxLDU_BaudRate38.getItems().clear();
		 * cmbBxLDU_BaudRate39.getItems().clear();
		 * cmbBxLDU_BaudRate40.getItems().clear();
		 * 
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * cmbBxLDU_BaudRate41.getItems().clear();
		 * cmbBxLDU_BaudRate42.getItems().clear();
		 * cmbBxLDU_BaudRate43.getItems().clear();
		 * cmbBxLDU_BaudRate44.getItems().clear();
		 * cmbBxLDU_BaudRate45.getItems().clear();
		 * cmbBxLDU_BaudRate46.getItems().clear();
		 * cmbBxLDU_BaudRate47.getItems().clear();
		 * cmbBxLDU_BaudRate48.getItems().clear();
		 * }
		 */

		/*
		 * cmbBxLDU_BaudRate8.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate9.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate10.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate11.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate12.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate13.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate14.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate15.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate16.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate17.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate18.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate19.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate20.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate21.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate22.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate23.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate24.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate25.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate26.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate27.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate28.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate29.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate30.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate31.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate32.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate33.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate34.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate35.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate36.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate37.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate38.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate39.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate40.getItems().addAll(ConstantApp.BaudRateConstant);
		 * 
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_BaudRate41.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate42.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate43.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate44.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate45.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate46.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate47.getItems().addAll(ConstantApp.BaudRateConstant);
		 * cmbBxLDU_BaudRate48.getItems().addAll(ConstantApp.BaudRateConstant);
		 * }
		 */
		/*
		 * cmbBxLDU_BaudRate8.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate9.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate10.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate11.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate12.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate13.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate14.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate15.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate16.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate17.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate18.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate19.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate20.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate21.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate22.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate23.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate24.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate25.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate26.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate27.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate28.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate29.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate30.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate31.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate32.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate33.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate34.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate35.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate36.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate37.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate38.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate39.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate40.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_BaudRate41.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate42.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate43.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate44.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate45.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate46.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate47.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * cmbBxLDU_BaudRate48.getSelectionModel().select(ConstantCcubeLDU.
		 * LDU_DefaultBaudRate);
		 * }
		 */

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

	/*
	 * public void updatePowerSourceModel() {
	 * ArrayList<String> ModelList = new ArrayList<String>();
	 * String ModelName = "";
	 * ConstantApp MyPropertyObj= new ConstantApp();
	 * ModelName = ConstantConfig.POWERSRC;
	 * ModelList.add(ModelName);
	 * cmbBxPowerSource_ModelName.getItems().clear();
	 * cmbBxPowerSource_ModelName.getItems().addAll(ModelList);
	 * cmbBxPowerSource_ModelName.getSelectionModel().select(0);
	 * }
	 * 
	 * public void updateReferenceMeterModel() {
	 * ArrayList<String> ModelList = new ArrayList<String>();
	 * String ModelName = "";
	 * ConstantApp MyPropertyObj= new ConstantApp();
	 * ModelName = ConstantConfig.REFSTD;
	 * ModelList.add(ModelName);
	 * cmbBxReferanceStd_ModelName.getItems().clear();
	 * cmbBxReferanceStd_ModelName.getItems().addAll(ModelList);
	 * cmbBxReferanceStd_ModelName.getSelectionModel().select(0);
	 * }
	 */

	public void updateLDUModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj = new ConstantApp();
		ModelName = ConstantMegaOhmPm.MEGA_OHM_PM_MODEL;
		ModelList.add(ModelName);

		// ref_cmbBxQrScannerName.get

		cmbBxMegaOhmPm1_ModelName.getItems().clear();
		cmbBxMegaOhmPm1_ModelName.getItems().addAll(ModelList);
		cmbBxMegaOhmPm1_ModelName.getSelectionModel().select(0);

		/*
		 * cmbBxQr2_ModelName.getItems().clear();
		 * cmbBxQr2_ModelName.getItems().addAll(ModelList);
		 * cmbBxQr2_ModelName.getSelectionModel().select(0);
		 */
		/*
		 * cmbBxLDU_ModelName3.getItems().clear();
		 * cmbBxLDU_ModelName3.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName3.getSelectionModel().select(0);
		 * 
		 * cmbBxLDU_ModelName4.getItems().clear();
		 * cmbBxLDU_ModelName4.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName4.getSelectionModel().select(0);
		 * 
		 * cmbBxLDU_ModelName5.getItems().clear();
		 * cmbBxLDU_ModelName5.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName5.getSelectionModel().select(0);
		 * 
		 * cmbBxLDU_ModelName6.getItems().clear();
		 * cmbBxLDU_ModelName6.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName6.getSelectionModel().select(0);
		 * 
		 * cmbBxLDU_ModelName7.getItems().clear();
		 * cmbBxLDU_ModelName7.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName7.getSelectionModel().select(0);
		 * 
		 * 
		 * cmbBxLDU_ModelName8.getItems().clear();
		 * cmbBxLDU_ModelName9.getItems().clear();
		 * cmbBxLDU_ModelName10.getItems().clear();
		 * cmbBxLDU_ModelName11.getItems().clear();
		 * cmbBxLDU_ModelName12.getItems().clear();
		 * cmbBxLDU_ModelName13.getItems().clear();
		 * cmbBxLDU_ModelName14.getItems().clear();
		 * cmbBxLDU_ModelName15.getItems().clear();
		 * cmbBxLDU_ModelName16.getItems().clear();
		 * cmbBxLDU_ModelName17.getItems().clear();
		 * cmbBxLDU_ModelName18.getItems().clear();
		 * cmbBxLDU_ModelName19.getItems().clear();
		 * cmbBxLDU_ModelName20.getItems().clear();
		 * cmbBxLDU_ModelName21.getItems().clear();
		 * cmbBxLDU_ModelName22.getItems().clear();
		 * cmbBxLDU_ModelName23.getItems().clear();
		 * cmbBxLDU_ModelName24.getItems().clear();
		 * cmbBxLDU_ModelName25.getItems().clear();
		 * cmbBxLDU_ModelName26.getItems().clear();
		 * cmbBxLDU_ModelName27.getItems().clear();
		 * cmbBxLDU_ModelName28.getItems().clear();
		 * cmbBxLDU_ModelName29.getItems().clear();
		 * cmbBxLDU_ModelName30.getItems().clear();
		 * cmbBxLDU_ModelName31.getItems().clear();
		 * cmbBxLDU_ModelName32.getItems().clear();
		 * cmbBxLDU_ModelName33.getItems().clear();
		 * cmbBxLDU_ModelName34.getItems().clear();
		 * cmbBxLDU_ModelName35.getItems().clear();
		 * cmbBxLDU_ModelName36.getItems().clear();
		 * cmbBxLDU_ModelName37.getItems().clear();
		 * cmbBxLDU_ModelName38.getItems().clear();
		 * cmbBxLDU_ModelName39.getItems().clear();
		 * cmbBxLDU_ModelName40.getItems().clear();
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_ModelName41.getItems().clear();
		 * cmbBxLDU_ModelName42.getItems().clear();
		 * cmbBxLDU_ModelName43.getItems().clear();
		 * cmbBxLDU_ModelName44.getItems().clear();
		 * cmbBxLDU_ModelName45.getItems().clear();
		 * cmbBxLDU_ModelName46.getItems().clear();
		 * cmbBxLDU_ModelName47.getItems().clear();
		 * cmbBxLDU_ModelName48.getItems().clear();
		 * }
		 */

		/*
		 * cmbBxLDU_ModelName8.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName9.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName10.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName11.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName12.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName13.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName14.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName15.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName16.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName17.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName18.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName19.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName20.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName21.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName22.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName23.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName24.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName25.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName26.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName27.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName28.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName29.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName30.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName31.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName32.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName33.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName34.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName35.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName36.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName37.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName38.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName39.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName40.getItems().addAll(ModelList);
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_ModelName41.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName42.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName43.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName44.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName45.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName46.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName47.getItems().addAll(ModelList);
		 * cmbBxLDU_ModelName48.getItems().addAll(ModelList);
		 * }
		 * 
		 * cmbBxLDU_ModelName8.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName9.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName10.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName11.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName12.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName13.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName14.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName15.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName16.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName17.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName18.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName19.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName20.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName21.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName22.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName23.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName24.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName25.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName26.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName27.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName28.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName29.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName30.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName31.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName32.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName33.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName34.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName35.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName36.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName37.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName38.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName39.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName40.getSelectionModel().select(0);
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_ModelName41.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName42.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName43.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName44.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName45.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName46.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName47.getSelectionModel().select(0);
		 * cmbBxLDU_ModelName48.getSelectionModel().select(0);
		 * }
		 */

	}

	public void scanSerialPortAndUpdateDisplay() {

		/*
		 * cmbBxPowerSrcPortSelection.getItems().clear();
		 * cmbBxRefStdPortSelection.getItems().clear();
		 */
		cmbBxMegaOhmPm1_PortSelection.getItems().clear();
		// cmbBxMegaOhmPm2_PortSelection.getItems().clear();
		/*
		 * cmbBxLDU_PortSelection3.getItems().clear();
		 * cmbBxLDU_PortSelection4.getItems().clear();
		 * cmbBxLDU_PortSelection5.getItems().clear();
		 * cmbBxLDU_PortSelection6.getItems().clear();
		 * cmbBxLDU_PortSelection7.getItems().clear();
		 * cmbBxLDU_PortSelection8.getItems().clear();
		 * cmbBxLDU_PortSelection9.getItems().clear();
		 * cmbBxLDU_PortSelection10.getItems().clear();
		 * 
		 * cmbBxLDU_PortSelection11.getItems().clear();
		 * cmbBxLDU_PortSelection12.getItems().clear();
		 * cmbBxLDU_PortSelection13.getItems().clear();
		 * cmbBxLDU_PortSelection14.getItems().clear();
		 * cmbBxLDU_PortSelection15.getItems().clear();
		 * cmbBxLDU_PortSelection16.getItems().clear();
		 * cmbBxLDU_PortSelection17.getItems().clear();
		 * cmbBxLDU_PortSelection18.getItems().clear();
		 * cmbBxLDU_PortSelection19.getItems().clear();
		 * cmbBxLDU_PortSelection20.getItems().clear();
		 * 
		 * cmbBxLDU_PortSelection21.getItems().clear();
		 * cmbBxLDU_PortSelection22.getItems().clear();
		 * cmbBxLDU_PortSelection23.getItems().clear();
		 * cmbBxLDU_PortSelection24.getItems().clear();
		 * cmbBxLDU_PortSelection25.getItems().clear();
		 * cmbBxLDU_PortSelection26.getItems().clear();
		 * cmbBxLDU_PortSelection27.getItems().clear();
		 * cmbBxLDU_PortSelection28.getItems().clear();
		 * cmbBxLDU_PortSelection29.getItems().clear();
		 * cmbBxLDU_PortSelection30.getItems().clear();
		 * 
		 * cmbBxLDU_PortSelection31.getItems().clear();
		 * cmbBxLDU_PortSelection32.getItems().clear();
		 * cmbBxLDU_PortSelection33.getItems().clear();
		 * cmbBxLDU_PortSelection34.getItems().clear();
		 * cmbBxLDU_PortSelection35.getItems().clear();
		 * cmbBxLDU_PortSelection36.getItems().clear();
		 * cmbBxLDU_PortSelection37.getItems().clear();
		 * cmbBxLDU_PortSelection38.getItems().clear();
		 * cmbBxLDU_PortSelection39.getItems().clear();
		 * cmbBxLDU_PortSelection40.getItems().clear();
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * cmbBxLDU_PortSelection41.getItems().clear();
		 * cmbBxLDU_PortSelection42.getItems().clear();
		 * cmbBxLDU_PortSelection43.getItems().clear();
		 * cmbBxLDU_PortSelection44.getItems().clear();
		 * cmbBxLDU_PortSelection45.getItems().clear();
		 * cmbBxLDU_PortSelection46.getItems().clear();
		 * cmbBxLDU_PortSelection47.getItems().clear();
		 * cmbBxLDU_PortSelection48.getItems().clear();
		 * }
		 */

		Enumeration ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				/*
				 * cmbBxPowerSrcPortSelection.getItems().add(curPort.getName());
				 * cmbBxRefStdPortSelection.getItems().add(curPort.getName());
				 */
				cmbBxMegaOhmPm1_PortSelection.getItems().add(curPort.getName());
				// cmbBxMegaOhmPm2_PortSelection.getItems().add(curPort.getName());
				/*
				 * cmbBxLDU_PortSelection3.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection4.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection5.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection6.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection7.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection8.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection9.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection10.getItems().add(curPort.getName());
				 * 
				 * cmbBxLDU_PortSelection11.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection12.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection13.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection14.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection15.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection16.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection17.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection18.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection19.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection20.getItems().add(curPort.getName());
				 * 
				 * cmbBxLDU_PortSelection21.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection22.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection23.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection24.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection25.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection26.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection27.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection28.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection29.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection30.getItems().add(curPort.getName());
				 * 
				 * cmbBxLDU_PortSelection31.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection32.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection33.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection34.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection35.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection36.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection37.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection38.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection39.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection40.getItems().add(curPort.getName());
				 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
				 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
				 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
				 * 
				 * cmbBxLDU_PortSelection41.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection42.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection43.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection44.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection45.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection46.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection47.getItems().add(curPort.getName());
				 * cmbBxLDU_PortSelection48.getItems().add(curPort.getName());
				 * }
				 */
			}
		}

		/*
		 * try {
		 * cmbBxPowerSrcPortSelection.getSelectionModel().select(0);
		 * } catch(Exception e) {
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception1:"+e.getMessage());
		 * }
		 * try {
		 * cmbBxRefStdPortSelection.getSelectionModel().select(0);
		 * } catch(Exception e) {
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception2:"+e.getMessage());
		 * }
		 */

		try {
			cmbBxMegaOhmPm1_PortSelection.getSelectionModel().select(0);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:" + e.getMessage());
		}

		/*
		 * try{
		 * cmbBxMegaOhmPm2_PortSelection.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-2:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection3.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-3:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection4.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-4:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection5.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-5:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection6.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-6:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection7.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-7:"+e.getMessage());
		 * }
		 * 
		 * 
		 * 
		 * try{
		 * cmbBxLDU_PortSelection8.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-8:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection9.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-9:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection10.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-10:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection11.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-11:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection12.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-12:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection13.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-13:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection14.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-14:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection15.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-15:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection16.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-16:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection17.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-17:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection18.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-18:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection19.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-19:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection20.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-20:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection21.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-21:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection22.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-22:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection23.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-23:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection24.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-24:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection25.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-25:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection26.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-26:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection27.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-27:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection28.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-28:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection29.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-29:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection30.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-30:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection31.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-31:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection32.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-32:"+e.getMessage());
		 * }
		 * 
		 * try{
		 * cmbBxLDU_PortSelection33.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-33:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection34.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-34:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection35.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-35:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection36.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-36:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection37.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-37:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection38.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-38:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection39.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-39:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection40.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-40:"+e.getMessage());
		 * }
		 * 
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * try{
		 * cmbBxLDU_PortSelection41.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-41:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection42.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-42:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection43.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-43:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection44.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-44:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection45.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-45:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection46.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-46:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection47.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-47:"+e.getMessage());
		 * }
		 * try{
		 * cmbBxLDU_PortSelection48.getSelectionModel().select(0);
		 * } catch(Exception e){
		 * e.printStackTrace();
		 * ApplicationLauncher.logger.
		 * error("scanSerialPortAndUpdateDisplay: Exception3-48:"+e.getMessage());
		 * }
		 * }
		 */

	}

	public void SaveOnClick() {
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;
		/*
		 * String pwr_model_name =
		 * cmbBxPowerSource_ModelName.getSelectionModel().getSelectedItem();
		 * String pwr_port_name =
		 * cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
		 * String pwr_baud_rate =
		 * cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
		 * String ref_type = ConstantApp.SOURCE_TYPE_REF_STD;
		 * String ref_model_name =
		 * cmbBxReferanceStd_ModelName.getSelectionModel().getSelectedItem();
		 * String ref_port_name =
		 * cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
		 * String ref_baud_rate =
		 * cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
		 */
		String device1_type_key = ConstantMegaOhmPm.MEGA_OHM_PM_TYPE_EIC_1;
		// String device2_type = ConstantQrScanner.QR_SCANNER_TYPE_NEWLAND_2;
		/*
		 * String ldu3_type = ConstantApp.SOURCE_TYPE_LDU3;
		 * String ldu4_type = ConstantApp.SOURCE_TYPE_LDU4;
		 * String ldu5_type = ConstantApp.SOURCE_TYPE_LDU5;
		 * String ldu6_type = ConstantApp.SOURCE_TYPE_LDU6;
		 * String ldu7_type = ConstantApp.SOURCE_TYPE_LDU7;
		 * 
		 * String ldu8_type = ConstantApp.SOURCE_TYPE_LDU8;
		 * String ldu9_type = ConstantApp.SOURCE_TYPE_LDU9;
		 * String ldu10_type = ConstantApp.SOURCE_TYPE_LDU10;
		 * String ldu11_type = ConstantApp.SOURCE_TYPE_LDU11;
		 * String ldu12_type = ConstantApp.SOURCE_TYPE_LDU12;
		 * String ldu13_type = ConstantApp.SOURCE_TYPE_LDU13;
		 * String ldu14_type = ConstantApp.SOURCE_TYPE_LDU14;
		 * String ldu15_type = ConstantApp.SOURCE_TYPE_LDU15;
		 * String ldu16_type = ConstantApp.SOURCE_TYPE_LDU16;
		 * String ldu17_type = ConstantApp.SOURCE_TYPE_LDU17;
		 * String ldu18_type = ConstantApp.SOURCE_TYPE_LDU18;
		 * String ldu19_type = ConstantApp.SOURCE_TYPE_LDU19;
		 * String ldu20_type = ConstantApp.SOURCE_TYPE_LDU20;
		 * String ldu21_type = ConstantApp.SOURCE_TYPE_LDU21;
		 * String ldu22_type = ConstantApp.SOURCE_TYPE_LDU22;
		 * String ldu23_type = ConstantApp.SOURCE_TYPE_LDU23;
		 * String ldu24_type = ConstantApp.SOURCE_TYPE_LDU24;
		 * String ldu25_type = ConstantApp.SOURCE_TYPE_LDU25;
		 * String ldu26_type = ConstantApp.SOURCE_TYPE_LDU26;
		 * String ldu27_type = ConstantApp.SOURCE_TYPE_LDU27;
		 * String ldu28_type = ConstantApp.SOURCE_TYPE_LDU28;
		 * String ldu29_type = ConstantApp.SOURCE_TYPE_LDU29;
		 * String ldu30_type = ConstantApp.SOURCE_TYPE_LDU30;
		 * String ldu31_type = ConstantApp.SOURCE_TYPE_LDU31;
		 * String ldu32_type = ConstantApp.SOURCE_TYPE_LDU32;
		 * String ldu33_type = ConstantApp.SOURCE_TYPE_LDU33;
		 * String ldu34_type = ConstantApp.SOURCE_TYPE_LDU34;
		 * String ldu35_type = ConstantApp.SOURCE_TYPE_LDU35;
		 * String ldu36_type = ConstantApp.SOURCE_TYPE_LDU36;
		 * String ldu37_type = ConstantApp.SOURCE_TYPE_LDU37;
		 * String ldu38_type = ConstantApp.SOURCE_TYPE_LDU38;
		 * String ldu39_type = ConstantApp.SOURCE_TYPE_LDU39;
		 * String ldu40_type = ConstantApp.SOURCE_TYPE_LDU40;
		 * String ldu41_type = ConstantApp.SOURCE_TYPE_LDU41;
		 * String ldu42_type = ConstantApp.SOURCE_TYPE_LDU42;
		 * String ldu43_type = ConstantApp.SOURCE_TYPE_LDU43;
		 * String ldu44_type = ConstantApp.SOURCE_TYPE_LDU44;
		 * String ldu45_type = ConstantApp.SOURCE_TYPE_LDU45;
		 * String ldu46_type = ConstantApp.SOURCE_TYPE_LDU46;
		 * String ldu47_type = ConstantApp.SOURCE_TYPE_LDU47;
		 * String ldu48_type = ConstantApp.SOURCE_TYPE_LDU48;
		 */

		String device1_model_name = cmbBxMegaOhmPm1_ModelName.getSelectionModel().getSelectedItem();
		// String device2_model_name =
		// cmbBxMegaOhmPm2_ModelName.getSelectionModel().getSelectedItem();
		/*
		 * String ldu3_model_name =
		 * cmbBxLDU_ModelName3.getSelectionModel().getSelectedItem();
		 * String ldu4_model_name =
		 * cmbBxLDU_ModelName4.getSelectionModel().getSelectedItem();
		 * String ldu5_model_name =
		 * cmbBxLDU_ModelName5.getSelectionModel().getSelectedItem();
		 * String ldu6_model_name =
		 * cmbBxLDU_ModelName6.getSelectionModel().getSelectedItem();
		 * String ldu7_model_name =
		 * cmbBxLDU_ModelName7.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu8_model_name =
		 * cmbBxLDU_ModelName8.getSelectionModel().getSelectedItem();
		 * String ldu9_model_name =
		 * cmbBxLDU_ModelName9.getSelectionModel().getSelectedItem();
		 * String ldu10_model_name =
		 * cmbBxLDU_ModelName10.getSelectionModel().getSelectedItem();
		 * String ldu11_model_name =
		 * cmbBxLDU_ModelName11.getSelectionModel().getSelectedItem();
		 * String ldu12_model_name =
		 * cmbBxLDU_ModelName12.getSelectionModel().getSelectedItem();
		 * String ldu13_model_name =
		 * cmbBxLDU_ModelName13.getSelectionModel().getSelectedItem();
		 * String ldu14_model_name =
		 * cmbBxLDU_ModelName14.getSelectionModel().getSelectedItem();
		 * String ldu15_model_name =
		 * cmbBxLDU_ModelName15.getSelectionModel().getSelectedItem();
		 * String ldu16_model_name =
		 * cmbBxLDU_ModelName16.getSelectionModel().getSelectedItem();
		 * String ldu17_model_name =
		 * cmbBxLDU_ModelName17.getSelectionModel().getSelectedItem();
		 * String ldu18_model_name =
		 * cmbBxLDU_ModelName18.getSelectionModel().getSelectedItem();
		 * String ldu19_model_name =
		 * cmbBxLDU_ModelName19.getSelectionModel().getSelectedItem();
		 * String ldu20_model_name =
		 * cmbBxLDU_ModelName20.getSelectionModel().getSelectedItem();
		 * String ldu21_model_name =
		 * cmbBxLDU_ModelName21.getSelectionModel().getSelectedItem();
		 * String ldu22_model_name =
		 * cmbBxLDU_ModelName22.getSelectionModel().getSelectedItem();
		 * String ldu23_model_name =
		 * cmbBxLDU_ModelName23.getSelectionModel().getSelectedItem();
		 * String ldu24_model_name =
		 * cmbBxLDU_ModelName24.getSelectionModel().getSelectedItem();
		 * String ldu25_model_name =
		 * cmbBxLDU_ModelName25.getSelectionModel().getSelectedItem();
		 * String ldu26_model_name =
		 * cmbBxLDU_ModelName26.getSelectionModel().getSelectedItem();
		 * String ldu27_model_name =
		 * cmbBxLDU_ModelName27.getSelectionModel().getSelectedItem();
		 * String ldu28_model_name =
		 * cmbBxLDU_ModelName28.getSelectionModel().getSelectedItem();
		 * String ldu29_model_name =
		 * cmbBxLDU_ModelName29.getSelectionModel().getSelectedItem();
		 * String ldu30_model_name =
		 * cmbBxLDU_ModelName30.getSelectionModel().getSelectedItem();
		 * String ldu31_model_name =
		 * cmbBxLDU_ModelName31.getSelectionModel().getSelectedItem();
		 * String ldu32_model_name =
		 * cmbBxLDU_ModelName32.getSelectionModel().getSelectedItem();
		 * String ldu33_model_name =
		 * cmbBxLDU_ModelName33.getSelectionModel().getSelectedItem();
		 * String ldu34_model_name =
		 * cmbBxLDU_ModelName34.getSelectionModel().getSelectedItem();
		 * String ldu35_model_name =
		 * cmbBxLDU_ModelName35.getSelectionModel().getSelectedItem();
		 * String ldu36_model_name =
		 * cmbBxLDU_ModelName36.getSelectionModel().getSelectedItem();
		 * String ldu37_model_name =
		 * cmbBxLDU_ModelName37.getSelectionModel().getSelectedItem();
		 * String ldu38_model_name =
		 * cmbBxLDU_ModelName38.getSelectionModel().getSelectedItem();
		 * String ldu39_model_name =
		 * cmbBxLDU_ModelName39.getSelectionModel().getSelectedItem();
		 * String ldu40_model_name =
		 * cmbBxLDU_ModelName40.getSelectionModel().getSelectedItem();
		 */

		String device1_port_name = cmbBxMegaOhmPm1_PortSelection.getSelectionModel().getSelectedItem();
		// String device2_port_name =
		// cmbBxMegaOhmPm2_PortSelection.getSelectionModel().getSelectedItem();
		/*
		 * String ldu_port_name3 =
		 * cmbBxLDU_PortSelection3.getSelectionModel().getSelectedItem();
		 * String ldu_port_name4 =
		 * cmbBxLDU_PortSelection4.getSelectionModel().getSelectedItem();
		 * String ldu_port_name5 =
		 * cmbBxLDU_PortSelection5.getSelectionModel().getSelectedItem();
		 * String ldu_port_name6 =
		 * cmbBxLDU_PortSelection6.getSelectionModel().getSelectedItem();
		 * String ldu_port_name7 =
		 * cmbBxLDU_PortSelection7.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu_port_name8 =
		 * cmbBxLDU_PortSelection8.getSelectionModel().getSelectedItem();
		 * String ldu_port_name9 =
		 * cmbBxLDU_PortSelection9.getSelectionModel().getSelectedItem();
		 * String ldu_port_name10 =
		 * cmbBxLDU_PortSelection10.getSelectionModel().getSelectedItem();
		 * String ldu_port_name11 =
		 * cmbBxLDU_PortSelection11.getSelectionModel().getSelectedItem();
		 * String ldu_port_name12 =
		 * cmbBxLDU_PortSelection12.getSelectionModel().getSelectedItem();
		 * String ldu_port_name13 =
		 * cmbBxLDU_PortSelection13.getSelectionModel().getSelectedItem();
		 * String ldu_port_name14 =
		 * cmbBxLDU_PortSelection14.getSelectionModel().getSelectedItem();
		 * String ldu_port_name15 =
		 * cmbBxLDU_PortSelection15.getSelectionModel().getSelectedItem();
		 * String ldu_port_name16 =
		 * cmbBxLDU_PortSelection16.getSelectionModel().getSelectedItem();
		 * String ldu_port_name17 =
		 * cmbBxLDU_PortSelection17.getSelectionModel().getSelectedItem();
		 * String ldu_port_name18 =
		 * cmbBxLDU_PortSelection18.getSelectionModel().getSelectedItem();
		 * String ldu_port_name19 =
		 * cmbBxLDU_PortSelection19.getSelectionModel().getSelectedItem();
		 * String ldu_port_name20 =
		 * cmbBxLDU_PortSelection20.getSelectionModel().getSelectedItem();
		 * String ldu_port_name21 =
		 * cmbBxLDU_PortSelection21.getSelectionModel().getSelectedItem();
		 * String ldu_port_name22 =
		 * cmbBxLDU_PortSelection22.getSelectionModel().getSelectedItem();
		 * String ldu_port_name23 =
		 * cmbBxLDU_PortSelection23.getSelectionModel().getSelectedItem();
		 * String ldu_port_name24 =
		 * cmbBxLDU_PortSelection24.getSelectionModel().getSelectedItem();
		 * String ldu_port_name25 =
		 * cmbBxLDU_PortSelection25.getSelectionModel().getSelectedItem();
		 * String ldu_port_name26 =
		 * cmbBxLDU_PortSelection26.getSelectionModel().getSelectedItem();
		 * String ldu_port_name27 =
		 * cmbBxLDU_PortSelection27.getSelectionModel().getSelectedItem();
		 * String ldu_port_name28 =
		 * cmbBxLDU_PortSelection28.getSelectionModel().getSelectedItem();
		 * String ldu_port_name29 =
		 * cmbBxLDU_PortSelection29.getSelectionModel().getSelectedItem();
		 * String ldu_port_name30 =
		 * cmbBxLDU_PortSelection30.getSelectionModel().getSelectedItem();
		 * String ldu_port_name31 =
		 * cmbBxLDU_PortSelection31.getSelectionModel().getSelectedItem();
		 * String ldu_port_name32 =
		 * cmbBxLDU_PortSelection32.getSelectionModel().getSelectedItem();
		 * String ldu_port_name33 =
		 * cmbBxLDU_PortSelection33.getSelectionModel().getSelectedItem();
		 * String ldu_port_name34 =
		 * cmbBxLDU_PortSelection34.getSelectionModel().getSelectedItem();
		 * String ldu_port_name35 =
		 * cmbBxLDU_PortSelection35.getSelectionModel().getSelectedItem();
		 * String ldu_port_name36 =
		 * cmbBxLDU_PortSelection36.getSelectionModel().getSelectedItem();
		 * String ldu_port_name37 =
		 * cmbBxLDU_PortSelection37.getSelectionModel().getSelectedItem();
		 * String ldu_port_name38 =
		 * cmbBxLDU_PortSelection38.getSelectionModel().getSelectedItem();
		 * String ldu_port_name39 =
		 * cmbBxLDU_PortSelection39.getSelectionModel().getSelectedItem();
		 * String ldu_port_name40 =
		 * cmbBxLDU_PortSelection40.getSelectionModel().getSelectedItem();
		 */

		String device1_baud_rate = cmbBxMegaOhmPm1_BaudRate.getSelectionModel().getSelectedItem().toString();
		// String device2_baud_rate =
		// cmbBxMegaOhmPm2_BaudRate.getSelectionModel().getSelectedItem().toString();
		/*
		 * String ldu3_baud_rate =
		 * cmbBxLDU_BaudRate3.getSelectionModel().getSelectedItem().toString();
		 * String ldu4_baud_rate =
		 * cmbBxLDU_BaudRate4.getSelectionModel().getSelectedItem().toString();
		 * String ldu5_baud_rate =
		 * cmbBxLDU_BaudRate5.getSelectionModel().getSelectedItem().toString();
		 * String ldu6_baud_rate =
		 * cmbBxLDU_BaudRate6.getSelectionModel().getSelectedItem().toString();
		 * String ldu7_baud_rate =
		 * cmbBxLDU_BaudRate7.getSelectionModel().getSelectedItem().toString();
		 * 
		 * String ldu8_baud_rate =
		 * cmbBxLDU_BaudRate8.getSelectionModel().getSelectedItem().toString();
		 * String ldu9_baud_rate =
		 * cmbBxLDU_BaudRate9.getSelectionModel().getSelectedItem().toString();
		 * String ldu10_baud_rate =
		 * cmbBxLDU_BaudRate10.getSelectionModel().getSelectedItem().toString();
		 * String ldu11_baud_rate =
		 * cmbBxLDU_BaudRate11.getSelectionModel().getSelectedItem().toString();
		 * String ldu12_baud_rate =
		 * cmbBxLDU_BaudRate12.getSelectionModel().getSelectedItem().toString();
		 * String ldu13_baud_rate =
		 * cmbBxLDU_BaudRate13.getSelectionModel().getSelectedItem().toString();
		 * String ldu14_baud_rate =
		 * cmbBxLDU_BaudRate14.getSelectionModel().getSelectedItem().toString();
		 * String ldu15_baud_rate =
		 * cmbBxLDU_BaudRate15.getSelectionModel().getSelectedItem().toString();
		 * String ldu16_baud_rate =
		 * cmbBxLDU_BaudRate16.getSelectionModel().getSelectedItem().toString();
		 * String ldu17_baud_rate =
		 * cmbBxLDU_BaudRate17.getSelectionModel().getSelectedItem().toString();
		 * String ldu18_baud_rate =
		 * cmbBxLDU_BaudRate18.getSelectionModel().getSelectedItem().toString();
		 * String ldu19_baud_rate =
		 * cmbBxLDU_BaudRate19.getSelectionModel().getSelectedItem().toString();
		 * String ldu20_baud_rate =
		 * cmbBxLDU_BaudRate20.getSelectionModel().getSelectedItem().toString();
		 * String ldu21_baud_rate =
		 * cmbBxLDU_BaudRate21.getSelectionModel().getSelectedItem().toString();
		 * String ldu22_baud_rate =
		 * cmbBxLDU_BaudRate22.getSelectionModel().getSelectedItem().toString();
		 * String ldu23_baud_rate =
		 * cmbBxLDU_BaudRate23.getSelectionModel().getSelectedItem().toString();
		 * String ldu24_baud_rate =
		 * cmbBxLDU_BaudRate24.getSelectionModel().getSelectedItem().toString();
		 * String ldu25_baud_rate =
		 * cmbBxLDU_BaudRate25.getSelectionModel().getSelectedItem().toString();
		 * String ldu26_baud_rate =
		 * cmbBxLDU_BaudRate26.getSelectionModel().getSelectedItem().toString();
		 * String ldu27_baud_rate =
		 * cmbBxLDU_BaudRate27.getSelectionModel().getSelectedItem().toString();
		 * String ldu28_baud_rate =
		 * cmbBxLDU_BaudRate28.getSelectionModel().getSelectedItem().toString();
		 * String ldu29_baud_rate =
		 * cmbBxLDU_BaudRate29.getSelectionModel().getSelectedItem().toString();
		 * String ldu30_baud_rate =
		 * cmbBxLDU_BaudRate30.getSelectionModel().getSelectedItem().toString();
		 * String ldu31_baud_rate =
		 * cmbBxLDU_BaudRate31.getSelectionModel().getSelectedItem().toString();
		 * String ldu32_baud_rate =
		 * cmbBxLDU_BaudRate32.getSelectionModel().getSelectedItem().toString();
		 * String ldu33_baud_rate =
		 * cmbBxLDU_BaudRate33.getSelectionModel().getSelectedItem().toString();
		 * String ldu34_baud_rate =
		 * cmbBxLDU_BaudRate34.getSelectionModel().getSelectedItem().toString();
		 * String ldu35_baud_rate =
		 * cmbBxLDU_BaudRate35.getSelectionModel().getSelectedItem().toString();
		 * String ldu36_baud_rate =
		 * cmbBxLDU_BaudRate36.getSelectionModel().getSelectedItem().toString();
		 * String ldu37_baud_rate =
		 * cmbBxLDU_BaudRate37.getSelectionModel().getSelectedItem().toString();
		 * String ldu38_baud_rate =
		 * cmbBxLDU_BaudRate38.getSelectionModel().getSelectedItem().toString();
		 * String ldu39_baud_rate =
		 * cmbBxLDU_BaudRate39.getSelectionModel().getSelectedItem().toString();
		 * String ldu40_baud_rate =
		 * cmbBxLDU_BaudRate40.getSelectionModel().getSelectedItem().toString();
		 */

		String device1ClusterName = (String) ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel().getSelectedItem();
		// String device2ClusterName =
		// (String)ref_cmbBxMegaOhmPm2ClusterId.getSelectionModel().getSelectedItem();

		String device1BayName = (String) ref_cmbBxMegaOhmPm1BayId.getSelectionModel().getSelectedItem();
		// String device2BayName =
		// (String)ref_cmbBxMegaOhmPm2BayId.getSelectionModel().getSelectedItem();

		String device1PositionNo = (String) ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().getSelectedItem();
		// String device2PositionNo =
		// (String)ref_cmbBxMegaOhmPm2PositionId.getSelectionModel().getSelectedItem();

		String device1Cname = ref_txtMegaOhmPm1Cname.getText();// getSelectionModel().getSelectedItem();
		// String device2Cname =
		// ref_txtMegaOhmPm2Cname.getText();//getSelectionModel().getSelectedItem();

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
			String deviceType = ConstantConveyor.DEVICE_TYPE_OHM_METER;
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
		// MySQL_Controller.sp_add_device_settings_v2 ( device2_type,
		// device2_model_name, device2_port_name, device2_baud_rate,
		// device2ClusterName,device2BayName,device2PositionNo, device2Cname,deviceId2);

		// MySQL_Controller.sp_add_device_settings(3, ldu1_type, ldu1_model_name,
		// ldu_port_name1, ldu1_baud_rate,qrPortName1);
		// MySQL_Controller.sp_add_device_settings(4, ldu2_type, ldu2_model_name,
		// ldu_port_name2, ldu2_baud_rate,qrPortName2);

		/*
		 * MySQL_Controller.sp_add_device_settings(5, ldu3_type, ldu3_model_name,
		 * ldu_port_name3, ldu3_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(6, ldu4_type, ldu4_model_name,
		 * ldu_port_name4, ldu4_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(7, ldu5_type, ldu5_model_name,
		 * ldu_port_name5, ldu5_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(8, ldu6_type, ldu6_model_name,
		 * ldu_port_name6, ldu6_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(9, ldu7_type, ldu7_model_name,
		 * ldu_port_name7, ldu7_baud_rate);
		 * 
		 * MySQL_Controller.sp_add_device_settings(10, ldu8_type, ldu8_model_name,
		 * ldu_port_name8, ldu8_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(11, ldu9_type, ldu9_model_name,
		 * ldu_port_name9, ldu9_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(12, ldu10_type, ldu10_model_name,
		 * ldu_port_name10, ldu10_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(13, ldu11_type, ldu11_model_name,
		 * ldu_port_name11, ldu11_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(14, ldu12_type, ldu12_model_name,
		 * ldu_port_name12, ldu12_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(15, ldu13_type, ldu13_model_name,
		 * ldu_port_name13, ldu13_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(16, ldu14_type, ldu14_model_name,
		 * ldu_port_name14, ldu14_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(17, ldu15_type, ldu15_model_name,
		 * ldu_port_name15, ldu15_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(18, ldu16_type, ldu16_model_name,
		 * ldu_port_name16, ldu16_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(19, ldu17_type, ldu17_model_name,
		 * ldu_port_name17, ldu17_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(20, ldu18_type, ldu18_model_name,
		 * ldu_port_name18, ldu18_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(21, ldu19_type, ldu19_model_name,
		 * ldu_port_name19, ldu19_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(22, ldu20_type, ldu20_model_name,
		 * ldu_port_name20, ldu20_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(23, ldu21_type, ldu21_model_name,
		 * ldu_port_name21, ldu21_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(24, ldu22_type, ldu22_model_name,
		 * ldu_port_name22, ldu22_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(25, ldu23_type, ldu23_model_name,
		 * ldu_port_name23, ldu23_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(26, ldu24_type, ldu24_model_name,
		 * ldu_port_name24, ldu24_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(27, ldu25_type, ldu25_model_name,
		 * ldu_port_name25, ldu25_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(28, ldu26_type, ldu26_model_name,
		 * ldu_port_name26, ldu26_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(29, ldu27_type, ldu27_model_name,
		 * ldu_port_name27, ldu27_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(30, ldu28_type, ldu28_model_name,
		 * ldu_port_name28, ldu28_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(31, ldu29_type, ldu29_model_name,
		 * ldu_port_name29, ldu29_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(32, ldu30_type, ldu30_model_name,
		 * ldu_port_name30, ldu30_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(33, ldu31_type, ldu31_model_name,
		 * ldu_port_name31, ldu31_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(34, ldu32_type, ldu32_model_name,
		 * ldu_port_name32, ldu32_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(35, ldu33_type, ldu33_model_name,
		 * ldu_port_name33, ldu33_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(36, ldu34_type, ldu34_model_name,
		 * ldu_port_name34, ldu34_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(37, ldu35_type, ldu35_model_name,
		 * ldu_port_name35, ldu35_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(38, ldu36_type, ldu36_model_name,
		 * ldu_port_name36, ldu36_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(39, ldu37_type, ldu37_model_name,
		 * ldu_port_name37, ldu37_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(40, ldu38_type, ldu38_model_name,
		 * ldu_port_name38, ldu38_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(41, ldu39_type, ldu39_model_name,
		 * ldu_port_name39, ldu39_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(42, ldu40_type, ldu40_model_name,
		 * ldu_port_name40, ldu40_baud_rate);
		 */

		/*
		 * if((ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK > 40) &&
		 * (ConstantConfig.TOTAL_NO_OF_SUPPORTED_RACK ==
		 * ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK_MAX_POSITION)){
		 * 
		 * String ldu41_model_name =
		 * cmbBxLDU_ModelName41.getSelectionModel().getSelectedItem();
		 * String ldu42_model_name =
		 * cmbBxLDU_ModelName42.getSelectionModel().getSelectedItem();
		 * String ldu43_model_name =
		 * cmbBxLDU_ModelName43.getSelectionModel().getSelectedItem();
		 * String ldu44_model_name =
		 * cmbBxLDU_ModelName44.getSelectionModel().getSelectedItem();
		 * String ldu45_model_name =
		 * cmbBxLDU_ModelName45.getSelectionModel().getSelectedItem();
		 * String ldu46_model_name =
		 * cmbBxLDU_ModelName46.getSelectionModel().getSelectedItem();
		 * String ldu47_model_name =
		 * cmbBxLDU_ModelName47.getSelectionModel().getSelectedItem();
		 * String ldu48_model_name =
		 * cmbBxLDU_ModelName48.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu_port_name41 =
		 * cmbBxLDU_PortSelection41.getSelectionModel().getSelectedItem();
		 * String ldu_port_name42 =
		 * cmbBxLDU_PortSelection42.getSelectionModel().getSelectedItem();
		 * String ldu_port_name43 =
		 * cmbBxLDU_PortSelection43.getSelectionModel().getSelectedItem();
		 * String ldu_port_name44 =
		 * cmbBxLDU_PortSelection44.getSelectionModel().getSelectedItem();
		 * String ldu_port_name45 =
		 * cmbBxLDU_PortSelection45.getSelectionModel().getSelectedItem();
		 * String ldu_port_name46 =
		 * cmbBxLDU_PortSelection46.getSelectionModel().getSelectedItem();
		 * String ldu_port_name47 =
		 * cmbBxLDU_PortSelection47.getSelectionModel().getSelectedItem();
		 * String ldu_port_name48 =
		 * cmbBxLDU_PortSelection48.getSelectionModel().getSelectedItem();
		 * 
		 * String ldu41_baud_rate =
		 * cmbBxLDU_BaudRate41.getSelectionModel().getSelectedItem().toString();
		 * String ldu42_baud_rate =
		 * cmbBxLDU_BaudRate42.getSelectionModel().getSelectedItem().toString();
		 * String ldu43_baud_rate =
		 * cmbBxLDU_BaudRate43.getSelectionModel().getSelectedItem().toString();
		 * String ldu44_baud_rate =
		 * cmbBxLDU_BaudRate44.getSelectionModel().getSelectedItem().toString();
		 * String ldu45_baud_rate =
		 * cmbBxLDU_BaudRate45.getSelectionModel().getSelectedItem().toString();
		 * String ldu46_baud_rate =
		 * cmbBxLDU_BaudRate46.getSelectionModel().getSelectedItem().toString();
		 * String ldu47_baud_rate =
		 * cmbBxLDU_BaudRate47.getSelectionModel().getSelectedItem().toString();
		 * String ldu48_baud_rate =
		 * cmbBxLDU_BaudRate48.getSelectionModel().getSelectedItem().toString();
		 * 
		 * MySQL_Controller.sp_add_device_settings(43, ldu41_type, ldu41_model_name,
		 * ldu_port_name41, ldu41_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(44, ldu42_type, ldu42_model_name,
		 * ldu_port_name42, ldu42_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(45, ldu43_type, ldu43_model_name,
		 * ldu_port_name43, ldu43_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(46, ldu44_type, ldu44_model_name,
		 * ldu_port_name44, ldu44_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(47, ldu45_type, ldu45_model_name,
		 * ldu_port_name45, ldu45_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(48, ldu46_type, ldu46_model_name,
		 * ldu_port_name46, ldu46_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(49, ldu47_type, ldu47_model_name,
		 * ldu_port_name47, ldu47_baud_rate);
		 * MySQL_Controller.sp_add_device_settings(50, ldu48_type, ldu48_model_name,
		 * ldu_port_name48, ldu48_baud_rate);
		 * 
		 * }
		 */

		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}

	/*
	 * public void PwrSrcValidateSerialCmd(){
	 * 
	 * String PowerSrcCommPortID= null;
	 * String PwrSrcCommBaudRate = null;
	 * txtValidatePwrSrcCmdStatus.clear();
	 * try{
	 * serialDM_Obj.commPowerSrc.searchForPorts();
	 * //PowerSrcCommPortID = getCurrentPwrSrcComPortID();
	 * //PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
	 * boolean status =
	 * serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
	 * 
	 * if (!status){
	 * 
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * status = serialDM_Obj.SetPowerSourceOff();
	 * if (!status){
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * 
	 * }
	 * ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: testD:"
	 * +serialDM_Obj.commPowerSrc.getPortDeviceMapping());
	 * serialDM_Obj.DisconnectPwrSrc();
	 * 
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("PwrSrcValidateSerialCmd: Exception1:"+e.
	 * getMessage());
	 * //ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: Exception:"+e.
	 * toString());
	 * txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }
	 * }
	 * 
	 * public void RefStdValidateSerialCmd(){
	 * 
	 * String RefStdCommPortID= null;
	 * String RefStdCommBaudRate = null;
	 * txtValidateRefStdCmdStatus.clear();
	 * try{
	 * serialDM_Obj.commRefStandard.searchForPorts();
	 * RefStdCommPortID = getCurrentRefStdComPortID();
	 * RefStdCommBaudRate = getCurrentRefStdComBaudRate();
	 * boolean status =
	 * serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * 
	 * status = serialDM_Obj.mteRefStd_ValidateVersionCMD();
	 * if (!status){
	 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * 
	 * }
	 * serialDM_Obj.DisconnectRefStd();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("RefStdValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 */
	/*
	 * class MyRunnable implements Runnable{
	 * 
	 * 
	 * double count ;
	 * TextField l_txtValidateRefStdCmdStatus;
	 * 
	 * public MyRunnable(TextField ValidateRefStdCmdStatus) {
	 * count = 0;
	 * l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
	 * }
	 * 
	 * @Override
	 * public void run() {
	 * for (int i = 0; i <= count; i++) {
	 * 
	 * final double update_i = count;
	 * 
	 * 
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test2");
	 * l_txtValidateRefStdCmdStatus.setText("Sending CMD"+update_i);
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test3");
	 * 
	 * //Update JavaFX UI with runLater() in UI thread
	 * Platform.runLater(new Runnable(){
	 * 
	 * @Override
	 * public void run() {
	 * for (int j = 0; j <= update_i; j++) {
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test2");
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test3");
	 * ApplicationLauncher.logger.info(
	 * "RefStdValidateSerialCmd:runLater2:SleepEntry");
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:Exception"
	 * );
	 * 
	 * 
	 * ApplicationLauncher.logger.info(
	 * "RefStdValidateSerialCmd:runLater2:NextForloop");
	 * 
	 * }
	 * }
	 * });
	 * 
	 * 
	 * }
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * class UI_DisplayTimerTask extends TimerTask{
	 * 
	 * 
	 * double count =10;
	 * TextField l_txtValidateRefStdCmdStatus;
	 * 
	 * public UI_DisplayTimerTask(TextField ValidateRefStdCmdStatus) {
	 * 
	 * l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
	 * 
	 * }
	 * 
	 * @Override
	 * public void run() {
	 * for(int i=0;i<count;i++){
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test2");
	 * l_txtValidateRefStdCmdStatus.setText("Sending CMD"+i);
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test3");
	 * 
	 * try {
	 * Thread.sleep(1000);
	 * } catch (InterruptedException e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.
	 * error("UI_DisplayTimerTask: InterruptedException: "+e.getMessage());
	 * }
	 * }
	 * 
	 * UI_DisplayTimer.cancel();
	 * 
	 * 
	 * }
	 * 
	 * }
	 */

	/*
	 * public void LDU_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus1.clear();
	 * try{
	 * serialDM_Obj.commLDU1.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID1();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
	 * boolean status = serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-1.00");
	 * DisplayDataObj.set_Error_max("+1.00");
	 * DisplayDataObj.setNoOfPulses("10");
	 * 
	 * DisplayDataObj.setLDU1_ReadDataFlag(true);
	 * status = serialDM_Obj.LDU_ResetSetting();
	 * if (!status){
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU1_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU1();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 */

	public void megaOhmPm1_ValidateSerialCmd() {
		ApplicationLauncher.logger.debug("megaOhmPm1_ValidateSerialCmd : Entry");

		String LDU_CommPortID = null;
		String LDUCommBaudRate = null;
		txtValidateMegaOhmPm1_CmdStatus.clear();
		txtMegaOhmPm1ReadData.clear();
		try {
			// serialDM_Obj.commLDU1.searchForPorts();
			LDU_CommPortID = getCurrentLDU_ComPortID1();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate1();
			// boolean status =
			// DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
			String portCname = ref_txtMegaOhmPm1Cname.getText();
			String slaveId = ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().getSelectedItem();
			// MegaOhmPmSpm serialPortManagerQrScanner = new MegaOhmPmSpm(portCname);
			SpmMegaOhmPm serialPortManagerQrScanner = new SpmMegaOhmPm(portCname + "-slv-" + slaveId,
					EIC_MegaOhmMeter.ER_LENGTH_ASCII);
			boolean status = serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID, LDUCommBaudRate);
			if (!status) {

				txtValidateMegaOhmPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				// status = serialDM_Obj.lscsLDU1_CheckCom();
				// DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
				serialPortManagerQrScanner.startSerialRxPhysical_MegaOhmPm();
				serialPortManagerQrScanner.enableSerialRxPhysical_MegaOhmPmMonitor();
				MegaOhmPmDirector pwrSrcDirector = new MegaOhmPmDirector(serialPortManagerQrScanner);
				// String slaveId=
				// ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().getSelectedItem();//ConstantBayPortNameMapping.ERC_MEGA_OHM_METER_01_SLAVE_ID;
				ApplicationLauncher.logger.debug("megaOhmPm1_ValidateSerialCmd: slaveId: " + slaveId);
				Map<String, Object> responseMap = pwrSrcDirector.fetchMegaOhmMetrics(slaveId);

				status = (boolean) responseMap.get("status");
				String responseData = "";
				try {
					if (status) {
						responseData = (String) responseMap.get("responseData");
						ApplicationLauncher.logger
								.debug("megaOhmPm1_ValidateSerialCmd: responseData1: " + responseData);
						EIC_MegaOhmMeter eicMegaOhmMeter = new EIC_MegaOhmMeter();
						responseData = eicMegaOhmMeter
								.extractFloatFromResponse((String) responseMap.get("responseData"));
						ApplicationLauncher.logger
								.debug("megaOhmPm1_ValidateSerialCmd: responseData2: " + responseData);
					} else {
						ApplicationLauncher.logger.debug("megaOhmPm1_ValidateSerialCmd: No response ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("megaOhmPm1_ValidateSerialCmd: Exception" + e.getMessage());
				}
				if (!status) {
					txtValidateMegaOhmPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				} else {
					txtValidateMegaOhmPm1_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
					txtMegaOhmPm1ReadData.setText(responseData);
				}
				setPortValidationTurnedON(false);
				// DisplayDataObj.pwrSrcDisconnectPort_V2();
				serialPortManagerQrScanner.disconnectMegaOhmPm();
				// DisplayDataObj.setLDU1_ReadDataFlag(false);
			}
			// serialDM_Obj.DisconnectLDU1();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("megaOhmPm1_ValidateSerialCmd: Exception" + e.getMessage());
		}

		ApplicationLauncher.logger.debug("megaOhmPm1_ValidateSerialCmd : Exit");

	}

	/*
	 * public void qr2_ValidateSerialCmd(){
	 * ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd : Entry");
	 * 
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateQr2_CmdStatus.clear();
	 * txtQrReadData2.clear();
	 * try{
	 * //serialDM_Obj.commLDU1.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID2();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate2();
	 * String portCname = ref_txtQr2Cname.getText();
	 * //boolean status =
	 * DisplayDataObj.pwrSrcPortAccessible_V2_1(LDU_CommPortID,LDUCommBaudRate);//
	 * false;//serialDM_Obj.LDU1_Init(LDU_CommPortID,LDUCommBaudRate);
	 * QrScannerSpm serialPortManagerQrScanner = new QrScannerSpm(portCname);
	 * boolean status =
	 * serialPortManagerQrScanner.powerSourceComInitV2(LDU_CommPortID,
	 * LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * //status = serialDM_Obj.lscsLDU1_CheckCom();
	 * //DisplayDataObj.pwrSrcEnableSerialMonitoring_V2();
	 * //PowerSourceDirector pwrSrcDirector = new PowerSourceDirector();
	 * serialPortManagerQrScanner.startSerialRxPhysical_PwrSrc();
	 * serialPortManagerQrScanner.enableSerialRxPhysical_QrScannerMonitor();
	 * QrScannerDirector pwrSrcDirector = new
	 * QrScannerDirector(serialPortManagerQrScanner);
	 * Map<String,Object> responseMap = pwrSrcDirector.scanQrCode();
	 * 
	 * status = (boolean)responseMap.get("status");
	 * String qrData = "";
	 * try{
	 * if(status) {
	 * qrData = (String)responseMap.get("responseData");
	 * ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: qrData1: "+qrData);
	 * qrData = NewlandQRCodeScanner.extractScannedData((String)responseMap.get(
	 * "responseData"));
	 * ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: qrData2: "+qrData);
	 * }else {
	 * ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd: No response ");
	 * }
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("qr2_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * if (!status){
	 * txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateQr2_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * txtQrReadData2.setText(qrData);
	 * }
	 * setPortValidationTurnedON(false);
	 * serialPortManagerQrScanner.disconnectQrScanner();
	 * //DisplayDataObj.pwrSrcDisconnectPort_V2();
	 * //DisplayDataObj.setLDU1_ReadDataFlag(false);
	 * }
	 * //serialDM_Obj.DisconnectLDU1();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("qr2_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * ApplicationLauncher.logger.debug("qr2_ValidateSerialCmd : Exit");
	 * 
	 * }
	 */

	/*
	 * public void lscsLDU2_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus2.clear();
	 * try{
	 * serialDM_Obj.commLDU2.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID2();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate2();
	 * boolean status = serialDM_Obj.LDU2_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU2_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU2_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU2();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU2_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU3_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus3.clear();
	 * try{
	 * serialDM_Obj.commLDU3.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID3();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate3();
	 * boolean status = serialDM_Obj.LDU3_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU3_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU3_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU3();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU3_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU4_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus4.clear();
	 * try{
	 * serialDM_Obj.commLDU4.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID4();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate4();
	 * boolean status = serialDM_Obj.LDU4_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU4_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU4_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU4();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU4_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU5_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus5.clear();
	 * try{
	 * serialDM_Obj.commLDU5.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID5();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate5();
	 * boolean status = serialDM_Obj.LDU5_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * 
	 * status = serialDM_Obj.lscsLDU5_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU5_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU5();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU5_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU6_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus6.clear();
	 * try{
	 * serialDM_Obj.commLDU6.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID6();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate6();
	 * boolean status = serialDM_Obj.LDU6_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-6.00");
	 * DisplayDataObj.set_Error_max("+6.00");
	 * DisplayDataObj.setNoOfPulses("60");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU6_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU6_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU6();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU6_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU7_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus7.clear();
	 * try{
	 * serialDM_Obj.commLDU7.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID7();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate7();
	 * boolean status = serialDM_Obj.LDU7_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-7.00");
	 * DisplayDataObj.set_Error_max("+7.00");
	 * DisplayDataObj.setNoOfPulses("70");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU7_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU7_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU7();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU7_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * public void lscsLDU8_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus8.clear();
	 * try{
	 * serialDM_Obj.commLDU8.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID8();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate8();
	 * boolean status = serialDM_Obj.LDU8_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-8.00");
	 * DisplayDataObj.set_Error_max("+8.00");
	 * DisplayDataObj.setNoOfPulses("80");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU8_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU8_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU8();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU8_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU9_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus9.clear();
	 * try{
	 * serialDM_Obj.commLDU9.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID9();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate9();
	 * boolean status = serialDM_Obj.LDU9_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-9.00");
	 * DisplayDataObj.set_Error_max("+9.00");
	 * DisplayDataObj.setNoOfPulses("90");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU9_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU9_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU9();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU9_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU10_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus10.clear();
	 * try{
	 * serialDM_Obj.commLDU10.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID10();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate10();
	 * boolean status = serialDM_Obj.LDU10_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-10.00");
	 * DisplayDataObj.set_Error_max("+10.00");
	 * DisplayDataObj.setNoOfPulses("100");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU10_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU10_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU10();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU10_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU11_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus11.clear();
	 * try{
	 * serialDM_Obj.commLDU11.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID11();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate11();
	 * boolean status = serialDM_Obj.LDU11_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-11.00");
	 * DisplayDataObj.set_Error_max("+11.00");
	 * DisplayDataObj.setNoOfPulses("110");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU11_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU11_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU11();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU11_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU12_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus12.clear();
	 * try{
	 * serialDM_Obj.commLDU12.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID12();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate12();
	 * boolean status = serialDM_Obj.LDU12_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-12.00");
	 * DisplayDataObj.set_Error_max("+12.00");
	 * DisplayDataObj.setNoOfPulses("120");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU12_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU12_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU12();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU12_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU13_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus13.clear();
	 * try{
	 * serialDM_Obj.commLDU13.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID13();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate13();
	 * boolean status = serialDM_Obj.LDU13_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-13.00");
	 * DisplayDataObj.set_Error_max("+13.00");
	 * DisplayDataObj.setNoOfPulses("130");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU13_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU13_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU13();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU13_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU14_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus14.clear();
	 * try{
	 * serialDM_Obj.commLDU14.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID14();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate14();
	 * boolean status = serialDM_Obj.LDU14_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-14.00");
	 * DisplayDataObj.set_Error_max("+14.00");
	 * DisplayDataObj.setNoOfPulses("140");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU14_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU14_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU14();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU14_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU15_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus15.clear();
	 * try{
	 * serialDM_Obj.commLDU15.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID15();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate15();
	 * boolean status = serialDM_Obj.LDU15_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-15.00");
	 * DisplayDataObj.set_Error_max("+15.00");
	 * DisplayDataObj.setNoOfPulses("150");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU15_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU15_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU15();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU15_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU16_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus16.clear();
	 * try{
	 * serialDM_Obj.commLDU16.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID16();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate16();
	 * boolean status = serialDM_Obj.LDU16_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-16.00");
	 * DisplayDataObj.set_Error_max("+16.00");
	 * DisplayDataObj.setNoOfPulses("160");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU16_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU16_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU16();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU16_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU17_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus17.clear();
	 * try{
	 * serialDM_Obj.commLDU17.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID17();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate17();
	 * boolean status = serialDM_Obj.LDU17_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-17.00");
	 * DisplayDataObj.set_Error_max("+17.00");
	 * DisplayDataObj.setNoOfPulses("170");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU17_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU17_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU17();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU17_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU18_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus18.clear();
	 * try{
	 * serialDM_Obj.commLDU18.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID18();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate18();
	 * boolean status = serialDM_Obj.LDU18_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-18.00");
	 * DisplayDataObj.set_Error_max("+18.00");
	 * DisplayDataObj.setNoOfPulses("180");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU18_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU18_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU18();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU18_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU19_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus19.clear();
	 * try{
	 * serialDM_Obj.commLDU19.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID19();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate19();
	 * boolean status = serialDM_Obj.LDU19_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-19.00");
	 * DisplayDataObj.set_Error_max("+19.00");
	 * DisplayDataObj.setNoOfPulses("190");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU19_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU19_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU19();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU19_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU20_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus20.clear();
	 * try{
	 * serialDM_Obj.commLDU20.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID20();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate20();
	 * boolean status = serialDM_Obj.LDU20_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-20.00");
	 * DisplayDataObj.set_Error_max("+20.00");
	 * DisplayDataObj.setNoOfPulses("200");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU20_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU20_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU20();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU20_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU21_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus21.clear();
	 * try{
	 * serialDM_Obj.commLDU21.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID21();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate21();
	 * boolean status = serialDM_Obj.LDU21_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus21.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-21.00");
	 * DisplayDataObj.set_Error_max("+21.00");
	 * DisplayDataObj.setNoOfPulses("210");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU21_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus21.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus21.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU21_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU21();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU21_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU22_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus22.clear();
	 * try{
	 * serialDM_Obj.commLDU22.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID22();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate22();
	 * boolean status = serialDM_Obj.LDU22_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus22.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-22.00");
	 * DisplayDataObj.set_Error_max("+22.00");
	 * DisplayDataObj.setNoOfPulses("220");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU22_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus22.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus22.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU22_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU22();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU22_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU23_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus23.clear();
	 * try{
	 * serialDM_Obj.commLDU23.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID23();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate23();
	 * boolean status = serialDM_Obj.LDU23_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus23.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-23.00");
	 * DisplayDataObj.set_Error_max("+23.00");
	 * DisplayDataObj.setNoOfPulses("230");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU23_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus23.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus23.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU23_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU23();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU23_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU24_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus24.clear();
	 * try{
	 * serialDM_Obj.commLDU24.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID24();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate24();
	 * boolean status = serialDM_Obj.LDU24_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus24.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-24.00");
	 * DisplayDataObj.set_Error_max("+24.00");
	 * DisplayDataObj.setNoOfPulses("240");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU24_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus24.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus24.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU24_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU24();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU24_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU25_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus25.clear();
	 * try{
	 * serialDM_Obj.commLDU25.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID25();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate25();
	 * boolean status = serialDM_Obj.LDU25_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus25.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-25.00");
	 * DisplayDataObj.set_Error_max("+25.00");
	 * DisplayDataObj.setNoOfPulses("250");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU25_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus25.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus25.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU25_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU25();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU25_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU26_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus26.clear();
	 * try{
	 * serialDM_Obj.commLDU26.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID26();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate26();
	 * boolean status = serialDM_Obj.LDU26_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus26.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-26.00");
	 * DisplayDataObj.set_Error_max("+26.00");
	 * DisplayDataObj.setNoOfPulses("260");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU26_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus26.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus26.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU26_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU26();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU26_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU27_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus27.clear();
	 * try{
	 * serialDM_Obj.commLDU27.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID27();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate27();
	 * boolean status = serialDM_Obj.LDU27_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus27.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-27.00");
	 * DisplayDataObj.set_Error_max("+27.00");
	 * DisplayDataObj.setNoOfPulses("270");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU27_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus27.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus27.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU27_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU27();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU27_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU28_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus28.clear();
	 * try{
	 * serialDM_Obj.commLDU28.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID28();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate28();
	 * boolean status = serialDM_Obj.LDU28_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus28.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-28.00");
	 * DisplayDataObj.set_Error_max("+28.00");
	 * DisplayDataObj.setNoOfPulses("280");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU28_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus28.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus28.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU28_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU28();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU28_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU29_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus29.clear();
	 * try{
	 * serialDM_Obj.commLDU29.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID29();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate29();
	 * boolean status = serialDM_Obj.LDU29_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus29.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-29.00");
	 * DisplayDataObj.set_Error_max("+29.00");
	 * DisplayDataObj.setNoOfPulses("290");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU29_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus29.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus29.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU29_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU29();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU29_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * public void lscsLDU30_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus30.clear();
	 * try{
	 * serialDM_Obj.commLDU30.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID30();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate30();
	 * boolean status = serialDM_Obj.LDU30_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus30.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-30.00");
	 * DisplayDataObj.set_Error_max("+30.00");
	 * DisplayDataObj.setNoOfPulses("300");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU30_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus30.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus30.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU30_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU30();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU30_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU31_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus31.clear();
	 * try{
	 * serialDM_Obj.commLDU31.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID31();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate31();
	 * boolean status = serialDM_Obj.LDU31_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus31.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-31.00");
	 * DisplayDataObj.set_Error_max("+31.00");
	 * DisplayDataObj.setNoOfPulses("310");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU31_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus31.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus31.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU31_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU31();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU31_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU32_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus32.clear();
	 * try{
	 * serialDM_Obj.commLDU32.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID32();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate32();
	 * boolean status = serialDM_Obj.LDU32_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus32.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-32.00");
	 * DisplayDataObj.set_Error_max("+32.00");
	 * DisplayDataObj.setNoOfPulses("320");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU32_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus32.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus32.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU32_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU32();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU32_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU33_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus33.clear();
	 * try{
	 * serialDM_Obj.commLDU33.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID33();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate33();
	 * boolean status = serialDM_Obj.LDU33_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus33.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-33.00");
	 * DisplayDataObj.set_Error_max("+33.00");
	 * DisplayDataObj.setNoOfPulses("330");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU33_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus33.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus33.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU33_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU33();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU33_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU34_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus34.clear();
	 * try{
	 * serialDM_Obj.commLDU34.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID34();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate34();
	 * boolean status = serialDM_Obj.LDU34_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus34.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-34.00");
	 * DisplayDataObj.set_Error_max("+34.00");
	 * DisplayDataObj.setNoOfPulses("340");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU34_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus34.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus34.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU34_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU34();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU34_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU35_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus35.clear();
	 * try{
	 * serialDM_Obj.commLDU35.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID35();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate35();
	 * boolean status = serialDM_Obj.LDU35_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus35.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-35.00");
	 * DisplayDataObj.set_Error_max("+35.00");
	 * DisplayDataObj.setNoOfPulses("350");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU35_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus35.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus35.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU35_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU35();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU35_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU36_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus36.clear();
	 * try{
	 * serialDM_Obj.commLDU36.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID36();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate36();
	 * boolean status = serialDM_Obj.LDU36_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus36.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-36.00");
	 * DisplayDataObj.set_Error_max("+36.00");
	 * DisplayDataObj.setNoOfPulses("360");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU36_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus36.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus36.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU36_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU36();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU36_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU37_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus37.clear();
	 * try{
	 * serialDM_Obj.commLDU37.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID37();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate37();
	 * boolean status = serialDM_Obj.LDU37_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus37.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-37.00");
	 * DisplayDataObj.set_Error_max("+37.00");
	 * DisplayDataObj.setNoOfPulses("370");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU37_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus37.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus37.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU37_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU37();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU37_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU38_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus38.clear();
	 * try{
	 * serialDM_Obj.commLDU38.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID38();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate38();
	 * boolean status = serialDM_Obj.LDU38_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus38.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-38.00");
	 * DisplayDataObj.set_Error_max("+38.00");
	 * DisplayDataObj.setNoOfPulses("380");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU38_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus38.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus38.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU38_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU38();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU38_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU39_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus39.clear();
	 * try{
	 * serialDM_Obj.commLDU39.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID39();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate39();
	 * boolean status = serialDM_Obj.LDU39_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus39.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-39.00");
	 * DisplayDataObj.set_Error_max("+39.00");
	 * DisplayDataObj.setNoOfPulses("390");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU39_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus39.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus39.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU39_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU39();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU39_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU40_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus40.clear();
	 * try{
	 * serialDM_Obj.commLDU40.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID40();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate40();
	 * boolean status = serialDM_Obj.LDU40_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus40.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-40.00");
	 * DisplayDataObj.set_Error_max("+40.00");
	 * DisplayDataObj.setNoOfPulses("400");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU40_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus40.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus40.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU40_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU40();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU40_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU41_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus41.clear();
	 * try{
	 * serialDM_Obj.commLDU41.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID41();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate41();
	 * boolean status = serialDM_Obj.LDU41_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus41.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-41.00");
	 * DisplayDataObj.set_Error_max("+41.00");
	 * DisplayDataObj.setNoOfPulses("410");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU41_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus41.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus41.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU41_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU41();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU41_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU42_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus42.clear();
	 * try{
	 * serialDM_Obj.commLDU42.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID42();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate42();
	 * boolean status = serialDM_Obj.LDU42_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus42.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-42.00");
	 * DisplayDataObj.set_Error_max("+42.00");
	 * DisplayDataObj.setNoOfPulses("420");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU42_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus42.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus42.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU42_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU42();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU42_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU43_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus43.clear();
	 * try{
	 * serialDM_Obj.commLDU43.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID43();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate43();
	 * boolean status = serialDM_Obj.LDU43_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus43.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-43.00");
	 * DisplayDataObj.set_Error_max("+43.00");
	 * DisplayDataObj.setNoOfPulses("430");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU43_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus43.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus43.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU43_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU43();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU43_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU44_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus44.clear();
	 * try{
	 * serialDM_Obj.commLDU44.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID44();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate44();
	 * boolean status = serialDM_Obj.LDU44_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus44.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-44.00");
	 * DisplayDataObj.set_Error_max("+44.00");
	 * DisplayDataObj.setNoOfPulses("440");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU44_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus44.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus44.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU44_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU44();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU44_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU45_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus45.clear();
	 * try{
	 * serialDM_Obj.commLDU45.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID45();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate45();
	 * boolean status = serialDM_Obj.LDU45_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus45.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-45.00");
	 * DisplayDataObj.set_Error_max("+45.00");
	 * DisplayDataObj.setNoOfPulses("450");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU45_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus45.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus45.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU45_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU45();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU45_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU46_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus46.clear();
	 * try{
	 * serialDM_Obj.commLDU46.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID46();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate46();
	 * boolean status = serialDM_Obj.LDU46_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus46.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-46.00");
	 * DisplayDataObj.set_Error_max("+46.00");
	 * DisplayDataObj.setNoOfPulses("460");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU46_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus46.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus46.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU46_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU46();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU46_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU47_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus47.clear();
	 * try{
	 * serialDM_Obj.commLDU47.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID47();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate47();
	 * boolean status = serialDM_Obj.LDU47_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus47.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-47.00");
	 * DisplayDataObj.set_Error_max("+47.00");
	 * DisplayDataObj.setNoOfPulses("470");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU47_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus47.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus47.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU47_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU47();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU47_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lscsLDU48_ValidateSerialCmd(){
	 * String LDU_CommPortID= null;
	 * String LDUCommBaudRate = null;
	 * txtValidateLDU_CmdStatus48.clear();
	 * try{
	 * serialDM_Obj.commLDU48.searchForPorts();
	 * LDU_CommPortID = getCurrentLDU_ComPortID48();
	 * LDUCommBaudRate = getCurrentLDU_ComBaudRate48();
	 * boolean status = serialDM_Obj.LDU48_Init(LDU_CommPortID,LDUCommBaudRate);
	 * if (!status){
	 * 
	 * txtValidateLDU_CmdStatus48.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	 * 
	 * } else {
	 * setPortValidationTurnedON(true);
	 * DisplayDataObj.set_Error_min("-48.00");
	 * DisplayDataObj.set_Error_max("+48.00");
	 * DisplayDataObj.setNoOfPulses("480");
	 * 
	 * DisplayDataObj.setLDU_ReadDataFlag(true);
	 * //status = serialDM_Obj.LDU_ResetSetting();
	 * status = serialDM_Obj.lscsLDU48_CheckCom();
	 * 
	 * if (!status){
	 * txtValidateLDU_CmdStatus48.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	 * }else{
	 * txtValidateLDU_CmdStatus48.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	 * }
	 * setPortValidationTurnedON(false);
	 * DisplayDataObj.setLDU48_ReadDataFlag(false);
	 * }
	 * serialDM_Obj.DisconnectLDU48();
	 * }catch(Exception e){
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU48_ValidateSerialCmd: Exception"+e.
	 * getMessage());
	 * }
	 * 
	 * 
	 * }
	 */

	/*
	 * private String getCurrentPwrSrcComBaudRate() {
	 * 
	 * return
	 * cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentPwrSrcComPortID() {
	 * 
	 * 
	 * return cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentRefStdComBaudRate() {
	 * 
	 * return cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentRefStdComPortID() {
	 * 
	 * 
	 * return cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
	 * }
	 */

	private String getCurrentLDU_ComBaudRate1() {

		return cmbBxMegaOhmPm1_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID1() {

		return cmbBxMegaOhmPm1_PortSelection.getSelectionModel().getSelectedItem();
	}

	/*
	 * private String getCurrentLDU_ComBaudRate2() {
	 * 
	 * return cmbBxQr2_BaudRate.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID2() {
	 * 
	 * 
	 * return cmbBxQr2_PortSelection.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate3() {
	 * 
	 * return cmbBxLDU_BaudRate3.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID3() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection3.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate4() {
	 * 
	 * return cmbBxLDU_BaudRate4.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID4() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection4.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate5() {
	 * 
	 * return cmbBxLDU_BaudRate5.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID5() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection5.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate6() {
	 * 
	 * return cmbBxLDU_BaudRate6.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID6() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection6.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate7() {
	 * 
	 * return cmbBxLDU_BaudRate7.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID7() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection7.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate8() {
	 * 
	 * return cmbBxLDU_BaudRate8.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID8() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection8.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate9() {
	 * 
	 * return cmbBxLDU_BaudRate9.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID9() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection9.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate10() {
	 * 
	 * return cmbBxLDU_BaudRate10.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID10() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection10.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate11() {
	 * 
	 * return cmbBxLDU_BaudRate11.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID11() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection11.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate12() {
	 * 
	 * return cmbBxLDU_BaudRate12.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID12() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection12.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate13() {
	 * 
	 * return cmbBxLDU_BaudRate13.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID13() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection13.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate14() {
	 * 
	 * return cmbBxLDU_BaudRate14.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID14() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection14.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate15() {
	 * 
	 * return cmbBxLDU_BaudRate15.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID15() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection15.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate16() {
	 * 
	 * return cmbBxLDU_BaudRate16.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID16() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection16.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate17() {
	 * 
	 * return cmbBxLDU_BaudRate17.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID17() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection17.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate18() {
	 * 
	 * return cmbBxLDU_BaudRate18.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID18() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection18.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate19() {
	 * 
	 * return cmbBxLDU_BaudRate19.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID19() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection19.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate20() {
	 * 
	 * return cmbBxLDU_BaudRate20.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID20() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection20.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate21() {
	 * 
	 * return cmbBxLDU_BaudRate21.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID21() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection21.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate22() {
	 * 
	 * return cmbBxLDU_BaudRate22.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID22() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection22.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate23() {
	 * 
	 * return cmbBxLDU_BaudRate23.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID23() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection23.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate24() {
	 * 
	 * return cmbBxLDU_BaudRate24.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID24() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection24.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate25() {
	 * 
	 * return cmbBxLDU_BaudRate25.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID25() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection25.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate26() {
	 * 
	 * return cmbBxLDU_BaudRate26.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID26() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection26.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate27() {
	 * 
	 * return cmbBxLDU_BaudRate27.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID27() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection27.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate28() {
	 * 
	 * return cmbBxLDU_BaudRate28.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID28() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection28.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate29() {
	 * 
	 * return cmbBxLDU_BaudRate29.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID29() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection29.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate30() {
	 * 
	 * return cmbBxLDU_BaudRate30.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID30() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection30.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate31() {
	 * 
	 * return cmbBxLDU_BaudRate31.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID31() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection31.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate32() {
	 * 
	 * return cmbBxLDU_BaudRate32.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID32() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection32.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate33() {
	 * 
	 * return cmbBxLDU_BaudRate33.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID33() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection33.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate34() {
	 * 
	 * return cmbBxLDU_BaudRate34.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID34() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection34.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate35() {
	 * 
	 * return cmbBxLDU_BaudRate35.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID35() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection35.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate36() {
	 * 
	 * return cmbBxLDU_BaudRate36.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID36() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection36.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate37() {
	 * 
	 * return cmbBxLDU_BaudRate37.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID37() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection37.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate38() {
	 * 
	 * return cmbBxLDU_BaudRate38.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID38() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection38.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate39() {
	 * 
	 * return cmbBxLDU_BaudRate39.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID39() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection39.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate40() {
	 * 
	 * return cmbBxLDU_BaudRate40.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID40() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection40.getSelectionModel().getSelectedItem();
	 * }
	 * private String getCurrentLDU_ComBaudRate41() {
	 * 
	 * return cmbBxLDU_BaudRate41.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID41() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection41.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate42() {
	 * 
	 * return cmbBxLDU_BaudRate42.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID42() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection42.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate43() {
	 * 
	 * return cmbBxLDU_BaudRate43.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID43() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection43.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate44() {
	 * 
	 * return cmbBxLDU_BaudRate44.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID44() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection44.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate45() {
	 * 
	 * return cmbBxLDU_BaudRate45.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID45() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection45.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate46() {
	 * 
	 * return cmbBxLDU_BaudRate46.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID46() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection46.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * private String getCurrentLDU_ComBaudRate47() {
	 * 
	 * return cmbBxLDU_BaudRate47.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID47() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection47.getSelectionModel().getSelectedItem();
	 * }
	 * 
	 * 
	 * private String getCurrentLDU_ComBaudRate48() {
	 * 
	 * return cmbBxLDU_BaudRate48.getSelectionModel().getSelectedItem().toString();
	 * }
	 * 
	 * private String getCurrentLDU_ComPortID48() {
	 * 
	 * 
	 * return cmbBxLDU_PortSelection48.getSelectionModel().getSelectedItem();
	 * }
	 */

	/*
	 * public void PwrSrcValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("PwrSrcValidateSerialCmdTrigger: Invoked:");
	 * PwrSrcValidateTimer = new Timer();
	 * PwrSrcValidateTimer.schedule(new PwrSrcValidateTimerTask(),100);// 1000);
	 * 
	 * }
	 * 
	 * public void RefStdValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("RefStdValidateSerialCmdTrigger: Invoked:");
	 * RefStdValidateTimer = new Timer();
	 * RefStdValidateTimer.schedule(new RefStdValidateTimerTask(),100);// 1000);
	 * 
	 * }
	 */

	@FXML
	public void megaOhmPm1_ValidateSerialCmdTrigger() {
		ApplicationLauncher.logger.info("qr1_ValidateSerialCmdTrigger: Invoked:");
		megaOhmPm1_ValidateTimer = new Timer();
		megaOhmPm1_ValidateTimer.schedule(new MegaOhmPm1_ValidateTimerTask(), 100);// 1000);

	}

	/*
	 * @FXML
	 * public void qr2_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("qr2_ValidateSerialCmdTrigger: Invoked:");
	 * LDU2_ValidateTimer = new Timer();
	 * LDU2_ValidateTimer.schedule(new Qr2_ValidateTimerTask(),200);// 2000);
	 * }
	 * 
	 * @FXML
	 * public void LDU3_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU3_ValidateSerialCmdTrigger: Invoked:");
	 * LDU3_ValidateTimer = new Timer();
	 * //LDU3_ValidateTimer.schedule(new LDU3_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU4_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU4_ValidateSerialCmdTrigger: Invoked:");
	 * LDU4_ValidateTimer = new Timer();
	 * //LDU4_ValidateTimer.schedule(new LDU4_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU5_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU5_ValidateSerialCmdTrigger: Invoked:");
	 * LDU5_ValidateTimer = new Timer();
	 * //LDU5_ValidateTimer.schedule(new LDU5_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU6_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU6_ValidateSerialCmdTrigger: Invoked:");
	 * LDU6_ValidateTimer = new Timer();
	 * //LDU6_ValidateTimer.schedule(new LDU6_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU7_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU7_ValidateSerialCmdTrigger: Invoked:");
	 * LDU7_ValidateTimer = new Timer();
	 * //LDU7_ValidateTimer.schedule(new LDU7_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU8_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU8_ValidateSerialCmdTrigger: Invoked:");
	 * LDU8_ValidateTimer = new Timer();
	 * //LDU8_ValidateTimer.schedule(new LDU8_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU9_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU9_ValidateSerialCmdTrigger: Invoked:");
	 * LDU9_ValidateTimer = new Timer();
	 * //LDU9_ValidateTimer.schedule(new LDU9_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU10_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU10_ValidateSerialCmdTrigger: Invoked:");
	 * LDU10_ValidateTimer = new Timer();
	 * //LDU10_ValidateTimer.schedule(new LDU10_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU11_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU11_ValidateSerialCmdTrigger: Invoked:");
	 * LDU11_ValidateTimer = new Timer();
	 * //LDU11_ValidateTimer.schedule(new LDU11_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU12_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU12_ValidateSerialCmdTrigger: Invoked:");
	 * LDU12_ValidateTimer = new Timer();
	 * //LDU12_ValidateTimer.schedule(new LDU12_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU13_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU13_ValidateSerialCmdTrigger: Invoked:");
	 * LDU13_ValidateTimer = new Timer();
	 * //LDU13_ValidateTimer.schedule(new LDU13_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU14_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU14_ValidateSerialCmdTrigger: Invoked:");
	 * LDU14_ValidateTimer = new Timer();
	 * //LDU14_ValidateTimer.schedule(new LDU14_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU15_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU15_ValidateSerialCmdTrigger: Invoked:");
	 * LDU15_ValidateTimer = new Timer();
	 * //LDU15_ValidateTimer.schedule(new LDU15_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU16_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU16_ValidateSerialCmdTrigger: Invoked:");
	 * LDU16_ValidateTimer = new Timer();
	 * //LDU16_ValidateTimer.schedule(new LDU16_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU17_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU17_ValidateSerialCmdTrigger: Invoked:");
	 * LDU17_ValidateTimer = new Timer();
	 * //LDU17_ValidateTimer.schedule(new LDU17_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU18_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU18_ValidateSerialCmdTrigger: Invoked:");
	 * LDU18_ValidateTimer = new Timer();
	 * //LDU18_ValidateTimer.schedule(new LDU18_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU19_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU19_ValidateSerialCmdTrigger: Invoked:");
	 * LDU19_ValidateTimer = new Timer();
	 * //LDU19_ValidateTimer.schedule(new LDU19_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU20_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU20_ValidateSerialCmdTrigger: Invoked:");
	 * LDU20_ValidateTimer = new Timer();
	 * //LDU20_ValidateTimer.schedule(new LDU20_ValidateTimerTask(),100);// 1000);
	 * } @FXML
	 * public void LDU21_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU21_ValidateSerialCmdTrigger: Invoked:");
	 * LDU21_ValidateTimer = new Timer();
	 * //LDU21_ValidateTimer.schedule(new LDU21_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU22_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU22_ValidateSerialCmdTrigger: Invoked:");
	 * LDU22_ValidateTimer = new Timer();
	 * //LDU22_ValidateTimer.schedule(new LDU22_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * 
	 * @FXML
	 * public void LDU23_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU23_ValidateSerialCmdTrigger: Invoked:");
	 * LDU23_ValidateTimer = new Timer();
	 * //LDU23_ValidateTimer.schedule(new LDU23_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU24_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU24_ValidateSerialCmdTrigger: Invoked:");
	 * LDU24_ValidateTimer = new Timer();
	 * //LDU24_ValidateTimer.schedule(new LDU24_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU25_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU25_ValidateSerialCmdTrigger: Invoked:");
	 * LDU25_ValidateTimer = new Timer();
	 * //LDU25_ValidateTimer.schedule(new LDU25_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU26_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU26_ValidateSerialCmdTrigger: Invoked:");
	 * LDU26_ValidateTimer = new Timer();
	 * //LDU26_ValidateTimer.schedule(new LDU26_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU27_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU27_ValidateSerialCmdTrigger: Invoked:");
	 * LDU27_ValidateTimer = new Timer();
	 * //LDU27_ValidateTimer.schedule(new LDU27_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU28_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU28_ValidateSerialCmdTrigger: Invoked:");
	 * LDU28_ValidateTimer = new Timer();
	 * //LDU28_ValidateTimer.schedule(new LDU28_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU29_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU29_ValidateSerialCmdTrigger: Invoked:");
	 * LDU29_ValidateTimer = new Timer();
	 * //LDU29_ValidateTimer.schedule(new LDU29_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU30_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU30_ValidateSerialCmdTrigger: Invoked:");
	 * LDU30_ValidateTimer = new Timer();
	 * //LDU30_ValidateTimer.schedule(new LDU30_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU31_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU31_ValidateSerialCmdTrigger: Invoked:");
	 * LDU31_ValidateTimer = new Timer();
	 * //LDU31_ValidateTimer.schedule(new LDU31_ValidateTimerTask(),100);// 1000);
	 * } @FXML
	 * public void LDU32_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU32_ValidateSerialCmdTrigger: Invoked:");
	 * LDU32_ValidateTimer = new Timer();
	 * //LDU32_ValidateTimer.schedule(new LDU32_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU33_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU33_ValidateSerialCmdTrigger: Invoked:");
	 * LDU33_ValidateTimer = new Timer();
	 * //LDU33_ValidateTimer.schedule(new LDU33_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU34_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU34_ValidateSerialCmdTrigger: Invoked:");
	 * LDU34_ValidateTimer = new Timer();
	 * //LDU34_ValidateTimer.schedule(new LDU34_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU35_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU35_ValidateSerialCmdTrigger: Invoked:");
	 * LDU35_ValidateTimer = new Timer();
	 * //LDU35_ValidateTimer.schedule(new LDU35_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU36_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU36_ValidateSerialCmdTrigger: Invoked:");
	 * LDU36_ValidateTimer = new Timer();
	 * //LDU36_ValidateTimer.schedule(new LDU36_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU37_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU37_ValidateSerialCmdTrigger: Invoked:");
	 * LDU37_ValidateTimer = new Timer();
	 * //LDU37_ValidateTimer.schedule(new LDU37_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU38_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU38_ValidateSerialCmdTrigger: Invoked:");
	 * LDU38_ValidateTimer = new Timer();
	 * //LDU38_ValidateTimer.schedule(new LDU38_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU39_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU39_ValidateSerialCmdTrigger: Invoked:");
	 * LDU39_ValidateTimer = new Timer();
	 * //LDU39_ValidateTimer.schedule(new LDU39_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU40_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU40_ValidateSerialCmdTrigger: Invoked:");
	 * LDU40_ValidateTimer = new Timer();
	 * //LDU40_ValidateTimer.schedule(new LDU40_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU41_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU41_ValidateSerialCmdTrigger: Invoked:");
	 * LDU41_ValidateTimer = new Timer();
	 * //LDU41_ValidateTimer.schedule(new LDU41_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU42_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU42_ValidateSerialCmdTrigger: Invoked:");
	 * LDU42_ValidateTimer = new Timer();
	 * //LDU42_ValidateTimer.schedule(new LDU42_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU43_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU43_ValidateSerialCmdTrigger: Invoked:");
	 * LDU43_ValidateTimer = new Timer();
	 * //LDU43_ValidateTimer.schedule(new LDU43_ValidateTimerTask(),100);// 1000);
	 * } @FXML
	 * public void LDU44_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU44_ValidateSerialCmdTrigger: Invoked:");
	 * LDU44_ValidateTimer = new Timer();
	 * //LDU44_ValidateTimer.schedule(new LDU44_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU45_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU45_ValidateSerialCmdTrigger: Invoked:");
	 * LDU45_ValidateTimer = new Timer();
	 * //LDU45_ValidateTimer.schedule(new LDU45_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU46_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU46_ValidateSerialCmdTrigger: Invoked:");
	 * LDU46_ValidateTimer = new Timer();
	 * //LDU46_ValidateTimer.schedule(new LDU46_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU47_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU47_ValidateSerialCmdTrigger: Invoked:");
	 * LDU47_ValidateTimer = new Timer();
	 * //LDU47_ValidateTimer.schedule(new LDU47_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * @FXML
	 * public void LDU48_ValidateSerialCmdTrigger(){
	 * ApplicationLauncher.logger.info("LDU48_ValidateSerialCmdTrigger: Invoked:");
	 * LDU48_ValidateTimer = new Timer();
	 * //LDU48_ValidateTimer.schedule(new LDU48_ValidateTimerTask(),100);// 1000);
	 * }
	 * 
	 * 
	 * 
	 * 
	 * class PwrSrcValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidatePwrSrcCmd.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: WAIT");
	 * try {
	 * PwrSrcValidateSerialCmd();
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("PwrSrcValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * PwrSrcValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: DEFAULT");
	 * btnValidatePwrSrcCmd.setDisable(false);
	 * }
	 * }
	 * 
	 * class RefStdValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateRefStdCmd.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("RefStdValidateTimerTask: WAIT");
	 * try {
	 * 
	 * RefStdValidateSerialCmd();
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("RefStdValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * RefStdValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("RefStdValidateTimerTask: DEFAULT");
	 * btnValidateRefStdCmd.setDisable(false);
	 * }
	 * }
	 */

	class MegaOhmPm1_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateMegaOhmPm1_Cmd.setDisable(true);
			WindowManager.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("MegaOhmPm1_ValidateTimerTask: WAIT");
			try {
				/*
				 * if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED){
				 * LDU_ValidateSerialCmd();
				 * } else if (ProcalFeatureEnable.LSCS_LDU_CONNECTED){
				 * lscsLDU1_ValidateSerialCmd();
				 * }
				 */
				megaOhmPm1_ValidateSerialCmd();

			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("MegaOhmPm1_ValidateTimerTask: Exception:" + e.getMessage());
			}
			megaOhmPm1_ValidateTimer.cancel();
			WindowManager.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("MegaOhmPm1_ValidateTimerTask: DEFAULT");
			ref_btnValidateMegaOhmPm1_Cmd.setDisable(false);
		}
	}

	/*
	 * class Qr2_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateQr_Cmd2.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU2_ValidateTimerTask: WAIT");
	 * try {
	 * qr2_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU2_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU2_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU2_ValidateTimerTask: DEFAULT");
	 * btnValidateQr_Cmd2.setDisable(false);
	 * }
	 * }
	 */
	/*
	 * class LDU3_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd3.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU3_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU3_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU3_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU3_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU3_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd3.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU4_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd4.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU4_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU4_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU4_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU4_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU4_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd4.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU5_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd5.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU5_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU5_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU5_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU5_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU5_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd5.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU6_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd6.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU6_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU6_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU6_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU6_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU6_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd6.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU7_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd7.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU7_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU7_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU7_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU7_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU7_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd7.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU8_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd8.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU8_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU8_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU8_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU8_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU8_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd8.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU9_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd9.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU9_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU9_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU9_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU9_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU9_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd9.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU10_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd10.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU10_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU10_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU10_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU10_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU10_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd10.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU11_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd11.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU11_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU11_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU11_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU11_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU11_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd11.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU12_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd12.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU12_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU12_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU12_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU12_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU12_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd12.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU13_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd13.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU13_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU13_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU13_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU13_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU13_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd13.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU14_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd14.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU14_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU14_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU14_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU14_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU14_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd14.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU15_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd15.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU15_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU15_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU15_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU15_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU15_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd15.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU16_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd16.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU16_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU16_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU16_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU16_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU16_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd16.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU17_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd17.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU17_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU17_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU17_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU17_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU17_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd17.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU18_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd18.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU18_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU18_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU18_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU18_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU18_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd18.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU19_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd19.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU19_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU19_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU19_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU19_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU19_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd19.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU20_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd20.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU20_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU20_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU20_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU20_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU20_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd20.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU21_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd21.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU21_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU21_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU21_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU21_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU21_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd21.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU22_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd22.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU22_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU22_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU22_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU22_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU22_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd22.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU23_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd23.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU23_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU23_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU23_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU23_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU23_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd23.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU24_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd24.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU24_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU24_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU24_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU24_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU24_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd24.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU25_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd25.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU25_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU25_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU25_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU25_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU25_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd25.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU26_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd26.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU26_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU26_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU26_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU26_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU26_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd26.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU27_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd27.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU27_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU27_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU27_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU27_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU27_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd27.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU28_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd28.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU28_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU28_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU28_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU28_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU28_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd28.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU29_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd29.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU29_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU29_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU29_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU29_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU29_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd29.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU30_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd30.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU30_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU30_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU30_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU30_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU30_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd30.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU31_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd31.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU31_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU31_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU31_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU31_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU31_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd31.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU32_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd32.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU32_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU32_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU32_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU32_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU32_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd32.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU33_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd33.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU33_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU33_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU33_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU33_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU33_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd33.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU34_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd34.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU34_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU34_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU34_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU34_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU34_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd34.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU35_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd35.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU35_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU35_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU35_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU35_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU35_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd35.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU36_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd36.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU36_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU36_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU36_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU36_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU36_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd36.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU37_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd37.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU37_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU37_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU37_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU37_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU37_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd37.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU38_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd38.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU38_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU38_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU38_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU38_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU38_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd38.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU39_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd39.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU39_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU39_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU39_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU39_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU39_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd39.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU40_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd40.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU40_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU40_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU40_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU40_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU40_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd40.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU41_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd41.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU41_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU41_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU41_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU41_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU41_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd41.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU42_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd42.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU42_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU42_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU42_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU42_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU42_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd42.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU43_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd43.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU43_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU43_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU43_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU43_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU43_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd43.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU44_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd44.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU44_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU44_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU44_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU44_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU44_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd44.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU45_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd45.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU45_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU45_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU45_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU45_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU45_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd45.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU46_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd46.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU46_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU46_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU46_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU46_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU46_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd46.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU47_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd47.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU47_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU47_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU47_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU47_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU47_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd47.setDisable(false);
	 * }
	 * }
	 * 
	 * class LDU48_ValidateTimerTask extends TimerTask {
	 * public void run() {
	 * btnValidateLDU_Cmd48.setDisable(true);
	 * WindowManager.setCursor(Cursor.WAIT);
	 * ApplicationLauncher.logger.info("LDU48_ValidateTimerTask: WAIT");
	 * try {
	 * lscsLDU48_ValidateSerialCmd();
	 * 
	 * } catch (Exception e) {
	 * 
	 * e.printStackTrace();
	 * ApplicationLauncher.logger.error("LDU48_ValidateTimerTask: Exception:"+e.
	 * getMessage());
	 * }
	 * LDU48_ValidateTimer.cancel();
	 * WindowManager.setCursor(Cursor.DEFAULT);
	 * ApplicationLauncher.logger.info("LDU48_ValidateTimerTask: DEFAULT");
	 * btnValidateLDU_Cmd48.setDisable(false);
	 * }
	 * }
	 */

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
	public void cmbBxMegaOhmPm1BaySelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
		megaOhmPm1BaySelectionOnChangeTimer = new Timer();
		megaOhmPm1BaySelectionOnChangeTimer.schedule(new MegaOhmPm1BaySelectionOnChangeTask(), 10);
	}

	@FXML
	public void cmbBxMegaOhmPm1ClusterSelectionOnChange() {
		ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
		megaOhmPm1ClusterSelectionOnChangeTimer = new Timer();
		megaOhmPm1ClusterSelectionOnChangeTimer.schedule(new MegaOhmPm1ClusterSelectionOnChangeTask(), 10);
	}

	// ref_cmbBxMegaOhmPmClusterId1;
	// ref_cmbBxMegaOhmPmBayId1;

	class MegaOhmPm1ClusterSelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel()
						.getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					ref_cmbBxMegaOhmPm1BayId.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					if (getClusterBayNameListMap().containsKey(selectedClusterName)) {
						ref_cmbBxMegaOhmPm1BayId.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					}
					ref_cmbBxMegaOhmPm1BayId.getSelectionModel().select(0);

				}
			});
			megaOhmPm1ClusterSelectionOnChangeTimer.cancel();

		}
	}

	class MegaOhmPm1BaySelectionOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {

				String selectedClusterName = (String) ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel()
						.getSelectedItem();
				String selectedBayName = (String) ref_cmbBxMegaOhmPm1BayId.getSelectionModel().getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					// ref_tbViewOutputPortData.getItems().clear();
					// ref_tbViewInputPortData.getItems().clear();
					ref_cmbBxMegaOhmPm1PositionId.getItems().clear();
					// ref_cmbBxMegaOhmPm1DeviceAddress.getItems().clear();
					// ApplicationLauncher.logger.debug("cmbBxMegaOhmPm1PositionIdOnChange:
					// selectedClusterName : " +selectedClusterName);
					// ApplicationLauncher.logger.debug("cmbBxMegaOhmPm1PositionIdOnChange:
					// selectedBayName : " +selectedBayName);
					// ApplicationLauncher.logger.debug("cmbBxMegaOhmPm1PositionIdOnChange:
					// getClusterBayNamePositionListMap : " +
					// getClusterBayNamePositionListMap().get(selectedClusterName +
					// "_"+selectedBayName));
					if (getClusterBayNamePositionListMap().containsKey(selectedClusterName + "_" + selectedBayName)) {
						ref_cmbBxMegaOhmPm1PositionId.getItems().addAll(
								getClusterBayNamePositionListMap().get(selectedClusterName + "_" + selectedBayName));
						ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().select(0);
						String selectionPositionNo = (String) ref_cmbBxMegaOhmPm1PositionId.getSelectionModel()
								.getSelectedItem();
						if (getClusterBayPositionNoCnameMap()
								.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo)) {
							ref_txtMegaOhmPm1Cname.setText(getClusterBayPositionNoCnameMap()
									.get(selectedClusterName + "_" + selectedBayName + "_" + selectionPositionNo));
						}

						/*
						 * String positionNo =
						 * ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().getSelectedItem();
						 * if(getClusterBayPositionNoAddressListMap().containsKey(selectedClusterName+
						 * "_"+selectedBayName + "_"+ positionNo)) {
						 * ref_cmbBxMegaOhmPm1DeviceAddress.getItems().addAll(
						 * getClusterBayPositionNoAddressListMap().get(selectedClusterName+"_"+
						 * selectedBayName + "_"+ positionNo));
						 * ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().select(0);
						 * }
						 */
					} else {
						ref_txtMegaOhmPm1Cname.setText("");
					}
					// if(getClusterBayNameListMap().containsKey(selectedClusterName)){
					// ref_cmbBxMegaOhmPmPositionId1.getItems().addAll(getClusterBayNameListMap().get(selectedClusterName));
					// }
					// ref_cmbBxMegaOhmPmPositionId1.getSelectionModel().select(0);

				}
			});
			megaOhmPm1BaySelectionOnChangeTimer.cancel();

		}
	}

	@FXML
	public void cmbBxMegaOhmPm1PositionIdOnChange() {
		ApplicationLauncher.logger.debug("cmbBxMegaOhmPm1PositionIdOnChange: Entry");
		megaOhmPm1PositionIdOnChangeTimer = new Timer();
		megaOhmPm1PositionIdOnChangeTimer.schedule(new MegaOhmPm1PositionIdOnChangeTask(), 10);
	}

	// ref_cmbBxMegaOhmPmClusterId1;
	// ref_cmbBxMegaOhmPmBayId1;

	class MegaOhmPm1PositionIdOnChangeTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				String selectedClusterName = (String) ref_cmbBxMegaOhmPm1ClusterId.getSelectionModel()
						.getSelectedItem();
				if (getClusterBayNameListMap().size() > 0) {
					ref_cmbBxMegaOhmPm1DeviceAddress.getItems().clear();
					/*
					 * ref_tbViewOutputPortData.getItems().clear();
					 * ref_tbViewInputPortData.getItems().clear();
					 */
					// ref_cmbBxMegaOhmPmBayId1.getItems().clear();
					/*
					 * ref_txtClusterIpAddress.setText("");
					 * ref_txtClusterPortNo.setText("");
					 */
					String selectedBayName = (String) ref_cmbBxMegaOhmPm1BayId.getSelectionModel().getSelectedItem();
					String selectedPositionNo = (String) cmbBxMegaOhmPm1PositionId.getSelectionModel()
							.getSelectedItem();
					if (getClusterBayPositionNoCnameMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_txtMegaOhmPm1Cname.setText(getClusterBayPositionNoCnameMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
					}
					// String positionNo =
					// ref_cmbBxMegaOhmPm1PositionId.getSelectionModel().getSelectedItem();
					if (getClusterBayPositionNoAddressListMap()
							.containsKey(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo)) {
						ref_cmbBxMegaOhmPm1DeviceAddress.getItems().addAll(getClusterBayPositionNoAddressListMap()
								.get(selectedClusterName + "_" + selectedBayName + "_" + selectedPositionNo));
						ref_cmbBxMegaOhmPm1DeviceAddress.getSelectionModel().select(0);
					}

				}
			});
			megaOhmPm1PositionIdOnChangeTimer.cancel();

		}
	}

	/*
	 * @FXML
	 * public void cmbBxQr2BaySelectionOnChange(){
	 * ApplicationLauncher.logger.debug("cmbBxBaySelectionOnChange: Entry");
	 * qr2BaySelectionOnChangeTimer = new Timer();
	 * qr2BaySelectionOnChangeTimer.schedule(new Qr2BaySelectionOnChangeTask(),10);
	 * }
	 * 
	 * @FXML
	 * public void cmbBxQr2ClusterSelectionOnChange(){
	 * ApplicationLauncher.logger.debug("cmbBxClusterSelectionOnChange: Entry");
	 * qr2ClusterSelectionOnChangeTimer = new Timer();
	 * qr2ClusterSelectionOnChangeTimer.schedule(new
	 * Qr2ClusterSelectionOnChangeTask(),10);
	 * }
	 * 
	 * // ref_cmbBxQrClusterId1;
	 * //ref_cmbBxQrBayId1;
	 * 
	 * class Qr2ClusterSelectionOnChangeTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * String selectedClusterName =
	 * (String)ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
	 * if(getClusterBayNameListMap().size()>0){
	 * ref_tbViewOutputPortData.getItems().clear();
	 * ref_tbViewInputPortData.getItems().clear();
	 * ref_cmbBxQr2BayId.getItems().clear();
	 * ref_txtClusterIpAddress.setText("");
	 * ref_txtClusterPortNo.setText("");
	 * if(getClusterBayNameListMap().containsKey(selectedClusterName)){
	 * ref_cmbBxQr2BayId.getItems().addAll(getClusterBayNameListMap().get(
	 * selectedClusterName));
	 * }
	 * ref_cmbBxQr2BayId.getSelectionModel().select(0);
	 * 
	 * }
	 * });
	 * qr2ClusterSelectionOnChangeTimer.cancel();
	 * 
	 * 
	 * 
	 * 
	 * }
	 * }
	 * 
	 * class Qr2BaySelectionOnChangeTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * 
	 * String selectedClusterName =
	 * (String)ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
	 * String selectedBayName =
	 * (String)ref_cmbBxQr2BayId.getSelectionModel().getSelectedItem();
	 * if(getClusterBayNameListMap().size()>0){
	 * //ref_tbViewOutputPortData.getItems().clear();
	 * //ref_tbViewInputPortData.getItems().clear();
	 * ref_cmbBxQr2PositionId.getItems().clear();
	 * if(getClusterBayNamePositionListMap().containsKey(selectedClusterName +
	 * "_"+selectedBayName)) {
	 * ref_cmbBxQr2PositionId.getItems().addAll(getClusterBayNamePositionListMap().
	 * get(selectedClusterName + "_"+selectedBayName));
	 * ref_cmbBxQr2PositionId.getSelectionModel().select(0);
	 * String selectionPositionNo = (String)
	 * ref_cmbBxQr2PositionId.getSelectionModel().getSelectedItem();
	 * if(getClusterBayPositionNoCnameMap().containsKey(selectedClusterName +
	 * "_"+selectedBayName+"_"+selectionPositionNo)) {
	 * ref_txtQr2Cname.setText(getClusterBayPositionNoCnameMap().get(
	 * selectedClusterName + "_"+selectedBayName+"_"+selectionPositionNo));
	 * }
	 * }else {
	 * ref_txtQr2Cname.setText("");
	 * }
	 * //if(getClusterBayNameListMap().containsKey(selectedClusterName)){
	 * // ref_cmbBxQrPositionId1.getItems().addAll(getClusterBayNameListMap().get(
	 * selectedClusterName));
	 * //}
	 * //ref_cmbBxQrPositionId1.getSelectionModel().select(0);
	 * 
	 * }
	 * });
	 * qr2BaySelectionOnChangeTimer.cancel();
	 * 
	 * 
	 * 
	 * 
	 * }
	 * }
	 * 
	 * 
	 * 
	 * 
	 * @FXML
	 * public void cmbBxQr2PositionIdOnChange(){
	 * ApplicationLauncher.logger.debug("cmbBxQr2PositionIdOnChange: Entry");
	 * qr2PositionIdOnChangeTimer = new Timer();
	 * qr2PositionIdOnChangeTimer.schedule(new Qr2PositionIdOnChangeTask(),10);
	 * }
	 * 
	 * // ref_cmbBxQrClusterId1;
	 * //ref_cmbBxQrBayId1;
	 * 
	 * class Qr2PositionIdOnChangeTask extends TimerTask {
	 * public void run() {
	 * Platform.runLater(()->{
	 * String selectedClusterName =
	 * (String)ref_cmbBxQr2ClusterId.getSelectionModel().getSelectedItem();
	 * if(getClusterBayNameListMap().size()>0){
	 * ref_tbViewOutputPortData.getItems().clear();
	 * ref_tbViewInputPortData.getItems().clear();
	 * //ref_cmbBxQrBayId1.getItems().clear();
	 * ref_txtClusterIpAddress.setText("");
	 * ref_txtClusterPortNo.setText("");
	 * String selectedBayName =
	 * (String)ref_cmbBxQr2BayId.getSelectionModel().getSelectedItem();
	 * String selectedPositionNo =
	 * (String)cmbBxQr2PositionId.getSelectionModel().getSelectedItem();
	 * if(getClusterBayPositionNoCnameMap().containsKey(selectedClusterName +
	 * "_"+selectedBayName+"_"+selectedPositionNo)) {
	 * ref_txtQr2Cname.setText(getClusterBayPositionNoCnameMap().get(
	 * selectedClusterName + "_"+selectedBayName+"_"+selectedPositionNo));
	 * }
	 * 
	 * }
	 * });
	 * qr2PositionIdOnChangeTimer.cancel();
	 * 
	 * 
	 * 
	 * 
	 * }
	 * }
	 */

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
		MegaOhmPmPortSetupController.bayConfigModel = bayConfigModel;
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

	/*
	 * public Map<String, String> getClusterBayPositionNoAddressMap() {
	 * return clusterBayPositionNoAddressMap;
	 * }
	 * 
	 * public void setClusterBayPositionNoAddressMap(Map<String, String>
	 * clusterBayPositionNoAddressMap) {
	 * this.clusterBayPositionNoAddressMap = clusterBayPositionNoAddressMap;
	 * }
	 */

}
