package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import gnu.io.CommPortIdentifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class SystemSettingController implements Initializable {

	private static ArrayList<String> presentSerialPortList = new ArrayList<String>();
	@FXML
	private AnchorPane DeviceSubMenupane;

	@FXML
	private AnchorPane device_childpane;

	@FXML
	private AnchorPane serverSettingChildPane;

	@FXML
	private AnchorPane stability_childpane;
	@FXML
	private AnchorPane qrScanner_childpane;

	@FXML
	private AnchorPane dut_childpane;

	@FXML
	private AnchorPane dut_conveyor_childpane;
	@FXML
	private AnchorPane about_childpane;

	@FXML
	private AnchorPane megaohmmeter_childpane;

	@FXML
	private AnchorPane voltmeter_childpane;

	@FXML
	private AnchorPane ldu_childpane;

	@FXML
	private AnchorPane terminalProfileSettingChildPane;

	@FXML
	private AnchorPane bayDeviceConfigChildPane;

	@FXML
	private AnchorPane admin_childpane;

	@FXML
	private AnchorPane backup_results_childpane;

	@FXML
	private AnchorPane ref_std_const_childpane;

	@FXML
	private AnchorPane reportconfig_childpane;

	@FXML
	private AnchorPane diagChildpane;
	@FXML
	private AnchorPane firmwareUpgradeChildpane;

	@FXML
	private AnchorPane reportexcelconfig_childpane;

	@FXML
	private AnchorPane reportfilelocation_childpane;

	@FXML
	private Accordion deviceAccordian;

	@FXML
	private TitledPane deviceSettingTitlePane;

	@FXML
	private TitledPane dutSettingsTitledPane;

	@FXML
	private TitledPane titledPaneRefConst;

	@FXML
	private TitledPane titlepane_admin;

	@FXML
	private TitledPane titledpane_backupresults;

	@FXML
	private TitledPane titledPaneStability;

	@FXML
	private TitledPane titledPaneReportConfiguration;

	public void LoadAllChild_FXML() {
		ApplicationLauncher.logger.info("LoadAllChild_FXML:Entry");
		try {
			try {
				device_childpane.getChildren()
						.add(getNodeFromFXML("/fxml/setting/DevicePortSetup" + ConstantApp.THEME_FXML));

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("LoadAllChild_FXML: Exception:" + e.getMessage());
			}

			Platform.runLater(() -> {
				try {

					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					// dut_conveyor_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DutPortSetup"
					// + ConstantApp.THEME_FXML));
					dut_conveyor_childpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/DutPortSetupV2" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: DutPortSetup Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {
					// ApplicationLauncher.logger.debug("LoadAllChild_FXML: ServerSetting: Loaded");
					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					serverSettingChildPane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/ServerSetting" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: ServerSetting Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					diagChildpane.getChildren().add(getNodeFromFXML("/fxml/setting/Diag" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: Diag Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					firmwareUpgradeChildpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/FirmwareUpgrade" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: FirmwareUpgrade Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					megaohmmeter_childpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/MegaOhmPmPortSetupV2" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger
							.error("LoadAllChild_FXML: MegaOhmPmPortSetup Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					// stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup"
					// + ConstantApp.THEME_FXML));
					// voltmeter_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/MegaOhmPanelMeterPortSetup"
					// + ConstantApp.THEME_FXML));
					voltmeter_childpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/VoltPmPortSetupV2" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: VoltPmPortSetup Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					ldu_childpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/LduPortSetupV2" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: LduPortSetup Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					terminalProfileSettingChildPane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/TerminalProfileSetting" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger
							.error("LoadAllChild_FXML: TerminalProfileSetting Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					bayDeviceConfigChildPane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/BayDeviceConfig" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("LoadAllChild_FXML: BayDeviceConfig Exception:" + e.getMessage());
				}
			});

			Platform.runLater(() -> {
				try {

					// qrScanner_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/QrScannerPortSetup"
					// + ConstantApp.THEME_FXML));
					qrScanner_childpane.getChildren()
							.add(getNodeFromFXML("/fxml/setting/QrScannerPortSetupV2" + ConstantApp.THEME_FXML));
					// InitCounter--;

				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger
							.error("LoadAllChild_FXML: QrScannerPortSetup Exception:" + e.getMessage());
				}
			});

			if (ConstantAppConfig.DUT_COMM_FEATURE_ENABLED) {
				dut_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DutPortSetup" + ConstantApp.THEME_FXML));

			}

			stability_childpane.getChildren()
					.add(getNodeFromFXML("/fxml/setting/StabilizationValidation" + ConstantApp.THEME_FXML));
			about_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/About" + ConstantApp.THEME_FXML));
			admin_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/Admin_Page" + ConstantApp.THEME_FXML));
			backup_results_childpane.getChildren()
					.add(getNodeFromFXML("/fxml/setting/Backup_Results" + ConstantApp.THEME_FXML));
			if (!ProcalFeatureEnable.PROPOWER_SRC_ONLY) {
				reportconfig_childpane.getChildren()
						.add(getNodeFromFXML("/fxml/testreport/ReportSubMenu" + ConstantApp.THEME_FXML));
			}
			ref_std_const_childpane.getChildren()
					.add(getNodeFromFXML("/fxml/setting/RefStdConst" + ConstantApp.THEME_FXML));
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.info("LoadAllChild_FXML: Exception:" + e.getMessage());
		}
	}

	private Parent getNodeFromFXML(String url) throws IOException {
		return FXMLLoader.load(getClass().getResource(url));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		deviceAccordian.setExpandedPane(deviceSettingTitlePane);
		ApplicationLauncher.logger.info("SystemSettingController: initialize: REF_STD_CONSTANT_CONFIG_ENABLE: "
				+ ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE);
		try {
			if (ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE) {
				titledPaneRefConst.setVisible(true);
			} else {
				titledPaneRefConst.setVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SystemSettingController: initialize: Exception:" + e.getMessage());
			titledPaneRefConst.setVisible(false);
		}
		/*
		 * if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL))
		 * ||
		 * (ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
		 * titlepane_admin.setVisible(false);
		 * }
		 */
		if (!ConstantAppConfig.DUT_COMM_FEATURE_ENABLED) {

			// dutSettingsTitledPane.setDisable(true);
			dutSettingsTitledPane.setVisible(false);
		}

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			if (!ApplicationHomeController.isUacAdminScreenDisplayEnabled()) {

				titlepane_admin.setDisable(true);
			}

			if (!ApplicationHomeController.isUacDeviceSettingsScreenDisplayEnabled()) {

				deviceSettingTitlePane.setDisable(true);
			}

			if (!ApplicationHomeController.isUacStabilitySettingsScreenDisplayEnabled()) {

				titledPaneStability.setDisable(true);
			}

			if (!ApplicationHomeController.isUacReportConfigurationScreenDisplayEnabled()) {

				titledPaneReportConfiguration.setDisable(true);
			}

			if (!ApplicationHomeController.isUacBackupSettingsScreenDisplayEnabled()) {

				titledpane_backupresults.setDisable(true);
			}

			applyUacSettings();

		}

		if (ProcalFeatureEnable.PROPOWER_SRC_ONLY) {

			titledPaneStability.setVisible(false);
			titledPaneReportConfiguration.setVisible(false);
			titledpane_backupresults.setVisible(false);

		}
		scanSerialPort();
		LoadAllChild_FXML();
	}

	public void scanSerialPort() {

		Enumeration ports = CommPortIdentifier.getPortIdentifiers();
		getPresentSerialPortList().clear();
		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();

			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				getPresentSerialPortList().add(curPort.getName());
			}
		}
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("SystemSettingController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
						// ref_btn_get_results.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
						// ref_btn_add.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
						// ref_btn_save.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getDeletePossible()) {
						// ref_btn_remove.setDisable(true);
						// ref_btn_reset.setDisable(true);

					}
					break;

				default:
					break;
			}

		}
	}

	public static ArrayList<String> getPresentSerialPortList() {
		return presentSerialPortList;
	}

	public static void setPresentSerialPortList(ArrayList<String> presentSerialPortList) {
		SystemSettingController.presentSerialPortList = presentSerialPortList;
	}
}
