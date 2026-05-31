package Nonogram.controller;

import Nonogram.model.CellState;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;

import Nonogram.view.PuzzleEditorView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.application.Platform;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

public class PuzzleEditorController {

    private final PuzzleEditorModel PUZZLE_EDITOR_MODEL;
    private final PuzzleEditorView PUZZLE_EDITOR_VIEW;
    private final AppController APP_CONTROLLER;

    private int startX;
    private int startY;

    /** ドラッグ中に適用するアクション */
    private CellState dragAction = null;
    /** ドラッグ中に処理済みのセルを記録 */
    private final Set<String> DRAGGED_CELLS = new HashSet<>();

    public PuzzleEditorController(PuzzleEditorModel puzzleEitorModel, PuzzleEditorView puzzleEditorView, AppController appController) {
        this.PUZZLE_EDITOR_MODEL = puzzleEitorModel;
        this.PUZZLE_EDITOR_VIEW  = puzzleEditorView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        PUZZLE_EDITOR_VIEW.initialize(PUZZLE_EDITOR_MODEL.getPuzzle(), APP_CONTROLLER);

        bindAllCellEvents();

        PUZZLE_EDITOR_VIEW.getSettingButton().setOnAction(e -> PUZZLE_EDITOR_VIEW.semiModalRender(PUZZLE_EDITOR_MODEL.getPuzzle()));
        PUZZLE_EDITOR_VIEW.getOkButton().setOnAction(e -> onSettingConfirm());
        PUZZLE_EDITOR_VIEW.getDeleteButton().setOnAction(e -> onDelete());
        PUZZLE_EDITOR_VIEW.getCheckButton().setOnAction(e -> onCheck());

        PUZZLE_EDITOR_VIEW.render();
        PUZZLE_EDITOR_VIEW.semiModalRender(PUZZLE_EDITOR_MODEL.getPuzzle());
    }

    public CellState onCellLeftClicked(int x, int y) {
        PUZZLE_EDITOR_MODEL.toggle(x, y, CellState.FILLED);
        PUZZLE_EDITOR_VIEW.updateCell(x, y, PUZZLE_EDITOR_MODEL.getGrid());
        return PUZZLE_EDITOR_MODEL.getGrid().getCellAt(x, y).getState();
    }

    private void applyDragAction(int x, int y) {
        if (dragAction == null) return;
        PUZZLE_EDITOR_MODEL.setState(x, y, dragAction);
        PUZZLE_EDITOR_VIEW.updateCell(x, y, PUZZLE_EDITOR_MODEL.getGrid());
    }

    public void onSettingConfirm() {
        PUZZLE_EDITOR_MODEL.updatePuzzleTitle(PUZZLE_EDITOR_VIEW.getTitleTextField());
        PUZZLE_EDITOR_MODEL.updatePuzzleGridSizeX(PUZZLE_EDITOR_VIEW.getGridSizeX());
        PUZZLE_EDITOR_MODEL.updatePuzzleGridSizeY(PUZZLE_EDITOR_VIEW.getGridSizeY());
        PUZZLE_EDITOR_MODEL.gridReSize();
        PUZZLE_EDITOR_VIEW.gridReSize(PUZZLE_EDITOR_MODEL.getGrid());
        PUZZLE_EDITOR_VIEW.settingConfirm();
        bindAllCellEvents();
        PUZZLE_EDITOR_VIEW.render();
    }

    public void onUndo(){
        PUZZLE_EDITOR_MODEL.undoGridLog();
        PUZZLE_EDITOR_MODEL.setGrid(PUZZLE_EDITOR_MODEL.getCurrentLog().copy());
        PUZZLE_EDITOR_VIEW.updateCellAll(PUZZLE_EDITOR_MODEL.getGrid());
    }

    public void onRedo(){
        PUZZLE_EDITOR_MODEL.redoGridLog();
        PUZZLE_EDITOR_MODEL.setGrid(PUZZLE_EDITOR_MODEL.getCurrentLog().copy());
        PUZZLE_EDITOR_VIEW.updateCellAll(PUZZLE_EDITOR_MODEL.getGrid());
    }

    public void onReset() {
        PUZZLE_EDITOR_MODEL.reset();
        PUZZLE_EDITOR_VIEW.updateCell(0, 0, PUZZLE_EDITOR_MODEL.getGrid());
    }

    public void onDelete() {
        if (PUZZLE_EDITOR_MODEL.getPuzzle().getPuzzleId() == -1) return;

        PUZZLE_EDITOR_VIEW.settingConfirm();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("削除確認");
            alert.setHeaderText(null);
            alert.setContentText("このパズルを削除してもよいですか？");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                PUZZLE_EDITOR_MODEL.deletePuzzle();
                APP_CONTROLLER.navigateTo("home");
            }
        });
    }

    public void onCheck() {
        PUZZLE_EDITOR_MODEL.updateDB();
        APP_CONTROLLER.navigateTo("home");
    }

    private void bindAllCellEvents() {
        Puzzle puzzle = PUZZLE_EDITOR_MODEL.getPuzzle();
        for (int x = 0; x < puzzle.getGridSizeX(); x++) {
            for (int y = 0; y < puzzle.getGridSizeY(); y++) {
                final int FINAL_X = x;
                final int FINAL_Y = y;

                var button = PUZZLE_EDITOR_VIEW.getButtons()[FINAL_X][FINAL_Y];

                button.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        onCellLeftClicked(FINAL_X, FINAL_Y);
                        PUZZLE_EDITOR_MODEL.pushGridLog();
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
                    if (e.getButton() == MouseButton.PRIMARY && dragAction != null) {
                        PUZZLE_EDITOR_MODEL.pushGridLog();
                    }
                    dragAction = null;
                    DRAGGED_CELLS.clear();
                });
            }
        }
    }
}
