package com.boxboxjason.games._2048.ui;

import com.boxboxjason.games._2048.Score;
import com.boxboxjason.games._2048.ScoreManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScoresPanel extends VBox {
  private static final String FONT_FAMILY = "Arial";
  private static final String TEXT_COLOR = "#f4e4bc";
  private static final String BUTTON_BG_COLOR = "#8b4513";
  private static final String FRAME_BG_COLOR = "#2d1810";
  private static final String WOOD_GRADIENT =
      "linear-gradient(from 0% 0% to 100% 100%, #8b4513, #654321, #8b4513, #a0522d, #654321)";
  private static final String BG_STYLE_PREFIX = "-fx-background-color: ";
  private static final String TEXT_STYLE_PREFIX = "-fx-text-fill: ";
  private ScoreManager scoreManager;
  private int currentGridSize;
  private VBox scoresContainer;

  public ScoresPanel(ScoreManager scoreManager, int initialGridSize) {
    this.scoreManager = scoreManager;
    this.currentGridSize = initialGridSize;

    setSpacing(10);
    setPadding(new Insets(20));
    setAlignment(Pos.TOP_CENTER);
    setStyle(BG_STYLE_PREFIX + WOOD_GRADIENT + ";");

    Label titleLabel = new Label("Top 10 Scores (Grid Size: " + currentGridSize + ")");
    titleLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
    titleLabel.setStyle(TEXT_STYLE_PREFIX + TEXT_COLOR + ";");

    // Create table header
    HBox headerRow = createTableRow("#", "Player", "Score", "Duration", "Date & Time", true);
    headerRow.setStyle(
        BG_STYLE_PREFIX
            + BUTTON_BG_COLOR
            + "; -fx-border-color: "
            + FRAME_BG_COLOR
            + "; -fx-border-width: 0 0 2 0;");

    scoresContainer = new VBox(0);
    scoresContainer.setAlignment(Pos.TOP_LEFT);
    scoresContainer.getChildren().add(headerRow);

    ScrollPane scrollPane = new ScrollPane(scoresContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(400);
    scrollPane.setStyle(BG_STYLE_PREFIX + FRAME_BG_COLOR + ";");

    getChildren().addAll(titleLabel, scrollPane);

    updateScores();
  }

  private HBox createTableRow(
      String rank,
      String player,
      String score,
      String duration,
      String dateTime,
      boolean isHeader) {
    Label rankLabel = new Label(rank);
    Label playerLabel = new Label(player);
    Label scoreLabel = new Label(score);
    Label durationLabel = new Label(duration);
    Label dateTimeLabel = new Label(dateTime);

    // Style all labels
    String labelStyle =
        TEXT_STYLE_PREFIX + TEXT_COLOR + "; -fx-padding: 8 12 8 12; -fx-font-size: 12px;";
    if (isHeader) {
      labelStyle += " -fx-font-weight: bold;";
    }
    rankLabel.setStyle(labelStyle + " -fx-min-width: 30px; -fx-alignment: center;");
    playerLabel.setStyle(labelStyle + " -fx-min-width: 100px;");
    scoreLabel.setStyle(labelStyle + " -fx-min-width: 60px; -fx-alignment: center-right;");
    durationLabel.setStyle(labelStyle + " -fx-min-width: 80px; -fx-alignment: center;");
    dateTimeLabel.setStyle(labelStyle + " -fx-min-width: 120px; -fx-alignment: center;");

    HBox row = new HBox();
    row.getChildren().addAll(rankLabel, playerLabel, scoreLabel, durationLabel, dateTimeLabel);
    row.setStyle(
        BG_STYLE_PREFIX
            + BUTTON_BG_COLOR
            + "; -fx-border-color: "
            + FRAME_BG_COLOR
            + "; -fx-border-width: 0 0 1 0;");

    return row;
  }

  public void updateScores() {
    // Clear all rows except header
    while (scoresContainer.getChildren().size() > 1) {
      scoresContainer.getChildren().remove(1);
    }

    List<Score> topScores = scoreManager.getTopScoresForGridSize(currentGridSize);

    if (topScores.isEmpty()) {
      Label noScoresLabel = new Label("No scores yet for grid size " + currentGridSize);
      noScoresLabel.setFont(Font.font(FONT_FAMILY, 14));
      noScoresLabel.setStyle(TEXT_STYLE_PREFIX + TEXT_COLOR + "; -fx-padding: 20;");
      scoresContainer.getChildren().add(noScoresLabel);
    } else {
      for (int i = 0; i < topScores.size(); i++) {
        Score score = topScores.get(i);
        String formattedDateTime = formatDateTime(score.getDateTime());
        HBox scoreRow =
            createTableRow(
                String.valueOf(i + 1),
                score.getUsername(),
                String.valueOf(score.getScore()),
                formatDuration(score.getDuration()),
                formattedDateTime,
                false);
        scoresContainer.getChildren().add(scoreRow);
      }
    }
  }

  private String formatDuration(long durationMillis) {
    long seconds = durationMillis / 1000;
    long minutes = seconds / 60;
    long hours = minutes / 60;

    if (hours > 0) {
      return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
    } else if (minutes > 0) {
      return String.format("%dm %ds", minutes, seconds % 60);
    } else {
      return String.format("%ds", seconds);
    }
  }

  private String formatDateTime(String isoDateTime) {
    try {
      LocalDateTime dateTime =
          LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    } catch (Exception e) {
      return isoDateTime; // Fallback to original format if parsing fails
    }
  }

  public void setCurrentGridSize(int gridSize) {
    this.currentGridSize = gridSize;
    Label titleLabel = (Label) getChildren().get(0);
    titleLabel.setText("Top 10 Scores (Grid Size: " + currentGridSize + ")");
    updateScores();
  }
}
