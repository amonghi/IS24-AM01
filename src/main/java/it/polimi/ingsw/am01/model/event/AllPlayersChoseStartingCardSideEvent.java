package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.GameStatus;

/**
 * This event is emitted when all players have chosen the side of the starting card.
 * {@code GameStatus} will be set to {@code SETUP_COLOR}.
 * @see it.polimi.ingsw.am01.model.game.selectionphase.SelectionPhase
 */
public class AllPlayersChoseStartingCardSideEvent implements GameEvent {
    private final GameStatus gameStatus = GameStatus.SETUP_COLOR;

    /**
     * @return the new game's status
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
