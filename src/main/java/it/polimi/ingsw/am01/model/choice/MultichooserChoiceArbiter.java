package it.polimi.ingsw.am01.model.choice;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultichooserChoiceArbiter<T, I> {
    private Set<T> options;
    private Map<I, T> selections;
    private boolean settled;
    private List<MultichooserChoice<T, I>> choosers;

    public MultichooserChoiceArbiter(Iterable<T> choices, int choosers) {
        throw new UnsupportedOperationException("TODO");
    }

    public MultichooserChoice<T, I> getChoice(I identity) {
        throw new UnsupportedOperationException("TODO");
    }
}