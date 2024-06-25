package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.List;

/**
 * This message tells to the current player his playable positions in the play area.
 * This message is sent as soon as the {@code PLACING} phase begins,
 * so that he can choose one of these positions to place the card.
 * @param playablePositions a list that contains all the playable positions
 * @see it.polimi.ingsw.am01.model.game.TurnPhase
 * @see it.polimi.ingsw.am01.model.game.PlayArea
 */
public record SetPlayablePositionsS2C(List<PlayablePosition> playablePositions) implements S2CNetworkMessage {
    public static final String ID = "SET_PLAYABLE_POSITIONS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * This record represents a playable position
     * @param i the i coordinate of the {@code PlayArea}
     * @param j the j coordinate of the {@code PlayArea}
     */
    public record PlayablePosition(int i, int j) implements Serializable {
    }
}
