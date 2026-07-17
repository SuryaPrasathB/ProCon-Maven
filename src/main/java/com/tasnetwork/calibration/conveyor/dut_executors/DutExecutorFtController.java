package com.tasnetwork.calibration.conveyor.dut_executors;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigLoader;
import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.bay.ft.Ft;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.pallet.PalletBayTestPalletActive_CheckBoxValueFactory;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.setting.BusyLoadingController;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.model.DutExecutionResult;
import com.tasnetwork.spring.orm.model.MeterResultSummary;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

import antlr.StringUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DutExecutorFtController implements Initializable{

	private Logger myBaylogger = Ft.logger;
	DutCmdIndividualDeviceExecutor commandExecutor = new DutCmdIndividualDeviceExecutor();
	 
	private String myBayKey = ConstantConveyor.FT_BAY_KEY;
	private CountdownTimer countdownTimer;
	private CountUpTimer countUpTimer;
	public String currentProjectName;
	public  String selectedDeployment_ID = "";
	public List<String> deploymentIdList = new ArrayList<String>();
	public  List<String> projectNameList = new ArrayList<String>();
	public  int selectedProjectIndex = -1;
	private static boolean userAborted = false;
	Timer selectProjectOnchangeTimer;
	Timer startOnClickTimer;
	public static Map<String,Boolean> deviceMountedMap = new LinkedHashMap<String,Boolean>();
	public Map<String,DutCmdManager> deviceTypeDutCmdManagerMap = new HashMap<String,DutCmdManager>();
	
	@FXML
	private ComboBox cmbxProjectName;//cmbBoxSelectProject;
	public static ComboBox ref_cmbxProjectName;
	
	@FXML
	private ComboBox cmbxBayType;
	public static ComboBox ref_cmbxBayType;
	
	@FXML
	private Button btnStartExecution;
	public static Button ref_btnStartExecution;
	
	@FXML
	private Button btnStopExecution;
	public static Button ref_btnStopExecution;
	
	@FXML
	private ProgressBar pBarTestExecution;
	public static ProgressBar ref_pBarTestExecution;
	
	@FXML
	private TextField txtClusterName;
	public static TextField ref_txtClusterName;
	
	@FXML
	private TextField txtEachExecutionTime;
	public static TextField ref_txtEachExecutionTime;
	
	@FXML
	private TextField txtTotalExecutionTime;
	public static TextField ref_txtTotalExecutionTime;
	
	@FXML
    private TableColumn<DutExecutionResult, String> colExePalletDistinctId;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP1;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP2;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP3;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP4;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP5;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeResultP6;

    @FXML
    private TableColumn<DutExecutionResult, Integer> colExeSerialNo;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeStatus;

    @FXML
    private TableColumn<DutExecutionResult, String> colExeTestPointName;

    @FXML
    private TableView<DutExecutionResult> tvFtExecutor;
    
    private static TableView<DutExecutionResult> ref_tvFtExecutor;
    
    
    
    
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		refAssignment();
		guiInit();
		dataInit();
	}

	private void dataInit() {
		// TODO Auto-generated method stub
		/*DutExecutionResult ftExecutionResult1 = new DutExecutionResult();
		ftExecutionResult1.setSerialNo(1);
		ftExecutionResult1.setTestPointName("Tp1");
		ftExecutionResult1.setStatus("TBD");
		ftExecutionResult1.setResultPosition1("r1 P");
		ftExecutionResult1.setResultPosition2("r2 P");
		ftExecutionResult1.setResultPosition3("r3 P");
		ftExecutionResult1.setResultPosition4("r4 P");
		ftExecutionResult1.setResultPosition5("r5 P");
		ftExecutionResult1.setResultPosition6("r6 P");
		ftExecutionResult1.setPalletDistinctId("Pallet1_01");
		ref_tvFtExecutor.getItems().add(ftExecutionResult1);
		*/
		
		//getManageDeployData();
		updateProjectListinGUI();
	}
	
	
	/*public List<String> getManageDeployData() {

		ApplicationLauncher.logger.debug("getManageDeployData : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		// ApplicationLauncher.logger.debug("getColNamesForErrorDisplay:
		// ERROR_DISPLAY_COLUMN_LIST:"+ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST);
		// col = ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST;

		// resultjson = DisplayDataObj.getDeployedDevicesJson();//
		// MySQL_Controller.sp_getdeploy_devices(project_name);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
		long currentTime = calcEpoch(timeStamp);
		long deployedTimeMaxSearchLimit = currentTime- (ConstantApp.NUMBER_OF_SECONDS_IN_A_DAY*ConstantAppConfig.DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS);
		resultjson = MySQL_Controller.sp_getdeploy_manage_active(deployedTimeMaxSearchLimit);
		ApplicationLauncher.logger.debug("getManageDeployData : resultjson: " + resultjson);
		try {
			int no_of_devices = resultjson.getInt("No_of_deployment");

			JSONArray arr = resultjson.getJSONArray("Deployment");
			//String equipmentSerialNumber = "";
			String deploymentID = "";
			String project_name = "";
			String mct_mode = "N";
			String nct_mode = "N";
			String energyFlowMode = "";
			String autoDeplyEnabled = "";
			//String deploy_mode = "";

			List<String> project_NameList = new ArrayList<String>();
			List<String> deployment_IdList = new ArrayList<String>();
			List<String> localEnergyFlowModeList = new ArrayList<String>();
			List<Boolean> localAutoDeployEnabledList = new ArrayList<Boolean>();
			//List<String> equipment_SerialNoList = new ArrayList<String>();
			//List<String> deployModeList = new ArrayList<String>();
			//clearEnergyFlowModeList();
			for (int i = 0; i < arr.length(); i++) {

				deploymentID = arr.getJSONObject(i).getString("deployment_id");
				//equipmentSerialNumber = arr.getJSONObject(i).getString("equipment_serial_no");
				mct_mode = arr.getJSONObject(i).getString("main_ct_mode");
				nct_mode = arr.getJSONObject(i).getString("neutral_ct_mode");
				project_name = arr.getJSONObject(i).getString("project_name");
				ApplicationLauncher.logger.debug("getManageDeployData : project_name: " + project_name);
				energyFlowMode = arr.getJSONObject(i).getString("energy_flow_mode"); 
				autoDeplyEnabled = arr.getJSONObject(i).getString("auto_deploy_enabled"); 
								if ((ct_mode.equals("Y")) && (pt_mode.equals("Y"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_PT_AND_CT_MODE;
				} else if ((ct_mode.equals("N")) && (pt_mode.equals("Y"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_PT_MODE;
				} else if ((ct_mode.equals("Y")) && (pt_mode.equals("N"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_CT_MODE;
				}

				deployment_IdList.add(deploymentID);
				//equipment_SerialNoList.add(equipmentSerialNumber);
				project_NameList.add(project_name);
				localEnergyFlowModeList.add(energyFlowMode);
				if(autoDeplyEnabled.equals("Y")) {
					localAutoDeployEnabledList.add(true);
				}else {
					localAutoDeployEnabledList.add(false);
				}
				//deployModeList.add(deploy_mode);

			}

			setProjectNameList(project_NameList);
			setDeploymentIdList(deployment_IdList);
			setEnergyFlowModeList(localEnergyFlowModeList);
			setAutoDeployEnabledList(localAutoDeployEnabledList);
			//setEquipmentSerialNoList(equipment_SerialNoList);
			//setDeployModeList(deployModeList);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getManageDeployData: JSONException: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("getManageDeployData: col:" + col);
		ApplicationLauncher.logger.debug("getManageDeployData: getProjectNameList: " + getProjectNameList());
		return col;
	}*/
	
	
	public static long calcEpoch(String Date_time){


		long epoch = 0;
		//String str = "2014-07-04 04:05:10";   // UTC
		String str = Date_time;   // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch: ParseException: "+e.getMessage());
		}
		epoch = datenew.getTime() /1000;

		return epoch;
	}
	
	public void updateProjectListinGUI() {
		ApplicationLauncher.logger.debug("updateProjectListinGUI: Entry");
		ApplicationLauncher.logger.debug("updateProjectListinGUI: getProjectNameList: " + getProjectNameList());
		// for(int i=0; i<getEquipmentSerialNoList().size();i++) {
		List<String> myProjectNameList = new ArrayList<String>();
		String configuredProjectName = DeviceDataManagerController.getConveyorConfigParsedKey().getFtDutCmdTestProjectName();
		if(configuredProjectName.endsWith(".*")) {
			configuredProjectName = configuredProjectName.replace(".*", "");
			List<DutCommand> dutCommandList = MySqlServiceManager.getDutCommandService().findByProjectNameStartsWithCaseInsensitive(configuredProjectName);
			myProjectNameList = dutCommandList.stream()
				.map(e->e.getProjectName())
				.distinct()
				.collect(Collectors.toList());
		}else {
			myProjectNameList.add(DeviceDataManagerController.getConveyorConfigParsedKey().getFtDutCmdTestProjectName());
		}
		if (myProjectNameList.size() > 0) {
			ref_cmbxProjectName.getItems().add("Select");
			ref_cmbxProjectName.getItems().addAll(myProjectNameList);
			ref_cmbxProjectName.getSelectionModel().select(0);
			/*if (getSelectedProjectIndex() == -1) {
				setSelectedProjectIndex(0);

			} else {
				ref_cmbxProjectName.getSelectionModel().select(1);
			}*/

		}

	}
	
	@FXML	
	public void cmbxBayTypeOnChange() {
		
		Platform.runLater(()->{
			String myBayKey = ref_cmbxBayType.getSelectionModel().getSelectedItem().toString();
			//ApplicationLauncher.logger.debug("cmbxBayTypeOnChange: myBayKey: " +myBayKey);
			TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(myBayKey);
			String clusterName = terminalBayProfile.getClusterId() + " " +terminalBayProfile.getClusterName();

			ref_txtClusterName.setText(clusterName);
			
			refreshDeviceMountedMap( terminalBayProfile);
		});
	}
	
	public void refreshDeviceMountedMap(TerminalProfileSetting terminalBayProfile) {
		
		getDeviceMountedMap().clear();
		
		String positionIdAsList = terminalBayProfile.getPositionIdAsList();
		List<Integer> positionIdList =  Arrays.stream(positionIdAsList.split(","))
                .map(String::trim)
                .map(e->Integer.parseInt(e))
                .collect(Collectors.toList());
		String positionIdToBeSkippedAsList = "";
		if(terminalBayProfile.getPositionTobeSkippedAsList()!=null) {
			positionIdToBeSkippedAsList = terminalBayProfile.getPositionTobeSkippedAsList();
		}
		List<Integer> positionIdToBeSkippedList = new ArrayList<Integer>() ;
		if(!positionIdToBeSkippedAsList.isEmpty()) {
			positionIdToBeSkippedList = Arrays.stream(positionIdToBeSkippedAsList.split(","))
            .map(String::trim)
            .map(e->Integer.parseInt(e))
            .collect(Collectors.toList());
		}
		
		
		for(int i=0; i<positionIdList.size();i++) {
			if(!positionIdToBeSkippedList.contains(positionIdList.get(i))) {
				getDeviceMountedMap().put(String.format("%02d", positionIdList.get(i)), true);
			}
		}
		
		for (Map.Entry<String, Boolean> entry : getDeviceMountedMap().entrySet()) {
			ApplicationLauncher.logger.debug("refreshDeviceMountedMap: Device: " + entry.getKey() + ", Mounted: " + entry.getValue());
		}
	}
	
	private void updateExecutionTimeDisplay(String formattedTime) {
        Platform.runLater(() -> {
            ref_txtEachExecutionTime.setText(formattedTime);
        });
    }
	
	private void updateElapsedTimeDisplay(String formattedTime) {
        Platform.runLater(() -> {
            ref_txtTotalExecutionTime.setText( formattedTime);
        });
    }

	private void guiInit() {
		// TODO Auto-generated method stub
		
		countdownTimer = new CountdownTimer(this::updateExecutionTimeDisplay);
		countUpTimer = new CountUpTimer(this::updateElapsedTimeDisplay);
		ref_btnStopExecution.setDisable(true);
		String myBayKey = getMyBayKey();//ConstantConveyor.FT_BAY_KEY;
		ref_cmbxBayType.getItems().addAll(ConstantConveyor.STATE_SEQUENCE_LIST);
		ref_cmbxBayType.getSelectionModel().select(myBayKey);
		
		TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(myBayKey);

		
		String clusterName = terminalBayProfile.getClusterId() + " " +terminalBayProfile.getClusterName();

		ref_txtClusterName.setText(clusterName);
		
		refreshDeviceMountedMap(terminalBayProfile) ;
		
		colExeSerialNo.setCellValueFactory(data -> data.getValue().serialNoProperty().asObject());	
		colExeSerialNo.setStyle( "-fx-alignment: CENTER;");
		colExeTestPointName.setCellValueFactory(data -> data.getValue().testPointNameProperty());

		colExeStatus.setCellValueFactory(data -> data.getValue().testPointExecutionStatusProperty());

		colExeResultP1.setCellValueFactory(data -> data.getValue().resultPosition1Property());
		colExeResultP1.setStyle( "-fx-alignment: CENTER;");
		colExeResultP1.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//CurrentValue = CurrentValue.substring(ConstantReport.REPORT_POPULATE_WFR.length(),CurrentValue.length());
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}/*else if (item.startsWith("✓")) {
	                    setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
	                } else if (item.startsWith("✗")) {
	                    setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
	                } else if (item.startsWith("⚠")) {
	                    setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
	                } else if (item.equals("Pending")) {
	                    setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
	                }*/
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		
		
		colExeResultP2.setCellValueFactory(data -> data.getValue().resultPosition2Property());
		colExeResultP2.setStyle( "-fx-alignment: CENTER;");
		colExeResultP2.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		colExeResultP3.setCellValueFactory(data -> data.getValue().resultPosition3Property());
		colExeResultP3.setStyle( "-fx-alignment: CENTER;");
		colExeResultP3.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//CurrentValue = CurrentValue.substring(ConstantReport.REPORT_POPULATE_WFR.length(),CurrentValue.length());
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		colExeResultP4.setCellValueFactory(data -> data.getValue().resultPosition4Property());
		colExeResultP4.setStyle( "-fx-alignment: CENTER;");
		colExeResultP4.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		colExeResultP5.setCellValueFactory(data -> data.getValue().resultPosition5Property());
		colExeResultP5.setStyle( "-fx-alignment: CENTER;");
		colExeResultP5.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		colExeResultP6.setCellValueFactory(data -> data.getValue().resultPosition6Property());
		colExeResultP6.setStyle( "-fx-alignment: CENTER;");
		colExeResultP6.setCellFactory(param -> new TableCell<DutExecutionResult, String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				// Debug logging
				// ApplicationLauncher.logger.info("meterResultSummaryGuiInit: colMsumTestTypeFt: item=" + item + ", empty=" + empty);

				if (empty || item == null) {
					setText(null);
					setStyle(""); // Reset style for empty cells
					return;
				}

				String CurrentValue = (String)item;


				try{        

					if (CurrentValue.contains(ConstantReport.REPORT_POPULATE_WFR) ) {	

						//this.setStyle("-fx-background-color: yellow;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: blue;");
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
					} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
						//this.setStyle("-fx-background-color: green;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: green;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
					}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) { //if (CurrentValue.startsWith("F ")) {	fhngvb
						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: red;");
						//if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){

							CurrentValue = CurrentValue.substring(2,CurrentValue.length());
						//}
						//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
					//this.setStyle("-fx-alignment: CENTER;-fx-background-color: red;");
					this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");
					if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
						CurrentValue = CurrentValue.substring(2,CurrentValue.length());
					}
					//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
					}else  if (CurrentValue.isEmpty()){	
						this.setStyle("-fx-alignment: CENTER;-fx-text-fill: black;");

						//this.setStyle("-fx-alignment: CENTER;-fx-background-color: transparent;");
						//ApplicationLauncher.logger.debug("insertTableValues : Background color transparent");
					}
				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("TestReportController: updateItem: Exception:"+e.getMessage());

				}
				setText(CurrentValue);
			}			
		});

		
		
		colExePalletDistinctId.setCellValueFactory(data -> data.getValue().palletDistinctIdProperty());
		
	}
	
	/*@FXML
	private void startOnClick() {
		
		ApplicationLauncher.logger.info("startOnClick : Entry");
		startOnClickTimer = new Timer();
		startOnClickTimer.schedule(new startOnClickTask(), 100);
	}
	*/
	
	@FXML
	private void stopOnClick() {
	    ApplicationLauncher.logger.info("stopOnClick : Entry");
	    setUserAborted(true);
	    
	    if(!DeviceDataManagerController.getConveyorConfigParsedKey().isFtDutCmdEachTpSeqExecuteMode()) {
	    	//getCommandExecutor().setIsUserCancelled(true);
	    	getCommandExecutor().cancelExecution();
	    	
	    	ref_tvFtExecutor.getItems().stream().filter(e->e.isSummaryRow()).forEach(e1->{
	    		getDeviceMountedMap().keySet().forEach(eachPositionNo -> {
	    			setPositionResult(e1,Integer.parseInt(eachPositionNo),ConstantApp.EXECUTION_STATUS_ABORTED);
	    		});
	    	});
	    	
	    }
	    Platform.runLater(() -> {
	    	ref_btnStopExecution.setDisable(true);
	    });
	    //setUiControlsDisabled(false);
	}
	
	private void setPositionResult(DutExecutionResult row, int positionNo, String value) {
        switch (positionNo) {
            case 1: row.setResultPosition1(value);break;
            case 2: row.setResultPosition2(value);break;
            case 3: row.setResultPosition3(value);break;
            case 4: row.setResultPosition4(value);break;
            case 5: row.setResultPosition5(value);break;
            case 6: row.setResultPosition6(value);break;
            default: break;
        }
    }
	
	
	@FXML
	private void startOnClick() {
	    ApplicationLauncher.logger.info("startOnClick : Entry");
	    String projectName = ref_cmbxProjectName.getSelectionModel().getSelectedItem().toString();
	    if(projectName.equals("Select")) {
	    	ApplicationLauncher.logger.info("Project Empty : Kindly select the project - prompted!");
			ApplicationLauncher.InformUser("Project Empty", "Kindly select the project",AlertType.ERROR);

	    	return;
	    }
	    // Disable UI controls during execution
	    setUiControlsDisabled(true);
	    
	    // Create a background task
	    Task<Void> executionTask = new Task<Void>() {
	        @Override
	        protected Void call() throws Exception {
	        	if(DeviceDataManagerController.getConveyorConfigParsedKey().isFtDutCmdEachTpSeqExecuteMode()) {
	        		startDutTestPointExecutionAsync();
	        	}else {
	        		startDutIndividualDeviceExecutionAsync();
	        	}
	            
	            return null;
	        }
	        
	        @Override
	        protected void succeeded() {
	        	exitIndividualDeviceTestExecution();
	        }
	        
	        @Override
	        protected void failed() {
	        	exitIndividualDeviceTestExecution();
	        }
	        
	        @Override
	        protected void cancelled() {
	            ApplicationLauncher.logger.info("startOnClick: Execution task cancelled");
	        }
	    };
	    
	    // Start the task in a background thread
	    Thread executionThread = new Thread(executionTask);
	    executionThread.setDaemon(true);
	    executionThread.start();
	}
	
	public void exitIndividualDeviceTestExecution() {
		ApplicationLauncher.logger.debug("exitIndividualDeviceTestExecution: Entry");
		countdownTimer.stopCountdown();
    	countUpTimer.stopTimer();
    	Platform.runLater(() -> {
        	//ref_tvFtExecutor.getSelectionModel().select(testPointIndex);
        	ref_tvFtExecutor.getItems().stream().filter(e->e.isSummaryRow())
        		.findFirst()
        		.get()
        		.setTestPointExecutionStatus(ConstantApp.EXECUTION_STATUS_COMPLETED);

        });
        Platform.runLater(() -> {
            setUiControlsDisabled(false);
            
            //ApplicationLauncher.logger.error("exitIndividualDeviceTestExecution: Test execution failed", getException());
            closeAllComPort();
            //showErrorAlert("Execution Error", "Test execution failed: " + getException().getMessage());
        });
	}

	private void setUiControlsDisabled(boolean disabled) {
	    // Disable/enable your UI controls here
		ref_cmbxBayType.setDisable(disabled);
		ref_cmbxProjectName.setDisable(disabled);
		ref_btnStartExecution.setDisable(disabled);
		ref_btnStopExecution.setDisable(!disabled);
	    // Add other controls that should be disabled during execution
	}
	
	private void clearPositionResult(DutExecutionResult row, int positionNo) {
        switch (positionNo) {
            case 1: row.setResultPosition1(""); break;
            case 2: row.setResultPosition2(""); break;
            case 3: row.setResultPosition3(""); break;
            case 4: row.setResultPosition4(""); break;
            case 5: row.setResultPosition5(""); break;
            case 6: row.setResultPosition6(""); break;
        }
    }

	private void startDutTestPointExecutionAsync() {
	    ApplicationLauncher.logger.debug("startDutTestPointExecutionAsync : Entry");
	    clearDeviceTypeDutCmdManagerMap();
	    String myBayKey = ref_cmbxBayType.getSelectionModel().getSelectedItem().toString();
	    
	    TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(myBayKey);
	    setUserAborted(false);
	    Platform.runLater(() -> {
	        ref_pBarTestExecution.setProgress(0);
	    });
	    
	    int totalTestCount = ref_tvFtExecutor.getItems().size();
	    final AtomicInteger completedTests = new AtomicInteger(0);
	    countUpTimer.startTimer();
	    for(int i = 0; i < totalTestCount; i++) {
	    	
	    	if (isUserAborted()) {
	            ApplicationLauncher.logger.info("startDutTestPointExecutionAsync: userAborted");
	            //setUiControlsDisabled(false);
	            break;
	        }
	        if (Thread.currentThread().isInterrupted()) {
	            ApplicationLauncher.logger.info("startDutTestPointExecutionAsync: Execution interrupted");
	            break;
	        }
	        int testPointIndex = i;
	        
	        
	        Platform.runLater(() -> {
	        	ref_tvFtExecutor.getSelectionModel().select(testPointIndex);
	        	//ref_tvFtExecutor.getItems().get(testPointIndex).setStatus(ConstantApp.EXECUTION_STATUS_INPROGRESS);
	        	DutExecutionResult row = ref_tvFtExecutor.getItems().get(testPointIndex);

	        	row.setTestPointExecutionStatus(ConstantApp.EXECUTION_STATUS_INPROGRESS);
	        	
	        	//clearPositionResult(row,positionNo);
	        	row.setResultPosition1("");
	        	row.setResultPosition2("");
	        	row.setResultPosition3("");
	        	row.setResultPosition4("");
	        	row.setResultPosition5("");
	        	row.setResultPosition6("");
	        	
	        });
	        
	        DutCommand dutCommand = ref_tvFtExecutor.getItems().get(i).getDutCommand();
	        String targetDeviceType = dutCommand.getTargetDeviceType();
	        String deviceIdPrefix = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
	                              terminalBayProfile.getBayId() + targetDeviceType;
	        
	        List<String> deviceIdList = new ArrayList<>();
	        getDeviceMountedMap().keySet().forEach(dutPositionNo -> {
	            String deviceId = deviceIdPrefix + dutPositionNo;
	            deviceIdList.add(deviceId);
	        });
	        
	        if(!getDeviceTypeDutCmdManagerMap().containsKey(targetDeviceType)) {
	            addDeviceTypeDutCmdManagerMap(targetDeviceType, new DutCmdManager());
	        }
	        
	        DutCmdTestPointExecutor commandTpExecutor = new DutCmdTestPointExecutor();
	        DutCmdManager dutCmdManager = getDeviceTypeDutCmdManagerMap().get(targetDeviceType);
	        dutCmdManager.setDutCommand(dutCommand);
	        
	        // Update UI on JavaFX thread
	        //String formattedTime = formatExecutionTime(dutCommand.getTotalDutExecutionTimeInSec()+5);
	        countdownTimer.startCountdown(dutCommand.getTotalDutExecutionTimeInSec()+5);
	        Platform.runLater(() -> {
	            commandTpExecutor.setTvExecutor(tvFtExecutor);
	           // ref_txtExecutionTime.setText(formattedTime);
	        });
	        
	        // Execute commands
	        ApplicationLauncher.logger.info("startDutTestPointExecutionAsync: switching to next Test: " + dutCommand.getProjectName() + " -> " +dutCommand.getTestCaseName());
	        commandTpExecutor.dutCmdExecuteWithTestPointTrigger(testPointIndex, deviceIdList, dutCmdManager);
	        
	        // Wait for completion in background thread (doesn't block UI)
	        try {
	            commandTpExecutor.waitForCompletion(dutCommand.getTotalDutExecutionTimeInSec()+5, TimeUnit.SECONDS);
	            countdownTimer.stopCountdown();
	        } catch (InterruptedException e) {
	            ApplicationLauncher.logger.error("startDutTestPointExecutionAsync: Interrupted while waiting for command execution to complete", e);
	            Thread.currentThread().interrupt();
	            break;
	        }
	        
	        completedTests.incrementAndGet();
	        double progress = (double) completedTests.get() / totalTestCount;
	        
	        Platform.runLater(() -> {
	            ref_pBarTestExecution.setProgress(progress);
	        });
	        
	        // Small delay between iterations
	        try {
	            Thread.sleep(3000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            break;
	        }
	        Platform.runLater(() -> {
	        	//ref_tvFtExecutor.getSelectionModel().select(testPointIndex);
	        	ref_tvFtExecutor.getItems().get(testPointIndex).setTestPointExecutionStatus(ConstantApp.EXECUTION_STATUS_COMPLETED);
	        });
	        
	    }
	    if (!isUserAborted()) {
		    Platform.runLater(() -> {
		        ref_pBarTestExecution.setProgress(1.0);
		    });
	    }
	    
	}
	
	
	private void startDutIndividualDeviceExecutionAsync() {
	    ApplicationLauncher.logger.debug("startDutIndividualDeviceExecutionAsync : Entry");
	    clearDeviceTypeDutCmdManagerMap();
	    String myBayKey = ref_cmbxBayType.getSelectionModel().getSelectedItem().toString();
	    int eachTpBufferTime = DeviceDataManagerController.getConveyorConfigParsedKey().getFtDutCmdEachTpBufferTimeInSec();
	    TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(myBayKey);
	    setUserAborted(false);
	    Platform.runLater(() -> {
	        //ref_pBarTestExecution.setProgress(0);
	    	ref_tvFtExecutor.getItems().removeIf(DutExecutionResult::isSummaryRow);
	    	ref_tvFtExecutor.refresh();
	    	ref_pBarTestExecution.setProgress(0);
	    });
	    
	    int totalTestCount = ref_tvFtExecutor.getItems().size();
	    List<DutCommand> dutCommandList = new ArrayList<DutCommand>();
	    int totalTargetExecutionTimeInSec = 0;
	    for(int i = 0; i < totalTestCount; i++) {
	    	if(!ref_tvFtExecutor.getItems().get(i).isSummaryRow()) {
	    		dutCommandList.add( ref_tvFtExecutor.getItems().get(i).getDutCommand());
		    	totalTargetExecutionTimeInSec = totalTargetExecutionTimeInSec + 
		    					ref_tvFtExecutor.getItems().get(i).getDutCommand().getTotalDutExecutionTimeInSec() +
		    					eachTpBufferTime;
		    
	    	}else {
	    		totalTestCount--;
	    	}
	    	ref_tvFtExecutor.getItems().get(i).setTestPointExecutionStatus(ConstantApp.EXECUTION_STATUS_NOT_EXECUTED);
	    }
	    ApplicationLauncher.logger.debug("startDutIndividualDeviceExecutionAsync : totalTestCount: " +totalTestCount);
	    
	    ApplicationLauncher.logger.debug("totalTargetExecutionTimeInSec-dutOnly: " + totalTargetExecutionTimeInSec);
	    
	    //totalTargetExecutionTimeInSec = totalTargetExecutionTimeInSec + (eachTpBufferTime*totalTestCount);
	    ApplicationLauncher.logger.debug("totalTargetExecutionTimeInSec-withBuffer: " + totalTargetExecutionTimeInSec);
	    dutCommandList.stream().forEach(e -> {
            //String deviceId = deviceIdPrefix + dutPositionNo;
           // deviceIdList.add(deviceId);
        	ApplicationLauncher.logger.debug("dutCommandList : getTestCaseName: " + e.getTestCaseName());
        });
	    
	    //List<DutCmdIndividualDeviceExecutor>  commandExecutorList = new ArrayList<DutCmdIndividualDeviceExecutor>();
	    List<String> deviceIdList = new ArrayList<String>();
        getDeviceMountedMap().keySet().forEach(dutPositionNo -> {
        	ApplicationLauncher.logger.debug("getDeviceMountedMap : dutPositionNo: " + dutPositionNo);
        	deviceIdList.add(dutPositionNo);
        });
        
        final AtomicInteger completedTests = new AtomicInteger(0);
	    countUpTimer.startTimer();
	    countdownTimer.startCountdown(totalTargetExecutionTimeInSec);
	    //for(Entry <String, Boolean> eachDeviceMounted : getDeviceMountedMap().entrySet()) {
            //String deviceId = deviceIdPrefix + dutPositionNo;
           // deviceIdList.add(deviceId);
	    	//String dutPositionNo = eachDeviceMounted.getKey();
        	//ApplicationLauncher.logger.debug("getDeviceMountedMap : dutPositionNo: " + dutPositionNo);
        	//DutCmdIndividualDeviceExecutor commandExecutor = new DutCmdIndividualDeviceExecutor();
        	setCommandExecutor(new DutCmdIndividualDeviceExecutor());
            
        	getCommandExecutor().setTvExecutor(tvFtExecutor);
        	getCommandExecutor().setParentBayName(getMyBayKey());
        	getCommandExecutor().setIsUserCancelled(false);
        	getCommandExecutor().resetSummary();
        	getCommandExecutor().resetAllCounters();
        	getCommandExecutor().setEachBaylogger(myBaylogger);
            dutCommandList.stream().forEach(e -> {
                //String deviceId = deviceIdPrefix + dutPositionNo;
               // deviceIdList.add(deviceId);
            	ApplicationLauncher.logger.debug("dutCommandList : getTestCaseName-2: " + e.getTestCaseName());
            });
            getCommandExecutor().setProgressCallback(progress -> {
                Platform.runLater(() -> {
                    ref_pBarTestExecution.setProgress(progress);
                });
            });
            getCommandExecutor().dutCmdExecuteWithIndividualDeviceTrigger(deviceIdList, dutCommandList,terminalBayProfile);
            //commandExecutorList.add(commandExecutor);
	   // }
	    
	    try {
	    	getCommandExecutor().waitForCompletion(totalTargetExecutionTimeInSec+5, TimeUnit.SECONDS);
            /*for (Map.Entry<String, DutCmdManager> entry : getCommandExecutor().getDeviceTypeDutCmdManagerMap().entrySet()) {
    			ApplicationLauncher.logger.debug("getDeviceTypeDutCmdManagerMap: key: " + entry.getKey() + ", Mounted: " + entry.getValue().dutSpmList.size());
    		}*/
            setDeviceTypeDutCmdManagerMap(getCommandExecutor().getDeviceTypeDutCmdManagerMap());
            
            countdownTimer.stopCountdown();
        } catch (InterruptedException e) {
        	if (getCommandExecutor().getIsUserCancelled().get()) {
                ApplicationLauncher.logger.info("Execution cancelled by user");
            } else {
	            ApplicationLauncher.logger.error("startDutIndividualDeviceExecutionAsync: Interrupted while waiting for command execution to complete", e);
	            Thread.currentThread().interrupt();
            }
            //break;
        }
        //});
        
        //countdownTimer.startCountdown(totalTargetExecutionTimeInSec+5);
        //countUpTimer.startTimer();
        
	    try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //break;
        }
        
        
        if(getDeviceMountedMap().size()>0) {
        	return;
        }
	    
	    
	    
	    
	    for(int i = 0; i < totalTestCount; i++) {
	    	
	    	if (isUserAborted()) {
	            ApplicationLauncher.logger.info("startDutIndividualDeviceExecutionAsync: userAborted");
	            //setUiControlsDisabled(false);
	            break;
	        }
	        if (Thread.currentThread().isInterrupted()) {
	            ApplicationLauncher.logger.info("startDutIndividualDeviceExecutionAsync: Execution interrupted");
	            break;
	        }
	        int testPointIndex = i;
	        
	        
	        /*Platform.runLater(() -> {
	        	ref_tvFtExecutor.getSelectionModel().select(testPointIndex);
	        	//ref_tvFtExecutor.getItems().get(testPointIndex).setStatus(ConstantApp.EXECUTION_STATUS_INPROGRESS);
	        	DutExecutionResult row = ref_tvFtExecutor.getItems().get(testPointIndex);

	        	row.setStatus(ConstantApp.EXECUTION_STATUS_INPROGRESS);
	        	
	        	row.setResultPosition1("");
	        	row.setResultPosition2("");
	        	row.setResultPosition3("");
	        	row.setResultPosition4("");
	        	row.setResultPosition5("");
	        	row.setResultPosition6("");
	        	
	        });*/
	        
	        DutCommand dutCommand = ref_tvFtExecutor.getItems().get(i).getDutCommand();
	        String targetDeviceType = dutCommand.getTargetDeviceType();
	        String deviceIdPrefix = terminalBayProfile.getTerminalId() + terminalBayProfile.getClusterId() +
	                              terminalBayProfile.getBayId() + targetDeviceType;
	        
	        /*List<String> deviceIdList = new ArrayList<>();
	        getDeviceMountedMap().keySet().forEach(dutPositionNo -> {
	            String deviceId = deviceIdPrefix + dutPositionNo;
	            deviceIdList.add(deviceId);
	        });*/
	        
	        if(!getDeviceTypeDutCmdManagerMap().containsKey(targetDeviceType)) {
	            addDeviceTypeDutCmdManagerMap(targetDeviceType, new DutCmdManager());
	        }
	        
	        //DutCmdTestPointExecutor commandExecutor = new DutCmdTestPointExecutor();
	        DutCmdManager dutCmdManager = getDeviceTypeDutCmdManagerMap().get(targetDeviceType);
	        dutCmdManager.setDutCommand(dutCommand);
	        
	        // Update UI on JavaFX thread
	        //String formattedTime = formatExecutionTime(dutCommand.getTotalDutExecutionTimeInSec()+5);
	        countdownTimer.startCountdown(dutCommand.getTotalDutExecutionTimeInSec()+5);
	        
	        
	        // Execute commands
	        ApplicationLauncher.logger.info("startDutIndividualDeviceExecutionAsync: switching to next Test: " + dutCommand.getProjectName() + " -> " +dutCommand.getTestCaseName());
	        //commandExecutor.dutCmdExecuteWithTestPointTrigger(testPointIndex, deviceIdList, dutCmdManager);
	        
	        // Wait for completion in background thread (doesn't block UI)
	        try {
	        	getCommandExecutor().waitForCompletion(dutCommand.getTotalDutExecutionTimeInSec()+5, TimeUnit.SECONDS);
	            countdownTimer.stopCountdown();
	        } catch (InterruptedException e) {
	            ApplicationLauncher.logger.error("startDutIndividualDeviceExecutionAsync: Interrupted while waiting for command execution to complete", e);
	            Thread.currentThread().interrupt();
	            break;
	        }
	        
	        completedTests.incrementAndGet();
	        double progress = (double) completedTests.get() / totalTestCount;
	        
	        Platform.runLater(() -> {
	            ref_pBarTestExecution.setProgress(progress);
	        });
	        
	        // Small delay between iterations
	        try {
	            Thread.sleep(3000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            break;
	        }
	        Platform.runLater(() -> {
	        	//ref_tvFtExecutor.getSelectionModel().select(testPointIndex);
	        	ref_tvFtExecutor.getItems().get(testPointIndex).setTestPointExecutionStatus(ConstantApp.EXECUTION_STATUS_COMPLETED);
	        });
	        
	    }
	    if (!isUserAborted()) {
		    Platform.runLater(() -> {
		        ref_pBarTestExecution.setProgress(1.0);
		    });
	    }
	    
	}
	
	
	
/*	private String formatExecutionTime(int totalSeconds) {
	    if (totalSeconds < 0) {
	        return "00:00";
	    }
	    
	    int minutes = totalSeconds / 60;
	    int seconds = totalSeconds % 60;
	    
	    return String.format("%02d:%02d", minutes, seconds);
	}*/
	
	
/*	class startOnClickTask extends TimerTask {

		@Override
		public void run() {
			ApplicationLauncher.logger.debug("startOnClickTask : Entry");
			Platform.runLater(() -> {

				startDutTestExecution();

			});

			startOnClickTimer.cancel();
		}

		


	}*/
	
	public void startDutTestExecution() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("startDutTestExecution : Entry");
		clearDeviceTypeDutCmdManagerMap();
		String myBayKey = ref_cmbxBayType.getSelectionModel().getSelectedItem().toString();
		//ApplicationLauncher.logger.debug("cmbxBayTypeOnChange: myBayKey: " +myBayKey);
		

		TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(myBayKey);
		//ArrayList<String> 
		/*
		getDeviceMountedMap().clear(); 
		String positionIdAsList = terminalBayProfile.getPositionIdAsList();
		List<Integer> positionIdList =  Arrays.stream(positionIdAsList.split(","))
                .map(String::trim)
                .map(e->Integer.parseInt(e))
                .collect(Collectors.toList());
		String positionIdToBeSkippedAsList = terminalBayProfile.getPositionTobeSkippedAsList();
		List<Integer> positionIdToBeSkippedList =  Arrays.stream(positionIdToBeSkippedAsList.split(","))
                .map(String::trim)
                .map(e->Integer.parseInt(e))
                .collect(Collectors.toList());
		
		for(int i=0; i<positionIdList.size();i++) {
			if(!positionIdToBeSkippedList.contains(positionIdList.get(i))) {
				getDeviceMountedMap().put(String.format("%02d", positionIdList.get(i)), true);
			}
		}*/
		
		/*for(int i=0; i<positionIdToBeSkippedList.size();i++) {
			
			getDeviceMountedMap().put(positionIdToBeSkippedList.get(i), false);
			
		}*/
		
		for (Map.Entry<String, Boolean> entry : getDeviceMountedMap().entrySet()) {
			ApplicationLauncher.logger.debug("startDutTestExecution: Device: " + entry.getKey() + ", Mounted: " + entry.getValue());
		}
		//getDeviceMountedMap().entrySet().stream().forEachOrdered((k,v)->{
			//ApplicationLauncher.logger.debug("startDutTestExecution : Entry");
		//});
		
		for(int i=0; i<ref_tvFtExecutor.getItems().size();i++) {
			//Platform.runLater(() -> {
			//	ref_tvFtExecutor.getSelectionModel().select(i);
			//});
			DutCommand dutCommand = ref_tvFtExecutor.getItems().get(i).getDutCommand();
			String targetDeviceType = dutCommand.getTargetDeviceType();
			String deviceIdPrefix = terminalBayProfile.getTerminalId()+ terminalBayProfile.getClusterId()+
					terminalBayProfile.getBayId()+
					targetDeviceType;
					//ConstantConveyor.DEVICE_TYPE_QR_SCANNER;
					//String.format("%02d", getPalletQrScannerPositionId());
			ApplicationLauncher.logger.debug("startDutTestExecution : deviceIdPrefix: " + deviceIdPrefix);
			ApplicationLauncher.logger.debug("startDutTestExecution : getDeviceMountedMap().keySet: " + getDeviceMountedMap().keySet());
			
			List<String> deviceIdList= new ArrayList<String>();
			getDeviceMountedMap().keySet().stream().forEach(dutPositionNo -> {
				String deviceId = deviceIdPrefix+ dutPositionNo;
				ApplicationLauncher.logger.debug("startDutTestExecution : deviceId: " +deviceId);
				deviceIdList.add(deviceId);
			});
			//ApplicationLauncher.logger.debug("dutCommandExecuteStart : deviceId-2: " + deviceIdPrefix);
			
			
			if(!getDeviceTypeDutCmdManagerMap().containsKey(targetDeviceType)) {
				addDeviceTypeDutCmdManagerMap(targetDeviceType,new DutCmdManager());
			}
			
			
			//deviceIdList.add("010101QR01");
			//deviceIdList.add("010101QR02");
			
			DutCmdTestPointExecutor commandExecutor = new DutCmdTestPointExecutor();
			DutCmdManager dutCmdManager = getDeviceTypeDutCmdManagerMap().get(targetDeviceType);
			dutCmdManager.setDutCommand(dutCommand);
			commandExecutor.setTvExecutor(tvFtExecutor);
			//commandExecutor.dutExecuteCommandWithDeviceIdListTrigger(deviceIdList,dutCmdManager);
			
			try {
	            commandExecutor.waitForCompletion(dutCommand.getTotalDutExecutionTimeInSec()+5, TimeUnit.SECONDS); // Adjust timeout as needed
	        } catch (InterruptedException e) {
	            ApplicationLauncher.logger.error("startDutTestExecution: Interrupted while waiting for command execution to complete", e);
	            Thread.currentThread().interrupt();
	            break; // Exit loop if interrupted
	        }
	        
	        // Optional: Check if execution completed successfully
	        if (!commandExecutor.isExecutionComplete()) {
	            ApplicationLauncher.logger.debug("startDutTestExecution: Command execution may not have completed fully for iteration: " + i);
	        }
			break;
			//Sleep(5000);
		}
		
		closeAllComPort();
	}
	
	
	public void closeAllComPort() {
		ApplicationLauncher.logger.debug("closeAllComPort: Entry");
		getDeviceMountedMap().keySet().stream().forEach(dutPositionNo -> {
			for (DutCmdManager dutCmdManager : getDeviceTypeDutCmdManagerMap().values()) {
				//DutCmdManager dutCmdManager :
				//DutCmdManager dutCmdManager = eachDeviceTypeDutCmdManager.getValue();
        		dutCmdManager.dutCmdDisconnectPort_V2(Integer.parseInt(dutPositionNo));
        		ApplicationLauncher.logger.debug("ShutDownAllCompPorts: Disconnecting DutCommand port after execution for position: " +  dutPositionNo);
			}
    	});
	}
		


	private void refAssignment() {
		// TODO Auto-generated method stub
		ref_tvFtExecutor = tvFtExecutor;
		ref_cmbxProjectName= cmbxProjectName;
		ref_cmbxBayType = cmbxBayType;
		ref_txtClusterName = txtClusterName;
		
		ref_btnStartExecution = btnStartExecution;
		ref_btnStopExecution = btnStopExecution;
		ref_txtEachExecutionTime = txtEachExecutionTime;
		ref_txtTotalExecutionTime = txtTotalExecutionTime;
		
		ref_pBarTestExecution = pBarTestExecution;
	}

	public  List<String> getProjectNameList() {
		return projectNameList;
	}

	public void setProjectNameList(List<String> projectNameList) {
		this.projectNameList = projectNameList;
	}

	public  int getSelectedProjectIndex() {
		return selectedProjectIndex;
	}

	public  void setSelectedProjectIndex(int selectedProjectIndex) {
		this.selectedProjectIndex = selectedProjectIndex;
	}
	
	private Parent getNodeFromFXML(String url) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
		Parent parentNode = loader.load();

		ApplicationLauncher.logger.info("ProjectExecutionController: Loaded property UI: " + parentNode);
		//testPropertyController = loader.getController();
		return parentNode;
	}
	public void enableBusyLoadingScreen() {//long time_in_seconds) {
		ApplicationLauncher.logger.info("ProjectAutoModeExecutionController: enableBusyLoadingScreen: entry");

		//FXMLLoader loader = new FXMLLoader(
		//		getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
		Parent nodeFromFXML = null;
		try {
			nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
			ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("ProjectExecutionController: enableBusyLoadingScreen: Exception: "+e.getMessage());
		}
	}
	
	public void disableBusyLoadingScreen(){
		BusyLoadingController.removeBusyLoadingScreenOverlay();
	}
	
	@FXML
	public void selectProjectOnChangeTrigger() {
		ApplicationLauncher.logger.info("selectProjectOnChangeTrigger : Entry");
		selectProjectOnchangeTimer = new Timer();
		selectProjectOnchangeTimer.schedule(new selectProjectOnChangeTask(), 100);
	}
	
	class selectProjectOnChangeTask extends TimerTask {

		@Override
		public void run() {
			ApplicationLauncher.logger.debug("selectProjectOnChangeTask : Entry");
			
				ApplicationLauncher.logger.debug("selectProjectOnChangeTask : Entry2");
				Platform.runLater(() -> {

					refreshAllDataInGUI();

				});

			selectProjectOnchangeTimer.cancel();
		}

	}
	
	public void refreshAllDataInGUI() {
		ref_tvFtExecutor.getItems().clear();
		String projectName = ref_cmbxProjectName.getSelectionModel().getSelectedItem().toString();
		
		List<DutCommand> dutCommandList = MySqlServiceManager.getDutCommandService().findByProjectName(projectName);
		AtomicInteger serialNo = new AtomicInteger(1) ;
		dutCommandList.stream().filter(e->e.isActive())
			.forEachOrdered(e->{
			DutExecutionResult ftExecutionResult1 = new DutExecutionResult();
			//int localSerialNo = serialNo++;
			ftExecutionResult1.setSerialNo(serialNo.getAndIncrement());
			ftExecutionResult1.setTestPointName(e.getTestCaseName());
			ftExecutionResult1.setTestPointExecutionStatus(ConstantConveyor.EXECUTION_STATUS_NOT_EXECUTED);
			ftExecutionResult1.setResultPosition1("");
			ftExecutionResult1.setResultPosition2("");
			ftExecutionResult1.setResultPosition3("");
			ftExecutionResult1.setResultPosition4("");
			ftExecutionResult1.setResultPosition5("");
			ftExecutionResult1.setResultPosition6("");
			ftExecutionResult1.setPalletDistinctId("Pallet1_02");
			ftExecutionResult1.setDutCommand(e);
			ref_tvFtExecutor.getItems().add(ftExecutionResult1);
		});
		ref_tvFtExecutor.refresh();




	}
	
	public static JSONArray getListOfTestPoints(String project_name, String deploymentID) throws JSONException{
		ApplicationLauncher.logger.debug("getListOfTestPoints : Entry");
		JSONObject testcaselist = MySQL_Controller.sp_getdeploy_test_cases(project_name,deploymentID);
		JSONArray testcases = testcaselist.getJSONArray("Test_cases");
		//ApplicationLauncher.logger.debug("testcases JSONArray: " + testcases);

		return testcases;
	}
	
	public  String getCurrentProjectName() {
		return currentProjectName;
	}

	public void setCurrentProjectName(String project_name) {
		this.currentProjectName = project_name;
	}
	
	public  String getSelectedDeployment_ID() {
		return selectedDeployment_ID;
	}

	public void setSelectedDeployment_ID(String selectedDeploymentID) {
		this.selectedDeployment_ID = selectedDeploymentID;
	}
	
	public List<String> getDeploymentIdList() {
		return deploymentIdList;
	}

	public void setDeploymentIdList(List<String> deployIdList) {
		deploymentIdList = deployIdList;
	}
	
	public static Map<String, Boolean> getDeviceMountedMap() {
		return deviceMountedMap;
	}

	public static void setDeviceMountedMap(Map<String, Boolean> deviceMountedMap) {
		DutExecutorFtController.deviceMountedMap = deviceMountedMap;
	}
	
	public void addDeviceTypeDutCmdManagerMap(String deviceType, DutCmdManager dutCmdManager) {
		this.deviceTypeDutCmdManagerMap.put(deviceType, dutCmdManager);
	}
	
	public void clearDeviceTypeDutCmdManagerMap() {
		this.deviceTypeDutCmdManagerMap.clear();;
	}
	
	public Map<String, DutCmdManager> getDeviceTypeDutCmdManagerMap() {
		return deviceTypeDutCmdManagerMap;
	}
	
	public void setDeviceTypeDutCmdManagerMap(Map<String, DutCmdManager> dutCmdManagerMap) {
		this.deviceTypeDutCmdManagerMap = dutCmdManagerMap;
	}

	public static boolean isUserAborted() {
		return userAborted;
	}

	public static void setUserAborted(boolean userAborted) {
		DutExecutorFtController.userAborted = userAborted;
	}

	public String getMyBayKey() {
		return myBayKey;
	}

	public void setMyBayKey(String myBayKey) {
		this.myBayKey = myBayKey;
	}

	public DutCmdIndividualDeviceExecutor getCommandExecutor() {
		return commandExecutor;
	}

	public void setCommandExecutor(DutCmdIndividualDeviceExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

}
