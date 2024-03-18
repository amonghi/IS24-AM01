package it.polimi.ingsw.am01.model.card.face.corner;

import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.BaseCardFace;
import it.polimi.ingsw.am01.model.card.face.CardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;

/**
 * Corner positions.
 * They are used to specify a corner's position in a {@code CardFace}
 *
 * @see CardFace
 * @see BaseCardFace
 * @see FrontCardFace
 * @see BackCardFace
 */

public enum CornerPosition {
    TOP_RIGHT,
    TOP_LEFT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    public CornerPosition getOpposite() {
        return switch (this) {
            case TOP_RIGHT -> BOTTOM_LEFT;
            case TOP_LEFT -> BOTTOM_RIGHT;
            case BOTTOM_LEFT -> TOP_RIGHT;
            case BOTTOM_RIGHT -> TOP_LEFT;
        };
    }
}
