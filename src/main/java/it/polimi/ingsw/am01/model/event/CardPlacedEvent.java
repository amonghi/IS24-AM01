package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted after a user has placed a card on his {@link PlayArea} (resource, golden or starting card).
 * @param player the player who has placed the card
 * @param cardPlacement the card placement
 * @see it.polimi.ingsw.am01.model.game.PlayArea.CardPlacement
 * @see it.polimi.ingsw.am01.model.game.Game#placeCard(PlayerProfile, Card, Side, int, int) Game.placeCard(player, card, side, i, j)
 * @see it.polimi.ingsw.am01.model.game.Game#selectStartingCardSide(PlayerProfile, Side) Game.selectStartingCardSide(player, side)
 */
public record CardPlacedEvent(PlayerProfile player, PlayArea.CardPlacement cardPlacement) implements GameEvent {
}
