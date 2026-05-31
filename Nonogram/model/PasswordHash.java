package Nonogram.model;

import org.mindrot.jbcrypt.BCrypt;

/**
 * パスワードのハッシュ化と照合を行うユーティリティクラス
 */
public final class PASSWORD_HASH {

    // インスタンス化不要なユーティリティクラス
    private PASSWORD_HASH() {}

    /** パスワード登録時：ハッシュ化して保存 */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /** ログイン時：入力と保存済みハッシュを照合 */
    public static boolean verify(String plainPassword, String storedHash) {
        return BCrypt.checkpw(plainPassword, storedHash);
    }
}
