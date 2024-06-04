package it.polimi.ingsw.am01.client.gui.event;

import java.util.List;

public record PlayerListChangedEvent(List<String> playerList) implements ViewEvent {
}
