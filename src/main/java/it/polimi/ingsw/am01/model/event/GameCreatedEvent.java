package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

import java.util.List;

public class GameCreatedEvent extends GameManagerEvent{
    public GameCreatedEvent(List<Game> gamesList) {
        super(gamesList);
    }
}
