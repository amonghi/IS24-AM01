package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.component.Composition;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.Show;
import it.polimi.ingsw.am01.tui.component.prop.DynProp;
import it.polimi.ingsw.am01.tui.keyboard.Keyboard;

public class KeypressCounterComponent extends Composition {
    private final DynProp<Integer> count = new DynProp<>(0);

    public static ComponentBuilder builder() {
        return KeypressCounterComponent::new;
    }

    protected KeypressCounterComponent(BuildContext context) {
        super(context);

        Keyboard.getInstance().onAny(event -> context.getRenderer().runOnRenderThread(() -> {
            count.compute(i -> i + 1);
        }));
    }

    @Override
    protected ComponentBuilder compose() {
        return Border.around(Column.of(
                Text.of(this.count.map(i -> "You pressed " + i + " keys")),
                Show.when(count.map(i -> i > 10), Text.of("More than 10!"))
        ));
    }
}
