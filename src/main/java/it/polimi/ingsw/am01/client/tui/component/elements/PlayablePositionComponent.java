package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.Position;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.ansi.GraphicalRenditionProperty;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Line;
import it.polimi.ingsw.am01.client.tui.rendering.draw.Text;

public class PlayablePositionComponent extends Element {
    private final it.polimi.ingsw.am01.client.Position playablePosition;

    public PlayablePositionComponent(it.polimi.ingsw.am01.client.Position playablePosition) {
        super(Dimensions.of(CardFaceComponent.CARD_W, CardFaceComponent.CARD_H));
        this.playablePosition = playablePosition;
    }

    @Override
    public void draw(RenderingContext ctx, DrawArea a) {
        Dimensions d = a.dimensions();

        a.weight(GraphicalRenditionProperty.Weight.DIM);
        Line.rectangle(a, Position.ZERO, d, Line.Style.ROUNDED);
        Text.writeCentered(a, d.width() / 2, d.height() / 2, this.playablePosition.i() + " " + this.playablePosition.j());
    }
}
