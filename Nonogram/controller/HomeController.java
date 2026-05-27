package Nonogram.controller;

import Nonogram.view.HomeView;


public class HomeController {
    
    private HomeView view;
    private AppController appController;

    /**
     * コンストラクタ
     * 
     * @param view
     * @param puzzlelist
     * @param appController
     */
    public HomeController(HomeView view, AppController appController) {
        this.view = view;
        this.appController = appController;        
    }

    public void initialize() {

        if (appController.isLoggedIn()) {
            view.initialize(appController.getCurrentPlayer().getUserName());
        } else {
            view.initialize();
        }

        view.getPlayButton().setOnAction(e -> onPlay());
        view.getCreateButton().setOnAction(e -> onCreate());
        view.getSolverButton().setOnAction(e -> onSolver());
        view.getExitButton().setOnAction(e -> onExit());
        if (view.getLoginButton() != null) {
            view.getLoginButton().setOnAction(e -> onLogin());
        }
        if (view.getSignupButton() != null) {
            view.getSignupButton().setOnAction(e -> onSignup());
        }

        view.render();

    }
    
    private void onPlay() {
        appController.navigateTo("list");
    }
    
    private void onCreate() {
        appController.navigateTo("create");
    }

    private void onSolver() {
        appController.navigateTo("homeSolver");
    }
    
    private void onExit() {
        appController.exitGame();
    }
    
    private void onLogin() {
        appController.navigateTo("login");
    }
    
    private void onSignup() {
        appController.navigateTo("signup");
    }



}