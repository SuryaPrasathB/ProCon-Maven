package com.tasnetwork.calibration.energymeter.setting;

import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeviceSettingPortNameComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeviceSetting, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<DeviceSetting, ComboBox<String>> param) {
			DeviceSetting rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			Combo_Box.setPrefWidth(120);
			Combo_Box.setMaxWidth(120);
			Combo_Box.setMinWidth(120);
			Combo_Box.getItems().addAll(SystemSettingController.getPresentSerialPortList());
			Combo_Box.getSelectionModel().select(0);
			Combo_Box.setValue(rowData.getPortName());

			Combo_Box.setOnAction((e) -> {
	             //ApplicationLauncher.logger.info(Combo_Box.getSelectionModel().getSelectedItem());
	             rowData.setPortName(Combo_Box.getSelectionModel().getSelectedItem());
	        });
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }

}
