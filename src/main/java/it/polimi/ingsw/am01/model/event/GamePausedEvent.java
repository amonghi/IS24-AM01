package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

/**
 * This event is emitted when a game is paused ({@code SUSPENDED}) by the server.
 * @see it.polimi.ingsw.am01.model.game.GameStatus
 * @see Game#pauseGame()
 */
public record GamePausedEvent() implements GameEvent {
}
