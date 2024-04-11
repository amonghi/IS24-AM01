package it.polimi.ingsw.am01.model.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameManagerTest {
    @Test
    void canSaveGames(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        int k = 10;
        for (int i = 0; i < k; i++) {
            gameManager.createGame(4);
        }
        gameManager.getGames().forEach(gameManager::saveGame);
        File[] files = dataDir.toFile().listFiles();
        assertNotNull(files);
        assertEquals(k, files.length);
    }

    @Test
    void canLoadGames(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        int k = 10;
        for (int i = 0; i < k; i++) {
            gameManager.saveGame(gameManager.createGame(4));
        }
        GameManager gm1 = new GameManager(dataDir);
        assertEquals(k, gm1.getGames().size());
    }

    @Test
    void canDeleteGames(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        int k = 10;
        int d = 5;
        for (int i = 0; i < k; i++) {
            gameManager.saveGame(gameManager.createGame(4));
        }
        for (int i = 0; i < d; i++) {
            gameManager.deleteGame(gameManager.getGames().getFirst());
        }
        assertEquals(k - d, gameManager.getGames().size());

        File[] files = dataDir.toFile().listFiles();
        assertNotNull(files);
        assertEquals(k - d, files.length);
    }
}