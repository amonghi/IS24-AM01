package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.card.Card;

import java.util.List;

public class PlayerData {
    private final List<Card> hand;
    private final Objective objective;
    private final PlayerColor color;

    public PlayerData(List<Card> hand, Objective objectiveChoice, PlayerColor colorChoice) {
        this.hand = hand;
        this.objective = objectiveChoice;
        this.color = colorChoice;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Objective getObjectiveChoice() {
        return objective;
    }

    public PlayerColor getColorChoice() {
        return color;
    }
}
