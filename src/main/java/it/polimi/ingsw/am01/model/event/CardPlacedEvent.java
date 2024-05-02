package it.polimi.ingsw.am01.model.event;

import it.polimi.ingsw.am01.model.game.PlayArea;

public class CardPlacedEvent extends GameEvent {
    private final String playerName;
    private final PlayArea.CardPlacement cardPlacement;

    public CardPlacedEvent(String playerName, PlayArea.CardPlacement cardPlacement) {
        this.playerName = playerName;
        this.cardPlacement = cardPlacement;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayArea.CardPlacement getCardPlacement() {
        return cardPlacement;
    }
}
