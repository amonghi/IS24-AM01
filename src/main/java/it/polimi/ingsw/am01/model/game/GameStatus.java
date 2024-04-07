package it.polimi.ingsw.am01.model.game;

/**
 * GameStatus represents all possibile macro-phases of a {@link Game}.
 * <p>
 * The sequence of status transitions is listed below:
 * <ol>
 *     <li>{@code AWAITING_PLAYERS}</li>
 *     <li>{@code SETUP_STARTING_CARD_SIDE}</li>
 *     <li>{@code SETUP_COLOR}</li>
 *     <li>{@code SETUP_OBJECTIVE}</li>
 *     <li>{@code AWAITING_START}</li>
 *     <li>{@code PLAY}</li>
 *     <li>{@code SECOND_LAST_TURN}</li>
 *     <li>{@code LAST_TURN}</li>
 *     <li>{@code FINISHED}</li>
 * </ol>
 */
public enum GameStatus {
    AWAITING_PLAYERS,
    SETUP_STARTING_CARD_SIDE,
    SETUP_COLOR,
    SETUP_OBJECTIVE,
    /**
     * {@code AWAITING_START} indicates that all decisions have been made and the turns' phase can start
     */
    AWAITING_START,
    /**
     * @see it.polimi.ingsw.am01.model.game.TurnPhase
     */
    PLAY,
    /**
     * @see it.polimi.ingsw.am01.model.game.TurnPhase
     */
    SECOND_LAST_TURN,
    /**
     * @see it.polimi.ingsw.am01.model.game.TurnPhase
     */
    LAST_TURN,
    FINISHED,
    /**
     * {@code SUSPENDED} is used to make {@link Game} paused.
     */
    SUSPENDED
}