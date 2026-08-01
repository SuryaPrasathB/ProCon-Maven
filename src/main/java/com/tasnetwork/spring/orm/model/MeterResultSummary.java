package com.tasnetwork.spring.orm.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MeterResultSummary {

    private int serialNo;
    private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();
    
    private boolean isSelected;
    private SimpleBooleanProperty isSelectedProperty = new SimpleBooleanProperty();
    
    private String meterSerialNo;
    private SimpleStringProperty meterSerialNoProperty = new SimpleStringProperty();

    private String palletDistinctId;
    private SimpleStringProperty palletDistinctIdProperty = new SimpleStringProperty();

    private int palletBatchNo;
    private SimpleIntegerProperty palletBatchNoProperty = new SimpleIntegerProperty();

    private String palletQrId;
    private SimpleStringProperty palletQrIdProperty = new SimpleStringProperty();

    private String testVerific1="";
    private SimpleStringProperty testVerific1Property = new SimpleStringProperty();

    private String testTypeCalib="";
    private SimpleStringProperty testTypeCalibProperty = new SimpleStringProperty();

    private String testTypeComm="";
    private SimpleStringProperty testTypeCommProperty = new SimpleStringProperty();

    private String testTypeFt="";
    private SimpleStringProperty testTypeFtProperty = new SimpleStringProperty();

    private String testTypeHv="";
    private SimpleStringProperty testTypeHvProperty = new SimpleStringProperty();

    private String testTypeIr="";
    private SimpleStringProperty testTypeIrProperty = new SimpleStringProperty();

    private String testTypeNoLoad="";
    private SimpleStringProperty testTypeNoLoadProperty = new SimpleStringProperty();

    private String testTypeSta="";
    private SimpleStringProperty testTypeStaProperty = new SimpleStringProperty();

    private String overAllStatus="";
    private SimpleStringProperty overAllStatusProperty = new SimpleStringProperty();
    
    private int rackPositionNo = 0;

    // Getter and Setter for serialNo
    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public SimpleIntegerProperty getSerialNoProperty() {
        serialNoProperty.set(serialNo);
        return serialNoProperty;
    }

    // Getter and Setter for palletDistinctId
    public String getPalletDistinctId() {
        return palletDistinctId;
    }

    public void setPalletDistinctId(String palletDistinctId) {
        this.palletDistinctId = palletDistinctId;
    }

    public SimpleStringProperty getPalletDistinctIdProperty() {
        palletDistinctIdProperty.set(palletDistinctId);
        return palletDistinctIdProperty;
    }

    // Getter and Setter for palletBatchNo
    public int getPalletBatchNo() {
        return palletBatchNo;
    }

    public void setPalletBatchNo(int palletBatchNo) {
        this.palletBatchNo = palletBatchNo;
    }

    public SimpleIntegerProperty getPalletBatchNoProperty() {
        palletBatchNoProperty.set(palletBatchNo);
        return palletBatchNoProperty;
    }

    // Getter and Setter for palletQrId
    public String getPalletQrId() {
        return palletQrId;
    }

    public void setPalletQrId(String palletQrId) {
        this.palletQrId = palletQrId;
    }

    public SimpleStringProperty getPalletQrIdProperty() {
        palletQrIdProperty.set(palletQrId);
        return palletQrIdProperty;
    }

    // Getter and Setter for testTVerific1
    public String getTestVerific1() {
        return testVerific1;
    }

    public void setTestVerific1(String testVerific1) {
        this.testVerific1 = testVerific1;
    }

    public SimpleStringProperty getTestVerific1Property() {
        testVerific1Property.set(testVerific1);
        return testVerific1Property;
    }

    // Getter and Setter for testTypeCalib
    public String getTestTypeCalib() {
        return testTypeCalib;
    }

    public void setTestTypeCalib(String testTypeCalib) {
        this.testTypeCalib = testTypeCalib;
    }

    public SimpleStringProperty getTestTypeCalibProperty() {
        testTypeCalibProperty.set(testTypeCalib);
        return testTypeCalibProperty;
    }

    // Getter and Setter for testTypeComm
    public String getTestTypeComm() {
        return testTypeComm;
    }

    public void setTestTypeComm(String testTypeComm) {
        this.testTypeComm = testTypeComm;
    }

    public SimpleStringProperty getTestTypeCommProperty() {
        testTypeCommProperty.set(testTypeComm);
        return testTypeCommProperty;
    }

    // Getter and Setter for testTypeFt
    public String getTestTypeFt() {
        return testTypeFt;
    }

    public void setTestTypeFt(String testTypeFt) {
        this.testTypeFt = testTypeFt;
    }

    public SimpleStringProperty getTestTypeFtProperty() {
        testTypeFtProperty.set(testTypeFt);
        return testTypeFtProperty;
    }

    // Getter and Setter for testTypeHv
    public String getTestTypeHv() {
        return testTypeHv;
    }

    public void setTestTypeHv(String testTypeHv) {
        this.testTypeHv = testTypeHv;
    }

    public SimpleStringProperty getTestTypeHvProperty() {
        testTypeHvProperty.set(testTypeHv);
        return testTypeHvProperty;
    }

    // Getter and Setter for testTypeIr
    public String getTestTypeIr() {
        return testTypeIr;
    }

    public void setTestTypeIr(String testTypeIr) {
        this.testTypeIr = testTypeIr;
    }

    public SimpleStringProperty getTestTypeIrProperty() {
        testTypeIrProperty.set(testTypeIr);
        return testTypeIrProperty;
    }

    // Getter and Setter for testTypeNoLoad
    public String getTestTypeNoLoad() {
    	 testTypeNoLoadProperty.set(testTypeNoLoad);
        return testTypeNoLoad;
    }

    public void setTestTypeNoLoad(String testTypeNoLoad) {
        this.testTypeNoLoad = testTypeNoLoad;
        testTypeNoLoadProperty.set(testTypeNoLoad);
    }

    public SimpleStringProperty getTestTypeNoLoadProperty() {
        testTypeNoLoadProperty.set(testTypeNoLoad);
        return testTypeNoLoadProperty;
    }

    // Getter and Setter for testTypeSta
    public String getTestTypeSta() {
        return testTypeSta;
    }

    public void setTestTypeSta(String testTypeSta) {
        this.testTypeSta = testTypeSta;
    }

    public SimpleStringProperty getTestTypeStaProperty() {
        testTypeStaProperty.set(testTypeSta);
        return testTypeStaProperty;
    }

    // Getter and Setter for vverAllStatus
    public String getOverAllStatus() {
        return overAllStatus;
    }

    public void setOverAllStatus(String overAllStatus) {
        this.overAllStatus = overAllStatus;
    }

    public SimpleStringProperty getOverAllStatusProperty() {
        overAllStatusProperty.set(overAllStatus);
        return overAllStatusProperty;
    }

	public boolean getIsSelected() {
		return isSelected;
	}

	public SimpleBooleanProperty getIsSelectedProperty() {
		isSelectedProperty.set(isSelected);
		return isSelectedProperty;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
	public String getMeterSerialNo() {
		return meterSerialNo;
	}

	public SimpleStringProperty getMeterSerialNoProperty() {
		meterSerialNoProperty.set(meterSerialNo);
		return meterSerialNoProperty;
	}

	public void setMeterSerialNo(String meterSerialNo) {
		this.meterSerialNo = meterSerialNo;
	}
	
	

	public int getRackPositionNo() {
		return rackPositionNo;
	}

	public void setRackPositionNo(int rackPositionNo) {
		this.rackPositionNo = rackPositionNo;
	}
}
