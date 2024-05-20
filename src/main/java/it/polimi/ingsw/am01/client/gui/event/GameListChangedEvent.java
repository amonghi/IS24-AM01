package it.polimi.ingsw.am01.client.gui.event;

import it.polimi.ingsw.am01.network.message.s2c.UpdateGameListS2C;

import java.util.Map;

public record GameListChangedEvent(Map<Integer, UpdateGameListS2C.GameStat> gameStatMap) implements ViewEvent {
}
