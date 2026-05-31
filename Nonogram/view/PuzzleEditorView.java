package Nonogram.view;
 
import Nonogram.controller.AppController;

import Nonogram.model.Grid;
import Nonogram.model.Puzzle;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
 
public class PuzzleEditorView {

    private PuzzleEditorDialog puzzleEditorDialog;
 
    private final Stage STAGE;
    private Scene scene;
 
    private GridPane gridPanel;
    private Button[][] buttons;
    private Button settingButton;
    private Button checkButton;
    private Label timerLabel;

    private ScrollPane scrollPane;
    private HBox midRow;

    private MenuItemBar menuItemBar;

    private int rows;
    private int cols;
 
    // ここだけ変えれば全体のサイズが変わる（ヒントもグリッドも同じ値で統一）
    private static final int CELL_SIZE = 20;
 
    // コンストラクト
    public PuzzleEditorView(Stage stage) {
        this.STAGE = stage;
    }

    //初期化
    public void initialize(Puzzle puzzle, AppController appController){

        this.puzzleEditorDialog = new PuzzleEditorDialog(this.STAGE);
        this.puzzleEditorDialog.initialize(puzzle);

        menuItemBar = new MenuItemBar(appController);

        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();
    
        // ===== グリッド =====
        gridPanel = new GridPane();
        buttons = new Button[puzzle.getGridSizeX()][puzzle.getGridSizeY()];
    
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                Button button = new Button();
                button.setPrefSize(CELL_SIZE, CELL_SIZE);
                button.setFocusTraversable(false);
                applyCellStyle(button, "empty", x, y);
                
                buttons[x][y] = button;
                gridPanel.add(button, y, x);
                
                updateCell(x, y, puzzle.getSolution());
            }
        }
    
        // ===== 下部ボタン（設定 : 登録 = 1 : 3）=====
        settingButton = new Button("設定");
        checkButton = new Button("登録");

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
        midRow = new HBox(gridPanel);
        midRow.setSpacing(0);

        // パズルエリア（スクロール対象：メニューバーと確認ボタンは除く）
        VBox puzzleArea = new VBox(midRow);
        puzzleArea.setSpacing(0);

        scrollPane = new ScrollPane(puzzleArea);
        scrollPane.setFitToWidth(false);   // コンテンツを縮小しない
        scrollPane.setFitToHeight(false);  // コンテンツを縮小しない
        scrollPane.setPannable(false);

        // スクロール領域の最大サイズ（この値を超えたらスクロールバーが出る）
        final int MAX_VIEW_WIDTH  = 1000;
        final int MAX_VIEW_HEIGHT = 800;

        // 実際のパズルサイズを計算
        int puzzleWidth  = cols * CELL_SIZE ;
        int puzzleHeight = rows * CELL_SIZE ;

        scrollPane.setPrefViewportWidth( Math.min(puzzleWidth,  MAX_VIEW_WIDTH));
        scrollPane.setPrefViewportHeight(Math.min(puzzleHeight, MAX_VIEW_HEIGHT));

        VBox root = new VBox(menuItemBar.getMENU_BAR(), scrollPane, bottomButtons);
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

    //セミモーダル描画
    public void semiModalRender(Puzzle puzzle){
        this.puzzleEditorDialog.setTitleTextField(puzzle.getTitle());
        this.puzzleEditorDialog.setRowTextField(puzzle.getGridSizeX());
        this.puzzleEditorDialog.setColTextField(puzzle.getGridSizeY());
        this.puzzleEditorDialog.render();
    }
    
    // 設定確定
    public void settingConfirm(){
        this.puzzleEditorDialog.settingConfirm();
    }

    // セル更新（row, col の順で統一）
    public void updateCell(int x, int y, Grid grid) {
        Button button = buttons[x][y];

        switch (grid.getCellAt(x, y).getState()) {
            case FILLED:
                applyCellStyle(button, "filled", x, y);
                button.setText("");
                break;
            case MARKED:
                applyCellStyle(button, "marked", x, y);
                button.setText("✕");
                break;
            default:
                applyCellStyle(button, "empty", x, y);
                button.setText("");
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
                Button cellButton = new Button();
                cellButton.setPrefSize(CELL_SIZE, CELL_SIZE);
                cellButton.setFocusTraversable(false);
                applyCellStyle(cellButton, "empty", x, y);
                
                buttons[x][y] = cellButton;
                gridPanel.add(cellButton, y, x);
                
                updateCell(x, y, grid);
            }
        }

        midRow.getChildren().setAll(gridPanel);

        // ===== ScrollPane のビューポートを再調整 =====
        final int MAX_VIEW_WIDTH  = 1000;
        final int MAX_VIEW_HEIGHT = 800;

        int puzzleWidth  = this.cols * CELL_SIZE;
        int puzzleHeight = this.rows * CELL_SIZE;

        scrollPane.setPrefViewportWidth( Math.min(puzzleWidth,  MAX_VIEW_WIDTH));
        scrollPane.setPrefViewportHeight(Math.min(puzzleHeight, MAX_VIEW_HEIGHT));

        // ウィンドウをコンテンツに合わせて再フィット
        STAGE.sizeToScene();
        STAGE.centerOnScreen();
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
 
 
    public Stage getSTAGE() { return STAGE; }
 
    public Button[][] getButtons() { return buttons; }
 
    public Button getSettingButton() { return settingButton; }

    public Button getOkButton() { return this.puzzleEditorDialog.getOkButton(); }

    public Button getDeleteButton() { return this.puzzleEditorDialog.getDeleteButton(); }
 
    public Button getCheckButton() { return checkButton; }

    public String getTitleTextField(){ return this.puzzleEditorDialog.getTitleTextField(); }

    public int getGridSizeX(){ return this.puzzleEditorDialog.getGridSizeX(); }

    public int getGridSizeY(){ return this.puzzleEditorDialog.getGridSizeY(); }

    public MenuItemBar getMenuItemBar() { return menuItemBar; }

}