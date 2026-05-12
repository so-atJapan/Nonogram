package Nonogram.controller;

import Nonogram.model.CellState;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.view.PuzzleEditorView;

public class PuzzleEditorController {

    private PuzzleEditorModel model;
    private PuzzleEditorView view;

    /**
     * コンストラクタ
     *
     * @param model 
     * @param view  
     */
    public PuzzleEditorController(PuzzleEditorModel model, PuzzleEditorView view) {
        this.model = model;
        this.view  = view;
    }

    /**
     * ゲームを起動
     * ボタン描画呼び出し、初期化
     */
    public void init() {
        // PuzzleのデータをViewに渡す
        view.initialize(model.getPuzzle());
        
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
        // view.getResetButton().setOnAction(e -> onReset());
        
        // 設定ボタン
        view.getSettingButton().setOnAction(e -> view.semiModalRender(model.getPuzzle()));
        
        //　OKボタン
        view.getOkButton().setOnAction(e -> onSettingConfirm());
        
        // チェックボタン
        view.getCheckButton().setOnAction(e -> onCheck());
        
        // 描画
        view.render();
        view.semiModalRender(model.getPuzzle());
    }

    /**
     * セルが左クリックされたときの処理。
     *
     * @param x クリックされたセルのX座標
     * @param y クリックされたセルのY座標
     */
    public void onCellClicked(int x, int y) {
        model.toggle(x, y, CellState.FILLED);
        view.updateCell(x, y, model.getGrid());
    }

    /**
     * 設定の決定ボタンが押されたときの処理。
     */
    public void onSettingConfirm() {
        model.updatePuzzleTitle(view.getTitleTextField());
        model.updatePuzzleGridSizeX(view.getGridSizeX());
        model.updatePuzzleGridSizeY(view.getGridSizeY());
        model.gridReSize();
        view.gridReSize(model.getGrid());
        view.settingConfirm();

        Puzzle puzzle = model.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                int finalX = x;
                int finalY = y;
                view.getButtons()[finalX][finalY].setOnAction(e -> onCellClicked(finalX, finalY));
            }
        }

        view.render();
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
        System.out.println("BD処理");
    }
}
