package Nonogram.model;

public class Cell {

    private CellState state;

    public Cell(CellState state) {
        this.state = state;
    }
    public Cell() {
        this(CellState.EMPTY);
    }

    public CellState getState() {
        return state;
    }

    public void toggle(CellState cellState) {
        if (cellState == this.state) {
            this.state = CellState.EMPTY;
        } else {
            this.state = cellState;
        }
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isFilled() {
        return state == CellState.FILLED;
    }

    public boolean isMarked() {
        return state == CellState.MARKED;
    }

    public boolean isEmpty() {
        return state == CellState.EMPTY;
    }
}
