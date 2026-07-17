package com.tasnetwork.calibration.conveyor.dutprocess;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DutDataLogParse {
	
	private String serialNo = "";
    private String date = "";
    private String timeStamp = "";
    private String voltage = "";
    private String current = "";
    private String activePowerWatt = "";
    private String reactivePowerVAR = "";
    private String apparentPowerVA = "";
    private String degreePhaseAngle = "";
    private String pf = "";
    private String energyMetric = "";
    private String freqency = "";
    private String phaseLabel = "";
    private String loraId = "";
    private String dutTimeStampEpoch = ""; 
    private String appDutTimeStampEpoch = ""; // app read time - not from parsed data
    private String appRefStdTimeStampEpoch = "";
    //private String metricUnit = "";
    

    public String getSerialNo() {
		return serialNo;
	}
	public String getDate() {
		return date;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public String getVoltage() {
		return voltage;
	}
	public String getCurrent() {
		return current;
	}
	public String getActivePowerWatt() {
		return activePowerWatt;
	}
	public String getPf() {
		return pf;
	}
	public String getEnergyMetric() {
		return energyMetric;
	}
	public String getFreqency() {
		return freqency;
	}
	public String getPhaseLabel() {
		return phaseLabel;
	}
	public String getLoraId() {
		return loraId;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public void setActivePowerWatt(String power) {
		this.activePowerWatt = power;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public void setEnergyMetric(String energyMetric) {
		this.energyMetric = energyMetric;
	}
	public void setFreqency(String freqency) {
		this.freqency = freqency;
	}
	public void setPhaseLabel(String phaseLabel) {
		this.phaseLabel = phaseLabel;
	}
	public void setLoraId(String loraId) {
		this.loraId = loraId;
	}
	
	
	
	public String getDutTimeStampEpoch() {
		return dutTimeStampEpoch;
	}
	
	public void setDutTimeStampEpoch(String dutTimeStampEpoch) {
		this.dutTimeStampEpoch = dutTimeStampEpoch;
	}
	
		
	public String getAppDutTimeStampEpoch() {
		return appDutTimeStampEpoch;
	}
	
	public void setAppDutTimeStampEpoch(String appDutTimeStampEpoch) {
		this.appDutTimeStampEpoch = appDutTimeStampEpoch;
	}
	public String getReactivePowerVAR() {
		return reactivePowerVAR;
	}
	public String getApparentPowerVA() {
		return apparentPowerVA;
	}
	public void setReactivePowerVAR(String reactivePowerVAR) {
		this.reactivePowerVAR = reactivePowerVAR;
	}
	public void setApparentPowerVA(String apparentPowerVA) {
		this.apparentPowerVA = apparentPowerVA;
	}
	public String getDegreePhaseAngle() {
		return degreePhaseAngle;
	}
	public void setDegreePhaseAngle(String degreePhaseAngle) {
		this.degreePhaseAngle = degreePhaseAngle;
	}
	public String getAppRefStdTimeStampEpoch() {
		return appRefStdTimeStampEpoch;
	}
	public void setAppRefStdTimeStampEpoch(String appRefStdTimeStampEpoch) {
		this.appRefStdTimeStampEpoch = appRefStdTimeStampEpoch;
	}

    
    
}

