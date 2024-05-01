package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.List;

public class UpdatePlayerListEvent extends GameEvent {
    private final List<PlayerProfile> playerProfiles;

    public UpdatePlayerListEvent(List<PlayerProfile> playerProfiles) {
        this.playerProfiles = playerProfiles;
    }

    public List<PlayerProfile> getPlayerProfiles() {
        return playerProfiles;
    }
}
