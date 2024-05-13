package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

public record UndoPlacementEvent(PlayerProfile pp, PlayArea.Position position, int seq) implements GameEvent {
}
