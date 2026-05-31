package Nonogram.model;

public class LoginPlayer extends Player {
    private final int PLAYER_ID;
    private final String E_MAIL;
    private final String HASH_PASSWORD;

    public LoginPlayer(String name, int playerId, String eMail, String hashedPassword){
        super(name);
        this.PLAYER_ID = playerId;
        this.E_MAIL = eMail;
        this.HASH_PASSWORD = hashedPassword;
    }

    @Override
    public int getPlayerId() {
        return PLAYER_ID;
    }

    @Override
    public String getEMail() {
        return E_MAIL;
    }

    @Override
    public String getHashedPassword() {
        return HASH_PASSWORD;
    }
}
