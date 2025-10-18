package com.boxboxjason.games._2048.ui;

import com.boxboxjason.games._2048.Direction;
import com.boxboxjason.games._2048.Grid;
import com.boxboxjason.games._2048.ScoreManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameUI extends Application {

  private static final Color BACKGROUND_COLOR = Color.web("#2d1810");

  private Grid gameGrid;
  private ScoreManager scoreManager;
  private GamePanel gamePanel;
  private ScoresPanel scoresPanel;
  private StackPane centerPane;
  private Spinner<Integer> gridSizeSpinner;
  private Scene scene;
  private BorderPane root;
  private Label scoreLabel;
  private HBox bottomBox;

  @Override
  public void start(Stage primaryStage) {
    gameGrid = new Grid(4);
    scoreManager = new ScoreManager();

    // Create panels
    gamePanel = new GamePanel(gameGrid);
    scoresPanel = new ScoresPanel(scoreManager, gameGrid.getSize());
    gamePanel.setOnGameOver(
        score -> {
          scoreManager.addScore(score);
          scoresPanel.updateScores();
          gameGrid.reinit(gameGrid.getSize());
          gamePanel.resetGame();
        });
    gamePanel.setOnScoreChange(score -> scoreLabel.setText("Score: " + score));

    // Center pane to switch between panels
    centerPane = new StackPane();
    centerPane.getChildren().addAll(scoresPanel, gamePanel); // gamePanel on top

    // Button selector
    ToggleButton gameButton = new ToggleButton("GAME");
    ToggleButton scoresButton = new ToggleButton("SCORES");
    ToggleGroup toggleGroup = new ToggleGroup();
    gameButton.setToggleGroup(toggleGroup);
    scoresButton.setToggleGroup(toggleGroup);
    gameButton.setSelected(true); // Start with game
    gameButton.setFocusTraversable(false);
    scoresButton.setFocusTraversable(false);

    HBox buttonBox = new HBox(20, gameButton, scoresButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setPadding(new Insets(15));
    buttonBox.setStyle(
        "-fx-background-color: #2d1810; -fx-border-color: #1a0f08; -fx-border-width: 2;");

    // Style buttons - larger with ridge borders and uppercase bold text
    String buttonStyle =
        "-fx-background-color: #8b4513; -fx-text-fill: #f4e4bc; -fx-font-size: 16px; -fx-font-weight: bold; "
            + "-fx-border-color: #654321; -fx-border-width: 3; -fx-border-style: solid; "
            + "-fx-padding: 10 20 10 20; -fx-min-width: 120px; -fx-min-height: 40px;";
    gameButton.setStyle(buttonStyle);
    scoresButton.setStyle(buttonStyle);

    // Grid size picker
    gridSizeSpinner = new Spinner<>(2, 10, gameGrid.getSize());
    gridSizeSpinner.setPrefWidth(80);
    gridSizeSpinner.setFocusTraversable(false);
    gridSizeSpinner.setStyle(
        "-fx-background-color: #8b4513; -fx-border-color: #654321; -fx-border-width: 3; -fx-border-style: solid; -fx-text-fill: #f4e4bc; -fx-font-weight: bold;");
    gridSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> changeGridSize(newVal));

    // Score display
    scoreLabel = new Label("Score: 0");
    scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f4e4bc;");

    Label gridSizeLabel = new Label("Grid Size");
    gridSizeLabel.setStyle("-fx-text-fill: #f4e4bc; -fx-font-weight: bold; -fx-font-size: 14px;");

    // Bottom layout - stack grid size label and spinner vertically
    VBox gridSizeBox = new VBox(5);
    gridSizeBox.setAlignment(Pos.CENTER);
    gridSizeBox.getChildren().addAll(gridSizeLabel, gridSizeSpinner);

    bottomBox = new HBox(40);
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.setPadding(new Insets(15));
    bottomBox.setMinHeight(60); // Ensure minimum height for proper display
    bottomBox.setStyle(
        "-fx-background-color: #2d1810; -fx-border-color: #1a0f08; -fx-border-width: 2;");
    bottomBox.getChildren().addAll(scoreLabel, gridSizeBox);

    // Main layout
    root = new BorderPane();
    root.setStyle("-fx-background-color: #2d1810;");
    root.setTop(buttonBox);
    root.setCenter(centerPane);
    root.setBottom(bottomBox);

    Scene localScene = new Scene(root, 600, 800, BACKGROUND_COLOR);
    this.scene = localScene;

    // Handle button toggles
    gameButton.setOnAction(e -> showGamePanel());
    scoresButton.setOnAction(e -> showScoresPanel());

    // Set initial panel state
    showGamePanel();

    // Keyboard input only for game
    scene.setOnKeyPressed(
        event -> {
          if (gameButton.isSelected()) {
            switch (event.getCode()) {
              case UP -> gamePanel.moveGrid(Direction.UP);
              case DOWN -> gamePanel.moveGrid(Direction.DOWN);
              case LEFT -> gamePanel.moveGrid(Direction.LEFT);
              case RIGHT -> gamePanel.moveGrid(Direction.RIGHT);
              default -> {
                // No action needed for other keys
              }
            }
          }
        });

    primaryStage.setTitle("2048 Game");
    primaryStage.setScene(scene);

    // Show the stage
    primaryStage.show();

    // Update grid whenever scene size changes
    scene
        .widthProperty()
        .addListener(
            (obs, o, n) ->
                Platform.runLater(() -> resizePanels(scene.getWidth(), scene.getHeight())));
    scene
        .heightProperty()
        .addListener(
            (obs, o, n) ->
                Platform.runLater(() -> resizePanels(scene.getWidth(), scene.getHeight())));

    // Initial setup
    Platform.runLater(() -> resizePanels(scene.getWidth(), scene.getHeight()));
  }

  private void showGamePanel() {
    centerPane.getChildren().clear();
    centerPane.getChildren().add(gamePanel);
    bottomBox.setVisible(true);
  }

  private void showScoresPanel() {
    centerPane.getChildren().clear();
    centerPane.getChildren().add(scoresPanel);
    scoresPanel.updateScores();
    bottomBox.setVisible(false);
  }

  private void resizePanels(double sceneWidth, double sceneHeight) {
    // Account for top bar (~95px) and bottom bar (60px minimum) = ~170px total UI
    // space
    double topBarHeight = 95; // buttonBox padding + button height + padding + additional spacing
    double bottomBarHeight = 75; // bottomBox minimum height
    double availableHeight = sceneHeight - topBarHeight - bottomBarHeight;
    double availableSize = Math.min(sceneWidth, availableHeight);
    gamePanel.resizeGrid(availableSize);
    // Ensure bottom box visibility is maintained during resize
    bottomBox.setVisible(centerPane.getChildren().contains(gamePanel));
  }

  private void changeGridSize(int newSize) {
    gameGrid.reinit(newSize);
    resizePanels(scene.getWidth(), scene.getHeight());
    scoresPanel.setCurrentGridSize(newSize);
    root.requestFocus();
  }
}
