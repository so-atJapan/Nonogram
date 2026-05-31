package Nonogram.controller;

import Nonogram.view.HomeView;

public class HomeController {

    private final HomeView HOME_VIEW;
    private final AppController APP_CONTROLLER;

    public HomeController(HomeView homeView, AppController appController) {
        this.HOME_VIEW = homeView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        if (APP_CONTROLLER.isLoggedIn()) {
            HOME_VIEW.initialize(APP_CONTROLLER.getCurrentPlayer().getUSER_NAME());
        } else {
            HOME_VIEW.initialize();
        }

        HOME_VIEW.getPlayButton().setOnAction(e -> onPlay());
        HOME_VIEW.getCreateButton().setOnAction(e -> onCreate());
        HOME_VIEW.getSolverButton().setOnAction(e -> onSolver());
        HOME_VIEW.getExitButton().setOnAction(e -> onExit());
        if (HOME_VIEW.getLoginButton() != null) {
            HOME_VIEW.getLoginButton().setOnAction(e -> onLogin());
        }
        if (HOME_VIEW.getSignupButton() != null) {
            HOME_VIEW.getSignupButton().setOnAction(e -> onSignup());
        }

        HOME_VIEW.render();
    }

    private void onPlay()   { APP_CONTROLLER.navigateTo("list"); }
    private void onCreate() { APP_CONTROLLER.navigateTo("create"); }
    private void onSolver() { APP_CONTROLLER.navigateTo("homeSolver"); }
    private void onExit()   { APP_CONTROLLER.exitGame(); }
    private void onLogin()  { APP_CONTROLLER.navigateTo("login"); }
    private void onSignup() { APP_CONTROLLER.navigateTo("signup"); }
}
