package it.polimi.ingsw.am01.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Collectible;
import it.polimi.ingsw.am01.model.deserializers.*;
import it.polimi.ingsw.am01.model.objective.Objective;
import it.polimi.ingsw.am01.model.objective.PatternObjective;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides predetermined lists of {@link Objective} and {@link Card} divided by type.
 * Takes all the provided cards and objectives from relatives json files.
 *
 */
public class GameAssets {
    private static GameAssets instance = null;
    private final Map<Integer, Card> cards;
    private final List<Card> resourceCardList;
    private final List<Card> goldenCardList;
    private final List<Card> starterCardList;
    private final Map<Integer, Objective> objectives;
    private final List<Objective> objectiveList;


    /**
     * Initialize GameAssets reading the cards and objectives from their json files
     */
    private GameAssets() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        gsonBuilder.registerTypeAdapter(Points.class, new PointsDeserializer());
        gsonBuilder.registerTypeAdapter(Collectible.class, new CollectibleDeserializer());
        gsonBuilder.registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer());
        gsonBuilder.registerTypeAdapter(Objective.class, new ObjectiveDeserializer());
        gsonBuilder.registerTypeAdapter(PatternObjective.class, new PatternObjectiveDeserializer());
        Gson gson = gsonBuilder.create();

        Card[] cardArray = new Card[0];
        Objective[] objectiveArray = new Objective[0];

        // Read and parse cards.json
        try (InputStream inputStream = GameAssets.class.getResourceAsStream("/it/polimi/ingsw/am01/cards.json")) {
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                cardArray = gson.fromJson(reader, Card[].class);

            } else {
                System.err.println("File not found: cards.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read and parse objectives.json
        try (InputStream inputStream = GameAssets.class.getResourceAsStream("/it/polimi/ingsw/am01/objectives.json")) {
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                objectiveArray = gson.fromJson(reader, Objective[].class);

            } else {
                System.err.println("File not found: objectives.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resourceCardList = Stream.of(cardArray).filter(card -> !card.isStarter() && !card.isGold()).toList();
        goldenCardList = Stream.of(cardArray).filter(Card::isGold).toList();
        starterCardList = Stream.of(cardArray).filter(Card::isStarter).toList();
        objectiveList = List.of(objectiveArray);

        cards = Stream.of(cardArray).collect(Collectors.toMap(Card::id, card -> card));
        objectives = Stream.of(objectiveArray).collect(Collectors.toMap(Objective::getId, objective -> objective));
    }

    /**
     * Creates and saves a new instance of this class if it's the first time calling this method.
     * Otherwise, provides that saved instance
     *
     * @return an instance of GameAssets
     */
    public static GameAssets getInstance() {
        if (instance == null)
            instance = new GameAssets();
        return instance;
    }

    /**
     * Filters the saved cards and provides all the resource cards
     *
     * @return all the resource cards
     */
    public List<Card> getResourceCards() {
        return resourceCardList;
    }

    /**
     * Filters the saved cards and provides all the golden cards
     *
     * @return all the golden cards
     */
    public List<Card> getGoldenCards() {
        return goldenCardList;
    }

    /**
     * Filters the saved cards and provides all the starter cards
     *
     * @return all the starter cards
     */
    public List<Card> getStarterCards() {
        return starterCardList;
    }

    /**
     * Provides all the saved objectives
     *
     * @return all the objectives
     */
    public List<Objective> getObjectives() {
        return objectiveList;
    }

    /**
     * Find a card by its id
     * @param id the id of the card
     * @return the corresponding card if the id is valid, {@code Optional.empty()} otherwise
     */
    public Optional<Card> getCardById(int id) {
        return Optional.ofNullable(cards.get(id));
    }

    /**
     * Find an objective by its id
     * @param id the id of the objective
     * @return the corresponding objective if the id is valid, {@code Optional.empty()} otherwise
     */
    public Optional<Objective> getObjectiveById(int id) {
        return Optional.ofNullable(objectives.get(id));
    }
}