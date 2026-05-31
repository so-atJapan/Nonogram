package Nonogram.controller;

import Nonogram.model.SignupModel;
import Nonogram.view.SignupView;

/**
 * サインアップ画面の入力処理と画面遷移を管理するコントローラクラス
 */
public class SignupController {

    private final SignupModel SIGNUP_MODEL;
    private final SignupView SIGNUP_VIEW;
    private final AppController APP_CONTROLLER;
    private String nextDestination;

    public SignupController(SignupModel signupModel, SignupView signupView, AppController appController) {
        this.SIGNUP_MODEL = signupModel;
        this.SIGNUP_VIEW = signupView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        SIGNUP_VIEW.initialize();
        SIGNUP_VIEW.getSignupButton().setOnAction(e -> onSignup());
        SIGNUP_VIEW.getLoginLink().setOnAction(e -> onLogin());
        SIGNUP_VIEW.getCancelButton().setOnAction(e -> SIGNUP_VIEW.close());
        SIGNUP_VIEW.render();

        if (nextDestination != null) {
            APP_CONTROLLER.navigateTo(nextDestination);
        }
    }

    private void onSignup() {
        if (isBlank(SIGNUP_VIEW.getUserName()) || isBlank(SIGNUP_VIEW.getEmail())
                || isBlank(SIGNUP_VIEW.getPassword()) || isBlank(SIGNUP_VIEW.getConfirmPassword())) {
            SIGNUP_VIEW.showMessage("すべての項目を入力してください。");
            return;
        }
        if (!SIGNUP_VIEW.getEmail().contains("@")) {
            SIGNUP_VIEW.showMessage("メールアドレスの形式を確認してください。");
            return;
        }
        if (SIGNUP_VIEW.getPassword().length() < 4) {
            SIGNUP_VIEW.showMessage("パスワードは4文字以上で入力してください。");
            return;
        }
        if (!SIGNUP_VIEW.getPassword().equals(SIGNUP_VIEW.getConfirmPassword())) {
            SIGNUP_VIEW.showMessage("確認用パスワードが一致しません。");
            return;
        }
        if (SIGNUP_MODEL.existsPlayerByEmail(SIGNUP_VIEW.getEmail())) {
            SIGNUP_VIEW.showMessage("このメールアドレスはすでに登録されています。");
            return;
        }

        boolean success = SIGNUP_MODEL.signup(
                SIGNUP_VIEW.getUserName(),
                SIGNUP_VIEW.getEmail(),
                SIGNUP_VIEW.getPassword(),
                SIGNUP_VIEW.getConfirmPassword()
        );

        if (success) {
            APP_CONTROLLER.setCurrentPlayer(SIGNUP_MODEL.getCreatedPlayer());
            nextDestination = "home";
            SIGNUP_VIEW.confirmSignup();
        } else {
            SIGNUP_VIEW.showMessage("アカウントを作成できませんでした。");
        }
    }

    private void onLogin() {
        nextDestination = "login";
        SIGNUP_VIEW.confirmSignup();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
