package it.polimi.ingsw.am01.model.collectible;

import it.polimi.ingsw.am01.model.card.CardColor;

/**
 * A resource.
 * Resources can be collected to allow placement of certain cards.
 *
 * @see it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint
 */
public enum Resource implements Collectible {
    PLANT(CardColor.GREEN),
    FUNGI(CardColor.RED),
    ANIMAL(CardColor.BLUE),
    INSECT(CardColor.PURPLE);

    private final CardColor color;

    Resource(CardColor color) {
        this.color = color;
    }

    /**
     * Each resource has an associated color.
     *
     * @return the color associated with this resource.
     * @see CardColor
     */
    public CardColor getAssociatedColor() {
        return this.color;
    }
}
