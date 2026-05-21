package Nonogram.model;

/**
 * ログイン処理とログイン結果を保持するModelクラス
 */
public class LoginModel {

    private static DAO dao = new DAO();
    private PasswordHasher passwordHasher = new PasswordHasher();
    private LoginPlayer loginPlayer;

    /**
     * メールアドレスとパスワードを検証してログインする
     *
     * @param email 入力されたメールアドレス
     * @param password 入力されたパスワード
     * @return ログインに成功した場合はtrue
     */
    public boolean login(String email, String password) {
        String passwordHash = passwordHasher.hash(password);
        loginPlayer = dao.getLoginPlayer(email, passwordHash);

        return loginPlayer != null;
    }

    /**
     * ログインに成功したプレイヤーを取得する
     *
     * @return ログイン済みプレイヤー
     */
    public LoginPlayer getLoginPlayer() {
        return loginPlayer;
    }
}