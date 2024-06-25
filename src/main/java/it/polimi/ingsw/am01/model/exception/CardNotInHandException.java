package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

/**
 * This exception is thrown when a player tries to place a card that is not in his hand.
 * @see it.polimi.ingsw.am01.model.game.Game#placeCard(PlayerProfile, Card, Side, int, int) Game.placeCard(player, card, side, i, j)
 */
public class CardNotInHandException extends InvalidCardException {

    public CardNotInHandException() {
        super();
    }
}
