package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class Rectangle implements Component {
    private final int preferredWidth;
    private final int preferredHeight;
    private final char character;

    public Rectangle(int preferredWidth, int preferredHeight, char character) {
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
        this.character = character;
    }

    @Override
    public Sized layout(Constraint constraint) {
        return new Sized(
                this,
                Dimensions.constrained(constraint, preferredWidth, preferredHeight),
                List.of()
        );
    }

    @Override
    public void drawSelf(DrawArea a) {
        for (int x = 0; x < a.dimensions().width(); x++) {
            for (int y = 0; y < a.dimensions().height(); y++) {
                a.draw(x, y, this.character);
            }
        }
    }
}
