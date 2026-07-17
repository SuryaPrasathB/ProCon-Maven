package com.tasnetwork.calibration.energymeter.setting;

import java.util.stream.Collectors;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
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

        /*for (Terminal eachTerminal : QrScannerPortSetupV2Controller.getBayConfigModel().getTerminal()) {
            if (eachTerminal.getTerminalId().equals(ConstantConveyorConfig.MY_TERMINAL_ID)) {
                for (ClusterDetail eachClusterDetail : eachTerminal.getClusterDetails()) {
                    clusterComboBox.getItems().add(eachClusterDetail.getName());
                }
            }
        }*/
        
        clusterComboBox.getItems().addAll(BayUtils.getClusterNameIdListMap().keySet().stream().collect(Collectors.toList()));

/*        clusterComboBox.setValue(rowData.getClusterName());
        clusterComboBox.setOnAction(e -> {
            String newClusterName = clusterComboBox.getSelectionModel().getSelectedItem();
            rowData.setClusterName(newClusterName); // Notify listeners
            rowData.setBayName(""); // Reset bay name on cluster change
        });*/
        
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