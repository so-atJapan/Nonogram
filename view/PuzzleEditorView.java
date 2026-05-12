package Nonogram.view;
 
import Nonogram.model.Cell;
import Nonogram.model.Grid;
import Nonogram.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
 
import java.util.ArrayList;
 
public class PuzzleEditorView {

    private SemiModal semiModal;
 
    private Stage stage;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button settingButton;
    private Button checkButton;
    private Label timerLabel;
 
    private int rows;
    private int cols;
 
    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private final int cellSize = 20;
 
    // コンストラクト
    public PuzzleEditorView(Stage stage) {
        this.stage = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle){

        this.semiModal = new SemiModal(this.stage);
        this.semiModal.initialize(puzzle);

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();
    
        ArrayList<ArrayList<Integer>> rowHints = puzzle.getClue().getRowClues();
        ArrayList<ArrayList<Integer>> colHints = puzzle.getClue().getColClues();
    
        // ヒントの最大数から左・上のヒントエリアサイズを決定
        int maxColHintRows = colHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        int maxRowHintCols = rowHints.stream().mapToInt(ArrayList::size).max().orElse(1);
    
        // すべて cellSize 単位で計算
        int hintAreaWidth  = maxRowHintCols * cellSize; // 左ヒントエリアの幅
        int hintAreaHeight = maxColHintRows * cellSize; // 上ヒントエリアの高さ
    
        // ===== 左上コーナー（タイマー）=====
        timerLabel = new Label("00:00");
        timerLabel.setPrefSize(hintAreaWidth, hintAreaHeight);
        timerLabel.setAlignment(Pos.CENTER);
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    
        // ===== 列ヒント（上）=====
        // 各列を VBox（下揃え）で表現し、横に HBox で並べる
        HBox hintPanelTop = new HBox();
        hintPanelTop.setSpacing(0);
    
        for (int col = 0; col < cols; col++) {
            VBox colBox = new VBox();
            colBox.setPrefSize(cellSize, hintAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);
    
            ArrayList<Integer> hints = colHints.get(col);
    
            // 上を空白で埋めて下揃えにする
            for (int p = 0; p < maxColHintRows - hints.size(); p++) {
                Label pad = new Label();
                pad.setPrefSize(cellSize, cellSize);
                colBox.getChildren().add(pad);
            }
            for (int num : hints) {
                Label lbl = new Label(String.valueOf(num));
                lbl.setPrefSize(cellSize, cellSize);
                lbl.setAlignment(Pos.CENTER);
                lbl.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0 0.5 0.5 0.5;" +
                    "-fx-font-size: 11px;"
                );
                colBox.getChildren().add(lbl);
            }
            hintPanelTop.getChildren().add(colBox);
        }
    
        // ===== 行ヒント（左）=====
        // 各行を HBox（右揃え）で表現し、縦に VBox で並べる
        VBox hintPanelSide = new VBox();
        hintPanelSide.setSpacing(0);
    
        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(hintAreaWidth, cellSize);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);
    
            ArrayList<Integer> hints = rowHints.get(row);
    
            // 左を空白で埋めて右揃えにする
            for (int p = 0; p < maxRowHintCols - hints.size(); p++) {
                Label pad = new Label();
                pad.setPrefSize(cellSize, cellSize);
                rowBox.getChildren().add(pad);
            }
            for (int num : hints) {
                Label lbl = new Label(String.valueOf(num));
                lbl.setPrefSize(cellSize, cellSize);
                lbl.setAlignment(Pos.CENTER);
                lbl.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0.5 0.5 0.5 0;" +
                    "-fx-font-size: 11px;"
                );
                rowBox.getChildren().add(lbl);
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
                applyCellStyle(btn, "empty");
                
                buttons[row][col] = btn;
                gridPanel.add(btn, col, row);
                
                updateCell(row, col, puzzle.getSolution());
            }
        }
    
        // ===== 下部ボタン（設定 : 確認 = 1 : 3）=====
        settingButton = new Button("設定");
        checkButton = new Button("確認");

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
        HBox midRow = new HBox(gridPanel);
        midRow.setSpacing(0);

        VBox root = new VBox(midRow, bottomButtons);
        root.setSpacing(0);
        root.setPadding(new Insets(8));

        scene = new Scene(root);
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
        this.semiModal.render();
    }
    
    // 設定確定
    public void settingConfirm(){
        this.semiModal.settingConfirm();
    }

    // セル更新（row, col の順で統一）
    public void updateCell(int row, int col, Grid grid) {
        Button btn = buttons[row][col];
 
        switch (grid.getCellAt(row, col).getState()) {
            case FILLED:
                applyCellStyle(btn, "filled");
                btn.setText("");
                break;
            case MARKED:
                applyCellStyle(btn, "marked");
                btn.setText("✕");
                break;
            default:
                applyCellStyle(btn, "empty");
                btn.setText("");
                break;
        }
    }
 
    // セルスタイル適用
    private void applyCellStyle(Button btn, String state) {
        String base =
            "-fx-border-color: #555555;" +
            "-fx-border-width: 1;" +
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
 
    // タイマー更新（Controllerから秒数で受け取り mm:ss 形式で表示）
    public void updateTimer(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
 
 
    public Stage getStage() {
        return stage;
    }
 
    public Button[][] getButtons() {
        return buttons;
    }
 
    public Button getSettingButton() {
        return settingButton;
    }

    public Button getOkButton() {
        return this.semiModal.getOkButton();
    }
 
    public Button getCheckButton() {
        return checkButton;
    }

    public String getTitleTextField(){
        return this.semiModal.getTitleTextField();
    }

    public String getGridSizeX(){
        return this.semiModal.getGridSizeX();
    }

    public String getGridSizeY(){
        return this.semiModal.getGridSizeY();
    }
}
 