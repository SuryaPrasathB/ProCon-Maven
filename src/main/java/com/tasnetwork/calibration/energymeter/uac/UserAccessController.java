package com.tasnetwork.calibration.energymeter.uac;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
//import com.tasnetwork.calibration.energymeter.deployment.DeploymentDataModel;
//import com.tasnetwork.calibration.energymeter.deployment.DeploymentDevicesCheckBoxValueFactory;
//import com.tasnetwork.calibration.energymeter.deployment.MeterParamsController;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class UserAccessController implements Initializable {

	Timer saveTimer;
	Timer screenNameOnChangeTimer;

	@FXML
	private TableView<UacDataModel> tableViewUac;

	@FXML
	private TableColumn<UacDataModel, String> columnSerialNo;
	@FXML
	private TableColumn<UacDataModel, String> columnRoleName;
	@FXML
	private TableColumn columnVisibleEnabled;
	@FXML
	private TableColumn columnExecutePossible;
	@FXML
	private TableColumn columnAddPossible;
	@FXML
	private TableColumn columnUpdatePossible;
	@FXML
	private TableColumn columnDeletePossible;

	public static TableView<UacDataModel> ref_tableViewUac;

	@FXML
	private ComboBox<String> cmbBxScreenName;
	public static ComboBox<String> ref_cmbBxScreenName;

	@FXML
	private Button btnSave;
	public static Button ref_btnSave;

	// private static ArrayList<UacDataModel> tableRowDataList = new
	// ArrayList<UacDataModel>();
	private ObservableList<UacDataModel> tableRowDataList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		loadScreenList();
		setupTableView();
	}

	private void refAssignment() {

		ref_tableViewUac = tableViewUac;
		ref_cmbBxScreenName = cmbBxScreenName;
		ref_btnSave = btnSave;
	}

	private void loadScreenList() {

		ApplicationLauncher.logger.debug("loadScreenList : Entry ");
		ref_cmbBxScreenName.getItems().addAll(ConstantApp.SCREEN_NAME_LIST);
		ref_cmbBxScreenName.getSelectionModel().selectFirst();
	}

	public void setupTableView() {
		// ref_tableViewUac.setEditable(true);

		columnSerialNo.setCellValueFactory(cellData -> cellData.getValue().getSerialNoProperty());
		columnRoleName.setCellValueFactory(cellData -> cellData.getValue().getRoleNameProperty());
		columnVisibleEnabled.setCellValueFactory(new UacVisibleEnabledCheckBoxValueFactory());
		columnExecutePossible.setCellValueFactory(new UacExecutePossibleCheckBoxValueFactory());
		columnAddPossible.setCellValueFactory(new UacAddPossibleCheckBoxValueFactory());
		columnUpdatePossible.setCellValueFactory(new UacUpdatePossibleCheckBoxValueFactory());
		columnDeletePossible.setCellValueFactory(new UacDeletePossibleCheckBoxValueFactory());

		refreshData();

	}

	private void refreshData() {

		ApplicationLauncher.logger.debug("refreshData: Entry");
		String screenName = ref_cmbBxScreenName.getSelectionModel().getSelectedItem();
		String screenSection = "";
		String screenSubSection = "";
		ArrayList<UacDataModel> uacDataList = FetchUaclistFromDB(screenName, screenSection, screenSubSection);

		if (uacDataList.size() > 0) {
			refreshDataOnGui(uacDataList);
		} else {
			loadDefaultData();
		}
	}

	private void refreshDataOnGui(ArrayList<UacDataModel> uacDataList) {

		ApplicationLauncher.logger.debug("refreshDataOnGui: Entry");
		ref_tableViewUac.getItems().clear();
		tableRowDataList.clear();
		String serialNo = "";
		String userProfileName = "";
		boolean bVisibleEnabled = false;
		boolean bExecutePossible = false;
		boolean bAddPossible = false;
		boolean bUpdatePossible = false;
		boolean bDeletePossible = false;
		String screenName = "";// ref_cmbBxScreenName.getSelectionModel().getSelectedItem();
		String sectionName = "";
		String subSectionName = "";
		for (int i = 0; i < uacDataList.size(); i++) {
			serialNo = Integer.toString(i + 1);// +1);
			screenName = uacDataList.get(i).getScreenName();
			sectionName = uacDataList.get(i).getSectionName();
			subSectionName = uacDataList.get(i).getSubSectionName();
			userProfileName = uacDataList.get(i).getRoleName();
			bVisibleEnabled = uacDataList.get(i).getVisibleEnabled();
			bExecutePossible = uacDataList.get(i).getExecutePossible();
			bAddPossible = uacDataList.get(i).getAddPossible();
			bUpdatePossible = uacDataList.get(i).getUpdatePossible();
			bDeletePossible = uacDataList.get(i).getDeletePossible();
			tableRowDataList.add(new UacDataModel(serialNo, screenName, sectionName, subSectionName, userProfileName,
					bVisibleEnabled, bExecutePossible, bAddPossible, bUpdatePossible, bDeletePossible));
		}

		ref_tableViewUac.setItems(tableRowDataList);
	}

	private void loadDefaultData() {

		ApplicationLauncher.logger.debug("loadDefaultData: Entry");
		ref_tableViewUac.getItems().clear();
		tableRowDataList.clear();
		String serialNo = "";
		String userProfileName = "";
		boolean bVisibleEnabled = false;
		boolean bExecutePossible = false;
		boolean bAddPossible = false;
		boolean bUpdatePossible = false;
		boolean bDeletePossible = false;
		String screenName = ref_cmbBxScreenName.getSelectionModel().getSelectedItem();
		String sectionName = "";
		String subSectionName = "";
		for (int i = 0; i < ConstantAppConfig.UAC_PROFILE_LIST.size(); i++) {
			serialNo = Integer.toString(i + 1);// +1);
			userProfileName = ConstantAppConfig.UAC_PROFILE_LIST.get(i);
			tableRowDataList.add(new UacDataModel(serialNo, screenName, sectionName, subSectionName, userProfileName,
					bVisibleEnabled, bExecutePossible, bAddPossible, bUpdatePossible, bDeletePossible));
		}

		ref_tableViewUac.setItems(tableRowDataList);
	}

	@FXML
	public void btnSaveOnClick() {
		ApplicationLauncher.logger.debug("btnSaveOnClick: Entry");
		saveTimer = new Timer();
		saveTimer.schedule(new btnSaveOnClickTrigger(), 100);
	}

	class btnSaveOnClickTrigger extends TimerTask {
		public void run() {

			ApplicationLauncher.logger.debug("btnSaveOnClickTrigger : Entry");
			WindowManager.setCursor(Cursor.WAIT);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime DB_StartTime = LocalDateTime.now();
			LocalDateTime DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("btnSaveOnClickTrigger: Start Time: " + dtf.format(DB_StartTime));

			saveTask();

			WindowManager.setCursor(Cursor.DEFAULT);
			DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("btnSaveOnClickTrigger: End Time6: " + dtf.format(DB_EndTime));

			ApplicationLauncher.logger.debug("btnSaveOnClickTrigger: Difference Time6: "
					+ TestReportController.DiffTime(dtf.format(DB_StartTime), dtf.format(DB_EndTime)));

			saveTimer.cancel();
		}

	}

	private void saveTask() {

		ApplicationLauncher.logger.info("saveTask: Entry");
		String project_name = "";// MeterParamsController.getCurrentProjectName();
		Iterator<UacDataModel> itr = tableRowDataList.iterator();

		String userProfileName = "";
		String selectedScreenName = ref_cmbBxScreenName.getSelectionModel().getSelectedItem();
		String selectedScreenSection = "";
		String selectedScreenSubSection = "";
		/*
		 * boolean visibleEnabled = false;
		 * boolean executePossible = false;
		 * boolean meter_const = false;
		 * boolean rack_id = false;
		 * boolean rack_id = false;
		 */
		boolean statusSuccess = true;
		boolean status = false;
		ArrayList<Integer> meter_const_arr = new ArrayList<Integer>();
		try {
			while (itr.hasNext()) {
				UacDataModel dataElement = itr.next();
				dataElement.setScreenName(selectedScreenName);
				dataElement.setSectionName(selectedScreenSection);
				dataElement.setSubSectionName(selectedScreenSubSection);
				status = MySQL_Controller.sp_add_uac_profile(dataElement);
				if (!status) {
					if (statusSuccess) {
						statusSuccess = false;
					}
				}

			}
			if (statusSuccess) {
				ApplicationLauncher.logger.info("saveTask: Success:  Saved to database - prompted");
				WindowManager.InformUser("Success", "Saved to database", AlertType.INFORMATION);
			} else {
				ApplicationLauncher.logger.info(
						"saveTask: Failed:  Saving to database failed. kindly check the logs or contact admin - prompted");
				WindowManager.InformUser("Failed", "Saving to database failed. kindly check the logs or contact admin",
						AlertType.ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("saveTask: Exception: " + e.getMessage());
		}

	}

	public static ArrayList<UacDataModel> FetchUaclistFromDB(String screenName, String screenSection,
			String subSection) {

		ApplicationLauncher.logger.info("FetchUaclistFromDB: Entry");
		ArrayList<UacDataModel> uacList = new ArrayList<UacDataModel>();
		try {

			JSONObject datalist = MySQL_Controller.sp_get_Uac_data_by_screen(screenName, screenSection, subSection);
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

			String profileName = "";
			// String screenName = "";
			String sectionName = "";
			String subSectionName = "";

			for (int i = 0; i < uacDataList.length(); i++) {
				jobj = uacDataList.getJSONObject(i);
				profileName = jobj.getString("profile_name");
				sectionName = jobj.getString("screen_section");
				subSectionName = jobj.getString("screen_sub_section");
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
			ApplicationLauncher.logger.error("FetchUaclistFromDB: JSONException: " + e.getMessage());
		}
		return uacList;
	}

	@FXML
	public void cmbBxScreenNameOnChange() {
		ApplicationLauncher.logger.debug("cmbBxScreenNameOnChange: Entry");
		screenNameOnChangeTimer = new Timer();
		screenNameOnChangeTimer.schedule(new cmbBxScreenNameOnChangeTrigger(), 100);
	}

	class cmbBxScreenNameOnChangeTrigger extends TimerTask {
		public void run() {

			ApplicationLauncher.logger.debug("cmbBxScreenNameOnChangeTrigger : Entry");
			WindowManager.setCursor(Cursor.WAIT);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime DB_StartTime = LocalDateTime.now();
			LocalDateTime DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("cmbBxScreenNameOnChangeTrigger: Start Time: " + dtf.format(DB_StartTime));

			refreshData();

			WindowManager.setCursor(Cursor.DEFAULT);
			DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("cmbBxScreenNameOnChangeTrigger: End Time6: " + dtf.format(DB_EndTime));

			ApplicationLauncher.logger.debug("cmbBxScreenNameOnChangeTrigger: Difference Time6: "
					+ TestReportController.DiffTime(dtf.format(DB_StartTime), dtf.format(DB_EndTime)));

			screenNameOnChangeTimer.cancel();
		}

	}

}
