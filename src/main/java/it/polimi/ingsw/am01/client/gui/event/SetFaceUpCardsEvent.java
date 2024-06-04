package it.polimi.ingsw.am01.client.gui.event;

import java.util.List;

public record SetFaceUpCardsEvent(List<Integer> faceUpCards) implements ViewEvent {
}
