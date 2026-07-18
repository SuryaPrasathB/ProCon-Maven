package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class StabilizationValidationController implements Initializable {

	@FXML
	private TextField txt_time;

	@FXML
	private TextField txt_retry;

	@FXML
	private TextField txt_total_time;

	@FXML
	private TextField txt_volt;

	@FXML
	private TextField txt_current;

	@FXML
	private TextField txt_phase;

	@FXML
	private TextField txt_freq;

	@FXML
	private TextField txt_har_volt;

	@FXML
	private TextField txt_har_current;

	@FXML
	private Button btn_Save;
	private static Button ref_btn_Save;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ref_btn_Save = btn_Save;
		AutoCalculate_TotalTime();
		LoadPropertyValues();
		/*
		 * if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
		 * (ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
		 * btn_Save.setDisable(true);
		 * }
		 */

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}
	}

	public void LoadPropertyValues() {
		ApplicationLauncher.logger
				.info("ConfigProperty.SECOND_VALIDATION_WAIT_TIME" + ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME);
		ApplicationLauncher.logger
				.info("ConfigProperty.SECOND_VALIDATION_RETRY_COUNT" + ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT);

		int time = ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME / ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT;
		txt_time.setText(Integer.toString(time));
		txt_retry.setText(Integer.toString(ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT));
		txt_volt.setText(Integer.toString(ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE));
		txt_current.setText(Integer.toString(ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE));
		txt_phase.setText(Integer.toString(ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE));
		txt_freq.setText(Float.toString(ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE));
		txt_har_volt.setText(Integer.toString(ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE));
		txt_har_current.setText(Integer.toString(ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE));

	}

	public void AutoCalculate_TotalTime() {
		txt_time.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!txt_retry.getText().isEmpty()) {
				if (GuiUtils.is_number(txt_retry.getText())) {
					if (GuiUtils.is_number(txt_time.getText())) {
						String time = txt_time.getText();
						String retry = txt_retry.getText();
						int time_duration = Integer.parseInt(time);
						int retry_count = Integer.parseInt(retry);
						int total_duration = time_duration * retry_count;
						txt_total_time.setText(Integer.toString(total_duration));
					}
				}
			}
		});
		txt_retry.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!txt_time.getText().isEmpty()) {
				if (GuiUtils.is_number(txt_time.getText())) {
					if (GuiUtils.is_number(txt_retry.getText())) {
						String time = txt_time.getText();
						String retry = txt_retry.getText();
						int time_duration = Integer.parseInt(time);
						int retry_count = Integer.parseInt(retry);
						int total_duration = time_duration * retry_count;
						txt_total_time.setText(Integer.toString(total_duration));
					}
				}
			}
		});
	}

	public void SaveStabilityValidation() {
		String time = txt_time.getText();
		String retry = txt_retry.getText();
		String tol_voltage = txt_volt.getText();
		String tol_current = txt_current.getText();
		String tol_phase = txt_phase.getText();
		String tol_freq = txt_freq.getText();
		String tol_har_volt = txt_har_volt.getText();
		String tol_har_current = txt_har_current.getText();
		int total_duration = 0;
		if (GuiUtils.isNumber(time)) {
			if (GuiUtils.isNumber(retry)) {
				int time_duration = Integer.parseInt(time);
				int retry_count = Integer.parseInt(retry);
				total_duration = time_duration * retry_count;
				ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME = total_duration;
				ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT = retry_count;
			}
		}
		if (GuiUtils.isNumber(tol_voltage)) {
			if (GuiUtils.isNumber(tol_current)) {
				if (GuiUtils.isNumber(tol_phase)) {
					if (GuiUtils.is_float(tol_freq)) {
						if (GuiUtils.isNumber(tol_har_volt)) {
							if (GuiUtils.isNumber(tol_har_current)) {
								MySQL_Controller.sp_add_system_config(
										ConstantApp.SECOND_VALIDATION_WAIT_TIME_IN_SEC_KEY,
										Integer.toString(total_duration));
								MySQL_Controller.sp_add_system_config(ConstantApp.SECOND_VALIDATION_RETRY_COUNT_KEY,
										retry);
								MySQL_Controller.sp_add_system_config(ConstantApp.VOLTAGE_ACCEPTED_PERCENTAGE_KEY,
										tol_voltage);
								MySQL_Controller.sp_add_system_config(ConstantApp.CURRENT_ACCEPTED_PERCENTAGE_KEY,
										tol_current);
								MySQL_Controller.sp_add_system_config(ConstantApp.PHASE_ACCEPTED_PERCENTAGE_KEY,
										tol_phase);
								MySQL_Controller.sp_add_system_config(ConstantApp.FREQUENCY_ACCEPTED_PERCENTAGE_KEY,
										tol_freq);
								MySQL_Controller.sp_add_system_config(ConstantApp.HAR_VOLTAGE_ACCEPTED_PERCENTAGE_KEY,
										tol_har_volt);
								MySQL_Controller.sp_add_system_config(ConstantApp.HAR_CURRENT_ACCEPTED_PERCENTAGE_KEY,
										tol_har_current);
								ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(tol_voltage);
								ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(tol_current);
								ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE = Integer.parseInt(tol_phase);
								ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE = Float.parseFloat(tol_freq);
								ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE = Integer.parseInt(tol_har_volt);
								ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE = Integer.parseInt(tol_har_current);
								ApplicationLauncher.logger.info("SaveStabilityValidation: save success");

								InformUser("Save success", "Data saved successfully", AlertType.INFORMATION);
							} else {
								ApplicationLauncher.logger.info(
										"SaveStabilityValidation: save failed: harmonic current percentage is not a number");
								InformUser("Save failed",
										"Data saved failed, harmonic current percentage is not a number",
										AlertType.ERROR);
							}
						} else {
							ApplicationLauncher.logger.info(
									"SaveStabilityValidation: save failed: harmonic voltage percentage is not a number");
							InformUser("Save failed", "Data saved failed, harmonic voltage percentage is not a number",
									AlertType.ERROR);
						}
					} else {
						ApplicationLauncher.logger
								.info("SaveStabilityValidation: save failed: frequency percentage is not a float");
						InformUser("Save failed", "Data saved failed, frequency percentage is not a float",
								AlertType.ERROR);
					}
				} else {
					ApplicationLauncher.logger
							.info("SaveStabilityValidation: save failed: phase percentage is not a number");
					InformUser("Save failed", "Data saved failed, phase percentage is not a number", AlertType.ERROR);
				}
			} else {
				ApplicationLauncher.logger
						.info("SaveStabilityValidation: save failed: current percentage is not a number");
				InformUser("Save failed", "Data saved failed, current percentage is not a number", AlertType.ERROR);
			}
		} else {
			ApplicationLauncher.logger.info("SaveStabilityValidation: save failed: voltage percentage is not a number");
			InformUser("Save failed", "Data saved failed, voltage percentage is not a number", AlertType.ERROR);
		}
	}

	public static void InformUser(String title, String info, AlertType Alert_type) {
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("StabilizationValidationController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {
			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_STABILITY_SETTINGS_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
						// ref_btn_deploy.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
						// ref_btn_Create.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
						// ref_vbox_testscript.setDisable(true);sdvsc
						// setChildPropertySaveEnabled(false);
						ref_btn_Save.setDisable(true);

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
}
