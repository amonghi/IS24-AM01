package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.model.choice.MultiChoice;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.choice.Choice;

import java.util.List;

public class PlayerData {
    private List<Card> hand;
    private Choice<Side> startingCardSideChoice;
    private Choice<Objective> objectiveChoice;
    private MultiChoice<PlayerColor, PlayerProfile> colorChoice;

    public PlayerData(Card starterCard, Choice<Side> startingCardSideChoice, Choice<Objective> objectiveChoice, MultiChoice<PlayerColor, PlayerProfile> colorChoice) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Card> getHand() {
        throw new UnsupportedOperationException("TODO");
    }

    public Choice<Objective> getObjectiveChoice() {
        throw new UnsupportedOperationException("TODO");
    }

    public MultiChoice<PlayerColor, PlayerProfile> getColorChoice() {
        throw new UnsupportedOperationException("TODO");
    }
}
