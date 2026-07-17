package com.tasnetwork.calibration.energymeter.testreport;

import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReportMeterMetaDataTypeSubModel {
	
	private final StringProperty serialNo;
	private final StringProperty psuedoSerialNo;
	private final BooleanProperty populateData;
	private final BooleanProperty populateDataForEachDut;
	private final BooleanProperty populateOnlyHeader;
	private final StringProperty dataTypeKey;
	private final StringProperty dataCellPosition;
	private final IntegerProperty pageNumber;
	private final StringProperty pageName;

	private boolean discardRackPositionInDutSerialNumber = false;
	private boolean filterActive = false;
	
	private String populateDataSelection = ConstantReportV2.NONE_DISPLAYED;

    
	public ReportMeterMetaDataTypeSubModel(String sNo, 
			String dataTypeKey , Boolean populateData, Boolean populateDataForEachDut, Boolean populateOnlyHeader,
			String dataCellPosition,String pageNumber,String pageName){//, Boolean discardRackPositionInDutSerialNumber) {
		
		this.serialNo = new SimpleStringProperty(sNo);
		this.psuedoSerialNo = new SimpleStringProperty(sNo);
		this.dataTypeKey = new SimpleStringProperty(dataTypeKey );
		this.dataCellPosition = new SimpleStringProperty(dataCellPosition );
		if(pageNumber.isEmpty()){
			this.pageNumber = new SimpleIntegerProperty(0);
		}else{
			this.pageNumber = new SimpleIntegerProperty(Integer.parseInt(pageNumber) );
		}
		this.populateData = new SimpleBooleanProperty(populateData);
		this.populateDataForEachDut = new SimpleBooleanProperty(populateDataForEachDut);
		this.populateOnlyHeader = new SimpleBooleanProperty(populateOnlyHeader);
		this.pageName = new SimpleStringProperty(pageName);
		//this.discardRackPositionInDutSerialNumber= new SimpleBooleanProperty(discardRackPositionInDutSerialNumber);

	}
	
	public StringProperty getSerialNoProperty() {
		return serialNo;
	}
	
	public String getSerialNo() {
		return serialNo.get();
	}
	
	public void setSerialNo(String serialNo) {
		this.serialNo.set(serialNo);
	}
	
	
	
	
	public StringProperty getPsuedoSerialNoProperty() {
		return psuedoSerialNo;
	}
	
	public String getPsuedoSerialNo() {
		return psuedoSerialNo.get();
	}
	
	public void setPsuedoSerialNo(String serialNo) {
		this.psuedoSerialNo.set(serialNo);
	}
	
	
	public String getPageName() {
		return pageName.get();
	}
	
	public void setPageName(String pageName) {
		this.pageName.set(pageName);
	}
	
	public StringProperty getDataTypeKeyProperty() {
		return dataTypeKey;
	}
	
	public String getDataTypeKey() {
		return dataTypeKey.get();
	}

	public void setDataTypeKey(String dataTypeKey) {
		this.dataTypeKey.set(dataTypeKey);
	}
	
	
	
	public StringProperty getDataCellPositionProperty() {
		return dataCellPosition;
	}
	
	public String getDataCellPosition() {
		return dataCellPosition.get();
	}

	public void setDataCellPosition(String dataCellPosition) {
		this.dataCellPosition.set(dataCellPosition);
	}
	
	
		
	
	public BooleanProperty isPopulateDataProperty() {
		return populateData;
	}
	public Boolean isPopulateData() {
		return populateData.get();
	}
	
	public Boolean getPopulateData() {
		return populateData.get();
	}

	public void setPopulateData(Boolean selected) {
		this.populateData.set(selected);
	}
	
	
	
	public BooleanProperty isPopulateOnlyHeaderProperty() {
		return populateOnlyHeader;
	}
	public Boolean isPopulateOnlyHeader() {
		return populateOnlyHeader.get();
	}
	
	public Boolean getPopulateOnlyHeader() {
		return populateOnlyHeader.get();
	}

	public void setPopulateOnlyHeader(Boolean populateOnlyHeader) {
		this.populateOnlyHeader.set(populateOnlyHeader);
	}
	
	
	
	
	
	
	
/*	public BooleanProperty getDiscardRackPositionInDutSerialNumberProperty() {
		return discardRackPositionInDutSerialNumber;
	}
	public Boolean isDiscardRackPositionInDutSerialNumber() {
		return discardRackPositionInDutSerialNumber.get();
	}


	public void setDiscardRackPositionInDutSerialNumber(Boolean populateOnlyHeader) {
		this.discardRackPositionInDutSerialNumber.set(populateOnlyHeader);
	}*/
	
	
	
	
	public BooleanProperty isPopulateDataForEachDutProperty() {
		return populateDataForEachDut;
	}
	public Boolean isPopulateDataForEachDut() {
		return populateDataForEachDut.get();
	}
	
	public Boolean getPopulateDataForEachDut() {
		return populateDataForEachDut.get();
	}

	public void setPopulateDataForEachDut(Boolean populateDataForEachDut) {
		this.populateDataForEachDut.set(populateDataForEachDut);
	}

	public IntegerProperty getPageNumberProperty() {
		return  pageNumber;
	}
		
	public int getPageNumberInt() {
		return pageNumber.get();
	}
	
	public String getPageNumber() {
		return String.valueOf(pageNumber.get());
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber.set(pageNumber);
	}

	public boolean isDiscardRackPositionInDutSerialNumber() {
		return discardRackPositionInDutSerialNumber;
	}

	public void setDiscardRackPositionInDutSerialNumber(boolean discardRackPositionInDutSerialNumber) {
		this.discardRackPositionInDutSerialNumber = discardRackPositionInDutSerialNumber;
	}

	public boolean isFilterActive() {
		return filterActive;
	}

	public void setFilterActive(boolean filterActive) {
		this.filterActive = filterActive;
	}

	public String getPopulateDataSelection() {
		return populateDataSelection;
	}

	public void setPopulateDataSelection(String populateDataSelection) {
		this.populateDataSelection = populateDataSelection;
	}


}
