package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.DrawSource;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted after a user tries to draw from an empty source.
 * @param drawSource the empty source
 * @see DrawSource
 * @see it.polimi.ingsw.am01.model.game.Game#drawCard(PlayerProfile, DrawSource) Game.drawCard(player, source)
 */
public record CardDrawnFromEmptySourceEvent(DrawSource drawSource) implements GameEvent {
}
