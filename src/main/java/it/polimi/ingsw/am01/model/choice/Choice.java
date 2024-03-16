package it.polimi.ingsw.am01.model.choice;


import java.util.OptionalInt;
import java.util.Set;

public class Choice<T> {
    private Set<T> options;
    private OptionalInt selection;

    public Choice(Iterable<T> choices) {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<T> getOptions() {
        throw new UnsupportedOperationException("TODO");
    }

    public void select(T selection) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean hasSelected() {
        throw new UnsupportedOperationException("TODO");
    }

    public T getSelected() {
        throw new UnsupportedOperationException("TODO");
    }
}