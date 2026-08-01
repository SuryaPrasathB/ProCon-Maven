package com.tasnetwork.calibration.conveyor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class PositionLoadedSelectCheckBoxValueFactory
		implements Callback<TableColumn.CellDataFeatures<PositionLoadedModel, CheckBox>, ObservableValue<CheckBox>> {

	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<PositionLoadedModel, CheckBox> param) {
		PositionLoadedModel rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		checkBox.selectedProperty().setValue(rowData.isPositionSelected());
		// checkBox.setAlignment(Pos.CENTER);
		// checkBox.setDisable(true);
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setPositionSelected(new_val);
		});

		return new SimpleObjectProperty<>(checkBox);
	}
}