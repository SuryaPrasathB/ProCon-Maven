package com.tasnetwork.spring.orm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "bay_device_config")
//@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class BayDeviceConfig {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;



	// @CreatedBy
	//  private User user;


	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	//@GeneratedValue(strategy = GenerationType.UUID )
	//@Id
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String serialNo;

	@Transient
	private SimpleStringProperty serialNoProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String terminalId;

	@Transient
	private SimpleStringProperty terminalIdProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String terminalName;

	@Transient
	private SimpleStringProperty terminalNameProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String bayKey;

	@Transient
	private SimpleStringProperty bayKeyProperty = new SimpleStringProperty();
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean bayDevicesActive= true;

	@Transient
	private SimpleBooleanProperty bayDevicesActiveProperty = new SimpleBooleanProperty();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String bayName;

	@Transient
	private SimpleStringProperty bayNameProperty = new SimpleStringProperty();
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrPalletDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty qrPalletDeviceEnabledProperty = new SimpleBooleanProperty();
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrDutDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty qrDutDeviceEnabledProperty = new SimpleBooleanProperty();
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean opticalSensorDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty opticalSensorDeviceEnabledProperty = new SimpleBooleanProperty();	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean dutDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty dutDeviceEnabledProperty = new SimpleBooleanProperty();	
	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean voltPmDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty voltPmDeviceEnabledProperty = new SimpleBooleanProperty();	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean megaOhmPmDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty megaOhmPmDeviceEnabledProperty = new SimpleBooleanProperty();	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean lduDeviceEnabled = false;

	@Transient
	private SimpleBooleanProperty lduDeviceEnabledProperty = new SimpleBooleanProperty();	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrPalletCommTypeSerialConfigured = true;
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrDutCommTypeSerialConfigured = true;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean opticalSensorCommTypeSerialConfigured = true;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean dutCommTypeSerialConfigured = true;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean voltPmCommTypeSerialConfigured = true;	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean megaOhmPmCommTypeSerialConfigured = true;
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean lduCommTypeSerialConfigured = true;

	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrPalletCommTypeIpAddressConfigured = false;
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean qrDutCommTypeIpAddressConfigured = false;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean opticalSensorCommTypeIpAddressConfigured = false;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean dutCommTypeIpAddressConfigured = false;

	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean voltPmCommTypeIpAddressConfigured = false;	
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean megaOhmPmCommTypeIpAddressConfigured = false;
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean lduCommTypeIpAddressConfigured = false;
		
	@Column(columnDefinition = "VARCHAR(45)")
	private String qrPalletComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty qrPalletComTypeProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String qrDutComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty qrDutComTypeProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String opticalSensorComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty opticalSensorComTypeProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String dutComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty dutComTypeProperty = new SimpleStringProperty();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String voltPmComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty voltPmComTypeProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String megaOhmPmComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty megaOhmPmComTypeProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String lduComType = ConstantConveyor.DEVICE_COM_TYPE_SERIAL;

	@Transient
	private SimpleStringProperty lduComTypeProperty = new SimpleStringProperty();


	public Integer getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public SimpleStringProperty getSerialNoProperty() {
		serialNoProperty.set(serialNo);
		return serialNoProperty;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public SimpleStringProperty getTerminalIdProperty() {
		terminalIdProperty.set(terminalId);
		return terminalIdProperty;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public SimpleStringProperty getTerminalNameProperty() {
		terminalNameProperty.set(terminalName);
		return terminalNameProperty;
	}

	public String getBayKey() {
		return bayKey;
	}

	public SimpleStringProperty getBayKeyProperty() {
		bayKeyProperty.set(bayKey);
		return bayKeyProperty;
	}

	public String getBayName() {
		return bayName;
	}

	public SimpleStringProperty getBayNameProperty() {
		bayNameProperty.set(bayName);
		return bayNameProperty;
	}



	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public void setBayKey(String bayKey) {
		this.bayKey = bayKey;
	}

	public void setBayName(String bayName) {
		this.bayName = bayName;
	}
/*	public Boolean isBayDevicesActive() {
		return bayDevicesActive;
	}*/

	public SimpleBooleanProperty getBayDevicesActiveProperty() {
		bayDevicesActiveProperty.set(bayDevicesActive);
		return bayDevicesActiveProperty;
	}

	public void setBayDevicesActive(Boolean bayActive) {
		this.bayDevicesActive = bayActive;
	}

	public Boolean isBayDevicesActive() {
		return bayDevicesActive;
	}

	public Boolean isQrPalletDeviceEnabled() {
		return qrPalletDeviceEnabled;
	}

	public SimpleBooleanProperty getQrPalletDeviceEnabledProperty() {
		qrPalletDeviceEnabledProperty.set(qrPalletDeviceEnabled);
		return qrPalletDeviceEnabledProperty;
	}

	public Boolean isQrDutDeviceEnabled() {
		return qrDutDeviceEnabled;
	}

	public SimpleBooleanProperty getQrDutDeviceEnabledProperty() {
		qrDutDeviceEnabledProperty.set(qrDutDeviceEnabled);
		return qrDutDeviceEnabledProperty;
	}

	public Boolean isOpticalSensorDeviceEnabled() {
		return opticalSensorDeviceEnabled;
	}

	public SimpleBooleanProperty getOpticalSensorDeviceEnabledProperty() {
		opticalSensorDeviceEnabledProperty.set(opticalSensorDeviceEnabled);
		return opticalSensorDeviceEnabledProperty;
	}

	public Boolean isDutDeviceEnabled() {
		return dutDeviceEnabled;
	}

	public SimpleBooleanProperty getDutDeviceEnabledProperty() {
		dutDeviceEnabledProperty.set(dutDeviceEnabled);
		return dutDeviceEnabledProperty;
	}

	public Boolean isVoltPmDeviceEnabled() {
		return voltPmDeviceEnabled;
	}

	public SimpleBooleanProperty getVoltPmDeviceEnabledProperty() {
		voltPmDeviceEnabledProperty.set(voltPmDeviceEnabled);
		return voltPmDeviceEnabledProperty;
	}

	public Boolean isMegaOhmPmDeviceEnabled() {
		return megaOhmPmDeviceEnabled;
	}

	public SimpleBooleanProperty getMegaOhmPmDeviceEnabledProperty() {
		megaOhmPmDeviceEnabledProperty.set(megaOhmPmDeviceEnabled);
		return megaOhmPmDeviceEnabledProperty;
	}

	public Boolean isLduDeviceEnabled() {
		return lduDeviceEnabled;
	}

	public SimpleBooleanProperty getLduDeviceEnabledProperty() {
		lduDeviceEnabledProperty.set(lduDeviceEnabled);
		return lduDeviceEnabledProperty;
	}

	public Boolean isQrPalletCommTypeSerialConfigured() {
		return qrPalletCommTypeSerialConfigured;
	}

	public Boolean isQrDutCommTypeSerialConfigured() {
		return qrDutCommTypeSerialConfigured;
	}

	public Boolean isOpticalSensorCommTypeSerialConfigured() {
		return opticalSensorCommTypeSerialConfigured;
	}

	public Boolean isDutCommTypeSerialConfigured() {
		return dutCommTypeSerialConfigured;
	}

	public Boolean isVoltPmCommTypeSerialConfigured() {
		return voltPmCommTypeSerialConfigured;
	}

	public Boolean isMegaOhmPmCommTypeSerialConfigured() {
		return megaOhmPmCommTypeSerialConfigured;
	}

	public Boolean isLduCommTypeSerialConfigured() {
		return lduCommTypeSerialConfigured;
	}

	public Boolean isQrPalletCommTypeIpAddressConfigured() {
		return qrPalletCommTypeIpAddressConfigured;
	}

	public Boolean isQrDutCommTypeIpAddressConfigured() {
		return qrDutCommTypeIpAddressConfigured;
	}

	public Boolean isOpticalSensorCommTypeIpAddressConfigured() {
		return opticalSensorCommTypeIpAddressConfigured;
	}

	public Boolean isDutCommTypeIpAddressConfigured() {
		return dutCommTypeIpAddressConfigured;
	}

	public Boolean isVoltPmCommTypeIpAddressConfigured() {
		return voltPmCommTypeIpAddressConfigured;
	}

	public Boolean isMegaOhmPmCommTypeIpAddressConfigured() {
		return megaOhmPmCommTypeIpAddressConfigured;
	}

	public Boolean isLduCommTypeIpAddressConfigured() {
		return lduCommTypeIpAddressConfigured;
	}

	public String getQrPalletComType() {
		return qrPalletComType;
	}

	public SimpleStringProperty getQrPalletComTypeProperty() {
		qrPalletComTypeProperty.set(qrPalletComType);
		return qrPalletComTypeProperty;
	}

	public String getQrDutComType() {
		return qrDutComType;
	}

	public SimpleStringProperty getQrDutComTypeProperty() {
		qrDutComTypeProperty.set(qrDutComType);
		return qrDutComTypeProperty;
	}

	public String getOpticalSensorComType() {
		return opticalSensorComType;
	}

	public SimpleStringProperty getOpticalSensorComTypeProperty() {
		opticalSensorComTypeProperty.set(opticalSensorComType);
		return opticalSensorComTypeProperty;
	}

	public String getDutComType() {
		return dutComType;
	}

	public SimpleStringProperty getDutComTypeProperty() {
		dutComTypeProperty.set(dutComType);
		return dutComTypeProperty;
	}

	public String getVoltPmComType() {
		return voltPmComType;
	}

	public SimpleStringProperty getVoltPmComTypeProperty() {
		voltPmComTypeProperty.set(voltPmComType);
		return voltPmComTypeProperty;
	}

	public String getMegaOhmPmComType() {
		return megaOhmPmComType;
	}

	public SimpleStringProperty getMegaOhmPmComTypeProperty() {
		megaOhmPmComTypeProperty.set(megaOhmPmComType);
		return megaOhmPmComTypeProperty;
	}

	public String getLduComType() {
		return lduComType;
	}

	public SimpleStringProperty getLduComTypeProperty() {
		lduComTypeProperty.set(lduComType);
		return lduComTypeProperty;
	}

	


	

	public void setQrPalletDeviceEnabled(Boolean qrPalletDeviceEnabled) {
		this.qrPalletDeviceEnabled = qrPalletDeviceEnabled;
	}

	public void setQrDutDeviceEnabled(Boolean qrDutDeviceEnabled) {
		this.qrDutDeviceEnabled = qrDutDeviceEnabled;
	}



	public void setOpticalSensorDeviceEnabled(Boolean opticalSensorDeviceEnabled) {
		this.opticalSensorDeviceEnabled = opticalSensorDeviceEnabled;
	}


	public void setDutDeviceEnabled(Boolean dutDeviceEnabled) {
		this.dutDeviceEnabled = dutDeviceEnabled;
	}


	public void setVoltPmDeviceEnabled(Boolean voltPmDeviceEnabled) {
		this.voltPmDeviceEnabled = voltPmDeviceEnabled;
	}


	public void setMegaOhmPmDeviceEnabled(Boolean megaOhmPmDeviceEnabled) {
		this.megaOhmPmDeviceEnabled = megaOhmPmDeviceEnabled;
	}


	public void setLduDeviceEnabled(Boolean lduDeviceEnabled) {
		this.lduDeviceEnabled = lduDeviceEnabled;
	}


	public void setQrPalletCommTypeSerialConfigured(Boolean qrPalletCommTypeSerialConfigured) {
		this.qrPalletCommTypeSerialConfigured = qrPalletCommTypeSerialConfigured;
	}

	public void setQrDutCommTypeSerialConfigured(Boolean qrDutCommTypeSerialConfigured) {
		this.qrDutCommTypeSerialConfigured = qrDutCommTypeSerialConfigured;
	}

	public void setOpticalSensorCommTypeSerialConfigured(Boolean opticalSensorCommTypeSerialConfigured) {
		this.opticalSensorCommTypeSerialConfigured = opticalSensorCommTypeSerialConfigured;
	}

	public void setDutCommTypeSerialConfigured(Boolean dutCommTypeSerialConfigured) {
		this.dutCommTypeSerialConfigured = dutCommTypeSerialConfigured;
	}

	public void setVoltPmCommTypeSerialConfigured(Boolean voltPmCommTypeSerialConfigured) {
		this.voltPmCommTypeSerialConfigured = voltPmCommTypeSerialConfigured;
	}

	public void setMegaOhmPmCommTypeSerialConfigured(Boolean megaOhmPmCommTypeSerialConfigured) {
		this.megaOhmPmCommTypeSerialConfigured = megaOhmPmCommTypeSerialConfigured;
	}

	public void setLduCommTypeSerialConfigured(Boolean lduCommTypeSerialConfigured) {
		this.lduCommTypeSerialConfigured = lduCommTypeSerialConfigured;
	}

	public void setQrPalletCommTypeIpAddressConfigured(Boolean qrPalletCommTypeIpAddressConfigured) {
		this.qrPalletCommTypeIpAddressConfigured = qrPalletCommTypeIpAddressConfigured;
	}

	public void setQrDutCommTypeIpAddressConfigured(Boolean qrDutCommTypeIpAddressConfigured) {
		this.qrDutCommTypeIpAddressConfigured = qrDutCommTypeIpAddressConfigured;
	}

	public void setOpticalSensorCommTypeIpAddressConfigured(Boolean opticalSensorCommTypeIpAddressConfigured) {
		this.opticalSensorCommTypeIpAddressConfigured = opticalSensorCommTypeIpAddressConfigured;
	}

	public void setDutCommTypeIpAddressConfigured(Boolean dutCommTypeIpAddressConfigured) {
		this.dutCommTypeIpAddressConfigured = dutCommTypeIpAddressConfigured;
	}

	public void setVoltPmCommTypeIpAddressConfigured(Boolean voltPmCommTypeIpAddressConfigured) {
		this.voltPmCommTypeIpAddressConfigured = voltPmCommTypeIpAddressConfigured;
	}

	public void setMegaOhmPmCommTypeIpAddressConfigured(Boolean megaOhmPmCommTypeIpAddressConfigured) {
		this.megaOhmPmCommTypeIpAddressConfigured = megaOhmPmCommTypeIpAddressConfigured;
	}

	public void setLduCommTypeIpAddressConfigured(Boolean lduCommTypeIpAddressConfigured) {
		this.lduCommTypeIpAddressConfigured = lduCommTypeIpAddressConfigured;
	}

	public void setQrPalletComType(String qrPalletComType) {
		this.qrPalletComType = qrPalletComType;
	}


	public void setQrDutComType(String qrDutComType) {
		this.qrDutComType = qrDutComType;
	}



	public void setOpticalSensorComType(String opticalSensorComType) {
		this.opticalSensorComType = opticalSensorComType;
	}



	public void setDutComType(String dutComType) {
		this.dutComType = dutComType;
	}



	public void setVoltPmComType(String voltPmComType) {
		this.voltPmComType = voltPmComType;
	}



	public void setMegaOhmPmComType(String megaOhmPmComType) {
		this.megaOhmPmComType = megaOhmPmComType;
	}


	public void setLduComType(String lduComType) {
		this.lduComType = lduComType;
	}


}
