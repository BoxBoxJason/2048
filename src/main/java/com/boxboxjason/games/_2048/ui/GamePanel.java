package com.boxboxjason.games._2048.ui;

import com.boxboxjason.games._2048.Direction;
import com.boxboxjason.games._2048.Grid;
import com.boxboxjason.games._2048.Score;
import java.util.function.Consumer;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GamePanel extends StackPane {
  private static final int GRID_GAP = 10;

  private Grid gameGrid;
  private GridPane gridPane;
  private double currentTileSize;
  private Consumer<Score> onGameOver;
  private boolean gameOver = false;
  private Consumer<Integer> onScoreChange;

  public GamePanel(Grid gameGrid) {
    this.gameGrid = gameGrid;
    this.gridPane = new GridPane();
    gridPane.setHgap(GRID_GAP);
    gridPane.setVgap(GRID_GAP);
    gridPane.setStyle("-fx-background-color: #bbada0;");

    getChildren().add(gridPane);
  }

  public void resizeGrid(double availableSize) {
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

  public void updateGrid() {
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

  public boolean moveGrid(Direction direction) {
    if (gameOver) {
      return false;
    }
    boolean moved = gameGrid.moveAndAddTile(direction);
    updateGrid();
    if (onScoreChange != null) {
      onScoreChange.accept(gameGrid.getScore());
    }
    if (!moved && !gameGrid.hasValidMoves()) {
      // Game over
      gameOver = true;
      TextInputDialog dialog = new TextInputDialog("Player");
      dialog.setTitle("Game Over");
      dialog.setHeaderText("Final Score: " + gameGrid.getScore());
      dialog.setContentText("Enter your name:");
      dialog
          .showAndWait()
          .ifPresent(
              username -> {
                if (!username.trim().isEmpty()) {
                  Score score =
                      new Score(
                          username.trim(),
                          gameGrid.getScore(),
                          gameGrid.getSize(),
                          gameGrid.getDuration());
                  if (onGameOver != null) {
                    onGameOver.accept(score);
                  }
                }
              });
    }
    return moved;
  }

  public Grid getGameGrid() {
    return gameGrid;
  }

  public void setGameGrid(Grid gameGrid) {
    this.gameGrid = gameGrid;
  }

  public void setOnGameOver(Consumer<Score> onGameOver) {
    this.onGameOver = onGameOver;
  }

  public void setOnScoreChange(Consumer<Integer> onScoreChange) {
    this.onScoreChange = onScoreChange;
  }

  public void resetGame() {
    gameOver = false;
    updateGrid();
    if (onScoreChange != null) {
      onScoreChange.accept(gameGrid.getScore());
    }
  }
}
