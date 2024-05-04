package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.exception.NameAlreadyTakenException;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.NameAlreadyTakenS2C;
import it.polimi.ingsw.am01.network.message.s2c.SetPlayerNameS2C;
import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;

import java.util.stream.Collectors;

public record AuthenticateC2S(String playerName) implements C2SNetworkMessage {
    public static final String ID = "AUTHENTICATE";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            PlayerProfile profile = controller.authenticate(playerName);
            connection.send(new SetPlayerNameS2C(profile.getName()));
            connection.send(new UpdateGameListS2C(virtualView.getGameManager().getGames().stream().collect(Collectors.toMap(
                    Game::getId,
                    g -> new UpdateGameListS2C.GameStat(g.getPlayerProfiles().size(), g.getMaxPlayers())
            ))));
        } catch (NameAlreadyTakenException e) {
            connection.send(new NameAlreadyTakenS2C(this.playerName));
        }
    }
}
