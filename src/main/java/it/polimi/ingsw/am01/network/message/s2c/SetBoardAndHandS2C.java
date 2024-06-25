package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

/**
 * This message gives players all the information they need to start playing.
 * Information includes: board, personal hand, common objectives and visible colors of the two decks.
 * @param resourceDeckColor the color of the resource deck
 * @param goldenDeckColor the color of the golden deck
 * @param commonObjectives common objectives
 * @param faceUpCards faceup cards
 * @param hand the player hand
 * @see it.polimi.ingsw.am01.model.game.Board
 */
public record SetBoardAndHandS2C(CardColor resourceDeckColor,
                                 CardColor goldenDeckColor,
                                 Set<Integer> commonObjectives,
                                 Set<Integer> faceUpCards,
                                 Set<Integer> hand) implements S2CNetworkMessage {
    public static final String ID = "SET_BOARD_AND_HAND";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
