package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;

public abstract class Element extends Component {
    private final Dimensions preferredDimensions;

    public Element(Dimensions preferredDimensions) {
        this.preferredDimensions = preferredDimensions;
    }

    @Override
    public void layout(Constraint constraint) {
        Dimensions d = this.preferredDimensions.constrain(constraint);
        this.setDimensions(d);
    }
}
