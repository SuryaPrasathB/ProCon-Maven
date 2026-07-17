package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;
import java.util.Arrays;

import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.testreport.ReportMeterMetaDataTypeSubModel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReportFilterPopulateEachDutCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<ReportMeterMetaDataTypeSubModel, CheckBox>, ObservableValue<CheckBox>> {
	   
		ArrayList<String> checkBoxAutoSelectionDisableList = new ArrayList<String> (Arrays.asList(ConstantReportV2.REPORT_META_DATATYPE_SERIAL_NO,
				ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO));
		
		ArrayList<String> checkBoxDisableList = new ArrayList<String> (Arrays.asList("Execution Time Stamp", "Execution Date", "Execution Time",
				"Report Gen Time Stamp", "Report Gen Date", "Report Gen Time",
				"Approved Time Stamp", "Approved Date", "Approved Time",
				"Tested by","Witnessed by","Approved by",
				"Page No","Max No Of Pages"));
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<ReportMeterMetaDataTypeSubModel, CheckBox> param) {
			ReportMeterMetaDataTypeSubModel rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        //checkBox.setText("Populate");
	        checkBox.selectedProperty().setValue(rowData.getPopulateDataForEachDut());
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setPopulateDataForEachDut(new_val);
	        });
	        if(checkBoxAutoSelectionDisableList.contains(rowData.getDataTypeKey())) {
	        	checkBox.setDisable(true);
	        	checkBox.setSelected(true);
	        }
	        
	        if(checkBoxDisableList.contains(rowData.getDataTypeKey())) {
	        	checkBox.setDisable(true);
	        }
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }
}
