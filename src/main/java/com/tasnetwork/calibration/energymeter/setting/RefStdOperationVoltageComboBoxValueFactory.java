package com.tasnetwork.calibration.energymeter.setting;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class RefStdOperationVoltageComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<RefStdPulseConstantModel, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	
	@Override
    public ObservableValue<ComboBox<String>> call(CellDataFeatures<RefStdPulseConstantModel, ComboBox<String>> param) {
		RefStdPulseConstantModel rowData = param.getValue();
		ComboBox<String> Combo_Box = new ComboBox<String>();
		ArrayList<String> operationtype = new ArrayList<String>();
		/*if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
			testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
		}*/
		operationtype.addAll(ConstantApp.REF_STD_OPERATION_VOLTAGE_LIST);//(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED);
/*			if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
			testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
		}*/
		Combo_Box.getItems().clear();
		Combo_Box.getItems().addAll(operationtype);
		Combo_Box.getSelectionModel().select(0);
		Combo_Box.setValue(rowData.getOperation());

		Combo_Box.setOnAction((e) -> {
             ApplicationLauncher.logger.info("RefStdOperationVoltageComboBoxValueFactory: "+Combo_Box.getSelectionModel().getSelectedItem());
             rowData.setOperation(Combo_Box.getSelectionModel().getSelectedItem());
        });
        
        return new SimpleObjectProperty<>(Combo_Box);
    }
}
