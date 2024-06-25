package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

/**
 * This event is emitted when a game is created.
 * @param newGame the new game
 * @see it.polimi.ingsw.am01.model.game.GameManager#createGame(int) GameManager.createGame(maxPlayers)
 */
public record GameCreatedEvent(Game newGame) implements GameManagerEvent {
}
