package Nonogram.controller;


import Nonogram.model.CellState;
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.Timer;
import Nonogram.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

public class GameController {

    private GameModel model;
    private GameView view;
    Timer timer = new Timer();
    private Timeline timeline;


    /**
     * コンストラクタ
     *
     * @param model 
     * @param view  
     */
    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view  = view;
    }

    /**
     * ゲームを起動
     * ボタン描画呼び出し、初期化
     */
    public void initialize() {
        // PuzzleのデータをViewに渡す
        view.initialize(model.getPuzzle());
        view.render();

        // 左クリック
        // 画面ボタン
        // ボタンを押したら受け取る
        Puzzle puzzle = model.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                int finalX = x;
                int finalY = y;
                view.getButtons()[finalX][finalY].setOnMouseClicked((e) -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(finalX, finalY);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        onCellRightClicked(finalX, finalY);
                    }
                });
                
            }
        }

        // リセットボタン
        // view.getResetButton().addActionListener(e -> onReset());

        // チェックボタン
        view.getCheckButton().setOnAction(e -> onCheck());


        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> view.updateTimer(timer.tick())));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
    }

    /**
     * セルが左クリックされたときの処理。
     *
     * @param x クリックされたセルのX座標
     * @param y クリックされたセルのY座標
     */
    public void onCellLeftClicked(int x, int y) {
        
        model.toggle(x, y, CellState.FILLED);
        view.updateCell(x, y, model.getGrid());
    }

    /**
     * セルが右クリックされた時の処理
     * 
     * @param x
     * @param y
     */
    public void onCellRightClicked(int x, int y) {
        
        model.toggle(x, y, CellState.MARKED);
        view.updateCell(x, y, model.getGrid());
    }

    // /**
    //  * リセットボタンが押されたときの処理。
    //  */
    // public void onReset() {
    //     model.reset();
    //     view.updateCell(0, 0, model.getCell());
    // }

    /**
     * チェックボタンが押されたときの処理。
     */
    public void onCheck() {
        boolean result = model.check();
        view.showResult(result);
    }
}
