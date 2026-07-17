package com.tasnetwork.calibration.energymeter.custom1report;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TestTypeFilter {

	@SerializedName("PopulateResult")
	@Expose
	private Boolean populateResult;
	@SerializedName("PrefixType")
	@Expose
	private String prefixType;
	@SerializedName("VoltageFilter")
	@Expose
	private String voltageFilter;
	@SerializedName("CurrentFilter")
	@Expose
	private String currentFilter;
	@SerializedName("PopulateType")
	@Expose
	private String populateType;
	@SerializedName("CellStartResultValuePosition")
	@Expose
	private String cellStartResultValuePosition;
	@SerializedName("CellStartResultStatusPosition")
	@Expose
	private String cellStartResultStatusPosition;
	@SerializedName("PopulateResultStatus")
	@Expose
	private Boolean populateResultStatus;
	@SerializedName("PopulateResultValue")
	@Expose
	private Boolean populateResultValue;
	@SerializedName("PfFilter")
	@Expose
	private String pfFilter;
	
	@SerializedName("ResultIterationId")
	@Expose
	private String resultIterationId = "0";
	
	@SerializedName("EnergyFilter")
	@Expose
	private String energyFilter = "0.0";
	
	@SerializedName("FreqFilter")
	@Expose
	private String freqFilter;
	
	
	//@SerializedName("VoltUnBalancePhaseSelectionFilter")
	//@Expose
	//private String voltUnbalancePhaseSelectionFilter;
	
	
	
	@SerializedName("PopulateResultFilterDutPulseCountReading")
	@Expose
	private boolean populateResultFilterDutPulseCountReading = false;
	
	@SerializedName("PopulateResultFilterDutInitialReading")
	@Expose
	private boolean populateResultFilterDutInitialReading = false;
	
	@SerializedName("PopulateResultFilterDutFinalReading")
	@Expose
	private boolean populateResultFilterDutFinalReading = false;
	
	@SerializedName("PopulateResultFilterRefStdInitialReading")
	@Expose
	private boolean populateResultFilterRefStdInitialReading = false;
	
	@SerializedName("PopulateResultFilterRefStdFinalReading")
	@Expose
	private boolean populateResultFilterRefStdFinalReading = false;
	
	@SerializedName("PageNumber")
	@Expose
	private int pageNumber = 1;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	public Boolean getPopulateResult() {
		return populateResult;
	}

	public void setPopulateResult(Boolean populateResult) {
		this.populateResult = populateResult;
	}

	public String getPrefixType() {
		return prefixType;
	}

	public void setPrefixType(String prefixType) {
		this.prefixType = prefixType;
	}

	public String getVoltageFilter() {
		return voltageFilter;
	}

	public void setVoltageFilter(String voltageFilter) {
		this.voltageFilter = voltageFilter;
	}

	public String getCurrentFilter() {
		return currentFilter;
	}

	public void setCurrentFilter(String currentFilter) {
		this.currentFilter = currentFilter;
	}

	public String getPopulateType() {
		return populateType;
	}

	public void setPopulateType(String populateType) {
		this.populateType = populateType;
	}

	public String getCellStartResultValuePosition() {
		return cellStartResultValuePosition;
	}

	public void setCellStartResultValuePosition(String cellStartResultValuePosition) {
		this.cellStartResultValuePosition = cellStartResultValuePosition;
	}

	public String getCellStartResultStatusPosition() {
		return cellStartResultStatusPosition;
	}

	public void setCellStartResultStatusPosition(String cellStartResultStatusPosition) {
		this.cellStartResultStatusPosition = cellStartResultStatusPosition;
	}

	public Boolean getPopulateResultStatus() {
		return populateResultStatus;
	}

	public void setPopulateResultStatus(Boolean populateResultStatus) {
		this.populateResultStatus = populateResultStatus;
	}

	public Boolean getPopulateResultValue() {
		return populateResultValue;
	}

	public void setPopulateResultValue(Boolean populateResultValue) {
		this.populateResultValue = populateResultValue;
	}

	public String getPfFilter() {
		return pfFilter;
	}

	public void setPfFilter(String pfFilter) {
		this.pfFilter = pfFilter;
	}

	public String getResultIterationId() {
		return resultIterationId;
	}

	public void setResultIterationId(String resultIterationId) {
		this.resultIterationId = resultIterationId;
	}

	public String getEnergyFilter() {
		return energyFilter;
	}

	public void setEnergyFilter(String energyFilter) {
		this.energyFilter = energyFilter;
	}

	public boolean isPopulateResultFilterDutPulseCountReading() {
		return populateResultFilterDutPulseCountReading;
	}

	public void setPopulateResultFilterDutPulseCountReading(boolean populateResultFilterDutPulseCountReading) {
		this.populateResultFilterDutPulseCountReading = populateResultFilterDutPulseCountReading;
	}

	public boolean isPopulateResultFilterDutInitialReading() {
		return populateResultFilterDutInitialReading;
	}

	public void setPopulateResultFilterDutInitialReading(boolean populateResultFilterDutInitialReading) {
		this.populateResultFilterDutInitialReading = populateResultFilterDutInitialReading;
	}

	public boolean isPopulateResultFilterDutFinalReading() {
		return populateResultFilterDutFinalReading;
	}

	public void setPopulateResultFilterDutFinalReading(boolean populateResultFilterDutFinalReading) {
		this.populateResultFilterDutFinalReading = populateResultFilterDutFinalReading;
	}

	public boolean isPopulateResultFilterRefStdInitialReading() {
		return populateResultFilterRefStdInitialReading;
	}

	public void setPopulateResultFilterRefStdInitialReading(boolean populateResultFilterRefStdInitialReading) {
		this.populateResultFilterRefStdInitialReading = populateResultFilterRefStdInitialReading;
	}

	public boolean isPopulateResultFilterRefStdFinalReading() {
		return populateResultFilterRefStdFinalReading;
	}

	public void setPopulateResultFilterRefStdFinalReading(boolean populateResultFilterRefStdFinalReading) {
		this.populateResultFilterRefStdFinalReading = populateResultFilterRefStdFinalReading;
	}

	public String getFreqFilter() {
		return freqFilter;
	}

	public void setFreqFilter(String freqFilter) {
		this.freqFilter = freqFilter;
	}

/*	public String getVoltUnbalancePhaseSelectionFilter() {
		return voltUnbalancePhaseSelectionFilter;
	}

	public void setVoltUnbalancePhaseSelectionFilter(String voltUnbalancePhaseSelectionFilter) {
		this.voltUnbalancePhaseSelectionFilter = voltUnbalancePhaseSelectionFilter;
	}*/



}