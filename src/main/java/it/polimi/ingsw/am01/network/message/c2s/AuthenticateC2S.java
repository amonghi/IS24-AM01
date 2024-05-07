package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.NetworkException;
import it.polimi.ingsw.am01.network.ProtocolState;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.NameAlreadyTakenS2C;
import it.polimi.ingsw.am01.network.message.s2c.SetPlayerNameS2C;

public record AuthenticateC2S(String playerName) implements C2SNetworkMessage {
    public static final String ID = "AUTHENTICATE";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, ProtocolState state) throws NetworkException {
        try {
            PlayerProfile profile = controller.authenticate(playerName);
            state.setPlayerName(profile.getName());
            connection.send(new SetPlayerNameS2C(profile.getName()));
        } catch (IllegalArgumentException e) {
            connection.send(new NameAlreadyTakenS2C(this.playerName));
        }
    }
}
