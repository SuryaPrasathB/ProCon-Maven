package com.tasnetwork.calibration.energymeter.device;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class DisplayDeviceDataController {
	
	//String PowerSrcCommPortID=DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_POWER_SOURCE).
	String PowerSrcCommPortID  = "";//		DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_POWER_SOURCE).getString("baud_rate");
	String PwrSrcCommBaudRate ="";//DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_POWER_SOURCE).getString("baud_rate");
	String LDU_CommPortID="";//DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_LDU).getString("port_name");
	String LDUCommBaudRate ="";//DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_LDU).getString("baud_rate");
	String RefStdCommPortID="";//DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_REF_STD).getString("port_name");
	String RefStdCommBaudRate ="";//DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_REF_STD).getString("baud_rate");

/*	String PowerSrcCommPortID="COM5";
	String RefStdCommPortID="COM16";
	String LDU_CommPortID="COM3";*/
	

	
	
	@FXML TextField txtR_PhaseVolt;
	@FXML TextField txtR_PhaseCurrent;
	@FXML TextField txtR_PhasePowerFactor;
	@FXML TextField txtR_PhaseDegreePhase;
	@FXML TextField txtR_PhaseFreq;
	@FXML TextField txtR_PhaseWatt;
	@FXML TextField txtR_PhaseVA;
	@FXML TextField txtTimeLeft;
	
	public static TextField ref_txtR_PhaseVolt;
	public static TextField ref_txtR_PhaseCurrent;
	public static TextField ref_txtR_PhasePowerFactor;
	public static TextField ref_txtR_PhaseDegreePhase;
	public static TextField ref_txtR_PhaseFreq;
	public static TextField ref_txtR_PhaseWatt;
	public static TextField ref_txtR_PhaseVA;
	
	@FXML TextField txtY_PhaseVolt;
	@FXML TextField txtY_PhaseCurrent;
	@FXML TextField txtY_PhasePowerFactor;
	@FXML TextField txtY_PhaseDegreePhase;
	@FXML TextField txtY_PhaseFreq;
	@FXML TextField txtY_PhaseWatt;
	@FXML TextField txtY_PhaseVA;
	
	public static TextField ref_txtY_PhaseVolt;
	public static TextField ref_txtY_PhaseCurrent;
	public static TextField ref_txtY_PhasePowerFactor;
	public static TextField ref_txtY_PhaseDegreePhase;
	public static TextField ref_txtY_PhaseFreq;
	public static TextField ref_txtY_PhaseWatt;
	public static TextField ref_txtY_PhaseVA;
	
	@FXML TextField txtB_PhaseVolt;
	@FXML TextField txtB_PhaseCurrent;
	@FXML TextField txtB_PhasePowerFactor;
	@FXML TextField txtB_PhaseDegreePhase;
	@FXML TextField txtB_PhaseFreq;
	@FXML TextField txtB_PhaseWatt;
	@FXML TextField txtB_PhaseVA;
	
	public static TextField ref_txtB_PhaseVolt;
	public static TextField ref_txtB_PhaseCurrent;
	public static TextField ref_txtB_PhasePowerFactor;
	public static TextField ref_txtB_PhaseDegreePhase;
	public static TextField ref_txtB_PhaseFreq;
	public static TextField ref_txtB_PhaseWatt;
	public static TextField ref_txtB_PhaseVA;
	
	private Integer BufferTimeToReadLDU_DataInSec =60;
	
	public static boolean AllPortInitSuccess = false;
	private Timeline ExecuteTimeLineObj = null;
	//private static Timeline PowerSourceTimerOff = null;
	
	
	
	
	
	@FXML Button btnReadRefStdData;
	@FXML Button btnLDUInit;
	@FXML Button btnRefStdInit;
	@FXML Button btnLDU_ResetError;
	@FXML Button btnLDU_ErrorSetting;
	@FXML Button btnLDU_CommDisconnect;
	@FXML Button btnSourceOn;
	@FXML Button btnSourceOff;
	@FXML Button btnPwrSrcDisconnect;
	@FXML Button btnRefStdDisconnect;
	@FXML Button btnPwrSrcInit;
	
	
	@FXML TextField txtDisplayErrorValue1;
	@FXML TextField txtDisplayErrorStatus1;
	@FXML TextField txtDisplayErrorValue2;
	@FXML TextField txtDisplayErrorStatus2;
	
	@FXML TextField txtPwrSrcR_PhaseVolt;
	@FXML TextField txtPwrSrcR_PhaseCurrent;
	@FXML TextField txtPwrSrcR_PhasePhiAngle;
	@FXML TextField txtPwrSrcR_PhaseFreq;
	
	public static TextField ref_txtPwrSrcR_PhaseVolt;
	public static TextField ref_txtPwrSrcR_PhaseCurrent;
	public static TextField ref_txtPwrSrcR_PhasePhiAngle;
	public static TextField ref_txtPwrSrcR_PhaseFreq;
	
	
	public static TextField ref_txtDisplayErrorValue1;
	public static TextField ref_txtDisplayErrorStatus1;
	public static TextField ref_txtDisplayErrorValue2;
	public static TextField ref_txtDisplayErrorStatus2;
	
	
	
	@FXML TextField txtWarmupDuration;
	@FXML TextField txtCreepDuration;
	@FXML TextField txtCreepNoOfPulses;
	@FXML TextField txtPercentageOfVoltage;
	
	public static TextField ref_txtCreepNoOfPulses;
	public static TextField ref_txtPercentageOfVoltage;
	public static TextField ref_txtCreepDuration;
	

	
	@FXML CheckBox ckBxAddress1;
	@FXML CheckBox ckBxAddress2;
	
	public static CheckBox ref_ckBxAddress1;
	public static CheckBox ref_ckBxAddress2;
	
	int ExecuteTimeCounter = 60;
	
	
	
	public static SerialDataManager serialDM_Obj = new SerialDataManager();
	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	@FXML
	private void initialize() throws IOException {
		
		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		//mDragOverIcon = new DragIcon();
		ref_txtR_PhaseVolt = txtR_PhaseVolt;
		ref_txtR_PhaseCurrent = txtR_PhaseCurrent;
		ref_txtR_PhasePowerFactor= txtR_PhasePowerFactor;
		ref_txtR_PhaseDegreePhase= txtR_PhaseDegreePhase;
		ref_txtR_PhaseFreq = txtR_PhaseFreq;
		ref_txtR_PhaseWatt = txtR_PhaseWatt;
		ref_txtR_PhaseVA = txtR_PhaseVA;
		
		ref_txtY_PhaseVolt = txtY_PhaseVolt;
		ref_txtY_PhaseCurrent = txtY_PhaseCurrent;
		ref_txtY_PhasePowerFactor= txtY_PhasePowerFactor;
		ref_txtY_PhaseDegreePhase= txtY_PhaseDegreePhase;
		ref_txtY_PhaseFreq = txtY_PhaseFreq;
		ref_txtY_PhaseWatt = txtY_PhaseWatt;
		ref_txtY_PhaseVA = txtY_PhaseVA;
		
		ref_txtB_PhaseVolt = txtB_PhaseVolt;
		ref_txtB_PhaseCurrent = txtB_PhaseCurrent;
		ref_txtB_PhasePowerFactor= txtB_PhasePowerFactor;
		ref_txtB_PhaseDegreePhase= txtB_PhaseDegreePhase;
		ref_txtB_PhaseFreq = txtB_PhaseFreq;
		ref_txtB_PhaseWatt = txtB_PhaseWatt;
		ref_txtB_PhaseVA = txtB_PhaseVA;
		ref_txtDisplayErrorStatus1 = txtDisplayErrorStatus1;
		ref_txtDisplayErrorValue1 = txtDisplayErrorValue1;
		ref_txtDisplayErrorStatus2 = txtDisplayErrorStatus2;
		ref_txtDisplayErrorValue2 = txtDisplayErrorValue2;
		
		ref_txtPwrSrcR_PhaseVolt = txtPwrSrcR_PhaseVolt;
		ref_txtPwrSrcR_PhaseCurrent = txtPwrSrcR_PhaseCurrent;
		ref_txtPwrSrcR_PhasePhiAngle = txtPwrSrcR_PhasePhiAngle;
		ref_txtPwrSrcR_PhaseFreq = txtPwrSrcR_PhaseFreq;
		ref_ckBxAddress1 = ckBxAddress1;
		ref_ckBxAddress2 = ckBxAddress2;
		ref_txtCreepNoOfPulses = txtCreepNoOfPulses;
		ref_txtPercentageOfVoltage = txtPercentageOfVoltage;
		ref_txtCreepDuration = txtCreepDuration;
		initPort();
		//serialDM_Obj.RefStdCommInit("COM16");
		
		
		
	}
	
	public void initPort(){
		
		try{
			 PowerSrcCommPortID  =		DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_POWER_SOURCE).getString("baud_rate");
	 PwrSrcCommBaudRate =DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_POWER_SOURCE).getString("baud_rate");
	 LDU_CommPortID=DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_LDU).getString("port_name");
	 LDUCommBaudRate =DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_LDU).getString("baud_rate");
	 RefStdCommPortID=DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_REF_STD).getString("port_name");
	 RefStdCommBaudRate =DevicePortSetupController.get_device_settings(ConstantApp.SOURCE_TYPE_REF_STD).getString("baud_rate");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		ApplicationLauncher.logger.error("initPort :JSONException:"+ e.getMessage());
	}
	}
	
	public void DisplayRefTimerInit() {
		if(AllPortInitSuccess){
			serialDM_Obj.RefStdTimerInit();
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Unable to access Ref Standard Serial Port", ButtonType.OK);
			alert.showAndWait();
			
		}
		/*RefStdTimer = new Timer();
		RefStdTimer.schedule(new RefComRemindTask(), 2000);
		try {
			refComSerialStatusConnected = serialDM_Obj.RefStdCommInit(RefStdCommPortID);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(refComSerialStatusConnected){
			setRefStdReadDataFlag( true);
		}
		System.out.println("Timer Init Invoked:");*/

	}
	
	public boolean DisplayRefStdInit() {
		ApplicationLauncher.logger.debug("DisplayRefStdInit : Entry" );
		boolean status = serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
		return status;
	}
	
	
	public boolean  CloseAllComPort(){
		boolean status = false;
		
		if(AllPortInitSuccess){
			
			DisplayDisconnectRefStd();
			DisplayDisconnectLDU();
			//DisplayPwrSrc_TurnOff();
			//serialDM_Obj.SetPowerSourceOff();
			DisplayDisconnectPwrSrc();
			ApplicationLauncher.logger.debug("CloseAllComPort: Closed all serial port");
		}
		ExecuteTimeLineObj.stop();
		return status;
	}
	
	public boolean  ValidateAllComPort(){
		
		boolean status=false;
		AllPortInitSuccess = false;
		AllPortInitSuccess = true;
		if (DisplayRefStdInit()) {
			if(DisplayPwrSrc_Init()) {
				if( DisplayLDU_Init()) {
					status= true;
					AllPortInitSuccess = true;
					ApplicationLauncher.logger.debug("ValidateAllComPort: All Serial port accessable");

				}else {
					
					ApplicationLauncher.logger.debug("ValidateAllComPort: Unable to access LDU Serial Port");
					Alert alert = new Alert(AlertType.ERROR, "Unable to access LDU Serial Port", ButtonType.OK);
					alert.showAndWait();

				}
			}
			else {
				
				ApplicationLauncher.logger.debug("ValidateAllComPort: Unable to access Power Source Serial Port");
				Alert alert = new Alert(AlertType.ERROR, "Unable to access Power Source Serial Port", ButtonType.OK);
				alert.showAndWait();
			}
			
		}
		else {
			
			ApplicationLauncher.logger.debug("ValidateAllComPort: Unable to access Ref Standard Serial Port");
			Alert alert = new Alert(AlertType.ERROR, "Unable to access Ref Standard Serial Port", ButtonType.OK);
			alert.showAndWait();
		}
		
		return status;
		
	}
	
	public void WarmupExecuteStart(){
		
		Integer WarmupDuration  = get_txtWarmupDuration();
		DisplayDataObj.setR_PhaseOutputVoltage(getPwrSrcR_PhaseVolt());
		DisplayDataObj.setR_PhaseOutputCurrent(getPwrSrcR_PhaseCurrent());
		DisplayDataObj.setPowerSrcOnTimerValue(WarmupDuration);
		RefStd_PreRequisite();
		LDU_PreRequisiteForReadError();
		DisplayPwrSrc_TurnOn();
		setExecuteTimeCounter(WarmupDuration);
		ExecuteTimerDisplay();
		
	}
	
	public void RefStd_PreRequisite(){
		//serialDM_Obj.setRefStdReadDataFlag(false);
		DisplayRefTimerInit();
	}
	
	public void LDU_PreRequisiteForReadError(){
		//serialDM_Obj.setLDU_ResetErrorStatus(false);
		//serialDM_Obj.setLDU_ResetSettingStatus(false);
		DisplayLDU_ResetError();
		DisplayLDU_ResetSetting();
		DisplayLDU_ReadErrorTimerInit();
	}
	
	public void LDU_PreRequisiteForCreepTest(){
		
		//serialDM_Obj.LDU_CreepSetting();
		//serialDM_Obj.setLDU_ResetErrorStatus(false);
		DisplayLDU_ResetError();
		DisplayLDU_CreepSetting();
		DisplayLDU_ReadCreepTimerInit();

	}
	
	public void CreepExecuteStart(){
		

		float CreepVolt = 230.0f;//serialDM_Obj.CalculateCreepVoltage();
		Integer CreepDuration = get_txtCreepDuration();
		
		DisplayDataObj.setR_PhaseOutputVoltage(CreepVolt);
		DisplayDataObj.setLDU_CreepTimeDurationFormat(CreepDuration);
		DisplayDataObj.setR_PhaseOutputCurrent(0f);
		DisplayDataObj.setPowerSrcOnTimerValue(CreepDuration);
		RefStd_PreRequisite();
		LDU_PreRequisiteForCreepTest();
		//LDU_PreRequisiteForReadError();
		
		DisplayPwrSrc_TurnOn();
		setExecuteTimeCounter(CreepDuration+BufferTimeToReadLDU_DataInSec);
		ExecuteTimerDisplay();
		
	}
	
	public void setExecuteTimeCounter(Integer Counter){
		ExecuteTimeCounter = Counter;
		
	}
	
	public Integer getExecuteTimeCounter(){
		return ExecuteTimeCounter ;
		
	}
	
	
	

	
	public void set_txtCreepDuration(Integer Duration){
		ref_txtCreepDuration.setText( Duration.toString());
		
	}
	
	public static Integer get_txtCreepDuration(){
		return Integer.parseInt(ref_txtCreepDuration.getText()) ;
		
	}
	
	
	
	
	
	public void set_txtWarmupDuration(Integer Duration){
		txtWarmupDuration.setText( Duration.toString());
		
	}
	
	public Integer get_txtWarmupDuration(){
		return Integer.parseInt(txtWarmupDuration.getText()) ;
		
	}
	
	

	
	public void setCreepNoOfPulses(String NoOfPulse){
		txtCreepNoOfPulses.setText(NoOfPulse);
		
	}
	
	public static String getCreepNoOfPulses(){
		return ref_txtCreepNoOfPulses.getText() ;
		
	}
	
	
	public static float getPercentageOfVoltage(){
		return Float.valueOf(ref_txtPercentageOfVoltage.getText()) ;
		
	}
	
	public static float getPwrSrcR_PhaseVolt(){
		return Float.valueOf(ref_txtPwrSrcR_PhaseVolt.getText()) ;
		
	}
	
	public static float getPwrSrcR_PhaseCurrent(){
		return Float.valueOf(ref_txtPwrSrcR_PhaseCurrent.getText()) ;
		
	}
	
	public int DecrementExecuteTimeCounter(){
		 return --ExecuteTimeCounter;
		
	}
	
	public void ExecuteStop(){
		
		if (serialDM_Obj.getPowerSrcTurnedOnStatus()){
			if(!serialDM_Obj.isPowerSourceTurnedOff()){
				serialDM_Obj.SetPowerSourceOff();
			}
		}
		DisplayDataObj.setLDU_ReadDataFlag(false);
		DisplayDataObj.setRefStdReadDataFlag(false);
		
	}
	
	public void DisplayLDU_ReadErrorTimerInit() {
/*		lduTimer = new Timer();
		lduTimer.schedule(new lduComReadErrorTask(), 1000);*/
		if(serialDM_Obj.getLDU_ResetSettingStatus()){
			serialDM_Obj.LDU_ReadErrorTimerTrigger();
		}
		else{
			Alert alert = new Alert(AlertType.ERROR, "LDU_ResetSetting not invoked", ButtonType.OK);
			alert.showAndWait();
		}

/*		if(lduComSerialStatusConnected){
			setLDU_ReadDataFlag( true);
		}
		System.out.println("Timer Init Invoked:");*/

	}
	
	public void DisplayLDU_ReadCreepTimerInit() {

		if(serialDM_Obj.getLDU_CreepSettingStatus()){
			serialDM_Obj.LDU_ReadCreepTimerTrigger();
		}
		else{
			Alert alert = new Alert(AlertType.ERROR, "LDU_CreepSettingStatus not invoked", ButtonType.OK);
			alert.showAndWait();
		}



	}
	
	public boolean DisplayPwrSrc_Init() {
		
		boolean status =serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
		return status;
	}
	
	public boolean DisplayLDU_Init() {
		
		boolean status =serialDM_Obj.LDU_Init(LDU_CommPortID, LDUCommBaudRate);
		return status;

		/*try {

			lduComSerialStatusConnected = serialDM_Obj.LDU_CommInit(LDU_CommPortID);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

		System.out.println("DisplayLDU_Init Invoked:");*/

	}
	
    public void Sleep(int timeInMsec) {
    	
        try {
    			Thread.sleep(timeInMsec);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        
    }
	
	public boolean DisplayLDU_ResetError() {
		
		
		AllPortInitSuccess = true;
		if(AllPortInitSuccess){
			serialDM_Obj.setLDU_ResetErrorStatus(false);
			serialDM_Obj.LDU_ResetErrorTrigger();
			Integer ResetErrorStatusWaitTimeCounter = 20;
			while (ResetErrorStatusWaitTimeCounter !=0 && !serialDM_Obj.getLDU_ResetErrorStatus()){
				
				ResetErrorStatusWaitTimeCounter--;
				Sleep(250);
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Unable to access LDU Serial Port2", ButtonType.OK);
			alert.showAndWait();
		}
/*		lduTimer = new Timer();
		lduTimer.schedule(new lduComResetErrorTask(), 1000);

		System.out.println("DisplayLDU_ResetError Invoked:");*/
		return serialDM_Obj.getLDU_ResetErrorStatus();
	}
	
	
	
/*	public static void PwrSrcOffTimer(Integer StopTimerInSec){
		
		PowerSourceTimerOff = new Timeline(new KeyFrame(Duration.seconds(StopTimerInSec), new EventHandler<ActionEvent>() {
	
		    @Override
		    public void handle(ActionEvent event) {
		        //System.out.println("this is called every 1 seconds on UI thread");
		    	int CurrentTimeInSec = getExecuteTimeCounter();
		    	int sec = (CurrentTimeInSec % 60);
		        int min = ((CurrentTimeInSec / 60)%60);
		        int hours = ((CurrentTimeInSec/60)/60);

		        System.out.println("ExecuteTimerDisplay:PowerSourceTimerOff invoked ");
				if (getPowerSrcTurnedOnStatus()){
					serialDM_Obj.SetPowerSourceOff();
				}
		        PowerSourceTimerOff.stop();
		        //txtTimeLeft.setText(String.format("%02d", hours) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
		        
		        //if(DecrementExecuteTimeCounter()<0){
		        	
		        	//ExecuteStop();
		        	
		        //}
		    }
		}));
		PowerSourceTimerOff.setCycleCount(Timeline.INDEFINITE);
		//WarmupTimeLineObj.setAutoReverse(false);
		PowerSourceTimerOff.play();
	
	}*/
	
	public void ExecuteTimerDisplay(){
		
		ExecuteTimeLineObj = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
	
		    @Override
		    public void handle(ActionEvent event) {
		        //System.out.println("this is called every 1 seconds on UI thread");
		    	int CurrentTimeInSec = getExecuteTimeCounter();
		    	int sec = (CurrentTimeInSec % 60);
		        int min = ((CurrentTimeInSec / 60)%60);
		        int hours = ((CurrentTimeInSec/60)/60);

		        //System.out.println(hours + ":" + min + ":" + sec);
		        txtTimeLeft.setText(String.format("%02d", hours) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
		        
		        if(DecrementExecuteTimeCounter()<0){
		        	ExecuteTimeLineObj.stop();
		        	ExecuteStop();
		        	
		        }
		    }
		}));
		ExecuteTimeLineObj.setCycleCount(Timeline.INDEFINITE);
		//WarmupTimeLineObj.setAutoReverse(false);
		ExecuteTimeLineObj.play();
	
	}
	public boolean DisplayLDU_ResetSetting() {
		if(serialDM_Obj.getLDU_ResetErrorStatus()){
			serialDM_Obj.setLDU_ResetSettingStatus(false);
			serialDM_Obj.LDU_ResetSettingTrigger();
			Integer ResetSettingStatusWaitTimeCounter = 20;
			while (ResetSettingStatusWaitTimeCounter !=0 && !serialDM_Obj.getLDU_ResetSettingStatus()){
				//System.out.println("DisplayLDU_ResetSetting awaiting:");
				ResetSettingStatusWaitTimeCounter--;
				Sleep(250);
			}
			//System.out.println("DisplayLDU_ResetSetting Exit:");
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "LDU_ResetError not invoked", ButtonType.OK);
			alert.showAndWait();
		}
		
		return serialDM_Obj.getLDU_ResetSettingStatus();


	}
	
	public boolean DisplayLDU_CreepSetting() {
		if(serialDM_Obj.getLDU_ResetErrorStatus()){
			serialDM_Obj.setLDU_CreepSettingStatus(false);
			serialDM_Obj.LDU_CreepSettingTrigger();
			Integer CreepSettingStatusWaitTimeCounter = 40;
			while (CreepSettingStatusWaitTimeCounter !=0 && !serialDM_Obj.getLDU_CreepSettingStatus()){
				//System.out.println("DisplayLDU_ResetSetting awaiting:");
				CreepSettingStatusWaitTimeCounter--;
				Sleep(250);
			}
			//System.out.println("DisplayLDU_ResetSetting Exit:");
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "LDU_ResetError not invoked", ButtonType.OK);
			alert.showAndWait();
		}
		
		return serialDM_Obj.getLDU_CreepSettingStatus();
		
	}
	

	


	
	


	

	
	public static void DeviceRefStdDataDisplayUpdate_R_Phase(SerialDataRadiantRefStd R_PhaseData){

		
		ref_txtR_PhaseVolt.setText(R_PhaseData.VoltageDisplayData);
		ref_txtR_PhaseCurrent.setText(R_PhaseData.CurrentDisplayData);
		ref_txtR_PhasePowerFactor.setText(R_PhaseData.PowerFactorData);
		ref_txtR_PhaseFreq.setText(R_PhaseData.FreqDisplayData);
		ref_txtR_PhaseWatt.setText(R_PhaseData.WattDisplayData);
		ref_txtR_PhaseVA.setText(R_PhaseData.VA_DisplayData);
		ref_txtR_PhaseDegreePhase.setText(R_PhaseData.DegreePhaseData);


	}
	
	public static void DeviceRefStdDataDisplayUpdate_Y_Phase(SerialDataRadiantRefStd Y_PhaseData){

		

		
		ref_txtY_PhaseVolt.setText(Y_PhaseData.VoltageDisplayData);
		ref_txtY_PhaseCurrent.setText(Y_PhaseData.CurrentDisplayData);
		ref_txtY_PhasePowerFactor.setText(Y_PhaseData.PowerFactorData);
		ref_txtY_PhaseFreq.setText(Y_PhaseData.FreqDisplayData);
		ref_txtY_PhaseWatt.setText(Y_PhaseData.WattDisplayData);
		ref_txtY_PhaseVA.setText(Y_PhaseData.VA_DisplayData);
		ref_txtY_PhaseDegreePhase.setText(Y_PhaseData.DegreePhaseData);



	}
	
	public static void DeviceRefStdDataDisplayUpdate_B_Phase(SerialDataRadiantRefStd B_PhaseData){

		
		
		ref_txtB_PhaseVolt.setText(B_PhaseData.VoltageDisplayData);
		ref_txtB_PhaseCurrent.setText(B_PhaseData.CurrentDisplayData);
		ref_txtB_PhasePowerFactor.setText(B_PhaseData.PowerFactorData);
		ref_txtB_PhaseFreq.setText(B_PhaseData.FreqDisplayData);
		ref_txtB_PhaseWatt.setText(B_PhaseData.WattDisplayData);
		ref_txtB_PhaseVA.setText(B_PhaseData.VA_DisplayData);
		ref_txtB_PhaseDegreePhase.setText(B_PhaseData.DegreePhaseData);


	}
	
	public static void DeviceErrorDisplayUpdate(int LDU_ReadAdress,String Resultstatus,String ErrorValue){
		if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
			if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}
		}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
			if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}
		}
		switch(LDU_ReadAdress){
		   case 1 :
				ref_txtDisplayErrorStatus1.setText(Resultstatus);
				ref_txtDisplayErrorValue1.setText(ErrorValue);
			      break; // break is optional
			   
		   case 2 :
			   ref_txtDisplayErrorStatus2.setText(Resultstatus);
			   ref_txtDisplayErrorValue2.setText(ErrorValue);
			      // Statements
			      break; // break is optional
			   
			   // We can have any number of case statements
			   // below is default statement,used when none of the cases is true. 
			   // No break is needed in the default case.
			   default : 
			      // Statements
		}

	}
	
	public static ArrayList<Boolean> getDeviceMountedList() {
		/* stub function to load the devicemountedlist*/
		ArrayList<Boolean> IsDeviceMounted = new ArrayList<Boolean>();
		//IsDeviceMounted.add(true);
		//IsDeviceMounted.add(false);
		IsDeviceMounted.add(ref_ckBxAddress1.isSelected());
		IsDeviceMounted.add(ref_ckBxAddress2.isSelected());
		return IsDeviceMounted;
	}
	
	public void  DisplayDisconnectLDU(){
		serialDM_Obj.DisconnectLDU();
		DisplayDataObj.setLDU_ReadDataFlag( false);
	}
	
	public void  DisplayDisconnectRefStd(){
		serialDM_Obj.DisconnectRefStd();
		DisplayDataObj.setRefStdReadDataFlag(false);
	}
	
	public void  DisplayDisconnectPwrSrc (){
		serialDM_Obj.DisconnectPwrSrc();
	}
	

/*	
	public void PwrSrc_TurnOn(){
		
		String Voltage1 = txtPwrSrcR_PhaseVolt.getText();
		String Current1 = txtPwrSrcR__PhaseCurrent.getText();
		String Phi1 = txtPwrSrcR__PhasePhiAngle.getText();
		String Freq = txtPwrSrcR__PhaseFreq.getText();
		

		serialDM_Obj.SetPowerSourceOn("COM5",Voltage1,Current1,Phi1,Freq);
	}
	
	public void PwrSrc_TurnOff(){
		
		serialDM_Obj.SetPowerSourceOff("COM5");
	}*/
	
	public void DisplayPwrSrc_TurnOn(){
		
/*		String Voltage1 = txtPwrSrcR_PhaseVolt.getText();
		String Current1 = txtPwrSrcR__PhaseCurrent.getText();
		String Phi1 = txtPwrSrcR__PhasePhiAngle.getText();
		String Freq = txtPwrSrcR__PhaseFreq.getText();*/
		serialDM_Obj.setPowerSrcTurnedOnStatus(false);
		serialDM_Obj.PwrSrcON_Trigger();//PowerSrcCommPortID);
		//serialDM_Obj.SetPowerSourceOn("COM5",Voltage1,Current1,Phi1,Freq);
	}
	
	public void DisplayPwrSrc_TurnOff(){
		
		serialDM_Obj.PwrSrcOFF_Trigger();//PowerSrcCommPortID);
	}
	

	
	public static String getPowerData(TextField refTextField){
		return refTextField.getText();
	}
	
	
	

	
	public void StopReadingRefStdData(){
		System.out.println("Stopped Reading Ref Data!");
		
		DisplayDataObj.setRefStdReadDataFlag( false);
	}
	
	public void StopReadingLDU_ErrorReadData(){
		System.out.println("Stopped Reading LDU_ErrorReadData!");
		
		DisplayDataObj.setLDU_ReadDataFlag( false);
	}
	

	

	
	

	
	

	
	


}
