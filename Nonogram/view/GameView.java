package Nonogram.view;
 
import Nonogram.controller.AppController;

import Nonogram.model.Grid;
import Nonogram.model.Puzzle;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
 
public class GameView {
 
    private final Stage STAGE;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button prevButton;
    private Button nextButton;
    private Button checkButton;
    private Label timerLabel;
    private MenuItemBar menuItemBar;
 
    private int rows;
    private int cols;
 
    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private static final int CELL_SIZE = 20;
 
    // コンストラクト
    public GameView(Stage stage) {
        this.STAGE = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle, AppController appController){

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();

        menuItemBar = new MenuItemBar(appController);
    
        ArrayList<ArrayList<Integer>> rowHints = puzzle.getClue().getROW_CLUES();
        ArrayList<ArrayList<Integer>> colHints = puzzle.getClue().getCOL_CLUES();
    
        // ヒントの最大数から左・上のヒントエリアサイズを決定
        int maxColHintRows = colHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        int maxRowHintCols = rowHints.stream().mapToInt(ArrayList::size).max().orElse(1);
    
        // すべて cellSize 単位で計算
        int hintAreaWidth  = maxRowHintCols * CELL_SIZE; // 左ヒントエリアの幅
        int hintAreaHeight = maxColHintRows * CELL_SIZE; // 上ヒントエリアの高さ
    
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
            colBox.setPrefSize(CELL_SIZE, hintAreaHeight);
            colBox.setAlignment(Pos.BOTTOM_CENTER);
            colBox.setSpacing(0);
    
            ArrayList<Integer> hints = colHints.get(col);
    
            // 上を空白で埋めて下揃えにする
            for (int paddingIndex = 0; paddingIndex < maxColHintRows - hints.size(); paddingIndex++) {
                Label emptyHintLabel = new Label();
                emptyHintLabel.setPrefSize(CELL_SIZE, CELL_SIZE);
                colBox.getChildren().add(emptyHintLabel);
            }
            for (int num : hints) {
                Label label = new Label(String.valueOf(num));
                label.setPrefSize(CELL_SIZE, CELL_SIZE);
                label.setAlignment(Pos.CENTER);
                label.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0 0.5 0.5 0.5;" +
                    "-fx-font-size: 11px;"
                );
                colBox.getChildren().add(label);
            }
            hintPanelTop.getChildren().add(colBox);
        }
    
        // ===== 行ヒント（左）=====
        // 各行を HBox（右揃え）で表現し、縦に VBox で並べる
        VBox hintPanelSide = new VBox();
        hintPanelSide.setSpacing(0);
    
        for (int row = 0; row < rows; row++) {
            HBox rowBox = new HBox();
            rowBox.setPrefSize(hintAreaWidth, CELL_SIZE);
            rowBox.setAlignment(Pos.CENTER_RIGHT);
            rowBox.setSpacing(0);
    
            ArrayList<Integer> hints = rowHints.get(row);
    
            // 左を空白で埋めて右揃えにする
            for (int paddingIndex = 0; paddingIndex < maxRowHintCols - hints.size(); paddingIndex++) {
                Label emptyHintLabel = new Label();
                emptyHintLabel.setPrefSize(CELL_SIZE, CELL_SIZE);
                rowBox.getChildren().add(emptyHintLabel);
            }
            for (int num : hints) {
                Label label = new Label(String.valueOf(num));
                label.setPrefSize(CELL_SIZE, CELL_SIZE);
                label.setAlignment(Pos.CENTER);
                label.setStyle(
                    "-fx-border-color: #cccccc;" +
                    "-fx-border-width: 0.5 0.5 0.5 0;" +
                    "-fx-font-size: 11px;"
                );
                rowBox.getChildren().add(label);
            }
            hintPanelSide.getChildren().add(rowBox);
        }
    
        // ===== グリッド =====
        gridPanel = new GridPane();
        buttons = new Button[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button cellButton = new Button();
                cellButton.setPrefSize(CELL_SIZE, CELL_SIZE);
                cellButton.setFocusTraversable(false);
                applyCellStyle(cellButton, "empty", row, col);  // ← row, col を追加

                buttons[row][col] = cellButton;
                gridPanel.add(cellButton, col, row);
            }
        }
    
        // ===== チェックボタン（下）=====
        prevButton = new Button("<");
        prevButton.setPrefHeight(36);
        prevButton.setPrefWidth(48);

        nextButton = new Button(">");
        nextButton.setPrefHeight(36);
        nextButton.setPrefWidth(48);

        checkButton = new Button("正解確認");
        checkButton.setMaxWidth(Double.MAX_VALUE);  // 残りの幅を全部占有
        checkButton.setPrefHeight(36);

        HBox bottomRow = new HBox(prevButton, nextButton, checkButton);
        HBox.setHgrow(checkButton, javafx.scene.layout.Priority.ALWAYS);  // checkButtonを伸ばす
        bottomRow.setSpacing(0);
    
        // ===== 全体レイアウト =====
        //
        //  [ cornerLabel   | hintPanelTop  ]
        //  [ hintPanelSide | gridPanel     ]
        //  [   checkButton（全幅）         ]
        //
        HBox topRow = new HBox(timerLabel, hintPanelTop);
        topRow.setSpacing(0);
    
        HBox midRow = new HBox(hintPanelSide, gridPanel);
        midRow.setSpacing(0);


        // パズルエリア（スクロール対象：メニューバーと確認ボタンは除く）
        VBox puzzleArea = new VBox(topRow, midRow);
        puzzleArea.setSpacing(0);

        ScrollPane scrollPane = new ScrollPane(puzzleArea);
        scrollPane.setFitToWidth(false);   // コンテンツを縮小しない
        scrollPane.setFitToHeight(false);  // コンテンツを縮小しない
        scrollPane.setPannable(false);

        // スクロール領域の最大サイズ（この値を超えたらスクロールバーが出る）
        final int MAX_VIEW_WIDTH  = 1000;
        final int MAX_VIEW_HEIGHT = 800;

        // 実際のパズルサイズを計算
        int maxRowHintColsForSize = rowHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        int maxColHintRowsForSize = colHints.stream().mapToInt(ArrayList::size).max().orElse(1);
        int puzzleWidth  = (maxRowHintColsForSize + cols) * CELL_SIZE ;
        int puzzleHeight = (maxColHintRowsForSize + rows) * CELL_SIZE ;

        scrollPane.setPrefViewportWidth( Math.min(puzzleWidth,  MAX_VIEW_WIDTH));
        scrollPane.setPrefViewportHeight(Math.min(puzzleHeight, MAX_VIEW_HEIGHT));

        VBox root = new VBox(menuItemBar.getMENU_BAR(), scrollPane, bottomRow);
        root.setSpacing(0);
        root.setPadding(new Insets(8));

        scene = new Scene(root);
    }
 
    // パズル描画
    public void render() {
        STAGE.setTitle("Nonogram");
        STAGE.setScene(scene);
        STAGE.setResizable(false);
        STAGE.sizeToScene();
        STAGE.centerOnScreen();
        STAGE.show();
    }
 
    // セル更新（row, col の順で統一）
    public void updateCell(int x, int y, Grid grid) {
        Button cellButton = buttons[x][y];

        switch (grid.getCellAt(x, y).getState()) {
            case FILLED:
                applyCellStyle(cellButton, "filled", x, y);  // ← 追加
                cellButton.setText("");
                break;
            case MARKED:
                applyCellStyle(cellButton, "marked", x, y);  // ← 追加
                cellButton.setText("✕");
                break;
            default:
                applyCellStyle(cellButton, "empty", x, y);   // ← 追加
                cellButton.setText("");
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
    private void applyCellStyle(Button styleButton, String state, int row, int col) {
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
                styleButton.setStyle(base + "-fx-background-color: #222222; -fx-text-fill: #222222;");
                break;
            case "marked":
                styleButton.setStyle(base + "-fx-background-color: #f0f0f0; -fx-text-fill: #cc0000;");
                break;
            default:
                styleButton.setStyle(base + "-fx-background-color: white; -fx-text-fill: black;");
                break;
        }
    }
 
    // タイマー更新（Controllerから秒数で受け取り mm:ss 形式で表示）
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
 
    public Stage getSTAGE() { return STAGE; }

    public Button[][] getButtons() { return buttons; }
 
    public Button getCheckButton() { return checkButton; }

    public Button getPrevButton() { return prevButton; }

    public Button getNextButton() { return nextButton; }

    public MenuItemBar getMenuItemBar() { return menuItemBar; }
}