package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

/**
 * This message tells players that the game is paused ({@code SUSPENDED}).
 * The pause is set exclusively by the server, in case there are not enough players connected.
 * @see GameStatus
 * @see it.polimi.ingsw.am01.model.event.GamePausedEvent
 */
public class SetGamePauseS2C implements S2CNetworkMessage {
    public static final String ID = "SET_PAUSE";
    private final GameStatus gameStatus = GameStatus.SUSPENDED;

    public SetGamePauseS2C() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * @return the current game's status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SetGamePauseS2C{" +
                "gameStatus=" + gameStatus +
                '}';
    }
}
