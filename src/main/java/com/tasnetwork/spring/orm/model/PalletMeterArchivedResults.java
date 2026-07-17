package com.tasnetwork.spring.orm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PalletArchivedResults")
public class PalletMeterArchivedResults {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date originalCreatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date originalUpdatedAt;
	

	/*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palletManageId", nullable = false)
    private PalletManage palletManage;*/

	/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palletBayStateId", nullable = false)
    private PalletBayState palletBayState;*/

	//@ManyToOne(fetch = FetchType.LAZY)
	//@Cascade(CascadeType.SAVE_UPDATE)
	//@JoinColumn(name = "palletMeterId", nullable = false)
	//private PalletMeter palletMeter = new PalletMeter();
	
	
/*	@Column( columnDefinition = "VARCHAR(3)" , nullable = false) 
	@Type(type = "yes_no")
	private Boolean resultActive = true;*/

	@Column(columnDefinition = "VARCHAR(45)")
	private String bayStateKey = "";

	@Transient
	private SimpleStringProperty  bayStateKeyProperty = new SimpleStringProperty ();


	/*    @Column(columnDefinition = "VARCHAR(45)")
    String palletBatchMapId = "";
    @Transient
	private SimpleStringProperty  palletBatchMapIdProperty = new SimpleStringProperty ();*/

	@Column(columnDefinition = "VARCHAR(45)")
	String palletDistinctId = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false)
	String sourceName = "";

	@Transient
	private SimpleStringProperty  palletDistinctIdProperty = new SimpleStringProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private int palletBatchNo = 1;
	@Transient
	private SimpleIntegerProperty  palletBatchNoProperty = new SimpleIntegerProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private int rackPositionNo = 1;
	@Transient
	private SimpleIntegerProperty  rackPositionNoProperty = new SimpleIntegerProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private String testType = "";
	@Transient
	private SimpleStringProperty  testTypeProperty = new SimpleStringProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private String testCaseName = "";
	@Transient
	private SimpleStringProperty  testCaseNameProperty = new SimpleStringProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private String resultValue = "";
	@Transient
	private SimpleStringProperty  resultValueProperty = new SimpleStringProperty ();

	@Column(columnDefinition = "VARCHAR(45)")
	private String resultStatus = "";
	@Transient
	private SimpleStringProperty  resultStatusProperty = new SimpleStringProperty ();


	@Column(columnDefinition = "VARCHAR(45)")
	private String meterSerialNo = "";
	@Transient
	private SimpleStringProperty  meterSerialNoProperty = new SimpleStringProperty ();

	@Column(columnDefinition = "VARCHAR(45)", nullable = true)
	private String permissibleUpperLimit = "";


	@Column(columnDefinition = "VARCHAR(45)", nullable = true)
	private String permissibleLowerLimit = "";
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = true) 
	@Type(type = "yes_no")
	private Boolean resultSummary = false;
	
	@Transient
	private SimpleStringProperty  permissibleLimitDisplayProperty = new SimpleStringProperty ();



	@Column(columnDefinition = "VARCHAR(45)")
	private String palletQrId = "";
	@Transient
	private SimpleStringProperty  palletQrIdProperty = new SimpleStringProperty ();

	@Transient
	private int serialNo;

	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*    public void addMeterResults(PalletMeterResults palletMeterResult) {

		this.add(palletMeterResult);
		palletMeterResult.setPalletMeter(this);
	}*/

	/*    public PalletManage getPalletManage() {
        return palletManage;
    }

    public void setPalletManage(PalletManage palletManage) {
        this.palletManage = palletManage;
    }

    public PalletBayState getPalletBayState() {
        return palletBayState;
    }

    public void setPalletBayState(PalletBayState palletBayState) {
        this.palletBayState = palletBayState;
    }*/

/*	public PalletMeter getPalletMeter() {
		return palletMeter;
	}

	public void setPalletMeter(PalletMeter palletMeter) {
		this.palletMeter = palletMeter;
	}*/

	public String getBayStateKey() {
		//presentBayKey = this.palletManage.getPresentBayKey();
		return bayStateKey;
	}



	public SimpleStringProperty getBayStateKeyProperty() {
		bayStateKeyProperty.set(bayStateKey);
		return bayStateKeyProperty;
	}

	public void setBayStateKey(String presentBayKey) {
		this.bayStateKey = presentBayKey;
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

	public int getPalletBatchNo() {
		return palletBatchNo;
	}



	public SimpleIntegerProperty getPalletBatchNoProperty() {
		palletBatchNoProperty.set(palletBatchNo);
		return palletBatchNoProperty;
	}



	public String getPalletQrId() {
		//palletQrId = this.palletManage.getPalletQrId();
		return palletQrId;
	}



	public SimpleStringProperty getPalletQrIdProperty() {
		palletQrIdProperty.set(palletQrId);
		return palletQrIdProperty;
	}


	public void setPalletBatchNo(int palletBatchNo) {
		this.palletBatchNo = palletBatchNo;
	}



	public void setPalletQrId(String palletQrId) {
		this.palletQrId = palletQrId;
	}

	/*	public String getPalletBatchMapId() {

		//palletBatchMapId = this.palletManage.getPalletBatchMapId();
		return palletBatchMapId;
	}

	public void setPalletBatchMapId(String palletBatchMapId) {
		this.palletBatchMapId = palletBatchMapId;
	}

	public SimpleStringProperty getPalletBatchMapIdProperty() {
		palletBatchMapIdProperty.set(palletBatchMapId);
		return palletBatchMapIdProperty;
	}*/

	public int getRackPositionNo() {
		return rackPositionNo;
	}

	public SimpleIntegerProperty getRackPositionNoProperty() {
		rackPositionNoProperty.set(rackPositionNo);
		return rackPositionNoProperty;
	}

	public String getTestType() {
		return testType;
	}

	public SimpleStringProperty getTestTypeProperty() {
		testTypeProperty.set(testType);
		return testTypeProperty;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public SimpleStringProperty getTestCaseNameProperty() {
		testCaseNameProperty.set(testCaseName);
		return testCaseNameProperty;
	}

	public String getResultValue() {
		return resultValue;
	}

	public SimpleStringProperty getResultValueProperty() {
		resultValueProperty.set(resultValue);
		return resultValueProperty;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public SimpleStringProperty getResultStatusProperty() {
		resultStatusProperty.set(resultStatus);
		return resultStatusProperty;
	}

	public void setRackPositionNo(int palletPositionNo) {
		this.rackPositionNo = palletPositionNo;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getMeterSerialNo() {
		return meterSerialNo;
	}

	public void setMeterSerialNo(String meterSerialNo) {
		this.meterSerialNo = meterSerialNo;
	}

	public SimpleStringProperty getMeterSerialNoProperty() {
		meterSerialNoProperty.set(meterSerialNo);
		return this.meterSerialNoProperty;
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

	public String getPermissibleUpperLimit() {
		return permissibleUpperLimit;
	}

	public String getPermissibleLowerLimit() {
		return permissibleLowerLimit;
	}


	public void setPermissibleUpperLimit(String permissibleUpperLimit) {
		this.permissibleUpperLimit = permissibleUpperLimit;
	}

	public void setPermissibleLowerLimit(String permissibleLowerLimit) {
		this.permissibleLowerLimit = permissibleLowerLimit;
	}

	public void setPermissibleLimitDisplayProperty(SimpleStringProperty permissibleLimitProperty) {
		this.permissibleLimitDisplayProperty = permissibleLimitProperty;
	}

	public Boolean getResultSummary() {
		return resultSummary;
	}

	public void setResultSummary(Boolean resultSummary) {
		this.resultSummary = resultSummary;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Date getOriginalCreatedAt() {
		return originalCreatedAt;
	}

	public Date getOriginalUpdatedAt() {
		return originalUpdatedAt;
	}

	public void setOriginalCreatedAt(Date originalCreatedAt) {
		this.originalCreatedAt = originalCreatedAt;
	}

	public void setOriginalUpdatedAt(Date originalUpdatedAt) {
		this.originalUpdatedAt = originalUpdatedAt;
	}

/*	public Boolean getResultActive() {
		return resultActive;
	}

	public void setResultActive(Boolean resultActive) {
		this.resultActive = resultActive;
	}*/

}
