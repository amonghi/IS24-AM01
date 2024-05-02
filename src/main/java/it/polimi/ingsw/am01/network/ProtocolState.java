package it.polimi.ingsw.am01.network;

import java.util.Optional;

public class ProtocolState {
    private ProtocolStatus status;
    private String playerName;
    private Integer gameId;

    public ProtocolState() {
        this.status = ProtocolStatus.NOT_AUTHENTICATED;
        this.playerName = null;
        this.gameId = null;
    }

    public ProtocolStatus getStatus(){
        return status;
    }

    public void setStatus(ProtocolStatus status) {
        this.status = status;
    }

    public Optional<String> getPlayerName() {
        return Optional.ofNullable(playerName);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Optional<Integer> getGameId() {
        return Optional.ofNullable(gameId);
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
}
