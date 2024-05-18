package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements a phase of the game in which the players have to make a choice among various options.
 * In the documentation, the players are referred to as "choosers".
 * <p>
 * This class uses a {@link SelectionPhaseStrategy}
 * to decide which options are available (and to what choosers)
 * and also to determine the condition that concludes this selection phase.
 * <p>
 * Each chooser should:
 * <ol>
 *     <li>get a {@link Selector} through {@link #getSelectorFor(Object)}</li>
 *     <li>either:</li>
 *     <ul>
 *         <li>express a preference by calling {@link Selector#expressPreference(Object)}</li>
 *         <li>drop out by calling {@link Selector#dropOut()}</li>
 *     </ul>
 * </ol>
 * <p>
 * If all the (not dropped out) choosers express a valid choice according to the {@link SelectionPhaseStrategy}
 * then this selection phase concludes ({@link #isConcluded()} will be {@code true})
 * and the results of the selection will be available in {@link #getResults()}.
 *
 * @param <O> the type of the options.
 * @param <I> the type that represents the identity of the players, aka the "choosers".
 */
public class SelectionPhase<O, I> {
    private final SelectionPhaseStrategy<O, I> strategy;
    private final Map<I, Selector> selectors;
    private Map<I, O> results;

    /**
     * @param strategy what strategy to use to provide options to the choosers and also to determine when to conclude the phase.
     */
    public SelectionPhase(SelectionPhaseStrategy<O, I> strategy) {
        this.strategy = strategy;
        this.selectors = strategy.createSelectors(this).stream()
                .collect(Collectors.toMap(Selector::getIdentity, Function.identity()));
        this.results = null;
    }

    /**
     * Retrieves a {@link Selector} for the given chooser.
     *
     * @param who the identity of a chooser
     * @return a {@link Selector} for the given choosers.
     * @throws NoSuchElementException if the given identity is not among the choosers that have been provided by the {@link SelectionPhaseStrategy}
     */
    public Selector getSelectorFor(I who) {
        Selector selector = this.selectors.get(who);
        if (selector == null) {
            // FIXME
            throw new NoSuchElementException();
        }

        return selector;
    }

    private void updateState() {
        if (this.results != null) {
            return;
        }

        this.strategy.tryConclude(List.copyOf(this.selectors.values()))
                .ifPresent(results -> this.results = results);
    }

    /**
     * Return whether this SelectionPhase has concluded or not.
     * <p>
     * Once the selection phase has concluded, this condition cannot change.
     * Once the selection phase has concluded, {@link #getResults()} can be called.
     *
     * @return true iff this SelectionPhase has concluded.
     */
    public boolean isConcluded() {
        this.updateState();
        return this.results != null;
    }

    /**
     * @return a map representing the selection that every chooser has made
     * @throws IllegalStateException iff called before this phase has concluded.
     * @see #isConcluded()
     */
    public Map<I, O> getResults() {
        this.updateState();
        if (this.results == null) {
            throw new IllegalStateException("The selection phase is not concluded");
        }

        return this.results;
    }

    /**
     * A Selector is an object through which a chooser can express a preference.
     */
    public class Selector {
        private final I identity;
        private final OptionsPool<O, I> pool;
        private final boolean allowPreferenceChange;
        private boolean droppedOut;
        private O preference;

        Selector(I identity, OptionsPool<O, I> pool, boolean allowPreferenceChange) {
            this.identity = identity;
            this.pool = pool;
            this.allowPreferenceChange = allowPreferenceChange;
            this.droppedOut = false;
            this.preference = null;
        }

        /**
         * @return the chooser identity associated with this selector
         */
        public I getIdentity() {
            return identity;
        }

        /**
         * @return the options available to this selector
         */
        public Set<O> getOptions() {
            return this.pool.getOptions();
        }

        /**
         * Expresses a preference for a given option.
         * <p>
         * If the {@link SelectionPhaseStrategy} that created this Selector allows
         * changing preference calling this method twice will remove the previous preference
         * and register a new one.
         * If changing the preference is not allowed, calling this method a second time will throw an exception.
         *
         * @param option the option in favor of which the chooser wishes to express a preference.
         * @throws IllegalStateException  if the {@link SelectionPhase} which owns this Selector has already concluded.
         * @throws IllegalStateException  when calling a second time and the {@link SelectionPhaseStrategy} does not allow this.
         * @throws NoSuchElementException if the given option is not among those returned by {@link #getOptions()}
         */
        public void expressPreference(O option) {
            if (this.droppedOut) {
                throw new IllegalStateException("This selector is not longer valid as the chooser associated with it has dropped out");
            }

            if (isConcluded()) {
                // FIXME: specific exception
                throw new IllegalStateException("The selection phase is concluded");
            }

            if (!this.allowPreferenceChange && this.preference != null) {
                throw new IllegalStateException("Changing preference is not allowed");
            }

            if (this.preference != null) {
                this.pool.removePreference(this.preference, this.identity);
            }
            this.pool.addPreference(option, this.identity);

            // this is after the call to expressPreference() on purpose
            // because if the chosen option is invalid, it will throw, and this.preference won't get set
            this.preference = option;
            SelectionPhase.this.updateState();
        }

        public Optional<O> getExpressedPreference() {
            return Optional.ofNullable(this.preference);
        }

        /**
         * Removes the chooser associated with this selector.
         * Once this method has been called, this Selector can no longer be used as the chooser associated with
         * it is no longer part of the selection phase.
         */
        public void dropOut() {
            this.droppedOut = true;
            SelectionPhase.this.selectors.remove(this.identity);
            SelectionPhase.this.updateState();
        }
    }
}
