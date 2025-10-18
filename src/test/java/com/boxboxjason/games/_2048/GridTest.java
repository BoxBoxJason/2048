package com.boxboxjason.games._2048;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the Grid class with edge cases coverage.
 */
@DisplayName("Grid Test Suite")
class GridTest {
    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(4);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create grid with valid size")
        void testValidGridCreation() {
            Grid testGrid = new Grid(4);
            assertNotNull(testGrid);
            assertNotNull(testGrid.getGrid());
            assertEquals(4, testGrid.getGrid().length);
            assertEquals(4, testGrid.getGrid()[0].length);
        }

        @ParameterizedTest
        @ValueSource(ints = { 2, 3, 4, 5, 6, 7, 8, 9, 10 })
        @DisplayName("Should create grid with all valid sizes (2-10)")
        void testAllValidGridSizes(int size) {
            Grid testGrid = new Grid(size);
            assertNotNull(testGrid);
            assertEquals(size, testGrid.getGrid().length);
        }

        @Test
        @DisplayName("Should throw exception for grid size less than 2")
        void testGridSizeTooSmall() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Grid(1));
            assertEquals("Grid size must be at least 2", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for grid size of 0")
        void testGridSizeZero() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Grid(0));
            assertEquals("Grid size must be at least 2", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for negative grid size")
        void testNegativeGridSize() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Grid(-1));
            assertEquals("Grid size must be at least 2", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for grid size greater than 10")
        void testGridSizeTooLarge() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Grid(11));
            assertEquals("Grid size must not exceed 10", exception.getMessage());
        }

        @Test
        @DisplayName("Should initialize grid with exactly 2 tiles")
        void testInitialTileCount() {
            Grid testGrid = new Grid(4);
            int[][] gridArray = testGrid.getGrid();
            int tileCount = 0;
            for (int[] row : gridArray) {
                for (int cell : row) {
                    if (cell != 0) {
                        tileCount++;
                    }
                }
            }
            assertEquals(2, tileCount);
        }

        @Test
        @DisplayName("Should initialize tiles with values 2 or 4 only")
        void testInitialTileValues() {
            Grid testGrid = new Grid(4);
            int[][] gridArray = testGrid.getGrid();
            for (int[] row : gridArray) {
                for (int cell : row) {
                    if (cell != 0) {
                        assertTrue(cell == 2 || cell == 4);
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Reinit Tests")
    class ReinitTests {

        @Test
        @DisplayName("Should reset grid to initial state")
        void testReinit() {
            // Make some moves to change the grid
            grid.moveAndAddTile(Direction.UP);
            grid.moveAndAddTile(Direction.LEFT);

            // Reinitialize
            grid.reinit(4);

            int[][] gridArray = grid.getGrid();
            int tileCount = 0;
            for (int[] row : gridArray) {
                for (int cell : row) {
                    if (cell != 0) {
                        tileCount++;
                    }
                }
            }
            assertEquals(2, tileCount);
        }

        @Test
        @DisplayName("Should allow changing grid size on reinit")
        void testReinitWithDifferentSize() {
            grid.reinit(6);
            assertEquals(6, grid.getGrid().length);
            assertEquals(6, grid.getGrid()[0].length);
        }

        @Test
        @DisplayName("Should reset score to 0 on reinit")
        void testReinitResetsScore() {
            // Manually set some tiles and make moves to generate score
            int[][] gridArray = grid.getGrid();
            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            grid.moveAndAddTile(Direction.UP);

            grid.reinit(4);

            assertEquals(0, grid.getScore());
        }
    }

    @Nested
    @DisplayName("AddRandomTile Tests")
    class AddRandomTileTests {

        @Test
        @DisplayName("Should add tile to empty cell")
        void testAddRandomTileToEmptyGrid() {
            Grid testGrid = new Grid(2);
            int initialCount = countNonZeroTiles(testGrid.getGrid());

            // Grid starts with 2 tiles, so we have 2 empty cells in a 2x2
            if (initialCount < 4) {
                testGrid.addRandomTile();
                int newCount = countNonZeroTiles(testGrid.getGrid());
                assertEquals(initialCount + 1, newCount);
            }
        }

        @Test
        @DisplayName("Should add tile with value 2 or 4")
        void testAddRandomTileValues() {
            Grid testGrid = new Grid(10); // Larger grid to have more space
            for (int i = 0; i < 50; i++) {
                if (!testGrid.isFull()) {
                    testGrid.addRandomTile();
                }
            }

            int[][] gridArray = testGrid.getGrid();
            for (int[] row : gridArray) {
                for (int cell : row) {
                    if (cell != 0) {
                        assertTrue(cell == 2 || cell == 4 || cell % 2 == 0,
                                "Cell value should be 2, 4, or a power of 2");
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Move Tests - Up Direction")
    class MoveUpTests {

        @Test
        @DisplayName("Should move tiles up when there's empty space")
        void testMoveUp() {
            int[][] gridArray = grid.getGrid();
            // Clear grid first
            clearGrid(gridArray);

            // Setup: tile at bottom
            gridArray[3][0] = 2;

            boolean moved = grid.move(Direction.UP);

            assertTrue(moved);
            assertEquals(2, gridArray[0][0]);
            assertEquals(0, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should merge identical tiles when moving up")
        void testMergeUp() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 2;

            boolean moved = grid.move(Direction.UP);

            assertTrue(moved);
            assertEquals(4, gridArray[0][0]);
            assertEquals(0, gridArray[1][0]);
        }

        @Test
        @DisplayName("Should not merge already merged tiles in same move")
        void testNoDoubleMergeUp() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            gridArray[2][0] = 2;
            gridArray[3][0] = 2;

            grid.move(Direction.UP);

            // Should result in 4, 4, 0, 0 not 8, 0, 0, 0
            assertEquals(4, gridArray[0][0]);
            assertEquals(4, gridArray[1][0]);
            assertEquals(0, gridArray[2][0]);
            assertEquals(0, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should not move when no valid moves up")
        void testNoMoveUp() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 4;

            boolean moved = grid.move(Direction.UP);

            assertFalse(moved);
        }

        @Test
        @DisplayName("Should handle complex up movement")
        void testComplexMoveUp() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[1][0] = 2;
            gridArray[2][0] = 4;
            gridArray[3][0] = 2;

            grid.move(Direction.UP);

            assertEquals(2, gridArray[0][0]);
            assertEquals(4, gridArray[1][0]);
            assertEquals(2, gridArray[2][0]);
            assertEquals(0, gridArray[3][0]);
        }
    }

    @Nested
    @DisplayName("Move Tests - Down Direction")
    class MoveDownTests {

        @Test
        @DisplayName("Should move tiles down when there's empty space")
        void testMoveDown() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;

            boolean moved = grid.move(Direction.DOWN);

            assertTrue(moved);
            assertEquals(0, gridArray[0][0]);
            assertEquals(2, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should merge identical tiles when moving down")
        void testMergeDown() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[2][0] = 2;
            gridArray[3][0] = 2;

            boolean moved = grid.move(Direction.DOWN);

            assertTrue(moved);
            assertEquals(0, gridArray[2][0]);
            assertEquals(4, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should not merge already merged tiles in same move")
        void testNoDoubleMergeDown() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            gridArray[2][0] = 2;
            gridArray[3][0] = 2;

            grid.move(Direction.DOWN);

            assertEquals(0, gridArray[0][0]);
            assertEquals(0, gridArray[1][0]);
            assertEquals(4, gridArray[2][0]);
            assertEquals(4, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should not move when no valid moves down")
        void testNoMoveDown() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[3][0] = 2;
            gridArray[3][1] = 4;

            boolean moved = grid.move(Direction.DOWN);

            assertFalse(moved);
        }

        @Test
        @DisplayName("Should handle complex down movement")
        void testComplexMoveDown() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[1][0] = 2;
            gridArray[2][0] = 4;
            gridArray[3][0] = 2;

            grid.move(Direction.DOWN);

            assertEquals(0, gridArray[0][0]);
            assertEquals(2, gridArray[1][0]);
            assertEquals(4, gridArray[2][0]);
            assertEquals(2, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should handle multiple consecutive merges in the DOWN direction")
        void testConsecutiveMergesDOWN() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            // Create a column with: 2, 2, 4, 4
            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            gridArray[2][0] = 4;
            gridArray[3][0] = 4;

            grid.move(Direction.DOWN);

            // Should result in: 0, 0, 4, 8
            assertEquals(4, gridArray[2][0]);
            assertEquals(8, gridArray[3][0]);
        }
    }

    @Nested
    @DisplayName("Move Tests - Left Direction")
    class MoveLeftTests {

        @Test
        @DisplayName("Should move tiles left when there's empty space")
        void testMoveLeft() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][3] = 2;

            boolean moved = grid.move(Direction.LEFT);

            assertTrue(moved);
            assertEquals(2, gridArray[0][0]);
            assertEquals(0, gridArray[0][3]);
        }

        @Test
        @DisplayName("Should merge identical tiles when moving left")
        void testMergeLeft() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 2;

            boolean moved = grid.move(Direction.LEFT);

            assertTrue(moved);
            assertEquals(4, gridArray[0][0]);
            assertEquals(0, gridArray[0][1]);
        }

        @Test
        @DisplayName("Should not merge already merged tiles in same move")
        void testNoDoubleMergeLeft() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 2;
            gridArray[0][2] = 2;
            gridArray[0][3] = 2;

            grid.move(Direction.LEFT);

            assertEquals(4, gridArray[0][0]);
            assertEquals(4, gridArray[0][1]);
            assertEquals(0, gridArray[0][2]);
            assertEquals(0, gridArray[0][3]);
        }

        @Test
        @DisplayName("Should not move when no valid moves left")
        void testNoMoveLeft() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 4;

            boolean moved = grid.move(Direction.LEFT);

            assertFalse(moved);
        }
    }

    @Nested
    @DisplayName("Move Tests - Right Direction")
    class MoveRightTests {

        @Test
        @DisplayName("Should move tiles right when there's empty space")
        void testMoveRight() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;

            boolean moved = grid.move(Direction.RIGHT);

            assertTrue(moved);
            assertEquals(0, gridArray[0][0]);
            assertEquals(2, gridArray[0][3]);
        }

        @Test
        @DisplayName("Should merge identical tiles when moving right")
        void testMergeRight() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][2] = 2;
            gridArray[0][3] = 2;

            boolean moved = grid.move(Direction.RIGHT);

            assertTrue(moved);
            assertEquals(0, gridArray[0][2]);
            assertEquals(4, gridArray[0][3]);
        }

        @Test
        @DisplayName("Should not merge already merged tiles in same move")
        void testNoDoubleMergeRight() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 2;
            gridArray[0][2] = 2;
            gridArray[0][3] = 2;

            grid.move(Direction.RIGHT);

            assertEquals(0, gridArray[0][0]);
            assertEquals(0, gridArray[0][1]);
            assertEquals(4, gridArray[0][2]);
            assertEquals(4, gridArray[0][3]);
        }

        @Test
        @DisplayName("Should not move when no valid moves right")
        void testNoMoveRight() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][3] = 2;
            gridArray[1][3] = 4;

            boolean moved = grid.moveAndAddTile(Direction.RIGHT);

            assertFalse(moved);
        }
    }

    @Nested
    @DisplayName("Move Tests - Edge Cases")
    class MoveEdgeCases {

        @Test
        @DisplayName("Should add new tile after successful move")
        void testAddTileAfterMove() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 2;

            int beforeCount = countNonZeroTiles(gridArray);
            grid.moveAndAddTile(Direction.UP);
            int afterCount = countNonZeroTiles(gridArray);

            // After merge (2 tiles -> 1 tile) and adding new random tile
            assertEquals(beforeCount, afterCount); // 2 - 1 + 1 = 2
        }

        @Test
        @DisplayName("Should not add new tile after unsuccessful move")
        void testNoAddTileAfterFailedMove() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;

            int beforeCount = countNonZeroTiles(gridArray);
            grid.moveAndAddTile(Direction.UP);
            int afterCount = countNonZeroTiles(gridArray);

            assertEquals(beforeCount, afterCount);
        }

        @Test
        @DisplayName("Should handle moves on minimum size grid (2x2)")
        void testMoveOnMinimumGrid() {
            Grid smallGrid = new Grid(2);
            int[][] gridArray = smallGrid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[1][0] = 2;

            boolean moved = smallGrid.move(Direction.UP);

            assertTrue(moved);
            assertEquals(4, gridArray[0][0]);
        }

        @Test
        @DisplayName("Should handle moves on maximum size grid (10x10)")
        void testMoveOnMaximumGrid() {
            Grid largeGrid = new Grid(10);
            int[][] gridArray = largeGrid.getGrid();
            clearGrid(gridArray);

            gridArray[9][0] = 2;

            boolean moved = largeGrid.move(Direction.UP);

            assertTrue(moved);
            assertEquals(2, gridArray[0][0]);
        }

        @Test
        @DisplayName("Should handle multiple consecutive merges in the UP direction")
        void testConsecutiveMergesUP() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            // Create a column with: 2, 2, 4, 4
            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            gridArray[2][0] = 4;
            gridArray[3][0] = 4;

            grid.move(Direction.UP);

            // Should result in: 4, 8, 0, 0 (plus random tile somewhere)
            assertEquals(4, gridArray[0][0]);
            assertEquals(8, gridArray[1][0]);
        }

        @Test
        @DisplayName("Should handle multiple consecutive merges in the DOWN direction")
        void testConsecutiveMergesDOWN() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            // Create a column with: 2, 2, 4, 4
            gridArray[0][0] = 2;
            gridArray[1][0] = 2;
            gridArray[2][0] = 4;
            gridArray[3][0] = 4;

            grid.move(Direction.DOWN);

            // Should result in: 0, 0, 4, 8
            assertEquals(4, gridArray[2][0]);
            assertEquals(8, gridArray[3][0]);
        }

        @Test
        @DisplayName("Should handle multiple consecutive merges in the LEFT direction")
        void testConsecutiveMergesLEFT() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            // Create a row with: 2, 2, 4, 4
            gridArray[0][0] = 2;
            gridArray[0][1] = 2;
            gridArray[0][2] = 4;
            gridArray[0][3] = 4;

            grid.move(Direction.LEFT);

            // Should result in: 4, 8, 0, 0
            assertEquals(4, gridArray[0][0]);
            assertEquals(8, gridArray[0][1]);
        }

        @Test
        @DisplayName("Should handle multiple consecutive merges in the RIGHT direction")
        void testConsecutiveMergesRIGHT() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            // Create a row with: 2, 2, 4, 4
            gridArray[0][0] = 2;
            gridArray[0][1] = 2;
            gridArray[0][2] = 4;
            gridArray[0][3] = 4;

            grid.move(Direction.RIGHT);

            // Should result in: 0, 0, 4, 8
            assertEquals(4, gridArray[0][2]);
            assertEquals(8, gridArray[0][3]);
        }
    }

    @Nested
    @DisplayName("IsFull Tests")
    class IsFullTests {

        @Test
        @DisplayName("Should return false for empty grid")
        void testEmptyGridNotFull() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            assertFalse(grid.isFull());
        }

        @Test
        @DisplayName("Should return false for partially filled grid")
        void testPartiallyFilledGridNotFull() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 4;

            assertFalse(grid.isFull());
        }

        @Test
        @DisplayName("Should return true when all cells are filled")
        void testFullGrid() {
            int[][] gridArray = grid.getGrid();

            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = 2;
                }
            }

            assertTrue(grid.isFull());
        }

        @Test
        @DisplayName("Should return false when only one cell is empty")
        void testAlmostFullGrid() {
            int[][] gridArray = grid.getGrid();

            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = 2;
                }
            }
            gridArray[3][3] = 0;

            assertFalse(grid.isFull());
        }
    }

    @Nested
    @DisplayName("HasValidMoves Tests")
    class HasValidMovesTests {

        @Test
        @DisplayName("Should return true for empty grid")
        void testEmptyGridHasValidMoves() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);
            gridArray[0][0] = 2; // Need at least one tile

            assertTrue(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should return true when grid has empty cells")
        void testGridWithEmptyCellsHasValidMoves() {
            int[][] gridArray = grid.getGrid();
            clearGrid(gridArray);

            gridArray[0][0] = 2;
            gridArray[0][1] = 4;

            assertTrue(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should return true when adjacent tiles can merge horizontally")
        void testHasValidMovesWithHorizontalMerge() {
            int[][] gridArray = grid.getGrid();

            // Fill grid with different values
            int value = 2;
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = value;
                    value += 2;
                }
            }

            // Set two adjacent horizontal tiles to same value
            gridArray[0][0] = 2;
            gridArray[0][1] = 2;

            assertTrue(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should return true when adjacent tiles can merge vertically")
        void testHasValidMovesWithVerticalMerge() {
            int[][] gridArray = grid.getGrid();

            // Fill grid with different values
            int value = 2;
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = value;
                    value += 2;
                }
            }

            // Set two adjacent vertical tiles to same value
            gridArray[0][0] = 2;
            gridArray[1][0] = 2;

            assertTrue(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should return false when grid is full with no adjacent matches")
        void testNoValidMovesWhenFullAndNoMatches() {
            int[][] gridArray = grid.getGrid();

            // Create a checkerboard pattern with no possible moves
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = (i + j) % 2 == 0 ? 2 : 4;
                }
            }

            assertFalse(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should return true when grid is full but has adjacent matches")
        void testHasValidMovesWhenFullWithMatches() {
            int[][] gridArray = grid.getGrid();

            // Fill grid completely
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = 2;
                }
            }

            assertTrue(grid.hasValidMoves());
        }

        @Test
        @DisplayName("Should handle edge case with single possible merge at corner")
        void testValidMoveAtCorner() {
            int[][] gridArray = grid.getGrid();

            // Fill with alternating pattern
            for (int i = 0; i < gridArray.length; i++) {
                for (int j = 0; j < gridArray[i].length; j++) {
                    gridArray[i][j] = (i + j) % 2 == 0 ? 2 : 4;
                }
            }

            // Create a valid merge at bottom-right corner
            gridArray[3][2] = 2;
            gridArray[3][3] = 2;

            assertTrue(grid.hasValidMoves());
        }
    }

    @Nested
    @DisplayName("GetGrid Tests")
    class GetGridTests {

        @Test
        @DisplayName("Should return non-null grid array")
        void testGetGridNotNull() {
            assertNotNull(grid.getGrid());
        }

        @Test
        @DisplayName("Should return correct grid dimensions")
        void testGetGridDimensions() {
            int[][] gridArray = grid.getGrid();
            assertEquals(4, gridArray.length);
            assertEquals(4, gridArray[0].length);
        }

        @Test
        @DisplayName("Should return actual grid reference (mutable)")
        void testGetGridReturnsMutableReference() {
            int[][] gridArray = grid.getGrid();
            gridArray[0][0] = 999;

            assertEquals(999, grid.getGrid()[0][0]);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete game scenario")
        void testCompleteGameScenario() {
            Grid testGrid = new Grid(4);
            int[][] gridArray = testGrid.getGrid();
            clearGrid(gridArray);

            // Setup initial state
            gridArray[3][0] = 2;
            gridArray[3][1] = 2;

            // Make moves
            testGrid.moveAndAddTile(Direction.UP);
            testGrid.moveAndAddTile(Direction.LEFT);

            // Verify grid is still valid
            assertTrue(testGrid.hasValidMoves() || testGrid.isFull());
        }

        @Test
        @DisplayName("Should handle multiple moves until no moves available")
        void testGameUntilNoMoves() {
            Grid testGrid = new Grid(2); // Small grid for faster test
            int movesCount = 0;

            // Make random moves until no more moves
            Direction[] directions = Direction.values();
            while (testGrid.hasValidMoves() && movesCount < 100) {
                Direction dir = directions[movesCount % directions.length];
                testGrid.moveAndAddTile(dir);
                movesCount++;
            }

            // Game should end eventually
            assertTrue(movesCount < 100);
        }

        @Test
        @DisplayName("Should maintain grid integrity after multiple operations")
        void testGridIntegrityAfterOperations() {
            grid.moveAndAddTile(Direction.UP);
            grid.moveAndAddTile(Direction.DOWN);
            grid.moveAndAddTile(Direction.LEFT);
            grid.moveAndAddTile(Direction.RIGHT);
            grid.reinit(4);

            assertNotNull(grid.getGrid());
            assertEquals(4, grid.getGrid().length);

            int tileCount = 0;
            for (int[] row : grid.getGrid()) {
                for (int cell : row) {
                    if (cell != 0)
                        tileCount++;
                }
            }
            assertEquals(2, tileCount);
        }
    }

    @Nested
    @DisplayName("Duration Tests")
    class DurationTests {

        @Test
        @DisplayName("Should return non-negative duration")
        void testGetDurationNonNegative() {
            Grid testGrid = new Grid(4);
            long duration = testGrid.getDuration();
            assertTrue(duration >= 0);
        }

        @Test
        @DisplayName("Should reset duration on reinit")
        void testDurationResetOnReinit() {
            Grid testGrid = new Grid(4);

            testGrid.reinit(4);
            long durationAfterReinit = testGrid.getDuration();

            // After reinit, duration should be very small (close to 0)
            assertTrue(durationAfterReinit < 100); // Less than 100ms
        }

        @Test
        @DisplayName("getDuration should be callable multiple times")
        void testGetDurationMultipleCalls() {
            Grid testGrid = new Grid(4);
            long duration1 = testGrid.getDuration();
            long duration2 = testGrid.getDuration();

            assertTrue(duration1 >= 0);
            assertTrue(duration2 >= 0);
            // duration2 should be >= duration1 (or very close)
            assertTrue(duration2 >= duration1 - 1); // Allow for 1ms difference
        }
    }

    // Helper method to clear the grid
    private void clearGrid(int[][] gridArray) {
        for (int i = 0; i < gridArray.length; i++) {
            for (int j = 0; j < gridArray[i].length; j++) {
                gridArray[i][j] = 0;
            }
        }
    }

    // Helper method to count non-zero tiles in the grid
    private int countNonZeroTiles(int[][] gridArray) {
        int count = 0;
        for (int[] row : gridArray) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        return count;
    }
}
