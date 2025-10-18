package com.boxboxjason.games._2048;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Score class.
 */
@DisplayName("Score Test Suite")
class ScoreTest {

  @Test
  @DisplayName("Should create Score with valid parameters")
  void testScoreCreation() {
    String username = "testUser";
    int score = 1000;
    int gridSize = 4;
    long duration = 60000L;

    Score scoreObj = new Score(username, score, gridSize, duration);

    assertNotNull(scoreObj);
    assertEquals(username, scoreObj.getUsername());
    assertEquals(score, scoreObj.getScore());
    assertEquals(gridSize, scoreObj.getGridSize());
    assertEquals(duration, scoreObj.getDuration());
    assertNotNull(scoreObj.getDateTime());
  }

  @Test
  @DisplayName("Should set dateTime automatically in constructor")
  void testDateTimeSetAutomatically() {
    Score score = new Score("user", 500, 4, 30000L);
    assertNotNull(score.getDateTime());
    assertFalse(score.getDateTime().isEmpty());
  }

  @Test
  @DisplayName("getUsername should return correct username")
  void testGetUsername() {
    Score score = new Score("testUser", 1000, 4, 60000L);
    assertEquals("testUser", score.getUsername());
  }

  @Test
  @DisplayName("getScore should return correct score")
  void testGetScore() {
    Score score = new Score("user", 2500, 4, 60000L);
    assertEquals(2500, score.getScore());
  }

  @Test
  @DisplayName("getDateTime should return non-null dateTime")
  void testGetDateTime() {
    Score score = new Score("user", 1000, 4, 60000L);
    assertNotNull(score.getDateTime());
    assertFalse(score.getDateTime().isEmpty());
  }

  @Test
  @DisplayName("getGridSize should return correct grid size")
  void testGetGridSize() {
    Score score = new Score("user", 1000, 5, 60000L);
    assertEquals(5, score.getGridSize());
  }

  @Test
  @DisplayName("getDuration should return correct duration")
  void testGetDuration() {
    long duration = 120000L;
    Score score = new Score("user", 1000, 4, duration);
    assertEquals(duration, score.getDuration());
  }

  @Test
  @DisplayName("toString should include all fields")
  void testToString() {
    String username = "testUser";
    int scoreVal = 1000;
    int gridSize = 4;
    long duration = 60000L;

    Score score = new Score(username, scoreVal, gridSize, duration);
    String toString = score.toString();

    assertTrue(toString.contains(username));
    assertTrue(toString.contains(String.valueOf(scoreVal)));
    assertTrue(toString.contains(String.valueOf(gridSize)));
    assertTrue(toString.contains(String.valueOf(duration)));
    assertTrue(toString.contains(score.getDateTime()));
  }

  @Test
  @DisplayName("toString should format correctly")
  void testToStringFormat() {
    Score score = new Score("user", 500, 4, 30000L);
    String expectedPattern = "user - 500 - .* - 4 - 30000";
    assertTrue(score.toString().matches(expectedPattern));
  }
}
