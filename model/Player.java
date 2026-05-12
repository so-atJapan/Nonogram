package Nonogram.model;

import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Player extends GuestPlayer{
    private int playerId;
    private String userName;
    private String passwordHash;
    private String e_mail;
    private LocalDateTime createdAt;
    private DAO dao = new DAO();

    /**
     * ハッシュ方法格納
     */
    private enum HashAlgorithm {
		SHA256
	}

    /**
     * ログイン確認
     * 
     * @param password
     * @return 正しければtrue
     */
    public boolean login(String password) {

        String hashAlgorithm = "SHA-256";
        StringBuffer stringBuffer = new StringBuffer();

        try {
			MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
			byte[] cipherBytes = messageDigest.digest(password.getBytes());

			for (int i=0; i<cipherBytes.length; i++) {
				stringBuffer.append(String.format("%02x", cipherBytes[i]&0xff));
			}

		} catch (NoSuchAlgorithmException ex1) {
			System.out.println("ハッシュアルゴリズム名が不正です。");
		} catch (NullPointerException ex2) {
			System.out.println("ハッシュアルゴリズム名が指定されていません。");
		}

        String reqPass = stringBuffer.toString();
        String checkPass = this.dao.getPassWordHash();
        if (reqPass.equals(checkPass)) {
            System.out.println("ログイン成功");
            return true;
        } else {
            System.out.println("ログイン失敗");
            return false;
        }
        
    }

    // ゲストプレイヤーになる
    public void logout() {}

    // プレイログ
}
