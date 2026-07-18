package com.tasnetwork.calibration.energymeter;

import java.io.IOException;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LicenseHandleController extends AnchorPane {

	@FXML
	private PasswordField txt_LicenseEntry;
	@FXML
	private PasswordField txt_SaltedKey;
	@FXML
	private Label lbl_ErrorCodeDisplay;

	// private static boolean licenseMatched = false;

	public static PasswordField ref_txt_LicenseEntry;
	public static PasswordField ref_txt_SaltedKey;
	public static Label ref_lbl_ErrorCodeDisplay;

	@FXML
	private void initialize() throws IOException {
		refAssignment();

	}

	private void refAssignment() {

		ref_txt_LicenseEntry = txt_LicenseEntry;
		ref_txt_SaltedKey = txt_SaltedKey;
		ref_lbl_ErrorCodeDisplay = lbl_ErrorCodeDisplay;
		// setLicenseMatched(false);
	}

	public void promptSuccessAndExitApp() {
		ApplicationLauncher.logger.debug("promptSuccessAndExitApp: Entry");
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle(ConstantVersion.APPLICATION_NAME);
		alert.setHeaderText("License accepted");
		String displayMessage = "License installed successfully\n\nKindly relaunch the application again manually\n\n";
		alert.setContentText(displayMessage);
		alert.showAndWait();
		ApplicationLauncher.logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
				+ " application with error code L002---------->\n");
		Platform.exit();
		System.exit(0);
	}

	public void promptFailureAndExitApp() {
		ApplicationLauncher.logger.debug("promptFailureAndExitApp: Entry");
		Alert alert = new Alert(AlertType.ERROR);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle(ConstantVersion.APPLICATION_NAME);
		alert.setHeaderText("ErrorCode L001: License update failed");
		String displayMessage = "License installation failed, kindly contact the supplier";
		alert.setContentText(displayMessage);
		alert.showAndWait();
		ApplicationLauncher.logger.info("<------------Exit " + ConstantVersion.APPLICATION_NAME
				+ " application with error code L001---------->\n");
		Platform.exit();
		System.exit(0);
	}

	@FXML
	public void keyBoardEnterAction(KeyEvent e) {
		ApplicationLauncher.logger.debug("keyBoardEnterAction : Entry");
		if (e.getCode().equals(KeyCode.ENTER))
			btnOkOnClick();
	}

	@FXML
	private void btnOkOnClick() {
		ApplicationLauncher.logger.debug("btnOkOnClick: Entry");
		String userInputSaltedKeyStr = ref_txt_SaltedKey.getText();
		String userInputLicenseEntryStr = ref_txt_LicenseEntry.getText();
		// String userInputSaltedKeyWithNumbersOnlyStr = "";
		boolean isValidLicenseEntry = GuiUtils.is_long(userInputLicenseEntryStr);
		boolean isValidSaltedEntry = false;// GuiUtils.is_long(userInputSaltedKey) ;

		if (!userInputLicenseEntryStr.isEmpty()) {
			if (!userInputSaltedKeyStr.isEmpty()) {
				if (userInputSaltedKeyStr.equals(ConstantApp.USER_LICENCE_SKIP_SALTED_KEY_VALIDATION_DEFAULT_VALUE)) {
					isValidSaltedEntry = true;
					// userInputSaltedKeyWithNumbersOnlyStr = userInputSaltedKeyStr;
				} else {
					String userInputSaltedKeyNumbersOnly = userInputSaltedKeyStr.replaceAll("[^0-9]", "");// remove all
																											// the
																											// alphabets
					// ApplicationLauncher.logger.debug("btnOkOnClick: userInputSaltedKeyStr with
					// only numbers: " + userInputSaltedKeyStr);
					isValidSaltedEntry = GuiUtils.is_long(userInputSaltedKeyNumbersOnly);
				}

				if (isValidLicenseEntry) {

					if (isValidSaltedEntry) {

						long licenceEntry = Long.parseLong(userInputLicenseEntryStr);

						Map<Boolean, String> islicenceValueMatching = GuiUtils
								.isLicenseVerificationMatching_V1_0(licenceEntry, userInputSaltedKeyStr);

						if (islicenceValueMatching.containsKey(true)) {
							ApplicationLauncher.logger.debug("                      ");
							boolean status = MySQL_Controller.sp_ltUpdateSystemConfig(ConstantApp.SYSTEM_CONFIG_KEY,
									userInputLicenseEntryStr);
							if (status) {
								promptSuccessAndExitApp();
							} else {
								promptFailureAndExitApp();
							}
							// setLicenseMatched(true);
							// ApplicationLauncher.logger.debug(" 10: license Entry Invalid - prompted");
							// WindowManager.InformUser("", "License update success",
							// AlertType.ERROR);

							// ref_txt_SaltedKey.getScene().getWindow().hide();
						} else {
							String errorCode = "";
							if (islicenceValueMatching.containsKey(false)) {
								errorCode = islicenceValueMatching.get(false);
								ApplicationLauncher.logger.debug("btnOkOnClick: errorCode: " + errorCode);
								ref_lbl_ErrorCodeDisplay.setText("Error Code : " + errorCode);
							}
							ApplicationLauncher.logger.debug("1: license Entry Invalid  - prompted");
							WindowManager.InformUser(" 1 ", "Invalid License", AlertType.ERROR);
						}
					} else {
						ApplicationLauncher.logger.debug(" 10: license Entry Invalid  - prompted");
						WindowManager.InformUser(" 10 ", "Invalid License", AlertType.ERROR);
					}

				} else {
					ApplicationLauncher.logger.debug("100: license Entry Invalid  - prompted");
					WindowManager.InformUser(" 100 ", "Invalid License", AlertType.ERROR);
				}
			} else {
				ApplicationLauncher.logger.debug("101: license Entry Invalid  - prompted");
				WindowManager.InformUser(" 101 ", "Invalid License", AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.debug("102: license Entry Invalid  - prompted");
			WindowManager.InformUser(" 102 ", "Invalid License", AlertType.ERROR);
		}

	}

}
