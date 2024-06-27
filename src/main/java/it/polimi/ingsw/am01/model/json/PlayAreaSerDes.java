package it.polimi.ingsw.am01.model.json;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.exception.IllegalPlacementException;
import it.polimi.ingsw.am01.model.game.PlayArea;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * GSON serializer and deserializer for {@link PlayArea}
 */
public class PlayAreaSerDes implements JsonSerializer<PlayArea>, JsonDeserializer<PlayArea> {
    /**
     * {@inheritDoc}
     */
    @Override
    public PlayArea deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("cards").getAsJsonArray();

        List<PlayArea.CardPlacement> placements = new ArrayList<>();
        PlayArea.CardPlacement startingPlacement = null;
        for (JsonElement card : jsonArray) {
            PlayArea.CardPlacement placement = context.deserialize(card, PlayArea.CardPlacement.class);
            if (placement.getPosition().equals(PlayArea.Position.ORIGIN)) {
                startingPlacement = placement;
            } else {
                placements.add(placement);
            }
        }
        if (startingPlacement == null) {
            throw new JsonParseException("No starting card found");
        }

        PlayArea playArea = new PlayArea(startingPlacement.getCard(), startingPlacement.getSide());
        placements.sort(Comparator.comparing(PlayArea.CardPlacement::getSeq));
        for (PlayArea.CardPlacement placement : placements) {
            try {
                playArea.placeAt(placement.getPosition(), placement.getCard(), placement.getSide());
            } catch (IllegalPlacementException e) {
                throw new JsonParseException(e);
            }
        }
        return playArea;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonElement serialize(PlayArea playArea, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        playArea.getCards().values().forEach(card -> jsonArray.add(context.serialize(card, card.getClass())));

        jsonObject.add("cards", jsonArray);
        return jsonObject;
    }
}
