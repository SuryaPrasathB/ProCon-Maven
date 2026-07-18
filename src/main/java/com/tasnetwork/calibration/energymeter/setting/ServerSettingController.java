package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ResourceBundle;
import org.json.JSONException;
import org.json.JSONObject;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ServerProperties;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;

import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ServerSettingController implements Initializable {

	/*
	 * private int scanDeviceTimerTimeOutInSec = 180;
	 * private int scanDeviceTimerCounter = 0;
	 * static Timer scanDeviceTimer;
	 * static Timer bootupStatusTimer;
	 * TimerTask bootupStatusTimerTask;
	 * public static boolean bootupStatusTimerAlreadyStarted = false;
	 */

	@FXML
	private ComboBox<Integer> cmbBxGUI_RefreshFreq;
	@FXML
	private ComboBox<String> cmbBxTimeZoneList;
	public static ComboBox<String> ref_cmbBxTimeZoneList;

	@FXML
	private TextField txtHttpProtocol;
	@FXML
	private TextField txtServerIP;
	@FXML
	private TextField txtServerPort;

	@FXML
	private TextField txtAppVersion;
	@FXML
	private TextField txtServerVersion;
	private static TextField ref_txtServerVersion;
	@FXML
	private TextField txtServerSerialPort1;
	private static TextField ref_txtServerSerialPort1;
	@FXML
	private TextField txtServerSerialPort2;
	private static TextField ref_txtServerSerialPort2;
	@FXML
	private TextField txtCurrentServerIP;

	@FXML
	private Button btnGetFirmwareVersion;
	@FXML
	private Button btnGetServerSerialStatus;
	@FXML
	private Button btnGetServerTimeZone;
	@FXML
	private Button btnSetServerTimeZone;

	@FXML
	private Button btnScanDevice;
	public static Button ref_btnScanDevice;

	public static String rootUrl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ref_txtServerVersion = txtServerVersion;
		ref_txtServerSerialPort1 = txtServerSerialPort1;
		ref_txtServerSerialPort2 = txtServerSerialPort2;
		ref_cmbBxTimeZoneList = cmbBxTimeZoneList;
		ref_btnScanDevice = btnScanDevice;

		setupGUI_RefreshFreqData();
		setupSystemAppVersion();

		cmbBxGUI_RefreshFreq.setDisable(true);
		// WindowManager.setCursor(Cursor.DEFAULT);
	}

	public void setupSystemAppVersion() {
		txtAppVersion.setText(ConstantVersion.APPLICATION_VERSION);
	}

	public void getServerFirmwareVersion() {
		AsyncClientManager httpclientManager = new AsyncClientManager();
		httpclientManager.getServerFirmwareVersion();
	}

	public void getServerSerialStatus() {
		AsyncClientManager httpclientManager = new AsyncClientManager();
		httpclientManager.getServerSerialStatus();
	}

	public void getServerTimeZoneList() {
		AsyncClientManager httpclientManager = new AsyncClientManager();
		httpclientManager.getServerTimeZoneList();
	}

	public void setTimeZoneOnServer() {
		String SelectedTimeZone = ref_cmbBxTimeZoneList.getSelectionModel().getSelectedItem();
		AsyncClientManager httpclientManager = new AsyncClientManager();
		httpclientManager.setTimeZoneOnServer(SelectedTimeZone);

	}

	public static void UpdateDisplayServerTimeZoneList(String[] value) {
		ref_cmbBxTimeZoneList.getItems().setAll(value);
		Platform.runLater(() -> {
			ref_cmbBxTimeZoneList.getSelectionModel().select(0);
		});

	}

	public static void UpdateDisplaySerialPortPort1(String value) {
		ref_txtServerSerialPort1.setText(value);
	}

	public static void UpdateDisplaySerialPortPort2(String value1) {
		ref_txtServerSerialPort2.setText(value1);
	}

	public static void UpdateDisplayServerFirmwareVersion(String value1) {
		ref_txtServerVersion.setText(value1);
	}

	public void setupGUI_RefreshFreqData() {
		for (int i = 2; i < 60; i++) {
			cmbBxGUI_RefreshFreq.getItems().add(i);
		}

		cmbBxGUI_RefreshFreq.setValue(3);
	}

	public void load_saved_server_settingsToGUI() {

		JSONObject servetSetting = MySQL_Controller.sp_get_server_setting();
		try {
			if (servetSetting.has("http_protocol")) {
				txtHttpProtocol.setText(servetSetting.getString("http_protocol"));
				// ServerProperties.HTTP_Protocol=servetSetting.getString("http_protocol");
			} else {

				txtHttpProtocol.setText("");
				ApplicationLauncher.logger
						.info("load_saved_server_settingsToGUI: http_protocol: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:" + e.getMessage());
			txtHttpProtocol.setText("");
			ApplicationLauncher.logger
					.info("load_saved_server_settingsToGUI: http_protocol: Data not retrieved from database");
		}

		try {
			if (servetSetting.has("server_ip")) {
				txtServerIP.setText(servetSetting.getString("server_ip"));
				// ServerProperties.PublicURL_Id = servetSetting.getString("server_ip");
			} else {

				txtServerIP.setText("");
				ApplicationLauncher.logger
						.info("load_saved_server_settingsToGUI: server_ip: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:" + e.getMessage());
			txtServerIP.setText("");
			ApplicationLauncher.logger
					.info("load_saved_server_settingsToGUI: server_ip: Data not retrieved from database");
		}

		try {
			if (servetSetting.has("server_port")) {
				txtServerPort.setText(servetSetting.getString("server_port"));
				// ServerProperties.URLPort = servetSetting.getString("server_port");
			} else {

				txtServerPort.setText("");
				ApplicationLauncher.logger
						.info("load_saved_server_settingsToGUI: server_port: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:" + e.getMessage());
			txtServerPort.setText("");
			ApplicationLauncher.logger
					.info("load_saved_server_settingsToGUI: server_port: Data not retrieved from database");
		}

		try {
			if (servetSetting.has("refresh_gui_freq")) {
				cmbBxGUI_RefreshFreq.setValue(servetSetting.getInt("refresh_gui_freq"));
				// ServerProperties.RefreshGUI_Freq = servetSetting.getInt("refresh_gui_freq");
			} else {

				cmbBxGUI_RefreshFreq.setValue(3);
				ApplicationLauncher.logger
						.info("load_saved_server_settingsToGUI: refresh_gui_freq: Data not retrieved from DB");

			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:" + e.getMessage());
			cmbBxGUI_RefreshFreq.setValue(3);
			ApplicationLauncher.logger
					.info("load_saved_server_settingsToGUI: refresh_gui_freq: Data not retrieved from database");
		}

	}

	public static String getRootUrl() {
		return rootUrl;
	}

	public static void setRootUrl() {
		rootUrl = ServerProperties.HTTP_Protocol +
				ServerProperties.PublicURL_Id +
				ServerProperties.EndURL + ":" +
				ServerProperties.URLPort;

	}

	public void SaveOnClick() {
		String serverHttpProtocol = txtHttpProtocol.getText();
		String serverIP = txtServerIP.getText();
		String serverPort = txtServerPort.getText();
		int refreshGUI_Frequency = cmbBxGUI_RefreshFreq.getSelectionModel().getSelectedItem();

		MySQL_Controller.sp_add_server_settings(serverHttpProtocol, serverIP, serverPort, refreshGUI_Frequency);
		AsyncClientManager.load_saved_server_settings();
		setRootUrl();
		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}
}
