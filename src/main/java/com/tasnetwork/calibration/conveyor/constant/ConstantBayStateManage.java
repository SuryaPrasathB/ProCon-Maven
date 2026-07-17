package com.tasnetwork.calibration.conveyor.constant;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstantBayStateManage {
	
	
/*	public static String HV_BAY_HP_SEQ_00 = "HV_HP_00_Idle";
	public static String HV_BAY_HP_SEQ_01 = "HV_HP_01_PalletCheckAtBay";*/

	public static String BAY_HP_SEQ_00 = "HP_00_Idle";
	public static String BAY_HP_SEQ_01 = "HP_01_PalletCheckAtBay";
	public static String BAY_HP_SEQ_02 = "HP_02_PalletAllowForEntry";
	public static String BAY_HP_SEQ_03 = "HP_03_FingerTipEngageInit";
	public static String BAY_HP_SEQ_04 = "HP_04_FingerTipEngagedAck";
	public static String BAY_HP_SEQ_05 = "HP_05_PalletQrScan";
	public static String BAY_HP_SEQ_06 = "HP_06_ScanMeterExist";
	public static String BAY_HP_SEQ_07 = "HP_07_MeterQrScan";

	public static String BAY_HP_SEQ_08 = "HP_08_StartFtPowerSource";
	public static String BAY_HP_SEQ_09 = "HP_09_SetMeterSerialNo";
	public static String BAY_HP_SEQ_09A = "HP_09_SetHardwareIdNo";
	public static String BAY_HP_SEQ_10 = "HP_10_ExecuteTest";	
	public static String FT_BAY_HP_SEQ_10 = "FT_HP_10_StopFtPowerSourceInit";
	public static String FT_BAY_HP_SEQ_11 = "FT_HP_11_StopFtPowerSourceAck";
	public static String FT_BAY_HP_SEQ_12 = "FT_HP_12_FingerTipDisEngageInit";
	public static String FT_BAY_HP_SEQ_13 = "FT_HP_13_FingerTipDisEngageAck";
	public static String FT_BAY_HP_SEQ_14 = "FT_HP_14_PalletAllowIfHvBayIdle";	
	public static String FT_BAY_HP_SEQ_15 = "FT_HP_15_DivertorOnInit";
	public static String FT_BAY_HP_SEQ_16 = "FT_HP_16_DivertorOnAck";
	public static String FT_BAY_HP_SEQ_17 = "FT_HP_17_FingerTipDisEngageInit";
	public static String FT_BAY_HP_SEQ_18 = "FT_HP_18_PalletFtExitAllow";
	public static String FT_BAY_HP_SEQ_19 = "FT_HP_19_PalletReachedHvBayAck";
	public static String FT_BAY_HP_SEQ_20 = "FT_HP_20_PalletCheckAtRejectionBay";
	public static String FT_BAY_HP_SEQ_21 = "FT_HP_21_DivertorOffInit";
	public static String FT_BAY_HP_SEQ_22 = "FT_HP_22_DivertorOffAck";
	public static String FT_BAY_HP_SEQ_23 = "FT_HP_23_AwaitResetBtnPressAtRejectBay";
	public static String FT_BAY_HP_SEQ_24 = "FT_HP_24_PalletFtExitAllow";
	public static String FT_BAY_HP_SEQ_25 = "FT_HP_25_PalletReachedRejectBayAck";

	public static String FT_BAY_EP_SEQ_00 = "FT_EP_Idle00";
	public static String FT_BAY_EP_SEQ_01 = "FT_EP_PalletCheckAtBay";
	public static String FT_BAY_EP_SEQ_02 = "FT_EP_PalletAllowForEntry";
	public static String FT_BAY_EP_SEQ_03 = "FT_EP_FingerTipEngageInit";
	public static String FT_BAY_EP_SEQ_04 = "FT_EP_FingerTipEngagedAck";

	public static ArrayList<String> FT_BAY_HP_SEQ_LIST  = new ArrayList<String> (Arrays.asList(	
			BAY_HP_SEQ_00,
			BAY_HP_SEQ_01,
			BAY_HP_SEQ_02,
			BAY_HP_SEQ_03,
			BAY_HP_SEQ_04,
			BAY_HP_SEQ_05,
			BAY_HP_SEQ_06,
			BAY_HP_SEQ_07,
			BAY_HP_SEQ_08,
			BAY_HP_SEQ_10,
			FT_BAY_HP_SEQ_10,
			FT_BAY_HP_SEQ_11,
			FT_BAY_HP_SEQ_12,
			FT_BAY_HP_SEQ_13,
			FT_BAY_HP_SEQ_14,	
			FT_BAY_HP_SEQ_15,
			FT_BAY_HP_SEQ_16,
			FT_BAY_HP_SEQ_17,
			FT_BAY_HP_SEQ_18,
			FT_BAY_HP_SEQ_19,
			FT_BAY_HP_SEQ_20,
			FT_BAY_HP_SEQ_21,
			FT_BAY_HP_SEQ_22,
			FT_BAY_HP_SEQ_23,
			FT_BAY_HP_SEQ_24,
			FT_BAY_HP_SEQ_25));
}
