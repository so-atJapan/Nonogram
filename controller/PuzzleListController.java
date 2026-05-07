package Nonogram.controller;

import java.util.List;

import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.view.PuzzleListView;

/**
 * @author 太田
 * エラー対応未実装(5/6時点)package View;

import java.util.List;
import java.util.function.Consumer;

import Model.Puzzle;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class PuzzleListView extends BorderPane {

    private ListView<Puzzle> puzzleListView;

    /**
     * コンストラクタ
     */
    public PuzzleListView() {

        puzzleListView = new ListView<>();

        // ListView内の表示形式
        puzzleListView.setCellFactory(param -> newpackage Nonogram.controller;

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

        bindViewEvents();
    }

    /**
     * パズル一覧を読み込みViewに表示させる
     */
    public void loadPuzzles() {
        List<Puzzle> allPuzzles = puzzlelist.getPuzzleList();
        view.displayPuzzles(allPuzzles);
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

    /**
     * コンストラクタ時実行、ボタン接続
     */
    private void bindViewEvents() {
        view.setOnPuzzleSelected(this::onSelectPuzzle);
        // view.getCreateButton().setOnAction(e -> onCreatePuzzle());
        // view.getDifficultyFilter().setOnAction(e -> onFilterChanged(view.getDifficultyFilter().getValue()));
    }

}
 ListCell<>() {

            @Override
            protected void updateItem(Puzzle puzzle, boolean empty) {
                super.updateItem(puzzle, empty);

                if (empty || puzzle == null) {
                    setText(null);

                } else {

                    setText(
                        "タイトル : " + puzzle.getTitle()
                        + " / 難易度 : " + puzzle.getDifficulty()
                        + " / サイズ : "
                        + puzzle.getGridSizeX()
                        + "x"
                        + puzzle.getGridSizeY()
                    );
                }
            }
        });

        setCenter(puzzleListView);
    }

    /**
     * パズル一覧表示
     *
     * @param puzzles
     */
    public void displayPuzzles(List<Puzzle> puzzles) {

        puzzleListView.setItems(
            FXCollections.observableArrayList(puzzles)
        );
    }

    /**
     * パズル選択イベント設定
     *
     * @param handler
     */
    public void setOnPuzzleSelected(Consumer<Puzzle> handler) {

        puzzleListView
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {

                if (newValue != null) {
                    handler.accept(newValue);
                }
            });
    }

}




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

        bindViewEvents();
    }

    /**
     * パズル一覧を読み込みViewに表示させる
     */
    public void loadPuzzles() {
        List<Puzzle> allPuzzles = puzzlelist.getAllPuzzles();
        view.displayPuzzles(allPuzzles);
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

    /**
     * コンストラクタ時実行、ボタン接続
     */
    private void bindViewEvents() {
        view.setOnPuzzleSelected(this::onSelectPuzzle);
        // view.getCreateButton().setOnAction(e -> onCreatePuzzle());
        // view.getDifficultyFilter().setOnAction(e -> onFilterChanged(view.getDifficultyFilter().getValue()));
    }

}
