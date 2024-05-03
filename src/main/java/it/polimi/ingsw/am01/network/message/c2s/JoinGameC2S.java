package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.GameNotFoundException;
import it.polimi.ingsw.am01.model.exception.IllegalGameStateException;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.exception.NotAuthenticatedException;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.GameAlreadyStartedS2C;
import it.polimi.ingsw.am01.network.message.s2c.GameJoinedS2C;
import it.polimi.ingsw.am01.network.message.s2c.GameNotFoundS2C;

public record JoinGameC2S(int gameId) implements C2SNetworkMessage {
    public static final String ID = "JOIN_GAME";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            controller.joinGame(gameId, virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName());
            virtualView.setGame(virtualView.getGameManager().getGame(gameId).orElseThrow(() -> new GameNotFoundException(gameId)));
            connection.send(new GameJoinedS2C(gameId, GameStatus.AWAITING_PLAYERS));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(gameId));
        } catch (IllegalGameStateException e) {
            connection.send(new GameAlreadyStartedS2C(gameId));
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
