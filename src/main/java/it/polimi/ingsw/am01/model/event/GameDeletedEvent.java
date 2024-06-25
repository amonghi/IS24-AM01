package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

/**
 * This event is emitted when a game has been deleted by the {@code GameManager} class.
 * @param gameDeletedId the id of the game deleted
 * @see it.polimi.ingsw.am01.model.game.GameManager#deleteGame(Game) GameManager.deleteGame(game)
 */
public record GameDeletedEvent(int gameDeletedId) implements GameManagerEvent {
}
