package Nonogram.view;
import Nonogram.model.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView extends Application {

    @Override
    public void start(Stage stage) {

        // タイトル
        Label titleLabel = new Label("ログイン");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // メールアドレス
        Label emailLabel = new Label("メールアドレス");
        TextField emailField = new TextField();
        emailField.setPromptText("example@email.com");

        // パスワード
        Label passLabel = new Label("パスワード");
        PasswordField passField = new PasswordField();
        passField.setPromptText("パスワードを入力");

        // ログインボタン
        Button loginButton = new Button("ログイン");
        loginButton.setPrefWidth(200);

        // アカウント作成誘導
        Label guideLabel = new Label("アカウントをお持ちでない方");
        guideLabel.setStyle("-fx-text-fill: gray;");

        Hyperlink createAccountLink = new Hyperlink("アカウントを作成");
        createAccountLink.setTextFill(Color.BLUE);

        // レイアウト
        VBox root = new VBox(12);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                titleLabel,
                emailLabel,
                emailField,
                passLabel,
                passField,
                loginButton,
                guideLabel,
                createAccountLink
        );

        // シーン
        Scene scene = new Scene(root, 350, 360);

        // ステージ設定
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();

        //仮///////////////////////////////

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String passward = passField.getText();

            Player player = new Player();



            if(player.login(email, passward)){
                System.out.println("Logined");
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}