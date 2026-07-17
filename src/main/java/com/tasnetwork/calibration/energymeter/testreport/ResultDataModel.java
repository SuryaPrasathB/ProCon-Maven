package com.tasnetwork.calibration.energymeter.testreport;

import com.tasnetwork.calibration.energymeter.testprofiles.TestDataTypeEnum;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultDataModel {
	private final String testCaseName;
	private final String aliasID;
	private final String deviceName;
	private final String testResult;
	private final String errorValue;
    
	public ResultDataModel(String testCaseName, String aliasID, String deviceName, String testResult, String errorValue) {
		
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