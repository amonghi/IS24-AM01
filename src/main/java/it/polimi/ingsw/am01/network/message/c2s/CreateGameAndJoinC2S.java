package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.exception.InvalidMaxPlayersException;
import it.polimi.ingsw.am01.model.exception.NotAuthenticatedException;
import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.GameJoinedS2C;
import it.polimi.ingsw.am01.network.message.s2c.InvalidMaxPlayersS2C;

public record CreateGameAndJoinC2S(int maxPlayers) implements C2SNetworkMessage {
    public static final String ID = "CREATE_GAME_AND_JOIN";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            Game game = controller.createAndJoinGame(maxPlayers, virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName());
            connection.send(new GameJoinedS2C(game.getId(), GameStatus.AWAITING_PLAYERS));
        } catch (InvalidMaxPlayersException e) {
            connection.send(new InvalidMaxPlayersS2C(maxPlayers));
        }
    }
}
