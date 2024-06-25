package it.polimi.ingsw.am01.model.game;

import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.Side;
import it.polimi.ingsw.am01.model.chat.BroadcastMessage;
import it.polimi.ingsw.am01.model.chat.DirectMessage;
import it.polimi.ingsw.am01.model.player.PlayerColor;
import it.polimi.ingsw.am01.model.player.PlayerManager;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    @Test
    void canSaveGames(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        int k = 10;
        for (int i = 0; i < k; i++) {
            assertDoesNotThrow(() -> gameManager.createGame(4));
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
            assertDoesNotThrow(() -> gameManager.saveGame(gameManager.createGame(4)));
        }
        gameManager.getGames().forEach(
                game -> {
                    PlayerProfile a = new PlayerProfile("a" + game.getId());
                    PlayerProfile b = new PlayerProfile("b" + game.getId());
                    PlayerProfile c = new PlayerProfile("c" + game.getId());
                    PlayerProfile d = new PlayerProfile("d" + game.getId());
                    List<PlayerProfile> playerProfiles = List.of(a, b, c, d);
                    assertDoesNotThrow(() -> {
                        for (PlayerProfile p : playerProfiles) {
                            game.join(p);
                        }
                        for (PlayerProfile p : playerProfiles) {
                            game.selectStartingCardSide(p, Side.BACK);
                        }
                        game.selectColor(a, PlayerColor.RED);
                        game.selectColor(b, PlayerColor.BLUE);
                        game.selectColor(c, PlayerColor.GREEN);
                        game.selectColor(d, PlayerColor.YELLOW);
                        for (PlayerProfile p : playerProfiles) {
                            game.selectObjective(p, game.getObjectiveOptions(p).stream().toList().getFirst());
                        }
                        game.getChatManager().send(new BroadcastMessage(a, "a"));
                        game.getChatManager().send(new DirectMessage(a, b, "a to b"));

                        PlayerProfile currentPlayer = game.getCurrentPlayer();
                        Card card = game.getPlayerData(currentPlayer).hand().getFirst();
                        game.placeCard(currentPlayer, card, Side.BACK, 0, 1);

                    });
                }
        );


        GameManager gm1 = new GameManager(dataDir);
        assertEquals(k, gm1.getGames().size());
    }

    @Test
    void canDeleteGames(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        int k = 10;
        int d = 5;
        for (int i = 0; i < k; i++) {
            assertDoesNotThrow(() -> gameManager.saveGame(gameManager.createGame(4)));
        }
        for (int i = 0; i < d; i++) {
            gameManager.deleteGame(gameManager.getGames().getFirst());
        }
        assertEquals(k - d, gameManager.getGames().size());

        File[] files = dataDir.toFile().listFiles();
        assertNotNull(files);
        assertEquals(k - d, files.length);
    }

    @Test
    void canGetGameById(@TempDir Path dataDir) {
        GameManager gameManager = new GameManager(dataDir);
        assertDoesNotThrow(() ->{
            Game createdGame = gameManager.createGame(3);
            Optional<Game> foundGame = gameManager.getGame(createdGame.getId());

            assertTrue(foundGame.isPresent());
            assertEquals(createdGame, foundGame.get());
        });
    }

    @Test
    void canGetGameByPlayer(@TempDir Path dataDir) {
        assertDoesNotThrow(() -> {
            PlayerManager playerManager = new PlayerManager();
            PlayerProfile player = playerManager.createProfile("Alice");

            GameManager gameManager = new GameManager(dataDir);
            Game game = gameManager.createGame(3);

            Optional<Game> found1 = gameManager.getGameWhereIsPlaying(player);
            assertTrue(found1.isEmpty());

            game.join(player);

            Optional<Game> found2 = gameManager.getGameWhereIsPlaying(player);
            assertTrue(found2.isPresent());
            assertEquals(game, found2.get());
        });
    }
}