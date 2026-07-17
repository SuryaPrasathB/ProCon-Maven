package com.tasnetwork.calibration.energymeter.setting;


import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TerminalProfileBayActive_CheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<TerminalProfileSetting, CheckBox>, ObservableValue<CheckBox>> {
	   
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<TerminalProfileSetting, CheckBox> param) {
			TerminalProfileSetting rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        checkBox.selectedProperty().setValue(rowData.isBayActive());
	        
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setBayActive(new_val);
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }


	}
