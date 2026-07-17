package com.tasnetwork.calibration.energymeter.testreport.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepeatabilityReportDisplay {

	@SerializedName("displayReportSerialNo")
	@Expose
	private Boolean displayReportSerialNo;
	@SerializedName("displayTesterName")
	@Expose
	private Boolean displayTesterName;
	@SerializedName("displayPageNumber")
	@Expose
	private Boolean displayPageNumber;
	@SerializedName("displayVoltage")
	@Expose
	private Boolean displayVoltage;
	@SerializedName("displayCurrent")
	@Expose
	private Boolean displayCurrent;
	@SerializedName("displayTestDuration")
	@Expose
	private Boolean displayTestDuration;
	@SerializedName("displayTestInterval")
	@Expose
	private Boolean displayTestInterval;

	public Boolean getDisplayReportSerialNo() {
		return displayReportSerialNo;
	}

	public void setDisplayReportSerialNo(Boolean displayReportSerialNo) {
		this.displayReportSerialNo = displayReportSerialNo;
	}

	public Boolean getDisplayTesterName() {
		return displayTesterName;
	}

	public void setDisplayTesterName(Boolean displayTesterName) {
		this.displayTesterName = displayTesterName;
	}

	public Boolean getDisplayPageNumber() {
		return displayPageNumber;
	}

	public void setDisplayPageNumber(Boolean displayPageNumber) {
		this.displayPageNumber = displayPageNumber;
	}

	public Boolean getDisplayVoltage() {
		return displayVoltage;
	}

	public void setDisplayVoltage(Boolean displayVoltage) {
		this.displayVoltage = displayVoltage;
	}

	public Boolean getDisplayCurrent() {
		return displayCurrent;
	}

	public void setDisplayCurrent(Boolean displayCurrent) {
		this.displayCurrent = displayCurrent;
	}
	public Boolean getDisplayTestDuration() {
		return displayTestDuration;
	}

	public void setDisplayTestDuration(Boolean displayTestDuration) {
		this.displayTestDuration = displayTestDuration;
	}

	public Boolean getDisplayTestInterval() {
		return displayTestInterval;
	}

	public void setDisplayTestInterval(Boolean displayTestInterval) {
		this.displayTestInterval = displayTestInterval;
	}

}
