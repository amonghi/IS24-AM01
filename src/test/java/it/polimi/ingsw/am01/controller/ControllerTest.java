package it.polimi.ingsw.am01.controller;

import it.polimi.ingsw.am01.model.game.Game;
import it.polimi.ingsw.am01.model.game.GameManager;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    /**
     * Tests related to {@link Controller#authenticate(String)}
     */
    @Nested
    class Authenticate {
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

    /**
     * Tests related to {@link Controller#createAndJoinGame(int, String)}
     */
    @Nested
    class CreateAndJoinGame {
        PlayerProfile alice;

        @BeforeEach
        void init() {
            this.alice = controller.authenticate("Alice");
            assertTrue(pm.getProfile("Alice").isPresent());
            assertTrue(gm.getGames().isEmpty());
        }

        @Test
        void canCreateGame() {
            Game aGame = controller.createAndJoinGame(4, "Alice");
            assertEquals(1, gm.getGames().size());

            Optional<Game> gameWhereIsPlaying = gm.getGameWhereIsPlaying(alice);
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
            controller.createAndJoinGame(3, "Alice");
            assertEquals(1, gm.getGames().size());
            assertTrue(gm.getGameWhereIsPlaying(alice).isPresent());

            assertThrows(IllegalArgumentException.class, () -> controller.createAndJoinGame(4, "Alice"));
        }
    }

    /**
     * Tests related to {@link Controller#joinGame(int, String)}
     */
    @Nested
    class JoinGame {
        PlayerProfile alice;
        PlayerProfile bob;
        Game game;

        @BeforeEach
        void init() {
            this.alice = controller.authenticate("Alice");
            this.bob = controller.authenticate("Bob");
            this.game = controller.createAndJoinGame(4, "Alice");
            assertEquals(1, gm.getGames().size());
            assertEquals(1, game.getPlayerProfiles().size());
        }

        @Test
        void canJoinGame() {
            controller.joinGame(game.getId(), "Bob");
            assertEquals(2, game.getPlayerProfiles().size());
        }

        @Test
        void nonexistentPlayerCannotJoinGame() {
            assertTrue(pm.getProfile("Carlos").isEmpty());

            assertThrows(NoSuchElementException.class, () -> controller.joinGame(game.getId(), "Carlos"));
        }

        @Test
        void cannotJoinNonexistentGame() {
            assertThrows(NoSuchElementException.class, () -> controller.joinGame(1234, "Bob"));
        }

        @Test
        void cannotJoinGameIfAlreadyPlaying() {
            Game game2 = controller.createAndJoinGame(4, "Bob");

            assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game2.getId(), "Alice"));
            assertThrows(IllegalArgumentException.class, () -> controller.joinGame(game.getId(), "Bob"));
        }
    }
}
