package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;

/**
 * @see PlayArea#placeAt(PlayArea.Position, Card, Side)
 * @see PlayArea#placeAt(int, int, Card, Side)
 */
public class IllegalPlacementException extends RuntimeException {
    public IllegalPlacementException(String message) {
        super(message);
    }
}
