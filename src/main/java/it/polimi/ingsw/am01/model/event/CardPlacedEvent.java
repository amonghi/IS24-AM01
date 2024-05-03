package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record CardPlacedEvent(PlayerProfile player, PlayArea.CardPlacement cardPlacement) implements GameEvent {
}
