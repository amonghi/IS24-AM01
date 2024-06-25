package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players that has been made an undo of the last placement of a player.
 * This occurs when a user places a card and subsequently disconnects, without drawing.
 * @param profile the name of the player to which the last placement was removed
 * @param i the i coordinate of the removed placement
 * @param j the j coordinate of the removed placement
 * @param score the score of the removed placement
 * @param seq the sequence number of the removed placement
 * @see it.polimi.ingsw.am01.model.event.UndoPlacementEvent
 * @see PlayArea#undoPlacement()
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public record UpdatePlayAreaAfterUndoS2C(String profile, int i, int j, int score,
                                         int seq) implements S2CNetworkMessage {
    public static final String ID = "UNDO_PLACEMENT";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
