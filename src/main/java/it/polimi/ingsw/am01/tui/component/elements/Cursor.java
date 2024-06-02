package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

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
