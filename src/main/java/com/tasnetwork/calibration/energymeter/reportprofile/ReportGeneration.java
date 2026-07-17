package com.tasnetwork.calibration.energymeter.reportprofile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfigReader;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.custom1report.Custom1ReportConfigModel;
import com.tasnetwork.calibration.energymeter.custom1report.ExcelReportMeterDataCellPositionPage;
import com.tasnetwork.calibration.energymeter.custom1report.ExcelReportMeterDataDisplayPage;
import com.tasnetwork.calibration.energymeter.custom1report.TestTypeFilter;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.OperationProcess;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;
import com.tasnetwork.spring.orm.model.RpPrintPosition;
import com.tasnetwork.spring.orm.service.OperationParamService;

import javafx.scene.control.Alert.AlertType;

public class ReportGeneration {


	
	

	private HashMap<String,String> meterDeviceOverAllStatus = new HashMap<String, String>();

	private ArrayList<String> EXCEL_DEFAULT_LIST = new ArrayList<String>(
			Arrays.asList(ConstantReportV2.NONE_DISPLAYED.toUpperCase(), 
					"DEFAULT"));

	private HashMap<String,Map<String, String>> meterDevicePageStatus = new HashMap<String,Map<String,String>>();

	private HashMap<String,Map<String, Integer>> meterDevicePageStatusCount = new HashMap<String,Map<String,Integer>>();
	private HashMap<String, Integer> meterDeviceOverAllStatusCount = new HashMap<String,Integer>();

	private JSONObject deployedDevicesJson = new JSONObject();

	private Map<String, String> repeatAverageMaxErrorAllowedHashMap =  new HashMap<String,String>();
	private Map<String, String> repeatAverageMinErrorAllowedHashMap =  new HashMap<String,String>();

	private Map<String, String> selfHeatAverageMaxErrorAllowedHashMap =  new HashMap<String,String>();
	private Map<String, String> selfHeatAverageMinErrorAllowedHashMap =  new HashMap<String,String>();

	private Map<String, String> constantTestResultRsmInitialHashMap =  new HashMap<String,String>();
	private Map<String, String> constantTestResultRsmFinalHashMap =  new HashMap<String,String>();
	private Map<String, String> constantTestResultRsmDiffHashMap =  new HashMap<String,String>();
	private Map<String, String> constantTestMaxErrorAllowedHashMap =  new HashMap<String,String>();
	private Map<String, String> constantTestMinErrorAllowedHashMap =  new HashMap<String,String>();

	//private Map<String, String> repeatAverageTestFilterNameTestPointFilterHashMap =  new HashMap<String,String>();
	//private Map<String, String> selfHeatAverageFilterNameTestCasePreviewHashMap =  new HashMap<String,String>();

	private JSONArray deployedTestCaseDetailsList = new JSONArray();

	public static int serialNumberMaxCount = 1;

	private Map<String, Boolean>  constantTestDutInitalAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestDutFinalAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestDutDiffAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestDutErrorPercentAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestRsmInitalAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestRsmFinalAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();
	private Map<String, Boolean>  constantTestRsmDiffAutoInsertionDataProcessedHashMap = new HashMap<String,Boolean>();


	private static Map<String, Boolean> processRepeatAverageValueHashMap = new HashMap<String,Boolean>();
	private static Map<String, Boolean> processSelfHeatAverageValueHashMap = new HashMap<String,Boolean>();
	private static Map<String, Boolean> processRepeatAverageStatusHashMap = new HashMap<String,Boolean>();
	private static Map<String, Boolean> processSelfHeatAverageStatusHashMap = new HashMap<String,Boolean>();

	private volatile int presentPageNumber = 0;
	private volatile int maxReportPageNumber = 0;

	private String complyStatus = ConstantReportV2.POPULATE_DOES_NOT_COMPLY;

	public final boolean OPERATION_PROCESS_ENABLED = true;

	/*	public static boolean processRepeatAverageValue = true; 
	public static boolean processSelfHeatAverageValue = true;


	public static boolean processRepeatAverageStatus = true; 
	public static boolean processSelfHeatAverageStatus = true;*/

	private String dutMeterDataPreviousPageLastUpdatedRackId = "0";
	private int dutMeterDataPreviousPageUpdatedPageNumber = 0;
	private JSONArray meterNames = new JSONArray();
	private boolean individualMeterReportSelected = false;
	private String selectedMeterSerialNo = ConstantReport.REPORT_SELECTED_ALL_METERS;

	private JSONObject meterProfileData = new JSONObject();

	private List<String> dutSerialNumberList = new ArrayList<String>();

	OperationProcessJsonReadModel  operationProcessDataModel = DeviceDataManagerController.getReportProfileConfigParsedKey();

	static private OperationParamService reportOperationParamService = DeviceDataManagerController.getRpOperationParamService();
	//private ArrayList<OperationParam>  operationParameterProfileDataList = new ArrayList<OperationParam>();

	private OperationProcessDataJsonRead targetOperationProcessInternalInputData = new OperationProcessDataJsonRead();
	private OperationProcessDataJsonRead targetOperationProcessInternalOutputData = new OperationProcessDataJsonRead();
	private OperationProcessDataJsonRead targetOperationProcessExternalOutputData = new OperationProcessDataJsonRead();

	/*private OperationProcessDataJsonRead repeatAverageOperationProcessData = new OperationProcessDataJsonRead();
	private OperationProcessDataJsonRead selfHeatAverageOperationProcessData = new OperationProcessDataJsonRead();

	private OperationProcessDataJsonRead repeatAverageOperationProcessData = new OperationProcessDataJsonRead();
	private OperationProcessDataJsonRead selfHeatAverageOperationProcessData = new OperationProcessDataJsonRead();
	 */
	private MultiValuedMap<String,String> repeatAggregatedAverageProcessData = new ArrayListValuedHashMap<String,String>();
	private MultiValuedMap<String,String> selfHeatAggregatedAverageProcessData = new ArrayListValuedHashMap<String,String>();

	//private Map<String,String> constantTestDutInitialInputProcessData = new HashMap<String,String>();
	//private Map<String,String> constantTestDutFinalInputProcessData = new HashMap<String,String>();
	//private MultiValuedMap<String,String> constantTestDutDiffAggregatedOutputProcessData = new ArrayListValuedHashMap<String,String>();


	private HashMap<String,String> resultRepeatAverageValueHashMap = new LinkedHashMap<String,String>();
	private HashMap<String,String> resultRepeatAverageStatusHashMap = new LinkedHashMap<String,String>();


	private HashMap<String,String> resultSelfHeatAverageValueHashMap = new LinkedHashMap<String,String>();
	private HashMap<String,String> resultSelfHeatAverageStatusHashMap = new LinkedHashMap<String,String>();


	PrintStyle loadedPrintStyle = new PrintStyle();
	String userSelectedPrintStyleResultName = "";
	String userSelectedPrintStyleGenericHeaderName = "";
	String userSelectedPrintStyleTableHeaderName = "";

	public boolean processReportGeneration(JSONObject result, ReportProfileManage reportProfileManage, 
			JSONArray resultMeterNames, JSONObject dutProfileData, int maxPageNumber) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("processReportGeneration: Entry ");

		String sourceTemplateFilePathName = "";
		String destinationPath = "";


		setMeterNames(resultMeterNames);
		setMeterProfileData(dutProfileData);
		setSelectedMeterSerialNo(ConstantReport.REPORT_SELECTED_ALL_METERS);
		//sourceTemplateFilePathName = custom1ReportCfg.getTemplateFileLocationPath();
		//destinationPath = custom1ReportCfg.getReportOutputPath();

		sourceTemplateFilePathName = reportProfileManage.getTemplateFileNameWithPath();
		destinationPath = reportProfileManage.getOutputFolderPath();

		ApplicationLauncher.logger.debug("processReportGeneration: sourceTemplateFilePathName: "+ sourceTemplateFilePathName);
		ApplicationLauncher.logger.debug("processReportGeneration: destinationPath: "+ destinationPath);


		boolean status = false;
		boolean TemplateFilePathExist = false;
		boolean OutputFilePathExist = true;
		HSSFWorkbook HSSFworkbook = null;
		HSSFSheet HSSF_Sheet = null;
		XSSFWorkbook XSSFworkbook = null;
		XSSFSheet XSSF_Sheet = null;
		boolean hssf_Format = true;
		FileInputStream file = null;
		try {
			//file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
			file = new FileInputStream(new File(sourceTemplateFilePathName));

			TemplateFilePathExist = true;

			try{
				HSSFworkbook = new HSSFWorkbook(file);
				HSSF_Sheet = HSSFworkbook.getSheetAt(0);
			}catch(Exception e1){
				e1.printStackTrace();
				hssf_Format= false;
				file.close();
				//file = new FileInputStream(new File(ConstantReport.ACC_TEMPL_FILE_LOCATION));
				file = new FileInputStream(new File(sourceTemplateFilePathName));
				XSSFworkbook = new XSSFWorkbook(file);
				XSSF_Sheet = XSSFworkbook.getSheetAt(0);

			}
			//result = FilterResultByTestType(result, ConstantApp.ACCURACY_ALIAS_NAME);
			ApplicationLauncher.logger.debug("processReportGeneration: result: "+result.toString());

			/*			String voltage = ConstantReport.ACC_TEMPL_VOLT;
			ArrayList<String> currents = ConstantReport.ACC_TEMPL_CURRENTS;
			ArrayList<String> pfs = ConstantReport.ACC_TEMPL_PFS;
			ApplicationLauncher.logger.debug("processReportGeneration: result: " + result);*/
			if(hssf_Format){
				//status = PopulateACCResultsHSSF(HSSF_Sheet, result,voltage, currents, pfs);
			}else{
				status = populateReportGenerationResultsXSSF(XSSF_Sheet, result, reportProfileManage,maxPageNumber);//meterSerialNoStartingPosition);
			}
			file.close();
			//String file_path = getSaveFilePath(ConstantReport.SAVE_FILE_LOCATION);
			//String file_name = getSaveFileName(ConstantReport.ACC_TEMPL_FILE_LOCATION);
			String file_path = getSaveFilePath(destinationPath);
			String file_name = getSaveFileName(sourceTemplateFilePathName);
			TestReportController.setReportGeneratedPath(file_path);
			TestReportController.setReportGeneratedFileName(file_name);
			if (!new File(file_path).exists()){			
				OutputFilePathExist = false;
			}

			FileOutputStream out = null;
			if(hssf_Format){
				out = 	new FileOutputStream(new File(file_path + file_name));
				HSSFFormulaEvaluator.evaluateAllFormulaCells(HSSFworkbook);
				HSSFworkbook.write(out);
			}else{
				out = new FileOutputStream(new File(file_path + file_name.replace(".xls", ".xlsx")));
				XSSFFormulaEvaluator.evaluateAllFormulaCells(XSSFworkbook);
				XSSFworkbook.write(out);
			}
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("processReportGeneration: FileNotFoundException: " + e.getMessage());
			status = false;
			if (!TemplateFilePathExist){
				ApplicationLauncher.logger.info("processReportGeneration: Template not found: Report template configured for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Template not found", "Report template configured for Accuracy not found. Kindly reconfigure", AlertType.ERROR);
			}else if (!OutputFilePathExist){
				ApplicationLauncher.logger.info("processReportGeneration: Output Path not found: Report output path for Accuracy not found. Kindly reconfigure - Prompted ");
				ApplicationLauncher.InformUser("Output Path not found", "Report output path for Accuracy not found. Kindly reconfigure", AlertType.ERROR);

			}else {
				ApplicationLauncher.logger.info("processReportGeneration: File access failed: Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again - Prompted ");
				ApplicationLauncher.InformUser("File access failed", "Access denied for output path file for Accuracy. Kindly close the excel file if opened and try again", AlertType.ERROR);

			}
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
			ApplicationLauncher.logger.error("processReportGeneration: IOException: " + e.getMessage());
		}
		return status;
	}

	public String getSaveFilePath(String file_path){
		ApplicationLauncher.logger.debug("ReportGeneration: getSaveFilePath: file_path: " + file_path);
		String project_name = TestReportController.getProjectListCurrentValue();//get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.debug("ReportGeneration: getSaveFilePath: project_name: " + project_name);
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		if(test_params.length>2){ // added if "_" is added in the project filename
			project_name = "";
			int i=0;
			while (i<(test_params.length-1)){
				project_name = project_name + test_params[i]+"_";
				i++;
			}
			//project_name.replace((char) (project_name.length() - 1), '-');
			if(project_name.endsWith("_"))
			{
				project_name = project_name.substring(0,project_name.length() - 1);
			}
		}else{
			project_name = test_params[0];
		}
		ApplicationLauncher.logger.debug("ReportGeneration: getSaveFilePath: project_name2: " + project_name);
		int dateTimeIndex = 1;
		if(test_params.length>2){ // added if "_" is added in the project filename
			dateTimeIndex = test_params.length-1;
			ApplicationLauncher.logger.debug("ReportGeneration: getSaveFilePath: dateTimeIndex: " + dateTimeIndex);
		}
		String date_time = test_params[dateTimeIndex];
		ApplicationLauncher.logger.debug("ReportGeneration: data_time1: " + date_time);
		String[] time_msec = date_time.split("\\.");
		ApplicationLauncher.logger.debug("ReportGeneration: data_time2: " + date_time);
		date_time = time_msec[0];
		ApplicationLauncher.logger.debug("ReportGeneration: data_time3: " + date_time);
		date_time = date_time.replaceAll("[^0-9]", "");
		ApplicationLauncher.logger.debug("ReportGeneration: data_time4: " + date_time);
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;
		String first_folder_save_file_path = file_path  + date_time;
		String second_folder_save_file_path = file_path  + date_time + "\\\\" + project_name;
		ApplicationLauncher.logger.debug("ReportGeneration: save_file_path: " + first_folder_save_file_path);
		File files = new File(first_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("ReportGeneration: Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("ReportGeneration: Failed to create multiple directories!");
			}
		}
		files = new File(second_folder_save_file_path);
		if (!files.exists()) {
			if (files.mkdir()) {
				ApplicationLauncher.logger.debug("ReportGeneration: Multiple directories are created!");
			} else {
				ApplicationLauncher.logger.debug("ReportGeneration: Failed to create multiple directories!");
			}
		}
		//String save_file_path = file_path + "\\" + date_time + "\\" + project_name + "\\";
		
		String save_file_path = file_path + date_time + "\\" + project_name + "\\";
		return save_file_path;
	}

	public String getSaveFileName(String template_file_path){
		String template_name = template_file_path.substring(template_file_path.lastIndexOf("\\")+1);
		ApplicationLauncher.logger.debug("ReportGeneration: template_name: " + template_name);
		String[] file_ext = template_name.split("\\.");
		String file_wo_extension = file_ext[0];
		ApplicationLauncher.logger.debug("ReportGeneration: file_wo_extension : " + file_wo_extension);


		String project_name = TestReportController.getProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		String[] test_params = project_name.split("_");
		project_name = test_params[0];
		int dateTimeIndex = 1;
		if(test_params.length>2){
			dateTimeIndex = test_params.length-1;
			ApplicationLauncher.logger.debug("ReportGeneration: getSaveFileName: dateTimeIndex: " + dateTimeIndex);
		}
		String date_time = test_params[dateTimeIndex];
		ApplicationLauncher.logger.debug("ReportGeneration: data_time: " + date_time);
		String[] time_msec = date_time.split("\\.");
		date_time = time_msec[0];
		date_time = date_time.replaceAll("[^0-9]", "");
		String date = date_time.substring(0,8);
		String time = date_time.substring(8);
		date_time = date + "_" + time;

		String file_name = file_wo_extension + "_" + date_time + ".xls";
		ApplicationLauncher.logger.debug("ReportGeneration: file_name: " + file_name);

		return file_name;
	}


	public Optional<RpPrintPosition> findKeyExistInPrintPosition(List<RpPrintPosition> rpPrintPositionDataList,  String dataKey){


		Optional<RpPrintPosition> rpPrintPositionOpt = rpPrintPositionDataList.stream()
				.filter(e -> e.getDataOwner().equals(ConstantReportV2.RP_DATA_OWNER_TEST_DATA_FILTER))
				.filter(e -> e.getKeyParam().equals( dataKey))
				.findFirst();

		return rpPrintPositionOpt;

	}

	public void manipulatePrintStyle(String userSelectedPrintStyleName){
		if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){
			PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
					.filter(e -> e.getReportPrintStyleName().equals(userSelectedPrintStyleName))
					.findFirst()
					.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

			setLoadedPrintStyle(resultPrintStyle);
		}
	}

	public String getTestPointFilterData(String testCasePreview  ){


		ApplicationLauncher.logger.debug("getTestPointFilterData: testCasePreview: " + testCasePreview);

		try {
			//String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
			//testCasePreview = "NLD_XX-115U";
			//if(testCasePreview.startsWith(ConstantApp.TEST_PROFILE_STA)){
			//	testCasePreview = testCasePreview.replace("U-1.0-", "U-");
			//}
			String[] splittedData = testCasePreview.split(ConstantReportV2.TEST_ID_MASK);

			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[0]:" + splittedData[0]);
			ApplicationLauncher.logger.debug("getTestPointFilterData: splittedData[1]:" + splittedData[1]);
			String testCaseFilterName = splittedData[1].substring(0, splittedData[1].lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.substring(0, testCaseFilterName.lastIndexOf(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR));
			testCaseFilterName = testCaseFilterName.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB, "I").replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "I");
			ApplicationLauncher.logger.debug("getTestPointFilterData: testCaseFilterName:" + testCaseFilterName);
			return testCaseFilterName;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getTestPointFilterData : Exception:"+e.getMessage());
		}

		return "";
	}


	public String getDataFromDeployedTestPoint(String keyParam,String inpTestCasePreview  ){

		String testCasePreview = new String(inpTestCasePreview);
		ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: keyParam: " + keyParam);
		ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: testCasePreview: " + testCasePreview);
		//String keyParam = "test_period_in_sec";

		String Projectname = TestReportController.getSelectedProjectName();
		String deploymentID = TestReportController.getSelectedDeploymentID();
		float value = 0.0f;
		JSONArray testcaselist;
		try {
			testcaselist = getDeployedTestCaseDetailsList();//ProjectExecutionController.getListOfTestPoints(Projectname,deploymentID);
			//setDeployedTestCaseDetailsList(testcaselist);
			//ArrayList<ArrayList<Object>> i_rowValues = initRowValues(testcaselist, columnNames);
			//String testCasePreview = "STA_XX-100U-1.0-0.004Ib";
			//testCasePreview = "NLD_XX-115U";
			if(inpTestCasePreview.startsWith(ConstantApp.STA_ALIAS_NAME)){
				testCasePreview = testCasePreview.replace("U-1.0-", "U-");
			}else if(inpTestCasePreview.startsWith(ConstantApp.CREEP_ALIAS_NAME)){
				testCasePreview = testCasePreview.replace("-1.0", "");
			}else if(inpTestCasePreview.startsWith(ConstantApp.CUSTOM_TEST_ALIAS_NAME)){
				testCasePreview = testCasePreview.replace(
						ConstantApp.CUSTOM_TEST_ALIAS_NAME + ConstantReportV2.TEST_POINT_NAME_SEPERATOR,"");
			}
			String[] splittedData = testCasePreview.split(ConstantReportV2.TEST_ID_MASK);

			ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: splittedData[0]:" + splittedData[0]);
			ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: splittedData[1]:" + splittedData[1]);
			
			if(inpTestCasePreview.startsWith(ConstantApp.CUSTOM_TEST_ALIAS_NAME)){
				splittedData[1] = splittedData[0];
				ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: custom: splittedData[1]:" + splittedData[1]);
			}

			JSONObject jobj = new JSONObject();
			boolean testCaseIdentified = false;

			for(int i=0; i<testcaselist.length(); i++){
				//ArrayList<Object> EachRowValues = new ArrayList<Object>();
				jobj = testcaselist.getJSONObject(i);
				String readTestCaseName = jobj.getString("test_case");

				ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: readTestCaseName: " + readTestCaseName);
				if(readTestCaseName.startsWith(splittedData[0])){
					if(readTestCaseName.endsWith(splittedData[1])){
						ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: readTestCaseName matching ");
						testCaseIdentified = true;
						/*value = Float.parseFloat(jobj.getString(keyParam));
						if(keyParam.equals("test_period_in_sec")){
							value = value/60;
						}else if(keyParam.equals("warmup_period_in_sec")){
							value = value/60;
						}
						//break;

						ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: value: " + String.valueOf(value));
						return String.valueOf(value);*/
						ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: jobj: " + jobj);
						String readData = jobj.getString(keyParam);
						ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: readData: " + readData);
						if(!readData.isEmpty()){
							if(keyParam.endsWith("pf")){

								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: pf Data : " + readData);
								return readData;

							}else if(keyParam.equals("min_error_allowed")){

								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: min_error_allowed : " + readData);
								return readData;

							}else if(keyParam.equals("max_error_allowed")){

								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: max_error_allowed : " + readData);
								return readData;

							}else if(keyParam.equals("test_period_in_sec")){

								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: test_period_in_sec : " + readData);
								return readData;

							}else if(keyParam.equals("warmup_period_in_sec")){

								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: warmup_period_in_sec : " + readData);
								return readData;

							}else {
								//if(!readData.isEmpty()){
								value = Float.parseFloat(readData);
								/*if(keyParam.equals("test_period_in_sec")){
									value = value/60;
									ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: test_period_in_sec: value: " + String.valueOf(value));
									return String.valueOf(value);
								}else 
									*/
								/*if(keyParam.equals("warmup_period_in_sec")){
									//value = Float.parseFloat(readData);
									value = value/60;
									ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: warmup_period_in_sec: value: " + String.valueOf(value));
									return String.valueOf(value);
								}*/
								ApplicationLauncher.logger.debug("getDataFromDeployedTestPoint: value: " + String.valueOf(value));
								return String.valueOf(value);
								//}
							}
						}

					}

				}
			}
			//if(testCaseIdentified){

			//}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataFromDeployedTestPoint : JSONException:"+e.getMessage());
		}

		return "";

	}


	public void loadParamProfileDataFromDataBase(String paramProfileName){
		ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: Entry ");
		//ref_tvOperationParamProfile.getItems().clear();
		String customerId = ConstantAppConfig.REPORT_PROFILE_DEFAULT_ACTIVE_CUSTOMER_ID;
		//String paramProfileName = (String) ref_cmbBxOperationParamProfileName.getSelectionModel().getSelectedItem();
		List<OperationParam> operationParamDataList = getReportOperationParamService().
				findByCustomerIdAndOperationParamProfileName(customerId,paramProfileName);
		//int maxSerialNo = 0;
		//setSerialNo(new AtomicInteger(0));



		List<OperationProcessDataJsonRead> operationLocalInputList = new ArrayList<OperationProcessDataJsonRead>();
		List<OperationProcessDataJsonRead> operationLocalOutputList = new ArrayList<OperationProcessDataJsonRead>();
		List<OperationProcessDataJsonRead> operationMasterOutputList = new ArrayList<OperationProcessDataJsonRead>();

		//ArrayList<OperationParam> operationParamProfileDataList = new ArrayList<OperationParam>();
		/*		operationParamDataList.stream().forEach( e -> {
			if(e.isPopulateOnlyHeaders()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ONLY_HEADERS);
			}else if(e.isResultTypeAverage()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT_AVERAGE);
			}else if(e.isPopulateAllDut()){
				e.setPopulateType(ConstantReportV2.POPULATE_DATA_TYPE_ALL_DUT);
			}

			ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: getPopulateType: " + e.getPopulateType());
			//if(Integer.parseInt(e.getTableSerialNo()) > getSerialNo().get()){
			//	setSerialNo(new AtomicInteger(Integer.parseInt(e.getTableSerialNo())));
			//}
			//operationParamProfileDataList.add(e);
		});*/

		operationParamDataList.stream()
		.filter( e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT))
		.forEachOrdered( e -> {
			OperationProcessDataJsonRead opProcessDataJson = new OperationProcessDataJsonRead();
			opProcessDataJson.setOperationProcessKey(e.getKeyParam());
			//ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: input: " + e.getPopulateType());
			//ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: input getParamType: " + e.getParamType());
			//ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: input getKeyParam: " + e.getKeyParam());
			//ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: input isResultTypeAverage: " + e.isResultTypeAverage());
			/*if(e.isResultTypeAverage()){
				opProcessDataJson.setResultTypeAverage(true);
			}else{
				opProcessDataJson.setResultTypeAverage(false);
			}*/
			if(e.isPopulateOnlyHeaders()){
				opProcessDataJson.setPopulateOnlyHeaders(true);

			}else {
				opProcessDataJson.setPopulateOnlyHeaders(false);
			}
			operationLocalInputList.add(opProcessDataJson);
		});

		operationParamDataList.stream()
		.filter( e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT))
		.forEachOrdered( e -> {
			OperationProcessDataJsonRead opProcessDataJson = new OperationProcessDataJsonRead();
			opProcessDataJson.setOperationProcessKey(e.getKeyParam());
			//ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: getKeyParam: " + e.getKeyParam());
			ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: output: " + e.getPopulateType());
			/*if(e.isResultTypeAverage()){
				opProcessDataJson.setResultTypeAverage(true);
			}else{
				opProcessDataJson.setResultTypeAverage(false);
			}*/
			if(e.isPopulateOnlyHeaders()){
				opProcessDataJson.setPopulateOnlyHeaders(true);

			}else {
				opProcessDataJson.setPopulateOnlyHeaders(false);
			}
			operationLocalOutputList.add(opProcessDataJson);
		});

		operationParamDataList.stream()
		.filter( e -> e.getParamType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
		.forEachOrdered( e -> {
			OperationProcessDataJsonRead opProcessDataJson = new OperationProcessDataJsonRead();
			opProcessDataJson.setOperationProcessKey(e.getKeyParam());
			ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: master output: " + e.getPopulateType());
			/*if(e.isResultTypeAverage()){
				opProcessDataJson.setResultTypeAverage(true);
			}else{
				opProcessDataJson.setResultTypeAverage(false);
			}*/
			if(e.isPopulateOnlyHeaders()){
				opProcessDataJson.setPopulateOnlyHeaders(true);

			}else {
				opProcessDataJson.setPopulateOnlyHeaders(false);
			}
			operationMasterOutputList.add(opProcessDataJson);
		});


		OperationProcessDataJsonRead opProcessDataDutInitialAutoInsertionJson = new OperationProcessDataJsonRead();
		opProcessDataDutInitialAutoInsertionJson.setOperationProcessKey(ConstantReportV2.PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY);
		//opProcessDataDutInitialAutoInsertionJson.setResultTypeAverage(false);
		opProcessDataDutInitialAutoInsertionJson.setPopulateOnlyHeaders(false);

		OperationProcessDataJsonRead opProcessDataDutFinalAutoInsertionJson = new OperationProcessDataJsonRead();
		opProcessDataDutFinalAutoInsertionJson.setOperationProcessKey(ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY);
		//opProcessDataDutFinalAutoInsertionJson.setResultTypeAverage(false);
		opProcessDataDutFinalAutoInsertionJson.setPopulateOnlyHeaders(false);

		operationLocalInputList.add(opProcessDataDutInitialAutoInsertionJson);
		operationLocalInputList.add(opProcessDataDutFinalAutoInsertionJson);


		OperationProcessDataJsonRead opProcessDataDutDiffAutoInsertionJson = new OperationProcessDataJsonRead();
		opProcessDataDutDiffAutoInsertionJson.setOperationProcessKey(ConstantReportV2.PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY);
		//opProcessDataDutDiffAutoInsertionJson.setResultTypeAverage(false);
		opProcessDataDutDiffAutoInsertionJson.setPopulateOnlyHeaders(false);

		operationLocalOutputList.add(opProcessDataDutDiffAutoInsertionJson);



		OperationProcessDataJsonRead opProcessDataRsmDiffAutoInsertionJson = new OperationProcessDataJsonRead();
		opProcessDataRsmDiffAutoInsertionJson.setOperationProcessKey(ConstantReportV2.PROCESS_PAGE_OUTPUT_RSM_DIFF_AUTO_INSERT_KEY);
		//opProcessDataRsmDiffAutoInsertionJson.setResultTypeAverage(false);
		opProcessDataRsmDiffAutoInsertionJson.setPopulateOnlyHeaders(true);

		operationLocalOutputList.add(opProcessDataRsmDiffAutoInsertionJson);


		getOperationProcessDataModel().setOperationLocalInput(operationLocalInputList);		
		getOperationProcessDataModel().setOperationLocalOutput(operationLocalOutputList);


		/*getOperationProcessDataModel().getOperationLocalOutput().stream()
		.forEach( e-> {
			ApplicationLauncher.logger.debug("loadParamProfileDataFromDataBase: getOperationLocalOutput1: " + e.getOperationProcessKey());
		});*/

		getOperationProcessDataModel().setOperationMasterOutput(operationMasterOutputList);
		//setOperationParameterProfileDataList(operationParamProfileDataList);

	}



	public Boolean populateReportGenerationResultsXSSF(XSSFSheet sheet1, JSONObject inputResult,
			ReportProfileManage custom1ReportCfg, int inputMaxPageNumber){//String meterSerialNoStartingPosition){


		ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Entry ");

		JSONObject result = inputResult;
		String meterSerialNoStartingPosition = "";
		List<ReportProfileTestDataFilter> filterTestType = new ArrayList<ReportProfileTestDataFilter>();
		filterTestType = custom1ReportCfg.getReportProfileTestDataFilterList();//.getExcelReportTestTypeFilter().getTestTypeFilter();
		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);  

		int NumberOfPages = ConstantAppConfig.ACC_NO_OF_PAGES_IN_REPORT;//2;
		//int page2_row_pos = row_pos;//row_pos+30;// ConstantReport.ACC_TEMPL_ROW;
		//int presentPageNumber = 1;		
		int maxPageNumberSupported = inputMaxPageNumber;//3;
		int readPageNumber = 0;

		boolean status = true;
		JSONArray unfilteredResult = new JSONArray ();
		String dataStartingErrorValueCellPosition = "";
		String dataStartingErrorStatusCellPosition = "";
		String dataStartingCellPosition = "";
		int serialNoMaxCount = 1;
		int row_pos = 1;
		int meter_col = 1;
		int column_pos = 0;
		boolean populateResult = false;
		int populateMeterProfileDataIterationCount = 1;
		boolean populateMeterProfileDataforEachMeter = false;
		String testType = "";
		String voltageFilter = "";
		String currentFilter = "";
		String pfFilter = "";
		String freqFilter = "";
		//String voltUnbalanceFilter = "";
		String resultIterationId = "0";
		boolean populateDataErrorValue = false;
		boolean populateDataStatus = false;
		String testFilterPopulateType = ConstantReport.REPORT_DATA_POPULATE_VERTICAL;
		//boolean appendMeterSerialNoAndRackPosition = false;
		boolean discardRackPositionInDutSerialNumber = false;

		int maxDutDisplayRequestedPerPage = custom1ReportCfg.getMaxDutDisplayPerPage();
		boolean splitDutDisplayIntoMultiplePage = custom1ReportCfg.isSplitDutDisplayInToMultiplePage();//.getSplitDutDisplayIntoMultiplePage();
		setDutMeterDataPreviousPageUpdatedPageNumber(0);
		setDutMeterDataPreviousPageLastUpdatedRackId("0");
		boolean errorInReportJsonConfiguration = true;



		try {

			//if(OPERATION_PROCESS_ENABLED){

			//}
			//getRepeatAverageOperationProcessData().setOperationProcessKey(ConstantReportV2.PARAM_PROFILE_REPEAT_AVERAGE_KEY);
			//getSelfHeatAverageOperationProcessData().setOperationProcessKey(ConstantReportV2.PARAM_PROFILE_SELF_HEAT_AVERAGE_KEY);
			//getSelfHeatAverageOperationProcessData().getResultValueHashMap().clear();
			//getSelfHeatAverageOperationProcessData().getResultStatusHashMap().clear();

			setRepeatAggregatedAverageProcessData(null);
			setSelfHeatAggregatedAverageProcessData(null);

			clearResultRepeatAverageValueHashMap();			
			clearResultRepeatAverageStatusHashMap();
			clearResultSelfHeatAverageValueHashMap();			
			clearResultSelfHeatAverageStatusHashMap();

			getConstantTestDutInitalAutoInsertionDataProcessedHashMap().clear();
			getConstantTestDutFinalAutoInsertionDataProcessedHashMap().clear();
			getConstantTestDutDiffAutoInsertionDataProcessedHashMap().clear();
			getConstantTestDutErrorPercentAutoInsertionDataProcessedHashMap().clear();
			getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().clear();
			getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().clear();
			getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().clear();


			getConstantTestResultRsmDiffHashMap().clear();
			getConstantTestMaxErrorAllowedHashMap().clear();
			getConstantTestMinErrorAllowedHashMap().clear();
			//setOperationProcessDataModel();
			String paramProfileName = custom1ReportCfg.getParameterProfileName();
			loadParamProfileDataFromDataBase(paramProfileName);

			setComplyStatus(ConstantReportV2.POPULATE_DOES_NOT_COMPLY);
			JSONObject deployedDevice = MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());
			setDeployedDevicesJson(deployedDevice);
			if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){
				setLoadedPrintStyle(ConstantAppConfig.PRINT_STYLE_LIST.get(0));
			}

			loadTestCaseDetails();

			setUserSelectedPrintStyleResultName(custom1ReportCfg.getPrintStyleResult());
			setUserSelectedPrintStyleGenericHeaderName(custom1ReportCfg.getPrintStyleGenericHeader());
			setUserSelectedPrintStyleTableHeaderName(custom1ReportCfg.getPrintStyleTableHeader());

			clearMeterDeviceOverAllStatus();
			clearMeterDevicePageStatus();
			clearMeterDevicePageStatusCount();
			clearMeterDeviceOverAllStatusCount();
			setMaxReportPageNumber(maxPageNumberSupported);
			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){
				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: next Page Iteration: " + pageNumberIteration);
				//for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataCellPositionIndex++ ){
				setPresentPageNumber(pageNumberIteration);
				int pNumber = 	pageNumberIteration;
				//ReportProfileMeterMetaDataFilter meterDataCellPositionPage

				meterSerialNoStartingPosition =  custom1ReportCfg.getDutMetaDataList().stream()//.get(meterDataCellPositionIndex);
						.filter(e -> e.getPageNumber() == pNumber)
						//.filter(e -> e.getMeterDataType().equals(ConstantReportV2.REPORT_METER_SERIAL_NO ))
						.filter(e -> e.isPopulateDutSerialNo() == true)
						.findFirst()
						.get()
						.getCellPosition();
				//if(meterDataCellPositionPage.getPageNumber() == pageNumberIteration){
				//meterSerialNoStartingPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
				//meterSerialNoStartingPosition = meterDataCellPositionPage.getCellPosition();//.getCellStartPositionMeterSerialNo();
				row_pos = getRowValueFromCellValue(meterSerialNoStartingPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
				meter_col = getColValueFromCellValue(meterSerialNoStartingPosition);//ConstantReport.ACC_TEMPL_METER_COL.get(0);
				column_pos = 0;

				unfilteredResult =  result.getJSONArray("Results");
				serialNoMaxCount = 1;
				populateMeterProfileDataIterationCount = 1;
				populateMeterProfileDataforEachMeter = false;
				ArrayList<String> uniqueMeterSerialNoList = new ArrayList<String>();
				//ApplicationLauncher.logger.info("populateReportGenerationResultsXSSF: pageNumberIteration: "+pageNumberIteration);
				//ApplicationLauncher.logger.info("populateReportGenerationResultsXSSF: meter_col: "+meter_col);
				//ApplicationLauncher.logger.info("populateReportGenerationResultsXSSF: row_pos: "+row_pos);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){
					PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
								.filter(e -> e.getReportPrintStyleName().equals(userSelectedPrintStyleName))
								.findFirst()
								.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

					setResultPrintStyle(resultPrintStyle);
				}*/
				uniqueMeterSerialNoList = FillMeterColumnXSSF_V2(sheet1, unfilteredResult,row_pos, meter_col,maxDutDisplayRequestedPerPage,splitDutDisplayIntoMultiplePage,pageNumberIteration);
				//clearMeterDeviceOverAllStatus();
				//clearMeterDevicePageStatus();gfhg
				//clearMeterDevicePageStatus(String.valueOf(pageNumberIteration));
				for(int i = 0; i< uniqueMeterSerialNoList.size() ; i++){
					ApplicationLauncher.logger.info("populateReportGenerationResultsXSSF: uniqueMeterSerialNoList: "+uniqueMeterSerialNoList.get(i));
					if(!getMeterDeviceOverAllStatus().containsKey(uniqueMeterSerialNoList.get(i))){
						//addMeterDeviceOverAllStatus(uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_PASS);
						addMeterDeviceOverAllStatus(uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_UNDEFINED);
						addMeterDeviceOverAllStatusCount(uniqueMeterSerialNoList.get(i), 0);
						//addMeterDeviceOverAllStatus(uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_FAIL);
					}

					//for(int pageNo= 1 ; pageNo < maxPageNumberSupported; pageNo++){
					//addMeterDevicePageStatus(String.valueOf(pageNumberIteration),uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_PASS);
					addMeterDevicePageStatus(String.valueOf(pageNumberIteration),uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_UNDEFINED);
					addMeterDevicePageStatusCount(String.valueOf(pageNumberIteration),uniqueMeterSerialNoList.get(i), 0);
					//addMeterDevicePageStatus(String.valueOf(pageNumberIteration),uniqueMeterSerialNoList.get(i), ConstantReport.REPORT_POPULATE_FAIL);
					///}
				}
				getMeterDevicePageStatus().entrySet().forEach(e -> {

					ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Page StatusX: " + e.getKey() + " -> " + e.getValue());

				});
				setDutSerialNumberList(uniqueMeterSerialNoList);
				serialNoMaxCount = getSerialNumberMaxCount();
				ApplicationLauncher.logger.info("populateReportGenerationResultsXSSF: serialNoMaxCount: "+serialNoMaxCount);
				//}
				//}
				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataDisplayIndex++ ){
					ReportProfileMeterMetaDataFilter meterDataDisplayPage =  custom1ReportCfg.getDutMetaDataList().get(meterDataDisplayIndex);
					//ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if((meterDataDisplayPage.getPageNumber() == pageNumberIteration) && (!meterDataDisplayPage.getMeterDataType().equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO)) ){

						populateDutMetaDataOnReport(sheet1,meterDataDisplayPage,serialNoMaxCount,meter_col);

					}
				}


				populateDataErrorValue = false;
				populateDataStatus = false;
				testFilterPopulateType = ConstantReport.REPORT_DATA_POPULATE_VERTICAL;
				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: pageNumberIteration: " + pageNumberIteration);
				for (int i = 0; i < filterTestType.size(); i++){
					//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test1");
					//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: filterTestType getPrefixType: "+filterTestType.get(i).getPrefixType());
					ReportProfileTestDataFilter filterTestTypeData = filterTestType.get(i);


					if(pageNumberIteration == Integer.parseInt(filterTestType.get(i).getPageNumber())){
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test2");
						populateResult = filterTestTypeData.isFilterActive();//getPopulateResult();
						if(populateResult){

							ArrayList<String> inputProcessDataList = new ArrayList<String>();

							List<OperationProcess> rpOperationProcessList = filterTestTypeData.getOperationProcessDataList();
							rpOperationProcessList.stream()
							.filter(e -> e.isAddedToInputProcess() == true)
							.forEach( e ->  inputProcessDataList.add(e.getOperationProcessKey()));
							filterTestTypeData.setOperationProcessInputKeyList(inputProcessDataList);

							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test3");
							JSONObject filteredResult = new JSONObject();
							testType  = filterTestTypeData.getTestTypeAlias();//.getPrefixType();
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: testType: "+ testType);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getFilterPreview: "+ filterTestTypeData.getFilterPreview());
							if(!filterTestTypeData.getTestTypeSelected().equals(ConstantReportV2.NONE_DISPLAYED)){
								if(filterTestTypeData.getTestTypeSelected().equals(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST)){
									String customTestName = filterTestTypeData.getCustomTestNameUserEntry();
									ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: customTestName: "+ customTestName);
									filteredResult = FilterResultForCustomTest(inputResult, customTestName);
								}else{
									filteredResult = FilterResultByTestType(inputResult, testType);
								}

								populateResultDataOnReport(sheet1,meter_col,  filterTestTypeData,filteredResult,testType);

								if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) ||
										(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
									processAverageDataOnReport(sheet1,meter_col,  filterTestTypeData,testType);
								}else if (testType.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){

									manipulateAutoInsertionConstantDutErrorCalculation(filterTestTypeData, sheet1,  meter_col);
									
								}
							}

						}
					}
				}

				//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: populateResultDataOnReport - End");
				if(OPERATION_PROCESS_ENABLED){
					for (int i = 0; i < filterTestType.size(); i++){
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test1");
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: filterTestType getPrefixType: "+filterTestType.get(i).getPrefixType());
						ReportProfileTestDataFilter filterTestTypeData = filterTestType.get(i);
						if(pageNumberIteration == Integer.parseInt(filterTestType.get(i).getPageNumber())){
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: pageNumberIteration: " + pageNumberIteration);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getTableSerialNo: " + filterTestTypeData.getTableSerialNo());
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getTestFilterNameX: " + filterTestTypeData.getTestFilterName());
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test2");
							populateResult = filterTestTypeData.isFilterActive();//getPopulateResult();
							if(populateResult){
								//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test3");
								//if(filterTestTypeData.getTestTypeSelected().equals(ConstantReportV2.NONE_DISPLAYED)){
								//if(filterTestTypeData.getOperationMode().equals(ConstantReportV2.OPERATION_PROCESS_MODE_OUTPUT)){
								if( (filterTestTypeData.getOperationMode().equals(ConstantReportV2.OPERATION_PROCESS_MODE_OUTPUT))
										|| (filterTestTypeData.getOperationMode().equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT))
										){
									/*ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test4");
									if(filterTestTypeData.getOperationProcessMethod().equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)){
										ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test5");
										//testType  = filterTestTypeData.getTestTypeAlias();
										if( (isProcessRepeatAverageValue()) ||
												(isProcessSelfHeatAverageValue()) ){
											ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test6");											
											manipulateOutputProcessDataOnReport(filterTestTypeData, sheet1,  meter_col);
										}

									}else {*/
									if(filterTestTypeData.getOperationProcessMethod().equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)){
										ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test7");

										manipulateOutputProcessDataOnReport(filterTestTypeData, sheet1,  meter_col);					
									}

								}
							}
						}
					}
				}



			}

			ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: update Master - begin ");

			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){

				if(OPERATION_PROCESS_ENABLED){
					for (int i = 0; i < filterTestType.size(); i++){
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test1");
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: filterTestType getPrefixType: "+filterTestType.get(i).getPrefixType());
						ReportProfileTestDataFilter filterTestTypeData = filterTestType.get(i);
						if(pageNumberIteration == Integer.parseInt(filterTestType.get(i).getPageNumber())){
							setPresentPageNumber(pageNumberIteration);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: master : pageNumberIteration: " + pageNumberIteration);
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: master : pageNumberIterationX2: " + filterTestTypeData.getFilterPreview());
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: master : getTableSerialNo: " + filterTestTypeData.getTableSerialNo());
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: master : getTestFilterName: " + filterTestTypeData.getTestFilterName());
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test2");
							populateResult = filterTestTypeData.isFilterActive();//getPopulateResult();
							if(populateResult){
								//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Test3");
								//if(filterTestTypeData.getTestTypeSelected().equals(ConstantReportV2.NONE_DISPLAYED)){
								if(filterTestTypeData.getOperationMode().equals(ConstantReportV2.OPERATION_PROCESS_MODE_OUTPUT)){
									ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: TestX5");
									ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getOperationProcessMethod(): " + filterTestTypeData.getOperationProcessMethod());
									//if(!filterTestTypeData.getOperationProcessMethod().equals(ConstantReportV2.OPERATION_PROCESS_MODE_NONE)){
										ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: TestX6");

										manipulateOutputProcessDataOnReport(filterTestTypeData, sheet1,  meter_col);					
									//}

								}
							}
						}
					}
				}				
			}

			ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Page status and Over all status - begin ");

			getMeterDevicePageStatus().entrySet().forEach(e -> {

				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Page Status: " + e.getKey() + " -> " + e.getValue());

			});

			getMeterDeviceOverAllStatus().entrySet().forEach(e -> {

				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: OverAll Status: " + e.getKey() + " -> " + e.getValue());

			});

			getMeterDeviceOverAllStatusCount().entrySet().forEach(e -> {

				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: OverAll Status Count: " + e.getKey() + " -> " + e.getValue());

			});
			processPageStatus(maxPageNumberSupported);

			processOverAllStatus(maxPageNumberSupported);

			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){

				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataDisplayIndex++ ){
					ReportProfileMeterMetaDataFilter meterDataDisplayPage =  custom1ReportCfg.getDutMetaDataList().get(meterDataDisplayIndex);
					//ExcelReportMeterDataDisplayPage meterDataDisplayPage =  custom1ReportCfg.getExcelReportMeterDataDisplay().getExcelReportMeterDataDisplayPage().get(meterDataDisplayIndex);
					//ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: pageNumberX: " + pageNumberIteration);
						//errorInReportJsonConfiguration = true;

						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: meterDataCellPositionPage: Hit3");

						getMeterDevicePageStatusCount().entrySet().forEach(e -> {

							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: Page Status Count: " + e.getKey() + " -> " + e.getValue());

						});


						populateResult = meterDataDisplayPage.isPopulateDutPageStatus();
						ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: populateResult: " + populateResult);
						if(populateResult){
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: meterDataCellPositionPage: Hit4");
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();
							String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
							manipulatePrintStyle(userSelectedPrintStyleName);
							dataStartingCellPosition = meterDataDisplayPage.getCellPosition();;//meterDataCellPositionPage.getCellStartPositionDutOverAllStatus();
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: dataStartingCellPosition: " + dataStartingCellPosition);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: pageNumberIteration: " + pageNumberIteration);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: getMeterDevicePageStatus().containsKey(pageNumberIteration): " + getMeterDevicePageStatus().containsKey(pageNumberIteration));
							if(getMeterDevicePageStatus().containsKey(String.valueOf(pageNumberIteration))){
								ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: populateResult: Test1");
								fillMeterPageStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col,pageNumberIteration);
							}
						}


						populateResult = meterDataDisplayPage.isPopulateDutOverAllStatus();
						ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutOverAllStatus: populateResult: " + populateResult);
						if(populateResult){
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: meterDataCellPositionPage: Hit4");
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();
							String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
							manipulatePrintStyle(userSelectedPrintStyleName);
							dataStartingCellPosition = meterDataDisplayPage.getCellPosition();;//meterDataCellPositionPage.getCellStartPositionDutOverAllStatus();
							fillMeterOverAllStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col);
							//manipulateComplyStatus();
						}
						manipulateComplyStatus();
						/*populateResult = meterDataDisplayPage.isPopulateDutPageStatus();
						ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: populateResult: " + populateResult);
						if(populateResult){
							//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: meterDataCellPositionPage: Hit4");
							//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionDutOverAllStatus();
							String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
							manipulatePrintStyle(userSelectedPrintStyleName);
							dataStartingCellPosition = meterDataDisplayPage.getCellPosition();;//meterDataCellPositionPage.getCellStartPositionDutOverAllStatus();
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: dataStartingCellPosition: " + dataStartingCellPosition);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: pageNumberIteration: " + pageNumberIteration);
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: getMeterDevicePageStatus().containsKey(pageNumberIteration): " + getMeterDevicePageStatus().containsKey(pageNumberIteration));
							if(getMeterDevicePageStatus().containsKey(String.valueOf(pageNumberIteration))){
								ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: getPopulateDutPageStatus: populateResult: Test1");
								fillMeterPageStatusColumnXSSF(  sheet1, dataStartingCellPosition, meter_col,pageNumberIteration);
							}
						}*/
					}
				}

				boolean alreadyDiscardRackPositionUpdated = false;
				ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: TestA1");
				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataDisplayIndex++ ){
					ReportProfileMeterMetaDataFilter meterDataDisplayPage =  custom1ReportCfg.getDutMetaDataList().get(meterDataDisplayIndex);
					//ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if(meterDataDisplayPage.getPageNumber() == pageNumberIteration){
						//ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: meterDataDisplayPage: Hit1");
						//errorInReportJsonConfiguration = true;

						//appendMeterSerialNoAndRackPosition = meterDataDisplayPage.getAppendMeterSerialNoAndRackPosition();
						if(meterDataDisplayPage.isPopulateDutSerialNo()){
							discardRackPositionInDutSerialNumber= meterDataDisplayPage.isDiscardRackPositionInDutSerialNumber();
							if(discardRackPositionInDutSerialNumber){
								if(!alreadyDiscardRackPositionUpdated){
									//dataStartingCellPosition = custom1ReportCfg.getExcelReportMeterDataCellPosition().getCellStartPositionMeterSerialNo();
									dataStartingCellPosition = meterDataDisplayPage.getCellPosition();//meterDataCellPositionPage.getCellStartPositionMeterSerialNo();
									ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: dataStartingCellPosition: " + dataStartingCellPosition);
									removeRackIdInMeterSerialNoColumnXSSF_V2(  sheet1, dataStartingCellPosition, meter_col);//,maxDutDisplayRequestedPerPage);
									alreadyDiscardRackPositionUpdated = true;
								}
							}
						}
					}
				}


			}



			// for printing over all comply status

			for(int pageNumberIteration = 1;  pageNumberIteration <=maxPageNumberSupported; pageNumberIteration++ ){
				//for(int meterDataCellPositionIndex = 0;  meterDataCellPositionIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataCellPositionIndex++ ){
				setPresentPageNumber(pageNumberIteration);
				int pNumber = 	pageNumberIteration;


				serialNoMaxCount = getSerialNumberMaxCount();
				for(int meterDataDisplayIndex = 0;  meterDataDisplayIndex < custom1ReportCfg.getDutMetaDataList().size(); meterDataDisplayIndex++ ){
					ReportProfileMeterMetaDataFilter meterDataDisplayPage =  custom1ReportCfg.getDutMetaDataList().get(meterDataDisplayIndex);
					//ExcelReportMeterDataCellPositionPage meterDataCellPositionPage = null;
					if((meterDataDisplayPage.getPageNumber() == pageNumberIteration) && (!meterDataDisplayPage.getMeterDataType().equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO)) ){
						populateResult =  meterDataDisplayPage.isPopulateComplyStatus();
						if(populateResult){
							ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: overall comply status");
							populateComplyStatusOnReport(sheet1,meterDataDisplayPage,serialNoMaxCount);
						}

					}
				}

			}





			if(ConstantAppConfig.REPORT_DATA_REPLACE_ENABLED){
				for (int i=0; i < ConstantAppConfig.REPORT_DATA_REPLACE_COUNT;i++){
					ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: i: " + i);
					ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: ConstantAppConfig.REPORT_DATA_FIND.get(i): " + ConstantAppConfig.REPORT_DATA_FIND.get(i));
					ApplicationLauncher.logger.debug("populateReportGenerationResultsXSSF: ConstantAppConfig.REPORT_DATA_REPLACE.get(i): " + ConstantAppConfig.REPORT_DATA_REPLACE.get(i));
					replaceDataInReportExcel(sheet1, ConstantAppConfig.REPORT_DATA_FIND.get(i),ConstantAppConfig.REPORT_DATA_REPLACE.get(i));
				}

			}
			//}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("populateReportGenerationResultsXSSF: JSONException: " + e.getMessage());
		}





		return status;

	}

	public boolean isAllCriteriaSatisfiedForConstantErrorCalculation(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1, int meter_col) {
		ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Entry");

		String testFilterPreview = filterTestTypeData.getFilterPreview();
		ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: testFilterPreview: " + testFilterPreview);
		boolean dutDiffCalculationDone = false;
		try{
			for(int iteration = 1; iteration < 10 ; iteration++){
				ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: iteration: " + iteration);
				try{
					if(getConstantTestDutInitalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
						ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut Inital statisfied");
						if(getConstantTestDutFinalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
							ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut final satisfied");
							if(!dutDiffCalculationDone){
								ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut difference calculation");
								manipulateAutoInsertionConstantDutDifference(filterTestTypeData, sheet1,  meter_col);
								dutDiffCalculationDone = true;
							}
							if(getConstantTestDutDiffAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
								ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut diff satisfied");
								if(getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
									ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Rsm Intital satisfied");
									if(getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
										ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Rsm Final satisfied");
										if(getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().containsKey(testFilterPreview)){
											ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Rsm Diff key exist");

											if(getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
												ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Rsm Diff satisfied");
												ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation : All criteria met for Constant Error Calculation AutoInsertion");
												return true;

											}else{
												ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: RSM Difference processing");
												float rsmInitialValue = Float.parseFloat(getConstantTestResultRsmInitialHashMap().get(testFilterPreview));
												float rsmFinalValue = Float.parseFloat(getConstantTestResultRsmFinalHashMap().get(testFilterPreview));
												float rsmDiffValue = Math.abs( rsmFinalValue - rsmInitialValue);
												String rsmDiffValueStr = "";
												if(rsmDiffValue>=0){
													rsmDiffValueStr= String.format("+%.03f", rsmDiffValue);
												}else{
													rsmDiffValueStr= String.format("%.03f", rsmDiffValue);
												}
												ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: rsmDiffValueStr : " + rsmDiffValueStr);
												//FillReportDataColumnXSSF_V1_1(sheet1, rsmDiffValueStr,row_pos, column_pos);
												getConstantTestResultRsmDiffHashMap().put(testFilterPreview, rsmDiffValueStr);
												HashMap<String, String> rsmDiffResultHashMap = new HashMap<String, String>();
												rsmDiffResultHashMap.put("Result", rsmDiffValueStr);

												getOperationProcessDataModel().getOperationLocalOutput().stream()
												.filter(e -> e.getOperationProcessKey().equals(ConstantReportV2.PROCESS_PAGE_OUTPUT_RSM_DIFF_AUTO_INSERT_KEY))
												.findFirst()
												.get()
												.setResultValueHashMap(rsmDiffResultHashMap);

												getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);


											}
										}else{
											ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: inserting Rsm Difference Key for further processing");
											getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
										}
									}else{
										ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: RSM Final processing");
										String populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL);
										//if(!getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
										populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL);

										//String refStdValue = fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation( populateResultFilterDataType, 
										//		voltageFilter, currentFilter, pfFilter,energyFilter);
										String refStdValue = fetchConstantTestRsmDataForAutoOperationProcess	(filterTestTypeData, populateResultFilterDataType);	
										ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: final refStdValue: " + refStdValue);
										float rsmFinalValue = Float.parseFloat(refStdValue);
										getConstantTestResultRsmFinalHashMap().put(testFilterPreview, refStdValue);
										getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
										ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: rsmFinalValue: " + rsmFinalValue);
										ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: initial getConstantTestRsmFinalAutoInsertionDataProcessedHashMap: " + getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().get(testFilterPreview));


										//}
									}
								}else{
									ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: RSM initial processing");
									String populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL);


									//if(!getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
									populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL);
									String refStdValue = fetchConstantTestRsmDataForAutoOperationProcess	(filterTestTypeData, populateResultFilterDataType);	

									ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: initial refStdValue: " + refStdValue);
									float rsmInitialValue = Float.parseFloat(refStdValue);
									getConstantTestResultRsmInitialHashMap().put(testFilterPreview, refStdValue);
									ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: initial getConstantTestResultRsmInitialHashMap: " + getConstantTestResultRsmInitialHashMap().get(testFilterPreview));
									getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
									//}


									//String refStdValue = fillConstRefStValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
									//		row_pos, column_pos);
									//ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: refStdValue: " + refStdValue);

									//float rsmInitialValue = Float.parseFloat(refStdValue);
									//getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);



								}
							}else{
								ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: ConstantTestDutDiff caluclation failed");
							}

						}else{

							ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut Final processing");
							String populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_FINAL_REGISTER);

							String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY;
							Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
									ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


							if(operationProcessDataJsonReadDataOpt.isPresent()){
								setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
								//ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: operationProcessDataJsonReadDataOpt: " + operationProcessDataJsonReadDataOpt.get().getOperationProcessKey());
							}else{
								setTargetOperationProcessInternalInputData(null);
							}

							ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: final populateResultFilterDataType: " + populateResultFilterDataType);

							fetchConstantTestDutDataForAutoOperationProcess(filterTestTypeData, populateResultFilterDataType);

							getConstantTestDutFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);

						}

					}else{

						ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: Dut Inital processing");
						String populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_INITIAL_REGISTER);

						String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY;
						Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
								ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


						if(operationProcessDataJsonReadDataOpt.isPresent()){
							setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
							//ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: operationProcessDataJsonReadDataOpt: " + operationProcessDataJsonReadDataOpt.get().getOperationProcessKey());
						}else{
							setTargetOperationProcessInternalInputData(null);
						}

						ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: initial populateResultFilterDataType: " + populateResultFilterDataType);

						fetchConstantTestDutDataForAutoOperationProcess(filterTestTypeData, populateResultFilterDataType);

						getConstantTestDutInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);

					}
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("isAllCriteriaSatisfiedForConstantErrorCalculation: Exception2: " + e.getMessage());
				}
			}
			ApplicationLauncher.logger.debug("isAllCriteriaSatisfiedForConstantErrorCalculation: All iteration completed!!!");
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("isAllCriteriaSatisfiedForConstantErrorCalculation: Exception1: " + e.getMessage());
		}

		return false;
	}


	private void manipulateAutoInsertionConstantDutErrorCalculation(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1, int meter_col) {
		// TODO Auto-generated method stub

		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: Entry");
		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		String operationSourceResultType = filterTestTypeData.getOperationSourceResultTypeSelected();
		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: operationSourceResultType: " + operationSourceResultType);
		//if(operationSourceResultType.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AUTO_INSERT_CALC_ERROR)){
		if(operationSourceResultType.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE)){

			if(isAllCriteriaSatisfiedForConstantErrorCalculation(filterTestTypeData,sheet1, meter_col)) {


				List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

				//String operationMode = filterTestTypeData.getOperationMode();
				//if(operationMode.equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT)){
				/*			String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();

												OperationProcessDataJsonRead operationProcessDataJsonReadData =  getOperationProcessDataModel().getOperationLocalInput().stream()
														.filter(e -> e.getOperationProcessKey().equals(operationStorageKey))
														.findFirst()
														.get();

												setTargetOperationProcessData(operationProcessDataJsonReadData);
				 */
				//}

				//List<String> operationAddedProcessKeyList = filterTestTypeData.getOperationProcessInputKeyList();
				String autoInsertionOperationMethod = ConstantReportV2.OPERATION_METHOD_ERROR_PERCENTAGE;;//filterTestTypeData.getOperationProcessMethod();
				filterTestTypeData.setOperationProcessMethod(autoInsertionOperationMethod);
				ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getOperationInputKey: " + filterTestTypeData.getOperationInputKey());
				filterTestTypeData.setOperationProcessLocalOutputKey(filterTestTypeData.getOperationInputKey());
				ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getOperationProcessLocalOutputKey: " + filterTestTypeData.getOperationProcessLocalOutputKey());
				ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: setting autoInsertionOperationMethod for Constant Test");

				filterTestTypeData.setOperationCompareLimitsSelected(true);

				
						
			    ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterUnitValue: " + filterTestTypeData.getEnergyFilterUnitValue());
			    ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterData: "  + filterTestTypeData.getEnergyFilterData());
				//filterTestTypeData.setOperationProcessLocalUpperLimit("+0.5");
				//filterTestTypeData.setOperationProcessLocalLowerLimit("-0.5");
			    
			    if(ProcalFeatureEnable.CONSTANT_TEST_RESULT_LIMIT_FROM_CONFIG_ENABLED) {
				
				    filterTestTypeData.setOperationProcessLocalUpperLimit(ConstantAppConfig.CONSTANT_TEST_DEFAULT_ACCEPTED_UPPER_LIMIT);
					filterTestTypeData.setOperationProcessLocalLowerLimit(ConstantAppConfig.CONSTANT_TEST_DEFAULT_ACCEPTED_LOWER_LIMIT);
					
					
					for(int i = 1; i<= ConstantAppConfig.CONSTANT_TEST_RESULT_CONFIG_LIMIT_COUNT; i++){
						
						ConstantAppConfig.CONSTANT_TEST_FILTER_UNIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestFilterUnit"+i) ));
						ConstantAppConfig.CONSTANT_TEST_FILTER_VALUE.add(ConstantAppConfigReader.getString("Report", ("ConstantTestFilterValue"+i) ));
						ConstantAppConfig.CONSTANT_TEST_ACCEPTED_UPPER_LIMIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestAcceptedUpperLimits"+i) ));
						ConstantAppConfig.CONSTANT_TEST_ACCEPTED_LOWER_LIMIT.add(ConstantAppConfigReader.getString("Report", ("ConstantTestAcceptedLowerLimits"+i) ));
						
						if(filterTestTypeData.getEnergyFilterUnitValue().equals(ConstantAppConfig.CONSTANT_TEST_FILTER_UNIT.get(i))) {
							if(filterTestTypeData.getEnergyFilterData().equals(ConstantAppConfig.CONSTANT_TEST_FILTER_VALUE.get(i))) {
								filterTestTypeData.setOperationProcessLocalUpperLimit(ConstantAppConfig.CONSTANT_TEST_ACCEPTED_UPPER_LIMIT.get(i));
								filterTestTypeData.setOperationProcessLocalLowerLimit(ConstantAppConfig.CONSTANT_TEST_ACCEPTED_LOWER_LIMIT.get(i));
								ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: updated new limits from config - index: " + i);
								
								//ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterUnitValue2: " + filterTestTypeData.getEnergyFilterUnitValue());
							    // ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterData2: "  + filterTestTypeData.getEnergyFilterData());
								
								
								ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterUnitValue2: " + filterTestTypeData.getEnergyFilterUnitValue());
							    ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: getEnergyFilterData2: "  + filterTestTypeData.getEnergyFilterData());
								
								
								break;
							}
						}
					
	
					}

			    }else {
			    	filterTestTypeData.setOperationProcessLocalUpperLimit(getConstantTestMaxErrorAllowedHashMap().get(filterTestTypeData.getFilterPreview()));
					filterTestTypeData.setOperationProcessLocalLowerLimit(getConstantTestMinErrorAllowedHashMap().get(filterTestTypeData.getFilterPreview()));

			    }

				



				setTargetOperationProcessInternalInputData(null);
				setTargetOperationProcessInternalOutputData(null);
				setTargetOperationProcessExternalOutputData(null);

				ArrayList<String> operationAddedProcessKeyList = new ArrayList<String>();//filterTestTypeData.getOperationProcessInputKeyList();

				//operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY);
				operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_OUTPUT_RSM_DIFF_AUTO_INSERT_KEY);
				operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY);
				filterTestTypeData.setOperationProcessInputKeyList(operationAddedProcessKeyList);
				String rpPrintPositionKeyForErrorValueCalculation = ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE;
				Optional<RpPrintPosition> rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
						.filter(e -> e.getKeyParam().equals(rpPrintPositionKeyForErrorValueCalculation))
						.findFirst();
				if(rpPrintPositionDataOptional.isPresent()){
					ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: rpPrintPositionDataOptional Value Exist");
					RpPrintPosition rpPrintPositionAutoInsertionData = rpPrintPositionDataOptional.get();
					rpPrintPositionAutoInsertionData.setKeyParam(filterTestTypeData.getOperationInputKey());


					filterTestTypeData.getRpPrintPositionList().add(rpPrintPositionAutoInsertionData);
				}

				String rpPrintPositionKeyForErrorStatus = ConstantReportV2.RESULT_SOURCE_TYPE_RESULT_STATUS_KEY;
				rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
						.filter(e -> e.getKeyParam().equals(rpPrintPositionKeyForErrorStatus))
						.findFirst();
				if(rpPrintPositionDataOptional.isPresent()){
					ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutErrorCalculation: rpPrintPositionDataOptional Status Exist");
					RpPrintPosition rpPrintPositionAutoInsertionData = rpPrintPositionDataOptional.get();
					rpPrintPositionAutoInsertionData.setKeyParam(ConstantReportV2.POPULATE_LOCAL_OUTPUT_STATUS_KEY);


					filterTestTypeData.getRpPrintPositionList().add(rpPrintPositionAutoInsertionData);
				}
				//filterTestTypeData.setOperationProcessLocalOutputKey(ConstantReportV2.PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY);


				try{
					processOperationMethod(filterTestTypeData);
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("manipulateAutoInsertionConstantDutErrorCalculation: Exception1: " + e.getMessage());
				}
				//}
				if(getTargetOperationProcessInternalOutputData()!=null){
					populateInternalOutputData(filterTestTypeData, sheet1, meter_col);
				}

				//if(getTargetOperationProcessInternalInputData()!=null){
				//	populateInternalOutputData(filterTestTypeData, sheet1, meter_col);
				//populateInternalInputData(filterTestTypeData, sheet1, meter_col);
				//}
				/*if(getTargetOperationProcessExternalOutputData()!=null){
										populateExternalOutputData(filterTestTypeData, sheet1, meter_col);
									}*/
				//}
				//}

				//}

				/*	}
							}
						}
					}

				}
			}*/
			}
		}

	}

	private void manipulateAutoInsertionConstantDutDifference(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1, int meter_col) {
		// TODO Auto-generated method stub
		//ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutDifference: Entry");

		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutDifference: Entry");
		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutDifference: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		//List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

		String maxErrorAllowedKey = "max_error_allowed";
		String minErrorAllowedKey = "min_error_allowed";



		//ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview2: " + testFilterPreviewWithIterationId);
		String testFilterPreview = filterTestTypeData.getFilterPreview();
		String maxErrorAllowed =  getDataFromDeployedTestPoint(maxErrorAllowedKey,testFilterPreview);//WithIterationId);
		String minErrorAllowed =  getDataFromDeployedTestPoint(minErrorAllowedKey,testFilterPreview);//WithIterationId);
		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Constant minErrorAllowed: " + minErrorAllowed);
		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Constant maxErrorAllowed: " + maxErrorAllowed);

		getConstantTestMaxErrorAllowedHashMap().put(testFilterPreview, maxErrorAllowed);
		getConstantTestMinErrorAllowedHashMap().put(testFilterPreview, minErrorAllowed);

		String autoInsertionOperationMethod = ConstantReportV2.OPERATION_METHOD_DIFFERENCE;//filterTestTypeData.getOperationProcessMethod();
		filterTestTypeData.setOperationProcessMethod(autoInsertionOperationMethod);
		ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutDifference: setting autoInsertionOperationMethod for Constant Test");



		setTargetOperationProcessInternalInputData(null);
		setTargetOperationProcessInternalOutputData(null);
		setTargetOperationProcessExternalOutputData(null);

		ArrayList<String> operationAddedProcessKeyList = new ArrayList<String>();//filterTestTypeData.getOperationProcessInputKeyList();

		//operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY);
		operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY);
		operationAddedProcessKeyList.add(ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY);
		filterTestTypeData.setOperationProcessInputKeyList(operationAddedProcessKeyList);
		filterTestTypeData.setOperationProcessLocalOutputKey(ConstantReportV2.PROCESS_PAGE_OUTPUT_DUT_DIFF_AUTO_INSERT_KEY);

		try{
			processOperationMethod(filterTestTypeData);
			filterTestTypeData.setOperationProcessLocalOutputKey(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_DIFFERENCE);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("manipulateAutoInsertionConstantDutErrorCalculation: Exception2: " + e.getMessage());
		}



		if(getTargetOperationProcessInternalOutputData()!=null){
			populateInternalOutputData(filterTestTypeData, sheet1, meter_col);
			getConstantTestDutDiffAutoInsertionDataProcessedHashMap().put(filterTestTypeData.getFilterPreview(), true);
		}else{
			ApplicationLauncher.logger.debug("manipulateAutoInsertionConstantDutDifference: ppopulate not occured");
		}


	}

	private void processPageStatus(int maxPageNumberSupported) {

		ApplicationLauncher.logger.debug("processPageStatus: Entry ");
		// TODO Auto-generated method stub
		Map<Integer,Integer> maxPageStatusCountHashMap = new HashMap<Integer,Integer>();

		//int pageStatusCount = 0;
		for(int pageNumberIteration = 1;  pageNumberIteration <= maxPageNumberSupported; pageNumberIteration++ ){
			int page = pageNumberIteration;
			HashMap<String,Integer>  getMeterDevicePageStatusCount = (HashMap<String, Integer>) getMeterDevicePageStatusCount().entrySet().stream()
					.filter(k -> k.getKey().equals(String.valueOf(page)))
					.findFirst()
					.get()
					.getValue();

			int maxCount = getMeterDevicePageStatusCount.entrySet().stream().mapToInt(k -> k.getValue()).max().getAsInt();
			maxPageStatusCountHashMap.put(pageNumberIteration, maxCount);
		}

		maxPageStatusCountHashMap.entrySet().stream().forEach( e -> {
			ApplicationLauncher.logger.debug("processPageStatus: maxPageStatusCountHashMap: " + e.getKey() + " -> "+ +e.getValue());	
		});

		for(int pageNumberIteration = 1;  pageNumberIteration <= maxPageNumberSupported; pageNumberIteration++ ){
			int page = pageNumberIteration;
			HashMap<String,Integer>  getMeterDevicePageStatusCount = (HashMap<String, Integer>) getMeterDevicePageStatusCount().entrySet().stream()
					.filter(k -> k.getKey().equals(String.valueOf(page)))
					.findFirst()
					.get()
					.getValue();

			int totalCount = getMeterDevicePageStatusCount.entrySet().stream().mapToInt(k -> k.getValue()).max().getAsInt();

			ApplicationLauncher.logger.debug("processPageStatus: getMeterDevicePageStatus: " + getMeterDevicePageStatus().get(pageNumberIteration));
			for ( Entry<String, Integer> eachDeviceId : getMeterDevicePageStatusCount.entrySet()){
				//ApplicationLauncher.logger.debug("processPageStatus: eachDeviceId.getKey(): " + eachDeviceId.getKey());
				//ApplicationLauncher.logger.debug("processPageStatus: eachDeviceId.getValue(): " + eachDeviceId.getValue());
				//ApplicationLauncher.logger.debug("processPageStatus: get(pageNumberIteration): " + getMeterDevicePageStatus().get(String.valueOf(pageNumberIteration)));
				//ApplicationLauncher.logger.debug("processPageStatus: get(pageNumberIteration)2: " + getMeterDevicePageStatus().get(String.valueOf(pageNumberIteration)).get(eachDeviceId.getKey()));
				if(eachDeviceId.getValue() < maxPageStatusCountHashMap.get(pageNumberIteration)){
					getMeterDevicePageStatus().get(String.valueOf(pageNumberIteration)).put(eachDeviceId.getKey(),ConstantReport.REPORT_POPULATE_UNDEFINED);
				}else if(eachDeviceId.getValue() == maxPageStatusCountHashMap.get(pageNumberIteration)){
					if(getMeterDevicePageStatus().get(String.valueOf(pageNumberIteration)).get(eachDeviceId.getKey()).equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
						getMeterDevicePageStatus().get(String.valueOf(pageNumberIteration)).put(eachDeviceId.getKey(),ConstantReport.REPORT_POPULATE_PASS);	
					}
				}

			}

		}


	}


	private void processOverAllStatus(int maxPageNumberSupported) {

		ApplicationLauncher.logger.debug("processOverAllStatus: Entry ");
		// TODO Auto-generated method stub
		Map<Integer,Integer> maxOverAllStatusCountHashMap = new HashMap<Integer,Integer>();

		int overAllStatusMaxCount = getMeterDeviceOverAllStatusCount().entrySet().stream().mapToInt(k -> k.getValue()).max().getAsInt();

		ApplicationLauncher.logger.debug("processOverAllStatus: overAllStatusMaxCount: " + overAllStatusMaxCount);
		for(Entry<String, Integer> eachDeviceId : getMeterDeviceOverAllStatusCount().entrySet()){
			if(eachDeviceId.getValue() < overAllStatusMaxCount){
				getMeterDeviceOverAllStatus().put(eachDeviceId.getKey(),ConstantReport.REPORT_POPULATE_UNDEFINED);
			}else if(eachDeviceId.getValue() == overAllStatusMaxCount){
				if(getMeterDeviceOverAllStatus().get(eachDeviceId.getKey()).equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
					getMeterDeviceOverAllStatus().put(eachDeviceId.getKey(),ConstantReport.REPORT_POPULATE_PASS);	
				}
			}

		}

	}


	private void loadTestCaseDetails() {
		// TODO Auto-generated method stub

		ApplicationLauncher.logger.debug("loadTestCaseDetails: Entry ");

		String Projectname = TestReportController.getSelectedProjectName();
		String deploymentID = TestReportController.getSelectedDeploymentID();

		JSONArray testcaselist;
		try {
			testcaselist = ProjectExecutionController.getListOfTestPoints(Projectname,deploymentID);
			setDeployedTestCaseDetailsList(testcaselist);
			//ArrayList<ArrayList<Object>> i_rowValues = initRowValues(testcaselist, columnNames);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadTestCaseDetails : JSONException:"+e.getMessage());
		}

	}

	private void manipulateOutputProcessDataOnReport(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1, int meter_col) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("manipulateOutputProcessDataOnReport: Entry");
		ApplicationLauncher.logger.debug("manipulateOutputProcessDataOnReport: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		//List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

		//String operationMode = filterTestTypeData.getOperationMode();
		//if(operationMode.equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT)){
		/*			String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();

			OperationProcessDataJsonRead operationProcessDataJsonReadData =  getOperationProcessDataModel().getOperationLocalInput().stream()
					.filter(e -> e.getOperationProcessKey().equals(operationStorageKey))
					.findFirst()
					.get();

			setTargetOperationProcessData(operationProcessDataJsonReadData);
		 */
		//}

		//List<String> operationAddedProcessKeyList = filterTestTypeData.getOperationProcessInputKeyList();
		//String userSelectedOperationMethod = filterTestTypeData.getOperationProcessMethod();
		//String operationOutputStorageKey = "";//filterTestTypeData.getNonDisplayedDataSet();
		//if(!userSelectedOperationMethod.equals(ConstantReportV2.NONE_DISPLAYED)){
		//if(filterTestTypeData.isPopulateOperationLocalOutput()){
		//if(userSelectedOperationMethod.equals(ConstantReportV2.OPERATION_METHOD_AVERAGE)){
		//List<OperationProcessDataJsonRead> operationProcessDataJsonReadList = getOperationProcessDataModel().getOperationLocalOutput();



		setTargetOperationProcessInternalInputData(null);
		setTargetOperationProcessInternalOutputData(null);
		setTargetOperationProcessExternalOutputData(null);
		String operationMethod = filterTestTypeData.getOperationProcessMethod();
		if(operationMethod.equals(ConstantReportV2.NONE_DISPLAYED)){

			//List<String> operationAddedProcessKeyList = filterTestTypeData.getOperationProcessInputKeyList();

			/*operationAddedProcessKeyList.stream()
									//.filter( e -> e.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT))
									.forEachOrdered( e -> {
										ApplicationLauncher.logger.debug("manipulateOutputProcessDataOnReport: e : " + e);
									});*/

			/*boolean processedAverageMethod  = false;
			if(operationAddedProcessKeyList.size() > 0){
				Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationAddedProcessKeyList.get(0), 
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


				if(operationProcessDataJsonReadDataOpt.isPresent()){
					OperationProcessDataJsonRead operProcessDataJsonRead = operationProcessDataJsonReadDataOpt.get();
					if(operProcessDataJsonRead.isResultTypeAverage()){
						ApplicationLauncher.logger.debug("processAverageDataOnReport: getOperationProcessKey: " + operationProcessDataJsonReadDataOpt.get().getOperationProcessKey());
						Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
								.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
								//.filter(e-> e.)
								.findFirst();

						//String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
						if(externalOutputDataKeyOptional.isPresent()){
							//OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
							//if(operationProcessData.isResultTypeAverage()){	

							processBookOutputData(filterTestTypeData);
							processedAverageMethod = true;
							//}
						}
					}
					//setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
				}
			}
			 */			
			//.logger.debug("manipulateOutputProcessDataOnReport: processedAverageMethod: " + processedAverageMethod);

			//if(!processedAverageMethod){
			//processExternalOutputData(filterTestTypeData); // commented in version s4.2.0.3.0.6
			processBookOutputData(filterTestTypeData);
			//}
		}else{
			try{
				processOperationMethod(filterTestTypeData);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("manipulateOutputProcessDataOnReport: Exception: " + e.getMessage());
			}
		}
		if(getTargetOperationProcessInternalOutputData()!=null){
			populateInternalOutputData(filterTestTypeData, sheet1, meter_col);
		}

		if(getTargetOperationProcessExternalOutputData()!=null){
			populateExternalOutputData(filterTestTypeData, sheet1, meter_col);
		}
		//}
		//}

		//}



	}


	private void processExternalOutputData(ReportProfileTestDataFilter filterTestTypeData) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("processExternalOutputData: Entry");

		Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
				.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				//.filter(e-> (!e.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)))
				.findFirst();

		String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
		if(externalOutputDataKeyOptional.isPresent()){
			OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
			ApplicationLauncher.logger.debug("processExternalOutputData: getOperationProcessKey: " + operationProcessData.getOperationProcessKey());
			if(operationProcessData.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)){

			}else{
				externalOutputDataKey = operationProcessData.getOperationProcessKey();
				ApplicationLauncher.logger.debug("processExternalOutputData: externalOutputDataKey: " + externalOutputDataKey);
				Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(externalOutputDataKey, 	
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
				if(operationProcessDataOptional.isPresent()){
					OperationProcessDataJsonRead operationProcessMasterData = operationProcessDataOptional.get();
					//operationProcessMasterData.setResultValueHashMap(resultValueHashMap);
					//operationProcessMasterData.setResultStatusHashMap(resultStatusHashMap);
					setTargetOperationProcessExternalOutputData(operationProcessMasterData);
				}
			}
		}

	}

	private void populateInternalOutputData(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1,int meter_col) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateInternalOutputData: Entry");

		String internalOutputDataKey = filterTestTypeData.getOperationProcessLocalOutputKey();
		ApplicationLauncher.logger.debug("populateInternalOutputData: internalOutputDataKey: " + internalOutputDataKey);
		List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

		Optional<RpPrintPosition> rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
				.filter(e -> e.getKeyParam().equals(internalOutputDataKey))
				.findFirst();
		if(rpPrintPositionDataOptional.isPresent()){
			RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			ApplicationLauncher.logger.debug("populateInternalOutputData: getKeyParam0: " + rpPrintPositionData.getKeyParam());
			if(rpPrintPositionData.isPopulateOnlyHeaders()){

				ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateOnlyHeaders");
				ApplicationLauncher.logger.debug("populateInternalOutputData: getKeyParam: " + rpPrintPositionData.getKeyParam());

				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				String headerValue = "";


				HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();

				resultValueHashMap = getTargetOperationProcessInternalOutputData().getResultValueHashMap();
				ApplicationLauncher.logger.debug("populateInternalOutputData: resultValueHashMap: " + resultValueHashMap);

				if(resultValueHashMap.size()>0){
					headerValue = resultValueHashMap.entrySet().stream().findFirst().get().getValue();

				}
				//}

				FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col);

			}else if(rpPrintPositionData.isPopulateAllData()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: populate All DUT");
				HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();
				resultValueHashMap = getTargetOperationProcessInternalOutputData().getResultValueHashMap();
				boolean populateErrorValue = true;
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
				resultStatusHashMap = getTargetOperationProcessInternalOutputData().getResultStatusHashMap();
				if(resultStatusHashMap.size()>0){
					fillProcessedOutputDataXSSF_V2(sheet1, resultValueHashMap, resultStatusHashMap, column_pos,row_pos, meter_col);
				}else{
					fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				}


			}



		}

		List<RpPrintPosition> rpPrintPositionHeaderList = rpPrintPositionDataList.stream()
				.filter(e -> e.isPopulateOnlyHeaders())
				.collect(Collectors.toList());

		//if(rpPrintPositionDataOptional.isPresent()){
		// headerValue = "";
		rpPrintPositionHeaderList.forEach( rpPrintPositionData -> {

			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			String headerValue = "";
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			//if(rpPrintPositionData.isPopulateOnlyHeaders()){
			//ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateOnlyHeaders");
			ApplicationLauncher.logger.debug("populateInternalOutputData: getKeyParam2: " + rpPrintPositionData.getKeyParam());
			//gfhg

			if(rpPrintPositionData.isPopulateHeaderUpperLimit()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: UpperLimit");
				headerValue = filterTestTypeData.getOperationProcessLocalUpperLimit();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}else if(rpPrintPositionData.isPopulateHeaderLowerLimit()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: LowerLimit");	
				headerValue = filterTestTypeData.getOperationProcessLocalLowerLimit();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}else if(rpPrintPositionData.isPopulateHeaderMergedLimit()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: mergedLimit");	
				headerValue = filterTestTypeData.getOperationProcessLocalLowerLimit()+ " / " + filterTestTypeData.getOperationProcessLocalUpperLimit();;
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}

			//}

			if(rpPrintPositionData.isPopulateHeader1()){
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				headerValue = filterTestTypeData.getHeader1_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader2()){
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				headerValue = filterTestTypeData.getHeader2_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader3()){
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				headerValue = filterTestTypeData.getHeader3_Value();
			
			}else if(rpPrintPositionData.isPopulateHeader4()){
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				headerValue = filterTestTypeData.getHeader4_Value();
			
			}else if(rpPrintPositionData.isPopulateHeader5()){
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				headerValue = filterTestTypeData.getHeader5_Value();
			
			}else if(rpPrintPositionData.isPopulateHeaderRepeat()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateHeaderRepeat");
				String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedHeaderPrintStyleName);

				String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
				if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
					resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY, "");	
				}
				String repeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
				ApplicationLauncher.logger.debug("populateInternalOutputData: repeatHeaderValue: " + repeatHeaderValue);
				headerValue = repeatHeaderValue;//filterTestTypeData.getHeader3_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeaderSelfHeat()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateHeaderSelfHeat");
				String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedHeaderPrintStyleName);

				String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
				if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
					resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY, "");	
				}

				String selfHeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
				ApplicationLauncher.logger.debug("populateInternalOutputData: selfHeatHeaderValue: " + selfHeatHeaderValue);
				headerValue = selfHeatHeaderValue;

				//headerValue = filterTestTypeData.getHeader3_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeaderTestPeriodInMinutes()){
				ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateHeaderTestPeriodInMinutes");
				String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedHeaderPrintStyleName);
				String testCasePreview = filterTestTypeData.getFilterPreview();
				String keyParam = "test_period_in_sec";
				ApplicationLauncher.logger.debug("populateInternalOutputData: testCasePreview: " + testCasePreview);
				headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
				ApplicationLauncher.logger.debug("populateInternalOutputData: headerValue: " + headerValue);
				if(!headerValue.isEmpty()){
					if(Float.parseFloat(headerValue)> 59){
						float timeValueInSec = Float.parseFloat(headerValue);
						float timeValueInMin = timeValueInSec/60;
						headerValue = ConstantReportV2.TEST_PERIOD_PREFIX + String.format("%.02f", timeValueInMin) + ConstantReportV2.TEST_PERIOD_MINUTE_POSTFIX;
					}else{
						headerValue = ConstantReportV2.TEST_PERIOD_PREFIX + headerValue + ConstantReportV2.TEST_PERIOD_SECONDS_POSTFIX;
					}
					
				}

				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}



			if(!headerValue.isEmpty()){
				FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}
		});

		String resultStatusPopulateHeaderKey = ConstantReportV2.POPULATE_LOCAL_OUTPUT_STATUS_KEY;
		//ApplicationLauncher.logger.debug("populateInternalOutputData: localOutputStatus populate exist ");
		rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
				.filter(e -> e.getKeyParam().equals(resultStatusPopulateHeaderKey))
				.findFirst();

		if(rpPrintPositionDataOptional.isPresent()){
			ApplicationLauncher.logger.debug("populateInternalOutputData: localOutputStatus populate exist ");
			RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			if(rpPrintPositionData.isPopulateLocalResultStatus()){
				Boolean populateErrorValue = false;
				//ApplicationLauncher.logger.debug("populateInternalOutputData: resultStatusHashMap: " + resultStatusHashMap);

				HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
				resultStatusHashMap = getTargetOperationProcessInternalOutputData().getResultStatusHashMap();
				ApplicationLauncher.logger.debug("populateInternalOutputData: resultStatusHashMap: " + resultStatusHashMap);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				fillProcessedOutputDataXSSF(sheet1, resultStatusHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
			}
		}


		//}


	}



	private void populateExternalOutputData(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1,int meter_col) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateExternalOutputData: Entry");

		Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
				.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				//.filter(e-> (!e.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)))
				.findFirst();


		//ApplicationLauncher.logger.debug("populateInternalOutputData: internalOutputDataKey: " + internalOutputDataKey);

		List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();


		String upperLimitData = "";
		String lowerLimitData = "";

		//String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
		if(externalOutputDataKeyOptional.isPresent()){
			//String externalOutputDataKey = ;//filterTestTypeData.getOperationProcessLocalOutputKey();
			//List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

			OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
			String externalOutputDataKey = operationProcessData.getOperationProcessKey();
			ApplicationLauncher.logger.debug("populateExternalOutputData: externalOutputDataKey: " + externalOutputDataKey);
			if(externalOutputDataKey.equals(ConstantReportV2.NONE_DISPLAYED)){

			}else{


				//List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();

				String searchKey = externalOutputDataKey;
				Optional<RpPrintPosition> rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
						.filter(e -> e.getKeyParam().equals(externalOutputDataKey))
						.findFirst();
				if(rpPrintPositionDataOptional.isPresent()){

					RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
					String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
					int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
					int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
					//upperLimitData= 
					if(rpPrintPositionData.isPopulateOnlyHeaders()){
						ApplicationLauncher.logger.debug("populateExternalOutputData: isPopulateOnlyHeaders");
						ApplicationLauncher.logger.debug("populateExternalOutputData: getKeyParam1: " + rpPrintPositionData.getKeyParam());
						String headerValue = "";


						HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();

						resultValueHashMap = getTargetOperationProcessExternalOutputData().getResultValueHashMap();
						ApplicationLauncher.logger.debug("populateExternalOutputData: resultValueHashMap: " + resultValueHashMap);

						if(resultValueHashMap.size()>0){
							headerValue = resultValueHashMap.entrySet().stream().findFirst().get().getValue();

						}


						//}
						String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
						manipulatePrintStyle(userSelectedPrintStyleName);
						FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
					}else if(rpPrintPositionData.isPopulateAllData()){
						boolean populateErrorValue = true;
						HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();
						resultValueHashMap = getTargetOperationProcessExternalOutputData().getResultValueHashMap();
						HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
						resultStatusHashMap = getTargetOperationProcessExternalOutputData().getResultStatusHashMap();
						ApplicationLauncher.logger.debug("populateExternalOutputData: resultValueHashMap: " + resultValueHashMap);
						ApplicationLauncher.logger.debug("populateExternalOutputData: resultStatusHashMap: " + resultStatusHashMap);
						upperLimitData = getTargetOperationProcessExternalOutputData().getUpperLimit();
						lowerLimitData = getTargetOperationProcessExternalOutputData().getLowerLimit();
						ApplicationLauncher.logger.debug("populateExternalOutputData: upperLimitData: " + upperLimitData);
						ApplicationLauncher.logger.debug("populateExternalOutputData: lowerLimitData: " + lowerLimitData);
						String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
						manipulatePrintStyle(userSelectedPrintStyleName);
						ApplicationLauncher.logger.debug("populateExternalOutputData: getKeyParam2: " + rpPrintPositionData.getKeyParam());
						ApplicationLauncher.logger.debug("populateExternalOutputData: isPopulateEachDutAverageValue: " + rpPrintPositionData.isPopulateEachDutAverageValue());

						//if(rpPrintPositionData.isPopulateEachDutAverageValue()){
						if(resultStatusHashMap.size()>0){
							//HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
							resultStatusHashMap = getTargetOperationProcessExternalOutputData().getResultStatusHashMap();
							fillProcessedOutputDataXSSF_V2(sheet1, resultValueHashMap, resultStatusHashMap, column_pos,row_pos, meter_col);
							//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
						}else{
							fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
						}


					}


				}		




			}



		}



		List<RpPrintPosition> rpPrintPositionHeaderList = rpPrintPositionDataList.stream()
				.filter(e -> e.isPopulateOnlyHeaders())
				.collect(Collectors.toList());

		//if(rpPrintPositionDataOptional.isPresent()){
		// headerValue = "";


		String upperLimitDataTmp = upperLimitData;
		String lowerLimitDataTmp = lowerLimitData;
		rpPrintPositionHeaderList.forEach( rpPrintPositionData -> {

			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			String headerValue = "";
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			//if(rpPrintPositionData.isPopulateOnlyHeaders()){
			//ApplicationLauncher.logger.debug("populateInternalOutputData: isPopulateOnlyHeaders");
			ApplicationLauncher.logger.debug("populateExternalOutputData: getKeyParamX: " + rpPrintPositionData.getKeyParam());
			//gfhg

			if(rpPrintPositionData.isPopulateHeaderUpperLimit()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: UpperLimit");
				//upperLimitData = getTargetOperationProcessExternalOutputData().getUpperLimit();
				headerValue = upperLimitDataTmp; //filterTestTypeData.getOperationProcessLocalUpperLimit();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();;//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}else if(rpPrintPositionData.isPopulateHeaderLowerLimit()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: LowerLimit");	
				//lowerLimitData = getTargetOperationProcessExternalOutputData().getLowerLimit();
				headerValue = lowerLimitDataTmp;//filterTestTypeData.getOperationProcessLocalLowerLimit();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}else if(rpPrintPositionData.isPopulateHeaderMergedLimit()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: mergedLimit");	
				//upperLimitData = getTargetOperationProcessExternalOutputData().getUpperLimit();
				//lowerLimitData = getTargetOperationProcessExternalOutputData().getLowerLimit();
				headerValue = lowerLimitDataTmp+ " / " + upperLimitDataTmp;
				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
			}

			//}

			if(rpPrintPositionData.isPopulateHeader1()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: Header1");	
				headerValue = filterTestTypeData.getHeader1_Value();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader2()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: Header2");	
				headerValue = filterTestTypeData.getHeader2_Value();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader3()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: Header3");	
				headerValue = filterTestTypeData.getHeader3_Value();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader4()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: Header4");	
				headerValue = filterTestTypeData.getHeader4_Value();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeader5()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: Header5");	
				headerValue = filterTestTypeData.getHeader5_Value();
				String userSelectedPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeaderRepeat()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: isPopulateHeaderRepeat");
				String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedHeaderPrintStyleName);

				String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
				if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
					resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY, "");	
				}
				String repeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
				ApplicationLauncher.logger.debug("populateExternalOutputData: repeatHeaderValue: " + repeatHeaderValue);
				headerValue = repeatHeaderValue;//filterTestTypeData.getHeader3_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}else if(rpPrintPositionData.isPopulateHeaderSelfHeat()){
				ApplicationLauncher.logger.debug("populateExternalOutputData: isPopulateHeaderSelfHeat");
				String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
				manipulatePrintStyle(userSelectedHeaderPrintStyleName);

				String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
				if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
					resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY, "");	
				}

				String selfHeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
				ApplicationLauncher.logger.debug("populateExternalOutputData: selfHeatHeaderValue: " + selfHeatHeaderValue);
				headerValue = selfHeatHeaderValue;

				//headerValue = filterTestTypeData.getHeader3_Value();
				//FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}

			if(!headerValue.isEmpty()){
				FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

			}
		});

		String resultStatusPopulateHeaderKey = ConstantReportV2.POPULATE_MASTER_OUTPUT_STATUS_KEY;

		Optional<RpPrintPosition> rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
				.filter(e -> e.getKeyParam().equals(resultStatusPopulateHeaderKey))
				.findFirst();

		if(rpPrintPositionDataOptional.isPresent()){
			ApplicationLauncher.logger.debug("populateExternalOutputData: MasterOutputStatus populate exist ");
			RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			if(rpPrintPositionData.isPopulateMasterResultStatus()){
				Boolean populateErrorValue = false;
				//ApplicationLauncher.logger.debug("populateInternalOutputData: resultStatusHashMap: " + resultStatusHashMap);

				HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
				resultStatusHashMap = getTargetOperationProcessExternalOutputData().getResultStatusHashMap();
				ApplicationLauncher.logger.debug("populateExternalOutputData: resultStatusHashMap: " + resultStatusHashMap);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				fillProcessedOutputDataXSSF(sheet1, resultStatusHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
			}
		}
	}













	//}


	private void manipulateAverage(ReportProfileTestDataFilter filterTestTypeData, 
			MultiValuedMap<String,String> inpAggregatedAverageHashMap ,
			String upperLimitStr, String lowerLimitStr){//ReportProfileTestDataFilter filterTestTypeData) {
		ApplicationLauncher.logger.debug("manipulateAverage: Entry");

		//ApplicationLauncher.logger.debug("manipulateAverage: getRepeatAverageOperationProcessData().getResultValueHashMap(): " + getRepeatAverageOperationProcessData().getResultValueHashMap());

		inpAggregatedAverageHashMap.entries().stream()
		.forEachOrdered( e -> {
			ApplicationLauncher.logger.debug("manipulateAverage: key : " + e.getKey() + " -> " + e.getValue());

		});


		HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();


		inpAggregatedAverageHashMap.keySet().forEach(eachHashMapDataKey -> {
			ApplicationLauncher.logger.debug("manipulateAverage: Key: "  + eachHashMapDataKey + " -> " + inpAggregatedAverageHashMap.get(eachHashMapDataKey));
			//ApplicationLauncher.logger.debug("manipulateAverage: Value: "  + aggregatedResultValueHashMap.get(eachHashMapData.getKey()));
			String resultValueStr = "";

			Collection<String> resultList = inpAggregatedAverageHashMap.get(eachHashMapDataKey);
			//String operationMethod = filterTestTypeData.getOperationProcessMethod();

			/*			if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_ERROR_PERCENTAGE)){
				resultList.stream().mapToDouble(e -> Double.parseDouble(e))
					.forEachOrdered(e -> {
						ApplicationLauncher.logger.debug("manipulateAverage: Value: "  + e);
					});
			}*/


			double aggregatedResultValue = 0;

			aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
					.average()
					.getAsDouble();
			ApplicationLauncher.logger.debug("manipulateAverage: averageResultValue: "  + aggregatedResultValue);


			ApplicationLauncher.logger.debug("manipulateAverage: aggregatedResultValue: "  + aggregatedResultValue);


			if(aggregatedResultValue>=0){
				resultValueStr= String.format("+%.03f", aggregatedResultValue);
			}else{
				resultValueStr= String.format("%.03f", aggregatedResultValue);
			}

			ApplicationLauncher.logger.debug("manipulateAverage: resultValueStr: "  + resultValueStr);

			resultValueHashMap.put(eachHashMapDataKey, resultValueStr);
		});

		resultValueHashMap.entrySet().forEach( kv -> {
			ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getKey: "  + kv.getKey());
			ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getValue: "  + kv.getValue());
		});


		HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();

		//String upperLimitStr = "";
		//String lowerLimitStr = "";

		//if(populateStatus){

		//if(filterTestTypeData.isOperationCompareLimitsSelected()){
		getDutSerialNumberList().stream().forEachOrdered( e -> {

			//ApplicationLauncher.logger.debug("manipulateAverage: HeadersOnly deviceName : " + e + " -> " + singleResultValueToPopulateOnAllDevice); 
			resultStatusHashMap.put(e, "");
		});

		//upperLimitStr = "+0.064";//filterTestTypeData.getOperationProcessLocalUpperLimit();
		//lowerLimitStr = "-0.064";//filterTestTypeData.getOperationProcessLocalLowerLimit();
		float upperLimitValue = Float.parseFloat(upperLimitStr);
		float lowerLimitValue = Float.parseFloat(lowerLimitStr);
		ApplicationLauncher.logger.debug("manipulateAverage: upperLimitValue: "  + upperLimitValue);
		ApplicationLauncher.logger.debug("manipulateAverage: lowerLimitValue: "  + lowerLimitValue);

		if(upperLimitValue == lowerLimitValue){
			resultValueHashMap.entrySet().forEach( kv -> {
				ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getKeyB: "  + kv.getKey() + " -> " + kv.getValue());
				//ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getValue: "  + kv.getValue());
				float resultValue = Float.parseFloat(kv.getValue());
				//Range<Float> validRange = Range.between(lowerLimitValue, upperLimitValue);
				//boolean inRange = validRange.contains(element)
				//ApplicationLauncher.logger.debug("manipulateAverage: resultValue: "  + resultValue);
				ApplicationLauncher.logger.debug("manipulateAverage: both upper and lower equal: "  + lowerLimitValue);
				//if ( (lowerLimitValue >= resultValue) && (resultValue <= upperLimitValue)) {

				//ApplicationLauncher.logger.debug("manipulateAverage: lowerLimitValue: "  + lowerLimitValue);

				if (resultValue == lowerLimitValue) {
					resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_PASS);
					//resultValueHashMap.put(kv.getKey(), resultValueHashMap.get(kv.getKey()) + " " + ConstantReport.RESULT_STATUS_PASS);
					ApplicationLauncher.logger.debug("manipulateAverage: resultStatus: PassB");
				}else{
					resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
					//resultValueHashMap.put(kv.getKey(), resultValueHashMap.get(kv.getKey()) + " " + ConstantReport.RESULT_STATUS_FAIL);
					ApplicationLauncher.logger.debug("manipulateAverage: resultStatus: FailB");
				}


			});
		}else{
			resultValueHashMap.entrySet().forEach( kv -> {
				ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getKey-A: "  + kv.getKey() + " -> " + kv.getValue());
				//ApplicationLauncher.logger.debug("manipulateAverage: resultValueHashMap.getValue: "  + kv.getValue());
				float resultValue = Float.parseFloat(kv.getValue());
				//Range<Float> validRange = Range.between(lowerLimitValue, upperLimitValue);
				//boolean inRange = validRange.contains(element)
				ApplicationLauncher.logger.debug("manipulateAverage: resultValue: "  + resultValue);
				//if ( (lowerLimitValue >= resultValue) && (resultValue <= upperLimitValue)) {
				//ApplicationLauncher.logger.debug("manipulateAverage: resultValue: "  + resultValue);
				//ApplicationLauncher.logger.debug("manipulateAverage: upperLimitValue: "  + upperLimitValue);
				ApplicationLauncher.logger.debug("manipulateAverage: lowerLimitValue: "  + lowerLimitValue);
				if (lowerLimitValue <= resultValue) {
					if (resultValue <= upperLimitValue) {
						resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_PASS);
						//resultValueHashMap.put(kv.getKey(), resultValueHashMap.get(kv.getKey()) + " " + ConstantReport.RESULT_STATUS_PASS);
						ApplicationLauncher.logger.debug("manipulateAverage: resultStatus: PassA");
					}else{
						resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
						//resultValueHashMap.put(kv.getKey(), resultValueHashMap.get(kv.getKey()) + " " + ConstantReport.RESULT_STATUS_FAIL);
						ApplicationLauncher.logger.debug("manipulateAverage: resultStatus: FailA-1");
					}
				}else{
					resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
					//resultValueHashMap.put(kv.getKey(), resultValueHashMap.get(kv.getKey()) + " " + ConstantReport.RESULT_STATUS_FAIL);
					ApplicationLauncher.logger.debug("manipulateAverage: resultStatus: FailA-2");
				}

			});

		}
		//}
		//}
		String testType  = filterTestTypeData.getTestTypeAlias();
		if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
			setResultRepeatAverageValueHashMap(resultValueHashMap);
			setResultRepeatAverageStatusHashMap(resultStatusHashMap);
		}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
			setResultSelfHeatAverageValueHashMap(resultValueHashMap);
			setResultSelfHeatAverageStatusHashMap(resultStatusHashMap);
		}
		/*
		Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
				.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				//.filter(e-> (!e.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)))
				.findFirst();

		String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
		if(externalOutputDataKeyOptional.isPresent()){
			OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
			if(operationProcessData.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)){
				ApplicationLauncher.logger.debug("manipulateAverage: NONE_DISPLAYED: " );
			}else{
				externalOutputDataKey = operationProcessData.getOperationProcessKey();
				ApplicationLauncher.logger.debug("manipulateAverage: externalOutputDataKey: " + externalOutputDataKey);
				Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(externalOutputDataKey, 	
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
				if(operationProcessDataOptional.isPresent()){
					ApplicationLauncher.logger.debug("manipulateAverage: setting data on external: " + externalOutputDataKey);
					OperationProcessDataJsonRead operationProcessMasterData = operationProcessDataOptional.get();
					operationProcessMasterData.setResultValueHashMap(resultValueHashMap);
					operationProcessMasterData.setResultStatusHashMap(resultStatusHashMap);
					operationProcessMasterData.setUpperLimit(upperLimitStr);
					operationProcessMasterData.setLowerLimit(lowerLimitStr);
					ApplicationLauncher.logger.debug("manipulateAverage: setting data on external: upperLimitStr: " + upperLimitStr);
					ApplicationLauncher.logger.debug("manipulateAverage: setting data on external: lowerLimitStr: " + lowerLimitStr);
					//setTargetOperationProcessExternalOutputData(operationProcessMasterData);
				}
			}
		}*/

		String internalOutputDataKey = filterTestTypeData.getOperationProcessLocalOutputKey();
		ApplicationLauncher.logger.debug("manipulateAverage: internalOutputDataKey3: "  + internalOutputDataKey);
		if(! internalOutputDataKey.equals(ConstantReportV2.NONE_DISPLAYED)){
			Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(internalOutputDataKey, 	
					ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);

			if(operationProcessDataOptional.isPresent()){
				OperationProcessDataJsonRead operationProcessData = operationProcessDataOptional.get();
				operationProcessData.setResultValueHashMap(resultValueHashMap);
				operationProcessData.setResultStatusHashMap(resultStatusHashMap);
				operationProcessData.setUpperLimit(upperLimitStr);
				operationProcessData.setLowerLimit(lowerLimitStr);
				setTargetOperationProcessInternalOutputData(operationProcessData);

				ApplicationLauncher.logger.debug("manipulateAverage: internalOutputDataKey: "  + internalOutputDataKey);
			}
		}


	}



	private void processBookOutputData(ReportProfileTestDataFilter filterTestTypeData) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: Entry");
		ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		List<String> operationAddedProcessKeyList = filterTestTypeData.getOperationProcessInputKeyList();
		ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: operationAddedProcessKeyList: " + operationAddedProcessKeyList);
		String operationOutputStorageKey = "";

		OperationProcessDataJsonRead operProcessDataJsonRead = null;
		Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataInputOpt =  getOperationProcessData(operationAddedProcessKeyList.get(0), 
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);

		
		Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOutputOpt =  getOperationProcessData(operationAddedProcessKeyList.get(0), 
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);

		if(operationProcessDataJsonReadDataInputOpt.isPresent()){
			operProcessDataJsonRead = operationProcessDataJsonReadDataInputOpt.get();
		}
		
		if(operationProcessDataJsonReadDataOutputOpt.isPresent()){
			operProcessDataJsonRead = operationProcessDataJsonReadDataOutputOpt.get();
		}
		
		//if(operationProcessDataJsonReadDataInputOpt.isPresent()){
		if(operProcessDataJsonRead!=null){	
			ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: operationProcessDataJsonReadDataOpt : present");
			//OperationProcessDataJsonRead operProcessDataJsonInputRead = operationProcessDataJsonReadDataInputOpt.get();
			HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();
			resultValueHashMap = operProcessDataJsonRead.getResultValueHashMap();

			resultValueHashMap.entrySet().forEach( kv -> {
				ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: resultValueHashMap.getKey: "  + kv.getKey());
				ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: resultValueHashMap.getValue: "  + kv.getValue());
			});


			HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
			resultStatusHashMap = operProcessDataJsonRead.getResultStatusHashMap();



			Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
					.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
					//.filter(e-> (!e.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)))
					.findFirst();

			String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
			if(externalOutputDataKeyOptional.isPresent()){
				OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
				if(operationProcessData.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)){
					ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: NONE_DISPLAYED: " );
				}else{
					externalOutputDataKey = operationProcessData.getOperationProcessKey();
					ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: externalOutputDataKey: " + externalOutputDataKey);
					Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(externalOutputDataKey, 	
							ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
					if(operationProcessDataOptional.isPresent()){
						ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: setting data on external: " + externalOutputDataKey);
						OperationProcessDataJsonRead operationProcessMasterData = operationProcessDataOptional.get();
						operationProcessMasterData.setResultValueHashMap(resultValueHashMap);
						operationProcessMasterData.setResultStatusHashMap(resultStatusHashMap);
						String externalKey = externalOutputDataKey;

						Optional<RpPrintPosition> rpPrintPositionOpt = filterTestTypeData.getRpPrintPositionList().stream()
								.filter(e -> e.getKeyParam().equals(externalKey))
								.findFirst();


						if(rpPrintPositionOpt.isPresent()){
							filterTestTypeData.getRpPrintPositionList().stream()
							.filter(e -> e.getKeyParam().equals(externalKey))
							.findFirst()
							.get()
							.setPopulateEachDutAverageValue(true);
						}
						//.map( e -> e.setPopulateEachDutAverageValue(true));
						//operationProcessMasterData.setUpperLimit(upperLimitStr);
						//operationProcessMasterData.setLowerLimit(lowerLimitStr);
						//ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: setting data on external: upperLimitStr: " + upperLimitStr);
						//ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: setting data on external: lowerLimitStr: " + lowerLimitStr);
						setTargetOperationProcessExternalOutputData(operationProcessMasterData);
					}
				}
			}

			/*		String internalOutputDataKey = filterTestTypeData.getOperationProcessLocalOutputKey();
			ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: internalOutputDataKey3: "  + internalOutputDataKey);
			if(! internalOutputDataKey.equals(ConstantReportV2.NONE_DISPLAYED)){
				Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(internalOutputDataKey, 	
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);

				if(operationProcessDataOptional.isPresent()){
					OperationProcessDataJsonRead operationProcessData = operationProcessDataOptional.get();
					operationProcessData.setResultValueHashMap(resultValueHashMap);
					operationProcessData.setResultStatusHashMap(resultStatusHashMap);
					operationProcessData.setUpperLimit(upperLimitStr);
					operationProcessData.setLowerLimit(lowerLimitStr);
					setTargetOperationProcessInternalOutputData(operationProcessData);

					ApplicationLauncher.logger.debug("processMasterAverageOperationMethod: internalOutputDataKey: "  + internalOutputDataKey);
				}
			}*/
		}

	}




	private void processOperationMethod(ReportProfileTestDataFilter filterTestTypeData) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("processOperationMethod: Entry");
		ApplicationLauncher.logger.debug("processOperationMethod: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		List<String> operationAddedProcessKeyList = filterTestTypeData.getOperationProcessInputKeyList();
		ApplicationLauncher.logger.debug("processOperationMethod: operationAddedProcessKeyList: " + operationAddedProcessKeyList);
		String operationOutputStorageKey = "";

		List<OperationProcessDataJsonRead> operationProcessDataJsonDataList = new ArrayList<OperationProcessDataJsonRead>();


		//*********************************************** delete me start ****************#deleteMe
		/*		Optional<OperationProcessDataJsonRead> eachDataOptionalDeleteMe = getOperationProcessData("LocalInputConstDutInitial", 	
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


		if(eachDataOptionalDeleteMe.isPresent()){
			OperationProcessDataJsonRead eachDataOptional = eachDataOptionalDeleteMe.get();
			ApplicationLauncher.logger.debug("processOperationMethod: LocalInputConstDutInitial: getResultValueHashMap: " + eachDataOptional.getResultValueHashMap());
		}

		eachDataOptionalDeleteMe = getOperationProcessData("LocalInputConstDutFinal", 	
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


		if(eachDataOptionalDeleteMe.isPresent()){
			OperationProcessDataJsonRead eachDataOptional = eachDataOptionalDeleteMe.get();
			ApplicationLauncher.logger.debug("processOperationMethod: LocalInputConstDutFinal: getResultValueHashMap: " + eachDataOptional.getResultValueHashMap());
		}*/
		/*
		getOperationProcessDataModel().getOperationLocalOutput().stream()
				.forEach( e-> {
					ApplicationLauncher.logger.debug("processOperationMethod: getOperationLocalOutput: " + e.getOperationProcessKey());
				});*/

		//*********************************************** delete me end ****************#deleteMe

		operationAddedProcessKeyList.stream()
		.forEach( e-> {
			Optional<OperationProcessDataJsonRead> eachDataOptional = getOperationProcessData(e, 	
					ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


			/*							getOperationProcessDataModel().getOperationLocalInput().stream()
										.filter(e1 -> e1.getOperationProcessKey().equals(e))
										.findFirst()
										.get();*/
			if(eachDataOptional.isPresent()){
				operationProcessDataJsonDataList.add(eachDataOptional.get());
			}

			eachDataOptional = getOperationProcessData(e, 	
					ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);
			if(eachDataOptional.isPresent()){
				operationProcessDataJsonDataList.add(eachDataOptional.get());
			}

			eachDataOptional = getOperationProcessData(e, 	
					ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
			if(eachDataOptional.isPresent()){
				operationProcessDataJsonDataList.add(eachDataOptional.get());
			}
		});

		int operationListCount = operationProcessDataJsonDataList.size();
		ApplicationLauncher.logger.debug("processOperationMethod: operationListCount: "  + operationListCount);

		MultiValuedMap<String,String> aggregatedResultValueHashMap = new ArrayListValuedHashMap<String,String>();
		//getDutSerialNumberList().stream().forEach( e -> aggregatedResultValueHashMap.put(e,""));

		//for(OperationProcessDataJsonRead eachData : operationProcessDataJsonDataList){

		/*		operationProcessDataJsonDataList.stream()
				.forEachOrdered( e -> {
					e.getResultValueHashMap().entrySet()
					.forEach( kv-> {
						aggregatedResultValueHashMap.put(kv.getKey(), kv.getValue());
						ApplicationLauncher.logger.debug("processOperationMethod: key3 : " + kv.getKey() + " -> " + kv.getValue());
					});
				});*/

		for(int i=0 ; i < operationProcessDataJsonDataList.size(); i++){

			ApplicationLauncher.logger.debug("processOperationMethod: getOperationProcessKey3 : " + operationProcessDataJsonDataList.get(i).getOperationProcessKey());
			ApplicationLauncher.logger.debug("processOperationMethod: getOperationProcessKey3 : getResultValueHashMap: " + operationProcessDataJsonDataList.get(i).getResultValueHashMap());
			
			if(operationProcessDataJsonDataList.get(i).isPopulateOnlyHeaders()){
				try{
					HashMap<String, String> headersOnlyResultHashMap = operationProcessDataJsonDataList.get(i).getResultValueHashMap();
					String singleResultValueToPopulateOnAllDevice = headersOnlyResultHashMap.entrySet().stream().findFirst().get().getValue();

					HashMap<String, String> newHeadersOnlyResultHashMap = new HashMap<String, String>();


					getDutSerialNumberList().stream().forEachOrdered( e -> {

						ApplicationLauncher.logger.debug("processOperationMethod: HeadersOnly deviceName : " + e + " -> " + singleResultValueToPopulateOnAllDevice); 
						newHeadersOnlyResultHashMap.put(e, singleResultValueToPopulateOnAllDevice);
					});

					if(newHeadersOnlyResultHashMap.size()>0){
						ApplicationLauncher.logger.debug("processOperationMethod: setting setResultValueHashMap" );
						operationProcessDataJsonDataList.get(i).setResultValueHashMap(newHeadersOnlyResultHashMap);
					}
				}catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("processOperationMethod: Exception: " + e.getMessage());
				}

				//for(String deviceName : getDutSerialNumberList()){

				//}

			}

		}

		//ApplicationLauncher.logger.debug("processOperationMethod: operationProcessDataJsonDataList : " + operationProcessDataJsonDataList);
		operationProcessDataJsonDataList.stream()
		.forEachOrdered( e -> {
			e.getResultValueHashMap().entrySet()
			.forEach( kv-> {
				aggregatedResultValueHashMap.put(kv.getKey(), kv.getValue());
				ApplicationLauncher.logger.debug("processOperationMethod: key3 : " + kv.getKey() + " -> " + kv.getValue());
			});
		});


		//}

		aggregatedResultValueHashMap.entries().forEach( kv -> {
			ApplicationLauncher.logger.debug("processOperationMethod: key4 : " + kv.getKey() + " -> " + kv.getValue());
			//ApplicationLauncher.logger.debug("processOperationMethod: kv.getValue: "  + kv.getValue());
			//String[] resultList = (String []) kv.getValue();
		});

		HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();


		aggregatedResultValueHashMap.keySet().forEach(eachHashMapDataKey -> {
			ApplicationLauncher.logger.debug("processOperationMethod: Key5: "  + eachHashMapDataKey + " -> " + aggregatedResultValueHashMap.get(eachHashMapDataKey));
			//ApplicationLauncher.logger.debug("processOperationMethod: Value: "  + aggregatedResultValueHashMap.get(eachHashMapData.getKey()));
			String resultValueStr = "";
			try{
				Collection<String> resultList = aggregatedResultValueHashMap.get(eachHashMapDataKey);
				String operationMethod = filterTestTypeData.getOperationProcessMethod();

				/*			if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_ERROR_PERCENTAGE)){
				resultList.stream().mapToDouble(e -> Double.parseDouble(e))
					.forEachOrdered(e -> {
						ApplicationLauncher.logger.debug("processOperationMethod: Value: "  + e);
					});
			}*/


				double aggregatedResultValue = 0;
				if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_AVERAGE)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.average()
							.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: averageResultValue: "  + aggregatedResultValue);

				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_MAXIMUM)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.max()
							.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: maxResultValue: "  + aggregatedResultValue);


				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_MINIMUM)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.min()
							.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: minResultValue: "  + aggregatedResultValue);

				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_ADD)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.sum();
					//.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: AddResultValue: "  + aggregatedResultValue);

				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_DIFFERENCE)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.reduce(0, (e1,e2) -> (e2-e1));
					//.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: DiffResultValue: "  + aggregatedResultValue);

				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_MULTIPLY)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.reduce(1, (e1,e2) -> (e1*e2));
					//.getAsDouble();
					ApplicationLauncher.logger.debug("processOperationMethod: MultiplyResultValue: "  + aggregatedResultValue);

				}else if(operationMethod.equals(ConstantReportV2.OPERATION_METHOD_ERROR_PERCENTAGE)){
					aggregatedResultValue = resultList.stream().mapToDouble(e -> Double.parseDouble(e))
							.reduce(123456789.0123, (e1,e2) -> {

								ApplicationLauncher.logger.debug("processOperationMethod: Key6: "  + eachHashMapDataKey + " -> " + aggregatedResultValueHashMap.get(eachHashMapDataKey));
								ApplicationLauncher.logger.debug("processOperationMethod: e1: "  + e1);
								ApplicationLauncher.logger.debug("processOperationMethod: e2: "  + e2);
								ApplicationLauncher.logger.debug("processOperationMethod: diff1 (e2-e1): "  + (e2-e1));
								ApplicationLauncher.logger.debug("processOperationMethod: diff2 (e1-e2): "  + (e1-e2));
								double errorPercentage  = 0;
								double diffValue = (e2-e1);//
								if(e1 == 123456789.0123){ // identity case
									ApplicationLauncher.logger.debug("processOperationMethod: identity case : ");
									errorPercentage = e2;
									return errorPercentage;
								}

								if(diffValue == 0){
									errorPercentage = 0.0;
									ApplicationLauncher.logger.debug("processOperationMethod: errorPercentage Zero: ");
								}else{
									/*							 if(e1 == 0){
										 errorPercentage = e2;
										 ApplicationLauncher.logger.debug("processOperationMethod: errorPercentage Initial Case: ");
									 }
									 else{*/
									errorPercentage = ((e2-e1)/e1)*100;//NaN
									//}
									ApplicationLauncher.logger.debug("processOperationMethod: errorPercentage : "+ errorPercentage);
									// errorPercentage = ((e2-e1)/e2)*100;//0 -> -83933.613, 100-> -41916.807 , 50 -> -169391.525

								}
								return errorPercentage;
							} );


					ApplicationLauncher.logger.debug("processOperationMethod: ErrorPercentage ResultValue: "  + aggregatedResultValue);
				}
				/*if(aggregatedResultValue>=0){
				resultValueStr= String.format("+%.03f", aggregatedResultValue);
			}else{
				resultValueStr= String.format("%.03f", aggregatedResultValue);
			}*/
				ApplicationLauncher.logger.debug("processOperationMethod: aggregatedResultValue: "  + aggregatedResultValue);

				boolean postProcessingActive = filterTestTypeData.isOperationProcessPostActive();
				if(postProcessingActive){
					float postProcessingInputValue = Float.parseFloat(filterTestTypeData.getOperationProcessPostInputValue());
					ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: postProcessingInputValue: " + aggregatedResultValue);
					String postProcessSelectedMathOperation = filterTestTypeData.getOperationProcessPostMethod();
					if(postProcessSelectedMathOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_ADD)){
						ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: Add ");
						aggregatedResultValue = aggregatedResultValue + postProcessingInputValue;
					}else if(postProcessSelectedMathOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_SUBTRACT)){
						ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: Subtract" );
						aggregatedResultValue = aggregatedResultValue - postProcessingInputValue;
					}else if(postProcessSelectedMathOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_MULTIPLY)){
						ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: Multiply");
						aggregatedResultValue = aggregatedResultValue * postProcessingInputValue;
					}else if(postProcessSelectedMathOperation.equals(ConstantReportV2.POST_OPERATION_METHOD_DIVIDE)){
						ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: Division" );
						if(postProcessingInputValue == 0){
							ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: aggregatedResultValue value is zero: Division omitted: aggregatedResultValue: " + aggregatedResultValue);
							aggregatedResultValue = 0;
						}else{
							ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: Division: " + aggregatedResultValue);
							aggregatedResultValue = aggregatedResultValue / postProcessingInputValue;
						}

					}
					ApplicationLauncher.logger.debug("processOperationMethod: postProcessingActive: aggregatedResultValue: " + aggregatedResultValue);
				}

				if(aggregatedResultValue>=0){
					resultValueStr= String.format("+%.03f", aggregatedResultValue);
				}else{
					resultValueStr= String.format("%.03f", aggregatedResultValue);
				}

				ApplicationLauncher.logger.debug("processOperationMethod: resultValueStr: "  + resultValueStr);

				resultValueHashMap.put(eachHashMapDataKey, resultValueStr);

			}catch (Exception e){
				//aggregatedResultValue = 0.0;
				resultValueStr = ConstantReport.REPORT_POPULATE_UNDEFINED;
				resultValueHashMap.put(eachHashMapDataKey, resultValueStr);
				e.printStackTrace();
				ApplicationLauncher.logger.error("processOperationMethod: Exception: " + e.getMessage());
			}
		});

		resultValueHashMap.entrySet().forEach( kv -> {
			ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getKey: "  + kv.getKey());
			ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getValue: "  + kv.getValue());
		});

		//ApplicationLauncher.logger.debug("processOperationMethod: TestHit-01");
		HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
		String upperLimitStr = "";
		String lowerLimitStr = "";

		if(filterTestTypeData.isOperationCompareLimitsSelected()){
			getDutSerialNumberList().stream().forEachOrdered( e -> {

				//ApplicationLauncher.logger.debug("processOperationMethod: HeadersOnly deviceName : " + e + " -> " + singleResultValueToPopulateOnAllDevice); 
				resultStatusHashMap.put(e, "");
			});

			upperLimitStr = filterTestTypeData.getOperationProcessLocalUpperLimit();
			lowerLimitStr = filterTestTypeData.getOperationProcessLocalLowerLimit();
			ApplicationLauncher.logger.debug("processOperationMethod: upperLimitStr: " + upperLimitStr);
			ApplicationLauncher.logger.debug("processOperationMethod: lowerLimitStr: " + lowerLimitStr);
			float upperLimitValue = Float.parseFloat(upperLimitStr);
			float lowerLimitValue = Float.parseFloat(lowerLimitStr);
			ApplicationLauncher.logger.debug("processOperationMethod: upperLimitValue: "  + upperLimitValue);
			ApplicationLauncher.logger.debug("processOperationMethod: lowerLimitValue: "  + lowerLimitValue);

			if(upperLimitValue == lowerLimitValue){
				resultValueHashMap.entrySet().forEach( kv -> {
					ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getKeyB: "  + kv.getKey() + " -> " + kv.getValue());
					//ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getValue: "  + kv.getValue());
					float resultValue = Float.parseFloat(kv.getValue());
					//Range<Float> validRange = Range.between(lowerLimitValue, upperLimitValue);
					//boolean inRange = validRange.contains(element)
					//ApplicationLauncher.logger.debug("processOperationMethod: resultValue: "  + resultValue);
					ApplicationLauncher.logger.debug("processOperationMethod: both upper and lower equal: "  + lowerLimitValue);
					//if ( (lowerLimitValue >= resultValue) && (resultValue <= upperLimitValue)) {

					//ApplicationLauncher.logger.debug("processOperationMethod: lowerLimitValue: "  + lowerLimitValue);

					if (resultValue == lowerLimitValue) {
						resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_PASS);
						ApplicationLauncher.logger.debug("processOperationMethod: resultStatus: PassB");
					}else{
						resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
						ApplicationLauncher.logger.debug("processOperationMethod: resultStatus: FailB");
					}


				});
			}else{
				resultValueHashMap.entrySet().forEach( kv -> {
					ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getKey-A: "  + kv.getKey() + " -> " + kv.getValue());
					//ApplicationLauncher.logger.debug("processOperationMethod: resultValueHashMap.getValue: "  + kv.getValue());
					if( ! kv.getValue().equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){

						float resultValue = Float.parseFloat(kv.getValue());
						//Range<Float> validRange = Range.between(lowerLimitValue, upperLimitValue);
						//boolean inRange = validRange.contains(element)
						ApplicationLauncher.logger.debug("processOperationMethod: resultValue: "  + resultValue);
						//if ( (lowerLimitValue >= resultValue) && (resultValue <= upperLimitValue)) {

						//ApplicationLauncher.logger.debug("processOperationMethod: lowerLimitValue: "  + lowerLimitValue);
						if (lowerLimitValue <= resultValue) {
							if (resultValue <= upperLimitValue) {
								resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_PASS);
								ApplicationLauncher.logger.debug("processOperationMethod: resultStatus: PassA");
							}else{
								resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
								ApplicationLauncher.logger.debug("processOperationMethod: resultStatus: FailA-1");
							}
						}else{
							resultStatusHashMap.put(kv.getKey(), ConstantReport.RESULT_STATUS_FAIL);
							ApplicationLauncher.logger.debug("processOperationMethod: resultStatus: FailA-2");
						}
					}else{
						resultStatusHashMap.put(kv.getKey(), ConstantReport.REPORT_POPULATE_UNDEFINED);
						ApplicationLauncher.logger.debug("processOperationMethod: resultStatus2: Error");
					}

				});

			}
		}

		//ApplicationLauncher.logger.debug("processOperationMethod: TestHit-02");

		Optional<OperationProcess> externalOutputDataKeyOptional = filterTestTypeData.getOperationProcessDataList().stream()
				.filter(e-> e.getOperationProcessDataType().equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT))
				//.filter(e-> (!e.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)))
				.findFirst();

		String externalOutputDataKey = ConstantReportV2.NONE_DISPLAYED;
		if(externalOutputDataKeyOptional.isPresent()){
			OperationProcess operationProcessData = externalOutputDataKeyOptional.get();
			if(operationProcessData.getOperationProcessKey().equals(ConstantReportV2.NONE_DISPLAYED)){
				ApplicationLauncher.logger.debug("processOperationMethod: NONE_DISPLAYED: " );
			}else{
				externalOutputDataKey = operationProcessData.getOperationProcessKey();
				ApplicationLauncher.logger.debug("processOperationMethod: externalOutputDataKey: " + externalOutputDataKey);
				Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(externalOutputDataKey, 	
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT);
				if(operationProcessDataOptional.isPresent()){
					ApplicationLauncher.logger.debug("processOperationMethod: setting data on external: " + externalOutputDataKey);
					OperationProcessDataJsonRead operationProcessMasterData = operationProcessDataOptional.get();
					operationProcessMasterData.setResultValueHashMap(resultValueHashMap);
					operationProcessMasterData.setResultStatusHashMap(resultStatusHashMap);
					operationProcessMasterData.setUpperLimit(upperLimitStr);
					operationProcessMasterData.setLowerLimit(lowerLimitStr);
					ApplicationLauncher.logger.debug("processOperationMethod: setting data on external: upperLimitStr: " + upperLimitStr);
					ApplicationLauncher.logger.debug("processOperationMethod: setting data on external: lowerLimitStr: " + lowerLimitStr);
					//setTargetOperationProcessExternalOutputData(operationProcessMasterData);
				}
			}
		}

		//ApplicationLauncher.logger.debug("processOperationMethod: TestHit-03");
		String internalOutputDataKey = filterTestTypeData.getOperationProcessLocalOutputKey();
		ApplicationLauncher.logger.debug("processOperationMethod: internalOutputDataKey3: "  + internalOutputDataKey);
		if(! internalOutputDataKey.equals(ConstantReportV2.NONE_DISPLAYED)){
			Optional<OperationProcessDataJsonRead> operationProcessDataOptional = getOperationProcessData(internalOutputDataKey, 	
					ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT);

			if(operationProcessDataOptional.isPresent()){
				ApplicationLauncher.logger.debug("processOperationMethod: internalOutputDataKey: "  + internalOutputDataKey);
				OperationProcessDataJsonRead operationProcessData = operationProcessDataOptional.get();
				operationProcessData.setResultValueHashMap(resultValueHashMap);
				operationProcessData.setResultStatusHashMap(resultStatusHashMap);
				operationProcessData.setUpperLimit(upperLimitStr);
				operationProcessData.setLowerLimit(lowerLimitStr);
				setTargetOperationProcessInternalOutputData(operationProcessData);


			}else{
				//added for Constant Test Error Calculation
				operationProcessDataOptional = getOperationProcessData(internalOutputDataKey, 	
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);
				if(operationProcessDataOptional.isPresent()){
					ApplicationLauncher.logger.debug("processOperationMethod: internalOutputDataKey2: "  + internalOutputDataKey);
					OperationProcessDataJsonRead operationProcessData = operationProcessDataOptional.get();
					operationProcessData.setResultValueHashMap(resultValueHashMap);
					operationProcessData.setResultStatusHashMap(resultStatusHashMap);
					operationProcessData.setUpperLimit(upperLimitStr);
					operationProcessData.setLowerLimit(lowerLimitStr);
					ApplicationLauncher.logger.debug("processOperationMethod: getOperationProcessKey2: "  + operationProcessData.getOperationProcessKey());
					operationProcessData.setOperationProcessKey(internalOutputDataKey);
					getOperationProcessDataModel().getOperationLocalOutput().add(operationProcessData);
					setTargetOperationProcessInternalOutputData(operationProcessData);


				}
			}
		}

		ApplicationLauncher.logger.debug("processOperationMethod: Exit");
	}

	public Optional<OperationProcessDataJsonRead> getOperationProcessData(String searchKey, String dataType){

		Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  null;//new OperationProcessDataJsonRead ();

		//Optional<OperationParam> operationParamOpt = null ;//getOperationParameterProfileDataList() {
		//operationProcessDataJsonReadData = 
		try{
			ApplicationLauncher.logger.debug("getOperationProcessData: searchKey: " + searchKey + " , dataType : " + dataType);
			//ApplicationLauncher.logger.debug("manipulateOutputProcessDataOnReport: dataType: " + dataType);
			if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT)){
				operationProcessDataJsonReadDataOpt =  getOperationProcessDataModel().getOperationLocalInput().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();

				if(operationProcessDataJsonReadDataOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationProcessData: result l-input found");
				}
			}else if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT)){
				operationProcessDataJsonReadDataOpt =  getOperationProcessDataModel().getOperationLocalOutput().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();
				if(operationProcessDataJsonReadDataOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationProcessData: result l-output found");
				}
			}else if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT)){
				operationProcessDataJsonReadDataOpt =  getOperationProcessDataModel().getOperationMasterOutput().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();
				if(operationProcessDataJsonReadDataOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationProcessData: result m-output found");
				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			operationProcessDataJsonReadDataOpt= null;
			e.printStackTrace();
			ApplicationLauncher.logger.error("getOperationProcessData: Exception: " + e.getMessage());
		}	

		return operationProcessDataJsonReadDataOpt;
	}

	/*	public Optional<OperationParam> getOperationParamProfileProcessData(String searchKey, String dataType){

		//Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  null;//new OperationProcessDataJsonRead ();

		Optional<OperationParam> operationParamOpt = null ;//getOperationParameterProfileDataList() {
		//operationProcessDataJsonReadData = 
		try{
			ApplicationLauncher.logger.debug("getOperationParamProfileProcessData: searchKey: " + searchKey + " , dataType : " + dataType);
			//ApplicationLauncher.logger.debug("manipulateOutputProcessDataOnReport: dataType: " + dataType);
			if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT)){
				operationParamOpt =  getOperationParameterProfileDataList().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();

				if(operationParamOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationParamProfileProcessData: result l-input found");
				}
			}else if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_OUTPUT)){
				operationParamOpt =  getOperationParameterProfileDataList().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();
				if(operationParamOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationParamProfileProcessData: result l-output found");
				}
			}else if(dataType.equals(ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_MASTER_OUTPUT)){
				operationParamOpt =  getOperationParameterProfileDataList().stream()
						.filter(e -> e.getOperationProcessKey().equals(searchKey))
						.findFirst();
				if(operationParamOpt.isPresent()){
					ApplicationLauncher.logger.debug("getOperationParamProfileProcessData: result m-output found");
				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			operationParamOpt= null;
			e.printStackTrace();
			ApplicationLauncher.logger.error("getOperationParamProfileProcessData: Exception: " + e.getMessage());
		}	

		return operationParamOpt;
	}
	 */

	private boolean populateDutMetaDataOnReport(XSSFSheet sheet1,ReportProfileMeterMetaDataFilter meterDataDisplayPage,
			int serialNoMaxCount, int meter_col) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: Entry");
		//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		boolean status = false;

		//boolean populateMeterProfileDataforEachMeter = false;
		//String dataStartingCellPosition = "";
		int populateMeterProfileDataIterationCount = serialNoMaxCount;
		boolean populateMeterProfileDataforEachMeter = meterDataDisplayPage.isPopulateForEachDut();
		//populateMeterProfileDataIterationCount = serialNoMaxCount;	
		String dataStartingCellPosition = meterDataDisplayPage.getCellPosition();
		//JSONObject dutModelData = getMeterProfileData();
		String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
		if(!populateMeterProfileDataforEachMeter){
			populateMeterProfileDataIterationCount = 1;
			//setResultPrintStyle();
			userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
			//manipulatePrintStyle(userSelectedPrintStyleName);
			/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){

				PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
							.filter(e -> e.getReportPrintStyleName().equals(userSelectedPrintStyleName))
							.findFirst()
							.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

				setResultPrintStyle(resultPrintStyle);
			}*/
		}
		//else{
		//userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
		//manipulatePrintStyle(userSelectedPrintStyleName);
		/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){

							PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
										.filter(e -> e.getReportPrintStyleName().equals(userSelectedPrintStyleName))
										.findFirst()
										.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

							setResultPrintStyle(resultPrintStyle);
			}*/
		//}

		manipulatePrintStyle(userSelectedPrintStyleName);

		boolean populateResult = meterDataDisplayPage.isPopulateSerialNo();


		if(populateResult){
			fillSerialNoColumnXSSF(sheet1, serialNoMaxCount, dataStartingCellPosition);
		}


		populateResult = meterDataDisplayPage.isPopulateMake();
		if(populateResult){


			populateMeterProfileDataIterationCount = meter_col;

			fillMakeColumnXSSF( sheet1, dataStartingCellPosition, populateMeterProfileDataIterationCount);
		}

		populateResult = meterDataDisplayPage.isPopulateModelNo();
		if(populateResult){


			populateMeterProfileDataIterationCount = meter_col;
			if(populateMeterProfileDataIterationCount ==1){

			}else{
				fillModelNoColumnXSSF( sheet1, dataStartingCellPosition, populateMeterProfileDataIterationCount);
			}
		}

		populateResult = meterDataDisplayPage.isPopulateCustomerRefNo();
		if(populateResult){

			fillCustomerReferenceNoColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
		}


		populateResult = meterDataDisplayPage.isPopulateCapacity();
		if(populateResult){

			fillCapacityColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
		}

		populateResult = meterDataDisplayPage.isPopulateMeterType();
		if(populateResult){

			fillMeterTypeColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);

		}

		populateResult = meterDataDisplayPage.isPopulateMeterConstant();
		if(populateResult){

			if(populateMeterProfileDataforEachMeter){
				fillMeterConstantColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
			}else{

				fillMeterConstantFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
			}
		}


		populateResult = meterDataDisplayPage.isPopulatePtRatio();
		if(populateResult){

			if(populateMeterProfileDataforEachMeter){

				fillPtRatioColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
			}else{

				fillPtRatioFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
			}
		}


		populateResult = meterDataDisplayPage.isPopulateCtRatio();
		if(populateResult){

			if(populateMeterProfileDataforEachMeter){

				fillCtRatioColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
			}else{

				fillCtRatioFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
			}
		}


		populateResult = meterDataDisplayPage.isPopulateRackPositionNo();
		if(populateResult){

			if(populateMeterProfileDataforEachMeter){
				fillRackPositionColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
				//fillRackPositionColumnXSSF(sheet1, serialNoMaxCount, dataStartingCellPosition);
				//fillCtRatioColumnXSSF( sheet1,dataStartingCellPosition,meter_col,  populateMeterProfileDataIterationCount );
			}else{

				//fillCtRatioFromMeterProfileColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition);
			}
		}


		populateResult = meterDataDisplayPage.isPopulateDutClass();
		if(populateResult){
			String dutMetaDataParser = "model_class";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateDutBasicCurrent();
		if(populateResult){

			String dutMetaDataParser = "basic_current_ib";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateDutMaxCurrent();
		if(populateResult){

			String dutMetaDataParser = "max_current_imax";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateDutRatedVolt();
		if(populateResult){

			String dutMetaDataParser = "rated_voltage_vd";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateDutFreq();
		if(populateResult){

			String dutMetaDataParser = "frequency";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateDutCtType();
		if(populateResult){

			String dutMetaDataParser = "ct_type";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateCustomerName();
		if(populateResult){

			String dutMetaDataParser = "customer_name";
			fillDutMetaDataWithParserColumnXSSF( sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,dutMetaDataParser);

		}

		populateResult = meterDataDisplayPage.isPopulateLoraId();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateLoraId: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateExecutedTimeStamp();
		if(populateResult){

			String formattedDateTimeStamp = TestReportController.getSelectedProjectExecutedDate() + " "+ TestReportController.getSelectedProjectExecutedTime();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateExecutedTimeStamp: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateExecutedDate();
		if(populateResult){
			String formattedDateTimeStamp = TestReportController.getSelectedProjectExecutedDate();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateExecutedDate: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateExecutedTime();
		if(populateResult){
			String formattedDateTimeStamp = TestReportController.getSelectedProjectExecutedTime();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateExecutedTime: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateReportGenTimeStamp();
		if(populateResult){
			Date date = new Date();
			SimpleDateFormat reportGeneratedDateFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_DATE_FORMAT + " " +ConstantAppConfig.REPORT_TIME_FORMAT);

			reportGeneratedDateFormat.setTimeZone(TimeZone.getTimeZone(ConstantAppConfig.REPORT_TIME_ZONE));
			String formattedDateTimeStamp = reportGeneratedDateFormat.format(date);//reportExecutedDateFormat.parse();

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateReportGenTimeStamp: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateReportGenDate();
		if(populateResult){
			Date date = new Date();
			SimpleDateFormat reportGeneratedDateFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_DATE_FORMAT );

			reportGeneratedDateFormat.setTimeZone(TimeZone.getTimeZone(ConstantAppConfig.REPORT_TIME_ZONE));
			String formattedDateTimeStamp = reportGeneratedDateFormat.format(date);//reportExecutedDateFormat.parse();

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateReportGenDate: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateReportGenTime();
		if(populateResult){
			Date date = new Date();
			SimpleDateFormat reportGeneratedDateFormat = new SimpleDateFormat(ConstantAppConfig.REPORT_TIME_FORMAT);

			reportGeneratedDateFormat.setTimeZone(TimeZone.getTimeZone(ConstantAppConfig.REPORT_TIME_ZONE));
			String formattedDateTimeStamp = reportGeneratedDateFormat.format(date);//reportExecutedDateFormat.parse();

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,formattedDateTimeStamp);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateReportGenTime: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateApprovedTimeStamp();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateApprovedTimeStamp: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateApprovedDate();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateApprovedDate: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateApprovedTime();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateApprovedTime: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateTestedBy();
		if(populateResult){
			String testedByUserName = TestReportController.getSelectedExecutionTesterName();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,testedByUserName);
			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateTestedBy: Not Implemented");hgj
		}

		populateResult = meterDataDisplayPage.isPopulateWitnessedBy();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateWitnessedBy: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateApprovedBy();
		if(populateResult){

			ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateApprovedBy: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulatePageNo();
		if(populateResult){
			String pageNumber = ConstantReportV2.PAGE_NO_PREFIX + String.valueOf(getPresentPageNumber());

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,pageNumber);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulatePageNo: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateMaxNoOfPages();
		if(populateResult){


			String maxPageNumber = ConstantReportV2.PAGE_NO_PREFIX + String.valueOf(getMaxReportPageNumber());

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,maxPageNumber);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateMaxNoOfPages: Not Implemented");
		}


		populateResult = meterDataDisplayPage.isPopulatePageNoWithMaxNoOfPages();
		if(populateResult){
			String pageNumberWithMax = ConstantReportV2.PAGE_NO_PREFIX + String.valueOf(getPresentPageNumber()) + " / " + String.valueOf(getMaxReportPageNumber());;

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,pageNumberWithMax);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulatePageNoWithMaxNoOfPages: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateEnergyFlowMode();
		if(populateResult){


			String energyFlowMode = TestReportController.getSelectedEnergyFlowMode();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,energyFlowMode);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateEnergyFlowMode: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateExecutionCtMode();
		if(populateResult){

			boolean mainCtMode = TestReportController.isSelectedExecutionMainCt();
			boolean neutralCtMode = TestReportController.isSelectedExecutionNeutralCt();

			String ctSelectionMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;
			if(mainCtMode && neutralCtMode){
				ctSelectionMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT + " / " + ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT;
			}else if(mainCtMode ){
				ctSelectionMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT ;//+ "/" + ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT;
			}else if(neutralCtMode ){
				ctSelectionMode = ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT;
			}


			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,ctSelectionMode);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateExecutionCtMode: Not Implemented");
		}

		populateResult = meterDataDisplayPage.isPopulateActiveReactiveEnergy();
		if(populateResult){


			// ""hgf;//TestReportController.getSelectedEnergyFlowMode();
			String dutMetaDataParser = "model_type";
			String activeReactiveEnergy = getDutMetaDataActiveReactiveEnergy( dutMetaDataParser);

			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,activeReactiveEnergy);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateEnergyFlowMode: Not Implemented");
		}

		/*		populateResult = meterDataDisplayPage.isPopulateComplyStatus();
		if(populateResult){


			String complyStatus = getComplyStatus();//"TestComply";//TestReportController.getSelectedEnergyFlowMode();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,complyStatus);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateEnergyFlowMode: Not Implemented");
		}*/

		return status;

	}


	private boolean populateComplyStatusOnReport(XSSFSheet sheet1,ReportProfileMeterMetaDataFilter meterDataDisplayPage,
			int serialNoMaxCount) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateComplyStatusOnReport: Entry");
		//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		boolean status = false;

		//boolean populateMeterProfileDataforEachMeter = false;
		//String dataStartingCellPosition = "";
		int populateMeterProfileDataIterationCount = serialNoMaxCount;
		boolean populateMeterProfileDataforEachMeter = meterDataDisplayPage.isPopulateForEachDut();
		//populateMeterProfileDataIterationCount = serialNoMaxCount;	
		String dataStartingCellPosition = meterDataDisplayPage.getCellPosition();
		//JSONObject dutModelData = getMeterProfileData();
		String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
		if(!populateMeterProfileDataforEachMeter){
			populateMeterProfileDataIterationCount = 1;
			userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";

		}

		manipulatePrintStyle(userSelectedPrintStyleName);

		boolean populateResult =  meterDataDisplayPage.isPopulateComplyStatus();
		if(populateResult){


			String complyStatus = getComplyStatus();//"TestComply";//TestReportController.getSelectedEnergyFlowMode();
			fillDutMetaDataWithValueColumnXSSF(sheet1, populateMeterProfileDataIterationCount, dataStartingCellPosition,complyStatus);

			//ApplicationLauncher.logger.debug("populateDutMetaDataOnReport: isPopulateEnergyFlowMode: Not Implemented");
		}

		return status;

	}


	private boolean populateResultDataOnReport(XSSFSheet sheet1,int meter_col ,
			ReportProfileTestDataFilter filterTestTypeData,JSONObject inputFilteredResult,
			String testType) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateResultDataOnReport: Entry");
		ApplicationLauncher.logger.debug("populateResultDataOnReport: testType: " + testType);
		ApplicationLauncher.logger.debug("populateResultDataOnReport: getTestFilterName: " + filterTestTypeData.getTestFilterName());
		boolean status = false;
		String populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
		RpPrintPosition rpPrintPositionData = new RpPrintPosition();
		String dataStartingErrorValueCellPosition = "";
		int row_pos = 0;
		int column_pos = 0;
		String testFilterPopulateType = "";
		String voltageFilter = "";
		String currentFilter =  "";
		String pfFilter =  "";
		//JSONObject result = inputResult;///null;
		boolean populateDataValue = false;
		String headerValue = "";
		List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();
		String testFilterPreview = filterTestTypeData.getFilterPreview();
		if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
			clearRepeatAggregatedAverageProcessData();
			//getRepeatAverageOperationProcessData().setOperationProcessKey(ConstantReportV2.PARAM_PROFILE_REPEAT_AVERAGE_KEY);

		}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
			//getSelfHeatAverageOperationProcessData().setOperationProcessKey(ConstantReportV2.PARAM_PROFILE_SELF_HEAT_AVERAGE_KEY);
			clearSelfHeatAggregatedAverageProcessData();
		}else if(testType.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){


			//String testFilterPreview = filterTestTypeData.getFilterPreview();
			getConstantTestDutInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
			getConstantTestDutFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
			getConstantTestDutDiffAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
			getConstantTestDutErrorPercentAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
			getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);
			getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, false);


		}else {
			//getRepeatAverageOperationProcessData().setOperationProcessKey("");
			//getSelfHeatAverageOperationProcessData().setOperationProcessKey("");

			setRepeatAggregatedAverageProcessData(null);
			setSelfHeatAggregatedAverageProcessData(null);
		}



		//float rsmInitialValue = 0.0f;
		//float rsmFinalValue = 0.0f;


		for(int i=0; i < rpPrintPositionDataList.size(); i++){
			try{
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){
					PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
								.filter(e -> e.getReportPrintStyleName().equals(userSelectedPrintStyleName))
								.findFirst()
								.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

					setResultPrintStyle(resultPrintStyle);
				}*/

				rpPrintPositionData = rpPrintPositionDataList.get(i);
				dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();//.getCellStartResultValuePosition();
				ApplicationLauncher.logger.debug("populateResultDataOnReport: dataStartingErrorValueCellPosition: "+dataStartingErrorValueCellPosition);
				row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);//ConstantReport.ACC_TEMPL_ROW.get(0);
				column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
				ApplicationLauncher.logger.info("populateResultDataOnReport: row_pos: "+row_pos);
				ApplicationLauncher.logger.info("populateResultDataOnReport: isPopulateHeaderTestPeriodInMinutes: "+rpPrintPositionData.isPopulateHeaderTestPeriodInMinutes());


				testFilterPopulateType = filterTestTypeData.getTestFilterDataPopulateType();//.getPopulateType();
				voltageFilter = filterTestTypeData.getVoltPercentFilterData();//.getVoltageFilter();
				currentFilter = filterTestTypeData.getCurrentPercentFilterData();//getCurrentFilter();
				pfFilter = filterTestTypeData.getPfFilterData();//.getPfFilter();

				if(OPERATION_PROCESS_ENABLED){
					String operationMode = filterTestTypeData.getOperationMode();
					if(operationMode.equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT)){
						String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();

						Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
								ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


						if(operationProcessDataJsonReadDataOpt.isPresent()){
							setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
						}else{
							setTargetOperationProcessInternalInputData(null);
						}

					}else{
						setTargetOperationProcessInternalInputData(null);
					}





				}
				if(rpPrintPositionData.isPopulateResultValue()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateResultValue");
					//result = FilterResultByTestType(inputResult, ConstantApp.ACCURACY_ALIAS_NAME);
					//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
					if(testFilterPopulateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
						if(testType.equals(ConstantApp.ACCURACY_ALIAS_NAME)){
							fillACCErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);
						}else if(testType.equals(ConstantApp.CREEP_ALIAS_NAME)){
							String userSelectedExecutionResultType = filterTestTypeData.getTestExecutionResultTypeSelected();
							populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(userSelectedExecutionResultType);
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : userSelectedExecutionResultType : " + userSelectedExecutionResultType);
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : populateResultFilterDataType : " + populateResultFilterDataType);
							if(rpPrintPositionData.isPopulateEachDutPulseCount()){
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep: pulse Count value print");
								String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);
								
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep: userSelectedResultSourceType:" +userSelectedResultSourceType);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep: populateResultFilterDataType:" +populateResultFilterDataType);
								//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : userSelectedResultSourceType : " + userSelectedResultSourceType);
								//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : populateResultFilterDataType2 : " + populateResultFilterDataType);
							}

							fillCreepErrorValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter, 
									column_pos,row_pos, meter_col);
						}else if(testType.equals(ConstantApp.STA_ALIAS_NAME)){
							String userSelectedExecutionResultType = filterTestTypeData.getTestExecutionResultTypeSelected();
							populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(userSelectedExecutionResultType);
							/*if(userSelectedExecutionResultType.equals(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_PULSE_COUNT)){
								ApplicationLauncher.logger.info("populateResultDataOnReport: STA - Pulse Count1" );
								populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
							}else if(userSelectedExecutionResultType.equals(ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_ONE_PULSE_DURATION)){
								ApplicationLauncher.logger.info("populateResultDataOnReport: STA - One Pulse duration" );
								populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_STA_TIME;
							}else{
								ApplicationLauncher.logger.info("populateResultDataOnReport: STA default - Pulse Count2" );
								populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
							}*/

							
							if(rpPrintPositionData.isPopulateEachDutPulseCount()){
								ApplicationLauncher.logger.debug("populateResultDataOnReport: STA: pulse Count value print");
								String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: STA: userSelectedResultSourceType:" +userSelectedResultSourceType);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: STA: populateResultFilterDataType:" +populateResultFilterDataType);
								
								//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : userSelectedResultSourceType : " + userSelectedResultSourceType);
								//ApplicationLauncher.logger.debug("populateResultDataOnReport: Creep : populateResultFilterDataType2 : " + populateResultFilterDataType);
							}


							//populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(userSelectedExecutionResultType);
							fillSTA_ErrorValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter, currentFilter, 
									column_pos, row_pos,meter_col);
						}else if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
							String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
							if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
								resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_START_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY, "");	
							}
							ApplicationLauncher.logger.info("populateResultDataOnReport: Repeat resultIterationId:" + resultIterationId );

							//String testFilterPreview = filterTestTypeData.getFilterPreview();
							//String testFilterPreviewWithIterationId = filterTestTypeData.getFilterPreview();
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: repeat testFilterPreview: " + testFilterPreviewWithIterationId);
							//String testPointFilterName = getTestPointFilterData(testCasePreview);
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: repeat testFilterPreview: " + testFilterPreview);
							String maxErrorAllowedKey = "max_error_allowed";
							String minErrorAllowedKey = "min_error_allowed";


							/*String [] splitTestCaseName;
							if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)){
								splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
								testFilterPreviewWithIterationId = splitTestCaseName[0]+ ConstantReport.EXTENSION_TYPE_CURRENT_IB + 
										ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
							}else if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)){
								splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
								testFilterPreviewWithIterationId = splitTestCaseName[0]+ConstantReport.EXTENSION_TYPE_CURRENT_IMAX + 
										ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
							}

							*/
							
													
							String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  filterTestTypeData.getFilterPreview());
							ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview2: " + testFilterPreviewWithIterationId);
							
							
							String maxErrorAllowed =  getDataFromDeployedTestPoint(maxErrorAllowedKey,testFilterPreviewWithIterationId);
							String minErrorAllowed =  getDataFromDeployedTestPoint(minErrorAllowedKey,testFilterPreviewWithIterationId);
							ApplicationLauncher.logger.debug("populateResultDataOnReport: minErrorAllowed: " + minErrorAllowed);
							ApplicationLauncher.logger.debug("populateResultDataOnReport: maxErrorAllowed: " + maxErrorAllowed);

							getRepeatAverageMaxErrorAllowedHashMap().put(testFilterPreview, maxErrorAllowed);
							getRepeatAverageMinErrorAllowedHashMap().put(testFilterPreview, minErrorAllowed);

							//ApplicationLauncher.logger.debug("populateResultDataOnReport: getRepeatAverageMaxErrorAllowedHashMap() : " + getRepeatAverageMaxErrorAllowedHashMap());

							fillSelfHeatRepeatabilityErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col, resultIterationId);
						}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
							String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
							if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
								resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_START_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY, "");
							}
							ApplicationLauncher.logger.info("populateResultDataOnReport: Self Heat resultIterationId:" + resultIterationId );

							//String testFilterPreview = filterTestTypeData.getFilterPreview();
							//String testFilterPreviewWithIterationId = filterTestTypeData.getFilterPreview();
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: Self Heat testFilterPreview: " + testFilterPreviewWithIterationId);
							//String testPointFilterName = getTestPointFilterData(testCasePreview);
							//ApplicationLauncher.logger.debug("populateResultDataOnReport: repeat testFilterPreview: " + testFilterPreview);
							String maxErrorAllowedKey = "max_error_allowed";
							String minErrorAllowedKey = "min_error_allowed";


							/*String [] splitTestCaseName;
							if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)){
								splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
								testFilterPreviewWithIterationId = splitTestCaseName[0]+ ConstantReport.EXTENSION_TYPE_CURRENT_IB + 
										ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
							}else if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)){
								splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
								testFilterPreviewWithIterationId = splitTestCaseName[0]+ConstantReport.EXTENSION_TYPE_CURRENT_IMAX + 
										ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
							}*/
							String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  filterTestTypeData.getFilterPreview());
							
							ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview2: " + testFilterPreviewWithIterationId);
							String maxErrorAllowed =  getDataFromDeployedTestPoint(maxErrorAllowedKey,testFilterPreviewWithIterationId);
							String minErrorAllowed =  getDataFromDeployedTestPoint(minErrorAllowedKey,testFilterPreviewWithIterationId);
							ApplicationLauncher.logger.debug("populateResultDataOnReport: minErrorAllowed: " + minErrorAllowed);
							ApplicationLauncher.logger.debug("populateResultDataOnReport: maxErrorAllowed: " + maxErrorAllowed);

							getSelfHeatAverageMaxErrorAllowedHashMap().put(testFilterPreview, maxErrorAllowed);
							getSelfHeatAverageMinErrorAllowedHashMap().put(testFilterPreview, minErrorAllowed);


							fillSelfHeatRepeatabilityErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col, resultIterationId);
						}else if(testType.equals(ConstantApp.VOLTAGE_ALIAS_NAME)){
							fillVoltageVariationErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);
						}else if(testType.equals(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME)){
							fillVoltUnbalanceErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);

						}else if(testType.equals(ConstantApp.FREQUENCY_ALIAS_NAME)){
							String freqFilter = filterTestTypeData.getFreqFilterData();
							fillFreqTestErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col, freqFilter);
						}else if(testType.equals(ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME)){
							fillRpsNormalErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);
						}else if(testType.equals(ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME)){
							fillRpsReverseErrorValueCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);
						}else if(testType.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){
							//populateResultFilterDataType = ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT;
							String energyFilter = filterTestTypeData.getEnergyFilterData();

							populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(filterTestTypeData.getTestExecutionResultTypeSelected());
							ApplicationLauncher.logger.debug("populateResultDataOnReport: getTestExecutionResultTypeSelected: " + filterTestTypeData.getTestExecutionResultTypeSelected());
							ApplicationLauncher.logger.debug("populateResultDataOnReport: populateResultFilterDataType: " + populateResultFilterDataType);






							if(rpPrintPositionData.isPopulateEachDutInitialRegister()){

								populateResultFilterDataType = dataSetupForConstantTestDutInitial(populateResultFilterDataType,testFilterPreview,rpPrintPositionData);
								/*							ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant: dut Initial: getKeyParam : " + rpPrintPositionData.getKeyParam());
								getConstantTestDutInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
								populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
								String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);

								String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY;
								Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
										ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


								if(operationProcessDataJsonReadDataOpt.isPresent()){
									setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
								}else{
									setTargetOperationProcessInternalInputData(null);
								}*/


							}else if(rpPrintPositionData.isPopulateEachDutFinalRegister()){

								populateResultFilterDataType = dataSetupForConstantTestDutFinal(populateResultFilterDataType,testFilterPreview,rpPrintPositionData);
								/*
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant: dut final getKeyParam: " + rpPrintPositionData.getKeyParam());
								getConstantTestDutFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
								populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
								String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);

								String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY;
								Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
										ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


								if(operationProcessDataJsonReadDataOpt.isPresent()){
									setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
								}else{
									setTargetOperationProcessInternalInputData(null);
								}

								String maxErrorAllowedKey = "max_error_allowed";
								String minErrorAllowedKey = "min_error_allowed";



								//ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview2: " + testFilterPreviewWithIterationId);
								String maxErrorAllowed =  getDataFromDeployedTestPoint(maxErrorAllowedKey,testFilterPreview);//WithIterationId);
								String minErrorAllowed =  getDataFromDeployedTestPoint(minErrorAllowedKey,testFilterPreview);//WithIterationId);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant minErrorAllowed: " + minErrorAllowed);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant maxErrorAllowed: " + maxErrorAllowed);

								getConstantTestMaxErrorAllowedHashMap().put(testFilterPreview, maxErrorAllowed);
								getConstantTestMinErrorAllowedHashMap().put(testFilterPreview, minErrorAllowed);
								 */
							}else if(rpPrintPositionData.isPopulateEachDutPulseCount()){
								//setTargetOperationProcessInternalInputData(null);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant: dut pulse count getKeyParam: " + rpPrintPositionData.getKeyParam());
								String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);

								String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();//ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY;
								ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant: operationStorageKey: " + operationStorageKey);
								Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
										ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


								if(operationProcessDataJsonReadDataOpt.isPresent()){
									ApplicationLauncher.logger.debug("populateResultDataOnReport: Constant: dut pulse count hit1");
									setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
								}else{
									setTargetOperationProcessInternalInputData(null);
								}
								//populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(rpPrintPositionData.getKeyParam());
							}

							if(testFilterPopulateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){

								/*if( singleCellData ){

									fillConstRefStValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
											row_pos, column_pos);
									status = true;
								}else{*/

								fillConstErrorValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
										row_pos, column_pos, meter_col);
								//	status = true;
								//}
							}

						}else if(testType.equals(ConstantApp.CUSTOM_TEST_ALIAS_NAME)){

							ApplicationLauncher.logger.debug("populateResultDataOnReport: Custom Test");
							fillCustomTestErrorValueCustomReportXSSF(sheet1,inputFilteredResult, 
									column_pos,row_pos, meter_col);

						}/*else if(testType.equals(ConstantApp.VOLTAGE_ALIAS_NAME)){

						}else if(testType.equals(ConstantApp.VOLTAGE_ALIAS_NAME)){

						}*/






						status = true;
					}
				}else if(rpPrintPositionData.isPopulateLocalResultStatus()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateLocalResultStatus");
					//result = FilterResultByTestType(inputResult, ConstantApp.ACCURACY_ALIAS_NAME);
					//fillACCErrorValueXSSF(sheet1, result, voltage,currents,pfs.get(j), j, row_pos,meter_col);
					if(testFilterPopulateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){
						if(testType.equals(ConstantApp.ACCURACY_ALIAS_NAME)){
							fillAccStatusCustomReportXSSF( sheet1, inputFilteredResult,
									voltageFilter, currentFilter, pfFilter, column_pos,
									row_pos, meter_col);
						} else if(testType.equals(ConstantApp.CREEP_ALIAS_NAME)){
							fillCreepResultStatusCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT,voltageFilter, 
									column_pos,row_pos, meter_col);
						}else if(testType.equals(ConstantApp.STA_ALIAS_NAME)){
							fillSTA_ResultStatusCustomReportXSSF(sheet1,ConstantReport.RESULT_DATA_TYPE_STA_TIME,voltageFilter, currentFilter, 
									column_pos, row_pos, meter_col);
						}
						status = true;
					}
				}else if(rpPrintPositionData.isPopulateHeader1()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeader1");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){

						PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
									.filter(e -> e.getReportPrintStyleName().equals(userSelectedHeaderPrintStyleName))
									.findFirst()
									.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

						setResultPrintStyle(resultPrintStyle);
					}*/
					headerValue = filterTestTypeData.getHeader1_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeader2()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeader2");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){

						PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
									.filter(e -> e.getReportPrintStyleName().equals(userSelectedHeaderPrintStyleName))
									.findFirst()
									.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

						setResultPrintStyle(resultPrintStyle);
					}*/
					headerValue = filterTestTypeData.getHeader2_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeader3()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeader3");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					headerValue = filterTestTypeData.getHeader3_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeader4()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeader4");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					headerValue = filterTestTypeData.getHeader4_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeader5()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeader5");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					headerValue = filterTestTypeData.getHeader5_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderRepeat()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderRepeat");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);

					String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
					if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
						resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_REPEAT_RESULT_DATA_PREFIX_KEY, "");	
					}
					String repeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
					ApplicationLauncher.logger.debug("populateResultDataOnReport: repeatHeaderValue: " + repeatHeaderValue);
					headerValue = repeatHeaderValue;//filterTestTypeData.getHeader3_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderSelfHeat()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderSelfHeat");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleTableHeaderName();//"TableHeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);

					String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
					if(ConstantReportV2.REPEAT_START_TO_END_FEATURE_ENABLED){
						resultIterationId = rpPrintPositionData.getKeyParam().replace(ConstantReportV2.CELL_HEADER_POSITION_HEADER_SELF_HEAT_RESULT_DATA_PREFIX_KEY, "");	
					}

					String selfHeatHeaderValue = filterTestTypeData.getIterationReadingPrefixValue() + resultIterationId;
					ApplicationLauncher.logger.debug("populateResultDataOnReport: selfHeatHeaderValue: " + selfHeatHeaderValue);
					headerValue = selfHeatHeaderValue;

					//headerValue = filterTestTypeData.getHeader3_Value();
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTestPeriodInMinutes()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderTestPeriodInMinutes");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "test_period_in_sec";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);
					
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();
						
						/*String testFilterPreviewWithIterationId = new String (testCasePreview);


						String [] splitTestCaseName;
						if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)){
							splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
							testFilterPreviewWithIterationId = splitTestCaseName[0]+ ConstantReport.EXTENSION_TYPE_CURRENT_IB + 
									ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
						}else if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)){
							splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
							testFilterPreviewWithIterationId = splitTestCaseName[0]+ConstantReport.EXTENSION_TYPE_CURRENT_IMAX + 
									ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
						}*/
						//ApplicationLauncher.logger.debug("populateResultDataOnReport: TestPeriod : testFilterPreviewWithIterationId: " + testFilterPreviewWithIterationId);
						//headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						//ApplicationLauncher.logger.debug("populateResultDataOnReport: TestPeriod: testCasePreview3: " + testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					
					
					
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					/*if(!headerValue.isEmpty()){
						headerValue = ConstantReportV2.TEST_PERIOD_PREFIX + headerValue + ConstantReportV2.TEST_PERIOD_MINUTE_POSTFIX;
					}*/
					
					if(!headerValue.isEmpty()){
						if(Float.parseFloat(headerValue)> 59){
							float timeValueInSec = Float.parseFloat(headerValue);
							float timeValueInMin = timeValueInSec/60;
							headerValue = ConstantReportV2.TEST_PERIOD_PREFIX + String.format("%.02f", timeValueInMin) + ConstantReportV2.TEST_PERIOD_MINUTE_POSTFIX;
						}else{
							headerValue = ConstantReportV2.TEST_PERIOD_PREFIX + headerValue + ConstantReportV2.TEST_PERIOD_SECONDS_POSTFIX;
						}
						
					}

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderWarmupPeriodInMinutes()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderWarmupPeriodInMinutes");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "warmup_period_in_sec";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
				/*	if(!headerValue.isEmpty()){
						headerValue = ConstantReportV2.WARMUP_PERIOD_PREFIX + headerValue + ConstantReportV2.WARMUP_PERIOD_MINUTE_POSTFIX;
					}*/
					
					if(!headerValue.isEmpty()){
						if(Float.parseFloat(headerValue)> 59){
							float timeValueInSec = Float.parseFloat(headerValue);
							float timeValueInMin = timeValueInSec/60;
							headerValue = ConstantReportV2.WARMUP_PERIOD_PREFIX + String.format("%.02f", timeValueInMin) + ConstantReportV2.WARMUP_PERIOD_MINUTE_POSTFIX;
						}else{
							headerValue = ConstantReportV2.WARMUP_PERIOD_PREFIX + headerValue + ConstantReportV2.WARMUP_PERIOD_SECONDS_POSTFIX;
						}
						
					}
					
					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTargetFreq()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderActualFreq");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "target_freq";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);

					//headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					if(!headerValue.isEmpty()){
						headerValue = headerValue + ConstantReportV2.FREQ_POSTFIX;
					}

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTargetEnergy()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderActualEnergy");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "target_energy";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);

					//headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					/*if(!headerValue.isEmpty()){
						headerValue = headerValue + ConstantReportV2.FREQ_POSTFIX;
					}*/

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTargetVoltage()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderActualVoltage");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "target_ryb_voltage";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);
					String[] paramSplittedData = testCasePreview.split("-");
					String voltageData = paramSplittedData[1];
					ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: voltageDatasplittedData[1]:" + voltageData);
					/*if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_voltage";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_voltage";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_voltage";
					}else*/ 
					
					if(testType.equals(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME)){
						keyParam = "target_ryb_voltage";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
						keyParam = "target_r_voltage";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_y_voltage";
					}else if(voltageData.contains(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_b_voltage";
					}else{
						keyParam = "target_ryb_voltage";
					}
					//headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					if(!headerValue.isEmpty()){
						headerValue = headerValue + ConstantReportV2.VOLT_POSTFIX;
					}

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTargetCurrent()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderActualCurrent");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "target_ryb_current";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);
					String[] paramSplittedData = testCasePreview.split("-");
					String voltageData = paramSplittedData[1];
					ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: voltageDatasplittedData[1]:" + voltageData);

/*					if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_current";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_current";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_current";
					}else */
						
						
					if(testType.equals(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME)){
						keyParam = "target_ryb_current";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
						keyParam = "target_r_current";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_y_current";
					}else if(voltageData.contains(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_b_current";
					}else{
						keyParam = "target_ryb_current";
					}
					//headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					if(!headerValue.isEmpty()){
						headerValue = headerValue + ConstantReportV2.CURRENT_POSTFIX;
					}

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateHeaderTargetPf()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateHeaderActualPf");
					String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
					manipulatePrintStyle(userSelectedHeaderPrintStyleName);
					String testCasePreview = filterTestTypeData.getFilterPreview();
					String keyParam = "target_ryb_pf";
					ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview: " + testCasePreview);
					String[] paramSplittedData = testCasePreview.split("-");
					String voltageData = paramSplittedData[1];
					ApplicationLauncher.logger.debug("assertPrintTestTimePeriod: voltageDatasplittedData[1]:" + voltageData);

					/*if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_pf";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_pf";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_ryb_pf";
					}else */
					
					
					if(testType.equals(ConstantApp.VOLT_UNBALANCE_ALIAS_NAME)){
						keyParam = "target_ryb_pf";
					}else if(voltageData.contains(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
						keyParam = "target_r_pf";
					}else if(voltageData.contains(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
						keyParam = "target_y_pf";
					}else if(voltageData.contains(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
						keyParam = "target_b_pf";
					}else{
						keyParam = "target_ryb_pf";
					}
					//headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					if( (testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)) || (testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)) ){
						String resultIterationId = filterTestTypeData.getIterationReadingStartIdUserEntry();							
						String testFilterPreviewWithIterationId = getObsoluteTestPointName( resultIterationId,  testCasePreview);
						headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
					}else {
						headerValue =    getDataFromDeployedTestPoint(keyParam,testCasePreview);
					}
					ApplicationLauncher.logger.debug("populateResultDataOnReport: headerValue: " + headerValue);
					if(!headerValue.isEmpty()){
						if(headerValue.equals("1.0")){
							headerValue = ConstantApp.PF_UPF;
						}
					}

					FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);

				}else if(rpPrintPositionData.isPopulateOnlyHeaders()){
					ApplicationLauncher.logger.debug("populateResultDataOnReport: isPopulateOnlyHeaders");

					/*if(ConstantAppConfig.PRINT_STYLE_LIST.size()>0){

						PrintStyle resultPrintStyle = ConstantAppConfig.PRINT_STYLE_LIST.stream()
									.filter(e -> e.getReportPrintStyleName().equals(userSelectedHeaderPrintStyleName))
									.findFirst()
									.orElse(ConstantAppConfig.PRINT_STYLE_LIST.get(0));

						setResultPrintStyle(resultPrintStyle);
					}*/

					if(testType.equals(ConstantApp.CONST_TEST_ALIAS_NAME)){
						String userSelectedHeaderPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
						manipulatePrintStyle(userSelectedHeaderPrintStyleName);
						String energyFilter = filterTestTypeData.getEnergyFilterData();
						populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(filterTestTypeData.getTestExecutionResultTypeSelected());
						if(rpPrintPositionData.isPopulateHeaderRsmInitial()){
							ApplicationLauncher.logger.debug("populateResultDataOnReport: getKeyParam X: " + rpPrintPositionData.getKeyParam());
							//populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
							populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(rpPrintPositionData.getKeyParam());


						}else if(rpPrintPositionData.isPopulateHeaderRsmFinal()){
							ApplicationLauncher.logger.debug("populateResultDataOnReport: getKeyParam X2: " + rpPrintPositionData.getKeyParam());
							//populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
							populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(rpPrintPositionData.getKeyParam());

						}else if(rpPrintPositionData.isPopulateHeaderRsmDifference()){
							if(!getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_INITIAL);

								String refStdValue = fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation( populateResultFilterDataType, 
										voltageFilter, currentFilter, pfFilter,energyFilter);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: initial refStdValue: " + refStdValue);
								float rsmInitialValue = Float.parseFloat(refStdValue);
								getConstantTestResultRsmInitialHashMap().put(testFilterPreview, refStdValue);
								getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
							}
							if(!getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().get(testFilterPreview)){
								populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_RSM_FINAL);

								String refStdValue = fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation( populateResultFilterDataType, 
										voltageFilter, currentFilter, pfFilter,energyFilter);

								ApplicationLauncher.logger.debug("populateResultDataOnReport: final refStdValue: " + refStdValue);
								float rsmFinalValue = Float.parseFloat(refStdValue);
								getConstantTestResultRsmFinalHashMap().put(testFilterPreview, refStdValue);
								getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: rsmFinalValue: " + rsmFinalValue);


							}
							float rsmInitialValue = Float.parseFloat(getConstantTestResultRsmInitialHashMap().get(testFilterPreview));
							float rsmFinalValue = Float.parseFloat(getConstantTestResultRsmFinalHashMap().get(testFilterPreview));
							float rsmDiffValue = Math.abs( rsmFinalValue - rsmInitialValue);
							String rsmDiffValueStr = "";
							if(rsmDiffValue>=0){
								rsmDiffValueStr= String.format("+%.03f", rsmDiffValue);
							}else{
								rsmDiffValueStr= String.format("%.03f", rsmDiffValue);
							}
							ApplicationLauncher.logger.debug("populateResultDataOnReport: rsmDiffValueStr : " + rsmDiffValueStr);
							FillReportDataColumnXSSF_V1_1(sheet1, rsmDiffValueStr,row_pos, column_pos);
							getConstantTestResultRsmDiffHashMap().put(testFilterPreview, rsmDiffValueStr);
							HashMap<String, String> rsmDiffResultHashMap = new HashMap<String, String>();
							rsmDiffResultHashMap.put("Result", rsmDiffValueStr);


							/*	getOperationProcessDataModel().getOperationLocalOutput().stream()
											.forEach( e-> {
												ApplicationLauncher.logger.debug("populateResultFilterDataType: getOperationLocalOutput1: " + e.getOperationProcessKey());
											});*/

							getOperationProcessDataModel().getOperationLocalOutput().stream()
							.filter(e -> e.getOperationProcessKey().equals(ConstantReportV2.PROCESS_PAGE_OUTPUT_RSM_DIFF_AUTO_INSERT_KEY))
							.findFirst()
							.get()
							.setResultValueHashMap(rsmDiffResultHashMap);

							/*	getOperationProcessDataModel().getOperationLocalOutput().stream()
									.forEach( e-> {
										ApplicationLauncher.logger.debug("populateResultFilterDataType: getOperationLocalOutput2: " + e.getOperationProcessKey());
									});*/

							getConstantTestRsmDiffAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
							//	}
							//}
						}







						if(!rpPrintPositionData.isPopulateHeaderRsmDifference()){

							ApplicationLauncher.logger.debug("populateResultDataOnReport: populateResultFilterDataType X: " + populateResultFilterDataType);
							String refStdValue = fillConstRefStValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
									row_pos, column_pos);
							ApplicationLauncher.logger.debug("populateResultDataOnReport: refStdValue: " + refStdValue);

							if(rpPrintPositionData.isPopulateHeaderRsmInitial()){

								float rsmInitialValue = Float.parseFloat(refStdValue);
								getConstantTestResultRsmInitialHashMap().put(testFilterPreview, refStdValue);
								getConstantTestRsmInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: rsmInitialValue: " + rsmInitialValue);
							}else if(rpPrintPositionData.isPopulateHeaderRsmFinal()){
								float rsmFinalValue = Float.parseFloat(refStdValue);
								getConstantTestResultRsmFinalHashMap().put(testFilterPreview, refStdValue);
								getConstantTestRsmFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
								ApplicationLauncher.logger.debug("populateResultDataOnReport: rsmFinalValue: " + rsmFinalValue);
							}
						}


					}

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("populateResultDataOnReport: Exception1: "+e.getMessage());
			}
		}

		if(OPERATION_PROCESS_ENABLED){
			String operationMode = filterTestTypeData.getOperationMode();
			if(operationMode.equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT)){


				String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();
				ApplicationLauncher.logger.debug("populateResultDataOnReport: testType: " + testType);
				ApplicationLauncher.logger.debug("populateResultDataOnReport: operationStorageKey: " + operationStorageKey);

				getOperationProcessDataModel().getOperationLocalInput().stream()
				.filter(e -> e.getOperationProcessKey().equals(operationStorageKey))
				.map( e -> e = getTargetOperationProcessInternalInputData());


				getOperationProcessDataModel().getOperationLocalInput().stream()
				.filter(e -> e.getOperationProcessKey().equals(operationStorageKey))
				.forEachOrdered( e -> {
					e.getResultValueHashMap().entrySet().stream().forEach( k -> {
						ApplicationLauncher.logger.debug("populateResultDataOnReport: key : " + k.getKey() + " -> " + k.getValue());

					});
				});

			}
			/*if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
				if(isProcessRepeatAverageValue()){
					boolean populateStatus = isProcessRepeatAverageStatus();
					String upperLimitStr = "+0.064";
					String lowerLimitStr = "-0.064";
					manipulateAverage(filterTestTypeData,getRepeatAggregatedAverageProcessData(),
								upperLimitStr, lowerLimitStr );
					populateAverageData( filterTestTypeData, sheet1, meter_col, 
							ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE,
							ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS,
							populateStatus);

				}
			}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
				if(isProcessSelfHeatAverageValue()){
					boolean populateStatus = isProcessSelfHeatAverageStatus();
					String upperLimitStr = "+0.064";
					String lowerLimitStr = "-0.064";
					manipulateAverage(filterTestTypeData,getSelfHeatAggregatedAverageProcessData(),
							upperLimitStr, lowerLimitStr );
					populateAverageData( filterTestTypeData, sheet1, meter_col, 
							ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE,
							ConstantReportV2.RESULT_DATA_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS,
							populateStatus);

				}
			} 
			 */

		}




		return status;
	}
	
	public String getObsoluteTestPointName(String resultIterationId, String testCasePreview){
		ApplicationLauncher.logger.debug("getObsoluteTestPointName: Entry");
			
			String testFilterPreviewWithIterationId = new String (testCasePreview);

			String headerValue = "";
			String [] splitTestCaseName;
			if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)){
				splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IB);
				testFilterPreviewWithIterationId = splitTestCaseName[0]+ ConstantReport.EXTENSION_TYPE_CURRENT_IB + 
						ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
			}else if(testFilterPreviewWithIterationId.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)){
				splitTestCaseName = testFilterPreviewWithIterationId.split(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX);
				testFilterPreviewWithIterationId = splitTestCaseName[0]+ConstantReport.EXTENSION_TYPE_CURRENT_IMAX + 
						ConstantReportV2.TEST_POINT_FILTER_SEPERATOR + resultIterationId;
			}
			ApplicationLauncher.logger.debug("getObsoluteTestPointName : testFilterPreviewWithIterationId: " + testFilterPreviewWithIterationId);
			//headerValue =    getDataFromDeployedTestPoint(keyParam,testFilterPreviewWithIterationId);
			return testFilterPreviewWithIterationId;
		
	}


	public String dataSetupForConstantTestDutInitial(String populateResultFilterDataType,String testFilterPreview,
			RpPrintPosition rpPrintPositionData){

		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutInitial: Entry");
		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutInitial: Constant: dut Initial: getKeyParam : " + rpPrintPositionData.getKeyParam());
		//getConstantTestDutInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
		populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
		String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
		populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);

		String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_INTIAL_AUTO_INSERT_KEY;
		Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


		if(operationProcessDataJsonReadDataOpt.isPresent()){
			setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
		}else{
			setTargetOperationProcessInternalInputData(null);
		}
		getConstantTestDutInitalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);

		return populateResultFilterDataType; 

	}

	public String dataSetupForConstantTestDutFinal(String populateResultFilterDataType,String testFilterPreview,
			RpPrintPosition rpPrintPositionData){

		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Entry");


		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Constant: dut final getKeyParam: " + rpPrintPositionData.getKeyParam());
		//getConstantTestDutFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);
		populateResultFilterDataType = ConstantReportV2.getResultDataTypeHashMap().get(rpPrintPositionData.getKeyParam());
		String userSelectedResultSourceType = rpPrintPositionData.getKeyParam();//rpPrintPositionData.getTestExecutionResultTypeSelected();
		populateResultFilterDataType = ConstantReportV2.getResultSourceTypeHashMap().get(userSelectedResultSourceType);

		String operationStorageKey = ConstantReportV2.PROCESS_PAGE_INPUT_DUT_FINAL_AUTO_INSERT_KEY;
		Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
				ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


		if(operationProcessDataJsonReadDataOpt.isPresent()){

			setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
		}else{
			setTargetOperationProcessInternalInputData(null);
		}

		/*		String maxErrorAllowedKey = "max_error_allowed";
		String minErrorAllowedKey = "min_error_allowed";



		//ApplicationLauncher.logger.debug("populateResultDataOnReport: testCasePreview2: " + testFilterPreviewWithIterationId);
		String maxErrorAllowed =  getDataFromDeployedTestPoint(maxErrorAllowedKey,testFilterPreview);//WithIterationId);
		String minErrorAllowed =  getDataFromDeployedTestPoint(minErrorAllowedKey,testFilterPreview);//WithIterationId);
		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Constant minErrorAllowed: " + minErrorAllowed);
		ApplicationLauncher.logger.debug("dataSetupForConstantTestDutFinal: Constant maxErrorAllowed: " + maxErrorAllowed);

		getConstantTestMaxErrorAllowedHashMap().put(testFilterPreview, maxErrorAllowed);
		getConstantTestMinErrorAllowedHashMap().put(testFilterPreview, minErrorAllowed);*/

		getConstantTestDutFinalAutoInsertionDataProcessedHashMap().put(testFilterPreview, true);

		return populateResultFilterDataType;
	}

	public void fetchConstantTestDutDataForAutoOperationProcess(ReportProfileTestDataFilter filterTestTypeData, String populateResultFilterDataType){

		ApplicationLauncher.logger.debug("fetchConstantTestDutDataForAutoOperationProcess: Entry");

		//String testFilterPopulateType = filterTestTypeData.getTestFilterDataPopulateType();//.getPopulateType();
		String voltageFilter = filterTestTypeData.getVoltPercentFilterData();//.getVoltageFilter();
		String currentFilter = filterTestTypeData.getCurrentPercentFilterData();//getCurrentFilter();
		String pfFilter = filterTestTypeData.getPfFilterData();//.getPfFilter();
		String energyFilter = filterTestTypeData.getEnergyFilterData();

		//if(testFilterPopulateType.equals(ConstantReport.REPORT_DATA_POPULATE_VERTICAL)){

		/*if( singleCellData ){

				fillConstRefStValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
						row_pos, column_pos);
				status = true;
			}else{*/

		//fillConstErrorValueCustomReportXSSF(sheet1,populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter,
		//		row_pos, column_pos, meter_col);
		fetchConstantTestDutErrorValueFromDataBaseForAutoProcessOperation(populateResultFilterDataType,voltageFilter,currentFilter,pfFilter, energyFilter);

		//	status = true;
		//}
		//}

	}


	public void fetchConstantTestDutErrorValueFromDataBaseForAutoProcessOperation( String datatype, 
			String voltage, String current, String pf,String selected_power){

		ApplicationLauncher.logger.debug("fetchConstantTestDutErrorValueFromDataBaseForAutoProcessOperation: Entry");

		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		processConstantTestDutDataForAutoProcessOperation( filter_result);
	}


	public String fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation( String datatype, 
			String voltage, String current, String pf,String selected_power){
		ApplicationLauncher.logger.debug("fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation: Entry");
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		filter_result = fetchLatestResult(filter_result);
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		String refStdValue = processConstantTestRsmDataForAutoProcessOperation(filter_result);
		return refStdValue;
	}

	public String fetchConstantTestRsmDataForAutoOperationProcess(ReportProfileTestDataFilter filterTestTypeData, String populateResultFilterDataType){

		ApplicationLauncher.logger.debug("fetchConstantTestRsmDataForAutoOperationProcess: Entry");

		//String testFilterPopulateType = filterTestTypeData.getTestFilterDataPopulateType();//.getPopulateType();
		String voltageFilter = filterTestTypeData.getVoltPercentFilterData();//.getVoltageFilter();
		String currentFilter = filterTestTypeData.getCurrentPercentFilterData();//getCurrentFilter();
		String pfFilter = filterTestTypeData.getPfFilterData();//.getPfFilter();
		String energyFilter = filterTestTypeData.getEnergyFilterData();


		String refStdValue = fetchConstantTestRsmValueFromDataBaseForAutoProcessOperation( populateResultFilterDataType, 
				voltageFilter, currentFilter, pfFilter,energyFilter);

		return refStdValue;
	}


	public String processConstantTestRsmDataForAutoProcessOperation(JSONArray filter_result){
		ApplicationLauncher.logger.debug("processConstantTestRsmDataForAutoProcessOperation: filter_result: " + filter_result);

		String error_value = "";
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";

			String test_case_name = "";
			String exportDeviceName = "";
			//int row_pos = input_row_pos;
			//Row row = sheet1.getRow(row_pos);
			//Cell column = row.getCell(column_pos);
			for(int i=0; i<filter_result.length(); i++){///expectation is 2 results one for import and another one for export

				result_json = filter_result.getJSONObject(i);
				device_name = result_json.getString("device_name");

				exportDeviceName = getRealDeviceIDForExportMode(device_name);
				error_value = result_json.getString("error_value");
				if(device_name.equals(exportDeviceName)){//import mode
					if(Integer.valueOf(device_name)<=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
						//column.setCellValue(error_value); 
						if(OPERATION_PROCESS_ENABLED){
							//test_status = result_json.getString("test_status");
							String test_status = "";
							updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);

						}
					}
				}else{ //export mode
					if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){

						//column.setCellValue(error_value); 
						if(OPERATION_PROCESS_ENABLED){
							//test_status = result_json.getString("test_status");
							String test_status = "";
							updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);

						}
					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReferenceWithCellXSSF :JSONException: "+e.getMessage());
		}

		return error_value;
	}





	public void processConstantTestDutDataForAutoProcessOperation( JSONArray filter_result){

		ApplicationLauncher.logger.debug("processConstantTestDutDataForAutoProcessOperation: Entry");
		//ApplicationLauncher.logger.debug("FillErrorValueXSSF: filter_result: " + filter_result);

		try {
			//sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			if (filter_result.length()>0){
				//int valuePopulatedInExcelCount = 0;
				//for(int i=0; i<filter_result.length(); i++){

				for(int j=0; j<filter_result.length()  ; j++){
					//	for(int j=0; j<filter_result.length() || (valuePopulatedInExcelCount <filter_result.length()) ; j++){	
					//for(int j=0; valuePopulatedInExcelCount<=filter_result.length(); j++){	
					result_json = filter_result.getJSONObject(j);
					device_name = result_json.getString("device_name");
					//ApplicationLauncher.logger.info("FillErrorValueXSSF: device_name:"+device_name);
					//ApplicationLauncher.logger.info("FillErrorValueXSSF: filter_result.length():"+filter_result.length());
					exportMode = false;
					if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
						exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
						ApplicationLauncher.logger.info("processConstantTestDutDataForAutoProcessOperation: device_name2:"+device_name);
						if(!device_name.equals(exportModeDeviceName)){
							exportMode = true;
							device_name= exportModeDeviceName;
						}
						//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
						//}
					}

					//ApplicationLauncher.logger.debug("FillErrorValueXSSF: result_json: " + result_json);
					error_value = result_json.getString("error_value");
					if(OPERATION_PROCESS_ENABLED){
						test_status = result_json.getString("test_status");
						ApplicationLauncher.logger.info("processConstantTestDutDataForAutoProcessOperation: device_name: "+device_name + " :" + error_value + " -> " + test_status);
						updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);
						updateRepeatAverageOperationProcessData(device_name,error_value,test_status);
						updateSelfHeatAverageOperationProcessData(device_name,error_value,test_status);

					}

					/*		ApplicationLauncher.logger.debug("FillErrorValueXSSF: device_name: " + device_name+ " : "+ error_value);
							column.setCellValue(error_value); 	
							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS){
								test_status = result_json.getString("test_status");
							}


							test_status = result_json.getString("test_status");
					 */						
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("processConstantTestDutDataForAutoProcessOperation: JSONException: " + e.getMessage());

		}catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("processConstantTestDutDataForAutoProcessOperation: Exception: " + e1.getMessage());

		}
	}



	public void processAverageDataOnReport(XSSFSheet sheet1,int meter_col ,
			ReportProfileTestDataFilter filterTestTypeData,
			String testType) {
		ApplicationLauncher.logger.debug("processAverageDataOnReport: Entry");

		String operationSourceResultType = filterTestTypeData.getOperationSourceResultTypeSelected();
		if(operationSourceResultType.equals(ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_AVERAGE)){
			String operationMode = filterTestTypeData.getOperationMode();
			if(operationMode.equals(ConstantReportV2.OPERATION_PROCESS_MODE_INPUT)){
				String operationStorageKey = filterTestTypeData.getNonDisplayedDataSet();

				Optional<OperationProcessDataJsonRead> operationProcessDataJsonReadDataOpt =  getOperationProcessData(operationStorageKey, 
						ConstantReportV2.OPERATION_PROCESS_DATA_TYPE_LOCAL_INPUT);


				if(operationProcessDataJsonReadDataOpt.isPresent()){
					ApplicationLauncher.logger.debug("processAverageDataOnReport: getOperationProcessKey: " + operationProcessDataJsonReadDataOpt.get().getOperationProcessKey());
					setTargetOperationProcessInternalInputData(operationProcessDataJsonReadDataOpt.get());
				}else{
					setTargetOperationProcessInternalInputData(null);
				}

			}else{
				setTargetOperationProcessInternalInputData(null);
			}



			String testFilterPreview = filterTestTypeData.getFilterPreview();
			ApplicationLauncher.logger.debug("processAverageDataOnReport: testFilterPreview : " + testFilterPreview);
			if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
				if(getProcessRepeatAverageValueHashMap().get(testFilterPreview)){//isProcessRepeatAverageValue()){
					boolean populateStatus = getProcessRepeatAverageStatusHashMap().get(testFilterPreview);// isProcessRepeatAverageStatus();



					try{
						ApplicationLauncher.logger.debug("processAverageDataOnReport: getRepeatAverageMaxErrorAllowedHashMap() : " + getRepeatAverageMaxErrorAllowedHashMap());
						String upperLimitStr = getRepeatAverageMaxErrorAllowedHashMap().get(testFilterPreview); //"+0.064";
						String lowerLimitStr = getRepeatAverageMinErrorAllowedHashMap().get(testFilterPreview);//"-0.064";
						
/*						if(upperLimitStr.isEmpty()) {
							
						}
						
						if(lowerLimitStr.isEmpty()) {
							
						}*/

						manipulateAverage(filterTestTypeData,getRepeatAggregatedAverageProcessData(),
								upperLimitStr, lowerLimitStr );
						populateAverageData( filterTestTypeData, sheet1, meter_col, 
								ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_VALUE,
								ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_REPEAT_AVERAGE_STATUS,
								populateStatus);

						//ApplicationLauncher.logger.debug("processAverageDataOnReport: getOperationProcessKey() : " + getTargetOperationProcessInternalInputData().getOperationProcessKey() );
						//ApplicationLauncher.logger.debug("processAverageDataOnReport: getResultValueHashMap() : " + getTargetOperationProcessInternalInputData().getResultValueHashMap() );
						//ApplicationLauncher.logger.debug("processAverageDataOnReport: getResultStatusHashMap() : " + getTargetOperationProcessInternalInputData().getResultStatusHashMap() );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("processAverageDataOnReport: Exception1: "+e.getMessage());
					}

				}
			}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
				if(getProcessSelfHeatAverageValueHashMap().get(testFilterPreview)){//isProcessSelfHeatAverageValue()){
					boolean populateStatus = getProcessSelfHeatAverageStatusHashMap().get(testFilterPreview);//isProcessSelfHeatAverageStatus();
					//String upperLimitStr = "+0.064";
					//String lowerLimitStr = "-0.064";
					try{
						ApplicationLauncher.logger.debug("processAverageDataOnReport: getSelfHeatAverageMaxErrorAllowedHashMap() : " + getSelfHeatAverageMaxErrorAllowedHashMap());
						String upperLimitStr = getSelfHeatAverageMaxErrorAllowedHashMap().get(testFilterPreview); //"+0.064";
						String lowerLimitStr = getSelfHeatAverageMinErrorAllowedHashMap().get(testFilterPreview);//"-0.064";


						manipulateAverage(filterTestTypeData,getSelfHeatAggregatedAverageProcessData(),
								upperLimitStr, lowerLimitStr );
						populateAverageData( filterTestTypeData, sheet1, meter_col, 
								ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_VALUE,
								ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_DUT_SELF_HEAT_AVERAGE_STATUS,
								populateStatus);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("processAverageDataOnReport: Exception2: "+e.getMessage());
					}
				}
			} 
		}


	}



	private void populateAverageData(ReportProfileTestDataFilter filterTestTypeData,XSSFSheet sheet1,int meter_col,
			String averageValueKey, String averageStatusKey, boolean populateStatus) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("populateAverageData: Entry");
		ApplicationLauncher.logger.debug("populateAverageData: averageValueKey: " + averageValueKey);
		ApplicationLauncher.logger.debug("populateAverageData: averageStatusKey: " + averageStatusKey);
		ApplicationLauncher.logger.debug("populateAverageData: populateStatus: " + populateStatus);
		//String internalOutputDataKey = filterTestTypeData.getOperationProcessLocalOutputKey();
		//ApplicationLauncher.logger.debug("populateAverageData: internalOutputDataKey: " + internalOutputDataKey);
		List<RpPrintPosition> rpPrintPositionDataList = filterTestTypeData.getRpPrintPositionList();
		String testType  = filterTestTypeData.getTestTypeAlias();
		Optional<RpPrintPosition> rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
				.filter(e -> e.getKeyParam().equals(averageValueKey))
				.findFirst();
		if(rpPrintPositionDataOptional.isPresent()){
			RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
			String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
			int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
			int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
			/*			if(rpPrintPositionData.isPopulateOnlyHeaders()){

				ApplicationLauncher.logger.debug("populateAverageData: isPopulateOnlyHeaders");
				ApplicationLauncher.logger.debug("populateAverageData: getKeyParam: " + rpPrintPositionData.getKeyParam());

				String userSelectedPrintStyleName = getUserSelectedPrintStyleGenericHeaderName();//"HeaderStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				String headerValue = "";


				HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();

				resultValueHashMap = getTargetOperationProcessInternalOutputData().getResultValueHashMap();
				ApplicationLauncher.logger.debug("populateAverageData: resultValueHashMap: " + resultValueHashMap);

				if(resultValueHashMap.size()>0){
					headerValue = resultValueHashMap.entrySet().stream().findFirst().get().getValue();

				}
				//}

				FillReportDataColumnXSSF_V1_1(sheet1, headerValue,row_pos, column_pos);
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col);

			}else */
			ApplicationLauncher.logger.debug("populateAverageData: isPopulateAllData(): " + rpPrintPositionData.isPopulateAllData());
			if(rpPrintPositionData.isPopulateAllData()){
				ApplicationLauncher.logger.debug("populateAverageData: populate All DUT");
				HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();
				//String testType  = filterTestTypeData.getTestTypeAlias();
				if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
					resultValueHashMap = getResultRepeatAverageValueHashMap();//getTargetOperationProcessInternalOutputData().getResultValueHashMap();
				}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
					resultValueHashMap = getResultSelfHeatAverageValueHashMap();
				}
				boolean populateErrorValue = true;
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
				if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
					resultStatusHashMap = getResultRepeatAverageStatusHashMap();//getTargetOperationProcessInternalOutputData().getResultStatusHashMap();
				}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
					resultStatusHashMap = getResultSelfHeatAverageStatusHashMap();
				}
				///*if(populateStatus){
				//	ApplicationLauncher.logger.debug("populateAverageData: hit1");
				if(resultStatusHashMap.size()>0){
					ApplicationLauncher.logger.debug("populateAverageData: hit1");
					fillProcessedOutputDataXSSF_V2(sheet1, resultValueHashMap, resultStatusHashMap, column_pos,row_pos, meter_col);
				}else{
					ApplicationLauncher.logger.debug("populateAverageData: hit2");
					fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				}
				//}else{*/
				//ApplicationLauncher.logger.debug("populateAverageData: hit4");
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				//}


			}





		}
		if(populateStatus){
			ApplicationLauncher.logger.debug("populateAverageData: hit3");
			rpPrintPositionDataOptional = rpPrintPositionDataList.stream()
					.filter(e -> e.getKeyParam().equals(averageStatusKey))
					.findFirst();
			if(rpPrintPositionDataOptional.isPresent()){
				RpPrintPosition rpPrintPositionData = rpPrintPositionDataOptional.get();
				String dataStartingErrorValueCellPosition = rpPrintPositionData.getCellPosition();
				int row_pos = getRowValueFromCellValue(dataStartingErrorValueCellPosition);
				int column_pos = getColValueFromCellValue(dataStartingErrorValueCellPosition);
				boolean populateErrorValue = false;
				//fillProcessedOutputDataXSSF(sheet1, resultValueHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
				String userSelectedPrintStyleName = getUserSelectedPrintStyleResultName();//"ResultStyle";
				manipulatePrintStyle(userSelectedPrintStyleName);
				HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
				if(testType.equals(ConstantApp.REPEATABILITY_ALIAS_NAME)){
					resultStatusHashMap = getResultRepeatAverageStatusHashMap();//getTargetOperationProcessInternalOutputData().getResultStatusHashMap();
				}else if(testType.equals(ConstantApp.SELF_HEATING_ALIAS_NAME)){
					resultStatusHashMap = getResultSelfHeatAverageStatusHashMap();
				}
				ApplicationLauncher.logger.debug("populateAverageData: hit4");
				fillProcessedOutputDataXSSF(sheet1, resultStatusHashMap,  column_pos,row_pos, meter_col,populateErrorValue);
			}
		}

	}

	public void replaceDataInReportExcel( XSSFSheet sheet1, String findText, String ReplaceText){

		ApplicationLauncher.logger.debug("replaceDataInReportExcel : Entry");
		ApplicationLauncher.logger.debug("replaceDataInReportExcel : findText: " +findText);
		ApplicationLauncher.logger.debug("replaceDataInReportExcel : ReplaceText: " +ReplaceText);
		String data = "";
		try {
			Iterator<Row> rowIterator = sheet1.rowIterator();
			while(rowIterator.hasNext()){

				try {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()){
						try {
							Cell cell = cellIterator.next();
							data = cell.getStringCellValue();
							// ApplicationLauncher.logger.debug("replaceDataInReportExcel : data: " +data);
							if(data.equals(findText)){
								ApplicationLauncher.logger.debug("replaceDataInReportExcel : updated: ");
								cell.setCellValue(ReplaceText);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception1: "+e.getMessage());
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception2: "+e.getMessage());
				}
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("replaceDataInReportExcel: Exception3: "+e.getMessage());
		}

		ApplicationLauncher.logger.debug("replaceDataInReportExcel : Exit");
	}


	public static int getRowValueFromCellValue(String cellvalue){
		String str_row = cellvalue.replaceAll("[^0-9]", "");
		int row = Integer.parseInt(str_row)-1;
		return row;
	}



	public static int getColValueFromCellValue(String cellvalue){
		String col = cellvalue.replaceAll("[0-9]", "");

		int col_value = 0;
		char ch = ' ';
		int ascii_value = 0;
		for(int i=0; i<col.length(); i++){
			ch = col.charAt(i);
			ascii_value = (int)ch;
			col_value = (col_value*26) + ascii_value - 64;
		}

		col_value = col_value - 1;
		return col_value;
	}


	public ArrayList<String> FillMeterColumnXSSF_V2(XSSFSheet sheet1, JSONArray result, int row_pos, int column_pos,
			int maxDutDisplayRequestedPerPage, boolean splitDutDisplayIntoMultiplePage, int presentPageNumberIteration){

		ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: Entry ");
		ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: presentPageNumberIteration: " + presentPageNumberIteration);
		int serialNumberCount =0;
		//int incrementedMaxDutDisplayRequestedPerPage = presentPageNumberIteration * maxDutDisplayRequestedPerPage;
		//if ( (serialNumberCount > maxDutDisplayRequestedPerPage) &&

		ArrayList<String> uniqueDeviceName= new ArrayList<String>();
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String rack_id = "";
			String device_rack_id = "";
			boolean updateSpreadSheetAllowed = true;	
			for(int i=0; i<result.length(); i++){

				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				result_json = result.getJSONObject(i);
				rack_id = result_json.getString("device_name");
				device_rack_id = getDeviceNameWithRackID(rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: rack_id: " + rack_id);
				ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: device_rack_id: " + device_rack_id);
				if(!rack_id.equals(String.valueOf(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID))) {
					/*********Added below snippet for unique device rack name on Excel file***********/
					//if( (serialNumberCount < maxDutDisplayRequestedPerPage)){
	
					updateSpreadSheetAllowed = true;	
					//|| (maxDutDisplayRequestedPerPage) ){
					ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: serialNumberCount: " + serialNumberCount);
					ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: maxDutDisplayRequestedPerPage: " + maxDutDisplayRequestedPerPage);
					if (serialNumberCount >= maxDutDisplayRequestedPerPage){
						//	&& (!splitDutDisplayIntoMultiplePage) ){
						if(splitDutDisplayIntoMultiplePage){
							if(getDutMeterDataPreviousPageUpdatedPageNumber()< presentPageNumberIteration){
								//if(getDutMeterDataPreviousPageUpdatedPageNumber()==0){
								if(presentPageNumberIteration==1){	
									//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit1: ");
									setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
									setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
									ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit1: ");
									break;
								}else{
									ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit2: ");
									if	(serialNumberCount >= maxDutDisplayRequestedPerPage) {
										//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit3A: ");
										setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
										setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
										ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit3: ");
										break;
									}
									/*								if	(serialNumberCount > incrementedMaxDutDisplayRequestedPerPage) {
										if(rack_id){
											ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit2: ");
											updateSpreadSheetAllowed = false;
										}
									}*/
								}
							}
	
	
						}else{
							//setDutMeterDataPreviousPageLastUpdatedRackId(device_rack_id);
							//setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
							setSerialNumberMaxCount(serialNumberCount);
							ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit4: ");
							break;
							//updateSpreadSheetAllowed = false;
						}
	
					}else{
						if(splitDutDisplayIntoMultiplePage){
							//if(getDutMeterDataPreviousPageUpdatedPageNumber()>1){
							/*						if	(serialNumberCount > incrementedMaxDutDisplayRequestedPerPage) {
	
							}*/
							if(presentPageNumberIteration>1){
								ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit5: ");
								//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit5: rack_id: " + rack_id);
								if(Integer.parseInt(rack_id) < Integer.parseInt(getDutMeterDataPreviousPageLastUpdatedRackId())){
									ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit6: ");
									updateSpreadSheetAllowed = false;
								}else if	(serialNumberCount >= maxDutDisplayRequestedPerPage) {
									//ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: hit3A: ");
									//updateSpreadSheetAllowed = false;
									setDutMeterDataPreviousPageLastUpdatedRackId(rack_id);
									setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
									ApplicationLauncher.logger.debug("FillMeterColumnXSSF_V2: break:  hit7: ");
									break;
								}
							}
						}
					}
	
					if(updateSpreadSheetAllowed){	
	
						if(ConstantAppConfig.GENERATE_INDIVIDUAL_METER_REPORT_ENABLED){
							if (isIndividualMeterReportSelected()) {
	
								if(getSelectedMeterSerialNo().equals(device_rack_id)){
									if (!uniqueDeviceName .contains(device_rack_id))    {
										uniqueDeviceName .add(device_rack_id);
										column.setCellValue(device_rack_id); 
										serialNumberCount++;
										row_pos++;
									}
								}
							}else {
								if (!uniqueDeviceName .contains(device_rack_id))   {
									uniqueDeviceName .add(device_rack_id);
									column.setCellValue(device_rack_id); 
									serialNumberCount++;
									row_pos++;
								}
							}
						}else {
							if (!uniqueDeviceName .contains(device_rack_id))   {
								uniqueDeviceName .add(device_rack_id);
								column.setCellValue(device_rack_id); 
								serialNumberCount++;
								row_pos++;
							}
						}
						//column.setCellValue(device_rack_id); 
						//row_pos++;
						/*********commented above snippet for unique device rack name on Excel file***********/
						
						applySelectedPrintStyle(column,"",ConstantReportV2.PRINT_STYLE_RESULT_TYPE_NONE);
						
						/*CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
						try{
							XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
							//font.setFontHeightInPoints((short)25);
								font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
							font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");
	
							style.setFont(font);
							style.setBorderTop(BorderStyle.THIN);
							style.setBorderRight(BorderStyle.THIN);
							style.setBorderBottom(BorderStyle.THIN);
							style.setBorderLeft(BorderStyle.THIN);
							//style.setBorderBottom(BorderStyle.THIN);
	
	
							PrintStyle selectedPrintStyle = getLoadedPrintStyle();
	
							font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
							font.setFontName(selectedPrintStyle.getFontName());//"Courier New");
	
							font.setBold(selectedPrintStyle.isBold());							
	
							style.setFont(font);
	
							style.setWrapText(selectedPrintStyle.isWrapText());
							if(selectedPrintStyle.isHorizontalAlignmentCentre()){
								style.setAlignment(HorizontalAlignment.CENTER);
							}
							if(selectedPrintStyle.isVerticalAlignmentCentre()){
								style.setVerticalAlignment(VerticalAlignment.CENTER);
							}
							if(selectedPrintStyle.isBorder()){
								style.setBorderTop(BorderStyle.THIN);
								style.setBorderRight(BorderStyle.THIN);
								style.setBorderBottom(BorderStyle.THIN);
								style.setBorderLeft(BorderStyle.THIN);
							}
							column.setCellStyle(style);
	
							//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: row: " + row);
							//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: column: " + column);
						}catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ApplicationLauncher.logger.error("FillMeterColumnXSSF_V2: Exception FontSetting: " + e.getMessage());
	
						}*/	
				
						
					}
					
					/*else{
						if(splitDutDisplayIntoMultiplePage){
							setDutMeterDataPreviousPageLastUpdatedRackId(device_rack_id);
							setDutMeterDataPreviousPageUpdatedPageNumber(presentPageNumberIteration);
						}
						ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: break hit: ");
						break;
					}*/
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillMeterColumnXSSF_V2: JSONException:"+e.getMessage());
		}

		setSerialNumberMaxCount(serialNumberCount);
		return uniqueDeviceName;
	}

	public void clearMeterDeviceOverAllStatus() {
		meterDeviceOverAllStatus.clear();
	}
	public String fetchMeterDeviceOverAllStatus(String meterSerialNoKey) {
		return meterDeviceOverAllStatus.get(meterSerialNoKey);
	}

	public HashMap<String, String> getMeterDeviceOverAllStatus() {
		return meterDeviceOverAllStatus;
	}

	public void addMeterDeviceOverAllStatus(String meterSerialNoKey, String meterOverAllStatus) {
		meterDeviceOverAllStatus.put(meterSerialNoKey,meterOverAllStatus);  
	}

	public void addMeterDeviceOverAllStatusCount(String meterSerialNoKey, int meterOverAllStatusCount) {
		meterDeviceOverAllStatusCount.put(meterSerialNoKey,meterOverAllStatusCount);  
	}

	public void updateMeterDeviceOverAllStatus(String meterSerialNoKey, String meterOverAllStatus) {
		meterDeviceOverAllStatus.put(meterSerialNoKey,meterOverAllStatus);  
	}





	public void clearMeterDevicePageStatus() {
		meterDevicePageStatus.clear();
	}

	public void clearMeterDevicePageStatusCount() {
		meterDevicePageStatusCount.clear();
	}



	public void clearMeterDeviceOverAllStatusCount() {
		meterDeviceOverAllStatusCount.clear();
	}


	public void clearMeterDevicePageStatus(String pageNo) {
		if(meterDevicePageStatus.containsKey(pageNo)){
			meterDevicePageStatus.get(pageNo).clear();
		}
	}

	public void clearMeterDevicePageStatusCount(String pageNo) {
		if(meterDevicePageStatusCount.containsKey(pageNo)){
			meterDevicePageStatusCount.get(pageNo).clear();
		}
	}



	/*public void clearMeterDeviceOverAllStatusCount(String pageNo) {
		if(meterDeviceOverAllStatusCount.containsKey(pageNo)){
			meterDeviceOverAllStatusCount.get(pageNo).clear();
		}
	}*/

	public String fetchMeterDevicePageStatus(String pageNo, String meterSerialNoKey ) {
		return meterDevicePageStatus.get(pageNo ).get(meterSerialNoKey);
	}



	public int fetchMeterDevicePageStatusCount(String pageNo, String meterSerialNoKey ) {
		return meterDevicePageStatusCount.get(pageNo ).get(meterSerialNoKey);
	}



	public int fetchMeterDeviceOverAllStatusCount( String meterSerialNoKey ) {
		return meterDeviceOverAllStatusCount.get(meterSerialNoKey);
	}

	public HashMap<String,Map<String, String>> getMeterDevicePageStatus() {
		return meterDevicePageStatus;
	}




	public HashMap<String,Map<String, Integer>> getMeterDevicePageStatusCount() {
		return meterDevicePageStatusCount;
	}



	public HashMap<String, Integer> getMeterDeviceOverAllStatusCount() {
		return meterDeviceOverAllStatusCount;
	}

	public void addMeterDevicePageStatus(String pageNo , String meterSerialNoKey, String meterStatus) {

		HashMap<String, String>  resultHashMap = new HashMap<String, String> ();
		if(meterDevicePageStatus.containsKey(pageNo)){
			resultHashMap = (HashMap<String, String>) meterDevicePageStatus.get(pageNo);
			//if(meterDevicePageStatus.get(pageNo).containsKey(meterSerialNoKey)){
			//	resultHashMap = (HashMap<String, String>) meterDevicePageStatus.get(pageNo);
			//	resultHashMap.put(meterSerialNoKey, meterStatus);
			//}else{
			//	resultHashMap.put(meterSerialNoKey, meterStatus);
			//}
		}//else{

		//}
		resultHashMap.put(meterSerialNoKey, meterStatus);
		//resultHashMap.put(meterSerialNoKey, meterStatus);
		meterDevicePageStatus.put(pageNo,resultHashMap);  
	}





	public void addMeterDevicePageStatusCount(String pageNo , String meterSerialNoKey, int meterStatusCount) {

		HashMap<String, Integer>  resultHashMap = new HashMap<String, Integer> ();
		if(meterDevicePageStatusCount.containsKey(pageNo)){
			resultHashMap = (HashMap<String, Integer>) meterDevicePageStatusCount.get(pageNo);

		}


		resultHashMap.put(meterSerialNoKey, meterStatusCount);

		meterDevicePageStatusCount.put(pageNo,resultHashMap);  
	}




	/*	public void addMeterDeviceOverAllStatusCount(String pageNo , String meterSerialNoKey, int meterStatusCount) {

		HashMap<String, Integer>  resultHashMap = new HashMap<String, Integer> ();
		if(meterDeviceOverAllStatusCount.containsKey(pageNo)){
			resultHashMap = (HashMap<String, Integer>) meterDeviceOverAllStatusCount.get(pageNo);

		}


		resultHashMap.put(meterSerialNoKey, meterStatusCount);

		meterDeviceOverAllStatusCount.put(pageNo,resultHashMap);  
	}*/

	/*public void addMeterDeviceOverAllStatusCount(String meterSerialNoKey, int meterOverAllStatus) {
		meterDeviceOverAllStatusCount.put(meterSerialNoKey,meterOverAllStatus);  
	}*/

	public void updateMeterDevicePageStatus(String pageNo ,String meterSerialNoKey, String meterStatus) {
		//HashMap<String, String>  resultHashMap = new HashMap<String, String> ();
		//resultHashMap.put(meterSerialNoKey, meterStatus);
		//HashMap<String, String>  resultHashMap = (HashMap<String, String>) meterDevicePageStatus.get(pageNo);
		HashMap<String, String>  resultHashMap = new HashMap<String, String> ();
		if(meterDevicePageStatus.containsKey(pageNo)){
			resultHashMap = (HashMap<String, String>) meterDevicePageStatus.get(pageNo);
		}
		resultHashMap.put(meterSerialNoKey, meterStatus);
		meterDevicePageStatus.put(pageNo,resultHashMap);  
	}




	public void updateMeterDevicePageStatusCount(String pageNo ,String meterSerialNoKey, int meterStatusCount) {

		HashMap<String, Integer>  resultHashMap = new HashMap<String, Integer> ();
		if(meterDevicePageStatusCount.containsKey(pageNo)){
			resultHashMap = (HashMap<String, Integer>) meterDevicePageStatusCount.get(pageNo);
		}
		resultHashMap.put(meterSerialNoKey, meterStatusCount);
		meterDevicePageStatusCount.put(pageNo,resultHashMap);  
	}



	/*	public void updateMeterDeviceOverAllStatusCount(String meterSerialNoKey, int meterStatusCount) {

		HashMap<String, Integer>  resultHashMap = new HashMap<String, Integer> ();
		if(meterDeviceOverAllStatusCount.containsKey(pageNo)){
			resultHashMap = (HashMap<String, Integer>) meterDeviceOverAllStatusCount.get(pageNo);
		}
		resultHashMap.put(meterSerialNoKey, meterStatusCount);
		meterDeviceOverAllStatusCount.put(pageNo,resultHashMap);  
	}*/

	public void updateMeterDeviceOverAllStatusCount(String meterSerialNoKey, int meterOverAllStatusCount) {
		meterDeviceOverAllStatusCount.put(meterSerialNoKey,meterOverAllStatusCount);  
	}

	public static int getSerialNumberMaxCount() {
		return serialNumberMaxCount;
	}

	public static void setSerialNumberMaxCount(int serialNumber) {
		serialNumberMaxCount = serialNumber;
	}

	public String getDutMeterDataPreviousPageLastUpdatedRackId() {
		return dutMeterDataPreviousPageLastUpdatedRackId;
	}


	public void setDutMeterDataPreviousPageLastUpdatedRackId(String dutPreviousPageLastUpdatedDeviceRackId) {
		this.dutMeterDataPreviousPageLastUpdatedRackId = dutPreviousPageLastUpdatedDeviceRackId;
	}


	public int getDutMeterDataPreviousPageUpdatedPageNumber() {
		return dutMeterDataPreviousPageUpdatedPageNumber;
	}


	public void setDutMeterDataPreviousPageUpdatedPageNumber(int dutMeterDataPreviousPageUpdatedPageNumber) {
		this.dutMeterDataPreviousPageUpdatedPageNumber = dutMeterDataPreviousPageUpdatedPageNumber;
	}

	public String getDeviceNameWithRackID(String input_rackid){
		ApplicationLauncher.logger.debug("getDeviceNameWithRackID: Entry");
		JSONArray meternames = getMeterNames();
		//if(Integer.parseInt(input_rackid)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
		//	input_rackid = String.valueOf(Integer.parseInt(input_rackid) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
		//}
		input_rackid = convertRackIdForReportExportMode(input_rackid);
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: input_rackid: " + input_rackid);
		//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: meternames: "+ meternames);
		String device_rack_id = "";

		JSONObject result_json = new JSONObject();
		String  device_with_rack = "";
		String rack_id = "";
		try {
			for(int i=0; i<meternames.length(); i++){
				result_json = meternames.getJSONObject(i);
				device_with_rack = result_json.getString("error_value");
				//ApplicationLauncher.logger.debug("getDeviceNameWithRackID: device_with_rack: "+ device_with_rack);
				String[] test_params = device_with_rack.split("/");
				rack_id = test_params[1];
				if(rack_id.equals(input_rackid)){
					device_rack_id = device_with_rack;
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDeviceNameWithRackID: JSONException: "+e.getMessage());
		}
		return device_rack_id;
	}

	public String convertRackIdForReportExportMode(String inputRackId){
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
			if(Integer.parseInt(inputRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
				String convertedRackId = String.valueOf(Integer.parseInt(inputRackId) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
				return convertedRackId;

			}
		}
		return inputRackId;
	}

	public void setMeterNames(JSONArray value){
		meterNames = value;
	}

	public JSONArray getMeterNames(){
		return meterNames;
	}

	public boolean isIndividualMeterReportSelected() {
		return individualMeterReportSelected;
	}

	public void setIndividualMeterReportSelected(boolean individualMeterReportSelected) {
		this.individualMeterReportSelected = individualMeterReportSelected;
	}

	public String getSelectedMeterSerialNo() {
		return selectedMeterSerialNo;
	}

	public void setSelectedMeterSerialNo(String selectedMeterSerialNo) {
		this.selectedMeterSerialNo = selectedMeterSerialNo;
	}

	public void fillSerialNoColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillSerialNoColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		for(int i = 1; i <= maxValue; i++ ){
			//serialNoCellPosition = row_pos +  column_pos;
			FillReportDataColumnXSSF_V1_1(sheet1, String.valueOf(i),row_pos, column_pos);
			row_pos++;
		}
	}

	/*	public void fillRackPositionColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		for(int i = 1; i <= maxValue; i++ ){
			//serialNoCellPosition = row_pos +  column_pos;
			FillReportDataColumnXSSF_V1_1(sheet1, String.valueOf(i),row_pos, column_pos);
			row_pos++;
		}
	}*/

	public boolean FillReportDataColumnXSSF_V1_1(XSSFSheet sheet1, String resultData, int row_pos,int column_pos){
		boolean status = false;
		try {

			//int inpRowPosition = getRowValueFromCellValue(cellPosition);
			//int column_pos = getColValueFromCellValue(cellPosition);
			//ApplicationLauncher.logger.debug("FillReportDataColumnXSSF_V1_1: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			//int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;

				applySelectedPrintStyle(column,"",ConstantReportV2.PRINT_STYLE_RESULT_TYPE_NONE);

				/*CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					//font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					//font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					//estyle.setFont(font);
										style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);

					PrintStyle selectedPrintStyle = getLoadedPrintStyle();

					//ApplicationLauncher.logger.debug("FillReportDataColumnXSSF_V1_1: getReportPrintStyleName: " + selectedPrintStyle.getReportPrintStyleName());

					font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
					font.setFontName(selectedPrintStyle.getFontName());//"Courier New");					
					font.setBold(selectedPrintStyle.isBold());							

					//style.setFont(font);

					style.setWrapText(selectedPrintStyle.isWrapText());
					if(selectedPrintStyle.isHorizontalAlignmentCentre()){
						style.setAlignment(HorizontalAlignment.CENTER);
					}
					if(selectedPrintStyle.isVerticalAlignmentCentre()){
						style.setVerticalAlignment(VerticalAlignment.CENTER);
					}
					if(selectedPrintStyle.isBorder()){
						style.setBorderTop(BorderStyle.THIN);
						style.setBorderRight(BorderStyle.THIN);
						style.setBorderBottom(BorderStyle.THIN);
						style.setBorderLeft(BorderStyle.THIN);
					}
					

					style.setFont(font);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillMeterColumnXSSF: Exception FontSetting: " + e.getMessage());

				}
*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
			}

			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportDataColumnXSSF_V1_1: Exception2:"+e.getMessage());
		}

		return status;
	}


	public boolean FillReportPageStatusColumnXSSF_V1_1(XSSFSheet sheet1, String resultData, int row_pos,int column_pos){
		boolean status = false;
		try {

			//int inpRowPosition = getRowValueFromCellValue(cellPosition);
			//int column_pos = getColValueFromCellValue(cellPosition);
			//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			//int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;

				
				applySelectedPrintStyle(column,resultData,ConstantReportV2.PRINT_STYLE_RESULT_TYPE_PAGE_STATUS);

/*				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					//font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					//font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					//estyle.setFont(font);
										style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);

					PrintStyle selectedPrintStyle = getLoadedPrintStyle();

					//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: getReportPrintStyleName: " + selectedPrintStyle.getReportPrintStyleName());

					font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
					font.setFontName(selectedPrintStyle.getFontName());//"Courier New");					
					font.setBold(selectedPrintStyle.isBold());							

					//style.setFont(font);

					style.setWrapText(selectedPrintStyle.isWrapText());
					if(selectedPrintStyle.isHorizontalAlignmentCentre()){
						style.setAlignment(HorizontalAlignment.CENTER);
					}
					if(selectedPrintStyle.isVerticalAlignmentCentre()){
						style.setVerticalAlignment(VerticalAlignment.CENTER);
					}
					if(selectedPrintStyle.isBorder()){
						style.setBorderTop(BorderStyle.THIN);
						style.setBorderRight(BorderStyle.THIN);
						style.setBorderBottom(BorderStyle.THIN);
						style.setBorderLeft(BorderStyle.THIN);
					}


					if(selectedPrintStyle.getResultPageStatusFailColorHighLightEnabled()){
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test1");
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: resultData: " + resultData);
						if(resultData.equals(ConstantReport.REPORT_POPULATE_FAIL)){
							//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
							String backGroundColor = selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
								style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
								//style.setFillForegroundColor(IndexedColors.RED.getIndex());
								//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							}
							String backGroundFillPattern = selectedPrintStyle.getResultPageStatusFailBackGroundFillPattern();

							if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
								style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
							}
							String textColor = selectedPrintStyle.getResultPageStatusFailTextFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
								font.setColor(IndexedColors.valueOf(textColor).getIndex());
							}
							//font.setColor(IndexedColors.WHITE.getIndex());
							//style.setFont(font);
						}
					}

					if(selectedPrintStyle.isResultPageStatusErrorColorHighLightEnabled()){
						if(resultData.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
							//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
							String backGroundColor = selectedPrintStyle.getResultPageStatusErrorBackGroundFailedColor();;//"BLUE";//selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
								style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
								//style.setFillForegroundColor(IndexedColors.RED.getIndex());


							}
							String backGroundFillPattern = selectedPrintStyle.getResultPageStatusErrorBackGroundFillPattern();

							if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
								style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
							}
							String textColor = selectedPrintStyle.getResultPageStatusErrorTextFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
								font.setColor(IndexedColors.valueOf(textColor).getIndex());
							}
							//font.setColor(IndexedColors.WHITE.getIndex());
							//style.setFont(font);
							font.setItalic(true);
						}
					}

					style.setFont(font);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillReportPageStatusColumnXSSF_V1_1: Exception FontSetting: " + e.getMessage());

				}*/

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportPageStatusColumnXSSF_V1_1: Exception2:"+e.getMessage());
			}

			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportPageStatusColumnXSSF_V1_1: Exception2:"+e.getMessage());
		}

		return status;
	}


	public boolean FillReportOverAllStatusColumnXSSF_V1_1(XSSFSheet sheet1, String resultData, int row_pos,int column_pos){
		boolean status = false;
		try {

			//int inpRowPosition = getRowValueFromCellValue(cellPosition);
			//int column_pos = getColValueFromCellValue(cellPosition);
			//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Entry ");
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			//JSONObject result_json = new JSONObject();
			//String rack_id = "";
			//String device_rack_id = "";
			//List<String> uniqueDeviceName= new ArrayList<String>();
			//int row_pos = inpRowPosition;
			//for(int i=0; i<filteredResultData.size(); i++){
			try{
				Row row = sheet1.getRow(row_pos);

				if(row == null){
					row = sheet1.createRow(row_pos);

				}


				Cell column = row.getCell(column_pos);
				if(column == null){
					column = sheet1.getRow(row_pos).createCell(column_pos);
				}

				//ApplicationLauncher.logger.info("FillMeterColumnXSSF_V2: getDutSerialNo: " + result.get(i).getDutSerialNo());
				column.setCellValue(resultData); 
				row_pos++;

				applySelectedPrintStyle(column,resultData,ConstantReportV2.PRINT_STYLE_RESULT_TYPE_OVER_ALL_STATUS);


/*				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					//font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					//font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					//estyle.setFont(font);
										style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);

					PrintStyle selectedPrintStyle = getLoadedPrintStyle();

					//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: getReportPrintStyleName: " + selectedPrintStyle.getReportPrintStyleName());

					font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
					font.setFontName(selectedPrintStyle.getFontName());//"Courier New");					
					font.setBold(selectedPrintStyle.isBold());							

					//style.setFont(font);

					style.setWrapText(selectedPrintStyle.isWrapText());
					if(selectedPrintStyle.isHorizontalAlignmentCentre()){
						style.setAlignment(HorizontalAlignment.CENTER);
					}
					if(selectedPrintStyle.isVerticalAlignmentCentre()){
						style.setVerticalAlignment(VerticalAlignment.CENTER);
					}
					if(selectedPrintStyle.isBorder()){
						style.setBorderTop(BorderStyle.THIN);
						style.setBorderRight(BorderStyle.THIN);
						style.setBorderBottom(BorderStyle.THIN);
						style.setBorderLeft(BorderStyle.THIN);
					}


					if(selectedPrintStyle.getResultOverAllStatusFailColorHighLightEnabled()){
						//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test1");
						//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: resultData: " + resultData);
						if(resultData.equals(ConstantReport.REPORT_POPULATE_FAIL)){
							//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test2");
							String backGroundColor = selectedPrintStyle.getResultOverAllStatusFailBackGroundFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
								style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
								//style.setFillForegroundColor(IndexedColors.RED.getIndex());
								//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							}
							String backGroundFillPattern = selectedPrintStyle.getResultOverAllStatusFailBackGroundFillPattern();

							if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
								style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
							}
							String textColor = selectedPrintStyle.getResultOverAllStatusFailTextFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
								font.setColor(IndexedColors.valueOf(textColor).getIndex());
							}
							//font.setColor(IndexedColors.WHITE.getIndex());
							//style.setFont(font);
						}
					}

					if(selectedPrintStyle.isResultOverAllStatusErrorColorHighLightEnabled()){
						if(resultData.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
							//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test2");
							String backGroundColor = selectedPrintStyle.getResultOverAllStatusErrorBackGroundFailedColor();;//"BLUE";//selectedPrintStyle.getResultOverAllStatusFailBackGroundFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
								style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
								//style.setFillForegroundColor(IndexedColors.RED.getIndex());


							}
							String backGroundFillPattern = selectedPrintStyle.getResultOverAllStatusErrorBackGroundFillPattern();

							if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
								style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
							}
							String textColor = selectedPrintStyle.getResultOverAllStatusErrorTextFailedColor();
							if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
								font.setColor(IndexedColors.valueOf(textColor).getIndex());
							}
							//font.setColor(IndexedColors.WHITE.getIndex());
							//style.setFont(font);
							font.setItalic(true);
						}
					}

					style.setFont(font);
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillReportOverAllStatusColumnXSSF_V1_1: Exception FontSetting: " + e.getMessage());

				}
*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("FillReportOverAllStatusColumnXSSF_V1_1: Exception2:"+e.getMessage());
			}

			//}
			status = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReportOverAllStatusColumnXSSF_V1_1: Exception2:"+e.getMessage());
		}

		return status;
	}
	
	
	public void applySelectedPrintStyle(Cell column,String resultDataOrStatus, String resultType){
		ApplicationLauncher.logger.debug("applySelectedPrintStyle : Entry");
		
		try{
			CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
			XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
			//font.setFontHeightInPoints((short)25);
			//font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
			//font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

			//estyle.setFont(font);
			/*					style.setBorderTop(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);*/
			//style.setBorderBottom(BorderStyle.THIN);

			PrintStyle selectedPrintStyle = getLoadedPrintStyle();

			//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: getReportPrintStyleName: " + selectedPrintStyle.getReportPrintStyleName());

			font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
			font.setFontName(selectedPrintStyle.getFontName());//"Courier New");					
			font.setBold(selectedPrintStyle.isBold());							

			//style.setFont(font);

			style.setWrapText(selectedPrintStyle.isWrapText());
			if(selectedPrintStyle.isHorizontalAlignmentCentre()){
				style.setAlignment(HorizontalAlignment.CENTER);
			}
			if(selectedPrintStyle.isVerticalAlignmentCentre()){
				style.setVerticalAlignment(VerticalAlignment.CENTER);
			}
			if(selectedPrintStyle.isBorder()){
				style.setBorderTop(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);
				style.setBorderBottom(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
			}
			if(resultType.equals(ConstantReportV2.PRINT_STYLE_RESULT_TYPE_VALUE)){
				if(selectedPrintStyle.getResultValueFailColorHighLightEnabled()){
				//if(selectedPrintStyle.getResultValueFailColorHighLightEnabled()){
					ApplicationLauncher.logger.debug("applySelectedPrintStyle: Test Result value : Test1");
					//ApplicationLauncher.logger.debug("FillErrorValueXSSF: test_status: <" + test_status + "> ->  " + ConstantReport.REPORT_POPULATE_FAIL);
					if(((ConstantReport.RESULT_STATUS_FAIL.contains(resultDataOrStatus) && (resultDataOrStatus.length()==1)))
						|| (resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)) 
						|| (resultDataOrStatus.equals(ConstantReport.RESULT_STATUS_FAIL)) )  {
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
						ApplicationLauncher.logger.debug("applySelectedPrintStyle: Test Result value : Test2");
						String backGroundColor = selectedPrintStyle.getResultValueFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
							//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						
						String backGroundFillPattern = selectedPrintStyle.getResultValueFailBackGroundFillPattern();
						
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						
						String textColor = selectedPrintStyle.getResultValueFailTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
					}
					
				}
				
				if(selectedPrintStyle.isResultValueErrorColorHighLightEnabled()){
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
						String backGroundColor = selectedPrintStyle.getResultValueErrorBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());		
						}
						String backGroundFillPattern = selectedPrintStyle.getResultValueErrorBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultValueErrorTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						
						font.setItalic(true);
					}
				}
				
			}else if(resultType.equals(ConstantReportV2.PRINT_STYLE_RESULT_TYPE_STATUS)){
				if(selectedPrintStyle.getResultStatusFailColorHighLightEnabled()){
					ApplicationLauncher.logger.debug("applySelectedPrintStyle: Test Result status : Test3");
				///if(selectedPrintStyle.getResultStatusFailColorHighLightEnabled()){
					//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test1");
					//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: test_status: <" + test_status + "> ->  " + ConstantReport.REPORT_POPULATE_FAIL);
					//if ((resultDataOrStatus.equals(ConstantReport.RESULT_STATUS_FAIL))
					//	|| (resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)) )  {
					if(((ConstantReport.RESULT_STATUS_FAIL.contains(resultDataOrStatus) && (resultDataOrStatus.length()==1)))
							|| (resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)) 
							|| (resultDataOrStatus.equals(ConstantReport.RESULT_STATUS_FAIL)) )  {
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
						ApplicationLauncher.logger.debug("applySelectedPrintStyle: Test Result status : Test4");
						String backGroundColor = selectedPrintStyle.getResultStatusFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						
						String backGroundFillPattern = selectedPrintStyle.getResultStatusFailBackGroundFillPattern();
						
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						
						String textColor = selectedPrintStyle.getResultStatusFailTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
					}
				}
				
				if(selectedPrintStyle.isResultStatusErrorColorHighLightEnabled()){
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
						String backGroundColor = selectedPrintStyle.getResultStatusErrorBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());		
						}
						String backGroundFillPattern = selectedPrintStyle.getResultStatusErrorBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultStatusErrorTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						
						font.setItalic(true);
					}
				}
				
				
			}else if(resultType.equals(ConstantReportV2.PRINT_STYLE_RESULT_TYPE_OVER_ALL_STATUS)){
			//if(overAllStatusType){
				if(selectedPrintStyle.getResultOverAllStatusFailColorHighLightEnabled()){
					//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test1");
					//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: resultData: " + resultData);
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
						//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test2");
						String backGroundColor = selectedPrintStyle.getResultOverAllStatusFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
							//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						String backGroundFillPattern = selectedPrintStyle.getResultOverAllStatusFailBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultOverAllStatusFailTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
					}
				}
	
				if(selectedPrintStyle.isResultOverAllStatusErrorColorHighLightEnabled()){
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
						//ApplicationLauncher.logger.debug("FillReportOverAllStatusColumnXSSF_V1_1: Test2");
						String backGroundColor = selectedPrintStyle.getResultOverAllStatusErrorBackGroundFailedColor();;//"BLUE";//selectedPrintStyle.getResultOverAllStatusFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
	
	
						}
						String backGroundFillPattern = selectedPrintStyle.getResultOverAllStatusErrorBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultOverAllStatusErrorTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
						font.setItalic(true);
					}
			
				}
			}else if(resultType.equals(ConstantReportV2.PRINT_STYLE_RESULT_TYPE_PAGE_STATUS)){
			
				if(selectedPrintStyle.getResultPageStatusFailColorHighLightEnabled()){
					//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test1");
					//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: resultData: " + resultData);
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_FAIL)){
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
						String backGroundColor = selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
							//style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						}
						String backGroundFillPattern = selectedPrintStyle.getResultPageStatusFailBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultPageStatusFailTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
					}
				}
	
				if(selectedPrintStyle.isResultPageStatusErrorColorHighLightEnabled()){
					if(resultDataOrStatus.equals(ConstantReport.REPORT_POPULATE_UNDEFINED)){
						//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
						String backGroundColor = selectedPrintStyle.getResultPageStatusErrorBackGroundFailedColor();;//"BLUE";//selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
							style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
							//style.setFillForegroundColor(IndexedColors.RED.getIndex());
	
	
						}
						String backGroundFillPattern = selectedPrintStyle.getResultPageStatusErrorBackGroundFillPattern();
	
						if(ConstantReportV2.FILL_PATTERN_LIST.contains(backGroundFillPattern)){
							style.setFillPattern(FillPatternType.valueOf(backGroundFillPattern));//FillPatternType.ALT_BARS);
						}
						String textColor = selectedPrintStyle.getResultPageStatusErrorTextFailedColor();
						if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
							font.setColor(IndexedColors.valueOf(textColor).getIndex());
						}
						//font.setColor(IndexedColors.WHITE.getIndex());
						//style.setFont(font);
						font.setItalic(true);
					}
				}
	
			}

			style.setFont(font);
			column.setCellStyle(style);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("applySelectedPrintStyle: Exception FontSetting: " + e.getMessage());

		}

	}


	public List<String> fillMakeColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMakeColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				//rack_id = arr.getJSONObject(i).getString("Rack_ID");
				rack_id = String.valueOf(arr.getJSONObject(i).getInt("Rack_ID"));
				meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillMakeColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMakeColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillMakeColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMakeColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMakeColumnXSSF: col:"+col);

		return col;
	}


	public List<String> fillModelNoColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterModelNo = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterModelNo = arr.getJSONObject(i).getString("meter_model_no");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : meterModelNo: " + meterModelNo);
				ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillModelNoColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterModelNo,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillModelNoColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillModelNoColumnXSSF: col:"+col);

		return col;
	}

	public boolean isRackidFromMeterColSameXSSF(XSSFSheet sheet1, int row_pos, int column_pos, String InpRackId, boolean currentTP_IsExportMode){

		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: row_pos: " + row_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column_pos: " + column_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: rack_id: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: currentTP_IsExportMode: " + currentTP_IsExportMode);
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//String rack_id = convertRackIdForReportExportMode(InpRackId);
		String rack_id = InpRackId;
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: InpRackId: " + InpRackId);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: rack_id2: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column: " + column);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: row_pos: " + row_pos);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: column_pos: " + column_pos);
		String value = column.getStringCellValue();
		ApplicationLauncher.logger.info("isRackidFromMeterColSame: value: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test1" );
			String rack = test_params[1];
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: rack: " + rack);
			if(rack_id.equals(rack)){
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test2" );
				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test3" );
					if(currentTP_IsExportMode){
						if(test_params[0].contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
							//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test4" );
							return true;
						}/*else if (Integer.parseInt(InpRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test4A" );
							return true;
						}*/else{
							//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test5" );
							return false;
						}
					}else {
						//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test6" );
						return true;
					}
				}else{
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test7" );
					return true;
				}
			}
			else{
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSame: test8" );
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSame: test_params.length: " + test_params.length);
			return false;
		}
	}

	public void fillCustomerReferenceNoColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCustomerReferenceNoColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		String customerRefNo = "";
		try {
			/*			JSONObject modeldata = getMeterProfileData();
			String ibValue = modeldata.getString("basic_current_ib");
			String iMaxValue = modeldata.getString("max_current_imax");
			String meterCapacityData = ibValue +"-" + iMaxValue + "A";*/

			customerRefNo = TestReportController.getSelectedCustomerRefNo();//;fetchCustomerReferenceNo();
			ApplicationLauncher.logger.debug("fillCustomerReferenceNoColumnXSSF: customerRefNo: " + customerRefNo);
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, customerRefNo,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCustomerReferenceNoColumnXSSF: Exception: "+e.getMessage());
		}
	}


	public void fillCapacityColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCapacityColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			JSONObject modeldata = getMeterProfileData();
			String ibValue = modeldata.getString("basic_current_ib");
			String iMaxValue = modeldata.getString("max_current_imax");
			String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				ApplicationLauncher.logger.debug("fillCapacityColumnXSSF: row_pos: " + row_pos);
				ApplicationLauncher.logger.debug("fillCapacityColumnXSSF: column_pos: " + column_pos);
				FillReportDataColumnXSSF_V1_1(sheet1, meterCapacityData,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCapacityColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public JSONObject getMeterProfileData() {
		return meterProfileData;
	}

	public void setMeterProfileData(JSONObject meterProfileData) {
		this.meterProfileData = meterProfileData;
	}


	public void fillMeterTypeColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillMeterTypeColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterType = modeldata.getString("model_type");
			for(int i = 1; i <= maxValue; i++ ){
			//for(int i = 1; i < maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterType,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterTypeColumnXSSF: Exception: "+e.getMessage());
		}
	}


	public void fillDutMetaDataWithParserColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition, String dutMetaDataParser){

		//ApplicationLauncher.logger.debug("fillDutMetaDataColumnXSSF: Entry ");
		ApplicationLauncher.logger.debug("fillDutMetaDataWithParserColumnXSSF: dutMetaDataParser: " + dutMetaDataParser);
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String dutMetaDataValue= modeldata.getString(dutMetaDataParser);
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, dutMetaDataValue,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillDutMetaDataWithParserColumnXSSF: Exception: "+e.getMessage());
		}
	}


	public String getDutMetaDataActiveReactiveEnergy(String dutMetaDataParser){

		//ApplicationLauncher.logger.debug("fillDutMetaDataColumnXSSF: Entry ");
		ApplicationLauncher.logger.debug("getDutMetaDataActiveReactiveEnergy: dutMetaDataParser: " + dutMetaDataParser);
		//String serialNoCellPosition = serialNoCellStartingPosition;
		//int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		//int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);
		String energyType = ConstantReportV2.POPULATE_ACTIVE_ENERGY;
		try {
			//sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String dutMetaDataValue= modeldata.getString(dutMetaDataParser);
			//for(int i = 1; i <= maxValue; i++ ){
			//serialNoCellPosition = row_pos +  column_pos;
			//	FillReportDataColumnXSSF_V1_1(sheet1, dutMetaDataValue,row_pos, column_pos);
			//	row_pos++;
			//}

			if(dutMetaDataValue.contains(ConstantApp.METERTYPE_ACTIVE)){
				energyType = ConstantReportV2.POPULATE_ACTIVE_ENERGY;
			}else if(dutMetaDataValue.contains(ConstantApp.METERTYPE_REACTIVE)){
				energyType = ConstantReportV2.POPULATE_REACTIVE_ENERGY;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillDutMetaDataWithParserColumnXSSF: Exception: "+e.getMessage());
		}

		return energyType;
	}



	public void fillDutMetaDataWithValueColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition, String dutMetaData){

		//ApplicationLauncher.logger.debug("fillDutMetaDataColumnXSSF: Entry ");
		ApplicationLauncher.logger.debug("fillDutMetaDataWithValueColumnXSSF: dutMetaData: " + dutMetaData);
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			//String dutMetaDataValue= modeldata.getString(dutMetaDataParser);
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, dutMetaData,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillDutMetaDataWithValueColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public List<String> fillMeterConstantColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterConstant = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				meterConstant = arr.getJSONObject(i).getString("meter_const");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : meterConstant: " + meterConstant);
				ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterConstantColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillMeterConstantColumnXSSF: col:"+col);

		return col;
	}

	public void fillMeterConstantFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillMeterConstantFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("impulses_per_unit");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterConstantFromMeterProfileColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public List<String> fillPtRatioColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String ptRatio = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				ptRatio = arr.getJSONObject(i).getString("ptr_ratio");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : ptRatio: " + ptRatio);
				ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, ptRatio,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillPtRatioColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillPtRatioColumnXSSF: col:"+col);

		return col;
	}

	public void fillPtRatioFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillPtRatioFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("ptr_ratio");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillPtRatioFromMeterProfileColumnXSSF: Exception: "+e.getMessage());
		}
	}

	public List<String> fillCtRatioColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String ctRatio = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				ctRatio = arr.getJSONObject(i).getString("ctr_ratio");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : ctRatio: " + ctRatio);
				ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, ctRatio,row_pos, column_pos);
					noOfDatapopulated++;
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCtRatioColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF: col:"+col);

		return col;
	}

	public List<String> fillRackPositionColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, 
			int populateMeterProfileDataIterationCount){
		ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());
		//int dutCount =0;
		try {

			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";

			String rack_id = "";
			String deviceWithRackId="";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			int noOfDatapopulated = 0;
			ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				deviceWithRackId = device_name + "/" + rack_id;
				//ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : ctRatio: " + ctRatio);
				ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				//ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : row_pos: " + row_pos);
				//ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : column_pos: " + column_pos);
				//ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : meter_col: " + meter_col);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : Filled Index: " + i);
					FillReportDataColumnXSSF_V1_1(sheet1, rack_id,row_pos, column_pos);
					noOfDatapopulated++;
					row_pos++;
				}
				if(noOfDatapopulated >= populateMeterProfileDataIterationCount){
					ApplicationLauncher.logger.debug("fillRackPositionColumnXSSF : hit break: ");
					break;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillRackPositionColumnXSSF: JSONException: "+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("fillCtRatioColumnXSSF: col:"+col);

		return col;
	}



	public void fillCtRatioFromMeterProfileColumnXSSF(XSSFSheet sheet1, int maxValue, String serialNoCellStartingPosition){

		ApplicationLauncher.logger.debug("fillCtRatioFromMeterProfileColumnXSSF: Entry ");
		//String serialNoCellPosition = serialNoCellStartingPosition;
		int row_pos = getRowValueFromCellValue(serialNoCellStartingPosition);
		int column_pos = getColValueFromCellValue(serialNoCellStartingPosition);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject modeldata = getMeterProfileData();
			String meterConstant = modeldata.getString("ctr_ratio");
			for(int i = 1; i <= maxValue; i++ ){
				//serialNoCellPosition = row_pos +  column_pos;
				FillReportDataColumnXSSF_V1_1(sheet1, meterConstant,row_pos, column_pos);
				row_pos++;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillCtRatioFromMeterProfileColumnXSSF: Exception: "+e.getMessage());

		}
	}


	public JSONObject FilterResultByTestType(JSONObject result, String test_alias_name){

		//ApplicationLauncher.logger.debug("FilterResultByTestType: input result: "+ result.toString());
		JSONObject filter_json = new JSONObject();
		JSONArray FilteredData = new JSONArray();
		try {
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";

			String TestType = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");

				String[] test_params = test_case_name.split("_");
				TestType = test_params[0];
				if((TestType.contains(test_alias_name))){
					FilteredData.put(result_json);	
				}/*else if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
					if((TestType.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME+test_alias_name))){
						FilteredData.put(result_json);
					}
				}*/
			}
			filter_json.put("No_of_results", FilteredData.length());
			filter_json.put("Results", FilteredData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterResultByTestType: JSONException:"+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("FilterResultByTestType: filter_json: "+ filter_json.toString());
		return filter_json;
	}


	public JSONObject FilterResultForCustomTest(JSONObject result, String inpCustomTestName){
		ApplicationLauncher.logger.debug("FilterResultForCustomTest: Entry ");
		//ApplicationLauncher.logger.debug("FilterResultForCustomTest: input result: "+ result.toString());
		JSONObject filter_json = new JSONObject();
		JSONArray FilteredData = new JSONArray();
		try {
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";

			String TestType = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");

				//String[] test_params = test_case_name.split("_");
				//TestType = test_params[0];
				//ApplicationLauncher.logger.debug("FilterResultByTestType:    test_case_name: " + test_case_name );
				//ApplicationLauncher.logger.debug("FilterResultByTestType: inpCustomTestName: " + inpCustomTestName );
				
				//if((test_case_name.equals(inpCustomTestName))){
				if((test_case_name.startsWith(inpCustomTestName+ ConstantReportV2.TEST_POINT_NAME_SEPERATOR))){
					FilteredData.put(result_json);	
				}/*else if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
					if((TestType.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME+test_alias_name))){
						FilteredData.put(result_json);
					}
				}*/
			}
			filter_json.put("No_of_results", FilteredData.length());
			filter_json.put("Results", FilteredData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterResultByTestType: JSONException:"+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("FilterResultByTestType: filter_json: "+ filter_json.toString());
		return filter_json;
	}


	public void fillACCErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		//int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		//for(int i=0; i<currents.size();i++){
		filter_result = FilterACCResults(voltageFilter,currentFilter,pfFilter,result );
		//FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);
		//start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		//}



	}


	public void fillAccStatusCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		//int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		//for(int i=0; i<currents.size();i++){
		filter_result = FilterACCResults(voltageFilter,currentFilter,pfFilter,result );
		//FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
		//FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);xbcdvfb
		fillResultStatusXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);
		//start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		//}



	}


	public JSONArray FilterACCResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterACCResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];

				/*ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfVolt: " + selectedRateOfVolt);

				ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfCurrent: " + selectedRateOfCurrent);

				ApplicationLauncher.logger.debug("FilterACCResults: lag_lead: " + lag_lead);*/

				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterACCResults: test_case_name: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterACCResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}


	public JSONArray FilterCustomTestResults(JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterCustomTestResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			//String selectedRateOfVolt = "";
			//String lag_lead = "";
			//String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				//testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);

				//String[] test_params = testname_wo_type.split("-");
				//selectedRateOfVolt = test_params[0];
				//lag_lead = test_params[1];
				//selectedRateOfCurrent = test_params[2];

				/*ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfVolt: " + selectedRateOfVolt);

				ApplicationLauncher.logger.debug("FilterACCResults: selectedRateOfCurrent: " + selectedRateOfCurrent);

				ApplicationLauncher.logger.debug("FilterACCResults: lag_lead: " + lag_lead);*/

				//if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
				//		(lag_lead.equals(pf))){
				ApplicationLauncher.logger.debug("FilterCustomTestResults: test_case_name: "+test_case_name);
				FilteredData.put(result_json);	
				//}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterCustomTestResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}


	@SuppressWarnings("deprecation")
	public void FillErrorValueXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		//ApplicationLauncher.logger.debug("FillErrorValueXSSF: filter_result: " + filter_result);

		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			boolean isRackIdMatching = false;
			if (filter_result.length()>0){
				//int valuePopulatedInExcelCount = 0;
				//for(int i=0; i<filter_result.length(); i++){
				for(int i=0; i<filter_result.length() || (i <getSerialNumberMaxCount()); i++){	//when result count is less than total meter loaded count - data not populated added logic here 
					XSSFRow row = sheet1.getRow(row_pos);
					if(row == null){
						row = sheet1.createRow(row_pos);
					}
					XSSFCell column = row.getCell(column_pos);

					for(int j=0; j<filter_result.length()  ; j++){
						//	for(int j=0; j<filter_result.length() || (valuePopulatedInExcelCount <filter_result.length()) ; j++){	
						//for(int j=0; valuePopulatedInExcelCount<=filter_result.length(); j++){	
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");
						//ApplicationLauncher.logger.info("FillErrorValueXSSF: device_name:"+device_name);
						//ApplicationLauncher.logger.info("FillErrorValueXSSF: filter_result.length():"+filter_result.length());
						exportMode = false;
						if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
							//exportModeDeviceName = "";
							//test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: test_case_name:"+test_case_name);

							//if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

							exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name2:"+device_name);
							if(!device_name.equals(exportModeDeviceName)){
								exportMode = true;
								device_name= exportModeDeviceName;
							}
							//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							//}
						}
						/*						if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
							test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueXSSF: test_case_name:"+test_case_name);

							if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

								device_name = getRealDeviceIDForExportMode( device_name);
								//ApplicationLauncher.logger.info("FillErrorValueXSSF: device_name2:"+device_name);
								exportMode = true;
								//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							}
						}*/
						//ApplicationLauncher.logger.debug("FillErrorValueXSSF: device_name0: " + device_name);
						//ApplicationLauncher.logger.debug("FillErrorValueXSSF: test1: " );
						isRackIdMatching = isRackidFromMeterColSameXSSFV2(sheet1, row_pos, meter_col, device_name,exportMode);
						ApplicationLauncher.logger.debug("FillErrorValueXSSF: isRackIdMatching: " + isRackIdMatching );
						
						if(isRackIdMatching){
							//if(isRackidFromMeterColSameExportModeHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){
							ApplicationLauncher.logger.debug("FillErrorValueXSSF: isRackidFromMeterColSameXSSFV2: Same Entry " );

							//ApplicationLauncher.logger.debug("FillErrorValueXSSF: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if(OPERATION_PROCESS_ENABLED){
								test_status = result_json.getString("test_status");
								updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);
								updateRepeatAverageOperationProcessData(device_name,error_value,test_status);
								updateSelfHeatAverageOperationProcessData(device_name,error_value,test_status);
								//getTargetOperationProcessData().getResultValueHashMap().put(device_name, error_value);
								//getTargetOperationProcessData().getResultStatusHashMap().put(device_name, test_status);

							}
							if (ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							ApplicationLauncher.logger.debug("FillErrorValueXSSF: device_name2: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}
							//column.setCellType(Cell.CELL_TYPE_NUMERIC);
							//column.setCellType(CellType.NUMERIC);
							column.setCellValue(error_value); 	
							/*if(OPERATION_PROCESS_ENABLED){
								if(ConstantReport.RESULT_STATUS_PASS.contains((test_status))){
									test_status = ConstantReport.REPORT_POPULATE_PASS;
								}else if(ConstantReport.RESULT_STATUS_FAIL.contains((test_status))){
									test_status = ConstantReport.REPORT_POPULATE_FAIL;
								}
							}*/
							CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
							//valuePopulatedInExcelCount++;
							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("FillErrorValueXSSF: test_status1: <"+test_status+">");
								if(test_status.contains("P")){
									/*									XSSFCellStyle style = new XSSFCellStyle(new StylesTable());
								    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
								    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = sheet1.createCellStyle(); 
									/*									XSSFCellStyle curStyle = (XSSFCellStyle) column.getCellStyle();

							        curStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        curStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//XSSFCell curCell = row.getCell(column_pos);//row.getCell(partNumberColumn);
									//CellStyle style = column.getCellStyle();
									/*							        CellStyle style = null ;
							        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//curCell.setCellStyle(curStyle);
									/*									XSSFWorkbook wb = new XSSFWorkbook();
									XSSFCellStyle style = wb.createCellStyle();
									style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
									style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = 
									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										//style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");

											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionP FontSetting: " + e.getMessage());

										}*/
										//column.
										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionP StyleSetting: " + e.getMessage());

									}
									//wb.close();
								}
							}

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_FAIL){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("FillErrorValueXSSF: test_status2: <"+test_status+">");
								if(test_status.contains("F")){


									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										//style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");
											//font.setColor(IndexedColors.WHITE.getIndex());
											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionF FontSetting: " + e.getMessage());

										}*/

										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("FillErrorValueXSSF: ExceptionF StyleSetting: " + e.getMessage());

									}

								}
							}
							test_status = result_json.getString("test_status");
							validateForMeterOvellFailStatus( test_status,  device_name);
							
							applySelectedPrintStyle(column,test_status,ConstantReportV2.PRINT_STYLE_RESULT_TYPE_VALUE);
							
/*							try{
								XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
								//font.setFontHeightInPoints((short)25);
								font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
								font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

								font.setBold(true);
								style.setWrapText(true);
								style.setAlignment(HorizontalAlignment.CENTER);
								//HorizontalAlignment data = HorizontalAlignment.CENTER;
								style.setFont(font);
								style.setBorderTop(BorderStyle.THIN);
								style.setBorderRight(BorderStyle.THIN);
								style.setBorderBottom(BorderStyle.THIN);
								style.setBorderLeft(BorderStyle.THIN);

								PrintStyle selectedPrintStyle = getLoadedPrintStyle();

								font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
								font.setFontName(selectedPrintStyle.getFontName());//"Courier New");

								font.setBold(selectedPrintStyle.isBold());							

								style.setFont(font);

								style.setWrapText(selectedPrintStyle.isWrapText());
								if(selectedPrintStyle.isHorizontalAlignmentCentre()){
									style.setAlignment(HorizontalAlignment.CENTER);
								}
								if(selectedPrintStyle.isVerticalAlignmentCentre()){
									style.setVerticalAlignment(VerticalAlignment.CENTER);
								}
								if(selectedPrintStyle.isBorder()){
									style.setBorderTop(BorderStyle.THIN);
									style.setBorderRight(BorderStyle.THIN);
									style.setBorderBottom(BorderStyle.THIN);
									style.setBorderLeft(BorderStyle.THIN);
								}


								//if(selectedPrintStyle.getResultPageStatusFailColorHighLightEnabled()){
								if(selectedPrintStyle.getResultValueFailColorHighLightEnabled()){
									//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test1");
									//ApplicationLauncher.logger.debug("FillErrorValueXSSF: test_status: <" + test_status + "> ->  " + ConstantReport.REPORT_POPULATE_FAIL);
									if(test_status.equals(ConstantReport.RESULT_STATUS_FAIL)){
										//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
										String backGroundColor = selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
										if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
											style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
											//style.setFillForegroundColor(IndexedColors.RED.getIndex());
											style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										}
										String textColor = selectedPrintStyle.getResultPageStatusFailTextFailedColor();
										if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
											font.setColor(IndexedColors.valueOf(textColor).getIndex());
										}
										//font.setColor(IndexedColors.WHITE.getIndex());
										//style.setFont(font);
									}
								}



								//style.setBorderBottom(BorderStyle.THIN);
								column.setCellStyle(style);
							}catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("FillErrorValueXSSF: Exception FontSetting: " + e.getMessage());

							}*/
							//row_pos++;
						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("FillErrorValueXSSF: Exception: " + e1.getMessage());

		}
	}

	private void updateTargetOperationProcessInternalInputData(String deviceName, String errorValue, String testStatus) {
		// TODO Auto-generated method stub
		String deviceWithRackId = getDeviceNameWithRackID(deviceName);;//"";//new String(device_name);
		//ApplicationLauncher.logger.debug("updateTargetOperationProcessInternalInputData: deviceWithRackId: " +deviceWithRackId);
		if(getTargetOperationProcessInternalInputData()!=null){
			getTargetOperationProcessInternalInputData().getResultValueHashMap().put(deviceWithRackId, errorValue);
			getTargetOperationProcessInternalInputData().getResultStatusHashMap().put(deviceWithRackId, testStatus);
		}
	}

	private void updateRepeatAverageOperationProcessData(String deviceName, String errorValue, String testStatus) {
		// TODO Auto-generated method stub
		String deviceWithRackId = getDeviceNameWithRackID(deviceName);;//"";//new String(device_name);
		//if(!getRepeatAverageOperationProcessData().getOperationProcessKey().isEmpty()){
		if(getRepeatAggregatedAverageProcessData()!=null){
			ApplicationLauncher.logger.debug("updateRepeatAverageOperationProcessData: deviceWithRackId: " + deviceWithRackId + " -> " + errorValue + " " + testStatus);
			//if(getRepeatAverageOperationProcessData().getOperationProcessKey().equals(ConstantReportV2.PARAM_PROFILE_REPEAT_AVERAGE_KEY)){
			//ApplicationLauncher.logger.debug("updateRepeatAverageOperationProcessData: Hit1");
			//getRepeatAverageOperationProcessData().getResultValueHashMap().put(deviceWithRackId, errorValue);
			//getRepeatAverageOperationProcessData().getResultStatusHashMap().put(deviceWithRackId, testStatus);
			getRepeatAggregatedAverageProcessData().put(deviceWithRackId, errorValue);
			//}
		}
	}

	private void updateSelfHeatAverageOperationProcessData(String deviceName, String errorValue, String testStatus) {
		// TODO Auto-generated method stub
		String deviceWithRackId = getDeviceNameWithRackID(deviceName);;//"";//new String(device_name);


		if(getSelfHeatAggregatedAverageProcessData()!=null){
			//if(getSelfHeatAverageOperationProcessData().getOperationProcessKey().equals(ConstantReportV2.PARAM_PROFILE_SELF_HEAT_AVERAGE_KEY)){
			//getSelfHeatAverageOperationProcessData().getResultValueHashMap().put(deviceWithRackId, errorValue);
			//getSelfHeatAverageOperationProcessData().getResultStatusHashMap().put(deviceWithRackId, testStatus);

			getSelfHeatAggregatedAverageProcessData().put(deviceWithRackId, errorValue);
			//}
		}
	}



	public String getRealDeviceIDForExportMode( String device_name){
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){

			if(Integer.valueOf(device_name)>=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
				device_name = String.valueOf (Integer.valueOf(device_name) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
			}

		}
		return device_name;
	}
	
/*	public String getRealDeviceIDForExportMode( String device_name){
		if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){

			if(Integer.valueOf(device_name)>=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
				device_name = String.valueOf (Integer.valueOf(device_name) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD);
			}

		}
		device_name = getDeviceIdSerialNumberHashMap().get(device_name) + "/" + device_name;
		
		return device_name;
	}*/

		
	public void validateForMeterOvellFailStatus(String test_status, String rackId){
		//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: rackId: " + rackId);
		//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: filter_result: " + test_status);
		if(ConstantReport.RESULT_STATUS_FAIL.contains((test_status))){
			//ArrayList<String> uniqueMeterSerialNoList = new ArrayList<String>(); 
			//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit1");

			for (HashMap.Entry<String,String> hashMeterid : getMeterDeviceOverAllStatus().entrySet()){
				//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
				//                 ", Value = " + hashMeterid.getValue());
				//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit2");
				if(hashMeterid.getKey().endsWith("/" + rackId)){
					//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit3");
					updateMeterDeviceOverAllStatus(hashMeterid.getKey(),ConstantReport.REPORT_POPULATE_FAIL);
					updateMeterDevicePageStatus(String.valueOf(getPresentPageNumber()),hashMeterid.getKey(),ConstantReport.REPORT_POPULATE_FAIL);
					int pageStatusCount = fetchMeterDevicePageStatusCount(String.valueOf(getPresentPageNumber()),hashMeterid.getKey())+1;
					updateMeterDevicePageStatusCount(String.valueOf(getPresentPageNumber()),hashMeterid.getKey(),pageStatusCount);
					int overAllStatusCount = fetchMeterDeviceOverAllStatusCount(hashMeterid.getKey())+1;
					updateMeterDeviceOverAllStatusCount(hashMeterid.getKey(),overAllStatusCount);
					break;
				}
			}

		}else if(ConstantReport.RESULT_STATUS_PASS.contains((test_status))){
			//ArrayList<String> uniqueMeterSerialNoList = new ArrayList<String>(); 
			//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit1");

			for (HashMap.Entry<String,String> hashMeterid : getMeterDeviceOverAllStatus().entrySet()){
				//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
				//                 ", Value = " + hashMeterid.getValue());
				//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit2");
				if(hashMeterid.getKey().endsWith("/" + rackId)){
					//ApplicationLauncher.logger.debug("validateForMeterOvellFailStatus: Hit3");
					//updateMeterDeviceOverAllStatus(hashMeterid.getKey(),ConstantReport.REPORT_POPULATE_FAIL);
					//updateMeterDevicePageStatus(String.valueOf(getPresentPageNumber()),hashMeterid.getKey(),ConstantReport.REPORT_POPULATE_FAIL);
					int pageStatusCount = fetchMeterDevicePageStatusCount(String.valueOf(getPresentPageNumber()),hashMeterid.getKey())+1;
					updateMeterDevicePageStatusCount(String.valueOf(getPresentPageNumber()),hashMeterid.getKey(),pageStatusCount);
					int overAllStatusCount = fetchMeterDeviceOverAllStatusCount(hashMeterid.getKey())+1;
					updateMeterDeviceOverAllStatusCount(hashMeterid.getKey(),overAllStatusCount);
					break;
				}
			}

		}



	}



	public boolean isRackidFromMeterColSameXSSFV2(XSSFSheet sheet1, int row_pos, int column_pos, String InpRackId, boolean currentTP_IsExportMode){

		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: row_pos: " + row_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: column_pos: " + column_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: rack_id: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSame: currentTP_IsExportMode: " + currentTP_IsExportMode);
		Row row = sheet1.getRow(row_pos);
		if(row == null){
			row = sheet1.createRow(row_pos);
		}
		Cell column = row.getCell(column_pos);
		if(column == null){
			column = sheet1.getRow(row_pos).createCell(column_pos);
		}
		//String rack_id = convertRackIdForReportExportMode(InpRackId);
		String rack_id = InpRackId;
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: InpRackId: " + InpRackId);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: rack_id2: " + rack_id);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: column: " + column);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: row_pos: " + row_pos);
		//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: column_pos: " + column_pos);
		String value = column.getStringCellValue();
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: column_pos: " + column_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: row_pos: " + row_pos);
		//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: valueFromExcel: " + value);
		String[] test_params = value.split("/");
		if(test_params.length >1){
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test1" );
			String rack = test_params[1];
			//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: deviceNameFromExcel: " + rack);
			if(rack_id.equals(rack)){
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test2" );
				if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){ 
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test3" );
					/*					if(currentTP_IsExportMode){
						if(test_params[0].contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test4" );
							return true;
						}else if (Integer.parseInt(InpRackId)> ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test4A" );
							return true;
						}else{
							ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test5" );
							return false;
						}
					}else {*/
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test6" );
					return true;
					//}
				}else{
					//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test7" );
					return true;
				}
			}
			else{
				//ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2: test8" );
				return false;
			}
		}else{
			ApplicationLauncher.logger.debug("isRackidFromMeterColSameXSSFV2XSSF: value: " + value);
			//ApplicationLauncher.logger.info("isRackidFromMeterColSameXSSFV2: test_params.length: " + test_params.length);
			return false;
		}
	}


	/*	public void fillAccResultStatusCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current,String pf, int start_col, int row_pos, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);fgfdg
		JSONArray filter_result = FilterACCResults(voltage,current,pf, result );
		fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}
	 */
	public void fillCreepResultStatusCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int row_pos, int meter_col){
		ApplicationLauncher.logger.debug("fillCreepResultStatusCustomReportXSSF: Entry");
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}


	public void fillResultStatusXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos,int meter_col){
		ApplicationLauncher.logger.debug("fillResultStatusXSSF: filter_result: " + filter_result);
		sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
		try {
			JSONObject result_json = new JSONObject();
			String device_name = "";
			String error_value ="";
			String test_status = "";
			String test_case_name = "";
			Boolean exportMode = false;
			String exportModeDeviceName = "";
			if (filter_result.length()>0){
				for(int i=0; i<filter_result.length(); i++){
					XSSFRow row = sheet1.getRow(row_pos);
					if(row == null){
						row = sheet1.createRow(row_pos);
					}
					XSSFCell column = row.getCell(column_pos);

					for(int j=0; j<filter_result.length(); j++){
						result_json = filter_result.getJSONObject(j);
						device_name = result_json.getString("device_name");
						//ApplicationLauncher.logger.info("fillResultStatusXSSF: device_name:"+device_name);
						exportMode = false;
						if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
							//exportModeDeviceName = "";
							//test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: test_case_name:"+test_case_name);

							//if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

							exportModeDeviceName = getRealDeviceIDForExportMode( device_name);
							//ApplicationLauncher.logger.info("FillErrorValueHSSF: device_name2:"+device_name);
							if(!device_name.equals(exportModeDeviceName)){
								exportMode = true;
								device_name= exportModeDeviceName;
							}
							//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							//}
						}
						/*						if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
							test_case_name  = result_json.getString("test_case_name");
							//ApplicationLauncher.logger.info("fillResultStatusXSSF: test_case_name:"+test_case_name);

							if(test_case_name.contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){

								device_name = getRealDeviceIDForExportMode( device_name);
								//ApplicationLauncher.logger.info("fillResultStatusXSSF: device_name2:"+device_name);
								exportMode = true;
								//device_rack_id = getDeviceNameWithRackIDForExportMode(rack_id);
							}
						}*/
						if(isRackidFromMeterColSameXSSFV2(sheet1, row_pos, meter_col, device_name,exportMode)){
							//if(isRackidFromMeterColSameExportModeHSSF(sheet1, row_pos, meter_col, device_name,exportMode)){


							//ApplicationLauncher.logger.debug("fillResultStatusXSSF: result_json: " + result_json);
							error_value = result_json.getString("error_value");
							if (ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
								test_status = result_json.getString("test_status");
								error_value = test_status + " " +error_value;
								error_value = error_value.replace("N ","");
							}
							ApplicationLauncher.logger.debug("fillResultStatusXSSF: device_name: " + device_name+ " : "+ error_value);
							if(column == null){
								column = sheet1.getRow(row_pos).createCell(column_pos);
							}
							test_status = result_json.getString("test_status");
							if(ConstantReport.RESULT_STATUS_PASS.contains((test_status))){
								test_status = ConstantReport.REPORT_POPULATE_PASS;
							}else if(ConstantReport.RESULT_STATUS_FAIL.contains((test_status))){
								test_status = ConstantReport.REPORT_POPULATE_FAIL;
							}
							column.setCellValue(test_status); 	
							CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_PASS){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("fillResultStatusXSSF: test_status1: <"+test_status+">");
								if(test_status.contains("P")){
									/*									XSSFCellStyle style = new XSSFCellStyle(new StylesTable());
								    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
								    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = sheet1.createCellStyle(); 
									/*									XSSFCellStyle curStyle = (XSSFCellStyle) column.getCellStyle();

							        curStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        curStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//XSSFCell curCell = row.getCell(column_pos);//row.getCell(partNumberColumn);
									//CellStyle style = column.getCellStyle();
									/*							        CellStyle style = null ;
							        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());*/

									//curCell.setCellStyle(curStyle);
									/*									XSSFWorkbook wb = new XSSFWorkbook();
									XSSFCellStyle style = wb.createCellStyle();
									style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
									style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
									//CellStyle style = 
									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										//style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");

											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionP FontSetting: " + e.getMessage());

										}*/
										//column.
										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionP StyleSetting: " + e.getMessage());

									}
									//wb.close();
								}
							}

							if(ConstantAppConfig.REPORT_HIGHLIGHT_COLOR_FOR_FAIL){
								test_status = result_json.getString("test_status");
								ApplicationLauncher.logger.info("fillResultStatusXSSF: test_status2: <"+test_status+">");
								if(test_status.contains("F")){


									try{
										//CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
										//style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
										style.setFillForegroundColor(IndexedColors.RED.getIndex());
										style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										//style.setBorderBottom(BorderStyle.THIN);
										/*										try{
											XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
											//font.setFontHeightInPoints((short)25);
											font.setFontHeightInPoints((short)ConstantConfig.REPORT_FONT_SIZE);
											font.setFontName(ConstantConfig.REPORT_FONT_NAME);//"Courier New");
											//font.setColor(IndexedColors.WHITE.getIndex());
											style.setFont(font);
										}catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionF FontSetting: " + e.getMessage());

										}*/

										//column.setCellStyle(style);
									}catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										ApplicationLauncher.logger.error("fillResultStatusXSSF: ExceptionF StyleSetting: " + e.getMessage());

									}

								}
							}
							test_status = result_json.getString("test_status");
							validateForMeterOvellFailStatus( test_status,  device_name);
							applySelectedPrintStyle(column,test_status,ConstantReportV2.PRINT_STYLE_RESULT_TYPE_STATUS);
							
							
/*							try{
								XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
								//font.setFontHeightInPoints((short)25);
								font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
								font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

								style.setFont(font);
								style.setBorderTop(BorderStyle.THIN);
								style.setBorderRight(BorderStyle.THIN);
								style.setBorderBottom(BorderStyle.THIN);
								style.setBorderLeft(BorderStyle.THIN);
								//style.setBorderBottom(BorderStyle.THIN);

								PrintStyle selectedPrintStyle = getLoadedPrintStyle();

								font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
								font.setFontName(selectedPrintStyle.getFontName());//"Courier New");

								font.setBold(selectedPrintStyle.isBold());							

								style.setFont(font);

								style.setWrapText(selectedPrintStyle.isWrapText());
								if(selectedPrintStyle.isHorizontalAlignmentCentre()){
									style.setAlignment(HorizontalAlignment.CENTER);
								}
								if(selectedPrintStyle.isVerticalAlignmentCentre()){
									style.setVerticalAlignment(VerticalAlignment.CENTER);
								}
								if(selectedPrintStyle.isBorder()){
									style.setBorderTop(BorderStyle.THIN);
									style.setBorderRight(BorderStyle.THIN);
									style.setBorderBottom(BorderStyle.THIN);
									style.setBorderLeft(BorderStyle.THIN);
								}

								if(selectedPrintStyle.getResultStatusFailColorHighLightEnabled()){
									//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test1");
									//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: test_status: <" + test_status + "> ->  " + ConstantReport.REPORT_POPULATE_FAIL);
									if(test_status.equals(ConstantReport.RESULT_STATUS_FAIL)){
										//ApplicationLauncher.logger.debug("FillReportPageStatusColumnXSSF_V1_1: Test2");
										String backGroundColor = selectedPrintStyle.getResultPageStatusFailBackGroundFailedColor();
										if(ConstantReportV2.INDEX_COLOR_LIST.contains(backGroundColor)){
											style.setFillForegroundColor(IndexedColors.valueOf(backGroundColor).getIndex());
											//style.setFillForegroundColor(IndexedColors.RED.getIndex());
											style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
										}
										String textColor = selectedPrintStyle.getResultPageStatusFailTextFailedColor();
										if(ConstantReportV2.INDEX_COLOR_LIST.contains(textColor)){
											font.setColor(IndexedColors.valueOf(textColor).getIndex());
										}
										//font.setColor(IndexedColors.WHITE.getIndex());
										//style.setFont(font);
									}
								}
								column.setCellStyle(style);
							}catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ApplicationLauncher.logger.error("fillResultStatusXSSF: Exception FontSetting: " + e.getMessage());

							}*/

						}
					}
					row_pos++;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillResultStatusXSSF: JSONException: " + e.getMessage());

		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ApplicationLauncher.logger.error("fillResultStatusXSSF: Exception: " + e1.getMessage());

		}
	}

	public JSONArray FilterCREEPResults(String voltage, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterCREEPResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";


			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				if((selectedRateOfVolt.equals(voltage))){
					ApplicationLauncher.logger.debug("FilterCREEPResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterCREEPResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}

	public JSONArray convertProcessedOutputToResultFormat(HashMap<String, String> inpResultHashMap, boolean populateErrorValue){
		JSONArray resultJsonList = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormat: Entry ");
			//JSONArray result_arr = result.getJSONArray("Results");
			//JSONObject resultJson = new JSONObject();
			//rackPositionId = ""; 
			inpResultHashMap.entrySet().forEach( e -> {
				ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormat: getKey " +e.getKey());
				ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormat: getValue " +e.getValue());

				try {

					String[] test_params = e.getKey().split("/");
					String rackPositionId = test_params[1];
					JSONObject resultJson = new JSONObject();
					resultJson.put("device_name",rackPositionId);
					ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormat: rackPositionId " + rackPositionId);
					if(populateErrorValue){

						resultJson.put("error_value",e.getValue());
						resultJson.put("test_status",ConstantReport.RESULT_STATUS_PASS);
						//resultJson.put("test_status",ConstantReport.RESULT_STATUS_FAIL);
					}else{
						resultJson.put("error_value","");
						resultJson.put("test_status",e.getValue());
					}
					resultJsonList.put(resultJson);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ApplicationLauncher.logger.error("convertProcessedOutputToResultFormat: Exception1: "+e1.getMessage());
				}
			});

			//String test_case_name = "";


			/*			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				if((selectedRateOfVolt.equals(voltage))){
					ApplicationLauncher.logger.debug("FilterCREEPResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("convertProcessedOutputToResultFormat: Exception2: "+e.getMessage());
		}
		return resultJsonList;
	}


	public JSONArray convertProcessedOutputToResultFormatV2(HashMap<String, String> inpResultValueHashMap,			
			HashMap<String, String> inpResultStatusHashMap	){
		JSONArray resultJsonList = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormatV2: Entry ");
			//JSONArray result_arr = result.getJSONArray("Results");
			//JSONObject resultJson = new JSONObject();
			//rackPositionId = ""; 
			inpResultValueHashMap.entrySet().forEach( e -> {
				ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormatV2: getKey " +e.getKey());
				ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormatV2: getValue " +e.getValue());

				try {

					String[] test_params = e.getKey().split("/");
					String rackPositionId = test_params[1];
					JSONObject resultJson = new JSONObject();
					resultJson.put("device_name",rackPositionId);
					ApplicationLauncher.logger.debug("convertProcessedOutputToResultFormatV2: rackPositionId " + rackPositionId);
					//if(populateErrorValue){

					resultJson.put("error_value",e.getValue());
					resultJson.put("test_status",inpResultStatusHashMap.get(e.getKey()));//ConstantReport.RESULT_STATUS_PASS);
					//}else{
					//	resultJson.put("error_value","");
					//	resultJson.put("test_status",e.getValue());
					//}
					resultJsonList.put(resultJson);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					ApplicationLauncher.logger.error("convertProcessedOutputToResultFormatV2: Exception1: "+e1.getMessage());
				}
			});

			//String test_case_name = "";


			/*			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				if((selectedRateOfVolt.equals(voltage))){
					ApplicationLauncher.logger.debug("FilterCREEPResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("convertProcessedOutputToResultFormatV2: Exception2: "+e.getMessage());
		}
		return resultJsonList;
	}

	public JSONObject GetResultsFromDB(String datatype){
		ApplicationLauncher.logger.debug("GetResultsFromDB: Entry");

		JSONObject result = new JSONObject();
		String project_name = TestReportController.getProjectListCurrentValue();//get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";
		ApplicationLauncher.logger.debug("GetResultsFromDB: project_name: "+ project_name);

		result = MySQL_Controller.sp_get_completed_result_data(TestReportController.getResultFilterMctNctMode(),datatype,TestReportController.getSelectedDeploymentID());

		ApplicationLauncher.logger.debug("GetResultsFromDB:"+result);
		return result;
	}

	public void fillCreepErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, int start_col, int row_pos, int meter_col){
		ApplicationLauncher.logger.debug("fillCreepErrorValueCustomReportXSSF: Entry");
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCREEPResults(voltage,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}


	public void fillCustomTestErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result, 
			int start_col, int row_pos, int meter_col){
		ApplicationLauncher.logger.debug("fillCustomTestErrorValueCustomReportXSSF: Entry");
		ApplicationLauncher.logger.debug("fillCustomTestErrorValueCustomReportXSSF: result: " + result);
		JSONArray filter_result = FilterCustomTestResults(result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}


	public void fillProcessedOutputDataXSSF(XSSFSheet sheet1, HashMap<String, String> resultData, 
			int start_col, int row_pos, int meter_col,boolean populateErrorValue){
		//JSONObject result = GetResultsFromDB(datatype);
		//JSONArray filter_result = FilterCREEPResults(voltage,result );
		//fillProcessedOutPutDataXSSF(sheet1, filter_result,row_pos, start_col, meter_col);

		ApplicationLauncher.logger.debug("fillProcessedOutPutDataXSSF: Entry");
		JSONArray filter_result = convertProcessedOutputToResultFormat(resultData,populateErrorValue);


		if(populateErrorValue){
			FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
		}else{
			fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);;
		}


	}


	public void fillProcessedOutputDataXSSF_V2(XSSFSheet sheet1, HashMap<String, String> resultData, 
			HashMap<String, String> resultStatus, 
			int start_col, int row_pos, int meter_col){
		//JSONObject result = GetResultsFromDB(datatype);
		//JSONArray filter_result = FilterCREEPResults(voltage,result );
		//fillProcessedOutPutDataXSSF(sheet1, filter_result,row_pos, start_col, meter_col);

		ApplicationLauncher.logger.debug("fillProcessedOutputDataXSSF_V2: Entry");
		JSONArray filter_result = convertProcessedOutputToResultFormatV2(resultData,resultStatus);


		//if(populateErrorValue){
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
		//}else{
		//	fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);;
		//}


	}
	public void fillSTA_ErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col,int row_pos, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public void fillSTA_ResultStatusCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage,String current, int start_col, int row_pos,int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		result = FilterResultByTestType(result, ConstantApp.STA_ALIAS_NAME);//Changed
		//ApplicationLauncher.logger.debug("fillSTA_ResultStatusCustomReportXSSF: result: " + result);
		JSONArray filter_result = FilterSTAResults(voltage, current,result );
		//ApplicationLauncher.logger.debug("fillSTA_ResultStatusCustomReportXSSF: filter_result: " + filter_result);
		fillResultStatusXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public JSONArray FilterSTAResults(String voltage, String current, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.info("FilterSTAResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");

			JSONObject result_json = new JSONObject();
			String test_case_name = "";


			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				//ApplicationLauncher.logger.info("FilterVUResults: testname_wo_type : " + testname_wo_type);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				selectedRateOfCurrent = test_params[1];
				//ApplicationLauncher.logger.info("FilterVUResults: selectedRateOfVolt: " + selectedRateOfVolt);
				//ApplicationLauncher.logger.info("FilterVUResults: voltage: " + voltage);



				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current))){
					ApplicationLauncher.logger.debug("FilterSTAResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterSTAResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}

	public List<String> fillMeterOverAllStatusColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : meter_col: "+ meter_col);
		//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : dataCellStartingPosition: "+ dataCellStartingPosition);
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : arr length: " + arr.length());
			boolean rackIdMatchFoundWithRowHeader = false;
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				rackIdMatchFoundWithRowHeader =  isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode);
				//if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
				if(rackIdMatchFoundWithRowHeader){
					ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/


					for (HashMap.Entry<String,String> hashMeterid : getMeterDeviceOverAllStatus().entrySet()){
						//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
						//                 ", Value = " + hashMeterid.getValue());
						if(hashMeterid.getKey().equals(deviceWithRackId)){
							//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: deviceWithRackId: " + deviceWithRackId);
							//ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: hashMeterid.getValue(): " + hashMeterid.getValue());
							//FillReportDataColumnXSSF_V1_1(sheet1, hashMeterid.getValue(),row_pos, column_pos);
							FillReportOverAllStatusColumnXSSF_V1_1(sheet1, hashMeterid.getValue(),row_pos, column_pos);
							break;
						}
					}
					//FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterOverAllStatusColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMeterOverAllStatusColumnXSSF: col:"+col);

		return col;
	}






	public void manipulateComplyStatus( ){
		ApplicationLauncher.logger.debug("manipulateComplyStatus : Entry");

		setComplyStatus(ConstantReportV2.POPULATE_DOES_NOT_COMPLY);
		for (HashMap.Entry<String,String> hashMeterid : getMeterDeviceOverAllStatus().entrySet()){
			if(hashMeterid.getValue().equals(ConstantReport.REPORT_POPULATE_FAIL)){
				ApplicationLauncher.logger.debug("manipulateComplyStatus : does not comply");
				return;
			}

		}

		ApplicationLauncher.logger.debug("manipulateComplyStatus : Complied");
		setComplyStatus(ConstantReportV2.POPULATE_COMPLY);

	}


	public List<String> fillMeterPageStatusColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col, int pageNumber){
		ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		//ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : meter_col: "+ meter_col);
		//ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : dataCellStartingPosition: "+ dataCellStartingPosition);
		ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : arr length: " + arr.length());
			boolean rackIdMatchFoundWithRowHeader = false;
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				deviceWithRackId = device_name + "/" + rack_id;
				//ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : meterMake: " + meterMake);
				ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				rackIdMatchFoundWithRowHeader =  isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode);
				//if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
				if(rackIdMatchFoundWithRowHeader){
					ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/


					for (HashMap.Entry<String,String> hashMeterid : getMeterDevicePageStatus().get(String.valueOf(pageNumber)).entrySet()){
						//ApplicationLauncher.logger.debug("Key = " + hashMeterid.getKey() +
						//                 ", Value = " + hashMeterid.getValue());
						if(hashMeterid.getKey().equals(deviceWithRackId)){
							//ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF: deviceWithRackId: " + deviceWithRackId);
							//ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF: hashMeterid.getValue(): " + hashMeterid.getValue());
							//FillReportDataColumnXSSF_V1_1(sheet1, hashMeterid.getValue(),row_pos, column_pos);
							FillReportPageStatusColumnXSSF_V1_1(sheet1, hashMeterid.getValue(),row_pos, column_pos);
							break;
						}
					}
					//FillReportDataColumnXSSF_V1_1(sheet1, meterMake,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("fillMeterPageStatusColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("fillMeterPageStatusColumnXSSF: col:"+col);

		return col;
	}


	public void fillSelfHeatRepeatabilityErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col, String resultIterationId){

		//int start_col = ConstantReport.ACC_TEMPL_DEF_I_COLS.get(section);
		JSONArray filter_result = new JSONArray ();
		//for(int i=0; i<currents.size();i++){
		//filter_result = FilterACCResults(voltageFilter,currentFilter,pfFilter,result );
		filter_result = FilterSelfHeatRepResults(voltageFilter, currentFilter, pfFilter,resultIterationId,result );
		//FillHeaderXSSF(sheet1, currents.get(i),row_pos-1, start_col);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);
		//start_col = start_col+ ConstantReport.ACC_SKIP_COL_COUNT;
		//}



	}

	public JSONArray FilterSelfHeatRepResults(String voltage, String current,
			String pf, String reading_no, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			JSONArray result_arr = result.getJSONArray("Results");
			String psuedo_name = getREPPseudoName(voltage, current,pf, reading_no);
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				//ApplicationLauncher.logger.debug("FilterSelfHeatResults: test_case_name : " + test_case_name);

				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				ApplicationLauncher.logger.debug("FilterSelfHeatResults: RG: testname_wo_type : " + testname_wo_type);

				if(psuedo_name.equals(testname_wo_type)){
					ApplicationLauncher.logger.debug("FilterSelfHeatResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterSelfHeatRepResults: JSONException:"+e.getMessage());
		}
		return FilteredData;
	}

	public String getREPPseudoName(String voltage, String current,
			String pf, String reading_no){
		String psuedo_name = voltage + "-" + pf + "-" + current + "-" + reading_no;
		ApplicationLauncher.logger.debug("getREPPseudoName: "+psuedo_name);
		return psuedo_name;
	}



	public void fillVoltageVariationErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterVVResults(voltageFilter,currentFilter,pfFilter,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}


	public void fillRpsNormalErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		//filter_result = FilterVVResults(voltageFilter,currentFilter,pfFilter,result );
		filter_result = FilterRPSResults(voltageFilter,currentFilter,pfFilter,result,ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public void fillRpsReverseErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterRPSResults(voltageFilter,currentFilter,pfFilter,result,ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public void fillFreqTestErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col, String freqFilter){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterSelfHeatRepResults(voltageFilter,
				currentFilter,pfFilter,freqFilter,result );
		//ApplicationLauncher.logger.debug("fillFreqTestErrorValueCustomReportXSSF : filter_result: " + filter_result);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public void fillVoltUnbalanceErrorValueCustomReportXSSF(XSSFSheet sheet1, JSONObject result,
			String voltageUnbalanceFilter, String currentFilter, String pfFilter,int col_pos,
			int row_pos, int meter_col ){

		JSONArray filter_result = new JSONArray ();
		filter_result = FilterVUResults(voltageUnbalanceFilter,//ConstantReport.VU_TEMPL_DEF_VOLT,
				currentFilter,pfFilter,result);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, col_pos, meter_col);

	}

	public JSONArray FilterVUResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterVUResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];
				ApplicationLauncher.logger.debug("FilterVUResults: selectedRateOfVolt: " + selectedRateOfVolt);
				ApplicationLauncher.logger.debug("FilterVUResults: voltage: " + voltage);



				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterVUResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterVUResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public void fillVVErrorValueXSSF(XSSFSheet sheet1, JSONObject result,
			ArrayList<String> volts,String current, String pf,int sec_no,
			int row_pos, int meter_col){
		String volt = ConstantReport.VV_TEMPL_DEFAULT_VOLT;

		int start_col = ConstantReport.VV_TEMPL_DEF_VOLT_COLS.get(sec_no);
		JSONArray filter_result = FilterVVResults(volt,current,pf,result );
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
		start_col = start_col+ ConstantReport.VV_REF_SKIP_COL_COUNT;
		for(int i=0; i<volts.size();i++){
			filter_result = FilterVVResults(volts.get(i),current,pf,result );
			FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
			start_col = start_col+ ConstantReport.VV_SKIP_COL_COUNT;
		}
	}

	public JSONArray FilterVVResults(String voltage, String current,
			String pf, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterVVResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				ApplicationLauncher.logger.debug("FilterVVResults: test_case_name: " + test_case_name);
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];
				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf))){
					ApplicationLauncher.logger.debug("FilterVVResults: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterVVResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public JSONArray FilterRPSResults(String voltage, String current,
			String pf, JSONObject result, String test_type){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterRPSResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			String selectedRateOfVolt = "";
			String lag_lead = "";
			String selectedRateOfCurrent = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");


				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				ApplicationLauncher.logger.debug("FilterRPSResults: testname_wo_type : " + testname_wo_type);

				String[] test_params = testname_wo_type.split("-");
				selectedRateOfVolt = test_params[0];
				lag_lead = test_params[1];
				selectedRateOfCurrent = test_params[2];


				if((selectedRateOfVolt.equals(voltage)) && (selectedRateOfCurrent.equals(current)) &&
						(lag_lead.equals(pf)) && (test_case_name.contains(test_type))){
					FilteredData.put(result_json);	
					ApplicationLauncher.logger.debug("FilterRPSResults: FilteredData:  "+FilteredData);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterRPSResults: JSONException: " + e.getMessage());
		}
		return FilteredData;
	}

	public String getConstPseudoName(String voltage, String current,
			String pf,String sel_power){
		String testcasename = voltage + "-" + pf + "-" + current + "-" + sel_power;
		ApplicationLauncher.logger.info("getConstPseudoName: testcasename: "+testcasename);
		return testcasename;
	}

	public JSONArray FilterCONSTResults(String voltage, String current,
			String pf,String sel_power, JSONObject result){
		JSONArray FilteredData = new JSONArray();
		try {
			ApplicationLauncher.logger.debug("FilterCONSTResults: Entry ");
			JSONArray result_arr = result.getJSONArray("Results");
			String pseudo_name = getConstPseudoName(voltage, current, pf, sel_power);

			JSONObject result_json = new JSONObject();
			String test_case_name = "";
			String testname_wo_type = "";
			for(int i=0; i<result_arr.length(); i++){
				result_json = result_arr.getJSONObject(i);
				test_case_name = result_json.getString("test_case_name");
				testname_wo_type = test_case_name.substring(test_case_name.indexOf("-") + 1);
				//ApplicationLauncher.logger.info("FilterCONSTResults: test_case_name : " + test_case_name);

				//ApplicationLauncher.logger.debug("FilterCONSTResults: testname_wo_type : " + testname_wo_type);
				//ApplicationLauncher.logger.debug("FilterCONSTResults: pseudo_name : " + pseudo_name);

				if(pseudo_name.equals(testname_wo_type)){
					//ApplicationLauncher.logger.debug("FilterCONSTResults: test_case_name1: "+test_case_name);
					FilteredData.put(result_json);	
				}else if(pseudo_name.equals(testname_wo_type+".0")){
					//ApplicationLauncher.logger.debug("FilterCONSTResults: test_case_name2: "+test_case_name);
					FilteredData.put(result_json);	
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FilterCONSTResults: JSONException: "+e.getMessage());
		}
		return FilteredData;
	}
	public void fillConstErrorValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power,int row_pos, int start_col, int meter_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		FillErrorValueXSSF(sheet1, filter_result,row_pos, start_col, meter_col);
	}

	public String fillConstRefStValueCustomReportXSSF(XSSFSheet sheet1, String datatype, 
			String voltage, String current, String pf,String selected_power,int row_pos, int start_col){
		JSONObject result = GetResultsFromDB(datatype);
		JSONArray filter_result = FilterCONSTResults(voltage,current,pf,selected_power,result );
		filter_result = fetchLatestResult(filter_result);
		//ApplicationLauncher.logger.info("fillConstErrorValueCustomReportXSSF: filter_result: "+filter_result);
		String refStdValue = FillReferenceWithCellXSSF(sheet1, filter_result,row_pos, start_col);
		return refStdValue;
	}

	private JSONArray fetchLatestResult(JSONArray filter_result) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("fetchLatestResult: Entry: ");

		JSONArray reducedResultList = new JSONArray();
		JSONObject resultWithMaxResultIdJsonObj = new JSONObject();
		int maxResultId = 0;
		int presentResultId =0 ; 
		if(filter_result.length() <2 ){
			return filter_result;
		}else{

			try {
				resultWithMaxResultIdJsonObj = filter_result.getJSONObject(0);
				maxResultId = Integer.parseInt(resultWithMaxResultIdJsonObj.getString("result_id"));
				ApplicationLauncher.logger.debug("fetchLatestResult: maxResultId: " + maxResultId);
				for (int i=0; i < filter_result.length() ; i++){

					try {
						JSONObject result = filter_result.getJSONObject(i);
						presentResultId = Integer.parseInt(result.getString("result_id"));
						ApplicationLauncher.logger.debug("fetchLatestResult: presentResultId: " + presentResultId);
						if(presentResultId > maxResultId ){
							resultWithMaxResultIdJsonObj = filter_result.getJSONObject(i);
							maxResultId = presentResultId;
							ApplicationLauncher.logger.debug("fetchLatestResult: maxResultId2: " + maxResultId);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ApplicationLauncher.logger.error("fetchLatestResult: JSONException1: " + e.getMessage());
					}


				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ApplicationLauncher.logger.error("fetchLatestResult: JSONException2: " + e1.getMessage());
			}
		}
		ApplicationLauncher.logger.debug("fetchLatestResult: resultWithMaxResultIdJsonObj: with Max Id: " + resultWithMaxResultIdJsonObj);
		reducedResultList.put(resultWithMaxResultIdJsonObj);



		return reducedResultList;
	}

	public String FillReferenceWithCellXSSF(XSSFSheet sheet1, JSONArray filter_result, int row_pos,
			int column_pos){
		ApplicationLauncher.logger.debug("FillReferenceWithCellXSSF: filter_result: " + filter_result);

		String error_value = "";
		try {
			sheet1.addIgnoredErrors(new CellRangeAddress(0, ConstantAppConfig.REPORT_EXCEL_LAST_ROW, 0, ConstantAppConfig.REPORT_EXCEL_LAST_COLUMN), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
			JSONObject result_json = new JSONObject();
			String device_name = "";
			//			String error_value = "";
			String test_case_name = "";
			String exportDeviceName = "";
			//int row_pos = input_row_pos;
			Row row = sheet1.getRow(row_pos);
			Cell column = row.getCell(column_pos);
			for(int i=0; i<filter_result.length(); i++){///expectation is 2 results one for import and another one for export
				//row = sheet1.getRow(row_pos);
				//column = row.getCell(column_pos);
				//if(column == null){
				//	column = sheet1.getRow(row_pos).createCell(column_pos);
				//}
				//for(int j=0; j<filter_result.length(); j++){
				result_json = filter_result.getJSONObject(i);
				device_name = result_json.getString("device_name");
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: "+device_name);

				exportDeviceName = getRealDeviceIDForExportMode(device_name);
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: result_json: " + result_json);
				error_value = result_json.getString("error_value");
				//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp: device_name: " + device_name+ " : "+ error_value);
				if(device_name.equals(exportDeviceName)){//import mode
					if(Integer.valueOf(device_name)<=ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD){

						row = sheet1.getRow(row_pos);
						/*if(i==1){
								row = sheet1.getRow(row_pos-1);
							}*/
						column = row.getCell(column_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp :row_pos:"+row_pos);
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-imp :column_pos:"+column_pos);
						if(column == null){
							column = sheet1.getRow(row_pos).createCell(column_pos);
						}
						column.setCellValue(error_value); 
						if(OPERATION_PROCESS_ENABLED){
							//test_status = result_json.getString("test_status");
							String test_status = "";
							updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);

						}
					}
				}else{ //export mode
					if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
						//row = sheet1.getRow(row_pos+1);
						row = sheet1.getRow(row_pos);
						/*if(i==1){
								row = sheet1.getRow(row_pos);
							}*/
						//ApplicationLauncher.logger.info("FillReferenceWithCellHSSF-exp -hit:");
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-exp :row_pos:"+(row_pos+1));
						//ApplicationLauncher.logger.info("FillReferenceWithCellXSSF-exp :column_pos:"+column_pos);

						column = row.getCell(column_pos);
						if(column == null){
							//column = sheet1.getRow(row_pos+1).createCell(column_pos);
							column = sheet1.getRow(row_pos).createCell(column_pos);
						}
						column.setCellValue(error_value); 
						if(OPERATION_PROCESS_ENABLED){
							//test_status = result_json.getString("test_status");
							String test_status = "";
							updateTargetOperationProcessInternalInputData(device_name,error_value,test_status);

						}
					}
				}
				
				applySelectedPrintStyle(column,"",ConstantReportV2.PRINT_STYLE_RESULT_TYPE_NONE);

/*				CellStyle style = (XSSFCellStyle) column.getSheet().getWorkbook().createCellStyle();
				try{
					XSSFFont  font = (XSSFFont) column.getSheet().getWorkbook().createFont();										
					//font.setFontHeightInPoints((short)25);
					//font.setFontHeightInPoints((short)ConstantAppConfig.REPORT_FONT_SIZE);
					//font.setFontName(ConstantAppConfig.REPORT_FONT_NAME);//"Courier New");

					//estyle.setFont(font);
										style.setBorderTop(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					//style.setBorderBottom(BorderStyle.THIN);

					PrintStyle selectedPrintStyle = getLoadedPrintStyle();

					font.setFontHeightInPoints((short)selectedPrintStyle.getFontSize());
					font.setFontName(selectedPrintStyle.getFontName());//"Courier New");					
					font.setBold(selectedPrintStyle.isBold());							

					style.setFont(font);

					style.setWrapText(selectedPrintStyle.isWrapText());
					if(selectedPrintStyle.isHorizontalAlignmentCentre()){
						style.setAlignment(HorizontalAlignment.CENTER);
					}
					if(selectedPrintStyle.isVerticalAlignmentCentre()){
						style.setVerticalAlignment(VerticalAlignment.CENTER);
					}
					if(selectedPrintStyle.isBorder()){
						style.setBorderTop(BorderStyle.THIN);
						style.setBorderRight(BorderStyle.THIN);
						style.setBorderBottom(BorderStyle.THIN);
						style.setBorderLeft(BorderStyle.THIN);
					}
					column.setCellStyle(style);
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("FillMeterColumnXSSF: Exception FontSetting: " + e.getMessage());

				}*/
				//}
				//row_pos++;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("FillReferenceWithCellXSSF :JSONException: "+e.getMessage());
		}

		return error_value;
	}

	public List<String> removeRackIdInMeterSerialNoColumnXSSF( XSSFSheet sheet1, String dataCellStartingPosition, int meter_col){
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());

		try {
			//JSONObject modeldata = getMeterProfileData();

			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			//String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				//deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : device_name: " + device_name);
				//ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, device_name,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);

				}
				row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("removeRackIdInMeterSerialNoColumnXSSF: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF: col:"+col);

		return col;
	}

	public List<String> removeRackIdInMeterSerialNoColumnXSSF_V2( XSSFSheet sheet1, String dataCellStartingPosition, 
			int meter_col){
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : Entry");
		JSONObject resultjson = new JSONObject();
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : project_name: "+ TestReportController.getSelectedProjectName());
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : getSelectedDeployment_ID: "+ TestReportController.getSelectedDeploymentID());
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(TestReportController.getSelectedProjectName(),TestReportController.getSelectedDeploymentID());
		//int dutCount = maxDutDisplayRequestedPerPage;
		try {
			//JSONObject modeldata = getMeterProfileData();

			int no_of_devices = resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			//String StrippedDeviceName="";
			String rack_id = "";
			//String deviceWithRackId="";
			String meterMake = "";
			//String ibValue = modeldata.getString("basic_current_ib");
			//String iMaxValue = modeldata.getString("max_current_imax");
			//String meterCapacityData = ibValue +"-" + iMaxValue + "A";
			//String MakeAndCapacityData = "";
			boolean exportMode = false;
			int row_pos = getRowValueFromCellValue(dataCellStartingPosition);
			int column_pos = getColValueFromCellValue(dataCellStartingPosition);
			ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				rack_id = arr.getJSONObject(i).getString("Rack_ID");
				//meterMake = arr.getJSONObject(i).getString("meter_make");
				//MakeAndCapacityData = "";
				//StrippedDeviceName = FetchLastEightCharacter(device_name);
				//col.add(StrippedDeviceName + "/" + rack_id);
				//deviceWithRackId = device_name + "/" + rack_id;
				ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : device_name: " + device_name);
				//ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : deviceWithRackId: " + deviceWithRackId);
				if(isRackidFromMeterColSameXSSF(sheet1, row_pos, meter_col, rack_id,exportMode)){
					ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2 : Filled Index: " + i);
					/*					if(meterMake.isEmpty()){
						MakeAndCapacityData =  meterCapacityData;
					}else{
						MakeAndCapacityData = meterMake +  "/" + meterCapacityData;
					}*/
					FillReportDataColumnXSSF_V1_1(sheet1, device_name,row_pos, column_pos);
					//FillReportDataColumnXSSF_V1_1(sheet1, MakeAndCapacityData,row_pos, column_pos);
					row_pos++;
				}
				//row_pos++;


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("removeRackIdInMeterSerialNoColumnXSSF_V2: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("removeRackIdInMeterSerialNoColumnXSSF_V2: col:"+col);

		return col;
	}

	public OperationProcessJsonReadModel getOperationProcessDataModel() {
		return operationProcessDataModel;
	}

	public void setOperationProcessDataModel(OperationProcessJsonReadModel operationProcessDataModel) {
		this.operationProcessDataModel = operationProcessDataModel;
	}

	public OperationProcessDataJsonRead getTargetOperationProcessInternalInputData() {
		return targetOperationProcessInternalInputData;
	}

	public void setTargetOperationProcessInternalInputData(OperationProcessDataJsonRead targetOperationProcessData) {
		this.targetOperationProcessInternalInputData = targetOperationProcessData;
	}

	public List<String> getDutSerialNumberList() {
		return dutSerialNumberList;
	}

	public void setDutSerialNumberList(List<String> dutSerialNumberList) {
		this.dutSerialNumberList = dutSerialNumberList;
	}

	public OperationProcessDataJsonRead getTargetOperationProcessInternalOutputData() {
		return targetOperationProcessInternalOutputData;
	}

	public void setTargetOperationProcessInternalOutputData(
			OperationProcessDataJsonRead targetOperationProcessInternalOutputData) {
		this.targetOperationProcessInternalOutputData = targetOperationProcessInternalOutputData;
	}

	public OperationProcessDataJsonRead getTargetOperationProcessExternalOutputData() {
		return targetOperationProcessExternalOutputData;
	}

	public void setTargetOperationProcessExternalOutputData(
			OperationProcessDataJsonRead targetOperationProcessExternalOutputData) {
		this.targetOperationProcessExternalOutputData = targetOperationProcessExternalOutputData;
	}

	public int getPresentPageNumber() {
		return presentPageNumber;
	}

	public void setPresentPageNumber(int presentPageNumber) {
		this.presentPageNumber = presentPageNumber;
	}

	public int getMaxReportPageNumber() {
		return maxReportPageNumber;
	}

	public void setMaxReportPageNumber(int maxReportPageNumber) {
		this.maxReportPageNumber = maxReportPageNumber;
	}

	public PrintStyle getLoadedPrintStyle() {
		return loadedPrintStyle;
	}

	public void setLoadedPrintStyle(PrintStyle resultPrintStyle) {
		this.loadedPrintStyle = resultPrintStyle;
	}

	public String getUserSelectedPrintStyleResultName() {
		return userSelectedPrintStyleResultName;
	}

	public void setUserSelectedPrintStyleResultName(String resultPrintStyle) {
		this.userSelectedPrintStyleResultName = resultPrintStyle;
	}

	public String getUserSelectedPrintStyleGenericHeaderName() {
		return userSelectedPrintStyleGenericHeaderName;
	}

	public void setUserSelectedPrintStyleGenericHeaderName(String genericHeaderPrintStyle) {
		this.userSelectedPrintStyleGenericHeaderName = genericHeaderPrintStyle;
	}

	public String getUserSelectedPrintStyleTableHeaderName() {
		return userSelectedPrintStyleTableHeaderName;
	}

	public void setUserSelectedPrintStyleTableHeaderName(String tableHeaderPrintStyle) {
		this.userSelectedPrintStyleTableHeaderName = tableHeaderPrintStyle;
	}

	public String getComplyStatus() {
		return complyStatus;
	}

	public void setComplyStatus(String complyStatus) {
		this.complyStatus = complyStatus;
	}

	public JSONObject getDeployedDevicesJson() {
		return deployedDevicesJson;
	}

	public void setDeployedDevicesJson(JSONObject deployedDevicesJson) {
		this.deployedDevicesJson = deployedDevicesJson;
	}

	public JSONArray getDeployedTestCaseDetailsList() {
		return deployedTestCaseDetailsList;
	}

	public void setDeployedTestCaseDetailsList(JSONArray deployedTestCaseDetailsList) {
		this.deployedTestCaseDetailsList = deployedTestCaseDetailsList;
	}

	/*	public OperationProcessDataJsonRead getRepeatAverageOperationProcessData() {
		return repeatAverageOperationProcessData;
	}

	public void setRepeatAverageOperationProcessData(OperationProcessDataJsonRead repeatAverageOperationProcessData) {
		this.repeatAverageOperationProcessData = repeatAverageOperationProcessData;
	}

	public OperationProcessDataJsonRead getSelfHeatAverageOperationProcessData() {
		return selfHeatAverageOperationProcessData;
	}

	public void setSelfHeatAverageOperationProcessData(OperationProcessDataJsonRead selfHeatAverageOperationProcessData) {
		this.selfHeatAverageOperationProcessData = selfHeatAverageOperationProcessData;
	}*/

	/*	public static boolean isProcessRepeatAverageValue() {
		return processRepeatAverageValue;
	}

	public static void setProcessRepeatAverageValue(boolean processRepeatAverage) {
		ReportGeneration.processRepeatAverageValue = processRepeatAverage;
	}

	public static boolean isProcessSelfHeatAverageValue() {
		return processSelfHeatAverageValue;
	}

	public static void setProcessSelfHeatAverageValue(boolean processSelfHeatAverage) {
		ReportGeneration.processSelfHeatAverageValue = processSelfHeatAverage;
	}

	public static boolean isProcessRepeatAverageStatus() {
		return processRepeatAverageStatus;
	}

	public static boolean isProcessSelfHeatAverageStatus() {
		return processSelfHeatAverageStatus;
	}

	public static void setProcessRepeatAverageStatus(boolean processRepeatAverageStatus) {
		ReportGeneration.processRepeatAverageStatus = processRepeatAverageStatus;
	}

	public static void setProcessSelfHeatAverageStatus(boolean processSelfHeatAverageStatus) {
		ReportGeneration.processSelfHeatAverageStatus = processSelfHeatAverageStatus;
	}*/

	public MultiValuedMap<String, String> getRepeatAggregatedAverageProcessData() {
		return repeatAggregatedAverageProcessData;
	}

	public MultiValuedMap<String, String> getSelfHeatAggregatedAverageProcessData() {
		return selfHeatAggregatedAverageProcessData;
	}

	public void setRepeatAggregatedAverageProcessData(MultiValuedMap<String, String> repeatAverageProcessData) {
		this.repeatAggregatedAverageProcessData = repeatAverageProcessData;
	}

	public void setSelfHeatAggregatedAverageProcessData(MultiValuedMap<String, String> selfHeatAverageProcessData) {
		this.selfHeatAggregatedAverageProcessData = selfHeatAverageProcessData;
	}

	public void clearRepeatAggregatedAverageProcessData() {
		this.repeatAggregatedAverageProcessData = new ArrayListValuedHashMap<String,String>();;
	}

	public void clearSelfHeatAggregatedAverageProcessData() {
		this.selfHeatAggregatedAverageProcessData = new ArrayListValuedHashMap<String,String>();;
	}

	public HashMap<String, String> getResultRepeatAverageValueHashMap() {
		return resultRepeatAverageValueHashMap;
	}

	public HashMap<String, String> getResultRepeatAverageStatusHashMap() {
		return resultRepeatAverageStatusHashMap;
	}

	public HashMap<String, String> getResultSelfHeatAverageValueHashMap() {
		return resultSelfHeatAverageValueHashMap;
	}

	public HashMap<String, String> getResultSelfHeatAverageStatusHashMap() {
		return resultSelfHeatAverageStatusHashMap;
	}

	public void setResultRepeatAverageValueHashMap(HashMap<String, String> resultRepeatAverageValueHashMap) {
		this.resultRepeatAverageValueHashMap = resultRepeatAverageValueHashMap;
	}

	public void setResultRepeatAverageStatusHashMap(HashMap<String, String> resultRepeatAverageStatusHashMap) {
		this.resultRepeatAverageStatusHashMap = resultRepeatAverageStatusHashMap;
	}

	public void setResultSelfHeatAverageValueHashMap(HashMap<String, String> resultSelfHeatAverageValueHashMap) {
		this.resultSelfHeatAverageValueHashMap = resultSelfHeatAverageValueHashMap;
	}

	public void setResultSelfHeatAverageStatusHashMap(HashMap<String, String> resultSelfHeatAverageStatusHashMap) {
		this.resultSelfHeatAverageStatusHashMap = resultSelfHeatAverageStatusHashMap;
	}


	public void clearResultRepeatAverageValueHashMap() {
		resultRepeatAverageValueHashMap.clear();
	}

	public void clearResultRepeatAverageStatusHashMap() {
		resultRepeatAverageStatusHashMap.clear();
	}

	public void clearResultSelfHeatAverageValueHashMap() {
		resultSelfHeatAverageValueHashMap.clear();
	}

	public void clearResultSelfHeatAverageStatusHashMap() {
		resultSelfHeatAverageStatusHashMap.clear();
	}
	/*	public PrintStyle getHeaderPrintStyle() {
		return headerPrintStyle;
	}

	public void setHeaderPrintStyle(PrintStyle headerPrintStyle) {
		this.headerPrintStyle = headerPrintStyle;
	}*/

	/*	public ArrayList<OperationParam> getOperationParameterProfileDataList() {
		return operationParameterProfileDataList;
	}

	public void setOperationParameterProfileDataList(ArrayList<OperationParam> operationParameterProfileDataList) {
		this.operationParameterProfileDataList = operationParameterProfileDataList;
	}*/

	public static OperationParamService getReportOperationParamService() {
		return reportOperationParamService;
	}

	public static void setReportOperationParamService(OperationParamService reportOperationParamService) {
		ReportGeneration.reportOperationParamService = reportOperationParamService;
	}



	public Map<String, String> getRepeatAverageMaxErrorAllowedHashMap() {
		return repeatAverageMaxErrorAllowedHashMap;
	}

	public Map<String, String> getRepeatAverageMinErrorAllowedHashMap() {
		return repeatAverageMinErrorAllowedHashMap;
	}

	public Map<String, String> getSelfHeatAverageMaxErrorAllowedHashMap() {
		return selfHeatAverageMaxErrorAllowedHashMap;
	}

	public Map<String, String> getSelfHeatAverageMinErrorAllowedHashMap() {
		return selfHeatAverageMinErrorAllowedHashMap;
	}

	/*	public Map<String, String> getRepeatAverageFilterNameTestCasePreviewHashMap() {
		return repeatAverageFilterNameTestCasePreviewHashMap;
	}

	public Map<String, String> getSelfHeatAverageFilterNameTestCasePreviewHashMap() {
		return selfHeatAverageFilterNameTestCasePreviewHashMap;
	}*/

	public void setRepeatAverageMaxErrorAllowedHashMap(Map<String, String> repeatAverageMaxErrorAllowedHashMap) {
		this.repeatAverageMaxErrorAllowedHashMap = repeatAverageMaxErrorAllowedHashMap;
	}

	public void setRepeatAverageMinErrorAllowedHashMap(Map<String, String> repeatAverageMinErrorAllowedHashMap) {
		this.repeatAverageMinErrorAllowedHashMap = repeatAverageMinErrorAllowedHashMap;
	}

	public void setSelfHeatAverageMaxErrorAllowedHashMap(Map<String, String> selfHeatAverageMaxErrorAllowedHashMap) {
		this.selfHeatAverageMaxErrorAllowedHashMap = selfHeatAverageMaxErrorAllowedHashMap;
	}

	public void setSelfHeatAverageMinErrorAllowedHashMap(Map<String, String> selfHeatAverageMinErrorAllowedHashMap) {
		this.selfHeatAverageMinErrorAllowedHashMap = selfHeatAverageMinErrorAllowedHashMap;
	}



	public Map<String, Boolean> getConstantTestDutInitalAutoInsertionDataProcessedHashMap() {
		return constantTestDutInitalAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestDutFinalAutoInsertionDataProcessedHashMap() {
		return constantTestDutFinalAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestDutDiffAutoInsertionDataProcessedHashMap() {
		return constantTestDutDiffAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestDutErrorPercentAutoInsertionDataProcessedHashMap() {
		return constantTestDutErrorPercentAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestRsmInitalAutoInsertionDataProcessedHashMap() {
		return constantTestRsmInitalAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestRsmFinalAutoInsertionDataProcessedHashMap() {
		return constantTestRsmFinalAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestDutInitalAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestDutInitalAutoInsertionDataProcessedHashMap) {
		this.constantTestDutInitalAutoInsertionDataProcessedHashMap = constantTestDutInitalAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestDutFinalAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestDutFinalAutoInsertionDataProcessedHashMap) {
		this.constantTestDutFinalAutoInsertionDataProcessedHashMap = constantTestDutFinalAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestDutDiffAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestDutDiffAutoInsertionDataProcessedHashMap) {
		this.constantTestDutDiffAutoInsertionDataProcessedHashMap = constantTestDutDiffAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestDutErrorPercentAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestDutErrorPercentAutoInsertionDataProcessedHashMap) {
		this.constantTestDutErrorPercentAutoInsertionDataProcessedHashMap = constantTestDutErrorPercentAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestRsmInitalAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestRsmInitalAutoInsertionDataProcessedHashMap) {
		this.constantTestRsmInitalAutoInsertionDataProcessedHashMap = constantTestRsmInitalAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestRsmFinalAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestRsmFinalAutoInsertionDataProcessedHashMap) {
		this.constantTestRsmFinalAutoInsertionDataProcessedHashMap = constantTestRsmFinalAutoInsertionDataProcessedHashMap;
	}

	public Map<String, Boolean> getConstantTestRsmDiffAutoInsertionDataProcessedHashMap() {
		return constantTestRsmDiffAutoInsertionDataProcessedHashMap;
	}

	public void setConstantTestRsmDiffAutoInsertionDataProcessedHashMap(
			Map<String, Boolean> constantTestRsmDiffAutoInsertionDataProcessedHashMap) {
		this.constantTestRsmDiffAutoInsertionDataProcessedHashMap = constantTestRsmDiffAutoInsertionDataProcessedHashMap;
	}

	public Map<String, String> getConstantTestResultRsmDiffHashMap() {
		return constantTestResultRsmDiffHashMap;
	}

	public Map<String, String> getConstantTestMaxErrorAllowedHashMap() {
		return constantTestMaxErrorAllowedHashMap;
	}

	public Map<String, String> getConstantTestMinErrorAllowedHashMap() {
		return constantTestMinErrorAllowedHashMap;
	}

	public void setConstantTestResultRsmDiffHashMap(Map<String, String> constantTestRsmDiffHashMap) {
		this.constantTestResultRsmDiffHashMap = constantTestRsmDiffHashMap;
	}

	public void setConstantTestMaxErrorAllowedHashMap(Map<String, String> constantTestMaxErrorAllowedHashMap) {
		this.constantTestMaxErrorAllowedHashMap = constantTestMaxErrorAllowedHashMap;
	}

	public void setConstantTestMinErrorAllowedHashMap(Map<String, String> constantTestMinErrorAllowedHashMap) {
		this.constantTestMinErrorAllowedHashMap = constantTestMinErrorAllowedHashMap;
	}

	public static Map<String, Boolean> getProcessRepeatAverageValueHashMap() {
		return processRepeatAverageValueHashMap;
	}

	public static Map<String, Boolean> getProcessSelfHeatAverageValueHashMap() {
		return processSelfHeatAverageValueHashMap;
	}

	public static Map<String, Boolean> getProcessRepeatAverageStatusHashMap() {
		return processRepeatAverageStatusHashMap;
	}

	public static Map<String, Boolean> getProcessSelfHeatAverageStatusHashMap() {
		return processSelfHeatAverageStatusHashMap;
	}

	public static void setProcessRepeatAverageValueHashMap(Map<String, Boolean> processRepeatAverageValueHashMap) {
		ReportGeneration.processRepeatAverageValueHashMap = processRepeatAverageValueHashMap;
	}

	public static void setProcessSelfHeatAverageValueHashMap(Map<String, Boolean> processSelfHeatAverageValueHashMap) {
		ReportGeneration.processSelfHeatAverageValueHashMap = processSelfHeatAverageValueHashMap;
	}

	public static void setProcessRepeatAverageStatusHashMap(Map<String, Boolean> processRepeatAverageStatusHashMap) {
		ReportGeneration.processRepeatAverageStatusHashMap = processRepeatAverageStatusHashMap;
	}

	public static void setProcessSelfHeatAverageStatusHashMap(Map<String, Boolean> processSelfHeatAverageStatusHashMap) {
		ReportGeneration.processSelfHeatAverageStatusHashMap = processSelfHeatAverageStatusHashMap;
	}

	public Map<String, String> getConstantTestResultRsmInitialHashMap() {
		return constantTestResultRsmInitialHashMap;
	}

	public Map<String, String> getConstantTestResultRsmFinalHashMap() {
		return constantTestResultRsmFinalHashMap;
	}

	public void setConstantTestResultRsmInitialHashMap(Map<String, String> constantTestResultRsmInitialHashMap) {
		this.constantTestResultRsmInitialHashMap = constantTestResultRsmInitialHashMap;
	}

	public void setConstantTestResultRsmFinalHashMap(Map<String, String> constantTestResultRsmFinalHashMap) {
		this.constantTestResultRsmFinalHashMap = constantTestResultRsmFinalHashMap;
	}
	
	

	/*	public void setRepeatAverageFilterNameTestCasePreviewHashMap(
			Map<String, String> repeatAverageFilterNameTestCasePreviewHashMap) {
		this.repeatAverageFilterNameTestCasePreviewHashMap = repeatAverageFilterNameTestCasePreviewHashMap;
	}

	public void setSelfHeatAverageFilterNameTestCasePreviewHashMap(
			Map<String, String> selfHeatAverageFilterNameTestCasePreviewHashMap) {
		this.selfHeatAverageFilterNameTestCasePreviewHashMap = selfHeatAverageFilterNameTestCasePreviewHashMap;
	}
	 */
}
