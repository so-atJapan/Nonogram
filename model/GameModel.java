package Nonogram.model;

public class GameModel {

    private Puzzle puzzle;
    private Cell[][] grid;

    // コンストラクタ
    public GameModel(Puzzle puzzle) {
        this.puzzle = puzzle;

        // 初期化
        grid = new Cell[puzzle.getGridSizeX()][puzzle.getGridSizeY()];

        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid[x][y] = new Cell();
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
        grid[x][y].toggle();
    }

    // 正誤判定
    public boolean check() {
        Grid solution = puzzle.getSolution();

        for (int x = 0; x < solution.getSizeX(); x++) {
            for (int y = 0; y < solution.getSizeY(); y++) {

                boolean shouldBeFilled = solution.getCellAt(x, y).getState() == CellState.FILLED;
                boolean isFilled = grid[x][y].getState() == CellState.FILLED;

                if (shouldBeFilled != isFilled) {
                    return false;
                }
            }
        }
        return true;
    }

    // 盤面リセット
    public void reset() {
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid[x][y].setState(CellState.EMPTY);
            }
        }
    }
}
