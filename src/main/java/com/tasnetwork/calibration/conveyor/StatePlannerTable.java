package com.tasnetwork.calibration.conveyor;


import com.tasnetwork.spring.orm.model.StateFlow;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StatePlannerTable {
	@FXML
    public TableView<StateFlow> tableStatePlanner;
    @FXML
    public TableColumn<StateFlow, String> columnPath;
    @FXML
    public TableColumn<StateFlow, String> columnState;
    @FXML
    public TableColumn<StateFlow, String> columnStateErrorCode;
    @FXML
    public TableColumn<StateFlow, String> columnSuccess;
    @FXML
    public TableColumn<StateFlow, String> columnSuccessErrorCode;
    @FXML
    public TableColumn<StateFlow, String> columnFailed;
    @FXML
    public TableColumn<StateFlow, String> columnFailedErrorCode;  
}
