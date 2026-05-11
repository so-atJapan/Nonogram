package Nonogram.controller;

import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.view.GameView;

public class GameController {

    private GameModel model;
    private GameView view;

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
                view.getButtons()[finalX][finalY].setOnAction(e -> onCellClicked(finalX, finalY));
            }
        }

        // リセットボタン
        // view.getResetButton().addActionListener(e -> onReset());

        // チェックボタン
        view.getCheckButton().setOnAction(e -> onCheck());
        
    }

    /**
     * セルが左クリックされたときの処理。
     *
     * @param x クリックされたセルのX座標
     * @param y クリックされたセルのY座標
     */
    public void onCellClicked(int x, int y) {
        model.toggle(x, y);
        view.updateCell(x, y, model.getCell());
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
