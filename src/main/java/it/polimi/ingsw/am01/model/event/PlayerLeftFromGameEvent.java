package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record PlayerLeftFromGameEvent(PlayerProfile playerProfile, Game game) implements GameManagerEvent {
}
