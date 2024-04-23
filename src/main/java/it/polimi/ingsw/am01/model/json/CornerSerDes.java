package it.polimi.ingsw.am01.model.json;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import com.google.gson.*;
import it.polimi.ingsw.am01.model.collectible.Collectible;

import java.lang.reflect.Type;


public class CornerSerDes implements JsonDeserializer<Corner>, JsonSerializer<Corner> {
    @Override
    public Corner deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        String value = jsonElement.getAsString();

        if ("EMPTY".equals(value)) {
            return Corner.empty();
        } else if ("NONE".equals(value)) {
            return Corner.missing();
        } else {
            Collectible collectible = context.deserialize(jsonElement, Collectible.class);
            return Corner.filled(collectible);
        }
    }

    @Override
    public JsonElement serialize(Corner corner, Type type, JsonSerializationContext context) {
        if (corner.getCollectible().isPresent()) {
            return new JsonPrimitive(corner.getCollectible().get().toString());
        }
        if (corner.isSocket()) {
            return new JsonPrimitive("EMPTY");
        }
        return new JsonPrimitive("NONE");
    }
}
