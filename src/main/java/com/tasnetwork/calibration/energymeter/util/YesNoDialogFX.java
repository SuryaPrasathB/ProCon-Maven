package com.tasnetwork.calibration.energymeter.util;



import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class YesNoDialogFX {

    public enum MessageType {
        INFO, WARNING, ERROR
    }

    private final Stage dialogStage;
    private final SimpleObjectProperty<Boolean> result = new SimpleObjectProperty<>();

    public YesNoDialogFX(String title, String message, MessageType type) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.NONE); // Non-blocking
        dialogStage.setTitle(title);
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));

        // Icon label
        Label iconLabel = new Label();
        //iconLabel.s
        //iconLabel.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 48));
        
        
        /*try{
        	Font emojiFont = Font.loadFont(getClass().getResourceAsStream("/fonts/SEGUIEMJ.TTF"), 48);
        	iconLabel.setFont(emojiFont);
        }catch (Exception e) {
			ApplicationLauncher.logger.error("YesNoDialogFX: font loading " + e.getMessage());
			iconLabel.setFont(Font.font("Segoe UI Emoji", FontWeight.BOLD, 48));
		}*/
        
        switch (type) {
            /*case WARNING: iconLabel.setText("âš ï¸�"); break;
            case ERROR: iconLabel.setText("â�Œ"); break;
            case INFO: default: iconLabel.setText("â„¹ï¸�"); break;*/
        
	        case WARNING: iconLabel.setText("Warning: "); break;
	        case ERROR: iconLabel.setText("Error: "); break;
	        case INFO: default: iconLabel.setText("Information: "); break;
        }

        // Message label
        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);

        // Horizontal box for icon and message
        HBox messageBox = new HBox(6, iconLabel, msgLabel);
        messageBox.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        yesButton.setPrefWidth(100);
        noButton.setPrefWidth(100);

        yesButton.setOnAction(e -> {
            result.set(true);
            dialogStage.close();
        });

        noButton.setOnAction(e -> {
            result.set(false);
            dialogStage.close();
        });

        HBox buttonBox = new HBox(20, yesButton, noButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox rootLayout = new VBox(20, messageBox, buttonBox);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        dialogStage.setScene(new Scene(rootLayout));
    }

    public void show() {
        dialogStage.show(); // Non-blocking
    }

    public ReadOnlyObjectProperty<Boolean> resultProperty() {
        return result;
    }
}
