package it.polimi.ingsw.am01.model.choice;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiChoice<T, I> {
    private final Set<T> options;
    private final Map<I, Choice> choices;
    private boolean settled;

    public MultiChoice(Set<T> choices, Set<I> chooserIdentities) {
        this.options = Collections.unmodifiableSet(choices);
        this.choices = chooserIdentities.stream().collect(Collectors.toMap(Function.identity(), Choice::new));
        this.settled = false;
    }

    public Map<I, Choice> getChoices() {
        return Collections.unmodifiableMap(this.choices);
    }

    private Map<T, Set<I>> getContendersPerOption() {
        Map<T, Set<I>> map = new HashMap<>();

        for (T option : options) {
            Set<I> contenders = new HashSet<>();
            for (Map.Entry<I, Choice> entry : choices.entrySet()) {
                Optional<T> selected = entry.getValue().getSelected();
                if (selected.isPresent() && selected.get().equals(option)) {
                    contenders.add(entry.getKey());
                }
            }

            map.put(option, contenders);
        }

        return map;
    }

    private boolean everyoneHasChosen() {
        return choices.values().stream().allMatch(choice -> choice.getSelected().isPresent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MultiChoice{" +
                "options=" + options +
                ", choices=" + choices +
                ", settled=" + settled +
                '}';
    }

    public class Choice {
        private T selection;

        private Choice(I identity) {
            this.selection = null;
        }

        public Set<T> getOptions() {
            return options;
        }

        public SelectionResult select(T choice) {
            if (this.isSettled()) {
                throw new ChoiceSettledException();
            }
            if (!options.contains(choice)) {
                throw new NoSuchElementException("No such option in this choice");
            }
            this.selection = choice;

            Map<T, Set<I>> contendersPerChoice = getContendersPerOption();

            // the choice is settled if everyone has chosen and all the choices have only one or fewer contenders
            settled = everyoneHasChosen() && contendersPerChoice.values().stream().allMatch(contenders -> contenders.size() <= 1);
            return contendersPerChoice.get(choice).size() > 1 ? SelectionResult.CONTENDED : SelectionResult.OK;
        }

        public Optional<T> getSelected() {
            return Optional.ofNullable(this.selection);
        }

        public Set<I> getContenders(T option) {
            return getContendersPerOption().get(option);
        }

        public boolean isSettled() {
            return settled;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Choice{" +
                    "selection=" + selection +
                    '}';
        }
    }
}
