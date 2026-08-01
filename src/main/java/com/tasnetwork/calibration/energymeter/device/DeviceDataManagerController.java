package com.tasnetwork.calibration.energymeter.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
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
import com.tasnetwork.spring.orm.service.AppConfigService;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.constant.ConveyorConfigModel;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigModel;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.reportprofile.OperationProcessJsonReadModel;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.DutCmdManager;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerRefStd_V2;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;
import com.tasnetwork.calibration.energymeter.testreport.config.ReportConfigModel;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.service.OperationParamService;
import com.tasnetwork.spring.orm.service.OperationProcessService;
import com.tasnetwork.spring.orm.service.ReportProfileManageService;
import com.tasnetwork.spring.orm.service.ReportProfileMeterMetaDataFilterService;
import com.tasnetwork.spring.orm.service.ReportProfileTestDataFilterService;
import com.tasnetwork.spring.orm.service.RpPrintPositionService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class DeviceDataManagerController {

	private static DutCmdManager dutCmdManagerV3 = new DutCmdManager();
	private static DutCommand dutCommandData = new DutCommand();

	private static String calibModeRphaseVoltageTarget = "240.0";
	private static String calibModeRphaseCurrentTarget = "10.0";
	private static String calibModeRphasePfTarget = "1.0";
	private static String calibModeFreqTarget = "50.0";
	public static HashMap<Integer, String> dutSerialNumberMap = new HashMap<>();
	// public static HashMap< Integer,String> dutSerialNumber = new HashMap<>();

	private static AtomicInteger presentCursorRowNo = new AtomicInteger(3);
	private static float metricsLogAcceptedLowerLimit = -0.250f;
	private static float metricsLogAcceptedUpperLimit = 0.250f;

	// public static ClassPathXmlApplicationContext springAppCtx =
	// ConstantVersion.springAppContext;

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

	private static ArrayList<Boolean> stepRunModeAtleastOneResultReadCompleted = new ArrayList<Boolean>();// ""; //
																											// Number of
																											// dial
																											// pulses
																											// (XXXXXXXX)

	// Get service from context.
	public static ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService = null;// springAppCtx.getBean(ReportProfileMeterMetaDataFilterService.class);
	public static ReportProfileManageService reportProfileManageService = null;// springAppCtx.getBean(ReportProfileMeterMetaDataFilterService.class);

	public static ReportProfileTestDataFilterService reportProfileTestDataFilterService = null;
	public static OperationProcessService reportOperationProcessService = null;
	public static RpPrintPositionService rpPrintPositionService = null;
	public static OperationParamService rpOperationParamService = null;
	public static AppConfigService appConfigService = null;

	private static volatile boolean powerSrcReadData = false;
	// private static volatile boolean refStdReadData = false;
	public static boolean powerSourcePortInitSuccess = false;// hvciPortInitSuccess
	public static boolean refStdPortInitSuccess = false;

	private static volatile boolean readRefStdAccuDataFlag = false;

	// public static HashMap< Integer,String> dutSerialNumber = new HashMap<>();
	private static ArrayList<UacDataModel> uacSelectProfileScreenList = null;
	private static Custom1ReportConfigModel custom1ReportConfigParsedKey = new Custom1ReportConfigModel();
	private static ConveyorConfigModel conveyorConfigParsedKey = new ConveyorConfigModel();

	static ReportConfigModel reportConfigParsedData = null;

	private static OperationProcessJsonReadModel reportProfileConfigParsedKey = new OperationProcessJsonReadModel();

	private static String sandsRefStdLastSetVoltageMode = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
	private static String sandsRefStdLastSetCurrentMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
	private static String sandsRefStdLastSetPulseOutputMode = ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY;

	private static HashMap<Integer, HashMap<Integer, String>> lduErrorDataHashMap2d = new HashMap<Integer, HashMap<Integer, String>>();

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

			bDUT1_ReadData, bDUT2_ReadData, bDUT3_ReadData, bDUT4_ReadData, bDUT5_ReadData,
			bDUT6_ReadData, bDUT7_ReadData, bDUT8_ReadData, bDUT9_ReadData, bDUT10_ReadData,
			bDUT11_ReadData, bDUT12_ReadData, bDUT13_ReadData, bDUT14_ReadData, bDUT15_ReadData,
			bDUT16_ReadData, bDUT17_ReadData, bDUT18_ReadData, bDUT19_ReadData, bDUT20_ReadData,
			bDUT21_ReadData, bDUT22_ReadData, bDUT23_ReadData, bDUT24_ReadData, bDUT25_ReadData,
			bDUT26_ReadData, bDUT27_ReadData, bDUT28_ReadData, bDUT29_ReadData, bDUT30_ReadData,
			bDUT31_ReadData, bDUT32_ReadData, bDUT33_ReadData, bDUT34_ReadData, bDUT35_ReadData,
			bDUT36_ReadData, bDUT37_ReadData, bDUT38_ReadData, bDUT39_ReadData, bDUT40_ReadData,
			bDUT41_ReadData, bDUT42_ReadData, bDUT43_ReadData, bDUT44_ReadData, bDUT45_ReadData,
			bDUT46_ReadData, bDUT47_ReadData, bDUT48_ReadData

	));

	// private static List<HashMap< Integer, Float>> lduErrorDatalistOfHashMaps =
	// new ArrayList<HashMap<Integer, Float>>();

	// HashMap<String, Integer> map = new HashMap<>();
	// private static ArrayList<ArrayList<Float>> lduErrorDataList = new
	// ArrayList<ArrayList<Float>>();// added for lscsc ldu average reading
	// calculation
	// private static ArrayList<ArrayList<Float>> lduErrorDataList = new
	// ArrayList<ArrayList<Float>>();

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

	String PowerSrcCommPortID = null;
	String PwrSrcCommBaudRate = null;
	String LDU_CommPortID = null;
	String LDUCommBaudRate = null;
	String RefStdCommPortID = null;
	String RefStdCommBaudRate = null;
	String ICT_CommPortID = null;
	String ICTCommBaudRate = null;

	String harmonicsSrcCommPortID = null;
	String harmonicsSrcCommBaudRate = null;

	Timer UIRefreshTimer;

	/*
	 * public static boolean bDUT1_ReadData = false;
	 * public static boolean bDUT2_ReadData = false;
	 * public static boolean bDUT3_ReadData = false;
	 * public static boolean bDUT4_ReadData = false;
	 * public static boolean bDUT5_ReadData = false;
	 * public static boolean bDUT6_ReadData = false;
	 * public static boolean bDUT7_ReadData = false;
	 * public static boolean bDUT8_ReadData = false;
	 * public static boolean bDUT9_ReadData = false;
	 * public static boolean bDUT10_ReadData = false;
	 * 
	 * public static boolean bDUT11_ReadData = false;
	 * public static boolean bDUT12_ReadData = false;
	 * public static boolean bDUT13_ReadData = false;
	 * public static boolean bDUT14_ReadData = false;
	 * public static boolean bDUT15_ReadData = false;
	 * public static boolean bDUT16_ReadData = false;
	 * public static boolean bDUT17_ReadData = false;
	 * public static boolean bDUT18_ReadData = false;
	 * public static boolean bDUT19_ReadData = false;
	 * public static boolean bDUT20_ReadData = false;
	 * 
	 * public static boolean bDUT21_ReadData = false;
	 * public static boolean bDUT22_ReadData = false;
	 * public static boolean bDUT23_ReadData = false;
	 * public static boolean bDUT24_ReadData = false;
	 * public static boolean bDUT25_ReadData = false;
	 * public static boolean bDUT26_ReadData = false;
	 * public static boolean bDUT27_ReadData = false;
	 * public static boolean bDUT28_ReadData = false;
	 * public static boolean bDUT29_ReadData = false;
	 * public static boolean bDUT30_ReadData = false;
	 * 
	 * public static boolean bDUT31_ReadData = false;
	 * public static boolean bDUT32_ReadData = false;
	 * public static boolean bDUT33_ReadData = false;
	 * public static boolean bDUT34_ReadData = false;
	 * public static boolean bDUT35_ReadData = false;
	 * public static boolean bDUT36_ReadData = false;
	 * public static boolean bDUT37_ReadData = false;
	 * public static boolean bDUT38_ReadData = false;
	 * public static boolean bDUT39_ReadData = false;
	 * public static boolean bDUT40_ReadData = false;
	 * 
	 * public static boolean bDUT41_ReadData = false;
	 * public static boolean bDUT42_ReadData = false;
	 * public static boolean bDUT43_ReadData = false;
	 * public static boolean bDUT44_ReadData = false;
	 * public static boolean bDUT45_ReadData = false;
	 * public static boolean bDUT46_ReadData = false;
	 * public static boolean bDUT47_ReadData = false;
	 * public static boolean bDUT48_ReadData = false;
	 */

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
	/*
	 * static float PwrSrc_Phase1 = 0;
	 * static float PwrSrc_Phase2 = 0;
	 * static float PwrSrc_Phase3 = 0;
	 */
	public static int error_count = 1;

	static boolean VoltageResetRequired = false;

	// public static String RSS_Pulse_Rate = GUIUtils.FormatPulseRate("100000000");
	public static String RSS_Pulse_Rate = "100000000";
	public static String LastSetRSS_Pulse_Rate = "100";
	// public static String LDU_PulseConstant =
	// GUIUtils.FormatPulseRate(ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT);

	// public static String RSS_ActivePulseConstant =
	// GUIUtils.FormatPulseRate(ConstantConfig.RSS_ACTIVE_PULSE_CONSTANT);
	// public static String RSS_ReactivePulseConstant =
	// GUIUtils.FormatPulseRate(ConstantConfig.RSS_REACTIVE_PULSE_CONSTANT);
	static ArrayList<String> PhaseDegreeOutput = new ArrayList<String>();

	static ArrayList<Integer> DevicesMounted = new ArrayList<Integer>();

	public DecimalFormat harmonicsOrderFormatter = new DecimalFormat("00");
	public DecimalFormat harmonicsPercentageFormatter = new DecimalFormat("00");
	public DecimalFormat harmonicsPhaseShiftFormatter = new DecimalFormat("000.0");
	public static DecimalFormat theoriticalPulseFormatter = new DecimalFormat("0000000");

	public static SerialPortManagerRefStd_V2 serialPortManagerRefStd_V2 = new SerialPortManagerRefStd_V2();
	public static SerialPortManagerPwrSrc_V2 serialPortManagerPwrSrc_V2 = new SerialPortManagerPwrSrc_V2();
	// public static SerialPortManagerDutCmd_V3 serialPortManagerDutCmd_V2 = new
	// SerialPortManagerDutCmd_V3();

	@FXML
	private void initialize() throws IOException {

	}

	static public void springDataInit() {
		ApplicationLauncher.logger.info("springDataInit : Entry");
		reportProfileManageService = ApplicationLauncher.springContext.getBean(ReportProfileManageService.class);
		reportProfileMeterMetaDataFilterService = ApplicationLauncher.springContext
				.getBean(ReportProfileMeterMetaDataFilterService.class);
		reportProfileTestDataFilterService = ApplicationLauncher.springContext
				.getBean(ReportProfileTestDataFilterService.class);
		reportOperationProcessService = ApplicationLauncher.springContext.getBean(OperationProcessService.class);
		rpPrintPositionService = ApplicationLauncher.springContext.getBean(RpPrintPositionService.class);
		rpOperationParamService = ApplicationLauncher.springContext.getBean(OperationParamService.class);
		appConfigService = ApplicationLauncher.springContext.getBean(AppConfigService.class);
	}

	public void CheckAllMeterConstSame() {
		ArrayList<Integer> meter_const_arr = new ArrayList<Integer>();

		JSONObject result = getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		try {
			JSONArray deployed_devices = result.getJSONArray("Devices");
			ApplicationLauncher.logger.info("CheckAllMeterConstSame :deployed_devices: " + deployed_devices.toString());
			// DeploymentManagerController.setAllMeterConstSame(true);// to be updated
			// if(DeploymentManagerController.IsAllMeterConstSame()){
			for (int index = 0; index < deployed_devices.length(); index++) {
				JSONObject jobj = deployed_devices.getJSONObject(index);
				int m_const = jobj.getInt("meter_const");
				// String meter_const = Integer.toString(m_const);
				meter_const_arr.add(m_const);
				ApplicationLauncher.logger.info("CheckAllMeterConstSame : index: " + index + " : m_const: " + m_const);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckAllMeterConstSame :JSONException: " + e.getMessage());
		}
		setAllMeterConstSame(true);
		if (meter_const_arr.size() > 0) {
			int ref_meter_const_value = meter_const_arr.get(0);

			for (int i = 1; i < meter_const_arr.size(); i++) {
				int value = meter_const_arr.get(i);
				if (!(ref_meter_const_value == value)) {
					setAllMeterConstSame(false);
					ApplicationLauncher.logger.info("CheckAllMeterConstSame : setting to false");
				}
			}
		}

		ApplicationLauncher.logger.info("CheckAllMeterConstSame : IsAllMeterConstSame: " + IsAllMeterConstSame());
	}

	public static boolean IsAllMeterConstSame() {
		return allMeterConstSame;
	}

	public static void setAllMeterConstSame(boolean value) {
		allMeterConstSame = value;
	}

	public static void setError_count(int inputErrorCount) {
		error_count = inputErrorCount;
	}

	public static int getError_countAndIncrement() {
		return error_count++;// = inputErrorCount;
	}

	public boolean updateExecutionStatusInDeployManageDB(String project_name, String deployment_id,
			String executionStatus, String mctModeCompletedStatus, String nctModeCompletedStatus) {
		boolean status = false;
		status = MySQL_Controller.sp_update_execution_status_deploy_manage(project_name, deployment_id, executionStatus,
				mctModeCompletedStatus, mctModeCompletedStatus);
		return status;
	}
	// public void SaveKWhToDB(){
	// ApplicationLauncher.logger.info("SaveKWhToDB : Entry");
	// String project_name = ProjectExecutionController.getCurrentProjectName();
	// String test_case_name = ProjectExecutionController.getCurrentTestPointName();
	// String alias_id = ProjectExecutionController.getCurrentTestAliasID();
	// ArrayList<Integer> devicesmounted = getDevicesMount();
	// String FailureReason = "";
	// String Resultstatus =
	// ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";hgfh
	// JSONArray initial_kwh_values = getInitMeterValues();
	// JSONArray final_kwh_values = getFinalMeterValues();
	// String rack_id = "";
	// String initialkwh = "";
	// String finalkwh = "";
	// for(int i =0; i<devicesmounted.size(); i++){
	// rack_id = Integer.toString(devicesmounted.get(i));
	//
	// initialkwh = get_kwh(rack_id, initial_kwh_values);
	// finalkwh = get_kwh(rack_id, final_kwh_values);
	//
	// int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
	// MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,
	// rack_id,
	// Resultstatus,getError_countAndIncrement(), initialkwh,FailureReason,
	// ConstantReport.RESULT_DATA_TYPE_INITIAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
	// ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
	// MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,
	// rack_id,
	// Resultstatus,getError_countAndIncrement(), finalkwh,FailureReason,
	// ConstantReport.RESULT_DATA_TYPE_FINAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
	// ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
	//
	// }
	// String ref_init_kwh = get_ref_i_kwh();
	// String ref_final_kwh = get_ref_f_kwh();
	// //MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, "0",
	// // Resultstatus,getError_countAndIncrement(), ref_init_kwh,FailureReason,
	// // ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH);
	// int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
	// MySQL_Controller.sp_add_result(project_name, test_case_name,
	// alias_id,rack_id,
	// Resultstatus,getError_countAndIncrement(), ref_init_kwh,FailureReason,
	// ConstantReport.RESULT_DATA_TYPE_REF_INITIAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
	// ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
	// MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,
	// rack_id,
	// Resultstatus,getError_countAndIncrement(), ref_final_kwh,FailureReason,
	// ConstantReport.RESULT_DATA_TYPE_REF_FINAL_KWH,getExecutionMctNctMode(),getEnergyFlowMode(),
	// ProjectExecutionController.getSelectedDeployment_ID(),seqNumber);
	// resetInitMeterValues();
	// resetFinalMeterValues();
	// }

	public String get_ref_i_kwh() {
		String RefInitPhaseA = getRefInitPhaseAReading();
		String RefInitPhaseB = getRefInitPhaseBReading();
		String RefInitPhaseC = getRefInitPhaseCReading();
		float total_value = Float.parseFloat(RefInitPhaseA) + Float.parseFloat(RefInitPhaseB)
				+ Float.parseFloat(RefInitPhaseC);
		String str_total_value = Float.toString(total_value);
		return str_total_value;
	}

	public String get_ref_f_kwh() {
		String RefFinalPhaseA = getRefFinalPhaseAReading();
		String RefFinalPhaseB = getRefFinalPhaseBReading();
		String RefFinalPhaseC = getRefFinalPhaseCReading();
		float total_value = Float.parseFloat(RefFinalPhaseA) + Float.parseFloat(RefFinalPhaseB)
				+ Float.parseFloat(RefFinalPhaseC);
		String str_total_value = Float.toString(total_value);
		return str_total_value;
	}

	public static void resetInitMeterValues() {
		JSONArray j_arr = new JSONArray();
		setInitMeterValues(j_arr);

	}

	public static void resetFinalMeterValues() {
		JSONArray j_arr = new JSONArray();
		setFinalMeterValues(j_arr);
	}

	public static String get_kwh(String req_rack_id, JSONArray kwh_values) {
		String kwh = "";
		try {
			JSONObject jobj = new JSONObject();
			String rack_id = "";
			for (int i = 0; i < kwh_values.length(); i++) {
				jobj = kwh_values.getJSONObject(i);
				rack_id = jobj.getString("rack_id");
				if (req_rack_id.equals(rack_id)) {
					kwh = jobj.getString("reading");
					break;
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("get_kwh: JSONException: " + e.toString());
		}
		return kwh;
	}

	public void LoadCurrentSerialComSettingFromDB() {
		ApplicationLauncher.logger.info("LoadCurrentSerialComSettingFromDB :Entry");
		PowerSrcCommPortID = get_port_name(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		PwrSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		LDU_CommPortID = get_port_name(ConstantApp.SOURCE_TYPE_LDU);
		LDUCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_LDU);
		RefStdCommPortID = get_port_name(ConstantApp.SOURCE_TYPE_REF_STD);
		RefStdCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_REF_STD);
		if (ProcalFeatureEnable.ICT_INTERFACE_ENABLED) {
			ICT_CommPortID = get_port_name(ConstantApp.SOURCE_TYPE_ICT);
			ICTCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_ICT);
		}

		// if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
		if (ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED) {
			harmonicsSrcCommPortID = get_port_name(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			harmonicsSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
		}

		if (ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
			harmonicsSrcCommPortID = get_port_name(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			harmonicsSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
		}

		ApplicationLauncher.logger.info("LoadCurrentSerialComSettingFromDB :RefStdCommPortID: " + RefStdCommPortID);
		ApplicationLauncher.logger
				.info("LoadCurrentSerialComSettingFromDB :harmonicsSrcCommPortID: " + harmonicsSrcCommPortID);

	}

	// public void setDeployedDevicesJson(){
	// String project_name = ProjectExecutionController.getCurrentProjectName();
	// String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();
	// DeployedDevicesJson =
	// MySQL_Controller.sp_getdeploy_devices(project_name,deploymentId);
	// }

	public static void setEnergyFlowMode(String FlowMode) {
		EnergyFlowMode = FlowMode;
	}

	public static String getEnergyFlowMode() {
		return EnergyFlowMode;
	}

	public void PowerSourceEnergyFlowModeDataInit() {

		if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
			if (getEnergyFlowMode().equals(ConstantPowerSourceMte.IMPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_IMPORT_ACTIVE_UPF_ANGLE;
			} else if (getEnergyFlowMode().equals(ConstantPowerSourceMte.EXPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_COS_EXPORT_ACTIVE_UPF_ANGLE;
			}
		} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
			if (getEnergyFlowMode().equals(ConstantPowerSourceMte.IMPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_IMPORT_REACTIVE_ZPF_ANGLE;
			} else if (getEnergyFlowMode().equals(ConstantPowerSourceMte.EXPORT_MODE)) {
				ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE = ConstantPowerSourceMte.POWER_SRC_3P4W_SINE_EXPORT_REACTIVE_ZPF_ANGLE;
			}
		}
	}

	public JSONObject getDeployedDevicesJson() {

		return DeployedDevicesJson;
	}

	public static void setPhaseRevPowerOn(boolean Value) {
		ApplicationLauncher.logger.debug("setPhaseRevPowerOn :Entry:Value:" + Value);
		PhaseRevPowerOn = Value;
	}

	final public boolean getPhaseRevPowerOn() {
		return PhaseRevPowerOn;
	}

	public static void setSkipPhaseRev(boolean Value) {
		SkipPhaseRev = Value;
	}

	final public boolean getSkipPhaseRev() {
		return SkipPhaseRev;
	}

	public static String getExecutionMctNctMode() {
		return executionMctNctMode;
	}

	public static void setExecutionMctNctMode(String executionMctNctMode) {
		DeviceDataManagerController.executionMctNctMode = executionMctNctMode;
	}

	public void setVoltageResetRequired(boolean value) {
		VoltageResetRequired = value;
		resetLastSetPowerSourceData();
	}

	public boolean getVoltageResetRequired() {
		return VoltageResetRequired;
	}

	public float CalculateInfVoltage(float VoltPercentage) {
		ApplicationLauncher.logger.info("CalculateInfVoltage :Entry");
		float RatedVolt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();// "240.0";

		float OutputVolt = RatedVolt * VoltPercentage / 100;
		ApplicationLauncher.logger.info("Output Voltage" + OutputVolt);
		// String op_volt = String.format("%.02f", OutputVolt);
		String op_volt = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, OutputVolt);
		OutputVolt = Float.parseFloat(op_volt);
		return OutputVolt;
	}

	public float CalculateInfCurrent() {
		ApplicationLauncher.logger.info("CalculateInfCurrent :Entry");
		String RatedofCurrent = DeviceDataManagerController.getRateOfCurrent();
		String is_ib = RatedofCurrent.substring(RatedofCurrent.length() - 2);
		String is_imax = RatedofCurrent.substring(RatedofCurrent.length() - 4);
		ApplicationLauncher.logger.info("is_ib: " + is_ib);
		ApplicationLauncher.logger.info("is_imax: " + is_imax);
		float OutputCurrent = 0;

		if (is_ib.equals("Ib")) {
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 2));
			float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();
			OutputCurrent = RatedCurrent * selectedRate;
		} else if (is_imax.equals("Imax")) {
			float selectedRate = Float.parseFloat(RatedofCurrent.substring(0, RatedofCurrent.length() - 4));
			float RatedMaxCurrent = DeviceDataManagerController.getPwrSrcR_PhaseMaxCurrentInFloat();
			OutputCurrent = RatedMaxCurrent * selectedRate;
		} else {
			OutputCurrent = 0;
		}
		ApplicationLauncher.logger.info("CalculateInfCurrent: Output Current1: " + OutputCurrent);
		// String op_current = String.format("%.02f", OutputCurrent);
		// String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION,
		// OutputCurrent);
		// OutputCurrent = Float.parseFloat(op_current);
		// ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);

		String op_current = "0.0";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			ApplicationLauncher.logger.info("Output Current2: " + OutputCurrent);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current3: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger.info("Output Current4: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current5: " + OutputCurrent);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(OutputCurrent);
			ApplicationLauncher.logger.info("Output Current5A: " + OutputCurrent);
		} else {

			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current3: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger.info("Output Current4: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current5: " + OutputCurrent);
			}
		}
		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("Output Current6: " + OutputCurrent);

		return OutputCurrent;
	}

	public float calculateInfCurrentWithPercentage(float inputPercentage) {
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage :Entry");
		String RatedofCurrent = DeviceDataManagerController.getRateOfCurrent();
		String is_ib = RatedofCurrent.substring(RatedofCurrent.length() - 2);
		String is_imax = RatedofCurrent.substring(RatedofCurrent.length() - 4);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: is_ib: " + is_ib);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: is_imax: " + is_imax);
		float OutputCurrent = 0;

		if (is_ib.equals("Ib")) {
			// float selectedRate = Float.parseFloat(RatedofCurrent.substring(0,
			// RatedofCurrent.length() - 2));
			float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();
			OutputCurrent = (RatedCurrent * inputPercentage) / 100;
		} else if (is_imax.equals("Imax")) {
			// float selectedRate = Float.parseFloat(RatedofCurrent.substring(0,
			// RatedofCurrent.length() - 4));
			float RatedMaxCurrent = DeviceDataManagerController.getPwrSrcR_PhaseMaxCurrentInFloat();
			OutputCurrent = (RatedMaxCurrent * inputPercentage) / 100;
		} else {
			OutputCurrent = 0;
		}
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage: Output Current1: " + OutputCurrent);
		// String op_current = String.format("%.02f", OutputCurrent);
		// String op_current = String.format(ConstantMtePowerSource.CURRENT_RESOLUTION,
		// OutputCurrent);
		// OutputCurrent = Float.parseFloat(op_current);
		// ApplicationLauncher.logger.info("Output Current2: "+ OutputCurrent);

		String op_current = "0.0";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current2: " + OutputCurrent);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger
						.info("calculateInfCurrentWithPercentage : Output Current3: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger
							.info("calculateInfCurrentWithPercentage : Output Current4: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger
						.info("calculateInfCurrentWithPercentage : Output Current5: " + OutputCurrent);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(OutputCurrent);
			ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current5A: " + OutputCurrent);
		} else {

			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger
						.info("calculateInfCurrentWithPercentage : Output Current3: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger
							.info("calculateInfCurrentWithPercentage : Output Current4: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger
						.info("calculateInfCurrentWithPercentage : Output Current5: " + OutputCurrent);
			}
		}
		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("calculateInfCurrentWithPercentage : Output Current6: " + OutputCurrent);

		return OutputCurrent;
	}

	public static void setHarmonic_data(JSONArray harmonics) {
		harmonic_data = harmonics;
	}

	final public JSONArray getHarmonic_data() {
		return harmonic_data;
	}

	public void setRefInitValues() {
		ApplicationLauncher.logger.info("setRefInitValues :Entry");
		setRefInitPhaseAReading(getCurrentPhaseAReading());
		setRefInitPhaseBReading(getCurrentPhaseBReading());
		setRefInitPhaseCReading(getCurrentPhaseCReading());
	}

	public void setRefFinalValues() {
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

	public float CalculateDiff(String init_value, String final_value) {
		float error = ((Float.parseFloat(final_value) - Float.parseFloat(init_value)));
		return error;
	}

	public float CalculateError(float em_diff, float ref_diff) {
		float errorvalue = ((em_diff / ref_diff) / 100);
		return errorvalue;
	}

	public static void set_CreepDuration(Integer Duration) {
		CreepDuration = Duration;

	}

	public Integer get_CreepDuration() {
		return CreepDuration;

	}

	public void set_STADuration(Integer Duration) {
		STADuration = Duration;

	}

	public Integer get_STADuration() {
		return STADuration;

	}

	public void set_Error_min(String Emin) {
		Error_min = Emin;

	}

	public static String get_Error_min() {
		return Error_min;

	}

	public void set_Error_max(String Emax) {
		Error_max = Emax;

	}

	public static String get_Error_max() {
		return Error_max;

	}

	public void setNoOfPulses(String no_of_cycles) {
		NoOfPulses = no_of_cycles;

	}

	public static String getNoOfPulses() {
		return NoOfPulses;

	}

	public static void setInfTimeDuration(int timeDuartion) {
		InfTimeDuration = timeDuartion;

	}

	public static int getInfTimeDuration() {
		return InfTimeDuration;

	}

	public void set_NoOfPulseReadingToBeSkipped(JSONObject count) {
		NoOfPulseReadingToBeSkipped = count;

	}

	public static JSONObject get_NoOfPulseReadingToBeSkipped() {
		return NoOfPulseReadingToBeSkipped;

	}

	public void decrement_NoOfPulseReadingToBeSkipped(int rack_id) {
		try {
			int skip_count = (NoOfPulseReadingToBeSkipped.getInt(Integer.toString(rack_id))) - 1;
			NoOfPulseReadingToBeSkipped.put(Integer.toString(rack_id), skip_count);
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("decrement_NoOfPulseReadingToBeSkipped :JSONException:" + e.getMessage());
		}
	}

	public void setTestRunType(String type) {
		TestRunType = type;

	}

	public static String getTestRunType() {
		return TestRunType;

	}

	public void setRateOfCurrent(String rate) {
		RateOfCurrent = rate;
	}

	public static String getRateOfCurrent() {
		return RateOfCurrent;

	}

	public void setRateOfVoltage(String rate) {
		RateOfVoltage = Float.parseFloat(rate);
	}

	public static Float getRateOfVoltage() {
		return RateOfVoltage;

	}

	public static void setInitMeterValues(JSONArray initMeterValues) {
		InitMeterValues = initMeterValues;
	}

	public static JSONArray getInitMeterValues() {
		return InitMeterValues;

	}

	public static void setFinalMeterValues(JSONArray finalMeterValues) {
		FinalMeterValues = finalMeterValues;
	}

	public static JSONArray getFinalMeterValues() {
		return FinalMeterValues;

	}

	public static void setRefInitPhaseAReading(String ref_reading) {
		Ref_Init_PhaseA_reading = ref_reading;
	}

	public static String getRefInitPhaseAReading() {
		return Ref_Init_PhaseA_reading;

	}

	public static void setRefInitPhaseBReading(String ref_reading) {
		Ref_Init_PhaseB_reading = ref_reading;
	}

	public static String getRefInitPhaseBReading() {
		return Ref_Init_PhaseB_reading;

	}

	public static void setRefInitPhaseCReading(String ref_reading) {
		Ref_Init_PhaseC_reading = ref_reading;
	}

	public static String getRefInitPhaseCReading() {
		return Ref_Init_PhaseC_reading;

	}

	public static void setRefFinalPhaseAReading(String ref_reading) {
		Ref_Final_PhaseA_reading = ref_reading;
	}

	public static String getRefFinalPhaseAReading() {
		return Ref_Final_PhaseA_reading;

	}

	public static void setRefFinalPhaseBReading(String ref_reading) {
		Ref_Final_PhaseB_reading = ref_reading;
	}

	public static String getRefFinalPhaseBReading() {
		return Ref_Final_PhaseB_reading;

	}

	public static void setRefFinalPhaseCReading(String ref_reading) {
		Ref_Final_PhaseC_reading = ref_reading;
	}

	public static String getRefFinalPhaseCReading() {
		return Ref_Final_PhaseC_reading;

	}

	public static void setCurrentPhaseAReading(String reading) {
		current_phaseA_reading = reading;
	}

	public static String getCurrentPhaseAReading() {
		return current_phaseA_reading;

	}

	public static void setCurrentPhaseBReading(String reading) {
		current_phaseB_reading = reading;
	}

	public static String getCurrentPhaseBReading() {
		return current_phaseB_reading;

	}

	public static void setCurrentPhaseCReading(String reading) {
		current_phaseC_reading = reading;
	}

	public static String getCurrentPhaseCReading() {
		return current_phaseC_reading;

	}

	public static void setHarmonics(JSONArray harmonic_data) {
		harmonics = harmonic_data;
	}

	public static JSONArray getHarmonics() {
		return harmonics;

	}

	public static void setcutnuetral_flag(boolean data) {
		cutnuetral_flag = data;
	}

	public static boolean getcutnuetral_flag() {
		return cutnuetral_flag;

	}

	public static void setcutnuetral_wait_flag(boolean data) {
		cutnuetral_wait_flag = data;
	}

	public static boolean getcutnuetral_wait_flag() {
		return cutnuetral_wait_flag;

	}

	public void set_WarmupDuration(Integer Duration) {
		WarmupDuration = Duration;

	}

	public Integer get_WarmupDuration() {
		return WarmupDuration;

	}

	public static void setPwrSrcR_PhaseVoltInFloat(String VoltageValue) {
		PwrSrcR_PhaseVoltInFloat = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcR_PhaseVoltInFloat() {
		return Float.valueOf(PwrSrcR_PhaseVoltInFloat);

	}

	public static void setPwrSrcR_PhaseCurrentInFloat(String CurrentValue) {
		PwrSrcR_PhaseCurrentInFloat = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcR_PhaseCurrentInFloat() {
		return Float.valueOf(PwrSrcR_PhaseCurrentInFloat);

	}

	public static void set_PwrSrcR_PhaseDegreePhase(String DegreePhase) {
		PwrSrcR_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcR_PhaseDegreePhase() {
		return PwrSrcR_PhaseDegreePhase;

	}

	public static String get_PwrSrcR_PhaseDegreePhaseStr() {

		String phaseAngleDegree = "123.4";
		// ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr:
		// PwrSrcR_PhaseDegreePhase: " + PwrSrcR_PhaseDegreePhase);
		if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			if (PwrSrcR_PhaseDegreePhase >= 0) {
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcR_PhaseDegreePhase);
				// ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr:
				// phaseAngleDegree1: " + phaseAngleDegree);

			} else {
				float reverseDegree = 360.0f + PwrSrcR_PhaseDegreePhase;
				// ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr:
				// reverseDegree2: " + reverseDegree);
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
				// ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr:
				// phaseAngleDegree3: " + phaseAngleDegree);
			}
		} else {
			phaseAngleDegree = String.valueOf(PwrSrcR_PhaseDegreePhase);
			// ApplicationLauncher.logger.debug("get_PwrSrcR_PhaseDegreePhaseStr:
			// phaseAngleDegree4: " + phaseAngleDegree);
		}
		return phaseAngleDegree;

	}

	public static void setPwrSrcY_PhaseVoltInFloat(String VoltageValue) {
		PwrSrcY_PhaseVoltInFloat = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcY_PhaseVoltInFloat() {
		return Float.valueOf(PwrSrcY_PhaseVoltInFloat);

	}

	public static void setPwrSrcY_PhaseCurrentInFloat(String CurrentValue) {
		PwrSrcY_PhaseCurrentInFloat = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcY_PhaseCurrentInFloat() {
		return Float.valueOf(PwrSrcY_PhaseCurrentInFloat);

	}

	public void set_PwrSrcY_PhaseDegreePhase(String DegreePhase) {
		PwrSrcY_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcY_PhaseDegreePhase() {
		return PwrSrcY_PhaseDegreePhase;

	}

	public static String get_PwrSrcY_PhaseDegreePhaseStr() {

		String phaseAngleDegree = "123.4";
		if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			// phaseAngleDegree =
			// ConstantBofaPowerSource.BOFA_DEGREE_RESOLUTION.format(PwrSrcY_PhaseDegreePhase);
			if (PwrSrcY_PhaseDegreePhase >= 0) {
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcY_PhaseDegreePhase);

			} else {
				float reverseDegree = 360.0f + PwrSrcY_PhaseDegreePhase;
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
			}
		} else {
			phaseAngleDegree = String.valueOf(PwrSrcY_PhaseDegreePhase);
		}
		return phaseAngleDegree;

	}

	public static void setPwrSrcB_PhaseVoltInFloat(String VoltageValue) {
		PwrSrcB_PhaseVoltInFloat = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcB_PhaseVoltInFloat() {
		return Float.valueOf(PwrSrcB_PhaseVoltInFloat);

	}

	public static void setPwrSrcB_PhaseCurrentInFloat(String CurrentValue) {
		PwrSrcB_PhaseCurrentInFloat = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcB_PhaseCurrentInFloat() {
		return Float.valueOf(PwrSrcB_PhaseCurrentInFloat);

	}

	public void set_PwrSrcB_PhaseDegreePhase(String DegreePhase) {
		PwrSrcB_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcB_PhaseDegreePhase() {
		return PwrSrcB_PhaseDegreePhase;

	}

	public static String get_PwrSrcB_PhaseDegreePhaseStr() {

		String phaseAngleDegree = "123.4";
		if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			// phaseAngleDegree =
			// ConstantBofaPowerSource.BOFA_DEGREE_RESOLUTION.format(PwrSrcB_PhaseDegreePhase);
			if (PwrSrcB_PhaseDegreePhase >= 0) {
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(PwrSrcB_PhaseDegreePhase);

			} else {
				float reverseDegree = 360.0f + PwrSrcB_PhaseDegreePhase;
				phaseAngleDegree = ConstantPowerSourceBofa.BOFA_DEGREE_RESOLUTION.format(reverseDegree);
			}
		} else {
			phaseAngleDegree = String.valueOf(PwrSrcB_PhaseDegreePhase);
		}
		return phaseAngleDegree;

	}

	public static void set_PwrSrc_Freq(String Frequency) {

		PwrSrcR_PhaseFreq = Float.valueOf(Frequency);

	}

	public static float get_PwrSrc_Freq() {
		return PwrSrcR_PhaseFreq;

	}

	public static String get_PwrSrc_FreqStr() {

		String freqStr = "50.99";
		if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {

			freqStr = ConstantPowerSourceBofa.BOFA_FREQ_RESOLUTION.format(PwrSrcR_PhaseFreq);
		} else {
			freqStr = String.valueOf(PwrSrcR_PhaseFreq);
		}
		return freqStr;

	}

	public static String getDeployedEM_ModelType() {
		return DeployedEM_ModelType;

	}

	public static void setDeployedEM_ModelType(String type) {
		DeployedEM_ModelType = type;

	}

	public static String getDeployedEM_CT_Type() {
		return DeployedEM_CT_Type;

	}

	public static void setDeployedEM_CT_Type(String type) {
		DeployedEM_CT_Type = type;

	}

	public void setDeployedDevicesJson(String project_name, String deploymentId) {
		/*
		 * String project_name = ProjectExecutionController.getCurrentProjectName();
		 * String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();
		 */
		DeployedDevicesJson = MySQL_Controller.sp_getdeploy_devices(project_name, deploymentId);
	}

	public static String getDutImpulsesPerUnit() {
		return dutImpulsesPerUnit;

	}

	public static void setDutImpulsesPerUnit(String impulses) {
		dutImpulsesPerUnit = impulses;

	}

	public static int getReadingToBeRead() {
		return ReadingToBeRead;

	}

	public static void DecrementReadingToBeRead() {
		ReadingToBeRead--;
	}

	public void setReadingToBeRead(int no_of_readings) {
		ReadingToBeRead = no_of_readings;

	}

	public static void setCreepNoOfPulses(String NoOfPulse) {
		CreepNoOfPulses = NoOfPulse;

	}

	public static String getCreepNoOfPulses() {
		return CreepNoOfPulses;

	}

	public static void setSTANoOfPulses(String NoOfPulse) {
		STANoOfPulses = NoOfPulse;

	}

	public static String getSTANoOfPulses() {
		return STANoOfPulses;

	}

	public static String getConstTestPower() {
		return ConstTestPower;

	}

	public static void setConstTestPower(String power) {
		ConstTestPower = power;

	}

	public static float getPercentageOfVoltage() {
		return PercentageOfVoltage;

	}

	public static void setPercentageOfVoltage(String VoltageValue) {
		PercentageOfVoltage = Float.valueOf(VoltageValue);

	}

	public static float getPercentageOfCurrent() {
		return PercentageOfCurrent;

	}

	public static void setPercentageOfCurrent(String CurrentValue) {
		PercentageOfCurrent = Float.valueOf(CurrentValue);

	}

	public static void setPwrSrc_Percent_VoltU1(String VoltageValue) {
		PwrSrc_Percent_VoltU1 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrc_Percent_VoltU1() {
		return Float.valueOf(PwrSrc_Percent_VoltU1);

	}

	public static void setPwrSrc_Percent_VoltU2(String VoltageValue) {
		PwrSrc_Percent_VoltU2 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrc_Percent_VoltU2() {
		return Float.valueOf(PwrSrc_Percent_VoltU2);

	}

	public static void setPwrSrc_Percent_VoltU3(String VoltageValue) {
		PwrSrc_Percent_VoltU3 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrc_Percent_VoltU3() {
		return Float.valueOf(PwrSrc_Percent_VoltU3);

	}

	public static void setPwrSrcCustomInFloat_VoltU1(String VoltageValue) {
		PwrSrcCustomInFloat_VoltU1 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcCustomInFloat_VoltU1() {
		return Float.valueOf(PwrSrcCustomInFloat_VoltU1);

	}

	public static void setPwrSrcCustomInFloat_VoltU2(String VoltageValue) {
		PwrSrcCustomInFloat_VoltU2 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcCustomInFloat_VoltU2() {
		return Float.valueOf(PwrSrcCustomInFloat_VoltU2);

	}

	public static void setPwrSrcCustomInFloat_VoltU3(String VoltageValue) {
		PwrSrcCustomInFloat_VoltU3 = Float.valueOf(VoltageValue);

	}

	public static float getPwrSrcCustomInFloat_VoltU3() {
		return Float.valueOf(PwrSrcCustomInFloat_VoltU3);

	}

	public static void setPwrSrcCustomInFloat_CurrentI1(String CurrentValue) {
		PwrSrcCustomInFloat_CurrentI1 = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcCustomInFloat_CurrentI1() {
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI1);

	}

	public static void setPwrSrcCustomInFloat_CurrentI2(String CurrentValue) {
		PwrSrcCustomInFloat_CurrentI2 = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcCustomInFloat_CurrentI2() {
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI2);

	}

	public static void setPwrSrcCustomInFloat_CurrentI3(String CurrentValue) {
		PwrSrcCustomInFloat_CurrentI3 = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcCustomInFloat_CurrentI3() {
		return Float.valueOf(PwrSrcCustomInFloat_CurrentI3);

	}

	/*
	 * public static void setPwrSrc_Phase1(String Value){
	 * PwrSrc_Phase1 = Float.valueOf(Value) ;
	 * 
	 * }
	 * 
	 * public static float getPwrSrc_Phase1(){
	 * return Float.valueOf(PwrSrc_Phase1) ;
	 * 
	 * }
	 * 
	 * public static void setPwrSrc_Phase2(String Value){
	 * PwrSrc_Phase2 = Float.valueOf(Value) ;
	 * 
	 * }
	 * 
	 * public static float getPwrSrc_Phase2(){
	 * return Float.valueOf(PwrSrc_Phase2) ;
	 * 
	 * }
	 * 
	 * public static void setPwrSrc_Phase3(String Value){
	 * PwrSrc_Phase3 = Float.valueOf(Value) ;
	 * 
	 * }
	 * 
	 * public static float getPwrSrc_Phase3(){
	 * return Float.valueOf(PwrSrc_Phase3) ;
	 * 
	 * }
	 */
	public static void setPwrSrcR_PhaseMaxCurrentInFloat(String CurrentValue) {
		PwrSrcR_PhaseMaxCurrentInFloat = Float.valueOf(CurrentValue);

	}

	public static float getPwrSrcR_PhaseMaxCurrentInFloat() {
		return Float.valueOf(PwrSrcR_PhaseMaxCurrentInFloat);

	}

	public void setLastSetRSS_Pulse_Rate(String pulseValue) {
		LastSetRSS_Pulse_Rate = pulseValue;
	}

	public String getLastSetRSS_Pulse_Rate() {
		return LastSetRSS_Pulse_Rate;

	}

	public void clearLastSetRSS_Pulse_Rate() {
		LastSetRSS_Pulse_Rate = "";
	}

	public void setRSSPulseRate(String pulseValue) {
		RSS_Pulse_Rate = pulseValue;
	}

	public String getRSSPulseRate() {
		return RSS_Pulse_Rate;

	}

	public static void setPhaseDegreeOutput(ArrayList<String> phasedegreeoutput) {
		PhaseDegreeOutput = phasedegreeoutput;
	}

	public static ArrayList<String> getPhaseDegreeOutput() {
		return PhaseDegreeOutput;

	}

	// public boolean DisplayPwrSrc_Init() {
	//
	// boolean status = false;
	//
	// if (ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE){
	//
	// ApplicationLauncher.logger.info("DisplayPwrSrc_Init: power source configured
	// as none connected");
	// return true;
	//
	// } else{
	//
	//
	//
	// if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
	// //status = BofaManager.enableSerialPortAndMonitor();
	// if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
	// status = BofaManager.enableSerialPortAndMonitorV2();
	// }else {
	// status = BofaManager.enableSerialPortAndMonitor();
	// }
	// }else{
	// status = serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
	// }
	// }
	// return status;
	// }

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public static String getPowerData(TextField refTextField) {
		return refTextField.getText();
	}

	public void StopReadingRefStdData() {
		ApplicationLauncher.logger.info("Stopped Reading Ref Data!");

		setRefStdReadDataFlag(false);
	}

	public void StopReadingLDU_ErrorReadData() {
		ApplicationLauncher.logger.info("Stopped Reading LDU_ErrorReadData!");

		setLDU_ReadDataFlag(false);
	}

	public String get_port_name(String src_type) {
		String port_name = "";
		try {
			port_name = DevicePortSetupController.get_device_settings(src_type).getString("port_name");
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("get_port_name :JSONException:" + e.getMessage());
		}
		return port_name;
	}

	public String get_baud_rate(String src_type) {
		String baud_rate = "";
		try {
			baud_rate = DevicePortSetupController.get_device_settings(src_type).getString("baud_rate");
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("get_baud_rate :JSONException:" + e.getMessage());
		}
		return baud_rate;
	}

	public void setAllPhaseOutput() {
		setR_PhaseOutputVoltage(getPwrSrcR_PhaseVoltInFloat());
		setR_PhaseOutputCurrent(getPwrSrcR_PhaseCurrentInFloat());
		setY_PhaseOutputVoltage(getPwrSrcY_PhaseVoltInFloat());
		setY_PhaseOutputCurrent(getPwrSrcY_PhaseCurrentInFloat());
		manipulateY_PhaseCurrentFor3PhaseDelta();
		setB_PhaseOutputVoltage(getPwrSrcB_PhaseVoltInFloat());
		setB_PhaseOutputCurrent(getPwrSrcB_PhaseCurrentInFloat());
	}

	public String manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String rated_volt) {
		String threePhaseDeltaVolt = rated_volt;
		String metertype = getDeployedEM_ModelType();
		if (metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)) {
			ApplicationLauncher.logger.info(
					"manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N: 3 Phase Delta : rated_volt: " + rated_volt);
			threePhaseDeltaVolt = String.valueOf(Float.parseFloat(rated_volt) / ConstantApp.SQRT_OF_THREE);
			ApplicationLauncher.logger
					.info("manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N: 3 Phase Delta : threePhaseDeltaVolt: "
							+ threePhaseDeltaVolt);
		}
		return threePhaseDeltaVolt;
	}

	public static String manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue(String selectedPhase,
			String powerSourceFeedBackCurrentRms) {

		ApplicationLauncher.logger.info("manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue :Entry");

		try {
			// String selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			String absoluteCurrent = "";// getR_PhaseOutputVoltage();
			if (selectedPhase.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)) {
			} else if (selectedPhase.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)) {
			} else if (selectedPhase.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)) {
			}

			return absoluteCurrent;

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("manipulatePowerSourceFeedbackCurrentRmsToAbsoluteValue : Exception :" + e.getMessage());
		}

		return "";

	}

	long calculateRSS_ConstantV42(String MUT_ConstInImpulsesPerKiloWattHour) {
		ApplicationLauncher.logger.info("calculateRSS_ConstantV42 :Entry");
		// float Volt1,float Volt2,float Volt3,float Current1,float Current2,float
		// Current3,float PF1,float PF2,float PF3
		float Volt1 = Float.parseFloat(getR_PhaseOutputVoltage());
		float Volt2 = Float.parseFloat(getY_PhaseOutputVoltage());
		float Volt3 = Float.parseFloat(getB_PhaseOutputVoltage());
		float Current1 = Float.parseFloat(getR_PhaseOutputCurrent());
		float Current2 = Float.parseFloat(getY_PhaseOutputCurrent());
		float Current3 = Float.parseFloat(getB_PhaseOutputCurrent());


		float PF_InDegree1 = DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
		float PF_InDegree2 = DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase();
		float PF_InDegree3 = DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase();

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
		if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
			ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Active");
			PF_Value1 = Float.parseFloat(String.format("%.02f", (float) Math.cos(Math.toRadians(PF_InDegree1))));
			PF_Value2 = Float.parseFloat(String.format("%.02f", (float) Math.cos(Math.toRadians(PF_InDegree2))));
			PF_Value3 = Float.parseFloat(String.format("%.02f", (float) Math.cos(Math.toRadians(PF_InDegree3))));
		} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
			ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Reactive");
			PF_Value1 = Float.parseFloat(String.format("%.02f", (float) Math.sin(Math.toRadians(PF_InDegree1))));
			PF_Value2 = Float.parseFloat(String.format("%.02f", (float) Math.sin(Math.toRadians(PF_InDegree2))));
			PF_Value3 = Float.parseFloat(String.format("%.02f", (float) Math.sin(Math.toRadians(PF_InDegree3))));
		}
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value1:" + PF_Value1);
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value2:" + PF_Value2);
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :PF_Value3:" + PF_Value3);
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power1:" + (Volt1 * Current1 * PF_Value1));
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power2:" + (Volt2 * Current2 * PF_Value2));
		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Power3:" + (Volt3 * Current3 * PF_Value3));
		double power = (Volt1 * Current1 * PF_Value1) + (Volt2 * Current2 * PF_Value2) + (Volt3 * Current3 * PF_Value3);

		ApplicationLauncher.logger.debug("calculateRSS_ConstantV42 :Total Power :" + power);
		// double RssConstantInWattHour =
		// (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000*Integer.parseInt(MUT_ConstInImpulsesPerKiloWattHour))/power;

		double RssConstantInWattHour = (ConstantRefStdRadiant.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ * 1000000 * 3600)
				/ power;
		// double RssConstantInWattHour =
		// (ConstantRefStd.REF_STD_MAX_OUTPUT_FREQ_IN_MEGA_HERTZ*1000000*3600)/power;

		long OutputConstantInWattHour = Math.abs((long) RssConstantInWattHour);

		ApplicationLauncher.logger.info("calculateRSS_Constantv42 in Watt Hour:" + OutputConstantInWattHour);


		return OutputConstantInWattHour;
	}

	public long RoundRefConst(String Input) {

		long OutputValue = 0;
		int Exponent = 0;
		ApplicationLauncher.logger.debug("RoundRefConst Input : " + Input);
		OutputValue = Long.parseLong(Input.substring(0, 4));

		// System.out.println("RoundRefConst OutputValue1 : " + OutputValue);

		Exponent = Integer.parseInt(Input.substring(5, 7));
		if (Exponent != 0) {
			OutputValue = OutputValue * ((long) (Math.pow(10, (double) Exponent)));
		}
		OutputValue = OutputValue / 1000;
		// System.out.println("RoundRefConst Exponent : " + (10^Exponent));
		ApplicationLauncher.logger.debug("RoundRefConst OutputValue2 : " + OutputValue);

		return OutputValue;
	}

	public void SetPulseConstantDataWithVoltageAndCurrent(float CurrentValue, float voltageValue) {

		ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: CurrentValue: " + CurrentValue);
		ApplicationLauncher.logger.debug("SetPulseConstantDataWithVoltageAndCurrent: voltageValue: " + voltageValue);
		setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);// setting default value
		if (ProcalFeatureEnable.REF_STD_CONST_CALCULATE) {

			setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit())));
			ApplicationLauncher.logger
					.debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: " + getRSSPulseRate());

			return;
		}

		if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_2) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_2: AMPS_below level 12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_2_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_3) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.debug("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_3: AMPS_below LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_3_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue > ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_LEVEL_4: AMPS_below_level 12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_ABOVE_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		} else if (voltageValue <= ConstantRefStdConfig.RSS_LTCT_VOLTAGE_THRESHOLD_IN_AMPS_LEVEL_4) {
			if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_1");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_1);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_2");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_2);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_3");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_3);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_4");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_4);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_5");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_5);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_6");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_6);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_7");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_7);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_8");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_8);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_9");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_9);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_10");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_10);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_11");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_11);

			} else if (CurrentValue > ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_ABOVE_LEVEL_12);

			} else {
				ApplicationLauncher.logger
						.info("SetPulseConstantDataWithVoltageAndCurrent: VOLTAGE_BELOW_LEVEL_4: AMPS_below_LEVEL_12");
				setRSSPulseRate(
						ConstantRefStdConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_VOLT_BELOW_OR_EQUAL_LEVEL_4_CURRENT_BELOW_OR_EQUAL_LEVEL_12);
				// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
				// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

			}
		}
		ApplicationLauncher.logger.debug(
				"SetPulseConstantDataWithVoltageAndCurrent: Setting LTCT Active Pulse Constant: " + getRSSPulseRate());
	}

	public void SetPulseConstantDataWithCurrent(float CurrentValue) {

		ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: CurrentValue: " + CurrentValue);
		setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);// setting default value
		if (ProcalFeatureEnable.REF_STD_CONST_CALCULATE) {

			setRSSPulseRate(String.valueOf(calculateRSS_ConstantV42(getDutImpulsesPerUnit())));
			ApplicationLauncher.logger
					.debug("SetPulseConstantDataWithCurrent: calculateRSS_ConstantV42: " + getRSSPulseRate());

			return;
		}

		if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
			// ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting
			// Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
			// DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
			if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);

				/*
				 * if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);
				 * 
				 * }else if(CurrentValue >=
				 * ConstantConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12){
				 * setRSSPulseRate(ConstantConfig.
				 * RSS_LTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);
				 * 
				 * }else{
				 * setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12)
				 * ;
				 * //ApplicationLauncher.logger.
				 * debug("SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant below 2 Amp: "
				 * +getRSSPulseRate());
				 * 
				 * }
				 */
				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting LTCT Active Pulse Constant: " + getRSSPulseRate());
			} else if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT);
				if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
					// HTCT Active Pulse Constant above 2 Amp: "+getRSSPulseRate());

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);

				} else {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
					// HTCT Active Pulse Constant below 2 Amp: "+getRSSPulseRate());

				}
				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant: " + getRSSPulseRate());

			}
		} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
			// ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting
			// reactive Pulse Constant:"+DisplayDataObj.RSS_ReactivePulseConstant);
			// DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ReactivePulseConstant);
			if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT);
				if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_LTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);

				} else {
					setRSSPulseRate(ConstantRefStdConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);

				}
				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting LTCT Reactive Pulse Constant: " + getRSSPulseRate());

			} else if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT);
				if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_1) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_1);
					// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
					// HTCT Reactive Pulse Constant above 2 Amp: "+getRSSPulseRate());

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_2) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_2);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_3) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_3);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_4) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_4);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_5) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_5);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_6) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_6);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_7) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_7);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_8) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_8);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_9) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_9);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_10) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_10);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_11) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_11);

				} else if (CurrentValue >= ConstantRefStdConfig.RSS_HTCT_CURRENT_THRESHOLD_IN_AMPS_LEVEL_12) {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_ABOVE_OR_EQUAL_LEVEL_12);

				} else {
					setRSSPulseRate(ConstantRefStdConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_BELOW_LEVEL_12);
					// ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: Setting
					// HTCT Reactive Pulse Constant below 2 Amp: "+getRSSPulseRate());
					//
				}
				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting HTCT Reactive Pulse Constant: " + getRSSPulseRate());
			}
		}

	}

	public ArrayList<String> CalculateLagLeadAngle(String input) {
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

		if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE) {
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

		/*
		 * int ReactiveImportExportSignAngle = 1;
		 * 
		 * if(getEnergyFlowMode().equals( ConstantPowerSource.IMPORT_MODE)){
		 * ReactiveImportExportSignAngle = 1;
		 * }else if(getEnergyFlowMode().equals(ConstantPowerSource.EXPORT_MODE)){
		 * ReactiveImportExportSignAngle = -1;
		 * }
		 */
		ArrayList<String> All_phases = new ArrayList<String>();
		if (lag_lead.equals(ConstantApp.PF_LAG)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));
				ApplicationLauncher.logger.info("CalculateLagLeadAngle : phasedegree:" + phasedegree);

			} catch (Exception e1) {
				// e1.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception1:"+
				// e1.getMessage());
				phase = input.substring(0, 1);
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

		} else if (lag_lead.equals(ConstantApp.PF_LEAD)) {
			try {
				lag_lead_value = Float.parseFloat(input.substring(0, input.length() - 1));
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}
				// phasedegree = -(phasedegree);

				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			} catch (Exception e2) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				lag_lead_value = Float.parseFloat(input.substring(2, input.length() - 1));
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE
							- (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE
							+ (Math.acos(lag_lead_value) * (180 / Math.PI));

				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = Math.acos(lag_lead_value) * (180 / Math.PI);
				}

				// phasedegree = - phasedegree;

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

			ApplicationLauncher.logger.info("phasedegree: " + phasedegree);
		} else {
			try {
				lag_lead_value = Float.parseFloat(input);
				phasedegree = 0.0;
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					// phasedegree = 0.0;
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {

					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}
				All_phases.add("All");
				All_phases.add(String.format("%.2f", phasedegree));

			} catch (Exception e3) {
				// e2.printStackTrace();
				// ApplicationLauncher.logger.info("calculatelag_lead :Exception2:"+
				// e2.getMessage());
				phase = input.substring(0, 1);
				phasedegree = 0.0;
				if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
					// phasedegree = 0.0;
					phasedegree = ConstantPowerSourceMte.POWER_SRC_COS_ACTIVE_UPF_ANGLE;
				} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {

					phasedegree = ConstantPowerSourceMte.POWER_SRC_SINE_REACTIVE_ZPF_ANGLE;
				} else {
					ApplicationLauncher.logger.info("Mismatch: Lead Lag ");
					phasedegree = 0.0;
				}

				// phasedegree = - phasedegree;

				if (phase.equals(FirstPhaseDisplayName)) {

					All_phases.add(FirstPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(SecondPhaseDisplayName)) {

					All_phases.add(SecondPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				}

				else if (phase.equals(ThirdPhaseDisplayName)) {

					All_phases.add(ThirdPhaseDisplayName);
					All_phases.add(String.format("%.2f", phasedegree));

				} else {

					All_phases.add("All");
					All_phases.add("0.0");
				}
			}

			ApplicationLauncher.logger.info("phasedegree: " + phasedegree);

		}
		ApplicationLauncher.logger.info("All_phases: " + All_phases);
		setPhaseDegreeOutput(All_phases);
		return All_phases;
	}

	public void manipulateY_PhaseCurrentFor3PhaseDelta() {
		ApplicationLauncher.logger.debug("manipulateY_PhaseCurrentFor3PhaseDelta: Entry");
		String metertype = getDeployedEM_ModelType();
		if (metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)) {
			ApplicationLauncher.logger.debug("manipulateY_PhaseCurrentFor3PhaseDelta: Y current to Zero");
			setY_PhaseOutputCurrent(0f);
		}
	}

	public void setAllPhaseParameters(float rated_voltA, float rated_voltB,
			float rated_voltC, float rated_current,
			ArrayList<String> phasedegree) {

		String FirstPhaseDisplayName = "A";
		String SecondPhaseDisplayName = "B";
		String ThirdPhaseDisplayName = "C";
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltA: " + rated_voltA);
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltB: " + rated_voltB);
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_voltC: " + rated_voltC);
		ApplicationLauncher.logger.debug("setAllPhaseParameters: rated_current: " + rated_current);

		if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE) {
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

		// String metertype = getDeployedEM_ModelType();
		String phase = phasedegree.get(0);
		setR_PhaseOutputVoltage(rated_voltA);
		setY_PhaseOutputVoltage(rated_voltB);
		setB_PhaseOutputVoltage(rated_voltC);
		if (phase.equals("All")) {
			ApplicationLauncher.logger.debug("setAllPhaseParameters: All");
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
			manipulateY_PhaseCurrentFor3PhaseDelta();

			// if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE_DELTA)){
			// ApplicationLauncher.logger.info("setAllPhaseParameters: 3 Phase Delta
			// :setting y current zero" );
			// setY_PhaseOutputCurrent(0f);

			// }
		} else if (phase.equals(FirstPhaseDisplayName)) {
			ApplicationLauncher.logger.debug("setAllPhaseParameters: First Phase");
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree.get(1));
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
		} else if (phase.equals(SecondPhaseDisplayName)) {
			ApplicationLauncher.logger.debug("setAllPhaseParameters: Second Phase");
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputCurrent(rated_current);
			set_PwrSrcY_PhaseDegreePhase(phasedegree.get(1));
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
			manipulateY_PhaseCurrentFor3PhaseDelta();
		} else if (phase.equals(ThirdPhaseDisplayName)) {
			ApplicationLauncher.logger.debug("setAllPhaseParameters: Third Phase");
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree.get(1));
		} else {
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
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getR_PhaseOutputVoltage: " + getR_PhaseOutputVoltage());
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getY_PhaseOutputVoltage: " + getY_PhaseOutputVoltage());
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getB_PhaseOutputVoltage: " + getB_PhaseOutputVoltage());
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getR_PhaseOutputCurrent: " + getR_PhaseOutputCurrent());
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getY_PhaseOutputCurrent: " + getY_PhaseOutputCurrent());
		ApplicationLauncher.logger
				.debug("setAllPhaseParameters: getB_PhaseOutputCurrent: " + getB_PhaseOutputCurrent());
	}

	public void setAllPhaseParametersForVoltUnbalance(float rated_volt1, float rated_volt2,
			float rated_volt3, float rated_current, String volt_phase, String phasedegree) {
		String All_Phase = "ABC";
		String FirstPhase = "A";
		String SecondPhase = "B";
		String ThirdPhase = "C";
		String FirstAndSecondPhase = "AB";
		String SecondAndThirdPhase = "BC";
		String FirstAndThirdPhase = "AC";
		if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE) {
			All_Phase = ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME
					+ ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndSecondPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			SecondAndThirdPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndThirdPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}
		if (volt_phase.equals(All_Phase)) {
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
		} else if (volt_phase.equals(FirstPhase)) {
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(0.0f);
			set_PwrSrcB_PhaseDegreePhase("0.0");
		} else if (volt_phase.equals(SecondPhase)) {
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
		} else if (volt_phase.equals(ThirdPhase)) {
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(0.0f);
			set_PwrSrcR_PhaseDegreePhase("0.0");
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
		} else if (volt_phase.equals(FirstAndSecondPhase)) {
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
		} else if (volt_phase.equals(SecondAndThirdPhase)) {
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
		} else if (volt_phase.equals(FirstAndThirdPhase)) {
			setR_PhaseOutputVoltage(rated_volt1);
			setR_PhaseOutputCurrent(rated_current);
			set_PwrSrcR_PhaseDegreePhase(phasedegree);
			setY_PhaseOutputVoltage(rated_volt2);
			setY_PhaseOutputCurrent(0.0f);
			set_PwrSrcY_PhaseDegreePhase("0.0");
			setB_PhaseOutputVoltage(rated_volt3);
			setB_PhaseOutputCurrent(rated_current);
			set_PwrSrcB_PhaseDegreePhase(phasedegree);
		} else {
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

	public ArrayList<String> ExtractI_PF_From_TP_Name(String testcasename) {
		String testname_wo_type = testcasename.substring(testcasename.indexOf("-") + 1);
		ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: testname_wo_type : " + testname_wo_type);
		ArrayList<String> I_PF_values = new ArrayList<String>();
		if (testname_wo_type.contains("U")) {
			String[] test_params = testname_wo_type.split("-");
			String lag_lead = test_params[1];
			String selectedRateOfCurrent = test_params[2];
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: lag_lead : " + lag_lead);
			ApplicationLauncher.logger
					.info("ExtractI_PF_From_TP_Name: selectedRateOfCurrent : " + selectedRateOfCurrent);
			I_PF_values.add(lag_lead);
			I_PF_values.add(selectedRateOfCurrent);
		} else {
			String[] test_params = testname_wo_type.split("-");
			String lag_lead = test_params[0];
			String selectedRateOfCurrent = test_params[1];
			ApplicationLauncher.logger.info("ExtractI_PF_From_TP_Name: lag_lead : " + lag_lead);
			ApplicationLauncher.logger
					.info("ExtractI_PF_From_TP_Name: selectedRateOfCurrent : " + selectedRateOfCurrent);
			I_PF_values.add(lag_lead);
			I_PF_values.add(selectedRateOfCurrent);
		}
		return I_PF_values;
	}

	public String Extract_V_phase_From_TP_Name(String testcasename) {
		String testname_wo_type = testcasename.substring(testcasename.indexOf("-") + 1);
		ApplicationLauncher.logger.info("Extract_V_phase_From_TP_Name: testname_wo_type : " + testname_wo_type);
		String volt_phase = "";
		if (testname_wo_type.contains("U")) {
			testname_wo_type.split("-");
			String[] volt_split = testname_wo_type.split(":");
			volt_phase = volt_split[0];
			ApplicationLauncher.logger.info("Extract_V_phase_From_TP_Name: testname_wo_type : " + volt_phase);
		}
		return volt_phase;
	}

	public float CalculateSTACurrent() {
		ApplicationLauncher.logger.info("CalculateSTACurrent :Entry");
		float RatedCurrent = DeviceDataManagerController.getPwrSrcR_PhaseCurrentInFloat();// "240.0";
		float CurrentPercentage = DeviceDataManagerController.getPercentageOfCurrent();

		float OutputCurrent = RatedCurrent * CurrentPercentage / 100;
		ApplicationLauncher.logger.info("CalculateSTACurrent: Output Current1: " + OutputCurrent);
		// String op_current = String.format("%.02f", OutputCurrent);
		String op_current = "";
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			op_current = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, OutputCurrent);
			ApplicationLauncher.logger.info("Output Current2: " + OutputCurrent);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current3: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger.info("Output Current4: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current5: " + OutputCurrent);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			op_current = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(OutputCurrent);
			ApplicationLauncher.logger.info("Output Current5A: " + OutputCurrent);
		} else {
			if (OutputCurrent < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current6: " + OutputCurrent);
			} else {
				if (OutputCurrent < 1.0f) {
					OutputCurrent = Math.round(OutputCurrent);
					ApplicationLauncher.logger.info("Output Current7: " + OutputCurrent);
				}
				op_current = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, OutputCurrent);
				ApplicationLauncher.logger.info("Output Current8: " + OutputCurrent);
			}
		}

		OutputCurrent = Float.parseFloat(op_current);
		ApplicationLauncher.logger.info("Output Current6: " + OutputCurrent);
		return OutputCurrent;
	}

	public static void setR_PhaseOutputVoltage(Float Value) {
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputVoltage = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputVoltage = ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format(Value);
		} else {
			R_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}

	}

	public static void setR_PhaseOutputVoltageStr(String Value) {
		R_PhaseOutputVoltage = Value;

	}

	final public static String getR_PhaseOutputVoltage() {
		return R_PhaseOutputVoltage;
	}

	public static void setY_PhaseOutputVoltage(Float Value) {
		// Y_PhaseOutputVoltage=String.format(ConstantMtePowerSource.VOLTAGE_RESOLUTION,
		// Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			Y_PhaseOutputVoltage = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			Y_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			Y_PhaseOutputVoltage = ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format(Value);
		} else {
			Y_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}
	}

	public static void setY_PhaseOutputVoltageStr(String Value) {
		Y_PhaseOutputVoltage = Value;

	}

	public static void setB_PhaseOutputVoltage(Float Value) {
		// B_PhaseOutputVoltage=String.format(ConstantMtePowerSource.VOLTAGE_RESOLUTION,
		// Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			B_PhaseOutputVoltage = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			B_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			B_PhaseOutputVoltage = ConstantPowerSourceBofa.BOFA_VOLTAGE_RESOLUTION.format(Value);
		} else {
			B_PhaseOutputVoltage = String.format(ConstantPowerSourceLscs.VOLTAGE_RESOLUTION, Value);
		}
	}

	public static void setB_PhaseOutputVoltageStr(String Value) {
		B_PhaseOutputVoltage = Value;

	}

	final public static String getB_PhaseOutputVoltage() {
		return B_PhaseOutputVoltage;
	}

	final public static String getY_PhaseOutputVoltage() {
		return Y_PhaseOutputVoltage;
	}

	public static void setLDU_STATimeDurationFormat(Integer STATimeInSec) {
		ApplicationLauncher.logger.info("setLDU_STATimeDurationFormat :Entry");
		int sec = (STATimeInSec % 60);
		// int min = ((STATimeInSec / 60)%60);
		int min = STATimeInSec / 60;
		if (min > 99) {
			min = 0;
		}
		STATimeDuration = String.format("%02d", min) + String.format("%02d", sec);

	}

	public static void setLscsLDU_STATimeDurationFormat(Integer STATimeInSec) {
		ApplicationLauncher.logger.info("setLscsLDU_STATimeDurationFormat :Entry");
		int sec = (STATimeInSec % 60);
		// int min = ((STATimeInSec / 60)%60);
		int min = STATimeInSec / 60;
		if (min > ConstantLduLscs.MAXIMUM_ALLOWED_MINUTES) {
			min = 0;
		}
		STATimeDuration = String.format("%03d", min) + String.format("%02d", sec);

	}

	public String getLDU_STATimeDurationFormat() {

		return DeviceDataManagerController.STATimeDuration;
	}

	public String getLscsLDU_STATimeDurationFormat() {

		return DeviceDataManagerController.STATimeDuration;
	}

	public static void setR_PhaseOutputCurrent(Float Value) {
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputCurrent = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				R_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				R_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputCurrent = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(Value);
		} else {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				R_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				R_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}

	public static void setR_PhaseOutputCurrentStr(String Value) {
		R_PhaseOutputCurrent = Value;
	}

	final public static String getR_PhaseOutputCurrent() {
		return R_PhaseOutputCurrent;
	}

	public static void setY_PhaseOutputCurrent(Float Value) {
		// Y_PhaseOutputCurrent=String.format(ConstantMtePowerSource.CURRENT_RESOLUTION,
		// Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			Y_PhaseOutputCurrent = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				Y_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				Y_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			Y_PhaseOutputCurrent = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(Value);
		} else {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				Y_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				Y_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}

	public static void setY_PhaseOutputCurrentStr(String Value) {
		Y_PhaseOutputCurrent = Value;
	}

	final public static String getY_PhaseOutputCurrent() {
		return Y_PhaseOutputCurrent;
	}

	public static void setB_PhaseOutputCurrent(Float Value) {
		// B_PhaseOutputCurrent=String.format(ConstantMtePowerSource.CURRENT_RESOLUTION,
		// Value);
		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			B_PhaseOutputCurrent = String.format(ConstantPowerSourceMte.CURRENT_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				B_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				B_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		} else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
			B_PhaseOutputCurrent = ConstantPowerSourceBofa.BOFA_CURRENT_RESOLUTION.format(Value);
		} else {
			if (Value < ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD) {
				B_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_LOW, Value);
			} else {
				B_PhaseOutputCurrent = String.format(ConstantPowerSourceLscs.CURRENT_RESOLUTION_HIGH, Value);
			}
		}
	}

	public static void setB_PhaseOutputCurrentStr(String Value) {
		B_PhaseOutputCurrent = Value;
	}

	final public static String getB_PhaseOutputCurrent() {
		return B_PhaseOutputCurrent;
	}

	public static void setR_PhaseOutputPhase(Float Value) {

		if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputPhase = String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
		} else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) {
			R_PhaseOutputPhase = String.format(ConstantPowerSourceLscs.PHASE_RESOLUTION, Value);
		}
	}

	final public String getR_PhaseOutputPhase() {
		return R_PhaseOutputPhase;
	}

	public static void setY_PhaseOutputPhase(Float Value) {
		Y_PhaseOutputPhase = String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
	}

	final public String getY_PhaseOutputPhase() {
		return Y_PhaseOutputPhase;
	}

	public static void setB_PhaseOutputPhase(Float Value) {
		B_PhaseOutputPhase = String.format(ConstantPowerSourceMte.PHASE_RESOLUTION, Value);
	}

	final public String getB_PhaseOutputPhase() {
		return B_PhaseOutputPhase;
	}

	public static void setVolt_Unbalanced_PowerOn(boolean Value) {
		Volt_Unbalanced_PowerOn = Value;
	}

	final public boolean getVolt_Unbalanced_PowerOn() {
		return Volt_Unbalanced_PowerOn;
	}

	public static void setPowerSrcOnTimerValue(Integer ValueInSec) {
		PowerSrcOnTimerInSec = ValueInSec;
		ApplicationLauncher.logger.info("ValueInSec: " + ValueInSec);
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
		ApplicationLauncher.logger.info("setRefStdReadDataFlag:" + bRefStdReadData);
	}

	public static boolean getRefStdReadDataFlag() {

		return bRefStdReadData;
	}

	public static void setAllPortInitSuccess(boolean value) {

		AllPortInitSuccess = value;
		ApplicationLauncher.logger.info("setAllPortInitSuccess:" + AllPortInitSuccess);
	}

	public static boolean getAllPortInitSuccess() {

		return AllPortInitSuccess;
	}

	public static ArrayList<Integer> getDevicesToBeRead() {
		return DevicesMounted;
	}

	public static void setDevicesToBeRead(ArrayList<Integer> values) {
		DevicesMounted = values;
	}

	public float CalculateVoltage(float RatedVolt, float VoltPercentage) {
		ApplicationLauncher.logger.info("CalculateVoltage :Entry");
		float OutputVolt = RatedVolt * VoltPercentage / 100;
		ApplicationLauncher.logger.info("Parse Voltage" + Float.valueOf(RatedVolt));
		ApplicationLauncher.logger.info("Parse output Voltage" + Float.valueOf(VoltPercentage));
		ApplicationLauncher.logger.info("Output Voltage" + OutputVolt);
		// String op_volt = String.format("%.02f", OutputVolt);
		String op_volt = String.format(ConstantPowerSourceMte.VOLTAGE_RESOLUTION, OutputVolt);
		OutputVolt = Float.parseFloat(op_volt);
		return OutputVolt;
	}

	public void setLDU_CreepTimeDurationFormat(Integer CreepTimeInSec) {
		ApplicationLauncher.logger.info("setLDU_CreepTimeDurationFormat :Entry");
		int sec = (CreepTimeInSec % 60);
		// int min = ((CreepTimeInSec / 60)%60);
		int min = CreepTimeInSec / 60;
		if (min > 99) {
			min = 0;
		}

		CreepTimeDuration = String.format("%02d", min) + String.format("%02d", sec);
	}

	public String getLDU_CreepTimeDurationFormat() {

		return DeviceDataManagerController.CreepTimeDuration;
	}

	public JSONObject getSkipReadingForAllDevices(ArrayList<Integer> rack_id, int skip_reading) {
		JSONObject jobj = new JSONObject();
		String rack = "";
		try {
			for (int i = 0; i < rack_id.size(); i++) {
				rack = Integer.toString(rack_id.get(i));
				jobj.put(rack, skip_reading);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getSkipReadingForAllDevices :JSONException :" + e.getMessage());
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

	public static String getAbsoluteVoltage(String selectedPhase, String userInputVoltageValue,
			String feedbackVoltageRmsValue,
			Double gainValue, Double offsetValue) {
		String voltageRmsValueStr = "00000";
		try {
			float inpVoltage = Float.parseFloat(userInputVoltageValue);
			ApplicationLauncher.logger.debug("getAbsoluteVoltage: inpVoltage: " + inpVoltage);
			if (inpVoltage <= 0.0f) {
				return voltageRmsValueStr;
			}
			// String feedbackRmsValueStr =
			// String.valueOf((Double.valueOf(feedbackVoltageRmsValue)));
			// ApplicationLauncher.logger.debug("getAbsoluteVoltage:
			// feedbackVoltageRmsValue: "+ feedbackVoltageRmsValue);
			// ApplicationLauncher.logger.debug("getAbsoluteVoltage: feedbackRmsValueStr: "+
			// feedbackRmsValueStr);
			if (Double.valueOf(feedbackVoltageRmsValue) > 0.0f) {
				voltageRmsValueStr = String
						.valueOf((Double.valueOf(feedbackVoltageRmsValue) - offsetValue) / gainValue);
				ApplicationLauncher.logger.debug("getAbsoluteVoltage: AbsoluteVoltageValue1: " + voltageRmsValueStr);
				return voltageRmsValueStr;
			}

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getAbsoluteVoltage: Exception: " + e.getMessage());
		}

		return voltageRmsValueStr;
	}

	public static String getAbsoluteCurrent(String selectedPhase, String userInputCurrentValue,
			String feedbackCurrentRmsValue,
			Double gainValue, Double offsetValue) {
		String currentRmsValueStr = "00000";
		try {
			float inpCurrent = Float.parseFloat(userInputCurrentValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: inpCurrent: " + inpCurrent);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: feedbackCurrentRmsValue: " + feedbackCurrentRmsValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: gainValue: " + gainValue);
			ApplicationLauncher.logger.debug("getAbsoluteCurrent: offsetValue: " + offsetValue);
			if (inpCurrent <= 0.0f) {
				return currentRmsValueStr;
			}
			if (Double.valueOf(feedbackCurrentRmsValue) > 0.0f) {
				currentRmsValueStr = String
						.valueOf((Double.valueOf(feedbackCurrentRmsValue) - offsetValue) / gainValue);
				ApplicationLauncher.logger.debug("getAbsoluteCurrent: AbsoluteCurrentValue1: " + currentRmsValueStr);
				return currentRmsValueStr;
			}

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getAbsoluteCurrent: Exception: " + e.getMessage());
		}

		return currentRmsValueStr;
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
		HashMap<Integer, String> errorDataInnerMap = lduErrorDataHashMap2d.get(lduAddressOuterKey);
		if (errorDataInnerMap == null) {
			errorDataInnerMap = new HashMap<Integer, String>();
			lduErrorDataHashMap2d.put(lduAddressOuterKey, errorDataInnerMap);
		}
		errorDataInnerMap.put(readingIndexInnerKey, errorValue);
	}

	public String getLduErrorDataHashMap2d(Integer lduAddressOuterKey, Integer readingIndexInnerKey) {
		HashMap<Integer, String> errorDataInnerMap = lduErrorDataHashMap2d.get(lduAddressOuterKey);
		if (errorDataInnerMap == null) {
			return null;
		}

		return errorDataInnerMap.get(readingIndexInnerKey);
	}

	public String getAverageLduErrorDataHashMap2d(Integer lduAddressOuterKey) {
		HashMap<Integer, String> errorDataInnerMap = lduErrorDataHashMap2d.get(lduAddressOuterKey);
		if (errorDataInnerMap == null) {
			return null;
		}

		Collection<String> errorDataList = errorDataInnerMap.values();
		/*
		 * for(int i = 0; i < errorDataList.size(); i++ ){
		 * ApplicationLauncher.logger.
		 * debug("getAverageLduErrorDataHashMap2d : errorDataList[0]; " +
		 * errorDataList.iterator().next());
		 * //if(errorDataList[0])
		 * }
		 */
		ApplicationLauncher.logger
				.debug("getAverageLduErrorDataHashMap2d : errorDataList: size : " + errorDataList.size());
		Float errorValue = 0.0f;
		Float averageValue = 0.0f;
		String averageValueStr = "";
		String data = "";
		for (Iterator i = errorDataList.iterator(); i.hasNext();) {
			// System.out.println(i.next());
			data = (String) i.next();
			ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : errorDataList: " + data);
			try {

				Float.parseFloat(data);// added for exception
				errorValue = errorValue + Float.parseFloat(data);
				ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : sum of errorValue: " + errorValue);

			} catch (Exception E) {
				E.printStackTrace();
				ApplicationLauncher.logger.error("getAverageLduErrorDataHashMap2d Exception :" + E.getMessage());
			}

		}
		if (errorDataList.size() > 0) {
			averageValue = errorValue / errorDataList.size();
			ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValue: " + averageValue);
		}
		if (averageValue >= 0.0f) {
			averageValueStr = "+" + String.format("%.3f", averageValue);
			ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValueStr1: " + averageValueStr);
		} else if (averageValue < 0.0f) {
			averageValueStr = String.format("%.3f", averageValue);
			ApplicationLauncher.logger.debug("getAverageLduErrorDataHashMap2d : averageValueStr2: " + averageValueStr);
		}
		return averageValueStr;
		// return errorDataInnerMap.values();

		// return errorDataInnerMap.get(readingIndexInnerKey);
	}

	public void resetLduErrorDataHashMap2d() {
		lduErrorDataHashMap2d = new HashMap<Integer, HashMap<Integer, String>>();
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

	/********** 9 ***/

	public static void setDUT9_ReadDataFlag(boolean value) {
		bDUT9_ReadData = value;
	}

	public static boolean getDUT9_ReadDataFlag() {
		return bDUT9_ReadData;
	}

	/********** 10 ***/

	public static void setDUT10_ReadDataFlag(boolean value) {
		bDUT10_ReadData = value;
	}

	public static boolean getDUT10_ReadDataFlag() {
		return bDUT10_ReadData;
	}

	/********** 11 ***/

	public static void setDUT11_ReadDataFlag(boolean value) {
		bDUT11_ReadData = value;
	}

	public static boolean getDUT11_ReadDataFlag() {
		return bDUT11_ReadData;
	}

	/********** 11 ***/

	public static void setDUT12_ReadDataFlag(boolean value) {
		bDUT12_ReadData = value;
	}

	public static boolean getDUT12_ReadDataFlag() {
		return bDUT12_ReadData;
	}

	/********** 13 ***/

	public static void setDUT13_ReadDataFlag(boolean value) {
		bDUT13_ReadData = value;
	}

	public static boolean getDUT13_ReadDataFlag() {
		return bDUT13_ReadData;
	}

	/********** 13 ***/

	public static void setDUT14_ReadDataFlag(boolean value) {
		bDUT14_ReadData = value;
	}

	public static boolean getDUT14_ReadDataFlag() {
		return bDUT14_ReadData;
	}

	/********** 14 ***/

	public static void setDUT15_ReadDataFlag(boolean value) {
		bDUT15_ReadData = value;
	}

	public static boolean getDUT15_ReadDataFlag() {
		return bDUT15_ReadData;
	}

	/********** 16 ***/

	public static void setDUT16_ReadDataFlag(boolean value) {
		bDUT16_ReadData = value;
	}

	public static boolean getDUT16_ReadDataFlag() {
		return bDUT16_ReadData;
	}

	/********** 16 ***/

	public static void setDUT17_ReadDataFlag(boolean value) {
		bDUT17_ReadData = value;
	}

	public static boolean getDUT17_ReadDataFlag() {
		return bDUT17_ReadData;
	}

	/********** 18 ***/

	public static void setDUT18_ReadDataFlag(boolean value) {
		bDUT18_ReadData = value;
	}

	public static boolean getDUT18_ReadDataFlag() {
		return bDUT18_ReadData;
	}

	/********** 19 ***/

	public static void setDUT19_ReadDataFlag(boolean value) {
		bDUT19_ReadData = value;
	}

	public static boolean getDUT19_ReadDataFlag() {
		return bDUT19_ReadData;
	}

	/********** 20 ***/

	public static void setDUT20_ReadDataFlag(boolean value) {
		bDUT20_ReadData = value;
	}

	public static boolean getDUT20_ReadDataFlag() {
		return bDUT20_ReadData;
	}

	/********** 21 ***/

	public static void setDUT21_ReadDataFlag(boolean value) {
		bDUT21_ReadData = value;
	}

	public static boolean getDUT21_ReadDataFlag() {
		return bDUT21_ReadData;
	}

	/********** 22 ***/

	public static void setDUT22_ReadDataFlag(boolean value) {
		bDUT22_ReadData = value;
	}

	public static boolean getDUT22_ReadDataFlag() {
		return bDUT22_ReadData;
	}

	/********** 23 ***/

	public static void setDUT23_ReadDataFlag(boolean value) {
		bDUT23_ReadData = value;
	}

	public static boolean getDUT23_ReadDataFlag() {
		return bDUT23_ReadData;
	}

	/********** 24 ***/

	public static void setDUT24_ReadDataFlag(boolean value) {
		bDUT24_ReadData = value;
	}

	public static boolean getDUT24_ReadDataFlag() {
		return bDUT24_ReadData;
	}

	/********** 25 ***/

	public static void setDUT25_ReadDataFlag(boolean value) {
		bDUT25_ReadData = value;
	}

	public static boolean getDUT25_ReadDataFlag() {
		return bDUT25_ReadData;
	}

	/********** 26 ***/

	public static void setDUT26_ReadDataFlag(boolean value) {
		bDUT26_ReadData = value;
	}

	public static boolean getDUT26_ReadDataFlag() {
		return bDUT26_ReadData;
	}

	/********** 27 ***/

	public static void setDUT27_ReadDataFlag(boolean value) {
		bDUT27_ReadData = value;
	}

	public static boolean getDUT27_ReadDataFlag() {
		return bDUT27_ReadData;
	}

	/********** 28 ***/

	public static void setDUT28_ReadDataFlag(boolean value) {
		bDUT28_ReadData = value;
	}

	public static boolean getDUT28_ReadDataFlag() {
		return bDUT28_ReadData;
	}

	/********** 29 ***/

	public static void setDUT29_ReadDataFlag(boolean value) {
		bDUT29_ReadData = value;
	}

	public static boolean getDUT29_ReadDataFlag() {
		return bDUT29_ReadData;
	}

	/********** 30 ***/

	public static void setDUT30_ReadDataFlag(boolean value) {
		bDUT30_ReadData = value;
	}

	public static boolean getDUT30_ReadDataFlag() {
		return bDUT30_ReadData;
	}

	/********** 30 ***/

	public static void setDUT31_ReadDataFlag(boolean value) {
		bDUT31_ReadData = value;
	}

	public static boolean getDUT31_ReadDataFlag() {
		return bDUT31_ReadData;
	}

	/********** 32 ***/

	public static void setDUT32_ReadDataFlag(boolean value) {
		bDUT32_ReadData = value;
	}

	public static boolean getDUT32_ReadDataFlag() {
		return bDUT32_ReadData;
	}

	/********** 33 ***/

	public static void setDUT33_ReadDataFlag(boolean value) {
		bDUT33_ReadData = value;
	}

	public static boolean getDUT33_ReadDataFlag() {
		return bDUT33_ReadData;
	}

	/********** 34 ***/

	public static void setDUT34_ReadDataFlag(boolean value) {
		bDUT34_ReadData = value;
	}

	public static boolean getDUT34_ReadDataFlag() {
		return bDUT34_ReadData;
	}

	/********** 35 ***/

	public static void setDUT35_ReadDataFlag(boolean value) {
		bDUT35_ReadData = value;
	}

	public static boolean getDUT35_ReadDataFlag() {
		return bDUT35_ReadData;
	}

	/********** 36 ***/

	public static void setDUT36_ReadDataFlag(boolean value) {
		bDUT36_ReadData = value;
	}

	public static boolean getDUT36_ReadDataFlag() {
		return bDUT36_ReadData;
	}

	/********** 37 ***/

	public static void setDUT37_ReadDataFlag(boolean value) {
		bDUT37_ReadData = value;
	}

	public static boolean getDUT37_ReadDataFlag() {
		return bDUT37_ReadData;
	}

	/********** 38 ***/

	public static void setDUT38_ReadDataFlag(boolean value) {
		bDUT38_ReadData = value;
	}

	public static boolean getDUT38_ReadDataFlag() {
		return bDUT38_ReadData;
	}

	/********** 39 ***/

	public static void setDUT39_ReadDataFlag(boolean value) {
		bDUT39_ReadData = value;
	}

	public static boolean getDUT39_ReadDataFlag() {
		return bDUT39_ReadData;
	}

	/********** 40 ***/

	public static void setDUT40_ReadDataFlag(boolean value) {
		bDUT40_ReadData = value;
	}

	public static boolean getDUT40_ReadDataFlag() {
		return bDUT40_ReadData;
	}

	/********** 41 ***/

	public static void setDUT41_ReadDataFlag(boolean value) {
		bDUT41_ReadData = value;
	}

	public static boolean getDUT41_ReadDataFlag() {
		return bDUT41_ReadData;
	}

	/********** 42 ***/

	public static void setDUT42_ReadDataFlag(boolean value) {
		bDUT42_ReadData = value;
	}

	public static boolean getDUT42_ReadDataFlag() {
		return bDUT42_ReadData;
	}

	/********** 43 ***/

	public static void setDUT43_ReadDataFlag(boolean value) {
		bDUT43_ReadData = value;
	}

	public static boolean getDUT43_ReadDataFlag() {
		return bDUT43_ReadData;
	}

	/********** 43 ***/

	public static void setDUT44_ReadDataFlag(boolean value) {
		bDUT44_ReadData = value;
	}

	public static boolean getDUT44_ReadDataFlag() {
		return bDUT44_ReadData;
	}

	/********** 45 ***/

	public static void setDUT45_ReadDataFlag(boolean value) {
		bDUT45_ReadData = value;
	}

	public static boolean getDUT45_ReadDataFlag() {
		return bDUT45_ReadData;
	}

	/********** 46 ***/

	public static void setDUT46_ReadDataFlag(boolean value) {
		bDUT46_ReadData = value;
	}

	public static boolean getDUT46_ReadDataFlag() {
		return bDUT46_ReadData;
	}

	/********** 47 ***/

	public static void setDUT47_ReadDataFlag(boolean value) {
		bDUT47_ReadData = value;
	}

	public static boolean getDUT47_ReadDataFlag() {
		return bDUT47_ReadData;
	}

	/********** 48 ***/

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

	public static String getDutSerialNumberMap(int lduAddress) {
		String serialNo = "";
		try {
			if (dutSerialNumberMap.containsKey(lduAddress)) {
				serialNo = dutSerialNumberMap.get(lduAddress);
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getDutSerialNumber : Exception :" + e.getMessage());
		}
		return serialNo;
	}

	public boolean isMeterIdExistInBlackList(int lduAddress) {
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: Entry");
		if (ConstantAppConfig.METER_ID_BLACKLIST_VALIDATION) {
			if (ConstantAppConfig.METER_ID_BLACKLISTED_LIST.size() > 0) {
				String readMeterId = getDutSerialNumberMap(lduAddress);
				ApplicationLauncher.logger.debug("isMeterIdExistInBlackList: readMeterId: " + readMeterId);
				if (ConstantAppConfig.METER_ID_BLACKLISTED_LIST.contains(readMeterId)) {
					ApplicationLauncher.logger
							.debug("isMeterIdExistInBlackList: meter id found in black list: " + readMeterId);
					status = true;
					return status;
				}
			}

		}

		return status;
	}

	public boolean isMeterIdExistInWhiteList(int lduAddress) {
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: Entry");
		if (ConstantAppConfig.METER_ID_WHITELIST_VALIDATION) {
			if (ConstantAppConfig.METER_ID_WHITELISTED_LIST.size() > 0) {
				String readMeterId = getDutSerialNumberMap(lduAddress);
				ApplicationLauncher.logger.debug("isMeterIdExistInWhiteList: readMeterId: " + readMeterId);
				if (ConstantAppConfig.METER_ID_WHITELISTED_LIST.contains(readMeterId)) {
					ApplicationLauncher.logger
							.debug("isMeterIdExistInWhiteList: meter id found in black list: " + readMeterId);
					status = true;
					return status;
				}
			}

		}

		return status;
	}

	public boolean isMeterIdExistInAlreadyTestedList(int lduAddress) {
		boolean status = false;
		ApplicationLauncher.logger.debug("isMeterIdExistInAlreadyTestedList: Entry");
		if (ConstantAppConfig.METER_ID_VALIDATE_ALREADY_TESTED) {
			String readMeterId = getDutSerialNumberMap(lduAddress);
			status = MySQL_Controller.sp_ValidateDutAlreadyTested(readMeterId,
					ConstantReport.RESULT_DATA_TYPE_DEVICE_NAME);
			if (status) {
				ApplicationLauncher.logger
						.debug("isMeterIdExistInAlreadyTestedList: meter id found in calibrated list: " + readMeterId);
				return status;
			}
		}

		return status;
	}

	public static String getLastSetPowerSourceFrequency() {
		return lastSetPowerSourceFrequency;
	}

	public static void setLastSetPowerSourceFrequency(String lastSetPowerSourceFreq) {
		ApplicationLauncher.logger
				.debug("setLastSetPowerSourceFrequency: lastSetPowerSourceFreq: " + lastSetPowerSourceFreq);
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

		return false;
	}

	public boolean powerSource_ComInitV2() {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(PowerSrcCommPortID, PwrSrcCommBaudRate);// HVCI_CommPortID,
																													// HVCI_CommBaudRate);
		return status;
	}

	public boolean powerSource_ComInitV2_1(String selectedComPortId, String selectedBaudRate) {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(selectedComPortId, selectedBaudRate);// HVCI_CommPortID,
																												// HVCI_CommBaudRate);
		return status;
	}

	/*
	 * public boolean dutCmd_ComInitV2_1(String selectedComPortId,String
	 * selectedBaudRate) {
	 * 
	 * boolean status =
	 * serialPortManagerDutCmd_V2.dutComInitV2(selectedComPortId,selectedBaudRate);/
	 * /HVCI_CommPortID, HVCI_CommBaudRate);
	 * return status;
	 * }
	 */

	public boolean refStd_ComInit() {

		return false;
	}

	public boolean refStd_ComInitV2() {

		boolean status = serialPortManagerRefStd_V2.refStdComInitV2(RefStdCommPortID, RefStdCommBaudRate);// HVCI_CommPortID,
																											// HVCI_CommBaudRate);
		return status;
	}

	public boolean refStd_ComInitV2_1(String selectedComPortId, String selectedBaudRate) {

		boolean status = serialPortManagerRefStd_V2.refStdComInitV2(selectedComPortId, selectedBaudRate);// HVCI_CommPortID,
																											// HVCI_CommBaudRate);
		return status;
	}

	public void refStdEnableSerialMonitoring() {

	}

	public void refStdEnableSerialMonitoring_V2() {
		ApplicationLauncher.logger.debug("refStdEnableSerialMonitoring_V2: Entry");
		serialPortManagerRefStd_V2.startSerialRxPhysical_RefStd();
		serialPortManagerRefStd_V2.enableSerialRxPhysical_RefStdMonitor();

	}

	public void pwrSrcEnableSerialMonitoring_V2() {

		serialPortManagerPwrSrc_V2.startSerialRxPhysical_PwrSrc();
		serialPortManagerPwrSrc_V2.enableSerialRxPhysical_PwrSrcMonitor();

	}

	/*
	 * public void dutCmdEnableSerialMonitoring_V2() {
	 * 
	 * serialPortManagerDutCmd_V2.startSerialRxPhysical_Dut();
	 * serialPortManagerDutCmd_V2.enableSerialRxPhysical_DutMonitor();
	 * 
	 * }
	 */

	public void disconnectRefStdSerialCommIfConnected() {

		serialPortManagerRefStd_V2.disconnectRefStdSerialCommIfConnected();

	}

	public void refStdDisconnectPort() {

	}

	public void refStdDisconnectPort_V2() {

		serialPortManagerRefStd_V2.disconnectRefStd();

	}

	public void pwrSrcDisconnectPort_V2() {

		serialPortManagerPwrSrc_V2.disconnectPwrSrc();

	}

	/*
	 * public void dutCmdDisconnectPort_V2() {
	 * 
	 * serialPortManagerDutCmd_V2.disconnectDut();
	 * 
	 * }
	 */

	public boolean powerSourcePortAccessible() {
		ApplicationHomeController.update_left_status("PwrSrc COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setPowerSourcePortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (powerSource_ComInit()) {

			status = true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("powerSourcePortAccessible: Serial port accessable");

		} else {

			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("powerSourcePortAccessible: " + ErrorCodeMapping.ERROR_CODE_900 + ": "
					+ ErrorCodeMapping.ERROR_CODE_900_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_900, ErrorCodeMapping.ERROR_CODE_900_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean refStdPortAccessible() {
		ApplicationHomeController.update_left_status("RefStd COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setRefStdPortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (refStd_ComInit()) {

			status = true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible: Serial port accessible");

		} else {

			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible:" + ErrorCodeMapping.ERROR_CODE_901 + ": "
					+ ErrorCodeMapping.ERROR_CODE_901_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_901, ErrorCodeMapping.ERROR_CODE_901_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean refStdPortAccessible_V2() {
		ApplicationHomeController.update_left_status("RefStd COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setRefStdPortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (refStd_ComInitV2()) {

			status = true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2: Serial port accessible");

		} else {

			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2:" + ErrorCodeMapping.ERROR_CODE_901 + ": "
					+ ErrorCodeMapping.ERROR_CODE_901_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_901, ErrorCodeMapping.ERROR_CODE_901_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean refStdPortAccessible_V2_1(String selectedComPortId, String selectedBaudRate) {
		ApplicationHomeController.update_left_status("RefStd COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setRefStdPortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (refStd_ComInitV2_1(selectedComPortId, selectedBaudRate)) {

			status = true;
			setRefStdPortInitSuccess(true);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2_1: Serial port accessible");

		} else {

			setRefStdPortInitSuccess(false);
			ApplicationLauncher.logger.info("refStdPortAccessible_V2_1:" + ErrorCodeMapping.ERROR_CODE_901 + ": "
					+ ErrorCodeMapping.ERROR_CODE_901_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_901, ErrorCodeMapping.ERROR_CODE_901_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean pwrSrcPortAccessible_V2() {
		ApplicationHomeController.update_left_status("Power Source COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setPowerSourcePortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (powerSource_ComInitV2()) {

			status = true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2: Serial port accessable");

		} else {

			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2: " + ErrorCodeMapping.ERROR_CODE_900 + ": "
					+ ErrorCodeMapping.ERROR_CODE_900_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_900, ErrorCodeMapping.ERROR_CODE_900_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean pwrSrcPortAccessible_V2_1(String selectedComPortId, String selectedBaudRate) {
		ApplicationHomeController.update_left_status("Power Source COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		setPowerSourcePortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (powerSource_ComInitV2_1(selectedComPortId, selectedBaudRate)) {

			status = true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: Serial port accessable");

		} else {

			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: " + ErrorCodeMapping.ERROR_CODE_900 + ": "
					+ ErrorCodeMapping.ERROR_CODE_900_MSG);
			WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_900, ErrorCodeMapping.ERROR_CODE_900_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	/*
	 * public boolean dutCmdPortAccessible_V2_1(String selectedComPortId, String
	 * selectedBaudRate){
	 * ApplicationHomeController.
	 * update_left_status("DutCmd COM port access validation",ConstantApp.
	 * LEFT_STATUS_DEBUG);
	 * 
	 * //LoadCurrentSerialComSettingFromDB();
	 * boolean status=false;
	 * //setPowerSourcePortInitSuccess(false);
	 * //if( DisplayLDU_Init()) {
	 * if( dutCmd_ComInitV2_1(selectedComPortId,selectedBaudRate)) {
	 * 
	 * status= true;
	 * //setPowerSourcePortInitSuccess(true);
	 * ApplicationLauncher.logger.
	 * info("dutCmdPortAccessible_V2_1: Serial port accessable");
	 * 
	 * }else {
	 * 
	 * 
	 * //setAllPortInitSuccess(false);
	 * ApplicationLauncher.logger.info("dutCmdPortAccessible_V2_1: " +
	 * ErrorCodeMapping.ERROR_CODE_900 +": "+ErrorCodeMapping.ERROR_CODE_900_MSG);
	 * WindowManager.InformUser(ErrorCodeMapping.ERROR_CODE_900,ErrorCodeMapping.
	 * ERROR_CODE_900_MSG,AlertType.ERROR);
	 * 
	 * }
	 * 
	 * 
	 * return status;
	 * 
	 * }
	 */

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

		return readRefStdAccuDataFlag;
	}

	public void setReadRefStdAccuDataFlag(boolean inputData) {

		readRefStdAccuDataFlag = inputData;
		// return readRefStdAccuDataFlag;
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
		DeviceDataManagerController.reportProfileMeterMetaDataFilterService = reportProfileMeterMetaDataFilterService;
	}

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
		// resetLastSetPowerSourceFrequency();// commented on version #s4.2.1.2.1.4 for
		// customtest issue on PowerSource only
	}

	public static void setHardwareBootupOccured(boolean bootOccured) {

		hardwareBootupOccured = bootOccured;
	}

	public static boolean isHardwareBootupOccured() {

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

	public static ArrayList<Boolean> getStepRunModeAtleastOneResultReadCompleted() {
		return stepRunModeAtleastOneResultReadCompleted;
	}

	public static void setStepRunModeAtleastOneResultReadCompleted(
			ArrayList<Boolean> stepRunModeAtleastOneResultReadCompleted) {
		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted = stepRunModeAtleastOneResultReadCompleted;
	}

	public static void resetStepRunModeAtleastOneResultReadCompleted() {

		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.clear();
		for (int i = 0; i <= 48; i++) {
			DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.add(false);
		}
		DeviceDataManagerController.stepRunModeAtleastOneResultReadCompleted.set(0, true);

	}

	public static void updateMetricsOnExcelLogFile(long presentTimeInEpochMilliSec, String timeInMsec, String file_path,
			String fileName, String targetVoltage, String targetCurrent,
			String actualVoltage,
			String actualCurrent,
			String actualPower,
			String actualPf

	) {

		int meterSerialNoHeaderColumn = 2;
		String sourceTemplateFilePathName = file_path + fileName;

		XSSFWorkbook xSSFworkbook = null;
		XSSFSheet spreadsheet = null;
		// try {
		FileInputStream file;
		try {
			// file = new FileInputStream(new File(sourceTemplateFilePathName));
			file = new FileInputStream(new File(sourceTemplateFilePathName));
			System.out
					.println("updateMetricsOnExcelLogFile: sourceTemplateFilePathName: " + sourceTemplateFilePathName);

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

				// String cmdTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((
				// getMetricsLogTestPointCmdSentEpochTimeInMSec()));

				// row.createCell(meterSerialNoHeaderColumn).setCellValue(cmdTimeStamp);
				// row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);

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
				float voltageAccuracy = ((factualVoltage / fTargetVoltage) * 100) - 100.0f;
				String voltageAccStr = "-NA-";
				/*
				 * if(fTargetVoltage!=0.0f) {
				 * voltageAccStr= String.format("%02.03f", voltageAccuracy);
				 * }
				 */

				int setScaleCurrentAfterDecimal = 3;
				if (fTargetVoltage != 0.0f) {
					BigDecimal bigValue = new BigDecimal(voltageAccuracy);
					bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
					voltageAccuracy = bigValue.floatValue();

					voltageAccStr = String.format("%02.03f", voltageAccuracy);
				}
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: voltageAccuracy: " + voltageAccuracy);
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: voltageAccStr: " + voltageAccStr);

				row.createCell(meterSerialNoHeaderColumn).setCellValue(voltageAccStr);
				// row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				if (!voltageAccStr.equals("-NA-")) {
					if ((voltageAccuracy > getMetricsLogAcceptedUpperLimit())
							|| (voltageAccuracy < getMetricsLogAcceptedLowerLimit())) {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(failStyle);
					} else {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
					}
				} else {
					row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				}

				float fTargetCurrent = Float.parseFloat(targetCurrent);
				float factualCurrent = Float.parseFloat(actualCurrent);
				float currentAccuracy = ((factualCurrent / fTargetCurrent) * 100) - 100.0f;
				String currentAccStr = "-NA-";
				if (fTargetCurrent != 0.0f) {
					BigDecimal bigValue = new BigDecimal(currentAccuracy);
					bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
					currentAccuracy = bigValue.floatValue();
					currentAccStr = String.format("%02.03f", currentAccuracy);
				}
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: currentAccuracy: " + currentAccuracy);
				ApplicationLauncher.logger.debug("updateMetricsOnExcelLogFile: currentAccStr: " + currentAccStr);

				row.createCell(meterSerialNoHeaderColumn).setCellValue(currentAccStr);
				// row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				if (!currentAccStr.equals("-NA-")) {
					if ((currentAccuracy > getMetricsLogAcceptedUpperLimit())
							|| (currentAccuracy < getMetricsLogAcceptedLowerLimit())) {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(failStyle);
					} else {
						row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
					}
				} else {
					row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);
				}

				for (int i = 2; i < 15; i++) {
					spreadsheet.autoSizeColumn(i);
				}

				// xSSFworkbook.write(arg0);
				// xSSFworkbook.close();
				try {
					FileOutputStream fileOut = new FileOutputStream(file_path + fileName);// fileName);
					// workbook.write(fileOut);
					xSSFworkbook.write(fileOut);
					xSSFworkbook.close();
					fileOut.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (IOException e) {

				e.printStackTrace();
				System.out.println("updateMetricsOnExcelLogFile: Exception-1:" + e.getMessage());
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
			System.out.println("updateMetricsOnExcelLogFile: Exception-2:" + e.getMessage());
		}

	}

	public static void createMetricsExcelLogFile(String file_path, String fileName) {

		String project_name = "myTest";// get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		// ApplicationLauncher.logger.info("exportAllResultToExcelV2: project_name:
		// "+project_name);
		if (project_name != null) {
			// ApplicationHomeController.update_left_status("Exporting...",ConstantApp.LEFT_STATUS_DEBUG);

			String reportFileNameWithSerialNo = "";// getProjectFileNameWithSerialNo();// getProjectFileName();
			String reportFileName = "";// getProjectFileName();
			String projectDate = "";// getProjectDate();
			// Workbook workbook = new HSSFWorkbook();
			// Sheet spreadsheet = workbook.createSheet("Sheet1");

			// HSSFWorkbook HSSFworkbook = new HSSFWorkbook(file);

			// String sourceTemplateFilePathName = ConstantConfig.REPORT_TEMPLATE_FILE_PATH
			// +
			// ConstantConfig.REPORT_TEMPLATE_FILE_NAME;//"C:\\Reports\\Template\\ProTampReport_V1_0.xlsx";
			// String sourceTemplateFilePathName =
			// "C:\\Reports\\Template\\ProTampReport_V1_0.xlsx";

			FileInputStream file = null;
			XSSFWorkbook xSSFworkbook = null;
			XSSFSheet spreadsheet = null;
			// try {
			// file = new FileInputStream(new File(sourceTemplateFilePathName));

			xSSFworkbook = new XSSFWorkbook();

			// XSSFSheet spreadsheet = xSSFworkbook.getSheetAt(0);
			// XSSFSheet spreadsheet = xSSFworkbook.createSheet("Result");
			spreadsheet = xSSFworkbook.createSheet("Result");
			// spreadsheet = xSSFworkbook.getSheetAt(0);//.createSheet("Result");

			int meterSerialNoRow = 0;// 1;
			int meterSerialNoHeaderColumn = 2;// userSelectedColumn+2 ;//3;
			int projectDetailTimeStampValueColumn = 6;// userSelectedColumn+5;//6;

			int dateValuePrintRow = 1;
			int dateHeaderPrintColumn = 5;// userSelectedColumn+5;//6;
			int dateValuePrintColumn = 6;// userSelectedColumn+6;//7;

			// Row row = spreadsheet.createRow(userSelectedRow);//0);
			Row row = spreadsheet.createRow(meterSerialNoRow++);// 0);
			// CellStyle style = (HSSFCellStyle)
			// row.getSheet().getWorkbook().createCellStyle();
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
			// XSSFColor myColor = new XSSFColor(Color.RED);
			// style.setFillForegroundColor(myColor);
			spreadsheet.addIgnoredErrors(new CellRangeAddress(0, 10000, 0, 99), IgnoredErrorType.NUMBER_STORED_AS_TEXT);

			row.createCell(meterSerialNoHeaderColumn).setCellValue("Lower Limit");
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);

			row.createCell(meterSerialNoHeaderColumn).setCellValue(getMetricsLogAcceptedLowerLimit());
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);

			row.createCell(meterSerialNoHeaderColumn).setCellValue("Upper Limit");
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);

			row.createCell(meterSerialNoHeaderColumn).setCellValue(getMetricsLogAcceptedUpperLimit());
			row.getCell(meterSerialNoHeaderColumn++).setCellStyle(style);

			meterSerialNoHeaderColumn = meterSerialNoHeaderColumn - 4;
			row = spreadsheet.createRow(meterSerialNoRow);
			// row.createCell(meterSerialNoHeaderColumn).setCellValue("Cmd TimeStamp");
			// row.getCell(meterSerialNoHeaderColumn++).setCellStyle(headerStyle);

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

			// Sheet.createFreezePane(int colSplit, int rowSplit)
			spreadsheet.createFreezePane(2, 2);

			setPresentCursorRowNo(new AtomicInteger(1));
			try {
				FileOutputStream fileOut = new FileOutputStream(file_path + fileName);// fileName);
				// workbook.write(fileOut);
				xSSFworkbook.write(fileOut);
				xSSFworkbook.close();
				fileOut.close();

			} catch (Exception e) {
				e.printStackTrace();

			}
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

	public static void setMetricsLogTestPointStartingAlreadyInitated(
			boolean metricsLogTestPointStartingAlreadyInitated) {
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
		DeviceDataManagerController.metricsLogAcceptedLowerLimit = metricsLogAcceptedLowerLimit;
	}

	public void setMetricsLogAcceptedUpperLimit(float metricsLogAacceptedUpperLimit) {
		DeviceDataManagerController.metricsLogAcceptedUpperLimit = metricsLogAacceptedUpperLimit;
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

	public static void setDutSerialNumberMap(String dutSerialNo, int lduAddress) {

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
}
