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
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ServerSettingController implements Initializable {
	
/*	private int scanDeviceTimerTimeOutInSec = 180;
	private int scanDeviceTimerCounter = 0;
	static Timer scanDeviceTimer;
    static Timer bootupStatusTimer;
    TimerTask bootupStatusTimerTask;
    public static boolean bootupStatusTimerAlreadyStarted = false;*/
	
    @FXML
    private ComboBox<Integer> cmbBxGUI_RefreshFreq;
    @FXML
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
    private Button btnSetServerTimeZone;
    
    @FXML
    private Button  btnScanDevice;
    public static Button  ref_btnScanDevice;
/*    private AsyncClient asyncClient;*/
    
    public static String rootUrl;
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
    	ref_txtServerVersion = txtServerVersion;
    	ref_txtServerSerialPort1 = txtServerSerialPort1;
    	ref_txtServerSerialPort2 = txtServerSerialPort2;
    	ref_cmbBxTimeZoneList = cmbBxTimeZoneList;
    	ref_btnScanDevice = btnScanDevice;

    	setupGUI_RefreshFreqData();
    	setupSystemAppVersion();
    	//load_saved_server_settingsToGUI();
    	//AsyncClientManager.load_saved_server_settings();
    	
    	cmbBxGUI_RefreshFreq.setDisable(true);
    	//ApplicationLauncher.setCursor(Cursor.DEFAULT);
    }
    
    public void setupSystemAppVersion(){
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
    
/*    @FXML
    public void  ScanDeviceTimerTaskTrigger(){
    	scanDeviceTimerCounter =0;
        scanDeviceTimer = new Timer();
        scanDeviceTimer.schedule(new scanDeviceTimerTask(),0, 1000);
    }*/
    
    public void  setTimeZoneOnServer(){
    	String SelectedTimeZone = ref_cmbBxTimeZoneList.getSelectionModel().getSelectedItem();
    	AsyncClientManager httpclientManager = new AsyncClientManager();
    	httpclientManager.setTimeZoneOnServer(SelectedTimeZone);
/*    	String TARGET_URL = ServerSettingController.getRootUrl()
    			+"/lstamperConnecttoTimezone";
    	Request req = new RequestBuilder("POST")
				.setUrl(TARGET_URL)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addParameter("timezone", ref_cmbBxTimeZoneList.getSelectionModel().getSelectedItem())
				.build();
    	asyncClient.lstamper_set_selected_Timezone(req);*/
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
 /*    public void initializeComPorts() {
       setupComPortsBaudRate();
        if(ConstantFeatureEnable.ENABLE_SERIALPORT){
        	loadAvailableComPorts();// Commented for ProTamp3pMigration
        }
        updatePowerSourceModel();
        updateReferenceMeterModel();
        updateLDUModel();
        load_saved_device_settings();
    }*/
    
    public void load_saved_server_settingsToGUI(){

    	JSONObject servetSetting = MySQL_Controller.sp_get_server_setting();
    	try {
			if(servetSetting.has("http_protocol")){
				txtHttpProtocol.setText(servetSetting.getString("http_protocol"));
				//ServerProperties.HTTP_Protocol=servetSetting.getString("http_protocol");
			} else{
				
				txtHttpProtocol.setText("");
				ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: http_protocol: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:"+e.getMessage());
			txtHttpProtocol.setText("");
			ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: http_protocol: Data not retrieved from database");
		} 	
    	
    	try {
			if(servetSetting.has("server_ip")){
				txtServerIP.setText(servetSetting.getString("server_ip"));
				//ServerProperties.PublicURL_Id = servetSetting.getString("server_ip");
			} else{
				
				txtServerIP.setText("");
				ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: server_ip: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:"+e.getMessage());
			txtServerIP.setText("");
			ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: server_ip: Data not retrieved from database");
		} 
    	
    	try {
			if(servetSetting.has("server_port")){
				txtServerPort.setText(servetSetting.getString("server_port"));
				//ServerProperties.URLPort = servetSetting.getString("server_port");
			} else{
				
				txtServerPort.setText("");
				ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: server_port: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:"+e.getMessage());
			txtServerPort.setText("");
			ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: server_port: Data not retrieved from database");
		} 
    	
    	try {
			if(servetSetting.has("refresh_gui_freq")){
				cmbBxGUI_RefreshFreq.setValue(servetSetting.getInt("refresh_gui_freq"));
				//ServerProperties.RefreshGUI_Freq = servetSetting.getInt("refresh_gui_freq");
			} else{
				
				cmbBxGUI_RefreshFreq.setValue(3);
				ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: refresh_gui_freq: Data not retrieved from DB");
			
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_server_settingsToGUI: JSONException1:"+e.getMessage());
			cmbBxGUI_RefreshFreq.setValue(3);
			ApplicationLauncher.logger.info("load_saved_server_settingsToGUI: refresh_gui_freq: Data not retrieved from database");
		} 
    	
    }
    
/*    public static JSONObject get_device_settings(String InputSourceType){

    	JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
    	return saved_pwr_setting;
    }*/
    
    
 /*   public void setupComPortsBaudRate() {
    	
    	cmbBxPowerSrcBaudRate.getItems().clear();
    	cmbBxPowerSrcBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantPowerSource.PowerSrcDefaultBaudRate);
    	cmbBxRefStdBaudRate.getItems().clear();
    	cmbBxRefStdBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxRefStdBaudRate.getSelectionModel().select(ConstantRefStd.RefStdDefaultBaudRate);
    	cmbBxLDU_BaudRate.getItems().clear();
    	cmbBxLDU_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxLDU_BaudRate.getSelectionModel().select(ConstantLDU.LDU_DefaultBaudRate);
    }
    */
/*    public static boolean  getPortValidationTurnedON(){
    	return PortValidationTurnedON;
    }
    
    public static void setPortValidationTurnedON(boolean status){
    	PortValidationTurnedON = status;
    	
    }*/
    
    
    
    public  static String  getRootUrl(){
    	return rootUrl;
    }
    
    public static void setRootUrl(){
    	rootUrl = ServerProperties.HTTP_Protocol +
				ServerProperties.PublicURL_Id+
				ServerProperties.EndURL+":"+
				ServerProperties.URLPort;
    	
    }
    
    /*
    public void loadAvailableComPorts() {
    	
    	
    	//Stub for testing
    	
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.REFSTD;
		 ModelList.add("Com1");
		 ModelList.add("Com2");
		 ModelList.add("Com3");
		 
    	//scanSerialPortAndUpdateDisplay();//dramesh
    }
    */
/*    public void updatePowerSourceModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.POWERSRC;
		 ModelList.add(ModelName);
		 cmbBxPowerSource_ModelName.getItems().clear();
	     cmbBxPowerSource_ModelName.getItems().addAll(ModelList);
	     cmbBxPowerSource_ModelName.getSelectionModel().select(0);
	 }*/
    
/*    public void updateReferenceMeterModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.REFSTD;
		 ModelList.add(ModelName);
		 cmbBxReferanceStd_ModelName.getItems().clear();
		 cmbBxReferanceStd_ModelName.getItems().addAll(ModelList);
		 cmbBxReferanceStd_ModelName.getSelectionModel().select(0);
	 }*/
    
/*    public void updateLDUModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.LDU;
		 ModelList.add(ModelName);
		 cmbBxLDU_ModelName.getItems().clear();
		 cmbBxLDU_ModelName.getItems().addAll(ModelList);
		 cmbBxLDU_ModelName.getSelectionModel().select(0);
	 }*/
    

    
    
    
/*    public void scanSerialPortAndUpdateDisplay() {

     	cmbBxPowerSrcPortSelection.getItems().clear();
     	cmbBxRefStdPortSelection.getItems().clear();
     	cmbBxLDU_PortSelection.getItems().clear();

     	Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
             	cmbBxPowerSrcPortSelection.getItems().add(curPort.getName());
            	cmbBxRefStdPortSelection.getItems().add(curPort.getName());
            	cmbBxLDU_PortSelection.getItems().add(curPort.getName());
            }
        }
        
        try {
        	cmbBxPowerSrcPortSelection.getSelectionModel().select(0);
        } catch(Exception e) {
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception1:"+e.getMessage());
        }
        try {
        	cmbBxRefStdPortSelection.getSelectionModel().select(0);
        } catch(Exception e) {
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception2:"+e.getMessage());
        }
        
        try{
        	cmbBxLDU_PortSelection.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3:"+e.getMessage());
        }
    }*/
    
    public void SaveOnClick(){
    	String serverHttpProtocol = txtHttpProtocol.getText() ;
    	String serverIP = txtServerIP.getText();
    	String serverPort = txtServerPort.getText();
    	int refreshGUI_Frequency = cmbBxGUI_RefreshFreq.getSelectionModel().getSelectedItem();

    	
    	MySQL_Controller.sp_add_server_settings(serverHttpProtocol, serverIP, serverPort, refreshGUI_Frequency);
    	AsyncClientManager.load_saved_server_settings();
    	setRootUrl();
    	ApplicationLauncher.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
    	
    }
 /*   public static void DisableScanDeviceButton(){
    	Platform.runLater(() -> {
    		ref_btnScanDevice.setDisable(true);
    	});
    	//ref_btnScanDevice.setDisable(arg0);

    }
    
    public static void EnableScanDeviceButton(){
    	Platform.runLater(() -> {
    		ref_btnScanDevice.setDisable(false);
    	});
    }

    public static void ScanDeviceCompletedPostProcess(){

        //ScanDeviceButtonStatus(true);
        EnableScanDeviceButton();
        //btnSystemSettings.setEnabled(true);
        
        
        
        //ProtampBootupDisplayVisible(View.INVISIBLE);
       // ScanTimerDisplayVisible(View.INVISIBLE);
        ApplicationHomeController.ScanTimerDisplayVisible(false);
        ApplicationHomeController.ProtampBootupDisplayVisible(false);
        try {
            bootupStatusTimer.cancel();
            bootupStatusTimerAlreadyStarted = false;
            System.out.println("ScanDeviceCompletedPostProcess: bootupStatusTimer: Timer Cancelled");
        }catch (Exception ex){
            System.out.println("ScanDeviceCompletedPostProcess: bootupStatusTimer : Exception:" + ex.getMessage());
        }
        try {
            scanDeviceTimer.cancel();
            System.out.println("ScanDeviceCompletedPostProcess : scanDeviceTimer: Timer Cancelled");
        }catch (Exception ex){
            System.out.println("ScanDeviceCompletedPostProcess: scanDeviceTimer : Exception:" + ex.getMessage());
        }
    }
    
    public static void triggerBootupStatusTimerTask() {

        if(!bootupStatusTimerAlreadyStarted) {
            bootupStatusTimerAlreadyStarted = true;
            //ProtampBootupDisplayVisible(View.VISIBLE);
            ApplicationHomeController.ProtampBootupDisplayVisible(true);
            bootupStatusTimer = new Timer();
            bootupStatusTimer.schedule(new bootupStatusTimerTask(), 0, 1000);
        }
    }

    
*/
    
    
   /* public void PwrSrcValidateSerialCmd(){
    	
    	String PowerSrcCommPortID= null;
    	String PwrSrcCommBaudRate = null;
    	txtValidatePwrSrcCmdStatus.clear();
    	try{
	    	serialDM_Obj.commPowerSrc.searchForPorts(); 
	    	PowerSrcCommPortID = getCurrentPwrSrcComPortID();
	    	PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
	    	boolean status = serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
	    	
	    	if (!status){
	    		
	    		txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		status = serialDM_Obj.SetPowerSourceOff();
	    		if (!status){
	    			txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		
	    	}
	    	ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: testD:"+serialDM_Obj.commPowerSrc.getPortDeviceMapping());
	    	serialDM_Obj.DisconnectPwrSrc();
	    	
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("PwrSrcValidateSerialCmd: Exception1:"+e.getMessage());
    		//ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: Exception:"+e.toString());
    		txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
    	}
    }*/
    
   /* public void RefStdValidateSerialCmd(){
    	
    	String RefStdCommPortID= null;
    	String RefStdCommBaudRate = null;
    	txtValidateRefStdCmdStatus.clear();
    	try{
	    	serialDM_Obj.commRefStandard.searchForPorts(); 
	    	RefStdCommPortID = getCurrentRefStdComPortID();
	    	RefStdCommBaudRate = getCurrentRefStdComBaudRate();
	    	boolean status = serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {

	    		status = serialDM_Obj.RefStd_ValidateNOP_CMD();
	    		if (!status){
	    			txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		
	    	}
	    	serialDM_Obj.DisconnectRefStd();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("RefStdValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }*/
    
    /*class MyRunnable implements Runnable{
        

        double count ;
    	TextField l_txtValidateRefStdCmdStatus;
 
        public MyRunnable(TextField ValidateRefStdCmdStatus) {
        	count = 0;
        	l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
        }
 
        @Override
        public void run() {
            for (int i = 0; i <= count; i++) {
                 
                final double update_i = count;
                 
                
            	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test2");
            	l_txtValidateRefStdCmdStatus.setText("Sending CMD"+update_i);
            	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test3");
                 
                //Update JavaFX UI with runLater() in UI thread
                Platform.runLater(new Runnable(){
 
                    @Override
                    public void run() {
                        for (int j = 0; j <= update_i; j++) {
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test2");
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test3");
	                    		ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:SleepEntry");
	                        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:Exception");
	                            
	                        
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:NextForloop");
                            
                    	}
                    }
                });
                 

            }
        }
         
    }*/
    
    
    
/*    class UI_DisplayTimerTask extends TimerTask{
    	 

        double count =10;
    	TextField l_txtValidateRefStdCmdStatus;
 
        public UI_DisplayTimerTask(TextField ValidateRefStdCmdStatus) {

        	l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
        	
        }
 
        @Override
        public void run() {
        	for(int i=0;i<count;i++){
	        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test2");
	        	l_txtValidateRefStdCmdStatus.setText("Sending CMD"+i);
	        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test3");

	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("UI_DisplayTimerTask: InterruptedException: "+e.getMessage());
				}
        	}
        	
        	UI_DisplayTimer.cancel();

             
        }
         
    }*/
    
    
/*    public void LDU_ValidateSerialCmd(){
    	String LDU_CommPortID= null;
    	String LDUCommBaudRate = null;
    	txtValidateLDU_CmdStatus.clear();
    	try{
	    	serialDM_Obj.commLDU.searchForPorts(); 
	    	LDU_CommPortID = getCurrentLDU_ComPortID();
	    	LDUCommBaudRate = getCurrentLDU_ComBaudRate();
	    	boolean status = serialDM_Obj.LDU_Init(LDU_CommPortID,LDUCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
				DisplayDataObj.set_Error_min("-1.00");
				DisplayDataObj.set_Error_max("+1.00");
				DisplayDataObj.setNoOfPulses("10");
				
				DisplayDataObj.setLDU_ReadDataFlag(true);
	    		status = serialDM_Obj.LDU_ResetSetting();
	    		if (!status){
	    			txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setLDU_ReadDataFlag(false);
	    	}
	    	serialDM_Obj.DisconnectLDU();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("LDU_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }*/
/*
	private String getCurrentPwrSrcComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentPwrSrcComPortID() {
		// TODO Auto-generated method stub
		
		return cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentRefStdComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentRefStdComPortID() {
		// TODO Auto-generated method stub
		
		return cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentLDU_ComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxLDU_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentLDU_ComPortID() {
		// TODO Auto-generated method stub
		
		return cmbBxLDU_PortSelection.getSelectionModel().getSelectedItem();
	}*/
	
/*    public void  PwrSrcValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("PwrSrcValidateSerialCmdTrigger: Invoked:");
		PwrSrcValidateTimer = new Timer();
		PwrSrcValidateTimer.schedule(new PwrSrcValidateTimerTask(),100);// 1000);
		
    }
    
    public void  RefStdValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("RefStdValidateSerialCmdTrigger: Invoked:");
    	RefStdValidateTimer = new Timer();
    	RefStdValidateTimer.schedule(new RefStdValidateTimerTask(),100);// 1000);
		
    }
    
    public void  LDU_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("LDU_ValidateSerialCmdTrigger: Invoked:");
    	LDU_ValidateTimer = new Timer();
    	LDU_ValidateTimer.schedule(new LDU_ValidateTimerTask(),100);// 1000);
		
    }*/

/*	
	class PwrSrcValidateTimerTask extends TimerTask {
		public void run() {
			btnValidatePwrSrcCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: WAIT");
			try {
				PwrSrcValidateSerialCmd();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrcValidateTimerTask: Exception:"+e.getMessage());
			}
			PwrSrcValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: DEFAULT");
			btnValidatePwrSrcCmd.setDisable(false);
		}
	}*/
	
/*	class RefStdValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateRefStdCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: WAIT");
			try {
				
				RefStdValidateSerialCmd();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("RefStdValidateTimerTask: Exception:"+e.getMessage());
			}
			RefStdValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: DEFAULT");
			btnValidateRefStdCmd.setDisable(false);
		}
	}*/
	
	/*class LDU_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateLDU_Cmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("LDU_ValidateTimerTask: WAIT");
			try {
				LDU_ValidateSerialCmd();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("LDU_ValidateTimerTask: Exception:"+e.getMessage());
			}
			LDU_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("LDU_ValidateTimerTask: DEFAULT");
			btnValidateLDU_Cmd.setDisable(false);
		}
	}*/

/*    static class scanDeviceTimerTask extends TimerTask {
		public void run() {

			ApplicationLauncher.logger.debug("InitTask : Entry");
			//ApplicationLauncher.setCursor(Cursor.WAIT);
			System.out.println("scanDeviceTimerTask: Invoked");
            scanDeviceTimerCounter++;

           // txtViewScanTimerDisplay.setText((scanDeviceTimerTimeOutInSec-scanDeviceTimerCounter)+ " Sec");
            ApplicationHomeController.update_labelBootupTimer((scanDeviceTimerTimeOutInSec-scanDeviceTimerCounter)+ " Sec");
            if(scanDeviceTimerCounter%6 == 0){
                System.out.println("scanDeviceTimerTask: Every 6 sec");
                try{
                    Trigger_Client_Get_Status.ValidateCred();
                }catch (Exception e){
                    System.out.println("scanDeviceTimerTask: Exception: "+e.getMessage());
                }
            }
            if( scanDeviceTimerCounter > scanDeviceTimerTimeOutInSec) {
                ScanDeviceCompletedPostProcess();
                ApplicationLauncher.InformUser("Device not found","Ensure equipment is with in the range or Switch OFF and Switch ON the equipment",AlertType.INFORMATION);
				
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Device not found:")
                        .setMessage("Ensure equipment is with in the range or Switch OFF and Switch ON the equipment" )

                        .setPositiveButton("ok",new DialogInterface.OnClickListener()
                        {
                            @Override

                            public void onClick(DialogInterface dialog,int which){


                            }
                        });


                AlertDialog alter = builder.create();
                alter.show();
			
			//ApplicationLauncher.setCursor(Cursor.DEFAULT);
            scanDeviceTimer.cancel();
	//	}
		}
	};*/


}
