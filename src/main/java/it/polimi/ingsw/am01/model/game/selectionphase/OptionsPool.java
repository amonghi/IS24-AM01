package it.polimi.ingsw.am01.model.game.selectionphase;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OptionsPool<O, I> {
    private final Map<O, Set<I>> choices;

    public OptionsPool(Set<O> options) {
        choices = options.stream()
                .collect(Collectors.toMap(Function.identity(), o -> new HashSet<>()));
    }

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

    public void addPreference(O option, I identity) {
        whoPrefers(option).add(identity);
    }

    public void removePreference(O option, I identity) {
        whoPrefers(option).remove(identity);
    }

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
