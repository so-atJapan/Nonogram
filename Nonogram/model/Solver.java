package Nonogram.model;

import java.util.ArrayList;

public class Solver {
    private Clue clue;
    private Grid grid;

    public Solver(Clue clue, Grid grid) {
        this.clue = clue;
        this.grid = grid;
    }

    public Solver(Clue clue) {
        this(clue, new Grid(clue.getRowClues().size(), clue.getColClues().size()));
    }

    public Grid getGrid() {
        return grid;
    }

    public void solveAtOnce() {
        for (int i = 0; i < 100; i++) {
            System.out.println(this.grid.filledPercent());
            if (this.grid.filledPercent() == 1.0) break;
            solveStepByStep();
        }
    }

    public void solveStepByStep() {
        step1();
    }

    private void step1() {
        for (int x = 0; x < clue.getRowClues().size(); x++) {
            Cell[] row = grid.getRow(x);
            step1Line(clue.getRowClues().get(x), row);
            grid.setRow(row, x);
        }
        for (int y = 0; y < clue.getColClues().size(); y++) {
            Cell[] col = grid.getCol(y);
            step1Line(clue.getColClues().get(y), col);
            grid.setCol(col, y);
        }
    }

    private void step1Line(ArrayList<Integer> question, Cell[] line) {
        int len = line.length;

        // 各セルが「有効パターン中で何回 FILLED だったか」「何回 EMPTY だったか」を数える
        int[] filledCount = new int[len];
        int[] emptyCount  = new int[len];
        int[] validCount  = new int[]{0};  // 有効パターン総数

        Cell[] work = new Cell[len];

        // 生成しながらカウントアップ（枝刈り付き再帰）
        countPatternsWithCommit(work, question, line, 0, 0,
                                filledCount, emptyCount, validCount);

        // 全有効パターンで FILLED だったセル → FILLED 確定
        // 全有効パターンで EMPTY  だったセル → MARKED 確定
        for (int i = 0; i < len; i++) {
            if (filledCount[i] == validCount[0]) {
                line[i].setState(CellState.FILLED);
            } else if (emptyCount[i] == validCount[0]) {
                line[i].setState(CellState.MARKED);
            }
        }
    }

    /**
     * パターンを生成しながら、現在の line と矛盾するものを枝刈りし、
     * 矛盾しないパターンについてのみ filledCount / emptyCount をカウントする。
     *
     * @param work       作業用ライン（再帰間で使い回す）
     * @param q          ヒント数列
     * @param line       現在の確定情報（FILLED / MARKED / EMPTY）
     * @param index      処理中のヒントインデックス
     * @param pos        配置開始位置
     * @param filledCount 各セルが FILLED だった有効パターン数
     * @param emptyCount  各セルが EMPTY  だった有効パターン数
     * @param validCount  有効パターン総数（int[1] で参照渡し）
     */
    private void countPatternsWithCommit(Cell[] work, ArrayList<Integer> q,
                                         Cell[] line, int index, int pos,
                                         int[] filledCount, int[] emptyCount,
                                         int[] validCount) {
        int len = work.length;

        if (index == q.size()) {
            // 残りをすべて EMPTY で埋める
            for (int i = pos; i < len; i++) {
                work[i] = new Cell(CellState.EMPTY);
            }

            // 現在の確定情報と矛盾チェック
            for (int i = pos; i < len; i++) {
                if (!isCompatible(line[i], work[i])) return;
            }

            // 有効パターン → カウント更新
            validCount[0]++;
            for (int i = 0; i < len; i++) {
                if (work[i].getState() == CellState.FILLED) {
                    filledCount[i]++;
                } else {
                    emptyCount[i]++;
                }
            }
            return;
        }

        int blockSize = q.get(index);

        // このブロック以降に必要な最小長
        int minRemaining = 0;
        for (int i = index; i < q.size(); i++) {
            minRemaining += q.get(i);
        }
        minRemaining += (q.size() - index - 1);

        for (int start = pos; start <= len - minRemaining; start++) {

            // ① [pos, start) を EMPTY で埋め、確定情報と矛盾チェック
            boolean ok = true;
            for (int i = pos; i < start; i++) {
                work[i] = new Cell(CellState.EMPTY);
                if (!isCompatible(line[i], work[i])) {
                    ok = false;
                    break;
                }
            }
            if (!ok) continue;

            // ② [start, start+blockSize) を FILLED で埋め、確定情報と矛盾チェック
            if (start + blockSize > len) continue;
            for (int i = start; i < start + blockSize; i++) {
                work[i] = new Cell(CellState.FILLED);
                if (!isCompatible(line[i], work[i])) {
                    ok = false;
                    break;
                }
            }
            if (!ok) continue;

            int nextPos = start + blockSize;

            // ③ ブロック後のセパレータ（最後のブロック以外）
            if (index < q.size() - 1) {
                if (nextPos >= len) continue;
                work[nextPos] = new Cell(CellState.EMPTY);
                if (!isCompatible(line[nextPos], work[nextPos])) continue;
                nextPos++;
            }

            // ④ 次のブロックへ再帰
            countPatternsWithCommit(work, q, line, index + 1, nextPos,
                                    filledCount, emptyCount, validCount);
        }
    }

    /**
     * work セルが line の確定状態と矛盾しないかチェック。
     * - line が FILLED  → work も FILLED でなければならない
     * - line が MARKED  → work は EMPTY  でなければならない
     * - line が EMPTY（未確定）→ 何でも OK
     */
    private boolean isCompatible(Cell lineCell, Cell workCell) {
        CellState ls = lineCell.getState();
        CellState ws = workCell.getState();
        if (ls == CellState.FILLED && ws != CellState.FILLED) return false;
        if (ls == CellState.MARKED && ws != CellState.EMPTY)  return false;
        return true;
    }
}
