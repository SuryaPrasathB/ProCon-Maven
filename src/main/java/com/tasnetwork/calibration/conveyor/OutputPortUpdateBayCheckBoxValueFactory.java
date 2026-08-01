package com.tasnetwork.calibration.conveyor;

import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class OutputPortUpdateBayCheckBoxValueFactory
		implements Callback<TableColumn.CellDataFeatures<OutputPort, CheckBox>, ObservableValue<CheckBox>> {

	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<OutputPort, CheckBox> param) {
		OutputPort rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		checkBox.selectedProperty().setValue(rowData.isUpdateBay());
		// checkBox.setAlignment(Pos.CENTER);
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setUpdateBay(new_val);
		});

		return new SimpleObjectProperty<>(checkBox);
	}
}
