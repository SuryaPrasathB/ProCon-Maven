package com.tasnetwork.spring.orm.model;

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

import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "RpPrintPosition")
public class RpPrintPosition {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ReportProfileTestDataFilter testDataFilter2;
    
    
    @Column(columnDefinition = "VARCHAR(100)") 
	private String keyParam;
    
   // @Transient
	//private SimpleStringProperty keyParamProperty = new SimpleStringProperty(keyParam);
    
    
    @Column(columnDefinition = "VARCHAR(45)") 
	private String cellPosition;
    
    
    @Column(columnDefinition = "VARCHAR(45)") 
	private String dataOwner;
/*    @Column(columnDefinition = "VARCHAR(45)") 
	private String dataType;*/
    
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean populateOnlyHeaders;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean populateAllData = false;
	
	

   // @Column(columnDefinition = "VARCHAR(100)") 
	//private String resultDataTypeDisplay;
	
	
	@Transient
	private boolean populateData = false;
	
	@Transient
	private boolean populateResultValue = false;
	
	@Transient
	private boolean populateLocalResultStatus = false;
	
	@Transient
	private boolean populateMasterResultStatus = false;
	
	@Transient
	private boolean populateHeader1 = false;
	
	@Transient
	private boolean populateHeader2 = false;
	
	@Transient
	private boolean populateHeader3 = false;
	
	@Transient
	private boolean populateHeader4 = false;
	
	@Transient
	private boolean populateHeader5 = false;
	
	@Transient
	private boolean populateHeaderRepeat = false;
	
	@Transient
	private boolean populateHeaderSelfHeat = false;
	
	@Transient
	private boolean populateEachDutInitialRegister = false;
	

	@Transient
	private boolean populateEachDutFinalRegister = false;
	

	@Transient
	private boolean populateEachDutFinalInitialDifference = false;
	

	//@Transient
	//private boolean populateEachDutErrorPercent = false;
	

	@Transient
	private boolean populateEachDutPulseCount = false;
	

	@Transient
	private boolean populateEachDutOnePulsePeriod = false;
	

	@Transient
	private boolean populateEachDutAverageValue = false;
	

	@Transient
	private boolean populateEachDutAverageStatus = false;

	@Transient
	private boolean populateHeaderRsmInitial = false;
	

	@Transient
	private boolean populateHeaderRsmFinal = false;
	
	@Transient
	private boolean populateHeaderRsmDifference = false;
	
	
	
	
	@Transient
	private boolean populateHeaderTestPeriodInMinutes = false;
	
	@Transient
	private boolean populateHeaderWarmupPeriodInMinutes = false;
	
	@Transient
	private boolean populateHeaderTargetVoltage = false;
	
	@Transient
	private boolean populateHeaderTargetCurrent = false;
	
	@Transient
	private boolean populateHeaderTargetPf = false;
	
	@Transient
	private boolean populateHeaderTargetFreq = false;
	
	@Transient
	private boolean populateHeaderTargetEnergy = false;
	
	
	@Transient
	private boolean populateHeaderUpperLimit = false;
	
	@Transient
	private boolean populateHeaderLowerLimit = false;
	
	@Transient
	private boolean populateHeaderMergedLimit= false;
	
	@Transient
	private String headerValue = "";
	
	
	@Transient
	private String testExecutionResultTypeSelected = ConstantReportV2.RESULT_SOURCE_TYPE_DISPLAY_ERROR_VALUE;
	
	//@Transient
	//private boolean populateLocal= false;
	
/*
	public RpPrintPosition(){
    	
    }
    
    public RpPrintPosition(String inputKey, String inputCellPosition, String inpDataOwner, String inpDataType){
    	key = inputKey;
    	cellPosition = inputCellPosition;
    	dataOwner = inpDataOwner;
    	dataType = inpDataType;
    }*/
    
    
    public String getTestExecutionResultTypeSelected() {
		return testExecutionResultTypeSelected;
	}
	public void setTestExecutionResultTypeSelected(String testExecutionResultTypeSelected) {
		this.testExecutionResultTypeSelected = testExecutionResultTypeSelected;
	}
	public ReportProfileTestDataFilter getTestDataFilter2() {
		return testDataFilter2;
	}
	public void setTestDataFilter2(ReportProfileTestDataFilter testDataFilter) {
		this.testDataFilter2 = testDataFilter;
	}
	public String getKeyParam() {
		return keyParam;
	}
	public void setKeyParam(String key) {
		this.keyParam = key;
	}
	public String getCellPosition() {
		return cellPosition;
	}
	public void setCellPosition(String cellPosition) {
		this.cellPosition = cellPosition;
	}

	public String getDataOwner() {
		return dataOwner;
	}

	public void setDataOwner(String dataOwner) {
		this.dataOwner = dataOwner;
	}

/*	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}*/
	public Boolean isPopulateOnlyHeaders() {
		return populateOnlyHeaders;
	}
	public void setPopulateOnlyHeaders(Boolean populateOnlyHeaders) {
		this.populateOnlyHeaders = populateOnlyHeaders;
	}
	public Boolean isPopulateAllData() {
		return populateAllData;
	}
	public void setPopulateAllData(Boolean populateAllData) {
		this.populateAllData = populateAllData;
	}
	public boolean isPopulateData() {
		//populateData = false;
		if(isPopulateAllData()){
			populateData = true;
		}else if(isPopulateOnlyHeaders()){
			populateData = true;
		}else{
			populateData = false;
		}
		
		return populateData;
	}
	public void setPopulateData(boolean populateData) {
		this.populateData = populateData;
	}
	public boolean isPopulateResultValue() {
		return populateResultValue;
	}
	public void setPopulateResultValue(boolean populateResultValue) {
		this.populateResultValue = populateResultValue;
	}
	public boolean isPopulateLocalResultStatus() {
		return populateLocalResultStatus;
	}
	public void setPopulateLocalResultStatus(boolean populateResultStatus) {
		this.populateLocalResultStatus = populateResultStatus;
	}
	public boolean isPopulateHeader1() {
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
	}
	public boolean isPopulateHeaderUpperLimit() {
		return populateHeaderUpperLimit;
	}
	public void setPopulateHeaderUpperLimit(boolean populateHeaderUpperLimit) {
		this.populateHeaderUpperLimit = populateHeaderUpperLimit;
	}
	public boolean isPopulateHeaderLowerLimit() {
		return populateHeaderLowerLimit;
	}
	public void setPopulateHeaderLowerLimit(boolean populateHeaderLowerLimit) {
		this.populateHeaderLowerLimit = populateHeaderLowerLimit;
	}
	public boolean isPopulateHeaderMergedLimit() {
		return populateHeaderMergedLimit;
	}
	public void setPopulateHeaderMergedLimit(boolean populateHeaderMergedLimit) {
		this.populateHeaderMergedLimit = populateHeaderMergedLimit;
	}
	public boolean isPopulateMasterResultStatus() {
		return populateMasterResultStatus;
	}
	public void setPopulateMasterResultStatus(boolean populateMasterResultStatus) {
		this.populateMasterResultStatus = populateMasterResultStatus;
	}
/*	public SimpleStringProperty getKeyParamProperty() {
		return keyParamProperty;
	}
	public void setKeyParamProperty(SimpleStringProperty keyParamProperty) {
		this.keyParamProperty = keyParamProperty;
	}*/
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isPopulateHeaderTestPeriodInMinutes() {
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
	public boolean isPopulateHeaderTargetVoltage() {
		return populateHeaderTargetVoltage;
	}
	public void setPopulateHeaderTargetVoltage(boolean populateHeaderTargetVoltage) {
		this.populateHeaderTargetVoltage = populateHeaderTargetVoltage;
	}
	public boolean isPopulateHeaderTargetCurrent() {
		return populateHeaderTargetCurrent;
	}
	public void setPopulateHeaderTargetCurrent(boolean populateHeaderTargetCurrent) {
		this.populateHeaderTargetCurrent = populateHeaderTargetCurrent;
	}
	public boolean isPopulateHeaderTargetPf() {
		return populateHeaderTargetPf;
	}
	public void setPopulateHeaderTargetPf(boolean populateHeaderTargetPf) {
		this.populateHeaderTargetPf = populateHeaderTargetPf;
	}
	public boolean isPopulateHeaderTargetFreq() {
		return populateHeaderTargetFreq;
	}
	public void setPopulateHeaderTargetFreq(boolean populateHeaderTargetFreq) {
		this.populateHeaderTargetFreq = populateHeaderTargetFreq;
	}
	public boolean isPopulateHeaderTargetEnergy() {
		return populateHeaderTargetEnergy;
	}
	public void setPopulateHeaderTargetEnergy(boolean populateHeaderTargetEnergy) {
		this.populateHeaderTargetEnergy = populateHeaderTargetEnergy;
	}
	public boolean isPopulateHeaderRsmInitial() {
		return populateHeaderRsmInitial;
	}
	public void setPopulateHeaderRsmInitial(boolean populateHeaderRsmInitial) {
		this.populateHeaderRsmInitial = populateHeaderRsmInitial;
	}
	public boolean isPopulateHeaderRsmFinal() {
		return populateHeaderRsmFinal;
	}
	public void setPopulateHeaderRsmFinal(boolean populateHeaderRsmFinal) {
		this.populateHeaderRsmFinal = populateHeaderRsmFinal;
	}
	public boolean isPopulateHeader4() {
		return populateHeader4;
	}
	public void setPopulateHeader4(boolean populateHeader4) {
		this.populateHeader4 = populateHeader4;
	}
	public boolean isPopulateHeader5() {
		return populateHeader5;
	}
	public void setPopulateHeader5(boolean populateHeader5) {
		this.populateHeader5 = populateHeader5;
	}
	public boolean isPopulateEachDutInitialRegister() {
		return populateEachDutInitialRegister;
	}
	public void setPopulateEachDutInitialRegister(boolean populateEachDutInitialRegister) {
		this.populateEachDutInitialRegister = populateEachDutInitialRegister;
	}
	public boolean isPopulateEachDutFinalRegister() {
		return populateEachDutFinalRegister;
	}
	public void setPopulateEachDutFinalRegister(boolean populateEachDutFinalRegister) {
		this.populateEachDutFinalRegister = populateEachDutFinalRegister;
	}
	public boolean isPopulateEachDutFinalInitialDifference() {
		return populateEachDutFinalInitialDifference;
	}
	public void setPopulateEachDutFinalInitialDifference(boolean populateEachDutFinalInitialDifference) {
		this.populateEachDutFinalInitialDifference = populateEachDutFinalInitialDifference;
	}
	/*public boolean isPopulateEachDutErrorPercent() {
		return populateEachDutErrorPercent;
	}
	public void setPopulateEachDutErrorPercent(boolean populateEachDutErrorPercent) {
		this.populateEachDutErrorPercent = populateEachDutErrorPercent;
	}*/
	public boolean isPopulateEachDutPulseCount() {
		return populateEachDutPulseCount;
	}
	public void setPopulateEachDutPulseCount(boolean populateEachDutPulseCount) {
		this.populateEachDutPulseCount = populateEachDutPulseCount;
	}
	public boolean isPopulateEachDutOnePulsePeriod() {
		return populateEachDutOnePulsePeriod;
	}
	public void setPopulateEachDutOnePulsePeriod(boolean populateEachDutOnePulsePeriod) {
		this.populateEachDutOnePulsePeriod = populateEachDutOnePulsePeriod;
	}
	public boolean isPopulateEachDutAverageValue() {
		return populateEachDutAverageValue;
	}
	public void setPopulateEachDutAverageValue(boolean populateEachDutAverage) {
		this.populateEachDutAverageValue = populateEachDutAverage;
	}
	public boolean isPopulateHeaderRepeat() {
		return populateHeaderRepeat;
	}
	public void setPopulateHeaderRepeat(boolean populateHeaderRepeat) {
		this.populateHeaderRepeat = populateHeaderRepeat;
	}
	public boolean isPopulateHeaderSelfHeat() {
		return populateHeaderSelfHeat;
	}
	public void setPopulateHeaderSelfHeat(boolean populateHeaderSelfHeat) {
		this.populateHeaderSelfHeat = populateHeaderSelfHeat;
	}
	public boolean isPopulateEachDutAverageStatus() {
		return populateEachDutAverageStatus;
	}
	public void setPopulateEachDutAverageStatus(boolean populateEachDutAverageStatus) {
		this.populateEachDutAverageStatus = populateEachDutAverageStatus;
	}
	public boolean isPopulateHeaderRsmDifference() {
		return populateHeaderRsmDifference;
	}
	public void setPopulateHeaderRsmDifference(boolean populateHeaderRsmDifference) {
		this.populateHeaderRsmDifference = populateHeaderRsmDifference;
	}
	public String getHeaderValue() {
		return headerValue;
	}
	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}
	
/*	public String getResultDataTypeDisplay() {
		return resultDataTypeDisplay;
	}
	public void setResultDataTypeDisplay(String resultDataTypeDisplay) {
		this.resultDataTypeDisplay = resultDataTypeDisplay;
	}    */
    
}
