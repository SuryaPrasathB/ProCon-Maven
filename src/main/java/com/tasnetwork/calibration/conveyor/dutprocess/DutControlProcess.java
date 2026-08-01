package com.tasnetwork.calibration.conveyor.dutprocess;

import java.util.ArrayList;
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
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

public class DutControlProcess {

	private ArrayList<DutDataLogParse> refStdResult = new ArrayList<>();

	private volatile boolean dutProcessExecutionStarted = false;
	private volatile boolean dutProcessExecutionCompleted = false;
	private volatile int dutAddress = 0;

	private volatile String dutSerialNumber = "";

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

	private volatile boolean refStdLogResultsStopRequested = false;

	private HashMap<Integer, String> dutResultSummary = new HashMap<>();

	private ArrayList<ResultDataModel> selectedResultData = new ArrayList<>();
	private ArrayList<DutDataLogParse> dataLogParsedResult = new ArrayList<>();

	private int error_count = 1;

	public void processCalibMenuCommand(int lduReadAddress) {
		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: Entry");
		ApplicationLauncher.logger.info("Dut: processCalibMenuCommand: lduReadAddress : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		setDutProcessExecutionCompleted(true);
	}

	public boolean executeDutCalibrationProcess(int lduReadAddress) {
		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			status = devSysEnergyMeter.calibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
	}

	public boolean executeDutPhaseCalibrationProcess(int lduReadAddress) {
		ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutPhaseCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			status = devSysEnergyMeter.phaseCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
	}

	public boolean executeDutNeutralCalibrationProcess(int lduReadAddress) {
		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeDutNeutralCalibrationProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		boolean status = false;
		if (ProconFeatureEnable.DEV_SYS_EM1_CONNECTED) {
			DevSysEnergyMeter devSysEnergyMeter = new DevSysEnergyMeter(Calib.logger);
			status = devSysEnergyMeter.neutralCalibrationProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return status;
	}

	public boolean executeFtProcess(int lduReadAddress) {
		ApplicationLauncher.logger.info("Dut: executeFtProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);
		boolean status = false;
		Map<String, Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false);
		if (ProconFeatureEnable.FT_SOURCE_CONNECTED) {
			S08_functional_Test s08_functional_Test = new S08_functional_Test();
			responseReturn = s08_functional_Test.functionalTestProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);
		status = (boolean) responseReturn.get("status");
		return status;
	}

	public Map<String, Object> executeCalibrationQrScanProcess(int lduReadAddress) {
		Map<String, Object> responseReturn = new HashMap<>();
		responseReturn.put("status", false);
		responseReturn.put("responseData", "");
		responseReturn.put("positionId", lduReadAddress);
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeCalibrationQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);

		TerminalProfileSetting terminalBayProfile = MySqlServiceManager.getTerminalProfileSettingService()
				.findByBayKey(ConstantConveyor.HV_BAY_KEY);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
			NewlandQRCodeScanner newlandQRCodeScanner = new NewlandQRCodeScanner(terminalBayProfile);
			String scannedData = newlandQRCodeScanner.scan_QR_code(lduReadAddress);
			if (scannedData != null && !scannedData.isEmpty()) {
				responseReturn.put("status", true);
				responseReturn.put("responseData", scannedData);
			}
		}
		setDutProcessExecutionCompleted(true);

		return responseReturn;
	}

	public BayResponse executeFtQrScanProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();

		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: Entry");
		ApplicationLauncher.logger.info("Dut: executeFtQrScanProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
			S07_qR_Code_Scanning_of_Meters s07_qR_Code_Scanning_of_Meters = new S07_qR_Code_Scanning_of_Meters();
			bayResponse = s07_qR_Code_Scanning_of_Meters.dutQrScanSerialNoProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;
	}

	public BayResponse executeFtWriteMeterSerialNoToDutProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();

		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterSerialNoToDutProcess: Entry");
		ApplicationLauncher.logger
				.info("Dut: executeFtWriteMeterSerialNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
			S073_Set_Serial_Numbers_On_Meters s0703_Set_Serial_Numbers_On_Meters = new S073_Set_Serial_Numbers_On_Meters();
			bayResponse = s0703_Set_Serial_Numbers_On_Meters.dutOpticalSerialNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;
	}

	public BayResponse executeFtWriteMeterHardwareIdNoToDutProcess(int lduReadAddress) {
		BayResponse bayResponse = new BayResponse();

		ApplicationLauncher.logger.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: Entry");
		ApplicationLauncher.logger
				.info("Dut: executeFtWriteMeterHardwareIdNoToDutProcess: position No : " + lduReadAddress);
		setDutAddress(lduReadAddress);

		if (ProconFeatureEnable.NEWLAND_QR_SCANNER_CONNECTED) {
			S074_Set_HardwareId_On_Meters s074_Set_HardwareId_On_Meters = new S074_Set_HardwareId_On_Meters();
			bayResponse = s074_Set_HardwareId_On_Meters.dutOpticalHardwareIdNoWriteProcess(lduReadAddress);
		}
		setDutProcessExecutionCompleted(true);

		return bayResponse;
	}

	public void updateDB_DeviceLDU_CalibrationErrorDataV2_1(ResultDataModel resultData, String DataType,
			String validationType, String validationType2, String validationType2ResultStatus,
			String validationType2ResultData,
			String overAllResultStatus, String errorMin2, String errorMax2, String averageType, String averageCount) {
		ApplicationLauncher.logger
				.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry: " + getSkipCurrentTP_Execution());
		if (!getSkipCurrentTP_Execution()) {
			ApplicationLauncher.logger.info("Dut: updateDB_DeviceLDU_CalibrationErrorDataV2_1 : Entry2");
		}
	}

	public void UpdateDB_DeviceLDU_ErrorDataV2_1(int LDU_ReadAddress, String Resultstatus, String ErrorValue,
			String DataType, String averageType, String averageCount) {
	}

	public void Sleep(int timeInMsec) {
		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			ApplicationLauncher.logger.error("Dut: Sleep :InterruptedException:" + e.getMessage());
			Thread.currentThread().interrupt();
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
		ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : delayValue: " + delayValue);
			ApplicationLauncher.logger.debug("Dut: setPresentLagValidationDelay : added to new list");
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: setPresentLagValidationDelay : Exception :" + e.getMessage(), e);
		}
	}

	public void setPresentValidationDataValue(final int dataValue) {
		ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : dataValue: " + dataValue);
			ApplicationLauncher.logger.debug("Dut: setPresentValidationDataValue : added to new list");
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: setPresentValidationDataValue : Exception :" + e.getMessage(), e);
		}
	}

	public void setPresentValidation2DataValue(final int dataValue) {
		ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dutAddress: " + dutAddress);
			ApplicationLauncher.logger.debug("Dut: setPresentValidation2DataValue : dataValue: " + dataValue);
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: setPresentValidation2DataValue : Exception :" + e.getMessage(), e);
		}
	}

	public String getPresentResultStatus() {
		ApplicationLauncher.logger.debug("Dut: getPresentResultStatus : dutAddress: " + dutAddress);
		String resultData = new String();
		try {
			resultData = presentResultStatus;
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: getPresentResultStatus : Exception :" + e.getMessage(), e);
		}
		return resultData;
	}

	public void setPresentResultStatus(final String resultData) {
		ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : dutAddress: " + dutAddress);
			presentResultStatus = resultData;
			ApplicationLauncher.logger.debug("Dut: setPresentResultStatus : added to new list");
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: setPresentResultStatus : Exception :" + e.getMessage(), e);
		}
	}

	public void clearPresentResultStatus() {
		presentResultStatus = "";
	}

	public String getPresentResultError() {
		ApplicationLauncher.logger.debug("Dut: getPresentResultError : dutAddress: " + dutAddress);
		String resultData = new String();
		try {
			resultData = presentResultError;
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: getPresentResultError : Exception :" + e.getMessage(), e);
		}
		return resultData;
	}

	public void setPresentResultError(final String resultData) {
		ApplicationLauncher.logger.debug("Dut: setPresentResultError : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: setPresentResultError : dutAddress: " + dutAddress);
			presentResultError = resultData;
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: setPresentResultError : Exception :" + e.getMessage(), e);
		}
	}

	public void clearPresentResultError() {
		presentResultError = "";
	}

	public String getDutResultSummary(int lduAddress) {
		String dutSummaryStatus = ConstantReport.RESULT_STATUS_PASS.trim();
		try {
			if (dutResultSummary.containsKey(lduAddress)) {
				dutSummaryStatus = dutResultSummary.get(lduAddress);
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: getDutResultSummary : Exception :" + e.getMessage(), e);
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
		ArrayList<ResultDataModel> resultData = new ArrayList<>();
		try {
			resultData = selectedResultData;
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: getSelectedResultData : Exception :" + e.getMessage(), e);
		}
		return resultData;
	}

	public void addSelectedResultData(final ResultDataModel resultData) {
		ApplicationLauncher.logger.debug("Dut: addSelectedResultData : Entry");
		try {
			ApplicationLauncher.logger.debug("Dut: addSelectedResultData : dutAddress: " + dutAddress);
			selectedResultData.add(resultData);
		} catch (Exception e) {
			ApplicationLauncher.logger.error("Dut: addSelectedResultData : Exception :" + e.getMessage(), e);
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
		return error_count++;
	}

	public void setSkipCurrentTP_Execution(boolean status) {
		ApplicationLauncher.logger.debug("Dut: setSkipCurrentTP_Execution :Entry:status:" + status);
		skipCurrentTP_Execution = status;
	}

	public boolean getSkipCurrentTP_Execution() {
		return skipCurrentTP_Execution;
	}

	public ArrayList<DutDataLogParse> getDataLogParsedResult() {
		ApplicationLauncher.logger.debug("getDataLogParsedResult : dutAddress: " + dutAddress);
		ArrayList<DutDataLogParse> dataLog = new ArrayList<>();
		try {
			dataLog = dataLogParsedResult;
		} catch (Exception e) {
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :" + e.getMessage(), e);
		}
		return dataLog;
	}

	public void addDataLogParsedResult(final DutDataLogParse data_LogParsedResult) {
		ApplicationLauncher.logger.debug("addDataLogParsedResult : dutAddress: " + dutAddress);
		try {
			dataLogParsedResult.add(data_LogParsedResult);
		} catch (Exception e) {
			ApplicationLauncher.logger.error("getDataLogParsedResult : Exception :" + e.getMessage(), e);
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
		this.refStdResult = ref_StdResult;
	}

	public boolean isRefStdLogResultsStopRequested() {
		return refStdLogResultsStopRequested;
	}

	public void setRefStdLogResultsStopRequested(boolean refStdLogResultsStopRequested) {
		this.refStdLogResultsStopRequested = refStdLogResultsStopRequested;
	}

}
