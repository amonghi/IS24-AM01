package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.*;

/**
 * With this strategy, all choosers choose from the same pool.
 * This strategy can be configured to allow or disallow
 * <ul>
 *     <li>changing preferences</li>
 *     <li>expressing overlapping preferences</li>
 * </ul>
 */
public class CommonPoolSelectionStrategy<O, I> implements SelectionPhaseStrategy<O, I> {
    private final OptionsPool<O, I> commonPool;
    private final boolean allowOverlap;
    private final boolean allowPreferenceChange;
    private final Set<I> identities;

    /**
     * @param identities            the identities of the choosers
     * @param commonPool            the pool from which the choosers will choose
     * @param allowPreferenceChange whether choosers are allowed to change their preference
     * @param allowOverlap          whether the {@link SelectionPhase} will conclude even if some preferences are overlapping
     * @throws IllegalArgumentException if changing preferences is not allowed and also overlaps are not allowed.
     *                                  This is because such a configuration could lead to situations where the selection phase will never conclude.
     */
    public CommonPoolSelectionStrategy(Set<I> identities, OptionsPool<O, I> commonPool, boolean allowPreferenceChange, boolean allowOverlap) {
        if (!allowPreferenceChange && !allowOverlap) {
            throw new IllegalArgumentException("If overlap is not allowed then it must be allowed to change the preference");
        }

        this.identities = identities;
        this.commonPool = commonPool;
        this.allowOverlap = allowOverlap;
        this.allowPreferenceChange = allowPreferenceChange;
    }

    @Override
    public List<SelectionPhase<O, I>.Selector> createSelectors(SelectionPhase<O, I> selectionPhase) {
        return identities.stream()
                .map(i -> selectionPhase.new Selector(i, commonPool, this.allowPreferenceChange))
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
