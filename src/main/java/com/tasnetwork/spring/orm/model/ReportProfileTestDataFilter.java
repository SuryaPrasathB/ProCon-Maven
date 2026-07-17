package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.testreport.ExcelCellValueModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "RpTestDataFilter")
//@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class ReportProfileTestDataFilter {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ReportProfileManage rpManage;//reportProfileManageTestFilter;
    
    @OneToMany(mappedBy = "testDataFilter1", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    List<OperationProcess> operationProcessDataList = new ArrayList<OperationProcess>();
    
    
    @OneToMany(mappedBy = "testDataFilter2", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<RpPrintPosition> rpPrintPositionList = new ArrayList<RpPrintPosition>();

    
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean filterActive;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateResultStatus;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean replicateData;
	
	/*@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeader1;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean populateHeader2;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean populateHeader3;
	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderTestPeriodInMinutes = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderWarmupPeriodInMinutes = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderActualVoltage = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderActualCurrent = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderActualPf = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderActualFreq = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private boolean populateHeaderActualEnergy = false;
	*/
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean operationNoneSelected;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean operationInputSelected;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean operationOutputSelected;
	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_VoltFilterSelected;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_CurrentFilterSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_PfFilterSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_FreqFilterSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_EnergyFilterSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_IterationReadingIdSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  header3_CustomSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateIterationHeaderSelected;
	//private boolean  header3_IterationPrefixSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  operationCompareLimitsSelected;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  operationMergeLimitsSelected;
	

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateOperationMasterOutput;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateOperationLocalOutput;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateOperationUpperLimit;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateOperationLowerLimit;


	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  populateOperationComparedLocalStatus;
	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	//@Transient
    private boolean  populateOperationComparedMasterStatus;
	

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  operationInputOnlyHeader;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  operationProcessLocalOutputOnlyHeader;

	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
    private boolean  operationProcessPostActive;	
	
	@Column(columnDefinition = "VARCHAR(45)")
    private String tableSerialNo;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String pageNumber;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testFilterDataPopulateType;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testExecutionResultTypeSelected;
	

	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationSourceResultTypeSelected;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String reportBaseTemplate;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testTypeSelected;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String subTestTypeSelected;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testTypeAlias;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testFilterName;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String header1_Value;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String header2_Value;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String header3_Value;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String header4_Value;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String header5_Value;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String voltPercentFilterUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String currentPercentFilterUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String pfFilterUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String freqFilterUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String energyFilterUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String iterationReadingStartIdUserEntry;
	

	@Column(columnDefinition = "VARCHAR(45)") 
	private String iterationReadingEndIdUserEntry;

	@Column(columnDefinition = "VARCHAR(45)") 
	private String voltFilterUnitValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String currentFilterUnitValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String pfFilterUnitValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String freqFilterUnitValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String energyFilterUnitValue;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String voltPercentFilterData;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String currentPercentFilterData;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String pfFilterData;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String freqFilterData;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String energyFilterData;
	
	@Column(columnDefinition = "VARCHAR(45)")
	private String iterationReadingPrefixValue;
	//private String iterationReadingPrefixValue;
	

	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String replicateCountValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationInputKey;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String userCommentValue;
	

	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessMethod;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessLocalOutputKey;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessLocalResult;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessLocalComparedStatus;
	

	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessMasterComparedStatus;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessLocalUpperLimit;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessLocalLowerLimit;
	

	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String customTestNameUserEntry;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String customTestNameData;
	

	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationMode;
	
	

	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String nonDisplayedDataSet;
	
	

	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessPostMethod;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String operationProcessPostInputValue;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String filterPreview;
	
	@Column(columnDefinition = "VARCHAR(45)") 
	//@Transient
	private String resultDataType;
		
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String header1_CellPosition;
	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String header2_CellPosition;
	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String header3_CellPosition;
	
	
	
	@Transient
	private String headerTestPeriodInMinutesCellPosition;
	
	@Transient
	private String headerWarmUpPeriodInMinutesCellPosition;
	
	@Transient
	private String headerActualVoltageCellPosition;
	
	@Transient
	private String headerActualCurrentCellPosition;
	
	@Transient
	private String headerActualPfCellPosition;
	
	@Transient
	private String headerActualFreqCellPosition;
	
	@Transient
	private String headerActualEnergyCellPosition;
	
	
	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String resultValueCellPosition;
	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String resultStatusCellPosition;
	

	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String resultUpperLimitCellPosition;
	
	//@Column(columnDefinition = "VARCHAR(45)") 
	@Transient
	private String resultLowerLimitCellPosition;
	
	//@Transient
	//private String testResultSourceTypeSelected;
	
	@Transient
	private ArrayList<String> operationProcessInputKeyList = new ArrayList<String>();
	
/*	@Transient
	private ArrayList<RpPrintPosition> testFilterDataCellList = new ArrayList<RpPrintPosition> ();
	
	@Transient
	private ArrayList<RpPrintPosition> headerDataCellList = new ArrayList<RpPrintPosition> ();*/
	
	@Transient
	private ArrayList<String> replicateResultCellPositionList  = new ArrayList<String>();
	
/*	@Transient
	private OperationProcessData operationProcessMasterOutputDataSet = new OperationProcessData();
	
	@Transient
	private OperationProcessData operationProcessLocalOutputDataSet = new OperationProcessData();
	
	@Transient
	private OperationProcessData operationProcessLocalInputDataSet = new OperationProcessData();*/
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
/*	public ReportProfileManage getReportProfileManageTestFilter() {
		return reportProfileManageTestFilter;
	}
	public void setReportProfileManageTestFilter(ReportProfileManage reportProfileManageTestFilter) {
		this.reportProfileManageTestFilter = reportProfileManageTestFilter;
	}*/

	public boolean isFilterActive() {
		return filterActive;
	}
	public void setFilterActive(boolean filterActive) {
		this.filterActive = filterActive;
	}
	public boolean isPopulateResultStatus() {
		return populateResultStatus;
	}
	public void setPopulateResultStatus(boolean populateResultStatus) {
		this.populateResultStatus = populateResultStatus;
	}
	public boolean isReplicateData() {
		return replicateData;
	}
	public void setReplicateData(boolean replicateData) {
		this.replicateData = replicateData;
	}
	/*public boolean isPopulateHeader1() {
		return populateHeader1;
	}
	public void setPopulateHeader1(boolean populateHeader1) {
		this.populateHeader1 = populateHeader1;
	}
	public boolean isPopulateHeader2() {
		return populateHeader2;
	}
	public void setPopulateHeader2(boolean populateHeader2) {
		this.populateHeader2 = populateHeader2;
	}
	public boolean isPopulateHeader3() {
		return populateHeader3;
	}
	public void setPopulateHeader3(boolean populateHeader3) {
		this.populateHeader3 = populateHeader3;
	}*/
	public boolean isOperationNoneSelected() {
		return operationNoneSelected;
	}
	public void setOperationNoneSelected(boolean operationNoneSelected) {
		this.operationNoneSelected = operationNoneSelected;
	}
	public boolean isOperationInputSelected() {
		return operationInputSelected;
	}
	public void setOperationInputSelected(boolean operationInputSelected) {
		this.operationInputSelected = operationInputSelected;
	}
	public boolean isOperationOutputSelected() {
		return operationOutputSelected;
	}
	public void setOperationOutputSelected(boolean operationOutputSelected) {
		this.operationOutputSelected = operationOutputSelected;
	}
	public boolean isHeader3_VoltFilterSelected() {
		return header3_VoltFilterSelected;
	}
	public void setHeader3_VoltFilterSelected(boolean header3_VoltFilterSelected) {
		this.header3_VoltFilterSelected = header3_VoltFilterSelected;
	}
	public boolean isHeader3_CurrentFilterSelected() {
		return header3_CurrentFilterSelected;
	}
	public void setHeader3_CurrentFilterSelected(boolean header3_CurrentFilterSelected) {
		this.header3_CurrentFilterSelected = header3_CurrentFilterSelected;
	}
	public boolean isHeader3_PfFilterSelected() {
		return header3_PfFilterSelected;
	}
	public void setHeader3_PfFilterSelected(boolean header3_PfFilterSelected) {
		this.header3_PfFilterSelected = header3_PfFilterSelected;
	}
	public boolean isHeader3_FreqFilterSelected() {
		return header3_FreqFilterSelected;
	}
	public void setHeader3_FreqFilterSelected(boolean header3_FreqFilterSelected) {
		this.header3_FreqFilterSelected = header3_FreqFilterSelected;
	}
	public boolean isHeader3_EnergyFilterSelected() {
		return header3_EnergyFilterSelected;
	}
	public void setHeader3_EnergyFilterSelected(boolean header3_EnergyFilterSelected) {
		this.header3_EnergyFilterSelected = header3_EnergyFilterSelected;
	}
	public boolean isHeader3_IterationReadingIdSelected() {
		return header3_IterationReadingIdSelected;
	}
	public void setHeader3_IterationReadingIdSelected(boolean header3_IterationReadingIdSelected) {
		this.header3_IterationReadingIdSelected = header3_IterationReadingIdSelected;
	}
	public boolean isHeader3_CustomSelected() {
		return header3_CustomSelected;
	}
	public void setHeader3_CustomSelected(boolean header3_CustomSelected) {
		this.header3_CustomSelected = header3_CustomSelected;
	}
	public boolean isPopulateIterationHeaderSelected() {
		return populateIterationHeaderSelected;
	}
	public void setPopulateIterationHeaderSelected(boolean populateIterationHeaderSelected) {
		this.populateIterationHeaderSelected = populateIterationHeaderSelected;
	}
	public boolean isOperationCompareLimitsSelected() {
		return operationCompareLimitsSelected;
	}
	public void setOperationCompareLimitsSelected(boolean operationCompareLimitsSelected) {
		this.operationCompareLimitsSelected = operationCompareLimitsSelected;
	}
	public boolean isOperationMergeLimitsSelected() {
		return operationMergeLimitsSelected;
	}
	public void setOperationMergeLimitsSelected(boolean operationMergeLimitsSelected) {
		this.operationMergeLimitsSelected = operationMergeLimitsSelected;
	}
	public boolean isPopulateOperationMasterOutput() {
		return populateOperationMasterOutput;
	}
	public void setPopulateOperationMasterOutput(boolean populateOperationMasterOutput) {
		this.populateOperationMasterOutput = populateOperationMasterOutput;
	}
	public boolean isPopulateOperationLocalOutput() {
		return populateOperationLocalOutput;
	}
	public void setPopulateOperationLocalOutput(boolean populateOperationLocalOutput) {
		this.populateOperationLocalOutput = populateOperationLocalOutput;
	}
	public boolean isPopulateOperationUpperLimit() {
		return populateOperationUpperLimit;
	}
	public void setPopulateOperationUpperLimit(boolean populateOperationUpperLimit) {
		this.populateOperationUpperLimit = populateOperationUpperLimit;
	}
	public boolean isPopulateOperationLowerLimit() {
		return populateOperationLowerLimit;
	}
	public void setPopulateOperationLowerLimit(boolean populateOperationLowerLimit) {
		this.populateOperationLowerLimit = populateOperationLowerLimit;
	}
	public boolean isPopulateOperationComparedLocalStatus() {
		return populateOperationComparedLocalStatus;
	}
	public void setPopulateOperationComparedLocalStatus(boolean populateOperationComparedStatus) {
		this.populateOperationComparedLocalStatus = populateOperationComparedStatus;
	}
	public boolean isOperationInputOnlyHeader() {
		return operationInputOnlyHeader;
	}
	public void setOperationInputOnlyHeader(boolean operationInputOnlyHeader) {
		this.operationInputOnlyHeader = operationInputOnlyHeader;
	}
	public boolean isOperationProcessLocalOutputOnlyHeader() {
		return operationProcessLocalOutputOnlyHeader;
	}
	public void setOperationProcessLocalOutputOnlyHeader(boolean operationProcessLocalOutputOnlyHeader) {
		this.operationProcessLocalOutputOnlyHeader = operationProcessLocalOutputOnlyHeader;
	}
	public boolean isOperationProcessPostActive() {
		return operationProcessPostActive;
	}
	public void setOperationProcessPostActive(boolean operationProcessPostActive) {
		this.operationProcessPostActive = operationProcessPostActive;
	}
	public String getTableSerialNo() {
		return tableSerialNo;
	}
	public void setTableSerialNo(String tableSerialNo) {
		this.tableSerialNo = tableSerialNo;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getTestFilterDataPopulateType() {
		return testFilterDataPopulateType;
	}
	public void setTestFilterDataPopulateType(String testFilterDataPopulateType) {
		this.testFilterDataPopulateType = testFilterDataPopulateType;
	}
	public String getTestExecutionResultTypeSelected() {
		return testExecutionResultTypeSelected;
	}
	public void setTestExecutionResultTypeSelected(String testExecutionResultTypeSelected) {
		this.testExecutionResultTypeSelected = testExecutionResultTypeSelected;
	}
	public String getReportBaseTemplate() {
		return reportBaseTemplate;
	}
	public void setReportBaseTemplate(String reportBaseTemplate) {
		this.reportBaseTemplate = reportBaseTemplate;
	}
	public String getTestTypeSelected() {
		return testTypeSelected;
	}
	public void setTestTypeSelected(String testTypeSelected) {
		this.testTypeSelected = testTypeSelected;
	}
	public String getTestTypeAlias() {
		return testTypeAlias;
	}
	public void setTestTypeAlias(String testTypeAlias) {
		this.testTypeAlias = testTypeAlias;
	}
	public String getTestFilterName() {
		return testFilterName;
	}
	public void setTestFilterName(String testFilterName) {
		this.testFilterName = testFilterName;
	}
	public String getHeader1_Value() {
		return header1_Value;
	}
	public void setHeader1_Value(String header1_Value) {
		this.header1_Value = header1_Value;
	}
	public String getHeader2_Value() {
		return header2_Value;
	}
	public void setHeader2_Value(String header2_Value) {
		this.header2_Value = header2_Value;
	}
	public String getHeader3_Value() {
		return header3_Value;
	}
	public void setHeader3_Value(String header3_Value) {
		this.header3_Value = header3_Value;
	}
	public String getVoltPercentFilterUserEntry() {
		return voltPercentFilterUserEntry;
	}
	public void setVoltPercentFilterUserEntry(String voltPercentFilterUserEntry) {
		this.voltPercentFilterUserEntry = voltPercentFilterUserEntry;
	}
	public String getCurrentPercentFilterUserEntry() {
		return currentPercentFilterUserEntry;
	}
	public void setCurrentPercentFilterUserEntry(String currentPercentFilterUserEntry) {
		this.currentPercentFilterUserEntry = currentPercentFilterUserEntry;
	}
	public String getPfFilterUserEntry() {
		return pfFilterUserEntry;
	}
	public void setPfFilterUserEntry(String pfFilterUserEntry) {
		this.pfFilterUserEntry = pfFilterUserEntry;
	}
	public String getFreqFilterUserEntry() {
		return freqFilterUserEntry;
	}
	public void setFreqFilterUserEntry(String freqFilterUserEntry) {
		this.freqFilterUserEntry = freqFilterUserEntry;
	}
	public String getEnergyFilterUserEntry() {
		return energyFilterUserEntry;
	}
	public void setEnergyFilterUserEntry(String energyFilterUserEntry) {
		this.energyFilterUserEntry = energyFilterUserEntry;
	}
	public String getIterationReadingStartIdUserEntry() {
		return iterationReadingStartIdUserEntry;
	}
	public void setIterationReadingStartIdUserEntry(String iterationReadingIdUserEntry) {
		this.iterationReadingStartIdUserEntry = iterationReadingIdUserEntry;
	}
	public String getVoltFilterUnitValue() {
		return voltFilterUnitValue;
	}
	public void setVoltFilterUnitValue(String voltFilterUnitValue) {
		this.voltFilterUnitValue = voltFilterUnitValue;
	}
	public String getCurrentFilterUnitValue() {
		return currentFilterUnitValue;
	}
	public void setCurrentFilterUnitValue(String currentFilterUnitValue) {
		this.currentFilterUnitValue = currentFilterUnitValue;
	}
	public String getPfFilterUnitValue() {
		return pfFilterUnitValue;
	}
	public void setPfFilterUnitValue(String pfFilterUnitValue) {
		this.pfFilterUnitValue = pfFilterUnitValue;
	}
	public String getFreqFilterUnitValue() {
		return freqFilterUnitValue;
	}
	public void setFreqFilterUnitValue(String freqFilterUnitValue) {
		this.freqFilterUnitValue = freqFilterUnitValue;
	}
	public String getEnergyFilterUnitValue() {
		return energyFilterUnitValue;
	}
	public void setEnergyFilterUnitValue(String energyFilterUnitValue) {
		this.energyFilterUnitValue = energyFilterUnitValue;
	}
	public String getVoltPercentFilterData() {
		return voltPercentFilterData;
	}
	public void setVoltPercentFilterData(String voltPercentFilterData) {
		this.voltPercentFilterData = voltPercentFilterData;
	}
	public String getCurrentPercentFilterData() {
		return currentPercentFilterData;
	}
	public void setCurrentPercentFilterData(String currentPercentFilterData) {
		this.currentPercentFilterData = currentPercentFilterData;
	}
	public String getPfFilterData() {
		return pfFilterData;
	}
	public void setPfFilterData(String pfFilterData) {
		this.pfFilterData = pfFilterData;
	}
	public String getFreqFilterData() {
		return freqFilterData;
	}
	public void setFreqFilterData(String freqFilterData) {
		this.freqFilterData = freqFilterData;
	}
	public String getEnergyFilterData() {
		return energyFilterData;
	}
	public void setEnergyFilterData(String energyFilterData) {
		this.energyFilterData = energyFilterData;
	}
	public String getIterationReadingPrefixValue() {
		return iterationReadingPrefixValue;
	}
	public void setIterationReadingPrefixValue(String iterationReadingPrefixValue) {
		this.iterationReadingPrefixValue = iterationReadingPrefixValue;
	}
	public String getReplicateCountValue() {
		return replicateCountValue;
	}
	public void setReplicateCountValue(String replicateCountValue) {
		this.replicateCountValue = replicateCountValue;
	}
	public String getOperationInputKey() {
		return operationInputKey;
	}
	public void setOperationInputKey(String operationInputKey) {
		this.operationInputKey = operationInputKey;
	}
	public String getUserCommentValue() {
		return userCommentValue;
	}
	public void setUserCommentValue(String userCommentValue) {
		this.userCommentValue = userCommentValue;
	}
	public String getOperationProcessMethod() {
		return operationProcessMethod;
	}
	public void setOperationProcessMethod(String operationProcessMethod) {
		this.operationProcessMethod = operationProcessMethod;
	}
	public String getOperationProcessLocalOutputKey() {
		return operationProcessLocalOutputKey;
	}
	public void setOperationProcessLocalOutputKey(String operationProcessLocalOutputKey) {
		this.operationProcessLocalOutputKey = operationProcessLocalOutputKey;
	}
	public String getOperationProcessLocalResult() {
		return operationProcessLocalResult;
	}
	public void setOperationProcessLocalResult(String operationProcessLocalResult) {
		this.operationProcessLocalResult = operationProcessLocalResult;
	}
	public String getOperationProcessLocalComparedStatus() {
		return operationProcessLocalComparedStatus;
	}
	public void setOperationProcessLocalComparedStatus(String operationProcessLocalComparedStatus) {
		this.operationProcessLocalComparedStatus = operationProcessLocalComparedStatus;
	}
	public String getOperationProcessLocalUpperLimit() {
		return operationProcessLocalUpperLimit;
	}
	public void setOperationProcessLocalUpperLimit(String operationProcessLocalUpperLimit) {
		this.operationProcessLocalUpperLimit = operationProcessLocalUpperLimit;
	}
	public String getOperationProcessLocalLowerLimit() {
		return operationProcessLocalLowerLimit;
	}
	public void setOperationProcessLocalLowerLimit(String operationProcessLocalLowerLimit) {
		this.operationProcessLocalLowerLimit = operationProcessLocalLowerLimit;
	}
	public String getCustomTestNameUserEntry() {
		return customTestNameUserEntry;
	}
	public void setCustomTestNameUserEntry(String customTestNameUserEntry) {
		this.customTestNameUserEntry = customTestNameUserEntry;
	}
	public String getCustomTestNameData() {
		return customTestNameData;
	}
	public void setCustomTestNameData(String customTestNameData) {
		this.customTestNameData = customTestNameData;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getNonDisplayedDataSet() {
		return nonDisplayedDataSet;
	}
	public void setNonDisplayedDataSet(String nonDisplayedDataSet) {
		this.nonDisplayedDataSet = nonDisplayedDataSet;
	}
	public String getOperationProcessPostMethod() {
		return operationProcessPostMethod;
	}
	public void setOperationProcessPostMethod(String operationProcessPostMethod) {
		this.operationProcessPostMethod = operationProcessPostMethod;
	}
	public String getOperationProcessPostInputValue() {
		return operationProcessPostInputValue;
	}
	public void setOperationProcessPostInputValue(String operationProcessPostInputValue) {
		this.operationProcessPostInputValue = operationProcessPostInputValue;
	}
	public String getFilterPreview() {
		return filterPreview;
	}
	public void setFilterPreview(String filterPreview) {
		this.filterPreview = filterPreview;
	}
	public String getHeader1_CellPosition() {
		return header1_CellPosition;
	}
	public void setHeader1_CellPosition(String header1_CellPosition) {
		this.header1_CellPosition = header1_CellPosition;
	}
	public String getHeader2_CellPosition() {
		return header2_CellPosition;
	}
	public void setHeader2_CellPosition(String header2_CellPosition) {
		this.header2_CellPosition = header2_CellPosition;
	}
	public String getHeader3_CellPosition() {
		return header3_CellPosition;
	}
	public void setHeader3_CellPosition(String header3_CellPosition) {
		this.header3_CellPosition = header3_CellPosition;
	}
	public String getResultValueCellPosition() {
		return resultValueCellPosition;
	}
	public void setResultValueCellPosition(String resultValueCellPosition) {
		this.resultValueCellPosition = resultValueCellPosition;
	}
	public String getResultStatusCellPosition() {
		return resultStatusCellPosition;
	}
	public void setResultStatusCellPosition(String resultStatusCellPosition) {
		this.resultStatusCellPosition = resultStatusCellPosition;
	}
	public String getResultDataType() {
		return resultDataType;
	}
	public void setResultDataType(String resultDataTypeCellPosition) {
		this.resultDataType = resultDataTypeCellPosition;
	}
	public String getResultUpperLimitCellPosition() {
		return resultUpperLimitCellPosition;
	}
	public void setResultUpperLimitCellPosition(String resultUpperLimitCellPosition) {
		this.resultUpperLimitCellPosition = resultUpperLimitCellPosition;
	}
	public String getResultLowerLimitCellPosition() {
		return resultLowerLimitCellPosition;
	}
	public void setResultLowerLimitCellPosition(String resultLowerLimitCellPosition) {
		this.resultLowerLimitCellPosition = resultLowerLimitCellPosition;
	}
	public ArrayList<String> getOperationProcessInputKeyList() {
		return operationProcessInputKeyList;
	}
	public void setOperationProcessInputKeyList(ArrayList<String> operationProcessInputKeyList) {
		this.operationProcessInputKeyList = operationProcessInputKeyList;
	}
/*	public ArrayList<RpPrintPosition> getTestFilterDataCellList() {
		return testFilterDataCellList;
	}
	public void setTestFilterDataCellList(ArrayList<RpPrintPosition> testFilterDataCellList) {
		this.testFilterDataCellList = testFilterDataCellList;
	}*/
/*	public ArrayList<RpPrintPosition> getHeaderDataCellList() {
		return headerDataCellList;
	}
	public void setHeaderDataCellList(ArrayList<RpPrintPosition> headerDataCellList) {
		this.headerDataCellList = headerDataCellList;
	}*/
	public ArrayList<String> getReplicateResultCellPositionList() {
		return replicateResultCellPositionList;
	}
	public void setReplicateResultCellPositionList(ArrayList<String> replicateResultCellPositionList) {
		this.replicateResultCellPositionList = replicateResultCellPositionList;
	}
/*	public OperationProcessData getOperationProcessMasterOutputDataSet() {
		return operationProcessMasterOutputDataSet;
	}
	public void setOperationProcessMasterOutputDataSet(OperationProcessData operationProcessMasterOutputDataSet) {
		this.operationProcessMasterOutputDataSet = operationProcessMasterOutputDataSet;
	}
	public OperationProcessData getOperationProcessLocalOutputDataSet() {
		return operationProcessLocalOutputDataSet;
	}
	public void setOperationProcessLocalOutputDataSet(OperationProcessData operationProcessLocalOutputDataSet) {
		this.operationProcessLocalOutputDataSet = operationProcessLocalOutputDataSet;
	}
	public OperationProcessData getOperationProcessLocalInputDataSet() {
		return operationProcessLocalInputDataSet;
	}
	public void setOperationProcessLocalInputDataSet(OperationProcessData operationProcessLocalInputDataSet) {
		this.operationProcessLocalInputDataSet = operationProcessLocalInputDataSet;
	}*/
	
	
	public void processOperationMode() {
		String operationModeNone = "None";
		String operationModeInput = "Input";
		String operationModeOutput = "Output";
		if(isOperationNoneSelected()){
			this.operationMode = operationModeNone;
		}else if(isOperationInputSelected()){
			this.operationMode = operationModeInput;
		}else if(isOperationOutputSelected()){
			this.operationMode = operationModeOutput;
		}
		
	}
	
	
	public ReportProfileManage getReportProfileManage() {
		return rpManage;
	}

	public void setReportProfileManage(ReportProfileManage reportProfileManage) {
		this.rpManage = reportProfileManage;
	}
	
	public void clearOperationProcessDataList() {
		operationProcessDataList.clear();;
	}
	
	public void clearOperationProcessDataListAddedToInputProcess() {
		try{
			operationProcessDataList.stream()
				.filter(e -> e.isAddedToInputProcess()==true)
				.forEach( e -> operationProcessDataList.remove(e));
		} catch(Exception e){
			ApplicationLauncher.logger.debug("clearOperationProcessDataListAddedToInputProcess: Exception: " + e.getMessage());
		}
	}
	public List<OperationProcess> getOperationProcessDataList() {
		return operationProcessDataList;
	}
	public void setOperationProcessDataList(List<OperationProcess> operationProcessDataList) {
		this.operationProcessDataList = operationProcessDataList;
	}
	public void addOperationProcessDataList(OperationProcess operationProcessData) {
		this.operationProcessDataList.add(operationProcessData);
		operationProcessData.setTestDataFilter(this);
	}
	
/*	public void appendAllOperationProcessDataList(List<OperationProcess> operationProcessDataList) {
		this.operationProcessDataList.addAll(operationProcessDataList);
	}*/
	public List<RpPrintPosition> getRpPrintPositionList() {
		return rpPrintPositionList;
	}
	public void setRpPrintPositionList(List<RpPrintPosition> rpPrintPositionList) {
		this.rpPrintPositionList = rpPrintPositionList;
	}
	
	public void addRpPrintPositionList(RpPrintPosition printPosition) {
		this.rpPrintPositionList.add(printPosition);
		printPosition.setTestDataFilter2(this);
	}
	public void clearRpPrintPositionList() {
		rpPrintPositionList.clear();
	}
	public String getSubTestTypeSelected() {
		return subTestTypeSelected;
	}
	public void setSubTestTypeSelected(String subTestTypeSelected) {
		this.subTestTypeSelected = subTestTypeSelected;
	}
	public String getOperationProcessMasterComparedStatus() {
		return operationProcessMasterComparedStatus;
	}
	public void setOperationProcessMasterComparedStatus(String operationProcessMasterComparedStatus) {
		this.operationProcessMasterComparedStatus = operationProcessMasterComparedStatus;
	}
	public boolean isPopulateOperationComparedMasterStatus() {
		return populateOperationComparedMasterStatus;
	}
	public void setPopulateOperationComparedMasterStatus(boolean populateOperationComparedMasterStatus) {
		this.populateOperationComparedMasterStatus = populateOperationComparedMasterStatus;
	}
	public String getHeaderTestPeriodInMinutesCellPosition() {
		return headerTestPeriodInMinutesCellPosition;
	}
	public void setHeaderTestPeriodInMinutesCellPosition(String headerTestPeriodInMinutesCellPosition) {
		this.headerTestPeriodInMinutesCellPosition = headerTestPeriodInMinutesCellPosition;
	}
	public String getHeaderWarmUpPeriodInMinutesCellPosition() {
		return headerWarmUpPeriodInMinutesCellPosition;
	}
	public void setHeaderWarmUpPeriodInMinutesCellPosition(String headerWarmUpPeriodInMinutesCellPosition) {
		this.headerWarmUpPeriodInMinutesCellPosition = headerWarmUpPeriodInMinutesCellPosition;
	}
	public String getHeaderActualVoltageCellPosition() {
		return headerActualVoltageCellPosition;
	}
	public void setHeaderActualVoltageCellPosition(String headerActualVoltageCellPosition) {
		this.headerActualVoltageCellPosition = headerActualVoltageCellPosition;
	}
	public String getHeaderActualCurrentCellPosition() {
		return headerActualCurrentCellPosition;
	}
	public void setHeaderActualCurrentCellPosition(String headerActualCurrentCellPosition) {
		this.headerActualCurrentCellPosition = headerActualCurrentCellPosition;
	}
	public String getHeaderActualPfCellPosition() {
		return headerActualPfCellPosition;
	}
	public void setHeaderActualPfCellPosition(String headerActualPfCellPosition) {
		this.headerActualPfCellPosition = headerActualPfCellPosition;
	}
	public String getHeaderActualFreqCellPosition() {
		return headerActualFreqCellPosition;
	}
	public void setHeaderActualFreqCellPosition(String headerActualFreqCellPosition) {
		this.headerActualFreqCellPosition = headerActualFreqCellPosition;
	}
	public String getHeaderActualEnergyCellPosition() {
		return headerActualEnergyCellPosition;
	}
	public void setHeaderActualEnergyCellPosition(String headerActualEnergyCellPosition) {
		this.headerActualEnergyCellPosition = headerActualEnergyCellPosition;
	}
	/*public boolean isPopulateHeaderTestPeriodInMinutes() {
		return populateHeaderTestPeriodInMinutes;
	}
	public void setPopulateHeaderTestPeriodInMinutes(boolean populateHeaderTestPeriodInMinutes) {
		this.populateHeaderTestPeriodInMinutes = populateHeaderTestPeriodInMinutes;
	}
	public boolean isPopulateHeaderWarmupPeriodInMinutes() {
		return populateHeaderWarmupPeriodInMinutes;
	}
	public void setPopulateHeaderWarmupPeriodInMinutes(boolean populateHeaderWarmupPeriodInMinutes) {
		this.populateHeaderWarmupPeriodInMinutes = populateHeaderWarmupPeriodInMinutes;
	}
	public boolean isPopulateHeaderActualVoltage() {
		return populateHeaderActualVoltage;
	}
	public void setPopulateHeaderActualVoltage(boolean populateHeaderActualVoltage) {
		this.populateHeaderActualVoltage = populateHeaderActualVoltage;
	}
	public boolean isPopulateHeaderActualCurrent() {
		return populateHeaderActualCurrent;
	}
	public void setPopulateHeaderActualCurrent(boolean populateHeaderActualCurrent) {
		this.populateHeaderActualCurrent = populateHeaderActualCurrent;
	}
	public boolean isPopulateHeaderActualPf() {
		return populateHeaderActualPf;
	}
	public void setPopulateHeaderActualPf(boolean populateHeaderActualPf) {
		this.populateHeaderActualPf = populateHeaderActualPf;
	}
	public boolean isPopulateHeaderActualFreq() {
		return populateHeaderActualFreq;
	}
	public void setPopulateHeaderActualFreq(boolean populateHeaderActualFreq) {
		this.populateHeaderActualFreq = populateHeaderActualFreq;
	}
	public boolean isPopulateHeaderActualEnergy() {
		return populateHeaderActualEnergy;
	}
	public void setPopulateHeaderActualEnergy(boolean populateHeaderActualEnergy) {
		this.populateHeaderActualEnergy = populateHeaderActualEnergy;
	}
	*/
	
	
	public String getIterationReadingEndIdUserEntry() {
		return iterationReadingEndIdUserEntry;
	}
	public void setIterationReadingEndIdUserEntry(String iterationReadingEndIdUserEntry) {
		this.iterationReadingEndIdUserEntry = iterationReadingEndIdUserEntry;
	}
	public String getOperationSourceResultTypeSelected() {
		return operationSourceResultTypeSelected;
	}
	public void setOperationSourceResultTypeSelected(String operationSourceResultTypeSelected) {
		this.operationSourceResultTypeSelected = operationSourceResultTypeSelected;
	}
	public String getHeader4_Value() {
		return header4_Value;
	}
	public String getHeader5_Value() {
		return header5_Value;
	}
	public void setHeader4_Value(String header4_Value) {
		this.header4_Value = header4_Value;
	}
	public void setHeader5_Value(String header5_Value) {
		this.header5_Value = header5_Value;
	}
    
}
