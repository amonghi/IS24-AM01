package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.eventemitter.Event;
import it.polimi.ingsw.am01.model.game.Game;

import java.util.Collections;
import java.util.List;

public class GameManagerEvent implements Event {
    private final List<Game> gamesList;

    public GameManagerEvent(List<Game> gamesList) {
        this.gamesList = gamesList;
    }

    public List<Game> getGamesList() {
        return Collections.unmodifiableList(gamesList);
    }

}
