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

public class GameAssets {

    private static GameAssets instance = null;
    private final List<Card> cards;
    private final List<Objective> objectives;


    private GameAssets() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        gsonBuilder.registerTypeAdapter(Points.class, new PointsDeserializer());
        gsonBuilder.registerTypeAdapter(Collectible.class, new CollectibleDeserializer());
        gsonBuilder.registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer());
        gsonBuilder.registerTypeAdapter(Objective.class, new ObjectiveDeserializer());
        gsonBuilder.registerTypeAdapter(PatternObjective.class, new PatternObjectiveDeserializer());
        Gson gson = gsonBuilder.create();

        String json;

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


        cards = List.of(cardArray);
        objectives = List.of(objectiveArray);
    }

    public static GameAssets getInstance() {
        if (instance == null)
            instance = new GameAssets();
        return instance;
    }


    public List<Card> getResourceCards() {
        return cards.stream().filter(card -> !card.isStarter() && !card.isGold()).toList();
    }

    public List<Card> getGoldenCards() {
        return cards.stream().filter(Card::isGold).toList();
    }

    public List<Card> getStarterCards() {
        return cards.stream().filter(Card::isStarter).toList();
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

}