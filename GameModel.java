package Model;

public class GameModel {

    private Puzzle puzzle;
    private Cell[][] grid;

    // コンストラクタ
    public GameModel() {
        this.puzzle = new Puzzle();

        // 初期化
        grid = new Cell[puzzle.gridSizeY][puzzle.gridSizeX];

        for (int y = 0; y < puzzle.gridSizeY; y++) {
            for (int x = 0; x < puzzle.gridSizeX; x++) {
                grid[y][x] = new Cell();
            }
        }
    }

    // Puzzle取得
    public Puzzle getPuzzle() {
        return puzzle;
    }

    // 全セル取得
    public Cell[][] getCell() {
        return grid;
    }

    // セル状態切り替え
    public void toggle(int x, int y) {
        grid[y][x].toggle();
    }

    // 正誤判定
    public boolean check() {
        boolean[][] solution = puzzle.solution;

        for (int y = 0; y < puzzle.gridSizeY; y++) {
            for (int x = 0; x < puzzle.gridSizeX; x++) {

                boolean shouldBeFilled = solution[y][x];
                boolean isFilled = grid[y][x].getState() == CellState.FILLED;

                if (shouldBeFilled != isFilled) {
                    return false;
                }
            }
        }
        return true;
    }

    // 盤面リセット
    public void reset() {
        for (int y = 0; y < puzzle.gridSizeY; y++) {
            for (int x = 0; x < puzzle.gridSizeX; x++) {
                grid[y][x].setState(CellState.EMPTY);
            }
        }
    }
}