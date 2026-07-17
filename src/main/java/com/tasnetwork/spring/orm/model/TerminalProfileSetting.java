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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "terminal_profile_setting")
//@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class TerminalProfileSetting {

	
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
	private Boolean bayActive= true;

	@Transient
	private SimpleBooleanProperty bayActiveProperty = new SimpleBooleanProperty();
	
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String bayName;

	@Transient
	private SimpleStringProperty bayNameProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String bayId;

	@Transient
	private SimpleStringProperty bayIdProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String clusterName;

	@Transient
	private SimpleStringProperty clusterNameProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String clusterId;

	@Transient
	private SimpleStringProperty clusterIdProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String totalNoOfPositions;

	@Transient
	private SimpleStringProperty totalNoOfPositionsProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String positionIdAsList="";

	@Transient
	private SimpleStringProperty positionIdAsListProperty = new SimpleStringProperty();
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String positionTobeSkippedAsList="";

	@Transient
	private SimpleStringProperty positionToBeSkippedAsListProperty = new SimpleStringProperty();

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
		String formatedTerminalId = String.format("%02d", Integer.parseInt(terminalId));
		return formatedTerminalId;
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

	public String getBayId() {
		String formattedBayId = String.format("%02d", Integer.parseInt(bayId));
		return formattedBayId;
	}

	public SimpleStringProperty getBayIdProperty() {
		bayIdProperty.set(bayId);
		return bayIdProperty;
	}

	public String getClusterName() {
		return clusterName;
	}

	public SimpleStringProperty getClusterNameProperty() {
		clusterNameProperty.set(clusterName);
		return clusterNameProperty;
	}

	public String getClusterId() {
		String formattedClusterId = String.format("%02d", Integer.parseInt(clusterId));
		return formattedClusterId;
	}

	public SimpleStringProperty getClusterIdProperty() {
		clusterIdProperty.set(clusterId);
		return clusterIdProperty;
	}

	public String getTotalNoOfPositions() {
		return totalNoOfPositions;
	}

	public SimpleStringProperty getTotalNoOfPositionsProperty() {
		totalNoOfPositionsProperty.set(totalNoOfPositions);
		return totalNoOfPositionsProperty;
	}

	public String getPositionIdAsList() {
		return positionIdAsList;
	}

	public SimpleStringProperty getPositionIdAsListProperty() {
		positionIdAsListProperty.set(positionIdAsList);
		return positionIdAsListProperty;
	}

	public String getPositionTobeSkippedAsList() {
		return positionTobeSkippedAsList;
	}

	public SimpleStringProperty getPositionToBeSkippedAsListProperty() {
		positionToBeSkippedAsListProperty.set(positionTobeSkippedAsList);
		return positionToBeSkippedAsListProperty;
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

	public void setBayId(String bayId) {
		this.bayId = bayId;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public void setTotalNoOfPositions(String totalNoOfPositions) {
		this.totalNoOfPositions = totalNoOfPositions;
	}

	public void setPositionIdAsList(String positionId) {
		this.positionIdAsList = positionId;
	}

	public void setPositionTobeSkippedAsList(String positionTobeSkipped) {
		this.positionTobeSkippedAsList = positionTobeSkipped;
	}

	public Boolean isBayActive() {
		return bayActive;
	}

	public SimpleBooleanProperty getBayActiveProperty() {
		bayActiveProperty.set(bayActive);
		return bayActiveProperty;
	}

	public void setBayActive(Boolean bayActive) {
		this.bayActive = bayActive;
	}
}
