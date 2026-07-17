package com.tasnetwork.spring.orm.model;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "conveyor_output_metrics")
public class ConveyorOutputMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column( columnDefinition = "VARCHAR(45)") 
    private String customerName;

    //@Temporal(TemporalType.TIMESTAMP)
	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
    private Date createdAt;
	
	@Column(name = "date_h", nullable = false)
    @Temporal(TemporalType.DATE)  // Stores only date part
    private Date dateH;

	@Column()
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    private Integer palletOutput=1;

    private Integer totalNoOfMeters;

    private Integer passedMeters;

    private Integer failedMeters;
    
    @Column( columnDefinition = "VARCHAR(45)") 
    private String bayType;
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String palletDistinctId="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String locationName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String departmentName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String plantName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String lineNo="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String userName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String palletQrId="";
    
    private Double averageHourlyOutput;
    
    // Add Getters and Setters for all fields
    
    @PrePersist
    protected void initializeDateH() {
        if (this.dateH == null) {
            this.dateH = new java.sql.Date(this.createdAt.getTime());
        }
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	
	public Date getUpdatedAt() {
		return updatedAt;
	}



	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getPalletOutput() {
		return palletOutput;
	}

	public void setPalletOutput(Integer palletOutput) {
		this.palletOutput = palletOutput;
	}

	public Integer getTotalNoOfMeters() {
		return totalNoOfMeters;
	}

	public void setTotalNoOfMeters(Integer totalNoOfMeters) {
		this.totalNoOfMeters = totalNoOfMeters;
	}

	public Integer getPassedMeters() {
		return passedMeters;
	}

	public void setPassedMeters(Integer passedMeters) {
		this.passedMeters = passedMeters;
	}

	public Integer getFailedMeters() {
		return failedMeters;
	}

	public void setFailedMeters(Integer failedMeters) {
		this.failedMeters = failedMeters;
	}

	public String getBayType() {
		return bayType;
	}

	public void setBayType(String bayType) {
		this.bayType = bayType;
	}

	public Double getAverageHourlyOutput() {
		return averageHourlyOutput;
	}

	public void setAverageHourlyOutput(Double averageHourlyOutput) {
		this.averageHourlyOutput = averageHourlyOutput;
	}

	public String getPalletDistinctId() {
		return palletDistinctId;
	}

	public void setPalletDistinctId(String palletDistinctId) {
		this.palletDistinctId = palletDistinctId;
	}

	public String getPalletQrId() {
		return palletQrId;
	}

	public void setPalletQrId(String palletQrId) {
		this.palletQrId = palletQrId;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public String getPlantName() {
		return plantName;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateH() {
		return dateH;
	}

	public void setDateH(Date dateH) {
		this.dateH = dateH;
	}
}
