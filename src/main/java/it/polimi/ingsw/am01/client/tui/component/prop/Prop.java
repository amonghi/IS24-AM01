package it.polimi.ingsw.am01.client.tui.component.prop;

import it.polimi.ingsw.am01.client.tui.component.Component;

import java.util.function.Function;

public interface Prop<T> {
    T get(Component watcher);

    T peek();

    default <U> Prop<U> map(Function<? super T, ? extends U> mapper) {
        return new MappedProp<>(this, mapper);
    }
}
