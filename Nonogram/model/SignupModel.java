package Nonogram.model;

/**
 * サインアップ処理と作成されたプレイヤー情報を保持するModelクラス
 */
public class SignupModel {

    private static final DAO DAO = new DAO();
    private LoginPlayer createdPlayer;

    /**
     * 入力情報を検証して新しいアカウントを作成する
     *
     * @param userName        入力されたユーザー名
     * @param email           入力されたメールアドレス
     * @param plainPassword   入力されたパスワード
     * @param confirmPassword 確認用パスワード
     * @return アカウント作成に成功した場合はtrue
     */
    public boolean signup(String userName, String email, String plainPassword, String confirmPassword) {
        String hashedPassword = PASSWORD_HASH.hash(plainPassword);
        boolean created = DAO.insertPlayer(userName, email, hashedPassword);
        if (!created) {
            return false;
        }
        createdPlayer = DAO.getLoginPlayer(email);
        return createdPlayer != null;
    }

    /**
     * 指定されたメールアドレスのプレイヤーが存在するか判定する
     */
    public boolean existsPlayerByEmail(String email) {
        return DAO.existsPlayerByEmail(email);
    }

    /**
     * 作成されたログイン済みプレイヤーを取得する
     */
    public LoginPlayer getCreatedPlayer() {
        return createdPlayer;
    }
}
