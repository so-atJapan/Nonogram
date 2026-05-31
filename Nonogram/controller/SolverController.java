package Nonogram.controller;

import Nonogram.model.CellState;
import Nonogram.model.Clue;
import Nonogram.model.Puzzle;
import Nonogram.model.Solver;
import Nonogram.model.SolverModel;

import Nonogram.view.SolverView;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.input.MouseButton;

/**
 * ソルバー画面の操作を管理するコントローラクラス
 */
public class SolverController {

    private final SolverModel SOLVER_MODEL;
    private final SolverView SOLVER_VIEW;
    private final AppController appController;
    private int startX;
    private int startY;

    /** ドラッグ中に適用するアクション */
    private CellState dragAction = null;
    /** ドラッグ中に処理済みのセルを記録 */
    private final Set<String> DRAGGED_CELLS = new HashSet<>();

    public SolverController(SolverModel solverModel, SolverView solverView, AppController appController) {
        this.SOLVER_MODEL = solverModel;
        this.SOLVER_VIEW  = solverView;
        this.appController = appController;
    }

    public void initialize() {
        SOLVER_VIEW.initialize(SOLVER_MODEL.getPUZZLE(), appController);
        bindAllCellEvents();
        SOLVER_VIEW.getSettingButton().setOnAction(e -> SOLVER_VIEW.semiModalRender(SOLVER_MODEL.getPUZZLE()));
        SOLVER_VIEW.getOkButton().setOnAction(e -> onSettingConfirm());
        SOLVER_VIEW.getCheckButton().setOnAction(e -> onJudge());
        SOLVER_VIEW.render();
    }

    public CellState onCellLeftClicked(int x, int y) {
        SOLVER_MODEL.toggle(x, y, CellState.FILLED);
        SOLVER_VIEW.updateCell(x, y, SOLVER_MODEL.getGrid());
        return SOLVER_MODEL.getGrid().getCellAt(x, y).getState();
    }

    public CellState onCellRightClicked(int x, int y) {
        SOLVER_MODEL.toggle(x, y, CellState.MARKED);
        SOLVER_VIEW.updateCell(x, y, SOLVER_MODEL.getGrid());
        return SOLVER_MODEL.getGrid().getCellAt(x, y).getState();
    }

    private void applyDragAction(int x, int y) {
        if (dragAction == null) return;
        SOLVER_MODEL.setState(x, y, dragAction);
        SOLVER_VIEW.updateCell(x, y, SOLVER_MODEL.getGrid());
    }

    public void onJudge() {
        applyClue();
        solveAndMeasure();
    }

    public void onSettingConfirm() {
        SOLVER_MODEL.updatePuzzleGridSizeX(SOLVER_VIEW.getGridSizeX());
        SOLVER_MODEL.updatePuzzleGridSizeY(SOLVER_VIEW.getGridSizeY());
        SOLVER_MODEL.gridReSize();
        SOLVER_VIEW.gridReSize(SOLVER_MODEL.getGrid());
        SOLVER_VIEW.clueFieldReSize(SOLVER_MODEL.getPUZZLE(), SOLVER_VIEW.getClueSize());
        SOLVER_VIEW.settingConfirm();
        bindAllCellEvents();
        SOLVER_VIEW.render();
    }

    public void onUndo(){
        SOLVER_MODEL.undoGridLog();
        SOLVER_MODEL.setGrid(SOLVER_MODEL.getCurrentLog().copy());
        SOLVER_VIEW.updateCellAll(SOLVER_MODEL.getGrid());
    }

    public void onRedo(){
        SOLVER_MODEL.redoGridLog();
        SOLVER_MODEL.setGrid(SOLVER_MODEL.getCurrentLog().copy());
        SOLVER_VIEW.updateCellAll(SOLVER_MODEL.getGrid());
    }

    private void applyClue(){
        final String ROW_CLUE = SOLVER_VIEW.getRowClueFields();
        final String COL_CLUE = SOLVER_VIEW.getColClueFields();
        Clue clue = new Clue(ROW_CLUE, COL_CLUE);
        SOLVER_MODEL.getPUZZLE().setClue(clue);
    }

    private void solveAndMeasure(){
        final long START = System.nanoTime();

        Solver solver = new Solver(SOLVER_MODEL.getPUZZLE().getClue(), SOLVER_MODEL.getGrid());
        solver.solveAtOnce();

        final long END = System.nanoTime();
        final double TICKMILLI = (END - START) / 1_000_000.0;

        SOLVER_VIEW.updateTimer(TICKMILLI);
        SOLVER_MODEL.setGrid(solver.getGrid());
        SOLVER_VIEW.updateCellAll(solver.getGrid());
    }

    private void bindAllCellEvents() {
        Puzzle puzzle = SOLVER_MODEL.getPUZZLE();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                final int FINAL_X = x;
                final int FINAL_Y = y;

                var button = SOLVER_VIEW.getButtons()[FINAL_X][FINAL_Y];

                button.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(FINAL_X, FINAL_Y);
                        SOLVER_MODEL.pushGridLog();
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        onCellRightClicked(FINAL_X, FINAL_Y);
                        SOLVER_MODEL.pushGridLog();
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
                        SOLVER_MODEL.pushGridLog();
                    }
                    dragAction = null;
                    DRAGGED_CELLS.clear();
                });
            }
        }
    }
}
