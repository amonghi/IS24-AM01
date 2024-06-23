package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

/**
 * This message tells players about new faceup cards list.
 * This message is sent after a user has drawn a faceup card.
 * If the faceup cards are not present, the set will be empty.
 * @param faceUpCards the set of faceup cards
 * @see it.polimi.ingsw.am01.model.event.FaceUpCardReplacedEvent
 */
public record UpdateFaceUpCardsS2C(Set<Integer> faceUpCards) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_FACE_UP_CARDS";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
