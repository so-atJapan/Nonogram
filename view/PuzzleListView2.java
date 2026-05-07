package View;

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
        puzzleListView.setCellFactory(param -> new ListCell<>() {

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