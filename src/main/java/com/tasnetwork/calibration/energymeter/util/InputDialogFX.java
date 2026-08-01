package com.tasnetwork.calibration.energymeter.util;

import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class InputDialogFX {

    public enum MessageType {
        INFO, WARNING, ERROR
    }

    private final Stage dialogStage;
    private final SimpleObjectProperty<String> result = new SimpleObjectProperty<>();

    public InputDialogFX(String title, String message, MessageType type) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));

        // Icon label
        Label iconLabel = new Label();
        switch (type) {
            case WARNING: iconLabel.setText("Warning: "); break;
            case ERROR: iconLabel.setText("Error: "); break;
            case INFO: default: iconLabel.setText("Information: "); break;
        }

        // Message label
        Label msgLabel = new Label(message);
        msgLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);

        // Input text field
        TextField inputField = new TextField();
        inputField.setPromptText("Enter screen number...");
        inputField.setPrefWidth(200);

        // Layout
        VBox contentBox = new VBox(10, iconLabel, msgLabel, inputField);
        contentBox.setAlignment(Pos.CENTER_LEFT);

        // Buttons
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        okButton.setPrefWidth(100);
        cancelButton.setPrefWidth(100);

        okButton.setOnAction(e -> {
            result.set(inputField.getText().trim());
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> {
            result.set(null);
            dialogStage.close();
        });

        HBox buttonBox = new HBox(20, okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox rootLayout = new VBox(20, contentBox, buttonBox);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        dialogStage.setScene(new Scene(rootLayout));
    }

    public void show() {
        dialogStage.show(); // non-blocking
    }

    public ReadOnlyObjectProperty<String> resultProperty() {
        return result;
    }
}

