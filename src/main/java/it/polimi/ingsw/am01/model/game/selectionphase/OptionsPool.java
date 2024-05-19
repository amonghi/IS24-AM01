package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a pool of options where "choosers" can express a preference for one or multiple options.
 *
 * @param <O> the type of the options
 * @param <I> the type of whatever object represents the identity of the choosers who are choosing the options
 */
public class OptionsPool<O, I> {
    private final Map<O, Set<I>> choices;

    public OptionsPool(Set<O> options) {
        choices = options.stream()
                .collect(Collectors.toMap(Function.identity(), o -> new HashSet<>()));
    }

    /**
     * @return the options that are available in this pool
     */
    public Set<O> getOptions() {
        return Collections.unmodifiableSet(this.choices.keySet());
    }

    private Set<I> whoPrefers(O option) {
        Set<I> choosers = this.choices.get(option);
        if (choosers == null) {
            // FIME: use specific option
            throw new NoSuchElementException();
        }

        return choosers;
    }

    /**
     * Records that the given chooser has a preference for the given option.
     * If the chooser had already expressed such a preference, this method does nothing.
     *
     * @param option   an option among those returned by {@link #getOptions()}
     * @param identity the identity of the chooser
     * @throws NoSuchElementException if the given option is not among those returned by {@link #getOptions()}
     */
    public void addPreference(O option, I identity) {
        whoPrefers(option).add(identity);
    }

    /**
     * Records that a given chooser no longer has a preference for the given option.
     * If there was no preference to begin with, this method does nothing.
     *
     * @param option   an option among those returned by {@link #getOptions()}
     * @param identity the identity of the chooser
     * @throws NoSuchElementException if the given option is not among those returned by {@link #getOptions()}
     */
    public void removePreference(O option, I identity) {
        whoPrefers(option).remove(identity);
    }

    /**
     * @param option an option among those returned by {@link #getOptions()}
     * @return a set of all the choosers that have expressed a preference for the given option
     */
    public Set<I> getWhoPrefers(O option) {
        return Collections.unmodifiableSet(whoPrefers(option));
    }

    @Override
    public String toString() {
        return "OptionsPool{" +
                "choices=" + choices +
                '}';
    }
}
