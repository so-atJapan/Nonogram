package Nonogram.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuItemBar {
    
    private MenuBar menuBar;
    
    private MenuItem homeMenuItem;
    private MenuItem synchroMenuItem;
    private MenuItem helpMenuItem;
    private MenuItem finMenuItem;

    public MenuItemBar() {
        homeMenuItem    = new MenuItem("ホーム");
        synchroMenuItem = new MenuItem("同期");
        helpMenuItem    = new MenuItem("ヘルプ");
        finMenuItem     = new MenuItem("終了");

        Menu menu = new Menu("メニュー");
        menu.getItems().addAll(homeMenuItem, synchroMenuItem, helpMenuItem, finMenuItem);
 
        menuBar = new MenuBar(menu);
        menuBar.setStyle("-fx-font-size: 15px;" + "-fx-background-color: #cccccc");
    }

    public MenuBar getMenuBar()         { return menuBar; }
 
    public MenuItem getHomeMenuItem()    { return homeMenuItem; }
    public MenuItem getSynchroMenuItem() { return synchroMenuItem; }
    public MenuItem getHelpMenuItem()    { return helpMenuItem; }
    public MenuItem getFinMenuItem()     { return finMenuItem; }
}
