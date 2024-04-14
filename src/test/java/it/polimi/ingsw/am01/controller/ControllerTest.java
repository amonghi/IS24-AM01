package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Test
    void canCreateGame() {
        PlayerProfile aProfile = controller.authenticate("Alice");
        assertTrue(pm.getProfile("Alice").isPresent());
        assertTrue(gm.getGames().isEmpty());

        Game aGame = controller.createAndJoinGame(4, "Alice");
        assertEquals(1, gm.getGames().size());

        Optional<Game> gameWhereIsPlaying = gm.getGameWhereIsPlaying(aProfile);
        assertTrue(gameWhereIsPlaying.isPresent());
        assertEquals(aGame, gameWhereIsPlaying.get());
    }

    @Test
    void nonexistentPlayerCannotCreateGame() {
        assertTrue(pm.getProfile("Bob").isEmpty());
        assertThrows(NoSuchElementException.class, () -> controller.createAndJoinGame(3, "Bob"));
    }

    @Test
    void cannotCreateGameIfAlreadyPlaying() {
        PlayerProfile aProfile = controller.authenticate("Alice");
        controller.createAndJoinGame(3, "Alice");
        assertTrue(pm.getProfile("Alice").isPresent());
        assertTrue(gm.getGameWhereIsPlaying(aProfile).isPresent());

        assertThrows(IllegalArgumentException.class, () -> controller.createAndJoinGame(4, "Alice"));
    }

    @Test
    void canJoinGame() {
        controller.authenticate("Alice");
        controller.authenticate("Bob");
        Game game = controller.createAndJoinGame(4, "Alice");
        assertEquals(1, game.getPlayerProfiles().size());

        controller.joinGame(game.getId(), "Bob");
        assertEquals(2, game.getPlayerProfiles().size());
    }

    @Test
    void nonexistentPlayerCannotJoinGame() {
        controller.authenticate("Alice");
        controller.authenticate("Bob");
        Game game = controller.createAndJoinGame(4, "Alice");
        assertEquals(1, game.getPlayerProfiles().size());
        assertTrue(pm.getProfile("Carlos").isEmpty());

        assertThrows(NoSuchElementException.class, () -> controller.joinGame(game.getId(), "Carlos"));
    }

    @Test
    void cannotJoinNonexistentGame() {
        controller.authenticate("Alice");
        controller.authenticate("Bob");
        Game game = controller.createAndJoinGame(4, "Alice");
        assertEquals(1, game.getPlayerProfiles().size());

        assertThrows(NoSuchElementException.class, () -> controller.joinGame(1234, "Bob"));
    }

    @Test
    void cannotJoinGameIfAlreadyPlaying() {
        controller.authenticate("Alice");
        controller.authenticate("Bob");
        Game game = controller.createAndJoinGame(4, "Alice");
        Game game2 = controller.createAndJoinGame(4, "Bob");
        assertEquals(1, game.getPlayerProfiles().size());

        assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game2.getId(), "Alice"));
        assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game.getId(), "Bob"));
    }

}