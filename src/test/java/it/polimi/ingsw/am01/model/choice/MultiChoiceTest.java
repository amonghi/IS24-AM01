package it.polimi.ingsw.am01.model.choice;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MultiChoiceTest {

    @Test
    void everyoneHasSameOptions() {
        Set<String> options = Set.of("red", "green", "blue");
        Set<String> choosers = Set.of("alice", "bob", "charlie");
        MultiChoice<String, String> multiChoice = new MultiChoice<>(options, choosers);

        assertEquals(multiChoice.getChoices().get("alice").getOptions(), options);
        assertEquals(multiChoice.getChoices().get("bob").getOptions(), options);
        assertEquals(multiChoice.getChoices().get("charlie").getOptions(), options);
    }

    @Test
    void selection() {
        Set<String> options = Set.of("red", "green", "blue");
        Set<String> choosers = Set.of("alice", "bob", "charlie");
        MultiChoice<String, String> multiChoice = new MultiChoice<>(options, choosers);

        assertEquals(multiChoice.getChoices().size(), 3);
        MultiChoice<String, String>.Choice a = multiChoice.getChoices().get("alice");
        MultiChoice<String, String>.Choice b = multiChoice.getChoices().get("bob");
        MultiChoice<String, String>.Choice c = multiChoice.getChoices().get("charlie");

        // nothing is selected, nothing is settled
        assertTrue(a.getSelected().isEmpty());
        assertTrue(b.getSelected().isEmpty());
        assertTrue(c.getSelected().isEmpty());

        assertFalse(a.isSettled());
        assertFalse(b.isSettled());
        assertFalse(c.isSettled());

        // make selections
        assertEquals(a.select("red"), SelectionResult.OK);
        assertEquals(b.select("green"), SelectionResult.OK);
        assertEquals(c.select("blue"), SelectionResult.OK);

        // everyone has selected, everything is settled
        assertTrue(a.isSettled());
        assertTrue(b.isSettled());
        assertTrue(c.isSettled());

        assertTrue(a.getSelected().isPresent());
        assertTrue(b.getSelected().isPresent());
        assertTrue(c.getSelected().isPresent());

        // no one can change selection after choice is settled
        assertThrows(ChoiceSettledException.class, () -> a.select("blue"));
        assertThrows(ChoiceSettledException.class, () -> b.select("blue"));
        assertThrows(ChoiceSettledException.class, () -> c.select("blue"));
    }

    @Test
    void contendSelection() {
        Set<String> options = Set.of("red", "green", "blue");
        Set<String> choosers = Set.of("alice", "bob");
        MultiChoice<String, String> multiChoice = new MultiChoice<>(options, choosers);

        MultiChoice<String, String>.Choice a = multiChoice.getChoices().get("alice");
        MultiChoice<String, String>.Choice b = multiChoice.getChoices().get("bob");

        assertEquals(a.select("red"), SelectionResult.OK);
        assertEquals(b.select("red"), SelectionResult.CONTENDED);

        assertEquals(b.getContenders("red"), Set.of("alice", "bob"));

        assertFalse(a.isSettled());
        assertFalse(b.isSettled());

        assertEquals(b.select("green"), SelectionResult.OK);

        assertTrue(a.isSettled());
        assertTrue(b.isSettled());
    }

    @Test
    void cantSelectUnknownValue() {
        Set<String> options = Set.of("red", "green", "blue");
        Set<String> choosers = Set.of("alice", "bob");
        MultiChoice<String, String> multiChoice = new MultiChoice<>(options, choosers);

        assertThrows(NoSuchElementException.class, () -> multiChoice.getChoices().get("alice").select("violet"));
    }

}
