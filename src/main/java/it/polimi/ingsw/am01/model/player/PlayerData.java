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
public record PlayerData(List<Card> hand, Objective objective, PlayerColor color) {
}