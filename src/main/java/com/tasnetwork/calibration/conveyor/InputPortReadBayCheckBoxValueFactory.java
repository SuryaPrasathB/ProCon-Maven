package com.tasnetwork.calibration.conveyor;

import com.tasnetwork.calibration.conveyor.bay.configloader.InputPort;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class InputPortReadBayCheckBoxValueFactory
		implements Callback<TableColumn.CellDataFeatures<InputPort, CheckBox>, ObservableValue<CheckBox>> {

	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<InputPort, CheckBox> param) {
		InputPort rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		checkBox.selectedProperty().setValue(rowData.isReadBay());
		// checkBox.setAlignment(Pos.CENTER);
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setReadBay(new_val);

		});

		return new SimpleObjectProperty<>(checkBox);
	}
}
