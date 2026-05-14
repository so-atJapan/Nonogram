package Nonogram.controller;


import Nonogram.model.CellState;
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.Timer;
import Nonogram.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class GameController {

    private GameModel model;
    private GameView view;
    Timer timer = new Timer();
    private Timeline timeline;

    // ドラッグ中に使用するマウスボタンを記憶
    private MouseButton dragButton = null;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view  = view;
    }

    public void initialize() {
        view.initialize(model.getPuzzle());
        view.render();

        Puzzle puzzle = model.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                int finalX = x;
                int finalY = y;

                // クリック
                view.getButtons()[finalX][finalY].setOnMouseClicked((e) -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(finalX, finalY);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        onCellRightClicked(finalX, finalY);
                    }
                });

                // ドラッグ開始：押したボタンを記憶してフルドラッグ開始
                view.getButtons()[finalX][finalY].setOnDragDetected((e) -> {
                    dragButton = e.getButton();
                    view.getButtons()[finalX][finalY].startFullDrag();
                });

                // ドラッグ中になぞったセルに対して処理
                view.getButtons()[finalX][finalY].setOnMouseDragEntered((e) -> {
                    if (dragButton == MouseButton.PRIMARY) {
                        onCellLeftClicked(finalX, finalY);
                    } else if (dragButton == MouseButton.SECONDARY) {
                        onCellRightClicked(finalX, finalY);
                    }
                });
            }
        }

        view.getCheckButton().setOnAction(e -> onJudge());

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> view.updateTimer(timer.tick())));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void onCellLeftClicked(int x, int y) {
        model.toggle(x, y, CellState.FILLED);
        view.updateCell(x, y, model.getGrid());
    }

    public void onCellRightClicked(int x, int y) {
        model.toggle(x, y, CellState.MARKED);
        view.updateCell(x, y, model.getGrid());
    }

    public void onJudge() {
        boolean result = model.check();
        view.showResult(result);
    }
}
