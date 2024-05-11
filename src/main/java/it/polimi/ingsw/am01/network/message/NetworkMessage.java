package it.polimi.ingsw.am01.network.message;

import java.io.Serializable;

// needs to be Serializable to work with RMI
public interface NetworkMessage extends Serializable {
    String getId();
}
