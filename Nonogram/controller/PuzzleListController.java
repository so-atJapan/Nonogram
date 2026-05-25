package Nonogram.controller;

import java.util.ArrayList;

import Nonogram.model.DAO;
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

    // ログインプレイヤーのクリア済みパズルIDリスト
    private ArrayList<Integer> clearedIds = new ArrayList<>();

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
    }

    /**
     * 問題リストを初期化、イベント登録
     * ログイン済みの場合、クリア済みパズルIDをDBから取得
     */
    public void initialize() {

        if (appController.isLoggedIn()) {
            DAO dao = new DAO();
            clearedIds = dao.getPuzzleRecords(appController.getCurrentPlayer());
        } else {
            clearedIds = new ArrayList<>();
        }
 
        view.initialize(puzzlelist, clearedIds);
        view.render();

        view.getMenuItemBar().getHomeMenuItem().setOnAction(e -> onBackHome());

        view.getSortComboBox().setOnAction(e -> {
            String selected = view.getSortComboBox().getValue();
            ArrayList<Puzzle> sorted = getSortedList(selected);
            view.updateList(sorted, clearedIds);
            rebindButtons(sorted);
        });

        rebindButtons(puzzlelist.getPuzzleList());
        
        // view.getDifficultyFilter().setOnAction(e -> onFilterChanged(view.getDifficultyFilter().getValue()));
    }

    /**
     * 選択・編集・ソルバーボタンを現在のリストに再バインドする
     */
    private void rebindButtons(ArrayList<Puzzle> list) {
        for (int i = 0; i < view.getSelectButtons().length; i++) {
            int index = i;
            if (index < list.size()) {
                Puzzle p = list.get(index);
                view.getSelectButtons()[index].setOnAction(e -> onSelectPuzzle(p));
                view.getEditMenuItems()[index].setOnAction(e -> onEditPuzzle(p));
                view.getSolverMenuItems()[index].setOnAction(e -> onSolverPuzzle(p));
            }
        }
    }

    /**
     * 並び順に応じてソートされたリストを返す
     */
    private ArrayList<Puzzle> getSortedList(String order) {
        ArrayList<Puzzle> list = new ArrayList<>(puzzlelist.getPuzzleList());
        if (order == null) return list;
        switch (order) {
            case "名前順":
                list.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
                break;
            case "難易度：低い順":
                list.sort((a, b) -> a.getDifficulty().ordinal() - b.getDifficulty().ordinal());
                break;
            case "難易度：高い順":
                list.sort((a, b) -> b.getDifficulty().ordinal() - a.getDifficulty().ordinal());
                break;
            case "サイズ：小さい順":
                list.sort((a, b) -> (a.getGridSizeX() * a.getGridSizeY()) - (b.getGridSizeX() * b.getGridSizeY()));
                break;
            case "サイズ：大きい順":
                list.sort((a, b) -> (b.getGridSizeX() * b.getGridSizeY()) - (a.getGridSizeX() * a.getGridSizeY()));
                break;
            case "作成日時：新しい順":
                list.sort((a, b) -> {
                    if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                });
                break;
            case "作成日時：古い順":
                list.sort((a, b) -> {
                    if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
                    return a.getCreatedAt().compareTo(b.getCreatedAt());
                });
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * パズル選択
     * 
     * @param puzzle 選択されたパズル
     * @return
     */
    public Puzzle onSelectPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("game");
        return puzzle;
    }

    /**
     * パズル編集
     * 
     * @param puzzle
     */
    public void onEditPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("editor");
    }

    /**
     * ソルバー
     * 
     * @param puzzle
     */
    public void onSolverPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("solver");
    }

    private void onBackHome() {
        appController.navigateTo("home");
    }


}
