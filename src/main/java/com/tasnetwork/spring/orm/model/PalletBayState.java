package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;
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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "PalletBayState")
public class PalletBayState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Transient
	private int serialNo;

	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();

	/*
	 * @Column(columnDefinition = "VARCHAR(45)")
	 * String meterSerialNo = "";
	 * 
	 * @Column(columnDefinition = "INT(45)")
	 * int positionNo = 0;
	 * 
	 * @Column(columnDefinition = "VARCHAR(45)")
	 * String meterProfileName ="";
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "palletManageId")
	private PalletManage palletManage;

	/*
	 * @OneToMany(mappedBy = "palletBayState", fetch = FetchType.LAZY, cascade =
	 * CascadeType.ALL)
	 * private Set<PalletMeterResults> palletResults = new HashSet<>();
	 */

	/*
	 * @OneToMany(mappedBy = "palletBayState", cascade = CascadeType.ALL, fetch =
	 * FetchType.LAZY)
	 * private Set<PalletMeterResults> palletResultsList = new HashSet<>();
	 */

	@Column(columnDefinition = "VARCHAR(45)")
	private int palletBatchNo = 1;
	@Transient
	private SimpleIntegerProperty palletBatchNoProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String palletQrId = "";
	@Transient
	private SimpleStringProperty palletQrIdProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	private String bayStateKey = "";

	@Transient
	private SimpleStringProperty bayStateKeyProperty = new SimpleStringProperty();

	/*
	 * @Column(columnDefinition = "VARCHAR(45)")
	 * String palletBatchMapId = "";
	 * 
	 * @Transient
	 * private SimpleStringProperty palletBatchMapIdProperty = new
	 * SimpleStringProperty ();
	 */

	@Column(columnDefinition = "VARCHAR(45)")
	String palletDistinctId = "";

	@Transient
	private SimpleStringProperty palletDistinctIdProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	String bayExecutionStatus = "";

	@Transient
	private SimpleStringProperty bayExecutionStatusProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	String bayResultStatus = "";

	@Transient
	private SimpleStringProperty bayResultStatusProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(3)")
	@org.hibernate.annotations.Type(type = "yes_no")
	private Boolean testCompleted = false;

	@Transient
	private javafx.beans.property.SimpleBooleanProperty testCompletedProperty = new javafx.beans.property.SimpleBooleanProperty();

	@Column(columnDefinition = "INT(45)")
	int noOfMeterPresent = 0;

	@Transient
	private SimpleIntegerProperty noOfMeterPresentProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "INT(45)")
	int noOfMeterPassed = 0;

	@Transient
	private SimpleIntegerProperty noOfMeterPassedProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "INT(45)")
	int noOfMeterFailed = 0;

	@Transient
	private SimpleIntegerProperty noOfMeterFailedProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "INT(45)")
	int noOfMeterRejected = 0;

	@Transient
	private SimpleIntegerProperty noOfMeterRejectedProperty = new SimpleIntegerProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	String palleteBayRunTimeInMin = "";

	@Transient
	private SimpleStringProperty palleteBayRunTimeInMinProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	String palletBayEntryTimeStampH = "";

	@Transient
	private SimpleStringProperty palletBayEntryTimeStampH_Property = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)")
	String palletBayExitTimeStampH = "";

	@Column(columnDefinition = "VARCHAR(45)")
	String palletBayEntryTimeEpoch = "";

	@Column(columnDefinition = "VARCHAR(45)")
	String palletBayExitTimeEpoch = "";

	@Transient
	private SimpleStringProperty palletBayExitTimeStampH_Property = new SimpleStringProperty();

	// String testPointId = "";
	// String testType ="";
	// ArrayList<PalletResults> bayTestStatus = new ArrayList<PalletResults>();

	/*
	 * @Column(columnDefinition = "VARCHAR(45)")
	 * String overAllTestResultStatus = "";
	 */

	/*
	 * public Set<PalletMeterResults> getPalletResults() {
	 * return palletResults;
	 * }
	 * 
	 * public void setPalletResults(Set<PalletMeterResults> palletResults) {
	 * this.palletResults = palletResults;
	 * }
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public PalletManage getPalletManage() {
		return palletManage;
	}

	public void setPalletManage(PalletManage palletManage) {
		this.palletManage = palletManage;
	}

	public int getPalletBatchNo() {
		return palletBatchNo;
	}

	public SimpleIntegerProperty getPalletBatchNoProperty() {
		palletBatchNoProperty.set(palletBatchNo);
		return palletBatchNoProperty;
	}

	public String getPalletQrId() {
		// palletQrId = this.palletManage.getPalletQrId();
		return palletQrId;
	}

	public SimpleStringProperty getPalletQrIdProperty() {
		palletQrIdProperty.set(palletQrId);
		return palletQrIdProperty;
	}

	public String getBayStateKey() {
		// presentBayKey = this.palletManage.getPresentBayKey();
		return bayStateKey;
	}

	public SimpleStringProperty getBayStateKeyProperty() {
		bayStateKeyProperty.set(bayStateKey);
		return bayStateKeyProperty;
	}

	public void setPalletBatchNo(int palletBatchNo) {
		this.palletBatchNo = palletBatchNo;
	}

	public void setPalletQrId(String palletQrId) {
		this.palletQrId = palletQrId;
	}

	public void setBayStateKey(String presentBayKey) {
		this.bayStateKey = presentBayKey;
	}

	/*
	 * public void setPalletBatchMapId(String palletBatchMapId) {
	 * this.palletBatchMapId = palletBatchMapId;
	 * }
	 */

	public String getPalleteBayRunTimeInMin() {
		return palleteBayRunTimeInMin;
	}

	public SimpleStringProperty getPalleteBayRunTimeInMinProperty() {
		palleteBayRunTimeInMinProperty.set(palleteBayRunTimeInMin);
		return palleteBayRunTimeInMinProperty;
	}

	public String getPalletBayEntryTimeStampH() {
		return palletBayEntryTimeStampH;
	}

	public SimpleStringProperty getPalletBayEntryTimeStampH_Property() {
		palletBayEntryTimeStampH_Property.set(palletBayEntryTimeStampH);
		return palletBayEntryTimeStampH_Property;
	}

	public String getPalletBayExitTimeStampH() {
		return palletBayExitTimeStampH;
	}

	public String getPalletBayEntryTimeEpoch() {
		return palletBayEntryTimeEpoch;
	}

	public long getNormalizedEntryTimeMilli() {
		if (palletBayEntryTimeEpoch == null || palletBayEntryTimeEpoch.isEmpty())
			return 0;
		long val = Long.parseLong(palletBayEntryTimeEpoch);
		return palletBayEntryTimeEpoch.length() <= 10 ? val * 1000 : val;
	}

	public String getPalletBayExitTimeEpoch() {
		return palletBayExitTimeEpoch;
	}

	public long getNormalizedExitTimeMilli() {
		if (palletBayExitTimeEpoch == null || palletBayExitTimeEpoch.isEmpty())
			return 0;
		long val = Long.parseLong(palletBayExitTimeEpoch);
		return palletBayExitTimeEpoch.length() <= 10 ? val * 1000 : val;
	}

	public SimpleStringProperty getPalletBayExitTimeStampH_Property() {
		palletBayExitTimeStampH_Property.set(palletBayExitTimeStampH);
		return palletBayExitTimeStampH_Property;
	}

	public void setPalleteBayRunTimeInMin(String palleteBayRunTimeInMin) {
		this.palleteBayRunTimeInMin = palleteBayRunTimeInMin;
	}

	public void setPalletBayEntryTimeStampH(String palletBayEntryTimeStampH) {
		this.palletBayEntryTimeStampH = palletBayEntryTimeStampH;
	}

	public void setPalletBayExitTimeStampH(String palletBayExitTimeStampH) {
		this.palletBayExitTimeStampH = palletBayExitTimeStampH;
	}

	public void setPalletBayEntryTimeEpoch(String palletBayEntryTimeEpoch) {
		this.palletBayEntryTimeEpoch = palletBayEntryTimeEpoch;
	}

	public void setPalletBayExitTimeEpoch(String palletBayExitTimeEpoch) {
		this.palletBayExitTimeEpoch = palletBayExitTimeEpoch;
	}

	public String getBayExecutionStatus() {
		return bayExecutionStatus;
	}

	public SimpleStringProperty getBayExecutionStatusProperty() {
		bayExecutionStatusProperty.set(bayExecutionStatus);
		return bayExecutionStatusProperty;
	}

	public String getBayResultStatus() {
		return bayResultStatus;
	}

	public SimpleStringProperty getBayResultStatusProperty() {
		bayResultStatusProperty.set(bayResultStatus);
		return bayResultStatusProperty;
	}

	public Boolean isTestCompleted() {
		return testCompleted;
	}

	public Boolean getTestCompleted() {
		return testCompleted;
	}

	public void setTestCompleted(Boolean testCompleted) {
		this.testCompleted = testCompleted;
	}

	public void setTestCompleted(String testCompleted) {
		this.testCompleted = "Y".equalsIgnoreCase(testCompleted) || "true".equalsIgnoreCase(testCompleted);
	}

	public javafx.beans.property.SimpleBooleanProperty getTestCompletedProperty() {
		testCompletedProperty.set(testCompleted != null ? testCompleted : false);
		return testCompletedProperty;
	}

	public int getNoOfMeterPresent() {
		return noOfMeterPresent;
	}

	public SimpleIntegerProperty getNoOfMeterPresentProperty() {
		noOfMeterPresentProperty.set(noOfMeterPresent);
		return noOfMeterPresentProperty;
	}

	public int getNoOfMeterPassed() {
		return noOfMeterPassed;
	}

	public SimpleIntegerProperty getNoOfMeterPassedProperty() {
		noOfMeterPassedProperty.set(noOfMeterPassed);
		return noOfMeterPassedProperty;
	}

	public int getNoOfMeterFailed() {
		return noOfMeterFailed;
	}

	public SimpleIntegerProperty getNoOfMeterFailedProperty() {
		noOfMeterFailedProperty.set(noOfMeterFailed);
		return noOfMeterFailedProperty;
	}

	public int getNoOfMeterRejected() {
		return noOfMeterRejected;
	}

	public SimpleIntegerProperty getNoOfMeterRejectedProperty() {
		noOfMeterRejectedProperty.set(noOfMeterRejected);
		return noOfMeterRejectedProperty;
	}

	public void setBayExecutionStatus(String bayExecutionStatus) {
		this.bayExecutionStatus = bayExecutionStatus;
	}

	public void setBayResultStatus(String bayResultStatus) {
		this.bayResultStatus = bayResultStatus;
	}

	public void setNoOfMeterPresent(int noOfMeterPresent) {
		this.noOfMeterPresent = noOfMeterPresent;
	}

	public void setNoOfMeterPassed(int noOfMeterPassed) {
		this.noOfMeterPassed = noOfMeterPassed;
	}

	public void setNoOfMeterFailed(int noOfMeterFailed) {
		this.noOfMeterFailed = noOfMeterFailed;
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

	/*
	 * public PalletManage getPalletManage() {
	 * return palletManage;
	 * }
	 * 
	 * public void setPalletManage(PalletManage palletBayTest) {
	 * this.palletManage = palletBayTest;
	 * }
	 */

}
