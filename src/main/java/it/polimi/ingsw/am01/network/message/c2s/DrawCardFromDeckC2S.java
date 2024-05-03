package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.DeckLocation;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.GameNotFoundException;
import it.polimi.ingsw.am01.model.exception.IllegalMoveException;
import it.polimi.ingsw.am01.model.exception.NotAuthenticatedException;
import it.polimi.ingsw.am01.model.exception.PlayerNotInGameException;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.GameNotFoundS2C;
import it.polimi.ingsw.am01.network.message.s2c.PlayerNotInGameS2C;

import java.util.Objects;

public record DrawCardFromDeckC2S(DeckLocation deckLocation) implements C2SNetworkMessage {

    public static final String ID = "DRAW_CARD_FROM_DECK";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            controller.drawCardFromDeck(virtualView.getGame().orElseThrow(PlayerNotInGameException::new).getId(), virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), deckLocation);
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(virtualView.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(virtualView.getGame().orElse(null)).getId()));
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
