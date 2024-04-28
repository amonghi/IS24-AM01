package it.polimi.ingsw.am01.network.message;

import it.polimi.ingsw.am01.eventemitter.Event;

public interface NetworkMessage extends Event {
    String getId();
}
