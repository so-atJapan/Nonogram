package Nonogram.view;

import Nonogram.model.CellState;
import Nonogram.model.Grid;
import Nonogram.model.ResultModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * リザルト画面を表示するViewクラス
 * 太田
 */
public class ResultView {

    private Stage stage;
    private Scene scene;
    private Label clearTimeLabel;
    private Button backButton;

    /** グリッドに使える最大ピクセル数。画面全体からUI部品の余白分を引いた値 */
    private static final int MAX_GRID_PX = 600;
    /** セルサイズの最小値（これ以上小さくしない） */
    private static final int MIN_CELL_SIZE = 4;
    /** セルサイズの最大値（小さいパズルでも大きくなりすぎないようにする） */
    private static final int MAX_CELL_SIZE = 30;

    /**
     * コンストラクタ
     * 
     * @param stage 画面表示に使用するStage
     */
    public ResultView(Stage stage) {
        this.stage = stage;
    }

    /**
     * リザルト画面の部品を生成する
     * 
     * @param model リザルト画面で表示するデータ
     */
    public void initialize(ResultModel model) {
        Label titleLabel = new Label("クリア");
        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 48));

        Label puzzleTitleLabel = new Label(model.getPuzzle().getTitle());
        puzzleTitleLabel.setFont(new Font(18));

        clearTimeLabel = new Label("経過時間 00:00");
        clearTimeLabel.setFont(new Font(24));
        clearTimeLabel.setAlignment(Pos.CENTER);

        GridPane completedGridPane = createCompletedGrid(model.getCompletedGrid());

        backButton = new Button("問題リストに戻る");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setPrefHeight(36);

        VBox root = new VBox(16, titleLabel, puzzleTitleLabel, clearTimeLabel, completedGridPane, backButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        scene = new Scene(root);
    }

    /**
     * リザルト情報を画面に反映する
     * 
     * @param result 表示するリザルトデータ
     */
    public void displayResult(ResultModel result) {
        updateTimer(result.getTickSeconds());
    }

    /**
     * グリッドのマス数に応じたセルサイズを計算する
     * 縦横それぞれ MAX_GRID_PX に収まるよう逆算し、
     * MIN_CELL_SIZE〜MAX_CELL_SIZE の範囲にクランプする
     *
     * @param grid サイズを参照するグリッド
     * @return セルの1辺のピクセルサイズ
     */
    private int calcCellSize(Grid grid) {
        // 縦と横の大きいほうに合わせてセルサイズを決める
        int maxSide = Math.max(grid.getSizeX(), grid.getSizeY());
        int cellSize = MAX_GRID_PX / maxSide;
        if (cellSize < MIN_CELL_SIZE) {
            cellSize = MIN_CELL_SIZE;
        } else if (cellSize > MAX_CELL_SIZE) {
            cellSize = MAX_CELL_SIZE;
        }
        return cellSize;
    }

    private GridPane createCompletedGrid(Grid grid) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // グリッドのマス数からセルサイズを逆算する
        int cellSize = calcCellSize(grid);

        for (int row = 0; row < grid.getSizeX(); row++) {
            for (int col = 0; col < grid.getSizeY(); col++) {
                Label cell = new Label();
                cell.setPrefSize(cellSize, cellSize);
                cell.setMinSize(cellSize, cellSize);
                cell.setMaxSize(cellSize, cellSize);

                if (grid.getCellAt(row, col).getState() == CellState.FILLED) {
                    cell.setStyle("-fx-background-color: #222222; -fx-border-color: #cccccc; -fx-border-width: 0.5;");
                } else {
                    cell.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0.5;");
                }

                gridPane.add(cell, col, row);
            }
        }

        return gridPane;
    }

    /**
     * リザルト画面をStageに表示する
     */
    public void render() {
        stage.setTitle("Nonogram Result");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * 経過時間の表示を更新する
     * 
     * @param totalSeconds クリアまでにかかった秒数
     */
    public void updateTimer(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        clearTimeLabel.setText(String.format("経過時間 %02d:%02d", minutes, seconds));

    }

    /**
     * 問題リストに戻るボタンを取得する
     * 
     * @return 問題リストに戻るボタン
     */
    public Button showHomeButton() {
        return backButton;
    }
}