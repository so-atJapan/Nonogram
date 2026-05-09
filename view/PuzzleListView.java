package View;

import java.util.List;
import java.util.function.Consumer;

import Model.Puzzle;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class PuzzleListView extends BorderPane {
package Nonogram.view;
 
import java.util.ArrayList;


import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 
public class PuzzleListView extends BorderPane {
 
    private ArrayList<Puzzle> puzzleList;
    private Stage stage;

    public PuzzleListView(Stage stage, PuzzleList puzzleList){
        this.stage = stage;
        this.puzzleList = puzzleList.getPuzzleList();
    }

    public void initialize(){
        Label selectLabel = new Label("問題選択");
        selectLabel.setFont(new Font(20));
        int length = puzzleList.size();
        Button[] selectButtons = new Button[length];
        ContextMenu[] contextMenus = new ContextMenu[length];
        MenuItem[][] menuItems = new MenuItem[length][3];
        
        Menu menu1 = new Menu("メニュー");
        MenuItem homeMenuItem = new MenuItem("ホーム");
        MenuItem synchroMenuItem = new MenuItem("同期");
        MenuItem helpMenuItem = new MenuItem("ヘルプ");
        MenuItem finMenuItem = new MenuItem("終了");
        menu1.getItems().addAll(homeMenuItem,synchroMenuItem,helpMenuItem,finMenuItem);
        // homeMenuItem.setOnAction((ActionEvent e) -> {
        //     this.mainScene(stage);
        // });
        // synchroMenuItem.setOnAction((ActionEvent e) -> {
        //     this.mainScene(stage);
        // });
        // helpMenuItem.setOnAction((ActionEvent e) -> {
        //     //ヘルプ画面表示
        // });
        // finMenuItem.setOnAction((ActionEvent e) -> {
        //     if(this.ShowAlert_Confim("終了確認", "アプリを終了しますか")){
        //         Platform.exit();
        //     }
        // });
        MenuBar menuBar = new MenuBar(menu1);
        menuBar.setStyle("-fx-font-size: 15px;" + "-fx-background-color: #cccccc");
        
        
        for (int i = 0; i < length; i++) {
            int index = i;
            String tatle = puzzleList.get(index).getTitle();
            selectButtons[index] = new Button(tatle);
            selectButtons[index].setPrefSize(140, 40);
            selectButtons[index].setFont(new Font(20));
            menuItems[index][0] = new MenuItem("追加");
            menuItems[index][0].setStyle("-fx-font-size: 17px;");
            menuItems[index][1] = new MenuItem("編集");
            menuItems[index][1].setStyle("-fx-font-size: 17px;");
            menuItems[index][2] = new MenuItem("削除");
            menuItems[index][2].setStyle("-fx-font-size: 17px;");
            contextMenus[index] = new ContextMenu(menuItems[index][0],menuItems[index][1],menuItems[index][2]);
            // selectButtons[index].setOnContextMenuRequested(e -> {
            //     contextMenus[index].show(selectButtons[index], e.getScreenX(), e.getScreenY());
            // });            
        }
        
        
        
        VBox temp1VBox = new VBox();
        temp1VBox.getChildren().addAll(selectButtons);
        
        ScrollPane scrollPane = new ScrollPane (temp1VBox);
        scrollPane.setPrefSize(155, 350);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        HBox hBox = new HBox(scrollPane);
        hBox.setPrefWidth(155);

        VBox selectVBox = new VBox(menuBar,selectLabel,hBox);
        Scene scene = new Scene(selectVBox, 300, 500);
        stage.setScene(scene);
        stage.show();
    }
 
    // /**
    //  * コンストラクタ
    //  */
    // public PuzzleListView() {
 
    //     puzzleListView = new ListView<>();
 
    //     // ListView内の表示形式
    //     puzzleListView.setCellFactory(param -> new ListCell<>() {
 
    //         @Override
    //         public void updateItem(Puzzle puzzle, boolean empty) {
    //             super.updateItem(puzzle, empty);
 
    //             if (empty || puzzle == null) {
    //                 setText(null);
 
    //             } else {
 
    //                 setText(
    //                     "タイトル : " + puzzle.getTitle()
    //                     + " / 難易度 : " + puzzle.getDifficulty()
    //                     + " / サイズ : "
    //                     + puzzle.getGridSizeX()
    //                     + "x"
    //                     + puzzle.getGridSizeY()
    //                 );
    //             }
    //         }
    //     });
 
    //     setCenter(puzzleListView);
    // }
 
    // /**
    //  * ルートノードを返す
    //  *
    //  * @return this（BorderPane自体がルート）
    //  */
    // public BorderPane getRoot() {
    //     return this;
    // }
 
    // /**
    //  * パズル一覧表示
    //  *
    //  * @param puzzles
    //  */
    // public void displayPuzzles(List<Puzzle> puzzles) {
 
    //     puzzleListView.setItems(
    //         FXCollections.observableArrayList(puzzles)
    //     );
    // }
 
    // /**
    //  * パズル選択イベント設定
    //  *
    //  * @param handler
    //  */
    // public void setOnPuzzleSelected(Function<Puzzle, Puzzle> handler) {
 
    //     puzzleListView
    //         .getSelectionModel()
    //         .selectedItemProperty()
    //         .addListener((observable, oldValue, newValue) -> {
 
    //             if (newValue != null) {
    //                 handler.apply(newValue);
    //             }
    //         });
    // }
 
}
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




