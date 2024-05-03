package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.FaceUpCard;

public class FaceUpCardReplacedEvent extends GameEvent {
    private final FaceUpCard faceUpCards;

    public FaceUpCardReplacedEvent(FaceUpCard faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    public FaceUpCard getFaceUpCards() {
        return faceUpCards;
    }
}
