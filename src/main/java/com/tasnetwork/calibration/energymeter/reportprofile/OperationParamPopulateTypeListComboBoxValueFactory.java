package com.tasnetwork.calibration.energymeter.reportprofile;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class OperationParamPopulateTypeListComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<OperationParam, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<OperationParam, ComboBox<String>> param) {
			OperationParam rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			ArrayList<String> cellPositionList = ConstantReportV2.POPULATE_PARAM_PROFILE_DATA_TYPE_LIST;//new ArrayList<String>();
			int columnWidth = 167;//165 150;//100//75;//100;//50;//150;//250//50

			cellPositionList.removeIf(e -> e.equals(ConstantReportV2.POPULATE_DATA_TYPE_NONE));
/*			if(rowData.getReplicateResultCellPositionList().size()>0){	
				ArrayList<String> dataList = new ArrayList<String>(rowData.getReplicateResultCellPositionList());
				cellPositionList.addAll(dataList);
			}else {
				cellPositionList.add(ConstantReportV2.NONE_DISPLAYED);
			}*/
			Combo_Box.getItems().clear();
			Combo_Box.getItems().addAll(cellPositionList);
			Combo_Box.getSelectionModel().select(0);
			//rowData.setPopulateType(Combo_Box.getSelectionModel().getSelectedItem());
			Combo_Box.setValue(rowData.getPopulateType());

			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info(Combo_Box.getSelectionModel().getSelectedItem());
	             rowData.setPopulateType(Combo_Box.getSelectionModel().getSelectedItem());
	        });
/*			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info("OperationParamPopulateTypeListComboBoxValueFactory: " + Combo_Box.getSelectionModel().getSelectedItem());
	             //rowData.setTestRunType(Combo_Box.getSelectionModel().getSelectedItem());
	        });*/
			
			Combo_Box.setMinWidth(columnWidth);
			Combo_Box.setPrefWidth(columnWidth);
			Combo_Box.setMaxWidth(columnWidth);
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }


	}
