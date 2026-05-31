package Nonogram.model;

public class PuzzleEditorModel {

    private Puzzle puzzle;
    private Grid grid;
    private GridLog gridLog;

    private static final DAO DAO = new DAO();

    /** 編集時のコンストラクタ */
    public PuzzleEditorModel(Puzzle puzzle) {
        this.puzzle = puzzle;

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

        this.gridLog = new GridLog(grid);
    }

    /** 新規作成時のコンストラクタ */
    public PuzzleEditorModel(Player editPlayer) {
        this(new Puzzle());
        this.puzzle.setCreatedBy(editPlayer);
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public Grid getGrid() {
        return grid;
    }

    public void toggle(int x, int y, CellState cellState) {
        grid.getCellAt(x, y).toggle(cellState);
    }

    public void gridReSize(){
        Grid reSizedGrid = new Grid(puzzle.getGridSizeX(), puzzle.getGridSizeY());
        Grid oldGrid = this.grid;

        for (int x = 0; x < oldGrid.getSizeX(); x++) {
            for (int y = 0; y < oldGrid.getSizeY(); y++) {
                if (x >= reSizedGrid.getSizeX() || y >= reSizedGrid.getSizeY()) continue;
                switch (oldGrid.getCellAt(x, y).getState()) {
                    case FILLED:
                        reSizedGrid.setCellAt(x, y, CellState.FILLED);
                        break;
                    default:
                        reSizedGrid.setCellAt(x, y, CellState.EMPTY);
                        break;
                }
            }
        }

        this.grid = reSizedGrid;
    }

    public void reset() {
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid.getCellAt(x, y).setState(CellState.EMPTY);
            }
        }
    }

    public void setState(int x, int y, CellState state) {
        grid.getCellAt(x, y).setState(state);
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void pushGridLog(){
        this.gridLog.push(this.grid);
        this.grid = this.gridLog.get();
    }

    public void undoGridLog(){
        this.gridLog.undo();
        this.grid = this.gridLog.get();
    }

    public void redoGridLog(){
        this.gridLog.redo();
    }

    public Grid getCurrentLog(){
        return this.gridLog.get();
    }

    public void updatePuzzleTitle(String title){
        puzzle.setTitle(title);
    }

    public void updatePuzzleGridSizeX(int gridSizeX){
        puzzle.setGridSizeX(gridSizeX);
    }

    public void updatePuzzleGridSizeY(int gridSizeY){
        puzzle.setGridSizeY(gridSizeY);
    }

    public void deletePuzzle(){
        DAO.deletePuzzle(puzzle.getPuzzleId());
    }

    public void updateDB(){
        puzzle.setClue(this.grid);
        puzzle.setSolution(this.grid);
        puzzle.setDifficultyBySize();

        if (puzzle.getPuzzleId() == -1) {
            DAO.setPuzzle(this.puzzle);
        } else {
            DAO.updatePuzzle(this.puzzle);
        }
    }
}
