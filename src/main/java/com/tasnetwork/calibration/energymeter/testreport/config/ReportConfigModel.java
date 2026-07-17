package com.tasnetwork.calibration.energymeter.testreport.config;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportConfigModel {
    @SerializedName("ConfigFileVersion")
    @Expose
    private String configFileVersion;
    @SerializedName("MeterProfileReportCellPosition")
    @Expose
    private MeterProfileReportCellPosition meterProfileReport;
    @SerializedName("MeterProfileReportDisplay")
    @Expose
    private MeterProfileReportDisplay meterProfileReportDisplay;
    @SerializedName("AccuracyReportCellPosition")
    @Expose
    private AccuracyReportCellPosition accuracyReportCellPosition;
    @SerializedName("AccuracyReportDisplay")
    @Expose
    private AccuracyReportDisplay accuracyReportDisplay;
    @SerializedName("NoLoadReportCellPosition")
    @Expose
    private NoLoadReportCellPosition noLoadReportCellPosition;
    @SerializedName("NoLoadReportDisplay")
    @Expose
    private NoLoadReportDisplay noLoadReportDisplay;
    @SerializedName("StartingCurrentReportCellPosition")
    @Expose
    private StartingCurrentReportCellPosition startingCurrentReportCellPosition;
    @SerializedName("StartingCurrentReportDisplay")
    @Expose
    private StartingCurrentReportDisplay startingCurrentReportDisplay;
    @SerializedName("VoltageVariationReportCellPosition")
    @Expose
    private VoltageVariationReportCellPosition voltageVariationReportCellPosition;
    @SerializedName("VoltageVariationReportDisplay")
    @Expose
    private VoltageVariationReportDisplay voltageVariationReportDisplay;
    @SerializedName("RepeatabilityReportCellPosition")
    @Expose
    private RepeatabilityReportCellPosition repeatabilityReportCellPosition;
    @SerializedName("VoltageUnbalanceReportDisplay")
    @Expose
    private VoltageUnbalanceReportDisplay voltageUnbalanceReportDisplay;
    @SerializedName("VoltageUnbalanceReportCellPosition")
    @Expose
    private VoltageUnbalanceReportCellPosition voltageUnbalanceReportCellPosition;
    @SerializedName("RepeatabilityReportDisplay")
    @Expose
    private RepeatabilityReportDisplay repeatabilityReportDisplay;



    public MeterProfileReportCellPosition getMeterProfileReport() {
    	return meterProfileReport;
    }

    public void setMeterProfileReport(MeterProfileReportCellPosition meterProfileReport) {
    	this.meterProfileReport = meterProfileReport;
    }

    public MeterProfileReportDisplay getMeterProfileReportDisplay() {
    	return meterProfileReportDisplay;
    }

    public void setMeterProfileReportDisplay(MeterProfileReportDisplay meterProfileReportDisplay) {
    	this.meterProfileReportDisplay = meterProfileReportDisplay;
    }
/*    @SerializedName("CalibAccuracyReport")
    @Expose
    private CalibAccuracyReport calibAccuracyReport;
    @SerializedName("CalibrationReport")
    @Expose
    private CalibrationReport calibrationReport;
*/
    public String getConfigFileVersion() {
        return configFileVersion;
    }

    public void setConfigFileVersion(String configFileVersion) {
        this.configFileVersion = configFileVersion;
    }
    
    public AccuracyReportCellPosition getAccuracyReportCellPosition() {
    	return accuracyReportCellPosition;
    	}

    	public void setAccuracyReportCellPosition(AccuracyReportCellPosition accuracyReportCellPosition) {
    	this.accuracyReportCellPosition = accuracyReportCellPosition;
    	}

    	public AccuracyReportDisplay getAccuracyReportDisplay() {
    	return accuracyReportDisplay;
    	}

    	public void setAccuracyReportDisplay(AccuracyReportDisplay accuracyReportDisplay) {
    	this.accuracyReportDisplay = accuracyReportDisplay;
    	}

    	public NoLoadReportCellPosition getNoLoadReportCellPosition() {
    	return noLoadReportCellPosition;
    	}

    	public void setNoLoadReportCellPosition(NoLoadReportCellPosition noLoadReportCellPosition) {
    	this.noLoadReportCellPosition = noLoadReportCellPosition;
    	}

    	public NoLoadReportDisplay getNoLoadReportDisplay() {
    	return noLoadReportDisplay;
    	}

    	public void setNoLoadReportDisplay(NoLoadReportDisplay noLoadReportDisplay) {
    	this.noLoadReportDisplay = noLoadReportDisplay;
    	}

    	public StartingCurrentReportCellPosition getStartingCurrentReportCellPosition() {
    	return startingCurrentReportCellPosition;
    	}

    	public void setStartingCurrentReportCellPosition(StartingCurrentReportCellPosition startingCurrentReportCellPosition) {
    	this.startingCurrentReportCellPosition = startingCurrentReportCellPosition;
    	}

    	public StartingCurrentReportDisplay getStartingCurrentReportDisplay() {
    	return startingCurrentReportDisplay;
    	}

    	public void setStartingCurrentReportDisplay(StartingCurrentReportDisplay startingCurrentReportDisplay) {
    	this.startingCurrentReportDisplay = startingCurrentReportDisplay;
    	}

    	public VoltageVariationReportCellPosition getVoltageVariationReportCellPosition() {
    	return voltageVariationReportCellPosition;
    	}

    	public void setVoltageVariationReportCellPosition(VoltageVariationReportCellPosition voltageVariationReportCellPosition) {
    	this.voltageVariationReportCellPosition = voltageVariationReportCellPosition;
    	}

    	public VoltageVariationReportDisplay getVoltageVariationReportDisplay() {
    	return voltageVariationReportDisplay;
    	}

    	public void setVoltageVariationReportDisplay(VoltageVariationReportDisplay voltageVariationReportDisplay) {
    	this.voltageVariationReportDisplay = voltageVariationReportDisplay;
    	}

    	public RepeatabilityReportCellPosition getRepeatabilityReportCellPosition() {
    	return repeatabilityReportCellPosition;
    	}

    	public void setRepeatabilityReportCellPosition(RepeatabilityReportCellPosition repeatabilityReportCellPosition) {
    	this.repeatabilityReportCellPosition = repeatabilityReportCellPosition;
    	}

    	public VoltageUnbalanceReportDisplay getVoltageUnbalanceReportDisplay() {
    	return voltageUnbalanceReportDisplay;
    	}

    	public void setVoltageUnbalanceReportDisplay(VoltageUnbalanceReportDisplay voltageUnbalanceReportDisplay) {
    	this.voltageUnbalanceReportDisplay = voltageUnbalanceReportDisplay;
    	}

    	public VoltageUnbalanceReportCellPosition getVoltageUnbalanceReportCellPosition() {
    	return voltageUnbalanceReportCellPosition;
    	}

    	public void setVoltageUnbalanceReportCellPosition(VoltageUnbalanceReportCellPosition voltageUnbalanceReportCellPosition) {
    	this.voltageUnbalanceReportCellPosition = voltageUnbalanceReportCellPosition;
    	}

    	public RepeatabilityReportDisplay getRepeatabilityReportDisplay() {
    	return repeatabilityReportDisplay;
    	}

    	public void setRepeatabilityReportDisplay(RepeatabilityReportDisplay repeatabilityReportDisplay) {
    	this.repeatabilityReportDisplay = repeatabilityReportDisplay;
    	}

}
