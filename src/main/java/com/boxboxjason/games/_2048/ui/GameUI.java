package com.boxboxjason.games._2048.ui;

import com.boxboxjason.games._2048.Direction;
import com.boxboxjason.games._2048.Grid;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.application.Platform;

public class GameUI extends Application {

  private static final int GRID_GAP = 10;
  private static final Color BACKGROUND_COLOR = Color.BEIGE;

  private Grid gameGrid;
  private GridPane gridPane;
  private double currentTileSize;

  @Override
  public void start(Stage primaryStage) {
    gameGrid = new Grid(4); // Default grid size is 4x4
    gridPane = new GridPane();
    gridPane.setHgap(GRID_GAP);
    gridPane.setVgap(GRID_GAP);
    gridPane.setStyle("-fx-background-color: #bbada0;");

    // Put gridPane inside a StackPane so it stays centered while the window resizes
    StackPane root = new StackPane(gridPane);
    Scene scene = new Scene(root, 600, 600, BACKGROUND_COLOR);

    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP -> moveGrid(Direction.UP);
        case DOWN -> moveGrid(Direction.DOWN);
        case LEFT -> moveGrid(Direction.LEFT);
        case RIGHT -> moveGrid(Direction.RIGHT);
        default -> {
          // No action needed for other keys
        }
      }
    });

    primaryStage.setTitle("2048 Game");
    primaryStage.setScene(scene);

    // Show the stage
    primaryStage.show();

    // Update grid whenever scene size changes so tiles recompute to the square area
    scene.widthProperty()
        .addListener((obs, o, n) -> Platform.runLater(() -> resizeGrid(scene.getWidth(), scene.getHeight())));
    scene.heightProperty()
        .addListener((obs, o, n) -> Platform.runLater(() -> resizeGrid(scene.getWidth(), scene.getHeight())));

    // Initial grid setup based on initial scene size
    Platform.runLater(() -> resizeGrid(scene.getWidth(), scene.getHeight()));
  }

  private void resizeGrid(double sceneWidth, double sceneHeight) {
    // Use the smaller of width/height so the grid remains square and centered
    double availableSize = Math.min(sceneWidth, sceneHeight);

    // Calculate tile size based on grid size and gaps
    int gridSize = gameGrid.getSize();
    double totalGapSize = (double) GRID_GAP * (gridSize - 1);
    currentTileSize = (availableSize - totalGapSize) / gridSize;

    // Ensure minimum tile size
    currentTileSize = Math.max(currentTileSize, 20);

    // Constrain gridPane to the computed square so it will be centered by StackPane
    gridPane.setMinWidth(availableSize);
    gridPane.setMinHeight(availableSize);
    gridPane.setPrefWidth(availableSize);
    gridPane.setPrefHeight(availableSize);
    gridPane.setMaxWidth(availableSize);
    gridPane.setMaxHeight(availableSize);

    updateGrid();
  }

  private void updateGrid() {
    gridPane.getChildren().clear();
    int[][] grid = gameGrid.getGrid();
    for (int row = 0; row < gameGrid.getSize(); row++) {
      for (int col = 0; col < gameGrid.getSize(); col++) {
        Rectangle tile = new Rectangle(currentTileSize, currentTileSize);
        int value = grid[row][col];
        if (value == 0) {
          tile.setFill(Color.LIGHTGRAY);
        } else {
          tile.setFill(getTileColor(value));
        }

        gridPane.add(tile, col, row);

        if (value != 0) {
          Text text = new Text(String.valueOf(value));
          text.setFont(Font.font("Arial", currentTileSize / 2.0));
          text.setFill(Color.BLACK);
          text.setTextAlignment(TextAlignment.CENTER);

          GridPane.setHalignment(text, javafx.geometry.HPos.CENTER);
          GridPane.setValignment(text, javafx.geometry.VPos.CENTER);

          gridPane.add(text, col, row);
        }
      }
    }
  }

  private Color getTileColor(int value) {
    return switch (value) {
      case 2 -> Color.BISQUE;
      case 4 -> Color.BURLYWOOD;
      case 8 -> Color.CORAL;
      case 16 -> Color.DARKORANGE;
      case 32 -> Color.ORANGE;
      case 64 -> Color.ORANGERED;
      case 128 -> Color.GOLD;
      case 256 -> Color.GOLDENROD;
      case 512 -> Color.YELLOW;
      case 1024 -> Color.LIGHTYELLOW;
      case 2048 -> Color.LIMEGREEN;
      default -> Color.DARKGRAY;
    };
  }

  private void moveGrid(Direction direction) {
    if (gameGrid.moveAndAddTile(direction)) {
      updateGrid();
    }
  }
}
