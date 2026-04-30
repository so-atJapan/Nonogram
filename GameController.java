package controller;

import model.GameModel;
import view.GameView;
import view.GridPanel;

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
    public void init() {
        // 左クリック
        // 画面ボタン
        // ボタンを押したら受け取る

        // リセットボタン

        // チェックボタン

        // PuzzleのデータをViewに渡す
        view.render(model.getPuzzle());
    }

    /**
     * セルが左クリックされたときの処理。
     *
     * @param x クリックされたセルのX座標
     * @param y クリックされたセルのY座標
     */
    public void onCellClicked(int x, int y) {
        model.toggle(x, y);
        view.updateCell(x, y, model.getGrid());
    }

    /**
     * リセットボタンが押されたときの処理。
     */
    public void onReset() {
        model.reset();
        view.updateCell(0, 0, model.getGrid());
    }

    /**
     * チェックボタンが押されたときの処理。
     */
    public void onCheck() {
        boolean result = model.check();
        view.showResult(result);
    }
}
