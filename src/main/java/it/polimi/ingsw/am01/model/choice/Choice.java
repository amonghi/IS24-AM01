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
        this.options = new HashSet<>(options);
        this.selection = null;
    }

    /**
     * @return all the options that are available in this {@code Choice}
     */
    public Set<T> getOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    /**
     * Selects one of the options from this {@code Choice}
     *
     * @param selection the selected option. Must be one of the options from {@code Choice.getOptions()}
     * @throws DoubleChoiceException  if the user tries to choose more than once
     * @throws NoSuchElementException if the selected option is not one of those returned by {@code getOptions()}
     */
    public void select(T selection) throws DoubleChoiceException, NoSuchElementException {
        if (this.selection != null) {
            throw new DoubleChoiceException();
        }

        if (!this.options.contains(selection)) {
            throw new NoSuchElementException("No such option in this choice");
        }

        this.selection = selection;
    }

    /**
     * @return the selected item, or empty a selection has not been made yet.
     */
    public Optional<T> getSelected() {
        return Optional.ofNullable(this.selection);
    }
}
