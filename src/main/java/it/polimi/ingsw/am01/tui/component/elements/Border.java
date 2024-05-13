package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Position;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.SizedPositioned;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class Border implements Component {
    private final BorderStyle style;
    private final Component child;

    public Border(BorderStyle style, Component child) {
        this.style = style;
        this.child = child;
    }

    @Override
    public Sized layout(Constraint constraint) {
        SizedPositioned child = this.child
                .layout(constraint.shrinkMax(2, 2))
                .placeAt(Position.of(1, 1));

        return new Sized(
                this,
                child.dimensions().grow(2, 2),
                List.of(child)
        );
    }

    @Override
    public void drawSelf(DrawArea a) {
        int w = a.dimensions().width() - 1;
        int h = a.dimensions().height() - 1;

        for (int x = 0; x < a.dimensions().width(); x++) {
            a.draw(x, 0, this.style.t());
        }

        for (int x = 0; x < a.dimensions().width(); x++) {
            a.draw(x, h, this.style.b());
        }

        for (int y = 0; y < a.dimensions().height(); y++) {
            a.draw(0, y, this.style.l());
        }

        for (int y = 0; y < a.dimensions().height(); y++) {
            a.draw(w, y, this.style.r());
        }

        a.draw(0, 0, this.style.tl());
        a.draw(w, 0, this.style.tr());
        a.draw(0, h, this.style.bl());
        a.draw(w, h, this.style.br());
    }
}
