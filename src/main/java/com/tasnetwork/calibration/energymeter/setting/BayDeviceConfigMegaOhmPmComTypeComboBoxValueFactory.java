package com.tasnetwork.calibration.energymeter.setting;

import java.util.ArrayList;

import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BayDeviceConfigMegaOhmPmComTypeComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<BayDeviceConfig, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<BayDeviceConfig, ComboBox<String>> param) {
			BayDeviceConfig rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			Combo_Box.getItems().addAll(ConstantConveyor.DEVICE_COM_TYPE_LIST);
			Combo_Box.getSelectionModel().select(0);
			Combo_Box.setValue(rowData.getMegaOhmPmComType());

			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info(Combo_Box.getSelectionModel().getSelectedItem());
	             rowData.setMegaOhmPmComType(Combo_Box.getSelectionModel().getSelectedItem());
	        });
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }

}