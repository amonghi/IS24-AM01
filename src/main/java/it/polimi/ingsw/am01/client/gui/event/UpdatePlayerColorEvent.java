package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.model.player.PlayerColor;

public record UpdatePlayerColorEvent(String playerName, PlayerColor playerColor) implements ViewEvent {
}
