package it.polimi.ingsw.am01.model.collectible;

/**
 * An item.
 * Items can be collected to earn points when certain cards are placed on the {@code PlayArea}.
 *
 * @see it.polimi.ingsw.am01.model.card.face.points.ItemPoints
 * @see it.polimi.ingsw.am01.model.game.PlayArea
 */
public enum Item implements Collectible {
    QUILL,
    INKWELL,
    MANUSCRIPT
}
