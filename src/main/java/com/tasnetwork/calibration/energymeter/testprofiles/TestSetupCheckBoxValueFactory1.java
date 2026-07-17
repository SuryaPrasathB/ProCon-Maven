package com.tasnetwork.calibration.energymeter.testprofiles;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TestSetupCheckBoxValueFactory1 implements Callback<TableColumn.CellDataFeatures<TestSetupDataModel, CheckBox>, ObservableValue<CheckBox>> {
	   
	@Override
    public ObservableValue<CheckBox> call(CellDataFeatures<TestSetupDataModel, CheckBox> param) {
		TestSetupDataModel rowData = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(rowData.getIsSelected1());
        
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
        	rowData.setIsSelected1(new_val);
        });
        
        return new SimpleObjectProperty<>(checkBox);
    }


}
