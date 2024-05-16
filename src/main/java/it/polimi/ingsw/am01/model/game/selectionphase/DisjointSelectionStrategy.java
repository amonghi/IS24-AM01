package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * With this strategy, every chooser chooses from a different pool.
 * Changing a preference is not allowed.
 */
public class DisjointSelectionStrategy<O, I> implements SelectionPhaseStrategy<O, I> {
    private final Map<I, OptionsPool<O, I>> assignedPools;

    /**
     * @param assignedPools a map that associates each chooser with the pool from which it will choose
     */
    public DisjointSelectionStrategy(Map<I, OptionsPool<O, I>> assignedPools) {
        this.assignedPools = assignedPools;
    }

    @Override
    public List<SelectionPhase<O, I>.Selector> createSelectors(SelectionPhase<O, I> selectionPhase) {
        return assignedPools.entrySet().stream()
                .map(entry -> selectionPhase.new Selector(entry.getKey(), entry.getValue(), false))
                .toList();
    }

    @Override
    public Optional<Map<I, O>> tryConclude(List<SelectionPhase<O, I>.Selector> selectors) {
        Map<I, O> results = new HashMap<>();

        for (SelectionPhase<O, I>.Selector selector : selectors) {
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
