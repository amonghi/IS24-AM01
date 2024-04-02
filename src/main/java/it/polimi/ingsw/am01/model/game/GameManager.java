package it.polimi.ingsw.am01.model.game;

import java.nio.file.Path;
import java.util.List;

public class GameManager {
    private final List<Game> games;

    public GameManager(Path dataDir) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Game> getGames() {
        throw new UnsupportedOperationException("TODO");
    }

    public Game createGame(int maxPlayers) {
        throw new UnsupportedOperationException("TODO");
    }

    private List<String> loadSavedGamesIds() {throw new UnsupportedOperationException("TODO");}

    private Game loadGame(String id){throw new UnsupportedOperationException("TODO");}

    public void saveGame(Game game){throw new UnsupportedOperationException("TODO");}
}
