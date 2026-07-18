package com.tasnetwork.calibration.energymeter;

import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

public class ReportConfigManager {
	public static void LoadFVToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.FREQ_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.FREQ_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.FREQ_TEMPL_PFS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_FREQUENCY)) {
			ConstantReport.FREQ_TEMPL_FREQUENCIES.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.FREQ_TEMPL_DEFAULT_FREQ = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadFVToReportProperty : Mismatch");
		}
	}

	public static void LoadVVToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.VV_TEMPL_VOLTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.VV_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.VV_TEMPL_PFS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.VV_TEMPL_DEFAULT_VOLT = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadVVToReportProperty : Mismatch");
		}
	}

	public static void LoadREPToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.REP_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.REP_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.REP_TEMPL_PF = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.REP_TEMPL_NO_OF_TESTS = Integer.parseInt(header_value);
		} else {
			ApplicationLauncher.logger.info("LoadREPToReportProperty : Mismatch");
		}
	}

	public static void LoadSELFHToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.SELF_HEAT_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.SELF_HEAT_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.SELF_HEAT_TEMPL_PFS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS = Integer.parseInt(header_value);
		} else {
			ApplicationLauncher.logger.info("LoadSELFHToReportProperty : Mismatch");
		}
	}

	public static void LoadACCToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.ACC_TEMPL_VOLT = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.ACC_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.ACC_TEMPL_PFS.add(header_value);
		} else {
			ApplicationLauncher.logger.info("LoadACCToReportProperty : Mismatch");
		}
	}

	public static void LoadRPSToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.RPS_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.RPS_TEMPL_CURRENT = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.RPS_TEMPL_PF = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadRPSToReportProperty : Mismatch");
		}
	}

	public static void LoadHARMToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.HARM_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.HARM_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.HARM_TEMPL_PF = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_HARMONICS)) {
			ConstantReport.HARM_TEMPL_HARM_TIMES.add(header_value);
		} else {
			ApplicationLauncher.logger.info("LoadHARMToReportProperty : Mismatch");
		}
	}

	public static void LoadVUToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.VU_TEMPL_VOLTAGES.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.VU_TEMPL_CURRENT = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.VU_TEMPL_PF = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.VU_TEMPL_DEF_VOLT = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadVUToReportProperty : Mismatch");
		}
	}

	public static void LoadCONSTToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.CONST_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.CONST_TEMPL_CURRENT = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.CONST_TEMPL_PF = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_REFERENCE_VALUE)) {
			ConstantReport.CONST_TEMPL_POWER = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadCONSTToReportProperty : Mismatch");
		}
	}

	public static void LoadCREEPToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.CREEP_TEMPL_VOLTAGE = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadCREEPToReportProperty : Mismatch");
		}
	}

	public static void LoadSTAToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_VOLTAGE)) {
			ConstantReport.STA_TEMPL_VOLTAGE = header_value;
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.STA_TEMPL_CURRENT = header_value;
		} else {
			ApplicationLauncher.logger.info("LoadSTAToReportProperty : Mismatch");
		}
	}

	public static void LoadUnbalanceLoadToReportProperty(String header_type, String header_value) {
		if (header_type.equals(ConstantReport.HEADER_TYPE_CURRENT)) {
			ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS.add(header_value);
		} else if (header_type.equals(ConstantReport.HEADER_TYPE_PF)) {
			ConstantReport.UNBALANCED_LOAD_TEMPL_PFS.add(header_value);
		} else {
			ApplicationLauncher.logger.info("LoadUnbalanceLoadToReportProperty : Mismatch");
		}
	}

	public static void LoadReportExcelConfigProperty() {
		ResetAllReportExcelConfigValues();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		for (int i = 0; i < test_types_list.size(); i++) {
			FetchReportExcelDataByTestType(test_types_list.get(i));
		}

	}

	public static void ResetAllReportExcelConfigValues() {

		ConstantReport.FREQ_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.FREQ_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.VV_TEMPL_DEF_VOLT_COLS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.REP_TEMPL_TEST_COL = 0;
		ConstantReport.SELF_HEAT_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.SELF_HEAT_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.SELF_HEAT_TEMPL_TEST_COLS = new ArrayList<Integer>();
		// ConstantReport.ACC_TEMPL_ROW = 0;
		// ConstantReport.ACC_TEMPL_METER_COL = 0;
		ConstantReport.ACC_TEMPL_ROW = new ArrayList<Integer>();
		ConstantReport.ACC_TEMPL_METER_COL = new ArrayList<Integer>();
		ConstantReport.ACC_TEMPL_DEF_I_COLS = new ArrayList<Integer>();
		ConstantReport.RPS_TEMPL_ROW = 0;
		ConstantReport.RPS_TEMPL_METER_COL = 0;
		ConstantReport.RPS_TEMPL_NORMAL_REV_COL = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.HARM_TEMPL_PHASE_COLS = new ArrayList<Integer>();
		ConstantReport.VU_TEMPL_ROW = 0;
		ConstantReport.VU_TEMPL_METER_COL = 0;
		ConstantReport.VU_TEMPL_DEF_VOLT_COL = 0;
		ConstantReport.CONST_TEMPL_ROW = 0;
		ConstantReport.CONST_TEMPL_METER_COL = 0;
		ConstantReport.CONST_TEMPL_CONST_COLS = new ArrayList<Integer>();
		ConstantReport.CREEP_TEMPL_ROW = 0;
		ConstantReport.CREEP_TEMPL_METER_COL = 0;
		ConstantReport.CREEP_TEMPL_CREEP_COLS = new ArrayList<Integer>();
		ConstantReport.STA_TEMPL_ROW = 0;
		ConstantReport.STA_TEMPL_METER_COL = 0;
		ConstantReport.STA_TEMPL_STA_COLS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS = new ArrayList<Integer>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_COLS = new ArrayList<Integer>();
	}

	public static void FetchReportExcelDataByTestType(String test_type) {
		JSONObject report_excel_config = MySQL_Controller
				.sp_getreport_excel_config(ConstantAppConfig.REPORT_PROFILE_LIST.get(0), test_type);
		try {
			JSONArray report_excel_arr = report_excel_config.getJSONArray("Report_Excel_Cells");
			ApplicationLauncher.logger.info("LoadSavedData : report_excel_arr: " + report_excel_arr);
			if (report_excel_arr.length() != 0) {
				JSONObject jobj = new JSONObject();
				for (int i = 0; i < report_excel_arr.length(); i++) {
					jobj = report_excel_arr.getJSONObject(i);
					String cell_type = jobj.getString("cell_type");
					String cell_value = jobj.getString("cell_value");
					LoadReportExcelConfigValues(test_type, cell_type, cell_value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchReportExcelDataByTestType : JSONException: " + e.getMessage());

		}
	}

	public static void LoadReportExcelConfigValues(String test_type, String cell_type,
			String cell_value) {
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				LoadFVExcelToReportProperty(cell_type, cell_value);
				break;
			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				LoadVVExcelToReportProperty(cell_type, cell_value);
				break;
			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				LoadREPExcelToReportProperty(cell_type, cell_value);
				break;
			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				LoadSELFHExcelToReportProperty(cell_type, cell_value);
				break;
			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				LoadACCExcelToReportProperty(cell_type, cell_value);
				break;
			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				LoadRPSExcelToReportProperty(cell_type, cell_value);
				break;
			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				LoadHARMExcelToReportProperty(cell_type, cell_value);
				break;
			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				LoadVUExcelToReportProperty(cell_type, cell_value);
				break;
			// case "ConstantTest":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
				LoadCONSTExcelToReportProperty(cell_type, cell_value);
				break;
			// case "NoLoad":
			case ConstantApp.TEST_PROFILE_NOLOAD:
				LoadCREEPExcelToReportProperty(cell_type, cell_value);
				break;
			// case "STA":
			case ConstantApp.TEST_PROFILE_STA:
				LoadSTAExcelToReportProperty(cell_type, cell_value);
				break;
			// case "UnbalancedLoad":
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				if (ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD) {
					LoadUnbalanceLoadExcelToReportProperty(cell_type, cell_value);
				}
				break;
			default:
				break;

		}
	}

	public static void LoadFVExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.FREQ_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.FREQ_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.FREQ_TEMPL_DEF_FREQ_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadFVExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadVVExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.VV_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.VV_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.VV_TEMPL_DEF_VOLT_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadVVExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadREPExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.REP_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.REP_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
			// ApplicationLauncher.logger.info("LoadREPExcelToReportProperty :
			// getRowValueFromCellValue(cell_value):"+getRowValueFromCellValue(cell_value));

		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.REP_TEMPL_TEST_COL = getColValueFromCellValue(cell_value);
		} else {
			ApplicationLauncher.logger.info("LoadREPExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadSELFHExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.SELF_HEAT_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.SELF_HEAT_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.SELF_HEAT_TEMPL_TEST_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadSELFHExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadACCExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			// ConstantReport.ACC_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			// ConstantReport.ACC_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
			ConstantReport.ACC_TEMPL_ROW.add(getRowValueFromCellValue(cell_value));
			ConstantReport.ACC_TEMPL_METER_COL.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.ACC_TEMPL_DEF_I_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadACCExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadRPSExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.RPS_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.RPS_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.RPS_TEMPL_NORMAL_REV_COL.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadRPSExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadHARMExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.HARM_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.HARM_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.HARM_TEMPL_PHASE_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadHARMExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadVUExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.VU_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.VU_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.VU_TEMPL_DEF_VOLT_COL = getColValueFromCellValue(cell_value);
		} else {
			ApplicationLauncher.logger.info("LoadVUExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadCONSTExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.CONST_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.CONST_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.CONST_TEMPL_CONST_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadVUExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadCREEPExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.CREEP_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.CREEP_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.CREEP_TEMPL_CREEP_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadCREEPExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadSTAExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.STA_TEMPL_ROW = getRowValueFromCellValue(cell_value);
			ConstantReport.STA_TEMPL_METER_COL = getColValueFromCellValue(cell_value);
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.STA_TEMPL_STA_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadSTAExcelToReportProperty : Mismatch");
		}
	}

	public static void LoadUnbalanceLoadExcelToReportProperty(String cell_type, String cell_value) {
		if (cell_type.equals(ConstantReport.EXCEL_ALPHA)) {
			ConstantReport.UNBALANCED_LOAD_TEMPL_ROWS.add(getRowValueFromCellValue(cell_value));
			ConstantReport.UNBALANCED_LOAD_TEMPL_METER_COLS.add(getColValueFromCellValue(cell_value));
		} else if (cell_type.equals(ConstantReport.EXCEL_BETA)) {
			ConstantReport.UNBALANCED_LOAD_TEMPL_COLS.add(getColValueFromCellValue(cell_value));
		} else {
			ApplicationLauncher.logger.info("LoadUnbalanceLoadExcelToReportProperty : Mismatch");
		}
	}

	public static int getRowValueFromCellValue(String cellvalue) {
		String str_row = cellvalue.replaceAll("[^0-9]", "");
		int row = Integer.parseInt(str_row) - 1;
		return row;
	}

	public static int getColValueFromCellValue(String cellvalue) {
		String col = cellvalue.replaceAll("[0-9]", "");

		int col_value = 0;
		char ch = ' ';
		int ascii_value = 0;
		for (int i = 0; i < col.length(); i++) {
			ch = col.charAt(i);
			ascii_value = (int) ch;
			col_value = (col_value * 26) + ascii_value - 64;
		}

		col_value = col_value - 1;
		return col_value;
	}

	public static void LoadReportHeaderConfigProperty() {
		ResetAllReportConfigValues();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		ApplicationLauncher.logger.info("LoadSavedData : test_types_list: " + test_types_list);
		for (int i = 0; i < test_types_list.size(); i++) {
			FetchDataByTestType(test_types_list.get(i));
		}

	}

	public static void ResetAllReportConfigValues() {
		ConstantReport.FREQ_TEMPL_VOLTAGE = "";
		ConstantReport.FREQ_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_FREQUENCIES = new ArrayList<String>();
		ConstantReport.FREQ_TEMPL_DEFAULT_FREQ = "";
		ConstantReport.VV_TEMPL_VOLTS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.VV_TEMPL_DEFAULT_VOLT = "";
		ConstantReport.REP_TEMPL_VOLTAGE = "";
		ConstantReport.REP_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.REP_TEMPL_PF = "";
		ConstantReport.REP_TEMPL_NO_OF_TESTS = 0;
		ConstantReport.SELF_HEAT_TEMPL_VOLTAGE = "";
		ConstantReport.SELF_HEAT_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.SELF_HEAT_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.SELF_HEAT_TEMPL_NO_OF_TESTS = 0;
		ConstantReport.ACC_TEMPL_VOLT = "";
		ConstantReport.ACC_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.ACC_TEMPL_PFS = new ArrayList<String>();
		ConstantReport.REP_TEMPL_VOLTAGE = "";
		ConstantReport.RPS_TEMPL_CURRENT = "";
		ConstantReport.RPS_TEMPL_PF = "";
		ConstantReport.HARM_TEMPL_VOLTAGE = "";
		ConstantReport.HARM_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.HARM_TEMPL_PF = "";
		ConstantReport.HARM_TEMPL_HARM_TIMES = new ArrayList<String>();
		ConstantReport.VU_TEMPL_VOLTAGES = new ArrayList<String>();
		ConstantReport.VU_TEMPL_CURRENT = "";
		ConstantReport.VU_TEMPL_PF = "";
		ConstantReport.VU_TEMPL_DEF_VOLT = "";
		ConstantReport.CONST_TEMPL_VOLTAGE = "";
		ConstantReport.CONST_TEMPL_CURRENT = "";
		ConstantReport.CONST_TEMPL_PF = "";
		ConstantReport.CONST_TEMPL_POWER = "";
		ConstantReport.CREEP_TEMPL_VOLTAGE = "";
		ConstantReport.STA_TEMPL_VOLTAGE = "";
		ConstantReport.STA_TEMPL_CURRENT = "";
		ConstantReport.UNBALANCED_LOAD_TEMPL_CURRENTS = new ArrayList<String>();
		ConstantReport.UNBALANCED_LOAD_TEMPL_PFS = new ArrayList<String>();
	}

	public static void FetchDataByTestType(String test_type) {
		JSONObject report_header_config = MySQL_Controller
				.sp_getreport_header_config(ConstantAppConfig.REPORT_PROFILE_LIST.get(0), test_type);
		try {
			JSONArray report_header_arr = report_header_config.getJSONArray("Report_Headers");
			ApplicationLauncher.logger.debug("LoadSavedData : report_header_arr: " + report_header_arr);
			if (report_header_arr.length() != 0) {
				JSONObject jobj = new JSONObject();
				for (int i = 0; i < report_header_arr.length(); i++) {
					jobj = report_header_arr.getJSONObject(i);
					String header_type = jobj.getString("header_type");
					String header_value = jobj.getString("header_value");
					LoadReportHeaderConfigValues(test_type, header_type, header_value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchDataByTestType : JSONException: " + e.getMessage());
		}
	}

	public static void LoadReportHeaderConfigValues(String test_type, String header_type, String header_value) {
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				LoadFVToReportProperty(header_type, header_value);
				break;

			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				LoadVVToReportProperty(header_type, header_value);
				break;

			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				LoadREPToReportProperty(header_type, header_value);
				break;

			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				LoadSELFHToReportProperty(header_type, header_value);
				break;

			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				LoadACCToReportProperty(header_type, header_value);
				break;

			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				LoadRPSToReportProperty(header_type, header_value);
				break;

			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				LoadHARMToReportProperty(header_type, header_value);
				break;

			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				LoadVUToReportProperty(header_type, header_value);
				break;

			// case "ConstantTest":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
				LoadCONSTToReportProperty(header_type, header_value);
				break;

			// case "NoLoad":
			case ConstantApp.TEST_PROFILE_NOLOAD:
				LoadCREEPToReportProperty(header_type, header_value);
				break;

			// case "STA":
			case ConstantApp.TEST_PROFILE_STA:
				LoadSTAToReportProperty(header_type, header_value);
				break;

			// case "UnbalancedLoad":
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				if (ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD) {
					LoadUnbalanceLoadToReportProperty(header_type, header_value);
				}
				break;
			default:
				break;
		}
	}

	public static void LoadReportFileLocationProperty() {
		ResetAllReportFileLocation();
		ArrayList<String> test_types_list = ConstantReport.REPORT_TEST_TYPES;
		for (int i = 0; i < test_types_list.size(); i++) {
			FetchFileLocationByTestType(test_types_list.get(i));
		}
	}

	public static void ResetAllReportFileLocation() {
		ConstantReport.SAVE_FILE_LOCATION = "";
		ConstantReport.INPUT_TEMPLATE_LOCATION = "";
		ConstantReport.ACC_TEMPL_FILE_LOCATION = "";
		ConstantReport.CONST_TEMPL_FILE_LOCATION = "";
		ConstantReport.CREEP_TEMPL_FILE_LOCATION = "";
		ConstantReport.FREQ_TEMPL_FILE_LOCATION = "";
		ConstantReport.HARM_TEMPL_FILE_LOCATION = "";
		ConstantReport.REP_TEMP_FILE_LOCATION = "";
		ConstantReport.RPS_TEMPL_FILE_LOCATION = "";
		ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION = "";
		ConstantReport.STA_TEMPL_FILE_LOCATION = "";
		ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION = "";
		ConstantReport.VU_TEMPL_FILE_LOCATION = "";
		ConstantReport.VV_TEMPL_FILE_LOCATION = "";
	}

	public static void FetchFileLocationByTestType(String test_type) {
		JSONObject data = MySQL_Controller.sp_getreport_file_location(ConstantAppConfig.REPORT_PROFILE_LIST.get(0),
				test_type);
		try {
			if (!data.isNull("template_file_location")) {
				String template_file = data.getString("template_file_location");
				String save_file_loc = data.getString("save_file_location");
				LoadReportFileLocationValues(test_type, template_file, save_file_loc);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchFileLOcationByTestType : JSONException: " + e.getMessage());
		}
	}

	public static void LoadReportFileLocationValues(String test_type, String template_file,
			String save_file_loc) {

		ConstantReport.SAVE_FILE_LOCATION = save_file_loc;
		switch (test_type) {
			// case "InfluenceFreq":
			case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
				ConstantReport.FREQ_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "InfluenceVolt":
			case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
				ConstantReport.VV_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "Repeatability":
			case ConstantApp.TEST_PROFILE_REPEATABILITY:
				ConstantReport.REP_TEMP_FILE_LOCATION = template_file;
				break;
			// case "SelfHeating":
			case ConstantApp.TEST_PROFILE_SELF_HEATING:
				ConstantReport.SELF_HEAT_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "Accuracy":
			case ConstantApp.TEST_PROFILE_ACCURACY:
				ConstantReport.ACC_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "PhaseReversal":
			case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
				ConstantReport.RPS_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "InfluenceHarmonic":
			case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
				ConstantReport.HARM_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "VoltageUnbalance":
			case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
				ConstantReport.VU_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "ConstantTest":
			case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
				ConstantReport.CONST_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "NoLoad":
			case ConstantApp.TEST_PROFILE_NOLOAD:
				ConstantReport.CREEP_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "STA":
			case ConstantApp.TEST_PROFILE_STA:
				ConstantReport.STA_TEMPL_FILE_LOCATION = template_file;
				break;
			// case "UnbalancedLoad":
			case ConstantApp.TEST_PROFILE_UNBALANCED_LOAD:
				if (ProcalFeatureEnable.REPORT_3PHASE_UNBALANCED_LOAD) {
					ConstantReport.UNBALANCED_LOAD_TEMPL_FILE_LOCATION = template_file;
				}
				break;

			case ConstantApp.METER_PROFILE_REPORT:
				ConstantReport.METER_PROFILE_REPORT_TEMPL_FILE_LOCATION = template_file;
				break;

			default:
				break;

		}
	}

	public static ArrayList<String> getIMappingDefaultValues() {
		ArrayList<String> default_values = new ArrayList<String>();
		for (int i = 1; i <= ConstantAppConfig.I_MAPPING_SIZE; i++) {
			try {
				if (ConstantAppConfigReader.getString("IMappingDefaultValues", "I" + i) != null) {
					default_values.add(ConstantAppConfigReader.getString("IMappingDefaultValues", "I" + i));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("getIMappingDefaultValues: Exception:" + i + ":" + e.getMessage());
			}
		}

		return default_values;
	}

	public static ArrayList<String> getPF_MappingDefaultValues() {
		ArrayList<String> default_values = new ArrayList<String>();
		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++) {
			try {
				if (ConstantAppConfigReader.getString("PF_MappingDefaultValues", "PF" + i) != null) {
					default_values.add(ConstantAppConfigReader.getString("PF_MappingDefaultValues", "PF" + i));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("getPF_MappingDefaultValues: Exception:" + i + ":" + e.getMessage());
			}
		}

		return default_values;
	}

	public static ArrayList<String> getABC_PF_Values() {
		ApplicationLauncher.logger.info("getABC_PF_Values: Entry:");
		ArrayList<String> default_values = new ArrayList<String>();

		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++) {
			try {
				if (ConstantAppConfigReader.getString("ABC_PF_MappingDefaultValues", "PF" + i) != null) {
					default_values.add(ConstantAppConfigReader.getString("ABC_PF_MappingDefaultValues", "PF" + i));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("getABC_PF_Values: Exception:" + i + ":" + e.getMessage());
			}
		}

		return default_values;
	}

	public static ArrayList<String> getA_B_C_PF_Values() {
		ApplicationLauncher.logger.info("getA_B_C_PF_Values: Entry");
		ArrayList<String> default_values = new ArrayList<String>();

		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++) {
			try {
				if (ConstantAppConfigReader.getString("A_B_C_PF_MappingDefaultValues", "PF" + i) != null) {
					default_values.add(ConstantAppConfigReader.getString("A_B_C_PF_MappingDefaultValues", "PF" + i));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("getA_B_C_PF_Values: Exception:" + i + ":" + e.getMessage());
			}
		}

		return default_values;
	}

	public static ArrayList<String> getPF_RowHeaderValues() {
		ApplicationLauncher.logger.info("getPF_RowHeaderValues: Entry");
		ArrayList<String> default_values = new ArrayList<String>();

		for (int i = 1; i <= ConstantAppConfig.PF_MAPPING_SIZE; i++) {
			try {
				if (ConstantAppConfigReader.getString("PF_MappingRowHeader", "PF" + i) != null) {
					default_values.add(ConstantAppConfigReader.getString("PF_MappingRowHeader", "PF" + i));
				}
			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("getPF_RowHeaderValues: Exception:" + i + ":" + e.getMessage());
			}
		}

		return default_values;
	}

}
