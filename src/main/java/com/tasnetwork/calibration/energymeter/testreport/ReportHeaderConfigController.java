package com.tasnetwork.calibration.energymeter.testreport;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.ReportConfigManager;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
//import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class ReportHeaderConfigController implements Initializable {
	@FXML
	private ComboBox<String> cmbBox_testtype;

	@FXML
	private ComboBox<String> cmbBox_parameter;

	@FXML
	private TextField txt_value;

	@FXML
	private TextField txt_reference_value;

	@FXML
	private TextField txt_reference_extension;

	@FXML
	private Label lbl_reference_value;

	@FXML
	private ComboBox<String> cmbBox_extension;

	@FXML
	private ListView<String> listview_voltage;

	@FXML
	private ListView<String> listview_current;

	@FXML
	private ListView<String> listview_phase;

	@FXML
	private ListView<String> listview_frequency;

	@FXML
	private TitledPane titledpane_freq_harm;

	@FXML
	private Button btn_add;

	@FXML
	private Button btn_delete;

	@FXML
	private ComboBox<String> cmbBxReportProfile;

	static ComboBox<String> ref_cmbBxReportProfile;

	private String selectedReportProfile = "";

	public int Max_Size = 0;

	@FXML
	private Button btnSave;

	private static Button ref_btnSave;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ref_cmbBxReportProfile = cmbBxReportProfile;
		ref_btnSave = btnSave;
		loadReportProfileList();
		LoadTestTypes();
		LoadParameters();
		LoadExtensions();
		UPF_listener();
		txt_reference_extension.setEditable(false);

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}

	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("ReportHeaderConfigController : applyUacSettings :  Entry");
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

	public void LoadTestTypes() {
		ApplicationLauncher.logger.info("LoadTestTypes : Entry");

		cmbBox_testtype.getItems().clear();
		cmbBox_testtype.getItems().addAll(ConstantReport.REPORT_TEST_TYPES_DISPLAY);
		cmbBox_testtype.getSelectionModel().select(0);
	}

	public void LoadParameters() {
		ApplicationLauncher.logger.info("LoadParameters : Entry");
		ArrayList<String> parameters = new ArrayList<String>();
		// String test_type = cmbBox_testtype.getSelectionModel().getSelectedItem();
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		if (test_type.equals(TestProfileType.InfluenceFreq.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
			parameters.add(ConstantReport.PARAMETER_TYPE_FREQUENCY);
			titledpane_freq_harm.setText(ConstantReport.PARAMETER_TYPE_FREQUENCY);
		} else if (test_type.equals(TestProfileType.NoLoad.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
		} else if (test_type.equals(TestProfileType.STA.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
		}
		// else if(test_type.equals("UnbalancedLoad")){
		else if (test_type.equals(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)) {
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
		} else if (test_type.equals(TestProfileType.InfluenceHarmonic.toString())) {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
			parameters.add(ConstantReport.PARAMETER_TYPE_HARMONICS);
			titledpane_freq_harm.setText(ConstantReport.PARAMETER_TYPE_HARMONICS);
		} else {
			parameters.add(ConstantReport.PARAMETER_TYPE_VOLTAGE);
			parameters.add(ConstantReport.PARAMETER_TYPE_CURRENT);
			parameters.add(ConstantReport.PARAMETER_TYPE_PF);
		}

		cmbBox_parameter.getItems().clear();
		cmbBox_parameter.getItems().addAll(parameters);
		cmbBox_parameter.getSelectionModel().select(0);
		ClearAllListView();
		LoadReferenceValue();
		LoadSavedData(test_type);
		// if((test_type.equals("UnbalancedLoad")) &&
		// (listview_voltage.getItems().isEmpty())){
		if ((test_type.equals(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)) && (listview_voltage.getItems().isEmpty())) {
			LoadDefaultVoltages();
		}
	}

	public void LoadDefaultVoltages() {
		listview_voltage.getItems().addAll(ConstantReport.UNBALANCED_LOAD_TEMPL_VOLTAGES);
	}

	public void ClearAllListView() {
		listview_voltage.getItems().clear();
		listview_current.getItems().clear();
		listview_phase.getItems().clear();
		listview_frequency.getItems().clear();
	}

	public void LoadExtensions() {
		ApplicationLauncher.logger.info("LoadExtensions : Entry");
		ArrayList<String> units = new ArrayList<String>();
		if (!cmbBox_parameter.getSelectionModel().isEmpty()) {
			String parameter = cmbBox_parameter.getSelectionModel().getSelectedItem();
			if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
				units.add(ConstantReport.EXTENSION_TYPE_VOLTAGE_U);
			} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
				units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
				units.add(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
			} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
				units.add(ConstantReport.EXTENSION_TYPE_PHASE_UPF);
				units.add(ConstantReport.EXTENSION_TYPE_PHASE_L);
				units.add(ConstantReport.EXTENSION_TYPE_PHASE_C);
			} else {
				units.add("");
			}

			if (units.isEmpty()) {
				// cmbBox_extension.setDisable(true);
				// cmbBox_extension.getItems().clear();
			} else {
				cmbBox_extension.getItems().clear();
				cmbBox_extension.getItems().addAll(units);
				cmbBox_extension.getSelectionModel().select(0);
			}
			extension_on_click();
			LoadMaxSize(parameter);
		}
	}

	public void LoadMaxSize(String parameter) {
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				} else {
					Max_Size = 50;
				}
				break;

			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 7;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_HARMONICS)) {
					Max_Size = 2;
				}
				break;

			// case "NoLoad":
			case ConstantApp.TEST_PROFILE_NOLOAD:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				}
				break;

			// case "STA":
			case ConstantApp.TEST_PROFILE_STA:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				}
				break;

			// case "ConstantTest":
			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
					Max_Size = 1;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 2;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 1;
				}
				break;

			// case "UnbalancedLoad":
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
					Max_Size = 50;
				} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
					Max_Size = 50;
				}
				break;

			default:
				break;
		}
	}

	public void LoadReferenceValue() {
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		txt_reference_extension.setEditable(false);
		txt_reference_extension.clear();
		txt_reference_value.clear();
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				lbl_reference_value.setText("Reference Frequency");
				txt_reference_extension.setText("");
				txt_reference_value.setEditable(true);
				txt_reference_extension.setVisible(true);
				txt_reference_value.setVisible(true);
				break;

			// case "InfluenceVolt":
			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				lbl_reference_value.setText("    Reference Voltage");
				txt_reference_extension.setText("U");
				txt_reference_value.setEditable(true);
				txt_reference_extension.setVisible(true);
				txt_reference_value.setVisible(true);
				break;

			// case "Repeatability":
			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				lbl_reference_value.setText("       No of Readings");
				txt_reference_extension.setText("");
				txt_reference_value.setEditable(true);
				txt_reference_extension.setVisible(true);
				txt_reference_value.setVisible(true);
				break;

			// case "PhaseReversal":
			// case "InfluenceHarmonic":
			// case "NoLoad":
			// case "STA":
			// case "UnbalancedLoad":
			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_STA:
			case ConstantApp.TEST_PROFILE_NOLOAD:
			case ConstantApp.TEST_PROFILE_ACCURACY:
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				lbl_reference_value.setText("");
				txt_reference_extension.setText("");
				txt_reference_value.setEditable(false);
				txt_reference_extension.setVisible(false);
				txt_reference_value.setVisible(false);
				break;

			// case "ConstantTest":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
				lbl_reference_value.setText("     Reference Energy");
				txt_reference_extension.setText("kWh");
				txt_reference_value.setEditable(true);
				txt_reference_extension.setVisible(true);
				txt_reference_value.setVisible(true);
				break;

			default:
				break;
		}

	}

	public void extension_on_click() {
		ApplicationLauncher.logger.info("extension_on_click : Entry ");

		if (!cmbBox_extension.getSelectionModel().isEmpty()) {
			if (cmbBox_extension.getSelectionModel().getSelectedItem()
					.equals(ConstantReport.EXTENSION_TYPE_PHASE_UPF)) {
				txt_value.setEditable(true);
				txt_value.setText("1.0");
				txt_value.setEditable(false);
			} else {
				txt_value.setEditable(true);
			}
		}

	}

	public void UPF_listener() {
		txt_value.textProperty().addListener((observable, oldValue, newValue) -> {
			if (cmbBox_parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
				if (!txt_value.getText().isEmpty()) {
					float value = Float.parseFloat(txt_value.getText());
					if (value == 1) {
						cmbBox_extension.setValue(ConstantReport.EXTENSION_TYPE_PHASE_UPF);
					}
				}
			}
		});
	}

	public void LoadSavedData(String test_type) {
		ApplicationLauncher.logger.info("LoadSavedData : test_type: " + test_type);
		JSONObject report_header_config = MySQL_Controller.sp_getreport_header_config(getSelectedReportProfile(),
				test_type);
		try {
			JSONArray report_header_arr = report_header_config.getJSONArray("Report_Headers");
			ApplicationLauncher.logger.info("LoadSavedData : report_header_arr: " + report_header_arr);
			for (int i = 0; i < report_header_arr.length(); i++) {
				JSONObject jobj = report_header_arr.getJSONObject(i);
				String header_type = jobj.getString("header_type");
				String header_value = jobj.getString("header_value");
				LoadValuesToList(header_type, header_value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadSavedData: JSONException : " + e.getMessage());
		}
	}

	public void LoadValuesToList(String header_type, String header_value) {
		ApplicationLauncher.logger.info("LoadValuesToList : " + header_type + "-" + header_value);
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			listview_voltage.getItems().add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			listview_current.getItems().add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			listview_phase.getItems().add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_FREQUENCY)) {
			listview_frequency.getItems().add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_HARMONICS)) {
			listview_frequency.getItems().add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			String str_row = header_value.replaceAll("[^0-9,.]", "");
			txt_reference_value.setText(str_row);
		}
	}

	@FXML
	public void AddParameter() {
		ApplicationLauncher.logger.debug("AddParameter: Entry");
		String parameter = cmbBox_parameter.getSelectionModel().getSelectedItem();
		ApplicationLauncher.logger.debug("AddParameter: parameter: " + parameter);
		if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
			ApplicationLauncher.logger.debug("AddParameter: listview_voltage: " + listview_voltage);
			AddValue(listview_voltage);
		} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
			AddValue(listview_current);
		} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
			AddValue(listview_phase);
		} else {
			AddValue(listview_frequency);
		}
	}

	public void AddValue(ListView<String> listview) {
		ApplicationLauncher.logger.debug("AddValue: Entry: ");
		//
		ObservableList<String> values = listview.getItems();
		ApplicationLauncher.logger.debug("AddValue: values: " + values);
		if (values.size() < Max_Size) {
			String value = "";
			if (cmbBox_extension.getSelectionModel().getSelectedItem()
					.equals(ConstantReport.EXTENSION_TYPE_PHASE_UPF)) {
				value = txt_value.getText();
			} else {
				value = txt_value.getText() + cmbBox_extension.getSelectionModel().getSelectedItem();
			}
			if ((!checkdataexists(value, listview)) && (!txt_value.getText().isEmpty())) {
				listview.getItems().add(value);
				txt_value.clear();
			}
		}
	}

	public boolean checkdataexists(String value, ListView<String> listview) {
		boolean dataexists = false;
		ObservableList<String> existing_data = listview.getItems();
		if (existing_data.size() != 0) {
			for (int i = 0; i < existing_data.size(); i++) {
				String data = existing_data.get(i);
				if (value.equals(data)) {
					dataexists = true;
					break;
				} else {
					dataexists = false;
				}
			}
		} else {
			dataexists = false;
		}
		return dataexists;
	}

	public void DeleteParameter() {
		String parameter = cmbBox_parameter.getSelectionModel().getSelectedItem();
		if (parameter.equals(ConstantReport.PARAMETER_TYPE_VOLTAGE)) {
			DeleteValue(listview_voltage);
		} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_CURRENT)) {
			DeleteValue(listview_current);
		} else if (parameter.equals(ConstantReport.PARAMETER_TYPE_PF)) {
			DeleteValue(listview_phase);
		} else {
			DeleteValue(listview_frequency);
		}
	}

	public void DeleteValue(ListView<String> listview) {
		ObservableList<String> values = listview.getItems();
		String del_value = listview.getSelectionModel().getSelectedItem();
		ApplicationLauncher.logger.info("DeleteValue : " + del_value);
		values.remove(del_value);
	}

	public ObservableList<String> getValuesFromList(ListView<String> listview) {
		ObservableList<String> values = listview.getItems();
		ApplicationLauncher.logger.info("getValuesFromList : " + values);
		return values;
	}

	public ObservableList<String> getReference_value() {
		String ref_value = "";
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		if (!txt_reference_extension.getText().isEmpty()
				&& (!test_type.equals(TestProfileType.ConstantTest.toString()))) {
			ref_value = txt_reference_value.getText() + txt_reference_extension.getText();
		} else {
			ref_value = txt_reference_value.getText();
		}
		ObservableList<String> values = FXCollections.observableArrayList();
		values.add(ref_value);
		return values;
	}

	public void ReportHeaderSaveOnClick() {

		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		MySQL_Controller.sp_delete_report_header_config(getSelectedReportProfile(), test_type);
		ObservableList<String> voltages = getValuesFromList(listview_voltage);
		ObservableList<String> currents = getValuesFromList(listview_current);
		ObservableList<String> pfs = getValuesFromList(listview_phase);
		ObservableList<String> reference_value = getReference_value();
		// if(test_type.equals("VoltageUnbalance")){
		if (test_type.equals(TestProfileType.VoltageUnbalance.toString())) {
			String value = reference_value.get(0);
			// reference_value.set(0, "ABC:" + value);
			if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE) {
				reference_value.set(0, ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME
						+ ConstantApp.THIRD_PHASE_DISPLAY_NAME + ":" + value);
			} else {
				reference_value.set(0, "ABC:" + value);
			}
		}
		SaveToDB(test_type, ConstantReport.HEADER_TYPE_VOLTAGE, voltages);
		SaveToDB(test_type, ConstantReport.HEADER_TYPE_CURRENT, currents);
		SaveToDB(test_type, ConstantReport.HEADER_TYPE_PF, pfs);
		if (test_type.equals(TestProfileType.InfluenceFreq.toString())) {
			ObservableList<String> frequencies = getValuesFromList(listview_frequency);
			SaveToDB(test_type, ConstantReport.HEADER_TYPE_FREQUENCY, frequencies);
		}
		if (test_type.equals(TestProfileType.InfluenceHarmonic.toString())) {
			ObservableList<String> harmonics = getValuesFromList(listview_frequency);
			SaveToDB(test_type, ConstantReport.HEADER_TYPE_HARMONICS, harmonics);
		}
		if (!test_type.equals(TestProfileType.PhaseReversal.toString()) &&
				!test_type.equals(TestProfileType.InfluenceHarmonic.toString()) &&
				!test_type.equals(TestProfileType.NoLoad.toString()) &&
				!test_type.equals(TestProfileType.STA.toString()) &&
				!test_type.equals(TestProfileType.Accuracy.toString()) &&
				!test_type.equals(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)) {
			// !test_type.equals("UnbalancedLoad")){
			SaveToDB(test_type, ConstantReport.HEADER_TYPE_REFERENCE_VALUE, reference_value);
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle(ConstantVersion.APPLICATION_NAME);
		alert.setHeaderText("Clear report excel config");
		String s = "Do you want to clear the corresponding excel configuration?";
		alert.setContentText(s);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		// Deactivate Defaultbehavior for yes-Button:
		Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		yesButton.setDefaultButton(false);

		// Activate Defaultbehavior for no-Button:
		Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);

		noButton.setDefaultButton(true);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			MySQL_Controller.sp_delete_report_excel_config(getSelectedReportProfile(), test_type);
		}
		WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
	}

	public void SaveToDB(String test_type, String header_type, ObservableList<String> values) {
		ApplicationLauncher.logger.info("SaveToDB: values : " + values);
		if (values.size() != 0) {
			for (int i = 0; i < values.size(); i++) {
				MySQL_Controller.sp_add_report_header_config(getSelectedReportProfile(), test_type, header_type,
						values.get(i));
			}
			ReportConfigManager.LoadReportHeaderConfigProperty();
			ReportConfigManager.LoadReportExcelConfigProperty();
		}

	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	 * }
	 */
}
