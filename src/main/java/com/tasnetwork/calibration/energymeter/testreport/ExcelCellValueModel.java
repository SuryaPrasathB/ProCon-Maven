package com.tasnetwork.calibration.energymeter.testreport;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ExcelCellValueModel {
	
	
	private final StringProperty header;
	private final StringProperty cell_value;
    
	public ExcelCellValueModel(String inp_header, String inp_cell_value) {
		
		this.header  = new SimpleStringProperty(inp_header);
		this.cell_value  = new SimpleStringProperty(inp_cell_value);
	}
	
	public StringProperty headerProperty() {
		return header;
	}
	
	public String getheader() {
		return header.get();
	}
	
	public void setheader(String header_Value) {
		this.header.set(header_Value);
	}
	
	public StringProperty cell_valueProperty() {
		return cell_value;
	}
	
	public String getcell_value() {
		return cell_value.get();
	}
	
	public void setcell_value(String cellValue) {
		this.cell_value.set(cellValue);
	}
/*	private final StringProperty header;
	private final StringProperty cell_value;
    
	public ExcelCellValueModel(String inp_header, String inp_cell_value) {
		
		this.header  = new SimpleStringProperty(inp_header);
		this.cell_value  = new SimpleStringProperty(inp_cell_value);
	}
	
	public StringProperty headerProperty() {
		return header;
	}
	
	public String getHeader() {
		return header.get();
	}
	
	public void setHeader(String header_Value) {
		this.header.set(header_Value);
	}
	
	public StringProperty cellPositionProperty() {
		return cell_value;
	}
	
	public String getCellPosition() {
		return cell_value.get();
	}
	
	public void setCellPosition(String cellValue) {
		this.cell_value.set(cellValue);
	}*/
	

}