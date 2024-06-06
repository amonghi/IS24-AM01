package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.Sized;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

public class Text implements Component {
    private final GraphicalRendition rendition;
    private final String text;

    public Text(String text) {
        this(GraphicalRendition.DEFAULT, text);
    }

    public Text(GraphicalRendition rendition, String text) {
        this.rendition = rendition;
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
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        a.setRendition(this.rendition);

        for (int i = 0; i < this.text.length(); i++) {
            a.draw(i, 0, this.text.charAt(i));
        }
    }
}
