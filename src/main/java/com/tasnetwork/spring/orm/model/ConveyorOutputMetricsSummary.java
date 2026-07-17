package com.tasnetwork.spring.orm.model;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
	    name = "conveyor_output_metrics_summary",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"customerName", "bayType", "createdAt"})
	    }
	)
public class ConveyorOutputMetricsSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(45)")
    private String customerName;

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
    
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String locationName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String departmentName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String plantName="";
    
    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String lineNo="";
    
    @Column(precision = 5, scale = 2,  nullable = false) // Stores percentages like 99.99
    private Double passedPercentage=0.0;

    @Column(precision = 5, scale = 2 , nullable = false) // Stores percentages like 99.99
    private Double failedPercentage=0.0;
    
/*    @Column( columnDefinition = "VARCHAR(45)" , nullable = false) 
    private String userName="";*/

    private Integer palletOutput;
    private Integer totalNoOfMeters;
    private Integer passedMeters;
    private Integer failedMeters;

    @Column(columnDefinition = "VARCHAR(45)")
    private String bayType;

    private Double averageHourlyOutput;

    // Auto-set timestamps
/*    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }*/

    // Getters and Setters
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

    // toString() Method
    @Override
    public String toString() {
        return "ConveyorOutputMetricsSummary {" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", palletOutput=" + palletOutput +
                ", totalNoOfMeters=" + totalNoOfMeters +
                ", passedMeters=" + passedMeters +
                ", failedMeters=" + failedMeters +
                ", bayType='" + bayType + '\'' +
                ", averageHourlyOutput=" + averageHourlyOutput +
                '}';
    }
    
    
/*    @PrePersist
    protected void initializeDateH() {
        if (this.dateH == null) {
            this.dateH = new java.sql.Date(this.createdAt.getTime());
        }
    }*/
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        if (this.dateH == null) {
            this.dateH = new Date(createdAt.getTime());
        }
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

	public Double getPassedPercentage() {
		return passedPercentage;
	}

	public Double getFailedPercentage() {
		return failedPercentage;
	}

	public void setPassedPercentage(Double passedPercentage) {
		this.passedPercentage = passedPercentage;
	}

	public void setFailedPercentage(Double failedPercentage) {
		this.failedPercentage = failedPercentage;
	}

	public Date getDateH() {
		return dateH;
	}

	public void setDateH(Date dateH) {
		this.dateH = dateH;
	}

/*	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}*/
}
