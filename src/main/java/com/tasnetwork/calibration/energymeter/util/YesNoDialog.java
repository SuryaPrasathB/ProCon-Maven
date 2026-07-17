package com.tasnetwork.calibration.energymeter.util;

import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

//package com.tasnetwork.gui.dialog;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class YesNoDialog extends Dialog<Boolean> {

	 public enum MessageType {
	        INFO, WARNING, ERROR
	    }

	    public YesNoDialog(String title, String message, MessageType type) {
	        setTitle(title);
	        DialogPane dialogPane = getDialogPane();
	        dialogPane.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

	        // Set icon
	        Node icon = getIcon(type);
	        Label msgLabel = new Label(message);
	        msgLabel.setWrapText(true);
	        msgLabel.setMinWidth(Region.USE_PREF_SIZE);

	        HBox content = new HBox(10, icon, msgLabel);
	        content.setAlignment(Pos.CENTER_LEFT);

	        dialogPane.setContent(content);

	        // Optional: Set dialog icon (window icon)
	        Stage stage = (Stage) dialogPane.getScene().getWindow();
	        stage.getIcons().add(new Image("file:images/app_icon.png")); // Your app icon

	        setResultConverter(dialogButton -> dialogButton == ButtonType.YES);
	    }

	    private Node getIcon(MessageType type) {
	        String imagePath;
	        switch (type) {
	            case WARNING:
	                imagePath = "file:images/warning.png";
	                break;
	            case ERROR:
	                imagePath = "file:images/error.png";
	                break;
	            case INFO:
	            default:
	                imagePath = "file:images/info.png";
	                break;
	        }

	        ImageView icon = new ImageView(new Image(imagePath));
	        icon.setFitWidth(32);
	        icon.setFitHeight(32);
	        return icon;
	    }
}

