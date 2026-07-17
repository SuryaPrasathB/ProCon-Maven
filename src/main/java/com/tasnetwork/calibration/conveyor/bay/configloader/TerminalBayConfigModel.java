package com.tasnetwork.calibration.conveyor.bay.configloader;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TerminalBayConfigModel {

	@SerializedName("ConfigFileVersion")
	@Expose
	private String configFileVersion;
	@SerializedName("ConfigFileVersionComment")
	@Expose
	private String configFileVersionComment;
	@SerializedName("Terminal")
	@Expose
	private List<Terminal> terminal;
	
	
	@SerializedName("CustomerName")
	@Expose
	private String customerName;
	
	
	@SerializedName("LocationName")
	@Expose
	private String locationName;
	
	
	@SerializedName("PlantName")
	@Expose
	private String plantName;
	
	
	@SerializedName("DepartmentName")
	@Expose
	private String departmentName;
	
	@SerializedName("LineNo")
	@Expose
	private String lineNo;
	
	
	public String getConfigFileVersion() {
		return configFileVersion;
	}
	public String getConfigFileVersionComment() {
		return configFileVersionComment;
	}
	public List<Terminal> getTerminal() {
		return terminal;
	}
	public void setConfigFileVersion(String configFileVersion) {
		this.configFileVersion = configFileVersion;
	}
	public void setConfigFileVersionComment(String configFileVersionComment) {
		this.configFileVersionComment = configFileVersionComment;
	}
	public void setTerminal(List<Terminal> terminal) {
		this.terminal = terminal;
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getLocationName() {
		return locationName;
	}
	public String getPlantName() {
		return plantName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public String getLineNo() {
		return lineNo;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

}