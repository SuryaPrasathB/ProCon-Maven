package com.tasnetwork.calibration.energymeter.uac;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class UacExecutePossibleCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<UacDataModel, CheckBox>, ObservableValue<CheckBox>> {
	   
	@Override
    public ObservableValue<CheckBox> call(CellDataFeatures<UacDataModel, CheckBox> param) {
		UacDataModel rowData = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(rowData.getExecutePossible());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
        	rowData.setExecutePossible(new_val);
        });
        
        return new SimpleObjectProperty<>(checkBox);
    }


}