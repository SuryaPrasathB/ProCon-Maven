package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;
import java.util.Arrays;

import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.deployment.DeploymentDataModel;
import com.tasnetwork.calibration.energymeter.testreport.ReportMeterMetaDataTypeSubModel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReportFilterPopulateDataCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<ReportMeterMetaDataTypeSubModel, CheckBox>, ObservableValue<CheckBox>> {
	//ArrayList<String> checkBoxDisableList = new ArrayList<String> (Arrays.asList(ConstantReportV2.REPORT_SERIAL_NO,
	//	ConstantReportV2.REPORT_METER_SERIAL_NO));
	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<ReportMeterMetaDataTypeSubModel, CheckBox> param) {
		ReportMeterMetaDataTypeSubModel rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		//checkBox.setText("Populate");
		//checkBox.setAlignment(Pos.CENTER);

		//setAlignment(Pos.CENTER);
		// setGraphic(checkBox);
		checkBox.selectedProperty().setValue(rowData.getPopulateData());
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setPopulateData(new_val);
		});

		if(rowData.getDataTypeKey().equals(ConstantReportV2.REPORT_META_DATATYPE_DUT_SERIAL_NO)){
			checkBox.setSelected(true);
			checkBox.setDisable(true);
		}

		return new SimpleObjectProperty<>(checkBox);
	}
}
