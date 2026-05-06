package Nonogram.controller;

import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.view.GameView;
import Nonogram.view.PuzzleListView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author 太田
 */
public class AppController {
    private Stage stage;
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;

    /**
     * コンストラクタ
     * 
     * @param stage
     */
    public AppController(Stage stage) {
        this.stage = stage;
    }

    /**
     * 起動時の最初の画面(今回は選択画面から)
     */
    public void initialize() {
        stage.setTitle("Nonogram");
        navigateTo("list");
        stage.show();
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
            case "game":
                showGame();
                break;
            // 将来に拡張地
        }
    }

    /**
     * 渡されたパズルのセット
     * 
     * @param puzzle 開始するパズル
     */
    public void setPendingPuzzle(Puzzle puzzle) {
        this.pendingPuzzle = puzzle;
    }


    /**
     * パズル選択画面生成
     */
    public void showPuzzleList() {
        // View・Model・Controllerを生成
        PuzzleListView listView      = new PuzzleListView(this);
        PuzzleList     puzzleList    = new PuzzleList();
        puzzleListController = new PuzzleListController(listView, puzzleList, this);
 
        // 初期データを読み込む
        puzzleListController.loadPuzzles();
 
        // ステージにシーンをセット
        Scene scene = new Scene(listView.getRoot(), 800, 600);
        stage.setScene(scene);
    }


    /**
     * ゲーム画面生成
     */
    public void showGame() { 
        // MVC各層を生成
        GameModel model = new GameModel(pendingPuzzle);
        GameView  view  = new GameView(this);
        gameController = new GameController(model, view);
 
        // ゲームを初期化（ボタンイベント登録など）
        gameController.init();
 
        // ステージにシーンをセット
        Scene scene = new Scene(view.getRoot(), 800, 600);
        stage.setScene(scene);
    }

}
