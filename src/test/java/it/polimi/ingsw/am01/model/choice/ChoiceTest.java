package it.polimi.ingsw.am01.model.choice;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
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

        choice.select("first");

        assertTrue(choice.hasSelected());
        assertEquals(choice.getSelected(), "first");
    }

    @Test
    void cantChangeSelection() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);

        choice.select("first");

        assertThrows(DoubleChoiceException.class, () -> choice.select("second"));
    }

    @Test
    void cantSelectUnknown() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);
        assertThrows(NoSuchElementException.class, () -> choice.select("fourth"));
    }

    @Test
    void cantGetSelectedBeforeSelection() {
        Set<String> options = Set.of("first", "second", "third");
        Choice<String> choice = new Choice<>(options);
        assertThrows(NoSuchElementException.class, () -> choice.getSelected());
    }

}
