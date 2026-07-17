package com.tasnetwork.calibration.energymeter.testreport;

//import com.tasnetwork.calibration.energymeter.deployment.DeploymentTestCaseDataModel;
import com.tasnetwork.spring.orm.model.MeterResultSummary;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class MeterResultSummaryCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<MeterResultSummary, CheckBox>, ObservableValue<CheckBox>> {
	   
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<MeterResultSummary, CheckBox> param) {
			MeterResultSummary rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        checkBox.selectedProperty().setValue(rowData.getIsSelected());
	        
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setIsSelected(new_val);
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }

}