package com.tasnetwork.spring.orm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


@Entity
@Table(name = "PalletMeter")
public class PalletMeter {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	String meterSerialNo = "XY";
	
	@Column(columnDefinition = "VARCHAR(45)")
	String hardwareId = ConstantConveyor.DEFAULT_DUT_HARDWARE_ID;//"YZ";
	
	@Transient
	private SimpleStringProperty meterSerialNoProperty = new SimpleStringProperty();
	
	@Transient
	private SimpleStringProperty hardwareIdProperty = new SimpleStringProperty();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	private int rackPositionNo = 1;
	@Transient
	private SimpleIntegerProperty rackPositionNoProperty = new SimpleIntegerProperty ();
	
/*	@Column(columnDefinition = "INT(45)")
	int positionNo = 0;*/
	
	@Column(columnDefinition = "VARCHAR(45)")
	String meterProfileName ="";
	
 	@ManyToOne(fetch = FetchType.LAZY)
 	@JoinColumn(name = "palletManageId")
 	private PalletManage palletManage;
 	
 	@OneToMany(mappedBy = "palletMeter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
 	private Set<PalletMeterResults> palletMeterResultsList = new HashSet<>();
 	
 	
/* 	@OneToMany(mappedBy = "palletMeter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
 	private Set<PalletMeterResults> palletMeterArchivedResultsList = new HashSet<>();*/

 	@Column(columnDefinition = "VARCHAR(45)")
	String overAllTestResultStatus = "";
 	
 	@Transient
 	private SimpleStringProperty overAllTestResultStatusProperty = new SimpleStringProperty ();
 	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletDistinctId = "";
	

	
	@Transient
	private SimpleStringProperty palletDistinctIdProperty = new SimpleStringProperty ();
 	
	// New column for error code
	@Column(columnDefinition = "VARCHAR(300)")
	String errorCode = "";

	@Transient
	private SimpleStringProperty errorCodeProperty = new SimpleStringProperty();
	
	@Transient
	private int serialNo;
	
	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();

	public Integer getId() {
		return id;
	}

	public String getMeterSerialNo() {
		return meterSerialNo;
	}
	
	public String getHardwareId() {
		return hardwareId;
	}

	public String getMeterProfileName() {
		return meterProfileName;
	}

	public String getOverAllTestResultStatus() {
		return overAllTestResultStatus;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMeterSerialNo(String meterSerialNo) {
		this.meterSerialNo = meterSerialNo;
	}
	
	public void setHardwareId(String hardwareIdNo) {
		this.hardwareId = hardwareIdNo;
	}
	

	public void setMeterProfileName(String meterProfileName) {
		this.meterProfileName = meterProfileName;
	}


	public void setOverAllTestResultStatus(String overAllTestResultStatus) {
		this.overAllTestResultStatus = overAllTestResultStatus;
	}

	public PalletManage getPalletManage() {
		return palletManage;
	}

	public void setPalletManage(PalletManage palletBayTest) {
		this.palletManage = palletBayTest;
	}

	public Set<PalletMeterResults> getPalletMeterResultsList() {
		return palletMeterResultsList;
	}
	
	public int addPalletMeterResults(PalletMeterResults palletMeterResult) {
		
		palletMeterResultsList.add(palletMeterResult);
		palletMeterResult.setPalletMeter(this);
		int getId = palletMeterResult.getPalletMeter().getId();
		return getId;
	}

	public void setPalletMeterResultsList(Set<PalletMeterResults> palletMeterResultsList) {
		this.palletMeterResultsList = palletMeterResultsList;
		
		for(PalletMeterResults palletMeterResults: palletMeterResultsList) {
			palletMeterResults.setPalletMeter(this);
		}
	}
	
	public String getPalletDistinctId() {
		return palletDistinctId;
	}

	public SimpleStringProperty getPalletDistinctIdProperty() {
		palletDistinctIdProperty.set(palletDistinctId);
		return palletDistinctIdProperty;
	}

	public void setPalletDistinctId(String palletDistinctId) {
		this.palletDistinctId = palletDistinctId;
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

	public SimpleStringProperty getMeterSerialNoProperty() {
		meterSerialNoProperty.set(meterSerialNo);
		return meterSerialNoProperty;
	}
	
	public SimpleStringProperty gethardwareIdProperty() {
		hardwareIdProperty.set(hardwareId);
		return hardwareIdProperty;
	}

	public int getRackPositionNo() {
		return rackPositionNo;
	}

	public SimpleIntegerProperty getRackPositionNoProperty() {
		rackPositionNoProperty.set(rackPositionNo);
		return rackPositionNoProperty;
	}

	public void setRackPositionNo(int rackPositionNo) {
		this.rackPositionNo = rackPositionNo;
	}

	public SimpleStringProperty getOverAllTestResultStatusProperty() {
		overAllTestResultStatusProperty.set(overAllTestResultStatus);
		return overAllTestResultStatusProperty;
	}

	// Getter for errorCode
	public String getErrorCode() {
		return errorCode;
	}

	// Setter for errorCode
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	// Getter for errorCodeProperty (JavaFX binding)
	public SimpleStringProperty getErrorCodeProperty() {
		errorCodeProperty.set(errorCode);
		return errorCodeProperty;
	}

	public void setMeterSerialNoProperty(SimpleStringProperty meterSerialNoProperty) {
		this.meterSerialNoProperty = meterSerialNoProperty;
	}
	
	public void setHardwareIdProperty(SimpleStringProperty hardwareIdNoProperty) {
		this.hardwareIdProperty = hardwareIdNoProperty;
	}

	public void setRackPositionNoProperty(SimpleIntegerProperty rackPositionNoProperty) {
		this.rackPositionNoProperty = rackPositionNoProperty;
	}

	public void setOverAllTestResultStatusProperty(SimpleStringProperty overAllTestResultStatusProperty) {
		this.overAllTestResultStatusProperty = overAllTestResultStatusProperty;
	}

	public void setPalletDistinctIdProperty(SimpleStringProperty palletDistinctIdProperty) {
		this.palletDistinctIdProperty = palletDistinctIdProperty;
	}

	public void setErrorCodeProperty(SimpleStringProperty errorCodeProperty) {
		this.errorCodeProperty = errorCodeProperty;
	}

	public void setSerialNoProperty(SimpleIntegerProperty serialNoProperty) {
		this.serialNoProperty = serialNoProperty;
	}
}
