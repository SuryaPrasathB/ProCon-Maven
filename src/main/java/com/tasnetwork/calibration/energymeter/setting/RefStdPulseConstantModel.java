package com.tasnetwork.calibration.energymeter.setting;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RefStdPulseConstantModel {
	
	private final SimpleStringProperty serialNo;
	private final SimpleStringProperty currentTapValue;
	private final SimpleStringProperty operation;
/*	private final SimpleStringProperty constantInV1_Wh;
	
	private final SimpleStringProperty constantInV2_Wh;
	private final SimpleStringProperty constantInV3_Wh;
	private final SimpleStringProperty constantInV4_Wh;
	private final SimpleStringProperty constantInV5_Wh;*/
	
	private final IntegerProperty constantInV1_Wh;	
	private final IntegerProperty constantInV2_Wh;
	private final IntegerProperty constantInV3_Wh;
	private final IntegerProperty constantInV4_Wh;
	private final IntegerProperty constantInV5_Wh;
	
	//private final IntegerProperty constantInV1_Wh1;
	
	//private final IntegerProperty constantAtV1;
	
	RefStdPulseConstantModel(){
		serialNo = new SimpleStringProperty("A");
		currentTapValue = new SimpleStringProperty("B");
		operation = new SimpleStringProperty(ConstantApp.REF_STD_OPERATION_CURRENT_ABOVE_OR_EQUAL);
/*		constantInV1_Wh = new SimpleStringProperty("D");
		constantInV2_Wh = new SimpleStringProperty("E");
		constantInV3_Wh = new SimpleStringProperty("F");
		constantInV4_Wh = new SimpleStringProperty("G");
		constantInV5_Wh = new SimpleStringProperty("H");*/
		
		constantInV1_Wh = new SimpleIntegerProperty(ConstantApp.DEFAULT_PULSE_CONSTANT);
		constantInV2_Wh = new SimpleIntegerProperty(ConstantApp.DEFAULT_PULSE_CONSTANT);
		constantInV3_Wh = new SimpleIntegerProperty(ConstantApp.DEFAULT_PULSE_CONSTANT);
		constantInV4_Wh = new SimpleIntegerProperty(ConstantApp.DEFAULT_PULSE_CONSTANT);
		constantInV5_Wh = new SimpleIntegerProperty(ConstantApp.DEFAULT_PULSE_CONSTANT);
		
		//constantInV1_Wh1 = new SimpleIntegerProperty(1);
		
		//constantAtV1 = new SimpleIntegerProperty(0);
		
	}
	
	public SimpleStringProperty getSerialNoProperty() {
		return serialNo;
	}
	
	public String getSerialNo() {
		return serialNo.get();
	}
	
	public void setSerialNo(String value) {
		serialNo.set(value);;
	}
	
	
	
	public SimpleStringProperty getCurrentTapValueProperty() {
		return currentTapValue;
	}
	
	public String getCurrentTapValue() {
		return currentTapValue.get();
	}
	
	public void setCurrentTapValue(String value) {
		currentTapValue.set(value);;
	}
	

	
	public SimpleStringProperty getOperationProperty() {
		return operation;
	}
	
	public String getOperation() {
		return operation.get();
	}
	
	public void setOperation(String value) {
		operation.set(value);;
	}
	
	
	public IntegerProperty getConstantInV1_WhProperty() {
		return constantInV1_Wh;
	}
	
	public Integer getConstantInV1_Wh() {
		return constantInV1_Wh.get();
	}
	
	public void setConstantInV1_Wh(Integer value) {
		constantInV1_Wh.set(value);;
	}

	
/*	public IntegerProperty getConstantInV1_Wh1_WhProperty() {
		return constantInV1_Wh1;
	}
	
	public Integer getConstantInV1_Wh1() {
		return constantInV1_Wh1.get();
	}
	
	public void setConstantInV1_Wh1(Integer value) {
		constantInV1_Wh1.set(value);;
	}*/
	
	
	
	
	
	
	public IntegerProperty getConstantInV2_WhProperty() {
		return constantInV2_Wh;
	}
	
	public Integer getConstantInV2_Wh() {
		return constantInV2_Wh.get();
	}
	
	public void setConstantInV2_Wh(Integer value) {
		constantInV2_Wh.set(value);;
	}
	
	public IntegerProperty getConstantInV3_WhProperty() {
		return constantInV3_Wh;
	}
	
	public Integer getConstantInV3_Wh() {
		return constantInV3_Wh.get();
	}
	
	public void setConstantInV3_Wh(Integer value) {
		constantInV3_Wh.set(value);;
	}
	
	public IntegerProperty getConstantInV4_WhProperty() {
		return constantInV4_Wh;
	}
	
	public Integer getConstantInV4_Wh() {
		return constantInV4_Wh.get();
	}
	
	public void setConstantInV4_Wh(Integer value) {
		constantInV4_Wh.set(value);;
	}
	
	public IntegerProperty getConstantInV5_WhProperty() {
		return constantInV5_Wh;
	}
	
	public Integer getConstantInV5_Wh() {
		return constantInV5_Wh.get();
	}
	
	public void setConstantInV5_Wh(Integer value) {
		constantInV5_Wh.set(value);;
	}
	


}
