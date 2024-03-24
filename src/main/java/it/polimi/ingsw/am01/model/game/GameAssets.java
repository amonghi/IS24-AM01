package it.polimi.ingsw.am01.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am01.model.card.Card;
import it.polimi.ingsw.am01.model.card.face.BackCardFace;
import it.polimi.ingsw.am01.model.card.face.FrontCardFace;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import it.polimi.ingsw.am01.model.card.face.placement.PlacementConstraint;
import it.polimi.ingsw.am01.model.card.face.points.Points;
import it.polimi.ingsw.am01.model.collectible.Item;
import it.polimi.ingsw.am01.model.collectible.Resource;
import it.polimi.ingsw.am01.model.deserializers.*;
import it.polimi.ingsw.am01.model.objective.Objective;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GameAssets {

    // TODO: divide into three json and three lists
    public static List<Card> getResourceCards() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        gsonBuilder.registerTypeAdapter(Item.class, new ItemDeserializer());
        gsonBuilder.registerTypeAdapter(Resource.class, new ResourceDeserializer());
        gsonBuilder.registerTypeAdapter(Points.class, new PointsDeserializer());
        gsonBuilder.registerTypeAdapter(Card.class, new CardDeserializer());
        gsonBuilder.registerTypeAdapter(BackCardFace.class, new BackCardFaceDeserializer());
        gsonBuilder.registerTypeAdapter(FrontCardFace.class, new FrontCardFaceDeserializer());
        gsonBuilder.registerTypeAdapter(PlacementConstraint.class, new PlacementConstraintDeserializer());
        Gson gson = gsonBuilder.create();

        String json;

        Card[] cards = new Card[0];
        try (InputStream inputStream = GameAssets.class.getResourceAsStream("/it/polimi/ingsw/am01/cards.json")) {
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                cards = gson.fromJson(reader, Card[].class);

            } else {
                System.err.println("File not found: cards.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of(cards);
    }

    public static List<Card> getGoldenCards() {
        throw new UnsupportedOperationException("TODO");
    }

    public static List<Card> getStarterCards() {
        throw new UnsupportedOperationException("TODO");
    }

    public static List<Objective> getObjectives() {
        throw new UnsupportedOperationException("TODO");
    }

    private GameAssets() {
        throw new UnsupportedOperationException("TODO");
    }
}