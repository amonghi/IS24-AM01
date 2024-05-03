package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.HashSet;
import java.util.Set;

public class SecretObjectiveChosenEvent extends GameEvent {

    private final Set<PlayerProfile> playersHaveChosen;

    public SecretObjectiveChosenEvent(Set<PlayerProfile> playersHaveChosen) {
        this.playersHaveChosen = new HashSet<>(playersHaveChosen);
    }

    public Set<PlayerProfile> getPlayersHaveChosen() {
        return playersHaveChosen;
    }
}
