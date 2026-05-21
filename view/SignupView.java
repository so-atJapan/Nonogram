package Nonogram.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * サインアップ画面のViewクラス（セミモーダル）。
 * UIの生成・表示のみを担い、ロジックは持たない。
 */
public class SignupView {

    private Stage primaryStage;
    private Stage modalStage;

    private TextField     nameField;
    private TextField     emailField;
    private PasswordField passField;
    private PasswordField passConfirmField;
    private Button        signupButton;
    private Hyperlink     loginLink;

    /**
     * コンストラクタ
     *
     * @param primaryStage 親ウィンドウ（セミモーダルのオーナー）
     */
    public SignupView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * UI部品を生成し、セミモーダルのStageを準備する。
     * render() を呼ぶ前に必ず実行する。
     */
    public void initialize() {
        modalStage = new Stage();
        modalStage.initOwner(primaryStage);
        modalStage.initModality(Modality.WINDOW_MODAL); // 親のみ操作不可 = セミモーダル
        modalStage.setTitle("サインアップ");
        modalStage.setResizable(false);

        // ---- 部品生成 ----
        Label titleLabel = new Label("サインアップ");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));

        Label nameLabel = new Label("ユーザー名");
        nameField = new TextField();
        nameField.setPromptText("ニックネームを入力");
        nameField.setPrefWidth(260);

        Label emailLabel = new Label("メールアドレス");
        emailField = new TextField();
        emailField.setPromptText("example@email.com");
        emailField.setPrefWidth(260);

        Label passLabel = new Label("パスワード");
        passField = new PasswordField();
        passField.setPromptText("パスワードを入力");
        passField.setPrefWidth(260);

        Label passConfirmLabel = new Label("パスワード（確認）");
        passConfirmField = new PasswordField();
        passConfirmField.setPromptText("もう一度入力");
        passConfirmField.setPrefWidth(260);

        signupButton = new Button("アカウントを作成");
        signupButton.setPrefWidth(260);
        signupButton.setDefaultButton(true);

        Label guideLabel = new Label("すでにアカウントをお持ちの方");
        guideLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        loginLink = new Hyperlink("ログイン");

        HBox linkBox = new HBox(4, guideLabel, loginLink);
        linkBox.setAlignment(Pos.CENTER);

        // ---- レイアウト ----
        VBox root = new VBox(10,
            titleLabel,
            nameLabel,        nameField,
            emailLabel,       emailField,
            passLabel,        passField,
            passConfirmLabel, passConfirmField,
            signupButton,
            new Separator(),
            linkBox
        );
        root.setPadding(new Insets(28, 32, 28, 32));
        root.setAlignment(Pos.CENTER_LEFT);

        modalStage.setScene(new Scene(root, 340, 488));
        modalStage.setOnShown(e -> centerOnParent());
    }

    /**
     * セミモーダルを表示する（showAndWait）。
     * 閉じるまで呼び出し元はブロックされる。
     */
    public void render() {
        modalStage.showAndWait();
    }

    /** モーダルを閉じる */
    public void close() {
        if (modalStage != null) modalStage.close();
    }

    /** エラーダイアログを表示する */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(modalStage);
        alert.setTitle("エラー");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ---- View固有のバリデーション ----
    /** パスワードと確認用が一致するか */
    public boolean isPasswordMatch() {
        return passField.getText().equals(passConfirmField.getText());
    }

    // ---- Getters ----
    public String    getUserName()     { return nameField.getText().trim(); }
    public String    getEmail()        { return emailField.getText().trim(); }
    public String    getPassword()     { return passField.getText(); }
    public Button    getSignupButton() { return signupButton; }
    public Hyperlink getLoginLink()    { return loginLink; }
    public Stage     getModalStage()   { return modalStage; }

    // ---- private ----
    private void centerOnParent() {
        modalStage.setX(primaryStage.getX() + (primaryStage.getWidth()  - modalStage.getWidth())  / 2);
        modalStage.setY(primaryStage.getY() + (primaryStage.getHeight() - modalStage.getHeight()) / 2);
    }
}
