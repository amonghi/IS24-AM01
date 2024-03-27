package it.polimi.ingsw.am01.model.deserializers;

import it.polimi.ingsw.am01.model.card.face.corner.Corner;
import com.google.gson.*;
import it.polimi.ingsw.am01.model.collectible.Collectible;

import java.lang.reflect.Type;


public class CornerDeserializer implements JsonDeserializer<Corner>{
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
}
