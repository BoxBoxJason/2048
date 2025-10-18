package com.boxboxjason.games._2048;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
  private List<Score> scores;
  private Gson gson;
  private String filePath;

  public ScoreManager() {
    this.filePath = "scores.json";
    this.gson = new Gson();
    loadScores();
  }

  private void loadScores() {
    try (FileReader reader = new FileReader(filePath)) {
      Type listType = new TypeToken<List<Score>>() {
      }.getType();
      scores = gson.fromJson(reader, listType);
      if (scores == null) {
        scores = new ArrayList<>();
      }
    } catch (IOException e) {
      scores = new ArrayList<>();
    }
    // Sort scores after loading
    scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
  }

  private void saveScores() {
    try (FileWriter writer = new FileWriter(filePath)) {
      gson.toJson(scores, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addScore(Score score) {
    scores.add(score);
    scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    if (scores.size() > 10) {
      scores = scores.subList(0, 10);
    }
    saveScores();
  }

  public List<Score> getTopScores() {
    return new ArrayList<>(scores);
  }
}
