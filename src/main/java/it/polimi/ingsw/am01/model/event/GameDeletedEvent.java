package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.Game;

import java.util.List;

public class GameDeletedEvent extends GameManagerEvent{
    public GameDeletedEvent(List<Game> gamesList) {
        super(gamesList);
    }
}
