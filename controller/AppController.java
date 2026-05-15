package Nonogram.controller;

import Nonogram.model.GameModel;
import Nonogram.model.Grid;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.model.PuzzleList;
import Nonogram.model.ResultModel;
import Nonogram.view.GameView;
import Nonogram.view.PuzzleEditorView;
import Nonogram.view.PuzzleListView;
import Nonogram.view.ResultView;
import javafx.stage.Stage;

public class AppController {

    private Stage stage;
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;
    private PuzzleEditorController puzzleEditorController;
    private ResultController resultController;
    private Grid completedGrid;
    private int elapsedSeconds;

    public AppController(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        navigateTo("list");
    }

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
            case "result":
                showResult();
                break;
            default:
                showPuzzleList();
                break;
        }
    }

    public void setPendingPuzzle(Puzzle puzzle) {
        this.pendingPuzzle = puzzle;
    }

    public void setResultData(Puzzle puzzle, Grid completedGrid, int elapsedSeconds) {
        this.pendingPuzzle = puzzle;
        this.completedGrid = completedGrid;
        this.elapsedSeconds = elapsedSeconds;
    }

    public void showPuzzleList() {
        PuzzleList puzzleList = new PuzzleList();
        puzzleList.initialize();

        PuzzleListView listView = new PuzzleListView(stage);
        listView.initialize(puzzleList);

        puzzleListController = new PuzzleListController(listView, puzzleList, this);
        puzzleListController.initialize();
    }

    public void showGame() {
        GameModel model = new GameModel(pendingPuzzle);
        GameView view = new GameView(stage);

        gameController = new GameController(model, view, this);
        gameController.initialize();
    }

    public void showEditor() {
        Puzzle target = this.pendingPuzzle;
        PuzzleEditorModel model = new PuzzleEditorModel(target);
        PuzzleEditorView view = new PuzzleEditorView(stage);

        puzzleEditorController = new PuzzleEditorController(model, view);
        puzzleEditorController.initialize();
    }

    public void showResult() {
        ResultModel model = new ResultModel(pendingPuzzle, completedGrid, elapsedSeconds);
        ResultView view = new ResultView(stage);

        resultController = new ResultController(model, view, this);
        resultController.initialize();
    }
}
