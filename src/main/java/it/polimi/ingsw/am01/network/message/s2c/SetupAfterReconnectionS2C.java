package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record SetupAfterReconnectionS2C(
        Map<String, Map<PlayArea.Position, CardPlacement>> playAreas,
        String currentPlayer,
        TurnPhase turnPhase,
        GameStatus status,
        List<Integer> hand,
        Map<String, PlayerColor> playerColors,
        int secretObjective,
        List<Integer> commonObjectives,
        boolean resourceCardDeckIsEmpty,
        boolean goldenCardDeckIsEmpty,
        List<Integer> faceUpCards,
        Map<String, Boolean> connections
) implements S2CNetworkMessage {
    public final static String ID = "SETUP_AFTER_RECONNECTION";

    @Override
    public String getId() {
        return ID;
    }


    public record CardPlacement(int cardId, Side side, int seq, int points) implements Serializable {
    }

}
