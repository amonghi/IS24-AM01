package it.polimi.ingsw.am01.model.exception;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.PlayArea;

/**
 * @see PlayArea#placeAt(PlayArea.Position, Card, Side)
 * @see PlayArea#placeAt(int, int, Card, Side)
 */
public class IllegalPlacementException extends Exception {
    public IllegalPlacementException(String message) {
        super(message);
    }
}
