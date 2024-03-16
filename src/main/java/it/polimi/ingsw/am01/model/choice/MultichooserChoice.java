package it.polimi.ingsw.am01.model.choice;


import java.util.List;
import java.util.Set;

public class MultichooserChoice<T, I> {
    private MultichooserChoiceArbiter parent;
    private I identity;

    public MultichooserChoice(MultichooserChoiceArbiter parent, I identity) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<T> getOptions() {
        throw new UnsupportedOperationException("TODO");
    }

    public SelectionResult select(T choice) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean hasSelected() {
        throw new UnsupportedOperationException("TODO");
    }

    public T getSelected() {
        throw new UnsupportedOperationException("TODO");
    }

    public Set<I> getContenders(T choice) {
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isSettled() {
        throw new UnsupportedOperationException("TODO");
    }
}