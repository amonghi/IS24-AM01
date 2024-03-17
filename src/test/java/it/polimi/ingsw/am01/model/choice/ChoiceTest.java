package it.polimi.ingsw.am01.model.choice;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceTest {

    @Test
    void optionsDoNotChange() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);

        assertEquals(choice.getOptions(), options);
    }

    @Test
    void canMakeSelection() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);

        Optional<String> selected1 = choice.getSelected();
        assertTrue(selected1.isEmpty());

        choice.select("first");

        Optional<String> selected2 = choice.getSelected();
        assertTrue(selected2.isPresent());
        assertEquals(selected2.get(), "first");
    }

    @Test
    void cantChangeSelection() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);

        choice.select("first");

        assertThrows(DoubleChoiceException.class, () -> choice.select("second"));
    }

    @Test
    void cantSelectUnknownValue() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);
        assertThrows(NoSuchElementException.class, () -> choice.select("fourth"));
    }

}
