package Nonogram.controller;

import java.util.List;

import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.view.PuzzleListView;

/**
 * @author 太田
 * エラー対応未実装(5/6時点)
 */
public class PuzzleListController {
    
    private PuzzleListView view;
    private PuzzleList puzzlelist;
    private AppController appController;

    /**
     * コンストラクタ
     * 
     * @param view
     * @param puzzlelist
     * @param appController
     */
    public PuzzleListController(PuzzleListView view, PuzzleList puzzlelist, AppController appController) {
        this.view = view;
        this.puzzlelist = puzzlelist;
        this.appController = appController;

        // for (int i = 0; i < view.getSelectButtons().length; i++) {
        //     int index = i;
        //         view.getSelectButtons()[index].setOnAction(e -> onSelectPuzzle(puzzlelist.getPuzzleList().get(index)));
        // }
        // view.getCreateButton().setOnAction(e -> onCreatePuzzle());
        // view.getDifficultyFilter().setOnAction(e -> onFilterChanged(view.getDifficultyFilter().getValue()));
    }

    /**
     * パズル一覧を読み込みViewに表示させる
     */
    public void loadPuzzles() {
        view.initialize(puzzlelist);
        view.render();
    }

    /**
     * パズル選択
     * 
     * @param puzzle 選択されたパズル
     * @return
     */
    // public Puzzle onSelectPuzzle(Puzzle puzzle) {
    //     appController.setPendingPuzzle(puzzle);
    //     appController.navigateTo("game");
    //     return puzzle;
    // }

    // /**
    //  * パズル作成(拡張)
    //  */
    // public void onCreatedPuzzle() {
    //     view.showCreateButton();
    //     appController.navigateTo("editor");
    // }

    // /**
    //  * 難易度
    //  * 
    //  * @param d
    //  */
    // public void onFilterChanged(Difficulty d) {
    //     if (d == null) {
    //         // フィルターなし → 全件再表示
    //         loadPuzzles();
    //         return;
    //     }
    //     List<Puzzle> filtered = puzzleList.filter(d);
    //     view.updateFilter(d);
    //     view.displayPuzzles(filtered);
    // }


}
