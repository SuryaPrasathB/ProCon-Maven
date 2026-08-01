package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
