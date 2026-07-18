package com.tasnetwork.calibration.conveyor.dutprocess;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.DevSysEnergyMeter;
import com.tasnetwork.calibration.conveyor.bay.NewlandQRCodeScanner;
import com.tasnetwork.calibration.conveyor.bay.calib.Calib;
import com.tasnetwork.calibration.conveyor.bay.ft.S073_Set_Serial_Numbers_On_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S074_Set_HardwareId_On_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S07_qR_Code_Scanning_of_Meters;
import com.tasnetwork.calibration.conveyor.bay.ft.S08_functional_Test;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.util.GUIUtils;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

public class DutControlProcess {

	// public static DeviceDataManagerController DisplayDataObj = new
	// DeviceDataManagerController();
	// public static SerialDataManager SerialDM_Obj = new SerialDataManager();

	private ArrayList<DutDataLogParse> refStdResult = new ArrayList<DutDataLogParse>();

	private volatile boolean dutProcessExecutionStarted = false;
	private volatile boolean dutProcessExecutionCompleted = false;
	private volatile int dutAddress = 0;
	// private volatile String dutSerialNo = "";

	private volatile String dutSerialNumber = "";

	// private Communicator serialPortObj = null;

	String error_min = "";
	String error_max = "";
	String error_min2 = "";
	String error_max2 = "";

	String testRunType = "";
	String dataValidationType = "";
	String dataValidationType2 = "";

	private volatile boolean skipCurrentTP_Execution = false;

	private String presentResultStatus = "";
	private String presentResultError = "";
	private int presentLagValidationDelay = 0;
	private int presentValidationDataValue = 0;
	private int presentValidation2DataValue = 0;

	/*
	 * private HashMap< Integer,String> presentResultStatusMap = new HashMap<>();
	 * private HashMap< Integer,String> presentResultErrorMap = new HashMap<>();
	 * private HashMap< Integer,Integer> presentLagValidationDelayMap = new
	 * HashMap<>();
	 * private HashMap< Integer,Integer> presentValidationDataValueMap = new
	 * HashMap<>();
	 * private HashMap< Integer,Integer> presentValidation2DataValueMap = new
	 * HashMap<>();
	 */

	private volatile boolean refStdLogResultsStopRequested = false;

	// private HashMap< Integer,String> dutSerialNumber = new HashMap<>();

	private HashMap<Integer, String> dutResultSummary = new HashMap<>();

	private ArrayList<ResultDataModel> selectedResultData = new ArrayList<ResultDataModel>();
	private ArrayList<DutDataLogParse> dataLogParsedResult = new ArrayList<DutDataLogParse>();

	// private HashMap< Integer,ArrayList<ResultDataModel>> selectedResultDataMap =
	// new HashMap<>();
	// private HashMap< Integer,ArrayList<DutDataLogParse>> dataLogParsedResultMap =
	// new HashMap< Integer,ArrayList<DutDataLogParse>>();

	/*
	 * String energyFlowMode = ConstantMtePowerSource.IMPORT_MODE;
	 * String executionMctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;
	 * 
	 * private DutCalibConfigModel calibParsedData = null;
	 */

	private int error_count = 1;

	public void processCalibMenuCommand(int lduReadAddress) {

		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: Entry");
		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: lduReadAddress : " + lduReadAddress);
		setDutAddress(lduReadAddress);

		setDutProcessExecutionCompleted(true);

		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public boolean executeDutCalibrationProcess(int lduReadAddress) {

		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			// status = devSysEnergyMeter.phaseCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public boolean executeDutPhaseCalibrationProcess(int lduReadAddress) {

		ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			// status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			status = devSysEnergyMeter.phaseCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public boolean executeDutNeutralCalibrationProcess(int lduReadAddress) {

		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			// status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			status = devSysEnergyMeter.neutralCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public boolean executeFtProcess(int lduReadAddress) {

		ApplicationLauncher.logger.info("Dut: executeFtProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		if (ProconFeatureEnable.FT_SOURCE_CONNECTED) {
			/*
			 * DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
			 * status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			 */

			S08_functional_Test s08_functional_Test = new S08_functional_Test();
			responseReturn = s08_functional_Test.functionalTestProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		status = (boolean) responseReturn.get("status");
		return status;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public Map<String, Object> executeCalibrationQrScanProcess(int lduReadAddress) {

		Map<String, Object> responseReturn = new HashMap<String, Object>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.HV_BAY_KEY);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if (scannedData != null && !scannedData.isEmpty()) {
				status = true;
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}

		}
		setDutProcessExecutionCompleted(true);

		return responseReturn;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public BayResponse executeFtQrScanProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();
		/*
		 * Map<String,Object> responseReturn = new HashMap<String,Object>();
		 * responseReturn.put("status", false);
		 * responseReturn.put("responseData", "");
		 * responseReturn.put("positionId", lduReadAddress);
		 */

		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.FT_BAY_KEY);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

			/*
			 * NewlandQRCodeScanner newlandQRCodeScanner = new
			 * NewlandQRCodeScanner(terminalBayProfile);
			 * String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			 * if(scannedData!=null && !scannedData.isEmpty()){
			 * status = true;
			 * responseReturn.put("status", true);
			 * responseReturn.put("responseData", scannedData);
			 * }
			 */
			S07_qR_Code_Scanning_of_Meters s07_qR_Code_Scanning_of_Meters = new S07_qR_Code_Scanning_of_Meters();
			bayResponse = s07_qR_Code_Scanning_of_Meters.dutQrScanSerialNoProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;// responseReturn;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public BayResponse executeFtWriteMeterSerialNoToDutProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();
		/*
		 * Map<String,Object> responseReturn = new HashMap<String,Object>();
		 * responseReturn.put("status", false);
		 * responseReturn.put("responseData", "");
		 * responseReturn.put("positionId", lduReadAddress);
		 */

		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterSerialNoToDutProcess: Entry");
		ApplicationLauncher.logger
				.info("Dut: executeFtWriteMeterSerialNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.FT_BAY_KEY);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

			/*
			 * NewlandQRCodeScanner newlandQRCodeScanner = new
			 * NewlandQRCodeScanner(terminalBayProfile);
			 * String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			 * if(scannedData!=null && !scannedData.isEmpty()){
			 * status = true;
			 * responseReturn.put("status", true);
			 * responseReturn.put("responseData", scannedData);
			 * }
			 */
			S073_Set_Serial_Numbers_On_Meters s0703_Set_Serial_Numbers_On_Meters = new S073_Set_Serial_Numbers_On_Meters();
			bayResponse = s0703_Set_Serial_Numbers_On_Meters.dutOpticalSerialNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;// responseReturn;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public BayResponse executeFtWriteMeterHardwareIdNoToDutProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();
		/*
		 * Map<String,Object> responseReturn = new HashMap<String,Object>();
		 * responseReturn.put("status", false);
		 * responseReturn.put("responseData", "");
		 * responseReturn.put("positionId", lduReadAddress);
		 */

		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: Entry");
		ApplicationLauncher.logger
				.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		// S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.FT_BAY_KEY);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

			/*
			 * NewlandQRCodeScanner newlandQRCodeScanner = new
			 * NewlandQRCodeScanner(terminalBayProfile);
			 * String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			 * if(scannedData!=null && !scannedData.isEmpty()){
			 * status = true;
			 * responseReturn.put("status", true);
			 * responseReturn.put("responseData", scannedData);
			 * }
			 */
			S074_Set_HardwareId_On_Meters s074_Set_HardwareId_On_Meters = new S074_Set_HardwareId_On_Meters();
			bayResponse = s074_Set_HardwareId_On_Meters.dutOpticalHardwareIdNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;// responseReturn;
		// UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress,
		// lduData.getLDU_ResultStatus(LDU_ReadAddress),
		// lduData.getLDU_ErrorValue(LDU_ReadAddress),
		// ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

	}

	public void updateDB_DeviceLDU_CalibrationErrorDataV2_1(ResultDataModel resultData, String DataType,
			String validationType, String validationType2, String validationType2ResultStatus,
			String validationType2ResultData,
			String overAllResultStatus, String errorMin2, String errorMax2, String averageType, String averageCount) {
		ApplicationLauncher.logger
				.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry: " + getSkipCurrentTP_Execution());
		if (!getSkipCurrentTP_Execution()) {

			ApplicationLauncher.logger.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry2");

			String FailureReason = "";
			/*
			 * //String rack_id =
			 * Integer.toString(resultData.getDeviceName());//LDU_ReadAddress);
			 * ApplicationHomeController.update_left_status("Updating DB LDU ErrorData"
			 * ,ConstantApp.LEFT_STATUS_DEBUG);
			 * ApplicationLauncher.logger.
			 * info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1: LDU_ReadAddress: " +
			 * resultData.getDeviceName());
			 * int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
			 * resultData.setTestType(ProjectExecutionController.getCurrentTestType());
			 * 
			 * boolean resultForTestTypeWithCurrentParameter = true;
			 * DutDatabaseWriteModel databaseWrQueue = new DutDatabaseWriteModel (
			 * ProjectExecutionController.getCurrentProjectName(),
			 * ProjectExecutionController.getCurrentTestPointName(),
			 * ProjectExecutionController.getCurrentTestAliasID(),
			 * getError_countAndIncrement(), resultData,
			 * FailureReason,DataType,getExecutionMctNctMode(),getEnergyFlowMode(),
			 * ProjectExecutionController.getSelectedDeployment_ID(),seqNumber,
			 * validationType,validationType2,validationType2ResultStatus,
			 * validationType2ResultData,overAllResultStatus,
			 * errorMin2, errorMax2,averageType, averageCount,
			 * resultForTestTypeWithCurrentParameter);
			 * DutManager.addDatabaseWriteQueue(databaseWrQueue, getDutAddress());
			 */

		}
	}

	public void UpdateDB_DeviceLDU_ErrorDataV2_1(int LDU_ReadAddress, String Resultstatus, String ErrorValue,
			String DataType, String averageType, String averageCount) {
		// ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1 :
		// Entry: "+getSkipCurrentTP_Execution());
		// if(!getSkipCurrentTP_Execution()){

		// ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1 :
		// Entry2");

		// String FailureReason = "";

		// String rack_id = Integer.toString(LDU_ReadAddress);
		// ApplicationHomeController.update_left_status("Updating DB LDU
		// ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
		// ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1:
		// LDU_ReadAddress: " + LDU_ReadAddress);
		// int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
		// boolean resultForTestTypeWithCurrentParameter = false;
		// FailureReason = null;//garbagecollector
		// rack_id= null;//garbagecollector
	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: Sleep :InterruptedException:" + e.getMessage());
		}

	}

	public boolean isDutProcessExecutionStarted() {
		return dutProcessExecutionStarted;
	}

	public void setDutProcessExecutionStarted(boolean dutProcessExecutionStarted) {
		this.dutProcessExecutionStarted = dutProcessExecutionStarted;
	}

	public boolean isDutProcessExecutionCompleted() {
		return dutProcessExecutionCompleted;
	}

	public void setDutProcessExecutionCompleted(boolean dutProcessexecutionCompleted) {
		this.dutProcessExecutionCompleted = dutProcessexecutionCompleted;
	}

	public int getDutAddress() {
		return dutAddress;
	}

	public void setDutAddress(int dutAddress) {
		this.dutAddress = dutAddress;
	}

	public String getDutSerialNumber() {
		return dutSerialNumber;
	}

	public void setDutSerialNumber(String dutSerialNo) {
		this.dutSerialNumber = dutSerialNo;
	}

	public void set_Error_min(String Emin) {
		error_min = Emin;

	}

	public String get_Error_min() {
		return error_min;

	}

	public void set_Error_max(String Emax) {
		error_max = Emax;

	}

	public String get_Error_max() {
		return error_max;

	}

	public void set_Error_min2(String Emin) {
		error_min2 = Emin;

	}

	public String get_Error_min2() {
		return error_min2;

	}

	public void set_Error_max2(String Emax) {
		error_max2 = Emax;

	}

	public String get_Error_max2() {
		return error_max2;

	}

	public void setTestRunType(String type) {
		testRunType = type;

	}

	public String getTestRunType() {
		return testRunType;

	}

	public String getDataValidationType() {
		return dataValidationType;
	}

	public void setDataValidationType(String validationType) {
		dataValidationType = validationType;
	}

	public String getDataValidationType2() {
		return dataValidationType2;
	}

	public void setDataValidationType2(String validationType) {
		dataValidationType2 = validationType;
	}

	public void setPresentLagValidationDelay(final int delayValue) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : delayValue: " + delayValue);

			// presentLagValidationDelayMap.put(lduAddress, delayValue);
			presentLagValidationDelay = delayValue;
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : added to new list");
			// }
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentLagValidationDelay : Exception :" + e.getMessage());
		}
	}

	public void setPresentValidationDataValue(final int dataValue) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dataValue: " + dataValue);
			// presentValidationDataValueMap.put(lduAddress, dataValue);
			presentValidationDataValue = dataValue;
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : added to new list");

		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidationDataValue : Exception :" + e.getMessage());
		}
	}

	public void setPresentValidation2DataValue(final int dataValue) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dataValue: " + dataValue);
			// presentValidation2DataValueMap.put(lduAddress, dataValue);
			// ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap :
			// added to new list");
			presentValidation2DataValue = dataValue;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidation2DataValue : Exception :" + e.getMessage());
		}
	}

	public String getPresentResultStatus() {

		ApplicationLauncher.logger.debug("Dut: getPresentResultStatus : dutAddress: " + dutAddress);
		String resultData = new String();
		try {
			/*
			 * if(presentResultStatusMap.containsKey(lduAddress)){
			 * resultData = presentResultStatusMap.get(lduAddress);
			 * }
			 */
			resultData = presentResultStatus;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultStatus : Exception :" + e.getMessage());
		}
		return resultData;
	}

	public void setPresentResultStatus(final String resultData) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : dutAddress: " + dutAddress);

			// presentResultStatusMap.put(lduAddress, resultData);
			presentResultStatus = resultData;
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : added to new list");
			// }
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultStatus : Exception :" + e.getMessage());
		}
	}

	public void clearPresentResultStatus() {
		presentResultStatus = "";
	}

	public String getPresentResultError() {

		ApplicationLauncher.logger.debug("Dut: getPresentResultError : dutAddress: " + dutAddress);
		String resultData = new String();
		try {
			/*
			 * if(presentResultErrorMap.containsKey(lduAddress)){
			 * resultData = presentResultErrorMap.get(lduAddress);
			 * }
			 */
			resultData = presentResultError;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultError : Exception :" + e.getMessage());
		}
		return resultData;
		// return dataLogParsedResultMap;
	}

	public void setPresentResultError(final String resultData) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultError : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: setPresentResultError : dutAddress: " + dutAddress);

			// presentResultErrorMap.put(lduAddress, resultData);
			presentResultError = resultData;
			// ApplicationLauncher.logger.debug("Dut: setPresentResultErrorMap : added to
			// new list");
			// }
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultError : Exception :" + e.getMessage());
		}
	}

	public void clearPresentResultError() {
		presentResultError = "";
	}

	public String getDutResultSummary(int lduAddress) {
		String dutSummaryStatus = ConstantReport.RESULT_STATUS_PASS.trim();
		// String dutSummaryStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
		try {
			if (dutResultSummary.containsKey(lduAddress)) {
				dutSummaryStatus = dutResultSummary.get(lduAddress);
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getDutResultSummary : Exception :" + e.getMessage());
		}
		return dutSummaryStatus;
	}

	public void setDutResultSummary(String dutSummaryStatus, int lduAddress) {

		ApplicationLauncher.logger.debug("Dut: setDutResultSummary : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("Dut: setDutResultSummary : dutSummaryStatus: " + dutSummaryStatus);
		dutResultSummary.put(lduAddress, dutSummaryStatus);

	}

	public void clearDutResultSummary() {
		dutResultSummary.clear();
	}

	public ArrayList<ResultDataModel> getSelectedResultData() {

		ApplicationLauncher.logger.debug("Dut: getSelectedResultData : dutAddress: " + dutAddress);
		ArrayList<ResultDataModel> resultData = new ArrayList<ResultDataModel>();
		try {
			/*
			 * if(selectedResultDataMap.containsKey(lduAddress)){
			 * resultData = selectedResultDataMap.get(lduAddress);
			 * }
			 */

			resultData = selectedResultData;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getSelectedResultData : Exception :" + e.getMessage());
		}
		return resultData;
		// return dataLogParsedResultMap;
	}

	public void addSelectedResultData(final ResultDataModel resultData) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: addSelectedResultData : Entry");

		try {
			ApplicationLauncher.logger.debug("Dut: addSelectedResultData : dutAddress: " + dutAddress);

			selectedResultData.add(resultData);
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: addSelectedResultData : Exception :" + e.getMessage());
		}
	}

	public void clearSelectedResultData() {
		selectedResultData.clear();
	}

	public void enableLduReadDataFlag(int lduAddress) {
	}

	public void setError_count(int inputErrorCount) {
		error_count = inputErrorCount;
	}

	public int getError_countAndIncrement() {
		return error_count++;// = inputErrorCount;
	}

	/*
	 * public Communicator getSerialPortObj() {
	 * return serialPortObj;
	 * }
	 * 
	 * public void setSerialPortObj(Communicator serialPortObj) {
	 * this.serialPortObj = serialPortObj;
	 * }
	 */

	public void setSkipCurrentTP_Execution(boolean status) {
		ApplicationLauncher.logger.debug("Dut: setSkipCurrentTP_Execution :Entry:status:" + status);
		skipCurrentTP_Execution = status;
	}

	public boolean getSkipCurrentTP_Execution() {
		// ApplicationLauncher.logger.debug("Dut: getSkipCurrentTP_Execution :Entry");
		return skipCurrentTP_Execution;
	}

	public ArrayList<DutDataLogParse> getDataLogParsedResult() {

		ApplicationLauncher.logger.debug("getDataLogParsedResult : dutAddress: " + dutAddress);
		ArrayList<DutDataLogParse> dataLog = new ArrayList<DutDataLogParse>();
		try {
			/*
			 * if(dataLogParsedResultMap.containsKey(lduAddress)){
			 * dataLog = dataLogParsedResultMap.get(lduAddress);
			 * }
			 */
			dataLog = dataLogParsedResult;
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :" + e.getMessage());
		}
		return dataLog;
	}

	public void addDataLogParsedResult(final DutDataLogParse data_LogParsedResult) {
		// DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;

		ApplicationLauncher.logger.debug("addDataLogParsedResult : dutAddress: " + dutAddress);

		try {

			dataLogParsedResult.add(data_LogParsedResult);
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :" + e.getMessage());
		}
	}

	public void clearDataLogParsedResult() {
		dataLogParsedResult.clear();
	}

	public void setDataLogParsedResult(ArrayList<DutDataLogParse> data_LogParsedResult) {
		this.dataLogParsedResult = data_LogParsedResult;
	}

	public ArrayList<DutDataLogParse> getRefStdResult() {
		return refStdResult;
	}

	public void addRefStdResult(DutDataLogParse ref_StdResult) {
		refStdResult.add(ref_StdResult);
	}

	public void setRefStdResult(ArrayList<DutDataLogParse> ref_StdResult) {
		refStdResult = ref_StdResult;
	}

	public boolean isRefStdLogResultsStopRequested() {
		return refStdLogResultsStopRequested;
	}

	public void setRefStdLogResultsStopRequested(boolean refStdLogResultsStopRequested) {
		this.refStdLogResultsStopRequested = refStdLogResultsStopRequested;
	}

}
