package Nonogram.controller;

import Nonogram.model.CellState;
import Nonogram.model.GameModel;
import Nonogram.model.Puzzle;
import Nonogram.model.Timer;
import Nonogram.view.GameView;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

/**
 * ゲーム画面の操作と判定を管理するコントローラクラス
 */
public class GameController {

    private final GameModel GAME_MODEL;
    private final GameView GAME_VIEW;
    private final AppController APP_CONTROLLER;
    private final Timer timer = new Timer();
    private Timeline timeline;
    private int startX;
    private int startY;
    /** ドラッグ中に適用するアクション（FILLED or MARKED or EMPTY） */
    private CellState dragAction = null;
    /** ドラッグ中に処理済みのセルを記録 */
    private final Set<String> DRAGGED_CELLS = new HashSet<>();

    public GameController(GameModel gameModel, GameView gameView, AppController appController) {
        this.GAME_MODEL = gameModel;
        this.GAME_VIEW  = gameView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        GAME_VIEW.initialize(GAME_MODEL.getPuzzle(), APP_CONTROLLER);
        GAME_VIEW.render();

        bindAllCellEvents();

        GAME_VIEW.getPrevButton().setOnAction(e -> onUndo());
        GAME_VIEW.getNextButton().setOnAction(e -> onRedo());
        GAME_VIEW.getCheckButton().setOnAction(e -> onJudge());

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            GAME_VIEW.updateTimer(timer.tick());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public CellState onCellLeftClicked(int x, int y) {
        GAME_MODEL.toggle(x, y, CellState.FILLED);
        GAME_VIEW.updateCell(x, y, GAME_MODEL.getGrid());
        return GAME_MODEL.getGrid().getCellAt(x, y).getState();
    }

    public CellState onCellRightClicked(int x, int y) {
        GAME_MODEL.toggle(x, y, CellState.MARKED);
        GAME_VIEW.updateCell(x, y, GAME_MODEL.getGrid());
        return GAME_MODEL.getGrid().getCellAt(x, y).getState();
    }

    private void applyDragAction(int x, int y) {
        if (dragAction == null) return;
        GAME_MODEL.setState(x, y, dragAction);
        GAME_VIEW.updateCell(x, y, GAME_MODEL.getGrid());
    }

    public void onJudge() {
        boolean resultCollect = GAME_MODEL.check();
        if (resultCollect) {
            timeline.stop();
            APP_CONTROLLER.setResultData(GAME_MODEL.getPuzzle(), GAME_MODEL.getGrid(), timer.getTickSeconds());
            APP_CONTROLLER.navigateTo("result");
        } else {
            GAME_VIEW.showResult(false);
        }
    }

    /** タイムラインを停止する（MenuItemBar からホーム遷移時に呼ばれる） */
    public void stopTimeline() {
        timeline.stop();
    }

    public void onUndo(){
        GAME_MODEL.undoGridLog();
        GAME_MODEL.setGrid(GAME_MODEL.getCurrentLog().copy());
        GAME_VIEW.updateCellAll(GAME_MODEL.getGrid());
    }

    public void onRedo(){
        GAME_MODEL.redoGridLog();
        GAME_MODEL.setGrid(GAME_MODEL.getCurrentLog().copy());
        GAME_VIEW.updateCellAll(GAME_MODEL.getGrid());
    }

    private void bindAllCellEvents() {
        Puzzle puzzle = GAME_MODEL.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {

                final int FINAL_X = x;
                final int FINAL_Y = y;

                var button = GAME_VIEW.getButtons()[FINAL_X][FINAL_Y];

                button.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(FINAL_X, FINAL_Y);
                        GAME_MODEL.pushGridLog();
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        onCellRightClicked(FINAL_X, FINAL_Y);
                        GAME_MODEL.pushGridLog();
                    }
                });

                button.setOnMousePressed(e -> {
                    if (e.getButton() == MouseButton.BACK) {
                        onUndo();
                    } else if (e.getButton() == MouseButton.FORWARD) {
                        onRedo();
                    }
                });

                button.setOnDragDetected(e -> {
                    startX = FINAL_X;
                    startY = FINAL_Y;

                    if (e.isPrimaryButtonDown()) {
                        dragAction = onCellLeftClicked(FINAL_X, FINAL_Y);
                    } else if (e.isSecondaryButtonDown()) {
                        dragAction = onCellRightClicked(FINAL_X, FINAL_Y);
                    }

                    DRAGGED_CELLS.clear();
                    DRAGGED_CELLS.add(FINAL_X + "," + FINAL_Y);
                    button.startFullDrag();
                });

                button.setOnMouseDragEntered(e -> {
                    int distanceX = FINAL_X - startX;
                    int distanceY = FINAL_Y - startY;
                    if (distanceX != 0 && distanceY != 0) return;

                    final String KEY = FINAL_X + "," + FINAL_Y;
                    if (DRAGGED_CELLS.contains(KEY)) return;
                    DRAGGED_CELLS.add(KEY);
                    applyDragAction(FINAL_X, FINAL_Y);
                });

                button.setOnMouseReleased(e -> {
                    if ((e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY)
                            && dragAction != null) {
                        GAME_MODEL.pushGridLog();
                    }
                    dragAction = null;
                    DRAGGED_CELLS.clear();
                });
            }
        }
    }
}
