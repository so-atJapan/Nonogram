package Nonogram.view;

import Nonogram.controller.AppController;

import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleListModel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * パズルリスト画面のView
 * メニューバー + ヘッダ（タイトル＋並び順） + スクロールリスト
 */
public class PuzzleListView {

    private final Stage STAGE;
    private Scene scene;

    private ArrayList<Puzzle> puzzleList;

    // クリア済みのパズルIDのリスト
    private ArrayList<Integer> clearedIds;

    private Button[] selectButtons;
    private Button[] detailButtons;
    private Button[] editButtons;
    private boolean isAdmin = false;
    private int currentPlayerId = -1;
    private MenuItemBar menuItemBar;
    private ComboBox<String> sortComboBox;

    private VBox listVBox;

    public PuzzleListView(Stage stage) {
        this.STAGE = stage;
    }

    /**
     * @param puzzleList  表示するパズルリスト
     * @param clearedIds  クリア済みパズルIDのリスト（未ログインなら空リスト）
     */
    public void initialize(PuzzleListModel puzzleList, ArrayList<Integer> clearedIds, boolean isAdmin, int currentPlayerId, AppController appController) {
        this.puzzleList = puzzleList.getPuzzleList();
        this.clearedIds = clearedIds;
        this.isAdmin = isAdmin;
        this.currentPlayerId = currentPlayerId;

        menuItemBar = new MenuItemBar(appController);

        // ---- ヘッダ行（タイトル＋並び順） ----
        Label titleLabel = new Label("問題選択");
        titleLabel.setFont(new Font(20));

        sortComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "デフォルト",
            "名前順",
            "難易度：低い順",
            "難易度：高い順",
            "サイズ：小さい順",
            "サイズ：大きい順",
            "作成日時：新しい順",
            "作成日時：古い順"
        ));
        sortComboBox.setValue("デフォルト");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerBox = new HBox(10, titleLabel, spacer, sortComboBox);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(6, 8, 4, 8));

        // ---- パズルカードリスト ----
        listVBox = new VBox(6);
        listVBox.setPadding(new Insets(4, 8, 8, 8));

        buildCards(this.puzzleList);

        ScrollPane scrollPane = new ScrollPane(listVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuItemBar.getMENU_BAR(), headerBox));
        root.setCenter(scrollPane);

        scene = new Scene(root, 580, 520);
    }

    /**
     * カードをすべて再構築する（並び替え時にも呼ばれる）
     */
    private void buildCards(ArrayList<Puzzle> list) {
        int length = list.size();
        selectButtons = new Button[length];
        detailButtons = new Button[length];
        editButtons   = new Button[length];

        listVBox.getChildren().clear();

        for (int puzzleIndex = 0; puzzleIndex < length; puzzleIndex++) {
            Puzzle puzzle = list.get(puzzleIndex);
            boolean cleared = clearedIds.contains(puzzle.getPuzzleId());

            // 問題名
            Label nameLabel = new Label(puzzle.getTitle());
            nameLabel.setFont(new Font(15));
            nameLabel.setMinWidth(120);
            nameLabel.setWrapText(true);

            // 難易度 ＋ サイズ
            Label diffLabel = new Label(puzzle.getDifficulty().name());
            diffLabel.setStyle("-fx-font-size: 12px;");

            Label sizeLabel = new Label(puzzle.getGridSizeX() + "×" + puzzle.getGridSizeY());
            sizeLabel.setStyle("-fx-font-size: 12px;");

            VBox infoBox = new VBox(3, diffLabel, sizeLabel);
            infoBox.setAlignment(Pos.CENTER_LEFT);
            infoBox.setMinWidth(70);

            // 星マーク（クリア済み）
            Label starLabel = new Label(cleared ? "★" : "");
            starLabel.setFont(new Font(18));
            starLabel.setMinWidth(24);
            starLabel.setAlignment(Pos.CENTER);

            // プレイボタン
            selectButtons[puzzleIndex] = new Button("▶ プレイ");
            selectButtons[puzzleIndex].setFont(new Font(12));

            // 詳細ボタン
            detailButtons[puzzleIndex] = new Button("詳細");
            detailButtons[puzzleIndex].setFont(new Font(12));
            detailButtons[puzzleIndex].setStyle(
                "-fx-background-color: #e8e8e8; -fx-border-color: #aaaaaa; -fx-border-radius: 3px;"
            );

            // 編集ボタン（adminまたはパズルの製作者に表示）
            editButtons[puzzleIndex] = new Button("✎ 編集");
            editButtons[puzzleIndex].setFont(new Font(12));
            editButtons[puzzleIndex].setStyle(
                "-fx-background-color: #fff3cd; -fx-border-color: #aaaaaa; -fx-border-radius: 3px;"
            );
            boolean canEdit = isAdmin
                    || (currentPlayerId != -1
                        && puzzle.getCreatedBy() != null
                        && puzzle.getCreatedBy().getPlayerId() == currentPlayerId);
            editButtons[puzzleIndex].setVisible(canEdit);
            editButtons[puzzleIndex].setManaged(canEdit);

            Region hSpacer = new Region();
            HBox.setHgrow(hSpacer, Priority.ALWAYS);

            HBox cardBox = new HBox(8, nameLabel, infoBox, hSpacer, starLabel, detailButtons[puzzleIndex], editButtons[puzzleIndex], selectButtons[puzzleIndex]);
            cardBox.setAlignment(Pos.CENTER_LEFT);
            cardBox.setPadding(new Insets(8, 10, 8, 10));

            listVBox.getChildren().add(cardBox);

            if (puzzleIndex < length - 1) {
                Region separator = new Region();
                separator.setPrefHeight(1);
                separator.setStyle("-fx-background-color: #AAAAAA;");
                listVBox.getChildren().add(separator);
            }
        }
    }

    /**
     * 並び替え後にリストとボタン配列を更新する（Controllerから呼ばれる）
     */
    public void updateList(ArrayList<Puzzle> sortedList, ArrayList<Integer> clearedIds) {
        this.puzzleList = sortedList;
        this.clearedIds = clearedIds;
        buildCards(sortedList);
    }

    public void render() {
        STAGE.setTitle("Nonogram");
        STAGE.setScene(scene);
        STAGE.setResizable(false);
        STAGE.sizeToScene();
        STAGE.centerOnScreen();
        STAGE.show();
    }

    public Stage getSTAGE()                   { return STAGE; }
    public Button[] getSelectButtons()        { return selectButtons; }
    public Button[] getDetailButtons()        { return detailButtons; }
    public Button[] getEditButtons()          { return editButtons; }
    public MenuItemBar getMenuItemBar()       { return menuItemBar; }
    public ComboBox<String> getSortComboBox() { return sortComboBox; }
    public ArrayList<Integer> getClearedIds() { return clearedIds; }
}