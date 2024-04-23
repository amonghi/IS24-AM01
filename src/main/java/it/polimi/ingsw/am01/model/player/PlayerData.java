package it.polimi.ingsw.am01.model.player;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.objective.Objective;

import java.util.List;

/**
 * PlayerData represents all player data related to the game. In a {@code Game}, it is associated with a {@link PlayerProfile}
 *
 * @see it.polimi.ingsw.am01.model.game.Game
 * @see it.polimi.ingsw.am01.model.player.PlayerProfile
 */
public class PlayerData {
    private final List<Card> hand;
    private final Objective objective;
    private final PlayerColor color;

    /**
     * Constructs a new {@code PlayerData} and set hand, objective choice and color choice
     * @param hand a list that contains player's {@link Card} (resource and golden cards)
     * @param objectiveChoice the secret {@link Objective} of player
     * @param colorChoice the color of player
     * @see it.polimi.ingsw.am01.model.objective.Objective
     * @see it.polimi.ingsw.am01.model.card.Card
     */
    public PlayerData(List<Card> hand, Objective objectiveChoice, PlayerColor colorChoice) {
        this.hand = hand;
        this.objective = objectiveChoice;
        this.color = colorChoice;
    }

    /**
     *
     * @return a list that contains player's {@link Card}
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     *
     * @return the secret {@link Objective} of player
     */
    public Objective getObjectiveChoice() {
        return objective;
    }

    /**
     *
     * @return the {@link PlayerColor} of player
     */
    public PlayerColor getColorChoice() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PlayerData{" +
                "hand=" + hand +
                ", objective=" + objective +
                ", color=" + color +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerData that = (PlayerData) o;
        return hand.equals(that.hand) && objective.equals(that.objective) && color == that.color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = hand.hashCode();
        result = 31 * result + objective.hashCode();
        result = 31 * result + color.hashCode();
        return result;
    }
}