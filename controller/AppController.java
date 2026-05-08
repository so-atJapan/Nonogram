package Nonogram.controller;
 
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.view.GameView;
import Nonogram.view.PuzzleListView;
 
/**
 * @author 太田
 */
public class AppController {
 
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;
 
    /**
     * 起動時の最初の画面(今回は選択画面から)
     */
    public void initialize() {
        navigateTo("list");
    }
 
    /**
     * switchで指定された画面へ遷移する
     *
     * @param destination 遷移先選択識別
     */
    public void navigateTo(String destination) {
        switch (destination) {
            case "list":
                showPuzzleList();
                break;
            // case "game":
            //     showGame();
            //     break;
            // 将来に拡張地
        }
    }
 
    // /**
    //  * 渡されたパズルのセット
    //  *
    //  * @param puzzle 開始するパズル
    //  */
    // public void setPendingPuzzle(Puzzle puzzle) {
    //     this.pendingPuzzle = puzzle;
    // }
 
    /**
     * パズル選択画面生成
     * ViewがScene・Stage・表示まで自己完結する
     */
    public void showPuzzleList() {
        // View・Model・Controllerを生成
        PuzzleListView listView   = new PuzzleListView();
        PuzzleList     puzzleList = new PuzzleList();
        puzzleListController = new PuzzleListController(listView, puzzleList, this);
 
        // 初期データを読み込む（View.displayPuzzles()で描画）
        puzzleListController.loadPuzzles();
    }
 
    // /**
    //  * ゲーム画面生成
    //  * ViewがScene・Stage・表示まで自己完結する
    //  */
    // public void showGame() {
    //     // MVC各層を生成
    //     GameModel model = new GameModel(pendingPuzzle);
    //     GameView  view  = new GameView();
    //     gameController = new GameController(model, view);
    //
    //     // ゲームを初期化（ボタンイベント登録など）
    //     gameController.init();
    // }
 
}
