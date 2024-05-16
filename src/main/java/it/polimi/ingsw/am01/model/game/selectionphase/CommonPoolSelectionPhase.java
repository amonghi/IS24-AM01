package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.*;
import java.util.stream.Collectors;

public class CommonPoolSelectionPhase<O, I> extends SelectionPhase<O, I> {
    private final OptionsPool<O, I> commonPool;
    private final Map<I, Selector> selectors;
    private final boolean allowOverlap;

    public CommonPoolSelectionPhase(Set<I> identities, OptionsPool<O, I> commonPool, boolean allowPreferenceChange, boolean allowOverlap) {
        if (!allowPreferenceChange && !allowOverlap) {
            throw new IllegalArgumentException("If overlap is not allowed then it must be allowed to change the preference");
        }

        this.allowOverlap = allowOverlap;
        this.commonPool = commonPool;
        selectors = identities.stream()
                .map(i -> new Selector(i, commonPool, allowPreferenceChange))
                .collect(Collectors.toMap(Selector::getIdentity, selector -> selector));
    }

    // FIXME: duplicate code in DisjointSelectionPhase
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

        for (SelectionPhase<O, I>.Selector selector : this.selectors.values()) {
            Optional<O> preference = selector.getExpressedPreference();
            if (preference.isEmpty()) {
                // we cannot conclude if someone still hasn't chosen
                return Optional.empty();
            }

            Set<I> whoPrefers = commonPool.getWhoPrefers(preference.get());
            if (!allowOverlap && whoPrefers.size() > 1) {
                // we cannot conclude if there is an overlap
                return Optional.empty();
            }

            results.put(selector.getIdentity(), preference.get());
        }

        return Optional.of(results);
    }
}
