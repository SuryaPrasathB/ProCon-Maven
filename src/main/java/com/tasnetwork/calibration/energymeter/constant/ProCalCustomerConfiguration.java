package com.tasnetwork.calibration.energymeter.constant;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;

public class ProCalCustomerConfiguration {

	public static boolean IN_HOUSE_CALIBRATION_MODE = false;

	public static boolean NSOFT_SINGLE_PHASE_48_POSITION_2021 = false;
	public static boolean SANDS_HYBRID_40_POSITION_2021 = false;// true;//false; // 20 - first 20 - three phase and
																// second 20- single phase

	public static boolean LECS_3PHASE_03_POSITION_JAN_2022 = false;// false;//true;//false;

	public static boolean EAPL_3PHASE_00_POSITION_2022 = false;// false;//true;

	public static boolean RACANAA_3PHASE_00_POSITION_2022 = false;// true;//false;//true;

	public static boolean ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022 = false;// false;//true;//false;

	public static boolean ENERCENT_1PHASE_3POSITION_2022 = false;

	public static boolean KIGG_1PHASE_20POSITION_2022 = false;// true;//false;

	public static boolean LECS_3PHASE_20_POSITION_DEC_2022 = false;// false;//true;

	public static boolean NEOLINK_1PHASE_10_POSITION_DEC_2022 = false;// true;//;//true;//false;

	public static boolean FUJI_1PHASE_40_POSITION_MAR_2024 = false;// true;//false;

	public static boolean ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024 = false;// false;//true;

	public static boolean CONVEYOR_DEMO_3PHASE_2024 = false;;// true;

	public static boolean FUJI_1PHASE_PORTABLE_00_POSITION_JUL_2024 = false;
	public static boolean ELMEASURE_1PHASE_450VA_00_POSITION_JAN_2025 = false;

	// public static boolean ADHYA_3PHASE_3_POSITION_MAR_2024 = true;

	public static boolean LSCS_TEST_MODE = false;// true;//false;//true;//;//true;//false;

	public static boolean DEVSYS_CONVEYOR_1PHASE_POSITION_2024_CALIB = true;

	public static void Init() {

		ApplicationLauncher.logger.debug("ProCalCustomerConfiguration: init");
		if (IN_HOUSE_CALIBRATION_MODE) {

			ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE = true;
			ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE = false;
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 3;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = true;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false; // Racks to support both 3 phase and single phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 3;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			inHouseCalibrationInit1();

		} else if (NSOFT_SINGLE_PHASE_48_POSITION_2021) {

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = false;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 48;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = true;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = false;// true;
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;

			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;

			nSoftInit1();

		} else if (LECS_3PHASE_03_POSITION_JAN_2022) {
			// ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = false; //false for
			// testing with out rsm, disabled the flag
			// ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true; //true for testing with out
			// rsm, enabled the flag
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 3;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false; // Racks to support both 3 phase and single phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			lecsInit1();
		} else if (SANDS_HYBRID_40_POSITION_2021) {
			// ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = false; //false for
			// testing with out rsm, disabled the flag
			// ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true; //true for testing with out
			// rsm, enabled the flag
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 40;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = true;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = true; // Racks to support both 3 phase and single phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 40;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 20;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;

			sandsInit1();
		} else if (EAPL_3PHASE_00_POSITION_2022) {

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 1;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// true;//false;//false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true;
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;
			eaplInit1();
		} else if (RACANAA_3PHASE_00_POSITION_2022) {

			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;
			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 1;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//true;//false;//false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true;
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;
			racanaaInit1();
		} else if (FUJI_1PHASE_PORTABLE_00_POSITION_JUL_2024) {
			ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE = true;
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;
			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 1;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = true;// false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;// false;//true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;
			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;// true;
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;
			fujiPortable1Init();
		} else if (ELMEASURE_1PHASE_450VA_00_POSITION_JAN_2025) {
			ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE = true;
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;
			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 1;
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = true;// false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;//false;//true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;
			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;// true ;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 3;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true;// true;
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;
			fujiPortable1Init();
		} else if (ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022) {

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 10;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;// false;//true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// false;//true
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = true;// false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = true;// false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = true;// false; // Racks to support both 3 phase and single
																// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			electroByteInit1();

		} else if (ENERCENT_1PHASE_3POSITION_2022) {

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = true;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 3;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;// true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// false;//true
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;
			enercentInit1();

		} else if (KIGG_1PHASE_20POSITION_2022) {

			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = true;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 20;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;// true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 1;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = false;

			/**** for Semi Automatic configuration end ****/
			kigg2022_Init1();

		} else if (LECS_3PHASE_20_POSITION_DEC_2022) {

			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.CONSTANT_TEST_RESULT_LIMIT_FROM_CONFIG_ENABLED = true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 20;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;// false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;// false;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 20;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			lecs2022_Init2();
		} else if (NEOLINK_1PHASE_10_POSITION_DEC_2022) {

			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 10;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;// false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;// false;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = false;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 20;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			neoLink2022_Init();
		} else if (LSCS_TEST_MODE) {
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;

			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 10;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;// false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true;//false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;// false;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 20;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;

			lscsTestInit();
		} else if (FUJI_1PHASE_40_POSITION_MAR_2024) {
			// ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED = true;//true;
			ProcalFeatureEnable.LIVE_TABLE_EXECUTION_STATUS_DISPLAY = true;
			ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.HARMONICS_FEATURE_V2_ENABLED = true;
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = false;// true;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = false;// true;//true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = false;// true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 40;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = true;// false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = false;// false;
			ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;// true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true;//false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.BOFA_LDU_CONNECTED = true;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;//false;
			ProcalFeatureEnable.BOFA_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = false;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = true;// false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 20;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			fuji2024_Init();
		} else if (ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024) {
			// ProcalFeatureEnable.LSC_LDU_INVALID_DATA_READING_WORK_AROUND = true;
			ProcalFeatureEnable.LSCS_MASTER_1PHASE_3PHASE_OPTION_ENABLED = true;
			ConstantApp.HARMONIC_COMPONENT_MAX = 6;// 6 = up to 5th Order harmonics
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = false;// true;//true;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = false;// true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 3;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;// false;//true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = true;// false;
			ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = true;// false;//true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = true;// true;//false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = false;// false;//true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.BOFA_LDU_CONNECTED = false;// true;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = true;// true;//false;
			ProcalFeatureEnable.BOFA_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = true;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = true;// false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = true;// false; // Racks to support both 3 phase and single
																// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 6;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 4;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			adhya2024_Init();
		} else if (CONVEYOR_DEMO_3PHASE_2024) {
			ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED = true;//
			ProcalFeatureEnable.HARMONICS_FEATURE_V2_ENABLED = true;
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = false;// true;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = false;// true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 6;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = false;// true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = false;// false;
			ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED = true;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;// true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;
			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true;//false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;// true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.BOFA_LDU_CONNECTED = true;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = false;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = false;// true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;//false;
			ProcalFeatureEnable.BOFA_REFSTD_CONNECTED = true;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = false;// true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false;// false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = false;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = false;// true;//false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 10;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 6;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 20;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			fuji2024_Init();
		} else if (DEVSYS_CONVEYOR_1PHASE_POSITION_2024_CALIB) {

			ProcalFeatureEnable.CONVEYOR_REST_CLIENT_ENABLED = true;
			ProcalFeatureEnable.PROPOWER_SRC_ONLY = false;// true;
			ProcalFeatureEnable.LSCS_MASTER_1PHASE_3PHASE_OPTION_ENABLED = false;// true;
			ConstantApp.HARMONIC_COMPONENT_MAX = 6;// 6 = up to 5th Order harmonics
			ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED = true;
			ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED = true;// false;//false;//false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED = false;// true;//true;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED = false;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE = false;
			ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED = true;// true;
			ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION = false;// true;

			ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY = false;
			ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED = true;
			ProcalFeatureEnable.EXPORT_MODE_ENABLED = true;

			ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS = false;// true;
			ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK = 2;// 10
			ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD = false;// true;
			ProcalFeatureEnable.RACK_MCT_NCT_ENABLED = true;// false;//true;
			ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED = false;
			ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED = true;
			ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED = false;

			ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED = false;// false;
			ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED = false;
			ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED = false;// false;//true;

			ProcalFeatureEnable.CCUBE_LDU_CONNECTED = false;

			// ###############################################################
			ProcalFeatureEnable.LSCS_LDU_CONNECTED = false;// true;//false;//false;//true//####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.LDU_CONNECTED_NONE = true;// false;//true;//true; //####DEBUG_2022_09_08_REF_STD_KIGG
			ProcalFeatureEnable.BOFA_LDU_CONNECTED = false;// true;
			ProcalFeatureEnable.REFSTD_CONNECTED_NONE = true;
			ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE = true;// false;//true;
			// #########################################################

			ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.MTE_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.SANDS_REFSTD_CONNECTED = false;
			ProcalFeatureEnable.KRE_REFSTD_CONNECTED = false;// true;//false;
			ProcalFeatureEnable.BOFA_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED = false;// true;
			ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST = false;// true;

			ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED = true; /* false; */// true;//true;//
			ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED = false; // false;//false;
			ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED = true;// false;//true;
			ProcalFeatureEnable.ICT_INTERFACE_ENABLED = false;// true;//false;//true;//false;//true;
			ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED = true;
			ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED = true;// false;
			ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED = true;

			ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED = false;// false; // Racks to support both 3 phase and single
																	// phase
			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS = 1;
			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS = 3;// only 2 supported but positioned at
																					// 1st and 6th position;

			ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION = 1;
			// ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_END_POSITION = 40;

			ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION = 4;
			ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED = false;// true;//false;
			// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_END_POSITION = 20;
			ProcalFeatureEnable.EMH_PWR_SRC_VOLT_UNBALANCE_ZEROVOLT_WORKAROUND = false;

			/**** for Semi Automatic configuration begin ****/

			ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE = true;// false;

			/**** for Semi Automatic configuration end ****/

			adhya2024_Init();
		}

	}

	public static void nSoftInit1() {

		ApplicationLauncher.logger.debug("nSoftInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void sandsInit1() {

		ApplicationLauncher.logger.debug("sandsInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void inHouseCalibrationInit1() {

		ApplicationLauncher.logger.debug("inHouseCalibrationInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), false);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger
					.error("ProCalCustomerConfiguration: inHouseCalibrationInit1: Init:" + e.getMessage());
		}

	}

	public static void lecsInit1() {

		ApplicationLauncher.logger.debug("lecsInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void electroByteInit1() {

		ApplicationLauncher.logger.debug("electroByteInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void lecs2022_Init2() {

		ApplicationLauncher.logger.debug("kigg2022_Init1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void neoLink2022_Init() {

		ApplicationLauncher.logger.debug("neoLink2022_Init: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void adhya2024_Init() {

		ApplicationLauncher.logger.debug("adhya2024_Init: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DutCommand.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), true);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void fuji2024_Init() {

		ApplicationLauncher.logger.debug("fuji2024_Init: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// neutral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void lscsTestInit() {

		ApplicationLauncher.logger.debug("lscsTestInit: Feature Enable:  Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsTestInit: JSONException: Init:" + e.getMessage());
		}

	}

	public static void kigg2022_Init1() {

		ApplicationLauncher.logger.debug("kigg2022_Init1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void enercentInit1() {

		ApplicationLauncher.logger.debug("enercentInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), true);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void eaplInit1() {

		ApplicationLauncher.logger.debug("eaplInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), false);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void racanaaInit1() {

		ApplicationLauncher.logger.debug("racanaaInit1: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), false);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

	public static void fujiPortable1Init() {

		ApplicationLauncher.logger.debug("fujiPortable1Init: Entry");
		try {

			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Warmup.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.NoLoad.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.STA.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.ConstantTest.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Accuracy.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceVolt.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceFreq.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CuttingNuetral.toString(), false);// disabled
																													// missing
																													// nuetral
																													// feature
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.CustomTest.toString(), true);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.Repeatability.toString(), false);
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.SelfHeating.toString(), false);// false
			ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.DefaultType.toString(), false); // disabled
																												// default
																												// type
			if (!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.VoltageUnbalance.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.PhaseReversal.toString(), false);
				ProcalFeatureEnable.TEST_PROFILE_ENABLE_FEATURE.put(TestProfileType.InfluenceHarmonic.toString(),
						false);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("ProCalCustomerConfiguration: JSONException: Init:" + e.getMessage());
		}

	}

}
