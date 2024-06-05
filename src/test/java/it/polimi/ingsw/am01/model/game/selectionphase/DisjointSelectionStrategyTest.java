package it.polimi.ingsw.am01.model.game.selectionphase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisjointSelectionStrategyTest {
    private SelectionPhase<String, String> phase;

    @BeforeEach
    void init() {
        this.phase = new SelectionPhase<>(new DisjointSelectionStrategy<>(Map.of(
                "alice", new OptionsPool<>(Set.of("a", "b")),
                "bob", new OptionsPool<>(Set.of("c", "d"))
        )));
    }

    @Test
    void choosersGetAssignedTheCorrectPool() {
        assertEquals(Set.of("a", "b"), phase.getSelectorFor("alice").getOptions());
        assertEquals(Set.of("c", "d"), phase.getSelectorFor("bob").getOptions());
    }

    @Test
    void concludesWhenEveryoneHasChosen() {
        phase.getSelectorFor("alice").expressPreference("a");
        phase.getSelectorFor("bob").expressPreference("c");

        assertTrue(phase.isConcluded());
        assertEquals(Map.of(
                        "alice", "a",
                        "bob", "c"),
                phase.getResults()
        );
    }

    @Test
    void dropoutsAreRemovedFromSelectionPhase() {
        this.phase.getSelectorFor("alice").expressPreference("a");
        this.phase.getSelectorFor("alice").dropOut();
        this.phase.getSelectorFor("bob").expressPreference("c");

        assertTrue(phase.isConcluded());
        assertEquals(Map.of("bob", "c"), phase.getResults());
    }
}
