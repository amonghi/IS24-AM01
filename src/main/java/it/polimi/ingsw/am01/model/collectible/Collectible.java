package it.polimi.ingsw.am01.model.collectible;

/**
 * Something that the player can collect.
 * Can be collected by placing {@code Card}s in the {@code PlayArea}
 * Can be collected to earn points by completing {@code Objective}s.
 *
 * @see it.polimi.ingsw.am01.model.card.Card
 * @see it.polimi.ingsw.am01.model.card.face.corner.Corner
 * @see it.polimi.ingsw.am01.model.game.PlayArea
 * @see it.polimi.ingsw.am01.model.objective.SameCollectibleObjective
 * @see it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective
 */
public sealed interface Collectible permits Item, Resource {
}
