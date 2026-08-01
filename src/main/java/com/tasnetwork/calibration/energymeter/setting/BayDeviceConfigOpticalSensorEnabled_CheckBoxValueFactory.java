package com.tasnetwork.calibration.energymeter.setting;


import com.tasnetwork.spring.orm.model.BayDeviceConfig;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BayDeviceConfigOpticalSensorEnabled_CheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<BayDeviceConfig, CheckBox>, ObservableValue<CheckBox>> {
	   
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<BayDeviceConfig, CheckBox> param) {
			BayDeviceConfig rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        checkBox.selectedProperty().setValue(rowData.isOpticalSensorDeviceEnabled());
	        
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setOpticalSensorDeviceEnabled(new_val);
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }


	}
