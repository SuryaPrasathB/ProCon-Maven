package com.tasnetwork.spring.orm.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MeterResultDetailed {

	 private int serialNo;
	    private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();

	    private String testType;
	    private SimpleStringProperty testTypeProperty = new SimpleStringProperty();

	    private String dutSerialNo;
	    //private SimpleStringProperty dutSerialNoProperty = new SimpleStringProperty();
	    
	    private String palletDistinctId;
	    //private SimpleStringProperty palletDistinctIdProperty = new SimpleStringProperty();

	    private String testName;
	    private SimpleStringProperty testNameProperty = new SimpleStringProperty();

	    private String resultValue;
	    private SimpleStringProperty resultValueProperty = new SimpleStringProperty();

	    private String resultStatus;
	    private SimpleStringProperty resultStatusProperty = new SimpleStringProperty();
	    
	    
	    private String  permissibleLimitDisplay = "";
		private SimpleStringProperty  permissibleLimitDisplayProperty = new SimpleStringProperty ();

	    // Getter and Setter for serialNo
	    public int getSerialNo() {
	        return serialNo;
	    }
	    
	    public MeterResultDetailed() {
	    	
	    }
	    
	    public MeterResultDetailed(String testType, String testName) {
	        this.testType = testType;
	        this.testName = testName;
	    }

	    public void setSerialNo(int serialNo) {
	        this.serialNo = serialNo;
	        this.serialNoProperty.set(serialNo);
	    }

	    public SimpleIntegerProperty getSerialNoProperty() {
	        serialNoProperty.set(serialNo);
	        return serialNoProperty;
	    }

	    // Getter and Setter for testType
	    public String getTestType() {
	        return testType;
	    }

	    public void setTestType(String testType) {
	        this.testType = testType;
	        this.testTypeProperty.set(testType);
	    }

	    public SimpleStringProperty getTestTypeProperty() {
	        testTypeProperty.set(testType);
	        return testTypeProperty;
	    }

	    // Getter and Setter for testName
	    public String getTestName() {
	        return testName;
	    }

	    public void setTestName(String testName) {
	        this.testName = testName;
	        this.testNameProperty.set(testName);
	    }

	    public SimpleStringProperty getTestNameProperty() {
	        testNameProperty.set(testName);
	        return testNameProperty;
	    }

	    // Getter and Setter for resultValue
	    public String getResultValue() {
	        return resultValue;
	    }

	    public void setResultValue(String resultValue) {
	        this.resultValue = resultValue;
	        this.resultValueProperty.set(resultValue);
	    }

	    public SimpleStringProperty getResultValueProperty() {
	        resultValueProperty.set(resultValue);
	        return resultValueProperty;
	    }

	    // Getter and Setter for resultStatus
	    public String getResultStatus() {
	        return resultStatus;
	    }

	    public void setResultStatus(String resultStatus) {
	        this.resultStatus = resultStatus;
	        this.resultStatusProperty.set(resultStatus);
	    }

	    public SimpleStringProperty getResultStatusProperty() {
	        resultStatusProperty.set(resultStatus);
	        return resultStatusProperty;
	    }

		public String getPermissibleLimitDisplay() {
			return permissibleLimitDisplay;
		}

		public SimpleStringProperty getPermissibleLimitDisplayProperty() {
			permissibleLimitDisplayProperty.set(permissibleLimitDisplay);
			return permissibleLimitDisplayProperty;
		}

		public void setPermissibleLimitDisplay(String permissibleLimitDisplay) {
			this.permissibleLimitDisplay = permissibleLimitDisplay;
			 this.permissibleLimitDisplayProperty.set(permissibleLimitDisplay);
		}

		public void setPermissibleLimitDisplayProperty(SimpleStringProperty permissibleLimitDisplayProperty) {
			this.permissibleLimitDisplayProperty = permissibleLimitDisplayProperty;
		}

		public String getDutSerialNo() {
			return dutSerialNo;
		}

		public String getPalletDistinctId() {
			return palletDistinctId;
		}

		public void setDutSerialNo(String dutSerialNo) {
			this.dutSerialNo = dutSerialNo;
		}

		public void setPalletDistinctId(String palletDistinctId) {
			this.palletDistinctId = palletDistinctId;
		}
	}
