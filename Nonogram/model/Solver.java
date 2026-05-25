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

    private static final int S_UNKNOWN = 0;
    private static final int S_FILLED  = 1;
    private static final int S_MARKED  = 2;

    private void step1Line(ArrayList<Integer> question, Cell[] line) {
        int len = line.length;
        int k   = question.size();

        // Cell[] → int[] 変換
        int[] ls = new int[len];
        for (int i = 0; i < len; i++) {
            CellState cs = line[i].getState();
            if      (cs == CellState.FILLED) ls[i] = S_FILLED;
            else if (cs == CellState.MARKED) ls[i] = S_MARKED;
        }

        // ヒントを int[] に
        int[] q = new int[k];
        for (int i = 0; i < k; i++) q[i] = question.get(i);

        // -------------------------------------------------------
        // 前向き DP
        // fwd[i][j] = 「ブロック j-1 まで配置し終えて位置 i にいる」パターン数
        //
        //   j == 0          : まだブロックを1つも置いていない
        //   j == 1..k       : ブロック j-1 を置き終えた直後
        //
        // 遷移:
        //   EMPTY セルを消費 (スペース or ブロック後スペース):
        //     fwd[i+1][j] += fwd[i][j]   (ls[i] != S_FILLED)
        //   ブロック j を pos=i から置く:
        //     fwd[i + q[j] + 1][j+1] += fwd[i][j]  or  fwd[i + q[j]][k] if j==k-1
        //     ただし [i, i+q[j]) に MARKED がなく、
        //            最後のブロック以外は i+q[j] が FILLED でないこと
        // -------------------------------------------------------
        long[][] fwd = new long[len + 1][k + 1];
        fwd[0][0] = 1L;

        for (int i = 0; i < len; i++) {
            for (int j = 0; j <= k; j++) {
                if (fwd[i][j] == 0) continue;
                long ways = fwd[i][j];

                if (j == k) {
                    // すべてのブロックを置き終えた → 残りは EMPTY のみ
                    if (ls[i] != S_FILLED) {
                        fwd[i + 1][k] += ways;
                    }
                    continue;
                }

                // ① 現在位置を EMPTY として読み飛ばす（ls[i] が FILLED でなければ）
                if (ls[i] != S_FILLED) {
                    fwd[i + 1][j] += ways;
                }

                // ② ブロック j を位置 i から置く
                int blen = q[j];
                if (i + blen > len) continue;

                // ブロック範囲に MARKED がないか確認
                boolean canPlace = true;
                for (int bi = i; bi < i + blen; bi++) {
                    if (ls[bi] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int after = i + blen;
                if (j == k - 1) {
                    // 最後のブロック：後続スペース不要
                    fwd[after][k] += ways;
                } else {
                    // 最後以外：直後は FILLED であってはならない
                    if (after < len && ls[after] != S_FILLED) {
                        fwd[after + 1][j + 1] += ways;
                    }
                }
            }
        }

        long total = fwd[len][k];
        if (total == 0) return; // 矛盾（解なし）

        // -------------------------------------------------------
        // 後向き DP
        // bwd[i][j] = 「位置 i・状態 j からゴールまでの」パターン数
        // -------------------------------------------------------
        long[][] bwd = new long[len + 1][k + 1];
        bwd[len][k] = 1L;

        for (int i = len - 1; i >= 0; i--) {
            for (int j = 0; j <= k; j++) {

                if (j == k) {
                    if (ls[i] != S_FILLED) {
                        bwd[i][k] += bwd[i + 1][k];
                    }
                    continue;
                }

                // ① EMPTY として読み飛ばす
                if (ls[i] != S_FILLED) {
                    bwd[i][j] += bwd[i + 1][j];
                }

                // ② ブロック j を位置 i から置く
                int blen = q[j];
                if (i + blen > len) continue;

                boolean canPlace = true;
                for (int bi = i; bi < i + blen; bi++) {
                    if (ls[bi] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int after = i + blen;
                if (j == k - 1) {
                    bwd[i][j] += bwd[after][k];
                } else {
                    if (after < len && ls[after] != S_FILLED) {
                        bwd[i][j] += bwd[after + 1][j + 1];
                    }
                }
            }
        }

        // -------------------------------------------------------
        // 各セルの FILLED 寄与数を計算
        //
        // セル i が FILLED になるのは「ブロック j が位置 s から始まり
        // s <= i < s+q[j]」を満たすすべての (j, s) の組み合わせ。
        //
        // FILLED 寄与数 = Σ_{j,s} fwd[s][j] * bwd[s+q[j]+sep][j+1]
        //   ただし s <= i < s+q[j]
        //
        // 差分配列で O(n*k) に抑える:
        //   ブロック (j,s) が確定したとき diff[s] += 寄与, diff[s+q[j]] -= 寄与
        // -------------------------------------------------------
        long[] filledWays = new long[len];
        long[] diff = new long[len + 1];

        for (int j = 0; j < k; j++) {
            int blen = q[j];
            for (int s = 0; s + blen <= len; s++) {
                if (fwd[s][j] == 0) continue;

                // ブロック j を s に置けるか
                boolean canPlace = true;
                for (int bi = s; bi < s + blen; bi++) {
                    if (ls[bi] == S_MARKED) { canPlace = false; break; }
                }
                if (!canPlace) continue;

                int after = s + blen;
                long bwdVal;
                if (j == k - 1) {
                    bwdVal = bwd[after][k];
                } else {
                    if (after >= len || ls[after] == S_FILLED) continue;
                    bwdVal = bwd[after + 1][j + 1];
                }

                long contrib = fwd[s][j] * bwdVal;
                if (contrib == 0) continue;
                diff[s]        += contrib;
                diff[s + blen] -= contrib;
            }
        }

        // 差分配列を累積して filledWays に変換
        long running = 0;
        for (int i = 0; i < len; i++) {
            running += diff[i];
            filledWays[i] = running;
        }

        // 確定セルを書き戻し
        for (int i = 0; i < len; i++) {
            if      (filledWays[i] == total)          line[i].setState(CellState.FILLED);
            else if (filledWays[i] == 0)              line[i].setState(CellState.MARKED);
        }
    }
}