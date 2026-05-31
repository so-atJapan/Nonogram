package Nonogram.view;

import Nonogram.controller.AppController;
import Nonogram.controller.GameController;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * 全画面共通のメニューバーを生成・管理するクラス
 * ホーム遷移と終了確認ダイアログをここで一元的に処理する
 */
public class MenuItemBar {

    private final MenuBar MENU_BAR;

    private MenuItem homeMenuItem;
    private MenuItem helpMenuItem;
    private MenuItem finMenuItem;

    /**
     * コンストラクタ
     * ホームボタンと終了ボタンのアクションをここで登録する
     *
     * @param appController 画面遷移・アプリ終了を管理するコントローラ
     */
    public MenuItemBar(AppController appController) {
        homeMenuItem    = new MenuItem("ホーム");
        helpMenuItem    = new MenuItem("ヘルプ");
        finMenuItem     = new MenuItem("終了");

        // ホームボタン：ゲーム画面からの遷移の場合は timeline を止めてから遷移する
        homeMenuItem.setOnAction(e -> {
            GameController gameController = appController.getGameController();
            if (gameController != null) {
                gameController.stopTimeline();
            }
            appController.navigateTo("home");
        });

        // 終了ボタン：確認ダイアログを出してからアプリを終了する
        finMenuItem.setOnAction(e -> onExit(appController));

        Menu menu = new Menu("メニュー");
        menu.getItems().addAll(homeMenuItem, helpMenuItem, finMenuItem);

        MENU_BAR = new MenuBar(menu);
        MENU_BAR.setStyle("-fx-font-size: 15px;" + "-fx-background-color: #cccccc");
    }

    /**
     * 終了確認ダイアログを表示し、OK が押された場合にアプリを終了する
     *
     * @param appController アプリ終了を呼び出すコントローラ
     */
    private void onExit(AppController appController) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("終了確認");
        alert.setHeaderText(null);
        alert.setContentText("アプリを終了しますがよろしいですか？");

        // OKが押されたときだけ終了する
        alert.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                appController.exitGame();
            }
        });
    }

    public MenuBar getMENU_BAR()          { return MENU_BAR; }

    public MenuItem getHomeMenuItem()    { return homeMenuItem; }
    public MenuItem getHelpMenuItem()    { return helpMenuItem; }
    public MenuItem getFinMenuItem()     { return finMenuItem; }
}