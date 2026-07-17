package com.tasnetwork.calibration.energymeter.custom1report;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ExcelReportMeterDataDisplayPage {

	@SerializedName("PopulateSerialNo")
	@Expose
	private Boolean populateSerialNo;
	@SerializedName("PopulateMeterSerialNo")
	@Expose
	private Boolean populateMeterSerialNo;
	@SerializedName("PopulateMeterType")
	@Expose
	private Boolean populateMeterType;
	@SerializedName("PopulateMake")
	@Expose
	private Boolean populateMake;
	@SerializedName("PopulateModelNo")
	@Expose
	private Boolean populateModelNo;
	@SerializedName("PopulateCapacity")
	@Expose
	private Boolean populateCapacity;
	@SerializedName("PopulateCustomerRefNo")
	@Expose
	private Boolean populateCustomerRefNo;
	@SerializedName("PopulateDutOverAllStatus")
	@Expose
	private Boolean populateDutOverAllStatus;
	@SerializedName("AppendMeterSerialNoAndRackPosition")
	@Expose
	private Boolean appendMeterSerialNoAndRackPosition;

	
	
	
	@SerializedName("PopulateMeterTypeForEach")
	@Expose
	private Boolean populateMeterTypeForEach = false;
	
	@SerializedName("PopulateMakeForEach")
	@Expose
	private Boolean populateMakeForEach = false;
	
	@SerializedName("PopulateModelNoForEach")
	@Expose
	private Boolean populateModelNoForEach = false;
	
	@SerializedName("PopulateCapacityForEach")
	@Expose
	private Boolean populateCapacityForEach = false;
	
	@SerializedName("PopulateCustomerRefNoForEach")
	@Expose
	private Boolean populateCustomerRefNoForEach = false;
	
	@SerializedName("PageNumber")
	@Expose
	private int pageNumber = 1;
	
	
	
	@SerializedName("PopulateMeterConstant")
	@Expose
	private Boolean populateMeterConstant;
	
	@SerializedName("PopulateMeterConstantForEach")
	@Expose
	private Boolean populateMeterConstantForEach;
	
	
	@SerializedName("PopulatePtRatio")
	@Expose
	private Boolean populatePtRatio;
	
	@SerializedName("PopulatePtRatioForEach")
	@Expose
	private Boolean populatePtRatioForEach;
	
	
	@SerializedName("PopulateCtRatio")
	@Expose
	private Boolean populateCtRatio;
	
	@SerializedName("PopulateCtRatioForEach")
	@Expose
	private Boolean populateCtRatioForEach;
	
	

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Boolean getPopulateSerialNo() {
		return populateSerialNo;
	}

	public void setPopulateSerialNo(Boolean populateSerialNo) {
		this.populateSerialNo = populateSerialNo;
	}

	public Boolean getPopulateMeterSerialNo() {
		return populateMeterSerialNo;
	}

	public void setPopulateMeterSerialNo(Boolean populateMeterSerialNo) {
		this.populateMeterSerialNo = populateMeterSerialNo;
	}

	public Boolean getPopulateMeterType() {
		return populateMeterType;
	}

	public void setPopulateMeterType(Boolean populateMeterType) {
		this.populateMeterType = populateMeterType;
	}

	public Boolean getPopulateMake() {
		return populateMake;
	}

	public void setPopulateMake(Boolean populateMake) {
		this.populateMake = populateMake;
	}
	
	public Boolean getPopulateModelNo() {
		return populateModelNo;
	}

	public void setPopulateModelNo(Boolean populateModelNo) {
		this.populateModelNo = populateModelNo;
	}

	public Boolean getPopulateCapacity() {
		return populateCapacity;
	}

	public void setPopulateCapacity(Boolean populateCapacity) {
		this.populateCapacity = populateCapacity;
	}

	public Boolean getPopulateCustomerRefNo() {
		return populateCustomerRefNo;
	}

	public void setPopulateCustomerRefNo(Boolean populateCustomerRefNo) {
		this.populateCustomerRefNo = populateCustomerRefNo;
	}

	public Boolean getPopulateDutOverAllStatus() {
		return populateDutOverAllStatus;
	}

	public void setPopulateDutOverAllStatus(Boolean populateDutOverAllStatus) {
		this.populateDutOverAllStatus = populateDutOverAllStatus;
	}

	public Boolean getAppendMeterSerialNoAndRackPosition() {
		return appendMeterSerialNoAndRackPosition;
	}

	public void setAppendMeterSerialNoAndRackPosition(Boolean appendMeterSerialNoAndRackPosition) {
		this.appendMeterSerialNoAndRackPosition = appendMeterSerialNoAndRackPosition;
	}
	
	public Boolean getPopulateMeterTypeForEach() {
		return populateMeterTypeForEach;
	}

	public void setPopulateMeterTypeForEach(Boolean populateMeterTypeForEach) {
		this.populateMeterTypeForEach = populateMeterTypeForEach;
	}

	public Boolean getPopulateMakeForEach() {
		return populateMakeForEach;
	}

	public void setPopulateMakeForEach(Boolean populateMakeForEach) {
		this.populateMakeForEach = populateMakeForEach;
	}
	
	
	
	public Boolean getPopulateModelNoForEach() {
		return populateModelNoForEach;
	}

	public void setPopulateModelNoForEach(Boolean populateModelNoForEach) {
		this.populateModelNoForEach = populateModelNoForEach;
	}

	public Boolean getPopulateCapacityForEach() {
		return populateCapacityForEach;
	}

	public void setPopulateCapacityForEach(Boolean populateCapacityForEach) {
		this.populateCapacityForEach = populateCapacityForEach;
	}

	public Boolean getPopulateCustomerRefNoForEach() {
		return populateCustomerRefNoForEach;
	}

	public void setPopulateCustomerRefNoForEach(Boolean populateCustomerRefNoForEach) {
		this.populateCustomerRefNoForEach = populateCustomerRefNoForEach;
	}

	public Boolean getPopulateMeterConstant() {
		return populateMeterConstant;
	}

	public void setPopulateMeterConstant(Boolean populateMeterConstant) {
		this.populateMeterConstant = populateMeterConstant;
	}

	public Boolean getPopulateMeterConstantForEach() {
		return populateMeterConstantForEach;
	}

	public void setPopulateMeterConstantForEach(Boolean populateMeterConstantForEach) {
		this.populateMeterConstantForEach = populateMeterConstantForEach;
	}

	public Boolean getPopulatePtRatio() {
		return populatePtRatio;
	}

	public void setPopulatePtRatio(Boolean populatePtRatio) {
		this.populatePtRatio = populatePtRatio;
	}

	public Boolean getPopulatePtRatioForEach() {
		return populatePtRatioForEach;
	}

	public void setPopulatePtRatioForEach(Boolean populatePtRatioForEach) {
		this.populatePtRatioForEach = populatePtRatioForEach;
	}

	public Boolean getPopulateCtRatio() {
		return populateCtRatio;
	}

	public void setPopulateCtRatio(Boolean populateCtRatio) {
		this.populateCtRatio = populateCtRatio;
	}

	public Boolean getPopulateCtRatioForEach() {
		return populateCtRatioForEach;
	}

	public void setPopulateCtRatioForEach(Boolean populateCtRatioForEach) {
		this.populateCtRatioForEach = populateCtRatioForEach;
	}

}