package com.tasnetwork.calibration.energymeter.testreport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
//import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReportFileLocationController implements Initializable {

	// Timer guiLaunchTimer;
	@FXML
	private ComboBox<String> cmbBox_testtype;

	@FXML
	private ComboBox<String> cmbBxReportProfile;

	static ComboBox<String> ref_cmbBxReportProfile;

	private String selectedReportProfile = "";

	@FXML
	private TextField txt_templ_file_location;

	@FXML
	private TextField txt_save_file_location;

	@FXML
	private Button btnSave;

	private static Button ref_btnSave;

	@FXML
	private Button btnReportGenV2;

	private static Button ref_btnReportGenV2;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ref_cmbBxReportProfile = cmbBxReportProfile;
		ref_btnSave = btnSave;
		ref_btnReportGenV2 = btnReportGenV2;
		loadReportProfileList();
		LoadTestType();
		txt_templ_file_location.setEditable(false);
		txt_save_file_location.setEditable(false);

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}

		hideGuiObjects();
	}

	private void hideGuiObjects() {

		if (!ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
			ref_btnReportGenV2.setVisible(false);
		}
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("ReportFileLocationController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_REPORT_CONFIGURATION_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
						// ref_btn_deploy.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
						// ref_btn_Create.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
						ref_btnSave.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getDeletePossible()) {
						// ref_btn_Delete.setDisable(true);

					}
					break;

				default:
					break;
			}

		}
	}

	public void loadReportProfileList() {

		Set<String> hSet = new HashSet<String>(ConstantAppConfig.REPORT_PROFILE_LIST);
		hSet.addAll(ConstantAppConfig.REPORT_PROFILE_LIST);

		ref_cmbBxReportProfile.getItems().clear();
		ref_cmbBxReportProfile.getItems().addAll(hSet);
		// ref_cmbBxReportProfile.getSelectionModel().select(0);
		ref_cmbBxReportProfile.getSelectionModel().select(ConstantAppConfig.DefaultReportProfileDisplay);
		setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
		// LoadSavedData();
	}

	public String getSelectedReportProfile() {
		return selectedReportProfile;
	}

	public void setSelectedReportProfile(String selectedReportProfile) {
		this.selectedReportProfile = selectedReportProfile;
	}

	public void LoadTestType() {
		cmbBox_testtype.getItems().clear();
		cmbBox_testtype.getItems().addAll(ConstantReport.REPORT_TEST_TYPES_DISPLAY);
		cmbBox_testtype.getSelectionModel().select(0);
		LoadSavedData();
	}

	public void LoadSavedData() {
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		JSONObject data = MySQL_Controller.sp_getreport_file_location(getSelectedReportProfile(), test_type);
		try {
			if (!data.isNull("template_file_location")) {
				String template_file = data.getString("template_file_location");
				String save_file_loc = data.getString("save_file_location");

				template_file = template_file.replace("\\\\", "\\");
				save_file_loc = save_file_loc.replace("\\\\", "\\");
				txt_templ_file_location.setText(template_file);
				txt_save_file_location.setText(save_file_loc);
			} else {
				txt_templ_file_location.setText("");
				txt_save_file_location.setText("");
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadSavedData: JSONException : " + e.getMessage());
		}
	}

	public void SelectTemplFileOnClick() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select File location");
		java.io.File file = chooser.showOpenDialog(new Stage());
		ApplicationLauncher.logger.info("showOpenDialog file location" + file);
		String file_location = file.toString();
		txt_templ_file_location.setText(file_location);
	}

	public void SelectSaveFileOnClick() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(new Stage());

		if (selectedDirectory == null) {

			ApplicationLauncher.logger.info("No Directory selected");
			txt_save_file_location.setText("");
		} else {

			ApplicationLauncher.logger.info("showSaveDialog file location" + selectedDirectory.getAbsolutePath());
			txt_save_file_location.setText(selectedDirectory.getAbsolutePath() + "\\");
		}
	}

	public void SaveFileLocation() {
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		String templ_file_location = txt_templ_file_location.getText();
		String save_file_location = txt_save_file_location.getText();
		templ_file_location = templ_file_location.replace("\\", "\\\\");
		save_file_location = save_file_location.replace("\\", "\\\\");
		MySQL_Controller.sp_add_report_file_location(getSelectedReportProfile(), test_type, templ_file_location,
				save_file_location);
		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
	}

	@FXML
	public void launchReportV2_GUI_Click() {
		ApplicationLauncher.logger.info("launchReportV2_GUI_Click: Entry");
		// guiLaunchTimer = new Timer();
		// guiLaunchTimer.schedule(new StartRunTaskClick(), 50);
		Platform.runLater(() -> {
			launchReportV2_Gui();
		});
	}

	/*
	 * class StartRunTaskClick extends TimerTask {
	 * public void run() {
	 * Platform.runLater(() -> {
	 * launchReportV2_Gui();
	 * });
	 * 
	 * guiLaunchTimer.cancel();
	 * 
	 * 
	 * }
	 * }
	 */

	public void launchReportV2_Gui() {
		ApplicationLauncher.logger.info("launchReportV2_Gui: entry");
		// ApplicationLauncher.logger.info("MeterReadingPopup: entry");
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/reportprofile/ReportProfileConfig" + ConstantApp.THEME_FXML));

		// ApplicationLauncher.logger.info("MeterReadingPopup: loading 48 position");
		// loader = new
		// FXMLLoader(getClass().getResource("/fxml/deployment/MeterReadingPopup48" +
		// ConstantApp.THEME_FXML));

		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {

			ex.printStackTrace();
			ApplicationLauncher.logger.error("launchReportV2_Gui :IOException:" + ex.getMessage());
			return;
		}

		Stage SaveAsStage = new Stage();
		// https://stackoverflow.com/questions/36071779/how-to-open-an-additional-window-in-a-javafx-fxml-app?rq=1

		Stage primaryStage = ApplicationLauncher.getPrimaryStage();

		// SaveAsStage.initModality(Modality.WINDOW_MODAL);
		SaveAsStage.initModality(Modality.APPLICATION_MODAL);
		SaveAsStage.initOwner(primaryStage);
		// https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1

		SaveAsStage.setScene(newScene);
		// Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		SaveAsStage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		SaveAsStage.setAlwaysOnTop(false);
		// SaveAsStage.setOnCloseRequest(e->e.consume());
		SaveAsStage.setTitle("Report Gen V2");

		try {
			SaveAsStage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("launchReportV2_Gui: Exception :" + e.getMessage());

		}

	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	 * }
	 */
}
