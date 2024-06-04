package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.card.Side;

public record UpdatePlayAreaEvent(String playerName, int i, int j, int cardId, Side side, int seq, int  points) implements ViewEvent {
}
