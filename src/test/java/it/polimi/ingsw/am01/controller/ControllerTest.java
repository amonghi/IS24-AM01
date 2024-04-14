package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    PlayerManager pm;
    GameManager gm;
    Controller controller;

    @BeforeEach
    void init(@TempDir Path dataDir) {
        this.pm = new PlayerManager();
        this.gm = new GameManager(dataDir);
        this.controller = new Controller(this.pm, this.gm);
    }

    @Test
    void authenticatesPlayer() {
        assertFalse(pm.getProfile("Alice").isPresent());

        PlayerProfile aProfile = controller.authenticate("Alice");
        assertEquals("Alice", aProfile.getName());

        assertTrue(pm.getProfile("Alice").isPresent());
    }

    @Test
    void cannotAuthenticateIfPlayerWithSameNameAlreadyExists() {
        controller.authenticate("Alice");
        assertTrue(pm.getProfile("Alice").isPresent());

        assertThrows(IllegalStateException.class, () -> controller.authenticate("Alice"));
    }

}