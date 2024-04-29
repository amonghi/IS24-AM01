package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

public class VirtualView implements Runnable {
    private final Controller controller;
    private final Connection<S2CNetworkMessage, C2SNetworkMessage> connection;
    private final ProtocolState protocolState;

    public VirtualView(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection) {
        this.controller = controller;
        this.connection = connection;
        this.protocolState = new ProtocolState();
    }

    @Override
    public void run() {
        while (true) {
            C2SNetworkMessage message = this.connection.receive();
            message.execute(controller, connection, protocolState);
        }
    }
}
