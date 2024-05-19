package it.polimi.ingsw.am01.model.game.selectionphase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommonPoolSelectionStrategyTest {
    @Nested
    class OverlapAllowed {
        private SelectionPhase<String, String> phase;

        @BeforeEach
        void init() {
            this.phase = new SelectionPhase<>(new CommonPoolSelectionStrategy<>(
                    Set.of("alice", "bob"),
                    new OptionsPool<>(Set.of("a", "b", "c")),
                    false,
                    true
            ));
        }

        @Test
        void everyoneGetsTheSamePool() {
            assertEquals(Set.of("a", "b", "c"), phase.getSelectorFor("alice").getOptions());
            assertEquals(Set.of("a", "b", "c"), phase.getSelectorFor("bob").getOptions());
        }

        @Test
        void concludesWhenEveryoneHasChosen() {
            phase.getSelectorFor("alice").expressPreference("a");
            phase.getSelectorFor("bob").expressPreference("a");

            assertTrue(phase.isConcluded());
            assertEquals(Map.of(
                            "alice", "a",
                            "bob", "a"),
                    phase.getResults()
            );
        }

        @Test
        void dropoutsAreRemovedFromSelectionPhase() {
            this.phase.getSelectorFor("alice").dropOut();
            this.phase.getSelectorFor("bob").expressPreference("b");

            assertTrue(phase.isConcluded());
            assertEquals(Map.of("bob", "b"), phase.getResults());
        }
    }

    @Nested
    class OverlapNotAllowed {
        private SelectionPhase<String, String> phase;

        @BeforeEach
        void init() {
            this.phase = new SelectionPhase<>(new CommonPoolSelectionStrategy<>(
                    Set.of("alice", "bob"),
                    new OptionsPool<>(Set.of("a", "b", "c")),
                    true,
                    false
            ));
        }

        @Test
        void doesNotConcludeIfOverlapNotAllowed() {
            phase.getSelectorFor("alice").expressPreference("a");
            phase.getSelectorFor("bob").expressPreference("a");

            assertFalse(phase.isConcluded());
            assertThrows(IllegalStateException.class, phase::getResults);

            phase.getSelectorFor("bob").expressPreference("b");

            assertTrue(phase.isConcluded());
            assertEquals(Map.of(
                            "alice", "a",
                            "bob", "b"),
                    phase.getResults()
            );
        }

        @Test
        void dropoutsAreRemovedFromSelectionPhase() {
            this.phase.getSelectorFor("alice").dropOut();
            this.phase.getSelectorFor("bob").expressPreference("b");

            assertTrue(phase.isConcluded());
            assertEquals(Map.of("bob", "b"), phase.getResults());
        }
    }
}
