package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.DoubleSideChoiceS2C;
import it.polimi.ingsw.am01.network.message.s2c.GameNotFoundS2C;
import it.polimi.ingsw.am01.network.message.s2c.PlayerNotInGameS2C;

import java.util.Objects;

public record SelectStartingCardSideC2S(Side side) implements C2SNetworkMessage {
    public static final String ID = "SELECT_STARTING_CARD_SIDE";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            controller.selectStartingCardSide(
                    virtualView.getGame().orElseThrow(PlayerNotInGameException::new).getId(),
                    virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(),
                    side()
            );
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(virtualView.getPlayerProfile().orElse(null)).getName()));
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(virtualView.getGame().orElse(null)).getId()));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleSideChoiceS2C(side()));
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
