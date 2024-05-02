package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Collections;
import java.util.List;

public class PlayerJoinedEvent extends GameEvent {
    private final List<PlayerProfile> playerList;

    public PlayerJoinedEvent(List<PlayerProfile> playerList) {
        this.playerList = playerList;
    }

    public List<PlayerProfile> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

}
