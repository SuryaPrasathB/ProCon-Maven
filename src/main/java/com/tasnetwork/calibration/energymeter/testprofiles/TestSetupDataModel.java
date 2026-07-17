package com.tasnetwork.calibration.energymeter.testprofiles;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestSetupDataModel {
	
	private final StringProperty testData;
	private final BooleanProperty isSelected;
	private final BooleanProperty isSelected1;

    private transient TestDataTypeEnum testDataTypeEnum1;
    private transient TestDataTypeEnum testDataTypeEnum;
    
	public TestSetupDataModel(String testData, Boolean isSelected, TestDataTypeEnum dataTypeEnum, Boolean isSelected1, TestDataTypeEnum dataTypeEnum1) {
		
		this.testData = new SimpleStringProperty(testData);
		this.isSelected = new SimpleBooleanProperty(isSelected);
		this.testDataTypeEnum = dataTypeEnum;
		this.isSelected1 = new SimpleBooleanProperty(isSelected1);
		this.testDataTypeEnum1 = dataTypeEnum1;
	}
	
	public StringProperty testDataProperty() {
		return testData;
	}
	
	public String getTestData() {
		return testData.get();
	}

	public void setTestData(String testData) {
		this.testData.set(testData);
	}
	
	
	public BooleanProperty isSelectedProperty() {
		return isSelected;
	}
	public Boolean IsIsSelected() {
		return isSelected.get();
	}
	
	public Boolean getIsSelected() {
		return isSelected.get();
	}

	public void setIsSelected(Boolean selected) {
		this.isSelected.set(selected);
	}
	
	
	public TestDataTypeEnum getTestDataTypeEnum() {
		return testDataTypeEnum;
	}	
	
	public void setTestDataTypeEnum(TestDataTypeEnum enumValue) {
		this.testDataTypeEnum = enumValue;
	}
	
	public BooleanProperty isSelected1Property() {
		return isSelected1;
	}
	public Boolean IsIsSelected1() {
		return isSelected1.get();
	}
	
	public Boolean getIsSelected1() {
		return isSelected1.get();
	}

	public void setIsSelected1(Boolean selected1) {
		this.isSelected1.set(selected1);
	}
	
	public TestDataTypeEnum getTestDataTypeEnum1() {
		return testDataTypeEnum;
	}	
	
	public void setTestDataTypeEnum1(TestDataTypeEnum enumValue1) {
		this.testDataTypeEnum1 = enumValue1;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof TestSetupDataModel)) {
			return false;
		}
		TestSetupDataModel testCaseObj = (TestSetupDataModel)obj;
		if (getTestData().equals(testCaseObj.getTestData())) {
			return true;
		}
		return false;
	}
	
	public String toStrig() {
		
		 StringBuilder toPrint = new StringBuilder();
		 toPrint.append("Test Data Type:" + testDataTypeEnum).append(", ");
		 toPrint.append("Test Data:" + testData).append(", ");
		 toPrint.append("Selected:" + isSelected);
		 
	     return toPrint.toString();
	}
}
