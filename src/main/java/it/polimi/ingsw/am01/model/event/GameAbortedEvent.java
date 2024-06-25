package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when there is only one player connected.
 * This event is emitted only when the game is on {@code SETUP_STARTING_CARD_SIDE}, {@code SETUP_COLOR} or {@code SETUP_OBJECTIVE} status.
 * After this, game will be deleted.
 * @see it.polimi.ingsw.am01.model.game.Game#handleDisconnection(PlayerProfile) Game.handleDisconnection(player)
 */
public class GameAbortedEvent implements GameEvent {
}
