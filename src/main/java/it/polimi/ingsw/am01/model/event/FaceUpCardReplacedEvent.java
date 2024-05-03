package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.FaceUpCard;

public record FaceUpCardReplacedEvent(FaceUpCard faceUpCards) implements GameEvent {
}
