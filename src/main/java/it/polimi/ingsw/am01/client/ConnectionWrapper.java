package it.polimi.ingsw.am01.client;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.ReceiveNetworkException;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;

public class ConnectionWrapper {
    private final Connection<C2SNetworkMessage, S2CNetworkMessage> connection;
    private final View view;

    public ConnectionWrapper(Connection<C2SNetworkMessage, S2CNetworkMessage> connection, View view) {
        this.connection = connection;
        this.view = view;

        new Thread(this::poll).start();
    }

    private void poll() {
        while (true) {
            S2CNetworkMessage message;
            try {
                message = connection.receive();
            } catch (ReceiveNetworkException e) {
                view.connectionLost();
                //TODO: try to reconnect
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
                    default -> throw new IllegalStateException("Unexpected value: " + message); //TODO: manage
                }
            });
        }
    }
}
