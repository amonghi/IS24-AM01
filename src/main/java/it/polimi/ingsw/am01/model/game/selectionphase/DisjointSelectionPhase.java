package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class DisjointSelectionPhase<O, I> extends SelectionPhase<O, I> {
    private final Map<I, Selector> selectors;

    public DisjointSelectionPhase(Map<I, OptionsPool<O, I>> assignedPools) {
        this.selectors = assignedPools.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new Selector(entry.getKey(), entry.getValue(), false)
                ));
    }

    @Override
    public SelectionPhase<O, I>.Selector getSelectorFor(I who) {
        SelectionPhase<O, I>.Selector selector = this.selectors.get(who);
        if (selector == null) {
            // FIXME
            throw new NoSuchElementException();
        }

        return selector;
    }

    @Override
    Optional<Map<I, O>> getResultsIfDone() {
        Map<I, O> results = new HashMap<>();

        for (Selector selector : this.selectors.values()) {
            Optional<O> preference = selector.getExpressedPreference();
            if (preference.isEmpty()) {
                // we cannot conclude if someone still hasn't chosen
                return Optional.empty();
            }

            results.put(selector.getIdentity(), preference.get());
        }

        return Optional.of(results);
    }
}
