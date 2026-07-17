package com.tasnetwork.calibration.energymeter.testreport;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.constant.ConstantReport;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class MeterSelectionController implements Initializable{

	@FXML
	private ComboBox<String> cmbBoxMeterList;
	 
	static  ComboBox<String> ref_cmbBoxMeterList;
	
	private static volatile boolean  screenDisplayed = true;

	private static  ArrayList<String> meterListData = new ArrayList<String>();
	
	private static  String selectedMeterSerialNo = "";



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ref_cmbBoxMeterList = cmbBoxMeterList;
		//meterListData.add("TestSerial");
		updateMeterListGUI(getMeterListData());
		
	}
	
	public static void updateMeterListData(ArrayList<String> meterList){
		
		clearMeterListData();
		setMeterListData(meterList);
		
	}
	
	public void updateMeterListGUI(ArrayList<String> meterList){
		//ref_cmbBoxMeterList.getSelectionModel().clearAndSelect(0);
		
		Platform.runLater(() -> {
	
			ref_cmbBoxMeterList.getItems().clear();
			ref_cmbBoxMeterList.getItems().add(ConstantReport.REPORT_SELECTED_ALL_METERS);
			ref_cmbBoxMeterList.getItems().addAll(meterList);
			ref_cmbBoxMeterList.getSelectionModel().select(0);
		
		});
	}
	
	public static boolean isScreenDisplayed() {
		return screenDisplayed;
	}


	public static void setScreenDisplayed(boolean screenDisplayed) {
		MeterSelectionController.screenDisplayed = screenDisplayed;
	}
	
	@FXML
	public void btnCancelClick(){
		TestReportController.meterSelectionStage.getScene().getWindow().hide();
		setSelectedMeterSerialNo(ConstantReport.REPORT_SELECTED_ALL_METERS);
		setScreenDisplayed(false);
	}
	
	@FXML
	public void btnOkClick(){
		TestReportController.meterSelectionStage.getScene().getWindow().hide();
		setSelectedMeterSerialNo(ref_cmbBoxMeterList.getSelectionModel().getSelectedItem().toString());
		setScreenDisplayed(false);
	}

	public static String getSelectedMeterSerialNo() {
		return selectedMeterSerialNo;
	}

	public static void setSelectedMeterSerialNo(String selectedMeterSerialNo) {
		MeterSelectionController.selectedMeterSerialNo = selectedMeterSerialNo;
	}

	public static ArrayList<String> getMeterListData() {
		return meterListData;
	}

	public static void setMeterListData(ArrayList<String> meterListData) {
		MeterSelectionController.meterListData = meterListData;
	}
	
	public static void clearMeterListData() {
		MeterSelectionController.meterListData.clear();
	}

}
