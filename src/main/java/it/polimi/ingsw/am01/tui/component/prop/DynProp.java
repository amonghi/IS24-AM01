package it.polimi.ingsw.am01.tui.component.prop;

import it.polimi.ingsw.am01.tui.component.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class DynProp<T> implements Prop<T> {
    private Set<Component> watchers;
    private T value;

    public DynProp(T initialValue) {
        this.watchers = new HashSet<>();
        this.value = initialValue;
    }

    public void set(T value) {
        this.value = value;
        this.updateWatchers();
    }

    public void compute(Function<? super T, ? extends T> function) {
        this.value = function.apply(this.value);
        this.updateWatchers();
    }

    public void mutate(Consumer<T> updater) {
        updater.accept(this.value);
        this.updateWatchers();
    }

    private void updateWatchers() {
        Set<Component> watchers = this.watchers;

        // clear the watchers, they will re-subscribe when the render function is run again
        this.watchers = new HashSet<>();

        watchers.forEach(Component::update);
    }

    public T get(Component watcher) {
        this.watchers.add(watcher);
        return this.value;
    }

    @Override
    public T peek() {
        return this.value;
    }
}
