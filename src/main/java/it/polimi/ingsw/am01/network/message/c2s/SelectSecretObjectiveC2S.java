package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.choice.DoubleChoiceException;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.DoubleChoiceS2C;
import it.polimi.ingsw.am01.network.message.s2c.GameNotFoundS2C;
import it.polimi.ingsw.am01.network.message.s2c.InvalidObjectiveSelectionS2C;
import it.polimi.ingsw.am01.network.message.s2c.PlayerNotInGameS2C;

import java.util.Objects;

public record SelectSecretObjectiveC2S(int objective) implements C2SNetworkMessage {

    public static final String ID = "SELECT_SECRET_OBJECTIVE";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            controller.selectSecretObjective(virtualView.getGame().orElseThrow(PlayerNotInGameException::new).getId(), virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(), objective);
        } catch (GameNotFoundException e) {
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(virtualView.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(virtualView.getPlayerProfile().orElse(null)).getName()));
        } catch (InvalidObjectiveException e) {
            connection.send(new InvalidObjectiveSelectionS2C(objective));
        } catch (DoubleChoiceException e) {
            connection.send(new DoubleChoiceS2C());
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
