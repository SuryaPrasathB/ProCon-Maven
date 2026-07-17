package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.energymeter.custom1report.TestTypeFilter;

public class OperationProcessJsonReadModel {
	
	
	@SerializedName("ConfigFileVersion")
	@Expose
	private String configFileVersion;
	@SerializedName("ConfigFileVersionComment")
	@Expose
	private String configFileVersionComment;
	
	@SerializedName("MasterOutputDataSet")
	@Expose
	private List<OperationProcessDataJsonRead> operationMasterOutput = null;
	@SerializedName("LocalOutputDataSet")
	@Expose
	private List<OperationProcessDataJsonRead> operationLocalOutput = null;
	@SerializedName("LocalInputDataSet")
	@Expose
	private List<OperationProcessDataJsonRead> operationLocalInput = null;
	
	
	public String getConfigFileVersion() {
		return configFileVersion;
	}

	public void setConfigFileVersion(String configFileVersion) {
		this.configFileVersion = configFileVersion;
	}

	public String getConfigFileVersionComment() {
		return configFileVersionComment;
	}

	public void setConfigFileVersionComment(String configFileVersionComment) {
		this.configFileVersionComment = configFileVersionComment;
	}
	
	public List<OperationProcessDataJsonRead> getOperationMasterOutput() {
		return operationMasterOutput;
	}
	public void setOperationMasterOutput(List<OperationProcessDataJsonRead> operationMasterOutput) {
		this.operationMasterOutput = operationMasterOutput;
	}
	public List<OperationProcessDataJsonRead> getOperationLocalOutput() {
		return operationLocalOutput;
	}
	public void setOperationLocalOutput(List<OperationProcessDataJsonRead> operationLocalOutput) {
		this.operationLocalOutput = operationLocalOutput;
	}
	public List<OperationProcessDataJsonRead> getOperationLocalInput() {
		return operationLocalInput;
	}
	public void setOperationLocalInput(List<OperationProcessDataJsonRead> operationLocalInput) {
		this.operationLocalInput = operationLocalInput;
	}
	

	
	
}
