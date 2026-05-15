package Nonogram.model;

public class ResultModel {

    private Puzzle puzzle;
    private Grid completedGrid;
    private int clearTimeSeconds;
    // private PlayRecord record; // TODO: PlayRecordクラス実装後、記録保存用に追加予定

    public ResultModel(Puzzle puzzle, Grid completedGrid, int elapsedSeconds) {
        this.puzzle = puzzle;
        this.completedGrid = completedGrid;
        this.clearTimeSeconds = elapsedSeconds;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public Grid getCompletedGrid() {
        return completedGrid;
    }

    public int getTickSeconds() {
        return clearTimeSeconds;
    }

    // public PlayRecord getRecord() {
    //     return record;
    // }
}
