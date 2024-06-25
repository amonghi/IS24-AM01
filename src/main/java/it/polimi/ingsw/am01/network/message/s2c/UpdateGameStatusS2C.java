package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells the players the new status of the game: {@code SETUP_COLOR}.
 * This message is sent after all players have chosen the side of the starting card.
 * @param gameStatus the new game's status
 * @see GameStatus
 * @see it.polimi.ingsw.am01.model.event.AllPlayersChoseStartingCardSideEvent
 */
public record UpdateGameStatusS2C(GameStatus gameStatus) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_GAME_STATUS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
