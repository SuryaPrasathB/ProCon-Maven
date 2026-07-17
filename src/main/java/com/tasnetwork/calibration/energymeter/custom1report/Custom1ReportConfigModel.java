package com.tasnetwork.calibration.energymeter.custom1report;




import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javafx.beans.property.SimpleBooleanProperty;

@Generated("jsonschema2pojo")
public class Custom1ReportConfigModel {

	@SerializedName("ConfigFileVersion")
	@Expose
	private String configFileVersion;
	@SerializedName("ConfigFileVersionComment")
	@Expose
	private String configFileVersionComment;
	@SerializedName("TemplateFileLocationPath")
	@Expose
	private String templateFileLocationPath;
	@SerializedName("ReportOutputPath")
	@Expose
	private String reportOutputPath;
	@SerializedName("ParentReportProfile")
	@Expose
	private String parentReportProfile;
	@SerializedName("ExcelReportTestTypeFilter")
	@Expose
	private ExcelReportTestTypeFilter excelReportTestTypeFilter;
	@SerializedName("ExcelReportMeterDataCellPosition")
	@Expose
	private ExcelReportMeterDataCellPosition excelReportMeterDataCellPosition;
	@SerializedName("ExcelReportMeterDataDisplay")
	@Expose
	private ExcelReportMeterDataDisplay excelReportMeterDataDisplay;
	
	
	@SerializedName("MaxNoOfPageInReport")
	@Expose
	private int maxNoOfPageInReport;
	
	@SerializedName("MaxDutDisplayPerPage")
	@Expose
	private int maxDutDisplayPerPage;
	
	@SerializedName("SplitDutDisplayIntoMultiplePage")
	@Expose
	private Boolean splitDutDisplayIntoMultiplePage;
	//private SimpleBooleanProperty splitDutDisplayIntoMultiplePage;

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

	public String getTemplateFileLocationPath() {
		return templateFileLocationPath;
	}

	public void setTemplateFileLocationPath(String templateFileLocationPath) {
		this.templateFileLocationPath = templateFileLocationPath;
	}

	public String getReportOutputPath() {
		return reportOutputPath;
	}

	public void setReportOutputPath(String reportOutputPath) {
		this.reportOutputPath = reportOutputPath;
	}

	public String getParentReportProfile() {
		return parentReportProfile;
	}

	public void setParentReportProfile(String parentReportProfile) {
		this.parentReportProfile = parentReportProfile;
	}

	public ExcelReportTestTypeFilter getExcelReportTestTypeFilter() {
		return excelReportTestTypeFilter;
	}

	public void setExcelReportTestTypeFilter(ExcelReportTestTypeFilter excelReportTestTypeFilter) {
		this.excelReportTestTypeFilter = excelReportTestTypeFilter;
	}

	public ExcelReportMeterDataCellPosition getExcelReportMeterDataCellPosition() {
		return excelReportMeterDataCellPosition;
	}

	public void setExcelReportMeterDataCellPosition(ExcelReportMeterDataCellPosition excelReportMeterDataCellPosition) {
		this.excelReportMeterDataCellPosition = excelReportMeterDataCellPosition;
	}

	public ExcelReportMeterDataDisplay getExcelReportMeterDataDisplay() {
		return excelReportMeterDataDisplay;
	}

	public void setExcelReportMeterDataDisplay(ExcelReportMeterDataDisplay excelReportMeterDataDisplay) {
		this.excelReportMeterDataDisplay = excelReportMeterDataDisplay;
	}

	public int getMaxNoOfPageInReport() {
		return maxNoOfPageInReport;
	}

	public void setMaxNoOfPageInReport(int maxNoOfPageInReport) {
		this.maxNoOfPageInReport = maxNoOfPageInReport;
	}

	public int getMaxDutDisplayPerPage() {
		return maxDutDisplayPerPage;
	}

	public void setMaxDutDisplayPerPage(int maxDutDisplayPerPage) {
		this.maxDutDisplayPerPage = maxDutDisplayPerPage;
	}

	public Boolean getSplitDutDisplayIntoMultiplePage() {
		return splitDutDisplayIntoMultiplePage;
	}

	public void setSplitDutDisplayIntoMultiplePage(Boolean splitDutDisplayIntoMultiplePage) {
		this.splitDutDisplayIntoMultiplePage = splitDutDisplayIntoMultiplePage;
	}

}
