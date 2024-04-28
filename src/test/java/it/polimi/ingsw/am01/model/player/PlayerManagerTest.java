package it.polimi.ingsw.am01.model.player;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerManagerTest {

    @Test
    void profileCreation() {
        PlayerManager pm = new PlayerManager();
        assertFalse(pm.getProfile("Alice").isPresent());

        PlayerProfile aProfile = pm.createProfile("Alice");
        assertEquals("Alice", aProfile.getName());

        assertTrue(pm.getProfile("Alice").isPresent());
    }

    @Test
    void cannotCreateSecondProfileWithSameName() {
        PlayerManager pm = new PlayerManager();
        pm.createProfile("Alice");
        assertTrue(pm.getProfile("Alice").isPresent());

        assertThrows(IllegalArgumentException.class, () -> pm.createProfile("Alice"));
    }

    @Test
    void profileDeletion() {
        PlayerManager pm = new PlayerManager();
        PlayerProfile aProfile = pm.createProfile("Alice");
        assertTrue(pm.getProfile("Alice").isPresent());

        pm.removeProfile(aProfile);
        assertFalse(pm.getProfile("Alice").isPresent());
    }

    @Test
    void cannotDeleteTwice() {
        PlayerManager pm = new PlayerManager();
        PlayerProfile aProfile = pm.createProfile("Alice");
        pm.removeProfile(aProfile);
        assertFalse(pm.getProfile("Alice").isPresent());

        assertThrows(NoSuchElementException.class, () -> pm.removeProfile(aProfile));
    }
}