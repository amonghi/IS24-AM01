package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.util.Set;

/**
 * This message tells the player his new hand.
 * This message is sent when a player draws or places a card.
 * @param currentHand a set of card ids
 * @see it.polimi.ingsw.am01.model.event.HandChangedEvent
 */
public record UpdatePlayerHandS2C(Set<Integer> currentHand) implements S2CNetworkMessage {
    public static final String ID = "UPDATE_PLAYER_HAND";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }
}
