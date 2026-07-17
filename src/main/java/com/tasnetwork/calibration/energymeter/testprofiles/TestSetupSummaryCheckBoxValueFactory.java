package com.tasnetwork.calibration.energymeter.testprofiles;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;


	
public class TestSetupSummaryCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<TestSetupSummaryDataModel, CheckBox>, ObservableValue<CheckBox>> {
		   
	@Override
	   public ObservableValue<CheckBox> call(CellDataFeatures<TestSetupSummaryDataModel, CheckBox> param) {
		TestSetupSummaryDataModel rowData = param.getValue();
	       CheckBox checkBox = new CheckBox();
	       checkBox.selectedProperty().setValue(rowData.getIsSelected());
	        
	       checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        rowData.setIsSelected(new_val);
	       });
	        
	       return new SimpleObjectProperty<>(checkBox);
	   }


}


