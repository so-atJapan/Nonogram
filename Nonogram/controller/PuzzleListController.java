package Nonogram.controller;

import java.util.ArrayList;

import Nonogram.model.DAO;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.view.PuzzleDetailDialog;
import Nonogram.view.PuzzleListView;

/**
 * @author 太田
 */
public class PuzzleListController {

    private PuzzleListView view;
    private PuzzleList puzzlelist;
    private AppController appController;

    // ログインプレイヤーのクリア済みパズルIDリスト
    private ArrayList<Integer> clearedIds = new ArrayList<>();

    /**
     * コンストラクタ
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

        int currentPlayerId;
        if (appController.isLoggedIn()) {
            currentPlayerId = appController.getCurrentPlayer().getPlayerId();
        } else {
            currentPlayerId = -1;
        }
        view.initialize(puzzlelist, clearedIds, appController.isAdmin(), currentPlayerId, appController);
        view.render();


        view.getSortComboBox().setOnAction(e -> {
            String selected = view.getSortComboBox().getValue();
            ArrayList<Puzzle> sorted = getSortedList(selected);
            view.updateList(sorted, clearedIds);
            rebindButtons(sorted);
        });

        rebindButtons(puzzlelist.getPuzzleList());
    }

    /**
     * プレイ・詳細ボタンを現在のリストに再バインドする
     */
    private void rebindButtons(ArrayList<Puzzle> list) {
        for (int i = 0; i < view.getSelectButtons().length; i++) {
            int index = i;
            if (index < list.size()) {
                Puzzle p = list.get(index);
                view.getSelectButtons()[index].setOnAction(e -> onSelectPuzzle(p));
                view.getDetailButtons()[index].setOnAction(e -> onShowDetail(p));
                view.getEditButtons()[index].setOnAction(e -> onEditPuzzle(p));
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
     * パズル選択（プレイ開始）
     */
    public Puzzle onSelectPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("game");
        return puzzle;
    }

    /**
     * 詳細ダイアログを表示する
     * ダイアログ内の「編集」「ソルバー」ボタンにもイベントを設定する
     */
    public void onShowDetail(Puzzle puzzle) {
        boolean cleared = clearedIds.contains(puzzle.getPuzzleId());

        PuzzleDetailDialog dialog = new PuzzleDetailDialog(view.getStage());
        dialog.initialize(puzzle, cleared);

        dialog.getSolverButton().setOnAction(e -> {
            dialog.close();
            onSolverPuzzle(puzzle);
        });

        dialog.show();
    }

    /**
     * パズル編集
     */
    public void onEditPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("editor");
    }

    /**
     * ソルバー起動
     */
    public void onSolverPuzzle(Puzzle puzzle) {
        appController.setPendingPuzzle(puzzle);
        appController.navigateTo("solver");
    }
}