package com.tasnetwork.spring.orm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "RpTestFilterOperationProcess")
public class OperationProcess {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ReportProfileTestDataFilter testDataFilter1;
    
    
	
    @Column(columnDefinition = "VARCHAR(100)") 
	private String operationProcessKey;
    @Column(columnDefinition = "VARCHAR(45)") 
	private String resultValue;
    @Column(columnDefinition = "VARCHAR(45)") 
	private String localComparedStatus;
    
    @Column(columnDefinition = "VARCHAR(45)") 
	private String masterComparedStatus;
    
    @Column(columnDefinition = "VARCHAR(45)") 
	private String upperLimit;
    @Column(columnDefinition = "VARCHAR(45)") 
	private String lowerLimit;
    
    @Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessDataType;
    
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean populateOnlyHeaders;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean addedToInputProcess;
	
	
	public String getOperationProcessKey() {
		return operationProcessKey;
	}
	public void setOperationProcessKey(String operationProcessKey) {
		this.operationProcessKey = operationProcessKey;
	}
	public String getResultValue() {
		return resultValue;
	}
	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	public String getLocalComparedStatus() {
		return localComparedStatus;
	}
	public void setLocalComparedStatus(String comparedStatus) {
		this.localComparedStatus = comparedStatus;
	}
	public String getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public Boolean isPopulateOnlyHeaders() {
		return populateOnlyHeaders;
	}
	public void setPopulateOnlyHeaders(Boolean populateOnlyHeaders) {
		this.populateOnlyHeaders = populateOnlyHeaders;
	}
	public String getOperationProcessDataType() {
		return operationProcessDataType;
	}
	public void setOperationProcessDataType(String operationProcessDataType) {
		this.operationProcessDataType = operationProcessDataType;
	}
	public ReportProfileTestDataFilter getTestDataFilter() {
		return testDataFilter1;
	}
	public void setTestDataFilter(ReportProfileTestDataFilter testDataFilter) {
		this.testDataFilter1 = testDataFilter;
	}
	public Boolean isAddedToInputProcess() {
		return addedToInputProcess;
	}
	public void setAddedToInputProcess(Boolean addedToInputProcess) {
		this.addedToInputProcess = addedToInputProcess;
	}
	public String getMasterComparedStatus() {
		return masterComparedStatus;
	}
	public void setMasterComparedStatus(String masterComparedStatus) {
		this.masterComparedStatus = masterComparedStatus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
