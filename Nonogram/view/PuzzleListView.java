package Nonogram.view;

import java.util.ArrayList;

import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * パズルリスト画面のView
 * メニューバー + ヘッダ（タイトル＋並び順） + スクロールリスト
 */
public class PuzzleListView {

    private Stage stage;
    private Scene scene;

    private ArrayList<Puzzle> puzzleList;

    // クリア済みのパズルIDのリスト
    private ArrayList<Integer> clearedIds;

    private Button[] selectButtons;
    private MenuItem[] editMenuItems;
    private MenuItem[] solverMenuItems;
    private MenuItemBar menuItemBar;
    private ComboBox<String> sortComboBox;

    private VBox listVBox;

    public PuzzleListView(Stage stage) {
        this.stage = stage;
    }

    /**
     * @param puzzleList  表示するパズルリスト
     * @param clearedIds  クリア済みパズルIDのリスト（未ログインなら空リスト）
     */
    public void initialize(PuzzleList puzzleList, ArrayList<Integer> clearedIds) {
        this.puzzleList = puzzleList.getPuzzleList();
        this.clearedIds = clearedIds;

        menuItemBar = new MenuItemBar();

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
        root.setTop(new VBox(menuItemBar.getMenuBar(), headerBox));
        root.setCenter(scrollPane);

        scene = new Scene(root, 460, 520);
    }

    /**
     * カードをすべて再構築する（並び替え時にも呼ばれる）
     */
    private void buildCards(ArrayList<Puzzle> list) {
        int length = list.size();
        selectButtons = new Button[length];
        editMenuItems = new MenuItem[length];
        solverMenuItems = new MenuItem[length];

        listVBox.getChildren().clear();

        for (int i = 0; i < length; i++) {
            Puzzle p = list.get(i);
            boolean cleared = clearedIds.contains(p.getPuzzleId());

            // 問題名
            Label nameLabel = new Label(p.getTitle());
            nameLabel.setFont(new Font(15));
            nameLabel.setMinWidth(120);
            nameLabel.setWrapText(true);

            // 難易度 ＋ サイズ
            Label diffLabel = new Label(p.getDifficulty().name());
            diffLabel.setStyle("-fx-font-size: 12px;");

            Label sizeLabel = new Label(p.getGridSizeX() + "×" + p.getGridSizeY());
            sizeLabel.setStyle("-fx-font-size: 12px;");

            VBox infoBox = new VBox(3, diffLabel, sizeLabel);
            infoBox.setAlignment(Pos.CENTER_LEFT);
            infoBox.setMinWidth(70);

            // 星マーク（クリア済み）
            Label starLabel = new Label(cleared ? "★" : "");
            starLabel.setFont(new Font(18));
            starLabel.setMinWidth(24);
            starLabel.setAlignment(Pos.CENTER);

            // 選択ボタン
            selectButtons[i] = new Button("▶ プレイ");
            selectButtons[i].setFont(new Font(12));

            Region hSpacer = new Region();
            HBox.setHgrow(hSpacer, Priority.ALWAYS);

            HBox cardBox = new HBox(10, nameLabel, infoBox, hSpacer, starLabel, selectButtons[i]);
            cardBox.setAlignment(Pos.CENTER_LEFT);
            cardBox.setPadding(new Insets(8, 10, 8, 10));

            // コンテキストメニュー（右クリック）
            editMenuItems[i] = new MenuItem("編集");
            editMenuItems[i].setStyle("-fx-font-size: 14px;");
            solverMenuItems[i] = new MenuItem("ソルバー");
            solverMenuItems[i].setStyle("-fx-font-size: 14px;");
            MenuItem deleteMenuItem = new MenuItem("削除");
            deleteMenuItem.setStyle("-fx-font-size: 14px;");

            ContextMenu contextMenu = new ContextMenu(editMenuItems[i], solverMenuItems[i], deleteMenuItem);
            cardBox.setOnContextMenuRequested(e ->
                contextMenu.show(cardBox, e.getScreenX(), e.getScreenY()));

            listVBox.getChildren().add(cardBox);

            if (i < length - 1) {
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
        stage.setTitle("Nonogram");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public Button[] getSelectButtons()       { return selectButtons; }
    public MenuItem[] getEditMenuItems()     { return editMenuItems; }
    public MenuItem[] getSolverMenuItems()   { return solverMenuItems; }
    public MenuItemBar getMenuItemBar()      { return menuItemBar; }
    public ComboBox<String> getSortComboBox(){ return sortComboBox; }
}