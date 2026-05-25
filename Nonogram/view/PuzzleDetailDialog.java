package Nonogram.view;

import java.time.format.DateTimeFormatter;

import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * パズル詳細ダイアログ
 * パズルの全情報を表示し、編集・ソルバー起動ボタンも提供する
 */
public class PuzzleDetailDialog {

    private Stage ownerStage;
    private Stage dialogStage;

    private Button solverButton;
    private Button closeButton;

    public PuzzleDetailDialog(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    /**
     * ダイアログを初期化する
     *
     * @param puzzle    表示するパズル
     * @param cleared   このパズルをクリア済みかどうか
     * @param isAdmin   編集ボタンを表示するかどうか
     */
    public void initialize(Puzzle puzzle, boolean cleared) {
        dialogStage = new Stage();
        dialogStage.initOwner(ownerStage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("パズル詳細");

        // タイトル
        Label titleValue = new Label(puzzle.getTitle());
        titleValue.setFont(Font.font(null, FontWeight.BOLD, 18));
        titleValue.setWrapText(true);

        // クリア状態
        Label clearedBadge = new Label(cleared ? "★ クリア済み" : "未クリア");
        clearedBadge.setStyle(cleared
                ? "-fx-text-fill: #c8a000; -fx-font-size: 13px;"
                : "-fx-text-fill: #888888; -fx-font-size: 13px;");

        HBox titleRow = new HBox(10, titleValue, clearedBadge);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        // 詳細
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(8);
        grid.setPadding(new Insets(8, 0, 8, 0));

        int row = 0;

        addRow(grid, row++, "ID",     String.valueOf(puzzle.getPuzzleId()));
        addRow(grid, row++, "サイズ", puzzle.getGridSizeX() + " × " + puzzle.getGridSizeY());
        addRow(grid, row++, "難易度", puzzle.getDifficulty().name());

        String publicStr = puzzle.getIsPublic() ? "公開" : "非公開";
        addRow(grid, row++, "公開設定", publicStr);

        addRow(grid, row++, "作成者", puzzle.getCreatedBy());

        String createdAtStr = (puzzle.getCreatedAt() != null)
                ? puzzle.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
                : "不明";
        addRow(grid, row++, "作成日時", createdAtStr);

        // 操作ボタン
        solverButton = new Button("💡 ソルバー");
        closeButton  = new Button("閉じる");

        solverButton.setFont(new Font(13));
        closeButton.setFont(new Font(13));
        closeButton.setStyle("-fx-text-fill: #555555;");

        closeButton.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(10, solverButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        //　組み立て
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                titleRow,
                new Separator(),
                grid,
                new Separator(),
                buttonBox
        );

        Scene scene = new Scene(root, 340, 310);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);
    }

    // ラベル＋値を GridPane に1行追加する
    private void addRow(GridPane grid, int rowIndex, String labelText, String valueText) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font(null, FontWeight.BOLD, 13));
        lbl.setStyle("-fx-text-fill: #555555;");
        lbl.setMinWidth(70);

        Label val = new Label(valueText);
        val.setFont(new Font(13));
        val.setWrapText(true);

        grid.add(lbl, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    // ダイアログを表示（閉じるまでブロック）
    public void show() {
        dialogStage.showAndWait();
    }

    // ダイアログを閉じる（Controllerから呼ぶ用）
    public void close() {
        dialogStage.close();
    }

    public Button getSolverButton() { return solverButton; }
    public Button getCloseButton()  { return closeButton; }
}