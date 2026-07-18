package com.tasnetwork.calibration.energymeter.setting;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EM_ModelController implements Initializable {

	@FXML
	private ComboBox<String> cmbBxMeterType;

	@FXML
	private ComboBox<String> cmbBxCT_Type;

	@FXML
	private ComboBox<String> cmbBxFrequency;

	@FXML
	private TextField txtEM_Model_ModelName;

	@FXML
	private Label lbl_ratedVoltage;

	@FXML
	private TextField txtCustomerName;

	@FXML
	private TextField txtBaseCurrent;

	@FXML
	private TextField txtMaxCurrent;

	@FXML
	private TextField txtRatedVoltage;

	@FXML
	private TextField txtNoOfImpulsesPerKWH;

	@FXML
	private TextField txtEM_Model_ClassValue;

	@FXML
	private TextField txtPowerSourceModelName;

	@FXML
	private TextField txt_ctrratio;

	@FXML
	private TextField txt_ptrratio;

	@FXML
	private Button btn_add;

	@FXML
	private Button btn_remove;

	@FXML
	private Button btn_save;

	@FXML
	private Button btn_reset;

	private static Button ref_btn_add;
	private static Button ref_btn_remove;
	private static Button ref_btn_save;
	private static Button ref_btn_reset;

	private static Label ref_lbl_ratedVoltage;

	@FXML
	private TextField filterField;
	@FXML
	private TableView<EnergyMeterModel> customer_EM_Model_Table;
	@FXML
	private TableColumn<EnergyMeterModel, String> customerNameColumn;
	@FXML
	private TableColumn<EnergyMeterModel, String> emModelNameColumn;

	private ObservableList<EnergyMeterModel> emModelDataList = FXCollections.observableArrayList();

	public EM_ModelController() {
		RefreshModelList();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ref_btn_add = btn_add;
		ref_btn_remove = btn_remove;
		ref_btn_save = btn_save;
		ref_btn_reset = btn_reset;
		ref_lbl_ratedVoltage = lbl_ratedVoltage;

		updateMeterTypeInEMModel();
		updateCT_TypeInEMModel();
		updateFrequencyInEMModel();

		customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
		emModelNameColumn.setCellValueFactory(cellData -> cellData.getValue().emModelNameProperty());

		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<EnergyMeterModel> filteredData = new FilteredList<>(emModelDataList, em -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(energyMeterModelFilterData -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (energyMeterModelFilterData.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				} else if (energyMeterModelFilterData.getEmModelName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<EnergyMeterModel> sortedData = new SortedList<>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		// Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(customer_EM_Model_Table.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
		customer_EM_Model_Table.setItems(sortedData);

		customer_EM_Model_Table.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						ApplicationLauncher.logger.info("New row selected: " + newSelection.getCustomerName() + ":"
								+ newSelection.getEmModelName());
						loadEnergyMeterProperties(newSelection);
					}
				});
		ApplicationLauncher.logger.info(" User Level: Entry");
		if ((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL))
				|| (ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))) {
			ApplicationLauncher.logger.info(" User Level: Tester");
			btn_add.setDisable(true);
			btn_remove.setDisable(true);
			btn_save.setDisable(true);
			btn_reset.setDisable(true);
		}

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}

	}

	public void loadEnergyMeterProperties(EnergyMeterModel newSelection) {

		txtEM_Model_ModelName.setText(newSelection.getEmModelName());
		txtEM_Model_ClassValue.setText(newSelection.getClassValue());
		txtCustomerName.setText(newSelection.getCustomerName());
		txtBaseCurrent.setText(newSelection.getBaseCurrent());
		txtMaxCurrent.setText(newSelection.getMaxCurrent());
		txtRatedVoltage.setText(newSelection.getRatedVoltage());
		txtNoOfImpulsesPerKWH.setText(newSelection.getNoOfImpulsesPerKWH());
		cmbBxMeterType.getSelectionModel().select(newSelection.getMeterType());
		cmbBxFrequency.getSelectionModel().select(newSelection.getFrequency());
		cmbBxCT_Type.getSelectionModel().select(newSelection.getCt_Type());
		txt_ctrratio.setText(newSelection.getCtr_Ratio());
		txt_ptrratio.setText(newSelection.getPtr_Ratio());
	}

	public void updateCT_TypeInEMModel() {
		cmbBxCT_Type.getItems().clear();
		cmbBxCT_Type.getItems().addAll(ConstantApp.METER_CT_TYPE_LTCT, ConstantApp.METER_CT_TYPE_HTCT);
		cmbBxCT_Type.getSelectionModel().select(ConstantApp.DEFAULT_CT_TYPE);

	}

	public void updateMeterTypeInEMModel() {
		cmbBxMeterType.getItems().clear();
		cmbBxMeterType.getItems().addAll(ConstantApp.MeterType);
		cmbBxMeterType.getSelectionModel().select(ConstantApp.DefaultMeterType);

	}

	public void updateFrequencyInEMModel() {
		cmbBxFrequency.getItems().clear();
		cmbBxFrequency.getItems().addAll(ConstantApp.Frequency);
		cmbBxFrequency.getSelectionModel().select(4);
	}

	public void onEmModelAddClick() {

		emModelDataList.add(new EnergyMeterModel("", "", ConstantApp.DefaultMeterType, "0.0", "0.0", "0.0", "0", "0.0",
				"50", ConstantApp.DEFAULT_CT_TYPE, "1", "1"));

		int row = emModelDataList.size() - 1;

		customer_EM_Model_Table.requestFocus();
		customer_EM_Model_Table.getSelectionModel().select(row);
		customer_EM_Model_Table.getFocusModel().focus(row);
	}

	public void onEmModelRemoveClick() {
		int row = customer_EM_Model_Table.getSelectionModel().getSelectedIndex();

		EnergyMeterModel em = (EnergyMeterModel) customer_EM_Model_Table.getSelectionModel().getSelectedItem();

		MySQL_Controller.sp_delete_em_model(em.getCustomerName(), em.getEmModelName());
		RefreshModelList();
	}

	public void onEmModelResetClick() {
		txtCustomerName.setText("");
		txtEM_Model_ModelName.setText("");
		cmbBxMeterType.getSelectionModel().select(ConstantApp.DefaultMeterType);
		cmbBxCT_Type.getSelectionModel().select(ConstantApp.DEFAULT_CT_TYPE);
		txtBaseCurrent.setText("0.0");
		txtMaxCurrent.setText("0.0");
		txtRatedVoltage.setText("0.0");
		txtNoOfImpulsesPerKWH.setText("0");
		txtEM_Model_ClassValue.setText("0.0");
		cmbBxFrequency.getSelectionModel().select(50);
		txt_ctrratio.setText("1");
		txt_ptrratio.setText("1");
	}

	public void onEmModelSaveClick() {
		int row = customer_EM_Model_Table.getSelectionModel().getSelectedIndex();
		String customer_name = txtCustomerName.getText();
		String model_name = txtEM_Model_ModelName.getText();
		String model_type = cmbBxMeterType.getSelectionModel().getSelectedItem();
		String model_class = txtEM_Model_ClassValue.getText();
		String current_ib = txtBaseCurrent.getText();
		String current_imax = txtMaxCurrent.getText();
		String voltage_vd = txtRatedVoltage.getText();
		String impulses_per_unit = txtNoOfImpulsesPerKWH.getText();
		String frequency = cmbBxFrequency.getSelectionModel().getSelectedItem();
		String ct_type = cmbBxCT_Type.getSelectionModel().getSelectedItem();
		String ctr_ratio = txt_ctrratio.getText();
		String ptr_ratio = txt_ptrratio.getText();
		// ProjectController.setProjectEM_CT_Type(ct_type);
		boolean status = Validate_User_em_input_parameters(customer_name,
				model_name, model_type, model_class, current_ib, current_imax,
				voltage_vd, impulses_per_unit, frequency, ct_type, ctr_ratio, ptr_ratio);
		if (status) {
			MySQL_Controller.sp_add_em_model(customer_name,
					model_name, model_type, model_class, current_ib,
					current_imax, voltage_vd, impulses_per_unit,
					frequency, ct_type, ctr_ratio, ptr_ratio);
			RefreshModelList();
			customer_EM_Model_Table.getSelectionModel().select(row);
			customer_EM_Model_Table.getFocusModel().focus(row);
			InformUser("Save success", "Data saved successfully", AlertType.INFORMATION);
		} else {
			ApplicationLauncher.logger.info("Validate_User_em_input_parameters:  Failure");
			InformUser("Save failure", "Data save failed due to invalid data", AlertType.ERROR);
		}
	}

	public boolean Validate_User_em_input_parameters(String customer_name,
			String model_name, String model_type, String model_class,
			String current_ib, String current_imax, String voltage_vd,
			String impulses_per_unit, String frequency, String ct_type, String ctr_ratio,
			String ptr_ratio) {
		boolean validation_status = false;
		validation_status = GuiUtils.Validate_voltage(voltage_vd);
		if (validation_status) {
			ApplicationLauncher.logger.info("Validate_User_em_input_parameters:  voltage_vd: Success");
			validation_status = GuiUtils.Validate_current(current_ib);
			if (validation_status) {
				ApplicationLauncher.logger.info("Validate_User_em_input_parameters:  current_ib: Success");
				validation_status = GuiUtils.Validate_current(current_imax);
				if (validation_status) {
					ApplicationLauncher.logger.info("Validate_User_em_input_parameters:  current_imax: Success");
					validation_status = Validate_impulses_per_unit(impulses_per_unit);
					if (validation_status) {
						ApplicationLauncher.logger
								.info("Validate_User_em_input_parameters:  impulses_per_unit: Success");
						validation_status = GuiUtils.Validate_frequency(frequency);
						if (validation_status) {
							ApplicationLauncher.logger.info("Validate_User_em_input_parameters:  frequency: Success");
							validation_status = GuiUtils.is_float(ctr_ratio);
							if (validation_status) {
								ApplicationLauncher.logger
										.info("Validate_User_em_input_parameters:  ctr_ratio: Success");
								validation_status = GuiUtils.is_float(ptr_ratio);
							}
						}
					}
				}
			}
		}

		return validation_status;
	}

	public boolean Validate_impulses_per_unit(String impulses_per_unit) {
		if (GuiUtils.FormatPulseRate(impulses_per_unit) != null) {
			return true;
		}
		return false;

	}

	public void RefreshModelList() {
		emModelDataList.clear();
		JSONObject Modeldata = MySQL_Controller.sp_getem_model_list();
		ApplicationLauncher.logger.info("RefreshModelList: Modeldata: " + Modeldata);
		JSONArray modellist = new JSONArray();
		try {
			modellist = Modeldata.getJSONArray("EM_models");
			ApplicationLauncher.logger.info("RefreshModelList:ModelList: " + modellist);
		} catch (JSONException e1) {

			e1.printStackTrace();
			ApplicationLauncher.logger.error("RefreshModelList: JSONException:" + e1.getMessage());
		}
		for (int i = 0; i < modellist.length(); i++) {

			String customer_name = "";
			String model_name = "";
			String model_type = "";
			String model_class = "";
			String current_ib = "";
			String current_imax = "";
			String voltage_vd = "";
			String impulses_per_unit = "";
			String model_freq = "";
			String ct_type = "";
			String ctr_ratio = "";
			String ptr_ratio = "";
			try {
				JSONObject model = (JSONObject) modellist.get(i);
				customer_name = model.getString("customer_name");
				model_name = model.getString("model_name");
				model_type = model.getString("model_type");
				ct_type = model.getString("ct_type");
				model_class = model.getString("model_class");
				current_ib = model.getString("basic_current_ib");
				current_imax = model.getString("max_current_imax");
				voltage_vd = model.getString("rated_voltage_vd");
				impulses_per_unit = model.getString("impulses_per_unit");
				model_freq = model.getString("frequency");
				ctr_ratio = model.getString("ctr_ratio");
				ptr_ratio = model.getString("ptr_ratio");
				ApplicationLauncher.logger.info("RefreshModelList: customer_name: " + customer_name);
			} catch (JSONException e) {

				ApplicationLauncher.logger.error("RefreshModelList: JSONException: " + e.toString());
				e.printStackTrace();
			}
			emModelDataList.add(new EnergyMeterModel(customer_name,
					model_name, model_type, current_ib, current_imax,
					voltage_vd, impulses_per_unit, model_class,
					model_freq, ct_type, ctr_ratio, ptr_ratio));
		}

	}

	public static void InformUser(String title, String info, AlertType Alert_type) {
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("EM_ModelController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_METER_PROFILE_SCREEN:

					if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
						// ref_btn_get_results.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
						ref_btn_add.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
						ref_btn_save.setDisable(true);

					}

					if (!uacSelectProfileScreenList.get(i).getDeletePossible()) {
						ref_btn_remove.setDisable(true);
						ref_btn_reset.setDisable(true);

					}
					break;

				default:
					break;
			}

		}
	}

	@FXML
	public void cmbBxMetrTypeOnChange() {

		String selectedMeterType = cmbBxMeterType.getSelectionModel().getSelectedItem().toString();
		if (selectedMeterType.contains(ConstantApp.METER_TYPE_THREE_PHASE_DELTA_ACTIVE)) {
			ref_lbl_ratedVoltage.setText(ConstantApp.LABEL_DISPLAY_THREE_PHASE_DELTA);
		} else if (selectedMeterType.contains(ConstantApp.METER_TYPE_THREE_PHASE_DELTA_REACTIVE)) {
			ref_lbl_ratedVoltage.setText(ConstantApp.LABEL_DISPLAY_THREE_PHASE_DELTA);
		} else {
			ref_lbl_ratedVoltage.setText(ConstantApp.LABEL_DISPLAY_THREE_PHASE_STAR);
		}
	}

}
