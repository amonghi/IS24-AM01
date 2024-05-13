package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.Component;
import it.polimi.ingsw.am01.tui.rendering.Constraint;
import it.polimi.ingsw.am01.tui.rendering.Sized;
import it.polimi.ingsw.am01.tui.rendering.draw.DrawArea;

public interface Composition extends Component {

    Component compose();

    @Override
    default Sized layout(Constraint constraint) {
        return compose().layout(constraint);
    }

    @Override
    default void drawSelf(DrawArea a) {
        compose().drawSelf(a);
    }
}
