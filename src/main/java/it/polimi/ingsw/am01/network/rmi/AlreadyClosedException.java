package it.polimi.ingsw.am01.network.rmi;

import it.polimi.ingsw.am01.network.NetworkException;

/**
 * Exception thrown when a message is transmitted to a {@link Receiver} that has already been closed.
 */
public class AlreadyClosedException extends NetworkException {
}
