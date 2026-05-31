package Nonogram.model;

/**
 * リザルト画面で表示する結果データを保持するモデルクラス
 * 太田
 */
public class ResultModel {

    private final Puzzle PUZZLE;
    private final Grid COMPLETED_GRID;
    private final int CLEAR_TIME_SECONDS;

    /**
     * コンストラクタ
     *
     * @param puzzle          クリアしたパズル
     * @param completedGrid   完成時の盤面
     * @param tickSeconds  クリアまでにかかった秒数
     */
    public ResultModel(Puzzle puzzle, Grid completedGrid, int tickSeconds) {
        this.PUZZLE = puzzle;
        this.COMPLETED_GRID = completedGrid;
        this.CLEAR_TIME_SECONDS = tickSeconds;
    }

    public Puzzle getPUZZLE() {
        return PUZZLE;
    }

    public Grid getCOMPLETED_GRID() {
        return COMPLETED_GRID;
    }

    public int getTickSeconds() {
        return CLEAR_TIME_SECONDS;
    }
}
