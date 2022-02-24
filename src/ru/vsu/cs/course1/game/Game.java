package ru.vsu.cs.course1.game;

import java.util.Random;

public class Game {

    public enum GameState {
        NOT_STARTED,
        PLAYING,
        STOPPED
    }

    public static class GameInLifeCell {
        private boolean isFilled = false;

        public GameInLifeCell(boolean isFilled) {
            this.isFilled = isFilled;
        }

        public boolean isFilled() {
            return isFilled;
        }
    }

    private final Random rnd = new Random();
    private int iteration = 0;
    private GameState state = GameState.NOT_STARTED;
    private GameInLifeCell[][] field = null;

    public Game() {
    }

    public void newGame(int rowCount, int colCount) {
        state = GameState.STOPPED;
        iteration = 0;
        field = new GameInLifeCell[rowCount][colCount];

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                field[r][c] = new GameInLifeCell(false);
            }
        }

        for (int i = 0; i < (rowCount * colCount) / 3; i++) {
            int pos = rnd.nextInt(rowCount * colCount - i);
            one:
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < colCount; c++) {
                    if (!field[r][c].isFilled) {
                        if (pos == 0) {
                            field[r][c].isFilled = true;
                            break one;
                        }
                        pos--;
                    }
                }
            }
        }
    }

    public void leftMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();

        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }

        field[row][col].isFilled = true;
    }

    public void rightMouseClick(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();

        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return;
        }

        field[row][col].isFilled = false;
    }

    public void updateField() {
        GameInLifeCell[][] newField = new GameInLifeCell[field.length][field[0].length];

        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                newField[r][c] = getUpdatedCell(r, c);
            }
        }

        iteration++;
        field = newField;
    }

    private GameInLifeCell getUpdatedCell(int r, int c) {
        int  cellCountAround = getCellCountAround(r, c);

        if (!field[r][c].isFilled) {
            if (cellCountAround == 3) {
                return new GameInLifeCell(true);
            }
        } else {
            if (1 < cellCountAround && cellCountAround < 4) {
                return new GameInLifeCell(true);
            }
        }

        return new GameInLifeCell(false);
    }

    private int getCellCountAround(int row, int col) {
        int rowCount = getRowCount(), colCount = getColCount();
        int cellCount = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (0 <= r && r < rowCount && 0 <= c && c < colCount) {
                    if (field[r][c].isFilled) cellCount++;
                }
            }
        }
        cellCount -= (field[row][col].isFilled ? 1 : 0);

        return cellCount;
    }

    public int getRowCount() {
        return field == null ? 0 : field.length;
    }

    public int getColCount() {
        return field == null ? 0 : field[0].length;
    }

    public GameState getState() {
        return state;
    }

    public int getIteration() {
        return iteration;
    }

    public GameInLifeCell getCell(int row, int col) {
        return (row < 0 || row >= getRowCount() || col < 0 || col >= getColCount()) ? null : field[row][col];
    }

    public void setState (GameState state) {
        this.state = state;
    }
}
