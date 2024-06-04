package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.client.ClientState;
import it.polimi.ingsw.am01.model.game.GameStatus;

public record StateChangedEvent(ClientState state, GameStatus gameStatus) implements ViewEvent {
}
