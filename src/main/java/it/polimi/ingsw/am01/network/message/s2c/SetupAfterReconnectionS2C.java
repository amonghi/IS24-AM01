package it.polimi.ingsw.am01.network.message.s2c;

import it.polimi.ingsw.am01.model.card.CardColor;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.MessageType;
import it.polimi.ingsw.am01.model.game.GameStatus;
import it.polimi.ingsw.am01.model.game.PlayArea;
import it.polimi.ingsw.am01.model.game.TurnPhase;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.network.message.S2CNetworkMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This message informs the reconnected player of all necessary game data (the current game's status).
 * @param playAreas a {@link Map} that contains {@code PlayArea}s of players
 * @param currentPlayer the current player (who has to play)
 * @param turnPhase the current turn phase
 * @param status the current game's status
 * @param hand the list of player's cards
 * @param playerColors a {@link Map} that contains all players' colors
 * @param secretObjective the id of the player's secret objective
 * @param commonObjectives the list of common objective
 * @param resourceDeckColor the visible color of resource deck
 * @param goldenDeckColor the visible color of golden deck
 * @param faceUpCards the list of faceup cards' ids
 * @param connections a {@link Map} that contains players' connection flags
 * @param chat a list of chat messages related to the player
 * @see TurnPhase
 * @see GameStatus
 * @see it.polimi.ingsw.am01.model.game.PlayArea.Position
 * @see it.polimi.ingsw.am01.model.event.PlayerReconnectedEvent
 * @see it.polimi.ingsw.am01.model.game.Game
 */
public record SetupAfterReconnectionS2C(
        Map<String, Map<PlayArea.Position, CardPlacement>> playAreas,
        String currentPlayer,
        TurnPhase turnPhase,
        GameStatus status,
        List<Integer> hand,
        Map<String, PlayerColor> playerColors,
        int secretObjective,
        List<Integer> commonObjectives,
        CardColor resourceDeckColor,
        CardColor goldenDeckColor,
        List<Integer> faceUpCards,
        Map<String, Boolean> connections,
        List<Message> chat
) implements S2CNetworkMessage {
    public final static String ID = "SETUP_AFTER_RECONNECTION";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }

    /**
     * This record represents a card's placement.
     * @param cardId the id of the card placed
     * @param side the side of the card placed
     * @param seq the sequence number of the card placed
     * @param points points earned from card's placement
     * @see it.polimi.ingsw.am01.model.game.PlayArea.CardPlacement
     */
    public record CardPlacement(int cardId, Side side, int seq, int points) implements Serializable {
    }

    /**
     * This record represents a chat message.
     * If message's type is {@code BROADCAST}, recipient will be set to {@code null}.
     * @param messageType the type of the message
     * @param sender the sender of the message
     * @param recipient the recipient of the message if present, otherwise {@code null}
     * @param content the content of the message
     * @param timestamp the timestamp of the message
     * @see it.polimi.ingsw.am01.model.chat.Message
     * @see MessageType
     */
    public record Message(MessageType messageType, String sender, String recipient, String content,
                          String timestamp) implements Serializable {
    }
}
