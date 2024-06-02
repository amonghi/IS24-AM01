package it.polimi.ingsw.am01.tui.component.elements;

import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.tui.component.BuildContext;
import it.polimi.ingsw.am01.tui.component.ComponentBuilder;
import it.polimi.ingsw.am01.tui.component.Composition;
import it.polimi.ingsw.am01.tui.component.layout.Column;
import it.polimi.ingsw.am01.tui.component.layout.Show;
import it.polimi.ingsw.am01.tui.component.prop.DynProp;
import it.polimi.ingsw.am01.tui.keyboard.Keyboard;
import it.polimi.ingsw.am01.tui.rendering.Renderer;

public class KeypressCounterComponent extends Composition {
    private final DynProp<Integer> count = new DynProp<>(0);
    private final Renderer renderer;
    private EventEmitter.Registration registration;

    public static ComponentBuilder builder() {
        return KeypressCounterComponent::new;
    }

    protected KeypressCounterComponent(BuildContext context) {
        super(context);
        this.renderer = context.getRenderer();
    }

    @Override
    public void onScreen() {
        Keyboard keyboard = Keyboard.getInstance();

        this.registration = keyboard.onAny(event -> renderer.runOnRenderThread(() -> {
            count.compute(i -> i + 1);
        }));
    }

    @Override
    public void offScreen() {
        if (registration != null) {
            Keyboard.getInstance().unregister(registration);
        }
    }

    @Override
    protected ComponentBuilder compose() {
        return Border.around(Column.of(
                Text.of(this.count.map(i -> "You pressed " + i + " keys")),
                Show.when(count.map(i -> i > 10), Text.of("More than 10!"))
        ));
    }
}
