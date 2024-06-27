package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;

import static it.polimi.ingsw.am01.client.tui.component.elements.CardFaceComponent.CARD_H;
import static it.polimi.ingsw.am01.client.tui.component.elements.CardFaceComponent.CARD_W;

/**
 * A placeholder component that is displayed when the deck is empty instead of the top card.
 */
public class EmptyDeckPlaceholderComponent extends Element {
    /**
     * Creates a new empty deck placeholder component.
     */
    public EmptyDeckPlaceholderComponent() {
        super(Dimensions.of(CARD_W, CARD_H));
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        Position center = Position.of(CARD_W / 2, CARD_H / 2);

        a.foreground(GraphicalRenditionProperty.ForegroundColor.DEFAULT);
        Line.rectangle(a, Position.ZERO, a.dimensions(), Line.Style.ROUNDED);

        a.weight(GraphicalRenditionProperty.Weight.DIM);
        Text.writeCentered(a, center, "Empty deck");
    }
}
