package it.polimi.ingsw.am01.serializers;

import com.google.gson.*;
import it.polimi.ingsw.am01.model.card.face.corner.Corner;

import java.lang.reflect.Type;

public class CornerSerializer implements JsonSerializer<Corner> {
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
