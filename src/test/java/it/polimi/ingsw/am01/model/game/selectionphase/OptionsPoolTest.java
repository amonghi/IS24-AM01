package it.polimi.ingsw.am01.model.game.selectionphase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionsPoolTest {
    private OptionsPool<String, Object> pool;

    @BeforeEach
    void init() {
        this.pool = new OptionsPool<>(Set.of("a", "b", "c"));
    }

    @Test
    void optionsDoNotChange() {
        assertEquals(Set.of("a", "b", "c"), pool.getOptions());
    }

    @Test
    void canExpressAndRemovePreference() {
        pool.addPreference("a", "alice");
        pool.addPreference("a", "bob");
        pool.addPreference("b", "bob");

        assertEquals(Set.of("alice", "bob"), pool.getWhoPrefers("a"));
        assertEquals(Set.of("bob"), pool.getWhoPrefers("b"));

        pool.removePreference("a", "bob");

        assertEquals(Set.of("alice"), pool.getWhoPrefers("a"));
    }

    @Test
    void cantSelectValueOutsideOfPool() {
        assertThrows(NoSuchElementException.class, () -> this.pool.addPreference("x", "alice"));
    }
}
