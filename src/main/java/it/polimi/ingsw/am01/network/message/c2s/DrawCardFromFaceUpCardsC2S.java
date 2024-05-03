package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.model.game.DrawResult;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.GameNotFoundS2C;
import it.polimi.ingsw.am01.network.message.s2c.InvalidCardS2C;
import it.polimi.ingsw.am01.network.message.s2c.PlayerNotInGameS2C;

import java.util.Objects;

public record DrawCardFromFaceUpCardsC2S(int cardId) implements C2SNetworkMessage {

    public static final String ID = "DRAW_CARD_FROM_FACE_UP_CARDS";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            DrawResult drawResult = controller.drawCardFromFaceUpCards(virtualView.getGame().orElseThrow(PlayerNotInGameException::new).getId(), virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), cardId);
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(virtualView.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(virtualView.getGame().orElse(null)).getId()));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(cardId));
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
