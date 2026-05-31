package Nonogram.controller;

import Nonogram.model.DAO;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleListModel;

import Nonogram.view.PuzzleDetailDialog;
import Nonogram.view.PuzzleListView;

import java.util.ArrayList;

/**
 * @author 太田
 */
public class PuzzleListController {

    private final PuzzleListView PUZZLE_LIST_VIEW;
    private final PuzzleListModel PUZZLE_LIST_MODEL;
    private final AppController APP_CONTROLLER;

    /** ログインプレイヤーのクリア済みパズルIDリスト */
    private ArrayList<Integer> clearedIds = new ArrayList<>();

    public PuzzleListController(PuzzleListView puzzleListView, PuzzleListModel puzzleListModel, AppController appController) {
        this.PUZZLE_LIST_VIEW = puzzleListView;
        this.PUZZLE_LIST_MODEL = puzzleListModel;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        if (APP_CONTROLLER.isLoggedIn()) {
            DAO dao = new DAO();
            clearedIds = dao.getPuzzleRecords(APP_CONTROLLER.getCurrentPlayer());
        } else {
            clearedIds = new ArrayList<>();
        }

        final int CURRENT_PLAYER_ID = APP_CONTROLLER.isLoggedIn()
                ? APP_CONTROLLER.getCurrentPlayer().getPlayerId()
                : -1;

        PUZZLE_LIST_VIEW.initialize(PUZZLE_LIST_MODEL, clearedIds, APP_CONTROLLER.isAdmin(), CURRENT_PLAYER_ID, APP_CONTROLLER);
        PUZZLE_LIST_VIEW.render();

        PUZZLE_LIST_VIEW.getSortComboBox().setOnAction(e -> {
            String selected = PUZZLE_LIST_VIEW.getSortComboBox().getValue();
            ArrayList<Puzzle> sorted = getSortedList(selected);
            PUZZLE_LIST_VIEW.updateList(sorted, clearedIds);
            rebindButtons(sorted);
        });

        rebindButtons(PUZZLE_LIST_MODEL.getPuzzleList());
    }

    private void rebindButtons(ArrayList<Puzzle> list) {
        for (int i = 0; i < PUZZLE_LIST_VIEW.getSelectButtons().length; i++) {
            final int INDEX = i;
            if (INDEX < list.size()) {
                final Puzzle PUZZLE = list.get(INDEX);
                PUZZLE_LIST_VIEW.getSelectButtons()[INDEX].setOnAction(e -> onSelectPuzzle(PUZZLE));
                PUZZLE_LIST_VIEW.getDetailButtons()[INDEX].setOnAction(e -> onShowDetail(PUZZLE));
                PUZZLE_LIST_VIEW.getEditButtons()[INDEX].setOnAction(e -> onEditPuzzle(PUZZLE));
            }
        }
    }

    private ArrayList<Puzzle> getSortedList(String sortOrder) {
        ArrayList<Puzzle> currentPuzzleList = new ArrayList<>(PUZZLE_LIST_MODEL.getPuzzleList());
        if (sortOrder == null) return currentPuzzleList;
        switch (sortOrder) {
            case "名前順":
                currentPuzzleList.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
                break;
            case "難易度：低い順":
                currentPuzzleList.sort((a, b) -> a.getDifficulty().ordinal() - b.getDifficulty().ordinal());
                break;
            case "難易度：高い順":
                currentPuzzleList.sort((a, b) -> b.getDifficulty().ordinal() - a.getDifficulty().ordinal());
                break;
            case "サイズ：小さい順":
                currentPuzzleList.sort((a, b) -> (a.getGridSizeX() * a.getGridSizeY()) - (b.getGridSizeX() * b.getGridSizeY()));
                break;
            case "サイズ：大きい順":
                currentPuzzleList.sort((a, b) -> (b.getGridSizeX() * b.getGridSizeY()) - (a.getGridSizeX() * a.getGridSizeY()));
                break;
            case "作成日時：新しい順":
                currentPuzzleList.sort((a, b) -> {
                    if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                });
                break;
            case "作成日時：古い順":
                currentPuzzleList.sort((a, b) -> {
                    if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
                    return a.getCreatedAt().compareTo(b.getCreatedAt());
                });
                break;
            default:
                break;
        }
        return currentPuzzleList;
    }

    public Puzzle onSelectPuzzle(Puzzle puzzle) {
        APP_CONTROLLER.setPendingPuzzle(puzzle);
        APP_CONTROLLER.navigateTo("game");
        return puzzle;
    }

    public void onShowDetail(Puzzle puzzle) {
        final boolean CLEARED = clearedIds.contains(puzzle.getPuzzleId());
        PuzzleDetailDialog dialog = new PuzzleDetailDialog(PUZZLE_LIST_VIEW.getSTAGE());
        dialog.initialize(puzzle, CLEARED);
        dialog.getSolverButton().setOnAction(e -> {
            dialog.close();
            onSolverPuzzle(puzzle);
        });
        dialog.show();
    }

    public void onEditPuzzle(Puzzle puzzle) {
        APP_CONTROLLER.setPendingPuzzle(puzzle);
        APP_CONTROLLER.navigateTo("editor");
    }

    public void onSolverPuzzle(Puzzle puzzle) {
        APP_CONTROLLER.setPendingPuzzle(puzzle);
        APP_CONTROLLER.navigateTo("solver");
    }
}
