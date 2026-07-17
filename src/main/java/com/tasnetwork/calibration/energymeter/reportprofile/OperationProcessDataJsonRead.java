package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class OperationProcessDataJsonRead {

	
	@SerializedName("OperationProcessKey")
	@Expose
	private String operationProcessKey;
	//@SerializedName("ResultValue")
	//@Expose
	//private List<String> resultValue = new ArrayList<String>();
	//private List<String> dutUniqueId = new ArrayList<String>();
	
	private HashMap<String,String> resultValueHashMap = new LinkedHashMap<String,String>();
	private HashMap<String,String> resultStatusHashMap = new LinkedHashMap<String,String>();
	
	@SerializedName("ComparedStatus")
	@Expose
	private String comparedStatus;
	@SerializedName("UpperLimit")
	@Expose
	private String upperLimit;
	@SerializedName("LowerLimit")
	@Expose
	private String lowerLimit;
	@SerializedName("PopulateOnlyHeaders")
	@Expose
	private Boolean populateOnlyHeaders;
	
	@SerializedName("DataType")
	@Expose
	private Boolean dataType;
	
	//private Boolean resultTypeAverage = false;
	
	public String getOperationProcessKey() {
		return operationProcessKey;
	}
	public void setOperationProcessKey(String operationProcessKey) {
		this.operationProcessKey = operationProcessKey;
	}
/*	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}*/
	public String getComparedStatus() {
		return comparedStatus;
	}
	public void setComparedStatus(String comparedStatus) {
		this.comparedStatus = comparedStatus;
	}
	public String getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public Boolean isPopulateOnlyHeaders() {
		return populateOnlyHeaders;
	}
	public void setPopulateOnlyHeaders(Boolean populateOnlyHeaders) {
		this.populateOnlyHeaders = populateOnlyHeaders;
	}
	public Boolean getDataType() {
		return dataType;
	}
	public void setDataType(Boolean dataType) {
		this.dataType = dataType;
	}
	public HashMap<String, String> getResultValueHashMap() {
		return resultValueHashMap;
	}
	public void setResultValueHashMap(HashMap<String, String> resultValueHashMap) {
		this.resultValueHashMap = resultValueHashMap;
	}
	public HashMap<String, String> getResultStatusHashMap() {
		return resultStatusHashMap;
	}
	public void setResultStatusHashMap(HashMap<String, String> resultStatusHashMap) {
		this.resultStatusHashMap = resultStatusHashMap;
	}
	/*public Boolean isResultTypeAverage() {
		return resultTypeAverage;
	}
	public void setResultTypeAverage(Boolean resultTypeAverage) {
		this.resultTypeAverage = resultTypeAverage;
	}*/
	
	//private HashMap<>
	//private SimpleStringProperty Key;
/*	@SerializedName("OperationProcessKey")
	@Expose
	private SimpleStringProperty OperationProcessKey;
	//private SimpleStringProperty OperationProcessKey;
	@SerializedName("ResultValue")
	@Expose
	private SimpleStringProperty ResultValue;
	@SerializedName("ComparedStatus")
	@Expose
	private SimpleStringProperty ComparedStatus;
	@SerializedName("UpperLimit")
	@Expose
	private SimpleStringProperty UpperLimit;
	@SerializedName("LowerLimit")
	@Expose
	private SimpleStringProperty LowerLimit;
	@SerializedName("PopulateOnlyHeaders")
	@Expose
	private BooleanProperty PopulateOnlyHeaders;*/
	
	/*OperationProcessData(){
		this.OperationProcessKey = new SimpleStringProperty("");
		this.ResultValue = new SimpleStringProperty("");
		this.ComparedStatus = new SimpleStringProperty("");
		this.UpperLimit = new SimpleStringProperty("");
		this.LowerLimit = new SimpleStringProperty("");
		this.PopulateOnlyHeaders  = new SimpleBooleanProperty(false);
	}
	
	
	public SimpleStringProperty getOperationProcessKeyProperty() {
		return OperationProcessKey;
	}
	public String getOperationProcessKey() {
		return OperationProcessKey.get();
	}
	public void setOperationProcessKey(String operationProcessKey) {
		this.OperationProcessKey.set(operationProcessKey);
	}
	
	
	public SimpleStringProperty getResultValueProperty() {
		return ResultValue;
	}
	public String getResultValue() {
		return ResultValue.get();
	}
	public void setResultValue(String resultValue) {
		this.ResultValue.set(resultValue);
	}
	public SimpleStringProperty getComparedStatusProperty() {
		return ComparedStatus;
	}
	public String getComparedStatus() {
		return ComparedStatus.get();
	}
	public void setComparedStatus(String comparedStatus) {
		this.ComparedStatus.set(comparedStatus);
	}
	public SimpleStringProperty getUpperLimitProperty() {
		return UpperLimit;
	}
	public String getUpperLimit() {
		return UpperLimit.get();
	}
	public void setUpperLimit(String UpperLimit) {
		this.UpperLimit.set(UpperLimit);
	}
	public SimpleStringProperty getLowerLimitProperty() {
		return LowerLimit;
	}
	public String getLowerLimit() {
		return LowerLimit.get();
	}
	public void setLowerLimit(String lowerLimit) {
		this.LowerLimit.set(lowerLimit);
	}
	
	public BooleanProperty getPopulateOnlyHeadersProperty() {
		return PopulateOnlyHeaders;
	}
	
	public boolean getPopulateOnlyHeaders() {
		return PopulateOnlyHeaders.get();
	}

	public void setPopulateOnlyHeaders(boolean populateOnlyHeaders) {
		this.PopulateOnlyHeaders.set(populateOnlyHeaders);
	}*/
	
}
