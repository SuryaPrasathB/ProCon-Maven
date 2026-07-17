package com.tasnetwork.calibration.energymeter.custom1report;


import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ExcelReportMeterDataCellPositionPage {

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
	
	@SerializedName("CellStartPositionModelNo")
	@Expose
	private String cellStartPositionModelNo;
	@SerializedName("CellStartPositionCapacity")
	@Expose
	private String cellStartPositionCapacity;
	@SerializedName("CellStartPositionCustomerRefNo")
	@Expose
	private String cellStartPositionCustomerRefNo;
	@SerializedName("CellStartPositionDutOverAllStatus")
	@Expose
	private String cellStartPositionDutOverAllStatus;
	
	@SerializedName("PageNumber")
	@Expose
	private int pageNumber = 1;
	
	@SerializedName("CellStartPositionMeterConstant")
	@Expose
	private String cellStartPositionMeterConstant;
	
	@SerializedName("CellStartPositionPtRatio")
	@Expose
	private String cellStartPositionPtRatio;
	
	
	@SerializedName("CellStartPositionCtRatio")
	@Expose
	private String cellStartPositionCtRatio;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

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
	
	public String getCellStartPositionModelNo() {
		return cellStartPositionModelNo;
	}

	public void setCellStartPositionModelNo(String cellStartPositionModelNo) {
		this.cellStartPositionModelNo = cellStartPositionModelNo;
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

}