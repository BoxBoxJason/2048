package com.boxboxjason.games._2048;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score {
  private String username;
  private int score;
  private String dateTime;
  private int gridSize;
  private long duration;

  public Score(String username, int score, int gridSize, long duration) {
    this.username = username;
    this.score = score;
    this.gridSize = gridSize;
    this.duration = duration;
    this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public String getUsername() {
    return username;
  }

  public int getScore() {
    return score;
  }

  public String getDateTime() {
    return dateTime;
  }

  public int getGridSize() {
    return gridSize;
  }

  public long getDuration() {
    return duration;
  }

  @Override
  public String toString() {
    return username + " - " + score + " - " + dateTime + " - " + gridSize + " - " + duration;
  }
}
