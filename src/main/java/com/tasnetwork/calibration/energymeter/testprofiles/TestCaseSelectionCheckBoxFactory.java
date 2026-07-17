package com.tasnetwork.calibration.energymeter.testprofiles;

import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TestCaseSelectionCheckBoxFactory implements Callback<TableColumn.CellDataFeatures<List<Object>, CheckBox>, ObservableValue<CheckBox>> {
   
	private int selectedIndex;
	
	public TestCaseSelectionCheckBoxFactory(int index) {
		selectedIndex = index;
	}
	
	@Override
    public ObservableValue<CheckBox> call(CellDataFeatures<List<Object>, CheckBox> param) {
		List<Object> rowData = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue((Boolean)rowData.get(selectedIndex));
        
        checkBox.selectedProperty().addListener((obj, oldVal, newVal) -> {
        	rowData.set(selectedIndex, newVal);
        });
        
        return new SimpleObjectProperty<>(checkBox);
    }


}
