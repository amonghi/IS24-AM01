package it.polimi.ingsw.am01.client.tui.component.elements;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.Constraint;
import it.polimi.ingsw.am01.client.tui.rendering.Dimensions;

/**
 * An element is a component that does not contain other components and has a fixed preferred size.
 */
public abstract class Element extends Component {
    private final Dimensions preferredDimensions;

    /**
     * Creates a new element with the given preferred dimensions.
     *
     * @param preferredDimensions the preferred dimensions
     */
    public Element(Dimensions preferredDimensions) {
        this.preferredDimensions = preferredDimensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void layout(Constraint constraint) {
        Dimensions d = this.preferredDimensions.constrain(constraint);
        this.setDimensions(d);
    }
}
