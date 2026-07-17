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
import com.tasnetwork.calibration.conveyor.bay.calib.S05_calibration;
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
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

public class DutControlProcess {

	//public static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	//public static SerialDataManager SerialDM_Obj = new SerialDataManager();
	
	private ArrayList<DutDataLogParse> refStdResult = new ArrayList<DutDataLogParse>();
	
	private volatile boolean dutProcessExecutionStarted = false;
	private volatile boolean dutProcessExecutionCompleted = false;
	private volatile int dutAddress = 0;
	//private volatile String dutSerialNo = "";
	
	private volatile String dutSerialNumber = "";
	
	//private Communicator serialPortObj = null;
	
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
	
/*	private HashMap< Integer,String> presentResultStatusMap = new HashMap<>();	
	private HashMap< Integer,String> presentResultErrorMap = new HashMap<>();
	private HashMap< Integer,Integer> presentLagValidationDelayMap = new HashMap<>();
	private HashMap< Integer,Integer> presentValidationDataValueMap = new HashMap<>();
	private HashMap< Integer,Integer> presentValidation2DataValueMap = new HashMap<>();*/
	
	private volatile boolean refStdLogResultsStopRequested = false;
	
	//private HashMap< Integer,String> dutSerialNumber = new HashMap<>(); 
	
	
	
	private HashMap< Integer,String> dutResultSummary = new HashMap<>(); 
	
	private ArrayList<ResultDataModel> selectedResultData = new ArrayList<ResultDataModel>();
	private ArrayList<DutDataLogParse> dataLogParsedResult = new ArrayList<DutDataLogParse>();
	
	//private HashMap< Integer,ArrayList<ResultDataModel>> selectedResultDataMap = new HashMap<>();
	//private  HashMap< Integer,ArrayList<DutDataLogParse>> dataLogParsedResultMap = new HashMap< Integer,ArrayList<DutDataLogParse>>();
	
	
/*	String energyFlowMode = ConstantMtePowerSource.IMPORT_MODE;
	String executionMctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;
	
	private DutCalibConfigModel calibParsedData = null;*/
	
	private int error_count = 1;
	
	
	
	public void processCalibMenuCommand(int lduReadAddress){
		
		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: Entry");
		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: lduReadAddress : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		

		
		setDutProcessExecutionCompleted(true);
		
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	
	public boolean executeDutCalibrationProcess(int lduReadAddress){
		
		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		if(ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			//status  = devSysEnergyMeter.phaseCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		
		return status;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	public boolean executeDutPhaseCalibrationProcess(int lduReadAddress){
			
			ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: Entry");
			ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: position No : " + lduReadAddress);
			setDutAddress(lduReadAddress);
			//S05_calibration s05_calibration = new S05_calibration();
			boolean status = false;
			if(ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
				DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
				//status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
				status  = devSysEnergyMeter.phaseCalibrationProcess(lduReadAddress);
			}
			setDutProcessExecutionCompleted(true);
			
			return status;
			//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			
		}
	
	public boolean executeDutNeutralCalibrationProcess(int lduReadAddress){
		
		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		if(ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			//status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
			status  = devSysEnergyMeter.neutralCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		
		return status;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	public boolean executeFtProcess(int lduReadAddress){
		
		ApplicationLauncher.logger.info("Dut: executeFtProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		if(ProconFeatureEnable.FT_SOURCE_CONNECTED) {
/*			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter();
			status = devSysEnergyMeter.calibrationProcess(lduReadAddress);*/
			
			S08_functional_Test s08_functional_Test = new S08_functional_Test();
			responseReturn = s08_functional_Test.functionalTestProcess (lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		status = (boolean) responseReturn.get("status");
		return status;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	public Map<String,Object> executeCalibrationQrScanProcess(int lduReadAddress){
		
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.HV_BAY_KEY);
		
		if(ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if(scannedData!=null && !scannedData.isEmpty()){
				status = true;
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}
			
		}
		setDutProcessExecutionCompleted(true);
		
		return responseReturn;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	public BayResponse executeFtQrScanProcess(int lduReadAddress){
		BayResponse bayResponse = new BayResponse();
		/*Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);*/
		
		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
		
		if(ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

/*			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if(scannedData!=null && !scannedData.isEmpty()){
				status = true;
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}*/
			S07_qR_Code_Scanning_of_Meters s07_qR_Code_Scanning_of_Meters = new S07_qR_Code_Scanning_of_Meters();
			bayResponse = s07_qR_Code_Scanning_of_Meters.dutQrScanSerialNoProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		
		return bayResponse;//responseReturn;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	public BayResponse executeFtWriteMeterSerialNoToDutProcess(int lduReadAddress){
		BayResponse bayResponse = new BayResponse();
		/*Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);*/
		
		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterSerialNoToDutProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterSerialNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
		
		if(ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

/*			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if(scannedData!=null && !scannedData.isEmpty()){
				status = true;
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}*/
			S073_Set_Serial_Numbers_On_Meters s0703_Set_Serial_Numbers_On_Meters = new S073_Set_Serial_Numbers_On_Meters();
			bayResponse = s0703_Set_Serial_Numbers_On_Meters.dutOpticalSerialNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		
		return bayResponse;//responseReturn;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	
	public BayResponse executeFtWriteMeterHardwareIdNoToDutProcess(int lduReadAddress){
		BayResponse bayResponse = new BayResponse();
		/*Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);*/
		
		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		//S05_calibration s05_calibration = new S05_calibration();
		boolean status = false;

		TerminalProfileSetting terminalBayProfile = new TerminalProfileSetting();
		terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService().findByBayKey(ConstantConveyor.FT_BAY_KEY);
		
		if(ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {

/*			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if(scannedData!=null && !scannedData.isEmpty()){
				status = true;
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}*/
			S074_Set_HardwareId_On_Meters s074_Set_HardwareId_On_Meters = new S074_Set_HardwareId_On_Meters();
			bayResponse = s074_Set_HardwareId_On_Meters.dutOpticalHardwareIdNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		
		return bayResponse;//responseReturn;
		//UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		
	}
	
	public void updateDB_DeviceLDU_CalibrationErrorDataV2_1(ResultDataModel resultData, String DataType,
			String validationType,String validationType2, String validationType2ResultStatus,String validationType2ResultData,
			String overAllResultStatus, String errorMin2,String errorMax2,String averageType,String averageCount){
		ApplicationLauncher.logger.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry: "+getSkipCurrentTP_Execution());
		if(!getSkipCurrentTP_Execution()){

			ApplicationLauncher.logger.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry2");
			
			String FailureReason = "";
/*
			//String rack_id = Integer.toString(resultData.getDeviceName());//LDU_ReadAddress);
			ApplicationHomeController.update_left_status("Updating DB LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1: LDU_ReadAddress: "  + resultData.getDeviceName());
			int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
			resultData.setTestType(ProjectExecutionController.getCurrentTestType());
			
			boolean resultForTestTypeWithCurrentParameter = true;
			DutDatabaseWriteModel databaseWrQueue = new DutDatabaseWriteModel ( ProjectExecutionController.getCurrentProjectName(), 
					ProjectExecutionController.getCurrentTestPointName(), ProjectExecutionController.getCurrentTestAliasID(), 
					getError_countAndIncrement(), resultData,
					FailureReason,DataType,getExecutionMctNctMode(),getEnergyFlowMode(),
					ProjectExecutionController.getSelectedDeployment_ID(),seqNumber,
					validationType,validationType2,validationType2ResultStatus,validationType2ResultData,overAllResultStatus,
					 errorMin2, errorMax2,averageType, averageCount, resultForTestTypeWithCurrentParameter);
			DutManager.addDatabaseWriteQueue(databaseWrQueue, getDutAddress());*/

		}
	}
	
	public void UpdateDB_DeviceLDU_ErrorDataV2_1(int LDU_ReadAddress,String Resultstatus,String ErrorValue,
			String DataType,String averageType, String averageCount){
		ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1 : Entry: "+getSkipCurrentTP_Execution());
		if(!getSkipCurrentTP_Execution()){

			ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1 : Entry2");
			

			


			String FailureReason = "";

			String rack_id = Integer.toString(LDU_ReadAddress);
			ApplicationHomeController.update_left_status("Updating DB LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.info("Dut: UpdateDB_DeviceLDU_ErrorDataV2_1: LDU_ReadAddress: "  + LDU_ReadAddress);
			int seqNumber = ProjectExecutionController.getCurrentTestPoint_Index()+1;
			boolean resultForTestTypeWithCurrentParameter = false;
/*			DutDatabaseWriteModel databaseWrQueue = new DutDatabaseWriteModel (ProjectExecutionController.getCurrentProjectName(), 
					ProjectExecutionController.getCurrentTestPointName(), ProjectExecutionController.getCurrentTestAliasID(), 
					rack_id, Resultstatus,getError_countAndIncrement(), ErrorValue,
					FailureReason,DataType,getExecutionMctNctMode(),getEnergyFlowMode(),
					ProjectExecutionController.getSelectedDeployment_ID(),seqNumber, averageType, averageCount,resultForTestTypeWithCurrentParameter);
			DutManager.addDatabaseWriteQueue(databaseWrQueue,getDutAddress());
*/

			FailureReason = null;//garbagecollector
			rack_id= null;//garbagecollector
		}
	}
	
	
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: Sleep :InterruptedException:"+ e.getMessage());
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
	
/*	public String getDutSerialNo() {
		return dutSerialNo;
	}
	
	public void setDutSerialNo(String dutSerialNo) {
		this.dutSerialNo = dutSerialNo;
	}*/
	
	public String getDutSerialNumber() {
		return dutSerialNumber;
	}
	
	public void setDutSerialNumber(String dutSerialNo) {
		this.dutSerialNumber = dutSerialNo;
	}
	
	public void set_Error_min(String Emin){
		error_min = Emin;

	}

	public String get_Error_min(){
		return error_min;

	}

	public void set_Error_max(String Emax){
		error_max = Emax;

	}

	public String get_Error_max(){
		return error_max;

	}
	
	public void set_Error_min2(String Emin){
		error_min2 = Emin;

	}

	public String get_Error_min2(){
		return error_min2;

	}
	
	public void set_Error_max2(String Emax){
		error_max2 = Emax;

	}

	public  String get_Error_max2(){
		return error_max2;

	}
	
	public void setTestRunType(String type){
		testRunType = type;

	}

	public String getTestRunType(){
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
	
/*	public int getPresentLagValidationDelayMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getPresentLagValidationDelayMap : lduAddress: " + lduAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{
			if(presentLagValidationDelayMap.containsKey(lduAddress)){
				delayValue = presentLagValidationDelayMap.get(lduAddress);
			}else{
				ApplicationLauncher.logger.debug("Dut: getPresentLagValidationDelayMap : returning default value" );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentLagValidationDelayMap : Exception :"+ e.getMessage());
		}
		return delayValue;
		//return dataLogParsedResultMap;
	}
	
	
	public void setPresentLagValidationDelayMap(final int  delayValue, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelayMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelayMap : lduAddress: " + lduAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelayMap : delayValue: " + delayValue);

				presentLagValidationDelayMap.put(lduAddress, delayValue);
				ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelayMap : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentLagValidationDelayMap : Exception :"+ e.getMessage());
		}
	}
	


		
	public void clearPresentLagValidationDelayMap() {
		presentLagValidationDelayMap.clear();
	}*/
	
/*	public int getPresentLagValidationDelay() {
		
		ApplicationLauncher.logger.debug("Dut: getPresentLagValidationDelay : dutAddress: " + dutAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{

			delayValue = presentLagValidationDelay;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentLagValidationDelay : Exception :"+ e.getMessage());
		}
		return delayValue;
		//return dataLogParsedResultMap;
	}*/
	
	
	public void setPresentLagValidationDelay(final int  delayValue) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : delayValue: " + delayValue);

				//presentLagValidationDelayMap.put(lduAddress, delayValue);
			presentLagValidationDelay = delayValue;
				ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentLagValidationDelay : Exception :"+ e.getMessage());
		}
	}
	


		
	/*public void clearPresentLagValidationDelay() {
		presentLagValidationDelay = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
	}*/
	
/*	public void clearPresentValidationDataValueMap() {
		presentValidationDataValueMap.clear();
	}
	
	public int getPresentValidationDataValueMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getPresentValidationDataValueMap : lduAddress: " + lduAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{
			if(presentValidationDataValueMap.containsKey(lduAddress)){
				delayValue = presentValidationDataValueMap.get(lduAddress);
			}else{
				ApplicationLauncher.logger.debug("Dut: getPresentValidationDataValueMap : returning default value" );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentValidationDataValueMap : Exception :"+ e.getMessage());
		}
		return delayValue;
		//return dataLogParsedResultMap;
	}
	
	
	public void setPresentValidationDataValueMap(final int  dataValue, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValueMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValueMap : lduAddress: " + lduAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValueMap : dataValue: " + dataValue);
				presentValidationDataValueMap.put(lduAddress, dataValue);
				ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValueMap : added to new list");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidationDataValueMap : Exception :"+ e.getMessage());
		}
	}*/
	
/*	public void clearPresentValidationDataValue() {
		presentValidationDataValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
	}
	
	public int getPresentValidationDataValue() {
		
		ApplicationLauncher.logger.debug("Dut: getPresentValidationDataValue : dutAddress: " + dutAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{

			
			delayValue = presentValidationDataValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentValidationDataValue : Exception :"+ e.getMessage());
		}
		return delayValue;
		//return dataLogParsedResultMap;
	}*/
	
	
	public void setPresentValidationDataValue(final int  dataValue) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dataValue: " + dataValue);
				//presentValidationDataValueMap.put(lduAddress, dataValue);
			presentValidationDataValue = dataValue;
				ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : added to new list");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidationDataValue : Exception :"+ e.getMessage());
		}
	}
	
	
/*	public void clearPresentValidation2DataValueMap() {
		presentValidation2DataValueMap.clear();
	}
	
	public int getPresentValidation2DataValueMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getPresentValidation2DataValueMap : lduAddress: " + lduAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{
			if(presentValidation2DataValueMap.containsKey(lduAddress)){
				delayValue = presentValidation2DataValueMap.get(lduAddress);
			}else{
				ApplicationLauncher.logger.debug("Dut: getPresentValidation2DataValueMap : returning default value" );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentValidation2DataValueMap : Exception :"+ e.getMessage());
		}
		return delayValue;
	}
	
	
	public void setPresentValidation2DataValueMap(final int  dataValue, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap : lduAddress: " + lduAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap : dataValue: " + dataValue);
				presentValidation2DataValueMap.put(lduAddress, dataValue);
				ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap : added to new list");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidation2DataValueMap : Exception :"+ e.getMessage());
		}
	}*/
	
/*	public void clearPresentValidation2DataValue() {
		presentValidation2DataValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
	}
	
	public int getPresentValidation2DataValue() {
		
		ApplicationLauncher.logger.debug("Dut: getPresentValidation2DataValue : dutAddress: " + dutAddress);
		int delayValue = ConstantCalibration.CALIBRATION_LAG_VALIDATION_INVALID_VALUE;
		try{

			delayValue = presentValidation2DataValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentValidation2DataValue : Exception :"+ e.getMessage());
		}
		return delayValue;
	}*/
	
	
	public void setPresentValidation2DataValue(final int  dataValue) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dataValue: " + dataValue);
				//presentValidation2DataValueMap.put(lduAddress, dataValue);
				//ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValueMap : added to new list");
			presentValidation2DataValue = dataValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentValidation2DataValue : Exception :"+ e.getMessage());
		}
	}
	
/*	public String getPresentResultStatusMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getPresentResultStatusMap : lduAddress: " + lduAddress);
		String resultData = new String();
		try{
			if(presentResultStatusMap.containsKey(lduAddress)){
				resultData = presentResultStatusMap.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultStatusMap : Exception :"+ e.getMessage());
		}
		return resultData;
	}
	


	
	
	public void setPresentResultStatusMap(final String  resultData, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultStatusMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatusMap : lduAddress: " +lduAddress);

				presentResultStatusMap.put(lduAddress, resultData);
				ApplicationLauncher.logger.debug("Dut: setPresentResultStatusMap : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultStatusMap : Exception :"+ e.getMessage());
		}
	}
	
	public void clearPresentResultStatusMap() {
		presentResultStatusMap.clear();
	}*/
	
	
	public String getPresentResultStatus() {
		
		ApplicationLauncher.logger.debug("Dut: getPresentResultStatus : dutAddress: " + dutAddress);
		String resultData = new String();
		try{
/*			if(presentResultStatusMap.containsKey(lduAddress)){
				resultData = presentResultStatusMap.get(lduAddress);
			}*/
			resultData = presentResultStatus;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultStatus : Exception :"+ e.getMessage());
		}
		return resultData;
	}
	
	public void setPresentResultStatus(final String  resultData) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : dutAddress: " +dutAddress);

				//presentResultStatusMap.put(lduAddress, resultData);
				presentResultStatus = resultData;
				ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultStatus : Exception :"+ e.getMessage());
		}
	}
	


		

	
	public void clearPresentResultStatus() {
		presentResultStatus="";
	}
	
	
	
/*	public String getPresentResultErrorMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getPresentResultErrorMap : lduAddress: " + lduAddress);
		String resultData = new String();
		try{
			if(presentResultErrorMap.containsKey(lduAddress)){
				resultData = presentResultErrorMap.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultErrorMap : Exception :"+ e.getMessage());
		}
		return resultData;
		//return dataLogParsedResultMap;
	}
	

	
	
	public void setPresentResultErrorMap(final String  resultData, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultErrorMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentResultErrorMap : lduAddress: " +lduAddress);

				presentResultErrorMap.put(lduAddress, resultData);
				ApplicationLauncher.logger.debug("Dut: setPresentResultErrorMap : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultErrorMap : Exception :"+ e.getMessage());
		}
	}
	
	public void clearPresentResultErrorMap() {
		presentResultErrorMap.clear();
	}
	*/
	public String getPresentResultError() {
		
		ApplicationLauncher.logger.debug("Dut: getPresentResultError : dutAddress: " + dutAddress);
		String resultData = new String();
		try{
/*			if(presentResultErrorMap.containsKey(lduAddress)){
				resultData = presentResultErrorMap.get(lduAddress);
			}*/
			resultData = presentResultError;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getPresentResultError : Exception :"+ e.getMessage());
		}
		return resultData;
		//return dataLogParsedResultMap;
	}
	
	public void setPresentResultError(final String  resultData) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: setPresentResultError : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: setPresentResultError : dutAddress: " +dutAddress);

				//presentResultErrorMap.put(lduAddress, resultData);
			presentResultError = resultData;
				//ApplicationLauncher.logger.debug("Dut: setPresentResultErrorMap : added to new list");
			//}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: setPresentResultError : Exception :"+ e.getMessage());
		}
	}

		

	
	public void clearPresentResultError() {
		presentResultError = "";
	}
	
/*	public String getDutSerialNumber(int lduAddress) {
		String serialNo = "";
		try{
			if(dutSerialNumber.containsKey(lduAddress)){
				serialNo = dutSerialNumber.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getDutSerialNumber : Exception :"+ e.getMessage());
		}
		return serialNo;
	}
	public void setDutSerialNumber( String dutSerialNo, int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: setDutSerialNumber : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("Dut: setDutSerialNumber : dutSerialNo: " + dutSerialNo);
		dutSerialNumber.put(lduAddress, dutSerialNo);
		

	}
	
	public void clearDutSerialNumber() {
		dutSerialNumber.clear();
	}*/
	
	public String getDutResultSummary(int lduAddress) {
		String dutSummaryStatus = ConstantReport.RESULT_STATUS_PASS.trim();
		//String dutSummaryStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
		try{
			if(dutResultSummary.containsKey(lduAddress)){
				dutSummaryStatus = dutResultSummary.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getDutResultSummary : Exception :"+ e.getMessage());
		}
		return dutSummaryStatus;
	}
	public void setDutResultSummary( String dutSummaryStatus, int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: setDutResultSummary : lduAddress: " + lduAddress);
		ApplicationLauncher.logger.debug("Dut: setDutResultSummary : dutSummaryStatus: " + dutSummaryStatus);
		dutResultSummary.put(lduAddress, dutSummaryStatus);		

	}
	
	public void clearDutResultSummary() {
		dutResultSummary.clear();
	}
	
/*	public ArrayList<ResultDataModel> getSelectedResultDataMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("Dut: getSelectedResultDataMap : lduAddress: " + lduAddress);
		ArrayList<ResultDataModel> resultData = new ArrayList<ResultDataModel>();
		try{
			if(selectedResultDataMap.containsKey(lduAddress)){
				resultData = selectedResultDataMap.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getSelectedResultDataMap : Exception :"+ e.getMessage());
		}
		return resultData;
		//return dataLogParsedResultMap;
	}
	
	
	public void addSelectedResultDataMap(final ResultDataModel  resultData, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : lduAddress: " +lduAddress);
			ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : getAcceptableErrorMaxLimit: " +resultData.getAcceptableErrorMaxLimit());
			ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : getErrorValue: " +resultData.getErrorValue());
			ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : getTestCaseName: " +resultData.getTestCaseName());
			ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : containsKey: " +selectedResultDataMap.containsKey(lduAddress));
			if(selectedResultDataMap.containsKey(lduAddress)){
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : Test1");
				selectedResultDataMap.get(lduAddress).add(resultData);
				
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : added to existing list");
			}else{
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : Test2");
				ArrayList<ResultDataModel> resultDataList = new ArrayList<ResultDataModel>();
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : Test3");
				resultDataList.add(resultData);
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : Test4");
				selectedResultDataMap.put(lduAddress, resultDataList);
				ApplicationLauncher.logger.debug("Dut: addSelectedResultDataMap : added to new list");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: addSelectedResultDataMap : Exception :"+ e.getMessage());
		}
	}
	


		
	public void clearSelectedResultDataMap() {
		selectedResultDataMap.clear();
	}*/
	
	
	public ArrayList<ResultDataModel> getSelectedResultData() {
		
		ApplicationLauncher.logger.debug("Dut: getSelectedResultData : dutAddress: " + dutAddress);
		ArrayList<ResultDataModel> resultData = new ArrayList<ResultDataModel>();
		try{
/*			if(selectedResultDataMap.containsKey(lduAddress)){
				resultData = selectedResultDataMap.get(lduAddress);
			}*/
			
			resultData = selectedResultData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: getSelectedResultData : Exception :"+ e.getMessage());
		}
		return resultData;
		//return dataLogParsedResultMap;
	}
	
	
	public void addSelectedResultData(final ResultDataModel  resultData) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		ApplicationLauncher.logger.debug("Dut: addSelectedResultData : Entry");
		
		try{
			ApplicationLauncher.logger.debug("Dut: addSelectedResultData : dutAddress: " +dutAddress);
			//ApplicationLauncher.logger.debug("Dut: addSelectedResultData : getAcceptableErrorMaxLimit: " +resultData.getAcceptableErrorMaxLimit());
			//ApplicationLauncher.logger.debug("Dut: addSelectedResultData : getErrorValue: " +resultData.getErrorValue());
			//ApplicationLauncher.logger.debug("Dut: addSelectedResultData : getTestCaseName: " +resultData.getTestCaseName());
			
			selectedResultData.add(resultData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Dut: addSelectedResultData : Exception :"+ e.getMessage());
		}
	}
	


		
	public void clearSelectedResultData() {
		selectedResultData.clear();
	}
	
	/*private void processDutAbsoluteTimeStamp(int lduAddress, long messageTimeStamp,ArrayList<Long> messageTimeStampList) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp :Entry");
		
		int maxDataSize = getDataLogParsedResult().size();
		
		ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : maxDataSize: " + maxDataSize);
		ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : messageTimeStampList.size(): " + messageTimeStampList.size());
		long dutTimeStamp = 0;
		long appDutTimeStamp = 0;
		long epoch = 0;
		ZoneId zone = ZoneId.of("Asia/Kolkata");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timeValueStr="";
		
		if(messageTimeStampList.size()!= maxDataSize){
			//long messageQueueDutLastTimeStamp = Long.parseLong(getDataLogParsedResultMap(lduAddress).get(maxDataSize-1).getDutTimeStampEpoch());
			
			long messageQueueDutLastTimeStamp = Long.parseLong(getDataLogParsedResult().get(maxDataSize-1).getDutTimeStampEpoch());
			ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : approximate last message queue app messageTimeStamp : " + messageTimeStamp);
			ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : last dut time stamp: " + messageQueueDutLastTimeStamp);
			long timeDifferenceBetweenDutAndApp = messageTimeStamp - messageQueueDutLastTimeStamp;
			ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : timeDifferenceBetweenDutAndApp: " + timeDifferenceBetweenDutAndApp); 
			
			for(int i = 0; i < maxDataSize; i++){
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : index: " + i);
				//dutTimeStamp = Long.parseLong(getDataLogParsedResultMap(lduAddress).get(i).getDutTimeStampEpoch());
				dutTimeStamp = Long.parseLong(getDataLogParsedResult().get(i).getDutTimeStampEpoch());
				appDutTimeStamp = dutTimeStamp + timeDifferenceBetweenDutAndApp;
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : dutTimeStamp: " + dutTimeStamp);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : appDutTimeStamp: " + appDutTimeStamp);
				
				//getDataLogParsedResultMap(lduAddress).get(i).setAppDutTimeStampEpoch(String.valueOf(appDutTimeStamp));
				getDataLogParsedResult().get(i).setAppDutTimeStampEpoch(String.valueOf(appDutTimeStamp));
				//ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : getAppDutTimeStampEpoch: " + getDataLogParsedResultMap(lduAddress).get(i).getAppDutTimeStampEpoch());
				
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : getAppDutTimeStampEpoch: " + getDataLogParsedResult().get(i).getAppDutTimeStampEpoch());
				epoch = dutTimeStamp;
				Instant instant = Instant.ofEpochSecond(epoch);			
				timeValueStr = ZonedDateTime.ofInstant(instant,zone ).format(dateFormatter);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : dutTimeStampStr: " + timeValueStr);
				epoch = appDutTimeStamp;
				instant = Instant.ofEpochSecond(epoch);	
				timeValueStr = ZonedDateTime.ofInstant(instant,zone ).format(dateFormatter);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : appDutTimeStampStr: " + timeValueStr);
			}
		}else{
			for(int i = 0; i < maxDataSize; i++){
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : index3: " + i);
				//dutTimeStamp = Long.parseLong(getDataLogParsedResultMap(lduAddress).get(i).getDutTimeStampEpoch());
				dutTimeStamp = Long.parseLong(getDataLogParsedResult().get(i).getDutTimeStampEpoch());
				appDutTimeStamp = messageTimeStampList.get(i);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : dutTimeStamp3: " + dutTimeStamp);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : appDutTimeStamp3: " + appDutTimeStamp);
				
				//getDataLogParsedResultMap(lduAddress).get(i).setAppDutTimeStampEpoch(String.valueOf(appDutTimeStamp));
				getDataLogParsedResult().get(i).setAppDutTimeStampEpoch(String.valueOf(appDutTimeStamp));
				//ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : getAppDutTimeStampEpoch3: " + getDataLogParsedResultMap(lduAddress).get(i).getAppDutTimeStampEpoch());
				
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : getAppDutTimeStampEpoch3: " + getDataLogParsedResult().get(i).getAppDutTimeStampEpoch());
				epoch = dutTimeStamp;
				Instant instant = Instant.ofEpochSecond(epoch);			
				timeValueStr = ZonedDateTime.ofInstant(instant,zone ).format(dateFormatter);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : dutTimeStampStr3: " + timeValueStr);
				epoch = appDutTimeStamp;
				instant = Instant.ofEpochSecond(epoch);	
				timeValueStr = ZonedDateTime.ofInstant(instant,zone ).format(dateFormatter);
				ApplicationLauncher.logger.debug("Dut: processDutAbsoluteTimeStamp : appDutTimeStampStr3: " + timeValueStr);
			}
			
		}
	}
	*/
	/*public void clearCorrespondingLduSerialData(int lduAddress){
		ApplicationLauncher.logger.debug("Dut: clearCorrespondingLduSerialData :Entry");
		Communicator SerialPortObj = this.serialPortObj;
		
		switch (lduAddress) {

			case 1:
				SerialPortObj = commLDU1;
				break;
				
			case 2:
				SerialPortObj = commLDU2;
				break;
				
			case 3:
				SerialPortObj = commLDU3;
				break;
				
			case 4:
				SerialPortObj = commLDU4;
				break;
				
			case 5:
				SerialPortObj = commLDU5;
				break;
				
			case 6:
				SerialPortObj = commLDU6;
				break;
				
			case 7:
				SerialPortObj = commLDU7;
				break;
				
			case 8:
				SerialPortObj = commLDU8;
				break;
				
			case 9:
				SerialPortObj = commLDU9;
				break;
			case 10:
				SerialPortObj = commLDU10;
				break;

	
			case 11:
				SerialPortObj = commLDU11;
				break;
				
			case 12:
				SerialPortObj = commLDU12;
				break;
				
			case 13:
				SerialPortObj = commLDU13;
				break;
				
			case 14:
				SerialPortObj = commLDU14;
				break;
				
			case 15:
				SerialPortObj = commLDU15;
				break;
				
			case 16:
				SerialPortObj = commLDU16;
				break;
				
			case 17:
				SerialPortObj = commLDU17;
				break;
	
			case 18:
				SerialPortObj = commLDU18;
				break;
				
			case 19:
				SerialPortObj = commLDU19;
				break;
			case 20:
				SerialPortObj = commLDU20;
				break;
				
				
			case 21:
				SerialPortObj = commLDU21;
				break;
				
			case 22:
				SerialPortObj = commLDU22;
				break;
				
			case 23:
				SerialPortObj = commLDU23;
				break;
				
			case 24:
				SerialPortObj = commLDU24;
				break;
				
			case 25:
				SerialPortObj = commLDU25;
				break;
				
			case 26:
				SerialPortObj = commLDU26;
				break;
				
			case 27:
				SerialPortObj = commLDU27;
				break;
	
			case 28:
				SerialPortObj = commLDU28;
				break;
				
			case 29:
				SerialPortObj = commLDU29;
				break;
			case 30:
				SerialPortObj = commLDU30;
				break;
				
				
			case 31:
				SerialPortObj = commLDU31;
				break;
				
			case 32:
				SerialPortObj = commLDU32;
				break;
				
			case 33:
				SerialPortObj = commLDU33;
				break;
				
			case 34:
				SerialPortObj = commLDU34;
				break;
				
			case 35:
				SerialPortObj = commLDU35;
				break;
				
			case 36:
				SerialPortObj = commLDU36;
				break;
				
			case 37:
				SerialPortObj = commLDU37;
				break;
	
			case 38:
				SerialPortObj = commLDU38;
				break;
				
			case 39:
				SerialPortObj = commLDU39;
				break;
			case 40:
				SerialPortObj = commLDU40;
				break;
				
				
				
			case 41:
				SerialPortObj = commLDU41;
				break;
				
			case 42:
				SerialPortObj = commLDU42;
				break;
				
			case 43:
				SerialPortObj = commLDU43;
				break;
				
			case 44:
				SerialPortObj = commLDU44;
				break;
				
			case 45:
				SerialPortObj = commLDU45;
				break;
				
			case 46:
				SerialPortObj = commLDU46;
				break;
				
			case 47:
				SerialPortObj = commLDU47;
				break;
	
			case 48:
				SerialPortObj = commLDU48;
				break;
				
				
	
			default:
				break;
		}


		SerialPortObj.ClearSerialData();

	}*/
/*	
	public void WriteToSerialCommDut(int dutAddress, String Data){
		//ApplicationLauncher.logger.debug("Dut: WriteToSerialCommLDU :DataHex:"+ConstantCcubeLDU.StringToHex(Data).toUpperCase());
		ApplicationLauncher.logger.debug("Dut: WriteToSerialCommDut :dutAddress:"+dutAddress);
		ApplicationLauncher.logger.debug("Dut: WriteToSerialCommDut :Data:"+Data);
		try {
			serialPortObj.writeStringMsgToPort(Data);
			Sleep(200);
		}catch(Exception e){
			ApplicationLauncher.logger.error("Dut: WriteToSerialCommDut :Exception :" + e.getMessage());
		}

	}*/
	
/*	public void calibrationSendCommand(int dutAddress, String data){
		ApplicationLauncher.logger.debug("Dut: calibrationSendCommand :Entry");
		data = data + ConstantCalibration.CMD_TERMINATOR;
		ApplicationLauncher.logger.debug("Dut: calibrationSendCommand :dutAddress:"+dutAddress);
		ApplicationLauncher.logger.debug("Dut: calibrationSendCommand : data: "+data);
		WriteToSerialCommDut(dutAddress,data);
		//ApplicationLauncher.logger.debug("Dut: LDU_SendCommandReadErrorData :LDU_ReadAddress:Hex:"+String.format("%02d" , LDU_ReadAddress));
		//String Data = ConstantCcubeLDU.CMD_LDU_READ_ERROR_DATA_HDR+ ConstantCcubeLDU.DecodeHextoString(String.format("%02x" , LDU_ReadAddress).toUpperCase())+ConstantCcubeLDU.CMD_LDU_READ_ERROR_DATA_CMD_DATA+ConstantApp.END_CR2;


	}*/
	
	public void enableLduReadDataFlag(int lduAddress){
	/*	
		switch (lduAddress) {

			case ConstantCalibration.LDU1_ADDRESS:
				DeviceDataManagerController.setLDU1_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU2_ADDRESS:
				DeviceDataManagerController.setLDU2_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU3_ADDRESS:
				DeviceDataManagerController.setLDU3_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU4_ADDRESS:
				DeviceDataManagerController.setLDU4_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU5_ADDRESS:
				DeviceDataManagerController.setLDU5_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU6_ADDRESS:
				DeviceDataManagerController.setLDU6_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU7_ADDRESS:
				DeviceDataManagerController.setLDU7_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU8_ADDRESS:
				DeviceDataManagerController.setLDU8_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU9_ADDRESS:
				DeviceDataManagerController.setLDU9_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU10_ADDRESS:
				DeviceDataManagerController.setLDU10_ReadDataFlag(true);
				break;
	
				
			case ConstantCalibration.LDU11_ADDRESS:
				DeviceDataManagerController.setLDU11_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU12_ADDRESS:
				DeviceDataManagerController.setLDU12_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU13_ADDRESS:
				DeviceDataManagerController.setLDU13_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU14_ADDRESS:
				DeviceDataManagerController.setLDU14_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU15_ADDRESS:
				DeviceDataManagerController.setLDU15_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU16_ADDRESS:
				DeviceDataManagerController.setLDU16_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU17_ADDRESS:
				DeviceDataManagerController.setLDU17_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU18_ADDRESS:
				DeviceDataManagerController.setLDU18_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU19_ADDRESS:
				DeviceDataManagerController.setLDU19_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU20_ADDRESS:
				DeviceDataManagerController.setLDU20_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU21_ADDRESS:
				DeviceDataManagerController.setLDU21_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU22_ADDRESS:
				DeviceDataManagerController.setLDU22_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU23_ADDRESS:
				DeviceDataManagerController.setLDU23_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU24_ADDRESS:
				DeviceDataManagerController.setLDU24_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU25_ADDRESS:
				DeviceDataManagerController.setLDU25_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU26_ADDRESS:
				DeviceDataManagerController.setLDU26_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU27_ADDRESS:
				DeviceDataManagerController.setLDU27_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU28_ADDRESS:
				DeviceDataManagerController.setLDU28_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU29_ADDRESS:
				DeviceDataManagerController.setLDU29_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU30_ADDRESS:
				DeviceDataManagerController.setLDU30_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU31_ADDRESS:
				DeviceDataManagerController.setLDU31_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU32_ADDRESS:
				DeviceDataManagerController.setLDU32_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU33_ADDRESS:
				DeviceDataManagerController.setLDU33_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU34_ADDRESS:
				DeviceDataManagerController.setLDU34_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU35_ADDRESS:
				DeviceDataManagerController.setLDU35_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU36_ADDRESS:
				DeviceDataManagerController.setLDU36_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU37_ADDRESS:
				DeviceDataManagerController.setLDU37_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU38_ADDRESS:
				DeviceDataManagerController.setLDU38_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU39_ADDRESS:
				DeviceDataManagerController.setLDU39_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU40_ADDRESS:
				DeviceDataManagerController.setLDU40_ReadDataFlag(true);
				break;
				
				
			case ConstantCalibration.LDU41_ADDRESS:
				DeviceDataManagerController.setLDU41_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU42_ADDRESS:
				DeviceDataManagerController.setLDU42_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU43_ADDRESS:
				DeviceDataManagerController.setLDU43_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU44_ADDRESS:
				DeviceDataManagerController.setLDU44_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU45_ADDRESS:
				DeviceDataManagerController.setLDU45_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU46_ADDRESS:
				DeviceDataManagerController.setLDU46_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU47_ADDRESS:
				DeviceDataManagerController.setLDU47_ReadDataFlag(true);
				break;
				
			case ConstantCalibration.LDU48_ADDRESS:
				DeviceDataManagerController.setLDU48_ReadDataFlag(true);
				break;
				

			default:
				break;
		}*/
	}

	/*public SerialDataLDU calibrationReadData(int lduAddress, int Expectedlength,String ExpectedResponse, int numberOfReadings){
		ApplicationLauncher.logger.debug("Dut: calibrationReadData :Entry");
		Communicator SerialPortObj = this.serialPortObj;//commLDU1;
		

	
		SerialPortObj.setExpectedLength(Expectedlength);
		SerialPortObj.setExpectedResult(ExpectedResponse);
		//SerialPortObj.ClearSerialData();
		ApplicationLauncher.logger.debug("Dut: calibrationReadData: setExpectedResult:"+SerialPortObj.getExpectedResult());
		ApplicationLauncher.logger.debug("Dut: calibrationReadData: setExpectedResult2:"+GUIUtils.hexToAscii(SerialPortObj.getExpectedResult()));
		ApplicationLauncher.logger.debug("Dut: calibrationReadData: setExpectedLength:"+SerialPortObj.getExpectedLength());
		SerialDataLDU lduData = new SerialDataLDU(SerialPortObj,lduAddress, getCalibParsedData());
		lduData.SerialReponseTimerStart(40,numberOfReadings);
		SerialPortObj = null;//garbagecollector
		return lduData;
	}*/
	
/*	
	public void setEnergyFlowMode(String FlowMode){
		energyFlowMode = FlowMode;
	}

	public String getEnergyFlowMode(){
		return energyFlowMode ;
	}
	
	public  String getExecutionMctNctMode() {
		return executionMctNctMode;
	}




	public void setExecutionMctNctMode(String execMctNctMode) {
		executionMctNctMode = execMctNctMode;
	}*/
	
	public void setError_count(int inputErrorCount){
		error_count = inputErrorCount;
	}
	public int getError_countAndIncrement(){
		return error_count++;// = inputErrorCount;
	}

/*	public Communicator getSerialPortObj() {
		return serialPortObj;
	}

	public void setSerialPortObj(Communicator serialPortObj) {
		this.serialPortObj = serialPortObj;
	}*/

	public void setSkipCurrentTP_Execution(boolean status){
		ApplicationLauncher.logger.debug("Dut: setSkipCurrentTP_Execution :Entry:status:"+status);
		skipCurrentTP_Execution = status;
	}

	public boolean getSkipCurrentTP_Execution(){
		//ApplicationLauncher.logger.debug("Dut: getSkipCurrentTP_Execution :Entry");
		return skipCurrentTP_Execution;
	}

/*	public DutCalibConfigModel getCalibParsedData() {
		return calibParsedData;
	}

	public void setCalibParsedData(DutCalibConfigModel calibParsedData) {
		this.calibParsedData = calibParsedData;
	}*/
	
/*	public ArrayList<DutDataLogParse> getDataLogParsedResultMap(int lduAddress) {
		
		ApplicationLauncher.logger.debug("getDataLogParsedResultMap : lduAddress: " + lduAddress);
		ArrayList<DutDataLogParse> dataLog = new ArrayList<DutDataLogParse>();
		try{
			if(dataLogParsedResultMap.containsKey(lduAddress)){
				dataLog = dataLogParsedResultMap.get(lduAddress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResultMap : Exception :"+ e.getMessage());
		}
		return dataLog;
	}
	
	public void addDataLogParsedResultMap(final DutDataLogParse  dataLogParsedResult, int lduAddress) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		
		ApplicationLauncher.logger.debug("addDataLogParsedResultMap : lduAddress: " + lduAddress);
		
		try{
			if(dataLogParsedResultMap.containsKey(lduAddress)){
				dataLogParsedResultMap.get(lduAddress).add(dataLogParsedResult);
				ApplicationLauncher.logger.debug("addDataLogParsedResultMap : added to existing list");
			}else{
				ArrayList<DutDataLogParse> dataLogList = new ArrayList<DutDataLogParse>();
				dataLogList.add(dataLogParsedResult);
				dataLogParsedResultMap.put(lduAddress, dataLogList);
				ApplicationLauncher.logger.debug("addDataLogParsedResultMap : added to new list");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResultMap : Exception :"+ e.getMessage());
		}
	}
	
	
	public void clearDataLogParsedResultMap() {
		dataLogParsedResultMap.clear();
	}

	public void setDataLogParsedResultMap( int lduAddress, ArrayList<DutDataLogParse> dataLogParsedResultMap) {
		this.dataLogParsedResultMap.put(lduAddress, dataLogParsedResultMap);
	}*/
	
	
	public ArrayList<DutDataLogParse> getDataLogParsedResult() {
		
		ApplicationLauncher.logger.debug("getDataLogParsedResult : dutAddress: " + dutAddress);
		ArrayList<DutDataLogParse> dataLog = new ArrayList<DutDataLogParse>();
		try{
/*			if(dataLogParsedResultMap.containsKey(lduAddress)){
				dataLog = dataLogParsedResultMap.get(lduAddress);
			}*/
			dataLog = dataLogParsedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :"+ e.getMessage());
		}
		return dataLog;
	}
	
	public void addDataLogParsedResult(final DutDataLogParse  data_LogParsedResult) {
		//DeviceDataManagerController.dataLogParsedResultMap = dataLogParsedResultMap;
		
		ApplicationLauncher.logger.debug("addDataLogParsedResult : dutAddress: " + dutAddress);
		
		try{
/*			if(dataLogParsedResultMap.containsKey(lduAddress)){
				dataLogParsedResultMap.get(lduAddress).add(dataLogParsedResult);
				ApplicationLauncher.logger.debug("addDataLogParsedResult : added to existing list");
			}else{
				ArrayList<DutDataLogParse> dataLogList = new ArrayList<DutDataLogParse>();
				dataLogList.add(dataLogParsedResult);
				dataLogParsedResultMap.put(lduAddress, dataLogList);
				ApplicationLauncher.logger.debug("addDataLogParsedResult : added to new list");
			}*/
			//ArrayList<DutDataLogParse> dataLogList = new ArrayList<DutDataLogParse>();
			//dataLogList.add(dataLogParsedResult);
			dataLogParsedResult.add(data_LogParsedResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :"+ e.getMessage());
		}
	}
	
	
	public void clearDataLogParsedResult() {
		dataLogParsedResult.clear();
	}

	public void setDataLogParsedResult(  ArrayList<DutDataLogParse> data_LogParsedResult) {
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
