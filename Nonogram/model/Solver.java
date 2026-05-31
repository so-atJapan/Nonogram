package Nonogram.model;

import java.util.ArrayList;

public class Solver {
    private final Clue CLUE;
    private Grid grid;

    public Solver(Clue clue, Grid grid) {
        this.CLUE = clue;
        this.grid = grid;
    }

    public Solver(Clue clue) {
        this(clue, new Grid(clue.getROW_CLUES().size(), clue.getCOL_CLUES().size()));
    }

    public Grid getGrid() {
        return grid;
    }

    public void solveAtOnce() {
        for (int passCount = 0; passCount < 100; passCount++) {
            System.out.println(this.grid.filledPercent());
            if (this.grid.filledPercent() == 1.0) break;
            solveStepByStep();
        }
    }

    public void solveStepByStep() {
        step1();
    }

    private void step1() {
        for (int x = 0; x < CLUE.getROW_CLUES().size(); x++) {
            Cell[] row = grid.getRow(x);
            step1Line(CLUE.getROW_CLUES().get(x), row);
            grid.setRow(row, x);
        }
        for (int y = 0; y < CLUE.getCOL_CLUES().size(); y++) {
            Cell[] col = grid.getCol(y);
            step1Line(CLUE.getCOL_CLUES().get(y), col);
            grid.setCol(col, y);
        }
    }

    private static final int S_UNKNOWN = 0;
    private static final int S_FILLED  = 1;
    private static final int S_MARKED  = 2;

    private void step1Line(ArrayList<Integer> question, Cell[] line) {
        final int LENGTH = line.length;
        final int CLUE_COUNT   = question.size();

        // Cell[] → int[] 変換
        int[] lineStates = new int[LENGTH];
        for (int cellIndex = 0; cellIndex < LENGTH; cellIndex++) {
            CellState cellState = line[cellIndex].getState();
            if      (cellState == CellState.FILLED) lineStates[cellIndex] = S_FILLED;
            else if (cellState == CellState.MARKED) lineStates[cellIndex] = S_MARKED;
        }

        // ヒントを int[] に
        int[] clueBlockLengths = new int[CLUE_COUNT];
        for (int clueIndex = 0; clueIndex < CLUE_COUNT; clueIndex++) clueBlockLengths[clueIndex] = question.get(clueIndex);

        // 前向き DP
        long[][] forwardPatternCounts = new long[LENGTH + 1][CLUE_COUNT + 1];
        forwardPatternCounts[0][0] = 1L;

        for (int cellIndex = 0; cellIndex < LENGTH; cellIndex++) {
            for (int clueIndex = 0; clueIndex <= CLUE_COUNT; clueIndex++) {
                if (forwardPatternCounts[cellIndex][clueIndex] == 0) continue;
                long patternCount = forwardPatternCounts[cellIndex][clueIndex];

                if (clueIndex == CLUE_COUNT) {
                    if (lineStates[cellIndex] != S_FILLED) {
                        forwardPatternCounts[cellIndex + 1][CLUE_COUNT] += patternCount;
                    }
                    continue;
                }

                if (lineStates[cellIndex] != S_FILLED) {
                    forwardPatternCounts[cellIndex + 1][clueIndex] += patternCount;
                }

                int blockLength = clueBlockLengths[clueIndex];
                if (cellIndex + blockLength > LENGTH) continue;

                boolean canPlace = true;
                for (int blockCellIndex = cellIndex; blockCellIndex < cellIndex + blockLength; blockCellIndex++) {
                    if (lineStates[blockCellIndex] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int afterBlockIndex = cellIndex + blockLength;
                if (clueIndex == CLUE_COUNT - 1) {
                    forwardPatternCounts[afterBlockIndex][CLUE_COUNT] += patternCount;
                } else {
                    if (afterBlockIndex < LENGTH && lineStates[afterBlockIndex] != S_FILLED) {
                        forwardPatternCounts[afterBlockIndex + 1][clueIndex + 1] += patternCount;
                    }
                }
            }
        }

        final long TOTAL_PATTERN_COUNT = forwardPatternCounts[LENGTH][CLUE_COUNT];
        if (TOTAL_PATTERN_COUNT == 0) return; // 矛盾（解なし）

        // 後向き DP
        long[][] backwardPatternCounts = new long[LENGTH + 1][CLUE_COUNT + 1];
        backwardPatternCounts[LENGTH][CLUE_COUNT] = 1L;

        for (int cellIndex = LENGTH - 1; cellIndex >= 0; cellIndex--) {
            for (int clueIndex = 0; clueIndex <= CLUE_COUNT; clueIndex++) {

                if (clueIndex == CLUE_COUNT) {
                    if (lineStates[cellIndex] != S_FILLED) {
                        backwardPatternCounts[cellIndex][CLUE_COUNT] += backwardPatternCounts[cellIndex + 1][CLUE_COUNT];
                    }
                    continue;
                }

                if (lineStates[cellIndex] != S_FILLED) {
                    backwardPatternCounts[cellIndex][clueIndex] += backwardPatternCounts[cellIndex + 1][clueIndex];
                }

                int blockLength = clueBlockLengths[clueIndex];
                if (cellIndex + blockLength > LENGTH) continue;

                boolean canPlace = true;
                for (int blockCellIndex = cellIndex; blockCellIndex < cellIndex + blockLength; blockCellIndex++) {
                    if (lineStates[blockCellIndex] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int afterBlockIndex = cellIndex + blockLength;
                if (clueIndex == CLUE_COUNT - 1) {
                    backwardPatternCounts[cellIndex][clueIndex] += backwardPatternCounts[afterBlockIndex][CLUE_COUNT];
                } else {
                    if (afterBlockIndex < LENGTH && lineStates[afterBlockIndex] != S_FILLED) {
                        backwardPatternCounts[cellIndex][clueIndex] += backwardPatternCounts[afterBlockIndex + 1][clueIndex + 1];
                    }
                }
            }
        }

        // 差分配列で FILLED 寄与数を計算
        long[] fillCoverageDiff = new long[LENGTH + 1];

        for (int clueIndex = 0; clueIndex < CLUE_COUNT; clueIndex++) {
            int blockLength = clueBlockLengths[clueIndex];
            for (int startIndex = 0; startIndex + blockLength <= LENGTH; startIndex++) {
                if (forwardPatternCounts[startIndex][clueIndex] == 0) continue;

                boolean canPlace = true;
                for (int blockCellIndex = startIndex; blockCellIndex < startIndex + blockLength; blockCellIndex++) {
                    if (lineStates[blockCellIndex] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int afterBlockIndex = startIndex + blockLength;
                long backwardPatternCount;
                if (clueIndex == CLUE_COUNT - 1) {
                    backwardPatternCount = backwardPatternCounts[afterBlockIndex][CLUE_COUNT];
                } else {
                    if (afterBlockIndex >= LENGTH || lineStates[afterBlockIndex] == S_FILLED) continue;
                    backwardPatternCount = backwardPatternCounts[afterBlockIndex + 1][clueIndex + 1];
                }

                long contributionCount = forwardPatternCounts[startIndex][clueIndex] * backwardPatternCount;
                if (contributionCount == 0) continue;
                fillCoverageDiff[startIndex]        += contributionCount;
                fillCoverageDiff[startIndex + blockLength] -= contributionCount;
            }
        }

        // 差分配列を累積して確定セルを書き戻す
        long running = 0;
        for (int cellIndex = 0; cellIndex < LENGTH; cellIndex++) {
            running += fillCoverageDiff[cellIndex];
            if      (running == TOTAL_PATTERN_COUNT) line[cellIndex].setState(CellState.FILLED);
            else if (running == 0)     line[cellIndex].setState(CellState.MARKED);
        }
    }
}
