package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRendition;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

public class Text extends Element {
    private final GraphicalRendition rendition;
    private final String text;

    public Text(String text) {
        this(GraphicalRendition.DEFAULT, text);
    }

    public Text(GraphicalRendition rendition, String text) {
        super(Dimensions.of(text.length(), 1));
        this.rendition = rendition;
        this.text = text;
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        a.setRendition(this.rendition);

        for (int i = 0; i < this.text.length(); i++) {
            a.draw(i, 0, this.text.charAt(i));
        }
    }
}
