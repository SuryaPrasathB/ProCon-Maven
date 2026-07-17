package com.tasnetwork.calibration.energymeter.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tasnetwork.spring.orm.service.AppConfigService;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.calib.CalibPoints;
import com.tasnetwork.calibration.energymeter.calib.CurrentCalibration;
import com.tasnetwork.calibration.energymeter.calib.CurrentTap;
import com.tasnetwork.calibration.energymeter.calib.LscsCalibrationConfigModel;
import com.tasnetwork.calibration.energymeter.calib.VoltageCalibration;
import com.tasnetwork.calibration.energymeter.calib.VoltageTap;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ConveyorConfigModel;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigModel;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.deployment.DeploymentDataModel;
import com.tasnetwork.calibration.energymeter.deployment.LiveTableDataManager;
import com.tasnetwork.calibration.energymeter.deployment.MeterParamsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectStatusRefresh;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.message.lscsPowerSourceHarmonicsMessage;
import com.tasnetwork.calibration.energymeter.reportprofile.OperationProcessJsonReadModel;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerPwrSrc;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerRefStd;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerDutCmd_V3;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerRefStd_V2;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;
import com.tasnetwork.calibration.energymeter.testreport.config.ReportConfigModel;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.service.OperationParamService;
import com.tasnetwork.spring.orm.service.OperationProcessService;
import com.tasnetwork.spring.orm.service.ReportProfileManageService;
import com.tasnetwork.spring.orm.service.ReportProfileMeterMetaDataFilterService;
import com.tasnetwork.spring.orm.service.ReportProfileTestDataFilterService;
import com.tasnetwork.spring.orm.service.RpPrintPositionService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class DeviceDataManagerController {

	
	private static DutCmdManager dutCmdManagerV3 = new DutCmdManager();
	private static DutCommand dutCommandData = new DutCommand();
	
	private static String calibModeRphaseVoltageTarget = "240.0";
	private static String calibModeRphaseCurrentTarget = "10.0";
	private static String calibModeRphasePfTarget = "1.0";
	private static String calibModeFreqTarget = "50.0";
	public static  HashMap< Integer,String> dutSerialNumberMap = new HashMap<>(); 
	//public static  HashMap< Integer,String> dutSerialNumber = new HashMap<>(); 
	
	private static AtomicInteger presentCursorRowNo = new AtomicInteger(3);
	private static float metricsLogAcceptedLowerLimit = -0.250f;
	private static float metricsLogAcceptedUpperLimit = 0.250f;
	
	//public static ClassPathXmlApplicationContext springAppCtx = ConstantVersion.springAppContext;
	
	private static String presentMetricLogFileName = "MetricLog.xlsx";
	private static long metricsLogTestPointStartingEpochTimeInMSec = 0;
	
	private static long metricsLogTestPointCmdSentEpochTimeInMSec = 0;
	
	private static String metricsLogTargetVoltage = "0";
	private static String metricsLogTargetCurrent = "0";
	
	private static int metricsLogCounter = 0;
	private static boolean metricsLogTestPointStartingAlreadyInitated = false;
	
	public static boolean hardwareBootupOccured = false;
	
	public static volatile boolean presentTestPointContainsHarmonics = false;
	
	public static int readProPowerAllDataNoOfVariables = 6;
	
	private static ArrayList<Boolean> stepRunModeAtleastOneResultReadCompleted             = new ArrayList<Boolean>();//""; // Number of dial pulses (XXXXXXXX)

	
	
	//Get service from context.
	public static ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService = null;//springAppCtx.getBean(ReportProfileMeterMetaDataFilterService.class);
	public static ReportProfileManageService reportProfileManageService = null;//springAppCtx.getBean(ReportProfileMeterMetaDataFilterService.class);
	
	public static ReportProfileTestDataFilterService reportProfileTestDataFilterService = null ;
	public static OperationProcessService reportOperationProcessService = null ;
	public static RpPrintPositionService rpPrintPositionService = null ;
	public static OperationParamService rpOperationParamService = null ;
	public static AppConfigService appConfigService = null ;
	
	private static volatile boolean powerSrcReadData = false;
	//private static volatile boolean refStdReadData = false;
	public static boolean powerSourcePortInitSuccess = false;//hvciPortInitSuccess
	public static boolean refStdPortInitSuccess = false;
	
	private static volatile boolean readRefStdAccuDataFlag = false;
	
	//public static  HashMap< Integer,String> dutSerialNumber = new HashMap<>(); 
	private static ArrayList<UacDataModel> uacSelectProfileScreenList = null;
	private static Custom1ReportConfigModel custom1ReportConfigParsedKey = new Custom1ReportConfigModel();
	private static LscsCalibrationConfigModel lscsCalibrationConfigParsedKey = new LscsCalibrationConfigModel();
	private static ConveyorConfigModel conveyorConfigParsedKey = new ConveyorConfigModel();
	
	static ReportConfigModel reportConfigParsedData = null;
	
	private static OperationProcessJsonReadModel reportProfileConfigParsedKey = new OperationProcessJsonReadModel();
	

	private static String sandsRefStdLastSetVoltageMode = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
	private static String sandsRefStdLastSetCurrentMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
	private static String sandsRefStdLastSetPulseOutputMode = ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY;
	
	private static  HashMap<Integer,HashMap<Integer, String>> lduErrorDataHashMap2d = new HashMap<Integer,HashMap<Integer, String>>();

	private static String lastSetPowerSourceFrequency = "40.0";
	
	private static String lastSetPowerSourceRphaseVoltage = "";
	private static String lastSetPowerSourceYphaseVoltage = "";
	private static String lastSetPowerSourceBphaseVoltage = "";
	
	private static String lastSetPowerSourceRphaseCurrent = "";
	private static String lastSetPowerSourceYphaseCurrent = "";
	private static String lastSetPowerSourceBphaseCurrent = "";
	
	private static String lastSetPowerSourceRphaseDegree = "";
	private static String lastSetPowerSourceYphaseDegree = "";
	private static String lastSetPowerSourceBphaseDegree = "";
	
	private static String lastSetPowerSourceCurrentTapRelayId = "";
	
	public static boolean bDUT1_ReadData = false;
	public static boolean bDUT2_ReadData = false;
	public static boolean bDUT3_ReadData = false;
	public static boolean bDUT4_ReadData = false;
	public static boolean bDUT5_ReadData = false;
	public static boolean bDUT6_ReadData = false;
	public static boolean bDUT7_ReadData = false;
	public static boolean bDUT8_ReadData = false;
	public static boolean bDUT9_ReadData = false;
	public static boolean bDUT10_ReadData = false;
	
	public static boolean bDUT11_ReadData = false;
	public static boolean bDUT12_ReadData = false;
	public static boolean bDUT13_ReadData = false;
	public static boolean bDUT14_ReadData = false;
	public static boolean bDUT15_ReadData = false;
	public static boolean bDUT16_ReadData = false;
	public static boolean bDUT17_ReadData = false;
	public static boolean bDUT18_ReadData = false;
	public static boolean bDUT19_ReadData = false;
	public static boolean bDUT20_ReadData = false;
	
	public static boolean bDUT21_ReadData = false;
	public static boolean bDUT22_ReadData = false;
	public static boolean bDUT23_ReadData = false;
	public static boolean bDUT24_ReadData = false;
	public static boolean bDUT25_ReadData = false;
	public static boolean bDUT26_ReadData = false;
	public static boolean bDUT27_ReadData = false;
	public static boolean bDUT28_ReadData = false;
	public static boolean bDUT29_ReadData = false;
	public static boolean bDUT30_ReadData = false;
	
	public static boolean bDUT31_ReadData = false;
	public static boolean bDUT32_ReadData = false;
	public static boolean bDUT33_ReadData = false;
	public static boolean bDUT34_ReadData = false;
	public static boolean bDUT35_ReadData = false;
	public static boolean bDUT36_ReadData = false;
	public static boolean bDUT37_ReadData = false;
	public static boolean bDUT38_ReadData = false;
	public static boolean bDUT39_ReadData = false;
	public static boolean bDUT40_ReadData = false;
	
	public static boolean bDUT41_ReadData = false;
	public static boolean bDUT42_ReadData = false;
	public static boolean bDUT43_ReadData = false;
	public static boolean bDUT44_ReadData = false;
	public static boolean bDUT45_ReadData = false;
	public static boolean bDUT46_ReadData = false;
	public static boolean bDUT47_ReadData = false;
	public static boolean bDUT48_ReadData = false;
	
	public static ArrayList<Boolean> bDutReadDataList = new ArrayList<Boolean>(Arrays.asList(
			
			bDUT1_ReadData,bDUT2_ReadData,bDUT3_ReadData,bDUT4_ReadData,bDUT5_ReadData,
			bDUT6_ReadData,bDUT7_ReadData,bDUT8_ReadData,bDUT9_ReadData,bDUT10_ReadData,
			bDUT11_ReadData,bDUT12_ReadData,bDUT13_ReadData,bDUT14_ReadData,bDUT15_ReadData,
			bDUT16_ReadData,bDUT17_ReadData,bDUT18_ReadData,bDUT19_ReadData,bDUT20_ReadData,
			bDUT21_ReadData,bDUT22_ReadData,bDUT23_ReadData,bDUT24_ReadData,bDUT25_ReadData,
			bDUT26_ReadData,bDUT27_ReadData,bDUT28_ReadData,bDUT29_ReadData,bDUT30_ReadData,
			bDUT31_ReadData,bDUT32_ReadData,bDUT33_ReadData,bDUT34_ReadData,bDUT35_ReadData,
			bDUT36_ReadData,bDUT37_ReadData,bDUT38_ReadData,bDUT39_ReadData,bDUT40_ReadData,
			bDUT41_ReadData,bDUT42_ReadData,bDUT43_ReadData,bDUT44_ReadData,bDUT45_ReadData,
			bDUT46_ReadData,bDUT47_ReadData,bDUT48_ReadData
			
			
			));


	//private static List<HashMap< Integer, Float>> lduErrorDatalistOfHashMaps = new ArrayList<HashMap<Integer, Float>>();
	
	//HashMap<String, Integer> map = new HashMap<>();
	//private static ArrayList<ArrayList<Float>> lduErrorDataList = new ArrayList<ArrayList<Float>>();// added for lscsc ldu average reading calculation
	//private static ArrayList<ArrayList<Float>> lduErrorDataList = new ArrayList<ArrayList<Float>>();
	
	
	static String sourceCurrentR_PhaseTapSelection = "001";
	
	static String R_PhaseOutputVoltageRms = "10000";
	static String Y_PhaseOutputVoltageRms = "10000";
	static String B_PhaseOutputVoltageRms = "10000";

	static String R_PhaseOutputCurrentRms = "25000";
	static String Y_PhaseOutputCurrentRms = "25000";
	static String B_PhaseOutputCurrentRms = "25000";
	
	static String R_PhaseOutputVoltage = "000.00";
	static String Y_PhaseOutputVoltage = "000.00";
	static String B_PhaseOutputVoltage = "000.00";
	
	static Double R_PhaseFeedBackProcessVoltageGain = 0.0;
	static Double Y_PhaseFeedBackProcessVoltageGain = 0.0;
	static Double B_PhaseFeedBackProcessVoltageGain = 0.0;
	
	static Double R_PhaseFeedBackProcessVoltageOffset = 0.0;
	static Double Y_PhaseFeedBackProcessVoltageOffset = 0.0;
	static Double B_PhaseFeedBackProcessVoltageOffset = 0.0;
	
	static Double R_PhaseFeedBackProcessCurrentGain = 0.0;
	static Double Y_PhaseFeedBackProcessCurrentGain = 0.0;
	static Double B_PhaseFeedBackProcessCurrentGain = 0.0;
	
	static Double R_PhaseFeedBackProcessCurrentOffset = 0.0;
	static Double Y_PhaseFeedBackProcessCurrentOffset = 0.0;
	static Double B_PhaseFeedBackProcessCurrentOffset = 0.0;

	static String STATimeDuration = "0000";
	static String CreepTimeDuration = "0000";
	static String R_PhaseOutputCurrent = "00.00";
	static String Y_PhaseOutputCurrent = "00.00";
	static String B_PhaseOutputCurrent = "00.00";

	static String R_PhaseOutputPhase = "00.00";
	static String Y_PhaseOutputPhase = "00.00";
	static String B_PhaseOutputPhase = "00.00";

	static volatile boolean PhaseRevPowerOn = false;
	static String userName = "";

	static String EnergyFlowMode = ConstantPowerSourceMte.IMPORT_MODE;
	
	static String executionMctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;
	
	public static boolean allMeterConstSame = false;


	static boolean SkipPhaseRev = false;

	static JSONArray harmonic_data = new JSONArray();

	static boolean Volt_Unbalanced_PowerOn = false;

	public static boolean bLDU_ReadData = false;
	private static boolean bRefStdReadData = false;
	
	private static volatile boolean powerSrcReadFeedbackData = false;
	
	public static boolean ictReadData = false;

	private static Integer PowerSrcOnTimerInSec;

	private static JSONObject DeployedDevicesJson = new JSONObject();


	String PowerSrcCommPortID= null;
	String PwrSrcCommBaudRate = null;
	String LDU_CommPortID= null;
	String LDUCommBaudRate = null;
	String RefStdCommPortID= null;
	String RefStdCommBaudRate = null;	
	String ICT_CommPortID= null;
	String ICTCommBaudRate = null;
	
	
	String harmonicsSrcCommPortID= null;
	String harmonicsSrcCommBaudRate = null;

	Timer UIRefreshTimer;
	
/*	public static boolean bDUT1_ReadData = false;
	public static boolean bDUT2_ReadData = false;
	public static boolean bDUT3_ReadData = false;
	public static boolean bDUT4_ReadData = false;
	public static boolean bDUT5_ReadData = false;
	public static boolean bDUT6_ReadData = false;
	public static boolean bDUT7_ReadData = false;
	public static boolean bDUT8_ReadData = false;
	public static boolean bDUT9_ReadData = false;
	public static boolean bDUT10_ReadData = false;
	
	public static boolean bDUT11_ReadData = false;
	public static boolean bDUT12_ReadData = false;
	public static boolean bDUT13_ReadData = false;
	public static boolean bDUT14_ReadData = false;
	public static boolean bDUT15_ReadData = false;
	public static boolean bDUT16_ReadData = false;
	public static boolean bDUT17_ReadData = false;
	public static boolean bDUT18_ReadData = false;
	public static boolean bDUT19_ReadData = false;
	public static boolean bDUT20_ReadData = false;
	
	public static boolean bDUT21_ReadData = false;
	public static boolean bDUT22_ReadData = false;
	public static boolean bDUT23_ReadData = false;
	public static boolean bDUT24_ReadData = false;
	public static boolean bDUT25_ReadData = false;
	public static boolean bDUT26_ReadData = false;
	public static boolean bDUT27_ReadData = false;
	public static boolean bDUT28_ReadData = false;
	public static boolean bDUT29_ReadData = false;
	public static boolean bDUT30_ReadData = false;
	
	public static boolean bDUT31_ReadData = false;
	public static boolean bDUT32_ReadData = false;
	public static boolean bDUT33_ReadData = false;
	public static boolean bDUT34_ReadData = false;
	public static boolean bDUT35_ReadData = false;
	public static boolean bDUT36_ReadData = false;
	public static boolean bDUT37_ReadData = false;
	public static boolean bDUT38_ReadData = false;
	public static boolean bDUT39_ReadData = false;
	public static boolean bDUT40_ReadData = false;
	
	public static boolean bDUT41_ReadData = false;
	public static boolean bDUT42_ReadData = false;
	public static boolean bDUT43_ReadData = false;
	public static boolean bDUT44_ReadData = false;
	public static boolean bDUT45_ReadData = false;
	public static boolean bDUT46_ReadData = false;
	public static boolean bDUT47_ReadData = false;
	public static boolean bDUT48_ReadData = false;*/

	
	public volatile static float refStdSelectedVoltageTap = 240.0f;
	public volatile static float refStdSelectedCurrentTap = 100.0f;

	public static boolean AllPortInitSuccess = false;

	static float PwrSrcR_PhaseVoltInFloat = 0;	
	static float PwrSrcR_PhaseCurrentInFloat = 0;	
	static float PwrSrcR_PhaseDegreePhase = 0;	
	static float PwrSrcY_PhaseVoltInFloat = 0;	
	static float PwrSrcY_PhaseCurrentInFloat = 0;	
	static float PwrSrcY_PhaseDegreePhase = 0;	
	static float PwrSrcB_PhaseVoltInFloat = 0;	
	static float PwrSrcB_PhaseCurrentInFloat = 0;	
	static float PwrSrcB_PhaseDegreePhase = 0;	
	static float PwrSrcR_PhaseFreq = 0;
	static float PwrSrcR_PhaseMaxCurrentInFloat = 0;
	static boolean pwrSrcInitCompleted = false;
	int WarmupDuration = 0;
	static int CreepDuration = 0;
	static float PercentageOfVoltage = 0;
	static String CreepNoOfPulses = "";
	int STADuration = 0;
	static float PercentageOfCurrent = 0;
	static String STANoOfPulses = "";
	static String Error_min = "";
	static String Error_max = "";
	static JSONObject NoOfPulseReadingToBeSkipped = new JSONObject();
	static String NoOfPulses = "";
	static int InfTimeDuration = 0;
	static String TestRunType = "";
	static int averageNoOfLduReadingRequired = 1;
	static String RateOfCurrent = "";
	static String ConstTestPower = "";
	static Float RateOfVoltage = 0f;
	static JSONArray InitMeterValues = new JSONArray();
	static JSONArray FinalMeterValues = new JSONArray();
	static String Ref_Init_PhaseA_reading = "0.0";
	static String Ref_Init_PhaseB_reading = "0.0";
	static String Ref_Init_PhaseC_reading = "0.0";
	static String Ref_Final_PhaseA_reading = "0.0";
	static String Ref_Final_PhaseB_reading = "0.0";
	static String Ref_Final_PhaseC_reading = "0.0";
	static String current_phaseA_reading = "0.0";
	static String current_phaseB_reading = "0.0";
	static String current_phaseC_reading = "0.0";
	static JSONArray harmonics = new JSONArray();
	static String DeployedEM_ModelType = "";
	static String DeployedEM_CT_Type = "";
	static String dutImpulsesPerUnit = "";
	static int ReadingToBeRead = 0;
	static boolean cutnuetral_flag = false;
	static boolean cutnuetral_wait_flag = false;
	static float PwrSrc_Percent_VoltU1 = 0;	
	static float PwrSrc_Percent_VoltU2 = 0;	
	static float PwrSrc_Percent_VoltU3 = 0;
	static float PwrSrcCustomInFloat_VoltU1 = 0;	
	static float PwrSrcCustomInFloat_VoltU2 = 0;	
	static float PwrSrcCustomInFloat_VoltU3 = 0;	
	static float PwrSrcCustomInFloat_CurrentI1 = 0;	
	static float PwrSrcCustomInFloat_CurrentI2 = 0;	
	static float PwrSrcCustomInFloat_CurrentI3 = 0;
	/*static float PwrSrc_Phase1 = 0;	
	static float PwrSrc_Phase2 = 0;	
	static float PwrSrc_Phase3 = 0;	*/
	public static int error_count = 1;

	static boolean VoltageResetRequired = false;	

	//public static String RSS_Pulse_Rate =  GUIUtils.FormatPulseRate("100000000");
	public static String RSS_Pulse_Rate =  "100000000";
	public static String LastSetRSS_Pulse_Rate =  "100";
	//public static String LDU_PulseConstant =  GUIUtils.FormatPulseRate(ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT);	
	
	//public static String RSS_ActivePulseConstant =  GUIUtils.FormatPulseRate(ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT);	
	//public static String RSS_ReactivePulseConstant =  GUIUtils.FormatPulseRate(ConstantConfig.RSS_REACTIVE_PULSE_CONSTANT);	
	static ArrayList<String> PhaseDegreeOutput = new ArrayList<String>();	

	static ArrayList<Integer> DevicesMounted = new ArrayList<Integer>();


	public static SerialDataManager serialDM_Obj = new SerialDataManager();
	
	public static SerialPortManagerPwrSrc serialPortManagerPwrSrc = new SerialPortManagerPwrSrc();
	public static SerialPortManagerRefStd serialPortManagerRefStd = new SerialPortManagerRefStd();
	public  DecimalFormat harmonicsOrderFormatter = new DecimalFormat("00");
	public  DecimalFormat harmonicsPercentageFormatter = new DecimalFormat("00");
	public  DecimalFormat harmonicsPhaseShiftFormatter = new DecimalFormat("000.0");
	public static DecimalFormat theoriticalPulseFormatter = new DecimalFormat("0000000");
	
	public static SerialPortManagerRefStd_V2 serialPortManagerRefStd_V2 = new SerialPortManagerRefStd_V2();
	public static SerialPortManagerPwrSrc_V2 serialPortManagerPwrSrc_V2 = new SerialPortManagerPwrSrc_V2();
	//public static SerialPortManagerDutCmd_V3 serialPortManagerDutCmd_V2 = new SerialPortManagerDutCmd_V3();
	
	@FXML
	private void initialize() throws IOException {





	}
	
	static public void springDataInit(){
		ApplicationLauncher.logger.info("springDataInit : Entry" );
		reportProfileManageService = ApplicationLauncher.springContext.getBean(ReportProfileManageService.class);
		reportProfileMeterMetaDataFilterService = ApplicationLauncher.springContext.getBean(ReportProfileMeterMetaDataFilterService.class);
		reportProfileTestDataFilterService = ApplicationLauncher.springContext.getBean(ReportProfileTestDataFilterService.class);
		reportOperationProcessService = ApplicationLauncher.springContext.getBean(OperationProcessService.class);
		rpPrintPositionService = ApplicationLauncher.springContext.getBean(RpPrintPositionService.class);
		rpOperationParamService = ApplicationLauncher.springContext.getBean(OperationParamService.class);
		appConfigService = ApplicationLauncher.springContext.getBean(AppConfigService.class);
	}


	public void CheckAllMeterConstSame(){
		ArrayList<Integer> meter_const_arr = new ArrayList<Integer>();
		
		JSONObject result = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		try {
			JSONArray deployed_devices = result.getJSONArray("Devices");
			ApplicationLauncher.logger.info("CheckAllMeterConstSame :deployed_devices: " +deployed_devices.toString());
			//DeploymentManagerController.setAllMeterConstSame(true);// to be updated 
			//if(DeploymentManagerController.IsAllMeterConstSame()){
			for(int index = 0 ; index < deployed_devices.length() ; index++){
				JSONObject jobj = deployed_devices.getJSONObject(index);
				int m_const = jobj.getInt("meter_const");
				//String meter_const = Integer.toString(m_const);
				meter_const_arr.add(m_const);
				ApplicationLauncher.logger.info("CheckAllMeterConstSame : index: " +index + " : m_const: " + m_const);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckAllMeterConstSame :JSONException: "+e.getMessage() );
		}
		setAllMeterConstSame(true);
		if(meter_const_arr.size() > 0) {
			int ref_meter_const_value = meter_const_arr.get(0); 
			
			for(int i=1; i<meter_const_arr.size(); i++){
				int value = meter_const_arr.get(i);
				if(!(ref_meter_const_value == value)){
					setAllMeterConstSame(false);
					ApplicationLauncher.logger.info("CheckAllMeterConstSame : setting to false");
				}
			}
		}
		
		ApplicationLauncher.logger.info("CheckAllMeterConstSame : IsAllMeterConstSame: " +IsAllMeterConstSame() );
	}
	
	public static boolean IsAllMeterConstSame(){
		return allMeterConstSame;
	}

	public static void setAllMeterConstSame(boolean value){
		allMeterConstSame = value;
	}

	public static void setError_count(int inputErrorCount){
		error_count = inputErrorCount;
	}
	public static int getError_countAndIncrement(){
		return error_count++;// = inputErrorCount;
	}
	
	public boolean updateExecutionStatusInDeployManageDB(String project_name,String deployment_id, String executionStatus,String mctModeCompletedStatus,String nctModeCompletedStatus) {
		boolean status = false; 
		status = MySQL_Controller.sp_update_execution_status_deploy_manage (project_name, deployment_id,  executionStatus,mctModeCompletedStatus,mctModeCompletedStatus);
		return status;
	}
	public void SaveKWhToDB(){
		ApplicationLauncher.logger.info("SaveKWhToDB : Entry");
		String project_name = ProjectExecutionController.getCurrentProjectName();
		String test_case_name = ProjectExecutionController.getCurrentTestPointName();
		String alias_id  = ProjectExecutionController.getCurrentTestAliasID();
		ArrayList<Integer> devicesmounted = getDevicesMount();
		String FailureReason = "";
		String Resultstatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";hgfh
		JSONArray initial_kwh_values = getInitMeterValues();
		JSONArray final_kwh_values = getFinalMeterValues();
		String rack_id = "";
		String initialkwh = "";
		String finalkwh = "";
		for(int i =0; i<devicesmounted.size(); i++){
			rack_id = Integer.toString(devicesmounted.get(i));
			
			initialkwh = get_kwh(rack_id, initial_kwh_values);
			finalkwh = get_kwh(rack_id, final_kwh_values);

			int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
			MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, 
					Resultstatus,getError_countAndIncrement(), initialkwh,FailureReason,
					ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
					ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
			MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, 
					Resultstatus,getError_countAndIncrement(), finalkwh,FailureReason,
					ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
					ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);

		}
		String ref_init_kwh = get_ref_i_kwh();
		String ref_final_kwh = get_ref_f_kwh();
		//MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, "0", 
		//		Resultstatus,getError_countAndIncrement(), ref_init_kwh,FailureReason,
		//		ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
		int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
		MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,rack_id, 
				Resultstatus,getError_countAndIncrement(), ref_init_kwh,FailureReason,
				ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
				ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
		MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, 
				Resultstatus,getError_countAndIncrement(), ref_final_kwh,FailureReason,
				ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
				ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
		resetInitMeterValues();
		resetFinalMeterValues();
	}

	public String get_ref_i_kwh(){
		String RefInitPhaseA = getRefInitPhaseAReading();
		String RefInitPhaseB = getRefInitPhaseBReading();
		String RefInitPhaseC = getRefInitPhaseCReading();
		float total_value = Float.parseFloat(RefInitPhaseA) + Float.parseFloat(RefInitPhaseB) + Float.parseFloat(RefInitPhaseC);
		String str_total_value = Float.toString(total_value);
		return str_total_value;
	}

	public String get_ref_f_kwh(){
		String RefFinalPhaseA = getRefFinalPhaseAReading();
		String RefFinalPhaseB = getRefFinalPhaseBReading();
		String RefFinalPhaseC = getRefFinalPhaseCReading();
		float total_value = Float.parseFloat(RefFinalPhaseA) + Float.parseFloat(RefFinalPhaseB) + Float.parseFloat(RefFinalPhaseC);
		String str_total_value = Float.toString(total_value);
		return str_total_value;
	}

	public static void resetInitMeterValues(){
		JSONArray j_arr = new JSONArray();
		setInitMeterValues(j_arr);
		
	}

	public static void resetFinalMeterValues(){
		JSONArray j_arr = new JSONArray();
		setFinalMeterValues(j_arr);
	}

	public static String get_kwh(String req_rack_id,JSONArray kwh_values){
		String kwh = "";
		try {
			JSONObject jobj = new JSONObject();
			String rack_id = "";
			for(int i=0; i<kwh_values.length();i++){
				jobj = kwh_values.getJSONObject(i);
				rack_id = jobj.getString("rack_id");
				if(req_rack_id.equals(rack_id)){
					kwh = jobj.getString("reading");
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_kwh: JSONException: " + e.toString());
		}
		return kwh;
	}


	public void LoadCurrentSerialComSettingFromDB(){
		ApplicationLauncher.logger.info("LoadCurrentSerialComSettingFromDB :Entry"  );
		PowerSrcCommPortID= get_port_name(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		PwrSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		LDU_CommPortID= get_port_name(ConstantApp.SOURCE_TYPE_LDU);
		LDUCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_LDU);
		RefStdCommPortID= get_port_name(ConstantApp.SOURCE_TYPE_REF_STD);
		RefStdCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_REF_STD);
		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			ICT_CommPortID= get_port_name(ConstantApp.SOURCE_TYPE_ICT);
			ICTCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_ICT);
		}
		
		//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){	
			harmonicsSrcCommPortID= get_port_name(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			harmonicsSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
		}
		
		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){	
			harmonicsSrcCommPortID= get_port_name(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			harmonicsSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
		}
		
		
		ApplicationLauncher.logger.info("LoadCurrentSerialComSettingFromDB :RefStdCommPortID: " +RefStdCommPortID  );
		ApplicationLauncher.logger.info("LoadCurrentSerialComSettingFromDB :harmonicsSrcCommPortID: " +harmonicsSrcCommPortID  );

	}

	public void setDeployedDevicesJson(){
		String project_name = ProjectExecutionController.getCurrentProjectName();
		String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();
		DeployedDevicesJson = MySQL_Controller.sp_getdeploy_devices(project_name,deploymentId);
	}

	public static void setEnergyFlowMode(String FlowMode){
		EnergyFlowMode = FlowMode;
	}

	public static String getEnergyFlowMode(){
		return EnergyFlowMode ;
	}

	public void PowerSourceEnergyFlowModeDataInit(){

		if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			if(getEnergyFlowMode().equals(ConstantPowerSourceMte.IMPORT_MODE)){
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE;
			}else if(getEnergyFlowMode().equals(ConstantPowerSourceMte.EXPORT_MODE)){
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_EXPORT_ACTIVE_UPF_ANGLE;
			}
		}else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			if(getEnergyFlowMode().equals(ConstantPowerSourceMte.IMPORT_MODE)){
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE;
			}else if(getEnergyFlowMode().equals(ConstantPowerSourceMte.EXPORT_MODE)){
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_EXPORT_REACTIVE_ZPF_ANGLE;
			}
		}
	}

	public JSONObject getDeployedDevicesJson(){

		return DeployedDevicesJson;
	}


	public boolean DisplayRefStdInit() {

		ApplicationLauncher.logger.info("RefStdCommPortID: " + RefStdCommPortID);
		ApplicationLauncher.logger.info("RefStdCommBaudRate: " + RefStdCommBaudRate);
		if (ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
			
			ApplicationLauncher.logger.info("DisplayRefStdInit: ref std configured as none connected");
			return true;

		} else{
			boolean status = false;
			if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
				ApplicationLauncher.logger.debug("DisplayRefStdInit: REFSTD_PORT_MANAGER_V2_ENABLED");
				//status = refStdPortAccessible();
				status = refStdPortAccessible_V2();
				ApplicationLauncher.logger.debug("DisplayRefStdInit: status: " + status);
				if(status) {
	    			//displayDataObj.refStdEnableSerialMonitoring();
					
	    			refStdEnableSerialMonitoring_V2();
	    		}

			}else if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				status = true;
			}else {
				status = serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
			}
			return status;
		}
	}
	
	
	public boolean  validateConveyorComPortAccessible(){

		ApplicationLauncher.logger.debug("validateConveyorComPortAccessible: Entry");
		ApplicationHomeController.update_left_status("COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		
		LoadCurrentSerialComSettingFromDB();
		//String metertype = getDeployedEM_ModelType();
		boolean status=false;
		//AllPortInitSuccess = false;





		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {

			status = displayHarmonicsSrc_Init();
			if(status){
				status= true;
				//AllPortInitSuccess = true;
				ApplicationLauncher.logger.info("validateConveyorComPortAccessible: conveyor Serial port accessable4");
			}else{
				//status= true;
				//AllPortInitSuccess = false;//true;
				//ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable5");
				ApplicationLauncher.logger.info("validateConveyorComPortAccessible: Unable to access CONVEYOR Serial Port");
				ApplicationLauncher.InformUser("Com Port Error","Unable to access CONVEYOR Serial Port", AlertType.ERROR);
			}


		}else{
			status= true;
			ApplicationLauncher.logger.info("validateConveyorComPortAccessible: conveyor Serial port accessable6");
		}






		return status;

	}




	public boolean  ValidateAllComPortAccessible(){
		
		ApplicationLauncher.logger.debug("ValidateAllComPortAccessible: Entry");
		ApplicationHomeController.update_left_status("COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		LoadCurrentSerialComSettingFromDB();
		String metertype = getDeployedEM_ModelType();
		boolean status=false;
		AllPortInitSuccess = false;
		if (DisplayRefStdInit()) {
			if(DisplayPwrSrc_Init()) {
				if( DisplayLDU_Init()) {
					

					if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
							status = DisplayICT_Init();
							if(status){
								status= true;
								AllPortInitSuccess = true;
								ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable1");
							}else{
								status= true;
								AllPortInitSuccess = true;
								ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable2");
							}
						}else{
							status= true;
							AllPortInitSuccess = true;
							ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable3");
						}
					}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED) {
						if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED) {
							status = displayHarmonicsSrc_Init();
							if(status){
								status= true;
								AllPortInitSuccess = true;
								ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable4");
							}else{
								//status= true;
								AllPortInitSuccess = false;//true;
								//ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable5");
								ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access Harmonics Serial Port");
								ApplicationLauncher.InformUser("Com Port Error","Unable to access Harmonics Serial Port", AlertType.ERROR);
							}
						}else {
							status= true;
							AllPortInitSuccess = true;
							ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable4A");
						}
						
					}else if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
						
							//status = displayHarmonicsSrc_Init();
							//if(status){
								status= true;
								AllPortInitSuccess = true;
								ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable4B");
							/*}else{
								//status= true;
								AllPortInitSuccess = false;//true;
								//ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable5");
								ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access CONVEYOR Serial Port");
								ApplicationLauncher.InformUser("Com Port Error","Unable to access CONVEYOR Serial Port", AlertType.ERROR);
							}*/
						
						
					}else{
						status= true;
						AllPortInitSuccess = true;
						ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable6");
					}

				}else {

					ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access LDU Serial Port");
					ApplicationLauncher.InformUser("Com Port Error","Unable to access LDU Serial Port", AlertType.ERROR);
					/*Alert alert = new Alert(AlertType.ERROR, "Unable to access LDU Serial Port", ButtonType.OK);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
					alert.showAndWait();*/

				}
			}
			else {

				ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access Power Source Serial Port");
				ApplicationLauncher.InformUser("Com Port Error","Unable to access Power Source Serial Port", AlertType.ERROR);
/*				Alert alert = new Alert(AlertType.ERROR, "Unable to access Power Source Serial Port", ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				alert.showAndWait();*/
			}

		}
		else {

			ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access Ref Standard Serial Port");
			/*Alert alert = new Alert(AlertType.ERROR, "Unable to access Ref Standard Serial Port", ButtonType.OK);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.showAndWait();*/
			ApplicationLauncher.InformUser("Com Port Error","Unable to access Ref Standard Serial Port", AlertType.ERROR);
		}

		return status;

	}
	
	public boolean  validateLscsSourceComPortAccessible(){
		ApplicationLauncher.logger.debug("validateLscsSourceComPortAccessible: Entry");
		ApplicationHomeController.update_left_status("COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		AllPortInitSuccess = false;
		if (DisplayRefStdInit()) {
			if(DisplayPwrSrc_Init()) {
				//if( DisplayLDU_Init()) {
					status= true;
					AllPortInitSuccess = true;
					ApplicationLauncher.logger.info("validateLscsSourceComPortAccessible: All Serial port accessable");

/*				}else {

					ApplicationLauncher.logger.info("ValidateSourceComPortAccessible: Unable to access LDU Serial Port");
					ApplicationLauncher.InformUser("Com Port Error","Unable to access LDU Serial Port", AlertType.ERROR);
					Alert alert = new Alert(AlertType.ERROR, "Unable to access LDU Serial Port", ButtonType.OK);
					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
					alert.showAndWait();

				}*/
			}
			else {

				ApplicationLauncher.logger.info("validateLscsSourceComPortAccessible: Unable to access Power Source Serial Port");
				ApplicationLauncher.InformUser("Com Port Error","Unable to access Power Source Serial Port", AlertType.ERROR);
/*				Alert alert = new Alert(AlertType.ERROR, "Unable to access Power Source Serial Port", ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				alert.showAndWait();*/
			}

		}
		else {

			ApplicationLauncher.logger.info("ValidateSourceComPortAccessible: Unable to access Ref Standard Serial Port");
			Alert alert = new Alert(AlertType.ERROR, "Unable to access Ref Standard Serial Port", ButtonType.OK);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.showAndWait();
			ApplicationLauncher.InformUser("Com Port Error","Unable to access Ref Standard Serial Port", AlertType.ERROR);
		}

		return status;

	}






	public static  void setPhaseRevPowerOn(boolean Value)
	{	
		ApplicationLauncher.logger.debug("setPhaseRevPowerOn :Entry:Value:"+Value);
		PhaseRevPowerOn=  Value;
	}



	final public boolean getPhaseRevPowerOn()
	{
		return PhaseRevPowerOn;
	}

	public static  void setSkipPhaseRev(boolean Value)
	{
		SkipPhaseRev=  Value;
	}

	final public boolean getSkipPhaseRev()
	{
		return SkipPhaseRev;
	}

	public static String getExecutionMctNctMode() {
		return executionMctNctMode;
	}




	public static void setExecutionMctNctMode(String executionMctNctMode) {
		DeviceDataManagerController.executionMctNctMode = executionMctNctMode;
	}




	public void setVoltageResetRequired(boolean value){
		VoltageResetRequired = value;
		resetLastSetPowerSourceData();
	}

	public boolean getVoltageResetRequired(){
		return VoltageResetRequired;
	}


	public float CalculateInfVoltage(float VoltPercentage){
		ApplicationLauncher.logger.info("CalculateInfVoltage :Entry");
		float RatedVolt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();//"240.0";

		float OutputVolt = RatedVolt*VoltPercentage/100 ;
		ApplicationLauncher.logger.info("Output Voltage"+ OutputVolt);
		//String op_volt = String.format("%.02f", OutputVolt);
		String op_volt = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, OutputVolt);
		OutputVolt = Float.parseFloat(op_volt);
		return OutputVolt;
	}

/*
	public float CalculateInfCurrent(){
		ApplicationLauncher.logger.info("CalculateInfCurrent :Entry");
		String RatedofCurrent = DeviceDataManagerController.getRateOfCurrent();
		String is_ib  = RatedofCurrent.substring(RatedofCurrent.length() - 2);
		String is_imax  = RatedofCurrent.substring(RatedofCurrent.length() - 4);
		ApplicationLauncher.logger.info("is_ib: " + is_ib);
		ApplicationLauncher.logger.info("is_imax: " + is_imax);
		float OutputCurrent = 0;

		if(is_ib.equals("Ib")){
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 2));
			float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();
			OutputCurrent = RatedCurrent*selectedRate ;
		}
		else if(is_imax.equals("Imax")){
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 4));
			float RatedMaxCurrent = DeviceDataManagerController.getPwrSrcR_PhaseMaxCurrentInFloat();
			OutputCurrent = RatedMaxCurrent*selectedRate ;
		}
		else{
			OutputCurrent = 0;
		}
		ApplicationLauncher.logger.info("Output Current"+ OutputCurrent);
		//String op_current = String.format("%.02f", OutputCurrent);
		String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, OutputCurrent);
		OutputCurrent = Float.parseFloat(op_current);
		return OutputCurrent;
	}*/
	
	public float CalculateInfCurrent(){
		ApplicationLauncher.logger.info("CalculateInfCurrent :Entry");
		String RatedofCurrent = DeviceDataManagerController.getRateOfCurrent();
		String is_ib  = RatedofCurrent.substring(RatedofCurrent.length() - 2);
		String is_imax  = RatedofCurrent.substring(RatedofCurrent.length() - 4);
		ApplicationLauncher.logger.info("is_ib: " + is_ib);
		ApplicationLauncher.logger.info("is_imax: " + is_imax);
		float OutputCurrent = 0;

		if(is_ib.equals("Ib")){
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 2));
			float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();
			OutputCurrent = RatedCurrent*selectedRate ;
		}
		else if(is_imax.equals("Imax")){
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 4));
			float RatedMaxCurrent = DeviceDataManagerController.getPwrSrcR_PhaseMaxCurrentInFloat();
			OutputCurrent = RatedMaxCurrent*selectedRate ;
		}
		else{
			OutputCurrent = 0;
		}
		ApplicationLauncher.logger.info("CalculateInfCurrent: Output Current1: "+ OutputCurrent);
		//String op_current = String.format("%.02f", OutputCurrent);
		//String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, OutputCurrent);
		//OutputCurrent = Float.parseFloat(op_current);
		//ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);
		
		
		String op_current = "0.0";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			 op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			 ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current3: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("Output Current4: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current5: "+ OutputCurrent);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			 op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format( OutputCurrent);
			 ApplicationLauncher.logger.info("Output Current5A: "+ OutputCurrent);
		}else{
			
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current3: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("Output Current4: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current5: "+ OutputCurrent);
			}
		}
		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("Output Current6: "+ OutputCurrent);
		
		
		return OutputCurrent;
	}
	
	public float calculateInfCurrentWithPercentage(float inputPercentage){
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage :Entry");
		String RatedofCurrent = DeviceDataManagerController.getRateOfCurrent();
		String is_ib  = RatedofCurrent.substring(RatedofCurrent.length() - 2);
		String is_imax  = RatedofCurrent.substring(RatedofCurrent.length() - 4);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: is_ib: " + is_ib);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: is_imax: " + is_imax);
		float OutputCurrent = 0;

		if(is_ib.equals("Ib")){
			//float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 2));
			float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();
			OutputCurrent = (RatedCurrent*inputPercentage)/100 ;
		}
		else if(is_imax.equals("Imax")){
			//float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 4));
			float RatedMaxCurrent = DeviceDataManagerController.getPwrSrcR_PhaseMaxCurrentInFloat();
			OutputCurrent = (RatedMaxCurrent*inputPercentage)/100 ;
		}
		else{
			OutputCurrent = 0;
		}
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: Output Current1: "+ OutputCurrent);
		//String op_current = String.format("%.02f", OutputCurrent);
		//String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, OutputCurrent);
		//OutputCurrent = Float.parseFloat(op_current);
		//ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);
		
		
		String op_current = "0.0";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			 op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current2: "+ OutputCurrent);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current3: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current4: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current5: "+ OutputCurrent);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			 op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format( OutputCurrent);
			 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current5A: "+ OutputCurrent);
		}else{
			
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current3: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current4: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current5: "+ OutputCurrent);
			}
		}
		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current6: "+ OutputCurrent);
		
		
		return OutputCurrent;
	}

	public static  void setHarmonic_data(JSONArray harmonics)
	{
		harmonic_data= harmonics;
	}



	final public JSONArray getHarmonic_data()
	{
		return harmonic_data;
	}








	public void setRefInitValues(){
		ApplicationLauncher.logger.info("setRefInitValues :Entry");
		setRefInitPhaseAReading(getCurrentPhaseAReading());
		setRefInitPhaseBReading(getCurrentPhaseBReading());
		setRefInitPhaseCReading(getCurrentPhaseCReading());
	}


	public void setRefFinalValues(){
		ApplicationLauncher.logger.info("setRefFinalValues :Entry");
		setRefFinalPhaseAReading(getCurrentPhaseAReading());
		setRefFinalPhaseBReading(getCurrentPhaseBReading());
		setRefFinalPhaseCReading(getCurrentPhaseCReading());
	}




	public static boolean isPwrSrcInitCompleted() {
		return pwrSrcInitCompleted;
	}




	public static void setPwrSrcInitCompleted(boolean pwrSrcInitCompleted) {
		DeviceDataManagerController.pwrSrcInitCompleted = pwrSrcInitCompleted;
	}




	public JSONObject CalculateResult(){
		ApplicationLauncher.logger.info("CalculateResult :Entry");
		JSONArray initValues = getInitMeterValues();
		JSONArray finalValues = getFinalMeterValues();
		Float emin = Float.parseFloat(get_Error_min());
		Float emax = Float.parseFloat(get_Error_max());
		//float ref_diff = CalculateDiff(getRefInitPhaseAReading(), getRefFinalPhaseAReading());
		float ref_diff = Float.parseFloat(getRefFinalPhaseAReading()) + Float.parseFloat(getRefFinalPhaseBReading()) + Float.parseFloat(getRefFinalPhaseCReading());
		ApplicationLauncher.logger.info("CalculateResult :ref_diff: " + ref_diff);
		JSONObject results = new JSONObject();
		JSONArray result_array = new JSONArray();
		JSONObject i_jobj = new JSONObject();
		JSONObject f_jobj = new JSONObject();
		String init_reading = "";
		String final_reading = "";
		float em_diff = 0f;
		JSONObject current_device = new JSONObject();
		int current_rack = 1;
		int ctr_ratio = 1;
		int ptr_ratio = 1;
		float ref_diff_with_ct_pt = 0f;
		float error_value = 0f;
		String str_err_value = "";
		
		try {
			for(int i=0; i<initValues.length(); i++){
				i_jobj = initValues.getJSONObject(i);
				f_jobj = finalValues.getJSONObject(i);
				init_reading = i_jobj.getString("reading");
				final_reading = f_jobj.getString("reading");
				em_diff = CalculateDiff(init_reading, final_reading);
				ApplicationLauncher.logger.info("CalculateResult :em_diff: " + em_diff);
				current_device = get_current_device(i);
				current_rack = current_device.getInt("Rack_ID");
				ctr_ratio = current_device.getInt("ctr_ratio");
				ptr_ratio = current_device.getInt("ptr_ratio");
				ref_diff_with_ct_pt = ref_diff * ctr_ratio * ptr_ratio;
				ApplicationLauncher.logger.info("CalculateResult :ref_diff_with_ct_pt: " + ref_diff_with_ct_pt);
				error_value = CalculateError(em_diff, ref_diff_with_ct_pt);
				//str_err_value = String.format("%.2f", error_value);
				str_err_value = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, error_value);
				ApplicationLauncher.logger.info("CalculateResult: error_value: " + error_value);
				JSONObject jobj = new JSONObject();
				if((emin < error_value) &&(error_value < emax)){

					jobj.put("Device_name", current_rack);
					jobj.put("Result", "P");
					jobj.put("Error_value", str_err_value);
				}
				else{
					jobj.put("Device_name", current_rack);
					jobj.put("Result", "F");
					jobj.put("Error_value", str_err_value);
				}
				result_array.put(jobj);
			}
			results.put("No_of_devices", result_array.length());
			results.put("Results", result_array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("CalculateResult :JSONException:"+ e.getMessage());
		}
		ApplicationLauncher.logger.info("CalculateResult :results:"+ results);
		return results;
	}

	public JSONObject get_current_device(int i){
		String project_name = ProjectExecutionController.getCurrentProjectName();
		JSONObject devices_deployed = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		JSONArray devices_arr = new JSONArray();
		JSONObject device = new JSONObject();
		try {
			devices_arr = devices_deployed.getJSONArray("Devices");
			device = devices_arr.getJSONObject(i);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_current_device :JSONException:"+ e.getMessage());
		}

		return device;
	}

	public float CalculateDiff(String init_value, String final_value){
		float error = ((Float.parseFloat(final_value) - Float.parseFloat(init_value)));
		return error;
	}

	public float CalculateError(float em_diff, float ref_diff){
		float errorvalue = ((em_diff/ref_diff)/100);
		return errorvalue;
	}



	public static void set_CreepDuration(Integer Duration){
		CreepDuration = Duration;

	}

	public Integer get_CreepDuration(){
		return CreepDuration;

	}

	public void set_STADuration(Integer Duration){
		STADuration = Duration;

	}

	public Integer get_STADuration(){
		return STADuration;

	}

	public void set_Error_min(String Emin){
		Error_min = Emin;

	}

	public static String get_Error_min(){
		return Error_min;

	}

	public void set_Error_max(String Emax){
		Error_max = Emax;

	}

	public static String get_Error_max(){
		return Error_max;

	}

	public void setNoOfPulses(String no_of_cycles){
		NoOfPulses = no_of_cycles;

	}

	public static String getNoOfPulses(){
		return NoOfPulses;

	}

	public static void setInfTimeDuration(int timeDuartion){
		InfTimeDuration = timeDuartion;

	}

	public static int getInfTimeDuration(){
		return InfTimeDuration;

	}


	public void set_NoOfPulseReadingToBeSkipped(JSONObject count){
		NoOfPulseReadingToBeSkipped = count;

	}

	public static JSONObject get_NoOfPulseReadingToBeSkipped(){
		return NoOfPulseReadingToBeSkipped;

	}

	public void decrement_NoOfPulseReadingToBeSkipped(int rack_id){
		try {
			int skip_count = (NoOfPulseReadingToBeSkipped.getInt(Integer.toString(rack_id))) - 1;
			NoOfPulseReadingToBeSkipped.put(Integer.toString(rack_id), skip_count);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("decrement_NoOfPulseReadingToBeSkipped :JSONException:"+ e.getMessage());
		}
	}

	public void setTestRunType(String type){
		TestRunType = type;

	}

	public static String getTestRunType(){
		return TestRunType;

	}

	public void setRateOfCurrent(String rate){
		RateOfCurrent = rate;
	}

	public static String getRateOfCurrent(){
		return RateOfCurrent;

	}

	public void setRateOfVoltage(String rate){
		RateOfVoltage = Float.parseFloat(rate);
	}

	public static Float getRateOfVoltage(){
		return RateOfVoltage;

	}

	public static void setInitMeterValues(JSONArray initMeterValues){
		InitMeterValues = initMeterValues;
	}

	public static JSONArray getInitMeterValues(){
		return InitMeterValues;

	}

	public static void setFinalMeterValues(JSONArray finalMeterValues){
		FinalMeterValues = finalMeterValues;
	}

	public static JSONArray getFinalMeterValues(){
		return FinalMeterValues;

	}

	public static void setRefInitPhaseAReading(String ref_reading){
		Ref_Init_PhaseA_reading = ref_reading;
	}

	public static String getRefInitPhaseAReading(){
		return Ref_Init_PhaseA_reading;

	}

	public static void setRefInitPhaseBReading(String ref_reading){
		Ref_Init_PhaseB_reading = ref_reading;
	}

	public static String getRefInitPhaseBReading(){
		return Ref_Init_PhaseB_reading;

	}

	public static void setRefInitPhaseCReading(String ref_reading){
		Ref_Init_PhaseC_reading = ref_reading;
	}

	public static String getRefInitPhaseCReading(){
		return Ref_Init_PhaseC_reading;

	}

	public static void setRefFinalPhaseAReading(String ref_reading){
		Ref_Final_PhaseA_reading = ref_reading;
	}

	public static String getRefFinalPhaseAReading(){
		return Ref_Final_PhaseA_reading;

	}

	public static void setRefFinalPhaseBReading(String ref_reading){
		Ref_Final_PhaseB_reading = ref_reading;
	}

	public static String getRefFinalPhaseBReading(){
		return Ref_Final_PhaseB_reading;

	}

	public static void setRefFinalPhaseCReading(String ref_reading){
		Ref_Final_PhaseC_reading = ref_reading;
	}

	public static String getRefFinalPhaseCReading(){
		return Ref_Final_PhaseC_reading;

	}

	public static void setCurrentPhaseAReading(String reading){
		current_phaseA_reading = reading;
	}

	public static String getCurrentPhaseAReading(){
		return current_phaseA_reading;

	}

	public static void setCurrentPhaseBReading(String reading){
		current_phaseB_reading = reading;
	}

	public static String getCurrentPhaseBReading(){
		return current_phaseB_reading;

	}

	public static void setCurrentPhaseCReading(String reading){
		current_phaseC_reading = reading;
	}

	public static String getCurrentPhaseCReading(){
		return current_phaseC_reading;

	}

	public static void setHarmonics(JSONArray harmonic_data){
		harmonics = harmonic_data;
	}

	public static JSONArray getHarmonics(){
		return harmonics;

	}

	public static void setcutnuetral_flag(boolean data){
		cutnuetral_flag = data;
	}

	public static boolean getcutnuetral_flag(){
		return cutnuetral_flag;

	}

	public static void setcutnuetral_wait_flag(boolean data){
		cutnuetral_wait_flag = data;
	}

	public static boolean getcutnuetral_wait_flag(){
		return cutnuetral_wait_flag;

	}
	public void set_WarmupDuration(Integer Duration){
		WarmupDuration = Duration;

	}

	public Integer get_WarmupDuration(){
		return WarmupDuration;

	}

	public static void setPwrSrcR_PhaseVoltInFloat(String VoltageValue){
		PwrSrcR_PhaseVoltInFloat  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcR_PhaseVoltInFloat(){
		return Float.valueOf(PwrSrcR_PhaseVoltInFloat) ;

	}

	public static void setPwrSrcR_PhaseCurrentInFloat(String CurrentValue){
		PwrSrcR_PhaseCurrentInFloat  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcR_PhaseCurrentInFloat(){
		return Float.valueOf(PwrSrcR_PhaseCurrentInFloat) ;

	}
	


	public static void set_PwrSrcR_PhaseDegreePhase(String DegreePhase){
		PwrSrcR_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcR_PhaseDegreePhase(){
		return PwrSrcR_PhaseDegreePhase;

	}
	
	public static String get_PwrSrcR_PhaseDegreePhaseStr(){
		
		
		String phaseAngleDegree = "123.4";
		//ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr: PwrSrcR_PhaseDegreePhase: " + PwrSrcR_PhaseDegreePhase);
		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			if(PwrSrcR_PhaseDegreePhase>=0){
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcR_PhaseDegreePhase);
				//ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr: phaseAngleDegree1: " + phaseAngleDegree);
				
			}else {
				float reverseDegree = 360.0f + PwrSrcR_PhaseDegreePhase;
				//ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr: reverseDegree2: " + reverseDegree);
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
				//ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr: phaseAngleDegree3: " + phaseAngleDegree);
			}
		}else{
			phaseAngleDegree = 	String.valueOf(PwrSrcR_PhaseDegreePhase);
			//ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr: phaseAngleDegree4: " + phaseAngleDegree);
		}
		return phaseAngleDegree;

	}

	public static void setPwrSrcY_PhaseVoltInFloat(String VoltageValue){
		PwrSrcY_PhaseVoltInFloat  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcY_PhaseVoltInFloat(){
		return Float.valueOf(PwrSrcY_PhaseVoltInFloat) ;

	}

	public static void setPwrSrcY_PhaseCurrentInFloat(String CurrentValue){
		PwrSrcY_PhaseCurrentInFloat  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcY_PhaseCurrentInFloat(){
		return Float.valueOf(PwrSrcY_PhaseCurrentInFloat) ;

	}

	public void set_PwrSrcY_PhaseDegreePhase(String DegreePhase){
		PwrSrcY_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcY_PhaseDegreePhase(){
		return PwrSrcY_PhaseDegreePhase;

	}
	
	public static String get_PwrSrcY_PhaseDegreePhaseStr(){
		
		
		String phaseAngleDegree = "123.4";
		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){	
			//phaseAngleDegree = 	ConstantBofaPowerSource.BOFA_DEGREE_RESOLUTION.format(PwrSrcY_PhaseDegreePhase);
			if(PwrSrcY_PhaseDegreePhase>=0){
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcY_PhaseDegreePhase);
				
			}else {
				float reverseDegree = 360.0f + PwrSrcY_PhaseDegreePhase;
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
			}
		}else{
			phaseAngleDegree = 	String.valueOf(PwrSrcY_PhaseDegreePhase);
		}
		return phaseAngleDegree;

	}

	public static void setPwrSrcB_PhaseVoltInFloat(String VoltageValue){
		PwrSrcB_PhaseVoltInFloat  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcB_PhaseVoltInFloat(){
		return Float.valueOf(PwrSrcB_PhaseVoltInFloat) ;

	}

	public static void setPwrSrcB_PhaseCurrentInFloat(String CurrentValue){
		PwrSrcB_PhaseCurrentInFloat  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcB_PhaseCurrentInFloat(){
		return Float.valueOf(PwrSrcB_PhaseCurrentInFloat) ;

	}

	public void set_PwrSrcB_PhaseDegreePhase(String DegreePhase){
		PwrSrcB_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcB_PhaseDegreePhase(){
		return PwrSrcB_PhaseDegreePhase;

	}
	
	public static String get_PwrSrcB_PhaseDegreePhaseStr(){
		
		
		String phaseAngleDegree = "123.4";
		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			//phaseAngleDegree = 	ConstantBofaPowerSource.BOFA_DEGREE_RESOLUTION.format(PwrSrcB_PhaseDegreePhase);
			if(PwrSrcB_PhaseDegreePhase>=0){
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcB_PhaseDegreePhase);
				
			}else {
				float reverseDegree = 360.0f + PwrSrcB_PhaseDegreePhase;
				phaseAngleDegree = 	ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
			}
		}else{
			phaseAngleDegree = 	String.valueOf(PwrSrcB_PhaseDegreePhase);
		}
		return phaseAngleDegree;

	}

	public static void set_PwrSrc_Freq(String Frequency){

			PwrSrcR_PhaseFreq = Float.valueOf(Frequency);

	}

	public static float get_PwrSrc_Freq(){
		return PwrSrcR_PhaseFreq;

	}
	
	public static String get_PwrSrc_FreqStr(){
		
		String freqStr = "50.99";
		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
					

			freqStr = 	ConstantPowerSourceBofa.BOFA_FREQ_RESOLUTION.format(PwrSrcR_PhaseFreq);
		}else{
			freqStr = 	String.valueOf(PwrSrcR_PhaseFreq);
		}
		return freqStr;

	}

	public static String getDeployedEM_ModelType(){
		return DeployedEM_ModelType;

	}

	public static void setDeployedEM_ModelType(String type){
		DeployedEM_ModelType = type;


	}	
	

	
	public static String getDeployedEM_CT_Type(){
		return DeployedEM_CT_Type;

	}

	public static void setDeployedEM_CT_Type(String type){
		DeployedEM_CT_Type = type;


	}	
	
	public void setDeployedDevicesJson(String project_name,String deploymentId){
/*		String project_name = ProjectExecutionController.getCurrentProjectName();
		String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();*/
		DeployedDevicesJson = MySQL_Controller.sp_getdeploy_devices(project_name,deploymentId);
	}

	public static String getDutImpulsesPerUnit(){
		return dutImpulsesPerUnit;

	}

	public static void setDutImpulsesPerUnit(String impulses){
		dutImpulsesPerUnit = impulses;

	}

	public static int getReadingToBeRead(){
		return ReadingToBeRead;

	}

	public static void DecrementReadingToBeRead(){
		ReadingToBeRead--;
	}

	public void setReadingToBeRead(int no_of_readings){
		ReadingToBeRead = no_of_readings;

	}


	public static void setCreepNoOfPulses(String NoOfPulse){
		CreepNoOfPulses = NoOfPulse;

	}

	public static String getCreepNoOfPulses(){
		return CreepNoOfPulses ;

	}

	public static void setSTANoOfPulses(String NoOfPulse){
		STANoOfPulses = NoOfPulse;

	}

	public static String getSTANoOfPulses(){
		return STANoOfPulses ;

	}

	public static String getConstTestPower(){
		return ConstTestPower ;

	}

	public static void setConstTestPower(String power){
		ConstTestPower = power;

	}


	public static float getPercentageOfVoltage(){
		return PercentageOfVoltage ;

	}

	public static void setPercentageOfVoltage(String VoltageValue){
		PercentageOfVoltage  = Float.valueOf(VoltageValue) ;

	}

	public static float getPercentageOfCurrent(){
		return PercentageOfCurrent ;

	}

	public static void setPercentageOfCurrent(String CurrentValue){
		PercentageOfCurrent  = Float.valueOf(CurrentValue) ;

	}

	public static void setPwrSrc_Percent_VoltU1(String VoltageValue){
		PwrSrc_Percent_VoltU1  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrc_Percent_VoltU1(){
		return Float.valueOf(PwrSrc_Percent_VoltU1) ;

	}

	public static void setPwrSrc_Percent_VoltU2(String VoltageValue){
		PwrSrc_Percent_VoltU2  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrc_Percent_VoltU2(){
		return Float.valueOf(PwrSrc_Percent_VoltU2) ;

	}

	public static void setPwrSrc_Percent_VoltU3(String VoltageValue){
		PwrSrc_Percent_VoltU3  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrc_Percent_VoltU3(){
		return Float.valueOf(PwrSrc_Percent_VoltU3) ;

	}

	public static void setPwrSrcCustomInFloat_VoltU1(String VoltageValue){
		PwrSrcCustomInFloat_VoltU1  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcCustomInFloat_VoltU1(){
		return Float.valueOf(PwrSrcCustomInFloat_VoltU1) ;

	}

	public static void setPwrSrcCustomInFloat_VoltU2(String VoltageValue){
		PwrSrcCustomInFloat_VoltU2  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcCustomInFloat_VoltU2(){
		return Float.valueOf(PwrSrcCustomInFloat_VoltU2) ;

	}

	public static void setPwrSrcCustomInFloat_VoltU3(String VoltageValue){
		PwrSrcCustomInFloat_VoltU3  = Float.valueOf(VoltageValue) ;

	}

	public static float getPwrSrcCustomInFloat_VoltU3(){
		return Float.valueOf(PwrSrcCustomInFloat_VoltU3) ;

	}

	public static void setPwrSrcCustomInFloat_CurrentI1(String CurrentValue){
		PwrSrcCustomInFloat_CurrentI1  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcCustomInFloat_CurrentI1(){
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI1) ;

	}

	public static void setPwrSrcCustomInFloat_CurrentI2(String CurrentValue){
		PwrSrcCustomInFloat_CurrentI2  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcCustomInFloat_CurrentI2(){
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI2) ;

	}

	public static void setPwrSrcCustomInFloat_CurrentI3(String CurrentValue){
		PwrSrcCustomInFloat_CurrentI3  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcCustomInFloat_CurrentI3(){
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI3) ;

	}

/*	public static void setPwrSrc_Phase1(String Value){
		PwrSrc_Phase1  = Float.valueOf(Value) ;

	}

	public static float getPwrSrc_Phase1(){
		return Float.valueOf(PwrSrc_Phase1) ;

	}

	public static void setPwrSrc_Phase2(String Value){
		PwrSrc_Phase2  = Float.valueOf(Value) ;

	}

	public static float getPwrSrc_Phase2(){
		return Float.valueOf(PwrSrc_Phase2) ;

	}

	public static void setPwrSrc_Phase3(String Value){
		PwrSrc_Phase3  = Float.valueOf(Value) ;

	}

	public static float getPwrSrc_Phase3(){
		return Float.valueOf(PwrSrc_Phase3) ;

	}
*/
	public static void setPwrSrcR_PhaseMaxCurrentInFloat(String CurrentValue){
		PwrSrcR_PhaseMaxCurrentInFloat  = Float.valueOf(CurrentValue) ;

	}

	public static float getPwrSrcR_PhaseMaxCurrentInFloat(){
		return Float.valueOf(PwrSrcR_PhaseMaxCurrentInFloat) ;

	}
	
	
	
	public void setLastSetRSS_Pulse_Rate(String pulseValue){
		LastSetRSS_Pulse_Rate  = pulseValue ;
	}

	public String getLastSetRSS_Pulse_Rate(){
		return LastSetRSS_Pulse_Rate;

	}
	
	public void clearLastSetRSS_Pulse_Rate(){
		LastSetRSS_Pulse_Rate  = "" ;
	}

	public void setRSSPulseRate(String pulseValue){
		RSS_Pulse_Rate  = pulseValue ;
	}

	public String getRSSPulseRate(){
		return RSS_Pulse_Rate;

	}

	public static void setPhaseDegreeOutput(ArrayList<String> phasedegreeoutput){
		PhaseDegreeOutput  = phasedegreeoutput ;
	}

	public static ArrayList<String> getPhaseDegreeOutput(){
		return PhaseDegreeOutput;

	}


	public boolean DisplayPwrSrc_Init() {

		boolean status = false;
		
		if (ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE){
			
			ApplicationLauncher.logger.info("DisplayPwrSrc_Init: power source configured as none connected");
			return true;

		} else{
				
				
				
				if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
					//status = BofaManager.enableSerialPortAndMonitor();
					if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
						status = BofaManager.enableSerialPortAndMonitorV2();
					}else {
						status = BofaManager.enableSerialPortAndMonitor();
					}
				}else{
					status = serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
				}
		}
		return status;
	}

	public boolean DisplayLDU_Init() {

		if (ProcalFeatureEnable.LDU_CONNECTED_NONE){
			
			ApplicationLauncher.logger.info("DisplayLDU_Init: LDU configured as none connected");
			return true;

		} else{
			boolean status =serialDM_Obj.LDU_Init(LDU_CommPortID, LDUCommBaudRate);
			return status;
		}

	}
	
	public boolean DisplayICT_Init() {

		boolean status =serialDM_Obj.ictInit(ICT_CommPortID, ICTCommBaudRate);
		return status;


	}
	public boolean displayHarmonicsSrc_Init() {

		boolean status = false;
		try{
			status = serialDM_Obj.harmonicsSrcInit(harmonicsSrcCommPortID, harmonicsSrcCommBaudRate);
		}catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("displayHarmonicsSrc_Init :Exception: "+ e.getMessage());
		}
		return status;


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



	public static String getPowerData(TextField refTextField){
		return refTextField.getText();
	}





	public void StopReadingRefStdData(){
		ApplicationLauncher.logger.info("Stopped Reading Ref Data!");

		setRefStdReadDataFlag( false);
	}

	public void StopReadingLDU_ErrorReadData(){
		ApplicationLauncher.logger.info("Stopped Reading LDU_ErrorReadData!");

		setLDU_ReadDataFlag( false);
	}

	public String get_port_name(String src_type){
		String port_name = "";
		try {
			port_name = DevicePortSetupController.get_device_settings(src_type).getString("port_name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_port_name :JSONException:"+ e.getMessage());
		}
		return port_name;
	}

	public String get_baud_rate(String src_type){
		String baud_rate ="";
		try {
			baud_rate = DevicePortSetupController.get_device_settings(src_type).getString("baud_rate");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("get_baud_rate :JSONException:"+ e.getMessage());
		}
		return baud_rate;
	}

	public void setAllPhaseOutput(){
		setR_PhaseOutputVoltage(getPwrSrcR_PhaseVoltInFloat());
		setR_PhaseOutputCurrent(getPwrSrcR_PhaseCurrentInFloat());
		setY_PhaseOutputVoltage(getPwrSrcY_PhaseVoltInFloat());
		setY_PhaseOutputCurrent(getPwrSrcY_PhaseCurrentInFloat());
		manipulateY_PhaseCurrentFor3PhaseDelta();
		setB_PhaseOutputVoltage(getPwrSrcB_PhaseVoltInFloat());
		setB_PhaseOutputCurrent(getPwrSrcB_PhaseCurrentInFloat());
	}

	public void setWarmupParameters(JSONObject param, JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setWarmupParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		String rated_current = modelparams.getString("basic_current_ib");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String warmup_duration = param.getString("time_duration");
		ApplicationLauncher.logger.info("setWarmupParameters: warmup_duration: " + warmup_duration);
		String Volt_percent = param.getString("voltage");
		String no_of_pulses = param.getString("inf_pulses");
		
		int average =  Integer.parseInt(param.getString("inf_average"));
		
		int warmup_duration_insec = (Integer.parseInt(warmup_duration)) * 60; 
		ApplicationLauncher.logger.info("warmup_duration_insec: " + warmup_duration_insec );
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		set_WarmupDuration(warmup_duration_insec);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		set_PwrSrcR_PhaseDegreePhase("0.0");
		set_PwrSrcY_PhaseDegreePhase("0.0");
		set_PwrSrcB_PhaseDegreePhase("0.0");
		if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			/*        	set_PwrSrcR_PhaseDegreePhase("0.0");
        	set_PwrSrcY_PhaseDegreePhase("0.0");
        	set_PwrSrcB_PhaseDegreePhase("0.0");*/

			set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
			set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
			set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
		}
		else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){

			set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
			set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
			set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));


		}

		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float finalVolt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(Volt_percent));

		setR_PhaseOutputVoltage(finalVolt_value);
		setR_PhaseOutputCurrent(getPwrSrcR_PhaseCurrentInFloat());
		setY_PhaseOutputVoltage(finalVolt_value);
		setY_PhaseOutputCurrent(getPwrSrcY_PhaseCurrentInFloat());
		setB_PhaseOutputVoltage(finalVolt_value);
		setB_PhaseOutputCurrent(getPwrSrcB_PhaseCurrentInFloat());
		setPowerSrcOnTimerValue(WarmupDuration);

		manipulateY_PhaseCurrentFor3PhaseDelta();

		set_PwrSrc_Freq(frequency);
		setDutImpulsesPerUnit(impulses_per_unit);
		setTestRunType( ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);

		ArrayList<String> All_phases = new ArrayList<String>();
		All_phases.add("All");
		All_phases.add("0.0");
		setPhaseDegreeOutput(All_phases);
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(getPwrSrcR_PhaseCurrentInFloat());
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(getPwrSrcR_PhaseCurrentInFloat(),finalVolt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
		ApplicationLauncher.logger.info("setWarmupParameters:  get_WarmupDuration: " + get_WarmupDuration() );
	}

	public void setSelfHeatingWarmupParameters(JSONObject param, JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setSelfHeatingWarmupParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String warmup_duration = param.getString("time_duration");
		String Volt_percent = param.getString("voltage");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		ApplicationLauncher.logger.info("setSelfHeatingWarmupParameters: warmup_duration: " + warmup_duration);
		int warmup_duration_insec = (Integer.parseInt(warmup_duration)) * 60; 
		ApplicationLauncher.logger.info("setSelfHeatingWarmupParameters: warmup_duration_insec: " + warmup_duration_insec );

		set_WarmupDuration(warmup_duration_insec);
		no_of_pulses="1";// just to dispaly WFR on the display
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat("0.0");
		set_PwrSrcR_PhaseDegreePhase("0.0");
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat("0.0");
		set_PwrSrcY_PhaseDegreePhase("0.0");
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat("0.0");
		set_PwrSrcB_PhaseDegreePhase("0.0");

		if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){

			set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
			set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
			set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
		}
		else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){

			set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
			set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
			set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));


		}

		//setAllPhaseOutput();
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float finalVolt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(Volt_percent));

		setR_PhaseOutputVoltage(finalVolt_value);
		setR_PhaseOutputCurrent(getPwrSrcR_PhaseCurrentInFloat());
		setY_PhaseOutputVoltage(finalVolt_value);
		setY_PhaseOutputCurrent(getPwrSrcY_PhaseCurrentInFloat());
		manipulateY_PhaseCurrentFor3PhaseDelta();
		setB_PhaseOutputVoltage(finalVolt_value);
		setB_PhaseOutputCurrent(getPwrSrcB_PhaseCurrentInFloat());
		setPowerSrcOnTimerValue(WarmupDuration);



		set_PwrSrc_Freq(frequency);
		setDutImpulsesPerUnit(impulses_per_unit);
		setTestRunType( ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);

		ArrayList<String> All_phases = new ArrayList<String>();
		All_phases.add("All");
		All_phases.add("0.0");
		setPhaseDegreeOutput(All_phases);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(getPwrSrcR_PhaseCurrentInFloat());
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(getPwrSrcR_PhaseCurrentInFloat(),finalVolt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
						
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
		ApplicationLauncher.logger.info("setSelfHeatingWarmupParameters:  get_WarmupDuration: " + get_WarmupDuration() );
	}

	public void setVoltUnbalanceParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setVoltUnbalanceParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");

		String test_case_name = param.getString("test_case_name");

		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);
		String volt_unb_phase = Extract_V_phase_From_TP_Name(test_case_name);

		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String volt_u1 = param.getString("inf_voltage_unbalance_u1");
		String volt_u2 = param.getString("inf_voltage_unbalance_u2");
		String volt_u3 = param.getString("inf_voltage_unbalance_u3");
		String testruntype = param.getString("testruntype");
		int time_duration_insec =Integer.parseInt(time_duration); //editedByMO
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);

		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);

		
		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setPwrSrc_Percent_VoltU1(volt_u1);
		setPwrSrc_Percent_VoltU2(volt_u2);
		setPwrSrc_Percent_VoltU3(volt_u3);
		setDutImpulsesPerUnit(impulses_per_unit);
		//ApplicationLauncher.logger.debug("setVoltUnbalanceParameters : volt_u1 : "+ volt_u1);
		//ApplicationLauncher.logger.debug("setVoltUnbalanceParameters : volt_u2 : "+ volt_u2);
		//ApplicationLauncher.logger.debug("setVoltUnbalanceParameters : volt_u3 : "+ volt_u3);

		float percentage_of_R_volt = getPwrSrc_Percent_VoltU1();
		float percentage_of_Y_volt = getPwrSrc_Percent_VoltU2();
		float percentage_of_B_volt = getPwrSrc_Percent_VoltU3();
		if (ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND){
			ApplicationLauncher.logger.info("setVoltUnbalanceParameters : Workaround for zero voltage issue on pwer source: Entry");
			int NoOfPhaseVoltZeroCount =0 ;
			if(percentage_of_R_volt == 0.0f){
				NoOfPhaseVoltZeroCount++;
			}
			if(percentage_of_Y_volt == 0.0f){
				NoOfPhaseVoltZeroCount++;
			}
			if(percentage_of_B_volt == 0.0f){
				NoOfPhaseVoltZeroCount++;
			}
			if (NoOfPhaseVoltZeroCount == 2){
				if(percentage_of_R_volt == 0.0f){
					if( Float.valueOf(rated_volt)< 100){
						percentage_of_R_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_LESS_THAN_100V;
					}else if( Float.valueOf(rated_volt) >= 100){
						percentage_of_R_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_GREATER_THAN_100V;
					}
				}
				if(percentage_of_Y_volt == 0.0f){
					if( Float.valueOf(rated_volt)< 100){
						percentage_of_Y_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_LESS_THAN_100V;
					}else if( Float.valueOf(rated_volt) >= 100){
						percentage_of_Y_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_GREATER_THAN_100V;
					}
				}
				if(percentage_of_B_volt == 0.0f){
					if( Float.valueOf(rated_volt)< 100){
						percentage_of_B_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_LESS_THAN_100V;
					}else if( Float.valueOf(rated_volt) >= 100){
						percentage_of_B_volt = ConstantAppConfig.VOLT_UNBALANCE_ZERO_VOLT_PERCENTAGE_GREATER_THAN_100V;
					}
				}
			}
		}
		float SelectedCurrentValue = CalculateInfCurrent();

		float Selected_R_VoltageValue = CalculateInfVoltage(percentage_of_R_volt);
		float Selected_Y_VoltageValue = CalculateInfVoltage(percentage_of_Y_volt);
		float Selected_B_VoltageValue = CalculateInfVoltage(percentage_of_B_volt);
		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		int timeduration = getInfTimeDuration();
		String e_min = get_Error_min();
		String e_max = get_Error_max();
		String No_of_pulses = getNoOfPulses();

		setAllPhaseParametersForVoltUnbalance(Selected_R_VoltageValue, Selected_Y_VoltageValue, Selected_B_VoltageValue, 
				SelectedCurrentValue,volt_unb_phase, phasedegree.get(1));
		setPowerSrcOnTimerValue(timeduration);
		setVolt_Unbalanced_PowerOn(true);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		ArrayList<String> phase_enabled = new ArrayList<String>();
		phase_enabled.add(volt_unb_phase);
		phase_enabled.add("");//dummy value(Phase only required)
		setPhaseDegreeOutput(phase_enabled);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,Selected_R_VoltageValue);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setCustomRatingParameters(JSONObject cus_param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setCustomRatingParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		JSONArray cus_param_array = cus_param.getJSONArray("test_details");
		JSONObject param = cus_param_array.getJSONObject(0);
		String volt_u1 = param.getString("cus_voltage_u1");
		String volt_u2 = param.getString("cus_voltage_u2");
		String volt_u3 = param.getString("cus_voltage_u3");
		String current_i1 = param.getString("cus_current_i1");
		String current_i2 = param.getString("cus_current_i2");
		String current_i3 = param.getString("cus_current_i3");
		String phase_1 = param.getString("cus_phase_ph1");
		String phase_2 = param.getString("cus_phase_ph2");
		String phase_3 = param.getString("cus_phase_ph3");
		String cus_frequency = param.getString("cus_frequency");
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		int time_duration_insec = (int) ((Float.parseFloat(time_duration)) * 60); 
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);

		phase_1 = CalculateLagLeadAngle(phase_1).get(1);
		phase_2 = CalculateLagLeadAngle(phase_2).get(1);
		phase_3 = CalculateLagLeadAngle(phase_3).get(1);

		set_Error_min(emin);
		set_Error_max(emax);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		setPwrSrcCustomInFloat_VoltU1(volt_u1);
		setPwrSrcCustomInFloat_VoltU2(volt_u2);
		setPwrSrcCustomInFloat_VoltU3(volt_u3);
		setPwrSrcCustomInFloat_CurrentI1(current_i1);
		setPwrSrcCustomInFloat_CurrentI2(current_i2);
		setPwrSrcCustomInFloat_CurrentI3(current_i3);
		set_PwrSrcR_PhaseDegreePhase(phase_1);
		set_PwrSrcY_PhaseDegreePhase(phase_2);
		set_PwrSrcB_PhaseDegreePhase(phase_3);
		set_PwrSrc_Freq(cus_frequency);
		setInfTimeDuration(time_duration_insec);
		setDutImpulsesPerUnit(impulses_per_unit);
		setTestRunType(testruntype);

		if(getPwrSrcCustomInFloat_VoltU1() == 0.0f){
			set_PwrSrcR_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_VoltU2() == 0.0f){
			set_PwrSrcY_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_VoltU3() == 0.0f){
			set_PwrSrcB_PhaseDegreePhase("0.0");
		}

		int timeduration = getInfTimeDuration();

		setR_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU1());
		setY_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU2());
		setB_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU3());
		setR_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI1());
		setY_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI2());
		setB_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI3());
		setR_PhaseOutputPhase(get_PwrSrcR_PhaseDegreePhase());
		setY_PhaseOutputPhase(get_PwrSrcY_PhaseDegreePhase());
		setB_PhaseOutputPhase(get_PwrSrcB_PhaseDegreePhase());
		setPowerSrcOnTimerValue(timeduration);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(getPwrSrcCustomInFloat_CurrentI1());
		//if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(getPwrSrcCustomInFloat_CurrentI1(),getPwrSrcCustomInFloat_VoltU1());
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		//}
		
		/*else{
			if(!ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
			
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxVoltage);
			}
		}*/
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}
	
	public void setManualModeParameters(Data_PowerSourceSetTarget sourceSetTargetData, boolean threePhaseSelected){//JSONObject cus_param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setManualModeParameters : Entry");
/*		String rated_volt = modelparams.getString("rated_voltage_vd");
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		JSONArray cus_param_array = cus_param.getJSONArray("test_details");
		JSONObject param = cus_param_array.getJSONObject(0);*/
		String volt_u1 = sourceSetTargetData.getR_PhaseVoltageDisplayData();//getparam.getString("cus_voltage_u1");
		String volt_u2 = sourceSetTargetData.getY_PhaseVoltageDisplayData();//param.getString("cus_voltage_u2");
		String volt_u3 = sourceSetTargetData.getB_PhaseVoltageDisplayData();// param.getString("cus_voltage_u3");
		String current_i1 = sourceSetTargetData.getR_PhaseCurrentDisplayData();//param.getString("cus_current_i1");
		String current_i2 = sourceSetTargetData.getY_PhaseCurrentDisplayData();//param.getString("cus_current_i2");
		String current_i3 = sourceSetTargetData.getB_PhaseCurrentDisplayData();//param.getString("cus_current_i3");
		String phase_1 = sourceSetTargetData.getR_PhasePowerFactorData();//param.getString("cus_phase_ph1");
		String phase_2 = sourceSetTargetData.getY_PhasePowerFactorData();//param.getString("cus_phase_ph2");
		String phase_3 = sourceSetTargetData.getB_PhasePowerFactorData();//param.getString("cus_phase_ph3");
		String cus_frequency = sourceSetTargetData.getFreqData();//param.getString("cus_frequency");
		if(!threePhaseSelected){
			volt_u2 = "0.0";
			volt_u3 = "0.0";
			current_i2 = "0.0";
			current_i3 = "0.0";
			phase_2 = "1.0";
			phase_3 = "1.0";
		}
		//String emin = param.getString("inf_emin");
		//String emax = param.getString("inf_emax");
		//String no_of_pulses = param.getString("inf_pulses");
		//int average =  Integer.parseInt(param.getString("inf_average"));
		//String time_duration = param.getString("time_duration");
		//String SkipReadingCount = param.getString("skip_reading_count");
		//String testruntype = param.getString("testruntype");
		//int time_duration_insec = (int) ((Float.parseFloat(time_duration)) * 60); 
		//int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		
		phase_1 = CalculateLagLeadAngle(phase_1).get(1);
		phase_2 = CalculateLagLeadAngle(phase_2).get(1);
		phase_3 = CalculateLagLeadAngle(phase_3).get(1);
		
		ApplicationLauncher.logger.info("setManualModeParameters : phase_1: " + phase_1);
		ApplicationLauncher.logger.info("setManualModeParameters : phase_2: " + phase_2);
		ApplicationLauncher.logger.info("setManualModeParameters : phase_3: " + phase_3);

//		set_Error_min(emin);
//		set_Error_max(emax);
//		setNoOfPulses(no_of_pulses);
///		setAverageNoOfLduReadingRequired(average);
		//setPwrSrcR_PhaseVoltInFloat(rated_volt);
		//setPwrSrcR_PhaseCurrentInFloat(rated_current);
		//setPwrSrcY_PhaseVoltInFloat(rated_volt);
		//setPwrSrcY_PhaseCurrentInFloat(rated_current);
		//setPwrSrcB_PhaseVoltInFloat(rated_volt);
		//setPwrSrcB_PhaseCurrentInFloat(rated_current);
		//setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		setPwrSrcCustomInFloat_VoltU1(volt_u1);
		setPwrSrcCustomInFloat_VoltU2(volt_u2);
		setPwrSrcCustomInFloat_VoltU3(volt_u3);
		setPwrSrcCustomInFloat_CurrentI1(current_i1);
		setPwrSrcCustomInFloat_CurrentI2(current_i2);
		setPwrSrcCustomInFloat_CurrentI3(current_i3);
		set_PwrSrcR_PhaseDegreePhase(phase_1);
		set_PwrSrcY_PhaseDegreePhase(phase_2);
		set_PwrSrcB_PhaseDegreePhase(phase_3);
		set_PwrSrc_Freq(cus_frequency);
//		setInfTimeDuration(time_duration_insec);
//		setDutImpulsesPerUnit(impulses_per_unit);
		//setTestRunType(testruntype);

		if(getPwrSrcCustomInFloat_VoltU1() == 0.0f){
			set_PwrSrcR_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_VoltU2() == 0.0f){
			set_PwrSrcY_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_VoltU3() == 0.0f){
			set_PwrSrcB_PhaseDegreePhase("0.0");
		}
		
		if(getPwrSrcCustomInFloat_CurrentI1() == 0.0f){
			set_PwrSrcR_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_CurrentI2() == 0.0f){
			set_PwrSrcY_PhaseDegreePhase("0.0");
		}
		if(getPwrSrcCustomInFloat_CurrentI3() == 0.0f){
			set_PwrSrcB_PhaseDegreePhase("0.0");
		}

		int timeduration = getInfTimeDuration();

		setR_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU1());
		setY_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU2());
		setB_PhaseOutputVoltage(getPwrSrcCustomInFloat_VoltU3());
		setR_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI1());
		setY_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI2());
		setB_PhaseOutputCurrent(getPwrSrcCustomInFloat_CurrentI3());
		setR_PhaseOutputPhase(get_PwrSrcR_PhaseDegreePhase());
		setY_PhaseOutputPhase(get_PwrSrcY_PhaseDegreePhase());
		setB_PhaseOutputPhase(get_PwrSrcB_PhaseDegreePhase());
//		setPowerSrcOnTimerValue(timeduration);
//		ArrayList<Integer> devices_mounted = getDevicesMount();
//		setDevicesToBeRead(devices_mounted);
//		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
//				no_of_pulse_reading_skipped);
//		set_NoOfPulseReadingToBeSkipped(skip_reading_json);

/*			SetPulseConstantDataWithVoltageAndCurrent(getPwrSrcCustomInFloat_CurrentI1(),getPwrSrcCustomInFloat_VoltU1());
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{*/
				//String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				//String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				//refStdUpdateConfigurationSettings(maxVoltage,maxVoltage);
//			}
			

		

		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setConstTestParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setConstTestParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");

		String test_case_name = param.getString("test_case_name");

		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);

		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String testruntype = param.getString("testruntype");
		String volt1_percent = param.getString("voltage");
		String volt2_percent = param.getString("voltage");
		String volt3_percent = param.getString("voltage");
		int time_duration_insec = (Integer.parseInt(time_duration)) * 60; 
		String power = param.getString("power");
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );
		ApplicationLauncher.logger.info("frequency: " + frequency );
		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrc_Percent_VoltU1(volt1_percent);
		setPwrSrc_Percent_VoltU2(volt2_percent);
		setPwrSrc_Percent_VoltU3(volt3_percent);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setConstTestPower(power);
		setDutImpulsesPerUnit(impulses_per_unit);
		
		float SelectedCurrentValue = CalculateInfCurrent();
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt1_percent));
		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		
		setAllPhaseParameters(final_volt_value, final_volt_value, final_volt_value, 
				SelectedCurrentValue, phasedegree);
		
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		int no_of_pulse_reading_skipped = 0;
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(getPwrSrcR_PhaseCurrentInFloat());
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			//SetPulseConstantDataWithVoltageAndCurrent(getPwrSrcR_PhaseCurrentInFloat(),getPwrSrcR_PhaseVoltInFloat());
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
				
				/*if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){

				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setHarmonicParameters(JSONObject param, JSONObject modelparams, JSONArray harmonics) throws JSONException{
		ApplicationLauncher.logger.info("setHarmonicParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String test_case_name = param.getString("test_case_name");

		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);

		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		String volt_percent = param.getString("voltage");
		//int time_duration_insec = (Integer.parseInt(time_duration)) * 60; 
		int time_duration_insec = Integer.parseInt(time_duration); //editedbyMO
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );

		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setHarmonics(harmonics);
		setDutImpulsesPerUnit(impulses_per_unit);

		float SelectedCurrentValue = CalculateInfCurrent();
		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		int timeduration = getInfTimeDuration();
		String metertype = getDeployedEM_ModelType();
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt_percent));
		setAllPhaseParameters(final_volt_value, final_volt_value, final_volt_value, SelectedCurrentValue, phasedegree);
		setPowerSrcOnTimerValue(timeduration);
		setHarmonic_data(getHarmonics());
		setPowerSrcOnTimerValue(300);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
/*			else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
		
		//lscsPowerSourceHarmonicsMessage.formDataFrames();   //form Data frames 
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			initialiseHarmonicsData();
			fetchHarmonicsDataFromDBandStore();
			
		}else if (ProcalFeatureEnable.HARMONICS_FEATURE_V2_ENABLED){
			initialiseHarmonicsData();
			fetchHarmonicsDataFromDBandStore();
			
			
		}
	}
	
	private void initialiseHarmonicsData(){
		
		ApplicationLauncher.logger.debug("initialiseHarmonicsData : Entry");

		for(int i=0; i<= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED ; i++){
			try {
				ApplicationLauncher.logger.debug("initialiseHarmonicsData : i : " + i);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V2.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V3.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I1.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I2.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I3.add(i, 0);
				
				//dataFrames.add(i, 0);
				
			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("initialiseHarmonicsData: Exception: " + e.getMessage());
			}	
		}
		
		//testing
		for(int j=0; j <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED ; j++){
			try {
				ApplicationLauncher.logger.debug("initialiseHarmonicsData : j : " + j);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j);	
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j));

				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.get(j): " + lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.get(j));

				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.get(j));

				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.get(j));

			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("initialiseHarmonicsData: Exception: " + e.getMessage());
			}
		}
		
		
		ApplicationLauncher.logger.debug("initialiseHarmonicsData : Exit");
	}
	
	private void fetchHarmonicsDataFromDBandStore(){
		
		try{
        	ApplicationLauncher.logger.info("fetchHarmonicsDataFromDBandStore : Entry");
			JSONArray harmonic_db_data = getHarmonic_data(); 
			JSONObject harmonics_row_data = new JSONObject();

			String harmonic_no = "";
			String phase_selected   = "";       // R = 1 ; Y = 2 ; B = 3 
			String harmonic_order   = "";       // eg: 03
			String harmonic_voltage = "";       // 100  Voltage Amplitude
			String harmonic_current = "";       // 100  Current Amplitude
			String harmonic_volt_phase = "";    // 180  Voltage Phase Shift
			String harmonic_current_phase = ""; // 180 Current Phase Shift

			// therefore we getting V1,03,100,180,I1,03,100,180 in a single row 

			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V1(false);          // initially setting false
			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I1(false);
			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V2(false);
			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I2(false);
			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V3(false);
			lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I3(false);
		
			// =============== fetching data from database and storing it into arrayList to access later ============================//
			
			ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_db_data: " + harmonic_db_data);
			for(int i=0; i<harmonic_db_data.length(); i++){       // harmonic_db_data.length == no. of rows
				
				ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn :  i  : " + i);
				try{
					harmonics_row_data = harmonic_db_data.getJSONObject(i);
	
					phase_selected         = harmonics_row_data.getString("phase_selected"); // R = 1 ; Y = 2 ; B = 3 
					harmonic_order         = harmonics_row_data.getString("harmonic_order");  // 3 
					harmonic_voltage       = harmonics_row_data.getString("harmonic_volt");   //  100
					harmonic_volt_phase    = harmonics_row_data.getString("harmonic_volt_phase");     // 180
					harmonic_current       = harmonics_row_data.getString("harmonic_current");        // 100
					harmonic_current_phase = harmonics_row_data.getString("harmonic_current_phase");  // 180
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : phase_selected         : " + phase_selected);
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_order         : " + harmonic_order);
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_voltage       : " + harmonic_voltage);
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_volt_phase    : " + harmonic_volt_phase);
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_current       : " + harmonic_current);
					ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_current_phase : " + harmonic_current_phase);
					
					if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.R_PHASE)){                                                //1
						
						ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing R Phase " );
	
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), true);	       //2
						lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V1(true);
						} else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), false); 
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V1(false);
					    }
						
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));      
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage)); //3
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	 //4
	
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I1(true);
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), true);          
							} else {
								lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), false);
								lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I1(false);
						    }	
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));      //5
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));   //6	
					}
					else if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.Y_PHASE)){
						ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing Y Phase " );
						
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V2(true);
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), true);
						} else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), false);
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V2(false);
					    }
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage));
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	
	
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I2(true);
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), true);
							} else {
								lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), false);
								lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I2(false);
						    }		
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));	
					}
					else if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.B_PHASE)){
						ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing B Phase " );
						
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V3(true);
						    lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), true);
					    } else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), false);
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_V3(false);
					    }
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage));
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	
	
						if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
							lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I3(true);
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), true);
							} else {
								lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), false);
								lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledInSignal_I3(false);
						    }			
						lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
						lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));
						lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));	
					}
				}
		         catch(Exception e){
		 			e.printStackTrace();
		 			ApplicationLauncher.logger.error("Display_Har_PwrSrc_TurnOn: Exception2: " + e.getMessage());
		 		}
			
			} // for loop end
			
			ApplicationLauncher.logger.info("fetchHarmonicsDataFromDBandStore :try end");
			
			lscsPowerSourceHarmonicsMessage.formDataFrames();   //form Data frames 
			
			// default values 
			Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
	
			Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
			
			Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
			
			//Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setFirstHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));
			//Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
	
			//Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setSecondHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));
			//Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
			
			//Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex("00"));
			Data_PowerSourceBofa.setThirdHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));
			//Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));
			
			int rPhaseVoltIteration = 0;
			int rPhaseCurrentIteration = 0;
			
			for( String eachData : lscsPowerSourceHarmonicsMessage.dataFrames){
				if(!eachData.isEmpty()){

					ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: harmonics Each Frame: " + eachData);
					String harmonicOrder = "";
					String percentage = "";
					String phaseShiftInDegree = "";
					if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
						try{
							String[] splittedData =  eachData.split(",");
							if(splittedData.length>0){
								if(splittedData[0].equals("V1")){
									harmonicOrder      = splittedData[1];
									percentage         = splittedData[2];
									phaseShiftInDegree = splittedData[3];
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : harmonicOrder : " + harmonicOrder);
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : percentage : " + percentage);
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : phaseShiftInDegree : " + phaseShiftInDegree);
									if(rPhaseVoltIteration==0){
										
										Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  	
										
									}else if(rPhaseVoltIteration==1){
										Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  
										 
									}else if(rPhaseVoltIteration==2){
										Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  
										 
									}
									rPhaseVoltIteration ++; 
									//ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : harmonicOrder : " + harmonicOrder);
									//ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : percentage : " + percentage);
									//ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase V1 harmonics : phaseShiftInDegree : " + phaseShiftInDegree);
								}							
								else if(splittedData[0].equals("I1")){
									harmonicOrder      = splittedData[1];
									percentage         = splittedData[2];
									phaseShiftInDegree = splittedData[3];
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase I1 harmonics : harmonicOrder : " + harmonicOrder);
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase I1 harmonics : percentage : " + percentage);
									ApplicationLauncher.logger.debug("fetchHarmonicsDataFromDBandStore: R Phase I1 harmonics : phaseShiftInDegree : " + phaseShiftInDegree);

									if(rPhaseCurrentIteration==0){
										//Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setFirstHarmonicPercentageCurrent(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										//Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  	

									}else if(rPhaseCurrentIteration==1){
										//Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setSecondHarmonicPercentageCurrent(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										//Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  

									}else if(rPhaseCurrentIteration==2){
										//Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex( harmonicsOrderFormatter.format(Integer.parseInt(harmonicOrder)) )) ;   
										Data_PowerSourceBofa.setThirdHarmonicPercentageCurrent(BofaManager.asciiToHex(harmonicsPercentageFormatter.format(Integer.parseInt(percentage)))) ;  
										//Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(harmonicsPhaseShiftFormatter.format(Float.parseFloat(phaseShiftInDegree))))) ;  
									}		
									rPhaseCurrentIteration ++; 
								}
					
		
							}
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("fetchHarmonicsDataFromDBandStore: Exception: "+ e.getMessage());
						}
					}
				}
			}
			
			//lscsSendHarmonicsDataToSlave();
			
         }
         catch(Exception e){
 			e.printStackTrace();
 			ApplicationLauncher.logger.error("fetchHarmonicsDataFromDBandStore: Exception: " + e.getMessage());
 		}
		
	}

	public String manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String rated_volt){
		String threePhaseDeltaVolt = rated_volt;
		String metertype = getDeployedEM_ModelType();
		if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)){
			ApplicationLauncher.logger.info("manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N: 3 Phase Delta : rated_volt: " + rated_volt );
			threePhaseDeltaVolt = String.valueOf(Float.parseFloat(rated_volt) / ConstantApp.SQRT_OF_THREE);
			ApplicationLauncher.logger.info("manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N: 3 Phase Delta : threePhaseDeltaVolt: " + threePhaseDeltaVolt );
		}
		return threePhaseDeltaVolt;
	}

	public void setAccuracyParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setAccuracyParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		String rated_current = modelparams.getString("basic_current_ib");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String test_case_name = param.getString("test_case_name");
		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);

		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		ApplicationLauncher.logger.info("setAccuracyParameters: no_of_pulses: " + no_of_pulses);
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		String volt_percent = param.getString("voltage");
		ApplicationLauncher.logger.info("setAccuracyParameters: time_duration: " + time_duration);
		ApplicationLauncher.logger.debug("setAccuracyParameters: testruntype: " + testruntype);
		int time_duration_insec = Integer.parseInt(time_duration);//editedbyMO
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );

		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setDutImpulsesPerUnit(impulses_per_unit);


		float SelectedCurrentValue = CalculateInfCurrent();
		//String metertype = getDeployedEM_ModelType();
		//if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)){
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		//}
		float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt_percent));
		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		ApplicationLauncher.logger.info("final_volt_value: " + final_volt_value);
		int timeduration = getInfTimeDuration();
		String e_min = get_Error_min();
		String e_max = get_Error_max();
		String No_of_pulses = getNoOfPulses();
		setAllPhaseParameters(final_volt_value, final_volt_value, final_volt_value, 
				SelectedCurrentValue, phasedegree);
		setPowerSrcOnTimerValue(timeduration);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else {
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				//SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
				
			}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){

						if(!Data_RefStdKre.isLastDataSetupForRefStdKreSame( maxCurrent)){
							clearLastSetRSS_Pulse_Rate();
							setRefStd_BNC_Constant();
						}

				}
			}*/
			
		}
		//Sleep(10000);
			
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
		
		/*if () {   // Bofa
			
			getR_PhaseOutputVoltage();
			getR_PhaseOutputCurrent();
			get_PwrSrcR_PhaseDegreePhase
		}*/
		
	}
//===============================================
	// pradeep 
	
	// more info required
	
	public static void setParameterSettingCmdVariablesBofaSource(){  


		String outputFrequency                     = get_PwrSrc_FreqStr();// "50.00";    // Example: Output frequency (xx.xxHz)
		String uaIaAngle                           = get_PwrSrcR_PhaseDegreePhaseStr();//"300.0"; xvcb   // UaIa angle (xxx.x)
		String firstHarmonic                       = "00";      // 1st harmonic (xx)
		String secondHarmonic                      = "00";      // 2nd harmonic (xx)
		String thirdHarmonic                       = "00";      // 3rd harmonic (xx)
	 	String firstHarmonicPercentageVoltage      = "00";      // 1st harmonic percentage (voltage) (xx)
		String secondHarmonicPercentageVoltage     = "00";      // 2nd harmonic percentage (voltage) (xx)
		String thirdHarmonicPercentageVoltage      = "00";      // 3rdharmonic percentage (voltage) (xx)
		String firstHarmonicPercentageCurrent      = "00";      // 1st harmonic percentage (current) (xx)
		String secondHarmonicPercentageCurrent     = "00";      // 2nd harmonic percentage (current) (xx)
		String thirdHarmonicPercentageCurrent      = "00";      // 3rd harmonic percentage (current) (xx)
		String firstHarmonicPercentagePhaseAngle   = "000.0";    // 1st harmonic percentage (phase angle) (xxx.x)
		String secondHarmonicPercentagePhaseAngle  = "000.0";    // 2nd harmonic percentage (phase angle) (xxx.x)
		String thirdHarmonicPercentagePhaseAngle   = "000.0";    // 3rd harmonic percentage (phase angle) (xxx.x)
		String phaseAVoltagePercentage             = "100.00";   // Phase A voltage percentage (xxx.xx%)
		String phaseACurrentPercentage             = "100.00";   // Phase A current percentage (xxx.xx%)
		String meterConstant                       = "03200.00" ;//"1234567"; // Meter constant (xxxxx.xx)
		String numOfPulse                          = "10";      // Number of pulse (xx)
		
		
/*		String outputFrequency                     = "50.00";    // Example: Output frequency (xx.xxHz)
		String uaIaAngle                           = "300.0";    // UaIa angle (xxx.x)
		String firstHarmonic                       = "00";      // 1st harmonic (xx)
		String secondHarmonic                      = "00";      // 2nd harmonic (xx)
		String thirdHarmonic                       = "00";      // 3rd harmonic (xx)
		String firstHarmonicPercentageVoltage      = "00";      // 1st harmonic percentage (voltage) (xx)
		String secondHarmonicPercentageVoltage     = "00";      // 2nd harmonic percentage (voltage) (xx)
		String thirdHarmonicPercentageVoltage      = "00";      // 3rdharmonic percentage (voltage) (xx)
		String firstHarmonicPercentageCurrent      = "00";      // 1st harmonic percentage (current) (xx)
		String secondHarmonicPercentageCurrent     = "00";      // 2nd harmonic percentage (current) (xx)
		String thirdHarmonicPercentageCurrent      = "00";      // 3rd harmonic percentage (current) (xx)
		String firstHarmonicPercentagePhaseAngle   = "000.0";    // 1st harmonic percentage (phase angle) (xxx.x)
		String secondHarmonicPercentagePhaseAngle  = "000.0";    // 2nd harmonic percentage (phase angle) (xxx.x)
		String thirdHarmonicPercentagePhaseAngle   = "000.0";    // 3rd harmonic percentage (phase angle) (xxx.x)
		String phaseAVoltagePercentage             = "100.00";   // Phase A voltage percentage (xxx.xx%)
		String phaseACurrentPercentage             = "100.00";   // Phase A current percentage (xxx.xx%)
		String meterConstant                       = "1234567"; // Meter constant (xxxxx.xx)
		String numOfPulse                          = "89";      // Number of pulse (xx)
*/

/*		Data_PowerSourceBofa.setMeterType(ConstantBofaPowerSource.METER_1PHASE2WIRE_WATT);   
		Data_PowerSourceBofa.setBasicVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(ConstantBofaPowerSource.BASIC_VOLTAGE))) ;  
		Data_PowerSourceBofa.setBasicCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(ConstantBofaPowerSource.BASIC_CURRENT))) ;  
		Data_PowerSourceBofa.setOutputFrequency(BofaManager.asciiToHex(BofaManager.removeDecimal(outputFrequency))) ;   
		Data_PowerSourceBofa.setOutputPhaseSequence(ConstantBofaPowerSource.POSITIVE_PHASE_SEQUENCE) ;   
		Data_PowerSourceBofa.setCurrentDirection(ConstantBofaPowerSource.FORWARD_CURRENT_DIRECTION)  ;   
		Data_PowerSourceBofa.setUaUbAngle(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FOUR_DIGITS))  ;   
		Data_PowerSourceBofa.setUaUcAngle(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setUaIaAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(uaIaAngle))) ;   
		Data_PowerSourceBofa.setUbIbAngle(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setUcIcAngle(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setWaveForm(BofaManager.removeDecimal(ConstantBofaPowerSource.SINE_WAVE))  ;   
		Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex(firstHarmonic)) ;   
		Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex(secondHarmonic));   
		Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex(thirdHarmonic)) ;   
		Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentageVoltage)))  ;   
		Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentageVoltage))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentageVoltage)))  ;  
		Data_PowerSourceBofa.setFirstHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentageCurrent)))  ;   
		Data_PowerSourceBofa.setSecondHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentageCurrent))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentageCurrent)))  ;   
		Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentagePhaseAngle)))  ;   
		Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentagePhaseAngle))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentagePhaseAngle)))  ;   
		Data_PowerSourceBofa.setPhaseAVoltagePercentage(BofaManager.asciiToHex(BofaManager.removeDecimal(phaseAVoltagePercentage)));  	  
		Data_PowerSourceBofa.setPhaseBVoltagePercentage(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FIVE_DIGITS)) ;     
		Data_PowerSourceBofa.setPhaseCVoltagePercentage(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FIVE_DIGITS)) ;   
		Data_PowerSourceBofa.setPhaseACurrentPercentage(BofaManager.asciiToHex(BofaManager.removeDecimal(phaseACurrentPercentage)))  ;  
		Data_PowerSourceBofa.setPhaseBCurrentPercentage(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FIVE_DIGITS)) ;  	
		Data_PowerSourceBofa.setPhaseCCurrentPercentage(BofaManager.asciiToHex(ConstantBofaPowerSource.DUMMY_FIVE_DIGITS)) ;      		
		Data_PowerSourceBofa.setMeterConstant(BofaManager.asciiToHex(BofaManager.removeDecimal(meterConstant))) ;  
		Data_PowerSourceBofa.setNumOfPulse(BofaManager.asciiToHex(BofaManager.removeDecimal(numOfPulse))) ;  
		Data_PowerSourceBofa.setConjunctionSplitPhase(ConstantBofaPowerSource.PHASE_A) ;*/

		
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: getR_PhaseOutputVoltage(): " + getR_PhaseOutputVoltage() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: getR_PhaseOutputCurrent(): " + getR_PhaseOutputCurrent() );	
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: get_PwrSrcR_PhaseDegreePhaseStr(): " + get_PwrSrcR_PhaseDegreePhaseStr() );	
		
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: get_PwrSrc_FreqStr(): " + get_PwrSrc_FreqStr() );	
		
		
		Data_PowerSourceBofa.setMeterType(ConstantPowerSourceBofa.METER_1PHASE2WIRE_WATT);   
		//Data_PowerSourceBofa.setBasicVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(ConstantBofaPowerSource.BASIC_VOLTAGE))) ;  
		//Data_PowerSourceBofa.setBasicCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(ConstantBofaPowerSource.BASIC_CURRENT))) ;  	
		Data_PowerSourceBofa.setBasicVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(getR_PhaseOutputVoltage()))) ;  
		Data_PowerSourceBofa.setBasicCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(getR_PhaseOutputCurrent()))) ; 
		
		/*ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonic(): " + Data_PowerSourceBofa.getFirstHarmonic() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentageVoltage(): " + Data_PowerSourceBofa.getFirstHarmonicPercentageVoltage() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle(): " + Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle() );
		
		*/
		Data_PowerSourceBofa.setOutputFrequency(BofaManager.asciiToHex(BofaManager.removeDecimal(outputFrequency))) ;   
		Data_PowerSourceBofa.setOutputPhaseSequence(ConstantPowerSourceBofa.POSITIVE_PHASE_SEQUENCE) ;   
		Data_PowerSourceBofa.setCurrentDirection(ConstantPowerSourceBofa.FORWARD_CURRENT_DIRECTION)  ;   
		Data_PowerSourceBofa.setUaUbAngle(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FOUR_DIGITS))  ;   
		Data_PowerSourceBofa.setUaUcAngle(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setUaIaAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(uaIaAngle))) ;   
		Data_PowerSourceBofa.setUbIbAngle(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setUcIcAngle(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FOUR_DIGITS)) ;   
		Data_PowerSourceBofa.setWaveForm(BofaManager.removeDecimal(ConstantPowerSourceBofa.SINE_WAVE))  ;    
		
		//Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex(firstHarmonic)) ;   
		//Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex(secondHarmonic));   
		//Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex(thirdHarmonic)) ;
		
		/*Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex(Data_PowerSourceBofa.getFirstHarmonic()));
		Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex(Data_PowerSourceBofa.getSecondHarmonic()));   
		Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex(Data_PowerSourceBofa.getThirdHarmonic())) ;*/
		   
		//Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentageVoltage)))  ; 
		//Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentageVoltage))) ;   
		//Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentageVoltage)))  ;  

		/*Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getFirstHarmonicPercentageVoltage())))  ; 
		Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getSecondHarmonicPercentageVoltage()))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getThirdHarmonicPercentageVoltage())))  ;  */

		//Data_PowerSourceBofa.setFirstHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentageCurrent)))  ;   		
		//Data_PowerSourceBofa.setSecondHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentageCurrent))) ;   
		//Data_PowerSourceBofa.setThirdHarmonicPercentageCurrent(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentageCurrent)))  ;  
		
		/*Data_PowerSourceBofa.setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getFirstHarmonicPercentageCurrent())))  ; 
		Data_PowerSourceBofa.setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getSecondHarmonicPercentageCurrent()))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getThirdHarmonicPercentageCurrent())))  ;  */
		
		//Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(firstHarmonicPercentagePhaseAngle)))  ;  
		//Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(secondHarmonicPercentagePhaseAngle))) ;   
		//Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(thirdHarmonicPercentagePhaseAngle)))  ;
		
/*		Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle())))  ; 
		Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getSecondHarmonicPercentagePhaseAngle()))) ;   
		Data_PowerSourceBofa.setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex(BofaManager.removeDecimal(Data_PowerSourceBofa.getThirdHarmonicPercentagePhaseAngle())))  ;*/
		
		Data_PowerSourceBofa.setPhaseAVoltagePercentage(BofaManager.asciiToHex(BofaManager.removeDecimal(phaseAVoltagePercentage)));  	  
		Data_PowerSourceBofa.setPhaseBVoltagePercentage(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FIVE_DIGITS)) ;     
		Data_PowerSourceBofa.setPhaseCVoltagePercentage(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FIVE_DIGITS)) ;   
		Data_PowerSourceBofa.setPhaseACurrentPercentage(BofaManager.asciiToHex(BofaManager.removeDecimal(phaseACurrentPercentage)))  ;  
		Data_PowerSourceBofa.setPhaseBCurrentPercentage(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FIVE_DIGITS)) ;  	
		Data_PowerSourceBofa.setPhaseCCurrentPercentage(BofaManager.asciiToHex(ConstantPowerSourceBofa.DUMMY_FIVE_DIGITS)) ;      		
		Data_PowerSourceBofa.setMeterConstant(BofaManager.asciiToHex(BofaManager.removeDecimal(meterConstant))) ;  
		Data_PowerSourceBofa.setNumOfPulse(BofaManager.asciiToHex(BofaManager.removeDecimal(numOfPulse))) ;  
		Data_PowerSourceBofa.setConjunctionSplitPhase(ConstantPowerSourceBofa.PHASE_A) ;
		
		//=====
		String presentTestPointName = ProjectExecutionController.getCurrentTestPointName();
		
		
		//if(presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
		if(isPresentTestPointContainsHarmonics()){		
			ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: setting waveform to Harmonics type" );
			
			Data_PowerSourceBofa.setWaveForm(BofaManager.removeDecimal(ConstantPowerSourceBofa.HARMONIC))  ;  
		}
		
		
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonic()2                      : " + Data_PowerSourceBofa.getFirstHarmonic() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonic()2                     : " + Data_PowerSourceBofa.getSecondHarmonic() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonic()2                      : " + Data_PowerSourceBofa.getThirdHarmonic() );

		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentageVoltage()2     : " + Data_PowerSourceBofa.getFirstHarmonicPercentageVoltage() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentageCurrent()2     : " + Data_PowerSourceBofa.getFirstHarmonicPercentageCurrent() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle()2  : " + Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle() );
		
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonicPercentageVoltage()2    : " + Data_PowerSourceBofa.getSecondHarmonicPercentageVoltage() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonicPercentageCurrent()2    : " + Data_PowerSourceBofa.getSecondHarmonicPercentageCurrent() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonicPercentagePhaseAngle()2 : " + Data_PowerSourceBofa.getSecondHarmonicPercentagePhaseAngle() );
		
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonicPercentageVoltage()2     : " + Data_PowerSourceBofa.getThirdHarmonicPercentageVoltage() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonicPercentageCurrent()2     : " + Data_PowerSourceBofa.getThirdHarmonicPercentageCurrent() );
		ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonicPercentagePhaseAngle()2  : " + Data_PowerSourceBofa.getThirdHarmonicPercentagePhaseAngle() );
		
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonic()2                     : " + Data_PowerSourceBofa.getFirstHarmonic() );
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle()2 : " + Data_PowerSourceBofa.getFirstHarmonicPercentagePhaseAngle() );
		
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonic()2                     : " + Data_PowerSourceBofa.getSecondHarmonic() );
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getSecondHarmonicPercentagePhaseAngle()2 : " + Data_PowerSourceBofa.getSecondHarmonicPercentagePhaseAngle() );
		
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonic()2                     : " + Data_PowerSourceBofa.getThirdHarmonic() );
		//ApplicationLauncher.logger.info("setParameterSettingCmdVariablesBofaSource: Data_PowerSourceBofa.getThirdHarmonicPercentagePhaseAngle()2 : " + Data_PowerSourceBofa.getThirdHarmonicPercentagePhaseAngle() );
		
	}
//===============================================	
	
	public void refStdUpdateConfigurationSettings(String maxVoltage,String maxCurrent){
		ApplicationLauncher.logger.info("refStdUpdateConfigurationSettings: Entry" );
		
		if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
			if( (maxVoltage!=null) && (maxCurrent!=null) ){
				if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
					clearLastSetRSS_Pulse_Rate();
					setRefStd_BNC_Constant();
				}
			}
			
		}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			if( (maxVoltage!=null) && (maxCurrent!=null) ){

					if(!Data_RefStdKre.isLastDataSetupForRefStdKreSame( maxCurrent)){
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
			}
		}
	}
	

	

	public static String manipulateNoOfPulsesForTimeBased(int meterConstantInImpulsesPerKiloWattHour,int timeDurationInSec,float totalTargetPowerInKiloWatt){
		String noOfPulsesTimeBasedStr = "1";
		float noOfPulsesFloat = 0.0f;
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : Entry");
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : meterConstantInImpulsesPerKiloWattHour : " + meterConstantInImpulsesPerKiloWattHour);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : timeDurationInSec : " + timeDurationInSec);
		ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : totalTargetPowerInKiloWatt : " + totalTargetPowerInKiloWatt);
		
		try{
			float meterConstantInImpulsesPerKiloWattSecond  = ((float)meterConstantInImpulsesPerKiloWattHour)/3600.0f;
			
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : meterConstantInImpulsesPerKiloWattSecond : " + meterConstantInImpulsesPerKiloWattSecond);
			
			noOfPulsesFloat = totalTargetPowerInKiloWatt * timeDurationInSec * meterConstantInImpulsesPerKiloWattSecond;
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : noOfPulsesFloat : " + noOfPulsesFloat);
			
			BigDecimal bigValue = new BigDecimal(noOfPulsesFloat);
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue : " + bigValue);
			bigValue = bigValue.setScale(2, RoundingMode.FLOOR);
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue FLOOR 1: " + bigValue);
			bigValue = bigValue.setScale(1, RoundingMode.UP);
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue scaled up2: " + bigValue);
			bigValue = bigValue.setScale(0, RoundingMode.UP);
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : bigValue scaled up3: " + bigValue);
			noOfPulsesTimeBasedStr = String.valueOf(bigValue.intValue());
			ApplicationLauncher.logger.debug("manipulateNoOfPulsesForTimeBased : noOfPulsesTimeBasedStr : " + noOfPulsesTimeBasedStr);
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateNoOfPulsesForTimeBased exception: " + e.getMessage());
		}
		
		return noOfPulsesTimeBasedStr;
	}

	
	public static void manipulateTargetRmsValue(){
		
		ApplicationLauncher.logger.info("manipulateTargetRmsValue :Entry");
		try{
			String selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			String voltage = getR_PhaseOutputVoltage();
			String current = getR_PhaseOutputCurrent();
			String rPhaseCurrent = current;
			String rPhaseCurrentRelayId = "002";
			String currentRelayId = "002";
			currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			//currentRelayId = rPhaseCurrentRelayId;
			voltage = getTargetVoltageRms(selectedPhase, voltage);		
			current = getTargetCurrentRms(selectedPhase, current);
			setR_PhaseOutputVoltageRms(voltage);
			setR_PhaseOutputCurrentRms(current);
			ApplicationLauncher.logger.debug("manipulateTargetRmsValue : R current: " + current);
			String metertype = getDeployedEM_ModelType();
	
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				//String yPhaseCurrentRelayId = "002";
				//String bPhaseCurrentRelayId = "002";
				selectedPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
				voltage = getY_PhaseOutputVoltage();
				current = getY_PhaseOutputCurrent();
				String yPhaseCurrent = current;
				//yPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				
				voltage = getTargetVoltageRms(selectedPhase, voltage);		
				current = getTargetCurrentRms(selectedPhase, current);
				ApplicationLauncher.logger.debug("manipulateTargetRmsValue : Y current: " + current);
				setY_PhaseOutputVoltageRms(voltage);
				setY_PhaseOutputCurrentRms(current);
				
				
				selectedPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
				voltage = getB_PhaseOutputVoltage();
				current = getB_PhaseOutputCurrent();
				String bPhaseCurrent = current;
				//bPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				voltage = getTargetVoltageRms(selectedPhase, voltage);		
				current = getTargetCurrentRms(selectedPhase, current);
				ApplicationLauncher.logger.debug("manipulateTargetRmsValue : B current: " + current);
				setB_PhaseOutputVoltageRms(voltage);
				setB_PhaseOutputCurrentRms(current);
				String maxCurrent = GuiUtils.getMax(rPhaseCurrent,yPhaseCurrent,bPhaseCurrent);
				selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
				currentRelayId = getTargetCurrentRelayId(selectedPhase, maxCurrent);// added in version s4.0.4.6.3.2 - for voltage unbalance issue
				ApplicationLauncher.logger.info("manipulateTargetRmsValue : currentRelayId: " + currentRelayId);
			}
			setSourceCurrentR_PhaseTapSelection(currentRelayId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateTargetRmsValue : Exception :"+ e.getMessage());
		}
		


	}
	
	
	public static void manipulateRelayIdValue(){
		
		ApplicationLauncher.logger.info("manipulateRelayIdValue :Entry");
		try{
			String selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			String voltage = getR_PhaseOutputVoltage();
			String current = getR_PhaseOutputCurrent();
			String rPhaseCurrent = current;
			String rPhaseCurrentRelayId = "002";
			String currentRelayId = "002";
			currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			//currentRelayId = rPhaseCurrentRelayId;
/*			voltage = getTargetVoltageRms(selectedPhase, voltage);		
			current = getTargetCurrentRms(selectedPhase, current);
			setR_PhaseOutputVoltageRms(voltage);
			setR_PhaseOutputCurrentRms(current);
			ApplicationLauncher.logger.debug("manipulateTargetRmsValue : R current: " + current);
			String metertype = getDeployedEM_ModelType();*/
	
/*			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				//String yPhaseCurrentRelayId = "002";
				//String bPhaseCurrentRelayId = "002";
				selectedPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
				voltage = getY_PhaseOutputVoltage();
				current = getY_PhaseOutputCurrent();
				String yPhaseCurrent = current;
				//yPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				
				voltage = getTargetVoltageRms(selectedPhase, voltage);		
				current = getTargetCurrentRms(selectedPhase, current);
				ApplicationLauncher.logger.debug("manipulateTargetRmsValue : Y current: " + current);
				setY_PhaseOutputVoltageRms(voltage);
				setY_PhaseOutputCurrentRms(current);
				
				
				selectedPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
				voltage = getB_PhaseOutputVoltage();
				current = getB_PhaseOutputCurrent();
				String bPhaseCurrent = current;
				//bPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				voltage = getTargetVoltageRms(selectedPhase, voltage);		
				current = getTargetCurrentRms(selectedPhase, current);
				ApplicationLauncher.logger.debug("manipulateTargetRmsValue : B current: " + current);
				setB_PhaseOutputVoltageRms(voltage);
				setB_PhaseOutputCurrentRms(current);
				String maxCurrent = GuiUtils.getMax(rPhaseCurrent,yPhaseCurrent,bPhaseCurrent);
				selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
				currentRelayId = getTargetCurrentRelayId(selectedPhase, maxCurrent);// added in version s4.0.4.6.3.2 - for voltage unbalance issue
				ApplicationLauncher.logger.info("manipulateTargetRmsValue : currentRelayId: " + currentRelayId);
			}*/
			setSourceCurrentR_PhaseTapSelection(currentRelayId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateRelayIdValue : Exception :"+ e.getMessage());
		}
		


	}
	
/*	public static String manipulatePowerSourceFeedbackVoltageRmsToAbsoluteValue(String selectedPhase, String powerSourceFeedBackVoltageRms){
		
		ApplicationLauncher.logger.info("manipulatePowerSourceFeedbackVoltageRmsToAbsoluteValue :Entry");
		
		try{
			//String selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			String absoluteVoltage = "";//getR_PhaseOutputVoltage();
			String userInputVoltage = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				userInputVoltage = getR_PhaseOutputVoltage();
			}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				userInputVoltage = getY_PhaseOutputVoltage();
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				userInputVoltage = getB_PhaseOutputVoltage();
			}
			//String current = getR_PhaseOutputCurrent();
			//String rPhaseCurrent = current;
			//String rPhaseCurrentRelayId = "002";
			//String currentRelayId = "002";
			//currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			//currentRelayId = rPhaseCurrentRelayId;
			//voltage = getTargetVoltageRms(selectedPhase, voltage);	
			absoluteVoltage = getAbsoluteVoltage(selectedPhase, userInputVoltage,powerSourceFeedBackVoltageRms);	
			return absoluteVoltage;
			//current = getTargetCurrentRms(selectedPhase, current);
			//setR_PhaseOutputVoltageRms(voltage);
			//setR_PhaseOutputCurrentRms(current);
			
			String metertype = getDeployedEM_ModelType();
	
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				//String yPhaseCurrentRelayId = "002";
				//String bPhaseCurrentRelayId = "002";
				selectedPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
				voltage = getY_PhaseOutputVoltage();
				current = getY_PhaseOutputCurrent();
				String yPhaseCurrent = current;
				//yPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				
				//voltage = getTargetVoltageRms(selectedPhase, voltage);		
				//current = getTargetCurrentRms(selectedPhase, current);
				//setY_PhaseOutputVoltageRms(voltage);
				//setY_PhaseOutputCurrentRms(current);
				
				
				selectedPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
				voltage = getB_PhaseOutputVoltage();
				current = getB_PhaseOutputCurrent();
				String bPhaseCurrent = current;
				//bPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				//voltage = getTargetVoltageRms(selectedPhase, voltage);		
				//current = getTargetCurrentRms(selectedPhase, current);
				//setB_PhaseOutputVoltageRms(voltage);
				//setB_PhaseOutputCurrentRms(current);
				//String maxCurrent = GUIUtils.getMax(rPhaseCurrent,yPhaseCurrent,bPhaseCurrent);
				//selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
				//currentRelayId = getTargetCurrentRelayId(selectedPhase, maxCurrent);// added in version s4.0.4.6.3.2 - for voltage unbalance issue
				//ApplicationLauncher.logger.info("manipulatePowerSourceFeedbackVoltageRmsToAbsoluteValue : currentRelayId: " + currentRelayId);
			}
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulatePowerSourceFeedbackVoltageRmsToAbsoluteValue : Exception :"+ e.getMessage());
		}
		
		return "";

	}*/
	
	public static String manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue(String selectedPhase, String powerSourceFeedBackCurrentRms){
		
		ApplicationLauncher.logger.info("manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue :Entry");
		
		try{
			//String selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			String absoluteCurrent = "";//getR_PhaseOutputVoltage();
			String userInputCurrent = "";
			if(selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				userInputCurrent = getR_PhaseOutputCurrent();
			}else if(selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				userInputCurrent = getY_PhaseOutputCurrent();
			}else if(selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				userInputCurrent = getB_PhaseOutputCurrent();
			}
			//String current = getR_PhaseOutputCurrent();
			//String rPhaseCurrent = current;
			//String rPhaseCurrentRelayId = "002";
			//String currentRelayId = "002";
			//currentRelayId = getTargetCurrentRelayId(selectedPhase, current);
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			//currentRelayId = rPhaseCurrentRelayId;
			//voltage = getTargetVoltageRms(selectedPhase, voltage);	
			//absoluteCurrent = getAbsoluteCurrent(selectedPhase, userInputCurrent,powerSourceFeedBackCurrentRms);	
			return absoluteCurrent;
			//current = getTargetCurrentRms(selectedPhase, current);
			//setR_PhaseOutputVoltageRms(voltage);
			//setR_PhaseOutputCurrentRms(current);
/*			
			String metertype = getDeployedEM_ModelType();
	
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				//String yPhaseCurrentRelayId = "002";
				//String bPhaseCurrentRelayId = "002";
				selectedPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
				voltage = getY_PhaseOutputVoltage();
				current = getY_PhaseOutputCurrent();
				String yPhaseCurrent = current;
				//yPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				
				//voltage = getTargetVoltageRms(selectedPhase, voltage);		
				//current = getTargetCurrentRms(selectedPhase, current);
				//setY_PhaseOutputVoltageRms(voltage);
				//setY_PhaseOutputCurrentRms(current);
				
				
				selectedPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
				voltage = getB_PhaseOutputVoltage();
				current = getB_PhaseOutputCurrent();
				String bPhaseCurrent = current;
				//bPhaseCurrentRelayId = getTargetCurrentRelayId(selectedPhase, current);
				//voltage = getTargetVoltageRms(selectedPhase, voltage);		
				//current = getTargetCurrentRms(selectedPhase, current);
				//setB_PhaseOutputVoltageRms(voltage);
				//setB_PhaseOutputCurrentRms(current);
				//String maxCurrent = GUIUtils.getMax(rPhaseCurrent,yPhaseCurrent,bPhaseCurrent);
				//selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
				//currentRelayId = getTargetCurrentRelayId(selectedPhase, maxCurrent);// added in version s4.0.4.6.3.2 - for voltage unbalance issue
				//ApplicationLauncher.logger.info("manipulatePowerSourceFeedbackVoltageRmsToAbsoluteValue : currentRelayId: " + currentRelayId);
			}*/
			//setSourceCurrentR_PhaseTapSelection(currentRelayId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue : Exception :"+ e.getMessage());
		}
		
		return "";

	}
	
	 long calculateRSS_ConstantV42(String MUT_ConstInImpulsesPerKiloWattHour){
		 	ApplicationLauncher.logger.info("calculateRSS_ConstantV42 :Entry");
		 	//float Volt1,float Volt2,float Volt3,float Current1,float Current2,float Current3,float PF1,float PF2,float PF3
		 	float Volt1 = Float.parseFloat(getR_PhaseOutputVoltage());
		 	float Volt2 =  Float.parseFloat(getY_PhaseOutputVoltage());
		 	float Volt3 =  Float.parseFloat(getB_PhaseOutputVoltage());
		 	float Current1 =  Float.parseFloat(getR_PhaseOutputCurrent());
		 	float Current2 =  Float.parseFloat(getY_PhaseOutputCurrent());
		 	float Current3 =  Float.parseFloat(getB_PhaseOutputCurrent());
/*		 	float PF_InDegree1 =  Float.parseFloat(DisplayDataObj.getR_PhaseOutputPhase());
		 	float PF_InDegree2 =  Float.parseFloat(DisplayDataObj.getY_PhaseOutputPhase());
		 	float PF_InDegree3 =  Float.parseFloat(DisplayDataObj.getB_PhaseOutputPhase());*/
		 	
		 	
		 	float PF_InDegree1 =  DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
		 	float PF_InDegree2 =  DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase();
		 	float PF_InDegree3 =  DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase();
		 	
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Volt1:" + Volt1);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Volt2:" + Volt2);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Volt3:" + Volt3);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Current1:" + Current1);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Current2:" + Current2);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Current3:" + Current3);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_InDegree1:" + PF_InDegree1);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_InDegree2:" + PF_InDegree2);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_InDegree3:" + PF_InDegree3);
		 	float PF_Value1 = 0.0f;
		 	float PF_Value2 = 0.0f;
		 	float PF_Value3 = 0.0f;
		 	if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
		 		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Active");
		 		PF_Value1 = Float.parseFloat(String.format("%.02f", (float)Math.cos(Math.toRadians(PF_InDegree1))));
		 	 	PF_Value2 = Float.parseFloat(String.format("%.02f", (float)Math.cos(Math.toRadians(PF_InDegree2))));
		 	 	PF_Value3 =  Float.parseFloat(String.format("%.02f",(float)Math.cos(Math.toRadians(PF_InDegree3))));
		 	}else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
		 		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Reactive");
		 		//PF_Value1 = Float.parseFloat(String.format("%.02f", (float)Math.sin(Math.toRadians(PF_InDegree1))));
		 	 	//PF_Value2 = Float.parseFloat(String.format("%.02f", (float)Math.sin(Math.toRadians(PF_InDegree2))));
		 	 	//PF_Value3 =  Float.parseFloat(String.format("%.02f",(float)Math.sin(Math.toRadians(PF_InDegree3))));
		 	 	PF_Value1 = Float.parseFloat(String.format("%.02f", (float)Math.sin(Math.toRadians(PF_InDegree1))))  ;
		 	 	PF_Value2 = Float.parseFloat(String.format("%.02f", (float)Math.sin(Math.toRadians(PF_InDegree2))));
		 	 	PF_Value3 =  Float.parseFloat(String.format("%.02f",(float)Math.sin(Math.toRadians(PF_InDegree3))));
		 	 	//PF_Value1 = 1.0f;
		 	 	//PF_Value2 = 1.0f;
		 	 	//PF_Value3 = 1.0f;
/*		 	 	PF_Value1 =  (float) Math.sqrt( (1- (PF_Value1*PF_Value1)) );
		 	 	PF_Value2 =  (float) Math.sqrt( (1- (PF_Value2*PF_Value2)) );
		 	 	PF_Value3 =  (float) Math.sqrt( (1- (PF_Value3*PF_Value3)) );
		 	 	
		 	 	if(PF_Value1 == 0.0f){
		 	 		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF1-0.001");
		 	 		PF_Value1 = 0.001f;
		 	 	}
		 	 	if(PF_Value2 == 0.0f){
		 	 		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF2-0.001");
		 	 		PF_Value2 = 0.001f;
		 	 	}
		 	
		 	 	if(PF_Value3 == 0.0f){
		 	 		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF3-0.001");
		 	 		PF_Value3 = 0.001f;
		 	 	}*/
		 	
		 	
		 	}
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value1:" + PF_Value1);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value2:" + PF_Value2);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value3:" + PF_Value3);
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power1:" + (Volt1 * Current1 * PF_Value1 ));
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power2:" + (Volt2 * Current2 * PF_Value2 ));
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power3:" + (Volt3 * Current3 * PF_Value3 ));
		 	
		 	//double power = (Volt1 * Current1  )+(Volt2 * Current2 )+ (Volt3 * Current3 );
		 	//double power = 1.732 *(Volt1 * Current1  );
		 	
		 	//float power = (Volt1 * Current1 * (float)Math.cos(Math.toRadians(PF_InDegree1)) )+(Volt2 * Current2 * (float)Math.cos(Math.toRadians(PF_InDegree2)))+ (Volt3 * Current3 * (float)Math.cos(Math.toRadians(PF_InDegree3)));
		 	//1\float power = (Volt1 * Current1  )+(Volt2 * Current2 )+ (Volt3 * Current3 );
			 
		 	//float power = (Volt1 * Current1 * Math.acos(PF1) )+(Volt2 * Current2 * Math.acos(PF2))+ (Volt3 * Current3 * Math.acos(PF3));
		 	//float power = (Volt1 * Current1 * PF1)+(Volt2 * Current2 * PF2)+ (Volt3 * Current3 * PF3);
			//double RssConstantInWattHour = (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000000*3600)/power;
		 	double power = (Volt1 * Current1 * PF_Value1 )+(Volt2 * Current2 * PF_Value2)+ (Volt3 * Current3 * PF_Value3);
		 	
		 	ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Total Power :" + power);
		 	//double RssConstantInWattHour = (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000*Integer.parseInt(MUT_ConstInImpulsesPerKiloWattHour))/power;
		 	
		 	double RssConstantInWattHour = (ConstantRefStdRadiant.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000000*3600)/power;
		 	//double RssConstantInWattHour = (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000000*3600)/power;
			
		 	long OutputConstantInWattHour = Math.abs((long)RssConstantInWattHour);
			//System.out.println("Double RssConstantInWattHour : " + RssConstantInWattHour);
			//System.out.println("Long OutputConstantInWattHour : " + OutputConstantInWattHour);
			//ApplicationLauncher.logger.info("calculateRSS_Constantv42 in Watt Hour:"+Math.abs(OutputConstantInWattHour));
			//return Math.abs(OutputConstantInWattHour); 
			ApplicationLauncher.logger.info("calculateRSS_Constantv42 in Watt Hour:"+OutputConstantInWattHour);
			
			/*long RssConstantInKWh = OutputConstantInWattHour*1000;

			ApplicationLauncher.logger.debug("LDU_SendCommandErrorSetting :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
			String LDU_FormatRSS_PulseRate = GUIUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
			OutputConstantInWattHour = RoundRefConst(LDU_FormatRSS_PulseRate);
			*/
			//ApplicationLauncher.logger.info("calculateRSS_Constantv42 in Watt Hour with Round:"+OutputConstantInWattHour);
			
			return OutputConstantInWattHour; 
	}
	 
	 public long RoundRefConst(String Input){
		 
		 long OutputValue = 0;
		 int Exponent = 0;
		 ApplicationLauncher.logger.debug("RoundRefConst Input : " + Input);
		 OutputValue = Long.parseLong(Input.substring(0, 4));
		 
		 //System.out.println("RoundRefConst OutputValue1 : " + OutputValue);
		 
		 Exponent = Integer.parseInt(Input.substring(5, 7));
		 if(Exponent != 0){
			 OutputValue = OutputValue * ((long)(Math.pow(10, (double)Exponent)));
		 }
		 OutputValue = OutputValue/1000;
		 //System.out.println("RoundRefConst Exponent : " + (10^Exponent));
		 ApplicationLauncher.logger.debug("RoundRefConst OutputValue2 : " + OutputValue);
		 
		 return OutputValue;
	 }
	 
/*	 public  void SetPulseConstantDataWithVoltageAndCurrent(float CurrentValue, float voltageValue){
			
			ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: CurrentValue: "+ CurrentValue);
			ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: voltageValue: "+ voltageValue);
			setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);//setting default value
			if(ProcalFeatureEnable.REF_STD_CONST_CALCULATE){
				
				setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit())));
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: "+getRSSPulseRate());
				
				return;
			}

			//if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
				//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
				//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
					//setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
				if(voltageValue >= ConstantConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_2){	
					if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_1);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_2);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_3);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_4);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_5);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_6);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_7);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_8);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_9);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_10);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_11);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_ABOVE_OR_EQUAL_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_below level 12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_2_CURRENT_BELOW_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue >= ConstantConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_3){	
					if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_1);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_2);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_3);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_4);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_5);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_6);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_7);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_8);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_9);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_10);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_11);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_ABOVE_OR_EQUAL_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_below LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_3_CURRENT_BELOW_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue >= ConstantConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4){	
					if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_1);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_2);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_3);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_4);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_5);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_6);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_7);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_8);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_9);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_10);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_11);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_below_level 12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_OR_EQUAL_LEVEL_4_CURRENT_BELOW_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue < ConstantConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4){	
					if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_1);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_2);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_3);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_4);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_5);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_6);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_7);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_8);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_9);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_10);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_11);
						
					}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_ABOVE_OR_EQUAL_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_below_LEVEL_12");
						setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_LEVEL_4_CURRENT_BELOW_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: Setting LTCT Active Pulse Constant: "+getRSSPulseRate());
		}*/
	 
	 	public void manipulateKiggsRefStRssConstant(){
	 		if(ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION){
				
				String manipulatedRSS_PulseConstantInWattHour = SerialDataRefStdKiggs.manipulateRssPulseConstantInWattHour(getRefStdSelectedVoltageTap(),getRefStdSelectedCurrentTap());
				setRSSPulseRate(manipulatedRSS_PulseConstantInWattHour);
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: manipulatedRSS_PulseConstantInWattHour: "+getRSSPulseRate());
				return;
			}
	 	}
	 	
	 
		public  void SetPulseConstantDataWithVoltageAndCurrent(float CurrentValue, float voltageValue){
			
			ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: CurrentValue: "+ CurrentValue);
			ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: voltageValue: "+ voltageValue);
			setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);//setting default value
			if(ProcalFeatureEnable.REF_STD_CONST_CALCULATE){
				
				setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit())));
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: "+getRSSPulseRate());
				
				return;
			}
			
/*			if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
				
				if(ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION){
				
					String manipulatedRSS_PulseConstantInWattHour = SerialDataRefStdKiggs.manipulateRssPulseConstantInWattHour(getRefStdSelectedVoltageTap(),getRefStdSelectedCurrentTap());
					setRSSPulseRate(manipulatedRSS_PulseConstantInWattHour);
					ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: manipulatedRSS_PulseConstantInWattHour: "+getRSSPulseRate());
					return;
				}
			}*/

			//if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
				//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
				//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
					//setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
			
			// commented below section, bcoz rms above 120V it is same, there is no 240 to 480 pulse constant change value
			// per kigg requirement over phone - requested by Sundermurty- 10-Feb-2021
			// reverted the changes on 10-Feb-2021- per Sundermurthy advice
				if(voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_2){	
					if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_below level 12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_3){	
					if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_below LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4){	
					if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_below_level 12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}else if(voltageValue <= ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4){	
					if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_1");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_2");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_3");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_4");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_5");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_6");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_7");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_8");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_9");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_10");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_11");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11);
						
					}else if(CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12);
						
					}else{
						ApplicationLauncher.logger.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_below_LEVEL_12");
						setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
						//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
						
					}
				}
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: Setting LTCT Active Pulse Constant: "+getRSSPulseRate());
		}
		
	 
	public void SetPulseConstantDataWithCurrent(float CurrentValue){
		
		ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: CurrentValue: "+ CurrentValue);
		setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);//setting default value
		if(ProcalFeatureEnable.REF_STD_CONST_CALCULATE){
			
			setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit())));
			ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: "+getRSSPulseRate());
			
			return;
		}

		if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
			//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
			if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
				//setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
				
/*				if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);
					
				}else if(CurrentValue >= ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);
					
				}else{
					setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
					
				}*/
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting LTCT Active Pulse Constant: "+getRSSPulseRate());
			} else if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				//setRSSPulseRate(ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT);
				if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant above 2 Amp: "+getRSSPulseRate());
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);
					
				}else{
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());
					
				}
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant: "+getRSSPulseRate());
				
			}
		}
		else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting reactive Pulse Constant:"+DisplayDataObj.RSS_ReactivePulseConstant);
			//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ReactivePulseConstant);
			if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
				//setRSSPulseRate(ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT);
				if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);
					
				}else{
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					
				}
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting LTCT Reactive Pulse Constant: "+getRSSPulseRate());
				
			} else if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				//setRSSPulseRate(ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT);
				if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Reactive Pulse Constant above 2 Amp: "+getRSSPulseRate());
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);
					
				}else if(CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);
					
				}else{
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					//ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Reactive Pulse Constant below 2 Amp: "+getRSSPulseRate());
					//
				}
				ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting HTCT Reactive Pulse Constant: "+getRSSPulseRate());
			}
		}
		
	}
	
	public void setRefStd_BNC_Constant(){
		ApplicationLauncher.logger.info("setRefStd_BNC_Constant :Entry");
		//if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			if(getAllPortInitSuccess()){
				if ( !(getRSSPulseRate().equals(getLastSetRSS_Pulse_Rate()))  ){
					ApplicationLauncher.logger.debug("setRefStd_BNC_Constant : Setting new value");
					serialDM_Obj.setBNC_ConfigConstantStatus(false);
					serialDM_Obj.RefStd_BNC_ConfigConstTrigger();
					//Integer BNC_ConfigConstantStatusWaitTimeCounter = 20;
					//Integer BNC_ConfigConstantStatusWaitTimeCounter = 40;//wait for 10 sec
					Integer BNC_ConfigConstantStatusWaitTimeCounter = 128;//wait for 32 sec
					while (BNC_ConfigConstantStatusWaitTimeCounter !=0 && !serialDM_Obj.getBNC_ConfigConstantStatus() && (!ProjectExecutionController.getUserAbortedFlag())){
	
						BNC_ConfigConstantStatusWaitTimeCounter--;
						Sleep(250);
					}
					setLastSetRSS_Pulse_Rate(getRSSPulseRate());
					//Sleep(10000);
				}else{
					ApplicationLauncher.logger.info("setRefStd_BNC_Constant : Previously set Ref standard value is same as new: "+getRSSPulseRate());
				}
			} else {
	
				ApplicationLauncher.logger.info("Error Code REFSTD_003: setRefStd_BNC_Constant: Unable to access ref Serial Port");
			}
		//}
		
		//return SerialDM_Obj.getLDU_ResetErrorStatus();
	}

	public void setCreepParameters(JSONObject testcasedetails,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setCreepParameters : Entry");
		JSONArray params = testcasedetails.getJSONArray("test_details");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		
		JSONObject param = new JSONObject();
		String creep_time_duration = "";
		String creep_PercentageOfVoltage = "";
		String creep_no_of_pulses = "";
		int creep_time_duration_insec = 1;
		ApplicationLauncher.logger.debug("setCreepParameters : params.length(): " + params.length());
		for(int i=0; i<params.length(); i++){
			param = params.getJSONObject(i);
			creep_time_duration = param.getString("time_duration");
			creep_PercentageOfVoltage = param.getString("creep_un");
			creep_no_of_pulses = param.getString("creep_pulses");
			ApplicationLauncher.logger.info("creep_time_duration: " + creep_time_duration);
			creep_time_duration_insec = (Integer.parseInt(creep_time_duration)) * 60; 
			ApplicationLauncher.logger.info("creep_time_duration_insec: " + creep_time_duration_insec );
			set_CreepDuration(creep_time_duration_insec);
			setPwrSrcR_PhaseVoltInFloat(rated_volt);
			setPwrSrcR_PhaseCurrentInFloat("0");
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setPwrSrcY_PhaseVoltInFloat(rated_volt);
			setPwrSrcY_PhaseCurrentInFloat("0");
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setPwrSrcB_PhaseVoltInFloat(rated_volt);
			setPwrSrcB_PhaseCurrentInFloat("0");
			set_PwrSrcB_PhaseDegreePhase("0.0");
			set_PwrSrc_Freq(frequency);
			setPercentageOfVoltage(creep_PercentageOfVoltage);
			setCreepNoOfPulses(creep_no_of_pulses);
			setDutImpulsesPerUnit(impulses_per_unit);
			//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
			float CreepVolt = CalculateVoltage(Float.parseFloat(rated_volt), getPercentageOfVoltage());
			Integer CreepDuration = get_CreepDuration();
			


			setR_PhaseOutputVoltage(CreepVolt);
			setR_PhaseOutputCurrent(0f);
			setY_PhaseOutputVoltage(CreepVolt);
			setY_PhaseOutputCurrent(0f);
			manipulateY_PhaseCurrentFor3PhaseDelta();
			setB_PhaseOutputVoltage(CreepVolt);
			setB_PhaseOutputCurrent(0f);
			setLDU_CreepTimeDurationFormat(CreepDuration);
			setPowerSrcOnTimerValue(CreepDuration);
			ArrayList<Integer> devices_mounted = getDevicesMount();
			setDevicesToBeRead(devices_mounted);
			ArrayList<String> All_phases = new ArrayList<String>();
			All_phases.add("All");
			All_phases.add("0.0");
			setPhaseDegreeOutput(All_phases);
			JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
					1);
			set_NoOfPulseReadingToBeSkipped(skip_reading_json);
			//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				//SetPulseConstantDataWithCurrent(0f);
			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
				SetPulseConstantDataWithVoltageAndCurrent(0.0f,CreepVolt);
				if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
					
					setRefStd_BNC_Constant();
				}else{
					String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
					String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
					refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
				}
				
				/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
					
					String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
					String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
					if( (maxVoltage!=null) && (maxCurrent!=null) ){
						if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
							//serialDM_Obj.sandsRefStdConfigureMode();
							clearLastSetRSS_Pulse_Rate();
							setRefStd_BNC_Constant();
						}
					}
				}*/
				
			}
		}  
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setVoltageParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setVoltageParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String test_case_name = param.getString("test_case_name");
		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);
		String selectedRateOfVoltage = param.getString("inf_voltage");
		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		int time_duration_insec = Integer.parseInt(time_duration);//editedbyMO 
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );
		ApplicationLauncher.logger.info("frequency: " + frequency );
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);//getPwrSrcR_PhaseVoltInFloat
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setRateOfVoltage(selectedRateOfVoltage);
		setDutImpulsesPerUnit(impulses_per_unit);

		float percentage_of_volt = getRateOfVoltage();
		float SelectedCurrentValue = CalculateInfCurrent();
		float SelectedVoltageValue = CalculateInfVoltage(percentage_of_volt);
		int timeduration = getInfTimeDuration();

		setAllPhaseParameters(SelectedVoltageValue, SelectedVoltageValue, SelectedVoltageValue, SelectedCurrentValue, phasedegree);
		setPowerSrcOnTimerValue(timeduration);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,SelectedVoltageValue);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
						
			manipulateTargetRmsValue();

		}else {
			manipulateRelayIdValue();
		}
		//}
	}

	public void setFrequencyParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setFrequencyParameters : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String test_case_name = param.getString("test_case_name");
		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);
		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		String volt_percent = param.getString("voltage");
		int time_duration_insec = Integer.parseInt(time_duration); //editedbyMO
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		String frequency = param.getString("frequency");
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );
		ApplicationLauncher.logger.info("frequency: " + frequency );

		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setDutImpulsesPerUnit(impulses_per_unit);

		float SelectedCurrentValue = CalculateInfCurrent();
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt_percent));

		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		int timeduration = getInfTimeDuration();
		setAllPhaseParameters(final_volt_value, final_volt_value, final_volt_value, SelectedCurrentValue, phasedegree);
		setPowerSrcOnTimerValue(timeduration);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setSTAParameters(JSONObject testcasedetails,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("setSTAParameters : Entry");
		JSONArray params = testcasedetails.getJSONArray("test_details");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");

		JSONObject param = new JSONObject();
		String STA_time_duration = "";
		String STA_PercentageOfCurrent = "";
		String STA_no_of_pulses = "";
		String volt_percent = "";
		int STA_time_duration_insec = 1; 
		ApplicationLauncher.logger.debug("setSTAParameters : params.length(): " + params.length());
		for(int i=0; i<params.length(); i++){
			param = params.getJSONObject(i);
			STA_time_duration = param.getString("time_duration");
			STA_PercentageOfCurrent = param.getString("sta_ib");
			STA_no_of_pulses = param.getString("sta_test_pulse_no");
			volt_percent = param.getString("voltage");
			ApplicationLauncher.logger.info("STA_time_duration: " + STA_time_duration);
			STA_time_duration_insec = (Integer.parseInt(STA_time_duration)) * 60; 
			ApplicationLauncher.logger.info("STA_time_duration_insec: " + STA_time_duration_insec );
			set_STADuration(STA_time_duration_insec);
			setPwrSrcR_PhaseVoltInFloat(rated_volt);
			setPwrSrcR_PhaseCurrentInFloat(rated_current);
			setPwrSrcY_PhaseVoltInFloat(rated_volt);
			setPwrSrcY_PhaseCurrentInFloat(rated_current);
			setPwrSrcB_PhaseVoltInFloat(rated_volt);
			setPwrSrcB_PhaseCurrentInFloat(rated_current);
			set_PwrSrc_Freq(frequency);
			setPercentageOfCurrent(STA_PercentageOfCurrent);
			setSTANoOfPulses(STA_no_of_pulses);
			setDutImpulsesPerUnit(impulses_per_unit);
			float STACurrent = CalculateSTACurrent();
			//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
			float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt_percent));
			setR_PhaseOutputVoltage(final_volt_value);
			setR_PhaseOutputCurrent(STACurrent);
			setY_PhaseOutputVoltage(final_volt_value);
			setY_PhaseOutputCurrent(STACurrent);
			setB_PhaseOutputVoltage(final_volt_value);
			setB_PhaseOutputCurrent(STACurrent);
			manipulateY_PhaseCurrentFor3PhaseDelta();
			if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
				setLDU_STATimeDurationFormat(STADuration);
			}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				setLscsLDU_STATimeDurationFormat(STADuration);
			}
			setPowerSrcOnTimerValue(STADuration);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			set_PwrSrcY_PhaseDegreePhase("0.0");
			set_PwrSrcB_PhaseDegreePhase("0.0");

			if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				/*            	set_PwrSrcR_PhaseDegreePhase("0.0");
            	set_PwrSrcY_PhaseDegreePhase("0.0");
            	set_PwrSrcB_PhaseDegreePhase("0.0");*/

				set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
				set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));
				set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE));


			}
			else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				set_PwrSrcR_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
				set_PwrSrcY_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));
				set_PwrSrcB_PhaseDegreePhase(Integer.toString(ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE));


			}

			ArrayList<Integer> devices_mounted = getDevicesMount();
			setDevicesToBeRead(devices_mounted);
			ArrayList<String> All_phases = new ArrayList<String>();
			All_phases.add("All");
			All_phases.add("0.0");
			setPhaseDegreeOutput(All_phases);
			JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
					1);
			set_NoOfPulseReadingToBeSkipped(skip_reading_json);
			//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				//SetPulseConstantDataWithCurrent(STACurrent);
			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {	
				SetPulseConstantDataWithVoltageAndCurrent(STACurrent,final_volt_value);
				if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
					
					setRefStd_BNC_Constant();
				}else{
					String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
					String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
					refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
				}
				
				/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
					String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
					String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
					if( (maxVoltage!=null) && (maxCurrent!=null) ){
						if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
							//serialDM_Obj.sandsRefStdConfigureMode();
							clearLastSetRSS_Pulse_Rate();
							setRefStd_BNC_Constant();
						}
					}
				}*/
				
			}
		}  	
		
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}

	public void setRepeatabilityParameters(JSONObject param,  JSONObject modelparams) throws JSONException{
		ApplicationLauncher.logger.info("Repeatability : Entry");
		String rated_volt = modelparams.getString("rated_voltage_vd");
		rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		String rated_current = modelparams.getString("basic_current_ib");
		String rated_max_current = modelparams.getString("max_current_imax");
		String frequency = modelparams.getString("frequency");
		String impulses_per_unit = modelparams.getString("impulses_per_unit");
		String test_case_name = param.getString("test_case_name");
		ArrayList<String> I_PF_values = ExtractI_PF_From_TP_Name(test_case_name);
		String lag_lead = I_PF_values.get(0);
		String selectedRateOfCurrent = I_PF_values.get(1);
		ArrayList<String> phasedegree = CalculateLagLeadAngle(lag_lead);
		String emin = param.getString("inf_emin");
		String emax = param.getString("inf_emax");
		String no_of_pulses = param.getString("inf_pulses");
		int average =  Integer.parseInt(param.getString("inf_average"));
		String time_duration = param.getString("time_duration");
		String SkipReadingCount = param.getString("skip_reading_count");
		String testruntype = param.getString("testruntype");
		int no_of_readings = ConstantApp.REPEATABILITY_NO_OF_READING;
		String volt_percent = param.getString("voltage");
		ApplicationLauncher.logger.info("setRepeatabilityParameters: time_duration: " + time_duration);
		int time_duration_insec = Integer.parseInt(time_duration);
		int time_duration_inmsec = time_duration_insec * 1000; 
		int total_time_duration_insec = time_duration_insec * no_of_readings;
		int no_of_pulse_reading_skipped = Integer.parseInt(SkipReadingCount);
		ApplicationLauncher.logger.info("emin: " + emin);
		ApplicationLauncher.logger.info("emax: " + emax );

		set_Error_min(emin);
		set_Error_max(emax);
		setPwrSrcR_PhaseVoltInFloat(rated_volt);
		setPwrSrcR_PhaseCurrentInFloat(rated_current);
		setPwrSrcY_PhaseVoltInFloat(rated_volt);
		setPwrSrcY_PhaseCurrentInFloat(rated_current);
		setPwrSrcB_PhaseVoltInFloat(rated_volt);
		setPwrSrcB_PhaseCurrentInFloat(rated_current);
		setPwrSrcR_PhaseMaxCurrentInFloat(rated_max_current);
		set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		set_PwrSrc_Freq(frequency);
		setNoOfPulses(no_of_pulses);
		setAverageNoOfLduReadingRequired(average);
		setInfTimeDuration(total_time_duration_insec);
		setTestRunType(testruntype);
		setRateOfCurrent(selectedRateOfCurrent);
		setDutImpulsesPerUnit(impulses_per_unit);
		setReadingToBeRead(no_of_readings);


		float SelectedCurrentValue = CalculateInfCurrent();
		ApplicationLauncher.logger.info("SelectedCurrentValue: " + SelectedCurrentValue);
		int timeduration = getInfTimeDuration();
		String e_min = get_Error_min();
		String e_max = get_Error_max();
		String No_of_pulses = getNoOfPulses();
		//rated_volt = manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
		float final_volt_value = CalculateVoltage(Float.parseFloat(rated_volt), Float.parseFloat(volt_percent));

		setAllPhaseParameters(final_volt_value, final_volt_value, final_volt_value, SelectedCurrentValue, phasedegree);
		setPowerSrcOnTimerValue(total_time_duration_insec);
		ArrayList<Integer> devices_mounted = getDevicesMount();
		setDevicesToBeRead(devices_mounted);
		serialDM_Obj.setSerialLDU_ComRefreshTimeInMsec(time_duration_inmsec);
		JSONObject skip_reading_json = getSkipReadingForAllDevices(devices_mounted,
				no_of_pulse_reading_skipped);
		set_NoOfPulseReadingToBeSkipped(skip_reading_json);
		
		//if(getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
			//SetPulseConstantDataWithCurrent(SelectedCurrentValue);
		if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SetPulseConstantDataWithVoltageAndCurrent(SelectedCurrentValue,final_volt_value);
			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				
				setRefStd_BNC_Constant();
			}else{
				String maxVoltage = GuiUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GuiUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				refStdUpdateConfigurationSettings(maxVoltage,maxCurrent);
			}
			
			/*else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				String maxVoltage = GUIUtils.getMax(getR_PhaseOutputVoltage(),getY_PhaseOutputVoltage(),getB_PhaseOutputVoltage());
				String maxCurrent = GUIUtils.getMax(getR_PhaseOutputCurrent(),getY_PhaseOutputCurrent(),getB_PhaseOutputCurrent());
				if( (maxVoltage!=null) && (maxCurrent!=null) ){
					if(!SerialDataSandsRefStd.isLastSetModeConfigurationSame(maxVoltage,maxCurrent)){
						//serialDM_Obj.sandsRefStdConfigureMode();
						clearLastSetRSS_Pulse_Rate();
						setRefStd_BNC_Constant();
					}
				}
			}*/
			
		}
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			manipulateTargetRmsValue();
		}else {
			manipulateRelayIdValue();
		}
	}


	public ArrayList<Integer> getDevicesMount(){
		String project_name = ProjectExecutionController.getCurrentProjectName();
		JSONObject devices_json = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		ArrayList<Integer> SelectedRacks = new ArrayList<Integer>();
		JSONArray devices = new JSONArray();
		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject device = new JSONObject();
			for(int i=0; i<devices.length(); i++){
				device = devices.getJSONObject(i);
				SelectedRacks.add(device.getInt("Rack_ID"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDevicesMount :JSONException:"+ e.getMessage());
		}
		return SelectedRacks;
	}

	public ArrayList<String> CalculateLagLeadAngle(String input){
		ApplicationLauncher.logger.info("CalculateLagLeadAngle : Entry");
		String lag_lead = input.substring(input.length() - 1);
		ApplicationLauncher.logger.info("input: " + input);
		ApplicationLauncher.logger.info("get_EM_Model_type: " + getDeployedEM_ModelType());
		double phasedegree = 0;
		float lag_lead_value = 0;
		String phase = "";


		String FirstPhaseDisplayName = "A";
		String SecondPhaseDisplayName = "B";
		String ThirdPhaseDisplayName = "C";

		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

/*		int ReactiveImportExportSignAngle = 1;

		if(getEnergyFlowMode().equals( ConstantPowerSource.IMPORT_MODE)){
			ReactiveImportExportSignAngle = 1;
		}else if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
			ReactiveImportExportSignAngle = -1;
		}*/
		ArrayList<String> All_phases = new ArrayList<String>();
		if(lag_lead.equals(ConstantApp.PF_LAG)){
			try{
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE +(Math.acos(lag_lead_value) * (180/Math.PI));

				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE-(Math.acos(lag_lead_value) * (180/Math.PI));
					
				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				}


				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : phasedegree:"+phasedegree);

			}
			catch(Exception e1){
				//e1.printStackTrace();
				//ApplicationLauncher.logger.info("calculatelag_lead :Exception1:"+ e1.getMessage());
				phase = input.substring(0,1);
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE+ (Math.acos(lag_lead_value) * (180/Math.PI));

				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE-(Math.acos(lag_lead_value) * (180/Math.PI));

				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				}

				if(phase.equals(FirstPhaseDisplayName)){

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(SecondPhaseDisplayName)){

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(ThirdPhaseDisplayName)){

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}
				else{

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

		}
		else if(lag_lead.equals(ConstantApp.PF_LEAD)){
			try{
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE-(Math.acos(lag_lead_value) * (180/Math.PI));

				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(lag_lead_value) * (180/Math.PI));
					
				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				}
				//phasedegree =  -(phasedegree);				

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			}
			catch(Exception e2){
				//e2.printStackTrace();
				//ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+ e2.getMessage());
				phase = input.substring(0,1);
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE-(Math.acos(lag_lead_value) * (180/Math.PI));

				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE+(Math.acos(lag_lead_value) * (180/Math.PI));
					
				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180/Math.PI);
				}

				//phasedegree = - phasedegree;

				if(phase.equals(FirstPhaseDisplayName)){

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(SecondPhaseDisplayName)){

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(ThirdPhaseDisplayName)){

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}
				else{

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}


			ApplicationLauncher.logger.info("phasedegree: "  + phasedegree);
		}
		else{
			try{
				lag_lead_value = Float.parseFloat(input);
				phasedegree = 0.0;
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					//phasedegree = 0.0;
					phasedegree =  ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){


					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}
				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			}
			catch(Exception e3){
				//e2.printStackTrace();
				//ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+ e2.getMessage());
				phase = input.substring(0,1);
				phasedegree = 0.0;
				if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					//phasedegree = 0.0;
					phasedegree =  ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				}
				else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){

					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				}
				else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}

				//phasedegree = - phasedegree;

				if(phase.equals(FirstPhaseDisplayName)){


					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(SecondPhaseDisplayName)){

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if(phase.equals(ThirdPhaseDisplayName)){

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}
				else{

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}


			ApplicationLauncher.logger.info("phasedegree: "  + phasedegree);

		}
		ApplicationLauncher.logger.info("All_phases: "  + All_phases);
		setPhaseDegreeOutput(All_phases);
		return All_phases;
	}
	
	public void manipulateY_PhaseCurrentFor3PhaseDelta (){
		ApplicationLauncher.logger.debug("manipulateY_PhaseCurrentFor3PhaseDelta: Entry" );
		String metertype = getDeployedEM_ModelType();
		if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)){
			ApplicationLauncher.logger.debug("manipulateY_PhaseCurrentFor3PhaseDelta: Y current to Zero" );
			setY_PhaseOutputCurrent(0f);
		}
	}

	public void setAllPhaseParameters(float rated_voltA, float rated_voltB, 
			float rated_voltC, float rated_current, 
			ArrayList<String> phasedegree){

		String FirstPhaseDisplayName = "A";
		String SecondPhaseDisplayName = "B";
		String ThirdPhaseDisplayName = "C";
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltA: " + rated_voltA );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltB: " + rated_voltB );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltC: " + rated_voltC );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_current: " + rated_current );
		
		
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

		//String metertype = getDeployedEM_ModelType();
		String phase = phasedegree.get(0);
		setR_PhaseOutputVoltage(rated_voltA);
		setY_PhaseOutputVoltage(rated_voltB);
		setB_PhaseOutputVoltage(rated_voltC);
		if(phase.equals("All")){
			ApplicationLauncher.logger.debug("setAllPhaseParameters: All");
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
			manipulateY_PhaseCurrentFor3PhaseDelta();
			
			//if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)){
				//ApplicationLauncher.logger.info("setAllPhaseParameters: 3 Phase Delta :setting y current zero" );
				//setY_PhaseOutputCurrent(0f);
				
			//}
		}else if(phase.equals(FirstPhaseDisplayName)){
			ApplicationLauncher.logger.debug("setAllPhaseParameters: First Phase");
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
		}else if(phase.equals(SecondPhaseDisplayName)){
			ApplicationLauncher.logger.debug("setAllPhaseParameters: Second Phase");
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}else if(phase.equals(ThirdPhaseDisplayName)){
			ApplicationLauncher.logger.debug("setAllPhaseParameters: Third Phase");
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		}else{
			ApplicationLauncher.logger.debug("setAllPhaseParameters: Default Phase");
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
			setY_PhaseOutputCurrent(rated_current);
			manipulateY_PhaseCurrentFor3PhaseDelta();
			set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getR_PhaseOutputVoltage: " + getR_PhaseOutputVoltage() );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getY_PhaseOutputVoltage: " + getY_PhaseOutputVoltage() );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getB_PhaseOutputVoltage: " + getB_PhaseOutputVoltage() );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getR_PhaseOutputCurrent: " + getR_PhaseOutputCurrent() );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getY_PhaseOutputCurrent: " + getY_PhaseOutputCurrent() );
		ApplicationLauncher.logger.debug("setAllPhaseParameters: getB_PhaseOutputCurrent: " + getB_PhaseOutputCurrent() );
	}

	public void setAllPhaseParametersForVoltUnbalance(float rated_volt1, float rated_volt2, 
			float rated_volt3, float rated_current, String volt_phase, String phasedegree){
		String All_Phase = 	"ABC";
		String FirstPhase = "A";
		String SecondPhase = "B";
		String ThirdPhase = "C";
		String FirstAndSecondPhase = 	"AB";
		String SecondAndThirdPhase = 	"BC";
		String FirstAndThirdPhase = 	"AC";
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			All_Phase = ConstantApp.FIRST_PHASE_DISPLAY_NAME +ConstantApp.SECOND_PHASE_DISPLAY_NAME +ConstantApp.THIRD_PHASE_DISPLAY_NAME ;
			FirstPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndSecondPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME	+ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			SecondAndThirdPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndThirdPhase = 	ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}
		if(volt_phase.equals(All_Phase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree);
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}	else if(volt_phase.equals(FirstPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
		}else if(volt_phase.equals(SecondPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree);
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}else if(volt_phase.equals(ThirdPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
		}else if(volt_phase.equals(FirstAndSecondPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree);
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}else if(volt_phase.equals(SecondAndThirdPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree);
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}else if(volt_phase.equals(FirstAndThirdPhase)){
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
		}
		else{
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree);
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
			manipulateY_PhaseCurrentFor3PhaseDelta();
		}
	}


	public ArrayList<String> ExtractI_PF_From_TP_Name(String testcasename){
		String testname_wo_type = testcasename.substring(testcasename.indexOf("-") + 1);
		ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: testname_wo_type : " + testname_wo_type);
		ArrayList<String> I_PF_values = new ArrayList<String>();
		if(testname_wo_type.contains("U")){
			String[] test_params = testname_wo_type.split("-");
			String lag_lead = test_params[1];
			String selectedRateOfCurrent = test_params[2];
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: lag_lead : " + lag_lead);
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: selectedRateOfCurrent : " + selectedRateOfCurrent);
			I_PF_values.add(lag_lead);
			I_PF_values.add(selectedRateOfCurrent);
		}
		else{
			String[] test_params = testname_wo_type.split("-");
			String lag_lead = test_params[0];
			String selectedRateOfCurrent = test_params[1];
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: lag_lead : " + lag_lead);
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: selectedRateOfCurrent : " + selectedRateOfCurrent);
			I_PF_values.add(lag_lead);
			I_PF_values.add(selectedRateOfCurrent);
		}
		return I_PF_values;
	}

	public String Extract_V_phase_From_TP_Name(String testcasename){
		String testname_wo_type = testcasename.substring(testcasename.indexOf("-") + 1);
		ApplicationLauncher.logger.info("Extract_V_phase_From_TP_Name: testname_wo_type : " + testname_wo_type);
		String volt_phase = "";
		if(testname_wo_type.contains("U")){
			String[] test_params = testname_wo_type.split("-");
			String phase = test_params[0];
			String[] volt_split = testname_wo_type.split(":");
			volt_phase = volt_split[0];
			ApplicationLauncher.logger.info("Extract_V_phase_From_TP_Name: testname_wo_type : " + volt_phase);
		}
		return volt_phase;
	}



	public int get_no_of_devices_connected(){
		ApplicationLauncher.logger.info("get_no_of_devices_connected Invoked:");
		int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
		int count =0;
		//ApplicationLauncher.logger.info("MaximumNumberOfDeviceConnected: " + MaximumNumberOfDeviceConnected);
		
		int initialLduAddress = 1;
		if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
			
			if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
				MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
			}else if(getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
				initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
				MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
			}
		}
		for(int Address=initialLduAddress;Address<=MaximumNumberOfDeviceConnected;Address++){
			try {
				if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
					ApplicationLauncher.logger.info("get_no_of_devices_connected: Address:  " + Address);
					count++;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("get_no_of_devices_connected :JSONException :"+ e.getMessage());
			}
		}
		ApplicationLauncher.logger.info("count: " + count);

		return count;
	}

/*	public float CalculateSTACurrent(){
		ApplicationLauncher.logger.info("CalculateSTACurrent :Entry");
		float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();//"240.0";
		float CurrentPercentage = DeviceDataManagerController.getPercentageOfCurrent();

		float OutputCurrent = RatedCurrent*CurrentPercentage/100 ;
		ApplicationLauncher.logger.info("Output Current: "+ OutputCurrent);
		//String op_current = String.format("%.02f", OutputCurrent);
		String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, OutputCurrent);
		OutputCurrent = Float.parseFloat(op_current);
		return OutputCurrent;
	}*/
	
	public float CalculateSTACurrent(){
		ApplicationLauncher.logger.info("CalculateSTACurrent :Entry");
		float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();//"240.0";
		float CurrentPercentage = DeviceDataManagerController.getPercentageOfCurrent();

		float OutputCurrent = RatedCurrent*CurrentPercentage/100 ;
		ApplicationLauncher.logger.info("CalculateSTACurrent: Output Current1: "+ OutputCurrent);
		//String op_current = String.format("%.02f", OutputCurrent);
		String op_current="";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			 op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			 ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current3: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("Output Current4: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current5: "+ OutputCurrent);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			 op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(OutputCurrent);
			 ApplicationLauncher.logger.info("Output Current5A: "+ OutputCurrent);
		}else{
			if(OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current6: "+ OutputCurrent);
			}else {
				 if(OutputCurrent < 1.0f) {
					 OutputCurrent = Math.round(OutputCurrent);
					 ApplicationLauncher.logger.info("Output Current7: "+ OutputCurrent);
				 }
				 op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				 ApplicationLauncher.logger.info("Output Current8: "+ OutputCurrent);
			}
		}
		
		
		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("Output Current6: "+ OutputCurrent);
		return OutputCurrent;
	}

	public static void setR_PhaseOutputVoltage(Float Value)
	{
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			R_PhaseOutputVoltage=String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			R_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			R_PhaseOutputVoltage=ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format( Value);
		}else{
			R_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}
		
	}
	
	public static void setR_PhaseOutputVoltageStr(String Value)
	{
			R_PhaseOutputVoltage=Value;
		
	}

	final public static String getR_PhaseOutputVoltage()
	{
		return R_PhaseOutputVoltage;
	}

	public static void setY_PhaseOutputVoltage(Float Value)
	{
		//Y_PhaseOutputVoltage=String.format(ConstantMtePowerSource.VOLTAGE_RESOLUTION, Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			Y_PhaseOutputVoltage=String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			Y_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			Y_PhaseOutputVoltage=ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format( Value);
		}else{
			Y_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}
	}
	
	public static void setY_PhaseOutputVoltageStr(String Value)
	{
			Y_PhaseOutputVoltage=Value;
		
	}

	public static void setB_PhaseOutputVoltage(Float Value)
	{
		//B_PhaseOutputVoltage=String.format(ConstantMtePowerSource.VOLTAGE_RESOLUTION, Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			B_PhaseOutputVoltage=String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			B_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			B_PhaseOutputVoltage=ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format(Value);
		}else{
			B_PhaseOutputVoltage=String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}
	}
	
	public static void setB_PhaseOutputVoltageStr(String Value)
	{
			B_PhaseOutputVoltage=Value;
		
	}



	final public static String getB_PhaseOutputVoltage()
	{
		return B_PhaseOutputVoltage;
	}



	final public static String getY_PhaseOutputVoltage()
	{
		return Y_PhaseOutputVoltage;
	}

	public static void setLDU_STATimeDurationFormat(Integer STATimeInSec)
	{
		ApplicationLauncher.logger.info("setLDU_STATimeDurationFormat :Entry");
		int sec = (STATimeInSec % 60);
		//int min = ((STATimeInSec / 60)%60);
		int min = STATimeInSec/60;
		if(min >99){
			min =0;
		}
		STATimeDuration = String.format("%02d", min)  + String.format("%02d", sec);

	}
	
	public static void setLscsLDU_STATimeDurationFormat(Integer STATimeInSec)
	{
		ApplicationLauncher.logger.info("setLscsLDU_STATimeDurationFormat :Entry");
		int sec = (STATimeInSec % 60);
		//int min = ((STATimeInSec / 60)%60);
		int min = STATimeInSec/60;
		if(min > ConstantLduLscs.MAXIMUM_ALLOWED_MINUTES){
			min =0;
		}
		STATimeDuration = String.format("%03d", min)  + String.format("%02d", sec);

	}

	public String getLDU_STATimeDurationFormat(){

		return this.STATimeDuration;
	}
	
	public String getLscsLDU_STATimeDurationFormat(){

		return this.STATimeDuration;
	}

	public static void setR_PhaseOutputCurrent(Float Value)
	{
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			R_PhaseOutputCurrent=String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				R_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				R_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			R_PhaseOutputCurrent=ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format( Value);
		}else{
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				R_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				R_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}
	
	public static void setR_PhaseOutputCurrentStr(String Value)
	{
		R_PhaseOutputCurrent =  Value;
	}



	final public static String getR_PhaseOutputCurrent()
	{
		return R_PhaseOutputCurrent;
	}


	public static void setY_PhaseOutputCurrent(Float Value)
	{
		//Y_PhaseOutputCurrent=String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			Y_PhaseOutputCurrent=String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				Y_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				Y_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			Y_PhaseOutputCurrent=ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format( Value);
		}else {
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				Y_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				Y_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}

	public static void setY_PhaseOutputCurrentStr(String Value)
	{
		Y_PhaseOutputCurrent =  Value;
	}

	final public static String getY_PhaseOutputCurrent()
	{
		return Y_PhaseOutputCurrent;
	}

	public static void setB_PhaseOutputCurrent(Float Value)
	{
		//B_PhaseOutputCurrent=String.format(ConstantMtePowerSource.CURRENT_RESOLUTION, Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			B_PhaseOutputCurrent=String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				B_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				B_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			B_PhaseOutputCurrent=ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format( Value);
		}else {
			if(Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				B_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			}else {
				B_PhaseOutputCurrent=String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}

	public static void setB_PhaseOutputCurrentStr(String Value)
	{
		B_PhaseOutputCurrent =  Value;
	}

	final public static String getB_PhaseOutputCurrent()
	{
		return B_PhaseOutputCurrent;
	}

	public static void setR_PhaseOutputPhase(Float Value)
	{
		
		
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			R_PhaseOutputPhase=String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
		}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			R_PhaseOutputPhase=String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION, Value);
		}
	}



	final public String getR_PhaseOutputPhase()
	{
		return R_PhaseOutputPhase;
	}

	public static void setY_PhaseOutputPhase(Float Value)
	{
		Y_PhaseOutputPhase=String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
	}



	final public String getY_PhaseOutputPhase()
	{
		return Y_PhaseOutputPhase;
	}

	public static void setB_PhaseOutputPhase(Float Value)
	{
		B_PhaseOutputPhase = String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
	}



	final public String getB_PhaseOutputPhase()
	{
		return B_PhaseOutputPhase;
	}

	public static void setVolt_Unbalanced_PowerOn(boolean Value)
	{
		Volt_Unbalanced_PowerOn=  Value;
	}



	final public boolean getVolt_Unbalanced_PowerOn()
	{
		return Volt_Unbalanced_PowerOn;
	}


	public static void setPowerSrcOnTimerValue(Integer ValueInSec)
	{
		PowerSrcOnTimerInSec=ValueInSec;
		ApplicationLauncher.logger.info("ValueInSec: "+ ValueInSec);
	}



	final public Integer getPowerSrcOnTimerValue() {
		return PowerSrcOnTimerInSec;
	}

	public static void setLDU_ReadDataFlag(boolean value) {

		bLDU_ReadData = value;
	}


	public static boolean getLDU_ReadDataFlag() {

		return bLDU_ReadData;
	}
	public static void setRefStdReadDataFlag(boolean value) {

		bRefStdReadData = value;
		ApplicationLauncher.logger.info("setRefStdReadDataFlag:"+bRefStdReadData);
	}

	public static boolean getRefStdReadDataFlag() {

		return bRefStdReadData;
	}

	public static void setAllPortInitSuccess(boolean value) {

		AllPortInitSuccess = value;
		ApplicationLauncher.logger.info("setAllPortInitSuccess:"+AllPortInitSuccess);
	}

	public static boolean getAllPortInitSuccess() {

		return AllPortInitSuccess;
	}

	public static ArrayList<Integer> getDevicesToBeRead(){
		return DevicesMounted;
	}

	public static void setDevicesToBeRead(ArrayList<Integer> values){
		DevicesMounted = values;
	}

	public float CalculateVoltage(float RatedVolt, float VoltPercentage){
		ApplicationLauncher.logger.info("CalculateVoltage :Entry");
		float OutputVolt = RatedVolt*VoltPercentage/100 ;
		ApplicationLauncher.logger.info("Parse Voltage"+ Float.valueOf(RatedVolt));
		ApplicationLauncher.logger.info("Parse output Voltage"+ Float.valueOf(VoltPercentage));
		ApplicationLauncher.logger.info("Output Voltage"+ OutputVolt);
		//String op_volt = String.format("%.02f", OutputVolt);
		String op_volt = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, OutputVolt);
		OutputVolt = Float.parseFloat(op_volt);
		return OutputVolt;
	}	

	public void setLDU_CreepTimeDurationFormat(Integer CreepTimeInSec)
	{
		ApplicationLauncher.logger.info("setLDU_CreepTimeDurationFormat :Entry");	
		int sec = (CreepTimeInSec % 60);
		//int min = ((CreepTimeInSec / 60)%60);
		int min = CreepTimeInSec/60;
		if(min >99){
			min =0;
		}

		CreepTimeDuration = String.format("%02d", min)  + String.format("%02d", sec);
	}

	public String getLDU_CreepTimeDurationFormat(){

		return this.CreepTimeDuration;
	}

	public JSONObject getSkipReadingForAllDevices(ArrayList<Integer> rack_id, int skip_reading){
		JSONObject jobj = new JSONObject();
		String rack = "";
		try {
			for(int i=0; i<rack_id.size(); i++){
				rack = Integer.toString(rack_id.get(i));
				jobj.put(rack, skip_reading);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getSkipReadingForAllDevices :JSONException :"+ e.getMessage());
		}
		return jobj;
	}
	
	public static ReportConfigModel getReportConfigParsedData() {
		return reportConfigParsedData;
	}




	public static void setReportConfigParsedData(ReportConfigModel reportConfigParsedData) {
		DeviceDataManagerController.reportConfigParsedData = reportConfigParsedData;
	}
	
	public static String getUserName() {
		return userName;
	}




	public static void setUserName(String userName) {
		DeviceDataManagerController.userName = userName;
	}


	public static LscsCalibrationConfigModel getLscsCalibrationConfigParsedKey() {
		return lscsCalibrationConfigParsedKey;
	}


	public static void setLscsCalibrationConfigParsedKey(LscsCalibrationConfigModel lscsCalibrationConfigParsedKey) {
		DeviceDataManagerController.lscsCalibrationConfigParsedKey = lscsCalibrationConfigParsedKey;
	}
	
	public static String getTargetVoltageRms(String selectedPhase, String inpVoltageValue){
		String voltageRmsValueStr = "00000";
		try{
			float inpVoltage = Float.parseFloat(inpVoltageValue);
			ApplicationLauncher.logger.debug("getTargetVoltageRms: inpVoltage: "+ inpVoltage);
			if(inpVoltage <= 0.0f){
				return voltageRmsValueStr;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().size();
			List<VoltageCalibration> voltCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration();
			VoltageCalibration voltCalib = new VoltageCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< voltCalibList.size() ; phaseIndex++){
				
				if(voltCalibList.get(phaseIndex).getVoltagePhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("getTargetVoltageRms: phaseIndex: "+ phaseIndex);
					voltCalib = voltCalibList.get(phaseIndex);
					//float inpVoltage = Float.parseFloat(inpVoltageValue);
					//ApplicationLauncher.logger.debug("getTargetVoltageRms: inpVoltage: "+ inpVoltage);
					List<VoltageTap> voltCalibTapList = voltCalib.getVoltageTap();
					for(int  tapIndex = 0; tapIndex< voltCalib.getVoltageTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("getTargetVoltageRms: getVoltageTapMax: "+ voltCalibTapList.get(tapIndex).getVoltageTapMax());
						if(inpVoltage <= voltCalibTapList.get(tapIndex).getVoltageTapMax()){
							ApplicationLauncher.logger.debug("getTargetVoltageRms: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("getTargetVoltageRms: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = voltCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpVoltage <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("getTargetVoltageRms: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpVoltage){
										voltageRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
										return voltageRmsValueStr;
									}else {
										
										float inpVolt_X = inpVoltage;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										voltageRmsValueStr = GuiUtils.calculateLinearInterpolation(inpVolt_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										return voltageRmsValueStr;
									}
								}else{
									lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getTargetVoltageRms: Exception: "+e.getMessage());
		}
		
		return voltageRmsValueStr;
	}
	
	public static String getAbsoluteVoltage(String selectedPhase, String userInputVoltageValue, String feedbackVoltageRmsValue,
			Double gainValue, Double offsetValue){
		String voltageRmsValueStr = "00000";
		try{
			float inpVoltage = Float.parseFloat(userInputVoltageValue);
			ApplicationLauncher.logger.debug("getAbsoluteVoltage: inpVoltage: "+ inpVoltage);
			if(inpVoltage <= 0.0f){
				return voltageRmsValueStr;
			}
			//String feedbackRmsValueStr = String.valueOf((Double.valueOf(feedbackVoltageRmsValue)));
			//ApplicationLauncher.logger.debug("getAbsoluteVoltage: feedbackVoltageRmsValue: "+ feedbackVoltageRmsValue);
			//ApplicationLauncher.logger.debug("getAbsoluteVoltage: feedbackRmsValueStr: "+ feedbackRmsValueStr);
			if(Double.valueOf(feedbackVoltageRmsValue)> 0.0f){
				voltageRmsValueStr = String.valueOf((Double.valueOf(feedbackVoltageRmsValue) - offsetValue)/gainValue );
				ApplicationLauncher.logger.debug("getAbsoluteVoltage: AbsoluteVoltageValue1: "+ voltageRmsValueStr);
				return voltageRmsValueStr;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAbsoluteVoltage: Exception: "+e.getMessage());
		}
		
		return voltageRmsValueStr;
	}
	
	public static Double manipulateVoltageGainValue(String selectedPhase, String userInputVoltageValue){
		double voltageRmsValue = 0.0; //"00000";
		try{
			float inpVoltage = Float.parseFloat(userInputVoltageValue);
			ApplicationLauncher.logger.debug("manipulateVoltageGainValue: inpVoltage: "+ inpVoltage);
			if(inpVoltage <= 0.0f){
				return voltageRmsValue;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().size();
			List<VoltageCalibration> voltCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration();
			VoltageCalibration voltCalib = new VoltageCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< voltCalibList.size() ; phaseIndex++){
				
				if(voltCalibList.get(phaseIndex).getVoltagePhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("manipulateVoltageGainValue: phaseIndex: "+ phaseIndex);
					voltCalib = voltCalibList.get(phaseIndex);
					//float inpVoltage = Float.parseFloat(inpVoltageValue);
					//ApplicationLauncher.logger.debug("manipulateVoltageGainValue: inpVoltage: "+ inpVoltage);
					List<VoltageTap> voltCalibTapList = voltCalib.getVoltageTap();
					for(int  tapIndex = 0; tapIndex< voltCalib.getVoltageTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getVoltageTapMax: "+ voltCalibTapList.get(tapIndex).getVoltageTapMax());
						if(inpVoltage <= voltCalibTapList.get(tapIndex).getVoltageTapMax()){
							ApplicationLauncher.logger.debug("manipulateVoltageGainValue: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("manipulateVoltageGainValue: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = voltCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpVoltage <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									//if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpVoltage){
										//voltageRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
									//ApplicationLauncher.logger.info("manipulateVoltageGainValue :powerSourceFeedBackVoltageRms : " + feedbackVoltageRmsValue);
									ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									//ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									//double sumValue = Double.valueOf(feedbackVoltageRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue();
									//ApplicationLauncher.logger.debug("manipulateVoltageGainValue: sumValue: "+ sumValue);
									//double calculatedValue = sumValue / calibPointList.get(calibPointIndex).getGainValue();
									//ApplicationLauncher.logger.debug("manipulateVoltageGainValue: calculatedValue: "+ calculatedValue);
									//voltageRmsValueStr = String.valueOf((Double.valueOf(feedbackVoltageRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									voltageRmsValue = calibPointList.get(calibPointIndex).getGainValue();
									ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getGainValue: "+ voltageRmsValue);
									return voltageRmsValue;
/*									}else {
										
										float inpVolt_X = inpVoltage;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										voltageRmsValueStr = GUIUtils.calculateLinearInterpolation(inpVolt_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										
										
										
										return voltageRmsValueStr;
									}*/
								}else{
									//lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									//lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();
									//voltageRmsValueStr = String.valueOf((Integer.valueOf(feedbackVoltageRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									voltageRmsValue = calibPointList.get(calibPointIndex).getGainValue();
									ApplicationLauncher.logger.debug("manipulateVoltageGainValue: getGainValue2: "+ voltageRmsValue);
									//return voltageRmsValueStr;
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateVoltageGainValue: Exception: "+e.getMessage());
		}
		
		return voltageRmsValue;
	}
	
	
	public static Double manipulateVoltageOffsetValue(String selectedPhase, String userInputVoltageValue){
		Double voltageRmsValue = 0.0;//"00000";
		try{
			float inpVoltage = Float.parseFloat(userInputVoltageValue);
			ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: inpVoltage: "+ inpVoltage);
			if(inpVoltage <= 0.0f){
				return voltageRmsValue;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().size();
			List<VoltageCalibration> voltCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration();
			VoltageCalibration voltCalib = new VoltageCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< voltCalibList.size() ; phaseIndex++){
				
				if(voltCalibList.get(phaseIndex).getVoltagePhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: phaseIndex: "+ phaseIndex);
					voltCalib = voltCalibList.get(phaseIndex);
					//float inpVoltage = Float.parseFloat(inpVoltageValue);
					//ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: inpVoltage: "+ inpVoltage);
					List<VoltageTap> voltCalibTapList = voltCalib.getVoltageTap();
					for(int  tapIndex = 0; tapIndex< voltCalib.getVoltageTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getVoltageTapMax: "+ voltCalibTapList.get(tapIndex).getVoltageTapMax());
						if(inpVoltage <= voltCalibTapList.get(tapIndex).getVoltageTapMax()){
							ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = voltCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpVoltage <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									//if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpVoltage){
										//voltageRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
									//ApplicationLauncher.logger.info("manipulateVoltageOffsetValue :powerSourceFeedBackVoltageRms : " + feedbackVoltageRmsValue);
									//ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									//double sumValue = Double.valueOf(feedbackVoltageRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue();
									//ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: sumValue: "+ sumValue);
									//double calculatedValue = sumValue / calibPointList.get(calibPointIndex).getGainValue();
									//ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: calculatedValue: "+ calculatedValue);
									//voltageRmsValueStr = String.valueOf((Double.valueOf(feedbackVoltageRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									voltageRmsValue = calibPointList.get(calibPointIndex).getOffsetValue();
									ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getOffsetValue: "+ voltageRmsValue);
									return voltageRmsValue;
/*									}else {
										
										float inpVolt_X = inpVoltage;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										voltageRmsValueStr = GUIUtils.calculateLinearInterpolation(inpVolt_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										
										
										
										return voltageRmsValueStr;
									}*/
								}else{
									//lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									//lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();
									//voltageRmsValueStr = String.valueOf((Integer.valueOf(feedbackVoltageRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									voltageRmsValue = calibPointList.get(calibPointIndex).getOffsetValue();
									ApplicationLauncher.logger.debug("manipulateVoltageOffsetValue: getOffsetValue2: "+ voltageRmsValue);
									//return voltageRmsValueStr;
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateVoltageOffsetValue: Exception: "+e.getMessage());
		}
		
		return voltageRmsValue;
	}
	
	public static String getTargetCurrentRms(String selectedPhase, String inpCurrentValue){
		String currentRmsValueStr = "00000";
		try{
			float inpCurrent = Float.parseFloat(inpCurrentValue);
			ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent: "+ inpCurrent);
			if(inpCurrent <= 0.0f){
				return currentRmsValueStr;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
			List<CurrentCalibration> currentCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
			CurrentCalibration currentCalib = new CurrentCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
				
				if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("getTargetCurrentRms: phaseIndex: "+ phaseIndex);
					currentCalib = currentCalibList.get(phaseIndex);
					/*float inpCurrent = Float.parseFloat(inpCurrentValue);
					ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent: "+ inpCurrent);*/
					List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
					for(int  tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("getTargetCurrentRms: getCurrentTapMax: "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
						if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
							ApplicationLauncher.logger.debug("getTargetCurrentRms: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("getTargetCurrentRms: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = currentCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpCurrent <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("getTargetCurrentRms: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpCurrent){
										ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent equal");
										currentRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
										return currentRmsValueStr;
									}else {
										ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent not equal");
										float inpCurrent_X = inpCurrent;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										currentRmsValueStr = GuiUtils.calculateLinearInterpolation(inpCurrent_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										return currentRmsValueStr;
									}
								}else{
									ApplicationLauncher.logger.debug("getTargetCurrentRms: inpCurrent greater");
									lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getTargetCurrentRms: Exception: "+e.getMessage());
		}
		
		return currentRmsValueStr;
	}
	
	//getAbsoluteCurrent
	
	//String userInputVoltageValue, String feedbackVoltageRmsValue
	
	public static String getAbsoluteCurrent(String selectedPhase, String userInputCurrentValue,String feedbackCurrentRmsValue,
			Double gainValue, Double offsetValue){
		String currentRmsValueStr = "00000";
		try{
			float inpCurrent = Float.parseFloat(userInputCurrentValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: inpCurrent: "+ inpCurrent);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: feedbackCurrentRmsValue: "+ feedbackCurrentRmsValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: gainValue: "+ gainValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: offsetValue: "+ offsetValue);
			if(inpCurrent <= 0.0f){
				return currentRmsValueStr;
			}
			if(Double.valueOf(feedbackCurrentRmsValue)> 0.0f){
				currentRmsValueStr = String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - offsetValue)/gainValue );
				ApplicationLauncher.logger.debug("getAbsoluteCurrent: AbsoluteCurrentValue1: "+ currentRmsValueStr);
				return currentRmsValueStr;
			}
			
/*			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
			List<CurrentCalibration> currentCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
			CurrentCalibration currentCalib = new CurrentCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
				
				if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("getAbsoluteCurrent: phaseIndex: "+ phaseIndex);
					currentCalib = currentCalibList.get(phaseIndex);
					float inpCurrent = Float.parseFloat(inpCurrentValue);
					ApplicationLauncher.logger.debug("getAbsoluteCurrent: inpCurrent: "+ inpCurrent);
					List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
					for(int  tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("getAbsoluteCurrent: getCurrentTapMax: "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
						if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
							ApplicationLauncher.logger.debug("getAbsoluteCurrent: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("getAbsoluteCurrent: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = currentCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpCurrent <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("getAbsoluteCurrent: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									ApplicationLauncher.logger.info("getAbsoluteCurrent :powerSourceFeedBackVoltageRms : " + feedbackCurrentRmsValue);
									ApplicationLauncher.logger.debug("getAbsoluteCurrent: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									ApplicationLauncher.logger.debug("getAbsoluteCurrent: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									currentRmsValueStr = String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("getAbsoluteCurrent: AbsoluteCurrentValue1: "+ currentRmsValueStr);
									return currentRmsValueStr;
									
																		if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpCurrent){
										currentRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
										return currentRmsValueStr;
									}else {
										
										float inpCurrent_X = inpCurrent;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										currentRmsValueStr = GUIUtils.calculateLinearInterpolation(inpCurrent_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										return currentRmsValueStr;
									}
								}else{
									lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();
									currentRmsValueStr = String.valueOf((Integer.valueOf(feedbackCurrentRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("getAbsoluteVoltage: AbsoluteCurrentValue2: "+ currentRmsValueStr);
								}
							}
						}
					}
	
				}
				
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAbsoluteCurrent: Exception: "+e.getMessage());
		}
		
		return currentRmsValueStr;
	}
	
	public static Double manipulateCurrentGainValue(String selectedPhase, String userInputCurrentValue){
		Double currentRmsValueStr = 0.0;//"00000";
		try{
			float inpCurrent = Float.parseFloat(userInputCurrentValue);
			ApplicationLauncher.logger.debug("manipulateCurrentGainValue: inpCurrent: "+ inpCurrent);
			if(inpCurrent <= 0.0f){
				return currentRmsValueStr;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
			List<CurrentCalibration> currentCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
			CurrentCalibration currentCalib = new CurrentCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
				
				if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("manipulateCurrentGainValue: phaseIndex: "+ phaseIndex);
					currentCalib = currentCalibList.get(phaseIndex);
					/*float inpCurrent = Float.parseFloat(inpCurrentValue);
					ApplicationLauncher.logger.debug("manipulateCurrentGainValue: inpCurrent: "+ inpCurrent);*/
					List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
					for(int  tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("manipulateCurrentGainValue: getCurrentTapMax: "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
						if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
							ApplicationLauncher.logger.debug("manipulateCurrentGainValue: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("manipulateCurrentGainValue: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = currentCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpCurrent <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("manipulateCurrentGainValue: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									//ApplicationLauncher.logger.info("getAbsoluteVoltage :powerSourceFeedBackVoltageRms : " + feedbackCurrentRmsValue);
									ApplicationLauncher.logger.debug("manipulateCurrentGainValue: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									//ApplicationLauncher.logger.debug("getAbsoluteVoltage: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									currentRmsValueStr = calibPointList.get(calibPointIndex).getGainValue();//String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("manipulateCurrentGainValue: getGainValue: "+ currentRmsValueStr);
									return currentRmsValueStr;
									
									/*									if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpCurrent){
										currentRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
										return currentRmsValueStr;
									}else {
										
										float inpCurrent_X = inpCurrent;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										currentRmsValueStr = GUIUtils.calculateLinearInterpolation(inpCurrent_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										return currentRmsValueStr;
									}*/
								}else{
									/*lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();*/
									//currentRmsValueStr = String.valueOf((Integer.valueOf(feedbackCurrentRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									//ApplicationLauncher.logger.debug("getAbsoluteVoltage: AbsoluteCurrentValue2: "+ currentRmsValueStr);
									currentRmsValueStr = calibPointList.get(calibPointIndex).getGainValue();//String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("getAbsoluteVoltage: getGainValue2: "+ currentRmsValueStr);
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateCurrentGainValue: Exception: "+e.getMessage());
		}
		
		return currentRmsValueStr;
	}
	
	
	public static Double manipulateCurrentOffsetValue(String selectedPhase, String userInputCurrentValue){
		Double currentRmsValueStr = 0.0;//"00000";
		try{
			float inpCurrent = Float.parseFloat(userInputCurrentValue);
			ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: inpCurrent: "+ inpCurrent);
			if(inpCurrent <= 0.0f){
				return currentRmsValueStr;
			}
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
			List<CurrentCalibration> currentCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
			CurrentCalibration currentCalib = new CurrentCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
				
				if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
					ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: phaseIndex: "+ phaseIndex);
					currentCalib = currentCalibList.get(phaseIndex);
					/*float inpCurrent = Float.parseFloat(inpCurrentValue);
					ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: inpCurrent: "+ inpCurrent);*/
					List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
					for(int  tapIndex = 0; tapIndex< currentCalib.getCurrentTap().size(); tapIndex++){		
						ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: getCurrentTapMax: "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
						if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
							ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: phaseIndex2: "+ phaseIndex);
							ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: tapIndex: "+ tapIndex);
							List<CalibPoints> calibPointList = currentCalibTapList.get(tapIndex).getCalibPoints();
							for(int  calibPointIndex = 0; calibPointIndex < calibPointList.size(); calibPointIndex++){
								if(inpCurrent <= calibPointList.get(calibPointIndex).getCalibPointValue() ){
									ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: getCalibPointValue: "+ calibPointList.get(calibPointIndex).getCalibPointValue());
									//ApplicationLauncher.logger.info("getAbsoluteVoltage :powerSourceFeedBackVoltageRms : " + feedbackCurrentRmsValue);
									//ApplicationLauncher.logger.debug("getAbsoluteVoltage: getOffsetValue: "+ calibPointList.get(calibPointIndex).getOffsetValue());
									ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: getGainValue: "+ calibPointList.get(calibPointIndex).getGainValue());
									currentRmsValueStr = calibPointList.get(calibPointIndex).getOffsetValue();//String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: getOffsetValue: "+ currentRmsValueStr);
									return currentRmsValueStr;
									
									/*									if(calibPointList.get(calibPointIndex).getCalibPointValue() == inpCurrent){
										currentRmsValueStr = String.valueOf(calibPointList.get(calibPointIndex).getRmsValue());
										return currentRmsValueStr;
									}else {
										
										float inpCurrent_X = inpCurrent;
										float lastReadRmsValue_Y1 = lastReadRmsValue;
										float lastReadCalibPointValue_X1 = lastReadCalibPointValue;
										float lastReadRmsValue_Y2 = calibPointList.get(calibPointIndex).getRmsValue();
										float lastReadCalibPointValue_X2 = calibPointList.get(calibPointIndex).getCalibPointValue();
										currentRmsValueStr = GUIUtils.calculateLinearInterpolation(inpCurrent_X, lastReadRmsValue_Y1, lastReadCalibPointValue_X1,
																lastReadRmsValue_Y2,lastReadCalibPointValue_X2); //y= mx+c //y = y1 + (x-x1)[(y2-y1)/(x2-x1)] // unknown is y
										return currentRmsValueStr;
									}*/
								}else{
									/*lastReadRmsValue = calibPointList.get(calibPointIndex).getRmsValue();
									lastReadCalibPointValue  = calibPointList.get(calibPointIndex).getCalibPointValue();*/
									//currentRmsValueStr = String.valueOf((Integer.valueOf(feedbackCurrentRmsValue) + calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									//ApplicationLauncher.logger.debug("getAbsoluteVoltage: AbsoluteCurrentValue2: "+ currentRmsValueStr);
									currentRmsValueStr = calibPointList.get(calibPointIndex).getOffsetValue();//String.valueOf((Double.valueOf(feedbackCurrentRmsValue) - calibPointList.get(calibPointIndex).getOffsetValue())/calibPointList.get(calibPointIndex).getGainValue() );
									ApplicationLauncher.logger.debug("manipulateCurrentOffsetValue: getOffsetValue2: "+ currentRmsValueStr);
								}
							}
						}
					}
	
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateCurrentOffsetValue: Exception: "+e.getMessage());
		}
		
		return currentRmsValueStr;
	}

	
	public static String getTargetCurrentRelayId(String selectedPhase, String inpCurrentValue){
		ApplicationLauncher.logger.debug("getTargetCurrentRelayId: Entry: selectedPhase: " + selectedPhase);
		String currentRelayId = "000";
		
		try{
			int calibSize = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration().size();
			List<CurrentCalibration> currentCalibList = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getCurrentCalibration();
			CurrentCalibration currentCalib = new CurrentCalibration();
			float lastReadRmsValue = 0.0f;
			float lastReadCalibPointValue = 0.0f;
			for(int phaseIndex=0; phaseIndex< currentCalibList.size() ; phaseIndex++){
				ApplicationLauncher.logger.debug("getTargetCurrentRelayId: phaseIndex: " + phaseIndex);
				try{
					if(currentCalibList.get(phaseIndex).getCurrentPhase().equals(selectedPhase)){
						ApplicationLauncher.logger.debug("getTargetCurrentRelayId: phaseIndex: "+ phaseIndex);
						ApplicationLauncher.logger.debug("getTargetCurrentRelayId: getCurrentPhase Name: "+ currentCalibList.get(phaseIndex).getCurrentPhase());
						currentCalib = currentCalibList.get(phaseIndex);
						float inpCurrent = Float.parseFloat(inpCurrentValue);
						ApplicationLauncher.logger.debug("getTargetCurrentRelayId: inpCurrent: "+ inpCurrent);
						List<CurrentTap> currentCalibTapList = currentCalib.getCurrentTap();
						for(int  tapIndex = 0; tapIndex< currentCalibTapList.size(); tapIndex++){		
							ApplicationLauncher.logger.debug("getTargetCurrentRelayId: getCurrentTapMax: "+ currentCalibTapList.get(tapIndex).getCurrentTapMax());
							try{
								if(inpCurrent <= currentCalibTapList.get(tapIndex).getCurrentTapMax()){
									ApplicationLauncher.logger.debug("getTargetCurrentRelayId: phaseIndex2: "+ phaseIndex);
									ApplicationLauncher.logger.debug("getTargetCurrentRelayId: tapIndex: "+ tapIndex);
									currentRelayId = currentCalibTapList.get(tapIndex).getCurrentRelayId();
									ApplicationLauncher.logger.debug("getTargetCurrentRelayId: currentRelayId: "+ currentRelayId);
									return currentRelayId;
			
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								ApplicationLauncher.logger.error("getTargetCurrentRelayId: Exception3: "+e1.getMessage());
							}
						}
		
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ApplicationLauncher.logger.error("getTargetCurrentRelayId: Exception2: "+e1.getMessage());
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getTargetCurrentRelayId: Exception: "+e.getMessage());
		}
		
		return currentRelayId;
	}


	public static String getSourceCurrentR_PhaseTapSelection() {
		return sourceCurrentR_PhaseTapSelection;
	}


	public static void setSourceCurrentR_PhaseTapSelection(String sourceCurrentR_PhaseTapSelection) {
		DeviceDataManagerController.sourceCurrentR_PhaseTapSelection = sourceCurrentR_PhaseTapSelection;
	}


	public static String getR_PhaseOutputVoltageRms() {
		return R_PhaseOutputVoltageRms;
	}


	public static void setR_PhaseOutputVoltageRms(String r_PhaseOutputVoltageRms) {
		R_PhaseOutputVoltageRms = r_PhaseOutputVoltageRms;
	}


	public static String getY_PhaseOutputVoltageRms() {
		return Y_PhaseOutputVoltageRms;
	}


	public static void setY_PhaseOutputVoltageRms(String y_PhaseOutputVoltageRms) {
		Y_PhaseOutputVoltageRms = y_PhaseOutputVoltageRms;
	}


	public static String getB_PhaseOutputVoltageRms() {
		return B_PhaseOutputVoltageRms;
	}


	public static void setB_PhaseOutputVoltageRms(String b_PhaseOutputVoltageRms) {
		B_PhaseOutputVoltageRms = b_PhaseOutputVoltageRms;
	}


	public static String getR_PhaseOutputCurrentRms() {
		return R_PhaseOutputCurrentRms;
	}


	public static void setR_PhaseOutputCurrentRms(String r_PhaseOutputCurrentRms) {
		R_PhaseOutputCurrentRms = r_PhaseOutputCurrentRms;
	}


	public static String getY_PhaseOutputCurrentRms() {
		return Y_PhaseOutputCurrentRms;
	}


	public static void setY_PhaseOutputCurrentRms(String y_PhaseOutputCurrentRms) {
		Y_PhaseOutputCurrentRms = y_PhaseOutputCurrentRms;
	}


	public static String getB_PhaseOutputCurrentRms() {
		return B_PhaseOutputCurrentRms;
	}


	public static void setB_PhaseOutputCurrentRms(String b_PhaseOutputCurrentRms) {
		B_PhaseOutputCurrentRms = b_PhaseOutputCurrentRms;
	}


	public static String getSandsRefStdLastSetVoltageMode() {
		return sandsRefStdLastSetVoltageMode;
	}


	public static void setSandsRefStdLastSetVoltageMode(String sandsRefStdLastSetVoltageMode) {
		DeviceDataManagerController.sandsRefStdLastSetVoltageMode = sandsRefStdLastSetVoltageMode;
	}


	public static String getSandsRefStdLastSetCurrentMode() {
		return sandsRefStdLastSetCurrentMode;
	}


	public static void setSandsRefStdLastSetCurrentMode(String sandsRefStdLastSetCurrentMode) {
		DeviceDataManagerController.sandsRefStdLastSetCurrentMode = sandsRefStdLastSetCurrentMode;
	}


	public static String getSandsRefStdLastSetPulseOutputMode() {
		return sandsRefStdLastSetPulseOutputMode;
	}


	public static void setSandsRefStdLastSetPulseOutputMode(String sandsRefStdLastSetPulseOutputMode) {
		DeviceDataManagerController.sandsRefStdLastSetPulseOutputMode = sandsRefStdLastSetPulseOutputMode;
	}


	public static boolean isIctReadData() {
		return ictReadData;
	}


	public static void setIctReadData(boolean ictReadData) {
		DeviceDataManagerController.ictReadData = ictReadData;
	}


	public static int getAverageNoOfLduReadingRequired() {
		return averageNoOfLduReadingRequired;
	}


	public static void setAverageNoOfLduReadingRequired(int averageNoOfLduReadingRequired) {
		DeviceDataManagerController.averageNoOfLduReadingRequired = averageNoOfLduReadingRequired;
	}


	
	
	
    public void addLduErrorDataHashMap2d(Integer lduAddressOuterKey, Integer readingIndexInnerKey, String errorValue) {
		HashMap<Integer, String> errorDataInnerMap=lduErrorDataHashMap2d.get(lduAddressOuterKey);
        if (errorDataInnerMap==null) {
            errorDataInnerMap = new HashMap<Integer,String>();
            lduErrorDataHashMap2d.put(lduAddressOuterKey,errorDataInnerMap);
        }
        errorDataInnerMap.put(readingIndexInnerKey,errorValue);
    }

    public String getLduErrorDataHashMap2d(Integer lduAddressOuterKey, Integer readingIndexInnerKey) {
    	HashMap<Integer, String> errorDataInnerMap = lduErrorDataHashMap2d.get(lduAddressOuterKey);
        if (errorDataInnerMap==null) {
            return null;
        }
        
        return errorDataInnerMap.get(readingIndexInnerKey);
    }
    
    public String getAverageLduErrorDataHashMap2d(Integer lduAddressOuterKey) {
    	HashMap<Integer, String> errorDataInnerMap = lduErrorDataHashMap2d.get(lduAddressOuterKey);
        if (errorDataInnerMap==null) {
            return null;
        }
        
        Collection<String> errorDataList = errorDataInnerMap.values();
/*        for(int i = 0; i < errorDataList.size(); i++ ){
        	ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : errorDataList[0]; "  + errorDataList.iterator().next());
        	//if(errorDataList[0])
        }*/
        ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : errorDataList: size : "  + errorDataList.size());
        Float errorValue = 0.0f;
        Float averageValue =  0.0f;
        String averageValueStr = "";
        String data = "";
        for (Iterator i = errorDataList.iterator(); i.hasNext(); ) {
            //System.out.println(i.next());
        	data = (String)i.next();
        	ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : errorDataList: "  + data);
        	try{
        		
        	
        		Float.parseFloat(data);// added for exception
        		errorValue = errorValue + Float.parseFloat(data);
        		ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : sum of errorValue: "  + errorValue);

        	
        	}catch(Exception E){
				E.printStackTrace();
				ApplicationLauncher.logger.error("getAverageLduErrorDataHashMap2d Exception :"+E.getMessage());
			}
        	
        }
        if (errorDataList.size()>0){
        	averageValue = errorValue /errorDataList.size();
        	ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValue: "  + averageValue);
        }
        if(averageValue >= 0.0f){
        	averageValueStr = "+" + String.format("%.3f", averageValue);
        	ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValueStr1: "  + averageValueStr);
        }else  if(averageValue < 0.0f){
        	averageValueStr =  String.format("%.3f", averageValue);
        	ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValueStr2: "  + averageValueStr);
        }
        return averageValueStr;
        //return errorDataInnerMap.values();
        
        //return errorDataInnerMap.get(readingIndexInnerKey);
    }
    
    public void resetLduErrorDataHashMap2d() {
    	lduErrorDataHashMap2d = new HashMap<Integer,HashMap<Integer, String>>();
    }


	public static Custom1ReportConfigModel getCustom1ReportConfigParsedKey() {
		return custom1ReportConfigParsedKey;
	}


	public static void setCustom1ReportConfigParsedKey(Custom1ReportConfigModel custom1ReportConfigParsedKey) {
		DeviceDataManagerController.custom1ReportConfigParsedKey = custom1ReportConfigParsedKey;
	}
	

	
	
	public static void setDUT1_ReadDataFlag(boolean value) {
		bDUT1_ReadData = value;
	}


	public static boolean getDUT1_ReadDataFlag() {
		return bDUT1_ReadData;
	}
	
	public static void setDUT2_ReadDataFlag(boolean value) {
		bDUT2_ReadData = value;
	}


	public static boolean getDUT2_ReadDataFlag() {
		return bDUT2_ReadData;
	}
	
	public static void setDUT3_ReadDataFlag(boolean value) {
		bDUT3_ReadData = value;
	}


	public static boolean getDUT3_ReadDataFlag() {
		return bDUT3_ReadData;
	}
	
	public static void setDUT4_ReadDataFlag(boolean value) {
		bDUT4_ReadData = value;
	}


	public static boolean getDUT4_ReadDataFlag() {
		return bDUT4_ReadData;
	}
	
	public static void setDUT5_ReadDataFlag(boolean value) {
		bDUT5_ReadData = value;
	}


	public static boolean getDUT5_ReadDataFlag() {
		return bDUT5_ReadData;
	}
	
	public static void setDUT6_ReadDataFlag(boolean value) {
		bDUT6_ReadData = value;
	}


	public static boolean getDUT6_ReadDataFlag() {
		return bDUT6_ReadData;
	}
	
	public static void setDUT7_ReadDataFlag(boolean value) {
		bDUT7_ReadData = value;
	}


	public static boolean getDUT7_ReadDataFlag() {
		return bDUT7_ReadData;
	}
	


	public static void setDUT8_ReadDataFlag(boolean value) {
		bDUT8_ReadData = value;
	}
	
	public static boolean getDUT8_ReadDataFlag() {
		return bDUT8_ReadData;
	}
	
	
				
				
				/**********9***/
				
	public static void setDUT9_ReadDataFlag(boolean value) {
		bDUT9_ReadData = value;
	}
	
	public static boolean getDUT9_ReadDataFlag() {
		return bDUT9_ReadData;
	}
	
	
				
				
				/**********10***/
				
	public static void setDUT10_ReadDataFlag(boolean value) {
		bDUT10_ReadData = value;
	}
	
	public static boolean getDUT10_ReadDataFlag() {
		return bDUT10_ReadData;
	}
	
	
				
				
				/**********11***/
				
		public static void setDUT11_ReadDataFlag(boolean value) {
		bDUT11_ReadData = value;
	}
	
	public static boolean getDUT11_ReadDataFlag() {
		return bDUT11_ReadData;
	}
	
	
				
				
				/**********11***/
				
	public static void setDUT12_ReadDataFlag(boolean value) {
		bDUT12_ReadData = value;
	}
	
	public static boolean getDUT12_ReadDataFlag() {
		return bDUT12_ReadData;
	}
	
	
				
				
				/**********13***/
				
	public static void setDUT13_ReadDataFlag(boolean value) {
		bDUT13_ReadData = value;
	}
	
	public static boolean getDUT13_ReadDataFlag() {
		return bDUT13_ReadData;
	}
	
	
				
				
				/**********13***/
				
	public static void setDUT14_ReadDataFlag(boolean value) {
		bDUT14_ReadData = value;
	}
	
	public static boolean getDUT14_ReadDataFlag() {
		return bDUT14_ReadData;
	}
	
	
				
				
				/**********14***/
				
		public static void setDUT15_ReadDataFlag(boolean value) {
		bDUT15_ReadData = value;
	}
	
	public static boolean getDUT15_ReadDataFlag() {
		return bDUT15_ReadData;
	}
	
	
				
				
				/**********16***/

	public static void setDUT16_ReadDataFlag(boolean value) {
		bDUT16_ReadData = value;
	}
	
	public static boolean getDUT16_ReadDataFlag() {
		return bDUT16_ReadData;
	}
	
	
				
				
				/**********16***/
				
	public static void setDUT17_ReadDataFlag(boolean value) {
		bDUT17_ReadData = value;
	}
	
	public static boolean getDUT17_ReadDataFlag() {
		return bDUT17_ReadData;
	}
	
	
				
				
				/**********18***/
				
	public static void setDUT18_ReadDataFlag(boolean value) {
		bDUT18_ReadData = value;
	}
	
	public static boolean getDUT18_ReadDataFlag() {
		return bDUT18_ReadData;
	}
	
	
				
				
				/**********19***/
				
		public static void setDUT19_ReadDataFlag(boolean value) {
		bDUT19_ReadData = value;
	}
	
	public static boolean getDUT19_ReadDataFlag() {
		return bDUT19_ReadData;
	}
	
	
				
				
				/**********20***/
				
	public static void setDUT20_ReadDataFlag(boolean value) {
		bDUT20_ReadData = value;
	}
	
	public static boolean getDUT20_ReadDataFlag() {
		return bDUT20_ReadData;
	}
	

				
				/**********21***/
				
	public static void setDUT21_ReadDataFlag(boolean value) {
		bDUT21_ReadData = value;
	}
	
	public static boolean getDUT21_ReadDataFlag() {
		return bDUT21_ReadData;
	}
	

				
				/**********22***/
				
	public static void setDUT22_ReadDataFlag(boolean value) {
		bDUT22_ReadData = value;
	}
	
	public static boolean getDUT22_ReadDataFlag() {
		return bDUT22_ReadData;
	}
	

				
				/**********23***/
				
		public static void setDUT23_ReadDataFlag(boolean value) {
		bDUT23_ReadData = value;
	}
	
	public static boolean getDUT23_ReadDataFlag() {
		return bDUT23_ReadData;
	}
	
				
				
				/**********24***/
				


	public static void setDUT24_ReadDataFlag(boolean value) {
		bDUT24_ReadData = value;
	}
	
	public static boolean getDUT24_ReadDataFlag() {
		return bDUT24_ReadData;
	}
	
	

				
				/**********25***/
				
	public static void setDUT25_ReadDataFlag(boolean value) {
		bDUT25_ReadData = value;
	}
	
	public static boolean getDUT25_ReadDataFlag() {
		return bDUT25_ReadData;
	}
	
				
				
				/**********26***/
				
	public static void setDUT26_ReadDataFlag(boolean value) {
		bDUT26_ReadData = value;
	}
	
	public static boolean getDUT26_ReadDataFlag() {
		return bDUT26_ReadData;
	}

				
				/**********27***/
				
		public static void setDUT27_ReadDataFlag(boolean value) {
		bDUT27_ReadData = value;
	}
	
	public static boolean getDUT27_ReadDataFlag() {
		return bDUT27_ReadData;
	}
	
		

				
				/**********28***/
				
	public static void setDUT28_ReadDataFlag(boolean value) {
		bDUT28_ReadData = value;
	}
	
	public static boolean getDUT28_ReadDataFlag() {
		return bDUT28_ReadData;
	}
	

				
				/**********29***/
				
	public static void setDUT29_ReadDataFlag(boolean value) {
		bDUT29_ReadData = value;
	}
	
	public static boolean getDUT29_ReadDataFlag() {
		return bDUT29_ReadData;
	}
	
	
				
				
				/**********30***/
				
	public static void setDUT30_ReadDataFlag(boolean value) {
		bDUT30_ReadData = value;
	}
	
	public static boolean getDUT30_ReadDataFlag() {
		return bDUT30_ReadData;
	}
	

				
				/**********30***/
				
		public static void setDUT31_ReadDataFlag(boolean value) {
		bDUT31_ReadData = value;
	}
	
	public static boolean getDUT31_ReadDataFlag() {
		return bDUT31_ReadData;
	}
	

				
				/**********32***/

	public static void setDUT32_ReadDataFlag(boolean value) {
		bDUT32_ReadData = value;
	}
	
	public static boolean getDUT32_ReadDataFlag() {
		return bDUT32_ReadData;
	}
	
			
				
				/**********33***/
				
	public static void setDUT33_ReadDataFlag(boolean value) {
		bDUT33_ReadData = value;
	}
	
	public static boolean getDUT33_ReadDataFlag() {
		return bDUT33_ReadData;
	}
	
	
				
				/**********34***/
				
	public static void setDUT34_ReadDataFlag(boolean value) {
		bDUT34_ReadData = value;
	}
	
	public static boolean getDUT34_ReadDataFlag() {
		return bDUT34_ReadData;
	}
	
	
				
				/**********35***/
				
		public static void setDUT35_ReadDataFlag(boolean value) {
		bDUT35_ReadData = value;
	}
	
	public static boolean getDUT35_ReadDataFlag() {
		return bDUT35_ReadData;
	}
	
	
				
				
				/**********36***/
				
	public static void setDUT36_ReadDataFlag(boolean value) {
		bDUT36_ReadData = value;
	}
	
	public static boolean getDUT36_ReadDataFlag() {
		return bDUT36_ReadData;
	}
	
	
				
				/**********37***/
				
	public static void setDUT37_ReadDataFlag(boolean value) {
		bDUT37_ReadData = value;
	}
	
	public static boolean getDUT37_ReadDataFlag() {
		return bDUT37_ReadData;
	}
	
	
				
				/**********38***/
				
	public static void setDUT38_ReadDataFlag(boolean value) {
		bDUT38_ReadData = value;
	}
	
	public static boolean getDUT38_ReadDataFlag() {
		return bDUT38_ReadData;
	}
	

				
				/**********39***/
				
		public static void setDUT39_ReadDataFlag(boolean value) {
		bDUT39_ReadData = value;
	}
	
	public static boolean getDUT39_ReadDataFlag() {
		return bDUT39_ReadData;
	}
	

				
				/**********40***/
				


	public static void setDUT40_ReadDataFlag(boolean value) {
		bDUT40_ReadData = value;
	}
	
	public static boolean getDUT40_ReadDataFlag() {
		return bDUT40_ReadData;
	}
	

				
				/**********41***/
				
	public static void setDUT41_ReadDataFlag(boolean value) {
		bDUT41_ReadData = value;
	}
	
	public static boolean getDUT41_ReadDataFlag() {
		return bDUT41_ReadData;
	}
	
				
				
				/**********42***/
				
	public static void setDUT42_ReadDataFlag(boolean value) {
		bDUT42_ReadData = value;
	}
	
	public static boolean getDUT42_ReadDataFlag() {
		return bDUT42_ReadData;
	}
	
	
				
				
				/**********43***/
				
		public static void setDUT43_ReadDataFlag(boolean value) {
		bDUT43_ReadData = value;
	}
	
	public static boolean getDUT43_ReadDataFlag() {
		return bDUT43_ReadData;
	}
	
	
				
				/**********43***/
				
	public static void setDUT44_ReadDataFlag(boolean value) {
		bDUT44_ReadData = value;
	}
	
	public static boolean getDUT44_ReadDataFlag() {
		return bDUT44_ReadData;
	}
	
	
				
				/**********45***/
				
	public static void setDUT45_ReadDataFlag(boolean value) {
		bDUT45_ReadData = value;
	}
	
	public static boolean getDUT45_ReadDataFlag() {
		return bDUT45_ReadData;
	}
	

				
				/**********46***/
				
	public static void setDUT46_ReadDataFlag(boolean value) {
		bDUT46_ReadData = value;
	}
	
	public static boolean getDUT46_ReadDataFlag() {
		return bDUT46_ReadData;
	}
	
		
				
				/**********47***/
				
		public static void setDUT47_ReadDataFlag(boolean value) {
		bDUT47_ReadData = value;
	}
	
	public static boolean getDUT47_ReadDataFlag() {
		return bDUT47_ReadData;
	}
	
	
				
				/**********48***/

	public static void setDUT48_ReadDataFlag(boolean value) {
		bDUT48_ReadData = value;
	}
	
	public static boolean getDUT48_ReadDataFlag() {
		return bDUT48_ReadData;
	}
	
	public static ArrayList<UacDataModel> getUacSelectProfileScreenList() {
		return uacSelectProfileScreenList;
	}
	public static void setUacSelectProfileScreenList(ArrayList<UacDataModel> uacSelectProfileScreenList) {
		DeviceDataManagerController.uacSelectProfileScreenList = uacSelectProfileScreenList;
	}


/*	public boolean isMeterIdExistInBlackList(int lduAddress){
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: Entry"  );
		if(ConstantAppConfig.METER_ID_BLACKLIST_VALIDATION){
			if (ConstantAppConfig.METER_ID_BLACKLISTED_LIST.size()>0){
				String readMeterId = getDutSerialNumber(lduAddress);
				ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: readMeterId: " + readMeterId  );

				if(ConstantAppConfig.METER_ID_BLACKLISTED_LIST.contains(readMeterId)){
					ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: meter id found in black list: " + readMeterId  );
					status = true;
					return status;
				}
			}
		
		}
		
		return status;
	}*/
	
	public static String getDutSerialNumberMap(int lduAddress) {
		String serialNo = "";
		try{
			if(dutSerialNumberMap.containsKey(lduAddress)){
				serialNo = dutSerialNumberMap.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDutSerialNumber : Exception :"+ e.getMessage());
		}
		return serialNo;
	}
	public boolean isMeterIdExistInBlackList(int lduAddress){
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: Entry"  );
		if(ConstantAppConfig.METER_ID_BLACKLIST_VALIDATION){
			if (ConstantAppConfig.METER_ID_BLACKLISTED_LIST.size()>0){
				String readMeterId = getDutSerialNumberMap(lduAddress);
				ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: readMeterId: " + readMeterId  );
/*				for(int i=0; i< ConstantConfig.TOTAL_METER_ID_BLACKLISTED ; i++){
					ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: ConstantConfig.METER_ID_BLACKLISTED_LIST.get(i): " + ConstantConfig.METER_ID_BLACKLISTED_LIST.get(i)  );
					if(ConstantConfig.METER_ID_BLACKLISTED_LIST.get(i).equals((readMeterId))){
						
						ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: meter id found in black list: " + readMeterId  );
						status = true;
						return status;
						
					}
				}*/		
				if(ConstantAppConfig.METER_ID_BLACKLISTED_LIST.contains(readMeterId)){
					ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: meter id found in black list: " + readMeterId  );
					status = true;
					return status;
				}
			}
		
		}
		
		return status;
	}
	
	public boolean isMeterIdExistInWhiteList(int lduAddress){
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: Entry"  );
		if(ConstantAppConfig.METER_ID_WHITELIST_VALIDATION){
			if (ConstantAppConfig.METER_ID_WHITELISTED_LIST.size()>0){
				String readMeterId = getDutSerialNumberMap(lduAddress);
				ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: readMeterId: " + readMeterId  );
/*				for(int i=0; i< ConstantConfig.TOTAL_METER_ID_WHITELISTED ; i++){
					ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: ConstantConfig.METER_ID_WHITELISTED_LIST.get(i): " + ConstantConfig.METER_ID_WHITELISTED_LIST.get(i)  );
					if(ConstantConfig.METER_ID_WHITELISTED_LIST.get(i).equals((readMeterId))){
						
						ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: meter id found in black list: " + readMeterId  );
						status = true;
						return status;
						
					}
				}	*/	
				
				if(ConstantAppConfig.METER_ID_WHITELISTED_LIST.contains(readMeterId)){
					ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: meter id found in black list: " + readMeterId  );
					status = true;
					return status;
				}
			}
		
		}
		
		return status;
	}
	
	public boolean isMeterIdExistInAlreadyTestedList(int lduAddress){
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInAlreadyTestedList: Entry"  );
		if(ConstantAppConfig.METER_ID_VALIDATE_ALREADY_TESTED){
				String readMeterId = getDutSerialNumberMap(lduAddress);
				status = MySQL_Controller.sp_ValidateDutAlreadyTested(  readMeterId, ConstantReport.RESULT_DATA_TYPE_DEVICE_NAME);
				if (status){
					ApplicationLauncher.logger.debug("isMeterIdExistInAlreadyTestedList: meter id found in calibrated list: " + readMeterId  );
					return status;
				}
		}
		
		return status;
	}
	
	public boolean isDutBlackListedOrAlreadyTested(Iterator<DeploymentDataModel> itr){
		boolean status = false;
		//clearDutSerialNumber();
		clearDutSerialNumberMap();
		String dutSerialNo = "";
		int lduAddress = 0;
		ArrayList<Integer> selectedLduAddressList = new ArrayList<Integer>();
		
		try{
			while (itr.hasNext()) {
				DeploymentDataModel dataElement = itr.next();

				if (dataElement.getIsSelected()) {
					dutSerialNo = dataElement.getSerialno();
					lduAddress = Integer.parseInt(dataElement.getrackid());
					selectedLduAddressList.add(lduAddress);
					setDutSerialNumberMap(dutSerialNo,lduAddress);

				}

			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("isDutBlackListedOrAlreadyTested: Exception: "+e.getMessage());
		}
		boolean blackListedMeterIdFoundOverAllStatus = false;
		boolean meterIdAlreadyCalibratedFoundOverAllStatus = false;
		boolean emptyMeterIdFoundOverAllStatus = false;
		ArrayList<String> blackListedMeterIdFoundList  = new ArrayList<String>();
		ArrayList<String> meterIdAlreadyCalibratedFoundList  = new ArrayList<String>();
		ArrayList<String> emptyMeterIdFoundList  = new ArrayList<String>();
		
		for(int i = 0 ;i<selectedLduAddressList.size();i++){
			lduAddress = selectedLduAddressList.get(i);
			ApplicationLauncher.logger.debug("isDutBlackListedOrAlreadyTested : lduAddress : " + lduAddress);
			boolean blackListedMeterIdFound =isMeterIdExistInBlackList(lduAddress);
			if(blackListedMeterIdFound){
				blackListedMeterIdFoundList.add("Rack Position-" + lduAddress + " : "+getDutSerialNumberMap(lduAddress));
				blackListedMeterIdFoundOverAllStatus = true;

			}
			boolean whiteListedMeterIdFound =isMeterIdExistInWhiteList(lduAddress);
			if(!whiteListedMeterIdFound){
				boolean meterIdAlreadyTestedFound = isMeterIdExistInAlreadyTestedList(lduAddress);
				if(meterIdAlreadyTestedFound){
					meterIdAlreadyCalibratedFoundList.add("Rack Position-" + lduAddress + " : "+ getDutSerialNumberMap(lduAddress));
					meterIdAlreadyCalibratedFoundOverAllStatus = true;
	
				}
			}
			if(ConstantAppConfig.METER_ID_VALIDATE_FOR_EMPTY){
				String readMeterId = getDutSerialNumberMap(lduAddress);
				if(readMeterId.isEmpty()){
					emptyMeterIdFoundOverAllStatus = true;
					emptyMeterIdFoundList.add("Rack Position-" + lduAddress );//+ " : "+String.valueOf(lduAddress));
				}
			}
		}
		
		if((blackListedMeterIdFoundOverAllStatus) || (meterIdAlreadyCalibratedFoundOverAllStatus) || 
				(emptyMeterIdFoundOverAllStatus)){
			String meterIdList = "";
			if(blackListedMeterIdFoundOverAllStatus) {
				status = true;
				for (int i = 0; i < blackListedMeterIdFoundList.size(); i++) {
					
					meterIdList = meterIdList + "\n"+ blackListedMeterIdFoundList.get(i);
				}
				ApplicationLauncher.logger.info("isDutBlackListedOrAlreadyTested: invoking exit process due to black listed meter id found");
				ApplicationLauncher.logger.debug("isDutBlackListedOrAlreadyTested: ERROR_CODE_5001 :" + ErrorCodeMapping.ERROR_CODE_5001_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
				ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_5001, ErrorCodeMapping.ERROR_CODE_5001_MSG + "\n\nMeter Id: " + meterIdList,AlertType.ERROR);
				ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution: Meter id -black list",ConstantApp.LEFT_STATUS_INFO);
				
			}
			

			
			if(meterIdAlreadyCalibratedFoundOverAllStatus) {
				status = true;
				meterIdList = "";
				for (int i = 0; i < meterIdAlreadyCalibratedFoundList.size(); i++) {
					
					meterIdList = meterIdList + "\n"+ meterIdAlreadyCalibratedFoundList.get(i);
				}
				ApplicationLauncher.logger.info("isDutBlackListedOrAlreadyTested: invoking exit process due to meter id found already calibrated");
				ApplicationLauncher.logger.debug("isDutBlackListedOrAlreadyTested: ERROR_CODE_5002 :" + ErrorCodeMapping.ERROR_CODE_5002_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
				ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_5002, ErrorCodeMapping.ERROR_CODE_5002_MSG + "\n\nMeter Id: " + meterIdList,AlertType.ERROR);
				ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution: Meter id -Already calibrated List",ConstantApp.LEFT_STATUS_INFO);
				
			}
			if(ConstantAppConfig.METER_ID_VALIDATE_FOR_EMPTY){
				if(emptyMeterIdFoundOverAllStatus) {
					status = true;
					meterIdList = "";
					for (int i = 0; i < emptyMeterIdFoundList.size(); i++) {
						
						meterIdList = meterIdList + "\n"+ emptyMeterIdFoundList.get(i);
					}
					ApplicationLauncher.logger.info("isDutBlackListedOrAlreadyTested: invoking exit process due to meter id found empty");
					ApplicationLauncher.logger.debug("isDutBlackListedOrAlreadyTested: ERROR_CODE_5003 :" + ErrorCodeMapping.ERROR_CODE_5003_MSG + "\n\nMeter Id: " + meterIdList + " : Prompted");
					ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_5003, ErrorCodeMapping.ERROR_CODE_5003_MSG + "\n\nMeter Id: " + meterIdList,AlertType.ERROR);
					ApplicationHomeController.updateBottomSecondaryStatus("Aborting execution: Empty Meter id or error in reading meter id found",ConstantApp.LEFT_STATUS_INFO);
					
				}
			}
		}
		
		
		return status;
	}
	
/*	public static String getDutSerialNumber(int lduAddress) {
		String serialNo = "";
		try{
			if(dutSerialNumber.containsKey(lduAddress)){
				serialNo = dutSerialNumber.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDutSerialNumber : Exception :"+ e.getMessage());
		}
		return serialNo;
	}*/
/*	public static void setDutSerialNumber( String dutSerialNo, int lduAddress) {
		
		ApplicationLauncher.logger.debug("setDutSerialNumber : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("setDutSerialNumber : dutSerialNo: " + dutSerialNo);
		DeviceDataManagerController.dutSerialNumberMap.put(lduAddress, dutSerialNo);
		

	}*/
	
/*	public static void clearDutSerialNumber() {
		dutSerialNumber.clear();
	}*/


	public static String getLastSetPowerSourceFrequency() {
		return lastSetPowerSourceFrequency;
	}


	public static void setLastSetPowerSourceFrequency(String lastSetPowerSourceFreq) {
		ApplicationLauncher.logger.debug("setLastSetPowerSourceFrequency: lastSetPowerSourceFreq: " + lastSetPowerSourceFreq);
		DeviceDataManagerController.lastSetPowerSourceFrequency = lastSetPowerSourceFreq;
	}
	
	public static void resetLastSetPowerSourceFrequency() {
		ApplicationLauncher.logger.debug("resetLastSetPowerSourceFrequency: ");
		DeviceDataManagerController.lastSetPowerSourceFrequency = "40.0";
	}

	public static boolean isPowerSrcReadData() {
		return powerSrcReadData;
	}


	public static void setPowerSrcReadData(boolean powerSrcReadData) {
		DeviceDataManagerController.powerSrcReadData = powerSrcReadData;
	}
	
	public static boolean isPowerSourcePortInitSuccess() {
		return powerSourcePortInitSuccess;
	}

	public static void setPowerSourcePortInitSuccess(boolean hvciPortInitSuccess) {
		DeviceDataManagerController.powerSourcePortInitSuccess = hvciPortInitSuccess;
	}
	
	
	public static void scanForPowerSourceSerialCommPortV2() {
		serialPortManagerPwrSrc_V2.scanForSerialCommPort();
	}
	
	public static void scanForRefStdSerialCommPortV2() {
		serialPortManagerRefStd_V2.scanForSerialCommPort();
	}
	
	public boolean powerSource_ComInit() {

		boolean status = serialPortManagerPwrSrc.powerSourceComInit(PowerSrcCommPortID,PwrSrcCommBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	
	public boolean powerSource_ComInitV2() {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(PowerSrcCommPortID,PwrSrcCommBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	
	public boolean powerSource_ComInitV2_1(String selectedComPortId,String selectedBaudRate) {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(selectedComPortId,selectedBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	
/*	public boolean dutCmd_ComInitV2_1(String selectedComPortId,String selectedBaudRate) {

		boolean status = serialPortManagerDutCmd_V2.dutComInitV2(selectedComPortId,selectedBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}*/
	
	
	public boolean refStd_ComInit() {
	
		boolean status = serialPortManagerRefStd.refStdComInit(RefStdCommPortID,RefStdCommBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	
	
	public boolean refStd_ComInitV2() {
		
		boolean status = serialPortManagerRefStd_V2.refStdComInitV2(RefStdCommPortID,RefStdCommBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	
	public boolean refStd_ComInitV2_1(String selectedComPortId,String selectedBaudRate) {
		
		boolean status = serialPortManagerRefStd_V2.refStdComInitV2(selectedComPortId,selectedBaudRate);//HVCI_CommPortID, HVCI_CommBaudRate);
		return status;
	}
	public void refStdEnableSerialMonitoring() {
		
		serialPortManagerRefStd.startSerialRxPhysical_RefStd();
		serialPortManagerRefStd.enableSerialRxPhysical_RefStdMonitor();
		
	}
	
	
	public void refStdEnableSerialMonitoring_V2() {
		ApplicationLauncher.logger.debug("refStdEnableSerialMonitoring_V2: Entry" );
		serialPortManagerRefStd_V2.startSerialRxPhysical_RefStd();
		serialPortManagerRefStd_V2.enableSerialRxPhysical_RefStdMonitor();
		
	}
	
	public void pwrSrcEnableSerialMonitoring_V2() {
		
		serialPortManagerPwrSrc_V2.startSerialRxPhysical_PwrSrc();
		serialPortManagerPwrSrc_V2.enableSerialRxPhysical_PwrSrcMonitor();
		
	}
	
/*	public void dutCmdEnableSerialMonitoring_V2() {
		
		serialPortManagerDutCmd_V2.startSerialRxPhysical_Dut();
		serialPortManagerDutCmd_V2.enableSerialRxPhysical_DutMonitor();
		
	}*/
	
	public void disconnectRefStdSerialCommIfConnected() {
		
		serialPortManagerRefStd_V2.disconnectRefStdSerialCommIfConnected();
		
	}
	
	
	public void refStdDisconnectPort() {
		
		serialPortManagerRefStd.disconnectRefStd();
		
	}
	
	
	public void refStdDisconnectPort_V2() {
		
		serialPortManagerRefStd_V2.disconnectRefStd();
		
	}
	
	public void pwrSrcDisconnectPort_V2() {
		
		serialPortManagerPwrSrc_V2.disconnectPwrSrc();
		
	}
	
/*	public void dutCmdDisconnectPort_V2() {
		
		serialPortManagerDutCmd_V2.disconnectDut();
		
	}
	*/
	
	
	
	public boolean  powerSourcePortAccessible(){
		ApplicationHomeController.update_left_status("PwrSrc COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		
		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setPowerSourcePortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( powerSource_ComInit()) {	
			
			status= true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("powerSourcePortAccessible: Serial port accessable");

		}else {


			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("powerSourcePortAccessible: " + ErrorCodeMapping.ERROR_CODE_900 +": "+ErrorCodeMapping.ERROR_CODE_900_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.ERROR_CODE_900_MSG,AlertType.ERROR);

		}


		return status;

	}
	
	public boolean  refStdPortAccessible(){
		ApplicationHomeController.update_left_status("RefStd COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		
		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setRefStdPortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( refStd_ComInit()) {	
			
			status= true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible: Serial port accessible");

		}else {


			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible:" + ErrorCodeMapping.ERROR_CODE_901 +": "+ErrorCodeMapping.ERROR_CODE_901_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_901,ErrorCodeMapping.ERROR_CODE_901_MSG,AlertType.ERROR);

		}


		return status;

	}

	
	public boolean  refStdPortAccessible_V2(){
		ApplicationHomeController.update_left_status("RefStd COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		
		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setRefStdPortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( refStd_ComInitV2()) {	
			
			status= true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2: Serial port accessible");

		}else {


			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2:" + ErrorCodeMapping.ERROR_CODE_901 +": "+ErrorCodeMapping.ERROR_CODE_901_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_901,ErrorCodeMapping.ERROR_CODE_901_MSG,AlertType.ERROR);

		}


		return status;

	}
	
	public boolean  refStdPortAccessible_V2_1(String selectedComPortId,String selectedBaudRate){
		ApplicationHomeController.update_left_status("RefStd COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);
		
		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setRefStdPortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( refStd_ComInitV2_1(selectedComPortId,selectedBaudRate)) {	
			
			status= true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2_1: Serial port accessible");

		}else {


			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2_1:" + ErrorCodeMapping.ERROR_CODE_901 +": "+ErrorCodeMapping.ERROR_CODE_901_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_901,ErrorCodeMapping.ERROR_CODE_901_MSG,AlertType.ERROR);

		}


		return status;

	}
	public boolean  pwrSrcPortAccessible_V2(){
		ApplicationHomeController.update_left_status("Power Source COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setPowerSourcePortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( powerSource_ComInitV2()) {	
			
			status= true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2: Serial port accessable");

		}else {


			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2: " + ErrorCodeMapping.ERROR_CODE_900 +": "+ErrorCodeMapping.ERROR_CODE_900_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.ERROR_CODE_900_MSG,AlertType.ERROR);

		}


		return status;

	}
	
	public boolean  pwrSrcPortAccessible_V2_1(String selectedComPortId, String selectedBaudRate){
		ApplicationHomeController.update_left_status("Power Source COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		setPowerSourcePortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( powerSource_ComInitV2_1(selectedComPortId,selectedBaudRate)) {	
			
			status= true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: Serial port accessable");

		}else {


			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: " + ErrorCodeMapping.ERROR_CODE_900 +": "+ErrorCodeMapping.ERROR_CODE_900_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.ERROR_CODE_900_MSG,AlertType.ERROR);

		}


		return status;

	}
	
	
/*	public boolean  dutCmdPortAccessible_V2_1(String selectedComPortId, String selectedBaudRate){
		ApplicationHomeController.update_left_status("DutCmd COM port access validation",ConstantApp.LEFT_STATUS_DEBUG);

		//LoadCurrentSerialComSettingFromDB();
		boolean status=false;
		//setPowerSourcePortInitSuccess(false);
		//if( DisplayLDU_Init()) {
		if( dutCmd_ComInitV2_1(selectedComPortId,selectedBaudRate)) {	
			
			status= true;
			//setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("dutCmdPortAccessible_V2_1: Serial port accessable");

		}else {


			//setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("dutCmdPortAccessible_V2_1: " + ErrorCodeMapping.ERROR_CODE_900 +": "+ErrorCodeMapping.ERROR_CODE_900_MSG);
			ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.ERROR_CODE_900_MSG,AlertType.ERROR);

		}


		return status;

	}*/

	public static boolean isPowerSrcReadFeedbackData() {
		return powerSrcReadFeedbackData;
	}


	public static void setPowerSrcReadFeedbackData(boolean powerSrcReadFeedbackData) {
		DeviceDataManagerController.powerSrcReadFeedbackData = powerSrcReadFeedbackData;
	}


	public static Double getR_PhaseFeedBackProcessVoltageGain() {
		return R_PhaseFeedBackProcessVoltageGain;
	}


	public static void setR_PhaseFeedBackProcessVoltageGain(Double r_PhaseFeedBackProcessVoltageGain) {
		R_PhaseFeedBackProcessVoltageGain = r_PhaseFeedBackProcessVoltageGain;
	}


	public static Double getY_PhaseFeedBackProcessVoltageGain() {
		return Y_PhaseFeedBackProcessVoltageGain;
	}


	public static void setY_PhaseFeedBackProcessVoltageGain(Double y_PhaseFeedBackProcessVoltageGain) {
		Y_PhaseFeedBackProcessVoltageGain = y_PhaseFeedBackProcessVoltageGain;
	}


	public static Double getB_PhaseFeedBackProcessVoltageGain() {
		return B_PhaseFeedBackProcessVoltageGain;
	}


	public static void setB_PhaseFeedBackProcessVoltageGain(Double b_PhaseFeedBackProcessVoltageGain) {
		B_PhaseFeedBackProcessVoltageGain = b_PhaseFeedBackProcessVoltageGain;
	}


	public static Double getR_PhaseFeedBackProcessVoltageOffset() {
		return R_PhaseFeedBackProcessVoltageOffset;
	}


	public static void setR_PhaseFeedBackProcessVoltageOffset(Double r_PhaseFeedBackProcessVoltageOffset) {
		R_PhaseFeedBackProcessVoltageOffset = r_PhaseFeedBackProcessVoltageOffset;
	}


	public static Double getY_PhaseFeedBackProcessVoltageOffset() {
		return Y_PhaseFeedBackProcessVoltageOffset;
	}


	public static void setY_PhaseFeedBackProcessVoltageOffset(Double y_PhaseFeedBackProcessVoltageOffset) {
		Y_PhaseFeedBackProcessVoltageOffset = y_PhaseFeedBackProcessVoltageOffset;
	}


	public static Double getB_PhaseFeedBackProcessVoltageOffset() {
		return B_PhaseFeedBackProcessVoltageOffset;
	}


	public static void setB_PhaseFeedBackProcessVoltageOffset(Double b_PhaseFeedBackProcessVoltageOffset) {
		B_PhaseFeedBackProcessVoltageOffset = b_PhaseFeedBackProcessVoltageOffset;
	}


	public static Double getR_PhaseFeedBackProcessCurrentGain() {
		return R_PhaseFeedBackProcessCurrentGain;
	}


	public static void setR_PhaseFeedBackProcessCurrentGain(Double r_PhaseFeedBackProcessCurrentGain) {
		R_PhaseFeedBackProcessCurrentGain = r_PhaseFeedBackProcessCurrentGain;
	}


	public static Double getY_PhaseFeedBackProcessCurrentGain() {
		return Y_PhaseFeedBackProcessCurrentGain;
	}


	public static void setY_PhaseFeedBackProcessCurrentGain(Double y_PhaseFeedBackProcessCurrentGain) {
		Y_PhaseFeedBackProcessCurrentGain = y_PhaseFeedBackProcessCurrentGain;
	}


	public static Double getB_PhaseFeedBackProcessCurrentGain() {
		return B_PhaseFeedBackProcessCurrentGain;
	}


	public static void setB_PhaseFeedBackProcessCurrentGain(Double b_PhaseFeedBackProcessCurrentGain) {
		B_PhaseFeedBackProcessCurrentGain = b_PhaseFeedBackProcessCurrentGain;
	}


	public static Double getR_PhaseFeedBackProcessCurrentOffset() {
		return R_PhaseFeedBackProcessCurrentOffset;
	}


	public static void setR_PhaseFeedBackProcessCurrentOffset(Double r_PhaseFeedBackProcessCurrentOffset) {
		R_PhaseFeedBackProcessCurrentOffset = r_PhaseFeedBackProcessCurrentOffset;
	}


	public static Double getY_PhaseFeedBackProcessCurrentOffset() {
		return Y_PhaseFeedBackProcessCurrentOffset;
	}


	public static void setY_PhaseFeedBackProcessCurrentOffset(Double y_PhaseFeedBackProcessCurrentOffset) {
		Y_PhaseFeedBackProcessCurrentOffset = y_PhaseFeedBackProcessCurrentOffset;
	}


	public static Double getB_PhaseFeedBackProcessCurrentOffset() {
		return B_PhaseFeedBackProcessCurrentOffset;
	}


	public static void setB_PhaseFeedBackProcessCurrentOffset(Double b_PhaseFeedBackProcessCurrentOffset) {
		B_PhaseFeedBackProcessCurrentOffset = b_PhaseFeedBackProcessCurrentOffset;
	}


	public static float getRefStdSelectedVoltageTap() {
		return refStdSelectedVoltageTap;
	}


	public static void setRefStdSelectedVoltageTap(float refStdSelectedVoltageTap) {
		DeviceDataManagerController.refStdSelectedVoltageTap = refStdSelectedVoltageTap;
	}


	public static float getRefStdSelectedCurrentTap() {
		return refStdSelectedCurrentTap;
	}


	public static void setRefStdSelectedCurrentTap(float refStdSelectedCurrentTap) {
		DeviceDataManagerController.refStdSelectedCurrentTap = refStdSelectedCurrentTap;
	}


	public boolean getReadRefStdAccuDataFlag() {
		// TODO Auto-generated method stub
		
		return readRefStdAccuDataFlag;
	}
	
	public void setReadRefStdAccuDataFlag(boolean inputData) {
		// TODO Auto-generated method stub
		readRefStdAccuDataFlag = inputData;
		//return readRefStdAccuDataFlag;
	}


	public static OperationProcessJsonReadModel getReportProfileConfigParsedKey() {
		return reportProfileConfigParsedKey;
	}


	public static void setReportProfileConfigParsedKey(
			OperationProcessJsonReadModel reportProfileOperationProcessDataKeyConfigParsedKey) {
		DeviceDataManagerController.reportProfileConfigParsedKey = reportProfileOperationProcessDataKeyConfigParsedKey;
	}


	static public ReportProfileMeterMetaDataFilterService getReportProfileMeterMetaDataFilterService() {
		return reportProfileMeterMetaDataFilterService;
	}


	public void setReportProfileMeterMetaDataFilterService(
			ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService) {
		this.reportProfileMeterMetaDataFilterService = reportProfileMeterMetaDataFilterService;
	}


/*	public static ClassPathXmlApplicationContext getSpringAppCtx() {
		return springAppCtx;
	}


	public static void setSpringAppCtx(ClassPathXmlApplicationContext springAppCtx) {
		DeviceDataManagerController.springAppCtx = springAppCtx;
	}*/

	public static ReportProfileManageService getReportProfileManageService() {
		return reportProfileManageService;
	}

	public static void setReportProfileManageService(ReportProfileManageService reportProfileManageService) {
		DeviceDataManagerController.reportProfileManageService = reportProfileManageService;
	}

	public static ReportProfileTestDataFilterService getReportProfileTestDataFilterService() {
		return reportProfileTestDataFilterService;
	}

	public static void setReportProfileTestDataFilterService(
			ReportProfileTestDataFilterService reportProfileTestDataFilterService) {
		DeviceDataManagerController.reportProfileTestDataFilterService = reportProfileTestDataFilterService;
	}

	public static OperationProcessService getReportOperationProcessService() {
		return reportOperationProcessService;
	}

	public static void setReportOperationProcessService(OperationProcessService reportOperationProcessService) {
		DeviceDataManagerController.reportOperationProcessService = reportOperationProcessService;
	}

	public static OperationParamService getRpOperationParamService() {
		return rpOperationParamService;
	}

	public static void setRpOperationParamService(OperationParamService rpOperationParamService) {
		DeviceDataManagerController.rpOperationParamService = rpOperationParamService;
	}

	public static boolean isRefStdPortInitSuccess() {
		return refStdPortInitSuccess;
	}

	public static void setRefStdPortInitSuccess(boolean refStdPortInitSuccess) {
		DeviceDataManagerController.refStdPortInitSuccess = refStdPortInitSuccess;
	}

	public static SerialPortManagerRefStd getSerialPortManagerRefStd() {
		return serialPortManagerRefStd;
	}

	public static void setSerialPortManagerRefStd(SerialPortManagerRefStd serialPortManagerRefStd) {
		DeviceDataManagerController.serialPortManagerRefStd = serialPortManagerRefStd;
	}

	public static SerialPortManagerRefStd_V2 getSerialPortManagerRefStd_V2() {
		return serialPortManagerRefStd_V2;
	}

	public static void setSerialPortManagerRefStd_V2(SerialPortManagerRefStd_V2 serialPortManagerRefStd_V2) {
		DeviceDataManagerController.serialPortManagerRefStd_V2 = serialPortManagerRefStd_V2;
	}

	public static ConveyorConfigModel getConveyorConfigParsedKey() {
		return conveyorConfigParsedKey;
	}

	public static void setConveyorConfigParsedKey(ConveyorConfigModel conveyorConfigParsedKey) {
		DeviceDataManagerController.conveyorConfigParsedKey = conveyorConfigParsedKey;
	}

	public static SerialPortManagerPwrSrc_V2 getSerialPortManagerPwrSrc_V2() {
		return serialPortManagerPwrSrc_V2;
	}

	public static void setSerialPortManagerPwrSrc_V2(SerialPortManagerPwrSrc_V2 serialPortManagerPwrSrc_V2) {
		DeviceDataManagerController.serialPortManagerPwrSrc_V2 = serialPortManagerPwrSrc_V2;
	}
	
	
	public static String getLastSetPowerSourceRphaseVoltage() {
		return lastSetPowerSourceRphaseVoltage;
	}

	public static void setLastSetPowerSourceRphaseVoltage(String lastSetPowerSourceRphaseVoltage) {
		DeviceDataManagerController.lastSetPowerSourceRphaseVoltage = lastSetPowerSourceRphaseVoltage;
	}

	public static String getLastSetPowerSourceYphaseVoltage() {
		return lastSetPowerSourceYphaseVoltage;
	}

	public static void setLastSetPowerSourceYphaseVoltage(String lastSetPowerSourceYphaseVoltage) {
		DeviceDataManagerController.lastSetPowerSourceYphaseVoltage = lastSetPowerSourceYphaseVoltage;
	}

	public static String getLastSetPowerSourceBphaseVoltage() {
		return lastSetPowerSourceBphaseVoltage;
	}

	public static void setLastSetPowerSourceBphaseVoltage(String lastSetPowerSourceBphaseVoltage) {
		DeviceDataManagerController.lastSetPowerSourceBphaseVoltage = lastSetPowerSourceBphaseVoltage;
	}

	public static String getLastSetPowerSourceRphaseCurrent() {
		return lastSetPowerSourceRphaseCurrent;
	}

	public static void setLastSetPowerSourceRphaseCurrent(String lastSetPowerSourceRphaseCurrent) {
		DeviceDataManagerController.lastSetPowerSourceRphaseCurrent = lastSetPowerSourceRphaseCurrent;
	}

	public static String getLastSetPowerSourceYphaseCurrent() {
		return lastSetPowerSourceYphaseCurrent;
	}

	public static void setLastSetPowerSourceYphaseCurrent(String lastSetPowerSourceYphaseCurrent) {
		DeviceDataManagerController.lastSetPowerSourceYphaseCurrent = lastSetPowerSourceYphaseCurrent;
	}

	public static String getLastSetPowerSourceBphaseCurrent() {
		return lastSetPowerSourceBphaseCurrent;
	}

	public static void setLastSetPowerSourceBphaseCurrent(String lastSetPowerSourceBphaseCurrent) {
		DeviceDataManagerController.lastSetPowerSourceBphaseCurrent = lastSetPowerSourceBphaseCurrent;
	}

	public static String getLastSetPowerSourceRphaseDegree() {
		return lastSetPowerSourceRphaseDegree;
	}

	public static void setLastSetPowerSourceRphaseDegree(String lastSetPowerSourceRphaseDegree) {
		DeviceDataManagerController.lastSetPowerSourceRphaseDegree = lastSetPowerSourceRphaseDegree;
	}

	public static String getLastSetPowerSourceYphaseDegree() {
		return lastSetPowerSourceYphaseDegree;
	}

	public static void setLastSetPowerSourceYphaseDegree(String lastSetPowerSourceYphaseDegree) {
		DeviceDataManagerController.lastSetPowerSourceYphaseDegree = lastSetPowerSourceYphaseDegree;
	}

	public static String getLastSetPowerSourceBphaseDegree() {
		return lastSetPowerSourceBphaseDegree;
	}

	public static void setLastSetPowerSourceBphaseDegree(String lastSetPowerSourceBphaseDegree) {
		DeviceDataManagerController.lastSetPowerSourceBphaseDegree = lastSetPowerSourceBphaseDegree;
	}

	public static String getLastSetPowerSourceCurrentTapRelayId() {
		return lastSetPowerSourceCurrentTapRelayId;
	}

	public static void setLastSetPowerSourceCurrentTapRelayId(String lastSetPowerSourceCurrentTapRelayId) {
		DeviceDataManagerController.lastSetPowerSourceCurrentTapRelayId = lastSetPowerSourceCurrentTapRelayId;
	}
	
	public static void resetLastSetPowerSourceData() {
		setLastSetPowerSourceRphaseVoltage("");
		setLastSetPowerSourceYphaseVoltage("");
		setLastSetPowerSourceBphaseVoltage("");
		
		setLastSetPowerSourceRphaseCurrent("");
		setLastSetPowerSourceYphaseCurrent("");
		setLastSetPowerSourceBphaseCurrent("");
		
		setLastSetPowerSourceRphaseDegree("");
		setLastSetPowerSourceYphaseDegree("");
		setLastSetPowerSourceBphaseDegree("");
		
		setLastSetPowerSourceCurrentTapRelayId("");
		//resetLastSetPowerSourceFrequency();// commented on version #s4.2.1.2.1.4 for customtest issue on PowerSource only
	}

	public static void setHardwareBootupOccured(boolean bootOccured) {
		// TODO Auto-generated method stub
		hardwareBootupOccured = bootOccured;
	}
	
	public static boolean isHardwareBootupOccured() {
		// TODO Auto-generated method stub
		return hardwareBootupOccured;
	}

	public static boolean isPresentTestPointContainsHarmonics() {
		return presentTestPointContainsHarmonics;
	}

	public static void setPresentTestPointContainsHarmonics(boolean presentTestPointContainsHarmonics) {
		DeviceDataManagerController.presentTestPointContainsHarmonics = presentTestPointContainsHarmonics;
	}

	public static int getReadProPowerAllDataNoOfVariables() {
		return readProPowerAllDataNoOfVariables;
	}

	public static void setReadProPowerAllDataNoOfVariables(int readProPowerAllDataNoOfVariables) {
		DeviceDataManagerController.readProPowerAllDataNoOfVariables = readProPowerAllDataNoOfVariables;
	}

/*	public static boolean isRefStdReadData() {
		return refStdReadData;
	}

	public static void setRefStdReadData(boolean refStdReadData) {
		DeviceDataManagerController.refStdReadData = refStdReadData;
	}*/
	
	
	public static ArrayList<Boolean> getStepRunModeAtleastOneResultReadCompleted() {
		return stepRunModeAtleastOneResultReadCompleted;
	}

	public static void setStepRunModeAtleastOneResultReadCompleted(
			ArrayList<Boolean> stepRunModeAtleastOneResultReadCompleted) {
		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted = stepRunModeAtleastOneResultReadCompleted;
	}
	
	public static void resetStepRunModeAtleastOneResultReadCompleted() {
		
		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.clear();
		for(int i=0; i<=48; i++) {
			DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.add(false);
		}
		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.set(0, true);
		
	}
	
	public static  void updateMetricsOnExcelLogFile(long presentTimeInEpochMilliSec, String timeInMsec, String file_path, String fileName,String targetVoltage, String targetCurrent,
			String actualVoltage,
			String actualCurrent,
			String actualPower,
			String actualPf
			
			) {
		
		
		int meterSerialNoHeaderColumn = 2;
		String sourceTemplateFilePathName = file_path + fileName;
		
		
		XSSFWorkbook xSSFworkbook = null;
		XSSFSheet spreadsheet = null;
		//try {
		FileInputStream file;
		try {
			//file = new FileInputStream(new File(sourceTemplateFilePathName));
			file = new FileInputStream(new File(sourceTemplateFilePathName));
			System.out.println("updateMetricsOnExcelLogFile: sourceTemplateFilePathName: "+ sourceTemplateFilePathName);
			
			try {
				xSSFworkbook = new XSSFWorkbook(file);
				spreadsheet = xSSFworkbook.getSheet("Result");
				
				XSSFCellStyle style = xSSFworkbook.createCellStyle();
				style.setBorderTop(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);
				style.setBorderBottom(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
				style.setWrapText(true); 
				
				XSSFCellStyle failStyle = xSSFworkbook.createCellStyle();
				failStyle.setBorderTop(BorderStyle.THIN);
				failStyle.setBorderRight(BorderStyle.THIN);
				failStyle.setBorderBottom(BorderStyle.THIN);
				failStyle.setBorderLeft(BorderStyle.THIN);
				failStyle.setWrapText(true); 
				failStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
				failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				
				
				Row row = spreadsheet.createRow(getPresentCursorRowNo().incrementAndGet());
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((presentTimeInEpochMilliSec));
				
				//String cmdTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((
				//		getMetricsLogTestPointCmdSentEpochTimeInMSec()));
				
				//row.createCell(meterSerialNoHeaderColumn).setCellValue(cmdTimeStamp);			
				//row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(timeStamp);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(timeInMsec);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(targetVoltage);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(targetCurrent);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(actualVoltage);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(actualCurrent);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(actualPower);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(actualPf);			
				row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				
				
				float fTargetVoltage = Float.parseFloat(targetVoltage);
				float factualVoltage = Float.parseFloat(actualVoltage);
				float voltageAccuracy =  ((factualVoltage/fTargetVoltage) * 100)-100.0f;
				String voltageAccStr = "-NA-";
				/*if(fTargetVoltage!=0.0f) {
					voltageAccStr= String.format("%02.03f", voltageAccuracy);
				}*/
				
				int setScaleCurrentAfterDecimal = 3;
				if(fTargetVoltage!=0.0f) {
					BigDecimal bigValue = new BigDecimal(voltageAccuracy);
					bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
					voltageAccuracy = bigValue.floatValue();
					
					voltageAccStr= String.format("%02.03f", voltageAccuracy);
				}
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: voltageAccuracy: "+ voltageAccuracy);
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: voltageAccStr: "+ voltageAccStr);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(voltageAccStr);			
				//row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				if(!voltageAccStr.equals("-NA-")) {
					if ((voltageAccuracy>getMetricsLogAcceptedUpperLimit()) || (voltageAccuracy<getMetricsLogAcceptedLowerLimit())) {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(failStyle);
					}else {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
					}
				}else {
					row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				}
				
				float fTargetCurrent = Float.parseFloat(targetCurrent);
				float factualCurrent = Float.parseFloat(actualCurrent);
				float currentAccuracy =  ((factualCurrent/fTargetCurrent) * 100)-100.0f;
				String currentAccStr = "-NA-";
				if(fTargetCurrent!=0.0f) {
					BigDecimal bigValue = new BigDecimal(currentAccuracy);
					bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
					currentAccuracy = bigValue.floatValue();
					currentAccStr= String.format("%02.03f", currentAccuracy);
				}
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: currentAccuracy: "+ currentAccuracy);
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: currentAccStr: "+ currentAccStr);
				
				row.createCell(meterSerialNoHeaderColumn).setCellValue(currentAccStr);			
				//row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				if(!currentAccStr.equals("-NA-")) {
					if ((currentAccuracy>getMetricsLogAcceptedUpperLimit()) || (currentAccuracy<getMetricsLogAcceptedLowerLimit())) {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(failStyle);
					}else {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
					}
				}else {
					row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				}
				
				for(int i=2; i< 15;i++)  {
					spreadsheet.autoSizeColumn(i);
				}
				
				
				//xSSFworkbook.write(arg0);
				//xSSFworkbook.close();
				try {
					FileOutputStream fileOut = new FileOutputStream(file_path+fileName);//fileName);
					//workbook.write(fileOut);
					xSSFworkbook.write(fileOut);
					xSSFworkbook.close();
					fileOut.close();
					
					
					//String pdfFileName = reportFileNameWithSerialNo;
					
					
					
					//ApplicationHomeController.update_left_status(fileName +" Exported",ConstantApp.LEFT_STATUS_DEBUG);
					//promptUserToOpenReportOutputFolderPath(pdfFileName,file_path);
					//ApplicationLauncher.logger.info("exportAllResultToExcelV2: Export Successful- " +reportFileName+" generated successfully- Prompted");

					//ApplicationLauncher.InformUser("Export Success", reportFileName +" exported successfully", AlertType.INFORMATION);
					} catch(Exception e) {
						e.printStackTrace();
						//ApplicationLauncher.logger.error("exportAllResultToExcelV2: Export failed- " +fileName+" failed due to "  + e.getMessage()+"- Prompted");

						//ApplicationLauncher.InformUser("Export failed", fileName +" failed due to below reason\n\n" + e.getMessage(), AlertType.ERROR);
					}
				
				//System.out.println("updateMetricsOnExcelLogFile: Success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("updateMetricsOnExcelLogFile: Exception-1:" + e.getMessage());
			}
		

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("updateMetricsOnExcelLogFile: Exception-2:" + e.getMessage());
		}
			
	}
	
	public static void createMetricsExcelLogFile(String file_path, String fileName) {
		// TODO Auto-generated method stub
		
		String project_name = "myTest";//get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		//ApplicationLauncher.logger.info("exportAllResultToExcelV2: project_name: "+project_name);
		if(project_name != null){
			//ApplicationHomeController.update_left_status("Exporting...",ConstantApp.LEFT_STATUS_DEBUG);
			
			String reportFileNameWithSerialNo = "";//getProjectFileNameWithSerialNo();// getProjectFileName();
			String reportFileName = "";//getProjectFileName();
			String projectDate = "";//getProjectDate();
			//Workbook workbook = new HSSFWorkbook();
			//Sheet spreadsheet = workbook.createSheet("Sheet1");
			
			//HSSFWorkbook HSSFworkbook = new HSSFWorkbook(file);
			
			//String sourceTemplateFilePathName = ConstantConfig.REPORT_TEMPLATE_FILE_PATH + ConstantConfig.REPORT_TEMPLATE_FILE_NAME;//"C:\\Reports\\Template\\ProTampReport_V1_0.xlsx";
			//String sourceTemplateFilePathName = "C:\\Reports\\Template\\ProTampReport_V1_0.xlsx";
			
			FileInputStream file = null;
			XSSFWorkbook xSSFworkbook = null;
			XSSFSheet spreadsheet = null;
			//try {
				//file = new FileInputStream(new File(sourceTemplateFilePathName));
			
				
					xSSFworkbook = new XSSFWorkbook();
				
			//XSSFSheet spreadsheet = xSSFworkbook.getSheetAt(0);
			//XSSFSheet spreadsheet = xSSFworkbook.createSheet("Result");
				spreadsheet = xSSFworkbook.createSheet("Result");
				//spreadsheet = xSSFworkbook.getSheetAt(0);//.createSheet("Result");
			
			/*} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			//xSSFworkbook.setActiveSheet(1);
			int userSelectedRow = 4;//0
			int userSelectedColumn = 0;//1;//0;
			int userSelectedInitForAutoSizeColumn = userSelectedColumn;
			
			int meterSerialNoRow = 0;//1;
			int meterSerialNoHeaderColumn = 2;//userSelectedColumn+2 ;//3;
			int meterSerialNoValueColumn = 3;//userSelectedColumn+3;//4;
			
			int projectDetailTimeStampHeaderColumn = 4;// userSelectedColumn+4;//5;
			int projectDetailTimeStampValueColumn = 6;// userSelectedColumn+5;//6;
			
			int dateValuePrintRow = 1;
			int dateHeaderPrintColumn = 5;//userSelectedColumn+5;//6;
			int dateValuePrintColumn = 6;//userSelectedColumn+6;//7;
			
			//Row row = spreadsheet.createRow(userSelectedRow);//0);
			Row row = spreadsheet.createRow(meterSerialNoRow++);//0);
			//CellStyle style = (HSSFCellStyle) row.getSheet().getWorkbook().createCellStyle();
			XSSFCellStyle headerStyle = xSSFworkbook.createCellStyle();
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setWrapText(true); 
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
			
			XSSFCellStyle style = xSSFworkbook.createCellStyle();
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setWrapText(true); 
			//XSSFColor myColor = new XSSFColor(Color.RED);
			//style.setFillForegroundColor(myColor);
			spreadsheet.addIgnoredErrors(new CellRangeAddress(0, 10000, 0, 99), IgnoredErrorType.NUMBER_STORED_AS_TEXT); 
			
			
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Lower Limit");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue(getMetricsLogAcceptedLowerLimit());			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Upper Limit");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue(getMetricsLogAcceptedUpperLimit());			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
			
			
			meterSerialNoHeaderColumn =meterSerialNoHeaderColumn -4;
			row = spreadsheet.createRow(meterSerialNoRow);
			//row.createCell(meterSerialNoHeaderColumn).setCellValue("Cmd TimeStamp");			
			//row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("TimeStamp");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Time (ms)");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Target-V");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Target-I");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Actual V");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Actual-I");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Actual-P");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Actual-pf");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Accu-V %");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			row.createCell(meterSerialNoHeaderColumn).setCellValue("Accu-I %");			
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);
			
			//Sheet.createFreezePane(int colSplit, int rowSplit)
			spreadsheet.createFreezePane(2, 2);
			
			setPresentCursorRowNo(new AtomicInteger(1));
			
			//row.createCell(meterSerialNoValueColumn).setCellValue("test1");			
			//row.getCell(meterSerialNoValueColumn).setCellStyle(style);
			//meterSerialNoHeaderColumn++;
			
			
			
			
			//spreadsheet.addMergedRegion(rowFrom          ,rowTo,          colFrom,                         colTo);
			//spreadsheet.addMergedRegion  (meterSerialNoRow,meterSerialNoRow,projectDetailTimeStampValueColumn,(projectDetailTimeStampValueColumn+1));
			
			
/*			row.createCell(projectDetailTimeStampValueColumn+1);
			spreadsheet.addMergedRegion(new CellRangeAddress(meterSerialNoRow,meterSerialNoRow,projectDetailTimeStampValueColumn,projectDetailTimeStampValueColumn+1));
			
			row.createCell(projectDetailTimeStampValueColumn+1);
			row.createCell(projectDetailTimeStampValueColumn).setCellValue(reportFileName.replace(".xlsx", ""));			
			row.getCell(projectDetailTimeStampValueColumn).setCellStyle(style);
			row.getCell(projectDetailTimeStampValueColumn+1).setCellStyle(style);*/
			
			
			//row = spreadsheet.createRow(dateValuePrintRow);//0);
			
/*			row.createCell(dateHeaderPrintColumn).setCellValue("Date:");	
			row.getCell(dateHeaderPrintColumn).setCellStyle(headerStyle);
			
			row.createCell(dateValuePrintColumn).setCellValue(projectDate);	
			row.getCell(dateValuePrintColumn).setCellStyle(style);
			*/
			//row = spreadsheet.createRow(userSelectedRow);
			
			//spreadsheet.getRange("A1").getColumns().autoFit();
			
			
			
			
			
/*			for (int j = 0; j < result_table_view.getColumns().size(); j++) {
				row.createCell(userSelectedColumn+j).setCellValue(result_table_view.getColumns().get(j).getText());
				
				
				row.getCell(userSelectedColumn+j).setCellStyle(headerStyle);
				
				ApplicationLauncher.logger.debug("exportAllResultToExcelV2: loop1: "+result_table_view.getColumns().get(j).getText());
			}
			
			row.createCell(result_table_view.getColumns().size()+userSelectedColumn).setCellValue("Occurance Status");
			row.getCell(result_table_view.getColumns().size()+userSelectedColumn).setCellStyle(headerStyle);
			row.createCell(result_table_view.getColumns().size()+userSelectedColumn+1).setCellValue("Restoration Status");
			row.getCell(result_table_view.getColumns().size()+userSelectedColumn+1).setCellStyle(headerStyle);

			//style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
			for (int i = 0; i < 2; i++) {
				row = spreadsheet.createRow(userSelectedRow +i+1 );//i + 1);
				//ApplicationLauncher.logger.debug("exportAllResultToExcelV2: loop2-A: ");
				for (int j = 0; j < 4; j++) {
					//if(result_table_view.getColumns().get(j).getCellData(i) != null) { 
						row.createCell(j+userSelectedColumn).setCellValue("H-"+i+"-"+j); 
						row.getCell(userSelectedColumn+j).setCellStyle(style);
						//ApplicationLauncher.logger.debug("exportAllResultToExcelV2: loop2-B: "+result_table_view.getColumns().get(j).getCellData(i).toString());
					}
					else {
						row.createCell(j).setCellValue("");
					}   
				}
			}*/
			
/*			for (int i = 0; i < result_table_view.getItems().size(); i++) {
				//row = spreadsheet.createRow(i + 1);
				row = spreadsheet.getRow(userSelectedRow+i+1);//i + 1);
				//ApplicationLauncher.logger.debug("exportAllResultToExcelV2: loop2-D: ");
				for (int j = 3; j < result_table_view.getColumns().size(); j++) {
					if(result_table_view.getColumns().get(j).getCellData(i) != null) { 
						
						if(result_table_view.getColumns().get(j).getCellData(i).toString().startsWith(ConstantReport.RESULT_STATUS_PASS)) {
							row.createCell(j+2+userSelectedColumn).setCellValue("Pass");
						}else if(result_table_view.getColumns().get(j).getCellData(i).toString().startsWith(ConstantReport.RESULT_STATUS_FAIL)) {
							row.createCell(j+2+userSelectedColumn).setCellValue("Fail");
						}else {
							row.createCell(j+2+userSelectedColumn).setCellValue("");
						}
						row.getCell(userSelectedColumn+j+2).setCellStyle(style);
					
						//ApplicationLauncher.logger.debug("exportAllResultToExcelV2: loop2-E: "+result_table_view.getColumns().get(j).getCellData(i).toString());
					}
					else {
						row.createCell(j+2+userSelectedColumn).setCellValue("");
					}   
				}
			}
			
			for(int i=userSelectedInitForAutoSizeColumn; i< (userSelectedInitForAutoSizeColumn+8);i++)  {
				spreadsheet.autoSizeColumn(i);
			}*/
			
			
			
			

			//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");  


			//String fileName = "MetricsLog.xlsx";//ConstantReport.ALL_PROJECT_REPORT_FILENAME;
			//String reportFileName = getProjectFileName();
			//ApplicationLauncher.logger.info("exportAllResultToExcelV2 : reportFileNameWithSerialNo:" + reportFileNameWithSerialNo);
			//ApplicationLauncher.logger.info("exportAllResultToExcelV2 : fileName:" + fileName);

			//String file_path = "c:\\Reports\\";//getSaveFilePath("")//ConstantConfig.SAVE_FILE_LOCATION);//ConstantReport.SAVE_FILE_LOCATION);
			try {
			FileOutputStream fileOut = new FileOutputStream(file_path+fileName);//fileName);
			//workbook.write(fileOut);
			xSSFworkbook.write(fileOut);
			xSSFworkbook.close();
			fileOut.close();
			
			
			//String pdfFileName = reportFileNameWithSerialNo;
			
			
			
			//ApplicationHomeController.update_left_status(fileName +" Exported",ConstantApp.LEFT_STATUS_DEBUG);
			//promptUserToOpenReportOutputFolderPath(pdfFileName,file_path);
			//ApplicationLauncher.logger.info("exportAllResultToExcelV2: Export Successful- " +reportFileName+" generated successfully- Prompted");

			//ApplicationLauncher.InformUser("Export Success", reportFileName +" exported successfully", AlertType.INFORMATION);
			} catch(Exception e) {
				e.printStackTrace();
				//ApplicationLauncher.logger.error("exportAllResultToExcelV2: Export failed- " +fileName+" failed due to "  + e.getMessage()+"- Prompted");

				//ApplicationLauncher.InformUser("Export failed", fileName +" failed due to below reason\n\n" + e.getMessage(), AlertType.ERROR);
			}
		}else{
			//ApplicationLauncher.logger.info("exportAllResultToExcelV2: No project selected- No Project is selected - Prompted");
			//ApplicationLauncher.InformUser("No project selected", "No Project selected", AlertType.ERROR);
		}
		
	}
	
	public static AtomicInteger getPresentCursorRowNo() {
		return presentCursorRowNo;
	}

	public static void setPresentCursorRowNo(AtomicInteger presentCursorRowNo) {
		DeviceDataManagerController.presentCursorRowNo = presentCursorRowNo;
	}

	public static String getPresentMetricLogFileName() {
		return presentMetricLogFileName;
	}

	public static void setPresentMetricLogFileName(String presentMetricLogFileName) {
		DeviceDataManagerController.presentMetricLogFileName = presentMetricLogFileName;
	}

	public void updateMetricLogFile( ) {
		// TODO Auto-generated method stub
		
		String file_path = "c:\\Reports\\Metrics\\";
		String fileName = getPresentMetricLogFileName();
		/*String targetVoltage =  getR_PhaseOutputVoltage();
		String targetCurrent =  getR_PhaseOutputCurrent();*/
		String actualVoltage =  ProjectExecutionController.getFeedbackR_phaseVolt();
		String actualCurrent =  ProjectExecutionController.getFeedbackR_phaseCurrent();
		
		String targetVoltage =  getMetricsLogTargetVoltage();
		String targetCurrent =  getMetricsLogTargetCurrent();
		String actualPower =  ProjectExecutionController.getFeedbackR_activePower();
		String actualPf =   ProjectExecutionController.getFeedbackR_powerFactor();
		long presentTimeInEpochMilliSec = Instant.now().toEpochMilli();
		if(getMetricsLogTestPointStartingEpochTimeInMSec()!=0){
			long diffInMSec = presentTimeInEpochMilliSec - getMetricsLogTestPointStartingEpochTimeInMSec();
			String timeInMilliSec = String.valueOf(diffInMSec);
			updateMetricsOnExcelLogFile(presentTimeInEpochMilliSec, timeInMilliSec, file_path, fileName,  targetVoltage,targetCurrent, 
					actualVoltage, actualCurrent, actualPower, actualPf);
			ApplicationLauncher.logger.debug("updateMetricLogFile: getPreviousMetricLogEpochTimeInMSec: " + getMetricsLogTestPointStartingEpochTimeInMSec());
			ApplicationLauncher.logger.debug("updateMetricLogFile: now: " + presentTimeInEpochMilliSec);
			ApplicationLauncher.logger.debug("updateMetricLogFile: diffInMSec: " + diffInMSec);
			ApplicationLauncher.logger.debug("updateMetricLogFile: getMetricsLogCounter: " + getMetricsLogCounter());
			if(getMetricsLogCounter()==0){
				ApplicationLauncher.logger.debug("updateMetricLogFile: Loading next test point" );
				ProjectExecutionController.loadNextTestPointTask();
			}
			decrementMetricsLogCounter();
		}
		//setPreviousMetricLogEpochTimeInMSec(now);
		
		
	}

	public static long getMetricsLogTestPointStartingEpochTimeInMSec() {
		return metricsLogTestPointStartingEpochTimeInMSec;
	}

	public static void setMetricsLogTestPointStartingEpochTimeInMSec(long previousMetricLogEpochTimeInMSec) {
		DeviceDataManagerController.metricsLogTestPointStartingEpochTimeInMSec = previousMetricLogEpochTimeInMSec;
	}

	public static int getMetricsLogCounter() {
		return metricsLogCounter;
	}
	
	public static int decrementMetricsLogCounter() {
		return metricsLogCounter--;
	}

	public static void setMetricsLogCounter(int metricsLogCounter) {
		DeviceDataManagerController.metricsLogCounter = metricsLogCounter;
	}

	public static boolean isMetricsLogTestPointStartingAlreadyInitated() {
		return metricsLogTestPointStartingAlreadyInitated;
	}

	public static void setMetricsLogTestPointStartingAlreadyInitated(boolean metricsLogTestPointStartingAlreadyInitated) {
		DeviceDataManagerController.metricsLogTestPointStartingAlreadyInitated = metricsLogTestPointStartingAlreadyInitated;
	}

	public static long getMetricsLogTestPointCmdSentEpochTimeInMSec() {
		return metricsLogTestPointCmdSentEpochTimeInMSec;
	}

	public static void setMetricsLogTestPointCmdSentEpochTimeInMSec(long metricsLogTestPointCmdSentEpochTimeInMSec) {
		DeviceDataManagerController.metricsLogTestPointCmdSentEpochTimeInMSec = metricsLogTestPointCmdSentEpochTimeInMSec;
	}

	public static String getMetricsLogTargetVoltage() {
		return metricsLogTargetVoltage;
	}

	public static void setMetricsLogTargetVoltage(String metricsLogTargetVoltage) {
		DeviceDataManagerController.metricsLogTargetVoltage = metricsLogTargetVoltage;
	}

	public static String getMetricsLogTargetCurrent() {
		return metricsLogTargetCurrent;
	}

	public static void setMetricsLogTargetCurrent(String metricsLogTargetCurrent) {
		DeviceDataManagerController.metricsLogTargetCurrent = metricsLogTargetCurrent;
	}
	
	public static float getMetricsLogAcceptedLowerLimit() {
		return metricsLogAcceptedLowerLimit;
	}

	public static float getMetricsLogAcceptedUpperLimit() {
		return metricsLogAcceptedUpperLimit;
	}

	public void setMetricsLogAcceptedLowerLimit(float metricsLogAcceptedLowerLimit) {
		this.metricsLogAcceptedLowerLimit = metricsLogAcceptedLowerLimit;
	}

	public void setMetricsLogAcceptedUpperLimit(float metricsLogAacceptedUpperLimit) {
		this.metricsLogAcceptedUpperLimit = metricsLogAacceptedUpperLimit;
	}

	public static String getCalibModeRphaseVoltageTarget() {
		return calibModeRphaseVoltageTarget;
	}

	public static String getCalibModeRphaseCurrentTarget() {
		return calibModeRphaseCurrentTarget;
	}

	public static String getCalibModeRphasePfTarget() {
		return calibModeRphasePfTarget;
	}

	public static void setCalibModeRphaseVoltageTarget(String calibModeVoltageTarget) {
		DeviceDataManagerController.calibModeRphaseVoltageTarget = calibModeVoltageTarget;
	}

	public static void setCalibModeRphaseCurrentTarget(String calibModeCurrentTarget) {
		DeviceDataManagerController.calibModeRphaseCurrentTarget = calibModeCurrentTarget;
	}

	public static void setCalibModeRphasePfTarget(String calibModePfTarget) {
		DeviceDataManagerController.calibModeRphasePfTarget = calibModePfTarget;
	}

	public static String getCalibModeFreqTarget() {
		return calibModeFreqTarget;
	}

	public static void setCalibModeFreqTarget(String calibModeFreqTarget) {
		DeviceDataManagerController.calibModeFreqTarget = calibModeFreqTarget;
	}

	public static DutCommand getDutCommandData() {
		return dutCommandData;
	}

	public static void setDutCommandData(DutCommand dutCommandData) {
		DeviceDataManagerController.dutCommandData = dutCommandData;
	}
	public static AppConfigService getAppConfigService() {
		return appConfigService;
	}

	public static void setAppConfigService(AppConfigService appConfigService) {
		DeviceDataManagerController.appConfigService = appConfigService;
	}
	
	public static void clearDutSerialNumberMap() {
		dutSerialNumberMap.clear();
	}
	
	public static void setDutSerialNumberMap( String dutSerialNo, int lduAddress) {
		
		ApplicationLauncher.logger.debug("setDutSerialNumber : dutAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("setDutSerialNumber : dutSerialNo: " + dutSerialNo);
		DeviceDataManagerController.dutSerialNumberMap.put(lduAddress, dutSerialNo);
		

	}

	public static DutCmdManager getDutCmdManagerV3() {
		return dutCmdManagerV3;
	}

	public static void setDutCmdManagerV3(DutCmdManager dutCmdManagerV3) {
		DeviceDataManagerController.dutCmdManagerV3 = dutCmdManagerV3;
	}

/*	public static SerialPortManagerDutCmd_V3 getSerialPortManagerDutCmd_V2() {
		return serialPortManagerDutCmd_V2;
	}

	public static void setSerialPortManagerDutCmd_V2(SerialPortManagerDutCmd_V3 serialPortManagerDut_V2) {
		DeviceDataManagerController.serialPortManagerDutCmd_V2 = serialPortManagerDut_V2;
	}
	*/
}
