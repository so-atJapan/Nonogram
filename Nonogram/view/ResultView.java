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

    private final Stage STAGE;
    private Scene scene;
    private Label clearTimeLabel;
    private Button backButton;

    /** グリッドに使える最大ピクセル数 */
    private static final int MAX_GRID_PX = 600;
    /** セルサイズの最小値 */
    private static final int MIN_CELL_SIZE = 4;
    /** セルサイズの最大値 */
    private static final int MAX_CELL_SIZE = 30;

    public ResultView(Stage stage) {
        this.STAGE = stage;
    }

    public void initialize(ResultModel model) {
        Label titleLabel = new Label("クリア");
        titleLabel.setFont(Font.font(null, FontWeight.BOLD, 48));

        Label puzzleTitleLabel = new Label(model.getPUZZLE().getTitle());
        puzzleTitleLabel.setFont(new Font(18));

        clearTimeLabel = new Label("経過時間 00:00");
        clearTimeLabel.setFont(new Font(24));
        clearTimeLabel.setAlignment(Pos.CENTER);

        GridPane completedGridPane = createCompletedGrid(model.getCOMPLETED_GRID());

        backButton = new Button("問題リストに戻る");
        backButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setPrefHeight(36);

        VBox root = new VBox(16, titleLabel, puzzleTitleLabel, clearTimeLabel, completedGridPane, backButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));

        scene = new Scene(root);
    }

    public void displayResult(ResultModel result) {
        updateTimer(result.getTickSeconds());
    }

    private int calcCellSize(Grid grid) {
        final int MAX_SIZE = Math.max(grid.getSizeX(), grid.getSizeY());
        int cellSize = MAX_GRID_PX / MAX_SIZE;
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

        final int CELL_SIZE = calcCellSize(grid);

        for (int row = 0; row < grid.getSizeX(); row++) {
            for (int col = 0; col < grid.getSizeY(); col++) {
                Label cell = new Label();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setMinSize(CELL_SIZE, CELL_SIZE);
                cell.setMaxSize(CELL_SIZE, CELL_SIZE);

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

    public void render() {
        STAGE.setTitle("Nonogram Result");
        STAGE.setScene(scene);
        STAGE.setResizable(false);
        STAGE.sizeToScene();
        STAGE.centerOnScreen();
        STAGE.show();
    }

    public void updateTimer(int totalSeconds) {
        final int MINUTES = totalSeconds / 60;
        final int SECONDS = totalSeconds % 60;
        clearTimeLabel.setText(String.format("経過時間 %02d:%02d", MINUTES, SECONDS));
    }

    public Button showHomeButton() {
        return backButton;
    }
}
