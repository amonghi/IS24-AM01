package it.polimi.ingsw.am01.model.event;

/**
 * This event is emitted when a game ends.
 * The event is managed by the {@code GameManager}, which will remove the game from disk.
 * @see it.polimi.ingsw.am01.model.game.Game
 * @see it.polimi.ingsw.am01.model.game.GameManager
 */
public record GameClosedEvent() implements GameEvent {
}
