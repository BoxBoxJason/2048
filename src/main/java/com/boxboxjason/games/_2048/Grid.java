package com.boxboxjason.games._2048;

import java.util.Random;

public class Grid {
    private int[][] grid;
    private int size;
    private Random random;
    private int score;
    private long started;

    public Grid(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Grid size must be at least 2");
        } else if (size > 10) {
            throw new IllegalArgumentException("Grid size must not exceed 10");
        }
        this.reinit(size);
    }

    public void reinit(int newSize) {
        this.size = newSize;
        this.grid = new int[newSize][newSize];
        this.random = new Random();
        this.score = 0;
        this.started = System.currentTimeMillis();
        addRandomTile();
        addRandomTile();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getSize() {
        return size;
    }

    public int getScore() {
        return score;
    }

    public long getDuration() {
        return System.currentTimeMillis() - started;
    }

    public void addRandomTile() {
        int x;
        int y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (grid[x][y] != 0);

        // Generate a random number and set the tile value based on the probability
        grid[x][y] = random.nextDouble() < 0.9 ? 2 : 4; // 90% chance for 2, 10% chance for 4
    }

    public boolean moveAndAddTile(Direction direction) {
        boolean moved = move(direction);
        if (moved) {
            addRandomTile();
        }
        return moved;
    }

    public boolean move(Direction direction) {
        boolean moved = false;
        switch (direction) {
            case UP:
                for (int col = 0; col < size; col++) {
                    moved |= moveColumnUp(col);
                }
                break;
            case RIGHT:
                for (int row = 0; row < size; row++) {
                    moved |= moveRowRight(row);
                }
                break;
            case DOWN:
                for (int col = 0; col < size; col++) {
                    moved |= moveColumnDown(col);
                }
                break;
            case LEFT:
                for (int row = 0; row < size; row++) {
                    moved |= moveRowLeft(row);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
        return moved;
    }

    private boolean moveColumnUp(int col) {
        boolean moved = false;
        int[] compact = new int[size];
        int compactSize = 0;

        for (int row = 0; row < size; row++) {
            if (grid[row][col] != 0) {
                compact[compactSize++] = grid[row][col];
            }
        }

        int[] newColumn = new int[size];
        int newIndex = 0;
        int i = 0;
        while (i < compactSize) {
            int value = compact[i];
            if (i + 1 < compactSize && compact[i + 1] == value) {
                value *= 2;
                this.score += value;
                newColumn[newIndex++] = value;
                i += 2;
            } else {
                newColumn[newIndex++] = value;
                i++;
            }
        }

        for (int row = 0; row < size; row++) {
            if (grid[row][col] != newColumn[row]) {
                moved = true;
            }
            grid[row][col] = newColumn[row];
        }

        return moved;
    }

    private boolean moveRowRight(int row) {
        boolean moved = false;
        for (int col = size - 2; col >= 0; col--) {
            if (grid[row][col] != 0) {
                int targetCol = col;
                while (targetCol < size - 1 && grid[row][targetCol + 1] == 0) {
                    grid[row][targetCol + 1] = grid[row][targetCol];
                    grid[row][targetCol] = 0;
                    targetCol++;
                    moved = true;
                }
                if (targetCol < size - 1 && grid[row][targetCol + 1] == grid[row][targetCol]) {
                    grid[row][targetCol + 1] *= 2;
                    this.score += grid[row][targetCol + 1];
                    grid[row][targetCol] = 0;
                    moved = true;
                }
            }
        }
        return moved;
    }

    private boolean moveColumnDown(int col) {
        boolean moved = false;
        for (int row = size - 2; row >= 0; row--) {
            if (grid[row][col] != 0) {
                int targetRow = row;
                while (targetRow < size - 1 && grid[targetRow + 1][col] == 0) {
                    grid[targetRow + 1][col] = grid[targetRow][col];
                    grid[targetRow][col] = 0;
                    targetRow++;
                    moved = true;
                }
                if (targetRow < size - 1 && grid[targetRow + 1][col] == grid[targetRow][col]) {
                    grid[targetRow + 1][col] *= 2;
                    this.score += grid[targetRow + 1][col];
                    grid[targetRow][col] = 0;
                    moved = true;
                }
            }
        }
        return moved;
    }

    private boolean moveRowLeft(int row) {
        boolean moved = false;
        int[] compact = new int[size];
        int compactSize = 0;

        for (int col = 0; col < size; col++) {
            if (grid[row][col] != 0) {
                compact[compactSize++] = grid[row][col];
            }
        }

        int[] newRow = new int[size];
        int newIndex = 0;
        int i = 0;
        while (i < compactSize) {
            int value = compact[i];
            if (i + 1 < compactSize && compact[i + 1] == value) {
                value *= 2;
                this.score += value;
                newRow[newIndex++] = value;
                i += 2;
            } else {
                newRow[newIndex++] = value;
                i++;
            }
        }

        for (int col = 0; col < size; col++) {
            if (grid[row][col] != newRow[col]) {
                moved = true;
            }
            grid[row][col] = newRow[col];
        }

        return moved;
    }

    public boolean isFull() {
        for (int[] row : grid) {
            for (int tile : row) {
                if (tile == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasValidMoves() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == 0) {
                    return true;
                }
                if (row < size - 1 && grid[row][col] == grid[row + 1][col]) {
                    return true;
                }
                if (col < size - 1 && grid[row][col] == grid[row][col + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            for (int tile : row) {
                sb.append(String.format("%4d", tile));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
