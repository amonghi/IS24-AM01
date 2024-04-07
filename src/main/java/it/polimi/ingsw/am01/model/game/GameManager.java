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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {
    private final List<Game> games;
    private int nextId;
    private static final String dataDir = "./data";

    // TODO: add command line argument for dataDir
    public GameManager() {
        this.games = new ArrayList<>();
        List<Integer> savedGamesIds = loadSavedGamesIds();
        nextId = savedGamesIds.stream().max(Comparator.naturalOrder()).map(n -> n + 1).orElse(-1);
        for (int id : savedGamesIds) {
            games.add(loadGame(id));
        }
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

    private List<Integer> loadSavedGamesIds() {
        File folder = new File(dataDir);
        File[] containedFiles = folder.listFiles();
        if (containedFiles == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(containedFiles)
                .map(File::getName)
                .filter(name -> name.endsWith(".json"))
                .map(name -> name.substring(0, name.lastIndexOf(".json")))
                .filter(name -> name.matches("\\d+"))
                .map(Integer::parseInt)
                .toList();
    }

    private Game loadGame(int id) {
        File file = new File(dataDir + "/" + id + ".json");

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String content = fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
            return deserializeGame(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGame(Game game) {
        File file = new File(dataDir + "/" + game.getId() + ".json");
        file.getParentFile().mkdir();
        try (Writer fileWriter = new FileWriter(file, false)) {
            fileWriter.write(serializeGame(game));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeGame(Game game) {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Corner.class, new CornerSerializer())
                .registerTypeAdapter(Points.class, new PointsSerializer())
                .registerTypeAdapter(SameCollectibleObjective.class, new ObjectiveSerializer())
                .registerTypeAdapter(DifferentCollectibleObjective.class, new ObjectiveSerializer())
                .registerTypeAdapter(PatternObjective.class, new ObjectiveSerializer())
                .registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintSerializer())
                .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerializer())
                .registerTypeAdapter(PlayArea.Position.class, new PositionSerializer())
                .create();
        return gson.toJson(game);
    }

    private Game deserializeGame(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Corner.class, new CornerDeserializer())
                .registerTypeAdapter(Points.class, new PointsDeserializer())
                .registerTypeAdapter(Collectible.class, new CollectibleDeserializer())
                .registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer())
                .registerTypeAdapter(Objective.class, new ObjectiveDeserializer())
                .registerTypeAdapter(PatternObjective.class, new PatternObjectiveDeserializer())
                .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerializer())
                .registerTypeAdapter(PlayArea.Position.class, new PositionSerializer())
                .create();
        return gson.fromJson(json, Game.class);
    }
}
