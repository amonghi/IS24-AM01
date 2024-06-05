package it.polimi.ingsw.am01.client.tui.component.layout.flex;

import it.polimi.ingsw.am01.client.tui.component.BuildContext;
import it.polimi.ingsw.am01.client.tui.component.ComponentBuilder;

public sealed interface FlexChild {
    ComponentFlexChild build(BuildContext parent);

    record Fixed(ComponentBuilder child) implements FlexChild {

        @Override
        public ComponentFlexChild build(BuildContext ctx) {
            return new ComponentFlexChild.Fixed(child().build(ctx));
        }
    }

    record Flexible(int growFactor, ComponentBuilder child) implements FlexChild {
        @Override
        public ComponentFlexChild build(BuildContext ctx) {
            return new ComponentFlexChild.Flexible(growFactor, child().build(ctx));
        }
    }
}
