package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;
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
	
	
/*	private ArrayList<LinkedHashMap<String,OperationProcessDataSetModel>> operationProcessInputDataList = new ArrayList< LinkedHashMap<String,OperationProcessDataSetModel >>();
	private ArrayList<LinkedHashMap<String,OperationProcessDataSetModel >> operationProcessLocalOutputData = new ArrayList<LinkedHashMap<String,OperationProcessDataSetModel >>();
	private ArrayList<LinkedHashMap<String,OperationProcessDataSetModel >> operationProcessMasterOutputData = new ArrayList<LinkedHashMap<String,OperationProcessDataSetModel >>();
	
	
	public ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> getOperationProcessInputDataList() {
		return operationProcessInputDataList;
	}
	public void setOperationProcessInputDataList(
			ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> operationProcessInputDataList) {
		this.operationProcessInputDataList = operationProcessInputDataList;
	}
	public ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> getOperationProcessLocalOutputData() {
		return operationProcessLocalOutputData;
	}
	public void setOperationProcessLocalOutputData(
			ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> operationProcessLocalOutputData) {
		this.operationProcessLocalOutputData = operationProcessLocalOutputData;
	}
	public ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> getOperationProcessMasterOutputData() {
		return operationProcessMasterOutputData;
	}
	public void setOperationProcessMasterOutputData(
			ArrayList<LinkedHashMap<String, OperationProcessDataSetModel>> operationProcessMasterOutputData) {
		this.operationProcessMasterOutputData = operationProcessMasterOutputData;
	}*/
	
	
/*	public HashMap<String, OperationProcessDataSetModel> getOperationProcessInputData() {
		return operationProcessInputData;
	}
	public void setOperationProcessInputData(HashMap<String, OperationProcessDataSetModel> operationProcessInputData) {
		this.operationProcessInputData = operationProcessInputData;
	}
	public HashMap<String, OperationProcessDataSetModel> getOperationProcessLocalOutputData() {
		return operationProcessLocalOutputData;
	}
	public void setOperationProcessLocalOutputData(
			HashMap<String, OperationProcessDataSetModel> operationProcessLocalOutputData) {
		this.operationProcessLocalOutputData = operationProcessLocalOutputData;
	}
	public HashMap<String, OperationProcessDataSetModel> getOperationProcessMasterOutputData() {
		return operationProcessMasterOutputData;
	}
	public void setOperationProcessMasterOutputData(
			HashMap<String, OperationProcessDataSetModel> operationProcessMasterOutputData) {
		this.operationProcessMasterOutputData = operationProcessMasterOutputData;
	}*/

	
/*	OperationProcessDataHashMapModel(String processDataKey){
		
	}*/
	
}
