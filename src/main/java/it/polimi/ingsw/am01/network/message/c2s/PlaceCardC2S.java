package it.polimi.ingsw.am01.network.message.c2s;

import it.polimi.ingsw.am01.controller.Controller;
import it.polimi.ingsw.am01.controller.VirtualView;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.exception.*;
import it.polimi.ingsw.am01.network.Connection;
import it.polimi.ingsw.am01.network.message.C2SNetworkMessage;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;
import it.polimi.ingsw.am01.network.message.s2c.*;

import java.util.Objects;

public record PlaceCardC2S(int cardId, Side side, int i, int j) implements C2SNetworkMessage {
    public static final String ID = "PLACE_CARD";

    @Override
    public void execute(Controller controller, Connection<S2CNetworkMessage, C2SNetworkMessage> connection, VirtualView virtualView) throws IllegalMoveException {
        try {
            controller.placeCard(virtualView.getGame().orElseThrow(PlayerNotInGameException::new).getId(), virtualView.getPlayerProfile().orElseThrow(NotAuthenticatedException::new).getName(),
                    cardId, side, i, j);
        } catch (GameNotFoundException e){
            connection.send(new GameNotFoundS2C(Objects.requireNonNull(virtualView.getGame().orElse(null)).getId()));
        } catch (PlayerNotInGameException e) {
            connection.send(new PlayerNotInGameS2C(Objects.requireNonNull(virtualView.getPlayerProfile().orElse(null)).getName()));
        } catch (IllegalGameStateException e) { //TODO: maybe remove
            connection.send(new InvalidGameStateS2C());
        } catch (IllegalPlacementException e) {
            connection.send(new InvalidPlacementS2C(cardId, side, i, j));
        } catch (InvalidCardException e) {
            connection.send(new InvalidCardS2C(cardId));
        }
    }

    @Override
    public String getId() {
        return ID;
    }
}
