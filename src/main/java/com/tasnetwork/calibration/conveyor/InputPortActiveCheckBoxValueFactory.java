package com.tasnetwork.calibration.conveyor;




import com.tasnetwork.calibration.conveyor.bay.configloader.InputPort;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class InputPortActiveCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<InputPort, CheckBox>, ObservableValue<CheckBox>> {
	   
	
	private final InputPortTableViewRefresher controller;

    public InputPortActiveCheckBoxValueFactory(InputPortTableViewRefresher controller) {
        this.controller = controller;
    }
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<InputPort, CheckBox> param) {
			InputPort rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        checkBox.selectedProperty().setValue(rowData.isInputActive());
	        
	        //checkBox.setAlignment(Pos.CENTER);
	        //checkBox.setDisable(true);
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setInputActive(new_val);
	        	//ApplicationLauncher.logger.debug("InputPortActiveCheckBoxValueFactory : new_val : " + new_val);
	        	//ApplicationLauncher.logger.debug("InputPortActiveCheckBoxValueFactory : getOnStateDesc : " + rowData.getOnStateDesc());
	        	//ApplicationLauncher.logger.debug("InputPortActiveCheckBoxValueFactory : getOffStateDesc : " + rowData.getOffStateDesc());
	        	if(new_val) {
	        		rowData.setStateDescription(rowData.getOnStateDesc());
	        		ApplicationLauncher.logger.debug("InputPortActiveCheckBoxValueFactory : getStateDescription-1 : " + rowData.getStateDescription());
	        	}else {
	        		rowData.setStateDescription(rowData.getOffStateDesc());
	        		ApplicationLauncher.logger.debug("InputPortActiveCheckBoxValueFactory : getStateDescription-2: " + rowData.getStateDescription());
	        	}
	        	//BayTestController.ref_tbViewInputPortData.refresh();
	        	controller.refreshInputPortTable();
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }

}
