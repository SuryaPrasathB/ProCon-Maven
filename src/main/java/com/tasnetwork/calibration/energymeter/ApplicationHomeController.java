package com.tasnetwork.calibration.energymeter;

//import java.awt.Color;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.glass.ui.Screen;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.dashboard.DashboardController;
import com.tasnetwork.calibration.conveyor.device.ConveyorDataManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ApplicationHomeController implements Initializable {

	public static Stage lscsSourceCalibrationStage = new Stage();

	private static boolean uacTestRunScreenDisplayEnabled = true;
	private static boolean uacDeployScreenDisplayEnabled = true;
	private static boolean uacProjectScreenDisplayEnabled = true;
	private static boolean uacReportScreenDisplayEnabled = true;
	private static boolean uacMeterProfileScreenDisplayEnabled = true;
	private static boolean uacSettingScreenDisplayEnabled = true;
	private static boolean uacManualModeScreenDisplayEnabled = true;

	// private static boolean uacPlcScreenDisplayEnabled = true;
	private static boolean uacAdminScreenDisplayEnabled = true;

	private static boolean uacDeviceSettingsScreenDisplayEnabled = true;
	private static boolean uacStabilitySettingsScreenDisplayEnabled = true;
	private static boolean uacReportConfigurationScreenDisplayEnabled = true;
	private static boolean uacBackupSettingsScreenDisplayEnabled = true;

	public static boolean calibrationStageAlreadyLoaded = false;

	private static boolean proPowerSrcOnly_BootupConfigStateEnabled = false;

	@FXML
	private AnchorPane childPane;
	private static AnchorPane ref_childPane;

	@FXML
	private Label lbl_Devices;

	@FXML
	private Label statusLabel;
	public static Label ref_statusLabel;

	/*
	 * @FXML
	 * private Label lbl_TestSetup;
	 */

	@FXML
	private Label navigationLabel;

	@FXML
	private Label versionLabel;
	public static Label ref_navigationLabel;

	@FXML
	private Label labelBottomtSecStatus;

	public static Label ref_labelBottomtSecStatus;

	@FXML
	private Label lbl_Report;

	@FXML
	private Label lbl_EM_Model;

	@FXML
	private Label lbl_Debug;

	@FXML
	private Label lbl_Dashboard;

	@FXML
	private VBox vbox_dashboard;

	@FXML
	private VBox vboxDebug;
	private static VBox ref_vboxDebug;

	@FXML
	private VBox vbox_report;
	private static VBox ref_vbox_report;

	@FXML
	private VBox vbox_metertype;
	private static VBox ref_vbox_metertype;

	@FXML
	private VBox vbox_system_config;
	private static VBox ref_vbox_system_config;

	@FXML
	private ComboBox<Integer> cmb_power_source;
	@FXML
	private ComboBox<Integer> cmb_reference_std;

	@FXML
	private ComboBox<Integer> cmb_LDU;
	@FXML
	private AnchorPane meterpane;
	@FXML
	private BorderPane borderpane;

	public static StringProperty DisplayExecuteProcalStatus1 = new SimpleStringProperty();
	public static StringProperty DisplayExecuteSecondaryStatus = new SimpleStringProperty();
	private static Boolean InstantMetricsGUI_Displayed = false;
	private static Boolean lduAllDataViewGUI_Displayed = false;

	private static Boolean maintenanceGUI_Displayed = false;

	public static String HIGHLIGHT_COLOUR_RED = "#FF0000";
	public static String HIGHLIGHT_COLOUR_BLACK = "#000000";

	public static Object busyLoadingFXMLNode;

	@FXML
	private void onDeviceClickAction() throws IOException {

		ApplicationLauncher.logger.info("You clicked Devices Icon!");

		if (lbl_Dashboard != null)
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_RED));
		lbl_Report.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_EM_Model.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Debug.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));

		unloadChildNodeFXML();
		Parent nodeFromFXML = getNodeFromFXML("/fxml/setting/SystemSetting" + ConstantApp.THEME_FXML);

		update_left_status("Devices", ConstantApp.LEFT_STATUS_DEBUG);
		childPane.getChildren().add(nodeFromFXML);
	}

	public static void update_labelBootupStatus(String value) {
		ApplicationLauncher.logger.debug("update_labelBootupStatus: Entry");
		Platform.runLater(() -> {
			// DisplayBootupStatus.setValue(value);
			// ref_labelBootupStatus.setText(value);
		});

	}

	@FXML
	private void onDashboardClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Dashboard Icon!");

		if (lbl_Dashboard != null) {
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_RED));
			lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_Report.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_EM_Model.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_Debug.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		}

		update_left_status("Dashboard", ConstantApp.LEFT_STATUS_DEBUG);
		unloadChildNodeFXML();
		Parent nodeFromFXML = getNodeFromFXML("/fxml/conveyor/Dashboard_v1_5_W.fxml");
		childPane.getChildren().add(nodeFromFXML);
	}

	@FXML
	private void onDebugClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Debug Icon!");

		if (lbl_Dashboard != null) {
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_Report.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_EM_Model.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
			lbl_Debug.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		}

		update_left_status("Dashboard", ConstantApp.LEFT_STATUS_DEBUG);
		unloadChildNodeFXML();
		Parent nodeFromFXML = getNodeFromFXML("/fxml/conveyor/PalletTracker_W.fxml");
		childPane.getChildren().add(nodeFromFXML);
	}

	@FXML
	private void onReportsClickAction() throws IOException {

		ApplicationLauncher.logger.info("You clicked Report Icon!");

		if (lbl_Dashboard != null)
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Report.setTextFill(Color.web(HIGHLIGHT_COLOUR_RED));
		lbl_EM_Model.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Debug.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));

		unloadChildNodeFXML();
		/*
		 * if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
		 * Parent nodeFromFXML = getNodeFromFXML("/fxml/calib/lscsCalibration" +
		 * ConstantApp.THEME_FXML);
		 * childPane.getChildren().add(nodeFromFXML);
		 * update_left_status("Calibration Mode",ConstantApp.LEFT_STATUS_DEBUG);
		 * }else{
		 */
		Parent nodeFromFXML = getNodeFromFXML("/fxml/testreport/Results" + ConstantApp.THEME_FXML);
		// Parent nodeFromFXML = getNodeFromFXML("/fxml/testreport/Results02" +
		// ConstantApp.THEME_FXML);
		// Parent nodeFromFXML = getNodeFromFXML("/fxml/testreport/ReportsTable" +
		// ConstantApp.THEME_FXML);
		childPane.getChildren().add(nodeFromFXML);
		update_left_status("Reports", ConstantApp.LEFT_STATUS_DEBUG);
		// }
	}

	public static void displayBusyLoadingScreen(Parent nodeFromFXML) {
		// public static void displayBusyLoadingScreen(Parent ) {
		// Parent nodeFromFXML = getNodeFromFXML("/fxml/setting/ScanDevice" +
		// ConstantApp.THEME_FXML);
		// Parent nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" +
		// ConstantApp.THEME_FXML);
		busyLoadingFXMLNode = nodeFromFXML;
		update_left_status("Loading data...", ConstantApp.LEFT_STATUS_DEBUG);
		ref_childPane.getChildren().add(nodeFromFXML);

	}

	public static void unloadScanDeviceFXML() throws IOException {
		ref_childPane.getChildren().remove(busyLoadingFXMLNode);
	}

	@FXML
	private void onEMModelClickAction() throws IOException {

		ApplicationLauncher.logger.info("You clicked EM Model Icon!");

		if (lbl_Dashboard != null)
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Report.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_EM_Model.setTextFill(Color.web(HIGHLIGHT_COLOUR_RED));
		lbl_Debug.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));

		unloadChildNodeFXML();
		Parent nodeFromFXML = getNodeFromFXML("/fxml/setting/EM_Model" + ConstantApp.THEME_FXML);

		update_left_status("Meter Profile", ConstantApp.LEFT_STATUS_DEBUG);
		childPane.getChildren().add(nodeFromFXML);
	}

	public static void disableLeftMenuButtonsForTestRun() {
		ref_vbox_system_config.setDisable(true);
		ref_vbox_metertype.setDisable(true);
		ref_vbox_report.setDisable(true);
		ref_vboxDebug.setDisable(true);
	}

	public static void DisableLeftMenuButtonsForTestRun() {
		ApplicationLauncher.logger.debug("DisableLeftMenuButtonsForTestRun : Entry");
		ref_vbox_system_config.setDisable(true);
		ref_vbox_metertype.setDisable(true);
		ref_vbox_report.setDisable(true);
		// ref_vbox_scan_devices.setDisable(true);
		// ref_vbox_logView.setDisable(true);

	}

	public static void enableLeftMenuButtonsForTestRun() {

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			if (isUacProjectScreenDisplayEnabled()) {
			}
			if (isUacDeployScreenDisplayEnabled()) {
			}
			if (isUacSettingScreenDisplayEnabled()) {
				ref_vbox_system_config.setDisable(false);
			}
			if (isUacMeterProfileScreenDisplayEnabled()) {
				ref_vbox_metertype.setDisable(false);
			}
			if (isUacTestRunScreenDisplayEnabled()) {
			}
			if (isUacReportScreenDisplayEnabled()) {
				ref_vbox_report.setDisable(false);
			}
			if (isUacManualModeScreenDisplayEnabled()) {
				ref_vboxDebug.setDisable(false);
			}

		} else {
			ref_vbox_system_config.setDisable(false);
			ref_vbox_metertype.setDisable(false);
			ref_vbox_report.setDisable(false);
			ref_vboxDebug.setDisable(false);
		}
	}

	public static void DisableScanDeviceButton() {
		ApplicationLauncher.logger.debug("DisableScanDeviceButton : Entry");

		// ref_vbox_scan_devices.setDisable(true);
		// setScanDeviceButtonSemLocked(true);

	}

	public static void EnableScanDeviceButton() {
		// ApplicationLauncher.logger.debug("DisableTestRunButton : Entry");
		/*
		 * if(getScanDeviceButtonSemLocked()){
		 * ref_vbox_scan_devices.setDisable(false);
		 * setScanDeviceButtonSemLocked( false);
		 * }
		 */

	}

	public static void EnableLeftMenuButtonsForTestRun() {
		ApplicationLauncher.logger.debug("EnableLeftMenuButtonsForSettingsClick : Entry");

		ref_vbox_system_config.setDisable(false);
		ref_vbox_metertype.setDisable(false);

		/*
		 * if(ConstantProTamp.ScanDeviceFound ){
		 * }else{
		 * }
		 */

		ref_vbox_report.setDisable(false);

		/*
		 * if(!getScanDeviceButtonSemLocked()){
		 * ref_vbox_scan_devices.setDisable(false);
		 * }
		 */

		// ref_vbox_logView.setDisable(false);

		ApplicationLauncher.logger.debug("EnableLeftMenuButtonsForSettingsClick : Exit");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		ref_statusLabel = statusLabel;
		ref_navigationLabel = navigationLabel;
		ref_childPane = childPane;
		ref_labelBottomtSecStatus = labelBottomtSecStatus;
		if (lbl_Dashboard != null)
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));

		SetInstantMetricsGUI_Displayed(false);
		setLduAllDataViewGUI_Displayed(false);
		ref_vboxDebug = vboxDebug;
		ref_vbox_report = vbox_report;
		ref_vbox_metertype = vbox_metertype;
		ref_vbox_system_config = vbox_system_config;
		Platform.runLater(() -> {
			DisplayExecuteProcalStatus1.setValue("");
		});

		ref_navigationLabel.textProperty().bind(DisplayExecuteProcalStatus1);
		Platform.runLater(() -> {
			DisplayExecuteSecondaryStatus.setValue("");
		});
		ref_labelBottomtSecStatus.textProperty().bind(DisplayExecuteSecondaryStatus);
		versionLabel.setText("Version: " + ConstantVersion.APPLICATION_VERSION);

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}

		if (ProcalFeatureEnable.PROPOWER_SRC_ONLY) {
			// ref_vbox_report.setVisible(false);
		}

		if (ProcalFeatureEnable.PROCAL_LAB_MODE) {
			// ref_vboxManualMode.setVisible(true);
		}

		// ref_vboxManualMode.setVisible(true);

		if (ProcalFeatureEnable.PROPOWER_SRC_ONLY) {
			proPowerSrcOnly_BootupConfigStateEnabled = true;
		} else {
			proPowerSrcOnly_BootupConfigStateEnabled = false;
		}

		Platform.runLater(() -> {
			try {
				onDashboardClickAction();
			} catch (IOException e) {
				ApplicationLauncher.logger.error("Failed to load dashboard on startup: " + e.getMessage());
			}
		});
	}

	public static void updateBottomSecondaryStatus(String value, String type) {
		if (ConstantApp.DEBUG_FLAG && type.equals(ConstantApp.LEFT_STATUS_DEBUG)) {
			Platform.runLater(() -> {
				DisplayExecuteSecondaryStatus.setValue(value);
			});
		} else if (ConstantApp.INFO_FLAG && type.equals(ConstantApp.LEFT_STATUS_INFO)) {
			Platform.runLater(() -> {
				DisplayExecuteSecondaryStatus.setValue(value);
			});
		}
	}

	private Parent getNodeFromFXML(String url) throws IOException {
		Parent node = FXMLLoader.load(getClass().getResource(url));
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		return node;
	}

	public static Boolean getLduAllDataViewGUI_Displayed() {
		return lduAllDataViewGUI_Displayed;
	}

	public static void setLduAllDataViewGUI_Displayed(Boolean lduAllDataViewGUI_Displayed) {
		ApplicationHomeController.lduAllDataViewGUI_Displayed = lduAllDataViewGUI_Displayed;
	}

	public static void SetInstantMetricsGUI_Displayed(boolean status) {
		InstantMetricsGUI_Displayed = status;
	}

	public static boolean GetInstantMetricsGUI_Displayed() {
		return InstantMetricsGUI_Displayed;
	}

	public void unloadChildNodeFXML() throws IOException {
		childPane.getChildren().clear();
	}

	public static void update_left_status(String value, String type) {
		if (ConstantApp.DEBUG_FLAG && type.equals(ConstantApp.LEFT_STATUS_DEBUG)) {
			Platform.runLater(() -> {
				DisplayExecuteProcalStatus1.setValue(value);
			});
		} else if (ConstantApp.INFO_FLAG && type.equals(ConstantApp.LEFT_STATUS_INFO)) {
			Platform.runLater(() -> {
				DisplayExecuteProcalStatus1.setValue(value);
			});
		}
	}

	public static void update_ServerStatus(String value) {

		/*
		 * Platform.runLater(() -> {
		 * //DisplayServerStatus.setValue(value);
		 * if(value.equals(ServerProperties.SERVER_CONNECTION_SUCCESS)){
		 * ref_circlePanelStatus.setFill(Color.LIGHTGREEN);
		 * }else if(value.equals(ServerProperties.SERVER_CONNECTION_FAILED)){
		 * ref_circlePanelStatus.setFill(Color.RED);
		 * //}else if(value.equals(ConstantProTamp.SLAVE_COMM_FAILED)){
		 * }else if(value.equals(ServerProperties.SERVER_CONNECTION_SLAVE_COMM_FAILED)){
		 * ref_circlePanelStatus.setFill(Color.ORANGE);
		 * //}else if(value.equals(ConstantProTamp.SLAVE_WAITING)){
		 * }else if(value.equals(ServerProperties.SERVER_CONNECTION_SLAVE_WAITING)){
		 * ref_circlePanelStatus.setFill(Color.BLUE);
		 * }
		 * 
		 * 
		 * 
		 * //circlePanelStatus
		 * });
		 */

	}

	public void lscsSourceCalibrationStageDisplay() {// long time_in_seconds) {
		ApplicationLauncher.logger.info("lscsSourceCalibrationStageDisplay: entry");
		// ApplicationLauncher.logger.info("lscsSourceCalibrationStageDisplay:
		// selectedTestPointName: " + selectedTestPointName);
		// ApplicationLauncher.logger.info("lscsSourceCalibrationStageDisplay:
		// deploymentId: " + deploymentId);

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/calib/lscsCalibration" + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("loadReportSettings: IOException:" + ex.getMessage());
			return;
		}

		lscsSourceCalibrationStage = new Stage();
		lscsSourceCalibrationStage.initModality(Modality.WINDOW_MODAL);
		// https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		lscsSourceCalibrationStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		lscsSourceCalibrationStage.setScene(newScene);
		lscsSourceCalibrationStage.setTitle(ConstantVersion.APPLICATION_NAME + " - Calibration");
		lscsSourceCalibrationStage.setResizable(false);
		// Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		// int width = 452;//1450;// 1600;//1275;//850;//413+10;
		// int height = 474;//675;// 650;//314+35;

		// lscsSourceCalibrationStage.setAlwaysOnTop(true);
		lscsSourceCalibrationStage.centerOnScreen();
		lscsSourceCalibrationStage.show();
		lscsSourceCalibrationStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}

		});
		// lscsSourceCalibrationStage.toFront();
		// lscsSourceCalibrationStage.showAndWait();
	}

	public static boolean isUacAdminScreenDisplayEnabled() {
		return uacAdminScreenDisplayEnabled;
	}

	public static void setUacAdminScreenDisplayEnabled(boolean uacAdminScreenDisplayEnabled) {
		ApplicationHomeController.uacAdminScreenDisplayEnabled = uacAdminScreenDisplayEnabled;
	}

	public static boolean isUacDeviceSettingsScreenDisplayEnabled() {
		return uacDeviceSettingsScreenDisplayEnabled;
	}

	public static void setUacDeviceSettingsScreenDisplayEnabled(boolean uacDeviceSettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacDeviceSettingsScreenDisplayEnabled = uacDeviceSettingsScreenDisplayEnabled;
	}

	public static boolean isUacStabilitySettingsScreenDisplayEnabled() {
		return uacStabilitySettingsScreenDisplayEnabled;
	}

	public static void setUacStabilitySettingsScreenDisplayEnabled(boolean uacStabilitySettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacStabilitySettingsScreenDisplayEnabled = uacStabilitySettingsScreenDisplayEnabled;
	}

	public static boolean isUacReportConfigurationScreenDisplayEnabled() {
		return uacReportConfigurationScreenDisplayEnabled;
	}

	public static void setUacReportConfigurationScreenDisplayEnabled(
			boolean uacReportConfigurationScreenDisplayEnabled) {
		ApplicationHomeController.uacReportConfigurationScreenDisplayEnabled = uacReportConfigurationScreenDisplayEnabled;
	}

	public static boolean isUacBackupSettingsScreenDisplayEnabled() {
		return uacBackupSettingsScreenDisplayEnabled;
	}

	public static void setUacBackupSettingsScreenDisplayEnabled(boolean uacBackupSettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacBackupSettingsScreenDisplayEnabled = uacBackupSettingsScreenDisplayEnabled;
	}

	public static boolean isUacTestRunScreenDisplayEnabled() {
		return uacTestRunScreenDisplayEnabled;
	}

	public static void setUacTestRunScreenDisplayEnabled(boolean uacTestRunScreenDisplayEnabled) {
		ApplicationHomeController.uacTestRunScreenDisplayEnabled = uacTestRunScreenDisplayEnabled;
	}

	public static boolean isUacDeployScreenDisplayEnabled() {
		return uacDeployScreenDisplayEnabled;
	}

	public static void setUacDeployScreenDisplayEnabled(boolean uacDeployScreenDisplayEnabled) {
		ApplicationHomeController.uacDeployScreenDisplayEnabled = uacDeployScreenDisplayEnabled;
	}

	public static boolean isUacProjectScreenDisplayEnabled() {
		return uacProjectScreenDisplayEnabled;
	}

	public static void setUacProjectScreenDisplayEnabled(boolean uacProjectScreenDisplayEnabled) {
		ApplicationHomeController.uacProjectScreenDisplayEnabled = uacProjectScreenDisplayEnabled;
	}

	public static boolean isUacReportScreenDisplayEnabled() {
		return uacReportScreenDisplayEnabled;
	}

	public static void setUacReportScreenDisplayEnabled(boolean uacReportScreenDisplayEnabled) {
		ApplicationHomeController.uacReportScreenDisplayEnabled = uacReportScreenDisplayEnabled;
	}

	public static boolean isUacMeterProfileScreenDisplayEnabled() {
		return uacMeterProfileScreenDisplayEnabled;
	}

	public static void setUacMeterProfileScreenDisplayEnabled(boolean uacMeterProfileScreenDisplayEnabled) {
		ApplicationHomeController.uacMeterProfileScreenDisplayEnabled = uacMeterProfileScreenDisplayEnabled;
	}

	public static boolean isUacSettingScreenDisplayEnabled() {
		return uacSettingScreenDisplayEnabled;
	}

	public static void setUacSettingScreenDisplayEnabled(boolean uacSettingScreenDisplayEnabled) {
		ApplicationHomeController.uacSettingScreenDisplayEnabled = uacSettingScreenDisplayEnabled;
	}

	private void applyUacSettings() {

		ApplicationLauncher.logger.info("ApplicationHomeController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_PROJECT_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacProjectScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_MANUAL_MODE_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_vboxDebug.setDisable(true);
						setUacManualModeScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_DEPLOY_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacDeployScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_TEST_RUN_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacTestRunScreenDisplayEnabled(false);
					}
					break;

				case ConstantApp.UAC_REPORT_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_vbox_report.setDisable(true);
						setUacReportScreenDisplayEnabled(false);
					}
					break;

				case ConstantApp.UAC_METER_PROFILE_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_vbox_metertype.setDisable(true);
						setUacMeterProfileScreenDisplayEnabled(false);
					}
					break;

				case ConstantApp.UAC_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_vbox_system_config.setDisable(true);
						setUacSettingScreenDisplayEnabled(false);
					}
					break;

				/*
				 * case ConstantApp.UAC_PLC_SCREEN:
				 * 
				 * 
				 * if(!uacSelectProfileScreenList.get(i).getVisibleEnabled()){
				 * setUacPlcScreenDisplayEnabled(false);
				 * 
				 * }
				 * break;
				 */

				case ConstantApp.UAC_ADMIN_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacAdminScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_DEVICE_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacStabilitySettingsScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_STABILITY_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacStabilitySettingsScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_REPORT_CONFIGURATION_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacReportConfigurationScreenDisplayEnabled(false);

					}
					break;

				case ConstantApp.UAC_BACKUP_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						setUacBackupSettingsScreenDisplayEnabled(false);

					}
					break;

				default:
					break;
			}

		}
	}

	public static void update_labelBootupTimer(String value) {

		/*
		 * Platform.runLater(() -> {
		 * DisplayBootupTimer.setValue(value);
		 * });
		 */

	}

	public static void ScanTimerDisplayVisible(Boolean value) {
		ApplicationLauncher.logger.debug("ScanTimerDisplayVisible: Entry");
		/*
		 * Platform.runLater(() -> {
		 * ref_labelScanTimer.setVisible(value);
		 * });
		 */

	}

	public static void ProtampBootupDisplayVisible(Boolean value) {
		ApplicationLauncher.logger.debug("ProtampBootupDisplayVisible: Entry");
		/*
		 * Platform.runLater(() -> {
		 * ref_labelBootupStatus.setVisible(value);
		 * //DisplayBootupStatus.
		 * });
		 */

	}

	public static Boolean getMaintenanceGUI_Displayed() {
		return maintenanceGUI_Displayed;
	}

	public static void setMaintenanceGUI_Displayed(Boolean maintenanceGUI_Displayed) {
		ApplicationHomeController.maintenanceGUI_Displayed = maintenanceGUI_Displayed;
	}

	public static boolean isUacManualModeScreenDisplayEnabled() {
		return uacManualModeScreenDisplayEnabled;
	}

	public static void setUacManualModeScreenDisplayEnabled(boolean uacManualModeScreenDisplayEnabled) {
		ApplicationHomeController.uacManualModeScreenDisplayEnabled = uacManualModeScreenDisplayEnabled;
	}

}