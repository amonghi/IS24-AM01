package it.polimi.ingsw.am01.tui.component.prop;

import it.polimi.ingsw.am01.tui.component.Component;

import java.util.function.Function;

public class MappedProp<T, U> implements Prop<U> {
    private final Prop<T> parent;
    private final Function<? super T, ? extends U> mapper;

    public MappedProp(Prop<T> parent, Function<? super T, ? extends U> mapper) {
        this.parent = parent;
        this.mapper = mapper;
    }

    @Override
    public U get(Component watcher) {
        return mapper.apply(parent.get(watcher));
    }

    @Override
    public U peek() {
        return mapper.apply(parent.peek());
    }
}
