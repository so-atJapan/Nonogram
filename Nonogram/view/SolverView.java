package Nonogram.view;
 
import java.util.ArrayList;

import Nonogram.controller.AppController;

import Nonogram.model.Grid;
import Nonogram.model.Puzzle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
 
public class SolverView {

    private SolverSettingView semiModal;
 
    private Stage stage;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button settingButton;
    private Button checkButton;
    private Label timerLabel;

    private ScrollPane scrollPane;
    private HBox midRow;

    // ヒント入力フィールド: [行インデックス][スロットインデックス（左→右、maxRowClueCols個固定）]
    private TextField[][] rowClueFields;
    // ヒント入力フィールド: [列インデックス][スロットインデックス（上→下、maxColClueRows個固定）]
    private TextField[][] colClueFields;

    private MenuItemBar menuItemBar;

    private int rows;
    private int cols;
    private int maxRowClueCols;
    private int maxColClueRows;

    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private final int cellSize = 20;
 
    // コンストラクト
    public SolverView(Stage stage) {
        this.stage = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle, AppController appController){

        this.semiModal = new SolverSettingView(this.stage);
        this.semiModal.initialize(puzzle);

        menuItemBar = new MenuItemBar(appController);

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();
    
        ArrayList<ArrayList<Integer>> rowClues = puzzle.getClue().getRowClues();
        ArrayList<ArrayList<Integer>> colClues = puzzle.getClue().getColClues();

        // ヒントの最大数から左・上のヒントエリアサイズを決定
        maxColClueRows = colClues.stream().mapToInt(ArrayList::size).max().orElse(1);
        maxRowClueCols = rowClues.stream().mapToInt(ArrayList::size).max().orElse(1);

        // すべて cellSize 単位で計算
        int clueAreaWidth  = maxRowClueCols * cellSize; // 左ヒントエリアの幅
        int clueAreaHeight = maxColClueRows * cellSize; // 上ヒントエリアの高さ

        // ===== 左上コーナー（タイマー）=====
        timerLabel = new Label("0 s");
        timerLabel.setPrefSize(clueAreaWidth, clueAreaHeight);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // ===== 列ヒント（上）=====
        // 各列を maxClueRows 個の TextField（上→下）で統一
        colClueFields = new TextField[cols][maxColClueRows];
        HBox cluePanelTop = new HBox();
        cluePanelTop.setSpacing(0);

        for (int col = 0; col < cols; col++) {
            VBox colBox = new VBox();
            colBox.setPrefSize(cellSize, clueAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);

            ArrayList<Integer> clues = colClues.get(col);
            int padding = maxColClueRows - clues.size();

            for (int slot = 0; slot < maxColClueRows; slot++) {
                // slot < padding の間は空白マス、以降は実際のヒント値
                String initialValue = (slot < padding) ? "" : String.valueOf(clues.get(slot - padding));
                TextField tf = createClueField(initialValue);
                colClueFields[col][slot] = tf;
                colBox.getChildren().add(tf);
            }
            cluePanelTop.getChildren().add(colBox);
        }

        // ===== 行ヒント（左）=====
        // 各行を maxClueCols 個の TextField（左→右）で統一
        rowClueFields = new TextField[rows][maxRowClueCols];
        VBox cluePanelSide = new VBox();
        cluePanelSide.setSpacing(0);

        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(clueAreaWidth, cellSize);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);

            ArrayList<Integer> clues = rowClues.get(row);
            int padding = maxRowClueCols - clues.size();

            for (int slot = 0; slot < maxRowClueCols; slot++) {
                // slot < padding の間は空白マス、以降は実際のヒント値
                String initialValue = (slot < padding) ? "" : String.valueOf(clues.get(slot - padding));
                TextField tf = createClueField(initialValue);
                rowClueFields[row][slot] = tf;
                rowBox.getChildren().add(tf);
            }
            cluePanelSide.getChildren().add(rowBox);
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
    
        // ===== 下部ボタン（設定 : 自動回答 = 1 : 3）=====
        settingButton = new Button("設定");
        checkButton = new Button("自動回答");

        // 横方向に伸ばせるようにする
        settingButton.setMaxWidth(Double.MAX_VALUE);
        checkButton.setMaxWidth(Double.MAX_VALUE);

        // 高さ統一
        settingButton.setPrefHeight(36);
        checkButton.setPrefHeight(36);

        // 比率設定
        HBox.setHgrow(settingButton, Priority.ALWAYS);
        HBox.setHgrow(checkButton, Priority.ALWAYS);

        // 下部ボタンレイアウト
        HBox bottomButtons = new HBox(settingButton, checkButton);
        bottomButtons.setSpacing(0);

        // 1:3 の比率
        settingButton.prefWidthProperty().bind(bottomButtons.widthProperty().multiply(0.25));
        checkButton.prefWidthProperty().bind(bottomButtons.widthProperty().multiply(0.75));


        // ===== 全体レイアウト =====
        //
        //  [ cornerLabel   | cluePanelTop  ]
        //  [ cluePanelSide | gridPanel     ]
        //  [   bottomRow（全幅）           ]
        //
        HBox topRow = new HBox(timerLabel, cluePanelTop);
        topRow.setSpacing(0);

        midRow = new HBox(cluePanelSide, gridPanel);
        midRow.setSpacing(0);

        // パズルエリア（スクロール対象：メニューバーと確認ボタンは除く）
        VBox puzzleArea = new VBox(topRow, midRow);
        puzzleArea.setSpacing(0);

        scrollPane = new ScrollPane(puzzleArea);
        scrollPane.setFitToWidth(false);   // コンテンツを縮小しない
        scrollPane.setFitToHeight(false);  // コンテンツを縮小しない
        scrollPane.setPannable(false);

        // スクロール領域の最大サイズ（この値を超えたらスクロールバーが出る）
        final int MAX_VIEW_WIDTH  = 1000;
        final int MAX_VIEW_HEIGHT = 800;

        // 実際のパズルサイズを計算
        int maxRowHintColsForSize = rowClues.stream().mapToInt(ArrayList::size).max().orElse(1);
        int maxColHintRowsForSize = colClues.stream().mapToInt(ArrayList::size).max().orElse(1);
        int puzzleWidth  = (maxRowHintColsForSize + cols) * cellSize ;
        int puzzleHeight = (maxColHintRowsForSize + rows) * cellSize ;

        scrollPane.setPrefViewportWidth( Math.min(puzzleWidth,  MAX_VIEW_WIDTH));
        scrollPane.setPrefViewportHeight(Math.min(puzzleHeight, MAX_VIEW_HEIGHT));

        VBox root = new VBox(menuItemBar.getMenuBar(), scrollPane, bottomButtons);
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
    private TextField createClueField(String initialValue) {
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

    //セミモーダル描画
    public void semiModalRender(Puzzle puzzle){
        this.semiModal.setTitleTextField(puzzle.getTitle());
        this.semiModal.setRowTextField(puzzle.getGridSizeX());
        this.semiModal.setColTextField(puzzle.getGridSizeY());
        //TODO
        this.semiModal.render();
    }
    
    // 設定確定
    public void settingConfirm(){
        this.semiModal.settingConfirm();
    }

    // セル更新（row, col の順で統一）
    public void updateCell(int x, int y, Grid grid) {
        Button btn = buttons[x][y];

        switch (grid.getCellAt(x, y).getState()) {
            case FILLED:
                applyCellStyle(btn, "filled", x, y);
                btn.setText("");
                break;
            case MARKED:
                applyCellStyle(btn, "marked", x, y);
                btn.setText("✕");
                break;
            default:
                applyCellStyle(btn, "empty", x, y);
                btn.setText("");
                break;
        }
    }

    // グリッド更新
    public void gridReSize(Grid grid) {

        this.gridPanel = new GridPane();
        this.buttons = new Button[grid.getSizeX()][grid.getSizeY()];
        this.rows = grid.getSizeX();
        this.cols = grid.getSizeY();

        for (int x = 0; x < grid.getSizeX(); x++) {
            for (int y = 0; y < grid.getSizeY(); y++) {
                Button btn = new Button();
                btn.setPrefSize(cellSize, cellSize);
                btn.setFocusTraversable(false);
                applyCellStyle(btn, "empty", x, y);
                
                buttons[x][y] = btn;
                gridPanel.add(btn, y, x);
                
                updateCell(x, y, grid);
            }
        }

        midRow.getChildren().setAll(gridPanel);

        // ===== ScrollPane のビューポートを再調整 =====
        final int MAX_VIEW_WIDTH  = 1000;
        final int MAX_VIEW_HEIGHT = 800;

        int puzzleWidth  = this.cols * cellSize;
        int puzzleHeight = this.rows * cellSize;

        scrollPane.setPrefViewportWidth( Math.min(puzzleWidth,  MAX_VIEW_WIDTH));
        scrollPane.setPrefViewportHeight(Math.min(puzzleHeight, MAX_VIEW_HEIGHT));

        // ウィンドウをコンテンツに合わせて再フィット
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public void clueFieldReSize(Puzzle puzzle, int clueSize) {
        ArrayList<ArrayList<Integer>> rowClues = puzzle.getClue().getRowClues();
        ArrayList<ArrayList<Integer>> colClues = puzzle.getClue().getColClues();

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();

        // clueSize が 0 の場合は自動算出
        int newMaxColClueRows = (clueSize > 0) ? clueSize : colClues.stream().mapToInt(ArrayList::size).max().orElse(1);
        int newMaxRowClueCols = (clueSize > 0) ? clueSize : rowClues.stream().mapToInt(ArrayList::size).max().orElse(1);

        this.maxRowClueCols = newMaxRowClueCols;
        this.maxColClueRows = newMaxColClueRows;

        int clueAreaWidth  = maxRowClueCols * cellSize;
        int clueAreaHeight = maxColClueRows * cellSize;

        // ===== タイマーラベルのサイズ更新 =====
        timerLabel.setPrefSize(clueAreaWidth, clueAreaHeight);

        // ===== 列ヒント（上）再構築（引き継ぎなし・空欄で初期化）=====
        TextField[][] newColClueFields = new TextField[cols][maxColClueRows];
        HBox newCluePanelTop = new HBox();
        newCluePanelTop.setSpacing(0);

        for (int col = 0; col < cols; col++) {
            VBox colBox = new VBox();
            colBox.setPrefSize(cellSize, clueAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);

            for (int slot = 0; slot < maxColClueRows; slot++) {
                TextField tf = createClueField("");
                newColClueFields[col][slot] = tf;
                colBox.getChildren().add(tf);
            }
            newCluePanelTop.getChildren().add(colBox);
        }

        // ===== 行ヒント（左）再構築（引き継ぎなし・空欄で初期化）=====
        TextField[][] newRowClueFields = new TextField[rows][maxRowClueCols];
        VBox newCluePanelSide = new VBox();
        newCluePanelSide.setSpacing(0);

        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(clueAreaWidth, cellSize);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);

            for (int slot = 0; slot < maxRowClueCols; slot++) {
                TextField tf = createClueField("");
                newRowClueFields[row][slot] = tf;
                rowBox.getChildren().add(tf);
            }
            newCluePanelSide.getChildren().add(rowBox);
        }

        // フィールド更新
        this.colClueFields = newColClueFields;
        this.rowClueFields = newRowClueFields;

        // topRow と midRow の子を差し替え
        VBox puzzleArea = (VBox) this.midRow.getParent();
        HBox existingTopRow = (HBox) puzzleArea.getChildren().get(0);
        existingTopRow.getChildren().setAll(timerLabel, newCluePanelTop);
        this.midRow.getChildren().setAll(newCluePanelSide, gridPanel);
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
 
    // タイマー更新（Controller からミリ秒数で受け取り 表示）
    public void updateTimer(double totalMilliseconds) {


        // 秒を計算
        int seconds = (int) ((totalMilliseconds / 1000) % 60);

        // ミリ秒を計算
        int milliseconds = (int) (totalMilliseconds % 1000);

        // mm:ss:SSS 形式で表示
        timerLabel.setText(
            String.format("%01d.%03d s",
                seconds,
                milliseconds)
        );
    }
 
 
    public Stage getStage() { return stage; }
 
    public Button[][] getButtons() { return buttons; }
 
    public Button getSettingButton() { return settingButton; }

    public Button getOkButton() { return this.semiModal.getOkButton(); }
 
    public Button getCheckButton() { return checkButton; }

    public String getTitleTextField(){ return this.semiModal.getTitleTextField(); }

    public int getGridSizeX(){ return this.semiModal.getGridSizeX(); }

    public int getGridSizeY(){ return this.semiModal.getGridSizeY(); }

    public int getClueSize(){ return this.semiModal.getClueSize(); }

    public MenuItemBar getMenuItemBar() { return menuItemBar; }

        public String getRowClueFields() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < rows; row++) {
            StringBuilder rowLine = new StringBuilder();
            for (int slot = 0; slot < maxRowClueCols; slot++) {
                String val = rowClueFields[row][slot].getText().trim();
                if (!val.isEmpty()) {
                    if (rowLine.length() > 0) rowLine.append(",");
                    rowLine.append(val);
                }
            }
            if (sb.length() > 0) sb.append(" ");
            sb.append(rowLine.length() > 0 ? rowLine.toString() : "0");
        }
        return sb.toString();
    }

    public String getColClueFields() {
        StringBuilder sb = new StringBuilder();

        for (int col = 0; col < cols; col++) {
            StringBuilder colLine = new StringBuilder();
            for (int slot = 0; slot < maxColClueRows; slot++) {
                String val = colClueFields[col][slot].getText().trim();
                if (!val.isEmpty()) {
                    if (colLine.length() > 0) colLine.append(",");
                    colLine.append(val);
                }
            }
            if (sb.length() > 0) sb.append(" ");
            sb.append(colLine.length() > 0 ? colLine.toString() : "0");
        }
        return sb.toString();
    }

}