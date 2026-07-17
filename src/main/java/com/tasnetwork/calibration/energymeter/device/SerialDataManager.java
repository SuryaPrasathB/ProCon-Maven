package com.tasnetwork.calibration.energymeter.device;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKiggs;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.deployment.DeploymentManagerController;
import com.tasnetwork.calibration.energymeter.deployment.DisplayInstantMetricsController;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.MeterParamsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.director.RefStdDirector;
import com.tasnetwork.calibration.energymeter.message.IctKreMessage;
import com.tasnetwork.calibration.energymeter.message.RefStdKiggsMessage;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.message.RefStdSandsMessage;
import com.tasnetwork.calibration.energymeter.message.lscsPowerSourceHarmonicsMessage;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerPwrSrc;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;
import com.tasnetwork.calibration.energymeter.testprofiles.PropertyHarmonicControllerV2;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class SerialDataManager {

	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();

	public static Communicator commPowerSrc = null;
	public volatile static Communicator commRefStandard = null;
	public static Communicator commLDU= null;



	public static Communicator commICT = null;
	public static Communicator  commHarmonicsSrc = null;

	SerialDataSandsRefStd lastSandsRefStdObj = new SerialDataSandsRefStd(commRefStandard);
	private static String lastReadRefStdData = "";


	public static boolean refComSerialStatusConnected = false;
	Timer RefStdTimer;
	Timer lscsPowerSrcReadFeedBackTimer;
	Timer RefStdBNC_ConstTimer;
	public static boolean bInitOccured = false;
	public static boolean SkipCurrentTP_Execution = false;
	private static int SerialRefComInstantMetricsRefreshDefaultTimeInMsec=800;//1200;//400;//800;//800;//500;//200;//20000;
	private static int SerialRefComInstantMetricsRefreshTimeInMsec=800;//1200;//400;//800;//500;//200;//500;//200;//20000; with 500 no errors observed
	private static int SerialLscsPowerSrcInstantMetricsRefreshTimeInMsec=800;//1200;//500;//800//1200
	//private static int SerialRefComKreInstantMetricsRefreshTimeInMsec=800;//5000;
	public static volatile boolean RefStdComSemlock = true;
	public static boolean LDU_ComSemlock = true;
	public static volatile boolean powerSrcComSemlock = true;


	Timer lduTimer;
	public static boolean lduComSerialStatusConnected = false;

	public static boolean ictComSerialStatusConnected = false;
	public static boolean harmonicsSrcComSerialStatusConnected = false;


	private static int SerialLDU_ComRefreshDefaultTimeInMsec=500;//1000;//200;//20000;
	private static int SerialLDU_ComRefreshTimeInMsec=500;//1000;//200;//20000;
	Timer pwrSrcTimer;
	public static boolean pwrSrcComSerialStatusConnected = false;
	public boolean LDU_ResetErrorStatus = false;
	public boolean LDU_ResetSettingStatus = false;
	public boolean LDU_CreepSettingStatus = false;
	public boolean LDU_ConstSettingStatus = false;
	public boolean LDU_STASettingStatus = false;
	public boolean LDU_AccuracySettingStatus = false;

	private static Timeline PowerSourceTimerOffObj = null;
	public static boolean bPowerSrcTurnedOnStatus = false;
	public static volatile boolean powerSourceTurnedOff = false;
	private static String BNC_OutputPortActive = "";
	private static String BNC_OutputPortReactive = "";
	public static volatile boolean bPowerSrcErrorResponseReceivedStatus = false;
	public boolean RefAccumulateSettingStatus = false;
	public boolean RefAccumlateStartStatus = false;
	public boolean RefBNC_OutputStatus = false;

	public static boolean ReadRefReadingStatus = false;

	public static boolean BNC_ConfigConstantStatus = false;

	public static volatile boolean PowerSrcOnFlag = false;

	public static boolean TimeExtendedForTimeBased = false;

	public static int TimeToBeExtendedInSec=120;

	public static int PowerSrcFreqSetFailureRetry= 3;
	private static String AccumlativeDataReadCommandType = ConstantRefStdRadiant.CMD_REF_STD_READ_WATT_HOUR_READING;




	public SerialDataManager(){
		if(!bInitOccured){
			try{
				InitSerialCommPort();
				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
					//setSerialRefComInstantMetricsRefreshTimeInMsec(SerialRefComKreInstantMetricsRefreshTimeInMsec);
					SerialRefComInstantMetricsRefreshTimeInMsec = 5;
					SerialRefComInstantMetricsRefreshDefaultTimeInMsec =5;
				}
			}catch (UnsatisfiedLinkError e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("SerialDataManager: exception:" + e.getMessage());
				ApplicationLauncher.InformUser("SerialPort search failed","Kindly ensure <rxtxSerial.dll> file is placed under path <C:\\Program Files\\Java\\jre1.8.0_161\\bin\\> folder. \n Additional Info:\n\n" + e.getMessage(),AlertType.ERROR);
			}
			bInitOccured= true;
			SetBNC_OutputPortData();
		}

	}

	public void clearPowerSourceSerialData(){
		Communicator SerialPortObj =commPowerSrc;
		commPowerSrc.ClearSerialData();
	}

	public static void SetBNC_OutputPortData(){
		switch(ConstantAppConfig.BNC_OUTPORT_PORT ){

		case 1:
			BNC_OutputPortActive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT1_ACTIVE;
			BNC_OutputPortReactive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT1_REACTIVE;
			/*					if(ConstantFeatureEnable.RADIANT_REF_STD_2X_FAMILY_ENABLED){
						BNC_OutputPortActive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT1_ACTIVE;
						BNC_OutputPortReactive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT1_REACTIVE;
					}*/
			break;
		case 2:
			BNC_OutputPortActive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT2_ACTIVE;
			BNC_OutputPortReactive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT2_REACTIVE;
			/*					if(ConstantFeatureEnable.RADIANT_REF_STD_2X_FAMILY_ENABLED){
						BNC_OutputPortActive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT2_ACTIVE;
						BNC_OutputPortReactive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT2_REACTIVE;
					}*/
			break;
		case 3:
			BNC_OutputPortActive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT3_ACTIVE;
			BNC_OutputPortReactive = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_PORT3_REACTIVE;
			/*					if(ConstantFeatureEnable.RADIANT_REF_STD_2X_FAMILY_ENABLED){
						BNC_OutputPortActive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT3_ACTIVE;
						BNC_OutputPortReactive = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_PORT3_REACTIVE;
					}*/
			break;
		}

	}


	public static void setPowerSrcTurnedOnStatus(boolean status){
		bPowerSrcTurnedOnStatus = status;

	}

	public static boolean getPowerSrcTurnedOnStatus(){
		return bPowerSrcTurnedOnStatus ;

	}



	public static void setPowerSrcErrorResponseReceivedStatus(boolean status){
		bPowerSrcErrorResponseReceivedStatus = status;

	}

	public static boolean getPowerSrcErrorResponseReceivedStatus(){
		return bPowerSrcErrorResponseReceivedStatus ;

	}

	public static void setSerialRefComInstantMetricsRefreshTimeInMsec(int TimeInMSec){
		SerialRefComInstantMetricsRefreshTimeInMsec = TimeInMSec;

	}

	public static void resetSerialRefComInstantMetricsRefreshTimeInMsec(){
		/*		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			SerialRefComInstantMetricsRefreshTimeInMsec  = SerialRefComKreInstantMetricsRefreshTimeInMsec;
		}else{*/
		SerialRefComInstantMetricsRefreshTimeInMsec = SerialRefComInstantMetricsRefreshDefaultTimeInMsec;
		//}

	}

	public static int getSerialRefComInstantMetricsRefreshTimeInMsec(){
		return SerialRefComInstantMetricsRefreshTimeInMsec ;

	}


	public static void setSerialLDU_ComRefreshTimeInMsec(int TimeInMSec){
		SerialLDU_ComRefreshTimeInMsec = TimeInMSec;

	}

	public static void resetSerialLDU_ComRefreshTimeInMsec(){
		SerialLDU_ComRefreshTimeInMsec = SerialLDU_ComRefreshDefaultTimeInMsec;

	}

	public static int getSerialLDU_ComRefreshTimeInMsec(){
		return SerialLDU_ComRefreshTimeInMsec ;

	}


	public void setRefAccumulateSettingStatus(boolean status){
		RefAccumulateSettingStatus = status;

	}

	public boolean getRefAccumulateSettingStatus(){
		return RefAccumulateSettingStatus ;

	}

	public void setRefAccumlateStartStatus(boolean status){
		RefAccumlateStartStatus = status;

	}

	public boolean getRefAccumlateStartStatus(){
		return RefAccumlateStartStatus ;

	}

	public void setReadRefReadingStatus(boolean status){
		ReadRefReadingStatus = status;

	}

	public static boolean getReadRefReadingStatus(){
		return ReadRefReadingStatus ;

	}



	public void setBNC_ConfigConstantStatus(boolean status){
		BNC_ConfigConstantStatus = status;

	}

	public static boolean getBNC_ConfigConstantStatus(){
		return BNC_ConfigConstantStatus ;

	}

	public void setRefAccumlateStopStatus(boolean status){
		RefAccumlateStartStatus = status;

	}

	public boolean getRefAccumlateStopStatus(){
		return RefAccumlateStartStatus;
	}

	public void setRefBNC_OutputStatus(boolean status){
		RefBNC_OutputStatus = status;

	}

	public boolean getRefBNC_OutputStatus(){
		return RefBNC_OutputStatus;
	}


	public void LDU_SendCommandReadErrorData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :Entry");
		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :LDU_ReadAddress:"+LDU_ReadAddress);
		ApplicationLauncher.logger.debug("LDU_SendCommandReadErrorData :LDU_ReadAddress:Hex:"+String.format("%02d" , LDU_ReadAddress));
		String Data = ConstantLduCcube.CMD_LDU_READ_ERROR_DATA_HDR+ ConstantLduCcube.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantLduCcube.CMD_LDU_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;


		commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		commLDU.SetRTS(false);
	}

	public void lscsLDU_SendCommandReadErrorData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadErrorData :Entry");
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadErrorData :LDU_ReadAddress:"+LDU_ReadAddress);
		//ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadErrorData :LDU_ReadAddress:Hex:"+String.format("%02d" , LDU_ReadAddress));
		//String Data = ConstantCcubeLDU.CMD_LDU_READ_ERROR_DATA_HDR+ ConstantCcubeLDU.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantCcubeLDU.CMD_LDU_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;
		String Data = lscsLduAddressMapping(LDU_ReadAddress);

		//commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		//commLDU.SetRTS(false);
	}

	public String lscsLduAddressMapping(int LDU_ReadAddress){
		/*if (LDU_ReadAddress == 1){
			return ConstantLscsLDU.CMD_LDU_POSITION_01_HDR;//"b";FXBGFV
		}else if (LDU_ReadAddress == 2){
			return ConstantLscsLDU.CMD_LDU_POSITION_01_HDR;//return "c";
		}if (LDU_ReadAddress == 3){
			return ConstantLscsLDU.CMD_LDU_POSITION_01_HDR;//return "d";
		}

		return "b";
		 */
		String positionValue = ConstantLduLscs.CMD_LDU_POSITION_01_HDR;
		switch (LDU_ReadAddress) {



		case 1:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_01_HDR;
			break;


		case 2:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_02_HDR;
			break;

		case 3:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_03_HDR;
			break;


		case 4:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_04_HDR;
			break;

		case 5:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_05_HDR;
			break;


		case 6:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_06_HDR;
			break;

		case 7:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_07_HDR;
			break;


		case 8:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_08_HDR;
			break;

		case 9:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_09_HDR;
			break;


		case 10:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_10_HDR;
			break;

		case 11:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_11_HDR;
			break;


		case 12:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_12_HDR;
			break;

		case 13:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_13_HDR;
			break;


		case 14:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_14_HDR;
			break;

		case 15:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_15_HDR;
			break;


		case 16:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_16_HDR;
			break;

		case 17:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_17_HDR;
			break;


		case 18:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_18_HDR;
			break;

		case 19:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_19_HDR;
			break;


		case 20:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_20_HDR;
			break;

		case 21:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_21_HDR;
			break;


		case 22:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_22_HDR;
			break;

		case 23:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_23_HDR;
			break;


		case 24:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_24_HDR;
			break;

		case 25:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_25_HDR;
			break;


		case 26:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_26_HDR;
			break;

		case 27:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_27_HDR;
			break;


		case 28:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_28_HDR;
			break;

		case 29:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_29_HDR;
			break;


		case 30:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_30_HDR;
			break;

		case 31:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_31_HDR;
			break;


		case 32:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_32_HDR;
			break;

		case 33:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_33_HDR;
			break;


		case 34:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_34_HDR;
			break;

		case 35:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_35_HDR;
			break;


		case 36:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_36_HDR;
			break;

		case 37:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_37_HDR;
			break;


		case 38:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_38_HDR;
			break;

		case 39:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_39_HDR;
			break;


		case 40:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_40_HDR;
			break;

		case 41:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_41_HDR;
			break;


		case 42:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_42_HDR;
			break;

		case 43:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_43_HDR;
			break;


		case 44:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_44_HDR;
			break;

		case 45:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_45_HDR;
			break;


		case 46:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_46_HDR;
			break;

		case 47:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_47_HDR;
			break;


		case 48:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_48_HDR;
			break;



		default:
			positionValue = ConstantLduLscs.CMD_LDU_POSITION_01_HDR;
			break;
		}

		return positionValue;

	}


	public void LDU_SendCommandReadCreepData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_SendCommandReadCreepData :Entry");
		String Data = ConstantLduCcube.CMD_LDU_READ_CREEP_DATA_HDR+ ConstantLduCcube.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantLduCcube.CMD_LDU_READ_CREEP_DATA_CMD_DATA+ConstantApp.END_CR2;
		commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		commLDU.SetRTS(false);

	}



	public void lscsLDU_SendCommandReadCreepData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadCreepData :Entry");
		//String Data = ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_HDR+ ConstantCcubeLDU.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_CMD_DATA+ConstantApp.END_CR2;
		//commLDU.SetRTS(true);
		//WriteToSerialCommLDU(Data);
		//commLDU.SetRTS(false);
		String Data = lscsLduAddressMapping(LDU_ReadAddress);
		WriteToSerialCommLDU(Data);

	}

	public void lscsLDU_SendCommandReadSTA_Data(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadSTA_Data :Entry");
		//String Data = ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_HDR+ ConstantCcubeLDU.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_CMD_DATA+ConstantApp.END_CR2;
		//commLDU.SetRTS(true);
		//WriteToSerialCommLDU(Data);
		//commLDU.SetRTS(false);
		String Data = lscsLduAddressMapping(LDU_ReadAddress);
		WriteToSerialCommLDU(Data);

	}

	public void lscsLDU_SendCommandReadConst_Data(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandReadConst_Data :Entry");
		//String Data = ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_HDR+ ConstantCcubeLDU.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantCcubeLDU.CMD_LDU_READ_CREEP_DATA_CMD_DATA+ConstantApp.END_CR2;
		//commLDU.SetRTS(true);
		//WriteToSerialCommLDU(Data);
		//commLDU.SetRTS(false);
		String Data = lscsLduAddressMapping(LDU_ReadAddress);
		WriteToSerialCommLDU(Data);

	}


	public void LDU_SendCommandReadConstData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_SendCommandReadConstData :Entry");
		String Data = ConstantLduCcube.CMD_LDU_READ_CONST_DATA_HDR+ ConstantLduCcube.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantLduCcube.CMD_LDU_READ_CONST_DATA_CMD_DATA+ConstantApp.END_CR2;
		commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		commLDU.SetRTS(false);

	}

	public void LDU_SendCommandReadAccuracyData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_SendCommandReadAccuracyData :Entry");
		String Data = ConstantLduCcube.CMD_LDU_READ_ERROR_DATA_HDR+ ConstantLduCcube.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantLduCcube.CMD_LDU_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;

		commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		commLDU.SetRTS(false);
	}


	public void lsLDU_ClearSerialData(){
		commLDU.ClearSerialData();
	}
	public void lsLDU_SendCommandReadAccuracyData(String LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lsLDU_SendCommandReadAccuracyData :Entry");
		ApplicationLauncher.logger.debug("lsLDU_SendCommandReadAccuracyData :LDU_ReadAddress : " + LDU_ReadAddress);

		String Data = LDU_ReadAddress;

		//commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		//commLDU.SetRTS(false);
	}
	public void LDU_SendCommandErrorSetting(String MeterAddress, String meter_const){
		ApplicationLauncher.logger.debug("LDU_SendCommandErrorSetting :Entry");
		String MeterConstUnit = "00";
		//String RSS_PulseRate = DisplayDataObj.getRSSPulseRate();

		//long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		/*		if(ConstantFeatureEnable.REF_STD_CONST_CALCULATE){
			if (!DevicePortSetupController.getPortValidationTurnedON()){
				//long RssConstantInWh = calculateRSS_ConstantV4(meter_const);
				RssConstantInKWh = calculateRSS_ConstantV4(meter_const)*1000;
			}
		}*/
		ApplicationLauncher.logger.debug("LDU_SendCommandErrorSetting :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
		String RSS_PulseRate = GuiUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
		String AverageCycle = "";
		String CalculateMode = "00";
		if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED)){
			AverageCycle = GuiUtils.FormatAvgPulses(DeviceDataManagerController.getNoOfPulses());
			ApplicationLauncher.logger.info("LDU_SendCommandErrorSetting:AverageCycle-Pulse-Based : " + AverageCycle);
			CalculateMode = "00";
		}
		else{
			AverageCycle = GuiUtils.FormatTimeForAvgPulses(DeviceDataManagerController.getInfTimeDuration());
			ApplicationLauncher.logger.info("LDU_SendCommandErrorSetting:AverageCycle-Time-Based :  " + AverageCycle);
			CalculateMode = "01";
		}



		String ErrorHighLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_max());
		String ErrorLowLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_min());
		String MUT_PulseRate = GuiUtils.FormatPulseRate(meter_const);
		ApplicationLauncher.logger.info("LDU_SendCommandErrorSetting: get_No_of_impulses: " + DeviceDataManagerController.getDutImpulsesPerUnit());
		ApplicationLauncher.logger.info("LDU_SendCommandErrorSetting: MUT_PulseRate: " + MUT_PulseRate);
		ApplicationLauncher.logger.info("LDU_SendCommandErrorSetting :RSS_PulseRate: " + RSS_PulseRate);
		ApplicationLauncher.logger.info("ErrorHighLimit1: " + ErrorHighLimit);
		ApplicationLauncher.logger.info("ErrorLowLimit1: " + ErrorLowLimit);  
		commLDU.SetRTS(true);
		LDU_SendMeterAddress(MeterAddress);
		LDU_SendMeterConstantUnit(MeterConstUnit);
		LDU_SetRSS_PulseRate(RSS_PulseRate);
		LDU_SetMUT_PulseRate(MUT_PulseRate);
		LDU_SetAverageCycle(AverageCycle);
		LDU_SetErrorHighLimit(ErrorHighLimit);
		LDU_SetErrorLowLimit(ErrorLowLimit);
		LDU_SetErrorCalculationMode(CalculateMode);
		commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("LDU DataSend Completed");



	}


	public void lscsLDU_SendCommandErrorSetting(String MeterAddress, String meter_const){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSetting :Entry");
		String MeterConstUnit = "00";
		//String RSS_PulseRate = DisplayDataObj.getRSSPulseRate();

		//long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		long RssConstantInKWh = 0L;
		/*		if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
			RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate());
		}else{*/
		RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		//		}
		/*		if(ConstantFeatureEnable.REF_STD_CONST_CALCULATE){
			if (!DevicePortSetupController.getPortValidationTurnedON()){
				//long RssConstantInWh = calculateRSS_ConstantV4(meter_const);
				RssConstantInKWh = calculateRSS_ConstantV4(meter_const)*1000;
			}
		}*/
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSetting :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
		//String RSS_PulseRate = GUIUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
		String AverageCycle = "";
		//String CalculateMode = "00";
		if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED)){
			//AverageCycle = GUIUtils.FormatAvgPulses(DeviceDataManagerController.getNoOfPulses());
			AverageCycle = DeviceDataManagerController.getNoOfPulses();
			ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting:AverageCycle-Pulse-Based : NoOfPulses: " + AverageCycle);
			//CalculateMode = "00";
		}else if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
			int timeDurationInSec  = DisplayDataObj.getInfTimeDuration();
			float totalTargetPowerInKiloWatt = calculateTotalTargetPower();
			int meterConstantInImpulsesPerKiloWattHour = Integer.parseInt(meter_const);
			AverageCycle = DisplayDataObj.manipulateNoOfPulsesForTimeBased(meterConstantInImpulsesPerKiloWattHour,timeDurationInSec,totalTargetPowerInKiloWatt); 
			ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting:AverageCycle-Time-Based : NoOfPulses: " + AverageCycle);
			//CalculateMode = "01";
		}
		/*		else{
			//AverageCycle = GUIUtils.FormatTimeForAvgPulses(DeviceDataManagerController.getInfTimeDuration());
			AverageCycle = DeviceDataManagerController.getNoOfPulses();bnbn n
			ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting:AverageCycle-Time-Based :  " + AverageCycle);
			//CalculateMode = "01";
		}*/



		String ErrorHighLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_max());
		String ErrorLowLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_min());
		//String MUT_PulseRate = GUIUtils.FormatPulseRate(meter_const);
		ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting: get_No_of_impulses: " + DeviceDataManagerController.getDutImpulsesPerUnit());
		//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting: MUT_PulseRate: " + MUT_PulseRate);
		//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting :RSS_PulseRate: " + RSS_PulseRate);
		ApplicationLauncher.logger.info("ErrorHighLimit1: " + ErrorHighLimit);
		ApplicationLauncher.logger.info("ErrorLowLimit1: " + ErrorLowLimit);  

		int RssPulsePerMutPulse = 0;
		RssPulsePerMutPulse = ceigLDU_SettingCalculationMethod( RssConstantInKWh, meter_const, AverageCycle);
		String rssPulsePerMutPulseStr = String.valueOf(RssPulsePerMutPulse);
		lscsLDU_SendCeigSettingMethod(rssPulsePerMutPulseStr,AverageCycle);
		//commLDU.SetRTS(true);
		//LDU_SendMeterAddress(MeterAddress);
		//LDU_SendMeterConstantUnit(MeterConstUnit);
		//LDU_SetRSS_PulseRate(RSS_PulseRate);
		//LDU_SetMUT_PulseRate(MUT_PulseRate);
		//LDU_SetAverageCycle(AverageCycle);
		//LDU_SetErrorHighLimit(ErrorHighLimit);
		//LDU_SetErrorLowLimit(ErrorLowLimit);
		//LDU_SetErrorCalculationMode(CalculateMode);
		//commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSetting: lscsLDU setting DataSend Completed");



	}

	public int ceigLDU_SettingCalculationMethod(long RssConstantInKWh,String meter_const,String AverageCycle){
		//int RssPulsePerMutPulse = (int)(RssConstantInKWh / Float.parseFloat(meter_const))* Integer.parseInt(AverageCycle);
		float RssPulsePerMutPulseFlt = ( ((float)RssConstantInKWh) / Float.parseFloat(meter_const))* Float.parseFloat(AverageCycle); // updated in version s4.2.0.9.0.3
		ApplicationLauncher.logger.info("ceigLDU_SettingCalculationMethod: RssPulsePerMutPulseFlt: " + RssPulsePerMutPulseFlt);
		int RssPulsePerMutPulse = (int) RssPulsePerMutPulseFlt;
		ApplicationLauncher.logger.info("ceigLDU_SettingCalculationMethod: RssPulsePerMutPulse: " + RssPulsePerMutPulse);
		return RssPulsePerMutPulse;
	}



	public String getCreepPulseCount(){

		ApplicationLauncher.logger.debug("getCreepPulseCount :Entry");
		return String.format("%03d", Integer.parseInt(DeviceDataManagerController.getCreepNoOfPulses()));
	}

	public String getSTAPulseCount(){

		ApplicationLauncher.logger.debug("getSTAPulseCount :Entry");	
		return String.format("%03d", Integer.parseInt(DeviceDataManagerController.getSTANoOfPulses()));
	}

	public String getAccuracyPulseCount(){

		ApplicationLauncher.logger.debug("getAccuracyPulseCount :Entry");	
		return String.format("%04d", Integer.parseInt(DeviceDataManagerController.getNoOfPulses()));
	}

	public void LDU_SendCommandCreepSetting(){
		ApplicationLauncher.logger.debug("LDU_SendCommandCreepSetting :Entry");
		String AllMeterAddress = "00";
		String CreepTime = "0100";//"1 minutes 00 seconds"
		CreepTime = DisplayDataObj.getLDU_CreepTimeDurationFormat();
		String CreepPulseCount = "001";
		CreepPulseCount = getCreepPulseCount();
		ApplicationLauncher.logger.info("LDU_SendCommandCreepSetting:CreepTime:"+CreepTime+":CreepPulseCount:"+CreepPulseCount);
		String CreepStartData = "01";

		commLDU.SetRTS(true);
		LDU_SendMeterAddress(AllMeterAddress);
		LDU_SendCreepTime(CreepTime);
		LDU_SendPulseCount(CreepPulseCount);
		LDU_SendStartCreep(CreepStartData);
		commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("LDU_SendCommandCreepSetting DataSend Completed");


	}

	public void lscsLDU_SendCommandCreepSetting(){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandCreepSetting :Entry");
		String AllMeterAddress = "00";
		String CreepTime = "0100";//"1 minutes 00 seconds"
		CreepTime = DisplayDataObj.getLDU_CreepTimeDurationFormat();
		String CreepPulseCount = "001";
		CreepPulseCount = getCreepPulseCount();
		ApplicationLauncher.logger.info("lscsLDU_SendCommandCreepSetting:"+CreepTime+":CreepPulseCount:"+CreepPulseCount);
		String CreepStartData = "01";

		//commLDU.SetRTS(true);
		//LDU_SendMeterAddress(AllMeterAddress);
		//LDU_SendCreepTime(CreepTime);
		//LDU_SendPulseCount(CreepPulseCount);
		lscsLDU_SendStartCreep();
		//commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandCreepSetting DataSend Completed");


	}

	public void lscsLDU_SendCommandSTA_Setting(){
		String AllMeterAddress = "00";
		String STATime = "0100";//"1 minutes 00 seconds"
		STATime = DisplayDataObj.getLDU_STATimeDurationFormat();//getLDU_STATimeDurationFormat();
		String STAPulseCount = "001";
		STAPulseCount = getSTAPulseCount();
		ApplicationLauncher.logger.info("lscsLDU_SendCommandSTA_Setting: STATime:"+STATime+":STAPulseCount :"+STAPulseCount);
		String STAStartData = "02";

		lscsLDU_SendStartSTA();
		/*		commLDU.SetRTS(true);
		LDU_SendMeterAddress(AllMeterAddress);
		LDU_SendSTATime(STATime);
		LDU_SendPulseCount(STAPulseCount);
		LDU_SendStartSTA(STAStartData);
		commLDU.SetRTS(false);*/
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandSTA_Setting DataSend Completed");


	}



	public void lscsLDU_SendCommandConstTest_Setting(){
		String AllMeterAddress = "00";
		//String STATime = "0100";//"1 minutes 00 seconds"
		//STATime = DisplayDataObj.getLDU_STATimeDurationFormat();//getLDU_STATimeDurationFormat();
		//String STAPulseCount = "001";
		//STAPulseCount = getSTAPulseCount();
		//ApplicationLauncher.logger.info("lscsLDU_SendCommandSTA_Setting: STATime:"+STATime+":STAPulseCount :"+STAPulseCount);
		//String STAStartData = "02";

		lscsLDU_SendStartConstTest();

		ApplicationLauncher.logger.debug("lscsLDU_SendCommandConstTest_Setting DataSend Completed");


	}





	public void LDU_SendCommandConstSetting(){
		ApplicationLauncher.logger.debug("LDU_SendCommandConstSetting :Entry");

		String AllMeterAddress = "00";


		commLDU.SetRTS(true);
		LDU_SendMeterAddress(AllMeterAddress);
		LDU_SendStartConst();
		commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("LDU_SendCommandConstSetting DataSend Completed");


	}

	public void LDU_SendCommandSTASetting(){
		String AllMeterAddress = "00";
		String STATime = "0100";//"1 minutes 00 seconds"
		STATime = DisplayDataObj.getLDU_STATimeDurationFormat();
		String STAPulseCount = "001";
		STAPulseCount = getSTAPulseCount();
		ApplicationLauncher.logger.info("LDU_SendCommandSTASetting: STATime:"+STATime+":STAPulseCount :"+STAPulseCount);
		String STAStartData = "02";

		commLDU.SetRTS(true);
		LDU_SendMeterAddress(AllMeterAddress);
		LDU_SendSTATime(STATime);
		LDU_SendPulseCount(STAPulseCount);
		LDU_SendStartSTA(STAStartData);
		commLDU.SetRTS(false);
		ApplicationLauncher.logger.debug("LDU_SendCommandCreepSetting DataSend Completed");


	}

	public void LDU_SendCommandResetError(){

		String Data = ConstantLduCcube.CMD_LDU_ERROR_RESET+ConstantApp.END_CR2;
		commLDU.SetRTS(true);
		WriteToSerialCommLDU(Data);
		commLDU.SetRTS(false);
	}

	public void lscsLDU_SendCommandResetError(){

		ApplicationLauncher.logger.debug("lscsLDU_SendCommandResetError :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(200);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(200);
		}
	}

	public void Ref_SendCommandResetSetting(){
		String data = ConstantRefStdRadiant.CMD_REF_STD_ACC_RESET_SETTING;
		WriteToSerialCommRef(data);
	}

	public void Ref_SendCommandAccumlateStart(){
		String data = ConstantRefStdRadiant.CMD_REF_STD_ACC_START;
		WriteToSerialCommRef(data);
	}

	public void Ref_SendCommandReadMeterReading(){
		String data = getAccumlativeDataReadCommandType();//MyProperty.CMD_REF_STD_READ_METER_READING;
		WriteToSerialCommRef(data);
	}

	public void Ref_SendCommandAccumlateStop(){
		String data = ConstantRefStdRadiant.CMD_REF_STD_ACC_STOP;
		WriteToSerialCommRef(data);
	}

	public void Ref_SendCommandBNC_OutPut(){

		String data = "";
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			//data = ConstantRefStd.CMD_REF_STD_BNC_OUTPUT_PORT_ACTIVE;
			data =  BNC_OutputPortActive;
		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			//data = ConstantRefStd.CMD_REF_STD_BNC_OUTPUT_PORT_REACTIVE;
			data =  BNC_OutputPortReactive;
		}
		WriteToSerialCommRef(data);
	}


	public void kiggsRefStdSendInitCommand(){


		//String data = "";
		String dummyExpectedData = "";
		int dummyExpectedLength = 2;
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			//ApplicationLauncher.logger.info("kiggsRefStdSendCommandBNC_OutPut: MSG_BNC_OUTPUT_SETTING_ACTIVE_POWER");
			//kiggsRefStdCommSendReadDataCommand(RefStdKiggsMessage.MSG_BNC_OUTPUT_SETTING_ACTIVE_POWER,dummyExpectedData,dummyExpectedLength );
			//Sleep(1000);
			//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE");
			//kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE,dummyExpectedData,dummyExpectedLength );
			//Sleep(1000);

			//kiggsRefStdSendCommandStateSettingBasicMeasurement();
			//Sleep(1000);
			ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE");
			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE,dummyExpectedData,dummyExpectedLength );
			//Sleep(1000);
			//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_BNC_OUTPUT_SETTING_ACTIVE_POWER");
			//kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_BNC_OUTPUT_SETTING_ACTIVE_POWER,dummyExpectedData,dummyExpectedLength );


		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			//ApplicationLauncher.logger.info("kiggsRefStdSendCommandBNC_OutPut: MSG_BNC_OUTPUT_SETTING_REACTIVE_POWER");
			//data = ConstantRefStd.CMD_REF_STD_BNC_OUTPUT_PORT_REACTIVE;
			//kiggsRefStdCommSendReadDataCommand(RefStdKiggsMessage.MSG_BNC_OUTPUT_SETTING_REACTIVE_POWER,dummyExpectedData,dummyExpectedLength);
			//Sleep(1000);
			//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_SETTING_MODE_SINGLE_PHASE_REACTIVE");
			//kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_SETTING_MODE_SINGLE_PHASE_REACTIVE,dummyExpectedData,dummyExpectedLength );

			ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE");
			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE,dummyExpectedData,dummyExpectedLength );
			//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_SETTING_MODE_SINGLE_PHASE_REACTIVE");
			//kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_SETTING_MODE_SINGLE_PHASE_REACTIVE,dummyExpectedData,dummyExpectedLength );
			//Sleep(1000);

			//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_BNC_OUTPUT_SETTING_REACTIVE_POWER");
			//kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_BNC_OUTPUT_SETTING_REACTIVE_POWER,dummyExpectedData,dummyExpectedLength );

		}
		//Sleep(1000);
		//ApplicationLauncher.logger.info("kiggsRefStdSendInitCommand: MSG_CURRENT_RANGE_SETTING_AUTO");
		//kiggsRefStdCommSendReadDataCommand(RefStdKiggsMessage.MSG_CURRENT_RANGE_SETTING_AUTO,dummyExpectedData,dummyExpectedLength );
		//WriteToSerialCommRef(data);*/
	}

	public void kiggsRefStdSendCommandStateSettingBasicMeasurement(){


		//String data = "";
		String dummyExpectedData = "";
		int dummyExpectedLength = 2;
		ApplicationLauncher.logger.info("kiggsRefStdSendCommandStateSettingBasicMeasurement: MSG_ACCU_SETTING_MODE_BASIC_MEASURE");
		kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_ACCU_SETTING_MODE_BASIC_MEASURE,dummyExpectedData,dummyExpectedLength );

		//WriteToSerialCommRef(data);*/
	}

	public void kiggsRefStdSendCommandCurrentRangeSettingManualModeMaxTap(){

		ApplicationLauncher.logger.debug("kiggsRefStdSendCommandCurrentRangeSettingManualModeMaxTap: Entry");
		//String data = "";
		String dummyExpectedData = "";
		int dummyExpectedLength = 2;
		ApplicationLauncher.logger.info("kiggsRefStdSendCommandCurrentRangeSettingManualModeMaxTap: MSG_CURRENT_RANGE_SETTING_MANUAL_MODE_MAX_TAP");
		kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_CURRENT_RANGE_SETTING_MANUAL_MODE_MAX_TAP,dummyExpectedData,dummyExpectedLength );

		//WriteToSerialCommRef(data);*/
	}


	public void Ref_SendCommandBNC_Constant1(){

		String data = "";
		//data = generateCommandforRefStdBNC_Constant(DisplayDataObj.getRSSPulseRate();
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			/*if(DisplayDataObj.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
				data = ConstantRefStd.CMD_REF_STD_BNC_OUTPUT_ACTIVE;
			}else{*/
			data = GuiUtils.generateCommandforRefStdBNC_Constant(DisplayDataObj.getRSSPulseRate(),ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_WH_SET);
			//}
		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			//data = ConstantRefStd.CMD_REF_STD_BNC_OUTPUT_REACTIVE;
			data = GuiUtils.generateCommandforRefStdBNC_Constant(DisplayDataObj.getRSSPulseRate(),ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_VARH_SET);

		}
		WriteToSerialCommRef(data);
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	public void LDU_SetErrorCalculationMode(String CalculateMode){
		ApplicationLauncher.logger.debug("LDU_SetErrorCalculationMode :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_ERR_CALC_MODE + CalculateMode + ConstantApp.END_CR2;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SetErrorLowLimit(String ErrorLowLimit){
		ApplicationLauncher.logger.debug("LDU_SetErrorLowLimit :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_ERR_L_LMT + ErrorLowLimit + ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SetErrorHighLimit(String ErrorHighLimit){
		ApplicationLauncher.logger.debug("LDU_SetErrorHighLimit :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_ERR_H_LMT + ErrorHighLimit + ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}
	public void LDU_SetAverageCycle(String AverageCycle){
		ApplicationLauncher.logger.debug("LDU_SetAverageCycle :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_AVG_CYCLE + AverageCycle + ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SetMUT_PulseRate(String MUT_PulseRate){
		ApplicationLauncher.logger.debug("LDU_SetMUT_PulseRate :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_MUT_PULSE_RATE + MUT_PulseRate+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SetRSS_PulseRate(String RSS_PulseRate){
		ApplicationLauncher.logger.debug("LDU_SetRSS_PulseRate :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_RSS_PULSE_RATE + RSS_PulseRate+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SendPulseCount(String PulseCount){
		ApplicationLauncher.logger.debug("LDU_SendPulseCount :Entry");
		String Data = ConstantLduCcube.CMD_LDU_CREEP_SETTING_HDR_PULSE_COUNT + PulseCount+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	} 

	public void LDU_SendStartCreep(String CreepStartData){
		ApplicationLauncher.logger.debug("LDU_SendStartCreep :Entry");
		String Data = ConstantLduCcube.CMD_LDU_CREEP_SETTING_HDR_START_CREEP + CreepStartData+ConstantApp.END_CR2;
		WriteToSerialCommLDU(Data);

	} 

	public void lscsLDU_SendStartCreep(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStartCreep :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			String Data = ConstantLduLscs.CMD_LDU_CREEP_SETTING_HDR_START_CREEP ;
			WriteToSerialCommLDU(Data);
		}

	} 

	public void lscsLDU_SendStopCreep(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStartCreep :Entry");
		//WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		Sleep(1000);
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);
		}
		//Sleep(1000);
		//String Data = ConstantLscsLDU.CMD_LDU_CREEP_SETTING_HDR_START_CREEP ;
		//WriteToSerialCommLDU(Data);

	}
	
	
	public void lscsLDU_SendStopStaCreepConstant(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStopStaCreepConstant :Entry");
		//WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		Sleep(1000);
		//if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);


	}

	public void lscsLDU_SendStartSTA(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStartSTA :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			String Data = ConstantLduLscs.CMD_LDU_STA_SETTING_HDR_START_STA ;
			WriteToSerialCommLDU(Data);
		}

	} 

	public void lscsLDU_SendStartConstTest(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStartConstTest :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(20);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			//Sleep(1000);
			//WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_STOP);
			Sleep(20);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			String Data = ConstantLduLscs.CMD_LDU_STA_SETTING_HDR_START_DIAL ;
			WriteToSerialCommLDU(Data);
		}

	} 

	public void lscsLDU_SendRefreshDataCommand(){
		ApplicationLauncher.logger.debug("lscsLDU_SendRefreshDataCommand :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(200);
		}
	}

	public void lscsLDU_SendStopSTA(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStopSTA :Entry");
		//WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		//Sleep(1000);
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);
			Sleep(1000);
		}
		//Sleep(1000);
		//String Data = ConstantLscsLDU.CMD_LDU_CREEP_SETTING_HDR_START_CREEP ;
		//WriteToSerialCommLDU(Data);

	}

	public void lscsLDU_SendStopConstantTest(){
		ApplicationLauncher.logger.debug("lscsLDU_SendStopConstantTest :Entry");
		//WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		//Sleep(1000);
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_STOP);
			Sleep(200);
		}
		//Sleep(1000);
		//String Data = ConstantLscsLDU.CMD_LDU_CREEP_SETTING_HDR_START_CREEP ;
		//WriteToSerialCommLDU(Data);

	}



	public void lscsLDU_SendPowerSourceCurrentRelayCutOff(){
		ApplicationLauncher.logger.debug("lscsLDU_SendPowerSourceCurrentRelayCutOff :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_POWER_SOURCE_CURRENT_RELAY_TURN_OFF);
			Sleep(200);
		}


	}

	public void LDU_SendStartConst(){
		ApplicationLauncher.logger.debug("LDU_SendStartConst :Entry");
		String Data = ConstantLduCcube.CMD_LDU_CONST_SETTING_HDR +ConstantLduCcube.CMD_LDU_CONST_SETTING_ADDRESS+ ConstantLduCcube.CMD_LDU_CONST_SETTING_COMMAND+ConstantLduCcube.CMD_LDU_CONST_SETTING_DATA+ConstantApp.END_CR2;
		WriteToSerialCommLDU(Data);

	} 

	public void LDU_SendStartSTA(String STAStartData){
		ApplicationLauncher.logger.debug("LDU_SendStartSTA :Entry");
		String Data = ConstantLduCcube.CMD_LDU_STA_SETTING_HDR_START_STA + STAStartData+ConstantApp.END_CR2;
		WriteToSerialCommLDU(Data);

	} 

	public void LDU_SendMeterAddress(String MeterAddress){
		ApplicationLauncher.logger.debug("LDU_SendMeterAddress :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_MTR_ADRS + MeterAddress+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void lscsLDU_SendDataRefreshCmd(){
		ApplicationLauncher.logger.debug("lscsLDU_SendDataRefreshCmd :Entry");

		WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
	}

	public void lscsLDU_SendCeigSettingMethod(String rssPulsePerMutPulse, String noOfPulses){
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod :Entry");
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod :Sending Refresh command");
		//WriteToSerialCommLDU("YY");dfhgf
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommLDU(ConstantLduLscs.CMD_LDU_CEIG_DATA_REFRESH_HDR);
			Sleep(1000);
		}
		String Data = ConstantLduLscs.CMD_LDU_CEIG_SETTING_HDR + noOfPulses+ConstantLduLscs.CMD_LDU_CEIG_SETTING_SEPERATOR +
				rssPulsePerMutPulse +ConstantLduLscs.CMD_LDU_CEIG_SETTING_SEPERATOR;
		//WriteToSerialCommLDU(Data);
		//Data = noOfPulses;
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : Data: " + Data);
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			for(int i = 0; i < Data.length(); i++){
				//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
				if(!ProjectExecutionController.getUserAbortedFlag()) {
					WriteToSerialCommLDU(String.valueOf(Data.charAt(i)));
					Sleep(200);
				}
			}
		}

	}



	public void LDU_SendCreepTime(String CreepTime){
		ApplicationLauncher.logger.debug("LDU_SendCreepTime :Entry");
		String Data = ConstantLduCcube.CMD_LDU_CREEP_SETTING_HDR_ADRS_CMD + CreepTime+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void LDU_SendSTATime(String STATime){
		ApplicationLauncher.logger.debug("LDU_SendSTATime :Entry");
		String Data = ConstantLduCcube.CMD_LDU_STA_SETTING_HDR_ADRS_CMD + STATime+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}

	public void WriteToSerialCommLDU(String Data){
		ApplicationLauncher.logger.debug("WriteToSerialCommLDU :DataHex:"+ConstantLduCcube.StringToHex(Data).toUpperCase());
		ApplicationLauncher.logger.debug("WriteToSerialCommLDU :Data:"+Data);

		commLDU.writeStringMsgToPort(Data);
		Sleep(200);

	}

	public void WriteToSerialCommICT(String Data){
		//ApplicationLauncher.logger.debug("WriteToSerialCommICT :DataHex:"+ConstantCcubeLDU.StringToHex(Data).toUpperCase());
		ApplicationLauncher.logger.debug("WriteToSerialCommICT :Data:"+Data);

		commICT.writeStringMsgToPortInHex(Data);
		Sleep(200);

	}

	public void WriteToSerialCommLduHex(String Data){
		ApplicationLauncher.logger.debug("WriteToSerialCommLduHex :DataHex:"+Data);
		try {
			commLDU.writeStringMsgToPortInHex(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommLduHex :Exception :" + e.getMessage());
		}

	}

	public void WriteToSerialCommRef(String Data){
		ApplicationLauncher.logger.debug("WriteToSerialCommRef :DataHex:"+Data);
		try {
			commRefStandard.writeStringMsgToPortInHex(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommRef :Exception :" + e.getMessage());
		}

	}




	public void LDU_SendMeterConstantUnit(String MeterConstUnit){
		ApplicationLauncher.logger.debug("LDU_SendMeterConstantUnit :Entry");
		String Data = ConstantLduCcube.CMD_LDU_ERR_SETTING_HDR_MTR_CONST + MeterConstUnit+ConstantApp.END_CR;
		WriteToSerialCommLDU(Data);

	}



	public void DisconnectSerialComm(Communicator SerialPortObj){
		try{
			SerialPortObj.disconnect();
		}catch(Exception e){
			ApplicationLauncher.logger.error("DisconnectSerialComm :Exception :" + e.getMessage());
		}
	}

	public void SetPowerSourceOn2(String U1,String I1,String DegreePhase,String Freq){
		ApplicationLauncher.logger.debug("SetPowerSourceOn2 :Entry");
		String InputVoltage = U1;
		String InputCurrent = I1;
		String InputDegreePhase = DegreePhase;
		String InputFrequency = Freq;
		if(!isPowerSourceTurnedOff()){
			SetPowerSourceOff();
		}

		WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+InputVoltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+InputCurrent,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+InputDegreePhase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);



	}

	public void SetSinglePhaseHarPowerSourceOn(){
		ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn :Entry");
		ApplicationHomeController.update_left_status("Setting HarmonicPowerSource",ConstantApp.LEFT_STATUS_DEBUG);

		String InputVoltage1 = DisplayDataObj.getR_PhaseOutputVoltage();
		String InputCurrent1 = DisplayDataObj.getR_PhaseOutputCurrent();
		String InputPhase1 = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());


		String InputFrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());


		ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn: R_Voltage: " + InputVoltage1);
		ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn: R_Current: " + InputCurrent1);
		ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn: R_Degree: " + InputPhase1);


		boolean status = false;
		if(DisplayDataObj.getVoltageResetRequired()){
			if(!isPowerSourceTurnedOff()){
				SetPowerSourceOff();
			}
		}
		PerformPowerSrcReboot();
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		if(status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+InputVoltage1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if(status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+InputCurrent1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if(status){
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+InputPhase1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);

				}		
			}
		}

		try{
			ApplicationHomeController.update_left_status("Setting 1 Phase harmonics",ConstantApp.LEFT_STATUS_DEBUG);
			JSONArray harmonic_db_data = DisplayDataObj.getHarmonic_data();
			JSONObject harmonic = new JSONObject();

			String harmonic_times = ""; 
			String harmonic_voltage = ""; 
			String harmonic_current = "";
			String harmonic_phase = "";

			for(int i=0; i<harmonic_db_data.length(); i++){

				harmonic = harmonic_db_data.getJSONObject(i);

				harmonic_times = harmonic.getString("harmonic_times"); 
				harmonic_voltage = harmonic.getString("harmonic_volt"); 
				harmonic_current = harmonic.getString("harmonic_current");
				harmonic_phase = harmonic.getString("harmonic_phase");
				if(!harmonic_current.equals("0")){
					String SetHarmonicData1 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_I1_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_current + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetSinglePhaseHarPowerSourceOn: SetHarmonicData1:" + SetHarmonicData1);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}

				}

				if(!harmonic_voltage.equals("0")){
					String SetHarmonicData_volt1 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_U1_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_voltage + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetSinglePhaseHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData_volt1);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData_volt1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}

				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetSinglePhaseHarPowerSourceOn: Exception: " + e.getMessage());
		}

		if(status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			if(status){
				setPowerSrcOnFlag(true);
				setPowerSourceTurnedOff(false);
				ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn: Power Source On");
				ApplicationHomeController.update_left_status("1 Phase Harmonic PowerSource On",ConstantApp.LEFT_STATUS_DEBUG);
			}
		}

		ApplicationLauncher.logger.debug("SetSinglePhaseHarPowerSourceOn :Exit");


	}

	/*	 long calculateRSS_ConstantV4(String MUT_ConstInImpulsesPerKiloWattHour){
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Entry");
		 	//float Volt1,float Volt2,float Volt3,float Current1,float Current2,float Current3,float PF1,float PF2,float PF3
		 	float Volt1 = Float.parseFloat(DisplayDataObj.getR_PhaseOutputVoltage());
		 	float Volt2 =  Float.parseFloat(DisplayDataObj.getY_PhaseOutputVoltage());
		 	float Volt3 =  Float.parseFloat(DisplayDataObj.getB_PhaseOutputVoltage());
		 	float Current1 =  Float.parseFloat(DisplayDataObj.getR_PhaseOutputCurrent());
		 	float Current2 =  Float.parseFloat(DisplayDataObj.getY_PhaseOutputCurrent());
		 	float Current3 =  Float.parseFloat(DisplayDataObj.getB_PhaseOutputCurrent());
		 	float PF_InDegree1 =  Float.parseFloat(DisplayDataObj.getR_PhaseOutputPhase());
		 	float PF_InDegree2 =  Float.parseFloat(DisplayDataObj.getY_PhaseOutputPhase());
		 	float PF_InDegree3 =  Float.parseFloat(DisplayDataObj.getB_PhaseOutputPhase());


		 	float PF_InDegree1 =  DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
		 	float PF_InDegree2 =  DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase();
		 	float PF_InDegree3 =  DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase();

		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Volt1:" + Volt1);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Volt2:" + Volt2);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Volt3:" + Volt3);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Current1:" + Current1);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Current2:" + Current2);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :Current3:" + Current3);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :PF_InDegree1:" + PF_InDegree1);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :PF_InDegree2:" + PF_InDegree2);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :PF_InDegree3:" + PF_InDegree3);
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV4 :P1:" + (Volt1 * Current1 * (float)Math.cos(Math.toRadians(PF_InDegree1)) ));

		 	float power = (Volt1 * Current1 * (float)Math.cos(Math.toRadians(PF_InDegree1)) )+(Volt2 * Current2 * (float)Math.cos(Math.toRadians(PF_InDegree2)))+ (Volt3 * Current3 * (float)Math.cos(Math.toRadians(PF_InDegree3)));
		 	//1\float power = (Volt1 * Current1  )+(Volt2 * Current2 )+ (Volt3 * Current3 );

		 	//float power = (Volt1 * Current1 * Math.acos(PF1) )+(Volt2 * Current2 * Math.acos(PF2))+ (Volt3 * Current3 * Math.acos(PF3));
		 	//float power = (Volt1 * Current1 * PF1)+(Volt2 * Current2 * PF2)+ (Volt3 * Current3 * PF3);
			double RssConstantInWattHour = (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000*Integer.parseInt(MUT_ConstInImpulsesPerKiloWattHour))/power;
			long OutputConstantInWattHour = (long)RssConstantInWattHour;
			//System.out.println("Double RssConstantInWattHour : " + RssConstantInWattHour);
			//System.out.println("Long OutputConstantInWattHour : " + OutputConstantInWattHour);
			ApplicationLauncher.logger.info("calculateRSS_Constant in Watt Hour:"+Math.abs(OutputConstantInWattHour));
			return Math.abs(OutputConstantInWattHour); 
	}*/

	public void SetHarPowerSourceOn(){
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn :Entry");
		ApplicationHomeController.update_left_status("Setting PowerSource",ConstantApp.LEFT_STATUS_DEBUG);
		String InputVoltage1 = DisplayDataObj.getR_PhaseOutputVoltage();
		String InputCurrent1 = DisplayDataObj.getR_PhaseOutputCurrent();
		String InputPhase1 = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());

		String InputVoltage2 = DisplayDataObj.getY_PhaseOutputVoltage();
		String InputCurrent2 = DisplayDataObj.getY_PhaseOutputCurrent();
		String InputPhase2 = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());

		String InputVoltage3 = DisplayDataObj.getB_PhaseOutputVoltage();
		String InputCurrent3 = DisplayDataObj.getB_PhaseOutputCurrent();
		String InputPhase3 = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());

		String InputFrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());


		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: R_Voltage: " + InputVoltage1);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: R_Current: " + InputCurrent1);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: R_Degree: " + InputPhase1);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: Y_Voltage: " + InputVoltage2);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: Y_Current: " + InputCurrent2);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: Y_Degree: " + InputPhase2);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: B_Voltage: " + InputVoltage3);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: B_Current: " + InputCurrent3);
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn: B_Degree: " + InputPhase3);

		boolean status = false;
		if(DisplayDataObj.getVoltageResetRequired()){
			if(!isPowerSourceTurnedOff()){
				SetPowerSourceOff();
			}
		}
		PerformPowerSrcReboot();
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		if(status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+InputVoltage1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if(status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+InputCurrent1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if(status){
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+InputPhase1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					if(status){
						status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U2_PREFIX+InputVoltage2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
						if(status){
							status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I2_PREFIX+InputCurrent2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
							if(status){
								status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF2_PREFIX+InputPhase2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
								if(status){
									status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U3_PREFIX+InputVoltage3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
									if(status){
										status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I3_PREFIX+InputCurrent3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
										if(status){
											status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF3_PREFIX+InputPhase3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
										}
									}
								}
							}
						}
					}
				}		
			}
		}

		try{
			ApplicationHomeController.update_left_status("Setting harmonics",ConstantApp.LEFT_STATUS_DEBUG);
			JSONArray harmonic_db_data = DisplayDataObj.getHarmonic_data();
			JSONObject harmonic = new JSONObject();
			String harmonic_times = "";
			String harmonic_voltage = "";
			String harmonic_current = "";
			String harmonic_phase = "";
			for(int i=0; i<harmonic_db_data.length(); i++){

				harmonic = harmonic_db_data.getJSONObject(i);
				harmonic_times = harmonic.getString("harmonic_times"); 
				harmonic_voltage = harmonic.getString("harmonic_volt"); 
				harmonic_current = harmonic.getString("harmonic_current");
				harmonic_phase = harmonic.getString("harmonic_phase");
				if(!harmonic_current.equals("0")){
					String SetHarmonicData1 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_I1_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_current + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData1);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
					String SetHarmonicData2 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_I2_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_current + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData2);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
					String SetHarmonicData3 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_I3_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_current + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData3);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
				}

				if(!harmonic_voltage.equals("0")){
					String SetHarmonicData_volt1 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_U1_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_voltage + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData_volt1);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData_volt1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
					String SetHarmonicData_volt2 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_U2_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_voltage + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData_volt2);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData_volt2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
					String SetHarmonicData_volt3 = ConstantPowerSourceMte.CMD_PWR_SRC_HAR_U3_PREFIX + harmonic_times + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_voltage + ConstantPowerSourceMte.CMD_PWR_SRC_COMMA+ harmonic_phase;
					ApplicationLauncher.logger.info("SetHarPowerSourceOn: SetHarmonicData:" + SetHarmonicData_volt3);;
					if(status){
						status = WriteToSerialCommPwrSrc(SetHarmonicData_volt3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetHarPowerSourceOn: Exception: " + e.getMessage());
		}

		if(status){
			if(status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
				if(status){
					setPowerSrcOnFlag(true);
					setPowerSourceTurnedOff(false);
					ApplicationLauncher.logger.debug("SetHarPowerSourceOn: Power Source On");
					ApplicationHomeController.update_left_status("Harmonic PowerSource On",ConstantApp.LEFT_STATUS_DEBUG);
				}
			}
		}
		ApplicationLauncher.logger.debug("SetHarPowerSourceOn:Exit");

	}


	public void SetCusPowerSourceOn(){
		ApplicationHomeController.update_left_status("Setting Custom PowerSource",ConstantApp.LEFT_STATUS_DEBUG);
		String inputvoltage1 = DisplayDataObj.getR_PhaseOutputVoltage();
		String inputvoltage2 = DisplayDataObj.getY_PhaseOutputVoltage();
		String inputvoltage3 = DisplayDataObj.getB_PhaseOutputVoltage();
		String inputcurrent1 = DisplayDataObj.getR_PhaseOutputCurrent();
		String inputcurrent2 = DisplayDataObj.getY_PhaseOutputCurrent();
		String inputcurrent3 = DisplayDataObj.getB_PhaseOutputCurrent();
		String inputphase1 = DisplayDataObj.getR_PhaseOutputPhase();
		String inputphase2 = DisplayDataObj.getY_PhaseOutputPhase();
		String inputphase3 = DisplayDataObj.getB_PhaseOutputPhase();
		String inputfrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());

		if(DisplayDataObj.getVoltageResetRequired()){
			if(!isPowerSourceTurnedOff()){
				SetPowerSourceOff();
			}
		}
		PerformPowerSrcReboot();

		boolean status = false;
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+inputfrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+inputfrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+inputvoltage1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		if (status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+inputcurrent1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+inputphase1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U2_PREFIX+inputvoltage2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					if (status){
						status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I2_PREFIX+inputcurrent2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
						if (status){
							status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF2_PREFIX+inputphase2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
							if (status){
								status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U3_PREFIX+inputvoltage3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
								if (status){
									status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I3_PREFIX+inputcurrent3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
									if (status){
										status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF3_PREFIX+inputphase3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
										if (status){
											//status = WriteToSerialCommPwrSrc(MyProperty.CMD_PWR_SRC_FRQ_PREFIX+inputfrequency,MyProperty.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
											if (status){
												status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
												setPowerSrcOnFlag(true);
												setPowerSourceTurnedOff(false);
												ApplicationLauncher.logger.info("***************************************");
												ApplicationLauncher.logger.info("SetCusPowerSourceOn: Power Src Turned On");
												ApplicationLauncher.logger.info("***************************************");
												//ApplicationHomeController.DisplayExecuteProcalStatus.setValue("Custom Power Src Turned On");
												ApplicationHomeController.update_left_status("Custom PowerSource TurnedOn",ConstantApp.LEFT_STATUS_DEBUG);
											}
										}
									}
								}
							}
						}
					}

				}
			}
		}

	}


	public void SetSinglePhaseCusPowerSourceOn(){
		ApplicationHomeController.update_left_status("Setting 1 phase Custom PowerSource",ConstantApp.LEFT_STATUS_DEBUG);
		String inputvoltage1 = DisplayDataObj.getR_PhaseOutputVoltage();
		String inputcurrent1 = DisplayDataObj.getR_PhaseOutputCurrent();
		String inputphase1 = DisplayDataObj.getR_PhaseOutputPhase();

		String inputfrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());

		if(DisplayDataObj.getVoltageResetRequired()){
			if(!isPowerSourceTurnedOff()){
				SetPowerSourceOff();
			}
		}
		PerformPowerSrcReboot();

		boolean status = false;
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+inputfrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+inputfrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+inputvoltage1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		if (status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+inputcurrent1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+inputphase1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
					setPowerSrcOnFlag(true);
					setPowerSourceTurnedOff(false);
					ApplicationLauncher.logger.info("***************************************");
					ApplicationLauncher.logger.info("SetCusPowerSourceOn: Power Src Turned On");
					ApplicationLauncher.logger.info("***************************************");
					ApplicationHomeController.update_left_status("Custom PowerSource TurnedOn",ConstantApp.LEFT_STATUS_DEBUG);
				}
			}
		}
		

	}

	public boolean SendPowerDataToSerialCommPwrSrc(String SetPowerData,String ExpectedPowerResponseData){
		ApplicationLauncher.logger.debug("SendPowerDataToSerialCommPwrSrc :Entry");
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.writeStringMsgToPort(SetPowerData);
			SerialPortObj.setExpectedResult(ExpectedPowerResponseData);
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			pwerData.SerialReponseTimerStart(SerialPortObj,20);
			status = pwerData.IsExpectedResponseReceived();

			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("SendPowerDataToSerialCommPwrSrc: Unable to set the Power source Parameter:");
					FailureManager.AppendPowerSrcReasonForFailure("Error Code XXX: StabilityValidation:PowerSource: Unable to set the Power source Parameter");
					///Gopi
					setSkipCurrentTP_Execution(true);
				}
			}
			pwerData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}

	public static void setSkipCurrentTP_Execution(boolean status){
		ApplicationLauncher.logger.debug("setSkipCurrentTP_Execution :Entry:status:"+status);
		SkipCurrentTP_Execution = status;
	}

	public boolean getSkipCurrentTP_Execution(){
		//ApplicationLauncher.logger.debug("getSkipCurrentTP_Execution :Entry");
		return SkipCurrentTP_Execution;
	}
	public boolean WriteToSerialCommPwrSrc(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrc :Entry");
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrc :Data: " + Data);
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrc :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try{
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialPortObj.writeStringMsgToPort(Data+Terminator);
				SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
				SerialPortObj.ClearSerialData();
				if(!ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE){
					Sleep(500);
				}
				String dataBuffer = ""; // pradeep
				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					 SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i))); // previous // working one

					dataBuffer = dataBuffer.concat(String.valueOf(Data.charAt(i))); // pradeep
					//Sleep(200);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					if(!ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE){
						Sleep(80);
					}
				}

				ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrc Stm32WithOutDelay : dataBuffer:" + dataBuffer); // pradeep
				//SerialPortObj.writeStringMsgToPort(dataBuffer);                                                             // pradeep

			}
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			//if(ProjectExecutionController.getCurrentTestPointName().contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			if(DisplayDataObj.isPresentTestPointContainsHarmonics()){	
				pwerData.SerialReponseTimerStart(SerialPortObj,40);
			}else {
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					pwerData.SerialReponseTimerStart(SerialPortObj,40);
				}else{
					pwerData.SerialReponseTimerStart(SerialPortObj,40);//45); //increased the wait time from 20 to 30 in version s4.0.46.4.1//20);//20
					//pwerData.SerialReponseTimerStart(SerialPortObj,40); 
				}
			}


			status = pwerData.IsExpectedResponseReceived();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("WriteToSerialCommPwrSrc : Unable to set the Power source Parameter:");
					setPowerSrcErrorResponseReceivedStatus(true);
				}




			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommPwrSrc :Exception :" + e.getMessage());
		}
		return status;
	}

	
	public boolean WriteToSerialCommPwrSrcV2(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 :Entry");
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 :Data: " + Data);
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try{
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialPortObj.writeStringMsgToPort(Data+Terminator);
				SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
				SerialPortObj.ClearSerialData();
				Sleep(500);
				String dataBuffer = ""; // pradeep
				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					 SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i))); // previous // working one

					dataBuffer = dataBuffer.concat(String.valueOf(Data.charAt(i))); // pradeep
					//Sleep(200);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					Sleep(80);
				}

				ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 Stm32WithOutDelay : dataBuffer:" + dataBuffer); // pradeep
				//SerialPortObj.writeStringMsgToPort(dataBuffer);                                                             // pradeep

			}
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			//if(ProjectExecutionController.getCurrentTestPointName().contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			if(DisplayDataObj.isPresentTestPointContainsHarmonics()){
				pwerData.SerialReponseTimerStartV2(SerialPortObj,40);
			}else {
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					pwerData.SerialReponseTimerStartV2(SerialPortObj,40);
				}else{
					pwerData.SerialReponseTimerStartV2(SerialPortObj,40);//45); //increased the wait time from 20 to 30 in version s4.0.46.4.1//20);//20
					//pwerData.SerialReponseTimerStart(SerialPortObj,40); 
				}
			}


			status = pwerData.IsExpectedResponseReceivedV2();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("WriteToSerialCommPwrSrcV2 : Unable to set the Power source Parameter:");
					setPowerSrcErrorResponseReceivedStatus(true);
				}




			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommPwrSrcV2 :Exception :" + e.getMessage());
		}
		return status;
	}
	
	
	
	public boolean WriteToSerialCommPwrSrcInit(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcInit :Entry");
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcInit :Data: " + Data);
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcInit :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try{
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialPortObj.writeStringMsgToPort(Data+Terminator);
				SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
				SerialPortObj.ClearSerialData();
				Sleep(500);
				String dataBuffer = ""; // pradeep
				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					 SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i))); // previous // working one

					//dataBuffer = dataBuffer.concat(String.valueOf(Data.charAt(i))); // pradeep
					//Sleep(200);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					Sleep(80);
				}

				ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcInit Stm32WithOutDelay : dataBuffer:" + dataBuffer); // pradeep
				//SerialPortObj.writeStringMsgToPort(dataBuffer);                                                             // pradeep

			}
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			//if(ProjectExecutionController.getCurrentTestPointName().contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			if(DisplayDataObj.isPresentTestPointContainsHarmonics()){
				pwerData.SerialReponseTimerStart(SerialPortObj,10);
			}else {
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					pwerData.SerialReponseTimerStart(SerialPortObj,10);
				}else{
					pwerData.SerialReponseTimerStart(SerialPortObj,10); //increased the wait time from 20 to 30 in version s4.0.46.4.1//20);//20
					//pwerData.SerialReponseTimerStart(SerialPortObj,40); 
				}
			}


			status = pwerData.IsExpectedResponseReceived();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("WriteToSerialCommPwrSrcInit : Unable to set the Power source Parameter:");
					setPowerSrcErrorResponseReceivedStatus(true);
				}




			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommPwrSrcInit :Exception :" + e.getMessage());
		}
		return status;
	}

	public boolean WriteToSerialCommPwrSrcForStm32WithOutDelay(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcForStm32WithOutDelay :Entry");
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcForStm32WithOutDelay :Data: " + Data);
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcForStm32WithOutDelay :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try{
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialPortObj.writeStringMsgToPort(Data+Terminator);
				SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantLscsHarmonicsSourceSlave.CMD_PWR_SRC_HRM_ACK_ERROR_RESPONSE);
				SerialPortObj.ClearSerialData();
				String dataBuffer = "";
				//String dataBuffer2 = "";

				for(int i = 0; i < Data.length(); i++){
					ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcForStm32WithOutDelay : index :" + i +": " + String.valueOf(Data.charAt(i)));
					//SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					dataBuffer = dataBuffer.concat(String.valueOf(Data.charAt(i)));
					//Sleep(200);
					//Sleep(10);
					//Sleep(50);
					//Sleep(80);
					//Sleep(1000);//worked good for 10mA and 25mA calibration
					//Sleep(500);
					//Sleep(80);



				}
				ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcForStm32WithOutDelay : dataBuffer:" + dataBuffer);
				SerialPortObj.writeStringMsgToPort(dataBuffer);

			}
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			//if(ProjectExecutionController.getCurrentTestPointName().contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
			if(DisplayDataObj.isPresentTestPointContainsHarmonics()){	
				pwerData.SerialReponseTimerStart(SerialPortObj,40);
			}else {
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					pwerData.SerialReponseTimerStart(SerialPortObj,40);
				}else{
					pwerData.SerialReponseTimerStart(SerialPortObj,30); //increased the wait time from 20 to 30 in version s4.0.46.4.1//20);//20
				}
			}


			status = pwerData.IsExpectedResponseReceived();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("WriteToSerialCommPwrSrcForStm32WithOutDelay : Unable to set the Power source Parameter:");
					setPowerSrcErrorResponseReceivedStatus(true);
				}




			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommPwrSrcForStm32WithOutDelay :Exception :" + e.getMessage());
		}
		return status;
	}


	public boolean writeToSerialCommHarmonicsSourceSlave(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave :Entry");
		ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave :Data: " + Data);
		ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commHarmonicsSrc;
		//char Terminator = ConstantApp.END_CR;
		try{
			//setPowerSrcErrorResponseReceivedStatus(false);
			//SerialPortObj.writeStringMsgToPort(Data);dfd
			SerialPortObj.setExpectedResult(ExpectedResult);
			SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
			SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
			SerialPortObj.ClearSerialData();
			if (ExpectedResult.isEmpty()){// added for conveyor
				ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave :Conveyor ");
				SerialPortObj.writeStringMsgToPort(Data);
			}else if(Data.contains(ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_SLAVE_TRANSMISION_END)){
				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave : data1: " + String.valueOf(Data.charAt(i)));
					//Sleep(80);
					Sleep(10);
				}
			
			}else {
				
				if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
					ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave :Conveyor2 ");
					SerialPortObj.writeStringMsgToPort(Data);
				}else{
					for(int i = 0; i < Data.length(); i++){
						//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
						SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
						ApplicationLauncher.logger.debug("writeToSerialCommHarmonicsSrcSlave : data2: " + String.valueOf(Data.charAt(i)));
						//Sleep(80);
						
						Sleep(120);
					}
				}
			}

			if(!ExpectedResult.isEmpty()){
				SerialDataHarmonicsSourceSlave pwerData = new SerialDataHarmonicsSourceSlave();
				if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
					pwerData.SerialReponseTimerStart(SerialPortObj,60); // increased for conveyor
				}else{
					pwerData.SerialReponseTimerStart(SerialPortObj,30); //increased the wait time from 20 to 30 in version s4.0.46.4.1//20);//20
				}
				
	
				status = pwerData.IsExpectedResponseReceived();
				if (!status){
					if(pwerData.IsExpectedErrorResponseReceived()){
						ApplicationLauncher.logger.info("writeToSerialCommHarmonicsSrcSlave : Unable to set the Power source Parameter:");
						//setPowerSrcErrorResponseReceivedStatus(true);
					}
	
	
	
	
				}
				pwerData = null;//garbagecollector
			}else{
				status= true;
			}
			
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("writeToSerialCommHarmonicsSrcSlave :Exception :" + e.getMessage());
		}
		return status;
	}
	
	


	public boolean isDataErrorAckRcvdFromLscsPowerSource(){
		ApplicationLauncher.logger.debug("isDataErrorAckRcvdFromLscsPowerSource : Entry");
		boolean status= false;
		Sleep(5000);//
		Communicator SerialPortObj =commPowerSrc;
		/*		char Terminator = ConstantApp.END_CR;
		if(ConstantFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			SerialPortObj.writeStringMsgToPort(Data+Terminator);
			SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

		}else if(ConstantFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			SerialPortObj.writeStringMsgToPort(Data);
			SerialPortObj.setExpectedResult(ExpectedResult);
			SerialPortObj.setExpectedDataErrorResult(ConstantLscsPowerSource.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
			SerialPortObj.setExpectedSetErrorResult(ConstantLscsPowerSource.CMD_PWR_SRC_ACK_ERROR_RESPONSE);

		}*/
		try{
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			if(ProjectExecutionController.isLscsZeroCurrentPowerTurnOn()) {
				ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : Setting expected response for Creep");
				String ExpectedPowerResponseData = ConstantPowerSourceLscs.CMD_PWR_SRC_SET_CREEP_ER;
				SerialPortObj.setExpectedResult(ExpectedPowerResponseData);
			}

			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_READY);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_READY);
			}


			//clearExpectedResponseRecieved
			pwerData.SerialReponseTimerStart(SerialPortObj,20);


			status = pwerData.IsExpectedResponseReceived();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						//String errorData = SerialPortObj.getSerialData();
						//ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : errorData: "+ errorData);
						//if(errorData.equals(ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_READY)){
						if(pwerData.getFeedBackControlReadyReceived()){	
							setPowerSrcOnFlag(true);
							ProjectExecutionController.setRefStdFeedBackControlAlreadyReceived(true);
							ApplicationLauncher.logger.info("******************************************");
							ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource: Power Src Turned On3");
							ApplicationLauncher.logger.info("******************************************");
							ApplicationHomeController.update_left_status("1 Phase: PowerSrc Turned On",ConstantApp.LEFT_STATUS_DEBUG);
						}
						else{
							ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : Unable to set the Power source Parameter3:");
							setPowerSrcErrorResponseReceivedStatus(true);
						}
					}else{
						ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : Unable to set the Power source Parameter2:");
						setPowerSrcErrorResponseReceivedStatus(true);
					}
				}else{

					setPowerSrcOnFlag(true);
					setPowerSourceTurnedOff(false);
					ApplicationLauncher.logger.info("******************************************");
					ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : Power Src Turned On1");
					ApplicationLauncher.logger.info("******************************************");
					ApplicationHomeController.update_left_status("PowerSrc Turned On",ConstantApp.LEFT_STATUS_DEBUG);

				}
			}else {
				setPowerSrcOnFlag(true);
				ApplicationLauncher.logger.info("******************************************");
				ApplicationLauncher.logger.info("isDataErrorAckRcvdFromLscsPowerSource : Power Src Turned On2");
				ApplicationLauncher.logger.info("******************************************");
				ApplicationHomeController.update_left_status("PowerSrc Turned ON",ConstantApp.LEFT_STATUS_DEBUG);

			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector

		}catch(Exception e){
			ApplicationLauncher.logger.error("isDataErrorAckRcvdFromLscsPowerSource :Exception :" + e.getMessage());
		}
		return status;
	}


	public void RefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("RefStdTimerInit:Entry");
		if(!ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			RefStdComSemlock = true;
		}
		RefStdTimer.schedule(new RefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec);// 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}

	public void mteRefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("mteRefStdTimerInit: Entry");
		RefStdComSemlock = true;
		RefStdTimer.schedule(new mteRefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec);// 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("mteRefStdTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}


	public void lscsPowerSourceReadFeedBackTimerInit() {
		lscsPowerSrcReadFeedBackTimer = new Timer();
		ApplicationLauncher.logger.debug("lscsPowerSourceReadFeedBackTimerInit: Entry");
		powerSrcComSemlock = true;
		lscsPowerSrcReadFeedBackTimer.schedule(new lscsPowerSourceReadFeedBackTimerTask(),SerialLscsPowerSrcInstantMetricsRefreshTimeInMsec);// 2000);
		if(pwrSrcComSerialStatusConnected){

			ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setPowerSrcReadFeedbackData( true);
		}

	}

	public void kreRefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("kreRefStdTimerInit: Entry");
		RefStdComSemlock = true;
		RefStdTimer.schedule(new KreRefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec);// 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("kreRefStdTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}
	
/*	public void bofaRefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("bofaRefStdTimerInit: Entry");
		RefStdComSemlock = true;
		RefStdTimer.schedule(new BofaRefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec); // 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("bofaRefStdTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}*/

	public void kiggsRefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("kiggsRefStdTimerInit: Entry");
		RefStdComSemlock = true;
		RefStdTimer.schedule(new KiggsRefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec);// 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("kiggsRefStdTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}



	public void kiggsRefStdTimerInitDebug() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("kiggsRefStdTimerInit: Entry");
		RefStdComSemlock = true;
		RefStdTimer.schedule(new KiggsRefComRemindTaskDebug(),SerialRefComInstantMetricsRefreshTimeInMsec);// 2000);
		if(refComSerialStatusConnected){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("kiggsRefStdTimerInit :setRefStdReadDataFlag to true");
			DisplayDataObj.setRefStdReadDataFlag( true);
		}

	}


	public boolean RefStdComInit(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("RefStdComInit1 Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			refComSerialStatusConnected = RefStdCommInit(InputComm,BaudRate);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("RefStdComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return refComSerialStatusConnected;
	}


	public void LDU_ReadErrorTimerTrigger() {
		ApplicationLauncher.logger.debug("LDU_ReadErrorTimerTrigger :Entry");
		LDU_ComSemlock = true;
		lduTimer = new Timer();
		setTimeExtendedForTimeBased(false);
		ApplicationLauncher.logger.info("LDU_ReadErrorTimerTrigger :getSerialLDU_ComRefreshTimeInMsec:" + getSerialLDU_ComRefreshTimeInMsec());
		lduTimer.schedule(new lduComReadErrorTask(), getSerialLDU_ComRefreshTimeInMsec());// 1000);
		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("LDU_ReadErrorTimerTrigger: Exit:");

	}

	public void lscsLDU_ReadErrorTimerTrigger() {
		ApplicationLauncher.logger.debug("lscsLDU_ReadErrorTimerTrigger :Entry");
		LDU_ComSemlock = true;
		lduTimer = new Timer();
		setTimeExtendedForTimeBased(false);
		SerialDataLDU.ClearLDU_ReadSerialData();
		lsLDU_ClearSerialData();
		ApplicationLauncher.logger.info("lscsLDU_ReadErrorTimerTrigger :getSerialLDU_ComRefreshTimeInMsec:" + getSerialLDU_ComRefreshTimeInMsec());
		lduTimer.schedule(new lduComReadErrorTask(), getSerialLDU_ComRefreshTimeInMsec());// 1000);

		if(lduComSerialStatusConnected){
			ApplicationHomeController.updateBottomSecondaryStatus("LDU read status: 0/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("lscsLDU_ReadErrorTimerTrigger: Exit:");

	}


	public void LDU_ReadCreepTimerTrigger() {
		ApplicationLauncher.logger.debug("LDU_ReadCreepTimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lduComReadCreepTask(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("LDU_ReadCreepTimerTrigger: Exit");

	}



	public void lscsLDU_ReadCreepTimerTrigger() {
		ApplicationLauncher.logger.debug("lscsLDU_ReadCreepTimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lscsLduComReadCreepTask(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("lscsLDU_ReadCreepTimerTrigger: Exit");

	}

	public void LDU_ReadConstTimerTrigger() {
		ApplicationLauncher.logger.debug("LDU_ReadConstTimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lduComReadConstTask(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("LDU_ReadConstTimerTrigger: Exit");

	}



	public void lscsLDU_ReadConstTimerTrigger() {
		ApplicationLauncher.logger.debug("lscsLDU_ReadConstTimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lscsLduComReadConstTask(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("lscsLDU_ReadConstTimerTrigger: Exit");

	}

	public void LDU_ReadSTATimerTrigger() {
		ApplicationLauncher.logger.debug("LDU_ReadSTATimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lduComReadSTATask(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("LDU_ReadSTATimerTrigger : Exit");

	}

	public void lscsLDU_ReadSTA_TimerTrigger() {
		ApplicationLauncher.logger.debug("lscsLDU_ReadSTA_TimerTrigger :Entry");
		lduTimer = new Timer();
		LDU_ComSemlock = true;
		lduTimer.schedule(new lscsLduComReadSTA_Task(),200);// 1000);

		if(lduComSerialStatusConnected){
			DisplayDataObj.setLDU_ReadDataFlag( true);
		}
		ApplicationLauncher.logger.debug("lscsLDU_ReadSTA_TimerTrigger : Exit");

	}


	public boolean RefStdCommInit(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStdCommInit2 :Entry");
		//InitSerialCommPort();
		//ApplicationLauncher.logger.debug("commRefStandard: " +commRefStandard );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = false;
		
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			status =  kreRefStdSetSerialComm(commRefStandard,CommInput,Integer.valueOf(BaudRate),true);;
		} else {
			status = SetSerialComm(commRefStandard,CommInput,Integer.valueOf(BaudRate),true);
		}
		if (status){
			commRefStandard.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("RefStdCommInit:"+CommInput+" access failed");
		}
		return status;


	} 



	public boolean LDU_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("LDU_CommInit :Entry");
		boolean status = SetSerialComm(commLDU,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commLDU.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("LDU_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 

	public void DisconnectRefSerialComm(){
		commRefStandard.disconnect();
	}

	public void DisconnectLDU_SerialComm(){
		commLDU.disconnect();
	}

	public void DisconnectICT_SerialComm(){
		commICT.disconnect();
	}

	public void DisconnectHarmonicsSrc_SerialComm(){
		commHarmonicsSrc.disconnect();
	}
	public void DisconnectPwrSrc_SerialComm(){
		commPowerSrc.disconnect();
	}

	public SerialDataLDU LDU_ReadData(int Expectedlength,String ExpectedResponse){
		ApplicationLauncher.logger.debug("LDU_ReadData :Entry");
		Communicator SerialPortObj =commLDU;

		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);

		ApplicationLauncher.logger.debug("LDU_ReadData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		ApplicationLauncher.logger.debug("LDU_ReadData: setExpectedLength:"+SerialPortObj.getExpectedLength());
		SerialDataLDU lduData = new SerialDataLDU(SerialPortObj);
		lduData.SerialReponseTimerStart(30);
		SerialPortObj = null;//garbagecollector
		return lduData;
	}

	public SerialDataRadiantRefStd Ref_ReadBNC_Response(int Expectedlength,String ExpectedResponse){
		ApplicationLauncher.logger.debug("Ref_ReadBNC_Response :Entry");
		Communicator SerialPortObj = commRefStandard;

		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);
		SerialDataRadiantRefStd RefData = new SerialDataRadiantRefStd(SerialPortObj);

		RefData.SerialReponseTimerStart(20);

		SerialPortObj = null;//garbagecollector

		return RefData;
	}

	public SerialDataRadiantRefStd Ref_ReadAccumulativeData(int Expectedlength,String ExpectedResponse){
		ApplicationLauncher.logger.debug("Ref_ReadAccumulativeData :Entry");
		Communicator SerialPortObj = commRefStandard;

		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);
		SerialDataRadiantRefStd RefData = new SerialDataRadiantRefStd(SerialPortObj);
		RefData.SerialReponseTimerStart(20);
		SerialPortObj = null;//garbagecollector
		return RefData;
	}

	public void StripLDU_SerialData(Integer length){
		ApplicationLauncher.logger.debug("StripLDU_SerialData :Entry");
		Communicator SerialPortObj =commLDU;
		SerialPortObj.StripLength(length);
		ApplicationLauncher.logger.debug("StripLDU_SerialData : getSerialData :"+SerialPortObj.getSerialData());

	}

	public void StripRefStd_SerialData(Integer length){
		Communicator SerialPortObj =commRefStandard;
		SerialPortObj.StripLength(length);
		ApplicationLauncher.logger.info("StripRefStd_SerialData: Length: " + length);

	}

	public void RefStdCommSendCommand(String CommandForReadingPhase,Integer ExpectedLength) {


		ApplicationLauncher.logger.debug("RefStdCommSendCommand :Entry");
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			WriteToSerialCommRefStd(CommandForReadingPhase,ExpectedLength);
			Sleep(200);
		}

	} 

	/*	public void mteRefStdCommSendCommand(String CommandForReadingPhase,Integer ExpectedLength) {


		ApplicationLauncher.logger.debug("mteRefStdCommSendCommand :Entry");
		WriteToSerialCommRefStd(CommandForReadingPhase,ExpectedLength);
		Sleep(200);

	} */

	public boolean mteRefStdCommSendCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("mteRefStdCommSendCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.writeStringMsgToPort(commandData);
			SerialPortObj.setExpectedResult(expectedResponseData);
			SerialDataMteRefStd pwerData = new SerialDataMteRefStd(SerialPortObj);
			pwerData.SerialReponseTimerStart(20);
			status = pwerData.IsExpectedResponseReceived();

			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("mteRefStdCommSendCommand: Command response failed:");
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code XX2: RefStd: Command response failed");
					///Gopi
					//setSkipCurrentTP_Execution(true);
				}
			}
			pwerData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}


	public boolean kreRefStdCommSendCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("kreRefStdCommSendCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.ClearSerialData();
			//SerialPortObj.writeStringMsgToPort(commandData);
			SerialPortObj.writeStringMsgToPortInHex(commandData);
			SerialPortObj.setExpectedResult(expectedResponseData);
			SerialDataRefStdKre pwerData = new SerialDataRefStdKre(SerialPortObj);
			pwerData.SerialReponseTimerStart(30);
			status = pwerData.IsExpectedResponseReceived();

			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("kreRefStdCommSendCommand: Command response failed:");
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code XX2: RefStd: Command response failed");
					///Gopi
					//setSkipCurrentTP_Execution(true);
				}
			}
			pwerData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}

	/*	public boolean kiggsRefStdCommSendCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("kiggsRefStdCommSendCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.ClearSerialData();
			//SerialPortObj.writeStringMsgToPort(commandData);
			SerialPortObj.writeStringMsgToPortInHex(commandData);
			SerialPortObj.setExpectedResult(expectedResponseData);
			//SerialPortObj.setExpectedLength(expectedDataLength);
			SerialDataRefStdKiggs pwerData = new SerialDataRefStdKiggs(SerialPortObj);
			pwerData.SerialReponseTimerStart(30);
			status = pwerData.IsExpectedResponseReceived();

			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("kiggsRefStdCommSendCommand: Command response failed:");
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code XX2: RefStd: Command response failed");
					///Gopi
					//setSkipCurrentTP_Execution(true);
				}
			}
			pwerData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}*/

	public boolean kreIctCommSendCommand(String commandData,String expectedResponseData, int noOfReadingRequired ){
		ApplicationLauncher.logger.debug("kreIctCommSendCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commICT;
		if(!getSkipCurrentTP_Execution()){
			SerialDataKreICT.clearIctReadSerialData();
			lsLDU_ClearSerialData();
			//SerialPortObj.writeStringMsgToPort(commandData);scxz
			SerialPortObj.setExpectedResult(expectedResponseData);
			WriteToSerialCommICT(commandData);
			SerialDataKreICT ictData = new SerialDataKreICT(SerialPortObj);
			ictData.SerialReponseTimerStartV2(20,noOfReadingRequired);
			status = ictData.IsExpectedResponseReceived();

			if (!status){
				if(ictData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("kreIctCommSendCommand: Command response failed:");
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code ICT2: ICT: Command response failed");
					///Gopi
					//setSkipCurrentTP_Execution(true);
				}
			}else{

			}
			ictData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}

	public boolean mteRefStdCommSendReadDataCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("mteRefStdCommSendReadDataCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.setExpectedResult(expectedResponseData);
			SerialPortObj.writeStringMsgToPort(commandData);
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}

	public boolean lscsPowerSourceCommSendReadFeedBackDataCommand(String commandData,String expectedResponseData){
		//ApplicationLauncher.logger.debug("lscsPowerSourceCommSendReadFeedBackDataCommand :Entry:" + getSkipCurrentTP_Execution());
		//expectedResponseData = GUIUtils.StringToHex(expectedResponseData);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		if(!getSkipCurrentTP_Execution()){
			//ApplicationLauncher.logger.debug("lscsPowerSourceCommSendReadFeedBackDataCommand :Entry2");
			//ApplicationLauncher.logger.debug("lscsPowerSourceCommSendReadFeedBackDataCommand :commandData :" + commandData);
			SerialPortObj.setExpectedResult(expectedResponseData);
			SerialPortObj.writeStringMsgToPort(commandData);
			//SerialPortObj.writeStringMsgToPortInHex(commandData);//.writeStringMsgToPort(commandData);
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}

	public boolean kiggsRefStdCommSendDataCommand(String commandData,String expectedResponseData, int expectedDataLength){
		ApplicationLauncher.logger.debug("kiggsRefStdCommSendDataCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.ClearSerialData();
			SerialPortObj.setExpectedResult(expectedResponseData);
			SerialPortObj.setExpectedLength(expectedDataLength);
			//ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommand :commandData :" + GUIUtils.hexToAscii(commandData));
			ApplicationLauncher.logger.debug("kiggsRefStdCommSendDataCommand :commandData :" + commandData);
			//SerialPortObj.writeStringMsgToPort(commandData);
			SerialPortObj.writeStringMsgToPortInHex(commandData);

			status= true;
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}



	public boolean kreRefStdCommSendReadDataCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommand :Entry");
		ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommand : commandData: " + commandData);
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			SerialPortObj.setExpectedResult(expectedResponseData);
			//ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommand :commandData :" + GuiUtils.hexToAscii(commandData));
			SerialPortObj.writeStringMsgToPort(commandData);
			//SerialPortObj.writeStringMsgToPortInHex(commandData);

			status= true;
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}
	
	
/*	public boolean kreRefStdCommSendReadDataCommandSpmV2(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommandSpmV2 :Entry");
		boolean status= false;
		//Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			//SerialPortObj.setExpectedResult(expectedResponseData);
			ApplicationLauncher.logger.debug("kreRefStdCommSendReadDataCommandSpmV2 :commandData :" + GuiUtils.hexToAscii(commandData));
			//SerialPortObj.writeStringMsgToPort(commandData);
			//SerialPortObj.writeStringMsgToPortInHex(commandData);
			//DisplayDataObj.getSerialPortManagerRefStd().
			status= true;
		}
		//SerialPortObj = null;//garbagecollector

		return status;
	}*/

	public boolean kreRefStdCommSendWriteDataCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("kreRefStdCommSendWriteDataCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			//SerialPortObj.ClearSerialData();
			SerialPortObj.setExpectedResult(expectedResponseData);
			//ApplicationLauncher.logger.debug("kreRefStdCommSendWriteDataCommand :commandData :" + GuiUtils.hexToAscii(commandData));
			//SerialPortObj.writeStringMsgToPortInHex(commandData);
			SerialPortObj.writeStringMsgToPort(commandData);
			

			status= true;
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}



	public SerialDataMteRefStd mteRefStdReadAllParameterData(int noOfReadingRequired){
		ApplicationLauncher.logger.debug("mteRefStdReadAllParameterData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStartV2(40, noOfReadingRequired);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					//ApplicationLauncher.logger.debug("mteRefStdReadAllParameterData : test1");
					String receivedResponse[] = CurrentReadData.split(ConstantRefStdMte.ER_TERMINATOR);
					if(receivedResponse.length >= 6) {
						//ApplicationLauncher.logger.debug("mteRefStdReadAllParameterData : test2");
						try {
							String currentData  = receivedResponse[0];
							String voltageData  = receivedResponse[1];
							String activePowerData  = receivedResponse[2];
							String apparentPowerData  = receivedResponse[3];
							String phaseAngleData  = receivedResponse[4];
							String freqData  = receivedResponse[5];
							refStdData.ParseCurrentDatafromRefStd(currentData);
							refStdData.ParseVoltageDatafromRefStd(voltageData);
							refStdData.ParseWattDatafromRefStd(activePowerData);
							refStdData.ParsePowerFactorDatafromRefStdV2(apparentPowerData);
							refStdData.ParseDegreePhaseDatafromRefStd(phaseAngleData);
							refStdData.ParseFreqDatafromRefStd(freqData);
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("mteRefStdReadAllParameterData :Exception1:"+ e.getMessage());

						}


						SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					}
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadAllParameterData :Exception2:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public Data_PowerSourceFeedBack processPowerSource3PhaseReadFeedBackData(String receivedResponse[]){

		Data_PowerSourceFeedBack powerSourceFeedBackData = new Data_PowerSourceFeedBack();
		String rPhaseVoltageData  = receivedResponse[0];
		if(rPhaseVoltageData.startsWith(ConstantPowerSourceLscs.ER_PWR_SRC_READ_ALL_HEADER)){
			rPhaseVoltageData = rPhaseVoltageData.replace(ConstantPowerSourceLscs.ER_PWR_SRC_READ_ALL_HEADER, "");
		}
		String yPhaseVoltageData  = receivedResponse[1];
		String bPhaseVoltageData  = receivedResponse[2];
		String rPhaseCurrentData  = receivedResponse[3];	
		String yPhaseCurrentData  = receivedResponse[4];
		String bPhaseCurrentData  = receivedResponse[5];

		String rPhasePhaseAngleData  = receivedResponse[6];
		String yPhasePhaseAngleData  = receivedResponse[7];
		String bPhasePhaseAngleData  = receivedResponse[8];

		double rPhasePowerFactor = 0.0f;
		double yPhasePowerFactor = 0.0f;
		double bPhasePowerFactor = 0.0f;

		String freqData  = receivedResponse[9];
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :rPhaseVoltageData0: " + rPhaseVoltageData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :yPhaseVoltageData0: " + yPhaseVoltageData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :bPhaseVoltageData0: " + bPhaseVoltageData);

		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :rPhaseCurrentData0: " + rPhaseCurrentData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :yPhaseCurrentData0: " + yPhaseCurrentData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :bPhaseCurrentData0: " + bPhaseCurrentData);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			rPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DisplayDataObj.getR_PhaseOutputVoltage(),rPhaseVoltageData,DisplayDataObj.getR_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getR_PhaseFeedBackProcessVoltageOffset());
			yPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
					DisplayDataObj.getY_PhaseOutputVoltage(),yPhaseVoltageData,DisplayDataObj.getY_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getY_PhaseFeedBackProcessVoltageOffset());

			bPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
					DisplayDataObj.getB_PhaseOutputVoltage(),bPhaseVoltageData,DisplayDataObj.getB_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getB_PhaseFeedBackProcessVoltageOffset());

			rPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DisplayDataObj.getR_PhaseOutputCurrent(),rPhaseCurrentData,DisplayDataObj.getR_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getR_PhaseFeedBackProcessCurrentOffset());
			yPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
					DisplayDataObj.getY_PhaseOutputCurrent(),yPhaseCurrentData,DisplayDataObj.getY_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getY_PhaseFeedBackProcessCurrentOffset());
			bPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
					DisplayDataObj.getB_PhaseOutputCurrent(),bPhaseCurrentData,DisplayDataObj.getB_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getB_PhaseFeedBackProcessCurrentOffset());


		}

		rPhaseVoltageData = String.format("%.2f", (Float.valueOf(rPhaseVoltageData)));
		yPhaseVoltageData = String.format("%.2f", (Float.valueOf(yPhaseVoltageData)));
		bPhaseVoltageData = String.format("%.2f", (Float.valueOf(bPhaseVoltageData)));

		rPhaseCurrentData = String.format("%.2f", (Float.valueOf(rPhaseCurrentData)));
		yPhaseCurrentData = String.format("%.2f", (Float.valueOf(yPhaseCurrentData)));
		bPhaseCurrentData = String.format("%.2f", (Float.valueOf(bPhaseCurrentData)));


		rPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(rPhasePhaseAngleData)/100));
		yPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(yPhasePhaseAngleData)/100));
		bPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(bPhasePhaseAngleData)/100));
		freqData = String.format("%.2f", Float.valueOf(freqData)/100);


		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :rPhaseVoltageData: " + rPhaseVoltageData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :yPhaseVoltageData: " + yPhaseVoltageData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :bPhaseVoltageData: " + bPhaseVoltageData);

		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :rPhaseCurrentData: " + rPhaseCurrentData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :yPhaseCurrentData: " + yPhaseCurrentData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :bPhaseCurrentData: " + bPhaseCurrentData);

		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :rPhasePhaseAngleData: " + rPhasePhaseAngleData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :yPhasePhaseAngleData: " + yPhasePhaseAngleData);
		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :bPhasePhaseAngleData: " + bPhasePhaseAngleData);

		ApplicationLauncher.logger.debug("processPowerSource3PhaseReadFeedBackData :freqData: " + freqData);
		powerSourceFeedBackData.setR_PhaseVoltageDisplayData(rPhaseVoltageData);
		powerSourceFeedBackData.setY_PhaseVoltageDisplayData(yPhaseVoltageData);
		powerSourceFeedBackData.setB_PhaseVoltageDisplayData(bPhaseVoltageData);

		powerSourceFeedBackData.setR_PhaseCurrentDisplayData(rPhaseCurrentData);
		powerSourceFeedBackData.setY_PhaseCurrentDisplayData(yPhaseCurrentData);
		powerSourceFeedBackData.setB_PhaseCurrentDisplayData(bPhaseCurrentData);

		powerSourceFeedBackData.setR_PhaseDegreePhaseData(rPhasePhaseAngleData);
		powerSourceFeedBackData.setY_PhaseDegreePhaseData(yPhasePhaseAngleData);
		powerSourceFeedBackData.setB_PhaseDegreePhaseData(bPhasePhaseAngleData);

		//rPhasePowerFactor = calculatePowerFactorWithDegree();Math.cos(Double.valueOf(rPhasePhaseAngleData));
		//yPhasePowerFactor = Math.acos(Double.valueOf(yPhasePhaseAngleData));
		//bPhasePowerFactor = Math.cos(Double.valueOf(bPhasePhaseAngleData)*Math.PI);
		//String rPhasePowerFactorStr = String.format("%.4f", rPhasePowerFactor);
		//String yPhasePowerFactorStr = String.format("%.4f", yPhasePowerFactor);
		//String bPhasePowerFactorStr = String.format("%.4f", bPhasePowerFactor);

		String rPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(rPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		String yPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(yPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		String bPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(bPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		powerSourceFeedBackData.setR_PhasePowerFactorData(rPhasePowerFactorStr);
		powerSourceFeedBackData.setY_PhasePowerFactorData(yPhasePowerFactorStr);
		powerSourceFeedBackData.setB_PhasePowerFactorData(bPhasePowerFactorStr);

		//bPhasePowerFactor = Math.acos(Double.valueOf(bPhasePhaseAngleData)*Math.PI);
		//ApplicationLauncher.logger.debug("processPowerSourceReadFeedBackData :bPhasePowerFactor: " + bPhasePowerFactor);

		powerSourceFeedBackData.setFreqData(freqData);

		return powerSourceFeedBackData;
	}
	
	
	public Data_PowerSourceFeedBack processPowerSource1PhaseReadFeedBackData(String receivedResponse[]){

		Data_PowerSourceFeedBack powerSourceFeedBackData = new Data_PowerSourceFeedBack();
		String rPhaseVoltageData  = receivedResponse[0];
		if(rPhaseVoltageData.startsWith(ConstantPowerSourceLscs.ER_PWR_SRC_READ_ALL_HEADER)){
			rPhaseVoltageData = rPhaseVoltageData.replace(ConstantPowerSourceLscs.ER_PWR_SRC_READ_ALL_HEADER, "");
		}
		//String yPhaseVoltageData  = receivedResponse[1];
		//String bPhaseVoltageData  = receivedResponse[2];
		String rPhaseCurrentData  = receivedResponse[1];	
		//String yPhaseCurrentData  = receivedResponse[4];
		//String bPhaseCurrentData  = receivedResponse[5];

		String rPhasePhaseAngleData  = receivedResponse[2];
		//String yPhasePhaseAngleData  = receivedResponse[7];
		//String bPhasePhaseAngleData  = receivedResponse[8];

		double rPhasePowerFactor = 0.0f;
		//double yPhasePowerFactor = 0.0f;
		//double bPhasePowerFactor = 0.0f;

		String freqData  = receivedResponse[3];
		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :rPhaseVoltageData0: " + rPhaseVoltageData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :yPhaseVoltageData0: " + yPhaseVoltageData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :bPhaseVoltageData0: " + bPhaseVoltageData);

		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :rPhaseCurrentData0: " + rPhaseCurrentData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :getR_PhaseFeedBackProcessVoltageGain: " + DisplayDataObj.getR_PhaseFeedBackProcessVoltageGain());
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :getR_PhaseFeedBackProcessVoltageOffset: " + DisplayDataObj.getR_PhaseFeedBackProcessVoltageOffset());
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :getR_PhaseFeedBackProcessCurrentGain: " + DisplayDataObj.getR_PhaseFeedBackProcessCurrentGain());
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :getR_PhaseFeedBackProcessCurrentOffset: " + DisplayDataObj.getR_PhaseFeedBackProcessCurrentOffset());
		
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :yPhaseCurrentData0: " + yPhaseCurrentData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :bPhaseCurrentData0: " + bPhaseCurrentData);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			rPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DisplayDataObj.getR_PhaseOutputVoltage(),rPhaseVoltageData,DisplayDataObj.getR_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getR_PhaseFeedBackProcessVoltageOffset());
/*			yPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
					DisplayDataObj.getY_PhaseOutputVoltage(),yPhaseVoltageData,DisplayDataObj.getY_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getY_PhaseFeedBackProcessVoltageOffset());

			bPhaseVoltageData = DisplayDataObj.getAbsoluteVoltage(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
					DisplayDataObj.getB_PhaseOutputVoltage(),bPhaseVoltageData,DisplayDataObj.getB_PhaseFeedBackProcessVoltageGain(),
					DisplayDataObj.getB_PhaseFeedBackProcessVoltageOffset());*/

			rPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DisplayDataObj.getR_PhaseOutputCurrent(),rPhaseCurrentData,DisplayDataObj.getR_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getR_PhaseFeedBackProcessCurrentOffset());
/*			yPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
					DisplayDataObj.getY_PhaseOutputCurrent(),yPhaseCurrentData,DisplayDataObj.getY_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getY_PhaseFeedBackProcessCurrentOffset());
			bPhaseCurrentData = DisplayDataObj.getAbsoluteCurrent(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
					DisplayDataObj.getB_PhaseOutputCurrent(),bPhaseCurrentData,DisplayDataObj.getB_PhaseFeedBackProcessCurrentGain(),
					DisplayDataObj.getB_PhaseFeedBackProcessCurrentOffset());*/


		}

		rPhaseVoltageData = String.format("%.2f", (Float.valueOf(rPhaseVoltageData)));
		//yPhaseVoltageData = String.format("%.2f", (Float.valueOf(yPhaseVoltageData)));
		//bPhaseVoltageData = String.format("%.2f", (Float.valueOf(bPhaseVoltageData)));
		if(Float.valueOf(rPhaseCurrentData)<=1.0f){
			rPhaseCurrentData = String.format("%.4f", (Float.valueOf(rPhaseCurrentData)));
		}else if(Float.valueOf(rPhaseCurrentData)<10.0f){
			rPhaseCurrentData = String.format("%.3f", (Float.valueOf(rPhaseCurrentData)));
		}else{
			rPhaseCurrentData = String.format("%.2f", (Float.valueOf(rPhaseCurrentData)));
		}
		//yPhaseCurrentData = String.format("%.2f", (Float.valueOf(yPhaseCurrentData)));
		//bPhaseCurrentData = String.format("%.2f", (Float.valueOf(bPhaseCurrentData)));


		rPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(rPhasePhaseAngleData)/100));
		//yPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(yPhasePhaseAngleData)/100));
		//bPhasePhaseAngleData = String.format("%.2f", (Float.valueOf(bPhasePhaseAngleData)/100));
		freqData = String.format("%.2f", Float.valueOf(freqData)/100);


		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :rPhaseVoltageData: " + rPhaseVoltageData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :yPhaseVoltageData: " + yPhaseVoltageData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :bPhaseVoltageData: " + bPhaseVoltageData);

		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :rPhaseCurrentData: " + rPhaseCurrentData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :yPhaseCurrentData: " + yPhaseCurrentData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :bPhaseCurrentData: " + bPhaseCurrentData);

		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :rPhasePhaseAngleData: " + rPhasePhaseAngleData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :yPhasePhaseAngleData: " + yPhasePhaseAngleData);
		//ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :bPhasePhaseAngleData: " + bPhasePhaseAngleData);

		ApplicationLauncher.logger.debug("processPowerSource1PhaseReadFeedBackData :freqData: " + freqData);
		powerSourceFeedBackData.setR_PhaseVoltageDisplayData(rPhaseVoltageData);
		//powerSourceFeedBackData.setY_PhaseVoltageDisplayData(yPhaseVoltageData);
		//powerSourceFeedBackData.setB_PhaseVoltageDisplayData(bPhaseVoltageData);

		powerSourceFeedBackData.setR_PhaseCurrentDisplayData(rPhaseCurrentData);
		//powerSourceFeedBackData.setY_PhaseCurrentDisplayData(yPhaseCurrentData);
		//powerSourceFeedBackData.setB_PhaseCurrentDisplayData(bPhaseCurrentData);

		powerSourceFeedBackData.setR_PhaseDegreePhaseData(rPhasePhaseAngleData);
		//powerSourceFeedBackData.setY_PhaseDegreePhaseData(yPhasePhaseAngleData);
		//powerSourceFeedBackData.setB_PhaseDegreePhaseData(bPhasePhaseAngleData);

		//rPhasePowerFactor = calculatePowerFactorWithDegree();Math.cos(Double.valueOf(rPhasePhaseAngleData));
		//yPhasePowerFactor = Math.acos(Double.valueOf(yPhasePhaseAngleData));
		//bPhasePowerFactor = Math.cos(Double.valueOf(bPhasePhaseAngleData)*Math.PI);
		//String rPhasePowerFactorStr = String.format("%.4f", rPhasePowerFactor);
		//String yPhasePowerFactorStr = String.format("%.4f", yPhasePowerFactor);
		//String bPhasePowerFactorStr = String.format("%.4f", bPhasePowerFactor);

		String rPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(rPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		//String yPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(yPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		//String bPhasePowerFactorStr = GuiUtils.calculatePowerFactorWithDegree(String.valueOf(bPhasePhaseAngleData),ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION);
		powerSourceFeedBackData.setR_PhasePowerFactorData(rPhasePowerFactorStr);
		//powerSourceFeedBackData.setY_PhasePowerFactorData(yPhasePowerFactorStr);
		//powerSourceFeedBackData.setB_PhasePowerFactorData(bPhasePowerFactorStr);

		//bPhasePowerFactor = Math.acos(Double.valueOf(bPhasePhaseAngleData)*Math.PI);
		//ApplicationLauncher.logger.debug("processPowerSourceReadFeedBackData :bPhasePowerFactor: " + bPhasePowerFactor);

		powerSourceFeedBackData.setFreqData(freqData);

		return powerSourceFeedBackData;
	}

	public Data_PowerSourceFeedBack powerSourceReadFeedBackAllParameterData(int noOfReadingRequired){
		ApplicationLauncher.logger.debug("powerSourceReadFeedBackAllParameterData :Entry");
		Communicator SerialPortObj =commPowerSrc;
		SerialDataPowerSrc powerSrcData = new SerialDataPowerSrc(SerialPortObj);
		Data_PowerSourceFeedBack powerSourceFeedBackData = new Data_PowerSourceFeedBack();
		//Sleep(200);
		powerSrcData.SerialReponseTimerStartV2(40, noOfReadingRequired);// 40 * 100 = 4000 milli second wait
		try{
			if(powerSrcData.IsExpectedResponseReceived()){
				String CurrentReadData = powerSrcData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					//ApplicationLauncher.logger.debug("powerSourceReadFeedBackAllParameterData : test1");
					String receivedResponse[] = CurrentReadData.split(ConstantPowerSourceLscs.ER_PWR_SRC_SEPERATOR);
					
					//cfvbv
					//if(receivedResponse.length >= 6) {
					if(receivedResponse.length >= DisplayDataObj.getReadProPowerAllDataNoOfVariables()) {	
						//ApplicationLauncher.logger.debug("powerSourceReadFeedBackAllParameterData : test2");
						try {
							if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
								powerSourceFeedBackData = processPowerSource3PhaseReadFeedBackData(receivedResponse);
							}else {
								powerSourceFeedBackData = processPowerSource1PhaseReadFeedBackData(receivedResponse);
							}
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("powerSourceReadFeedBackAllParameterData :Exception1:"+ e.getMessage());

						}


						SerialPortObj.StripLength(powerSrcData.getRefStd_ReadSerialData().length());
					}
					return powerSourceFeedBackData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("powerSourceReadFeedBackAllParameterData :Exception2:"+ e.getMessage());

		}
		powerSrcData = null;
		powerSourceFeedBackData = null;
		return powerSourceFeedBackData;
	}

	public SerialDataRefStdKre kreRefStdReadBasicParameterData(int noOfReadingRequired){
		ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKre refStdData = new SerialDataRefStdKre(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStartV2(150, noOfReadingRequired);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = GuiUtils.StringToHex(refStdData.getRefStd_ReadSerialData());
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					refStdData.parseBasicsSerialData(CurrentReadData);
					//ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData : test1");
					/*String receivedResponse[] = CurrentReadData.split(ConstantMteRefStd.ER_REF_STD_SEPERATOR);
					if(receivedResponse.length >= 6) {
						//ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData : test2");
						try {

							ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :receivedResponse[0]:"+ receivedResponse[0]);
							ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :receivedResponse[1]:"+ receivedResponse[1]);
							ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :receivedResponse[2]:"+ receivedResponse[2]);
							ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :receivedResponse[3]:"+ receivedResponse[3]);
							ApplicationLauncher.logger.debug("kreRefStdReadBasicParameterData :receivedResponse[4]:"+ receivedResponse[4]);
							String currentData  = receivedResponse[0];
							String voltageData  = receivedResponse[1].replace("U1=", "").trim();
							String activePowerData  = receivedResponse[2];
							String apparentPowerData  = receivedResponse[3];
							String phaseAngleData  = receivedResponse[4];
							String freqData  = receivedResponse[0].replace("KRE:////FREQ=", "").trim();


							refStdData.ParseCurrentDatafromRefStd(currentData);
							refStdData.ParseVoltageDatafromRefStd(voltageData);
							refStdData.ParseWattDatafromRefStd(activePowerData);
							refStdData.ParsePowerFactorDatafromRefStdV2(apparentPowerData);
							refStdData.ParseDegreePhaseDatafromRefStd(phaseAngleData);
							refStdData.ParseFreqDatafromRefStd(freqData);
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("kreRefStdReadBasicParameterData :Exception1:"+ e.getMessage());

						}


						SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					}*/
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kreRefStdReadBasicParameterData :Exception2:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKre kreRefStdReadSettingParameterData(String expectedResponseData,int noOfReadingRequired){
		ApplicationLauncher.logger.debug("kreRefStdReadSettingParameterData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialPortObj.setExpectedResult(expectedResponseData);
		SerialDataRefStdKre refStdData = new SerialDataRefStdKre(SerialPortObj);
		//Sleep(200);

		refStdData.SerialReponseTimerStartV2(40, noOfReadingRequired);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				ApplicationLauncher.logger.debug("kreRefStdReadSettingParameterData : ExpectedResponse recieved");
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				//ApplicationLauncher.logger.debug("kreRefStdReadSettingParameterData : CurrentReadData: " + CurrentReadData);
				if (CurrentReadData !=null){
					//ApplicationLauncher.logger.debug("kreRefStdReadSettingParameterData : CurrentReadData2: " + CurrentReadData);
					refStdData.parseSettingsSerialData(CurrentReadData);

					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}else{
				ApplicationLauncher.logger.debug("kreRefStdReadSettingParameterData : ExpectedResponse not recieved");
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kreRefStdReadSettingParameterData :Exception2:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKiggs kiggsRefStdReadVoltageAndCurrentData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadVoltageAndCurrentData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadVoltageAndCurrentData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseVoltageDatafromRefStd(readData);
					refStdData.parseCurrentDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadVoltageAndCurrentData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKiggs kiggsRefStdReadDegreeData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadDegreeData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadDegreeData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseDegreeDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadDegreeData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}




	public SerialDataRefStdKiggs kiggsRefStdReadPowerFactorData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadPowerFactorData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadPowerFactorData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parsePowerFactorDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadPowerFactorData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}


	public SerialDataRefStdKiggs kiggsRefStdReadApparentPowerData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadApparentPowerData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadApparentPowerData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseApparentPowerDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadApparentPowerData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}


	public SerialDataRefStdKiggs kiggsRefStdReadSettingVoltTapAndCurrentTapData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadSettingVoltTapAndCurrentTapData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadSettingVoltTapAndCurrentTapData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseVoltageTapDatafromRefStd(readData);
					refStdData.parseCurrentTapDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadSettingVoltTapAndCurrentTapData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKiggs kiggsRefStdReadReactivePowerData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadReactivePowerData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadReactivePowerData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseReactivePowerDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadReactivePowerData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKiggs kiggsRefStdReadActivePowerData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadActivePowerData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadActivePowerData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseActivePowerDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadActivePowerData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRefStdKiggs kiggsRefStdReadFrequencyData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadFrequencyData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadFrequencyData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseFrequencyDatafromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadFrequencyData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}


	public SerialDataRefStdKiggs kiggsRefStdReadAccuEnergyData(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadAccuEnergyData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRefStdKiggs refStdData = new SerialDataRefStdKiggs(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String readData = SerialDataRefStdKiggs.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.debug("kiggsRefStdReadAccuEnergyData :readData: " +readData);
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (readData !=null){
					refStdData.parseAccumulatedActiveEnergyfromRefStd(readData);
					refStdData.parseAccumulatedReactiveEnergyfromRefStd(readData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadAccuEnergyData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataMteRefStd mteRefStdReadVoltageData(){
		ApplicationLauncher.logger.debug("mteRefStdReadVoltageData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					refStdData.ParseVoltageDatafromRefStd(CurrentReadData);
					/*refStdData.ParseCurrentDatafromRefStd(CurrentReadData);
					refStdData.ParseWattDatafromRefStd(CurrentReadData);
					refStdData.ParseVA_DatafromRefStd(CurrentReadData);
					refStdData.ParseVAR_DatafromRefStd(CurrentReadData);
					refStdData.ParseFreqDatafromRefStd(CurrentReadData);
					refStdData.ParseDegreePhaseDatafromRefStd(CurrentReadData);
					refStdData.ParsePowerFactorDatafromRefStd(CurrentReadData);*/
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadPhaseData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}



	public SerialDataMteRefStd mteRefStdReadCurrentData(){
		ApplicationLauncher.logger.debug("mteRefStdReadCurrentData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){

					refStdData.ParseCurrentDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadCurrentData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}


	public SerialDataMteRefStd mteRefStdReadWattData(){
		ApplicationLauncher.logger.debug("mteRefStdReadWattData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){

					refStdData.ParseWattDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadWattData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataMteRefStd mteRefStdReadPowerFactorData(){
		ApplicationLauncher.logger.debug("mteRefStdReadPowerFactorData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){

					refStdData.ParsePowerFactorDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadPowerFactorData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}



	public SerialDataMteRefStd mteRefStdReadDegreePhaseData(){
		//ApplicationLauncher.logger.debug("mteRefStdReadDegreePhaseData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){

					refStdData.ParseDegreePhaseDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadDegreePhaseData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataMteRefStd mteRefStdReadFreqData(){
		ApplicationLauncher.logger.debug("mteRefStdReadFreqData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataMteRefStd refStdData = new SerialDataMteRefStd(SerialPortObj);
		//Sleep(200);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedResponseReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				//CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					refStdData.ParseFreqDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("mteRefStdReadFreqData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public SerialDataRadiantRefStd RefStdReadPhaseData(){
		ApplicationLauncher.logger.debug("RefStdReadPhaseData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRadiantRefStd refStdData = new SerialDataRadiantRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);// 40 * 100 = 4000 milli second wait
		try{
			if(refStdData.IsExpectedLengthReceived()){
				String CurrentReadData = refStdData.getRefStd_ReadSerialData();
				CurrentReadData = refStdData.AssertValidateForHeaderAndStripFirstByteIfInvalid(CurrentReadData);
				if (CurrentReadData !=null){
					refStdData.ParseVoltageDatafromRefStd(CurrentReadData);
					refStdData.ParseCurrentDatafromRefStd(CurrentReadData);
					refStdData.ParseWattDatafromRefStd(CurrentReadData);
					refStdData.ParseVA_DatafromRefStd(CurrentReadData);
					refStdData.ParseVAR_DatafromRefStd(CurrentReadData);
					refStdData.ParseFreqDatafromRefStd(CurrentReadData);
					refStdData.ParseDegreePhaseDatafromRefStd(CurrentReadData);
					refStdData.ParsePowerFactorDatafromRefStd(CurrentReadData);
					SerialPortObj.StripLength(refStdData.getRefStd_ReadSerialData().length());
					return refStdData;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("RefStdReadPhaseData :Exception:"+ e.getMessage());

		}
		refStdData = null;
		return refStdData;
	}

	public void ClearStdRefSerialData(){
		Communicator SerialPortObj =commRefStandard;
		commRefStandard.ClearSerialData();
	}

	public void ClearPowerSourceSerialData(){
		//Communicator SerialPortObj =commPowerSrc;
		commPowerSrc.ClearSerialData();
	}

	public SerialDataRadiantRefStd RefStdReadData(){
		ApplicationLauncher.logger.debug("RefStdReadData :Entry");
		Communicator SerialPortObj =commRefStandard;
		SerialDataRadiantRefStd refStdData = new SerialDataRadiantRefStd(SerialPortObj);
		refStdData.SerialReponseTimerStart(40);
		if(refStdData.IsExpectedLengthReceived()){
			refStdData.getRefStd_ReadSerialData();
			return refStdData;
		}
		return refStdData;
	}



	public void WriteToSerialCommRefStd(String Data, Integer ExpectedLength){
		ApplicationLauncher.logger.debug("WriteToSerialCommRefStd :Entry");
		Communicator SerialPortObj =commRefStandard;

		ApplicationLauncher.logger.debug("WriteToSerialCommRefStd :DataHex:"+Data);
		SerialPortObj.writeStringMsgToPortInHex(Data);
		SerialPortObj.setExpectedLength(ExpectedLength);
		SerialPortObj = null;//garbagecollector

	}


	public boolean SetSerialComm(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("SetSerialComm :SerialPort_ID:" + SerialPort_ID);
		boolean status=false;
		try{
			//ApplicationLauncher.logger.debug("SetSerialComm : test0");
			ApplicationLauncher.logger.debug("SetSerialComm : getDeviceConnected: " + SerialPortObj.getDeviceConnected());
			//ApplicationLauncher.logger.debug("SetSerialComm : getSerialPortOwner1: " + SerialPortObj.getSerialPortOwner(SerialPort_ID));
			SerialPortObj.connect(SerialPort_ID);
			ApplicationLauncher.logger.debug("SetSerialComm : test1");
			if(SerialPortObj.getDeviceConnected()==true){
				//ApplicationLauncher.logger.debug("SetSerialComm :test2");
				//ApplicationLauncher.logger.debug("SetSerialComm : getSerialPortOwner2: " + SerialPortObj.getSerialPortOwner(SerialPort_ID));
				
				if (SerialPortObj.initIOStream() == true){
					ApplicationLauncher.logger.debug("SetSerialComm :test3");
					SerialPortObj.SerialPortConfig(BaudRate);
					//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
					ApplicationLauncher.logger.info("SetSerialComm: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
					SerialPortObj.initListener();
					/*				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
					SerialPortObj.setDataReadFormatInHex(false);
				}else{*/
					SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
					//}
					status = true;
					return status;
				}
			}
		}catch(Exception e){
			ApplicationLauncher.logger.error("SetSerialComm: Exception: "+ e);
		}
		return status;
	}
	
	public boolean kreRefStdSetSerialComm(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("kreRefStdSetSerialComm :Entry");
		boolean status=false;

		SerialPortObj.connect(SerialPort_ID);
		ApplicationLauncher.logger.debug("kreRefStdSetSerialComm : test1");
		if(SerialPortObj.getDeviceConnected()==true){
			ApplicationLauncher.logger.debug("kreRefStdSetSerialComm :test2");
			if (SerialPortObj.initIOStream() == true){
				ApplicationLauncher.logger.debug("kreRefStdSetSerialComm :test3");
				SerialPortObj.SerialPortConfig(BaudRate);
				//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
				ApplicationLauncher.logger.info("kreRefStdSetSerialComm: PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
				SerialPortObj.initListener();
				//if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
					SerialPortObj.setDataReadFormatInHex(false);
				//}else{
				//	SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
				//}
				status = true;
				return status;
			}
		}
		return status;
	}

	public void InitSerialCommPort(){
		createObjects();
		commPowerSrc.searchForPorts();  

	}

	public void ScanForSerialCommPort(){

		commPowerSrc.searchForPorts();  

	}
	
	public void ScanForHarmonicsSerialCommPort(){
		createObjects();
		commHarmonicsSrc.searchForPorts();  

	}

	private void createObjects()
	{
		ApplicationLauncher.logger.debug("SerialDataManager: createObjects :Entry");
		commPowerSrc = new Communicator(ConstantPowerSourceMte.SERIAL_PORT_POWER_SOURCE);
		commRefStandard = new Communicator(ConstantRefStdRadiant.SERIAL_PORT_REF_STD);
		commLDU = new Communicator(ConstantLduCcube.SERIAL_PORT_LDU);
		try{
			//if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){ // commented due to failure in communication, during procal app boot ProCalCustomerConfiguration inits are not effective
			commICT = new Communicator(Constant_ICT.SERIAL_PORT_ICT);
			//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
				commHarmonicsSrc = new Communicator(ConstantLscsHarmonicsSourceSlave.SERIAL_PORT_HARMONICS_SLAVE);
				ApplicationLauncher.logger.debug("createObjects :Hit1");
			}
			
			if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
				commHarmonicsSrc = new Communicator(ConstantLscsHarmonicsSourceSlave.SERIAL_PORT_HARMONICS_SLAVE);
				ApplicationLauncher.logger.debug("createObjects :Hit2");
			}
			//}
		}catch(Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("createObjects Exception :"+E.getMessage());
		}
		ApplicationLauncher.logger.debug("SerialDataManager: createObjects :Exit");
	}

	public boolean LDU_ReadErrorData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_ReadErrorData :Entry");
		ApplicationHomeController.update_left_status("Reading LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :LDU_ReadErrorData:LDU_ReadAddress:"+LDU_ReadAddress);
		try{
			LDU_SendCommandReadErrorData(LDU_ReadAddress);
			SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_DATA_LENGTH,ConstantLduCcube.CMD_LDU_ERROR_DATA_ER);
			if(lduData.IsExpectedResponseReceived()){

				lduData.LDU_DecodeSerialData();
				ApplicationLauncher.logger.debug("LDU_ReadErrorData: getLDU_ErrorValue: " + lduData.getLDU_ErrorValue(LDU_ReadAddress));
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
				StripLDU_SerialData(lduData.getReceivedLength());
				String testruntype = DeviceDataManagerController.getTestRunType();
				ApplicationLauncher.logger.debug("LDU_ReadErrorData: testruntype: " + testruntype);
				ApplicationLauncher.logger.debug("LDU_ReadErrorData: getSkipCurrentTP_Execution: " + getSkipCurrentTP_Execution());

				String test_type = ProjectExecutionController.getCurrentTestType();
				ApplicationLauncher.logger.debug("LDU_ReadErrorData: test_type: " + test_type);
				ApplicationLauncher.logger.debug("LDU_ReadErrorData: getStepRunFlag(): " + ProjectExecutionController.getStepRunFlag());
				if(!test_type.equals(TestProfileType.Warmup.toString()) &&
						!ProjectExecutionController.getStepRunFlag()){
					if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) 
							&& !getSkipCurrentTP_Execution()){
						ApplicationLauncher.logger.debug("LDU_ReadErrorData: test1: ");
						String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
						if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
							ErrorValue = "WFR";
						}
						int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
						if(!(ErrorValue.equals("WFR"))){
							if(skip_reading <= 0){
								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

								ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
								devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
								DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
								ApplicationLauncher.logger.info("LDU_ReadErrorData: devices_to_be_read:-p " + devices_to_be_read);
								if(DisplayDataObj.getDevicesToBeRead().size() == 0){
									ApplicationLauncher.logger.info("LDU_ReadErrorData: All Devices Completed-p " );
									//RunningStatusController.setExecuteTimeCounter(5);
									ProjectExecutionController.setExecuteTimeCounter(0);
									lduTimer.cancel();
								}
							}
							else{
								ApplicationLauncher.logger.info("LDU_ReadErrorData: ErrorValue:-p  "  + ErrorValue);
								if(ProjectExecutionController.getExecuteTimeCounter()<30){
									int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
									ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
								}
								ApplicationLauncher.logger.info("LDU_ReadErrorData: NoOfPulseReadingToBeSkipped -p : "  + skip_reading);
								DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
							}
						}
						else{
							if(ProjectExecutionController.getExecuteTimeCounter()<30){
								int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
								ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
							}
						}
					}
					else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
						//ApplicationLauncher.logger.debug("LDU_ReadErrorData: test2: ");
						String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
						if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
							ErrorValue = "WFR";
						}
						//ApplicationLauncher.logger.debug("LDU_ReadErrorData: test3: " + ErrorValue);
						if(!(ErrorValue.equals("WFR"))){
							//ApplicationLauncher.logger.debug("LDU_ReadErrorData: test4: ");
							ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

							ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
							devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
							DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
							ApplicationLauncher.logger.info("LDU_ReadErrorData: devices_to_be_read-t:" + devices_to_be_read);
							if(DisplayDataObj.getDevicesToBeRead().size() == 0){
								ApplicationLauncher.logger.info("LDU_ReadErrorData: -t All Devices Completed" );
								//RunningStatusController.setExecuteTimeCounter(5);
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							}
							else{
								if(!isTimeExtendedForTimeBased()){
									ApplicationLauncher.logger.info("LDU_ReadErrorData: ErrorValue: -t  "  + ErrorValue);
									if(ProjectExecutionController.getExecuteTimeCounter()<30){
										int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) +
												TimeToBeExtendedInSec;
										ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
										ApplicationLauncher.logger.info("LDU_ReadErrorData: Time Extended:-t  " + extend_executetime);
										setTimeExtendedForTimeBased(true);
										//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
									}
								}
							}
						}
						else{
							ApplicationLauncher.logger.info("LDU_ReadErrorData: isTimeExtendedForTimeBased:-t  " + isTimeExtendedForTimeBased());
							ApplicationLauncher.logger.info("LDU_ReadErrorData: getExecuteTimeCounter:-t  " + ProjectExecutionController.getExecuteTimeCounter());
							if(!isTimeExtendedForTimeBased()){
								if(ProjectExecutionController.getExecuteTimeCounter()<30){
									int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) + 
											TimeToBeExtendedInSec;
									ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
									setTimeExtendedForTimeBased(true);
								}
							}
						}
					}
				}else if(ProjectExecutionController.getStepRunFlag()){
					if(!test_type.equals(TestProfileType.Warmup.toString())){
						if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) 
								&& !getSkipCurrentTP_Execution()){

							String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
							if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}
							if(!(ErrorValue.equals("WFR"))){

								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
							}
						} else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){

							String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
							if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}

							if(!(ErrorValue.equals("WFR"))){

								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
							}
						}
					}
				}

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("LDU_ReadErrorData:No Data Received");
			}
		}catch(Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("LDU_ReadErrorData Exception :"+E.getMessage());
		}
		return status;
	}

	public String lscsLduManipulateErrorResultStatus(String actualError) {
		//String ratioErrorActual = "";
		String acceptableErrorMax = "";
		String acceptableErrorMin = "";
		String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
		String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
		String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
		String result = undefined;
		try {
			acceptableErrorMin = DisplayDataObj.get_Error_min();////DisplayDataObj.getRatioErrorMin();
			acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
			ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus : actualError: " + actualError);
			ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMin: " + acceptableErrorMin);
			ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

			if(!actualError.isEmpty()){
				if ((Float.parseFloat(acceptableErrorMin)) <= Float.parseFloat(actualError)) {
					if (Float.parseFloat(actualError) <= (Float.parseFloat(acceptableErrorMax))) {
						result = pass;
						ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: :result: " + result);
					}else {
						if(!actualError.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR)){
							result = fail;
						}
					}
				}else {
					result = fail;
				}
			}
			//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsLduManipulateErrorResultStatus: Exception: " + e.getMessage());
		}

		//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
		//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
		return result;
	}

	public String lscsLduManipulateSTA_ResultStatus(String actualPulseCount) {
		//String ratioErrorActual = "";
		//String acceptableErrorMax = "";
		//String acceptableErrorMin = "";
		int minimumExpectedPulseCount  = 0;
		String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
		String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
		String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
		String result = undefined;
		try {
			minimumExpectedPulseCount = Integer.parseInt(DisplayDataObj.getSTANoOfPulses());////DisplayDataObj.getRatioErrorMin();
			//acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
			ApplicationLauncher.logger.info("lscsLduManipulateSTA_ResultStatus : actualPulseCount: " + actualPulseCount);
			ApplicationLauncher.logger.info("lscsLduManipulateSTA_ResultStatus: minimumExpectedPulseCount: " + minimumExpectedPulseCount);
			//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

			if(!actualPulseCount.isEmpty()){
				if ( Integer.parseInt(actualPulseCount) >= minimumExpectedPulseCount ) {
					//if (Float.parseFloat(actualPulseCount) <= (Float.parseFloat(acceptableErrorMax))) {
					result = pass;
					ApplicationLauncher.logger.info("lscsLduManipulateSTA_ResultStatus: :result: " + result);
					//}
				}else {

					if(!actualPulseCount.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR)){
						result = fail;
					}
					//result = fail;
				}
			}
			//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsLduManipulateSTA_ResultStatus: Exception: " + e.getMessage());
		}

		//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
		//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
		return result;
	}

	public String lscsLduManipulateCreepResultStatus(String actualPulseCount) {
		//String ratioErrorActual = "";
		//String acceptableErrorMax = "";
		//String acceptableErrorMin = "";
		int maximumAcceptablePulseCount  = 0;
		String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
		String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
		String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
		String result = undefined;
		try {
			maximumAcceptablePulseCount = Integer.parseInt(DisplayDataObj.getCreepNoOfPulses());////DisplayDataObj.getRatioErrorMin();
			//acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
			ApplicationLauncher.logger.info("lscsLduManipulateCreepResultStatus : actualPulseCount: " + actualPulseCount);
			ApplicationLauncher.logger.info("lscsLduManipulateCreepResultStatus: maximumAcceptablePulseCount: " + maximumAcceptablePulseCount);
			//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

			if(!actualPulseCount.isEmpty()){
				if ( Integer.parseInt(actualPulseCount) <= maximumAcceptablePulseCount ) {
					//if (Float.parseFloat(actualPulseCount) <= (Float.parseFloat(acceptableErrorMax))) {
					result = pass;
					ApplicationLauncher.logger.info("lscsLduManipulateCreepResultStatus: :result: " + result);
					//}
				}else {
					if(!actualPulseCount.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR)){
						result = fail;
					}
					//result = fail;
				}
			}
			//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsLduManipulateCreepResultStatus: Exception: " + e.getMessage());

		}

		//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
		//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
		return result;
	}

	public boolean lscsLDU_ReadErrorData(int LDU_ReadAddress){//(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData :Entry");
		ApplicationHomeController.update_left_status("Reading LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :lscsLDU_ReadErrorData:LDU_ReadAddress:"+LDU_ReadAddress);
		try{
			//LDU_SendCommandReadErrorData(LDU_ReadAddress);
			//SerialDataLDU lduData1 = new SerialDataLDU(commLDU);
			
			//ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData :getLDU_ReadSerialData: " + lduData1.getLDU_ReadSerialData());
			
			lscsLDU_SendCommandReadErrorData(LDU_ReadAddress);
			String expectedResponse = "";
			SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_ERROR_DATA_LENGTH,expectedResponse);
			String resultStatus= ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;//
			if(lduData.IsExpectedResponseReceived()){
				ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: Setting ldustatus to Pass");
				lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);

				//lduData.LDU_DecodeSerialData();
				lduData.lscsLDU_DecodeSerialData(LDU_ReadAddress);
				ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getLDU_ErrorValue: " + lduData.getLDU_ErrorValue(LDU_ReadAddress));
				resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
				lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
				StripLDU_SerialData(lduData.getReceivedLength());

				String testruntype = DeviceDataManagerController.getTestRunType();
				ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: testruntype: " + testruntype);
				ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getSkipCurrentTP_Execution: " + getSkipCurrentTP_Execution());

				//DeviceDataManagerController.setAverageNoOfLduReadingRequired(5);//averageLduFeature
				//int averageLduRequired = DeviceDataManagerController.getAverageNoOfLduReadingRequired();

				String test_type = ProjectExecutionController.getCurrentTestType();
				ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test_type: " + test_type);
				ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getStepRunFlag(): " + ProjectExecutionController.getStepRunFlag());
				if(!test_type.equals(TestProfileType.Warmup.toString()) &&
						!ProjectExecutionController.getStepRunFlag()){
					if( (testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) || testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED) )
							&& !getSkipCurrentTP_Execution()){
						ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test1: ");
						int averageLduRequired = DeviceDataManagerController.getAverageNoOfLduReadingRequired();
						String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
						ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: ErrorValue: "+ ErrorValue);
						if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
							ErrorValue = "WFR";
						}
						ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test2: ");
						int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
						ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: skip_reading: " + skip_reading);
						//averageLduFeature
						ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getReceivedReadingIndex1: " + lduData.getReceivedReadingIndex(LDU_ReadAddress));
						boolean invalidDataRecieved = false;
						if(ConstantAppConfig.LSC_LDU_INVALID_DATA_SKIP) {
							if((ErrorValue.equals(ConstantLduLscs.LDU_INVALID_READING))){
								invalidDataRecieved = true;
							}
						}
						if((!ErrorValue.equals("WFR")) && (!invalidDataRecieved)){
							ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test3: ");
							if(ErrorValue.length() > 1){ // sometimes LDU sends data 000000
								ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test3A: ");
								if(skip_reading <= lduData.getReceivedReadingIndex(LDU_ReadAddress)){
									ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test4: LDU_ReadAddress: " +LDU_ReadAddress);
									//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
									//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
									//averageLduFeature
									ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getLastReceivedReadingIndex: " + lduData.getLastReceivedReadingIndex(LDU_ReadAddress));
									//averageLduFeature
									ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getReceivedReadingIndex2: " + lduData.getReceivedReadingIndex(LDU_ReadAddress));
									//averageLduFeature
									//if( (lduData.getLastReceivedReadingIndex(LDU_ReadAddress) < lduData.getReceivedReadingIndex(LDU_ReadAddress)) || (lduData.getReceivedReadingIndex(LDU_ReadAddress)== 0 )){
									if( (lduData.getLastReceivedReadingIndex(LDU_ReadAddress) < lduData.getReceivedReadingIndex(LDU_ReadAddress)) ){

										ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test4A:");
										ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
										//averageLduFeature
										lduData.setLastReceivedReadingIndex(lduData.getReceivedReadingIndex(LDU_ReadAddress),LDU_ReadAddress);
										//averageLduFeature
										lduData.incrementNoOfReceivedReading(LDU_ReadAddress);
										//averageLduFeature
										DisplayDataObj.addLduErrorDataHashMap2d(LDU_ReadAddress,lduData.getReceivedReadingIndex(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
										//averageLduFeature
										ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: getNoOfReceivedReading: " + lduData.getNoOfReceivedReading(LDU_ReadAddress));
										//averageLduFeature
										ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: averageLduRequired: " + averageLduRequired);
										//averageLduFeature								
										if(averageLduRequired == lduData.getNoOfReceivedReading(LDU_ReadAddress)){
											//averageLduFeature
											ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test4B:");
											ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
											devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
											DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
											ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: devices_to_be_read:-p " + devices_to_be_read);
											ApplicationHomeController.updateBottomSecondaryStatus("LDU error read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

											//averageLduFeature
											try{
												/*											//averageLduFeature
												Float averageValue = 0.0f;
												Float SumReading = 0.0f;
												for(int i=0; i < averageLduRequired;i++){
													// ssdf   ssfdg   SumReading = SumReading + DisplayDataObj.getLduErrorDataList(i) ;fffff
													averageValue = SumReading/averageLduRequired;
												}*/
												/*if(lduData.getAverageReading(LDU_ReadAddress)== ConstantApp.LDU_AVERAGE_INIT_DATA){
													averageValue = Float.valueOf(lduData.getLDU_ErrorValue(LDU_ReadAddress));
												}else{
													averageValue = (Float.valueOf(lduData.getLDU_ErrorValue(LDU_ReadAddress))+lduData.getAverageReading(LDU_ReadAddress))/2;
												}*/
												//averageLduFeature
												String averageValueStr = DisplayDataObj.getAverageLduErrorDataHashMap2d(LDU_ReadAddress);
												//averageLduFeature
												if(averageValueStr!=null){
													//averageLduFeature
													ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: averageValueStr: " + averageValueStr);
													//averageLduFeature												
													lduData.setAverageReading(averageValueStr,LDU_ReadAddress);

													//averageLduFeature
													resultStatus = lscsLduManipulateErrorResultStatus(lduData.getAverageReading(LDU_ReadAddress));
													//averageLduFeature
													lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);//averageLduFeature
													//averageLduFeature
													ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getAverageReading(LDU_ReadAddress));

													//averageLduFeature
													ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getAverageReading(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
												}
												//averageLduFeature
											}catch(Exception E){
												//averageLduFeature
												E.printStackTrace();
												//averageLduFeature
												ApplicationLauncher.logger.error("lscsLDU_ReadErrorData Exception0 :"+E.getMessage());
											}


											if(DisplayDataObj.getDevicesToBeRead().size() == 0){
												ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: All Devices Completed-p " );
												//RunningStatusController.setExecuteTimeCounter(5);
												ProjectExecutionController.setExecuteTimeCounter(0);
												lduTimer.cancel();
											}
										}else{
											ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: Extending1 :-p  " );
											if(ProjectExecutionController.getExecuteTimeCounter()<30){
												int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
												ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
											}

										}
									}else{
										ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: Extending2 :-p  " );
										if(ProjectExecutionController.getExecuteTimeCounter()<30){
											int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
											ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
										}

									}
								} else{
									ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: ErrorValue:-p  "  + ErrorValue);
									if(ProjectExecutionController.getExecuteTimeCounter()<30){
										int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
										ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
									}
									ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: NoOfPulseReadingToBeSkipped -p : "  + skip_reading);
									//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
								}
							} else{
								ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: ErrorValue:-p2  "  + ErrorValue);
								if(ProjectExecutionController.getExecuteTimeCounter()<30){
									int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
									ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
								}
								ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: NoOfPulseReadingToBeSkipped -p2 : "  + skip_reading);
								//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
							}
						}
						else{
							if(ProjectExecutionController.getExecuteTimeCounter()<30){
								int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
								ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
							}
						}
					}
					else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
						//ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test2: ");
						String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
						/*						if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
							ErrorValue = "WFR";
						}*/
						if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
							ErrorValue = "WFR";
						}
						//ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test3: " + ErrorValue);
						if(!(ErrorValue.equals("WFR"))){
							//ApplicationLauncher.logger.debug("lscsLDU_ReadErrorData: test4: ");
							//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
							//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
							ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

							ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
							devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
							DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
							ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: devices_to_be_read-t:" + devices_to_be_read);
							if(DisplayDataObj.getDevicesToBeRead().size() == 0){
								ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: -t All Devices Completed" );
								//RunningStatusController.setExecuteTimeCounter(5);
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							}
							else{
								if(!isTimeExtendedForTimeBased()){
									ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: ErrorValue: -t  "  + ErrorValue);
									if(ProjectExecutionController.getExecuteTimeCounter()<30){
										int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) +
												TimeToBeExtendedInSec;
										ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
										ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: Time Extended:-t  " + extend_executetime);
										setTimeExtendedForTimeBased(true);
										//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
									}
								}
							}
						}
						else{
							ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: isTimeExtendedForTimeBased:-t  " + isTimeExtendedForTimeBased());
							ApplicationLauncher.logger.info("lscsLDU_ReadErrorData: getExecuteTimeCounter:-t  " + ProjectExecutionController.getExecuteTimeCounter());
							if(!isTimeExtendedForTimeBased()){
								if(ProjectExecutionController.getExecuteTimeCounter()<30){
									int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) + 
											TimeToBeExtendedInSec;
									ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
									setTimeExtendedForTimeBased(true);
								}
							}
						}
					}
				}else if(ProjectExecutionController.getStepRunFlag()){
					boolean invalidDataRecieved = false;
					
					if(!test_type.equals(TestProfileType.Warmup.toString())){
						if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) 
								&& !getSkipCurrentTP_Execution()){

							String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
							
							if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}
							if(ConstantAppConfig.LSC_LDU_INVALID_DATA_SKIP) {
								if((ErrorValue.equals(ConstantLduLscs.LDU_INVALID_READING))){
									invalidDataRecieved = true;
								}
							}
							if((!ErrorValue.equals("WFR")) && (!invalidDataRecieved)){
								//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
								//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
							}
						} else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){

							String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
							if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}
							if(ConstantAppConfig.LSC_LDU_INVALID_DATA_SKIP) {
								if((ErrorValue.equals(ConstantLduLscs.LDU_INVALID_READING))){
									invalidDataRecieved = true;
								}
							}
							if((!ErrorValue.equals("WFR")) && (!invalidDataRecieved)){
								//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
								//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
							}
						}
					}
				}

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("lscsLDU_ReadErrorData:No Data Received");
			}
		}catch(Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("lscsLDU_ReadErrorData Exception :"+E.getMessage());
		}
		return status;
	}

	public int getSkipReadingFromJSON(int rack_id, JSONObject skip_reading){
		ApplicationLauncher.logger.debug("getSkipReadingFromJSON : Entry :");
		int skip_count =0;
		try {
			skip_count = skip_reading.getInt(Integer.toString(rack_id));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getSkipReadingFromJSON : JSONException :"+e.getMessage());
		}
		return skip_count;
	}

	public boolean isTimeExtendedForTimeBased(){
		return TimeExtendedForTimeBased;
	}

	public void setTimeExtendedForTimeBased(boolean value){
		TimeExtendedForTimeBased = value;
	}

	public ArrayList<Integer> RemoveDeviceFromReadList (ArrayList<Integer> devices, int Address){
		ArrayList<Integer> read_devices = new ArrayList<Integer>();
		for(int i=0; i<devices.size(); i++){
			if(Address != devices.get(i)){
				read_devices.add(devices.get(i));
			}
		}
		return read_devices;
	}




	public boolean LDU_ReadCreepData(int LDU_ReadAddress){
		ApplicationHomeController.update_left_status("Reading LDU Data",ConstantApp.LEFT_STATUS_DEBUG);
		ApplicationLauncher.logger.debug("LDU_ReadCreepData :Entry");
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :LDU_ReadCreepData :LDU_ReadAddress:"+LDU_ReadAddress);

		LDU_SendCommandReadCreepData(LDU_ReadAddress);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_CREEP_DATA_LENGTH,ConstantLduCcube.CMD_LDU_CREEP_DATA_ER);
		if(lduData.IsExpectedResponseReceived()){

			lduData.LDU_DecodeSerialDataForCreepOrConst();
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());

			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(ErrorValue.equals("WFR"))){
				if(skip_reading <= 0){
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("LDU_ReadCreepData: devices_to_be_read:" + devices_to_be_read);
					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						ApplicationLauncher.logger.info("LDU_ReadCreepData: All Devices Completed" );
						ProjectExecutionController.setExecuteTimeCounter(0);
						lduTimer.cancel();
					}
				}
				else{
					ApplicationLauncher.logger.info("LDU_ReadCreepData: ErrorValue: "  + ErrorValue);
					ApplicationLauncher.logger.info("LDU_ReadCreepData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}
			/*			else{

			}*/

			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_ReadCreepData :No Data Received");
		}

		return status;


	}



	public boolean lscsLDU_ReadCreepData(int LDU_ReadAddress){
		ApplicationHomeController.update_left_status("Reading LDU Data",ConstantApp.LEFT_STATUS_DEBUG);
		ApplicationLauncher.logger.debug("lscsLDU_ReadCreepData :Entry");
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :lscsLDU_ReadCreepData :LDU_ReadAddress:"+LDU_ReadAddress);

		lscsLDU_SendCommandReadCreepData(LDU_ReadAddress);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_CREEP_DATA_LENGTH,"");
		String resultStatus= ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;//
		if(lduData.IsExpectedResponseReceived()){

			//lduData.LDU_DecodeSerialDataForCreepOrConst();
			//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
			resultStatus = lscsLduManipulateCreepResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));
			lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getNoOfPulsesCounted(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());

			//String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			String noOfPulsesCounted = lduData.getNoOfPulsesCounted(LDU_ReadAddress);
			if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				noOfPulsesCounted = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(noOfPulsesCounted.equals("WFR"))){
				if(skip_reading <= 0){
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					//resultStatus = lscsLduManipulateCreepResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("lscsLDU_ReadCreepData: devices_to_be_read:" + devices_to_be_read);
					ApplicationHomeController.updateBottomSecondaryStatus("LDU creep read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						ApplicationLauncher.logger.info("lscsLDU_ReadCreepData: All Devices Completed" );
						ProjectExecutionController.setExecuteTimeCounter(0);
						lduTimer.cancel();
					}
				}
				else{
					ApplicationLauncher.logger.info("lscsLDU_ReadCreepData: noOfPulsesCounted: "  + noOfPulsesCounted);
					ApplicationLauncher.logger.info("lscsLDU_ReadCreepData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}
			/*			else{

			}*/

			status=true;
		}
		else{
			ApplicationLauncher.logger.info("lscsLDU_ReadCreepData :No Data Received");
		}

		return status;


	}

	public boolean LDU_ReadConstData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("LDU_ReadConstData :Entry");

		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :LDU_ReadConstData :LDU_ReadAddress:"+LDU_ReadAddress);
		LDU_SendCommandReadConstData(LDU_ReadAddress);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_CREEP_DATA_LENGTH,ConstantLduCcube.CMD_LDU_CREEP_DATA_ER);
		if(lduData.IsExpectedResponseReceived()){


			lduData.LDU_DecodeSerialDataForCreepOrConst();

			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());

			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(ErrorValue.equals("WFR"))){
				if(skip_reading <= 0){
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("LDU_ReadConstData: devices_to_be_read:" + devices_to_be_read);
					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						ApplicationLauncher.logger.info("LDU_ReadConstData: All Devices Completed" );
						ProjectExecutionController.setExecuteTimeCounter(0);
						lduTimer.cancel();
					}
				}
				else{
					ApplicationLauncher.logger.info("LDU_ReadConstData: ErrorValue: "  + ErrorValue);

					ApplicationLauncher.logger.info("LDU_ReadConstData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}


			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_ReadConstData :No Data Received");
		}

		return status;


	}




	public boolean lscsLDU_ReadConstData(int LDU_ReadAddress){
		ApplicationLauncher.logger.debug("lscsLDU_ReadConstData :Entry");

		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :lscsLDU_ReadConstData :LDU_ReadAddress:"+LDU_ReadAddress);
		//LDU_SendCommandReadConstData(LDU_ReadAddress);
		lscsLDU_SendCommandReadConst_Data(LDU_ReadAddress);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_DIAL_DATA_LENGTH,"");
		String resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
		if(lduData.IsExpectedResponseReceived()){

			lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
			//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));mmb
			//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getNoOfPulsesCounted(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());

			/*			lduData.LDU_DecodeSerialDataForCreepOrConst();
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());*/

			/*			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}*/
			String noOfPulsesCounted = lduData.getNoOfPulsesCounted(LDU_ReadAddress);
			if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				noOfPulsesCounted = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(noOfPulsesCounted.equals("WFR"))){
				if(skip_reading <= 0){
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("lscsLDU_ReadConstData: devices_to_be_read:" + devices_to_be_read);
					ApplicationHomeController.updateBottomSecondaryStatus("LDU Const read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						ApplicationLauncher.logger.info("lscsLDU_ReadConstData: All Devices Completed" );
						ProjectExecutionController.setExecuteTimeCounter(0);
						try{
							lduTimer.cancel();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("lscsLDU_ReadConstData :Exception:"+ e.getMessage());

						}
					}
				}
				else{
					ApplicationLauncher.logger.info("lscsLDU_ReadConstData: noOfPulsesCounted: "  + noOfPulsesCounted);

					ApplicationLauncher.logger.info("lscsLDU_ReadConstData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}


			status=true;
		}
		else{
			ApplicationLauncher.logger.info("lscsLDU_ReadConstData :No Data Received");
		}

		return status;


	}

	public boolean LDU_ReadSTAData(int LDU_ReadAddress){
		ApplicationHomeController.update_left_status("Reading LDU STA Data",ConstantApp.LEFT_STATUS_DEBUG);
		ApplicationLauncher.logger.debug("LDU_ReadSTAData :Entry");
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :LDU_ReadSTAData :LDU_ReadAddress:"+LDU_ReadAddress);

		LDU_SendCommandReadErrorData(LDU_ReadAddress);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_STA_DATA_LENGTH,ConstantLduCcube.CMD_LDU_STA_DATA_ER);
		if(lduData.IsExpectedResponseReceived()){

			lduData.LDU_DecodeSerialDataForSTA();
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());


			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(ErrorValue.equals("WFR"))){
				if(skip_reading <= 0){
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("LDU_ReadSTAData: devices_to_be_read:" + devices_to_be_read);
					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						ApplicationLauncher.logger.info("LDU_ReadSTAData: All Devices Completed" );
						ProjectExecutionController.setExecuteTimeCounter(0);
						lduTimer.cancel();
					}
				}
				else{
					ApplicationLauncher.logger.info("LDU_ReadSTAData: ErrorValue: "  + ErrorValue);
					ApplicationLauncher.logger.info("LDU_ReadSTAData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}
			/*			else{
			}*/

			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_ReadSTAData : No Data Received");
		}

		return status;

	}

	public boolean lscsLDU_ReadSTA_Data(int LDU_ReadAddress){
		ApplicationHomeController.update_left_status("Reading LDU STA Data",ConstantApp.LEFT_STATUS_DEBUG);
		ApplicationLauncher.logger.debug("lscsLDU_ReadSTA_Data :Entry");
		boolean status=false;
		ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :lscsLDU_ReadSTA_Data :LDU_ReadAddress:"+LDU_ReadAddress);

		//LDU_SendCommandReadErrorData(LDU_ReadAddress);
		lscsLDU_SendCommandReadSTA_Data(LDU_ReadAddress);
		//SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_DATA_LENGTH,ConstantCcubeLDU.CMD_LDU_STA_DATA_ER);
		SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_STA_DATA_LENGTH,"");
		String resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;
		if(lduData.IsExpectedResponseReceived()){

			//lduData.LDU_DecodeSerialDataForSTA();

			lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
			resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));
			lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getNoOfPulsesCounted(LDU_ReadAddress));
			StripLDU_SerialData(lduData.getReceivedLength());


			/*			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
			if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
				ErrorValue = "WFR";
			}*/
			String noOfPulsesCounted = lduData.getNoOfPulsesCounted(LDU_ReadAddress);
			if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
				noOfPulsesCounted = "WFR";
			}
			int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
			if(!(noOfPulsesCounted.equals("WFR"))){
				if(skip_reading <= 0){
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
					//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
					ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);



					ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
					devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
					DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
					ApplicationLauncher.logger.info("lscsLDU_ReadSTA_Data: devices_to_be_read:" + devices_to_be_read);
					ApplicationHomeController.updateBottomSecondaryStatus("LDU STA read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

					if(DisplayDataObj.getDevicesToBeRead().size() == 0){
						try{
							ApplicationLauncher.logger.info("lscsLDU_ReadSTA_Data: All Devices Completed" );
							ProjectExecutionController.setExecuteTimeCounter(0);
							lduTimer.cancel();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("lscsLDU_ReadSTA_Data :Exception:"+ e.getMessage());

						}
					}
				}
				else{
					ApplicationLauncher.logger.info("lscsLDU_ReadSTA_Data: noOfPulsesCounted: "  + noOfPulsesCounted);
					ApplicationLauncher.logger.info("lscsLDU_ReadSTA_Data: NoOfPulseReadingToBeSkipped: "  + skip_reading);
					DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
				}
			}
			/*			else{
			}*/

			status=true;
		}
		else{
			ApplicationLauncher.logger.info("lscsLDU_ReadSTA_Data : No Data Received");
		}

		return status;

	}

	public boolean LDU_Init(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("LDU_Init :Entry");

		boolean status =false;
		try {
			lduComSerialStatusConnected = LDU_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_Init :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("LDU_Init : Exit");
		status=lduComSerialStatusConnected;

		return  status;

	}

	public boolean ictInit(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("ictInit :Entry");

		boolean status =false;
		try {
			ictComSerialStatusConnected = ict_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("ictInit :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("ictInit : Exit");
		status=ictComSerialStatusConnected;

		return  status;

	}

	public boolean harmonicsSrcInit(String CommPort_ID, String BaudRate) {
		ApplicationLauncher.logger.debug("harmonicsSrcInit :Entry");

		boolean status =false;
		try {
			harmonicsSrcComSerialStatusConnected = harmonicsSrc_CommInit(CommPort_ID, BaudRate);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("harmonicsSrcInit :UnsupportedEncodingException:"+ e.getMessage());

		}
		ApplicationLauncher.logger.info("harmonicsSrcInit : Exit");
		status=harmonicsSrcComSerialStatusConnected;

		return  status;

	}

	public boolean ict_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("ict_CommInit :Entry");
		ApplicationLauncher.logger.debug("ict_CommInit : ComPort_ID : "+ ComPort_ID);
		ApplicationLauncher.logger.debug("ict_CommInit : BaudRate : "+ BaudRate);
		//boolean status = SetSerialComm(commLDU,ComPort_ID,Integer.valueOf(BaudRate),true);
		boolean status = SetSerialCommEvenParity(commICT,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			commICT.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("ict_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	} 


	public boolean harmonicsSrc_CommInit(String ComPort_ID, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("harmonicsSrc_CommInit :Entry");
		ApplicationLauncher.logger.debug("harmonicsSrc_CommInit : ComPort_ID : "+ ComPort_ID);
		ApplicationLauncher.logger.debug("harmonicsSrc_CommInit : BaudRate : "+ BaudRate);
		boolean status = SetSerialComm(commHarmonicsSrc,ComPort_ID,Integer.valueOf(BaudRate),false);//SetSerialComm(commLDU,ComPort_ID,Integer.valueOf(BaudRate),true);
		//boolean status = SetSerialCommEvenParity(commHarmonicsSrc,ComPort_ID,Integer.valueOf(BaudRate),true);
		if (status){
			//boolean status = true;
			commHarmonicsSrc.SetFlowControlMode();//commPowerSrc.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("harmonicsSrc_CommInit:"+ComPort_ID+" access failed");
		}
		return status;

	}

	public boolean SetSerialCommEvenParity(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("SetSerialComm :Entry");
		boolean status=false;

		SerialPortObj.connect(SerialPort_ID);
		if(SerialPortObj.getDeviceConnected()==true){

			if (SerialPortObj.initIOStream() == true){
				SerialPortObj.SerialPortConfigEvenParity(BaudRate);
				//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
				ApplicationLauncher.logger.info("SetSerialCommEvenParity:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
				SerialPortObj.initListener();
				SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
				status = true;
				return status;
			}
		}
		return status;
	}

	public void DisconnectLDU(){
		ApplicationLauncher.logger.debug("DisconnectLDU :Entry");
		if(lduComSerialStatusConnected){
			DisconnectLDU_SerialComm();
			lduComSerialStatusConnected=false;
		}
	}

	public void DisconnectICT(){
		ApplicationLauncher.logger.debug("DisconnectLDU :Entry");
		if(ictComSerialStatusConnected){
			DisconnectICT_SerialComm();
			ictComSerialStatusConnected=false;
		}
	}


	public void DisConnectHarmonicsSrc(){
		ApplicationLauncher.logger.debug("DisConnectHarmonicsSrc :Entry");
		if(harmonicsSrcComSerialStatusConnected){
			DisconnectHarmonicsSrc_SerialComm();
			harmonicsSrcComSerialStatusConnected=false;
		}
	}

	public void DisconnectRefStd(){
		ApplicationLauncher.logger.debug("DisconnectRefStd :Entry");
		DisconnectRefSerialComm();
		if(refComSerialStatusConnected){

			refComSerialStatusConnected=false;
		}

	}

	public void ClearSerialDataInAllPorts(){

		ApplicationLauncher.logger.debug("sdm: ClearSerialDataInAllPorts :Entry");
		commPowerSrc.ClearSerialData();
		commLDU.ClearSerialData();
		SerialDataLDU.ClearLDU_ReadSerialData();
		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			commICT.ClearSerialData();
		}
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			commHarmonicsSrc.ClearSerialData();
		}
		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
			commHarmonicsSrc.ClearSerialData();
		}
		//lsLDU_ClearSerialData();
	}

	public boolean RefStd_ValidateNOP_CMD() throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStd_ValidateNOP_CMD :Entry");
		boolean status = false;
		RefStdCommSendCommand(ConstantRefStdRadiant.CMD_REF_STD_NOP_CMD,ConstantRefStdRadiant.CMD_REF_STD_NOP_EXPECTED_LENGTH);
		SerialDataRadiantRefStd refStdData =RefStdReadData();
		String CurrentReceivedData = refStdData.getRefStd_ReadSerialData();
		if (CurrentReceivedData.equals(ConstantRefStdRadiant.CMD_REF_STD_NOP_ER)){
			ApplicationLauncher.logger.info("RefStd_NOP_CMD: Expected Data Received");
			ApplicationLauncher.logger.debug("RefStd_NOP_CMD: CurrentReceivedData:"+CurrentReceivedData);

			StripRefStd_SerialData(ConstantRefStdRadiant.CMD_REF_STD_NOP_EXPECTED_LENGTH);
			status = true;
		}else{
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("RefStd_NOP_CMD: UnExpected Data Received: Expected:"+ConstantRefStdRadiant.CMD_REF_STD_NOP_ER+":Actual:"+CurrentReceivedData);
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			StripRefStd_SerialData(CurrentReceivedData.length());
		}

		return status;




	}


	public boolean mteRefStd_ValidateVersionCMD() throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("mteRefStd_ValidateVersionCMD :Entry");
		boolean status = false;
		status = mteRefStdCommSendCommand(ConstantRefStdMte.CMD_REF_STD_VERSION_HDR,ConstantRefStdMte.ER_REF_STD_VERSION_HDR);
		return status;

	}

	public boolean sandsRefStdAccuResetTask(){
		ApplicationLauncher.logger.debug("sandsRefStdAccuResetTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_ACCU_RESET;
		String expectedAckResponse = ConstantRefStdSands.ER_ACCU_RESET_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;

		status = sandsRefStdProcessCommand1Tier(command, expectedAckResponse, expectedErrorAckResponse);

		return status;

	}

	public boolean sandsRefStdAccuStartTask(){
		ApplicationLauncher.logger.debug("sandsRefStdAccuStartTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_ACCU_START;
		String expectedAckResponse = ConstantRefStdSands.ER_ACCU_START_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;

		status = sandsRefStdProcessCommand1Tier(command, expectedAckResponse, expectedErrorAckResponse);

		return status;

	}

	public boolean sandsRefStdAccuStopTask(){
		ApplicationLauncher.logger.debug("sandsRefStdAccuStopTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_ACCU_STOP;
		String expectedAckResponse = ConstantRefStdSands.ER_ACCU_STOP_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;

		status = sandsRefStdProcessCommand1Tier(command, expectedAckResponse, expectedErrorAckResponse);

		return status;

	}

	public boolean sandsRefStdGetConfigTask(){
		ApplicationLauncher.logger.debug("sandsRefStdGetConfigTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_GET_CONFIG;
		String expectedAckResponse = ConstantRefStdSands.ER_GET_CONFIG_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_GET_CONFIG_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_GET_CONFIG_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_GET_CONFIG_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){
			SerialDataSandsRefStd.parseReadModeData(getLastReadRefStdData());
		}
		return status;

	}


	public boolean kreRefStdReadSettingTask(){
		ApplicationLauncher.logger.debug("kreRefStdReadSettingTask :Entry");
		boolean status = false;
		String command = GuiUtils.hexToAscii(RefStdKreMessage.MSG_READ_SETTING);
		String expectedAckResponse = GuiUtils.hexToAscii(ConstantRefStdKre.ER_READ_SETTING_ACK);

		/*		String command = RefStdKreMessage.MSG_READ_BASIC_DATA;
		ApplicationLauncher.logger.debug("kreRefStdReadSettingTask : command: " + command);
		String expectedAckResponse = ConstantRefStdKre.ER_READ_BASIC_DATA_ACK;*/
		//status = kreRefStdCommSendCommand(command,expectedAckResponse);zxvcx
		//status = kreRefStdCommSendReadDataCommand(RefStdKreMessage.MSG_READ_BASIC_DATA,ConstantRefStdKre.ER_READ_BASIC_DATA_ACK);
		status = kreRefStdCommSendReadDataCommand(command,expectedAckResponse);
		if(status){
			SerialDataRefStdKre refStdData = kreRefStdReadSettingParameterData(expectedAckResponse,ConstantRefStdKre.ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT);
			if (refStdData!=null){
				ApplicationLauncher.logger.debug("kreRefStdReadSettingTask :Entry2" );
			}else{
				status = false;
			}
		}


		return status;

	}

	public boolean kreRefStdWriteSettingTask(){
		ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask :Entry");
		boolean status = false;
		String command =  GuiUtils.hexToAscii(Data_RefStdKre.getCommandWriteSettingParameter());//MSG_READ_SETTING;
		String expectedAckResponse = GuiUtils.hexToAscii( ConstantRefStdKre.ER_READ_SETTING_ACK);
		//ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask : command: " + GUIUtils.hexToAscii(command));
		/*		String command = RefStdKreMessage.MSG_READ_BASIC_DATA;
		ApplicationLauncher.logger.debug("kreRefStdReadSettingTask : command: " + command);
		String expectedAckResponse = ConstantRefStdKre.ER_READ_BASIC_DATA_ACK;*/
		//status = kreRefStdCommSendCommand(command,expectedAckResponse);zxvcx
		//status = kreRefStdCommSendReadDataCommand(RefStdKreMessage.MSG_READ_BASIC_DATA,ConstantRefStdKre.ER_READ_BASIC_DATA_ACK);
		status = kreRefStdCommSendWriteDataCommand(command,expectedAckResponse);
		if(status){
			SerialDataRefStdKre refStdData = kreRefStdReadSettingParameterData(expectedAckResponse,ConstantRefStdKre.ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT);
			if (refStdData!=null){

				//refStdData.parseSettingsSerialData(refStdData.getRefStd_ReadSerialData());
				status = true;
				ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask : status : " + status);
				//ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask :Entry2" : refStdData. );
			}else{
				status = false;
			}
		}


		return status;

	}

	public boolean kreRefStdAccumulationStartTask(){
		ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask :Entry");
		boolean status = false;
		String command = RefStdKreMessage.MSG_ENERGY_ACCU_START;//MSG_READ_SETTING;
		String expectedAckResponse = ConstantRefStdKre.ER_ENERGY_ACC_START_ACK;
		ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask : command: " + GuiUtils.hexToAscii(command));
		/*		String command = RefStdKreMessage.MSG_READ_BASIC_DATA;
		ApplicationLauncher.logger.debug("kreRefStdReadSettingTask : command: " + command);
		String expectedAckResponse = ConstantRefStdKre.ER_READ_BASIC_DATA_ACK;*/
		//status = kreRefStdCommSendCommand(command,expectedAckResponse);zxvcx
		//status = kreRefStdCommSendReadDataCommand(RefStdKreMessage.MSG_READ_BASIC_DATA,ConstantRefStdKre.ER_READ_BASIC_DATA_ACK);
		//status = kreRefStdCommSendWriteDataCommand(command,expectedAckResponse);
		status = kreRefStdCommSendCommand(command,expectedAckResponse);
		if(status){
			//SerialDataRefStdKre refStdData = kreRefStdReadSettingParameterData(ConstantRefStdKre.ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT);
			//if (refStdData!=null){
			ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask :Success" );
			//}else{
			//	status = false;
			//}
		}


		return status;

	}


	public boolean kiggsRefStdAccumulationStartTask(){
		ApplicationLauncher.logger.debug("kiggsRefStdAccumulationStartTask :Entry");
		boolean status = false;
		String command = RefStdKiggsMessage.MSG_ACCU_CONTROL_START;//MSG_READ_SETTING;
		String dummyExpectedAckResponse = "";//ConstantRefStdKre.ER_ENERGY_ACC_START_ACK;
		int dummyExpectedlength = 10;
		//ApplicationLauncher.logger.debug("kiggsRefStdAccumulationStartTask : command: " + GUIUtils.hexToAscii(command));

		status = kiggsRefStdCommSendDataCommand(command,dummyExpectedAckResponse,dummyExpectedlength);
		if(status){

			ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask :Success" );

		}


		return status;

	}

	public boolean sandsRefStdSetConfigTask(){
		ApplicationLauncher.logger.debug("sandsRefStdSetConfigTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_SET_CONFIG;
		String expectedAckResponse = ConstantRefStdSands.ER_SET_CONFIG_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured1 = ConstantRefStdSands.CMD_SET_CONFIG_SEC; 
		String expectedSecAckResponse1 = ConstantRefStdSands.ER_SET_CONFIG_SEC_ACK;
		String commandSecured2 = RefStdSandsMessage.getMsgSetConfigData();
		String expectedDataResponse = ConstantRefStdSands.ER_SET_CONFIG_TERT_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_SET_CONFIG_TERT_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		//status = sandsRefStdProcessCommand(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		status = sandsRefStdProcessCommand3Tier(command,expectedAckResponse,expectedErrorAckResponse, 
				commandSecured1 , expectedSecAckResponse1,
				commandSecured2 , expectedDataResponse,expectedDataResponseLength, expectedDataErrorResponse);

		if(status){

			DisplayDataObj.setSandsRefStdLastSetVoltageMode(Data_RefStdSands.getWriteConfigModeVoltage());
			DisplayDataObj.setSandsRefStdLastSetCurrentMode(Data_RefStdSands.getWriteConfigModeCurrent());
			DisplayDataObj.setSandsRefStdLastSetPulseOutputMode(Data_RefStdSands.getWriteConfigModePulseOutputUnit());

		}
		return status;

	}

	/*	public boolean refStdKreSetConfigTask(){
		ApplicationLauncher.logger.debug("refStdKreSetConfigTask :Entry");
		boolean status = false;

		String command = RefStdSandsMessage.MSG_SET_CONFIG;
		String expectedAckResponse = ConstantSandsRefStd.ER_SET_CONFIG_HDR_ACK;
		String expectedErrorAckResponse = ConstantSandsRefStd.ER_ERROR_DATA;
		String commandSecured1 = ConstantSandsRefStd.CMD_SET_CONFIG_SEC; 
		String expectedSecAckResponse1 = ConstantSandsRefStd.ER_SET_CONFIG_SEC_ACK;
		String commandSecured2 = RefStdSandsMessage.getMsgSetConfigData();
		String expectedDataResponse = ConstantSandsRefStd.ER_SET_CONFIG_TERT_ACK;
		int expectedDataResponseLength = ConstantSandsRefStd.ER_SET_CONFIG_TERT_LENGTH; 
		String expectedDataErrorResponse = ConstantSandsRefStd.ER_ERROR_DATA;
		//status = sandsRefStdProcessCommand(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		status = sandsRefStdProcessCommand3Tier(command,expectedAckResponse,expectedErrorAckResponse, 
												commandSecured1 , expectedSecAckResponse1,
												commandSecured2 , expectedDataResponse,expectedDataResponseLength, expectedDataErrorResponse);

		if(status){

			DisplayDataObj.setSandsRefStdLastSetVoltageMode(Data_RefStdSands.getWriteConfigModeVoltage());
			DisplayDataObj.setSandsRefStdLastSetCurrentMode(Data_RefStdSands.getWriteConfigModeCurrent());
			DisplayDataObj.setSandsRefStdLastSetPulseOutputMode(Data_RefStdSands.getWriteConfigModePulseOutputUnit());

		}
		return status;

	}*/

	/*	public boolean sandsRefStdCommSendCommand(String commandData,String expectedResponseData){
		ApplicationLauncher.logger.debug("sandsRefStdCommSendCommand :Entry");
		boolean status= false;
		Communicator SerialPortObj =commRefStandard;
		if(!getSkipCurrentTP_Execution()){
			WriteToSerialCommRef(commandData);
			SerialPortObj.setExpectedResult(expectedResponseData);knjnj
			SerialDataMteRefStd pwerData = new SerialDataMteRefStd(SerialPortObj);
			pwerData.SerialReponseTimerStart(20);
			status = pwerData.IsExpectedResponseReceived();

			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("sandsRefStdCommSendCommand: Command response failed:");
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code XX2: RefStd: Command response failed");
					///Gopi
					//setSkipCurrentTP_Execution(true);
				}
			}
			pwerData = null; //garbagecollector
		}
		SerialPortObj = null;//garbagecollector

		return status;
	}*/

	public boolean sandsRefStdProcessCommand1Tier(String command,String expectedAckResponse,String expectedErrorAckResponse){
		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand1Tier :Entry");
		boolean status=false;
		//Ref_SendCommandBNC_OutPut();xzvcx
		//WriteToSerialCommRef(command);
		//ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Test1");
		//String expectedResult = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER;
		clearLastReadRefStdData();
		Communicator SerialPortObj = commRefStandard;

		SerialPortObj.setExpectedLength(expectedAckResponse.length());
		SerialPortObj.setExpectedResult(expectedAckResponse);
		SerialPortObj.setExpectedDataErrorResult(expectedErrorAckResponse);
		WriteToSerialCommRef(command);
		SerialDataSandsRefStd refStdSecConnection = new SerialDataSandsRefStd(SerialPortObj);

		refStdSecConnection.SerialReponseTimerStart(20);
		//SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResponse);

		if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand1Tier :RefStd ack Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			setLastReadRefStdData(CurrentRef_Data);
			setLastSandsRefStdObj(refStdSecConnection);
			status=true;
		}else if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand1Tier :RefStd error ack response Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7799: RefStd: Command ack response failed");
			status=false;
		}else{
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand1Tier :No ack Data Received");
			status=false;
		}

		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand1Tier :Exit");
		return status;
	}

	public boolean sandsRefStdProcessCommand2Tier(String command,String expectedAckResponse,String expectedErrorAckResponse, String commandSecured , String expectedDataResponse,int expectedDataResponseLength, String expectedDataErrorResponse){
		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand2Tier :Entry");
		boolean status=false;
		//Ref_SendCommandBNC_OutPut();xzvcx
		//WriteToSerialCommRef(command);
		//ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Test1");
		//String expectedResult = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER;
		clearLastReadRefStdData();
		Communicator SerialPortObj = commRefStandard;

		SerialPortObj.setExpectedLength(expectedAckResponse.length());
		SerialPortObj.setExpectedResult(expectedAckResponse);
		SerialPortObj.setExpectedDataErrorResult(expectedErrorAckResponse);
		WriteToSerialCommRef(command);
		SerialDataSandsRefStd refStdSecConnection = new SerialDataSandsRefStd(SerialPortObj);

		refStdSecConnection.SerialReponseTimerStart(20);
		//SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResponse);

		if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :RefStd ack Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			status=true;
		}else if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :RefStd error ack response Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7800: RefStd: Command ack response failed");
			status=false;
		}else{
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :No ack Data Received");
			status=false;
		}

		if(status){
			SerialPortObj.setExpectedLength(expectedDataResponseLength);
			SerialPortObj.setExpectedResult(expectedDataResponse);
			SerialPortObj.setExpectedDataErrorResult(expectedDataErrorResponse);
			WriteToSerialCommRef(commandSecured);

			SerialDataSandsRefStd refStdData = new SerialDataSandsRefStd(SerialPortObj);

			refStdData.SerialReponseTimerStart(20);
			if(refStdData.IsExpectedResponseReceived()){

				String CurrentRef_Data =refStdData.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :RefStd Data Received Data:"+CurrentRef_Data);
				setLastReadRefStdData(CurrentRef_Data);
				StripRefStd_SerialData(refStdData.getReceivedLength());
				status=true;
				setLastSandsRefStdObj(refStdData);
			}else if(refStdData.IsExpectedResponseReceived()){

				String CurrentRef_Data =refStdData.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :RefStd error data response Received Data:"+CurrentRef_Data);
				StripRefStd_SerialData(refStdData.getReceivedLength());
				FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7801: RefStd: Command data response failed");
				status=false;
			}else{
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand2Tier :No Data Received");
				status=false;
			}
		}
		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand2Tier :Exit");
		return status;
	}

	public boolean sandsRefStdProcessCommand3Tier(String command,String expectedAckResponse,String expectedErrorAckResponse, String commandSecured1 , String expectedSecAckResponse1,String commandSecured2 , String expectedDataResponse,int expectedDataResponseLength, String expectedDataErrorResponse){
		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand3Tier :Entry");
		boolean status=false;
		//Ref_SendCommandBNC_OutPut();xzvcx
		//WriteToSerialCommRef(command);
		//ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Test1");
		//String expectedResult = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER;
		clearLastReadRefStdData();
		Communicator SerialPortObj = commRefStandard;

		SerialPortObj.setExpectedLength(expectedAckResponse.length());
		SerialPortObj.setExpectedResult(expectedAckResponse);
		SerialPortObj.setExpectedDataErrorResult(expectedErrorAckResponse);
		WriteToSerialCommRef(command);
		SerialDataSandsRefStd refStdSecConnection = new SerialDataSandsRefStd(SerialPortObj);

		refStdSecConnection.SerialReponseTimerStart(20);
		//SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResponse);

		if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd ack Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			status=true;
		}else if(refStdSecConnection.IsExpectedResponseReceived()){

			String CurrentRef_Data =refStdSecConnection.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd error ack response Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refStdSecConnection.getReceivedLength());
			FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7802: RefStd: Command ack response failed");
			status=false;
		}else{
			ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :No ack Data Received");
			status=false;
		}
		if(status){

			SerialPortObj.setExpectedLength(expectedSecAckResponse1.length());
			SerialPortObj.setExpectedResult(expectedSecAckResponse1);
			SerialPortObj.setExpectedDataErrorResult(expectedErrorAckResponse);
			WriteToSerialCommRef(commandSecured1);
			SerialDataSandsRefStd refStdSecConnect2 = new SerialDataSandsRefStd(SerialPortObj);

			refStdSecConnect2.SerialReponseTimerStart(20);
			//SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResponse);

			if(refStdSecConnect2.IsExpectedResponseReceived()){

				String CurrentRef_Data =refStdSecConnect2.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd ack Received Data:"+CurrentRef_Data);
				StripRefStd_SerialData(refStdSecConnect2.getReceivedLength());
				status=true;
			}else if(refStdSecConnect2.IsExpectedResponseReceived()){

				String CurrentRef_Data =refStdSecConnect2.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd error ack response Received Data:"+CurrentRef_Data);
				StripRefStd_SerialData(refStdSecConnect2.getReceivedLength());
				FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7803: RefStd: Command ack response failed");
				status=false;
			}else{
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :No ack Data Received");
				status=false;
			}



			if(status){
				SerialPortObj.setExpectedLength(expectedDataResponseLength);
				SerialPortObj.setExpectedResult(expectedDataResponse);
				SerialPortObj.setExpectedDataErrorResult(expectedDataErrorResponse);
				ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier : commandSecured2 :"+commandSecured2);
				WriteToSerialCommRef(commandSecured2);

				SerialDataSandsRefStd refStdData = new SerialDataSandsRefStd(SerialPortObj);

				refStdData.SerialReponseTimerStart(20);
				if(refStdData.IsExpectedResponseReceived()){

					String CurrentRef_Data =refStdData.getRefStd_ReadSerialData();
					ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd Data Received Data:"+CurrentRef_Data);
					setLastReadRefStdData(CurrentRef_Data);
					StripRefStd_SerialData(refStdData.getReceivedLength());
					status=true;
					setLastSandsRefStdObj(refStdData);
				}else if(refStdData.IsExpectedResponseReceived()){

					String CurrentRef_Data =refStdData.getRefStd_ReadSerialData();
					ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :RefStd error data response Received Data:"+CurrentRef_Data);
					StripRefStd_SerialData(refStdData.getReceivedLength());
					FailureManager.AppendRefStdFeedBackReasonForFailure("Error Code 7804: RefStd: Command data response failed");
					status=false;
				}else{
					ApplicationLauncher.logger.info("sandsRefStdProcessCommand3Tier :No Data Received");
					status=false;
				}
			}
		}
		ApplicationLauncher.logger.debug("sandsRefStdProcessCommand3Tier :Exit");
		return status;
	}





	public void ReadRefStdData() {

		ApplicationLauncher.logger.debug("ReadRefStdData:Entry:");
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			RefStdCommSendCommand(ConstantRefStdRadiant.CMD_REF_STD_READ_R_PHASE,ConstantRefStdRadiant.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			SerialDataRadiantRefStd refStdDataR_Phase = RefStdReadPhaseData();
			if (refStdDataR_Phase!=null){
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_R_Phase(refStdDataR_Phase);
				}
				ProjectExecutionController.refStdDataDisplayUpdate_R_Phase(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("R Phase Volt:"+refStdDataR_Phase.VoltageDisplayData);
			}
			ApplicationLauncher.logger.debug("ReadRefStdData: Test1:");
			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}
			ApplicationLauncher.logger.debug("ReadRefStdData: Test2:");
			refStdDataR_Phase=null; //garbagecollector
			ApplicationLauncher.logger.debug("ReadRefStdData: Test3:");
		}
		ApplicationLauncher.logger.debug("ReadRefStdData: Test4:");
		if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
			ApplicationLauncher.logger.debug("ReadRefStdData: Test5:");
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();

				SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
				RefStdCommSendCommand(ConstantRefStdRadiant.CMD_REF_STD_READ_Y_PHASE,ConstantRefStdRadiant.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				SerialDataRadiantRefStd refStdDataY_Phase =RefStdReadPhaseData();
				if (refStdDataY_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_Y_Phase(refStdDataY_Phase);
					ApplicationLauncher.logger.debug("Y Phase Volt:"+refStdDataY_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataY_Phase =null; //garbagecollector
			}
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();
				//SerialDataRefStd.setRefStd_ReadSerialData("");
				SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
				RefStdCommSendCommand(ConstantRefStdRadiant.CMD_REF_STD_READ_B_PHASE,ConstantRefStdRadiant.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				SerialDataRadiantRefStd refStdDataB_Phase =RefStdReadPhaseData();
				if (refStdDataB_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_B_Phase(refStdDataB_Phase);
					ApplicationLauncher.logger.debug("B Phase Volt:"+refStdDataB_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataB_Phase = null; //garbagecollector
			}
		}

		//ApplicationLauncher.logger.debug("ReadRefStdData: Test5:");
		ApplicationLauncher.logger.debug("ReadRefStdData:Exit");



	}



	public void sandsReadRefStdData() {

		ApplicationLauncher.logger.debug("sandsReadRefStdData:Entry:");
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
			sandsRefStdGetR_PhaseData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			//SerialDataRadiantRefStd refStdDataR_Phase =RefStdReadPhaseData();gfcfgc

			SerialDataSandsRefStd refStdDataR_Phase = getLastSandsRefStdObj();
			if (refStdDataR_Phase!=null){
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_R_Phase(refStdDataR_Phase);
				}
				//ProjectExecutionController.refStdDataDisplayUpdate_R_Phase(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("R Phase Volt:"+refStdDataR_Phase.VoltageDisplayData);
			}
			//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test1:");
			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}
			//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test2:");
			refStdDataR_Phase=null; //garbagecollector
			//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test3:");
		}
		//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test4:");
		if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
			//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test5:");
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();

				SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
				sandsRefStdGetY_PhaseData();
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_Y_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				//SerialDataRadiantRefStd refStdDataY_Phase =RefStdReadPhaseData();
				SerialDataSandsRefStd refStdDataY_Phase = getLastSandsRefStdObj();
				if (refStdDataY_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_Y_Phase(refStdDataY_Phase);
					ApplicationLauncher.logger.debug("Y Phase Volt:"+refStdDataY_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataY_Phase =null; //garbagecollector
			}
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();
				//SerialDataRefStd.setRefStd_ReadSerialData("");
				SerialDataSandsRefStd.ClearRefStd_ReadSerialData();  
				sandsRefStdGetB_PhaseData();
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_B_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				//SerialDataRadiantRefStd refStdDataB_Phase =RefStdReadPhaseData();
				SerialDataSandsRefStd refStdDataB_Phase = getLastSandsRefStdObj();
				if (refStdDataB_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_B_Phase(refStdDataB_Phase);
					ApplicationLauncher.logger.debug("B Phase Volt:"+refStdDataB_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataB_Phase = null; //garbagecollector
			}

			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();
				//SerialDataRefStd.setRefStd_ReadSerialData("");
				SerialDataSandsRefStd.ClearRefStd_ReadSerialData();  
				sandsRefStdGetTotalPowerInstantAngleData();
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_B_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				//SerialDataRadiantRefStd refStdDataB_Phase =RefStdReadPhaseData();
				SerialDataSandsRefStd refStdDataDegreePhaseAngle = getLastSandsRefStdObj();
				if (refStdDataDegreePhaseAngle!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhaseAngle(refStdDataDegreePhaseAngle);
					ApplicationLauncher.logger.debug("R Phase Degree:"+refStdDataDegreePhaseAngle.rPhaseDegreePhaseData);
					ApplicationLauncher.logger.debug("Y Phase Degree:"+refStdDataDegreePhaseAngle.yPhaseDegreePhaseData);
					ApplicationLauncher.logger.debug("B Phase Degree:"+refStdDataDegreePhaseAngle.bPhaseDegreePhaseData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataDegreePhaseAngle = null; //garbagecollector
			}


		}else{
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();
				//SerialDataRefStd.setRefStd_ReadSerialData("");
				SerialDataSandsRefStd.ClearRefStd_ReadSerialData();  
				sandsRefStdGetTotalPowerInstantAngleData();
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_B_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				//SerialDataRadiantRefStd refStdDataB_Phase =RefStdReadPhaseData();
				SerialDataSandsRefStd refStdDataDegreePhaseAngle = getLastSandsRefStdObj();
				if (refStdDataDegreePhaseAngle!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateR_PhaseDegreePhaseAngle(refStdDataDegreePhaseAngle);
					ApplicationLauncher.logger.debug("R Phase Degree2:"+refStdDataDegreePhaseAngle.rPhaseDegreePhaseData);

				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataDegreePhaseAngle = null; //garbagecollector
			}
		}

		//ApplicationLauncher.logger.debug("sandsReadRefStdData: Test5:");
		ApplicationLauncher.logger.debug("sandsReadRefStdData:Exit");



	}



	public boolean sandsRefStdGetR_PhaseData(){
		ApplicationLauncher.logger.debug("sandsRefStdGetR_PhaseData: Entry:");
		boolean status = false;
		setLastSandsRefStdObj(null);
		String command = RefStdSandsMessage.MSG_READ_R_PHASE;
		String expectedAckResponse = ConstantRefStdSands.ER_READ_R_PHASE_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_READ_R_PHASE_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_READ_R_PHASE_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_READ_R_PHASE_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){

			String lastReadPayLoad = getLastReadRefStdData();
			if(!lastReadPayLoad.isEmpty()){
				SerialDataSandsRefStd refStdDataR_Phase = getLastSandsRefStdObj();
				refStdDataR_Phase.ParseVoltageDatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParseCurrentDatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParsePowerFactorDatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParseFreqDatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParseWattDatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParseVA_DatafromRefStd(lastReadPayLoad);
				refStdDataR_Phase.ParseVAR_DatafromRefStd(lastReadPayLoad);
				setLastSandsRefStdObj(refStdDataR_Phase);
			}else{
				status = false;
				ApplicationLauncher.logger.debug("sandsRefStdGetR_PhaseData: lastReadPayLoad Empty:");
			}

		}
		return status;

	}

	public boolean sandsRefStdGetY_PhaseData(){
		ApplicationLauncher.logger.debug("sandsRefStdGetY_PhaseData: Entry:");
		boolean status = false;
		setLastSandsRefStdObj(null);
		String command = RefStdSandsMessage.MSG_READ_Y_PHASE;
		String expectedAckResponse = ConstantRefStdSands.ER_READ_Y_PHASE_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_READ_Y_PHASE_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_READ_Y_PHASE_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_READ_Y_PHASE_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){

			String lastReadPayLoad = getLastReadRefStdData();
			if(!lastReadPayLoad.isEmpty()){
				SerialDataSandsRefStd refStdDataY_Phase = getLastSandsRefStdObj();
				refStdDataY_Phase.ParseVoltageDatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParseCurrentDatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParsePowerFactorDatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParseFreqDatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParseWattDatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParseVA_DatafromRefStd(lastReadPayLoad);
				refStdDataY_Phase.ParseVAR_DatafromRefStd(lastReadPayLoad);
				setLastSandsRefStdObj(refStdDataY_Phase);
			}else{
				status = false;
				ApplicationLauncher.logger.debug("sandsRefStdGetY_PhaseData: lastReadPayLoad Empty:");
			}

		}

		return status;

	}

	public boolean sandsRefStdGetB_PhaseData(){
		ApplicationLauncher.logger.debug("sandsRefStdGetB_PhaseData: Entry:");
		boolean status = false;
		setLastSandsRefStdObj(null);
		String command = RefStdSandsMessage.MSG_READ_B_PHASE;
		String expectedAckResponse = ConstantRefStdSands.ER_READ_B_PHASE_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_READ_B_PHASE_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_READ_B_PHASE_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_READ_B_PHASE_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){

			String lastReadPayLoad = getLastReadRefStdData();
			if(!lastReadPayLoad.isEmpty()){
				SerialDataSandsRefStd refStdDataB_Phase = getLastSandsRefStdObj();
				refStdDataB_Phase.ParseVoltageDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseCurrentDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParsePowerFactorDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseFreqDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseWattDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseVA_DatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseVAR_DatafromRefStd(lastReadPayLoad);
				setLastSandsRefStdObj(refStdDataB_Phase);
			}else{
				status = false;
				ApplicationLauncher.logger.debug("sandsRefStdGetB_PhaseData: lastReadPayLoad Empty:");
			}

		}
		return status;

	}



	public boolean sandsRefStdReadEnergyData(){
		ApplicationLauncher.logger.debug("sandsRefStdReadEnergyData: Entry:");
		boolean status = false;
		setLastSandsRefStdObj(null);
		String command = RefStdSandsMessage.MSG_READ_ACCU_ENERGY;
		String expectedAckResponse = ConstantRefStdSands.ER_READ_ACCU_ENERGY_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_READ_ACCU_ENERGY_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_READ_ACCU_ENERGY_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_READ_ACCU_ENERGY_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){

			String lastReadPayLoad = getLastReadRefStdData();
			if(!lastReadPayLoad.isEmpty()){
				SerialDataSandsRefStd refStdDataAccuEnergy = getLastSandsRefStdObj();
				refStdDataAccuEnergy.RefStd_DecodeSerialDataForConstTest(lastReadPayLoad);
				setLastSandsRefStdObj(refStdDataAccuEnergy);
			}else{
				status = false;
				ApplicationLauncher.logger.debug("sandsRefStdReadEnergyData: lastReadPayLoad Empty:");
			}

		}
		return status;

	}



	public boolean sandsRefStdGetTotalPowerInstantAngleData(){
		ApplicationLauncher.logger.debug("sandsRefStdGetTotalPowerInstantAngleData: Entry:");
		boolean status = false;
		setLastSandsRefStdObj(null);
		String command = RefStdSandsMessage.MSG_READ_TOTAL_POWER_INSTANT_ANGLE;
		String expectedAckResponse = ConstantRefStdSands.ER_TOTAL_POWER_INSTANT_ANGLE_HDR_ACK;
		String expectedErrorAckResponse = ConstantRefStdSands.ER_ERROR_DATA;
		String commandSecured = ConstantRefStdSands.CMD_TOTAL_POWER_INSTANT_ANGLE_SEC; 
		String expectedDataResponse = ConstantRefStdSands.ER_TOTAL_POWER_INSTANT_ANGLE_SEC_ACK;
		int expectedDataResponseLength = ConstantRefStdSands.ER_TOTAL_POWER_INSTANT_ANGLE_SEC_LENGTH; 
		String expectedDataErrorResponse = ConstantRefStdSands.ER_ERROR_DATA;
		status = sandsRefStdProcessCommand2Tier(command, expectedAckResponse, expectedErrorAckResponse,  commandSecured ,  expectedDataResponse, expectedDataResponseLength, expectedDataErrorResponse);
		if(status){

			String lastReadPayLoad = getLastReadRefStdData();
			if(!lastReadPayLoad.isEmpty()){
				SerialDataSandsRefStd refStdDataB_Phase = getLastSandsRefStdObj();
				refStdDataB_Phase.ParseDegreeR_PhaseDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseDegreeY_PhaseDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseDegreeB_PhaseDatafromRefStd(lastReadPayLoad);
				/*				refStdDataB_Phase.ParseFreqDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseWattDatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseVA_DatafromRefStd(lastReadPayLoad);
				refStdDataB_Phase.ParseVAR_DatafromRefStd(lastReadPayLoad);*/
				setLastSandsRefStdObj(refStdDataB_Phase);
			}else{
				status = false;
				ApplicationLauncher.logger.debug("sandsRefStdGetTotalPowerInstantAngleData: lastReadPayLoad Empty:");
			}

		}
		return status;

	}

	public void mteReadRefStdAllData() {

		ApplicationLauncher.logger.debug("mteReadRefStdAllData: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_ALL_PARAM,ConstantRefStdMte.ER_REF_STD_READ_ALL_PARAM);

			SerialDataMteRefStd refStdData = mteRefStdReadAllParameterData(ConstantRefStdMte.ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT);
			if (refStdData!=null){

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdData);
				ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdData);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdData.rPhaseVoltageDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(refStdData);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+refStdData.rPhaseDegreePhaseData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdData);
				ApplicationLauncher.logger.debug("freq data update:"+refStdData.rPhaseFreqDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdData);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdData.rPhasePowerFactorData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateWattData(refStdData);
				ApplicationLauncher.logger.debug("ActivePower data update:"+refStdData.rPhaseWattDisplayData);
				/*				ApplicationLauncher.logger.debug("mteReadRefStdData : isRefStdLogResultsEnabled : "+ DeviceDataManagerController.isRefStdLogResultsEnabled());
				if(DeviceDataManagerController.isRefStdLogResultsEnabled()) {
					ApplicationLauncher.logger.debug("mteReadRefStdData : AddingData: ");
					try {
						//DeviceDataManagerController.addRefStdInstantActivePowerResult(Float.parseFloat(refStdData.rPhaseWattDisplayData));
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test1: ");
						String timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
						long currentTimeEpoch = GUIUtils.calcEpoch(timeStampFormat);
						//refStdData.getRefStdDataRphase().setAppDutTimeStampEpoch(String.valueOf(currentTimeEpoch));
						DataLogParse rPhaseRefStdResult = new DataLogParse ();
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test2: ");
						rPhaseRefStdResult.setAppRefStdTimeStampEpoch(String.valueOf(currentTimeEpoch));
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test3: ");
						//rPhaseRefStdResult.setRefStdTimeStampEpoch(String.valueOf(currentTimeEpoch));
						rPhaseRefStdResult.setActivePowerWatt(refStdData.rPhaseWattDisplayData);
						//rPhaseRefStdResult.setApparentPowerVA(refStdData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test4: ");
						rPhaseRefStdResult.setCurrent(refStdData.rPhaseCurrentDisplayData);
						rPhaseRefStdResult.setDegreePhaseAngle(refStdData.rPhaseDegreePhaseData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData: rPhaseDegreePhaseData: "+refStdData.rPhaseDegreePhaseData);
						//rPhaseRefStdResult.setEnergyMetric(energyMetric);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test5: ");
						rPhaseRefStdResult.setFreqency(refStdData.rPhaseFreqDisplayData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test6: ");
						rPhaseRefStdResult.setPf(refStdData.rPhasePowerFactorData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test7: ");
						rPhaseRefStdResult.setPhaseLabel("R");
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test8: ");
						rPhaseRefStdResult.setVoltage(refStdData.rPhaseVoltageDisplayData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData: rPhaseDegreePhaseData 2: "+refStdData.rPhaseDegreePhaseData);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test9: ");
						DisplayDataObj.addRefStdResult(rPhaseRefStdResult);
						//ApplicationLauncher.logger.debug("mteReadRefStdData : Test10: ");
						//DeviceDataManagerController.refStdResult.add(arg0)hbmn
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("mteReadRefStdData :Exception: "+e.getMessage() );
					}
				}*/


			}

			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}


		}
		ApplicationLauncher.logger.debug("mteReadRefStdAllData: Exit");
	}

	public void  lscsPowerSourceReadFeedBackAllData () {

		ApplicationLauncher.logger.debug("lscsPowerSourceReadFeedBackAllData: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.isPowerSrcReadFeedbackData() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearPowerSourceSerialData();
			//SerialDataPowerSrc.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			lscsPowerSourceCommSendReadFeedBackDataCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_READ_FEEDBACK_ALL_DATA_HDR,ConstantPowerSourceLscs.ER_PWR_SRC_READ_ALL_HEADER);
			int terminatorCount = ConstantPowerSourceLscs.ER_PWR_SRC_READ_FEEDBACK_ALL_PARAM_3PHASE_TERMINATOR_COUNT;
			if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
				terminatorCount = ConstantPowerSourceLscs.ER_PWR_SRC_READ_FEEDBACK_ALL_PARAM_1PHASE_TERMINATOR_COUNT;
			}
			Data_PowerSourceFeedBack powerSrcFeedBackData = powerSourceReadFeedBackAllParameterData(terminatorCount);//ConstantPowerSourceLscs.ER_PWR_SRC_READ_FEEDBACK_ALL_PARAM_3PHASE_TERMINATOR_COUNT);
			if (powerSrcFeedBackData!=null){

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){

					DisplayInstantMetricsController.devicePowerSourceFeedBackDataDisplayUpdate(powerSrcFeedBackData);

				}
				
				if(ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED){
					ProjectExecutionController.refStdDataDisplayUpdate_R_Phase(powerSrcFeedBackData);
				}
				/*					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(powerSrcFeedBackData);

				ProjectExecutionController.refStdDataDisplayUpdateCurrentData(powerSrcFeedBackData);
				//ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				//if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(powerSrcFeedBackData);
				//}
				ProjectExecutionController.refStdDataDisplayUpdateVoltageData(powerSrcFeedBackData);
				ApplicationLauncher.logger.debug("Volt data update:"+powerSrcFeedBackData.rPhaseVoltageDisplayData);

				//if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(powerSrcFeedBackData);
				//}
				ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(powerSrcFeedBackData);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+powerSrcFeedBackData.rPhaseDegreePhaseData);

				//if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(powerSrcFeedBackData);
				//}
				ProjectExecutionController.refStdDataDisplayUpdateFreqData(powerSrcFeedBackData);
				ApplicationLauncher.logger.debug("freq data update:"+powerSrcFeedBackData.rPhaseFreqDisplayData);
				}*/
				/*				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdData);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdData.rPhasePowerFactorData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateWattData(refStdData);
				ApplicationLauncher.logger.debug("ActivePower data update:"+refStdData.rPhaseWattDisplayData);

				 */

				//}

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					ProjectExecutionController.UpdateRefStdProgressBar();
				}
			}

		}
		ApplicationLauncher.logger.debug("lscsPowerSourceReadFeedBackAllData: Exit");
	}

	public void kreReadRefStdAllData() {

		ApplicationLauncher.logger.debug("kreReadRefStdAllData: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRefStdKre.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			kreRefStdCommSendReadDataCommand( GuiUtils.hexToAscii(RefStdKreMessage.MSG_READ_BASIC_DATA), GuiUtils.hexToAscii(ConstantRefStdKre.ER_READ_BASIC_DATA_ACK));
			//SerialDataKreRefStd //SerialDataMteRefStd
			SerialDataRefStdKre refStdData = kreRefStdReadBasicParameterData(ConstantRefStdKre.ER_REF_STD_READ_ALL_PARAM_TERMINATOR_COUNT);
			if (refStdData!=null){

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdData);
				ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdData);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdData.rPhaseVoltageDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(refStdData);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+refStdData.rPhaseDegreePhaseData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdData);
				ApplicationLauncher.logger.debug("freq data update:"+refStdData.rPhaseFreqDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdData);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdData.rPhasePowerFactorData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateApparentPower(refStdData);
				}
				if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdData);
				}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactiveEnergy(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateWattData(refStdData);
				
				ApplicationLauncher.logger.debug("ActivePower data update:"+refStdData.rPhaseActivePowerDisplayData);

				if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
					
					DisplayDataObj.updateMetricLogFile();
					
					
				}

			}

			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}


		}
		ApplicationLauncher.logger.debug("kreReadRefStdAllData: Exit");
	}


	public void kiggsReadRefStdAllData() {

		ApplicationLauncher.logger.debug("kiggsReadRefStdAllData: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRefStdKiggs.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(RefStdKiggsMessage.MSG_CMD_GENERAL_REQUEST,ConstantRefStdKiggs.CMD_REF_STD_GENERAL_REQUEST_DATA_EXPECTED_LENGTH);
			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_VOLT_CURRENT,ConstantRefStdKiggs.ER_MEASURED_VOLT_CURRENT_HEADER,ConstantRefStdKiggs.ER_MEASURED_VOLT_CURRENT_DATA_EXPECTED_LENGTH);
			//SerialDataKreRefStd //SerialDataMteRefStd
			SerialDataRefStdKiggs refStdData = kiggsRefStdReadVoltageAndCurrentData();
			if (refStdData!=null){

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdData);
				ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdData);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdData.rPhaseVoltageDisplayData);


			}

			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_DEGREE,
					ConstantRefStdKiggs.ER_MEASURED_DEGREE_PHASE_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_DEGREE_PHASE_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadDegreeData();
			if (refStdData!=null){
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(refStdData);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+refStdData.rPhaseDegreePhaseData);
			}

			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_POWER_FACTOR,
					ConstantRefStdKiggs.ER_MEASURED_POWER_FACTOR_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_POWER_FACTOR_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadPowerFactorData();
			if (refStdData!=null){
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdData);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdData.rPhasePowerFactorData);
			}


			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_APPARENT_POWER,
					ConstantRefStdKiggs.ER_MEASURED_APPARENT_POWER_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_APPARENT_POWER_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadApparentPowerData();
			if (refStdData!=null){
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateApparentPower(refStdData);
			}

			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_REACTIVE_POWER,
					ConstantRefStdKiggs.ER_MEASURED_REACTIVE_POWER_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_REACTIVE_POWER_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadReactivePowerData();
			if (refStdData!=null){
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateREACTIVEPower(refStdData);
			}

			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_ACTIVE_POWER,
					ConstantRefStdKiggs.ER_MEASURED_ACTIVE_POWER_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_ACTIVE_POWER_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadActivePowerData();
			if (refStdData!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
				//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateApparentPower(refStdData);
			}

			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_READ_R_PHASE_FREQUENCY,
					ConstantRefStdKiggs.ER_MEASURED_FREQUENCY_HEADER,
					ConstantRefStdKiggs.ER_MEASURED_FREQUENCY_DATA_EXPECTED_LENGTH);
			refStdData = kiggsRefStdReadFrequencyData();
			if (refStdData!=null){
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdData);
				ApplicationLauncher.logger.debug("freq data update:"+refStdData.rPhaseFreqDisplayData);
			}

			if(DisplayDataObj.getReadRefStdAccuDataFlag()){
				kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_ACCU_READ_MESSAGE,
						ConstantRefStdKiggs.ER_ENERGY_ACCU_READ_HEADER,
						ConstantRefStdKiggs.ER_ENERGY_ACCU_READ_EXPECTED_LENGTH);
				refStdData = kiggsRefStdReadAccuEnergyData();

				if (refStdData!=null){
					if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdData);
						if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdData);
						}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactiveEnergy(refStdData);
						}
					}
					kiggsRefStdReadAccumalativeEnergyTask();
					//ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdData);
					ApplicationLauncher.logger.debug("Energy:");//+refStdData.rPhaseFreqDisplayData);
				}


			}




			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}






		}
		ApplicationLauncher.logger.debug("kiggsReadRefStdAllData: Exit");
	}


	public void kiggsReadRefStdAllDataDebug() {

		ApplicationLauncher.logger.debug("kiggsReadRefStdAllDataDebug: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRefStdKiggs.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(RefStdKiggsMessage.MSG_CMD_GENERAL_REQUEST,ConstantRefStdKiggs.CMD_REF_STD_GENERAL_REQUEST_DATA_EXPECTED_LENGTH);
			kiggsRefStdCommSendDataCommand(GuiUtils.StringToHex("RefTest")+"0D0A",ConstantRefStdKiggs.ER_MEASURED_VOLT_CURRENT_HEADER,ConstantRefStdKiggs.ER_MEASURED_VOLT_CURRENT_DATA_EXPECTED_LENGTH);
			//SerialDataKreRefStd //SerialDataMteRefStd
			SerialDataRefStdKiggs refStdData = kiggsRefStdReadVoltageAndCurrentData();
			if (refStdData!=null){

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdData);
				ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdData);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdData.rPhaseVoltageDisplayData);


			}
		}
	}



	public void kiggsRefStdSetManualModeCurrentTapRange(float selectedUserInputCurrent) {

		ApplicationLauncher.logger.debug("kiggsRefStdSetManualModeCurrentTapRange: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){

			int retrycount =25;//15; //Waiting for 3 Seconds 
			while((!RefStdComSemlock) && (retrycount > 0) && (!ProjectExecutionController.getUserAbortedFlag())){
				Sleep(200);
				retrycount--;
			}
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					ClearStdRefSerialData();
					SerialDataRefStdKiggs.ClearRefStd_ReadSerialData();
					int dummyExpectedLength = 10;
					//RefStdCommSendCommand(RefStdKiggsMessage.getMsgCurrentRangeSettingManualMode(selectedUserInputCurrent),dummyExpectedLength);
					String dummyExpectedData = "";
					ApplicationLauncher.logger.debug("kiggsRefStdSetManualModeCurrentTapRange: Setting selectedUserInputCurrent: " + selectedUserInputCurrent);
					kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.getMsgCurrentRangeSettingManualMode(selectedUserInputCurrent),dummyExpectedData,dummyExpectedLength );
	
	
					RefStdComSemlock = true;
				}
			}


		}
	}

	public boolean kiggsReadRefStdVoltAndCurrentTapRange() {

		ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: Entry:");
		boolean status = false;
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		//if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
		if( !ProjectExecutionController.getUserAbortedFlag()){	
			int retrycount =25;//15; //Waiting for 3 Seconds 
			//ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: semLock awaiting");
			while((!RefStdComSemlock) && (retrycount > 0) && (!ProjectExecutionController.getUserAbortedFlag())){
				Sleep(200);
				retrycount--;
			}

			if(RefStdComSemlock){
				RefStdComSemlock = false;
				//ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: semLock locked");
				ClearStdRefSerialData();
				SerialDataRefStdKiggs.ClearRefStd_ReadSerialData();
				int dummyExpectedLength = 10;
				//RefStdCommSendCommand(RefStdKiggsMessage.getMsgCurrentRangeSettingManualMode(selectedUserInputCurrent),dummyExpectedLength);
				ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: Setting..:");
				kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_CMD_REFSTD_METER_WORKING_MODE,
						ConstantRefStdKiggs.ER_REFSTD_METER_WORKING_MODE_HEADER,
						ConstantRefStdKiggs.ER_REFSTD_METER_WORKING_MODE_EXPECTED_LENGTH);
				SerialDataRefStdKiggs refStdData = kiggsRefStdReadSettingVoltTapAndCurrentTapData();
				if (refStdData!=null){
					//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
					//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
					try{
						DisplayDataObj.setRefStdSelectedVoltageTap((Float.parseFloat(refStdData.getR_PhaseVoltageTapData())/ConstantRefStdKiggs.VOLTAGE_TAP_DIVISOR));
						DisplayDataObj.setRefStdSelectedCurrentTap((Float.parseFloat(refStdData.getR_PhaseCurrentTapData())/ConstantRefStdKiggs.CURRENT_TAP_DIVISOR));
						status = true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("kiggsReadRefStdVoltAndCurrentTapRange :Exception: "+e.getMessage() );
					}
				}

				RefStdComSemlock = true;
				//ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: semLock released");
			}else{
				ApplicationLauncher.logger.debug("kiggsReadRefStdVoltAndCurrentTapRange: semLock failed");
			}


		}
		return status;
	}



	public void mteReadRefStdData() {

		ApplicationLauncher.logger.debug("mteReadRefStdData: Entry:");
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_VOLT_PH_N_HDR,ConstantRefStdMte.ER_REF_STD_READ_VOLT_PH_N_HDR);

			SerialDataMteRefStd refStdDataR_Phase = mteRefStdReadVoltageData();
			if (refStdDataR_Phase!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdDataR_Phase.rPhaseVoltageDisplayData);
			}
			ProjectExecutionController.UpdateRefStdProgressBar();


			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_CURRENT_HDR,ConstantRefStdMte.ER_REF_STD_READ_CURRENT_HDR);
			refStdDataR_Phase = mteRefStdReadCurrentData();
			if (refStdDataR_Phase!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("Current data update:"+refStdDataR_Phase.rPhaseCurrentDisplayData);
			}
			ProjectExecutionController.UpdateRefStdProgressBar();



			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_ACTIVE_POWER_HDR,ConstantRefStdMte.ER_REF_STD_READ_ACTIVE_POWER_HDR);
			refStdDataR_Phase = mteRefStdReadWattData();
			if (refStdDataR_Phase!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdateWattData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("ActivePower data update:"+refStdDataR_Phase.rPhaseWattDisplayData);
				/*				ApplicationLauncher.logger.debug("mteReadRefStdData : isRefStdLogResultsEnabled : "+ DeviceDataManagerController.isRefStdLogResultsEnabled());
				if(DeviceDataManagerController.isRefStdLogResultsEnabled()) {
					ApplicationLauncher.logger.debug("mteReadRefStdData : AddingData: ");
					try {
						//DeviceDataManagerController.addRefStdInstantActivePowerResult(Float.parseFloat(refStdDataR_Phase.rPhaseWattDisplayData));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("mteReadRefStdData :Exception: "+e.getMessage() );
					}
				}*/
			}
			ProjectExecutionController.UpdateRefStdProgressBar();

			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			// power factor data not available from the Mte refstandard, hence calculated with formula pf = ActivePower/ ApparentPower suggested by MTE, Mr VinayGupa
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_APPARENT_POWER_HDR,ConstantRefStdMte.ER_REF_STD_READ_APPARENT_POWER_HDR);
			refStdDataR_Phase = mteRefStdReadPowerFactorData();
			if (refStdDataR_Phase!=null){

				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdDataR_Phase.rPhasePowerFactorData);
			}
			ProjectExecutionController.UpdateRefStdProgressBar();


			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_PHASE_ANGLE_HDR,ConstantRefStdMte.ER_REF_STD_READ_PHASE_ANGLE_HDR);
			refStdDataR_Phase = mteRefStdReadDegreePhaseData();
			if (refStdDataR_Phase!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+refStdDataR_Phase.rPhaseDegreePhaseData);
			}
			ProjectExecutionController.UpdateRefStdProgressBar();


			ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			mteRefStdCommSendReadDataCommand(ConstantRefStdMte.CMD_REF_STD_READ_FREQ_HDR,ConstantRefStdMte.ER_REF_STD_READ_FREQ_HDR);

			refStdDataR_Phase = mteRefStdReadFreqData();
			if (refStdDataR_Phase!=null){
				DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdDataR_Phase);
				ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdDataR_Phase);
				ApplicationLauncher.logger.debug("freq data update:"+refStdDataR_Phase.rPhaseFreqDisplayData);
			}
			ProjectExecutionController.UpdateRefStdProgressBar();


			refStdDataR_Phase=null; //garbagecollector

		}

		/*		if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();

				SerialDataRefStd.ClearRefStd_ReadSerialData();k;kl
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_Y_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				mteRefStdCommSendCommand(ConstantMteRefStd.CMD_REF_STD_READ_Y_PHASE,ConstantMteRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);

				SerialDataRefStd refStdDataY_Phase =mteRefStdReadPhaseData();
				if (refStdDataY_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_Y_Phase(refStdDataY_Phase);
					ApplicationLauncher.logger.debug("Y Phase Volt:"+refStdDataY_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataY_Phase =null; //garbagecollector
			}
			if(DisplayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
				ClearStdRefSerialData();
				//SerialDataRefStd.setRefStd_ReadSerialData("");
				SerialDataRefStd.ClearRefStd_ReadSerialData();m,.,
				//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_B_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
				mteRefStdCommSendCommand(ConstantMteRefStd.CMD_REF_STD_READ_B_PHASE,ConstantMteRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);

				SerialDataRefStd refStdDataB_Phase =mteRefStdReadPhaseData();
				if (refStdDataB_Phase!=null){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_B_Phase(refStdDataB_Phase);
					ApplicationLauncher.logger.debug("B Phase Volt:"+refStdDataB_Phase.VoltageDisplayData);
				}
				ProjectExecutionController.UpdateRefStdProgressBar();
				refStdDataB_Phase = null; //garbagecollector
			}
		}*/


		ApplicationLauncher.logger.debug("mteReadRefStdData:Exit");



	}



	public void LDU_ResetErrorTrigger() {
		ApplicationLauncher.logger.debug("LDU_ResetErrorTrigger :Entry");
		lduTimer = new Timer();
		lduTimer.schedule(new lduComResetErrorTask(), 100);

		ApplicationLauncher.logger.debug("LDU_ResetErrorTrigger : Exit");

	}

	public void Ref_AccumulateSettingTrigger(){
		ApplicationLauncher.logger.debug("Ref_AccumulateSettingTrigger :Entry");
		RefStdTimer = new Timer();
		RefStdTimer.schedule(new RefAccumulateSettingTask(), 100);

		ApplicationLauncher.logger.debug("Ref_AccumulateSettingTrigger: Exit");

	}

	public void Ref_AccumlateStartTrigger(){
		ApplicationLauncher.logger.debug("Ref_AccumlateStartTrigger :Entry");
		RefStdTimer = new Timer();
		RefStdTimer.schedule(new RefAccumulateStartTask(), 100);

		ApplicationLauncher.logger.debug("Ref_AccumlateStartTrigger : Exit");

	}

	public static void setAccumlativeDataReadCommandType(String ActiveReactiveCommand){
		AccumlativeDataReadCommandType =ActiveReactiveCommand;
	}

	public static String getAccumlativeDataReadCommandType( ){
		return AccumlativeDataReadCommandType;
	}




	public void RefStd_BNC_ConfigConstTrigger(){
		ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTrigger :Entry");
		//RefStdTimer = new Timer();
		RefStdBNC_ConstTimer = new Timer();
		/*if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			setAccumlativeDataReadCommandType(ConstantRefStd.CMD_REF_STD_READ_WATT_HOUR_READING);
		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			setAccumlativeDataReadCommandType(ConstantRefStd.CMD_REF_STD_READ_VARH_READING);
		}*/
		//RefStdTimer.schedule(new RefStd_BNC_ConfigConstTask(), 100);
		RefStdBNC_ConstTimer.schedule(new RefStd_BNC_ConfigConstTask(), 10);

		ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTrigger : Exit");

	}


	public void refStdAccuReadingTrigger(){
		ApplicationLauncher.logger.debug("Ref_MeterReadingTrigger :Entry");
		RefStdTimer = new Timer();
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			setAccumlativeDataReadCommandType(ConstantRefStdRadiant.CMD_REF_STD_READ_WATT_HOUR_READING);
		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			setAccumlativeDataReadCommandType(ConstantRefStdRadiant.CMD_REF_STD_READ_VARH_READING);
		}

		RefStdTimer.schedule(new RefMeterAccuReadingTask(), 100);



		ApplicationLauncher.logger.debug("Ref_MeterReadingTrigger : Exit");

	}

	public void Ref_AccumlateStopTrigger(){
		ApplicationLauncher.logger.debug("Ref_AccumlateStopTrigger :Entry");
		RefStdTimer = new Timer();
		RefStdTimer.schedule(new RefAccStopTask(), 100);

		ApplicationLauncher.logger.debug("Ref_AccumlateStopTrigger : Exit");

	}


	public void LDU_ResetSettingTrigger() {
		ApplicationLauncher.logger.debug("LDU_ResetSettingTrigger :Entry");
		lduTimer = new Timer();
		lduTimer.schedule(new lduComResetSettingTask(), 100);

		ApplicationLauncher.logger.debug("LDU_ResetSettingTrigger : Exit");

	}


	public void LDU_CreepSettingTrigger() {
		ApplicationLauncher.logger.debug("LDU_CreepSettingTrigger :Entry");
		lduTimer = new Timer();
		lduTimer.schedule(new lduComCreepSettingTask(), 100);

		ApplicationLauncher.logger.debug("LDU_CreepSettingTrigger : Exit");

	}

	public void LDU_ConstSettingTrigger() {
		ApplicationLauncher.logger.debug("LDU_ConstSettingTrigger :Entry");
		lduTimer = new Timer();
		lduTimer.schedule(new lduComConstSettingTask(), 100);

		ApplicationLauncher.logger.debug("LDU_ConstSettingTrigger : Exit");

	}

	public void LDU_STASettingTrigger() {
		ApplicationLauncher.logger.debug("LDU_STASettingTrigger :Entry");
		lduTimer = new Timer();
		lduTimer.schedule(new lduComSTASettingTask(), 100);

		ApplicationLauncher.logger.debug("LDU_STASettingTrigger : Exit");

	}

	public boolean LDU_ResetSetting(){
		ApplicationLauncher.logger.debug("LDU_ResetSetting :Entry");
		boolean status=false;
		status = Check_And_SendLDUCommand();
		return status;
	}



	public boolean lscsUploadLOE_Setting(){
		ApplicationLauncher.logger.debug("lscsUploadLOE_Setting :Entry");
		boolean status=false;
		status = lscsSendLDU_SettingCommand();
		return status;
	}

	public void lscsClearLDU_Result() {
		ApplicationLauncher.logger.debug("lscsClearLDU_Result: Entry"  );
		int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
		Communicator SerialPortObj =commLDU;
		SerialDataLDU lduData = new SerialDataLDU(SerialPortObj);


		for(int Address=1;Address<=MaximumNumberOfDeviceConnected;Address++){
			//lduData.setLDU_ResultStatus(ConstantLscsLDU.TO_BE_UPDATED,Address);
			lduData.setLDU_ResultStatus(ConstantReport.RESULT_STATUS_UNDEFINED.trim(),Address);
			lduData.setLDU_ErrorValue("",Address);
			lduData.setReceivedReadingIndex(-1, Address);
		}
		SerialPortObj = null;//garbagecollector

	}

	public boolean lscsLDU_CheckCom(){
		ApplicationLauncher.logger.debug("lscsLDU_CheckCom :Entry");
		boolean status=false;
		//status = Check_And_SendLDUCommand();

		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsLDU_CheckCom: getPortValidationTurnedON");

			SerialDataLDU.ClearLDU_ReadSerialData();
			lsLDU_ClearSerialData();
			DisplayDataObj.setLDU_ReadDataFlag( true);
			lsLDU_SendCommandReadAccuracyData(ConstantLduLscs.CMD_LDU_POSITION_01_HDR);
			SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER.length(),"");//ConstantLsLDU.CMD_LDU_ERROR_DATA_ER);
			if(lduData.IsExpectedResponseReceived()){

				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("lscsLDU_CheckCom: LDU Received DataA:"+CurrentLDU_Data);
				StripLDU_SerialData(lduData.getReceivedLength());
				status=true;
			}else{
				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("lscsLDU_CheckCom: CurrentLDU_Data2: "+CurrentLDU_Data);
			}
			DisplayDataObj.setLDU_ReadDataFlag( false);
		}
		return status;
	}

	public boolean kreICT_CheckCom(){
		ApplicationLauncher.logger.debug("kreICT_CheckCom :Entry");
		boolean status=false;
		//status = Check_And_SendLDUCommand();

		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("kreICT_CheckCom: getPortValidationTurnedON");

			//SerialDataKreICT.clearIctReadSerialData();
			//lsLDU_ClearSerialData();
			DisplayDataObj.setIctReadData( true);
			String command = IctKreMessage.getCommandResetState(Constant_ICT.CMD_ICT_POSITION_BROADCAST_HDR);
			String expectedResponse = Constant_ICT.ER_RESET_STATE;
			int noOfReadingRequired = 1;
			status = kreIctCommSendCommand(command,expectedResponse,noOfReadingRequired);
			if(status){

				ApplicationLauncher.logger.info("kreICT_CheckCom: ICT Received expected data:");
				status=true;
			}else{
				ApplicationLauncher.logger.info("kreICT_CheckCom: ICT response failed:");
			}
			DisplayDataObj.setIctReadData( false);
		}
		return status;
	}

	public boolean kreIctResetAllState(){
		ApplicationLauncher.logger.debug("kreIctResetAllState :Entry");
		boolean status=false;
		//status = Check_And_SendLDUCommand();


		ApplicationLauncher.logger.info("kreIctResetAllState: getPortValidationTurnedON");

		//SerialDataKreICT.clearIctReadSerialData();
		//lsLDU_ClearSerialData();
		DisplayDataObj.setIctReadData( true);
		String command = IctKreMessage.getCommandResetState(Constant_ICT.CMD_ICT_POSITION_BROADCAST_HDR);
		String expectedResponse = Constant_ICT.ER_RESET_STATE;
		int noOfReadingRequired = 1;
		status = kreIctCommSendCommand(command,expectedResponse,noOfReadingRequired);
		if(status){

			ApplicationLauncher.logger.info("kreIctResetAllState: ICT Received expected data:");
			status=true;
		}else{
			ApplicationLauncher.logger.info("kreIctResetAllState: ICT response failed:");
		}
		DisplayDataObj.setIctReadData( false);
		//}
		return status;
	}

	public boolean Check_And_SendLDUCommand(){
		ApplicationLauncher.logger.debug("Check_And_SendLDUCommand :Entry");
		boolean status =  false;
		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("Check_And_SendLDUCommand: getPortValidationTurnedON");
			String meter_const = "2500";
			String MeterAddress = "00";
			LDU_SendCommandErrorSetting(MeterAddress,meter_const);
			SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);
			if(lduData.IsExpectedResponseReceived()){

				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataA:"+CurrentLDU_Data);
				StripLDU_SerialData(lduData.getReceivedLength());
				status=true;
			}
		}else{
			String project_name = ProjectExecutionController.getCurrentProjectName();
			ApplicationLauncher.logger.info("Check_And_SendLDUCommand :project_name: " +project_name);
			if(project_name!=null){
				JSONObject result = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
				try {
					JSONArray deployed_devices = result.getJSONArray("Devices");
					ApplicationLauncher.logger.info("Check_And_SendLDUCommand :deployed_devices: " +deployed_devices.toString());
					if(DisplayDataObj.IsAllMeterConstSame()){
						JSONObject jobj = deployed_devices.getJSONObject(0);
						int m_const = jobj.getInt("meter_const");
						String meter_const = Integer.toString(m_const);
						String MeterAddress = "00";
						ApplicationLauncher.logger.info("Check_And_SendLDUCommand: meter_const"+ meter_const);
						LDU_SendCommandErrorSetting(MeterAddress,meter_const);
						SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);						

						if(lduData.IsExpectedResponseReceived()){

							String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataB:"+CurrentLDU_Data);
							StripLDU_SerialData(lduData.getReceivedLength());
							status=true;
						}

					}
					else{
						ApplicationLauncher.logger.info("Check_And_SendLDUCommand :deployed_devices.length(): " +deployed_devices.length());
						SerialDataLDU lduData3 = null;
						int AllDeviceStatus = 0;
						JSONObject jobj = new JSONObject();
						int m_const = 0;
						String meter_const = "";
						String MeterAddress = "";
						String CurrentLDU_Data = "";
						for(int i=0; i<deployed_devices.length(); i++){
							jobj = deployed_devices.getJSONObject(i);
							m_const = jobj.getInt("meter_const");
							meter_const = Integer.toString(m_const);
							MeterAddress = "0"+Integer.toString(i+1);
							ApplicationLauncher.logger.info("Check_And_SendLDUCommand: meter_const2"+ meter_const);
							ApplicationLauncher.logger.info("Check_And_SendLDUCommand: MeterAddress2:"+ MeterAddress);
							LDU_SendCommandErrorSetting(MeterAddress,meter_const);
							lduData3 = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);
							if(lduData3.IsExpectedResponseReceived()){

								CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
								ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received Data"+ i + ":"+CurrentLDU_Data);
								StripLDU_SerialData(lduData3.getReceivedLength());
								AllDeviceStatus ++;
							}
							CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
							ApplicationLauncher.logger.info("LDU_ResetSetting: test LDU Received Data"+ i + ":"+CurrentLDU_Data);

						}
						if (AllDeviceStatus == deployed_devices.length()){
							ApplicationLauncher.logger.debug("Check_And_SendLDUCommand :All Device response: Passed" );
							status = true;
						}else{
							status = false;
							ApplicationLauncher.logger.debug("Check_And_SendLDUCommand :All Device response: Failed:AllDeviceStatus:"+AllDeviceStatus+ "deployed_devices.length():"+deployed_devices.length());
						}

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("Check_And_SendLDUCommand :JSONException: "+e.getMessage() );
				}
			} 
		}
		return status;
	}

	public boolean lscsSendLDU_SettingCommand(){
		ApplicationLauncher.logger.debug("lscsSendLDU_SettingCommand :Entry");
		boolean status =  false;
		/*		if (DevicePortSetupController.getPortValidationTurnedON()){
			ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand: getPortValidationTurnedON");
			String meter_const = "2500";
			String MeterAddress = "00";
			LDU_SendCommandErrorSetting(MeterAddress,meter_const);
			SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER);
			if(lduData.IsExpectedResponseReceived()){

				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataA:"+CurrentLDU_Data);
				StripLDU_SerialData(lduData.getReceivedLength());
				status=true;
			}
		}else{*/
		String project_name = ProjectExecutionController.getCurrentProjectName();
		ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand :project_name: " +project_name);
		if(project_name!=null){
			JSONObject result = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
			try {
				JSONArray deployed_devices = result.getJSONArray("Devices");
				ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand :deployed_devices: " +deployed_devices.toString());
				//DeploymentManagerController.setAllMeterConstSame(true);// to be updated 
				if(DisplayDataObj.IsAllMeterConstSame()){
					JSONObject jobj = deployed_devices.getJSONObject(0);
					int m_const = jobj.getInt("meter_const");
					String meter_const = Integer.toString(m_const);
					String MeterAddress = "00";
					ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand: meter_const: "+ meter_const);
					//LDU_SendCommandErrorSetting(MeterAddress,meter_const);
					lscsLDU_SendCommandErrorSetting(MeterAddress,meter_const);
					status=true;
					/*						SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER);						

						if(lduData.IsExpectedResponseReceived()){

							String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataB:"+CurrentLDU_Data);
							StripLDU_SerialData(lduData.getReceivedLength());
							status=true;
						}*/

				}else{
					ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand :deployed_devices.length(): " +deployed_devices.length());
					SerialDataLDU lduData3 = null;
					int AllDeviceStatus = 0;
					JSONObject jobj = new JSONObject();
					int m_const = 0;
					String meter_const = "";
					String MeterAddress = "";
					String CurrentLDU_Data = "";
					int readRackId= 0;
					if(!ProjectExecutionController.getUserAbortedFlag()) {
						lscsLDU_SendRefreshDataCommand();
						Sleep(1000);
					}
					if(!ProjectExecutionController.getUserAbortedFlag()) {
						lscsLDU_SendRefreshDataCommand();
						Sleep(1000);
					}
					//for(int rackId=1; rackId<=ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK; rackId++){
					if(!ProjectExecutionController.getUserAbortedFlag()) {
						for(int i=0; i<deployed_devices.length(); i++){
							jobj = deployed_devices.getJSONObject(i);
							m_const = jobj.getInt("meter_const");
							readRackId = jobj.getInt("Rack_ID");
							//if(readRackId == rackId){
							meter_const = Integer.toString(m_const);
							MeterAddress = Integer.toString(readRackId);
							//MeterAddress = "0"+Integer.toString(i+1);
							/*							if(i==0){
											MeterAddress = Integer.toString(19);
										}else{
											MeterAddress = Integer.toString(20);
										}*/
							//MeterAddress = lscsLduAddressMapping((i+1));
							ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand: meter_const2: "+ meter_const);
							ApplicationLauncher.logger.info("lscsSendLDU_SettingCommand: MeterAddress2: "+ MeterAddress);
							//LDU_SendCommandErrorSetting(MeterAddress,meter_const);
							//							lscsLDU_SendCommandErrorSetting(MeterAddress,meter_const);
							//							AllDeviceStatus ++;
							ApplicationHomeController.update_left_status("LDU setting update: " + readRackId + "/" + deployed_devices.length(),ConstantApp.LEFT_STATUS_DEBUG);
							lscsLDU_SendCommandErrorSettingForIndividualMeter(MeterAddress,meter_const);
							AllDeviceStatus ++;
							//}
							//}
							//}
	
	
	
							/*							lduData3 = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER);
								if(lduData3.IsExpectedResponseReceived()){
	
									CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
									ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received Data"+ i + ":"+CurrentLDU_Data);
									StripLDU_SerialData(lduData3.getReceivedLength());
									AllDeviceStatus ++;
								}
								CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
								ApplicationLauncher.logger.info("LDU_ResetSetting: test LDU Received Data"+ i + ":"+CurrentLDU_Data);*/
	
						}
					}else {
						ApplicationLauncher.logger.debug("lscsSendLDU_SettingCommand : aborted" );
						return false;
					}
					if (AllDeviceStatus == deployed_devices.length()){
						ApplicationLauncher.logger.debug("lscsSendLDU_SettingCommand :All Device response: Passed" );
						status = true;
					}else{
						status = false;
						ApplicationLauncher.logger.debug("lscsSendLDU_SettingCommand :All Device response: Failed:AllDeviceStatus:"+AllDeviceStatus+ "deployed_devices.length():"+deployed_devices.length());
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("lscsSendLDU_SettingCommand :JSONException: "+e.getMessage() );
			}
		} 
		//}
		return status;
	}

	public void lscsLDU_SendCommandErrorSettingForIndividualMeter(String MeterAddress, String meter_const){
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSetting :Entry");
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSetting : MeterAddress: " + MeterAddress);
		String MeterConstUnit = "00";
		//String RSS_PulseRate = DisplayDataObj.getRSSPulseRate();

		//long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
		/*		if(ConstantFeatureEnable.REF_STD_CONST_CALCULATE){
			if (!DevicePortSetupController.getPortValidationTurnedON()){
				//long RssConstantInWh = calculateRSS_ConstantV4(meter_const);
				RssConstantInKWh = calculateRSS_ConstantV4(meter_const)*1000;
			}
		}*/
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSettingForIndividualMeter :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
		//String RSS_PulseRate = GUIUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
		String AverageCycle = "";
		//String CalculateMode = "00";
		if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED)){
			//AverageCycle = GUIUtils.FormatAvgPulses(DeviceDataManagerController.getNoOfPulses());
			AverageCycle = DeviceDataManagerController.getNoOfPulses();
			ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter-Pulse-Based : NoOfPulses: " + AverageCycle);
			//CalculateMode = "00";
		}else if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
			//AverageCycle = GUIUtils.FormatTimeForAvgPulses(DeviceDataManagerController.getInfTimeDuration());
			//AverageCycle = DeviceDataManagerController.getNoOfPulses();
			int timeDurationInSec  = DisplayDataObj.getInfTimeDuration();
			float totalTargetPowerInKiloWatt = calculateTotalTargetPower();
			int meterConstantInImpulsesPerKiloWattHour = Integer.parseInt(meter_const);
			AverageCycle = DisplayDataObj.manipulateNoOfPulsesForTimeBased(meterConstantInImpulsesPerKiloWattHour,timeDurationInSec,totalTargetPowerInKiloWatt); 
			ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter-Time-Based : NoOfPulses: " + AverageCycle);
			//CalculateMode = "01";
		}





		String ErrorHighLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_max());
		String ErrorLowLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_min());
		//String MUT_PulseRate = GUIUtils.FormatPulseRate(meter_const);
		ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter: getDutImpulsesPerUnit: " + DeviceDataManagerController.getDutImpulsesPerUnit());
		ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter: meter_const: " + meter_const);
		//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting: MUT_PulseRate: " + MUT_PulseRate);
		//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting :RSS_PulseRate: " + RSS_PulseRate);
		ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter: ErrorHighLimit1: " + ErrorHighLimit);
		ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSettingForIndividualMeter: ErrorLowLimit1: " + ErrorLowLimit);  

		int RssPulsePerMutPulse = 0;
		RssPulsePerMutPulse = ceigLDU_SettingCalculationMethod( RssConstantInKWh, meter_const, AverageCycle);
		String rssPulsePerMutPulseStr = String.valueOf(RssPulsePerMutPulse);
		lscsLDU_SendCeigSettingMethodIndividualMeter(MeterAddress,rssPulsePerMutPulseStr,AverageCycle);
		ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSettingForIndividualMeter: lscsLDU setting DataSend Completed");



	}

	private float calculateTotalTargetPower() {

		ApplicationLauncher.logger.debug("calculateTotalTargetPower :Entry");
		float targetPowerInKiloWatt = 0.0f;


		try{

			float targetRphaseVoltage = Float.parseFloat(DisplayDataObj.getR_PhaseOutputVoltage());
			float targetRphaseCurrent = Float.parseFloat(DisplayDataObj.getR_PhaseOutputCurrent());
			float targetRphasePowerFactor = 0.0f;


			String metertype = DisplayDataObj.getDeployedEM_ModelType();

			/*		if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
				ArrayList<String> I_PF_values = DisplayDataObj.ExtractI_PF_From_TP_Name(ProjectExecutionController.getCurrentTestPointName());
				String pfValueStr = I_PF_values.get(0).replace("C", "").replace("L", "");
				targetRphasePowerFactor = Float.parseFloat(pfValueStr);
			}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
				//DisplayDataObj.get_PwrSrcR_PhaseDegreePhase();v v 
			}*/
			float rPhaseDegree = DisplayDataObj.get_PwrSrcR_PhaseDegreePhase();
			ApplicationLauncher.logger.debug("calculateTotalTargetPower : rPhaseDegree : " + rPhaseDegree  );
			targetRphasePowerFactor = (float) Math.cos(Math.toRadians(rPhaseDegree));
			BigDecimal bigValue = new BigDecimal(targetRphasePowerFactor);
			bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
			ApplicationLauncher.logger.debug("calculateTotalTargetPower : rPhaseDegree : bigValue :" + bigValue);
			targetRphasePowerFactor = bigValue.floatValue();
			ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetRphasePowerFactor : " + targetRphasePowerFactor  );
			//targetRphasePowerFactor = (float) Math.cos(rPhaseDegree);
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				targetPowerInKiloWatt = (targetRphaseVoltage*targetRphaseCurrent*targetRphasePowerFactor)/1000.0f;
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : single Phase : targetPowerInKiloWatt : " + targetPowerInKiloWatt);
			}else{
				float targetYphaseVoltage = Float.parseFloat(DisplayDataObj.getY_PhaseOutputVoltage());
				float targetYphaseCurrent = Float.parseFloat(DisplayDataObj.getY_PhaseOutputCurrent());
				float targetYphasePowerFactor = 0.0f;

				float yPhaseDegree = DisplayDataObj.get_PwrSrcY_PhaseDegreePhase();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : yPhaseDegree : " + yPhaseDegree  );
				targetYphasePowerFactor = (float) Math.cos(Math.toRadians(yPhaseDegree));
				bigValue = new BigDecimal(targetYphasePowerFactor);
				bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : yPhaseDegree : bigValue :" + bigValue);
				targetYphasePowerFactor = bigValue.floatValue();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetYphasePowerFactor : " + targetYphasePowerFactor  );



				float targetBphaseVoltage = Float.parseFloat(DisplayDataObj.getB_PhaseOutputVoltage());
				float targetBphaseCurrent = Float.parseFloat(DisplayDataObj.getB_PhaseOutputCurrent());
				float targetBphasePowerFactor = 0.0f;

				float bPhaseDegree = DisplayDataObj.get_PwrSrcY_PhaseDegreePhase();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : bPhaseDegree : " + bPhaseDegree  );
				targetBphasePowerFactor = (float) Math.cos(Math.toRadians(bPhaseDegree));
				bigValue = new BigDecimal(targetBphasePowerFactor);
				bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : bPhaseDegree : bigValue :" + bigValue);
				targetBphasePowerFactor = bigValue.floatValue();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetBphasePowerFactor : " + targetBphasePowerFactor  );


				float rPhasePowerInWatt = targetRphaseVoltage*targetRphaseCurrent*targetRphasePowerFactor;
				float yPhasePowerInWatt = targetYphaseVoltage*targetYphaseCurrent*targetYphasePowerFactor;
				float bPhasePowerInWatt = targetBphaseVoltage*targetBphaseCurrent*targetBphasePowerFactor;
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : rPhasePowerInWatt : " + rPhasePowerInWatt);
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : yPhasePowerInWatt : " + yPhasePowerInWatt);
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : bPhasePowerInWatt : " + bPhasePowerInWatt);
				targetPowerInKiloWatt = (rPhasePowerInWatt+ yPhasePowerInWatt+bPhasePowerInWatt)/1000.0f;
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : targetPowerInKiloWatt : " + targetPowerInKiloWatt);
			}

		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("calculateTotalTargetPower exception: " + e.getMessage());
		}

		// TODO Auto-generated method stub
		return targetPowerInKiloWatt;
	}

	public void lscsLDU_SendCeigSettingMethodIndividualMeter(String LDU_ReadAddress, String rssPulsePerMutPulse, String noOfPulses){
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethodIndividualMeter :Entry");
		/*		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethodIndividualMeter :Sending Refresh command");
		//WriteToSerialCommLDU("YY");dfhgf
		WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		Sleep(1000);
		WriteToSerialCommLDU(ConstantLscsLDU.CMD_LDU_CEIG_DATA_REFRESH_HDR);
		Sleep(1000);*/
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethodIndividualMeter : LDU_ReadAddress: " + LDU_ReadAddress);
		String meterAddressHeader = lscsLduDialTestAddressMapping(Integer.parseInt(LDU_ReadAddress));
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethodIndividualMeter : meterAddressHeader: " + meterAddressHeader);
		String Data = meterAddressHeader + noOfPulses+ConstantLduLscs.CMD_LDU_CEIG_SETTING_SEPERATOR +
				rssPulsePerMutPulse +ConstantLduLscs.CMD_LDU_CEIG_SETTING_SEPERATOR;
		//String Data = noOfPulses+ConstantLscsLDU.CMD_LDU_CEIG_SETTING_SEPERATOR +
		//		rssPulsePerMutPulse +ConstantLscsLDU.CMD_LDU_CEIG_SETTING_SEPERATOR;
		//WriteToSerialCommLDU(Data);
		//Data = noOfPulses;
		ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethodIndividualMeter : Data: " + Data);
		//WriteToSerialCommLduHex(meterAddressHeader);
		//Sleep(200);
		if(!ProjectExecutionController.getUserAbortedFlag()) {
			for(int i = 0; i < Data.length(); i++){
				//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
				if(!ProjectExecutionController.getUserAbortedFlag()) {
					WriteToSerialCommLDU(String.valueOf(Data.charAt(i)));
					Sleep(200);
				}
			}
		}

	}


	public boolean LDU_CreepSetting(){
		ApplicationLauncher.logger.debug("LDU_CreepSetting :Entry");
		boolean status=false;
		LDU_SendCommandCreepSetting();
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_CREEP_RESET_ER.length(),ConstantLduCcube.CMD_LDU_CREEP_RESET_ER);
		if(lduData.IsExpectedResponseReceived()){

			String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
			ApplicationLauncher.logger.info("LDU_CreepSetting: LDU Received Data:"+CurrentLDU_Data);
			StripLDU_SerialData(lduData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_CreepSetting: No Data Received");
		}

		return status;

	}

	public boolean lscsLDU_CreepSetting(){
		ApplicationLauncher.logger.debug("lscsLDU_CreepSetting :Entry");
		boolean status=false;
		lscsLDU_SendCommandCreepSetting();
		/*		SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_CREEP_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_CREEP_RESET_ER);
		if(lduData.IsExpectedResponseReceived()){

			String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
			ApplicationLauncher.logger.info("lscsLDU_CreepSetting: LDU Received Data:"+CurrentLDU_Data);
			StripLDU_SerialData(lduData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("lscsLDU_CreepSetting: No Data Received");
		}*/

		return status;

	}

	public boolean LDU_ConstSetting(){
		ApplicationLauncher.logger.debug("LDU_ConstSetting :Entry");
		boolean status=false;
		LDU_SendCommandConstSetting();
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_CONST_RESET_ER.length(),ConstantLduCcube.CMD_LDU_CONST_RESET_ER);
		if(lduData.IsExpectedResponseReceived()){

			String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
			ApplicationLauncher.logger.info("LDU_ConstSetting: LDU Received Data:"+CurrentLDU_Data);
			StripLDU_SerialData(lduData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_ConstSetting: No Data Received");
		}

		return status;

	}

	public boolean LDU_STASetting(){
		ApplicationLauncher.logger.debug("LDU_STASetting :Entry");
		boolean status=false;
		LDU_SendCommandSTASetting();
		SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_STA_RESET_ER.length(),ConstantLduCcube.CMD_LDU_STA_RESET_ER);
		if(lduData.IsExpectedResponseReceived()){

			String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
			ApplicationLauncher.logger.info("LDU_STASetting: LDU Received Data:"+CurrentLDU_Data);
			StripLDU_SerialData(lduData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_STASetting: No Data Received");
		}

		return status;

	}

	public boolean lscsLDU_STASetting(){
		ApplicationLauncher.logger.debug("lscsLDU_STASetting :Entry");
		boolean status=false;
		lscsLDU_SendCommandSTA_Setting();
		/*		SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_STA_RESET_ER);
		if(lduData.IsExpectedResponseReceived()){

			String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
			ApplicationLauncher.logger.info("LDU_STASetting: LDU Received Data:"+CurrentLDU_Data);
			StripLDU_SerialData(lduData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("LDU_STASetting: No Data Received");
		}*/

		return status;

	}



	public boolean lscsLDU_ConstTestSetting(){
		ApplicationLauncher.logger.debug("lscsLDU_ConstTestSetting :Entry");
		boolean status=false;
		lscsLDU_SendCommandConstTest_Setting();


		return status;

	}

	public boolean LDU_ResetError(){
		ApplicationLauncher.logger.debug("LDU_ResetError :Entry");
		boolean status=false;
		int retryCount=3;

		while (retryCount>0 && status==false){
			retryCount--;
			LDU_SendCommandResetError();
			SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);
			if(lduData.IsExpectedResponseReceived()){

				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("LDU_ResetError:LDU Received Data1:"+CurrentLDU_Data);
				StripLDU_SerialData(lduData.getReceivedLength());
				status=true;
			}
			else{
				ApplicationLauncher.logger.info("LDU_ResetError:No Data Received");
			}
		}
		return status;
	}

	public boolean lscsLDU_ResetError(){
		ApplicationLauncher.logger.debug("lscsLDU_ResetError :Entry");
		boolean status=false;
		int retryCount=1;

		while (retryCount>0 && status==false){
			retryCount--;
			lscsLDU_SendCommandResetError();
			/*			SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER.length(),ConstantCcubeLDU.CMD_LDU_ERROR_RESET_ER);
			if(lduData.IsExpectedResponseReceived()){

				String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
				ApplicationLauncher.logger.info("lscsLDU_ResetError: LDU Received Data1:"+CurrentLDU_Data);
				StripLDU_SerialData(lduData.getReceivedLength());
				status=true;
			}
			else{
				ApplicationLauncher.logger.info("lscsLDU_ResetError: No Data Received");
			}*/
		}
		return status;
	}

	public boolean Ref_AccumulativeSettingReset(){
		ApplicationLauncher.logger.debug("Ref_AccSettingReset :Entry");
		boolean status=false;
		Ref_SendCommandResetSetting();
		SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRefStdRadiant.CMD_REF_STD_ACC_RESET_SETTING_ER.length(),ConstantRefStdRadiant.CMD_REF_STD_ACC_RESET_SETTING_ER);
		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("Ref_AccSettingReset:No Data Received");
		}
		ApplicationLauncher.logger.info("Ref_AccSettingReset: Exit: status: " + status );
		return status;
	}


	public boolean kiggsRefStdAccumulativeSettingMode(){
		ApplicationLauncher.logger.debug("kiggsRefStdAccumulativeSettingMode :Entry");
		boolean status=false;
		String dummyExpectedData = "";
		int dummyExpectedLength = 2;
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			ApplicationLauncher.logger.info("kiggsRefStdAccumulativeSettingMode: MSG_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU");
			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU,dummyExpectedData,dummyExpectedLength );
			status=true;

		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			ApplicationLauncher.logger.info("kiggsRefStdAccumulativeSettingMode: MSG_ACCU_SETTING_MODE_REACTIVE_ENERGY_ACCU - *****");
			kiggsRefStdCommSendDataCommand(RefStdKiggsMessage.MSG_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU,dummyExpectedData,dummyExpectedLength );
			status=true;
		}

		ApplicationLauncher.logger.info("kiggsRefStdAccumulativeSettingMode: Exit: status: " + status );
		return status;
	}



	public boolean Ref_AccumulativeStart(){
		ApplicationLauncher.logger.debug("Ref_AccumulativeStart :Entry");
		boolean status=false;
		Ref_SendCommandAccumlateStart();
		SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRefStdRadiant.CMD_REF_STD_ACC_START_ER.length(),ConstantRefStdRadiant.CMD_REF_STD_ACC_START_ER);
		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("Ref_AccumulativeStart :No Data Received");
		}

		return status;
	}





	public boolean Ref_ReadAccumalative(){
		ApplicationLauncher.logger.debug("Ref_ReadMeterReading : UI update Entry");
		// TODO
		boolean status=false;
		try {
			Ref_SendCommandReadMeterReading();
			String metertype = DisplayDataObj.getDeployedEM_ModelType();
			SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRefStdRadiant.CMD_REF_STD_READ_METER_READING_ER_LENGTH,ConstantRefStdRadiant.CMD_REF_STD_READ_METER_READING_ER);
			if(refData.IsExpectedResponseReceived()){

				refData.RefStd_DecodeSerialDataForConstTest();
				Float phaseA_kwh = Float.parseFloat(refData.getPhaseAReading())/1000;
				if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
					Float phaseB_kwh = Float.parseFloat(refData.getPhaseBReading())/1000;
					Float phaseC_kwh = Float.parseFloat(refData.getPhaseCReading())/1000;

					DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
					DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kwh));
					DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kwh));
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refData.getPhaseAReading(),
							refData.getPhaseBReading(), refData.getPhaseCReading());
				}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refData.getPhaseAReading(),"", "");
				}
				StripRefStd_SerialData(refData.getReceivedLength());
				ProjectExecutionController.UpdateRefStdProgressBar();
				status=true;
			}
			else{
				ApplicationLauncher.logger.info("Ref_ReadMeterReading:No Data Received");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("Ref_ReadMeterReading exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("Ref_ReadMeterReading : UI update Exit");

		return status;
	}

	public boolean sandsRefStdReadAccumalativeEnergyTask(){
		ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : UI update Entry");
		// TODO
		boolean status=false;
		try {
			ClearStdRefSerialData();
			SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
			status = sandsRefStdReadEnergyData();
			//Ref_SendCommandReadMeterReading();ghg
			String metertype = DisplayDataObj.getDeployedEM_ModelType();
			//SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER_LENGTH,ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER);
			//if(refData.IsExpectedResponseReceived()){
			if(status){
				//refData.RefStd_DecodeSerialDataForConstTest();
				SerialDataSandsRefStd refStdDataAccuEnergy = getLastSandsRefStdObj();
				if (refStdDataAccuEnergy!=null){
					if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
						ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Active Entry");
						Float phaseA_kwh = Float.parseFloat(refStdDataAccuEnergy.getPhaseA_ActivePowerReading());///1000;
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
							ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Active-3Phase Entry");
							Float phaseB_kwh = Float.parseFloat(refStdDataAccuEnergy.getPhaseB_ActivePowerReading());///1000;
							Float phaseC_kwh = Float.parseFloat(refStdDataAccuEnergy.getPhaseC_ActivePowerReading());///1000;

							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
							DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kwh));
							DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kwh));
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),
									refStdDataAccuEnergy.getPhaseB_ActivePowerReading(), refStdDataAccuEnergy.getPhaseC_ActivePowerReading());
						}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
							ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Active-1Phase Entry");
							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),"", "");
						}
					}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
						ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Reactive Entry");
						Float phaseA_kVarh = Float.parseFloat(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading());///1000;
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
							ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Reactive-3Phase Entry");
							Float phaseB_kVarh = Float.parseFloat(refStdDataAccuEnergy.getPhaseB_ReactivePowerReading());///1000;
							Float phaseC_kVarh = Float.parseFloat(refStdDataAccuEnergy.getPhaseC_ReactivePowerReading());///1000;

							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
							DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kVarh));
							DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kVarh));
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),
									refStdDataAccuEnergy.getPhaseB_ReactivePowerReading(), refStdDataAccuEnergy.getPhaseC_ReactivePowerReading());
						}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
							ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : Reactive-1Phase Entry");
							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
							DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),"", "");
						}
					}					


					//StripRefStd_SerialData(refData.getReceivedLength());
					ProjectExecutionController.UpdateRefStdProgressBar();
					status=true;
				}else{
					ApplicationLauncher.logger.info("sandsRefStdReadAccumalativeEnergyTask invalid read data");
					status=false;
				}
				refStdDataAccuEnergy = null;
			}
			else{
				ApplicationLauncher.logger.info("sandsRefStdReadAccumalativeEnergyTask read failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sandsRefStdReadAccumalativeEnergyTask exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("sandsRefStdReadAccumalativeEnergyTask : UI update Exit");

		return status;
	}

	public boolean refStdKreReadAccumalativeEnergyTask(){
		ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : UI update Entry");
		// TODO
		boolean status=true;
		try {
			//ClearStdRefSerialData();
			//SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
			//status = sandsRefStdReadEnergyData();
			//Ref_SendCommandReadMeterReading();ghg
			String metertype = DisplayDataObj.getDeployedEM_ModelType();
			//SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER_LENGTH,ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER);
			//if(refData.IsExpectedResponseReceived()){
			if(status){
				//refData.RefStd_DecodeSerialDataForConstTest();
				//SerialDataSandsRefStd refStdDataAccuEnergy = getLastSandsRefStdObj();
				//if (refStdDataAccuEnergy!=null){
				if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
					ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Active Entry");
					Float phaseA_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ActiveEnergy())/1000;///1000;
					ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseA_kwh: " + phaseA_kwh);
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Active-3Phase Entry");
						Float phaseB_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ActiveEnergy())/1000;///1000;
						Float phaseC_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackB_ActiveEnergy())/1000;///1000;
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseB_kwh: " + phaseB_kwh);
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseC_kwh: " + phaseC_kwh);
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
						DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kwh));
						DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kwh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),
						//		refStdDataAccuEnergy.getPhaseB_ActivePowerReading(), refStdDataAccuEnergy.getPhaseC_ActivePowerReading());
					}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Active-1Phase Entry");
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),"", "");
					}
				}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
					ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Reactive Entry");
					Float phaseA_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ReactiveEnergy())/1000;///1000;
					ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseA_kVarh: " + phaseA_kVarh);
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Reactive-3Phase Entry");
						Float phaseB_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						Float phaseC_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseB_kVarh: " + phaseB_kVarh);
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : phaseC_kVarh: " + phaseC_kVarh);
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kVarh));
						DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),
						//		refStdDataAccuEnergy.getPhaseB_ReactivePowerReading(), refStdDataAccuEnergy.getPhaseC_ReactivePowerReading());
					}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : Reactive-1Phase Entry");
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),"", "");
					}
				}					


				//StripRefStd_SerialData(refData.getReceivedLength());
				ProjectExecutionController.UpdateRefStdProgressBar();
				status=true;
				/*				}else{
					ApplicationLauncher.logger.info("refStdKreReadAccumalativeEnergyTask invalid read data");
					status=false;
				}
				refStdDataAccuEnergy = null;*/
			}
			else{
				ApplicationLauncher.logger.info("refStdKreReadAccumalativeEnergyTask read failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdKreReadAccumalativeEnergyTask exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("refStdKreReadAccumalativeEnergyTask : UI update Exit");

		return status;
	}


	public boolean refStdBofaReadAccumalativeEnergyTask(){
		ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : UI update Entry");
		// TODO
		boolean status=true;
		try {
			//ClearStdRefSerialData();
			//SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
			//status = sandsRefStdReadEnergyData();
			//Ref_SendCommandReadMeterReading();ghg
			String metertype = DisplayDataObj.getDeployedEM_ModelType();
			//SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER_LENGTH,ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER);
			//if(refData.IsExpectedResponseReceived()){
			if(status){
				//refData.RefStd_DecodeSerialDataForConstTest();
				//SerialDataSandsRefStd refStdDataAccuEnergy = getLastSandsRefStdObj();
				//if (refStdDataAccuEnergy!=null){
				if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
					ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Active Entry");
					if(!ProjectExecutionController.getFeedbackR_ActiveEnergy().isEmpty()){
						Float phaseA_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ActiveEnergy());///1000;///1000;
						ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseA_kwh: " + phaseA_kwh);
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
							ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Active-3Phase Entry");
							Float phaseB_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ActiveEnergy())/1000;///1000;
							Float phaseC_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackB_ActiveEnergy())/1000;///1000;
							ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseB_kwh: " + phaseB_kwh);
							ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseC_kwh: " + phaseC_kwh);
							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
							DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kwh));
							DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kwh));
							//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),
							//		refStdDataAccuEnergy.getPhaseB_ActivePowerReading(), refStdDataAccuEnergy.getPhaseC_ActivePowerReading());
						}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
							ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Active-1Phase Entry");
							DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
							//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),"", "");
						}
					}
				}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
					ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Reactive Entry");
					Float phaseA_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ReactiveEnergy())/1000;///1000;
					ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseA_kVarh: " + phaseA_kVarh);
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Reactive-3Phase Entry");
						Float phaseB_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						Float phaseC_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseB_kVarh: " + phaseB_kVarh);
						ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : phaseC_kVarh: " + phaseC_kVarh);
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kVarh));
						DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),
						//		refStdDataAccuEnergy.getPhaseB_ReactivePowerReading(), refStdDataAccuEnergy.getPhaseC_ReactivePowerReading());
					}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : Reactive-1Phase Entry");
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),"", "");
					}
				}					


				//StripRefStd_SerialData(refData.getReceivedLength());
				ProjectExecutionController.UpdateRefStdProgressBar();
				status=true;
				/*				}else{
					ApplicationLauncher.logger.info("refStdBofaReadAccumalativeEnergyTask invalid read data");
					status=false;
				}
				refStdDataAccuEnergy = null;*/
			}
			else{
				ApplicationLauncher.logger.info("refStdBofaReadAccumalativeEnergyTask read failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdBofaReadAccumalativeEnergyTask exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("refStdBofaReadAccumalativeEnergyTask : UI update Exit");

		return status;
	}


	public boolean kiggsRefStdReadAccumalativeEnergyTask(){
		ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : UI update Entry");
		// TODO
		boolean status=true;
		try {
			//ClearStdRefSerialData();
			//SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
			//status = sandsRefStdReadEnergyData();
			//Ref_SendCommandReadMeterReading();ghg
			String metertype = DisplayDataObj.getDeployedEM_ModelType();
			//SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER_LENGTH,ConstantRadiantRefStd.CMD_REF_STD_READ_METER_READING_ER);
			//if(refData.IsExpectedResponseReceived()){
			if(status){
				//refData.RefStd_DecodeSerialDataForConstTest();
				//SerialDataSandsRefStd refStdDataAccuEnergy = getLastSandsRefStdObj();
				//if (refStdDataAccuEnergy!=null){
				if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
					ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Active Entry");
					Float phaseA_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ActiveEnergy());///1000;
					ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseA_kwh: " + phaseA_kwh);
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Active-3Phase Entry");
						Float phaseB_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ActiveEnergy())/1000;///1000;
						Float phaseC_kwh = Float.parseFloat(ProjectExecutionController.getFeedbackB_ActiveEnergy())/1000;///1000;
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseB_kwh: " + phaseB_kwh);
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseC_kwh: " + phaseC_kwh);
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
						DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kwh));
						DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kwh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),
						//		refStdDataAccuEnergy.getPhaseB_ActivePowerReading(), refStdDataAccuEnergy.getPhaseC_ActivePowerReading());
					}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Active-1Phase Entry");
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kwh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ActivePowerReading(),"", "");
					}
				}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
					ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Reactive Entry");
					Float phaseA_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackR_ReactiveEnergy());///1000;
					ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseA_kVarh: " + phaseA_kVarh);
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Reactive-3Phase Entry");
						Float phaseB_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						Float phaseC_kVarh = Float.parseFloat(ProjectExecutionController.getFeedbackY_ReactiveEnergy())/1000;///1000;
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseB_kVarh: " + phaseB_kVarh);
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : phaseC_kVarh: " + phaseC_kVarh);
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						DeviceDataManagerController.setCurrentPhaseBReading(Float.toString(phaseB_kVarh));
						DeviceDataManagerController.setCurrentPhaseCReading(Float.toString(phaseC_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),
						//		refStdDataAccuEnergy.getPhaseB_ReactivePowerReading(), refStdDataAccuEnergy.getPhaseC_ReactivePowerReading());
					}else if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : Reactive-1Phase Entry");
						DeviceDataManagerController.setCurrentPhaseAReading(Float.toString(phaseA_kVarh));
						//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdDataAccuEnergy.getPhaseA_ReactivePowerReading(),"", "");
					}
				}					


				//StripRefStd_SerialData(refData.getReceivedLength());
				//ProjectExecutionController.UpdateRefStdProgressBar();
				status=true;
				/*				}else{
					ApplicationLauncher.logger.info("kiggsRefStdReadAccumalativeEnergyTask invalid read data");
					status=false;
				}
				refStdDataAccuEnergy = null;*/
			}
			else{
				ApplicationLauncher.logger.info("kiggsRefStdReadAccumalativeEnergyTask read failed");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("kiggsRefStdReadAccumalativeEnergyTask exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("kiggsRefStdReadAccumalativeEnergyTask : UI update Exit");

		return status;
	}


	public boolean Ref_AccumulativeStop(){
		ApplicationLauncher.logger.debug("Ref_AccumulativeStop :Entry");
		boolean status=false;
		Ref_SendCommandAccumlateStop();
		SerialDataRadiantRefStd refData = Ref_ReadAccumulativeData(ConstantRefStdRadiant.CMD_REF_STD_ACC_STOP_ER.length(),ConstantRefStdRadiant.CMD_REF_STD_ACC_STOP_ER);
		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("Ref_AccumulativeStop :No Data Received");
		}

		return status;
	}

	public boolean RefStd_ConfigureBNC_Output(){
		ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Entry");
		boolean status=false;
		Ref_SendCommandBNC_OutPut();

		//ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Test1");
		String expectedResult = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_ER;
		if(ProcalFeatureEnable.RADIANT_REF_STD_2X_FAMILY_ENABLED) {
			expectedResult = ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_ER;
		}
		SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRefStdRadiant.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResult);

		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Output :RefStd Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Output :No Data Received");
		}
		ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Output :Exit");
		return status;
	}


	public boolean kiggsRefStd_ConfigureBNC_Output(){
		ApplicationLauncher.logger.debug("kiggsRefStd_ConfigureBNC_Output :Entry");
		boolean status=true;
		kiggsRefStdSendInitCommand();
		/*
		//ApplicationLauncher.logger.debug("kiggsRefStd_ConfigureBNC_Output :Test1");
		String expectedResult = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER;
		if(ProcalFeatureEnable.RADIANT_REF_STD_2X_FAMILY_ENABLED) {
			expectedResult = ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_2XFAMILY_ER;
		}
		SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRadiantRefStd.CMD_REF_STD_BNC_OUTPUT_ER.length(),expectedResult);

		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("kiggsRefStd_ConfigureBNC_Output :RefStd Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("kiggsRefStd_ConfigureBNC_Output :No Data Received");
		}*/
		ApplicationLauncher.logger.debug("kiggsRefStd_ConfigureBNC_Output :Exit");
		return status;
	}


	public boolean kiggsRefStdStateSettingBasicMeasurement(){
		ApplicationLauncher.logger.debug("kiggsRefStdStateSettingBasicMeasurement :Entry");
		boolean status=true;
		kiggsRefStdSendCommandStateSettingBasicMeasurement();


		ApplicationLauncher.logger.debug("kiggsRefStdStateSettingBasicMeasurement :Exit");
		return status;
	}



	/*public boolean RefStd_ConfigureBNC_Constant1(){
		ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Constant :Entry");
		boolean status=false;
		//Ref_SendCommandBNC_OutPut();
		Ref_SendCommandBNC_Constant1();

		SerialDataRefStd refData = Ref_ReadBNC_Response(ConstantRefStd.CMD_REF_STD_BNC_CONSTANT_ER.length(),ConstantRefStd.CMD_REF_STD_BNC_CONSTANT_ER);

		if(refData.IsExpectedResponseReceived()){

			String CurrentRef_Data =refData.getRefStd_ReadSerialData();
			ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Constant :RefStd Received Data:"+CurrentRef_Data);
			StripRefStd_SerialData(refData.getReceivedLength());
			status=true;
		}
		else{
			ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Constant :No Data Received");
		}

		return status;
	}*/


	public boolean RefStd_ConfigureBNC_Constant(){
		ApplicationLauncher.logger.debug("RefStd_ConfigureBNC_Constant :Entry");
		boolean status=false;
		ClearStdRefSerialData();
		SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
		//Ref_SendCommandBNC_OutPut();
		try {
			Ref_SendCommandBNC_Constant1();

			SerialDataRadiantRefStd refData = Ref_ReadBNC_Response(ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_ER.length(),ConstantRefStdRadiant.CMD_REF_STD_BNC_CONSTANT_ER);

			if(refData.IsExpectedResponseReceived()){

				String CurrentRef_Data =refData.getRefStd_ReadSerialData();
				ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Constant :RefStd Received Data:"+CurrentRef_Data);
				StripRefStd_SerialData(refData.getReceivedLength());
				status=true;
			}
			else{
				ApplicationLauncher.logger.info("RefStd_ConfigureBNC_Constant :No Data Received");
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("RefStd_ConfigureBNC_Constant exception: " + e.getMessage());
		}
		return status;
	}

	public boolean sandsRefStdConfigureMode(){
		ApplicationLauncher.logger.debug("sandsRefStdConfigureMode :Entry");
		boolean status=false;
		ClearStdRefSerialData();
		SerialDataSandsRefStd.ClearRefStd_ReadSerialData();
		//Ref_SendCommandBNC_OutPut();
		try {
			sandsRefStdSetConfigTask();
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sandsRefStdConfigureMode exception: " + e.getMessage());
		}
		return status;
	}


	public boolean refStdKreConfigureMode(){
		ApplicationLauncher.logger.debug("refStdKreConfigureMode :Entry");
		boolean status=false;
		ClearStdRefSerialData();
		SerialDataRefStdKre.ClearRefStd_ReadSerialData();
		//Ref_SendCommandBNC_OutPut();
		try {
			//refStdKreSetConfigTask();
			//RefStdKreMessage.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
			if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
    			//displayDataObj.refStdEnableSerialMonitoring();
    			//DisplayDataObj.refStdEnableSerialMonitoring_V2();
				
				RefStdDirector refStdKreDirector = new RefStdDirector();
				status = refStdKreDirector.refStdWriteSettingTask();
				
    		}else {
    			status = kreRefStdWriteSettingTask();
    		}
			if(status){
				Data_RefStdKre.setLastWriteSettingIstall(Data_RefStdKre.getWriteSettingIstall());
				Data_RefStdKre.setLastWriteSettingWiringMode(Data_RefStdKre.getWriteSettingWiringMode());
				Data_RefStdKre.setLastWriteSettingOutput1PulseConstant(Data_RefStdKre.getWriteSettingOutput1PulseConstant());
				Data_RefStdKre.setLastWriteSettingOutput1PulseType(Data_RefStdKre.getWriteSettingOutput1PulseType());

			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdKreConfigureMode exception: " + e.getMessage());
		}
		return status;
	}

	public boolean pwrSrc_CommInit(String CommInput,String BaudRate){
		ApplicationLauncher.logger.debug("pwrSrc_CommInit :Entry");
		boolean status = false;
		if(!pwrSrcComSerialStatusConnected) {
			pwrSrcComSerialStatusConnected = SetSerialComm(commPowerSrc,CommInput,Integer.valueOf(BaudRate),false);

			if (pwrSrcComSerialStatusConnected){
				commPowerSrc.SetFlowControlMode();
			} else {

				ApplicationLauncher.logger.info("pwrSrc_CommInit:"+CommInput+" access failed");
			}
		}
		status = pwrSrcComSerialStatusConnected;
		return status;
	}

	public void PwrSrcON_Trigger() {
		ApplicationLauncher.logger.debug("PwrSrcON_Trigger :Entry");
		pwrSrcTimer = new Timer();
		pwrSrcTimer.schedule(new pwrSrcComSetOnTask(), 100);
		ApplicationLauncher.logger.debug("PwrSrcON_Trigger : Exit");

	}

	public void Har_PwrSrcON_Trigger() {
		ApplicationLauncher.logger.debug("Har_PwrSrcON_Trigger :Entry");
		pwrSrcTimer = new Timer();
		pwrSrcTimer.schedule(new har_pwrSrcComSetOnTask(), 100);
		ApplicationLauncher.logger.debug("Har_PwrSrcON_Trigger : Exit");

	}

	public void Cus_PwrSrcON_Trigger() {
		ApplicationLauncher.logger.debug("Cus_PwrSrcON_Trigger :Entry");
		pwrSrcTimer = new Timer();
		pwrSrcTimer.schedule(new cus_pwrSrcComSetOnTask(), 100);
		ApplicationLauncher.logger.debug("Har_PwrSrcON_Trigger : Exit");

	}

	public void PwrSrcOFF_Trigger() {
		ApplicationLauncher.logger.debug("PwrSrcOFF_Trigger :Entry");
		pwrSrcTimer = new Timer();
		pwrSrcTimer.schedule(new pwrSrcComSetOffTask(), 100);

	}

	public void PwrSrcOffTimerTrigger(Integer StopTimerInSec){
		ApplicationLauncher.logger.debug("PwrSrcOffTimerTrigger :Entry");
		PowerSourceTimerOffObj = new Timeline(new KeyFrame(Duration.seconds(StopTimerInSec), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ApplicationLauncher.logger.info("PwrSrcOffTimerTrigger: PowerSourceTimerOff invoked ");
				if (getPowerSrcTurnedOnStatus()){
					if(!isPowerSourceTurnedOff()){
						SetPowerSourceOff();
					}
				}
				PowerSourceTimerOffObj.stop();
			}
		}));
		PowerSourceTimerOffObj.setCycleCount(Timeline.INDEFINITE);
		PowerSourceTimerOffObj.play();

	}

	public boolean SetPowerSourceOff(){
		ApplicationLauncher.logger.debug("SetPowerSourceOff :Entry");
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc Off",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;
		ApplicationLauncher.logger.info("SetPowerSourceOff :getPhaseRevPowerOn():"+DisplayDataObj.getPhaseRevPowerOn());
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOff :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}
					else{
						ApplicationLauncher.logger.info("SetPowerSourceOff: ConfigProperty.CMD_PWR_SRC_PHASE_RY_NORMAL:" + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL );
						ApplicationLauncher.logger.info("SetPowerSourceOff: ConfigProperty.CMD_PWR_SRC_PHASE_RB_NORMAL " + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL );
						String SetPhaseRYData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantApp.END_CR;
						String ExpectedPhaseRYResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

						status = SendPowerDataToSerialCommPwrSrc(SetPhaseRYData, ExpectedPhaseRYResponseData);

						if(status){
							String SetPhaseRBData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantApp.END_CR;
							String ExpectedPhaseRBResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

							status = SendPowerDataToSerialCommPwrSrc(SetPhaseRBData, ExpectedPhaseRBResponseData);
							DisplayDataObj.setPhaseRevPowerOn(false);
							if(!status){
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("SetPowerSourceOff: Power Src RB phase sequence Turned OFF Failed");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_102 );

								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
							}
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SetPowerSourceOff: Power Src RY Phase sequence Turned OFF Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_101);

							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}
				}
			}
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//TBD
		}

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_OFF,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE); 
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOff :getSkipPhaseRev()2 :"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}else{
						DisplayDataObj.setPhaseRevPowerOn(false);
					}
				}
			}
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean stopResponse = false;
			//while((!status) && (retryCount>0)){
			ApplicationLauncher.logger.debug("SetPowerSourceOff : Test1");
			//if(ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED){
			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
				//ApplicationLauncher.logger.debug("SetPowerSourceOff : Sending additional command <E> to stop the source");
				WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF);
				//WriteToSerialCommPwrSrcV2(ConstantLscsPowerSource.CMD_PWR_SRC_OFF);
			}
			//}

			//stopResponse = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_OFF,ConstantLscsPowerSource.CMD_PWR_SRC_OFF_ER);
			stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
			if(stopResponse) {
				status = true;
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while((retryCount>0) && (!stopResponse)){
						Sleep(1000);
						ApplicationLauncher.logger.debug("SetPowerSourceOff : Test2: retryCount:" + retryCount);
						stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
						if(stopResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("SetPowerSourceOff : status success1");
					}
				}else {
					//ApplicationLauncher.logger.info("SetPowerSourceOff : resetting setPwrSrcInitCompleted");
					//DisplayDataObj.setPwrSrcInitCompleted(false);
					//Sleep(1000);
					ApplicationLauncher.logger.debug("SetPowerSourceOff : Test3");
					//status = lscsPowerSrcInit();
					if(status){
						ApplicationLauncher.logger.info("SetPowerSourceOff : power reset validation success");
						/*							boolean threePhase = true;
							if(threePhase) {
								Sleep(3000);
							}else {*/
						Sleep(1000);
						//}

						ApplicationLauncher.logger.debug("SetPowerSourceOff : Test4");
						status = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
						if(status) {
							//status = true;
							ApplicationLauncher.logger.info("SetPowerSourceOff : status success2");
						}
					}
					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}
		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			
			status = BofaManager.setBofaPowerSourceOff();
			
		}
		/*else if{
			bofa
		}*/

		if(status){
			setPowerSrcOnFlag(false);
			setPowerSrcTurnedOnStatus(false);
			setPowerSourceTurnedOff(true);
			ProjectExecutionController.setExecuteStopStatus(true);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOff: Power Src Turned Off");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOff: Power Src Turned OFF Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1001);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("SetPowerSourceOff: Exit");
		return status;
	}

	
	public boolean SetPowerSourceOffV2(){ // for force shut down with out monitoring getUserAbortedFlag()
		ApplicationLauncher.logger.debug("SetPowerSourceOffV2 :Entry");
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc Off",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;
		ApplicationLauncher.logger.info("SetPowerSourceOffV2 :getPhaseRevPowerOn():"+DisplayDataObj.getPhaseRevPowerOn());
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffV2 :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}
					else{
						ApplicationLauncher.logger.info("SetPowerSourceOffV2: ConfigProperty.CMD_PWR_SRC_PHASE_RY_NORMAL:" + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL );
						ApplicationLauncher.logger.info("SetPowerSourceOffV2: ConfigProperty.CMD_PWR_SRC_PHASE_RB_NORMAL " + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL );
						String SetPhaseRYData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantApp.END_CR;
						String ExpectedPhaseRYResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

						status = SendPowerDataToSerialCommPwrSrc(SetPhaseRYData, ExpectedPhaseRYResponseData);

						if(status){
							String SetPhaseRBData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantApp.END_CR;
							String ExpectedPhaseRBResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

							status = SendPowerDataToSerialCommPwrSrc(SetPhaseRBData, ExpectedPhaseRBResponseData);
							DisplayDataObj.setPhaseRevPowerOn(false);
							if(!status){
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("SetPowerSourceOffV2: Power Src RB phase sequence Turned OFF Failed");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_102 );

								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
							}
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SetPowerSourceOffV2: Power Src RY Phase sequence Turned OFF Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_101);

							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}
				}
			}
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//TBD
		}

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_OFF,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE); 
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffV2 :getSkipPhaseRev()2 :"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}else{
						DisplayDataObj.setPhaseRevPowerOn(false);
					}
				}
			}
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean stopResponse = false;
			//while((!status) && (retryCount>0)){
			ApplicationLauncher.logger.debug("SetPowerSourceOffV2 : Test1");
			//if(ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED){
			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
				//ApplicationLauncher.logger.debug("SetPowerSourceOffV2 : Sending additional command <E> to stop the source");
				WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF);
				//WriteToSerialCommPwrSrcV2(ConstantLscsPowerSource.CMD_PWR_SRC_OFF);
			}
			//}

			//stopResponse = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_OFF,ConstantLscsPowerSource.CMD_PWR_SRC_OFF_ER);
			stopResponse = lscsWriteToSerialCommPwrSrcOffV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
			if(stopResponse) {
				status = true;
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while((retryCount>0) && (!stopResponse)){
						Sleep(1000);
						ApplicationLauncher.logger.debug("SetPowerSourceOffV2 : Test2: retryCount:" + retryCount);
						stopResponse = lscsWriteToSerialCommPwrSrcOffV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
						if(stopResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("SetPowerSourceOffV2 : status success1");
					}
				}else {
					//ApplicationLauncher.logger.info("SetPowerSourceOffV2 : resetting setPwrSrcInitCompleted");
					//DisplayDataObj.setPwrSrcInitCompleted(false);
					//Sleep(1000);
					ApplicationLauncher.logger.debug("SetPowerSourceOffV2 : Test3");
					//status = lscsPowerSrcInit();
					if(status){
						ApplicationLauncher.logger.info("SetPowerSourceOffV2 : power reset validation success");
						/*							boolean threePhase = true;
							if(threePhase) {
								Sleep(3000);
							}else {*/
						Sleep(1000);
						//}

						ApplicationLauncher.logger.debug("SetPowerSourceOffV2 : Test4");
						status = lscsWriteToSerialCommPwrSrcOffV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
						if(status) {
							//status = true;
							ApplicationLauncher.logger.info("SetPowerSourceOffV2 : status success2");
						}
					}
					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}
		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			
			status = BofaManager.setBofaPowerSourceOff();
			
		}
		/*else if{
			bofa
		}*/

		if(status){
			setPowerSrcOnFlag(false);
			setPowerSrcTurnedOnStatus(false);
			setPowerSourceTurnedOff(true);
			ProjectExecutionController.setExecuteStopStatus(true);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffV2: Power Src Turned Off");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffV2: Power Src Turned OFF Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1001);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("SetPowerSourceOffV2: Exit");
		return status;
	}



	public boolean WriteToSerialCommPwrSrcV2(String Data){
		//ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 :Entry");
		ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrcV2 :Data: <" + Data+">");
		//ApplicationLauncher.logger.debug("WriteToSerialCommPwrSrc :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try {
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialPortObj.writeStringMsgToPort(Data+Terminator);
				//SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				//SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
				//SerialPortObj.ClearSerialData();
				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					Sleep(1);
					//GUIUtils.busyWaitMicros(100);
					/*if(Data.length()>1){
						Sleep(200);
					}*/
				}

			}

			//pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("WriteToSerialCommPwrSrcV2 :Exception :" + e.getMessage());
		}
		return status;
	}


	public boolean SetPowerSourceOffWithInit(){
		ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit :Entry");
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc Off",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;
		ApplicationLauncher.logger.info("SetPowerSourceOffWithInit :getPhaseRevPowerOn():"+DisplayDataObj.getPhaseRevPowerOn());
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffWithInit :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}
					else{
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: ConfigProperty.CMD_PWR_SRC_PHASE_RY_NORMAL:" + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL );
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: ConfigProperty.CMD_PWR_SRC_PHASE_RB_NORMAL " + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL );
						String SetPhaseRYData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantApp.END_CR;
						String ExpectedPhaseRYResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

						status = SendPowerDataToSerialCommPwrSrc(SetPhaseRYData, ExpectedPhaseRYResponseData);

						if(status){
							String SetPhaseRBData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantApp.END_CR;
							String ExpectedPhaseRBResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

							status = SendPowerDataToSerialCommPwrSrc(SetPhaseRBData, ExpectedPhaseRBResponseData);
							DisplayDataObj.setPhaseRevPowerOn(false);
							if(!status){
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: Power Src RB phase sequence Turned OFF Failed");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_102 );

								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
							}
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: Power Src RY Phase sequence Turned OFF Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_101);

							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}

				}
			}
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//TBD
		}

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_OFF,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE); 
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;

			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffWithInit :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}else{
						DisplayDataObj.setPhaseRevPowerOn(false);
					}
				}
			}
			//}
			boolean stopResponse = false;
			//while((!status) && (retryCount>0)){
			ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit : Test1");
			//stopResponse = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_OFF,ConstantLscsPowerSource.CMD_PWR_SRC_OFF_ER);
			stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
			if(stopResponse) {
				status = true;
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while ( (retryCount>0) && (!stopResponse)){
						Sleep(1000);
						ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit : Test2: retryCount:" + retryCount);
						stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
						if(stopResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInit : status success1");
					}
				}else {
					ApplicationLauncher.logger.info("SetPowerSourceOffWithInit : resetting setPwrSrcInitCompleted");
					DisplayDataObj.setPwrSrcInitCompleted(false);
					Sleep(1000);
					ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit : Test3");
					status = lscsPowerSrcInit();
					if(status){
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInit : power reset validation success");
						/*
								Sleep(1000);


							ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit : Test4");
							status = lscsWriteToSerialCommPwrSrcOff(ConstantLscsPowerSource.CMD_PWR_SRC_OFF,ConstantLscsPowerSource.CMD_PWR_SRC_OFF_ER);
							if(status) {

								ApplicationLauncher.logger.info("SetPowerSourceOffWithInit : status success2");
							}*/
					}
					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			setPowerSrcOnFlag(false);
			setPowerSrcTurnedOnStatus(false);
			setPowerSourceTurnedOff(true);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: Power Src Turned Off");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffWithInit: Power Src Turned OFF Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1001);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("SetPowerSourceOffWithInit: Exit");
		return status;
	}
	

	public boolean SetPowerSourceOffWithInitV2(){
		ApplicationLauncher.logger.debug("SetPowerSourceOffWithInitV2 :Entry");
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc Off",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;
		ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 :getPhaseRevPowerOn():"+DisplayDataObj.getPhaseRevPowerOn());
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}
					else{
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: ConfigProperty.CMD_PWR_SRC_PHASE_RY_NORMAL:" + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL );
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: ConfigProperty.CMD_PWR_SRC_PHASE_RB_NORMAL " + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL );
						String SetPhaseRYData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantApp.END_CR;
						String ExpectedPhaseRYResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

						status = SendPowerDataToSerialCommPwrSrc(SetPhaseRYData, ExpectedPhaseRYResponseData);

						if(status){
							String SetPhaseRBData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantApp.END_CR;
							String ExpectedPhaseRBResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_NORMAL + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

							status = SendPowerDataToSerialCommPwrSrc(SetPhaseRBData, ExpectedPhaseRBResponseData);
							DisplayDataObj.setPhaseRevPowerOn(false);
							if(!status){
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: Power Src RB phase sequence Turned OFF Failed");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_102 );

								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
							}
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: Power Src RY Phase sequence Turned OFF Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_101);

							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}

				}
			}
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//TBD
		}

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_OFF,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE); 
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;

			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				if(DisplayDataObj.getPhaseRevPowerOn()){
					ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 :getSkipPhaseRev():"+DisplayDataObj.getSkipPhaseRev());
					if(DisplayDataObj.getSkipPhaseRev()){
						DisplayDataObj.setSkipPhaseRev(false);
					}else{
						DisplayDataObj.setPhaseRevPowerOn(false);
					}
				}
			}
			//}
			boolean stopResponse = false;
			//while((!status) && (retryCount>0)){
			ApplicationLauncher.logger.debug("SetPowerSourceOffWithInitV2 : Test1");
			//status = lscsPowerSrcInit();
			status = lscsPowerSrcInitV2();
			//stopResponse = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_OFF,ConstantLscsPowerSource.CMD_PWR_SRC_OFF_ER);
			if(!status) {
				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
					WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF);
					
				}
				stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
				if(stopResponse) {
					status = true;
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while ( (retryCount>0) && (!stopResponse)){
							Sleep(1000);
							ApplicationLauncher.logger.debug("SetPowerSourceOffWithInitV2 : Test2: retryCount:" + retryCount);
							stopResponse = lscsWriteToSerialCommPwrSrcOff(ConstantPowerSourceLscs.CMD_PWR_SRC_OFF,ConstantPowerSourceLscs.CMD_PWR_SRC_OFF_ER);
							if(stopResponse) {
								status = true;
							}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 : status success1");
						}
					}/*else {
						ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 : resetting setPwrSrcInitCompleted");
						DisplayDataObj.setPwrSrcInitCompleted(false);
						Sleep(1000);
						ApplicationLauncher.logger.debug("SetPowerSourceOffWithInitV2 : Test3");
						status = lscsPowerSrcInit();
						if(status){
							ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2 : power reset validation success");

						}
						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}*/
				}
			}

			//}


		}



		if(status){
			setPowerSrcOnFlag(false);
			setPowerSrcTurnedOnStatus(false);
			setPowerSourceTurnedOff(true);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: Power Src Turned Off");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SetPowerSourceOffWithInitV2: Power Src Turned OFF Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1001);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("SetPowerSourceOffWithInitV2: Exit");
		return status;
	}



	public boolean lscsWriteToSerialCommPwrSrcOff(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOff :Entry");
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOff : Data:" +Data);
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOff : ExpectedResult:" +ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			setPowerSrcErrorResponseReceivedStatus(false);
			//SerialPortObj.writeStringMsgToPort(Data);dfd
			SerialPortObj.setExpectedResult(ExpectedResult);
			SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
			SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
			SerialPortObj.ClearSerialData();
			
			for(int i = 0; i < Data.length(); i++){
				//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
				SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
				Sleep(200);
			}

		}
		SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
		boolean threePhase = true;
		int timerValue = 30;
		if(threePhase) {
			timerValue = 60;
		}
		pwerData.SerialReponseTimerStart(SerialPortObj,timerValue);//30);//20


		status = pwerData.IsExpectedResponseReceived();
		if (!status){
			if(pwerData.IsExpectedErrorResponseReceived()){
				ApplicationLauncher.logger.info("lscsWriteToSerialCommPwrSrcOff : Unable to set the Power source Parameter:");
				setPowerSrcErrorResponseReceivedStatus(true);
			}
		}
		pwerData = null;//garbagecollector
		SerialPortObj = null;//garbagecollector
		return status;
	}
	
	
	public boolean lscsWriteToSerialCommPwrSrcOffV2(String Data,String ExpectedResult){
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOffV2 :Entry");
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOffV2 : Data:" +Data);
		ApplicationLauncher.logger.debug("lscsWriteToSerialCommPwrSrcOffV2 : ExpectedResult:" +ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			setPowerSrcErrorResponseReceivedStatus(false);
			//SerialPortObj.writeStringMsgToPort(Data);dfd
			SerialPortObj.setExpectedResult(ExpectedResult);
			SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
			SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
			SerialPortObj.ClearSerialData();
			
			for(int i = 0; i < Data.length(); i++){
				//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
				SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
				Sleep(200);
			}

		}
		SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
		boolean threePhase = true;
		int timerValue = 30;
		if(threePhase) {
			timerValue = 60;
		}
		pwerData.SerialReponseTimerStartV2(SerialPortObj,timerValue);//30);//20


		status = pwerData.IsExpectedResponseReceivedV2();
		if (!status){
			if(pwerData.IsExpectedErrorResponseReceived()){
				ApplicationLauncher.logger.info("lscsWriteToSerialCommPwrSrcOffV2 : Unable to set the Power source Parameter:");
				setPowerSrcErrorResponseReceivedStatus(true);
			}
		}
		pwerData = null;//garbagecollector
		SerialPortObj = null;//garbagecollector
		return status;
	}


	public boolean lscsSetPowerSourceMctNctMode(String mctNctMode, boolean forceSet){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode : mctNctMode: " +mctNctMode);
		ApplicationHomeController.update_left_status("Setting MCT/NCT mode",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode :Sleeping 10 sec");
			if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
				//Sleep(5000);
			}else {
				Sleep(10000);
			}
			//Sleep(5000);
			//SleepForSecondsWithAbortMonitoring(10);
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode :Sleeping awake 10 sec");
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_MAIN_CT_MODE;
			} else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_NEUTRAL_CT_MODE;
			}else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_ALL_CT_MODE_OFF_HDR;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_ALL_CT_MODE_OFF;
			}else {
				ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode :invalid mode data: mctNctMode: "+mctNctMode);
				return status;
			}
			//while((!status) && (retryCount>0)){fsdf
			if( (!ProjectExecutionController.getUserAbortedFlag()) || (forceSet) ) {
				if(forceSet) {
					modeResponse = WriteToSerialCommPwrSrcV2(command,expectedRepsonse);
				}else {
					modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
				}
				if(modeResponse) {
					status = true;
					ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode : status success1");
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while(retryCount>0){
							//Sleep(2000);
							SleepForSecondsWithAbortMonitoring(2);
							//if(!ProjectExecutionController.getUserAbortedFlag()) {
							if(forceSet) {
								modeResponse = WriteToSerialCommPwrSrcV2(command,expectedRepsonse);
							}else {
								modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
							}
								if(modeResponse) {
									status = true;
								}
							//}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode : status success2");
						}
					}else {
	
						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}
				}
				//Sleep(2000);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			
 			String circuit = "";
			int address    = ConstantPowerSourceBofa.ADDRESS_SLAVE_01;
			
			if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT)) {
				circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR;
 			} else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT)) {
 				circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR;
 			}else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF)) {
 				circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_ALL_CT_MODE_OFF_HDR;
 			}else {
				ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode :invalid mode data: mctNctMode: "+mctNctMode);
				return status;
			}
			int retryCount = 3;
			status = false;
			while ((retryCount>0) && (!status) ){
				ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode : Bofa : retryCount: "+retryCount);
				status = Data_LduBofa.bofaSendSwitchCtCircuit(address, circuit);
				retryCount--;
				Sleep(300);
			}
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode : Bofa : status: "+status);
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode : Bofa : retry Exit: ");
		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctMode: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}
		
		ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctMode: Exit");
		return status;
	}

	public boolean lscsSetPowerSourceMctNctModeV2(String mctNctMode, boolean forceSet){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctModeV2 :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2 : mctNctMode: " +mctNctMode);
		ApplicationHomeController.update_left_status("Setting MCT/NCT mode",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctModeV2 :Sleeping 10 sec");
			if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
				//Sleep(5000);
			}else {
				//Sleep(10000);
			}
			//Sleep(5000);
			//SleepForSecondsWithAbortMonitoring(10);
			ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctModeV2 :Sleeping awake 10 sec");
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDRV2;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_MAIN_CT_MODE;
			} else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDRV2;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_NEUTRAL_CT_MODE;
			}else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_ALL_CT_MODE_OFF_HDRV2;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_ALL_CT_MODE_OFF;
			}else {
				ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2 :invalid mode data: mctNctMode: "+mctNctMode);
				return status;
			}
			//while((!status) && (retryCount>0)){fsdf
			if( (!ProjectExecutionController.getUserAbortedFlag()) || (forceSet) ) {
				if(forceSet) {
					modeResponse = WriteToSerialCommPwrSrcV2(command,expectedRepsonse);
				}else {
					modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
				}
				if(modeResponse) {
					status = true;
					ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2 : status success1");
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while(retryCount>0){
							//Sleep(2000);
							SleepForSecondsWithAbortMonitoring(2);
							//if(!ProjectExecutionController.getUserAbortedFlag()) {
							if(forceSet) {
								modeResponse = WriteToSerialCommPwrSrcV2(command,expectedRepsonse);
							}else {
								modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
							}
								if(modeResponse) {
									status = true;
								}
							//}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2 : status success2");
						}
					}else {
	
						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}
				}
				//Sleep(2000);
			}
		}

		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceMctNctModeV2: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}
		
		ApplicationLauncher.logger.debug("lscsSetPowerSourceMctNctModeV2: Exit");
		return status;
	}
	

	public boolean  sendDataToConveyor(String command,String expectedRepsonse){
		boolean status = writeToSerialCommHarmonicsSourceSlave(command,expectedRepsonse);
		return status;
	}

	public void SleepForSecondsWithAbortMonitoring(int timeInSec) {
		
		while((timeInSec>0) && (!ProjectExecutionController.getUserAbortedFlag())) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
			}
			timeInSec--;
		}

	}

	public boolean lscsSetPowerSourceIctMode(boolean  modeOn){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceIctMode : modeOn: " +modeOn);
		ApplicationHomeController.update_left_status("Setting ICT mode",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){

			ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping 10 sec");
			//Sleep(10000);
			SleepForSecondsWithAbortMonitoring(10);
			ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping awake 10 sec");
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(modeOn) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_ICT_MODE_ON_HDR;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_MAIN_ICT_ON_MODE;
			} else {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_ICT_MODE_OFF_HDR;
				expectedRepsonse = ConstantPowerSourceLscs.ER_PWR_SRC_MAIN_ICT_OFF_MODE;
			}
			//while((!status) && (retryCount>0)){fsdf
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
				if(modeResponse) {
					status = true;
					ApplicationLauncher.logger.info("lscsSetPowerSourceIctMode : status success1");
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while(retryCount>0){
							//Sleep(2000);
							SleepForSecondsWithAbortMonitoring(2);
							if(!ProjectExecutionController.getUserAbortedFlag()) {
								modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
								if(modeResponse) {
									status = true;
								}
							}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("lscsSetPowerSourceIctMode : status success2");
						}
					}else {
	
						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceIctMode: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceIctMode: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode: Exit");
		return status;
	}




	public boolean lscsPowerSourceRphaseVoltIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseVoltIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_VOLT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_VOLT_INC_LEVEL1);

		return status;
	}




	public boolean lscsHarmonicsSourceSlaveSetHarmonicsNotRequired(){
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSetHarmonicsNotRequired :Entry");

		boolean status = false;
		String expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS +                                     
				ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS +  
				ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD;

		//status = lscsSendHarmonicsSourceSlaveCommand(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS_HDR,
		//											 ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS +                                     
		//											 ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS +  
		//		                                     ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD );

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
			status = lscsSendHarmonicsSourceSlaveCommand(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS_HDR, expectedData);
		}else{
			status = lscsSendPowerSourceCommandForStm32WithOutDelay(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS_HDR, expectedData);
		}
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSetHarmonicsNotRequired : Exit");
		return status;
	}


	/*	public boolean lscsHarmonicsSourceStm32SetHarmonicsNotRequired(){
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SetHarmonicsNotRequired :Entry");

		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS_HDR,
													 ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS +                                     
													 ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_DISABLE_ALL_HARMONICS +  
				                                     ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD );
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SetHarmonicsNotRequired : Exit");
		return status;
	}*/

	//=======================
	public boolean lscsHarmonicsSourceSlaveSetHarmonicsRequired(){
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSetHarmonicsRequired : Entry");

		boolean status = false;
		String expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS + 
				ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_ENABLE_ALL_HARMONICS + 
				ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD;

		//status = lscsSendHarmonicsSourceSlaveCommand(lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame(),
		//		                                     ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS + 
		//											 ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_ENABLE_ALL_HARMONICS + 
		//											 ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD );

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
			status = lscsSendHarmonicsSourceSlaveCommand(lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame(), expectedData);
		}else{
			status = lscsSendPowerSourceCommandForStm32WithOutDelay(lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame(), expectedData);
		}

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSetHarmonicsRequired : Exit");
		return status;
	}


	/*	public boolean lscsHarmonicsSourceStm32SetHarmonicsRequired(){
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SetHarmonicsRequired : Entry");

		boolean status = false;

		status = lscsSendPowerSourceCommand(lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame(),
				                                     ConstantLscsHarmonicsSourceSlave.HEADER_HARMONICS + 
													 ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_ENABLE_ALL_HARMONICS + 
													 ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD );

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SetHarmonicsRequired : Exit");
		return status;
	}*/
	//=============================================================================================================================================================

	public boolean lscsHarmonicsSourceSlaveSendHarmonicsData(){   
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendHarmonicsData : Entry");

		//	public static void sendHarmonicsDataToSlave_V1(){ 
		int i , j = 0 ;
		String frameToBeTransmitted ;
		boolean status = false ;
		//	char charToBeTransmitted ;
		String expectedData = "" ;


		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendHarmonicsData : frameNumber1: " + lscsPowerSourceHarmonicsMessage.getFrameNumber() );

		for(i = 1; i<= lscsPowerSourceHarmonicsMessage.getFrameNumber(); i++){
			ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendHarmonicsData : frameNumber: " + lscsPowerSourceHarmonicsMessage.getFrameNumber() );
			frameToBeTransmitted = "";
			frameToBeTransmitted = lscsPowerSourceHarmonicsMessage.dataFrames.get(i);

			if(frameToBeTransmitted.charAt(0)== lscsPowerSourceHarmonicsMessage.VOLTAGE_SIGNAL){              
				if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.R_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_VOLTAGE + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_V1_DATA + 
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
				else if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.Y_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_VOLTAGE + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_V2_DATA +
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
				else if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.B_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_VOLTAGE + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_V3_DATA +
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
			}
			else if(frameToBeTransmitted.charAt(0)== lscsPowerSourceHarmonicsMessage.CURRENT_SIGNAL){         
				if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.R_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_CURRENT + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_I1_DATA +
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
				else if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.Y_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_CURRENT + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_I2_DATA +
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
				else if(frameToBeTransmitted.charAt(1)== lscsPowerSourceHarmonicsMessage.B_PHASE_){
					expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_CURRENT + 
							ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_FOR_I3_DATA +
							ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;
				}
			}				
			//status = lscsSendHarmonicsSourceSlaveCommand(frameToBeTransmitted, expectedData);

			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
				status = lscsSendHarmonicsSourceSlaveCommand(frameToBeTransmitted, expectedData);
			}else{
				status = lscsSendPowerSourceCommandForStm32WithOutDelay(frameToBeTransmitted, expectedData);
				Sleep(1);
			}
		}	

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendHarmonicsData : Exit");
		return status;
	}
	//=============================================================================================================================

	public boolean lscsHarmonicsSourceSlaveSendFundFrequency(){   

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendFundFrequency : Entry");

		boolean status   = false ;
		String frequency =  "FQ,50.0";//PropertyHarmonicControllerV2.ref_comboBoxFrequency.getValue() ;///code 20240131-Gopi
		String expectedData = "" ;

		expectedData = ConstantLscsHarmonicsSourceSlave.HEADER_FREQUENCY + 
				ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;

		//status = lscsSendHarmonicsSourceSlaveCommand(frequency, expectedData);

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
			status = lscsSendHarmonicsSourceSlaveCommand(frequency, expectedData);
		}else{
			status = lscsSendPowerSourceCommandForStm32WithOutDelay(frequency, expectedData);
		}

		return status;
	}

	//========================================================================================================================	

	public boolean lscsHarmonicsSourceSlaveSendCmdTransmissionEnd(){    
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendCmdTransmissionEnd : Entry");

		boolean status = false;
		Sleep(100);// added for debugging on 29-Dec-23
		String command = ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_TRANSMISION_END_HDR; 
		String expectedResponse =  ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_SLAVE_TRANSMISION_END +
				ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendCmdTransmissionEnd : command      : " + command);
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendCmdTransmissionEnd : expectedData : " + expectedResponse);
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
			status = lscsSendHarmonicsSourceSlaveCommand(command,expectedResponse);
		}else{
			status = lscsSendPowerSourceCommandForStm32WithOutDelay(command,expectedResponse);
		}

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceSlaveSendCmdTransmissionEnd : Exit");

		return status;
	}

	/*	public boolean lscsHarmonicsSourceStm32SendCmdTransmissionEnd(){    
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SendCmdTransmissionEnd : Entry");

		boolean status = false;
		Sleep(100);// added for debugging on 29-Dec-23
		String command = ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_TRANSMISION_END_HDR; 
		String expectedResponse =  ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_SLAVE_TRANSMISION_END +
				               ConstantLscsHarmonicsSourceSlave.ACKNOWLEDGMENT_KEYWORD ;

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SendCmdTransmissionEnd : command      : " + command);
		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SendCmdTransmissionEnd : expectedData : " + expectedResponse);

		status = lscsSendPowerSourceCommand(command,expectedResponse);

		ApplicationLauncher.logger.debug("lscsHarmonicsSourceStm32SendCmdTransmissionEnd : Exit");

		return status;
	}*/
	//=======================

	public boolean lscsPowerSourceRphaseVoltDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseVoltDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_VOLT_DEC_LEVEL1);

		return status;
	}



	public boolean lscsPowerSourceRphaseCurrentIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseCurrentIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_CURRENT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_CURRENT_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceRphaseCurrentDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseCurrentDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_CURRENT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_CURRENT_DEC_LEVEL1);

		return status;
	}


	public boolean lscsPowerSourceRphaseDegreeIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseDegreeIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_DEGREE_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_DEGREE_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceRphaseDegreeDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceRphaseDegreeDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_R_PHASE_DEGREE_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_R_PHASE_DEGREE_DEC_LEVEL1);

		return status;
	}



	public boolean lscsPowerSourceYphaseVoltIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseVoltIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_VOLT_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceYphaseVoltDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseVoltDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_VOLT_DEC_LEVEL1);

		return status;
	}



	public boolean lscsPowerSourceYphaseCurrentIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseCurrentIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_CURRENT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_CURRENT_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceYphaseCurrentDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseCurrentDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_CURRENT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_CURRENT_DEC_LEVEL1);

		return status;
	}


	public boolean lscsPowerSourceYphaseDegreeIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseDegreeIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_DEGREE_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_DEGREE_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceYphaseDegreeDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceYphaseDegreeDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_Y_PHASE_DEGREE_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_Y_PHASE_DEGREE_DEC_LEVEL1);

		return status;
	}



	public boolean lscsPowerSourceBphaseVoltIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseVoltIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_VOLT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_VOLT_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceBphaseVoltDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseVoltDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_VOLT_DEC_LEVEL1);

		return status;
	}



	public boolean lscsPowerSourceBphaseCurrentIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseCurrentIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_CURRENT_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_CURRENT_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceBphaseCurrentDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseCurrentDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_CURRENT_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_CURRENT_DEC_LEVEL1);

		return status;
	}


	public boolean lscsPowerSourceBphaseDegreeIncrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseDegreeIncrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_DEGREE_INC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_DEGREE_INC_LEVEL1);

		return status;
	}

	public boolean lscsPowerSourceBphaseDegreeDecrementLevel1(){
		ApplicationLauncher.logger.debug("lscsPowerSourceBphaseDegreeDecrementLevel1 :Entry");
		boolean status = false;

		status = lscsSendPowerSourceCommand(ConstantPowerSourceLscs.CMD_PWR_SRC_B_PHASE_DEGREE_DEC_LEVEL1_HDR,
				ConstantPowerSourceLscs.ER_PWR_SRC_B_PHASE_DEGREE_DEC_LEVEL1);

		return status;
	}





	public boolean lscsSendPowerSourceCommand(String command, String expectedResponse){
		ApplicationLauncher.logger.debug("lscsSendPowerSourceCommand :Entry");
		//ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : modeOn: " +modeOn);
		ApplicationHomeController.update_left_status("Sending Data to Source",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;

		if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){

			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping 10 sec");
			//Sleep(10000);
			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping awake 10 sec");
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			status = false;

			boolean sourceResponse = false;

			sourceResponse = WriteToSerialCommPwrSrc(command,expectedResponse);
			if(sourceResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : status success1");
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : error response received");
					while((retryCount>0) && (!ProjectExecutionController.getUserAbortedFlag()) ){
						Sleep(2000);
							if(!ProjectExecutionController.getUserAbortedFlag()) {
								sourceResponse = WriteToSerialCommPwrSrc(command,expectedResponse);
								if(sourceResponse) {
									status = true;
								}
							}
						retryCount--;
					}
					if(status) {

						ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSendPowerSourceCommand: success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSendPowerSourceCommand: Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSendPowerSourceCommand: Exit");
		return status;
	}


	public boolean lscsSendPowerSourceCommandForStm32WithOutDelay(String command, String expectedResponse){
		ApplicationLauncher.logger.debug("lscsSendPowerSourceCommandForStm32WithOutDelay :Entry");
		//ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay : modeOn: " +modeOn);
		ApplicationHomeController.update_left_status("Sending Data to Source",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;

		if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){

			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping 10 sec");
			//Sleep(10000);
			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping awake 10 sec");
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			status = false;

			boolean sourceResponse = false;

			sourceResponse = WriteToSerialCommPwrSrcForStm32WithOutDelay(command,expectedResponse);
			if(sourceResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay : status success1");
			}else{
				ApplicationLauncher.logger.debug("lscsSendPowerSourceCommandForStm32WithOutDelay : getPowerSrcErrorResponseReceivedStatus: " + getPowerSrcErrorResponseReceivedStatus());
				if(getPowerSrcErrorResponseReceivedStatus()){
					ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay : error response received");
					while ((retryCount>0) && (!sourceResponse) && (!ProjectExecutionController.getUserAbortedFlag())){
						Sleep(2000);
						if(!ProjectExecutionController.getUserAbortedFlag()) {
							sourceResponse = WriteToSerialCommPwrSrcForStm32WithOutDelay(command,expectedResponse);
							if(sourceResponse) {
								status = true;
							}
						}
						retryCount--;
					}
					if(status) {

						ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay: success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSendPowerSourceCommandForStm32WithOutDelay: Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSendPowerSourceCommandForStm32WithOutDelay: Exit");
		return status;
	}







	public boolean lscsSendHarmonicsSourceSlaveCommand(String command, String expectedResponse){
		ApplicationLauncher.logger.debug("lscsSendHarmonicsSourceSlaveCommand :Entry");
		//ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : modeOn: " +modeOn);
		//ApplicationHomeController.update_left_status("Sending Data to Source",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;

		if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){

			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping 10 sec");
			//Sleep(10000);
			//ApplicationLauncher.logger.debug("lscsSetPowerSourceIctMode :Sleeping awake 10 sec");
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 3;//20
			status = false;

			boolean sourceResponse = false;

			sourceResponse = writeToSerialCommHarmonicsSourceSlave(command,expectedResponse);

			if(sourceResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand : status success1");
			}else{
				//if(getPowerSrcErrorResponseReceivedStatus()){
				ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand :  response failed");
				while( (retryCount>0 ) && (!status) && (!ProjectExecutionController.getUserAbortedFlag())) {
					ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand : retryCount: " + retryCount);
					Sleep(2000);
					if(!ProjectExecutionController.getUserAbortedFlag()) {
						sourceResponse = writeToSerialCommHarmonicsSourceSlave(command,expectedResponse);
						if(sourceResponse) {
							status = true;
						}
					}
					retryCount--;
				}
				if(status) {

					ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand : status success2");
				}
				//}else {

				//setPowerSrcErrorResponseReceivedStatus(true);
				//DisplayDataObj.setPwrSrcInitCompleted(false);
				//}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand: success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSendHarmonicsSourceSlaveCommand: Failed");
			//FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSendHarmonicsSourceSlaveCommand: Exit");
		return status;
	}
	
	
	public boolean awaitForStartCommandFromConveyor(){
		ApplicationLauncher.logger.debug("awaitForStartCommandFromConveyor :Entry");
		//ApplicationLauncher.logger.info("lscsSendPowerSourceCommand : modeOn: " +modeOn);
		//ApplicationHomeController.update_left_status("Sending Data to Source",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;
		Communicator SerialPortObj =commHarmonicsSrc;



		String expectedResult = ConstantConveyor.ER_START_CMD;
		SerialPortObj.setExpectedResult(expectedResult);
		SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
		SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
		SerialPortObj.ClearSerialData();
		
		//SerialPortObj.writeStringMsgToPort(ConstantConveyor.CMD_START_CONVEYOR);
		//ApplicationLauncher.logger.debug("awaitForStartCommandFromConveyor : getUserAbortedFlag(): " + ProjectExecutionController.getUserAbortedFlag());
		while(  (!status) && (!ProjectExecutionController.getUserAbortedFlag())) {
			//ApplicationLauncher.logger.info("awaitForStartCommandFromConveyor : while.. ");
			Sleep(1000);
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				try{
					SerialPortObj =commHarmonicsSrc;
					expectedResult = ConstantConveyor.ER_START_CMD;
					SerialPortObj.setExpectedResult(expectedResult);
					SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
					SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
					SerialDataHarmonicsSourceSlave pwerData = new SerialDataHarmonicsSourceSlave();
					pwerData.SerialReponseTimerStart(SerialPortObj,60);//30

					status = pwerData.IsExpectedResponseReceived();
					if (status){


						ApplicationLauncher.logger.info("awaitForStartCommandFromConveyor : status success1");


					}
					pwerData = null;//garbagecollector
					SerialPortObj = null;//garbagecollector
				}catch(Exception e){
					ApplicationLauncher.logger.error("awaitForStartCommandFromConveyor :Exception :" + e.getMessage());
				}

			}

		}


		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("awaitForStartCommandFromConveyor: success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("awaitForStartCommandFromConveyor: Failed");
			//FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("awaitForStartCommandFromConveyor: Exit");
		return status;
	}


	public boolean lscsCalibModeSetSourceVoltage(String selectedPhase, String voltRmsValue, String voltageTap){
		ApplicationLauncher.logger.debug("lscsCalibModeSetSourceVoltage :Entry");
		//ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage : mctNctMode: " +mctNctMode);
		ApplicationHomeController.update_left_status("Setting Voltage param",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean deviceResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX + voltRmsValue + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
				expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE;
			} else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_U2_PREFIX + voltRmsValue + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
				expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE;
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_U3_PREFIX + voltRmsValue + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
				expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE;
			}else {
				ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage :invalid Phase data: "+selectedPhase);
				return status;
			}
			ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage : command : "+command);
			//while((!status) && (retryCount>0)){fsdf
			deviceResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			if(deviceResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage : status success1");
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while( (retryCount>0) && (!ProjectExecutionController.getUserAbortedFlag()) ){
						Sleep(2000);
						if(!ProjectExecutionController.getUserAbortedFlag()) {
							deviceResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
							if(deviceResponse) {
								status = true;
							}
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage: voltage set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsCalibModeSetSourceVoltage: voltage set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_134);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsCalibModeSetSourceVoltage: Exit");
		return status;
	}

	public boolean lscsPowerSrcInit(){
		ApplicationLauncher.logger.debug("lscsPowerSrcInit: Entry");
		boolean status = false;
		if(!DisplayDataObj.isPwrSrcInitCompleted()){
			ApplicationLauncher.logger.info("lscsPowerSrcInit: Sending Password");
			//status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD,ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD_ER);
			//status = WriteToSerialCommPwrSrc(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD_ER);
			
			//status = WriteToSerialCommPwrSrcInit(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
			
			if(ProcalFeatureEnable.LSCS_MASTER_1PHASE_3PHASE_OPTION_ENABLED){
				status = WriteToSerialCommPwrSrcInit(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_INIT_PASSWORD_ER);
				if(status){
					
					String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
					ApplicationLauncher.logger.info("lscsPowerSrcInit: metertype: "+ metertype);
					if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_1PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}
					else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_3PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}else{
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_1PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}
				}
				
			}else{
				status = WriteToSerialCommPwrSrcInit(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
			}
			
			if(status){
				if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
					ApplicationLauncher.logger.info("lscsPowerSrcInit: setting App mode-calibration");
					status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_APP_MODE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_APP_MODE_ER);

				}else {
					ApplicationLauncher.logger.info("lscsPowerSrcInit: setting hyperterminal mode");
					status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_HYPERT_MODE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_HYPERT_MODE_ER);

				}
				if(status){
					ApplicationLauncher.logger.info("lscsPowerSrcInit: power source init completed");
					DisplayDataObj.setPwrSrcInitCompleted(true);
				}
			}
		}else{
			return true;
		}
		ApplicationLauncher.logger.debug("lscsPowerSrcInit: Exit");
		return status;
	}
	
	
	public boolean lscsPowerSrcInitV2(){
		ApplicationLauncher.logger.debug("lscsPowerSrcInitV2: Entry");
		boolean status = false;
		if(!DisplayDataObj.isPwrSrcInitCompleted()){
			ApplicationLauncher.logger.info("lscsPowerSrcInitV2: Sending Password");
			//status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD,ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD_ER);
			//status = WriteToSerialCommPwrSrc(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantLscsPowerSource.CMD_PWR_SRC_INIT_PASSWORD_ER);
			if(ProcalFeatureEnable.LSCS_MASTER_1PHASE_3PHASE_OPTION_ENABLED){
				status = WriteToSerialCommPwrSrcInit(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_INIT_PASSWORD_ER);
				if(status){
					
					String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
					if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_1PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}
					else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_3PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}else{
						status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_METER_TYPE_OPTION_SELECTION_1PHASE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
						
					}					
				}
				
			}else{
				status = WriteToSerialCommPwrSrcInit(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_PASSWORD_ER);
			}
			
			if(status){
				DisplayDataObj.setHardwareBootupOccured(true);
				if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
					ApplicationLauncher.logger.info("lscsPowerSrcInitV2: setting App mode-calibration");
					status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_APP_MODE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_APP_MODE_ER);

				}else {
					ApplicationLauncher.logger.info("lscsPowerSrcInitV2: setting hyperterminal mode");
					status = WriteToSerialCommPwrSrcInit(ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_HYPERT_MODE,ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_HYPERT_MODE_ER);

				}
				if(status){
					ApplicationLauncher.logger.info("lscsPowerSrcInitV2: power source init completed");
					DisplayDataObj.setPwrSrcInitCompleted(true);
				}
			}
		}//else{
		//	return true;
		//}
		ApplicationLauncher.logger.debug("lscsPowerSrcInitV2: Exit");
		return status;
	}


	public boolean lscsPowerSrcMakeAllCurrentZero(){
		ApplicationLauncher.logger.debug("lscsPowerSrcMakeAllCurrentZero: Entry");
		boolean status = false;
		//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
		//	ApplicationLauncher.logger.debug("lscsPowerSrcMakeAllCurrentZero: power source current setting to zero");
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceLscs.CMD_PWR_SRC_SET_ALL_CURRENT_ZERO,ConstantPowerSourceLscs.CMD_PWR_SRC_SET_ALL_CURRENT_ZERO_ER);
		//}
		ApplicationLauncher.logger.debug("lscsPowerSrcMakeAllCurrentZero: Exit");
		return status;
	}

	public void DisconnectPwrSrc(){
		if(pwrSrcComSerialStatusConnected){
			if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
				BofaManager.disconnectPwrSrc();
			}else{
				DisconnectSerialComm(commPowerSrc);
			}
			pwrSrcComSerialStatusConnected=false;
		}
	}

	public static boolean getPowerSrcOnFlag(){
		return PowerSrcOnFlag;
	}

	public static void setPowerSrcOnFlag(boolean status){
		PowerSrcOnFlag = status;
	}

	public void SetPowerSourceOn(){
		boolean status = false;
		ApplicationLauncher.logger.debug("SetPowerSourceOn :Entry");
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc On",ConstantApp.LEFT_STATUS_DEBUG);

		String InputVoltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String InputCurrent = DisplayDataObj.getR_PhaseOutputCurrent();
		ApplicationLauncher.logger.info("InputVoltage: "   +InputVoltage);
		ApplicationLauncher.logger.info("InputCurrent: " + InputCurrent);

		String InputPhase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String InputFrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());


		if(DisplayDataObj.getVoltageResetRequired()){
			if(!isPowerSourceTurnedOff()){
				SetPowerSourceOff();
			}
			Sleep(3000);
		}
		
		ApplicationHomeController.update_left_status("Setting 1 Phase PowerSrc On",ConstantApp.LEFT_STATUS_DEBUG);
		PerformPowerSrcReboot();
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			SendSinglePhasePwrSrcOn( InputFrequency);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			status = lscsSinglePhasePwrSrcOn( InputFrequency);

			if(status) {
				//isDataErrorAckRcvdFromLscsPowerSource();
				setPowerSrcOnFlag(true);
				setPowerSourceTurnedOff(false);
			}else {
				ApplicationLauncher.logger.debug("SetPowerSourceOn: setting error failure");
				setPowerSrcErrorResponseReceivedStatus(true);
			}
			/*			if(status) {
				isDataErrorAckRcvdFromLscsPowerSource();
			}else {

			}*/
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			
			
			status = bofaSinglePhasePwrSrcOn();//lscsSinglePhasePwrSrcOn( InputFrequency);

			if(status) {
				//isDataErrorAckRcvdFromLscsPowerSource();
				setPowerSrcOnFlag(true);
				setPowerSourceTurnedOff(false);
			}else {
				ApplicationLauncher.logger.debug("SetPowerSourceOn: setting error failure");
				setPowerSrcErrorResponseReceivedStatus(true);
			}
		}
		/*else{
			 bofa
		}*/

		ApplicationLauncher.logger.debug("SetPowerSourceOn: Exit");


	}

	private boolean bofaSinglePhasePwrSrcOn() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("bofaSinglePhasePwrSrcOn: Entry");
		boolean status = false;
		DisplayDataObj.setRefStdReadDataFlag(false);

		
		
		//if(!ProjectExecutionController.getCurrentTestPointName().contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
		if(!DisplayDataObj.isPresentTestPointContainsHarmonics()){	
			Data_PowerSourceBofa.noHarmonicsDefaultSettings();
		}
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			status = Data_PowerSourceBofa.sendParameterSettingCmdV2();
		}else {
			status = Data_PowerSourceBofa.sendParameterSettingCommand();
		}
		ApplicationLauncher.logger.debug("bofaSinglePhasePwrSrcOn: sendParameterSettingCommand Status :" + status );
		if(status){
			//Sleep(20000);
			status = Data_PowerSourceBofa.sendVoltageCurrentEnableOutputCommand();
			/*if (status) {
				if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
					//Sleep(20000);
					status = Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
				}
			}*/
		}
		DisplayDataObj.setRefStdReadDataFlag(true);
		if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
			Data_RefStdBofa.setRsmFreqReadingRequired(true);
		}
		//status = true;
		return status;
	}

	public boolean lscsSinglePhasePwrSrcOn(String InputFrequency){
		ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: Entry");
		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String currentTapRelayId = "005";
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			currentTapRelayId = DisplayDataObj.getSourceCurrentR_PhaseTapSelection();
			R_Voltage = DisplayDataObj.getR_PhaseOutputVoltageRms();
			R_Current = DisplayDataObj.getR_PhaseOutputCurrentRms();

		}else {
			currentTapRelayId = DisplayDataObj.getSourceCurrentR_PhaseTapSelection();
		}
		ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: currentTapRelayId: " + currentTapRelayId);



		ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: R_Voltage: " + R_Voltage);
		ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: R_Current: " + R_Current);
		ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: R_Phase: " + R_Phase);
		

		if(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()< 0.0f) { 
			R_Phase = String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: R_Phase2:" + R_Phase);
		}

		boolean status = false;
		/*		status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_SET,ConstantLscsPowerSource.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}*/
		//status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX,ConstantLscsPowerSource.CMD_PWR_SRC_SINGLE_PHASE_INIT_ER);
		int singlePhaseInitRetryCount = 4;//5;//getPowerSrcFreqSetFailureRetry();
		//while((!status) && (singlePhaseInitRetryCount>0)){
		
		if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
			//ApplicationLauncher.logger.debug("SetPowerSourceOff : Sending additional command <E> to stop the source");
			if(DisplayDataObj.isHardwareBootupOccured()){
				DisplayDataObj.setHardwareBootupOccured(false);
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: skipped CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX");
			}else {
				WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX);
				
			}
			
			//WriteToSerialCommPwrSrcV2(ConstantLscsPowerSource.CMD_PWR_SRC_OFF);
		}
		while( (singlePhaseInitRetryCount>0) && (!status)  && (!ProjectExecutionController.getUserAbortedFlag()) ){
			//setPowerSrcErrorResponseReceivedStatus(false);
			boolean initResponseStatus = WriteToSerialCommPwrSrc(ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX,ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_ER);
			if(initResponseStatus) {
				status = initResponseStatus;
			}else{
				Sleep(1000);
			}
			//if (status){
			//	status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_SET,ConstantLscsPowerSource.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			//}
			singlePhaseInitRetryCount--;
			//Sleep(1000);
		}

		if (status){
			String powerSourceData = "";
			setPowerSrcErrorResponseReceivedStatus(false);
			//Sleep(1000);
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseVoltage: " + DisplayDataObj.getLastSetPowerSourceRphaseVoltage());
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseCurrent: " + DisplayDataObj.getLastSetPowerSourceRphaseCurrent());
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseDegree: " + DisplayDataObj.getLastSetPowerSourceRphaseDegree());
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceCurrentTapRelayId: " + DisplayDataObj.getLastSetPowerSourceCurrentTapRelayId());
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceFrequency: " + DisplayDataObj.getLastSetPowerSourceFrequency());
			
			/*if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
				powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
			}else */
				
				
			if(!InputFrequency.equals(DisplayDataObj.getLastSetPowerSourceFrequency())) {
				
				
				
				//powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency  + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
				powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
						ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
			}else {
				
				if(!R_Voltage.equals(DisplayDataObj.getLastSetPowerSourceRphaseVoltage())) {
					ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit1 " ); 
					if(Float.parseFloat(R_Voltage)>0) {
						ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit2 " ); 
						powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
					}
				}
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: After Voltage: powerSourceData : " + powerSourceData);
				
				if(!R_Current.equals(DisplayDataObj.getLastSetPowerSourceRphaseCurrent())) {
					if(Float.parseFloat(R_Current)>0) {
						powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					}
				}
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: After Current: powerSourceData : " + powerSourceData);
				
				if(!R_Phase.equals(DisplayDataObj.getLastSetPowerSourceRphaseDegree())) {
					powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					
				}
				
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: After Phase Angle: powerSourceData : " + powerSourceData);
				if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
					if(!currentTapRelayId.equals(DisplayDataObj.getLastSetPowerSourceCurrentTapRelayId())) {
						powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId  + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
							
					}
				}
				
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: After CurrentTapRelayId: powerSourceData : " + powerSourceData);
			}
			
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: After Freq: powerSourceData : " + powerSourceData);
			
			powerSourceData = powerSourceData + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
			
/*			powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;*/
			ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: powerSourceData : <" + powerSourceData +">");
			status = WriteToSerialCommPwrSrc(powerSourceData,ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			/*			if (status){
				status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_I1_PREFIX+R_Current,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_PF1_PREFIX+R_Phase,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);

					status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_U1_PREFIX+R_Voltage,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_I1_PREFIX+R_Current,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_PF1_PREFIX+R_Phase,ConstantLscsPowerSource.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);


					if (status){
						status = WriteToSerialCommPwrSrc(ConstantLscsPowerSource.CMD_PWR_SRC_SET,ConstantLscsPowerSource.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			 */						
			if (status){
				DisplayDataObj.setLastSetPowerSourceFrequency(InputFrequency);	
				ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit3 " ); 
				if(Float.parseFloat(R_Voltage)>0) {
					ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit4 " ); 
					DisplayDataObj.setLastSetPowerSourceRphaseVoltage(R_Voltage);	
				}
				if(Float.parseFloat(R_Current)>0) {
					DisplayDataObj.setLastSetPowerSourceRphaseCurrent(R_Current);
				}
				DisplayDataObj.setLastSetPowerSourceRphaseDegree(R_Phase);	
				
				DisplayDataObj.setLastSetPowerSourceCurrentTapRelayId(currentTapRelayId);
				if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
					long now = Instant.now().toEpochMilli();
					if(!DisplayDataObj.isMetricsLogTestPointStartingAlreadyInitated()){
						
						
						DisplayDataObj.setMetricsLogTestPointStartingEpochTimeInMSec(now);
						DisplayDataObj.setMetricsLogTestPointStartingAlreadyInitated(true);
					}
					DisplayDataObj.setMetricsLogTestPointCmdSentEpochTimeInMSec(now);
					DisplayDataObj.setMetricsLogTargetVoltage(DisplayDataObj.getR_PhaseOutputVoltage());
					DisplayDataObj.setMetricsLogTargetCurrent(DisplayDataObj.getR_PhaseOutputCurrent());
					DisplayDataObj.setMetricsLogCounter(20);
					ApplicationLauncher.logger.debug("lscsSinglePhasePwrSrcOn: getPreviousMetricLogEpochTimeInMSec: " + DisplayDataObj.getMetricsLogTestPointStartingEpochTimeInMSec());
					
				}
				
				/*setPowerSrcOnFlag(true);
							ApplicationLauncher.logger.info("******************************************");
							ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: Power Src Turned On");
							ApplicationLauncher.logger.info("******************************************");
							ApplicationHomeController.update_left_status("1 Phase: PowerSrc Turned On",ConstantApp.LEFT_STATUS_DEBUG);*/
			}else{
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: SET Command Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_103A);
				setPowerSrcErrorResponseReceivedStatus(true);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}
			/*					}else{
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: PhaseRevPowerOn Failed");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1XXA);

						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
					}*/



			/*				}else{
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: R Phase Current set Failed");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_112A);
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
				}*/
			/*			}else{
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: R Phase Voltage set Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_111A);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}*/
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSinglePhasePwrSrcOn: Acknowledgement Failed");
			setPowerSrcErrorResponseReceivedStatus(true);
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_104C);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		return status;
	}

	/*	
	public void sendManualModeCommandProcessTask( String targetCommand ){
		ApplicationLauncher.logger.info("sendManualModeCommandProcessTask :Entry"  );
		//VI_StartProcessTaskTimer = new Timer();
		//VI_StartProcessTaskTimer.schedule(new VI_StartTask(CommandVI_PayLoad), 100);
		// version s3.9.5 -  added below flag - because Test point was running and not moving forward
		///*Platform.runLater(() -> {
			//DisplayDataObj.setLDU_ReadDataFlag( true);

			String payLoadInHex = GUIUtils.StringToHex(targetCommand);//ref_txtAreaTargetCommand.getText());
			ApplicationLauncher.logger.info("VI_StartProcess : payLoadInHex: " + payLoadInHex  );
			int timeDelayInMilliSec = 100;//;
			if(ref_chkBoxIncludeDelay.isSelected()){
				timeDelayInMilliSec = Integer.parseInt(ref_txtSerialTxDelay.getText());
			}
			String expectedDataInHex = GUIUtils.StringToHex("K");
			boolean isResponseExpected = ref_chBxResponseExpectedForCmd.isSelected();
			String responseStatus = SerialDM_Obj.powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			//DisplayDataObj.setLDU_ReadDataFlag( false);
			if(isResponseExpected){
				if(responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)){
					ref_txtAreaSerialData.appendText("PwrSrc-Rcvd\n");
					//if(!isHV_StopRequest()) {
						//SerialDM_Obj.VICI_ReadVI_TimerTrigger();
					//}

				}else if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){

					ApplicationLauncher.logger.info("VI Start:" + ErrorCodeMapping.ERROR_CODE_004 +": "+ErrorCodeMapping.ERROR_CODE_004_MSG);
					ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_004,ErrorCodeMapping.ERROR_CODE_004_MSG,AlertType.ERROR);
					//ApplicationLauncher.logger.info("VI Start no response received");
					//ApplicationLauncher.InformUser("Error-04","VI Start no response received",AlertType.ERROR);
					//SerialDM_Obj.disconnectPowerSource();
					//EnablePossibleClickEvents();
				}else {
					//String ExpectedError1Data = ConstantPrimaryVICI_Meter.VI_CMD_START_ERROR_RESPONSE+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
					String ExpectedError1Data = DeleteMeConstant.HV_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;

					ApplicationLauncher.logger.info("VI Start: responseStatus:" + responseStatus);
					ApplicationLauncher.logger.info("VI Start: ExpectedError1Data:" + ExpectedError1Data);
					//if(responseStatus.endsWith(ExpectedError1Data)){
					if ( (responseStatus.contains(ExpectedError1Data)) && 
						 (responseStatus.endsWith(DeleteMeConstant.HV_ER_TERMINATOR))){	
						ApplicationLauncher.logger.info("VI Start: response: "+responseStatus);
						String ErrorMsgID = ErrorCodeMapping.getErrorCodeID(responseStatus);
						String ErrorMsgDesc = ErrorCodeMapping.getErrorMsgDescription(ErrorMsgID);
						ApplicationLauncher.logger.info("VI Start:" + ErrorMsgID +": "+ErrorMsgDesc);
						ApplicationLauncher.InformUser(ErrorMsgID,ErrorMsgDesc,AlertType.ERROR);
						//ApplicationLauncher.logger.info("VI Start:");

						//EnablePossibleClickEvents();
					}
					//SerialDM_Obj.disconnectPowerSource();
				}
			}else{
				ApplicationLauncher.logger.info("sendManualModeCommandProcessTask : no response expected"  );
				//EnablePossibleClickEvents();
			}


		//return status;

	}*/


	public String propowerComputeCommandV2_process(boolean threePhaseSelected) {
		// TODO Auto-generated method stub
		//String selectedCommand = ref_cmbBxMessageType.getSelectionModel().getSelectedItem().toString();
		//String outputCommand = "";
		ApplicationLauncher.logger.debug("propowerComputeCommandV2_process : Entry" );//+ selectedCommand);
		String command = "";
		//ref_txtTxCmdSeperatorInStr.setText(GUIUtils.HexToString(ref_txtTxCmdSeperatorInHex.getText()));
		//ref_txtTxCmdTerminatorInStr.setText(GUIUtils.HexToString(ref_txtTxCmdTerminatorInHex.getText()));
		/*		switch (selectedCommand){

			case PROPOWER_V1_BOOT_INIT:

				command = ref_txInitPasswordStr.getText();
				break;

			case PROPOWER_V1_PRE_START:
				command = ConstantLscsPowerSource.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX;
				break;

			case PROPOWER_V1_APP_VB_MODE:
				command = ConstantLscsPowerSource.CMD_PWR_SRC_INIT_SET_APP_MODE;
				break;

			case PROPOWER_V1_HYPER_TERMINAL_MODE:
				command = ConstantLscsPowerSource.CMD_PWR_SRC_INIT_SET_HYPERT_MODE;
				break;

			case PROPOWER_V1_DAC_MODE:
				command = ConstantLscsPowerSource.CMD_PWR_SRC_INIT_SET_DAC_MODE;
				break;

			case PROPOWER_V1_STOP:
				command = ConstantLscsPowerSource.CMD_PWR_SRC_OFF;
				break;

			case PROPOWER_V1_MANIPULATE:*/
		boolean phaseReverse = false;
		String rPhaseVoltage = DisplayDataObj.getR_PhaseOutputVoltageRms();
		String yPhaseVoltage = DisplayDataObj.getY_PhaseOutputVoltageRms();
		String bPhaseVoltage = DisplayDataObj.getB_PhaseOutputVoltageRms();

		String rPhaseCurrent = DisplayDataObj.getR_PhaseOutputCurrentRms();
		String yPhaseCurrent = DisplayDataObj.getY_PhaseOutputCurrentRms();
		String bPhaseCurrent = DisplayDataObj.getB_PhaseOutputCurrentRms();

		String rPhaseDegree = String.valueOf(DisplayDataObj.get_PwrSrcR_PhaseDegreePhase());
		String yPhaseDegree = String.valueOf(DisplayDataObj.get_PwrSrcY_PhaseDegreePhase());
		String bPhaseDegree = String.valueOf(DisplayDataObj.get_PwrSrcB_PhaseDegreePhase());

		if(phaseReverse){//ref_chBxPhaseReversal.isSelected()){
			command = ConstantPowerSourceLscs.CMD_PWR_SRC_PHASE_REVERSE_PREFIX ;
		}

		//if(ref_ckBxRphaseVoltage.isSelected()){
		command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX + rPhaseVoltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
		//}

		//if(ref_ckBxRphaseCurrent.isSelected()){
		command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX + rPhaseCurrent + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
		//}

		//if(ref_ckBxRphasePhaseAngle.isSelected()){
		command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX + rPhaseDegree + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
		//}

		if(threePhaseSelected){
			//if(ref_ckBxYphaseVoltage.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U2_PREFIX + yPhaseVoltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}

			//if(ref_ckBxYphaseCurrent.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I2_PREFIX + yPhaseCurrent + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}

			//if(ref_ckBxYphasePhaseAngle.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF2_PREFIX + yPhaseDegree + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}


			//if(ref_ckBxBphaseVoltage.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U3_PREFIX + bPhaseVoltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}

			//if(ref_ckBxBphaseCurrent.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I3_PREFIX + bPhaseCurrent + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}

			//if(ref_ckBxBphasePhaseAngle.isSelected()){
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF3_PREFIX + bPhaseDegree + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
			//}


		}
		//if(ref_ckBxRphaseCurrentRelayId.isSelected()){
		command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX + DisplayDataObj.getSourceCurrentR_PhaseTapSelection()
		+ ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
		//}

		//if(ref_ckBxRphaseFrequency.isSelected()){
		command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX + DisplayDataObj.get_PwrSrc_Freq() + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
		//}

		if(command.equals(ConstantPowerSourceLscs.CMD_PWR_SRC_PHASE_REVERSE_PREFIX )){
			//command = command + ref_txtTxCmdSeperatorInStr.getText()  + ref_txtTxCmdTerminatorInStr.getText();
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR  + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
		}else {

			//command = command + ref_txtTxCmdTerminatorInStr.getText();
			command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
		}
		/*				
				break;


			default:
				ApplicationLauncher.logger.debug("propowerComputeCommandV2_process : default" );
				break;

		}*/
		ApplicationLauncher.logger.debug("propowerComputeCommandV2_process : command: " + command);
		String commandStr = command;
		/*		Platform.runLater(() -> {
			ref_txtAreaTargetCommand.setText(commandStr);
		});*/
		return commandStr;
	}




	public boolean lscsThreePhaseBalancedPwrSrcOn(String InputFrequency){
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Entry");



		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());
		String currentTapRelayId = "004";
		/*		R_Voltage = "30990";
		R_Current = "204843";// "56139";
		Y_Voltage = "30990";
		Y_Current = "206093";
		B_Voltage = "30990";
		B_Current = "205094";*/
		//R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());


		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: App Mode update");
			currentTapRelayId = DisplayDataObj.getSourceCurrentR_PhaseTapSelection();
			R_Voltage = DisplayDataObj.getR_PhaseOutputVoltageRms();
			Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltageRms();
			B_Voltage = DisplayDataObj.getB_PhaseOutputVoltageRms();

			R_Current = DisplayDataObj.getR_PhaseOutputCurrentRms();
			Y_Current = DisplayDataObj.getY_PhaseOutputCurrentRms();
			B_Current = DisplayDataObj.getB_PhaseOutputCurrentRms();

		}

/*		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: work around for harmonics testing");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: this must be deleted for production");			
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ApplicationLauncher.logger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			R_Voltage = "0";
			Y_Voltage = "150000";//DisplayDataObj.getY_PhaseOutputVoltageRms();
			B_Voltage = "0";

			R_Current = "0";
			Y_Current = "150000";//DisplayDataObj.getY_PhaseOutputCurrentRms();
			B_Current = "0";
		}*/

		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: currentTapRelayId: " + currentTapRelayId);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: R_Voltage: " + R_Voltage);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: R_Current: " + R_Current);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: R_Phase: " + R_Phase);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Y_Voltage: " + Y_Voltage);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Y_Current: " + Y_Current);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Y_Phase: " + Y_Phase);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: B_Voltage: " + B_Voltage);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: B_Current: " + B_Current);
		ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: B_Phase: " + B_Phase);

		/*		if(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()< 0.0f) {
			R_Phase = String.format(ConstantLscsPowerSource.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: R_Phase2:" + R_Phase);
			Y_Phase = String.format(ConstantLscsPowerSource.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Y_Phase2:" + R_Phase);
			B_Phase = String.format(ConstantLscsPowerSource.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: B_Phase2:" + R_Phase);
		}*/

		if(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()< 0.0f) { 
			R_Phase = String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: R_Phase2:" + R_Phase);
		}

		if(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase()< 0.0f) { 
			Y_Phase = String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Y_Phase2:" + Y_Phase);
		}

		if(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase()< 0.0f) { 
			B_Phase = String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase()));
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: B_Phase2:" + B_Phase);
		}

		boolean status = false;

		int initRetryCount = 4;//5;//getPowerSrcFreqSetFailureRetry();

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			//initRetryCount = 6;
		}
		
		if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
			if(DisplayDataObj.isHardwareBootupOccured()){
				DisplayDataObj.setHardwareBootupOccured(false);
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: skipped CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX");
			}else {
				WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX);
			}
		}

		//while((!status) && (singlePhaseInitRetryCount>0)){
		while((initRetryCount>0) && (!status) ){
			//setPowerSrcErrorResponseReceivedStatus(false);
			if(ProjectExecutionController.getUserAbortedFlag()){
				initRetryCount =1 ;
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getUserAbortedFlag:" + ProjectExecutionController.getUserAbortedFlag());
			}else{
				boolean initResponseStatus = WriteToSerialCommPwrSrc(ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX,ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_ER);
				if(initResponseStatus) {
					status = initResponseStatus;
					//Sleep(500);//worked good for 10mA and 25mA calibration
				}else{
					Sleep(1000);
				}
			}
			initRetryCount--;
			
			//Sleep(1000);
		}

		if (status){
			String powerSourceData = "";
			setPowerSrcErrorResponseReceivedStatus(false);
			
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseVoltage: " + DisplayDataObj.getLastSetPowerSourceRphaseVoltage());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseCurrent: " + DisplayDataObj.getLastSetPowerSourceRphaseCurrent());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseDegree: " + DisplayDataObj.getLastSetPowerSourceRphaseDegree());
			
			
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseVoltage: " + DisplayDataObj.getLastSetPowerSourceYphaseVoltage());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseCurrent: " + DisplayDataObj.getLastSetPowerSourceYphaseCurrent());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseDegree: " + DisplayDataObj.getLastSetPowerSourceYphaseDegree());
			
			
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseVoltage: " + DisplayDataObj.getLastSetPowerSourceBphaseVoltage());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseCurrent: " + DisplayDataObj.getLastSetPowerSourceBphaseCurrent());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseDegree: " + DisplayDataObj.getLastSetPowerSourceBphaseDegree());
			
			
			
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceCurrentTapRelayId: " + DisplayDataObj.getLastSetPowerSourceCurrentTapRelayId());
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceFrequency: " + DisplayDataObj.getLastSetPowerSourceFrequency());
			
			//Sleep(1000);
			if(!InputFrequency.equals(DisplayDataObj.getLastSetPowerSourceFrequency())) {
				powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +

					ConstantPowerSourceLscs.CMD_PWR_SRC_U2_PREFIX+Y_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_I2_PREFIX+Y_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_PF2_PREFIX+Y_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +

					ConstantPowerSourceLscs.CMD_PWR_SRC_U3_PREFIX+B_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_I3_PREFIX+B_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +
					ConstantPowerSourceLscs.CMD_PWR_SRC_PF3_PREFIX+B_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +

					ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId //ConstantLscsPowerSource.CMD_PWR_SRC_SELECT_CUR_RELAY_10A 
					+ ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR +


					ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;//+
					//ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
			}else {
				
				if(!R_Voltage.equals(DisplayDataObj.getLastSetPowerSourceRphaseVoltage())) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit1 " ); 
					if(Float.parseFloat(R_Voltage)>0) {
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit2 " ); 
						powerSourceData = ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX+R_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After R Phase Voltage: powerSourceData : " + powerSourceData);
				
				if(!R_Current.equals(DisplayDataObj.getLastSetPowerSourceRphaseCurrent())) {
					if(Float.parseFloat(R_Current)>0) {
						powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX+R_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After R Phase Current: powerSourceData : " + powerSourceData);
				
				if(!R_Phase.equals(DisplayDataObj.getLastSetPowerSourceRphaseDegree())) {
					powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX+R_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					
				}
				
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After R Phase-Phase Angle: powerSourceData : " + powerSourceData);
				
				
				
				
				
				
				if(!Y_Voltage.equals(DisplayDataObj.getLastSetPowerSourceYphaseVoltage())) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseVoltage: Hit1 " ); 
					if(Float.parseFloat(Y_Voltage)>0) {
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseVoltage: Hit2 " ); 
						powerSourceData = powerSourceData + ConstantPowerSourceLscs.CMD_PWR_SRC_U2_PREFIX+Y_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After Y Phase Voltage: powerSourceData : " + powerSourceData);
				
				if(!Y_Current.equals(DisplayDataObj.getLastSetPowerSourceYphaseCurrent())) {
					if(Float.parseFloat(Y_Current)>0) {
						powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_I2_PREFIX+Y_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After Y Phase Current: powerSourceData : " + powerSourceData);
				
				if(!Y_Phase.equals(DisplayDataObj.getLastSetPowerSourceYphaseDegree())) {
					powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_PF2_PREFIX+Y_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					
				}
				
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After Y Phase-Phase Angle: powerSourceData : " + powerSourceData);
				
				
				
				
				
				if(!B_Voltage.equals(DisplayDataObj.getLastSetPowerSourceBphaseVoltage())) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseVoltage: Hit1 " ); 
					if(Float.parseFloat(B_Voltage)>0) {
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseVoltage: Hit2 " ); 
						powerSourceData = powerSourceData + ConstantPowerSourceLscs.CMD_PWR_SRC_U3_PREFIX+B_Voltage + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After B Phase Voltage: powerSourceData : " + powerSourceData);
				
				if(!B_Current.equals(DisplayDataObj.getLastSetPowerSourceBphaseCurrent())) {
					if(Float.parseFloat(B_Current)>0) {
						powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_I3_PREFIX+B_Current + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					}
				}
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After B Phase Current: powerSourceData : " + powerSourceData);
				
				if(!B_Phase.equals(DisplayDataObj.getLastSetPowerSourceBphaseDegree())) {
					powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_PF3_PREFIX+B_Phase + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
					
				}
				
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After B Phase-Phase Angle: powerSourceData : " + powerSourceData);
				
				
				
				
				
				if(!currentTapRelayId.equals(DisplayDataObj.getLastSetPowerSourceCurrentTapRelayId())) {
					powerSourceData = powerSourceData +  ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX  + currentTapRelayId  + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR ;
						
				}
				
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After CurrentTapRelayId: powerSourceData : " + powerSourceData);
			}
			
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: After Freq: powerSourceData : " + powerSourceData);
			
			powerSourceData = powerSourceData + ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_TERMINATOR;
			
			
					
			
			
			//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  test1");
			if(DisplayDataObj.getPhaseRevPowerOn()){
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: Phase reverse" );
				powerSourceData =  ConstantPowerSourceLscs.CMD_PWR_SRC_PHASE_REVERSE_PREFIX + powerSourceData;
			}

			//powerSourceData = "v250000 s";
			
			//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  test2");
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
				//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  test3");
				String presentTestPointName = ProjectExecutionController.getCurrentTestPointName();
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  presentTestPointName: "+ presentTestPointName);
				if(ProjectExecutionController.isLastExecutedTestPointContainedHarmonics_Master()){//pradeep  last executed test point 
					// send slave


					//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  test4");
					//if(!presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
					if(!DisplayDataObj.isPresentTestPointContainsHarmonics()){
						//boolean slaveStatus = lscsSetDisableHarmonicsAtSlave(); 
						//if(slaveStatus){
						//lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  Master Harmonics disable append");
						powerSourceData =  ConstantPowerSourceLscs.CMD_PWR_SRC_MASTER_HARMONICS_DISABLE + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + powerSourceData;

						ProjectExecutionController.setLastExecutedTestPointContainedHarmonics_Master(false);
					}else{
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  Master Harmonics enable append1");
						powerSourceData =  ConstantPowerSourceLscs.CMD_PWR_SRC_MASTER_HARMONICS_ENABLE + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + powerSourceData;
						ProjectExecutionController.setLastExecutedTestPointContainedHarmonics_Master(true);
					}

				}else{
					//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  test5");
					//if(presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
					if(DisplayDataObj.isPresentTestPointContainsHarmonics()){
						if(ProjectExecutionController.isFirstExecutionTestPointWithHarmonics()){
							ProjectExecutionController.setFirstExecutionTestPointWithHarmonics(false);
						}else{
							ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  Master Harmonics enable append2");
							powerSourceData =  ConstantPowerSourceLscs.CMD_PWR_SRC_MASTER_HARMONICS_ENABLE + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + powerSourceData;
							//ProjectExecutionController.setLastExecutedTestPointContainedHarmonics_Master(true);
						}
						ProjectExecutionController.setLastExecutedTestPointContainedHarmonics_Master(true);
					}else if(!ProjectExecutionController.getOneTimeExecuted()){
						ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn:  Master Harmonics disable append3");
						powerSourceData =  ConstantPowerSourceLscs.CMD_PWR_SRC_MASTER_HARMONICS_DISABLE + ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + powerSourceData;
					}
				}
				//}

			}

			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: powerSourceData : <" + powerSourceData +">");
			if(ProjectExecutionController.getUserAbortedFlag()){
				
				ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getUserAbortedFlag2:" + ProjectExecutionController.getUserAbortedFlag());
			}else{
				status = WriteToSerialCommPwrSrc(powerSourceData,ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			}
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: powerSourceData sent completed:");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			if (status){
				DisplayDataObj.setLastSetPowerSourceFrequency(InputFrequency);
				
				
				//ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit3 " ); 
				if(Float.parseFloat(R_Voltage)>0) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceRphaseVoltage: Hit4 " ); 
					DisplayDataObj.setLastSetPowerSourceRphaseVoltage(R_Voltage);	
				}
				if(Float.parseFloat(R_Current)>0) {
					DisplayDataObj.setLastSetPowerSourceRphaseCurrent(R_Current);
				}
				DisplayDataObj.setLastSetPowerSourceRphaseDegree(R_Phase);	
				
				
				
				
				if(Float.parseFloat(Y_Voltage)>0) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceYphaseVoltage: Hit4 " ); 
					DisplayDataObj.setLastSetPowerSourceYphaseVoltage(Y_Voltage);	
				}
				if(Float.parseFloat(Y_Current)>0) {
					DisplayDataObj.setLastSetPowerSourceYphaseCurrent(Y_Current);
				}
				DisplayDataObj.setLastSetPowerSourceYphaseDegree(Y_Phase);	
				
				
				
				if(Float.parseFloat(B_Voltage)>0) {
					ApplicationLauncher.logger.debug("lscsThreePhaseBalancedPwrSrcOn: getLastSetPowerSourceBphaseVoltage: Hit4 " ); 
					DisplayDataObj.setLastSetPowerSourceBphaseVoltage(B_Voltage);	
				}
				if(Float.parseFloat(B_Current)>0) {
					DisplayDataObj.setLastSetPowerSourceBphaseCurrent(B_Current);
				}
				DisplayDataObj.setLastSetPowerSourceBphaseDegree(B_Phase);	
				
				
				
				
				DisplayDataObj.setLastSetPowerSourceCurrentTapRelayId(currentTapRelayId);
				
			}else{
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("lscsThreePhaseBalancedPwrSrcOn: SET Command Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_103A);
				setPowerSrcErrorResponseReceivedStatus(true);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}


		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsThreePhaseBalancedPwrSrcOn: Acknowledgement Failed");
			setPowerSrcErrorResponseReceivedStatus(true);
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_104C);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		return status;
	}


	public void SendSinglePhasePwrSrcOn(String InputFrequency){
		ApplicationLauncher.logger.debug("SendSinglePhasePwrSrcOn: Entry");
		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());



		ApplicationLauncher.logger.debug("SendSinglePhasePwrSrcOn: R_Voltage" + R_Voltage);
		ApplicationLauncher.logger.debug("SendSinglePhasePwrSrcOn: R_Current" + R_Current);
		ApplicationLauncher.logger.debug("SendSinglePhasePwrSrcOn: R_Phase" + R_Phase);

		boolean status = false;
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		if (status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+R_Voltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+R_Current,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+R_Phase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);

					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+R_Voltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+R_Current,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+R_Phase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);


					if (status){
						status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
						if (status){
							setPowerSrcOnFlag(true);
							setPowerSourceTurnedOff(false);
							ApplicationLauncher.logger.info("******************************************");
							ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: Power Src Turned On");
							ApplicationLauncher.logger.info("******************************************");
							ApplicationHomeController.update_left_status("1 Phase: PowerSrc Turned On",ConstantApp.LEFT_STATUS_DEBUG);
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: SET Command Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_103A);

							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}else{
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: PhaseRevPowerOn Failed");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1XXA);

						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
					}



				}else{
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: R Phase Current set Failed");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_112A);
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
				}
			}else{
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: R Phase Voltage set Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_111A);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SendSinglePhasePwrSrcOn: Frequency SET Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_104B);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}


	}



	public void SetThreePhasePowerSrcOn(){
		ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn :Entry");
		ApplicationHomeController.update_left_status("Setting 3 Phase PowerSrc",ConstantApp.LEFT_STATUS_DEBUG);

		String InputVoltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String InputCurrent = DisplayDataObj.getR_PhaseOutputCurrent();
		ApplicationLauncher.logger.info("InputVoltage: "   +InputVoltage);
		ApplicationLauncher.logger.info("InputCurrent: " + InputCurrent);

		String InputPhase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String InputFrequency = String.valueOf( DeviceDataManagerController.get_PwrSrc_Freq());

		if(DisplayDataObj.getVoltageResetRequired()){
			ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn :Source Off1");
			if(!isPowerSourceTurnedOff()){
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn :Source Off2");
					int retryCountMax = 4;//3;
					int executeCount =0;
					boolean status = false;
					while ((!status) && (executeCount!=retryCountMax) ){
						ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn :Source Off2: executeCount: " + executeCount);
						status = SetPowerSourceOff();
						executeCount++;
					}
				}else {
					ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn :Source Off3");
					SetPowerSourceOff();
				}
			}
			Sleep(3000);
		}
		PerformPowerSrcReboot();
		//SendPwrSrcOn_Balanced(InputFrequency);    

		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			SendPwrSrcOn_Balanced(InputFrequency);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			boolean  status = false;
			status = lscsThreePhaseBalancedPwrSrcOn( InputFrequency);
			if(status) {
				//isDataErrorAckRcvdFromLscsPowerSource();
				setPowerSrcOnFlag(true);
				setPowerSourceTurnedOff(false);
			}else {
				ApplicationLauncher.logger.debug("SetThreePhasePowerSrcOn: setting error failure");
				setPowerSrcErrorResponseReceivedStatus(true);
			}
		}


		ApplicationLauncher.logger.debug("SetPowerSourceOn: Exit");

	}

	public void PerformPowerSrcReboot(){
		//ApplicationLauncher.logger.info("PowerSrcSoftwareReboot: getCurrentTestType():"+ProjectExecutionController.getCurrentTestType());
		if(ProcalFeatureEnable.POWER_SRC_SW_REBOOT_ENABLE_FEATURE){
			if(ConstantPowerSourceMte.POWER_RESET_TEST_TYPES.contains( ProjectExecutionController.getCurrentTestType() ) ){
				PowerSrcSoftwareReboot();
				//Sleep(MyProperty.BOOT_UP_WAIT_TIME);
			}
		}
	}

	public boolean PowerSrcSoftwareReboot(){
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			ApplicationLauncher.logger.info("PowerSrcSoftwareReboot: Entry");
			boolean status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_RESET,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			ApplicationLauncher.logger.debug("PowerSrcSoftwareReboot: Entering Sleep for BOOT_UP_WAIT_TIME:"+ConstantAppConfig.POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC);
			Sleep((int)ConstantAppConfig.POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC);
			ApplicationLauncher.logger.debug("PowerSrcSoftwareReboot: ClearSerialData in PowerSource");
			commPowerSrc.ClearSerialData();
			ApplicationLauncher.logger.debug("PowerSrcSoftwareReboot: awake from Sleep for BOOT_UP_WAIT_TIME:"+ConstantAppConfig.POWER_SRC_REBOOT_UP_WAIT_TIME_IN_MSEC);
			return status;
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			return true;
		}else {
			return true;
		}
	}

	public int getPowerSrcFreqSetFailureRetry(){
		return PowerSrcFreqSetFailureRetry;
	}

	public void SendPwrSrcOn_Balanced(String InputFrequency){
		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());

		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: Entry");
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: R_Voltage" + R_Voltage);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: R_Current" + R_Current);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: R_Phase" + R_Phase);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: Y_Voltage" + Y_Voltage);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: Y_Current" + Y_Current);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: Y_Phase" + Y_Phase);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: B_Voltage" + B_Voltage);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: B_Current" + B_Current);
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Balanced: B_Phase" + B_Phase);
		boolean status = false;
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		int FreqFailureRetry = getPowerSrcFreqSetFailureRetry();
		while((!status) && (FreqFailureRetry>0)){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
			}
			FreqFailureRetry--;
		}
		if (status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+R_Voltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+R_Current,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+R_Phase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					if (status){
						status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U2_PREFIX+Y_Voltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
						if (status){
							status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I2_PREFIX+Y_Current,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
							if (status){
								status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF2_PREFIX+Y_Phase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
								if (status){
									status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U3_PREFIX+B_Voltage,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
									if (status){
										status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I3_PREFIX+B_Current,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
										if (status){
											status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF3_PREFIX+B_Phase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
											if (status){
												ApplicationLauncher.logger.info("DisplayDataObj.getPhaseRevPowerOn()" + DisplayDataObj.getPhaseRevPowerOn());

												if(DisplayDataObj.getPhaseRevPowerOn()){
													status = phaserev_on();
												}
												if (status){
													status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
													if (status){
														setPowerSrcOnFlag(true);
														setPowerSourceTurnedOff(false);
														ApplicationLauncher.logger.info("******************************************");
														ApplicationLauncher.logger.info("3 Phase: SendPwrSrcOn_Balanced: Power Src Turned On");
														ApplicationLauncher.logger.info("******************************************");
														ApplicationHomeController.update_left_status("3 Phase: PowerSrc Turned On",ConstantApp.LEFT_STATUS_DEBUG);
													}else{
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("3 phase: SendPwrSrcOn_Balanced: SET Command Failed");
														FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_103);

														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
														ApplicationLauncher.logger.info("*************************************");
													}
												}else{
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("3 Phase: SendPwrSrcOn_Balanced: PhaseRevPowerOn Failed");
													FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1XX);

													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
													ApplicationLauncher.logger.info("*************************************");
												}

											}else{
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("3 Phase : SendPwrSrcOn_Balanced: B Phase Degree set Failed");
												FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_133);

												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
												ApplicationLauncher.logger.info("*************************************");
											}
										}else{
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: B Phase Current set Failed");
											FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_132);
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
											ApplicationLauncher.logger.info("*************************************");
										}
									}else{
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: B Phase Voltage set Failed");
										FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_131);
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
										ApplicationLauncher.logger.info("*************************************");
									}
								}else{
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: Y Phase Degree set Failed");
									FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_123);
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
									ApplicationLauncher.logger.info("*************************************");
								}
							}else{
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: Y Phase Current set Failed");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_122);
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
								ApplicationLauncher.logger.info("*************************************");
							}
						}else{
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: Y Phase Voltage set Failed");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_121);
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
							ApplicationLauncher.logger.info("*************************************");
						}
					}else{
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: R Phase Degree set Failed");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_113);
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
						ApplicationLauncher.logger.info("*************************************");
					}

				}else{
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: R Phase Current set Failed");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_112);
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
					ApplicationLauncher.logger.info("*************************************");
				}
			}else{
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: R Phase Voltage set Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_111);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("SendPwrSrcOn_Balanced: Frequency SET Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_104A);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}
	}



	public boolean phaserev_on(){
		ApplicationLauncher.logger.info("phaserev_on: Entry");
		ApplicationLauncher.logger.info("phaserev_on: ConfigProperty.CMD_PWR_SRC_PHASE_RY_PHASEREV:" + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_PHASEREV );
		ApplicationLauncher.logger.info("phaserev_on: ConfigProperty.CMD_PWR_SRC_PHASE_RB_PHASEREV " + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_PHASEREV );

		String SetPhaseRYData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_PHASEREV + ConstantApp.END_CR;
		String ExpectedPhaseRYResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RY_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RY_PHASEREV + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

		boolean status = SendPowerDataToSerialCommPwrSrc(SetPhaseRYData, ExpectedPhaseRYResponseData);

		if(status){
			String SetPhaseRBData = ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_PHASEREV + ConstantApp.END_CR;
			String ExpectedPhaseRBResponseData=ConstantPowerSourceMte.CMD_PWR_SRC_PHASE_RB_PREFIX + ConstantAppConfig.CMD_PWR_SRC_PHASE_RB_PHASEREV + ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE+ ConstantApp.END_CR;

			status = SendPowerDataToSerialCommPwrSrc(SetPhaseRBData, ExpectedPhaseRBResponseData);
			if(!status){
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("phaserev_on: RB Phase sequence Failed");
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_106);
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
				ApplicationLauncher.logger.info("*************************************");
			}
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("phaserev_on: RY Phase sequence Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_106A);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}
		return status;
	}

	public void SendPwrSrcOn_Unbalanced(String InputCurrent, String InputPhase, String InputFrequency){
		String InputVoltage1 = DisplayDataObj.getR_PhaseOutputVoltage();
		String InputVoltage2 = DisplayDataObj.getY_PhaseOutputVoltage();
		String InputVoltage3 = DisplayDataObj.getB_PhaseOutputVoltage();

		ApplicationLauncher.logger.debug("SendPwrSrcOn_Unbalanced: InputVoltage1: " +InputVoltage1 );
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Unbalanced: InputVoltage2: " +InputVoltage2 );
		ApplicationLauncher.logger.debug("SendPwrSrcOn_Unbalanced: InputVoltage3: " +InputVoltage3 );
		boolean status = false;
		status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U1_PREFIX+InputVoltage1,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
		if (status){
			status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I1_PREFIX+InputCurrent,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
			if (status){
				status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF1_PREFIX+InputPhase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
				if (status){
					status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U2_PREFIX+InputVoltage2,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
					if (status){
						status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I2_PREFIX+InputCurrent,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
						if (status){
							status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF2_PREFIX+InputPhase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
							if (status){
								status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_U3_PREFIX+InputVoltage3,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
								if (status){
									status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_I3_PREFIX+InputCurrent,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
									if (status){
										status =WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_PF3_PREFIX+InputPhase,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
										if (status){
											status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_FRQ_PREFIX+InputFrequency,ConstantPowerSourceMte.CMD_PWR_SRC_DATA_EXPECTED_RESPONSE);
											if (status){
												status = WriteToSerialCommPwrSrc(ConstantPowerSourceMte.CMD_PWR_SRC_SET,ConstantPowerSourceMte.CMD_PWR_SRC_SET_EXPECTED_RESPONSE);
												setPowerSrcOnFlag(true);
												setPowerSourceTurnedOff(false);
												ApplicationLauncher.logger.info("********************************************");
												ApplicationLauncher.logger.info("SendPwrSrcOn_Unbalanced: Power Src Turned On");
												ApplicationLauncher.logger.info("********************************************");

											}
										}
									}
								}
							}
						}
					}

				}
			}
		}

	}



	public boolean getLDU_ResetErrorStatus(){
		return LDU_ResetErrorStatus;
	}
	public void setLDU_ResetErrorStatus(boolean status){
		LDU_ResetErrorStatus=status;
	}

	public boolean getLDU_ResetSettingStatus(){
		return LDU_ResetSettingStatus;
	}
	public void setLDU_ResetSettingStatus(boolean status){
		ApplicationLauncher.logger.debug("setLDU_ResetSettingStatus:status:"+ status);

		LDU_ResetSettingStatus=status;
	}

	public void setLDU_InfSettingStatus(boolean status){
		LDU_AccuracySettingStatus=status;
	}

	public boolean getLDU_CreepSettingStatus(){
		return LDU_CreepSettingStatus;
	}

	public void setLDU_CreepSettingStatus(boolean status){
		LDU_CreepSettingStatus=status;
	}

	public boolean getLDU_ConstSettingStatus(){
		return LDU_ConstSettingStatus;
	}

	public void setLDU_ConstSettingStatus(boolean status){
		LDU_ConstSettingStatus=status;
	}

	public boolean getLDU_STASettingStatus(){
		return LDU_STASettingStatus;
	}

	public void setLDU_STASettingStatus(boolean status){
		LDU_STASettingStatus=status;
	}

	public boolean RefStd_SetVoltageRelay(String Voltage) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStd_SetVoltageRelay :Entry");
		boolean Status = false;
		float InputVoltage =  Float.parseFloat(Voltage);
		if((InputVoltage > 0) && (InputVoltage <= 120)){
			ApplicationLauncher.logger.info("RefStd_SetVoltageRelay :120V");
			Status = SendRefStdRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY120,ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputVoltage > 120) && (InputVoltage <= 240)){
			ApplicationLauncher.logger.info("RefStd_SetVoltageRelay :240V");
			Status = SendRefStdRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY240,ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputVoltage > 240) && (InputVoltage <= 480)){
			ApplicationLauncher.logger.info("RefStd_SetVoltageRelay :480V");
			Status = SendRefStdRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY480,ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY_EXPECTED_LENGTH);
		}
		return Status;

	}

	public boolean RefStd_SetCurrentRelay(String Current) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :Entry");
		boolean Status = false;
		float InputCurrent =  Float.parseFloat(Current);
		if((InputCurrent > 0) && (InputCurrent <= 0.0328)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent1: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_0_0328,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 0.0329) && (InputCurrent <= 0.0656)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent2: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_0_0656,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 0.0657) && (InputCurrent <= 0.1312)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent3: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_0_1312,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 0.1313) && (InputCurrent <= 0.2624)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent4: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_0_2624,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 0.2625) && (InputCurrent <= 0.5248)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent5: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_0_5248,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 0.5249) && (InputCurrent <= 1.0496)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent6: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_1_0496,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 1.0497) && (InputCurrent <= 2.0992)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent7: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_2_0992,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 2.0993) && (InputCurrent <= 4.1984)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent8: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_4_1984,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 4.1985) && (InputCurrent <= 8.3968)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent9: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_8_3968,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 8.3969) && (InputCurrent <= 16.7936)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent10: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_16_7936,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 16.7937) && (InputCurrent <= 33.5872)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent11: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_33_5872,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 33.5873) && (InputCurrent <= 67.1744)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent12: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_67_1744,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}
		else if((InputCurrent >= 67.1745) && (InputCurrent <= 200)){
			ApplicationLauncher.logger.debug("RefStd_SetCurrentRelay :InputCurrent13: " + InputCurrent);
			Status = SendRefStdCurrentRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_200,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		}


		return Status;

	}

	public boolean SendRefStdCurrentRelayTapCommand(String cmd, int expected_length){
		boolean status = false;
		RefStdCommSendCommand(cmd, expected_length);
		SerialDataRadiantRefStd refStdData =RefStdReadData();
		String CurrentReceivedData = refStdData.getRefStd_ReadSerialData();
		if (CurrentReceivedData.length() == expected_length){
			ApplicationLauncher.logger.debug("SendRefStdCurrentRelayTapCommand: Expected length Received:CurrentReceivedData:"+CurrentReceivedData);
			StripRefStd_SerialData(CurrentReceivedData.length());
			refStdData.ClearRefStd_ReadSerialData();
			status = true;
		}else{
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("SendRefStdCurrentRelayTapCommand: UnExpected length Received: Expected:"+ expected_length +":Actual:"+CurrentReceivedData.length());
			ApplicationLauncher.logger.info("SendRefStdCurrentRelayTapCommand: Data Received: CurrentReceivedData:"+ CurrentReceivedData);
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			StripRefStd_SerialData(CurrentReceivedData.length());
		}

		return status;
	}

	public boolean SendRefStdRelayTapCommand(String cmd, int expected_length){
		boolean status = false;
		RefStdCommSendCommand(cmd, expected_length);
		SerialDataRadiantRefStd refStdData =RefStdReadData();
		String CurrentReceivedData = refStdData.getRefStd_ReadSerialData();
		if (CurrentReceivedData.length() == expected_length){
			ApplicationLauncher.logger.debug("SendRefStdRelayTapCommand: Expected length Received:CurrentReceivedData:"+CurrentReceivedData);
			StripRefStd_SerialData(CurrentReceivedData.length());
			status = true;
		}else{
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("SendRefStdRelayTapCommand: UnExpected length Received: Expected:"+ expected_length +":Actual:"+CurrentReceivedData.length());
			ApplicationLauncher.logger.info("SendRefStdRelayTapCommand: Data Received: CurrentReceivedData:"+ CurrentReceivedData);
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			ApplicationLauncher.logger.info("**********************************************************");
			StripRefStd_SerialData(CurrentReceivedData.length());
		}

		return status;
	}

	public String RefStd_GetRSSPulseRate(String Current){
		ApplicationLauncher.logger.debug("RefStd_GetRSSPulseRate :Entry");
		float InputCurrent =  Float.parseFloat(Current);
		ApplicationLauncher.logger.debug("RefStd_GetRSSPulseRate :InputCurrent" + InputCurrent);
		JSONArray const_values_arr = getconst_values_arr();
		String tap_name = "";
		String RSSPulseRate = "";
		if((InputCurrent > 0) && (InputCurrent <= 0.0328)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(0);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 0.0329) && (InputCurrent <= 0.0656)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(1);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 0.0657) && (InputCurrent <= 0.1312)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(2);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 0.1313) && (InputCurrent <= 0.2624)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(3);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 0.2625) && (InputCurrent <= 0.5248)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(4);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 0.5249) && (InputCurrent <= 1.0496)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(5);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 1.0497) && (InputCurrent <= 2.0992)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(6);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 2.0993) && (InputCurrent <= 4.1984)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(7);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 4.1985) && (InputCurrent <= 8.3968)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(8);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 8.3969) && (InputCurrent <= 16.7936)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(9);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 16.7937) && (InputCurrent <= 33.5872)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(10);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 33.5873) && (InputCurrent <= 67.1744)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(11);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		else if((InputCurrent >= 67.1745) && (InputCurrent <= 200)){
			tap_name = ConstantRefStdRadiant.REF_STD_TAP_NAMES.get(12);
			RSSPulseRate = GetRSS_Value(tap_name, const_values_arr);
		}
		ApplicationLauncher.logger.debug("RefStd_GetRSSPulseRate :RSSPulseRate: " + RSSPulseRate);

		return RSSPulseRate;

	}

	public JSONArray getconst_values_arr(){
		String meter_type = DisplayDataObj.getDeployedEM_ModelType();
		JSONObject result = MySQL_Controller.sp_getref_std_const(meter_type);
		JSONArray const_values_arr = new JSONArray();
		try {
			const_values_arr = result.getJSONArray("Const_Values");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getconst_values_arr : JSONException: " + e.getMessage());
		}
		return const_values_arr;
	}

	public String GetRSS_Value(String req_tap, JSONArray const_arr){
		String const_value = "";
		JSONObject jobj = new JSONObject();
		String tap_name ="";
		for(int i=0; i<const_arr.length(); i++){
			//JSONObject jobj;
			try {
				jobj = const_arr.getJSONObject(i);
				tap_name = jobj.getString("tap_name");
				if(req_tap.equals(tap_name)){
					const_value = jobj.getString("const_value");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("GetRSS_Value : JSONException: " + e.getMessage());
			}
		}
		if(!const_value.equals("")){
			const_value = GuiUtils.FormatPulseRate(const_value);
		}
		return const_value;
	}

	public boolean RefStd_UnlockVoltageRelay() throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStd_UnlockVoltageRelay :Entry");
		boolean Status = false;
		Status = SendRefStdRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_UNLOCK_VOLT_RELAY,ConstantRefStdRadiant.CMD_REF_STD_VOLT_RELAY_EXPECTED_LENGTH);
		return Status;
	}
	public boolean RefStd_UnlockCurrentRelay() throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("RefStd_UnlockCurrentRelay :Entry");
		boolean Status = false;
		Status = SendRefStdRelayTapCommand(ConstantRefStdRadiant.CMD_REF_STD_UNLOCK_CURRENT_RELAY,ConstantRefStdRadiant.CMD_REF_STD_CURRENT_RELAY_EXPECTED_LENGTH);
		return Status;
	}	

	class RefComRemindTask extends TimerTask {
		public void run() {
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected && (!ProjectExecutionController.getUserAbortedFlag())){
				try {
					if(RefStdComSemlock){
						RefStdComSemlock = false;
						//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");
						if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
							ReadRefStdData();
						}else if (ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
							sandsReadRefStdData();
						}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
							// not required here// already initiated at ValidateAllSerialPortCommand
						}else if (ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED) {
							ReadRefStdData();
						}


						RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("RefComRemindTask :Exception1:"+ e1.getMessage());

				}
				if(!ProjectExecutionController.getUserAbortedFlag()) {
					RefStdTimer.schedule(new RefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);
				}


			}
			else{

				ApplicationLauncher.logger.debug("RefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}



	class mteRefComRemindTask extends TimerTask {
		public void run() {
			//ApplicationLauncher.logger.info("RefComRemindTask: Entry");
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected){
				try {
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock:" + RefStdComSemlock);
					//if(RefStdComSemlock){
					RefStdComSemlock = false;
					//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");

					//mteReadRefStdData();

					mteReadRefStdAllData();

					RefStdComSemlock = true;
					Sleep(200);// added delay to allow the Ref std BNC Constant task 
					//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					//}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("mteRefComRemindTask :Exception1:"+ e1.getMessage());

				}
				RefStdTimer.schedule(new mteRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);


			}
			else{

				ApplicationLauncher.logger.debug("mteRefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}



	class lscsPowerSourceReadFeedBackTimerTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerTask: Entry : " + pwrSrcComSerialStatusConnected);
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.isPowerSrcReadFeedbackData() && pwrSrcComSerialStatusConnected && (!ProjectExecutionController.getUserAbortedFlag())){
				try {
					ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerTask: Entry2");
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock:" + RefStdComSemlock);
					//if(RefStdComSemlock){
					powerSrcComSemlock = false;


					lscsPowerSourceReadFeedBackAllData();

					powerSrcComSemlock = true;
					Sleep(200);// added delay to allow the Ref std BNC Constant task 
					//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					//}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("lscsPowerSourceReadFeedBackTimerTask :Exception1:"+ e1.getMessage());

				}
				if(!ProjectExecutionController.getUserAbortedFlag()) {
					lscsPowerSrcReadFeedBackTimer.schedule(new lscsPowerSourceReadFeedBackTimerTask(), SerialLscsPowerSrcInstantMetricsRefreshTimeInMsec);
				}

			}
			else{

				ApplicationLauncher.logger.debug("lscsPowerSourceReadFeedBackTimerTask :Timer Exit!%n");
				lscsPowerSrcReadFeedBackTimer.cancel(); //Terminate the timer thread



			}
			ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerTask: Exit");
		}
	}

	class KreRefComRemindTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("RefComRemindTask: Entry");
			ApplicationLauncher.logger.info("RefComRemindTask: Entry: SerialRefComInstantMetricsRefreshTimeInMsec: " + SerialRefComInstantMetricsRefreshTimeInMsec);
			ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected && (!ProjectExecutionController.getUserAbortedFlag())){
				try {
					ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock: trying for RefStdComSemlock: acquiring" );
					if(RefStdComSemlock){
						RefStdComSemlock = false;
						ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");

						//mteReadRefStdData();
						if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
							
							RefStdDirector refStdKreDirector = new RefStdDirector();
		    				refStdKreDirector.refStdReadAllData();
							
						}else {
							kreReadRefStdAllData();
						}

						RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					}else{
						ApplicationLauncher.logger.debug("RefComRemindTask: sem already locked:");
					}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("KreRefComRemindTask :Exception1:"+ e1.getMessage());

				}
				if(!ProjectExecutionController.getUserAbortedFlag()) {
					if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
						RefStdTimer.schedule(new KreRefComRemindTask(), 10);//100);//500//SerialRefComKreInstantMetricsRefreshTimeInMsec);
						
					}else{
						RefStdTimer.schedule(new KreRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("KreRefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
			ApplicationLauncher.logger.debug("RefComRemindTask: Exit:");
		}
		
		
	}

/*	class BofaRefComRemindTask extends TimerTask {
		public void run() {
			//ApplicationLauncher.logger.info("RefComRemindTask: Entry");
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected){fbxvdc
				try {
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock: trying for RefStdComSemlock: acquiring" );
					if(RefStdComSemlock){
						RefStdComSemlock = false;
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");

						//mteReadRefStdData();

						//kreReadRefStdAllData();sdfdsc
						Data_RefStdBofa.sendActualOutputValueReadingCommand();

						RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("BofaRefComRemindTask :Exception1:"+ e1.getMessage());

				}
				RefStdTimer.schedule(new BofaRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);


			}
			else{

				ApplicationLauncher.logger.debug("BofaRefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}*/


	class KiggsRefComRemindTask extends TimerTask {
		public void run() {
			//ApplicationLauncher.logger.info("RefComRemindTask: Entry");
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected && (!ProjectExecutionController.getUserAbortedFlag())){
				try {
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock: trying for RefStdComSemlock: acquiring" );
					if(RefStdComSemlock){
						RefStdComSemlock = false;
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");

						//mteReadRefStdData();

						kiggsReadRefStdAllData();

						RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("KreRefComRemindTask :Exception1:"+ e1.getMessage());

				}
				RefStdTimer.schedule(new KiggsRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);


			}
			else{

				ApplicationLauncher.logger.debug("KreRefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}



	class KiggsRefComRemindTaskDebug extends TimerTask {
		public void run() {
			//ApplicationLauncher.logger.info("RefComRemindTask: Entry");
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			if (DisplayDataObj.getRefStdReadDataFlag() && refComSerialStatusConnected && (!ProjectExecutionController.getUserAbortedFlag())){
				try {
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock: trying for RefStdComSemlock: acquiring" );
					if(RefStdComSemlock){
						RefStdComSemlock = false;
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Acquired");

						//mteReadRefStdData();

						//kiggsReadRefStdAllData();
						kiggsReadRefStdAllDataDebug();
						RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("KiggsRefComRemindTaskDebug :Exception1:"+ e1.getMessage());

				}
				RefStdTimer.schedule(new KiggsRefComRemindTaskDebug(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);


			}
			else{

				ApplicationLauncher.logger.debug("KiggsRefComRemindTaskDebug :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}


	class lduComResetErrorTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("lduComResetErrorTask Entry:"+lduComSerialStatusConnected);
			if (lduComSerialStatusConnected){
				setLDU_ResetErrorStatus( LDU_ResetError());
			}
			else{

				ApplicationLauncher.logger.debug("lduComResetErrorTask:Timer Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}

	class RefAccumulateSettingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefAccumulateSettingTask Entry:" + refComSerialStatusConnected);
			if (refComSerialStatusConnected){
				int retrycount =15; //Waiting for 3 Seconds 
				while((!RefStdComSemlock) && (retrycount > 0)){
					Sleep(200);
					retrycount--;
				}

				if(RefStdComSemlock){
					RefStdComSemlock = false;
					if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
						setRefAccumulateSettingStatus( Ref_AccumulativeSettingReset());
					}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
						setRefAccumulateSettingStatus( sandsRefStdAccuResetTask());
					}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){

					}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
						setRefAccumulateSettingStatus( kiggsRefStdAccumulativeSettingMode());

					}

					RefStdComSemlock = true;
				}
			}
			else{

				ApplicationLauncher.logger.debug("RefAccumulateSettingTask Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}

	class RefAccumulateStartTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefAccumulateStartTask Entry:" + refComSerialStatusConnected);
			if (refComSerialStatusConnected){
				int retrycount =15; //Waiting for 3 Seconds 
				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
					retrycount =60;// Waiting for 12 Seconds 
				}
				while((!RefStdComSemlock) && (retrycount > 0)){
					Sleep(200);
					retrycount--;
				}
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
						setRefAccumlateStartStatus( Ref_AccumulativeStart());
					}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
						setRefAccumlateStartStatus( sandsRefStdAccuStartTask());
					}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
						if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
							
							RefStdDirector refStdKreDirector = new RefStdDirector();
		    				//refStdKreDirector.kreReadRefStdAllData();
		    				setRefAccumlateStartStatus( refStdKreDirector.kreRefStdAccumulationStartTask());
		    				
							
						}else {
							setRefAccumlateStartStatus( kreRefStdAccumulationStartTask());
						}
					}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
						setRefAccumlateStartStatus( kiggsRefStdAccumulationStartTask());

					}
					RefStdComSemlock = true;
				}
			}
			else{

				ApplicationLauncher.logger.debug("RefAccumulateStartTask: Timer Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread
				//RefStdTimer.cancel();



			}
			ApplicationLauncher.logger.debug("RefAccumulateStartTask Exit:");
		}
	}


	public void  kiggsRefStdSetModeSettingDefault() {
		//public void run() {
		ApplicationLauncher.logger.debug("kiggsRefStdSetModeSettingDefault Entry:" + refComSerialStatusConnected);
		if (refComSerialStatusConnected){
			int retrycount =15; //Waiting for 3 Seconds 
			if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				retrycount =60;// Waiting for 12 Seconds 
			}
			while((!RefStdComSemlock) && (retrycount > 0)  && (!ProjectExecutionController.getUserAbortedFlag())){
				Sleep(200);
				retrycount--;
			}
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					kiggsRefStdSendInitCommand();
	
					RefStdComSemlock = true;
				}
			}
		}
		else{

			ApplicationLauncher.logger.debug("kiggsRefStdSetModeSettingDefault: Timer Exit!%n");
			//lduTimer.cancel(); //Terminate the timer thread
			//RefStdTimer.cancel();



		}
		ApplicationLauncher.logger.debug("kiggsRefStdSetModeSettingDefault Exit:");
		//}
	}


	public boolean  kiggsRefStdSetCurrentRangeSettingMaxTap() {
		//public void run() {
		boolean status = false;
		ApplicationLauncher.logger.debug("kiggsRefStdSetCurrentRangeSettingMaxTap Entry:" + refComSerialStatusConnected);
		if (refComSerialStatusConnected){
			int retrycount =25;//15; //Waiting for 3 Seconds 
			if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				retrycount =60;// Waiting for 12 Seconds 
			}
			while((!RefStdComSemlock) && (retrycount > 0) && (!ProjectExecutionController.getUserAbortedFlag()) ){
				Sleep(200);
				retrycount--;
			}
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					kiggsRefStdSendCommandCurrentRangeSettingManualModeMaxTap();
					status = true;
					RefStdComSemlock = true;
				}
			}
		}
		else{

			ApplicationLauncher.logger.debug("kiggsRefStdSetCurrentRangeSettingMaxTap: Timer Exit!%n");
			//lduTimer.cancel(); //Terminate the timer thread
			//RefStdTimer.cancel();



		}
		ApplicationLauncher.logger.debug("kiggsRefStdSetCurrentRangeSettingMaxTap Exit:");
		return status ;
		//}
	}


	public void  kiggsRefStdSetModeBasicMeasurement() {
		//public void run() {
		ApplicationLauncher.logger.debug("kiggsRefStdSetModeBasicMeasurement Entry:" + refComSerialStatusConnected);
		if (refComSerialStatusConnected){
			int retrycount =15; //Waiting for 3 Seconds 
			if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				retrycount =60;// Waiting for 12 Seconds 
			}
			while((!RefStdComSemlock) && (retrycount > 0)  && (!ProjectExecutionController.getUserAbortedFlag())){
				Sleep(200);
				retrycount--;
			}
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					kiggsRefStdSendCommandStateSettingBasicMeasurement();
	
					RefStdComSemlock = true;
				}
			}
		}
		else{

			ApplicationLauncher.logger.debug("kiggsRefStdSetModeBasicMeasurement: Timer Exit!%n");
			//lduTimer.cancel(); //Terminate the timer thread
			//RefStdTimer.cancel();



		}
		ApplicationLauncher.logger.debug("kiggsRefStdSetModeBasicMeasurement Exit:");
		//}
	}




	class RefStd_BNC_ConfigConstTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask Entry:" + refComSerialStatusConnected);
			if (refComSerialStatusConnected){
				//int retrycount =30; //Waiting for 6 Seconds 
				//int retrycount = 45; //Waiting for 9 Seconds 
				int retrycount = 600; //Waiting for 30 Seconds 
				ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask awaiting for RefStdComSemlock: unlock:" );
				ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask awaiting for RefStdComSemlock: " + RefStdComSemlock);
				while((!RefStdComSemlock) && (retrycount > 0)){
					ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask retrycount:" + retrycount);
					Sleep(50);
					retrycount--;

				}
				//ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask trying for RefStdComSemlock: lock:" );
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					//ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask RefStdComSemlock: locked:" );

					ApplicationLauncher.logger.info("RefStd_BNC_ConfigConstTask : Setting BNC Constant in RefStd");
					Sleep(200);
					if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
						setBNC_ConfigConstantStatus( RefStd_ConfigureBNC_Constant());
					}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
						setBNC_ConfigConstantStatus( sandsRefStdConfigureMode());
					}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
						setBNC_ConfigConstantStatus( refStdKreConfigureMode());

					}

					RefStdComSemlock = true;
					//ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask RefStdComSemlock: unlocked:" );
				}else{
					ApplicationLauncher.logger.error("**************************************************************************");
					ApplicationLauncher.logger.error("**************************************************************************");					
					ApplicationLauncher.logger.error("RefStd_BNC_ConfigConstTask : Unable to set Setting BNC Constant in RefStd");
					ApplicationLauncher.logger.error("**************************************************************************");
					ApplicationLauncher.logger.error("**************************************************************************");
				}
				RefStdBNC_ConstTimer.cancel();
			}
			else{

				ApplicationLauncher.logger.debug("RefStd_BNC_ConfigConstTask :Timer Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread
				//RefStdTimer.cancel();



			}
		}
	}

	class RefMeterAccuReadingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefMeterReadingTask Entry:" + refComSerialStatusConnected);
			if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				refComSerialStatusConnected = true;
			}
			if (refComSerialStatusConnected){
				int retrycount =15; //Waiting for 3 Seconds 
				while((!RefStdComSemlock) && (retrycount > 0)){
					Sleep(200);
					retrycount--;

				}
				ApplicationLauncher.logger.debug("RefMeterReadingTask RefStdComSemlock:" + RefStdComSemlock);
				if(RefStdComSemlock){
					RefStdComSemlock = false;
					if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
						setReadRefReadingStatus( Ref_ReadAccumalative());
					}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
						setReadRefReadingStatus(sandsRefStdReadAccumalativeEnergyTask());
						//setReadRefReadingStatus( Ref_ReadAccumalative());dgfdh
					}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
						setReadRefReadingStatus(refStdKreReadAccumalativeEnergyTask());
					}else if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
						Data_RefStdBofa.setReadEnergyAccumulateEnabled(true);
						setReadRefReadingStatus(refStdBofaReadAccumalativeEnergyTask());
						ApplicationLauncher.logger.debug("RefMeterReadingTask : getReadRefReadingStatus : " + getReadRefReadingStatus());
					}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
					

						//setReadRefReadingStatus(kiggsRefStdReadAccumalativeEnergyTask());
						//ZCxz
					}


					RefStdComSemlock = true;
				}
			}
			else{

				ApplicationLauncher.logger.debug("RefMeterReadingTask :Timer Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread
				//RefStdTimer.cancel();



			}
		}
	}

	class RefAccStopTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefAccStopTask Entry:" + refComSerialStatusConnected);
			if (refComSerialStatusConnected){
				int retrycount =15; //Waiting for 3 Seconds 
				while((!RefStdComSemlock) && (retrycount > 0)){
					Sleep(200);
					retrycount--;
				}
				if(RefStdComSemlock){
					RefStdComSemlock = false;

					if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
						setRefAccumlateStopStatus( Ref_AccumulativeStop());
					}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
						setRefAccumlateStopStatus( sandsRefStdAccuStopTask());
					}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){

					}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
						kiggsRefStdAccumulationStopTask();
					}

					RefStdComSemlock = true;
				}
			}
			else{

				ApplicationLauncher.logger.debug("RefAccStopTask :Timer Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread
				RefStdTimer.cancel();
			}
		}
	}


	public boolean kiggsRefStdAccumulationStopTask(){
		ApplicationLauncher.logger.debug("kiggsRefStdAccumulationStopTask :Entry");
		boolean status = false;
		String command = RefStdKiggsMessage.MSG_ACCU_CONTROL_STOP;//MSG_READ_SETTING;
		String dummyExpectedAckResponse = "";//ConstantRefStdKre.ER_ENERGY_ACC_START_ACK;
		int dummyExpectedlength = 10;
		ApplicationLauncher.logger.debug("kiggsRefStdAccumulationStopTask : command: " + GuiUtils.hexToAscii(command));

		status = kiggsRefStdCommSendDataCommand(command,dummyExpectedAckResponse,dummyExpectedlength);
		if(status){

			ApplicationLauncher.logger.debug("kiggsRefStdAccumulationStopTask :Success" );

		}


		return status;

	}

	class RefStdSetBNC_Output extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("RefStdSetBNC_Output Entry:" + refComSerialStatusConnected);
			if (refComSerialStatusConnected){

				setRefBNC_OutputStatus( RefStd_ConfigureBNC_Output());

				ApplicationLauncher.logger.debug("RefStdSetBNC_Output :Timer Exit!%n");
				//lduTimer.cancel(); //Terminate the timer thread
				//RefStdTimer.cancel(); //Terminate the timer thread

			}
		}
	}



	class lduComCreepSettingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("lduComCreepSettingTask Entry:"+lduComSerialStatusConnected);
			if (lduComSerialStatusConnected){
				setLDU_CreepSettingStatus(LDU_CreepSetting());
			}
			else{

				ApplicationLauncher.logger.debug("lduComCreepSettingTask Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}

	}

	class lduComConstSettingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("lduComConstSettingTask Entry:"+lduComSerialStatusConnected);
			if (lduComSerialStatusConnected){
				setLDU_ConstSettingStatus(LDU_ConstSetting());
			}
			else{

				ApplicationLauncher.logger.debug("lduComConstSettingTask Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread
			}
		}

	}

	class lduComSTASettingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("lduComSTASettingTask Entry:"+lduComSerialStatusConnected);
			if (lduComSerialStatusConnected){
				setLDU_STASettingStatus(LDU_STASetting());
			}
			else{

				ApplicationLauncher.logger.debug("lduComSTASettingTask Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread
			}
		}

	}

	class lduComResetSettingTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("lduComResetSettingTask Entry:"+lduComSerialStatusConnected);
			if (lduComSerialStatusConnected){

				setLDU_ResetSettingStatus(LDU_ResetSetting());
			}
			else{

				ApplicationLauncher.logger.debug("lduComResetSettingTask :Timer Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread
			}
		}
	}

	class pwrSrcComSetOnTask extends TimerTask {
		public void run() {
			//ApplicationLauncher.logger.info("Timer invoked!");
			ApplicationLauncher.logger.debug("pwrSrcComSetOnTask Entry:"+pwrSrcComSerialStatusConnected);
			if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					pwrSrcComSerialStatusConnected = SerialPortManagerPwrSrc_V2.isPwrSrcSerialStatusConnected();
				}else {
					pwrSrcComSerialStatusConnected = SerialPortManagerPwrSrc.powerSourceSerialStatusConnected;
				}
			}
			if (pwrSrcComSerialStatusConnected){
				//try {
				String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
				if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					ApplicationLauncher.logger.debug("pwrSrcComSetOnTask:" +ConstantApp.METERTYPE_SINGLEPHASE);
					SetPowerSourceOn();
				}
				else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
					ApplicationLauncher.logger.debug("pwrSrcComSetOnTask:" +ConstantApp.METERTYPE_THREEPHASE);
					SetThreePhasePowerSrcOn();
				}
				else{
					ApplicationLauncher.logger.debug("pwrSrcComSetOnTask: Else Case");
				}

				setPowerSrcTurnedOnStatus(true);
			}
			else{

				ApplicationLauncher.logger.debug("pwrSrcComSetOnTask :Timer Exit!%n");
				pwrSrcTimer.cancel(); //Terminate the timer thread
			}
		}
	}

	public class har_pwrSrcComSetOnTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("har_pwrSrcComSetOnTask Entry:"+pwrSrcComSerialStatusConnected);
			if (pwrSrcComSerialStatusConnected){
				String metertype = DisplayDataObj.getDeployedEM_ModelType();
				if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
					SetHarPowerSourceOn();
					setPowerSrcTurnedOnStatus(true);
				} else if (metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){

					SetSinglePhaseHarPowerSourceOn();
					setPowerSrcTurnedOnStatus(true);
				}
			}
			else{

				ApplicationLauncher.logger.debug("har_pwrSrcComSetOnTask :Timer Exit!%n");
				pwrSrcTimer.cancel(); //Terminate the timer thread

			}
		}
	}

	class cus_pwrSrcComSetOnTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("cus_pwrSrcComSetOnTask Entry:"+pwrSrcComSerialStatusConnected);
			if (pwrSrcComSerialStatusConnected){
				String metertype = DisplayDataObj.getDeployedEM_ModelType();
				if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
					SetCusPowerSourceOn();
					setPowerSrcTurnedOnStatus(true);
				} else if (metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					SetSinglePhaseCusPowerSourceOn();
					setPowerSrcTurnedOnStatus(true);
				}
			}
			else{

				ApplicationLauncher.logger.debug("cus_pwrSrcComSetOnTask :Timer Exit!%n");
				pwrSrcTimer.cancel(); //Terminate the timer thread



			}
		}
	}

	class pwrSrcComSetOffTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.debug("pwrSrcComSetOffTask : Entry");
			if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					pwrSrcComSerialStatusConnected = SerialPortManagerPwrSrc_V2.isPwrSrcSerialStatusConnected();
				}else {
					pwrSrcComSerialStatusConnected = SerialPortManagerPwrSrc.powerSourceSerialStatusConnected;
				}
			}
			if (pwrSrcComSerialStatusConnected){
				if(!isPowerSourceTurnedOff()){
					SetPowerSourceOff();
				}
				setPowerSrcTurnedOnStatus(false);

			}
			else{

				ApplicationLauncher.logger.debug("pwrSrcComSetOffTask :Timer Exit!%n");
				pwrSrcTimer.cancel(); //Terminate the timer thread
			}
		}
	}
	class lduComReadCreepTask extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=1;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											LDU_ReadCreepData(Address);
										}
									}
								}
							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("lduComReadCreepTask: JSONException:" + e.getMessage());
					}
					LDU_ComSemlock = true;
				}

				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						lduTimer.schedule(new lduComReadCreepTask(), SerialLDU_ComRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lduComReadCreepTask: Timer Exit!");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}



	class lscsLduComReadCreepTask extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			ApplicationLauncher.logger.debug("MaximumNumberOfDeviceConnected: " + MaximumNumberOfDeviceConnected);
			ApplicationLauncher.logger.debug("MaximumNumberOfDeviceConnected: getListOfDevices: " + ProjectExecutionController.getListOfDevices());
			int initialLduAddress = 1;
			if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
				
				if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
				}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
				}
			}


			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=initialLduAddress;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											lscsLDU_ReadCreepData(Address);
										}
									}
								}
							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("lscsLduComReadCreepTask: JSONException:" + e.getMessage());
					}
					LDU_ComSemlock = true;
				}

				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						lduTimer.schedule(new lscsLduComReadCreepTask(), SerialLDU_ComRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lscsLduComReadCreepTask: Timer Exit!");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}


	class lduComReadConstTask extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=1;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											LDU_ReadConstData(Address);
										}
									}
								}
							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("lduComReadConstTask: JSONException: " + e.getMessage());
					}
					LDU_ComSemlock = true;
				}

				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						lduTimer.schedule(new lduComReadConstTask(), SerialLDU_ComRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lduComReadConstTask: Timer Exit!");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}



	class lscsLduComReadConstTask extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			int initialLduAddress = 1;
			if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
				
				if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
				}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
				}
			}


			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=initialLduAddress;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											lscsLDU_ReadConstData(Address);
										}
									}
								}
							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("lscsLduComReadConstTask: JSONException: " + e.getMessage());
					}
					LDU_ComSemlock = true;
				}

				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						if(DisplayDataObj.getDevicesToBeRead().size() != 0){
							lduTimer.schedule(new lscsLduComReadConstTask(), SerialLDU_ComRefreshTimeInMsec);
						}else{
							ApplicationLauncher.logger.debug("lscsLduComReadConstTask: Timer already Cancelled!");
						}
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lscsLduComReadConstTask: Timer Exit!");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}

	class lduComReadSTATask extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=1;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){	
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											LDU_ReadSTAData(Address);
										}
									}
								}

							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error(" lduComReadSTATask: JSONException:" + e.getMessage());
					}
					LDU_ComSemlock = true;
				}
				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						lduTimer.schedule(new lduComReadSTATask(), SerialLDU_ComRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lduComReadSTATask: Timer Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}

	class lscsLduComReadSTA_Task extends TimerTask {
		public void run() {
			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			int initialLduAddress = 1;
			if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
				
				if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
				}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
				}
			}


			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				if(LDU_ComSemlock){
					LDU_ComSemlock = false;
					try {
						for(int Address=initialLduAddress;Address<=MaximumNumberOfDeviceConnected;Address++){
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								if(IsDeviceToBeRead(Address)){	
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											lscsLDU_ReadSTA_Data(Address);
										}
									}
								}

							}
						}
					}
					catch(JSONException e){
						e.printStackTrace();
						ApplicationLauncher.logger.error(" lscsLduComReadSTA_Task: JSONException:" + e.getMessage());
					}
					LDU_ComSemlock = true;
				}
				if(lduComSerialStatusConnected){
					if(DeviceDataManagerController.getLDU_ReadDataFlag()){
						lduTimer.schedule(new lscsLduComReadSTA_Task(), SerialLDU_ComRefreshTimeInMsec);
					}
				}

			}
			else{

				ApplicationLauncher.logger.debug("lscsLduComReadSTA_Task: Timer Exit!%n");
				lduTimer.cancel(); //Terminate the timer thread



			}
		}
	}


	class lduComReadErrorTask extends TimerTask {
		public void run() {

			int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
			ApplicationLauncher.logger.debug("MaximumNumberOfDeviceConnected: " + MaximumNumberOfDeviceConnected);
			int initialLduAddress = 1;
			if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
				
				if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
				}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
					initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
					MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
				}
			}

			if (DisplayDataObj.getLDU_ReadDataFlag() && lduComSerialStatusConnected){
				ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag True entry");
				if(LDU_ComSemlock){
					ApplicationLauncher.logger.debug("lduComReadErrorTask: LDU_ComSemlock True entry");
					LDU_ComSemlock = false;
					try {
						for(int Address=initialLduAddress;Address<=MaximumNumberOfDeviceConnected;Address++){
							ApplicationLauncher.logger.debug("lduComReadErrorTask: Address:" + Address);
							ApplicationLauncher.logger.debug("lduComReadErrorTask: getListOfDevices:" + ProjectExecutionController.getListOfDevices());
							ApplicationLauncher.logger.debug("lduComReadErrorTask: getListOfDevices2:" + ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address)));
							if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
								ApplicationLauncher.logger.debug("lduComReadErrorTask: getListOfDevices True entry");
								if(IsDeviceToBeRead(Address)){
									ApplicationLauncher.logger.debug("Reading LDU..."+Address);
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){
											if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
												LDU_ReadErrorData(Address);
											}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
												if(!ProjectExecutionController.getUserAbortedFlag()) {
													lscsLDU_ReadErrorData(Address);
												}
											}
										}else{
											ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag exit");
										}
									}else{
										ApplicationLauncher.logger.debug("lduComReadErrorTask: UserAbortedFlag exit");
									}
								}
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("lduComReadErrorTask: JSONException: " + e.getMessage());
					}
					LDU_ComSemlock = true;
				}
				if(lduComSerialStatusConnected){
					try{
						//for repeatabality test check
						if(getSerialLDU_ComRefreshTimeInMsec() != SerialLDU_ComRefreshDefaultTimeInMsec){
							if(DisplayDataObj.getDevicesToBeRead().size()>0){
								if(!ProjectExecutionController.getUserAbortedFlag()){
									if(DeviceDataManagerController.getLDU_ReadDataFlag()){
										if(!ProjectExecutionController.getUserAbortedFlag()) {
											lduTimer.schedule(new lduComReadErrorTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
										}else {
											ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit4X !%n");
											lduTimer.cancel(); //Terminate the timer thread
										}
									}
								}else{
									ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit4 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}else{
								ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit1 !%n");
								lduTimer.cancel(); //Terminate the timer thread
							}
						}
						else{
							if(!ProjectExecutionController.getUserAbortedFlag()){
								if(DeviceDataManagerController.getLDU_ReadDataFlag()){
									ApplicationLauncher.logger.debug("lduComReadErrorTask: validating device to be read");
									if(DisplayDataObj.getDevicesToBeRead().size() !=0){
										if(!ProjectExecutionController.getUserAbortedFlag()) {
											ApplicationLauncher.logger.debug("lduComReadErrorTask:Scheduling task again");
											lduTimer.schedule(new lduComReadErrorTask(), getSerialLDU_ComRefreshTimeInMsec());
										}else {
											ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit3X !%n");
											lduTimer.cancel(); //Terminate the timer thread
										}
									}/*else{
										ApplicationLauncher.logger.debug("lduComReadErrorTask: send refresh command");
										lscsLDU_SendRefreshDataCommand();

									}*/
								}
							}else{
								ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit3 !%n");
								lduTimer.cancel(); //Terminate the timer thread
							}
						}

					}
					catch(Exception e){
						e.printStackTrace();
						ApplicationLauncher.logger.error("lduComReadErrorTask: Exception:" + e.getMessage());
						ApplicationLauncher.logger.info("lduComReadErrorTask: lduTimer already Cancelled");

					}
				}


			}
			else{

				ApplicationLauncher.logger.debug("lduComReadErrorTask:Timer Exit2 !%n");
				lduTimer.cancel(); //Terminate the timer thread

			}
		}
	}




	public boolean IsDeviceToBeRead(int Address){
		ApplicationLauncher.logger.debug("IsDeviceToBeRead:  Entry");
		ArrayList<Integer> devices_mounted = DisplayDataObj.getDevicesToBeRead();
		if(devices_mounted.contains(Address)){
			ApplicationLauncher.logger.debug("IsDeviceToBeRead: " + Address + ": Yes");
			return true;
		}
		else{
			ApplicationLauncher.logger.debug("IsDeviceToBeRead: " + Address + ": No");
			return false;
		}
	}

	public static String getLastReadRefStdData() {
		return lastReadRefStdData;
	}

	public static void setLastReadRefStdData(String lastReadRefStdData) {
		SerialDataManager.lastReadRefStdData = lastReadRefStdData;
	}

	public static void clearLastReadRefStdData() {
		SerialDataManager.lastReadRefStdData = "";
	}

	public SerialDataSandsRefStd getLastSandsRefStdObj() {
		return lastSandsRefStdObj;
	}

	public void setLastSandsRefStdObj(SerialDataSandsRefStd lastSandsRefStdObj) {
		this.lastSandsRefStdObj = lastSandsRefStdObj;
	}

	public String lscsLduDialTestAddressMapping(int LDU_ReadAddress){

		ApplicationLauncher.logger.debug("lscsLduDialTestAddressMapping :Entry");

		String positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_01_HDR;
		switch (LDU_ReadAddress) {



		case 1:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_01_HDR;
			break;


		case 2:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_02_HDR;
			break;

		case 3:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_03_HDR;
			break;


		case 4:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_04_HDR;
			break;

		case 5:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_05_HDR;
			break;


		case 6:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_06_HDR;
			break;

		case 7:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_07_HDR;
			break;


		case 8:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_08_HDR;
			break;

		case 9:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_09_HDR;
			break;


		case 10:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_10_HDR;
			break;

		case 11:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_11_HDR;
			break;


		case 12:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_12_HDR;
			break;

		case 13:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_13_HDR;
			break;


		case 14:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_14_HDR;
			break;

		case 15:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_15_HDR;
			break;


		case 16:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_16_HDR;
			break;

		case 17:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_17_HDR;
			break;


		case 18:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_18_HDR;
			break;

		case 19:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_19_HDR;
			break;


		case 20:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_20_HDR;
			break;

		case 21:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_21_HDR;
			break;


		case 22:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_22_HDR;
			break;

		case 23:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_23_HDR;
			break;


		case 24:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_24_HDR;
			break;

		case 25:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_25_HDR;
			break;


		case 26:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_26_HDR;
			break;

		case 27:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_27_HDR;
			break;


		case 28:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_28_HDR;
			break;

		case 29:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_29_HDR;
			break;


		case 30:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_30_HDR;
			break;

		case 31:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_31_HDR;
			break;


		case 32:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_32_HDR;
			break;

		case 33:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_33_HDR;
			break;


		case 34:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_34_HDR;
			break;

		case 35:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_35_HDR;
			break;


		case 36:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_36_HDR;
			break;

		case 37:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_37_HDR;
			break;


		case 38:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_38_HDR;
			break;

		case 39:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_39_HDR;
			break;


		case 40:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_40_HDR;
			break;

		case 41:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_41_HDR;
			break;


		case 42:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_42_HDR;
			break;

		case 43:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_43_HDR;
			break;


		case 44:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_44_HDR;
			break;

		case 45:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_45_HDR;
			break;


		case 46:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_46_HDR;
			break;

		case 47:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_47_HDR;
			break;


		case 48:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_48_HDR;
			break;


		default:
			positionValue = ConstantLduLscs.CMD_DIFF_METER_CONSTANT_POSITION_01_HDR;
			break;
		}

		return positionValue;

	}

	public boolean lscsWaitForPowerSrcFeedBackControl(int waitTimeOutInSec){
		ApplicationLauncher.logger.debug("lscsWaitForPowerSrcFeedBackControl :Entry");
		ApplicationLauncher.logger.info("lscsWaitForPowerSrcFeedBackControl : waitTimeOutInSec: " +waitTimeOutInSec);
		ApplicationHomeController.update_left_status("Awaiting for feedback Control",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;

		waitTimeOutInSec = waitTimeOutInSec; // * 10;
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 1;
			String expectedResponse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_READY;
			while ( (retryCount <= waitTimeOutInSec) && (!status) && (!ProjectExecutionController.getUserAbortedFlag())){
				ApplicationLauncher.logger.info("lscsWaitForPowerSrcFeedBackControl : retryCount: " +retryCount);
				status = waitforSerialCommPwrSrcResponse(expectedResponse);
				//Sleep(1000);
				Sleep(100);
				retryCount++;

			}
			boolean modeResponse = false;
			String command = "";




		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsWaitForPowerSrcFeedBackControl: feedback set success");
			ApplicationHomeController.update_left_status("Feedback Control received",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsWaitForPowerSrcFeedBackControl: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsWaitForPowerSrcFeedBackControl: Exit");
		return status;
	}



	public boolean waitforSerialCommPwrSrcResponse(String ExpectedResult){
		ApplicationLauncher.logger.debug("waitforSerialCommPwrSrcResponse :Entry");
		//ApplicationLauncher.logger.debug("waitforSerialCommPwrSrcResponse :Data: " + Data);
		ApplicationLauncher.logger.debug("waitforSerialCommPwrSrcResponse :ExpectedResult: " + ExpectedResult);
		boolean status= false;
		Communicator SerialPortObj =commPowerSrc;
		char Terminator = ConstantApp.END_CR;
		try {
			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				//SerialPortObj.writeStringMsgToPort(Data+Terminator);
				//SerialPortObj.setExpectedResult(Data+ExpectedResult+Terminator);

			}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				setPowerSrcErrorResponseReceivedStatus(false);
				//SerialPortObj.writeStringMsgToPort(Data);dfd
				SerialPortObj.setExpectedResult(ExpectedResult);
				SerialPortObj.setExpectedDataErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_DATA_ERROR_RESPONSE);
				SerialPortObj.setExpectedSetErrorResult(ConstantPowerSourceLscs.CMD_PWR_SRC_ACK_ERROR_RESPONSE);
				//SerialPortObj.ClearSerialData();
				/*				for(int i = 0; i < Data.length(); i++){
					//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
					SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
					Sleep(200);
				}*/

			}
			SerialDataPowerSrc pwerData = new SerialDataPowerSrc();
			pwerData.SerialReponseTimerStart(SerialPortObj,20);//20


			status = pwerData.IsExpectedResponseReceived();
			if (!status){
				if(pwerData.IsExpectedErrorResponseReceived()){
					ApplicationLauncher.logger.info("waitforSerialCommPwrSrcResponse : Unable to set the Power source Parameter:");
					setPowerSrcErrorResponseReceivedStatus(true);
				}
			}
			pwerData = null;//garbagecollector
			SerialPortObj = null;//garbagecollector
		}catch(Exception e){
			ApplicationLauncher.logger.error("waitforSerialCommPwrSrcResponse :Exception :" + e.getMessage());
		}
		return status;
	}

	public boolean lscsSetPowerSourcePhaseAngleFineTune(String selectedPhase, int fineTuneLevel,boolean incrementMode){
		ApplicationLauncher.logger.debug("lscsSetPowerSourcePhaseAngleFineTune :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune : fineTuneLevel: " +fineTuneLevel);
		ApplicationHomeController.update_left_status("Setting Degree FineTune",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF1_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-A: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF1_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-B: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF2_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF2_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-C: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF2_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF2_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-D: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF3_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF3_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-E: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_PHASE_ANGLE_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF3_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_PF3_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune :invalid mode data: fineTuneLevel-F: "+fineTuneLevel);
						return status;
					}
				}
			}
			//while((!status) && (retryCount>0)){fsdf

			ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune : command : " + command);
			ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune : expectedRepsonse : " + expectedRepsonse);
			modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			if(modeResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune : status success1");
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while(retryCount>0){
						Sleep(2000);
						modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
						if(modeResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourcePhaseAngleFineTune: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1003);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSetPowerSourcePhaseAngleFineTune: Exit");
		return status;
	}

	public boolean lscsSetPowerSourceVoltFineTune(String selectedPhase, int fineTuneLevel,boolean incrementMode){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceVoltFineTune :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune : fineTuneLevel: " +fineTuneLevel);
		ApplicationHomeController.update_left_status("Setting Volt FineTune",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-A: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-B: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-C: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-D: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-E: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune :invalid mode data: fineTuneLevel-F: "+fineTuneLevel);
						return status;
					}
				}
			}
			//while((!status) && (retryCount>0)){fsdf

			ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune : command : " + command);
			ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune : expectedRepsonse : " + expectedRepsonse);
			modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			if(modeResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune : status success1");
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while(retryCount>0){
						Sleep(2000);
						modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
						if(modeResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceVoltFineTune: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1004);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSetPowerSourceVoltFineTune: Exit");
		return status;
	}

	public boolean lscsSetPowerSourceFineTuneData(String commandData){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceFineTuneData :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData : commandData: " +commandData);
		ApplicationHomeController.update_left_status("Setting Volt FineTune",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		/*		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else */
		if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			//int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			//boolean modeResponse = false;
			String command = "";
			//String expectedRepsonse = "";
			//if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
			//if(noChange){
			command = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + commandData;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
			//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
			/*						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-A: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-B: "+fineTuneLevel);
							return status;
						}*/
			//	}
			//}
			/*else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
					if(noChange){
						command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
						//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-C: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-D: "+fineTuneLevel);
							return status;
						}
					}
				}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
					if(noChange){
						command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
						//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-E: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData :invalid mode data: fineTuneLevel-F: "+fineTuneLevel);
							return status;
						}
					}*/
			//}
			//while((!status) && (retryCount>0)){fsdf

			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData : command : " + command);
			//ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData : expectedRepsonse : " + expectedRepsonse);
			status = WriteToSerialCommPwrSrcV2(command);
			//modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			/*				if(modeResponse) {
					status = true;
					ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData : status success1");
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while(retryCount>0){
							Sleep(2000);
							modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
							if(modeResponse) {
								status = true;
							}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData : status success2");
						}
					}else {

						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}
				}
			 */
			//}


		}


		/*
		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneData: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1004);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}*/

		ApplicationLauncher.logger.debug("lscsSetPowerSourceFineTuneData: Exit");
		return status;
	}

	//public boolean lscsSetPowerSourceFineTuneNoChange(String selectedPhase, int fineTuneLevel,boolean noChange){
	public boolean lscsSetPowerSourceFineTuneNoChange(){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceFineTuneNoChange :Entry");
		//ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange : fineTuneLevel: " +fineTuneLevel);
		ApplicationHomeController.update_left_status("Setting Volt FineTune",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			//if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
			//	if(noChange){
			command = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + 
						ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE+
						ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
				
			}
			
			//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
			/*						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-A: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-B: "+fineTuneLevel);
							return status;
						}*/
			//}
			//}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
			//	if(noChange){
			//		command = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR + ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
			//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
			/*						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V2_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-C: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V2_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-D: "+fineTuneLevel);
							return status;
						}*/
			//	}
			//}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
			//	if(noChange){
			//		command = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR + ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;//CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;
			//expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V1_INC_LEVEL1;
			/*						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V3_INC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-E: "+fineTuneLevel);
							return status;
						}
					}else{
						if(fineTuneLevel == ConstantLscsPowerSource.FINETUNE_VOLT_ADJUSTMENT_LEVEL1) {
							command = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1;
							expectedRepsonse = ConstantLscsPowerSource.CMD_PWR_SRC_ER_FINE_CONTROL_V3_DEC_LEVEL1;

						}else {
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange :invalid mode data: fineTuneLevel-F: "+fineTuneLevel);
							return status;
						}*/
			//	}
			//}
			//while((!status) && (retryCount>0)){fsdf

			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange : command : " + command);
			//ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange : expectedRepsonse : " + expectedRepsonse);
			status = WriteToSerialCommPwrSrcV2(command);
			//modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			/*				if(modeResponse) {
					status = true;
					ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange : status success1");
				}else{
					if(getPowerSrcErrorResponseReceivedStatus()){
						while(retryCount>0){
							Sleep(2000);
							modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
							if(modeResponse) {
								status = true;
							}
							retryCount--;
						}
						if(status) {
							//i=initRetryCount;
							ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange : status success2");
						}
					}else {

						//setPowerSrcErrorResponseReceivedStatus(true);
						//DisplayDataObj.setPwrSrcInitCompleted(false);
					}
				}
			 */
			//}


		}


		/*
		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceFineTuneNoChange: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1004);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}*/

		ApplicationLauncher.logger.debug("lscsSetPowerSourceFineTuneNoChange: Exit");
		return status;
	}



	public boolean lscsSetPowerSourceCurrentFineTune(String selectedPhase, int fineTuneLevel,boolean incrementMode){
		ApplicationLauncher.logger.debug("lscsSetPowerSourceCurrentFineTune :Entry");
		ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune : fineTuneLevel: " +fineTuneLevel);
		ApplicationHomeController.update_left_status("Setting Volt FineTune",ConstantApp.LEFT_STATUS_DEBUG);
		boolean status = false;


		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){

		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			//int initRetryCount = 2;
			//for(int i = 0 ; i< initRetryCount ; i++) {
			//lscsPowerSrcInit();
			int retryCount = 2;
			//if(i!=0) {
			status = false;
			//}
			boolean modeResponse = false;
			String command = "";
			String expectedRepsonse = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I1_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-A: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I1_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-B: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I2_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-C: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I2_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-D: "+fineTuneLevel);
						return status;
					}
				}
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if(incrementMode){
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_INC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I3_INC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-E: "+fineTuneLevel);
						return status;
					}
				}else{
					if(fineTuneLevel == ConstantPowerSourceLscs.FINETUNE_CURRENT_ADJUSTMENT_LEVEL1) {
						command = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_DEC_LEVEL1;
						expectedRepsonse = ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_I3_DEC_LEVEL1;

					}else {
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune :invalid mode data: fineTuneLevel-F: "+fineTuneLevel);
						return status;
					}
				}
			}
			//while((!status) && (retryCount>0)){fsdf

			ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune : command : " + command);
			ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune : expectedRepsonse : " + expectedRepsonse);
			modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
			if(modeResponse) {
				status = true;
				ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune : status success1");
			}else{
				if(getPowerSrcErrorResponseReceivedStatus()){
					while(retryCount>0){
						Sleep(2000);
						modeResponse = WriteToSerialCommPwrSrc(command,expectedRepsonse);
						if(modeResponse) {
							status = true;
						}
						retryCount--;
					}
					if(status) {
						//i=initRetryCount;
						ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune : status success2");
					}
				}else {

					//setPowerSrcErrorResponseReceivedStatus(true);
					//DisplayDataObj.setPwrSrcInitCompleted(false);
				}
			}

			//}


		}



		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("lscsSetPowerSourceCurrentFineTune: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1005);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("lscsSetPowerSourceCurrentFineTune: Exit");
		return status;
	}


	public static boolean isPowerSourceTurnedOff() {
		return powerSourceTurnedOff;
	}

	public static void setPowerSourceTurnedOff(boolean powerSourceTurnedOff) {
		SerialDataManager.powerSourceTurnedOff = powerSourceTurnedOff;
	}

	public static boolean isRefComSerialStatusConnected() {
		return refComSerialStatusConnected;
	}

	public static void setRefComSerialStatusConnected(boolean refComSerialStatusConnected) {
		SerialDataManager.refComSerialStatusConnected = refComSerialStatusConnected;
	}


}
