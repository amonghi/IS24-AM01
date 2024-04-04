package it.polimi.ingsw.am01.model.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final List<Game> games;
    private int nextId;

    public GameManager(Path dataDir) {
        this.games = new ArrayList<>();
        nextId = 0;
        // TODO: ....
    }

    public List<Game> getGames() {
        return games;
    }

    public Game createGame(int maxPlayers) {
        Game newGame = new Game(nextId, maxPlayers);
        nextId++;
        games.add(newGame);
        return newGame;
    }

    private List<Integer> loadSavedGamesIds() {throw new UnsupportedOperationException("TODO");}

    private Game loadGame(int id){throw new UnsupportedOperationException("TODO");}

    public void saveGame(Game game){throw new UnsupportedOperationException("TODO");}
}
