package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.DrawSource;

public class CardDrawnFromEmptySourceEvent extends GameEvent {
    private final DrawSource drawSource;

    public CardDrawnFromEmptySourceEvent(DrawSource drawSource) {
        this.drawSource = drawSource;
    }

    public DrawSource getDrawSource() {
        return drawSource;
    }
}
