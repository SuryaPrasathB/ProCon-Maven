package com.tasnetwork.calibration.energymeter.testreport;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.Transient;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigLoader;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigModel;
import com.tasnetwork.calibration.energymeter.custom1report.ExcelReportMeterDataCellPositionPage;
import com.tasnetwork.calibration.energymeter.custom1report.ExcelReportMeterDataDisplayPage;
import com.tasnetwork.calibration.energymeter.custom1report.TestTypeFilter;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
//import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
//import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.reportprofile.ReportGeneration;
import com.tasnetwork.calibration.energymeter.reportprofile.ReportProfileConfigController;
import com.tasnetwork.calibration.energymeter.setting.BusyLoadingController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseSelectionCheckBoxFactory;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;
import com.tasnetwork.spring.orm.model.RpPrintPosition;
import com.tasnetwork.spring.orm.service.ReportProfileManageService;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestReportController implements Initializable {

	
	private static HashMap<String,String> deviceIdSerialNumberHashMap = new HashMap<String,String>();
	
	Timer getProjectListTimer;

	
	public static String reportGeneratedPath = "";
	public static String reportGeneratedFileName  = "";
	public static String selectedDeploymentID="";
	
	
	public static String selectedExecutionTesterName="";
	public static String selectedReportSerialNo = "";
	public static String selectedProjectExecutedDate="";
	public static String selectedProjectExecutedTime="";
	public static String selectedProjectName;
	public static String selectedCustomerRefNo = "";
	public static String selectedEnergyFlowMode = "";
	public static boolean selectedExecutionMainCt = false;
	public static boolean selectedExecutionNeutralCt = false;

	private MultiValuedMap<String,String> groupProfileNameHashMap     = new ArrayListValuedHashMap<String,String>();
	private List<ReportProfileManage> activeReportProfileDatabaseList = new ArrayList<ReportProfileManage>();

	static private ReportProfileManageService reportProfileManageService = DeviceDataManagerController.getReportProfileManageService();

	public static Stage meterSelectionStage;

	private String dutMeterDataPreviousPageLastUpdatedRackId = "0";
	private int dutMeterDataPreviousPageUpdatedPageNumber = 0;

	private HashMap<String,String> meterDeviceOverAllStatus = new HashMap<String, String>();

	public static int serialNumberMaxCount = 1;
	static DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();

	private JSONObject meterProfileData = new JSONObject();

	ObservableList<String> ProjectEndTimeList = FXCollections.observableArrayList();
	public List<String> deploymentIdList = new ArrayList<String>();
	public List<String> projectNameList = new ArrayList<String>();
	public List<String> mctCompletedList = new ArrayList<String>();
	public List<String> nctCompletedList = new ArrayList<String>();
	public List<String> testerNameList = new ArrayList<String>();
	public List<String> customerRefNoList = new ArrayList<String>();
	public List<String> energyFlowMode = new ArrayList<String>();

	private static String resultFilterMctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;	

	private String resultFilterImportExportMode = ConstantApp.DEPLOYMENT_IMPORT_MODE;

	private String selectedMeterSerialNo = ConstantReport.REPORT_SELECTED_ALL_METERS;
	private boolean individualMeterReportSelected = false;
	public static boolean mainCtMode = false;
	public static boolean neutralCtMode = true;
	@FXML
	private TableView<List<Object>> result_table_view;
	public static TableView<List<Object>> ref_result_table_view;
	String  DateDisplayPattern = "dd-MMM-yyyy";

	@FXML
	private ComboBox<String> cmbBxReportProfile;

	static  ComboBox<String> ref_cmbBxReportProfile;

	@FXML
	private ComboBox<String> cmbBxReportProfileGroup;

	static  ComboBox<String> ref_cmbBxReportProfileGroup;


	@FXML
	private Label lblReportGroup;

	static  Label ref_lblReportGroup;



	private String selectedReportProfile = "";

	@FXML
	private RadioButton radioBtnMainCt;
	@FXML
	private RadioButton radioBtnNeutralCt;

	static RadioButton ref_radioBtnMainCt;
	static RadioButton ref_radioBtnNeutralCt;

	@FXML Label labelMode;
	public static Label ref_labelMode;

	@FXML
	private DatePicker datepicker_fromdate;

	@FXML
	private DatePicker datepicker_todate;

	@FXML
	private TitledPane 	titlePaneSearchResult;

	@FXML
	private TitledPane 	titledPaneReportTypes;

	@FXML
	private AnchorPane 	anchorPaneSearchResult;
	//@FXML
	//private TitledPane 	titlePaneDisplayResult;
	//@FXML
	//private AnchorPane 	anchorPaneDisplayResult;

	@FXML
	private Spinner<LocalTime> spinner_fromtime;

	@FXML
	private Spinner<LocalTime> spinner_totime;

	@FXML ComboBox<String> cmbBoxProjectList;
	static ComboBox<String> ref_cmbBoxProjectList;
	
	@FXML ComboBox<String> cmbBoxRangeSelect;
	static ComboBox<String> ref_cmbBoxRangeSelect;

	@FXML
	private CheckBox chk_bx_accuracy;
	@FXML
	private CheckBox chk_bx_no_load;
	@FXML
	private CheckBox chk_bx_sta;
	@FXML
	private CheckBox chk_bx_voltage_variation;
	@FXML
	private CheckBox chk_bx_frequency_variation;
	@FXML
	private CheckBox chk_bx_voltage_unbalance;
	@FXML
	private CheckBox chk_bx_repeatability;
	@FXML
	private CheckBox chk_bx_self_heating;
	@FXML
	private CheckBox chk_bx_phase_reversal;
	@FXML
	private CheckBox chk_bx_constant_test;
	@FXML
	private CheckBox chk_bx_harmonics;
	@FXML
	private CheckBox chk_bx_SelectAll;

	@FXML
	private HBox hBoxTable;

	@FXML
	private Button btn_get_projectlist;

	@FXML
	private Button btn_get_results;
	private static Button ref_btn_get_results;

	@FXML
	private Button btn_GenerateReport;

	@FXML
	private Button btn_ExportAllResults;

	@FXML
	private GridPane gridPaneGenReport;

	//@FXML
	//private ColumnConstraints gridPaneGenReportCol1;



	private int max_devices = ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK;//12;
	public JSONArray ProjectRunData = new JSONArray();

	private JSONArray MeterNames = new JSONArray();
	private boolean CursorWaitFlag = false;

	private JSONObject LastDisplayedResultData = new JSONObject();
	Timer GetResultDataTimer;
	Timer GenerateReportTimer;
	Timer AllResultToExcelTimer;
	private String All_PhaseDisplayName = 	"ABC";

	public void initialize(URL url, ResourceBundle rb){
		ApplicationLauncher.logger.info("Reports controller");

		ReportAdjustScreen();
		/*initializeSpinner(LocalTime.now(), spinner_fromtime);
		initializeSpinner(LocalTime.now(), spinner_totime);*/
		
		initializeSpinner(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), LocalTime.now().getNano()), spinner_fromtime);
		initializeSpinner(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond(), LocalTime.now().getNano()), spinner_totime);

		
		ApplicationLauncher.logger.info("Reports controller : Local Time : Now :" + LocalTime.now() );

		SetVisibilityForReportCheckBox();
		UpdateReportCheckBoxDisplayName();
		result_table_view.setVisible(true);
		cmbBoxProjectList.setVisible(true);
		btn_get_results.setVisible(true);
		btn_GenerateReport.setVisible(true);
		datepicker_fromdate.setEditable(false);
		
		LocalDate today=LocalDate.now();
		LocalDate yesterday=LocalDate.now().minusDays(1L);

		init_DateFormatDisplay(datepicker_todate);
		init_DateFormatDisplay(datepicker_fromdate);
		
		datepicker_todate.setValue(today);
		datepicker_fromdate.setValue(yesterday);

		ref_result_table_view = result_table_view;
		ref_cmbBoxProjectList = cmbBoxProjectList;
		ref_cmbBoxRangeSelect = cmbBoxRangeSelect;
		ref_radioBtnMainCt = radioBtnMainCt;;
		ref_radioBtnNeutralCt = radioBtnNeutralCt;
		ref_cmbBxReportProfile= cmbBxReportProfile;
		ref_cmbBxReportProfileGroup = cmbBxReportProfileGroup;
		ref_lblReportGroup = lblReportGroup;
		ref_btn_get_results = btn_get_results;
		ref_labelMode = labelMode;
		if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
			loadReportProfileListV2();
		}else {
			ref_cmbBxReportProfileGroup.setVisible(false);
			ref_lblReportGroup.setVisible(false);
			loadReportProfileList();
		}
		Platform.setImplicitExit(false);


		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			All_PhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME +ConstantApp.SECOND_PHASE_DISPLAY_NAME +ConstantApp.THIRD_PHASE_DISPLAY_NAME ;
		}

		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}

		hideGuiObjects();
		
		populateRangeSelectComboBox();
		
		cmbBoxRangeSelect.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
		    if (newVal != null) {
		        RangeSelectOnChange(newVal);
		    }
		});
		
		datepicker_fromdate.valueProperty().addListener((obs, oldDate, newDate) -> {
			adjustDateForSelectedRange();
		});

		datepicker_todate.valueProperty().addListener((obs, oldDate, newDate) -> {
			adjustDateForSelectedRange();
		});
		
		/*// Populate and set event listener for the range selection combo box
	    populateRangeSelectComboBox();
	    
	    // Add listeners to datepickers to ensure automatic updates
	    addDatePickersListeners();*/

	}
	
	/**
	 * Populates the cmbBoxRangeSelect with predefined time ranges.
	 */
	private void populateRangeSelectComboBox() {
	    cmbBoxRangeSelect.getItems().addAll("End of Day", "Weekly", "Monthly", "Quarterly", "Yearly");

	    // Add a listener to handle selection changes
	    cmbBoxRangeSelect.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
	        if (newVal != null) {
	            handleRangeSelectChange(newVal);
	        }
	    });

	    // Default selection
	    cmbBoxRangeSelect.getSelectionModel().selectFirst();
	}
	
	/**
	 * Updates the date pickers and time spinners based on the selected range.
	 */
	private void handleRangeSelectChange(String selectedRange) {
	    LocalDate today = LocalDate.now();
	    LocalDate fromDate = today;
	    LocalTime fromTime = LocalTime.of(0, 0);
	    LocalTime toTime = LocalTime.of(23, 59);

	    switch (selectedRange) {
	        case "End of Day":
	            fromDate = today;
	            break;
	        case "Weekly":
	            fromDate = today.minusDays(7);
	            break;
	        case "Monthly":
	            fromDate = today.minusMonths(1);
	            break;
	        case "Quarterly":
	            fromDate = today.minusMonths(3);
	            break;
	        case "Yearly":
	            fromDate = today.minusYears(1);
	            break;
	    }

	    datepicker_fromdate.setValue(fromDate);
	    datepicker_todate.setValue(today);
	    spinner_fromtime.getValueFactory().setValue(fromTime);
	    spinner_totime.getValueFactory().setValue(toTime);
	}
	
	/**
	 * Adds listeners to both datepickers to automatically adjust based on manual changes.
	 */
	private void addDatePickersListeners() {
	    datepicker_fromdate.valueProperty().addListener((obs, oldVal, newVal) -> {
	        if (newVal != null) {
	            adjustDatesOnManualChange("fromdate", newVal);
	        }
	    });

	    datepicker_todate.valueProperty().addListener((obs, oldVal, newVal) -> {
	        if (newVal != null) {
	            adjustDatesOnManualChange("todate", newVal);
	        }
	    });
	}

	/**
	 * Adjusts datepickers dynamically if one is changed manually by the user.
	 */
	private void adjustDatesOnManualChange(String changedField, LocalDate newDate) {
	    String selectedRange = cmbBoxRangeSelect.getSelectionModel().getSelectedItem();
	    if (selectedRange == null) return;

	    LocalDate fromDate = datepicker_fromdate.getValue();
	    LocalDate toDate = datepicker_todate.getValue();

	    switch (selectedRange) {
	        case "End of Day":
	            if (changedField.equals("fromdate")) {
	                datepicker_todate.setValue(newDate);
	            } else {
	                datepicker_fromdate.setValue(newDate);
	            }
	            spinner_fromtime.getValueFactory().setValue(LocalTime.of(0, 0));
	            spinner_totime.getValueFactory().setValue(LocalTime.of(23, 59));
	            break;

	        case "Weekly":
	            if (changedField.equals("fromdate")) {
	                datepicker_todate.setValue(newDate.plusDays(7));
	            } else {
	                datepicker_fromdate.setValue(newDate.minusDays(7));
	            }
	            break;

	        case "Monthly":
	            if (changedField.equals("fromdate")) {
	                datepicker_todate.setValue(newDate.plusMonths(1));
	            } else {
	                datepicker_fromdate.setValue(newDate.minusMonths(1));
	            }
	            break;

	        case "Quarterly":
	            if (changedField.equals("fromdate")) {
	                datepicker_todate.setValue(newDate.plusMonths(3));
	            } else {
	                datepicker_fromdate.setValue(newDate.minusMonths(3));
	            }
	            break;

	        case "Yearly":
	            if (changedField.equals("fromdate")) {
	                datepicker_todate.setValue(newDate.plusYears(1));
	            } else {
	                datepicker_fromdate.setValue(newDate.minusYears(1));
	            }
	            break;
	    }
	}
	
	private void adjustDateForSelectedRange() {
		String selectedRange = cmbBoxRangeSelect.getSelectionModel().getSelectedItem();
		if (selectedRange == null) return;

		LocalDate today = LocalDate.now();
		LocalDate toDate = datepicker_todate.getValue();

		switch (selectedRange) {
			case "End of Day":
				datepicker_fromdate.setValue(toDate);
				initializeSpinner(LocalTime.of(0, 0, 0, LocalTime.now().getNano()), spinner_fromtime);
				initializeSpinner(LocalTime.of(23, 59, 59, LocalTime.now().getNano()), spinner_totime);
				break;

			case "Weekly":
				datepicker_fromdate.setValue(toDate.minusDays(7));
				break;

			case "Monthly":
				datepicker_fromdate.setValue(toDate.minusMonths(1));
				break;

			case "Quarterly":
				datepicker_fromdate.setValue(toDate.minusMonths(3));
				break;

			case "Yearly":
				datepicker_fromdate.setValue(toDate.minusYears(1));
				break;
		}
	}



	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("TestReportController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
			case ConstantApp.UAC_REPORT_SCREEN:


				if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
					ref_btn_get_results.setDisable(true);
				}

				if(!uacSelectProfileScreenList.get(i).getAddPossible()){
					//ref_btn_Create.setDisable(true);
				}

				if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
					//ref_vbox_testscript.setDisable(true);sdvsc
					//setChildPropertySaveEnabled(false);
					//ref_btn_Save.setDisable(true);
				}

				if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
					//ref_btn_Delete.setDisable(true);
				}
				break;

			default:
				break;
			}
		}
	}

	public void hideGuiObjects(){
		Platform.runLater(() -> {
			//btn_get_results.setVisible(true);
			//chk_bx_voltage_unbalance.setVisible(false);
			//chk_bx_phase_reversal.setVisible(false);
			chk_bx_harmonics.setVisible(false);
			//radioBtnMainCt.setVisible(false);
			//radioBtnNeutralCt.setVisible(false);
			//chk_bx_frequency_variation.setVisible(false);
			//chk_bx_self_heating.setVisible(false);
			if(!ProcalFeatureEnable.RACK_MCT_NCT_ENABLED){
				ref_radioBtnMainCt.setVisible(false);
				ref_radioBtnNeutralCt.setVisible(false);
				ref_labelMode.setVisible(false);
			}else{
				//ref_radioBtnMainCt.setSelected(true);
				//ref_radioBtnNeutralCt.setSelected(false);
			}

		});
	}

	public void loadReportProfileList(){

		Set<String> hSet = new HashSet<String>(ConstantAppConfig.REPORT_PROFILE_LIST); 
		hSet.addAll(ConstantAppConfig.REPORT_PROFILE_LIST); 

		ref_cmbBxReportProfile.getItems().clear();
		ref_cmbBxReportProfile.getItems().addAll(hSet);
		//ref_cmbBxReportProfile.getSelectionModel().select(0);
		ref_cmbBxReportProfile.getSelectionModel().select(ConstantAppConfig.DefaultReportProfileDisplay);
		setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
		if(!getSelectedReportProfile().equals(ConstantApp.StandardReportProfileDisplay)){
			titledPaneReportTypes.setDisable(true);
		}
		//LoadSavedData();
	}

	public void loadReportProfileListV2(){

		/*		Set<String> hSet = new HashSet<String>(ConstantAppConfig.REPORT_PROFILE_LIST); 
        hSet.addAll(ConstantAppConfig.REPORT_PROFILE_LIST); 

		ref_cmbBxReportProfile.getItems().clear();
		ref_cmbBxReportProfile.getItems().addAll(hSet);
		//ref_cmbBxReportProfile.getSelectionModel().select(0);
		ref_cmbBxReportProfile.getSelectionModel().select(ConstantAppConfig.DefaultReportProfileDisplay);
		setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
		if(!getSelectedReportProfile().equals(ConstantApp.StandardReportProfileDisplay)){
			titledPaneReportTypes.setDisable(true);
		}*/
		//LoadSavedData();

		readActiveReportProfileManageFromDataBase();
		MultiValuedMap<String,String> groupNameHashMap = new ArrayListValuedHashMap<String,String>();

		getActiveReportProfileDatabaseList().stream().forEach( e-> 
		{
			ApplicationLauncher.logger.info("loadReportProfileList: groupNameList : " + e.getReportGroupName());
			//if(groupNameHashMap.containsKey(e.getReportGroupName())){

			//}else{
			groupNameHashMap.put(e.getReportGroupName(), e.getReportProfileName());
			//}
			//groupNameSetList.add(e.getReportGroupName());
		});


		//ref_cmbBxReportProfileGroup.getItems().addAll(groupNameSetList);
		//String lastProfileGroupName = "";
		groupNameHashMap.keySet().stream().forEach( e -> {

			ref_cmbBxReportProfileGroup.getItems().add(e);
			//lastProfileGroupName = e;
			//groupNameHashMap

		});

		ref_cmbBxReportProfileGroup.getSelectionModel().selectLast();
		String lastProfileGroupName = (String)ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem();//.toString();

		ApplicationLauncher.logger.info("loadReportProfileList: lastProfileGroupName : " + lastProfileGroupName);
		ApplicationLauncher.logger.info("loadReportProfileList: groupNameHashMap : " +groupNameHashMap);

		//String profileNameList = groupNameHashMap.entries().stream().filter(e -> e.getKey().equals(lastProfileGroupName)).findFirst().get().getValue();


		Collection<String> reportFileNameList = groupNameHashMap.get(lastProfileGroupName);
		reportFileNameList.stream().forEach( value -> {

			ref_cmbBxReportProfile.getItems().add(value);
		} );

		ref_cmbBxReportProfile.getSelectionModel().selectLast();
		setGroupProfileNameHashMap(groupNameHashMap);
		if(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem()!=null){
			setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
		}
		if(!getSelectedReportProfile().equals(ConstantApp.StandardReportProfileDisplay)){
			titledPaneReportTypes.setDisable(true);
		}
	}

	private void readActiveReportProfileManageFromDataBase() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase : Entry");

		//List<ReportProfileMeterMetaDataFilter> meterMetaDataDatabaseList = dataManagerObj.getReportProfileMeterMetaDataFilterService().findAll();
		List<ReportProfileManage> reportProfileActiveDatabaseList = getReportProfileManageService().findAll();//findAllOrderByTableSerialNoAsc();

		List<String> reportProfileGroupNameFilterList = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_GROUP_NAME_LIST;
		String reportProfileDefaultActiveCustomerId   =  ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID; 
		if(reportProfileGroupNameFilterList.contains(ConstantReportV2.REPORT_GROUP_IMPORT_ALL)){
			ApplicationLauncher.logger.debug("TestReportController: readActiveReportProfileManageFromDataBase : Importing All");		
			
			reportProfileActiveDatabaseList = getReportProfileManageService().findActiveProfileAndCustomerId(reportProfileDefaultActiveCustomerId);//findAllOrderByTableSerialNoAsc();
		}else{
			ApplicationLauncher.logger.debug("TestReportController: readActiveReportProfileManageFromDataBase : Importing filtered group");
			reportProfileActiveDatabaseList = getReportProfileManageService().findActiveCustomerByReportGroupNameList(reportProfileDefaultActiveCustomerId,reportProfileGroupNameFilterList);//findAllOrderByTableSerialNoAsc();
		}
		reportProfileActiveDatabaseList.stream().forEach(e -> {
			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getId: " + e.getId());
			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : isProfileActive: " + e.isProfileActive() );
			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportGroupId: " + e.getReportGroupId() );
			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportGroupName: " + e.getReportGroupName() );
			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase Active : Reading from DB : getReportProfileName: " + e.getReportProfileName());


			ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase : findActiveCustomerByReportGroupNameList: Reading from DB : 000000000000000000000000 " );
		});

		setActiveReportProfileDatabaseList(reportProfileActiveDatabaseList);
		ApplicationLauncher.logger.debug("readActiveReportProfileManageFromDataBase : Exit");
	}

	public String getSelectedReportProfile() {
		return selectedReportProfile;
	}

	public void setSelectedReportProfile(String selectedReportProfile) {
		this.selectedReportProfile = selectedReportProfile;
	}

	public static String getSelectedProjectName(){
		return selectedProjectName;
	}

	public void setSelectedProjectName(String project_name){
		selectedProjectName = project_name;
	}

	public void init_DateFormatDisplay( DatePicker mDatePicker){


		mDatePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DateDisplayPattern);

			@Override 
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override 
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}



	public void SetVisibilityForReportCheckBox(){
		String TestCaseType = "";
		for (int i = 0; i < (TestProfileType.values().length); i++) {
			boolean IsFeatureEnabledInConfig = false;
			try{
				IsFeatureEnabledInConfig = ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.getBoolean(TestProfileType.values()[i].toString());
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ProjectController: initialize: Exception :"+e.getMessage());

			}
			if(!IsFeatureEnabledInConfig){
				TestCaseType = TestProfileType.values()[i].toString();
				switch (TestCaseType) {



				case ConstantApp.TEST_PROFILE_STA:
					chk_bx_sta.setVisible(false);
					break;


				case	ConstantApp.TEST_PROFILE_WARMUP:

					break;


				case ConstantApp.TEST_PROFILE_NOLOAD:
					chk_bx_no_load.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_ACCURACY:
					chk_bx_accuracy.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
					chk_bx_voltage_variation.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
					chk_bx_frequency_variation.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
					chk_bx_harmonics.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_CUT_NUETRAL:
					//REPORT_TEST_TYPES_DISPLAY1.add();
					break;


				case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
					chk_bx_voltage_unbalance.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
					chk_bx_phase_reversal.setVisible(false);
					break;



				case ConstantApp.TEST_PROFILE_REPEATABILITY:
					chk_bx_repeatability.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_SELF_HEATING:
					chk_bx_self_heating.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
					chk_bx_constant_test.setVisible(false);
					break;


				case ConstantApp.TEST_PROFILE_CUSTOM_TEST:
					//REPORT_TEST_TYPES_DISPLAY1.add();
					break;

				default:
					break;
				}
			}
		}


	}

	public void UpdateReportCheckBoxDisplayName(){

		chk_bx_self_heating.setText(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING);;
		chk_bx_repeatability.setText(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY);
		chk_bx_frequency_variation.setText(ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY);
		chk_bx_voltage_variation.setText(ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE);
		chk_bx_accuracy.setText(ConstantApp.DISPLAY_TC_TITLE_ACCURACY);
		chk_bx_phase_reversal.setText(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL);
		chk_bx_harmonics.setText(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS);
		chk_bx_voltage_unbalance.setText(ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE);
		chk_bx_constant_test.setText(ConstantApp.DISPLAY_TC_TITLE_CONST_TEST);
		chk_bx_no_load.setText(ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST);
		chk_bx_sta.setText(ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT);


	}
	public static boolean isMainCtMode() {
		return mainCtMode;
	}

	public String getResultFilterImportExportMode() {
		return resultFilterImportExportMode;
	}

	public void setResultFilterImportExportMode(String resultFilterImportExportMode) {
		this.resultFilterImportExportMode = resultFilterImportExportMode;
	}

	public static void setMainCtMode(boolean mainCt_Mode) {
		mainCtMode = mainCt_Mode;
	}

	public static boolean isNeutralCtMode() {
		return neutralCtMode;
	}

	public static void setNeutralCtMode(boolean neutralCt_Mode) {
		neutralCtMode = neutralCt_Mode;
	}

	public void ReportAdjustScreen(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth= primaryScreenBounds.getWidth();
		double SplitPaneOffset = 135; //149;
		double BottomStatusPaneOffset = 56;
		//double HeaderOffset = 30;

		//double ProgressStatusPaneHeight = 192;
		//double Monitor_RefData_Width = 250;
		double ReportHeightOffset = 110;

		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;
		ConstantApp MyPropertyObj= new ConstantApp();
		//ScreenWidthThreshold = (long)MyPropertyObj.readDatafromJsonConfig(MyProperty.SCREEN_WIDTH_THRESHOLD);
		//ScreenHeightThreshold = (long)MyPropertyObj.readDatafromJsonConfig(MyProperty.SCREEN_HEIGHT_THRESHOLD);

		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;

		ApplicationLauncher.logger.info("ReportAdjustScreen :Current Screen Height:"+ScreenHeight);
		ApplicationLauncher.logger.info("ReportAdjustScreen: Current Screen Width:"+ScreenWidth);
		ApplicationLauncher.logger.info("ReportAdjustScreen :ScreenHeightThreshold:"+ScreenHeightThreshold);
		ApplicationLauncher.logger.info("ReportAdjustScreen: ScreenWidthThreshold:"+ScreenWidthThreshold);
		double SearchResultPaneHeight = ScreenHeight-ReportHeightOffset;
		double PaneHeaderHeight = 60;
		double tableOffset = 20;
		double SecondaryHeaderHeightOffset = 160;//320;
		double gridPaneGenReportHeight = 250;
		if (ScreenHeight> ScreenHeightThreshold){

			titlePaneSearchResult.setMinHeight(SearchResultPaneHeight +30 );
			anchorPaneSearchResult.setMinHeight(SearchResultPaneHeight-PaneHeaderHeight );
			//btn_GenerateReport.setLayoutY(SearchResultPaneHeight -125);
			hBoxTable.setMinHeight(SearchResultPaneHeight - SecondaryHeaderHeightOffset-PaneHeaderHeight + 200);
			//titlePaneDisplayResult.setMinHeight(SearchResultPaneHeight -SecondaryHeaderHeightOffset+150);
			//anchorPaneDisplayResult.setMinHeight(SearchResultPaneHeight -SecondaryHeaderHeightOffset-PaneHeaderHeight+150 );
			result_table_view.setMinHeight(SearchResultPaneHeight - SecondaryHeaderHeightOffset-PaneHeaderHeight + 155);
			//titledPaneReportTypes.setMinHeight(SearchResultPaneHeight -SecondaryHeaderHeightOffset+150);
			if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
				gridPaneGenReport.setMinHeight(gridPaneGenReportHeight );
			}
		}

		double SearchResultPaneWidth = ScreenWidth-SplitPaneOffset;//-15;
		if(ScreenWidth >ScreenWidthThreshold){
			int gridPaneGenReportWidth = 380;//300;
			titlePaneSearchResult.setMinWidth(SearchResultPaneWidth );
			anchorPaneSearchResult.setMinWidth(SearchResultPaneWidth );
			hBoxTable.setMinWidth(SearchResultPaneWidth );
			//titlePaneDisplayResult.setMinWidth(SearchResultPaneWidth -gridPaneGenReportWidth );
			//anchorPaneDisplayResult.setMinWidth(SearchResultPaneWidth -gridPaneGenReportWidth);
			result_table_view.setMinWidth(SearchResultPaneWidth  -gridPaneGenReportWidth -tableOffset);
			//gridPaneGenReport.setMinWidth(gridPaneGenReportWidth );
			//gridPaneGenReportCol1.setMaxWidth(50);//50 );

		}

	}

	public void ProjectListOnChange(){
		ApplicationLauncher.logger.debug("ProjectListOnChange: Entry");
		ClearLastDisplayedResultData();
		clearResultView();
	}
	
	private void RangeSelectOnChange(String selectedRange) {
	    LocalDate today = LocalDate.now();
	    LocalDate fromDate = today;
	    LocalTime fromTime = LocalTime.of(0, 0, 0);
	    LocalTime toTime = LocalTime.of(23, 59, 59);

	    switch (selectedRange) {
	        case "End of Day":
	            fromDate = today;
	            fromTime = LocalTime.of(0, 0, 0);
	            toTime = LocalTime.of(23, 59, 59);
	            break;
	            
	        case "Weekly":
	            fromDate = today.minusDays(7);
	            break;

	        case "Monthly":
	            fromDate = today.minusMonths(1);
	            break;

	        case "Quarterly":
	            fromDate = today.minusMonths(3);
	            break;

	        case "Yearly":
	            fromDate = today.minusYears(1);
	            break;

	        case "Fortnight":
	            fromDate = today.minusDays(14);
	            break;

	        default:
	            ApplicationLauncher.logger.error("Unknown Range Selected: " + selectedRange);
	            break;
	    }

	    // Set values
	    datepicker_fromdate.setValue(fromDate);
	    datepicker_todate.setValue(today);
	    initializeSpinner(LocalTime.of(0, 0, 0, LocalTime.now().getNano()), spinner_fromtime);
		initializeSpinner(LocalTime.of(23, 59, 59, LocalTime.now().getNano()), spinner_totime);
	}


	//@FXML

	@FXML
	public void GetProjectOnClickTrigger() {
		ApplicationLauncher.logger.info("GetProjectOnClickTrigger: Entry");
		getProjectListTimer = new Timer();
		getProjectListTimer.schedule(new GetProjectOnClick(), 100);

	}
	
	@FXML
	void RangeSelectOnChange() {

	}

	class GetProjectOnClick extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				enableBusyLoadingScreen();
			});
			GetProjectOnClickTask();
			disableBusyLoadingScreen();
			getProjectListTimer.cancel();


		}
	}

	public void GetProjectOnClickTask() {

		//setUserComments("");
		//clearResultView();
		boolean  status = refreshReportList();
		if(!status){
			ApplicationLauncher.logger.info("refreshReportList: No Results- Results not found! - Prompted");
			ApplicationLauncher.InformUser("No Results", "Results not found!!", AlertType.ERROR);
		}

	}


	public boolean refreshReportList(){// throws  JSONException{
		//emModelDataList.clear();

		//PTCT_ModelDataList.clear();
		boolean status = true;
		//customernamelist.clear();
		//equipmentSerialNolist.clear();
		ProjectEndTimeList.clear();
		deploymentIdList.clear();
		mctCompletedList.clear();
		nctCompletedList.clear();
		projectNameList.clear();
		testerNameList.clear();
		customerRefNoList.clear();
		energyFlowMode.clear();

		//String customer_name = "";
		//String equipment_serial_no_ = "";

		//JSONObject Modeldata = MySQL_Controller.sp_getem_model_list();
		ApplicationLauncher.logger.info("refreshReportList: from time: " + spinner_fromtime.getValue());
		ApplicationLauncher.logger.info("refreshReportList: to time: " + spinner_totime.getValue());
		String fromdate = datepicker_fromdate.getValue().toString() +" " + spinner_fromtime.getValue();
		String todate = datepicker_todate.getValue().toString() + " " + spinner_totime.getValue();

		//String currentSelectedCustomerName = ref_cmbBox_CustomerName.getSelectionModel().getSelectedItem().toString();
		//String selectedEquipmentSerialNumber = ref_cmbBox_SerialNumber.getSelectionModel().getSelectedItem().toString();
		try{
			if((!fromdate.isEmpty()) && (!todate.isEmpty())){
				long from_epoch = calcEpoch(fromdate);
				long to_epoch = calcEpoch(todate);
				ApplicationLauncher.logger.info("refreshReportList: from_epoch: " + from_epoch);
				ApplicationLauncher.logger.info("refreshReportList: to_epoch: " + to_epoch);
				//JSONObject projectjson = MySQL_Controller.sp_get_project_run(from_epoch, to_epoch);
				JSONObject projectjson = MySQL_Controller.sp_get_executed_project_results(from_epoch, to_epoch);
				//ArrayList<String> project_list = new ArrayList<String>();
				try{
					JSONArray project_arr = projectjson.getJSONArray("Runs");
					setProjectRunData(project_arr);
					JSONObject jobj = new JSONObject();
					String end_time = "";
					String end_epoch_time = "";
					String deployment_id = "";
					String mct_mode_status = "";
					String nct_mode_status = "";
					String project_name = "";
					String testedBy = "";
					String customerReferenceNo = "";
					String energy_Flow_Mode = "";
					for(int i =0; i<project_arr.length(); i++ ){
						try{
							jobj = project_arr.getJSONObject(i);
							//customer_name =  model.getString("customer_name");
							//equipment_serial_no_ =  model.getString("equipment_serial_no");
							//customer_name =  jobj.getString("customer_name");
							//equipment_serial_no_ =  jobj.getString("equipment_serial_no");
							end_time = jobj.getString("end_time");
							end_epoch_time = jobj.getString("epoch_end_time");
							deployment_id = jobj.getString("deployment_id");
							testedBy  = jobj.getString("tested_by");
							mct_mode_status = jobj.getString("mct_mode_completed");
							nct_mode_status = jobj.getString("nct_mode_completed");
							project_name = jobj.getString("project_name");
							customerReferenceNo = jobj.getString("customer_reference_no");
							energy_Flow_Mode = jobj.getString("energy_flow_mode");
							ApplicationLauncher.logger.info("refreshReportList: project_name: " + project_name);
							//ApplicationLauncher.logger.info("refreshReportList: equipment_serial_no_: " + equipment_serial_no_);
							//if((mct_mode_status.equals("Y"))||(nct_mode_status.equals("Y"))){
							//customernamelist.add(customer_name);
							//equipmentSerialNolist.add(equipment_serial_no_);

							end_time=end_time.replaceFirst("(.*)"+".0"+"$","$1"+"") ; // remove the last ".0" for the display
							//project_list.add(project_name +"_"+ end_time);
							ProjectEndTimeList.add(project_name +"_"+ end_time);//end_time);
							deploymentIdList.add(deployment_id);
							testerNameList.add(testedBy);
							mctCompletedList.add(mct_mode_status);
							nctCompletedList.add(nct_mode_status);
							projectNameList.add(project_name);
							customerRefNoList.add(customerReferenceNo);
							energyFlowMode.add(energy_Flow_Mode);
							ApplicationLauncher.logger.debug("refreshReportList: Index: " + i);
							//ApplicationLauncher.logger.info("refreshReportList: customer_name2: " + customer_name);
							//ApplicationLauncher.logger.info("refreshReportList: equipment_serial_no_2: " + equipment_serial_no_);

							ApplicationLauncher.logger.debug("refreshReportList: end_time: " + end_time);
							ApplicationLauncher.logger.debug("refreshReportList: deployment_id: " + deployment_id);
							ApplicationLauncher.logger.debug("refreshReportList: customerReferenceNo: " + customerReferenceNo);
							ApplicationLauncher.logger.debug("refreshReportList: project_name2: " + project_name);
							//}
							//				customernamelist.add(customer_name);
							//				equipmentSerialNolist.add(equipment_serial_no_);
							//				ProjectEndTimeList.add(end_time);
							//				deployement_id_list.add(deployment_id);
							/*				StringBuilder b = new StringBuilder(start_time);
								b.replace(start_time.lastIndexOf(".0"), start_time.lastIndexOf(".0") + 1, "" );
								start_time = b.toString();*/
							//start_time= start_time.replaceAll(".0", "");
							///////////start_time=start_time.replaceFirst("(.*)"+".0"+"$","$1"+"") ; // remove the last ".0" for the display
							/////////project_list.add(project +"_"+ start_time);

							//project_list.add(end_time);
						}catch (JSONException e2){
							e2.printStackTrace();
							ApplicationLauncher.logger.error("refreshReportList: : JSONException2 :"+e2.getMessage());
							status = false;
						}
					}

					if(ProjectEndTimeList.size()!=0){
						//loadCustomerNameList();
						//loadEquipmentSerialNumber();
						loadProjectEndTime();
						EnableDisplayResultsButton();
					}else{
						Platform.runLater(() -> {
							// ref_cmbBox_CustomerName.getItems().clear();
							// ref_cmbBox_SerialNumber.getItems().clear();
							ref_cmbBoxProjectList.getItems().clear();
							ref_cmbBoxRangeSelect.getItems().clear();
							// ref_txtHumidity.clear();
							// ref_txtTemperature.clear();
							//btn_ExportAllResults.setVisible(false);
							//btn_GenerateReport.setVisible(false);
							btn_GenerateReport.setDisable(true);
							btn_ExportAllResults.setDisable(true);
							ref_result_table_view.getItems().clear();

						});
						disableDisplayResultsButton();
						status = false;
						//ApplicationLauncher.logger.info("refreshReportList: No Results- Results not found! - Prompted");
						//ApplicationLauncher.InformUser("No Results", "Results not found!!", AlertType.ERROR);
					}
				}catch (JSONException e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("refreshReportList: : JSONException :"+e1.getMessage());
					status = false;
				}
				//				if(project_list.size() != 0){
				//					cmbBoxProjectList.getItems().clear();
				//					cmbBoxProjectList.getItems().addAll(project_list);
				//					cmbBoxProjectList.getSelectionModel().select(0);
				//					cmbBoxProjectList.setVisible(true);
				//					btn_get_results.setVisible(true);
				//				}
			}
		}catch (Exception e2){
			e2.printStackTrace();
			ApplicationLauncher.logger.error("refreshReportList: : Exception :"+e2.getMessage());
			status = false;
		}
		return status;
	}

	public void EnableDisplayResultsButton(){
		Platform.runLater(() -> {
			//btn_get_results.setVisible(true);
			btn_get_results.setDisable(false);
		});
	}

	public void disableDisplayResultsButton(){
		Platform.runLater(() -> {
			//btn_get_results.setVisible(false);
			btn_get_results.setDisable(true);
		});
	}

	public void GetProjectOnClick(){ // throws ParseException, JSONException{

		ClearLastDisplayedResultData();
		ApplicationLauncher.logger.info("from time: " + spinner_fromtime.getValue());
		ApplicationLauncher.logger.info("to time: " + spinner_totime.getValue());
		String fromdate = datepicker_fromdate.getValue().toString() +" " + spinner_fromtime.getValue();
		String todate = datepicker_todate.getValue().toString() + " " + spinner_totime.getValue();
		try{
			if((!fromdate.isEmpty()) && (!todate.isEmpty())){
				long from_epoch = calcEpoch(fromdate);
				long to_epoch = calcEpoch(todate);
				JSONObject projectjson = MySQL_Controller.sp_get_project_run(from_epoch, to_epoch);
				ArrayList<String> project_list = new ArrayList<String>();
				JSONArray project_arr = projectjson.getJSONArray("Runs");
				setProjectRunData(project_arr);
				JSONObject jobj = new JSONObject();
				String project = "";
				String start_time = "";
				for(int i =0; i<project_arr.length(); i++ ){
					jobj = project_arr.getJSONObject(i);
					project = jobj.getString("project_name");
					start_time = jobj.getString("start_time");
					/*				StringBuilder b = new StringBuilder(start_time);
					b.replace(start_time.lastIndexOf(".0"), start_time.lastIndexOf(".0") + 1, "" );
					start_time = b.toString();*/
					//start_time= start_time.replaceAll(".0", "");
					start_time=start_time.replaceFirst("(.*)"+".0"+"$","$1"+"") ; // remove the last ".0" for the display
					project_list.add(project +"_"+ start_time);
				}
				if(project_list.size() != 0){
					cmbBoxProjectList.getItems().clear();
					cmbBoxProjectList.getItems().addAll(project_list);
					cmbBoxProjectList.getSelectionModel().select(0);
					cmbBoxProjectList.setVisible(true);
					btn_get_results.setVisible(true);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getStartEndEpochTime: Exception:" + e.getMessage());
		}


	}

	public void setProjectRunData(JSONArray ProjectRun_data){
		ProjectRunData = ProjectRun_data;
	}

	public JSONArray getProjectRunData(){
		return ProjectRunData;
	}

	public ArrayList<Long> getStartEndEpochTime(String selected_project_run){
		JSONArray projectrundata = getProjectRunData();
		ArrayList<Long> start_end_epoch_time = new ArrayList<Long>();
		String[] test_params = selected_project_run.split("_");
		String project_name = test_params[0];
		String start_time = test_params[1];
		try {
			JSONObject jobj = new JSONObject();
			String project = "";
			String starttime = "";
			for(int i=0; i<projectrundata.length(); i++){
				jobj = projectrundata.getJSONObject(i);
				project = jobj.getString("project_name");
				starttime = jobj.getString("start_time");
				if(project_name.equals(project) && start_time.equals(starttime)){
					long start_epoch_time = jobj.getLong("epoch_start_time");
					long end_epoch_time = jobj.getLong("epoch_end_time");
					start_end_epoch_time.add(start_epoch_time);
					start_end_epoch_time.add(end_epoch_time);
					return start_end_epoch_time;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getStartEndEpochTime: JSONException:" + e.getMessage());
		}
		return start_end_epoch_time;
	}



	public void FetchResultDataFromDBTrigger() {
		GetResultDataTimer = new Timer();
		GetResultDataTimer.schedule(new FetchResultDataFromDB(),100);// 1000);
		ApplicationLauncher.logger.info("FetchResultDataFromDBTrigger Invoked:");
	}

	class FetchResultDataFromDB extends TimerTask {
		public void run() {
			ApplicationLauncher.setCursor(Cursor.WAIT);
			//ApplicationLauncher.logger.info("FetchResultDataFromDB: WAIT");
			ApplicationLauncher.logger.info("FetchResultDataFromDB: Invoked");
			try {
				String selectedProjectRun = "";
				try{
					selectedProjectRun = ref_cmbBoxProjectList.getSelectionModel().getSelectedItem().toString();
				}catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					ApplicationLauncher.logger.error("FetchResultDataFromDB: Exception1:" + e2.getMessage());
				}
				if  (!selectedProjectRun.isEmpty()) {
					Platform.runLater(() -> {
						enableBusyLoadingScreen();
					});
					GetResultDetails();
					disableBusyLoadingScreen();
				}else{
					ApplicationLauncher.logger.info("FetchResultDataFromDB: Kindly redefine project search criteria - Prompted");

					ApplicationLauncher.InformUser("Redefine search project","Kindly redefine project search criteria", AlertType.ERROR);
				}
				/*			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FetchResultDataFromDB: JSONException:" + e.getMessage());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ApplicationLauncher.logger.error("FetchResultDataFromDB: ParseException:" + e1.getMessage());*/
			}catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				ApplicationLauncher.logger.error("FetchResultDataFromDB: Exception2:" + e2.getMessage());
			}


			GetResultDataTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			//ApplicationLauncher.logger.info("FetchResultDataFromDB: DEFAULT");
		}
	}

	public static String DiffTime(String dateStart, String dateStop) {


		ApplicationLauncher.logger.debug("DiffTime: Entry");
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			/*			ApplicationLauncher.logger.info(diffDays + " days, ");
			ApplicationLauncher.logger.info(diffHours + " hours, ");
			ApplicationLauncher.logger.info(diffMinutes + " minutes, ");
			ApplicationLauncher.logger.info(diffSeconds + " seconds.");*/
			return (diffHours +":"+diffMinutes+":"+diffSeconds);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("DiffTime: Exception: "+e.getMessage());
			return "";
		}

	}


	public String get_cmbBoxProjectListCurrentValue(){
		return ref_cmbBoxProjectList.getValue()+".0";
	}

	public static String getProjectListCurrentValue(){
		return ref_cmbBoxProjectList.getValue()+".0";
	}

	/*	public void GetResultDetails() throws ParseException, JSONException, Exception{


		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.info("GetResultDetails: project_name: "+project_name);
		if(project_name != null){
			ArrayList<Long> from_to_epoch = getStartEndEpochTime(project_name);
			String[] test_params = project_name.split("_");
			project_name = test_params[0];
			ApplicationLauncher.logger.info("GetResultDetails: project_name2: "+project_name);
			long from_epoch = from_to_epoch.get(0);
			long to_epoch = from_to_epoch.get(1); 

			ApplicationLauncher.logger.info("GetResultDetails: from_epoch: "+from_epoch);
			ApplicationLauncher.logger.info("GetResultDetails: to_epoch: "+to_epoch);
			ApplicationLauncher.logger.info("GetResultDetails: RESULT_DATA_TYPE_ERROR_VALUE: "+ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			ApplicationHomeController.update_left_status("Fetching results from DB...",ConstantApp.LEFT_STATUS_DEBUG);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime DB_StartTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("GetResultDetails: DB Fetch Start Time: "+dtf.format(DB_StartTime));
			JSONObject result = MySQL_Controller.sp_getresult_data(from_epoch, to_epoch, project_name,ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			LocalDateTime DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("GetResultDetails: DB Fetch End Time: "+dtf.format(DB_EndTime));

			ApplicationLauncher.logger.info("GetResultDetails: Difference Time: "+ DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			if(result.length()> 0){
				setLastDisplayedResultData(result);
			}
			//ApplicationLauncher.logger.info("GetResultDetails:"+result);

			if(!result.isNull("Results")){//Uncomment


				if (result.getInt("No_of_results")!=0){
					Platform.runLater(() -> {
						try {
							ApplicationHomeController.update_left_status("Publishing results on the table",ConstantApp.LEFT_STATUS_DEBUG);
							SetResultTable(result);
							ApplicationHomeController.update_left_status("Result publish completed",ConstantApp.LEFT_STATUS_DEBUG);
							LocalDateTime DisplayEndTime = LocalDateTime.now();
							ApplicationLauncher.logger.info("GetResultDetails: Display update End Time: "+dtf.format(DisplayEndTime));
							ApplicationLauncher.logger.info("GetResultDetails: Difference Time from DB to Display end: "+ DiffTime(dtf.format(DB_StartTime),dtf.format(DisplayEndTime)));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("GetResultDetails: Exception: "+e.getMessage());
						} 
					});
				}else{
					ApplicationLauncher.logger.info("GetResultDetails: No Results- Result not found! - Prompted");

					ApplicationLauncher.InformUser("No Results", "Result not found!!", AlertType.ERROR);
				}
			}
		}else{
			ApplicationLauncher.logger.info("GetResultDetails: No project selected- Kindly select a project - Prompted");
			ApplicationLauncher.InformUser("No project selected", "Kindly select a project", AlertType.ERROR);
		}

		btn_GenerateReport.setVisible(true);// here


	}*/

	public static String getSelectedDeploymentID() {
		return selectedDeploymentID;
	}



	public static void setSelectedDeploymentID(String selectedDeploymentID) {
		TestReportController.selectedDeploymentID = selectedDeploymentID;
	}

	public void GetResultDetails() {


		//String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		String deployment_id = "";
		String neutral_ct_mode_ = "";
		String main_ct_mode_ = "";
		String project_name = "";
		String testedBy = "";
		String customerReferenceNo = "";
		String energy_Flow_Mode = "";
		//String eqp_serial_no = "";

		String project_end_time = ref_cmbBoxProjectList.getSelectionModel().getSelectedItem().toString();;
		ApplicationLauncher.logger.info("GetResultDetails: project_end_time1: "+project_end_time);

		/*		String DateinReport = project_end_time;
		//SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		//Date datenew=null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter_ = new SimpleDateFormat("dd-MM-yyyy");

		try {
			String[] endTimeSplit = project_end_time.split("_");
			project_end_time = endTimeSplit[1];
            Date date = formatter.parse(DateinReport);
            //date = date.;
            String strDate = formatter_.format(date);
            DateinReport = strDate;
            //date.parse(strDate,formatter_).
            //Calendar cal = Calendar.getInstance();
            //Date today = cal.getTime();
            //date.add(Calendar.YEAR, 1); // to get previous year add -1
            //Date nextYear = cal.getTime();
            //ApplicationLauncher.logger.debug(date);
            //ApplicationLauncher.logger.debug(formatter.format(date));

        } catch (ParseException e) {
        	ApplicationLauncher.logger.info("GetResultDetails: date format exception: "+e.toString());
            e.printStackTrace();
        }*/

		//		try {
		//			datenew = df.parse(DateinReport);
		//		} catch (ParseException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//			ApplicationLauncher.logger.error("datenew: ParseException:"+e.getMessage());
		//		}
		//DateinReport = datenew.toString();

		//ApplicationLauncher.logger.info("GetResultDetails: DateinReport: "+DateinReport);

		if(project_end_time != null){
			//jhfjfjf;


			//String current_customer_name = ref_cmbBox_CustomerName.getSelectionModel().getSelectedItem();
			for(int i = 0;i<ProjectEndTimeList.size();i++) {
				ApplicationLauncher.logger.debug("GetResultDetails: project_end_time2: "+project_end_time);
				ApplicationLauncher.logger.debug("GetResultDetails: ProjectEndTimeList.get("+i+"): "+ProjectEndTimeList.get(i));
				if((project_end_time).equals(ProjectEndTimeList.get(i))) {
					deployment_id = deploymentIdList.get(i);
					project_name = projectNameList.get(i);
					testedBy = testerNameList.get(i);
					customerReferenceNo = customerRefNoList.get(i);
					//eqp_serial_no = equipmentSerialNolist.get(i);
					main_ct_mode_ = mctCompletedList.get(i);
					neutral_ct_mode_ = nctCompletedList.get(i);
					energy_Flow_Mode = energyFlowMode.get(i);
					ApplicationLauncher.logger.debug("GetResultDetails:  deployment_id1:"+deployment_id);
					break;
					//txtCustomerAddressArea.setText(customerAddresslist.get(j));
					//status = true;
					//j=i;
				}	
			}
			ApplicationLauncher.logger.info("GetResultDetails:  getResultFilterMctNctMode: "+getResultFilterMctNctMode());

			ApplicationLauncher.logger.info("GetResultDetails:  RESULT_DATA_TYPE_ERROR_VALUE: "+ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			ApplicationLauncher.logger.info("GetResultDetails:  deployment_id2: "+deployment_id);
			ApplicationLauncher.logger.info("GetResultDetails:  customerReferenceNo: "+customerReferenceNo);
			//			ArrayList<Long> from_to_epoch = getStartEndEpochTime(project_name);
			//			String[] test_params = project_name.split("_");
			//			project_name = test_params[0];
			//			project_name = "";
			//			ApplicationLauncher.logger.info("GetResultDetails: project_name2: "+project_name);
			//			long from_epoch = from_to_epoch.get(0);
			//			long to_epoch = from_to_epoch.get(1); 
			//
			//			ApplicationLauncher.logger.info("GetResultDetails: from_epoch: "+from_epoch);
			//			ApplicationLauncher.logger.info("GetResultDetails: to_epoch: "+to_epoch);
			//			ApplicationLauncher.logger.info("GetResultDetails: RESULT_DATA_TYPE_ERROR_VALUE: "+ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			//			ApplicationHomeController.update_left_status("Fetching results from DB...",ConstantApp.LEFT_STATUS_DEBUG);
			//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			//			LocalDateTime DB_StartTime = LocalDateTime.now();
			//			ApplicationLauncher.logger.info("GetResultDetails: DB Fetch Start Time: "+dtf.format(DB_StartTime));
			//JSONObject result = MySQL_Controller.sp_getresult_data(from_epoch, to_epoch, project_name,
			//		ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE,getSelectedDeploymentID());
			//JSONObject result = MySQL_Controller.sp_getresult_data(1576151252, 1576152083, "TEST02_|_T2CT",
			//		ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE,"43");



			JSONObject result = MySQL_Controller.sp_get_completed_result_data(getResultFilterMctNctMode(),ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE,deployment_id);
			LocalDateTime DB_EndTime = LocalDateTime.now();
			//			ApplicationLauncher.logger.info("GetResultDetails: DB Fetch End Time: "+dtf.format(DB_EndTime));
			//SetReportRadioButtons(pt_mode_,ct_mode_);
			setSelectedDeploymentID(deployment_id);
			setSelectedProjectName(project_name);
			setSelectedExecutionTesterName(testedBy);
			setSelectedCustomerRefNo(customerReferenceNo);
			setSelectedEnergyFlowMode(energy_Flow_Mode);
			if(main_ct_mode_.equals("Y")){
				setSelectedExecutionMainCt(true);
			}else{
				setSelectedExecutionMainCt(false);
			}

			if(neutral_ct_mode_.equals("Y")){
				setSelectedExecutionNeutralCt(true);
			}else{
				setSelectedExecutionNeutralCt(false);
			}
			//setSelectedExecutionMainCt();
			//setSelectedExecutionNeutralCt();
			//setSelectedEquipmentSerialNo(eqp_serial_no);
			//			ApplicationLauncher.logger.info("GetResultDetails: Difference Time: "+ DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			if(result.length()> 0){

				setLastDisplayedResultData(result);
				//getFilteredResultJson(getResultFilterMctNctMode()
			}
			//ApplicationLauncher.logger.info("GetResultDetails:"+result);

			if(!result.isNull("Results")){//Uncomment

				try{
					if (result.getInt("No_of_results")!=0){
						Platform.runLater(() -> {
							try {
								ApplicationLauncher.logger.info("GetResultDetails:  result: "+result);
								ApplicationHomeController.update_left_status("Publishing results on the table",ConstantApp.LEFT_STATUS_DEBUG);
								//setFetchedResultJson(result);
								SetResultTable(getFilteredResultJson(getResultFilterMctNctMode()));

								//SetReportRadioButtons(pt_mode_,ct_mode_);
								ApplicationHomeController.update_left_status("Result publish completed",ConstantApp.LEFT_STATUS_DEBUG);
								LocalDateTime DisplayEndTime = LocalDateTime.now();
								//updateHumidityTemperatureData();
								//updateUserCommentsData();

								btn_GenerateReport.setDisable(false);
								btn_ExportAllResults.setDisable(false);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("GetResultDetails: Exception: "+e.getMessage());
								//Platform.runLater(() -> {
								//btn_GenerateReport.setVisible(false);// here
								//btn_ExportAllResults.setVisible(false);
								btn_GenerateReport.setDisable(true);
								btn_ExportAllResults.setDisable(true);
								//});
							} 
						});
					}else{
						Platform.runLater(() -> {
							//btn_GenerateReport.setVisible(false);// here
							//btn_ExportAllResults.setVisible(false);
							ref_result_table_view.getItems().clear();
							btn_GenerateReport.setDisable(true);
							btn_ExportAllResults.setDisable(true);
						});

						ApplicationLauncher.logger.info("GetResultDetails: No Results- Result not found! - Prompted");

						ApplicationLauncher.InformUser("No Results", "Result not found!!", AlertType.ERROR);

					}
				} catch (JSONException e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("GetResultDetails: JSONException:"+e.getMessage());

				}
			}

		}else{
			ApplicationLauncher.logger.info("GetResultDetails: No project selected- Kindly select a project - Prompted");
			ApplicationLauncher.InformUser("No project selected", "Kindly select a project", AlertType.ERROR);
			Platform.runLater(() -> {
				btn_GenerateReport.setVisible(false);// here
				btn_ExportAllResults.setVisible(false);
			});
		}

		//btn_GenerateReport.setVisible(true);// here



	}

	@FXML
	public void radioBtnNeutralCtOnChange(){
		if (ref_radioBtnNeutralCt.isSelected()){
			setMainCtMode(false);
			setNeutralCtMode(true);
			setResultFilterMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT);

			Platform.runLater(() -> {
				ref_radioBtnMainCt.setSelected(false);
				//radioBtnbothCTandPTReport.setSelected(false);
				try{
					SetResultTable(getFilteredResultJson(getResultFilterMctNctMode()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("radioBtnNeutralCtOnChange: Exception: "+ e.getMessage());
				}
			});
		}else{

		}
	}

	@FXML
	public void radioBtnMainCtOnChange(){
		if (ref_radioBtnMainCt.isSelected()){
			setMainCtMode(true);
			setNeutralCtMode(false);
			setResultFilterMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT);

			Platform.runLater(() -> {
				ref_radioBtnNeutralCt.setSelected(false);
				//radioBtnbothCTandPTReport.setSelected(false);
				try{
					SetResultTable(getFilteredResultJson(getResultFilterMctNctMode()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("radioBtnMainCtOnChange: Exception: "+ e.getMessage());
				}
			});
		}else{

		}
	}

	public JSONObject getFilteredResultJson(String filterMctNctMode) {

		JSONObject filteredResultJson = new JSONObject();
		JSONArray filteredResultArray = new JSONArray();

		JSONObject allResultJson = new JSONObject();
		JSONArray allResultArray  = new JSONArray();

		int count = 0;

		try {
			allResultJson = getLastDisplayedResultData();
			if(allResultJson!=null){
				allResultArray = allResultJson.getJSONArray("Results");

				JSONObject jobj = new JSONObject();
				String mctNctModeData = "";

				for(int i=0; i<allResultArray.length(); i++){
					jobj = allResultArray.getJSONObject(i);	
					mctNctModeData = jobj.getString("main_neutral_ct_mode");
					if(mctNctModeData.contains(filterMctNctMode)){

						//ApplicationLauncher.logger.error ("sp_getresult_data :"+jobj.getString("ratio_error"));
						filteredResultArray.put(jobj);
						count++;
					}
				}


				filteredResultJson.put("No_of_results", count);
				filteredResultJson.put("Results", filteredResultArray);
			}else{
				filteredResultJson = null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getFilteredResultJson: JSONException: "+ e.getMessage());
		}
		return filteredResultJson;
	}

	public void setLastDisplayedResultData(JSONObject result){
		LastDisplayedResultData = result;
	}

	public JSONObject getLastDisplayedResultData(){
		return LastDisplayedResultData;
	}

	public void ClearLastDisplayedResultData(){
		LastDisplayedResultData= null;
	}

	@FXML
	public void GetDetailsOnClick() {//throws ParseException, JSONException{
		FetchResultDataFromDBTrigger();

	}
	
	@FXML
    void SummaryResultsOnClick() {

    }
	
	

	public void clearResultView(){
		Platform.runLater(() -> {
			ref_result_table_view.getItems().clear();


		});
	}




	public void SetResultTable(JSONObject result) throws JSONException{
		ref_result_table_view.getColumns().clear();
		ref_result_table_view.getItems().clear();

		ArrayList<String> devicesList = getDeviceList(result);
		ArrayList<String> columnNames = new ArrayList<String>();
		devicesList = getColNamesFromDB(getSelectedProjectName(),getSelectedDeploymentID());
		columnNames.add("S.No");
		columnNames.add("Test Point");
		//ApplicationLauncher.logger.info("SetResultTable: devicesList1: "+devicesList);
		//Collections.sort(devicesList);
		
		//Collections.sort(devicesList, Comparator.comparing(Integer::valueOf)); // commented on s4.2.0.9.0.5
		ApplicationLauncher.logger.info("SetResultTable: devicesList2: "+devicesList);
		for(int i=0; i<devicesList.size(); i++){
			//columnNames.add(devicesList.get(i) + "-Status");
			columnNames.add(devicesList.get(i));// + "-Error_Value");
		}

		//devicesList.sort(Comparator.comparing(Double::parseDouble));

		/*		columnNames = Array.sort(columnNames, new Comparator<String>() {
	        @Override
	        public int compare(String o1, String o2) {
	            return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
	        }
	    });*/
		//columnNames.sort();
		ApplicationLauncher.logger.info("SetResultTable:"+columnNames);
		ArrayList<ArrayList<Object>> initrowValues = initRowValues(result, devicesList);
		ArrayList<ArrayList<Object>> rowValues = setRowValues(columnNames, result, initrowValues);
		insertTableValues(columnNames, rowValues);
		ref_result_table_view.setVisible(true);	
		//Platform.runLater(() -> {
		GuiUtils.autoResizeColumns(result_table_view,false);
		//});
	}
	
	public static ArrayList<String> getColNamesFromDB(String project_name, String getSelectedDeployment_ID){
		ApplicationLauncher.logger.debug("getColNamesFromDB : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		ArrayList<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("getColNamesFromDB : project_name: "+ project_name);
		ApplicationLauncher.logger.debug("getColNamesFromDB : getSelectedDeployment_ID: "+ getSelectedDeployment_ID);
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(project_name,getSelectedDeployment_ID);

		try {
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			String StrippedDeviceName="";
			String rack_id = "";
			ApplicationLauncher.logger.debug("getColNamesFromDB : arr length: " + arr.length());
			getDeviceIdSerialNumberHashMap().clear();
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				StrippedDeviceName = FetchLastEightCharacter(device_name);
				col.add(StrippedDeviceName + "/" + rack_id);
				getDeviceIdSerialNumberHashMap().put(rack_id, StrippedDeviceName);
				

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getColNamesFromDB: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("getColNamesFromDB: col:"+col);

		return col;
	}
	
	public static String FetchLastEightCharacter(String input){

		//ApplicationLauncher.logger.debug("FetchLastEightCharacter: : Entry");
		String Output= "";
		int length_of_input=input.length();
		//ApplicationLauncher.logger.debug("length_of_input:"+length_of_input);	
		if(length_of_input<=8){
			//ApplicationLauncher.logger.debug("input:"+input);
			Output = input;
		}
		else{
			int strip_input=length_of_input-8;
			String strip_input_string=input.substring(strip_input, length_of_input);
			//ApplicationLauncher.logger.debug("stripped_input_string"+strip_input_string);
			Output= strip_input_string;

		}
		return Output;
	}



	public static String getResultFilterMctNctMode() {
		return resultFilterMctNctMode;
	}

	public void setResultFilterMctNctMode(String filterMctNctMode) {
		this.resultFilterMctNctMode = filterMctNctMode;
	}

	public String getRealDeviceIDForExportMode( String device_name){
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){

			if(Integer.valueOf(device_name)>=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
				device_name = String.valueOf (Integer.valueOf(device_name) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
			}

		}
		
		device_name = getDeviceIdSerialNumberHashMap().get(device_name) + "/" + device_name;
		
		return device_name;
	}
	public ArrayList<ArrayList<Object>> setRowValues(ArrayList<String> columnNames,JSONObject result ,ArrayList<ArrayList<Object>> rowValues) throws JSONException{
		ArrayList<ArrayList<Object>> row_Values = new ArrayList<ArrayList<Object>>();
		JSONArray result_arr = result.getJSONArray("Results");
		JSONObject jobj = new JSONObject();
		String test_name = "";
		String alais_id = "";
		String device_name = "";
		String test_result = "";
		String error_value = "";
		for(int i=0; i<result_arr.length(); i++){
			jobj = result_arr.getJSONObject(i);
			test_name = jobj.getString("test_case_name");
			alais_id = jobj.getString("alias_id");
			device_name = jobj.getString("device_name");
			device_name = getRealDeviceIDForExportMode(device_name);
			//ApplicationLauncher.logger.debug("setRowValues : device_name: " + device_name);
			/*if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){

				if(Integer.valueOf(device_name)>ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
					device_name = String.valueOf (Integer.valueOf(device_name) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
				}

			}*/
			test_result = jobj.getString("test_status");
			error_value = test_result +" " +jobj.getString("error_value");				  
			ResultDataModel record = new ResultDataModel(test_name, alais_id, device_name, test_result, error_value);
			row_Values = insertcellvalue(columnNames, record, rowValues);
		}
		return row_Values;
	}

	public ArrayList<ArrayList<Object>> insertcellvalue(ArrayList<String> columnNames, ResultDataModel record,ArrayList<ArrayList<Object>> rowValues){
		String testcaseName = record.gettestCaseName();// + record.getaliasID();

		String columnName = "";// + "-Error_Value";
		String testCaseResult = "";
		String testErrorValue = "";
		for(int i =0; i <rowValues.size(); i++ ){
			ArrayList<Object> row = rowValues.get(i);
			if(testcaseName.equals(row.get(1))){
				columnName = record.getdeviceName();// + "-Error_Value";
				for(int j=0; j<columnNames.size(); j++){
					if(columnName.equals(columnNames.get(j))){
						testCaseResult = record.gettestResult();
						testErrorValue = record.geterrorValue();

						row.set(j, testErrorValue);

					}
				}
			}
			rowValues.set(i, row);
		}

		return rowValues;
	}

	public void insertTableValues(ArrayList<String> columnNames, ArrayList<ArrayList<Object>> rowValues){
		for (int i = 0; i < columnNames.size(); i++) {
			TableColumn<List<Object>, Object> col = new TableColumn<List<Object>, Object>(columnNames.get(i));

			int j = i;
			col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(j)));
			col.setSortable(false);

			Platform.runLater(() -> {
				ref_result_table_view.getColumns().add(col);
			});
			col.setCellFactory(cellData -> new TableCell<List<Object>, Object>() {


				@Override
				public void updateItem(Object item, boolean empty) {

					// Always invoke super constructor.
					//ApplicationLauncher.logger.info("insertTableValues : updateItem");
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						String CurrentValue = (String)item;


						try{        

							if (CurrentValue.contains("WFR") ) {	

								//this.setStyle("-fx-background-color: yellow;");
								this.setStyle("-fx-text-fill: blue;");
								//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
							} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
								//this.setStyle("-fx-background-color: green;");
								this.setStyle("-fx-text-fill: green;");
								if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

									CurrentValue = CurrentValue.substring(2,CurrentValue.length());
								}
								//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
							}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
								//this.setStyle("-fx-background-color: red;");
								this.setStyle("-fx-text-fill: red;");
								if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

									CurrentValue = CurrentValue.substring(2,CurrentValue.length());
								}
								//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
							}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
							//this.setStyle("-fx-background-color: red;");
							this.setStyle("-fx-text-fill: black;");
							if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								CurrentValue = CurrentValue.substring(2,CurrentValue.length());
							}
							//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
							}else  if (CurrentValue.isEmpty()){	
								this.setStyle("-fx-text-fill: black;");

								//this.setStyle("-fx-background-color: transparent;");
								//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
							}
						} catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

						}
						setText(CurrentValue);
					}

				}

			});

		}

		ref_result_table_view.getItems().setAll(FXCollections.observableList(rowValues));
	}

	public void printresults(JSONObject result) throws JSONException{
		JSONArray result_arr = result.getJSONArray("Results");
		JSONObject jobj = new JSONObject();
		for(int i=0; i<result_arr.length(); i++){
			/*ResultDataModel data = (ResultDataModel) result.get(i);
		   ApplicationLauncher.logger.info("*******************************");
		   ApplicationLauncher.logger.info("Test Case Name: " + data.gettestCaseName());
		   ApplicationLauncher.logger.info("Test Alias ID : " + data.getaliasID());
		   ApplicationLauncher.logger.info("Model Name    : " + data.getdeviceName());
		   ApplicationLauncher.logger.info("Test Result   : " + data.gettestResult());
		   ApplicationLauncher.logger.info("Error Value   : " + data.geterrorValue());*/

			jobj = result_arr.getJSONObject(i);
			ApplicationLauncher.logger.info("*******************************");
			ApplicationLauncher.logger.info("Test Point Name: " + jobj.getString("test_case_name"));
			ApplicationLauncher.logger.info("Test Alias ID : " + jobj.getString("alias_id"));
			ApplicationLauncher.logger.info("Device Name    : " + jobj.getString("device_name"));
			ApplicationLauncher.logger.info("Test Result   : " + jobj.getString("test_status"));
			ApplicationLauncher.logger.info("Error Value   : " + jobj.getString("error_value"));
		}
	}

	public ArrayList<ArrayList<Object>> initRowValues(JSONObject result, ArrayList<String> devicesList) throws JSONException{
		ArrayList<String> testCaseList = gettestCases(result);
		ArrayList<ArrayList<Object>> rowValues = new ArrayList<ArrayList<Object>>() ;
		for(int i=0; i<testCaseList.size(); i++){
			ArrayList<Object> row = new ArrayList<Object>();
			row.clear();
			row.add(Integer.toString(i+1));
			row.add(testCaseList.get(i));
			for(int j=0; j<(devicesList.size()); j++){
				row.add(ConstantApp.TableRowFiller);
			}
			rowValues.add(row);

		}
		return rowValues;


	}

	public ArrayList<String> gettestCases(JSONObject result) throws JSONException{
		ArrayList<String> testCaseList = new ArrayList<String>();
		JSONArray result_arr = result.getJSONArray("Results");
		JSONObject jobj = new JSONObject();
		String testcase_name =  "";
		for(int i = 0; i < result_arr.length(); i++){

			jobj = result_arr.getJSONObject(i);
			testcase_name =  jobj.getString("test_case_name");// +  jobj.getString("alias_id");
			AddToTestList(testcase_name, testCaseList);
		}		

		return testCaseList;
	}

	public void AddToTestList(String testcase_name, ArrayList<String> testCaseList){
		boolean isExists = false;
		if(testCaseList.size() != 0){
			for(int i = 0; i<testCaseList.size(); i++){
				if(testCaseList.get(i).equals(testcase_name)){
					isExists = true;
				}
			}
		}
		if(!isExists){
			testCaseList.add(testcase_name);
		}
	}

	public ArrayList<String> getDeviceList(JSONObject result) throws JSONException{
		ArrayList<String> deviceList = new ArrayList<String>();
		JSONArray result_arr = result.getJSONArray("Results");
		ApplicationLauncher.logger.info("getDeviceList: result: " + result_arr);
		JSONObject jobj = new JSONObject();
		String deviceName =  "";
		for(int i = 0; i < result_arr.length(); i++){

			jobj = result_arr.getJSONObject(i);
			deviceName =  jobj.getString("device_name");
			AddToDeviceList(deviceName, deviceList);
		}		

		return deviceList;
	}

	public void AddToDeviceList(String deviceName, ArrayList<String> device_list){
		boolean AlreadyExists = false;
		/*		if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){

				if(Integer.valueOf(deviceName)>ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
					deviceName = String.valueOf (Integer.valueOf(deviceName) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
				}

		}*/
		deviceName = getRealDeviceIDForExportMode(deviceName);
		if(device_list.size() != 0){
			for(int i = 0; i<device_list.size(); i++){
				if(device_list.get(i).equals(deviceName)){
					AlreadyExists = true;
				}
			}
		}

		if(!AlreadyExists){
			//ApplicationLauncher.logger.info("AddToDeviceList: deviceName: " + deviceName);
			device_list.add(deviceName);
		}

	}



	public long calcEpoch(String Date_time) throws ParseException{
		long epoch = 0;
		//String str = "2014-07-04 04:05:10";   // UTC
		String str = Date_time;   // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = df.parse(str);
		epoch = datenew.getTime() /1000;


		return epoch;
	}

	/*	public void initResultTable(){
		result_table_view.getColumns().clear();
		result_table_view.getItems().clear();	

		List<String> columnNames = new ArrayList<String>();
		List<List<Object>> rowValues = new ArrayList<List<Object>>();

		columnNames.add("Test Point");
		columnNames.add("Device 1");
		columnNames.add("Device 2");
		columnNames.add("Device 3");
		columnNames.add("Device 4");


		for(int i=0; i<4; i++){
			List<Object> row = new ArrayList<Object>();
			row.add("test_name"+ i);
			row.add("Pass");
			row.add("Fail");
			row.add("Pass");
			row.add("Incomplete");
			rowValues.add(row);
		}



		TableColumn<List<Object>, Object> column = new TableColumn<List<Object>, Object>("Test Point");
		column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
		result_table_view.getColumns().add(column);


		for (int i = 1; i < columnNames.size(); i++) {
			TableColumn<List<Object>, Object> col = new TableColumn<List<Object>, Object>(columnNames.get(i));

			int j = i;
			col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(j)));
			result_table_view.getColumns().add(col);

		}

		result_table_view.getItems().setAll(FXCollections.observableList(rowValues));

	}*/

	public void SelectAllOnClick(){
		if(chk_bx_SelectAll.isSelected()){
			if(chk_bx_self_heating.isVisible()){
				chk_bx_self_heating.setSelected(true);
			}
			if(chk_bx_repeatability.isVisible()){
				chk_bx_repeatability.setSelected(true);
			}
			if(chk_bx_frequency_variation.isVisible()){
				chk_bx_frequency_variation.setSelected(true);
			}
			if(chk_bx_voltage_variation.isVisible()){
				chk_bx_voltage_variation.setSelected(true);
			}
			if(chk_bx_accuracy.isVisible()){
				chk_bx_accuracy.setSelected(true);
			}
			if(chk_bx_phase_reversal.isVisible()){
				chk_bx_phase_reversal.setSelected(true);
			}
			if(chk_bx_harmonics.isVisible()){
				chk_bx_harmonics.setSelected(true);
			}
			if(chk_bx_voltage_unbalance.isVisible()){
				chk_bx_voltage_unbalance.setSelected(true);
			}
			if(chk_bx_constant_test.isVisible()){
				chk_bx_constant_test.setSelected(true);
			}
			if(chk_bx_no_load.isVisible()){
				chk_bx_no_load.setSelected(true);
			}
			if(chk_bx_sta.isVisible()){
				chk_bx_sta.setSelected(true);

			}
		}
		else{

			if(chk_bx_self_heating.isVisible()){
				chk_bx_self_heating.setSelected(false);
			}
			if(chk_bx_repeatability.isVisible()){
				chk_bx_repeatability.setSelected(false);
			}
			if(chk_bx_frequency_variation.isVisible()){
				chk_bx_frequency_variation.setSelected(false);
			}
			if(chk_bx_voltage_variation.isVisible()){
				chk_bx_voltage_variation.setSelected(false);
			}
			if(chk_bx_accuracy.isVisible()){
				chk_bx_accuracy.setSelected(false);
			}
			if(chk_bx_phase_reversal.isVisible()){
				chk_bx_phase_reversal.setSelected(false);
			}
			if(chk_bx_harmonics.isVisible()){
				chk_bx_harmonics.setSelected(false);
			}
			if(chk_bx_voltage_unbalance.isVisible()){
				chk_bx_voltage_unbalance.setSelected(false);
			}
			if(chk_bx_constant_test.isVisible()){
				chk_bx_constant_test.setSelected(false);
			}
			if(chk_bx_no_load.isVisible()){
				chk_bx_no_load.setSelected(false);
			}
			if(chk_bx_sta.isVisible()){
				chk_bx_sta.setSelected(false);
			}

		}
	}

	public void GenerateReportTrigger() {
		GetResultDataTimer = new Timer();
		GetResultDataTimer.schedule(new GenerateReport(),100);// 1000);
		//GetResultDataTimer.schedule(new GenerateConveyorMeterIndividualReport(),100);// 1000);
		
		//ApplicationLauncher.logger.info("GenerateReportTrigger Invoked:");
	}

	public boolean CheckAtleastOneCheckBoxEnabled() {

		if (chk_bx_self_heating.isSelected()||
				chk_bx_repeatability.isSelected()||
				chk_bx_frequency_variation.isSelected()||
				chk_bx_voltage_variation.isSelected()||
				chk_bx_accuracy.isSelected()||
				chk_bx_phase_reversal.isSelected()||
				chk_bx_harmonics.isSelected()||
				chk_bx_voltage_unbalance.isSelected()||
				chk_bx_constant_test.isSelected()||
				chk_bx_no_load.isSelected() || 
				chk_bx_sta.isSelected()){
			return true;
		}
		return false;
	}

	public void meterSelectionStageDisplay(){

		ApplicationLauncher.logger.info("meterSelectionStageDisplay: entry");

		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/fxml/testreport/MeterSelection" + ConstantApp.THEME_FXML));
		Scene newScene = null;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			// TODO: handle error
			ex.printStackTrace();
			ApplicationLauncher.logger.error("meterSelectionStageDisplay :IOException:" + ex.getMessage());
			// return;
		}

		meterSelectionStage = new Stage();

		Stage primaryStage = ApplicationLauncher.getPrimaryStage();

		meterSelectionStage.initModality(Modality.WINDOW_MODAL);
		meterSelectionStage.initOwner(primaryStage);


		meterSelectionStage.setTitle("Meter Selection");
		meterSelectionStage.setScene(newScene);
		meterSelectionStage.setMinWidth(375);//410);
		meterSelectionStage.setMinHeight(270);//350);
		meterSelectionStage.setMaxWidth(375);//410);
		meterSelectionStage.setMaxHeight(270);//350);
		meterSelectionStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		meterSelectionStage.setAlwaysOnTop(true);
		meterSelectionStage.setOnCloseRequest(e -> e.consume());

		meterSelectionStage.showAndWait();

	}
	
	


	class GenerateReport extends TimerTask {
		public void run() {
			ApplicationLauncher.reportGenerationFlag = true;
			String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
			ApplicationLauncher.logger.info("GenerateReport: project_name: "+project_name);
			ApplicationLauncher.logger.info("GenerateReport: getSelectedReportProfile: "+getSelectedReportProfile());
			if(project_name != null){

				//if(getSelectedReportProfile().equals(ConstantAppConfig.DefaultReportProfileDisplay)){
				if(getSelectedReportProfile().equals(ConstantApp.StandardReportProfileDisplay)){
					ApplicationLauncher.logger.info("GenerateReport: StandardReportProfileDisplay: ");
					if (CheckAtleastOneCheckBoxEnabled()){
						/*setIndividualMeterReportSelected(false);
						if(ProcalFeatureEnable.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
							ArrayList<String> meterSerialNoList = new ArrayList<String> ();
							//MeterSelectionController.updateMeterListData(meterSerialNoList);
							Platform.runLater(() -> {							
								meterSelectionStageDisplay();

							});
							ApplicationLauncher.logger.info("GenerateReport: While Entry");
							while(MeterSelectionController.isScreenDisplayed());
							MeterSelectionController.setScreenDisplayed(true);
							ApplicationLauncher.logger.info("GenerateReport: While Exit");
							setSelectedMeterSerialNo(MeterSelectionController.getSelectedMeterSerialNo());

							if(!getSelectedMeterSerialNo().equals(ConstantReport.REPORT_SELECTED_ALL_METERS)){
								setIndividualMeterReportSelected(true);
							}
						}*/


						ApplicationLauncher.setCursor(Cursor.WAIT);
						ApplicationLauncher.logger.info("GenerateReport: WAIT-1");
						//ApplicationLauncher.logger.info("FetchResultDataFromDB: Invoked");
						GenerateReportOnClick();


					}else{
						ApplicationLauncher.logger.info("GenerateReport: No reports selected - Atleast one report should be selected- Prompted ");
						ApplicationLauncher.InformUser("No reports selected", "Atleast one report should be selected", AlertType.ERROR);


					}
				}else if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
					ApplicationLauncher.logger.info("GenerateReport: REPORT_GENERATION_V2_ENABLED: " + ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED);
					ApplicationLauncher.setCursor(Cursor.WAIT);
					ApplicationLauncher.logger.info("GenerateReport: WAIT-2");
					generateCustom1Report();
				}else if(ConstantAppConfig.REPORT_PROFILE_LIST.size() > 1){ 
					ApplicationLauncher.logger.info("GenerateReport: Report Profile List: " + ConstantAppConfig.REPORT_PROFILE_LIST);
					//ApplicationLauncher.logger.debug("GenerateReport: ConstantAppConfig.REPORT_PROFILE_LIST.get(0): " + ConstantAppConfig.REPORT_PROFILE_LIST.get(0));
					//ApplicationLauncher.logger.debug("GenerateReport: ConstantAppConfig.REPORT_PROFILE_LIST.get(1): " + ConstantAppConfig.REPORT_PROFILE_LIST.get(1));
					//ApplicationLauncher.logger.debug("GenerateReport: ConstantAppConfig.REPORT_PROFILE_LIST.get(2): " + ConstantAppConfig.REPORT_PROFILE_LIST.get(2));
					//ApplicationLauncher.logger.debug("GenerateReport: ConstantAppConfig.REPORT_PROFILE_LIST.get(3): " + ConstantAppConfig.REPORT_PROFILE_LIST.get(3));

					/*					if(getSelectedReportProfile().equals(ConstantAppConfig.REPORT_PROFILE_LIST.get(1))){
						ApplicationLauncher.setCursor(Cursor.WAIT);
						ApplicationLauncher.logger.info("GenerateReport: WAIT-2");
						Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH,ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(1));
						Custom1ReportConfigLoader.init();
						generateCustom1Report();
					}else if(ConstantAppConfig.REPORT_PROFILE_LIST.size() > 2){ 
						if(getSelectedReportProfile().equals(ConstantAppConfig.REPORT_PROFILE_LIST.get(2))){
							ApplicationLauncher.setCursor(Cursor.WAIT);
							ApplicationLauncher.logger.info("GenerateReport: WAIT-3");
							Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH, ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(2));
							Custom1ReportConfigLoader.init();
							generateCustom1Report();
							//generateCustom2Report();
						}
					}*/

					for(int i = 0 ; i < ConstantAppConfig.REPORT_PROFILE_LIST.size(); i++){
						if(getSelectedReportProfile().equals(ConstantAppConfig.REPORT_PROFILE_LIST.get(i))){
							ApplicationLauncher.setCursor(Cursor.WAIT);
							ApplicationLauncher.logger.info("GenerateReport: WAIT-2A");
							Custom1ReportConfigLoader.setConfigFilePathName(ConstantAppConfig.REPORT_PROFILE_PATH,ConstantAppConfig.REPORT_PROFILE_CONFIG_PATH_LIST.get(i));
							Custom1ReportConfigLoader.init();
							generateCustom1Report();
							break;
						}
					}
					//}
				}
			}else{
				ApplicationLauncher.logger.info("GenerateReport: No project selected- No Project is selected - Prompted");
				ApplicationLauncher.InformUser("No project selected", "No Project selected", AlertType.ERROR);
			}
			GetResultDataTimer.cancel();
			ApplicationLauncher.reportGenerationFlag = false;

			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("GenerateReport: Exit");
		}
	}

	public void manipulateReportSerialNo(){
		ApplicationLauncher.logger.debug("manipulateReportSerialNo: Entry" );
		try{
			String project_end_time =ref_cmbBoxProjectList.getSelectionModel().getSelectedItem().toString().replace(getSelectedProjectName(), "").replace("_", "");
			ApplicationLauncher.logger.debug("manipulateReportSerialNo: project_end_time: "+ project_end_time);
			String projectExecutedYear = project_end_time.substring(0,4);
			ApplicationLauncher.logger.debug("manipulateReportSerialNo: projectExecutedYear: "+ projectExecutedYear);
			setSelectedReportSerialNo(getSelectedDeploymentID()+ "/"+ projectExecutedYear);
			ApplicationLauncher.logger.debug("manipulateReportSerialNo: getSelectedReportSerialNo: "+ getSelectedReportSerialNo());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("manipulateReportSerialNo : Exception:" + e1.getMessage());
		}

	}

	public void manipulateProjectExecutedDate(){
		ApplicationLauncher.logger.debug("manipulateProjectExecutedDate: Entry" );
		try{
			String project_end_time =ref_cmbBoxProjectList.getSelectionModel().getSelectedItem().toString().replace(getSelectedProjectName(), "").replace("_", "");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


			Date date = df.parse(project_end_time);
			long executedEndTimeEpochInSec = date.getTime();///1000;
			ApplicationLauncher.logger.debug("manipulateProjectExecutedDate: executedEndTimeEpochInSec: "+ executedEndTimeEpochInSec);
			SimpleDateFormat reportExecutedDateFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_DATE_FORMAT);

			reportExecutedDateFormat.setTimeZone(TimeZone.getTimeZone(ConstantAppConfig.REPORT_TIME_ZONE));
			String formattedDate = reportExecutedDateFormat.format(date);
			setSelectedProjectExecutedDate(formattedDate);
			ApplicationLauncher.logger.debug("manipulateProjectExecutedDate: formattedDate:"+ formattedDate);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("manipulateProjectExecutedDate : Exception:" + e1.getMessage());
		}
	}

	public void manipulateProjectExecutedTime(){
		ApplicationLauncher.logger.debug("manipulateProjectExecutedTime: Entry" );
		try{
			String project_end_time =ref_cmbBoxProjectList.getSelectionModel().getSelectedItem().toString().replace(getSelectedProjectName(), "").replace("_", "");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


			Date date = df.parse(project_end_time);
			long executedEndTimeEpoch = date.getTime();///1000;
			ApplicationLauncher.logger.debug("manipulateProjectExecutedTime: executedEndTimeEpoch: "+ executedEndTimeEpoch);
			SimpleDateFormat reportExecutedTimeFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_TIME_FORMAT);
			reportExecutedTimeFormat.setTimeZone(TimeZone.getTimeZone(ConstantAppConfig.REPORT_TIME_ZONE));
			String formattedTime = reportExecutedTimeFormat.format(date);
			setSelectedProjectExecutedTime(formattedTime);
			ApplicationLauncher.logger.debug("manipulateProjectExecutedTime: formattedTime:"+ formattedTime);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("manipulateProjectExecutedTime : Exception:" + e1.getMessage());
		}
	}

	public void showMeterSelectionDisplay(JSONObject result){
		ApplicationLauncher.logger.info("showMeterSelectionDisplay: Entry");
		setIndividualMeterReportSelected(false);
		setSelectedMeterSerialNo(ConstantReport.REPORT_SELECTED_ALL_METERS);
		if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
			ArrayList<String> meterSerialNoList = new ArrayList<String> ();
			meterSerialNoList = getMeterSerialNoList(result);
			ApplicationLauncher.logger.info("showMeterSelectionDisplay: meterSerialNoList : " + meterSerialNoList);
			MeterSelectionController.updateMeterListData(meterSerialNoList);
			Platform.runLater(() -> {							
				meterSelectionStageDisplay();

			});
			ApplicationLauncher.logger.info("showMeterSelectionDisplay: While Entry");
			while(MeterSelectionController.isScreenDisplayed());
			MeterSelectionController.setScreenDisplayed(true);
			ApplicationLauncher.logger.info("showMeterSelectionDisplay: While Exit");
			setSelectedMeterSerialNo(MeterSelectionController.getSelectedMeterSerialNo());

			if(!getSelectedMeterSerialNo().equals(ConstantReport.REPORT_SELECTED_ALL_METERS)){
				setIndividualMeterReportSelected(true);
			}
		}

	}

	public void GenerateReportOnClick(){
		boolean ResultNotFound = false;
		String ReportsFailed = "";
		boolean status = false;
		JSONObject result = getLastDisplayedResultData();
		//ApplicationLauncher.logger.debug("GenerateReportOnClick: result:"+result);
		if((result == null) || (result.length()== 0)){
			ApplicationHomeController.update_left_status("Fetching results from DB..",ConstantApp.LEFT_STATUS_DEBUG);
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		}

		try {

			if (result.getInt("No_of_results")!=0){



				int model_id = MySQL_Controller.sp_getProjectModel_ID(getSelectedProjectName());


				JSONObject modeldata = MySQL_Controller.sp_getem_model_data(model_id);
				setMeterProfileData(modeldata);
				manipulateReportSerialNo();
				manipulateProjectExecutedDate();
				manipulateProjectExecutedTime();


				JSONArray devices = GetMeterNamesFromDB();
				ApplicationLauncher.logger.debug("GenerateReportOnClick: devices:"+ devices);
				setMeterNames(devices);
				showMeterSelectionDisplay(result);
				ApplicationHomeController.update_left_status("Processing data for report..",ConstantApp.LEFT_STATUS_DEBUG);



				if(ConstantAppConfig.METER_PROFILE_REPORT_ENABLED){
					status = false;
					ApplicationHomeController.update_left_status("Processing Meter Profile data...",ConstantApp.LEFT_STATUS_DEBUG);

					status = exportMeterProfileReport(result);
					if(!status){
						ResultNotFound=true;
						ReportsFailed = "Meter Profile report \n"+ReportsFailed;
					}
				}


				if(chk_bx_self_heating.isSelected()){
					if(checkParams(TestProfileType.SelfHeating.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing SELFH data...",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportSelfHeatingReport(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Self-Heating Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Self-Heating \n"+ReportsFailed;
					}
				}
				if(chk_bx_repeatability.isSelected()){
					if(checkParams(TestProfileType.Repeatability.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing repeatability data...",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportRepeatability(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Repeatablity Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Repeatablity \n"+ReportsFailed;
					}
				}
				if(chk_bx_frequency_variation.isSelected()){
					if(checkParams(TestProfileType.InfluenceFreq.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing frequency report data...",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportFrequencyVariation(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Frequency variation Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Frequency variation \n"+ReportsFailed;
					}
				}
				if(chk_bx_voltage_variation.isSelected()){
					if(checkParams(TestProfileType.InfluenceVolt.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing VV report data..",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportVoltageVariation(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Voltage variation Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Voltage variation \n"+ReportsFailed;
					}
				}
				if(chk_bx_accuracy.isSelected()){

					if(checkParams(TestProfileType.Accuracy.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing LOE report data..",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportAccuracy(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Accuracy Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Accuracy  \n"+ReportsFailed;
					}

					//if(checkParams("UnbalancedLoad")){

					if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
						//int model_id = MySQL_Controller.sp_getProjectModel_ID(getSelectedProjectName());
						//JSONObject modeldata = MySQL_Controller.sp_getem_model_data(model_id);

						if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
							String meterModelType = getMeterProfileData().getString("model_type");
							ApplicationLauncher.logger.debug("GenerateReportOnClick: meterModelType:"+ meterModelType);
							if( (!meterModelType.contains("Single Phase "))   ){

								if(checkParams(ConstantApp.TEST_PROFILE_UNBALANCED_LOAD)){
									status = false;
									ApplicationHomeController.update_left_status("Processing unbalanced report data..",ConstantApp.LEFT_STATUS_DEBUG);

									status = ExportUnbalanceLoad(result);
									if(!status){
										ResultNotFound=true;
										ReportsFailed = "Unbalanced Load Test \n"+ReportsFailed;
									}
								}else{
									//if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
									ResultNotFound=true;
									ReportsFailed = "Unbalanced Load \n"+ReportsFailed;
									//}
								}
							}else {
								//ResultNotFound=true;
								//ReportsFailed = "Unbalanced Load not generated\n"+ReportsFailed;
							}
						}else{
							//if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
							//	ResultNotFound=true;
							//	ReportsFailed = "Unbalanced Load \n"+ReportsFailed;
							//}
						}
					}
				}
				if(chk_bx_phase_reversal.isSelected()){
					if(checkParams(TestProfileType.PhaseReversal.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing RPS report data..",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportRPS(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Phase reversal sequence Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Phase reversal sequence \n"+ReportsFailed;
					}
				}
				if(chk_bx_harmonics.isSelected()){
					if(checkParams(TestProfileType.InfluenceHarmonic.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing Harmonics report data..",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportHARM(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Harmonics Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Harmonics \n"+ReportsFailed;
					}
				}
				if(chk_bx_voltage_unbalance.isSelected()){
					if(checkParams(TestProfileType.VoltageUnbalance.toString())){
						status = false;
						ApplicationHomeController.update_left_status("Processing VU report data..",ConstantApp.LEFT_STATUS_DEBUG);

						status = ExportVU(result);
						if(!status){
							ResultNotFound=true;
							ReportsFailed = "Voltage Unbalance Test \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Voltage Unbalance \n"+ReportsFailed;
					}
				}
				if(chk_bx_constant_test.isSelected()){
					if(checkParams(TestProfileType.ConstantTest.toString())){
						JSONObject const_result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						if (const_result.getInt("No_of_results")!=0){
							status = false;
							ApplicationHomeController.update_left_status("Processing Const Test report data..",ConstantApp.LEFT_STATUS_DEBUG);

							status = ExportCONST(const_result);
							if(!status){
								ResultNotFound=true;
								ReportsFailed = "Constant Test \n"+ReportsFailed;
							}
						}else{
							ResultNotFound=true;
							ReportsFailed = "Constant \n"+ReportsFailed;
						}
					}
					else{
						ResultNotFound=true;
						ReportsFailed = "Constant report configuration\n"+ReportsFailed;
					}
				}
				if(chk_bx_no_load.isSelected()){
					if(checkParams(TestProfileType.NoLoad.toString())){
						JSONObject creep_result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						if (creep_result.getInt("No_of_results")!=0){
							status = false;
							ApplicationHomeController.update_left_status("Processing NoLoad report data..",ConstantApp.LEFT_STATUS_DEBUG);

							status = ExportCREEP(creep_result);
							if(!status){
								ResultNotFound=true;
								ReportsFailed = "NoLoad Test \n"+ReportsFailed;
							}
						}else{
							ResultNotFound=true;
							ReportsFailed = "NoLoad \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "NoLoad report configuration\n"+ReportsFailed;
					}
				}
				if(chk_bx_sta.isSelected()){
					if(checkParams(TestProfileType.STA.toString())){
						JSONObject STA_result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						if (STA_result.getInt("No_of_results")!=0){
							status = false;
							status = ExportSTA(STA_result);
							ApplicationHomeController.update_left_status("Processing STA report data..",ConstantApp.LEFT_STATUS_DEBUG);

							if(!status){
								ResultNotFound=true;
								ReportsFailed = "Starting Current Test \n"+ReportsFailed;
							}
						}else{
							ResultNotFound=true;
							ReportsFailed = "Starting Current \n"+ReportsFailed;
						}
					}else{
						ResultNotFound=true;
						ReportsFailed = "Starting Current report configuration\n"+ReportsFailed;
					}
				}

				if(ResultNotFound){
					ApplicationLauncher.logger.info("GenerateReport: Few reports not exported- Report generation completed. Below few reports not generated due to insufficient data\n"
							+ReportsFailed+ "- Prompted");

					ApplicationLauncher.InformUser("Few reports not exported", 
							"Report generation completed. Below few reports not generated due to insufficient data\n"
									+ReportsFailed, AlertType.WARNING);
				}else{
					ApplicationHomeController.update_left_status("Report generation Success",ConstantApp.LEFT_STATUS_DEBUG);

					ApplicationLauncher.logger.info("GenerateReport: Export Successful- Report generated successfully- Prompted");

					ApplicationLauncher.InformUser("Export Successful", "Report generated successfully", AlertType.INFORMATION);
				}

				//status = SaveExcelAsPDF();
				status=true;
			}else{
				ApplicationLauncher.logger.info("GenerateReport: Export Failed- Result not found! - Prompted");
				ApplicationHomeController.update_left_status("Result not found",ConstantApp.LEFT_STATUS_DEBUG);

				ApplicationLauncher.InformUser("Export Failed", "Result not found!!", AlertType.ERROR);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("exportToExcel : JSONException:" + e.getMessage());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("GenerateReportOnClick : Exception:" + e1.getMessage());
		}
	}


	private ReportProfileManage readReportProfileManageFromDataBaseWithProfileName(String reportGroupName, String reportProfileName, String baseTemplateName) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("readReportProfileManageFromDataBaseWithProfileName : Entry");

		boolean activeProfile = true;
		String reportProfileDefaultActiveCustomerId =  ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID; 

		//List<ReportProfileMeterMetaDataFilter> meterMetaDataDatabaseList = dataManagerObj.getReportProfileMeterMetaDataFilterService().findAll();
		List<ReportProfileManage> reportProfileActiveDatabaseList = getReportProfileManageService().findByActiveProfileAndCustomerIdAndReportGroupNameAndReportProfileNameAndBaseTemplateName ( activeProfile, reportProfileDefaultActiveCustomerId, reportGroupName,  reportProfileName,  baseTemplateName);
		if(reportProfileActiveDatabaseList.size()>0){
			return reportProfileActiveDatabaseList.get(0);
		}else{
			ApplicationLauncher.logger.debug("readReportProfileManageFromDataBaseWithProfileName : No Records found");
		}
		return null;
	}


	public void generateCustom1Report(){

		ApplicationLauncher.logger.debug("generateCustom1Report: Entry");

		boolean ResultNotFound = false;
		String ReportsFailed = "";
		boolean status = false;
		JSONObject result = getLastDisplayedResultData();
		//ApplicationLauncher.logger.debug("GenerateReportOnClick: result:"+result);
		if((result == null) || (result.length()== 0)){
			ApplicationHomeController.update_left_status("Fetching results from DB..",ConstantApp.LEFT_STATUS_DEBUG);
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		}

		try {

			if (result.getInt("No_of_results")!=0){



				int model_id = MySQL_Controller.sp_getProjectModel_ID(getSelectedProjectName());


				JSONObject modeldata = MySQL_Controller.sp_getem_model_data(model_id);
				setMeterProfileData(modeldata);
				manipulateReportSerialNo();
				manipulateProjectExecutedDate();
				manipulateProjectExecutedTime();


				JSONArray devices = GetMeterNamesFromDB();
				ApplicationLauncher.logger.debug("generateCustom1Report: devices:"+ devices);
				setMeterNames(devices);
				showMeterSelectionDisplay(result);
				ApplicationHomeController.update_left_status("Processing data for report..",ConstantApp.LEFT_STATUS_DEBUG);



				//if(chk_bx_accuracy.isSelected()){
				status = true;
				if(status){
					//if(checkParams(TestProfileType.Accuracy.toString())){
					status = false;
					ApplicationHomeController.update_left_status("Processing Custom1Report data..",ConstantApp.LEFT_STATUS_DEBUG);

					if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){

						boolean individualReportProfileSelected = true;

						if(individualReportProfileSelected){
							String selectedReportProfileGroupName = "RPG3";
							String selectedReportProfileName = "RPG3-PR1";
							String selectedBaseTemplateName = "Base Template1";


							selectedReportProfileGroupName = "RPG4";
							selectedReportProfileName = "RPG4-PR1";


							selectedReportProfileGroupName = "RPG5";
							selectedReportProfileName = "RPG5-RP1";
							selectedReportProfileName = "RPG5-PR2";
							selectedReportProfileName = "RPG5-PR3";
							selectedReportProfileName = "RPG5-PR4";

							selectedReportProfileGroupName = (String)ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem();//.toString();
							selectedReportProfileName = (String)ref_cmbBxReportProfile.getSelectionModel().getSelectedItem();//.toString();
							if(selectedReportProfileGroupName != null){
								if(selectedReportProfileName!=null){
									ApplicationLauncher.logger.debug("generateCustom1Report: selectedReportProfileGroupName: " + selectedReportProfileGroupName);
									ApplicationLauncher.logger.debug("generateCustom1Report: selectedReportProfileName: " + selectedReportProfileName);
									ReportProfileManage reportProfileManage = readReportProfileManageFromDataBaseWithProfileName(selectedReportProfileGroupName, selectedReportProfileName,  selectedBaseTemplateName);

									ApplicationLauncher.logger.debug("generateCustom1Report: reportProfileManage.getOutputFolderPath: " + reportProfileManage.getOutputFolderPath());
									ApplicationLauncher.logger.debug("generateCustom1Report: reportProfileManage.getTemplateFileNameWithPath: " + reportProfileManage.getTemplateFileNameWithPath());

									reportProfileManage.getDutMetaDataList().forEach( e-> {
										ApplicationLauncher.logger.debug("generateCustom1Report: getPageNumber: " + e.getPageNumber());
										ApplicationLauncher.logger.debug("generateCustom1Report: getMeterDataType: " + e.getMeterDataType());
										ApplicationLauncher.logger.debug("generateCustom1Report: getCellPosition: " + e.getCellPosition());
									});

									int maxPageNumber = reportProfileManage.getDutMetaDataList().stream()
											.mapToInt(e -> e.getPageNumber())
											.max()
											.getAsInt();


									//final String dutOverAllStatus = ConstantReportV2.REPORT_META_DATATYPE_DUT_OVERALL_STATUS;
									for (int i =0 ; i < reportProfileManage.getDutMetaDataList().size(); i++){

										String meterDatatype = reportProfileManage.getDutMetaDataList().get(i).getMeterDataType();

										switch(meterDatatype){
										case ConstantReportV2.REPORT_META_DATATYPE_SERIAL_NO:
											reportProfileManage.getDutMetaDataList().get(i).setPopulateSerialNo(true);
											break;

										case ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutSerialNo(true);   
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_DUT_MAKE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateMake(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_DUT_MODEL_NO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateModelNo(true);
											break;
											
											
										case ConstantReportV2.REPORT_META_DATATYPE_BATCH_NO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateCustomerRefNo(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_CAPACITY:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateCapacity(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_DUT_TYPE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateMeterType(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_METER_CONSTANT:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateMeterConstant(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_PT_RATIO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulatePtRatio(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_CT_RATIO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateCtRatio(true);
											break;
										case ConstantReportV2.REPORT_META_DATATYPE_RACK_POSITION_NO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateRackPositionNo(true);
											break;	



										case ConstantReportV2.REPORT_META_DATATYPE_DUT_CLASS:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutClass(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_DUT_BASIC_CURRENT:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutBasicCurrent(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_DUT_MAX_CURRENT:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutMaxCurrent(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_DUT_RATED_VOLT:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutRatedVolt(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_DUT_FREQ:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutFreq(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_CT_TYPE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutCtType(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_CUSTOMER_NAME:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateCustomerName(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_LORA_ID:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateLoraId(true);
											break;

										case ConstantReportV2.REPORT_META_DATATYPE_EXEC_TIME_STAMP:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateExecutedTimeStamp(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_EXEC_DATE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateExecutedDate(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_EXEC_TIME:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateExecutedTime(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_TIME_STAMP:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateReportGenTimeStamp(true);
											break;

										case ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_DATE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateReportGenDate(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_REPORT_GEN_TIME:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateReportGenTime(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_APPROVED_TIME_STAMP:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateApprovedTimeStamp(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_APPROVED_DATE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateApprovedDate(true);
											break;


										case ConstantReportV2.REPORT_META_DATATYPE_APPROVED_TIME:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateApprovedTime(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_TESTED_BY:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateTestedBy(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_WITNESSED_BY:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateWitnessedBy(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_APPROVED_BY:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateApprovedBy(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_PAGE_NO:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulatePageNo(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_MAX_NO_OF_PAGES:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateMaxNoOfPages(true);
											break;	


										case ConstantReportV2.REPORT_META_DATATYPE_PAGE_NO_WITH_MAX_NO_OF_PAGES:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulatePageNoWithMaxNoOfPages(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_ENERGY_FLOW_MODE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateEnergyFlowMode(true);
											break;	
										case ConstantReportV2.REPORT_META_DATATYPE_EXECUTION_CT_MODE:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateExecutionCtMode(true);
											break;	

										case ConstantReportV2.REPORT_META_DATATYPE_ACTIVE_REACTIVE_ENERGY:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateActiveReactiveEnergy(true);
											break;	
											
										case ConstantReportV2.REPORT_META_DATATYPE_COMPLIES:  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateComplyStatus(true);
											break;	
											
											

										default: break;
										}

										if(meterDatatype.equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_OVERALL_STATUS)){  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutOverAllStatus(true);
											//break;
										}

										if(meterDatatype.equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_PAGE_STATUS)){  
											reportProfileManage.getDutMetaDataList().get(i).setPopulateDutPageStatus(true);
											//break;
										}

										//}


									}
									/*
									ReportGeneration.setProcessRepeatAverageValue(false);
									ReportGeneration.setProcessRepeatAverageStatus(false);
									ReportGeneration.setProcessSelfHeatAverageValue(false);
									ReportGeneration.setProcessSelfHeatAverageStatus(false);*/
									
									

									for (int i =0 ; i < reportProfileManage.getReportProfileTestDataFilterList().size(); i++){
										ReportProfileTestDataFilter reportProfileTestDataFilter = reportProfileManage.getReportProfileTestDataFilterList().get(i);
										String testFilterPreview = reportProfileTestDataFilter.getFilterPreview();
										String testTypeAlias = reportProfileTestDataFilter.getTestTypeAlias();

										ReportGeneration.getProcessRepeatAverageValueHashMap().put(testFilterPreview,false);
										ReportGeneration.getProcessRepeatAverageStatusHashMap().put(testFilterPreview,false);
										ReportGeneration.getProcessSelfHeatAverageValueHashMap().put(testFilterPreview,false);
										ReportGeneration.getProcessSelfHeatAverageStatusHashMap().put(testFilterPreview,false);
										
										for (int j =0 ; j < reportProfileTestDataFilter.getRpPrintPositionList().size(); j++){
											if(reportProfileTestDataFilter.getRpPrintPositionList().get(j).getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER)){
												RpPrintPosition rpPrintPositionList = reportProfileTestDataFilter.getRpPrintPositionList().get(j);

												String keyParam = rpPrintPositionList.getKeyParam();
												ApplicationLauncher.logger.info("generateCustom1Report: keyParam: " + keyParam);
												switch(keyParam){
												/*case ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_DATA_KEY:
													//rpPrintPositionList.setPopulateResultValue(true);
													break;*/

												case ConstantReportV2.POPULATE_LOCAL_OUTPUT_STATUS_KEY:
													rpPrintPositionList.setPopulateLocalResultStatus(true);
													break;

												case ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY:
													rpPrintPositionList.setPopulateMasterResultStatus(true);
													break;

													/*	case ConstantReportV2.:
													rpPrintPositionList.setPopulateResultValue(true);
													break;
												case ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER:
													rpPrintPositionList.setPopulateResultValue(true);
													break;*/

												/*case ConstantReportV2.CELL_START_POSITION_HEADER_RESULT_STATUS_KEY:
													rpPrintPositionList.setPopulateLocalResultStatus(true);
													break;*/

/*												case ConstantReportV2.CELL_HEADER_POSITION_HEADER_RESULT_RSM_INITIAL:
													//rpPrintPositionList.setPopulateResultValue(true);
													break;

												case ConstantReportV2.CELL_HEADER_POSITION_HEADER_RESULT_RSM_FINAL:
													//rpPrintPositionList.setPopulateResultValue(true);
													break;*/
													
												case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL:
													rpPrintPositionList.setPopulateHeaderRsmInitial(true);
													break;

												case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL:
													rpPrintPositionList.setPopulateHeaderRsmFinal(true);
													break;
													
												case ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_DIFFERENCE:
													rpPrintPositionList.setPopulateHeaderRsmDifference(true);
													break;	
													

												case ConstantReportV2.POPULATE_HEADER1_KEY:
													rpPrintPositionList.setPopulateHeader1(true);
													break;

												case ConstantReportV2.POPULATE_HEADER2_KEY:
													rpPrintPositionList.setPopulateHeader2(true);
													break;

												case ConstantReportV2.POPULATE_HEADER3_KEY:
													rpPrintPositionList.setPopulateHeader3(true);
													break;

												case ConstantReportV2.POPULATE_HEADER4_KEY:
													rpPrintPositionList.setPopulateHeader4(true);
													break;

												case ConstantReportV2.POPULATE_HEADER5_KEY:
													rpPrintPositionList.setPopulateHeader5(true);
													break;

																						
												
												case ConstantReportV2.POPULATE_HEADER_KEY_TEST_PERIOD_IN_MINUTES:
													rpPrintPositionList.setPopulateHeaderTestPeriodInMinutes(true);
													break;													
												case ConstantReportV2.POPULATE_HEADER_KEY_WARMUP_PERIOD_IN_MINUTES:
													rpPrintPositionList.setPopulateHeaderWarmupPeriodInMinutes(true);
													break;
												case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_VOLTAGE:
													rpPrintPositionList.setPopulateHeaderTargetVoltage(true);
													break;
												case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_CURRENT:
													rpPrintPositionList.setPopulateHeaderTargetCurrent(true);
													break;
												case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_PF:
													rpPrintPositionList.setPopulateHeaderTargetPf(true);
													break;
												case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_FREQ:
													rpPrintPositionList.setPopulateHeaderTargetFreq(true);
													break;
												case ConstantReportV2.POPULATE_HEADER_KEY_TARGET_ENERGY:
													rpPrintPositionList.setPopulateHeaderTargetEnergy(true);
													break;
													
												case ConstantReportV2.POPULATE_LOCAL_UPPER_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderUpperLimit(true);
													break;

												case ConstantReportV2.POPULATE_LOCAL_LOWER_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderLowerLimit(true);
													break;

												case ConstantReportV2.POPULATE_LOCAL_MERGED_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderMergedLimit(true);
													break;	


												case ConstantReportV2.POPULATE_MASTER_UPPER_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderUpperLimit(true);
													break;

												case ConstantReportV2.POPULATE_MASTER_LOWER_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderLowerLimit(true);
													break;
												case ConstantReportV2.POPULATE_MASTER_MERGED_LIMIT_KEY:
													rpPrintPositionList.setPopulateHeaderMergedLimit(true);
													break;		


													/*													populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
											}else if(populateResultFilterDutInitialReading){
												populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH;
											}else if(populateResultFilterDutFinalReading){
												populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_FINAL_KWH;
													 */	
												default: break;
												}

												if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY)){
													rpPrintPositionList.setPopulateLocalResultStatus(true);
												} 
												
												
/*												else if(keyParam.equals(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_INITIAL_REGISTER)){  
													rpPrintPositionList.setPopulateResultValue(true);

												}else if(keyParam.equals(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_FINAL_REGISTER)){  
													rpPrintPositionList.setPopulateResultValue(true);

												}*/
												
												else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER)){
													rpPrintPositionList.setPopulateResultValue(true);
													rpPrintPositionList.setPopulateEachDutInitialRegister(true);
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER)){
													rpPrintPositionList.setPopulateResultValue(true);
													rpPrintPositionList.setPopulateEachDutFinalRegister(true);
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE)){
													rpPrintPositionList.setPopulateEachDutFinalInitialDifference(true);
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_PULSE_COUNT)){
													rpPrintPositionList.setPopulateResultValue(true);
													rpPrintPositionList.setPopulateEachDutPulseCount(true);
													//rpPrintPositionList.setTestExecutionResultTypeSelected(keyParam);
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE)){
													if(testTypeAlias!=null){
														if(!testTypeAlias.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){
															//rpPrintPositionList.setPopulateEachDutErrorPercent(true);
															rpPrintPositionList.setPopulateResultValue(true);
														}
													}
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION)){
													rpPrintPositionList.setPopulateEachDutOnePulsePeriod(true);													
													
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_VALUE)){
													rpPrintPositionList.setPopulateEachDutAverageValue(true);
													
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE_STATUS)){
													rpPrintPositionList.setPopulateEachDutAverageStatus(true);
												
												
												
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE)){
													rpPrintPositionList.setPopulateEachDutAverageValue(true);
													//ReportGeneration.setProcessRepeatAverageValue(true);
													ReportGeneration.getProcessRepeatAverageValueHashMap().put(testFilterPreview, true);
													
													
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS)){
													rpPrintPositionList.setPopulateEachDutAverageStatus(true);
													//ReportGeneration.setProcessRepeatAverageStatus(true);
													ReportGeneration.getProcessRepeatAverageStatusHashMap().put(testFilterPreview, true);
													
													
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE)){
													rpPrintPositionList.setPopulateEachDutAverageValue(true);
													//ReportGeneration.setProcessSelfHeatAverageValue(true);
													ReportGeneration.getProcessSelfHeatAverageValueHashMap().put(testFilterPreview,true);
													
												}else if(keyParam.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS)){
													rpPrintPositionList.setPopulateEachDutAverageStatus(true);
													//ReportGeneration.setProcessSelfHeatAverageStatus(true);
													ReportGeneration.getProcessSelfHeatAverageStatusHashMap().put(testFilterPreview,true);
													
												
												}else if(keyParam.startsWith(ConstantReportV2.CELL_START_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY)){
													rpPrintPositionList.setPopulateResultValue(true);
												}else if(keyParam.startsWith(ConstantReportV2.CELL_START_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY)){
													rpPrintPositionList.setPopulateResultValue(true);
												}else if(keyParam.startsWith(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY)){
													rpPrintPositionList.setPopulateHeaderRepeat(true);
												}else if(keyParam.startsWith(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY)){
													rpPrintPositionList.setPopulateHeaderSelfHeat(true);
												}
												
												
											}
										}

									}

									ReportGeneration reportGeneration = new ReportGeneration();

									status = reportGeneration.processReportGeneration(result, reportProfileManage,getMeterNames(),getMeterProfileData(),maxPageNumber);
								}
							}
						}

					}else{

						Custom1ReportConfigModel custom1ReportCfg = DeviceDataManagerController.getCustom1ReportConfigParsedKey();

						status = processCustom1Report(result, custom1ReportCfg);// sourceTemplateFilePathName, destinationPath, filterTestType,meterSerialNoStartingPosition);
					}
					if(!status){
						ResultNotFound=true;
						ReportsFailed = "Custom1Report Test \n"+ReportsFailed;
					}




				}

				if(ResultNotFound){
					ApplicationLauncher.logger.info("generateCustom1Report: Few reports not exported- Report generation completed. Below few reports not generated due to insufficient data\n"
							+ReportsFailed+ "- Prompted");

					ApplicationLauncher.InformUser("Few reports not exported", 
							"Report generation completed. Below few reports not generated due to insufficient data\n"
									+ReportsFailed, AlertType.WARNING);
				}else{
					
					if(ConstantAppConfig.REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED){
						if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
							String outputReportPath = getReportGeneratedPath();
							status = SaveExcelAsPDFWithPath(outputReportPath);
						}else{
							status = SaveExcelAsPDF();
						}
					}
					
					ApplicationHomeController.update_left_status("Report generation Success",ConstantApp.LEFT_STATUS_DEBUG);

					ApplicationLauncher.logger.info("generateCustom1Report: Export Successful- Report generated successfully- Prompted");

					String outputReportFileName=getReportGeneratedFileName();
					String outputReportPath = getReportGeneratedPath();
					
					if(ConstantAppConfig.REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED){
						if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
							outputReportFileName = outputReportFileName.replace(".xlsx", ".pdf").replace(".xls", ".pdf");
						}
					}
					
					ApplicationLauncher.logger.info("generateCustom1Report: outputReportFileName: "+ outputReportFileName);
					ApplicationLauncher.logger.info("generateCustom1Report: outputReportPath: "+ outputReportPath);

					//ApplicationLauncher.InformUser("Export Successful", "Report generated successfully", AlertType.INFORMATION);
					promptUserToOpenReportOutputFolderPath(outputReportFileName, outputReportPath);
				}
/*				if(ConstantAppConfig.REPORT_CUSTOM_EXPORT_AS_PDF_ENABLED){
					if(ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED){
						String outputReportPath = getReportGeneratedPath();
						status = SaveExcelAsPDFWithPath(outputReportPath);
					}else{
						status = SaveExcelAsPDF();
					}
				}*/
				status=true;
			}else{
				ApplicationLauncher.logger.info("generateCustom1Report: Export Failed- Result not found! - Prompted");
				ApplicationHomeController.update_left_status("Result not found",ConstantApp.LEFT_STATUS_DEBUG);

				ApplicationLauncher.InformUser("Export Failed", "Result not found!!", AlertType.ERROR);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("generateCustom1Report : JSONException:" + e.getMessage());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("generateCustom1Report : Exception:" + e1.getMessage());
		}
	}
	
	public static String getReportGeneratedPath(){
		return reportGeneratedPath;
	}
	public static String getReportGeneratedFileName(){
		return reportGeneratedFileName;
	}
	
	public static void setReportGeneratedPath(String inpReportGeneratedPath ){
		reportGeneratedPath = inpReportGeneratedPath;
	}
	public static void setReportGeneratedFileName(String inpReportGeneratedFileName){
		reportGeneratedFileName = inpReportGeneratedFileName;
	}
	
	
	public void promptUserToOpenReportOutputFolderPath(String outputPdfPathFileName,String outputFolderDocPath){
		ApplicationLauncher.logger.debug("promptUserToOpenReportOutputFolderPath: Entry");
		
		String header = "Do you want to open output folder path?";
		String title = "Report generation success";
		String filePath = outputPdfPathFileName.replace("\\\\", "\\");
		//String outputDocPathLocal = outputDocPath;
		Platform.runLater(() -> {
			String returnedData = GuiUtils.textAreaInputDialogDisplay(header,title,filePath,outputFolderDocPath);
			if(returnedData!=null){
				if(returnedData.equals("OpenOutputFolder")){
					if(!outputFolderDocPath.isEmpty()){
						try {
							Desktop.getDesktop().open(new File(outputFolderDocPath));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.debug("assertGenerateManualTestJsonFileConfig : Exception: " + e.getMessage());
						}
					}
				}
			}
			
		});
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

	public boolean SaveExcelAsPDF(){

		ApplicationLauncher.logger.info("SaveExcelAsPDF: Entry");
		boolean status = false;
		Sleep(1000);
		String PYTHON_ABSOLUTE_PATH = ConstantAppConfig.PYTHON_EXE_LOCATION;
		String script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		ApplicationLauncher.logger.info("SaveExcelAsPDF: script_path1: "+script_path);
		/*		File file = new File(ConstantConfig.reportPythonFilePathName);
		script_path = file.getAbsolutePath();*/
		//script_path = ConstantConfig.reportPythonFilePathName;

		/*		try {
			URL resource = TestReportController.class.getResource(ConstantVersion.pythonFileName);
			File file = Paths.get(resource.toURI()).toFile();
			script_path = file.getAbsolutePath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionA:"+e1.getMessage());

		}*/

		/*		ClassLoader classLoader = getClass().getClassLoader();
		script_path  = classLoader.getResource(ConstantVersion.pythonFileName).getPath();*/

		URL resource = TestReportController.class.getResource(ConstantAppConfig.reportPythonFilePathName);
		try {
			File file = Paths.get(resource.toURI()).toFile();
			script_path = Paths.get(resource.toURI()).toFile().getAbsolutePath();  
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionA:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionB:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} // return a file


		ApplicationLauncher.logger.info("SaveExcelAsPDF: script_path2: "+script_path);
		ApplicationLauncher.logger.info("SaveExcelAsPDF: ConstantReport.SAVE_FILE_LOCATION: "+ConstantReport.SAVE_FILE_LOCATION);
		String ReportPath = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);

		ApplicationLauncher.logger.info("SaveExcelAsPDF: ReportPath:"+ReportPath);
		ReportPath = ReportPath.replace("\\\\", "\\");
		ReportPath = ReportPath.replace("\\\\", "\\");
		//ReportPath = ReportPath.replace("\\", "\\\\");
		ApplicationLauncher.logger.info("SaveExcelAsPDF: ReportPath2:"+ReportPath);
		ApplicationLauncher.logger.info("SaveExcelAsPDF: CONSOLIDATED_PDF_REPORT_FILE_NAME:"+ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		String outputFileName = getConsolidateFileNamePath(ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		//outputFileName = outputFileName.replace(" ", "_");
		ApplicationLauncher.logger.debug("SaveExcelAsPDF: outputFileName:"+outputFileName);
		File pyExecFile = new File(PYTHON_ABSOLUTE_PATH);
		if(pyExecFile.isFile() ) { 
			// do something
			File pyScriptFile = new File(script_path);
			if(pyScriptFile.isFile() ) { 
				//String Command = PYTHON_ABSOLUTE_PATH + " " + script_path + " \"" + ReportPath + "\\\" \"" + outputFileName + "\"";
				String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" \"" + ReportPath + "\" \"" + outputFileName + "\"";
				//String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" \"" + ReportPath + "\" " + outputFileName;
				ApplicationLauncher.logger.debug("SaveExcelAsPDF: Command: "+Command);
				try {
					Process p = Runtime.getRuntime().exec(Command);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String pythonOutput = "";
					//while ((pythonOutput = in.readLine()) != null) {
					pythonOutput = in.readLine();

					ApplicationLauncher.logger.info("SaveExcelAsPDF: pythonOutput: "+pythonOutput);
					if(pythonOutput == null){

						ApplicationLauncher.logger.info("SaveExcelAsPDF: pythonOutput: null:  "+pythonOutput);
						return false;
					}




				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("SaveExcelAsPDF: Exception2:"+e.getMessage());
					return false;
				}
			}else{
				ApplicationLauncher.logger.info("SaveExcelAsPDF: Python Script file does not exist in path :"+script_path);
				ApplicationLauncher.InformUser("Error P201", "PDF generation failed due to invalid script path:\n"+script_path, AlertType.ERROR);
				return false;
			}
		}else{
			ApplicationLauncher.logger.info("SaveExcelAsPDF: Python execution file does not exist in path :"+PYTHON_ABSOLUTE_PATH);
			ApplicationLauncher.InformUser("Error P202", "PDF generation failed due to invalid python executable path:\n"+PYTHON_ABSOLUTE_PATH, AlertType.ERROR);
			return false;
		}


		ApplicationLauncher.logger.info("SaveExcelAsPDF: Exit");
		return status;

	}
	
	
	
	public boolean SaveExcelAsPDFWithPath(String outputReportPath){

		ApplicationLauncher.logger.info("SaveExcelAsPDFWithPath: Entry");
		boolean status = false;
		Sleep(1000);
		String PYTHON_ABSOLUTE_PATH = ConstantAppConfig.PYTHON_EXE_LOCATION;
		String script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		ApplicationLauncher.logger.info("SaveExcelAsPDF: script_path1: "+script_path);
		/*		File file = new File(ConstantConfig.reportPythonFilePathName);
		script_path = file.getAbsolutePath();*/
		//script_path = ConstantConfig.reportPythonFilePathName;

		/*		try {
			URL resource = TestReportController.class.getResource(ConstantVersion.pythonFileName);
			File file = Paths.get(resource.toURI()).toFile();
			script_path = file.getAbsolutePath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionA:"+e1.getMessage());

		}*/

		/*		ClassLoader classLoader = getClass().getClassLoader();
		script_path  = classLoader.getResource(ConstantVersion.pythonFileName).getPath();*/

		URL resource = TestReportController.class.getResource(ConstantAppConfig.reportPythonFilePathName);
		try {
			File file = Paths.get(resource.toURI()).toFile();
			script_path = Paths.get(resource.toURI()).toFile().getAbsolutePath();  
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionA:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("SaveExcelAsPDF: ExceptionB:"+e1.getMessage());
			script_path = ConstantAppConfig.PYTHON_SCRIPT_LOCATION;
		} // return a file


		ApplicationLauncher.logger.info("SaveExcelAsPDF: script_path2: "+script_path);
		//ApplicationLauncher.logger.info("SaveExcelAsPDF: ConstantReport.SAVE_FILE_LOCATION: "+ConstantReport.SAVE_FILE_LOCATION);
		String ReportPath = outputReportPath;//getSaveFilePathV2(outputReportPath);

		ApplicationLauncher.logger.info("SaveExcelAsPDF: ReportPath:"+ReportPath);
		ReportPath = ReportPath.replace("\\\\", "\\");
		ReportPath = ReportPath.replace("\\\\", "\\");
		//ReportPath = ReportPath.replace("\\", "\\\\");
		ApplicationLauncher.logger.info("SaveExcelAsPDF: ReportPath2:"+ReportPath);
		ApplicationLauncher.logger.info("SaveExcelAsPDF: CONSOLIDATED_PDF_REPORT_FILE_NAME:"+ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		String outputFileName = getConsolidateFileNamePath(ConstantReport.CONSOLIDATED_PDF_REPORT_FILE_NAME);
		//outputFileName = outputFileName.replace(" ", "_");
		ApplicationLauncher.logger.debug("SaveExcelAsPDF: outputFileName:"+outputFileName);
		File pyExecFile = new File(PYTHON_ABSOLUTE_PATH);
		if(pyExecFile.isFile() ) { 
			// do something
			File pyScriptFile = new File(script_path);
			if(pyScriptFile.isFile() ) { 
				//String Command = PYTHON_ABSOLUTE_PATH + " " + script_path + " \"" + ReportPath + "\\\" \"" + outputFileName + "\"";
				//String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" \"" + ReportPath + "\" \"" + outputFileName + "\"";
				if(ReportPath.contains(" ")) {
					//ReportPath = "\"" + ReportPath + "\"";
					ApplicationLauncher.logger.debug("SaveExcelAsPDF: ReportPath1:"+ReportPath);
				}
				
				if(outputFileName.contains(" ")) {
					//outputFileName = "\"" + outputFileName + "\"";
					ApplicationLauncher.logger.debug("SaveExcelAsPDF: outputFileName1:"+outputFileName);
				}
				String Command = PYTHON_ABSOLUTE_PATH + " \"" + script_path + "\" " + ReportPath + " " + outputFileName ;//+ "\"";
				
				String args[] = { PYTHON_ABSOLUTE_PATH,  script_path , ReportPath, outputFileName};
				//Runtime.getRuntime().exec(args);
				
				ApplicationLauncher.logger.debug("SaveExcelAsPDF: Command: "+Command);
				ApplicationLauncher.logger.debug("SaveExcelAsPDF: args: "+Arrays.asList(args));
				try {
					//Process p = Runtime.getRuntime().exec(Command);
					Process p = Runtime.getRuntime().exec(args);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String pythonOutput = "";
					//while ((pythonOutput = in.readLine()) != null) {
					pythonOutput = in.readLine();

					ApplicationLauncher.logger.info("SaveExcelAsPDF: pythonOutput: "+pythonOutput);
					if(pythonOutput == null){

						ApplicationLauncher.logger.info("SaveExcelAsPDF: pythonOutput: null:  "+pythonOutput);
						return false;
					}




				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("SaveExcelAsPDF: Exception2:"+e.getMessage());
					return false;
				}
			}else{
				ApplicationLauncher.logger.info("SaveExcelAsPDF: Python Script file does not exist in path :"+script_path);
				ApplicationLauncher.InformUser("Error P201", "PDF generation failed due to invalid script path:\n"+script_path, AlertType.ERROR);
				return false;
			}
		}else{
			ApplicationLauncher.logger.info("SaveExcelAsPDF: Python execution file does not exist in path :"+PYTHON_ABSOLUTE_PATH);
			ApplicationLauncher.InformUser("Error P202", "PDF generation failed due to invalid python executable path:\n"+PYTHON_ABSOLUTE_PATH, AlertType.ERROR);
			return false;
		}


		ApplicationLauncher.logger.info("SaveExcelAsPDFWithPath: Exit");
		return status;

	}



	public void exportAllResultTrigger() {
		ApplicationLauncher.logger.info("exportAllResultTrigger: Invoked:");
		AllResultToExcelTimer = new Timer();
		AllResultToExcelTimer.schedule(new exportAllResult(),100);// 1000);

	}

	class exportAllResult extends TimerTask {
		public void run() {
			try {
				exportAllResultToExcel();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("exportAllResult: IOException:"+e.getMessage());
			}
			AllResultToExcelTimer.cancel();
		}
	}

	public void exportAllResultToExcel() throws IOException{

		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.info("exportAllResultToExcel: project_name: "+project_name);
		if(project_name != null){
			ApplicationHomeController.update_left_status("Exporting...",ConstantApp.LEFT_STATUS_DEBUG);

			Workbook workbook = new HSSFWorkbook();
			Sheet spreadsheet = workbook.createSheet("Sheet1");

			Row row = spreadsheet.createRow(0);

			for (int j = 0; j < result_table_view.getColumns().size(); j++) {
				row.createCell(j).setCellValue(result_table_view.getColumns().get(j).getText());
			}

			for (int i = 0; i < result_table_view.getItems().size(); i++) {
				row = spreadsheet.createRow(i + 1);
				for (int j = 0; j < result_table_view.getColumns().size(); j++) {
					if(result_table_view.getColumns().get(j).getCellData(i) != null) { 
						row.createCell(j).setCellValue(result_table_view.getColumns().get(j).getCellData(i).toString()); 
					}
					else {
						row.createCell(j).setCellValue("");
					}   
				}
			}

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");  


			String fileName = ConstantReport.ALL_PROJECT_REPORT_FILENAME;
			ApplicationLauncher.logger.info("exportAllResultToExcel : fileName:" + fileName);

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);

			FileOutputStream fileOut = new FileOutputStream(file_path+fileName);
			workbook.write(fileOut);
			fileOut.close();
			ApplicationHomeController.update_left_status(fileName +" Exported",ConstantApp.LEFT_STATUS_DEBUG);

			ApplicationLauncher.logger.info("exportAllResultToExcel: Export Successful- " +fileName+" generated successfully- Prompted");

			ApplicationLauncher.InformUser("Export Success", fileName +" exported successfully", AlertType.INFORMATION);
		}else{
			ApplicationLauncher.logger.info("exportAllResultToExcel: No project selected- No Project is selected - Prompted");
			ApplicationLauncher.InformUser("No project selected", "No Project selected", AlertType.ERROR);
		}
	}

	public JSONObject GetResultsFromDB(String datatype){
		ApplicationLauncher.logger.debug("GetResultsFromDB: Entry");

		JSONObject result = new JSONObject();
		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.debug("GetResultsFromDB: project_name: "+ project_name);
		/*		ArrayList<Long> from_to_epoch = getStartEndEpochTime(project_name);
		long from_epoch = from_to_epoch.get(0);
		long to_epoch = from_to_epoch.get(1); 
		String[] test_params = project_name.split("_");
		project_name = test_params[0];*/
		//result = MySQL_Controller.sp_getresult_data(from_epoch, to_epoch, project_name,datatype);vhjvjh
		//result = MySQL_Controller.sp_get_completed_result_data(getResultFilterMctNctMode(),ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE,getSelectedDeploymentID());
		result = MySQL_Controller.sp_get_completed_result_data(getResultFilterMctNctMode(),datatype,getSelectedDeploymentID());

		ApplicationLauncher.logger.debug("GetResultsFromDB:"+result);
		return result;
	}

	public JSONArray GetMeterNamesFromDB(){
		JSONObject meter_names = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_DEVICE_NAME);
		JSONArray json_arr = new JSONArray();
		try {
			json_arr = meter_names.getJSONArray("Results");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("GetMeterNamesFromDB: JSONException:"+e.getMessage());
		}
		return json_arr;
	}

	public boolean checkParams(String testtype){
		ApplicationLauncher.logger.debug("checkParams: testtype: "+testtype);
		boolean status = false;
		if(checkFileLocation(testtype)){
			if(checkHeader(testtype)){
				if(checkExcel(testtype)){
					status = true;
				}else{
					ApplicationLauncher.logger.info("checkParams: Report excel configuration failed for "+testtype);
					ApplicationLauncher.InformUser("Report configuration missing", "Report excel configuration failed for "+testtype, AlertType.ERROR);

				}
			}else{
				ApplicationLauncher.logger.info("checkParams: Report header configuration failed for "+testtype);
				ApplicationLauncher.InformUser("Report configuration missing", "Report header configuration failed for "+testtype, AlertType.ERROR);

			}
		}else{
			ApplicationLauncher.logger.info("checkParams: Report File Location configuration failed for "+testtype);
			ApplicationLauncher.InformUser("Report configuration missing", "Report File Location configuration failed for "+testtype, AlertType.ERROR);
		}
		return status;
	}

	public boolean checkFileLocation(String testtype){
		boolean status = false;
		switch(testtype){
		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			if(!ConstantReport.FREQ_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			if(!ConstantReport.VV_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			if(!ConstantReport.REP_TEMP_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			if(!ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			if(!ConstantReport.ACC_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			if(!ConstantReport.RPS_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			if(!ConstantReport.HARM_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			if(!ConstantReport.VU_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			if(!ConstantReport.CONST_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			if(!ConstantReport.CREEP_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			if(!ConstantReport.STA_TEMPL_FILE_LOCATION.isEmpty()){
				if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
					status = true;
				}
			}
			break;

			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				if(!ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION.isEmpty()){
					if(!ConstantReport.SAVE_FILE_LOCATION.isEmpty()){
						status = true;
					}
				}
			}
			break;
		default:
			break;
		}
		return status;
	}

	public boolean checkHeader(String testtype){
		boolean status = false;
		switch(testtype){
		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			if(!ConstantReport.FREQ_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.FREQ_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.FREQ_TEMPL_PFS.isEmpty()){
						if(!ConstantReport.FREQ_TEMPL_FREQUENCIES.isEmpty()){
							if(!ConstantReport.FREQ_TEMPL_DEFAULT_FREQ.isEmpty()){
								status = true;
							}
						}
					}
				}
			}
			break;

			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			if(!ConstantReport.VV_TEMPL_VOLTS.isEmpty()){
				if(!ConstantReport.VV_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.VV_TEMPL_PFS.isEmpty()){
						if(!ConstantReport.VV_TEMPL_DEFAULT_VOLT.isEmpty()){
							status = true;
						}
					}
				}
			}
			break;

			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			if(!ConstantReport.REP_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.REP_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.REP_TEMPL_PF.isEmpty()){
						if(ConstantReport.REP_TEMPL_NO_OF_TESTS != 0){
							status = true;
						}
					}
				}
			}
			break;

			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			if(!ConstantReport.SELF_HEAT_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.SELF_HEAT_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.SELF_HEAT_TEMPL_PFS.isEmpty()){
						if(ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS != 0){
							status = true;
						}
					}
				}
			}
			break;

			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			if(!ConstantReport.ACC_TEMPL_VOLT.isEmpty()){
				if(!ConstantReport.ACC_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.ACC_TEMPL_PFS.isEmpty()){
						//if(!ReportProperty.ACC_TEMPL_DEFAULT_CURRENT.isEmpty()){
						status = true;
						//}
					}
				}
			}
			break;

			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			if(!ConstantReport.RPS_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.RPS_TEMPL_CURRENT.isEmpty()){
					if(!ConstantReport.RPS_TEMPL_PF.isEmpty()){
						status = true;
					}
				}
			}
			break;

			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			if(!ConstantReport.HARM_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.HARM_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.HARM_TEMPL_PF.isEmpty()){
						status = true;
					}
				}
			}
			break;

			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			if(!ConstantReport.VU_TEMPL_VOLTAGES.isEmpty()){
				if(!ConstantReport.VU_TEMPL_CURRENT.isEmpty()){
					if(!ConstantReport.VU_TEMPL_PF.isEmpty()){
						if(!ConstantReport.VU_TEMPL_DEF_VOLT.isEmpty()){
							status = true;
						}
					}
				}
			}
			break;

			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
			if(!ConstantReport.CONST_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.CONST_TEMPL_CURRENT.isEmpty()){
					if(!ConstantReport.CONST_TEMPL_PF.isEmpty()){
						if(!ConstantReport.CONST_TEMPL_POWER.isEmpty()){
							status = true;
						}
					}
				}
			}
			break;

			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:
			if(!ConstantReport.CREEP_TEMPL_VOLTAGE.isEmpty()){
				status = true;
			}
			break;

			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:
			if(!ConstantReport.STA_TEMPL_VOLTAGE.isEmpty()){
				if(!ConstantReport.STA_TEMPL_CURRENT.isEmpty()){
					status = true;
				}
			}
			break;

			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				if(!ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS.isEmpty()){
					if(!ConstantReport.UNBALANCED_LOAD_TEMPL_PFS.isEmpty()){
						status = true;
					}
				}
			}
			break;
		default:
			break;
		}
		return status;
	}

	public boolean checkExcel(String testtype){
		boolean status = false;
		switch(testtype){
		//case "InfluenceFreq":
		case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
			if(!ConstantReport.FREQ_TEMPL_ROWS.isEmpty()){
				if(!ConstantReport.FREQ_TEMPL_METER_COLS.isEmpty()){
					if(!ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS.isEmpty()){
						status = true;
					}
				}
			}
			break;

			//case "InfluenceVolt":
		case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
			if(!ConstantReport.VV_TEMPL_ROWS.isEmpty()){
				if(!ConstantReport.VV_TEMPL_METER_COLS.isEmpty()){
					if(!ConstantReport.VV_TEMPL_DEF_VOLT_COLS.isEmpty()){
						status = true;
					}
				}
			}
			break;

			//case "Repeatability":
		case ConstantApp.TEST_PROFILE_REPEATABILITY:
			if(!ConstantReport.REP_TEMPL_ROWS.isEmpty()){
				if(!ConstantReport.REP_TEMPL_METER_COLS.isEmpty()){
					status = true;

				}
			}
			break;

			//case "SelfHeating":
		case ConstantApp.TEST_PROFILE_SELF_HEATING:
			if(!ConstantReport.SELF_HEAT_TEMPL_ROWS.isEmpty()){
				if(!ConstantReport.SELF_HEAT_TEMPL_METER_COLS.isEmpty()){
					if(!ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.isEmpty()){
						status = true;
					}
				}
			}
			break;

			//case "Accuracy":
		case ConstantApp.TEST_PROFILE_ACCURACY:
			if(!ConstantReport.ACC_TEMPL_DEF_I_COLS.isEmpty()){
				status = true;
			}

			break;

			//case "PhaseReversal":
		case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
			if(!ConstantReport.RPS_TEMPL_NORMAL_REV_COL.isEmpty()){
				status = true;
			}

			break;

			//case "InfluenceHarmonic":
		case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
			if(!ConstantReport.HARM_TEMPL_PHASE_COLS.isEmpty()){
				status = true;
			}

			break;

			//case "VoltageUnbalance":
		case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
			status = true;//Gopi...why no check validation?

			break;

			//case "ConstantTest":
		case ConstantApp.TEST_PROFILE_CONSTANT_TEST:

			if(!ConstantReport.CONST_TEMPL_CONST_COLS.isEmpty()){
				status = true;
			}

			break;

			//case "NoLoad":
		case ConstantApp.TEST_PROFILE_NOLOAD:

			if(!ConstantReport.CREEP_TEMPL_CREEP_COLS.isEmpty()){
				status = true;
			}

			break;

			//case "STA":
		case ConstantApp.TEST_PROFILE_STA:

			if(!ConstantReport.STA_TEMPL_STA_COLS.isEmpty()){
				status = true;
			}

			break;

			//case "UnbalancedLoad":
		case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
			if(ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD){
				if(!ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS.isEmpty()){
					if(!ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS.isEmpty()){
						if(!ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.isEmpty()){
							status = true;
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return status;
	}

	public Boolean ExportSelfHeatingReport(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.SELF_HEATING_ALIAS_NAME);
			String volt = ConstantReport.SELF_HEAT_TEMPL_VOLTAGE;
			ArrayList<String> currents = ConstantReport.SELF_HEAT_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.SELF_HEAT_TEMPL_PFS;
			if(hssf_Format){
				status= PopulateSelfHeatResultsHSSF(HSSF_Sheet, result, volt, currents, pfs);
			}else{
				status= PopulateSelfHeatResultsXSSF(XSSF_Sheet, result, volt, currents, pfs);
			}
			file.close();
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists())
			{
				OutputFilePathExist = false;
			}
			/*			FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;

			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportSelfHeatingReport: FileNotFoundException:"+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Self Heating not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Self Heating not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Self Heating. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportSelfHeatingReport: IOException:"+e.getMessage());
		}
		return status;

	}
	
	
	


	public Boolean exportMeterProfileReport(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;

			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.SELF_HEATING_ALIAS_NAME);
			String volt = ConstantReport.SELF_HEAT_TEMPL_VOLTAGE;
			ArrayList<String> currents = ConstantReport.SELF_HEAT_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.SELF_HEAT_TEMPL_PFS;

			ApplicationLauncher.logger.debug("exportMeterProfileReport: getSelectedProjectName: "+ getSelectedProjectName());
			if(hssf_Format){
				//status= populateMeterProfileData(HSSF_Sheet, result, volt, currents, pfs);
			}else{
				status= populateMeterProfileData(XSSF_Sheet, getSelectedProjectName());
			}

			/*			if(hssf_Format){
				status= PopulateSelfHeatResultsHSSF(HSSF_Sheet, result, volt, currents, pfs);
			}else{
				status= PopulateSelfHeatResultsXSSF(XSSF_Sheet, result, volt, currents, pfs);
			}*/
			file.close();
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists())
			{
				OutputFilePathExist = false;
			}

			FileOutputStream out = null;

			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("exportMeterProfileReport: FileNotFoundException:"+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Meter Profile not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Meter Profile not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Meter Profile. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("exportMeterProfileReport: IOException:"+e.getMessage());
		}
		return status;

	}

	public String getSaveFilePath(String file_path){
		ApplicationLauncher.logger.debug("getSaveFilePath: file_path: " + file_path);
		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.debug("getSaveFilePath: project_name: " + project_name);
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		if(test_params.length>2){ // added if "_" is added in the project filename
			project_name = "";
			int i=0;
			while (i<(test_params.length-1)){
				project_name = project_name + test_params[i]+"_";
				i++;
			}
			//project_name.replace((char) (project_name.length() - 1), '-');
			if(project_name.endsWith("_"))
			{
				project_name = project_name.substring(0,project_name.length() - 1);
			}
		}else{
			project_name = test_params[0];
		}
		ApplicationLauncher.logger.debug("getSaveFilePath: project_name2: " + project_name);
		int dateTimeIndex = 1;
		if(test_params.length>2){ // added if "_" is added in the project filename
			dateTimeIndex = test_params.length-1;
			ApplicationLauncher.logger.debug("getSaveFilePath: dateTimeIndex: " + dateTimeIndex);
		}
		String date_time = test_params[dateTimeIndex];
		ApplicationLauncher.logger.debug("data_time1: " + date_time);
		String[] time_msec = date_time.split("\\.");
		ApplicationLauncher.logger.debug("data_time2: " + date_time);
		date_time = time_msec[0];
		ApplicationLauncher.logger.debug("data_time3: " + date_time);
		date_time = date_time.replaceAll("[^0-9]", "");
		ApplicationLauncher.logger.debug("data_time4: " + date_time);
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;
		String first_folder_save_file_path = file_path  + date_time;
		String second_folder_save_file_path = file_path  + date_time + "\\\\" + project_name;
		ApplicationLauncher.logger.debug("getSaveFilePath: save_file_path: " + first_folder_save_file_path);
		File files = new File(first_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}
		files = new File(second_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}
		String save_file_path = file_path + "\\" + date_time + "\\" + project_name + "\\";
		
		return save_file_path;
	}
	
	
	public String getSaveFilePathV2(String file_path){
		ApplicationLauncher.logger.debug("getSaveFilePathV2: file_path: " + file_path);
		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.debug("getSaveFilePath: project_name: " + project_name);
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		if(test_params.length>2){ // added if "_" is added in the project filename
			project_name = "";
			int i=0;
			while (i<(test_params.length-1)){
				project_name = project_name + test_params[i]+"_";
				i++;
			}
			//project_name.replace((char) (project_name.length() - 1), '-');
			if(project_name.endsWith("_"))
			{
				project_name = project_name.substring(0,project_name.length() - 1);
			}
		}else{
			project_name = test_params[0];
		}
		ApplicationLauncher.logger.debug("getSaveFilePath: project_name2: " + project_name);
		int dateTimeIndex = 1;
		if(test_params.length>2){ // added if "_" is added in the project filename
			dateTimeIndex = test_params.length-1;
			ApplicationLauncher.logger.debug("getSaveFilePath: dateTimeIndex: " + dateTimeIndex);
		}
		String date_time = test_params[dateTimeIndex];
		ApplicationLauncher.logger.debug("data_time1: " + date_time);
		String[] time_msec = date_time.split("\\.");
		ApplicationLauncher.logger.debug("data_time2: " + date_time);
		date_time = time_msec[0];
		ApplicationLauncher.logger.debug("data_time3: " + date_time);
		date_time = date_time.replaceAll("[^0-9]", "");
		ApplicationLauncher.logger.debug("data_time4: " + date_time);
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;
		String first_folder_save_file_path = file_path  ;//+ date_time;
		String second_folder_save_file_path = file_path  + "\\\\" + project_name;
		ApplicationLauncher.logger.debug("getSaveFilePath: save_file_path: " + first_folder_save_file_path);
		File files = new File(first_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}
		files = new File(second_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}
		String save_file_path = file_path + "\\" + date_time + "\\" + project_name + "\\";
		
		return save_file_path;
	}

	public String getSaveFileName(String template_file_path){
		String template_name = template_file_path.substring(template_file_path.lastIndexOf("\\")+1);
		ApplicationLauncher.logger.debug("template_name: " + template_name);
		String[] file_ext = template_name.split("\\.");
		String file_wo_extension = file_ext[0];
		ApplicationLauncher.logger.debug("file_wo_extension : " + file_wo_extension);


		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		int dateTimeIndex = 1;
		if(test_params.length>2){
			dateTimeIndex = test_params.length-1;
			ApplicationLauncher.logger.debug("getSaveFileName: dateTimeIndex: " + dateTimeIndex);
		}
		String date_time = test_params[dateTimeIndex];
		ApplicationLauncher.logger.debug("data_time: " + date_time);
		String[] time_msec = date_time.split("\\.");
		date_time = time_msec[0];
		date_time = date_time.replaceAll("[^0-9]", "");
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;

		String file_name = file_wo_extension + "_" + date_time + ".xls";
		ApplicationLauncher.logger.debug("getSaveFileName: file_name: " + file_name);

		return file_name;
	}


	public String getConsolidateFileNamePath(String fileName){
		String project_name = get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		String date_time = test_params[1];
		ApplicationLauncher.logger.debug("data_time: " + date_time);
		String[] time_msec = date_time.split("\\.");
		date_time = time_msec[0];
		date_time = date_time.replaceAll("[^0-9]", "");
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;
		/*String first_folder_save_file_path = file_path  + date_time;
		String second_folder_save_file_path = file_path  + date_time + "\\\\" + project_name;
		ApplicationLauncher.logger.debug("save_file_path: " + first_folder_save_file_path);
		File files = new File(first_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}
		files = new File(second_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("Failed to create multiple directories!");
			}
		}*/
		String save_file_name =  project_name  + "_" + date_time + "_" + fileName;
		return save_file_name;
	}

	public JSONObject FilterResultByTestType(JSONObject result, String test_alias_name){

		//ApplicationLauncher.logger.debug("FilterResultByTestType: input result: "+ result.toString());
		JSONObject filter_json = new JSONObject();
		JSONArray FilteredData = new JSONArray();
		try {
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";

			String TestType = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");

				String[] test_params = test_case_name.split("_");
				TestType = test_params[0];
				if((TestType.contains(test_alias_name))){
					FilteredData.put(result_json);	
				}/*else if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
					if((TestType.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME+test_alias_name))){
						FilteredData.put(result_json);
					}
				}*/
			}
			filter_json.put("No_of_results", FilteredData.length());
			filter_json.put("Results", FilteredData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterResultByTestType: JSONException:"+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("FilterResultByTestType: filter_json: "+ filter_json.toString());
		return filter_json;
	}

	public Boolean PopulateSelfHeatResultsXSSF(XSSFSheet sheet1, JSONObject result, 
			String voltage, ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.info("PopulateSelfHeatResultsXSSF: result: "+result);
		boolean status = false;

		int row_pos = 0;
		int meter_col = 0;
		int init_col = 0;
		int col_range = 0;
		int reading_no = 1;
		JSONArray filter_result = new JSONArray();
		String filter_reading_no ="";
		for(int i=0; i<currents.size(); i++){
			try{
				row_pos = ConstantReport.SELF_HEAT_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.SELF_HEAT_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateSelfHeatResultsXSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Selfheating excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				status = false;
				return status;

			}
			for(int j=0; j<currents.size();j++){
				filter_result = FilterSelfHeatRepResults(voltage, currents.get(0), pfs.get(i),"1",result );
				if(filter_result.length() != 0){
					ApplicationLauncher.logger.debug("PopulateSelfHeatResultsXSSF: filter_result " + filter_result);
					FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
					status= true;
					break;
				}
			}
			for(int j=0; j<pfs.size(); j++){
				init_col = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j);
				col_range = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j) + ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS;
				reading_no = 1;
				for(int k= init_col; k<col_range; k++){
					filter_reading_no = Integer.toString(reading_no);
					filter_result = FilterSelfHeatRepResults(voltage, currents.get(i), pfs.get(j),filter_reading_no,result );
					ApplicationLauncher.logger.debug("PopulateSelfHeatResultsXSSF :Section1 result: "+filter_result);
					FillErrorValueXSSF(sheet1, filter_result, row_pos,k,meter_col);	
					reading_no++;
				}   
			}		

		}

		return status;
	}

	public Boolean PopulateSelfHeatResultsHSSF(HSSFSheet sheet1, JSONObject result, 
			String voltage, ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.info("PopulateSelfHeatResultsHSSF: result: "+result);
		boolean status = false;

		int row_pos = 0;
		int meter_col = 0;
		int init_col = 0;
		int col_range = 0;
		int reading_no = 1;
		JSONArray filter_result = new JSONArray();
		String filter_reading_no ="";
		for(int i=0; i<currents.size(); i++){
			try{
				row_pos = ConstantReport.SELF_HEAT_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.SELF_HEAT_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateSelfHeatResultsHSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Selfheating excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				status = false;
				return status;

			}
			for(int j=0; j<currents.size();j++){
				filter_result = FilterSelfHeatRepResults(voltage, currents.get(0), pfs.get(i),"1",result );
				if(filter_result.length() != 0){
					ApplicationLauncher.logger.debug("PopulateSelfHeatResultsHSSF: filter_result " + filter_result);
					FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
					status= true;
					break;
				}
			}
			for(int j=0; j<pfs.size(); j++){
				init_col = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j);
				col_range = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j) + ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS;
				reading_no = 1;
				for(int k= init_col; k<col_range; k++){
					filter_reading_no = Integer.toString(reading_no);
					filter_result = FilterSelfHeatRepResults(voltage, currents.get(i), pfs.get(j),filter_reading_no,result );
					ApplicationLauncher.logger.debug("PopulateSelfHeatResultsHSSF :Section1 result: "+filter_result);
					FillErrorValueHSSF(sheet1, filter_result, row_pos,k,meter_col);	
					reading_no++;
				}   
			}		

		}

		return status;
	}





	/*	public Boolean populateMeterProfileData(XSSFSheet sheet1, JSONObject result, 
			String voltage, ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.info("populateMeterProfileData: result: "+result);
		boolean status = false;

		int row_pos = 0;
		int meter_col = 0;
		int init_col = 0;
		int col_range = 0;
		int reading_no = 1;
		JSONArray filter_result = new JSONArray();
		String filter_reading_no ="";
		for(int i=0; i<currents.size(); i++){
			try{
			row_pos = ConstantReport.SELF_HEAT_TEMPL_ROWS.get(i);
			meter_col = ConstantReport.SELF_HEAT_TEMPL_METER_COLS.get(i);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("populateMeterProfileData: Exception:"+e.getMessage());
			ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Selfheating excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
			status = false;
			return status;

		}
			for(int j=0; j<currents.size();j++){
				filter_result = FilterSelfHeatRepResults(voltage, currents.get(0), pfs.get(i),"1",result );
				if(filter_result.length() != 0){
					ApplicationLauncher.logger.debug("populateMeterProfileData: filter_result " + filter_result);
					FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
					status= true;
					break;
				}
			}
			for(int j=0; j<pfs.size(); j++){
				init_col = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j);
				col_range = ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.get(j) + ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS;
				reading_no = 1;
				for(int k= init_col; k<col_range; k++){
					filter_reading_no = Integer.toString(reading_no);
					filter_result = FilterSelfHeatRepResults(voltage, currents.get(i), pfs.get(j),filter_reading_no,result );
					ApplicationLauncher.logger.debug("populateMeterProfileData :Section1 result: "+filter_result);
					FillErrorValueXSSF(sheet1, filter_result, row_pos,k,meter_col);	
					reading_no++;
				}   
			}		

		}

		return status;
	}*/


	public Boolean populateMeterProfileData(XSSFSheet sheet1, String projectName){


		ApplicationLauncher.logger.debug("populateMeterProfileData: Entry ");

		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);  
		JSONObject modeldata = getMeterProfileData();
		boolean status = true;

		status = meterProfileReportUpdateFrequency(sheet1,modeldata);

		if(status){
			status = meterProfileReportUpdateCustomerName(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status1 : " + status);
		if(status){
			status = meterProfileReportUpdateMeterModel(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status2 : " + status);
		if(status){
			status = meterProfileReportUpdateMeterType(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status3 : " + status);
		if(status){
			status = meterProfileReportUpdateMeterClass(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status4 : " + status);
		if(status){
			status = meterProfileReportUpdateBasicCurrent(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status5 : " + status);
		if(status){
			status = meterProfileReportUpdateMaxCurrent(sheet1,modeldata);	
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status6 : " + status);
		if(status){
			status = meterProfileReportUpdateRatedVoltage(sheet1,modeldata);	
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status7 : " + status);
		if(status){
			status = meterProfileReportUpdateNoOfImpulses(sheet1,modeldata);	
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status8 : " + status);
		if(status){
			status = meterProfileReportUpdateCtType(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status9 : " + status);
		if(status){
			status = meterProfileReportUpdateCtRatio(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status10 : " + status);
		if(status){
			status = meterProfileReportUpdatePtRatio(sheet1,modeldata);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status11 : " + status);
		if(status){
			status = meterProfileReportUpdateReportSerialNo(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status12 : " + status);
		if(status){
			status = meterProfileReportUpdateNoOfPages(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status13 : " + status);
		if(status){
			status = meterProfileReportUpdatePresentPageNo(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status14 : " + status);
		if(status){
			status = meterProfileReportUpdateExecutedDate(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status15 : " + status);
		if(status){
			status = meterProfileReportUpdateExecutedTime(sheet1);	
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status16 : " + status);
		if(status){
			status = meterProfileReportUpdateGeneratedDate(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status17 : " + status);
		if(status){
			status = meterProfileReportUpdateGeneratedTime(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status18 : " + status);
		if(status){
			status = meterProfileReportUpdateMeterSerialNo(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status19 : " + status);
		if(status){
			status = meterProfileReportUpdateTesterName(sheet1);
		}
		ApplicationLauncher.logger.debug("populateMeterProfileData: Status20 : " + status);

		return status;

	}

	public boolean meterProfileReportUpdateCtType(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateCtType: Entry");

		boolean status = false;
		try {
			boolean populateCtType = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayCtType();
			if(populateCtType){
				String ctTypeCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getCtTypeCell();

				try {
					String ctTypeData = modeldata.getString("ct_type");
					status = FillReportDataColumnXSSF(sheet1, ctTypeData,ctTypeCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateCtType: ct_type: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateCtType: Exception2: " + e.getMessage());
		}
		return status;
	}	

	public boolean meterProfileReportUpdateRatedVoltage(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateCtRatio: Entry");

		boolean status = false;
		try {
			boolean populateRatedVoltage = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayRatedVoltage();
			if(populateRatedVoltage){
				String ratedVoltageCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getRatedVoltageCell();

				try {
					String ratedVoltageData = modeldata.getString("rated_voltage_vd");
					status = FillReportDataColumnXSSF(sheet1, ratedVoltageData,ratedVoltageCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateRatedVoltage: rated_voltage_vd: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateRatedVoltage: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateMaxCurrent(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMaxCurrent: Entry");

		boolean status = false;
		try {
			boolean populateMaxCurrent = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMaxCurrent();
			if(populateMaxCurrent){
				String maxCurrentCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMaxCurrentCell();

				try {
					String maxCurrentData = modeldata.getString("max_current_imax");
					status = FillReportDataColumnXSSF(sheet1, maxCurrentData,maxCurrentCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMaxCurrent: max_current_imax: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMaxCurrent: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateBasicCurrent(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateBasicCurrent: Entry");

		boolean status = false;
		try {
			boolean populateBaseCurrent = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayBasicCurrent();
			if(populateBaseCurrent){
				String basicCurrentCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getBasicCurrentCell();

				try {
					String baseCurrentData = modeldata.getString("basic_current_ib");
					status = FillReportDataColumnXSSF(sheet1, baseCurrentData,basicCurrentCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateBasicCurrent: basic_current_ib: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateBasicCurrent: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateMeterClass(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterClass: Entry");

		boolean status = false;
		try {
			boolean populateClass = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMeterClass();
			if(populateClass){
				String classCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMeterClassCell();

				try {
					String classData = modeldata.getString("model_class");
					status = FillReportDataColumnXSSF(sheet1, classData,classCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterClass: model_class: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterClass: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateMeterType(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterType: Entry");

		boolean status = false;
		try {
			boolean populateMeterType = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMeterType();
			if(populateMeterType){
				String meterTypeCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMeterTypeCell();

				try {
					String modelTypeData = modeldata.getString("model_type");
					status = FillReportDataColumnXSSF(sheet1, modelTypeData,meterTypeCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterType: model_type: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterType: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateMeterModel(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterModel: Entry");

		boolean status = false;
		try {
			boolean populateMeterModel = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMeterModelName();
			if(populateMeterModel){
				String modelNameCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMeterModelNameCell();

				try {
					String modelNameData = modeldata.getString("model_name");
					status = FillReportDataColumnXSSF(sheet1, modelNameData,modelNameCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterModel: model_name: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterModel: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateCustomerName(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateCustomerName: Entry");

		boolean status = false;
		try {
			boolean populateCustomerName = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayCustomerName();
			if(populateCustomerName){
				String customerNameCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getCustomerNameCell();


				try {
					String customerNameData = modeldata.getString("customer_name");
					status = FillReportDataColumnXSSF(sheet1, customerNameData,customerNameCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateCustomerName: customer_name: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateCustomerName: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateFrequency(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateFrequency: Entry");

		boolean status = false;
		try {
			boolean populateFreq = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayFrequency();
			if(populateFreq){
				String freqCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getFrequencyCell();



				try {
					String freqData = modeldata.getString("frequency");
					status = FillReportDataColumnXSSF(sheet1, freqData,freqCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					ApplicationLauncher.logger.error("meterProfileReportUpdateFrequency: frequency: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateFrequency: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean meterProfileReportUpdateCtRatio(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateCtRatio: Entry");

		boolean status = false;
		try {
			boolean populateCtRatio = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayCtRatio();
			if(populateCtRatio){
				String ctRatioCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getCtRatioCell();

				try {
					String ctRatioData = modeldata.getString("ctr_ratio");
					status = FillReportDataColumnXSSF(sheet1, ctRatioData,ctRatioCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateCtRatio: ctr_ratio: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateCtRatio: Exception2: " + e.getMessage());
		}
		return status;
	}	


	public boolean meterProfileReportUpdatePtRatio(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdatePtRatio: Entry");

		boolean status = false;
		try {
			boolean populatePtRatio = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayPtRatio();
			if(populatePtRatio){
				String ptRatioCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getPtRatioCell();

				try {
					String ptRatioData = modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, ptRatioData,ptRatioCell);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdatePtRatio: ptr_ratio: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdatePtRatio: Exception2: " + e.getMessage());
		}
		return status;
	}			


	public boolean meterProfileReportUpdateNoOfPages(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfPages: Entry");

		boolean status = false;
		try {

			boolean populateNoOfPages = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayNoOfPages();
			if(populateNoOfPages){
				String noOfPagesCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getNoOfPagesCell();

				try {
					String noOfPagesData = ConstantAppConfig.REPORT_TOTAL_NO_OF_PAGES;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, noOfPagesData,noOfPagesCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateNoOfPages: noOfPagesData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateNoOfPages: Exception2: " + e.getMessage());
		}
		return status;
	}	

	public boolean meterProfileReportUpdatePresentPageNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdatePresentPageNo: Entry");

		boolean status = false;
		try {
			boolean populatePresentPageNo = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayPageNumber();
			if(populatePresentPageNo){
				String presentPageNoCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getPageNumberCell();

				try {
					String presentPageNoData = ConstantAppConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, presentPageNoData,presentPageNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdatePresentPageNo: presentPageNoData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdatePresentPageNo: Exception2: " + e.getMessage());
		}
		return status;
	}	


	public boolean meterProfileReportUpdateGeneratedDate(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateGeneratedDate: Entry");

		boolean status = false;
		try {
			boolean populateReportGeneratedDate = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayReportGeneratedDate();
			if(populateReportGeneratedDate){
				String reportGeneratedDateCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getReportGeneratedDateCell();

				try {
					String timeStamp = new SimpleDateFormat(ConstantAppConfig.REPORT_DATE_FORMAT).format(Calendar.getInstance().getTime());;
					String reportGeneratedDateData = timeStamp;//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportGeneratedDateData,reportGeneratedDateCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateGeneratedDate: reportGeneratedDateData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateGeneratedDate:  Exception2: " + e.getMessage());
		}
		return status;
	}
	public boolean meterProfileReportUpdateGeneratedTime(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateGeneratedTime: Entry");

		boolean status = false;
		try {
			boolean populateReportGeneratedTime = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayReportGeneratedTime();
			if(populateReportGeneratedTime){
				String reportGeneratedTimeCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getReportGeneratedTimeCell();
				String timeStamp = new SimpleDateFormat(ConstantAppConfig.REPORT_TIME_FORMAT).format(Calendar.getInstance().getTime());;
				try {
					String reportGeneratedTimeData = timeStamp;//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportGeneratedTimeData,reportGeneratedTimeCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateGeneratedTime: reportGeneratedTimeData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateGeneratedTime:  Exception2: " + e.getMessage());
		}
		return status;
	}
	public boolean meterProfileReportUpdateMeterSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateMeterSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateMeterSerialNo = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayMeterSerialNo();
			if(populateMeterSerialNo){
				String meterSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getMeterSerialNoCell();

				try {
					String meterSerialNoData = "";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, meterSerialNoData,meterSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateMeterSerialNo: meterSerialNoData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateMeterSerialNo:  Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateNoOfImpulses(XSSFSheet sheet1, JSONObject modeldata){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateNoOfImpulses: Entry");

		boolean status = false;
		try{
			boolean populateNoOfImpulses = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayNoOfImpulsesPerUnit();
			if(populateNoOfImpulses){
				String noOfImpulsesCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getNoOfImpulsesPerUnitCell();

				try {
					displayDataObj.setDeployedDevicesJson(getSelectedProjectName(),getSelectedDeploymentID());
					displayDataObj.CheckAllMeterConstSame();
					String noOfImpulsesData = modeldata.getString("impulses_per_unit");


					if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
						if (isIndividualMeterReportSelected()) {


							if(!displayDataObj.IsAllMeterConstSame()){
								JSONObject result = displayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
								JSONArray deployed_devices = result.getJSONArray("Devices");
								JSONObject jobj = new JSONObject();
								int m_const = 0;
								String meter_const = "";
								String deviceName = "";
								String rackId = "";
								String deviceRackId = "";
								for(int i=0; i<deployed_devices.length(); i++){
									jobj = deployed_devices.getJSONObject(i);
									m_const = jobj.getInt("meter_const");
									meter_const = Integer.toString(m_const);
									//MeterAddress = "0"+Integer.toString(i+1);
									deviceName = jobj.getString("Device_name");
									rackId = jobj.getString("Rack_ID");
									ApplicationLauncher.logger.info("meterProfileReportUpdateNoOfImpulses : meter_const: "+ meter_const);
									ApplicationLauncher.logger.info("meterProfileReportUpdateNoOfImpulses : deviceName: "+ deviceName);
									ApplicationLauncher.logger.info("meterProfileReportUpdateNoOfImpulses : rackId: "+ rackId);
									deviceRackId = deviceName + "/" + rackId;
									ApplicationLauncher.logger.info("meterProfileReportUpdateNoOfImpulses : deviceRackId: "+ deviceRackId);
									if(getSelectedMeterSerialNo().equals(deviceRackId)){
										noOfImpulsesData = meter_const;
										break;
									}

								}
							}
						}
					}

					ApplicationLauncher.logger.info("meterProfileReportUpdateNoOfImpulses : noOfImpulsesData: "+ noOfImpulsesData);
					status = FillReportDataColumnXSSF(sheet1, noOfImpulsesData,noOfImpulsesCell);


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateNoOfImpulses: impulses_per_unit: JSONException: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateNoOfImpulses:  Exception2: " + e.getMessage());
		}

		return status;


	}

	public boolean meterProfileReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){
				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateTesterName: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateExecutedDate(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateExecutedDate: Entry");

		boolean status = false;
		try {
			boolean populateExecutedDate = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayExecutedDate();
			if(populateExecutedDate){
				String executedDateCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getExecutedDateCell();

				try {
					String executedDateData = getSelectedProjectExecutedDate();//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, executedDateData,executedDateCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateExecutedDate: executedDateData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateExecutedDate: executedDateData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean meterProfileReportUpdateExecutedTime(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("meterProfileReportUpdateExecutedTime: Entry");

		boolean status = false;
		try {
			boolean populateExecutedTime = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayExecutedTime();
			if(populateExecutedTime){
				String executedTimeCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getExecutedTimeCell();

				try {
					String executedTimeData = getSelectedProjectExecutedTime();//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, executedTimeData,executedTimeCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateExecutedTime: executedTimeData: Exception: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateExecutedTime: executedTimeData: Exception2: " + e.getMessage());
		}
		return status;
	}





	public boolean meterProfileReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("meterProfileReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getMeterProfileReport().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("meterProfileReportUpdateTesterName: meterSerialNoData: Exception1: " + e.getMessage());
				}

			}else{
				status = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("meterProfileReportUpdateTesterName: meterSerialNoData: Exception2: " + e.getMessage());
		}
		return status;
	}




	public boolean noLoadReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("noLoadReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("noLoadReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("noLoadReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}



	public boolean noLoadReportUpdateVoltage(XSSFSheet sheet1, String voltagePercentage){

		ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					voltagePercentage = voltagePercentage.replace("U", "");
					String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, voltageData,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("noLoadReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("noLoadReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean noLoadReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("noLoadReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){
				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getNoLoadReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("noLoadReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("noLoadReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean voltageVariationReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("voltageVariationReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getVoltageVariationReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageVariationReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageVariationReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageVariationReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean repeatabilityReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("repeatabilityReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("repeatabilityReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("repeatabilityReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean voltageUnbalanceReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("voltageUnbalanceReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}



	public boolean startingCurrentReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("startingCurrentReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("startingCurrentReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("startingCurrentReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean startingCurrentReportUpdateVoltage(XSSFSheet sheet1, String voltagePercentage){

		ApplicationLauncher.logger.debug("startingCurrentReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					voltagePercentage = voltagePercentage.replace("U", "");
					String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					ApplicationLauncher.logger.debug("startingCurrentReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, voltageData,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("startingCurrentReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("startingCurrentReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean voltageVariationReportUpdateVoltage(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("voltageVariationReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getVoltageVariationReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageVariationReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					//voltagePercentage = voltagePercentage.replace("U", "");
					//String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					//ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, ratedVoltage,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageVariationReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageVariationReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean repeatabilityReportUpdateVoltage(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("repeatabilityReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					//voltagePercentage = voltagePercentage.replace("U", "");
					//String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					//ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, ratedVoltage,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("repeatabilityReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("repeatabilityReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean voltageUnbalanceReportUpdateVoltage(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("voltageUnbalanceReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					//voltagePercentage = voltagePercentage.replace("U", "");
					//String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					//ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, ratedVoltage,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean repeatabilityReportUpdateCurrent(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("repeatabilityReportUpdateCurrent: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateCurrent = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportDisplay().getDisplayCurrent();
			if(populateCurrent){
				String currentCell = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportCellPosition().getCurrentCell();

				try {
					String basicCurrent = modeldata.getString("basic_current_ib");
					//currentPercentage = currentPercentage.replace("Ib", "").replace("ib", "");
					//String currentData = String.format("%.02f", (Float.parseFloat(basicCurrent)*Float.parseFloat(currentPercentage)/100.0f));
					//ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: currentData : " + currentData);
					status = FillReportDataColumnXSSF(sheet1, basicCurrent,currentCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("repeatabilityReportUpdateCurrent: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("repeatabilityReportUpdateCurrent: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean voltageUnbalanceReportUpdateCurrent(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("voltageUnbalanceReportUpdateCurrent: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateCurrent = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportDisplay().getDisplayCurrent();
			if(populateCurrent){
				String currentCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportCellPosition().getCurrentCell();

				try {
					String basicCurrent = modeldata.getString("basic_current_ib");
					//currentPercentage = currentPercentage.replace("Ib", "").replace("ib", "");
					//String currentData = String.format("%.02f", (Float.parseFloat(basicCurrent)*Float.parseFloat(currentPercentage)/100.0f));
					//ApplicationLauncher.logger.debug("noLoadReportUpdateVoltage: currentData : " + currentData);
					status = FillReportDataColumnXSSF(sheet1, basicCurrent,currentCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateCurrent: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateCurrent: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean startingCurrentReportUpdateCurrent(XSSFSheet sheet1, String currentPercentage){

		ApplicationLauncher.logger.debug("startingCurrentReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateCurrent = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportDisplay().getDisplayCurrent();
			if(populateCurrent){
				String currentCell = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportCellPosition().getCurrentCell();

				try {
					String basicCurrent = modeldata.getString("basic_current_ib");
					currentPercentage = currentPercentage.replace("Ib", "").replace("ib", "");
					//String currentData = String.format("%.04f", (Float.parseFloat(basicCurrent)*Float.parseFloat(currentPercentage)/100.0f));
					String currentData = String.format("%.03f", (Float.parseFloat(basicCurrent)*Float.parseFloat(currentPercentage)));
					ApplicationLauncher.logger.debug("startingCurrentReportUpdateVoltage: currentData : " + currentData);
					status = FillReportDataColumnXSSF(sheet1, currentData,currentCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("startingCurrentReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("startingCurrentReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}



	public boolean startingCurrentReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("startingCurrentReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){
				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getStartingCurrentReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("startingCurrentReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("startingCurrentReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean voltageVariationReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("voltageVariationReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){

				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageVariationReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageVariationReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageVariationReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean repeatabiltyReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("repeatabiltyReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){

				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getRepeatabilityReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("repeatabiltyReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("repeatabiltyReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}

	public boolean voltageUnbalanceReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("voltageUnbalanceReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){

				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getVoltageUnbalanceReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("voltageUnbalanceReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean accuracyReportUpdateTesterName(XSSFSheet sheet1){

		ApplicationLauncher.logger.debug("accuracyReportUpdateTesterName: Entry");

		boolean status = false;
		try {
			boolean populateTesterName = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportDisplay().getDisplayTesterName();
			if(populateTesterName){
				String testerNameCell = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportCellPosition().getTesterNameCell();

				try {
					String testerNameData = getSelectedExecutionTesterName();//"TesterName";//ConstantConfig.METER_PROFILE_REPORT_PAGE_NUMBER;//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, testerNameData,testerNameCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("accuracyReportUpdateTesterName: testerNameData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("accuracyReportUpdateTesterName: testerNameData: Exception2: " + e.getMessage());
		}
		return status;
	}



	public boolean accuracyReportUpdateReportSerialNo(XSSFSheet sheet1){
		ApplicationLauncher.logger.debug("accuracyReportUpdateReportSerialNo: Entry");

		boolean status = false;
		try {
			boolean populateReportSerialNo = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportDisplay().getDisplayReportSerialNo();;
			if(populateReportSerialNo){
				String reportSerialNoCell = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportCellPosition().getReportSerialNoCell();

				try {
					String reportSerialNumberData = getSelectedReportSerialNo();//"TestSerialNo";//modeldata.getString("ptr_ratio");
					status = FillReportDataColumnXSSF(sheet1, reportSerialNumberData,reportSerialNoCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("accuracyReportUpdateReportSerialNo: reportSerialNumberData: Exception: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("accuracyReportUpdateReportSerialNo: reportSerialNumberData: Exception2: " + e.getMessage());
		}
		return status;
	}


	public boolean accuracyReportUpdateVoltage(XSSFSheet sheet1, String voltagePercentage){

		ApplicationLauncher.logger.debug("accuracyReportUpdateVoltage: Entry");

		boolean status = false;
		try {
			JSONObject modeldata = getMeterProfileData();
			boolean populateVoltage = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportDisplay().getDisplayVoltage();
			if(populateVoltage){
				String voltageCell = DeviceDataManagerController.getReportConfigParsedData().getAccuracyReportCellPosition().getVoltageCell();

				try {
					String ratedVoltage = modeldata.getString("rated_voltage_vd");
					voltagePercentage = voltagePercentage.replace("U", "");
					String voltageData = String.format("%.02f", (Float.parseFloat(ratedVoltage)*Float.parseFloat(voltagePercentage)/100.0f));
					ApplicationLauncher.logger.debug("accuracyReportUpdateVoltage: voltageData : " + voltageData);
					status = FillReportDataColumnXSSF(sheet1, voltageData,voltageCell);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					ApplicationLauncher.logger.error("accuracyReportUpdateVoltage: voltageData: Exception1: " + e.getMessage());
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ApplicationLauncher.logger.error("accuracyReportUpdateVoltage: voltageData: Exception2: " + e.getMessage());
		}
		return status;
	}



	public static int getRowValueFromCellValue(String cellvalue){
		String str_row = cellvalue.replaceAll("[^0-9]", "");
		int row = Integer.parseInt(str_row)-1;
		return row;
	}



	public static int getColValueFromCellValue(String cellvalue){
		String col = cellvalue.replaceAll("[0-9]", "");

		int col_value = 0;
		char ch = ' ';
		int ascii_value = 0;
		for(int i=0; i<col.length(); i++){
			ch = col.charAt(i);
			ascii_value = (int)ch;
			col_value = (col_value*26) + ascii_value - 64;
		}

		col_value = col_value - 1;
		return col_value;
	}



	public boolean FillReportDataColumnXSSF(XSSFSheet sheet1, String resultData, String cellPosition){
		boolean status = false;
		try {

			int inpRowPosition = getRowValueFromCellValue(cellPosition);
			int column_pos = getColValueFromCellValue(cellPosition);
			ApplicationLauncher.logger.debug("FillReportDataColumnXSSF: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportDataColumnXSSF: Exception2:"+e.getMessage());
			}
			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportDataColumnXSSF: Exception2:"+e.getMessage());
		}

		return status;
	}

	public boolean FillReportDataColumnXSSF_V1_1(XSSFSheet sheet1, String resultData, int row_pos,int column_pos){
		boolean status = false;
		try {

			//int inpRowPosition = getRowValueFromCellValue(cellPosition);
			//int column_pos = getColValueFromCellValue(cellPosition);
			//ApplicationLauncher.logger.debug("FillReportDataColumnXSSF_V1_1: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			//int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;

				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					style.setFont(font);
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillMeterColumnXSSF: Exception FontSetting: " + e.getMessage());

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
			}

			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
		}

		return status;
	}

	public void fillSerialNoColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillSerialNoColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		for(int i = 1; i <= maxValue; i++ ){
			//serialNoCellPosition = row_pos +  column_pos;
			FillReportDataColumnXSSF_V1_1(sheet1, String.valueOf(i),row_pos, column_pos);
			row_pos++;
		}
	}


	public void fillCapacityColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCapacityColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			JSONObject modeldata = getMeterProfileData();
			String ibValue = modeldata.getString("basic_current_ib");
			String iMaxValue = modeldata.getString("max_current_imax");
			String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterCapacityData,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCapacityColumnXSSF: Exception: "+e.getMessage());
		}
	}


	public void fillCustomerReferenceNoColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCustomerReferenceNoColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		String customerRefNo = "";
		try {
			/*			JSONObject modeldata = getMeterProfileData();
			String ibValue = modeldata.getString("basic_current_ib");
			String iMaxValue = modeldata.getString("max_current_imax");
			String meterCapacityData = ibValue +"-" + iMaxValue + "A";*/

			customerRefNo = getSelectedCustomerRefNo();//;fetchCustomerReferenceNo();
			ApplicationLauncher.logger.debug("fillCustomerReferenceNoColumnXSSF: customerRefNo: " + customerRefNo);
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, customerRefNo,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCustomerReferenceNoColumnXSSF: Exception: "+e.getMessage());
		}
	}

	/*	public String fetchCustomerReferenceNo(){// throws  JSONException{
		 //emModelDataList.clear();

		 //PTCT_ModelDataList.clear();
		 boolean status = true;
		 String customerRefNo = "";

		 ApplicationLauncher.logger.info("fetchCustomerReferenceNo: from time: " + spinner_fromtime.getValue());
		 ApplicationLauncher.logger.info("fetchCustomerReferenceNo: to time: " + spinner_totime.getValue());
		 String fromdate = datepicker_fromdate.getValue().toString() +" " + spinner_fromtime.getValue();
		 String todate = datepicker_todate.getValue().toString() + " " + spinner_totime.getValue();

		 //String currentSelectedCustomerName = ref_cmbBox_CustomerName.getSelectionModel().getSelectedItem().toString();
		 //String selectedEquipmentSerialNumber = ref_cmbBox_SerialNumber.getSelectionModel().getSelectedItem().toString();
		 try{
			 if((!fromdate.isEmpty()) && (!todate.isEmpty())){
				 long from_epoch = calcEpoch(fromdate);
				 long to_epoch = calcEpoch(todate);
				 ApplicationLauncher.logger.info("fetchCustomerReferenceNo: from_epoch: " + from_epoch);
				 ApplicationLauncher.logger.info("fetchCustomerReferenceNo: to_epoch: " + to_epoch);
				 JSONObject projectjson = MySQL_Controller.sp_get_executed_project_results(from_epoch, to_epoch);
				 try{
					 JSONArray project_arr = projectjson.getJSONArray("Runs");
					 setProjectRunData(project_arr);
					 JSONObject jobj = new JSONObject();
					 //String end_time = "";
					// String end_epoch_time = "";
					 String deployment_id = "";
					 //String mct_mode_status = "";
					// String nct_mode_status = "";
					// String project_name = "";
					// String testedBy = "";
		 ApplicationLauncher.logger.debug("fetchCustomerReferenceNo: getSelectedDeploymentID(): " + getSelectedDeploymentID());
					 for(int i =0; i<deploymentIdList.size(); i++ ){
						 //try{

							 if(deploymentIdList.get(i).equals(getSelectedDeploymentID())){
								 customerRefNo  = customerRefNoList.get(i);
								 return customerRefNo;
							 }
					 }




		 return customerRefNo;
	}*/


	public void fillMeterTypeColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillMeterTypeColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterType = modeldata.getString("model_type");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterType,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterTypeColumnXSSF: Exception: "+e.getMessage());
		}
	}





	//fillMakeCapacityColumnXSSF

	public List<String> fillMakeCapacityColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();

			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			String ibValue = modeldata.getString("basic_current_ib");
			String iMaxValue = modeldata.getString("max_current_imax");
			String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterMake = arr.getJSONObject(i).getString("meter_make");
				MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF : Filled Index: " + i);
					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}
					//FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMakeCapacityColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMakeCapacityColumnXSSF: col:"+col);

		return col;
	}

	public List<String> fillMakeColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMakeColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillMakeColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMakeColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillMakeColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMakeColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF: col:"+col);

		return col;
	}
	
	public List<String> fillModelNoColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterModelNo = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterModelNo = arr.getJSONObject(i).getString("meter_model_no");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : metemeterModelNorMake: " + meterModelNo);
				ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterModelNo,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillModelNoColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF: col:"+col);

		return col;
	}


	public void fillMeterConstantFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillMeterConstantFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("impulses_per_unit");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterConstantFromMeterProfileColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public void fillPtRatioFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillPtRatioFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("ptr_ratio");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillPtRatioFromMeterProfileColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public void fillCtRatioFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCtRatioFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("ctr_ratio");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCtRatioFromMeterProfileColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public List<String> fillMeterConstantColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterConstant = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterConstant = arr.getJSONObject(i).getString("meter_const");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : meterConstant: " + meterConstant);
				ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterConstantColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF: col:"+col);

		return col;
	}


	public List<String> fillPtRatioColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String ptRatio = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				ptRatio = arr.getJSONObject(i).getString("ptr_ratio");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : ptRatio: " + ptRatio);
				ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, ptRatio,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillPtRatioColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF: col:"+col);

		return col;
	}


	public List<String> fillCtRatioColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String ctRatio = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				ctRatio = arr.getJSONObject(i).getString("ctr_ratio");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : ctRatio: " + ctRatio);
				ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, ctRatio,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCtRatioColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF: col:"+col);

		return col;
	}

	public void replaceDataInReportExcel( XSSFSheet sheet1, String findText, String ReplaceText){

		ApplicationLauncher.logger.debug("replaceDataInReportExcel : Entry");
		ApplicationLauncher.logger.debug("replaceDataInReportExcel : findText: " +findText);
		ApplicationLauncher.logger.debug("replaceDataInReportExcel : ReplaceText: " +ReplaceText);
		String data = "";
		try {
			Iterator<Row> rowIterator = sheet1.rowIterator();
			while(rowIterator.hasNext()){

				try {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						try {
							Cell cell = cellIterator.next();
							data = cell.getStringCellValue();
							// ApplicationLauncher.logger.debug("replaceDataInReportExcel : data: " +data);
							if(data.equals(findText)){
								ApplicationLauncher.logger.debug("replaceDataInReportExcel : updated: ");
								cell.setCellValue(ReplaceText);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception1: "+e.getMessage());
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception2: "+e.getMessage());
				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception3: "+e.getMessage());
		}

		ApplicationLauncher.logger.debug("replaceDataInReportExcel : Exit");
	}

	public List<String> removeRackIdInMeterSerialNoColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();

			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			//String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				//deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : meterMake: " + device_name);
				//ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, device_name,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("removeRackIdInMeterSerialNoColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF: col:"+col);

		return col;
	}


	public List<String> fillMeterOverAllStatusColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : meter_col: "+ meter_col);
		//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : dataCellStartingPosition: "+ dataCellStartingPosition);
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : project_name: "+ getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : getSelectedDeployment_ID: "+ getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(getSelectedProjectName(),getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : arr length: " + arr.length());
			boolean rackIdMatchFoundWithRowHeader = false;
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				rackIdMatchFoundWithRowHeader =  isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode);
				//if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
				if(rackIdMatchFoundWithRowHeader){
					ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/


					for (HashMap.Entry<String,String> hashMeterid : meterDeviceOverAllStatus.entrySet()){
						//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
						//                 ", Value = " + hashMeterid.getValue());
						if(hashMeterid.getKey().equals(deviceWithRackId)){
							//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: deviceWithRackId: " + deviceWithRackId);
							//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: hashMeterid.getValue(): " + hashMeterid.getValue());
							FillReportDataColumnXSSF_V1_1(sheet1, hashMeterid.getValue(),row_pos, column_pos);
							break;
						}
					}
					//FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterOverAllStatusColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: col:"+col);

		return col;
	}


	public ArrayList<String> FillMeterColumnXSSF(XSSFSheet sheet1, JSONArray result, int row_pos, int column_pos){
		ApplicationLauncher.logger.debug("FillMeterColumnXSSF: Entry ");
		int serialNumberCount =0;

		ArrayList<String> uniqueDeviceName= new ArrayList<String>();
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String rack_id = "";
			String device_rack_id = "";

			for(int i=0; i<result.length(); i++){

				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				result_json = result.getJSONObject(i);
				rack_id = result_json.getString("device_name");
				device_rack_id = getDeviceNameWithRackID(rack_id);
				//ApplicationLauncher.logger.info("FillMeterColumnXSSF: rack_id: " + rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF: device_rack_id: " + device_rack_id);
				/*********Added below snippet for unique device rack name on Excel file***********/
				if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
					if (isIndividualMeterReportSelected()) {

						if(getSelectedMeterSerialNo().equals(device_rack_id)){
							if (!uniqueDeviceName .contains(device_rack_id))    {
								uniqueDeviceName .add(device_rack_id);
								column.setCellValue(device_rack_id); 
								serialNumberCount++;
								row_pos++;
							}
						}
					}else {
						if (!uniqueDeviceName .contains(device_rack_id))   {
							uniqueDeviceName .add(device_rack_id);
							column.setCellValue(device_rack_id); 
							serialNumberCount++;
							row_pos++;
						}
					}
				}else {
					if (!uniqueDeviceName .contains(device_rack_id))   {
						uniqueDeviceName .add(device_rack_id);
						column.setCellValue(device_rack_id); 
						serialNumberCount++;
						row_pos++;
					}
				}
				//column.setCellValue(device_rack_id); 
				//row_pos++;
				/*********commented above snippet for unique device rack name on Excel file***********/
				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					style.setFont(font);
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillMeterColumnXSSF: Exception FontSetting: " + e.getMessage());

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillMeterColumnXSSF: JSONException:"+e.getMessage());
		}

		setSerialNumberMaxCount(serialNumberCount);
		return uniqueDeviceName;
	}


	public ArrayList<String> FillMeterColumnXSSF_V2(XSSFSheet sheet1, JSONArray result, int row_pos, int column_pos,
			int maxDutDisplayRequestedPerPage, boolean splitDutDisplayIntoMultiplePage, int presentPageNumberIteration){

		ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: Entry ");
		ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: presentPageNumberIteration: " + presentPageNumberIteration);
		int serialNumberCount =0;
		int incrementedMaxDutDisplayRequestedPerPage = presentPageNumberIteration * maxDutDisplayRequestedPerPage;
		//if ( (serialNumberCount > maxDutDisplayRequestedPerPage) &&

		ArrayList<String> uniqueDeviceName= new ArrayList<String>();
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String rack_id = "";
			String device_rack_id = "";
			boolean updateSpreadSheetAllowed = true;	
			for(int i=0; i<result.length(); i++){

				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				result_json = result.getJSONObject(i);
				rack_id = result_json.getString("device_name");
				device_rack_id = getDeviceNameWithRackID(rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: rack_id: " + rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: device_rack_id: " + device_rack_id);
				/*********Added below snippet for unique device rack name on Excel file***********/
				//if( (serialNumberCount < maxDutDisplayRequestedPerPage)){

				updateSpreadSheetAllowed = true;	
				//|| (maxDutDisplayRequestedPerPage) ){
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: serialNumberCount: " + serialNumberCount);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: maxDutDisplayRequestedPerPage: " + maxDutDisplayRequestedPerPage);
				if (serialNumberCount >= maxDutDisplayRequestedPerPage){
					//	&& (!splitDutDisplayIntoMultiplePage) ){
					if(splitDutDisplayIntoMultiplePage){
						if(getDutMeterDataPreviousPageUpdatedPageNumber()< presentPageNumberIteration){
							//if(getDutMeterDataPreviousPageUpdatedPageNumber()==0){
							if(presentPageNumberIteration==1){	
								//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit1: ");
								setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
								setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
								ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit1: ");
								break;
							}else{
								ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit2: ");
								if	(serialNumberCount >= maxDutDisplayRequestedPerPage) {
									//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit3A: ");
									setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
									setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
									ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit3: ");
									break;
								}
								/*								if	(serialNumberCount > incrementedMaxDutDisplayRequestedPerPage) {
									if(rack_id){
										ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit2: ");
										updateSpreadSheetAllowed = false;
									}
								}*/
							}
						}


					}else{
						//setDutMeterDataPreviousPageLastUpdatedRackId(device_rack_id);
						//setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
						ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit4: ");
						break;
						//updateSpreadSheetAllowed = false;
					}

				}else{
					if(splitDutDisplayIntoMultiplePage){
						//if(getDutMeterDataPreviousPageUpdatedPageNumber()>1){
						/*						if	(serialNumberCount > incrementedMaxDutDisplayRequestedPerPage) {

						}*/
						if(presentPageNumberIteration>1){
							ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit5: ");
							//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit5: rack_id: " + rack_id);
							if(Integer.parseInt(rack_id) < Integer.parseInt(getDutMeterDataPreviousPageLastUpdatedRackId())){
								ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit6: ");
								updateSpreadSheetAllowed = false;
							}else if	(serialNumberCount >= maxDutDisplayRequestedPerPage) {
								//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit3A: ");
								//updateSpreadSheetAllowed = false;
								setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
								setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
								ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit7: ");
								break;
							}
						}
					}
				}

				if(updateSpreadSheetAllowed){	

					if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
						if (isIndividualMeterReportSelected()) {

							if(getSelectedMeterSerialNo().equals(device_rack_id)){
								if (!uniqueDeviceName .contains(device_rack_id))    {
									uniqueDeviceName .add(device_rack_id);
									column.setCellValue(device_rack_id); 
									serialNumberCount++;
									row_pos++;
								}
							}
						}else {
							if (!uniqueDeviceName .contains(device_rack_id))   {
								uniqueDeviceName .add(device_rack_id);
								column.setCellValue(device_rack_id); 
								serialNumberCount++;
								row_pos++;
							}
						}
					}else {
						if (!uniqueDeviceName .contains(device_rack_id))   {
							uniqueDeviceName .add(device_rack_id);
							column.setCellValue(device_rack_id); 
							serialNumberCount++;
							row_pos++;
						}
					}
					//column.setCellValue(device_rack_id); 
					//row_pos++;
					/*********commented above snippet for unique device rack name on Excel file***********/
					CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
					try{
						XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
						//font.setFontHeightInPoints((short)25);
						font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
						font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

						style.setFont(font);
						style.setBorderTop(BorderStyle.THIN);
						style.setBorderRight(BorderStyle.THIN);
						style.setBorderBottom(BorderStyle.THIN);
						style.setBorderLeft(BorderStyle.THIN);
						//style.setBorderBottom(BorderStyle.THIN);
						column.setCellStyle(style);
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("FillMeterColumnXSSF_V2: Exception FontSetting: " + e.getMessage());

					}
				}/*else{
					if(splitDutDisplayIntoMultiplePage){
						setDutMeterDataPreviousPageLastUpdatedRackId(device_rack_id);
						setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
					}
					ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: break hit: ");
					break;
				}*/
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillMeterColumnXSSF_V2: JSONException:"+e.getMessage());
		}

		setSerialNumberMaxCount(serialNumberCount);
		return uniqueDeviceName;
	}


	public void fillMeterColumnWitOutRackId_XSSF(XSSFSheet sheet1, JSONArray result, int row_pos, int column_pos){
		try {
			ApplicationLauncher.logger.debug("fillMeterColumnWitOutRackId_XSSF: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String rack_id = "";
			String device_rack_id = "";
			List<String> uniqueDeviceName= new ArrayList<String>();
			for(int i=0; i<result.length(); i++){

				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				result_json = result.getJSONObject(i);
				rack_id = result_json.getString("device_name");
				device_rack_id = getDeviceNameWithOutRackID(rack_id);
				//ApplicationLauncher.logger.info("fillMeterColumnWitOutRackId_XSSF: rack_id: " + rack_id);
				ApplicationLauncher.logger.info("fillMeterColumnWitOutRackId_XSSF: device_rack_id: " + device_rack_id);
				/*********Added below snippet for unique device rack name on Excel file***********/
				if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
					if (isIndividualMeterReportSelected()) {

						if(getSelectedMeterSerialNo().equals(device_rack_id)){
							if (!uniqueDeviceName .contains(device_rack_id))    {
								uniqueDeviceName .add(device_rack_id);
								column.setCellValue(device_rack_id); 
								row_pos++;
							}
						}
					}else {
						if (!uniqueDeviceName .contains(device_rack_id))   {
							uniqueDeviceName .add(device_rack_id);
							column.setCellValue(device_rack_id); 
							row_pos++;
						}
					}
				}else {
					if (!uniqueDeviceName .contains(device_rack_id))   {
						uniqueDeviceName .add(device_rack_id);
						column.setCellValue(device_rack_id); 
						row_pos++;
					}
				}
				//column.setCellValue(device_rack_id); 
				//row_pos++;
				/*********commented above snippet for unique device rack name on Excel file***********/
				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					style.setFont(font);
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillMeterColumnXSSF: Exception FontSetting: " + e.getMessage());

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterColumnWitOutRackId_XSSF: JSONException:"+e.getMessage());
		}
	}



	public ArrayList<String> getMeterSerialNoList( JSONObject inpResult){

		ApplicationLauncher.logger.debug("getMeterSerialNoList: Entry ");
		ArrayList<String> uniqueDeviceName= new ArrayList<String>();
		try {

			JSONObject result_json = new JSONObject();
			JSONArray result = inpResult.getJSONArray("Results");
			String rack_id = "";
			String device_rack_id = "";

			for(int i=0; i<result.length(); i++){



				result_json = result.getJSONObject(i);
				rack_id = result_json.getString("device_name");
				device_rack_id = getDeviceNameWithRackID(rack_id);
				//ApplicationLauncher.logger.info("getMeterSerialNoList: rack_id: " + rack_id);
				ApplicationLauncher.logger.info("getMeterSerialNoList: device_rack_id: " + device_rack_id);
				/*********Added below snippet for unique device rack name on Excel file***********/
				if(!uniqueDeviceName .contains(device_rack_id)) {
					uniqueDeviceName .add(device_rack_id);
					/*		            column.setCellValue(device_rack_id); 
		            row_pos++;*/
				}
				//column.setCellValue(device_rack_id); 
				//row_pos++;
				/*********commented above snippet for unique device rack name on Excel file***********/
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getMeterSerialNoList: JSONException:"+e.getMessage());
		}

		return uniqueDeviceName;
	}


	public void FillMeterColumnHSSF(HSSFSheet sheet1, JSONArray result, int row_pos, int column_pos){
		try {
			ApplicationLauncher.logger.debug("FillMeterColumnHSSF: Entry ");
			JSONObject result_json = new JSONObject();
			String rack_id = "";
			String device_rack_id = "";
			String test_case_name = "";
			List<String> uniqueDeviceName= new ArrayList<String>();
			for(int i=0; i<result.length(); i++){

				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				result_json = result.getJSONObject(i);
				ApplicationLauncher.logger.info("FillMeterColumnHSSF: result_json: " + result_json);

				rack_id = result_json.getString("device_name");

				ApplicationLauncher.logger.info("FillMeterColumnHSSF: rack_id1: " + rack_id);

				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
					test_case_name  = result_json.getString("test_case_name");
					if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

						rack_id = getRealDeviceIDForExportMode( rack_id);
						device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
					}else{
						device_rack_id = getDeviceNameWithRackID(rack_id);
					}
				}else{
					device_rack_id = getDeviceNameWithRackID(rack_id);
				}

				//device_rack_id = getDeviceNameWithRackID(rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnHSSF: rack_id2: " + device_rack_id);
				/*********Added below snippet for unique device rack name on Excel file***********/
				if(!uniqueDeviceName .contains(device_rack_id)) {
					uniqueDeviceName .add(device_rack_id);
					column.setCellValue(device_rack_id); 
					//ApplicationLauncher.logger.info("FillMeterColumnHSSF: row_pos: " + row_pos);
					row_pos++;
				}
				//column.setCellValue(device_rack_id); 
				//row_pos++;
				/*********commented above snippet for unique device rack name on Excel file***********/
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillMeterColumnHSSF: JSONException:"+e.getMessage());
		}
	}

	/*	public void FillErrorValueXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		ApplicationLauncher.logger.debug("FillErrorValueXSSF: filter_result: " + filter_result);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			if (filter_result.length()>0){
				for(int i=0; i<filter_result.length(); i++){
					Row row = sheet1.getRow(row_pos);
					if(row == null){
						row = sheet1.createRow(row_pos);
					}
					Cell column = row.getCell(column_pos);

					for(int j=0; j<filter_result.length(); j++){
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");
						if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, device_name)){
							//ApplicationLauncher.logger.debug("FillErrorValue: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if (ConstantFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							//ApplicationLauncher.logger.debug("FillErrorValue: device_name: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}

							column.setCellValue(error_value); 	
						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: Exception: " + e1.getMessage());

		}
	}
	 */

	@SuppressWarnings("deprecation")
	public void FillErrorValueXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		//ApplicationLauncher.logger.debug("FillErrorValueXSSF: filter_result: " + filter_result);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			if (filter_result.length()>0){
				//int valuePopulatedInExcelCount = 0;
				//for(int i=0; i<filter_result.length(); i++){
				for(int i=0; i<filter_result.length() || (i <getSerialNumberMaxCount()); i++){	//when result count is less than total meter loaded count - data not populated added logic here 
					XSSFRow row = sheet1.getRow(row_pos);
					if(row == null){
						row = sheet1.createRow(row_pos);
					}
					XSSFCell column = row.getCell(column_pos);

					for(int j=0; j<filter_result.length()  ; j++){
						//	for(int j=0; j<filter_result.length() || (valuePopulatedInExcelCount <filter_result.length()) ; j++){	
						//for(int j=0; valuePopulatedInExcelCount<=filter_result.length(); j++){	
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");
						//ApplicationLauncher.logger.info("FillErrorValueXSSF: device_name:"+device_name);
						//ApplicationLauncher.logger.info("FillErrorValueXSSF: filter_result.length():"+filter_result.length());
						exportMode = false;
						if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
							//exportModeDeviceName = "";
							//test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: test_case_name:"+test_case_name);

							//if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

							exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name2:"+device_name);
							if(!device_name.equals(exportModeDeviceName)){
								exportMode = true;
								device_name= exportModeDeviceName;
							}
							//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							//}
						}
						/*						if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
							test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueXSSF: test_case_name:"+test_case_name);

							if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

								device_name = getRealDeviceIDForExportMode( device_name);
								//ApplicationLauncher.logger.info("FillErrorValueXSSF: device_name2:"+device_name);
								exportMode = true;
								//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							}
						}*/
						//ApplicationLauncher.logger.debug("FillErrorValueXSSF: device_name0: " + device_name);
						//ApplicationLauncher.logger.debug("FillErrorValueXSSF: test1: " );
						if(isRackidFromMeterColSameXSSFV2(sheet1, row_pos, meter_col, device_name,exportMode)){
							//if(isRackidFromMeterColSameExportModeHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){


							//ApplicationLauncher.logger.debug("FillErrorValueXSSF: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if (ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							ApplicationLauncher.logger.debug("FillErrorValueXSSF: device_name: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}
							//column.setCellType(Cell.CELL_TYPE_NUMERIC);
							//column.setCellType(CellType.NUMERIC);
							column.setCellValue(error_value); 	


							CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
							//valuePopulatedInExcelCount++;
							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("FillErrorValueXSSF: test_status1: <"+test_status+">");
								if(test_status.contains("P")){
									/*									XSSFCellStyle style = new XSSFCellStyle(new StylesTable());
								    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
								    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = sheet1.createCellStyle(); 
									/*									XSSFCellStyle curStyle = (XSSFCellStyle) column.getCellStyle();

							        curStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        curStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//XSSFCell curCell = row.getCell(column_pos);//row.getCell(partNumberColumn);
									//CellStyle style = column.getCellStyle();
									/*							        CellStyle style = null ;
							        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//curCell.setCellStyle(curStyle);
									/*									XSSFWorkbook wb = new XSSFWorkbook();
									XSSFCellStyle style = wb.createCellStyle();
									style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
									style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = 
									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										//style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");

											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionP FontSetting: " + e.getMessage());

										}*/
										//column.
										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionP StyleSetting: " + e.getMessage());

									}
									//wb.close();
								}
							}

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_FAIL){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("FillErrorValueXSSF: test_status2: <"+test_status+">");
								if(test_status.contains("F")){


									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										//style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");
											//font.setColor(IndexedColors.WHITE.getIndex());
											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionF FontSetting: " + e.getMessage());

										}*/

										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionF StyleSetting: " + e.getMessage());

									}

								}
							}
							test_status = result_json.getString("test_status");
							validateForMeterOvellFailStatus( test_status,  device_name);
							try{
								XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
								//font.setFontHeightInPoints((short)25);
								font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
								font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

								style.setFont(font);
								style.setBorderTop(BorderStyle.THIN);
								style.setBorderRight(BorderStyle.THIN);
								style.setBorderBottom(BorderStyle.THIN);
								style.setBorderLeft(BorderStyle.THIN);

								//style.setBorderBottom(BorderStyle.THIN);
								column.setCellStyle(style);
							}catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("FillErrorValueXSSF: Exception FontSetting: " + e.getMessage());

							}

						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: Exception: " + e1.getMessage());

		}
	}

	public void validateForMeterOvellFailStatus(String test_status, String rackId){
		//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: rackId: " + rackId);
		//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: filter_result: " + test_status);
		if(ConstantReport.RESULT_STATUS_FAIL.contains((test_status))){
			//ArrayList<String> uniqueMeterSerialNoList = new ArrayList<String>(); 
			//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit1");

			for (HashMap.Entry<String,String> hashMeterid : meterDeviceOverAllStatus.entrySet()){
				//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
				//                 ", Value = " + hashMeterid.getValue());
				//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit2");
				if(hashMeterid.getKey().endsWith("/" + rackId)){
					//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit3");
					updateMeterDeviceOverAllStatus(hashMeterid.getKey(),ConstantReport.REPORT_POPULATE_FAIL);
					break;
				}
			}

		}
	}

	public void fillResultStatusXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		ApplicationLauncher.logger.debug("fillResultStatusXSSF: filter_result: " + filter_result);
		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			if (filter_result.length()>0){
				for(int i=0; i<filter_result.length(); i++){
					XSSFRow row = sheet1.getRow(row_pos);
					if(row == null){
						row = sheet1.createRow(row_pos);
					}
					XSSFCell column = row.getCell(column_pos);

					for(int j=0; j<filter_result.length(); j++){
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");
						//ApplicationLauncher.logger.info("fillResultStatusXSSF: device_name:"+device_name);
						exportMode = false;
						if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
							//exportModeDeviceName = "";
							//test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: test_case_name:"+test_case_name);

							//if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

							exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name2:"+device_name);
							if(!device_name.equals(exportModeDeviceName)){
								exportMode = true;
								device_name= exportModeDeviceName;
							}
							//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							//}
						}
						/*						if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
							test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("fillResultStatusXSSF: test_case_name:"+test_case_name);

							if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

								device_name = getRealDeviceIDForExportMode( device_name);
								//ApplicationLauncher.logger.info("fillResultStatusXSSF: device_name2:"+device_name);
								exportMode = true;
								//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							}
						}*/
						if(isRackidFromMeterColSameXSSFV2(sheet1, row_pos, meter_col, device_name,exportMode)){
							//if(isRackidFromMeterColSameExportModeHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){


							//ApplicationLauncher.logger.debug("fillResultStatusXSSF: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if (ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							ApplicationLauncher.logger.debug("fillResultStatusXSSF: device_name: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}
							test_status = result_json.getString("test_status");
							if(ConstantReport.RESULT_STATUS_PASS.contains((test_status))){
								test_status = ConstantReport.REPORT_POPULATE_PASS;
							}else if(ConstantReport.RESULT_STATUS_FAIL.contains((test_status))){
								test_status = ConstantReport.REPORT_POPULATE_FAIL;
							}
							column.setCellValue(test_status); 	
							CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("fillResultStatusXSSF: test_status1: <"+test_status+">");
								if(test_status.contains("P")){
									/*									XSSFCellStyle style = new XSSFCellStyle(new StylesTable());
								    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
								    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = sheet1.createCellStyle(); 
									/*									XSSFCellStyle curStyle = (XSSFCellStyle) column.getCellStyle();

							        curStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        curStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//XSSFCell curCell = row.getCell(column_pos);//row.getCell(partNumberColumn);
									//CellStyle style = column.getCellStyle();
									/*							        CellStyle style = null ;
							        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//curCell.setCellStyle(curStyle);
									/*									XSSFWorkbook wb = new XSSFWorkbook();
									XSSFCellStyle style = wb.createCellStyle();
									style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
									style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = 
									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										//style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");

											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionP FontSetting: " + e.getMessage());

										}*/
										//column.
										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionP StyleSetting: " + e.getMessage());

									}
									//wb.close();
								}
							}

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_FAIL){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("fillResultStatusXSSF: test_status2: <"+test_status+">");
								if(test_status.contains("F")){


									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										//style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");
											//font.setColor(IndexedColors.WHITE.getIndex());
											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionF FontSetting: " + e.getMessage());

										}*/

										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionF StyleSetting: " + e.getMessage());

									}

								}
							}
							test_status = result_json.getString("test_status");
							validateForMeterOvellFailStatus( test_status,  device_name);
							try{
								XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
								//font.setFontHeightInPoints((short)25);
								font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
								font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

								style.setFont(font);
								style.setBorderTop(BorderStyle.THIN);
								style.setBorderRight(BorderStyle.THIN);
								style.setBorderBottom(BorderStyle.THIN);
								style.setBorderLeft(BorderStyle.THIN);
								//style.setBorderBottom(BorderStyle.THIN);
								column.setCellStyle(style);
							}catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("fillResultStatusXSSF: Exception FontSetting: " + e.getMessage());

							}

						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillResultStatusXSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("fillResultStatusXSSF: Exception: " + e1.getMessage());

		}
	}
	public void FillErrorValueHSSF(HSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		ApplicationLauncher.logger.debug("FillErrorValueHSSF: filter_result: " + filter_result);
		try {

			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			if (filter_result.length()>0){
				for(int i=0; i<filter_result.length(); i++){
					//ApplicationLauncher.logger.debug("FillErrorValueHSSF: Test1: " + i);
					Row row = sheet1.getRow(row_pos);
					if(row == null){
						//ApplicationLauncher.logger.debug("FillErrorValueHSSF: Test2: " + i);
						row = sheet1.createRow(row_pos);
					}
					Cell column = row.getCell(column_pos);
					//ApplicationLauncher.logger.debug("FillErrorValueHSSF: Test3: " + i);
					for(int j=0; j<filter_result.length(); j++){
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");

						//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name:"+device_name);
						exportMode = false;
						if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
							//exportModeDeviceName = "";
							//test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: test_case_name:"+test_case_name);

							//if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

							exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name2:"+device_name);
							if(!device_name.equals(exportModeDeviceName)){
								exportMode = true;
								device_name= exportModeDeviceName;
							}
							//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							//}
						}
						if(isRackidFromMeterColSameHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){
							//if(isRackidFromMeterColSameExportModeHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){


							//ApplicationLauncher.logger.debug("FillErrorValue: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if (ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							ApplicationLauncher.logger.debug("FillErrorValueHSSF: device_name: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}

							column.setCellValue(error_value); 	
						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueHSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueHSSF: Exception: " + e1.getMessage());

		}
	}

	public boolean isRackidFromMeterColSameXSSF(XSSFSheet sheet1, int row_pos, int column_pos, String InpRackId, boolean currentTP_IsExportMode){

		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: row_pos: " + row_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column_pos: " + column_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: rack_id: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: currentTP_IsExportMode: " + currentTP_IsExportMode);
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//String rack_id = convertRackIdForReportExportMode(InpRackId);
		String rack_id = InpRackId;
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: InpRackId: " + InpRackId);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: rack_id2: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column: " + column);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: row_pos: " + row_pos);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: column_pos: " + column_pos);
		String value = column.getStringCellValue();
		ApplicationLauncher.logger.info("isRackidFromMeterColSame: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test1" );
			String rack = test_params[1];
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: rack: " + rack);
			if(rack_id.equals(rack)){
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test2" );
				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test3" );
					if(currentTP_IsExportMode){
						if(test_params[0].contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
							//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test4" );
							return true;
						}/*else if (Integer.parseInt(InpRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test4A" );
							return true;
						}*/else{
							//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test5" );
							return false;
						}
					}else {
						//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test6" );
						return true;
					}
				}else{
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test7" );
					return true;
				}
			}
			else{
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test8" );
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: test_params.length: " + test_params.length);
			return false;
		}
	}


	public boolean isRackidFromMeterColSameXSSFV2(XSSFSheet sheet1, int row_pos, int column_pos, String InpRackId, boolean currentTP_IsExportMode){

		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: row_pos: " + row_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column_pos: " + column_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: rack_id: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: currentTP_IsExportMode: " + currentTP_IsExportMode);
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//String rack_id = convertRackIdForReportExportMode(InpRackId);
		String rack_id = InpRackId;
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: InpRackId: " + InpRackId);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: rack_id2: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: column: " + column);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: row_pos: " + row_pos);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: column_pos: " + column_pos);
		String value = column.getStringCellValue();
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test1" );
			String rack = test_params[1];
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: rack: " + rack);
			if(rack_id.equals(rack)){
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test2" );
				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test3" );
					/*					if(currentTP_IsExportMode){
						if(test_params[0].contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test4" );
							return true;
						}else if (Integer.parseInt(InpRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test4A" );
							return true;
						}else{
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test5" );
							return false;
						}
					}else {*/
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test6" );
					return true;
					//}
				}else{
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test7" );
					return true;
				}
			}
			else{
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test8" );
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: test_params.length: " + test_params.length);
			return false;
		}
	}

	/*	public boolean isRackidFromMeterColSameXSSF(XSSFSheet sheet1, int row_pos, int column_pos, String rack_id){
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column: " + column);
		String value = "";
		try{
			value = column.getStringCellValue();
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSF: row_pos: " + row_pos);
			ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSF: column_pos: " + column_pos);
			ApplicationLauncher.logger.error("isRackidFromMeterColSameXSSF: Exception: " + e.getMessage());
			return false;
		}
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			String rack = test_params[1];
			if(rack_id.equals(rack)){
				return true;
			}
			else{
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: test_params.length: " + test_params.length);
			return false;
		}
	}
	 */




	//public boolean isRackidFromMeterColSameExportModeHSSF(HSSFSheet sheet1, int row_pos, int column_pos, String rack_id, boolean currentTP_IsExportMode){
	public boolean isRackidFromMeterColSameHSSF(HSSFSheet sheet1, int row_pos, int column_pos, String rack_id, boolean currentTP_IsExportMode){

		Row row = sheet1.getRow(row_pos);
		if(row == null){
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: Test1: ");
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: Test2: ");
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column: " + column);

		String value = column.getStringCellValue();
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			String rack = test_params[1];
			if(rack_id.equals(rack)){
				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
					if(currentTP_IsExportMode){
						if(test_params[0].contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
							return true;
						}else{
							return false;
						}
					}else {
						return true;
					}
				}else{
					return true;
				}
			}
			else{
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameHSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: test_params.length: " + test_params.length);
			return false;
		}
	}
	/*	public boolean isRackidFromMeterColSameHSSF(HSSFSheet sheet1, int row_pos, int column_pos, String rack_id){
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column: " + column);

		String value = column.getStringCellValue();
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			String rack = test_params[1];
			if(rack_id.equals(rack)){
				return true;
			}
			else{
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameHSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: test_params.length: " + test_params.length);
			return false;
		}
	}*/

	public void FillHeaderXSSF(XSSFSheet sheet1, String value , int row_pos,
			int column_pos){
		Row row = sheet1.getRow(row_pos);
		Cell column = row.getCell(column_pos);
		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		column.setCellValue(value); 
	}

	public void FillHeaderHSSF(HSSFSheet sheet1, String value , int row_pos,
			int column_pos){
		Row row = sheet1.getRow(row_pos);
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		column.setCellValue(value); 
	}

	public JSONArray FilterSelfHeatRepResults(String voltage, String current,
			String pf, String reading_no, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			JSONArray result_arr = result.getJSONArray("Results");
			String psuedo_name = getREPPseudoName(voltage, current,pf, reading_no);
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				//ApplicationLauncher.logger.debug("FilterSelfHeatResults: test_case_name : " + test_case_name);

				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				ApplicationLauncher.logger.debug("FilterSelfHeatResults: testname_wo_type : " + testname_wo_type);

				if(psuedo_name.equals(testname_wo_type)){
					ApplicationLauncher.logger.debug("FilterSelfHeatResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterSelfHeatRepResults: JSONException:"+e.getMessage());
		}
		return FilteredData;
	}

	public String getREPPseudoName(String voltage, String current,
			String pf, String reading_no){
		String psuedo_name = voltage + "-" + pf + "-" + current + "-" + reading_no;
		ApplicationLauncher.logger.debug("getREPPseudoName: "+psuedo_name);
		return psuedo_name;
	}



	public Boolean ExportRepeatability(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(ConstantReport.REP_TEMP_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*			HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.REP_TEMP_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.REPEATABILITY_ALIAS_NAME);
			String volt = ConstantReport.REP_TEMPL_VOLTAGE;
			ArrayList<String> currents = ConstantReport.REP_TEMPL_CURRENTS;
			String pf = ConstantReport.REP_TEMPL_PF;
			if(hssf_Format){
				status = PopulateRepeatabilityResultsHSSF(HSSF_Sheet, result, volt,currents,pf);
			}else{
				status = PopulateRepeatabilityResultsXSSF(XSSF_Sheet, result, volt,currents,pf);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.REP_TEMP_FILE_LOCATION);
			if (!new File(file_path).exists())
			{
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportRepeatability: FileNotFoundException:"+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Repeatability not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Repeatability not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Repeatability. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportRepeatability: IOException:"+e.getMessage());
		}
		return status;
	}

	public Boolean PopulateRepeatabilityResultsXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf){
		boolean status = false;
		int row_pos = 0;
		int meter_col = 0;
		int init_col = 0;
		int col_range = 0;
		int reading_no = 1;
		JSONArray filter_result = new JSONArray();
		String filter_reading_no ="";
		//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: currents.size(): "+currents.size());
		//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: ConstantReport.REP_TEMPL_ROWS.size: "+ConstantReport.REP_TEMPL_ROWS.size());
		for(int i=0; i<currents.size(); i++){
			//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: index: "+i);
			try{
				row_pos = ConstantReport.REP_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.REP_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateRepeatabilityResultsXSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Repeatability excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				status = false;
				return status;

			}
			filter_result = FilterSelfHeatRepResults(voltage,currents.get(i),pf,"1",result );
			if(filter_result.length()!=0){
				status = true;
				FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
				init_col = ConstantReport.REP_TEMPL_TEST_COL;
				col_range = ConstantReport.REP_TEMPL_TEST_COL + ConstantReport.REP_TEMPL_NO_OF_TESTS;
				reading_no = 1;
				for(int j=init_col; j<col_range; j++){
					filter_reading_no = Integer.toString(reading_no);
					ApplicationLauncher.logger.debug("PopulateRepeatabilityResultsXSSF: filter_reading_no: "+filter_reading_no);
					filter_result = FilterSelfHeatRepResults(voltage,currents.get(i),pf,filter_reading_no,result );
					FillErrorValueXSSF(sheet1, filter_result,row_pos,j,meter_col);		 
					reading_no++;
				} 
			}
		}  
		if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
			repeatabilityReportUpdateTesterName(sheet1);
			repeatabilityReportUpdateVoltage(sheet1);
			repeatabilityReportUpdateCurrent(sheet1);
			repeatabiltyReportUpdateReportSerialNo(sheet1); 
		}
		return status;
	}

	public Boolean PopulateRepeatabilityResultsHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf){
		boolean status = false;
		int row_pos = 0;
		int meter_col = 0;
		int init_col = 0;
		int col_range = 0;
		int reading_no = 1;
		JSONArray filter_result = new JSONArray();
		String filter_reading_no ="";
		//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: currents.size(): "+currents.size());
		//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: ConstantReport.REP_TEMPL_ROWS.size: "+ConstantReport.REP_TEMPL_ROWS.size());
		for(int i=0; i<currents.size(); i++){
			//ApplicationLauncher.logger.info("PopulateRepeatabilityResults: index: "+i);
			try{
				row_pos = ConstantReport.REP_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.REP_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateRepeatabilityResultsHSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Repeatability excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				status = false;
				return status;

			}
			filter_result = FilterSelfHeatRepResults(voltage,currents.get(i),pf,"1",result );
			if(filter_result.length()!=0){
				status = true;
				FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
				init_col = ConstantReport.REP_TEMPL_TEST_COL;
				col_range = ConstantReport.REP_TEMPL_TEST_COL + ConstantReport.REP_TEMPL_NO_OF_TESTS;
				reading_no = 1;
				for(int j=init_col; j<col_range; j++){
					filter_reading_no = Integer.toString(reading_no);
					ApplicationLauncher.logger.debug("PopulateRepeatabilityResultsHSSF: filter_reading_no: "+filter_reading_no);
					filter_result = FilterSelfHeatRepResults(voltage,currents.get(i),pf,filter_reading_no,result );
					FillErrorValueHSSF(sheet1, filter_result,row_pos,j,meter_col);		 
					reading_no++;
				} 
			}
		}  
		return status;
	}

	public Boolean ExportFrequencyVariation(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(ConstantReport.FREQ_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				ApplicationLauncher.logger.error("ExportFrequencyVariation: Exception1:"+e1.getMessage());
				hssf_Format= false;
				file.close();

				file = new FileInputStream(new File(ConstantReport.FREQ_TEMPL_FILE_LOCATION));

				//try {
				//OPCPackage pkg = OPCPackage.open(file);
				XSSFworkbook = new XSSFWorkbook(file);
				//XSSFWorkbook wb = new XSSFWorkbook(pkg);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);
				//} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//ApplicationLauncher.logger.error("ExportFrequencyVariation: ExceptInvalidFormatExceptionion2:"+e.getMessage());
				//}


			}

			result = FilterResultByTestType(result, ConstantApp.FREQUENCY_ALIAS_NAME);

			String volt = ConstantReport.FREQ_TEMPL_VOLTAGE;
			ArrayList<String> currents = ConstantReport.FREQ_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.FREQ_TEMPL_PFS;
			ArrayList<String> freqs = ConstantReport.FREQ_TEMPL_FREQUENCIES;
			if(hssf_Format){
				status = PopulateFrequencyResults_HSSF(HSSF_Sheet, result, volt,currents,pfs, freqs);
			}else{
				status = PopulateFrequencyResults_XSSF(XSSF_Sheet, result, volt,currents,pfs, freqs);
			}
			file.close();
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.FREQ_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists())
			{
				OutputFilePathExist = false;
			}

			FileOutputStream out = null;
			//new FileOutputStream(new File(file_path + file_name));
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

			ApplicationLauncher.logger.error("ExportFrequencyVariation: FileNotFoundException:"+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Frequency variation not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Frequency variation not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Frequency variation. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportFrequencyVariation: IOException:"+e.getMessage());
		}
		return status;
	}

	public Boolean PopulateFrequencyResults_XSSF(XSSFSheet sheet1, JSONObject result,
			String voltage,ArrayList<String> currents, ArrayList<String> pfs ,
			ArrayList<String> freqs){
		boolean ResultFoundstatus = false;
		int row_pos = 0;
		int meter_col = 0;

		JSONArray filter_result = new JSONArray();

		for(int i=0; i<pfs.size(); i++){
			if(i == 0){
				try{
					row_pos = ConstantReport.FREQ_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.FREQ_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateFrequencyResults_XSSF: Exception1:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Frequency excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundstatus = false;
					return ResultFoundstatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<pfs.size(); k++){
						filter_result = FilterSelfHeatRepResults(voltage,
								currents.get(j),pfs.get(k),ConstantReport.FREQ_TEMPL_DEFAULT_FREQ,result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateFrequencyResults_XSSF: filter_result " + filter_result);
							FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
							ResultFoundstatus = true;
							status = true;
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillFreqErrorValueXSSF(sheet1, result,voltage, 
							currents.get(j),pfs.get(i),freqs, j, row_pos,meter_col);
				}
			}
			else{
				try{
					row_pos = ConstantReport.FREQ_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.FREQ_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateFrequencyResults_XSSF: Exception2:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Frequency excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundstatus = false;
					return ResultFoundstatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<pfs.size(); k++){
						filter_result = FilterSelfHeatRepResults(voltage,
								currents.get(j),pfs.get(k),ConstantReport.FREQ_TEMPL_DEFAULT_FREQ,result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateFrequencyResults_XSSF2: filter_result " + filter_result);
							FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
							ResultFoundstatus = true;
							status = true;
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillFreqErrorValueXSSF(sheet1, result,voltage, 
							currents.get(j),pfs.get(i),freqs, j, row_pos,meter_col);
				}
			}
		}
		return ResultFoundstatus;
	}





	public Boolean PopulateFrequencyResults_HSSF(HSSFSheet sheet1, JSONObject result,
			String voltage,ArrayList<String> currents, ArrayList<String> pfs ,
			ArrayList<String> freqs){
		boolean ResultFoundstatus = false;
		int row_pos = 0;
		int meter_col = 0;

		JSONArray filter_result = new JSONArray();

		for(int i=0; i<pfs.size(); i++){
			if(i == 0){
				try{
					row_pos = ConstantReport.FREQ_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.FREQ_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateFrequencyResults_HSSF: Exception1:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Frequency excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundstatus = false;
					return ResultFoundstatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<pfs.size(); k++){
						filter_result = FilterSelfHeatRepResults(voltage,
								currents.get(j),pfs.get(k),ConstantReport.FREQ_TEMPL_DEFAULT_FREQ,result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateFrequencyResults_HSSF: filter_result " + filter_result);
							FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
							ResultFoundstatus = true;
							status = true;
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillFreqErrorValueHSSF(sheet1, result,voltage, 
							currents.get(j),pfs.get(i),freqs, j, row_pos,meter_col);
				}
			}
			else{
				try{
					row_pos = ConstantReport.FREQ_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.FREQ_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateFrequencyResults_HSSF: Exception2:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Frequency excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundstatus = false;
					return ResultFoundstatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<pfs.size(); k++){
						filter_result = FilterSelfHeatRepResults(voltage,
								currents.get(j),pfs.get(k),ConstantReport.FREQ_TEMPL_DEFAULT_FREQ,result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateFrequencyResults_HSSF2: filter_result " + filter_result);
							FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
							ResultFoundstatus = true;
							status = true;
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillFreqErrorValueHSSF(sheet1, result,voltage, 
							currents.get(j),pfs.get(i),freqs, j, row_pos,meter_col);
				}
			}
		}
		return ResultFoundstatus;
	}

	public void fillFreqErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, String current, String pf, ArrayList<String> freqs,int sec_no,
			int row_pos, int meter_col){
		String freq = ConstantReport.FREQ_TEMPL_DEFAULT_FREQ;

		int start_col = ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS.get(sec_no);
		JSONArray filter_result = FilterSelfHeatRepResults(voltage,current,pf,freq,result );
		FillHeaderXSSF(sheet1, freq,row_pos-1, start_col);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col,meter_col);
		start_col = start_col+ ConstantReport.FREQ_REF_SKIP_COL_COUNT;
		for(int i=0; i<freqs.size();i++){
			filter_result = FilterSelfHeatRepResults(voltage,current,pf,freqs.get(i),result );
			FillHeaderXSSF(sheet1, freqs.get(i),row_pos-1, start_col);
			FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col,meter_col);
			start_col = start_col+ ConstantReport.FREQ_SKIP_COL_COUNT;
		}
	}


	public void fillFreqErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, String current, String pf, ArrayList<String> freqs,int sec_no,
			int row_pos, int meter_col){
		String freq = ConstantReport.FREQ_TEMPL_DEFAULT_FREQ;

		int start_col = ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS.get(sec_no);
		JSONArray filter_result = FilterSelfHeatRepResults(voltage,current,pf,freq,result );
		FillHeaderHSSF(sheet1, freq,row_pos-1, start_col);
		FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col,meter_col);
		start_col = start_col+ ConstantReport.FREQ_REF_SKIP_COL_COUNT;
		for(int i=0; i<freqs.size();i++){
			filter_result = FilterSelfHeatRepResults(voltage,current,pf,freqs.get(i),result );
			FillHeaderHSSF(sheet1, freqs.get(i),row_pos-1, start_col);
			FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col,meter_col);
			start_col = start_col+ ConstantReport.FREQ_SKIP_COL_COUNT;
		}
	}





	public Boolean ExportVoltageVariation(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportVoltageVariation: Entry ");
			file = new FileInputStream(new File(ConstantReport.VV_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.VV_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.VOLTAGE_ALIAS_NAME);

			ApplicationLauncher.logger.debug("ExportVoltageVariation: result: " + result);

			ArrayList<String> volts = ConstantReport.VV_TEMPL_VOLTS;
			ArrayList<String> currents = ConstantReport.VV_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.VV_TEMPL_PFS;
			if(hssf_Format){
				status = PopulateVVResultsHSSF(HSSF_Sheet, result,volts, currents, pfs);
			}else{
				status = PopulateVVResultsXSSF(XSSF_Sheet, result,volts, currents, pfs);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.VV_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*			FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportVoltageVariation: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Voltage variation not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Voltage variation not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Voltage variation. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportVoltageVariation: IOException: " + e.getMessage());
		}
		return status;
	}

	public void loadProjectEndTime(){

		try {
			/*			String selectedEquipmentSerialNumber = "";//ref_cmbBox_SerialNumber.getSelectionModel().getSelectedItem().toString();
			if(ref_cmbBox_SerialNumber.getSelectionModel().getSelectedItem()!=null){
				selectedEquipmentSerialNumber = ref_cmbBox_SerialNumber.getSelectionModel().getSelectedItem().toString();
			}*/
			if((!ProjectEndTimeList.isEmpty())){

				List<String> selectedProjectEndTimeList = new ArrayList<String>(); 

				for (int i = 0; i < ProjectEndTimeList.size(); i++) {
					//if(equipmentSerialNolist.get(i).equals(selectedEquipmentSerialNumber)) {
					selectedProjectEndTimeList.add(ProjectEndTimeList.get(i));
					//}
				}

				ApplicationLauncher.logger.info(" loadProjectEndTime: loadProjectEndTime"+selectedProjectEndTimeList.size());
				Platform.runLater(() -> {
					ref_cmbBoxProjectList.getItems().clear();
				});
				if(selectedProjectEndTimeList.size()>0) {
					Platform.runLater(() -> {
						ref_cmbBoxProjectList.getItems().addAll(selectedProjectEndTimeList);
						ref_cmbBoxProjectList.getSelectionModel().select(0);
					});

				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadProjectEndTime: Exception:"+e.getMessage());
		}

	}

	public Boolean PopulateVVResultsXSSF(XSSFSheet sheet1, JSONObject result, ArrayList<String> volts, 
			ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.debug("PopulateVVResultsXSSF: Entry ");

		boolean ResultFoundStatus = false;

		int row_pos = 0;
		int meter_col = 0;

		JSONArray filter_result = new JSONArray();
		for(int i=0; i<pfs.size(); i++){
			if(i == 0){
				try{
					row_pos = ConstantReport.VV_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.VV_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateVVResultsXSSF: Exception1:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Voltage Variation excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundStatus = false;
					return ResultFoundStatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<volts.size(); k++){
						filter_result = FilterVVResults(volts.get(k),currents.get(j),
								pfs.get(i),result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateVVResultsXSSF1: filter_result " + filter_result);
							FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
							status = true;
							ResultFoundStatus = true;
							break;
						}
						if(status){
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillVVErrorValueXSSF(sheet1, result, volts,currents.get(j),
							pfs.get(i), j,row_pos,meter_col);
				}
			}
			else{
				try{
					row_pos = ConstantReport.VV_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.VV_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateVVResultsXSSF: Exception2:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Voltage Variation excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundStatus = false;
					return ResultFoundStatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<volts.size(); k++){
						filter_result = FilterVVResults(volts.get(k),currents.get(j),
								pfs.get(i),result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateVVResultsXSSF2: filter_result " + filter_result);
							FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
							status = true;
							ResultFoundStatus = true;
							break;
						}
						if(status){
							break;
						}
					}
					if(status){
						break;
					}
				}

				for(int j=0; j<currents.size(); j++){
					fillVVErrorValueXSSF(sheet1, result, volts,currents.get(j),
							pfs.get(i), j,row_pos,meter_col);
				}
			}
		}
		if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
			voltageVariationReportUpdateTesterName(sheet1);
			voltageVariationReportUpdateVoltage(sheet1);
			voltageVariationReportUpdateReportSerialNo(sheet1);
		}
		return ResultFoundStatus;
	}

	public Boolean PopulateVVResultsHSSF(HSSFSheet sheet1, JSONObject result, ArrayList<String> volts, 
			ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.debug("PopulateVVResultsHSSF: Entry ");

		boolean ResultFoundStatus = false;

		int row_pos = 0;
		int meter_col = 0;

		JSONArray filter_result = new JSONArray();
		for(int i=0; i<pfs.size(); i++){
			if(i == 0){
				try{
					row_pos = ConstantReport.VV_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.VV_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateVVResultsHSSF: Exception1:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Voltage Variation excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundStatus = false;
					return ResultFoundStatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<volts.size(); k++){
						filter_result = FilterVVResults(volts.get(k),currents.get(j),
								pfs.get(i),result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateVVResultsHSSF1: filter_result " + filter_result);
							FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
							status = true;
							ResultFoundStatus = true;
							break;
						}
						if(status){
							break;
						}
					}
					if(status){
						break;
					}
				}
				for(int j=0; j<currents.size(); j++){
					fillVVErrorValueHSSF(sheet1, result, volts,currents.get(j),
							pfs.get(i), j,row_pos,meter_col);
				}
			}
			else{
				try{
					row_pos = ConstantReport.VV_TEMPL_ROWS.get(i);
					meter_col = ConstantReport.VV_TEMPL_METER_COLS.get(i);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("PopulateVVResultsHSSF: Exception2:"+e.getMessage());
					ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Voltage Variation excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
					ResultFoundStatus = false;
					return ResultFoundStatus;

				}
				boolean status = false;
				for(int j=0; j<currents.size();j++){
					for(int k=0; k<volts.size(); k++){
						filter_result = FilterVVResults(volts.get(k),currents.get(j),
								pfs.get(i),result );
						if(filter_result.length() != 0){
							ApplicationLauncher.logger.debug("PopulateVVResultsHSSF2: filter_result " + filter_result);
							FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
							status = true;
							ResultFoundStatus = true;
							break;
						}
						if(status){
							break;
						}
					}
					if(status){
						break;
					}
				}

				for(int j=0; j<currents.size(); j++){
					fillVVErrorValueHSSF(sheet1, result, volts,currents.get(j),
							pfs.get(i), j,row_pos,meter_col);
				}
			}
		}


		return ResultFoundStatus;
	}

	public void fillVVErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			ArrayList<String> volts,String current, String pf,int sec_no,
			int row_pos, int meter_col){
		String volt = ConstantReport.VV_TEMPL_DEFAULT_VOLT;

		int start_col = ConstantReport.VV_TEMPL_DEF_VOLT_COLS.get(sec_no);
		JSONArray filter_result = FilterVVResults(volt,current,pf,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
		start_col = start_col+ ConstantReport.VV_REF_SKIP_COL_COUNT;
		for(int i=0; i<volts.size();i++){
			filter_result = FilterVVResults(volts.get(i),current,pf,result );
			FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.VV_SKIP_COL_COUNT;
		}
	}

	public void fillVVErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			ArrayList<String> volts,String current, String pf,int sec_no,
			int row_pos, int meter_col){
		String volt = ConstantReport.VV_TEMPL_DEFAULT_VOLT;

		int start_col = ConstantReport.VV_TEMPL_DEF_VOLT_COLS.get(sec_no);
		JSONArray filter_result = FilterVVResults(volt,current,pf,result );
		FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
		start_col = start_col+ ConstantReport.VV_REF_SKIP_COL_COUNT;
		for(int i=0; i<volts.size();i++){
			filter_result = FilterVVResults(volts.get(i),current,pf,result );
			FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.VV_SKIP_COL_COUNT;
		}
	}





	public JSONArray FilterVVResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterVVResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];
				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterVVResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterVVResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public Boolean ExportAccuracy(JSONObject result){
		ApplicationLauncher.logger.debug("ExportAccuracy: Entry ");
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {

			file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.ACCURACY_ALIAS_NAME);
			ApplicationLauncher.logger.debug("ExportAccuracy: result: "+result.toString());

			String voltage = ConstantReport.ACC_TEMPL_VOLT;
			ArrayList<String> currents = ConstantReport.ACC_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.ACC_TEMPL_PFS;
			ApplicationLauncher.logger.debug("ExportAccuracy: result: " + result);
			if(hssf_Format){
				status = PopulateACCResultsHSSF(HSSF_Sheet, result,voltage, currents, pfs);
			}else{
				status = PopulateACCResultsXSSF(XSSF_Sheet, result,voltage, currents, pfs);
			}
			file.close();
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.ACC_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportAccuracy: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.logger.info("ExportAccuracy: Template not found: Report template configured for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Accuracy not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.logger.info("ExportAccuracy: Output Path not found: Report output path for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Accuracy not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.logger.info("ExportAccuracy: File access failed: Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again - Prompted ");
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportAccuracy: IOException: " + e.getMessage());
		}
		return status;
	}


	public Boolean processCustom1Report(JSONObject result, Custom1ReportConfigModel custom1ReportCfg) { //String sourceTemplateFilePathName, 
		//String destinationPath, List<TestTypeFilter> filterTestType,
		//String meterSerialNoStartingPosition){
		ApplicationLauncher.logger.debug("processCustom1Report: Entry ");

		String sourceTemplateFilePathName = "";
		String destinationPath = "";


		sourceTemplateFilePathName = custom1ReportCfg.getTemplateFileLocationPath();
		destinationPath = custom1ReportCfg.getReportOutputPath();
		//List<TestTypeFilter> filterTestType = new ArrayList<TestTypeFilter>();
		//filterTestType = custom1ReportCfg.getExcelReportTestTypeFilter().getTestTypeFilter();
		//String meterSerialNoStartingPosition = "";
		//meterSerialNoStartingPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();


		ApplicationLauncher.logger.debug("processCustom1Report: sourceTemplateFilePathName: "+ sourceTemplateFilePathName);
		ApplicationLauncher.logger.debug("processCustom1Report: destinationPath: "+ destinationPath);


		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			//file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
			file = new FileInputStream(new File(sourceTemplateFilePathName));

			TemplateFilePathExist = true;

			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				//file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
				file = new FileInputStream(new File(sourceTemplateFilePathName));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			//result = FilterResultByTestType(result, ConstantApp.ACCURACY_ALIAS_NAME);
			ApplicationLauncher.logger.debug("processCustom1Report: result: "+result.toString());

			String voltage = ConstantReport.ACC_TEMPL_VOLT;
			ArrayList<String> currents = ConstantReport.ACC_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.ACC_TEMPL_PFS;
			ApplicationLauncher.logger.debug("processCustom1Report: result: " + result);
			if(hssf_Format){
				//status = PopulateACCResultsHSSF(HSSF_Sheet, result,voltage, currents, pfs);
			}else{
				status = populateCustom1ReportResultsXSSF(XSSF_Sheet, result, custom1ReportCfg);//meterSerialNoStartingPosition);
			}
			file.close();
			//String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			//String file_name = getSaveFileName(ConstantReport.ACC_TEMPL_FILE_LOCATION);
			String file_path = getSaveFilePath(destinationPath);
			String file_name = getSaveFileName(sourceTemplateFilePathName);
			
			setReportGeneratedPath(file_path);
			setReportGeneratedFileName(file_name);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}

			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("processCustom1Report: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.logger.info("processCustom1Report: Template not found: Report template configured for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Accuracy not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.logger.info("processCustom1Report: Output Path not found: Report output path for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Accuracy not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.logger.info("processCustom1Report: File access failed: Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again - Prompted ");
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("processCustom1Report: IOException: " + e.getMessage());
		}
		return status;
	}

	/*public Boolean PopulateACCResultsXSSF(XSSFSheet sheet1, JSONObject result, String voltage,
			ArrayList<String> currents, ArrayList<String> pfs){


		ApplicationLauncher.logger.debug("PopulateACCResultsXSSF: Entry ");
		int row_pos = ConstantReport.ACC_TEMPL_ROW;
		int meter_col = ConstantReport.ACC_TEMPL_METER_COL;
		boolean status = false;
		JSONArray filter_result = new JSONArray ();
		for(int i=0; i<currents.size();i++){
			for(int j=0; j<pfs.size(); j++){
				filter_result = FilterACCResults(voltage,
						currents.get(0),pfs.get(0),result );
				if(filter_result.length() != 0){
					FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
					status = true;
					break;
				}
			}
			if(status){
				break;
			}
		}

		for(int j=0; j<pfs.size(); j++){
			fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
		}
		return status;

	}*/

	public Boolean PopulateACCResultsXSSF(XSSFSheet sheet1, JSONObject result, String voltage,
			ArrayList<String> currents, ArrayList<String> pfs){


		ApplicationLauncher.logger.debug("PopulateACCResultsXSSF: Entry ");
		//sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT); 
		int row_pos = ConstantReport.ACC_TEMPL_ROW.get(0);
		int meter_col = ConstantReport.ACC_TEMPL_METER_COL.get(0);

		//ApplicationLauncher.logger.info("PopulateACCResultsXSSF: row_pos: "+row_pos);
		//ApplicationLauncher.logger.info("PopulateACCResultsXSSF: meter_col: "+meter_col);
		//ApplicationLauncher.logger.info("PopulateACCResultsXSSF: pfs.size(): "+pfs.size());
		int NumberOf_PF_InEachPage = pfs.size();
		int NumberOfPages = ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT;//2;
		int page2_row_pos = row_pos;//row_pos+30;// ConstantReport.ACC_TEMPL_ROW;


		boolean status = false;
		JSONArray filter_result = new JSONArray ();
		for(int i=0; i<currents.size();i++){
			for(int j=0; j<pfs.size(); j++){
				//filter_result = FilterACCResults(voltage,currents.get(0),pfs.get(0),result );
				filter_result = FilterACCResults(voltage,currents.get(i),pfs.get(j),result );
				if(filter_result.length() != 0){
					FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);

					if(NumberOfPages>1){
						try{
							page2_row_pos =ConstantReport.ACC_TEMPL_ROW.get(1);
						} catch (Exception e){
							ApplicationLauncher.logger.error("PopulateACCResultsXSSF: Exception: "+e.getMessage());
							ApplicationLauncher.logger.info("PopulateACCResultsXSSF: Report Excel Configuration Error1:  Issue with Report Excel Configuration-Accuracy, kindly check and reconfgiure the same - Prompted");
							ApplicationLauncher.InformUser("Report Excel Configuration Error", "Issue with Report Excel Configuration-Accuracy, kindly check and reconfgiure the same", AlertType.ERROR);

							return status;
						}
						FillMeterColumnXSSF(sheet1, filter_result,page2_row_pos, meter_col);
						NumberOf_PF_InEachPage = ConstantAppConfig.ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE;//3;
						//ApplicationLauncher.logger.info("PopulateACCResultsXSSF: page2_row_pos: "+page2_row_pos);
					}
					if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
						accuracyReportUpdateTesterName(sheet1);
						accuracyReportUpdateVoltage(sheet1,voltage);
						accuracyReportUpdateReportSerialNo(sheet1);
					}
					status = true;
					break;
				}
			}
			if(status){
				break;
			}
		}

		for(int j=0; j<pfs.size(); j++){
			if(j<NumberOf_PF_InEachPage){
				fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
			}else{
				if(NumberOfPages>1){
					fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, page2_row_pos,meter_col);
				}
			}
		}
		return status;

	}

	public Boolean PopulateACCResultsHSSF(HSSFSheet sheet1, JSONObject result, String voltage,
			ArrayList<String> currents, ArrayList<String> pfs){


		ApplicationLauncher.logger.debug("PopulateACCResultsHSSF: Entry ");

		int row_pos = ConstantReport.ACC_TEMPL_ROW.get(0);
		int meter_col = ConstantReport.ACC_TEMPL_METER_COL.get(0);

		//ApplicationLauncher.logger.info("PopulateACCResultsHSSF: row_pos: "+row_pos);
		//ApplicationLauncher.logger.info("PopulateACCResultsHSSF: meter_col: "+meter_col);
		//ApplicationLauncher.logger.info("PopulateACCResultsHSSF: pfs.size(): "+pfs.size());
		int NumberOf_PF_InEachPage = pfs.size();
		int NumberOfPages = ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT;//2;
		int page2_row_pos = row_pos;//row_pos+30;// ConstantReport.ACC_TEMPL_ROW;


		boolean status = false;
		JSONArray filter_result = new JSONArray ();
		for(int i=0; i<currents.size();i++){
			for(int j=0; j<pfs.size(); j++){
				//filter_result = FilterACCResults(voltage,currents.get(0),pfs.get(0),result );
				filter_result = FilterACCResults(voltage,currents.get(i),pfs.get(j),result );
				if(filter_result.length() != 0){
					FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);

					if(NumberOfPages>1){
						try{
							page2_row_pos =ConstantReport.ACC_TEMPL_ROW.get(1);
						} catch (Exception e){
							ApplicationLauncher.logger.error("PopulateACCResultsHSSF: Exception: "+e.getMessage());
							ApplicationLauncher.logger.info("PopulateACCResultsHSSF: Report Excel Configuration Error2:  Issue with Report Excel Configuration-Accuracy, kindly check and reconfgiure the same - Prompted");
							ApplicationLauncher.InformUser("Report Excel Configuration Error", "Issue with Report Excel Configuration-Accuracy, kindly check and reconfgiure the same", AlertType.ERROR);

							return status;
						}
						FillMeterColumnHSSF(sheet1, filter_result,page2_row_pos, meter_col);
						NumberOf_PF_InEachPage = ConstantAppConfig.ACC_NO_OF_PF_VARIANT_IN_EACH_PAGE;//3;
						//ApplicationLauncher.logger.info("PopulateACCResultsHSSF: page2_row_pos: "+page2_row_pos);
					}

					status = true;
					break;
				}
			}
			if(status){
				break;
			}
		}

		for(int j=0; j<pfs.size(); j++){
			if(j<NumberOf_PF_InEachPage){
				fillACCErrorValueHSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
			}else{
				if(NumberOfPages>1){
					fillACCErrorValueHSSF(sheet1, result, voltage,currents,pfs.get(j), j, page2_row_pos,meter_col);
				}
			}
		}
		return status;

	}

	public Boolean populateCustom1ReportResultsXSSF(XSSFSheet sheet1, JSONObject inputResult,Custom1ReportConfigModel custom1ReportCfg){//String meterSerialNoStartingPosition){


		ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: Entry ");

		JSONObject result = inputResult;
		String meterSerialNoStartingPosition = "";
		List<TestTypeFilter> filterTestType = new ArrayList<TestTypeFilter>();
		filterTestType = custom1ReportCfg.getExcelReportTestTypeFilter().getTestTypeFilter();
		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);  

		//ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: row_pos: "+row_pos);
		//ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: meter_col: "+meter_col);
		//ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: pfs.size(): "+pfs.size());
		//int NumberOf_PF_InEachPage = pfs.size();
		int NumberOfPages = ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT;//2;
		//int page2_row_pos = row_pos;//row_pos+30;// ConstantReport.ACC_TEMPL_ROW;
		int presentPageNumber = 1;		
		int maxPageNumberSupported = 3;
		int readPageNumber = 0;

		boolean status = false;
		JSONArray filter_result = new JSONArray ();
		String dataStartingErrorValueCellPosition = "";
		String dataStartingErrorStatusCellPosition = "";
		String dataStartingCellPosition = "";
		int serialNoMaxCount = 1;
		int row_pos = 1;
		int meter_col = 1;
		int column_pos = 0;
		boolean populateResult = false;
		int populateMeterProfileDataIterationCount = 1;
		boolean populateMeterProfileDataforEachMeter = false;
		String testType = "";
		String voltageFilter = "";
		String currentFilter = "";
		String pfFilter = "";
		String freqFilter = "";
		//String voltUnbalanceFilter = "";
		String resultIterationId = "0";
		boolean populateDataErrorValue = false;
		boolean populateDataStatus = false;
		String populateType = ConstantReport.REPORT_DATA_POPULATE_VERTICAL;
		boolean appendMeterSerialNoAndRackPosition = false;

		int maxDutDisplayRequestedPerPage = custom1ReportCfg.getMaxDutDisplayPerPage();
		boolean splitDutDisplayIntoMultiplePage = custom1ReportCfg.getSplitDutDisplayIntoMultiplePage();
		setDutMeterDataPreviousPageUpdatedPageNumber(0);
		setDutMeterDataPreviousPageLastUpdatedRackId("0");
		boolean errorInReportJsonConfiguration = true;
		try {

			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){
				for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().size(); meterDataCellPositionIndex++ ){
					ExcelReportMeterDataCellPositionPage meterDataCellPositionPage =  custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().get(meterDataCellPositionIndex);
					if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
						//meterSerialNoStartingPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
						meterSerialNoStartingPosition = meterDataCellPositionPage.getCellStartPositionMeterSerialNo();
						row_pos = getRowValueFromCellValue(meterSerialNoStartingPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
						meter_col = getColValueFromCellValue(meterSerialNoStartingPosition);//ConstantReport.ACC_TEMPL_METER_COL.get(0);
						column_pos = 0;
						/*String dataStartingErrorValueCellPosition = "";
						String dataStartingErrorStatusCellPosition = "";
						String dataStartingCellPosition = "";*/
						filter_result =  result.getJSONArray("Results");
						serialNoMaxCount = 1;
						populateMeterProfileDataIterationCount = 1;
						populateMeterProfileDataforEachMeter = false;
						ArrayList<String> uniqueMeterSerialNoList = new ArrayList<String>();
						uniqueMeterSerialNoList = FillMeterColumnXSSF_V2(sheet1, filter_result,row_pos, meter_col,maxDutDisplayRequestedPerPage,splitDutDisplayIntoMultiplePage,pageNumberIteration);
						clearMeterDeviceOverAllStatus();
						for(int i = 0; i< uniqueMeterSerialNoList.size() ; i++){
							ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: uniqueMeterSerialNoList: "+uniqueMeterSerialNoList.get(i));
							addMeterDeviceOverAllStatus(uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_PASS);
						}
						serialNoMaxCount = getSerialNumberMaxCount();
						ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: serialNoMaxCount: "+serialNoMaxCount);
					}
				}
				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().size(); meterDataDisplayIndex++ ){
					ExcelReportMeterDataDisplayPage meterDataDisplayPage =  custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().get(meterDataDisplayIndex);
					ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						errorInReportJsonConfiguration = true;
						for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().size(); meterDataCellPositionIndex++ ){
							meterDataCellPositionPage =  custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().get(meterDataCellPositionIndex);
							if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
								errorInReportJsonConfiguration = false;
								break;
							}
						}
						if(errorInReportJsonConfiguration){

							ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: Error_Code R-333: user prompted: Configuration mismatch on report json between ExcelReportMeterDataDisplay and ExcelReportMeterDataCellPosition related to pagenumber. Kindly reconfigure the same");
							ApplicationLauncher.InformUser("Error_Code R-333","Configuration mismatch on report json between ExcelReportMeterDataDisplay and ExcelReportMeterDataCellPosition related to pagenumber. Kindly reconfigure the same", AlertType.ERROR);
							return false;
						}
						populateResult = false;
						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateSerialNo();
						populateResult = meterDataDisplayPage.getPopulateSerialNo();
						if(populateResult){		
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionSerialNo();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionSerialNo();
							fillSerialNoColumnXSSF(sheet1, serialNoMaxCount, dataStartingCellPosition);
						}
						//fillMakeCapacityColumnXSSF(sheet1, serialNoMaxCount, );



						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateMake();
						populateResult = meterDataDisplayPage.getPopulateMake();
						if(populateResult){

							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMake();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMake();
							populateMeterProfileDataIterationCount = meter_col;
							populateMeterProfileDataforEachMeter = false;

							//populateMeterProfileDataforEachMeter = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateMakeForEach();
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateMakeForEach();

							if(!populateMeterProfileDataforEachMeter){
								populateMeterProfileDataIterationCount = 1;
							}

							//fillMakeColumnXSSF( sheet1, dataStartingCellPosition, meter_col);
							fillMakeColumnXSSF( sheet1, dataStartingCellPosition, populateMeterProfileDataIterationCount);
						}
						try {
						populateResult = meterDataDisplayPage.getPopulateModelNo();
						if(populateResult){

							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMake();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionModelNo();
							populateMeterProfileDataIterationCount = meter_col;
							populateMeterProfileDataforEachMeter = false;

							//populateMeterProfileDataforEachMeter = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateMakeForEach();
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateModelNoForEach();

							if(!populateMeterProfileDataforEachMeter){
								populateMeterProfileDataIterationCount = 1;
							}

							//fillMakeColumnXSSF( sheet1, dataStartingCellPosition, meter_col);
							fillModelNoColumnXSSF( sheet1, dataStartingCellPosition, populateMeterProfileDataIterationCount);
						}
						}catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("populateCustom1ReportResultsXSSF: fillModelNoColumnXSSF: Exception: " + e.getMessage());
						}

						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateCustomerRefNo();
						populateResult = meterDataDisplayPage.getPopulateCustomerRefNo();
						if(populateResult){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionCustomerRefNo();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionCustomerRefNo();
							populateMeterProfileDataIterationCount = serialNoMaxCount;
							populateMeterProfileDataforEachMeter = false;
							//populateMeterProfileDataforEachMeter = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateCustomerRefNoForEach();
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateCustomerRefNoForEach();
							if(!populateMeterProfileDataforEachMeter){
								populateMeterProfileDataIterationCount = 1;
							}

							fillCustomerReferenceNoColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
						}



						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateCapacity();
						populateResult = meterDataDisplayPage.getPopulateCapacity();
						if(populateResult){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionCapacity();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionCapacity();
							populateMeterProfileDataIterationCount = serialNoMaxCount;				
							populateMeterProfileDataforEachMeter = false;
							//populateMeterProfileDataforEachMeter = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateCapacityForEach();
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateCapacityForEach();
							if(!populateMeterProfileDataforEachMeter){
								populateMeterProfileDataIterationCount = 1;
							}
							fillCapacityColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
						}

						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateMeterType();
						populateResult = meterDataDisplayPage.getPopulateMeterType();
						if(populateResult){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterType();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMeterType();
							//fillMakeCapacityColumnXSSF( sheet1, dataStartingCellPosition, meter_col);

							populateMeterProfileDataIterationCount = serialNoMaxCount;				
							populateMeterProfileDataforEachMeter = false;
							//populateMeterProfileDataforEachMeter = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateMeterTypeForEach();
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateMeterTypeForEach();
							if(!populateMeterProfileDataforEachMeter){
								populateMeterProfileDataIterationCount = 1;
							}
							fillMeterTypeColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);

						}

						populateResult = meterDataDisplayPage.getPopulateMeterConstant();
						if(populateResult){
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMeterConstant();
							populateMeterProfileDataIterationCount = serialNoMaxCount;				
							populateMeterProfileDataforEachMeter = false;
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateMeterConstantForEach();

							if(populateMeterProfileDataforEachMeter){
								fillMeterConstantColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
							}else{
								populateMeterProfileDataIterationCount = 1;
								fillMeterConstantFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
							}
						}



						populateResult = meterDataDisplayPage.getPopulatePtRatio();
						if(populateResult){
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionPtRatio();
							populateMeterProfileDataIterationCount = serialNoMaxCount;				
							populateMeterProfileDataforEachMeter = false;
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulatePtRatioForEach();

							if(populateMeterProfileDataforEachMeter){

								fillPtRatioColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
							}else{
								populateMeterProfileDataIterationCount = 1;

								fillPtRatioFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
							}
						}


						populateResult = meterDataDisplayPage.getPopulateCtRatio();
						if(populateResult){
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionCtRatio();
							populateMeterProfileDataIterationCount = serialNoMaxCount;				
							populateMeterProfileDataforEachMeter = false;
							populateMeterProfileDataforEachMeter = meterDataDisplayPage.getPopulateCtRatioForEach();

							if(populateMeterProfileDataforEachMeter){

								fillCtRatioColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
							}else{
								populateMeterProfileDataIterationCount = 1;

								fillCtRatioFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
							}
						}






						//populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateDutOverAllStatus();
						/*						populateResult = meterDataDisplayPage.getPopulateDutOverAllStatus();
						if(populateResult){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionDutOverAllStatus();
							fillMeterOverAllStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
						}*/

						// added below logic to remove RackId In MeterSerialNoColumn
						//boolean appendMeterSerialNoAndRackPosition = custom1ReportCfg.getExcelReportMeterDataDisplay().getAppendMeterSerialNoAndRackPosition();
						/*						appendMeterSerialNoAndRackPosition = meterDataDisplayPage.getAppendMeterSerialNoAndRackPosition();
						if(!appendMeterSerialNoAndRackPosition){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMeterSerialNo();
							removeRackIdInMeterSerialNoColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
						}*/
					}
				}


				populateDataErrorValue = false;
				populateDataStatus = false;
				populateType = ConstantReport.REPORT_DATA_POPULATE_VERTICAL;
				for (int i = 0; i < filterTestType.size(); i++){
					//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: filterTestType getPrefixType: "+filterTestType.get(i).getPrefixType());
					if(pageNumberIteration == filterTestType.get(i).getPageNumber()){
						populateResult = filterTestType.get(i).getPopulateResult();
						if(populateResult){
							testType  = filterTestType.get(i).getPrefixType();
							dataStartingErrorValueCellPosition = filterTestType.get(i).getCellStartResultValuePosition();
							dataStartingErrorStatusCellPosition = filterTestType.get(i).getCellStartResultStatusPosition();
							row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
							column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
							ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: row_pos: "+row_pos);
							ApplicationLauncher.logger.info("z "+column_pos);
							populateDataErrorValue = filterTestType.get(i).getPopulateResultValue();
							populateDataStatus = filterTestType.get(i).getPopulateResultStatus();
							populateType = filterTestType.get(i).getPopulateType();
							resultIterationId = filterTestType.get(i).getResultIterationId();
							if(testType.equals(ConstantApp.ACCURACY_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.ACCURACY_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillACCErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col);
									status = true;
								}
							}else if(testType.equals(ConstantApp.CREEP_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();

								if(populateDataErrorValue){
									if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
										fillCreepErrorValueCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltageFilter, 
												column_pos,row_pos, meter_col);
										status = true;
									}
								}

								if(populateDataStatus){
									if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
										row_pos = getRowValueFromCellValue(dataStartingErrorStatusCellPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
										column_pos = getColValueFromCellValue(dataStartingErrorStatusCellPosition);
										fillCreepResultStatusCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltageFilter, 
												column_pos,row_pos, meter_col);
										status = true;
									}
								}
							}else if(testType.equals(ConstantApp.STA_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								/*if(populateDataErrorValue && populateDataStatus){
									ApplicationLauncher.logger.info("populateCustom1ReportResultsXSSF: STA : both Result and error value not implemented yet ");
								}else*/ 
								if(populateDataErrorValue){
									if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
										fillSTA_ErrorValueCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_STA_TIME,voltageFilter, currentFilter, 
												column_pos, row_pos,meter_col);
										status = true;
									}
								}

								if(populateDataStatus){
									row_pos = getRowValueFromCellValue(dataStartingErrorStatusCellPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
									column_pos = getColValueFromCellValue(dataStartingErrorStatusCellPosition);
									if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
										fillSTA_ResultStatusCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_STA_TIME,voltageFilter, currentFilter, 
												column_pos, row_pos, meter_col);
										status = true;
									}
								}
							}else if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.REPEATABILITY_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillSelfHeatRepeatabilityErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col, resultIterationId);
									status = true;
								}
							}else if(testType.equals(ConstantApp.VOLTAGE_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.VOLTAGE_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillVoltageVariationErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col);
									status = true;
								}
							}else if(testType.equals(ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillRpsNormalErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col);
									status = true;
								}
							}else if(testType.equals(ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillRpsReverseErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col);
									status = true;
								}
							}else if(testType.equals(ConstantApp.FREQUENCY_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								freqFilter = filterTestType.get(i).getFreqFilter();;//"52.5";
								result = FilterResultByTestType(inputResult, ConstantApp.FREQUENCY_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillFreqTestErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col, freqFilter);
									status = true;
								}

							}else if(testType.equals(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								//voltUnbalanceFilter = filterTestType.get(i).getVoltUnbalancePhaseSelectionFilter();
								//String freqFilter = filterTestType.get(i).getFreqFilter();;//"52.5";
								result = FilterResultByTestType(inputResult, ConstantApp.VOLT_UNBALANCE_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillVoltUnbalanceErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col);
									status = true;
								}

							}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.SELF_HEATING_ALIAS_NAME);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
									fillSelfHeatRepeatabilityErrorValueCustomReportXSSF( sheet1, result,
											voltageFilter, currentFilter, pfFilter, column_pos,
											row_pos, meter_col, resultIterationId);
									status = true;
								}
							}else if(testType.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){
								voltageFilter = filterTestType.get(i).getVoltageFilter();
								currentFilter = filterTestType.get(i).getCurrentFilter();
								pfFilter = filterTestType.get(i).getPfFilter();
								result = FilterResultByTestType(inputResult, ConstantApp.CONST_TEST_ALIAS_NAME);
								String energyFilter = filterTestType.get(i).getEnergyFilter();
								String populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
								boolean populateResultFilterDutPulseCountReading = filterTestType.get(i).isPopulateResultFilterDutPulseCountReading();
								boolean populateResultFilterDutInitialReading = filterTestType.get(i).isPopulateResultFilterDutInitialReading();
								boolean populateResultFilterDutFinalReading = filterTestType.get(i).isPopulateResultFilterDutFinalReading();
								boolean populateResultFilterRefStdInitialReading = filterTestType.get(i).isPopulateResultFilterRefStdInitialReading();
								boolean populateResultFilterRefStdFinalReading = filterTestType.get(i).isPopulateResultFilterRefStdFinalReading();
								boolean singleCellData = false;
								if(populateResultFilterDutPulseCountReading){
									populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
								}else if(populateResultFilterDutInitialReading){
									populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH;
								}else if(populateResultFilterDutFinalReading){
									populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_FINAL_KWH;
								}else if(populateResultFilterRefStdInitialReading){
									populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH;
									singleCellData = true;
								}else if(populateResultFilterRefStdFinalReading){
									populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH;
									singleCellData = true;
								}

								ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: populateResultFilterDataType: " + populateResultFilterDataType);
								//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
								if(populateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){

									if( singleCellData ){

										fillConstRefStValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
												row_pos, column_pos);
										status = true;
									}else{
										fillConstErrorValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
												row_pos, column_pos, meter_col);
										status = true;
									}
								}
							}
						}
					}
				}

				for (HashMap.Entry<String,String> hashMeterid : meterDeviceOverAllStatus.entrySet()){
					ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: Key1: " + hashMeterid.getKey()  + " Value: "+ hashMeterid.getValue());
				}

				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().size(); meterDataDisplayIndex++ ){
					ExcelReportMeterDataDisplayPage meterDataDisplayPage =  custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().get(meterDataDisplayIndex);
					ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataDisplayPage: Hit1");
						//errorInReportJsonConfiguration = true;
						for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().size(); meterDataCellPositionIndex++ ){
							meterDataCellPositionPage =  custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().get(meterDataCellPositionIndex);
							if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
								//errorInReportJsonConfiguration = false;
								//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataCellPositionPage: Hit2");
								break;
							}

						}
						//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataCellPositionPage: Hit3");

						populateResult = meterDataDisplayPage.getPopulateDutOverAllStatus();
						ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: getPopulateDutOverAllStatus: populateResult: " + populateResult);
						if(populateResult){
							//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataCellPositionPage: Hit4");
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionDutOverAllStatus();
							fillMeterOverAllStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
						}
					}
				}


				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().size(); meterDataDisplayIndex++ ){
					ExcelReportMeterDataDisplayPage meterDataDisplayPage =  custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().get(meterDataDisplayIndex);
					ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataDisplayPage: Hit1");
						//errorInReportJsonConfiguration = true;
						for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().size(); meterDataCellPositionIndex++ ){
							meterDataCellPositionPage =  custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().get(meterDataCellPositionIndex);
							if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
								//errorInReportJsonConfiguration = false;
								//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataCellPositionPage: Hit2");
								break;
							}

						}
						appendMeterSerialNoAndRackPosition = meterDataDisplayPage.getAppendMeterSerialNoAndRackPosition();
						if(!appendMeterSerialNoAndRackPosition){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMeterSerialNo();
							removeRackIdInMeterSerialNoColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
						}
					}
				}



			}
			///int pageNumberIteration = 1;
			/*			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){
				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().size(); meterDataDisplayIndex++ ){
					ExcelReportMeterDataDisplayPage meterDataDisplayPage =  custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().get(meterDataDisplayIndex);
					ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataDisplayPage: Hit1");
						//errorInReportJsonConfiguration = true;
						for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().size(); meterDataCellPositionIndex++ ){
							meterDataCellPositionPage =  custom1ReportCfg.getExcelReportMeterDataCellPosition().getExcelReportMeterDataCellPositionPage().get(meterDataCellPositionIndex);
							if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
								//errorInReportJsonConfiguration = false;
								//ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: meterDataCellPositionPage: Hit2");
								break;
							}

						}
						appendMeterSerialNoAndRackPosition = meterDataDisplayPage.getAppendMeterSerialNoAndRackPosition();
						if(!appendMeterSerialNoAndRackPosition){
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
							dataStartingCellPosition = meterDataCellPositionPage.getCellStartPositionMeterSerialNo();
							removeRackIdInMeterSerialNoColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
						}
					}
				}
			}*/


			//fillMeterColumnWitOutRackId_XSSF(sheet1, filter_result,row_pos, meter_col);
			/*String testType = "";
			String voltageFilter = "";
			String currentFilter = "";
			String pfFilter = "";
			String resultIterationId = "0";*/

			/*			testType = "";
			voltageFilter = "";
			currentFilter = "";
			pfFilter = "";
			resultIterationId = "0"*/




			/*			populateResult = custom1ReportCfg.getExcelReportMeterDataDisplay().getPopulateDutOverAllStatus();
			if(populateResult){
				dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();

				fillMeterOverAllStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
			}

			// added below logic to remove RackId In MeterSerialNoColumn
			boolean appendMeterSerialNoAndRackPosition = custom1ReportCfg.getExcelReportMeterDataDisplay().getAppendMeterSerialNoAndRackPosition();
			if(!appendMeterSerialNoAndRackPosition){
				dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
				removeRackIdInMeterSerialNoColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
			}*/

			if(ConstantAppConfig.REPORT_DATA_REPLACE_ENABLED){
				for (int i=0; i < ConstantAppConfig.REPORT_DATA_REPLACE_COUNT;i++){
					ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: i: " + i);
					ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: ConstantAppConfig.REPORT_DATA_FIND.get(i): " + ConstantAppConfig.REPORT_DATA_FIND.get(i));
					ApplicationLauncher.logger.debug("populateCustom1ReportResultsXSSF: ConstantAppConfig.REPORT_DATA_REPLACE.get(i): " + ConstantAppConfig.REPORT_DATA_REPLACE.get(i));
					replaceDataInReportExcel(sheet1, ConstantAppConfig.REPORT_DATA_FIND.get(i),ConstantAppConfig.REPORT_DATA_REPLACE.get(i));
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("populateCustom1ReportResultsXSSF: JSONException: " + e.getMessage());
		}





		return status;

	}

	public JSONArray FilterACCResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterACCResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];

				/*ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfVolt: " + selectedRateOfVolt);

				ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfCurrent: " + selectedRateOfCurrent);

				ApplicationLauncher.logger.debug("FilterACCResults: lag_lead: " + lag_lead);*/

				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterACCResults: test_case_name: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterACCResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}
	public void fillACCErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf,int section,
			int row_pos, int meter_col){

		int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		for(int i=0; i<currents.size();i++){
			filter_result = FilterACCResults(voltage,currents.get(i),pf,result );
			FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
			FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		}



	}

	public void fillACCErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		//int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		//for(int i=0; i<currents.size();i++){
		filter_result = FilterACCResults(voltageFilter,currentFilter,pfFilter,result );
		//FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);
		//start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		//}



	}


	public void fillSelfHeatRepeatabilityErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col, String resultIterationId){

		//int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		//for(int i=0; i<currents.size();i++){
		//filter_result = FilterACCResults(voltageFilter,currentFilter,pfFilter,result );
		filter_result = FilterSelfHeatRepResults(voltageFilter, currentFilter, pfFilter,resultIterationId,result );
		//FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);
		//start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		//}



	}


	public void fillVoltageVariationErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterVVResults(voltageFilter,currentFilter,pfFilter,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}


	public void fillRpsNormalErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		//filter_result = FilterVVResults(voltageFilter,currentFilter,pfFilter,result );
		filter_result = FilterRPSResults(voltageFilter,currentFilter,pfFilter,result,ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public void fillRpsReverseErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterRPSResults(voltageFilter,currentFilter,pfFilter,result,ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public void fillFreqTestErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col, String freqFilter){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterSelfHeatRepResults(voltageFilter,
				currentFilter,pfFilter,freqFilter,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}



	public void fillVoltUnbalanceErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageUnbalanceFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col ){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterVUResults(voltageUnbalanceFilter,//ConstantReport.VU_TEMPL_DEF_VOLT,
				currentFilter,pfFilter,result);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public Boolean fillConstantTestValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col, String energyFilter){

		ApplicationLauncher.logger.debug("fillConstantTestValueCustomReportXSSF: Entry ");
		boolean ResultFoundStatus = false;
		//int row_pos = ConstantReport.CONST_TEMPL_ROW;
		//int meter_col = ConstantReport.CONST_TEMPL_METER_COL;
		//String selected_power = ConstantReport.CONST_TEMPL_POWER;
		JSONArray filter_result = FilterCONSTResults(voltageFilter,currentFilter,pfFilter,energyFilter,result);
		if(filter_result.length()!=0){
			ResultFoundStatus = true;
			//FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			/*			fillConstErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltageFilter,currentFilter,pfFilter, energyFilter,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(0),meter_col);
			fillConstErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltageFilter,currentFilter,pfFilter, energyFilter,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(1), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(2), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(3), meter_col);*/
			fillConstErrorValueCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltageFilter,currentFilter,pfFilter, energyFilter,
					row_pos, col_pos, meter_col);

			/*			ArrayList<String> values = getRefKWh();
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
			filter_result = FilterCONSTResults(voltageFilter,currentFilter,pfFilter,energyFilter,result );
			int row = ApplicationLauncher.getRowValueFromCellValue(values.get(0));
			int col = ApplicationLauncher.getColValueFromCellValue(values.get(0));
			//FillHeaderWithCellXSSF(sheet1, filter_result,row, col);
			FillReferenceWithCellXSSF(sheet1, filter_result,row, col);
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
			filter_result = FilterCONSTResults(voltageFilter,currentFilter,pfFilter,energyFilter,result );
			row = ApplicationLauncher.getRowValueFromCellValue(values.get(1));
			col = ApplicationLauncher.getColValueFromCellValue(values.get(1));
			FillReferenceWithCellXSSF(sheet1, filter_result,row, col);*/
		}
		return ResultFoundStatus;
	}

	public void fillACCErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf,int section,
			int row_pos, int meter_col){

		int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		for(int i=0; i<currents.size();i++){
			filter_result = FilterACCResults(voltage,currents.get(i),pf,result );
			FillHeaderHSSF(sheet1, currents.get(i),row_pos-1, start_col);
			FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		}



	}


	public Boolean ExportRPS(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportRPS: Entry ");
			file = new FileInputStream(new File(ConstantReport.RPS_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.RPS_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantReport.RPS_FILTER_TESTTYPE);
			ApplicationLauncher.logger.debug("ExportRPS: result: " + result);
			String volt = ConstantReport.RPS_TEMPL_VOLTAGE;
			String current = ConstantReport.RPS_TEMPL_CURRENT;
			String pf = ConstantReport.RPS_TEMPL_PF;
			ApplicationLauncher.logger.info("ExportRPS: volt: " + volt);
			if(hssf_Format){
				status = PopulateRPSResultsHSSF(HSSF_Sheet, result,volt,current,pf);
			}else{
				status = PopulateRPSResultsXSSF(XSSF_Sheet, result,volt,current,pf);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.RPS_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportRPS: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Reverse Phase Sequence not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Reverse Phase Sequence not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Reverse Phase Sequence. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportRPS: IOException: " + e.getMessage());
		}
		return status;
	}

	public Boolean PopulateRPSResultsXSSF(XSSFSheet sheet1, JSONObject result, String voltage, String current, String pf){
		ApplicationLauncher.logger.debug("PopulateRPSResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.RPS_TEMPL_ROW;
		int meter_col = ConstantReport.RPS_TEMPL_METER_COL;
		JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME );
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			fillRPSErrorValueXSSF(sheet1, result, voltage,current,pf,
					ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME, row_pos, meter_col);
			fillRPSErrorValueXSSF(sheet1, result, voltage,current,pf,
					ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME, row_pos, meter_col);
		}
		return ResultFoundStatus;
	}

	public Boolean PopulateRPSResultsHSSF(HSSFSheet sheet1, JSONObject result, String voltage, String current, String pf){
		ApplicationLauncher.logger.debug("PopulateRPSResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.RPS_TEMPL_ROW;
		int meter_col = ConstantReport.RPS_TEMPL_METER_COL;
		JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME );
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
			fillRPSErrorValueHSSF(sheet1, result, voltage,current,pf,
					ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME, row_pos, meter_col);
			fillRPSErrorValueHSSF(sheet1, result, voltage,current,pf,
					ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME, row_pos, meter_col);
		}
		return ResultFoundStatus;
	}

	public JSONArray FilterRPSResults(String voltage, String current,
			String pf, JSONObject result, String test_type){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterRPSResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				ApplicationLauncher.logger.debug("FilterRPSResults: testname_wo_type : " + testname_wo_type);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];


				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf)) && (test_case_name.contains(test_type))){
					FilteredData.put(result_json);	
					ApplicationLauncher.logger.debug("FilterRPSResults: FilteredData:  "+FilteredData);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterRPSResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public void fillRPSErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, String current, String pf, String Testtype, int row_pos, int meter_col){
		if(Testtype.equals(ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME)){
			ApplicationLauncher.logger.info("fillRPSErrorValueXSSF1: Testtype: "+Testtype);
			JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,Testtype );
			ApplicationLauncher.logger.debug("fillRPSErrorValueXSSF1: filter_result: "+filter_result);
			FillErrorValueXSSF(sheet1, filter_result,row_pos, ConstantReport.RPS_TEMPL_NORMAL_REV_COL.get(0), meter_col);
		}
		else{
			ApplicationLauncher.logger.info("fillRPSErrorValueXSSF2: Testtype: "+Testtype);
			JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,Testtype );
			ApplicationLauncher.logger.debug("fillRPSErrorValueXSSF2: filter_result: "+filter_result);
			FillErrorValueXSSF(sheet1, filter_result,row_pos, ConstantReport.RPS_TEMPL_NORMAL_REV_COL.get(1), meter_col);
		}
	}

	public void fillRPSErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, String current, String pf, String Testtype, int row_pos, int meter_col){
		if(Testtype.equals(ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME)){
			ApplicationLauncher.logger.info("fillRPSErrorValueHSSF1: Testtype: "+Testtype);
			JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,Testtype );
			ApplicationLauncher.logger.debug("fillRPSErrorValueHSSF1: filter_result: "+filter_result);
			FillErrorValueHSSF(sheet1, filter_result,row_pos, ConstantReport.RPS_TEMPL_NORMAL_REV_COL.get(0), meter_col);
		}
		else{
			ApplicationLauncher.logger.info("fillRPSErrorValueHSSF2: Testtype: "+Testtype);
			JSONArray filter_result = FilterRPSResults(voltage,current,pf,result,Testtype );
			ApplicationLauncher.logger.debug("fillRPSErrorValueHSSF2: filter_result: "+filter_result);
			FillErrorValueHSSF(sheet1, filter_result,row_pos, ConstantReport.RPS_TEMPL_NORMAL_REV_COL.get(1), meter_col);
		}
	}

	public Boolean ExportHARM(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportHARM: Entry ");
			file = new FileInputStream(new File(ConstantReport.HARM_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.HARM_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantReport.HARM_FILTER_TESTTYPE);

			String volt = ConstantReport.HARM_TEMPL_VOLTAGE;
			ArrayList<String> currents = ConstantReport.HARM_TEMPL_CURRENTS;
			String pf = ConstantReport.HARM_TEMPL_PF;
			ApplicationLauncher.logger.debug("ExportHARM: result: " + result);
			if(hssf_Format){
				status = PopulateHARMResultsHSSF(HSSF_Sheet, result,volt,currents,pf);
			}else{
				status = PopulateHARMResultsXSSF(XSSF_Sheet, result,volt,currents,pf);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.HARM_TEMPL_FILE_LOCATION);

			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportHARM: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Harmonics not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Harmonics not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Harmonics. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportHARM: IOException: " + e.getMessage());
		}
		return status;
	}

	public Boolean PopulateHARMResultsXSSF(XSSFSheet sheet1, JSONObject result, 
			String voltage, ArrayList<String> currents,
			String pf){
		ApplicationLauncher.logger.debug("PopulateHARMResultsXSSF: Entry ");
		ArrayList<String> harmonics = ConstantReport.HARM_TEMPL_HARM_TIMES;
		boolean ResultFoundStatus =false;
		JSONArray filter_result = new JSONArray ();
		int row_pos = 0;
		int meter_col = 0;
		String harmonic_times = "";
		for(int i=0; i<harmonics.size(); i++){
			try{
				row_pos = ConstantReport.HARM_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.HARM_TEMPL_METER_COLS.get(i);
				harmonic_times = ConstantReport.HARM_TEMPL_HARM_TIMES.get(i);

			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateHARMResultsXSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Harmonic excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				ResultFoundStatus = false;
				return ResultFoundStatus;

			}

			for(int j=0; j<currents.size();j++){
				filter_result = FilterHARMResults(voltage,currents.get(j),
						pf,harmonic_times, result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				if(filter_result.length() != 0){
					ApplicationLauncher.logger.debug("PopulateHARMResultsXSSF: filter_result " + filter_result);
					FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
					ResultFoundStatus = true;
					break;
				}
			}
			fillHARMErrorValueXSSF(sheet1, result, voltage,pf,currents,harmonic_times,
					ConstantApp.HARMONIC_INPHASE_ALIAS_NAME,row_pos, meter_col);
			fillHARMErrorValueXSSF(sheet1, result, voltage,pf,currents,harmonic_times, 
					ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME,row_pos, meter_col);

		}
		return ResultFoundStatus;
	}

	public Boolean PopulateHARMResultsHSSF(HSSFSheet sheet1, JSONObject result, 
			String voltage, ArrayList<String> currents,
			String pf){
		ApplicationLauncher.logger.debug("PopulateHARMResultsHSSF: Entry ");
		ArrayList<String> harmonics = ConstantReport.HARM_TEMPL_HARM_TIMES;
		boolean ResultFoundStatus =false;
		JSONArray filter_result = new JSONArray ();
		int row_pos = 0;
		int meter_col = 0;
		String harmonic_times = "";
		for(int i=0; i<harmonics.size(); i++){
			try{
				row_pos = ConstantReport.HARM_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.HARM_TEMPL_METER_COLS.get(i);
				harmonic_times = ConstantReport.HARM_TEMPL_HARM_TIMES.get(i);

			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateHARMResultsHSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Harmonic excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				ResultFoundStatus = false;
				return ResultFoundStatus;

			}

			for(int j=0; j<currents.size();j++){
				filter_result = FilterHARMResults(voltage,currents.get(j),
						pf,harmonic_times, result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				if(filter_result.length() != 0){
					ApplicationLauncher.logger.debug("PopulateHARMResultsHSSF: filter_result " + filter_result);
					FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
					ResultFoundStatus = true;
					break;
				}
			}
			fillHARMErrorValueHSSF(sheet1, result, voltage,pf,currents,harmonic_times,
					ConstantApp.HARMONIC_INPHASE_ALIAS_NAME,row_pos, meter_col);
			fillHARMErrorValueHSSF(sheet1, result, voltage,pf,currents,harmonic_times, 
					ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME,row_pos, meter_col);

		}
		return ResultFoundStatus;
	}

	public JSONArray FilterHARMResults(String voltage, String current,
			String pf,String harmonic_times, JSONObject result, String test_type){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterHARMResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			String pseudo_name =  getHarmPseudoName(voltage, current, pf, harmonic_times);

			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				ApplicationLauncher.logger.debug("FilterHARMResults: testname_wo_type : " + testname_wo_type);


				ApplicationLauncher.logger.debug("FilterHARMResults: pseudo_name: "+pseudo_name);
				ApplicationLauncher.logger.debug("FilterHARMResults: testname_wo_type: "+testname_wo_type);
				ApplicationLauncher.logger.debug("FilterHARMResults: test_case_name: "+test_case_name);
				ApplicationLauncher.logger.debug("FilterHARMResults: test_type: "+test_type);
				if(pseudo_name.equals(testname_wo_type) && (test_case_name.contains(test_type))){
					ApplicationLauncher.logger.debug("FilterHARMResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterHARMResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public String getHarmPseudoName(String voltage, String current,
			String pf,String harmonic_times){
		String TestCaseName = voltage + "-" + pf + "-" + current + "-" + harmonic_times;
		return TestCaseName;
	}

	public void fillHARMErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, String pf, ArrayList<String> currents,String harmonic_times,String testtype,int row_pos, int meter_col){
		JSONArray filter_result = new JSONArray ();
		if(testtype.equals(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			int start_col = ConstantReport.HARM_TEMPL_PHASE_COLS.get(0);
			for(int i=0; i<currents.size();i++){
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times,result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_WO_SKIP_COL_COUNT;
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result,ConstantApp.HARMONIC_INPHASE_ALIAS_NAME );
				FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_W_SKIP_COL_COUNT;
			}
		}
		else{
			int start_col = ConstantReport.HARM_TEMPL_PHASE_COLS.get(1);
			for(int i=0; i<currents.size();i++){
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_WO_SKIP_COL_COUNT;
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result, ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME );
				FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_W_SKIP_COL_COUNT;
			}
		}
	}

	public void fillHARMErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, String pf, ArrayList<String> currents,String harmonic_times,String testtype,int row_pos, int meter_col){
		JSONArray filter_result = new JSONArray ();
		if(testtype.equals(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			int start_col = ConstantReport.HARM_TEMPL_PHASE_COLS.get(0);
			for(int i=0; i<currents.size();i++){
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times,result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_WO_SKIP_COL_COUNT;
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result,ConstantApp.HARMONIC_INPHASE_ALIAS_NAME );
				FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_W_SKIP_COL_COUNT;
			}
		}
		else{
			int start_col = ConstantReport.HARM_TEMPL_PHASE_COLS.get(1);
			for(int i=0; i<currents.size();i++){
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result,ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME );
				FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_WO_SKIP_COL_COUNT;
				filter_result = FilterHARMResults(voltage,currents.get(i),pf,harmonic_times, result, ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME );
				FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);
				start_col = start_col+ ConstantReport.HARM_W_SKIP_COL_COUNT;
			}
		}
	}



	public Boolean ExportVU(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportVU: Entry ");
			file = new FileInputStream(new File(ConstantReport.VU_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.VU_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.VOLT_UNBALANCE_ALIAS_NAME);

			ArrayList<String> voltages = ConstantReport.VU_TEMPL_VOLTAGES;
			String current = ConstantReport.VU_TEMPL_CURRENT;
			String pf = ConstantReport.VU_TEMPL_PF;
			ApplicationLauncher.logger.debug("ExportVU: result: " + result);
			if(hssf_Format){
				status = PopulateVUResultsHSSF(HSSF_Sheet, result,voltages,current,pf);
			}else{
				status = PopulateVUResultsXSSF(XSSF_Sheet, result,voltages,current,pf);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.VU_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportVU: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Voltage Unbalance not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Voltage Unbalance not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Voltage Unbalance. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportVU: IOException: " + e.getMessage());
		}
		return status;
	}

	public Boolean PopulateVUResultsXSSF(XSSFSheet sheet1, JSONObject result, 
			ArrayList<String> voltages,
			String current, String pf){
		ApplicationLauncher.logger.debug("PopulateVUResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.VU_TEMPL_ROW;
		int meter_col = ConstantReport.VU_TEMPL_METER_COL;
		JSONArray filter_result = FilterVUResults(ConstantReport.VU_TEMPL_DEF_VOLT,
				current,pf,result);
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			fillVUErrorValueXSSF(sheet1, result, voltages,current,pf, meter_col);
			if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
				voltageUnbalanceReportUpdateTesterName(sheet1);
				voltageUnbalanceReportUpdateVoltage(sheet1);
				voltageUnbalanceReportUpdateCurrent(sheet1);
				voltageUnbalanceReportUpdateReportSerialNo(sheet1); 
			}
		}
		return ResultFoundStatus;
	}

	public Boolean PopulateVUResultsHSSF(HSSFSheet sheet1, JSONObject result, 
			ArrayList<String> voltages,
			String current, String pf){
		ApplicationLauncher.logger.debug("PopulateVUResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.VU_TEMPL_ROW;
		int meter_col = ConstantReport.VU_TEMPL_METER_COL;
		JSONArray filter_result = FilterVUResults(ConstantReport.VU_TEMPL_DEF_VOLT,
				current,pf,result);
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
			fillVUErrorValueHSSF(sheet1, result, voltages,current,pf, meter_col);
		}
		return ResultFoundStatus;
	}

	public JSONArray FilterVUResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterVUResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];
				ApplicationLauncher.logger.debug("FilterVUResults: selectedRateOfVolt: " + selectedRateOfVolt);
				ApplicationLauncher.logger.debug("FilterVUResults: voltage: " + voltage);



				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterVUResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterVUResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public void fillVUErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			ArrayList<String> voltages, String current, String pf, int meter_col){
		int start_col = ConstantReport.VU_TEMPL_DEF_VOLT_COL;
		ApplicationLauncher.logger.info("fillVUErrorValueXSSF:  ref Volt:" + ConstantReport.VU_TEMPL_DEF_VOLT);
		JSONArray filter_result = FilterVUResults(ConstantReport.VU_TEMPL_DEF_VOLT,current,pf,result );
		FillErrorValueXSSF(sheet1, filter_result,ConstantReport.VU_TEMPL_ROW, start_col, meter_col);
		start_col = start_col+ ConstantReport.VU_REF_SKIP_COL_COUNT;
		for(int i=0; i<voltages.size();i++){
			filter_result = FilterVUResults(voltages.get(i),current,pf,result );
			FillErrorValueXSSF(sheet1, filter_result,ConstantReport.VU_TEMPL_ROW, start_col, meter_col);
			start_col = start_col+ ConstantReport.VU_SKIP_COL_COUNT;
		}
	}

	public void fillVUErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			ArrayList<String> voltages, String current, String pf, int meter_col){
		int start_col = ConstantReport.VU_TEMPL_DEF_VOLT_COL;
		ApplicationLauncher.logger.info("fillVUErrorValueHSSF:  ref Volt:" + ConstantReport.VU_TEMPL_DEF_VOLT);
		JSONArray filter_result = FilterVUResults(ConstantReport.VU_TEMPL_DEF_VOLT,current,pf,result );
		FillErrorValueHSSF(sheet1, filter_result,ConstantReport.VU_TEMPL_ROW, start_col, meter_col);
		start_col = start_col+ ConstantReport.VU_REF_SKIP_COL_COUNT;
		for(int i=0; i<voltages.size();i++){
			filter_result = FilterVUResults(voltages.get(i),current,pf,result );
			FillErrorValueHSSF(sheet1, filter_result,ConstantReport.VU_TEMPL_ROW, start_col, meter_col);
			start_col = start_col+ ConstantReport.VU_SKIP_COL_COUNT;
		}
	}

	public Boolean ExportUnbalanceLoad(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportUnbalanceLoad: Entry ");
			file = new FileInputStream(new File(ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;

			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}

			result = FilterResultByTestType(result, ConstantApp.ACCURACY_ALIAS_NAME);
			ApplicationLauncher.logger.debug("ExportUnbalanceLoad: result: " + result);
			ArrayList<String> voltages = ConstantReport.UNBALANCED_LOAD_TEMPL_VOLTAGES;
			ArrayList<String> currents = ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.UNBALANCED_LOAD_TEMPL_PFS;
			if(hssf_Format){
				status = PopulateUnbalancedLoad_ResultsHSSF(HSSF_Sheet, result, voltages, currents,pfs);
			}else{
				status = PopulateUnbalancedLoad_ResultsXSSF(XSSF_Sheet, result, voltages, currents,pfs);

			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportUnbalanceLoad: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Unbalanced load not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Unbalanced load not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Unbalanced load. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportUnbalanceLoad: IOException: " + e.getMessage());
		}
		return status;
	}

	public Boolean PopulateUnbalancedLoad_ResultsXSSF(XSSFSheet sheet1, JSONObject result, ArrayList<String> voltages, 
			ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.debug("PopulateUnbalancedLoad_ResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		JSONArray filter_result = new JSONArray ();
		int row_pos = 0;
		int meter_col =0;
		for(int i=0; i<pfs.size(); i++){
			//if(i == 0){
			try{
				row_pos = ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateUnbalancedLoad_ResultsXSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Unbalanced Load excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				ResultFoundStatus = false;
				return ResultFoundStatus;

			}
			boolean status = false;
			for(int j=0; j<currents.size();j++){
				for(int k=0; k<voltages.size(); k++){
					filter_result = FilterUnbalanceLoadResults(voltages.get(k),
							currents.get(j),pfs.get(i),result);

					if(filter_result.length() != 0){
						ApplicationLauncher.logger.debug("PopulateUnbalancedLoad_ResultsXSSF: filter_result " + filter_result);
						FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
						status = true;
						ResultFoundStatus = true;
						break;
					}
					if(status){
						break;
					}
				}
				if(status){
					break;
				}
			}

			fillACC_Def_ErrorValueXSSF(sheet1, result,pfs.get(i),0,row_pos, meter_col);
			for(int j=0; j<voltages.size(); j++){
				UnbalanceLoadTempl_ErrorValueXSSF(sheet1, result, voltages.get(j),currents,pfs.get(i), j+1,row_pos, meter_col);
			}


		}
		return ResultFoundStatus;
	}

	public Boolean PopulateUnbalancedLoad_ResultsHSSF(HSSFSheet sheet1, JSONObject result, ArrayList<String> voltages, 
			ArrayList<String> currents, ArrayList<String> pfs){
		ApplicationLauncher.logger.debug("PopulateUnbalancedLoad_ResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		JSONArray filter_result = new JSONArray ();
		int row_pos = 0;
		int meter_col =0;
		for(int i=0; i<pfs.size(); i++){
			//if(i == 0){
			try{
				row_pos = ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS.get(i);
				meter_col = ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS.get(i);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("PopulateUnbalancedLoad_ResultsHSSF: Exception:"+e.getMessage());
				ApplicationLauncher.InformUser("Report Excel configuration failed", "Few fields on Unbalanced Load excel configuration missing. Kindly reconfigure the same and try again", AlertType.ERROR);
				ResultFoundStatus = false;
				return ResultFoundStatus;

			}
			boolean status = false;
			for(int j=0; j<currents.size();j++){
				for(int k=0; k<voltages.size(); k++){
					filter_result = FilterUnbalanceLoadResults(voltages.get(k),
							currents.get(j),pfs.get(i),result);

					if(filter_result.length() != 0){
						ApplicationLauncher.logger.debug("PopulateUnbalancedLoad_ResultsHSSF: filter_result " + filter_result);
						FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
						status = true;
						ResultFoundStatus = true;
						break;
					}
					if(status){
						break;
					}
				}
				if(status){
					break;
				}
			}

			fillACC_Def_ErrorValueHSSF(sheet1, result,pfs.get(i),0,row_pos, meter_col);
			for(int j=0; j<voltages.size(); j++){
				UnbalanceLoadTempl_ErrorValueHSSF(sheet1, result, voltages.get(j),currents,pfs.get(i), j+1,row_pos, meter_col);
			}


		}
		return ResultFoundStatus;
	}

	public void fillACC_Def_ErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String pf,int sec_no, int row_pos, int meter_col){
		int start_col = ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.get(sec_no);
		JSONArray filter_result = FilterUnbalanceLoadResults(ConstantReport.UNBALANCED_LOAD_TEMPL_DEF_VOLT,
				ConstantReport.UNBALANCED_LOAD_TEMPL_DEF_CURRENT,pf,result );

		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);

	}

	public void fillACC_Def_ErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String pf,int sec_no, int row_pos, int meter_col){
		int start_col = ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.get(sec_no);
		JSONArray filter_result = FilterUnbalanceLoadResults(ConstantReport.UNBALANCED_LOAD_TEMPL_DEF_VOLT,
				ConstantReport.UNBALANCED_LOAD_TEMPL_DEF_CURRENT,pf,result );

		FillErrorValueHSSF(sheet1, filter_result,row_pos, start_col, meter_col);

	}

	public void UnbalanceLoadTempl_ErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf,int sec_no,int row_pos, int meter_col){

		int start_col = ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.get(sec_no);
		JSONArray filter_result = new JSONArray (); 
		for(int i=0; i<currents.size();i++){
			filter_result = FilterUnbalanceLoadResults(voltage,currents.get(i),pf,result );
			FillErrorValueXSSF(sheet1, filter_result, row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.UNBALANCELOAD_SKIP_COL_COUNT;
		}
	}

	public void UnbalanceLoadTempl_ErrorValueHSSF(HSSFSheet sheet1, JSONObject result,
			String voltage, ArrayList<String> currents, String pf,int sec_no,int row_pos, int meter_col){

		int start_col = ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.get(sec_no);
		JSONArray filter_result = new JSONArray (); 
		for(int i=0; i<currents.size();i++){
			filter_result = FilterUnbalanceLoadResults(voltage,currents.get(i),pf,result );
			FillErrorValueHSSF(sheet1, filter_result, row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.UNBALANCELOAD_SKIP_COL_COUNT;
		}
	}

	public JSONArray FilterUnbalanceLoadResults(String f_voltage, String f_current,
			String f_pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterUnbalanceLoadResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			/*			String phase = "";
			String pf_value = "";
			String f_phase = "";*/
			String pseudo_testpointname  = "";
			for(int i=0; i<result_arr.length(); i++){
				try{
					result_json = result_arr.getJSONObject(i);
					test_case_name = result_json.getString("test_case_name");
					testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
					String[] test_params = testname_wo_type.split("-");
					selectedRateOfVolt = test_params[0];
					lag_lead = test_params[1];
					selectedRateOfCurrent = test_params[2];
					/*					phase = "";
					pf_value = "";
					f_phase = "";*/
					pseudo_testpointname  = GetPseudoTestPointName(f_voltage, f_current, f_pf);
					if(pseudo_testpointname.equals(testname_wo_type)){
						ApplicationLauncher.logger.debug("FilterUnbalanceLoadResults: "+test_case_name);
						FilteredData.put(result_json);	
					}
				}
				catch(Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("FilterUnbalanceLoadResults: Exception:"+e.getMessage());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterUnbalanceLoadResults: JSONException:"+e.getMessage());
		}
		return FilteredData;
	}

	public String GetPseudoTestPointName(String f_voltage, String f_current, String f_pf){
		String voltage = "";
		String phase = "";
		String test_case_name = "";
		if(f_voltage.contains(":")){
			String[] phase_voltage = f_voltage.split(":");
			phase = phase_voltage[0];
			voltage = phase_voltage[1];
			//if(!phase.equals("ABC")){
			if(!phase.equals(All_PhaseDisplayName)){

				test_case_name = voltage + "-" + phase + ":" + f_pf + "-" + f_current;
			}
			else{
				test_case_name = voltage + "-" + f_pf + "-" + f_current;
			}
		}
		else{
			test_case_name = voltage + "-" + f_pf + "-" + f_current;
		}
		return test_case_name;
	}

	public Boolean ExportCONST(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportCONST: Entry ");
			file = new FileInputStream(new File(ConstantReport.CONST_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.CONST_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.CONST_TEST_ALIAS_NAME);
			String voltage = ConstantReport.CONST_TEMPL_VOLTAGE;
			String current = ConstantReport.CONST_TEMPL_CURRENT;
			String pf = ConstantReport.CONST_TEMPL_PF;
			ApplicationLauncher.logger.debug("ExportCONST: result: " + result);
			if(hssf_Format){
				status = PopulateCONSTResultsHSSF(HSSF_Sheet, result,voltage,current,pf);
			}else{
				status = PopulateCONSTResultsXSSF(XSSF_Sheet, result,voltage,current,pf);
			}
			file.close();

			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.CONST_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path+ file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
				//XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportCONST :FileNotFoundException: "+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Constant Test not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Constant Test not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Constant Test. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportCONST :IOException: "+e.getMessage());
		}
		return status;
	}

	public Boolean PopulateCONSTResultsXSSF(XSSFSheet sheet1, JSONObject result, 
			String voltage, String current, String pf){

		ApplicationLauncher.logger.debug("PopulateCONSTResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.CONST_TEMPL_ROW;
		int meter_col = ConstantReport.CONST_TEMPL_METER_COL;
		String selected_power = ConstantReport.CONST_TEMPL_POWER;
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result);
		if(filter_result.length()!=0){
			ResultFoundStatus = true;
			FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			fillConstErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(0),meter_col);
			fillConstErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(1), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(2), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(3), meter_col);
			fillConstErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(4), meter_col);

			ArrayList<String> values = getRefKWh();
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
			filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
			int row = ApplicationLauncher.getRowValueFromCellValue(values.get(0));
			int col = ApplicationLauncher.getColValueFromCellValue(values.get(0));
			//FillHeaderWithCellXSSF(sheet1, filter_result,row, col);
			FillReferenceWithCellXSSF(sheet1, filter_result,row, col);
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
			filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
			row = ApplicationLauncher.getRowValueFromCellValue(values.get(1));
			col = ApplicationLauncher.getColValueFromCellValue(values.get(1));
			//FillHeaderWithCellXSSF(sheet1, filter_result,row, col);
			FillReferenceWithCellXSSF(sheet1, filter_result,row, col);
			//XSSFFormulaEvaluator.evaluateAllFormulaCells(sheet1);
		}
		return ResultFoundStatus;
	}

	public Boolean PopulateCONSTResultsHSSF(HSSFSheet sheet1, JSONObject result, 
			String voltage, String current, String pf){

		ApplicationLauncher.logger.debug("PopulateCONSTResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.CONST_TEMPL_ROW;
		int meter_col = ConstantReport.CONST_TEMPL_METER_COL;
		String selected_power = ConstantReport.CONST_TEMPL_POWER;
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result);
		if(filter_result.length()!=0){
			ResultFoundStatus = true;
			FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
			fillConstErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(0),meter_col);
			fillConstErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(1), meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(2), meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(3), meter_col);
			fillConstErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltage,current,pf, selected_power,
					ConstantReport.CONST_TEMPL_CONST_COLS.get(4), meter_col);

			ArrayList<String> values = getRefKWh();
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
			ApplicationLauncher.logger.info("PopulateCONSTResultsHSSF: result: "+result);
			filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
			int row = ApplicationLauncher.getRowValueFromCellValue(values.get(0));
			int col = ApplicationLauncher.getColValueFromCellValue(values.get(0));
			//ApplicationLauncher.logger.info("PopulateCONSTResultsHSSF: filter_result: "+filter_result);
			//FillHeaderWithCellHSSF(sheet1, filter_result,row, col);
			FillReferenceWithCellHSSF(sheet1, filter_result,row, col);
			result = GetResultsFromDB(ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH);
			filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
			row = ApplicationLauncher.getRowValueFromCellValue(values.get(1));
			col = ApplicationLauncher.getColValueFromCellValue(values.get(1));
			//FillHeaderWithCellHSSF(sheet1, filter_result,row, col);
			FillReferenceWithCellHSSF(sheet1, filter_result,row, col);
		}
		return ResultFoundStatus;
	}

	public void FillHeaderWithCellXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillHeaderWithCellXSSF: filter_result: " + filter_result);
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			for(int i=0; i<filter_result.length(); i++){
				Row row = sheet1.getRow(row_pos);
				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}
				for(int j=0; j<filter_result.length(); j++){
					result_json = filter_result.getJSONObject(j);
					device_name = result_json.getString("device_name");
					device_name = getRealDeviceIDForExportMode(device_name);
					ApplicationLauncher.logger.debug("FillHeaderWithCellXSSF: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					ApplicationLauncher.logger.debug("FillHeaderWithCellXSSF: device_name: " + device_name+ " : "+ error_value);
					column.setCellValue(error_value); 	

				}
				row_pos++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillHeaderWithCellXSSF :JSONException: "+e.getMessage());
		}
	}

	/*	public void FillReferenceWithCellXSSF(XSSFSheet sheet1, JSONArray filter_result, int input_row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillReferenceWithCellXSSF: filter_result: " + filter_result);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			String test_case_name = "";
			String exportDeviceName = "";
			int row_pos = input_row_pos;
			Row row = sheet1.getRow(row_pos);
			Cell column = row.getCell(column_pos);
			for(int i=0; i<filter_result.length(); i++){///expectation is 2 results one for import and another one for export
				row = sheet1.getRow(row_pos);
				column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}
				//for(int j=0; j<filter_result.length(); j++){
					result_json = filter_result.getJSONObject(i);
					device_name = result_json.getString("device_name");
					//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: "+device_name);

					exportDeviceName = getRealDeviceIDForExportMode(device_name);
					//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: " + device_name+ " : "+ error_value);
					if(device_name.equals(exportDeviceName)){
						if(Integer.valueOf(device_name)<=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){

							row = sheet1.getRow(row_pos);
							if(i==1){
								row = sheet1.getRow(row_pos-1);
							}
							column = row.getCell(column_pos);
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp -hit:");
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :row_pos:"+row_pos);
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :column_pos:"+column_pos);

							column.setCellValue(error_value); 
						}
					}else{
						if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
							row = sheet1.getRow(row_pos+1);
							if(i==1){
								row = sheet1.getRow(row_pos);
							}
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp -hit:");
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :row_pos:"+row_pos);
							//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :column_pos:"+column_pos);

							column = row.getCell(column_pos);
							column.setCellValue(error_value); 	
						}
					}
				//}
				row_pos++;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReferenceWithCellXSSF :JSONException: "+e.getMessage());
		}
	}*/

	public void FillReferenceWithCellXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillReferenceWithCellXSSF: filter_result: " + filter_result);
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			String test_case_name = "";
			String exportDeviceName = "";
			//int row_pos = input_row_pos;
			Row row = sheet1.getRow(row_pos);
			Cell column = row.getCell(column_pos);
			for(int i=0; i<filter_result.length(); i++){///expectation is 2 results one for import and another one for export
				//row = sheet1.getRow(row_pos);
				//column = row.getCell(column_pos);
				//if(column == null){
				//	column = sheet1.getRow(row_pos).createCell(column_pos);
				//}
				//for(int j=0; j<filter_result.length(); j++){
				result_json = filter_result.getJSONObject(i);
				device_name = result_json.getString("device_name");
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: "+device_name);

				exportDeviceName = getRealDeviceIDForExportMode(device_name);
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: result_json: " + result_json);
				error_value = result_json.getString("error_value");
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: " + device_name+ " : "+ error_value);
				if(device_name.equals(exportDeviceName)){//import mode
					if(Integer.valueOf(device_name)<=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){

						row = sheet1.getRow(row_pos);
						/*if(i==1){
								row = sheet1.getRow(row_pos-1);
							}*/
						column = row.getCell(column_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp :row_pos:"+row_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp :column_pos:"+column_pos);
						if(column == null){
							column = sheet1.getRow(row_pos).createCell(column_pos);
						}
						column.setCellValue(error_value); 
					}
				}else{ //export mode
					if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
						//row = sheet1.getRow(row_pos+1);
						row = sheet1.getRow(row_pos);
						/*if(i==1){
								row = sheet1.getRow(row_pos);
							}*/
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-exp :row_pos:"+(row_pos+1));
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-exp :column_pos:"+column_pos);

						column = row.getCell(column_pos);
						if(column == null){
							//column = sheet1.getRow(row_pos+1).createCell(column_pos);
							column = sheet1.getRow(row_pos).createCell(column_pos);
						}
						column.setCellValue(error_value); 	
					}
				}
				//}
				//row_pos++;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReferenceWithCellXSSF :JSONException: "+e.getMessage());
		}
	}

	public void FillReferenceWithCellHSSF(HSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillReferenceWithCellHSSF: filter_result: " + filter_result);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			String test_case_name = "";
			String exportDeviceName = "";
			//int row_pos = input_row_pos;
			Row row = sheet1.getRow(row_pos);
			Cell column = row.getCell(column_pos);
			for(int i=0; i<filter_result.length(); i++){///expectation is 2 results one for import and another one for export
				//row = sheet1.getRow(row_pos);
				//column = row.getCell(column_pos);
				//if(column == null){
				//	column = sheet1.getRow(row_pos).createCell(column_pos);
				//}
				//for(int j=0; j<filter_result.length(); j++){
				result_json = filter_result.getJSONObject(i);
				device_name = result_json.getString("device_name");
				//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: device_name: "+device_name);

				exportDeviceName = getRealDeviceIDForExportMode(device_name);
				//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: result_json: " + result_json);
				error_value = result_json.getString("error_value");
				//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: device_name: " + device_name+ " : "+ error_value);
				if(device_name.equals(exportDeviceName)){//import mode
					if(Integer.valueOf(device_name)<=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){

						row = sheet1.getRow(row_pos);
						/*if(i==1){
								row = sheet1.getRow(row_pos-1);
							}*/
						column = row.getCell(column_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :row_pos:"+row_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :column_pos:"+column_pos);
						if(column == null){
							column = sheet1.getRow(row_pos).createCell(column_pos);
						}
						column.setCellValue(error_value); 
					}
				}else{ //export mode
					if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
						row = sheet1.getRow(row_pos+1);
						/*if(i==1){
								row = sheet1.getRow(row_pos);
							}*/
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :row_pos:"+(row_pos+1));
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :column_pos:"+column_pos);

						column = row.getCell(column_pos);
						if(column == null){
							column = sheet1.getRow(row_pos+1).createCell(column_pos);
						}
						column.setCellValue(error_value); 	
					}
				}
				//}
				//row_pos++;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillHeaderWithCellHSSF :JSONException: "+e.getMessage());
		}
	}

	/*public void FillReferenceWithCellHSSF(HSSFSheet sheet1, JSONArray filter_result, int input_row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillReferenceWithCellHSSF: filter_result: " + filter_result);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			String test_case_name = "";
			String exportDeviceName = "";
			int row_pos = input_row_pos;
			for(int i=0; i<filter_result.length(); i++){
				Row row = sheet1.getRow(row_pos);
				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}
				//for(int j=0; j<filter_result.length(); j++){
					result_json = filter_result.getJSONObject(i);
					device_name = result_json.getString("device_name");
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: device_name: "+device_name);

					exportDeviceName = getRealDeviceIDForExportMode(device_name);
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp: device_name: " + device_name+ " : "+ error_value);
					if(device_name.equals(exportDeviceName)){
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp -hit:");
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :row_pos:"+row_pos);
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-imp :column_pos:"+column_pos);
						column.setCellValue(error_value); 	
					}
				//}
				row_pos++;
			}
			//For export Mode
			row_pos = input_row_pos;
			for(int i=0; i<filter_result.length(); i++){
				Row row = sheet1.getRow(row_pos+1);
				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}
				//for(int j=0; j<filter_result.length(); j++){
					result_json = filter_result.getJSONObject(i);
					device_name = result_json.getString("device_name");
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp: device_name: "+device_name);

					exportDeviceName = getRealDeviceIDForExportMode(device_name);
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp: device_name: " + device_name+ " : "+ error_value);
					if(!device_name.equals(exportDeviceName)){
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp -hit:");
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :row_pos:"+row_pos);
						ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp :column_pos:"+column_pos);
						column.setCellValue(error_value); 	
					}
				//}
				row_pos++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillHeaderWithCellHSSF :JSONException: "+e.getMessage());
		}
	}*/

	public void FillHeaderWithCellHSSF(HSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillHeaderWithCellHSSF: filter_result: " + filter_result);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value = "";
			for(int i=0; i<filter_result.length(); i++){
				Row row = sheet1.getRow(row_pos);
				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}
				for(int j=0; j<filter_result.length(); j++){
					result_json = filter_result.getJSONObject(j);
					device_name = result_json.getString("device_name");
					ApplicationLauncher.logger.info("FillHeaderWithCellHSSF: device_name: "+device_name);

					device_name = getRealDeviceIDForExportMode(device_name);
					ApplicationLauncher.logger.info("FillHeaderWithCellHSSF: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					ApplicationLauncher.logger.info("FillHeaderWithCellHSSF: device_name: " + device_name+ " : "+ error_value);
					column.setCellValue(error_value); 	

				}
				row_pos++;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillHeaderWithCellHSSF :JSONException: "+e.getMessage());
		}
	}


	public ArrayList<String> getRefKWh(){
		ApplicationLauncher.logger.debug("getRefKWh : Entry: ");
		//JSONObject report_excel_config = MySQL_Controller.sp_getreport_excel_config("ConstantTest");
		JSONObject report_excel_config = MySQL_Controller.sp_getreport_excel_config(getSelectedReportProfile(),TestProfileType.ConstantTest.toString());
		ArrayList<String> saved_values = new ArrayList<String>();
		ArrayList<String> sec_values = new ArrayList<String>();
		try{
			JSONArray report_excel_arr = report_excel_config.getJSONArray("Report_Excel_Cells");
			ApplicationLauncher.logger.info("getRefKWh : report_excel_arr: " + report_excel_arr);
			if(report_excel_arr.length() != 0){
				JSONObject jobj = new JSONObject();
				String cell_type = "";
				String cell_value = "";
				for(int i=0; i<report_excel_arr.length();i++){
					jobj = report_excel_arr.getJSONObject(i);
					cell_type = jobj.getString("cell_type");
					cell_value = jobj.getString("cell_value");
					if(cell_type.equals(ConstantReport.EXCEL_BETA)){
						sec_values.add(cell_value);
					}
				}
			}
			saved_values.add(sec_values.get(5));
			saved_values.add(sec_values.get(6));
		}
		catch(JSONException e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("getRefKWh : JSONException: " + e.getMessage());
		}
		ApplicationLauncher.logger.info("getRefKWh : saved_values: " + saved_values);
		return saved_values;
	}

	public JSONArray FilterCONSTResults(String voltage, String current,
			String pf,String sel_power, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterCONSTResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			String pseudo_name = getConstPseudoName(voltage, current, pf, sel_power);

			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				//ApplicationLauncher.logger.info("FilterCONSTResults: test_case_name : " + test_case_name);

				//ApplicationLauncher.logger.debug("FilterCONSTResults: testname_wo_type : " + testname_wo_type);
				//ApplicationLauncher.logger.debug("FilterCONSTResults: pseudo_name : " + pseudo_name);

				if(pseudo_name.equals(testname_wo_type)){
					//ApplicationLauncher.logger.debug("FilterCONSTResults: test_case_name1: "+test_case_name);
					FilteredData.put(result_json);	
				}else if(pseudo_name.equals(testname_wo_type+".0")){
					//ApplicationLauncher.logger.debug("FilterCONSTResults: test_case_name2: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterCONSTResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}

	public String getConstPseudoName(String voltage, String current,
			String pf,String sel_power){
		String testcasename = voltage + "-" + pf + "-" + current + "-" + sel_power;
		ApplicationLauncher.logger.info("getConstPseudoName: testcasename: "+testcasename);
		return testcasename;
	}

	public void fillConstErrorValueXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		FillErrorValueXSSF(sheet1, filter_result,ConstantReport.CONST_TEMPL_ROW, start_col, meter_col);
	}

	public void fillConstErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power,int row_pos, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public void fillConstRefStValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power,int row_pos, int start_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		FillReferenceWithCellXSSF(sheet1, filter_result,row_pos, start_col);
	}

	public void fillConstErrorValueHSSF(HSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		FillErrorValueHSSF(sheet1, filter_result,ConstantReport.CONST_TEMPL_ROW, start_col, meter_col);
	}

	public void fillCt_Pt_valueXSSF(XSSFSheet sheet1, String datatype,int start_row, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray result_arr = new JSONArray();
		try {
			result_arr = result.getJSONArray("Results");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCt_Pt_valueXSSF: JSONException: "+e.getMessage());
		}
		FillErrorValueXSSF(sheet1, result_arr , start_row, start_col, meter_col);
	}

	public void fillCt_Pt_valueHSSF(HSSFSheet sheet1, String datatype,int start_row, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray result_arr = new JSONArray();
		try {
			result_arr = result.getJSONArray("Results");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCt_Pt_valueHSSF: JSONException: "+e.getMessage());
		}
		FillErrorValueHSSF(sheet1, result_arr , start_row, start_col, meter_col);
	}

	public Boolean ExportCREEP(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportCREEP: Entry ");
			file = new FileInputStream(new File(ConstantReport.CREEP_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*			HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/

			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.CREEP_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}

			result = FilterResultByTestType(result, ConstantApp.CREEP_ALIAS_NAME);
			String voltage = ConstantReport.CREEP_TEMPL_VOLTAGE;
			ApplicationLauncher.logger.debug("ExportCREEP: result: " + result);
			if(hssf_Format){
				status = PopulateCreepResultsHSSF(HSSF_Sheet, result,voltage);
			}else{
				status = PopulateCreepResultsXSSF(XSSF_Sheet, result,voltage);
			}
			file.close();
			//FileOutputStream out = 
			//		new FileOutputStream(new File("C:\\TAS_Network\\Procal_Excel\\CREEP.xls"));
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.CREEP_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportCREEP: FileNotFoundException: "+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for NoLoad Test not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for NoLoad Test not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for NoLoad Test. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportCREEP: IOException: "+e.getMessage());
		}
		return status;
	}

	public Boolean PopulateCreepResultsXSSF(XSSFSheet sheet1, JSONObject result, String voltage){
		ApplicationLauncher.logger.debug("PopulateCreepResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.CREEP_TEMPL_ROW;
		int meter_col = ConstantReport.CREEP_TEMPL_METER_COL;
		JSONArray filter_result = FilterCREEPResults(voltage,result);
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			fillCreepErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(0), meter_col);
			fillCreepErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(1),meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,ConstantReport.CREEP_TEMPL_CREEP_COLS.get(2), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos, ConstantReport.CREEP_TEMPL_CREEP_COLS.get(3), meter_col);
			fillCreepErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(4), meter_col);

			if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
				noLoadReportUpdateTesterName(sheet1);
				noLoadReportUpdateVoltage(sheet1,voltage);
				noLoadReportUpdateReportSerialNo(sheet1);
			}
		}
		return  ResultFoundStatus ;
	}

	public Boolean PopulateCreepResultsHSSF(HSSFSheet sheet1, JSONObject result, String voltage){
		ApplicationLauncher.logger.debug("PopulateCreepResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.CREEP_TEMPL_ROW;
		int meter_col = ConstantReport.CREEP_TEMPL_METER_COL;
		JSONArray filter_result = FilterCREEPResults(voltage,result);
		if(filter_result.length()!=0){

			ResultFoundStatus = true;
			FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
			fillCreepErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(0), meter_col);
			fillCreepErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(1),meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,ConstantReport.CREEP_TEMPL_CREEP_COLS.get(2), meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos, ConstantReport.CREEP_TEMPL_CREEP_COLS.get(3), meter_col);
			fillCreepErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltage, 
					ConstantReport.CREEP_TEMPL_CREEP_COLS.get(4), meter_col);
		}
		return  ResultFoundStatus ;
	}

	public JSONArray FilterCREEPResults(String voltage, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("TestReport: FilterCREEPResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";


			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				if((selectedRateOfVolt.equals(voltage))){
					ApplicationLauncher.logger.debug("FilterCREEPResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterCREEPResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}

	public void fillCreepErrorValueXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		FillErrorValueXSSF(sheet1, filter_result,ConstantReport.CREEP_TEMPL_ROW, start_col, meter_col);
	}



	public void fillCreepErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int row_pos, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public void fillCreepResultStatusCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int row_pos, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public void fillCreepErrorValueHSSF(HSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		FillErrorValueHSSF(sheet1, filter_result,ConstantReport.CREEP_TEMPL_ROW, start_col, meter_col);
	}

	public Boolean ExportSTA(JSONObject result){
		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			ApplicationLauncher.logger.debug("ExportSTA: Entry ");
			file = new FileInputStream(new File(ConstantReport.STA_TEMPL_FILE_LOCATION));
			TemplateFilePathExist = true;
			/*			HSSFWorkbook yourworkbook = new HSSFWorkbook(file);
			HSSFSheet sheet1 = yourworkbook.getSheetAt(0);*/
			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				file = new FileInputStream(new File(ConstantReport.STA_TEMPL_FILE_LOCATION));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);
			String voltage = ConstantReport.STA_TEMPL_VOLTAGE;
			String current = ConstantReport.STA_TEMPL_CURRENT;
			ApplicationLauncher.logger.debug("ExportCREEP: result: " + result);
			if(hssf_Format){
				status = PopulateSTAResultsHSSF(HSSF_Sheet, result,voltage,current);
			}else{
				status = PopulateSTAResultsXSSF(XSSF_Sheet, result,voltage,current);
			}
			file.close();
			//FileOutputStream out = 
			//		new FileOutputStream(new File("C:\\TAS_Network\\Procal_Excel\\STA.xls"));
			String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			String file_name = getSaveFileName(ConstantReport.STA_TEMPL_FILE_LOCATION);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}
			/*FileOutputStream out = 
					new FileOutputStream(new File(file_path + file_name));
			yourworkbook.write(out);*/
			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ExportSTA: FileNotFoundException: "+e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Starting Current not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Starting Current not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Starting Current. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("ExportSTA: IOException: "+e.getMessage());
		}
		return status;
	}

	public Boolean PopulateSTAResultsXSSF(XSSFSheet sheet1, JSONObject result, String voltage, String current){
		ApplicationLauncher.logger.debug("PopulateSTAResultsXSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.STA_TEMPL_ROW;
		int meter_col = ConstantReport.STA_TEMPL_METER_COL;
		JSONArray filter_result = FilterSTAResults(voltage,current,result);
		if(filter_result.length()!=0){
			ResultFoundStatus = true;
			FillMeterColumnXSSF(sheet1, filter_result,row_pos, meter_col);
			fillSTAErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage, current,
					ConstantReport.STA_TEMPL_STA_COLS.get(0), meter_col);
			fillSTAErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage, current, 
					ConstantReport.STA_TEMPL_STA_COLS.get(1), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,ConstantReport.STA_TEMPL_STA_COLS.get(2), meter_col);
			fillCt_Pt_valueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos, ConstantReport.STA_TEMPL_STA_COLS.get(3), meter_col);
			fillSTAErrorValueXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_STA_TIME,voltage, current, 
					ConstantReport.STA_TEMPL_STA_COLS.get(4), meter_col);
			if(ProcalFeatureEnable.REPORT_SUPPORTING_DATA_POPULATE_ENABLED){
				startingCurrentReportUpdateTesterName(sheet1);
				startingCurrentReportUpdateVoltage(sheet1,voltage);
				startingCurrentReportUpdateCurrent(sheet1,current);
				startingCurrentReportUpdateReportSerialNo(sheet1);
			}
		}
		return ResultFoundStatus;
	}

	public Boolean PopulateSTAResultsHSSF(HSSFSheet sheet1, JSONObject result, String voltage, String current){
		ApplicationLauncher.logger.debug("PopulateSTAResultsHSSF: Entry ");
		boolean ResultFoundStatus = false;
		int row_pos = ConstantReport.STA_TEMPL_ROW;
		int meter_col = ConstantReport.STA_TEMPL_METER_COL;
		JSONArray filter_result = FilterSTAResults(voltage,current,result);
		if(filter_result.length()!=0){
			ResultFoundStatus = true;
			FillMeterColumnHSSF(sheet1, filter_result,row_pos, meter_col);
			fillSTAErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,voltage, current,
					ConstantReport.STA_TEMPL_STA_COLS.get(0), meter_col);
			fillSTAErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,voltage, current, 
					ConstantReport.STA_TEMPL_STA_COLS.get(1), meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_CTR,row_pos,ConstantReport.STA_TEMPL_STA_COLS.get(2), meter_col);
			fillCt_Pt_valueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PTR,row_pos, ConstantReport.STA_TEMPL_STA_COLS.get(3), meter_col);
			fillSTAErrorValueHSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_STA_TIME,voltage, current, 
					ConstantReport.STA_TEMPL_STA_COLS.get(4), meter_col);
		}
		return ResultFoundStatus;
	}

	public JSONArray FilterSTAResults(String voltage, String current, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.info("FilterCONSTResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");

			JSONObject result_json = new JSONObject();
			String test_case_name = "";


			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				//ApplicationLauncher.logger.info("FilterVUResults: testname_wo_type : " + testname_wo_type);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				selectedRateOfCurrent = test_params[1];
				//ApplicationLauncher.logger.info("FilterVUResults: selectedRateOfVolt: " + selectedRateOfVolt);
				//ApplicationLauncher.logger.info("FilterVUResults: voltage: " + voltage);



				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current))){
					ApplicationLauncher.logger.debug("FilterSTAResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterSTAResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}

	public void fillSTAErrorValueXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		FillErrorValueXSSF(sheet1, filter_result,ConstantReport.STA_TEMPL_ROW, start_col, meter_col);
	}

	public void fillSTA_ErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col,int row_pos, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}


	public void fillSTA_ResultStatusCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col, int row_pos,int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		//ApplicationLauncher.logger.debug("fillSTA_ResultStatusCustomReportXSSF: result: " + result);
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		//ApplicationLauncher.logger.debug("fillSTA_ResultStatusCustomReportXSSF: filter_result: " + filter_result);
		fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public void fillSTAErrorValueHSSF(HSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		FillErrorValueHSSF(sheet1, filter_result,ConstantReport.STA_TEMPL_ROW, start_col, meter_col);
	}

	public void setMeterNames(JSONArray value){
		MeterNames = value;
	}

	public JSONArray getMeterNames(){
		return MeterNames;
	}

	public String getDeviceNameWithRackIDForExportMode(String input_rackid){
		JSONArray meternames = getMeterNames();
		String device_rack_id = "";

		JSONObject result_json = new JSONObject();
		String  device_with_rack = "";
		String rack_id = "";
		try {
			for(int i=0; i<meternames.length(); i++){
				result_json = meternames.getJSONObject(i);
				device_with_rack = result_json.getString("error_value");

				String[] test_params = device_with_rack.split("/");
				rack_id = test_params[1];
				if(rack_id.equals(input_rackid)){
					device_rack_id = ConstantApp.EXPORT_MODE_ALIAS_NAME+device_with_rack;
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDeviceNameWithRackIDForExportMode: JSONException: "+e.getMessage());
		}
		return device_rack_id;
	}

	public String convertRackIdForReportExportMode(String inputRackId){
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
			if(Integer.parseInt(inputRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
				String convertedRackId = String.valueOf(Integer.parseInt(inputRackId) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
				return convertedRackId;

			}
		}
		return inputRackId;
	}

	public String getDeviceNameWithRackID(String input_rackid){
		ApplicationLauncher.logger.debug("getDeviceNameWithRackID: Entry");
		JSONArray meternames = getMeterNames();
		//if(Integer.parseInt(input_rackid)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
		//	input_rackid = String.valueOf(Integer.parseInt(input_rackid) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
		//}
		input_rackid = convertRackIdForReportExportMode(input_rackid);
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: input_rackid: " + input_rackid);
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: meternames: "+ meternames);
		String device_rack_id = "";

		JSONObject result_json = new JSONObject();
		String  device_with_rack = "";
		String rack_id = "";
		try {
			for(int i=0; i<meternames.length(); i++){
				result_json = meternames.getJSONObject(i);
				device_with_rack = result_json.getString("error_value");
				//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: device_with_rack: "+ device_with_rack);
				String[] test_params = device_with_rack.split("/");
				rack_id = test_params[1];
				if(rack_id.equals(input_rackid)){
					device_rack_id = device_with_rack;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDeviceNameWithRackID: JSONException: "+e.getMessage());
		}
		return device_rack_id;
	}

	public String getDeviceNameWithOutRackID(String input_rackid){
		ApplicationLauncher.logger.debug("getDeviceNameWithOutRackID: Entry");
		JSONArray meternames = getMeterNames();
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: input_rackid: " + input_rackid);
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: meternames: "+ meternames);
		String device_id = "";

		JSONObject result_json = new JSONObject();
		String  device_with_rack = "";
		String rack_id = "";
		try {
			for(int i=0; i<meternames.length(); i++){
				result_json = meternames.getJSONObject(i);
				device_with_rack = result_json.getString("error_value");

				String[] test_params = device_with_rack.split("/");
				rack_id = test_params[1];
				if(rack_id.equals(input_rackid)){
					//device_id = device_with_rack;
					device_id = test_params[0];
					return device_id;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDeviceNameWithOutRackID: JSONException: "+e.getMessage());
		}
		return device_id;
	}


	// Mode represents the unit that is currently being edited.
	// For convenience expose methods for incrementing and decrementing that
	// unit, and for selecting the appropriate portion in a spinner's editor
	enum Mode {

		HOURS {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusHours(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int index = spinner.getEditor().getText().indexOf(':');
				spinner.getEditor().selectRange(0, index);
			}
		},
		MINUTES {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusMinutes(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int hrIndex = spinner.getEditor().getText().indexOf(':');
				int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
				spinner.getEditor().selectRange(hrIndex+1, minIndex);
			}
		},
		SECONDS {
			@Override
			LocalTime increment(LocalTime time, int steps) {
				return time.plusSeconds(steps);
			}
			@Override
			void select(Spinner<LocalTime> spinner) {
				int index = spinner.getEditor().getText().lastIndexOf(':');
				spinner.getEditor().selectRange(index+1, spinner.getEditor().getText().length());
			}
		};
		abstract LocalTime increment(LocalTime time, int steps);
		abstract void select(Spinner<LocalTime> spinner);
		LocalTime decrement(LocalTime time, int steps) {
			return increment(time, -steps);
		}
	}

	// Property containing the current editing mode:

	private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.HOURS) ;

	public ObjectProperty<Mode> modeProperty() {
		return mode;
	}

	public final Mode getMode() {
		return modeProperty().get();
	}

	public final void setMode(Mode mode) {
		modeProperty().set(mode);
	}


	public void initializeSpinner(LocalTime time, Spinner<LocalTime> spinner) {
		spinner.setEditable(true);

		// Create a StringConverter for converting between the text in the
		// editor and the actual value:

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

			@Override
			public String toString(LocalTime time) {
				return formatter.format(time);
			}

			@Override
			public LocalTime fromString(String string) {
				String[] tokens = string.split(":");
				int hours = getIntField(tokens, 0);
				int minutes = getIntField(tokens, 1) ;
				int seconds = getIntField(tokens, 2);
				int totalSeconds = (hours * 60 + minutes) * 60 + seconds ;
				return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
			}

			private int getIntField(String[] tokens, int index) {
				if (tokens.length <= index || tokens[index].isEmpty()) {
					return 0 ;
				}
				return Integer.parseInt(tokens[index]);
			}

		};

		// The textFormatter both manages the text <-> LocalTime conversion,
		// and vetoes any edits that are not valid. We just make sure we have
		// two colons and only digits in between:

		TextFormatter<LocalTime> textFormatter = new TextFormatter<LocalTime>(localTimeConverter, LocalTime.now(), c -> {
			String newText = c.getControlNewText();
			if (newText.matches("[0-9]{0,2}:[0-9]{0,2}:[0-9]{0,2}")) {
				return c ;
			}
			return null ;
		});

		// The spinner value factory defines increment and decrement by
		// delegating to the current editing mode:

		SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
			{
				setConverter(localTimeConverter);
				setValue(time);
			}

			@Override
			public void decrement(int steps) {
				setValue(mode.get().decrement(getValue(), steps));
				mode.get().select(spinner);
			}

			@Override
			public void increment(int steps) {
				setValue(mode.get().increment(getValue(), steps));
				mode.get().select(spinner);
			}

		};

		spinner.setValueFactory(valueFactory);
		spinner.getEditor().setTextFormatter(textFormatter);

		// Update the mode when the user interacts with the editor.
		// This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
		// could result in incorrect state. Directly observing the caretPostion
		// didn't work well though; getting that to work properly might be
		// a better approach in the long run.
		spinner.getEditor().addEventHandler(InputEvent.ANY, e -> {
			int caretPos = spinner.getEditor().getCaretPosition();
			int hrIndex = spinner.getEditor().getText().indexOf(':');
			int minIndex = spinner.getEditor().getText().indexOf(':', hrIndex + 1);
			if (caretPos <= hrIndex) {
				mode.set( Mode.HOURS );
			} else if (caretPos <= minIndex) {
				mode.set( Mode.MINUTES );
			} else {
				mode.set( Mode.SECONDS );
			}
		});

		DateTimeFormatter consoleFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
		spinner.valueProperty().addListener((obs, oldTime, newTime) -> 
		ApplicationLauncher.logger.info("initializeSpinner:"+consoleFormatter.format(newTime)));

		// When the mode changes, select the new portion:
		mode.addListener((obs, oldMode, newMode) -> newMode.select(spinner));

	}

	public void enableBusyLoadingScreen() {//long time_in_seconds) {
		ApplicationLauncher.logger.info("TestReportController: enableBusyLoadingScreen: entry");

		//FXMLLoader loader = new FXMLLoader(
		//		getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
		Parent nodeFromFXML = null;
		try {
			nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
			ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("TestReportController: enableBusyLoadingScreen: Exception: "+e.getMessage());
		}
	}

	public void disableBusyLoadingScreen(){
		BusyLoadingController.removeBusyLoadingScreenOverlay();
	}

	private Parent getNodeFromFXML(String url) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
		Parent parentNode = loader.load();

		ApplicationLauncher.logger.info("TestReportController: Loaded property UI: " + parentNode);
		//testPropertyController = loader.getController();
		return parentNode;
	}

	public static String getSelectedExecutionTesterName() {
		return selectedExecutionTesterName;
	}

	public static void setSelectedExecutionTesterName(String selectedExecutionTesterName) {
		TestReportController.selectedExecutionTesterName = selectedExecutionTesterName;
	}

	public JSONObject getMeterProfileData() {
		return meterProfileData;
	}

	public void setMeterProfileData(JSONObject meterProfileData) {
		this.meterProfileData = meterProfileData;
	}

	public static String getSelectedReportSerialNo() {
		return selectedReportSerialNo;
	}

	public static void setSelectedReportSerialNo(String selectedReportSerialNo) {
		TestReportController.selectedReportSerialNo = selectedReportSerialNo;
	}

	public static String getSelectedProjectExecutedDate() {
		return selectedProjectExecutedDate;
	}

	public static String getSelectedProjectExecutedTime() {
		return selectedProjectExecutedTime;
	}

	public static void setSelectedProjectExecutedDate(String selectedProjectExecutedDate) {
		TestReportController.selectedProjectExecutedDate = selectedProjectExecutedDate;
	}

	public static void setSelectedProjectExecutedTime(String selectedProjectExecutedTime) {
		TestReportController.selectedProjectExecutedTime = selectedProjectExecutedTime;
	}

	public String getSelectedMeterSerialNo() {
		return selectedMeterSerialNo;
	}

	public void setSelectedMeterSerialNo(String selectedMeterSerialNo) {
		this.selectedMeterSerialNo = selectedMeterSerialNo;
	}

	public boolean isIndividualMeterReportSelected() {
		return individualMeterReportSelected;
	}

	public void setIndividualMeterReportSelected(boolean individualMeterReportSelected) {
		this.individualMeterReportSelected = individualMeterReportSelected;
	}

	@FXML
	public void cmbBxReportProfileOnChange() {
		//ref_cmbBxReportProfile.getSelectionModel().select(ConstantConfig.DefaultReportProfileDisplay);
		setSelectedReportProfile(ref_cmbBxReportProfile.getSelectionModel().getSelectedItem().toString());
		//if(cmbBxReportProfile.getSelectionModel().getSelectedItem().toString().equals(ConstantAppConfig.DefaultReportProfileDisplay)){
		if(cmbBxReportProfile.getSelectionModel().getSelectedItem().toString().equals(ConstantApp.StandardReportProfileDisplay)){

			titledPaneReportTypes.setDisable(false);
		}else{
			titledPaneReportTypes.setDisable(true);
		}
	}
	
	

	public static int getSerialNumberMaxCount() {
		return serialNumberMaxCount;
	}

	public static void setSerialNumberMaxCount(int serialNumber) {
		TestReportController.serialNumberMaxCount = serialNumber;
	}

	public void clearMeterDeviceOverAllStatus() {
		meterDeviceOverAllStatus.clear();
	}
	public String getMeterDeviceOverAllStatus(String meterSerialNoKey) {
		return meterDeviceOverAllStatus.get(meterSerialNoKey);
	}

	public void addMeterDeviceOverAllStatus(String meterSerialNoKey, String meterOverAllStatus) {
		meterDeviceOverAllStatus.put(meterSerialNoKey,meterOverAllStatus);  
	}

	public void updateMeterDeviceOverAllStatus(String meterSerialNoKey, String meterOverAllStatus) {
		meterDeviceOverAllStatus.put(meterSerialNoKey,meterOverAllStatus);  
	}

	public static String getSelectedCustomerRefNo() {
		return selectedCustomerRefNo;
	}

	public static void setSelectedCustomerRefNo(String selectedCustomerRefNo) {
		TestReportController.selectedCustomerRefNo = selectedCustomerRefNo;
	}


	public String getDutMeterDataPreviousPageLastUpdatedRackId() {
		return dutMeterDataPreviousPageLastUpdatedRackId;
	}


	public void setDutMeterDataPreviousPageLastUpdatedRackId(String dutPreviousPageLastUpdatedDeviceRackId) {
		this.dutMeterDataPreviousPageLastUpdatedRackId = dutPreviousPageLastUpdatedDeviceRackId;
	}


	public int getDutMeterDataPreviousPageUpdatedPageNumber() {
		return dutMeterDataPreviousPageUpdatedPageNumber;
	}


	public void setDutMeterDataPreviousPageUpdatedPageNumber(int dutMeterDataPreviousPageUpdatedPageNumber) {
		this.dutMeterDataPreviousPageUpdatedPageNumber = dutMeterDataPreviousPageUpdatedPageNumber;
	}

	public static ReportProfileManageService getReportProfileManageService() {
		return reportProfileManageService;
	}

	public static void setReportProfileManageService(ReportProfileManageService rptProfileManageService) {
		reportProfileManageService = rptProfileManageService;
	}


	public static String getSelectedEnergyFlowMode() {
		return selectedEnergyFlowMode;
	}


	public static void setSelectedEnergyFlowMode(String selectedEnergyFlowMode) {
		TestReportController.selectedEnergyFlowMode = selectedEnergyFlowMode;
	}


	public static boolean isSelectedExecutionMainCt() {
		return selectedExecutionMainCt;
	}


	public static void setSelectedExecutionMainCt(boolean selectedExecutionMainCt) {
		TestReportController.selectedExecutionMainCt = selectedExecutionMainCt;
	}


	public static boolean isSelectedExecutionNeutralCt() {
		return selectedExecutionNeutralCt;
	}


	public static void setSelectedExecutionNeutralCt(boolean selectedExecutionNeutralCt) {
		TestReportController.selectedExecutionNeutralCt = selectedExecutionNeutralCt;
	}


	public List<ReportProfileManage> getActiveReportProfileDatabaseList() {
		return activeReportProfileDatabaseList;
	}


	public void setActiveReportProfileDatabaseList(List<ReportProfileManage> activeReportProfileDatabaseList) {
		this.activeReportProfileDatabaseList = activeReportProfileDatabaseList;
	}

	public MultiValuedMap<String, String> getGroupProfileNameHashMap() {
		return groupProfileNameHashMap;
	}

	public void setGroupProfileNameHashMap(MultiValuedMap<String, String> groupProfileNameHashMap) {
		this.groupProfileNameHashMap = groupProfileNameHashMap;
	}

	@FXML
	public void cmbBxReportProfileGroupOnchange() {
		ApplicationLauncher.logger.debug("cmbBxReportProfileGroupOnchange: Entry");

		try{
			String lastSelectedGroupName = ref_cmbBxReportProfileGroup.getSelectionModel().getSelectedItem().toString();

			ApplicationLauncher.logger.info("cmbBxReportProfileGroupOnchange: lastSelectedGroupName : " + lastSelectedGroupName);
			//ApplicationLauncher.logger.info("loadReportProfileList: groupNameHashMap : " +groupNameHashMap);

			//String profileNameList = groupNameHashMap.entries().stream().filter(e -> e.getKey().equals(lastProfileGroupName)).findFirst().get().getValue();


			Collection<String> reportFileNameList = getGroupProfileNameHashMap().get(lastSelectedGroupName);

			ref_cmbBxReportProfile.getItems().clear();
			reportFileNameList.stream().forEach( value -> {

				ref_cmbBxReportProfile.getItems().add(value);
			} );

			ref_cmbBxReportProfile.getSelectionModel().selectLast();
		}catch(Exception e){
			ApplicationLauncher.logger.error("cmbBxReportProfileGroupOnchange: Exception : " + e.getMessage());
		}

	}
	
	public static HashMap<String, String> getDeviceIdSerialNumberHashMap() {
		return deviceIdSerialNumberHashMap;
	}


	public void setDeviceIdSerialNumberHashMap(HashMap<String, String> deviceIdSerialNunberHashMap) {
		deviceIdSerialNumberHashMap = deviceIdSerialNunberHashMap;
	}

}
