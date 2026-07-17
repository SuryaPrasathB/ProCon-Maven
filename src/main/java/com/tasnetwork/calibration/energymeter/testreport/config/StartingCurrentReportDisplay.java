package com.tasnetwork.calibration.energymeter.testreport.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartingCurrentReportDisplay {

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
	@SerializedName("displayPowerFactor")
	@Expose
	private Boolean displayPowerFactor;

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

	public Boolean getDisplayPowerFactor() {
		return displayPowerFactor;
	}

	public void setDisplayPowerFactor(Boolean displayPowerFactor) {
		this.displayPowerFactor = displayPowerFactor;
	}

}
