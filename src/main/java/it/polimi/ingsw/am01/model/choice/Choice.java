package it.polimi.ingsw.am01.model.choice;


import java.util.*;

/**
 * A choice between multiple options.
 * After the choice has been made (that is, an option has been selected) it cannot be changed.
 *
 * @param <T> the type of options that this choice offers
 */
public class Choice<T> {
    private final Set<T> options;
    private T selection;

    /**
     * Constructs a new choice, given the available options.
     *
     * @param options the options that will be available for this choice
     */
    public Choice(Set<T> options) {
        this.options = Collections.unmodifiableSet(options);
        this.selection = null;
    }

    /**
     * @return all the options that are available in this {@code Choice}
     */
    public Set<T> getOptions() {
        return this.options;
    }

    /**
     * Selects one of the options from this {@code Choice}
     *
     * @param selection the selected option. Must be one of the options from {@code Choice.getOptions()}
     */
    public void select(T selection) {
        if (this.selection != null) {
            throw new DoubleChoiceException();
        }

        if (!this.options.contains(selection)) {
            throw new NoSuchElementException("No such option in this choice");
        }

        this.selection = selection;
    }

    /**
     * @return whether the element for this choice has been selected
     */
    public boolean hasSelected() {
        return this.selection != null;
    }

    /**
     * @return the selected element
     * @throws NoSuchElementException if an element hasn't been selected yet
     */
    public T getSelected() {
        if (this.selection == null) {
            throw new NoSuchElementException();
        }

        return this.selection;
    }
}
