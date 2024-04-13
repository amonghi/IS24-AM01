package it.polimi.ingsw.am01.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.deserializers.*;
import it.polimi.ingsw.am01.model.objective.DifferentCollectibleObjective;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;
import it.polimi.ingsw.am01.model.objective.SameCollectibleObjective;
import it.polimi.ingsw.am01.model.player.PlayerProfile;
import it.polimi.ingsw.am01.model.serializers.*;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages multiple {@link Game} instances at once and allows to save the current status in a json file
 */
public class GameManager {
    private final List<Game> games;
    private int nextId;
    private final Path dataDir;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Corner.class, new CornerSerializer())
            .registerTypeAdapter(Points.class, new PointsSerializer())
            .registerTypeAdapter(SameCollectibleObjective.class, new ObjectiveSerializer())
            .registerTypeAdapter(DifferentCollectibleObjective.class, new ObjectiveSerializer())
            .registerTypeAdapter(PatternObjective.class, new ObjectiveSerializer())
            .registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintSerializer())
            .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerializer())
            .registerTypeAdapter(PlayArea.Position.class, new PositionSerializer())
            .registerTypeAdapter(Corner.class, new CornerDeserializer())
            .registerTypeAdapter(Points.class, new PointsDeserializer())
            .registerTypeAdapter(Collectible.class, new CollectibleDeserializer())
            .registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer())
            .registerTypeAdapter(Objective.class, new ObjectiveDeserializer())
            .registerTypeAdapter(PatternObjective.class, new PatternObjectiveDeserializer())
            .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerializer())
            .registerTypeAdapter(PlayArea.Position.class, new PositionSerializer())
            .create();

    /**
     * Creates a new {@code GameManager} and load all the saved games from file
     *
     * @param dataDir the path of the directory in which the saved games will be stored
     */
    public GameManager(Path dataDir) {
        this.games = new ArrayList<>();
        this.dataDir = dataDir;
        List<Integer> savedGamesIds = loadSavedGamesIds();
        nextId = savedGamesIds.stream().max(Comparator.naturalOrder()).map(n -> n + 1).orElse(0);
        for (int id : savedGamesIds) {
            games.add(loadGame(id));
        }
    }

    /**
     * @return a list of the games that are currently running
     */
    public List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    public Optional<Game> getGame(int id) {
        return this.games.stream()
                .filter(game -> game.getId() == id)
                .findAny();
    }

    public Optional<Game> getGameWhereIsPlaying(PlayerProfile pp) {
        // a player cannot be in more than one game at a time
        return this.games.stream()
                .filter(game -> game.getPlayerProfiles().equals(pp))
                .findFirst();
    }

    /**
     * Creates a new game
     *
     * @param maxPlayers the maximum amount of players allowed in that game
     * @return a reference to the created game
     */
    public Game createGame(int maxPlayers) {
        Game newGame = new Game(nextId, maxPlayers);
        nextId++;
        games.add(newGame);
        return newGame;
    }

    /**
     * Deletes a game and the relative saved file
     *
     * @param game a reference of the selected game
     */
    public void deleteGame(Game game) {
        games.remove(game);
        // Delete json if save() has been already called
        if (loadSavedGamesIds().contains(game.getId())) {
            File file = dataDir.resolve(game.getId() + ".json").toFile();
            boolean deleted = file.delete();
            if(!deleted){
                throw new RuntimeException("Failed to delete file " + game.getId() + ".json");
            }
        }
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
    public void saveGame(Game game) {
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
}
