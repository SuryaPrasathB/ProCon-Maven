package com.tasnetwork.calibration.energymeter.testreport.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoltageVariationReportCellPosition {

	@SerializedName("reportSerialNoCell")
	@Expose
	private String reportSerialNoCell;
	@SerializedName("testerNameCell")
	@Expose
	private String testerNameCell;
	@SerializedName("pageNumberCell")
	@Expose
	private String pageNumberCell;
	@SerializedName("voltageCell")
	@Expose
	private String voltageCell;

	public String getReportSerialNoCell() {
		return reportSerialNoCell;
	}

	public void setReportSerialNoCell(String reportSerialNoCell) {
		this.reportSerialNoCell = reportSerialNoCell;
	}

	public String getTesterNameCell() {
		return testerNameCell;
	}

	public void setTesterNameCell(String testerNameCell) {
		this.testerNameCell = testerNameCell;
	}

	public String getPageNumberCell() {
		return pageNumberCell;
	}

	public void setPageNumberCell(String pageNumberCell) {
		this.pageNumberCell = pageNumberCell;
	}

	public String getVoltageCell() {
		return voltageCell;
	}

	public void setVoltageCell(String voltageCell) {
		this.voltageCell = voltageCell;
	}

}
