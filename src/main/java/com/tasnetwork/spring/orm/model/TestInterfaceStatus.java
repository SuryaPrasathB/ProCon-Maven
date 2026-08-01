package com.tasnetwork.spring.orm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "TestIntefaceStatus")
public class TestInterfaceStatus {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;


	@Column(columnDefinition = "VARCHAR(45)") 
	private String serialNo;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String hardwareIdNo;

	@Transient
	private SimpleStringProperty serialNoProperty = new SimpleStringProperty();
	
	@Transient
	private SimpleStringProperty hardwareIdNoProperty = new SimpleStringProperty();


	@Column(columnDefinition = "VARCHAR(45)") 
	private String  serialStatus = "";
	@Transient
	private SimpleStringProperty serialStatusProperty = new SimpleStringProperty();


	@Column(columnDefinition = "VARCHAR(45)") 
	private String  deviceResponseStatus = "";
	@Transient
	private SimpleStringProperty deviceResponseStatusProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  deviceResponseData = "";
	@Transient
	private SimpleStringProperty deviceResponseDataProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  testStatus = "";
	@Transient
	private SimpleStringProperty testStatusProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  bayName = "";
	@Transient
	private SimpleStringProperty bayNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  deviceType = "";
	@Transient
	private SimpleStringProperty deviceTypeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  pathNo = "";	
	@Transient
	private SimpleStringProperty pathNoProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  positionNo = "";	
	@Transient
	private SimpleStringProperty positionNoProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  cName = "";	
	@Transient
	private SimpleStringProperty cNameProperty = new SimpleStringProperty();


	@Column(columnDefinition = "VARCHAR(45)") 
	private String  portName = "";	
	@Transient
	private SimpleStringProperty portNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  portId = "";
	@Transient
	private SimpleStringProperty portIdProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String  stateName = "";
	@Transient
	private SimpleStringProperty stateNameProperty = new SimpleStringProperty();

	public TestInterfaceStatus(){

	}

	public TestInterfaceStatus(
			String bayName, 
			String stateName, 
			String deviceType,
			String pathNo,
			String positionNo,
			String portName,
			String cName,
			String serialStatus,
			String deviceResponseData,
			String testStatus
			)
	{
		this.bayName = bayName;
		this.stateName = stateName;
		this.cName= cName;
		this.deviceType = deviceType;
		this.serialStatus= serialStatus;
		this.pathNo = pathNo;
		this.portName = portName;
		this.positionNo = positionNo;
		this.deviceResponseData = deviceResponseData;
		this.testStatus =testStatus;
	}
	
	@Override
	public String toString() {
	    return "TestInterfaceStatus{" +
	            "bay='" + bayName + '\'' +
	            ", state='" + stateName + '\'' +
	            ", cName='" + cName + '\'' +
	            ", device='" + deviceType + '\'' +
	            ", status='" + serialStatus + '\'' +
	            ", sequence='" + pathNo + '\'' +
	            ", position='" + positionNo + '\'' +	
	            ", response='" + deviceResponseData + '\'' +
	            ", executionStatus='" + testStatus + '\'' +
	            '}';
	}

	public TestInterfaceStatus(
			String sNo, 
			String bayName,  
			String stateName, 
			String deviceType, 
			String pathNo,
			String positionNo,
			String cName,
			String sStatus, 
			String qrR, 
			String qrD, 
			String tStatus) 
	{
		this.serialNo = sNo;
		this.bayName = bayName;
		this.stateName = stateName;
		this.deviceType = deviceType;
		this.pathNo = pathNo;
		this.positionNo = positionNo;
		this.cName = cName;
		this.serialStatus = sStatus;
		this.deviceResponseStatus = qrR;
		this.deviceResponseData = qrD;
		this.testStatus = tStatus;
	}
	
	


	public Integer getId() {
		return id;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public SimpleStringProperty getSerialNoProperty() {
		serialNoProperty.set(serialNo);
		return serialNoProperty;
	}
	
	public String getHardwareIdNo() {
		return hardwareIdNo;
	}


	public SimpleStringProperty getHardwareIdNoProperty() {
		hardwareIdNoProperty.set(hardwareIdNo);
		return hardwareIdNoProperty;
	}


	public String getSerialStatus() {
		return serialStatus;
	}


	public SimpleStringProperty getSerialStatusProperty() {
		serialStatusProperty.set(serialStatus);
		return serialStatusProperty;
	}


	public String getDeviceResponseStatus() {
		return deviceResponseStatus;
	}


	public SimpleStringProperty getDeviceResponseStatusProperty() {
		deviceResponseStatusProperty.set(deviceResponseStatus);
		return deviceResponseStatusProperty;
	}


	public String getDeviceResponseData() {
		return deviceResponseData;
	}


	public SimpleStringProperty getDeviceResponseDataProperty() {
		deviceResponseDataProperty.set(deviceResponseData);
		return deviceResponseDataProperty;
	}


	public String getTestStatus() {
		return testStatus;
	}


	public SimpleStringProperty getTestStatusProperty() {
		testStatusProperty.set(testStatus);
		return testStatusProperty;
	}


	public String getBayName() {
		return bayName;
	}


	public SimpleStringProperty getBayNameProperty() {
		bayNameProperty.set(bayName);
		return bayNameProperty;
	}


	public String getDeviceType() {
		return deviceType;
	}


	public SimpleStringProperty getDeviceTypeProperty() {
		deviceTypeProperty.set(deviceType);
		return deviceTypeProperty;
	}

	public String getPathNo() {
		return pathNo;
	}


	public SimpleStringProperty getPathNoProperty() {
		pathNoProperty.set(pathNo);
		return pathNoProperty;
	}


	public String getPositionNo() {
		return positionNo;
	}


	public SimpleStringProperty getPositionNoProperty() {
		positionNoProperty.set(positionNo);
		return positionNoProperty;
	}


	public String getcName() {
		return cName;
	}


	public SimpleStringProperty getcNameProperty() {
		cNameProperty.set(cName);
		return cNameProperty;
	}


	public String getPortId() {
		return portId;
	}


	public SimpleStringProperty getPortIdProperty() {
		portIdProperty.set(portId);
		return portIdProperty;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	
	public void setHardwareIdNo(String hardwareIdNo) {
			this.hardwareIdNo = hardwareIdNo;
		}



	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}




	public void setDeviceResponseStatus(String deviceResponseStatus) {
		this.deviceResponseStatus = deviceResponseStatus;
	}



	public void setDeviceResponseData(String deviceResponseData) {
		this.deviceResponseData = deviceResponseData;
	}




	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}


	public void setBayName(String bayName) {
		this.bayName = bayName;
	}




	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setPathNo(String pathNo) {
		this.pathNo = pathNo;
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}


	public void setPortId(String portId) {
		this.portId = portId;
	}


	public String getStateName() {
		return stateName;
	}


	public SimpleStringProperty getStateNameProperty() {
		stateNameProperty.set(stateName);
		return stateNameProperty;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}


	public String getPortName() {
		return portName;
	}


	public void setPortName(String portName) {
		this.portName = portName;
	}


	public SimpleStringProperty getPortNameProperty() {
		portNameProperty.set(portName);
		return portNameProperty;
	}




	/*	public SimpleStringProperty getSerialNoProperty() {
    	serialNoProperty.set(serialNo);
		return serialNoProperty;
	}*/




	/*StringProperty serialNo = new SimpleStringProperty();
	StringProperty serialStatus = new SimpleStringProperty();
	StringProperty qrResponse = new SimpleStringProperty();
	StringProperty qrData = new SimpleStringProperty();
	StringProperty testStatus = new SimpleStringProperty();
	StringProperty bayName = new SimpleStringProperty();
	StringProperty deviceType = new SimpleStringProperty();
	StringProperty positionNo = new SimpleStringProperty();	
	StringProperty cName = new SimpleStringProperty();	
	StringProperty portId = new SimpleStringProperty();

	TestIntefaceStatus() {}

	TestIntefaceStatus(String sNo, String bayName, String deviceType, String positionNo,String cName,String sStatus, String qrR, String qrD, String tStatus) {
		this.serialNo = new SimpleStringProperty(sNo);
		this.bayName = new SimpleStringProperty(bayName);
		this.deviceType = new SimpleStringProperty(deviceType);
		this.positionNo = new SimpleStringProperty(positionNo);
		this.cName = new SimpleStringProperty(cName);
		this.serialStatus = new SimpleStringProperty(sStatus);
		this.qrResponse = new SimpleStringProperty(qrR);
		this.qrData = new SimpleStringProperty(qrD);
		this.testStatus = new SimpleStringProperty(tStatus);
	}

	// SERIAL NO

	public StringProperty getSerialNoProperty () {
		return serialNo;
	}

	public void setSerialNoProperty(SimpleStringProperty serialNo) {
		this.serialNo = serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo.setValue(serialNo);
	}

	public String getSerialNo() {
		return this.serialNo.getValue();
	}

	// SERIAL STATUS

	public StringProperty getSerialStatusProperty () {
		return serialStatus;
	}

	public void setSerialStatusProperty(SimpleStringProperty sStatus) {
		this.serialStatus = sStatus;
	}

	public void setSerialStatus(String sStatus) {
		this.serialStatus.setValue(sStatus);
	}

	public String getSerialStatus() {
		return this.serialStatus.getValue();
	}

	// QR RESPONSE

	public StringProperty getQrResponseProperty () {
		return qrResponse;
	}

	public void setQrResponseProperty(SimpleStringProperty qrR) {
		this.qrResponse = qrR;
	}

	public void setQrResponse(String qrR) {
		this.qrResponse.setValue(qrR);
	}

	public String getQrResponse() {
		return this.qrResponse.getValue();
	}

	// QR DATA

	public StringProperty getQrDataProperty () {
		return qrData;
	}

	public void setQrDataProperty(SimpleStringProperty qrD) {
		this.qrData = qrD;
	}

	public void setQrData(String qrD) {
		this.qrData.setValue(qrD);
	}

	public String getQrData() {
		return this.qrData.getValue();
	}

	// TEST STATUS

	public StringProperty getTestStatusProperty () {
		return testStatus;
	}

	public void setTestStatusProperty(SimpleStringProperty tStatus) {
		this.testStatus = tStatus;
	}

	public void setTestStatus(String tStatus) {
		this.testStatus.setValue(tStatus);
	}

	public String getTestStatus() {
		return this.testStatus.getValue();
	}



	public StringProperty getBayNameProperty () {
		return bayName;
	}

	public void setBayName(String bName) {
		this.bayName.setValue(bName);
	}

	public String getBayName() {
		return this.bayName.getValue();
	}



	public StringProperty getDeviceTypeProperty () {
		return deviceType;
	}

	public void setDeviceType(String dType) {
		this.deviceType.setValue(dType);
	}

	public String getDeviceType() {
		return this.deviceType.getValue();
	}

	public StringProperty getPositionNoProperty () {
		return positionNo;
	}

	public void setPositionNo(String pNo) {
		this.positionNo.setValue(pNo);
	}

	public String getPositionNo() {
		return this.positionNo.getValue();
	}



	public StringProperty getCNameProperty () {
		return cName;
	}

	public void setCName(String pNo) {
		this.cName.setValue(pNo);
	}

	public String getCName() {
		return this.cName.getValue();
	}

	public StringProperty getPortIdProperty () {
		return portId;
	}

	public void setPortId(String pNo) {
		this.portId.setValue(pNo);
	}

	public String getPortId() {
		return this.portId.getValue();
	}
	 */

}
