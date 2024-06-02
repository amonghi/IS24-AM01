package it.polimi.ingsw.am01.tui.component.layout;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.component.prop.Prop;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Dimensions;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

import java.util.List;

public class Show extends Component {
    public static ComponentBuilder when(Prop<Boolean> condition, ComponentBuilder child) {
        return ctx -> new Show(ctx, condition, child.build(ctx));
    }

    private final Prop<Boolean> condition;
    private final Component child;

    public Show(BuildContext ctx, Prop<Boolean> condition, Component child) {
        super(ctx);
        this.condition = condition;
        this.child = child;
    }

    @Override
    public Sized layout(Constraint constraint) {
        return this.condition.get(this)
                ? child.layout(constraint)
                : new Sized(this, Dimensions.ZERO.constrain(constraint), List.of());
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        if (this.condition.get(this)) {
            child.drawSelf(ctx, a);
        }
    }
}
