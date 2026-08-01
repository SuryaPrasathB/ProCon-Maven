package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class OperationProcessDataHashMap {

	
	private HashMap<String,OperationProcessDataJsonRead> operationProcessData = new LinkedHashMap<String,OperationProcessDataJsonRead >();

	public HashMap<String, OperationProcessDataJsonRead> getOperationProcessData() {
		return operationProcessData;
	}

	public void setOperationProcessData(HashMap<String, OperationProcessDataJsonRead> operationProcessData) {
		this.operationProcessData = operationProcessData;
	}
	
	public void addOperationProcessData(String operationDataKey, OperationProcessDataJsonRead operationProcessData) {
		this.operationProcessData.put(operationDataKey, operationProcessData);
	}
}
