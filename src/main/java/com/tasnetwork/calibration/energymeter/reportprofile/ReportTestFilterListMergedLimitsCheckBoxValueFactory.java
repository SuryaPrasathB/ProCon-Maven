package com.tasnetwork.calibration.energymeter.reportprofile;

import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReportTestFilterListMergedLimitsCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<ReportProfileTestDataFilter, CheckBox>, ObservableValue<CheckBox>> {
	//ArrayList<String> checkBoxDisableList = new ArrayList<String> (Arrays.asList(ConstantReportV2.REPORT_SERIAL_NO,
	//	ConstantReportV2.REPORT_METER_SERIAL_NO));
	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<ReportProfileTestDataFilter, CheckBox> param) {
		ReportProfileTestDataFilter rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		//checkBox.setText("Populate");
		//checkBox.setAlignment(Pos.CENTER);

		//setAlignment(Pos.CENTER);
		// setGraphic(checkBox);
		checkBox.selectedProperty().setValue(rowData.isOperationMergeLimitsSelected());
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setOperationMergeLimitsSelected(new_val);
		});

		//if(rowData.getDataTypeKey().equals(ConstantReportV2.REPORT_METER_SERIAL_NO)){
		//	checkBox.setSelected(true);
		checkBox.setDisable(true);
		//}

		return new SimpleObjectProperty<>(checkBox);
	}
}
