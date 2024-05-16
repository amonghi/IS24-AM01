package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class SelectionPhase<O, I> {
    private Map<I, O> results;

    public SelectionPhase() {
        this.results = null;
    }

    public abstract Selector getSelectorFor(I who);

    abstract Optional<Map<I, O>> getResultsIfDone();

    private void updateState() {
        if (this.results != null) {
            return;
        }

        Optional<Map<I, O>> maybeResults = this.getResultsIfDone();
        maybeResults.ifPresent(results -> this.results = results);
    }

    public boolean isConcluded() {
        this.updateState();
        return this.results != null;
    }

    public Map<I, O> getResults() {
        this.updateState();
        if (this.results == null) {
            throw new IllegalStateException("The selection phase is not concluded");
        }

        return this.results;
    }

    public class Selector {
        private final I identity;
        private final OptionsPool<O, I> pool;
        private final boolean allowPreferenceChange;
        private O preference;

        public Selector(I identity, OptionsPool<O, I> pool, boolean allowPreferenceChange) {
            this.identity = identity;
            this.pool = pool;
            this.allowPreferenceChange = allowPreferenceChange;
        }

        public I getIdentity() {
            return identity;
        }

        public Set<O> getOptions() {
            return this.pool.getOptions();
        }

        public void expressPreference(O option) {
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
    }
}
