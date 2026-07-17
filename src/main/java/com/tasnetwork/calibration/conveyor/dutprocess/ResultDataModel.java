package com.tasnetwork.calibration.conveyor.dutprocess;

public class ResultDataModel {
	
	private String reportSerialNo = "1";
	private String testCaseName = "";
	private String aliasID = "";
	private String deviceName = "";
	private String testResultStatus = "";
	private String errorValue = "";
	
	//private String readingId = "0" = "";
	
	//private String phaseLabel = "";
	private String readingNumber = ""; // error_reading_id
	private String deployedSerialNo = "";
	private String testType = "";
	private String executionStatus = "";
	
	private String energyMetricUnit = "";
	private String dutLoraId = "";
	
	private String acceptableErrorMinLimit = "";
	private String acceptableErrorMaxLimit = "";
	
	
	private String targetR_PhaseVoltage = "";
	private String targetR_PhaseCurrent = "";
	private String targetR_PhasePhaseAngle = "";
	private String targetR_PhasePf = "";
	
	private String targetY_PhaseVoltage = "";
	private String targetY_PhaseCurrent = "";
	private String targetY_PhasePhaseAngle = "";
	private String targetY_PhasePf = "";
	
	private String targetB_PhaseVoltage = "";
	private String targetB_PhaseCurrent = "";
	private String targetB_PhasePhaseAngle = "";
	private String targetB_PhasePf = "";
	
	private String targetFreq = "";

	
	private String refStdTimeStampEpoch = "";
	private String refStdDateHuman = "";
	private String refStdTimeHuman = "";
	
	private String actualRefStd_R_PhaseVoltage = "";
	private String actualRefStd_R_PhaseCurrent = "";
	private String actualRefStd_R_PhaseActivePowerWatt = "";
	private String actualRefStd_R_PhaseReactivePowerVAR = "";
	private String actualRefStd_R_PhaseApparentPowerVA = "";
	private String actualRefStd_R_PhasePf = "";
	private String actualRefStd_R_PhaseDegreeAngle = "";
	
	private String actualRefStd_Y_PhaseVoltage = "";
	private String actualRefStd_Y_PhaseCurrent = "";
	private String actualRefStd_Y_PhaseActivePowerWatt = "";
	private String actualRefStd_Y_PhaseReactivePowerVAR = "";
	private String actualRefStd_Y_PhaseApparentPowerVA = "";
	private String actualRefStd_Y_PhasePf = "";
	private String actualRefStd_Y_PhaseDegreeAngle = "";
	
	private String actualRefStd_B_PhaseVoltage = "";
	private String actualRefStd_B_PhaseCurrent = "";
	private String actualRefStd_B_PhaseActivePowerWatt = "";
	private String actualRefStd_B_PhaseReactivePowerVAR = "";
	private String actualRefStd_B_PhaseApparentPowerVA = "";
	private String actualRefStd_B_PhasePf = "";
	private String actualRefStd_B_PhaseDegreeAngle = "";
	
	private String actualRefStdFreq = "";
	
	
	
	private String appDutTimeStampEpoch = "";
	private String dutTimeStampEpoch = "";
	//private SimpleStringProperty dutSerialNo = new SimpleStringProperty();
	private String dutSerialNo = "";
	private String dutDateHuman = "";
	private String dutTimeHuman = "";
	
	private String actualDut_R_PhaseVoltage = "";
	private String actualDut_R_PhaseCurrent = "";
	private String actualDut_R_PhaseActivePowerWatt = "";
	private String actualDut_R_PhaseReactivePowerVAR = "";
	private String actualDut_R_PhaseApparentPowerVA = "";
	private String actualDut_R_PhasePf = "";
	private String actualDut_R_PhaseDegreeAngle = "";
	
	private String actualDut_Y_PhaseVoltage = "";
	private String actualDut_Y_PhaseCurrent = "";
	private String actualDut_Y_PhaseActivePowerWatt = "";
	private String actualDut_Y_PhaseReactivePowerVAR = "";
	private String actualDut_Y_PhaseApparentPowerVA = "";
	private String actualDut_Y_PhasePf = "";
	private String actualDut_Y_PhaseDegreeAngle = "";
	
	private String actualDut_B_PhaseVoltage = "";
	private String actualDut_B_PhaseCurrent = "";
	private String actualDut_B_PhaseActivePowerWatt = "";
	private String actualDut_B_PhaseReactivePowerVAR = "";
	private String actualDut_B_PhaseApparentPowerVA = "";
	private String actualDut_B_PhasePf = "";
	private String actualDut_B_PhaseDegreeAngle = "";
	
	private String actualDutFreq = "";
		
	private DutDataLogParse actualDut_R_PhaseData = new DutDataLogParse();
	private DutDataLogParse actualDut_Y_PhaseData = new DutDataLogParse();
	private DutDataLogParse actualDut_B_PhaseData = new DutDataLogParse();
	
	private String averageType = "";
	private String averageCount = "";
	
	private String validationType = "";
	private String validationType2 = "";
	private String errorValue2 = "";
	private String acceptableErrorMin2 = "";
	private String acceptableErrorMax2 = "";
	
	private String acceptableErrorMin = "";
	private String acceptableErrorMax = "";
	
	private String overAllResultStatus = "";
	
	public ResultDataModel(){
		
	}
	
/*	public ResultDataModel(DataLogParse rPhaseData, DataLogParse yPhaseData, DataLogParse bPhaseData){
		
		this.appDutTimeStampEpoch = rPhaseData.getAppDutTimeStampEpoch();
		
		this.dutSerialNo = rPhaseData.getSerialNo();
		this.dutDateHuman = rPhaseData.getDate();
		this.dutTimeHuman = rPhaseData.getTimeStamp();
		this.actualDutFreq = rPhaseData.getFreqency();
		
		this.actualDut_R_PhaseVoltage = rPhaseData.getVoltage();
		this.actualDut_R_PhaseCurrent = rPhaseData.getCurrent();
		this.actualDut_R_PhaseActivePowerWatt = rPhaseData.getActivePowerWatt();
		this.actualDut_R_PhasePf = rPhaseData.getPf();
		this.actualDut_R_PhaseDegreeAngle = "";
		
		this.actualDut_Y_PhaseVoltage = yPhaseData.getVoltage();
		this.actualDut_Y_PhaseCurrent = yPhaseData.getCurrent();
		this.actualDut_Y_PhasePower = yPhaseData.getActivePowerWatt();
		this.actualDut_Y_PhasePf = yPhaseData.getPf();
		this.actualDut_Y_PhaseDegreeAngle = "";
		
		this.actualDut_B_PhaseVoltage = bPhaseData.getVoltage();
		this.actualDut_B_PhaseCurrent = bPhaseData.getCurrent();
		this.actualDut_B_PhasePower = bPhaseData.getActivePowerWatt();
		this.actualDut_B_PhasePf = bPhaseData.getPf();
		this.actualDut_B_PhaseDegreeAngle = "";
		
		
		
	}*/
	
	public void updateRefStdDataLogParseRphase(DutDataLogParse rPhaseData){
		this.refStdTimeStampEpoch = rPhaseData.getAppRefStdTimeStampEpoch();
		
		//this.dutSerialNo = rPhaseData.getSerialNo();
		this.refStdDateHuman = rPhaseData.getDate();
		this.refStdTimeHuman = rPhaseData.getTimeStamp();
		this.actualRefStdFreq = rPhaseData.getFreqency();
		
		this.actualRefStd_R_PhaseVoltage = rPhaseData.getVoltage();
		this.actualRefStd_R_PhaseCurrent = rPhaseData.getCurrent();
		this.actualRefStd_R_PhaseActivePowerWatt = rPhaseData.getActivePowerWatt();
		this.actualRefStd_R_PhaseApparentPowerVA = rPhaseData.getApparentPowerVA();
		this.actualRefStd_R_PhasePf = rPhaseData.getPf();
		this.actualRefStd_R_PhaseDegreeAngle = "";
	}
	
	public void updateRefStdDataLogParseYphase(DutDataLogParse yPhaseData){
		//this.refStdTimeStampEpoch = yPhaseData.getAppDutTimeStampEpoch();
		
		//this.dutSerialNo = rPhaseData.getSerialNo();
		//this.refStdDateHuman = yPhaseData.getDate();
		//this.refStdTimeHuman = yPhaseData.getTimeStamp();
		//this.actualRefStdFreq = yPhaseData.getFreqency();
		
		this.actualRefStd_Y_PhaseVoltage = yPhaseData.getVoltage();
		this.actualRefStd_Y_PhaseCurrent = yPhaseData.getCurrent();
		this.actualRefStd_Y_PhaseActivePowerWatt = yPhaseData.getActivePowerWatt();
		this.actualRefStd_Y_PhaseApparentPowerVA = yPhaseData.getApparentPowerVA();
		this.actualRefStd_Y_PhasePf = yPhaseData.getPf();
		this.actualRefStd_Y_PhaseDegreeAngle = yPhaseData.getDegreePhaseAngle();
	}
	
	public void updateRefStdDataLogParseBphase(DutDataLogParse bPhaseData){
		//this.refStdTimeStampEpoch = yPhaseData.getAppDutTimeStampEpoch();
		
		//this.dutSerialNo = rPhaseData.getSerialNo();
		//this.refStdDateHuman = yPhaseData.getDate();
		//this.refStdTimeHuman = yPhaseData.getTimeStamp();
		//this.actualRefStdFreq = yPhaseData.getFreqency();
		
		this.actualRefStd_B_PhaseVoltage = bPhaseData.getVoltage();
		this.actualRefStd_B_PhaseCurrent = bPhaseData.getCurrent();
		this.actualRefStd_B_PhaseActivePowerWatt = bPhaseData.getActivePowerWatt();
		this.actualRefStd_B_PhaseApparentPowerVA = bPhaseData.getApparentPowerVA();
		this.actualRefStd_B_PhasePf = bPhaseData.getPf();
		this.actualRefStd_B_PhaseDegreeAngle = bPhaseData.getDegreePhaseAngle();
	}
	
	
	
	
	public void updateDutDataLogParseRPhase(DutDataLogParse rPhaseData){
		this.appDutTimeStampEpoch = rPhaseData.getAppDutTimeStampEpoch();
		this.dutTimeStampEpoch = rPhaseData.getDutTimeStampEpoch();
		
		this.dutSerialNo = rPhaseData.getSerialNo();
		this.dutDateHuman = rPhaseData.getDate();
		this.dutTimeHuman = rPhaseData.getTimeStamp();
		this.actualDutFreq = rPhaseData.getFreqency();
		
		this.actualDut_R_PhaseVoltage = rPhaseData.getVoltage();
		this.actualDut_R_PhaseCurrent = rPhaseData.getCurrent();
		this.actualDut_R_PhaseActivePowerWatt = rPhaseData.getActivePowerWatt();
		this.actualDut_R_PhaseReactivePowerVAR = rPhaseData.getReactivePowerVAR();
		this.actualDut_R_PhaseApparentPowerVA = rPhaseData.getApparentPowerVA();
		this.actualDut_R_PhasePf = rPhaseData.getPf();
		this.actualDut_R_PhaseDegreeAngle = "";
	}
	
	public void updateDutDataLogParseYPhase(DutDataLogParse yPhaseData){
		
		this.actualDut_Y_PhaseVoltage = yPhaseData.getVoltage();
		this.actualDut_Y_PhaseCurrent = yPhaseData.getCurrent();
		this.actualDut_Y_PhaseActivePowerWatt = yPhaseData.getActivePowerWatt();
		this.actualDut_Y_PhaseReactivePowerVAR = yPhaseData.getReactivePowerVAR();
		this.actualDut_Y_PhaseApparentPowerVA = yPhaseData.getApparentPowerVA();
		this.actualDut_Y_PhasePf = yPhaseData.getPf();
		this.actualDut_Y_PhaseDegreeAngle = "";
		
	}
	
	public void updateDutDataLogParseBPhase(DutDataLogParse bPhaseData){
		
		this.actualDut_B_PhaseVoltage = bPhaseData.getVoltage();
		this.actualDut_B_PhaseCurrent = bPhaseData.getCurrent();
		this.actualDut_B_PhaseActivePowerWatt = bPhaseData.getActivePowerWatt();
		this.actualDut_B_PhaseReactivePowerVAR = bPhaseData.getReactivePowerVAR();
		this.actualDut_B_PhaseApparentPowerVA = bPhaseData.getApparentPowerVA();
		this.actualDut_B_PhasePf = bPhaseData.getPf();
		this.actualDut_B_PhaseDegreeAngle = "";
		
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public String getAliasID() {
		return aliasID;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getTestResultStatus() {
		return testResultStatus;
	}

	public String getErrorValue() {
		return errorValue;
	}

/*	public String getPhaseLabel() {
		return phaseLabel;
	}*/

	public String getReadingNumber() {
		return readingNumber;
	}

	public String getDeployedSerialNo() {
		return deployedSerialNo;
	}

	public String getTestType() {
		return testType;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public String getEnergyMetricUnit() {
		return energyMetricUnit;
	}

	public String getDutLoraId() {
		return dutLoraId;
	}

	public String getAcceptableErrorMinLimit() {
		return acceptableErrorMinLimit;
	}

	public String getAcceptableErrorMaxLimit() {
		return acceptableErrorMaxLimit;
	}

	public String getTargetR_PhaseVoltage() {
		return targetR_PhaseVoltage;
	}

	public String getTargetR_PhaseCurrent() {
		return targetR_PhaseCurrent;
	}

	public String getTargetR_PhasePhaseAngle() {
		return targetR_PhasePhaseAngle;
	}

	public String getTargetR_PhasePf() {
		return targetR_PhasePf;
	}

	public String getTargetY_PhaseVoltage() {
		return targetY_PhaseVoltage;
	}

	public String getTargetY_PhaseCurrent() {
		return targetY_PhaseCurrent;
	}

	public String getTargetY_PhasePhaseAngle() {
		return targetY_PhasePhaseAngle;
	}

	public String getTargetY_PhasePf() {
		return targetY_PhasePf;
	}

	public String getTargetB_PhaseVoltage() {
		return targetB_PhaseVoltage;
	}

	public String getTargetB_PhaseCurrent() {
		return targetB_PhaseCurrent;
	}

	public String getTargetB_PhasePhaseAngle() {
		return targetB_PhasePhaseAngle;
	}

	public String getTargetB_PhasePf() {
		return targetB_PhasePf;
	}

	public String getTargetFreq() {
		return targetFreq;
	}

	public String getRefStdTimeStampEpoch() {
		return refStdTimeStampEpoch;
	}

	public String getRefStdDateHuman() {
		return refStdDateHuman;
	}

	public String getRefStdTimeHuman() {
		return refStdTimeHuman;
	}

	public String getActualRefStd_R_PhaseVoltage() {
		return actualRefStd_R_PhaseVoltage;
	}

	public String getActualRefStd_R_PhaseCurrent() {
		return actualRefStd_R_PhaseCurrent;
	}

	public String getActualRefStd_R_PhaseActivePowerWatt() {
		return actualRefStd_R_PhaseActivePowerWatt;
	}
	


	public String getActualRefStd_R_PhasePf() {
		return actualRefStd_R_PhasePf;
	}

	public String getActualRefStd_R_PhaseDegreeAngle() {
		return actualRefStd_R_PhaseDegreeAngle;
	}

	public String getActualRefStd_Y_PhaseVoltage() {
		return actualRefStd_Y_PhaseVoltage;
	}

	public String getActualRefStd_Y_PhaseCurrent() {
		return actualRefStd_Y_PhaseCurrent;
	}

	public String getActualRefStd_Y_PhaseActivePowerWatt() {
		return actualRefStd_Y_PhaseActivePowerWatt;
	}

	public String getActualRefStd_Y_PhasePf() {
		return actualRefStd_Y_PhasePf;
	}

	public String getActualRefStd_Y_PhaseDegreeAngle() {
		return actualRefStd_Y_PhaseDegreeAngle;
	}

	public String getActualRefStd_B_PhaseVoltage() {
		return actualRefStd_B_PhaseVoltage;
	}

	public String getActualRefStd_B_PhaseCurrent() {
		return actualRefStd_B_PhaseCurrent;
	}

	public String getActualRefStd_B_PhaseActivePowerWatt() {
		return actualRefStd_B_PhaseActivePowerWatt;
	}

	public String getActualRefStd_B_PhasePf() {
		return actualRefStd_B_PhasePf;
	}

	public String getActualRefStd_B_PhaseDegreeAngle() {
		return actualRefStd_B_PhaseDegreeAngle;
	}

	public String getActualRefStdFreq() {
		return actualRefStdFreq;
	}

	public String getAppDutTimeStampEpoch() {
		return appDutTimeStampEpoch;
	}
	
	public String getDutTimeStampEpoch() {
		return dutTimeStampEpoch;
	}

	public String getDutSerialNo() {
		return dutSerialNo;
	}
	
/*	public String getDutSerialNo() {
		return dutSerialNo;
	}*/

	public String getDutDateHuman() {
		return dutDateHuman;
	}

	public String getDutTimeHuman() {
		return dutTimeHuman;
	}

	public String getActualDut_R_PhaseVoltage() {
		return actualDut_R_PhaseVoltage;
	}

	public String getActualDut_R_PhaseCurrent() {
		return actualDut_R_PhaseCurrent;
	}

/*	public String getActualDut_R_PhasePower() {
		return actualDut_R_PhasePower;
	}
*/
	public String getActualDut_R_PhasePf() {
		return actualDut_R_PhasePf;
	}

	public String getActualDut_R_PhaseDegreeAngle() {
		return actualDut_R_PhaseDegreeAngle;
	}

	public String getActualDut_Y_PhaseVoltage() {
		return actualDut_Y_PhaseVoltage;
	}

	public String getActualDut_Y_PhaseCurrent() {
		return actualDut_Y_PhaseCurrent;
	}

/*	public String getActualDut_Y_PhasePower() {
		return actualDut_Y_PhasePower;
	}*/

	public String getActualDut_Y_PhasePf() {
		return actualDut_Y_PhasePf;
	}

	public String getActualDut_Y_PhaseDegreeAngle() {
		return actualDut_Y_PhaseDegreeAngle;
	}

	public String getActualDut_B_PhaseVoltage() {
		return actualDut_B_PhaseVoltage;
	}

	public String getActualDut_B_PhaseCurrent() {
		return actualDut_B_PhaseCurrent;
	}

/*	public String getActualDut_B_PhasePower() {
		return actualDut_B_PhasePower;
	}*/

	public String getActualDut_B_PhasePf() {
		return actualDut_B_PhasePf;
	}

	public String getActualDut_B_PhaseDegreeAngle() {
		return actualDut_B_PhaseDegreeAngle;
	}

	public String getActualDutFreq() {
		return actualDutFreq;
	}

	public DutDataLogParse getActualDut_R_PhaseData() {
		return actualDut_R_PhaseData;
	}

	public DutDataLogParse getActualDut_Y_PhaseData() {
		return actualDut_Y_PhaseData;
	}

	public DutDataLogParse getActualDut_B_PhaseData() {
		return actualDut_B_PhaseData;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public void setAliasID(String aliasID) {
		this.aliasID = aliasID;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setTestResultStatus(String testResult) {
		this.testResultStatus = testResult;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

/*	public void setPhaseLabel(String phaseLabel) {
		this.phaseLabel = phaseLabel;
	}*/

	public void setReadingNumber(String readingNumber) {
		this.readingNumber = readingNumber;
	}

	public void setDeployedSerialNo(String deployedSerialNo) {
		this.deployedSerialNo = deployedSerialNo;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public void setEnergyMetricUnit(String energyMetricUnit) {
		this.energyMetricUnit = energyMetricUnit;
	}

	public void setDutLoraId(String loraId) {
		this.dutLoraId = loraId;
	}

	public void setAcceptableErrorMinLimit(String acceptableErrorMinLimit) {
		this.acceptableErrorMinLimit = acceptableErrorMinLimit;
	}

	public void setAcceptableErrorMaxLimit(String acceptableErrorMaxLimit) {
		this.acceptableErrorMaxLimit = acceptableErrorMaxLimit;
	}

	public void setTargetR_PhaseVoltage(String targetR_PhaseVoltage) {
		this.targetR_PhaseVoltage = targetR_PhaseVoltage;
	}

	public void setTargetR_PhaseCurrent(String targetR_PhaseCuurent) {
		this.targetR_PhaseCurrent = targetR_PhaseCuurent;
	}

	public void setTargetR_PhasePhaseAngle(String targetR_PhasePhaseAngle) {
		this.targetR_PhasePhaseAngle = targetR_PhasePhaseAngle;
	}

	public void setTargetR_PhasePf(String targetR_PhasePf) {
		this.targetR_PhasePf = targetR_PhasePf;
	}

	public void setTargetY_PhaseVoltage(String targetY_PhaseVoltage) {
		this.targetY_PhaseVoltage = targetY_PhaseVoltage;
	}

	public void setTargetY_PhaseCurrent(String targetY_PhaseCuurent) {
		this.targetY_PhaseCurrent = targetY_PhaseCuurent;
	}

	public void setTargetY_PhasePhaseAngle(String targetY_PhasePhaseAngle) {
		this.targetY_PhasePhaseAngle = targetY_PhasePhaseAngle;
	}

	public void setTargetY_PhasePf(String targetY_PhasePf) {
		this.targetY_PhasePf = targetY_PhasePf;
	}

	public void setTargetB_PhaseVoltage(String targetB_PhaseVoltage) {
		this.targetB_PhaseVoltage = targetB_PhaseVoltage;
	}

	public void setTargetB_PhaseCurrent(String targetB_PhaseCuurent) {
		this.targetB_PhaseCurrent = targetB_PhaseCuurent;
	}

	public void setTargetB_PhasePhaseAngle(String targetB_PhasePhaseAngle) {
		this.targetB_PhasePhaseAngle = targetB_PhasePhaseAngle;
	}

	public void setTargetB_PhasePf(String targetB_PhasePf) {
		this.targetB_PhasePf = targetB_PhasePf;
	}

	public void setTargetFreq(String targetFreq) {
		this.targetFreq = targetFreq;
	}

	public void setRefStdTimeStampEpoch(String refStdTimeStampEpoch) {
		this.refStdTimeStampEpoch = refStdTimeStampEpoch;
	}

	public void setRefStdDateHuman(String refStdDateHuman) {
		this.refStdDateHuman = refStdDateHuman;
	}

	public void setRefStdTimeHuman(String refStdTimeHuman) {
		this.refStdTimeHuman = refStdTimeHuman;
	}

	public void setActualRefStd_R_PhaseVoltage(String actualRefStd_R_PhaseVoltage) {
		this.actualRefStd_R_PhaseVoltage = actualRefStd_R_PhaseVoltage;
	}

	public void setActualRefStd_R_PhaseCurrent(String actualRefStd_R_PhaseCurrent) {
		this.actualRefStd_R_PhaseCurrent = actualRefStd_R_PhaseCurrent;
	}

	public void setActualRefStd_R_PhaseActivePowerWatt(String actualRefStd_R_PhasePower) {
		this.actualRefStd_R_PhaseActivePowerWatt = actualRefStd_R_PhasePower;
	}

	public void setActualRefStd_R_PhasePf(String actualRefStd_R_PhasePf) {
		this.actualRefStd_R_PhasePf = actualRefStd_R_PhasePf;
	}

	public void setActualRefStd_R_PhaseDegreeAngle(String actualRefStd_R_PhaseDegreeAngle) {
		this.actualRefStd_R_PhaseDegreeAngle = actualRefStd_R_PhaseDegreeAngle;
	}

	public void setActualRefStd_Y_PhaseVoltage(String actualRefStd_Y_PhaseVoltage) {
		this.actualRefStd_Y_PhaseVoltage = actualRefStd_Y_PhaseVoltage;
	}

	public void setActualRefStd_Y_PhaseCurrent(String actualRefStd_Y_PhaseCurrent) {
		this.actualRefStd_Y_PhaseCurrent = actualRefStd_Y_PhaseCurrent;
	}

	public void setActualRefStd_Y_PhaseActivePowerWatt(String actualRefStd_Y_PhasePower) {
		this.actualRefStd_Y_PhaseActivePowerWatt = actualRefStd_Y_PhasePower;
	}

	public void setActualRefStd_Y_PhasePf(String actualRefStd_Y_PhasePf) {
		this.actualRefStd_Y_PhasePf = actualRefStd_Y_PhasePf;
	}

	public void setActualRefStd_Y_PhaseDegreeAngle(String actualRefStd_Y_PhaseDegreeAngle) {
		this.actualRefStd_Y_PhaseDegreeAngle = actualRefStd_Y_PhaseDegreeAngle;
	}

	public void setActualRefStd_B_PhaseVoltage(String actualRefStd_B_PhaseVoltage) {
		this.actualRefStd_B_PhaseVoltage = actualRefStd_B_PhaseVoltage;
	}

	public void setActualRefStd_B_PhaseCurrent(String actualRefStd_B_PhaseCurrent) {
		this.actualRefStd_B_PhaseCurrent = actualRefStd_B_PhaseCurrent;
	}

	public void setActualRefStd_B_PhaseActivePowerWatt(String actualRefStd_B_PhasePower) {
		this.actualRefStd_B_PhaseActivePowerWatt = actualRefStd_B_PhasePower;
	}

	public void setActualRefStd_B_PhasePf(String actualRefStd_B_PhasePf) {
		this.actualRefStd_B_PhasePf = actualRefStd_B_PhasePf;
	}

	public void setActualRefStd_B_PhaseDegreeAngle(String actualRefStd_B_PhaseDegreeAngle) {
		this.actualRefStd_B_PhaseDegreeAngle = actualRefStd_B_PhaseDegreeAngle;
	}

	public void setActualRefStdFreq(String actualRefStdFreq) {
		this.actualRefStdFreq = actualRefStdFreq;
	}

	public void setAppDutTimeStampEpoch(String appDutTimeStampEpoch) {
		this.appDutTimeStampEpoch = appDutTimeStampEpoch;
	}
	
	public void setDutTimeStampEpoch(String dutTimeStampEpoch) {
		this.dutTimeStampEpoch = dutTimeStampEpoch;
	}

	public void setDutSerialNo(String dutSerialNo) {
		this.dutSerialNo = dutSerialNo;
	}

	public void setDutDateHuman(String dutStdDateHuman) {
		this.dutDateHuman = dutStdDateHuman;
	}

	public void setDutTimeHuman(String dutStdTimeHuman) {
		this.dutTimeHuman = dutStdTimeHuman;
	}

	public void setActualDut_R_PhaseVoltage(String actualDut_R_PhaseVoltage) {
		this.actualDut_R_PhaseVoltage = actualDut_R_PhaseVoltage;
	}

	public void setActualDut_R_PhaseCurrent(String actualDut_R_PhaseCurrent) {
		this.actualDut_R_PhaseCurrent = actualDut_R_PhaseCurrent;
	}

/*	public void setActualDut_R_PhasePower(String actualDut_R_PhasePower) {
		this.actualDut_R_PhasePower = actualDut_R_PhasePower;
	}*/

	public void setActualDut_R_PhasePf(String actualDut_R_PhasePf) {
		this.actualDut_R_PhasePf = actualDut_R_PhasePf;
	}

	public void setActualDut_R_PhaseDegreeAngle(String actualDut_R_PhaseDegreeAngle) {
		this.actualDut_R_PhaseDegreeAngle = actualDut_R_PhaseDegreeAngle;
	}

	public void setActualDut_Y_PhaseVoltage(String actualDut_Y_PhaseVoltage) {
		this.actualDut_Y_PhaseVoltage = actualDut_Y_PhaseVoltage;
	}

	public void setActualDut_Y_PhaseCurrent(String actualDut_Y_PhaseCurrent) {
		this.actualDut_Y_PhaseCurrent = actualDut_Y_PhaseCurrent;
	}

/*	public void setActualDut_Y_PhasePower(String actualDut_Y_PhasePower) {
		this.actualDut_Y_PhasePower = actualDut_Y_PhasePower;
	}*/

	public void setActualDut_Y_PhasePf(String actualDut_Y_PhasePf) {
		this.actualDut_Y_PhasePf = actualDut_Y_PhasePf;
	}

	public void setActualDut_Y_PhaseDegreeAngle(String actualDut_Y_PhaseDegreeAngle) {
		this.actualDut_Y_PhaseDegreeAngle = actualDut_Y_PhaseDegreeAngle;
	}

	public void setActualDut_B_PhaseVoltage(String actualDut_B_PhaseVoltage) {
		this.actualDut_B_PhaseVoltage = actualDut_B_PhaseVoltage;
	}

	public void setActualDut_B_PhaseCurrent(String actualDut_B_PhaseCurrent) {
		this.actualDut_B_PhaseCurrent = actualDut_B_PhaseCurrent;
	}

/*	public void setActualDut_B_PhasePower(String actualDut_B_PhasePower) {
		this.actualDut_B_PhasePower = actualDut_B_PhasePower;
	}*/

	public void setActualDut_B_PhasePf(String actualDut_B_PhasePf) {
		this.actualDut_B_PhasePf = actualDut_B_PhasePf;
	}

	public void setActualDut_B_PhaseDegreeAngle(String actualDut_B_PhaseDegreeAngle) {
		this.actualDut_B_PhaseDegreeAngle = actualDut_B_PhaseDegreeAngle;
	}

	public void setActualDutFreq(String actualDutFreq) {
		this.actualDutFreq = actualDutFreq;
	}

	public void setActualDut_R_PhaseData(DutDataLogParse actualDut_R_PhaseData) {
		this.actualDut_R_PhaseData = actualDut_R_PhaseData;
	}

	public void setActualDut_Y_PhaseData(DutDataLogParse actualDut_Y_PhaseData) {
		this.actualDut_Y_PhaseData = actualDut_Y_PhaseData;
	}

	public void setActualDut_B_PhaseData(DutDataLogParse actualDut_B_PhaseData) {
		this.actualDut_B_PhaseData = actualDut_B_PhaseData;
	}

	public String getActualRefStd_R_PhaseReactivePowerVAR() {
		return actualRefStd_R_PhaseReactivePowerVAR;
	}

	public String getActualRefStd_R_PhaseApparentPowerVA() {
		return actualRefStd_R_PhaseApparentPowerVA;
	}

	public String getActualRefStd_Y_PhaseReactivePowerVAR() {
		return actualRefStd_Y_PhaseReactivePowerVAR;
	}

	public String getActualRefStd_Y_PhaseApparentPowerVA() {
		return actualRefStd_Y_PhaseApparentPowerVA;
	}

	public String getActualRefStd_B_PhaseReactivePowerVAR() {
		return actualRefStd_B_PhaseReactivePowerVAR;
	}

	public String getActualRefStd_B_PhaseApparentPowerVA() {
		return actualRefStd_B_PhaseApparentPowerVA;
	}

	public void setActualRefStd_R_PhaseReactivePowerVAR(String actualRefStd_R_PhaseReactivePowerVAR) {
		this.actualRefStd_R_PhaseReactivePowerVAR = actualRefStd_R_PhaseReactivePowerVAR;
	}

	public void setActualRefStd_R_PhaseApparentPowerVA(String actualRefStd_R_PhaseApparentPowerVA) {
		this.actualRefStd_R_PhaseApparentPowerVA = actualRefStd_R_PhaseApparentPowerVA;
	}

	public void setActualRefStd_Y_PhaseReactivePowerVAR(String actualRefStd_Y_PhaseReactivePowerVAR) {
		this.actualRefStd_Y_PhaseReactivePowerVAR = actualRefStd_Y_PhaseReactivePowerVAR;
	}

	public void setActualRefStd_Y_PhaseApparentPowerVA(String actualRefStd_Y_PhaseApparentPowerVA) {
		this.actualRefStd_Y_PhaseApparentPowerVA = actualRefStd_Y_PhaseApparentPowerVA;
	}

	public void setActualRefStd_B_PhaseReactivePowerVAR(String actualRefStd_B_PhaseReactivePowerVAR) {
		this.actualRefStd_B_PhaseReactivePowerVAR = actualRefStd_B_PhaseReactivePowerVAR;
	}

	public void setActualRefStd_B_PhaseApparentPowerVA(String actualRefStd_B_PhaseApparentPowerVA) {
		this.actualRefStd_B_PhaseApparentPowerVA = actualRefStd_B_PhaseApparentPowerVA;
	}

	public String getActualDut_R_PhaseActivePowerWatt() {
		return actualDut_R_PhaseActivePowerWatt;
	}

	public String getActualDut_R_PhaseReactivePowerVAR() {
		return actualDut_R_PhaseReactivePowerVAR;
	}

	public String getActualDut_R_PhaseApparentPowerVA() {
		return actualDut_R_PhaseApparentPowerVA;
	}

	public String getActualDut_Y_PhaseActivePowerWatt() {
		return actualDut_Y_PhaseActivePowerWatt;
	}

	public String getActualDut_Y_PhaseReactivePowerVAR() {
		return actualDut_Y_PhaseReactivePowerVAR;
	}

	public String getActualDut_Y_PhaseApparentPowerVA() {
		return actualDut_Y_PhaseApparentPowerVA;
	}

	public String getActualDut_B_PhaseActivePowerWatt() {
		return actualDut_B_PhaseActivePowerWatt;
	}

	public String getActualDut_B_PhaseReactivePowerVAR() {
		return actualDut_B_PhaseReactivePowerVAR;
	}

	public String getActualDut_B_PhaseApparentPowerVA() {
		return actualDut_B_PhaseApparentPowerVA;
	}

	public void setActualDut_R_PhaseActivePowerWatt(String actualDut_R_PhaseActivePowerWatt) {
		this.actualDut_R_PhaseActivePowerWatt = actualDut_R_PhaseActivePowerWatt;
	}

	public void setActualDut_R_PhaseReactivePowerVAR(String actualDut_R_PhaseReactivePowerVAR) {
		this.actualDut_R_PhaseReactivePowerVAR = actualDut_R_PhaseReactivePowerVAR;
	}

	public void setActualDut_R_PhaseApparentPowerVA(String actualDut_R_PhaseApparentPowerVA) {
		this.actualDut_R_PhaseApparentPowerVA = actualDut_R_PhaseApparentPowerVA;
	}

	public void setActualDut_Y_PhaseActivePowerWatt(String actualDut_Y_PhaseActivePowerWatt) {
		this.actualDut_Y_PhaseActivePowerWatt = actualDut_Y_PhaseActivePowerWatt;
	}

	public void setActualDut_Y_PhaseReactivePowerVAR(String actualDut_Y_PhaseReactivePowerVAR) {
		this.actualDut_Y_PhaseReactivePowerVAR = actualDut_Y_PhaseReactivePowerVAR;
	}

	public void setActualDut_Y_PhaseApparentPowerVA(String actualDut_Y_PhaseApparentPowerVA) {
		this.actualDut_Y_PhaseApparentPowerVA = actualDut_Y_PhaseApparentPowerVA;
	}

	public void setActualDut_B_PhaseActivePowerWatt(String actualDut_B_PhaseActivePowerWatt) {
		this.actualDut_B_PhaseActivePowerWatt = actualDut_B_PhaseActivePowerWatt;
	}

	public void setActualDut_B_PhaseReactivePowerVAR(String actualDut_B_PhaseReactivePowerVAR) {
		this.actualDut_B_PhaseReactivePowerVAR = actualDut_B_PhaseReactivePowerVAR;
	}

	public void setActualDut_B_PhaseApparentPowerVA(String actualDut_B_PhaseApparentPowerVA) {
		this.actualDut_B_PhaseApparentPowerVA = actualDut_B_PhaseApparentPowerVA;
	}

	public String getReportSerialNo() {
		return reportSerialNo;
	}

	public void setReportSerialNo(String reportSerialNo) {
		this.reportSerialNo = reportSerialNo;
	}

	public String getAverageType() {
		return averageType;
	}

	public void setAverageType(String averageType) {
		this.averageType = averageType;
	}

	public String getAverageCount() {
		return averageCount;
	}

	public void setAverageCount(String averageCount) {
		this.averageCount = averageCount;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getValidationType2() {
		return validationType2;
	}

	public void setValidationType2(String validationType2) {
		this.validationType2 = validationType2;
	}

	public String getErrorValue2() {
		return errorValue2;
	}

	public void setErrorValue2(String errorValue2) {
		this.errorValue2 = errorValue2;
	}

	public String getAcceptableErrorMin2() {
		return acceptableErrorMin2;
	}

	public void setAcceptableErrorMin2(String acceptableErrorMin2) {
		this.acceptableErrorMin2 = acceptableErrorMin2;
	}

	public String getAcceptableErrorMax2() {
		return acceptableErrorMax2;
	}

	public void setAcceptableErrorMax2(String acceptableErrorMax2) {
		this.acceptableErrorMax2 = acceptableErrorMax2;
	}

	public String getAcceptableErrorMin() {
		return acceptableErrorMin;
	}

	public void setAcceptableErrorMin(String acceptableErrorMin) {
		this.acceptableErrorMin = acceptableErrorMin;
	}

	public String getAcceptableErrorMax() {
		return acceptableErrorMax;
	}

	public void setAcceptableErrorMax(String acceptableErrorMax) {
		this.acceptableErrorMax = acceptableErrorMax;
	}

	public String getOverAllResultStatus() {
		return overAllResultStatus;
	}

	public void setOverAllResultStatus(String overAllResultStatus) {
		this.overAllResultStatus = overAllResultStatus;
	}

/*	public String getReadingId() {
		return readingId;
	}

	public void setReadingId(String readingId) {
		this.readingId = readingId;
	}*/
	
    
/*	public ResultDataModel(String testCaseName, String aliasID, String deviceName, String testResult, String errorValue) {
		
		this.testCaseName = testCaseName;
		this.aliasID = aliasID;
		this.deviceName = deviceName;
		this.testResult = testResult;
		this.errorValue = errorValue;
	}*/
	
/*	public String getTestCaseName() {
		return this.testCaseName;
	}
	public String getAliasID() {
		return this.aliasID;
	}
	public String getDeviceName() {
		return this.deviceName;
	}
	public String getTestResult() {
		return this.testResult;
	}
	public String getErrorValue() {
		return this.errorValue;
	}

	public String getPhaseLabel() {
		return phaseLabel;
	}

	public String getReadingNumber() {
		return readingNumber;
	}

	public String getDeployedSerialNo() {
		return deployedSerialNo;
	}

	public String getTestType() {
		return testType;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public String getEnergyMetricUnit() {
		return energyMetricUnit;
	}

	public String getLoraId() {
		return loraId;
	}

	public String getAcceptableErrorMinLimit() {
		return acceptableErrorMinLimit;
	}

	public String getAcceptableErrorMaxLimit() {
		return acceptableErrorMaxLimit;
	}

	public String getTargetR_PhaseVoltage() {
		return targetR_PhaseVoltage;
	}

	public String getTargetR_PhaseCuurent() {
		return targetR_PhaseCuurent;
	}

	public String getTargetR_PhasePhaseAngle() {
		return targetR_PhasePhaseAngle;
	}

	public String getTargetR_PhasePf() {
		return targetR_PhasePf;
	}

	public String getTargetY_PhaseVoltage() {
		return targetY_PhaseVoltage;
	}

	public String getTargetY_PhaseCuurent() {
		return targetY_PhaseCuurent;
	}

	public String getTargetY_PhasePhaseAngle() {
		return targetY_PhasePhaseAngle;
	}

	public String getTargetY_PhasePf() {
		return targetY_PhasePf;
	}

	public String getTargetB_PhaseVoltage() {
		return targetB_PhaseVoltage;
	}

	public String getTargetB_PhaseCuurent() {
		return targetB_PhaseCuurent;
	}

	public String getTargetB_PhasePhaseAngle() {
		return targetB_PhasePhaseAngle;
	}

	public String getTargetB_PhasePf() {
		return targetB_PhasePf;
	}

	public String getTargetFreq() {
		return targetFreq;
	}

	public String getRefStdTimeStampEpoch() {
		return refStdTimeStampEpoch;
	}

	public String getRefStdDateHuman() {
		return refStdDateHuman;
	}

	public String getRefStdTimeHuman() {
		return refStdTimeHuman;
	}

	public String getActualRefStd_R_PhaseVoltage() {
		return actualRefStd_R_PhaseVoltage;
	}

	public String getActualRefStd_R_PhaseCurrent() {
		return actualRefStd_R_PhaseCurrent;
	}

	public String getActualRefStd_R_PhasePower() {
		return actualRefStd_R_PhasePower;
	}

	public String getActualRefStd_R_PhasePf() {
		return actualRefStd_R_PhasePf;
	}

	public String getActualRefStd_R_PhaseDegreeAngle() {
		return actualRefStd_R_PhaseDegreeAngle;
	}

	public String getActualRefStd_Y_PhaseVoltage() {
		return actualRefStd_Y_PhaseVoltage;
	}

	public String getActualRefStd_Y_PhaseCurrent() {
		return actualRefStd_Y_PhaseCurrent;
	}

	public String getActualRefStd_Y_PhasePower() {
		return actualRefStd_Y_PhasePower;
	}

	public String getActualRefStd_Y_PhasePf() {
		return actualRefStd_Y_PhasePf;
	}

	public String getActualRefStd_Y_PhaseDegreeAngle() {
		return actualRefStd_Y_PhaseDegreeAngle;
	}

	public String getActualRefStd_B_PhaseVoltage() {
		return actualRefStd_B_PhaseVoltage;
	}

	public String getActualRefStd_B_PhaseCurrent() {
		return actualRefStd_B_PhaseCurrent;
	}

	public String getActualRefStd_B_PhasePower() {
		return actualRefStd_B_PhasePower;
	}

	public String getActualRefStd_B_PhasePf() {
		return actualRefStd_B_PhasePf;
	}

	public String getActualRefStd_B_PhaseDegreeAngle() {
		return actualRefStd_B_PhaseDegreeAngle;
	}

	public String getActualRefStdFreq() {
		return actualRefStdFreq;
	}

	public String getDutSerialNo() {
		return dutSerialNo;
	}

	public String getDutTimeStampEpoch() {
		return dutTimeStampEpoch;
	}

	public String getDutStdDateHuman() {
		return dutStdDateHuman;
	}

	public String getDutStdTimeHuman() {
		return dutStdTimeHuman;
	}

	public String getActualDut_R_PhaseVoltage() {
		return actualDut_R_PhaseVoltage;
	}

	public String getActualDut_R_PhaseCurrent() {
		return actualDut_R_PhaseCurrent;
	}

	public String getActualDut_R_PhasePower() {
		return actualDut_R_PhasePower;
	}

	public String getActualDut_R_PhasePf() {
		return actualDut_R_PhasePf;
	}

	public String getActualDut_R_PhaseDegreeAngle() {
		return actualDut_R_PhaseDegreeAngle;
	}

	public String getActualDut_Y_PhaseVoltage() {
		return actualDut_Y_PhaseVoltage;
	}

	public String getActualDut_Y_PhaseCurrent() {
		return actualDut_Y_PhaseCurrent;
	}

	public String getActualDut_Y_PhasePower() {
		return actualDut_Y_PhasePower;
	}

	public String getActualDut_Y_PhasePf() {
		return actualDut_Y_PhasePf;
	}

	public String getActualDut_Y_PhaseDegreeAngle() {
		return actualDut_Y_PhaseDegreeAngle;
	}

	public String getActualDut_B_PhaseVoltage() {
		return actualDut_B_PhaseVoltage;
	}

	public String getActualDut_B_PhaseCurrent() {
		return actualDut_B_PhaseCurrent;
	}

	public String getActualDut_B_PhasePower() {
		return actualDut_B_PhasePower;
	}

	public String getActualDut_B_PhasePf() {
		return actualDut_B_PhasePf;
	}

	public String getActualDut_B_PhaseDegreeAngle() {
		return actualDut_B_PhaseDegreeAngle;
	}

	public String getActualDutFreq() {
		return actualDutFreq;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public void setAliasID(String aliasID) {
		this.aliasID = aliasID;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}*/
}