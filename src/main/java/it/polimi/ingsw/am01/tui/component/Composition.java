package it.polimi.ingsw.am01.tui.component;

import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.RenderingContext;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

public abstract class Composition extends Component {
    private Component root;

    protected Composition(BuildContext context) {
        super(context);
    }

    protected abstract ComponentBuilder compose();

    private Component getRoot() {
        if (root == null) {
            root = compose().build(this.getContext());
        }

        return root;
    }

    @Override
    public Sized layout(Constraint constraint) {
        return getRoot().layout(constraint);
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        getRoot().drawSelf(ctx, a);
    }
}
