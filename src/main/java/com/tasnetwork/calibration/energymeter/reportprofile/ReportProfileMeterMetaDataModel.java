package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.energymeter.testreport.ReportMeterMetaDataTypeSubModel;

public class ReportProfileMeterMetaDataModel {
/*	@SerializedName("PopulateSerialNo")
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
	@SerializedName("PopulateCapacity")
	@Expose
	private Boolean populateCapacity;
	@SerializedName("PopulateCustomerRefNo")
	@Expose
	private Boolean populateCustomerRefNo;
	@SerializedName("PopulateDutOverAllStatus")
	@Expose
	private Boolean populateDutOverAllStatus;
	//@SerializedName("AppendMeterSerialNoAndRackPosition")
	//@Expose
	//private Boolean appendMeterSerialNoAndRackPosition;

	
	
	
	@SerializedName("PopulateMeterTypeForEach")
	@Expose
	private Boolean populateMeterTypeForEach = false;
	
	@SerializedName("PopulateMakeForEach")
	@Expose
	private Boolean populateMakeForEach = false;
	
	@SerializedName("PopulateCapacityForEach")
	@Expose
	private Boolean populateCapacityForEach = false;
	
	@SerializedName("PopulateCustomerRefNoForEach")
	@Expose
	private Boolean populateCustomerRefNoForEach = false;*/
	
	@SerializedName("PageNumber")
	@Expose
	private String pageNumber = "1";
	
	private String pageName = "";
	
/*	@SerializedName("PopulateMeterConstant")
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
	
	
	////////////////////////////////////////////////
	
	@SerializedName("CellStartPositionSerialNo")
	@Expose
	private String cellStartPositionSerialNo;
	@SerializedName("CellStartPositionMeterSerialNo")
	@Expose
	private String cellStartPositionMeterSerialNo;
	@SerializedName("CellStartPositionMeterType")
	@Expose
	private String cellStartPositionMeterType;
	@SerializedName("CellStartPositionMake")
	@Expose
	private String cellStartPositionMake;
	@SerializedName("CellStartPositionCapacity")
	@Expose
	private String cellStartPositionCapacity;
	@SerializedName("CellStartPositionCustomerRefNo")
	@Expose
	private String cellStartPositionCustomerRefNo;
	@SerializedName("CellStartPositionDutOverAllStatus")
	@Expose
	private String cellStartPositionDutOverAllStatus;
	
	
	@SerializedName("CellStartPositionMeterConstant")
	@Expose
	private String cellStartPositionMeterConstant;
	
	@SerializedName("CellStartPositionPtRatio")
	@Expose
	private String cellStartPositionPtRatio;
	
	
	@SerializedName("CellStartPositionCtRatio")
	@Expose
	private String cellStartPositionCtRatio;

	
	//////////////////////////////
	
	private String serialNo = "";
	private String meterMetaDataType = "";
	private String meterMetaDataCellPosition = "";
	private boolean populateData = false;
	private boolean populateOnlyHeaders = false;
	private boolean populateForEachDut = false;*/
	private boolean appendMeterSerialNoAndRackPosition = false;
	
	private String meterMetaDataPopulateType = "";
	//private String reportBaseTemplate = "";
	private ArrayList<ReportMeterMetaDataTypeSubModel> reportProfileMeterMetaData = new ArrayList<ReportMeterMetaDataTypeSubModel>();
	
	/////////////////////
	
	

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

/*	public Boolean getPopulateSerialNo() {
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
*/
	public Boolean getAppendMeterSerialNoAndRackPosition() {
		return appendMeterSerialNoAndRackPosition;
	}

	public void setAppendMeterSerialNoAndRackPosition(Boolean appendMeterSerialNoAndRackPosition) {
		this.appendMeterSerialNoAndRackPosition = appendMeterSerialNoAndRackPosition;
	}
/*	
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
	}*/
	
	
	///////////////////////////////////
	
/*	
	public String getCellStartPositionSerialNo() {
		return cellStartPositionSerialNo;
	}

	public void setCellStartPositionSerialNo(String cellStartPositionSerialNo) {
		this.cellStartPositionSerialNo = cellStartPositionSerialNo;
	}

	public String getCellStartPositionMeterSerialNo() {
		return cellStartPositionMeterSerialNo;
	}

	public void setCellStartPositionMeterSerialNo(String cellStartPositionMeterSerialNo) {
		this.cellStartPositionMeterSerialNo = cellStartPositionMeterSerialNo;
	}

	public String getCellStartPositionMeterType() {
		return cellStartPositionMeterType;
	}

	public void setCellStartPositionMeterType(String cellStartPositionMeterType) {
		this.cellStartPositionMeterType = cellStartPositionMeterType;
	}

	public String getCellStartPositionMake() {
		return cellStartPositionMake;
	}

	public void setCellStartPositionMake(String cellStartPositionMake) {
		this.cellStartPositionMake = cellStartPositionMake;
	}

	public String getCellStartPositionCapacity() {
		return cellStartPositionCapacity;
	}

	public void setCellStartPositionCapacity(String cellStartPositionCapacity) {
		this.cellStartPositionCapacity = cellStartPositionCapacity;
	}

	public String getCellStartPositionCustomerRefNo() {
		return cellStartPositionCustomerRefNo;
	}

	public void setCellStartPositionCustomerRefNo(String cellStartPositionCustomerRefNo) {
		this.cellStartPositionCustomerRefNo = cellStartPositionCustomerRefNo;
	}

	public String getCellStartPositionDutOverAllStatus() {
		return cellStartPositionDutOverAllStatus;
	}

	public void setCellStartPositionDutOverAllStatus(String cellStartPositionDutOverAllStatus) {
		this.cellStartPositionDutOverAllStatus = cellStartPositionDutOverAllStatus;
	}

	public String getCellStartPositionMeterConstant() {
		return cellStartPositionMeterConstant;
	}

	public void setCellStartPositionMeterConstant(String cellStartPositionMeterConstant) {
		this.cellStartPositionMeterConstant = cellStartPositionMeterConstant;
	}

	public String getCellStartPositionPtRatio() {
		return cellStartPositionPtRatio;
	}

	public void setCellStartPositionPtRatio(String cellStartPositionPtRatio) {
		this.cellStartPositionPtRatio = cellStartPositionPtRatio;
	}

	public String getCellStartPositionCtRatio() {
		return cellStartPositionCtRatio;
	}

	public void setCellStartPositionCtRatio(String cellStartPositionCtRatio) {
		this.cellStartPositionCtRatio = cellStartPositionCtRatio;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getMeterMetaDataType() {
		return meterMetaDataType;
	}

	public void setMeterMetaDataType(String meterMetaDataType) {
		this.meterMetaDataType = meterMetaDataType;
	}

	public String getMeterMetaDataCellPosition() {
		return meterMetaDataCellPosition;
	}

	public void setMeterMetaDataCellPosition(String meterMetaDataCellPosition) {
		this.meterMetaDataCellPosition = meterMetaDataCellPosition;
	}

	public boolean isPopulateData() {
		return populateData;
	}

	public void setPopulateData(boolean populateData) {
		this.populateData = populateData;
	}

	public boolean isPopulateOnlyHeaders() {
		return populateOnlyHeaders;
	}

	public void setPopulateOnlyHeaders(boolean populateOnlyHeaders) {
		this.populateOnlyHeaders = populateOnlyHeaders;
	}

	public boolean isPopulateForEachDut() {
		return populateForEachDut;
	}

	public void setPopulateForEachDut(boolean populateForEachDut) {
		this.populateForEachDut = populateForEachDut;
	}*/

	public String getMeterMetaDataPopulateType() {
		return meterMetaDataPopulateType;
	}

	public void setMeterMetaDataPopulateType(String populateType) {
		this.meterMetaDataPopulateType = populateType;
	}

/*	public String getReportBaseTemplate() {
		return reportBaseTemplate;
	}

	public void setReportBaseTemplate(String reportBaseTemplate) {
		this.reportBaseTemplate = reportBaseTemplate;
	}*/

	public ArrayList<ReportMeterMetaDataTypeSubModel> getReportProfileMeterMetaData() {
		return reportProfileMeterMetaData;
	}

	public void setReportProfileMeterMetaData(ArrayList<ReportMeterMetaDataTypeSubModel> reportProfileMeterMetaData) {
		this.reportProfileMeterMetaData = reportProfileMeterMetaData;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}


}
