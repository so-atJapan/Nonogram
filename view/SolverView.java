package Nonogram.view;

import Nonogram.model.Grid;
import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SolverView {

    private Stage stage;
    private Scene scene;

    private GridPane gridPanel;
    private Button[][] buttons;
    private Button prevButton;
    private Button nextButton;
    private Button checkButton;
    private Label timerLabel;

    // ヒント入力フィールド: [行インデックス][スロットインデックス（左→右、maxRowHintCols個固定）]
    private TextField[][] rowHintFields;
    // ヒント入力フィールド: [列インデックス][スロットインデックス（上→下、maxColHintRows個固定）]
    private TextField[][] colHintFields;

    private int rows;
    private int cols;
    private int maxRowHintCols;
    private int maxColHintRows;

    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private final int cellSize = 20;

    // コンストラクト
    public SolverView(Stage stage) {
        this.stage = stage;
    }

    // 初期化
    public void initialize(Puzzle puzzle) {

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();

        ArrayList<ArrayList<Integer>> rowHints = puzzle.getClue().getRowClues();
        ArrayList<ArrayList<Integer>> colHints = puzzle.getClue().getColClues();

        // ヒントの最大数から左・上のヒントエリアサイズを決定
        maxColHintRows = colHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        maxRowHintCols = rowHints.stream().mapToInt(ArrayList::size).max().orElse(1);

        // すべて cellSize 単位で計算
        int hintAreaWidth  = maxRowHintCols * cellSize; // 左ヒントエリアの幅
        int hintAreaHeight = maxColHintRows * cellSize; // 上ヒントエリアの高さ

        // ===== 左上コーナー（タイマー）=====
        timerLabel = new Label("00:00");
        timerLabel.setPrefSize(hintAreaWidth, hintAreaHeight);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // ===== 列ヒント（上）=====
        // 各列を maxColHintRows 個の TextField（上→下）で統一
        colHintFields = new TextField[cols][maxColHintRows];
        HBox hintPanelTop = new HBox();
        hintPanelTop.setSpacing(0);

        for (int col = 0; col < cols; col++) {
            VBox colBox = new VBox();
            colBox.setPrefSize(cellSize, hintAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);

            ArrayList<Integer> hints = colHints.get(col);
            int padding = maxColHintRows - hints.size();

            for (int slot = 0; slot < maxColHintRows; slot++) {
                // slot < padding の間は空白マス、以降は実際のヒント値
                String initialValue = (slot < padding) ? "" : String.valueOf(hints.get(slot - padding));
                TextField tf = createHintField(initialValue);
                colHintFields[col][slot] = tf;
                colBox.getChildren().add(tf);
            }
            hintPanelTop.getChildren().add(colBox);
        }

        // ===== 行ヒント（左）=====
        // 各行を maxRowHintCols 個の TextField（左→右）で統一
        rowHintFields = new TextField[rows][maxRowHintCols];
        VBox hintPanelSide = new VBox();
        hintPanelSide.setSpacing(0);

        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(hintAreaWidth, cellSize);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);

            ArrayList<Integer> hints = rowHints.get(row);
            int padding = maxRowHintCols - hints.size();

            for (int slot = 0; slot < maxRowHintCols; slot++) {
                // slot < padding の間は空白マス、以降は実際のヒント値
                String initialValue = (slot < padding) ? "" : String.valueOf(hints.get(slot - padding));
                TextField tf = createHintField(initialValue);
                rowHintFields[row][slot] = tf;
                rowBox.getChildren().add(tf);
            }
            hintPanelSide.getChildren().add(rowBox);
        }

        // ===== グリッド =====
        gridPanel = new GridPane();
        buttons = new Button[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button btn = new Button();
                btn.setPrefSize(cellSize, cellSize);
                btn.setFocusTraversable(false);
                applyCellStyle(btn, "empty", row, col);
                buttons[row][col] = btn;
                gridPanel.add(btn, col, row);
            }
        }

        // ===== ボタン（下）=====
        prevButton = new Button("<");
        prevButton.setPrefHeight(36);
        prevButton.setPrefWidth(48);

        nextButton = new Button(">");
        nextButton.setPrefHeight(36);
        nextButton.setPrefWidth(48);

        checkButton = new Button("確認");
        checkButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setPrefHeight(36);

        HBox bottomRow = new HBox(prevButton, nextButton, checkButton);
        HBox.setHgrow(checkButton, javafx.scene.layout.Priority.ALWAYS);
        bottomRow.setSpacing(0);

        // ===== 全体レイアウト =====
        //
        //  [ cornerLabel   | hintPanelTop  ]
        //  [ hintPanelSide | gridPanel     ]
        //  [   bottomRow（全幅）           ]
        //
        HBox topRow = new HBox(timerLabel, hintPanelTop);
        topRow.setSpacing(0);

        HBox midRow = new HBox(hintPanelSide, gridPanel);
        midRow.setSpacing(0);

        VBox root = new VBox(topRow, midRow, bottomRow);
        root.setSpacing(0);
        root.setPadding(new Insets(8));

        scene = new Scene(root);
    }

    /**
     * ヒントマス用 TextField を生成する。
     * - 数字のみ入力可（1〜2桁）
     * - Enter キーまたはフォーカスアウト時に確定（不正値は空欄に戻す）
     * - 空欄は「ヒントなし」を意味する（0 への補正はしない）
     */
    private TextField createHintField(String initialValue) {
        TextField tf = new TextField(initialValue);
        tf.setPrefSize(cellSize, cellSize);
        tf.setAlignment(Pos.CENTER);
        tf.setStyle(
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 0.5;" +
            "-fx-font-size: 11px;" +
            "-fx-padding: 0;" +
            "-fx-background-insets: 0;" +
            "-fx-background-radius: 0;"
        );

        // 入力を数字2桁以内に制限（空欄は許可）
        tf.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d{0,2}")) {
                tf.setText(oldVal);
            }
        });

        // Enter キーでフォーカスを外す（フォーカスアウトで検証が走る）
        tf.setOnAction(e -> tf.getParent().requestFocus());

        return tf;
    }

    // パズル描画
    public void render() {
        stage.setTitle("Nonogram");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    // セル更新（row, col の順で統一）
    public void updateCell(int row, int col, Grid grid) {
        Button btn = buttons[row][col];

        switch (grid.getCellAt(row, col).getState()) {
            case FILLED:
                applyCellStyle(btn, "filled", row, col);
                btn.setText("");
                break;
            case MARKED:
                applyCellStyle(btn, "marked", row, col);
                btn.setText("✕");
                break;
            default:
                applyCellStyle(btn, "empty", row, col);
                btn.setText("");
                break;
        }
    }

    public void updateCellAll(Grid grid) {
        for (int x = 0; x < grid.getSizeX(); x++) {
            for (int y = 0; y < grid.getSizeY(); y++) {
                updateCell(x, y, grid);
            }
        }
    }

    // セルスタイル適用
    private void applyCellStyle(Button btn, String state, int row, int col) {
        // 5マスごとに太い線（0行目・0列目も太く）
        double top    = (row % 5 == 0) ? 2.0 : 0.5;
        double left   = (col % 5 == 0) ? 2.0 : 0.5;
        // 右端・下端も太く
        double bottom = (row == rows - 1) ? 2.0 : 0.5;
        double right  = (col == cols - 1) ? 2.0 : 0.5;

        String border =
            "-fx-border-color: #555555;" +
            "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";";

        String base = border +
            "-fx-padding: 0;" +
            "-fx-font-size: 11px;";

        switch (state) {
            case "filled":
                btn.setStyle(base + "-fx-background-color: #222222; -fx-text-fill: #222222;");
                break;
            case "marked":
                btn.setStyle(base + "-fx-background-color: #f0f0f0; -fx-text-fill: #cc0000;");
                break;
            default:
                btn.setStyle(base + "-fx-background-color: white; -fx-text-fill: black;");
                break;
        }
    }

    // タイマー更新（Controller から秒数で受け取り mm:ss 形式で表示）
    public void updateTimer(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // 結果表示
    public void showResult(boolean result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("結果");
        alert.setHeaderText(null);
        alert.setContentText(result ? "クリア！おめでとう！" : "不正解... もう一度！");
        alert.showAndWait();
    }

    public Stage getStage() { return stage; }
    public Button[][] getButtons() { return buttons; }
    public Button getCheckButton() { return checkButton; }
    public Button getPrevButton() { return prevButton; }
    public Button getNextButton() { return nextButton; }

    /**
     * 行ヒントの全 TextField を返す。
     * rowHintFields[row][slot]  slot: 0=左端 〜 maxRowHintCols-1=右端
     * 空欄（""）はそのスロットにヒントなしを意味する。
     */
    public TextField[][] getRowHintFields() { return rowHintFields; }

    /**
     * 列ヒントの全 TextField を返す。
     * colHintFields[col][slot]  slot: 0=上端 〜 maxColHintRows-1=下端
     * 空欄（""）はそのスロットにヒントなしを意味する。
     */
    public TextField[][] getColHintFields() { return colHintFields; }

    /**
     * 指定行のヒント値リストを返す（空欄スロットは除外）。
     * Controller 側で Clue を再構築する際に使用する。
     */
    public ArrayList<Integer> getRowHintValues(int row) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int slot = 0; slot < maxRowHintCols; slot++) {
            String text = rowHintFields[row][slot].getText().trim();
            if (!text.isEmpty()) {
                values.add(Integer.parseInt(text));
            }
        }
        return values;
    }

    /**
     * 指定列のヒント値リストを返す（空欄スロットは除外）。
     * Controller 側で Clue を再構築する際に使用する。
     */
    public ArrayList<Integer> getColHintValues(int col) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int slot = 0; slot < maxColHintRows; slot++) {
            String text = colHintFields[col][slot].getText().trim();
            if (!text.isEmpty()) {
                values.add(Integer.parseInt(text));
            }
        }
        return values;
    }
}