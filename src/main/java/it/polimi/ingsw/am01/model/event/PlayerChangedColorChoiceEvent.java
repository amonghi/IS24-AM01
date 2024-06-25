package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted when a player expresses a color preference.
 * @param player the player who has chosen
 * @param playerColor the color chosen
 * @see it.polimi.ingsw.am01.model.game.Game#selectColor(PlayerProfile, PlayerColor) Game.selectColor(player, color)
 */
public record PlayerChangedColorChoiceEvent(PlayerProfile player, PlayerColor playerColor) implements GameEvent {
}
