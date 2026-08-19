package com.tasnetwork.calibration.energymeter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

/**
 * Controller for the main application home screen. Handles layout components,
 * navigation actions, user access control configurations, and popup windows.
 */
public class ApplicationHomeController implements Initializable {

	// ==========================================
	// Constants & Static UI Stages/Properties
	// ==========================================

	public static Stage lscsSourceCalibrationStage = new Stage();
	public static String HIGHLIGHT_COLOUR_RED = "#FF0000";
	public static String HIGHLIGHT_COLOUR_BLACK = "#000000";

	public static StringProperty DisplayExecuteProcalStatus1 = new SimpleStringProperty();
	public static StringProperty DisplayExecuteSecondaryStatus = new SimpleStringProperty();

	private static Boolean InstantMetricsGUI_Displayed = false;
	private static Boolean lduAllDataViewGUI_Displayed = false;

	public static Object busyLoadingFXMLNode;

	// ==========================================
	// User Access Control (UAC) Display Flags
	// ==========================================

	private static boolean uacTestRunScreenDisplayEnabled = true;
	private static boolean uacDeployScreenDisplayEnabled = true;
	private static boolean uacProjectScreenDisplayEnabled = true;
	private static boolean uacReportScreenDisplayEnabled = true;
	private static boolean uacMeterProfileScreenDisplayEnabled = true;
	private static boolean uacSettingScreenDisplayEnabled = true;
	private static boolean uacManualModeScreenDisplayEnabled = true;
	private static boolean uacAdminScreenDisplayEnabled = true;

	private static boolean uacDeviceSettingsScreenDisplayEnabled = true;
	private static boolean uacStabilitySettingsScreenDisplayEnabled = true;
	private static boolean uacReportConfigurationScreenDisplayEnabled = true;
	private static boolean uacBackupSettingsScreenDisplayEnabled = true;

	// ==========================================
	// FXML UI Control Fields
	// ==========================================

	@FXML
	private AnchorPane childPane;
	private static AnchorPane ref_childPane;

	@FXML
	private Label lbl_Devices;

	@FXML
	private Label statusLabel;
	public static Label ref_statusLabel;

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
	private VBox vbox_meterprofile;
	private static VBox ref_vbox_meterprofile;

	@FXML
	private VBox vbox_settings;
	private static VBox ref_vbox_settings;

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

	private final Map<String, Stage> popupStages = new HashMap<>();

	// ==========================================
	// Initialization
	// ==========================================

	/**
	 * Initializes the controller. Sets up FX bindings, labels, versions,
	 * applies User Access Control (UAC) if enabled, and loads the dashboard.
	 *
	 * @param url the location used to resolve relative paths for the root object,
	 *            or null if the location is not known
	 * @param rb  the resources used to localize the root object, or null if the
	 *            root object was not localized
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ref_statusLabel = statusLabel;
		ref_navigationLabel = navigationLabel;
		ref_childPane = childPane;
		ref_labelBottomtSecStatus = labelBottomtSecStatus;

		if (lbl_Dashboard != null) {
			lbl_Dashboard.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));
		}
		lbl_Devices.setTextFill(Color.web(HIGHLIGHT_COLOUR_BLACK));

		SetInstantMetricsGUI_Displayed(false);
		setLduAllDataViewGUI_Displayed(false);

		ref_vboxDebug = vboxDebug;
		ref_vbox_report = vbox_report;
		ref_vbox_meterprofile = vbox_meterprofile;
		ref_vbox_settings = vbox_settings;

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

		try {
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
			
			showPopupScreen("Pallet Tracker", "/fxml/conveyor/PalletTracker_W.fxml");
			showPopupScreen("Conveyor Debug", "/fxml/conveyor/ConveyorDebugV2_W.fxml");
		} catch (IOException e) {
			ApplicationLauncher.logger.error("Failed to load dashboard on startup: " + e.getMessage());
		}
	}

	// ==========================================
	// FXML Click Event Handlers
	// ==========================================

	/**
	 * Handles the action when the Settings navigation icon or label is clicked.
	 * Highlights the Settings menu and shows the System Settings popup screen.
	 *
	 * @throws IOException if the FXML view for system settings cannot be loaded
	 */
	@FXML
	private void onSettingsClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Devices Icon!");

		update_left_status("Devices", ConstantApp.LEFT_STATUS_DEBUG);
		showPopupScreen("Devices", "/fxml/setting/SystemSetting" + ConstantApp.THEME_FXML);
	}

	/**
	 * Handles the action when the Dashboard navigation icon or label is clicked.
	 * Highlights the Dashboard menu, unloads existing child nodes, and loads the
	 * dashboard FXML.
	 *
	 * @throws IOException if the FXML view for the dashboard cannot be loaded
	 */
	@FXML
	private void onDashboardClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Dashboard Icon!");
	}

	/**
	 * Handles the action when the Debug navigation icon or label is clicked.
	 * Shows the Pallet Tracker and Conveyor Debug popup screens.
	 *
	 * @throws IOException if any of the debug FXML views cannot be loaded
	 */
	@FXML
	private void onDebugClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Debug Icon!");

		update_left_status("Debug", ConstantApp.LEFT_STATUS_DEBUG);
		showPopupScreen("Pallet Tracker", "/fxml/conveyor/PalletTracker_W.fxml");
		showPopupScreen("Conveyor Debug", "/fxml/conveyor/ConveyorDebugV2_W.fxml");
	}

	/**
	 * Handles the action when the Reports navigation icon or label is clicked.
	 * Shows the Reports popup screen.
	 *
	 * @throws IOException if the reports FXML view cannot be loaded
	 */
	@FXML
	private void onReportsClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked Report Icon!");

		showPopupScreen("Reports", "/fxml/testreport/Results" + ConstantApp.THEME_FXML);
		update_left_status("Reports", ConstantApp.LEFT_STATUS_DEBUG);
	}

	/**
	 * Handles the action when the EM Model (Meter Profile) navigation icon or label
	 * is clicked.
	 * Shows the Meter Profile popup screen.
	 *
	 * @throws IOException if the meter profile FXML view cannot be loaded
	 */
	@FXML
	private void onMeterProfileClickAction() throws IOException {
		ApplicationLauncher.logger.info("You clicked EM Model Icon!");

		update_left_status("Meter Profile", ConstantApp.LEFT_STATUS_DEBUG);
		showPopupScreen("Meter Profile", "/fxml/setting/EM_Model" + ConstantApp.THEME_FXML);
	}

	// ==========================================
	// GUI & FXML Management Methods
	// ==========================================

	/**
	 * Displays a busy loading/progress screen overlay in the child pane.
	 *
	 * @param nodeFromFXML the parent node representing the loading view
	 */
	public static void displayBusyLoadingScreen(Parent nodeFromFXML) {
		busyLoadingFXMLNode = nodeFromFXML;
		update_left_status("Loading data...", ConstantApp.LEFT_STATUS_DEBUG);
		ref_childPane.getChildren().add(nodeFromFXML);
	}

	/**
	 * Unloads the busy loading overlay screen from the child pane.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	public static void unloadScanDeviceFXML() throws IOException {
		ref_childPane.getChildren().remove(busyLoadingFXMLNode);
	}

	/**
	 * Clears all children nodes currently loaded in the child pane.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	public void unloadChildNodeFXML() throws IOException {
		childPane.getChildren().clear();
	}

	/**
	 * Helper method to load an FXML node and anchor it to all edges of its
	 * container.
	 *
	 * @param url the classpath relative URL of the FXML resource to load
	 * @return the loaded Parent node anchored to its boundaries
	 * @throws IOException if the FXML file cannot be found or loaded
	 */
	private Parent getNodeFromFXML(String url) throws IOException {
		Parent node = FXMLLoader.load(getClass().getResource(url));
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		return node;
	}

	/**
	 * Loads and displays an FXML view in a new modeless popup window.
	 *
	 * @param title    the title suffix for the popup window
	 * @param fxmlPath the classpath relative path of the FXML resource
	 * @throws IOException if the FXML file cannot be found or loaded
	 */
	private void showPopupScreen(String title, String fxmlPath) throws IOException {

		Stage existingStage = popupStages.get(fxmlPath);

		if (existingStage != null) {

			// Window was minimized
			if (existingStage.isIconified()) {
				existingStage.setIconified(false);
			}

			// Bring it to the front
			existingStage.show();
			existingStage.toFront();
			existingStage.requestFocus();

			return;
		}

		URL fxmlUrl = getClass().getResource(fxmlPath);
		if (fxmlUrl == null) {
			throw new IOException("FXML resource not found: " + fxmlPath);
		}

		FXMLLoader loader = new FXMLLoader(fxmlUrl);
		Scene scene = new Scene(loader.load());

		Stage popupStage = new Stage();

		popupStage.initModality(Modality.NONE);
		popupStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		popupStage.setTitle(ConstantVersion.APPLICATION_NAME + " - " + title);
		popupStage.setScene(scene);

		// Remove from map when actually closed
		popupStage.setOnHidden(e -> popupStages.remove(fxmlPath));

		popupStages.put(fxmlPath, popupStage);

		popupStage.show();
	}

	/**
	 * Loads and displays the LSCS source calibration window as a modal window.
	 */
	public void lscsSourceCalibrationStageDisplay() {
		ApplicationLauncher.logger.info("lscsSourceCalibrationStageDisplay: entry");

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
		lscsSourceCalibrationStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		lscsSourceCalibrationStage.setScene(newScene);
		lscsSourceCalibrationStage.setTitle(ConstantVersion.APPLICATION_NAME + " - Calibration");
		lscsSourceCalibrationStage.setResizable(false);
		lscsSourceCalibrationStage.centerOnScreen();
		lscsSourceCalibrationStage.show();
		lscsSourceCalibrationStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}
		});
	}

	// ==========================================
	// Left Menu Navigation & Status Management
	// ==========================================

	/**
	 * Disables the left menu configuration and debug items during a test run.
	 */
	public static void disableLeftMenuButtonsForTestRun() {
		ref_vbox_settings.setDisable(true);
		ref_vbox_meterprofile.setDisable(true);
		ref_vbox_report.setDisable(true);
		ref_vboxDebug.setDisable(true);
	}

	/**
	 * Disables the left menu system configuration, meter type, and report items
	 * during a test run.
	 * (Note: Kept capitalized for backward compatibility with external callers).
	 */
	public static void DisableLeftMenuButtonsForTestRun() {
		ApplicationLauncher.logger.debug("DisableLeftMenuButtonsForTestRun : Entry");
		ref_vbox_settings.setDisable(true);
		ref_vbox_meterprofile.setDisable(true);
		ref_vbox_report.setDisable(true);
	}

	/**
	 * Enables the left menu items depending on current User Access Control (UAC)
	 * settings.
	 */
	public static void enableLeftMenuButtonsForTestRun() {
		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			if (isUacSettingScreenDisplayEnabled()) {
				ref_vbox_settings.setDisable(false);
			}
			if (isUacMeterProfileScreenDisplayEnabled()) {
				ref_vbox_meterprofile.setDisable(false);
			}
			if (isUacReportScreenDisplayEnabled()) {
				ref_vbox_report.setDisable(false);
			}
			if (isUacManualModeScreenDisplayEnabled()) {
				ref_vboxDebug.setDisable(false);
			}
		} else {
			ref_vbox_settings.setDisable(false);
			ref_vbox_meterprofile.setDisable(false);
			ref_vbox_report.setDisable(false);
			ref_vboxDebug.setDisable(false);
		}
	}

	/**
	 * Enables the left system configuration and meter type menu items.
	 * (Note: Kept capitalized for backward compatibility with external callers).
	 */
	public static void EnableLeftMenuButtonsForTestRun() {
		ApplicationLauncher.logger.debug("EnableLeftMenuButtonsForSettingsClick : Entry");
		ref_vbox_settings.setDisable(false);
		ref_vbox_meterprofile.setDisable(false);
		ref_vbox_report.setDisable(false);
		ApplicationLauncher.logger.debug("EnableLeftMenuButtonsForSettingsClick : Exit");
	}

	/**
	 * Updates the left status text property based on the log level type and
	 * configuration.
	 *
	 * @param value the status message to display
	 * @param type  the logging type (e.g., DEBUG or INFO)
	 */
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

	/**
	 * Updates the bottom secondary status text property based on the log level type
	 * and configuration.
	 *
	 * @param value the status message to display
	 * @param type  the logging type (e.g., DEBUG or INFO)
	 */
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

	/**
	 * Stub method to update the bootup status message. Currently has no visual
	 * impact
	 * but is kept for compatibility with callers.
	 *
	 * @param value the bootup status message
	 */
	public static void update_labelBootupStatus(String value) {
		ApplicationLauncher.logger.debug("update_labelBootupStatus: Entry");
	}

	/**
	 * Stub method to update the bootup timer message. Currently has no visual
	 * impact
	 * but is kept for compatibility with callers.
	 *
	 * @param value the bootup timer string
	 */
	public static void update_labelBootupTimer(String value) {
		// Stub method kept for backward compatibility.
	}

	/**
	 * Stub method to change the visibility of the scan timer. Currently has no
	 * visual impact
	 * but is kept for compatibility with callers.
	 *
	 * @param value true to make visible, false to hide
	 */
	public static void ScanTimerDisplayVisible(Boolean value) {
		ApplicationLauncher.logger.debug("ScanTimerDisplayVisible: Entry");
	}

	/**
	 * Stub method to change the visibility of the protamp bootup display. Currently
	 * has no visual impact
	 * but is kept for compatibility with callers.
	 *
	 * @param value true to make visible, false to hide
	 */
	public static void ProtampBootupDisplayVisible(Boolean value) {
		ApplicationLauncher.logger.debug("ProtampBootupDisplayVisible: Entry");
	}

	/**
	 * Stub method to disable the scan device button. Currently has no visual impact
	 * but is kept for compatibility with callers.
	 */
	public static void DisableScanDeviceButton() {
		ApplicationLauncher.logger.debug("DisableScanDeviceButton : Entry");
	}

	/**
	 * Stub method to enable the scan device button. Currently has no visual impact
	 * but is kept for compatibility with callers.
	 */
	public static void EnableScanDeviceButton() {
		// Stub method kept for backward compatibility.
	}

	// ==========================================
	// Instant Metrics & LDU Data Getters/Setters
	// ==========================================

	/**
	 * Returns the current display status of the LDU all data view GUI.
	 *
	 * @return true if the LDU all data view GUI is displayed, false otherwise
	 */
	public static Boolean getLduAllDataViewGUI_Displayed() {
		return lduAllDataViewGUI_Displayed;
	}

	/**
	 * Sets the display status of the LDU all data view GUI.
	 *
	 * @param lduAllDataViewGUI_Displayed the display status to set
	 */
	public static void setLduAllDataViewGUI_Displayed(Boolean lduAllDataViewGUI_Displayed) {
		ApplicationHomeController.lduAllDataViewGUI_Displayed = lduAllDataViewGUI_Displayed;
	}

	/**
	 * Sets the display status of the instant metrics GUI.
	 *
	 * @param status the display status to set
	 */
	public static void SetInstantMetricsGUI_Displayed(boolean status) {
		InstantMetricsGUI_Displayed = status;
	}

	/**
	 * Returns the current display status of the instant metrics GUI.
	 *
	 * @return true if the instant metrics GUI is displayed, false otherwise
	 */
	public static boolean GetInstantMetricsGUI_Displayed() {
		return InstantMetricsGUI_Displayed;
	}

	// ==========================================
	// User Access Control (UAC) Getters & Setters
	// ==========================================

	/**
	 * Returns whether the UAC admin screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacAdminScreenDisplayEnabled() {
		return uacAdminScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC admin screen display is enabled.
	 *
	 * @param uacAdminScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacAdminScreenDisplayEnabled(boolean uacAdminScreenDisplayEnabled) {
		ApplicationHomeController.uacAdminScreenDisplayEnabled = uacAdminScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC device settings screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacDeviceSettingsScreenDisplayEnabled() {
		return uacDeviceSettingsScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC device settings screen display is enabled.
	 *
	 * @param uacDeviceSettingsScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacDeviceSettingsScreenDisplayEnabled(boolean uacDeviceSettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacDeviceSettingsScreenDisplayEnabled = uacDeviceSettingsScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC stability settings screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacStabilitySettingsScreenDisplayEnabled() {
		return uacStabilitySettingsScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC stability settings screen display is enabled.
	 *
	 * @param uacStabilitySettingsScreenDisplayEnabled true to enable, false to
	 *                                                 disable
	 */
	public static void setUacStabilitySettingsScreenDisplayEnabled(boolean uacStabilitySettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacStabilitySettingsScreenDisplayEnabled = uacStabilitySettingsScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC report configuration screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacReportConfigurationScreenDisplayEnabled() {
		return uacReportConfigurationScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC report configuration screen display is enabled.
	 *
	 * @param uacReportConfigurationScreenDisplayEnabled true to enable, false to
	 *                                                   disable
	 */
	public static void setUacReportConfigurationScreenDisplayEnabled(
			boolean uacReportConfigurationScreenDisplayEnabled) {
		ApplicationHomeController.uacReportConfigurationScreenDisplayEnabled = uacReportConfigurationScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC backup settings screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacBackupSettingsScreenDisplayEnabled() {
		return uacBackupSettingsScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC backup settings screen display is enabled.
	 *
	 * @param uacBackupSettingsScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacBackupSettingsScreenDisplayEnabled(boolean uacBackupSettingsScreenDisplayEnabled) {
		ApplicationHomeController.uacBackupSettingsScreenDisplayEnabled = uacBackupSettingsScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC test run screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacTestRunScreenDisplayEnabled() {
		return uacTestRunScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC test run screen display is enabled.
	 *
	 * @param uacTestRunScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacTestRunScreenDisplayEnabled(boolean uacTestRunScreenDisplayEnabled) {
		ApplicationHomeController.uacTestRunScreenDisplayEnabled = uacTestRunScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC deploy screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacDeployScreenDisplayEnabled() {
		return uacDeployScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC deploy screen display is enabled.
	 *
	 * @param uacDeployScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacDeployScreenDisplayEnabled(boolean uacDeployScreenDisplayEnabled) {
		ApplicationHomeController.uacDeployScreenDisplayEnabled = uacDeployScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC project screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacProjectScreenDisplayEnabled() {
		return uacProjectScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC project screen display is enabled.
	 *
	 * @param uacProjectScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacProjectScreenDisplayEnabled(boolean uacProjectScreenDisplayEnabled) {
		ApplicationHomeController.uacProjectScreenDisplayEnabled = uacProjectScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC report screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacReportScreenDisplayEnabled() {
		return uacReportScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC report screen display is enabled.
	 *
	 * @param uacReportScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacReportScreenDisplayEnabled(boolean uacReportScreenDisplayEnabled) {
		ApplicationHomeController.uacReportScreenDisplayEnabled = uacReportScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC meter profile screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacMeterProfileScreenDisplayEnabled() {
		return uacMeterProfileScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC meter profile screen display is enabled.
	 *
	 * @param uacMeterProfileScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacMeterProfileScreenDisplayEnabled(boolean uacMeterProfileScreenDisplayEnabled) {
		ApplicationHomeController.uacMeterProfileScreenDisplayEnabled = uacMeterProfileScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC setting screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacSettingScreenDisplayEnabled() {
		return uacSettingScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC setting screen display is enabled.
	 *
	 * @param uacSettingScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacSettingScreenDisplayEnabled(boolean uacSettingScreenDisplayEnabled) {
		ApplicationHomeController.uacSettingScreenDisplayEnabled = uacSettingScreenDisplayEnabled;
	}

	/**
	 * Returns whether the UAC manual mode screen display is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public static boolean isUacManualModeScreenDisplayEnabled() {
		return uacManualModeScreenDisplayEnabled;
	}

	/**
	 * Sets whether the UAC manual mode screen display is enabled.
	 *
	 * @param uacManualModeScreenDisplayEnabled true to enable, false to disable
	 */
	public static void setUacManualModeScreenDisplayEnabled(boolean uacManualModeScreenDisplayEnabled) {
		ApplicationHomeController.uacManualModeScreenDisplayEnabled = uacManualModeScreenDisplayEnabled;
	}

	/**
	 * Reads User Access Control (UAC) permissions from the data manager and
	 * configures
	 * screen display flags and visual menu enablement accordingly.
	 */
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
						ref_vbox_meterprofile.setDisable(true);
						setUacMeterProfileScreenDisplayEnabled(false);
					}
					break;

				case ConstantApp.UAC_SETTINGS_SCREEN:
					if (!uacSelectProfileScreenList.get(i).getVisibleEnabled()) {
						ref_vbox_settings.setDisable(true);
						setUacSettingScreenDisplayEnabled(false);
					}
					break;

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
}