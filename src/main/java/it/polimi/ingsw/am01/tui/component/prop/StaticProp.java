package it.polimi.ingsw.am01.tui.component.prop;

import it.polimi.ingsw.am01.tui.component.Component;

public class StaticProp<T> implements Prop<T> {
    private final T value;

    public StaticProp(T value) {
        this.value = value;
    }

    @Override
    public T get(Component watcher) {
        return this.value;
    }

    @Override
    public T peek() {
        return this.value;
    }
}
