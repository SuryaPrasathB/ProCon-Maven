package com.tasnetwork.calibration.conveyor;

import com.tasnetwork.calibration.conveyor.bay.configloader.OutputPort;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class OutputPortActiveCheckBoxValueFactory
		implements Callback<TableColumn.CellDataFeatures<OutputPort, CheckBox>, ObservableValue<CheckBox>> {

	private final OutputPortTableViewRefresher controller;

	public OutputPortActiveCheckBoxValueFactory(OutputPortTableViewRefresher controller) {
		this.controller = controller;
	}

	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<OutputPort, CheckBox> param) {
		OutputPort rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		checkBox.selectedProperty().setValue(rowData.isOutputActive());
		// checkBox.setAlignment(Pos.CENTER);
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setOutputActive(new_val);
			// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
			// new_val : " + new_val);
			// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
			// getOnStateDesc : " + rowData.getOnStateDesc());
			// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
			// getOffStateDesc : " + rowData.getOffStateDesc());
			if (new_val) {
				rowData.setStateDescription(rowData.getOnStateDesc());
				// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
				// getStateDescription-1 : " + rowData.getStateDescription());
			} else {
				rowData.setStateDescription(rowData.getOffStateDesc());
				// ApplicationLauncher.logger.debug("OutputPortActiveCheckBoxValueFactory :
				// getStateDescription-12: " + rowData.getStateDescription());

			}
			// BayTestController.ref_tbViewOutputPortData.refresh();
			controller.refreshOutputPortTable();
		});

		return new SimpleObjectProperty<>(checkBox);
	}

}
