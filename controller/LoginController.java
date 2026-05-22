package Nonogram.controller;

import Nonogram.model.LoginModel;
import Nonogram.view.LoginView;

/**
 * ログイン画面の入力処理と画面遷移を管理するコントローラクラス
 */
public class LoginController {

    private LoginModel model;
    private LoginView view;
    private AppController appController;

    /**
     * コンストラクタ
     *
     * @param model ログイン処理で使用するModel
     * @param view ログイン画面のView
     * @param appController 画面遷移を管理するコントローラ
     */
    public LoginController(LoginModel model, LoginView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
    }

    /**
     * ログイン画面を初期化し、ボタンイベントを登録する
     */
    public void initialize() {
        view.initialize();

        view.getLoginButton().setOnAction(e -> onLogin());
        view.getSignupLink().setOnAction(e -> view.requestSignup());
        view.getCancelButton().setOnAction(e -> view.close());

        view.render();

        if (view.isLoginConfirmed()) {
            appController.navigateTo("list");
        } else if (view.isSignupRequested()) {
            appController.navigateTo("signup");
        }
    }

    /**
     * ログインボタンが押されたときの処理
     * 入力情報が正しければログイン済みプレイヤーを保持し、問題リスト画面へ遷移する
     */
    private void onLogin() {
        if (isBlank(view.getEmail()) || isBlank(view.getPassword())) {
            view.showMessage("メールアドレスとパスワードを入力してください。");
            return;
        }

        boolean success = model.login(view.getEmail(), view.getPassword());
        if (success) {
            appController.setCurrentPlayer(model.getLoginPlayer());
            view.confirmLogin();
        } else {
            view.showMessage("メールアドレスまたはパスワードが正しくありません。");
        }
    }

    /**
     * 文字列が未入力かどうかを判定する
     *
     * @param value 判定する文字列
     * @return nullまたは空文字の場合はtrue
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
