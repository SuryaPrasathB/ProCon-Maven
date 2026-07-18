package com.tasnetwork.calibration.energymeter.testreport;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
//import com.tasnetwork.calibration.energymeter.deployment.DeploymentDataModel;
//import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.MyFloatStringConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class ReportExcelConfigController implements Initializable {

	@FXML
	private ComboBox<String> cmbBox_testtype;

	@FXML
	private ComboBox<String> cmbBxReportProfile;

	static ComboBox<String> ref_cmbBxReportProfile;

	private String selectedReportProfile = "";

	@FXML
	private TableView<ExcelCellValueModel> table_page_excel;

	@FXML
	private TableView<ExcelCellValueModel> table_sec_excel;

	@FXML
	private TableColumn<ExcelCellValueModel, String> col_page_header;

	@FXML
	private TableColumn<ExcelCellValueModel, String> col_page_cell_value;

	@FXML
	private TableColumn<ExcelCellValueModel, String> col_sec_header;

	@FXML
	private TableColumn<ExcelCellValueModel, String> col_sec_cell_value;

	@FXML
	private Label label_MaxPages;

	@FXML
	private Label label_NoOfSectionPerPage;

	// @FXML
	// private ComboBox cmbBoxMaxPages;

	@FXML
	private TextField txtNoOfPages;

	@FXML
	private TextField txtNoOfSectionPerPage;

	@FXML
	private Button btnSave;

	private static Button ref_btnSave;

	private ObservableList<ExcelCellValueModel> page_cell_values = FXCollections.observableArrayList();
	private ObservableList<ExcelCellValueModel> sec_cell_values = FXCollections.observableArrayList();

	// private int No_Of_PagesInReport;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ref_cmbBxReportProfile = cmbBxReportProfile;

		ref_btnSave = btnSave;
		loadReportProfileList();
		LoadTestType();

		LoadTableColProperty();
		LoadExcelConfig();
		LoadMaxPageData();
		LoadSectionPerPageData();
		setVisiblityForMaxPagesAndSectionPerPage(false);

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}
	}

	private static void applyUacSettings() {

		ApplicationLauncher.logger.info("ReportExcelConfigController : applyUacSettings :  Entry");
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

	public void setVisiblityForMaxPagesAndSectionPerPage(boolean value) {

		label_MaxPages.setVisible(value);
		label_NoOfSectionPerPage.setVisible(value);
		// cmbBoxMaxPages.setVisible(value);
		txtNoOfPages.setVisible(value);
		txtNoOfSectionPerPage.setVisible(value);
	}

	public void LoadMaxPageData() {

		txtNoOfPages.setText(String.valueOf(ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT));

	}

	public void LoadSectionPerPageData() {

		if (ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT > 1) {
			txtNoOfSectionPerPage.setText(String.valueOf(ConstantAppConfig.ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE));
		} else {
			txtNoOfSectionPerPage.setText("");
		}

	}

	/*
	 * public void LoadMaxPageData(){
	 * 
	 * for (int i = 0; i <ConstantReport.ACC_MAX_NO_OF_PAGES_IN_REPORT; i++){
	 * cmbBoxMaxPages.getItems().add(i+1);
	 * }
	 * //cmbBoxMaxPages.getSelectionModel().select(ConstantReport.
	 * ACC_NO_OF_PAGES_IN_REPORT);;
	 * 
	 * }
	 */

	/*
	 * public void maxPageOnChange(){
	 * 
	 * No_Of_PagesInReport =
	 * Integer.parseInt(cmbBoxMaxPages.getSelectionModel().getSelectedItem().
	 * toString());
	 * LoadExcelConfig();
	 * }
	 */

	public void LoadTestType() {
		cmbBox_testtype.getItems().clear();
		cmbBox_testtype.getItems().addAll(ConstantReport.REPORT_TEST_TYPES_DISPLAY);
		cmbBox_testtype.getSelectionModel().select(0);
	}

	public void LoadExcelConfig() {
		// String Test_type = cmbBox_testtype.getSelectionModel().getSelectedItem();
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		ArrayList<String> pages = new ArrayList<String>();
		ArrayList<String> sec = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = LoadSavedData(test_type);
		setVisiblityForMaxPagesAndSectionPerPage(false);
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				pages = ConstantReport.FREQ_TEMPL_PFS;
				sec = ConstantReport.FREQ_TEMPL_CURRENTS;
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				pages = ConstantReport.VV_TEMPL_PFS;
				sec = ConstantReport.VV_TEMPL_CURRENTS;
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				pages = ConstantReport.REP_TEMPL_CURRENTS;
				// pages.add("Meter");
				sec.add("First Reading");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				pages.add("Meter");
				sec = ConstantReport.SELF_HEAT_TEMPL_PFS;
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				setVisiblityForMaxPagesAndSectionPerPage(true);
				// if(No_Of_PagesInReport < ConstantReport.ACC_MAX_NO_OF_PAGES_IN_REPORT){
				if (ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT < ConstantReport.ACC_MAX_NO_OF_PAGES_IN_REPORT) {
					pages.add("Meter");
				} else {
					pages.add("Meter-Page1");
					pages.add("Meter-Page2");
				}
				sec = ConstantReport.ACC_TEMPL_PFS;
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				pages.add("Meter");
				sec.add("RPS Normal");
				sec.add("RPS Reversal");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				pages = ConstantReport.HARM_TEMPL_HARM_TIMES;
				// pages.add("Meter");
				sec.add("In Phase");
				sec.add("Anti Phase");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				pages.add("Meter");
				sec.add("Reference Voltage");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "ConstantTest":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
				pages.add("Meter");
				sec.add("Initial KWh");
				sec.add("Final KWh");
				sec.add("CTR");
				sec.add("PTR");
				sec.add("Pulse Count");
				sec.add("Reference Initial KWh");
				sec.add("Reference Final KWh");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "NoLoad":
			// case "STA":
			case ConstantApp.TEST_PROFILE_NOLOAD:
			case ConstantApp.TEST_PROFILE_STA:
				pages.add("Meter");
				sec.add("Initial KWh");
				sec.add("Final KWh");
				sec.add("CTR");
				sec.add("PTR");
				sec.add("Pulse Count");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			// case "UnbalancedLoad":

			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				pages = ConstantReport.UNBALANCED_LOAD_TEMPL_PFS;
				sec.add("All Phase");
				sec.add("R Phase");
				sec.add("Y Phase");
				sec.add("B Phase");
				LoadTable(table_page_excel, pages, data.get(0), page_cell_values);
				LoadTable(table_sec_excel, sec, data.get(1), sec_cell_values);
				break;
			default:
				break;
		}
	}

	public void LoadTableColProperty() {
		table_page_excel.setEditable(true);
		table_sec_excel.setEditable(true);

		col_page_header.setCellValueFactory(cellData -> cellData.getValue().headerProperty());
		col_sec_header.setCellValueFactory(cellData -> cellData.getValue().headerProperty());

		col_page_cell_value.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		col_page_cell_value.setCellFactory(TextFieldTableCell.forTableColumn());
		col_page_cell_value.setOnEditCommit(new EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
			public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
				ExcelCellValueModel rowData = ((ExcelCellValueModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setcell_value(t.getNewValue().toUpperCase());
				}
			}
		});

		col_sec_cell_value.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));
		col_sec_cell_value.setCellFactory(TextFieldTableCell.forTableColumn());
		col_sec_cell_value.setOnEditCommit(new EventHandler<CellEditEvent<ExcelCellValueModel, String>>() {
			public void handle(CellEditEvent<ExcelCellValueModel, String> t) {
				ExcelCellValueModel rowData = ((ExcelCellValueModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setcell_value(t.getNewValue().toUpperCase());
				}
			}
		});
	}

	public void LoadTable(TableView<ExcelCellValueModel> table, ArrayList<String> headers, ArrayList<String> values,
			ObservableList<ExcelCellValueModel> table_data) {
		table_data.clear();
		ApplicationLauncher.logger.info("LoadTable : table: " + table.getId());
		ApplicationLauncher.logger.info("LoadTable : headers: " + headers);
		ApplicationLauncher.logger.info("LoadTable : values: " + values);
		if (headers.size() == values.size()) {
			ApplicationLauncher.logger.info("LoadTable : Match ");
			for (int i = 0; i < headers.size(); i++) {
				table_data.add(new ExcelCellValueModel(headers.get(i), values.get(i)));
			}
			table.getItems().setAll(table_data);
		} else {
			ApplicationLauncher.logger.info("LoadTable : Mismatch ");
			for (int i = 0; i < headers.size(); i++) {
				table_data.add(new ExcelCellValueModel(headers.get(i), ""));
			}
			table.getItems().setAll(table_data);
		}
	}

	public ArrayList<ArrayList<String>> LoadSavedData(String test_type) {
		ApplicationLauncher.logger.info("LoadSavedData : Entry: ");
		JSONObject report_excel_config = MySQL_Controller.sp_getreport_excel_config(getSelectedReportProfile(),
				test_type);
		ArrayList<ArrayList<String>> saved_values = new ArrayList<ArrayList<String>>();
		ArrayList<String> page_values = new ArrayList<String>();
		ArrayList<String> sec_values = new ArrayList<String>();
		try {
			JSONArray report_excel_arr = report_excel_config.getJSONArray("Report_Excel_Cells");
			ApplicationLauncher.logger.info("LoadSavedData : report_excel_arr: " + report_excel_arr);
			if (report_excel_arr.length() != 0) {
				for (int i = 0; i < report_excel_arr.length(); i++) {
					JSONObject jobj = report_excel_arr.getJSONObject(i);
					String cell_type = jobj.getString("cell_type");
					String cell_value = jobj.getString("cell_value");
					if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
						page_values.add(cell_value);
					} else {
						sec_values.add(cell_value);
					}
				}
			}
			saved_values.add(page_values);
			saved_values.add(sec_values);
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadSavedData : JSONException: " + e.getMessage());
		}
		ApplicationLauncher.logger.info("LoadSavedData : saved_values: " + saved_values);
		return saved_values;
	}

	public void ReportExcelSaveOnClick() {
		int TestTypeDisplayIndex = cmbBox_testtype.getSelectionModel().getSelectedIndex();
		String test_type = ConstantReport.REPORT_TEST_TYPES.get(TestTypeDisplayIndex);
		MySQL_Controller.sp_delete_report_excel_config(getSelectedReportProfile(), test_type);

		ArrayList<String> pages = getValuesFromTable(page_cell_values);
		ArrayList<String> sections = getValuesFromTable(sec_cell_values);
		boolean AllDataFilled = true;
		for (int i = 0; i < pages.size(); i++) {
			ApplicationLauncher.logger.debug("ReportExcelSaveOnClick : pages: " + i + ":" + pages.get(i));
			if (pages.get(i).isEmpty()) {
				AllDataFilled = false;
				ApplicationLauncher.logger.debug("ReportExcelSaveOnClick : pages: data found empty");
				break;
			}
		}
		if (AllDataFilled) {
			for (int i = 0; i < sections.size(); i++) {
				ApplicationLauncher.logger.debug("ReportExcelSaveOnClick : sections:" + i + ":" + sections.get(i));
				if (sections.get(i).isEmpty()) {
					AllDataFilled = false;
					ApplicationLauncher.logger.debug("ReportExcelSaveOnClick : sections: data found empty");
					break;
				}
			}
		}
		if (AllDataFilled) {
			SaveToDB(test_type, ConstantReport.EXCEL_ALPHA, pages);
			SaveToDB(test_type, ConstantReport.EXCEL_BETA, sections);
			ApplicationLauncher.logger.info("ReportExcelSaveOnClick :Saved data successfully");
			WindowManager.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
		} else {
			ApplicationLauncher.logger.info("ReportExcelSaveOnClick :data found empty: user prompt");
			WindowManager.InformUser("Not Saved", "Data should not be empty. Kindly fill the same", AlertType.ERROR);
		}
	}

	public ArrayList<String> getValuesFromTable(ObservableList<ExcelCellValueModel> cell_values) {
		Iterator<ExcelCellValueModel> itr = cell_values.iterator();

		ArrayList<String> cell_values_arr = new ArrayList<String>();
		try {
			while (itr.hasNext()) {
				ExcelCellValueModel dataElement = itr.next();
				String cell_value = dataElement.getcell_value();
				cell_values_arr.add(cell_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("getValuesFromTable: Exception : " + e.getMessage());
		}
		return cell_values_arr;

	}

	public void SaveToDB(String test_type, String excel_type, ArrayList<String> values) {
		ApplicationLauncher.logger.info("SaveToDB: values : " + values);
		for (int i = 0; i < values.size(); i++) {
			MySQL_Controller.sp_add_report_excel_config(getSelectedReportProfile(), test_type, excel_type,
					values.get(i));
		}
		ReportConfigManager.LoadReportExcelConfigProperty();
	}
	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	 * }
	 */
}
