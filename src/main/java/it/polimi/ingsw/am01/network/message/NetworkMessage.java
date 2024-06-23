package it.polimi.ingsw.am01.network.message;

import java.io.Serializable;

/**
 * Interface for all the messages that can be sent through the network.
 * All the messages are {@link Serializable}.
 * Each message has an id that identifies the type of the message.
 */
public interface NetworkMessage extends Serializable {

    /**
     * Returns the id of the message.
     *
     * @return the id of the message
     */
    String getId();
}
