package com.tasnetwork.calibration.energymeter.setting;

import java.util.List;

import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.configloader.ClusterDetail;
import com.tasnetwork.calibration.conveyor.bay.configloader.Terminal;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.DeviceSetting;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeviceSettingBayNameComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeviceSetting, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
    @Override
    public ObservableValue<ComboBox<String>> call(CellDataFeatures<DeviceSetting, ComboBox<String>> param) {
        DeviceSetting rowData = param.getValue();
        ComboBox<String> bayComboBox = new ComboBox<>();
        bayComboBox.setPrefWidth(160);
        bayComboBox.setMaxWidth(160);
        bayComboBox.setMinWidth(160);

        updateBayComboBox(bayComboBox, rowData.getClusterName());

/*        bayComboBox.setValue(rowData.getBayName());
        bayComboBox.setOnAction(e -> rowData.setBayName(bayComboBox.getSelectionModel().getSelectedItem()));*/
        
        if (rowData.getBayName() != null) {
            bayComboBox.getSelectionModel().select(rowData.getBayName());
        } else if (!bayComboBox.getItems().isEmpty()) {
            // Default to the first item if no value is set
            bayComboBox.getSelectionModel().select(0);
            rowData.setBayName(bayComboBox.getSelectionModel().getSelectedItem());
        }
        
        bayComboBox.valueProperty().addListener((obs, oldBay, newBay) -> {
            if (newBay != null) {
                rowData.setBayName(newBay);
                rowData.setBayId(BayUtils.getClusterBayNameIdMap().get(rowData.getClusterName()+"_"+rowData.getBayName()));
            }
        });

        // Add a listener to watch for changes in the cluster name
        rowData.getClusterNameProperty().addListener((obs, oldCluster, newCluster) -> {
            if (newCluster != null) {
                updateBayComboBox(bayComboBox, newCluster);
                bayComboBox.getSelectionModel().select(0); // Select the first item after update
                rowData.setBayName(bayComboBox.getSelectionModel().getSelectedItem()); // Reset the selected bay name
                rowData.setBayId(BayUtils.getClusterBayNameIdMap().get(newCluster+"_"+rowData.getBayName()));
            }
        });

        return new SimpleObjectProperty<>(bayComboBox);
    }

    private void updateBayComboBox(ComboBox<String> bayComboBox, String clusterName) {
        bayComboBox.getItems().clear();
        if (BayUtils.getClusterBayNameListMap().containsKey(clusterName)) {
            bayComboBox.getItems().addAll(BayUtils.getClusterBayNameListMap().get(clusterName));
        }
    }
}