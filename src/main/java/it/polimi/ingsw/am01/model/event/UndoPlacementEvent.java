package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted after a placement's undo.
 * @param pp the owner of the placement
 * @param position the position of the placement
 * @param score the score of the placement
 * @param seq the sequence number of the placement
 * @see it.polimi.ingsw.am01.model.game.Game#undoLastPlacement(PlayerProfile) Game.undoLastPlacement(player)
 */
public record UndoPlacementEvent(PlayerProfile pp, PlayArea.Position position, int score,
                                 int seq) implements GameEvent {
}
