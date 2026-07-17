 package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClient;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.AsyncClientManager;
import com.tasnetwork.calibration.conveyor.AsyncHttpClient.ServerProperties;
import com.tasnetwork.calibration.conveyor.constant.ConstantLdu;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import gnu.io.CommPortIdentifier;
import javafx.application.Application;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class TestParamSetupController implements Initializable {
	
	@FXML
	private TitledPane titledPaneFrequencyTestPoint;
	
	@FXML
	private TitledPane titledPaneTimerTestPoint;
	

    @FXML
    private ComboBox<Integer> cmbBxRefStdNoOfPulses;
    
    @FXML
    private ComboBox<String> cmbBxOccuranceTimeMin;
    @FXML
    private ComboBox<String> cmbBxOccuranceTimeSec;
    @FXML
    private ComboBox<String> cmbBxRestorationTimeMin;
    @FXML
    private ComboBox<String> cmbBxRestorationTimeSec;
    @FXML
    private ComboBox<String> cmbBxTimerTP_OnTimeMin;
    @FXML
    private ComboBox<String> cmbBxTimerTP_OnTimeSec;
    @FXML
    private ComboBox<String> cmbBxTimerTP_OffTimeMin;
    @FXML
    private ComboBox<String> cmbBxTimerTP_OffTimeSec;
    @FXML
    private ComboBox<Integer> cmbBxTimerTP_NoOfCycle;
    @FXML
    private ComboBox<Integer> cmbBxFreqTP_PulsatingDC_Freq;
    @FXML
    private TextField txtRefStandardConstant;
    public static TextField ref_txtRefStandardConstant;
    

    
 /*   @FXML
    private ComboBox<String> cmbBxTimeZoneList;
    public static ComboBox<String> ref_cmbBxTimeZoneList;
    
    @FXML
    private TextField txtHttpProtocol;
    @FXML
    private TextField txtServerIP;
    @FXML
    private TextField txtServerPort;
    
    @FXML
    private TextField txtAppVersion;
    @FXML
    private TextField txtServerVersion;
    private static TextField ref_txtServerVersion;
    @FXML
    private TextField txtServerSerialPort1;
    private static TextField ref_txtServerSerialPort1;
    @FXML
    private TextField txtServerSerialPort2;
    private static TextField ref_txtServerSerialPort2;
    @FXML
    private TextField txtCurrentServerIP;
    
    @FXML
    private Button btnGetFirmwareVersion;
    @FXML
    private Button btnGetServerSerialStatus;
    @FXML
    private Button btnGetServerTimeZone;
    @FXML
    private Button btnSetServerTimeZone;*/
    
/*    private AsyncClient asyncClient;*/
    
//    public static String rootUrl;
/*    Timer PwrSrcValidateTimer;
    Timer RefStdValidateTimer;
    Timer LDU_ValidateTimer;
    
    private static HashMap FXML_PortMap = new HashMap();
    
    private static  boolean PortValidationTurnedON = false;*/
    
/*    Timer UI_DisplayTimer = new Timer();
    UI_DisplayTimerTask UI_DisplayTimerTaskObj;*/
    
    //MyRunnable myRunnable;
    //Thread myRunnableThread;
/*    
    public SerialDataManager serialDM_Obj = new SerialDataManager();
    DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();*/
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//ApplicationLauncher.setCursor(Cursor.WAIT);
    	
    	ref_txtRefStandardConstant = txtRefStandardConstant;
    	titledPaneFrequencyTestPoint.setVisible(false);
    	titledPaneTimerTestPoint.setVisible(false);
    	setupGUI_RefStdData();
    	setupGUI_AutoModeData();
  		setupGUI_FrequencyTP_Data();
  		setupGUI_TimerTP_Data();
  		LoadSavedTestParamDataToGUI();
    	
/*    	ref_txtServerVersion = txtServerVersion;
    	ref_txtServerSerialPort1 = txtServerSerialPort1;
    	ref_txtServerSerialPort2 = txtServerSerialPort2;
    	ref_cmbBxTimeZoneList = cmbBxTimeZoneList;*/
/*    	setupGUI_RefreshFreqData();
    	setupSystemAppVersion();
    	load_saved_server_settingsToGUI();
    	AsyncClientManager.load_saved_server_settings();*/
    	//ApplicationLauncher.setCursor(Cursor.DEFAULT);
    }
    
    public void setupGUI_RefStdData(){
    	
    	for (int i = 1; i<100; i++){
    		cmbBxRefStdNoOfPulses.getItems().add(i);

		}

    	cmbBxRefStdNoOfPulses.setValue(1);
    	
    }
    
    public void setupGUI_AutoModeData(){
    	
		cmbBxOccuranceTimeSec.getItems().add(String.format("%02d", 0));
		cmbBxRestorationTimeSec.getItems().add(String.format("%02d",0));
		
    	for (int i = 1; i<60; i++){
    		cmbBxOccuranceTimeMin.getItems().add(String.format("%02d",i));//add(String.format("%02d",i));
    		cmbBxRestorationTimeMin.getItems().add(String.format("%02d",i));
    		cmbBxOccuranceTimeSec.getItems().add(String.format("%02d",i));
    		cmbBxRestorationTimeSec.getItems().add(String.format("%02d",i));


    	}
    	

    	
    	cmbBxOccuranceTimeMin.setValue("01");
    	cmbBxOccuranceTimeSec.setValue("00");
    	cmbBxRestorationTimeMin.setValue("01");
    	cmbBxRestorationTimeSec.setValue("00");
    }
    
    public void setupGUI_TimerTP_Data(){
    	
    	if(titledPaneTimerTestPoint.isVisible()){
    		for (int i = 1; i<100; i++){
    			cmbBxTimerTP_OnTimeMin.getItems().add(String.format("%02d",i));
    			cmbBxTimerTP_OffTimeMin.getItems().add(String.format("%02d",i));
    			cmbBxTimerTP_NoOfCycle.getItems().add(i);
    		}
    		cmbBxTimerTP_NoOfCycle.getItems().add(100);

    		for (int i = 0; i<60; i++){

    			cmbBxTimerTP_OnTimeSec.getItems().add(String.format("%02d",i));
    			cmbBxTimerTP_OffTimeSec.getItems().add(String.format("%02d",i));
    		}



    		cmbBxTimerTP_OnTimeMin.setValue("01");
    		cmbBxTimerTP_OnTimeSec.setValue("00");
    		cmbBxTimerTP_OffTimeMin.setValue("01");
    		cmbBxTimerTP_OffTimeSec.setValue("00");
    		cmbBxTimerTP_NoOfCycle.setValue(1);
    	}
    }
    
    public void setupGUI_FrequencyTP_Data(){
    	if(titledPaneFrequencyTestPoint.isVisible()){
    		for (int i = 1; i<=100; i++){
    			cmbBxFreqTP_PulsatingDC_Freq.getItems().add(i);

    		}

    		cmbBxFreqTP_PulsatingDC_Freq.setValue(1);
    	}
    }
    
/*    public void setupSystemAppVersion(){
    	txtAppVersion.setText(ConstantVersion.APPLICATION_VERSION);
    }
    public void getServerFirmwareVersion(){
    	AsyncClientManager httpclientManager = new AsyncClientManager();
    	httpclientManager.getServerFirmwareVersion();
    }
    
    public void  getServerSerialStatus(){
    	AsyncClientManager httpclientManager = new AsyncClientManager();
    	httpclientManager.getServerSerialStatus();
    	//asyncClient.lstamper_serialStatus();
    }
    
    
    public void  getServerTimeZoneList(){
    	AsyncClientManager httpclientManager = new AsyncClientManager();
    	httpclientManager.getServerTimeZoneList();
    	//asyncClient.lstamper_Scan_Available_timezones();
    }
    
    public void  setTimeZoneOnServer(){
    	String SelectedTimeZone = ref_cmbBxTimeZoneList.getSelectionModel().getSelectedItem();
    	AsyncClientManager httpclientManager = new AsyncClientManager();
    	httpclientManager.setTimeZoneOnServer(SelectedTimeZone);

    }
    public static void UpdateDisplayServerTimeZoneList(String[] value){
    	ref_cmbBxTimeZoneList.getItems().setAll(value);
    	Platform.runLater(() -> {
    		ref_cmbBxTimeZoneList.getSelectionModel().select(0);
		});
    	
    }
    public static void UpdateDisplaySerialPortPort1(String value){
    	ref_txtServerSerialPort1.setText(value);
    }

    public static void UpdateDisplaySerialPortPort2(String value1){
    	ref_txtServerSerialPort2.setText(value1);
    }
    
    public static void UpdateDisplayServerFirmwareVersion(String value1){
    	ref_txtServerVersion.setText(value1);
    }
    
   
    
    public void setupGUI_RefreshFreqData(){
    	for (int i = 2; i<60; i++){
    		cmbBxGUI_RefreshFreq.getItems().add(i);
    	}
    	
    	cmbBxGUI_RefreshFreq.setValue(3);
    }

    */
    public void LoadSavedTestParamDataToGUI(){

    	JSONObject TestParamData = MySQL_Controller.sp_get_testparam_config();
    	if(TestParamData.length()>0){
    		try {
    			int data_inSec = TestParamData.getInt("occurtime_insec");

    			int min = data_inSec/60;
    			int sec = data_inSec%60;
    			cmbBxOccuranceTimeMin.setValue(String.format("%02d",min));
    			cmbBxOccuranceTimeSec.setValue(String.format("%02d",sec));

    			data_inSec = TestParamData.getInt("restoretime_insec");
    			min = data_inSec/60;
    			sec = data_inSec%60;

    			cmbBxRestorationTimeMin.setValue(String.format("%02d",min));
    			cmbBxRestorationTimeSec.setValue(String.format("%02d",sec));

    			data_inSec = TestParamData.getInt("timer_tp_ontime_insec");
    			min = data_inSec/60;
    			sec = data_inSec%60;
    			cmbBxTimerTP_OnTimeMin.setValue(String.format("%02d",min));
    			cmbBxTimerTP_OnTimeSec.setValue(String.format("%02d",sec));

    			data_inSec = TestParamData.getInt("timer_tp_offtime_insec");
    			min = data_inSec/60;
    			sec = data_inSec%60;
    			cmbBxTimerTP_OffTimeMin.setValue(String.format("%02d",min));
    			cmbBxTimerTP_OffTimeSec.setValue(String.format("%02d",sec));

    			cmbBxTimerTP_NoOfCycle.setValue(TestParamData.getInt("timer_tp_no_of_cycle"));
    			ref_txtRefStandardConstant.setText(TestParamData.getString("ref_std_constant"));
    			cmbBxRefStdNoOfPulses.setValue(TestParamData.getInt("ref_std_no_of_pulses"));
    			cmbBxFreqTP_PulsatingDC_Freq.setValue(TestParamData.getInt("freq_tp_pulsating_dc_freq"));

    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			ApplicationLauncher.logger.error("LoadSavedTestParamDataToGUI: JSONException1:"+e.getMessage());

    		} 


    	}

    	
    }

    
    

    
/*    
    
    public  static String  getRootUrl(){
    	return rootUrl;
    }
    
    public static void setRootUrl(){
    	rootUrl = ServerProperties.HTTP_Protocol +
				ServerProperties.PublicURL_Id+
				ServerProperties.EndURL+":"+
				ServerProperties.URLPort;
    	
    }*/
    


    

    
    public void SaveOnClick(){
/*    	String serverHttpProtocol = txtHttpProtocol.getText() ;
    	String serverIP = txtServerIP.getText();
    	String serverPort = txtServerPort.getText();
    	int refreshGUI_Frequency = cmbBxGUI_RefreshFreq.getSelectionModel().getSelectedItem();

    	
    	MySQL_Controller.sp_add_server_settings(serverHttpProtocol, serverIP, serverPort, refreshGUI_Frequency);
    	AsyncClientManager.load_saved_server_settings();
    	setRootUrl();*/
    	

        int OccuranceTimeInSec=0;
        int RestorationTimeInSec=0;
        int TimerTP_OnTimeInSec =0;
        int TimerTP_OffTimeInSec =0;
        int TimerTP_NoOfCycle =0;
        int TP_PulsatingDC_Freq = 0;
        int RefStdNoOfPulses = cmbBxRefStdNoOfPulses.getSelectionModel().getSelectedItem();
        String RefStandardConstantStr = ref_txtRefStandardConstant.getText();
        OccuranceTimeInSec = (Integer.parseInt(cmbBxOccuranceTimeMin.getSelectionModel().getSelectedItem())*60) + 
        		Integer.parseInt(cmbBxOccuranceTimeSec.getSelectionModel().getSelectedItem());
        RestorationTimeInSec = (Integer.parseInt(cmbBxRestorationTimeMin.getSelectionModel().getSelectedItem())*60) + 
        		Integer.parseInt(cmbBxRestorationTimeSec.getSelectionModel().getSelectedItem());
        ApplicationLauncher.logger.info("SaveOnClick: OccuranceTimeInSec: "+OccuranceTimeInSec);
        ApplicationLauncher.logger.info("SaveOnClick: RestorationTimeInSec: "+RestorationTimeInSec);
        if(titledPaneTimerTestPoint.isVisible()){
	        TimerTP_OnTimeInSec = (Integer.parseInt(cmbBxTimerTP_OnTimeMin.getSelectionModel().getSelectedItem())*60) + 
								Integer.parseInt(cmbBxTimerTP_OnTimeSec.getSelectionModel().getSelectedItem());
	        TimerTP_OffTimeInSec = (Integer.parseInt(cmbBxTimerTP_OffTimeMin.getSelectionModel().getSelectedItem())*60) + 
	        					Integer.parseInt(cmbBxTimerTP_OffTimeSec.getSelectionModel().getSelectedItem());
	        TimerTP_NoOfCycle = cmbBxTimerTP_NoOfCycle.getSelectionModel().getSelectedItem();
        }
        
        ApplicationLauncher.logger.info("SaveOnClick: TimerTP_OnTimeInSec: "+TimerTP_OnTimeInSec);
        ApplicationLauncher.logger.info("SaveOnClick: TimerTP_OffTimeInSec: "+TimerTP_OffTimeInSec);
        
        if(titledPaneFrequencyTestPoint.isVisible()){
        	TP_PulsatingDC_Freq = cmbBxFreqTP_PulsatingDC_Freq.getSelectionModel().getSelectedItem();
        }
        ApplicationLauncher.logger.info("SaveOnClick: TP_PulsatingDC_Freq: "+TP_PulsatingDC_Freq);
        
        if(!RefStandardConstantStr.isEmpty()){
        	if(GUIUtils.isNumber(RefStandardConstantStr)){
        		long RefStandardConstant = Long.parseLong(RefStandardConstantStr);
        		MySQL_Controller.sp_add_testparam_config( OccuranceTimeInSec,  RestorationTimeInSec,  TimerTP_OnTimeInSec, 
        				 TimerTP_OffTimeInSec,  TimerTP_NoOfCycle, RefStandardConstant,  RefStdNoOfPulses ,
        				 TP_PulsatingDC_Freq );
        		ApplicationLauncher.logger.info("TestParamSetupController: Saved");
        		ApplicationLauncher.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
        	}else{
        		ApplicationLauncher.logger.info("TestParamSetupController: Enter valid number on Reference Standard : No of impulses/unit : Prompted");
        		ApplicationLauncher.InformUser("Save failed", "Enter valid number on Reference Standard : No of impulses/unit", AlertType.ERROR);
        	}
        }else{
        	ApplicationLauncher.logger.info("TestParamSetupController: Enter the value for Reference Standard : No of impulses/unit : Prompted");
        	ApplicationLauncher.InformUser("Save failed", "Enter the value for Reference Standard : No of impulses/unit", AlertType.ERROR);
        }
        
    	
    	
    }





}
