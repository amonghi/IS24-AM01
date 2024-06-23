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
    TOP_RIGHT(true, false),
    TOP_LEFT(true, true),
    BOTTOM_LEFT(false, true),
    BOTTOM_RIGHT(false, false);

    private final boolean top;
    private final boolean left;

    CornerPosition(boolean top, boolean left) {
        this.top = top;
        this.left = left;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isLeft() {
        return left;
    }

    public CornerPosition getOpposite() {
        return switch (this) {
            case TOP_RIGHT -> BOTTOM_LEFT;
            case TOP_LEFT -> BOTTOM_RIGHT;
            case BOTTOM_LEFT -> TOP_RIGHT;
            case BOTTOM_RIGHT -> TOP_LEFT;
        };
    }
}
