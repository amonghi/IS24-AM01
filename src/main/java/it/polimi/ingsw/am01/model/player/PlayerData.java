package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.card.Card;

import java.util.List;

public class PlayerData {
    private final List<Card> hand;
    private final Objective objective;
    private final PlayerColor color;

    public PlayerData(Card starterCard, Objective objectiveChoice, PlayerColor colorChoice) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Card> getHand() {
        throw new UnsupportedOperationException("TODO");
    }

    public Objective getObjectiveChoice() {
        throw new UnsupportedOperationException("TODO");
    }

    public PlayerColor getColorChoice() {
        throw new UnsupportedOperationException("TODO");
    }
}
