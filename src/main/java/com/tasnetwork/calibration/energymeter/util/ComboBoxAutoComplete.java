package com.tasnetwork.calibration.energymeter.util;


import java.util.stream.Stream;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Window;

public class ComboBoxAutoComplete<T> {
	private ComboBox<T> cmb;
	String filter = "";
	private ObservableList<T> originalItems;
	public ComboBoxAutoComplete(ComboBox<T> cmb) {
		this.cmb = cmb;
		originalItems = FXCollections.observableArrayList(cmb.getItems());
		cmb.setTooltip(new Tooltip());
		cmb.setOnKeyPressed(this::handleOnKeyPressed);
		cmb.setOnHidden(this::handleOnHiding);
	}
	public void handleOnKeyPressed(KeyEvent e) {
		ObservableList<T> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();
		//new KeyCodeCombination(KeyCode.MINUS,  KeyCode.SHIFT);
/*		if(code.equals(KeyCode.MINUS)){
			ApplicationLauncher.logger.info("handleOnKeyPressed: UnderScore Pressed:" + KeyCode.UNDERSCORE);
		}else{
			ApplicationLauncher.logger.info("handleOnKeyPressed: UnderScore :" + KeyCode.UNDERSCORE + ":Current:" +code);
		}*/
		if (code.isLetterKey() || code.isDigitKey()) {
			filter += e.getText();
		}
		if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
			filter = filter.substring(0, filter.length() - 1);
			cmb.getItems().setAll(originalItems);
		}
		if (code == KeyCode.ESCAPE) {
			filter = "";
		}
		if (filter.length() == 0) {
			filteredList = originalItems;
			cmb.getTooltip().hide();
		} else {
			Stream<T> itens = cmb.getItems().stream();
			String txtUsr = filter.toString().toLowerCase();
			itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
			cmb.getTooltip().setText(txtUsr);
			Window stage = cmb.getScene().getWindow();
			//double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
			//double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
			double posX = stage.getX() + cmb.localToScene(cmb.getBoundsInLocal()).getMinX();
			double posY = stage.getY() + cmb.localToScene(cmb.getBoundsInLocal()).getMinY();
			cmb.getTooltip().show(stage, posX, posY);
			cmb.show();
		}
		cmb.getItems().setAll(filteredList);
	}
	public void handleOnHiding(Event e) {
		filter = "";
		cmb.getTooltip().hide();
		T s = cmb.getSelectionModel().getSelectedItem();
		cmb.getItems().setAll(originalItems);
		cmb.getSelectionModel().select(s);
	}
}