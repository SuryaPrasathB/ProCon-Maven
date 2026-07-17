package com.tasnetwork.spring.orm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "AppConfig")
public class AppConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean active = false;
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String customerId = "0"; // energy meter or Fan
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String accountId = "0";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String factoryCode = "0";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String lineNo = "1";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String propertyName = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String propertyValue = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String propertyType = "boolean";

	public Long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Boolean getActive() {
		return active;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getFactoryCode() {
		return factoryCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
}
