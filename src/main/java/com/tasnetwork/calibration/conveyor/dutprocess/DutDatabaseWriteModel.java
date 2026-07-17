package com.tasnetwork.calibration.conveyor.dutprocess;

import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;

public class DutDatabaseWriteModel {
	
	String currentProjectName = "";
	String currentTestPointName = "";
	String currentTestAliasID = "";
	int errorCount  = 0;
	ResultDataModel resultData = new ResultDataModel();
	String failureReason = "";
	String dataType = "";
	String executionMctNctMode = "";
	String energyFlowMode = "";
	String selectedDeployment_ID = "" ;
	int sequenceNumber = 0;
	String validationType = "";
	String validationType2 = "";
	String validationType2ResultStatus = "";
	String validationType2ResultData = "";
	String overAllResultStatus = "";
	String errorMin2 = "";
	String errorMax2 = "";
	String averageType = "";
	String averageCount = "";
	
	String rackId = "";
	String resultStatus = "";
	String errorValue = "";
	
	boolean resultForTestTypeWithCurrentParameter = false;
	
	public DutDatabaseWriteModel(String currentProjectName, String currentTestPointName,String currentTestAliasID,
			int errorCount,	ResultDataModel resultData,	String failureReason,	String dataType,String executionMctNctMode,
			String energyFlowMode,	String selectedDeployment_ID,int sequenceNumber, String validationType,
			String validationType2,	String validationType2ResultStatus,	String validationType2ResultData,
			String overAllResultStatus,	String errorMin2,String errorMax2,	String averageType,	String averageCount,
			boolean resultForTestTypeWithCurrentParameter  ){
		
		this.currentProjectName = currentProjectName;
		this.currentTestPointName = currentTestPointName;
		this.currentTestAliasID = currentTestAliasID;
		this.errorCount  = errorCount;
		this.resultData = resultData;
		this.failureReason = failureReason;
		this.dataType = dataType;
		this.executionMctNctMode = executionMctNctMode;
		this.energyFlowMode = energyFlowMode;
		this.selectedDeployment_ID = selectedDeployment_ID ;
		this.sequenceNumber = sequenceNumber;
		this.validationType = validationType;
		this.validationType2 = validationType2;
		this.validationType2ResultStatus = validationType2ResultStatus;
		this.validationType2ResultData = validationType2ResultData;
		this.overAllResultStatus = overAllResultStatus;
		this.errorMin2 = errorMin2;
		this.errorMax2 = errorMax2;
		this.averageType = averageType;
		this.averageCount = averageCount;
		this.resultForTestTypeWithCurrentParameter = resultForTestTypeWithCurrentParameter;
		
	}
	
	public DutDatabaseWriteModel(String currentProjectName, String currentTestPointName,String currentTestAliasID,
			String rackId, String resultStatus, int errorCount,	String errorValue, String failureReason,	
			String dataType,String executionMctNctMode,	String energyFlowMode,	String selectedDeployment_ID,
			int sequenceNumber, String averageType,	String averageCount,boolean resultForTestTypeWithCurrentParameter	  ){
		

		
		this.currentProjectName = currentProjectName;
		this.currentTestPointName = currentTestPointName;
		this.currentTestAliasID = currentTestAliasID;
		this.rackId = rackId;
		this.resultStatus = resultStatus;
		this.errorCount  = errorCount;
		this.errorValue = errorValue;
		this.failureReason = failureReason;
		this.dataType = dataType;
		this.executionMctNctMode = executionMctNctMode;
		this.energyFlowMode = energyFlowMode;
		this.selectedDeployment_ID = selectedDeployment_ID ;
		this.sequenceNumber = sequenceNumber;
		this.averageType = averageType;
		this.averageCount = averageCount;
		this.resultForTestTypeWithCurrentParameter = resultForTestTypeWithCurrentParameter;
		
	}
	


	public String getCurrentProjectName() {
		return currentProjectName;
	}
	public void setCurrentProjectName(String currentProjectName) {
		this.currentProjectName = currentProjectName;
	}
	public String getCurrentTestPointName() {
		return currentTestPointName;
	}
	public void setCurrentTestPointName(String currentTestPointName) {
		this.currentTestPointName = currentTestPointName;
	}
	public String getCurrentTestAliasID() {
		return currentTestAliasID;
	}
	public void setCurrentTestAliasID(String currentTestAliasID) {
		this.currentTestAliasID = currentTestAliasID;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public ResultDataModel getResultData() {
		return resultData;
	}
	public void setResultData(ResultDataModel resultData) {
		this.resultData = resultData;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getExecutionMctNctMode() {
		return executionMctNctMode;
	}
	public void setExecutionMctNctMode(String executionMctNctMode) {
		this.executionMctNctMode = executionMctNctMode;
	}
	public String getEnergyFlowMode() {
		return energyFlowMode;
	}
	public void setEnergyFlowMode(String energyFlowMode) {
		this.energyFlowMode = energyFlowMode;
	}
	public String getSelectedDeployment_ID() {
		return selectedDeployment_ID;
	}
	public void setSelectedDeployment_ID(String selectedDeployment_ID) {
		this.selectedDeployment_ID = selectedDeployment_ID;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
	public String getValidationType2ResultStatus() {
		return validationType2ResultStatus;
	}
	public void setValidationType2ResultStatus(String validationType2ResultStatus) {
		this.validationType2ResultStatus = validationType2ResultStatus;
	}
	public String getValidationType2ResultData() {
		return validationType2ResultData;
	}
	public void setValidationType2ResultData(String validationType2ResultData) {
		this.validationType2ResultData = validationType2ResultData;
	}
	public String getOverAllResultStatus() {
		return overAllResultStatus;
	}
	public void setOverAllResultStatus(String overAllResultStatus) {
		this.overAllResultStatus = overAllResultStatus;
	}
	public String getErrorMin2() {
		return errorMin2;
	}
	public void setErrorMin2(String errorMin2) {
		this.errorMin2 = errorMin2;
	}
	public String getErrorMax2() {
		return errorMax2;
	}
	public void setErrorMax2(String errorMax2) {
		this.errorMax2 = errorMax2;
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
	public boolean isResultForTestTypeWithCurrentParameter() {
		return resultForTestTypeWithCurrentParameter;
	}
	public void setResultForTestTypeWithCurrentParameter(boolean resultForTestTypeWithCurrentParameter) {
		this.resultForTestTypeWithCurrentParameter = resultForTestTypeWithCurrentParameter;
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

}
