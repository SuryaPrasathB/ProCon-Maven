package com.tasnetwork.calibration.energymeter.setting;

import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeviceSettingClusterNameComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeviceSetting, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
    @Override
    public ObservableValue<ComboBox<String>> call(CellDataFeatures<DeviceSetting, ComboBox<String>> param) {
        DeviceSetting rowData = param.getValue();
        ComboBox<String> clusterComboBox = new ComboBox<>();
        clusterComboBox.setPrefWidth(120);
        clusterComboBox.setMaxWidth(120);
        clusterComboBox.setMinWidth(120);
        
        clusterComboBox.getItems().addAll(BayUtils.getClusterNameIdListMap().keySet().stream().collect(Collectors.toList()));
        
        if (rowData.getClusterName() != null) {
            clusterComboBox.getSelectionModel().select(rowData.getClusterName());
        } else if (!clusterComboBox.getItems().isEmpty()) {
            // Default to the first item if no value is set
            clusterComboBox.getSelectionModel().select(0);
            rowData.setClusterName(clusterComboBox.getSelectionModel().getSelectedItem());
        }
        
        clusterComboBox.valueProperty().addListener((obs, oldCluster, newCluster) -> {
            if (newCluster != null) {
                rowData.setClusterName(newCluster); // Notify BayName factory
                rowData.setBayName(BayUtils.getClusterBayNameListMap().get(newCluster).get(0)); // Reset BayName                
				rowData.setClusterId(BayUtils.getClusterNameIdListMap().get(newCluster));
                //rowData.setcName(QrScannerPortSetupV2Controller.getClusterBayNameListMap().get(newCluster).get(0));sxcxz
                
            }
        });

        return new SimpleObjectProperty<>(clusterComboBox);
    }
}