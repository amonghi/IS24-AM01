package it.polimi.ingsw.am01.client.tui.component.layout;

import it.polimi.ingsw.am01.client.tui.component.Component;
import it.polimi.ingsw.am01.client.tui.rendering.*;

import java.util.ArrayList;
import java.util.List;

public class Column extends LayoutComponent {
    private final List<Component> children;

    public Column(List<Component> children) {
        this.children = children;
    }

    @Override
    public Sized layout(Constraint constraint) {
        int filledH = 0;

        List<SizedPositioned> sizedPositionedChildren = new ArrayList<>();

        for (Component child : this.children) {
            Sized layout = child.layout(constraint.shrinkMax(0, filledH));

            Position placementPosition = Position.of(0, filledH);
            sizedPositionedChildren.add(layout.placeAt(placementPosition));

            filledH += layout.dimensions().height();
        }

        int w = sizedPositionedChildren.stream()
                .mapToInt(value -> value.dimensions().width())
                .max()
                .orElse(0);

        return new Sized(this, Dimensions.constrained(constraint, w, filledH), sizedPositionedChildren);
    }
}
