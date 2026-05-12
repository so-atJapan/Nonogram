package Nonogram.model;

public class PuzzleEditorModel {

    private Puzzle puzzle;
    private Grid grid;

    // コンストラクタ
    public PuzzleEditorModel(Puzzle puzzle) {
        this.puzzle = puzzle;

        // 初期化
        grid = new Grid(puzzle.getGridSizeX(), puzzle.getGridSizeY());

        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                switch (puzzle.getSolution().getCellAt(x, y).getState()) {
                    case FILLED:
                        grid.setCellAt(x, y, CellState.FILLED);
                        break;
                    default:
                        grid.setCellAt(x, y, CellState.EMPTY);
                        break;
                }
            }
        }
    }

    // Puzzle取得
    public Puzzle getPuzzle() {
        return puzzle;
    }

    // 全セル取得
    public Grid getGrid() {
        return grid;
    }

    // セル状態切り替え
    public void toggle(int x, int y) {
        grid.getCellAt(x, y).toggle();
    }

    // 盤面リセット
    public void reset() {
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid.getCellAt(x, y).setState(CellState.EMPTY);
            }
        }
    }

    public void updatePuzzleTitle(String title){
        puzzle.setTitle(title);
    }
}
