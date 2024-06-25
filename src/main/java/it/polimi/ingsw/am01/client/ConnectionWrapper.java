package it.polimi.ingsw.am01.client;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The main purpose is to receive network messages from the server.
 * In response to these messages the appropriate methods in the {@link View} class are invoked.
 *
 * @see View
 * @see S2CNetworkMessage
 * @see Connection
 */
public class ConnectionWrapper {
    private static final Logger LOGGER = Logger.getLogger(ConnectionWrapper.class.getName());

    private final Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private final View view;

    /**
     * It starts a new thread, responsible for receiving network messages from the server
     *
     * @param connection The open {@link Connection} between client and server
     * @param view       The main {@link View} class, containing the local and reduced copy of server data
     *                   and the methods to invoke in response to the network message
     */
    public ConnectionWrapper(Connection<C2SNetworkMessage, S2CNetworkMessage> connection, View view) {
        this.connection = connection;
        this.view = view;

        new Thread(this::poll).start();
    }

    /**
     * It receives network messages from the server and calls the handler method
     * in the {@link View} class, based on the type of the received message.
     * <p>
     * The handler method is run on a different thread.
     * <p>
     * If it catches a {@link ReceiveNetworkException} it informs the {@link View}
     * that the connection has been lost.
     */
    private void poll() {
        while (true) {
            S2CNetworkMessage message;
            try {
                message = connection.receive();
            } catch (ReceiveNetworkException e) {
                view.connectionLost();
                return;
            }

            view.runLater(() -> {
                switch (message) {
                    case SetPlayerNameS2C m -> view.handleMessage(m);
                    case NameAlreadyTakenS2C m -> view.handleMessage(m);
                    case UpdateGameListS2C m -> view.handleMessage(m);
                    case SetPlayablePositionsS2C m -> view.handleMessage(m);
                    case UpdatePlayAreaS2C m -> view.handleMessage(m);
                    case InvalidPlacementS2C m -> view.handleMessage(m);
                    case GameJoinedS2C m -> view.handleMessage(m);
                    case UpdatePlayerListS2C m -> view.handleMessage(m);
                    case SetStartingCardS2C m -> view.handleMessage(m);
                    case UpdateGameStatusS2C m -> view.handleMessage(m);
                    case UpdatePlayerColorS2C m -> view.handleMessage(m);
                    case UpdateGameStatusAndSetupObjectiveS2C m -> view.handleMessage(m);
                    case UpdateObjectiveSelectedS2C m -> view.handleMessage(m);
                    case UpdatePlayAreaAfterUndoS2C m -> view.handleMessage(m);
                    case SetBoardAndHandS2C m -> view.handleMessage(m);
                    case UpdatePlayerHandS2C m -> view.handleMessage(m);
                    case UpdateDeckStatusS2C m -> view.handleMessage(m);
                    case UpdateGameStatusAndTurnS2C m -> view.handleMessage(m);
                    case UpdateFaceUpCardsS2C m -> view.handleMessage(m);
                    case PlayerDisconnectedS2C m -> view.handleMessage(m);
                    case PlayerReconnectedS2C m -> view.handleMessage(m);
                    case SetupAfterReconnectionS2C m -> view.handleMessage(m);
                    case SetGamePauseS2C m -> view.handleMessage(m);
                    case GameResumedS2C m -> view.handleMessage(m);
                    case GameFinishedS2C m -> view.handleMessage(m);
                    case KickedFromGameS2C m -> view.handleMessage(m);
                    case NewMessageS2C m -> view.handleMessage(m);
                    case BroadcastMessageSentS2C m -> view.handleMessage(m);
                    case DirectMessageSentS2C m -> view.handleMessage(m);
                    case PingS2C m -> {
                    }
                    default -> LOGGER.log(Level.WARNING, "Unexpected message", message);
                }
            });
        }
    }
}
