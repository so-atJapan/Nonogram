package Nonogram.model;

public class LoginPlayer extends Player {
    private int playerId;
    private String eMail;

    public LoginPlayer(String name, int playerId, String eMail){
        super(name);
        this.playerId = playerId;
        this.eMail = eMail;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public String getEMail() {
        return eMail;
    }
}
