package com.tasnetwork.calibration.energymeter.testprofiles;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestSetupSummaryDataModel {
	
	private final StringProperty testData;
	private final BooleanProperty isSelected;
	private final StringProperty sequentialno;
	
	public TestSetupSummaryDataModel(String sequentialno,String testData, Boolean isSelected ) {
	
		this.sequentialno = new SimpleStringProperty(sequentialno);
		this.testData = new SimpleStringProperty(testData);
		this.isSelected = new SimpleBooleanProperty(isSelected);
		
	
	}
	
	public StringProperty testDataSerialNoProperty(){
		return sequentialno;
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
	public boolean equals(Object obj) {
		if (!(obj instanceof TestSetupSummaryDataModel)) {
			return false;
		}
		TestSetupSummaryDataModel testCaseObj = (TestSetupSummaryDataModel)obj;
		if (getTestData().equals(testCaseObj.getTestData())) {
			return true;
		}
		return false;
	}

}
