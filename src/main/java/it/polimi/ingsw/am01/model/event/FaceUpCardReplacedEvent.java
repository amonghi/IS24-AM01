package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.FaceUpCard;

/**
 * This event is emitted after a user has drawn a {@link FaceUpCard}.
 * @param faceUpCards the new {@link FaceUpCard}
 * @see FaceUpCard#draw()
 */
public record FaceUpCardReplacedEvent(FaceUpCard faceUpCards) implements GameEvent {
}
