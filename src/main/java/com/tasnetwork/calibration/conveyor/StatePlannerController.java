package com.tasnetwork.calibration.conveyor;

import java.awt.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.calib.CalibrationBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.calib.S01_check_for_pallet_at_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S041_start_CALIB_source;
import com.tasnetwork.calibration.conveyor.bay.calib.S042_Turn_On_Main_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S043_Turn_On_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S044_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S045_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S046_Turn_On_Meter_Relays;
import com.tasnetwork.calibration.conveyor.bay.calib.S051_stop_CALIB_source;
import com.tasnetwork.calibration.conveyor.bay.calib.S058_02_Turn_On_Meter_Relays;
import com.tasnetwork.calibration.conveyor.bay.calib.S058_03_Turn_On_Main_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S058_07_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S058_08_Turn_On_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S058_12_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_00_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_01_Turn_On_Main_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_02_Power_Source_Start_Main_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_03_Phase_Calibration;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_04_Current_Stop;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_05_Turn_Off_Main_CT_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_06_Turn_On_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_07_Power_Source_Start_Neutral_CT;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_08_Neutral_Calibration;
import com.tasnetwork.calibration.conveyor.bay.calib.S059_09_Stop_Execution;
import com.tasnetwork.calibration.conveyor.bay.calib.S08_check_for_pallet_at_Waiting_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S09_let_the_pallet_to_Waiting_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S12_close_stop_latch_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S13_open_stop_latch_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S29_turn_on_tower_lamp_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.calib.S30_turn_off_tower_lamp_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.CommunicationTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.comm.S01_check_for_pallet_at_Comm_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S05_communication_Test;
import com.tasnetwork.calibration.conveyor.bay.comm.S08_check_for_pallet_at_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S09_let_the_pallet_to_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S12_open_stop_latch_COMM_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S13_close_stop_latch_COMM_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S29_turn_on_tower_lamp_COMM_Bay;
import com.tasnetwork.calibration.conveyor.bay.comm.S30_turn_off_tower_lamp_COMM_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.FunctionalTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.ft.S00_DELAY_STATE_2sec;
import com.tasnetwork.calibration.conveyor.bay.ft.S01_check_for_pallet_at_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S02_let_the_pallet_to_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S03_close_the_fingerTip_Latch;
import com.tasnetwork.calibration.conveyor.bay.ft.S04_ensure_the_fingerTip_Latch_Closed;
import com.tasnetwork.calibration.conveyor.bay.ft.S05_qR_Code_Scanning_of_Pallet;
import com.tasnetwork.calibration.conveyor.bay.ft.S06_scanning_of_Meters_Presence;
import com.tasnetwork.calibration.conveyor.bay.ft.S070_opticalProbe_Scanning_of_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S071_start_FT_source;
import com.tasnetwork.calibration.conveyor.bay.ft.S072_ensure_FT_source_started;
import com.tasnetwork.calibration.conveyor.bay.ft.S073_Set_Serial_Numbers_On_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S074_Set_HardwareId_On_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S07_qR_Code_Scanning_of_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S0811_stop_FT_source;
import com.tasnetwork.calibration.conveyor.bay.ft.S081_stop_FT_source;
import com.tasnetwork.calibration.conveyor.bay.ft.S0821_ensure_FT_source_stopped;
import com.tasnetwork.calibration.conveyor.bay.ft.S082_ensure_FT_source_stopped;
import com.tasnetwork.calibration.conveyor.bay.ft.S08_functional_Test;
import com.tasnetwork.calibration.conveyor.bay.ft.S091_open_the_fingerTip_Latch;
import com.tasnetwork.calibration.conveyor.bay.ft.S09_open_the_fingerTip_Latch;
import com.tasnetwork.calibration.conveyor.bay.ft.S101_ensure_the_fingerTip_Latch_Opened;
import com.tasnetwork.calibration.conveyor.bay.ft.S10_ensure_the_fingerTip_Latch_Opened;
import com.tasnetwork.calibration.conveyor.bay.ft.S11_check_for_pallet_at_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S12_turn_on_divertor_relay_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S13_ensure_divertor_relay_turned_on_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S14_let_the_pallet_to_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S15_ensure_pallet_reached_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S16_check_for_pallet_at_Rejection_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S17_turn_off_divertor_relay_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S18_ensure_divertor_relay_turned_off_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S19_wait_for_reset_button_ip_Rejection_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S20_let_the_pallet_to_Rejection_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S21_ensure_pallet_reached_Reject_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S22_error_Handling;
import com.tasnetwork.calibration.conveyor.bay.ft.S23_idle_condition;
import com.tasnetwork.calibration.conveyor.bay.ft.S241_close_stop_latch_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S24_close_stop_latch_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S251_open_stop_latch_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S25_open_stop_latch_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S261_close_stop_latch_b4_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S26_close_stop_latch_b4_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S27_open_stop_latch_b4_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S28_check_for_stop_latch_B4_status_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S29_turn_on_tower_lamp1_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S301_turn_off_tower_lamp1_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ft.S30_turn_off_tower_lamp1_FT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.HighVoltageTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.hv.S01_check_for_pallet_at_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S02_qR_Code_Scanning_of_Pallet;
import com.tasnetwork.calibration.conveyor.bay.hv.S041_start_HV_source;
import com.tasnetwork.calibration.conveyor.bay.hv.S051_stop_HV_source;
import com.tasnetwork.calibration.conveyor.bay.hv.S05_high_Voltage_Test;
import com.tasnetwork.calibration.conveyor.bay.hv.S06_open_the_fingerTip_Latch;
import com.tasnetwork.calibration.conveyor.bay.hv.S07_ensure_the_fingerTip_Latch_Opened;
import com.tasnetwork.calibration.conveyor.bay.hv.S08_check_for_pallet_at_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S09_let_the_pallet_to_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S10_error_Handling;
import com.tasnetwork.calibration.conveyor.bay.hv.S11_idle_condition;
import com.tasnetwork.calibration.conveyor.bay.hv.S12_open_stop_latch_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S13_close_stop_latch_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S29_turn_on_tower_lamp_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S30_turn_off_tower_lamp_HVT_Bay;
import com.tasnetwork.calibration.conveyor.bay.hv.S420_ByPass_delay1;
import com.tasnetwork.calibration.conveyor.bay.ir.InsulationResistanceTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.ir.S01_check_for_pallet_at_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S05_insulationResistance_Test;
import com.tasnetwork.calibration.conveyor.bay.ir.S08_check_for_pallet_at_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S09_let_the_pallet_to_CALIB_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S12_close_stop_latch_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S13_open_stop_latch_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S29_turn_on_tower_lamp_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.ir.S30_turn_off_tower_lamp_IRT_Bay;
import com.tasnetwork.calibration.conveyor.bay.loading.LoadingBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.loading.S01_check_for_LoadingBay_pushButton_status;
import com.tasnetwork.calibration.conveyor.bay.loading.S02_let_pallet_outside_loading_bay;
import com.tasnetwork.calibration.conveyor.bay.loading.S03_error_Handling;
import com.tasnetwork.calibration.conveyor.bay.loading.S04_idle_condition;
import com.tasnetwork.calibration.conveyor.bay.loading.S05_open_stop_latch_Loading_Bay;
import com.tasnetwork.calibration.conveyor.bay.loading.S06_close_stop_latch_Loading_Bay;
import com.tasnetwork.calibration.conveyor.bay.rejection.RejectionBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.rejection.S01_check_for_pallets_at_Reject_Bay;
import com.tasnetwork.calibration.conveyor.bay.rejection.S021_check_for_pallet_removed_at_Reject_Bay;
import com.tasnetwork.calibration.conveyor.bay.rejection.S02_qR_Code_Scanning_of_Rejected_Pallet;
import com.tasnetwork.calibration.conveyor.bay.rejection.S29_turn_on_tower_lamp_Rejection_Bay;
import com.tasnetwork.calibration.conveyor.bay.rejection.S30_turn_off_tower_lamp_Rejection_Bay;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S01_check_for_pallets_at_SCT_NLT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S02_qR_Code_Scanning_of_Pallet_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S03_close_the_fingerTip_Latch_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S041_Start_STA_NLDT_Bay1_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S042_Start_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S043_Get_Test_Point_Immediately_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S043_Get_Test_Point_Status_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S044_Close_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S045_Select_Run_Project_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S046_Stop_Execution_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S04_ensure_the_fingerTip_Latch_Closed_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S051_Stop_STA_NLDT_Bay1_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S05_sc_nl_Test_at_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S06_open_the_fingerTip_Latch_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S07_ensure_the_fingerTip_Latch_Opened_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S08_check_for_pallets_at_outArea_1_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S09_check_for_pallets_at_outArea_2_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S10_acquire_the_pallet_release_semaphore_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S11_let_the_pallets_to_Comm_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S12_release_the_pallet_release_semaphore_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S13_error_Handling_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S14_idle_condition_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S15_close_stop_latch_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S16_open_stop_latch_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S17_close_stop_latch2_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S18_open_stop_latch2_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S19_release_pallets_from_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S27_turn_on_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S28_turn_off_motor_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S29_turn_on_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S30_turn_off_tower_lamp_STA_NLDT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S31_turn_on_Diverter_STA_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S32_turn_off_Diverter_STA_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.S420_ByPass_delay1_Bay1;
import com.tasnetwork.calibration.conveyor.bay.sta_nld1.STA_NoLoadTestBay1SingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S01_check_for_pallets_at_SCT_NLT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S02_qR_Code_Scanning_of_Pallet_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S03_close_the_fingerTip_Latch_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S041_Start_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S042_Start_Execution_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S043_Get_Test_Point_Immediately_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S043_Get_Test_Point_Status_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S044_Close_Run_Project_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S045_Select_Run_Project_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S046_Stop_Execution_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S04_ensure_the_fingerTip_Latch_Closed_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S051_Stop_STA_NLDT_Bay2_Source;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S05_sc_nl_Test_at_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S06_open_the_fingerTip_Latch_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S07_ensure_the_fingerTip_Latch_Opened_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S08_check_for_pallets_at_outArea_1_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S09_check_for_pallets_at_outArea_2_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S10_acquire_the_pallet_release_semaphore_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S11_let_the_pallets_to_Comm_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S12_release_the_pallet_release_semaphore_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S13_error_Handling_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S14_idle_condition_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S15_open_stop_latch_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S16_close_stop_latch_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S27_turn_on_motor_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S28_turn_off_motor_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S29_turn_on_tower_lamp_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S30_turn_off_tower_lamp_STA_NLDT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S420_ByPass_delay1_Bay2;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.STA_NoLoadTestBay2SingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.unloading.S01_check_for_pallet_at_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S03_check_for_empty_pallet;
import com.tasnetwork.calibration.conveyor.bay.unloading.S04_check_for_failed_meters;
import com.tasnetwork.calibration.conveyor.bay.unloading.S05_indicate_the_status_of_meters;
import com.tasnetwork.calibration.conveyor.bay.unloading.S06_check_for_alarm_pushButton_status;
import com.tasnetwork.calibration.conveyor.bay.unloading.S07_check_for_loadingBay_pushButton_status;
import com.tasnetwork.calibration.conveyor.bay.unloading.S08_check_for_pallet_at_Loading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S09_let_the_pallet_to_Loading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S12_close_stop_latch_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S13_open_stop_latch_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S29_turn_on_tower_lamp_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.S30_turn_off_tower_lamp_Unloading_Bay;
import com.tasnetwork.calibration.conveyor.bay.unloading.UnloadingBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.verific.S01_check_for_pallets_at_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S041_Load_Project_From_DB;
import com.tasnetwork.calibration.conveyor.bay.verific.S042_Start_Execution;
import com.tasnetwork.calibration.conveyor.bay.verific.S043_Get_Test_Point_Immediately;
import com.tasnetwork.calibration.conveyor.bay.verific.S043_Get_Test_Point_Status;
import com.tasnetwork.calibration.conveyor.bay.verific.S044_Close_Run_Project;
import com.tasnetwork.calibration.conveyor.bay.verific.S045_Select_Run_Project;
import com.tasnetwork.calibration.conveyor.bay.verific.S046_Stop_Execution;
import com.tasnetwork.calibration.conveyor.bay.verific.S051_Stop_VerificTest_Source;
import com.tasnetwork.calibration.conveyor.bay.verific.S05_verification_Test;
import com.tasnetwork.calibration.conveyor.bay.verific.S08_check_for_pallets_at_SCT_NLT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.verific.S09_turn_off_Diverter_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S10_ensure_divertor_relay_turned_off_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S11_let_the_pallets_to_SCT_NLT_Bay1;
import com.tasnetwork.calibration.conveyor.bay.verific.S12_check_for_pallets_at_SCT_NLT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.S13_turn_on_Diverter_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S14_ensure_divertor_relay_turned_on_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S15_let_the_pallets_to_SCT_NLT_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.S16_check_for_pallets_at_both_bays_in_loop;
import com.tasnetwork.calibration.conveyor.bay.verific.S17_error_Handling;
import com.tasnetwork.calibration.conveyor.bay.verific.S18_idle_condition;
import com.tasnetwork.calibration.conveyor.bay.verific.S191_open_stop_latch_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S19_open_stop_latch_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S201_close_stop_latch_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S20_close_stop_latch_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S211_open_stop_latch2_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S21_open_stop_latch2_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S221_close_stop_latch2_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S22_close_stop_latch2_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S27_turn_on_motor_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S28_turn_off_motor_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S29_turn_on_tower_lamp_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.S30_turn_off_tower_lamp_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific.VerificationTestBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S01_check_for_all_pallets_at_Waiting_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S02_check_for_pallets_at_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S03_let_the_pallet_to_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S04_ensure_all_pallets_reached_Verific_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S05_error_Handling;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S06_idle_condition;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S07_open_stop_latch_Waiting_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.S08_close_stop_latch_Waiting_Bay;
import com.tasnetwork.calibration.conveyor.bay.verific_waiting.WaitngBaySingleStateTestRun;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantStateModes;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.WindowManager;
import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;

public class StatePlannerController implements Initializable {

	/*
	 * @FXML private TextField txtNoOfPosition;
	 * 
	 * private static TextField ref_txtNoOfPosition;
	 */

	@FXML
	public Button buttonAddRow;
	@FXML
	public Button buttonDeleteRow;
	@FXML
	public Button buttonSaveStateFlow;
	@FXML
	public Button buttonLoadStates;

	public static TableView<StateFlow> tableStatePlanner_FtBay_UI = new TableView<StateFlow>();

	@FXML
	private ComboBox<String> cmbBxSelectBayType;
	private static ComboBox<String> ref_cmbBxSelectBayType;

	@FXML
	private ComboBox<String> cmbBxSelectSingleState;
	public static ComboBox<String> ref_cmbBxSelectSingleState;

	private AtomicInteger serialNoAtomic = new AtomicInteger(1);

	private final Map<String, String> stateCodeMap = new HashMap<>();

	@FXML
	public TableView<StateFlow> tableStatePlanner;
	@FXML
	public TableColumn<StateFlow, Integer> columnSerialNo;
	@FXML
	public TableColumn<StateFlow, String> columnPath;
	@FXML
	public TableColumn<StateFlow, String> columnState;
	@FXML
	public TableColumn<StateFlow, String> columnStateErrorCode;
	@FXML
	public TableColumn<StateFlow, String> columnSuccess;
	@FXML
	public TableColumn<StateFlow, String> columnSuccessErrorCode;
	@FXML
	public TableColumn<StateFlow, String> columnFailed;
	@FXML
	public TableColumn<StateFlow, String> columnFailedErrorCode;

	@FXML
	private ComboBox<String> cmbModeSelection; // Added ComboBox for mode selection

	Timer funtionalBaySingleStateTaskTimer;
	Timer hvtBaySingleStateTaskTimer;
	Timer irBaySingleStateTaskTimer;
	Timer calibBaySingleStateTaskTimer;
	Timer commBaySingleStateTaskTimer;
	Timer loadingBaySingleStateTaskTimer;
	Timer sctNltBay1SingleStateTaskTimer;
	Timer sctNltBay2SingleStateTaskTimer;
	Timer unloadingBaySingleStateTaskTimer;
	Timer verificBaySingleStateTaskTimer;
	Timer waitingBaySingleStateTaskTimer;
	Timer rejectionBaySingleStateTaskTimer;

	private final ObservableList<String> stateNames = FXCollections.observableArrayList();

	private final ObservableList<String> ftBayStateNames = FXCollections.observableArrayList(
			S00_DELAY_STATE_2sec.class.getSimpleName(),
			S01_check_for_pallet_at_FT_Bay.class.getSimpleName(),
			S02_let_the_pallet_to_FT_Bay.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S05_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S06_scanning_of_Meters_Presence.class.getSimpleName(),
			S07_qR_Code_Scanning_of_Meters.class.getSimpleName(),
			S070_opticalProbe_Scanning_of_Meters.class.getSimpleName(),
			S071_start_FT_source.class.getSimpleName(),
			S072_ensure_FT_source_started.class.getSimpleName(),
			S073_Set_Serial_Numbers_On_Meters.class.getSimpleName(),
			S074_Set_HardwareId_On_Meters.class.getSimpleName(),
			S08_functional_Test.class.getSimpleName(),
			S081_stop_FT_source.class.getSimpleName(),
			S0811_stop_FT_source.class.getSimpleName(),
			S082_ensure_FT_source_stopped.class.getSimpleName(),
			S0821_ensure_FT_source_stopped.class.getSimpleName(),
			S09_open_the_fingerTip_Latch.class.getSimpleName(),
			S091_open_the_fingerTip_Latch.class.getSimpleName(),
			S10_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S101_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S11_check_for_pallet_at_HVT_Bay.class.getSimpleName(),
			S12_turn_on_divertor_relay_FT_Bay.class.getSimpleName(),
			S13_ensure_divertor_relay_turned_on_FT_Bay.class.getSimpleName(),
			S14_let_the_pallet_to_HVT_Bay.class.getSimpleName(),
			S15_ensure_pallet_reached_HVT_Bay.class.getSimpleName(),
			S16_check_for_pallet_at_Rejection_Bay.class.getSimpleName(),
			S17_turn_off_divertor_relay_FT_Bay.class.getSimpleName(),
			S18_ensure_divertor_relay_turned_off_FT_Bay.class.getSimpleName(),
			S19_wait_for_reset_button_ip_Rejection_Bay.class.getSimpleName(),
			S20_let_the_pallet_to_Rejection_Bay.class.getSimpleName(),
			S21_ensure_pallet_reached_Reject_Bay.class.getSimpleName(),
			S22_error_Handling.class.getSimpleName(),
			S23_idle_condition.class.getSimpleName(),
			S24_close_stop_latch_FT_Bay.class.getSimpleName(),
			S241_close_stop_latch_FT_Bay.class.getSimpleName(),
			S25_open_stop_latch_FT_Bay.class.getSimpleName(),
			S251_open_stop_latch_FT_Bay.class.getSimpleName(),
			S26_close_stop_latch_b4_FT_Bay.class.getSimpleName(),
			S261_close_stop_latch_b4_FT_Bay.class.getSimpleName(),
			S27_open_stop_latch_b4_FT_Bay.class.getSimpleName(),
			S28_check_for_stop_latch_B4_status_FT_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp1_FT_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp1_FT_Bay.class.getSimpleName(),
			S301_turn_off_tower_lamp1_FT_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> hvtBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallet_at_HVT_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S041_start_HV_source.class.getSimpleName(),
			S05_high_Voltage_Test.class.getSimpleName(),
			S051_stop_HV_source.class.getSimpleName(),
			S06_open_the_fingerTip_Latch.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S08_check_for_pallet_at_IRT_Bay.class.getSimpleName(),
			S09_let_the_pallet_to_IRT_Bay.class.getSimpleName(),
			S10_error_Handling.class.getSimpleName(),
			S11_idle_condition.class.getSimpleName(),
			S12_open_stop_latch_HVT_Bay.class.getSimpleName(),
			S13_close_stop_latch_HVT_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_HVT_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_HVT_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> irtBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallet_at_IRT_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S05_insulationResistance_Test.class.getSimpleName(),
			S06_open_the_fingerTip_Latch.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S08_check_for_pallet_at_CALIB_Bay.class.getSimpleName(),
			S09_let_the_pallet_to_CALIB_Bay.class.getSimpleName(),
			S10_error_Handling.class.getSimpleName(),
			S11_idle_condition.class.getSimpleName(),
			S12_close_stop_latch_IRT_Bay.class.getSimpleName(),
			S13_open_stop_latch_IRT_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_IRT_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_IRT_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> calibBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallet_at_CALIB_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S041_start_CALIB_source.class.getSimpleName(),
			S042_Turn_On_Main_CT.class.getSimpleName(),
			S043_Turn_On_Neutral_CT.class.getSimpleName(),
			S044_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S045_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S046_Turn_On_Meter_Relays.class.getSimpleName(),
			S051_stop_CALIB_source.class.getSimpleName(),
			S058_02_Turn_On_Meter_Relays.class.getSimpleName(),
			S058_03_Turn_On_Main_CT.class.getSimpleName(),
			S058_07_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S058_08_Turn_On_Neutral_CT.class.getSimpleName(),
			S058_12_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S059_00_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S059_01_Turn_On_Main_CT.class.getSimpleName(),
			S059_02_Power_Source_Start_Main_CT.class.getSimpleName(),
			S059_03_Phase_Calibration.class.getSimpleName(),
			S059_04_Current_Stop.class.getSimpleName(),
			S059_05_Turn_Off_Main_CT_Neutral_CT.class.getSimpleName(),
			S059_06_Turn_On_Neutral_CT.class.getSimpleName(),
			S059_07_Power_Source_Start_Neutral_CT.class.getSimpleName(),
			S059_08_Neutral_Calibration.class.getSimpleName(),
			S059_09_Stop_Execution.class.getSimpleName(),
			S06_open_the_fingerTip_Latch.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S08_check_for_pallet_at_Waiting_Bay.class.getSimpleName(),
			S09_let_the_pallet_to_Waiting_Bay.class.getSimpleName(),
			S10_error_Handling.class.getSimpleName(),
			S11_idle_condition.class.getSimpleName(),
			S12_close_stop_latch_CALIB_Bay.class.getSimpleName(),
			S13_open_stop_latch_CALIB_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_CALIB_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_CALIB_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> commBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallet_at_Comm_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S05_communication_Test.class.getSimpleName(),
			S06_open_the_fingerTip_Latch.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S08_check_for_pallet_at_Unloading_Bay.class.getSimpleName(),
			S09_let_the_pallet_to_Unloading_Bay.class.getSimpleName(),
			S10_error_Handling.class.getSimpleName(),
			S11_idle_condition.class.getSimpleName(),
			S12_open_stop_latch_COMM_Bay.class.getSimpleName(),
			S13_close_stop_latch_COMM_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_COMM_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_COMM_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> loadingBayStateNames = FXCollections.observableArrayList(
			S01_check_for_LoadingBay_pushButton_status.class.getSimpleName(),
			S02_let_pallet_outside_loading_bay.class.getSimpleName(),
			S03_error_Handling.class.getSimpleName(),
			S04_idle_condition.class.getSimpleName(),
			S05_open_stop_latch_Loading_Bay.class.getSimpleName(),
			S06_close_stop_latch_Loading_Bay.class.getSimpleName());

	private final ObservableList<String> rejectionBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallets_at_Reject_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Rejected_Pallet.class.getSimpleName(),
			S021_check_for_pallet_removed_at_Reject_Bay.class.getSimpleName(),
			S03_error_Handling.class.getSimpleName(),
			S04_idle_condition.class.getSimpleName(),
			S29_turn_on_tower_lamp_Rejection_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_Rejection_Bay.class.getSimpleName());

	private final ObservableList<String> sctNltBay1StateNames = FXCollections.observableArrayList(
			S01_check_for_pallets_at_SCT_NLT_Bay1.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet_Bay1.class.getSimpleName(),
			S03_close_the_fingerTip_Latch_Bay1.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed_Bay1.class.getSimpleName(),
			S041_Start_STA_NLDT_Bay1_Source.class.getSimpleName(),
			S042_Start_Execution_Bay1.class.getSimpleName(),
			S043_Get_Test_Point_Immediately_Bay1.class.getSimpleName(),
			S043_Get_Test_Point_Status_Bay1.class.getSimpleName(),
			S044_Close_Run_Project_Bay1.class.getSimpleName(),
			S045_Select_Run_Project_Bay1.class.getSimpleName(),
			S046_Stop_Execution_Bay1.class.getSimpleName(),
			S05_sc_nl_Test_at_Bay1.class.getSimpleName(),
			S051_Stop_STA_NLDT_Bay1_Source.class.getSimpleName(),
			S06_open_the_fingerTip_Latch_Bay1.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened_Bay1.class.getSimpleName(),
			S08_check_for_pallets_at_outArea_1_Bay1.class.getSimpleName(),
			S09_check_for_pallets_at_outArea_2_Bay1.class.getSimpleName(),
			S10_acquire_the_pallet_release_semaphore_Bay1.class.getSimpleName(),
			S11_let_the_pallets_to_Comm_Bay1.class.getSimpleName(),
			S12_release_the_pallet_release_semaphore_Bay1.class.getSimpleName(),
			S13_error_Handling_Bay1.class.getSimpleName(),
			S14_idle_condition_Bay1.class.getSimpleName(),
			S15_close_stop_latch_STA_NLDT_Bay1.class.getSimpleName(),
			S16_open_stop_latch_STA_NLDT_Bay1.class.getSimpleName(),
			S17_close_stop_latch2_STA_NLDT_Bay1.class.getSimpleName(),
			S18_open_stop_latch2_STA_NLDT_Bay1.class.getSimpleName(),
			S19_release_pallets_from_STA_NLDT_Bay1.class.getSimpleName(),
			S27_turn_on_motor_STA_NLDT_Bay1.class.getSimpleName(),
			S28_turn_off_motor_STA_NLDT_Bay1.class.getSimpleName(),
			S29_turn_on_tower_lamp_STA_NLDT_Bay1.class.getSimpleName(),
			S30_turn_off_tower_lamp_STA_NLDT_Bay1.class.getSimpleName(),
			S31_turn_on_Diverter_STA_Bay1.class.getSimpleName(),
			S32_turn_off_Diverter_STA_Bay1.class.getSimpleName(),
			S420_ByPass_delay1_Bay1.class.getSimpleName());

	private final ObservableList<String> sctNltBay2StateNames = FXCollections.observableArrayList(
			S01_check_for_pallets_at_SCT_NLT_Bay2.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet_Bay2.class.getSimpleName(),
			S03_close_the_fingerTip_Latch_Bay2.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed_Bay2.class.getSimpleName(),
			S041_Start_STA_NLDT_Bay2_Source.class.getSimpleName(),
			S042_Start_Execution_Bay2.class.getSimpleName(),
			S043_Get_Test_Point_Immediately_Bay2.class.getSimpleName(),
			S043_Get_Test_Point_Status_Bay2.class.getSimpleName(),
			S044_Close_Run_Project_Bay2.class.getSimpleName(),
			S045_Select_Run_Project_Bay2.class.getSimpleName(),
			S046_Stop_Execution_Bay2.class.getSimpleName(),
			S05_sc_nl_Test_at_Bay2.class.getSimpleName(),
			S051_Stop_STA_NLDT_Bay2_Source.class.getSimpleName(),
			S06_open_the_fingerTip_Latch_Bay2.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened_Bay2.class.getSimpleName(),
			S08_check_for_pallets_at_outArea_1_Bay2.class.getSimpleName(),
			S09_check_for_pallets_at_outArea_2_Bay2.class.getSimpleName(),
			S10_acquire_the_pallet_release_semaphore_Bay2.class.getSimpleName(),
			S11_let_the_pallets_to_Comm_Bay2.class.getSimpleName(),
			S12_release_the_pallet_release_semaphore_Bay2.class.getSimpleName(),
			S13_error_Handling_Bay2.class.getSimpleName(),
			S14_idle_condition_Bay2.class.getSimpleName(),
			S15_open_stop_latch_STA_NLDT_Bay2.class.getSimpleName(),
			S16_close_stop_latch_STA_NLDT_Bay2.class.getSimpleName(),
			S27_turn_on_motor_STA_NLDT_Bay2.class.getSimpleName(),
			S28_turn_off_motor_STA_NLDT_Bay2.class.getSimpleName(),
			S29_turn_on_tower_lamp_STA_NLDT_Bay2.class.getSimpleName(),
			S30_turn_off_tower_lamp_STA_NLDT_Bay2.class.getSimpleName(),
			S420_ByPass_delay1_Bay2.class.getSimpleName());

	private final ObservableList<String> unloadingBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallet_at_Unloading_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_check_for_empty_pallet.class.getSimpleName(),
			S04_check_for_failed_meters.class.getSimpleName(),
			S05_indicate_the_status_of_meters.class.getSimpleName(),
			S06_check_for_alarm_pushButton_status.class.getSimpleName(),
			S07_check_for_loadingBay_pushButton_status.class.getSimpleName(),
			S08_check_for_pallet_at_Loading_Bay.class.getSimpleName(),
			S09_let_the_pallet_to_Loading_Bay.class.getSimpleName(),
			S10_error_Handling.class.getSimpleName(),
			S11_idle_condition.class.getSimpleName(),
			S12_close_stop_latch_Unloading_Bay.class.getSimpleName(),
			S13_open_stop_latch_Unloading_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_Unloading_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_Unloading_Bay.class.getSimpleName());

	private final ObservableList<String> verificBayStateNames = FXCollections.observableArrayList(
			S01_check_for_pallets_at_Verific_Bay.class.getSimpleName(),
			S02_qR_Code_Scanning_of_Pallet.class.getSimpleName(),
			S03_close_the_fingerTip_Latch.class.getSimpleName(),
			S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(),
			S041_Load_Project_From_DB.class.getSimpleName(),
			S042_Start_Execution.class.getSimpleName(),
			S043_Get_Test_Point_Immediately.class.getSimpleName(),
			S043_Get_Test_Point_Status.class.getSimpleName(),
			S044_Close_Run_Project.class.getSimpleName(),
			S045_Select_Run_Project.class.getSimpleName(),
			S046_Stop_Execution.class.getSimpleName(),
			S05_verification_Test.class.getSimpleName(),
			S051_Stop_VerificTest_Source.class.getSimpleName(),
			S06_open_the_fingerTip_Latch.class.getSimpleName(),
			S07_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(),
			S08_check_for_pallets_at_SCT_NLT_Bay1.class.getSimpleName(),
			S09_turn_off_Diverter_Verific_Bay.class.getSimpleName(),
			S10_ensure_divertor_relay_turned_off_Verific_Bay.class.getSimpleName(),
			S11_let_the_pallets_to_SCT_NLT_Bay1.class.getSimpleName(),
			S12_check_for_pallets_at_SCT_NLT_Bay2.class.getSimpleName(),
			S13_turn_on_Diverter_Verific_Bay.class.getSimpleName(),
			S14_ensure_divertor_relay_turned_on_Verific_Bay.class.getSimpleName(),
			S15_let_the_pallets_to_SCT_NLT_Bay2.class.getSimpleName(),
			S16_check_for_pallets_at_both_bays_in_loop.class.getSimpleName(),
			S17_error_Handling.class.getSimpleName(),
			S18_idle_condition.class.getSimpleName(),
			S19_open_stop_latch_Verific_Bay.class.getSimpleName(),
			S191_open_stop_latch_Verific_Bay.class.getSimpleName(),
			S20_close_stop_latch_Verific_Bay.class.getSimpleName(),
			S201_close_stop_latch_Verific_Bay.class.getSimpleName(),
			S21_open_stop_latch2_Verific_Bay.class.getSimpleName(),
			S211_open_stop_latch2_Verific_Bay.class.getSimpleName(),
			S22_close_stop_latch2_Verific_Bay.class.getSimpleName(),
			S221_close_stop_latch2_Verific_Bay.class.getSimpleName(),
			S27_turn_on_motor_Verific_Bay.class.getSimpleName(),
			S28_turn_off_motor_Verific_Bay.class.getSimpleName(),
			S29_turn_on_tower_lamp_Verific_Bay.class.getSimpleName(),
			S30_turn_off_tower_lamp_Verific_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<String> waitingBayStateNames = FXCollections.observableArrayList(
			S01_check_for_all_pallets_at_Waiting_Bay.class.getSimpleName(),
			S02_check_for_pallets_at_Verific_Bay.class.getSimpleName(),
			S03_let_the_pallet_to_Verific_Bay.class.getSimpleName(),
			S04_ensure_all_pallets_reached_Verific_Bay.class.getSimpleName(),
			S05_error_Handling.class.getSimpleName(),
			S06_idle_condition.class.getSimpleName(),
			S07_open_stop_latch_Waiting_Bay.class.getSimpleName(),
			S08_close_stop_latch_Waiting_Bay.class.getSimpleName(),
			S420_ByPass_delay1.class.getSimpleName());

	private final ObservableList<StateFlow> stateFlowRows = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		refAssignment();
		guiInit();
	}

	public void refAssignment() {
		ref_cmbBxSelectBayType = cmbBxSelectBayType;
		ref_cmbBxSelectSingleState = cmbBxSelectSingleState;
	}

	public void guiInit() {

		ArrayList<String> bayList = (ArrayList<String>) ConstantConveyor.getBayLookup().keySet().stream()
				.collect(Collectors.toList());
		ref_cmbBxSelectBayType.getItems().add("Select Bay");
		ref_cmbBxSelectBayType.getItems().addAll(bayList);
		ref_cmbBxSelectBayType.getSelectionModel().select(0);

		cmbModeSelection.getItems().addAll(ConstantStateModes.RUN, ConstantStateModes.STOP, ConstantStateModes.RESET,
				ConstantStateModes.BAY_BYPASS);
		cmbModeSelection.getSelectionModel().select("RUN"); // Set default selection
		cmbModeSelection.valueProperty().addListener((observable, oldValue, newValue) -> {
			ApplicationLauncher.logger.debug(newValue + " is selected for execution mode");
		});

		statePlannerGuiInit();
	}

	@FXML
	public void loadBayStateOnClick() {
		String selectedBayType = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		stateNames.clear();
		tableStatePlanner.getItems().clear();
		ApplicationLauncher.logger.error("loadBayState : selectedBayType : " + selectedBayType);

		if (selectedBayType.equals("Select Bay")) {
			// Handle the case for Select Bay
		} else if (selectedBayType.equals(ConstantConveyor.FT_BAY_DISPLAY_NAME)) {// "Functional Bay")) {nn n n
			stateNames.addAll(ftBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.HV_BAY_DISPLAY_NAME)) {// "High Voltage Bay")) {
			stateNames.addAll(hvtBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.IR_BAY_DISPLAY_NAME)) {// "IR Bay")) {
			stateNames.addAll(irtBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.CALIBRATION_BAY_DISPLAY_NAME)) {// "Calibration Bay")) {
			stateNames.addAll(calibBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.COMMUNICATION_BAY_DISPLAY_NAME)) {// "Communication Bay")) {
			stateNames.addAll(commBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.LOADING_BAY_DISPLAY_NAME)) {// "Loading Bay")) {
			stateNames.addAll(loadingBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.STA_NLD1_BAY_DISPLAY_NAME)) {// "SCT NLT Bay 1")) {
			stateNames.addAll(sctNltBay1StateNames);
		} else if (selectedBayType.equals(ConstantConveyor.STA_NLD2_BAY_DISPLAY_NAME)) {// "SCT NLT Bay 2")) {
			stateNames.addAll(sctNltBay2StateNames);
		} else if (selectedBayType.equals(ConstantConveyor.UNLOADING_BAY_DISPLAY_NAME)) {// "Unloading Bay")) {
			stateNames.addAll(unloadingBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.VERIFICATION_BAY_DISPLAY_NAME)) {// "Verification Bay")) {
			stateNames.addAll(verificBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.WAITING_BAY_DISPLAY_NAME)) {// "Waiting Bay")) {
			stateNames.addAll(waitingBayStateNames);
		} else if (selectedBayType.equals(ConstantConveyor.REJECTION_BAY_DISPLAY_NAME)) {// "Waiting Bay")) {
			stateNames.addAll(rejectionBayStateNames);
		}

		statePlannerGuiInit();
		loadBayStatesFromDatabase();

		// Populate the single state combo box
		ref_cmbBxSelectSingleState.getItems().clear(); // Clear existing items
		ref_cmbBxSelectSingleState.getItems().addAll(stateNames); // Add new state names

	}

	public void statePlannerGuiInit() {

		// ==================== STATE PLANNER
		// ==================================================================//
		ApplicationLauncher.logger.info("StatePlanner : initialize : Entry");

		columnSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		columnPath.setCellValueFactory(data -> data.getValue().getPathProperty());
		columnState.setCellValueFactory(data -> data.getValue().getStateProperty());
		columnStateErrorCode.setCellValueFactory(data -> data.getValue().getErrorCodeProperty());
		columnSuccess.setCellValueFactory(data -> data.getValue().getIfSuccessProperty());
		columnSuccessErrorCode.setCellValueFactory(data -> data.getValue().getIfSuccessErrorCodeProperty());
		columnFailed.setCellValueFactory(data -> data.getValue().getIfFailedProperty());
		columnFailedErrorCode.setCellValueFactory(data -> data.getValue().getIfFailedErrorCodeProperty());

		// Set the columns as editable
		tableStatePlanner.setEditable(true);

		columnState.setEditable(true);
		columnSuccess.setEditable(true);

		// Set ComboBox for State and Success columns (dropdown)
		columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));
		columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		// No drop-down for the Failed column, it will auto-update based on other
		// columns
		// columnFailed.setCellFactory(TextFieldTableCell.forTableColumn());

		// Populate table with initial dummy data
		stateFlowRows.addAll(

		);

		tableStatePlanner.setItems(stateFlowRows);
		tableStatePlanner.refresh();

		// Initialize stateCodeMap
		initializeStateCodeMap();
		// Set up the table (this includes setting up ComboBox for the State column)
		setupTable();

	}

	public void loadBayStatesFromDatabase() {
		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Entry");

		// Get the selected bay name from the combo box
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Bay Name : " + bayName);

		// Clear the TableView before populating it
		tableStatePlanner.getItems().clear();

		String bayKey = ConstantConveyor.getBayLookup().get(bayName);

		String executionMode = getExecutionMode();

		try {
			if (bayKey == null || bayKey.isEmpty() || bayKey.equals("Select Bay")) {
				ApplicationLauncher.logger.warn("loadBayStatesFromDatabase : No valid bayId selected");
				return;
			}

			// Fetch data from the database for the selected bayId
			List<StateFlow> stateFlows = MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(bayKey,
					executionMode);

			// Log the number of rows retrieved
			ApplicationLauncher.logger
					.debug("loadBayStatesFromDatabase : Retrieved " + stateFlows.size() + " rows for bayId: " + bayKey);

			tableStatePlanner.getItems().addAll(stateFlows);
			OptionalInt lastSerialNo = tableStatePlanner.getItems().stream().mapToInt(e -> e.getSerialNo()).max();
			if (lastSerialNo.isPresent()) {
				getSerialNoAtomic().set(lastSerialNo.getAsInt() + 1);
			}

			if (bayKey.equals(ConstantConveyor.FT_BAY_KEY)) {
				setTableStatePlannerFtBay_UI(tableStatePlanner);
			} else {

			}

		} catch (Exception e) {
			// Handle exceptions and log errors
			ApplicationLauncher.logger.error("loadBayStatesFromDatabase : Failed to load data: " + e.getMessage(), e);
		}

		ApplicationLauncher.logger.debug("loadBayStatesFromDatabase : Exit");
	}

	@FXML
	public void cmbBxSelectBayTypeOnChange() {
		tableStatePlanner.getItems().clear();
	}

	private void initializeStateCodeMap() {
		stateCodeMap.put(S01_check_for_pallet_at_FT_Bay.class.getSimpleName(), "STATE_CODE_001");
		stateCodeMap.put(S02_let_the_pallet_to_FT_Bay.class.getSimpleName(), "STATE_CODE_002");
		stateCodeMap.put(S03_close_the_fingerTip_Latch.class.getSimpleName(), "STATE_CODE_003");
		stateCodeMap.put(S04_ensure_the_fingerTip_Latch_Closed.class.getSimpleName(), "STATE_CODE_004");
		stateCodeMap.put(S05_qR_Code_Scanning_of_Pallet.class.getSimpleName(), "STATE_CODE_005");
		stateCodeMap.put(S06_scanning_of_Meters_Presence.class.getSimpleName(), "STATE_CODE_006");
		stateCodeMap.put(S07_qR_Code_Scanning_of_Meters.class.getSimpleName(), "STATE_CODE_007");
		stateCodeMap.put(S070_opticalProbe_Scanning_of_Meters.class.getSimpleName(), "STATE_CODE_107");
		stateCodeMap.put(S071_start_FT_source.class.getSimpleName(), "STATE_CODE_008");
		stateCodeMap.put(S072_ensure_FT_source_started.class.getSimpleName(), "STATE_CODE_009");
		stateCodeMap.put(S08_functional_Test.class.getSimpleName(), "STATE_CODE_010");
		stateCodeMap.put(S081_stop_FT_source.class.getSimpleName(), "STATE_CODE_011");
		stateCodeMap.put(S082_ensure_FT_source_stopped.class.getSimpleName(), "STATE_CODE_012");
		stateCodeMap.put(S09_open_the_fingerTip_Latch.class.getSimpleName(), "STATE_CODE_013");
		stateCodeMap.put(S10_ensure_the_fingerTip_Latch_Opened.class.getSimpleName(), "STATE_CODE_014");
		stateCodeMap.put(S11_check_for_pallet_at_HVT_Bay.class.getSimpleName(), "STATE_CODE_015");
		stateCodeMap.put(S12_turn_on_divertor_relay_FT_Bay.class.getSimpleName(), "STATE_CODE_016");
		stateCodeMap.put(S13_ensure_divertor_relay_turned_on_FT_Bay.class.getSimpleName(), "STATE_CODE_017");
		stateCodeMap.put(S14_let_the_pallet_to_HVT_Bay.class.getSimpleName(), "STATE_CODE_018");
		stateCodeMap.put(S15_ensure_pallet_reached_HVT_Bay.class.getSimpleName(), "STATE_CODE_019");
		stateCodeMap.put(S16_check_for_pallet_at_Rejection_Bay.class.getSimpleName(), "STATE_CODE_020");
		stateCodeMap.put(S17_turn_off_divertor_relay_FT_Bay.class.getSimpleName(), "STATE_CODE_021");
		stateCodeMap.put(S18_ensure_divertor_relay_turned_off_FT_Bay.class.getSimpleName(), "STATE_CODE_022");
		stateCodeMap.put(S19_wait_for_reset_button_ip_Rejection_Bay.class.getSimpleName(), "STATE_CODE_023");
		stateCodeMap.put(S20_let_the_pallet_to_Rejection_Bay.class.getSimpleName(), "STATE_CODE_024");
		stateCodeMap.put(S21_ensure_pallet_reached_Reject_Bay.class.getSimpleName(), "STATE_CODE_025");
		stateCodeMap.put(S22_error_Handling.class.getSimpleName(), "STATE_CODE_026");

	}

	@FXML
	private void setupTable() {
		// Set the cell factory for the "State" column to use a ComboBox
		columnState.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		// Listen for changes in the "State" column and update the "StateErrorCode"
		// column
		columnState.setOnEditCommit(event -> {
			// Get the selected row
			StateFlow row = event.getRowValue();

			// Get the new state name
			String newState = event.getNewValue();

			// Update the state in the row
			row.setState(newState);

			// Map the state name to its corresponding state code and update it in the row
			String stateCode = stateCodeMap.getOrDefault(newState, "UNKNOWN_CODE");
			row.setErrorCode(stateCode);

			// Refresh the table view to reflect the updated data
			tableStatePlanner.refresh();
		});

		// Set up other columns (e.g., "Success") as needed
		columnSuccess.setCellFactory(ComboBoxTableCell.forTableColumn(stateNames));

		columnSuccess.setOnEditCommit(event -> {
			// Get the selected row
			StateFlow row = event.getRowValue();

			// Get the new state name
			String newState = event.getNewValue();

			// Update the state in the row
			row.setIfSuccess(newState);

			// Map the state name to its corresponding state code and update it in the row
			String stateCode = stateCodeMap.getOrDefault(newState, "UNKNOWN_CODE");
			row.setIfSuccessErrorCode(stateCode);

			// Refresh the table view to reflect the updated data
			tableStatePlanner.refresh();
		});
	}
	// ==============================================================================

	@FXML
	public void ButtonSaveStateFlowOnClick() {
		// Call the validation function
		if (!validateStateFlowRows()) {
			// If validation fails, stop further execution
			return;
		}

		// setTableStatePlannerFtBay_UI(tableStatePlanner);

		// Save rows to the database
		try {
			saveStateFlowRowsToDatabase();
			WindowManager.InformUser("State Plan", "Successfully Saved", AlertType.INFORMATION);
		} catch (Exception e) {
			e.printStackTrace();
			WindowManager.InformUser("State Plan", "Failed to Save: " + e.getMessage(), AlertType.ERROR);
		}

	}

	/**
	 * Save the rows from the tableStatePlanner to the database.
	 */
	private void saveStateFlowRowsToDatabase() {
		/*
		 * ObservableList<StateFlow> rows = tableStatePlanner.getItems();
		 * for (StateFlow row : rows) {
		 * if (row != null) {
		 * saveRowToDatabase(row);
		 * }
		 * }
		 */

		ApplicationLauncher.logger.debug(
				"saveStateFlowRowsToDatabase : tableStatePlanner : Size : " + tableStatePlanner.getItems().size());

		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();

		String bayKey = ConstantConveyor.getBayLookup().get(bayName);

		String executionMode = getExecutionMode();

		List<StateFlow> dbRows = MySqlServiceManager.getStateFlowService().findByBayKeyAndExecutionMode(bayKey,
				executionMode);

		// Get the current rows from the UI
		List<StateFlow> uiRows = new ArrayList<>(tableStatePlanner.getItems());

		// Identify rows to delete (present in the DB but not in the UI)
		List<StateFlow> rowsToDelete = dbRows.stream()
				.filter(dbRow -> uiRows.stream().noneMatch(uiRow -> uiRow.getId() == dbRow.getId()))
				.collect(Collectors.toList());

		// Delete rows from the database
		for (StateFlow rowToDelete : rowsToDelete) {
			MySqlServiceManager.getStateFlowService().deleteById(rowToDelete.getId());
		}

		// Save or update rows from the UI to the database
		for (StateFlow row : uiRows) {
			MySqlServiceManager.getStateFlowService().saveToDb(row);
		}
	}

	// ==============================================================================

	// ===============================================================================

	@FXML
	public void buttonDeleteRowOnClick() {
		// Get the selected row
		StateFlow selectedRow = tableStatePlanner.getSelectionModel().getSelectedItem();

		// Check if a row is selected
		if (selectedRow != null) {
			// Remove the selected row from the list
			stateFlowRows.remove(selectedRow);

			// Refresh the table view to reflect the deletion
			tableStatePlanner.refresh();
		}
	}

	@FXML
	public void buttonAddRowOnClick() {
		// Get the selected row index
		int selectedIndex = tableStatePlanner.getSelectionModel().getSelectedIndex();

		// Add a new row to the stateFlowRows list after the selected row
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();
		String bayKey = ConstantConveyor.getBayLookup().get(bayName);
		String executionMode = getExecutionMode();

		StateFlow newRow = new StateFlow(
				getSerialNoAtomic().getAndIncrement(),
				"S" + (stateFlowRows.size() + 1),
				bayKey,
				executionMode,
				"Select State",
				"No State Selected",
				"Select State",
				"No State Selected",
				" ",
				" ");

		if (selectedIndex >= 0 && selectedIndex < stateFlowRows.size()) {
			// Insert the new row after the selected row
			stateFlowRows.add(selectedIndex + 1, newRow);
		} else {
			// If no row is selected or the selection is invalid, add the row to the end
			stateFlowRows.add(newRow);
		}

		// Refresh the table view to display the updated list
		tableStatePlanner.refresh();
	}

	private String getExecutionMode() {
		// Get the selected item from the ComboBox
		return cmbModeSelection.getSelectionModel().getSelectedItem();
	}

	// ==============================================================================
	@FXML
	public void runSingleStateOnClick() {
		String bayName = ref_cmbBxSelectBayType.getSelectionModel().getSelectedItem();

		if (bayName.equals(ConstantConveyor.FT_BAY_DISPLAY_NAME)) {// "Functional Bay")) {
			funtionalBaySingleStateTaskTimer = new Timer();
			funtionalBaySingleStateTaskTimer.schedule(new FunctionalTestBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.HV_BAY_DISPLAY_NAME)) {// "High Voltage Bay")) {
			hvtBaySingleStateTaskTimer = new Timer();
			hvtBaySingleStateTaskTimer.schedule(new HighVoltageTestBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.IR_BAY_DISPLAY_NAME)) {// "IR Bay")) {
			irBaySingleStateTaskTimer = new Timer();
			irBaySingleStateTaskTimer.schedule(new InsulationResistanceTestBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.CALIBRATION_BAY_DISPLAY_NAME)) {// "Calibration Bay")) {
			calibBaySingleStateTaskTimer = new Timer();
			calibBaySingleStateTaskTimer.schedule(new CalibrationBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.COMMUNICATION_BAY_DISPLAY_NAME)) {// "Communication Bay")) {
			commBaySingleStateTaskTimer = new Timer();
			commBaySingleStateTaskTimer.schedule(new CommunicationTestBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.LOADING_BAY_DISPLAY_NAME)) {// "Loading Bay")) {
			loadingBaySingleStateTaskTimer = new Timer();
			loadingBaySingleStateTaskTimer.schedule(new LoadingBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.STA_NLD1_BAY_DISPLAY_NAME)) {// "SCT NLT Bay 1")) {
			sctNltBay1SingleStateTaskTimer = new Timer();
			sctNltBay1SingleStateTaskTimer.schedule(new STA_NoLoadTestBay1SingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.STA_NLD2_BAY_DISPLAY_NAME)) {// "SCT NLT Bay 2")) {
			sctNltBay2SingleStateTaskTimer = new Timer();
			sctNltBay2SingleStateTaskTimer.schedule(new STA_NoLoadTestBay2SingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.UNLOADING_BAY_DISPLAY_NAME)) {// "Unloading Bay")) {
			unloadingBaySingleStateTaskTimer = new Timer();
			unloadingBaySingleStateTaskTimer.schedule(new UnloadingBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.VERIFICATION_BAY_DISPLAY_NAME)) {// "Verification Bay")) {
			verificBaySingleStateTaskTimer = new Timer();
			verificBaySingleStateTaskTimer.schedule(new VerificationTestBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.WAITING_BAY_DISPLAY_NAME)) {// "Waiting Bay")) {
			waitingBaySingleStateTaskTimer = new Timer();
			waitingBaySingleStateTaskTimer.schedule(new WaitngBaySingleStateTestRun(), 100);

		} else if (bayName.equals(ConstantConveyor.REJECTION_BAY_DISPLAY_NAME)) {// "Rejection Bay")) {
			rejectionBaySingleStateTaskTimer = new Timer();
			rejectionBaySingleStateTaskTimer.schedule(new RejectionBaySingleStateTestRun(), 100);

		}
	}

	@FXML
	void cmbBxSelectSingleStateOnChange() {

	}
	// ==============================================================================

	// ==============================================================================

	private boolean validateStateFlowRows() {
		// Get all rows from the TableView
		ObservableList<StateFlow> rows = tableStatePlanner.getItems();

		// Iterate through the rows to validate
		for (int i = 0; i < rows.size(); i++) {
			StateFlow row = rows.get(i);

			// Check if the state is "Select State"
			if ("Select State".equals(row.getState())) {
				// Prompt the user with the row number
				WindowManager.InformUser("State not selected in row " + (i + 1), "Kindly select a state",
						AlertType.INFORMATION);

				return false; // Validation failed
			}
		}
		return true; // Validation successful
	}

	/*
	 * private boolean validateStateFlowRows(StatePlannerTable statePlannerTable) {
	 * // Get all rows from the TableView
	 * ObservableList<StateFlowRow> rows =
	 * statePlannerTable.tableStatePlanner.getItems();
	 * 
	 * // Iterate through the rows to validate
	 * for (int i = 0; i < rows.size(); i++) {
	 * StateFlowRow row = rows.get(i);
	 * 
	 * // Check if the state is "Select State"
	 * if ("Select State".equals(row.getState())) {
	 * // Prompt the user with the row number
	 * WindowManager.InformUser("State not selected in row " + (i + 1),
	 * "Kindly select a state", AlertType.INFORMATION);
	 * 
	 * return false; // Validation failed
	 * }
	 * }
	 * return true; // Validation successful
	 * }
	 */

	/*
	 * public static StatePlannerTable getTableStatePlannerFtBay_UI() {
	 * return tableStatePlanner_FtBay_UI;
	 * }
	 * 
	 * public void setTableStatePlannerFtBay_UI(StatePlannerTable
	 * tableStatePlannerFtBay_UI) {
	 * this.tableStatePlanner_FtBay_UI = tableStatePlannerFtBay_UI;
	 * }
	 */

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:" + e.getMessage());
		}

	}

	public static TableView<StateFlow> getTableStatePlannerFtBay_UI() {
		return tableStatePlanner_FtBay_UI;
	}

	public void setTableStatePlannerFtBay_UI(TableView<StateFlow> tableStatePlannerFtBay_UI) {
		this.tableStatePlanner_FtBay_UI = tableStatePlannerFtBay_UI;
	}

	public AtomicInteger getSerialNoAtomic() {
		return serialNoAtomic;
	}

	public void setSerialNoAtomic(AtomicInteger serialNoAtomic) {
		this.serialNoAtomic = serialNoAtomic;
	}

}
