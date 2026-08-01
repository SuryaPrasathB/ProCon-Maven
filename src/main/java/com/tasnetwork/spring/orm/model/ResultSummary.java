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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ResultSummary")
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)

public class ResultSummary {

	   
		    @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY )
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
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String customerId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String customerName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String siteId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String siteName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String plantId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String plantName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String departmentId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String departmentName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String projectId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String projectName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String deploymentId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String runId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String runIdHuman;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String batchId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String dutProfileId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String terminalId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String clusterId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String bayId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String terminalName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String clusterName;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String bayName;
			
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String palletId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String dutSerialNo;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String deviceType;

			@Column(columnDefinition = "VARCHAR(45)")
		    private String deviceId;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String positionNo;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String deviceResponseData;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String deviceResponseStatus;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String testExecutionStatus;
			
			@Column(columnDefinition = "VARCHAR(45)")
		    private String testExecutionType;

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
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

			public String getCustomerId() {
				return customerId;
			}

			public void setCustomerId(String customerId) {
				this.customerId = customerId;
			}

			public String getCustomerName() {
				return customerName;
			}
			
			

			public void setCustomerName(String customerName) {
				this.customerName = customerName;
			}

			public String getProjectId() {
				return projectId;
			}

			public void setProjectId(String projectId) {
				this.projectId = projectId;
			}

			public String getProjectName() {
				return projectName;
			}

			public void setProjectName(String projectName) {
				this.projectName = projectName;
			}

			public String getDeploymentId() {
				return deploymentId;
			}

			public void setDeploymentId(String deploymentId) {
				this.deploymentId = deploymentId;
			}

			public String getRunId() {
				return runId;
			}

			public void setRunId(String runId) {
				this.runId = runId;
			}

			public String getRunIdHuman() {
				return runIdHuman;
			}

			public void setRunIdHuman(String runIdHuman) {
				this.runIdHuman = runIdHuman;
			}

			public String getBatchId() {
				return batchId;
			}

			public void setBatchId(String batchId) {
				this.batchId = batchId;
			}

			public String getDutProfileId() {
				return dutProfileId;
			}

			public void setDutProfileId(String dutProfileId) {
				this.dutProfileId = dutProfileId;
			}

			public String getTerminalId() {
				return terminalId;
			}

			public void setTerminalId(String terminalId) {
				this.terminalId = terminalId;
			}

			public String getClusterId() {
				return clusterId;
			}

			public void setClusterId(String clusterId) {
				this.clusterId = clusterId;
			}

			public String getBayId() {
				return bayId;
			}

			public void setBayId(String bayId) {
				this.bayId = bayId;
			}

			public String getTerminalName() {
				return terminalName;
			}

			public void setTerminalName(String terminalName) {
				this.terminalName = terminalName;
			}

			public String getClusterName() {
				return clusterName;
			}

			public void setClusterName(String clusterName) {
				this.clusterName = clusterName;
			}

			public String getBayName() {
				return bayName;
			}

			public void setBayName(String bayName) {
				this.bayName = bayName;
			}

			public String getPalletId() {
				return palletId;
			}

			public void setPalletId(String palletId) {
				this.palletId = palletId;
			}

			public String getDutSerialNo() {
				return dutSerialNo;
			}

			public void setDutSerialNo(String dutSerialNo) {
				this.dutSerialNo = dutSerialNo;
			}

			public String getDeviceType() {
				return deviceType;
			}

			public void setDeviceType(String deviceType) {
				this.deviceType = deviceType;
			}

			public String getDeviceId() {
				return deviceId;
			}

			public void setDeviceId(String deviceId) {
				this.deviceId = deviceId;
			}

			public String getPositionNo() {
				return positionNo;
			}

			public void setPositionNo(String positionNo) {
				this.positionNo = positionNo;
			}

			public String getDeviceResponseData() {
				return deviceResponseData;
			}

			public void setDeviceResponseData(String deviceResponseData) {
				this.deviceResponseData = deviceResponseData;
			}

			public String getDeviceResponseStatus() {
				return deviceResponseStatus;
			}

			public void setDeviceResponseStatus(String deviceResponseStatus) {
				this.deviceResponseStatus = deviceResponseStatus;
			}

			public String getTestExecutionStatus() {
				return testExecutionStatus;
			}

			public void setTestExecutionStatus(String testExecutionStatus) {
				this.testExecutionStatus = testExecutionStatus;
			}

			public String getTestExecutionType() {
				return testExecutionType;
			}

			public void setTestExecutionType(String testExecutionType) {
				this.testExecutionType = testExecutionType;
			}

			public String getSiteId() {
				return siteId;
			}

			public void setSiteId(String siteId) {
				this.siteId = siteId;
			}

			public String getSiteName() {
				return siteName;
			}

			public void setSiteName(String siteName) {
				this.siteName = siteName;
			}

			public String getPlantId() {
				return plantId;
			}

			public void setPlantId(String plantId) {
				this.plantId = plantId;
			}

			public String getPlantName() {
				return plantName;
			}

			public void setPlantName(String plantName) {
				this.plantName = plantName;
			}

			public String getDepartmentId() {
				return departmentId;
			}

			public void setDepartmentId(String departmentId) {
				this.departmentId = departmentId;
			}

			public String getDepartmentName() {
				return departmentName;
			}

			public void setDepartmentName(String departmentName) {
				this.departmentName = departmentName;
			}
			
			
			
		    
			
			//@Column(name = "report_profile_group_id", columnDefinition = "VARCHAR(45)")
			
		    
		    
/*		    
		    //@Column(name = "active", columnDefinition = "VARCHAR(3)", nullable = false)
		    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
		    //@Convert(converter=BooleanToYNStringConverter.class)
		    @Type(type = "yes_no")
		    private boolean activeFilter;*/
		   

		    
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
		    
/*
		    @Column(name = "filter_name",columnDefinition = "VARCHAR(45)")//,unique=true)
		    private String filterName;
		    
		    @Column(name = "append_dut_serial_no_and_rack_pos",columnDefinition = "VARCHAR(3)", nullable = false)
		    //@Convert(converter=BooleanToYNStringConverter.class)
		    @Type(type = "yes_no")
		    private boolean discardRackPositionInDutSerialNumber;

		    
		    @Column(name = "append_dut_serial_no_and_rack_pos",columnDefinition = "VARCHAR(3)", nullable = false)
		    //@Convert(converter=BooleanToYNStringConverter.class)
		    @Type(type = "yes_no")
		    private boolean populateOnlyOnHeader;
		    
		    @Column(name = "append_dut_serial_no_and_rack_pos",columnDefinition = "VARCHAR(3)", nullable = false)
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
			private boolean populateDutPageStatus = false;*/
		    /*
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

			public Date getCreatedAt() {
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
			}

			public boolean isFilterActive() {
				return activeFilter;
			}

			public void setFilterActive(boolean filterActive) {
				this.activeFilter = filterActive;
			}

			public String getReportGroupId() {
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
			}

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
		   
*/


}


