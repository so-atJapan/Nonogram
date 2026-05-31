package Nonogram.model;

public abstract class Player {
    private final String USER_NAME;

    public Player(String userName){
        this.USER_NAME = userName;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public abstract int getPlayerId();

    public abstract String getEMail();

    public abstract String getHashedPassword();
}
