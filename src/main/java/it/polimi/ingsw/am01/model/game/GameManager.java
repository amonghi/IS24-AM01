package it.polimi.ingsw.am01.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.eventemitter.EventEmitter;
import it.polimi.ingsw.am01.eventemitter.EventEmitterImpl;
import it.polimi.ingsw.am01.eventemitter.EventListener;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.chat.Message;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.event.*;
import it.polimi.ingsw.am01.model.exception.IllegalGameStateException;
import it.polimi.ingsw.am01.model.exception.InvalidMaxPlayersException;
import it.polimi.ingsw.am01.model.exception.NotUndoableOperationException;
import it.polimi.ingsw.am01.model.json.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.player.PlayerProfile;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages multiple {@link Game} instances at once and allows to save the current status in a json file
 */
public class GameManager implements EventEmitter<GameManagerEvent> {

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerDes())
            .registerTypeAdapter(Corner.class, new CornerDeserializer())
            .registerTypeAdapter(Message.class, new ChatMessageSerDes())
            .registerTypeAdapter(Points.class, new PointsDeserializer())
            .registerTypeAdapter(Collectible.class, new CollectibleDeserializer())
            .registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer())
            .registerTypeAdapter(Card.class, new IDCardSerDes())
            .registerTypeAdapter(Objective.class, new IDObjectiveSerDes())
            .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerDes())
            .registerTypeAdapter(PlayArea.Position.class, new PositionSerDes())
            .registerTypeAdapter(Board.class, new BoardSerDes())
            .registerTypeAdapter(PlayArea.class, new PlayAreaSerDes())
            .create();
    private final EventEmitterImpl<GameManagerEvent> emitter;
    private final List<Game> games;
    private final Path dataDir;
    private final Map<Game, List<EventEmitter.Registration>> gamesRegistrations;
    private int nextId;

    /**
     * Creates a new {@code GameManager} and load all the saved games from file
     *
     * @param dataDir the path of the directory in which the saved games will be stored
     */
    public GameManager(Path dataDir) {
        this.emitter = new EventEmitterImpl<>();
        this.games = new ArrayList<>();
        this.dataDir = dataDir;
        this.gamesRegistrations = new HashMap<>();
        List<Integer> savedGamesIds = loadSavedGamesIds();
        nextId = savedGamesIds.stream().max(Comparator.naturalOrder()).map(n -> n + 1).orElse(0);
        for (int id : savedGamesIds) {
            Game game = loadGame(id);
            // Flag all players as disconnected
            game.getPlayerProfiles().forEach(p -> game.setPlayerConnection(p, false));
            try {
                // Undo last move if current player was in the middle of a turn
                if ((game.getStatus() == GameStatus.PLAY || game.getStatus() == GameStatus.SECOND_LAST_TURN || game.getStatus() == GameStatus.LAST_TURN)
                        && game.getTurnPhase() == TurnPhase.DRAWING) {
                    game.undoLastPlacement(game.getCurrentPlayer());
                }
            } catch (IllegalGameStateException | NotUndoableOperationException e) {
                throw new RuntimeException(e);
            }

            GameStatus status = game.getStatus();
            if (status == GameStatus.PLAY
                    || status == GameStatus.SECOND_LAST_TURN
                    || status == GameStatus.LAST_TURN
                    || status == GameStatus.SUSPENDED
                    || status == GameStatus.RESTORING) {
                try {
                    game.setRestoringStatus();
                } catch (IllegalGameStateException e) {
                    throw new RuntimeException(e); // TODO: handle exception
                }

                games.add(game);
                gamesRegistrations.put(game, List.of(
                        game.on(PlayerJoinedEvent.class, e -> this.playerJoinedInGame(e.player(), game)),
                        game.on(PlayerLeftEvent.class, e -> this.playerLeftFromGame(e.player(), game)),
                        game.on(GameEvent.class, e -> this.saveGame(game)),
                        game.on(GameClosedEvent.class, e -> this.deleteGame(game)),
                        game.on(AllPlayersJoinedEvent.class, this::updateGamesList)
                ));
            } else {
                // Delete game if server crashed before it was started
                deleteGame(game);
            }
        }
    }

    /**
     * @return an unmodifiable list of the games that are currently running
     */
    public synchronized List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    /**
     * Get a {@link Game} by its ID, if such a game exists
     *
     * @param id the id of the game
     * @return the game with the given ID, i such a game exists
     */
    public synchronized Optional<Game> getGame(int id) {
        return this.games.stream()
                .filter(game -> game.getId() == id)
                .findAny();
    }

    /**
     * Returns the game where a given player is currently playing, if such a game exists
     *
     * @param pp the player of the player whose current game we want to find
     * @return The game where the player is currently playing, if such a game exists
     */
    public synchronized Optional<Game> getGameWhereIsPlaying(PlayerProfile pp) {
        // a player cannot be in more than one game at a time
        return this.games.stream()
                .filter(game -> game.getPlayerProfiles().stream().anyMatch(profile -> profile.equals(pp)))
                .findFirst();
    }

    /**
     * Creates a new game
     *
     * @param maxPlayers the maximum amount of players allowed in that game
     * @return a reference to the created game
     */
    public synchronized Game createGame(int maxPlayers) throws InvalidMaxPlayersException {
        Game newGame = new Game(nextId, maxPlayers);
        nextId++;
        games.add(newGame);
        emitter.emit(new GameCreatedEvent(newGame));
        gamesRegistrations.put(newGame, List.of(
                newGame.on(PlayerJoinedEvent.class, e -> this.playerJoinedInGame(e.player(), newGame)),
                newGame.on(PlayerLeftEvent.class, e -> this.playerLeftFromGame(e.player(), newGame)),
                newGame.on(GameEvent.class, e -> this.saveGame(newGame)),
                newGame.on(GameClosedEvent.class, e -> this.deleteGame(newGame)),
                newGame.on(AllPlayersJoinedEvent.class, this::updateGamesList)
        ));
        return newGame;
    }

    /**
     * Emits a new {@link PlayerJoinedInGameEvent} given a player and a game
     *
     * @param player the player that joined the game
     * @param game   the game where the player joined
     */
    private void playerJoinedInGame(PlayerProfile player, Game game) {
        emitter.emit(
                new PlayerJoinedInGameEvent(
                        player, game
                )
        );
    }

    /**
     * Emits a new {@link GameStartedEvent} when all players joined a game
     *
     * @param event an {@link AllPlayersJoinedEvent} event
     */
    private void updateGamesList(AllPlayersJoinedEvent event) {
        emitter.emit(
                new GameStartedEvent()
        );
    }

    /**
     * Emits a new {@link PlayerLeftFromGameEvent} given a player and a game
     *
     * @param player the player that left the game
     * @param game   the game where the player left
     */
    private void playerLeftFromGame(PlayerProfile player, Game game) {
        emitter.emit(
                new PlayerLeftFromGameEvent(
                        player, game
                )
        );
    }

    /**
     * Deletes a game and the relative saved file
     *
     * @param game a reference of the selected game
     */
    public synchronized void deleteGame(Game game) {
        if (gamesRegistrations.containsKey(game)) {
            gamesRegistrations.get(game).forEach(game::unregister);
        }
        gamesRegistrations.remove(game);

        games.remove(game);
        // Delete json if save() has been already called
        if (loadSavedGamesIds().contains(game.getId())) {
            File file = dataDir.resolve(game.getId() + ".json").toFile();
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("Failed to delete file " + game.getId() + ".json");
            }
        }
        emitter.emit(new GameDeletedEvent(game.getId()));
    }

    /**
     * Provides all ids of games saved as files
     *
     * @return a list of ids of games saved
     */
    private List<Integer> loadSavedGamesIds() {
        File folder = dataDir.toFile();
        File[] containedFiles = folder.listFiles();
        if (containedFiles == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(containedFiles)
                .map(File::getName)
                .filter(name -> name.matches("\\d+\\.json"))
                .map(name -> name.substring(0, name.lastIndexOf(".json")))
                .map(Integer::parseInt)
                .toList();
    }

    /**
     * Loads a game from file and recovering its status
     *
     * @param id the id of the game
     * @return a reference to the loaded game
     */
    private Game loadGame(int id) {
        File file = dataDir.resolve(id + ".json").toFile();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String content = fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
            return deserializeGame(content);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file " + id + ".json");
        }
    }

    /**
     * Saves the status of a game into a file
     *
     * @param game a reference to the game
     */
    public synchronized void saveGame(Game game) {
        //noinspection ResultOfMethodCallIgnored
        dataDir.toFile().mkdir();
        File file = dataDir.resolve(game.getId() + ".json").toFile();
        try (Writer fileWriter = new FileWriter(file, false)) {
            fileWriter.write(serializeGame(game));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file " + game.getId() + ".json");
        }
    }

    /**
     * Serializes a {@link Game} object into a json string
     *
     * @param game a reference to the game
     * @return the serialized json
     */
    private String serializeGame(Game game) {
        return gson.toJson(game);
    }

    /**
     * Deserializes a json string into a {@link Game} object
     *
     * @param json the json string
     * @return a reference to the deserialized game
     */
    private Game deserializeGame(String json) {
        return gson.fromJson(json, Game.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Registration onAny(EventListener<GameManagerEvent> listener) {
        return emitter.onAny(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends GameManagerEvent> Registration on(Class<T> eventClass, EventListener<T> listener) {
        return emitter.on(eventClass, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregister(Registration registration) {
        return emitter.unregister(registration);
    }
}
