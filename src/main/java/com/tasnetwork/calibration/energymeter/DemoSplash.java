package com.tasnetwork.calibration.energymeter;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/** Example of displaying a splash page for a standalone JavaFX application */
public class DemoSplash extends Application {
  private Pane splashLayout;
  // private ProgressBar loadProgress;
  // private Label progressText;
  // private WebView webView;
  private Stage mainStage;
  private static final int SPLASH_WIDTH = 480;
  private static final int SPLASH_HEIGHT = 360;

  public static void main(String[] args) throws Exception {
    launch(args);
  }

  @Override
  public void init() {
    // ImageView splash = new ImageView(new Image("//images/ProCAL-Splash1.jpg"));

    File file = new File("@../../images/ProCAL-Splash1.jpg");

    ImageView splash = new ImageView(new Image(file.toURI().toString()));
    // loadProgress = new ProgressBar();
    // loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
    // progressText = new Label("Loading hobbits with pie . . .");
    splashLayout = new VBox();
    // splashLayout.getChildren().addAll(splash, loadProgress, progressText);
    splashLayout.getChildren().addAll(splash);
    // progressText.setAlignment(Pos.CENTER);
    splashLayout.setStyle(
        "-fx-padding: 5; -fx-background-color: lightblue; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, lightblue, derive(lightblue, 50%));");
    splashLayout.setEffect(new DropShadow());
  }

  @Override
  public void start(final Stage initStage) throws Exception {
    showSplash(initStage);
    showMainStage();

    /*
     * webView.getEngine().documentProperty().addListener(new
     * ChangeListener<Document>() {
     * 
     * @Override public void changed(ObservableValue<? extends Document>
     * observableValue, Document document, Document document1) {
     * if (initStage.isShowing()) {
     * mainStage.setIconified(false);
     * initStage.toFront();
     * FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2),
     * splashLayout);
     * fadeSplash.setFromValue(1.0);
     * fadeSplash.setToValue(0.0);
     * fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
     * 
     * @Override public void handle(ActionEvent actionEvent) {
     * initStage.hide();
     * }
     * });
     * fadeSplash.play();
     * }
     * }
     * });
     */
  }

  private void showMainStage() {
    mainStage = new Stage(StageStyle.DECORATED);
    mainStage.setTitle("FX Experience");
    mainStage.setIconified(true);

    // create a WebView.
    // webView = new WebView();
    // webView.getEngine().load("http://fxexperience.com/");

    // layout the scene.
    // Scene scene = new Scene(webView, 1000, 600);
    // mainStage.setScene(scene);
    mainStage.show();
  }

  private void showSplash(Stage initStage) {
    Scene splashScene = new Scene(splashLayout);
    initStage.initStyle(StageStyle.UNDECORATED);
    final Rectangle2D bounds = Screen.getPrimary().getBounds();
    initStage.setScene(splashScene);
    initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
    initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
    initStage.show();
  }
}
