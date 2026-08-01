package com.tasnetwork.spring.orm.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PalletManage")
public class PalletManage {

	public PalletManage() {
		
	}
/*	public PalletBayTest(PalletBayTest palletBayTest) {
		this = palletBayTest;
		palletBayTest.id=null;
	}*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;



	@CreatedBy
	private String userName="";
	//  private User user;


	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	

	
	
	@Column(columnDefinition = "INT(45)")
	private int palletBatchNo = 1;
	
	@Transient
	private SimpleIntegerProperty  palletBatchNoProperty = new SimpleIntegerProperty ();
	
	
	@Column( columnDefinition = "VARCHAR(3)") 
	@Type(type = "yes_no")
	private Boolean palletActive = true;
	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean testingPhase = false;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean exitAppeared = false;
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletQrId = "";

	
	@Transient
	private SimpleStringProperty  palletQrIdProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "INT(45)")
	int noOfMeterPresent = 0;

	
	@Transient
	private SimpleIntegerProperty  noOfMeterPresentProperty = new SimpleIntegerProperty ();
	
	@Column(columnDefinition = "INT(45)")
	int noOfMeterPassed = 0;

	
	@Transient
	private SimpleIntegerProperty  noOfMeterPassedProperty = new SimpleIntegerProperty ();
	
	@Column(columnDefinition = "INT(45)")
	int noOfMeterFailed = 0;

	
	@Transient
	private SimpleIntegerProperty  noOfMeterFailedProperty = new SimpleIntegerProperty ();
	
	@Column(columnDefinition = "INT(45)")
	int noOfMeterRejected = 0;

	
	@Transient
	private SimpleIntegerProperty  noOfMeterRejectedProperty = new SimpleIntegerProperty ();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletConvEntryTimeStampH = "";
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletConvEntryDateH = "";
	
	@Transient
	private SimpleStringProperty  palletConvEntryTimeStampH_Property = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletConvExitTimeStampH = "";
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletConvEntryTimeEpoch = "";
	

	@Column(columnDefinition = "VARCHAR(45)")
	String palletConvExitTimeEpoch = "";
	
	@Transient
	private SimpleStringProperty  palletConvExitTimeStampH_Property = new SimpleStringProperty ();
	
/*	@Column(columnDefinition = "VARCHAR(45)")
	String palletBatchMapId = "";
	
	@Transient
	private SimpleStringProperty  palletBatchMapIdProperty = new SimpleStringProperty ();*/
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletDistinctId = "";
	
	@Transient
	private SimpleStringProperty  palletDistinctIdProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String testType = "";
	
	@Transient
	private SimpleStringProperty  testTypeProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String presentBayKey = "";
	
	
/*    @OneToMany(mappedBy = "palletManage", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    List<PalletMeter> palletMeterList = new ArrayList<PalletMeter>();
    
    @OneToMany(mappedBy = "palletManage", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.LAZY)
    List<PalletBayState> palleteBayStateList = new ArrayList<PalletBayState>();
    //Set<PalletBayState> palleteBayStateList = new HashSet<>();
*/    
	
	@OneToMany(mappedBy = "palletManage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<PalletMeter> palletMeterList  = new HashSet<PalletMeter>();

	@OneToMany(mappedBy = "palletManage", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<PalletBayState> palleteBayStateList = new HashSet<PalletBayState>();
   
	
/*    @OneToMany(mappedBy = "palletManage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PalletMeterResults> palleteResultList = new HashSet<>();*/

	
	@Transient
	private SimpleStringProperty  presentBayKeyProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palleteConveyorRunTimeInMin = "";
	
	@Transient
	private SimpleStringProperty  palleteConveyorRunTimeInMinProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletExecutionStatus = "";
	
	@Transient
	private SimpleStringProperty  palletExecutionStatusProperty = new SimpleStringProperty ();
	
	@Column(columnDefinition = "VARCHAR(45)")
	String palletResultStatus = "";
	
	@Transient
	private SimpleStringProperty  palletResultStatusProperty = new SimpleStringProperty ();
	
	@Transient
	private int serialNo;

	@Transient
	private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();
	
	@Transient
	private String transitionErrorDetails = "";
	
	@Transient
	private SimpleStringProperty transitionErrorDetailsProperty = new SimpleStringProperty();
	
	
	
/*	public Set<PalletMeterResults> getPalleteResultList() {
        return palleteResultList;
    }

    public void setPalleteResultList(Set<PalletMeterResults> palleteResultList) {
        this.palleteResultList = palleteResultList;
    }
	*/

	public Integer getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public int getPalletBatchNo() {
		return palletBatchNo;
	}

	public SimpleIntegerProperty getPalletBatchNoProperty() {
		palletBatchNoProperty.set(palletBatchNo);
		return palletBatchNoProperty;
	}

	public String getPalletQrId() {
		return palletQrId;
	}

	public SimpleStringProperty getPalletQrIdProperty() {
		palletQrIdProperty.set(palletQrId);
		return palletQrIdProperty;
	}

	public String getPalletConvEntryTimeStampH() {
		return palletConvEntryTimeStampH;
	}

	public SimpleStringProperty getPalletConvEntryTimeStampH_Property() {
		palletConvEntryTimeStampH_Property.set(palletConvEntryTimeStampH);
		return palletConvEntryTimeStampH_Property;
	}

	public String getPalletConvExitTimeStampH() {
		return palletConvExitTimeStampH;
	}

	public SimpleStringProperty getPalletConvExitTimeStampH_Property() {
		palletConvExitTimeStampH_Property.set(palletConvExitTimeStampH);
		return palletConvExitTimeStampH_Property;
	}

/*	public String getPalletBatchMapId() {
		return palletBatchMapId;
	}

	public SimpleStringProperty getPalletBatchMapIdProperty() {
		palletBatchMapIdProperty.set(palletBatchMapId);
		return palletBatchMapIdProperty;
	}*/

	public String getTestType() {
		return testType;
	}

	public SimpleStringProperty getTestTypeProperty() {
		testTypeProperty.set(testType);
		return testTypeProperty;
	}

	public String getPresentBayKey() {
		return presentBayKey;
	}

	public SimpleStringProperty getPresentBayKeyProperty() {
		presentBayKeyProperty.set(presentBayKey);
		return presentBayKeyProperty;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setPalletBatchNo(int palletBatchNo) {
		this.palletBatchNo = palletBatchNo;
	}

	public void setPalletQrId(String palletQrId) {
		this.palletQrId = palletQrId;
	}

	public void setPalletConvEntryTimeStampH(String palletConvEntryTimeStamp) {
		this.palletConvEntryTimeStampH = palletConvEntryTimeStamp;
	}

	public void setPalletConvExitTimeStampH(String palletConvExitTimeStamp) {
		this.palletConvExitTimeStampH = palletConvExitTimeStamp;
	}
/*
	public void setPalletBatchMapId(String palletBatchMapId) {
		this.palletBatchMapId = palletBatchMapId;
	}*/

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public void setPresentBayKey(String bayType) {
		this.presentBayKey = bayType;
	}

	public String getPalletExecutionStatus() {
		return palletExecutionStatus;
	}

	public SimpleStringProperty getPalletExecutionStatusProperty() {
		palletExecutionStatusProperty.set(palletExecutionStatus);
		return palletExecutionStatusProperty;
	}

	public void setPalletExecutionStatus(String bayTestStatus) {
		
		this.palletExecutionStatus = bayTestStatus;
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

	public String getTransitionErrorDetails() {
		return transitionErrorDetails;
	}

	public SimpleStringProperty getTransitionErrorDetailsProperty() {
		transitionErrorDetailsProperty.set(transitionErrorDetails);
		return transitionErrorDetailsProperty;
	}

	public void setTransitionErrorDetails(String transitionErrorDetails) {
		this.transitionErrorDetails = transitionErrorDetails;
	}

	public String getPalletResultStatus() {

		
		return palletResultStatus;
	}

	public SimpleStringProperty getPalletResultStatusProperty() {
		palletResultStatusProperty.set(palletResultStatus);
		return palletResultStatusProperty;
	}

	public void setPalletResultStatus(String bayTestResultStatus) {
		this.palletResultStatus = bayTestResultStatus;
	}

/*	public List<PalletMeter> getPalletMeterList() {
		return palletMeterList;
	}

	public void setPalletMeterList(ArrayList<PalletMeter> palletMeterList) {
		//palletMeterList.forEach(PalletMeter::setPalletBayTestId);
		//palletMeterList.forEach(PalletMeter::);
		this.palletMeterList = palletMeterList;
	}*/

	public Boolean isPalletActive() {
		return palletActive;
	}

	public void setPalletActive(Boolean palletActive) {
		this.palletActive = palletActive;
	}

	public String getPalletConvEntryTimeEpoch() {
		return palletConvEntryTimeEpoch;
	}

	public String getPalletConvExitTimeEpoch() {
		return palletConvExitTimeEpoch;
	}

	public void setPalletConvEntryTimeEpoch(String palletConvEntryTimeEpoch) {
		this.palletConvEntryTimeEpoch = palletConvEntryTimeEpoch;
	}

	public void setPalletConvExitTimeEpoch(String palletConvExitTimeEpoch) {
		this.palletConvExitTimeEpoch = palletConvExitTimeEpoch;
	}

	public String getPalleteConveyorRunTimeInMin() {
		return palleteConveyorRunTimeInMin;
	}

	public SimpleStringProperty getPalleteConveyorRunTimeInMinProperty() {
		palleteConveyorRunTimeInMinProperty.set(palleteConveyorRunTimeInMin);
		return palleteConveyorRunTimeInMinProperty;
	}

	public void setPalleteConveyorRunTimeInMin(String palleteConveyorRunTimeInMin) {
		this.palleteConveyorRunTimeInMin = palleteConveyorRunTimeInMin;
	}

	public int getNoOfMeterPresent() {
		return noOfMeterPresent;
	}

	public SimpleIntegerProperty getNoOfMeterPresentProperty() {
		noOfMeterPresentProperty.set(noOfMeterPresent);
		return noOfMeterPresentProperty;
	}

	public void setNoOfMeterPresent(int noOfMeterPresent) {
		this.noOfMeterPresent = noOfMeterPresent;
	}

	public void setNoOfMeterPresentProperty(SimpleIntegerProperty noOfMeterPresentProperty) {
		this.noOfMeterPresentProperty = noOfMeterPresentProperty;
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

	public void setNoOfMeterPassed(int noOfMeterPassed) {
		this.noOfMeterPassed = noOfMeterPassed;
	}



	public void setNoOfMeterFailed(int noOfMeterFailed) {
		this.noOfMeterFailed = noOfMeterFailed;
	}



	public void setNoOfMeterRejected(int noOfMeterRejected) {
		this.noOfMeterRejected = noOfMeterRejected;
	}

	public Set<PalletMeter> getPalletMeterList() {
		return palletMeterList;
	}
	

	public void addPalletMeter(PalletMeter palletMeter) {
		palletMeterList.add(palletMeter);
		palletMeter.setPalletManage(this);
	}

	public Set<PalletBayState> getPalleteBayStateList() {
		return palleteBayStateList;
	}
	
	public void addPalleteBayState(PalletBayState palletBayState) {
		palleteBayStateList.add(palletBayState);
		palletBayState.setPalletManage(this);
	}

	public void setPalletMeterList(Set<PalletMeter> palletMeterList) {
		this.palletMeterList = palletMeterList;
		for(PalletMeter eachMeter :palletMeterList) {
			eachMeter.setPalletManage(this);
		}
	}

	public void setPalleteBayStateList(Set<PalletBayState> palleteBayStateList) {
		this.palleteBayStateList = palleteBayStateList;
		for(PalletBayState palletBayState: palleteBayStateList) {
			palletBayState.setPalletManage(this);
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

	public String getPalletConvEntryDateH() {
		return palletConvEntryDateH;
	}

	public void setPalletConvEntryDateH(String palletConvEntryDateH) {
		this.palletConvEntryDateH = palletConvEntryDateH;
	}

	public Boolean isExitAppeared() {
		return exitAppeared;
	}

	public void setExitAppeared(Boolean exitAppeared) {
		this.exitAppeared = exitAppeared;
	}

/*	public List<PalletBayState> getPalleteBayStateList() {
		return palleteBayStateList;
	}

	public void setPalleteBayStateList(List<PalletBayState> palleteBayStateList) {
		this.palleteBayStateList = palleteBayStateList;
	}*/

/*	public List<PalletBayState> getPalleteBayStateList() {
		return palleteBayStateList;
	}

	public void setPalleteBayStateList(List<PalletBayState> palleteBayStateList) {
		this.palleteBayStateList = palleteBayStateList;
	}*/


/*	public List<PalletBayState> getPalleteBayStateList() {
		return palleteBayStateList;
	}

	public void setPalleteBayStateList(List<PalletBayState> palleteBayStateList) {
		this.palleteBayStateList = palleteBayStateList;
	}*/
	
/*	 public void addPalletMeterList(PalletMeter inpPalletMeter){
	    	palletMeterList.add(inpPalletMeter);
			inpPalletMeter.setPalletBayTestId(this);
	    }*/
	
	
/*	ArrayList<PalletTimeLog> palletBayTimeLogList = new ArrayList<PalletTimeLog>();
	ArrayList<PalletResults> bayTestResultsList = new ArrayList<PalletResults>();
	*/
	
}
