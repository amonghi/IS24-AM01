package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

/**
 * This event is emitted when the maximum capacity of the game is reached
 * or when an early start is requested by players.
 * @see Game#startGame()
 * @see it.polimi.ingsw.am01.network.message.c2s.StartGameC2S
 */
public record AllPlayersJoinedEvent() implements GameEvent {
}
