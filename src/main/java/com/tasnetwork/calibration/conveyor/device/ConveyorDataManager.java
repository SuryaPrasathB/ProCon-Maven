package com.tasnetwork.calibration.conveyor.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.bay.configloader.TerminalBayConfigModel;
import com.tasnetwork.calibration.conveyor.dashboard.DashboardController;
import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.serial.portmanager.SpmDut;
import com.tasnetwork.calibration.conveyor.tree.ConveyorTreeLogic;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.setting.QrScannerPortSetupController;
import com.tasnetwork.spring.orm.model.DeviceSetting;

/*import javafx.fxml.FXML;*/
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
//import javafx.scene.control.TextField;

public class ConveyorDataManager {

	private static DashboardController dashboardObject = new DashboardController();
	private static ConveyorTreeLogic myConveyorTree = new ConveyorTreeLogic();
	public static SpmDut serialPortManagerPwrSrc_V2 = new SpmDut();
	public static HashMap<Integer, String> dutResultSummary = new HashMap<>();
	public static ArrayList<DeviceSetting> deviceSettingList = new ArrayList<DeviceSetting>();
	// public static MySqlServiceManager dbServiceManager = MySqlServiceManager();

	private static boolean noEntryActiveInFt = false;
	private static boolean noEntryActiveInHv = false;
	private static boolean noEntryActiveInIr = false;
	private static boolean noEntryActiveInCalib = false;
	private static boolean noEntryActiveInVerific1Waiting = false;
	private static boolean noEntryActiveInVerific1 = false;
	private static boolean noEntryActiveInSta1 = false;
	private static boolean noEntryActiveInSta2 = false;
	private static boolean noEntryActiveInRejection = false;
	private static boolean noEntryActiveInUnloading = false;

	private static boolean haltPalletActiveInFt = false;
	private static boolean haltPalletActiveInHv = false;
	private static boolean haltPalletActiveInIr = false;
	private static boolean haltPalletActiveInCalib = false;
	private static boolean haltPalletActiveInVerific1Waiting = false;
	private static boolean haltPalletActiveInVerific1 = false;
	private static boolean haltPalletActiveInSta1 = false;
	private static boolean haltPalletActiveInSta2 = false;
	private static boolean haltPalletActiveInRejection = false;
	private static boolean haltPalletActiveInUnloading = false;

	public static boolean waitingVerific1BayPalletsAllCleared = true;

	public static boolean waitingVerific1PalletsAccepted = true;
	// public static boolean verific1PalletsAccepted = true;
	public static boolean verific1PalletsAllCleared = true;
	public static boolean sta1PalletsAccepted = true;
	public static boolean sta2PalletsAccepted = true;
	public static boolean sta1PalletsAllCleared = true;
	public static boolean sta2PalletsAllCleared = true;
	private static boolean sta1PalletsExitInProgress = false;
	private static boolean sta2PalletsExitInProgress = false;

	private static boolean verific1BatchFailedPromptUserInputReceived = false;
	private static boolean verific1BatchFailedReleasePalletRequested = false;

	// private static boolean hvMoreActivePalletsPromptUserInputReceived = false;

	private static boolean unloadingBayScreenNameUserInputReceived = false;
	private static int unloadingBayUserEntryScreenName = 1;

	private static boolean unloadingBayScreenPalletResultDisplayUserRequested = false;
	private static String unloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId = "";

	private static boolean rejectionBayScreenNameUserInputReceived = false;
	private static int rejectionBayUserEntryScreenName = 1;

	private static boolean rejectionBayScreenPalletResultDisplayUserRequested = false;
	private static String rejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId = "";

	public static boolean powerSourcePortInitSuccess = false;
	static int occuranceTimeInSec = 0;
	static int restorationTimeInSec = 0;
	static int TimerTP_OnCycleTimeInSec = 0;
	static int TimerTP_OffCycleTimeInSec = 0;
	static int TimerTP_NoOfCycle = 0;
	static long TestParamRefStdMeterConstant = 0;
	static int testParamNoOfSamples = 1;
	static int TestParamNoOfTestPulses = 0;
	static int FreqTP_PulsatingDC_Freq = 0;
	// static long SlaveMeterConstantData = 0;

	static String R_PhaseOutputVoltage = "000.00";
	static String Y_PhaseOutputVoltage = "000.00";
	static String B_PhaseOutputVoltage = "000.00";

	static String STATimeDuration = "0000";
	static String CreepTimeDuration = "0000";
	static String R_PhaseOutputCurrent = "00.00";
	static String Y_PhaseOutputCurrent = "00.00";
	static String B_PhaseOutputCurrent = "00.00";

	static String R_PhaseOutputPhase = "00.00";
	static String Y_PhaseOutputPhase = "00.00";
	static String B_PhaseOutputPhase = "00.00";

	static boolean PhaseRevPowerOn = false;
	static boolean refStdDeviceConfigurationSuccess = false;

	// static String EnergyFlowMode = ConstantPowerSource.IMPORT_MODE;

	static boolean SkipPhaseRev = false;

	static JSONArray harmonic_data = new JSONArray();

	static boolean Volt_Unbalanced_PowerOn = false;

	public static boolean bLDU_ReadData = false;
	private static boolean bRefStdReadData = false;

	private static Integer PowerSrcOnTimerInSec;

	private static JSONObject DeployedDevicesJson = new JSONObject();

	String PowerSrcCommPortID = null;
	String PwrSrcCommBaudRate = null;
	String LDU_CommPortID = null;
	String LDUCommBaudRate = null;
	String RefStdCommPortID = null;
	String RefStdCommBaudRate = null;

	Timer UIRefreshTimer;

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
	static String meterConstantRatio = "";
	static String executionMctNctMode = "MCT";// ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;

	static String DeployedEM_CT_Type = "";
	static String ImpulsesPerUnit = "";
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

	// public static SerialDataManager serialDM_Obj = new SerialDataManager();
	private static TerminalBayConfigModel terminalBayConfig = new TerminalBayConfigModel();

	/*
	 * @FXML
	 * private void initialize() throws IOException {
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	public void setDeployedDevicesJson(String project_name, String deploymentId) {
		/*
		 * String project_name = ProjectExecutionController.getCurrentProjectName();
		 * String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();
		 */
		DeployedDevicesJson = MySQL_Controller.sp_getdeploy_devices(project_name, deploymentId);
	}

	public boolean updateExecutionStatusInDeployManageDB(String project_name, String deployment_id,
			String executionStatus, String mctModeCompletedStatus, String nctModeCompletedStatus) {
		boolean status = false;
		status = MySQL_Controller.sp_update_execution_status_deploy_manage(project_name, deployment_id, executionStatus,
				mctModeCompletedStatus, mctModeCompletedStatus);
		return status;
	}

	public static void LoadSavedTestParamData() {

		JSONObject TestParamData = MySQL_Controller.sp_get_testparam_config();
		if (TestParamData.length() > 0) {
			try {
				int data_inSec = TestParamData.getInt("occurtime_insec");
				setOccuranceTimeInSec(data_inSec);

				data_inSec = TestParamData.getInt("restoretime_insec");
				setRestorationTimeInSec(data_inSec);

				data_inSec = TestParamData.getInt("timer_tp_ontime_insec");
				setTimerTP_OnCycleTimeInSec(data_inSec);

				data_inSec = TestParamData.getInt("timer_tp_offtime_insec");
				setTimerTP_OffCycleTimeInSec(data_inSec);

				setTimerTP_NoOfCycle(TestParamData.getInt("timer_tp_no_of_cycle"));

				setTestParamRefStdMeterConstant(Long.parseLong(TestParamData.getString("ref_std_constant")));
				// setImpulsesPerUnit(TestParamData.getString("impulses_per_unit"));
				ApplicationLauncher.logger.debug(
						"LoadSavedTestParamDataToGUI: no of pulses: " + TestParamData.getInt("ref_std_no_of_pulses"));
				setTestParamNoOfTestPulses(TestParamData.getInt("ref_std_no_of_pulses"));
				setFreqTP_PulsatingDC_Freq(TestParamData.getInt("freq_tp_pulsating_dc_freq"));

				// ApplicationLauncher.logger.info("calculateMeterConstantRatio:
				// meterConstantRatio:" +meterConstantRatio);

			} catch (JSONException e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("LoadSavedTestParamDataToGUI: JSONException1:" + e.getMessage());

			}

		}

	}

	public String getMeterConstantRatio() {
		return meterConstantRatio;
	}

	public static void setMeterConstantRatio(String mConstRatio) {
		meterConstantRatio = mConstRatio;
	}

	public static void calculateMeterConstantRatio() {
		// DisplayDataObj.getTestParamRefStdMeterConstant(),DisplayDataObj.getImpulsesPerUnit()
		if (!getImpulsesPerUnit().isEmpty()) {
			try {
				meterConstantRatio = String
						.valueOf(getTestParamRefStdMeterConstant() / Long.parseLong(getImpulsesPerUnit()));
				ApplicationLauncher.logger
						.info("calculateMeterConstantRatio:  meterConstantRatio:" + meterConstantRatio);
			} catch (Exception e) {

				e.printStackTrace();
				ApplicationLauncher.logger.error("calculateMeterConstantRatio: Exception:" + e.getMessage());

			}
		}
		// meterConstantRatio = mConstRatio;
	}

	public static String getExecutionMctNctMode() {
		return executionMctNctMode;
	}

	public static void setExecutionMctNctMode(String executionMctNctMode) {
		ConveyorDataManager.executionMctNctMode = executionMctNctMode;
	}

	public static void setError_count(int inputErrorCount) {
		error_count = inputErrorCount;
	}

	public static int getError_countAndIncrement() {
		return error_count++;// = inputErrorCount;
	}

	/*
	 * public static void setupSlaveMeterConstantData(){
	 * SlaveMeterConstantData = value;
	 * }
	 * public static long getSlaveMeterConstantData(){
	 * return SlaveMeterConstantData;
	 * }
	 */

	public static void setTestParamRefStdMeterConstant(long value) {
		TestParamRefStdMeterConstant = value;
	}

	public static long getTestParamRefStdMeterConstant() {
		return TestParamRefStdMeterConstant;
	}

	public static void setFreqTP_PulsatingDC_Freq(int value) {
		FreqTP_PulsatingDC_Freq = value;
	}

	public static int getFreqTP_PulsatingDC_Freq() {
		return FreqTP_PulsatingDC_Freq;
	}

	public static void setTestParamNoOfTestPulses(int value) {
		TestParamNoOfTestPulses = value;
	}

	public static int getTestParamNoOfTestPulses() {
		return TestParamNoOfTestPulses;
	}

	public static void setTimerTP_NoOfCycle(int value) {
		TimerTP_NoOfCycle = value;
	}

	public static int getTimerTP_NoOfCycle() {
		return TimerTP_NoOfCycle;
	}

	public static void setTimerTP_OffCycleTimeInSec(int value) {
		TimerTP_OffCycleTimeInSec = value;
	}

	public static int getTimerTP_OffCycleTimeInSec() {
		return TimerTP_OffCycleTimeInSec;
	}

	public static void setTimerTP_OnCycleTimeInSec(int value) {
		TimerTP_OnCycleTimeInSec = value;
	}

	public static int getTimerTP_OnCycleTimeInSec() {
		return TimerTP_OnCycleTimeInSec;
	}

	public static void setOccuranceTimeInSec(int value) {
		occuranceTimeInSec = value;
	}

	public static int getOccuranceTimeInSec() {
		return occuranceTimeInSec;
	}

	public static void setRestorationTimeInSec(int value) {
		restorationTimeInSec = value;
	}

	public static int getRestorationTimeInSec() {
		return restorationTimeInSec;
	}

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

	}

	public void loadQrSerialComSettingFromDB() {
		ApplicationLauncher.logger.info("loadQrSerialComSettingFromDB :Entry");
		PowerSrcCommPortID = get_port_name(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		PwrSrcCommBaudRate = get_baud_rate(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
	}

	public JSONObject getDeployedDevicesJson() {

		return DeployedDevicesJson;
	}

	public boolean ValidateAllComPortAccessible() {
		ApplicationHomeController.update_left_status("COM port access validation", ConstantApp.LEFT_STATUS_DEBUG);
		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		AllPortInitSuccess = false;
		// if (DisplayRefStdInit()) {
		if (DisplayPwrSrc_Init()) {
			// if( DisplayLDU_Init()) {
			status = true;
			AllPortInitSuccess = true;
			ApplicationLauncher.logger.info("ValidateAllComPort: All Serial port accessable");

		} else {

			ApplicationLauncher.logger.info("ValidateAllComPort: Unable to access Power Source Serial Port");
			Alert alert = new Alert(AlertType.ERROR, "Unable to access Power Source Serial Port", ButtonType.OK);
			alert.showAndWait();
		}

		return status;

	}

	public boolean validateQrComPortAccessible() {
		ApplicationHomeController.update_left_status("COM port access validation", ConstantApp.LEFT_STATUS_DEBUG);
		LoadCurrentSerialComSettingFromDB();
		boolean status = false;
		AllPortInitSuccess = false;
		// if (DisplayRefStdInit()) {
		if (DisplayPwrSrc_Init()) {
			// if( DisplayLDU_Init()) {
			status = true;
			AllPortInitSuccess = true;
			ApplicationLauncher.logger.info("validateQrComPortAccessible: All Serial port accessable");

			/*
			 * }else {
			 * 
			 * ApplicationLauncher.logger.
			 * info("ValidateAllComPort: Unable to access LDU Serial Port");
			 * Alert alert = new Alert(AlertType.ERROR, "Unable to access LDU Serial Port",
			 * ButtonType.OK);
			 * alert.showAndWait();
			 * 
			 * }
			 */
		} else {

			ApplicationLauncher.logger.info("validateQrComPortAccessible: Unable to access QR Serial Port");
			Alert alert = new Alert(AlertType.ERROR, "Unable to access QR Serial Port", ButtonType.OK);
			alert.showAndWait();
		}

		// }
		/*
		 * else {
		 * 
		 * ApplicationLauncher.logger.
		 * info("ValidateAllComPort: Unable to access Ref Standard Serial Port");
		 * Alert alert = new Alert(AlertType.ERROR,
		 * "Unable to access Ref Standard Serial Port", ButtonType.OK);
		 * alert.showAndWait();
		 * }
		 */

		return status;

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

	public void setVoltageResetRequired(boolean value) {
		VoltageResetRequired = value;
	}

	public boolean getVoltageResetRequired() {
		return VoltageResetRequired;
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

	public void set_PwrSrcR_PhaseDegreePhase(String DegreePhase) {
		PwrSrcR_PhaseDegreePhase = Float.valueOf(DegreePhase);

	}

	public static float get_PwrSrcR_PhaseDegreePhase() {
		return PwrSrcR_PhaseDegreePhase;

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

	public void set_PwrSrc_Freq(String Frequency) {
		PwrSrcR_PhaseFreq = Float.valueOf(Frequency);

	}

	public static float get_PwrSrc_Freq() {
		return PwrSrcR_PhaseFreq;

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

	public static String getImpulsesPerUnit() {
		return ImpulsesPerUnit;

	}

	public static void setImpulsesPerUnit(String impulses) {
		ImpulsesPerUnit = impulses;

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

	public boolean DisplayPwrSrc_Init() {

		// boolean status
		// =serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
		// return status;

		boolean status = enableSerialPortAndMonitorV2();
		return status;
	}

	public boolean enableSerialPortAndMonitorV2() {
		ApplicationLauncher.logger.debug("enableSerialPortAndMonitorV2 Invoked:");
		boolean status = false;
		// if(displayDataObj.powerSourcePortAccessible()){
		if (pwrSrcPortAccessible_V2()) {
			// SerialDM_Obj.ClearSerialDataInLDU_Ports();
			// serialDM_Obj.startSerialRxPhysical_PowerSource();
			// serialDM_Obj.enableSerialRxPhysical_PowerSourceMonitor();
			pwrSrcEnableSerialMonitoring_V2();
			// setSerialDmV2_Obj(getSerialPortManagerPwrSrc_V2());
			status = true;
		}
		return status;
	}

	public boolean pwrSrcPortAccessible_V2_1(String selectedComPortId, String selectedBaudRate) {
		ApplicationHomeController.update_left_status("Power Source COM port access validation",
				ConstantApp.LEFT_STATUS_DEBUG);

		// LoadCurrentSerialComSettingFromDB();
		loadQrSerialComSettingFromDB();
		boolean status = false;
		setPowerSourcePortInitSuccess(false);
		// if( DisplayLDU_Init()) {
		if (powerSource_ComInitV2_1(selectedComPortId, selectedBaudRate)) {

			status = true;
			setPowerSourcePortInitSuccess(true);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: Serial port accessable");

		} else {

			setAllPortInitSuccess(false);
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2_1: " + ConvErrorCodeMapping.ERROR_CODE_900 + ": "
					+ ConvErrorCodeMapping.ERROR_CODE_900_MSG);
			WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_900, ConvErrorCodeMapping.ERROR_CODE_900_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean powerSource_ComInitV2_1(String selectedComPortId, String selectedBaudRate) {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(selectedComPortId, selectedBaudRate);// HVCI_CommPortID,
																												// HVCI_CommBaudRate);
		return status;
	}

	public void pwrSrcEnableSerialMonitoring_V2() {

		serialPortManagerPwrSrc_V2.startSerialRxPhysical_Dut();
		serialPortManagerPwrSrc_V2.enableSerialRxPhysical_DutMonitor();

	}

	public static boolean isPowerSourcePortInitSuccess() {
		return powerSourcePortInitSuccess;
	}

	public static void setPowerSourcePortInitSuccess(boolean hvciPortInitSuccess) {
		ConveyorDataManager.powerSourcePortInitSuccess = hvciPortInitSuccess;
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
			ApplicationLauncher.logger.info("pwrSrcPortAccessible_V2: " + ConvErrorCodeMapping.ERROR_CODE_900 + ": "
					+ ConvErrorCodeMapping.ERROR_CODE_900_MSG);
			WindowManager.InformUser(ConvErrorCodeMapping.ERROR_CODE_900, ConvErrorCodeMapping.ERROR_CODE_900_MSG,
					AlertType.ERROR);

		}

		return status;

	}

	public boolean powerSource_ComInitV2() {

		boolean status = serialPortManagerPwrSrc_V2.powerSourceComInitV2(PowerSrcCommPortID, PwrSrcCommBaudRate);// HVCI_CommPortID,
																													// HVCI_CommBaudRate);
		return status;
	}

	/*
	 * public boolean DisplayLDU_Init() {
	 * 
	 * boolean status =serialDM_Obj.LDU_Init(LDU_CommPortID, LDUCommBaudRate);
	 * return status;
	 * 
	 * 
	 * }
	 */

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:" + e.getMessage());
		}

	}

	/*
	 * public static String getPowerData(TextField refTextField){
	 * return refTextField.getText();
	 * }
	 */

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
			port_name = QrScannerPortSetupController.get_device_settings(src_type).getString("port_name");
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("get_port_name :JSONException:" + e.getMessage());
		}
		return port_name;
	}

	public String get_baud_rate(String src_type) {
		String baud_rate = "";
		try {
			baud_rate = QrScannerPortSetupController.get_device_settings(src_type).getString("baud_rate");
		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("get_baud_rate :JSONException:" + e.getMessage());
		}
		return baud_rate;
	}

	public void SetPulseConstantDataWithCurrent(float CurrentValue) {

		ApplicationLauncher.logger.debug("SetPulseConstantDataWithCurrent: CurrentValue: " + CurrentValue);

		if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)) {
			// ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting
			// Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
			// DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
			if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);

				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting LTCT Active Pulse Constant: " + getRSSPulseRate());
			} else if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT);

				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting HTCT Active Pulse Constant: " + getRSSPulseRate());

			}
		} else if (getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)) {
			// ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting
			// reactive Pulse Constant:"+DisplayDataObj.RSS_ReactivePulseConstant);
			// DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ReactivePulseConstant);
			if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT);

				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting LTCT Reactive Pulse Constant: " + getRSSPulseRate());

			} else if (getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)) {
				// setRSSPulseRate(ConstantConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT);

				ApplicationLauncher.logger.debug(
						"SetPulseConstantDataWithCurrent: Setting HTCT Reactive Pulse Constant: " + getRSSPulseRate());
			}
		}

	}

	public void pwrSrcDisconnectPort_V2() {

		serialPortManagerPwrSrc_V2.disconnectDut();

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

	final public String getB_PhaseOutputVoltage() {
		return B_PhaseOutputVoltage;
	}

	final public String getY_PhaseOutputVoltage() {
		return Y_PhaseOutputVoltage;
	}

	public static void setLDU_STATimeDurationFormat(Integer STATimeInSec) {
		ApplicationLauncher.logger.info("setLDU_STATimeDurationFormat :Entry");
		int sec = (STATimeInSec % 60);
		int min = ((STATimeInSec / 60) % 60);
		STATimeDuration = String.format("%02d", min) + String.format("%02d", sec);

	}

	public String getLDU_STATimeDurationFormat() {

		return ConveyorDataManager.STATimeDuration;
	}

	final public String getR_PhaseOutputCurrent() {
		return R_PhaseOutputCurrent;
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

	public void setLDU_CreepTimeDurationFormat(Integer CreepTimeInSec) {
		ApplicationLauncher.logger.info("setLDU_CreepTimeDurationFormat :Entry");
		int sec = (CreepTimeInSec % 60);
		int min = ((CreepTimeInSec / 60) % 60);
		CreepTimeDuration = String.format("%02d", min) + String.format("%02d", sec);
	}

	public String getLDU_CreepTimeDurationFormat() {

		return ConveyorDataManager.CreepTimeDuration;
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

	public JSONObject getModelParameters(String project_name) {
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);

		return model_data;
	}

	public static int getTestParamNoOfSamples() {
		return testParamNoOfSamples;
	}

	public static void setTestParamNoOfSamples(int testParamNoOfSamples) {
		ConveyorDataManager.testParamNoOfSamples = testParamNoOfSamples;
		ApplicationLauncher.logger.debug("setTestParamNoOfSamples : testParamNoOfSamples:" + testParamNoOfSamples);
	}

	public static boolean isRefStdDeviceConfigurationSuccess() {
		return refStdDeviceConfigurationSuccess;
	}

	public static void setRefStdDeviceConfigurationSuccess(boolean refStdDeviceConfigurationSuccess) {
		ConveyorDataManager.refStdDeviceConfigurationSuccess = refStdDeviceConfigurationSuccess;
	}

	public static void setTerminalBayConfig(TerminalBayConfigModel fromJson) {

		ConveyorDataManager.terminalBayConfig = fromJson;
	}

	public static TerminalBayConfigModel getTerminalBayConfig() {

		return ConveyorDataManager.terminalBayConfig;
	}

	public static SpmDut getSerialPortManagerPwrSrc_V2() {
		return serialPortManagerPwrSrc_V2;
	}

	public static void setSerialPortManagerPwrSrc_V2(SpmDut serialPortManagerPwrSrc_V2) {
		ConveyorDataManager.serialPortManagerPwrSrc_V2 = serialPortManagerPwrSrc_V2;
	}

	public static void setDutResultSummary(String dutSummaryStatus, int lduAddress) {

		ApplicationLauncher.logger.debug("setDutResultSummary : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("setDutResultSummary : dutSummaryStatus: " + dutSummaryStatus);
		ConveyorDataManager.dutResultSummary.put(lduAddress, dutSummaryStatus);

	}

	public static void clearDutResultSummary() {
		dutResultSummary.clear();
	}

	public static ConveyorTreeLogic getMyConveyorTree() {
		return myConveyorTree;
	}

	public static void setMyConveyorTree(ConveyorTreeLogic myConveyorTree) {
		ConveyorDataManager.myConveyorTree = myConveyorTree;
	}

	public static ArrayList<DeviceSetting> getDeviceSettingList() {
		return deviceSettingList;
	}

	public static void setDeviceSettingList(ArrayList<DeviceSetting> deviceSettingList) {
		ConveyorDataManager.deviceSettingList = deviceSettingList;
	}

	public static void loadDeviceSettingFromDb() {
		ArrayList<DeviceSetting> deviceSettingList = new ArrayList<DeviceSetting>();
		deviceSettingList = (ArrayList<DeviceSetting>) MySqlServiceManager.getDeviceSettingService().findAll();
		setDeviceSettingList(deviceSettingList);
	}

	public DeviceSetting getDeviceSettingByDeviceTypeKey(String deviceTypeKey) {

		DeviceSetting deviceSetting = null;

		Optional<DeviceSetting> deviceSettingOpt = getDeviceSettingList().stream()
				.filter(e -> e.getDeviceTypeKey().equals(deviceTypeKey))
				.findFirst();
		if (deviceSettingOpt.isPresent()) {
			deviceSetting = deviceSettingOpt.get();
		}

		return deviceSetting;
	}

	public DeviceSetting getDeviceSettingByDeviceId(String deviceId) {

		DeviceSetting deviceSetting = null;

		Optional<DeviceSetting> deviceSettingOpt = getDeviceSettingList().stream()
				.filter(e -> e.getDeviceId().equals(deviceId))
				.findFirst();
		if (deviceSettingOpt.isPresent()) {
			deviceSetting = deviceSettingOpt.get();
		}

		return deviceSetting;
	}

	public static DashboardController getDashboardObject() {
		return dashboardObject;
	}

	public static void setDashboardObject(DashboardController dashboardObject) {
		ConveyorDataManager.dashboardObject = dashboardObject;
	}

	public static boolean isWaitingVerific1BayPalletsAllCleared() {
		return waitingVerific1BayPalletsAllCleared;
	}

	public static void setWaitingVerific1BayPalletsAllCleared(boolean verific1WaitingBayPalletsAllCleared) {
		ConveyorDataManager.waitingVerific1BayPalletsAllCleared = verific1WaitingBayPalletsAllCleared;
	}

	public static boolean isWaitingVerific1PalletsAccepted() {
		return waitingVerific1PalletsAccepted;
	}

	public static void setWaitingVerific1PalletsAccepted(boolean waitingVerific1PalletsAccepted) {
		ConveyorDataManager.waitingVerific1PalletsAccepted = waitingVerific1PalletsAccepted;
	}

	/*
	 * public static boolean isVerific1PalletsAccepted() {
	 * return verific1PalletsAccepted;
	 * }
	 * 
	 * public static void setVerific1PalletsAccepted(boolean
	 * verific1PalletsAccepted) {
	 * ConveyorDataManager.verific1PalletsAccepted = verific1PalletsAccepted;
	 * }
	 */

	public static boolean isSta1PalletsAccepted() {
		return sta1PalletsAccepted;
	}

	public static void setSta1PalletsAccepted(boolean sta1PalletsAccepted) {
		ConveyorDataManager.sta1PalletsAccepted = sta1PalletsAccepted;
	}

	public static boolean isSta2PalletsAccepted() {
		return sta2PalletsAccepted;
	}

	public static void setSta2PalletsAccepted(boolean sta2PalletsAccepted) {
		ConveyorDataManager.sta2PalletsAccepted = sta2PalletsAccepted;
	}

	public static boolean isSta1PalletsExitInProgress() {
		return sta1PalletsExitInProgress;
	}

	public static void setSta1PalletsExitInProgress(boolean sta1PalletsExitInProgress) {
		ConveyorDataManager.sta1PalletsExitInProgress = sta1PalletsExitInProgress;
	}

	public static boolean isSta2PalletsExitInProgress() {
		return sta2PalletsExitInProgress;
	}

	public static void setSta2PalletsExitInProgress(boolean sta2PalletsExitInProgress) {
		ConveyorDataManager.sta2PalletsExitInProgress = sta2PalletsExitInProgress;
	}

	public static boolean isSta1PalletsAllCleared() {
		return sta1PalletsAllCleared;
	}

	public static void setSta1PalletsAllCleared(boolean sta1PalletsAllCleared) {
		ConveyorDataManager.sta1PalletsAllCleared = sta1PalletsAllCleared;
	}

	public static boolean isSta2PalletsAllCleared() {
		return sta2PalletsAllCleared;
	}

	public static void setSta2PalletsAllCleared(boolean sta2PalletsAllCleared) {
		ConveyorDataManager.sta2PalletsAllCleared = sta2PalletsAllCleared;
	}

	public static boolean isUnloadingBayScreenNameUserInputReceived() {
		return unloadingBayScreenNameUserInputReceived;
	}

	public static void setUnloadingBayScreenNameUserInputReceived(boolean unloadingBayScreenNameUserInputReceived) {
		ConveyorDataManager.unloadingBayScreenNameUserInputReceived = unloadingBayScreenNameUserInputReceived;
	}

	public static int getUnloadingBayUserEntryScreenName() {
		return unloadingBayUserEntryScreenName;
	}

	public static void setUnloadingBayUserEntryScreenName(int unloadingBayUserEntryScreenName) {
		ConveyorDataManager.unloadingBayUserEntryScreenName = unloadingBayUserEntryScreenName;
	}

	public static boolean isRejectionBayScreenNameUserInputReceived() {
		return rejectionBayScreenNameUserInputReceived;
	}

	public static void setRejectionBayScreenNameUserInputReceived(boolean rejectionBayScreenNameUserInputReceived) {
		ConveyorDataManager.rejectionBayScreenNameUserInputReceived = rejectionBayScreenNameUserInputReceived;
	}

	public static int getRejectionBayUserEntryScreenName() {
		return rejectionBayUserEntryScreenName;
	}

	public static void setRejectionBayUserEntryScreenName(int rejectionBayUserEntryScreenName) {
		ConveyorDataManager.rejectionBayUserEntryScreenName = rejectionBayUserEntryScreenName;
	}

	public static boolean isVerific1BatchFailedPromptUserInputReceived() {
		return verific1BatchFailedPromptUserInputReceived;
	}

	public static void setVerific1BatchFailedPromptUserInputReceived(
			boolean verific1BatchFailedPromptUserInputReceived) {
		ConveyorDataManager.verific1BatchFailedPromptUserInputReceived = verific1BatchFailedPromptUserInputReceived;
	}

	public static boolean isVerific1BatchFailedReleasePalletRequested() {
		return verific1BatchFailedReleasePalletRequested;
	}

	public static void setVerific1BatchFailedReleasePalletRequested(boolean verific1BatchFailedReleasePalletRequested) {
		ConveyorDataManager.verific1BatchFailedReleasePalletRequested = verific1BatchFailedReleasePalletRequested;
	}

	public static boolean isUnloadingBayScreenPalletResultDisplayUserRequested() {
		return unloadingBayScreenPalletResultDisplayUserRequested;
	}

	public static String getUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId() {
		return unloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId;
	}

	public static boolean isRejectionBayScreenPalletResultDisplayUserRequested() {
		return rejectionBayScreenPalletResultDisplayUserRequested;
	}

	public static String getRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId() {
		return rejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId;
	}

	public static void setUnloadingBayScreenPalletResultDisplayUserRequested(
			boolean unloadingBayScreenPalletResultDisplayUserRequested) {
		ConveyorDataManager.unloadingBayScreenPalletResultDisplayUserRequested = unloadingBayScreenPalletResultDisplayUserRequested;
	}

	public static void setUnloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId(
			String unloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId) {
		ConveyorDataManager.unloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId = unloadingBayScreenPalletResultDisplayUserRequestedPalletDistinctId;
	}

	public static void setRejectionBayScreenPalletResultDisplayUserRequested(
			boolean rejectionBayScreenPalletResultDisplayUserRequested) {
		ConveyorDataManager.rejectionBayScreenPalletResultDisplayUserRequested = rejectionBayScreenPalletResultDisplayUserRequested;
	}

	public static void setRejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId(
			String rejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId) {
		ConveyorDataManager.rejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId = rejectionBayScreenPalletResultDisplayUserRequestedPalletDistinctId;
	}

	public static boolean isNoEntryActiveInFt() {
		return noEntryActiveInFt;
	}

	public static boolean isNoEntryActiveInHv() {
		return noEntryActiveInHv;
	}

	public static boolean isNoEntryActiveInIr() {
		return noEntryActiveInIr;
	}

	public static boolean isNoEntryActiveInCalib() {
		return noEntryActiveInCalib;
	}

	public static boolean isNoEntryActiveInVerific1Waiting() {
		return noEntryActiveInVerific1Waiting;
	}

	public static boolean isNoEntryActiveInVerific1() {
		return noEntryActiveInVerific1;
	}

	public static boolean isNoEntryActiveInSta1() {
		return noEntryActiveInSta1;
	}

	public static boolean isNoEntryActiveInSta2() {
		return noEntryActiveInSta2;
	}

	public static void setNoEntryActiveInFt(boolean noEntryActiveInFt) {
		ConveyorDataManager.noEntryActiveInFt = noEntryActiveInFt;
	}

	public static void setNoEntryActiveInHv(boolean noEntryActiveInHv) {
		ConveyorDataManager.noEntryActiveInHv = noEntryActiveInHv;
	}

	public static void setNoEntryActiveInIr(boolean noEntryActiveInIr) {
		ConveyorDataManager.noEntryActiveInIr = noEntryActiveInIr;
	}

	public static void setNoEntryActiveInCalib(boolean noEntryActiveInCalib) {
		ConveyorDataManager.noEntryActiveInCalib = noEntryActiveInCalib;
	}

	public static void setNoEntryActiveInVerific1Waiting(boolean noEntryActiveInVerific1Waiting) {
		ConveyorDataManager.noEntryActiveInVerific1Waiting = noEntryActiveInVerific1Waiting;
	}

	public static void setNoEntryActiveInVerific1(boolean noEntryActiveInVerific1) {
		ConveyorDataManager.noEntryActiveInVerific1 = noEntryActiveInVerific1;
	}

	public static void setNoEntryActiveInSta1(boolean noEntryActiveInSta1) {
		ConveyorDataManager.noEntryActiveInSta1 = noEntryActiveInSta1;
	}

	public static void setNoEntryActiveInSta2(boolean noEntryActiveInSta2) {
		ConveyorDataManager.noEntryActiveInSta2 = noEntryActiveInSta2;
	}

	public static boolean isNoEntryActiveInRejection() {
		return noEntryActiveInRejection;
	}

	public static boolean isNoEntryActiveInUnloading() {
		return noEntryActiveInUnloading;
	}

	public static void setNoEntryActiveInRejection(boolean noEntryActiveInRejection) {
		ConveyorDataManager.noEntryActiveInRejection = noEntryActiveInRejection;
	}

	public static void setNoEntryActiveInUnloading(boolean noEntryActiveInUnloading) {
		ConveyorDataManager.noEntryActiveInUnloading = noEntryActiveInUnloading;
	}

	public static boolean isHaltPalletActiveInFt() {
		return haltPalletActiveInFt;
	}

	public static boolean isHaltPalletActiveInHv() {
		return haltPalletActiveInHv;
	}

	public static boolean isHaltPalletActiveInIr() {
		return haltPalletActiveInIr;
	}

	public static boolean isHaltPalletActiveInCalib() {
		return haltPalletActiveInCalib;
	}

	public static boolean isHaltPalletActiveInVerific1Waiting() {
		return haltPalletActiveInVerific1Waiting;
	}

	public static boolean isHaltPalletActiveInVerific1() {
		return haltPalletActiveInVerific1;
	}

	public static boolean isHaltPalletActiveInSta1() {
		return haltPalletActiveInSta1;
	}

	public static boolean isHaltPalletActiveInSta2() {
		return haltPalletActiveInSta2;
	}

	public static boolean isHaltPalletActiveInRejection() {
		return haltPalletActiveInRejection;
	}

	public static boolean isHaltPalletActiveInUnloading() {
		return haltPalletActiveInUnloading;
	}

	public static void setHaltPalletActiveInFt(boolean haltPalletActiveInFt) {
		ConveyorDataManager.haltPalletActiveInFt = haltPalletActiveInFt;
	}

	public static void setHaltPalletActiveInHv(boolean haltPalletActiveInHv) {
		ConveyorDataManager.haltPalletActiveInHv = haltPalletActiveInHv;
	}

	public static void setHaltPalletActiveInIr(boolean haltPalletActiveInIr) {
		ConveyorDataManager.haltPalletActiveInIr = haltPalletActiveInIr;
	}

	public static void setHaltPalletActiveInCalib(boolean haltPalletActiveInCalib) {
		ConveyorDataManager.haltPalletActiveInCalib = haltPalletActiveInCalib;
	}

	public static void setHaltPalletActiveInVerific1Waiting(boolean haltPalletActiveInVerific1Waiting) {
		ConveyorDataManager.haltPalletActiveInVerific1Waiting = haltPalletActiveInVerific1Waiting;
	}

	public static void setHaltPalletActiveInVerific1(boolean haltPalletActiveInVerific1) {
		ConveyorDataManager.haltPalletActiveInVerific1 = haltPalletActiveInVerific1;
	}

	public static void setHaltPalletActiveInSta1(boolean haltPalletActiveInSta1) {
		ConveyorDataManager.haltPalletActiveInSta1 = haltPalletActiveInSta1;
	}

	public static void setHaltPalletActiveInSta2(boolean haltPalletActiveInSta2) {
		ConveyorDataManager.haltPalletActiveInSta2 = haltPalletActiveInSta2;
	}

	public static void setHaltPalletActiveInRejection(boolean haltPalletActiveInRejection) {
		ConveyorDataManager.haltPalletActiveInRejection = haltPalletActiveInRejection;
	}

	public static void setHaltPalletActiveInUnloading(boolean haltPalletActiveInUnloading) {
		ConveyorDataManager.haltPalletActiveInUnloading = haltPalletActiveInUnloading;
	}

	public static boolean isVerific1PalletsAllCleared() {
		return verific1PalletsAllCleared;
	}

	public static void setVerific1PalletsAllCleared(boolean verific1PalletsAllCleared) {
		ConveyorDataManager.verific1PalletsAllCleared = verific1PalletsAllCleared;
	}
}
