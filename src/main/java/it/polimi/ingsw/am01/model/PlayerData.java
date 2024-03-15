package it.polimi.ingsw.am01.model;

import java.util.List;

public class PlayerData {
    private List<Card> hand;
    private Choice<Side> startingCardSideChoice;
    private Choice<Objective> objectiveChoice;
    private MultichooserChoice<PlayerColor, PlayerProfile> colorChoice;

    public PlayerData(Card starterCard, Choice<Side> startingCardSideChoice, Choice<Objective> objectiveChoice, MultichooserChoice<PlayerColor, PlayerProfile> colorChoice) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Card> getHand() {
        throw new UnsupportedOperationException("TODO");
    }

    public Choice<Objective> getObjectiveChoice() {
        throw new UnsupportedOperationException("TODO");
    }

    public MultichooserChoice<PlayerColor, PlayerProfile> getColorChoice() {
        throw new UnsupportedOperationException("TODO");
    }
}
