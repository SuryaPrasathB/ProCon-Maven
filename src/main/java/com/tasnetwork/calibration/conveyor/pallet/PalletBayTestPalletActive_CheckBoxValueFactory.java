package com.tasnetwork.calibration.conveyor.pallet;


import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;
import com.tasnetwork.spring.orm.model.PalletManage;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class PalletBayTestPalletActive_CheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<PalletManage, CheckBox>, ObservableValue<CheckBox>> {
	   
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<PalletManage, CheckBox> param) {
			PalletManage rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        checkBox.setDisable(true);
	        checkBox.selectedProperty().setValue(rowData.isPalletActive());
	        
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setPalletActive(new_val);
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }


	}
