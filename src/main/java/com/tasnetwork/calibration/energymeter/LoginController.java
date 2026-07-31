package com.tasnetwork.calibration.energymeter;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController extends AnchorPane {

	@FXML
	TextField txt_username;
	public static TextField ref_txt_username;

	@FXML
	PasswordField txt_password;
	public static PasswordField ref_txt_password;

	@FXML
	private Label lblProfileDisplay;

	@FXML
	private ComboBox cmbBxProfileDisplay;

	private static Label ref_lblProfileDisplay;

	private static ComboBox ref_cmbBxProfileDisplay;

	@FXML
	private void initialize() throws IOException {
		ref_txt_username = txt_username;
		ref_txt_password = txt_password;
		ref_lblProfileDisplay = lblProfileDisplay;
		ref_cmbBxProfileDisplay = cmbBxProfileDisplay;

		updateProfileDisplay();
		if (!ConstantAppConfig.DEFAULT_LOGIN_ID_POPULATE_ENABLED) {
			ref_txt_username.setText("");
			ref_txt_password.setText("");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ref_txt_username.requestFocus();
				}
			});
		}

		ref_txt_username.setText("admin");
		ref_txt_password.setText("admin");

	}

	public void updateProfileDisplay() {
		ApplicationLauncher.logger.debug("updateProfileDisplay : Entry");

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			ref_cmbBxProfileDisplay.getItems().clear();
			ref_cmbBxProfileDisplay.getItems().addAll(ConstantAppConfig.UAC_PROFILE_LIST);
			ref_cmbBxProfileDisplay.getSelectionModel().select(ConstantAppConfig.UAC_DEFAULT_PROFILE);
		} else {
			ref_lblProfileDisplay.setVisible(false);
			ref_cmbBxProfileDisplay.setVisible(false);
		}

	}

	public void keyBoardEnterAction(KeyEvent e) {
		if (e.getCode().equals(KeyCode.ENTER))
			LoginOnClick();
	}

	public void LoginOnClick() {
		String username = ref_txt_username.getText();
		String password = ref_txt_password.getText();
		String access_level = checkLoginCredentials(username, password);
		if (!access_level.equals("")) {

			if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
				String selectedUserProfileType = ref_cmbBxProfileDisplay.getSelectionModel().getSelectedItem()
						.toString();
				ApplicationLauncher.logger.info("LoginOnClick : selectedUserProfileType: " + selectedUserProfileType);
				ApplicationLauncher.logger.info("LoginOnClick : access_level: " + access_level);
				if (access_level.equals(selectedUserProfileType)) {
					ApplicationLauncher.logger.info("LoginOnClick : selectedUserProfileType matched: ");
					ConstantApp.USER_ACCESS_LEVEL = access_level;
					ConstantApp.USER_NAME = username;
					loadUacProfileData(access_level);
					DeviceDataManagerController.setUserName(username);
					loadInHouseMode(username);
					LaunchHomePage();
				} else {
					ApplicationLauncher.logger.info(
							"LoginOnClick : Incorrect Profile Type: Kindly select appropriate profile type or kindly contact admin - prompted");
					WindowManager.InformUser("Incorrect profile type",
							"Kindly select appropriate profile type or kindly contact admin", AlertType.ERROR);
				}
			} else {
				ConstantApp.USER_ACCESS_LEVEL = access_level;
				LaunchHomePage();
			}
		} else {
			ref_txt_username.clear();
			ref_txt_password.clear();
			ref_txt_username.requestFocus();
			WindowManager.InformUser("Invalid credential", "Invalid user name or password", AlertType.ERROR);
		}
	}

	private void loadInHouseMode(String username) {

		ApplicationLauncher.logger.info("loadInHouseMode : username: " + username);
		if (username.equals(ConstantApp.CALIBRATION_MODE_USER_NAME)) {
			ProCalCustomerConfiguration.IN_HOUSE_CALIBRATION_MODE = true;
			ProCalCustomerConfiguration.Init();

			ApplicationLauncher.logger.info("loadInHouseMode : calibration mode enabled");
		} else if (username.equals(ConstantApp.MAINTENANCE_MODE_USER_NAME)) {
			ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED = true;
			ApplicationLauncher.logger.info("loadInHouseMode : maintenance/self test mode enabled");
		}
	}

	public String checkLoginCredentials(String username, String password) {
		JSONObject result = MySQL_Controller.sp_getprocal_user_access_level(username, password);
		JSONArray userlist = new JSONArray();
		String access_level = "";
		try {
			userlist = result.getJSONArray("User_list");
		} catch (JSONException e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("checkLoginCredentials : JSONException1" + e1.getMessage());
		}
		JSONObject model = new JSONObject();
		for (int i = 0; i < userlist.length(); i++) {
			try {
				model = (JSONObject) userlist.get(i);
				access_level = model.getString("access_level");
			} catch (JSONException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("checkLoginCredentials : JSONException2" + e.getMessage());
			}
		}
		return access_level;
	}

	public void LaunchHomePage() {
		Stage stage = ApplicationLauncher.getPrimaryStage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/main/Home" + ConstantApp.THEME_FXML));
		} catch (IOException e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("LaunchHomePage : IOException1" + e1.getMessage());
		}

		// Set the application icon.
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));

		// Set minimum size
		stage.setMinWidth(500);
		stage.setMinHeight(400);

		Scene scene = new Scene(root);

		stage.setScene(scene);
		scene.setFill(Color.TRANSPARENT);

		stage.setTitle(ConstantVersion.APPLICATION_NAME);
		stage.setMaximized(true);

		stage.show();

		try {
			loadProjectPage(scene);
		} catch (IOException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("LaunchHomePage : IOException2" + e.getMessage());
		}

	}

	private void loadProjectPage(Scene scene) throws IOException {
		AnchorPane childpane = (AnchorPane) scene.lookup("#childPane");

	}

	private void loadUacProfileData(String profileName) {

		ApplicationLauncher.logger.debug("loadUacProfileData : Entry");
		ArrayList<UacDataModel> uacDataList = FetchUacProfileFromDB(profileName);
		DeviceDataManagerController.setUacSelectProfileScreenList(uacDataList);

	}

	public static ArrayList<UacDataModel> FetchUacProfileFromDB(String profileName) {

		ApplicationLauncher.logger.debug("FetchUacProfileFromDB: Entry");
		ArrayList<UacDataModel> uacList = new ArrayList<UacDataModel>();
		try {

			JSONObject datalist = MySQL_Controller.sp_get_Uac_data_by_profile(profileName);
			JSONArray uacDataList = datalist.getJSONArray("Summary_data");

			// ApplicationLauncher.logger.info(projectdatalist);
			JSONObject jobj = new JSONObject();
			String visibleEnabled = "N";
			String executePossible = "N";
			String addPossible = "N";
			String updatePossible = "N";
			String deletePossible = "N";

			boolean bVisibleEnabled = false;
			boolean bExecutePossible = false;
			boolean bAddPossible = false;
			boolean bUpdatePossible = false;
			boolean bDeletePossible = false;

			// String profileName = "";
			String screenName = "";
			String sectionName = "";
			String subSectionName = "";

			for (int i = 0; i < uacDataList.length(); i++) {
				jobj = uacDataList.getJSONObject(i);
				screenName = jobj.getString("screen_name");
				sectionName = jobj.getString("screen_section");
				subSectionName = jobj.getString("screen_sub_section");
				ApplicationLauncher.logger.debug("FetchUacProfileFromDB: screenName: " + screenName);
				ApplicationLauncher.logger.debug("FetchUacProfileFromDB: sectionName: " + sectionName);
				ApplicationLauncher.logger.debug("FetchUacProfileFromDB: subSectionName: " + subSectionName);
				// profileName = jobj.getString("profile_name");
				visibleEnabled = jobj.getString("visible_enabled");
				executePossible = jobj.getString("execute_possible");
				addPossible = jobj.getString("add_possible");
				updatePossible = jobj.getString("update_possible");
				deletePossible = jobj.getString("delete_possible");
				if (visibleEnabled.equals("Y")) {
					bVisibleEnabled = true;
				} else {
					bVisibleEnabled = false;
				}
				if (executePossible.equals("Y")) {
					bExecutePossible = true;
				} else {
					bExecutePossible = false;
				}

				if (addPossible.equals("Y")) {
					bAddPossible = true;
				} else {
					bAddPossible = false;
				}
				if (updatePossible.equals("Y")) {
					bUpdatePossible = true;
				} else {
					bUpdatePossible = false;
				}
				if (deletePossible.equals("Y")) {
					bDeletePossible = true;
				} else {
					bDeletePossible = false;
				}
				UacDataModel uacData = new UacDataModel(String.valueOf((i + 1)), screenName, sectionName,
						subSectionName,
						profileName, bVisibleEnabled, bExecutePossible, bAddPossible, bUpdatePossible, bDeletePossible);
				uacList.add(uacData);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchUacProfileFromDB: JSONException: " + e.getMessage());
		}
		return uacList;
	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	 * }
	 */
}
