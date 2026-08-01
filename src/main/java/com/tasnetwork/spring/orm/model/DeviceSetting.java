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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "device_settings")
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)

public class DeviceSetting {


	public DeviceSetting() {

	}

	public DeviceSetting(String deviceTypeKey, String modelName, String portName, String baudRate,String clusterId, String clusterName,
			String bayId,String bayName, String positionNo, String cName, String deviceId,String deviceType) {
		super();
		
		this.deviceTypeKey = deviceTypeKey;
		this.bayId = bayId;
		this.clusterId = clusterId;
		this.modelName = modelName;
		this.portName = portName;
		this.baudRate = baudRate;
		this.clusterName = clusterName;
		this.bayName = bayName;
		this.positionNo = positionNo;
		this.canName = cName;
		this.deviceId = deviceId;
		this.deviceType = deviceType;
	}

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
	private String deviceTypeKey="0";

	@Transient
	private SimpleStringProperty deviceTypeKeyProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String deviceType;

	@Transient
	private SimpleStringProperty deviceTypeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String modelName;

	@Transient
	private SimpleStringProperty modelNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String portName="";
	@Transient
	private SimpleStringProperty portNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String baudRate;

	@Transient
	private SimpleStringProperty baudRateProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String clusterName;

	@Transient
	private SimpleStringProperty clusterNameProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String clusterId="97";

	@Transient
	private SimpleStringProperty clusterIdProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String bayId="98";

	@Transient
	private SimpleStringProperty bayIdProperty = new SimpleStringProperty();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String bayName;

	@Transient
	private SimpleStringProperty bayNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String positionNo="99";

	@Transient
	private SimpleStringProperty positionNoProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String canName;

	@Transient
	private SimpleStringProperty cNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String deviceId="";

	@Transient
	private SimpleStringProperty deviceIdProperty = new SimpleStringProperty();
	
	@Transient
	private String serialStatus="";

	@Transient
	private SimpleStringProperty serialStatusProperty = new SimpleStringProperty();
	
	
	@Transient
	private int serialNo=0;

	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();
	
	@Transient
	private String rs485Address="";

	@Transient
	private SimpleStringProperty rs485AddressProperty = new SimpleStringProperty();	
	
	@Transient
	private String serialResponseData="";

	@Transient
	private SimpleStringProperty serialResponseDataProperty = new SimpleStringProperty();
	
	@Transient
    private final BooleanProperty buttonDisabled = new SimpleBooleanProperty(false);

    public BooleanProperty buttonDisabledProperty() {
        return buttonDisabled;
    }

    public boolean isButtonDisabled() {
        return buttonDisabled.get();
    }

    public void setButtonDisabled(boolean buttonDisabled) {
        this.buttonDisabled.set(buttonDisabled);
    }
	

	public Integer getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public String getDeviceTypeKey() {
		return deviceTypeKey;
	}

	public SimpleStringProperty getDeviceTypeKeyProperty() {
		deviceTypeKeyProperty.set(deviceTypeKey);
		return deviceTypeKeyProperty;
	}
	
	public SimpleStringProperty getDeviceTypeProperty() {
		return deviceTypeProperty;
	}

	public String getModelName() {
		return modelName;
	}

	public SimpleStringProperty getModelNameProperty() {
		modelNameProperty.set(modelName);
		return modelNameProperty;
	}

	public String getPortName() {
		return portName;
	}

	public SimpleStringProperty getPortNameProperty() {
		portNameProperty.set(portName);
		return portNameProperty;
	}

	public String getBaudRate() {
		return baudRate;
	}

	public SimpleStringProperty getBaudRateProperty() {
		baudRateProperty.set(baudRate);
		return baudRateProperty;
	}

	public String getClusterName() {
		return clusterName;
	}

	public SimpleStringProperty getClusterNameProperty() {
		clusterNameProperty.set(clusterName);
		return clusterNameProperty;
	}

	public String getBayName() {
		return bayName;
	}

	public SimpleStringProperty getBayNameProperty() {
		bayNameProperty.set(bayName);
		return bayNameProperty;
	}

	public String getPositionNo() {
		return positionNo;
	}

	public SimpleStringProperty getPositionNoProperty() {
		positionNoProperty.set(positionNo);
		return positionNoProperty;
	}

	public String getCanName() {
		return canName;
	}

	public SimpleStringProperty getcNameProperty() {
		cNameProperty.set(canName);
		return cNameProperty;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public SimpleStringProperty getDeviceIdProperty() {
		deviceIdProperty.set(deviceId);
		return deviceIdProperty;
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

	public void setDeviceTypeKey(String deviceType) {
		this.deviceTypeKey = deviceType;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public void setBaudRate(String baudRate) {
		this.baudRate = baudRate;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
		clusterNameProperty.set(clusterName);
	}

	public void setBayName(String bayName) {
		this.bayName = bayName;
		bayNameProperty.set(bayName);
	}

	public void setPositionNo(String positionNo) {
		this.positionNo = positionNo;
		positionNoProperty.set(positionNo);
	}

	public void setCanName(String cName) {
		this.canName = cName;
		cNameProperty.set(cName);
	}

	public void setDeviceId(String deviceId) {
		
		this.deviceId = deviceId;
		deviceIdProperty.set(deviceId);
	}

	public String getClusterId() {
		return clusterId;
	}

	public SimpleStringProperty getClusterIdProperty() {
		return clusterIdProperty;
	}

	public String getBayId() {
		return bayId;
	}

	public SimpleStringProperty getBayIdProperty() {
		return bayIdProperty;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public void setBayId(String bayId) {
		this.bayId = bayId;
	}

	public String getSerialStatus() {
		return serialStatus;
	}

	public SimpleStringProperty getSerialStatusProperty() {
		serialStatusProperty.set(serialStatus);
		return serialStatusProperty;
	}

	public String getSerialResponseData() {
		return serialResponseData;
	}

	public SimpleStringProperty getSerialResponseDataProperty() {
		serialResponseDataProperty.set(serialResponseData);
		return serialResponseDataProperty;
	}

	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}

	public void setSerialResponseData(String serialResponseData) {
		this.serialResponseData = serialResponseData;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public SimpleIntegerProperty getSerialNoProperty() {
		serialNoProperty.set(serialNo);
		return serialNoProperty;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getRs485Address() {
		return rs485Address;
	}

	public SimpleStringProperty getRs485AddressProperty() {
		rs485AddressProperty.set(rs485Address);
		return rs485AddressProperty;
	}

	public void setRs485Address(String rs485Address) {
		rs485AddressProperty.set(rs485Address);
		this.rs485Address = rs485Address;
	}





}


