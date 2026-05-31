package Nonogram.controller;

import Nonogram.model.LoginModel;
import Nonogram.view.LoginView;

/**
 * ログイン画面の入力処理と画面遷移を管理するコントローラクラス
 */
public class LoginController {

    private final LoginModel LOGIN_MODEL;
    private final LoginView LOGIN_VIEW;
    private final AppController APP_CONTROLLER;
    private String nextDestination;

    public LoginController(LoginModel loginModel, LoginView loginView, AppController appController) {
        this.LOGIN_MODEL = loginModel;
        this.LOGIN_VIEW = loginView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        LOGIN_VIEW.initialize();
        LOGIN_VIEW.getLoginButton().setOnAction(e -> onLogin());
        LOGIN_VIEW.getSignupLink().setOnAction(e -> onSignup());
        LOGIN_VIEW.getCancelButton().setOnAction(e -> LOGIN_VIEW.close());
        LOGIN_VIEW.render();

        if (nextDestination != null) {
            APP_CONTROLLER.navigateTo(nextDestination);
        }
    }

    private void onLogin() {
        final String CHALLENGE_EMAIL    = LOGIN_VIEW.getEmail();
        final String CHALLENGE_PASSWORD = LOGIN_VIEW.getPassword();

        if (isBlank(CHALLENGE_EMAIL) || isBlank(CHALLENGE_PASSWORD)) {
            LOGIN_VIEW.showMessage("メールアドレスとパスワードを入力してください。");
            return;
        }

        boolean success = LOGIN_MODEL.login(CHALLENGE_EMAIL, CHALLENGE_PASSWORD);
        if (success) {
            APP_CONTROLLER.setCurrentPlayer(LOGIN_MODEL.getLoginPlayer());
            nextDestination = "home";
            LOGIN_VIEW.confirmLogin();
        } else {
            LOGIN_VIEW.showMessage("メールアドレスまたはパスワードが正しくありません。");
        }
    }

    private void onSignup() {
        nextDestination = "signup";
        LOGIN_VIEW.confirmLogin();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
