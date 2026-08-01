package com.tasnetwork.calibration.energymeter.util;

import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * A dialog that shows a TextArea input
 */
public class TextFieldInputDialog extends Dialog<String> {

    /**************************************************************************
     *
     * Fields
     *
     **************************************************************************/

    private final GridPane grid;
    // private final TextArea textArea;
    private final TextField textField;
    private final String defaultValue;

    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextField}.
     */
    public TextFieldInputDialog() {
        this("");
    }

    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextField}.
     */
    public TextFieldInputDialog(@NamedArg("defaultValue") String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // -- textarea
        this.textField = new TextField(defaultValue);
        this.textField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        GridPane.setFillWidth(textField, true);

        this.defaultValue = defaultValue;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        // setTitle(ControlResources.getString("Dialog.confirm.title"));
        setTitle("Confirmation");
        dialogPane.setHeaderText("Confirmation");
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        // dialogPane.
        // SaveAsStage.getIcons().add(new
        // Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        // Stage primaryStage = ApplicationLauncher.getPrimaryStage();
        // stage.getIcons().add(new
        // Image(this.getClass().getResource("file:images/"+ConstantVersion.APP_ICON_FILENAME).toString()));
        stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
        // Stage stage = (Stage) dialogPane.getScene().getWindow();

        // Stage primaryStage = ApplicationLauncher.getPrimaryStage();
        // stage.initOwner(primaryStage);
        // stage.getIcons().add(new
        // Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));

        // SaveAsStage.initModality(Modality.APPLICATION_MODAL);
        // stage.initOwner(primaryStage);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textField.getText() : null;
        });
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /**
     * Returns the {@link TextField} used within this dialog.
     */
    public final TextField getEditor() {
        return textField;
    }

    /**
     * Returns the default value that was specified in the constructor.
     */
    public final String getDefaultValue() {
        return defaultValue;
    }

    /**************************************************************************
     *
     * Private Implementation
     *
     **************************************************************************/

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(textField, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> textField.requestFocus());
    }
}
