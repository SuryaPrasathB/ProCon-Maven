package com.tasnetwork.calibration.energymeter.testreport;

public class ReportDataModel {
 
	private final String testCaseName;
	private final String aliasID;
	private final String deviceName;
	private final String testResult;
	private final String errorValue;
    
	public ReportDataModel(String testCaseName, String aliasID, String deviceName, String testResult, String errorValue) {
		
		this.testCaseName = testCaseName;
		this.aliasID = aliasID;
		this.deviceName = deviceName;
		this.testResult = testResult;
		this.errorValue = errorValue;
	}
	
	public String gettestCaseName() {
		return this.testCaseName;
	}
	public String getaliasID() {
		return this.aliasID;
	}
	public String getdeviceName() {
		return this.deviceName;
	}
	public String gettestResult() {
		return this.testResult;
	}
	public String geterrorValue() {
		return this.errorValue;
	}
	
	
	
	
	
}
