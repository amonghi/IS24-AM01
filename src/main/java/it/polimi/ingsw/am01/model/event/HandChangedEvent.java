package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.DrawSource;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.util.Set;

/**
 * This event is emitted when a player draws or places a card (hand changes).
 * @param playerProfile the player who has performed the action
 * @param currentHand the new player's hand
 * @see it.polimi.ingsw.am01.model.game.Game#placeCard(PlayerProfile, Card, Side, int, int) Game.placeCard(player, card, side, i, j)
 * @see it.polimi.ingsw.am01.model.game.Game#drawCard(PlayerProfile, DrawSource) Game.drawCard(player, source)
 * @see it.polimi.ingsw.am01.model.player.PlayerData
 */
public record HandChangedEvent(PlayerProfile playerProfile, Set<Card> currentHand) implements GameEvent {
}
