package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class Text implements Component {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public Sized layout(Constraint constraint) {
        return new Sized(
                this,
                Dimensions.constrained(constraint, this.text.length(), 1),
                List.of()
        );
    }

    @Override
    public void drawSelf(DrawArea a) {
        for (int i = 0; i < this.text.length(); i++) {
            a.draw(i, 0, this.text.charAt(i));
        }
    }
}
