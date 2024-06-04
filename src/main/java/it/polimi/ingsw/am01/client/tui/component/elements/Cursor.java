package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.client.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.client.tui.rendering.Sized;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

public class Cursor extends Component {
    public static ComponentBuilder here() {
        return Cursor::new;
    }

    public Cursor(BuildContext ctx) {
        super(ctx);
    }

    @Override
    public Sized layout(Constraint constraint) {
        Dimensions d = Dimensions.constrained(constraint, 0, 1);
        return new Sized(this, d, List.of());
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        ctx.global().setCursorPosition(ctx.local().getOffset());
    }
}
