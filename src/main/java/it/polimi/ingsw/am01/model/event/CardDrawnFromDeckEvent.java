package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Deck;
import it.polimi.ingsw.am01.model.game.DrawSource;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This event is emitted after a user draws a card from decks.
 * @param resourceCardDeck the resource deck
 * @param goldenCardDeck the golden deck
 * @see it.polimi.ingsw.am01.model.game.Game#drawCard(PlayerProfile, DrawSource)  Game.drawCard(player, source)
 */
public record CardDrawnFromDeckEvent(Deck resourceCardDeck, Deck goldenCardDeck) implements GameEvent {
}
