package com.tasnetwork.calibration.energymeter.setting;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EnergyMeterModel {
	
	private final StringProperty customerName;
	private final StringProperty emModelName;
	private final StringProperty meterType;
	private final StringProperty baseCurrent;
	private final StringProperty maxCurrent;
	private final StringProperty ratedVoltage;
	private final StringProperty noOfImpulsesPerKWH;
	private final StringProperty classValue;
	private final StringProperty frequency;
	private final StringProperty ct_Type;
	private final StringProperty ctr_ratio;
	private final StringProperty ptr_ratio;

	public EnergyMeterModel(String customerName, String emModelName, String meterType, 
			String baseCurrent, String maxCurrent , String ratedVoltage, String noOfImpulsesPerKWH, String classValue,
			String frequency, String ct_type, String ctr_ratio, String ptr_ratio) {
		
		this.customerName = new SimpleStringProperty(customerName);
		this.emModelName = new SimpleStringProperty(emModelName);
		
		this.meterType = new SimpleStringProperty(meterType);
		this.baseCurrent = new SimpleStringProperty(baseCurrent);
		this.maxCurrent = new SimpleStringProperty(maxCurrent);
		this.ratedVoltage = new SimpleStringProperty(ratedVoltage);
		this.noOfImpulsesPerKWH = new SimpleStringProperty(noOfImpulsesPerKWH);
		this.classValue = new SimpleStringProperty(classValue);
		this.frequency = new SimpleStringProperty(frequency);
		this.ct_Type = new SimpleStringProperty(ct_type);
		this.ctr_ratio = new SimpleStringProperty(ctr_ratio);
		this.ptr_ratio = new SimpleStringProperty(ptr_ratio);
	}
	
	public String getCustomerName() {
		return customerName.get();
	}

	public void setCustomerName(String customerName) {
		this.customerName.set(customerName);
	}
	
	public StringProperty customerNameProperty() {
		return customerName;
	}

	public String getEmModelName() {
		return emModelName.get();
	}

	public void setEmModelName(String emModelName) {
		this.emModelName.set(emModelName);
	}
	
	public StringProperty emModelNameProperty() {
		return emModelName;
	}
	
	public String getMeterType() {
		return meterType.get();
	}

	public void setMeterType(String meterType) {
		this.meterType.set(meterType);
	}
	
	public String getBaseCurrent() {
		return baseCurrent.get();
	}

	public void setBaseCurrent(String baseCurrent) {
		this.baseCurrent.set(baseCurrent);
	}
	
	public String getMaxCurrent() {
		return maxCurrent.get();
	}

	public void setMaxCurrent(String maxCurrent) {
		this.maxCurrent.set(maxCurrent);
	}
	
	public String getRatedVoltage() {
		return ratedVoltage.get();
	}

	public void setRatedVoltage(String ratedVoltage) {
		this.ratedVoltage.set(ratedVoltage);
	}
	
	public String getNoOfImpulsesPerKWH() {
		return noOfImpulsesPerKWH.get();
	}

	public void setNoOfImpulsesPerKWH(String noOfImpulsesPerKWH) {
		this.noOfImpulsesPerKWH.set(noOfImpulsesPerKWH);
	}
	
	public String getClassValue() {
		return classValue.get();
	}

	public void setClassValue(String classValue) {
		this.classValue.set(classValue);
	}
	
	public String getFrequency() {
		return frequency.get();
	}

	public void setFrequency(String frequency) {
		this.frequency.set(frequency);
	}
	
	public String getCtr_Ratio() {
		return ctr_ratio.get();
	}

	public void setCtr_Ratio(String Ctr_Ratio) {
		this.ctr_ratio.set(Ctr_Ratio);
	}
	
	public StringProperty ctrRatioProperty() {
		return ctr_ratio;
	}
	
	public String getCt_Type() {
		return ct_Type.get();
	}

	public void setCt_Type(String ctType) {
		this.ct_Type.set(ctType);
	}
	
	public String getPtr_Ratio() {
		return ptr_ratio.get();
	}

	public void setPtr_Ratio(String Ptr_Ratio) {
		this.ptr_ratio.set(Ptr_Ratio);
	}
	
	public StringProperty ptrRatioProperty() {
		return ctr_ratio;
	}
	
	public String toStrig() {
		
		 StringBuilder toPrint = new StringBuilder();
		 toPrint.append("CustomerName:" + customerName).append(", ");
		 toPrint.append("ModelName:" + emModelName).append(", ");
		 toPrint.append("MeterType:" + meterType).append(", ");
		 toPrint.append("BaseCurrent:" + baseCurrent).append(", ");
		 toPrint.append("MaxCurrent:" + maxCurrent).append(", ");
		 toPrint.append("RatedVoltage:" + ratedVoltage).append(", ");
		 toPrint.append("NoOfImpulsesKWH:" + noOfImpulsesPerKWH).append(", ");
		 toPrint.append("ClassValue:" + classValue);
		 
	     return toPrint.toString();
	}
}
