package com.tasnetwork.calibration.energymeter.constant;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONException;
import org.json.JSONObject;
/*
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;*/

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

import javafx.fxml.Initializable;

public class ProcalFeatureEnable  {

	public static boolean CONVEYOR_REST_CLIENT_ENABLED = true;
	public static boolean AUTO_CALIBRATION_MODE_ENABLE_FEATURE = false;
	public static boolean METRICS_EXCEL_LOG_ENABLE_FEATURE = false;
	public static boolean STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;
	public static boolean POWER_SRC_SW_REBOOT_ENABLE_FEATURE = false;//true;
	public static boolean PHASE_DISPLAY_ENABLE_FEATURE = true;
	public static boolean RESULT_STATUS_DISPLAY_ENABLE_FEATURE = false;//This is to display result status on the table and reports
	public static boolean WARMUP_NO_OF_PULSES_DISPLAY_ENABLE_FEATURE = false;//true;
	public static boolean REF_STD_PHASE_180_FLAG = false; //For radiant reference standard 30 degree lag will be displayed as -30 degree. In case for other reference standard  if the display is 30 degree for the same input, then this flag should be set to true
	
	public static boolean EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = true;//For the HT CT meters for the unbalance voltage for Volt-R only, Volt-Y only and Volt-B only Test point failed with exceeding error limit. 
	//For example for Volt-R only test point, Y phase volt and B phase volt were set to 43 volt by the power source, hence built a logic to maintain other phase voltage to 1 to 10 volt, which can be configured in config.json as percentage parameter
	
	public static boolean TEST_ENABLE_FEATURE = true; // false means import mode, true means export mode
	
	public static boolean EXPORT_MODE_ENABLED = false;
	
	
	public static boolean MTE_POWER_SOURCE_CONNECTED = false;
	public static boolean LSCS_POWER_SOURCE_CONNECTED = true;
	public static boolean BOFA_POWER_SOURCE_CONNECTED = false;//true;
	public static boolean POWER_SOURCE_3PHASE_ENABLED = true;
	
	
	public static boolean DISPLAY_3PHASE_INSTANT_METRICS = true;//true;
	public static boolean REPORT_3PHASE_UNBALANCED_LOAD = true;
	
	
	public static boolean CCUBE_LDU_CONNECTED = false;
	public static boolean LSCS_LDU_CONNECTED = false;
	public static boolean BOFA_LDU_CONNECTED = false;//true;
	
	public static boolean RADIANT_REF_STD_2X_FAMILY_ENABLED = true;
	public static int TOTAL_NO_OF_SUPPORTED_RACK = 40;//3;//12;
	public static int RACK_MAX_POSITION = 48;

	
	public static final long I_MAPPING_SIZE = 15;
	public static final long PF_MAPPING_SIZE = 10;
	public static boolean REF_STD_CONST_CALCULATE = false;// true;//making this true will calculate the ref std const value for Wh and for false the value will be read from the config.json lookup
	public static boolean RACK_MCT_NCT_ENABLED = true;// feature to enable Main CT and Neutral CT feature
	
	public static boolean CREEP_KWH_READING_PROMPT_ENABLED = false;// for Creep-NoLoad Test , to Enable/Disable the user prompt for kWH reading from Meter DUT
	public static boolean STA_KWH_READING_PROMPT_ENABLED = false;// for Starting Current Test , to Enable/Disable the user prompt for kWH reading from Meter DUT
	
	public static boolean RUN_TYPE_TIME_BASED_ENABLED = false;
	
	public static boolean TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;//false;
	
	public static boolean REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = true;// to enable the reference standard instant metrics in Run Screen
	
	
	
	public static JSONObject TEST_PROFILE_ENABLE_FEATURE = new JSONObject() ;
	
	
	
	public static boolean REPORT_SUPPORTING_DATA_POPULATE_ENABLED = true; // feature to populate voltage and current , user name , time stamp on the report
	
	//public static boolean THREE_PHASE_INSTANT_METRICS_DISPLAY_ENABLED = false;vcb
	public static boolean SECONDARY_LDU_DISPLAY_ENABLED = true;
	
	public static boolean REFSTD_CONNECTED_NONE = false;
	public static boolean LDU_CONNECTED_NONE = false;
	public static boolean RADIANT_REFSTD_CONNECTED = false;
	public static boolean MTE_REFSTD_CONNECTED = false;
	public static boolean SANDS_REFSTD_CONNECTED = true;
	public static boolean BOFA_REFSTD_CONNECTED = false;
	
	public static boolean SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = true;// in sands some time PF values are displayed with negative -work around to fix the display
	
	public static boolean LSCS_APP_CONTROL_MODE_ENABLED = true;
	
	public static boolean LSCS_CALIBRATION_MODE_ENABLED = false;//true;
	
	public static boolean LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;
	
	public static boolean ICT_INTERFACE_ENABLED = false;
	public static boolean ICT_KRE_KE6323_CONNECTED = false;
	
	public static boolean MAIN_CT_FEATURE_ENABLED = true;
	public static boolean NEUTRAL_CT_FEATURE_ENABLED = true;
	
	
	public static boolean DEPLOY_MAKE_FIELD_ENABLED = false;
	
	public static boolean RACK_HYBRID_MODE_ENABLED = false; // Racks to support both 3 phase and single phase
	public static int HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 40;
	public static int HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 20;
	
	public static int HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
	//public static int HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;
	
	public static int HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
	
	public static boolean DUT_EXECUTION_PROCESS_IN_PARALLEL = true;
	
	public static boolean USER_ACCESS_CONTROL_ENABLED = true;
	
	public static boolean KRE_REFSTD_CONNECTED = false;//false;//true;
	
	public static boolean KIGGS_REFSTD_CONNECTED = false;
	public static boolean KIGGS_REFSTD_AUTO_CALCULATION = false;
	
	public static boolean MAINTENANCE_MODE_ENABLED = false;//true;//true;
	
	public static boolean PROPOWER_SRC_FEEDBACK_DISPLAY = false;
	
	public static boolean PROPOWER_SRC_ONLY = false;
	public static boolean POWERSOURCE_CONNECTED_NONE = false;
	
	public static boolean POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;
	
	public static boolean POWERSOURCE_MANUAL_MODE = false;
	
	public static boolean POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = false;
	
	public static boolean REPORT_GENERATION_V2_ENABLED = false;
	
	public static boolean DEPLOY_MODEL_NO_FIELD_ENABLED = true;
	
	public static boolean LICENSE_FEATURE_DISPLAY_ENABLED = true;
	
	public static boolean LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = false;//true;//false;
	
	public static boolean LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;//false;
	
	public static boolean HARMONICS_FEATURE_V2_ENABLED = false;
	public static boolean REFSTD_PORT_MANAGER_V2_ENABLED = false;//true;//true;
	public static boolean PWRSRC_PORT_MANAGER_V2_ENABLED = false;//true;
	
	public static boolean CONVEYOR_FEATURE_ENABLED =  false;//true;
	public static boolean SHUTDOWN_OPTIMISATION_ENABLED =  true;//true;
	
	public static boolean LSCS_MASTER_1PHASE_3PHASE_OPTION_ENABLED =  false;//true;
	
	public static boolean CONSTANT_TEST_RESULT_LIMIT_FROM_CONFIG_ENABLED =  false;//true;
	
	public static boolean PROCAL_LAB_MODE = false;
	
	public static boolean LIVE_TABLE_EXECUTION_STATUS_DISPLAY = false;
	
	public static boolean MODBUS_PLC_SLAVE_MODE = true;  // plc has no intelligence, it will enable/disable output based on java command
	
	public static boolean DUT_CMD_EXECUTE_MODE_V3 = true;
	
	
	public static boolean HARDWARE_ID_FEATURE_ENABLED = true;
	//public static boolean PROPOWER_SRC_ONLY = false;
	
	//public static boolean LSC_LDU_INVALID_DATA_READING_WORK_AROUND = false;//true;
	
	//public static int HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
	
	//public static final int LDU_AVERAGE_READING = 3;

/*	public static void Init() {
		// TODO Auto-generated method stub
		
		try {

			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(),false);// disabled missing nuetral feature
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(),true);
			TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(),false); // disabled default type
			if(!POWER_SOURCE_3PHASE_ENABLED){
				TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(),false);
				TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(),false);
				TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),false);
			}
			
		

		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("ConstantFeatureEnable: JSONException: Init:"+e.getMessage());
		}

		
	}*/


}
