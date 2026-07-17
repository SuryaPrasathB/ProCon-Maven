package com.tasnetwork.spring.orm.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
//import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.util.BooleanToYNStringConverter;



@Entity
@Table(name = "RpMeterMetaDataFilter")
//@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class ReportProfileMeterMetaDataFilter {
	

     
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ReportProfileManage rpManage;
    
   // @CreatedBy
   // private User user;

    
/*	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;*/
    
	
	//@Column(name = "report_profile_group_id", columnDefinition = "VARCHAR(45)")
	
    
    
    
    //@Column(name = "active", columnDefinition = "VARCHAR(3)", nullable = false)
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    //@Convert(converter=BooleanToYNStringConverter.class)
    @Type(type = "yes_no")
    private boolean activeFilter;
   

    
/*    @Column(name = "report_group_id",columnDefinition = "VARCHAR(45)")
    private String reportGroupId;
    
    @Column(name = "report_group_name",columnDefinition = "VARCHAR(45)")
    private String reportGroupName;
    
    @Column(name = "report_profile_id",columnDefinition = "VARCHAR(45)")
    private String reportProfileId;
    
    @Column(name = "report_profile_name",columnDefinition = "VARCHAR(45)")
    private String reportProfileName;
    
    @Column(name = "base_template_name",columnDefinition = "VARCHAR(45)")
    private String baseTemplateName;*/
    
    @Column(/*name = "base_template_meta_data_populate_type",*/columnDefinition = "VARCHAR(45)")
    private String baseTemplateMetaDataPopulateType;

    //@Column(/*name = "page_number")
    private Integer pageNumber;
    
    
    
    @Column(/*name = "filter_name",*/columnDefinition = "VARCHAR(45)")//,unique=true)
    private String filterName;
    
    @Column(/*name = "append_dut_serial_no_and_rack_pos",*/columnDefinition = "VARCHAR(3)", nullable = false)
    //@Convert(converter=BooleanToYNStringConverter.class)
    @Type(type = "yes_no")
    private boolean discardRackPositionInDutSerialNumber;

    
    @Column(/*name = "append_dut_serial_no_and_rack_pos",*/columnDefinition = "VARCHAR(3)", nullable = false)
    //@Convert(converter=BooleanToYNStringConverter.class)
    @Type(type = "yes_no")
    private boolean populateOnlyOnHeader;
    
    @Column(/*name = "append_dut_serial_no_and_rack_pos",*/columnDefinition = "VARCHAR(3)", nullable = false)
    //@Convert(converter=BooleanToYNStringConverter.class)
    @Type(type = "yes_no")
    private boolean populateForEachDut;
    
    
    @Column(columnDefinition = "VARCHAR(45)")
    private String tableSerialNo;
    
    @Column(columnDefinition = "VARCHAR(45)")//,unique=true)
    private String meterDataType;
    
    @Column(columnDefinition = "VARCHAR(45)")
    private String cellPosition;
    

    @Transient
	private boolean populateDutPageStatus = false;
    
    @Transient
	private boolean populateDutOverAllStatus = false;
    
    @Transient
	private boolean populateDutSerialNo = false;
    
    @Transient
	private boolean populateSerialNo = false;
    
    
    @Transient
	private boolean populateMake = false;
    
    @Transient
	private boolean populateModelNo = false;
    
	@Transient
	private boolean populateCustomerRefNo = false;
    
	@Transient
	private boolean populateCapacity = false;
    
	@Transient
	private boolean populateMeterType = false;
    
	@Transient
	private boolean populateMeterConstant = false;
    
	@Transient
	private boolean populatePtRatio = false;
    
	@Transient
	private boolean populateCtRatio = false;
	
	@Transient
	private boolean populateRackPositionNo = false;
	
	

	@Transient
	private boolean populateDutClass = false;

	@Transient
	private boolean populateDutBasicCurrent = false;

	@Transient
	private boolean populateDutMaxCurrent = false;

	@Transient
	private boolean populateDutRatedVolt = false;

	@Transient
	private boolean populateDutFreq = false;

	@Transient
	private boolean populateDutCtType = false;

	@Transient
	private boolean populateCustomerName = false;

	@Transient
	private boolean populateLoraId = false;

	@Transient
	private boolean populateExecutedTimeStamp = false;

	@Transient
	private boolean populateExecutedDate = false;

	@Transient
	private boolean populateExecutedTime = false;

	@Transient
	private boolean populateReportGenTimeStamp = false;

	@Transient
	private boolean populateReportGenDate = false;

	@Transient
	private boolean populateReportGenTime = false;

	@Transient
	private boolean populateApprovedTimeStamp = false;

	@Transient
	private boolean populateApprovedDate = false;

	@Transient
	private boolean populateApprovedTime = false;

	@Transient
	private boolean populateTestedBy = false;
	
	@Transient
	private boolean populateWitnessedBy = false;
	
	@Transient
	private boolean populateApprovedBy = false;
	
	@Transient
	private boolean populatePageNo = false;
	
	@Transient
	private boolean populateMaxNoOfPages = false;
	
	
	@Transient
	private boolean populatePageNoWithMaxNoOfPages = false;
	
	@Transient
	private boolean populateEnergyFlowMode = false;
	
	@Transient
	private boolean populateExecutionCtMode = false;
	
	
	@Transient
	private boolean populateActiveReactiveEnergy = false;
	
	@Transient
	private boolean populateComplyStatus = false;
	
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

/*	public Date getCreatedAt() {
		ApplicationLauncher.logger.debug("getCreatedAt: Entry");
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		//ApplicationLauncher.logger.debug("getCreatedAt: createdAt:" + createdAt);
		//ApplicationLauncher.logger.debug("getCreatedAt: Date.from -Instant.now:" + Date.from(Instant.now()));
		this.createdAt = createdAt;//Date.from(Instant.now());;//createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;//Date.from(Instant.now());//LocalDateTime.now();//updatedAt;
	}*/

	public boolean isFilterActive() {
		return activeFilter;
	}

	public void setFilterActive(boolean filterActive) {
		this.activeFilter = filterActive;
	}

/*	public String getReportGroupId() {
		return reportGroupId;
	}

	public void setReportGroupId(String reportGroupId) {
		this.reportGroupId = reportGroupId;
	}

	public String getReportGroupName() {
		return reportGroupName;
	}

	public void setReportGroupName(String reportGroupName) {
		this.reportGroupName = reportGroupName;
	}

	public String getReportProfileId() {
		return reportProfileId;
	}

	public void setReportProfileId(String reportProfileId) {
		this.reportProfileId = reportProfileId;
	}

	public String getReportProfileName() {
		return reportProfileName;
	}

	public void setReportProfileName(String reportProfileName) {
		this.reportProfileName = reportProfileName;
	}

	public String getBaseTemplateName() {
		return baseTemplateName;
	}

	public void setBaseTemplateName(String baseTemplateName) {
		this.baseTemplateName = baseTemplateName;
	}*/

	public String getBaseTemplateMetaDataPopulateType() {
		return baseTemplateMetaDataPopulateType;
	}

	public void setBaseTemplateMetaDataPopulateType(String baseTemplateMetaDataPopulateType) {
		this.baseTemplateMetaDataPopulateType = baseTemplateMetaDataPopulateType;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public boolean isDiscardRackPositionInDutSerialNumber() {
		return discardRackPositionInDutSerialNumber;
	}

	public void setDiscardRackPositionInDutSerialNumber(boolean appendDutSerialAndRackPosition) {
		this.discardRackPositionInDutSerialNumber = appendDutSerialAndRackPosition;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public boolean isPopulateOnlyOnHeader() {
		return populateOnlyOnHeader;
	}

	public void setPopulateOnlyOnHeader(boolean populateOnlyOnHeader) {
		this.populateOnlyOnHeader = populateOnlyOnHeader;
	}

	public boolean isPopulateForEachDut() {
		return populateForEachDut;
	}

	public void setPopulateForEachDut(boolean populateForEachDut) {
		this.populateForEachDut = populateForEachDut;
	}

	public String getTableSerialNo() {
		return tableSerialNo;
	}

	public void setTableSerialNo(String tableSerialNo) {
		this.tableSerialNo = tableSerialNo;
	}

	public String getMeterDataType() {
		return meterDataType;
	}

	public void setMeterDataType(String meterDataType) {
		this.meterDataType = meterDataType;
	}

	public String getCellPosition() {
		return cellPosition;
	}

	public void setCellPosition(String cellPosition) {
		this.cellPosition = cellPosition;
	}

	public ReportProfileManage getReportProfileManage() {
		return rpManage;
	}

	public void setReportProfileManage(ReportProfileManage reportProfileManage) {
		this.rpManage = reportProfileManage;
	}

	public boolean isPopulateSerialNo() {
		return populateSerialNo;
	}

	public void setPopulateSerialNo(boolean populateSerialNo) {
		this.populateSerialNo = populateSerialNo;
	}

	public boolean isPopulateMake() {
		return populateMake;
	}

	public void setPopulateMake(boolean populateMake) {
		this.populateMake = populateMake;
	}

	
	
	
	public boolean isPopulateModelNo() {
		return populateModelNo;
	}

	public void setPopulateModelNo(boolean populateModelNo) {
		this.populateModelNo = populateModelNo;
	}
	
	public boolean isPopulateCustomerRefNo() {
		return populateCustomerRefNo;
	}

	public void setPopulateCustomerRefNo(boolean populateCustomerRefNo) {
		this.populateCustomerRefNo = populateCustomerRefNo;
	}

	public boolean isPopulateCapacity() {
		return populateCapacity;
	}

	public void setPopulateCapacity(boolean populateCapacity) {
		this.populateCapacity = populateCapacity;
	}

	public boolean isPopulateMeterType() {
		return populateMeterType;
	}

	public void setPopulateMeterType(boolean populateMeterType) {
		this.populateMeterType = populateMeterType;
	}

	public boolean isPopulateMeterConstant() {
		return populateMeterConstant;
	}

	public void setPopulateMeterConstant(boolean populateMeterConstant) {
		this.populateMeterConstant = populateMeterConstant;
	}

	public boolean isPopulatePtRatio() {
		return populatePtRatio;
	}

	public void setPopulatePtRatio(boolean populatePtRatio) {
		this.populatePtRatio = populatePtRatio;
	}

	public boolean isPopulateCtRatio() {
		return populateCtRatio;
	}

	public void setPopulateCtRatio(boolean populateCtRatio) {
		this.populateCtRatio = populateCtRatio;
	}

	public boolean isPopulateDutSerialNo() {
		return populateDutSerialNo;
	}

	public void setPopulateDutSerialNo(boolean populateDutSerialNo) {
		this.populateDutSerialNo = populateDutSerialNo;
	}

	public boolean isPopulateDutOverAllStatus() {
		return populateDutOverAllStatus;
	}

	public void setPopulateDutOverAllStatus(boolean populateDutOverAllStatus) {
		this.populateDutOverAllStatus = populateDutOverAllStatus;
	}

	public boolean isPopulateRackPositionNo() {
		return populateRackPositionNo;
	}

	public void setPopulateRackPositionNo(boolean populateRackPoistionNo) {
		this.populateRackPositionNo = populateRackPoistionNo;
	}

	public boolean isPopulateDutPageStatus() {
		return populateDutPageStatus;
	}

	public void setPopulateDutPageStatus(boolean populateDutPageStatus) {
		this.populateDutPageStatus = populateDutPageStatus;
	}

	public boolean isPopulateDutClass() {
		return populateDutClass;
	}

	public void setPopulateDutClass(boolean populateDutClass) {
		this.populateDutClass = populateDutClass;
	}

	public boolean isPopulateDutBasicCurrent() {
		return populateDutBasicCurrent;
	}

	public void setPopulateDutBasicCurrent(boolean populateDutBasicCurrent) {
		this.populateDutBasicCurrent = populateDutBasicCurrent;
	}

	public boolean isPopulateDutMaxCurrent() {
		return populateDutMaxCurrent;
	}

	public void setPopulateDutMaxCurrent(boolean populateDutMaxCurrent) {
		this.populateDutMaxCurrent = populateDutMaxCurrent;
	}

	public boolean isPopulateDutRatedVolt() {
		return populateDutRatedVolt;
	}

	public void setPopulateDutRatedVolt(boolean populateDutRatedVolt) {
		this.populateDutRatedVolt = populateDutRatedVolt;
	}

	public boolean isPopulateDutFreq() {
		return populateDutFreq;
	}

	public void setPopulateDutFreq(boolean populateDutFreq) {
		this.populateDutFreq = populateDutFreq;
	}

	public boolean isPopulateDutCtType() {
		return populateDutCtType;
	}

	public void setPopulateDutCtType(boolean populateDutCtType) {
		this.populateDutCtType = populateDutCtType;
	}

	public boolean isPopulateCustomerName() {
		return populateCustomerName;
	}

	public void setPopulateCustomerName(boolean populateCustomerName) {
		this.populateCustomerName = populateCustomerName;
	}

	public boolean isPopulateLoraId() {
		return populateLoraId;
	}

	public void setPopulateLoraId(boolean populateLoraId) {
		this.populateLoraId = populateLoraId;
	}

	public boolean isPopulateExecutedTimeStamp() {
		return populateExecutedTimeStamp;
	}

	public void setPopulateExecutedTimeStamp(boolean populateExecutedTimeStamp) {
		this.populateExecutedTimeStamp = populateExecutedTimeStamp;
	}

	public boolean isPopulateExecutedDate() {
		return populateExecutedDate;
	}

	public void setPopulateExecutedDate(boolean populateExecutedDate) {
		this.populateExecutedDate = populateExecutedDate;
	}

	public boolean isPopulateExecutedTime() {
		return populateExecutedTime;
	}

	public void setPopulateExecutedTime(boolean populateExecutedTime) {
		this.populateExecutedTime = populateExecutedTime;
	}

	public boolean isPopulateReportGenTimeStamp() {
		return populateReportGenTimeStamp;
	}

	public void setPopulateReportGenTimeStamp(boolean populateReportGenTimeStamp) {
		this.populateReportGenTimeStamp = populateReportGenTimeStamp;
	}

	public boolean isPopulateReportGenDate() {
		return populateReportGenDate;
	}

	public void setPopulateReportGenDate(boolean populateReportGenDate) {
		this.populateReportGenDate = populateReportGenDate;
	}

	public boolean isPopulateReportGenTime() {
		return populateReportGenTime;
	}

	public void setPopulateReportGenTime(boolean populateReportGenTime) {
		this.populateReportGenTime = populateReportGenTime;
	}

	public boolean isPopulateApprovedTimeStamp() {
		return populateApprovedTimeStamp;
	}

	public void setPopulateApprovedTimeStamp(boolean populateApprovedTimeStamp) {
		this.populateApprovedTimeStamp = populateApprovedTimeStamp;
	}

	public boolean isPopulateApprovedDate() {
		return populateApprovedDate;
	}

	public void setPopulateApprovedDate(boolean populateApprovedDate) {
		this.populateApprovedDate = populateApprovedDate;
	}

	public boolean isPopulateApprovedTime() {
		return populateApprovedTime;
	}

	public void setPopulateApprovedTime(boolean populateApprovedTime) {
		this.populateApprovedTime = populateApprovedTime;
	}

	public boolean isPopulateTestedBy() {
		return populateTestedBy;
	}

	public void setPopulateTestedBy(boolean populateTestedBy) {
		this.populateTestedBy = populateTestedBy;
	}

	public boolean isPopulateWitnessedBy() {
		return populateWitnessedBy;
	}

	public void setPopulateWitnessedBy(boolean populateWitnessedBy) {
		this.populateWitnessedBy = populateWitnessedBy;
	}

	public boolean isPopulateApprovedBy() {
		return populateApprovedBy;
	}

	public void setPopulateApprovedBy(boolean populateApprovedBy) {
		this.populateApprovedBy = populateApprovedBy;
	}

	public boolean isPopulatePageNo() {
		return populatePageNo;
	}

	public void setPopulatePageNo(boolean populatePageNo) {
		this.populatePageNo = populatePageNo;
	}

	public boolean isPopulateMaxNoOfPages() {
		return populateMaxNoOfPages;
	}

	public void setPopulateMaxNoOfPages(boolean populateMaxNoOfPages) {
		this.populateMaxNoOfPages = populateMaxNoOfPages;
	}

	public boolean isPopulatePageNoWithMaxNoOfPages() {
		return populatePageNoWithMaxNoOfPages;
	}

	public void setPopulatePageNoWithMaxNoOfPages(boolean populatePageNoWithMaxNoOfPages) {
		this.populatePageNoWithMaxNoOfPages = populatePageNoWithMaxNoOfPages;
	}

	public boolean isPopulateEnergyFlowMode() {
		return populateEnergyFlowMode;
	}

	public void setPopulateEnergyFlowMode(boolean populateEnergyFlowMode) {
		this.populateEnergyFlowMode = populateEnergyFlowMode;
	}

	public boolean isPopulateExecutionCtMode() {
		return populateExecutionCtMode;
	}

	public void setPopulateExecutionCtMode(boolean populateExecutionCtMode) {
		this.populateExecutionCtMode = populateExecutionCtMode;
	}

	public boolean isPopulateActiveReactiveEnergy() {
		return populateActiveReactiveEnergy;
	}

	public void setPopulateActiveReactiveEnergy(boolean populateActiveReactiveEnergy) {
		this.populateActiveReactiveEnergy = populateActiveReactiveEnergy;
	}

	public boolean isPopulateComplyStatus() {
		return populateComplyStatus;
	}

	public void setPopulateComplyStatus(boolean populateComplyStatus) {
		this.populateComplyStatus = populateComplyStatus;
	}
   



}
