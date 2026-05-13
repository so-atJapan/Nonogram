package Nonogram.controller;
 
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.model.PuzzleList;
import Nonogram.view.GameView;
import Nonogram.view.PuzzleEditorView;
import Nonogram.view.PuzzleListView;
import javafx.stage.Stage;
 
/**
 * @author 太田
 */
public class AppController {
 
    private Stage stage;
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;
    private PuzzleEditorController puzzleEditorController;

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
            case "game":
                showGame();
                break;
            case "editor":
                showEditor();
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
     * ViewがScene・Stage・表示まで自己完結する
     */
    public void showPuzzleList() {
        // View・Model・Controllerを生成
        PuzzleList     puzzleList = new PuzzleList();
        puzzleList.initialize();
        PuzzleListView listView   = new PuzzleListView(stage);
        listView.initialize(puzzleList);
        puzzleListController = new PuzzleListController(listView, puzzleList, this);
        puzzleListController.initialize();
    }
 
    /**
     * ゲーム画面生成
     * ViewがScene・Stage・表示まで自己完結する
     */
    public void showGame() {
        // MVC各層を生成
        GameModel model = new GameModel(pendingPuzzle);
        GameView  view  = new GameView(stage);
        gameController = new GameController(model, view);

        // ゲームを初期化（ボタンイベント登録など）
        gameController.initialize();
    }

    /**
     * 編集画面生成
     */
    public void showEditor() {
        Puzzle target = this.pendingPuzzle;
        PuzzleEditorModel model = new PuzzleEditorModel(target);
        PuzzleEditorView  view  = new PuzzleEditorView(stage);
 
        puzzleEditorController = new PuzzleEditorController(model, view);
        puzzleEditorController.initialize();
    }
 
}
