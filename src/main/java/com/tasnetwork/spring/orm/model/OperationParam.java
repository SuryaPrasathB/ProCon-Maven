package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "RpOperationParamProfile")
public class OperationParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean populateOnlyHeaders = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean populateAllDut = false;
    
	
	// Marked for delete
	/*@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean resultTypeAverage = false;*/
	
    @Column(columnDefinition = "VARCHAR(45)") 
	private String operationParamProfileName;
    @Column(columnDefinition = "VARCHAR(45)") 
	private String keyParam = "";
    @Column(columnDefinition = "VARCHAR(45)") 
	private String paramType;
    @Column(columnDefinition = "VARCHAR(45)") 
	private String tableSerialNo;
    
    @Transient
    private SimpleStringProperty tableSerialNoProperty = new SimpleStringProperty();

    @Transient
    private SimpleStringProperty keyParamProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String customerId;
    
/*    @Transient
    private boolean  populateAllDut;*/
    
    
    @Transient
	private String populateType  = "";//new ArrayList<String>();


	public Boolean isPopulateOnlyHeaders() {
		return populateOnlyHeaders;
	}


	public void setPopulateOnlyHeaders(Boolean populateOnlyHeaders) {
		this.populateOnlyHeaders = populateOnlyHeaders;
	}


	public String getOperationParamProfileName() {
		return operationParamProfileName;
	}


	public void setOperationParamProfileName(String operationParamProfileName) {
		this.operationParamProfileName = operationParamProfileName;
	}


	public String getKeyParam() {
		return keyParam;
	}


	public void setKeyParam(String keyParam) {
		this.keyParam = keyParam;
	}


/*	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}*/


	public String getTableSerialNo() {
		return tableSerialNo;
	}


	public void setTableSerialNo(String serialNo) {
		this.tableSerialNo = serialNo;
	}


	public boolean isPopulateAllDut() {
		return populateAllDut;
	}


	public void setPopulateAllDut(boolean populateAllDut) {
		this.populateAllDut = populateAllDut;
	}


/*	public ArrayList<String> getPopulateType() {
		return populateType;
	}


	public void setPopulateType(ArrayList<String> populateType) {
		this.populateType = populateType;
	}*/
	
	public String getPopulateType() {
		return populateType;
	}


	public void setPopulateType(String populateType) {
		this.populateType = populateType;
	}

    
    public SimpleStringProperty getTableSerialNoProperty() {
    	tableSerialNoProperty.set(tableSerialNo);
		return tableSerialNoProperty;
	}


	public void setTableSerialNoProperty(SimpleStringProperty tableSerialNoProperty) {
		this.tableSerialNoProperty = tableSerialNoProperty;
	}


	public SimpleStringProperty getKeyParamProperty() {
		
		keyParamProperty.set(keyParam);
		return keyParamProperty;
	}


	public void setKeyParamProperty(SimpleStringProperty keyParamProperty) {
		this.keyParamProperty = keyParamProperty;
	}


	public String getParamType() {
		return paramType;
	}


	public void setParamType(String paramType) {
		this.paramType = paramType;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


/*	public Boolean isResultTypeAverage() {
		return resultTypeAverage;
	}


	public void setResultTypeAverage(Boolean resultTypeAverage) {
		this.resultTypeAverage = resultTypeAverage;
	}*/
    
}
