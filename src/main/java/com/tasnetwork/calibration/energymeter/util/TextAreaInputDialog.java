package com.tasnetwork.calibration.energymeter.util;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * A dialog that shows a TextArea input
 */
public class TextAreaInputDialog extends Dialog<String> {

    /**************************************************************************
     *
     * Fields
     *
     **************************************************************************/

    private final GridPane grid;
    private final TextArea textArea;
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
    public TextAreaInputDialog() {
        this("");
    }

    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextField}.
     */
    public TextAreaInputDialog(@NamedArg("defaultValue") String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // -- textarea
        this.textArea = new TextArea(defaultValue);
        this.textArea.setMaxWidth(Double.MAX_VALUE);
        this.textArea.setMaxHeight(100);
        //this.textArea.getStyleClass().add("-fx-background-color: lightgrey");
        
        //this.textArea.getStyleClass().add("-fx-prompt-text-fill: white");
        //this.textArea.getStyleClass().add(".text-area .content {  -fx-background-color: black ;}");
        //this.textArea.getStyleClass().add("-fx-text-fill: #1e88e5;-fx-font-size: 32px;");
        //this.textArea.getStyleClass().add("-fx-highlight-text-fill: #1e88e5;-fx-font-size: 32px;");
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setFillWidth(textArea, true);

        this.defaultValue = defaultValue;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle(ControlResources.getString("Dialog.confirm.title"));
        dialogPane.setHeaderText(ControlResources.getString("Dialog.confirm.header"));
        dialogPane.getStyleClass().add("text-input-dialog");
        //dialogPane.getStyleClass().add("text-input-dialog;.text-area .content {  -fx-background-color: black ;}");
        //dialogPane.getStyleClass().add("text-input-dialog;-fx-background-color: lightgrey");
        //dialogPane.getStyleClass().add("text-input-dialog; .text-area .content {  -fx-background-color: black ;}");
        //dialogPane.getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
        dialogPane.getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.YES ? "OpenOutputFolder" : null;
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
    public final TextArea getEditor() {
        return textArea;
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

        grid.add(textArea, 1, 0);
        getDialogPane().setContent(grid);

        Platform.runLater(() -> textArea.requestFocus());
    }
}
