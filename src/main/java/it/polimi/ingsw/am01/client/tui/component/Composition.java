package it.polimi.ingsw.am01.client.tui.component;

import it.polimi.ingsw.am01.client.tui.rendering.*;
import it.polimi.ingsw.am01.client.tui.rendering.draw.DrawArea;

import java.util.List;

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
        SizedPositioned layout = getRoot().layout(constraint).placeAt(Position.ZERO);
        return new Sized(this, layout.dimensions(), List.of(layout));
    }

    @Override
    public void drawSelf(RenderingContext ctx, DrawArea a) {
        getRoot().drawSelf(ctx, a);
    }
}
