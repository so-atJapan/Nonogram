package Nonogram.model;

public class SolverModel {

    private final Puzzle PUZZLE;
    private Grid grid;
    private GridLog gridLog;

    public SolverModel(Puzzle puzzle) {
        this.PUZZLE = puzzle;

        grid = new Grid(puzzle.getGridSizeX(), puzzle.getGridSizeY());

        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                grid.setCellAt(x, y, CellState.EMPTY);
            }
        }

        this.gridLog = new GridLog(grid);
    }

    public Puzzle getPUZZLE() {
        return PUZZLE;
    }

    public Grid getGrid() {
        return grid;
    }

    public void toggle(int x, int y, CellState cellState) {
        grid.getCellAt(x, y).toggle(cellState);
    }

    public void gridReSize(){
        Grid reSizedGrid = new Grid(PUZZLE.getGridSizeX(), PUZZLE.getGridSizeY());
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
        for (int x = 0; x < PUZZLE.getGridSizeX(); x++) {
            for (int y = 0; y < PUZZLE.getGridSizeY(); y++) {
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

    public void updatePuzzleGridSizeX(int gridSizeX){
        PUZZLE.setGridSizeX(gridSizeX);
    }

    public void updatePuzzleGridSizeY(int gridSizeY){
        PUZZLE.setGridSizeY(gridSizeY);
    }
}
